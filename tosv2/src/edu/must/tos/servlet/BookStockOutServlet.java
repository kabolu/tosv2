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
import javax.sql.DataSource;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookInventory;
import edu.must.tos.bean.BookInventoryInfo;
import edu.must.tos.bean.BookPurchasing;
import edu.must.tos.bean.BookPurchasingBean;
import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.BookStockIn;
import edu.must.tos.bean.BookStockOut;
import edu.must.tos.bean.BookStockOutBean;
import edu.must.tos.bean.BookStockOutOrder;
import edu.must.tos.bean.BookSupplier;
import edu.must.tos.bean.ExassLect;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookInventoryDAOImpl;
import edu.must.tos.impl.BookPurchasingDAOImpl;
import edu.must.tos.impl.BookRelDAOImpl;
import edu.must.tos.impl.BookStockInDAOImpl;
import edu.must.tos.impl.BookStockOutDAOImpl;
import edu.must.tos.impl.BookStockOutOrderDAOImpl;
import edu.must.tos.impl.BookSupplierDAOImpl;
import edu.must.tos.impl.ExassLectDAOImpl;
import edu.must.tos.util.ToolsOfString;

public class BookStockOutServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public BookStockOutServlet() {
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
			
			BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
			BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
			BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
			
			String userId = (String)request.getSession().getAttribute("userId");			
			String curIntake = (String)request.getSession().getAttribute("curIntake");
			
