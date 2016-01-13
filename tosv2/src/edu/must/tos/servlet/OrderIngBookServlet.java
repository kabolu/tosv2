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

import edu.must.tos.bean.OrderInfo;

public class OrderIngBookServlet extends HttpServlet {

	public OrderIngBookServlet() {
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
		List<OrderInfo> selectedBookList = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			HttpSession session = request.getSession();
	    
			selectedBookList = (List)session.getAttribute("selectedBookList");
			if(selectedBookList == null) {
				selectedBookList = new ArrayList();
			}
	   
			String oprType = request.getParameter("oprType");
	    
			if (oprType != null && oprType.equals("cancel")) {
				int i = Integer.parseInt(request.getParameter("id"));
				selectedBookList.remove(i);
				session.setAttribute("selectedBookList", selectedBookList);
				request.getRequestDispatcher("orderbookindex2.jsp").forward(request, response);
			} else {
				String isbn = request.getParameter("isbn");
				String title = request.getParameter("title");
				String studentNo = request.getParameter("studentNo");
				int confirmQty = Integer.parseInt(request.getParameter("confirmQty"));
				String courseCode = request.getParameter("courseCode");
				String majorCode = request.getParameter("majorCode");
				int year = Integer.parseInt(request.getParameter("year"));
				double mopFuturePrice = Double.parseDouble(request.getParameter("mopFuturePrice"));
				double mopNetPrice = Double.parseDouble(request.getParameter("mopFuturePrice"));
				double rmbFuturePrice = Double.parseDouble(request.getParameter("rmbFuturePrice"));
				
				//rainbow add
				String supplement = request.getParameter("supplement");
				
				OrderInfo orderInfo = new OrderInfo();
				orderInfo.setIsbn(isbn);
				orderInfo.setTitle(title);
				orderInfo.setCourseCode(courseCode);
				orderInfo.setMajorCode(majorCode);
				orderInfo.setYear(year);
				orderInfo.setStudentNo(studentNo);
				orderInfo.setConfirmQty(confirmQty);				
		   
				orderInfo.setMopFuturePrice(mopFuturePrice);
				orderInfo.setMopNetPrice(mopNetPrice);
				orderInfo.setRmbFuturePrice(rmbFuturePrice);			
				
				//rainbow add 
				orderInfo.setSupplement(supplement);
				
				selectedBookList.add(orderInfo);
				session.setAttribute("selectedBookList", selectedBookList);
				request.getRequestDispatcher("orderbookindex2.jsp").forward(request, response);
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

	public void init() throws ServletException {
		// Put your code here
	}

}
