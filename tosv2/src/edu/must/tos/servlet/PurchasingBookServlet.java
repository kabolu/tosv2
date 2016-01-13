package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookPurchasing;
import edu.must.tos.bean.BookPurchasingBean;
import edu.must.tos.bean.BookPurchasingCond;
import edu.must.tos.bean.BookSupplier;
import edu.must.tos.bean.SysUser;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookPurchasingCondDAOImpl;
import edu.must.tos.impl.BookPurchasingDAOImpl;
import edu.must.tos.impl.BookSupplierDAOImpl;
import edu.must.tos.impl.SysUserDAOImpl;
import edu.must.tos.util.AutoPurchaseNumber;

public class PurchasingBookServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PurchasingBookServlet() {
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
		HttpSession session = request.getSession();
		Connection conn = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String userId = (String)session.getAttribute("userId");
			String curIntake = (String)session.getAttribute("curIntake");
			
			BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
			BookDAOImpl bookDAOImpl = new BookDAOImpl();
			BookPurchasingDAOImpl bookPurchasingDAOImpl = new BookPurchasingDAOImpl();
			SysUserDAOImpl sysUserDAOImpl = new SysUserDAOImpl();
			BookPurchasingCondDAOImpl bookPurchasingCondDAOImpl = new BookPurchasingCondDAOImpl();
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("saveRecord")){
				String orderDate = "";
				if(request.getParameter("orderDate") != null && !request.getParameter("orderDate").equals("")){
					orderDate = request.getParameter("orderDate");
				}
				String orderBy = null;
				if(request.getParameter("orderBy") != null){
					orderBy = request.getParameter("orderBy");
				}
				String orderPp = null;
				if(request.getParameter("orderPp") != null){
					orderPp = request.getParameter("orderPp");
				}
				String orderNo = "";
				if(request.getParameter("orderNo") != null){
					orderNo = request.getParameter("orderNo");
				}
				if(orderNo.equals("")){
					if(orderBy != null && !orderBy.equals("OTHER"))
						orderNo = AutoPurchaseNumber.getPurchaseNumber(conn, Integer.parseInt(orderBy), curIntake);
					else
						orderNo = AutoPurchaseNumber.getPurchaseNumber(conn, 0, curIntake);
				}
				String issued = "Y";
				if(request.getParameter("issued") != null){
					issued = request.getParameter("issued");
				}
				String intake = null;
				if(request.getParameter("intake") != null){
					intake = request.getParameter("intake");
				}
				boolean flag2 = false;
				List searchList = (List)session.getAttribute("searchList");
				int recdActY = 0;
				if(searchList != null && !searchList.isEmpty()){
					for(int i=0; i<searchList.size(); i++){
						BookPurchasingBean bean = (BookPurchasingBean)searchList.get(i);
						BookPurchasing bookPurchasing = new BookPurchasing();
						bookPurchasing.setId(bean.getId());
						bookPurchasing.setIntake(bean.getBookPurchase().getIntake());
						bookPurchasing.setOrderNo(orderNo);
						bookPurchasing.setIsbn(bean.getBook().getIsbn());
						bookPurchasing.setQuantity(bean.getBookPurchase().getQuantity());
						bookPurchasing.setCostPrice(bean.getBookPurchase().getCostPrice());
						bookPurchasing.setOrderBy(orderBy);
						if(orderPp != null){
							bookPurchasing.setOrderPp(orderPp);
						}						
						bookPurchasing.setRemarks(bean.getBookPurchase().getRemarks());
						bookPurchasing.setIssued(issued);
						bookPurchasing.setOrderDate(orderDate);
						if(bean.getBookPurchase().getQuantity() == 0){
							bookPurchasing.setActind("N");
						}else {
							bookPurchasing.setActind("Y");
							recdActY++;
						}
						bookPurchasing.setUpdDate(new Date());
						bookPurchasing.setUpdUid(userId);
						bookPurchasing.setLeave(bean.getBookPurchase().getLeave());
						flag2 = bookPurchasingDAOImpl.updateBookPurchasing(conn, bookPurchasing);
						if(!flag2)
							break;
					}
				}else{
					flag2 = true;
				}
				boolean flag1 = false;
				List resultList = (List)session.getAttribute("resultList");
				if(resultList != null && !resultList.isEmpty()){
					for(int i=0; i<resultList.size(); i++){
						BookPurchasingBean bean = (BookPurchasingBean)resultList.get(i);
						BookPurchasing bookPurchasing = new BookPurchasing();
						bookPurchasing.setId(0);
						bookPurchasing.setIntake(bean.getBookPurchase().getIntake());
						bookPurchasing.setOrderNo(orderNo);
						bookPurchasing.setIsbn(bean.getBook().getIsbn());
						bookPurchasing.setQuantity(bean.getBookPurchase().getQuantity());
						bookPurchasing.setCostPrice(bean.getBookPurchase().getCostPrice());
						bookPurchasing.setOrderBy(orderBy);
						if(orderPp != null){
							bookPurchasing.setOrderPp(orderPp);
						}
						bookPurchasing.setRemarks(bean.getBookPurchase().getRemarks());
						bookPurchasing.setIssued(issued);
						bookPurchasing.setOrderDate(orderDate);
						bookPurchasing.setActind("Y");
						bookPurchasing.setCreDate(new Date());
						bookPurchasing.setCreUid(userId);
						bookPurchasing.setUpdDate(new Date());
						bookPurchasing.setUpdUid(userId);
						flag1 = bookPurchasingDAOImpl.addBookPurchasing(conn, bookPurchasing);
						if(!flag1)
							break;
					}
				}else{
					flag1 = true;
				}
				boolean flag3 = true;
				if("M".equals(issued) && ((resultList != null && !resultList.isEmpty()) || recdActY >= 1)){
					BookPurchasing bean = new BookPurchasing();
					bean.setIntake(intake);
					bean.setOrderNo(orderNo);
					bean.setOrderBy(orderBy);
					bean.setOrderPp(orderPp);
					bean.setOrderDate(orderDate);
					bean.setIssued(issued);
					bean.setUpdUid(userId);
					bean.setUpdDate(new Date());
					bean.setActind("Y");
					flag3 = bookPurchasingDAOImpl.updateBookPurchasingByOrderNo(conn, bean);
				}
				
				if(flag1 && flag2 && flag3){
					conn.commit();
					request.setAttribute("msg", "代購圖書資料保存成功！<p>代購編號為：" + orderNo +"</p>");
				}else{
					conn.rollback();
					request.setAttribute("msg", "代購圖書資料保存失敗！");
				}
				request.setAttribute("issued", issued);
				request.setAttribute("type", "BookPurchasing");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			}else if(type != null && type.equals("checkSearchList")){
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				
				String BfromDate = null;
				if(request.getParameter("BfromDate") != null){
					BfromDate = request.getParameter("BfromDate");
				}
				String BtoDate = null;
				if(request.getParameter("BtoDate") != null){
					BtoDate = request.getParameter("BtoDate");
				}
				String supplierNo = null;
				if(request.getParameter("supplierNo") != null && !request.getParameter("supplierNo").equals("0")){
					supplierNo = request.getParameter("supplierNo");
				}
				List list = bookPurchasingDAOImpl.getBookPurchasingInfo(conn, curIntake, BfromDate, BtoDate, supplierNo);
				if(list != null && !list.isEmpty()){
					out.print(1);
				}else{
					out.print(0);
				}
			}else if(type != null && type.equals("checkCondition")){
				String BfromDate = null;
				if(request.getParameter("BfromDate") != null && !request.getParameter("BfromDate").equals("")){
					BfromDate = request.getParameter("BfromDate");
				}
				String BtoDate = null;
				if(request.getParameter("BtoDate") != null && !request.getParameter("BtoDate").equals("")){
					BtoDate = request.getParameter("BtoDate");
				}
				String supplierNo = null;
				if(request.getParameter("supplierNo") != null && !request.getParameter("supplierNo").equals("0")){
					supplierNo = request.getParameter("supplierNo");
				}
				BfromDate = BfromDate == null? "ALL" : BfromDate;
				BtoDate = BtoDate == null? "ALL" : BtoDate;
				List dateList = bookPurchasingCondDAOImpl.getPurchasingCond(conn, curIntake, BfromDate, BtoDate, supplierNo);
				
				if(dateList != null && !dateList.isEmpty()){
					response.setContentType("text/html;charset=utf-8");
					PrintWriter out = response.getWriter();
					response.setContentType("text/xml;charset=UTF-8");
					response.setHeader("ContentType","text/xml");
					request.setCharacterEncoding("UTF-8");
					StringBuilder xml = new StringBuilder();
					xml.append("<?xml version='1.0' encoding='UTF-8'?>");
					xml.append("<results>");
					for(int i=0;i<dateList.size();i++){
						BookPurchasingCond sc = (BookPurchasingCond)dateList.get(i);
						xml.append("<result>");
						xml.append("<credate>"+sc.getCreDate().toString().substring(0, sc.getCreDate().toString().length()-2)+"</credate>");
						xml.append("<fromdate>"+sc.getFromDate()+"</fromdate>");
						xml.append("<todate>"+sc.getToDate()+"</todate>");
						xml.append("</result>");
					}
					xml.append("</results>");
					out.print(xml.toString());
				}
			}else if(type != null && type.equals("searchData")){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				SysUser u = new SysUser();
				u.setUserId(userId);
				String userName = "";
				String contactNo = "";
				String faxNo = "";
				String email = "";
				SysUser user = sysUserDAOImpl.getUserInfoById(conn, u);
				if(user.getUserName() != null ){
					userName = user.getUserName();
				}
				if(user.getContactNo() != null){
					contactNo = user.getContactNo();
				}
				if(user.getFaxNo() != null){
					faxNo = user.getFaxNo();
				}
				if(user.getEmail() != null){
					email = user.getEmail();
				}
				
				String searchType = null;
				if(request.getParameter("searchType") != null){
					searchType = request.getParameter("searchType");
				}
				String supplierNo = "";
				if(request.getParameter("supplierNo") != null && !request.getParameter("supplierNo").equals("0")){
					supplierNo = request.getParameter("supplierNo");
				}
				
				String param = "";
				if(request.getParameter("param") != null){
					param = request.getParameter("param");
				}
				
				if(searchType != null && "B".equals(searchType)){
					String BfromDate = null;
					if(request.getParameter("BfromDate") != null && !request.getParameter("BfromDate").equals("")){
						BfromDate = request.getParameter("BfromDate");
					}
					String BtoDate = null;
					if(request.getParameter("BtoDate") != null && !request.getParameter("BtoDate").equals("")){
						BtoDate = request.getParameter("BtoDate");
					}
					boolean flag = false;
					boolean flag2 = false;
					if(param != null && "Y".equals(param)){
						BookPurchasingCond cond = new BookPurchasingCond();
						cond.setCreDate(new Date());
						cond.setCreUid(userId);
						cond.setFromDate(BfromDate == null? "ALL" : BfromDate);
						cond.setToDate(BtoDate == null? "ALL" : BtoDate);
						cond.setOrderBy(String.valueOf(supplierNo));
						cond.setIntake(curIntake);
						flag = bookPurchasingCondDAOImpl.addBookPurchasingCond(conn, cond);
						
						flag2 = bookPurchasingDAOImpl.updateBookPurchasingBySupplierNo(conn, userId, supplierNo, BfromDate, BtoDate, curIntake);
					}else{
						flag = true;
						flag2 = true;
					}
					if(flag && flag2){
						String supplierName = "";
						String supplierTel = "";
						String supplierFax = "";
						String supplierEmail = "";
						if(!supplierNo.equals("0") && !supplierNo.equals("OTHER")){
							BookSupplier bookSupplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, Integer.parseInt(supplierNo));
							if(bookSupplier.getSupplierName() != null){
								supplierName = bookSupplier.getSupplierName();
							}
							if(bookSupplier.getSupplierTel_1() != null){
								supplierTel = bookSupplier.getSupplierTel_1();
								if(bookSupplier.getSupplierTel_2() != null){
									supplierTel += " / "+bookSupplier.getSupplierTel_2();;
								}
							}
							if(bookSupplier.getSupplierFax_1() != null){
								supplierFax = bookSupplier.getSupplierFax_1();
								if(bookSupplier.getSupplierFax_2() != null){
									supplierFax += " / "+bookSupplier.getSupplierFax_2();;
								}
							}
							if(bookSupplier.getSupplierEmail() != null){
								supplierEmail = bookSupplier.getSupplierEmail();
							}
						}						
						
						/**
						 * Export Data begin....
						 */
						response.reset();
					    response.setContentType("application/octet-stream");
						response.addHeader("Content-Disposition" , "attachment ; filename = bookPurchasing.xls " );
						
						OutputStream os = response.getOutputStream();
						WritableWorkbook workbook = Workbook.createWorkbook(os);
						WritableSheet worksheet = workbook.createSheet("result", 0);
						
						//表格式樣
						WritableCellFormat titleFormat = new WritableCellFormat();
						titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
						
						worksheet.mergeCells(0, 0, 9, 0);
						worksheet.addCell(new Label(0, 0, "澳門科技大學\012 "+ curIntake +"學期購書訂單", titleFormat));
						
						worksheet.mergeCells(0, 1, 9, 1);
						worksheet.addCell(new Label(0, 1, "書商："+supplierName));
						
						worksheet.mergeCells(0, 2, 9, 2);
						worksheet.addCell(new Label(0, 2, "書商聯繫方式：電話："+supplierTel+"  傳真："+supplierFax+"  電郵："+supplierEmail));
						
						worksheet.mergeCells(0, 3, 9, 3);
						worksheet.addCell(new Label(0, 3, "發件人："+userName));
						
						worksheet.mergeCells(0, 4, 9, 4);
						worksheet.addCell(new Label(0, 4, "聯繫方式：電話："+contactNo+"  傳真："+faxNo+"  電郵："+email));
						
						worksheet.mergeCells(0, 5, 9, 5);
						worksheet.addCell(new Label(0, 5, "發單日期："+df.format(new Date())));
						
						//表格式樣
						WritableCellFormat cellFormat = new WritableCellFormat();
						cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
						cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
						cellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
						
						String[] title = {"序號","書名","ISBN","作者","出版社","版次","出版年","學生訂數(付款)","代購訂數","總訂數數"};
						
						for(int i=0; i<title.length; i++){
							 Label titleLabel = new Label(i, 6, title[i], cellFormat);
							 worksheet.addCell(titleLabel);
						}
						List list = bookPurchasingDAOImpl.getBookPurchasingInfo(conn, curIntake, BfromDate, BtoDate, supplierNo);
						if(list != null && !list.isEmpty()){
							for(int i=0; i<list.size(); i++){
								int j = 0;
								BookPurchasingBean bean = (BookPurchasingBean)list.get(i);
								worksheet.addCell(new Label(j++, i+7, String.valueOf(i+1)));
								worksheet.addCell(new Label(j++, i+7, bean.getBook().getTitle()));
								worksheet.addCell(new Label(j++, i+7, bean.getBook().getIsbn()));
								worksheet.addCell(new Label(j++, i+7, bean.getBook().getAuthor()));
								worksheet.addCell(new Label(j++, i+7, bean.getBook().getPublisher()));
								worksheet.addCell(new Label(j++, i+7, bean.getBook().getPublishYear()));
								worksheet.addCell(new Label(j++, i+7, bean.getBook().getEdition()));
								worksheet.addCell(new Number(j++, i+7, bean.getTotalConfirmSum()));
								worksheet.addCell(new Number(j++, i+7, bean.getTotalQuantitySum()));
								worksheet.addCell(new Number(j++, i+7, (bean.getTotalConfirmSum()+bean.getTotalQuantitySum()) ));
							}
						}
						workbook.write();
						workbook.close();
					    
						os.flush();
						os.close();
					}else{
						//System.out.println("save bookpurchasingcond fail!");
					}
				}else if(searchType != null && "A".equals(searchType)){
					String orderNo = null;
					if(request.getParameter("orderNo") != null){
						orderNo = request.getParameter("orderNo");
					}
					String AfromDate = null;
					if(request.getParameter("AfromDate") != null){
						AfromDate = request.getParameter("AfromDate");
					}
					String AtoDate = null;
					if(request.getParameter("AtoDate") != null){
						AtoDate = request.getParameter("AtoDate");
					}
					List list = null;
					if(orderNo != null){
						list = bookPurchasingDAOImpl.getBookPurchasingByOrderNo(conn, orderNo, AfromDate, AtoDate);
					}
					if(list.isEmpty()){
						request.setAttribute("searchResult", "沒有符合該條件的記錄！");
					}
					request.setAttribute("list", list);
					request.getRequestDispatcher("purchasingBook.jsp").forward(request, response);
				}				
			}else if(type != null && type.equals("forwardPage")){
				session.removeAttribute("resultList");
				session.removeAttribute("orderDate");
				session.removeAttribute("orderBy");
				session.removeAttribute("orderPp");
				
				List bookSupplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
				request.setAttribute("bookSupplierList", bookSupplierList);
				
				String orderNo = null;
				if(request.getParameter("orderNo") != null && !request.getParameter("orderNo").equals("")){
					orderNo = request.getParameter("orderNo");
				}
				List list = bookPurchasingDAOImpl.getBookPurchasingBeanByOrderNo(conn, orderNo);
				if(list != null && !list.isEmpty()){
					BookPurchasingBean bean = (BookPurchasingBean)list.get(0);
					session.setAttribute("orderNo", bean.getOrderNo());
					session.setAttribute("orderDate", bean.getBookPurchase().getOrderDate());
					session.setAttribute("orderBy", bean.getBookPurchase().getOrderBy());
					session.setAttribute("orderPp", bean.getBookPurchase().getOrderPp());
					//以編輯數據的intake為準
					request.setAttribute("intake", bean.getBookPurchase().getIntake());
				}
				
				session.setAttribute("searchList", list);
				
				request.getRequestDispatcher("purchasingBookEdit.jsp").forward(request, response);
			}else if(type != null && type.equals("searchIsbn")){
				List bookSupplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
				request.setAttribute("bookSupplierList", bookSupplierList);
				
				String orderNo = null;
				if(request.getParameter("orderNo") != null){
					orderNo = request.getParameter("orderNo");
				}
				String orderDate = null;
				if(request.getParameter("orderDate") != null){
					orderDate = request.getParameter("orderDate");
				}
				String orderBy = null;
				if(session.getAttribute("orderBy") != null){
					orderBy = (String)session.getAttribute("orderBy");
				}else if(request.getParameter("orderBy") != null){
					orderBy = request.getParameter("orderBy");
				}
				
				String orderPp = null;
				if(orderBy != null && "OTHER".equals(orderBy)){
					orderPp = request.getParameter("orderPp");
				}
				String intake = null;
				if(request.getParameter("intake") != null){
					intake = request.getParameter("intake");
				}
				String isbn = null;
				if(request.getParameter("isbn") != null){
					isbn = request.getParameter("isbn");
				}
				List resultList = null;
				if(session.getAttribute("resultList") == null){
					resultList = new ArrayList();
				}else{
					resultList = (List)session.getAttribute("resultList");
				}

				
				Book book = bookDAOImpl.getBookByPK(conn, isbn);
				if(book != null && book.getIsbn() != null){
					BookPurchasingBean bean = new BookPurchasingBean();
					bean.getBook().setIsbn(book.getIsbn());
					bean.getBook().setTitle(book.getTitle());
					bean.getBook().setAuthor(book.getAuthor());
					bean.getBook().setEdition(book.getEdition());
					bean.setOrderNo(orderNo);
					bean.getBookPurchase().setOrderBy(orderBy);
					bean.getBookPurchase().setOrderPp(orderPp);
					bean.getBookPurchase().setOrderDate(orderDate);
					bean.getBookPurchase().setIntake(intake);
					//add by xfke取出優惠價
					bean.getBookPurchase().setCostPrice(book.getFavourablePrice());
					resultList.add(bean);
				}else{
					request.setAttribute("searchIsbnResult", "沒有該圖書ISBN記錄！");
				}
				session.setAttribute("orderDate", orderDate);
				session.setAttribute("orderBy", orderBy);
				session.setAttribute("orderPp", orderPp);
				session.setAttribute("resultList", resultList);
				if(orderNo != null){
					request.setAttribute("intake", intake);
					request.getRequestDispatcher("purchasingBookEdit.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher("purchasingBookAdd.jsp").forward(request, response);
				}
			}else if(type != null && type.equals("remove")){
				
				String i = request.getParameter("i");
				int id = Integer.parseInt(i);
				List resultList = (List)session.getAttribute("resultList");
				BookPurchasingBean bean = (BookPurchasingBean)resultList.get(id);
				
				resultList.remove(id);
				session.setAttribute("resultList", resultList);
				
				List bookSupplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
				request.setAttribute("bookSupplierList", bookSupplierList);
				
				//rainbow add
				String intake = (String)bean.getBookPurchase().getIntake();
				if(intake != null && !intake.equals(""))
					request.setAttribute("intake",intake);
				
				if(request.getParameter("page") != null && request.getParameter("page").equals("EDIT")){
					request.getRequestDispatcher("purchasingBookEdit.jsp").forward(request, response);
				}
				else{
					request.getRequestDispatcher("purchasingBookAdd.jsp").forward(request, response);
				}
			
			}else if(type != null && type.equals("saveSession")){
				int i = 0;
				if(request.getParameter("i") != null){
					i = Integer.parseInt(request.getParameter("i"));
				}
				List resultList = (List)session.getAttribute("resultList");
				
				List searchList = (List)session.getAttribute("searchList");
				int quantity = 0;
				String remarks = "";
								
				//add by xfke
				double favourablePrice = 0;

				String param = request.getParameter("param");
				if(param != null && "quantity".equals(param)){
					resultList = (List)session.getAttribute("resultList");
					BookPurchasingBean bean = (BookPurchasingBean)resultList.get(i);
					if(request.getParameter("value") != null && !request.getParameter("value").equals("")){
						quantity = Integer.parseInt(request.getParameter("value"));
					}
					bean.getBookPurchase().setQuantity(quantity);
				}else if(param != null && "remarks".equals(param)){
					resultList = (List)session.getAttribute("resultList");
					BookPurchasingBean bean = (BookPurchasingBean)resultList.get(i);
					remarks = request.getParameter("value");
					bean.getBookPurchase().setRemarks(remarks);
				}else if(param != null && "favourablePrice".equals(param)){
					resultList = (List)session.getAttribute("resultList");
					BookPurchasingBean bean = (BookPurchasingBean)resultList.get(i);
					favourablePrice = Double.parseDouble(request.getParameter("value"));
					bean.getBookPurchase().setCostPrice(favourablePrice);
				}else if(param != null && "sFavourablePrice".equals(param)){
					searchList = (List)session.getAttribute("searchList");
					BookPurchasingBean bean = (BookPurchasingBean)searchList.get(i);
					favourablePrice = Double.parseDouble(request.getParameter("value"));
					bean.getBookPurchase().setCostPrice(favourablePrice);
				}else if(param != null && "sRemarks".equals(param)){
					searchList = (List)session.getAttribute("searchList");
					BookPurchasingBean bean = (BookPurchasingBean)searchList.get(i);
					remarks = request.getParameter("value");
					bean.getBookPurchase().setRemarks(remarks);
				}else if(param!=null && "sQuantity".equals(param)){
					searchList = (List)session.getAttribute("searchList");
					BookPurchasingBean bean = (BookPurchasingBean)searchList.get(i);
					if(request.getParameter("value") != null && !request.getParameter("value").equals("")){
						quantity = Integer.parseInt(request.getParameter("value"));
					}
					int leave = bean.getBookPurchase().getLeave();
					int temp = bean.getBookPurchase().getQuantity();
					
					if(leave == 0){
						if(quantity >= temp){
							bean.getBookPurchase().setQuantity(quantity);
							bean.getBookPurchase().setLeave(quantity - temp);
						}
					}else{
						if(temp == leave){
							bean.getBookPurchase().setQuantity(quantity);
							bean.getBookPurchase().setLeave(quantity);
						}else if(temp > leave){
							if(quantity >= temp){
								bean.getBookPurchase().setQuantity(quantity);
								bean.getBookPurchase().setLeave(leave + (quantity - temp));
							}else if((temp - leave) <= quantity && quantity < temp){
								bean.getBookPurchase().setQuantity(quantity);
								bean.getBookPurchase().setLeave(leave - (temp - quantity));
							}
						}
					}
				}
				session.setAttribute("searchList", searchList);
				session.setAttribute("resultList", resultList);
			}else if(type != null && type.equals("addPage")){
				session.removeAttribute("resultList");
				session.removeAttribute("orderDate");
				session.removeAttribute("orderBy");
				session.removeAttribute("orderPp");
				session.removeAttribute("searchList");
				List bookSupplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
				request.setAttribute("bookSupplierList", bookSupplierList);
				request.getRequestDispatcher("purchasingBookAdd.jsp").forward(request, response);
			}else if(type != null && type.equals("report")){
				String fromDate = null;
				if(request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")){
					fromDate = request.getParameter("fromDate");
				}
				String toDate = null;
				if(request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")){
					toDate = request.getParameter("toDate");
				}
				List list = bookPurchasingDAOImpl.getBookPurchasingReport(conn, curIntake, fromDate, toDate);
				List detailList = bookPurchasingDAOImpl.getBookPurchasingList(conn, curIntake, fromDate, toDate);
				if(list != null && !list.isEmpty() && detailList != null && !detailList.isEmpty()){
					response.reset();
					response.setContentType("application/octet-stream");
					response.addHeader("Content-Disposition" , "attachment ; filename = StatisticBookPurchasing.xls " );
					
					OutputStream os = response.getOutputStream();
					WritableWorkbook workbook = Workbook.createWorkbook(os);
					WritableSheet worksheet = workbook.createSheet("Result", 0);
					String msg = "";
					if(fromDate != null && toDate != null){
						msg = "搜索日期為 " + fromDate +" ~ " + toDate;
					}else {
						msg = "搜索日期為空！";
					}
					worksheet.mergeCells(0, 0, 8, 0);
					worksheet.addCell(new Label(0, 0, msg));
					String[] titleName = {"書商", "ISBN", "書名", "作者", "出版商", "出版年份", "版本", "代購數量", "創建人"};
					for(int i=0; i<titleName.length; i++){
						 Label label = new Label(i, 1, titleName[i]);
						 worksheet.addCell(label);
					}
					for(int i=0; i<list.size(); i++){
						int j = 0;
						BookPurchasingBean bean = (BookPurchasingBean)list.get(i);
						worksheet.addCell(new Label(j++, i+2, bean.getBookPurchase().getOrderBy()));
						worksheet.addCell(new Label(j++, i+2, bean.getBook().getIsbn()));
						worksheet.addCell(new Label(j++, i+2, bean.getBook().getTitle()));
						worksheet.addCell(new Label(j++, i+2, bean.getBook().getAuthor()));
						worksheet.addCell(new Label(j++, i+2, bean.getBook().getPublisher()));
						worksheet.addCell(new Label(j++, i+2, bean.getBook().getPublishYear()));
						worksheet.addCell(new Label(j++, i+2, bean.getBook().getEdition()));
						worksheet.addCell(new Number(j++, i+2, bean.getBookPurchase().getQuantity()));
						worksheet.addCell(new Label(j++, i+2, bean.getBookPurchase().getCreUid()));
					}
					
					WritableSheet worksheet1 = workbook.createSheet("Detail", 1);
					worksheet1.mergeCells(0, 0, 9, 0);
					worksheet1.addCell(new Label(0, 0, msg));
					String[] titleName1 = {"學期","代購編號","ISBN","書名","數量","書商/學院","備註","代購日期","創建人","狀態(N=未發出訂單；M=有修改記錄；Y=已發出訂單)"};
					for(int i=0; i<titleName1.length; i++){
						 Label label = new Label(i, 1, titleName1[i]);
						 worksheet1.addCell(label);
					}
					for(int i=0; i<detailList.size(); i++){
						int j = 0;
						BookPurchasingBean bean = (BookPurchasingBean)detailList.get(i);
						worksheet1.addCell(new Label(j++, i+2, bean.getBookPurchase().getIntake()));
						worksheet1.addCell(new Label(j++, i+2, bean.getOrderNo()));
						worksheet1.addCell(new Label(j++, i+2, bean.getBook().getIsbn()));
						worksheet1.addCell(new Label(j++, i+2, bean.getBook().getTitle()));
						worksheet1.addCell(new Number(j++, i+2, bean.getBookPurchase().getQuantity()));
						worksheet1.addCell(new Label(j++, i+2, bean.getBookPurchase().getOrderBy()));					
						worksheet1.addCell(new Label(j++, i+2, bean.getBookPurchase().getRemarks()));
						worksheet1.addCell(new Label(j++, i+2, bean.getBookPurchase().getOrderDate()));
						worksheet1.addCell(new Label(j++, i+2, bean.getBookPurchase().getCreUid()));
						worksheet1.addCell(new Label(j++, i+2, bean.getBookPurchase().getIssued()));
					}
					
					workbook.write();
					workbook.close();
				    
					os.flush();
					os.close();
				}else{
					request.setAttribute("flag", "false");
					request.getRequestDispatcher("statisticPurchasingBook.jsp").forward(request, response);
				}
			}else{
				session.removeAttribute("supplierList");
				List supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
				session.setAttribute("supplierList", supplierList);
				request.getRequestDispatcher("purchasingBook.jsp").forward(request, response);
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
