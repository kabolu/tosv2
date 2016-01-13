package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.util.Authenticate;

import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.SysUserDAOImpl;

public class LoginServlet extends HttpServlet {

	public LoginServlet() {
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
			HttpSession session = request.getSession();
	    
			String psUserId = request.getParameter("psUserId");
			String psPassWord = request.getParameter("psPassWord");
			
			SysUserDAOImpl sysUserDAOImpl = new SysUserDAOImpl();
			Authenticate auth = new Authenticate();
			if(auth.validate(psUserId, psPassWord, this.getServletContext())) {
				boolean loginResult = sysUserDAOImpl.checkLogin(conn, psUserId);
				if(loginResult){
					SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
					String key = "CURRINTAKE";
					String intake = sysConfigDAOImpl.getCurIntake(conn, key);
					
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Date today = new Date();
					//退書期
					SysConfig withdraw = new SysConfig();
					withdraw.setScType("WITHDRAW");
					withdraw.setScKey(intake);
					withdraw.setActInd("Y");
					SysConfig withdrawPeriod = sysConfigDAOImpl.getSysConfig(conn, withdraw);
					String WITHDRAW = "N";
					if(withdrawPeriod != null && withdrawPeriod.getScValue1() != null){
						if(today.after(sf.parse(withdrawPeriod.getScValue1()))){
							WITHDRAW = "Y";
						}
					}
					session.setAttribute("WITHDRAW", WITHDRAW);
					//逾期付款罰款期
					SysConfig paid2 = new SysConfig();
					paid2.setScType("PAID2");
					paid2.setScKey(intake);
					paid2.setActInd("Y");
					SysConfig paid2Period = sysConfigDAOImpl.getSysConfig(conn, paid2);
					String PAID2 = "N";
					if(paid2Period != null && paid2Period.getScValue1()!= null){
						if(today.after(sf.parse(paid2Period.getScValue1()))){
							PAID2 = "Y";
						}
					}
					session.setAttribute("PAID2", PAID2);
					//退運期
					SysConfig carry = new SysConfig();
					carry.setScType("FINERATE");
					carry.setScKey("SHIPPING");
					carry.setActInd("Y");
					SysConfig carryPeriod = sysConfigDAOImpl.getSysConfig(conn, carry);
					String CARRY = "N";
					if(carryPeriod != null && carryPeriod.getScValue2() != null){
						if(today.after(sf.parse(carryPeriod.getScValue2()))){
							CARRY = "Y";
						}
					}
					session.setAttribute("CARRY", CARRY);
					//保管期
					SysConfig keepiing = new SysConfig();
					keepiing.setScType("FINERATE");
					keepiing.setScKey("LATERECEIVE");
					keepiing.setActInd("Y");
					SysConfig keepingPeriod = sysConfigDAOImpl.getSysConfig(conn, keepiing);
					String KEEPING = "N";
					if(keepingPeriod != null && keepingPeriod.getScValue3() != null){
						if(today.after(sf.parse(keepingPeriod.getScValue3()))){
							KEEPING = "Y";
						}
					}
					session.setAttribute("KEEPING", KEEPING);
					session.setAttribute("keepingPeriod", keepingPeriod);
					
					session.setAttribute("userId", psUserId);
					session.setAttribute("curIntake", intake);
					request.getRequestDispatcher("bottom.jsp").forward(request, response);
				} else {
					request.setAttribute("error", "用戶ID沒有訪問權限,請重新輸入!");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				}
			} else {
				request.setAttribute("error", "用戶ID或密碼不存在,請重新輸入!");
				request.getRequestDispatcher("login.jsp").forward(request, response);
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

	public void init() throws ServletException {
		// Put your code here
	}

}
