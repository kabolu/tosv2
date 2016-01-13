package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import edu.must.tos.bean.WithdrawBean;
import edu.must.tos.impl.WithdrawDAOImpl;

public class WithdrawServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public WithdrawServlet() {
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
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String userId = (String)request.getSession().getAttribute("userId");
			
			String searchIntake = request.getParameter("intake");
			String from = "";
			String fromDate = request.getParameter("fromDate");
			if(fromDate != null && !fromDate.equals("")){
				from = fromDate + " 00:00:00";
			}
			String to = "";
			String toDate = request.getParameter("toDate");
			if(toDate != null && !toDate.equals("")){
				to = toDate + " 23:59:59";
			}
			String isbnParam = "";
			if(request.getParameter("isbn") != null && !request.getParameter("isbn").equals("")){
				isbnParam = request.getParameter("isbn");
			}
			String causeParam = "";
			if(request.getParameter("cause") != null && !request.getParameter("cause").equals("")){
				causeParam = request.getParameter("cause");
				if(causeParam.equals("NoBookStorage")){
					causeParam = "無貨退書";
				}
				if(causeParam.equals("StudentDo")){
					causeParam = "學生退書";
				}
			}
			String type = null;
			if(request.getParameter("type") != null && !request.getParameter("type").equals("")){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("search")){
				searchResultXLS(conn, searchIntake, userId, request, response, from, to, isbnParam, causeParam);
			}else{
		    	request.getRequestDispatcher("statisticWithdrawBook.jsp").forward(request, response);
			}
		}catch (Exception e) {
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

	private void searchResultXLS(Connection conn, String searchIntake,
			String userId, HttpServletRequest request, HttpServletResponse response, String from, String to, String isbnParam, String causeParam) throws Exception {
		WithdrawDAOImpl withdrawDAOImpl = new WithdrawDAOImpl();
		List resultList = withdrawDAOImpl.getWithdrawList(conn, searchIntake, from, to, isbnParam, causeParam);
		if(resultList.isEmpty()){
	    	request.setAttribute("flag", "false");
	    	request.getRequestDispatcher("statisticWithdrawBook.jsp").forward(request, response);
	    }else{
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition" , "attachment ; filename = StatisticWithdrawBookResult.xls " );
		    
		    OutputStream os = response.getOutputStream();
		    				    
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet worksheet = workbook.createSheet("WithdrawBookResult", 0);
			
			String[] titleName = {"學期", "學生編號", "學生姓名", "ISBN", "書名", "退訂書", "退書原因", "退書日期", "操作用戶"};
			Label label = null;
			for(int i=0; i<titleName.length; i++){
				 label = new Label(i, 0, titleName[i]);
				 worksheet.addCell(label);
			}
			DateFormat customDateFormat = new DateFormat ("yyyy-MM-dd hh:mm:ss"); 
			WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat); 
			for(int i=0; i<resultList.size(); i++){
				int j = 0;
				WithdrawBean bean = (WithdrawBean)resultList.get(i);						
				worksheet.addCell(new Label(j++, i+1, bean.getOrderIntake()));
				worksheet.addCell(new Label(j++, i+1, bean.getStudentNo()));
				worksheet.addCell(new Label(j++, i+1, bean.getStudentName()));
				worksheet.addCell(new Label(j++, i+1, bean.getIsbn()));
				worksheet.addCell(new Label(j++, i+1, bean.getBookName()));
				worksheet.addCell(new jxl.write.Number(j++, i+1, bean.getWithdrawQty()));
				worksheet.addCell(new Label (j++, i+1, bean.getCause()));
				worksheet.addCell(new jxl.write.DateTime(j++, i+1, (Date)bean.getCreDate(), dateFormat));
				worksheet.addCell(new Label(j++, i+1, bean.getCreUid()));
			}
			workbook.write();
			workbook.close();
		    
			os.flush();
			os.close();
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
