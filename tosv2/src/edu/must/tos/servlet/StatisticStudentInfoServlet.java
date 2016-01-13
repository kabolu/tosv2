package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.util.CellFormat;

public class StatisticStudentInfoServlet extends HttpServlet {

	public StatisticStudentInfoServlet() {
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
			conn.setAutoCommit(false);
			
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String intake = request.getParameter("intake");
			
			Vector v = new Vector();
			StudentDAOImpl studentImpl = new StudentDAOImpl();
			v = studentImpl.getStatisticStudentInfo(conn, fromDate, toDate, intake);
			
			if(v.isEmpty()){
				request.setAttribute("flag", "false");
		    	request.getRequestDispatcher("statisticStudentInfo.jsp").forward(request, response);
			}else{
				response.reset();
			    response.setContentType("application/octet-stream");
				response.addHeader("Content-Disposition" , "attachment ; filename = StatisticStudentInfo.xls " );
			    
			    OutputStream os = response.getOutputStream();
			    
			    Vector inner = new Vector();
			    
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				WritableSheet worksheet = workbook.createSheet("result", 0);
				WritableCellFormat cellFormat = CellFormat.getCellFormat();
				
				String[] titleName = {"學生學號","中文名","英文名","圖書編號","書名","作者","出版商","出版年份","版本","圖書語言",
						"圖書類型","備注","學期","訂書日期","確定數量","未確定數量","退訂數量",
						"預付單價(MOP)","預付總金額(MOP)","預付單價(RMB)","預付總金額(RMB)",
						"實付單價(MOP)","實付總金額(MOP)","實付單價(RMB)","實付總金額(RMB)",
						"退定價(MOP)","退訂總金額(MOP)","退訂價(RMB)","退訂總金額(RMB)"};
				String value = "";
				Label label = null;
				jxl.write.Number number = null;
				for(int i=0; i<titleName.length; i++){
					 label=new Label(i, 0, titleName[i], cellFormat);
					 worksheet.addCell(label);
					 worksheet.setColumnView(i, titleName[i].getBytes().length+2);
				}
				DateFormat customDateFormat = new DateFormat ("yyyy-MM-dd hh:mm:ss"); 
				WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat);
				for(int i=0; i<v.size(); i++){
					inner = (Vector)v.get(i);
					for(int j=0; j<inner.size(); j++){
						if(j==inner.size()-13 || j==inner.size()-14 || j==inner.size()-15 ){
							Integer num = (Integer)inner.get(j);
							number = new jxl.write.Number(j, i+1, num.intValue());
							worksheet.addCell(number);
						}else if(j==inner.size()-17 || j==inner.size()-18 || j==inner.size()-19 
								|| j==inner.size()-20 || j==inner.size()-21 || j==inner.size()-22 || j==inner.size()-23 || j==inner.size()-24 
								|| j==inner.size()-25 || j==inner.size()-26 || j==inner.size()-27 || j==inner.size()-28
								 || j==inner.size()-29){
							value = (String)inner.get(j);
							label = new Label(j,i+1,value);
							worksheet.addCell(label);
						}else if(j==inner.size()-16){
							Date date = (Date)inner.get(j); 
							DateTime datetime = new DateTime(j, i+1, date, dateFormat);
							worksheet.addCell(datetime);
						}else{
							Double price = (Double)inner.get(j);
							number = new jxl.write.Number(j, i+1, price.doubleValue());
							worksheet.addCell(number);
						}
					}
				}
				workbook.write();
				workbook.close();
			    
				os.flush();
				os.close();
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
