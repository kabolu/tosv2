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
import edu.must.tos.bean.BookInventory;
import edu.must.tos.bean.BookInventoryInfo;
import edu.must.tos.bean.BookStockIn;
import edu.must.tos.bean.BookStockInOrder;
import edu.must.tos.bean.BookStockOut;
import edu.must.tos.bean.BookStockOutOrder;
import edu.must.tos.bean.BookSupplier;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookInventoryDAOImpl;
import edu.must.tos.impl.BookStockInDAOImpl;
import edu.must.tos.impl.BookStockInOrderDAOImpl;
import edu.must.tos.impl.BookStockOutDAOImpl;
import edu.must.tos.impl.BookStockOutOrderDAOImpl;
import edu.must.tos.impl.BookSupplierDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;

public class BookInventoryServlet extends HttpServlet {

	public BookInventoryServlet() {
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
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		HttpSession session = request.getSession();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
			BookStockInOrderDAOImpl bookStockInOrderDAOImpl = new BookStockInOrderDAOImpl();
			BookStockOutOrderDAOImpl bookStockOutOrderDAOImpl = new BookStockOutOrderDAOImpl();
			BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
			
			String userId = (String)session.getAttribute("userId");
			
			String key = "CURRINTAKE";
			String curIntake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			BookSupplier inSupplier = new BookSupplier();
			inSupplier.setIo("I");
			List supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, inSupplier);
			
			String type = null;
			if(request.getParameter("type") != null && !request.getParameter("type").equals("")){
				type = request.getParameter("type");
			}
			
