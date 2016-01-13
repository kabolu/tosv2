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
import edu.must.tos.bean.BookPurchasingBean;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookPurchasingDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.util.ToolsOfString;

public class ChangeBookServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ChangeBookServlet() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		try {
			// 使用連接池獲取連接
			DataSource ds = (DataSource) getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);

			String userId = request.getSession().getAttribute("userId").toString();
			String curIntake = request.getSession().getAttribute("curIntake").toString();

			String isbn = null;
			if (request.getParameter("isbn") != null && !request.getParameter("isbn").equals("")) {
				isbn = request.getParameter("isbn").trim();
			}
			
			String fromDate = null;
			if (request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")) {
				fromDate = request.getParameter("fromDate");
			}
			
			String toDate = null;
			if (request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")) {
				toDate = request.getParameter("toDate");
			}

			BookDAOImpl bookDAOImpl = new BookDAOImpl();
			OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
			BookPurchasingDAOImpl bookPurchasingDAOImpl = new BookPurchasingDAOImpl();

			String type = null;
			if (request.getParameter("type") != null && !request.getParameter("type").equals("")) {
				type = request.getParameter("type");
			}
			// add
			String changetype = null;
			changetype = request.getParameter("changetype");
			
			// add
			if (type != null && type.equals("changeBook")) {
				List list = null;
				if (request.getSession().getAttribute("list") != null) {
					list = (List) request.getSession().getAttribute("list");
				}
				String sId = request.getParameter("sId");
				String[] id = sId.split(";");
				
				String remarks = request.getParameter("remarks");
				if(!ToolsOfString.isNull(remarks)){
					remarks = new String(remarks.getBytes("ISO-8859-1"), "UTF-8");  
				}
				if (changetype.equals("S")) {
					int confrimQty = 0;
					//該Isbn值為要更新的Isbn
					//List<OrDetail> changeIsbnlist = orderDetailDAOImpl.getChangeBookList(conn, curIntake, isbn, confrimQty);
					List<OrDetail> changeIsbnlist = orderDetailDAOImpl.getChangeNoActindBookList(conn, curIntake, isbn, confrimQty);
					List<OrDetail> updateList = new ArrayList();
					List<OrDetail> unUpdateList = new ArrayList<OrDetail>();
					boolean updateFlag = true;
									
					if (list != null && !list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							for(int j=0; j < id.length; j++){
								Integer doId = Integer.parseInt(id[j]);
								if(doId == i){
									OrDetail od = (OrDetail) list.get(i);
									boolean flag = true;
									if (changeIsbnlist != null && !changeIsbnlist.isEmpty()) {
										for (OrDetail orDetail : changeIsbnlist) {
											//若同一個訂單內含有要更換的isbn就不再更新 remove  by xfke
											/*if (od.getOrderSeqNo() == orDetail .getOrderSeqNo()) {
												unUpdateList.add(orDetail);
												flag = false;
												break;
											}*/
											
											//若同一個訂單內含有要更換的ISBN，則被更換的ISBN改為 actind='N'，要更換的ISBN改為actind='Y'
											if (od.getOrderSeqNo() == orDetail .getOrderSeqNo()) {
												od.setActInd("N");
												od.setUpdUid(userId);
												orDetail.setActInd("Y");
												orDetail.setUpdUid(userId);
												if(orderDetailDAOImpl.updateOrderDetailInfo(conn, od)
														&& orderDetailDAOImpl.updateOrderDetailInfo(conn, orDetail)){
													
													updateFlag = true;
													//并不是真正的置換，只是修改兩個記錄的actind
													unUpdateList.add(orDetail);
												} else {
													updateFlag=false;
												}
												flag = false;
												break;
											}
										}
									}
									if (flag) {
										od.setUpdDate(new Date());
										od.setUpdUid(userId);
										od.setActInd("Y");
										updateList.add(od);
									}
								}
							}
						}
					}
					request.setAttribute("unUpdateList", unUpdateList);
					if(updateList != null && !updateList.isEmpty()){
						for (OrDetail od : updateList) {
							try {
								if (orderDetailDAOImpl.changeOrDetailIsbn(conn, od, isbn, remarks) == false) {
									updateFlag = false;
									break;
								}
							} catch (Exception e) {
								conn.rollback();
								request.setAttribute("msg", e.getMessage());
								request.setAttribute("type", "changeBook");
								request.setAttribute("changetype", changetype);
								request.getRequestDispatcher("msg.jsp").forward(request, response);
							}
						}						
					}else if(unUpdateList == null || unUpdateList.isEmpty()){
						request.setAttribute("msg", "沒有可以置換的圖書記錄！");
						request.setAttribute("type", "changeBook");
						request.setAttribute("changetype", changetype);
						request.getRequestDispatcher("msg.jsp").forward(request,response);
					}
					
					if (updateFlag) {
						conn.commit();
						request.setAttribute("msg", "圖書置換成功！");
						request.setAttribute("type", "changeBook");
						request.setAttribute("changetype", changetype);
						request.getRequestDispatcher("msg.jsp").forward(request, response);
					} else {
						conn.rollback();
						request.setAttribute("msg", "圖書置換失敗！");
						request.setAttribute("type", "changeBook");
						request.setAttribute("changetype", changetype);
						request.getRequestDispatcher("msg.jsp").forward(request, response);
					}
				}else if(changetype.equals("P")){
					//List<BookPurchasingBean> changeIsbnlist = bookPurchasingDAOImpl.getBookPurchasingBeanByIsbn(conn, isbn, curIntake, null, null, true);
					List<BookPurchasingBean> updateList = new ArrayList<BookPurchasingBean>();
					List<BookPurchasingBean> unUpdateList = new ArrayList<BookPurchasingBean>();
									
					if (list != null && !list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							for(int j = 0; j < id.length; j++){
								Integer doId = Integer.parseInt(id[j]);
								if(doId == i){
									BookPurchasingBean od = (BookPurchasingBean) list.get(i);
									if(od.getBookPurchase().getQuantity() == od.getBookPurchase().getLeave()){
										//NEW ISBN
										od.getBook().setIsbn(isbn);
										od.setChangeRemarks(remarks);
										od.getBookPurchase().setUpdDate(new Date());
										od.getBookPurchase().setUpdUid(userId);
										updateList.add(od);
									} else {
										unUpdateList.add(od);
									}
								}
							}
						}
					}
					request.setAttribute("unUpdateList", unUpdateList);
					if(updateList != null && !updateList.isEmpty()){
						boolean updateFlag = true;
						for (BookPurchasingBean bPurchasingBean : updateList) {
							try {
								if (bookPurchasingDAOImpl.changeIsbnList(conn, bPurchasingBean) == false){
									updateFlag = false;
									break;
								}
							} catch (Exception e) {
								conn.rollback();
								request.setAttribute("msg", e.getMessage());
								request.setAttribute("type", "changeBook");
								request.setAttribute("changetype", changetype);
								request.getRequestDispatcher("msg.jsp").forward(request, response);
							}
						}
						if (updateFlag) {
							conn.commit();
							request.setAttribute("msg", "圖書置換成功！");
							request.setAttribute("type", "changeBook");
							request.setAttribute("changetype", changetype);
							request.getRequestDispatcher("msg.jsp").forward(request, response);
						} else {
							conn.rollback();
							request.setAttribute("msg", "圖書置換失敗！");
							request.setAttribute("type", "changeBook");
							request.setAttribute("changetype", changetype);
							request.getRequestDispatcher("msg.jsp").forward(request, response);
						}
					}else{
						request.setAttribute("msg", "沒有可以置換的圖書記錄！");
						request.setAttribute("type", "changeBook");
						request.setAttribute("changetype", changetype);
						request.getRequestDispatcher("msg.jsp").forward(request,response);
					}
				}
			} else if (type != null && type.equals("checkChangeIsbn")) {
				Book book = bookDAOImpl.getBookByPK(conn, isbn);
				if (book != null && book.getIsbn() != null) {
					out.print("Y");
				} else {
					out.print("N");
				}
			} else {
				request.getSession().removeAttribute("list");
				int confrimQty = 1;

				// add rainbow
				request.setAttribute("changetype", changetype);
				if (changetype.equals("S")) {
					List list = orderDetailDAOImpl.getChangeBookList(conn, curIntake, isbn, fromDate, toDate, confrimQty);
					request.setAttribute("list", list);
					request.getSession().setAttribute("list", list);
				} else {
					List list = bookPurchasingDAOImpl.getBookPurchasingBeanByIsbn(conn, isbn, curIntake, fromDate, toDate, true);
					request.setAttribute("list", list);
					request.getSession().setAttribute("list", list);
				}
				// add rainbow
				request.setAttribute("isbn", isbn);
				request.setAttribute("fromDate", fromDate);
				request.setAttribute("toDate", toDate);
				request.getRequestDispatcher("changeBookResult.jsp").forward(request, response);
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			// throw new ServletException (e);
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
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
