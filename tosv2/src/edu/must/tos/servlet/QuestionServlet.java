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

import edu.must.tos.bean.Question;
import edu.must.tos.impl.QuestionDAOImpl;

public class QuestionServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public QuestionServlet() {
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
		HttpSession session = request.getSession();
		Connection conn = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String userId = (String)request.getSession().getAttribute("userId");
			
			String type = null;
			if(request.getParameter("type") != null && !"".equals(request.getParameter("type"))){
				type = request.getParameter("type");
			}
			if(type != null && "editQuestion".equals(type)){
				editQuestion(conn, userId, request, response);
			}
			else if(type != null && "getQuestion".equals(type)){
				getQuestion(conn, request, response);
			}
			else{
				getQuestionList(conn, request, response);
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

	private void getQuestion(Connection conn, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/**
		 * XML
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("ContentType","text/xml");
		*/
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		int id = 0;
		if(request.getParameter("id") != null && !request.getParameter("id").equals("0")){
			id = Integer.parseInt(request.getParameter("id"));
		}
		Question ques = new Question();
		ques.setId(id);
		QuestionDAOImpl questionDAOImpl = new QuestionDAOImpl();
		Question question = questionDAOImpl.getQuestionByPK(conn, ques);
		out.print(question.getQuestion()+"&"+question.getAnswer()+"&"+question.getActInd()+"&");
		/*
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<results>");
		xml.append("<result>");
		xml.append("<question>" + question.getQuestion() + "</question>");
		xml.append("<answer>" + question.getAnswer() + "</answer>");
		xml.append("<actind>" + question.getActInd() + "</actind>");
		xml.append("</result>");
		xml.append("</results>");
		out.print(xml.toString());
		*/
	}

	private void getQuestionList(Connection conn, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QuestionDAOImpl questionDAOImpl = new QuestionDAOImpl();
		
		List questionList = questionDAOImpl.getQuestionList(conn, null);
		
		request.getSession().setAttribute("questionList", questionList);
		request.getRequestDispatcher("questionList.jsp").forward(request, response);
	}

	private void editQuestion(Connection conn, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		QuestionDAOImpl questionDAOImpl = new QuestionDAOImpl();
		
		int editId = 0;
		if(request.getParameter("editId")!=null && !"0".equals(request.getParameter("editId"))){
			editId = Integer.parseInt(request.getParameter("editId"));
		}		
		String question = "";
		if(request.getParameter("question")!=null && !"".equals(request.getParameter("question"))){
			question = request.getParameter("question");
		}
		String answer = "";
		if(request.getParameter("answer")!=null && !"".equals(request.getParameter("answer"))){
			answer = request.getParameter("answer");
		}
		String actind = "";
		if(request.getParameter("actind")!=null && !"".equals(request.getParameter("actind"))){
			actind = request.getParameter("actind");
		}
		Question ques = new Question();
		ques.setId(editId);
		ques.setQuestion(question);
		ques.setAnswer(answer);
		ques.setActInd(actind);
		ques.setCreDate(new Date());
		ques.setCreUid(userId);
		ques.setUpdDate(new Date());
		ques.setUpdUid(userId);
		boolean flag = true;
		if(editId == 0)
			flag = questionDAOImpl.insertQuestion(conn, ques);
		else
			flag = questionDAOImpl.updateQuestion(conn, ques);
		if(flag){
			out.print(0);
		}else{
			out.print(1);
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