			BookSupplier outSupplier = new BookSupplier();
			outSupplier.setIo("O");
			List supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, outSupplier);
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("cleanSession")){
				request.getSession().removeAttribute("bookStockOutOrder");
				request.getSession().removeAttribute("searchInfoList");
				request.getSession().removeAttribute("infoList");
				request.getSession().removeAttribute("courses");
				request.getSession().removeAttribute("lecturers");
				request.getSession().removeAttribute("orderSupplier");
				request.setAttribute("supplierList", supplierList);
				request.setAttribute("isp", "N");
				request.getRequestDispatcher("bookStockOut.jsp").forward(request, response);
			}else if(type != null && type.equals("saveSession")){
				saveInSession(conn, curIntake, userId, request, response);
			}else if(type != null && type.equals("remove")){
				String i = request.getParameter("i");
				int id = Integer.parseInt(i);
				List infoList = (List)request.getSession().getAttribute("infoList");
				List courses = (List)request.getSession().getAttribute("courses");
				List lecturers = (List)request.getSession().getAttribute("lecturers");
				// rainbow add
				BookStockOutOrder bookStockOutOrder = (BookStockOutOrder)request.getSession().getAttribute("bookStockOutOrder");
				
				infoList.remove(id);
				courses.remove(id);
				lecturers.remove(id);
				request.getSession().setAttribute("infoList", infoList);
				request.setAttribute("isp", bookStockOutOrder.getIsp());
				request.setAttribute("supplierList", supplierList);
				request.getRequestDispatcher("bookStockOut.jsp").forward(request, response);
			}
			else if(type != null && type.equals("purchaseOut")){
				doPurchaseOut(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("saveRecords")){
				saveRecords(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("checkOutPurchase")){
				String isbn = null;
				if(request.getParameter("isbn") != null && !request.getParameter("isbn").equals("")){
					isbn = request.getParameter("isbn");
				}
				int adjnum = 0;
				if(request.getParameter("adjnum") != null && !request.getParameter("adjnum").equals("")){
					adjnum = Integer.parseInt(request.getParameter("adjnum"));
				}
				BookInventory bookInventory = bookInventoryDAOImpl.getBookInventoryByPK(conn, ToolsOfString.getUniqueIsbn(isbn));
				if(bookInventory != null){
					if(adjnum > bookInventory.getStock()){
						out.println(0);
					}else{
						out.println(1);
					}
				} else {
					out.print(0);
				}
			}
			else if(type != null && type.equals("checkOutAdjnum")){
				checkOutAdjNum(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("updateStockOut")){
				updateStockOut(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("sessionRemarks")){
				saveRemarksInSession(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("sessionAdjnum")){
				saveAdjNumInSession(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("searchPrNum")){
				request.setAttribute("supplierList", supplierList);
				searchPrNum(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("searchSupplier")){ //add by Rainbow
				request.setAttribute("supplierList", supplierList);
				searchSupplier(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("stockOutDetail")){
				String prNum = null;
				if(request.getParameter("prNum") != null){
					prNum = request.getParameter("prNum");
				}
				if(prNum != null && !"".equals(prNum)){
					List list = bookStockOutDAOImpl.getStockOutInfoList(conn, null, prNum, null, null, null, 0);
					request.setAttribute("detailList", list);
				}
				request.setAttribute("prNum", prNum);
				request.getRequestDispatcher("bookStockOutDetail.jsp").forward(request, response);
			}
			else if(type != null && type.equals("printStockOutList")){
				String prNum = request.getParameter("prNum");
				List stockOutList = bookStockOutDAOImpl.getStockOutInfoList(conn, null, prNum, null, null, null, 0);
				request.setAttribute("stockOutList", stockOutList);
				request.getRequestDispatcher("bookStockOutDetailList.jsp").forward(request, response);
			}
			else if(type != null && type.equals("deleteStockOut")){
				deleteStockOut(conn, curIntake, userId, request, response);
			}
			else{
				request.setAttribute("supplierList", supplierList);
				doStockOut(conn, curIntake, userId, request, response);
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

	private void checkOutAdjNum(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
		
		String isbn = null;
		if(request.getParameter("isbn") != null && !request.getParameter("isbn").equals("")){
			isbn = request.getParameter("isbn");
		}
		int adjnum = 0;
		if(request.getParameter("adjnum") != null && !request.getParameter("adjnum").equals("")){
			adjnum = Integer.parseInt(request.getParameter("adjnum"));
		}
		int oldAdjnum = 0;
		if(request.getParameter("oldAdjnum") != null && !request.getParameter("oldAdjnum").equals("")){
			oldAdjnum = Integer.parseInt(request.getParameter("oldAdjnum"));
		}
		//-------------------
		int infoListAdjnumSum = 0;
		List<BookInventoryInfo> infoList = (List)request.getSession().getAttribute("infoList");
		if(infoList != null && !infoList.isEmpty()){
			for(BookInventoryInfo bInfo : infoList){
				if(isbn.equals(bInfo.getIsbn())){
					infoListAdjnumSum += bInfo.getAdjnum();
				}
			}
		}
		int differ = adjnum - oldAdjnum;
		if(differ > 0){
			infoListAdjnumSum = infoListAdjnumSum + differ;
		}
		BookInventory bookInventory = bookInventoryDAOImpl.getBookInventoryByPK(conn, ToolsOfString.getUniqueIsbn(isbn));
		if(bookInventory != null){
			if(infoListAdjnumSum > bookInventory.getStock()){
				out.println(0);
			}else{
				out.println(1);
			}			
		} else {
			out.println(0);
		}
	}

	private void doStockOut(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		BookPurchasingDAOImpl bookPurchasingDAOImpl = new BookPurchasingDAOImpl();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		BookStockOutOrderDAOImpl bookStockOutOrderDAOImpl = new BookStockOutOrderDAOImpl();
		
		String isbn = "";
		if(request.getParameter("isbn") != null && !request.getParameter("isbn").trim().equals("")){
			isbn = request.getParameter("isbn").trim();
		}
		String prDate = "";
		if(request.getSession().getAttribute("prDate") != null && !request.getSession().getAttribute("prDate").equals("")){
			prDate = (String)request.getSession().getAttribute("prDate");
		}
		String prNum = "";
		if(request.getSession().getAttribute("prNum") != null && !request.getSession().getAttribute("prNum").equals("")){
			prNum = (String)request.getSession().getAttribute("prNum");
		}else{
			prNum = request.getParameter("prNumParam");
		}		
		//search tbookpurchasing table first
		boolean isExist = false;
		List<BookPurchasingBean> purchasingList = new ArrayList<BookPurchasingBean>();
		if(!"".equals(prNum) && prNum != null)
			purchasingList = bookPurchasingDAOImpl.getBookPurchasingBeanByOrderNo(conn, prNum);
		if(purchasingList != null && !purchasingList.isEmpty()){
			isExist = true;
		}
		if(isExist){
			request.getRequestDispatcher("BookStockOutServlet?type=searchPrNum&prNum="+prNum).forward(request, response);
		}else{
			Book book = bookDAOImpl.getBookByPK(conn, isbn);
			//輸入的ISBN是否存在
			if(book.getIsbn() == null){
				List searchInfoList = (List)request.getSession().getAttribute("searchInfoList");
				if(searchInfoList == null){
					searchInfoList = bookStockOutOrderDAOImpl.getStockOutListByPrNum(conn, prNum);
				}
				BookStockOutOrder bookStockOutOrder = (BookStockOutOrder)request.getSession().getAttribute("bookStockOutOrder");
				if(bookStockOutOrder == null){
					bookStockOutOrder  = bookStockOutOrderDAOImpl.getStockOutOrderByPrNum(conn, prNum);
				}
				request.getSession().setAttribute("bookStockOutOrder", bookStockOutOrder);
				request.getSession().setAttribute("searchInfoList", searchInfoList);
				
				request.setAttribute("wrong", "沒有該圖書資料!");
				
				request.setAttribute("isp", bookStockOutOrder.getIsp());
				request.getRequestDispatcher("bookStockOut.jsp").forward(request, response);
			} else {
				BookStockOutOrder checkOutOrder  = bookStockOutOrderDAOImpl.getStockOutOrderByPrNum(conn, prNum);
				if(checkOutOrder != null && checkOutOrder.getIsp().equals("Y")){
					request.getRequestDispatcher("BookStockOutServlet?type=searchPrNum&prNum="+prNum).forward(request, response);
				}else{
					forwardPage(conn, request, response, curIntake, isbn, book.getTitle(), prNum, prDate, book.getFavourablePrice(), book.getUnitPrice());
				}
			}
		}
	}

	private void searchSupplier(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
		BookPurchasingDAOImpl bookPurchasingDAOImpl = new BookPurchasingDAOImpl();
		
		request.getSession().removeAttribute("bookStockOutOrder");
		request.getSession().removeAttribute("searchInfoList");
		request.getSession().removeAttribute("infoList");
		request.getSession().removeAttribute("orderSupplier");
		
		String supplierNo = "0";
		if(request.getParameter("supplierNo")!=null && !request.getParameter("supplierNo").equals("")){
			supplierNo = request.getParameter("supplierNo");
		}

		BookSupplier searchSupplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, Integer.parseInt(supplierNo));
		//search tbookpurchasing table first
		List<BookPurchasingBean> purchasingList = bookPurchasingDAOImpl.getBookPurchasingBeanBySupplierCode(conn, searchSupplier.getSupplierCode().toUpperCase()+curIntake, curIntake);
		BookStockOutOrder bookStockOutOrder = null;
		List list = new ArrayList();
		String isp = "N";
		if(purchasingList != null && !purchasingList.isEmpty()){
			isp = "Y";
			BookPurchasingBean bean = (BookPurchasingBean)purchasingList.get(0);
			bookStockOutOrder = new BookStockOutOrder();
			bookStockOutOrder.setStockOutNo(0);
			bookStockOutOrder.setIntake(bean.getBookPurchase().getIntake());
			bookStockOutOrder.setPrnum(bean.getOrderNo());
			bookStockOutOrder.setSupplierNo(Integer.parseInt(supplierNo));
			bookStockOutOrder.setPaidCurrency("");
			bookStockOutOrder.setOutDate("");
			bookStockOutOrder.setActind("Y");
			List sCourses = (List)request.getSession().getAttribute("sCourses");
			if(sCourses == null){
				sCourses = new ArrayList();
			}
			for(BookPurchasingBean pBean : purchasingList){
				BookStockOutBean bStockOut = new BookStockOutBean();
				bStockOut.setIsbn(pBean.getBook().getIsbn());
				bStockOut.getBook().setTitle(pBean.getBook().getTitle());
				bStockOut.setPrNum(pBean.getOrderNo());
				bStockOut.setOutDate("");
				bStockOut.setRemarks("");
				bStockOut.setCostPrice(pBean.getBookPurchase().getCostPrice());
				
				//Rainbow add
				BookDAOImpl bookDaoImpl = new BookDAOImpl();
				Book book = bookDaoImpl.getBookByPK(conn, bStockOut.getIsbn());
				bStockOut.getBook().setPublisher(book.getPublisher());
				bStockOut.getBook().setPublishYear(book.getPublishYear());
				bStockOut.getBook().setEdition(book.getEdition());
				bStockOut.getBook().setAuthor(book.getAuthor());
				
				bStockOut.setAdjNum(pBean.getBookPurchase().getQuantity());
				bStockOut.setOutNo(pBean.getId());
				bStockOut.setPurchaseLeave(pBean.getBookPurchase().getLeave());
				list.add(bStockOut);
				
				BookRelDAOImpl bookRelDAOImpl = new BookRelDAOImpl();
				BookRel rel = new BookRel();
				rel.setIntake(curIntake);
				rel.setIsbn(pBean.getBook().getIsbn());
				rel.setActInd("Y");					
				List courseList = bookRelDAOImpl.getBookRelList(conn, rel);
				sCourses.add(courseList);
			}
			request.getSession().setAttribute("sCourses", sCourses);
		}
		request.setAttribute("isp", isp);
		request.getSession().setAttribute("search", "search");
		request.getSession().setAttribute("searchInfoList", list);
		request.getSession().setAttribute("bookStockOutOrder", bookStockOutOrder);
		request.getRequestDispatcher("bookStockOut.jsp").forward(request, response);
	}

	private void searchPrNum(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		BookPurchasingDAOImpl bookPurchasingDAOImpl = new BookPurchasingDAOImpl();
		BookStockOutOrderDAOImpl bookStockOutOrderDAOImpl = new BookStockOutOrderDAOImpl();
		
		String prDate = request.getParameter("prDate");
		String paidCurrency = request.getParameter("paidCurrency");
		String supplierNo = "0";
		if(request.getParameter("supplierNo") != null && !request.getParameter("supplierNo").equals("")){
			supplierNo = request.getParameter("supplierNo");
		}
		String prNum = request.getParameter("prNum");
		if(prNum != null && !prNum.equals("")){
			prNum = prNum.trim();
		}
		//search tbookpurchasing table first
		List<BookPurchasingBean> purchasingList = bookPurchasingDAOImpl.getBookPurchasingBeanByOrderNo(conn, prNum);
		BookStockOutOrder bookStockOutOrder = null;
		List list = new ArrayList();
		String isp = "N";
		if(purchasingList != null && !purchasingList.isEmpty()){
			isp = "Y";
			BookPurchasingBean bean = (BookPurchasingBean)purchasingList.get(0);
			bookStockOutOrder = new BookStockOutOrder();
			bookStockOutOrder.setStockOutNo(0);
			bookStockOutOrder.setIntake(bean.getBookPurchase().getIntake());
			bookStockOutOrder.setPrnum(bean.getOrderNo());
			bookStockOutOrder.setSupplierNo(Integer.parseInt(bean.getBookPurchase().getOrderBy()));
			bookStockOutOrder.setPaidCurrency("");
			bookStockOutOrder.setOutDate("");
			bookStockOutOrder.setActind("Y");
			for(BookPurchasingBean pBean : purchasingList){
				BookStockOutBean bStockOut = new BookStockOutBean();
				bStockOut.setIsbn(pBean.getBook().getIsbn());
				bStockOut.getBook().setTitle(pBean.getBook().getTitle());
				bStockOut.setPrNum(pBean.getOrderNo());
				bStockOut.setOutDate("");
				bStockOut.setRemarks("");
				bStockOut.setPurchasePrice(Double.valueOf(0));
				bStockOut.setDisCount(Double.valueOf(0));
				//remove by xfke
				//bStockOut.setCostPrice(Double.valueOf(0));
				//add by xfke
				bStockOut.setCostPrice(pBean.getBookPurchase().getCostPrice());
				
				//Rainbow add
				BookDAOImpl bookDaoImpl = new BookDAOImpl();
				Book book = bookDaoImpl.getBookByPK(conn, bStockOut.getIsbn());
				bStockOut.getBook().setPublisher(book.getPublisher());
				bStockOut.getBook().setPublishYear(book.getPublishYear());
				bStockOut.getBook().setEdition(book.getEdition());
				bStockOut.getBook().setAuthor(book.getAuthor());
				
				bStockOut.setAdjNum(pBean.getBookPurchase().getQuantity());
				bStockOut.setOutNo(pBean.getId());
				bStockOut.setPurchaseLeave(pBean.getBookPurchase().getLeave());
				list.add(bStockOut);
			}
		}else{
			bookStockOutOrder = bookStockOutOrderDAOImpl.getStockOutOrderByPrNum(conn, prNum);
			if(bookStockOutOrder == null){
				bookStockOutOrder = new BookStockOutOrder();
				bookStockOutOrder.setPrnum(prNum);
				bookStockOutOrder.setOutDate(prDate);
				bookStockOutOrder.setSupplierNo(Integer.parseInt(supplierNo));
				bookStockOutOrder.setPaidCurrency(paidCurrency);
				bookStockOutOrder.setIntake(curIntake);
				bookStockOutOrder.setActind("Y");
				bookStockOutOrder.setIsp("N");
			}
			else if(bookStockOutOrder != null && bookStockOutOrder.getIsp().equals("Y")){
				prNum = null;
				bookStockOutOrder = null;
			}
			
			if(prNum != null && !prNum.equals(""))
				list = bookStockOutOrderDAOImpl.getStockOutListByPrNum(conn, prNum);
			if(!list.isEmpty()){
				BookRelDAOImpl bookRelDAOImpl = new BookRelDAOImpl();
				List sCourses = (List)request.getSession().getAttribute("sCourses");
				if(sCourses == null){
					sCourses = new ArrayList();
				}
				for(int i=0; i<list.size(); i++){
					BookStockOutBean bean = (BookStockOutBean)list.get(i);
					BookRel rel = new BookRel();
					rel.setIntake(curIntake);
					rel.setIsbn(bean.getIsbn());
					rel.setActInd("Y");					
					List courseList = bookRelDAOImpl.getBookRelList(conn, rel);
					sCourses.add(courseList);
				}
				request.getSession().setAttribute("sCourses", sCourses);
			}
		}
		request.setAttribute("isp", isp);
		request.getSession().setAttribute("search", "search");
		request.setAttribute("searchPrNum", prNum);
		request.getSession().setAttribute("searchInfoList", list);
		request.getSession().setAttribute("bookStockOutOrder", bookStockOutOrder);
		request.getRequestDispatcher("bookStockOut.jsp").forward(request, response);
	}

	private void saveAdjNumInSession(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
		
		int adjnum = 0;
		if(request.getParameter("adjnum") != null && !request.getParameter("adjnum").equals("")){
			if(request.getParameter("adjnum").substring(0, 1).equals("-")){
				if(request.getParameter("adjnum").length() > 1){
					adjnum = Integer.parseInt(request.getParameter("adjnum").substring(1, request.getParameter("adjnum").length()));
					adjnum = -adjnum;
				}
			}else{
				adjnum = Integer.parseInt(request.getParameter("adjnum"));
			}
		}
		int i = Integer.parseInt(request.getParameter("i"));
		
		int adjnumSum = adjnum;
		List<BookStockOutBean> infoList = (List)request.getSession().getAttribute("infoList");
		BookStockOutBean info = (BookStockOutBean)infoList.get(i);
		
		if(infoList != null && infoList.size()>=2){
			for(int j=0; j<infoList.size(); j++){
				BookStockOutBean bookInventoryInfo = (BookStockOutBean)infoList.get(j);
				if(j != i){
					if(info.getIsbn().equals(bookInventoryInfo.getIsbn())){
						adjnumSum += bookInventoryInfo.getAdjNum();
					}
				}
			}
		}
		//adjnumSum = adjnum + adjnumSum;
		BookInventory bookInventory = bookInventoryDAOImpl.getBookInventoryByPK(conn, ToolsOfString.getUniqueIsbn(info.getIsbn()));
		if(bookInventory != null){
			if(adjnumSum > bookInventory.getStock()){
				out.print(0);
			}else{
				out.print(1);
			}
		} else {
			out.print(0);
		}
		
		info.setAdjNum(adjnum);
		request.getSession().setAttribute("infoList", infoList);
	}

	private void saveRemarksInSession(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String remarks = "";
		if(request.getParameter("remarks") != null && !request.getParameter("remarks").equals("")){
			remarks = request.getParameter("remarks");
		}
		double purchasePrice = 0;
		if(request.getParameter("purchasePrice") != null && !request.getParameter("purchasePrice").equals("")){
			if(this.isNumber(request.getParameter("purchasePrice"))){
				purchasePrice = Double.parseDouble(request.getParameter("purchasePrice"));
			}
		}
		double discount = 0;
		if(request.getParameter("discount") != null && !request.getParameter("discount").equals("")){
			if(this.isNumber(request.getParameter("discount"))){
				discount = Double.parseDouble(request.getParameter("discount"));
			}
		}
		double costPrice = 0;
		if(request.getParameter("costPrice") != null && !request.getParameter("costPrice").equals("")){
			if(this.isNumber(request.getParameter("costPrice"))){
				costPrice = Double.parseDouble(request.getParameter("costPrice"));
			}
		}
		String courseCode = "";
		if(request.getParameter("courseCode") != null && !request.getParameter("courseCode").equals("")){
			courseCode = request.getParameter("courseCode");
		}
		String lectCode = "";
		if(request.getParameter("lectCode") != null && !request.getParameter("lectCode").equals("")){
			lectCode = request.getParameter("lectCode");
		}
		int i = Integer.parseInt(request.getParameter("i"));
		List infoList = (List)request.getSession().getAttribute("infoList");
		BookStockOutBean info = (BookStockOutBean)infoList.get(i);
		if(info != null){
			info.setRemarks(remarks);
			info.setPurchasePrice(purchasePrice);
			info.setDisCount(discount);
			info.setCostPrice(costPrice);
			info.setCourseCode(courseCode);
			info.setLectCode(lectCode);
			//Rainbow add
			BookDAOImpl bookDaoImpl = new BookDAOImpl();
			Book book = bookDaoImpl.getBookByPK(conn, info.getIsbn());
			info.getBook().setPublisher(book.getPublisher());
			info.getBook().setPublishYear(book.getPublishYear());
			info.getBook().setEdition(book.getEdition());
			info.getBook().setAuthor(book.getAuthor());
		}
		
		request.getSession().setAttribute("infoList", infoList);
	}

	private void updateStockOut(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
		BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
		
		int no = 0;
		if(request.getParameter("no") != null && !request.getParameter("no").equals("")){
			no = Integer.parseInt(request.getParameter("no"));
		}
		String prNum = null;
		if(request.getParameter("prNum") != null && !request.getParameter("prNum").equals("")){
			prNum = request.getParameter("prNum");
		}
		String isbn = null;
		if(request.getParameter("isbn") != null && !request.getParameter("isbn").equals("")){
			isbn = request.getParameter("isbn");
		}
		double sPurchasePrice = 0;
		if(request.getParameter("sPurchasePrice") != null && !request.getParameter("sPurchasePrice").equals("")){
			sPurchasePrice = Double.parseDouble(request.getParameter("sPurchasePrice"));
		}
		double sDiscount = 0;
		if(request.getParameter("sDiscount") != null && !request.getParameter("sDiscount").equals("")){
			sDiscount = Double.parseDouble(request.getParameter("sDiscount"));
		}
		double sCostPrice = 0;
		if(request.getParameter("sCostPrice") != null && !request.getParameter("sCostPrice").equals("")){
			sCostPrice = Double.parseDouble(request.getParameter("sCostPrice"));
		}
		String remarks = "";
		if(request.getParameter("sRemarks") != null && !request.getParameter("sRemarks").equals("")){
			remarks = request.getParameter("sRemarks");
		}
		int i = 0;
		if(request.getParameter("i") != null && !request.getParameter("i").equals("")){
			i = Integer.parseInt(request.getParameter("i"));
		}
		String adjnum = null;
		if(request.getParameter("adjnum") != null && !request.getParameter("adjnum").equals("")){
			adjnum = request.getParameter("adjnum");
		}
		int oldAdjnum = 0;
		if(request.getParameter("oldAdjnum") != null && !request.getParameter("oldAdjnum").equals("")){
			oldAdjnum = Integer.parseInt(request.getParameter("oldAdjnum"));
		}
		String crsCode = "";
		if(request.getParameter("crsCode") != null && !request.getParameter("crsCode").equals("")){
			crsCode = request.getParameter("crsCode");
		}
		String lectCode = "";
		if(request.getParameter("lectCode") != null && !request.getParameter("lectCode").equals("")){
			lectCode = request.getParameter("lectCode");
		}
		if(adjnum != null && !adjnum.equals("")){
			if(Integer.parseInt(adjnum) == 0){	//---------delete record
				BookStockOut bo = new BookStockOut();
				bo.setOutNo(no);
				bo.setPRnum(prNum);
				bo.setIsbn(isbn);
				bo.setPurchasePrice(sPurchasePrice);
				bo.setDiscount(sDiscount);
				bo.setCostPrice(sCostPrice);
				bo.setAdjnum(0);
				bo.setRemarks(remarks);
				bo.setActind("N");
				bo.setCourseCode(crsCode);
				bo.setLectCode(lectCode);
				bo.setUpddate(new Date());
				bo.setUpduid(userId);
				boolean flag = bookStockOutDAOImpl.updateBookStockOut(conn, bo);
				
				BookInventory inventry = new BookInventory();
				inventry.setIsbn(ToolsOfString.getUniqueIsbn(isbn));
				inventry.setStock(oldAdjnum);	//因為是已經出貨的，所以要將數量加回到庫存
				inventry.setUpddate(new Date());
				inventry.setUpduid(userId);
				boolean updateInventoryFlag = bookInventoryDAOImpl.updateBookInventoryByIsbn(conn, inventry);
				if(flag && updateInventoryFlag){
					conn.commit();
					out.print("delete");
				}else{
					conn.rollback();
					out.print("false");
				}
			}else{
				BookInventory bookInventory = bookInventoryDAOImpl.getBookInventoryByPK(conn, ToolsOfString.getUniqueIsbn(isbn));
				int differ = -(Integer.parseInt(adjnum) - oldAdjnum);	//出貨記錄被修改時，差值要為負數
				int rightNumber = bookInventory.getStock() + differ;
				if(rightNumber < 0){
					out.print("negative");
				}else {
					BookStockOut bi = new BookStockOut();
					bi.setOutNo(no);
					bi.setPRnum(prNum);
					bi.setIsbn(isbn);
					bi.setPurchasePrice(sPurchasePrice);
					bi.setDiscount(sDiscount);
					bi.setCostPrice(sCostPrice);
					bi.setAdjnum(Integer.parseInt(adjnum));
					bi.setRemarks(remarks);
					bi.setActind("Y");
					bi.setCourseCode(crsCode);
					bi.setLectCode(lectCode);
					bi.setUpddate(new Date());
					bi.setUpduid(userId);
					boolean flag = bookStockOutDAOImpl.updateBookStockOut(conn, bi);
					
					BookInventory inventry = new BookInventory();
					inventry.setIsbn(ToolsOfString.getUniqueIsbn(isbn));
					inventry.setStock(differ);
					inventry.setUpddate(new Date());
					inventry.setUpduid(userId);
					boolean updateInventoryFlag = bookInventoryDAOImpl.updateBookInventoryByIsbn(conn, inventry);
					if(flag && updateInventoryFlag){
						conn.commit();
						out.print("delete");
					}else{
						conn.rollback();
						out.print("false");
					}
				}						
			}					
		}else{
			BookStockOut bi = new BookStockOut();
			bi.setOutNo(no);
			bi.setPRnum(prNum);
			bi.setIsbn(isbn);
			bi.setPurchasePrice(sPurchasePrice);
			bi.setDiscount(sDiscount);
			bi.setCostPrice(sCostPrice);
			bi.setAdjnum(oldAdjnum);
			bi.setActind("Y");
			bi.setRemarks(remarks);
			bi.setCourseCode(crsCode);
			bi.setLectCode(lectCode);
			bi.setUpddate(new Date());
			bi.setUpduid(userId);
			boolean flag = bookStockOutDAOImpl.updateBookStockOut(conn, bi);
			if(flag){
				conn.commit();
				out.print("true");
				List searchInfoList = (List)request.getSession().getAttribute("searchInfoList");
				BookStockOutBean searchInfo = new BookStockOutBean();
				searchInfo = (BookStockOutBean)searchInfoList.get(i);
				searchInfo.setPurchasePrice(sPurchasePrice);
				searchInfo.setDisCount(sDiscount);
				searchInfo.setCostPrice(sCostPrice);
				request.getSession().setAttribute("searchInfoList", searchInfoList);
			}else{
				conn.rollback();
				out.print("false");
			}
		}
	}

	private void saveRecords(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
		BookStockOutOrderDAOImpl bookStockOutOrderDAOImpl = new BookStockOutOrderDAOImpl();
		BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
		BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
		
		String prDate = request.getParameter("prDate");
		String prNum = request.getParameter("prNum");
		String paidCurrency = request.getParameter("paidCurrency");
		int supplierNo = 0;
		BookSupplier orderSupplier = new BookSupplier();
		
		if(request.getParameter("supplierNo") != null && !request.getParameter("supplierNo").equals("")){
			supplierNo = Integer.parseInt(request.getParameter("supplierNo"));
			orderSupplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, supplierNo);
			request.getSession().setAttribute("orderSupplier", orderSupplier.getSupplierName());
		}
		int searchSize = 0;
		if(request.getParameter("searchSize") != null){
			searchSize = Integer.parseInt(request.getParameter("searchSize"));
		}		
		
		BookStockOutOrder bookStockOutOrder = new BookStockOutOrder();
		bookStockOutOrder.setIntake(curIntake);
		bookStockOutOrder.setPrnum(prNum);
		bookStockOutOrder.setSupplierNo(supplierNo);
		bookStockOutOrder.setOutDate(prDate);
		bookStockOutOrder.setPaidCurrency(paidCurrency);
		bookStockOutOrder.setIsp("N"); //Default value is N, not for the book purchase 
		bookStockOutOrder.setActind("Y");
		bookStockOutOrder.setCreDate(new Date());
		bookStockOutOrder.setCreUid(userId);
		bookStockOutOrder.setUpdDate(new Date());
		bookStockOutOrder.setUpdUid(userId);
		
		List<BookStockOutBean> infoList = (List)request.getSession().getAttribute("infoList");
		List<BookInventory> inventoryList = new ArrayList<BookInventory>();
		List<BookStockOut> stockOutList = new ArrayList<BookStockOut>();
		
		if(infoList != null && !infoList.isEmpty()){
			for(BookStockOutBean info : infoList){
				BookInventory inventory = new BookInventory();
				inventory.setIsbn(ToolsOfString.getUniqueIsbn(info.getIsbn()));
				inventory.setStock(-info.getAdjNum());	//出貨時，庫存數量要減！！
				inventory.setUpddate(new Date());
				inventory.setUpduid(userId);						
				
				BookStockOut stockOut = new BookStockOut();
				stockOut.setIntake(curIntake);
				stockOut.setIsbn(info.getIsbn());
				stockOut.setPRnum(info.getPrNum());
				stockOut.setAdjnum(info.getAdjNum());
				stockOut.setPurchasePrice(info.getPurchasePrice());
				stockOut.setDiscount(info.getDisCount());
				stockOut.setCostPrice(info.getCostPrice());
				stockOut.setRemarks(info.getRemarks());
				stockOut.setCourseCode(info.getCourseCode());
				stockOut.setPurchaseId(0);
				stockOut.setLectCode(info.getLectCode());
				stockOut.setUpddate(new Date());
				stockOut.setUpduid(userId);
				stockOut.setActind("Y");
				inventoryList.add(inventory);
				stockOutList.add(stockOut);
			}
			if(!inventoryList.isEmpty() && !stockOutList.isEmpty()){
				boolean stockOutOrder = bookStockOutOrderDAOImpl.addBookStockOutOrder(conn, bookStockOutOrder);
				boolean inventoryFlag = bookInventoryDAOImpl.updateBookInventory(conn, inventoryList);
				boolean stockOutFlag = bookStockOutDAOImpl.insertBookStockOutList(conn, stockOutList);
				if(stockOutOrder && inventoryFlag && stockOutFlag){
					conn.commit();
					out.print(1);
				}else{
					conn.rollback();
					out.print(0);
				}
			}
		} else {
			if(searchSize != 0){
				boolean stockOutOrder = bookStockOutOrderDAOImpl.addBookStockOutOrder(conn, bookStockOutOrder);
				if(stockOutOrder){
					conn.commit();
					out.print(1);
				}else{
					conn.rollback();
					out.print(0);
				}
			}
		}
	}

	private void doPurchaseOut(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String prDate = request.getParameter("prDate");
		String prNum = request.getParameter("prNum");
		String paidCurrency = request.getParameter("paidCurrency");
		int supplierNo = 0;
		if(request.getParameter("supplierNo") != null && !request.getParameter("supplierNo").equals("")){
			supplierNo = Integer.parseInt(request.getParameter("supplierNo"));
		}
		String idStr = null;
		if(request.getParameter("idStr") != null && !request.getParameter("idStr").equals("")){
			idStr = (String)request.getParameter("idStr");
		}
		String pStockOutStr = null;
		if(request.getParameter("pStockOutStr") != null && !request.getParameter("pStockOutStr").equals("")){
			pStockOutStr = (String)request.getParameter("pStockOutStr");
		}
		String pDateStr = null;
		if(request.getParameter("pDateStr") != null && !request.getParameter("pDateStr").equals("")){
			pDateStr = (String)request.getParameter("pDateStr");
		}
		String purchasePriceStr = null;
		if(request.getParameter("purchasePriceStr") != null && !request.getParameter("purchasePriceStr").equals("")){
			purchasePriceStr = (String)request.getParameter("purchasePriceStr");
		}
		String disCountStr = null;
		if(request.getParameter("disCountStr") != null && !request.getParameter("disCountStr").equals("")){
			disCountStr = (String)request.getParameter("disCountStr");
		}
		String costPriceStr = null;
		if(request.getParameter("costPriceStr") != null && !request.getParameter("costPriceStr").equals("")){
			costPriceStr = (String)request.getParameter("costPriceStr");
		}
		String crsCodes = null;
		if(request.getParameter("crsCodes") != null && !request.getParameter("crsCodes").equals("")){
			crsCodes = (String)request.getParameter("crsCodes");
		}
		String lectCodes = null;
		if(request.getParameter("lectCodes") != null && !request.getParameter("lectCodes").equals("")){
			lectCodes = (String)request.getParameter("lectCodes");
		}
		List searchInfoList = (List)request.getSession().getAttribute("searchInfoList");
		boolean flag = this.savePurchaseOut(conn, request, curIntake, userId, prDate, prNum, paidCurrency, supplierNo, 
				idStr, pStockOutStr, pDateStr, searchInfoList, purchasePriceStr, disCountStr, costPriceStr, crsCodes, lectCodes);
		if(flag){
			conn.commit();
			out.print(request.getSession().getAttribute("newPrNum").toString());
		}else{
			conn.rollback();
			out.print(0);
		}
	}

	private void saveInSession(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) {
		String prDate = "";
		if(request.getParameter("prDate") != null && !request.getParameter("prDate").trim().equals("")){
			prDate = request.getParameter("prDate");
		}
		String prNum = "";
		if(request.getParameter("prNum") != null && !request.getParameter("prNum").trim().equals("")){
			prNum = request.getParameter("prNum");
		}
		String paidCurrency = "";
		if(request.getParameter("paidCurrency") != null && !request.getParameter("paidCurrency").trim().equals("")){
			paidCurrency = request.getParameter("paidCurrency");
		}
		String supplierNo = "0";
		if(request.getParameter("supplierNo") != null && !request.getParameter("supplierNo").equals("")){
			supplierNo = request.getParameter("supplierNo");
		}
		BookStockOutOrder bookStockOutOrder = new BookStockOutOrder();
		bookStockOutOrder.setOutDate(prDate);
		bookStockOutOrder.setPrnum(prNum);
		bookStockOutOrder.setIntake(curIntake);
		bookStockOutOrder.setSupplierNo(Integer.parseInt(supplierNo));
		bookStockOutOrder.setPaidCurrency(paidCurrency);
		bookStockOutOrder.setActind("Y");
		bookStockOutOrder.setIsp("N");
		request.getSession().setAttribute("bookStockOutOrder", bookStockOutOrder);
	}

	private void deleteStockOut(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		int outNo = 0;
		if(request.getParameter("outNo") != null && !request.getParameter("outNo").equals("")){
			outNo = Integer.parseInt(request.getParameter("outNo"));
		}
		boolean flag = this.delStockOutDetail(conn, request, outNo);
		if(flag){
			conn.commit();
			out.print(0);
		}else{
			conn.rollback();
			out.print(1);
		}
	}
	
	private boolean delStockOutDetail(Connection conn, HttpServletRequest request, int outNo) throws Exception {
		boolean flag = false;
		BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
		BookStockOut b = new BookStockOut();
		b.setOutNo(outNo);
		BookStockOut bean = bookStockOutDAOImpl.getBookStockOut(conn, b);
		bean.setActind("N");
		bean.setUpddate(new Date());
		bean.setUpduid((String)request.getSession().getAttribute("userId"));
		
		//rainbow add		
		BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
		BookInventory bookInventory = bookInventoryDAOImpl.getBookInventoryByPK(conn, ToolsOfString.getUniqueIsbn(bean.getIsbn()));
		bookInventory.setStock(bean.getAdjnum());
		
		int adjNum = bean.getAdjnum();
		int purchaseId = bean.getPurchaseId();
		BookPurchasingDAOImpl bookPurchasingDAOImpl = new BookPurchasingDAOImpl();
		BookPurchasing bookPurchasing = bookPurchasingDAOImpl.getBookPurchasingById(conn, purchaseId);
		int leave = bookPurchasing.getLeave();
		bookPurchasing.setLeave(leave + adjNum);
		bookPurchasing.setUpdDate(new Date());
		bookPurchasing.setUpdUid((String)request.getSession().getAttribute("userId"));
		
		boolean stockOutFlag = bookStockOutDAOImpl.updateBookStockOut(conn, bean);
		
		boolean purchaseFlag = bookPurchasingDAOImpl.updateBookPurchasing(conn, bookPurchasing);
		
		boolean inventoryFlag = bookInventoryDAOImpl.updateBookInventoryByIsbn(conn, bookInventory);
		if(stockOutFlag && purchaseFlag){
			flag = true;
		}
		return flag;
	}

	private void forwardPage(Connection conn, HttpServletRequest request, HttpServletResponse response, 
			String curIntake, String isbn, String title, String prNum, String prDate, Double favourablePrice, Double unitPrice) throws Exception {
		BookStockInDAOImpl bookStockInDAOImpl = new BookStockInDAOImpl();
		BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
		BookStockOutOrderDAOImpl bookStockOutOrderDAOImpl = new BookStockOutOrderDAOImpl();
				
		BookStockIn bin = new BookStockIn();
		bin.setIntake(curIntake);
		bin.setIsbn(isbn);
		
		BookRelDAOImpl bookRelDAOImpl = new BookRelDAOImpl();
		BookRel rel = new BookRel();
		rel.setIsbn(isbn);
		rel.setIntake(curIntake);
		rel.setActInd("Y");
		List courseList = bookRelDAOImpl.getBookRelList(conn, rel);
		
		ExassLectDAOImpl exassLectDAOImpl = new ExassLectDAOImpl();
		ExassLect lect = new ExassLect();
		lect.setIntake(curIntake);
		List exassLectList = exassLectDAOImpl.getLectByCrs(conn, lect);
		
		List lastStockIn = bookStockInDAOImpl.getBookStockInList(conn, bin);
		
		BookStockOut b = new BookStockOut();
		b.setIntake(curIntake);
		b.setIsbn(isbn);
		b.setPRnum(prNum);
		BookStockOut bookStockOut = bookStockOutDAOImpl.getBookStockOut(conn, b);
		if(bookStockOut == null){	//沒有錄入過該單號的ISBN記錄
			List searchInfoList = (List)request.getSession().getAttribute("searchInfoList");
			if(searchInfoList == null){
				searchInfoList = bookStockOutOrderDAOImpl.getStockOutListByPrNum(conn, prNum);
			}
			BookStockOutOrder bookStockOutOrder = (BookStockOutOrder)request.getSession().getAttribute("bookStockOutOrder");
			BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
			if(bookStockOutOrder == null){
				bookStockOutOrder  = bookStockOutOrderDAOImpl.getStockOutOrderByPrNum(conn, prNum);
			}
			//判斷書商是屬於學院內部or學院外部
			BookSupplier bookSupplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, bookStockOutOrder.getSupplierNo());
			
			BookStockOutBean bStockOut = new BookStockOutBean();
			bStockOut.setIsbn(isbn);
			bStockOut.getBook().setTitle(title);
			bStockOut.setOutDate(prDate);
			bStockOut.setPrNum(prNum);
			int stock = 0;
			bStockOut.setAdjNum(stock);
			if("Y".equals(bookSupplier.getInner())){
				//顯示圖書的優惠價，折扣默認為1
				bStockOut.setPurchasePrice(favourablePrice);
				bStockOut.setDisCount(Double.valueOf(1));
				bStockOut.setCostPrice(favourablePrice * 1);
			} else {
				//顯示圖書的進貨單價，折扣為入貨時所輸入的折扣
				if(lastStockIn != null && !lastStockIn.isEmpty()){
					BookStockIn bi = (BookStockIn)lastStockIn.get(0);
					bStockOut.setPurchasePrice(unitPrice);
					bStockOut.setDisCount(bi.getDiscount());
					bStockOut.setCostPrice(unitPrice * bi.getDiscount());
				} else {
					bStockOut.setPurchasePrice(unitPrice);
					bStockOut.setDisCount(Double.valueOf(0));
					bStockOut.setCostPrice(Double.valueOf(0));
				}
			}
			
			boolean flag = true;
			List infoList = (List)request.getSession().getAttribute("infoList");
			if(infoList == null){
				infoList = new ArrayList();
			}
			List courses = (List)request.getSession().getAttribute("courses");
			if(courses == null){
				courses = new ArrayList(); 
			}
			List lecturers = (List)request.getSession().getAttribute("lecturers");
			if(lecturers == null){
				lecturers = new ArrayList(); 
			}
			
			if(flag){
				infoList.add(bStockOut);
				courses.add(courseList);
				lecturers.add(exassLectList);
			}
			request.getSession().setAttribute("courses", courses);
			request.getSession().setAttribute("lecturers", lecturers);
			request.getSession().setAttribute("infoList", infoList);
			request.setAttribute("isp", bookStockOutOrder.getIsp());
			request.getSession().setAttribute("bookStockOutOrder", bookStockOutOrder);
			request.getSession().setAttribute("searchInfoList", searchInfoList);
			request.getRequestDispatcher("bookStockOut.jsp").forward(request, response);
		} else {
			List infoList = (List)request.getSession().getAttribute("infoList");
			List searchInfoList = (List)request.getSession().getAttribute("searchInfoList");
			if(searchInfoList == null){
				searchInfoList = bookStockOutOrderDAOImpl.getStockOutListByPrNum(conn, prNum);
			}
			BookStockOutOrder bookStockOutOrder = (BookStockOutOrder)request.getSession().getAttribute("bookStockOutOrder");
			if(bookStockOutOrder == null){
				bookStockOutOrder  = bookStockOutOrderDAOImpl.getStockOutOrderByPrNum(conn, prNum);
			}
			BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
			//判斷書商是屬於學院內部or學院外部
			BookSupplier bookSupplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, bookStockOutOrder.getSupplierNo());
			
			BookStockOutBean bStockOut = new BookStockOutBean();
			bStockOut.setIsbn(isbn);
			bStockOut.getBook().setTitle(title);
			bStockOut.setOutDate(prDate);
			bStockOut.setPrNum(prNum);
			int stock = 0;
			bStockOut.setAdjNum(stock);
			if("Y".equals(bookSupplier.getInner())){
				//顯示圖書的優惠價，折扣默認為1
				bStockOut.setPurchasePrice(favourablePrice);
				bStockOut.setDisCount(Double.valueOf(1));
				bStockOut.setCostPrice(favourablePrice * 1);
			} else {
				//顯示圖書的進貨單價，折扣為入貨時所輸入的折扣
				if(lastStockIn != null && !lastStockIn.isEmpty()){
					BookStockIn bi = (BookStockIn)lastStockIn.get(0);
					bStockOut.setPurchasePrice(unitPrice);
					bStockOut.setDisCount(bi.getDiscount());
					bStockOut.setCostPrice(unitPrice * bi.getDiscount());
				} else {
					bStockOut.setPurchasePrice(unitPrice);
					bStockOut.setDisCount(Double.valueOf(0));
					bStockOut.setCostPrice(Double.valueOf(0));
				}
			}
			
			boolean flag = true;
			if(infoList == null){
				infoList = new ArrayList();
			}
			if(flag){
				infoList.add(bStockOut);
			}
			
			request.getSession().setAttribute("infoList", infoList);
			request.setAttribute("isp", bookStockOutOrder.getIsp());
			request.getSession().setAttribute("bookStockOutOrder", bookStockOutOrder);
			request.getSession().setAttribute("searchInfoList", searchInfoList);
			request.getRequestDispatcher("bookStockOut.jsp").forward(request, response);
		}
	}

	private boolean savePurchaseOut(Connection conn, HttpServletRequest request,String curIntake, String userId, String prDate,
			String prNum, String paidCurrency, int supplierNo, String idStr, String pStockOutStr, String pDateStr, List searchInfoList,
			String purchasePriceStr, String disCountStr, String costPriceStr, String crsCodeStr, String lectCodeStr) throws Exception {
		
		
		boolean flag = false;
		BookPurchasingDAOImpl bookPurchasingDAOImpl = new BookPurchasingDAOImpl();
		BookStockOutOrderDAOImpl bookStockOutOrderDAOImpl = new BookStockOutOrderDAOImpl();
		BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
		BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
		
		//Rainbow add
		BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
		BookSupplier supplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, supplierNo);
		
		String newPrNum = "DE"+supplier.getSupplierCode()+curIntake;
		
		List tempList = bookStockOutDAOImpl.getStockOutbySupplierCode(conn, newPrNum);
		if(tempList!=null && !tempList.isEmpty()){
			
			String temp = (String)tempList.get(0);
			int seq = Integer.parseInt(temp.substring(temp.length()-3, temp.length()));
			seq++;
			newPrNum += String.format("%03d",seq);
		}else{
			newPrNum += "001";
		}
		
		//保存出貨order信息，並設置isp為Y，標記為代購出貨
		BookStockOutOrder bookStockOutOrder = new BookStockOutOrder();
		bookStockOutOrder.setIntake(curIntake);
		bookStockOutOrder.setPrnum(newPrNum);
		bookStockOutOrder.setSupplierNo(supplierNo);
		bookStockOutOrder.setOutDate(prDate);
		bookStockOutOrder.setPaidCurrency(paidCurrency);
		bookStockOutOrder.setActind("Y");
		bookStockOutOrder.setCreDate(new Date());
		bookStockOutOrder.setCreUid(userId);
		bookStockOutOrder.setUpdDate(new Date());
		bookStockOutOrder.setUpdUid(userId);
		bookStockOutOrder.setIsp("Y");
		request.getSession().setAttribute("bookStockOutOrder", bookStockOutOrder);
		
		if(bookStockOutOrder.getSupplierNo() != null && !bookStockOutOrder.getSupplierNo().equals("")){			
			BookSupplier orderSupplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, bookStockOutOrder.getSupplierNo());
			request.getSession().setAttribute("orderSupplier", orderSupplier.getSupplierName());
		}
		
		//獲取頁面傳來的參數
		String[] ids = idStr.split(";");
		String[] stockOuts = pStockOutStr.split(";");
		String[] dates = pDateStr.split(";");
		String[] purchasePrices = purchasePriceStr.split(";"); 
		String[] disCounts = disCountStr.split(";"); 
		String[] costPrices = costPriceStr.split(";");
		String[] crsCodes = crsCodeStr.split(";");
		String[] lectCodes = lectCodeStr.split(";");		
		
		List inventoryList = new ArrayList();
		List stockOutList = new ArrayList();
		List<BookPurchasing> bookPurchaseList = new ArrayList();		
		
		for(int i=0; i<ids.length; i++){
			int id = Integer.parseInt(ids[i]);
			int stockOut = Integer.parseInt(stockOuts[i]);
			String date = dates[i];
			double purchasePrice = 0;
			double disCount = 0;
			double costPrice = Double.parseDouble(costPrices[i]);
			String crsCode = "";
			if(crsCodes.length >0 && crsCodes[i] != null)
				crsCode = crsCodes[i];
			String lectCode = "";
			if(lectCodes.length > 0 && lectCodes[i] != null)
				lectCode = lectCodes[i];
			BookStockOutBean info = (BookStockOutBean)searchInfoList.get(id);
			//庫存資料update
			BookInventory inventory = new BookInventory();
			inventory.setIsbn(ToolsOfString.getUniqueIsbn(info.getIsbn()));
			inventory.setStock(-stockOut);	//出貨時，庫存數量要減！！
			inventory.setUpddate(new Date());
			inventory.setUpduid(userId);
			//出貨細明資料，記錄每個代購出貨的ispDate的信息
			BookStockOut bookStockOut = new BookStockOut();
			bookStockOut.setIntake(curIntake);
			bookStockOut.setIsbn(info.getIsbn());
			
			bookStockOut.setPRnum(newPrNum);
			
			bookStockOut.setAdjnum(stockOut);
			bookStockOut.setIspDate(date);
			bookStockOut.setPurchasePrice(purchasePrice);
			bookStockOut.setDiscount(disCount);
			bookStockOut.setCostPrice(costPrice);
			bookStockOut.setRemarks(info.getRemarks());
			bookStockOut.setCourseCode(crsCode);
			bookStockOut.setLectCode(lectCode);
			bookStockOut.setCredate(new Date());
			bookStockOut.setCreuid(userId);
			bookStockOut.setUpddate(new Date());
			bookStockOut.setUpduid(userId);
			bookStockOut.setActind("Y");
			bookStockOut.setPurchaseId(info.getOutNo());
			//更新代購資料的leave信息
			BookPurchasing bookPurchasing = bookPurchasingDAOImpl.getBookPurchasingById(conn, info.getOutNo());
			bookPurchasing.setLeave(bookPurchasing.getLeave() - stockOut);
			bookPurchasing.setUpdDate(new Date());
			bookPurchasing.setUpdUid(userId);
			
			bookPurchaseList.add(bookPurchasing);
			inventoryList.add(inventory);
			stockOutList.add(bookStockOut);
		}
		if(!inventoryList.isEmpty() && !stockOutList.isEmpty() && !bookPurchaseList.isEmpty()){
			//update or insert datas into tbookstockoutorder table
			boolean stockOutOrder = bookStockOutOrderDAOImpl.addBookStockOutOrder(conn, bookStockOutOrder);
			
			boolean inventoryFlag = bookInventoryDAOImpl.updateBookInventory(conn, inventoryList);
			boolean stockOutFlag = bookStockOutDAOImpl.insertBookStockOutList(conn, stockOutList);
			boolean bookPurchaseFlag = true;
			for(BookPurchasing bean : bookPurchaseList){
				bookPurchaseFlag = bookPurchasingDAOImpl.updateBookPurchasing(conn, bean);
			}
			if(stockOutOrder && inventoryFlag && stockOutFlag && bookPurchaseFlag)
				flag = true;
			request.getSession().setAttribute("newPrNum", newPrNum);
		}
		return flag;
	}

	private boolean isNumber(String str){
		if(java.lang.Character.isDigit(str.charAt(0))){
			return true;
		}
		return false;	
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
