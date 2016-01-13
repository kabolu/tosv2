package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.BookRel;
import edu.must.tos.impl.StudentBookDAOImpl;

public class StudentBookListServlet extends HttpServlet {

	public StudentBookListServlet() {
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
				
				HttpSession session = request.getSession();
				String studentNo = request.getParameter("studentNo");
				String applicantNo = request.getParameter("applicantNo");
				
				String orderIntake = (String)session.getAttribute("curIntake");
				String psFacultyCode = request.getParameter("facultyCode");
				String psYear = request.getParameter("year");
				
				String facultyCode = "";
				if(psFacultyCode != null) {
					facultyCode = psFacultyCode;
				}
				int year = 0;
				if (psYear != null) {
					year = Integer.parseInt(psYear);
				}
				
				StudentBookDAOImpl studentBookDAOImpl = new StudentBookDAOImpl();
				List availableBooklist = new ArrayList();
				String other = "";
				if (request.getParameter("other") != null && !request.getParameter("other").equals("")) {
					other = request.getParameter("other");
				}
				if (other != "") {
					BookRel bookRel = new BookRel();
					bookRel.setIntake(orderIntake);
					bookRel.setIsbn(other);
					bookRel.setActInd("Y");
					
					String studParam = studentNo + "," + applicantNo;
					availableBooklist = studentBookDAOImpl.searchAvailableBookByIsbn(conn, bookRel, studParam);
				} else {
					availableBooklist = studentBookDAOImpl.searchAvailableBook(conn, studentNo, orderIntake, facultyCode, year);
				}
				
				session.setAttribute("availableBooklist", availableBooklist);
				session.setAttribute("facultyCode", facultyCode);
				session.setAttribute("year", year);
				request.getRequestDispatcher("orderbookindex2.jsp").forward(request, response);
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
