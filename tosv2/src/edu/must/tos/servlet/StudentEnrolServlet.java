package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.must.tos.impl.StudentenrolDAOImpl;

public class StudentEnrolServlet extends HttpServlet {

	public StudentEnrolServlet() {
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
		request.setCharacterEncoding("utf-8");
		Connection conn = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			//每頁記錄數
		    String num = "10";
			//分頁從第幾條記錄開始取數據
			String start = request.getParameter("start");
			if(start == null || start.equals("")){
				start="0";
			}
			if(start.equals("page")) {
				start = "0";
			}
			
			String intakeList = null;
			if(request.getParameter("intakeList") != null && !request.getParameter("intakeList").equals("")){
				intakeList = request.getParameter("intakeList");
			}
			String faculty = null;
			if(request.getParameter("faculty") != null && !request.getParameter("faculty").equals("")){
				faculty = request.getParameter("faculty");
			}
			String prog = null;
			if(request.getParameter("prog") != null && !request.getParameter("prog").equals("")){
				prog = request.getParameter("prog");
			}
			String studentNo = null;
			if(request.getParameter("studentNo") != null && !request.getParameter("studentNo").equals("")){
				studentNo = request.getParameter("studentNo");
			}
			StudentenrolDAOImpl enlImpl = new StudentenrolDAOImpl();
			List studEnrolList = enlImpl.getStudentenrol(conn, intakeList, faculty, prog, studentNo, Integer.parseInt(start), Integer.parseInt(num));
			
			List page = enlImpl.getpage(conn, intakeList, faculty, prog, studentNo, start, num);
			
			double totalPages = Math.ceil(enlImpl.getRsCount(conn, intakeList, faculty, prog, studentNo)/Double.parseDouble(num));
			
			request.setAttribute("totalPages", totalPages);
  		   	request.setAttribute("page", page);
  		   	request.setAttribute("start", start);
  		   	request.setAttribute("intakeList", intakeList);
  		   	request.setAttribute("faculty", faculty);
  		   	request.setAttribute("prog", prog);
  		   	request.setAttribute("studentNo", studentNo);
  		   	request.setAttribute("studEnrolList", studEnrolList);
  		   	request.getRequestDispatcher("studentEnrolList.jsp").forward(request,response);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
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
