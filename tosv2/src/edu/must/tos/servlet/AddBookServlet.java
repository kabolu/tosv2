package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import edu.must.tos.bean.BookRelInfo;
import edu.must.tos.bean.Model;
import edu.must.tos.bean.Price;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookRelDAOImpl;
import edu.must.tos.impl.BookRelInfoDAOImpl;
import edu.must.tos.impl.BookSupplierDAOImpl;
import edu.must.tos.impl.MajorDAOImpl;
import edu.must.tos.impl.PriceDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;

public class AddBookServlet extends HttpServlet {

	public AddBookServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		HttpSession session = request.getSession();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String userId = (String)session.getAttribute("userId");
			String intake = (String)session.getAttribute("curIntake");
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			SysConfig config = new SysConfig();
			config.setScType("EXCHANGERATE");
			config.setActInd("Y");
			List rateList = sysConfigDAOImpl.getSysConfigList(conn, config);
			request.setAttribute("rateList", rateList);
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("addPage")){
				//清除上次session中添加信息
				session.removeAttribute("bookrellist");
				session.removeAttribute("courseNameList");
				session.removeAttribute("addM");
				session.removeAttribute("isbn");
				session.removeAttribute("supplierList");
				
				BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
				List supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
				
				SysConfigDAOImpl impl = new SysConfigDAOImpl();
				List langList = impl.getLanguage(conn);
				request.setAttribute("langList", langList);
				request.setAttribute("supplierList", supplierList);
				request.getRequestDispatcher("bookAdd.jsp").forward(request, response);	
			} else if (type != null && type.equals("AddBookInSession")){
				MajorDAOImpl majorDAOImpl = new MajorDAOImpl();
				List majorList = majorDAOImpl.getMajorList(conn);
				request.getSession().setAttribute("majorList", majorList);
				addBookInSession(conn, intake, userId, request, response);
			} else {
				saveBook(conn, intake, userId, request, response);
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

	private void saveBook(Connection conn, String intake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		BookDAOImpl bookImpl = new BookDAOImpl(); 
		PriceDAOImpl priceImpl = new PriceDAOImpl();
		BookRelDAOImpl relImpl = new BookRelDAOImpl();
		BookRelInfoDAOImpl relInfoImpl = new BookRelInfoDAOImpl();
		
		Model addM = (Model)session.getAttribute("addM");
		List bookRelList = null;
		if(session.getAttribute("bookrellist") != null ){
			bookRelList = (List)session.getAttribute("bookrellist");
		} else {
			bookRelList = new ArrayList();
		}
		
		BookRel br = null;
		if(session.getAttribute("otherBR") != null){
			br = (BookRel)session.getAttribute("otherBR");
			bookRelList.add(br);
		}
		
		Book book = addM.getBook();
		List priceList = addM.getPrice();
		
		String[] grades = request.getParameterValues("grade");
		String[] cores = request.getParameterValues("core");
		String[] bookTypes = request.getParameterValues("bookType");
		List bookRelInfoList = new ArrayList();
		if(bookRelList != null && !bookRelList.isEmpty()){
			BookRel otherRel = null;
			for(int i=0; i<bookRelList.size(); i++){
				BookRel rel = (BookRel)bookRelList.get(i);
				if("OTHER".equals(rel.getCourseCode())){
					otherRel = rel;
					bookRelList.remove(i);
				}
			}
			int size = bookRelList.size();
			if(otherRel != null)
				size = size + 1;
			for(int i=0; i<size; i++){
				BookRel rel = new BookRel();
				if(otherRel != null){
					if(otherRel != null && i == 0)
						rel = otherRel;
					else 
						rel = (BookRel)bookRelList.get(i-1);
				} else {
					rel = (BookRel)bookRelList.get(i);
				}
				
				String grade = grades[i];
				String core = cores[i];
				String bookType = bookTypes[i];
				BookRelInfo info = new BookRelInfo();
				info.setIntake(rel.getIntake());
				info.setIsbn(rel.getIsbn());
				info.setCourseCode(rel.getCourseCode());
				info.setMajorCode(rel.getMajorCode());
				info.setGrade(grade);
				info.setCe(core);
				info.setBookType(bookType);
				info.setActInd("Y");
				info.setCreUid(userId);
				info.setUpdUid(userId);
				bookRelInfoList.add(info);
			}
			if(otherRel != null)
				bookRelList.add(otherRel);
		}
		
		int bookResult = 0, priceResult = 0, bookRelResult = 0, bookRelInfoResult = 0;
		bookResult = bookImpl.saveInDB(conn, book);
		priceResult = priceImpl.savePriceInDB(conn, priceList);
		bookRelResult = relImpl.saveBookRelInDB(conn, bookRelList);
		bookRelInfoResult = relInfoImpl.saveBookRelInfo(conn, bookRelInfoList);
		
		if( bookResult>0 && priceResult>0 && bookRelResult>0 && bookRelInfoResult>0){
			conn.commit();
			session.removeAttribute("addM");
			session.removeAttribute("isbn");
			session.removeAttribute("bookrellist");
			session.removeAttribute("courseNameList");
			session.removeAttribute("otherBR");
			session.removeAttribute("other");
			
			request.setAttribute("msg", "Book adding success!");
			request.setAttribute("type", "addBook");
			request.getRequestDispatcher("msg.jsp").forward(request, response);
			
		} else {
			conn.rollback();
			request.setAttribute("msg", "Book adding fail!");
			request.setAttribute("type", "addBook");
			request.getRequestDispatcher("msg.jsp").forward(request, response);	
		}
	}

	private void addBookInSession(Connection conn, String intake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		//獲取版面上的圖書信息
		String isbn = request.getParameter("isbn");
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		String publisher = request.getParameter("publisher");
		String publishYear = request.getParameter("publishYear");
		String edition = request.getParameter("edition");
		String language = request.getParameter("language");
		
		String remarks = request.getParameter("remarks");
		String withdrawInd = request.getParameter("withdrawInd");
		String supplement = request.getParameter("supplement");		
		String supplierCode1 = request.getParameter("supplierCode1");
		String supplierCode2 = request.getParameter("supplierCode2");
		String unitPrice = request.getParameter("unitPrice");
		String currency = request.getParameter("currency");
		String disCount = request.getParameter("disCount");
		String favourablePrice = request.getParameter("favourablePrice");
		
		//String bookType = request.getParameter("bookType");
		//String grade = request.getParameter("grade");
		//String core = request.getParameter("core");
		
		BookDAOImpl bookImpl = new BookDAOImpl();
		Book checkBook = bookImpl.getBookByPK(conn, isbn);
		if( isbn.equals(checkBook.getIsbn()) ){
			out.print("<script language='javascript'>alert('The ISBN code is existed!');history.back();</script>");
		}else{
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
			book.setSupplement(supplement);
			book.setSupplierCode1(supplierCode1);
			book.setSupplierCode2(supplierCode2);
			book.setUnitPrice(Double.valueOf(unitPrice));
			book.setCurrency(currency);
			book.setDisCount(Double.valueOf(disCount));
			book.setFavourablePrice(Double.valueOf(favourablePrice));
			
			//book.setBookType(bookType);
			//book.setGrade(grade);
			//book.setCore(core);
			
			List price = new ArrayList();;
			SysConfigDAOImpl confImpl = new SysConfigDAOImpl();
			Double mopfuturePrice = Double.parseDouble(request.getParameter("mopfuturePrice"));
			Double mopnetPrice = Double.parseDouble(request.getParameter("mopnetPrice"));
			Double rmbfuturePrice = Double.parseDouble(request.getParameter("rmbfuturePrice"));
			Double rmbnetPrice = Double.parseDouble(request.getParameter("rmbnetPrice"));
			Price p1 = new Price();
			p1.setIsbn(isbn);
			p1.setCurrency("MOP");
			p1.setFuturePrice(mopfuturePrice);
			p1.setNetPrice(mopnetPrice);
			p1.setWithdrawPrice( confImpl.getWithDrawPrice(conn,language) );
			p1.setCreUid(userId);
			p1.setCreDate(new Date());
			p1.setUpdUid(userId);
			p1.setUpdDate(new Date());
			p1.setActInd("Y");
			p1.setIntake(intake);
			
			Price p2 = new Price();
			p2.setIsbn(isbn);
			p2.setCurrency("RMB");
			p2.setFuturePrice(rmbfuturePrice);
			p2.setNetPrice(rmbnetPrice);
			p2.setWithdrawPrice( confImpl.getWithDrawPrice(conn,language) );
			p2.setCreUid(userId);
			p2.setCreDate(new Date());
			p2.setUpdUid(userId);
			p2.setUpdDate(new Date());
			p2.setActInd("Y");
			p2.setIntake(intake);
			price.add(p1);
			price.add(p2);
			
			List bookrel = new ArrayList();
			Model addM = new Model(book, price, bookrel);
			request.getSession().setAttribute("addM", addM);
			request.getSession().setAttribute("isbn", isbn);
			request.getRequestDispatcher("bookRelAddPage.jsp").forward(request, response);
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
