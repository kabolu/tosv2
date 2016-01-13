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

import edu.must.tos.bean.BookInventory;
import edu.must.tos.bean.Order;
import edu.must.tos.impl.BookInventoryDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StudentBookDAOImpl;
import edu.must.tos.util.ToolsOfString;

public class UpdateNotEnQtyServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UpdateNotEnQtyServlet() {
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
			
			String retail = null;
			if(request.getParameter("retail") != null){
				retail = request.getParameter("retail");
			}
			if(retail != null && retail.equals("retail")){
				
			}else{	//-----------------------------學員領取圖書-------------------------------
				String studentNo = null;
				String applicantNo = null;
				if(request.getParameter("studentNo") != null && !request.getParameter("studentNo").equals("null")){
					studentNo = request.getParameter("studentNo");
				}
				if(request.getParameter("applicantNo") != null && !request.getParameter("applicantNo").equals("null")){
					applicantNo = request.getParameter("applicantNo");
				}
				String studValue = studentNo + "," + applicantNo;
				
				String result = "";
				if(request.getParameter("result") != null){
					result = (String)request.getParameter("result");
				}
				request.setAttribute("result", result);
				
				String[] orderSeqNo = request.getParameterValues("orderSeqNo");
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
				for(int i=0; i<isbn.length; i++){
					String isbnValue = isbn[i];
					String value = notEnoughQty[i];
					String oldValue = oldNotEnoughQty[i];
					String confirmValue = confirmQty[i];
					String seqNo = orderSeqNo[i];
					if(!value.equals(oldValue)){
						isbnList.add(isbnValue);
						qtyList.add(value);
						oldQtyList.add(oldValue);
						confirmQtyList.add(confirmValue);
						seqNoList.add(seqNo);
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
					for(int i=0; i<isbnList.size(); i++){
						newSeqNo[i] = (String)seqNoList.get(i);
						newIsbn[i] = (String)isbnList.get(i);
						newNotEnoughQty[i] = (String)qtyList.get(i);
						oldNotEnoughQty[i] = (String)oldQtyList.get(i);
						confirmQty[i] = (String)confirmQtyList.get(i);
						
						boolean notEnoughQtyFlag = true;
						BookInventory bookInventory = new BookInventory();
						bookInventory.setIsbn(ToolsOfString.getUniqueIsbn(newIsbn[i]));	//2011-12-09 modified by jblu：若ISBN最後一位為'A'，截取剩餘前面部份；
						bookInventory.setUpddate(new Date());
						bookInventory.setUpduid(userId);
						if(newNotEnoughQty[i].equals("0")){	//領書狀態
							if(oldNotEnoughQty[i].equals("") && !oldNotEnoughQty[i].equals("0")){
								//從未領書到領書狀態
								bookInventory.setStock(-Integer.parseInt(confirmQty[i]));
							}else if(!oldNotEnoughQty[i].equals("") && !oldNotEnoughQty[i].equals("0")){
								//從缺書到領書狀態
								bookInventory.setStock(-Integer.parseInt(oldNotEnoughQty[i]));
							}
							bookInventoryList.add(bookInventory);
						}else if(newNotEnoughQty[i].equals("")) {	//未領書狀態
							if(!oldNotEnoughQty[i].equals("") && oldNotEnoughQty[i].equals("0")){
								//從領書到未領書狀態
								bookInventory.setStock(Integer.parseInt(confirmQty[i]));
							}else if(!oldNotEnoughQty[i].equals("") && !oldNotEnoughQty[i].equals("0")){
								//從缺書到未領書狀態
								bookInventory.setStock(Integer.parseInt(confirmQty[i]) - Integer.parseInt(oldNotEnoughQty[i]));
							}
							bookInventoryList.add(bookInventory);
						}else{	//缺書狀態
							if(oldNotEnoughQty[i].equals("0") && !oldNotEnoughQty[i].equals("")){
								if(Integer.parseInt(confirmQty[i]) == Integer.parseInt(newNotEnoughQty[i])){
									notEnoughQtyFlag = false;
								}
								//從領書到缺書狀態
								bookInventory.setStock(Integer.parseInt(newNotEnoughQty[i]));
							}else if(!oldNotEnoughQty[i].equals("0") && oldNotEnoughQty[i].equals("")){
								//從未領書到缺書狀態
								if(Integer.parseInt(confirmQty[i]) == Integer.parseInt(newNotEnoughQty[i])){
									notEnoughQtyFlag = false;
								}
								bookInventory.setStock(-(Integer.parseInt(confirmQty[i]) - Integer.parseInt(newNotEnoughQty[i])));
							}else if(!oldNotEnoughQty[i].equals("0") && !oldNotEnoughQty[i].equals("")){
								//從缺書到變更缺書數量狀態
								if(Integer.parseInt(newNotEnoughQty[i]) > Integer.parseInt(oldNotEnoughQty[i])){
									int differ = Integer.parseInt(newNotEnoughQty[i]) - Integer.parseInt(oldNotEnoughQty[i]);
									if(differ>0){
										bookInventory.setStock(differ);
									}else{
										bookInventory.setStock(-differ);
									}
								}else{
									int differ = Integer.parseInt(oldNotEnoughQty[i]) - Integer.parseInt(newNotEnoughQty[i]);
									if(differ>0){
										bookInventory.setStock(-differ);
									}else{
										bookInventory.setStock(differ);
									}
								}
							}
							bookInventoryList.add(bookInventory);
						}
						if(notEnoughQtyFlag){
							BookInventory bookInventoryInfo = bookInventoryDAOImpl.getBookInventoryByPK(conn, bookInventory.getIsbn());
							if(bookInventoryInfo == null){
								msg += newIsbn[i]+",";
							}
						}else {
							msg += "";
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
		        //double amerceMount = orderDAOImpl.getAmerceMount(conn, studValue, curIntake);
		        double amerceMount = 0;
		        session.setAttribute("amerceMount", amerceMount);
				float amercePercent = 0;
				
				if(!msg.equals("")){
					request.setAttribute("updateResult", msg.equals("nothing")?"數據已保存過！":"編號為 "+msg+" 的圖書沒有庫存記錄！");
					request.setAttribute("oprType", "receive");
					request.setAttribute("payResult","");
					request.setAttribute("oprStep", "step3");
					request.setAttribute("order", order);
					request.setAttribute("amercePercent", amercePercent);
					request.getRequestDispatcher("receiveBookInfo.jsp").forward(request, response);
				}else{
					OrderDetailDAOImpl impl = new OrderDetailDAOImpl();
					String errMsg = impl.updateNotEnQty(conn, curIntake, newSeqNo, newIsbn, newNotEnoughQty, userId);
					
					if(!errMsg.equals("")) {
						conn.rollback();
						request.setAttribute("updateResult", "保存失敗!");
						request.setAttribute("oprType", "receive");
						request.setAttribute("payResult", "");
						request.setAttribute("oprStep", "step3");
						request.setAttribute("order", order);
						request.setAttribute("amercePercent", amercePercent);
						request.getRequestDispatcher("receiveBookInfo.jsp").forward(request, response);
					} else {
						boolean updateBookInventoryFlag = false;
						if(bookInventoryList.isEmpty()){
							updateBookInventoryFlag = true;
						}else{
							updateBookInventoryFlag = bookInventoryDAOImpl.updateBookInventory(conn, bookInventoryList);
						}
						if(updateBookInventoryFlag){
							conn.commit();
							
							StudentBookDAOImpl  studentBookDAOImpl = new StudentBookDAOImpl();
							int orderSeqNoParam = 0;
							String paidStatus = "Y";
							List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, studValue, curIntake, paidStatus, orderSeqNoParam);
							session.setAttribute("orderedBookList", orderedBookList);
							
							request.setAttribute("updateResult", "保存成功!");
							request.setAttribute("oprType", "receive");
							request.setAttribute("result", request.getParameter("result"));
							request.setAttribute("order", order);
							request.setAttribute("payResult", "");
							request.setAttribute("oprStep", "step3");
							request.setAttribute("amercePercent", amercePercent);
							request.getRequestDispatcher("receiveBookInfo.jsp").forward(request, response);
						}else{
							conn.rollback();
							request.setAttribute("updateResult", "保存失敗!!");
							request.setAttribute("oprType", "receive");
							request.setAttribute("result", request.getParameter("result"));
							request.setAttribute("payResult", "");
							request.setAttribute("oprStep", "step3");
							request.setAttribute("order", order);
							request.setAttribute("amercePercent", amercePercent);
							request.getRequestDispatcher("receiveBookInfo.jsp").forward(request, response);
						}
					}
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
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
