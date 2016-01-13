package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
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

public class StatisticUnSelReceiveTimeRecord extends HttpServlet {

	public StatisticUnSelReceiveTimeRecord() {
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
			if(!recordList.isEmpty()){
				response.reset();
				response.setContentType("application/octet-stream");
				String fileName = null;
				if(unselperiodType.equals("P")){
					fileName = "UnSelPaidTimeRecord";
				}else{
					fileName = "UnSelReceivedTimeRecord";
				}
				response.addHeader("Content-Disposition" , "attachment ; filename = "+fileName+".xls " );
				
				OutputStream os = response.getOutputStream();
			    WritableWorkbook workbook = Workbook.createWorkbook(os);
				WritableSheet worksheet = workbook.createSheet(fileName, 0);
				WritableCellFormat cellFormat = CellFormat.getCellFormat();
				
				String[] titleName = {"訂書序號","學員姓名","學員編號","學院","課程","年級"};
				for(int i=0; i<titleName.length; i++){
					worksheet.setColumnView(i, (titleName[i].getBytes().length+2));
			    	worksheet.addCell(new Label(i, 0, titleName[i], cellFormat));
				}
				for(int i=0; i<recordList.size(); i++){
					int j=0;
					StudReceivePeriodRecord record = new StudReceivePeriodRecord();
					record = (StudReceivePeriodRecord)recordList.get(i);
					worksheet.addCell(new Label(j++, i+1, record.getOrderseqNo()));
					worksheet.addCell(new Label(j++, i+1, record.getChineseName()));
					worksheet.addCell(new Label(j++, i+1, record.getStudentNo()));
					
					worksheet.addCell(new Label(j++, i+1, record.getFacChnName()));
					worksheet.addCell(new Label(j++, i+1, record.getProChnName()));
					if(record.getAcademicyear()<0){
						worksheet.addCell(new Label(j++, i+1, ""));
					}else {
						worksheet.addCell(new jxl.write.Number(j++, i+1, record.getAcademicyear()));
					}
				}
				workbook.write();
				workbook.close();
			    
				os.flush();
				os.close();
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
