package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.must.tos.bean.Book;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.util.CellFormat;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportBookServlet extends HttpServlet {

	public ExportBookServlet() {
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
			
			response.reset();
		    //response.setContentType("application/vnd.ms-excel");
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition" , "attachment ; filename = bookInfoExport.xls " );
		    
		    OutputStream os = response.getOutputStream();
		    
		    List bookList = new ArrayList();
		    BookDAOImpl bookImpl = new BookDAOImpl();
			bookList = bookImpl.getAllBook(conn);
			
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet worksheet = workbook.createSheet("result", 0);
			WritableCellFormat cellFormat = CellFormat.getCellFormat();
			Label isbn = null;
			Label title = null;
			Label author = null;
			Label publisher = null;
			Label publishYear = null;
			Label edition = null;
			
			String[] titleName = {"ISBN編號", "圖書書名", "圖書作者", "出版社", "出版年份", "版次"};
			
			for(int i=0; i<titleName.length; i++){
				 Label label = new Label(i, 0, titleName[i], cellFormat);
				 worksheet.addCell(label);
				 worksheet.setColumnView(i, titleName[i].getBytes().length+2);//設置欄位寬度
			}
			
			for(int i=0;i<bookList.size();i++){
				Book book = (Book)bookList.get(i);
				isbn = new Label(0, i+1, book.getIsbn());
				title = new Label(1, i+1, book.getTitle());
				author = new Label(2, i+1, book.getAuthor());
				publisher = new Label(3, i+1, book.getPublisher());
				publishYear = new Label(4, i+1, book.getPublishYear());
				edition = new Label(5, i+1, book.getEdition());
				
				worksheet.addCell(isbn);
				worksheet.addCell(title);
				worksheet.addCell(author);
				worksheet.addCell(publisher);
				worksheet.addCell(edition);
				worksheet.addCell(publishYear);
			}
			workbook.write();
			workbook.close();
		    
			os.flush();
			os.close();
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
