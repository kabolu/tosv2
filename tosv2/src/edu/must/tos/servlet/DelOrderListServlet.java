package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.impl.OrderDetailDAOImpl;

public class DelOrderListServlet extends HttpServlet {

	public DelOrderListServlet() {
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
		HttpSession session = request.getSession();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
		    conn.setAutoCommit(false);
		    
			//每頁記錄數
		    String num = "10";
			//分頁從第幾條記錄開始取數據
			String start=request.getParameter("start");
			if(start == null || start.equals("")){
				start="0";
			}
			if(start.equals("page")) {
				start = "0";
			}
			
			String curIntake = (String)session.getAttribute("curIntake");
			String isbn = request.getParameter("isbn");
			String courseCode = request.getParameter("courseCode");
			
			OrderDetailDAOImpl impl = new OrderDetailDAOImpl();
			List delOrderList = impl.getDelOrderList(conn, isbn, courseCode, curIntake);
			session.setAttribute("delOrderList", delOrderList);
			request.getRequestDispatcher("deleteOrderList.jsp").forward(request, response);
						
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
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
