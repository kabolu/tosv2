package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
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

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import edu.must.tos.bean.BookInventory;
import edu.must.tos.bean.Faculty;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.OrderInfo;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.bean.Transaction;
import edu.must.tos.impl.BookInventoryDAOImpl;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StudentBookDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.TransactionDAOImpl;
import edu.must.tos.util.ToolsOfString;

public class RetailBookServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public RetailBookServlet() {
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
		HttpSession session = request.getSession();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String curIntake = (String)session.getAttribute("curIntake");
			String userId = (String)session.getAttribute("userId");
			
			OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
			OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
			StudentBookDAOImpl studentBookDAOImpl = new StudentBookDAOImpl();
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			
			//獲取當前學期的匯率參數
			double rmbRate = 1;
			double hkdRate = 1;
			SysConfig rateConfig = new SysConfig();
			rateConfig.setScType(curIntake);
			rateConfig.setActInd("Y");
			List<SysConfig> rateList = sysConfigDAOImpl.getSysConfigList(conn, rateConfig);
			if(rateList != null && !rateList.isEmpty()){
				for(SysConfig config : rateList){
					if("HKD".equals(config.getScKey())){
						hkdRate = Double.parseDouble(config.getScValue1());
					} else if ("RMB".equals(config.getScKey())){
						rmbRate = Double.parseDouble(config.getScValue1());
					}
				}
			}
			request.setAttribute("hkdRate", hkdRate);
			request.setAttribute("rmbRate", rmbRate);
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("searchOrderSeqNo")){
				int orderSeqNo = 0;
				if(request.getParameter("orderSeqNo") != null && !request.getParameter("orderSeqNo").equals("")){
					orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
				}
				String fromDate = null;
				if(request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")){
					fromDate = request.getParameter("fromDate");
				}
				String toDate = null;
				if(request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")){
					toDate = request.getParameter("toDate");
				}
				List list = orderDAOImpl.getRetailOrderList(conn, orderSeqNo, fromDate, toDate);
				session.setAttribute("list", list);
				request.getRequestDispatcher("retailBookIndex.jsp").forward(request, response);
			}else if(type != null && type.equals("retailBookOrder")){
				request.setAttribute("retail", "RETAIL");
				String remarks = null;
				if(request.getParameter("remarks") != null){
					remarks = request.getParameter("remarks");
				}
				String confirmQty[] = request.getParameterValues("confirmQty");
				List<OrderInfo> selectedBookList = (List)session.getAttribute("selectedBookList");
				for(int i=0; i<selectedBookList.size(); i++) {
					OrderInfo info = (OrderInfo)selectedBookList.get(i);
					info.setStudentNo("RETAIL");
					info.setConfirmQty(Integer.parseInt(confirmQty[i]));
				}
				OrderInfo info = (OrderInfo)selectedBookList.get(0);
				Order o = new Order();
				o.setStudentNo(info.getStudentNo());
				o.setOrderIntake(curIntake);
				o.setOrderSeqNo(0);
				o.setPaidCurrency("");
				o.setPaidAmount(0);
				o.setPaidStatus("N");
				o.setActInd("Y");
				o.setCreUid(userId);
				o.setCreDate(new Date());
				o.setUpdUid(userId);
				o.setUpdDate(new Date());
				o.setAmerceMount(0);
				o.setFineforlatepay(0);
				o.setNetpaidamount(new Double(0));
				o.setNetpaidcurrency("");
				o.setCurrate(new Double(1));
				o.setRemarks(remarks);
				int orderSeqNo = orderDAOImpl.addOrderSeqNoInfo(conn, o);
				List detailList = new ArrayList();
				for(int i=0; i<selectedBookList.size(); i++) {
					OrderInfo in = (OrderInfo)selectedBookList.get(i);
					
					OrDetail detail = new OrDetail();
					detail.setStudentNo(in.getStudentNo());
					detail.setOrderIntake(curIntake);
					detail.setOrderSeqNo(orderSeqNo);
					detail.setIsbn(in.getIsbn());
					detail.setConfirmQty(Integer.parseInt(confirmQty[i]));
					detail.setActInd("Y");
					detail.setCreUid(userId);
					detail.setCreDate(new Date());
					detail.setUpdUid(userId);
					detail.setUpdDate(new Date());
					detail.setCourseCode(in.getCourseCode());
					detailList.add(detail);
				}
				boolean detailFlag = orderDetailDAOImpl.addDetailSeqNoInfo(conn, detailList);
				if(orderSeqNo != 0 && detailFlag){
					conn.commit();
					
					List orderedBookList = studentBookDAOImpl.searchOrderedBookByRetailNo(conn, orderSeqNo, curIntake);
					session.setAttribute("orderedBookList", orderedBookList);
					session.removeAttribute("selectedBookList");
					request.setAttribute("orderSeqNo", orderSeqNo);
					request.getRequestDispatcher("retailBookPage.jsp").forward(request, response);
				}else{
					conn.rollback();
					request.getRequestDispatcher("orderBookerror.jsp").forward(request, response);
				}
			}else if(type != null && type.equals("retailBookSearch")){
				session.removeAttribute("facultyCode");
				session.removeAttribute("year");
				session.removeAttribute("facList");
				session.removeAttribute("availableBooklist");
				session.removeAttribute("stuDetList");
				
				String retail = null;
				if(request.getParameter("retail") != null && !request.getParameter("retail").equals("")){
					retail = request.getParameter("retail");
				}
				List facList = new ArrayList();
				Faculty faculty = new Faculty();
				faculty.setFacultyCode("other");
				faculty.setChineseName("其他");
				facList.add(faculty);
				
				session.setAttribute("facList", facList);
				session.setAttribute("retail", retail);
				request.getRequestDispatcher("orderbookindex2.jsp").forward(request, response);
			}else if(type != null && type.equals("payOrderPage")){
				int orderSeqNo = 0;
				if(request.getParameter("orderSeqNo") != null){
					orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
				}
	        	
				String back = null;
				if(request.getParameter("back") != null){
					back = request.getParameter("back");
				}
				
				String value = ",";
				String paidStatus = "Y";
				
				Order order = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNo);
				if(order != null && order.getPaidStatus().equals("N")){
					paidStatus = "N";
				}
				
				List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, null, paidStatus, orderSeqNo);
				session.setAttribute("orderedBookList", orderedBookList);
				request.setAttribute("order", order);
				request.setAttribute("retail", "RETAIL");
				request.setAttribute("oprStep", "PAY");
				request.setAttribute("orderSeqNo", orderSeqNo);
				request.setAttribute("back", back);
				request.getRequestDispatcher("retailBookList.jsp").forward(request, response);
			}else if(type != null && type.equals("receiveBookPage")){
				int orderSeqNo = 0;
				if(request.getParameter("orderSeqNo") != null){
					orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
				}
				
				String back = null;
				if(request.getParameter("back") != null){
					back = request.getParameter("back");
				}
	        	
				String value = ",";
				String paidStatus = "Y";
				Order o = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNo);
				if(o != null && o.getPaidStatus().equals("N")){
					paidStatus = "N";
				}else{
					String payResult = "繳費成功!";
					request.setAttribute("payResult", payResult);
					request.setAttribute("order", o);
				}
				
				if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("receive")){
					paidStatus = "Y";
					request.setAttribute("payResult", "Y");
				}
				List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, null, paidStatus, orderSeqNo);
				session.setAttribute("orderedBookList", orderedBookList);
				request.setAttribute("retail", "RETAIL");
				request.setAttribute("oprStep", "RECEIVE");
				request.setAttribute("back", back);
				request.setAttribute("orderSeqNo", orderSeqNo);
				request.getRequestDispatcher("retailBookList.jsp").forward(request, response);
			}else if(type != null && type.equals("payOrder")){
				int orderSeqNo = 0;
				if(request.getParameter("orderSeqNo") != null){
					orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
				}
				String paidCurrency = request.getParameter("paidCurrency");
				String retail = null;
				if(request.getParameter("retail") != null){
					retail = request.getParameter("retail");
				}
				double shippingFee = 0;
				float paidAmount = 0;
				float amercePrice = 0;
				
				String netPaidCurrency = null;
				float netPaidAmount = 0;
				
				if(paidCurrency != null && paidCurrency.equals("MOP")) {
					paidAmount = Float.parseFloat(request.getParameter("totalMop"));
					netPaidAmount = paidAmount;
					netPaidCurrency = paidCurrency; 
				} else if (paidCurrency != null && paidCurrency.equals("RMB")) {
					paidAmount = Float.parseFloat(request.getParameter("totalRmb"));
					netPaidCurrency = "MOP";
					netPaidAmount = Float.parseFloat(request.getParameter("totalMop"));
				}else if(paidCurrency != null && paidCurrency.equals("HKD")){
					paidAmount = Float.parseFloat(request.getParameter("totalHkd"));
					netPaidCurrency = "MOP";
					netPaidAmount = Float.parseFloat(request.getParameter("totalMop"));
				}
				
				String[] isbn = request.getParameterValues("isbn");
				String[] mopPrice = request.getParameterValues("mopPrice");
				
				List odList = new ArrayList();
				for(int i=0; i<isbn.length; i++ ){
					OrDetail od = new OrDetail();
					String isbnValue = isbn[i];
					od.setOrderSeqNo(orderSeqNo);
					od.setOrderIntake(curIntake);
					od.setIsbn(isbnValue);
					od.setUpdDate(new Date());
					od.setUpdUid(userId);
					od.setPaidAmount(Double.parseDouble(mopPrice[i]));
					odList.add(od);
				}
				
				float amercePercent = 0;
								
		        double feePercent = 0;
		        
		        Double curRate = new Double(1);
		        if(!"MOP".equals(paidCurrency)){
		        	if("RMB".equals(paidCurrency)){
		        		curRate = (Double)request.getAttribute("rmbRate");
		        	} else {
		        		curRate = (Double)request.getAttribute("hkdRate");
		        	}
		        }
		        		        			
				boolean updateResult = orderDAOImpl.updateOrder(conn, orderSeqNo, curIntake, paidCurrency, paidAmount, amercePrice, userId, shippingFee, netPaidCurrency, netPaidAmount, curRate) ;
				
				//更新購買時圖書的單價
				boolean updateBookPrice = orderDetailDAOImpl.updateOdBookPrice(conn, odList);
				
				Transaction t = new Transaction();
				t.setTransactionNo(0);
				t.setOrderSeqNo(String.valueOf(orderSeqNo));
				t.setCashier(userId);
				t.setPaidDate(new Date());
				t.setPaidMentType("收費");
				t.setPay(paidAmount);
				t.setPaidCurrency(paidCurrency);
				t.setRemarks("");
				t.setStudentNo(retail);
				t.setStatus("Y");
				TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
				boolean transactionFlag = transactionDAOImpl.addTransactionInfo(conn, t);
				
				if(updateResult && updateBookPrice && transactionFlag){
					conn.commit();
					Order order = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNo);
					List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, ",", curIntake, order.getPaidStatus(), orderSeqNo);
					session.setAttribute("orderedBookList", orderedBookList);
					request.setAttribute("payResult", "繳費成功!");
					request.setAttribute("order", order);
					request.setAttribute("paidCurrency", order.getPaidCurrency());
					request.setAttribute("oprStep", "PAY");
					request.setAttribute("oprType", "PAY");
				} else {
					conn.rollback();
				}
				request.setAttribute("feePercent", feePercent);
				request.setAttribute("amercePercent", amercePercent);
				request.setAttribute("shippingFee", (shippingFee*100));
				request.setAttribute("retail", retail);
				request.setAttribute("orderSeqNo", orderSeqNo);
				request.setAttribute("back", request.getParameter("back"));
				request.getRequestDispatcher("retailBookList.jsp").forward(request, response);
			}else if(type != null && type.equals("receiveRetailBook")){
				request.setAttribute("back", request.getParameter("back"));
				//-----------------------------零售領書-------------------------------
				request.setAttribute("retail", "RETAIL");
				int seqNoParam = 0;
				if(request.getParameter("orderSeqNo")!=null){
					seqNoParam = Integer.parseInt(request.getParameter("orderSeqNo"));
				}
				request.setAttribute("orderSeqNo", seqNoParam);
				String[] isbn = request.getParameterValues("isbn");
				String[] notEnoughQty = request.getParameterValues("notEnoughQty");
				String[] oldNotEnoughQty = request.getParameterValues("oldNotEnoughQty");
				String[] confirmQty = request.getParameterValues("confirmQty");
				
				//只更新缺書數被修改過的記錄
				List seqNoList = new ArrayList();
				List isbnList = new ArrayList();
				List qtyList = new ArrayList();
				List oldQtyList = new ArrayList();
				List confirmQtyList = new ArrayList();
				for(int i=0;i<isbn.length;i++){
					String isbnValue = isbn[i];
					String value = notEnoughQty[i];
					String oldValue = oldNotEnoughQty[i];
					String confirmValue = confirmQty[i];
					if(!value.equals(oldValue)){
						isbnList.add(isbnValue);
						qtyList.add(value);
						oldQtyList.add(oldValue);
						confirmQtyList.add(confirmValue);
						seqNoList.add(String.valueOf(seqNoParam));
					}
				}
				BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();			
				String[] newSeqNo = new String[seqNoList.size()];
				String[] newIsbn = new String[isbnList.size()];
				String[] newNotEnoughQty = new String[isbnList.size()];
				String[] newConfirmQty = new String[confirmQtyList.size()];
				String[] oldQty = new String[oldQtyList.size()];
				
				List<BookInventory> bookInventoryList = new ArrayList<BookInventory>();
				String msg = "";
				if(isbnList.size()>0){
					for(int i=0;i<isbnList.size();i++){
						newSeqNo[i] = (String)seqNoList.get(i);
						newIsbn[i] = (String)isbnList.get(i);
						newNotEnoughQty[i] = (String)qtyList.get(i);
						oldNotEnoughQty[i] = (String)oldQtyList.get(i);
						confirmQty[i] = (String)confirmQtyList.get(i);
						
						BookInventory bookInventory = new BookInventory();
						bookInventory.setIsbn(ToolsOfString.getUniqueIsbn(newIsbn[i]));
						bookInventory.setUpddate(new Date());
						bookInventory.setUpduid(userId);
						if(newNotEnoughQty[i].equals("0")){
							if(oldNotEnoughQty[i].equals("") && !oldNotEnoughQty[i].equals("0")){
								bookInventory.setStock(-Integer.parseInt(confirmQty[i]));
								bookInventoryList.add(bookInventory);
							}else if(!oldNotEnoughQty[i].equals("") && !oldNotEnoughQty[i].equals("0")){
								bookInventory.setStock(-Integer.parseInt(oldNotEnoughQty[i]));
								bookInventoryList.add(bookInventory);
							}
						}else if(newNotEnoughQty[i].equals("")){
							if(!oldNotEnoughQty[i].equals("") && oldNotEnoughQty[i].equals("0")){
								bookInventory.setStock(Integer.parseInt(confirmQty[i]));
								bookInventoryList.add(bookInventory);
							}else if(!oldNotEnoughQty[i].equals("") && !oldNotEnoughQty[i].equals("0")){
								bookInventory.setStock(Integer.parseInt(confirmQty[i]) - Integer.parseInt(oldNotEnoughQty[i]));
								bookInventoryList.add(bookInventory);
							}
						}else{
							if(oldNotEnoughQty[i].equals("0") && !oldNotEnoughQty[i].equals("")){
								bookInventory.setStock(Integer.parseInt(newNotEnoughQty[i]));
								bookInventoryList.add(bookInventory);
							}else if(!oldNotEnoughQty[i].equals("0") && oldNotEnoughQty[i].equals("")){
								bookInventory.setStock(-(Integer.parseInt(confirmQty[i]) - Integer.parseInt(newNotEnoughQty[i])));
								bookInventoryList.add(bookInventory);
							}else if(!oldNotEnoughQty[i].equals("0") && !oldNotEnoughQty[i].equals("")){
								int differ = Integer.parseInt(newNotEnoughQty[i]) - Integer.parseInt(oldNotEnoughQty[i]);
								if(differ>0){
									bookInventory.setStock(differ);
									bookInventoryList.add(bookInventory);
								}else{
									bookInventory.setStock(-differ);
									bookInventoryList.add(bookInventory);
								}
							}
						}
						BookInventory bookInventoryInfo = bookInventoryDAOImpl.getBookInventoryByPK(conn, bookInventory.getIsbn());
						if(bookInventoryInfo==null){
							msg += newIsbn[i]+",";
						}
					}
					if(msg.length()!=0){
						msg = msg.substring(0, msg.length()-1);
					}
				}else{
					msg = "nothing";
				}
				double feePercent = 0;
		        request.setAttribute("feePercent", feePercent);
		        
		        Order order = null;
		        double amerceMount = 0;
		        session.setAttribute("amerceMount", amerceMount);
				float amercePercent = 0;
				
				if(!msg.equals("")){
					request.setAttribute("updateResult", msg.equals("nothing")?"數據已保存過！":"編號為 "+msg+" 的圖書沒有庫存記錄！");
					request.setAttribute("oprType", "RECEIVE");
					request.setAttribute("payResult", "");
					request.setAttribute("oprStep", "RECEIVE");
					request.setAttribute("order", order);
					request.setAttribute("amercePercent", amercePercent);
					request.getRequestDispatcher("retailBookList.jsp").forward(request, response);
				}else{
					OrderDetailDAOImpl impl = new OrderDetailDAOImpl();
					String errMsg = impl.updateNotEnQty(conn, curIntake, newSeqNo, newIsbn, newNotEnoughQty, userId);
					
					if(!errMsg.equals("")) {
						conn.rollback();
						request.setAttribute("updateResult", "保存失敗!");
						request.setAttribute("oprType", "RECEIVE");
						request.setAttribute("payResult", "");
						request.setAttribute("oprStep", "RECEIVE");
						request.setAttribute("order", order);
						request.setAttribute("amercePercent", amercePercent);
						request.getRequestDispatcher("retailBookList.jsp").forward(request, response);
					} else {
						boolean updateBookInventoryFlag = bookInventoryDAOImpl.updateBookInventory(conn, bookInventoryList);
						if(updateBookInventoryFlag){
							conn.commit();
							
							List orderedBookList = studentBookDAOImpl.searchOrderedBookByRetailNo(conn, seqNoParam, curIntake);
							session.setAttribute("orderedBookList", orderedBookList);
							
							request.setAttribute("updateResult", "保存成功!");
							request.setAttribute("oprType", "RECEIVE");
							request.setAttribute("order", order);
							request.setAttribute("payResult", "");
							request.setAttribute("oprStep", "RECEIVE");
							request.setAttribute("amercePercent", amercePercent);
							request.getRequestDispatcher("retailBookList.jsp").forward(request, response);
						}else{
							conn.rollback();
							request.setAttribute("updateResult", "保存失敗!!");
							request.setAttribute("oprType", "RECEIVE");
							request.setAttribute("payResult", "");
							request.setAttribute("oprStep", "RECEIVE");
							request.setAttribute("order", order);
							request.setAttribute("amercePercent", amercePercent);
							request.getRequestDispatcher("retailBookList.jsp").forward(request, response);
						}
					}
				}
			}else if(type!=null && type.equals("checkRetailOrder")){
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				int orderSeqNo = 0;
				if(request.getParameter("orderSeqNo")!=null){
					orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
				}
				
				Order o = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNo);
				if(o!=null && o.getPaidStatus().equals("Y")){
					out.print("Y");
				}else{
					out.print("N");
				}
			}else if(type!=null && type.equals("returnBack")){
				request.setAttribute("retail", "RETAIL");
				int orderSeqNo = 0;
				if(request.getParameter("orderSeqNo")!=null){
					orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
				}
				String back = null;
				if(request.getParameter("back")!=null){
					back = request.getParameter("back");
				}
				String oprStep = null;
				if(request.getParameter("oprStep")!=null){
					oprStep = request.getParameter("oprStep");
				}
				List orderedBookList = studentBookDAOImpl.searchOrderedBookByRetailNo(conn, orderSeqNo, curIntake);
				session.setAttribute("orderedBookList", orderedBookList);
				
				request.setAttribute("oprStep", oprStep);
				request.setAttribute("orderSeqNo", orderSeqNo);
				if(back!=null && "searchPage".equals(back)){
					Order order = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNo);
					session.setAttribute("order", order);
					request.getRequestDispatcher("retailBookIndex.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher("retailBookPage.jsp").forward(request, response);
				}
			}else if(type!=null && type.equals("retailReceipt")){
	        	int orderSeqNo = 0;
	        	if(request.getParameter("orderSeqNo")!=null){
	        		orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
	        	}
	        	List receiptList = orderDetailDAOImpl.getRetailReceipt(conn, orderSeqNo);
	        	
	        	request.setAttribute("receiptList", receiptList);
	        	request.setAttribute("retail", "RETAIL");
	        	request.getRequestDispatcher("viewRetailReceipt.jsp").forward(request, response);
	        }else if(type!=null && type.equals("exportReport")){
	        	String fromDate = null;
	        	if(request.getParameter("fromDate")!=null){
	        		fromDate = request.getParameter("fromDate");
	        	}
	        	String toDate = null;
	        	if(request.getParameter("toDate")!=null){
	        		toDate = request.getParameter("toDate");
	        	}
	        	
	        	List list = orderDetailDAOImpl.getOrderDetailByDate(conn, fromDate, toDate);
	        	if(list!=null && !list.isEmpty()){
	        		response.reset();
	    			response.setContentType("application/octet-stream");
	    			response.addHeader("Content-Disposition" , "attachment ; filename = retailBookInfo.xls " );
	    			
	    			OutputStream os = response.getOutputStream();
	    			WritableWorkbook workbook = Workbook.createWorkbook(os);
	    			WritableSheet worksheet = workbook.createSheet("result", 0);
	    			
	    			String[] titleName = {"學期","訂單序號","付款狀態","付款幣種","付款金額","收款人","ISBN","書名","確定數","領書數","備註"};
	    			for(int i=0; i<titleName.length; i++){
	    				 Label label = new Label(i, 0, titleName[i]);
	    				 worksheet.addCell(label);
	    			}
	    			for(int i=0; i<list.size(); i++){
	    				int j = 0;
	    				OrDetail od = (OrDetail)list.get(i);
	    				worksheet.addCell(new Label(j++, i+1, od.getOrderIntake()));
	    				worksheet.addCell(new Label(j++, i+1, String.valueOf(od.getOrderSeqNo())));
	    				worksheet.addCell(new Label(j++, i+1, od.getPaidStatus()));
	    				worksheet.addCell(new Label(j++, i+1, od.getPaidCurrency()));
	    				worksheet.addCell(new Number(j++, i+1, od.getPaidAmount()));
	    				worksheet.addCell(new Label(j++, i+1, od.getUpdUid()));
	    				worksheet.addCell(new Label(j++, i+1, od.getIsbn()));
	    				worksheet.addCell(new Label(j++, i+1, od.getBookTitle()));
	    				worksheet.addCell(new Number(j++, i+1, od.getConfirmQty()));
	    				if(od.getNotEnoughQty()==0){
	    					worksheet.addCell(new Number(j++, i+1, od.getConfirmQty()));
	    				}else if(od.getNotEnoughQty() < 0){
	    					worksheet.addCell(new Label(j++, i+1, ""));
	    				}else{
	    					worksheet.addCell(new Number(j++, i+1, od.getConfirmQty()-od.getNotEnoughQty()));
	    				}
	    				worksheet.addCell(new Label(j++, i+1, od.getRemarks()));
	    			}
	    			workbook.write();
	    			workbook.close();
	    		    
	    			os.flush();
	    			os.close();
	        	}else{
	        		request.setAttribute("flag", "false");
	        		request.getRequestDispatcher("statisticRetailBook.jsp").forward(request, response);
	        	}
	        }else if(type!=null && type.equals("retailBookReport")){
	        	request.getRequestDispatcher("statisticRetailBook.jsp").forward(request, response);
	        }else {
	        	session.removeAttribute("list");
				request.getRequestDispatcher("retailBookIndex.jsp").forward(request, response);
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
