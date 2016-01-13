package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.must.tos.impl.OrderDetailDAOImpl;

public class CheckIsbnServlet extends HttpServlet {

	public CheckIsbnServlet() {
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

			String isbn = request.getParameter("isbn");
			OrderDetailDAOImpl ordetailImpl = new OrderDetailDAOImpl();
			boolean flag = ordetailImpl.getIsbnInOrDetail(conn, isbn);
			
			if(flag){
				request.setAttribute("isbn", isbn);
				request.setAttribute("msg", "The book has been reserved, please confirm delete or not?");
				request.getRequestDispatcher("bookDelConfirm.jsp").forward(request, response);
			}else{
				request.setAttribute("isbn", isbn);
				request.getRequestDispatcher("bookDelConfirm.jsp").forward(request, response);
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

	public void init() throws ServletException {
		// Put your code here
	}

}
