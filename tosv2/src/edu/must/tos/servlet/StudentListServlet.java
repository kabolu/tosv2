package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Student;
import edu.must.tos.impl.PeriodDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;

public class StudentListServlet extends HttpServlet {

	public StudentListServlet() {
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
			conn.setAutoCommit(false);
			
			String intake = (String)session.getAttribute("curIntake");
			
			if(request.getParameter("remove") != null && request.getParameter("remove").equals("remove")){
				session.removeAttribute("totalPages");
  		   		session.removeAttribute("page");
  		   		session.removeAttribute("start");
  		   		session.removeAttribute("student");
  		   		session.removeAttribute("studentList");
  		   		session.removeAttribute("studReceOrPaidPeriodList");
			}
			
			//每頁記錄數
		    String num = "10";
			//分頁從第幾條記錄開始取數據
			String start = request.getParameter("start");
			if(start == null || start.equals("")){
				start = "0";
			}
			if(start.equals("page")) {
				start = "0";
			}
		
			//查詢條件參數
			String studentNo = "";
			if(request.getParameter("studentNo") != null && !request.getParameter("studentNo").equals("")){
				studentNo = request.getParameter("studentNo").trim();
			}
			String chineseName = "";
			if(request.getParameter("chineseName") != null && !request.getParameter("chineseName").equals("")){
				chineseName = request.getParameter("chineseName");
			}
			String englishName = "";
			if(request.getParameter("englishName") != null && !request.getParameter("englishName").equals("")){
				englishName = request.getParameter("englishName");
			}
			String idNo = "";
			if(request.getParameter("idNo") != null && !request.getParameter("idNo").equals("")){
				idNo = request.getParameter("idNo");
			}
			String facultyCode = "";
			if(request.getParameter("facultyCode") != null && !request.getParameter("facultyCode").equals("")){
				facultyCode = request.getParameter("facultyCode");
			}
			String email = "";
			if(request.getParameter("email") != null && !request.getParameter("email").equals("")){
				email = request.getParameter("email");
			}
			String contactNo = "";
			if(request.getParameter("contactNo") != null && !request.getParameter("contactNo").equals("")){
				contactNo = request.getParameter("contactNo");
			}
			String programCode = "";
			if(request.getParameter("programCode") != null && !request.getParameter("programCode").equals("")){
				programCode = request.getParameter("programCode");
			}
			
			//查找類型（普通學生信息查找還是 訂書學生信息查找）
			String searchType = request.getParameter("searchType");
			String oprType = request.getParameter("oprType");
			
			Student student = new Student();
			if(studentNo != null && !studentNo.equals("")){
				if(oprType != null && oprType.equals("received")){
					//若讀卡器讀取學生證時，program讀出的編號會不讀出‘-’和最後一位數字
					if(studentNo.indexOf("-")<0 && studentNo.length()>12){
						studentNo = studentNo.substring(0, 8)+"-"+studentNo.substring(8, 12)+"-"+studentNo.substring(12, studentNo.length());
					}
				}
				//若讀入學生編號中含有‘AP’符號的屬於申請編號，否則以學生編號處理
				if(studentNo.contains("AP")){
					student.setApplicantNo(studentNo);
				} else {
					student.setStudentNo(studentNo);
				}
			}
			if(chineseName != null && !chineseName.equals("")){
				student.setChineseName(chineseName);
			}
  		   	if(englishName != null && !englishName.equals("")){
  		   		student.setEnglishName(englishName);
  		   	}
  		   	if(idNo != null && !idNo.equals("")){
  		   		student.setIdNo(idNo);
  		   	}
  		   	if(facultyCode != null && !facultyCode.equals("")){
  		   		student.setFacultyCode(facultyCode);
  		   	}
  		   	if(email != null && !email.equals("")){
  		   		student.setEmail(email);
  		   	}
  		   	if(contactNo != null && !contactNo.equals("")){
  		   		student.setContactNo(contactNo);
  		   	}
  		   	if(programCode != null && !programCode.equals("")){
  		   		student.setProgramCode(programCode);
  		   	}
		   
  		   	StudentDAOImpl stuList = new StudentDAOImpl();
  		   	List studentList = stuList.searchStudent(conn, student, Integer.parseInt(start), Integer.parseInt(num));
  		   	
  		   	int count = stuList.getRsCount(conn, student);
  		   	
  		   	List page = stuList.getpage(conn, student, start, num, count);
  		   	
  		   	double totalPages = Math.ceil(count/Double.parseDouble(num));
  		   	
  		   	PeriodDAOImpl periodImpl = new PeriodDAOImpl();
  		   	if(searchType != null && searchType.equals("orderStudent")) {
  		   		Map studReceOrPaidPeriodList = periodImpl.getStudReceOrPaidPeriodMap(conn, intake);
  		   		session.setAttribute("totalPages", totalPages);
  		   		session.setAttribute("page", page);
  		   		session.setAttribute("start", start);
  		   		session.setAttribute("student", student);
  		   		session.setAttribute("studentList", studentList);
  		   		session.setAttribute("studReceOrPaidPeriodList", studReceOrPaidPeriodList);
  		   		
  		   		session.setAttribute("oprType", oprType);
  		   		if(request.getParameter("back") != null && request.getParameter("back").equals("back")){
  		   			request.setAttribute("back", "back");
  		   			request.getRequestDispatcher("orderbookindex.jsp").forward(request, response);
  		   		}else{
  		   			request.getRequestDispatcher("orderstudentlist.jsp").forward(request, response);
  		   		}
  		   	} else {
  		   		request.setAttribute("totalPages", totalPages);
  		   		request.setAttribute("page", page);
  		   		request.setAttribute("start", start);
  		   		request.setAttribute("student", student);
  		   		request.setAttribute("studentList", studentList);
  		   		request.getRequestDispatcher("studentList.jsp").forward(request,response);
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
