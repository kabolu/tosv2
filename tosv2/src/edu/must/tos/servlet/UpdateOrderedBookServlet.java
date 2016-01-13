package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Student;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.bean.Withdraw;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StudentBookDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.WithdrawDAOImpl;

public class UpdateOrderedBookServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UpdateOrderedBookServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;    
		HttpSession session = request.getSession();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();	//獲取當前學期
			String key = "CURRINTAKE";
			String orderIntake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			//當前學期匯率
			SysConfig curRate = new SysConfig();
			curRate.setScType(orderIntake);
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
			
			//退書期
			String withdraw = request.getSession().getAttribute("WITHDRAW").toString();
			if(withdraw != null && "Y".equals(withdraw)){
				withdraw = "Y";
			}else{
				withdraw = "N";
			}
			
			String userId = (String)session.getAttribute("userId");
			String studentNo = null;
			String applicantNo = null;
			if(request.getParameter("studentNo") != null && !request.getParameter("studentNo").equals("null")){
				studentNo = request.getParameter("studentNo");
			}
			if(request.getParameter("applicantNo") != null && !request.getParameter("applicantNo").equals("null")){
				applicantNo = request.getParameter("applicantNo");
			}
			String value = studentNo + "," + applicantNo;
			
			String isPay = null;
			if(request.getParameter("paidStatus") != null && !request.getParameter("paidStatus").equals("")){
				isPay = request.getParameter("paidStatus");
			}
			
			StudentBookDAOImpl studentBookDAOImpl = new StudentBookDAOImpl();	//獲取已訂圖書信息
			int orderSeqNoParam = 0;
			if(request.getParameter("orderSeqNo") != null){
				orderSeqNoParam = Integer.parseInt(request.getParameter("orderSeqNo"));
			}
			String paidStatus = "";
			List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, orderIntake, paidStatus, orderSeqNoParam);
			
			StudentDAOImpl stuImpl = new StudentDAOImpl();
			List stuDetList =  stuImpl.showStudentDetail(conn, value);
			
			//判斷是否為新生，若是則不記錄退書數量值
			//academicyear=0 or academicyear is null
			//status is null or status in ('A','D','UR')
			StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
	        Student stu = new Student();
	        if(studentNo!=null){
	        	stu.setStudentNo(studentNo);
	        	stu.setApplicantNo(studentNo);
	        }else {
	        	stu.setStudentNo(applicantNo);
	        	stu.setApplicantNo(applicantNo);
	        }	        
	        boolean isNewStud = studentDAOImpl.isNewStudent(conn, stu);
	        
	        //退運時期檢查
	        SysConfig config = new SysConfig();
			config.setScType("FINERATE");
			config.setScKey("SHIPPING");
			config.setActInd("Y");
			SysConfig withdrawForCarry = sysConfigDAOImpl.getSysConfig(conn, config);
			
	        String scvalue2 = "";
	        if(withdrawForCarry!=null && withdrawForCarry.getScValue2()!=null){
	        	scvalue2 = withdrawForCarry.getScValue2()+" 00:00:00";
	        }
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date beginDate = df.parse(scvalue2);
	        Date now = new Date();
	        boolean isBeginDate = false;
	        if(now.after(beginDate)){
	        	isBeginDate = true;
	        }
	        
			String confirmQty[] = request.getParameterValues("confirmQty");
			String cause[] = new String[confirmQty.length];
			for(int i=0; i<confirmQty.length; i++){
				cause[i] = request.getParameter("cause"+i);
			}
			
			String withdrawQty[] = {"", ""};
			if("Y".equals(withdraw)) {
				withdrawQty = request.getParameterValues("newWithdrawQty");
			}
			
			List<Withdraw> withdrawList = new ArrayList<Withdraw>();
			List<OrDetail> qtyList = null;
			List<OrDetail> updOrdetailList = new ArrayList<OrDetail>();
			if(orderedBookList!=null) {
				qtyList = (List)orderedBookList.get(2); 
			}
			if(qtyList!=null) {
				for(int i=0; i<qtyList.size(); i++) {
					//確定訂書數
					OrDetail orDetail = (OrDetail)qtyList.get(i);
					//原確定數
					int confirm = orDetail.getConfirmQty();
					//修改後的確定數
					int m = Integer.parseInt(confirmQty[i]);
					if(cause[i]!=null && cause[i].equals("NoBookStorage")){	//無貨退書
						int withdrawQuantity = 0;
						if(orDetail.getNotEnoughQty() > 0){
							withdrawQuantity = orDetail.getNotEnoughQty();
							int differ = confirm - orDetail.getNotEnoughQty();
							orDetail.setConfirmQty(differ);
							if(differ == 0){
								orDetail.setNotEnoughQty(-1);
							}else{
								orDetail.setNotEnoughQty(0);
							}
						}else if(orDetail.getNotEnoughQty() < 0){
							withdrawQuantity = orDetail.getConfirmQty();
							orDetail.setConfirmQty(0);
							orDetail.setNotEnoughQty(-1);
						}
						orDetail.setUpdDate(new Date());
						orDetail.setUpdUid(userId);
						orDetail.setActInd("Y");
						
						if(orDetail.getConfirmQty()!=confirm ){
							updOrdetailList.add(orDetail);
						}
						
						Withdraw w = new Withdraw();
						w.setOrderIntake(orDetail.getOrderIntake());
						w.setOrderSeqNo(orDetail.getOrderSeqNo());
						w.setStudentNo(studentNo==null?applicantNo:studentNo);
						w.setWithdrawQty(withdrawQuantity);
						w.setIsbn(orDetail.getIsbn());
						w.setCause("無貨退書");
						w.setCreDate(new Date());
						w.setCreUid(userId);
						
						withdrawList.add(w);
					}else if(cause[i]!=null && cause[i].equals("StudentDo")){	//學生退書
						orDetail.setConfirmQty(Integer.parseInt(confirmQty[i]));
						//當為退書期時，保存退書數
						if(!isNewStud){
							int differ = confirm - m;
							//舊生處理
							if("Y".equals(withdraw) && isBeginDate){
								if(isPay!=null && !isPay.equals("N")){
									orDetail.setWithdrawQty(Integer.parseInt(withdrawQty[i]));
									if(differ >= 0){
										orDetail.setWithdrawQty2(orDetail.getWithdrawQty2()+differ);
									}
								}
							}else if("Y".equals(withdraw) && !isBeginDate){
								if(isPay!=null && !isPay.equals("N")){
									orDetail.setWithdrawQty(Integer.parseInt(withdrawQty[i]));
								}
							}else if(!"Y".equals(withdraw) && isBeginDate){
								if(isPay!=null && !isPay.equals("N")){
									if(differ >= 0){
										orDetail.setWithdrawQty(orDetail.getWithdrawQty()+differ);
										orDetail.setWithdrawQty2(orDetail.getWithdrawQty2()+differ);
									}
								}
							}
							if(orDetail.getNotEnoughQty() > 0){
								int notEnough = orDetail.getNotEnoughQty() - differ;
								if(orDetail.getConfirmQty() == 0){
									orDetail.setNotEnoughQty(-1);
								}else{
									orDetail.setNotEnoughQty(notEnough);
								}
								
							}
						}else{
							//新生不處理
							int differ = confirm - m;
							if(orDetail.getNotEnoughQty() > 0){
								int notEnough = orDetail.getNotEnoughQty() - differ;
								orDetail.setNotEnoughQty(notEnough);
							}
						}
						orDetail.setUpdDate(new Date());
						orDetail.setUpdUid(userId);
						orDetail.setActInd("Y");
						
						if(orDetail.getConfirmQty()!=confirm ){
							updOrdetailList.add(orDetail);
						}
						if(isPay!=null && !isPay.equals("N")){
							Withdraw w = new Withdraw();
							w.setOrderIntake(orDetail.getOrderIntake());
							w.setOrderSeqNo(orDetail.getOrderSeqNo());
							w.setStudentNo(studentNo==null?applicantNo:studentNo);
							w.setWithdrawQty((confirm-m));
							w.setIsbn(orDetail.getIsbn());
							w.setCause("學生退書");
							w.setCreDate(new Date());
							w.setCreUid(userId);
							withdrawList.add(w);
						}
					}
				}
			}
			request.setAttribute("withdraw", withdraw);
			request.setAttribute("stuDetList", stuDetList);
			request.setAttribute("orderSeqNo", orderSeqNoParam);
			request.setAttribute("curIntake", orderIntake);
			request.setAttribute("orderedBookList", orderedBookList);
			
			OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
			//更新有退書的記錄，原用qtyList則會更新全部detail的記錄
			boolean updateOrderDetail = true;
			if(!updOrdetailList.isEmpty()){
				updateOrderDetail = orderDetailDAOImpl.updateWithdrawInfo(conn, updOrdetailList);
			}
			
			OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
			//判斷是否已經付費，若在已付費后修改訂單書目數，將torder表的付費狀態設置為‘R’
			boolean setPaidStatusR = orderDAOImpl.setPaidStatusN(conn, orderSeqNoParam, orderIntake);
			
			WithdrawDAOImpl withdrawDAOImpl = new WithdrawDAOImpl();
			boolean updateWithdraw = true;
			if(!withdrawList.isEmpty()){
				for(Withdraw w : withdrawList){
					if(w.getWithdrawQty()>0){
						updateWithdraw = withdrawDAOImpl.addWithdraw(conn, w);
					}
					if(!updateWithdraw){
						updateWithdraw = false;
						break;
					}
				}
			}
			
			if( updateOrderDetail && setPaidStatusR && updateWithdraw) {
				conn.commit();
				//再查一次訂書信息
				List updatedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, orderIntake, "", orderSeqNoParam);
				request.setAttribute("orderedBookList", updatedBookList);
				SysConfig sysconfig = new SysConfig();
				sysconfig.setScKey("TERM");
				sysconfig.setScType("INTAKEPARAM");
				sysconfig.setActInd("Y");
				sysconfig = sysConfigDAOImpl.getSysConfig(conn, sysconfig);
				request.setAttribute("term", sysconfig.getScValue1());
				request.getRequestDispatcher("bookInvoice.jsp").forward(request, response);
			} else {
				conn.rollback();
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
