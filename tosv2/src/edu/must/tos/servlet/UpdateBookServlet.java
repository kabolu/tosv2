package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookDeliver;
import edu.must.tos.bean.BookInventory;
import edu.must.tos.bean.BookPurchasingBean;
import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.BookRelInfo;
import edu.must.tos.bean.BookStockInBean;
import edu.must.tos.bean.BookStockOut;
import edu.must.tos.bean.Model;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Price;
import edu.must.tos.bean.Stocktake;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookDeliverDAOImpl;
import edu.must.tos.impl.BookInventoryDAOImpl;
import edu.must.tos.impl.BookPurchasingDAOImpl;
import edu.must.tos.impl.BookRelDAOImpl;
import edu.must.tos.impl.BookRelInfoDAOImpl;
import edu.must.tos.impl.BookStockInDAOImpl;
import edu.must.tos.impl.BookStockOutDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.PriceDAOImpl;
import edu.must.tos.impl.StocktakeDAOImpl;

public class UpdateBookServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UpdateBookServlet() {
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
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
	    
			HttpSession session = request.getSession();
	    	String userId = (String)session.getAttribute("userId");
			String intake = (String)session.getAttribute("curIntake");
			
			Model m = (Model)session.getAttribute("editM");
			
			String isbn = (String)session.getAttribute("isbn");
			
			String changeIsbn = (String)session.getAttribute("changeIsbn");
			String oldIsbn = (String)session.getAttribute("oldIsbn");
			
			List bookrelList = (List)session.getAttribute("bookRelList");
			
			String[] grades = request.getParameterValues("grade");
			
			String[] cores = request.getParameterValues("core");
			
			String[] bookTypes = request.getParameterValues("bookType");
			
			List bookRelListInDB = (List)session.getAttribute("bookRelListInDB");
			
			Book book = m.getBook();
			List priceList = m.getPrice();
			
			HashSet set = new HashSet();			
			
			List delList = new ArrayList();
			for(int i=0; i<bookRelListInDB.size(); i++){
				BookRel dbRel = (BookRel)bookRelListInDB.get(i);
				boolean flag = false;
				for(int j=0; j<bookrelList.size(); j++){
					BookRel bookRel = (BookRel)bookrelList.get(j);
					if(dbRel.getCourseCode() != null && dbRel.getCourseCode().equals(bookRel.getCourseCode()) 
							&& dbRel.getMajorCode() != null && dbRel.getMajorCode().equals(bookRel.getMajorCode())){
						flag = true;
						break;
					}
				}
				if(!flag)
					delList.add(dbRel);
			}
			BookDAOImpl bookImpl = new BookDAOImpl(); 
			PriceDAOImpl priceImpl = new PriceDAOImpl();
			BookRelDAOImpl relImpl = new BookRelDAOImpl();
			BookRelInfoDAOImpl relInfoImpl = new BookRelInfoDAOImpl();
			
