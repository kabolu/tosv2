package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Student;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;

/**
 * @author xfke
 * 2013/10/24
 */
public class StudentLackBookServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public StudentLackBookServlet() {
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

		doPost(request, response);
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

		HttpSession session = request.getSession();
		Connection conn = null;    
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String curIntake = (String)session.getAttribute("curIntake");
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			
			if(type != null && "SEARCHARRIVAL".equals(type)){
				doSearchArrival(conn, request, response, curIntake);
			} 
			else if (type != null && "SEARCHDRAWBACK".equals(type)){
				doSearchDrawback(conn, request, response, curIntake);
			}
			else if (type != null && "SEARCHCHANGED".equals(type)){
				doSearchChanged(conn, request, response, curIntake);
			}
			else if(type != null && "SEARCHOVERDUE".equals(type)){
				doSearchOverdue(conn, request, response, curIntake);
			}
			else if(type != null && "SEARCHSUPPLEMENT".equals(type)){
				doSearchSupplement(conn, request, response, curIntake);
			}
			else if(type != null && "SEARCHTEACHINGCHANGE".equals(type)){
				doSearchTeachingChange(conn, request, response, curIntake);
			}
			else if(type != null && "SEARCHUNPAIDORDER".equals(type)){
				doSearchUnpaidOrder(conn, request, response, curIntake);
			}
			else if(type != null && "SEARCHPREBOOK".equals(type)){
				doSearchPreBook(conn, request, response, curIntake);
			}
			else if(type != null && "SEARCHCANCEL".equals(type)){
				doSearchCancel(conn, request, response, curIntake);
			}
			else if (type != null && "changedPage".equals(type)){
				request.getRequestDispatcher("emailnotice/searchChanged.jsp").forward(request, response);
			}
			else if (type != null && "drawbackPage".equals(type)){
				request.getRequestDispatcher("emailnotice/searchDrawback.jsp").forward(request, response);
			}
			else if (type != null && "overduePage".equals(type)){
				request.getRequestDispatcher("emailnotice/searchOverdue.jsp").forward(request, response);
			}
			else if (type != null && "supplementPage".equals(type)){
				request.getRequestDispatcher("emailnotice/searchSupplement.jsp").forward(request, response);
			}
			else if (type != null && "teachingChangePage".equals(type)){
				request.getRequestDispatcher("emailnotice/searchTeachingChange.jsp").forward(request, response);
			}
			else if (type != null && "unpaidOrderPage".equals(type)){
				request.getRequestDispatcher("emailnotice/searchUnpaidOrder.jsp").forward(request, response);
			}
			else if (type != null && "preBookPage".equals(type)){
				request.getRequestDispatcher("emailnotice/searchPreBook.jsp").forward(request, response);
			}
			else if (type != null && "cancelPage".equals(type)){
				request.getRequestDispatcher("emailnotice/searchCancel.jsp").forward(request, response);
			}
			else {
				request.getRequestDispatcher("emailnotice/searchLack.jsp").forward(request, response);
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

	
	private void doSearchChanged(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception {
		//查詢條件參數
		String isbn = request.getParameter("isbn");
		Book book = new Book();
		if(isbn != null && !"".equals(isbn)){
			book.setIsbn(isbn);
		}
		
		StudentDAOImpl stuList = new StudentDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		List<OrDetail> detailList = orderDetailDAOImpl.getOrDetail(conn, curIntake, isbn, "Y");
		List resultList = new ArrayList();
		List studList = new ArrayList();
		for(OrDetail od : detailList){
			//不要零售記錄
			if(!"RETAIL".equals(od.getStudentNo())){
				Student stud = (Student)((List)stuList.showStudentDetail(conn, od.getStudentNo()+","+od.getStudentNo())).get(0);
				studList.add(stud);
				resultList.add(od);
			}
		}
		request.setAttribute("studList", studList);
   		request.setAttribute("detailList", resultList);
   		request.setAttribute("isbn", isbn);
   		request.getRequestDispatcher("emailnotice/searchChanged.jsp").forward(request, response);
	}

	private void doSearchDrawback(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception, Exception {
		//查詢條件參數
		String isbn = request.getParameter("isbn");
		Book book = new Book();
		if(isbn != null && !"".equals(isbn)){
			book.setIsbn(isbn);
		}
		
		StudentDAOImpl stuList = new StudentDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		List<OrDetail> detailList = orderDetailDAOImpl.getOrDetail(conn, curIntake, isbn, "Y");
		List resultList = new ArrayList();
		List studList = new ArrayList();
		for(OrDetail od : detailList){
			//不要零售記錄
			if(!"RETAIL".equals(od.getStudentNo())){
				Student stud = (Student)((List)stuList.showStudentDetail(conn, od.getStudentNo()+","+od.getStudentNo())).get(0);
				studList.add(stud);
				resultList.add(od);
			}
		}
		request.setAttribute("studList", studList);
   		request.setAttribute("detailList", resultList);
   		request.setAttribute("isbn", isbn);
   		request.getRequestDispatcher("emailnotice/searchDrawback.jsp").forward(request, response);
	}

	private void doSearchArrival(Connection conn, HttpServletRequest request, HttpServletResponse response, String curIntake) throws Exception {
		//查詢條件參數
		String isbn = request.getParameter("isbn");
		Book book = new Book();
		if(isbn != null && !"".equals(isbn)){
			book.setIsbn(isbn);
		}
		
		String fromDate = null;
		if(request.getParameter("fromDate") != null){
			fromDate = request.getParameter("fromDate") + " 00:00:00";
		}
		String toDate = null;
		if(request.getParameter("toDate") != null){
			toDate = request.getParameter("toDate") + " 23:59:59";
		}
		
		StudentDAOImpl stuList = new StudentDAOImpl();
		List studentList = stuList.searchStudentLackBook(conn, book, curIntake, fromDate, toDate);
   		request.setAttribute("studentList", studentList);
   		request.setAttribute("isbn", isbn);
   		request.getRequestDispatcher("emailnotice/searchLack.jsp").forward(request, response);
	}
	
	private void doSearchOverdue(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception {
		//查詢條件參數
		String isbn = request.getParameter("isbn");
		Book book = new Book();
		if(isbn != null && !"".equals(isbn)){
			book.setIsbn(isbn);
		}
		
		String fromDate = null;
		if(request.getParameter("fromDate") != null){
			fromDate = request.getParameter("fromDate") + " 00:00:00";
		}
		String toDate = null;
		if(request.getParameter("toDate") != null){
			toDate = request.getParameter("toDate") + " 23:59:59";
		}
		
		StudentDAOImpl stuList = new StudentDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		List<OrDetail> detailList = orderDetailDAOImpl.getOrderDetailByStatus(conn, curIntake, isbn, "2", fromDate, toDate);
		List resultList = new ArrayList();
		List studList = new ArrayList();
		for(OrDetail od : detailList){
			//不要零售記錄
			if(!"RETAIL".equals(od.getStudentNo()) && ("Y".equals(od.getPaidStatus()) || "R".equals(od.getPaidStatus()))){
				List<Student> stList = stuList.showStudentDetail(conn, od.getStudentNo()+","+od.getStudentNo());
				if(stList!=null&&!stList.isEmpty()){
					Student stud = (Student) stList.get(0);					
					studList.add(stud);
					resultList.add(od);
				}
			}
		}
		request.setAttribute("studList", studList);
   		request.setAttribute("detailList", resultList);
   		request.setAttribute("isbn", isbn);
   		request.getRequestDispatcher("emailnotice/searchOverdue.jsp").forward(request, response);
	}
	
	private void doSearchSupplement(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception {
		//查詢條件參數
		String isbn = request.getParameter("isbn");
		Book book = new Book();
		if(isbn != null && !"".equals(isbn)){
			book.setIsbn(isbn);
		}
		
		String fromDate = null;
		if(request.getParameter("fromDate") != null){
			fromDate = request.getParameter("fromDate") + " 00:00:00";
		}
		String toDate = null;
		if(request.getParameter("toDate") != null){
			toDate = request.getParameter("toDate") + " 23:59:59";
		}
		
		StudentDAOImpl stuList = new StudentDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		List<OrDetail> detailList = orderDetailDAOImpl.getOrderDetailByStatus(conn, curIntake, isbn, "2", fromDate, toDate);
		List resultList = new ArrayList();
		List studList = new ArrayList();
		for(OrDetail od : detailList){
			//不要零售記錄
			if(!"RETAIL".equals(od.getStudentNo()) && ("Y".equals(od.getPaidStatus()) || "R".equals(od.getPaidStatus()))){
				List<Student> stList = stuList.showStudentDetail(conn, od.getStudentNo()+","+od.getStudentNo());
				if(stList!=null&&!stList.isEmpty()){
					Student stud = (Student) stList.get(0);
					studList.add(stud);
					resultList.add(od);
				}
			}
		}
		request.setAttribute("studList", studList);
   		request.setAttribute("detailList", resultList);
   		request.setAttribute("isbn", isbn);
   		request.getRequestDispatcher("emailnotice/searchSupplement.jsp").forward(request, response);
	}
	
	private void doSearchTeachingChange(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception {
		//查詢條件參數
		String isbn = request.getParameter("isbn");
		Book book = new Book();
		if(isbn != null && !"".equals(isbn)){
			book.setIsbn(isbn);
		}
		
		StudentDAOImpl stuList = new StudentDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		String fromDate = null;
		if(request.getParameter("fromDate") != null){
			fromDate = request.getParameter("fromDate") + " 00:00:00";
		}
		String toDate = null;
		if(request.getParameter("toDate") != null){
			toDate = request.getParameter("toDate") + " 23:59:59";
		}
		
		List<OrDetail> detailList = orderDetailDAOImpl.getOrderDetailByStatus(conn, curIntake, isbn, null, fromDate, toDate);
		List resultList = new ArrayList();
		List studList = new ArrayList();
		for(OrDetail od : detailList){
			//不要零售記錄
			if(!"RETAIL".equals(od.getStudentNo())){
				Student stud = (Student)((List)stuList.showStudentDetail(conn, od.getStudentNo()+","+od.getStudentNo())).get(0);
				studList.add(stud);
				resultList.add(od);
			}
		}
		request.setAttribute("studList", studList);
   		request.setAttribute("detailList", resultList);
   		request.setAttribute("isbn", isbn);
   		request.getRequestDispatcher("emailnotice/searchTeachingChange.jsp").forward(request, response);
	}
	
	private void doSearchUnpaidOrder(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception {
		//查詢條件參數
		String isbn = request.getParameter("isbn");
		Book book = new Book();
		if(isbn != null && !"".equals(isbn)){
			book.setIsbn(isbn);
		}
		
		String fromDate = null;
		if(request.getParameter("fromDate") != null){
			fromDate = request.getParameter("fromDate") + " 00:00:00";
		}
		String toDate = null;
		if(request.getParameter("toDate") != null){
			toDate = request.getParameter("toDate") + " 23:59:59";
		}
		
		StudentDAOImpl stuList = new StudentDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		List<OrDetail> detailList = orderDetailDAOImpl.getOrderDetailByStatus(conn, curIntake, isbn, null, fromDate, toDate);
		List resultList = new ArrayList();
		List studList = new ArrayList();
		for(OrDetail od : detailList){
			//不要零售記錄
			if(!"RETAIL".equals(od.getStudentNo()) && "N".equals(od.getPaidStatus())){
				List<Student> stList = stuList.showStudentDetail(conn, od.getStudentNo()+","+od.getStudentNo());
				if(stList!=null&&!stList.isEmpty()){
					Student stud = (Student) stList.get(0);					
					studList.add(stud);
					resultList.add(od);
				}
			}
		}
		request.setAttribute("studList", studList);
   		request.setAttribute("detailList", resultList);
   		request.setAttribute("isbn", isbn);
   		request.getRequestDispatcher("emailnotice/searchUnpaidOrder.jsp").forward(request, response);
	}

	private void doSearchCancel(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception {
		//查詢條件參數
		String isbn = request.getParameter("isbn");
		Book book = new Book();
		if(isbn != null && !"".equals(isbn)){
			book.setIsbn(isbn);
		}
		
		String fromDate = null;
		if(request.getParameter("fromDate") != null){
			fromDate = request.getParameter("fromDate") + " 00:00:00";
		}
		String toDate = null;
		if(request.getParameter("toDate") != null){
			toDate = request.getParameter("toDate") + " 23:59:59";
		}
		
		StudentDAOImpl stuList = new StudentDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		List<OrDetail> detailList = orderDetailDAOImpl.getOrderDetailByStatus(conn, curIntake, isbn, null, fromDate, toDate);
		List resultList = new ArrayList();
		List studList = new ArrayList();
		for(OrDetail od : detailList){
			//不要零售記錄
			if(!"RETAIL".equals(od.getStudentNo()) && "N".equals(od.getPaidStatus()) ){
				List<Student> stList = stuList.showStudentDetail(conn, od.getStudentNo()+","+od.getStudentNo());
				if(stList!=null&&!stList.isEmpty()){
					Student stud = (Student) stList.get(0);					
					studList.add(stud);
					resultList.add(od);
				}
			}
		}
		request.setAttribute("studList", studList);
   		request.setAttribute("detailList", resultList);
   		request.setAttribute("isbn", isbn);
   		request.getRequestDispatcher("emailnotice/searchCancel.jsp").forward(request, response);
	}
	
	private void doSearchPreBook(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception {
		//查詢條件參數
		String studentNo = request.getParameter("studentNo");

		String fromDate = null;
		if(request.getParameter("fromDate") != null && !"".equals(request.getParameter("toDate"))){
			fromDate = request.getParameter("fromDate") + " 00:00:00";
		}
		String toDate = null;
		if(request.getParameter("toDate") != null && !"".equals(request.getParameter("toDate"))){
			toDate = request.getParameter("toDate") + " 23:59:59";
		}
		
		StudentDAOImpl stuList = new StudentDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		List<OrDetail> detailList = orderDetailDAOImpl.getOrderDetailByStatus(conn, curIntake, null, null, fromDate, toDate);
		List resultList = new ArrayList();
		List studList = new ArrayList();
		for(OrDetail od : detailList){
			//不要零售記錄
			if(!"RETAIL".equals(od.getStudentNo()) && "N".equals(od.getPaidStatus())){
				if(studentNo == null || "".equals(studentNo)){
					String value = "";
					String orderStudentNo = od.getStudentNo();
					if(orderStudentNo.length() == 14)
						value = "null," + od.getStudentNo();
					else
						value = od.getStudentNo() + "," + od.getStudentNo();
					List<Student> stList = stuList.showStudentDetail(conn, value);
					if(stList != null && !stList.isEmpty()){
						Student stud = (Student) stList.get(0);	
						if(studList.isEmpty()){
							studList.add(stud);
							resultList.add(od);
						} else {
							boolean flag = false;
							for(Object rl : resultList){
								OrDetail ode = (OrDetail) rl;
								if(ode.getOrderSeqNo()!=od.getOrderSeqNo()){
									flag = true;
								}else{
									flag = false;
									break;
								}
							}
							if(flag){
								studList.add(stud);
								resultList.add(od);
							}
						}
					}
				} else if (studentNo.trim().equals(od.getStudentNo())){
					String value = "";
					String orderStudentNo = od.getStudentNo();
					if(orderStudentNo.length() == 14)
						value = "null," + od.getStudentNo();
					else
						value = od.getStudentNo() + "," + od.getStudentNo();
					List<Student> stList = stuList.showStudentDetail(conn, value);
					if(stList != null && !stList.isEmpty()){
						Student stud = (Student) stList.get(0);					
						studList.add(stud);
						resultList.add(od);
					}
					break;
				}
			}
		}
		request.setAttribute("studList", studList);
   		request.setAttribute("detailList", resultList);
   		request.setAttribute("studentNo", studentNo);
   		request.getRequestDispatcher("emailnotice/searchPreBook.jsp").forward(request, response);
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
