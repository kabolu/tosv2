package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.must.tos.impl.BookDeliverDAOImpl;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.PeriodDAOImpl;

public class CheckSearchServlet extends HttpServlet {

	public CheckSearchServlet() {
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
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Connection conn = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String type = null;
			if(request.getParameter("type") != null && !request.getParameter("type").equals("")){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("SummAndDetail")){
				String intake = "";
				if(request.getParameter("intake") != null && !request.getParameter("intake").equals("")){
					intake = request.getParameter("intake");
				}
				String faculty = "";
				if(request.getParameter("faculty") != null && !request.getParameter("faculty").equals("")){
					faculty = request.getParameter("faculty");
				}
				String program = "";
				if(request.getParameter("program") != null && !request.getParameter("program").equals("")){
					program = request.getParameter("program");
				}
				String academicYear = "";
				if(request.getParameter("year") != null && !request.getParameter("year").equals("")){
					academicYear = request.getParameter("year");
				}
				String studType = "";
				if(request.getParameter("studType") != null && !request.getParameter("studType").equals("")){
					studType = request.getParameter("studType");
				}
				String reportType = "";
				if(request.getParameter("reportType") != null && !request.getParameter("reportType").equals("")){
					reportType = request.getParameter("reportType");
				}
				String fromDate = "";
				if(request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")){
					fromDate = request.getParameter("fromDate")+" 00:00:00";
				}
				String toDate = "";
				if(request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")){
					toDate = request.getParameter("toDate")+" 23:59:59";
				}
				String fromReceiveDate = "";
				if(request.getParameter("fromReceiveDate") != null && !request.getParameter("fromReceiveDate").equals("")){
					fromReceiveDate = request.getParameter("fromReceiveDate")+" 00:00:00";
				}
				String toReceiveDate = "";
				if(request.getParameter("toReceiveDate") != null && !request.getParameter("toReceiveDate").equals("")){
					toReceiveDate = request.getParameter("toReceiveDate")+" 23:59:59";
				}
				//增加訂書日期查詢條件
				String fromOrderDate = "";
				String toOrderDate = "";
				OrderDAOImpl orderImpl = new OrderDAOImpl();
				List resultList = orderImpl.statisticStudOrdInfo(conn, intake, faculty, program, academicYear, studType, reportType, fromDate, toDate, fromReceiveDate, toReceiveDate, fromOrderDate, toOrderDate);
				List recordList = (List)resultList.get(0);
				String sql = (String)resultList.get(1);
				if(recordList.isEmpty()){
					out.print(recordList.size() + "-" + "sql=" + sql);
				}else{
					out.print(recordList.size() + "-" + "sql=" + sql);
				}
			}else if(type != null && type.equals("deliver")){
				String intakeList = "";
				String faculty = "";
				String program = "";
				String academicYear = "";
				String status = "";
				if(request.getParameter("intakeList") != null && !request.getParameter("intakeList").equals("")){
					intakeList = request.getParameter("intakeList");
				}
				if(request.getParameter("faculty") != null && !request.getParameter("faculty").equals("")){
					faculty = request.getParameter("faculty");
				}
				if(request.getParameter("prog") != null && !request.getParameter("prog").equals("")){
					program = request.getParameter("prog");
				}
				if(request.getParameter("academicYear") != null && !request.getParameter("academicYear").equals("")){
					academicYear = request.getParameter("academicYear");
				}
				if(request.getParameter("status") != null && !request.getParameter("status").equals("")){
					status = request.getParameter("status");
				}
				BookDeliverDAOImpl bookDeliverImpl = new BookDeliverDAOImpl();
				List resultList = bookDeliverImpl.getDeliverBookRecord(conn, intakeList, faculty, program, academicYear, status, "", "");
				
				if(resultList.isEmpty()){
					out.print(1);
				}
			}else if(type != null && type.equals("StudReceiveTimeRecord")){
				String intake = null;
				String fromDate = null;
				String toDate = null;
				int year = 0;
				String faculty = null;
				String program = null;
				String periodType = null;
				
				if(request.getParameter("intake") != null && !request.getParameter("intake").equals("")){
					intake = request.getParameter("intake");
				}
				if(request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")){
					fromDate = request.getParameter("fromDate")+" 00:00:00";
				}
				if(request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")){
					toDate = request.getParameter("toDate")+" 23:59:59";
				}
				if(request.getParameter("year") != null && !request.getParameter("year").equals("")){
					year = Integer.parseInt(request.getParameter("year"));
				}
				if(request.getParameter("faculty") != null && !request.getParameter("faculty").equals("")){
					faculty = request.getParameter("faculty");
				}
				if(request.getParameter("program") != null && !request.getParameter("program").equals("")){
					program = request.getParameter("program");
				}
				if(request.getParameter("periodType") != null && !request.getParameter("periodType").equals("")){
					periodType = request.getParameter("periodType");
				}
				
				PeriodDAOImpl periodImpl = new PeriodDAOImpl();
				List recordList = periodImpl.getStatisticStudReceiveTimeRecord(conn, intake, faculty, program, year, fromDate, toDate, periodType);
				if(recordList.isEmpty()){
					out.print(1);
				}
			}else if(type != null && type.equals("UnselectedReceiveTimeRecord")){
				String intake = null;
				int year = 0;
				String faculty = null;
				String program = null;
				String unselperiodType = null;
				if(request.getParameter("intake") != null && !request.getParameter("intake").equals("")){
					intake = request.getParameter("intake");
				}
				if(request.getParameter("year") != null && !request.getParameter("year").equals("")){
					year = Integer.parseInt(request.getParameter("year"));
				}
				if(request.getParameter("faculty") != null && !request.getParameter("faculty").equals("")){
					faculty = request.getParameter("faculty");
				}
				if(request.getParameter("program") != null && !request.getParameter("program").equals("")){
					program = request.getParameter("program");
				}
				if(request.getParameter("unselperiodType") != null && !request.getParameter("unselperiodType").equals("")){
					unselperiodType = request.getParameter("unselperiodType");
				}
				PeriodDAOImpl periodImpl = new PeriodDAOImpl();
				List recordList = periodImpl.getStatisticUnSelReceiveTimeRecord(conn, intake, faculty, program, year, unselperiodType);
				if(recordList.isEmpty()){
					out.print(1);
				}
			}else if(type != null && type.equals("StatisticReceivePeriod")){
				String intake = null;
				String faculty = null;
				String fromDate = null;
				String toDate = null;
				String periodType = null;
				if(request.getParameter("intake") != null && !request.getParameter("intake").equals("")){
					intake = request.getParameter("intake");
				}
				if(request.getParameter("faculty") != null && !request.getParameter("faculty").equals("")){
					faculty = request.getParameter("faculty");
				}
				if(request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")){
					fromDate = request.getParameter("fromDate");
				}
				if(request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")){
					toDate = request.getParameter("toDate");
				}
				if(request.getParameter("periodType") != null && !request.getParameter("periodType").equals("")){
					periodType = request.getParameter("periodType");
				}
				PeriodDAOImpl periodImpl = new PeriodDAOImpl();
				List list = periodImpl.getStatisticReceivePeriod(conn, intake, faculty, fromDate, toDate, periodType);
				if(list == null || list.isEmpty()){
					out.print(1);
				}
			}else if(type != null && type.equals("leavings")){
				String intake = null;
				String fromDate = null;
				String toDate = null;
				String periodType = null;
				if(request.getParameter("intake") != null && !request.getParameter("intake").equals("")){
					intake = request.getParameter("intake");
				}
				if(request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")){
					fromDate = request.getParameter("fromDate");
				}
				if(request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")){
					toDate = request.getParameter("toDate");
				}
				if(request.getParameter("periodType") != null && !request.getParameter("periodType").equals("")){
					periodType = request.getParameter("periodType");
				}
				PeriodDAOImpl periodImpl = new PeriodDAOImpl();
				List list = periodImpl.getReceivePeriodLeavings(conn, intake, fromDate, toDate, periodType);
				if(list == null || list.isEmpty()){
					out.print(1);
				}
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
