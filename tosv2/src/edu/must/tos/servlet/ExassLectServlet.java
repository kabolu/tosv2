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

import edu.must.tos.bean.ExassLect;
import edu.must.tos.impl.ExassLectDAOImpl;

public class ExassLectServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ExassLectServlet() {
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
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String curIntake = (String)request.getSession().getAttribute("curIntake");
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			if(type != null && "showLecturers".equals(type)){
				String courseCode = request.getParameter("courseCode");
				ExassLectDAOImpl exassLectDAOImpl = new ExassLectDAOImpl();
				ExassLect lect = new ExassLect();
				lect.setIntake(curIntake);
				lect.setCourseCode(courseCode);
				List lectList = exassLectDAOImpl.getLectByCrs(conn, lect);
				request.setAttribute("courseCode", courseCode);
				request.setAttribute("lectList", lectList);
				request.getRequestDispatcher("showLecturers.jsp").forward(request, response);
			} else if(type != null && "submitLect".equals(type)){
				String lectCode = null;
				if(request.getParameter("lectCode") != null){
					lectCode = request.getParameter("lectCode");
				}
				String courseCode = null;
				if(request.getParameter("courseCode") != null){
					courseCode = request.getParameter("courseCode");
				}
				if(lectCode != null){
					ExassLectDAOImpl exassLectDAOImpl = new ExassLectDAOImpl();
					ExassLect lecturer = new ExassLect();
					lecturer.setIntake(curIntake);
					lecturer.setCourseCode(courseCode);
					lecturer.setLecturer(lectCode);
					List lectList = exassLectDAOImpl.getLectByCrs(conn, lecturer);
					if(lectList != null && !lectList.isEmpty()){
						ExassLect lect = (ExassLect)lectList.get(0);
						out.print(lect.getLecturer() + "-" + lect.getLectChnName());
					}
				}
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
