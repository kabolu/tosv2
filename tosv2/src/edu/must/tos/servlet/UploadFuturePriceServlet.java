package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import edu.must.tos.bean.Price;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.PriceDAOImpl;

public class UploadFuturePriceServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UploadFuturePriceServlet() {
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
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			FileItem fileItem = null;
			RequestContext requestContext = new ServletRequestContext(request);
			if ( ServletFileUpload.isMultipartContent(requestContext) ) {
				DiskFileItemFactory factory = new  DiskFileItemFactory();
				factory.setSizeThreshold( 1024 * 1024 );
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax( 2*1024 * 1024 );
				List items = null ;
				items = upload.parseRequest(requestContext);
				for ( int i = 0 ; i < items.size(); i++ ) {
					FileItem fi = (FileItem)items.get(i);
					String fileName = fi.getName().substring(fi.getName().lastIndexOf("\\")+1, fi.getName().length());
                    
					String extName = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
					
					if( !"xls".equalsIgnoreCase(extName) ) {
						request.setAttribute("msg", "Require The Excel File !");
						request.setAttribute("type", "uploadFuturePrice");
						request.getRequestDispatcher("msg.jsp").forward(request, response);
						break;
					}
					if (! fi.isFormField()) {
						fileItem  =  fi;
						break ;
					}
				}
 				boolean format = getExcelFormat(fileItem);
 				if(format){
 					List resultErrYearAndPrice = getCheckPrice(conn,fileItem);
 	                List errIsbnFormatList = getCheckIsbnFormat(fileItem);
 	 				if( !resultErrYearAndPrice.isEmpty() || !errIsbnFormatList.isEmpty()){
 	 					request.setAttribute("errYearAndPrice", resultErrYearAndPrice);
 	 					request.setAttribute("errIsbnFormat", errIsbnFormatList);
 	 					request.setAttribute("type", "uploadFuturePrice");
 	 					request.getRequestDispatcher("errExcel.jsp").forward(request, response);
 	 				}else{
 	 					readExcel( conn, fileItem, request );
 	 					
 	 					HttpSession session = request.getSession();
 	 					List price = (List)session.getAttribute("price");
 	 					PriceDAOImpl priceImpl = new PriceDAOImpl();
 	 					int temp = priceImpl.getUpdateFuturePrice(conn, price);
 	 					if(temp > 0){
 	 						conn.commit();
 	 						request.setAttribute("msg", "Books futureprice update success!");
 	 						request.setAttribute("type", "uploadFuturePrice");
 	 						request.getRequestDispatcher("msg.jsp").forward(request, response);
 	 					}else{
 	 						conn.rollback();
 	 						request.setAttribute("msg", "Books future price update faile!");
 	 						request.setAttribute("type", "uploadFuturePrice");
 	 						request.getRequestDispatcher("msg.jsp").forward(request, response);
 	 					}
 	 				}
 				}else{
 					request.setAttribute("msg", "The excel format is wrong!");
					request.setAttribute("type", "uploadFuturePrice");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
 				}
				
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
	 * read the excel file
	 * @param conn
	 * @param fi
	 * @param request
	 * @throws Exception
	 */
	private void readExcel(Connection conn,FileItem fi,HttpServletRequest request) throws Exception {
		Workbook workbook = null ;
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        String intake = (String)session.getAttribute("curIntake");
			
        try {
        	workbook = Workbook.getWorkbook(fi.getInputStream());
            Sheet sheet = workbook.getSheet( 0 );
           
            List price = new ArrayList();
            for( int a=1; a<sheet.getRows(); a++ ){
	           	int j = 0;
	           	if ( sheet.getCell(j,a).getContents() == null || sheet.getCell(j,a).getContents().equals("") ) {
	           		break;
	           	}
	           	
    			for(int k=0; k<2; k++) {
    				Price p1 = new Price();
        			p1.setIsbn(sheet.getCell(j, a).getContents().trim().replace(" ", "").replace("\\n\\r", ""));
        			if ( k == 0 )
        				p1.setCurrency("MOP");
        			else
        				p1.setCurrency("RMB");
        			p1.setFuturePrice(new Double(sheet.getCell(k+6, a).getContents().trim().replace("\\n\\r", "")));
        			p1.setCreUid(userId);
        			p1.setUpdUid(userId);
        			p1.setActInd("Y");
        			p1.setIntake(intake);
        			price.add(p1);
    			}
    		 }
            session.setAttribute("price", price);
        } catch (Exception e){
            throw  e;
        }finally {
            if (workbook != null ){
               workbook.close();
            }
        }
   }	
	
	private List getExcelIsbn(FileItem fi){
 		List isbnList = null;
 		Workbook workbook = null;
 		try{
 			workbook = Workbook.getWorkbook(fi.getInputStream());
 		    Sheet sheet = workbook.getSheet(0);
 		    isbnList = new ArrayList();
 		    for(int i=1; i<sheet.getRows(); i++){
 		    	isbnList.add(sheet.getCell(0,i).getContents().trim().replace(" ", "").replace("\\n\\r", ""));
 		    }
 		    return isbnList;
 		}catch(Exception e){
 			e.printStackTrace();
 			return null;		
 		}finally {
             if (workbook != null ){
                workbook.close();
             }
        }
 	}
	
	private List getCheckIsbnFormat(FileItem fi){
 		List formatList = new ArrayList();
 		List isbnList = getExcelIsbn(fi);
 		for(int i=0; i<isbnList.size(); i++){
 			if(!isbnList.get(i).toString().matches("^[0-9A-Za-z$]*$") ){
 				formatList.add(i);
 			}
 		}
 		return formatList;
 	}
	
	private List getCheckPrice(Connection conn, FileItem fi){
 		List resultList = null;
 		Workbook workbook = null;
 		BookDAOImpl bookDAOImpl = new BookDAOImpl();
 		try{
 			workbook = Workbook.getWorkbook(fi.getInputStream());
 		    Sheet sheet = workbook.getSheet(0);
 		    int rows = sheet.getRows();
 		    
			List list = bookDAOImpl.getIsbn(conn);
 		    
 		    resultList = new ArrayList();
 		    for( int i=1; i<rows; i++ ){
 		    	String isbn = sheet.getCell(0, i).getContents().trim().replace(" ", "").replace("\\n\\r", "");
 		    	String mopPrice = sheet.getCell(6, i).getContents().trim().replace("\\n\\r", "");
 		    	String rmbprice = sheet.getCell(7, i).getContents().trim().replace("\\n\\r", "");
 		    	if( checkPrice(mopPrice) == true && checkPrice(rmbprice) == true && list.contains(isbn) == true ){
 		    		
 		    	}else{
 		    		resultList.add(i);
 		    	}
 		    }
 		}catch(Exception e){
 			e.printStackTrace();
 		}finally {
             if (workbook != null ){
                workbook.close();
             }
 		}
 		return resultList;
 	}
	
	private boolean getExcelFormat(FileItem fi){
		boolean flag = false;
		Workbook workbook = null;
		try{
			workbook = Workbook.getWorkbook(fi.getInputStream());
			Sheet sheet = workbook.getSheet(0);
			for( int i=1; i<sheet.getRows(); i++ ){
 		    	String isbn = sheet.getCell(0, i).getContents();
 		    	String mopPrice = sheet.getCell(6, i).getContents();
 		    	String rmbprice = sheet.getCell(7, i).getContents();
 		    	if( mopPrice != null && !mopPrice.equals("") && rmbprice != null && !rmbprice.equals("") 
 		    			&& isbn != null && !isbn.equals("") ){
 		    		flag = true;
 		    	}else{
 		    		flag = false;
 		    		break;
 		    	}
 		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally {
             if (workbook != null ){
                workbook.close();
             }
 		}
		return flag;
	}
	
	private boolean checkPrice(String price) {
		String in = java.lang.String.valueOf(price);
		if(in == null || in.length() == 0){
			return false;
		}else{
			int j = 0;
			for (int i = 0; i < in.length(); i++) {
				if (in.charAt(i) < '0' || in.charAt(i) > '9') {
					if (in.charAt(i) != '.')
						return false;
					else {
						String str = in.substring(0, in.indexOf("."));
	       			    if(str.length() >= 6){
	       			    	return false;
	           		    }else{
	           		    	j++;
	               			if (j > 1 || (j == in.length() - 1))
	               				return false;
	           			}
					}
				}
			}
		}
		return true;
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
