package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Order;
import edu.must.tos.bean.Period;
import edu.must.tos.bean.StudReceipt;
import edu.must.tos.bean.Student;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.bean.Transaction;
import edu.must.tos.bean.VBookFeeForApp;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.PeriodDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.TransactionDAOImpl;
import edu.must.tos.impl.VBookFeeForAppDAOImpl;
import edu.must.tos.receipt.StudReceiptInfo;

public class ViewReceiptServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ViewReceiptServlet() {
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
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Connection conn = null;    
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		try{
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
	        conn = ds.getConnection();
	        conn.setAutoCommit(false);
	        
	        OrderDetailDAOImpl orderDetailImpl = new OrderDetailDAOImpl();
	        SysConfigDAOImpl sysConfigImpl = new SysConfigDAOImpl();
	        
	        String userId = (String)session.getAttribute("userId");
	        
	        String studentNo = null;
	        String applicantNo = null;
	        if(request.getParameter("studentNo") != null && !request.getParameter("studentNo").trim().equals("null")){
	        	studentNo = request.getParameter("studentNo");
	        }
	        if(request.getParameter("applicantNo") != null && !request.getParameter("applicantNo").trim().equals("null")){
	        	applicantNo = request.getParameter("applicantNo");
	        }
	        String value = studentNo + "," + applicantNo;
	        
			String key = "CURRINTAKE";
			String curIntake = sysConfigImpl.getCurIntake(conn, key);
	        
	        String type = null;
	        if(request.getParameter("type") != null){
	        	type = request.getParameter("type");
	        }
	        if(type != null && type.equals("search")){
	        	List receiptCount = orderDetailImpl.getReceiptCount(conn, value, curIntake);
	        	if(!receiptCount.isEmpty()){
	        		if(receiptCount.size() == 1){
	        			Integer serilaNo = (Integer)receiptCount.get(0);
	        			out.print(serilaNo);
	        		}else{
	        			out.print(1);
	        		}
	        	}else{
	        		out.print(0);
	        	}
	        }
	        else if(type != null && type.equals("show")){
	        	showReceipt(conn, request, response, curIntake, studentNo, applicantNo);
	        }
	        else if(type != null && type.equals("reprint")){
	        	doRePrint(conn, curIntake, userId, request, response, value);
	        }
	        else if(type != null && type.equals("reReceiptFee")){
	        	doReReceiptFee(conn, curIntake, userId, request, response);
	        }
	        else{	//=================================打印收據版面=================================
	        	doPrintReceipt(conn, curIntake, userId, request, response, value, studentNo, applicantNo);
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
	
	private void showReceipt(Connection conn, HttpServletRequest request, HttpServletResponse response, 
			String curIntake, String studentNo, String applicantNo) throws Exception {
		int orderSeqNo = 0;
        //List<StudReceipt> receiptList = orderDetailImpl.getStudReceipt(conn, value, curIntake, orderSeqNo);
    	List receiptList = StudReceiptInfo.getStudReceiptList(conn, curIntake, studentNo+","+applicantNo, orderSeqNo, null);
        
        List printList = this.getPrintResult(receiptList);
        
        request.setAttribute("receiptList", printList);
        request.setAttribute("studentNo", studentNo);
        request.setAttribute("applicantNo", applicantNo);
        request.getRequestDispatcher("receiptList.jsp").forward(request, response);
	}

	private void doPrintReceipt(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response, String value, String studentNo, String applicantNo) throws Exception {
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		//退運費參數
    	double shippingFee = 0;
    	SysConfig shippingFeeConfig = new SysConfig();
    	shippingFeeConfig.setScType("SHIPPINGFEE");
    	shippingFeeConfig.setScKey("FEE");
    	shippingFeeConfig.setActInd("Y");
    	SysConfig conf = sysConfigDAOImpl.getSysConfig(conn, shippingFeeConfig);
    	if(conf != null && conf.getScValue1() != null){
    		shippingFee = Double.parseDouble(conf.getScValue1());
    	}
    	
        StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
        Student stu = new Student();
        stu.setStudentNo(studentNo);
        stu.setApplicantNo(applicantNo);
        //年級為0 或者為NULL
		//狀態為NULL 或者屬於'A'、'D'、'UR'
        boolean isNewStud = studentDAOImpl.isNewStudent(conn, stu);
        
        //當前學期匯率
		SysConfig curRate = new SysConfig();
		curRate.setScType(curIntake);
		curRate.setActInd("Y");
		String curInRMBRate = "1";
		String curInHKDRate = "1";
		List<SysConfig> curInRateList = sysConfigDAOImpl.getSysConfigList(conn, curRate);
		for(SysConfig config : curInRateList){
			if(config.getScKey() != null && "RMB".equals(config.getScKey())){
				curInRMBRate = config.getScValue1();
			}else if(config.getScKey() != null && "HKD".equals(config.getScKey())){
				curInHKDRate = config.getScValue1();
			}
		}        
        
        //逾期付款罰款計算
        float amercePercent = 0;
        String paid2 = request.getSession().getAttribute("PAID2").toString();
		if(paid2 != null && "Y".equals(paid2)  && !isNewStud){
			SysConfig sc = new SysConfig();
			sc.setScType("FINERATE");
			sc.setScKey("LATEPAY");
			sc.setActInd("Y");
			SysConfig paid2Period = sysConfigDAOImpl.getSysConfig(conn, sc);
			if(paid2Period != null && paid2Period.getScValue1() != null){
				amercePercent = Float.parseFloat(paid2Period.getScValue1());
			}
        }
        
        //領書時間
        PeriodDAOImpl periodImpl = new PeriodDAOImpl();
        Period receivedPeriod = periodImpl.getStudReceiveTime(conn, studentNo, curIntake);
        
        //是否重印
        String rePrint = "";
        if(request.getParameter("rePrint") != null){
        	rePrint = request.getParameter("rePrint");
        }
        
        int orderSeqNo = 0;
        if(request.getParameter("orderSeqNo") != null){
        	orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
        }
        
        double vbookfeeforappAmount = 0;
        double fineforlatepay = 0;
        
        OrderDAOImpl orderImpl = new OrderDAOImpl();
	    Order order = orderImpl.getOrderBySeqNo(conn, orderSeqNo);
	    fineforlatepay = order.getFineforlatepay();
	    
	    //重印收據的時候，需要使用order表上的shippingfee參數
	    if(!rePrint.equals("") && rePrint.equals("Y")){
	    	shippingFee = new BigDecimal(Double.toString(order.getShippingFee())).multiply(new BigDecimal(Double.toString(100))).doubleValue();
	    }
	    if(order != null && order.getPaidStatus().equals("R")){
	    	shippingFee = new BigDecimal(Double.toString(order.getShippingFee())).multiply(new BigDecimal(Double.toString(100))).doubleValue();
	    }
	    if(order != null && order.getPaidStatus().equals("N") && order.getPaidCurrency() != null){
	    	shippingFee = new BigDecimal(Double.toString(order.getShippingFee())).multiply(new BigDecimal(Double.toString(100))).doubleValue();
	    	//獲取該學生的vbookfeeforapp表的記錄
			VBookFeeForAppDAOImpl vbookfeeforappDAOImpl = new VBookFeeForAppDAOImpl();
			VBookFeeForApp v = new VBookFeeForApp();
			v.setStudentNo(studentNo);
			v.setApplicantNo(applicantNo);
			v.setPaidIntake(curIntake);
			VBookFeeForApp vbookfeeforapp = vbookfeeforappDAOImpl.getVBookFeeForAppInfo(conn, v);
	    	vbookfeeforappAmount = vbookfeeforapp.getPaidAmount();
	    }
	    String paidStatus = null;
	    if(order != null ){
	    	String studNo = value.split(",")[0]; 
	    	if(order.getStudentNo().equals(studNo)){
	    		value = order.getStudentNo() + "," + order.getStudentNo();
	    	}
	    	
	    	paidStatus = order.getPaidStatus();
	    }
	    
	    //List receiptList = orderDetailImpl.getStudReceipt(conn, value, curIntake, orderSeqNo);	//paidStatus is R and N
        
	    List receiptList = StudReceiptInfo.getStudReceiptList(conn, curIntake, value, orderSeqNo, null);
	    
	    //退運費
        List fineratePriceList = this.getWithdrawForCarryFee(conn, value, curIntake);
        double ratePercent = (Double)fineratePriceList.get(2);
	    
        if(receiptList != null && !receiptList.isEmpty() ){
        	request.setAttribute("userId", userId);
        	if(studentNo != null && !studentNo.equals("")){
        		request.setAttribute("studentNo", studentNo);
        	}else{
        		request.setAttribute("studentNo", applicantNo);
        	}
        	request.setAttribute("shippingFee", shippingFee);
        	request.setAttribute("fineforlatepay", fineforlatepay);
        	
        	request.setAttribute("scintake", curIntake);
        	request.setAttribute("receivedPeriod", receivedPeriod);
        	request.setAttribute("receiptList", receiptList);
        	request.setAttribute("amercePercent", amercePercent);
        	request.setAttribute("ratePercent", ratePercent);
        	request.setAttribute("rePrint", rePrint);
        	request.setAttribute("vbookfeeforappAmount", vbookfeeforappAmount);
        	
        	request.setAttribute("curInRMBRate", curInRMBRate);
			request.setAttribute("curInHKDRate", curInHKDRate);
        	
        	request.getRequestDispatcher("viewReceipt.jsp").forward(request, response);
        }else{
        	request.setAttribute("msg", "No Records!");
        	request.getRequestDispatcher("msg.jsp").forward(request, response);
        }
	}

	private void doReReceiptFee(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		TransactionDAOImpl transactionImpl = new TransactionDAOImpl();
		
    	String orderSeqNoParam = null;
    	if(request.getParameter("orderSeqNoParam") != null){
    		orderSeqNoParam = request.getParameter("orderSeqNoParam");
    	}
    	double rePrintFee = 0;
    	if(request.getParameter("feeValue") != null && !request.getParameter("feeValue").equals("0")){
    		rePrintFee = Double.parseDouble(request.getParameter("feeValue"));
    	}
    	String currencyValue = "";
    	if(request.getParameter("feeCurrency") != null && !request.getParameter("feeCurrency").equals("")){
    		currencyValue = request.getParameter("feeCurrency");
    	}
    	String studNo = "";
    	if(request.getParameter("studNo") != null && !request.getParameter("studNo").equals("")){
    		studNo = request.getParameter("studNo");
    	}
    	boolean addFlag = false;
    	if(orderSeqNoParam != null){
    		String orderSeqNo[] = orderSeqNoParam.split(",");
    		for(int i=0; i<orderSeqNo.length; i++){	        			
    			//將重印收據罰款記錄到到Ttransaction表內，設置status為‘M’，表示該筆記錄未收取費用狀態；
    			Transaction t = new Transaction();
    			t.setOrderSeqNo(orderSeqNo[i]);
    			t.setCashier(userId);
    			t.setPaidDate(new Date());
    			t.setPaidMentType("REPRINTFEE");
    			t.setPay(rePrintFee);
    			t.setPaidCurrency(currencyValue);
    			t.setStudentNo(studNo);
    			t.setStatus("M");
    			addFlag = transactionImpl.addTransactionInfo(conn, t);
    			if(!addFlag)
    				break;
    		}
    	}
    	if(addFlag){
    		conn.commit();
    		out.print(1);
    	}else{
    		conn.rollback();
    		out.print(0);
    	}
	}

	private void doRePrint(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response, String value) throws Exception {
		PrintWriter out = response.getWriter();
		OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String method = "";
    	if(request.getParameter("method") != null){
    		method = request.getParameter("method");
    	}
    	List<Order> orderList = orderDAOImpl.getAllOrderSeqNoInfo(conn, value, curIntake, "Y");
    	List list = new ArrayList();
    	if(!orderList.isEmpty()){
    		for(Order o : orderList){
    			if(o.getPaidStatus().equals("Y")){
    				list.add(o);
    			}
    		}
    	}
    	if(method.equals("searchRePrint")){
    		if(!list.isEmpty()){
        		out.print(1);
        	}else{
        		//No Order
        		out.print(0);
        	}
    	}else{
    		StudentDAOImpl stuImpl = new StudentDAOImpl();
			List stuDetList =  stuImpl.showStudentDetail(conn, value);
			request.setAttribute("stuDetList", stuDetList);
    		
			SysConfig sc = new SysConfig();
			sc.setScType("REPRINTFEE");
			sc.setScKey("FEE");
			sc.setActInd("Y");
			SysConfig sysconfig = sysConfigDAOImpl.getSysConfig(conn, sc);
			request.setAttribute("sysconfig", sysconfig);
			
    		request.setAttribute("rePrintList", list);
    		request.getRequestDispatcher("viewReReceipt.jsp").forward(request, response);
    	}
	}

	private List getPrintResult(List<StudReceipt> receiptList){
		List list = new ArrayList();
		HashSet set = new HashSet();
		try{
			for(StudReceipt s : receiptList){
				if(!set.contains(s.getOrderSeqno())){
					set.add(s.getOrderSeqno());
					list.add(s);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	private List getWithdrawForCarryFee(Connection conn, String studentNo, String curIntake){
		List list = new ArrayList();
		try{
			SysConfigDAOImpl sysconfigImpl = new SysConfigDAOImpl();
			OrderDetailDAOImpl orderDetailImpl = new OrderDetailDAOImpl();
			//int orderSeqNo = 0;
	        //List<StudReceipt> receiptList = orderDetailImpl.getStudReceipt(conn, studentNo, curIntake, orderSeqNo);
	        
	        SysConfig config = new SysConfig();
			config.setScType("FINERATE");
			config.setScKey("SHIPPING");
			config.setActInd("Y");
			SysConfig withdrawForCarry = sysconfigImpl.getSysConfig(conn, config);
	        double feePercent = 0;
	        if(withdrawForCarry != null && withdrawForCarry.getScValue1() != null){
	        	feePercent = Double.parseDouble(withdrawForCarry.getScValue1());
	        }
	        
	        double sumMop = 0, sumRmb = 0;
	        double mopPrice = 0, rmbPrice = 0;
//	        for(StudReceipt receipt : receiptList){
//	        	if(receipt.getMopNetPrice()==0){
//	        		mopPrice = receipt.getMopFuturePrice();
//	        	}else{
//	        		mopPrice = receipt.getMopNetPrice();
//	        	}
//	        	if(receipt.getRmbNetPrice()==0){
//	        		rmbPrice = receipt.getRmbFuturePrice();
//	        	}else{
//	        		rmbPrice = receipt.getRmbNetPrice();
//	        	}
//	        	BigDecimal withdrawForCarryFeeMop = new BigDecimal(Integer.toString(receipt.getWithDrawQty2())).multiply(new BigDecimal(Double.toString(mopPrice)));
//	        	BigDecimal withdrawForCarryFeeRmb = new BigDecimal(Integer.toString(receipt.getWithDrawQty2())).multiply(new BigDecimal(Double.toString(rmbPrice)));
//	        	
//	        	sumMop = (new BigDecimal(Double.toString(sumMop)).add(withdrawForCarryFeeMop)).doubleValue();
//	        	sumRmb = (new BigDecimal(Double.toString(sumRmb)).add(withdrawForCarryFeeRmb)).doubleValue();
//	        }
//	        sumMop = new BigDecimal(Double.toString(sumMop)).multiply(new BigDecimal(Double.toString(feePercent))).multiply(new BigDecimal(Double.toString(0.01))).doubleValue();
//	        sumRmb = new BigDecimal(Double.toString(sumRmb)).multiply(new BigDecimal(Double.toString(feePercent))).multiply(new BigDecimal(Double.toString(0.01))).doubleValue();
//	        //System.out.println("sumMop="+sumMop+"  sumRmb="+sumRmb);
//	        if(0<sumMop && sumMop<1){
//	        	sumMop = 1;
//	        }else{
//	        	sumMop = new BigDecimal(Double.toString(sumMop)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
//	        }
//	        if(0<sumRmb && sumRmb<1){
//	        	sumRmb = 1;
//	        }else{
//	        	sumRmb = new BigDecimal(Double.toString(sumRmb)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
//	        }
	        //System.out.println("sumMop="+sumMop+"  sumRmb="+sumRmb);
	        list.add(sumMop);
	        list.add(sumRmb);
	        list.add(feePercent);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