			int bookResult = 0, priceResult = 0, bookRelUpd = 0, bookRelDel = 0, bookRelInfo = 0;
			if("Y".equals(changeIsbn)){
				// Add new book record 
				book.setIsbn(isbn);
				bookResult = bookImpl.saveInDB(conn, book);
				
				if(!priceList.isEmpty()){
					for(int i=0; i<priceList.size(); i++){
						Price p = (Price)priceList.get(i);
						p.setIsbn(isbn);
					}
				}
				priceResult = priceImpl.savePriceInDB(conn, priceList);
				
				if(bookResult > 0 && priceResult > 0){
					if(delList.isEmpty()){
						List updRelList = new ArrayList();
						List updRelInfoList = new ArrayList();
						for(int i=0; i<bookrelList.size(); i++){
							BookRel bookRel = (BookRel)bookrelList.get(i);
							BookRel updBookRel = new BookRel();
							updBookRel.setIsbn(isbn);
							updBookRel.setCourseCode(bookRel.getCourseCode());
							updBookRel.setMajorCode(bookRel.getMajorCode());
							updBookRel.setActInd("Y");
							updBookRel.setCreUid(userId);
							updBookRel.setUpdUid(userId);
							updBookRel.setIntake(intake);
							updRelList.add(updBookRel);
							
							String grade = grades[i];
							String core = cores[i];
							String bookType = bookTypes[i];
							BookRelInfo info = new BookRelInfo();
							info.setIntake(intake);
							info.setIsbn(isbn);
							info.setCourseCode(bookRel.getCourseCode());
							info.setMajorCode(bookRel.getMajorCode());
							info.setGrade(grade);
							info.setCe(core);
							info.setBookType(bookType);
							info.setActInd("Y");
							info.setCreUid(userId);
							info.setUpdUid(userId);
							updRelInfoList.add(info);
						}
						bookRelUpd = relImpl.saveBookRelInDB(conn, updRelList);
						bookRelInfo = relInfoImpl.saveBookRelInfo(conn, updRelInfoList);
						if(bookRelUpd > 0 && bookRelInfo > 0){
							conn.commit();
							session.removeAttribute("oldIsbn");
							session.removeAttribute("isbn");
							session.removeAttribute("editM");
							session.removeAttribute("bookRelList");
							session.removeAttribute("courseNameList");
							session.removeAttribute("courselist");
							
							request.setAttribute("msg", "Book data modification success!");
						} else {
							conn.rollback();
							request.setAttribute("msg", "book data modification failed!");
						}
					}					
				} else {
					conn.rollback();
					request.setAttribute("msg", "book data modification failed!");	
				}
			} else {
				if(!isbn.equals(oldIsbn)){
					// Edit isbn 
					book.setIsbn(isbn);
					bookResult = bookImpl.saveInDB(conn, book);
					
					book.setIsbn(oldIsbn);
					book.setActInd("N");
					if(bookResult > 0)
						bookResult = bookImpl.updateOneBook(conn, book);
					
					if(!priceList.isEmpty()){
						for(int i=0; i<priceList.size(); i++){
							Price p = (Price)priceList.get(i);
							p.setIsbn(isbn);
						}
						priceResult = priceImpl.savePriceInDB(conn, priceList);
						
						for(int i=0; i<priceList.size(); i++){
							Price p = (Price)priceList.get(i);
							p.setIsbn(oldIsbn);
							p.setActInd("N");
							priceResult = priceImpl.getDelPrice(conn, p);
							if(priceResult < 0)
								break;
						}
					}
					if(bookResult > 0 && priceResult > 0){
						List updRelList = new ArrayList();
						List updRelInfoList = new ArrayList();
						for(int i=0; i<bookrelList.size(); i++){
							BookRel bookRel = (BookRel)bookrelList.get(i);
							BookRel updBookRel = new BookRel();
							updBookRel.setIsbn(isbn);
							updBookRel.setCourseCode(bookRel.getCourseCode());
							updBookRel.setMajorCode(bookRel.getMajorCode());
							updBookRel.setActInd("Y");
							updBookRel.setCreUid(userId);
							updBookRel.setUpdUid(userId);
							updBookRel.setIntake(intake);
							updRelList.add(updBookRel);
							
							String grade = grades[i];
							String core = cores[i];
							String bookType = bookTypes[i];
							BookRelInfo info = new BookRelInfo();
							info.setIntake(intake);
							info.setIsbn(isbn);
							info.setCourseCode(bookRel.getCourseCode());
							info.setMajorCode(bookRel.getMajorCode());
							info.setGrade(grade);
							info.setCe(core);
							info.setBookType(bookType);
							info.setActInd("Y");
							info.setCreUid(userId);
							info.setUpdUid(userId);
							updRelInfoList.add(info);
						}
						bookRelUpd = relImpl.saveBookRelInDB(conn, updRelList);
						
						BookRel bookRel = new BookRel();
						bookRel.setIsbn(oldIsbn);
						bookRel.setIntake(intake);
						bookRel.setActInd("Y");
						List bookRelList = relImpl.getBookRelList(conn, bookRel);
						if(bookRelList != null && !bookRelList.isEmpty()){
							for(int i=0; i<bookRelList.size(); i++){
								BookRel r = (BookRel)bookRelList.get(i);
								r.setActInd("N");
								r.setUpdUid(userId);
							}
						}
						if(bookRelUpd > 0)
							bookRelUpd = relImpl.saveBookRelInDB(conn, bookRelList);
						if(bookRelUpd > 0)
							bookRelInfo = relInfoImpl.saveBookRelInfo(conn, updRelInfoList);
					}
					
					//tstocktake
					boolean stocktakeFlag = true;
					StocktakeDAOImpl stocktakeDAOImpl = new StocktakeDAOImpl();
					Timestamp maxStockDate = stocktakeDAOImpl.getMaxStocktakeDate(conn);
					Stocktake stocktake = stocktakeDAOImpl.getStocktakeBean(conn, maxStockDate, oldIsbn);
					if(stocktake != null){
						stocktake.setIsbn(isbn);
						stocktake.setUpduid(userId);
						stocktakeFlag = stocktakeDAOImpl.updateStocktake(conn, stocktake);
					}
					
					//tbookstockin
					boolean stockinFlag = true;
					BookStockInDAOImpl bookStockInDAOImpl = new BookStockInDAOImpl();
					List<BookStockInBean> stockinList = bookStockInDAOImpl.getBookStockInByIsbn(conn, intake, oldIsbn);
					if(stockinList != null && !stockinList.isEmpty()){
						for(BookStockInBean in : stockinList){
							in.setIsbn(isbn);
							in.setActind("Y");
							stockinFlag = bookStockInDAOImpl.updateBookStockInByNo(conn, in);
							if(!stockinFlag)
								break;
						}
					}
					
					//tbookstockout
					boolean stockoutFlag = true;
					BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
					BookStockOut bookStockOut = new BookStockOut();
					bookStockOut.setIntake(intake);
					bookStockOut.setIsbn(oldIsbn);
					List<BookStockOut> stockoutList = bookStockOutDAOImpl.getStockOutList(conn, bookStockOut);
					for(BookStockOut out : stockoutList){
						out.setIsbn(isbn);
						out.setUpduid(userId);
						stockoutFlag = bookStockOutDAOImpl.updateBookStockOutByNo(conn, out);
						if(!stockoutFlag)
							break;
					}
					
					//tbookpurchasing
					boolean bookpurchasingFlag = true;
					BookPurchasingDAOImpl bookPurchasingDAOImpl = new BookPurchasingDAOImpl();
					List<BookPurchasingBean> bookPurchasingList = bookPurchasingDAOImpl.getBookPurchasingBeanByIsbn(conn, oldIsbn, intake, null, null, false);
					for(BookPurchasingBean pur : bookPurchasingList){
						pur.getBook().setIsbn(isbn);
						pur.getBookPurchase().setUpdUid(userId);
						bookpurchasingFlag = bookPurchasingDAOImpl.changeIsbnList(conn, pur);
						if(!bookpurchasingFlag)
							break;
					}
					
					//tbookinventory
					boolean inventoryFlag = true;
					BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
					BookInventory inventory = bookInventoryDAOImpl.getBookInventoryByPK(conn, oldIsbn);
					if(inventory != null){
						inventory.setIsbn(isbn);
						inventory.setUpduid(userId);
						inventoryFlag = bookInventoryDAOImpl.updateBookInventoryByIsbn(conn, inventory, oldIsbn);
					}
					
					//tordetail
					boolean detailFlag = true;
					OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
					List<OrDetail> detailList = orderDetailDAOImpl.getOrDetail(conn, intake, oldIsbn, "Y");
					for(OrDetail od : detailList){
						od.setUpdUid(userId);
						detailFlag = orderDetailDAOImpl.changeOrDetailIsbn(conn, od, isbn, oldIsbn + " change to " + isbn);
						if(!detailFlag)
							break;
					}
					
					//tbookdeliver
					boolean deliverFlag = true;
					BookDeliverDAOImpl bookDeliverDAOImpl = new BookDeliverDAOImpl();
					List<BookDeliver> deliverList = bookDeliverDAOImpl.getBookDeliverList(conn, intake, oldIsbn);
					for(BookDeliver deliver : deliverList){
						deliver.setIsbn(isbn);
						deliver.setUpdUid(userId);
						deliverFlag = bookDeliverDAOImpl.updateBookDeliverByNo(conn, deliver);
						if(!deliverFlag)
							break;
					}
					if(bookResult > 0 && priceResult > 0 && bookRelUpd > 0 && bookRelInfo > 0 && stocktakeFlag
							&& stockinFlag && stockoutFlag && bookpurchasingFlag && inventoryFlag && detailFlag && deliverFlag){
						conn.commit();
						session.removeAttribute("isbn");
						session.removeAttribute("oldIsbn");
						session.removeAttribute("oldIsbn");
						session.removeAttribute("editM");
						session.removeAttribute("bookRelList");
						session.removeAttribute("courseNameList");
						session.removeAttribute("courselist");						
						request.setAttribute("msg", "Book data modification success!");
					} else {
						conn.rollback();
						request.setAttribute("msg", "book data modification failed!");
					}					
				} else {
					bookResult = bookImpl.updateOneBook(conn, book);
					priceResult = priceImpl.savePriceInDB(conn, priceList);
					
					if(bookResult > 0 && priceResult > 0){
						if(delList.isEmpty()){
							List updRelList = new ArrayList();
							List updRelInfoList = new ArrayList();
							for(int i=0; i<bookrelList.size(); i++){
								BookRel bookRel = (BookRel)bookrelList.get(i);
								BookRel updBookRel = new BookRel();
								updBookRel.setIsbn(isbn);
								updBookRel.setCourseCode(bookRel.getCourseCode());
								updBookRel.setMajorCode(bookRel.getMajorCode());
								updBookRel.setActInd("Y");
								updBookRel.setCreUid(userId);
								updBookRel.setUpdUid(userId);
								updBookRel.setIntake(intake);
								updRelList.add(updBookRel);
								
								String grade = grades[i];
								String core = cores[i];
								String bookType = bookTypes[i];
								BookRelInfo info = new BookRelInfo();
								info.setIntake(intake);
								info.setIsbn(isbn);
								info.setCourseCode(bookRel.getCourseCode());
								info.setMajorCode(bookRel.getMajorCode());
								info.setGrade(grade);
								info.setCe(core);
								info.setBookType(bookType);
								info.setActInd("Y");
								info.setCreUid(userId);
								info.setUpdUid(userId);
								updRelInfoList.add(info);
							}
							bookRelUpd = relImpl.saveBookRelInDB(conn, updRelList);
							bookRelInfo = relInfoImpl.saveBookRelInfo(conn, updRelInfoList);
							if(bookRelUpd > 0 && bookRelInfo > 0){
								conn.commit();
								session.removeAttribute("isbn");
								session.removeAttribute("editM");
								session.removeAttribute("bookRelList");
								session.removeAttribute("courseNameList");
								session.removeAttribute("courselist");
								
								request.setAttribute("msg", "Book data modification success!");
							} else {
								conn.rollback();
								request.setAttribute("msg", "book data modification failed!");
							}
						} else {
							List updRelList = new ArrayList();
							List updRelInfoList = new ArrayList();
							for(int i=0; i<bookrelList.size(); i++){
								BookRel bookRel = (BookRel)bookrelList.get(i);
								BookRel updBookRel = new BookRel();
								updBookRel.setIsbn(isbn);
								updBookRel.setCourseCode(bookRel.getCourseCode());
								updBookRel.setMajorCode(bookRel.getMajorCode());
								updBookRel.setActInd("Y");
								updBookRel.setCreUid(userId);
								updBookRel.setUpdUid(userId);
								updBookRel.setIntake(intake);
								updRelList.add(updBookRel);
								
								String grade = grades[i];
								String core = cores[i];
								String bookType = bookTypes[i];
								BookRelInfo info = new BookRelInfo();
								info.setIntake(intake);
								info.setIsbn(isbn);
								info.setCourseCode(bookRel.getCourseCode());
								info.setMajorCode(bookRel.getMajorCode());
								info.setGrade(grade);
								info.setCe(core);
								info.setBookType(bookType);
								info.setActInd("Y");
								info.setCreUid(userId);
								info.setUpdUid(userId);
								updRelInfoList.add(info);
							}
							List delRelList = new ArrayList();
							for(int i=0; i<delList.size(); i++){
								BookRel delRel = (BookRel)delList.get(i);
								BookRel delBookRel = new BookRel();
								delBookRel.setIsbn(isbn);
								delBookRel.setCourseCode(delRel.getCourseCode());
								delBookRel.setMajorCode(delRel.getMajorCode());
								delBookRel.setActInd("N");
								delBookRel.setCreUid(userId);
								delBookRel.setUpdUid(userId);
								delBookRel.setIntake(intake);
								delRelList.add(delBookRel);
							}
							bookRelDel = relImpl.deleteBookRelList(conn, delRelList);
							bookRelUpd = relImpl.saveBookRelInDB(conn, updRelList);
							bookRelInfo = relInfoImpl.saveBookRelInfo(conn, updRelInfoList);
							
							if(bookRelUpd > 0 && bookRelDel > 0 && bookRelInfo > 0){
								conn.commit();
								session.removeAttribute("isbn");
								session.removeAttribute("editM");
								session.removeAttribute("bookRelList");
								session.removeAttribute("courseNameList");
								session.removeAttribute("courselist");
								
								request.setAttribute("msg", "Book data modification success!");
							} else {
								conn.rollback();
								request.setAttribute("msg", "book data modification failed!");
							}
						}
					} else {
						conn.rollback();
						request.setAttribute("msg", "book data modification failed!");	
					}
				}
			}
			
			request.setAttribute("type", "editBook");
			request.getRequestDispatcher("msg.jsp").forward(request, response);
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
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
