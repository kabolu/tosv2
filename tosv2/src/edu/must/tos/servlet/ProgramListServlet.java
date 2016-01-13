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

import edu.must.tos.bean.Program;
import edu.must.tos.bean.SysCtrl;
import edu.must.tos.impl.ProgramDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.SysCtrlDAOImpl;

public class ProgramListServlet extends HttpServlet {

	public ProgramListServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response) ;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Connection conn = null;    
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			
			//每頁記錄數
			String num = "10";
			//分頁從第幾條記錄開始取數據
			String start=request.getParameter("start");
			if(start==null || start.equals("")){
				start="0";
			}
			if(start.equals("page")) {
				start = "0";
			}
			
			String prosearchType = request.getParameter("prosearchType");
			String searchType = request.getParameter("searchType");
			String programCode = request.getParameter("programCode");
			String facultyCode = request.getParameter("facultyCode");
			String chineseName = request.getParameter("chineseName");
			String englishName = request.getParameter("englishName");
			
			Program program =new Program();
			if(programCode!=null){
				program.setProgramCode(programCode);
			}
			if(facultyCode!=null){
				program.setFacultyCode(facultyCode);
			}
			if(chineseName!=null){
				program.setChineseName(chineseName);
			}
			if(englishName!=null){
				program.setEnglishName(englishName);
			}
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			String key = "CURRINTAKE";
			String intake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			ProgramDAOImpl proImpl = new ProgramDAOImpl();
			List programList = proImpl.searchProgram(conn,program,Integer.parseInt(start),Integer.parseInt(num));
			List page = proImpl.getpage(conn,program, start,num);
			double totalPages = Math.ceil(proImpl.getRsCount(conn,program)/Double.parseDouble(num));
			if(prosearchType!=null&&prosearchType.equals("programList")) {
				List list = proImpl.findByFacultyCode(conn,facultyCode);
				request.setAttribute("programList", list);
				request.setAttribute("searchType", searchType);
				request.getRequestDispatcher("studentsearch.jsp").forward(request, response);
			} else if (prosearchType != null && prosearchType.equals("totalReport")) {
				SysCtrlDAOImpl sysCtrlImpl = new SysCtrlDAOImpl();
				SysCtrl regionCtrl = new SysCtrl();
				regionCtrl.setType("AREA");
				List regionList = sysCtrlImpl.getDistinctValue1(conn, regionCtrl);
				
				List list = proImpl.findByFacultyCode(conn, facultyCode);
				request.setAttribute("intake", intake);
				request.setAttribute("programList", list);
				request.setAttribute("program", program);
				request.setAttribute("regionList", regionList);
				request.getRequestDispatcher("totalOrdBooksReport.jsp").forward(request, response);
			} else if(prosearchType != null && prosearchType.equals("statistic")){
				String code = request.getParameter("facultyCode");
				List promList = proImpl.findByFacultyCode(conn, code);
				response.setContentType("text/xml; charset=UTF-8");
				response.setHeader("Cache-Control", "no-cache");
				out.println("<response>");
				for(int i=0;i<promList.size();i++){
					Program prog = (Program)promList.get(i);
					out.println("<prog_code>" + prog.getProgramCode() + "</prog_code>");
					out.println("<prog_name>" + prog.getChineseName() + "</prog_name>");
				}
				out.println("</response>");
				out.close();
			} else {
				request.setAttribute("totalPages", totalPages);
				request.setAttribute("program", program);
				request.setAttribute("start", start);
				request.setAttribute("page", page);
				request.setAttribute("programList", programList);
				request.getRequestDispatcher("programList.jsp").forward(request,response);
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
