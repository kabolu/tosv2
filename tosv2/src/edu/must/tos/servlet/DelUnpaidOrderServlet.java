package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Order;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;

public class DelUnpaidOrderServlet extends HttpServlet {

	public DelUnpaidOrderServlet() {
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
		HttpSession session = request.getSession();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
		    conn.setAutoCommit(false);
		    
		    SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		    OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
		    
		    String userId = (String)session.getAttribute("userId");
		    
		    String oprType = null;
		    if(request.getParameter("oprType") != null){
		    	oprType = request.getParameter("oprType");
		    }
		    if(oprType != null && oprType.equals("confirm")){
		    	String intake = null;
		    	if(request.getParameter("intake") != null){
		    		intake = request.getParameter("intake");
		    	}
		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	Date endDate = null;
		    	if(request.getParameter("endDate") != null){
		    		String date = request.getParameter("endDate") + " 23:59:59";
		    		endDate = df.parse(date);
		    	}
		    	Order order = new Order();
		    	order.setOrderIntake(intake);
		    	order.setPaidStatus("N");
		    	order.setActInd("Y");
		    	order.setCreDate(endDate);
		    	order.setUpdUid(userId);
		    	order.setUpdDate(new Date());
		    	int updateResult = orderDAOImpl.updateOrderActindList(conn, order);
		    	if(updateResult == 0){
		    		out.print("沒有記錄可操作！");
		    	}else if(updateResult == 1){
		    		conn.commit();
		    		out.print("取消未付款訂單操作成功！");
		    	}else{
		    		conn.rollback();
		    		out.print("取消未付款訂單操作失敗！");
		    	}
		    } else {		    	
				String key = "CURRINTAKE";
				String curIntake = sysConfigDAOImpl.getCurIntake(conn, key);
				SysConfig config = new SysConfig();
				config.setScKey(curIntake);
				config.setScType("ONSALE");
				config = sysConfigDAOImpl.getSysConfig(conn, config);
				String onSaleEndDate = "";
				if(config.getScValue2() != null && !"".equals(config.getScValue2())){
					onSaleEndDate = config.getScValue2();
				}
				
				request.setAttribute("curIntake", curIntake);
				request.setAttribute("onSaleEndDate", onSaleEndDate);
				request.getRequestDispatcher("unpaidOrder.jsp").forward(request, response);
		    }
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
