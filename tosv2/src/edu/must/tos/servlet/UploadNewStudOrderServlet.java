package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.fileupload.FileItem;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.Course;
import edu.must.tos.bean.Faculty;
import edu.must.tos.bean.Major;
import edu.must.tos.bean.NewsOrder;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.OrderInfo;
import edu.must.tos.bean.Program;
import edu.must.tos.bean.Student;
import edu.must.tos.bean.VBookFeeForApp;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookRelDAOImpl;
import edu.must.tos.impl.CourseDAOImpl;
import edu.must.tos.impl.FacultyDAOImpl;
import edu.must.tos.impl.MajorDAOImpl;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.ProgramDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.VBookFeeForAppDAOImpl;
import edu.must.tos.util.UploadFile;

public class UploadNewStudOrderServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UploadNewStudOrderServlet() {
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
			//Savepoint savepoint = conn.setSavepoint();
			
			OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
			OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
			
			int size = 10;	//設置上傳文件大小
			
			List list = UploadFile.upload(request, size);
			List elementsList = null;
			String sizeInfo = "";
			FileItem fileItem = null;
			if(list != null && list.size() == 3){
				fileItem = (FileItem)list.get(0);
				elementsList = (List)list.get(1);
				sizeInfo = (String)list.get(2);
			}
			if(sizeInfo.equals("wrong")){
				request.setAttribute("type", "uploadNewStudOrder");
				request.setAttribute("msg", "上傳文件超過"+size+"M！");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			}else{
				if(fileItem != null){
					//boolean excelFormat = checkExcelFormat(fileItem);
					boolean excelFormat = true;
					if(excelFormat){
						List dataList = getExcelDate(conn, fileItem, request);
						/*
						* dataList.getList(0)為Error信息
						* dataList.getList(1)為Order信息
						* dateList.getList(2)為Order Detail信息
						*/
						Map map = (HashMap)dataList.get(0);
						//如果通過Excel數據檢查後，沒有Error信息
						if(map.isEmpty()){
							//獲取Order信息
							List<NewsOrder> orderList = new ArrayList((HashSet)dataList.get(1));
							//獲取Order Detail信息
							List detailList = (List)dataList.get(2);
							if(orderList != null && detailList != null){
								boolean totalResult = false;
								String error = "";
								List<String> errorList = new ArrayList();
								
								for(int i=0; i<orderList.size(); i++){
									NewsOrder order = new NewsOrder();
									order = (NewsOrder)orderList.get(i);
									//讀取Excel數據，使用applicant#
									String studNo = order.getApplicantNo();
									
									String studentNo = null;
									String applicantNo = null;
									if(order.getStudentNo() != null && !order.getStudentNo().equals("") ){
										studentNo = order.getStudentNo();
									}
									if(order.getApplicantNo() != null && !order.getApplicantNo().equals("") ){
										applicantNo = order.getApplicantNo();
									}
									
									//整理訂單信息
									Order updOrder = new Order();
									updOrder.setStudentNo(studNo);
									updOrder.setOrderIntake(order.getIntake());
									updOrder.setOrderSeqNo(0);
									updOrder.setPaidAmount(order.getPaidAmount());
									updOrder.setPaidCurrency(order.getPaidCurrency());
									updOrder.setPaidStatus(order.getPaidStatus());
									updOrder.setCreUid("NEWSTUD");
									updOrder.setUpdUid("NEWSTUD");
									updOrder.setAmerceMount(0);
									updOrder.setChaUid("NEWSTUD");
									updOrder.setFineforlatepay(0);
									updOrder.setDifference(null);
									updOrder.setShippingFee(0);
									updOrder.setNetpaidcurrency(order.getPaidCurrency());
									updOrder.setNetpaidamount(order.getPaidAmount());
									updOrder.setCurrate(Double.valueOf("1"));
									updOrder.setActInd("Y");
									updOrder.setCreDate(new Date());
									updOrder.setUpdDate(new Date());
									//收集訂單細明資料
									List tempList = new ArrayList();
									for(int j=0; j<detailList.size(); j++){
										OrDetail detail = new OrDetail();
										detail = (OrDetail)detailList.get(j);
										//Order Detail的studentNo保存是applicantNo值
										if(detail.getStudentNo().equals(studNo)){
											OrderInfo orderInfo = new OrderInfo();
											orderInfo.setOrderIntake(detail.getOrderIntake());
											orderInfo.setOrderSeqNo(0);
											orderInfo.setIsbn(detail.getIsbn());
											orderInfo.setConfirmQty(detail.getConfirmQty());
											orderInfo.setCourseCode(detail.getCourseCode());
											orderInfo.setStudentNo(detail.getStudentNo()); //studentNo為Excel中的applicantNo
											orderInfo.setActInd("Y");
											orderInfo.setCreUid("NEWSTUD");
											orderInfo.setUpdUid("NEWSTUD");
											orderInfo.setCreDate(new Date());
											orderInfo.setUpdDate(new Date());
											tempList.add(orderInfo);
										}
									}
									
									Order db_order2 = null;
									//檢查是否有以student#的訂單資料
									
									/* 新生訂書的細明記錄在Tordetail中，studentNo等於applicantNo
									Order db_order1 = null;
									db_order1 = orderDAOImpl.getNewsOrderInfo(conn, order.getStudentNo(), order.getIntake()); 
									*/
									
									//檢查是否有以applicant#的訂單資料
									db_order2 = orderDAOImpl.getNewsOrderInfo(conn, order.getApplicantNo(), order.getIntake());
									
									/*
									if(db_order1!=null && db_order2!=null){
										error = "學員編號為 "+order.getStudentNo()+" 存在兩份導入新生訂單記錄！";
										errorList.add(error);
										break;
									}else 
									*/ 
									
									if(db_order2 == null){
										int orderSeqNo = orderDAOImpl.addOrderSeqNoInfo(conn, updOrder);
										boolean detailFlag = false;
										//List orDetailList = new ArrayList();
										if(tempList != null && !tempList.isEmpty()){
											for(int j=0; j<tempList.size(); j++){
												OrderInfo orderInfo = (OrderInfo)tempList.get(j);
												OrDetail detail = new OrDetail();
												detail.setOrderSeqNo(orderSeqNo);
												detail.setOrderIntake(orderInfo.getOrderIntake());
												detail.setIsbn(orderInfo.getIsbn());
												detail.setConfirmQty(orderInfo.getConfirmQty());
												detail.setCourseCode(orderInfo.getCourseCode());
												detail.setStudentNo(orderInfo.getStudentNo());
												detail.setCreUid(orderInfo.getCreUid());
												detail.setUpdUid(orderInfo.getUpdUid());
												detail.setActInd(orderInfo.getActInd());
												detail.setCreDate(orderInfo.getCreDate());
												detail.setUpdDate(orderInfo.getUpdDate());
												boolean checkRecd = orderDetailDAOImpl.getDetailSeqNoByIsbn(conn, detail);
												if(checkRecd){
													detailFlag = orderDetailDAOImpl.updateDetailInfo(conn, detail);
												}else{
													detailFlag = orderDetailDAOImpl.insertDetailInfo(conn, detail);
												}
												if(!detailFlag){
													break;
												}
											}
										}
										//boolean detailFlag = orderDetailDAOImpl.addDetailSeqNoInfo(conn, orDetailList);
										
										if(orderSeqNo != 0 && detailFlag){
											totalResult = true;
										}else{
											error = "申請編號為 "+studNo+" 的訂單記錄保存失敗！";
											errorList.add(error);
											totalResult = false;
											break;
										}
									} else {
										String isPay = "";
										String noType = "";
										String tempNo = "";
										int orderSeqNo = 0;
										
										noType = "學員申請編號";
										tempNo = db_order2.getStudentNo();
										isPay = db_order2.getPaidStatus();
										orderSeqNo = db_order2.getOrderSeqNo();
										
										if(isPay.equals("Y")){
											errorList.add(noType + "為 " + tempNo + " 已有付費的訂書記錄");
											totalResult = true;
										}else{
											OrDetail od = new OrDetail();
											od.setActInd("N");
											od.setUpdUid("NEWSTUD");
											od.setUpdDate(new Date());
											od.setOrderSeqNo(orderSeqNo);
											
											boolean delOrDetialFlag = false;				
											boolean detailFlag = false;
											
											boolean updOrderFlag = false;
											db_order2.setPaidCurrency(updOrder.getPaidCurrency());
											db_order2.setPaidAmount(updOrder.getPaidAmount());
											db_order2.setPaidStatus(updOrder.getPaidStatus());
											db_order2.setActInd(updOrder.getActInd());
											db_order2.setUpdDate(new Date());
											db_order2.setUpdUid(updOrder.getUpdUid());
											db_order2.setAmerceMount(updOrder.getAmerceMount());
											db_order2.setChaUid(updOrder.getChaUid());
											db_order2.setFineforlatepay(updOrder.getFineforlatepay());
											db_order2.setDifference(updOrder.getDifference());
											db_order2.setShippingFee(updOrder.getShippingFee());
											db_order2.setNetpaidcurrency(updOrder.getNetpaidcurrency());
											db_order2.setNetpaidamount(updOrder.getNetpaidamount());
											db_order2.setCurrate(updOrder.getCurrate());
											updOrderFlag = orderDAOImpl.updateOrderBySeqNo(conn, db_order2);
											//boolean updatePaidAmount = false;
											if(tempNo.equals(order.getApplicantNo())){
												//設置舊的資料actind為N
												delOrDetialFlag = orderDetailDAOImpl.delOrderDetailInfo(conn, od);
												List orDetailList = new ArrayList();
												if(tempList != null && !tempList.isEmpty()){
													for(int j=0; j<tempList.size(); j++){
														OrderInfo orderInfo = (OrderInfo)tempList.get(j);
														OrDetail detail = new OrDetail();
														detail.setOrderSeqNo(orderSeqNo);
														detail.setOrderIntake(orderInfo.getOrderIntake());
														detail.setIsbn(orderInfo.getIsbn());
														detail.setConfirmQty(orderInfo.getConfirmQty());
														detail.setCourseCode(orderInfo.getCourseCode());
														detail.setStudentNo(orderInfo.getStudentNo());
														detail.setCreUid(orderInfo.getCreUid());
														detail.setUpdUid(orderInfo.getUpdUid());
														detail.setActInd(orderInfo.getActInd());
														detail.setCreDate(orderInfo.getCreDate());
														detail.setUpdDate(orderInfo.getUpdDate());
														//orDetailList.add(detail);
														boolean checkRecd = orderDetailDAOImpl.getDetailSeqNoByIsbn(conn, detail);
														if(checkRecd){
															detailFlag = orderDetailDAOImpl.updateDetailInfo(conn, detail);
														}else{
															detailFlag = orderDetailDAOImpl.insertDetailInfo(conn, detail);
														}
														if(!detailFlag){
															break;
														}
													}
												}
												//添加訂書細明記錄
												//detailFlag = orderDetailDAOImpl.addDetailSeqNoInfo(conn, orDetailList);
											}
											/*
											* 											
											else{
												Order o = new Order();
												o.setStudentNo(tempNo);
												o.setActInd("Y");
												o.setUpdUid("NEWSTUD");
												o.setUpdDate(new Date());
												o.setOrderSeqNo(orderSeqNo);
												
												delOrDetialFlag = orderDetailDAOImpl.delOrderDetailInfo(conn, od);
												
												List orDetailList = new ArrayList();
												if(tempList!=null && !tempList.isEmpty()){
													for(int j=0;j<tempList.size();j++){
														OrderInfo orderInfo = (OrderInfo)tempList.get(j);
														OrDetail detail = new OrDetail();
														detail.setOrderSeqNo(orderSeqNo);
														detail.setOrderIntake(orderInfo.getOrderIntake());
														detail.setIsbn(orderInfo.getIsbn());
														detail.setConfirmQty(orderInfo.getConfirmQty());
														detail.setCourseCode(orderInfo.getCourseCode());
														detail.setStudentNo(orderInfo.getStudentNo());
														detail.setCreUid(orderInfo.getCreUid());
														detail.setUpdUid(orderInfo.getUpdUid());
														detail.setActInd(orderInfo.getActInd());
														detail.setCreDate(orderInfo.getCreDate());
														detail.setUpdDate(orderInfo.getUpdDate());
														orDetailList.add(detail);
													}
												}
												//添加訂書細明記錄
												detailFlag = orderDetailDAOImpl.addDetailSeqNoInfo(conn, orDetailList);
												//更新torder的studentNo值
												updOrderFlag = orderDAOImpl.updateOrderStudNoBySeqNo(conn, o);
												//result = orderDAOImpl.addOrderInfo(newTempList, order.getIntake(), order.getCreUid(), conn);
												//updatePaidAmount = orderDAOImpl.updateOrderPaidAmount(conn, updOrder);
											}
											*/
											
											if(delOrDetialFlag && detailFlag && updOrderFlag /*&& updatePaidAmount*/){
												totalResult = true;
											}else{
												error = noType + "為 " + tempNo + " 的訂單記錄保存失敗！";
												errorList.add(error);
												totalResult = false;
												break;
											}
										}
									}
								}
								if(totalResult){
									if(errorList.size() != orderList.size()){
										conn.commit();
										request.setAttribute("msg", "新生訂書信息導入成功！共導入"+(orderList.size()-errorList.size())+"條訂書記錄！");
									}else{
										conn.rollback();
										request.setAttribute("msg", "新生訂書信息導入失敗！該導入文件的新生訂書信息已存在或為付款狀態！");
									}
									request.setAttribute("type", "uploadNewStudOrder");
									request.setAttribute("errorList", errorList);
									
									request.getRequestDispatcher("show.jsp").forward(request, response);
								}else{
									conn.rollback();
									request.setAttribute("type", "uploadNewStudOrder");
									request.setAttribute("errorList", errorList);
									request.setAttribute("msg", "新生訂書信息導入失敗！該導入文件的新生訂書信息沒有被導入！");
									request.getRequestDispatcher("show.jsp").forward(request, response);
								}
							}else{
								request.setAttribute("type", "uploadNewStudOrder");
								request.setAttribute("msg", "該導入文件沒有新生訂書信息！");
								request.getRequestDispatcher("msg.jsp").forward(request, response);
							}
						}else{
							request.setAttribute("type", "uploadNewStudOrder");
							request.setAttribute("map", map);
							request.getRequestDispatcher("errExcel.jsp").forward(request, response);
						}
					}else{
						request.setAttribute("msg", "新生訂書資料導入文件不符合，請檢查！");
						request.setAttribute("type", "uploadNewStudOrder");
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

	/**
	 * Intake、studentNo、applicantNo、isbn、confirmQty和courseCode五欄必須檢查
	 * @param conn
	 * @param fi
	 * @param request
	 * @return
	 */
	private List getExcelDate(Connection conn, FileItem fi, HttpServletRequest request){
		List list = new ArrayList();
		Workbook workbook = null;
		Map map = new HashMap();
		HttpSession session = request.getSession();
		try{
			workbook = Workbook.getWorkbook(fi.getInputStream());
			Sheet sheet = workbook.getSheet(0);
			
			//當前學院
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			String key = "CURRINTAKE";
			String intake = sysConfigDAOImpl.getCurIntake(conn, key);
						
			//圖書資料
			BookDAOImpl bookImpl = new BookDAOImpl();
			List bookList = bookImpl.getAllBook(conn);
			HashSet bookSet = new HashSet();
			for(int x=0; x<bookList.size(); x++){
				Book book = (Book)bookList.get(x);
				bookSet.add(book.getIsbn());
			}
			
			//學生資料，包括status in ('A','D','UR') or status is null的資料
			Student newStud = new Student();
			newStud.setAcademicYear(0);
			StudentDAOImpl studentImpl = new StudentDAOImpl();
			List<Student> newStudList = studentImpl.getNewStudList(conn, newStud);
			HashSet studentNoSet = new HashSet();
			HashSet applicantNoSet = new HashSet();
			
			for(Student stud : newStudList){
				studentNoSet.add(stud.getStudentNo());
				applicantNoSet.add(stud.getApplicantNo());
			}
			
			//學院資料
			FacultyDAOImpl facultyImpl = new FacultyDAOImpl();
			List<Faculty> facultyList = facultyImpl.getFacultyList(conn);
			
			//課程資料
			ProgramDAOImpl programImpl = new ProgramDAOImpl();
			List<Program> programList = programImpl.getProgramInfo(conn);
			
			//專業資料
			MajorDAOImpl majorDAOImpl = new MajorDAOImpl();
			List<Major> majorList = majorDAOImpl.getMajorList(conn);
			
			//科目資料
			CourseDAOImpl courseImpl = new CourseDAOImpl();
			HashSet courseSet = new HashSet();
			List<Course> crsList = courseImpl.getCourseCode(conn, null);
			for(Course crs : crsList){
				courseSet.add(crs.getCourseCode());
			}
			//獲取書與科目的關係
			BookRel bookRel = new BookRel();
			bookRel.setIntake(intake);
			bookRel.setActInd("Y");
			BookRelDAOImpl bookRelImpl = new BookRelDAOImpl();
			List bookRelList = bookRelImpl.getBookRelList(conn, bookRel);
			
			//獲取各個學院預設金額參數
			//List<SysConfig> prePaidParamList = sysConfigDAOImpl.getPrepaidParam(conn);
			
			//獲取VBookFeeForApp表對應學期的數據
			VBookFeeForAppDAOImpl vBookFeeForAppDAOImpl = new VBookFeeForAppDAOImpl();
			VBookFeeForApp v = new VBookFeeForApp();
			v.setApplicantNo("");
			v.setStudentNo("");
			v.setPaidIntake(intake);
			List<VBookFeeForApp> vBookFeeList = vBookFeeForAppDAOImpl.getVBookFeeForAppList(conn, v);
			
			HashSet orderSet = new HashSet();
			HashSet studNoSet = new HashSet();
			
			List detailList = new ArrayList();
			
			for(int i=1; i<sheet.getRows(); i++){
				Map err = new HashMap();
				//檢查學期
				String e_intake = sheet.getCell(0, i).getContents().trim().replace("\\n\\r", "");
				boolean check1 = this.isNumeric(e_intake);
				if(check1){
					if(e_intake.length() == 4){
						if(!e_intake.equals(intake)){
							err.put(1, "第1列學期值與當前學期不相符！");
						}
					}else{
						err.put(1, "第1列學期位數需要4位！");
					}
				}else{
					err.put(1, "第1列學期填寫不正確！");
				}
				
				//學員編號
				String e_studentno = sheet.getCell(1, i).getContents().trim().replace("\\n\\r", "");
				
				//申請編號
				String e_applicantno = sheet.getCell(7, i).getContents().trim().replace("\\n\\r", "");
				
				/* 不需要檢查Excel表上的學生編號，學生編號可以為空
				if(e_studentno==null || e_studentno.equals("")){
					if(e_applicantno==null || e_applicantno.equals("")){
						err.put(2, "第2列學員編號為空！");
					}
				}else{
					if(e_studentno.length()>18){
						err.put(2, "第2列學員編號長度超過18位！");
					}else {
						if(!studentNoSet.contains(e_studentno)){
							err.put(2, "第2列學員編號不存在! ");
						}
					}
				}
				*/
				
				if(e_applicantno == null || e_applicantno.equals("")){
					err.put(8, "第8列學員申請編號為空！");
				}else{
					if(e_applicantno.length() > 20){
						err.put(8, "第8列學員申請編號長度超過20位！");
					}else{
						if(!applicantNoSet.contains(e_applicantno)){
							err.put(8, "第8列學員申請編號不存在！");
						}else{
							/*
							HashSet appSet = new HashSet();
							for(Student stud : newStudList){
								if(e_studentno!=null && e_studentno.equals(stud.getStudentNo()) ){
									appSet.add(stud.getApplicantNo());
								}
							}
							if(appSet.size()>0 && !appSet.contains(e_applicantno)){
								err.put(7, "第7列學員申請編號與學員編號不對應！Excel:studNo="+e_studentno+" appNo="+e_applicantno);
							} 
							*/
							//System.out.println(e_applicantno+" is okay!");
						}
					}
				}
				
				//學員姓名
				String e_studentname = sheet.getCell(2, i).getContents().trim().replace("\\n\\r", "");
				//學員英文姓名
				String e_studentename = sheet.getCell(2, i).getContents().trim().replace("\\n\\r", "");
				
				//學院編號
				String e_faculty = sheet.getCell(4, i).getContents().trim().replace("\\n\\r", "");
//				String e_facultycode = "";
//				if(e_faculty==null || e_faculty.equals("")){
//					err.put(5, "第5列學院編號為空！");
//				}else{
//					boolean facultyFlag = true;
//					for(Faculty faculty : facultyList){
//						if(!faculty.getFacultyCode().equals(e_faculty)){
//							facultyFlag = false;
//						}else{
//							e_facultycode = faculty.getFacultyCode();
//							facultyFlag = true;
//							break;
//						}
//					}
//					if(!facultyFlag){
//						err.put(5, "第5列學院編號填寫有誤！");
//					}
//				}
				//課程名稱
				String e_program = sheet.getCell(5, i).getContents().trim().replace("\\n\\r", "");
//				if(e_program==null || e_program.equals("")){
//					err.put(6, "第6列課程編號為空！");
//				}else{
//					boolean programFlag = false;
//					HashSet programSet = new HashSet();
//					for(Program p : programList){
//						if(p.getFacultyCode().equals(e_facultycode)){
//							programSet.add(p.getProgramCode());
//						}
//					}
//					if(!programSet.contains(e_program)){
//						err.put(6, "第6列課程編號與學院標號不對應！");
//					}
//				}
				//專業編號
				String e_major = sheet.getCell(6, i).getContents().trim().replace("\\n\\r", "");
				/*
				boolean isAvailable = false;
				if(!newStudList.isEmpty()){
					for(Student stu : newStudList){
						if(!e_studentno.equals("") && !e_applicantno.equals("")){
							if(stu.getStudentNo()!=null && stu.getStudentNo().equals(e_studentno) 
									&& stu.getApplicantNo().equals(e_applicantno) ){
								isAvailable = true;
								break;
							}
						}else if(!e_studentno.equals("") && e_applicantno.equals("")){
							if(stu.getStudentNo()!=null && stu.getStudentNo().equals(e_studentno) ){
								isAvailable = true;
								if(stu.getApplicantNo()!=null && !stu.getApplicantNo().equals("")){
									e_applicantno = stu.getApplicantNo();
								}
								break;
							}
						}else if(e_studentno.equals("") && !e_applicantno.equals("")){
							if(stu.getApplicantNo()!=null && stu.getApplicantNo().equals(e_applicantno)){
								isAvailable = true;
								if(stu.getStudentNo()!=null && !stu.getStudentNo().equals("")){
									e_studentno = stu.getStudentNo();
								}
								break;
							}
						}
					}
				} 
				*/
				
				//錄取狀況
				String e_status = sheet.getCell(8, i).getContents().trim().replace("\\n\\r", "");
				//地區
				String e_region = sheet.getCell(9, i).getContents().trim().replace("\\n\\r", "");
				//圖書ISBN
				String e_isbn = sheet.getCell(10, i).getContents().trim().replace("\\n\\r", "");
				boolean check5 = e_isbn.matches("^[0-9A-Za-z$]*$");
				if(!check5 || (e_isbn == null || e_isbn.equals(""))){
					err.put(11, "第11列ISBN編號填寫有誤，或為空！");
				}else{
					if(e_isbn.length() > 30){
						err.put(11, "第11列ISBN編號長度超過30位！");
					}else{
						if(!bookSet.contains(e_isbn)){
							err.put(11, "第11列ISBN不存在數據庫中！");
						}
					}
				}
				//科目編號
				HashSet checkSet = new HashSet();
				for(int j=0; j<bookRelList.size(); j++){
					BookRel r = new BookRel();
					r = (BookRel)bookRelList.get(j);
					if(r.getIsbn().equals(e_isbn)){
						checkSet.add(r.getCourseCode());
					}
				}
				String e_coursecode = sheet.getCell(11, i).getContents().trim().replace("\\n\\r", "");
				if(e_coursecode == null || e_coursecode.equals("")){
					err.put(12, "第12列科目編號填寫有誤！");
				}else{
					if(e_coursecode.length() > 20){
						err.put(12, "第12列科目編號長度超過20位！");
					}else{
						if(!courseSet.contains(e_coursecode)){
							err.put(12, "第12列科目編號不存在數據庫中！");
						}else{
							if(!checkSet.contains(e_coursecode)){
								err.put(12, "第12列科目編與該ISBN圖書沒有聯繫！");
							}
						}
					}
				}
				//確定數量
				String e_confirm = sheet.getCell(12, i).getContents().trim().replace("\\n\\r", "");
				
				if(e_confirm == null || e_confirm.equals("")){
					err.put(13, "第13列確定數量為空！");
				}else{
					if(!this.isNumeric(e_confirm)){
						err.put(13, "第13列確定數量不是數字！");
					}
				}
				
				if((e_intake == null || e_intake.equals("")) && (e_studentno == null || e_studentno.equals("")) && 
						(e_studentname == null || e_studentname.equals("")) && (e_studentename == null || e_studentename.equals("")) && 
						(e_faculty == null || e_faculty.equals("")) && (e_program == null || e_program.equals("")) &&
						(e_major == null || e_major.equals("")) &&
						(e_applicantno == null || e_applicantno.equals("")) && (e_status == null || e_status.equals("")) && 
						(e_region == null || e_region.equals("")) && (e_isbn == null || e_isbn.equals("")) && 
						(e_coursecode == null || e_coursecode.equals("")) && (e_confirm == null || e_confirm.equals(""))){
					break;
				}else{
					if(!err.isEmpty()){
						map.put(i+1, err);
					}
				}
				//---------------------------------------- end check 
				
				double paidAmount = 0;
				String paidCurrency = "";
				String paidStatus = "N";
				
				for(VBookFeeForApp vBookFee : vBookFeeList){
					String v_studentNo = vBookFee.getStudentNo();
					String v_applicantNo = vBookFee.getApplicantNo();
					if((e_studentno != null && !e_studentno.equals("") && e_studentno.equals(v_studentNo)) 
							|| (e_applicantno != null && !e_applicantno.equals("") && e_applicantno.equals(v_applicantNo))){
						//TOrder的paidAmount使用VBookFeeForApp表的paidAmount值
						//paidAmount = vBookFee.getPrePaid();
						paidAmount = vBookFee.getPaidAmount();
						
						if(!vBookFee.getPaidCurrency().toUpperCase().equals("RMB") && !vBookFee.getPaidCurrency().toUpperCase().equals("MOP")){
							paidCurrency = "MOP";
						}else{
							paidCurrency = vBookFee.getPaidCurrency();
						}
						break;
					}
				}
				NewsOrder order = new NewsOrder();
				order.setIntake(e_intake);
				order.setStudentNo(e_studentno);
				order.setApplicantNo(e_applicantno);
				order.setPaidAmount(paidAmount);
				order.setPaidCurrency(paidCurrency);
				order.setPaidStatus(paidStatus);
				order.setActind("Y");
				order.setCreUid("NEWSTUD");
				
				//如有相同的申請編號，只保存一個Order記錄
				if(!studNoSet.contains(e_applicantno)){
					studNoSet.add(e_applicantno);
					orderSet.add(order);
				}
				
				OrDetail detail = new OrDetail();
				//以申請編號爲準
				detail.setStudentNo(e_applicantno);
				detail.setOrderIntake(e_intake);
				detail.setIsbn(e_isbn);
				if(this.isNumeric(e_confirm)){
					if(e_confirm.equals("")){
						e_confirm = "0";
					}
					detail.setConfirmQty(Integer.parseInt(e_confirm));
				}
				detail.setActInd("Y");
				detail.setCreUid("NEWSTUD");
				detail.setCourseCode(e_coursecode);
				
				detailList.add(detail);				
			}
			list.add(map);
			list.add(orderSet);
			list.add(detailList);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(workbook != null){
				workbook.close();
			}
		}
		return list;
	}
	
	private boolean checkExcelFormat(FileItem fi){
		boolean flag = false;
		Workbook workbook = null;
		try{
			workbook = Workbook.getWorkbook(fi.getInputStream());
			Sheet sheet = workbook.getSheet(0);
			int colnums = sheet.getColumns();
			if(colnums == 13){
				flag = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(workbook!=null){
				workbook.close();
			}
		}
		return flag;
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
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}