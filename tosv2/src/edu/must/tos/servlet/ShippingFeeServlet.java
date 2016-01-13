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
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import edu.must.tos.bean.ShippingFee;
import edu.must.tos.impl.ShippingFeeDAOImpl;

public class ShippingFeeServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ShippingFeeServlet() {
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
			
			ShippingFeeDAOImpl shippingFeeDAOImpl = new ShippingFeeDAOImpl();
			
			String userId = (String)request.getSession().getAttribute("userId");
			
			String curIntake = (String)request.getSession().getAttribute("curIntake");
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("edit")){
				int feeNo = 0;
				if(request.getParameter("feeNo") != null){
					feeNo = Integer.parseInt(request.getParameter("feeNo"));
				}
				String company = null;
				if(request.getParameter("company") != null){
					company = request.getParameter("company");
				}
				String prnum = null;
				if(request.getParameter("prnum") != null){
					prnum = request.getParameter("prnum");
				}
				String feeType = null;
				if(request.getParameter("feeType") != null){
					feeType = request.getParameter("feeType");
				}
				String rmb = null;
				if(request.getParameter("rmb") != null){
					rmb = request.getParameter("rmb");
				}
				String mop = null;
				if(request.getParameter("mop") != null ){
					mop = request.getParameter("mop");
				}
				String toMop = null;
				if(request.getParameter("toMop") != null){
					toMop = request.getParameter("toMop");
				}
				String invoiceDate = null;
				if(request.getParameter("invoiceDate") != null){
					invoiceDate = request.getParameter("invoiceDate");
				}
				String remarks = null;
				if(request.getParameter("remarks") != null){
					remarks = request.getParameter("remarks");
				}
				boolean flag = false;
				ShippingFee fee = null;
				if(feeNo != 0){
					fee = shippingFeeDAOImpl.getShippingFeeByFeeNo(conn, feeNo);
					fee.setCompanyName(company);
					fee.setPrNum(prnum);
					fee.setFeeType(feeType);
					if(rmb != null && !"".equals(rmb)){
						fee.setRmbPrice(Double.parseDouble(rmb));
					}else{
						fee.setRmbPrice(0);
					}
					if(mop != null && !"".equals(mop)){
						fee.setMopPrice(Double.parseDouble(mop));
					}else{
						fee.setMopPrice(0);
					}
					if(toMop != null && !"".equals(toMop)){
						fee.setToMopPrice(Double.parseDouble(toMop));
					}else{
						fee.setToMopPrice(0);
					}
					fee.setRemarks(remarks);
					fee.setInvoiceDate(invoiceDate);
					fee.setUpdDate(new Date());
					fee.setUpdUid(userId);
					flag = shippingFeeDAOImpl.updateShippingFee(conn, fee);
				}else{
					fee = new ShippingFee();
					fee.setFeeNo(feeNo);
					fee.setIntake(curIntake);
					fee.setCompanyName(company);
					fee.setPrNum(prnum);
					fee.setFeeType(feeType);
					if(rmb != null && !"".equals(rmb)){
						fee.setRmbPrice(Double.parseDouble(rmb));
					}else{
						fee.setRmbPrice(0);
					}
					if(mop != null && !"".equals(mop)){
						fee.setMopPrice(Double.parseDouble(mop));
					}else{
						fee.setMopPrice(0);
					}
					if(toMop != null && !"".equals(toMop)){
						fee.setToMopPrice(Double.parseDouble(toMop));
					}else{
						fee.setToMopPrice(0);
					}
					fee.setRemarks(remarks);
					fee.setInvoiceDate(invoiceDate);
					fee.setActind("Y");
					fee.setCreDate(new Date());
					fee.setCreUid(userId);
					fee.setUpdDate(new Date());
					fee.setUpdUid(userId);
					flag = shippingFeeDAOImpl.addShippingFee(conn, fee);
				}
				if(flag ){
					conn.commit();
					request.setAttribute("msg", "運費資料錄入成功！");
					request.setAttribute("type", "shippingFee");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}else{
					conn.rollback();
					request.setAttribute("msg", "運費資料錄入失敗！");
					request.setAttribute("type", "shippingFee");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}
			}else if(type != null && type.equals("forward")){
				int feeNo = 0;
				if(request.getParameter("feeNo") != null && !"0".equals(request.getParameter("feeNo"))){
					feeNo = Integer.parseInt(request.getParameter("feeNo"));
				}
				ShippingFee shippingFee = null;
				if(feeNo != 0){
					shippingFee = shippingFeeDAOImpl.getShippingFeeByFeeNo(conn, feeNo);
				}
				request.setAttribute("shippingFee", shippingFee);
				request.getRequestDispatcher("shippingFeeEdit.jsp").forward(request, response);
			}else if(type != null && type.equals("report")){
				response.reset();
				response.setContentType("application/octet-stream");
				response.addHeader("Content-Disposition" , "attachment ; filename = ShippingFeeInfo.xls " );
				
				OutputStream os = response.getOutputStream();
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				
				WritableSheet worksheet = workbook.createSheet("運費信息表", 0);
				
				WritableCellFormat cellFormat = new WritableCellFormat();
				cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
				cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				cellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
				
				//Title
				worksheet.mergeCells(0, 0, 0, 1);
				worksheet.addCell(new Label(0, 0, "運輸公司名稱", cellFormat));
				worksheet.mergeCells(1, 0, 1, 1);
				worksheet.addCell(new Label(1, 0, "單號", cellFormat));
				worksheet.mergeCells(2, 0, 5, 0);
				worksheet.addCell(new Label(2, 0, "費用", cellFormat));
				worksheet.addCell(new Label(2, 1, "類型", cellFormat));
				worksheet.addCell(new Label(3, 1, "RMB", cellFormat));
				worksheet.addCell(new Label(4, 1, "MOP", cellFormat));
				worksheet.addCell(new Label(5, 1, "折成MOP", cellFormat));
				worksheet.mergeCells(6, 0, 6, 1);
				worksheet.addCell(new Label(6, 0, "發標日期", cellFormat));
				worksheet.mergeCells(7, 0, 7, 1);
				worksheet.addCell(new Label(7, 0, "操作者", cellFormat));
				worksheet.mergeCells(8, 0, 8, 1);
				worksheet.addCell(new Label(8, 0, "備註", cellFormat));
				
				//Data
				//List list = shippingFeeDAOImpl.getShippingFeeList(conn, curIntake);
				//window.location.href='ShippingFeeServlet?type=report'
				String fromDate = null;
				if(request.getParameter("fromDate") != null){
					fromDate = request.getParameter("fromDate");
				}
				String toDate = null;
				if(request.getParameter("toDate") != null){
					toDate = request.getParameter("toDate");
				}
				String intake = null;
				if(request.getParameter("intake") != null){
					intake = request.getParameter("intake");
				}
				if(intake == null)
					intake = curIntake;
				List list = shippingFeeDAOImpl.getShippingFeeList(conn, intake, fromDate, toDate);
				if(list != null && !list.isEmpty()){
					for(int i=0; i<list.size(); i++){
						ShippingFee fee = (ShippingFee)list.get(i);
						int j = 0;
						worksheet.addCell(new Label(j++, i+2, fee.getCompanyName()));
						worksheet.addCell(new Label(j++, i+2, fee.getPrNum()));
						worksheet.addCell(new Label(j++, i+2, fee.getFeeType()));
						worksheet.addCell(new Number(j++, i+2, fee.getRmbPrice()));
						worksheet.addCell(new Number(j++, i+2, fee.getMopPrice()));
						worksheet.addCell(new Number(j++, i+2, fee.getToMopPrice()));
						worksheet.addCell(new Label(j++, i+2, fee.getInvoiceDate()));
						worksheet.addCell(new Label(j++, i+2, fee.getUpdUid()));
						worksheet.addCell(new Label(j++, i+2, fee.getRemarks()));
					}
				}
				workbook.write();
				workbook.close();
			    
				os.flush();
				os.close();
			} else {
				//每頁記錄數
			    String num = "10";
				//分頁從第幾條記錄開始取數據
				String start = request.getParameter("start");
				if(start == null || start.equals("")){
					start = "0";
				}
				if(start.equals("page")) {
					start = "0";
				}
				
				String fromDate = null;
				if(request.getParameter("fromDate") != null){
					fromDate = request.getParameter("fromDate");
				}
				String toDate = null;
				if(request.getParameter("toDate") != null){
					toDate = request.getParameter("toDate");
				}
				String intake = null;
				if(request.getParameter("intake") != null){
					intake = request.getParameter("intake");
				}
				if(intake == null)
					intake = curIntake;
				List list = shippingFeeDAOImpl.getShippingFeeList(conn, intake, fromDate, toDate, Integer.parseInt(start), Integer.parseInt(num));
				List page = shippingFeeDAOImpl.getpage(conn, intake, fromDate, toDate, start, num);
				double totalPages = Math.ceil(shippingFeeDAOImpl.getRsCount(conn, intake, fromDate, toDate)/Double.parseDouble(num));
				request.setAttribute("totalPages", totalPages);
			    request.setAttribute("fromDate", fromDate);
			    request.setAttribute("toDate", toDate);
			    request.setAttribute("intake", intake);
			    request.setAttribute("start", start);
			    request.setAttribute("page", page);
			    request.setAttribute("shippingFeeList", list);
				request.getRequestDispatcher("shippingFee.jsp").forward(request,response);
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
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
