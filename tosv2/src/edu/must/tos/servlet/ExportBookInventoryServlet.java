package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import edu.must.tos.bean.BookInventoryRecord;
import edu.must.tos.bean.Stocktake;
import edu.must.tos.impl.BookInventoryDAOImpl;
import edu.must.tos.impl.StocktakeDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.util.CellFormat;

public class ExportBookInventoryServlet extends HttpServlet {

	public ExportBookInventoryServlet() {
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
			
			BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			StocktakeDAOImpl stocktakeDAOImpl = new StocktakeDAOImpl();
			
			String key = "CURRINTAKE";
			String intake = sysConfigDAOImpl.getCurIntake(conn, key);
			//intake = "1502";
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("Export")){
				String fromDate = "";
				if(request.getParameter("fromDate") != null){
					fromDate = request.getParameter("fromDate");
				}
				List recordList = bookInventoryDAOImpl.getBookInventoryRecord(conn, intake, fromDate);
				List isbnList = new ArrayList();
				HashSet isbnSet = new HashSet();
				List newList = new ArrayList();
				if(recordList != null && !recordList.isEmpty()){
					for(int i=0; i<recordList.size(); i++){
						BookInventoryRecord record1 = (BookInventoryRecord)recordList.get(i);
						BookInventoryRecord record2 = null;
						if((i+1) >= recordList.size())
							record2 = null;
						else 
							record2 = (BookInventoryRecord)recordList.get(i+1);
						if(record2 != null && record1.getIsbn().equals(record2.getIsbn())){
							if(record1.getAdjnumSum()!=0 && record2.getAdjnumSum() == 0){
								newList.add(record1);
							}else if(record1.getAdjnumSum() == 0 && record2.getAdjnumSum() != 0){
								newList.add(record2);
							}else if(record1.getAdjnumSum() == 0 && record2.getAdjnumSum() == 0){
								newList.add(record1);
							}
							isbnSet.add(record1.getIsbn());
						}else{
							if(!isbnSet.contains(record1.getIsbn())){
								newList.add(record1);
							}
						}
					}
				}
				
				if(newList.isEmpty()){
					request.setAttribute("msg", "圖書庫存記錄為空，請檢查！");
					request.setAttribute("type", "inventoryRecord");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}else {
					response.reset();
					response.setContentType("application/octet-stream");
					response.addHeader("Content-Disposition" , "attachment ; filename = BookInventoryInfo.xls " );
					
					OutputStream os = response.getOutputStream();
					WritableWorkbook workbook = Workbook.createWorkbook(os);
					
					WritableSheet worksheet = workbook.createSheet("圖書庫存信息表", 0);
					
					//表格式樣
					WritableCellFormat cellFormat = CellFormat.getCellFormat();
					
					worksheet.mergeCells(0, 0, 2, 0);
					
					worksheet.mergeCells(3, 0, 5, 0);
					
					worksheet.mergeCells(6, 0, 13, 0);
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					worksheet.addCell(new Label(0, 0, "盤點日期："+fromDate, cellFormat));
					worksheet.addCell(new Label(3, 0, "當前日期："+sf.format(new Date()), cellFormat));
					worksheet.addCell(new Label(6, 0, "學期："+intake, cellFormat));
					
					String[] title = {"學期","圖書ISBN","圖書書名","入貨書商","期初數量","總入貨數","總出貨數","當前存量","訂購書數(付款)","訂購書數(未付款)","已領書數","入貨單價","入貨幣種","入貨折扣","優惠價"};
					for(int i=0; i<title.length; i++){
						 Label titleLabel = new Label(i, 1, title[i], cellFormat);
						 worksheet.addCell(titleLabel);
						 worksheet.setColumnView(i, title[i].getBytes().length+2);
					}
					for(int i=0; i<newList.size(); i++){
						int j = 0;
						BookInventoryRecord record = (BookInventoryRecord)newList.get(i);
						worksheet.addCell(new Label(j++, i+2, record.getIntake()));
						worksheet.addCell(new Label(j++, i+2, record.getIsbn()));
						worksheet.addCell(new Label(j++, i+2, record.getTitle()));
						worksheet.addCell(new Label(j++, i+2, record.getSupplierName()));
						worksheet.addCell(new jxl.write.Number(j++, i+2, record.getStocktake()));
						worksheet.addCell(new jxl.write.Number(j++, i+2, record.getAdjnumSum()));
						worksheet.addCell(new jxl.write.Number(j++, i+2, record.getOutSum()));
						worksheet.addCell(new jxl.write.Number(j++, i+2, record.getStock()));
						worksheet.addCell(new jxl.write.Number(j++, i+2, record.getPaidYSum()));
						worksheet.addCell(new jxl.write.Number(j++, i+2, record.getPaidNSum()));
						worksheet.addCell(new jxl.write.Number(j++, i+2, (record.getReceiveNum() + record.getReceiveANum())));
						if(record.getCurrency() != null){
							worksheet.addCell(new jxl.write.Number(j++, i+2, record.getUnitPrice()));
							worksheet.addCell(new Label(j++, i+2, record.getCurrency()));
							worksheet.addCell(new jxl.write.Number(j++, i+2, record.getDisCount()));
							worksheet.addCell(new jxl.write.Number(j++, i+2, record.getFavourablePrice()));
						}
					}
					workbook.write();
					workbook.close();
				    
					os.flush();
					os.close();
				}
			}else{
				Stocktake stocktake = new Stocktake();
				List<Stocktake> stocktakeList = stocktakeDAOImpl.getStocktakeList(conn, stocktake);
				
				HashSet set = new HashSet();
				List timeList = new ArrayList();
				if(!stocktakeList.isEmpty()){
					for(Stocktake s : stocktakeList){
						if(!set.contains(s.getStock_date())){
							set.add(s.getStock_date());
							timeList.add(s.getStock_date());
						}
					}
				}
				request.setAttribute("timeList", timeList);
				request.getRequestDispatcher("inventoryRecord.jsp").forward(request, response);
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

	public void init() throws ServletException {
		// Put your code here
	}

}
