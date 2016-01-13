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
import java.util.Vector;

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
import edu.must.tos.bean.BookPurchasingBean;
import edu.must.tos.bean.BookSummaryBean;
import edu.must.tos.bean.Stocktake;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookPurchasingDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StocktakeDAOImpl;
import edu.must.tos.util.CellFormat;

public class StatisticByBookServlet extends HttpServlet {

	public StatisticByBookServlet() {
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
			
			String searchIntake = request.getParameter("intake");
			//訂書日期
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
			//代購日期
			String pFromDate = request.getParameter("pFromDate");
			String pToDate = request.getParameter("pToDate");
			
			String type = null;
			if(request.getParameter("type") != null && !request.getParameter("type").equals("")){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("summary")){
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Date fDate = sf.parse(fromDate);
				Date pDate = sf.parse(pFromDate);
				String beforeTime = "";
				if(fDate.compareTo(pDate) >= 0){
					beforeTime = sf.format(fDate);
				}else{
					beforeTime = sf.format(pDate);
				}
				exportSummary(conn, request, response, searchIntake, from, to, pFromDate, pToDate, beforeTime);
			}
			else if(type != null && type.equals("page")){
				forwardPage(conn, request, response);
			}
			else{
				exportRpt(conn, request, response, searchIntake, from, to);
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

	private void exportRpt(Connection conn, HttpServletRequest request,
			HttpServletResponse response, String searchIntake, String from, String to) throws Exception {
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		Vector v = new Vector();
	    v = bookDAOImpl.getStatisticByBook(conn, searchIntake, from, to);
	    if(v.isEmpty()){
	    	request.setAttribute("flag", "false");
	    	request.getRequestDispatcher("statisticByBook.jsp").forward(request, response);
	    }else{
			response.reset();
		    response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition" , "attachment ; filename = StatisticByBookResult.xls " );
		    
		    OutputStream os = response.getOutputStream();
		    
		    Vector inner = new Vector();
		    
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet worksheet = workbook.createSheet("result", 0);
			
			WritableCellFormat cellFormat = CellFormat.getCellFormat();
			
			String[] titleName = {"圖書編號","書名","作者","出版商","出版年份","版本","圖書語言",
					"備注","書商1","書商2","訂單學期",
					"確定數量","退訂數量","預付單價(MOP)","預付單價(RMB)","實付單價(MOP)","實付單價(RMB)",
					"退訂單價(MOP)","退訂單價(RMB)","確定預付總金額(MOP)","確定預付總金額(RMB)","確定實付總金額(MOP)",
					"確定實付總金額(RMB)","退訂總金額(MOP)","退訂總金額(RMB)",
					/*
					"未確定數量","未確定預付總金額(MOP)","未確定預付總金額(RMB)","未確定實付總金額(MOP)","未確定實付總金額(RMB)",
					*/
					};
			String value = "";
			Label label = null;
			jxl.write.Number number = null;
			for(int i=0; i<titleName.length; i++){
				 label = new Label(i, 0, titleName[i], cellFormat);
				 worksheet.addCell(label);
				 worksheet.setColumnView(i, titleName[i].getBytes().length+2);
			}
			
			for(int i=0; i<v.size(); i++){
				inner = (Vector)v.get(i);
				for(int j=0; j<inner.size(); j++){
					if(j==0 || j==1 || j==2 || j==3 || j==4 || j==5 || j==6 || j==7 || j==8 || j==9 || j==10 ){
						value = (String)inner.get(j);
						label = new Label(j, i+1, value);
						worksheet.addCell(label);
					} else if (j==11 || j==12) {
						Integer num = (Integer)inner.get(j);
						number = new jxl.write.Number(j, i+1, num.intValue());
						worksheet.addCell(number);
					} else {
						Double price = (Double)inner.get(j);
						number = new jxl.write.Number(j, i+1, price.doubleValue());
						worksheet.addCell(number);
					}
				}
			}
			
			workbook.write();
			workbook.close();
		    
			os.flush();
			os.close();
	    }
	}

	private void forwardPage(Connection conn, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StocktakeDAOImpl stocktakeDAOImpl = new StocktakeDAOImpl();
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
		request.getRequestDispatcher("statisticSummaryByBook.jsp").forward(request, response);
	}

	private void exportSummary(Connection conn, HttpServletRequest request, HttpServletResponse response,
			String searchIntake, String from, String to, String pFromDate, String pToDate, String beforeTime) throws Exception {
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		BookPurchasingDAOImpl bookPurchasingDAOImpl = new BookPurchasingDAOImpl();
		StocktakeDAOImpl stocktakeDAOImpl = new StocktakeDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		String paidFromDate = request.getParameter("paidFromDate");
		if(paidFromDate != null && !"".equals(paidFromDate)){
			paidFromDate = paidFromDate + " 00:00:00";
		}
		String paidToDate = request.getParameter("paidToDate");
		if(paidToDate != null && !"".equals(paidToDate)){
			paidToDate = paidToDate + " 23:59:59";
		}
		
		String stockFormDate = null;
		if(request.getParameter("stockFormDate") != null){
			stockFormDate = request.getParameter("stockFormDate");
		}
		
		List resultList = bookDAOImpl.getStatisticSummaryByBook(conn, searchIntake, from, to, paidFromDate, paidToDate);
	    //tbookpurchasing info
	    List isbnList = new ArrayList();
	    List<BookPurchasingBean> list = bookPurchasingDAOImpl.getBookPurchasingSummByIntake(conn, searchIntake, pFromDate, pToDate);
	    for(BookPurchasingBean bean : list){
	    	isbnList.add(bean.getBook().getIsbn());
	    }
	    //tstocktake info
	    List<Stocktake> stocktakeList = stocktakeDAOImpl.getStocktakeByUpdata(conn, stockFormDate);
	    List stocktakeIsbnList = new ArrayList();
	    for(Stocktake st : stocktakeList){
	    	stocktakeIsbnList.add(st.getIsbn());
	    }
	    if((resultList == null || resultList.isEmpty()) && (list == null || list.isEmpty())){
	    	request.setAttribute("flag", "false");
	    	request.getRequestDispatcher("statisticSummaryByBook.jsp").forward(request, response);
	    } else {
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition" , "attachment ; filename = StatisticSummaryByBookResult.xls " );
		    
		    OutputStream os = response.getOutputStream();
		    				    
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet worksheet = workbook.createSheet("圖書訂購信息簡表", 0);
			WritableCellFormat cellFormat = CellFormat.getCellFormat();
			
			worksheet.mergeCells(0, 0, 20, 0);
			String searchStr = "訂書日期:" + from + " ~ " + to + "\n" + "    代購日期:" + pFromDate + " ~ " + pToDate ;
			if(paidFromDate != null && !"".equals(paidFromDate) && paidToDate != null && !"".equals(paidToDate)){
				searchStr += "    付款日期:" + paidFromDate + " ~ " + paidToDate;
			}
			worksheet.addCell(new Label(0, 0, searchStr, cellFormat));
			worksheet.setRowView(0, 500);
			
			String[] titleName = {"圖書編號","書名","作者","出版商","出版年份","版本","圖書語言","備注","書商1","書商2",
					"訂單學期","學生訂書數量","未領書數量","代購數量","週期期初數量","剩餘期初數量","入貨幣種","入貨單價","入貨折扣","優惠價"};
			
			Label label = null;
			for(int i=0; i<titleName.length; i++){
				 label = new Label(i, 1, titleName[i], cellFormat);
				 worksheet.addCell(label);
				 worksheet.setColumnView(i, titleName[i].getBytes().length+2);
			}
			List indexList = new ArrayList();
			for(int i=0; i<resultList.size(); i++){
				int j = 0;
				BookSummaryBean bean = (BookSummaryBean)resultList.get(i);
				if(isbnList.contains(bean.getBook().getIsbn())){
					int index = isbnList.indexOf(bean.getBook().getIsbn());
					BookPurchasingBean bp = (BookPurchasingBean)list.get(index);
					bean.setQuantity(bp.getBookPurchase().getQuantity());
					indexList.add(index);
				}
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getIsbn()));
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getTitle()));
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getAuthor()));
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getPublisher()));
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getPublishYear()));
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getEdition()));
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getLanguage()));
				//worksheet.addCell(new Label(j++, i+2, bean.getBook().getBookType()));
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getRemarks()));
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getSupplierCode1()));
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getSupplierCode2()));
				worksheet.addCell(new Label(j++, i+2, bean.getOrderIntake()));
				worksheet.addCell(new jxl.write.Number(j++, i+2, bean.getConfirmQty()));
				worksheet.addCell(new jxl.write.Number(j++, i+2, bean.getUnconfirmTake()));
				worksheet.addCell(new jxl.write.Number(j++, i+2, bean.getQuantity()));
				j = j + 2;
				worksheet.addCell(new Label(j++, i+2, bean.getBook().getCurrency()));
				worksheet.addCell(new jxl.write.Number(j++, i+2, bean.getBook().getUnitPrice()));				
				worksheet.addCell(new jxl.write.Number(j++, i+2, bean.getBook().getDisCount()));
				worksheet.addCell(new jxl.write.Number(j++, i+2, bean.getBook().getFavourablePrice()));
			}
			if(list != null && !list.isEmpty()){
				int row = resultList.size() + 1;
				for(int i=0; i<list.size(); i++){
					int j = 0;
					if((resultList.size() != indexList.size()) || resultList.size()==0){
						if(!indexList.contains(i)){
							row++;
							BookPurchasingBean bean = (BookPurchasingBean)list.get(i);
							worksheet.addCell(new Label(j++, row, bean.getBook().getIsbn()));
							worksheet.addCell(new Label(j++, row, bean.getBook().getTitle()));
							worksheet.addCell(new Label(j++, row, bean.getBook().getAuthor()));
							worksheet.addCell(new Label(j++, row, bean.getBook().getPublisher()));
							worksheet.addCell(new Label(j++, row, bean.getBook().getPublishYear()));
							worksheet.addCell(new Label(j++, row, bean.getBook().getEdition()));
							worksheet.addCell(new Label(j++, row, bean.getBook().getLanguage()));
							//worksheet.addCell(new Label(j++, row, bean.getBook().getBookType()));
							worksheet.addCell(new Label(j++, row, bean.getBookPurchase().getRemarks()));
							worksheet.addCell(new Label(j++, row, bean.getBook().getSupplierCode1()));
							worksheet.addCell(new Label(j++, row, bean.getBook().getSupplierCode2()));
							worksheet.addCell(new Label(j++, row, bean.getBookPurchase().getIntake()));
							worksheet.addCell(new jxl.write.Number(j++, row, 0));
							worksheet.addCell(new jxl.write.Number(j++, row, 0));
							worksheet.addCell(new jxl.write.Number(j++, row, bean.getBookPurchase().getQuantity()));
							j = j + 2;
							worksheet.addCell(new Label(j++, row, bean.getBook().getCurrency()));
							worksheet.addCell(new jxl.write.Number(j++, row, bean.getBook().getUnitPrice()));
							worksheet.addCell(new jxl.write.Number(j++, row, bean.getBook().getDisCount()));
							worksheet.addCell(new jxl.write.Number(j++, row, bean.getBook().getFavourablePrice()));
						}
					}
				}
			}
			
			
			if(stocktakeList != null && !stocktakeList.isEmpty()){
				int rows = worksheet.getRows();
				if(rows != 0){
					for(int i=0; i<rows; i++){
						String isbn = worksheet.getCell(0, i+2).getContents();
						String intake = worksheet.getCell(11, i+2).getContents();
						if(stocktakeIsbnList.contains(isbn)){
							int index = stocktakeIsbnList.indexOf(isbn);
							//週期期初數量
							int quantity = stocktakeList.get(index).getAfter_stocktake();
							worksheet.addCell(new jxl.write.Number(14, i+2, quantity));
							//時間前的訂書數量
							int befConfirmQty = orderDetailDAOImpl.getBefOrderQty(conn, searchIntake, isbn, beforeTime);
							//時間前的代購數量
							int befQuantity = bookPurchasingDAOImpl.getbefPurchaseQty(conn, searchIntake, isbn, beforeTime);
							//填寫最後一欄上的‘剩餘期初數量’的訊息
							worksheet.addCell(new jxl.write.Number(15, i+2, (quantity - befQuantity - befConfirmQty)));
							System.out.println(quantity - befQuantity - befConfirmQty);
						}
					}
				}
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
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
