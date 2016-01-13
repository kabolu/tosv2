package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Period;
import edu.must.tos.impl.PeriodDAOImpl;
import edu.must.tos.impl.Schedule2DAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;

public class ReceiptBookTimeServlet extends HttpServlet {

	public ReceiptBookTimeServlet() {
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
		PrintWriter out = response.getWriter();
		Connection conn = null;
		HttpSession session = request.getSession();
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String type = null;
			if(request.getParameter("type")!=null && !request.getParameter("type").equals("")){
				type = request.getParameter("type");
			}
			PeriodDAOImpl periodImpl = new PeriodDAOImpl();
			SysConfigDAOImpl scImpl = new SysConfigDAOImpl();
			Schedule2DAOImpl scheImpl = new Schedule2DAOImpl();
			String curIntake = (String)session.getAttribute("curIntake");
			String userId = (String)session.getAttribute("userId");
			
			if(type!=null && type.equals("check")){
				String oprType = null;
				if(request.getParameter("oprType")!=null && !request.getParameter("oprType").equals("")){
					oprType = request.getParameter("oprType");
				}
				String addIntake = request.getParameter("intake");
				String date = request.getParameter("date");
				String fromTime = request.getParameter("fromTime");
				String toTime = request.getParameter("toTime");
				String maxNo = request.getParameter("maxNo");
				String periodType = request.getParameter("periodType");
				String fromDate = null;
				String toDate = null;
				String periodNo = request.getParameter("periodNo");
				if(date!=null && !date.equals("")){
					fromDate = date+" "+fromTime+":00";
					toDate = date+" "+toTime+":00";
				}else{
					fromDate = request.getParameter("fromDate")+" "+fromTime+":00";
					toDate = request.getParameter("toDate")+" "+toTime+":00";
				}
				int result = periodImpl.checkReceiptBookTime(conn, addIntake, fromDate, toDate, maxNo, periodType, periodNo, oprType);
				out.print(result);
			}else if(type!=null && type.equals("DateCheck")){
				String checkIntake = request.getParameter("intake");
				String date = request.getParameter("date");
				String fromTime = request.getParameter("fromTime");
				String toTime = request.getParameter("toTime");
				String periodType = request.getParameter("periodType");
				
				String fromDate = null;
				String toDate = null;
				if(date!=null && !date.equals("")){
					fromDate = date+" "+fromTime+":00";
					toDate = date+" "+toTime+":00";
				}else{
					fromDate = request.getParameter("fromDate")+" "+fromTime+":00";
					toDate = request.getParameter("toDate")+" "+toTime+":00";
				}
				
				boolean checkDate = scImpl.checkReceiptTime(conn, checkIntake, fromDate, toDate, periodType);
				if(!checkDate && periodType.equals("R")){
					out.print(1);
				}else if(!checkDate && periodType.equals("P")){
					out.print(2);
				}
			}else if(type!=null && type.equals("search")){
				String searchIntake = request.getParameter("searchIntake");
				if(searchIntake==null || searchIntake.equals("")){
					searchIntake = curIntake;
				}
				String periodType = request.getParameter("periodType");
				List resultList = periodImpl.getSearchPeriod(conn, searchIntake, periodType);
				
				request.setAttribute("type", "searchPage");
				request.setAttribute("resultList", resultList);
				request.setAttribute("searchIntake", searchIntake);
				request.getRequestDispatcher("receiptBookTime.jsp").forward(request, response);
				
			}else if(type!=null && type.equals("checkDel")){
				String periodNo = request.getParameter("periodNo");
				String periodType = null;
				
				boolean flag = scheImpl.checkPeriodNo(conn, periodType, periodNo);
				if(flag){
					out.print(1);
				}
			}else if(type!=null && type.equals("del")){
				String id = request.getParameter("id");
				Period period = periodImpl.getPeriodByNo(conn, Integer.parseInt(id));
				boolean flag = periodImpl.updatePeriodIsN(conn, Integer.parseInt(id), period, userId);
				if(flag){
					conn.commit();
					request.setAttribute("type", "delReceiptBookTime");
					request.setAttribute("msg", "選定的時段資料已被刪除！");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}else{
					conn.rollback();
					request.setAttribute("type", "delReceiptBookTime");
					request.setAttribute("msg", "選定的時段資料刪除失敗！");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}
			}else if(type!=null && type.equals("edit")){
				session.removeAttribute("oldPeriod");
				String id = request.getParameter("id");
				Period period = periodImpl.getPeriodByNo(conn, Integer.parseInt(id));
				session.setAttribute("oldPeriod", period);
				request.setAttribute("period", period);
				request.getRequestDispatcher("receiptBookTimeEdit.jsp").forward(request, response);
			}else if(type!=null && type.equals("checkModify")){
				String periodNo = request.getParameter("periodNo");
				String periodType = request.getParameter("periodType");
				
				boolean flag = scheImpl.checkPeriodNo(conn, periodType, periodNo);
				if(flag){
					out.print(1);
				}
			}
			else if(type!=null && type.equals("checkMaxNo")){
				String periodNo = request.getParameter("periodNo");
				int max = periodImpl.getMaxNo(conn, periodNo);
				out.print(max);
			}
			else if (type != null && type.equals("addReceiptBookTime")){
				addReceiptBookTime(conn, curIntake, userId, request, response);
			}
			else if (type != null && type.equals("editReceiptBookTime") ){
				editReceiptBookTime(conn, curIntake, userId, request, response);
			}
			else{
				List resultList = periodImpl.getAllReceiptBookTime(conn, curIntake);
				request.setAttribute("type", "currPage");
				request.setAttribute("timeList", resultList);
				request.getRequestDispatcher("receiptBookTime.jsp").forward(request, response);
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

	private void editReceiptBookTime(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String modifyIntake = request.getParameter("intake");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String fromTime = request.getParameter("fromTime");
		String toTime = request.getParameter("toTime");
		String maxNo = request.getParameter("maxNo");
		String periodType = request.getParameter("periodType");
		String periodNo = request.getParameter("periodNo");
		String display = request.getParameter("display");
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date from = format.parse(fromDate+" "+fromTime+":00");
		Date to = format.parse(toDate+" "+toTime+":00");
		Period period = new Period();
		period.setPeriodNo(Integer.parseInt(periodNo));
		period.setIntake(modifyIntake);
		period.setType(periodType);
		period.setStartTime(from);
		period.setEndTime(to);
		period.setMaxNo(Integer.parseInt(maxNo));
		period.setActInd(display);
		period.setCreUid(userId);
		period.setUpdUid(userId);
		
		PeriodDAOImpl periodImpl = new PeriodDAOImpl();
		Period oldPeriod = (Period)request.getSession().getAttribute("oldPeriod");
		boolean flag = periodImpl.updateReceiptBookTime(conn, period, oldPeriod);
		
		if(flag){
			conn.commit();
			request.setAttribute("type", "modifyReceiptBookTime");
			request.setAttribute("msg", "時段資料已修改成功！已選取這時段的相關資料，也已作出相關的更新！");
			request.getRequestDispatcher("msg.jsp").forward(request, response);
		}else{
			conn.rollback();
			request.setAttribute("type", "modifyReceiptBookTime");
			request.setAttribute("msg", "時段資料修改不成功！");
			request.getRequestDispatcher("msg.jsp").forward(request, response);
		}
	}

	private void addReceiptBookTime(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String addIntake = request.getParameter("intake");
		String date = request.getParameter("date");
		String fromTime = request.getParameter("fromTime");
		String toTime = request.getParameter("toTime");
		String maxNo = request.getParameter("maxNo");
		String periodType = request.getParameter("periodType");
		String display = request.getParameter("display");
		
		DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date fromDate = format.parse(date+" "+fromTime+":00");
		Date toDate = format.parse(date+" "+toTime+":00");
		Period period = new Period();
		period.setIntake(addIntake);
		period.setType(periodType);
		period.setStartTime(fromDate);
		period.setEndTime(toDate);
		period.setMaxNo(Integer.parseInt(maxNo));
		period.setActInd(display);
		period.setCreUid(userId);
		period.setUpdUid(userId);
		
		PeriodDAOImpl periodImpl = new PeriodDAOImpl();
		int result = periodImpl.addReceiptBookTime(conn, period);
		if(result == 1){
			conn.commit();
			request.setAttribute("type", "addReceiptBookTime");
			request.setAttribute("msg", "新添加時段資料已完成存儲！");
			request.getRequestDispatcher("msg.jsp").forward(request, response);
		}else{
			conn.rollback();
			request.setAttribute("type", "addReceiptBookTime");
			request.setAttribute("msg", "新添加時段資料不能完成存儲！");
			request.getRequestDispatcher("msg.jsp").forward(request, response);
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
