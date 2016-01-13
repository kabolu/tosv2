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

import edu.must.tos.bean.SysUser;
import edu.must.tos.impl.SysUserDAOImpl;

public class SysUserServlet extends HttpServlet {

	public SysUserServlet() {
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
		HttpSession session = request.getSession();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			SysUserDAOImpl userDAOImpl = new SysUserDAOImpl();
			
			String userId = (String)session.getAttribute("userId");
			String curIntake = (String)session.getAttribute("curIntake");
			
			String type = null;
			if(request.getParameter("type") != null && !request.getParameter("type").equals("")){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("add")){	//添加用戶資料
				addUser(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("modify")){	//轉至用戶資料編輯版面
				modifyUser(conn, request, response);
			}
			else if(type != null && type.equals("update")){	//更新用戶資料
				upddateUser(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("checkUserId")){	//检查用户ID是否存在
				checkUserId(conn, request, response);
			}
			else if(type != null && type.equals("search")){	//根据条件搜寻用户资料列表
				SysUser user = new SysUser();
				if(request.getParameter("userId") != null && !request.getParameter("userId").equals("")){
					user.setUserId(request.getParameter("userId"));
				}
				if(request.getParameter("userName") != null && !request.getParameter("userName").equals("")){
					user.setUserName(request.getParameter("userName"));
				}
				if(request.getParameter("department") != null && !request.getParameter("department").equals("")){
					user.setDepartment(request.getParameter("department"));
				}
				if(request.getParameter("status") != null && !request.getParameter("status").equals("")){
					user.setStatus(request.getParameter("status"));
				}
				if(request.getParameter("role") != null && !request.getParameter("role").equals("")){
					user.setRole(request.getParameter("role"));
				}
				
				List userList = userDAOImpl.getUserInfoByCond(conn, user);
				
				String number = request.getParameter("i");
				int pageNumber = userList.size();
				int maxPage = pageNumber;
				if(maxPage % 10 == 0){
					maxPage = maxPage / 10;
				}else{
					maxPage = maxPage / 10 + 1;
				}
				if(number == null){
					number = "0";
				}
				request.setAttribute("user", user);
				request.setAttribute("number", number);
				request.setAttribute("maxPage", String.valueOf(maxPage));
				request.setAttribute("pageNumber", String.valueOf(pageNumber));
				request.setAttribute("userList", userList);
				request.getRequestDispatcher("userSearch.jsp").forward(request, response);
			}else{	//初始用户资料列表版面
				List userList = userDAOImpl.getAllUserInfo(conn);
				
				String number = request.getParameter("i");
				int pageNumber = userList.size();
				int maxPage = pageNumber;
				if(maxPage % 15 == 0){
					maxPage = maxPage / 15;
				}else{
					maxPage = maxPage / 15 + 1;
				}
				if(number == null){
					number = "0";
				}
				request.setAttribute("number", number);
				request.setAttribute("maxPage", String.valueOf(maxPage));
				request.setAttribute("pageNumber", String.valueOf(pageNumber));
				request.setAttribute("userList", userList);
				request.getRequestDispatcher("userList.jsp").forward(request, response);
			}
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

	private void modifyUser(Connection conn, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SysUserDAOImpl sysUserDAOImpl = new SysUserDAOImpl();
		
		String value = request.getParameter("userId");
		SysUser sysUser = new SysUser();
		sysUser.setUserId(value);
		SysUser user = sysUserDAOImpl.getUserInfoById(conn, sysUser);
		request.setAttribute("user", user);
		request.getRequestDispatcher("userEdit.jsp").forward(request, response);
	}

	private void checkUserId(Connection conn, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		SysUserDAOImpl sysUserDAOImpl = new SysUserDAOImpl();
		
		String value = request.getParameter("value");
		SysUser user = new SysUser();
		user.setUserId(value);
		if(value != null && !value.equals("")){
			boolean flag = sysUserDAOImpl.checkUserId(conn, user);
			if(flag){
				out.print(1);
			}else{
				out.print(0);
			}
		}
	}

	private void upddateUser(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserDAOImpl sysUserDAOImpl = new SysUserDAOImpl();
		
		SysUser user = new SysUser();
		if(request.getParameter("userId")!=null && !request.getParameter("userId").equals("")){
			user.setUserId(request.getParameter("userId"));
		}
		//if(request.getParameter("password")!=null && !request.getParameter("password").equals("")){
		//	user.setPasswd(request.getParameter("password"));
		//}
		if(request.getParameter("userName")!=null && !request.getParameter("userName").equals("")){
			user.setUserName(request.getParameter("userName"));
		}
		if(request.getParameter("department")!=null && !request.getParameter("department").equals("")){
			user.setDepartment(request.getParameter("department"));
		}
		if(request.getParameter("status")!=null && !request.getParameter("status").equals("")){
			user.setStatus(request.getParameter("status"));
		}
		if(request.getParameter("role")!=null && !request.getParameter("role").equals("")){
			user.setRole(request.getParameter("role"));
		}
		if(request.getParameter("remarks")!=null && !request.getParameter("remarks").equals("")){
			user.setRemarks(request.getParameter("remarks"));
		}
		if(request.getParameter("email")!=null && !request.getParameter("email").equals("")){
			user.setEmail(request.getParameter("email"));
		}
		if(request.getParameter("address")!=null && !request.getParameter("address").equals("")){
			user.setAddress(request.getParameter("address"));
		}
		if(request.getParameter("contactNo")!=null && !request.getParameter("contactNo").equals("")){
			user.setContactNo(request.getParameter("contactNo"));
		}
		if(request.getParameter("faxNo")!=null && !request.getParameter("faxNo").equals("")){
			user.setFaxNo(request.getParameter("faxNo"));
		}
		user.setUpddate(new Date());
		user.setUpduid(userId);
		
		boolean flag = sysUserDAOImpl.updateUserInfo(conn, user);
		if(flag){
			conn.commit();
			request.setAttribute("type", "modifyUser");
			request.setAttribute("msg", "用戶信息修改成功！");
		}else{
			conn.rollback();
			request.setAttribute("type", "modifyUser");
			request.setAttribute("msg", "用戶信息修改失敗！");
		}
		request.getRequestDispatcher("msg.jsp").forward(request, response);
	}

	private void addUser(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserDAOImpl sysUserDAOImpl = new SysUserDAOImpl();
		
		SysUser user = new SysUser();
		if(request.getParameter("userId")!=null && !request.getParameter("userId").equals("")){
			user.setUserId(request.getParameter("userId"));
		}
		//if(request.getParameter("password")!=null && !request.getParameter("password").equals("")){
		//	user.setPasswd(request.getParameter("password"));
		//}
		if(request.getParameter("userName")!=null && !request.getParameter("userName").equals("")){
			user.setUserName(request.getParameter("userName"));
		}
		if(request.getParameter("department")!=null && !request.getParameter("department").equals("")){
			user.setDepartment(request.getParameter("department"));
		}
		if(request.getParameter("status")!=null && !request.getParameter("status").equals("")){
			user.setStatus(request.getParameter("status"));
		}
		if(request.getParameter("role")!=null && !request.getParameter("role").equals("")){
			user.setRole(request.getParameter("role"));
		}
		if(request.getParameter("remarks")!=null && !request.getParameter("remarks").equals("")){
			user.setRemarks(request.getParameter("remarks"));
		}
		if(request.getParameter("email")!=null && !request.getParameter("email").equals("")){
			user.setEmail(request.getParameter("email"));
		}
		if(request.getParameter("address")!=null && !request.getParameter("address").equals("")){
			user.setAddress(request.getParameter("address"));
		}
		if(request.getParameter("contactNo")!=null && !request.getParameter("contactNo").equals("")){
			user.setContactNo(request.getParameter("contactNo"));
		}
		if(request.getParameter("faxNo")!=null && !request.getParameter("faxNo").equals("")){
			user.setFaxNo(request.getParameter("faxNo"));
		}
		user.setCredate(new Date());
		user.setCreuid(userId);
		user.setUpddate(new Date());
		user.setUpduid(userId);
		user.setActInd("Y");
		
		boolean flag = sysUserDAOImpl.addUserInfo(conn, user);
		if(flag){
			conn.commit();
			request.setAttribute("type", "addUser");
			request.setAttribute("msg", "用戶信息添加成功！");
		}else{
			conn.rollback();
			request.setAttribute("type", "addUser");
			request.setAttribute("msg", "用戶信息添加失敗！");
		}
		request.getRequestDispatcher("msg.jsp").forward(request, response);
	}

	/**
	 * Initialization of the servlet
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
