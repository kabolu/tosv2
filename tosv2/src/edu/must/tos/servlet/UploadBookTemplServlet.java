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

import edu.must.tos.bean.BookTempl;
import edu.must.tos.bean.Course;
import edu.must.tos.bean.Major;
import edu.must.tos.impl.BookTemplDAOImpl;
import edu.must.tos.impl.CourseDAOImpl;
import edu.must.tos.impl.MajorDAOImpl;

public class UploadBookTemplServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UploadBookTemplServlet() {
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
			if (ServletFileUpload.isMultipartContent(requestContext)) {
				DiskFileItemFactory factory = new  DiskFileItemFactory();
				factory.setSizeThreshold( 1024 * 1024 );
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax( 2*1024 * 1024 );
				List items = null ;
				items = upload.parseRequest(requestContext);
				String facultyCode = null;
				for ( int i = 0 ; i < items.size(); i++ ) {
					FileItem fi = (FileItem)items.get(i);
					
					if (! fi.isFormField()) {
						String fileName = fi.getName().substring(fi.getName().lastIndexOf("\\")+1, fi.getName().length());                 
						
						String extName = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
						
						if(!"xls".equalsIgnoreCase(extName)) {
							request.setAttribute("msg", "Require The Excel File !");
							request.setAttribute("type", "uploadBookTempl");
							request.getRequestDispatcher("msg.jsp").forward(request, response);
							break;
						}
						fileItem  =  fi;
						break ;
					} else {
						facultyCode = fi.getString();
					}
				}
				List resultErrStud_grp = getCheckStud_grp(conn, fileItem);
				
				List resultErrYear = getCheckYear(conn, fileItem);
				
				List resultList = getCheckCourseCode(conn, fileItem, facultyCode);
				
				List majorCodeErrList = new ArrayList();//this.getMajorCodeErrs(conn, fileItem, facultyCode);
				
 				if(!resultErrYear.isEmpty() || !resultList.isEmpty() || !resultErrStud_grp.isEmpty() || !majorCodeErrList.isEmpty()){
 					request.setAttribute("resultErrYear", resultErrYear);
 					request.setAttribute("templCourseCode", resultList);
 					request.setAttribute("resultErrStud_grp", resultErrStud_grp);
 					request.setAttribute("majorCodeErrList", majorCodeErrList);
					request.setAttribute("type", "uploadBookTempl");
 					request.getRequestDispatcher("errExcel.jsp").forward(request, response);
 				} else {
 					List bookTemplList = readExcel(conn, fileItem, request, facultyCode);
 					
 					if(bookTemplList != null && !bookTemplList.isEmpty()){
 						BookTemplDAOImpl booktemplImpl = new BookTemplDAOImpl();
 						int temp = booktemplImpl.saveBookTemplInDB(conn, bookTemplList);
 						if(temp > 0){
 							conn.commit();
 							request.setAttribute("msg", "Import success!");
 							request.setAttribute("type", "uploadBookTempl");
 							request.getRequestDispatcher("msg.jsp").forward(request, response);
 						} else {
 							conn.rollback();
 							request.setAttribute("msg", "Import faile!");
 							request.setAttribute("type", "uploadBookTempl");
 							request.getRequestDispatcher("msg.jsp").forward(request, response);
 						}
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

	private List getMajorCodeErrs(Connection conn, FileItem fileItem,
			String facultyCode) {
		List resultList = new ArrayList();
 		Workbook workbook = null;
		try{
			workbook = Workbook.getWorkbook(fileItem.getInputStream());
 		    Sheet sheet = workbook.getSheet(0);
 		    MajorDAOImpl majorDAOImpl = new MajorDAOImpl();
 		   List<Major> majorCodeList = new ArrayList();
 		    if("GS".equals(facultyCode))
 		    	majorCodeList = majorDAOImpl.getMajByFac(conn, null);
 		    else
 		    	majorCodeList = majorDAOImpl.getMajByFac(conn, facultyCode);
 		    
 		    HashSet set = new HashSet();
 		    if(majorCodeList != null && !majorCodeList.isEmpty()){
 		    	for(Major major : majorCodeList){
 		    		set.add(major.getMajorCode());
 		    	}
 		    }
 		    int rows = sheet.getRows();
 		    for(int i=1; i<rows; i++){
 		    	if("GS".equals(facultyCode)){
 		    		if(!"ALL".equals(sheet.getCell(3, i).getContents().toUpperCase().trim().replace("\\n\\r", "")))
 		    			if(!set.contains(sheet.getCell(3, i).getContents().toUpperCase().trim().replace("\\n\\r", "")))
 	 	 		    		resultList.add(i);
 		    	} else {
 		    		if(!set.contains(sheet.getCell(3, i).getContents().toUpperCase().trim().replace("\\n\\r", "")))
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

	private List readExcel(Connection conn, FileItem fi, HttpServletRequest request, String facultyCode) throws Exception {
		
		Workbook workbook = null ;
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        String intake = (String)session.getAttribute("curIntake");
        List bookTempl = new ArrayList();
        try {
        	workbook = Workbook.getWorkbook(fi.getInputStream());
            Sheet sheet = workbook.getSheet( 0 );           
            for(int a=1; a<sheet.getRows(); a++){
	           	int j = 0;
	           	if (sheet.getCell(j,a).getContents() == null || sheet.getCell(j,a).getContents().equals("")) {
	           		break;
	           	}	           	
	           	BookTempl booktempl = new BookTempl();
	           	booktempl.setFacultyCode(facultyCode);
	           	booktempl.setIntake(intake);
	           	booktempl.setCourseCode(sheet.getCell(1, a).getContents().toUpperCase().trim().replace("\\n\\r", ""));
	           	booktempl.setYear(Integer.parseInt(sheet.getCell(0, a).getContents().trim().replace("\\n\\r", "")));
	           	booktempl.setStud_grp(sheet.getCell(2, a).getContents().trim().replace("\\n\\r", "").equals("")?null:sheet.getCell(2, a).getContents().trim().replace("\\n\\r", ""));
	           	booktempl.setMajorCode(sheet.getCell(3, a).getContents().toUpperCase().trim().replace("\\n\\r", ""));
	           	booktempl.setActInd("Y");
	           	booktempl.setCreUid(userId);
	           	booktempl.setUpdUid(userId);
	           	bookTempl.add(booktempl);
            }
        } catch (Exception e){
            throw  e;
        }finally {
            if (workbook != null ){
               workbook.close();
            }
        }
        return bookTempl;
   }
	
	private List getCheckCourseCode(Connection conn, FileItem fi, String facultyCode){
		List resultList = new ArrayList();
 		Workbook workbook = null;
		try{
			workbook = Workbook.getWorkbook(fi.getInputStream());
 		    Sheet sheet = workbook.getSheet(0);
 		    
 		    CourseDAOImpl courseDAOImpl = new CourseDAOImpl();
 		    Course course = new Course();
 		    course.setFacultyCode(facultyCode);
 		    List<Course> courseList = courseDAOImpl.getCourseCode(conn, course);
 		    HashSet courseSet = new HashSet();
 		    for(Course cos : courseList){
 		    	courseSet.add(cos.getCourseCode());
 		    }
 		    int rows = sheet.getRows();
 		    
 		    for(int i=1; i<rows; i++){
 		    	String courseCode = sheet.getCell(1, i).getContents().toUpperCase().trim().replace("\\n\\r", "");
 		    	Course cos = new Course();
 		    	cos.setCourseCode(courseCode);
 		    	List cosList = courseDAOImpl.getCourseCode(conn, cos);
 		    	String facCode = null;
 		    	if(cosList != null){
 		    		cos = (Course)cosList.get(0);
 		    		facCode = cos.getFacultyCode();
 		    	}
 		    	if(facCode != null && facCode.equals(facultyCode)){
 		    		if(!courseSet.contains(courseCode))
 	 		    		resultList.add(i);
 		    	} else if (facCode != null && "COM".equals(facultyCode)) {
 		    		//允許錄入COM的coursecode資料
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
	
	private List getCheckStud_grp(Connection conn, FileItem fi){
 		List resultList = new ArrayList();
 		Workbook workbook = null;
 		try{
 			workbook = Workbook.getWorkbook(fi.getInputStream());
 		    Sheet sheet = workbook.getSheet(0);
 		    int rows = sheet.getRows();
 		    for(int i=1; i<rows; i++){
 		    	String studGrp = sheet.getCell(2, i).getContents().trim().replace("\\n\\r", "");
 		    	if( !"01".equals(studGrp) && !"02".equals(studGrp) && !"03".equals(studGrp) && !"04".equals(studGrp)){
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
	
	private List getCheckYear(Connection conn, FileItem fi){
 		List resultList = new ArrayList();
 		Workbook workbook = null;
 		try{
 			workbook = Workbook.getWorkbook(fi.getInputStream());
 		    Sheet sheet = workbook.getSheet(0);
 		    int rows = sheet.getRows();
 		    for(int i=1; i<rows; i++){
 		    	String grade = sheet.getCell(0, i).getContents().trim().replace("\\n\\r", "");
 		    	if( isNumeric(grade) == false || grade.length() != 1){
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
	
	private boolean isNumeric(String str){
		for (int i = str.length();--i >= 0;){
			if (!Character.isDigit(str.charAt(i))){
				return false;
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
