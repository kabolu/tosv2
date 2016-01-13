package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.must.tos.bean.V_NewStudOrder;
import edu.must.tos.impl.NewStudOrderDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.util.CellFormat;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportNewStudOrderInfoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ExportNewStudOrderInfoServlet() {
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
			
			NewStudOrderDAOImpl newStudOrderImpl = new NewStudOrderDAOImpl();
			//當前學院
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			String key = "CURRINTAKE";
			String intake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			String oprType = null;
			if(request.getParameter("oprType") != null){
				oprType = (String)request.getParameter("oprType");
			}
			if(oprType != null && oprType.equals("export")){
				String exportType = request.getParameter("exportType");
				if(exportType != null && exportType.equals("A")){
					V_NewStudOrder v = new V_NewStudOrder();
					v.setIntake(intake);
					//獲取數據
					List newCourseInfoList = newStudOrderImpl.getNewStudCourseInfo(conn, intake);
					if(newCourseInfoList != null && !newCourseInfoList.isEmpty() ){
						List resultList = newStudOrderImpl.getNewStudOrderInfo(conn, v);
						if(resultList != null && !resultList.isEmpty()){
							response.reset();
							response.setContentType("application/octet-stream");
							response.addHeader("Content-Disposition" , "attachment ; filename = NewStudOrderInfo-A.xls " );
							
							OutputStream os = response.getOutputStream();
							WritableWorkbook workbook = Workbook.createWorkbook(os);
							
							//標頭信息
							Date date = new Date();
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String dateStr = dateFormat.format(date);
							
							
							int pageNumber = resultList.size();
							int defuatRow = 60000;
							int maxPage = pageNumber;
							if(maxPage % defuatRow == 0){
								maxPage = maxPage / defuatRow;
							}else{
								maxPage = maxPage / defuatRow + 1;
							}
							//表格式樣
							WritableCellFormat cellFormat = CellFormat.getCellFormat();
							for(int page=0; page<maxPage; page++){
								int start = page * defuatRow;
								int over = (page+1)*defuatRow;
								int count = pageNumber - over;
								if(count <= 0){
									over = pageNumber;
								}
								
								WritableSheet worksheet = workbook.createSheet("NewStudOrderInfo-A"+page, page);
								
								String[] titleName = {"學期","學員編號","學員姓名","學員英文名稱","學院編號","課程編號","專業編號","申請編號","錄取狀況","地區","圖書ISBN","圖書預估價（MOP）","科目編號","確定數量","訂書日期"};
								for(int i=0; i<titleName.length; i++){
									 Label label = new Label(i, 0, titleName[i], cellFormat);
									 worksheet.addCell(label);
									 worksheet.setColumnView(i, titleName[i].getBytes().length + 2);
								}
								for(int i=start; i<over; i++){
									int j = 0;
									V_NewStudOrder info = new V_NewStudOrder();
									info = (V_NewStudOrder)resultList.get(i);
									worksheet.addCell(new Label(j++, i-start+1, info.getIntake()));
									worksheet.addCell(new Label(j++, i-start+1, info.getStudentNo()));
									worksheet.addCell(new Label(j++, i-start+1, info.getStudentName()));
									worksheet.addCell(new Label(j++, i-start+1, info.getStudEngName()));
									worksheet.addCell(new Label(j++, i-start+1, info.getFacultyName()));
									worksheet.addCell(new Label(j++, i-start+1, info.getProgramName()));
									worksheet.addCell(new Label(j++, i-start+1, info.getMajorCode()));
									worksheet.addCell(new Label(j++, i-start+1, info.getApplicantNo()));
									worksheet.addCell(new Label(j++, i-start+1, info.getAcceptance()));
									worksheet.addCell(new Label(j++, i-start+1, info.getRegion()));
									worksheet.addCell(new Label(j++, i-start+1, info.getIsbn()));
									worksheet.addCell(new jxl.write.Number(j++, i-start+1, info.getMopPrice()));
									worksheet.addCell(new Label(j++, i-start+1, info.getCourseCode()));
									worksheet.addCell(new jxl.write.Number(j++, i-start+1, info.getConfirmQty()));
									if(info.getCreDate() != null){
										worksheet.addCell(new Label(j++, i-start+1, info.getCreDate().toString()));
									}else{
										worksheet.addCell(new Label(j++, i-start+1, ""));
									}
									/*if(info.getTranDate()!=null){
										worksheet.addCell(new Label(j++, i+1, trandateFormat.format(info.getTranDate())));
									}else{
										worksheet.addCell(new Label(j++, i+1, ""));
									}*/
									
								}
							}
							
							workbook.write();
							workbook.close();
							os.flush();
							os.close();
						}else{
							request.setAttribute("msg", "沒有任何記錄！記錄數為="+resultList.size());
							request.getRequestDispatcher("msg.jsp").forward(request, response);
						}
					}else{
						request.setAttribute("msg", "新生選科的資料還未被上載！記錄數為="+newCourseInfoList.size());
						request.getRequestDispatcher("msg.jsp").forward(request, response);
					}
				}else if(exportType != null && exportType.equals("B")){
					V_NewStudOrder v = new V_NewStudOrder();
					v.setIntake(intake);
					//獲取數據
					List newCourseInfoList = newStudOrderImpl.getNewStudCourseInfo(conn, intake);
					if(!newCourseInfoList.isEmpty() && newCourseInfoList.size()!=0){
						List resultList = newStudOrderImpl.getNewStudOrderInfoAndAcct(conn, v);
						if(resultList != null && !resultList.isEmpty()){
							response.reset();
							response.setContentType("application/octet-stream");
							response.addHeader("Content-Disposition" , "attachment ; filename = NewStudOrderInfo-B.xls " );
							
							OutputStream os = response.getOutputStream();
							WritableWorkbook workbook = Workbook.createWorkbook(os);
							
							//標頭信息
							Date date = new Date();
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String dateStr = dateFormat.format(date);
							
							//表格式樣
							WritableCellFormat cellFormat = CellFormat.getCellFormat();
							
//							int pageNumber = resultList.size();
//							int maxPage = pageNumber;
//							if(maxPage % 60000 == 0){
//								maxPage = maxPage / 60000;
//							}else{
//								maxPage = maxPage / 60000 + 1;
//							}
//							for(int page=0; page<maxPage; page++){
//								int start = page * 60000;
//								int over = (page+1)*60000;
//								int count = pageNumber - over;
//								if(count <= 0){
//									over = pageNumber;
//								}
//								
//							}
							int pageNumber = resultList.size();
							int defuatRow = 60000;
							int maxPage = pageNumber;
							if(maxPage % defuatRow == 0){
								maxPage = maxPage / defuatRow;
							}else{
								maxPage = maxPage / defuatRow + 1;
							}
							//表格式樣
							for(int page=0; page<maxPage; page++){
								int start = page * defuatRow;
								int over = (page+1)*defuatRow;
								int count = pageNumber - over;
								if(count <= 0){
									over = pageNumber;
								}
								WritableSheet worksheet = workbook.createSheet("NewStudOrderInfo-B"+page, page);
							
								String[] titleName = {"學期","學員編號","學員姓名","學員英文名稱","學院編號","課程編號","專業編號","申請編號","錄取狀況","地區","圖書ISBN",
										"科目編號","確定數量","付款金額","付款幣種","訂書日期","書費創建日期","書費修改日期"};
								for(int i=0; i<titleName.length; i++){
									 Label label = new Label(i, 0, titleName[i], cellFormat);
									 worksheet.addCell(label);
									 worksheet.setColumnView(i, titleName[i].getBytes().length + 2);
								}
								for(int k=start;k<over;k++){
									int j = 0;
									V_NewStudOrder info = new V_NewStudOrder();
									info = (V_NewStudOrder)resultList.get(k);
									worksheet.addCell(new Label(j++, k-start+1, info.getIntake()));
									worksheet.addCell(new Label(j++, k-start+1, info.getStudentNo()));
									worksheet.addCell(new Label(j++, k-start+1, info.getStudentName()));
									worksheet.addCell(new Label(j++, k-start+1, info.getStudEngName()));
									worksheet.addCell(new Label(j++, k-start+1, info.getFacultyName()));
									worksheet.addCell(new Label(j++, k-start+1, info.getProgramName()));
									worksheet.addCell(new Label(j++, k-start+1, info.getMajorCode()));
									worksheet.addCell(new Label(j++, k-start+1, info.getApplicantNo()));
									worksheet.addCell(new Label(j++, k-start+1, info.getAcceptance()));
									worksheet.addCell(new Label(j++, k-start+1, info.getRegion()));
									worksheet.addCell(new Label(j++, k-start+1, info.getIsbn()));
									worksheet.addCell(new Label(j++, k-start+1, info.getCourseCode()));
									worksheet.addCell(new jxl.write.Number(j++, k-start+1, info.getConfirmQty()));
									worksheet.addCell(new Label(j++, k-start+1, info.getPaidCurrency()));
									worksheet.addCell(new jxl.write.Number(j++, k-start+1, info.getPaidAmount()));
									if(info.getCreDate()!=null){
										worksheet.addCell(new Label(j++, k-start+1, dateFormat.format(info.getCreDate())));
									}else{
										worksheet.addCell(new Label(j++, k-start+1, ""));
									}
									if(info.getBookFeeCreDate()!=null){
										worksheet.addCell(new Label(j++, k-start+1, dateFormat.format(info.getBookFeeCreDate())));
									}else{
										worksheet.addCell(new Label(j++, k-start+1, ""));
									}
									if(info.getBookFeeUpdDate()!=null){
										worksheet.addCell(new Label(j++, k-start+1, dateFormat.format(info.getBookFeeUpdDate())));
									}else{
										worksheet.addCell(new Label(j++, k-start+1, ""));
									}
									
								}
							}
							workbook.write();
							workbook.close();
							os.flush();
							os.close();
						}else{
							request.setAttribute("msg", "沒有任何記錄！記錄數為="+resultList.size());
							request.getRequestDispatcher("msg.jsp").forward(request, response);
						}
					}else{
						request.setAttribute("msg", "新生選科的資料還未被上載！記錄數為="+newCourseInfoList.size());
						request.getRequestDispatcher("msg.jsp").forward(request, response);
					}
				}
			}else{
				request.getRequestDispatcher("exportNewStudOrderInfo.jsp").forward(request, response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(conn != null && !conn.isClosed())
					conn.close();
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
