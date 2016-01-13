package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookImport;
import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.BookRelInfo;
import edu.must.tos.bean.Price;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookRelDAOImpl;
import edu.must.tos.impl.BookRelInfoDAOImpl;
import edu.must.tos.impl.PriceDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;

public class SaveBookServlet extends HttpServlet {

	public SaveBookServlet() {
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
			
			String userId = (String)request.getSession().getAttribute("userId");
			
			List bookImportList = (List)request.getSession().getAttribute("bookImportList");
			int size = bookImportList.size();
			
			BookDAOImpl bookDAOImpl = new BookDAOImpl(); 
			PriceDAOImpl priceDAOImpl = new PriceDAOImpl();
			BookRelDAOImpl bookRelDAOImpl = new BookRelDAOImpl();
			BookRelInfoDAOImpl bookRelInfoDAOImpl = new BookRelInfoDAOImpl();
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			
			String key = "CURRINTAKE";
			String currIntake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			boolean resultFlag = true;
			for(int i=0; i<size; i++){
				BookImport bookImport = (BookImport)bookImportList.get(i);
				Book book = new Book();
				book.setIsbn(bookImport.getIsbn());
				book.setTitle(bookImport.getTitle());
				book.setAuthor(bookImport.getAuthor());
				book.setPublisher(bookImport.getPublisher());
				book.setEdition(bookImport.getEdition());
				book.setLanguage(bookImport.getLanguage().toUpperCase());
				book.setBookType(bookImport.getBookType().toUpperCase());
				book.setRemarks(bookImport.getRemarks());
				book.setActInd("Y");
				book.setUpdUid(userId);
				book.setUpdDate(new Date());
				book.setWithdrawInd(bookImport.getWithdrawInd().toUpperCase());
				book.setSupplierCode1(bookImport.getSupplierCode1().toUpperCase());
				book.setSupplierCode2(bookImport.getSupplierCode2().toUpperCase());
				book.setPublishYear(bookImport.getPublishYear());
				book.setSupplement(bookImport.getSupplement().toUpperCase());
				book.setCurrency(bookImport.getCurrency().toUpperCase());
				book.setUnitPrice(Double.parseDouble(bookImport.getUnitPrice()));
				book.setDisCount(Double.parseDouble(bookImport.getDisCount()));
				book.setFavourablePrice(Double.parseDouble(bookImport.getFavourablePrice()));
				book.setGrade(bookImport.getGrade());
				book.setCls(bookImport.getCls());
				book.setCore(bookImport.getCore());
				
				Double withdrawPrice = sysConfigDAOImpl.getWithDrawPrice(conn, book.getLanguage());
				Price priceMop = new Price();
				priceMop.setIsbn(bookImport.getIsbn());
				priceMop.setCurrency("MOP");
				priceMop.setFuturePrice(Double.parseDouble(bookImport.getFuturePrice_MOP()));
				priceMop.setNetPrice(0);
				priceMop.setWithdrawPrice(withdrawPrice);
				priceMop.setIntake(currIntake);
				priceMop.setActInd("Y");
				priceMop.setUpdUid(userId);
				priceMop.setUpdDate(new Date());
				
				Price priceRmb = new Price();
				priceRmb.setIsbn(bookImport.getIsbn());
				priceRmb.setCurrency("RMB");
				priceRmb.setFuturePrice(Double.parseDouble(bookImport.getFuturePrice_RMB()));
				priceRmb.setNetPrice(0);
				priceRmb.setWithdrawPrice(withdrawPrice);
				priceRmb.setIntake(currIntake);
				priceRmb.setActInd("Y");
				priceRmb.setUpdUid(userId);
				priceRmb.setUpdDate(new Date());
				List priceList = new ArrayList();
				priceList.add(priceMop);
				priceList.add(priceRmb);
				
				BookRel bookRel = new BookRel();
				bookRel.setIntake(currIntake);
				bookRel.setIsbn(book.getIsbn());
				bookRel.setCourseCode(bookImport.getCourseCode().toUpperCase());
				bookRel.setMajorCode(bookImport.getMajorCode().toUpperCase());
				bookRel.setActInd("Y");
				bookRel.setUpdUid(userId);
				bookRel.setUpdDate(new Date());
				List bookRelList = new ArrayList();
				bookRelList.add(bookRel);
				
				BookRelInfo info = new BookRelInfo();
				info.setIntake(currIntake);
				info.setIsbn(book.getIsbn());
				info.setCourseCode(bookImport.getCourseCode().toUpperCase());
				info.setMajorCode(bookImport.getMajorCode().toUpperCase());
				info.setGrade(bookImport.getGrade());
				info.setCe(bookImport.getCore());
				info.setBookType(bookImport.getBookType().toUpperCase());
				info.setActInd("Y");
				info.setCreDate(new Date());
				info.setCreUid(userId);
				info.setUpdDate(new Date());
				info.setUpdUid(userId);
				
				int bookFlag = bookDAOImpl.saveInDB(conn, book);
				int priceFlag = priceDAOImpl.savePriceInDB(conn, priceList);
				int bookRelFlag = bookRelDAOImpl.saveBookRelInDB(conn, bookRelList);
				int bookRelInfoFlag = bookRelInfoDAOImpl.saveBookRelInfo(conn, info);
				
				if(bookFlag == 0 || priceFlag == 0 || bookRelFlag == 0 || bookRelInfoFlag == 0){
					resultFlag = false;
					break;
				}			
			}
			
			if (resultFlag) {
				conn.commit();
				request.setAttribute("msg", "圖書資料導入成功！");
				request.setAttribute("type", "uploadBook");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			} else {
				conn.rollback();
				request.setAttribute("msg", "圖書資料導入有誤，請與管理員聯繫！");
				request.setAttribute("type", "uploadBook");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
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

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
