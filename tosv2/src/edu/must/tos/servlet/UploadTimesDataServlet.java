package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import edu.must.tos.bean.Period;
import edu.must.tos.impl.PeriodDAOImpl;

public class UploadTimesDataServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UploadTimesDataServlet() {
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
				
				List items = null ;
				items = upload.parseRequest(requestContext);
				for ( int i = 0 ; i < items.size(); i++ ) {
					FileItem fi = (FileItem)items.get(i);
					
					String fileName = fi.getName().substring(fi.getName().lastIndexOf("\\")+1, fi.getName().length());
		         
					String extName = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());   
		            
					if(!"xls".equalsIgnoreCase(extName)) {
						request.setAttribute("msg","Require the excel file!");
						request.setAttribute("type", "uploadBook");
						request.getRequestDispatcher("msg.jsp").forward(request, response);
						break;
					}
					if (! fi.isFormField()) {
						fileItem  =  fi;
						break ;
					}
				}
				List list = getExcelData(request, fileItem);
				Map map = (Map)list.get(0);
				List dataList = (List)list.get(1);
				PeriodDAOImpl periodDAOImpl = new PeriodDAOImpl();
				if(map.isEmpty()){
					if(dataList != null && !dataList.isEmpty()){
						boolean flag = true;
						for(int i=0; i<dataList.size(); i++){
							Period p = (Period)dataList.get(i);
							int result = periodDAOImpl.addReceiptBookTime(conn, p);
							if(result == 0){
								flag = false;
								break;
							}
						}
						if(flag == true){
							conn.commit();
							request.setAttribute("msg", "Upload Data Success!");
							request.setAttribute("type", "uploadTimes");
							request.getRequestDispatcher("msg.jsp").forward(request, response);
						}else{
							conn.rollback();
							request.setAttribute("msg", "Upload Data failed!");
							request.setAttribute("type", "uploadTimes");
							request.getRequestDispatcher("msg.jsp").forward(request, response);
						}
					}else{
						request.setAttribute("msg", "No Data!");
						request.setAttribute("type", "uploadTimes");
						request.getRequestDispatcher("msg.jsp").forward(request, response);
					}
				}else{
					request.setAttribute("type", "uploadTimes");
					request.setAttribute("map", map);
					request.getRequestDispatcher("errExcel.jsp").forward(request, response);
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
	
	private List getExcelData(HttpServletRequest request, FileItem fileItem) throws Exception{
		List list = new ArrayList();
		Workbook workbook = null;
		
		String userId = request.getSession().getAttribute("userId").toString();
		String curIntake = request.getSession().getAttribute("curIntake").toString();
		
		Map map = new HashMap();
		HashSet HHSet = this.getHHMM(24);
		HashSet MMSet = this.getHHMM(60);
		try {
			workbook = Workbook.getWorkbook(fileItem.getInputStream());
		    Sheet sheet = workbook.getSheet( 0 );
		    SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		    SimpleDateFormat dateFormat = null;
		    List dataList = new ArrayList();
		    for(int i=1; i<sheet.getRows(); i++){
		    	int j = 0;
		    	
		    	Map err = new HashMap();
		    	
		    	String type = sheet.getCell(j++, i).getContents().trim().replace(" ", "").replace("\\n\\r", "");
		    	if(type.equals("")){
		    		err.put(1, "第一列時段種類不能為空！");
		    	}else{
		    		if(!"R".equals(type) && !"P".equals(type)){
		    			err.put(1, "第一列時段種類填寫有誤！");
		    		}
		    	}
		    	String date = sheet.getCell(j++, i).getContents();
		    	if(date.equals("")){
		    		err.put(2, "第二列日期為空！");
		    	}else {
		    		if(date.indexOf("/")>0){
		    			try{
		    				dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		    				dateFormat.parse(date);
		    			}catch(ParseException e){
		    				e.printStackTrace();
		    				if (e instanceof java.text.ParseException) {
								err.put(2, "第二列日期格式不正確！");
							}
		    			}
		    		}else if(date.indexOf("-")>0){
		    			try{
		    				dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    				dateFormat.parse(date);
		    			}catch(ParseException e){
		    				e.printStackTrace();
		    				if(e instanceof java.text.ParseException){
		    					err.put(2, "第二列日期格式不正確！");
		    				}
		    			}
		    		}
		    	}
		    	String from = sheet.getCell(j++, i).getContents().trim();
		    	if(from != null && from.substring(0, from.indexOf(":")).length() < 2){
		    		from = "0"+from;
		    	}
		    	if(from.equals("")){
		    		err.put(3, "第三列時段從為空！");
		    	}else{
		    		if(from.indexOf(":")>0){
		    			String hh = from.substring(0, from.indexOf(":"));
		    			String mm = from.substring(from.indexOf(":"), from.length());
		    			if(!HHSet.contains(hh) && !MMSet.contains(mm)){
		    				err.put(3, "第三列時段從填寫有誤！");
		    			}
		    		}else{
		    			err.put(3, "第三列時段從填寫有誤！");
		    		}
		    	}
		    	String to = sheet.getCell(j++, i).getContents().trim();
		    	if(to != null && to.substring(0, to.indexOf(":")).length() < 2){
		    		to = "0"+to;
		    	}
		    	if(to.equals("")){
		    		err.put(4, "第四列時段至為空！");
		    	}else{
		    		if(to.indexOf(":") > 0){
		    			String hh = to.substring(0, to.indexOf(":"));
		    			String mm = to.substring(to.indexOf(":"), to.length());
		    			if(!HHSet.contains(hh) && !MMSet.contains(mm)){
		    				err.put(4, "第四列時段至填寫有誤！");
		    			}
		    		}else{
		    			err.put(4, "第四列時段至填寫有誤！");
		    		}
		    	}
		    	String maxNo = sheet.getCell(j++, i).getContents().trim().replace(" ", "").replace("\\n\\r", "");
		    	if(maxNo.equals("")){
		    		err.put(5, "第五列人數限制為空！");
		    	}else{
		    		if(maxNo.length()>3){
		    			err.put(5, "第五列人數限制位數不能超過三位！");
		    		}else{
		    			if(!this.isNumeric(maxNo)){
		    				err.put(5, "第五列人數限制填寫有誤！");
		    			}
		    		}
		    	}
		    	String actind = sheet.getCell(j++, i).getContents().trim().replace(" ", "").replace("\\n\\r", "");
		    	if(actind.equals("")){
		    		err.put(6, "第六列顯示/不顯示不能為空！");
		    	}else{
		    		if(!"N".equals(actind) && !"Y".equals(actind)){
		    			err.put(6, "第六列顯示/不顯示填寫有誤！");
		    		}
		    	}
		    	
		    	Period p = new Period();
		    	p.setType(type);
		    	try{
		    		p.setStartTime(sf.parse(date+" "+from));
			    	p.setEndTime(sf.parse(date+" "+to));
		    	}catch(Exception e){
		    		if (e instanceof java.text.ParseException) {
						err.put(2, "第二列日期與時段從、時段至填寫有誤！");
					}
		    	}
		    	p.setIntake(curIntake);
		    	p.setMaxNo(Integer.parseInt(maxNo));
		    	p.setActInd(actind);
		    	p.setCreUid(userId);
		    	p.setCreDate(new Date());
		    	p.setUpdUid(userId);
		    	p.setUpdDate(new Date());
		    	dataList.add(p);
		    	
		    	if(type.equals("") && date.equals("") && from.equals("") && to.equals("") && maxNo.equals("") && actind.equals("")){
					break;
				}
				
				if(!err.isEmpty()){
					map.put(i+1, err);
				}
		    }
		    list.add(map);
			list.add(dataList);
		} catch (Exception e){
		    throw  e;
		}finally {
		    if (workbook != null ){
		       workbook.close();
		    }
		}
		return list;
	}

	private HashSet getHHMM(int params){
		HashSet set = new HashSet();
		try{
			for(int i=1; i<params+1; i++){
				if(i<10){
					set.add("0"+String.valueOf(i));
				}else {
					set.add(String.valueOf(i));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return set;
	}
	
	private boolean isNumeric(String str){
		for (int i = str.length();--i>=0;){
			if (!Character.isDigit(str.charAt(i))){
				return false;
		    }
		}
		return true;
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
