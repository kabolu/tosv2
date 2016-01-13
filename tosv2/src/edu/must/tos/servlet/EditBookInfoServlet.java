package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import edu.must.tos.bean.Price;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookRelDAOImpl;
import edu.must.tos.impl.BookRelInfoDAOImpl;
import edu.must.tos.impl.BookSupplierDAOImpl;
import edu.must.tos.impl.CourseDAOImpl;
import edu.must.tos.impl.MajorDAOImpl;
import edu.must.tos.impl.PriceDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;

public class EditBookInfoServlet extends HttpServlet {

	public EditBookInfoServlet() {
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
			String curIntake = (String)session.getAttribute("curIntake");
			String userId = (String)session.getAttribute("userId");
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			SysConfig config = new SysConfig();
			config.setScType("EXCHANGERATE");
			config.setActInd("Y");
			List rateList = sysConfigDAOImpl.getSysConfigList(conn, config);
			request.setAttribute("rateList", rateList);
			
			String type = null;
			if(request.getParameter("type") != null && !"".equals(request.getParameter("type"))){
				type = request.getParameter("type");
			}
			if(type != null && "saveInSession".equals(type)){
				MajorDAOImpl majorDAOImpl = new MajorDAOImpl();
				List majorList = majorDAOImpl.getMajorList(conn);
				session.setAttribute("majorList", majorList);
				saveInSession(conn, curIntake, userId, request, response);
			} else {
				forwardPage(conn, curIntake, userId, request, response);
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

	private void forwardPage(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String isbn = request.getParameter("isbn");
		
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		Book book = bookDAOImpl.getBookByPK(conn, isbn);
		
		PriceDAOImpl priceDAOImpl = new PriceDAOImpl();
		List priceList = priceDAOImpl.getBookPrice(conn, isbn, curIntake);
					
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		List langList = sysConfigDAOImpl.getLanguage(conn);
		
		BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
		List supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
		
		MajorDAOImpl majorDAOImpl = new MajorDAOImpl();
		List majorList = majorDAOImpl.getMajorList(conn);
		request.getSession().setAttribute("majorList", majorList);
		
		request.setAttribute("book", book);
		request.setAttribute("priceList", priceList);//priceList--pList
		request.setAttribute("langList", langList);
	
		request.getSession().setAttribute("book", book);
		request.getSession().setAttribute("priceList", priceList);//priceList--pList
		request.getSession().setAttribute("langList", langList);
		request.getSession().setAttribute("supplierList", supplierList);
		request.getRequestDispatcher("bookEdit.jsp").forward(request, response);
	}

	private void saveInSession(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String isbn = request.getParameter("isbn");
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		String publisher = request.getParameter("publisher");
		String publishYear = request.getParameter("publishYear");
		String edition = request.getParameter("edition");
		String language = request.getParameter("language");
		String remarks = request.getParameter("remarks");
		String withdrawInd = request.getParameter("withdrawInd");		
		String supplierCode1 = request.getParameter("supplierCode1");
		String supplierCode2 = request.getParameter("supplierCode2");
		String supplement = request.getParameter("supplement");
		String unitPrice = request.getParameter("unitPrice");
		String currency = request.getParameter("currency");
		String disCount = request.getParameter("disCount");
		String favourablePrice = request.getParameter("favourablePrice");
		
		//String bookType = request.getParameter("bookType");
		//String grade = request.getParameter("grade");
		//String core = request.getParameter("core");
		
		String oldIsbn = request.getParameter("oldIsbn");
		String changeIsbn = request.getParameter("changeIsbn");
				
		Book book = new Book();
		book.setIsbn(isbn);
		book.setTitle(title);
		book.setAuthor(author);
		book.setPublisher(publisher);
		book.setPublishYear(publishYear);
		book.setEdition(edition);
		book.setLanguage(language);
		book.setRemarks(remarks);
		book.setCreUid(userId);
		book.setCreDate(new Date());
		book.setUpdUid(userId);
		book.setUpdDate(new Date());
		book.setActInd("Y");
		book.setWithdrawInd(withdrawInd);
		book.setSupplierCode1(supplierCode1);
		book.setSupplierCode2(supplierCode2);
		book.setSupplement(supplement);
		book.setUnitPrice(Double.valueOf(unitPrice));
		book.setCurrency(currency);
		book.setDisCount(Double.valueOf(disCount));
		book.setFavourablePrice(Double.valueOf(favourablePrice));
		
		//book.setBookType(bookType);
		//book.setGrade(grade);
		//book.setCore(core);
		
		List priceList = new ArrayList();;
		try{
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			Double mopfuturePrice = Double.parseDouble(request.getParameter("MOPfuturePrice"));
			Double mopnetPrice = Double.parseDouble(request.getParameter("MOPnetPrice"));
			Double rmbfuturePrice = Double.parseDouble(request.getParameter("RMBfuturePrice"));
			Double rmbnetPrice = Double.parseDouble(request.getParameter("RMBnetPrice"));
			Price p1 = new Price();
			p1.setIsbn(isbn);
			p1.setCurrency("MOP");
			p1.setFuturePrice(mopfuturePrice);
			p1.setNetPrice(mopnetPrice);
			p1.setWithdrawPrice(sysConfigDAOImpl.getWithDrawPrice(conn,language));
			p1.setCreUid(userId);
			p1.setCreDate(new Date());
			p1.setUpdUid(userId);
			p1.setUpdDate(new Date());
			p1.setActInd("Y");
			p1.setIntake(curIntake);
			
			Price p2 = new Price();
			p2.setIsbn(isbn);
			p2.setCurrency("RMB");
			p2.setFuturePrice(rmbfuturePrice);
			p2.setNetPrice(rmbnetPrice);
			p2.setWithdrawPrice(sysConfigDAOImpl.getWithDrawPrice(conn,language));
			p2.setCreUid(userId);
			p2.setCreDate(new Date());
			p2.setUpdUid(userId);
			p2.setUpdDate(new Date());
			p2.setActInd("Y");
			p2.setIntake(curIntake);
			
			priceList.add(p1);
			priceList.add(p2);
		}catch(Exception e){
			e.printStackTrace();
		}
		List bookrel = new ArrayList();
		Model editM = new Model(book, priceList, bookrel);
	
		BookRelDAOImpl bookRelDAOImpl = new BookRelDAOImpl();
		BookRel bookRel = new BookRel();
		bookRel.setIntake(curIntake);
		bookRel.setIsbn(oldIsbn);
		bookRel.setActInd("Y");
		List bookRelList = bookRelDAOImpl.getBookRelList(conn, bookRel);
		List bookRelListInDB = bookRelDAOImpl.getBookRelList(conn, bookRel);
		
		List courseNameList = new ArrayList();
		CourseDAOImpl courseDAOImpl = new CourseDAOImpl();
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
		
		BookRelInfoDAOImpl relInfoDAOImpl = new BookRelInfoDAOImpl();
		List relInfoList = relInfoDAOImpl.getBookRelInfoList(conn, curIntake, oldIsbn);
		request.getSession().setAttribute("relInfoList", relInfoList);
		
		request.getSession().setAttribute("courseNameList", courseNameList);
		request.getSession().setAttribute("bookRelList", bookRelList);
		request.getSession().setAttribute("bookRelListInDB", bookRelListInDB);
		request.getSession().setAttribute("editM", editM);
		request.getSession().setAttribute("isbn", isbn);
		
		request.getSession().setAttribute("oldIsbn", oldIsbn);
		request.getSession().setAttribute("changeIsbn", changeIsbn);
		
		request.getRequestDispatcher("bookRelEditPage.jsp").forward(request, response);
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
