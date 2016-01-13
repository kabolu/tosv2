/**
 * 
 */
package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.ContactBean;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Price;
import edu.must.tos.bean.Student;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.ContactDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.PriceDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.mail.SendEmail;

/**
 * @author xfke
 * 2013/10/25
 */
public class EmailNoticeServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public EmailNoticeServlet() {
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
			
			String curIntake = (String)request.getSession().getAttribute("curIntake");
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			
			if(type != null && "DRAWBACK".equals(type)){
				doDrawbackEmail(conn, request, response, curIntake);
			} else if (type != null && "CHANGEPEICE".equals(type)){
				doChangePriceEmail(conn, request, response, curIntake);
			} else if (type != null && "OVERDUE".equals(type)){
				doDateNoticeEmailbyType(conn, request, response, curIntake, type);
			} else if (type != null && "SUPPLEMENT".equals(type)){
				doDateNoticeEmailbyType(conn, request, response, curIntake, type);
			} else if (type != null && "TEACHINGCHANGE".equals(type)){
				doTeachingChangeEmail(conn, request, response, curIntake);
			} else if (type != null && "UNPAIDORDER".equals(type)){
				doOrderNoticeEmailbyType(conn, request, response, curIntake, type);
			} else if (type != null && "CANCEL".equals(type)){
				doOrderNoticeEmailbyType(conn, request, response, curIntake, type);
			} else if (type != null && "PREBOOK".equals(type)){
				doPreBookNoticeEmail(conn, request, response, curIntake, type);
			} else {
				doArrivalEmail(conn, request, response, curIntake);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (Exception sqle) {
				sqle.printStackTrace();
			}
		}
	}

	private void doChangePriceEmail(Connection conn,
			HttpServletRequest request, HttpServletResponse response,
			String curIntake) throws Exception {
		String isbn = null;
		if(request.getParameter("isbn") != null){
			isbn = request.getParameter("isbn");
		}
		
		StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		ContactDAOImpl contactDAOImpl = new ContactDAOImpl();
		PriceDAOImpl priceDAOImpl = new PriceDAOImpl();
		
		String[] studentNos = request.getParameter("studentNos").split(",");
		String[] orderSeqNos = request.getParameter("orderSeqNos").split(",");
		String studentNo = "";
		String orderSeqNo = "";
		List<Student> notSent = new ArrayList<Student>();
		//get book info
		Book book = bookDAOImpl.getBookByPK(conn, isbn);
		//get book price info
		List<Price> priceList = priceDAOImpl.getBookPrice(conn, isbn, curIntake);
		double mopPrice = 0;
		double mopNetPrice = 0;
		for(Price price : priceList){
			if("MOP".equals(price.getCurrency())){
				mopPrice = price.getFuturePrice();
				mopNetPrice = price.getNetPrice();
			}
		}
		for(int i=0; i<studentNos.length; i++){
			studentNo = studentNos[i];
			orderSeqNo = orderSeqNos[i];
			//get student info
			String studParam = studentNo + "," + studentNo;
			List studentList = studentDAOImpl.showStudentDetail(conn, studParam);
			Student student = (Student)studentList.get(0);
			
			//get contact info
			ContactBean contact = contactDAOImpl.getContact(conn, studentNo);
			String toEmail = null;
			if(contact != null){
				toEmail = contact.getEmail();
			} else {
				toEmail = student.getEmail();
			}
			
			StringBuffer sbf = new StringBuffer();
			sbf.append("<tr>"+
				    "<th>ISBN</th>"+
				    "<th>Title</th>"+
				    "<th>預估價(MOP)</th>"+
				    "<th>實價(MOP)</th>"+
				   "</tr>");
			sbf.append("<tr align='center'>"+
				    "<td>" + book.getIsbn() + "</td>"+
				    "<td>" + book.getTitle() + "</td>"+
				    "<td>" + mopPrice + "</td>"+
				    "<td>" + mopNetPrice + "</td>"+
				  "</tr>");
			String bookTable = sbf.toString();
			
			SendEmail sendEmail = new SendEmail(getServletContext(), conn);
			boolean flag = sendEmail.sendTestEmailInfo("PRICECHANGEDNOTICE", student.getChineseName(), bookTable, toEmail); //toEmail
			
			if(!flag){
				notSent.add(student);
			}
		}
		String msg = "";
		if(!notSent.isEmpty()){
			msg = "以下學生";
			for(Student st : notSent){
				msg += st.getChineseName()+"("+ st.getStudentNo() +") ";
			}
			msg += "郵件發送出現異常";
		}else{
			msg = "郵件通知發送完畢！";
		}
		request.setAttribute("notSent", notSent);
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("msg.jsp").forward(request, response);
	}

	private void doDrawbackEmail(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception {
		String isbn = null;
		if(request.getParameter("isbn") != null){
			isbn = request.getParameter("isbn");
		}
		
		StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		ContactDAOImpl contactDAOImpl = new ContactDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		String[] studentNos = request.getParameter("studentNos").split(",");
		String[] orderSeqNos = request.getParameter("orderSeqNos").split(",");
		String studentNo = "";
		String orderSeqNo = "";
		List<Student> notSent = new ArrayList<Student>();
		//get book info
		Book book = bookDAOImpl.getBookByPK(conn, isbn);
		for(int i=0; i<studentNos.length; i++){
			studentNo = studentNos[i];
			orderSeqNo = orderSeqNos[i];
			//get student info
			String studParam = studentNo + "," + studentNo;
			List studentList = studentDAOImpl.showStudentDetail(conn, studParam);
			Student student = (Student)studentList.get(0);
			
			//get contact info
			ContactBean contact = contactDAOImpl.getContact(conn, studentNo);
			String toEmail = null;
			if(contact != null){
				toEmail = contact.getEmail();
			} else {
				toEmail = student.getEmail();
			}			
							
			//get order detail info
			OrDetail detail = new OrDetail();
			detail.setOrderIntake(curIntake);
			detail.setOrderSeqNo(Integer.parseInt(orderSeqNo));
			detail.setIsbn(isbn);
			detail.setActInd("Y");
			List<OrDetail> detailList = orderDetailDAOImpl.getOrDetailInfo(conn, detail);
			detail = detailList.get(0);
			StringBuffer sbf = new StringBuffer();
			if(detailList != null && !detailList.isEmpty()){
				sbf.append("<tr>"+
					    "<th>ISBN</th>"+
					    "<th>Title</th>"+
					    "<th>Amount</th>"+
					   "</tr>");
				sbf.append("<tr align='center'>"+
					    "<td>" + book.getIsbn() + "</td>"+
					    "<td>" + book.getTitle() + "</td>"+
					    "<td>" + detail.getPaidAmount() + "</td>"+
					  "</tr>");
			}
			String bookTable = sbf.toString();
			
			SendEmail sendEmail = new SendEmail(getServletContext(), conn);
			boolean flag = sendEmail.sendTestEmailInfo("DRAWBACKNOTICE", student.getChineseName(), bookTable, toEmail);
			
			if(!flag){
				notSent.add(student);
			}
		}
		String msg = "";
		if(!notSent.isEmpty()){
			msg = "以下學生";
			for(Student st : notSent){
				msg += st.getChineseName()+"("+ st.getStudentNo() +") ";
			}
			msg += "郵件發送出現異常";
		}else{
			msg = "郵件通知發送完畢！";
		}
		request.setAttribute("notSent", notSent);
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("msg.jsp").forward(request, response);
	}

	private void doArrivalEmail(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception {
		String isbn = null;
		if(request.getParameter("isbn") != null){
			isbn = request.getParameter("isbn");
		}
		
		StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		ContactDAOImpl contactDAOImpl = new ContactDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		String[] studentNos = request.getParameter("studentNos").split(",");
		String[] orderSeqNos = request.getParameter("orderSeqNos").split(",");
		String studentNo = "";
		String orderSeqNo = "";
		List<Student> notSent = new ArrayList<Student>();
		for(int i=0; i<studentNos.length; i++){
			studentNo = studentNos[i];
			orderSeqNo = orderSeqNos[i];
			//get student info
			String studParam = studentNo + "," + studentNo;
			List studentList = studentDAOImpl.showStudentDetail(conn, studParam);
			Student student = (Student)studentList.get(0);
			
			//get contact info
			String sNo = student.getStudentNo();
			String aNo = student.getApplicantNo();
			ContactBean contact = contactDAOImpl.getContact(conn, sNo);
			String toEmail = null;
			if(contact != null){
				toEmail = contact.getEmail();
			} else {
				contact = contactDAOImpl.getContact(conn, aNo);
				if(contact != null){
					toEmail = contact.getEmail();
				} else {
					toEmail = student.getEmail();
				}
			}
			//get order detail info
			OrDetail detail = new OrDetail();
			detail.setOrderIntake(curIntake);
			detail.setOrderSeqNo(Integer.parseInt(orderSeqNo));
			detail.setIsbn(isbn);
			detail.setActInd("Y");
			List<OrDetail> detailList = orderDetailDAOImpl.getOrDetailInfo(conn, detail);
			StringBuffer sbf = new StringBuffer();
			if(detailList != null && !detailList.isEmpty()){
				sbf.append("<tr>"+
					    "<th>ISBN</th>"+
					    "<th>書名</th>"+
					    "<th>數量</th>"+
					   "</tr>");
				for(OrDetail od : detailList){
					Book book = bookDAOImpl.getBookByPK(conn, od.getIsbn());
					sbf.append("<tr align='center'>"+
						    "<td>" + book.getIsbn() + "</td>"+
						    "<td>" + book.getTitle() + "</td>"+
						    "<td>" + od.getNotEnoughQty() + "</td>"+
						  "</tr>");
				}
			}
			String bookTable = sbf.toString();
			
			SendEmail sendEmail = new SendEmail(getServletContext(), conn);
			boolean flag = sendEmail.sendTestEmailInfo("ARRIVALNOTICE", student.getChineseName(), bookTable, toEmail);
			
			if(!flag){
				notSent.add(student);
			}
		}
		String msg = "";
		if(!notSent.isEmpty()){
			msg = "以下學生";
			for(Student st : notSent){
				msg += st.getChineseName()+"("+ st.getStudentNo() +") ";
			}
			msg += "郵件發送出現異常";
		}else{
			msg = "郵件通知發送完畢！";
		}
		request.setAttribute("notSent", notSent);
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("msg.jsp").forward(request, response);
	}
	
	private void doTeachingChangeEmail(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake) throws Exception {
		//已更換圖書ISBN
		String isbn = null;
		if(request.getParameter("isbn") != null){
			isbn = request.getParameter("isbn");
		}
		//原圖書ISBN
		String changeisbn = null;
		if(request.getParameter("changeisbn") != null){
			changeisbn = request.getParameter("changeisbn");
		}
		
		StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		PriceDAOImpl priceDAOImpl = new PriceDAOImpl();
		ContactDAOImpl contactDAOImpl = new ContactDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		String[] studentNos = request.getParameter("studentNos").split(",");
		String[] orderSeqNos = request.getParameter("orderSeqNos").split(",");
		String studentNo = "";
		String orderSeqNo = "";
		//無法發送名單
		List<Student> notSent = new ArrayList<Student>();
		//將要發送名單
		Map<String,Student> sentList = new HashMap<String,Student>();
		//將要發送booktable
		Map<String,String> booktableList = new HashMap<String,String>();
		//學號與訂書號
		Map<String,String> orderStudentList = new HashMap<String,String>();
		for(int i=0; i<studentNos.length; i++){
			StringBuffer sbf = new StringBuffer();
			sbf.append("<tr>"+
					"<th>ISBN</th>"+
					"<th>更換教材</th>"+
					"<th>預估價(MOP)</th>"+
					"<th>實價(MOP)</th>"+
			"</tr>");
			studentNo = studentNos[i];
			orderSeqNo = orderSeqNos[i];
			//get student info
			String studParam = studentNo + "," + studentNo;
			List studentList = studentDAOImpl.showStudentDetail(conn, studParam);
			Student student = (Student)studentList.get(0);
							
			//get order detail info
			OrDetail detail = new OrDetail();
			detail.setOrderIntake(curIntake);
			detail.setOrderSeqNo(Integer.parseInt(orderSeqNo));
			if(isbn != null && !"".equals(isbn)){				
				detail.setIsbn(isbn);
			}
			detail.setActInd("Y");
			List<OrDetail> detailList = orderDetailDAOImpl.getOrDetailInfo(conn, detail);
			
			if(detailList != null && !detailList.isEmpty()){
				for(OrDetail od : detailList){
				//get book info
				Book book = bookDAOImpl.getBookByPK(conn, od.getIsbn());
				List<Price> priceList = priceDAOImpl.getBookPrice(conn, isbn, curIntake);
				Double d = 0.0;
				Double n = 0.0;
				if(priceList != null && !priceList.isEmpty()){
					for(Price p : priceList){
						if("MOP".equals(p.getCurrency())){
							d = p.getFuturePrice();
							n = p.getNetPrice();
							break;
						}
					}
				}
				sbf.append("<tr align='center'>"+
					    "<td>" + book.getIsbn() + "</td>"+
					    "<td>" + book.getTitle() + "</td>"+
					    "<td>" + d + "</td>"+
					    "<td>" + n + "</td>"+
					  "</tr>");
				Book changebook = bookDAOImpl.getBookByPK(conn, changeisbn);
				List<Price> changepriceList = priceDAOImpl.getBookPrice(conn, changeisbn, curIntake);
				Double fd = 0.0;
				Double nd = 0.0;
				if(changepriceList != null && !changepriceList.isEmpty()){
					for(Price p : changepriceList){
						if("MOP".equals(p.getCurrency())){
							fd = p.getFuturePrice();
							nd = p.getNetPrice();
							break;
						}
					}
				}
				sbf.append("<tr>"+
						"<th>ISBN</th>"+
						"<th>原訂購教材</th>"+
						"<th>預估價(MOP)</th>"+
						"<th>實價(MOP)</th>"+
				"</tr>");
				sbf.append("<tr align='center'>"+
						"<td>" + changebook.getIsbn() + "</td>"+
						"<td>" + changebook.getTitle() + "</td>"+
						"<td>" + fd + "</td>"+
						"<td>" + nd + "</td>"+
				"</tr>");
				}
			}
			String bookTable = sbf.toString();
			
			//同一訂單不在添加
			if(!orderStudentList.containsKey(orderSeqNo)){
				orderStudentList.put(orderSeqNo, studentNo);
				if(!booktableList.containsKey(studentNo)){
					booktableList.put(studentNo, bookTable);
				}else{
					bookTable +=  booktableList.get(studentNo);
					booktableList.remove(studentNo);
					booktableList.put(studentNo, bookTable);
				}
			}
			
			//同一學生不再通知
			if(!sentList.containsKey(studentNo)){
				sentList.put(studentNo, student);
			}			
		}
        
		for(String s : sentList.keySet()){
			Student student = sentList.get(s);
			//get contact info
			ContactBean contact = contactDAOImpl.getContact(conn, student.getStudentNo());
			String toEmail = null;
			if(contact != null){
				toEmail = contact.getEmail();
			} else {
				toEmail = student.getEmail();
			}
			SendEmail sendEmail = new SendEmail(getServletContext(), conn);
			boolean flag = false;
			flag = sendEmail.sendTestEmailInfo("TEACHINGCHANGENOTICE", student.getChineseName(), booktableList.get(s), toEmail);	
			if(!flag){
				notSent.add(student);
			}
		}

		String msg = "";
		if(!notSent.isEmpty()){
			msg = "以下學生";
			for(Student st : notSent){
				msg += st.getChineseName()+"("+ st.getStudentNo() +") ";
			}
			msg += "郵件發送出現異常";
		}else{
			msg = "郵件通知發送完畢！";
		}
		request.setAttribute("notSent", notSent);
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("msg.jsp").forward(request, response);
		
	}

	private void doOrderNoticeEmailbyType(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake,String type) throws Exception {
		String isbn = null;
		if(request.getParameter("isbn") != null){
			isbn = request.getParameter("isbn");
		}
		
		StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		PriceDAOImpl priceDAOImpl = new PriceDAOImpl();
		ContactDAOImpl contactDAOImpl = new ContactDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		String[] studentNos = request.getParameter("studentNos").split(",");
		String[] orderSeqNos = request.getParameter("orderSeqNos").split(",");
		String[] isbnSeqNos = request.getParameter("isbnSeqNos").split(",");
		String studentNo = "";
		String orderSeqNo = "";
		String isbnSeqNo = "";
		//無法發送名單
		List<Student> notSent = new ArrayList<Student>();
		//將要發送名單
		Map<String,Student> sentList = new HashMap<String,Student>();
		//將要發送booktable
		Map<String,String> booktableList = new HashMap<String,String>();
		//學號與訂書號
		Map<String,String> orderStudentList = new HashMap<String,String>();
		for(int i=0; i<studentNos.length; i++){
			StringBuffer sbf = new StringBuffer();
			sbf.append("<tr>"+
					"<th>ISBN</th>"+
					"<th>Title</th>"+
					"<th>預估價(MOP)</th>"+
					"<th>實價(MOP)</th>"+
			"</tr>");
			studentNo = studentNos[i];
			orderSeqNo = orderSeqNos[i];
			isbnSeqNo = isbnSeqNos[i];

			//get student info
			String studParam = studentNo + "," + studentNo;
			List studentList = studentDAOImpl.showStudentDetail(conn, studParam);
			Student student = (Student)studentList.get(0);
							
			//get order detail info
			OrDetail detail = new OrDetail();
			detail.setOrderIntake(curIntake);
			detail.setOrderSeqNo(Integer.parseInt(orderSeqNo));
			if(isbn != null && !"".equals(isbn)){				
				detail.setIsbn(isbn);
			}else{
				detail.setIsbn(isbnSeqNo);
			}
			detail.setActInd("Y");
			List<OrDetail> detailList = orderDetailDAOImpl.getOrDetailInfo(conn, detail);
			
			//detail = detailList.get(0);
			if(detailList != null && !detailList.isEmpty()){
				for(OrDetail od : detailList){
					
				//get book info
				Book book = bookDAOImpl.getBookByPK(conn, od.getIsbn());
				List<Price> priceList = priceDAOImpl.getBookPrice(conn, isbnSeqNo, curIntake);
				Double d = 0.0;
				Double n = 0.0;
				if(priceList!=null&&!priceList.isEmpty()){
					for(Price p : priceList){
						if("MOP".equals(p.getCurrency())){
							d = p.getFuturePrice();
							n = p.getNetPrice();
							break;
						}
					}
				}
				sbf.append("<tr align='center'>"+
					    "<td>" + book.getIsbn() + "</td>"+
					    "<td>" + book.getTitle() + "</td>"+
					    "<td>" + d + "</td>"+
					    "<td>" + n + "</td>"+
					  "</tr>");
				}
			}
			String bookTable = sbf.toString();
			
			//同一訂單不在添加
			if(!orderStudentList.containsKey(orderSeqNo)){
				orderStudentList.put(orderSeqNo, studentNo);
				if(!booktableList.containsKey(studentNo)){
					booktableList.put(studentNo, bookTable);
				}else{
					bookTable +=  booktableList.get(studentNo);
					booktableList.remove(studentNo);
					booktableList.put(studentNo, bookTable);
				}
			}
			
			//同一學生不再通知
			if(!sentList.containsKey(studentNo)){
				sentList.put(studentNo, student);
			}
			
		}
        
		for(String s : sentList.keySet()){
			Student student = sentList.get(s);
			//get contact info
			ContactBean contact = contactDAOImpl.getContact(conn, student.getStudentNo());
			String toEmail = null;
			if(contact != null){
				toEmail = contact.getEmail();
			} else {
				toEmail = student.getEmail();
			}
			SendEmail sendEmail = new SendEmail(getServletContext(), conn);
			boolean flag = false;
			if(type != null && "UNPAIDORDER".equals(type)){
				flag = sendEmail.sendTestEmailInfo("UNPAIDORDERNOTICE", student.getChineseName(), booktableList.get(s), toEmail);	
			}else if(type != null && "CANCEL".equals(type)){
				flag = sendEmail.sendTestEmailInfo("CANCELNOTICE", student.getChineseName(), booktableList.get(s), toEmail);	
			}
			
			if(!flag){
				notSent.add(student);
			}
		}

		String msg = "";
		if(!notSent.isEmpty()){
			msg = "以下學生";
			for(Student st : notSent){
				msg += st.getChineseName()+"("+ st.getStudentNo() +") ";
			}
			msg += "郵件發送出現異常";
		}else{
			msg = "郵件通知發送完畢！";
		}
		request.setAttribute("notSent", notSent);
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("msg.jsp").forward(request, response);
		
	}
	
	private void doDateNoticeEmailbyType(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake,String type) throws Exception {
		String isbn = null;
		if(request.getParameter("isbn") != null){
			isbn = request.getParameter("isbn");
		}
		
		StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		ContactDAOImpl contactDAOImpl = new ContactDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		String[] studentNos = request.getParameter("studentNos").split(",");
		String[] orderSeqNos = request.getParameter("orderSeqNos").split(",");
		String[] isbnSeqNos = request.getParameter("isbnSeqNos").split(",");
		String studentNo = "";
		String orderSeqNo = "";
		String isbnSeqNo = "";
		//無法發送名單
		List<Student> notSent = new ArrayList<Student>();
		//將要發送名單
		Map<String,Student> sentList = new HashMap<String,Student>();
		//將要發送booktable
		Map<String,String> booktableList = new HashMap<String,String>();
		//學號與訂書號
		Map<String,String> orderStudentList = new HashMap<String,String>();
		for(int i=0; i<studentNos.length; i++){
			StringBuffer sbf = new StringBuffer();
			sbf.append("<tr>"+
					"<th>ISBN</th>"+
					"<th>Title</th>"+
					"<th>Quantiy</th>"+
					"<th>Order number</th>"+
			"</tr>");
			studentNo = studentNos[i];
			orderSeqNo = orderSeqNos[i];
			isbnSeqNo = isbnSeqNos[i];

			//get student info
			String studParam = studentNo + "," + studentNo;
			List studentList = studentDAOImpl.showStudentDetail(conn, studParam);
			Student student = (Student)studentList.get(0);
			
			//get order detail info
			OrDetail detail = new OrDetail();
			detail.setOrderIntake(curIntake);
			detail.setOrderSeqNo(Integer.parseInt(orderSeqNo));
			if(isbn != null && !"".equals(isbn)){				
				detail.setIsbn(isbn);
			}else{
				detail.setIsbn(isbnSeqNo);
			}
			detail.setActInd("Y");
			List<OrDetail> detailList = orderDetailDAOImpl.getOrDetailInfo(conn, detail);
			
			//detail = detailList.get(0);
			if(detailList != null && !detailList.isEmpty()){
				for(OrDetail od : detailList){
					
				//get book info
				Book book = bookDAOImpl.getBookByPK(conn, od.getIsbn());
				sbf.append("<tr align='center'>"+
					    "<td>" + book.getIsbn() + "</td>"+
					    "<td>" + book.getTitle() + "</td>"+
					    "<td>" + od.getConfirmQty() + "</td>"+
					    "<td>" + od.getOrderSeqNo() + "</td>"+
					  "</tr>");
				}
			}
			String bookTable = sbf.toString();
			
			//同一訂單不在添加
			if(!orderStudentList.containsKey(orderSeqNo)){
				orderStudentList.put(orderSeqNo, studentNo);
				if(!booktableList.containsKey(studentNo)){
					booktableList.put(studentNo, bookTable);
				}else{
					bookTable +=  booktableList.get(studentNo);
					booktableList.remove(studentNo);
					booktableList.put(studentNo, bookTable);
				}
			}
			
			//同一學生不再通知
			if(!sentList.containsKey(studentNo)){
				sentList.put(studentNo, student);
			}
			
		}
        
		for(String s : sentList.keySet()){
			Student student = sentList.get(s);
			//get contact info
			ContactBean contact = contactDAOImpl.getContact(conn, student.getStudentNo());
			String toEmail = null;
			if(contact!=null){
				toEmail = contact.getEmail();
			} else {
				toEmail = student.getEmail();
			}
			SendEmail sendEmail = new SendEmail(getServletContext(), conn);
			boolean flag = false;
			if(type != null && "OVERDUE".equals(type)){
				flag = sendEmail.sendTestEmailInfo("OVERDUENOTICE", student.getChineseName(), booktableList.get(s), toEmail);
			}else if(type!=null&&"SUPPLEMENT".equals(type)){
				flag = sendEmail.sendTestEmailInfo("SUPPLEMENTNOTICE", student.getChineseName(), booktableList.get(s), toEmail);	
			}			
			
			if(!flag){
				notSent.add(student);
			}
		}

		String msg = "";
		if(!notSent.isEmpty()){
			msg = "以下學生";
			for(Student st : notSent){
				msg += st.getChineseName()+"("+ st.getStudentNo() +") ";
			}
			msg += "郵件發送出現異常";
		}else{
			msg = "郵件通知發送完畢！";
		}
		request.setAttribute("notSent", notSent);
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("msg.jsp").forward(request, response);
	}
	
	private void doPreBookNoticeEmail(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String curIntake,String type) throws Exception {
		StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		ContactDAOImpl contactDAOImpl = new ContactDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		PriceDAOImpl priceDAOImpl = new PriceDAOImpl();
		
		String[] studentNos = request.getParameter("studentNos").split(",");
		String[] orderSeqNos = request.getParameter("orderSeqNos").split(",");
		String studentNo = "";
		String orderSeqNo = "";
		String isbnSeqNo = "";
		//無法發送名單
		List<Student> notSent = new ArrayList<Student>();
		//將要發送名單
		Map<String,Student> sentList = new HashMap<String, Student>();
		//將要發送booktable
		Map<String,String> booktableList = new HashMap<String,String>();
		//學號與訂書號
		Map<String,String> orderStudentList = new HashMap<String,String>();
		
		String header = "<tr>"+
				"<th>ISBN</th>"+
				"<th>Title</th>"+
				"<th>Quantiy</th>"+
				"<th>預估價(MOP) </th>"+
		"</tr>";
		for(int i=0; i<studentNos.length; i++){
			StringBuffer sbf = new StringBuffer();
			studentNo = studentNos[i];
			orderSeqNo = orderSeqNos[i];

			//get student info
			String studParam = "";
			if(studentNo.length() == 14){
				studParam = "null," + studentNo;
			} else {
				studParam = studentNo + "," + studentNo;
			}
			List studentList = studentDAOImpl.showStudentDetail(conn, studParam);
			Student student = (Student)studentList.get(0);
			
			//get order detail info
			OrDetail detail = new OrDetail();
			detail.setOrderIntake(curIntake);
			detail.setOrderSeqNo(Integer.parseInt(orderSeqNo));
			detail.setIsbn(null);
			detail.setActInd("Y");
			List<OrDetail> detailList = orderDetailDAOImpl.getOrDetailInfo(conn, detail);
			
			//detail = detailList.get(0);
			if(detailList != null && !detailList.isEmpty()){
				for(OrDetail od : detailList){
					
				//get book info
				Book book = bookDAOImpl.getBookByPK(conn, od.getIsbn());
				isbnSeqNo = book.getIsbn();
				List<Price> priceList = priceDAOImpl.getBookPrice(conn, isbnSeqNo, curIntake);
				Double d = 0.0;
				if(priceList!=null&&!priceList.isEmpty()){
					for(Price p : priceList){
						if("MOP".equals(p.getCurrency())){
							d = p.getFuturePrice();
							break;
						}
					}
				}
				sbf.append("<tr align='center'>"+
					    "<td>" + book.getIsbn() + "</td>"+
					    "<td>" + book.getTitle() + "</td>"+
					    "<td>" + od.getConfirmQty() + "</td>"+
					    "<td>" + d + "</td>"+
					  "</tr>");
				}
			}
			String bookTable = sbf.toString();
			

			if(!booktableList.containsKey(studentNo)){
				bookTable = header + bookTable;
				booktableList.put(studentNo, bookTable);
			}else{
				bookTable =  booktableList.get(studentNo)+bookTable;
				booktableList.put(studentNo, bookTable);
			}
			
			//同一學生不再通知
			if(!sentList.containsKey(studentNo)){
				sentList.put(studentNo, student);
			}	
		}
        
		for(String s : sentList.keySet()){
			Student student = sentList.get(s);
			//get contact info
			String tmpStudentNo = student.getStudentNo();
			if(tmpStudentNo == null || "".equals(tmpStudentNo)){
				tmpStudentNo = student.getApplicantNo();
			}
			ContactBean contact = contactDAOImpl.getContact(conn, tmpStudentNo);
			String toEmail = null;
			if(contact!=null){
				toEmail = contact.getEmail();
			} else {
				toEmail = student.getEmail();
			}
			SendEmail sendEmail = new SendEmail(getServletContext(), conn);
			boolean flag = false;
			if(type != null && "PREBOOK".equals(type)){
				flag = sendEmail.sendTestEmailInfo("PREBOOKNOTICE", student.getChineseName(), booktableList.get(s), toEmail);
			}
			
			if(!flag){
				notSent.add(student);
			}
		}

		String msg = "";
		if(!notSent.isEmpty()){
			msg = "以下學生";
			for(Student st : notSent){
				msg += st.getChineseName()+"("+ st.getStudentNo() +") ";
			}
			msg += "郵件發送出現異常";
		}else{
			msg = "郵件通知發送完畢！";
		}
		request.setAttribute("notSent", notSent);
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("msg.jsp").forward(request, response);
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
