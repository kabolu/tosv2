package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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

import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.Course;
import edu.must.tos.bean.Faculty;
import edu.must.tos.bean.Major;
import edu.must.tos.bean.NewCourseInfo;
import edu.must.tos.bean.Program;
import edu.must.tos.bean.SysCtrl;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookRelDAOImpl;
import edu.must.tos.impl.CourseDAOImpl;
import edu.must.tos.impl.FacultyDAOImpl;
import edu.must.tos.impl.MajorDAOImpl;
import edu.must.tos.impl.NewStudOrderDAOImpl;
import edu.must.tos.impl.ProgramDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.SysCtrlDAOImpl;
import edu.must.tos.util.UploadFile;

public class UploadNewCourseServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UploadNewCourseServlet() {
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
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			String key = "CURRINTAKE";
			String intake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			int size = 3;	//設置上傳文件大小
			
			List list = UploadFile.upload(request, size);
			List elementsList = null;
			FileItem fileItem = null;
			String sizeInfo = "";
			if(list != null && list.size() == 3){
				fileItem = (FileItem)list.get(0);
				elementsList = (List)list.get(1);
				sizeInfo = (String)list.get(2);
			}
			if(sizeInfo.equals("wrong")){
				request.setAttribute("type", "uploadNewStudOrder");
				request.setAttribute("msg", "上傳文件超過 " + size + " M！");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			} else {
				if(fileItem != null){
					boolean excelFormat = checkExcelFormat(fileItem);
					if(excelFormat){
						List dataList = getExcelDate(conn, fileItem); 
						Map map = (HashMap)dataList.get(0);
						if(!map.isEmpty()){
							request.setAttribute("type", "uploadNewCourseInfo");
							request.setAttribute("map", map);
							request.getRequestDispatcher("errExcel.jsp").forward(request, response);
						}else{
							List infoList = (List)dataList.get(1);
							if(infoList.isEmpty()){
								request.setAttribute("type", "uploadNewCourseInfo");
								request.setAttribute("msg", "該新生選科文件沒有符合導入的資料！");
								request.getRequestDispatcher("msg.jsp").forward(request, response);
							}else{
								NewStudOrderDAOImpl newStudCourseImpl = new NewStudOrderDAOImpl();
								
								List recordList = newStudCourseImpl.getNewStudCourseInfo(conn, intake);
								if(recordList.isEmpty() && recordList.size() == 0){
									boolean result = newStudCourseImpl.importNewStudCourseInfo(conn, infoList);
									if(result){
										conn.commit();
										request.setAttribute("type", "uploadNewCourseInfo");
										request.setAttribute("msg", "新生選科資料導入成功！");
										request.getRequestDispatcher("msg.jsp").forward(request, response);
									}else{
										conn.rollback();
										request.setAttribute("type", "uploadNewCourseInfo");
										request.setAttribute("msg", "新生選科資料導入失敗！");
										request.getRequestDispatcher("msg.jsp").forward(request, response);
									}
								}else{
									//有舊資料！需要刪除
									//由於要導入研究生新生的資料，所以不用刪除舊的資料
									//boolean deleteRecord = newStudCourseImpl.deleteNewStudCourseInfo(conn);
									boolean result = newStudCourseImpl.importNewStudCourseInfo(conn, infoList);
									if(result){
										conn.commit();
										request.setAttribute("type", "uploadNewCourseInfo");
										request.setAttribute("msg", "新生選科資料導入成功！之前舊資料刪除成功！");
										request.getRequestDispatcher("msg.jsp").forward(request, response);
									}else{
										conn.rollback();
										request.setAttribute("type", "uploadNewCourseInfo");
										request.setAttribute("msg", "新生選科資料導入失敗!");
										request.getRequestDispatcher("msg.jsp").forward(request, response);
									}
								}
							}
						}
					}else{
						request.setAttribute("msg", "新生選科資料導入文件不符合，請檢查！");
						request.setAttribute("type", "uploadNewCourseInfo");
						request.getRequestDispatcher("msg.jsp").forward(request, response);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if (conn != null && !conn.isClosed())
				conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	private List getExcelDate(Connection conn, FileItem fi){
		List list = new ArrayList();
		Map map = new HashMap();
		Workbook workbook = null;
		try{
			//學院資料
			FacultyDAOImpl facultyImpl = new FacultyDAOImpl();
			List facultyList = facultyImpl.getFacultyList(conn);
			HashSet facultySet = new HashSet();
			for(int x=0; x<facultyList.size(); x++){
				Faculty f = (Faculty)facultyList.get(x);
				facultySet.add(f.getFacultyCode());
			}
			//課程資料
			ProgramDAOImpl programImpl = new ProgramDAOImpl();
			List programList = programImpl.getProgramInfo(conn);
			HashSet awardTypeSet = new HashSet();
			HashSet progSet = new HashSet();
			for(int i=0; i<programList.size(); i++){
				Program p = (Program)programList.get(i);
				progSet.add(p.getProgramCode());
				awardTypeSet.add(p.getAwardType());
			}
			//專業資料
			MajorDAOImpl majorDAOImpl = new MajorDAOImpl();
			List majorList = majorDAOImpl.getMajorList(conn);
			HashSet majSet = new HashSet();
			for(int i=0; i<majorList.size(); i++){
				Major major = new Major();
				major = (Major)majorList.get(i);
				majSet.add(major.getMajorCode());
			}
			//科目資料
			CourseDAOImpl courseImpl = new CourseDAOImpl();
			HashSet courseSet = new HashSet();
			List<Course> crsList = courseImpl.getCourseCode(conn, null);
			for(Course crs : crsList){
				courseSet.add(crs.getCourseCode());
			}
			//圖書資料
			BookDAOImpl bookImpl = new BookDAOImpl();
			List bookList = bookImpl.getAllBook(conn);
			HashSet bookSet = new HashSet();
			for(int x=0; x<bookList.size(); x++){
				Book book = (Book)bookList.get(x);
				bookSet.add(book.getIsbn());
			}
			//地區資料
			SysCtrlDAOImpl sysCtrlImpl = new SysCtrlDAOImpl();
			SysCtrl regionCtrl = new SysCtrl();
			regionCtrl.setType("AREA");
			List regionList = sysCtrlImpl.getSysCtrlInfo(conn, regionCtrl);
			HashSet regionSet = new HashSet();
			for(int i=0; i<regionList.size(); i++){
				SysCtrl ctrl = new SysCtrl();
				ctrl = (SysCtrl)regionList.get(i);
				regionSet.add(ctrl.getValue1());
			}
			List rList = new ArrayList(regionSet);
			
			//錄取情況資料
			SysCtrl statusCtrl = new SysCtrl();
			statusCtrl.setType("APPACCEPT");
			List statusList = sysCtrlImpl.getSysCtrlInfo(conn, statusCtrl);
			List statusYSet = new ArrayList();
			HashSet statusSet = new HashSet();
			for(int i=0; i<statusList.size(); i++){
				SysCtrl ctrl = new SysCtrl();
				ctrl = (SysCtrl)statusList.get(i);
				if(ctrl.getValue3().equals("Y")){
					statusYSet.add(ctrl.getKey());
				}
				statusSet.add(ctrl.getKey());
			}
			//當前學期
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			String key = "CURRINTAKE";
			String intake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			//獲取書與科目的關係
			BookRel bookRel = new BookRel();
			bookRel.setIntake(intake);
			bookRel.setActInd("Y");
			BookRelDAOImpl bookRelImpl = new BookRelDAOImpl();
			List bookRelList = bookRelImpl.getBookRelList(conn, bookRel);
			
			workbook = Workbook.getWorkbook(fi.getInputStream());
			Sheet sheet = workbook.getSheet(0);
			
			List infoList = new ArrayList();
			
			for(int i=1; i<sheet.getRows(); i++){
				Map err = new HashMap();
				String region = sheet.getCell(0, i).getContents().trim().replace("\\n\\r", "");
				boolean check1 = this.checkStr(region);
				if(!check1 || (region == null || region.equals(""))){
					err.put(1, "第1列地區資料填寫有誤，或為空！");
				}else{
					if(region.length() > 10){
						err.put(1, "第1列地區資料字符長度超過10位！");
					}else{
						if(!"ALL".equalsIgnoreCase(region)){
							if(!regionSet.contains(region)){
								err.put(1, "第1列地區資料填寫不正確！");
							}
						}
					}
				}
				
				String faculty = sheet.getCell(1, i).getContents().trim().replace("\\n\\r", "");
				boolean check2 = this.checkStr(faculty);
				if(!check2 || (faculty == null || faculty.equals(""))){
					err.put(2, "第2列學院編號填寫有誤，或為空！");
				}else{
					if(faculty.length() > 4){
						err.put(2, "第二列學院編號長度超過4位！");
					}else{
						if(!"ALL".equalsIgnoreCase(faculty) && !facultySet.contains(faculty)){
							err.put(2, "第2列學院編號不存在數據庫中！");
						}
					}
				}
				
				String program = sheet.getCell(2, i).getContents().trim().replace("\\n\\r", "");
				if(program == null || program.equals("")){
					err.put(3, "第3列課程編號填寫有誤，或為空！");
				}else{
					if(program.length() > 10){
						err.put(3, "第3列課程編號長度超過10位！");
					}else{
						if(!"ALL".equalsIgnoreCase(program)){
							boolean checkProgram = this.checkProgram(programList, faculty, program);
							if(!checkProgram){
								err.put(3, "第3列課程編號不在對應的學院內！");
							}
						}
					}
				}
				//新增專業分類
				String major = sheet.getCell(3, i).getContents().trim().replace("\\n\\r", "");
				if(major == null || major.equals("")){
					err.put(4, "第4列專業編號填寫有誤，或為空！");
				}else{
					if(major.length() > 10){
						err.put(4, "第4列專業編號長度超過10位！");
					}else{
						if(!"ALL".equalsIgnoreCase(major) ){
							boolean checkMajor = this.checkMajor(majorList, program, major);
							if(!checkMajor){
								err.put(4, "第4列專業編號不在對應的課程內！");
							}
						}
					}
				}
				
				String status = sheet.getCell(4, i).getContents().trim().replace("\\n\\r", "");
				boolean check5 = this.checkStr(status);
				if(!check5 || (status == null || status.equals(""))){
					err.put(5, "第5列錄取狀況填寫有誤，或為空！");
				}else{
					if(status.length() > 3){
						err.put(5, "第5列錄取狀況資料長度超過3位！");
					}else{
						if(!"ALL".equalsIgnoreCase(status)){
							if(!statusSet.contains(status)){
								err.put(5, "第5列錄取狀況填寫不正確！");
							}
						}
					}
				}
				String isbn = sheet.getCell(5, i).getContents().trim().replace("\\n\\r", "");
				boolean check6 = isbn.matches("^[0-9A-Za-z$]*$");
				if(!check6 || (isbn == null || isbn.equals(""))){
					err.put(6, "第6列ISBN編號填寫有誤，或為空！");
				}else{
					if(isbn.length() > 30){
						err.put(6, "第6列ISBN編號長度超過30位！");
					}else{
						if(!bookSet.contains(isbn)){
							err.put(6, "第6列ISBN不存在數據庫中！");
						}
					}
				}
				HashSet checkSet = new HashSet();
				for(int j=0; j<bookRelList.size(); j++){
					BookRel r = new BookRel();
					r = (BookRel)bookRelList.get(j);
					if(r.getIsbn().equals(isbn)){
						checkSet.add(r.getCourseCode());
					}
				}
				String course = sheet.getCell(6, i).getContents().trim().replace("\\n\\r", "");
				if(course == null || course.equals("")){
					err.put(7, "第7列科目編號填寫有誤！");
				}else{
					if(course.length() > 20){
						err.put(7, "第7列科目編號長度超過20位！");
					}else{
						if(!courseSet.contains(course)){
							err.put(7, "第7列科目編號不存在數據庫中！");
						}else{
							if(!checkSet.contains(course)){
								err.put(7, "第7列科目編與該ISBN圖書沒有聯繫！");
							}
						}
					}
				}
				String awardType = sheet.getCell(7, i).getContents().trim().replace("\\n\\r", "");
				boolean check8 = this.checkStr(awardType);
				if(!check8 || (awardType == null || awardType.equals(""))){
					err.put(8, "第8列課程類別編號填寫有誤，或為空！");
				}else{
					if(!awardTypeSet.contains(awardType)){
						err.put(8, "第8列課程類別編號不存在數據庫中！");
					}
				}
				
				//讀取數值
				if("ALL".equalsIgnoreCase(status)){
					for(int d=0; d<statusYSet.size(); d++){
						String s = (String)statusYSet.get(d);
						if("ALL".equalsIgnoreCase(region)){
							for(int c=0; c<rList.size(); c++){
								String regionStr = (String)rList.get(c);
								if("ALL".equalsIgnoreCase(faculty)){
									for(int b=0;b<facultyList.size();b++){
										Faculty fac = (Faculty)facultyList.get(b);
										List progList = this.getProgram(programList, fac.getFacultyCode(), awardType);
										if("ALL".equalsIgnoreCase(program)){
											for(int a=0;a<progList.size();a++){
												String prog = (String)progList.get(a);
												List majList = this.getMajor(majorList, prog);
												if("ALL".equalsIgnoreCase(major)){
													for(int x=0; x<majList.size(); x++){
														String maj = (String)majList.get(x);
														NewCourseInfo info = new NewCourseInfo();
														info.setRegion(regionStr);
														info.setFacultyCode(fac.getFacultyCode());
														info.setProgramCode(prog);
														info.setMajorCode(maj);
														info.setStatus(s);
														info.setCourseCode(course);
														info.setIsbn(isbn);
														info.setIntake(intake);
														info.setAwardType(awardType);
														infoList.add(info);
													}
												}else if(majSet.contains(major)){
													NewCourseInfo info = new NewCourseInfo();
													info.setRegion(regionStr);
													info.setFacultyCode(fac.getFacultyCode());
													info.setProgramCode(prog);
													info.setMajorCode(major);
													info.setStatus(s);
													info.setCourseCode(course);
													info.setIsbn(isbn);
													info.setIntake(intake);
													info.setAwardType(awardType);
													infoList.add(info);
												}
											}
										}else if(progSet.contains(program)){
											List majList = this.getMajor(majorList, program);
											if("ALL".equalsIgnoreCase(major)){
												for(int a=0; a<majList.size(); a++){
													String maj = (String)majList.get(a);
													NewCourseInfo info = new NewCourseInfo();
													info.setRegion(regionStr);
													info.setFacultyCode(fac.getFacultyCode());
													info.setProgramCode(program);
													info.setMajorCode(maj);
													info.setStatus(s);
													info.setCourseCode(course);
													info.setIsbn(isbn);
													info.setIntake(intake);
													info.setAwardType(awardType);
													infoList.add(info);
												}				
											}else if(majSet.contains(major)){
												NewCourseInfo info = new NewCourseInfo();
												info.setRegion(regionStr);
												info.setFacultyCode(fac.getFacultyCode());
												info.setProgramCode(program);
												info.setMajorCode(major);
												info.setStatus(s);
												info.setCourseCode(course);
												info.setIsbn(isbn);
												info.setIntake(intake);
												info.setAwardType(awardType);
												infoList.add(info);
											}
										}
									}
								}else if(facultySet.contains(faculty)){
									List progList = this.getProgram(programList, faculty, awardType);
									if("ALL".equalsIgnoreCase(program)){
										for(int a=0;a<progList.size();a++){
											String prog = (String)progList.get(a);
											List majList = this.getMajor(majorList, prog);
											if("ALL".equalsIgnoreCase(major)){
												for(int x=0; x<majList.size(); x++){
													String maj = (String)majList.get(x);
													NewCourseInfo info = new NewCourseInfo();
													info.setRegion(regionStr);
													info.setFacultyCode(faculty);
													info.setProgramCode(prog);
													info.setMajorCode(maj);
													info.setStatus(s);
													info.setCourseCode(course);
													info.setIsbn(isbn);
													info.setIntake(intake);
													info.setAwardType(awardType);
													infoList.add(info);
												}
											}else if(majSet.contains(major)){
												NewCourseInfo info = new NewCourseInfo();
												info.setRegion(regionStr);
												info.setFacultyCode(faculty);
												info.setProgramCode(prog);
												info.setMajorCode(major);
												info.setStatus(s);
												info.setCourseCode(course);
												info.setIsbn(isbn);
												info.setIntake(intake);
												info.setAwardType(awardType);
												infoList.add(info);
											}
										}
									}else{
										List majList = this.getMajor(majorList, program);
										if("ALL".equalsIgnoreCase(major)){
											for(int x=0; x<majList.size(); x++){
												String maj = (String)majList.get(x);
												NewCourseInfo info = new NewCourseInfo();
												info.setRegion(regionStr);
												info.setFacultyCode(faculty);
												info.setProgramCode(program);
												info.setMajorCode(maj);
												info.setStatus(s);
												info.setCourseCode(course);
												info.setIsbn(isbn);
												info.setIntake(intake);
												info.setAwardType(awardType);
												infoList.add(info);
											}
										}else if(majSet.contains(major)){
											NewCourseInfo info = new NewCourseInfo();
											info.setRegion(regionStr);
											info.setFacultyCode(faculty);
											info.setProgramCode(program);
											info.setMajorCode(major);
											info.setStatus(s);
											info.setCourseCode(course);
											info.setIsbn(isbn);
											info.setIntake(intake);
											info.setAwardType(awardType);
											infoList.add(info);
										}
									}
								}
							}
						}else if(regionSet.contains(region)){
							if("ALL".equalsIgnoreCase(faculty)){
								for(int b=0; b<facultyList.size(); b++){
									Faculty fac = (Faculty)facultyList.get(b);
									List progList = this.getProgram(programList, fac.getFacultyCode(), awardType);
									for(int a=0; a<progList.size(); a++){
										String prog = (String)progList.get(a);
										List majList = this.getMajor(majorList, prog);
										if("ALL".equalsIgnoreCase(major)){
											for(int x=0; x<majList.size(); x++){
												String maj = (String)majList.get(x);
												NewCourseInfo info = new NewCourseInfo();
												info.setRegion(region);
												info.setFacultyCode(fac.getFacultyCode());
												info.setProgramCode(prog);
												info.setMajorCode(maj);
												info.setStatus(s);
												info.setCourseCode(course);
												info.setIsbn(isbn);
												info.setIntake(intake);
												info.setAwardType(awardType);
												infoList.add(info);
											}
										}else if(majSet.contains(major)){
											NewCourseInfo info = new NewCourseInfo();
											info.setRegion(region);
											info.setFacultyCode(fac.getFacultyCode());
											info.setProgramCode(prog);
											info.setMajorCode(major);
											info.setStatus(s);
											info.setCourseCode(course);
											info.setIsbn(isbn);
											info.setIntake(intake);
											info.setAwardType(awardType);
											infoList.add(info);
										}
									}
								}
							}else if(facultySet.contains(faculty)){
								List progList = this.getProgram(programList, faculty, awardType);
								if("ALL".equalsIgnoreCase(program)){
									for(int a=0; a<progList.size(); a++){
										String prog = (String)progList.get(a);
										List majList = this.getMajor(majorList, prog);
										if("ALL".equalsIgnoreCase(major)){
											for(int x=0; x<majList.size(); x++){
												String maj = (String)majList.get(x);
												NewCourseInfo info = new NewCourseInfo();
												info.setRegion(region);
												info.setFacultyCode(faculty);
												info.setProgramCode(prog);
												info.setMajorCode(maj);
												info.setStatus(s);
												info.setCourseCode(course);
												info.setIsbn(isbn);
												info.setIntake(intake);
												info.setAwardType(awardType);
												infoList.add(info);
											}
										}else if(majSet.contains(major)){
											NewCourseInfo info = new NewCourseInfo();
											info.setRegion(region);
											info.setFacultyCode(faculty);
											info.setProgramCode(prog);
											info.setMajorCode(major);
											info.setStatus(s);
											info.setCourseCode(course);
											info.setIsbn(isbn);
											info.setIntake(intake);
											info.setAwardType(awardType);
											infoList.add(info);
										}
									}
								}else if(progSet.contains(program)){
									List majList = this.getMajor(majorList, program);
									if("ALL".equalsIgnoreCase(major)){
										for(int x=0; x<majList.size(); x++){
											String maj = (String)majList.get(x);
											NewCourseInfo info = new NewCourseInfo();
											info.setRegion(region);
											info.setFacultyCode(faculty);
											info.setProgramCode(program);
											info.setMajorCode(maj);
											info.setStatus(s);
											info.setCourseCode(course);
											info.setIsbn(isbn);
											info.setIntake(intake);
											info.setAwardType(awardType);
											infoList.add(info);
										}
									}else if(majSet.contains(major)){
										NewCourseInfo info = new NewCourseInfo();
										info.setRegion(region);
										info.setFacultyCode(faculty);
										info.setProgramCode(program);
										info.setMajorCode(major);
										info.setStatus(s);
										info.setCourseCode(course);
										info.setIsbn(isbn);
										info.setIntake(intake);
										info.setAwardType(awardType);
										infoList.add(info);
									}
								}
							}
						}
					}
				}else if(statusYSet.contains(status)){
					if("ALL".equalsIgnoreCase(region)){
						for(int c=0; c<rList.size(); c++){
							String regionStr = (String)rList.get(c);
							if("ALL".equalsIgnoreCase(faculty)){
								for(int b=0; b<facultyList.size(); b++){
									Faculty fac = (Faculty)facultyList.get(b);
									List progList = this.getProgram(programList, fac.getFacultyCode(), awardType);
									if("ALL".equalsIgnoreCase(program)){
										for(int a=0; a<progList.size(); a++){
											String prog = (String)progList.get(a);
											List majList = this.getMajor(majorList, prog);
											if("ALL".equalsIgnoreCase(major)){
												for(int x=0; x<majList.size(); x++){
													String maj = (String)majList.get(x);
													NewCourseInfo info = new NewCourseInfo();
													info.setRegion(regionStr);
													info.setFacultyCode(fac.getFacultyCode());
													info.setProgramCode(prog);
													info.setMajorCode(maj);
													info.setStatus(status);
													info.setCourseCode(course);
													info.setIsbn(isbn);
													info.setIntake(intake);
													info.setAwardType(awardType);
													infoList.add(info);
												}
											}else if(majSet.contains(major)){
												NewCourseInfo info = new NewCourseInfo();
												info.setRegion(regionStr);
												info.setFacultyCode(fac.getFacultyCode());
												info.setProgramCode(prog);
												info.setMajorCode(major);
												info.setStatus(status);
												info.setCourseCode(course);
												info.setIsbn(isbn);
												info.setIntake(intake);
												info.setAwardType(awardType);
												infoList.add(info);
											}
										}
									}else if(progSet.contains(program)){
										List majList = this.getMajor(majorList, program);
										if("ALL".equalsIgnoreCase(major)){
											for(int x=0; x<majList.size(); x++){
												String maj = (String)majList.get(x);
												NewCourseInfo info = new NewCourseInfo();
												info.setRegion(regionStr);
												info.setFacultyCode(fac.getFacultyCode());
												info.setProgramCode(program);
												info.setMajorCode(maj);
												info.setStatus(status);
												info.setCourseCode(course);
												info.setIsbn(isbn);
												info.setIntake(intake);
												info.setAwardType(awardType);
												infoList.add(info);
											}
										}else if(majSet.contains(major)){
											NewCourseInfo info = new NewCourseInfo();
											info.setRegion(regionStr);
											info.setFacultyCode(fac.getFacultyCode());
											info.setProgramCode(program);
											info.setMajorCode(major);
											info.setStatus(status);
											info.setCourseCode(course);
											info.setIsbn(isbn);
											info.setIntake(intake);
											info.setAwardType(awardType);
											infoList.add(info);
										}
									}
								}
							}else if(facultySet.contains(faculty)){
								List progList = this.getProgram(programList, faculty, awardType);
								if("ALL".equalsIgnoreCase(program)){
									for(int a=0; a<progList.size(); a++){
										String prog = (String)progList.get(a);
										List majList = this.getMajor(majorList, prog);
										if("ALL".equalsIgnoreCase(major)){
											for(int x=0; x<majList.size(); x++){
												String maj = (String)majList.get(x);
												NewCourseInfo info = new NewCourseInfo();
												info.setRegion(regionStr);
												info.setFacultyCode(faculty);
												info.setProgramCode(prog);
												info.setMajorCode(maj);
												info.setStatus(status);
												info.setCourseCode(course);
												info.setIsbn(isbn);
												info.setIntake(intake);
												info.setAwardType(awardType);
												infoList.add(info);
											}
										}else if(majSet.contains(major)){
											NewCourseInfo info = new NewCourseInfo();
											info.setRegion(regionStr);
											info.setFacultyCode(faculty);
											info.setProgramCode(prog);
											info.setMajorCode(major);
											info.setStatus(status);
											info.setCourseCode(course);
											info.setIsbn(isbn);
											info.setIntake(intake);
											info.setAwardType(awardType);
											infoList.add(info);
										}
									}
								}else if(progSet.contains(program)){
									List majList = this.getMajor(majorList, program);
									if("ALL".equalsIgnoreCase(major)){
										for(int x=0; x<majList.size(); x++){
											String maj = (String)majList.get(x);
											NewCourseInfo info = new NewCourseInfo();
											info.setRegion(regionStr);
											info.setFacultyCode(faculty);
											info.setProgramCode(program);
											info.setMajorCode(maj);
											info.setStatus(status);
											info.setCourseCode(course);
											info.setIsbn(isbn);
											info.setIntake(intake);
											info.setAwardType(awardType);
											infoList.add(info);
										}
									}else if(majSet.contains(major)){
										NewCourseInfo info = new NewCourseInfo();
										info.setRegion(regionStr);
										info.setFacultyCode(faculty);
										info.setProgramCode(program);
										info.setMajorCode(major);
										info.setStatus(status);
										info.setCourseCode(course);
										info.setIsbn(isbn);
										info.setIntake(intake);
										info.setAwardType(awardType);
										infoList.add(info);
									}
								}
							}
						}
					}else if(regionSet.contains(region)){
						if("ALL".equalsIgnoreCase(faculty)){
							for(int b=0; b<facultyList.size(); b++){
								Faculty fac = (Faculty)facultyList.get(b);
								List progList = this.getProgram(programList, fac.getFacultyCode(), awardType);
								if("ALL".equalsIgnoreCase(program)){
									for(int a=0; a<progList.size(); a++){
										String prog = (String)progList.get(a);
										List majList = this.getMajor(majorList, prog);
										if("ALL".equalsIgnoreCase(major)){
											for(int x=0; x<majList.size(); x++){
												String maj = (String)majList.get(x);
												NewCourseInfo info = new NewCourseInfo();
												info.setRegion(region);
												info.setFacultyCode(fac.getFacultyCode());
												info.setProgramCode(prog);
												info.setMajorCode(maj);
												info.setStatus(status);
												info.setCourseCode(course);
												info.setIsbn(isbn);
												info.setIntake(intake);
												info.setAwardType(awardType);
												infoList.add(info);
											}
										}else if(majSet.contains(major)){
											NewCourseInfo info = new NewCourseInfo();
											info.setRegion(region);
											info.setFacultyCode(fac.getFacultyCode());
											info.setProgramCode(prog);
											info.setMajorCode(major);
											info.setStatus(status);
											info.setCourseCode(course);
											info.setIsbn(isbn);
											info.setIntake(intake);
											info.setAwardType(awardType);
											infoList.add(info);
										}
									}
								}else if(progSet.contains(program)){
									List majList = this.getMajor(majorList, program);
									if("ALL".equalsIgnoreCase(major)){
										for(int x=0; x<majList.size(); x++){
											String maj = (String)majList.get(x);
											NewCourseInfo info = new NewCourseInfo();
											info.setRegion(region);
											info.setFacultyCode(fac.getFacultyCode());
											info.setProgramCode(program);
											info.setMajorCode(maj);
											info.setStatus(status);
											info.setCourseCode(course);
											info.setIsbn(isbn);
											info.setIntake(intake);
											info.setAwardType(awardType);
											infoList.add(info);
										}
									}else if(majSet.contains(major)){
										NewCourseInfo info = new NewCourseInfo();
										info.setRegion(region);
										info.setFacultyCode(fac.getFacultyCode());
										info.setProgramCode(program);
										info.setMajorCode(major);
										info.setStatus(status);
										info.setCourseCode(course);
										info.setIsbn(isbn);
										info.setIntake(intake);
										info.setAwardType(awardType);
										infoList.add(info);
									}
								}
							}
						}else if(facultySet.contains(faculty)){
							List progList = this.getProgram(programList, faculty, awardType);
							if("ALL".equalsIgnoreCase(program)){
								for(int a=0; a<progList.size(); a++){
									String prog = (String)progList.get(a);
									List majList = this.getMajor(majorList, prog);
									if("ALL".equalsIgnoreCase(major)){
										for(int x=0; x<majList.size(); x++){
											String maj = (String)majList.get(x);
											NewCourseInfo info = new NewCourseInfo();
											info.setRegion(region);
											info.setFacultyCode(faculty);
											info.setProgramCode(prog);
											info.setMajorCode(maj);
											info.setStatus(status);
											info.setCourseCode(course);
											info.setIsbn(isbn);
											info.setIntake(intake);
											info.setAwardType(awardType);
											infoList.add(info);
										}
									}else if(majSet.contains(major)){
										NewCourseInfo info = new NewCourseInfo();
										info.setRegion(region);
										info.setFacultyCode(faculty);
										info.setProgramCode(prog);
										info.setMajorCode(major);
										info.setStatus(status);
										info.setCourseCode(course);
										info.setIsbn(isbn);
										info.setIntake(intake);
										info.setAwardType(awardType);
										infoList.add(info);
									}
								}
							}else if(progSet.contains(program)){
								List majList = this.getMajor(majorList, program);
								if("ALL".equalsIgnoreCase(major)){
									for(int x=0; x<majList.size(); x++){
										String maj = (String)majList.get(x);
										NewCourseInfo info = new NewCourseInfo();
										info.setRegion(region);
										info.setFacultyCode(faculty);
										info.setProgramCode(program);
										info.setMajorCode(maj);
										info.setStatus(status);
										info.setCourseCode(course);
										info.setIsbn(isbn);
										info.setIntake(intake);
										info.setAwardType(awardType);
										infoList.add(info);
									}
								}else if(majSet.contains(major)){
									NewCourseInfo info = new NewCourseInfo();
									info.setRegion(region);
									info.setFacultyCode(faculty);
									info.setProgramCode(program);
									info.setMajorCode(major);
									info.setStatus(status);
									info.setCourseCode(course);
									info.setIsbn(isbn);
									info.setIntake(intake);
									info.setAwardType(awardType);
									infoList.add(info);
								}
							}
						}
					}
				}
				if(!err.isEmpty()){
					map.put(i+1, err);
				}
			}
			list.add(map);
			list.add(infoList);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(workbook!=null){
				workbook.close();
			}
		}
		return list;
	}
	
	private List getProgram(List programList, String facultyCode, String awardType){
		List list = new ArrayList();
		try{
			for(int i=0; i<programList.size(); i++){
				Program p = (Program)programList.get(i);
				if(p.getFacultyCode().equals(facultyCode) && p.getAwardType().equals(awardType)){
					list.add(p.getProgramCode());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	private List getMajor(List majorList, String programCode){
		List list = new ArrayList();
		try{
			for(int i=0; i<majorList.size(); i++){
				Major m = (Major)majorList.get(i);
				if(m.getProgramCode().equals(programCode)){
					list.add(m.getMajorCode());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	private boolean checkProgram(List programList, String facultyCode, String programCode){
		boolean flag = false;
		try{
			HashSet set = new HashSet();
			for(int i=0; i<programList.size(); i++){
				Program p = (Program)programList.get(i);
				if(p.getFacultyCode().equals(facultyCode)){
					set.add(p.getProgramCode());
				}
			}
			if(set.contains(programCode)){
				flag = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	
	private boolean checkMajor(List majorList, String programCode, String majorCode){
		boolean flag = false;
		try{
			HashSet set = new HashSet();
			for(int i=0; i<majorList.size(); i++){
				Major major = (Major)majorList.get(i);
				if(major.getProgramCode().equals(programCode)){
					set.add(major.getMajorCode());
				}
			}
			if(set.contains(majorCode)){
				flag = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	
	private boolean checkStr(String str){
		boolean flag = true;
		try{
			String number = "abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLNMOPQRSTUVWXYZ";
			for(int i=0; i<str.length(); i++){
				char point = str.charAt(i);
				if(number.indexOf(point) == -1){	
					flag = false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	
	private boolean checkExcelFormat(FileItem fi){
		boolean flag = false;
		Workbook workbook = null;
		try{
			workbook = Workbook.getWorkbook(fi.getInputStream());
			Sheet sheet = workbook.getSheet(0);
			int colnum = sheet.getColumns();
			if(colnum == 8){
				flag = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(workbook != null){
				workbook.close();
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
