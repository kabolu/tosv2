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

import edu.must.tos.bean.Course;
import edu.must.tos.impl.CourseDAOImpl;

public class CourseListServlet extends HttpServlet {

	public CourseListServlet() {
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
			
		    //每頁記錄數
		    String num = "10";
			//分頁從第幾條記錄開始取數據
			String start=request.getParameter("start");
			if(start==null || start.equals("")){
				start="0";
			}
			if(start.equals("page")) {
				start = "0";
			}
		
		    String courseCode=request.getParameter("courseCode");
		    String chineseName=request.getParameter("chineseName");
		    String englishName=request.getParameter("englishName");
		    String facultyCode=request.getParameter("facultyCode");
		    
		    Course course = new Course();
		    
		    if(courseCode!=null&&!courseCode.equals("")){
		    course.setCourseCode(courseCode);
		    }
		    if(chineseName!=null&&!chineseName.equals("")){
		    course.setChineseName(chineseName);
		    }
		    if(englishName!=null&&!englishName.equals("")){
		    course.setEnglishName(englishName);
		    }
		    if(facultyCode!=null&&!facultyCode.equals("")){
		    course.setFacultyCode(facultyCode);
		    }
		    
		    CourseDAOImpl courList =new CourseDAOImpl();
		    List courseList = courList.searchCourse(conn,course,Integer.parseInt(start),Integer.parseInt(num));
		    List page = courList.getpage(conn,course, start,num);
		    double totalPages = Math.ceil(courList.getRsCount(conn,course)/Double.parseDouble(num));
		    
		    request.setAttribute("totalPages", totalPages);
		    request.setAttribute("course", course);
		    request.setAttribute("start", start);
		    request.setAttribute("page", page);
		    request.setAttribute("courseList", courseList);
			request.getRequestDispatcher("courseList.jsp").forward(request,response);
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
