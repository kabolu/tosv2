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
import javax.sql.DataSource;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.Price;
import edu.must.tos.bean.Student;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.bean.Transaction;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StudentBookDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.TransactionDAOImpl;

public class EditCurrencyServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public EditCurrencyServlet() {
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
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Connection conn = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String curIntake = request.getSession().getAttribute("curIntake").toString();
			String userId = request.getSession().getAttribute("userId").toString();
			
			OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
			StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			StudentBookDAOImpl  studentBookDAOImpl = new StudentBookDAOImpl();
			//當前學期匯率
			SysConfig curRateConfig = new SysConfig();
			curRateConfig.setScType(curIntake);
			curRateConfig.setActInd("Y");
			String curInRMBRate = "1";
			String curInHKDRate = "1";
			List<SysConfig> curInRateList = sysConfigDAOImpl.getSysConfigList(conn, curRateConfig);
			for(SysConfig config : curInRateList){
				if(config.getScKey() != null && "RMB".equals(config.getScKey())){
					curInRMBRate = config.getScValue1();
				}else if(config.getScKey() != null && "HKD".equals(config.getScKey())){
					curInHKDRate = config.getScValue1();
				}
			}
			request.setAttribute("curInRMBRate", curInRMBRate);
			request.setAttribute("curInHKDRate", curInHKDRate);
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			
			if(type != null && "SEARCH".equals(type)){
				String orderSeqNo = request.getParameter("orderSeqNo");
				
				double shippingFee = 0;
	        	SysConfig shippingFeeConfig = new SysConfig();
	        	shippingFeeConfig.setScType("SHIPPINGFEE");
	        	shippingFeeConfig.setScKey("FEE");
	        	shippingFeeConfig.setActInd("Y");
	        	SysConfig conf = sysConfigDAOImpl.getSysConfig(conn, shippingFeeConfig);
	        	if(conf != null && conf.getScValue1() != null){
	        		shippingFee = Double.parseDouble(conf.getScValue1());
	        	}
				
	        	List orderedList = new ArrayList();
	        	double feePercent = 0;
	        	double amerceMount = 0;
	        	float amercePercent = 0;
	        	
	        	Order order = null;
	        	if(orderSeqNo != null && !"".equals(orderSeqNo)){
	        		order = orderDAOImpl.getOrderBySeqNo(conn, Integer.parseInt(orderSeqNo));
		        	if(order != null){
		        		Student stu = new Student();
				        stu.setStudentNo(order.getStudentNo());
				        boolean isNewStud = studentDAOImpl.isNewStudent(conn, stu);			        
				        
						String paid2 = request.getSession().getAttribute("PAID2").toString();
						if(paid2 != null && "Y".equals(paid2)  && !isNewStud){
							SysConfig sc = new SysConfig();
							sc.setScType("FINERATE");
							sc.setScKey("LATEPAY");
							sc.setActInd("Y");
							SysConfig paid2Period = sysConfigDAOImpl.getSysConfig(conn, sc);
							if(paid2Period != null && paid2Period.getScValue1() != null){
								amercePercent = Float.parseFloat(paid2Period.getScValue1());
							}
				        }
						
						String paidStatus = "Y";
						String value = order.getStudentNo() + "," + order.getStudentNo();
						int orderSeqNoParam = 0;
						List orderedBookList = new ArrayList();
						if("RETAIL".equals(order.getStudentNo())){
							value = ",";
							orderSeqNoParam = order.getOrderSeqNo();
						}
						orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, curIntake, paidStatus, orderSeqNoParam);
						orderedList = orderedBookList;
						
						SysConfig config = new SysConfig();
						config.setScType("FINERATE");
						config.setScKey("SHIPPING");
						config.setActInd("Y");
						SysConfig withdrawForCarry = sysConfigDAOImpl.getSysConfig(conn, config);
						
						if(withdrawForCarry != null && withdrawForCarry.getScValue1() != null){
							feePercent = Double.parseDouble(withdrawForCarry.getScValue1());
						}
		        	}
	        	}	        	
		        
				request.setAttribute("amerceMount", amerceMount);
				request.setAttribute("orderedBookList", orderedList);
				request.setAttribute("order", order);
				request.setAttribute("amercePercent", amercePercent);
				request.setAttribute("feePercent", feePercent);
				request.setAttribute("shippingFee", shippingFee);
				
