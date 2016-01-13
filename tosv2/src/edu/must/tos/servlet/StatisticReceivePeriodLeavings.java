package edu.must.tos.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import edu.must.tos.bean.StudReceivePeriodRecord;
import edu.must.tos.impl.PeriodDAOImpl;
import edu.must.tos.util.CellFormat;

public class StatisticReceivePeriodLeavings extends HttpServlet {

	public StatisticReceivePeriodLeavings() {
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
		String root_path = this.getServletContext().getRealPath("/");
		String path = root_path+"report/statisticRecrivePeriodLeavings.jasper";
		File reportFile = new File(path);
		
		Connection conn = null; 
		ResultSet rs = null;
		try{
	        //使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
		    conn = ds.getConnection();
		
		    String intake = null;
			String fromDate = null;
			String toDate = null;
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
			if(request.getParameter("periodType") != null && !request.getParameter("periodType").equals("")){
				periodType = request.getParameter("periodType");
			}
			PeriodDAOImpl periodImpl = new PeriodDAOImpl();
			/*
			rs = periodImpl.getReceivePeriodLeavings(conn, intake, fromDate, toDate, periodType);
			if(rs.next()) {
				rs.previous();
				byte[] bytes = null;
				try {
					bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), null, new JRResultSetDataSource(rs));
				} catch (JRException e) {
					e.printStackTrace();
				}
				response.setContentType("application/pdf");
				response.setContentLength(bytes.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();
			}
			*/
			List list = periodImpl.getReceivePeriodLeavings(conn, intake, fromDate, toDate, periodType);
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition" , "attachment ; filename = '"+periodType+"'.xls " );
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			
			WritableSheet worksheet = workbook.createSheet(("P".equals(periodType)?"付款時段":"領書時段"), 0);
			
			worksheet.mergeCells(0, 0, 4, 0);
			worksheet.addCell(new Label(0, 0, "類型：" + ("P".equals(periodType)?"付款時段":"領書時段")));
			worksheet.mergeCells(0, 1, 4, 1);
			worksheet.addCell(new Label(0, 1, "打印日期：" + (new Date()).toString()));
			
			//表格式樣
			WritableCellFormat cellFormat = CellFormat.getCellFormat();
			String[] title = {("P".equals(periodType)?"付款":"領書")+"時段由", ("P".equals(periodType)?"付款":"領書")+"時段至", "設置人數", "已選人數", "人數剩餘"};
			for(int i=0; i<title.length; i++){
				 Label titleLabel = new Label(i, 2, title[i], cellFormat);
				 worksheet.addCell(titleLabel);
				 worksheet.setColumnView(i, title[i].getBytes().length+6);
			}
			for(int i=0; i<list.size(); i++){
				int j = 0;
				StudReceivePeriodRecord record = (StudReceivePeriodRecord)list.get(i);
				worksheet.addCell(new Label(j++, i+3, record.getStarttime().toString()));
				worksheet.addCell(new Label(j++, i+3, record.getEndtime().toString()));
				worksheet.addCell(new jxl.write.Number(j++, i+3, record.getMaxNo()));
				worksheet.addCell(new jxl.write.Number(j++, i+3, record.getReceivedNumber()));
				worksheet.addCell(new jxl.write.Number(j++, i+3, record.getLeavingNumber()));
			}
			workbook.write();
			workbook.close();
		    
			os.flush();
			os.close();
		}catch (Exception ex) {
			ex.printStackTrace();
		}  finally {
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
