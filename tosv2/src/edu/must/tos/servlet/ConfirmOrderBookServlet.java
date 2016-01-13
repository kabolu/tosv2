package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.Difference;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.OrderInfo;
import edu.must.tos.bean.Price;
import edu.must.tos.bean.Student;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.bean.VBookFeeForApp;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StudentBookDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.TransactionDAOImpl;
import edu.must.tos.impl.VBookFeeForAppDAOImpl;
import edu.must.tos.util.Calculate;

public class ConfirmOrderBookServlet extends HttpServlet {

	public ConfirmOrderBookServlet() {
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
		Connection conn = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			//每頁記錄數
			String num = "10";
			//分頁從第幾條記錄開始取數據
			String start = request.getParameter("start");
			if(start == null){
				start = "0";
			}
			HttpSession session = request.getSession();
		
			String oprType = request.getParameter("oprType");
		 
			String userId = (String)session.getAttribute("userId");
			String curIntake = (String)session.getAttribute("curIntake");
			List stuDetList = (List)session.getAttribute("stuDetList");
	
			
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			
			//當前學期匯率
			SysConfig curRate = new SysConfig();
			curRate.setScType(curIntake);
			curRate.setActInd("Y");
			String curInRMBRate = "1";
			String curInHKDRate = "1";
			List<SysConfig> curInRateList = sysConfigDAOImpl.getSysConfigList(conn, curRate);
			for(SysConfig config : curInRateList){
				if(config.getScKey()!=null&&"RMB".equals(config.getScKey())){
					curInRMBRate = config.getScValue1();
				}else if(config.getScKey()!=null&&"HKD".equals(config.getScKey())){
					curInHKDRate = config.getScValue1();
				}
			}
			request.setAttribute("curInRMBRate", curInRMBRate);
			request.setAttribute("curInHKDRate", curInHKDRate);
			
			if(oprType != null && oprType.equals("search")) {
				String studentNo = null;
				String applicantNo = null;
				if(request.getParameter("studentNo") != null && !request.getParameter("studentNo").trim().equals("null")){
					studentNo = request.getParameter("studentNo");
				}
				if(request.getParameter("applicantNo") != null && !request.getParameter("applicantNo").trim().equals("null")){
					applicantNo = request.getParameter("applicantNo");
				}
				String value = studentNo + "," + applicantNo;
				StudentDAOImpl stuImpl = new StudentDAOImpl();
				stuDetList =  stuImpl.showStudentDetail(conn, value);
				session.setAttribute("stuDetList", stuDetList);	

				StudentBookDAOImpl  studentBookDAOImpl = new StudentBookDAOImpl();
				String paidStatus = "N";
				int orderSeqNoParam = 0;
				List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, curIntake, paidStatus, orderSeqNoParam);
				session.setAttribute("orderedBookList", orderedBookList);
				request.setAttribute("oprType", oprType);
				request.getRequestDispatcher("buyBookList.jsp").forward(request, response);
			} else if (oprType != null && oprType.equals("checkReceiveOrder")){
				String studentNo = null;
				String applicantNo = null;
				if(request.getParameter("studentNo") != null && !request.getParameter("studentNo").trim().equals("null")){
					studentNo = request.getParameter("studentNo");
				}
				if(request.getParameter("applicantNo") != null && !request.getParameter("applicantNo").trim().equals("null")){
					applicantNo = request.getParameter("applicantNo");
				}
				String studParam = studentNo + "," + applicantNo;
				OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
				List<Order> orderList = orderDAOImpl.getAllOrderSeqNoInfo(conn, studParam, curIntake, "Y");
				
				//獲取該學生的vbookfeeforapp表的記錄
				VBookFeeForAppDAOImpl vbookfeeforappDAOImpl = new VBookFeeForAppDAOImpl();
				VBookFeeForApp v = new VBookFeeForApp();
				v.setStudentNo(studentNo);
				v.setApplicantNo(applicantNo);
				v.setPaidIntake(curIntake);
				VBookFeeForApp vbookfeeforapp = vbookfeeforappDAOImpl.getVBookFeeForAppInfo(conn, v);
				
				List<Difference> differList = Calculate.getDifferAmount(conn, studParam, curIntake, vbookfeeforapp.getPaidAmount());
				
				TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
				
				if(orderList.isEmpty()){
					out.print(0);	//no orders info
				} else {
					boolean rightFlag = false;
					String diffOrderSeqNo = "";
					for(Order o : orderList){
						int orderSeqNo = o.getOrderSeqNo();
						//檢查是否存在未繳交的重印收據罰款
						List rePrintFeeList = transactionDAOImpl.getTransactionByItem(conn, Arrays.asList(orderSeqNo), Arrays.asList("REPRINTFEE"), "M");
						if(rePrintFeeList != null && !rePrintFeeList.isEmpty()){
							rightFlag = false;
							break;
						}
						for(Difference d : differList){
							if(d.getOrderSeqNo() == orderSeqNo ){
								if(d.getDifference() == 0){
									rightFlag = true;
									break;
								}else{
									diffOrderSeqNo += orderSeqNo+",";
									break;
								}
							}
						}
					}
					
					if(rightFlag){
						out.print("".equals(diffOrderSeqNo)?1:diffOrderSeqNo);
					}else{
						out.print(2);
					}
				}
			} else if (oprType != null && oprType.equals("receive")) {
				String studentNo = null;
				String applicantNo = null;
				if(request.getParameter("studentNo") != null && !request.getParameter("studentNo").trim().equals("null")){
					studentNo = request.getParameter("studentNo");
				}
				if(request.getParameter("applicantNo") != null && !request.getParameter("applicantNo").trim().equals("null")){
					applicantNo = request.getParameter("applicantNo");
				}
				String value = studentNo + "," + applicantNo;
				
				String oprStep = request.getParameter("oprStep");
				if(oprStep != null) {
					request.setAttribute("oprStep", oprStep);
				}
				
				String result = "";
				if(request.getParameter("result") != null ){
					result = request.getParameter("result");
				}
								
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
		        boolean isNewStud = studentDAOImpl.isNewStudent(conn, stu);
		        
				
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
				
				StudentDAOImpl stuImpl = new StudentDAOImpl();
				stuDetList =  stuImpl.showStudentDetail(conn, value);
				session.setAttribute("stuDetList", stuDetList);
			 
				StudentBookDAOImpl  studentBookDAOImpl = new StudentBookDAOImpl();
				String paidStatus = "N";
				if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("receive")){
					paidStatus = "Y";
					request.setAttribute("payResult", "Y");
				}
				int orderSeqNoParam = 0;
				List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, curIntake, paidStatus, orderSeqNoParam);
				List orderedList = new ArrayList();
				if(paidStatus.equals("N")){
					if(orderedBookList != null && orderedBookList.size() == 4 ){
						List newBookList = new ArrayList();
						List newPriceList = new ArrayList();
						List newOrDetailList = new ArrayList();
						List newOrderList = new ArrayList();
						List bookList = (List)orderedBookList.get(0);
						List priceList = (List)orderedBookList.get(1);
						List qtyList = (List)orderedBookList.get(2);
						List oList = (List)orderedBookList.get(3);
						if(oList != null && !oList.isEmpty()){
							for(int i=0; i<oList.size(); i++){
								Book b = (Book)bookList.get(i);
								Price p = (Price)priceList.get(i);
								OrDetail od = (OrDetail)qtyList.get(i);
								Order o = (Order)oList.get(i);
								if( o.getPaidCurrency() == null ){
									newBookList.add(b);
									newPriceList.add(p);
									newOrDetailList.add(od);
									newOrderList.add(o);
								}
							}
							orderedList.add(newBookList);
							orderedList.add(newPriceList);
							orderedList.add(newOrDetailList);
							orderedList.add(newOrderList);
						}
					}
				} else {
					orderedList = orderedBookList;
				}
				
				double amerceMount = 0;
				
				//Order order = orderDAOImpl.getOrderInfo(conn, value, curIntake);
				Order order = null;
				
				SysConfig config = new SysConfig();
				config.setScType("FINERATE");
				config.setScKey("SHIPPING");
				config.setActInd("Y");
				SysConfig withdrawForCarry = sysConfigDAOImpl.getSysConfig(conn, config);
				double feePercent = 0;
				if(withdrawForCarry != null && withdrawForCarry.getScValue1() != null){
					feePercent = Double.parseDouble(withdrawForCarry.getScValue1());
				}
		        
				session.setAttribute("amerceMount", amerceMount);
				session.setAttribute("orderedBookList", orderedList);
				//session.setAttribute("orderedBookList", orderedBookList);
				request.setAttribute("oprType", oprType);
				request.setAttribute("order", order);
				request.setAttribute("amercePercent", amercePercent);
				request.setAttribute("feePercent", feePercent);
				request.setAttribute("shippingFee", shippingFee);
				
				if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("receive")){
					request.setAttribute("result", result.substring(0, result.length()-1));
					//領書版面
					request.getRequestDispatcher("receiveBookInfo.jsp").forward(request, response);
				} else {
					//收費版面
					request.getRequestDispatcher("receiveBookList.jsp").forward(request, response);
				}
				
			} else if (oprType != null && oprType.equals("checkOrder")) {
				String studentNo = null;
				String applicantNo = null;
				if(request.getParameter("studentNo") != null && !request.getParameter("studentNo").trim().equals("null")){
					studentNo = request.getParameter("studentNo");
				}
				if(request.getParameter("applicantNo") != null && !request.getParameter("applicantNo").trim().equals("null")){
					applicantNo = request.getParameter("applicantNo");
				}
				OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
				Order order1 = orderDAOImpl.getPaidNOrder(conn, studentNo, curIntake);
				Order order2 = orderDAOImpl.getPaidNOrder(conn, applicantNo, curIntake);
				if(order1 == null && order2 == null){
					out.print(0);	//沒有訂單記錄
				} else if (order1 != null && order2 != null){
					out.print(2);	//系統存在兩個訂單記錄
				} else if (order1 != null && order2 == null){
					//未付款訂單記錄
					if(order1.getPaidCurrency() != null){
						out.print(3);	//請到補差價版面處理
					}else {
						out.print(1);
					}
				} else if (order1 == null && order2 != null) {
					if (order2.getPaidCurrency() != null) {
						out.print(3);	//請到補差價版面處理
					} else {
						out.print(1);
					}
				}
			} else if (oprType != null && oprType.equals("checkReceiveDays")) {
				SysConfigDAOImpl sysConfigImpl = new SysConfigDAOImpl();
				//保管費
				SysConfig lateReceiveConfig = new SysConfig();
				lateReceiveConfig.setScType("FINERATE");
				lateReceiveConfig.setScKey("LATERECEIVE");
				lateReceiveConfig.setActInd("Y");
				SysConfig lateReceive = sysConfigImpl.getSysConfig(conn, lateReceiveConfig);
				
				SysConfig receiveConfig = new SysConfig();
				receiveConfig.setScType("RECEIVED");
				receiveConfig.setScKey(curIntake);
				receiveConfig.setActInd("Y");
				receiveConfig = sysConfigImpl.getSysConfig(conn, receiveConfig);
				
				DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
				Date receiveEnd = dt.parse(receiveConfig.getScValue2());
				Date now = new Date();
				long days = now.getTime()-receiveEnd.getTime();
				days = days/1000/60/60/24;
				
				if(Integer.parseInt(lateReceive.getScValue2()) < days){
					out.print(1);
				}else{
					out.print(0);
				}
			} else if (oprType != null && oprType.equals("CheckPayResult")) {
				String studentNo = null;
				String applicantNo = null;
				if(request.getParameter("studNo") != null && !request.getParameter("studNo").trim().equals("null")){
					studentNo = request.getParameter("studNo");
				}
				if(request.getParameter("applicantNo") != null && !request.getParameter("applicantNo").trim().equals("null")){
					applicantNo = request.getParameter("applicantNo");
				}
				String value = studentNo + "," + applicantNo;
				
				//检查是否已缴费
				OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
				//boolean isPaying = orderDAOImpl.isPaying(conn, value, curIntake);
				boolean isPaying = true;
				if(isPaying){
					out.print(1);
				} else {
					out.print(0);
				}
			} else if (oprType != null && oprType.equals("isReceived")){
				int orderSeqNo = 0;
				if(request.getParameter("orderSeqNo") != null){
					orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
				}
				String isbn = request.getParameter("isbn");
				OrDetail od = new OrDetail();
				od.setOrderSeqNo(orderSeqNo);
				od.setOrderIntake(curIntake);
				od.setIsbn(isbn);
				od.setActInd("Y");
				OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
				int isReceived = orderDetailDAOImpl.getReceivedBookStatus(conn, od);
				if(isReceived == 0){
					out.print(0);
				}else{
					out.print(1);
				}
			} else {	//==============================確認購買操作============================
				OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
				Student stu = (Student)stuDetList.get(0);
				String value = stu.getStudentNo() + "," + stu.getApplicantNo();
				
				Order db_order1 = orderDAOImpl.getPaidNOrderInfo(conn, stu.getStudentNo(), curIntake);
				Order db_order2 = orderDAOImpl.getPaidNOrderInfo(conn, stu.getApplicantNo(), curIntake);
				
				List<OrderInfo> selectedBookList = (List)session.getAttribute("selectedBookList");
				String confirmQty[] = request.getParameterValues("confirmQty");
				int status = 0;
				if (db_order1 != null && db_order2 == null) {	//存在以學生編號為訂書的訂單記錄
					status = 1;
					for(int i=0; i<selectedBookList.size(); i++) {
						OrderInfo info = (OrderInfo)selectedBookList.get(i);
						info.setStudentNo(stu.getStudentNo());
						info.setConfirmQty(Integer.parseInt(confirmQty[i]));
					}
				} else if (db_order1 == null && db_order2 != null) {	//存在以申請編號為訂書的訂單記錄
					status = 2;
					for(int i=0; i<selectedBookList.size(); i++) {
						OrderInfo info = (OrderInfo)selectedBookList.get(i);
						info.setStudentNo(stu.getApplicantNo());
						info.setConfirmQty(Integer.parseInt(confirmQty[i]));
					}
				} else if (db_order1 == null && db_order2 == null) {	//不存在以學生編號和申請編號為訂書的訂單記錄
					status = 3;
					for(int i=0; i<selectedBookList.size(); i++) {
						OrderInfo info = (OrderInfo)selectedBookList.get(i);
						info.setStudentNo(stu.getStudentNo()==null?stu.getApplicantNo():stu.getStudentNo());
						info.setConfirmQty(Integer.parseInt(confirmQty[i]));
					}
				}
				OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
				if(status == 0){
					request.setAttribute("message", "該學員同時具有兩個未付款的訂單記錄，請檢查！");
					request.getRequestDispatcher("orderBookerror.jsp").forward(request, response);
				} else {
					OrderInfo info = (OrderInfo)selectedBookList.get(0);
					Order o = new Order();
					o.setStudentNo(info.getStudentNo());
					o.setOrderIntake(curIntake);
					o.setOrderSeqNo(0);
					o.setPaidCurrency("");
					o.setPaidAmount(0);
					o.setPaidStatus("N");
					o.setActInd("Y");
					o.setCreUid(userId);
					o.setCreDate(new Date());
					o.setUpdUid(userId);
					o.setUpdDate(new Date());
					o.setAmerceMount(0);
					o.setFineforlatepay(0);
					o.setNetpaidcurrency("");
					o.setNetpaidamount(new Double(0));
					o.setCurrate(new Double(1));
					
					if(status == 3){	//若學生并沒有購買過圖書的情況
						int orderSeqNo = orderDAOImpl.addOrderSeqNoInfo(conn, o);
						List detailList = new ArrayList();
						for(int i=0; i<selectedBookList.size(); i++) {
							OrderInfo in = (OrderInfo)selectedBookList.get(i);
							
							OrDetail detail = new OrDetail();
							detail.setStudentNo(in.getStudentNo());
							detail.setOrderIntake(curIntake);
							detail.setOrderSeqNo(orderSeqNo);
							detail.setIsbn(in.getIsbn());
							detail.setConfirmQty(Integer.parseInt(confirmQty[i]));
							detail.setActInd("Y");
							detail.setCreUid(userId);
							detail.setCreDate(new Date());
							detail.setUpdUid(userId);
							detail.setUpdDate(new Date());
							detail.setCourseCode(in.getCourseCode());
							detail.setMajorCode(in.getMajorCode());
							detailList.add(detail);
						}
						boolean detailFlag = orderDetailDAOImpl.addDetailSeqNoInfo(conn, detailList);
						if(orderSeqNo != 0 && detailFlag){	//訂書訂單記錄保存成功操作
							conn.commit();
							StudentBookDAOImpl  studentBookDAOImpl = new StudentBookDAOImpl();
							String paidStatus = "N";
							int orderSeqNoParam = 0;
							List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, curIntake, paidStatus, orderSeqNoParam);
							session.setAttribute("orderedBookList", orderedBookList);
							session.removeAttribute("selectedBookList");
					
							request.getRequestDispatcher("buyBookList.jsp").forward(request, response);
							
						} else {
							conn.rollback();
							request.getRequestDispatcher("orderBookerror.jsp").forward(request, response);
						}
					} else {	//存在學生編號或申請編號的訂書訂單記錄
						int seqNo = 0;
						if (status == 1) {	//以學生編號為訂單的記錄
							seqNo = db_order1.getOrderSeqNo();
						} else if (status == 2) {	//以申請編號為訂單的記錄
							seqNo = db_order2.getOrderSeqNo();
						}
						List dList = new ArrayList();
						for(int i=0; i<selectedBookList.size(); i++) {
							OrderInfo in = (OrderInfo)selectedBookList.get(i);
							
							OrDetail detail = new OrDetail();
							detail.setStudentNo(in.getStudentNo());
							detail.setOrderIntake(curIntake);
							detail.setOrderSeqNo(seqNo);
							detail.setIsbn(in.getIsbn());
							detail.setConfirmQty(Integer.parseInt(confirmQty[i]));
							detail.setActInd("Y");
							detail.setCreUid(userId);
							detail.setCreDate(new Date());
							detail.setUpdUid(userId);
							detail.setUpdDate(new Date());
							detail.setCourseCode(in.getCourseCode());
							detail.setMajorCode(in.getMajorCode());
							dList.add(detail);
						}
						boolean detailFlag = orderDetailDAOImpl.addDetailSeqNoInfo(conn, dList);
						if (detailFlag) {
							conn.commit();
							StudentBookDAOImpl  studentBookDAOImpl = new StudentBookDAOImpl();
							String paidStatus = "N";
							int orderSeqNoParam = 0;
							List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, curIntake, paidStatus, orderSeqNoParam);
							session.setAttribute("orderedBookList", orderedBookList);
							session.removeAttribute("selectedBookList");
				
							request.getRequestDispatcher("buyBookList.jsp").forward(request, response);
						} else {
							conn.rollback();
							request.getRequestDispatcher("orderBookerror.jsp").forward(request, response);
						}
					}
					session.removeAttribute("selectedBookList");
				}
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

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
