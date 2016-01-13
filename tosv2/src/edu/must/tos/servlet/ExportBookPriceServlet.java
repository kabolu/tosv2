package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

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
import edu.must.tos.impl.PriceDAOImpl;

import edu.must.tos.util.CellFormat;

public class ExportBookPriceServlet extends HttpServlet {

	public ExportBookPriceServlet() {
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
			
			HttpSession session = request.getSession();
			String intake = (String)session.getAttribute("curIntake");
			
			response.reset();
		    //response.setContentType("application/vnd.ms-excel");
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition" , "attachment ; filename = bookPriceExport.xls " );
		    
		    OutputStream os = response.getOutputStream();
		    
		    Vector v = new Vector();
		    Vector inner = new Vector();
		    PriceDAOImpl priceImpl = new PriceDAOImpl();
		    v = priceImpl.getPrice(conn, intake);
		    
		    WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet worksheet = workbook.createSheet("result", 0);
			WritableCellFormat cellFormat = CellFormat.getCellFormat();
			//rainbow add
			String[] titleName = {"ISBN編號", "圖書書名", "圖書作者", "出版社", "出版年份", "版次", "圖書語言", "備註", "退書標識",
					"書商編號1", "書商編號2", "補訂標識", "預估價(MOP)", "預估價(RMB)", "實價(MOP)", "實價(RMB)", "入貨幣種", "入貨單價", "入貨折扣", "優惠價"};
			
			String value = "";
			Label label = null;
			jxl.write.Number number = null;
			for(int i=0; i<titleName.length; i++){
				 label = new Label(i, 0, titleName[i], cellFormat);
				 worksheet.addCell(label);
				 worksheet.setColumnView(i, titleName[i].getBytes().length + 2);
			}
			for(int i=0; i<v.size(); i++){
				inner = (Vector)v.get(i);
				for(int j=0; j<inner.size(); j++){
					if(j==inner.size()-1 || j==inner.size()-2 || j==inner.size()-3 || j==inner.size()-5 || j==inner.size()-6 || j==inner.size()-7 || j==inner.size()-8 ){
						Double price = (Double)inner.get(j);
						if(price != null){
							number = new jxl.write.Number(j, i+1, price.doubleValue());
						} else {
							number = new jxl.write.Number(j, i+1, 0);
						}
						worksheet.addCell(number);
					} else {
						value = (String)inner.get(j);
						label = new Label(j, i+1, value);
						worksheet.addCell(label);
					}
				}
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
