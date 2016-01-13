package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import edu.must.tos.bean.SurveyQuest;
import edu.must.tos.bean.SurveyResp;
import edu.must.tos.bean.SurveyUser;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.SurveyQuestDAOImpl;
import edu.must.tos.impl.SurveyRespDAOImpl;
import edu.must.tos.impl.SurveyUserDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.util.CellFormat;

public class TosSurveyServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public TosSurveyServlet() {
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
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
						
			String key = "CURRINTAKE";
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			String curIntake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			SysConfig config = new SysConfig();
			config.setScType("TOSSURVEY");
			config.setScKey(curIntake);
			SysConfig sysConfig = sysConfigDAOImpl.getSysConfig(conn, config);
			int refNo = Integer.parseInt(sysConfig.getScValue1());
			
			SurveyQuestDAOImpl surveyQuestDAOImpl = new SurveyQuestDAOImpl();
			List questList = surveyQuestDAOImpl.getSurveyQuest(conn, refNo);
			SurveyUserDAOImpl surveyUserDAOImpl = new SurveyUserDAOImpl();
			List userList = surveyUserDAOImpl.getSurveyUsers(conn, curIntake);
			SurveyRespDAOImpl surveyRespDAOImpl = new SurveyRespDAOImpl();
			
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition" , "attachment ; filename = Survey Data.xls " );
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			
			WritableSheet worksheet = workbook.createSheet("澳門科技大學圖書出版及供應中心學生服務滿意度調查問卷", 0);
			//表格式樣
			WritableCellFormat cellFormat = CellFormat.getCellFormat();
			
			Label studentNo = new Label(0, 0, "學生編號", cellFormat);
			worksheet.addCell(studentNo);
			worksheet.setColumnView(0, "學生編號".getBytes().length + 2);
			int size = questList.size() + 1;
			for(int i=1; i<size; i++){
				SurveyQuest quest = (SurveyQuest)questList.get(i-1);
				String title = (quest.getSqName() == null? "":quest.getSqName()) + "" + quest.getSqChnTitle();
				Label titleLabel = new Label(i, 0, title, cellFormat);
				worksheet.addCell(titleLabel);
				worksheet.setColumnView(i, title.getBytes().length + 2);
			}
			for(int i=0; i<userList.size(); i++){
				SurveyUser user = (SurveyUser)userList.get(i);
				int srSuRefno = user.getSuRefNo();
				List respList = surveyRespDAOImpl.getSurveyResp(conn, srSuRefno);
				
				worksheet.addCell(new Label(0, i+1, user.getSuUser()));
				for(int j=0; j<respList.size(); j++){
					SurveyResp resp = (SurveyResp)respList.get(j);
					worksheet.addCell(new Label(j+1, i+1, resp.getSrValue()));
				}
			}
			workbook.write();
			workbook.close();
		    
			os.flush();
			os.close();
		
		} catch (Exception e) {
			throw new ServletException (e);
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
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
