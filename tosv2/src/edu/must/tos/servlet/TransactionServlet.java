package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.bean.Transaction;
import edu.must.tos.bean.TransactionOrderSumm;
import edu.must.tos.bean.TransactionSumm;
import edu.must.tos.bean.VBookFeeForAppBean;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.TransactionDAOImpl;
import edu.must.tos.impl.VBookFeeForAppDAOImpl;

public class TransactionServlet extends HttpServlet {

	public TransactionServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Connection conn = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
						
			String userId = (String)session.getAttribute("userId");
			String curIntake = (String)session.getAttribute("curIntake");
			
			String type = null;
			if (request.getParameter("type") != null) {
				type = request.getParameter("type");
			}
			if (type != null && "search".equals(type)) {
				searchRecd(conn, curIntake, userId, request, response);
			}
			else if (type != null && "orderPaySumm".equals(type)) {
				exportOrderPaySumm(conn, curIntake, userId, request, response);
			}
			else if (type != null && "studPaySumm".equals(type)) {
				exportStudPaySumm(conn, curIntake, userId, request, response);
			}
			else if (type != null && "removeSession".equals(type)) {
				removeSession(conn, request, response);
			}
			else if (type != null && "saveSession".equals(type)) {
				saveSession(conn, curIntake, userId, request, response);
			}
			else if (type != null && "editTransaction".equals(type)) {
				request.getRequestDispatcher("transactionEdit.jsp").forward(request, response);
			}
			else if (type != null && "searchRecord".equals(type)) {
				searchEditRecd(conn, curIntake, userId, request, response);
				request.getRequestDispatcher("transactionEdit.jsp").forward(request, response);
			} 
			else if (type != null && "cancelRecd".equals(type)) {
				cancelRecd(conn, curIntake, userId, request, response);
				request.getRequestDispatcher("transactionEdit.jsp").forward(request, response);
			}
			else {
				request.getRequestDispatcher("transaction.jsp").forward(request, response);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	private void cancelRecd(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String transactionNo = null;
		if(request.getParameter("transactionNo") != null){
			transactionNo = request.getParameter("transactionNo");
		}
		TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
		Transaction t = transactionDAOImpl.getTransaction(conn, Integer.parseInt(transactionNo));
		boolean flag = false;
		if("保管費".equals(t.getPaidMentType()) || "REPRINTFEE".equals(t.getPaidMentType()) || "調整金額".equals(t.getPaidMentType())){
			t.setStatus("N");
			t.setRemarks("Cancel by " + userId);
			flag = transactionDAOImpl.updTransactionInfo(conn, t);
		} else if ("補差價".equals(t.getPaidMentType())) {
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			SysConfig config = new SysConfig();
			config.setScType("EXCHANGERATE");
			config.setActInd("Y");
			List<SysConfig> sysConfigList = sysConfigDAOImpl.getSysConfigList(conn, config);
			Double hkdRate = new Double(0);
			Double rmbRate = new Double(0);
			for(SysConfig c : sysConfigList){
				if("HKD".equals(c.getScKey()))
					hkdRate = Double.parseDouble(c.getScValue1());
				if("RMB".equals(c.getScKey()))
					rmbRate = Double.parseDouble(c.getScValue1());
			}
			Double mopAmount = new Double(0);
			if("MOP".equals(t.getPaidCurrency())){
				mopAmount = t.getPay();								
			} else if ("HKD".equals(t.getPaidCurrency())) {
				mopAmount = new BigDecimal(t.getPay()).multiply(new BigDecimal(hkdRate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
			} else {
				mopAmount = new BigDecimal(t.getPay()).multiply(new BigDecimal(rmbRate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			t.setStatus("N");
			t.setRemarks("Cancel by " + userId);
			conn.setAutoCommit(false);
			flag = transactionDAOImpl.updTransactionInfo(conn, t);
			if(flag){
				OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
				Order o = orderDAOImpl.getOrderBySeqNo(conn, Integer.parseInt(t.getOrderSeqNo()));
				o.setDifference(new Double(0));
				o.setNetpaidamount(new BigDecimal(o.getNetpaidamount()).subtract(new BigDecimal(mopAmount)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
				if("MOP".equals(o.getPaidCurrency()))
					o.setPaidAmount(o.getNetpaidamount());
				if("HKD".equals(o.getPaidCurrency()))
					o.setPaidAmount(new BigDecimal(o.getNetpaidamount()).multiply(new BigDecimal(o.getCurrate())).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
				if("RMB".equals(o.getPaidCurrency()))
					o.setPaidAmount(new BigDecimal(o.getNetpaidamount()).multiply(new BigDecimal(o.getCurrate())).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
				
				o.setUpdUid(userId);
				o.setUpdDate(new Date());
				flag = orderDAOImpl.updateOrderBySeqNo(conn, o);
			}
		}
		if(flag){
			conn.commit();
			request.setAttribute("msg", "操作成功!");
		} else {
			conn.rollback();
			request.setAttribute("msg", "操作失敗!");
		}
	}

	private void searchEditRecd(Connection conn, String curIntake,
			String userId, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String fromDate = null;
		if(request.getParameter("fromDate") != null){
			fromDate = request.getParameter("fromDate");
		}
		String toDate = null;
		if(request.getParameter("toDate") != null){
			toDate = request.getParameter("toDate");
		}
		String orderSeqNo = null;
		if(request.getParameter("orderSeqNo") != null){
			orderSeqNo = request.getParameter("orderSeqNo");
		}
		String items = null;
		if(request.getParameter("items") != null){
			items = request.getParameter("items");
		}
		TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
		List list = transactionDAOImpl.getTransactionList(conn, fromDate, toDate, orderSeqNo, items);
		request.setAttribute("list", list);
	}

	private void saveSession(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		List tranList = null;
		if(request.getSession().getAttribute("tranList") != null){
			tranList = (List)request.getSession().getAttribute("tranList");
		}else{
			tranList = new ArrayList();
		}
		String orderSeqNo = null;
		if(request.getParameter("orderSeqNo") != null){
			orderSeqNo = request.getParameter("orderSeqNo");
		}
		String studNo = null;
		if(request.getParameter("studNo") != null){
			studNo = request.getParameter("studNo");
		}
		String appNo = null;
		if(request.getParameter("appNo") != null){
			appNo = request.getParameter("appNo");
		}
		String item = null;
		if(request.getParameter("item") != null){
			item = request.getParameter("item");
		}
		String currency = null;
		if(request.getParameter("currency") != null){
			currency = request.getParameter("currency");
		}
		String cause = null;
		if(request.getParameter("cause") != null){
			cause = request.getParameter("cause");
		}
		String remarks = null;
		if(request.getParameter("remarks") != null){
			remarks = request.getParameter("remarks");
		}
		String i = null;
		if(request.getParameter("i") != null){
			i = request.getParameter("i");
		}
//		String rmbRate = null;
//		if(request.getParameter("rmbRate") != null){
//			rmbRate = request.getParameter("rmbRate");
//		}
//		String hkdRate = null;
//		if(request.getParameter("hkdRate") != null){
//			hkdRate = request.getParameter("hkdRate");
//		}
		

		
//		Double causeRmb = new BigDecimal(Double.parseDouble(cause)).multiply(new BigDecimal(Double.parseDouble(rmbRate))).doubleValue();
//		Double causeHkd = new BigDecimal(Double.parseDouble(cause)).multiply(new BigDecimal(Double.parseDouble(hkdRate))).doubleValue();

        
		Transaction t = new Transaction();
		t.setTransactionNo(Integer.parseInt(i));
		t.setOrderSeqNo(orderSeqNo);
		t.setCashier(userId);
		t.setPaidMentType(item);
		t.setPay(Double.parseDouble(cause));
		t.setPaidCurrency(currency);
		t.setRemarks(remarks);
		t.setStatus("Y");
		t.setStudentNo(studNo==null?appNo:studNo);
		
		tranList.add(t);
		request.getSession().setAttribute("tranList", tranList);
		
	}

	private void removeSession(Connection conn, HttpServletRequest request,
			HttpServletResponse response) {
		List tranList = (List)request.getSession().getAttribute("tranList");
		String i = null;
		if(request.getParameter("i") != null){
			i = request.getParameter("i");
		}
		for(int j=0; j<tranList.size(); j++){
			Transaction t = (Transaction)tranList.get(j);
			if(t.getTransactionNo()==Integer.parseInt(i)){
				tranList.remove(j);
			}
		}
		request.getSession().setAttribute("tranList", tranList);
	}

	private void exportStudPaySumm(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
		VBookFeeForAppDAOImpl vBookFeeForAppDAOImpl = new VBookFeeForAppDAOImpl();
		OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
		
		String fromDate = "";
		if(request.getParameter("fromDate") != null){
			fromDate = request.getParameter("fromDate")+" 00:00:00";
		}
		String toDate = "";
		if(request.getParameter("toDate") != null){
			toDate = request.getParameter("toDate")+" 23:59:59";
		}
		List studPaySummList = transactionDAOImpl.getTransStudPaySumm(conn, fromDate, toDate);
		List transSummList = transactionDAOImpl.getTransSumm(conn, fromDate, toDate);
		List accuSummList = vBookFeeForAppDAOImpl.getVBookFeeForAppAccuSumm(conn, curIntake);
		List orderNumList = null;
		if(accuSummList != null && !accuSummList.isEmpty()){
			orderNumList = orderDAOImpl.getOrderSumm(conn, curIntake, fromDate, toDate);
		}else{					
			List<Order> orderSumList = orderDAOImpl.getAllOrderSeqNoInfo(conn, null, curIntake, "Y");
			HashSet studSum = new HashSet();
			if(orderSumList != null && !orderSumList.isEmpty()){
				for(Order o : orderSumList){
					if(!studSum.contains(o.getStudentNo())){
						studSum.add(o.getStudentNo());
					}
				}
			}
			orderNumList = new ArrayList();
			int[] num1 = new int[2];
			num1[0] = 0;
			num1[1] = 0;
			int[] num2 = new int[2];
			num2[0] = orderSumList.size();
			num2[1] = studSum.size();
			orderNumList.add(num1);
			orderNumList.add(num2);
		}
		
		if(studPaySummList != null && !studPaySummList.isEmpty() ){
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition" , "attachment ; filename = StudPaySummInfo.xls " );
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			
			WritableSheet worksheet = workbook.createSheet("學生收費匯總", 0);
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			cellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
			
			String[] title = {"學生編號/申請編號","學生姓名","付款幣種","付款金額"};
			
			for(int i=0;i<title.length;i++){
				 Label titleLabel = new Label(i, 0, title[i], cellFormat);
				 worksheet.addCell(titleLabel);
			}
			for(int i=0; i<studPaySummList.size(); i++){
				int j = 0;
				TransactionSumm record = (TransactionSumm)studPaySummList.get(i);
				worksheet.addCell(new Label(j++, i+1, record.getStudentNo()));
				worksheet.addCell(new Label(j++, i+1, record.getStudentName()));
				worksheet.addCell(new Label(j++, i+1, record.getPaidCurrency()));
				worksheet.addCell(new jxl.write.Number(j++, i+1, record.getPay()));
			}
			
			int rows = studPaySummList.size()+2;
			
			//summary info
			worksheet.addCell(new Label(0, rows, "訂書中心總收金額("+fromDate+"~"+toDate+")"));
			if(transSummList!=null && !transSummList.isEmpty()){
				for(int i=0; i<transSummList.size(); i++){
					int j = 0;
					TransactionSumm record = (TransactionSumm)transSummList.get(i);
					worksheet.addCell(new Label(i+1, rows+(j++), record.getPaidCurrency()));
					worksheet.addCell(new jxl.write.Number(i+1, rows+(j++), record.getPay()));
				}
			}else{
				worksheet.addCell(new Label(1, rows, "沒有記錄！"));
			}
			
			rows = rows + 2 + 1;
			worksheet.addCell(new Label(0, rows, "會計處代收金額"));
			if(accuSummList != null && !accuSummList.isEmpty()){
				for(int i=0; i<accuSummList.size(); i++){
					int j = 0;
					VBookFeeForAppBean record = (VBookFeeForAppBean)accuSummList.get(i);
					worksheet.addCell(new Label(i+1, rows+(j++), record.getPaidCurrency()));
					worksheet.addCell(new jxl.write.Number(i+1, rows+(j++), record.getPaidAmount()));
				}
			}else{
				worksheet.addCell(new Label(1, rows, "沒有記錄！"));
			}
			
			rows = rows + 2 +1;
			worksheet.addCell(new Label(0, rows, "本學期訂單總數"));
			if(orderNumList != null && !orderNumList.isEmpty()){
				int[] record1 = (int[])orderNumList.get(0);
				int a = record1[0];
				int b = record1[1];
				int[] record2 = (int[])orderNumList.get(1);
				int c = record2[0] - a;
				int d = record2[1] - b;
				
				worksheet.addCell(new Label(1, rows, "新生訂單總數(包括延遲入學、研究生)"));
				worksheet.addCell(new jxl.write.Number(1, rows+1, a));
				worksheet.addCell(new Label(2, rows, "舊生訂單總數"));
				worksheet.addCell(new jxl.write.Number(2, rows+1, c));
				
				rows = rows + 2 + 1;
				worksheet.addCell(new Label(0, rows, "本學期訂書總人數"));
				worksheet.addCell(new Label(1, rows, "新生人總數(包括延遲入學、研究生)"));
				worksheet.addCell(new jxl.write.Number(1, rows+1, b));
				worksheet.addCell(new Label(2, rows, "舊生總人數"));
				worksheet.addCell(new jxl.write.Number(2, rows+1, d));
			}else{
				worksheet.addCell(new Label(1, rows, "沒有記錄！"));
			}
			
			workbook.write();
			workbook.close();
		    
			os.flush();
			os.close();
		}else{
			request.setAttribute("flag", "false");
			request.getRequestDispatcher("transaction.jsp").forward(request, response);
		}
	}

	private void exportOrderPaySumm(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
		
		List orderSummList = transactionDAOImpl.getOrderPaySummList(conn, curIntake);
		if(orderSummList != null && !orderSummList.isEmpty()){
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition" , "attachment ; filename = OrderPaySummary.xls " );
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet worksheet = workbook.createSheet("書單收費匯總", 0);
			
			String[] title = {"學生編號/申請編號","訂單編號","總繳費(MOP)","總繳費(RMB)"};
			
			for(int i=0; i<title.length; i++){
				 Label titleLabel = new Label(i, 0, title[i]);
				 worksheet.addCell(titleLabel);
			}
			for(int i=0; i<orderSummList.size(); i++){
				TransactionOrderSumm ts = (TransactionOrderSumm)orderSummList.get(i);
				int j = 0;
				worksheet.addCell(new Label(j++, i+1, ts.getStudentNo()));
				worksheet.addCell(new Label(j++, i+1, ts.getOrderSeqNo()));
				worksheet.addCell(new jxl.write.Number(j++, i+1, ts.getMopAmount()));
				worksheet.addCell(new jxl.write.Number(j++, i+1, ts.getRmbAmount()));
			}
			workbook.write();
			workbook.close();
		    
			os.flush();
			os.close();
		}else{
			request.setAttribute("flag", "false");
			request.getRequestDispatcher("transaction.jsp").forward(request, response);
		}
	}

	private void searchRecd(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
		
		String fromDate = "";
		if(request.getParameter("fromDate") != null){
			fromDate = request.getParameter("fromDate") + " 00:00:00";
		}
		String toDate = "";
		if(request.getParameter("toDate") != null){
			toDate = request.getParameter("toDate") + " 23:59:59";
		}
		//第一個sheet列出有關交易項目各項收費信息
		List resultList = transactionDAOImpl.getTransactionInfo(conn, fromDate, toDate);
		//第二個sheet列出以收款人統計出所收的金額和訂單數
		List summaryList = transactionDAOImpl.getTransactionSumm(conn, fromDate, toDate);
		if(resultList != null && !resultList.isEmpty() && summaryList != null && !summaryList.isEmpty()){
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition" , "attachment ; filename = TransactionInfo.xls " );
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			
			//the first sheet content
			WritableSheet worksheet = workbook.createSheet("交易項目信息表", 0);
			//table css
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			cellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
			
			String[] title = {"操作日期","訂單號/學員編號","學生編號","學生姓名","訂書冊數","退訂冊數","交易項目","付款幣種","付款金額","收款人","備註"};
			
			for(int i=0; i<title.length; i++){
				 Label titleLabel = new Label(i, 0, title[i], cellFormat);
				 worksheet.addCell(titleLabel);
			}
			DateFormat customDateFormat = new DateFormat ("yyyy-MM-dd hh:mm:ss"); 
			WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat);
			
			for(int i=0; i<resultList.size(); i++){
				int j = 0;
				Transaction record = (Transaction)resultList.get(i);
				worksheet.addCell(new DateTime(j++, i+1, (Date)record.getPaidDate(), dateFormat));
				worksheet.addCell(new Label(j++, i+1, record.getOrderSeqNo()));
				worksheet.addCell(new Label(j++, i+1, record.getStudentNo()));
				worksheet.addCell(new Label(j++, i+1, record.getChineseName()));
				int confirmQty = 0;
				int withdrawQty = 0;
				if("收費".equals(record.getPaidMentType())){
					confirmQty = record.getConfirmQty();
					withdrawQty = record.getWithdrawQty();
				}
				worksheet.addCell(new jxl.write.Number(j++, i+1, confirmQty));
				worksheet.addCell(new jxl.write.Number(j++, i+1, withdrawQty));
				worksheet.addCell(new Label(j++, i+1, record.getPaidMentType()));				
				worksheet.addCell(new Label(j++, i+1, record.getPaidCurrency()));
				worksheet.addCell(new jxl.write.Number(j++, i+1, record.getPay()));
				worksheet.addCell(new Label(j++, i+1, record.getCashier()));
				worksheet.addCell(new Label(j++, i+1, record.getRemarks()));
			}
			
			//the second sheet content
			WritableSheet worksheet1 = workbook.createSheet("交易項目信息表-(匯總)", 1);
			String[] title1 = {"操作日期","收款人","付款幣種","付款總金額","單數"};
			
			for(int i=0; i<title1.length; i++){
				 Label titleLabel = new Label(i, 0, title1[i], cellFormat);
				 worksheet1.addCell(titleLabel);
			}					
			for(int i=0; i<summaryList.size(); i++){
				int j = 0;
				TransactionSumm record = (TransactionSumm)summaryList.get(i);
				worksheet1.addCell(new Label(j++, i+1, record.getPaidDate()));
				worksheet1.addCell(new Label(j++, i+1, record.getCashier()));
				worksheet1.addCell(new Label(j++, i+1, record.getPaidCurrency()));
				worksheet1.addCell(new jxl.write.Number(j++, i+1, record.getPay()));
				worksheet1.addCell(new jxl.write.Number(j++, i+1, record.getCountNum()));
			}
			
			workbook.write();
			workbook.close();
		    
			os.flush();
			os.close();
		}else{
			request.setAttribute("flag", "false");
			request.getRequestDispatcher("transaction.jsp").forward(request, response);
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
