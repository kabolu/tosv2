package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
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
import edu.must.tos.bean.BookStockOutBean;
import edu.must.tos.impl.BookStockOutDAOImpl;
import edu.must.tos.impl.BookSupplierDAOImpl;
import edu.must.tos.util.CellFormat;

public class ExportBookStockOutServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ExportBookStockOutServlet() {
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
			
			String curIntake = (String)request.getSession().getAttribute("curIntake");
			String type = null;
			if(request.getParameter("type") != null ){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("export")){
				BookStockOutDAOImpl bookStockOutDAOImpl = new BookStockOutDAOImpl();
				String intake = null;
				if(request.getParameter("intake") != null && !request.getParameter("intake").equals("")){
					intake = request.getParameter("intake");
				}
				if(intake == null){
					intake = curIntake;
				}
				String beginDate = null;
				if(request.getParameter("beginDate") != null && !request.getParameter("beginDate").equals("")){
					beginDate = request.getParameter("beginDate");
				}
				String toDate = null;
				if(request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")){
					toDate = request.getParameter("toDate");
				}
				String prnum = null;
				if(request.getParameter("prnum") != null && !request.getParameter("prnum").equals("")){
					prnum = request.getParameter("prnum");
				}
				String supplier = null;
				if(request.getParameter("supplier") != null && !request.getParameter("supplier").equals("")){
					supplier = request.getParameter("supplier");
				}
				List list = bookStockOutDAOImpl.getBookStockOurReport(conn, intake, beginDate, toDate, prnum, supplier);
				List detailList = bookStockOutDAOImpl.getStockOutInfoList(conn, intake, prnum, beginDate, toDate, null, supplier==null?0:Integer.parseInt(supplier));
				if(list != null && !list.isEmpty()){
					response.reset();
					response.setContentType("application/octet-stream");
					response.addHeader("Content-Disposition" , "attachment ; filename = BookStockOutSummary.xls " );
					
					OutputStream os = response.getOutputStream();
					WritableWorkbook workbook = Workbook.createWorkbook(os);
					WritableCellFormat cellFormat = CellFormat.getCellFormat();
					
					WritableSheet worksheet = workbook.createSheet("圖書出庫匯總表", 0);
					
					worksheet.mergeCells(0, 0, 8, 0);					
					if(beginDate!=null && toDate != null){
						worksheet.addCell(new Label(0, 0, "搜索日期為：" + beginDate + "~" + toDate, cellFormat));
					}else{
						worksheet.addCell(new Label(0, 0, "學期為 " + intake + " 資料", cellFormat));
					}
					
					String[] titleName={"書商/學院","ISBN","書名","作者","出版社","出版年","版次","出貨總冊數","總金額"};
					for(int i=0; i<titleName.length; i++){
						 Label label = new Label(i, 1, titleName[i], cellFormat);
						 worksheet.addCell(label);
						 worksheet.setColumnView(i, titleName[i].getBytes().length+2);
					}
					for(int i=0; i<list.size(); i++){
						BookStockOutBean bookStockOutBean = (BookStockOutBean)list.get(i);
						int j = 0;
						worksheet.addCell(new Label(j++, i+2, bookStockOutBean.getSupplierName()==null?bookStockOutBean.getSupplierEngName():bookStockOutBean.getSupplierName()));
						worksheet.addCell(new Label(j++, i+2, bookStockOutBean.getBook().getIsbn()));
						worksheet.addCell(new Label(j++, i+2, bookStockOutBean.getBook().getTitle()));
						worksheet.addCell(new Label(j++, i+2, bookStockOutBean.getBook().getAuthor()));
						worksheet.addCell(new Label(j++, i+2, bookStockOutBean.getBook().getPublisher()));
						worksheet.addCell(new Label(j++, i+2, bookStockOutBean.getBook().getPublishYear()));
						worksheet.addCell(new Label(j++, i+2, bookStockOutBean.getBook().getEdition()));
						worksheet.addCell(new Number(j++, i+2, bookStockOutBean.getQuantity()));
						worksheet.addCell(new Number(j++, i+2, bookStockOutBean.getAmount()));
					}
					
					WritableSheet worksheet1 = workbook.createSheet("圖書出庫詳細表", 1);
					String[] titleName1 = {"學期","出貨單編號","書商/學院","出貨日期","出貨幣種","ISBN","書名","作者","出版社","出版年","版次","定價","折扣","出貨價","出貨數","出貨總金額"};
					for(int i=0; i<titleName1.length; i++){
						 Label label = new Label(i, 0, titleName1[i], cellFormat);
						 worksheet1.addCell(label);
						 worksheet1.setColumnView(i, titleName1[i].getBytes().length+2);
					}
					for(int j=0; j<detailList.size(); j++){
						BookStockOutBean bean = (BookStockOutBean)detailList.get(j);
						int i = 0;
						worksheet1.addCell(new Label(i++, j+1, bean.getIntake()));
						worksheet1.addCell(new Label(i++, j+1, bean.getPrNum()));
						worksheet1.addCell(new Label(i++, j+1, bean.getSupplierName()));
						if(bean.getIsp().equals("Y"))
							worksheet1.addCell(new Label(i++, j+1, bean.getIspDate()));
						else
							worksheet1.addCell(new Label(i++, j+1, bean.getOutDate()));
						worksheet1.addCell(new Label(i++, j+1, bean.getPaidCurrency()));
						worksheet1.addCell(new Label(i++, j+1, bean.getIsbn()));
						worksheet1.addCell(new Label(i++, j+1, bean.getBook().getTitle()));
						worksheet1.addCell(new Label(i++, j+1, bean.getBook().getAuthor()));
						worksheet1.addCell(new Label(i++, j+1, bean.getBook().getPublisher()));
						worksheet1.addCell(new Label(i++, j+1, bean.getBook().getPublishYear()));
						worksheet1.addCell(new Label(i++, j+1, bean.getBook().getEdition()));
						worksheet1.addCell(new Number(i++, j+1, bean.getPurchasePrice()));
						worksheet1.addCell(new Number(i++, j+1, bean.getDisCount()));
						worksheet1.addCell(new Number(i++, j+1, bean.getCostPrice()));
						worksheet1.addCell(new Number(i++, j+1, bean.getAdjNum()));
						worksheet1.addCell(new Number(i++, j+1, (bean.getCostPrice()*bean.getAdjNum())));
					}
					workbook.write();
					workbook.close();
				    
					os.flush();
					os.close();
				}else{
					request.setAttribute("msg", "圖書出庫匯總記錄為空，請檢查！");
					request.setAttribute("type", "stockOut");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}
			}else{
				BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
				List supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
				request.setAttribute("supplierList", supplierList);
				request.getRequestDispatcher("bookStockOutSummary.jsp").forward(request, response);
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
