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

import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.Course;
import edu.must.tos.bean.Model;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookRelDAOImpl;
import edu.must.tos.impl.BookRelInfoDAOImpl;
import edu.must.tos.impl.BookSupplierDAOImpl;
import edu.must.tos.impl.CourseDAOImpl;
import edu.must.tos.impl.PriceDAOImpl;

public class BookDetailServlet extends HttpServlet {

	public BookDetailServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;    
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
	    
			HttpSession session = request.getSession();
			
			String isbn = request.getParameter("isbn");
			String intake = (String)session.getAttribute("curIntake");
			
			BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
			List supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
			
			BookDAOImpl bookImpl = new BookDAOImpl();
			Book book = null;
			if(isbn != null){
				book = bookImpl.getBookByPK(conn, isbn);
			}
			PriceDAOImpl priceImpl = new PriceDAOImpl();
			List price = priceImpl.getBookPrice(conn, isbn, intake);
			
			BookRelDAOImpl bookrelImpl = new BookRelDAOImpl();
			BookRel bookRel = new BookRel();
			bookRel.setIsbn(isbn);
			bookRel.setIntake(intake);
			bookRel.setActInd("Y");
			List bookrel = bookrelImpl.getBookRelList(conn, bookRel);
						
			List courseCNameList = new ArrayList();
			CourseDAOImpl courseDAOImpl = new CourseDAOImpl();
			for( int i=0; i<bookrel.size(); i++ ){ //bookrel--rList
				BookRel br = (BookRel)bookrel.get(i); //bookrel--rList
				String courseCName = null;
				if(br.getCourseCode().equals("OTHER")){
					courseCName = "OTHER";
				}else{
					Course cos = courseDAOImpl.getACourse(conn, br.getCourseCode());
					courseCName = cos.getChineseName();
				}
				courseCNameList.add(courseCName);
			}
			
			BookRelInfoDAOImpl relInfoDAOImpl = new BookRelInfoDAOImpl();
			List relInfoList = relInfoDAOImpl.getBookRelInfoList(conn, intake, isbn);
			request.setAttribute("relInfoList", relInfoList);
			
			Model m = new Model(book, price, bookrel); //price -- pList  bookrel--rList
			request.setAttribute("bookModel", m);
			request.setAttribute("courseCNameList", courseCNameList);
			request.setAttribute("supplierList", supplierList);
			request.getRequestDispatcher("viewBook.jsp").forward(request, response);
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

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
