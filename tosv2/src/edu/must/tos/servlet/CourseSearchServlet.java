package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.must.tos.impl.CourseDAOImpl;

public class CourseSearchServlet extends HttpServlet {

	public CourseSearchServlet() {
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
		PrintWriter out = response.getWriter();
		Connection conn = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String curIntake = (String)request.getSession().getAttribute("curIntake");
		    String oprType = request.getParameter("oprType");
		    
		    CourseDAOImpl courseImpl = new CourseDAOImpl();
			String cond = request.getParameter("cond");
			List courseList = new ArrayList();
			if(cond != null){
				courseList = courseImpl.getCourseCodeLike(conn, cond);
			}
			
			if(oprType != null && oprType.equals("edit")){
				request.setAttribute("courselist", courseList);
				request.getRequestDispatcher("bookRelEdit.jsp").forward(request, response);
			} else if(oprType != null && oprType.equals("add")) {
				request.setAttribute("courselist", courseList);
				request.getRequestDispatcher("bookRelAdd.jsp").forward(request, response);
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
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
