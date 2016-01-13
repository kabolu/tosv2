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

import edu.must.tos.bean.Book;
import edu.must.tos.bean.Model;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.SysConfigDAOImpl;

public class EditBookBackServlet extends HttpServlet {

	public EditBookBackServlet() {
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
	    
			HttpSession session = request.getSession();
			Model editM = (Model)session.getAttribute("editM");
			Book book = editM.getBook();
			List priceList = editM.getPrice();
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			List langList = sysConfigDAOImpl.getLanguage(conn);
			
			SysConfig config = new SysConfig();
			config.setScType("EXCHANGERATE");
			config.setActInd("Y");
			List rateList = sysConfigDAOImpl.getSysConfigList(conn, config);
			request.setAttribute("rateList", rateList);
			
			session.setAttribute("book", book);
			session.setAttribute("priceList", priceList);
			session.setAttribute("langList", langList);
			request.getRequestDispatcher("bookEdit.jsp").forward(request, response);
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
