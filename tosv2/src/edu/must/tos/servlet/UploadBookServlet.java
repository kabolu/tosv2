package edu.must.tos.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.FileItem;

import edu.must.tos.bean.BookImport;
import edu.must.tos.bean.BookSupplier;
import edu.must.tos.bean.Course;
import edu.must.tos.bean.Major;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.BookSupplierDAOImpl;
import edu.must.tos.impl.CourseDAOImpl;
import edu.must.tos.impl.MajorDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.util.ToolsOfNumber;

import jxl.Workbook;
import jxl.Sheet;

public class UploadBookServlet extends HttpServlet {
        
     /** 
     * Constructor of the object.
      */ 
     public UploadBookServlet() {
         super ();
    }

     /** 
     * Destruction of the servlet.
      */ 
     public void destroy() {
         super .destroy();
    }

     public void doGet(HttpServletRequest request, HttpServletResponse response)
             throws  ServletException, IOException {
    	 this.doPost(request, response);
    }

     /**  
     *  @param  request
     *  @param  response
     *  @throws  ServletException
     *  @throws  IOException
      */ 
     public void doPost(HttpServletRequest request, HttpServletResponse response)
             throws  ServletException, IOException {
    	 Connection conn = null;    
    	 try{
    		DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
 			conn = ds.getConnection();
 			
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
 				
 				List bookImportList = this.getBookImportList(conn, fileItem);
 				CourseDAOImpl courseDAOImpl = new CourseDAOImpl();
 				//獲取全部Course Code資料
 				List<Course> courseCodeList = courseDAOImpl.getCourseCode(conn, null);
 				
 				MajorDAOImpl majorDAOImpl = new MajorDAOImpl();
 				List majorCodeList = majorDAOImpl.getMajorList(conn);
 				
 				BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
 	 		    List<BookSupplier> supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
 	 		    HashSet supplierSet = new HashSet();
 	 		    if(supplierList != null && !supplierList.isEmpty()){
 	 		    	for(BookSupplier b : supplierList){
 	 		    		supplierSet.add(b.getSupplierCode());
 	 		    	}
 	 		    }
 				List resultList = this.getBookImportErrs(conn, bookImportList, courseCodeList, majorCodeList, supplierSet);              
                
                if(resultList != null && !resultList.isEmpty()){
                	List indexList = (List)resultList.get(0);
                	List errsList = (List)resultList.get(1);
                	List bookList = (List)resultList.get(2);
                	if(indexList != null && !indexList.isEmpty()){
                		request.setAttribute("indexList", indexList);
                    	request.setAttribute("errsList", errsList);
                    	request.setAttribute("bookList", bookList);
     					request.setAttribute("type", "uploadBook");
     					request.getRequestDispatcher("errExcel.jsp").forward(request, response);
                	} else {
                		List titleList = this.getExcelTitle(fileItem);
     					request.getSession().setAttribute("titleList", titleList);
     					request.getSession().setAttribute("bookImportList", bookImportList);
     					request.getRequestDispatcher("showExcelData.jsp").forward(request, response);
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

     private List getExcelTitle(FileItem fileItem) throws Exception {
    	 Workbook workbook = Workbook.getWorkbook(fileItem.getInputStream());
         Sheet sheet = workbook.getSheet( 0 );
         
         List titleList = new ArrayList();
         for (int i=0; i<13; i++) {
        	 titleList.add(sheet.getCell(i,0).getContents().trim().replace("\\n\\r", ""));
         }
         return titleList;
	}

	private List getBookImportErrs(Connection conn, List bookImportList, List<Course> crsList, List<Major> majorCodeList, HashSet supplierSet) throws Exception{
		List resultList = new ArrayList();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		SysConfig config = new SysConfig();
		config.setScType("EXCHANGERATE");
		config.setActInd("Y");
		List<SysConfig> exchangeRateList = sysConfigDAOImpl.getSysConfigList(conn, config);
		
		HashSet majCodeSet = new HashSet();
		HashSet crsCodeSet = new HashSet();
		HashSet crsCode4GSSet = new HashSet();
		if(crsList != null && !crsList.isEmpty()){
			for(Course crs : crsList){
				if("GS".equals(crs.getFacultyCode())){
					crsCode4GSSet.add(crs.getCourseCode());
				} else {
					crsCodeSet.add(crs.getCourseCode());
				}
			}
		}
		for(Major maj : majorCodeList){
			majCodeSet.add(maj.getMajorCode());
		}
		if(bookImportList != null && !bookImportList.isEmpty()){
			int size = bookImportList.size();
			List bookList = new ArrayList();
			List errsList = new ArrayList();
			List indexList = new ArrayList();
			for(int i=0; i<size; i++){
				List errList = new ArrayList();
				BookImport bookImport = (BookImport)bookImportList.get(i);
				boolean isGS = false;
				if(bookImport.getCourseCode() != null && !bookImport.getCourseCode().equals("")){
					if(!"OTHER".equals(bookImport.getCourseCode().toUpperCase())){
						if(crsCode4GSSet.contains(bookImport.getCourseCode().toUpperCase())){
							isGS = true;
						} else {
							if(!crsCodeSet.contains(bookImport.getCourseCode().toUpperCase())){
								errList.add("不存在該科目編號!");
							}
						}						
					}
				} else {
					errList.add("科目編號不能為空!");
				}
				if(bookImport.getMajorCode() != null && !bookImport.getMajorCode().equals("")){
					/*
					if(isGS){
						
					} else {
						if(!majCodeSet.contains(bookImport.getMajorCode().toUpperCase()))
							errList.add("不存在該專業編號!");
					}
					*/
					if(!"ALL".equals(bookImport.getMajorCode().toUpperCase())){
						if(!majCodeSet.contains(bookImport.getMajorCode().toUpperCase())){
							errList.add("不存在該專業編號!");
						}
					}
				} else {
					errList.add("專業編號不能為空!");
				}
				if(bookImport.getGrade() != null && !bookImport.getGrade().equals("")){
					
				} else {
					errList.add("年級不能為空!");
				}
				if(bookImport.getCore() != null && !bookImport.getCore().equals("")){
					if(!"C".equals(bookImport.getCore().toUpperCase()) && !"E".equals(bookImport.getCore().toUpperCase())){
						errList.add("必修/選修欄不符合“C”、“E”!");
					}
				} else {
					errList.add("必修/選修欄不能為空!");
				} 
				if(bookImport.getLanguage() != null && !bookImport.getLanguage().equals("")){
					if(!"GB".equals(bookImport.getLanguage().toUpperCase()) && !"BIG5".equals(bookImport.getLanguage().toUpperCase()) 
							&& !"ENG".equals(bookImport.getLanguage().toUpperCase())){
						errList.add("圖書語言編碼不正確!");
					}
				} else {
					errList.add("圖書語言不能為空!");
				}
				if(bookImport.getBookType() != null && !bookImport.getBookType().equals("")){
					if(!"CB".equals(bookImport.getBookType().toUpperCase()) && !"RB".equals(bookImport.getBookType().toUpperCase()) 
							&& !"TB".equals(bookImport.getBookType().toUpperCase())){
						errList.add("圖書類型編碼不正確!");
					}
				} else {
					errList.add("圖書類型不能為空!");
				}
				if(bookImport.getIsbn() != null && !bookImport.getIsbn().equals("")){
					if(!bookImport.getIsbn().trim().matches("^[0-9A-Za-z$]*$"))
						errList.add("Isbn格式不正確!");
				} else {
					errList.add("Isbn不能為空!");
				}
				if(bookImport.getTitle() == null || bookImport.getTitle().equals("")){
					errList.add("圖書書目不能為空!");
				}
				if(bookImport.getAuthor() == null || bookImport.getAuthor().equals("")){
					errList.add("圖書作者不能為空!");
				}
				if(bookImport.getPublisher() == null || bookImport.getPublisher().equals("")){
					errList.add("圖書出版社不能為空!");
				}
				if(bookImport.getEdition() == null || bookImport.getEdition().equals("")){
					errList.add("圖書版本不能為空!");
				}
				if(bookImport.getPublishYear() == null || bookImport.getPublishYear().equals("")){
					errList.add("圖書出版年份不能為空!");
				}
				//若輸入有幣種資料，則需要檢查入貨價和折扣信息
				if(bookImport.getCurrency() != null && !bookImport.getCurrency().equals("")){
					if(!"MOP".equals(bookImport.getCurrency().toUpperCase()) && !"HKD".equals(bookImport.getCurrency().toUpperCase()) 
							&& !"USD".equals(bookImport.getCurrency().toUpperCase()) && !"RMB".equals(bookImport.getCurrency().toUpperCase())){
						errList.add("圖書入貨幣種不正確，幣種包括MOP、HKD、USD、RMB!");
					}
					if(bookImport.getUnitPrice() != null && !bookImport.getUnitPrice().equals("")){
						if(!ToolsOfNumber.isPrice(bookImport.getUnitPrice())){
							errList.add("圖書入貨單價不正確!");
						}
					} else {
						errList.add("圖書入貨單價不能為空!");
					}
					if(bookImport.getDisCount() != null && !bookImport.getDisCount().equals("")){
						if(!ToolsOfNumber.isPrice(bookImport.getDisCount())){
							errList.add("圖書入貨折扣不正確!");
						}
					} else {
						errList.add("圖書入貨折扣不能為空!");
					}
				}
				
				if(bookImport.getWithdrawInd() != null && !bookImport.getWithdrawInd().equals("")){
					if(!"Y".equals(bookImport.getWithdrawInd().toUpperCase()) && !"N".equals(bookImport.getWithdrawInd().toUpperCase())){
						errList.add("圖書退書標識不正確，需要Y、N!");
					}
				} else {
					errList.add("圖書退書標識不能為空!");
				}
				if(bookImport.getSupplierCode1() != null && !bookImport.getSupplierCode1().equals("")){
					if(!this.checkSupplierCode(supplierSet, bookImport.getSupplierCode1())){
						errList.add("圖書書商1不正確!");
					}
				} else {
					errList.add("圖書書商1不能為空!");
				}
				if(bookImport.getSupplierCode2() != null && !bookImport.getSupplierCode2().equals("")){
					if(!this.checkSupplierCode(supplierSet, bookImport.getSupplierCode2())){
						errList.add("圖書書商2不正確!");
					}
				}
				if(bookImport.getSupplement() != null && !bookImport.getSupplement().equals("")){
					if(!"Y".equals(bookImport.getSupplement().toUpperCase()) && !"N".equals(bookImport.getSupplement().toUpperCase())){
						errList.add("圖書補訂標識不正確，需要Y、N!");
					}
				}
				if(bookImport.getFuturePrice_MOP() != null && !bookImport.getFuturePrice_MOP().equals("")){
					if(!ToolsOfNumber.isPrice(bookImport.getFuturePrice_MOP())){
						errList.add("圖書預設單價（MOP）不正確!");
					}
				} else {
					errList.add("圖書預設單價（MOP）不能為空!");
				}
				if(bookImport.getFuturePrice_RMB() != null && !bookImport.getFuturePrice_RMB().equals("")){
					if(!ToolsOfNumber.isPrice(bookImport.getFuturePrice_RMB())){
						errList.add("圖書預設單價（RMB）不正確!");
					}
				} else {
					errList.add("圖書預設單價（RMB）不能為空!");
				}
				if(bookImport.getFavourablePrice() != null && !bookImport.getFavourablePrice().equals("")){
					if(!ToolsOfNumber.isPrice(bookImport.getFavourablePrice())){
						errList.add("圖書優惠價（MOP）不正確!");
					}
				} else {
					errList.add("圖書優惠價（MOP）不能為空!");
				}
				if(bookImport.getCurrency() != null && !bookImport.getCurrency().equals("")){
					Double unitPrice = new Double(0);
					if(bookImport.getUnitPrice() != null && !"".equals(bookImport.getUnitPrice()))
						try{
							unitPrice = Double.parseDouble(bookImport.getUnitPrice());
						}catch(Exception e){
							errList.add("單價格式不正確!");
						}
					Double disCount = new Double(0);
					if(bookImport.getDisCount() != null && !"".equals(bookImport.getDisCount()))
						try{
							disCount = Double.parseDouble(bookImport.getDisCount());
						}catch(Exception e){
							errList.add("折扣格式不正確!");
						}
						
					for(SysConfig sysConfig : exchangeRateList){
						if ("MOP".equals(bookImport.getCurrency().toUpperCase())){
							Double rate = 1.0;
							Double result = unitPrice * disCount * rate;
							try{
								if(result - Double.parseDouble(bookImport.getFuturePrice_MOP()) > 0 ){
									errList.add("圖書入貨價大於預設價（MOP）!");
								}
							}catch(Exception e){
								errList.add("圖書預設單價（MOP）不正確!");
							}
							
						} else {
							if(sysConfig.getScKey().equals(bookImport.getCurrency().toUpperCase())){
								Double rate = Double.parseDouble(sysConfig.getScValue1());
								Double result = unitPrice * disCount * rate;
								try{
									if(result - Double.parseDouble(bookImport.getFuturePrice_MOP()) > 0 ){
										errList.add("圖書入貨價大於預設價（MOP）!");
									}
								}catch(Exception e){
									errList.add("圖書預設單價（MOP）不正確!");
								}
							}
						}
					}
				}
				if(!errList.isEmpty()){
					indexList.add(i);
					errsList.add(errList);
					bookList.add(bookImport);
				}
			}
			resultList.add(indexList);
			resultList.add(errsList);
			resultList.add(bookList);
		}
		return resultList;
	}

	private List getBookImportList(Connection conn, FileItem fileItem) throws Exception{
    	 Workbook workbook = Workbook.getWorkbook(fileItem.getInputStream());
    	 Sheet sheet = workbook.getSheet(0);
    	 List resultList = new ArrayList();
    	 for(int i=1; i<sheet.getRows(); i++){
    		 int j = 0;
    		 BookImport bookImport = new BookImport();
    		 bookImport.setCourseCode(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setMajorCode(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setGrade(sheet.getCell(j++, i).getContents().replaceAll("　", "  ").trim().replace("\\n\\r\\s", ""));
    		 //bookImport.setCls(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setCore(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setLanguage(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setBookType(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setIsbn(sheet.getCell(j++, i).getContents().trim().replace(" ", "").replace("\\n\\r", ""));
    		 bookImport.setTitle(sheet.getCell(j++, i).getContents().trim().replace("", ""));
    		 bookImport.setAuthor(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setPublisher(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setEdition(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setPublishYear(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));    		 
    		 bookImport.setRemarks(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setCurrency(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setUnitPrice(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setDisCount(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setWithdrawInd(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setSupplierCode1(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setSupplierCode2(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setSupplement(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setFuturePrice_MOP(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setFuturePrice_RMB(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 bookImport.setFavourablePrice(sheet.getCell(j++, i).getContents().trim().replace("\\n\\r", ""));
    		 resultList.add(bookImport);
    	 }
    	 return resultList;
	}
 	
 	/**
 	 * check publishYear, language, bookType, withdrawInd, futureprice(MOP or RMB)
 	 * @param conn
 	 * @param fi
 	 * @return list contains rows which data is wrong 
 	 */
 	private List getCheckBookTypeAndPrice(Connection conn, FileItem fi){
 		List resultList = null;
 		Workbook workbook = null;
 		try{
 			workbook = Workbook.getWorkbook(fi.getInputStream());
 		    Sheet sheet = workbook.getSheet(0);
 		    int rows = sheet.getRows();
 		    SysConfigDAOImpl confImpl = new SysConfigDAOImpl();
 		    BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
 		    List<BookSupplier> supplierList = bookSupplierDAOImpl.getBookSuppliersList(conn, null);
 		    HashSet set = new HashSet();
 		    if(supplierList!=null && !supplierList.isEmpty()){
 		    	for(BookSupplier b : supplierList){
 		    		set.add(b.getSupplierCode());
 		    	}
 		    }
 		    resultList = new ArrayList();
 		    for(int i=1; i<rows; i++){
 		    	String publishYear = sheet.getCell(5, i).getContents().trim().replace("\\n\\r", "");
 		    	String booktype = sheet.getCell(7, i).getContents().trim().replace("\\n\\r", "");
 		    	String language = sheet.getCell(6, i).getContents().toUpperCase().trim().replace("\\n\\r", "");
 		    	String withdrawInd = sheet.getCell(9, i).getContents().toUpperCase().trim().replace("\\n\\r", "");
 		    	String supplierCode1 = sheet.getCell(10, i).getContents().trim().replace("\\n\\r", "");
 		    	String supplierCode2 = sheet.getCell(11, i).getContents().trim().replace("\\n\\r", "");
 		    	
 		    	//rainbow add
 		    	String supplement = sheet.getCell(12, i).getContents().toUpperCase().trim().replace("\\n\\r", "");
 		    	String futureprice = sheet.getCell(13, i).getContents().trim().replace("\\n\\r", "");
 		    	String netprice = sheet.getCell(14, i).getContents().trim().replace("\\n\\r", "");
 		    	
 		    	//String futureprice = sheet.getCell(12, i).getContents().trim().replace("\\n\\r", "");
 		    	//String netprice = sheet.getCell(13, i).getContents().trim().replace("\\n\\r", "");
 		    	
 		    	if(("RB".equals(booktype.toUpperCase()) || "CB".equals(booktype.toUpperCase()) || "TB".equals(booktype.toUpperCase()))
 		    			&& ToolsOfNumber.isPrice(futureprice) == true && ToolsOfNumber.isPrice(netprice) == true
 		    			&& confImpl.getLangResult(conn,language) == true
 		    			&& publishYear != null && !publishYear.equals("") && publishYear.length()<=20 
 		    			&& ("Y".equals(withdrawInd) || "N".equals(withdrawInd)) && ("Y".equals(supplement) || "N".equals(supplement) || "".equals(supplement)) 
 		    			&& this.checkSupplierCode(set, supplierCode1) && this.checkSupplierCode(set, supplierCode2)){
 		    		//do nothing
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
 	
 	private boolean checkSupplierCode(HashSet set, String code){
 		boolean flag = true;
 		if(code != null && !code.equals("")){
 			if(code.length() > 20){
 				flag = false;
 			}else{
 				if(set != null && !set.isEmpty()){
 					if(!set.contains(code)){
 						flag = false;
 					}
 				}else{
 					flag = false;
 				}
 			}
 		}
 		return flag ;
 	}
    
     /** 
     * Initialization of the servlet.
     * 
     *  @throws  ServletException
      */ 
     public   void  init()  throws  ServletException {
    }
} 