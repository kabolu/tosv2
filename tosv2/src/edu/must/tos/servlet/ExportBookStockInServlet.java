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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import edu.must.tos.bean.BookStockInBean;
import edu.must.tos.impl.BookStockInDAOImpl;
import edu.must.tos.impl.BookSupplierDAOImpl;
import edu.must.tos.util.CellFormat;

public class ExportBookStockInServlet extends HttpServlet {

	public ExportBookStockInServlet() {
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
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			BookStockInDAOImpl bookStockInDAOImpl = new BookStockInDAOImpl ();
			
			String type = null;
			if(request.getParameter("type") != null ){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("search")){
				String prNum = null;
				if(request.getParameter("prNum") != null && !request.getParameter("prNum").equals("")){
					prNum = request.getParameter("prNum");
				}
				String fromDate = null;
				if(request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")){
					fromDate = request.getParameter("fromDate");
				}
				String toDate = null;
				if(request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")){
					toDate = request.getParameter("toDate");
				}
				String paidStatus = null;
				if(request.getParameter("paidStatus") != null && !request.getParameter("paidStatus").equals("")){
					paidStatus = request.getParameter("paidStatus");
				}
				int supplierNo = 0;
				if(request.getParameter("supplier") != null && !request.getParameter("supplier").equals("")){
					supplierNo = Integer.parseInt(request.getParameter("supplier"));
				}
				List list = bookStockInDAOImpl.getBookStockInInfoList(conn, prNum, fromDate, toDate, paidStatus, supplierNo);
				if(list.isEmpty()){
					request.setAttribute("flag", "false");
					request.getRequestDispatcher("exportBookStockIn.jsp").forward(request, response);
				}else{
					response.reset();
					response.setContentType("application/octet-stream");
					response.addHeader("Content-Disposition" , "attachment ; filename = BookStockInResult.xls " );
					
					OutputStream os = response.getOutputStream();
					
					WritableWorkbook workbook = Workbook.createWorkbook(os);
					WritableSheet worksheet = workbook.createSheet("result", 0);
					WritableCellFormat cellFormat = CellFormat.getCellFormat();
					
					String[] titleName={"書商","學期","入貨單編號","入貨日期","付款狀態","付款日期","付款幣種","發票日期","圖書ISBN",
							"圖書書名","作者","出版社","版次","出版年","定價","折扣","入貨價","入貨數","金額","備注"};
					for(int i=0; i<titleName.length; i++){
						 Label label = new Label(i, 0, titleName[i], cellFormat);
						 worksheet.addCell(label);
						 worksheet.setColumnView(i, titleName[i].getBytes().length+2);
					}
					for(int j=0; j<list.size(); j++){
						BookStockInBean bookStockInBean = (BookStockInBean)list.get(j);
						Label supplierName = new Label(0, j+1, bookStockInBean.getSupplierName());
						Label intake = new Label(1, j+1, bookStockInBean.getIntake());
						Label prnum = new Label(2, j+1, bookStockInBean.getPrNum());
						Label indate = new Label(3, j+1, bookStockInBean.getIndate());
						
						String status = "待付";
						String paidDate = "";
						String currency = "";
						if(bookStockInBean.getPaidStatus().equals("Y")){
							status = "已付";
						}
						if(bookStockInBean.getPaiddate() != null){
							paidDate = bookStockInBean.getPaiddate();
						}
						if(bookStockInBean.getPaidCurrency() != null){
							currency = bookStockInBean.getPaidCurrency();
						}
						Label paidstatus = new Label(4, j+1, status);
						Label paiddate = new Label(5, j+1, paidDate);
						Label paidcurrency = new Label(6, j+1, currency);
						Label invoiceDate = new Label(7, j+1, bookStockInBean.getInvoiceDate());
						Label isbn = new Label(8, j+1, bookStockInBean.getIsbn());
						Label bookName = new Label(9, j+1, bookStockInBean.getBook().getTitle());
						Label author = new Label(10, j+1, bookStockInBean.getBook().getAuthor());
						Label publisher = new Label(11, j+1, bookStockInBean.getBook().getPublisher());
						Label edition = new Label(12, j+1, bookStockInBean.getBook().getEdition());
						Label publishYear = new Label(13, j+1, bookStockInBean.getBook().getPublishYear());
						jxl.write.Number purchasePrice = new jxl.write.Number(14, j+1, bookStockInBean.getPurchasePrice());
						jxl.write.Number discount = new jxl.write.Number(15, j+1, bookStockInBean.getDiscount());
						jxl.write.Number costPrice = new jxl.write.Number(16, j+1, bookStockInBean.getCostPrice());
						jxl.write.Number adjNum = new jxl.write.Number(17, j+1, bookStockInBean.getAdjNum());
						jxl.write.Number totalPrice = new jxl.write.Number(18, j+1, bookStockInBean.getTotalPrice());
						Label remarks = new Label(19, j+1, bookStockInBean.getRemarks());
						
						worksheet.addCell(remarks);
						worksheet.addCell(totalPrice);
						worksheet.addCell(adjNum);
						worksheet.addCell(costPrice);
						worksheet.addCell(discount);
						worksheet.addCell(purchasePrice);
						worksheet.addCell(publishYear);
						worksheet.addCell(edition);
						worksheet.addCell(publisher);
						worksheet.addCell(author);
						worksheet.addCell(bookName);
						worksheet.addCell(isbn);
						worksheet.addCell(invoiceDate);
						worksheet.addCell(paidcurrency);
						worksheet.addCell(paiddate);
						worksheet.addCell(paidstatus);
						worksheet.addCell(supplierName);
						worksheet.addCell(intake);
						worksheet.addCell(prnum);
						worksheet.addCell(indate);
					}
					workbook.write();
					workbook.close();
				    
					os.flush();
					os.close();
			    }
			}else{
				BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
				List supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
				request.setAttribute("supplierList", supplierList);
				request.getRequestDispatcher("exportBookStockIn.jsp").forward(request, response);
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
