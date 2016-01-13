package edu.must.tos.servlet;

import java.io.IOException;
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

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookSupplier;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookSupplierDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;

public class UploadUpdBookServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UploadUpdBookServlet() {
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
    throws  ServletException, IOException {
		Connection conn = null;    
		try{
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			FileItem fileItem = null;
			RequestContext requestContext = new ServletRequestContext(request);
			if (ServletFileUpload.isMultipartContent(requestContext)) {
				DiskFileItemFactory factory = new  DiskFileItemFactory();
				factory.setSizeThreshold( 1024 * 1024 );
				ServletFileUpload upload = new ServletFileUpload(factory);
				
				List items = null ;
				items = upload.parseRequest(requestContext);
				for ( int i = 0 ; i < items.size(); i++ ) {
					FileItem fi = (FileItem)items.get(i);
					
					String fileName = fi.getName().substring(fi.getName().lastIndexOf("\\")+1, fi.getName().length());
		         
					String extName = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());   
		            
					if(!"xls".equalsIgnoreCase(extName)) {
						request.setAttribute("msg", "Require the excel file!");
						request.setAttribute("type", "uploadBook");
						request.getRequestDispatcher("msg.jsp").forward(request, response);
						break;
					}
					if (! fi.isFormField()) {
						fileItem  =  fi;
						break ;
					}
				}
				
				List resultIsbnList = getCheckIsbnResult(conn,fileItem);
				
				List resultErrBookElement = getCheckBookElement(conn,fileItem);
		        
				List sameIsbnList = getSameIsbn(fileItem);
		        
				List errIsbnFormat = getCheckIsbnFormat(fileItem);
				if (resultErrBookElement.size()!=0 || sameIsbnList.size()!=0 || !resultIsbnList.isEmpty() || !errIsbnFormat.isEmpty()) {
					request.setAttribute("errSameIsbn", sameIsbnList);
					request.setAttribute("errIsbnFormat", errIsbnFormat);
        			request.setAttribute("errIsbn", resultIsbnList);
					request.setAttribute("errTypeandPrice", resultErrBookElement);
					request.setAttribute("type", "uploadUpdBook");
					request.getRequestDispatcher("errExcel.jsp").forward(request, response);
				} else {
					int temp = 0;
					List bookList = readExcel(conn, fileItem, request);
					BookDAOImpl bookImpl = new BookDAOImpl();
					if (bookList != null || !bookList.isEmpty() || bookList.size()!=0) {
						for (int i=0; i<bookList.size(); i++) {
							Book book = (Book)bookList.get(i);
							Book dbBook = bookImpl.getBookByPK(conn, book.getIsbn());
							this.setBookData(dbBook, book);	
							temp = bookImpl.saveInDB(conn, dbBook);
							if (temp < 1)
								break;
						}
						if (temp == 1) {
							conn.commit();
							request.setAttribute("msg", "Update Books Success!");
							request.setAttribute("type", "uploadUpdBook");
							request.getRequestDispatcher("msg.jsp").forward(request, response);
						} else {
							conn.rollback();
							request.setAttribute("msg", "Update Books failed!");
							request.setAttribute("type", "uploadUpdBook");
							request.getRequestDispatcher("msg.jsp").forward(request, response);
						}
					} else if (bookList.size() == 0) {
						request.setAttribute("msg", "No Data!");
						request.setAttribute("type", "uploadUpdBook");
						request.getRequestDispatcher("msg.jsp").forward(request, response);
					}
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

	private void setBookData(Book dbBook, Book book) {
		dbBook.setTitle(book.getTitle());
		dbBook.setAuthor(book.getAuthor());
		dbBook.setPublisher(book.getPublisher());
		dbBook.setEdition(book.getEdition());
		dbBook.setPublishYear(book.getPublishYear());
		dbBook.setLanguage(book.getLanguage());
		dbBook.setBookType(book.getBookType());
		dbBook.setRemarks(book.getRemarks());
		dbBook.setWithdrawInd(book.getWithdrawInd());
		dbBook.setSupplierCode1(book.getSupplierCode1());
		dbBook.setSupplierCode2(book.getSupplierCode2());
		dbBook.setSupplement(book.getSupplement());
	}

	/**
	 * 
	 * @param conn
	 * @param fi
	 * @param request
	 * @throws Exception
	 */
	private List readExcel(Connection conn, FileItem fi, HttpServletRequest request) throws Exception {
		Workbook workbook = null;
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		List list = null;
		try {
			workbook = Workbook.getWorkbook(fi.getInputStream());
			Sheet sheet = workbook.getSheet(0);

			list = new ArrayList();

			for (int a = 1; a < sheet.getRows(); a++) {
				int j = 0;
				if (sheet.getCell(j, a).getContents() == null || sheet.getCell(j, a).getContents().equals("")) 
					break;

				Book book = new Book();
				book.setIsbn(sheet.getCell(j, a).getContents().trim().replace(" ", "").replace("\\n\\r", ""));
				book.setTitle(sheet.getCell(++j, a).getContents().trim().replace("\\n\\r", ""));
				book.setAuthor(sheet.getCell(++j, a).getContents().trim().replace("\\n\\r", ""));
				book.setPublisher(sheet.getCell(++j, a).getContents().trim().replace("\\n\\r", ""));
				book.setPublishYear(sheet.getCell(++j, a).getContents().trim().replace("\\n\\r", ""));
				book.setEdition(sheet.getCell(++j, a).getContents().trim().replace("\\n\\r", ""));
				book.setLanguage(sheet.getCell(++j, a).getContents().trim().toUpperCase().replace("\\n\\r", ""));
				//Default Value is NA
				book.setBookType("NA");
				book.setRemarks(sheet.getCell(++j, a).getContents().trim().replace("\\n\\r", ""));
				book.setWithdrawInd(sheet.getCell(++j, a).getContents().trim().toUpperCase().replace("\\n\\r", ""));
				book.setCreUid(userId);
				book.setUpdUid(userId);
				book.setActInd("Y");

				// rainbow add
				book.setSupplierCode1(sheet.getCell(++j, a).getContents().trim().replace("\\n\\r", ""));
				book.setSupplierCode2(sheet.getCell(++j, a).getContents().trim().toUpperCase().replace("\\n\\r", ""));
				book.setSupplement(sheet.getCell(++j, a).getContents().trim().toUpperCase().replace("\\n\\r", ""));
				list.add(book);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
		return list;
	}
		
	/**
	 * 
	 * @param fi
	 * @return all isbn in the excel file
	 */
	private List getExcelIsbn(FileItem fi) {
		List isbnList = null;
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(fi.getInputStream());
			Sheet sheet = workbook.getSheet(0);
			isbnList = new ArrayList();
			for (int i = 1; i < sheet.getRows(); i++) {
				isbnList.add(sheet.getCell(0, i).getContents().trim().replace(" ", "").replace("\\n\\r", ""));
			}
			return isbnList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
	}
		
	private List getCheckIsbnFormat(FileItem fi) {
		List formatList = new ArrayList();
		List isbnList = getExcelIsbn(fi);
		for (int i = 0; i < isbnList.size(); i++) {
			if (isbnList.get(i).toString().matches("^[0-9A-Za-z$]*$")) {

			} else {
				formatList.add(i);
			}
		}
		return formatList;
	}

	/**
	 * 
	 * @param fi
	 * @return the same isbn in excel file
	 */
	private List getSameIsbn(FileItem fi) {
		List isbnList = null;
		isbnList = getExcelIsbn(fi);
		List resultList = new ArrayList();
		HashSet hs = new HashSet();
		for (int i = 0; i < isbnList.size(); i++) {
			if (!hs.contains(isbnList.get(i))) {
				hs.add(isbnList.get(i));
			} else {
				// the same isbn on the excel file
				resultList.add(i);
			}
		}
		return resultList;
	}

	private List getCheckIsbnResult(Connection conn, FileItem fi) {
		List resultList = null;
		List list = new ArrayList();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		try {
			list = bookDAOImpl.getIsbn(conn);
			List isbnList = getExcelIsbn(fi);
			resultList = new ArrayList();
			for (int i = 0; i < isbnList.size(); i++) {
				if (!list.contains(isbnList.get(i))) {
					// the isbn is not existed in database!");
					resultList.add(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	private List getCheckBookElement(Connection conn, FileItem fi) {
		List resultList = null;
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(fi.getInputStream());
			Sheet sheet = workbook.getSheet(0);
			int rows = sheet.getRows();
			SysConfigDAOImpl confImpl = new SysConfigDAOImpl();

			// rainbow add
			BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
			List<BookSupplier> supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
			HashSet set = new HashSet();
			if (supplierList != null && !supplierList.isEmpty()) {
				for (BookSupplier b : supplierList) {
					set.add(b.getSupplierCode());
				}
			}

			resultList = new ArrayList();
			for (int i = 1; i < rows; i++) {
				String publishYear = sheet.getCell(5, i).getContents().trim().replace("\\n\\r", "");
				String language = sheet.getCell(6, i).getContents().toUpperCase().trim().replace("\\n\\r", "");
				//String booktype = sheet.getCell(7, i).getContents().trim().replace("\\n\\r", "");
				String withdrawInd = sheet.getCell(8, i).getContents().toUpperCase().trim().replace("\\n\\r", "");
				String supplierCode1 = sheet.getCell(9, i).getContents().toUpperCase().trim().replace("\\n\\r", "");
				String supplierCode2 = sheet.getCell(10, i).getContents().toUpperCase().trim().replace("\\n\\r", "");

				// rainbow add
				String supplement = sheet.getCell(11, i).getContents().toUpperCase().trim().replace("\\n\\r", "");

				if (
					//("RB".equals(booktype.toUpperCase()) || "CB".equals(booktype.toUpperCase()) || "TB".equals(booktype.toUpperCase())) && 
					confImpl.getLangResult(conn, language) == true && publishYear != null && !publishYear.equals("")	&& publishYear.length() <= 20 && 
					("Y".equals(withdrawInd) || "N".equals(withdrawInd))	&& ("Y".equals(supplement) || "N".equals(supplement) || "".equals(supplement)) && 
					this.checkSupplierCode(set, supplierCode1) && this.checkSupplierCode(set, supplierCode2)
					) {

				} else {
					resultList.add(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
		return resultList;
	}
		
	private boolean checkSupplierCode(HashSet set, String code) {
		boolean flag = true;
		if (code != null && !code.equals("")) {
			if (code.length() > 20) {
				flag = false;
			} else {
				if (set != null && !set.isEmpty()) {
					if (!set.contains(code)) {
						flag = false;
					}
				} else {
					flag = false;
				}
			}
		}
		return flag;
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
