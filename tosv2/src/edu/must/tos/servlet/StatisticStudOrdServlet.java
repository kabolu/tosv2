package edu.must.tos.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import edu.must.tos.bean.SysConfig;
import edu.must.tos.bean.ViewStudOrdInfo;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.util.CellFormat;

public class StatisticStudOrdServlet extends HttpServlet {

	public StatisticStudOrdServlet() {
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
						
			String intakeList = "";
			if(request.getParameter("intakeList") != null && !request.getParameter("intakeList").equals("")){
				intakeList = request.getParameter("intakeList");
			}
			String faculty = "";
			if(request.getParameter("faculty") != null && !request.getParameter("faculty").equals("")){
				faculty = request.getParameter("faculty");
			}
			String program = "";
			if(request.getParameter("prog") != null && !request.getParameter("prog").equals("")){
				program = request.getParameter("prog");
			}
			String academicYear = "";
			if(request.getParameter("academicYear") != null && !request.getParameter("academicYear").equals("")){
				academicYear = request.getParameter("academicYear");
			}
			String studType = "";
			if(request.getParameter("StudType") != null && !request.getParameter("StudType").equals("")){
				studType = request.getParameter("StudType");
			}
			String reportType = "";
			if(request.getParameter("reportType") != null && !request.getParameter("reportType").equals("")){
				reportType = request.getParameter("reportType");
			}
			String fromDate = "";
			if(request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")){
				fromDate = request.getParameter("fromDate")+" 00:00:00";
			}
			String toDate = "";
			if(request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")){
				toDate = request.getParameter("toDate")+" 23:59:59";
			}
			String fromReceiveDate = "";
			if(request.getParameter("fromReceiveDate") != null && !request.getParameter("fromReceiveDate").equals("")){
				fromReceiveDate = request.getParameter("fromReceiveDate")+" 00:00:00";
			}
			String toReceiveDate = "";
			if(request.getParameter("toReceiveDate") != null && !request.getParameter("toReceiveDate").equals("")){
				toReceiveDate = request.getParameter("toReceiveDate")+" 23:59:59";
			}
			//Order Date
			String fromOrderDate = "";
			if(request.getParameter("fromOrderDate") != null && !request.getParameter("fromOrderDate").equals("")){
				fromOrderDate = request.getParameter("fromOrderDate")+" 00:00:00";
			}
			String toOrderDate = "";
			if(request.getParameter("toOrderDate") != null && !request.getParameter("toOrderDate").equals("")){
				toOrderDate = request.getParameter("toOrderDate")+" 23:59:59";
			}
			
			String curIntake = (String)request.getSession().getAttribute("curIntake");
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			SysConfig rateConfig = new SysConfig();
			rateConfig.setScType(curIntake);
			rateConfig.setScKey("RMB");
			rateConfig.setActInd("Y");
			SysConfig rmbRateConfig = sysConfigDAOImpl.getSysConfig(conn, rateConfig);
			double rmbRate = Double.parseDouble(rmbRateConfig.getScValue1());
			
			rateConfig.setScKey("HKD");
			rateConfig.setActInd("Y");
			SysConfig hkdRateConfig = sysConfigDAOImpl.getSysConfig(conn, rateConfig);
			double hkdRate = Double.parseDouble(hkdRateConfig.getScValue1());
			
			OrderDAOImpl orderImpl = new OrderDAOImpl();
			List resultList = orderImpl.statisticStudOrdInfo(conn, intakeList, faculty, program, academicYear, studType, reportType, fromDate, toDate, fromReceiveDate, toReceiveDate, fromOrderDate, toOrderDate);
			List recordList = (List)resultList.get(0);
			String sql = (String)resultList.get(1);
			WritableCellFormat cellFormat = CellFormat.getCellFormat();
			if(recordList != null && !recordList.isEmpty()){
				response.reset();
				response.setContentType("application/octet-stream");
				String fileName = "";
				if(reportType != null && reportType.equals("summary")){
					fileName = "StatisticStudOrdSummary";
				}else if(reportType != null && reportType.equals("detail")){
					fileName = "StatisticStudOrdDetail";
				}else if(reportType != null && reportType.equals("summary_only")){
					fileName = "StatisticStudOrdSummaryOnly";
				}
				response.addHeader("Content-Disposition" , "attachment ; filename = "+fileName+".xls " );
			    
			    OutputStream os = response.getOutputStream();
			    WritableWorkbook workbook = Workbook.createWorkbook(os);
				WritableSheet worksheet = workbook.createSheet(fileName, 0);
				
				DateFormat customDateFormat = new DateFormat ("yyyy-MM-dd hh:mm:ss"); 
				WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat);
								
				if(reportType != null && reportType.equals("summary")){
					String[] titleName={"學員姓名","電郵","新電郵","聯繫號碼","新聯繫號碼","學員編號","學員入學申請編號","學院","課程","年級","訂書序號","訂書冊數","預估金額總數(MOP)","預估金額總數(RMB)",
							"預繳金額總數(MOP)","預繳金額總數(RMB)","退書費(MOP)","退書費(RMB)","補訂運費百分比","罰款金額","逾期付款罰款金額","補差價","付款幣種","實繳金額總數","收款日期","收款員","缺書冊數",
							"缺書金額總數(MOP)","缺書金額總數(RMB)"};
				    for(int i=0; i<titleName.length; i++){
						 worksheet.addCell(new Label(i, 0, titleName[i], cellFormat));
						 worksheet.setColumnView(i, titleName[i].getBytes().length+2);
					}
					
				    for(int i=0; i<recordList.size(); i++){
				    	int j = 0;
				    	ViewStudOrdInfo info = new ViewStudOrdInfo();
				    	info = (ViewStudOrdInfo)recordList.get(i);
				    	worksheet.addCell(new Label(j++, i+1, info.getChineseName()));
				    	worksheet.addCell(new Label(j++, i+1, info.getEmail()));
				    	worksheet.addCell(new Label(j++, i+1, info.getNewEmail()));
				    	worksheet.addCell(new Label(j++, i+1, info.getContactNo()));
				    	worksheet.addCell(new Label(j++, i+1, info.getNewContactNo()));
				    	worksheet.addCell(new Label(j++, i+1, info.getStudentNo()));
				    	worksheet.addCell(new Label(j++, i+1, info.getApplicantNo()));
				    	worksheet.addCell(new Label(j++, i+1, info.getFacultyName()));
				    	worksheet.addCell(new Label(j++, i+1, info.getProgram()));
				    	jxl.write.Number year = new jxl.write.Number(j++, i+1, info.getAcademicYear());
				    	worksheet.addCell(year);
				    	worksheet.addCell(new Label(j++, i+1, info.getOrderSeqNo()));
				    	jxl.write.Number totalQty = new jxl.write.Number(j++, i+1, info.getTotalQty());
				    	worksheet.addCell(totalQty);
				    	jxl.write.Number totalFutPriceMOP = new jxl.write.Number(j++, i+1, info.getTotalFutPriceMOP());
				    	worksheet.addCell(totalFutPriceMOP);
				    	
				    	jxl.write.Number totalFutPriceRMB = new jxl.write.Number(j++, i+1, info.getTotalFutPriceRMB());
				    	worksheet.addCell(totalFutPriceRMB);
				    	
				    	jxl.write.Number totalNetPriceMOP = new jxl.write.Number(j++, i+1, info.getTotalNetPriceMOP());
				    	worksheet.addCell(totalNetPriceMOP);
				    	
				    	jxl.write.Number totalNetPriceRMB = new jxl.write.Number(j++, i+1, info.getTotalNetPriceRMB());
				    	worksheet.addCell(totalNetPriceRMB);
				    	
				    	jxl.write.Number totalWithdrawPriceMOP = new jxl.write.Number(j++, i+1, info.getTotalWithdrawPriceMop());
				    	worksheet.addCell(totalWithdrawPriceMOP);
				    	
				    	jxl.write.Number totalWithdrawPriceRMB = new jxl.write.Number(j++, i+1, info.getTotalWithdrawPriceRmb());
				    	worksheet.addCell(totalWithdrawPriceRMB);
				    	
				    	jxl.write.Number fee = new jxl.write.Number(j++, i+1, (info.getShippingFee()*100));
				    	worksheet.addCell(fee);
				    	
				    	jxl.write.Number amercemount = new jxl.write.Number(j++, i+1, info.getAmercemount());
				    	worksheet.addCell(amercemount);
				    	jxl.write.Number fineforlatepay = new jxl.write.Number(j++, i+1, info.getFineforlatepay());
				    	worksheet.addCell(fineforlatepay);
				    	
				    	jxl.write.Number difference = new jxl.write.Number(j++, i+1, info.getDifference());
				    	worksheet.addCell(difference);
				    	
				    	worksheet.addCell(new Label(j++, i+1, info.getPaidCurrency()));
				    	
				    	jxl.write.Number paidAmount = new jxl.write.Number(j++, i+1, info.getPaidAmount());
				    	worksheet.addCell(paidAmount);

				    	if(info.getPaidDate() == null){
				    		worksheet.addCell(new Label(j++, i+1, ""));	//沒有時間記錄
				    	}else{
				    		worksheet.addCell(new DateTime(j++, i+1, (Date)info.getPaidDate(), dateFormat));
				    	}
				    	worksheet.addCell(new Label(j++, i+1, info.getCashier()));
				    	jxl.write.Number totalNotEngQty = new jxl.write.Number(j++, i+1, info.getTotalNotEngQty());
				    	worksheet.addCell(totalNotEngQty);
				    	jxl.write.Number totalNotEngPriceMOP = new jxl.write.Number(j++, i+1, info.getTotalNotEngPriceMOP());
				    	worksheet.addCell(totalNotEngPriceMOP);
				    	jxl.write.Number totalNotEngPriceRMB = new jxl.write.Number(j++, i+1, info.getTotalNotEngPriceRMB());
				    	worksheet.addCell(totalNotEngPriceRMB);
				    	
				    }
				}else if(reportType != null && reportType.equals("summary_only")){
					String[] titleName = {"訂書序號","訂書冊數","預估金額總數(MOP)","預估金額總數(RMB)","預繳金額總數(MOP)","預繳金額總數(RMB)",
							"退書費(MOP)","退書費(RMB)","補訂運費百分比","罰款金額","逾期付款罰款金額","補差價","付款幣種","實繳金額總數",
							"收款日期","收款員","缺書冊數","缺書金額總數(MOP)","缺書金額總數(RMB)"};
				    for(int i=0; i<titleName.length; i++){
						 worksheet.addCell(new Label(i, 0, titleName[i], cellFormat));
						 worksheet.setColumnView(i, titleName[i].getBytes().length+2);
					}
				     
				    for(int i=0; i<recordList.size(); i++){
				    	int j = 0;
				    	ViewStudOrdInfo info = new ViewStudOrdInfo();
				    	info = (ViewStudOrdInfo)recordList.get(i);
				    	worksheet.addCell(new Label(j++, i+1, info.getOrderSeqNo()));
				    	jxl.write.Number totalQty = new jxl.write.Number(j++, i+1, info.getTotalQty());
				    	worksheet.addCell(totalQty);
				    	jxl.write.Number totalFutPriceMOP = new jxl.write.Number(j++, i+1, info.getTotalFutPriceMOP());
				    	worksheet.addCell(totalFutPriceMOP);
				    	
				    	jxl.write.Number totalFutPriceRMB = new jxl.write.Number(j++, i+1, info.getTotalFutPriceRMB());
				    	worksheet.addCell(totalFutPriceRMB);
				    	
				    	jxl.write.Number totalNetPriceMOP = new jxl.write.Number(j++, i+1, info.getTotalNetPriceMOP());
				    	worksheet.addCell(totalNetPriceMOP);
				    	
				    	jxl.write.Number totalNetPriceRMB = new jxl.write.Number(j++, i+1, info.getTotalNetPriceRMB());
				    	worksheet.addCell(totalNetPriceRMB);
				    	
				    	jxl.write.Number totalWithdrawPriceMOP = new jxl.write.Number(j++, i+1, info.getTotalWithdrawPriceMop());
				    	worksheet.addCell(totalWithdrawPriceMOP);
				    	
				    	jxl.write.Number totalWithdrawPriceRMB = new jxl.write.Number(j++, i+1, info.getTotalWithdrawPriceRmb());
				    	worksheet.addCell(totalWithdrawPriceRMB);
				    	
				    	jxl.write.Number fee = new jxl.write.Number(j++, i+1, (info.getShippingFee()*100));
				    	worksheet.addCell(fee);
				    	
				    	jxl.write.Number amercemount = new jxl.write.Number(j++, i+1, info.getAmercemount());
				    	worksheet.addCell(amercemount);
				    	jxl.write.Number fineforlatepay = new jxl.write.Number(j++, i+1, info.getFineforlatepay());
				    	worksheet.addCell(fineforlatepay);
				    	
				    	jxl.write.Number difference = new jxl.write.Number(j++, i+1, info.getDifference());
				    	worksheet.addCell(difference);
				    	
				    	worksheet.addCell(new Label(j++, i+1, info.getPaidCurrency()));
				    	
				    	jxl.write.Number paidAmount = new jxl.write.Number(j++, i+1, info.getPaidAmount());
				    	worksheet.addCell(paidAmount);

				    	if(info.getPaidDate() == null){
				    		worksheet.addCell(new Label(j++, i+1, ""));	//沒有時間記錄
				    	}else{
				    		worksheet.addCell(new DateTime(j++, i+1, (Date)info.getPaidDate(), dateFormat));
				    	}
				    	worksheet.addCell(new Label(j++, i+1, info.getCashier()));
				    	jxl.write.Number totalNotEngQty = new jxl.write.Number(j++, i+1, info.getTotalNotEngQty());
				    	worksheet.addCell(totalNotEngQty);
				    	jxl.write.Number totalNotEngPriceMOP = new jxl.write.Number(j++, i+1, info.getTotalNotEngPriceMOP());
				    	worksheet.addCell(totalNotEngPriceMOP);
				    	jxl.write.Number totalNotEngPriceRMB = new jxl.write.Number(j++, i+1, info.getTotalNotEngPriceRMB());
				    	worksheet.addCell(totalNotEngPriceRMB);
				    	
				    }
				}else if(reportType != null && reportType.equals("detail")){
					String[] titleName = {"學員姓名","電郵","新電郵","聯繫號碼","新聯繫號碼","學員編號","學員入學申請編號","學院","年級","課程","訂書序號","書本名稱",
							"作者姓名","出版社","版次","出版年","ISBN","訂購冊數","缺書冊數","預估金額(MOP)","預估金額(RMB)",
							"退書費(MOP)","退書費(RMB)","付款幣種","預繳金額","收款日期","收款員","最後更新日期","最後操作員"};
				    for(int i=0; i<titleName.length; i++){
						 worksheet.addCell(new Label(i, 0, titleName[i], cellFormat));
						 worksheet.setColumnView(i, titleName[i].getBytes().length);
					}
					
					SysConfigDAOImpl sysconfigImpl = new SysConfigDAOImpl();
					SysConfig config = new SysConfig();
					config.setScType("FINERATE");
					config.setScKey("SHIPPING");
					config.setActInd("Y");
					SysConfig withdrawForCarry = sysconfigImpl.getSysConfig(conn, config);
					
				    for(int i=0; i<recordList.size(); i++){
				    	int j=0;
				    	ViewStudOrdInfo info = new ViewStudOrdInfo();
				    	info = (ViewStudOrdInfo)recordList.get(i);
				    	worksheet.addCell(new Label(j++, i+1, info.getChineseName()));
				    	worksheet.addCell(new Label(j++, i+1, info.getEmail()));
				    	worksheet.addCell(new Label(j++, i+1, info.getNewEmail()));
				    	worksheet.addCell(new Label(j++, i+1, info.getContactNo()));
				    	worksheet.addCell(new Label(j++, i+1, info.getNewContactNo()));
				    	worksheet.addCell(new Label(j++, i+1, info.getStudentNo()));
				    	worksheet.addCell(new Label(j++, i+1, info.getApplicantNo()));
				    	worksheet.addCell(new Label(j++, i+1, info.getFacultyName()));
				    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getAcademicYear()));
				    	worksheet.addCell(new Label(j++, i+1, info.getProgram()));
				    	worksheet.addCell(new Label(j++, i+1, info.getOrderSeqNo()));
				    	worksheet.addCell(new Label(j++, i+1, info.getTitle()));
				    	worksheet.addCell(new Label(j++, i+1, info.getAuthor()));
				    	worksheet.addCell(new Label(j++, i+1, info.getPublisher()));
				    	worksheet.addCell(new Label(j++, i+1, info.getEdition()));
				    	worksheet.addCell(new Label(j++, i+1, info.getPublishYear()));
				    	worksheet.addCell(new Label(j++, i+1, info.getIsbn()));
				    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getOrderEdQty()));
				    	if(info.getNotEnoughQty()<0){
				    		worksheet.addCell(new Label(j++, i+1, ""));
				    	}else{
				    		worksheet.addCell(new jxl.write.Number(j++, i+1, info.getNotEnoughQty()));
				    	}
				    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getFuturePriceMOP()));
				    	worksheet.addCell(new jxl.write.Number(j++, i+1, info.getFuturePriceRMB()));
				    	
				    	//退運費計算
				    	double mopPrice = 0;
				    	if(info.getNetPriceMOP() == 0){
				    		mopPrice = info.getFuturePriceMOP();
				    	}else{
				    		mopPrice = info.getNetPriceMOP();
				    	}
				    	
				    	double rmbPrice = 0;
				    	if(info.getNetPriceRMB() == 0){
				    		rmbPrice = info.getFuturePriceRMB();
				    	}else{
				    		rmbPrice = info.getNetPriceRMB();
				    	}
				    	double withdrawCarryForFeeMop = new BigDecimal(Double.toString(mopPrice)).multiply(new BigDecimal(Integer.toString(info.getWithdrawqty2()))).multiply(new BigDecimal(withdrawForCarry.getScValue1())).multiply(new BigDecimal(0.01)).doubleValue();
				    	if(0<withdrawCarryForFeeMop && withdrawCarryForFeeMop<1){
				    		withdrawCarryForFeeMop = 1;
				        }else{
				        	withdrawCarryForFeeMop = new BigDecimal(Double.toString(withdrawCarryForFeeMop)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				        }
				    	double withdrawCarryForFeeRmb = new BigDecimal(Double.toString(rmbPrice)).multiply(new BigDecimal(Integer.toString(info.getWithdrawqty2()))).multiply(new BigDecimal(withdrawForCarry.getScValue1())).multiply(new BigDecimal(0.01)).doubleValue();
				    	if(0<withdrawCarryForFeeRmb && withdrawCarryForFeeRmb<1){
				    		withdrawCarryForFeeRmb = 1;
				        }else{
				        	withdrawCarryForFeeRmb = new BigDecimal(Double.toString(withdrawCarryForFeeRmb)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				        }
				    	
				    	worksheet.addCell(new jxl.write.Number(j++, i+1, (info.getWithdrawpriceMOP()+withdrawCarryForFeeMop)));
				    	worksheet.addCell(new jxl.write.Number(j++, i+1, (info.getWithdrawpriceRMB()+withdrawCarryForFeeRmb)));
				    	
				    	if(info.getPaidCurrency() != null && info.getPaidAmount() != null){
				    		worksheet.addCell(new Label(j++, i+1, info.getPaidCurrency()));
				    		if(info.getPaidCurrency().equals("MOP")){
				    			if(info.getNetPriceMOP() == 0){
				    				worksheet.addCell(new jxl.write.Number(j++, i+1, (info.getPayfuturepricemop()+withdrawCarryForFeeMop)));
				    			}else{
				    				worksheet.addCell(new jxl.write.Number(j++, i+1, (info.getPaynetpricemop()+withdrawCarryForFeeMop)));
				    			}
				    		}else if(info.getPaidCurrency().equals("RMB")){
				    			double mopFuturePrice = info.getPayfuturepricemop() + withdrawCarryForFeeMop;
			    				double mopNetPrice = info.getPaynetpricemop() + withdrawCarryForFeeMop;
				    			if(info.getNetPriceMOP() == 0){
				    				double rmb = new BigDecimal(mopFuturePrice).multiply(new BigDecimal(rmbRate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				    				worksheet.addCell(new jxl.write.Number(j++, i+1, (rmb)));
				    			}else{
				    				double rmb = new BigDecimal(mopNetPrice).multiply(new BigDecimal(rmbRate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				    				worksheet.addCell(new jxl.write.Number(j++, i+1, (rmb)));
				    			}
				    		}else if(info.getPaidCurrency().equals("HKD")){
				    			double hkdFuturePrice = info.getPayfuturepricemop() + withdrawCarryForFeeMop;
			    				double hkdNetPrice = info.getPaynetpricemop() + withdrawCarryForFeeMop;
				    			if(info.getNetPriceMOP() == 0){
				    				double hkd = new BigDecimal(hkdFuturePrice).multiply(new BigDecimal(hkdRate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				    				worksheet.addCell(new jxl.write.Number(j++, i+1, (hkd)));
				    			}else{
				    				double hkd = new BigDecimal(hkdNetPrice).multiply(new BigDecimal(hkdRate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				    				worksheet.addCell(new jxl.write.Number(j++, i+1, (hkd)));
				    			}
				    		}
				    	}else{
				    		worksheet.addCell(new Label(j++, i+1, info.getPaidCurrency()));
				    		if(info.getPaidAmount() == 0){
				    			worksheet.addCell(new Label(j++, i+1, ""));
				    		}
				    		//worksheet.addCell(new jxl.write.Number(j++, i+1, info.getPaidAmount()));
				    	}
				    	
				    	if(info.getPaidDate() == null){
				    		worksheet.addCell(new Label(j++, i+1, ""));	//沒有時間記錄
				    	}else{
				    		worksheet.addCell(new DateTime(j++, i+1, (Date)info.getPaidDate(), dateFormat));
				    	}
				    	worksheet.addCell(new Label(j++, i+1, info.getCashier()));
				    	
				    	if(info.getUpddate() == null){
				    		worksheet.addCell(new Label(j++, i+1, ""));	//沒有更新時間記錄
				    	}else{
				    		worksheet.addCell(new DateTime(j++, i+1, (Date)info.getUpddate(), dateFormat));
				    	}
				    	worksheet.addCell(new Label(j++, i+1, info.getUpduid()));
				    }
				}
			    workbook.write();
				workbook.close();
			    
				os.flush();
				os.close();
			}else{
				//no record				
				response.sendRedirect("FacultyListServlet?type=studOrdBookSummary&error=error");
				//request.getRequestDispatcher("FacultyListServlet?type=studOrdBookSummary").forward(request, response);
				//request.getRequestDispatcher("statisticStudOrdSumm.jsp").forward(request, response);
			}
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
