package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Stocktake;
import edu.must.tos.bean.StocktakeRecord;
import edu.must.tos.impl.StocktakeDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.util.CellFormat;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportStocktakeServlet extends HttpServlet {

	public ExportStocktakeServlet() {
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
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();	//獲取當前學期
			StocktakeDAOImpl stocktakeDAOImpl = new StocktakeDAOImpl();
			
			String key = "CURRINTAKE";
			String curIntake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			String type = null;
			if(request.getParameter("type")!=null){
				type = request.getParameter("type");
			}
			if(type!=null && type.equals("export")){
				String fromDate = "";
				if(request.getParameter("fromDate")!=null){
					fromDate = request.getParameter("fromDate");
				}
				String toDate = "";
				if(request.getParameter("toDate")!=null){
					toDate = request.getParameter("toDate");
				}
				List stocktakeRecordList = stocktakeDAOImpl.getStocktakeRecord(conn, fromDate, toDate, curIntake);
				if(stocktakeRecordList.isEmpty()){
					request.setAttribute("flag", "false");
					Stocktake stocktake = new Stocktake();
					List<Stocktake> stocktakeList = stocktakeDAOImpl.getStocktakeList(conn, stocktake);
					
					HashSet set = new HashSet();
					List timeList = new ArrayList();
					if(!stocktakeList.isEmpty()){
						for(Stocktake s : stocktakeList){
							if(!set.contains(s.getUpddate())){
								set.add(s.getUpddate());
								timeList.add(s.getUpddate());
							}
						}
					}
					request.setAttribute("timeList", timeList);
					request.getRequestDispatcher("stocktakeRecord.jsp").forward(request, response);
				}else{
					response.reset();
					response.setContentType("application/octet-stream");
					response.addHeader("Content-Disposition" , "attachment ; filename = StocktakeRecord.xls " );
					
					OutputStream os = response.getOutputStream();
					WritableWorkbook workbook = Workbook.createWorkbook(os);
					
					WritableSheet worksheet = workbook.createSheet("盤點記錄", 0);
					//表格式樣
					WritableCellFormat cellFormat = CellFormat.getCellFormat();
					
					String[] title = {"書名","作者","版次","出版年","出版社","ISBN","上期剩餘庫存數","總入貨數","當前庫存數","實際庫存數"};
					for(int i=0; i<title.length; i++){
						 Label titleLabel = new Label(i, 0, title[i], cellFormat);
						 worksheet.addCell(titleLabel);
						 worksheet.setColumnView(i, title[i].getBytes().length+2);
					}
					
					for(int i=0; i<stocktakeRecordList.size(); i++){
						int j = 0;
						StocktakeRecord sr = (StocktakeRecord)stocktakeRecordList.get(i);
						
						Label bookTitle = new Label(j++, i+1, sr.getTitle());
						Label author = new Label(j++, i+1, sr.getAuthor());
						Label edition = new Label(j++, i+1, sr.getEdition());
						Label publishYear = new Label(j++, i+1, sr.getPublishYear());
						Label publisher = new Label(j++, i+1, sr.getPublisher());						
						Label isbn = new Label(j++, i+1, sr.getIsbn());
						jxl.write.Number after_stocktake1 = new jxl.write.Number(j++, i+1, sr.getAfter_stocktake1());
						jxl.write.Number adjnumsum = new jxl.write.Number(j++, i+1, sr.getAdjnumsum());
						jxl.write.Number original_stock = null;
						if(sr.getOriginal_stock()==0){
							original_stock = new jxl.write.Number(j++, i+1, sr.getStock());
						}else{
							original_stock = new jxl.write.Number(j++, i+1, sr.getOriginal_stock());
						}
						
						jxl.write.Number after_stocktake2 = null;
						if(sr.getAfter_stocktake2()==0){
							after_stocktake2 = new jxl.write.Number(j++, i+1, sr.getStock());
						}else{
							after_stocktake2 = new jxl.write.Number(j++, i+1, sr.getAfter_stocktake2());
						}
						
						
						worksheet.addCell(bookTitle);
						worksheet.addCell(author);
						worksheet.addCell(edition);
						worksheet.addCell(publishYear);
						worksheet.addCell(publisher);
						worksheet.addCell(isbn);
						worksheet.addCell(after_stocktake1);
						worksheet.addCell(adjnumsum);
						worksheet.addCell(original_stock);
						worksheet.addCell(after_stocktake2);
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
						if(!set.contains(s.getUpddate())){
							set.add(s.getUpddate());
							timeList.add(s.getUpddate());
						}
					}
				}
				request.setAttribute("timeList", timeList);
				request.getRequestDispatcher("stocktakeRecord.jsp").forward(request, response);
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
