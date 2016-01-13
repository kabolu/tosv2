package edu.must.tos.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import edu.must.tos.impl.OrdBooksReportDAOImpl;

public class TotalOrdBooksReportServlet extends HttpServlet {

	public TotalOrdBooksReportServlet() {
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
		//报表编译之后生成的.jasper 文件的存放位置
		String root_path = this.getServletContext().getRealPath("/");
		root_path = root_path.replace('\\', '/');
		String path = "";
		
		Connection conn = null; 
		ResultSet rs = null;
		try{
	        //使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
		    conn = ds.getConnection();
			//传递报表中用到的参数值
	
		    String facultyCode = "", programCode = "", year = "", intake = "";
			if(request.getParameter("facultyCode") != null && !request.getParameter("facultyCode").equals("")){
				facultyCode = request.getParameter("facultyCode");
			}
			if(request.getParameter("programCode") != null && !request.getParameter("programCode").equals("")){
				programCode = request.getParameter("programCode");
			}
			year = request.getParameter("year");
			intake = request.getParameter("intake");
			String studType = null;
			if(request.getParameter("studType") != null && !request.getParameter("studType").equals("")){
				studType = request.getParameter("studType");
			}
			OrdBooksReportDAOImpl impl = new OrdBooksReportDAOImpl();
			String region = request.getParameter("region");
			String paidStatus = request.getParameter("paidstatus");
			String isDiffer = "A";
			if(request.getParameter("isDiffer") != null){
				isDiffer = request.getParameter("isDiffer");
			}
			String fromDate = "";
			if(request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")){
				fromDate = request.getParameter("fromDate") + " 00:00:00";
			}
			String toDate = "";
			if(request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")){
				toDate = request.getParameter("toDate") + " 23:59:59";
			}
			String printParam = request.getParameter("printParam");
			if(printParam != null){	//按學員訂單序號打印
				if(studType != null){
					if(studType != null && studType.equals("new")){
						path = root_path + "report/totalNewsOrdBooksBySeqNo.jasper";
						rs = impl.getNewsOrdBooksBySeqNo(conn, facultyCode, programCode, year, intake, studType, region, paidStatus, isDiffer, fromDate, toDate);
					}else if(studType != null && studType.equals("old")){
						if(paidStatus == null || paidStatus.equals("")){
							paidStatus = "N";
						}
						path = root_path + "report/totalOrderBooksBySeqNo.jasper";
						rs = impl.getOrdBooksBySeqNo(conn, facultyCode, programCode, year, intake, studType, region, paidStatus, fromDate, toDate);
					}
				}else{
					studType = "old";
					paidStatus = "N"; //默認為N
					path = root_path + "report/totalOrderBooksBySeqNo.jasper";
					rs = impl.getOrdBooksBySeqNo(conn, facultyCode, programCode, year, intake, studType, region, paidStatus, fromDate, toDate);
				}
			}else{
				if(studType != null){
					if(studType != null && studType.equals("new")){
						path = root_path + "report/totalNewsOrdBooks.jasper";
						rs = impl.getNewsOrdBooks(conn, facultyCode, programCode, year, intake, studType, region, paidStatus, isDiffer, fromDate, toDate);
					}else if(studType != null && studType.equals("old")){
						if(paidStatus == null || paidStatus.equals("")){
							paidStatus = "N";
						}
						path = root_path + "report/totalOrdBooks.jasper";
						rs = impl.getOrdBooks(conn, facultyCode, programCode, year, intake, studType, region, paidStatus, fromDate, toDate);
					}
				}else{
					studType = "old";
					paidStatus = "N"; //默認為N
					path = root_path + "report/totalOrdBooks.jasper";
					rs = impl.getOrdBooks(conn, facultyCode, programCode, year, intake, studType, region, paidStatus, fromDate, toDate);
				}
			}
			File reportFile = new File(path);
			if(rs.next()) {
				//System.out.println("---------Jasper begin-------------");
				//在控制台显示一下报表文件的物理路径
				//System.out.println(reportFile.getPath());
				Map parameters = new HashMap();
				parameters.put("SUBREPORT_DIR", root_path+"report/");
				parameters.put("REPORT_CONNECTION", conn);
				if(studType!=null){
					if(studType!=null && studType.equals("new")){
						parameters.put("paidStatus", isDiffer);
					}else if(studType!=null && studType.equals("old")){
						if(paidStatus==null || paidStatus.equals("")){
							paidStatus = "N";
						}
						parameters.put("paidStatus", paidStatus);
					}
				}else{
					paidStatus = "N"; //默認為A
					parameters.put("paidStatus", paidStatus);
				}
				rs.previous();
				byte[] bytes = null;
				try {
					bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, new JRResultSetDataSource(rs));
					
				} catch (JRException e) {
					e.printStackTrace();
				}
				//System.out.println("---------Jasper end-------------");
				response.setContentType("application/pdf");
				response.setContentLength(bytes.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();
			} else {
				request.setAttribute("msg", "無打印數據!");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
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
