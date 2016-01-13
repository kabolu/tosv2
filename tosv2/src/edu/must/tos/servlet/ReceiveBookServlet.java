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

import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.Student;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.bean.Transaction;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.TransactionDAOImpl;

public class ReceiveBookServlet extends HttpServlet {

	public ReceiveBookServlet() {
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
		HttpSession session = request.getSession();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);			
			
			//從session 中取值
			String updUid = (String)session.getAttribute("userId");
			String orderIntake = (String)session.getAttribute("curIntake");
			
			
			//頁面參數
			int orderSeqNo = 0;
			if(request.getParameter("orderSeqNo") != null){
				orderSeqNo = Integer.parseInt(request.getParameter("orderSeqNo"));
			}
			String studentNo = request.getParameter("studentNo");
			String applicantNo = request.getParameter("applicantNo");
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
			
			String retail = null;
			if(request.getParameter("retail") != null){
				retail = request.getParameter("retail");
			}
			
			double shippingFee = 0;
			if(request.getParameter("shippingFee") != null){
				shippingFee = Double.parseDouble(request.getParameter("shippingFee"));
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
				od.setOrderIntake(orderIntake);
				od.setIsbn(isbnValue);
				od.setUpdDate(new Date());
				od.setUpdUid(updUid);
				//由於只有MOP值，只記錄每本圖書的MOP單價即可
				od.setPaidAmount(Double.parseDouble(mopPrice[i]));
				odList.add(od);
			}
			
			/*is not used here
			StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
	        Student stu = new Student();
	        stu.setStudentNo(studentNo);
	        stu.setApplicantNo(applicantNo);
	        boolean isNewStud = studentDAOImpl.isNewStudent(conn, stu);
			*/
			
			SysConfigDAOImpl sysConfigImpl = new SysConfigDAOImpl();			
			//當前學期匯率
			SysConfig curRateConfig = new SysConfig();
			curRateConfig.setScType(orderIntake);
			curRateConfig.setActInd("Y");
			String curInRMBRate = "1";
			String curInHKDRate = "1";
			List<SysConfig> curInRateList = sysConfigImpl.getSysConfigList(conn, curRateConfig);
			for(SysConfig config : curInRateList){
				if(config.getScKey() != null && "RMB".equals(config.getScKey())){
					curInRMBRate = config.getScValue1();
				}else if(config.getScKey() != null && "HKD".equals(config.getScKey())){
					curInHKDRate = config.getScValue1();
				}
			}
			request.setAttribute("curInRMBRate", curInRMBRate);
			request.setAttribute("curInHKDRate", curInHKDRate);
			
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
			SysConfig withdrawForCarry = sysConfigImpl.getSysConfig(conn, config);
			
	        double feePercent = 0;
	        if(withdrawForCarry != null && withdrawForCarry.getScValue1() != null){
	        	feePercent = Double.parseDouble(withdrawForCarry.getScValue1());
	        }
	        
			OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
			Order order = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNo);
			if(order != null && "N".equals(order.getPaidStatus())){
				boolean updateResult = orderDAOImpl.updateOrder(conn, orderSeqNo, orderIntake, paidCurrency, paidAmount, amercePrice, updUid, shippingFee, mopCurrency, mopPaidAmount, curRate) ;
				
				//更新購買時圖書的單價
				OrderDetailDAOImpl odImpl = new OrderDetailDAOImpl();
				boolean updateBookPrice = odImpl.updateOdBookPrice(conn, odList);
				
				Transaction t = new Transaction();
				t.setTransactionNo(0);
				t.setOrderSeqNo(String.valueOf(orderSeqNo));
				t.setCashier(updUid);
				t.setPaidDate(new Date());
				t.setPaidMentType("收費");
				t.setPay(paidAmount);
				t.setPaidCurrency(paidCurrency);
				t.setRemarks("");
				if(studentNo != null && !"null".equals(studentNo)){
					t.setStudentNo(studentNo);
				} else {
					t.setStudentNo(applicantNo);
				}
				t.setStatus("Y");
				TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
				boolean transactionFlag = transactionDAOImpl.addTransactionInfo(conn, t);				
				if(updateResult && updateBookPrice && transactionFlag){
					conn.commit();
					order = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNo);
					String payResult = "繳費成功!";
					request.setAttribute("payResult", payResult);
					request.setAttribute("order", order);
					request.setAttribute("paidCurrency", order.getPaidCurrency());
					request.setAttribute("oprStep", "step2");				
				} else {
					conn.rollback();
				}				
			} else {
				String payResult = "此訂單已繳費成功!";
				request.setAttribute("payResult", payResult);
				request.setAttribute("order", order);
				request.setAttribute("paidCurrency", order.getPaidCurrency());
				request.setAttribute("oprStep", "step2");
			}
			request.setAttribute("feePercent", feePercent);
			request.setAttribute("amercePercent", 0);
			request.setAttribute("shippingFee", (shippingFee*100));
			if(retail != null && retail.equals("retail")){
				request.setAttribute("retail", "retail");
				request.setAttribute("orderSeqNo", orderSeqNo);
				request.getRequestDispatcher("retailBookList.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("receiveBookList.jsp").forward(request, response);
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
