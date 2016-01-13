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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.VBookFeeForApp;
import edu.must.tos.bean.VBookFeeForAppBean;
import edu.must.tos.impl.VBookFeeForAppDAOImpl;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class NewStudAccountServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public NewStudAccountServlet() {
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
			
			HttpSession session = request.getSession();
			String intake = (String)session.getAttribute("curIntake");
			//intake = "1009";
			VBookFeeForAppDAOImpl vBookFeeForAppDAOImpl = new VBookFeeForAppDAOImpl();
			
			String type = null;
			if(request.getParameter("type") != null){
				type = request.getParameter("type");
			}
			if(type != null && type.equals("A")){
				response.reset();
				response.setContentType("application/octet-stream");
				response.addHeader("Content-Disposition" , "attachment ; filename = newStudAccount_A.xls " );
				
				OutputStream os = response.getOutputStream();
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				WritableSheet worksheet = workbook.createSheet("result", 0);
				
				String[] titleName = {"APPLICANTNO", "STUDENTNO", "PAIDINTAKE", "PAIDCURRENCY", "PREPAID", "PAIDAMOUNT", "DUEDATE"};
				for(int i=0; i<titleName.length; i++){
					 worksheet.addCell(new Label(i, 0, titleName[i]));
				}
				
				List list = vBookFeeForAppDAOImpl.getMoreRecordList(conn, intake);
				if(list != null && !list.isEmpty()){
					for(int i=0; i<list.size(); i++){
						int j = 0;
						VBookFeeForApp vBookFeeForApp = (VBookFeeForApp)list.get(i);
						worksheet.addCell(new Label(j++, i+1, vBookFeeForApp.getApplicantNo()));
						worksheet.addCell(new Label(j++, i+1, vBookFeeForApp.getStudentNo()));
						worksheet.addCell(new Label(j++, i+1, vBookFeeForApp.getPaidIntake()));
						worksheet.addCell(new Label(j++, i+1, vBookFeeForApp.getPaidCurrency()));
						worksheet.addCell(new jxl.write.Number(j++, i+1, vBookFeeForApp.getPrePaid()));
						worksheet.addCell(new jxl.write.Number(j++, i+1, vBookFeeForApp.getPaidAmount()));
						worksheet.addCell(new Label(j++, i+1, vBookFeeForApp.getDueDate().toString()));
					}
				}
				
				workbook.write();
				workbook.close();
			    
				os.flush();
				os.close();
			}
			else if(type != null && type.equals("B")){
				response.reset();
				response.setContentType("application/octet-stream");
				response.addHeader("Content-Disposition" , "attachment ; filename = newStudAccount_B.xls " );
				
				OutputStream os = response.getOutputStream();
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				WritableSheet worksheet = workbook.createSheet("result", 0);
				
				String[] titleName = {"申請編號", "學生編號", "學期", "訂單序號", "付款幣種", "付款金額", "付款狀態", "圖書ISBN", "訂購冊數", "退書冊數"};
				for(int i=0; i<titleName.length; i++){
					 worksheet.addCell(new Label(i, 0, titleName[i]));
				}
				
				List list = vBookFeeForAppDAOImpl.getUnDifferList(conn, intake);
				int row = 0;
				if(list != null && !list.isEmpty()){
					for(int i=0; i<list.size(); i++){
						int j = 0;
						VBookFeeForAppBean bean = (VBookFeeForAppBean)list.get(i);
						if(bean.getOrderSeqNo() != null){
							if(bean.getPaidCurrency() != null && !"Y".equals(bean.getPaidstatus())){
								row++;
								worksheet.addCell(new Label(j++, row, bean.getApplicantNo()));
								worksheet.addCell(new Label(j++, row, bean.getStudentNo()));
								worksheet.addCell(new Label(j++, row, bean.getPaidIntake()));
								worksheet.addCell(new Label(j++, row, bean.getOrderSeqNo()));
								worksheet.addCell(new Label(j++, row, bean.getPaidCurrency()));
								worksheet.addCell(new jxl.write.Number(j++, row, bean.getPaidAmount()));
								worksheet.addCell(new Label(j++, row, bean.getPaidstatus()));
								worksheet.addCell(new Label(j++, row, bean.getIsbn()));
								worksheet.addCell(new jxl.write.Number(j++, row, bean.getConfirmQty()));
								worksheet.addCell(new jxl.write.Number(j++, row, bean.getWithdrawQty()));
							}
						}else{
							row++;
							worksheet.addCell(new Label(j++, row, bean.getApplicantNo()));
							worksheet.addCell(new Label(j++, row, ""));
							worksheet.addCell(new Label(j++, row, ""));
							worksheet.addCell(new Label(j++, row, ""));
							worksheet.addCell(new Label(j++, row, ""));
							worksheet.addCell(new jxl.write.Number(j++, row, bean.getvPaidAmount()));
							worksheet.addCell(new Label(j++, row, ""));
							worksheet.addCell(new Label(j++, row, ""));
							worksheet.addCell(new Label(j++, row, ""));
							worksheet.addCell(new Label(j++, row, ""));
						}
					}
				}
				
				workbook.write();
				workbook.close();
			    
				os.flush();
				os.close();
			}
			else if (type != null && "C".equals(type)){
				response.reset();
				response.setContentType("application/octet-stream");
				response.addHeader("Content-Disposition" , "attachment ; filename = newGradStudAccount.xls " );
				
				OutputStream os = response.getOutputStream();
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				WritableSheet worksheet = workbook.createSheet("result", 0);
				
				String[] titleName = {"APPLICANTNO", "STUDENTNO", "PAIDINTAKE", "PAIDCURRENCY", "PREPAID", "PAIDAMOUNT", "DUEDATE"};
				for(int i=0; i<titleName.length; i++){
					 worksheet.addCell(new Label(i, 0, titleName[i]));
				}
				
				List list = vBookFeeForAppDAOImpl.getNewGradStudAccount(conn, intake);
				if(list != null && !list.isEmpty()){
					for(int i=0; i<list.size(); i++){
						int j = 0;
						VBookFeeForApp vBookFeeForApp = (VBookFeeForApp)list.get(i);
						worksheet.addCell(new Label(j++, i+1, vBookFeeForApp.getApplicantNo()));
						worksheet.addCell(new Label(j++, i+1, vBookFeeForApp.getStudentNo()));
						worksheet.addCell(new Label(j++, i+1, vBookFeeForApp.getPaidIntake()));
						worksheet.addCell(new Label(j++, i+1, vBookFeeForApp.getPaidCurrency()));
						if(vBookFeeForApp.getPrePaid() != 0)
							worksheet.addCell(new jxl.write.Number(j++, i+1, vBookFeeForApp.getPrePaid()));
						if(vBookFeeForApp.getPaidAmount() != 0)
							worksheet.addCell(new jxl.write.Number(j++, i+1, vBookFeeForApp.getPaidAmount()));
						if(vBookFeeForApp.getDueDate() != null)
							worksheet.addCell(new Label(j++, i+1, vBookFeeForApp.getDueDate().toString()));
					}
				}
				
				workbook.write();
				workbook.close();
			    
				os.flush();
				os.close();
			}
			else{
				request.getRequestDispatcher("newStudAccount.jsp").forward(request, response);
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
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
