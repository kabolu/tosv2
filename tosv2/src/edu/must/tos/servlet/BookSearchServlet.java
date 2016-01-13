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

import edu.must.tos.bean.Book;
import edu.must.tos.bean.Course;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;

public class BookSearchServlet extends HttpServlet {

	public BookSearchServlet() {
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
			//開始
			String start=request.getParameter("start");
			if(start==null || start.equals("")){
				start="0";
			}
			if(start.equals("page")) {
				start = "0";
			}
		
			String facultyCode = request.getParameter("facultyCode");
			String isbn = request.getParameter("isbn");
			String courseCode = request.getParameter("courseCode");
			String title = request.getParameter("title");
			String author = request.getParameter("author");
			String publisher = request.getParameter("publisher");
			
			Book book = new Book();
			Course course = new Course();
			if( facultyCode != null && !facultyCode.equals("") ){
				course.setFacultyCode(facultyCode);
			}
			if(courseCode!=null && !courseCode.equals("")){
				course.setCourseCode(courseCode);
			}
			if(isbn!=null && !isbn.equals("")){
				book.setIsbn(isbn.trim());
			}
			if(title!=null && !title.equals("")){
				book.setTitle(title);
			}
			if(author!=null && !author.equals("")){
				book.setAuthor(author);
			}
			if(publisher!=null && !publisher.equals("")){
				book.setPublisher(publisher);
			}
			BookDAOImpl bookDAOImpl = new BookDAOImpl();
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			if(type != null && "searchCosBooks".equals(type)){
				String intake = null;
				if(request.getParameter("intake") != null){
					intake = request.getParameter("intake");
				}
				if(intake == null){
					intake = (String)request.getSession().getAttribute("curIntake");
				}
				SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
				List scIntakeList = sysConfigDAOImpl.getSysIntake(conn);
				
				List cosBooksList = bookDAOImpl.getBooksByCos(conn, intake, courseCode);
				request.setAttribute("intake", intake);
				request.setAttribute("courseCode", courseCode);
				request.setAttribute("cosBooksList", cosBooksList);
				request.setAttribute("intakeList", scIntakeList);
				request.getRequestDispatcher("courseOfBookList.jsp").forward(request, response);
			}else{
				int count = bookDAOImpl.getRsCount(conn, book, course);
				
				List page = bookDAOImpl.getpage(conn, book, course, start, num, count);
				
				List bookList = bookDAOImpl.queryBook(conn, book, course, Integer.parseInt(start), Integer.parseInt(num));
				
				double totalPages = Math.ceil(count/Double.parseDouble(num));
				
				request.setAttribute("totalPages", totalPages);
				request.setAttribute("book", book);
				request.setAttribute("courseCode", courseCode);
				request.setAttribute("facultyCode", facultyCode);
				request.setAttribute("start", start);
				request.setAttribute("page", page);
				if(bookList==null || bookList.size()==0){
					request.setAttribute("noResult", "No Records!");
					request.getRequestDispatcher("null.jsp").forward(request, response);
				}else{
					request.setAttribute("booklist", bookList);
					request.getRequestDispatcher("bookList.jsp").forward(request, response);
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
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
