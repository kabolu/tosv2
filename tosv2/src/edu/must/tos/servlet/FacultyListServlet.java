package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Faculty;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.bean.SysCtrl;
import edu.must.tos.impl.FacultyDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.SysCtrlDAOImpl;

public class FacultyListServlet extends HttpServlet {

	public FacultyListServlet() {
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
		HttpSession session = request.getSession();
		Connection conn = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
	    
			String type = request.getParameter("type");
			String searchType = request.getParameter("searchType");
			String facultyCode = request.getParameter("facultyCode");
			String chineseName = request.getParameter("chineseName");
			String englishName = request.getParameter("englishName");
		
			Faculty fa = new Faculty();
			if(facultyCode != null){
				fa.setFacultyCode(facultyCode);
			}
			if(chineseName != null){
				fa.setChineseName(chineseName);
			}
			if(englishName != null){
				fa.setEnglishName(englishName);
			}
		
			FacultyDAOImpl facultyDAOImpl = new FacultyDAOImpl();
			List facultyList = facultyDAOImpl.searchFaculty(conn, fa);
				
			session.removeAttribute("oprType");
		
			session.setAttribute("facultyList", facultyList);
		
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			String key = "CURRINTAKE";
			String intake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			if(type != null && type.equals("studentSearch")) {
				String oprType = request.getParameter("oprType");
				if(oprType != null && !oprType.equals("")) {
					session.setAttribute("oprType", oprType);
				}
				//學生訂書搜索頁面searchType=orderStudent && oprType=orderBooks
				if(searchType != null && searchType.equals("orderStudent") && oprType != null && oprType.equals("orderBooks")) {
					request.setAttribute("searchType", "orderStudent");
					request.setAttribute("oprType", oprType);
					request.getRequestDispatcher("studentsearch.jsp").forward(request,response);
				}
				//領書頁面
				else if(searchType != null && searchType.equals("orderStudent") && oprType != null && oprType.equals("received")){
					request.setAttribute("searchType", "orderStudent");
					request.setAttribute("oprType", oprType);
					request.getRequestDispatcher("studentsearch.jsp").forward(request,response);
				}else {
					request.getRequestDispatcher("studentsearch.jsp").forward(request, response);
				}
			}else if(type != null && type.equals("studEnrolSearch")){
				List intakeList = sysConfigDAOImpl.getSysIntake(conn);
				request.setAttribute("intake", intake);
				request.setAttribute("facultyList", facultyList);
				request.setAttribute("intakeList", intakeList);
				request.getRequestDispatcher("studEnrolSearch.jsp").forward(request,response);
			} else if(type != null && type.equals("uploadBookTempl")){
				request.setAttribute("facultyList", facultyList);
				request.getRequestDispatcher("uploadBookTempl.jsp").forward(request, response);
			} else if(type != null && type.equals("bookSearch")) {
				request.getRequestDispatcher("bookSearch.jsp").forward(request, response);
			} else if(type != null && type.equals("courseSearch")) {
				request.getRequestDispatcher("courseSearch.jsp").forward(request, response);
			} else if(type != null && type.equals("programSearch")) {
				request.getRequestDispatcher("programSearch.jsp").forward(request, response);
			} else if(type != null && type.equals("orderedBookSearch")) {	//點擊“訂書”按鈕的操作
				session.removeAttribute("facultyCode");
				session.removeAttribute("year");
				session.removeAttribute("facList");
				String studentNo = null;
				String applicantNo = null;
				if(request.getParameter("studentNo")!=null && !request.getParameter("studentNo").equals("null")){
					studentNo = request.getParameter("studentNo");
				}
				if(request.getParameter("applicantNo")!=null && !request.getParameter("applicantNo").equals("null")){
					applicantNo = request.getParameter("applicantNo");
				}
				String value = studentNo+","+applicantNo;
				
				StudentDAOImpl stuImpl = new StudentDAOImpl();
				List stuDetList =  stuImpl.showStudentDetail(conn, value);
				
				String retail = null;
				if(request.getParameter("retail")!=null && !request.getParameter("retail").equals("")){
					retail = request.getParameter("retail");
				}
				
				List facList = null;
				facList = facultyDAOImpl.getStudToFaculty(conn, value, intake);
				
				if(facList.size() == 0){
					facList = facultyDAOImpl.getAutoFaculty(conn, value);
				}
				Faculty faculty = new Faculty();
				faculty.setFacultyCode("other");
				faculty.setChineseName("其他");
				facList.add(faculty);
				
				session.setAttribute("facList", facList);
				session.setAttribute("stuDetList", stuDetList);
				session.setAttribute("retail", retail);
				session.removeAttribute("availableBooklist");
				request.getRequestDispatcher("orderbookindex2.jsp").forward(request, response);
			} else if(type != null && type.equals("totalReport")) {
				SysCtrlDAOImpl sysCtrlImpl = new SysCtrlDAOImpl();
				SysCtrl regionCtrl = new SysCtrl();
				regionCtrl.setType("AREA");
				List regionList = sysCtrlImpl.getDistinctValue1(conn, regionCtrl);
				
				request.setAttribute("intake", intake);
				request.setAttribute("facultyList", facultyList);
				request.setAttribute("regionList", regionList);
				request.getRequestDispatcher("totalOrdBooksReport.jsp").forward(request,response);
			} else if(type != null && type.equals("studOrdBookSummary")){
				String error = null;
				if(request.getParameter("error")!=null){
					error = request.getParameter("error");
				}
				List intakeList = sysConfigDAOImpl.getSysIntake(conn);
				request.setAttribute("intake", intake);
				request.setAttribute("facultyList", facultyList);
				request.setAttribute("intakeList", intakeList);
				request.setAttribute("error", error);
				request.getRequestDispatcher("statisticStudOrdSumm.jsp").forward(request,response);
			}else if(type != null && type.equals("studDeliverBook")){
				List intakeList = sysConfigDAOImpl.getSysIntake(conn);
				request.setAttribute("intake", intake);
				request.setAttribute("facultyList", facultyList);
				request.setAttribute("intakeList", intakeList);
				request.getRequestDispatcher("statisticDeliverBook.jsp").forward(request,response);
			}else if(type != null && type.equals("studReceiveTimeRecord")){
				List intakeList = sysConfigDAOImpl.getSysIntake(conn);
				
				SysConfig sc = new SysConfig();
				sc.setScType("RECEIVED");
				sc.setScKey(intake);
				sc.setActInd("Y");
				sc = sysConfigDAOImpl.getSysConfig(conn, sc);
				request.setAttribute("sc", sc);
				request.setAttribute("intake", intake);
				request.setAttribute("intakeList", intakeList);
				request.setAttribute("facultyList", facultyList);
				request.getRequestDispatcher("statisticStudReceivePeriod.jsp").forward(request,response);
			}else if(type != null && type.equals("statisticReceivePeriod")){
				List intakeList = sysConfigDAOImpl.getSysIntake(conn);
				
				SysConfig sc = new SysConfig();
				sc.setScType("RECEIVED");
				sc.setScKey(intake);
				sc.setActInd("Y");
				sc = sysConfigDAOImpl.getSysConfig(conn, sc);
				//sc = sysConfigDAOImpl.getSysConfig(conn, sc);
				request.setAttribute("sc", sc);
				request.setAttribute("intake", intake);
				request.setAttribute("intakeList", intakeList);
				request.setAttribute("facultyList", facultyList);
				request.getRequestDispatcher("statisticReceivePeriod.jsp").forward(request,response);
			}else {
				request.getRequestDispatcher("facultyList.jsp").forward(request,response);
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
