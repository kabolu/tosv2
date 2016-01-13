package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class CancelOrderBookServlet extends HttpServlet {

	public CancelOrderBookServlet() {
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
			
			String studentNo = request.getParameter("studentNo");
			
			/*
			boolean result = false;
			String isbn = request.getParameter("isbn");
			String orderIntake = (String)session.getAttribute("curIntake");
			String userId = (String)session.getAttribute("userId");
			OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
			result = orderDetailDAOImpl.deleteOrderDetail(studentNo, isbn, orderIntake, userId, conn);
			if(result) {
				conn.commit();
			} else  {
				conn.rollback();
			}
			*/
			request.getRequestDispatcher("OrderedBookListServlet?studentNo = "+studentNo+"").forward(request, response);
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
