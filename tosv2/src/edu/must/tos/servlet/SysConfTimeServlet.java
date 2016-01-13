package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.PriceDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;

public class SysConfTimeServlet extends HttpServlet {

	public SysConfTimeServlet() {
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
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			HttpSession session = request.getSession();
			String curIntake = (String)session.getAttribute("curIntake");
			String userId = (String)session.getAttribute("userId");
			
			String type = request.getParameter("type");
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			if(type != null && type.equals("view")){
				List scType = sysConfigDAOImpl.getSysScType(conn);
				List timeList = sysConfigDAOImpl.getSysTime(conn, curIntake);
				if(timeList != null && !timeList.isEmpty()){
					request.setAttribute("timeList", timeList);
					request.setAttribute("scType", scType);
					request.getRequestDispatcher("systemConfigure.jsp").forward(request, response);
				}else{
					request.setAttribute("timeList", timeList);
					request.getRequestDispatcher("systemConfigure.jsp").forward(request, response);
				}
			}
			else if(type != null && type.equals("add")){
				List scType = sysConfigDAOImpl.getSysScType(conn);
				List timeList = sysConfigDAOImpl.getSysTime(conn, curIntake);
				if(timeList != null && !timeList.isEmpty()){
					request.setAttribute("timeList", timeList);
					request.setAttribute("scType", scType);
					request.getRequestDispatcher("sysTimeAdd.jsp").forward(request, response);
				}else{
					request.setAttribute("scType", scType);
					request.setAttribute("timeList", timeList);
					request.getRequestDispatcher("sysTime.Addjsp").forward(request, response);
				}
			}
			else if(type != null && type.equals("edit")){
				int id = Integer.parseInt(request.getParameter("id"));
				List timeList = sysConfigDAOImpl.getSysTime(conn, curIntake);
				if(timeList != null && !timeList.isEmpty()){
					request.setAttribute("timeList", timeList);
					request.setAttribute("id", id);
					request.getRequestDispatcher("sysTimeEdit.jsp").forward(request, response);
				}
			}
			else if(type != null && type.equals("search")){
				String searchIntake = null;
				if(request.getParameter("searchIntake") != null && !request.getParameter("searchIntake").equals("")){
					searchIntake = request.getParameter("searchIntake");
				}else {
					searchIntake = curIntake;
				}
				List timeList = sysConfigDAOImpl.getSysTime(conn, searchIntake);
				if(timeList != null && !timeList.isEmpty()){
					request.setAttribute("timeList", timeList);
					request.setAttribute("searchIntake", searchIntake);
					request.setAttribute("type", "searchResult");
					request.getRequestDispatcher("systemConfigure.jsp").forward(request, response);
				}else{
        			request.setAttribute("timeList", timeList);
					request.setAttribute("searchIntake", searchIntake);
					request.setAttribute("type", "searchResult");
					request.getRequestDispatcher("systemConfigure.jsp").forward(request, response);
				}
			}
			else if(type != null && type.equals("editSearch")){
				int id = Integer.parseInt(request.getParameter("id"));
				String searchIntake = request.getParameter("intake");
				List timeList = sysConfigDAOImpl.getSysTime(conn, searchIntake);
				if(timeList != null && !timeList.isEmpty()){
					request.setAttribute("timeList", timeList);
					request.setAttribute("id", id);
					request.getRequestDispatcher("sysTimeEdit.jsp").forward(request, response);
				}
			}
			else if(type != null && type.equals("param")){	//獲取補差價參數				
				getParam(conn, curIntake, userId, request, response);
				
			}
			else if(type != null && type.equals("delAdjustItem")){
				delAdjustItem(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("adjustItemParam")){
				updateAdjustItemParam(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("intakeParam")){
				getIntakeParam(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("intakeModify")){
				doIntakeModify(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("periodParams")){
				savePeriodParams(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("withdrawFee")){
				saveWithdrawFee(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("saveAmerce")){
				saveAmerce(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("rePrintFee")){
				saveRePrintFee(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("shippingFee")){				
				saveShippingFee(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("withdrawForCarry")){	//退運費百分比數據更新
				updateWithdrawForCarry(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("keepingFeeParam")){
				saveKeepingFeeParam(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("saveDifferParam")){
				saveDifferParam(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("addSysTime")){
				addSysTime(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("editSysTime")){
				editSysTime(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("savePrepaid")){
				savePrePaid(conn, curIntake, userId, request, response);
			}
			else if(type != null && type.equals("saveRate")){
				saveRate(conn, curIntake, userId, request, response);
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

	private void saveRate(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String rate = request.getParameter("rate");
		String currencyRate = request.getParameter("currencyRate");
		String currencyType = request.getParameter("currencyType");
		try{
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			SysConfig rateConfig = new SysConfig();
			if(currencyType!=null){
				rateConfig.setScType(currencyType);				
			}else{
				rateConfig.setScType("EXCHANGERATE");
			}
			rateConfig.setScKey(currencyRate);
			SysConfig config = sysConfigDAOImpl.getSysConfig(conn, rateConfig);
			if(config != null){
				config.setScValue1(rate);
				config.setActInd("Y");
				config.setUpdDate(new Date());
				config.setUpdUid(userId);
			}
			boolean flag = sysConfigDAOImpl.updateSysConfig(conn, config);
			if(flag){
				conn.commit();
				out.print(1);
			} else {
				conn.rollback();
				out.print(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			out.print(0);
		}
	}

	private void editSysTime(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String scType = request.getParameter("scType");
		String chnDesc = request.getParameter("chnDesc");
		String engDesc = request.getParameter("engDesc");
		String intake = request.getParameter("intake");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String fromTime = request.getParameter("fromTime");
		String toTime = request.getParameter("toTime");
		String scValue3 = request.getParameter("scValue3");

		
		SysConfig sc = new SysConfig();
		sc.setScType(scType);
		sc.setScKey(intake);
		sc.setScValue1(fromDate+" "+fromTime);
		sc.setScValue2(toDate+" "+toTime);
		if(scValue3 != null && !"".equals(scValue3))
			sc.setScValue3(scValue3);
		else 
			sc.setScValue3("");
		sc.setScChnDesc(chnDesc);
		sc.setScEngDesc(engDesc);
		sc.setActInd("Y");
		sc.setUpdUid(userId);
		sc.setUpdDate(new Date());
		
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		String opType = "edit";
		boolean flag = sysConfigDAOImpl.getCheckSysTime(conn, sc, opType);
		if(flag){
			boolean temp = sysConfigDAOImpl.updateSysConfig(conn, sc);
			if(temp){
				conn.commit();
				
				String withdraw = (String)request.getSession().getAttribute("WITHDRAW");
				if(withdraw != null && "Y".equals(withdraw)){
					withdraw = "Y";
				}else{
					withdraw = "N";
				}
				request.getSession().setAttribute("withdraw", withdraw);
				
				request.setAttribute("msg", "修改成功！");
				request.setAttribute("type", "editSysTime");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			}else{
				conn.rollback();
				request.setAttribute("msg", "修改失敗！");
				request.setAttribute("type", "editSysTime");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			}
		}else{
			request.setAttribute("msg", "輸入的時間與其他時段時間衝突，請檢查！");
			request.setAttribute("type", "sysTime");
			request.getRequestDispatcher("msg.jsp").forward(request, response);
		}
	}

	private void getParam(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String currType = "CURRINTAKE";
		
		SysConfig c = new SysConfig();
		c.setScType("INTAKE");
		c.setScKey(currType);
		SysConfig intakeConfig = sysConfigDAOImpl.getSysConfig(conn, c);
		
		SysConfig withdraw = new SysConfig();
		withdraw.setScType("WITHDRAW");
		withdraw.setScKey(curIntake);
		withdraw.setActInd("Y");
		SysConfig withdrawPeriod = sysConfigDAOImpl.getSysConfig(conn, withdraw);
		request.setAttribute("withdrawPeriod", withdrawPeriod);
		
		SysConfig conf = new SysConfig();
		conf.setScType("WITHDRAWFEE");
		conf.setActInd("Y");
		List withdrawFeeList = sysConfigDAOImpl.getSysConfigList(conn, conf);
		
		SysConfig paid2 = new SysConfig();
		paid2.setScKey(curIntake);
		paid2.setScType("PAID2");
		paid2.setActInd("Y");
		SysConfig paid2Period = sysConfigDAOImpl.getSysConfig(conn, paid2);
		
		SysConfig sc = new SysConfig();
		sc.setScType("FINERATE");
		sc.setScKey("LATEPAY");
		sc.setActInd("Y");
		SysConfig amerceInfo = sysConfigDAOImpl.getSysConfig(conn, sc);
		
		//補差價參數
		String scType1 = "DIFFVALUE1";
		String scType2 = "DIFFVALUE2";
		String scKey = "DIFFERENCE";
		SysConfig differConfig1 = new SysConfig();
		differConfig1.setScType(scType1);
		differConfig1.setScKey(scKey);
		differConfig1.setActInd("Y");
		differConfig1 = sysConfigDAOImpl.getSysConfig(conn, differConfig1);	//追討參數
		double differ1 = 0;
		if(differConfig1.getScValue1() != null){
			differ1 = Double.parseDouble(differConfig1.getScValue1());
		}
		
		SysConfig differConfig2 = new SysConfig();
		differConfig2.setScType(scType2);
		differConfig2.setScKey(scKey);
		differConfig2.setActInd("Y");
		differConfig2 = sysConfigDAOImpl.getSysConfig(conn, differConfig2);	//退款參數
		double differ2 = 0;
		if(differConfig2.getScValue1() != null){
			differ2 = Double.parseDouble(differConfig2.getScValue1());
		}
		
		SysConfig prepaidConfig = new SysConfig();
		prepaidConfig.setScType("PREPAID");
		prepaidConfig.setActInd("Y");
		List prepaidList = sysConfigDAOImpl.getSysConfigList(conn, prepaidConfig);
		
		//退運費
		SysConfig config = new SysConfig();
		config.setScType("FINERATE");
		config.setScKey("SHIPPING");
		config.setActInd("Y");
		SysConfig withdrawForCarry = sysConfigDAOImpl.getSysConfig(conn, config);
		
		//保管費
		SysConfig lateReceiveConfig = new SysConfig();
		lateReceiveConfig.setScType("FINERATE");
		lateReceiveConfig.setScKey("LATERECEIVE");
		lateReceiveConfig.setActInd("Y");
		SysConfig lateReceive = sysConfigDAOImpl.getSysConfig(conn, lateReceiveConfig);
		
		SysConfig rePrint = new SysConfig();
		rePrint.setScType("REPRINTFEE");
		rePrint.setScKey("FEE");
		rePrint.setActInd("Y");
		SysConfig rePrintFee = sysConfigDAOImpl.getSysConfig(conn, rePrint);
		
		//调整选项
		SysConfig adjust = new SysConfig();
		adjust.setScType("ADJUSTITEM");
		adjust.setScKey("ITEM");
		adjust.setActInd("Y");
		SysConfig adjustConfig = sysConfigDAOImpl.getSysConfig(conn, adjust);
		
		//補訂運費
		SysConfig shippingFee = new SysConfig();
		shippingFee.setScType("SHIPPINGFEE");
		shippingFee.setScKey("FEE");
		shippingFee.setActInd("Y");
		SysConfig shippingFeeConfig = sysConfigDAOImpl.getSysConfig(conn, shippingFee);
		
		//當前學期MOP兌HKD RMB匯率
		
		SysConfig curRate = new SysConfig();
		curRate.setScType(curIntake);
		curRate.setActInd("Y");
		List curInRateList = sysConfigDAOImpl.getSysConfigList(conn, curRate);
		
		
		
		//RMB HKD USD兌MOP的匯率
		SysConfig rate = new SysConfig();
		rate.setScType("EXCHANGERATE");
		rate.setActInd("Y");
		List rateList = sysConfigDAOImpl.getSysConfigList(conn, rate);
		
		request.setAttribute("shippingFeeConfig", shippingFeeConfig);
		request.setAttribute("adjustConfig", adjustConfig);
		request.setAttribute("rePrintFee", rePrintFee);
		request.setAttribute("lateReceive", lateReceive);
		request.setAttribute("withdrawForCarry", withdrawForCarry);
		request.setAttribute("differ1", differ1);
		request.setAttribute("differ2", differ2);
		request.setAttribute("amerceInfo", amerceInfo);
		request.setAttribute("paid2Period", paid2Period);
		request.setAttribute("withdrawFeeList", withdrawFeeList);
		request.setAttribute("intakeConfig", intakeConfig);
		request.setAttribute("prepaidList", prepaidList);
		request.setAttribute("curInRateList", curInRateList);
		request.setAttribute("rateList", rateList);
		request.getRequestDispatcher("paramConfig.jsp").forward(request, response);
	}

	private void delAdjustItem(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String itemValue = null;
		if(request.getParameter("itemValue") != null){
			itemValue = request.getParameter("itemValue");
		}
		SysConfig adjust = new SysConfig();
		adjust.setScType("ADJUSTITEM");
		adjust.setScKey("ITEM");
		adjust.setActInd("Y");
		SysConfig adjustConfig = sysConfigDAOImpl.getSysConfig(conn, adjust);
		String adjustStr = "";
		if(itemValue!=null && adjustConfig != null && adjustConfig.getScValue1()!=null){
			String item[] = itemValue.split(",");
			String adjustItem[] = adjustConfig.getScValue1().split("/");
			List list = new ArrayList();
			for(int i=0; i<adjustItem.length; i++){
				list.add(adjustItem[i]);
			}
			for(int j=item.length-1; j>=0; j--){
				list.remove(Integer.parseInt(item[j]));
			}
			for(int i=0;i<list.size();i++){
				adjustStr += (String)list.get(i)+"/";
			}
		}
		adjustConfig.setScValue1(adjustStr);
		adjustConfig.setUpdDate(new Date());
		adjustConfig.setUpdUid(userId);
		boolean updateFlag = sysConfigDAOImpl.updateSysConfig(conn, adjustConfig);
		if(updateFlag){
			conn.commit();
		}else{
			conn.rollback();
		}
		out.print(Boolean.toString(updateFlag));
	}

	private void updateAdjustItemParam(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String adjustItem = null;
		if(request.getParameter("adjustItem") != null){
			adjustItem = request.getParameter("adjustItem").trim();
		}
		SysConfig adjust = new SysConfig();
		adjust.setScType("ADJUSTITEM");
		adjust.setScKey("ITEM");
		adjust.setActInd("Y");
		SysConfig adjustConfig = sysConfigDAOImpl.getSysConfig(conn, adjust);
		String adjustStr = "";
		if(adjustItem!=null){
			if(adjustConfig.getScValue1() == null){
				adjustStr = adjustItem+"/";
			}else{
				adjustStr = adjustConfig.getScValue1()+adjustItem+"/";
			}
		}
		adjustConfig.setScValue1(adjustStr);
		adjustConfig.setUpdDate(new Date());
		adjustConfig.setUpdUid(userId);
		boolean updateFlag = sysConfigDAOImpl.updateSysConfig(conn, adjustConfig);
		if(updateFlag){
			conn.commit();
		}else{
			conn.rollback();
		}
		out.print(Boolean.toString(updateFlag));
	}

	private void getIntakeParam(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("ContentType","text/xml");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String currIntake = request.getParameter("currIntake");
		List scIntakeList = sysConfigDAOImpl.getSysIntake(conn);
		
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<results>");
		
		for(int i=0;i<scIntakeList.size();i++){
			SysConfig sc = (SysConfig)scIntakeList.get(i);
			xml.append("<result>");
			xml.append("<scType>"+sc.getScType()+"</scType>");
			xml.append("<scValue1>"+sc.getScValue1()+"</scValue1>");
			xml.append("</result>");
		}
		xml.append("</results>");
		out.print(xml.toString());
	}

	private void doIntakeModify(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String oldIntake = request.getParameter("oldIntake");
		String editIntake = request.getParameter("editIntake");
		boolean flag = sysConfigDAOImpl.updateCurrIntake(conn, editIntake, oldIntake, userId);
		if(flag){
			conn.commit();
			request.getSession().setAttribute("curIntake", editIntake);
			out.print(1);
		}else{
			conn.rollback();
			out.print(0);
		}
	}

	private void savePeriodParams(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String scType = request.getParameter("scType");
		String scKey = request.getSession().getAttribute("curIntake").toString();
		String scValue = request.getParameter("scValue");
		if(scValue != null && !"".equals(scValue)){
			SysConfig sc = new SysConfig();
			if("WITHDRAW".equals(scType)){
				sc.setScType(scType);
				sc.setScKey(scKey);
				sc.setScValue1(scValue);
				sc.setScChnDesc("退書期");
				sc.setScEngDesc("Order Withdrawn Period");
			}else if("PAID2".equals(scType)){
				sc.setScType(scType);
				sc.setScKey(scKey);
				sc.setScValue1(scValue);
				sc.setScChnDesc("逾期付款罰款期");
				sc.setScEngDesc("Over Paid Period");
			}
			sc.setActInd("Y");
			sc.setCreDate(new Date());
			sc.setCreUid(userId);
			sc.setUpdDate(new Date());
			sc.setUpdUid(userId);
			SysConfig sysConfig = sysConfigDAOImpl.getSysConfig(conn, sc);
			boolean flag = false;
			if(sysConfig != null){
				flag = sysConfigDAOImpl.updateSysConfig(conn, sc);
			}else{
				flag = sysConfigDAOImpl.addSysCnnfig(conn, sc);
			}
			if(flag){
				conn.commit();
				out.print(1);
			}else{
				conn.rollback();
				out.print(0);
			}
		}
	}

	private void saveWithdrawFee(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String scKey = request.getParameter("sckey");
		String scValue1 = request.getParameter("scvalue1");
		
		SysConfig sc = new SysConfig();
		sc.setScType("WITHDRAWFEE");
		sc.setScKey(scKey);
		sc.setActInd("Y");
		sc.setScValue1(scValue1);
		sc.setUpdDate(new Date());
		sc.setUpdUid(userId);
		boolean flag = sysConfigDAOImpl.updateSysConfig(conn, sc);
		
		PriceDAOImpl priceImpl = new PriceDAOImpl();
		boolean flag1 = priceImpl.updateWithdrawPrice(conn, scKey, scValue1, curIntake);
		if(flag && flag1){
			conn.commit();
			out.print(1);
		}else{
			conn.rollback();
			out.print(0);
		}
	}

	private void saveAmerce(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("ContentType","text/xml");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String amerce = request.getParameter("amerce");
		SysConfig sc = new SysConfig();
		sc.setScType("FINERATE");
		sc.setScKey("LATEPAY");
		sc.setActInd("Y");
		sc.setScValue1(amerce);
		sc.setCreDate(new Date());
		sc.setCreUid(userId);
		sc.setUpdDate(new Date());
		sc.setUpdUid(userId);
		SysConfig sysConfig = sysConfigDAOImpl.getSysConfig(conn, sc);
		
		boolean flag = false;
		if(sysConfig != null){
			flag = sysConfigDAOImpl.updateSysConfig(conn, sc);
		}else{
			flag = sysConfigDAOImpl.addSysCnnfig(conn, sc);
		}
		if(flag){
			conn.commit();
			
			SysConfig conf = new SysConfig();
			conf.setScType("FINERATE");
			conf.setScKey("LATEPAY");
			conf.setActInd("Y");
			SysConfig amerceInfo = sysConfigDAOImpl.getSysConfig(conn, conf);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String amerceUpdDate = "";
			String amerceUpdUid = "";
			if(amerceInfo != null){
				amerceUpdUid = amerceInfo.getUpdUid();
				amerceUpdDate = dateFormat.format(amerceInfo.getUpdDate());
			}
			StringBuilder xml = new StringBuilder();
			xml.append("<?xml version='1.0' encoding='UTF-8'?>");
			xml.append("<results>");
			xml.append("<result>");
			xml.append("<commit>"+1+"</commit>");
			xml.append("<upduid>"+amerceUpdUid+"</upduid>");
			xml.append("<upddate>"+amerceUpdDate+"</upddate>");
			xml.append("</result>");
			xml.append("</results>");
			out.print(xml.toString());
			
		}else{
			conn.rollback();
			out.print(0);
		}
	}

	private void saveRePrintFee(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("ContentType","text/xml");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String rePrintFee = request.getParameter("rePrintFee");
		String rePrintAmerceCurrency = request.getParameter("rePrintAmerceCurrency");
		
		SysConfig fee = new SysConfig();
		fee.setScType("REPRINTFEE");
		fee.setScKey("FEE");
		fee.setScValue1(rePrintFee);
		fee.setScValue2(rePrintAmerceCurrency);
		fee.setActInd("Y");
		fee.setUpdUid(userId);
		fee.setUpdDate(new Date());
		boolean flag = sysConfigDAOImpl.updateSysConfig(conn, fee);
		if(flag){
			conn.commit();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuilder xml = new StringBuilder();
			xml.append("<?xml version='1.0' encoding='UTF-8'?>");
			xml.append("<results>");
			xml.append("<result>");
			xml.append("<commit>"+1+"</commit>");
			xml.append("<upduid>"+fee.getUpdUid()+"</upduid>");
			xml.append("<upddate>"+dateFormat.format(fee.getUpdDate())+"</upddate>");
			xml.append("</result>");
			xml.append("</results>");
			out.print(xml.toString());
		}else{
			conn.rollback();
			out.print(0);
		}
	}

	private void saveShippingFee(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String scValue1 = request.getParameter("shippingFee");
		SysConfig config = new SysConfig();
		config.setScType("SHIPPINGFEE");
		config.setScKey("FEE");
		config.setScValue1(scValue1);
		config.setScChnDesc("補訂運費");
		config.setScEngDesc("SHIPPING FEE");
		config.setActInd("Y");
		config.setUpdUid(userId);
		config.setUpdDate(new Date());
		boolean flag = sysConfigDAOImpl.updateSysConfig(conn, config);
		if(flag){
			out.print("true");
			conn.commit();
		}else{
			out.print("false");
			conn.rollback();
		}
	}

	private void updateWithdrawForCarry(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("ContentType","text/xml");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String scValue1 = request.getParameter("fee");
		String scValue2 = request.getParameter("beginDate");
		
		SysConfig config = new SysConfig();
		config.setScType("FINERATE");
		config.setScKey("SHIPPING");
		config.setScValue1(scValue1);
		config.setScValue2(scValue2);
		config.setActInd("Y");
		config.setUpdUid(userId);
		config.setUpdDate(new Date());
		
		boolean flag = sysConfigDAOImpl.updateSysConfig(conn, config);
		
		if(flag){
			conn.commit();
			SysConfig withdrawForCarryParam = sysConfigDAOImpl.getSysConfig(conn, config);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String updDate = "";
			String updUid = "";
			if(withdrawForCarryParam != null){
				updUid = withdrawForCarryParam.getUpdUid();
				updDate = dateFormat.format(withdrawForCarryParam.getUpdDate());
			}
			StringBuilder xml = new StringBuilder();
			xml.append("<?xml version='1.0' encoding='UTF-8'?>");
			xml.append("<results>");
			xml.append("<result>");
			xml.append("<commit>"+1+"</commit>");
			xml.append("<upduid>"+updUid+"</upduid>");
			xml.append("<upddate>"+updDate+"</upddate>");
			xml.append("</result>");
			xml.append("</results>");
			out.print(xml.toString());
		}else{
			conn.rollback();
			out.print(0);
		}
	}

	private void saveKeepingFeeParam(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("ContentType","text/xml");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		SysConfig paramConfig = new SysConfig();
		if(request.getParameter("keepingFee") != null && !request.getParameter("keepingFee").equals("")){
			paramConfig.setScValue1(request.getParameter("keepingFee"));
		}
		if(request.getParameter("keepingDay") != null && !request.getParameter("keepingDay").equals("")){
			paramConfig.setScValue2(request.getParameter("keepingDay"));
		}
		if(request.getParameter("keepingPeriod") != null && !request.getParameter("keepingPeriod").equals("")){
			paramConfig.setScValue3(request.getParameter("keepingPeriod"));
		}
		paramConfig.setScType("FINERATE");
		paramConfig.setScKey("LATERECEIVE");
		paramConfig.setActInd("Y");
		paramConfig.setCreDate(new Date());
		paramConfig.setCreUid(userId);
		paramConfig.setUpdUid(userId);
		paramConfig.setUpdDate(new Date());
		SysConfig sc = sysConfigDAOImpl.getSysConfig(conn, paramConfig);
		boolean flag = false;
		if(sc != null){
			flag = sysConfigDAOImpl.updateSysConfig(conn, paramConfig);
		}else{
			flag = sysConfigDAOImpl.addSysCnnfig(conn, paramConfig);
		}
		if(flag){
			conn.commit();
			SysConfig lateReceiveParam = sysConfigDAOImpl.getSysConfig(conn, paramConfig);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuilder xml = new StringBuilder();
			xml.append("<?xml version='1.0' encoding='UTF-8'?>");
			xml.append("<results>");
			xml.append("<result>");
			xml.append("<commit>"+1+"</commit>");
			xml.append("<upduid>"+lateReceiveParam.getUpdUid()+"</upduid>");
			xml.append("<upddate>"+dateFormat.format(lateReceiveParam.getUpdDate())+"</upddate>");
			xml.append("</result>");
			xml.append("</results>");
			out.print(xml.toString());
		}else{
			conn.rollback();
			out.print(0);
		}
	}

	private void saveDifferParam(Connection conn, String curIntake,
			String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String i = request.getParameter("i");
		String scType = "DIFFVALUE"+i;
		String scKey = "DIFFERENCE";
		String differ = request.getParameter("differ");
		
		if(differ != null && !differ.equals("")){
			SysConfig config = new SysConfig();
			config.setScType(scType);
			config.setScKey(scKey);
			config.setActInd("Y");
			config = sysConfigDAOImpl.getSysConfig(conn, config);
			config.setScValue1(differ);
			config.setUpdDate(new Date());
			config.setUpdUid(userId);
			boolean flag = sysConfigDAOImpl.updateSysConfig(conn, config);
			if(flag){
				conn.commit();
				out.print(1);
			}else{
				conn.rollback();
				out.print(0);
			}
		}
	}

	private void savePrePaid(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		SysConfigDAOImpl SysConfigDAOImpl = new SysConfigDAOImpl();
		
		String scType = "PREPAID";
		String faculty = request.getParameter("faculty");
		String prepaid = request.getParameter("prepaid");
		String currency = request.getParameter("currency");
		SysConfig prepaidConfig = new SysConfig();
		prepaidConfig.setScType(scType);
		prepaidConfig.setScKey(faculty);
		prepaidConfig.setScValue1(prepaid);
		prepaidConfig.setScValue2(currency);
		prepaidConfig.setScValue3("");
		prepaidConfig.setScChnDesc("");
		prepaidConfig.setScEngDesc("");
		prepaidConfig.setUpdUid(userId);
		prepaidConfig.setUpdDate(new Date());
		prepaidConfig.setActInd("Y");
		boolean flag = SysConfigDAOImpl.updateSysConfig(conn, prepaidConfig);
		if(flag){
			conn.commit();
			out.print(1);
		}else{
			conn.rollback();
			out.print(0);
		}
	}

	private void addSysTime(Connection conn, String curIntake, String userId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String period = request.getParameter("period");
		String[] msg = period.split(",");
		String scType = msg[0];
		String scChnDesc = msg[1];
		String scEngDesc = msg[2];
		String intake = request.getParameter("intake");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String fromTime = request.getParameter("fromTime");
		String toTime = request.getParameter("toTime");
		SysConfig sc = new SysConfig();
		sc.setScType(scType);
		sc.setScKey(intake);
		sc.setScValue1(fromDate+" "+fromTime);
		sc.setScValue2(toDate+" "+toTime);
		sc.setScChnDesc(scChnDesc);
		sc.setScEngDesc(scEngDesc);
		sc.setActInd("Y");
		sc.setCreUid(userId);
		sc.setUpdUid(userId);
		
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		String opType = "add";
		boolean flag = sysConfigDAOImpl.getCheckSysTime(conn, sc, opType);
		if(flag){
			int temp = sysConfigDAOImpl.getAddConfTime(conn, sc);
			if(temp > 0){
				boolean addIntake = sysConfigDAOImpl.addSysIntake(conn, intake, userId);
				if(addIntake){
					conn.commit();
					
					Date psDate = new Date();
					String curPeriod = sysConfigDAOImpl.checkPeriod(conn, psDate);
					request.getSession().setAttribute("period", curPeriod);
					
					boolean rel = sysConfigDAOImpl.checkWithdraw(conn, curIntake, psDate);
					String withdraw = "";
					if(rel){
						withdraw = "true";
					}else{
						withdraw = "false";
					}
					request.getSession().setAttribute("withdraw", withdraw);
					
					request.setAttribute("msg", "Time Adding Success!");
					request.setAttribute("type", "AddSysTime");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}else{
					conn.rollback();
					request.setAttribute("msg", "Time Adding Failed!");
					request.setAttribute("type", "AddSysTime");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}
			}else{
				if(temp == 0){
					conn.rollback();
					request.setAttribute("msg", "Time Adding Failed!");
					request.setAttribute("type", "AddSysTime");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}else{
					conn.rollback();
					request.setAttribute("msg", "The Time has existed in the system!");
					request.setAttribute("type", "AddSysTime");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}
			}
		}else{
			request.setAttribute("msg", "輸入的時間與其他時段時間衝突，請檢查！");
			request.setAttribute("type", "sysTime");
			request.getRequestDispatcher("msg.jsp").forward(request, response);
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
