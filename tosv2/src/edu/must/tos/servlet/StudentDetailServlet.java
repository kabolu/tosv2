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

import edu.must.tos.impl.StudentDAOImpl;

public class StudentDetailServlet extends HttpServlet {

	public StudentDetailServlet() {
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
		    
		    String psStudentNo = null;
		    String psApplicantNo = null;
		    if(request.getParameter("psStudentNo") != null && !request.getParameter("psStudentNo").equals("null")){
		    	psStudentNo = request.getParameter("psStudentNo");
		    }
		    if(request.getParameter("psapplicantNo") != null && !request.getParameter("psapplicantNo").equals("null")){
		    	psApplicantNo = request.getParameter("psapplicantNo");
		    }
		    String value = psStudentNo + "," + psApplicantNo;
		    StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
		    List studentDetailList = studentDAOImpl.showStudentDetail(conn, value);
		    request.setAttribute("studentDetailList", studentDetailList);
		    request.getRequestDispatcher("studentDetail.jsp").forward(request, response);
	    
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

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