			if(type != null && type.equals("cleanSession")){
				session.removeAttribute("bookStockInOrder");
				session.removeAttribute("searchInfoList");
				session.removeAttribute("infoList");
				request.setAttribute("supplierList", supplierList);
				request.getRequestDispatcher("bookInventory.jsp").forward(request, response);
			}
			else if(type != null && type.equals("saveSession")){
				saveSession(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("updateStockIn")){
				updateStockIn(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("remove")){
				String i =request.getParameter("i");
				int id = Integer.parseInt(i);
				List infoList = (List)session.getAttribute("infoList");
				infoList.remove(id);
				session.setAttribute("infoList", infoList);
				
				request.setAttribute("supplierList", supplierList);
				request.getRequestDispatcher("bookInventory.jsp").forward(request, response);
			}
			else if(type != null && type.equals("sessionRemarks")){
				saveRemarksInSession(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("sessionAdjnum")){
				saveAdjNumInSession(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("showReceipt")){ //rainbow add
				String prNum = null;
				if(request.getParameter("prNum") != null && !request.getParameter("prNum").equals("")){
					prNum = request.getParameter("prNum");

					List bookStockOutList = bookStockOutDAOImpl.getStockOutInfoList(conn, null, prNum, null, null, null, 0);
					BookStockOutOrder bookStockOutOrder = bookStockOutOrderDAOImpl.getStockOutOrderByPrNum(conn, prNum);
				
				
					if(bookStockOutOrder.getSupplierNo() != null && !bookStockOutOrder.getSupplierNo().equals("")){
						BookSupplier orderSupplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, bookStockOutOrder.getSupplierNo());
						request.getSession().setAttribute("orderSupplier", orderSupplier.getSupplierName());
					}
					request.getSession().setAttribute("bookStockOutList", curIntake);
					request.getSession().setAttribute("bookStockOutList", bookStockOutList);
					request.getSession().setAttribute("bookStockOutOrder", bookStockOutOrder);

					out.print(0);
				}else{
					out.print(1);
				}				
			}
			else if(type != null && type.equals("searchPrNumList")){
				searchPrNumList(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("modifyPrNum")){
				modifyPrNum(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("saveRecords")){
				saveRecords(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("searchPrNum")){
				String prNum = request.getParameter("prNum");
				BookStockInOrder bookStockInOrder = bookStockInOrderDAOImpl.getStockInOrderByPrNum(conn, prNum);
				List list = bookStockInOrderDAOImpl.getStockInListByPrNum(conn, prNum);
				
				session.setAttribute("search", "search");
				request.setAttribute("searchPrNum", prNum);
				request.setAttribute("supplierList", supplierList);
				session.setAttribute("searchInfoList", list);
				session.setAttribute("bookStockInOrder", bookStockInOrder);
				request.getRequestDispatcher("bookInventory.jsp").forward(request, response);
			}
			else if(type != null && type.equals("stockDetailPage")){
				request.getRequestDispatcher("bookStockDetail.jsp").forward(request, response);
			}
			else if(type != null && type.equals("stockDetail")){
				showStockDetail(conn, curIntake, userId, request, response);
			}
			else{
				request.setAttribute("supplierList", supplierList);
				doStockIn(conn, curIntake, userId, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage()+" method occurs an exception!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	private void searchPrNumList(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BookStockInOrderDAOImpl bookStockInOrderDAOImpl = new BookStockInOrderDAOImpl();
		BookStockOutOrderDAOImpl bookStockOutOrderDAOImpl = new BookStockOutOrderDAOImpl();
		
		String prNum = null;
		if(request.getParameter("searchPrNum") != null && !request.getParameter("searchPrNum").equals("")){
			prNum = request.getParameter("searchPrNum");
		}
		String paidStatus = null;
		if(request.getParameter("paidStatus") != null && !request.getParameter("paidStatus").equals("")){
			paidStatus = request.getParameter("paidStatus");
		}
		String noIntake = null;
		if(request.getParameter("noIntake") != null && !request.getParameter("noIntake").equals("")){
			noIntake = request.getParameter("noIntake");
		}
		if(noIntake!=null && "Y".equals(noIntake)){
			curIntake = null;
		}
		String inOrOut = "I";
		if(request.getParameter("inOrOut") != null && !request.getParameter("inOrOut").equals("")){
			inOrOut = request.getParameter("inOrOut");
		}
		List list = null;
		if(inOrOut != null && inOrOut.equals("I")){
			list = bookStockInOrderDAOImpl.getStockInOrderListByCond(conn, prNum, curIntake, paidStatus);
		}else if(inOrOut != null && inOrOut.equals("O")){
			list = bookStockOutOrderDAOImpl.getStockOutOrderPrNum(conn, prNum, curIntake);
		}else if(inOrOut != null && inOrOut.equals("P")){	//代購出貨記錄
			list = bookStockOutOrderDAOImpl.getStockOutDetailByPrNum(conn, curIntake, prNum, "Y");
		}				
		request.setAttribute("paidStatus", paidStatus);
		
		request.setAttribute("list", list);
		request.setAttribute("inOrOut", inOrOut);
		request.setAttribute("prNum", prNum);
		
		request.getRequestDispatcher("prNum.jsp").forward(request, response);
	}

	private void doStockIn(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		BookStockInOrderDAOImpl bookStockInOrderDAOImpl = new BookStockInOrderDAOImpl();
		BookStockInDAOImpl bookStockInDAOImpl = new BookStockInDAOImpl();
		
		String isbn = "";
		if(request.getParameter("isbn") != null && !request.getParameter("isbn").trim().equals("")){
			isbn = request.getParameter("isbn");
		}
		String prDate = "";
		if(session.getAttribute("prDate") != null && !session.getAttribute("prDate").equals("")){
			prDate = (String)session.getAttribute("prDate");
		}
		String prNum = "";
		if(session.getAttribute("prNum") != null && !session.getAttribute("prNum").equals("")){
			prNum = (String)session.getAttribute("prNum");
		}else{
			prNum = request.getParameter("prNumParam");
		}		
		Book book = bookDAOImpl.getBookByPK(conn, isbn);
		int prsum = 0;
		
		if(book.getIsbn() == null){
			List searchInfoList = (List)session.getAttribute("searchInfoList");
			if(searchInfoList == null){
				searchInfoList = bookStockInOrderDAOImpl.getStockInListByPrNum(conn, prNum);
			}
			BookStockInOrder bookStockInOrder = (BookStockInOrder)session.getAttribute("bookStockInOrder");
			if(bookStockInOrder == null){
				bookStockInOrder  = bookStockInOrderDAOImpl.getStockInOrderByPrNum(conn, prNum);
			}
			session.setAttribute("bookStockInOrder", bookStockInOrder);
			session.setAttribute("searchInfoList", searchInfoList);
			
			request.setAttribute("wrong", "沒有該圖書資料!");
			request.getRequestDispatcher("bookInventory.jsp").forward(request, response);
		}else{
			BookStockIn b = new BookStockIn();
			b.setIntake(curIntake);
			b.setIsbn(isbn);
			
			List lastStockIn = bookStockInDAOImpl.getBookStockInList(conn, b);
			
			b.setPRnum(prNum);
			BookStockIn bookStockIn = bookStockInDAOImpl.getBookStockIn(conn, b);
			if(bookStockIn == null){
				List searchInfoList = (List)session.getAttribute("searchInfoList");
				if(searchInfoList == null){
					searchInfoList = bookStockInOrderDAOImpl.getStockInListByPrNum(conn, prNum);
				}
				BookStockInOrder bookStockInOrder = (BookStockInOrder)session.getAttribute("bookStockInOrder");
				if(bookStockInOrder == null){
					bookStockInOrder  = bookStockInOrderDAOImpl.getStockInOrderByPrNum(conn, prNum);
				}
				
				BookInventoryInfo bookInventoryBean = new BookInventoryInfo();
				bookInventoryBean.setIsbn(isbn);
				bookInventoryBean.getBook().setTitle(book.getTitle());
				bookInventoryBean.setPRdate(prDate);
				bookInventoryBean.setPRnum(prNum);
				int stock = 0;
				bookInventoryBean.setStock(stock);
				bookInventoryBean.setPRsum(prsum);
				if(lastStockIn != null && !lastStockIn.isEmpty()){
					BookStockIn bi = (BookStockIn)lastStockIn.get(0);
					bookInventoryBean.setPurchasePrice(bi.getPurchasePrice());
					bookInventoryBean.setDiscount(bi.getDiscount());
					bookInventoryBean.setCostPrice(bi.getCostPrice());
				} else {
					bookInventoryBean.setPurchasePrice(book.getUnitPrice());
					bookInventoryBean.setDiscount(book.getDisCount());
					bookInventoryBean.setCostPrice(book.getUnitPrice() * book.getDisCount());
				}
				
				boolean flag = true;
				List infoList = (List)session.getAttribute("infoList");
				if(infoList == null){
					infoList = new ArrayList();
				}
				if(flag){
					infoList.add(bookInventoryBean);
				}
				session.setAttribute("infoList", infoList);				
				session.setAttribute("bookStockInOrder", bookStockInOrder);
				session.setAttribute("searchInfoList", searchInfoList);
				request.getRequestDispatcher("bookInventory.jsp").forward(request, response);
			}else{
				List infoList = (List)session.getAttribute("infoList");
				List searchInfoList = (List)session.getAttribute("searchInfoList");
				if(searchInfoList == null){
					searchInfoList = bookStockInOrderDAOImpl.getStockInListByPrNum(conn, prNum);
				}
				BookStockInOrder bookStockInOrder = (BookStockInOrder)session.getAttribute("bookStockInOrder");
				if(bookStockInOrder == null){
					bookStockInOrder  = bookStockInOrderDAOImpl.getStockInOrderByPrNum(conn, prNum);
				}
				
				BookInventoryInfo bookInventoryBean = new BookInventoryInfo();
				bookInventoryBean.setIsbn(isbn);
				bookInventoryBean.getBook().setTitle(book.getTitle());
				bookInventoryBean.setPRdate(prDate);
				bookInventoryBean.setPRnum(prNum);
				int stock = 0;
				bookInventoryBean.setStock(stock);
				bookInventoryBean.setPRsum(prsum);
				if(lastStockIn != null && !lastStockIn.isEmpty()){
					BookStockIn bi = (BookStockIn)lastStockIn.get(0);
					bookInventoryBean.setPurchasePrice(bi.getPurchasePrice());
					bookInventoryBean.setDiscount(bi.getDiscount());
					bookInventoryBean.setCostPrice(bi.getCostPrice());
				}
				
				boolean flag = true;
				if(infoList == null){
					infoList = new ArrayList();
				}
				if(flag){
					infoList.add(bookInventoryBean);
				}
				session.setAttribute("infoList", infoList);
				session.setAttribute("bookStockInOrder", bookStockInOrder);
				session.setAttribute("searchInfoList", searchInfoList);
				request.getRequestDispatcher("bookInventory.jsp").forward(request, response);
			}
		}
	}

	private void showStockDetail(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		BookStockInDAOImpl bookStockInDAOImpl = new BookStockInDAOImpl();
		BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
		
		String isbn = null;
		if(request.getParameter("isbn") != null){
			isbn = request.getParameter("isbn");
		}
		if(isbn != null && !isbn.equals("")){
			Book book = bookDAOImpl.getBookByPK(conn, isbn);
			if(book.getIsbn() != null){
				request.setAttribute("bookFlag", "true");
				request.setAttribute("book", book);
				
				BookStockIn bookStockIn = new BookStockIn();
				bookStockIn.setIsbn(book.getIsbn());
				bookStockIn.setIntake(curIntake);
				List stockInList = bookStockInDAOImpl.getBookStockInByIsbn(conn, curIntake, isbn);
				request.setAttribute("stockInList", stockInList);
				
				BookInventory bookInventory = bookInventoryDAOImpl.getBookInventoryByPK(conn, book.getIsbn());
				request.setAttribute("bookInventory", bookInventory);
				
				List orDetailList = orderDetailDAOImpl.getOrDetail(conn, curIntake, book.getIsbn(), "Y");
				int receiveQty = 0;
				int retailQty = 0;
				for(int i=0; i<orDetailList.size(); i++){
					OrDetail od = (OrDetail)orDetailList.get(i);
					if(od.getStudentNo().equals("RETAIL")){
						retailQty += od.getConfirmQty();
					}else{
						if(od.getNotEnoughQty() == 0){
							receiveQty += od.getConfirmQty();
						}else if(od.getNotEnoughQty() > 0){
							receiveQty += (od.getConfirmQty()-od.getNotEnoughQty());
						}
					}
				}
				request.setAttribute("receiveQty", receiveQty);
				request.setAttribute("retailQty", retailQty);
				
				BookStockOut bookStockOut = new BookStockOut();
				bookStockOut.setIntake(curIntake);
				bookStockOut.setIsbn(book.getIsbn());
				List stockOutList = bookStockOutDAOImpl.getStockOutList(conn, bookStockOut);
				int stockOutQty = 0;
				for(int i=0; i<stockOutList.size(); i++){
					BookStockOut stockOut = (BookStockOut)stockOutList.get(i);
					stockOutQty += stockOut.getAdjnum();
				}
				request.setAttribute("stockOutQty", stockOutQty);
			}else{
				request.setAttribute("bookFlag", "false");
			}
		}
		request.getRequestDispatcher("bookStockDetail.jsp").forward(request, response);
	}

	private void saveRecords(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		BookStockInOrderDAOImpl bookStockInOrderDAOImpl = new BookStockInOrderDAOImpl();
		BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
		BookStockInDAOImpl bookStockInDAOImpl = new BookStockInDAOImpl();
		String prDate = request.getParameter("prDate");
		String prNum = request.getParameter("prNum");
		BookStockInOrder stockInIntake = bookStockInOrderDAOImpl.getStockInOrderByPrNum(conn, prNum);
		if(stockInIntake != null){
			curIntake = stockInIntake.getIntake();
		}
		int supplierNo = 0;
		if(request.getParameter("supplierNo") != null && !request.getParameter("supplierNo").equals("")){
			supplierNo = Integer.parseInt(request.getParameter("supplierNo"));
		}
		String paidStatus = request.getParameter("paidStatus");
		String paidDate = request.getParameter("paidDate");
		String paidCurrency = request.getParameter("paidCurrency");
		String invoiceDate = request.getParameter("invoiceDate");
		int searchSize = 0;
		if(request.getParameter("searchSize") != null){
			searchSize = Integer.parseInt(request.getParameter("searchSize"));
		}
		
		BookStockInOrder bookStockInOrder = new BookStockInOrder();
		bookStockInOrder.setIntake(curIntake);
		bookStockInOrder.setPrnum(prNum);
		bookStockInOrder.setSupplierNo(supplierNo);
		bookStockInOrder.setPaidstatus(paidStatus);
		bookStockInOrder.setPaidDate(paidDate);
		bookStockInOrder.setPaidcurrency(paidCurrency);
		bookStockInOrder.setInDate(prDate);
		bookStockInOrder.setInvoiceDate(invoiceDate);
		bookStockInOrder.setActind("Y");
		bookStockInOrder.setCreDate(new Date());
		bookStockInOrder.setCreUid(userId);
		
		List<BookInventoryInfo> infoList = (List)request.getSession().getAttribute("infoList");
		List<BookInventory> inventoryList = new ArrayList<BookInventory>();
		List<BookStockIn> stockInList = new ArrayList<BookStockIn>();
		if(infoList != null && !infoList.isEmpty()){
			for(BookInventoryInfo info : infoList){
				BookInventory inventory = new BookInventory();
				if(info.getIsbn().toUpperCase().endsWith("A"))
					inventory.setIsbn(info.getIsbn().substring(0, info.getIsbn().length() - 1));
				else
					inventory.setIsbn(info.getIsbn());
				inventory.setStock(info.getAdjnum());
				inventory.setUpddate(new Date());
				inventory.setUpduid(userId);
				
				BookStockIn stockIn = new BookStockIn();
				stockIn.setIntake(curIntake);
				stockIn.setIsbn(info.getIsbn());
				stockIn.setPRnum(info.getPRnum());
				stockIn.setAdjnum(info.getAdjnum());
				stockIn.setPurchasePrice(info.getPurchasePrice());
				stockIn.setDiscount(info.getDiscount());
				stockIn.setCostPrice(info.getCostPrice());
				stockIn.setRemarks(info.getRemarks());
				stockIn.setCredate(new Date());
				stockIn.setCreuid(userId);
				stockIn.setActind("Y");
				inventoryList.add(inventory);
				stockInList.add(stockIn);
			}
			if(!inventoryList.isEmpty() && !stockInList.isEmpty()){
				boolean stockInOrder = bookStockInOrderDAOImpl.addStockInOrder(conn, bookStockInOrder);
				boolean inventoryFlag = bookInventoryDAOImpl.updateBookInventory(conn, inventoryList);
				boolean stockInFlag = bookStockInDAOImpl.updateBookStockInList(conn, stockInList);
				if(stockInOrder && inventoryFlag && stockInFlag){
					request.getSession().removeAttribute("searchInfoList");
					conn.commit();
					out.print(1);
				}else{
					conn.rollback();
					out.print(0);
				}
			}
		}else{
			if(searchSize != 0){
				boolean stockInOrder = bookStockInOrderDAOImpl.addStockInOrder(conn, bookStockInOrder);
				if(stockInOrder){
					request.getSession().removeAttribute("searchInfoList");
					conn.commit();
					out.print(1);
				}else{
					conn.rollback();
					out.print(0);
				}
			}
		}
	}

	private void modifyPrNum(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		BookStockInOrderDAOImpl bookStockInOrderDAOImpl = new BookStockInOrderDAOImpl();
		BookStockInDAOImpl bookStockInDAOImpl = new BookStockInDAOImpl();
		BookStockOutOrderDAOImpl bookStockOutOrderDAOImpl = new BookStockOutOrderDAOImpl();
		BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
		
		String prNum = null;
		if(request.getParameter("prNum") != null && !request.getParameter("prNum").equals("")){
			prNum = request.getParameter("prNum");
		}
		String oldPrNum = null;
		if(request.getParameter("oldPrNum") != null && !request.getParameter("oldPrNum").equals("")){
			oldPrNum = request.getParameter("oldPrNum");
		}
		String stockNo = null;
		if(request.getParameter("stockNo") != null && !request.getParameter("stockNo").equals("")){
			stockNo = request.getParameter("stockNo");
		}
		String inOrOut = null;
		if(request.getParameter("inOrOut") != null && !request.getParameter("inOrOut").equals("")){
			inOrOut = request.getParameter("inOrOut");
		}
		if(prNum != null && !oldPrNum.equals(prNum)){
			if(inOrOut != null && "I".equals(inOrOut)){
				BookStockInOrder bookStockInOrder = bookStockInOrderDAOImpl.getStockInOrderByPrNum(conn, prNum);
				if(bookStockInOrder != null){
					out.print("1");
				}else{
					BookStockInOrder order = new BookStockInOrder();
					order.setStockInNo(Integer.parseInt(stockNo));
					order.setPrnum(prNum);
					order.setUpdDate(new Date());
					order.setUpdUid(userId);
					boolean flag1 = bookStockInOrderDAOImpl.updateStockInOrder(conn, order);
					
					boolean flag2 = bookStockInDAOImpl.updateStockInPrNum(conn, prNum, oldPrNum);
					if(flag1 && flag2){
						out.print("0");
					}
				}
			}else if(inOrOut != null && "O".equals(inOrOut)){
				BookStockOutOrder bookStockOutOrder = bookStockOutOrderDAOImpl.getStockOutOrderByPrNum(conn, prNum);
				if(bookStockOutOrder != null){
					out.print("1");
				}else{
					BookStockOutOrder order = new BookStockOutOrder();
					order.setStockOutNo(Integer.parseInt(stockNo));
					order.setPrnum(prNum);
					order.setUpdDate(new Date());
					order.setUpdUid(userId);
					boolean flag1 = bookStockOutOrderDAOImpl.updateStockOutOrder(conn, order);
					
					boolean flag2 = bookStockOutDAOImpl.updateStockOutPrNum(conn, prNum, oldPrNum);
					if(flag1 && flag2){
						out.print("0");
					}
				}
			}
		}else if(prNum != null && prNum.equals(oldPrNum)){
			out.print("2");
		}
	}

	private void saveAdjNumInSession(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) {
		int adjnum = 0;
		if(request.getParameter("adjnum") != null && !request.getParameter("adjnum").equals("")){
			if(request.getParameter("adjnum").subSequence(0, 1).equals("-")){
				if(request.getParameter("adjnum").length() > 1){
					adjnum = Integer.parseInt(request.getParameter("adjnum").substring(1, request.getParameter("adjnum").length()));
					adjnum = -adjnum;
				}
			}else{
				adjnum = Integer.parseInt(request.getParameter("adjnum"));
			}
		}
		int i = Integer.parseInt(request.getParameter("i"));
		
		List infoList = (List)request.getSession().getAttribute("infoList");
		BookInventoryInfo info = (BookInventoryInfo)infoList.get(i);
		info.setAdjnum(adjnum);
		request.getSession().setAttribute("infoList", infoList);
	}

	private void saveRemarksInSession(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) {
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
		int i = Integer.parseInt(request.getParameter("i"));
		List infoList = (List)request.getSession().getAttribute("infoList");
		BookInventoryInfo info = (BookInventoryInfo)infoList.get(i);
		info.setRemarks(remarks);
		info.setPurchasePrice(purchasePrice);
		info.setDiscount(discount);
		info.setCostPrice(costPrice);
		
		request.getSession().setAttribute("infoList", infoList);
	}

	private void updateStockIn(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
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
		BookStockInDAOImpl bookStockInDAOImpl = new BookStockInDAOImpl();
		BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
		if(adjnum != null && !adjnum.equals("")){
			if(Integer.parseInt(adjnum) == 0){	//---------delete record
				BookStockIn bi = new BookStockIn();
				bi.setNo(no);
				bi.setPRnum(prNum);
				bi.setIsbn(isbn);
				bi.setPurchasePrice(sPurchasePrice);
				bi.setDiscount(sDiscount);
				bi.setCostPrice(sCostPrice);
				bi.setAdjnum(0);
				bi.setRemarks(remarks);
				bi.setActind("N");
				boolean flag = bookStockInDAOImpl.updateBookStockIn(conn, bi);
				
				BookInventory inventry = new BookInventory();
				inventry.setIsbn(isbn);
				inventry.setStock(-oldAdjnum);
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
				BookInventory bookInventory = bookInventoryDAOImpl.getBookInventoryByPK(conn, isbn);
				int differ = Integer.parseInt(adjnum) - oldAdjnum;
				int rightNumber = bookInventory.getStock() + differ;
				if(rightNumber < 0){
					out.print("negative");
				}else {
					BookStockIn bi = new BookStockIn();
					bi.setNo(no);
					bi.setPRnum(prNum);
					bi.setIsbn(isbn);
					bi.setPurchasePrice(sPurchasePrice);
					bi.setDiscount(sDiscount);
					bi.setCostPrice(sCostPrice);
					bi.setAdjnum(Integer.parseInt(adjnum));
					bi.setRemarks(remarks);
					bi.setActind("Y");
					boolean flag = bookStockInDAOImpl.updateBookStockIn(conn, bi);
					
					BookInventory inventry = new BookInventory();
					inventry.setIsbn(isbn);
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
			BookStockIn bi = new BookStockIn();
			bi.setNo(no);
			bi.setPRnum(prNum);
			bi.setIsbn(isbn);
			bi.setPurchasePrice(sPurchasePrice);
			bi.setDiscount(sDiscount);
			bi.setCostPrice(sCostPrice);
			bi.setAdjnum(oldAdjnum);
			bi.setActind("Y");
			bi.setRemarks(remarks);
			boolean flag = bookStockInDAOImpl.updateBookStockIn(conn, bi);
			if(flag){
				conn.commit();
				out.print("true");
				List searchInfoList = (List)request.getSession().getAttribute("searchInfoList");
				BookInventoryInfo searchInfo = new BookInventoryInfo();
				searchInfo = (BookInventoryInfo)searchInfoList.get(i);
				searchInfo.setPurchasePrice(sPurchasePrice);
				searchInfo.setDiscount(sDiscount);
				searchInfo.setCostPrice(sCostPrice);
				request.getSession().setAttribute("searchInfoList", searchInfoList);
			}else{
				conn.rollback();
				out.print("false");
			}
		}
	}

	private void saveSession(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) {
		String prDate = "";
		if(request.getParameter("prDate") != null && !request.getParameter("prDate").trim().equals("")){
			prDate = request.getParameter("prDate");
		}
		String prNum = "";
		if(request.getParameter("prNum") != null && !request.getParameter("prNum").trim().equals("")){
			prNum = request.getParameter("prNum");
		}
		String paidCurrency = "";
		if(request.getParameter("paidCurrency") != null && !request.getParameter("paidCurrency").equals("")){
			paidCurrency = request.getParameter("paidCurrency");
		}
		String paidDate = "";
		if(request.getParameter("paidDate") != null ){
			paidDate = request.getParameter("paidDate");
		}
		String paidStatus = ""; 
		if(request.getParameter("paidStatus") != null && !request.getParameter("paidStatus").equals("")){
			paidStatus = request.getParameter("paidStatus");
		}
		String supplierNo = "0";
		if(request.getParameter("supplierNo") != null && !request.getParameter("supplierNo").equals("")){
			supplierNo = request.getParameter("supplierNo");
		}
		String invoiceDate = "";
		if(request.getParameter("invoiceDate") != null && !request.getParameter("invoiceDate").equals("")){
			invoiceDate = request.getParameter("invoiceDate");
		}
		BookStockInOrder bookStockInOrder = new BookStockInOrder();
		bookStockInOrder.setInDate(prDate);
		bookStockInOrder.setPrnum(prNum);
		bookStockInOrder.setIntake(curIntake);
		bookStockInOrder.setSupplierNo(Integer.parseInt(supplierNo));
		bookStockInOrder.setPaidstatus(paidStatus);
		bookStockInOrder.setPaidcurrency(paidCurrency);
		bookStockInOrder.setPaidDate(paidDate);
		bookStockInOrder.setInvoiceDate(invoiceDate);
		bookStockInOrder.setActind("Y");
		request.getSession().setAttribute("bookStockInOrder", bookStockInOrder);
	}

	private int calulatePRSum(List<BookStockIn> bookStockInlist){
		int sum = 0;
		for(BookStockIn bookStockIn : bookStockInlist){
			sum = sum + bookStockIn.getAdjnum();
		}
		return sum;
	}
	
	private boolean isNumber(String str){
		if(java.lang.Character.isDigit(str.charAt(0))){
			return true;
		}
		return false;	
	}
	
	public void init() throws ServletException {
		// Put your code here
	}

}
