package edu.must.tos.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import edu.must.tos.bean.Student;
import edu.must.tos.impl.OrdBooksReportDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;

public class StuOrdBooksReportServlet extends HttpServlet {

	public StuOrdBooksReportServlet() {
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
		try{
	        //使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
		    conn = ds.getConnection();
			//传递报表中用到的参数值
	
			String studentNo = "", intake = "";
			studentNo = request.getParameter("studentNo");
			intake = request.getParameter("intake");  
			
			String paidStatus = "N";
			if(request.getParameter("paidstatus") != null && !request.getParameter("paidstatus").equals("")){
				paidStatus = request.getParameter("paidstatus");
			}
			
			OrdBooksReportDAOImpl impl = new OrdBooksReportDAOImpl();
			
			path = root_path + "report/studOrderBooks-v1.jasper";
			
			ResultSet rs = impl.isExistStuOrdBooks(conn, studentNo, intake, paidStatus);
			File reportFile = new File(path);
			
			StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
			String value = "";
			if(studentNo.length() == 18){
				value = studentNo + "," + null;
			}else{
				value = null + "," + studentNo;
			}
			List studentDetailList = studentDAOImpl.showStudentDetail(conn, value);
			Student stu = (Student)studentDetailList.get(0);
			if(rs.next()) {
				Map parameters = new HashMap();
				parameters.put("studentNo", studentNo);
				parameters.put("applicantNo", stu.getApplicantNo());
				parameters.put("intake", intake);
				parameters.put("paidStatus", paidStatus);
				parameters.put("SUBREPORT_DIR", root_path+"report/");
				parameters.put("REPORT_CONNECTION", conn);
				//System.out.println("---------Jasper begin-------------");
				//在控制台显示一下报表文件的物理路径
				//System.out.println(reportFile.getPath());
				rs.previous();
				byte[] bytes = null;
				try {
					//bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conn);
					bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, new JRResultSetDataSource(rs));
				} catch (Exception e) {
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
