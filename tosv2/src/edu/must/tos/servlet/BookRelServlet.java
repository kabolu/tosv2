package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.Course;
import edu.must.tos.impl.CourseDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;

public class BookRelServlet extends HttpServlet {

	public BookRelServlet() {
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
	    
			HttpSession session = request.getSession();
	    	String userId = (String)session.getAttribute("userId");
			String curIntake = (String)session.getAttribute("curIntake");
						
			String type = null;
			if(request.getParameter("type") != null && !"".equals(request.getParameter("type"))){
				type = request.getParameter("type");
			}
			if(type != null && "removeSession".equals(type)){
				removeSession(conn, request, response, curIntake, userId);
			}
			else if(type != null && "removeBookRel".equals(type)){
				removeBookRel(conn, request, response, curIntake, userId);
			}
			else if(type != null && "saveSession".equals(type)){
				saveSession(conn, request, response, curIntake, userId);
			}
			else if(type != null && "saveSession4Maj".equals(type)){
				saveSession4Maj(conn, request, response, curIntake, userId);
			}
			else{
				saveBookRel(conn, request, response, curIntake, userId);
			}
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

	private void saveSession4Maj(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake, String userId) {
		String id = null;
		if(request.getParameter("id") != null){
			id = request.getParameter("id");
		}
		String majorCode = null;
		if(request.getParameter("majorCode") != null){
			majorCode = request.getParameter("majorCode");
		}
		if(id != null && "OTHER".equals(id)){
			BookRel otherRel = (BookRel)request.getSession().getAttribute("otherBR");
			otherRel.setMajorCode(majorCode);
			request.getSession().setAttribute("otherBR", otherRel);
		} else {
			Integer i = Integer.parseInt(id);
			//增加圖書與科目關係的session
			List bookRelList = (List)request.getSession().getAttribute("bookrellist");
			if(bookRelList != null){
				BookRel bookRel = (BookRel)bookRelList.get(i);
				bookRel.setMajorCode(majorCode);
				request.getSession().setAttribute("bookrellist", bookRelList);
			} else {
				bookRelList = (List)request.getSession().getAttribute("bookRelList");
				BookRel bookRel = (BookRel)bookRelList.get(i);
				bookRel.setMajorCode(majorCode);
				request.getSession().setAttribute("bookRelList", bookRelList);
			}
		}
	}

	private void saveSession(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake, String userId) throws Exception, IOException {
		HttpSession session = request.getSession();
		
		String isbn = request.getParameter("isbn");
		String[] courses = request.getParameterValues("box");
		List courseList = (List)session.getAttribute("courselist");
		
		List bookRelList = (List)session.getAttribute("bookrellist");
		
		String other = request.getParameter("other");
		CourseDAOImpl courseDAOImpl = new CourseDAOImpl();
		List courseNameList = new ArrayList();
		if(bookRelList == null || bookRelList.isEmpty()){
			bookRelList = new ArrayList();
			if(courses != null && courses.length > 0){
				for(int i=0; i<courses.length; i++){
					Course c = (Course)courseList.get(Integer.parseInt(courses[i]));
					BookRel br = new BookRel();
					br.setIsbn(isbn);
					br.setCourseCode(c.getCourseCode());
					br.setActInd("Y");
					br.setCreUid(userId);
					br.setCreDate(new Date());
					br.setUpdUid(userId);
					br.setUpdDate(new Date());
					br.setIntake(curIntake);
					bookRelList.add(br);
				}
				for(int i=0; i<bookRelList.size(); i++){
					BookRel br = (BookRel)bookRelList.get(i);
					Course cos = courseDAOImpl.getACourse(conn, br.getCourseCode());
					String courseName = cos.getChineseName();
					courseNameList.add(courseName);
				}
			}
			session.setAttribute("bookrellist", bookRelList);
		} else {
			List courseCodelist = new ArrayList();
			if(courses != null && courses.length > 0){
				for(int i=0; i<courses.length; i++){
					Course c = (Course)courseList.get(Integer.parseInt(courses[i]));
					BookRel br = new BookRel();
					br.setIsbn(isbn);
					br.setCourseCode(c.getCourseCode());
					br.setActInd("Y");
					br.setCreUid(userId);
					br.setCreDate(new Date());
					br.setUpdUid(userId);
					br.setUpdDate(new Date());
					br.setIntake(curIntake);
					bookRelList.add(br);
				}
			}
			//可以錄入多個相同CourseCode
			
			HashSet set = new HashSet();
			for(int j=0; j<bookRelList.size(); j++ ){
				BookRel br = (BookRel)bookRelList.get(j);
				if(!set.contains(br.getCourseCode())){
					set.add(br.getCourseCode());
					courseCodelist.add(bookRelList.get(j));
				}
			}
			
			session.setAttribute("bookrellist", courseCodelist);
			for(int i=0; i<courseCodelist.size(); i++){
				BookRel br = (BookRel)courseCodelist.get(i);
				Course cos = courseDAOImpl.getACourse(conn, br.getCourseCode());
				String courseName = cos.getChineseName();
				courseNameList.add(courseName);
			}
		}
		session.setAttribute("courseNameList", courseNameList);
		if(other != null){
			BookRel otherBR = new BookRel();
			otherBR.setIsbn(isbn);
			otherBR.setCourseCode(other);
			otherBR.setActInd("Y");
			otherBR.setCreUid(userId);
			otherBR.setCreDate(new Date());
			otherBR.setUpdUid(userId);
			otherBR.setUpdDate(new Date());
			otherBR.setIntake(curIntake);
			session.setAttribute("otherBR", otherBR);
			session.setAttribute("other", other);
		}
		request.setAttribute("para", "1");
		request.getRequestDispatcher("forward.jsp").forward(request, response);
	}

	private void removeBookRel(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake, String userId) throws Exception {
		HttpSession session = request.getSession();
		
		List bookrelList = (List)session.getAttribute("bookRelList");
		List courseNameList = (List)session.getAttribute("courseNameList");
		int id = Integer.parseInt(request.getParameter("id"));
		BookRel br = (BookRel)bookrelList.get(id);
		OrderDetailDAOImpl ordetailImpl = new OrderDetailDAOImpl();
		boolean flag = ordetailImpl.getCheckCourseCode(conn, br);
		if(flag){
			request.setAttribute("flag", "true");
			request.getRequestDispatcher("bookRelEditPage.jsp").forward(request, response);
		}else{
			String isbn = br.getIsbn();
			bookrelList.remove(id);
			courseNameList.remove(id);
			session.setAttribute("courseNameList", courseNameList);
			session.setAttribute("bookRelList", bookrelList);
			request.setAttribute("isbn", isbn);
			response.sendRedirect("bookRelEditPage.jsp");
		}
	}

	private void removeSession(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake, String userId) throws Exception {
		HttpSession session = request.getSession();
		List bookRelList = (List)session.getAttribute("bookrellist");
		if(request.getParameter("id")!=null && !request.getParameter("id").equals("")){
			int id = Integer.parseInt(request.getParameter("id"));
			bookRelList.remove(id);
		}
		
		if(request.getParameter("other")!=null && request.getParameter("other").equals("OTHER")){
			session.removeAttribute("other");
			session.removeAttribute("otherBR");
		}
		
		session.setAttribute("bookrellist", bookRelList);
		response.sendRedirect("bookRelAddPage.jsp");
	}

	private void saveBookRel(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake, String userId) throws Exception {
		HttpSession session = request.getSession();
		
		String isbn = request.getParameter("isbn");
		String[] course = request.getParameterValues("box");
		List courseList = (List)session.getAttribute("courselist");
		
		String other = null;
		if(request.getParameter("other")!=null){
			other = request.getParameter("other");
		}
		List bookRelList = (List)session.getAttribute("bookRelList");
		if(bookRelList == null || bookRelList.isEmpty()){
			bookRelList = new ArrayList();
			if(course!=null && course.length>0){
				for(int i=0; i<course.length; i++){
					Course c = new Course();
					c = (Course)courseList.get(Integer.parseInt(course[i]));
					BookRel br = new BookRel();
					br.setIsbn(isbn);
					br.setCourseCode(c.getCourseCode());
					br.setMajorCode("ALL");
					br.setActInd("Y");
					br.setCreUid(userId);
					br.setUpdUid(userId);
					br.setIntake(curIntake);
					bookRelList.add(br);
				}
			}
			if(other!=null){
				BookRel br = new BookRel();
				br.setIsbn(isbn);
				br.setCourseCode(other);
				br.setActInd("Y");
				br.setCreUid(userId);
				br.setUpdUid(userId);
				br.setIntake(curIntake);
				bookRelList.add(br);
			}
			
			CourseDAOImpl courseDAOImpl = new CourseDAOImpl();
			List courseNameList = new ArrayList();
			for(int i=0; i<bookRelList.size(); i++){
				BookRel br = (BookRel)bookRelList.get(i);
				String courseName = null;
				if(br.getCourseCode().equals("OTHER")){
					courseName = "OTHER";
				}else{
					Course cos = courseDAOImpl.getACourse(conn, br.getCourseCode());
					courseName = cos.getChineseName();
				}
				courseNameList.add(courseName);
			}
			session.setAttribute("courseNameList", courseNameList);
			session.setAttribute("bookRelList", bookRelList);
			request.setAttribute("isbn", isbn);
			request.setAttribute("para", "3");
			request.getRequestDispatcher("forward.jsp").forward(request, response);
		} else {
			if(course != null && course.length>0){
				for(int i=0; i<course.length; i++){
					Course c = new Course();
					c = (Course)courseList.get(Integer.parseInt(course[i]));
					BookRel br = new BookRel();
					br.setIsbn(isbn);
					br.setCourseCode(c.getCourseCode());
					br.setActInd("Y");
					br.setCreUid(userId);
					br.setUpdUid(userId);
					br.setIntake(curIntake);
					bookRelList.add(br);
				}
			}
			if(other != null){
				BookRel br = new BookRel();
				br.setIntake(curIntake);
				br.setIsbn(isbn);
				br.setCourseCode(other);
				br.setMajorCode("");
				br.setActInd("Y");
				br.setCreUid(userId);
				br.setUpdUid(userId);
				bookRelList.add(br);
			}
			
			HashSet set = new HashSet();
			List courseCodelist = new ArrayList();
			for(int j=0; j<bookRelList.size(); j++ ){
				BookRel br = (BookRel)bookRelList.get(j);
				if(!set.contains(br.getCourseCode())){
					set.add(br.getCourseCode());
					courseCodelist.add(bookRelList.get(j));
				}
			}
			
			CourseDAOImpl courseDAOImpl = new CourseDAOImpl();
			List courseNameList = new ArrayList();
			for(int i=0; i<bookRelList.size(); i++){
				BookRel br = (BookRel)bookRelList.get(i);
				String courseName = null;
				if(br.getCourseCode().equals("OTHER")){
					courseName = "OTHER";
				}else{
					Course cos = courseDAOImpl.getACourse(conn, br.getCourseCode());
					courseName = cos.getChineseName();
				}
				courseNameList.add(courseName);
			}
			session.setAttribute("courseNameList", courseNameList);
			session.setAttribute("bookRelList", bookRelList);
			request.setAttribute("isbn", isbn);
			request.setAttribute("para", "3");
			request.getRequestDispatcher("forward.jsp").forward(request, response);
		}
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
