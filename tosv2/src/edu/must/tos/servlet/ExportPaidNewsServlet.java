package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import edu.must.tos.bean.VBookFeeForAppBean;
import edu.must.tos.impl.VBookFeeForAppDAOImpl;
import edu.must.tos.util.CellFormat;

public class ExportPaidNewsServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ExportPaidNewsServlet() {
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
		Connection conn = null;
		HttpSession session = request.getSession();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			VBookFeeForAppDAOImpl vbookFeeForAppDAOImpl = new VBookFeeForAppDAOImpl();
			
			String curIntake = (String)session.getAttribute("curIntake");
			
			String oprType = null;
			if(request.getParameter("oprType") != null){
				oprType = (String)request.getParameter("oprType");
			}
			if(oprType != null && oprType.equals("export")){
				String paidStatus = null;
				if(request.getParameter("paidStatus") != null){
					paidStatus = request.getParameter("paidStatus");
				}
				String date = null;
				if(request.getParameter("date") != null){
					date = request.getParameter("date");
				}
				String tranDate = null;
				if(request.getParameter("tranDate") != null){
					tranDate = request.getParameter("tranDate");
				}
				List list = vbookFeeForAppDAOImpl.getVBookFeeForAppList(conn, paidStatus, date, tranDate, curIntake);
				if(list.isEmpty()){
					request.setAttribute("flag", "false");
					request.getRequestDispatcher("exportPaidNewsInfo.jsp").forward(request, response);
				}else{
					response.reset();
					response.setContentType("application/octet-stream");
					response.addHeader("Content-Disposition" , "attachment ; filename = VBookFeeForAppResult.xls " );
					
					OutputStream os = response.getOutputStream();
					
					WritableWorkbook workbook = Workbook.createWorkbook(os);
					WritableSheet worksheet = workbook.createSheet("result", 0);
					
					WritableCellFormat cellFormat = CellFormat.getCellFormat();
					jxl.write.DateFormat customDateFormat = new jxl.write.DateFormat ("yyyy-MM-dd hh:mm"); 
					WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat);
					
					String[] titleName = {"學期","學員編號","學員姓名","學員英文姓名","學院編號","課程編號","專業編號","申請編號",
							"錄取狀況","地區","付款學期","付款幣種","付款金額","預付金額","到期日"};
					for(int i=0; i<titleName.length; i++){
						 Label label = new Label(i, 0, titleName[i], cellFormat);
						 worksheet.addCell(label);
						 worksheet.setColumnView(i, titleName[i].getBytes().length+2);
					}
					for(int j=0; j<list.size(); j++){
						VBookFeeForAppBean v = (VBookFeeForAppBean)list.get(j);
						worksheet.addCell(new Label(0, j+1, v.getPaidIntake()));
						worksheet.addCell(new Label(1, j+1, v.getStudentNo()));
						
						worksheet.addCell(new Label(2, j+1, v.getChinesename()));
						worksheet.addCell(new Label(3, j+1, v.getEnglishname()));
						worksheet.addCell(new Label(4, j+1, v.getFacultycode()));
						worksheet.addCell(new Label(5, j+1, v.getProgramcode()));
						worksheet.addCell(new Label(6, j+1, v.getMajorcode()));
						
						worksheet.addCell(new Label(7, j+1, v.getApplicantNo()));
						worksheet.addCell(new Label(8, j+1, v.getAcceptance()));
						worksheet.addCell(new Label(9, j+1, v.getRegion()));
						
						worksheet.addCell(new Label(10, j+1, v.getPaidIntake()));
						worksheet.addCell(new Label(11, j+1, v.getPaidCurrency()));
						worksheet.addCell(new jxl.write.Number(12, j+1, v.getPaidAmount()));
						worksheet.addCell(new jxl.write.Number(13, j+1, v.getPrePaid()));
						worksheet.addCell(new jxl.write.DateTime(14, j+1, (Date)v.getDueDate(), dateFormat));
					}
					workbook.write();
					workbook.close();
				    
					os.flush();
					os.close();
				}
			}else{
				request.getRequestDispatcher("exportPaidNewsInfo.jsp").forward(request, response);
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
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
