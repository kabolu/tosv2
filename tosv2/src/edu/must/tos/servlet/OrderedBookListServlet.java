package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.must.tos.bean.Order;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.StudentBookDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;

public class OrderedBookListServlet extends HttpServlet {

	public OrderedBookListServlet() {
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
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String oprType  = request.getParameter("oprType");
			String studentNo = null;
			String applicantNo = null;			
			if(request.getParameter("studentNo")!=null && !request.getParameter("studentNo").equals("null")){
				studentNo = request.getParameter("studentNo");
			}
			if(request.getParameter("applicantNo")!=null && !request.getParameter("applicantNo").equals("null")){
				applicantNo = request.getParameter("applicantNo");
			}
			String value = studentNo+","+applicantNo;
			
			int orderSeqNoParam = 0;
			
			
			
			if(request.getParameter("orderSeqNo")!=null){
				orderSeqNoParam = Integer.parseInt(request.getParameter("orderSeqNo"));
			}
			
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
			
			//退書期
			String withdraw = request.getSession().getAttribute("WITHDRAW").toString();
			if(withdraw!=null && "Y".equals(withdraw)){
				withdraw = "Y";
			}else{
				withdraw = "N";
			}
			
			OrderDAOImpl orderDAOImpl = new OrderDAOImpl();			//獲取訂單編號
			String orderSeqNo = "";
			List<Order> seqNoList = orderDAOImpl.getAllOrderSeqNoInfo(conn, value, orderIntake, "Y");
			String isPay = "Y";
			if(orderSeqNoParam==0){
				if(!seqNoList.isEmpty()){
					boolean check = true;
					for(Order o : seqNoList){
						if(o.getPaidStatus().equals("N")){
							orderSeqNoParam = o.getOrderSeqNo();
							isPay = "N";
							check = false;
							break;
						}
					}
					if(check){
						orderSeqNoParam = seqNoList.get(0).getOrderSeqNo();
					}
				}
			}else{
				Order o = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNoParam);
				if(o.getPaidStatus().equals("N"))
					isPay = "N";
			}
			StudentDAOImpl stuImpl = new StudentDAOImpl();			//獲取學生詳細信息
			List stuDetList =  stuImpl.showStudentDetail(conn, value);			

			StudentBookDAOImpl studentBookDAOImpl = new StudentBookDAOImpl();	//獲取已訂圖書信息
			String paidStatus = "";
			List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, orderIntake, paidStatus, orderSeqNoParam);
						
			request.setAttribute("oprType", oprType);
			request.setAttribute("withdraw", withdraw);
			request.setAttribute("stuDetList", stuDetList);
			request.setAttribute("orderSeqNo", orderSeqNoParam);
			request.setAttribute("curIntake", orderIntake);
			request.setAttribute("orderedBookList", orderedBookList);
			request.setAttribute("seqNoList", seqNoList);
			request.setAttribute("isPay", isPay);
			
			request.setAttribute("curInRMBRate", curInRMBRate);
			request.setAttribute("curInHKDRate", curInHKDRate);
			
			if(oprType!=null && oprType.equals("bookInvoice")) {
				SysConfig config = new SysConfig();
				config.setScKey("TERM");
				config.setScType("INTAKEPARAM");
				config.setActInd("Y");
				config = sysConfigDAOImpl.getSysConfig(conn, config);
				request.setAttribute("term", config.getScValue1());
	    		request.getRequestDispatcher("bookInvoice.jsp").forward(request, response);
			} else {  
				request.getRequestDispatcher("orderedBooklist.jsp").forward(request, response);
			}
	    
		} catch (Exception e) {
			throw new ServletException (e);
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