				request.getRequestDispatcher("editPaidCurrency.jsp").forward(request, response);
			} else if (type != null && "CHANGE".equals(type)){
				//頁面參數
				int orderSeqNo = 0;
				if(request.getParameter("orderSeqNo") != null){
					orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
				}
				
				double shippingFee = 0;
				if(request.getParameter("shippingFee") != null){
					shippingFee = Double.parseDouble(request.getParameter("shippingFee"));
				}
				
				//所選擇的幣種
				String paidCurrency = request.getParameter("paidCurrency");
				float paidAmount = 0;
				
				//葡幣價
				String mopCurrency = "MOP";
				
				float mopPaidAmount = 0;			
				if(paidCurrency != null && paidCurrency.equals("MOP")) {
					paidAmount = Float.parseFloat(request.getParameter("totalMop"));
					mopPaidAmount = paidAmount;
				}
				else if (paidCurrency != null && paidCurrency.equals("RMB")) {
					paidAmount = Float.parseFloat(request.getParameter("totalRmb"));
					mopPaidAmount = Float.parseFloat(request.getParameter("totalMop"));				
				}
				else if(paidCurrency != null && paidCurrency.equals("HKD")){
					paidAmount = Float.parseFloat(request.getParameter("totalHkd"));
					mopPaidAmount = Float.parseFloat(request.getParameter("totalMop"));
				}
				
				float amercePrice = 0;
				if(request.getParameter("amerceMop") != null){
					amercePrice = Float.parseFloat(request.getParameter("amerceMop"));
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
					//由於只有MOP值，只記錄每本圖書的MOP單價即可
					od.setPaidAmount(Double.parseDouble(mopPrice[i]));
					odList.add(od);
				}
				
				Double curRate = new Double(1);
				if("RMB".equals(paidCurrency)){
					curRate = Double.parseDouble(curInRMBRate);
				} else if ("HKD".equals(paidCurrency)){
					curRate = Double.parseDouble(curInHKDRate);
				}
				
				SysConfig config = new SysConfig();
				config.setScType("FINERATE");
				config.setScKey("SHIPPING");
				config.setActInd("Y");
				SysConfig withdrawForCarry = sysConfigDAOImpl.getSysConfig(conn, config);
				
		        double feePercent = 0;
		        if(withdrawForCarry != null && withdrawForCarry.getScValue1() != null){
		        	feePercent = Double.parseDouble(withdrawForCarry.getScValue1());
		        }
		        	
				boolean updateResult = orderDAOImpl.updateOrder(conn, orderSeqNo, curIntake, paidCurrency, paidAmount, amercePrice, userId, shippingFee, mopCurrency, mopPaidAmount, curRate) ;
				
				//更新購買時圖書的單價
				OrderDetailDAOImpl odImpl = new OrderDetailDAOImpl();
				boolean updateBookPrice = odImpl.updateOdBookPrice(conn, odList);
				
				Transaction t = new Transaction();
				t.setTransactionNo(0);
				t.setOrderSeqNo(String.valueOf(orderSeqNo));
				t.setCashier(userId);
				t.setPaidDate(new Date());
				t.setPaidMentType("收費");
				t.setPay(paidAmount);
				t.setPaidCurrency(paidCurrency);
				t.setRemarks("");
				t.setStatus("Y");
				TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
				boolean transactionFlag = transactionDAOImpl.updRecdByOrderSeqNo(conn, t);
				
				if(updateResult && updateBookPrice && transactionFlag){
					conn.commit();
					Order order = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNo);
					String paidStatus = "Y";
					String value = order.getStudentNo() + "," + order.getStudentNo();
					int orderSeqNoParam = 0;
					if("RETAIL".equals(order.getStudentNo())){
						value = ",";
						orderSeqNoParam = orderSeqNo;
					}
					List orderedBookList = studentBookDAOImpl.searchOrderedBook(conn, value, curIntake, paidStatus, orderSeqNoParam);
					request.setAttribute("orderedBookList", orderedBookList);
					
					String payResult = "更改成功!";
					request.setAttribute("payResult", payResult);
					request.setAttribute("order", order);
					request.setAttribute("paidCurrency", order.getPaidCurrency());				
				} else {
					conn.rollback();
				}
				
				request.setAttribute("feePercent", feePercent);
				request.setAttribute("amercePercent", 0);
				request.setAttribute("shippingFee", (shippingFee * 100));
				request.getRequestDispatcher("editPaidCurrency.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("editPaidCurrency.jsp").forward(request, response);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
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
