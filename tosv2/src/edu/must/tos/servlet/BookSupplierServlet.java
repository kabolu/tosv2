package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import edu.must.tos.bean.BookSupplier;
import edu.must.tos.impl.BookSupplierDAOImpl;

public class BookSupplierServlet extends HttpServlet {

	public BookSupplierServlet() {
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
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		HttpSession session = request.getSession();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
			
			String userId = session.getAttribute("userId").toString();
			
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
			
			String type = null;
			if(request.getParameter("type")!=null && !request.getParameter("type").equals("")){
				type = request.getParameter("type");
			}
			if(type!=null && type.equals("add")){
				BookSupplier supplier = new BookSupplier();
				String code = null;
				if(request.getParameter("code")!=null){
					code = request.getParameter("code");
					supplier.setSupplierCode(code);
				}
				String name = null;
				if(request.getParameter("name")!=null){
					name = request.getParameter("name");
					supplier.setSupplierName(name);
				}
				String engName = null;
				if(request.getParameter("engName")!=null){
					engName = request.getParameter("engName");
					supplier.setSupplierEngName(engName);
				}
				String contactName = null;
				if(request.getParameter("contactName")!=null){
					contactName = request.getParameter("contactName");
					supplier.setContactName(contactName);
				}
				String contactTel1 = null;
				if(request.getParameter("contactTel1")!=null){
					contactTel1 = request.getParameter("contactTel1");
					supplier.setSupplierTel_1(contactTel1);
				}
				String contactTel2 = null;
				if(request.getParameter("contactTel2")!=null){
					contactTel2 = request.getParameter("contactTel2");
					supplier.setSupplierTel_2(contactTel2);
				}
				String contactFax1 = null;
				if(request.getParameter("contactFax1")!=null){
					contactFax1 = request.getParameter("contactFax1");
					supplier.setSupplierFax_1(contactFax1);
				}
				String contactFax2 = null;
				if(request.getParameter("contactFax2")!=null){
					contactFax2 = request.getParameter("contactFax2");
					supplier.setSupplierFax_2(contactFax2);
				}
				String email = null;
				if(request.getParameter("email")!=null){
					email = request.getParameter("email");
					supplier.setSupplierEmail(email);
				}
				String address = null;
				if(request.getParameter("address")!=null){
					address = request.getParameter("address");
					supplier.setAddress(address);
				}
				String resume = null;
				if(request.getParameter("resume")!=null){
					resume = request.getParameter("resume");
					supplier.setResumeInfo(resume);
				}
				String remarks = null;
				if(request.getParameter("remarks")!=null){
					remarks = request.getParameter("remarks");
					supplier.setRemarks(remarks);
				}
				String io = null;
				if(request.getParameterValues("io")!=null){
					String[] ioString = request.getParameterValues("io");
					if(ioString.length == 2){
						io = "IO";
					}else{
						io = request.getParameter("io");
					}
					supplier.setIo(io);
				}
				String inner = null;
				if(request.getParameter("inner") != null){
					inner = request.getParameter("inner");
					supplier.setInner(inner);
				} else {
					supplier.setInner("N");
				}
				supplier.setActInd("Y");
				supplier.setCreDate(new Date());
				supplier.setCreUid(userId);
				boolean flag = bookSupplierDAOImpl.addBookSupplier(conn, supplier);
				if(flag){
					conn.commit();
					request.setAttribute("msg", "添加書商資料成功！");
				}else{
					conn.rollback();
					request.setAttribute("msg", "添加書商資料失敗！");
				}
				request.setAttribute("type", "addBookSupplier");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			}else if(type != null && type.equals("edit")){
				BookSupplier supplier = new BookSupplier();
				if(request.getParameter("no") != null){
					supplier.setSupplierNo(Integer.parseInt(request.getParameter("no").toString()));
				}
				if(request.getParameter("code") != null){
					supplier.setSupplierCode(request.getParameter("code"));
				}
				if(request.getParameter("name") != null){
					supplier.setSupplierName(request.getParameter("name"));
				}
				if(request.getParameter("engName") != null){
					supplier.setSupplierEngName(request.getParameter("engName"));
				}
				if(request.getParameter("contactName") != null){
					supplier.setContactName(request.getParameter("contactName"));
				}
				if(request.getParameter("contactTel1") != null){
					supplier.setSupplierTel_1(request.getParameter("contactTel1"));
				}
				if(request.getParameter("contactTel2") != null){
					supplier.setSupplierTel_2(request.getParameter("contactTel2"));
				}
				if(request.getParameter("contactFax1") != null){
					supplier.setSupplierFax_1(request.getParameter("contactFax1"));
				}
				if(request.getParameter("contactFax2") != null){
					supplier.setSupplierFax_2(request.getParameter("contactFax2"));
				}
				if(request.getParameter("email") != null){
					supplier.setSupplierEmail(request.getParameter("email"));
				}
				if(request.getParameter("address") != null){
					supplier.setAddress(request.getParameter("address"));
				}
				if(request.getParameter("resume") != null){
					supplier.setResumeInfo(request.getParameter("resume"));
				}
				if(request.getParameter("remarks") != null){
					supplier.setRemarks(request.getParameter("remarks"));
				}
				String io = null;
				if(request.getParameterValues("io") != null){
					String[] ioString = request.getParameterValues("io");
					int length = ioString.length;
					if(length==2){
						io = "IO";
					}else{
						io = request.getParameter("io");
					}
					supplier.setIo(io);
				}
				if(request.getParameter("inner") != null){
					supplier.setInner(request.getParameter("inner"));
				} else {
					supplier.setInner("N");
				}
				supplier.setActInd("Y");
				supplier.setUpdDate(new Date());
				supplier.setUpdUid(userId);
				boolean flag = bookSupplierDAOImpl.updateBookSupplier(conn, supplier);
				if(flag){
					conn.commit();
					request.setAttribute("msg", "修改書商資料成功！");
				}else{
					conn.rollback();
					request.setAttribute("msg", "修改書商資料失敗！");
				}
				request.setAttribute("type", "editBookSupplier");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			}else if(type!=null && type.equals("searchInfo")){
				BookSupplier supplier = new BookSupplier();
				String code = null;
				if(request.getParameter("code")!=null && !request.getParameter("code").equals("")){
					code = request.getParameter("code");
					supplier.setSupplierCode(code);
				}
				String name = null;
				if(request.getParameter("name")!=null && !request.getParameter("name").equals("")){
					name = request.getParameter("name");
					supplier.setSupplierName(name);
				}
				List bookSuppliersList = bookSupplierDAOImpl.getBookSuppliersList(conn, supplier, Integer.parseInt(start),Integer.parseInt(num));
				List page = bookSupplierDAOImpl.getPage(conn, supplier, start, num);
				double totalPages = Math.ceil(bookSupplierDAOImpl.getRsCount(conn, supplier)/Double.parseDouble(num));
				
				request.setAttribute("totalPages", totalPages);
				request.setAttribute("supplier", supplier);
				request.setAttribute("start", start);
				request.setAttribute("page", page);
				request.setAttribute("bookSuppliersList", bookSuppliersList);
				request.getRequestDispatcher("suppliers.jsp").forward(request, response);
			}else if(type!=null && type.equals("delete")){
				int no = 0;
				if(request.getParameter("no")!=null){
					no = Integer.parseInt(request.getParameter("no"));
				}
				boolean flag = bookSupplierDAOImpl.updateBookSupplierActN(conn, no);
				if(flag){
					conn.commit();
				}else{
					conn.rollback();
				}
				out.print(String.valueOf(flag));
			}else if(type!=null && type.equals("viewInfo")){
				int no = 0;
				if(request.getParameter("no")!=null){
					no = Integer.parseInt(request.getParameter("no"));
				}
				BookSupplier supplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, no);
				request.setAttribute("supplier", supplier);
				request.getRequestDispatcher("supplierInfo.jsp").forward(request, response);
			}else if(type!=null && type.equals("editPage")){
				int no = 0;
				if(request.getParameter("no")!=null){
					no = Integer.parseInt(request.getParameter("no"));
				}
				BookSupplier supplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, no);
				request.setAttribute("supplier", supplier);
				request.getRequestDispatcher("supplierEdit.jsp").forward(request, response);
			}else{
				BookSupplier supplier = new BookSupplier();
				String code = null;
				if(request.getParameter("code")!=null && !request.getParameter("code").equals("")){
					code = request.getParameter("code");
					supplier.setSupplierCode(code);
				}
				String name = null;
				if(request.getParameter("name")!=null && !request.getParameter("name").equals("")){
					name = request.getParameter("name");
					supplier.setSupplierName(name);
				}
				List bookSuppliersList = bookSupplierDAOImpl.getBookSuppliersList(conn, supplier, Integer.parseInt(start),Integer.parseInt(num));
				List page = bookSupplierDAOImpl.getPage(conn, supplier, start, num);
				double totalPages = Math.ceil(bookSupplierDAOImpl.getRsCount(conn, supplier)/Double.parseDouble(num));
				
				request.setAttribute("totalPages", totalPages);
				request.setAttribute("supplier", supplier);
				request.setAttribute("start", start);
				request.setAttribute("page", page);
				request.setAttribute("bookSuppliersList", bookSuppliersList);
				request.getRequestDispatcher("suppliers.jsp").forward(request, response);
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
