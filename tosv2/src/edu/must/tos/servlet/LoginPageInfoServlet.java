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
import javax.sql.DataSource;

import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.SysConfigDAOImpl;

public class LoginPageInfoServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public LoginPageInfoServlet() {
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
		this.doPost(request, response);
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
		Connection conn = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			
			String userId = (String)request.getSession().getAttribute("userId");
			
			String oprType = null;
			if(request.getParameter("oprType") != null){
				oprType = (String)request.getParameter("oprType");
			}
			if(oprType != null && oprType.equals("addPage")){
				request.getRequestDispatcher("loginPageInfo_add.jsp").forward(request, response);
			}else if(oprType != null && oprType.equals("editPage")){
				SysConfig config = new SysConfig();
				if(request.getParameter("scType") != null){
					config.setScType(request.getParameter("scType"));
				}
				if(request.getParameter("scKey") != null){
					config.setScKey(request.getParameter("scKey"));
				}
				SysConfig conf = sysConfigDAOImpl.getSysConfig(conn, config);
				request.setAttribute("conf", conf);
				request.getRequestDispatcher("loginPageInfo_edit.jsp").forward(request, response);
			}else if(oprType != null && oprType.equals("addInfo")){
				SysConfig conf = new SysConfig();
				if(request.getParameter("sckey") != null){
					conf.setScKey(request.getParameter("sckey"));
				}
				if(request.getParameter("actind") != null){
					conf.setActInd(request.getParameter("actind"));
				}
				if(request.getParameter("scchndesc") != null){
					conf.setScChnDesc(request.getParameter("scchndesc"));
				}
				if(request.getParameter("scengdesc") != null){
					conf.setScEngDesc(request.getParameter("scengdesc"));
				}
				conf.setScType("REMARK4FP");
				conf.setCreDate(new Date());
				conf.setCreUid(userId);
				conf.setUpdDate(new Date());
				conf.setUpdUid(userId);
				boolean flag = sysConfigDAOImpl.addSysCnnfig(conn, conf);
				if(flag ){
					conn.commit();
					request.setAttribute("msg", "資料保存成功！");
				}else{
					conn.rollback();
					request.setAttribute("msg", "資料保存失敗！");
				}
				request.setAttribute("type", "AddLoginPageInfo");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			}else if(oprType!=null && oprType.equals("updateInfo")){
				SysConfig config = new SysConfig();
				if(request.getParameter("scType") != null){
					config.setScType(request.getParameter("scType"));
				}
				if(request.getParameter("scKey") != null){
					config.setScKey(request.getParameter("scKey"));
				}
				SysConfig conf = sysConfigDAOImpl.getSysConfig(conn, config);
				if(request.getParameter("key") != null){
					conf.setScKey(request.getParameter("key"));
				}
				if(request.getParameter("actind") != null){
					conf.setActInd(request.getParameter("actind"));
				}
				if(request.getParameter("scchndesc") != null){
					conf.setScChnDesc(request.getParameter("scchndesc"));
				}
				if(request.getParameter("scengdesc") != null){
					conf.setScEngDesc(request.getParameter("scengdesc"));
				}
				conf.setUpdDate(new Date());
				conf.setUpdUid(userId);
				boolean flag = sysConfigDAOImpl.updateSysConfig(conn, conf);
				if(flag ){
					conn.commit();
					request.setAttribute("msg", "資料更新成功！");
				}else{
					conn.rollback();
					request.setAttribute("msg", "資料更新失敗！");
				}
				request.setAttribute("type", "AddLoginPageInfo");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			}else{
				SysConfig config = new SysConfig();
				config.setScType("REMARK4FP");
				config.setActInd("Y");
				List list = sysConfigDAOImpl.getSysConfigList(conn, config);
				request.setAttribute("infoList", list);
				request.getRequestDispatcher("loginPageInfo.jsp").forward(request, response);
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
