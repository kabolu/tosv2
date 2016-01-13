package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;

public class UpdateNewStudNoServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UpdateNewStudNoServlet() {
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
		response.setContentType("text/html;charset=utf-8");
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String updIntake = null;
			if(request.getParameter("updIntake") != null && !request.getParameter("updIntake").equals("")){
				updIntake = request.getParameter("updIntake");
			}
			StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
			List updNewStudList = studentDAOImpl.getUpdNewStudList(conn, updIntake);
			
			if(!updNewStudList.isEmpty()){				
				OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
				OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
				boolean updOrderNewStudNo = orderDAOImpl.updateOrdNewStudNo(conn, updIntake, updNewStudList);
				
				boolean updDetailNewStudNo = orderDetailDAOImpl.updateOrddetailNewStudNo(conn, updIntake, updNewStudList);
				
				if(updOrderNewStudNo && updDetailNewStudNo){
					conn.commit();
					request.setAttribute("msg", "更新成功，共"+updNewStudList.size()+"條記錄完成！");
				}else{
					conn.rollback();
					request.setAttribute("msg", "更新失败!");
				}
				request.getRequestDispatcher("updateNewStudNo.jsp").forward(request, response);
			}else{
				request.setAttribute("msg", "無資料可更新!");
				request.getRequestDispatcher("updateNewStudNo.jsp").forward(request, response);
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
