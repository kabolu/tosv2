package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
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

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import edu.must.tos.bean.BookDeliver;
import edu.must.tos.bean.DeliverSummary;
import edu.must.tos.bean.Faculty;
import edu.must.tos.bean.ViewStudOrdInfo;
import edu.must.tos.impl.BookDeliverDAOImpl;
import edu.must.tos.impl.FacultyDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.util.CellFormat;

public class StatisticDeliverBookServlet extends HttpServlet {

	public StatisticDeliverBookServlet() {
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
			
			String curIntake = (String)request.getSession().getAttribute("curIntake");
			
			OrderDetailDAOImpl orderDetailImpl = new OrderDetailDAOImpl();
			BookDeliverDAOImpl bookDeliverDAOImpl = new BookDeliverDAOImpl();
			
			String type = null;
			if (request.getParameter("type") != null) {
				type = request.getParameter("type");
			}
			if (type != null && !"".equals(type)) {
				String reportType = null;
				if (request.getParameter("reportType") != null) {
					reportType = request.getParameter("reportType");
				}
				WritableCellFormat cellFormat = CellFormat.getCellFormat();
				if (reportType != null && "C".equals(reportType)){
					List list = bookDeliverDAOImpl.getBookDeliverList(conn, curIntake);
					if (list != null && !list.isEmpty()) {
						response.reset();
						response.setContentType("application/octet-stream");
						response.addHeader("Content-Disposition" , "attachment ; filename = BookDeliverList.xls " );
						
						OutputStream os = response.getOutputStream();
					    WritableWorkbook workbook = Workbook.createWorkbook(os);
						WritableSheet worksheet = workbook.createSheet("BookDeliverReport_"+reportType, 0);
						String[] titleName = {"派書日期", "訂單序號", "學生編號", "中文姓名", "英文姓名", "已派總冊數", "派書人"};
						for (int i=0; i<titleName.length; i++) {
							worksheet.addCell(new Label(i, 0, titleName[i], cellFormat));
							worksheet.setColumnView(i, titleName[i].getBytes().length+2);
						}
						for (int i=0; i<list.size(); i++) {
					    	int j = 0;
					    	BookDeliver bd = (BookDeliver)list.get(i);
					    	worksheet.addCell(new Label(j++, i+1, bd.getDeliverDate()));
					    	worksheet.addCell(new Number(j++, i+1, bd.getOrderSeqNo()));
					    	worksheet.addCell(new Label(j++, i+1, bd.getStudentNo()));
					    	worksheet.addCell(new Label(j++, i+1, bd.getChineseName()));
					    	worksheet.addCell(new Label(j++, i+1, bd.getEnglishName()));
					    	worksheet.addCell(new Number(j++, i+1, bd.getQuantity()));
					    	worksheet.addCell(new Label(j++, i+1, bd.getCreUid()));
					    }
					    workbook.write();
						workbook.close();
					    
						os.flush();
						os.close();
					} else {
						FacultyDAOImpl fac = new FacultyDAOImpl();
						List facultyList = fac.searchFaculty(conn, new Faculty());
						SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
						List intakeParams = sysConfigDAOImpl.getSysIntake(conn);
						request.setAttribute("intake", (String)request.getSession().getAttribute("curIntake"));
						request.setAttribute("facultyList", facultyList);
						request.setAttribute("intakeList", intakeParams);
						request.setAttribute("error", "error");
						request.getRequestDispatcher("statisticDeliverBook.jsp").forward(request, response);
					}
				} else {
					List list = bookDeliverDAOImpl.getDeliverReportList(conn, curIntake, reportType);
					if (list != null && !list.isEmpty()) {
						response.reset();
						response.setContentType("application/octet-stream");
						response.addHeader("Content-Disposition" , "attachment ; filename = BookDeliverList.xls " );
						
						OutputStream os = response.getOutputStream();
					    WritableWorkbook workbook = Workbook.createWorkbook(os);
						WritableSheet worksheet = workbook.createSheet("BookDeliverReport_"+reportType, 0);
						String[] titleName = {"日期", "派書員", "課程", "課程名稱", "學院編號", "單數"};
						
					    for (int j=0, i=0; i<titleName.length; i++) {
					    	if (reportType != null && "A".equals(reportType) && i==3) {
					    		continue;
					    	}
					    	if (reportType != null && "B".equals(reportType) && i==1) {
					    		continue;
					    	}
					    	j++;
					    	worksheet.addCell(new Label(j-1, 0, titleName[i], cellFormat));
					    	worksheet.setColumnView(i, titleName[i].getBytes().length + 4);
						}
					    for (int i=0; i<list.size(); i++) {
					    	int j = 0;
					    	DeliverSummary dSummary = (DeliverSummary)list.get(i);
					    	worksheet.addCell(new Label(j++, i+1, dSummary.getDate()));
					    	if (reportType != null && "A".equals(reportType)) {
					    		worksheet.addCell(new Label(j++, i+1, dSummary.getUpduid()));
					    	}
					    	worksheet.addCell(new Label(j++, i+1, dSummary.getProgramCode()));
					    	if (reportType != null && "B".equals(reportType)) {
					    		worksheet.addCell(new Label(j++, i+1, dSummary.getProgramName()));
					    	}
					    	worksheet.addCell(new Label(j++, i+1, dSummary.getFacultyCode()));
					    	worksheet.addCell(new Number(j++, i+1, dSummary.getOrderCount()));
					    }
					    workbook.write();
						workbook.close();
					    
						os.flush();
						os.close();
					} else {
						FacultyDAOImpl fac = new FacultyDAOImpl();
						List facultyList = fac.searchFaculty(conn, new Faculty());
						SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
						List intakeParams = sysConfigDAOImpl.getSysIntake(conn);
						request.setAttribute("intake", (String)request.getSession().getAttribute("curIntake"));
						request.setAttribute("facultyList", facultyList);
						request.setAttribute("intakeList", intakeParams);
						request.setAttribute("error", "error");
						request.getRequestDispatcher("statisticDeliverBook.jsp").forward(request, response);
					}
				}
			} else {
				String intakeList = "";
				String faculty = "";
				String program = "";
				String academicYear = "";
				String status = "";
				String fromDate = "";
				String toDate = "";
				if (request.getParameter("intakeList") != null && !request.getParameter("intakeList").equals("")) {
					intakeList = request.getParameter("intakeList");
				}
				if (request.getParameter("faculty") != null && !request.getParameter("faculty").equals("")) {
					faculty = request.getParameter("faculty");
				}
				if (request.getParameter("prog") != null && !request.getParameter("prog").equals("")) {
					program = request.getParameter("prog");
				}
				if (request.getParameter("academicYear") != null && !request.getParameter("academicYear").equals("")) {
					academicYear = request.getParameter("academicYear");
				}
				if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
					status = request.getParameter("status");
				}
				if (request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")) {
					fromDate = request.getParameter("fromDate") + " 00:00:00";
				}
				if (request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")) {
					toDate = request.getParameter("toDate") + " 23:59:59";
				}
				List resultList = new ArrayList();
				if("1".equals(status) || "2".equals(status))
					resultList = bookDeliverDAOImpl.getDeliverBookRecord(conn, intakeList, faculty, program, academicYear, status, fromDate, toDate);
				else if ("3".equals(status))
					resultList = bookDeliverDAOImpl.getDeliverBookRecord3(conn, intakeList, faculty, program, academicYear, status, fromDate, toDate);
				else
					resultList = bookDeliverDAOImpl.getDeliverBookRecord(conn, intakeList, faculty, program, academicYear, fromDate, toDate);
				if (resultList != null && !resultList.isEmpty()) {
					response.reset();
					response.setContentType("application/octet-stream");
					response.addHeader("Content-Disposition" , "attachment ; filename = BookDeliverList.xls " );
					
					OutputStream os = response.getOutputStream();
				    WritableWorkbook workbook = Workbook.createWorkbook(os);
					WritableSheet worksheet = workbook.createSheet("BookDeliverList", 0);
					WritableCellFormat cellFormat = CellFormat.getCellFormat();
					
					String[] titleName = {
							"學員姓名",	"學員編號",	"學院",		"年級",			"課程",			"訂書序號",		"書本名稱",			"作者姓名",	"出版社",	"版次",	"出版年", 
							"ISBN",		"訂購冊數",	"領書冊數",	"缺書冊數",	"預估金額(MOP)",	"預估金額(RMB)",	"實繳金額(MOP)",	"實繳金額(RMB)",		"付款幣種",	"實繳金額",	"收款員",
							"收款日期",	"領書日期",	"派書員",	"聯絡電話",		"Email",		"在讀狀態",		"聯絡電話（更新）",	"Email（更新）"
							};
					
				    for(int j=0, i=0; i<titleName.length; i++){
				    	if (status.equals("1") && (i==14 || i==25 || i==26 || i==27 || i==29)) {
				    		continue;
				    	} else if (status.equals("2") && (i==14 || i==23 || i==24)) {
				    		continue;
				    	} else if (status.equals("3") && i==27) {
				    		continue;
				    	} else if (status.equals("4") 
				    			&& (i==6 || i==7 || i==8 || i==9 || i==10 || i==11 || i==15 || i==16 || i==17 || i==18 || i==19 || i==20 || i==23 || i==24) ) {
				    		continue;
				    	}
				    	j++;
				    	worksheet.addCell(new Label(j-1, 0, titleName[i], cellFormat));
				    	worksheet.setColumnView(i, titleName[i].getBytes().length+2);
					}
				    DateFormat customDateFormat = new DateFormat ("yyyy-MM-dd hh:mm:ss"); 
					WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat); 
				    for(int i=0; i<resultList.size(); i++){
				    	int j=0;
				    	ViewStudOrdInfo info = new ViewStudOrdInfo();
				    	info = (ViewStudOrdInfo)resultList.get(i);
				    	
				    	worksheet.addCell(new Label(j++, i+1, info.getChineseName()));
				    	worksheet.addCell(new Label(j++, i+1, info.getStudentNo()));
				    	worksheet.addCell(new Label(j++, i+1, info.getFacultyName()));
				    	
				    	if (status != null && status.equals("4")) {
				    		worksheet.addCell(new jxl.write.Number(j++, i+1, info.getAcademicYear()));
				    		worksheet.addCell(new Label(j++, i+1, info.getProgram()));
				    		worksheet.addCell(new Label(j++, i+1, info.getOrderSeqNo()));
				    		worksheet.addCell(new jxl.write.Number(j++, i+1, info.getOrderEdQty()));
				    		worksheet.addCell(new jxl.write.Number(j++, i+1, info.getReceivedQty()));
				    		worksheet.addCell(new jxl.write.Number(j++, i+1, info.getNotEnoughQty()));
				    		worksheet.addCell(new Label(j++, i+1, info.getCashier()));
					    	if (info.getPaidDate() == null) {
					    		worksheet.addCell(new Label(j++, i+1, "")); //沒有時間記錄
					    	} else {
					    		worksheet.addCell(new DateTime(j++, i+1, (Date)info.getPaidDate(), dateFormat));
					    	}
					    	worksheet.addCell(new Label(j++, i+1, info.getContactNo()));
				    		worksheet.addCell(new Label(j++, i+1, info.getEmail()));
				    		worksheet.addCell(new Label(j++, i+1, info.getStatus()));
				    		worksheet.addCell(new Label(j++, i+1, info.getNewContactNo()));
				    		worksheet.addCell(new Label(j++, i+1, info.getNewEmail()));
				    	} else {
				    		worksheet.addCell(new jxl.write.Number(j++, i+1, info.getAcademicYear()));
					    	worksheet.addCell(new Label(j++, i+1, info.getProgram()));
					    	worksheet.addCell(new Label(j++, i+1, info.getOrderSeqNo()));
					    	worksheet.addCell(new Label(j++, i+1, info.getTitle()));
					    	worksheet.addCell(new Label(j++, i+1, info.getAuthor()));
					    	worksheet.addCell(new Label(j++, i+1, info.getPublisher()));
					    	worksheet.addCell(new Label(j++, i+1, info.getEdition()));
					    	worksheet.addCell(new Label(j++, i+1, info.getPublishYear()));
					    	worksheet.addCell(new Label(j++, i+1, info.getIsbn()));
					    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getOrderEdQty()));
					    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getReceivedQty()));
					    	if (status != null && status.equals("3")) {
					    		worksheet.addCell(new jxl.write.Number(j++, i+1, info.getNotEnoughQty()));
					    	}
					    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getFuturePriceMOP()));
					    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getFuturePriceRMB()));
					    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getNetPriceMOP()));
					    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getNetPriceRMB()));
					    	worksheet.addCell(new Label(j++, i+1, info.getPaidCurrency()));
					    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getPaidAmount()));
					    	worksheet.addCell(new Label(j++, i+1, info.getCashier()));
					    	if (info.getPaidDate() == null) {
					    		worksheet.addCell(new Label(j++, i+1, "")); //沒有時間記錄
					    	} else {
					    		worksheet.addCell(new DateTime(j++, i+1, (Date)info.getPaidDate(), dateFormat));
					    	}			    	
					    	if (status != null && !status.equals("2")) {
					    		if (info.getBookDeliverDate() == null) {
					    			worksheet.addCell(new Label(j++, i+1, "")); //沒有時間記錄
					    		} else {
					    			worksheet.addCell(new DateTime(j++, i+1, (Date)info.getBookDeliverDate(), dateFormat));
					    		}
					    		worksheet.addCell(new Label(j++, i+1, info.getBookDeliver()));
					    	}
					    	if (status != null && !status.equals("1")) {
					    		worksheet.addCell(new Label(j++, i+1, info.getContactNo()));
					    		worksheet.addCell(new Label(j++, i+1, info.getEmail()));
					    	}
					    	if (status != null && !status.equals("3")) {
					    		worksheet.addCell(new Label(j++, i+1, info.getStatus()));
					    	}
					    	if (status != null && !status.equals("1")) {
					    		worksheet.addCell(new Label(j++, i+1, info.getNewContactNo()));
					    		worksheet.addCell(new Label(j++, i+1, info.getNewEmail()));
					    	}
				    	}
				    }
				    
				    workbook.write();
					workbook.close();
				    
					os.flush();
					os.close();
				} else {
					FacultyDAOImpl fac = new FacultyDAOImpl();
					List facultyList = fac.searchFaculty(conn, new Faculty());
					SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
					List intakeParams = sysConfigDAOImpl.getSysIntake(conn);
					request.setAttribute("intake", (String)request.getSession().getAttribute("curIntake"));
					request.setAttribute("facultyList", facultyList);
					request.setAttribute("intakeList", intakeParams);
					request.setAttribute("error", "error");
					request.getRequestDispatcher("statisticDeliverBook.jsp").forward(request, response);
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

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
