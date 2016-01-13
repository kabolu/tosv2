package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.Course;
import edu.must.tos.bean.Studentenrol;
import edu.must.tos.impl.BookRelDAOImpl;
import edu.must.tos.impl.CourseDAOImpl;
import edu.must.tos.impl.StudentenrolDAOImpl;

public class CheckEnrolCrsServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public CheckEnrolCrsServlet() {
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
		try{
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String curIntake = (String)request.getSession().getAttribute("curIntake");
			StudentenrolDAOImpl studentenrolDAOImpl = new StudentenrolDAOImpl();
			CourseDAOImpl courseDAOImpl = new CourseDAOImpl();
			BookRelDAOImpl bookRelDAOImpl = new BookRelDAOImpl();
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			if(type != null && "CheckCrs".equals(type)){
				List<Studentenrol> enrolList = studentenrolDAOImpl.getCrsMajList(conn, curIntake);
				Course crs = new Course();
				crs.setFacultyCode("GS");
				List<Course> gsCrsList = courseDAOImpl.getCourseCode(conn, crs) ;
				List gsList = new ArrayList();
				for(Course course : gsCrsList){
					gsList.add(course.getCourseCode());
				}
				List checkList = new ArrayList();
				for(Studentenrol enrol : enrolList){
					if(gsList.contains(enrol.getCourseCode())){
						checkList.add(enrol);
					}
				}
				BookRel bookRel = new BookRel();
				bookRel.setIntake(curIntake);
				List<BookRel> bookRelList = bookRelDAOImpl.getBookRelList(conn, bookRel);
				HashSet relSet = new HashSet();
				HashSet relObjSet = new HashSet();
				for(BookRel rel : bookRelList){
					if(!relSet.contains(rel.getCourseCode())){
						relSet.add(rel.getCourseCode());
						relObjSet.add(rel);
					}
				}
				if(checkList != null && !checkList.isEmpty()){
					for(int i = 0; i < checkList.size(); i++){
						Studentenrol enrol = (Studentenrol)checkList.get(i);
						if(!relSet.contains(enrol.getCourseCode())){
							System.out.println(enrol.getCourseCode()+" "+enrol.getMajorCode()+" is not exist in bookrel!");
						} else {
							boolean isExist = false;
							Iterator iter = bookRelList.iterator();
							while(iter.hasNext()){
								BookRel br = (BookRel)iter.next();
								if(br.getCourseCode().equals(enrol.getCourseCode()) && br.getMajorCode().equals(enrol.getMajorCode())){
									isExist = true;
									break;
								}
							}
							if(!isExist)
								System.out.println("There is no "+enrol.getCourseCode()+" "+enrol.getMajorCode()+" in bookrel!");
						}
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
