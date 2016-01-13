package edu.must.tos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Difference;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.Student;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.bean.Transaction;
import edu.must.tos.bean.VBookFeeForApp;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.impl.TransactionDAOImpl;
import edu.must.tos.impl.VBookFeeForAppDAOImpl;
import edu.must.tos.util.Calculate;

public class DifferenceServlet extends HttpServlet {

	public DifferenceServlet() {
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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		Connection conn = null;
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String type = null;
			if(request.getParameter("type") != null && !request.getParameter("type").equals("")){
				type = request.getParameter("type");
			}
			
	        String userId = (String)session.getAttribute("userId");
			
			//獲取學員的編號
			String studNo = null;
			String applicantNo = null;
			if(request.getParameter("studNo") != null && !request.getParameter("studNo").equals("null")){
				studNo = request.getParameter("studNo");
			}
			if(request.getParameter("applicantNo") != null && !request.getParameter("applicantNo").equals("null")){
				applicantNo = request.getParameter("applicantNo");
			}
			
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			
			//獲取當前學期
			String key = "CURRINTAKE";
			String orderIntake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			//當前學期匯率
			SysConfig curRate = new SysConfig();
			curRate.setScType(orderIntake);
			curRate.setActInd("Y");
			String curInRMBRate = "1";
			String curInHKDRate = "1";
			List<SysConfig> curInRateList = sysConfigDAOImpl.getSysConfigList(conn, curRate);
			for(SysConfig config : curInRateList){
				if(config.getScKey() != null && "RMB".equals(config.getScKey())){
					curInRMBRate = config.getScValue1();
				}else if(config.getScKey() != null && "HKD".equals(config.getScKey())){
					curInHKDRate = config.getScValue1();
				}
			}
			request.setAttribute("curInRMBRate", curInRMBRate);
			request.setAttribute("curInHKDRate", curInHKDRate);

			StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
			
			
			if(type != null && type.equals("view")){
				Student stu = new Student();
		        stu.setStudentNo(studNo);
		        stu.setApplicantNo(applicantNo);
		        boolean isNewStud = studentDAOImpl.isNewStudent(conn, stu);
		        request.setAttribute("isNewStud", isNewStud==true?"Y":"N");
				forwardPage(conn, orderIntake, userId, request, response, studNo, applicantNo);
			}
			else if(type != null && type.equals("printDifferReceipt")){
				Student stu = new Student();
		        stu.setStudentNo(studNo);
		        stu.setApplicantNo(applicantNo);
		        boolean isNewStud = studentDAOImpl.isNewStudent(conn, stu);
		        request.setAttribute("isNewStud", isNewStud==true?"Y":"N");
		        String currency = request.getParameter("currency");
		        request.setAttribute("paidCurrency", currency);
				printDifferReceipt(conn, request, response, orderIntake, userId, studNo, applicantNo);
			}
			else if(type != null && type.equals("dealDifference")){
				dealDifference(conn, request, response, orderIntake, userId, studNo, applicantNo);
			}
			else if(type != null && type.equals("checkOrdersPaidStatus")){
				checkOrdersPaidStatus(conn, request, response, orderIntake, userId, studNo, applicantNo);
			}
			else if(type != null && type.equals("keepingFee")){
				getKeepingFee(conn, request, response, orderIntake, userId, studNo, applicantNo);
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
	
	private void getKeepingFee(Connection conn, HttpServletRequest request, HttpServletResponse response, 
			String orderIntake, String userId, String studNo, String applicantNo) throws Exception {
		PrintWriter out = response.getWriter();
		
		String keeping = request.getSession().getAttribute("KEEPING").toString();
		SysConfig keepingPeriod = null;
		if(request.getSession().getAttribute("keepingPeriod") != null){
			keepingPeriod = (SysConfig)request.getSession().getAttribute("keepingPeriod");
		}
		String seqNoStr = null;
		if(request.getParameter("seqNoStr") != null){
			seqNoStr = request.getParameter("seqNoStr");
		}
		List<Integer> seqNoList = new ArrayList();
		if(seqNoStr.length()>0){
			if(seqNoStr.indexOf(",")<0){
				seqNoList.add(Integer.parseInt(seqNoStr));
			}else{
				String no[] = seqNoStr.split(",");
				for(int i=0;i<no.length;i++){
					seqNoList.add(Integer.parseInt(no[i]));
				}
			}
		}
		//DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		OrderDetailDAOImpl orDetailDAOImpl = new OrderDetailDAOImpl();
		boolean lateFlag = false;
		if(!seqNoList.isEmpty()){
			for(Integer no : seqNoList){
				OrDetail od = new OrDetail();
				od.setOrderSeqNo(no);
				List<OrDetail> orDetailList = orDetailDAOImpl.getOrDetailInfo(conn, od);
				if(orDetailList!=null && !orDetailList.isEmpty()){
					for(OrDetail detail : orDetailList){
						//1009學年收取保管費開始日期
						//boolean flag = detail.getCreDate().before(dtf.parse("2010-06-11 23:59:59"));
						
						if(detail.getNotEnoughQty()<0 && "Y".equals(keeping)){
							lateFlag = true;
							break;
						}
					}
				}
				if(lateFlag == true){
					break;
				}
			}
		}
		double fee = 0;
		if(lateFlag == true ){
			//保管費
			String keepingFee = "0";
			String keepingDay = "0";
			String keepingDate = null;
			if(keepingPeriod != null && keepingPeriod.getScValue1() != null){
				keepingFee = keepingPeriod.getScValue1();
			}
			if(keepingPeriod != null && keepingPeriod.getScValue2() != null){
				keepingDay = keepingPeriod.getScValue2();
			}
			if(keepingPeriod != null && keepingPeriod.getScValue3() != null){
				keepingDate = keepingPeriod.getScValue3();
			}
			
			DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			Date receiveEnd = dt.parse(keepingDate);
			Date now = new Date();
			long days = now.getTime() - receiveEnd.getTime();
			days = days/1000/60/60/24;
			if(days > Integer.parseInt(keepingDay)){
				fee = new BigDecimal(keepingDay).multiply(new BigDecimal(keepingFee)).doubleValue();
			}else{
				fee = new BigDecimal(days).multiply(new BigDecimal(keepingFee)).doubleValue();
			}
		}
		out.print(fee);
	}

	private void checkOrdersPaidStatus(Connection conn, HttpServletRequest request, HttpServletResponse response,
			String orderIntake, String userId, String studNo, String applicantNo) throws Exception {
		PrintWriter out = response.getWriter();
		OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		String studParam = studNo + "," + applicantNo;
		//獲取有關該學生所有訂單記錄
		List<Order> orderList = orderDAOImpl.getAllOrderSeqNoInfo(conn, studParam, orderIntake, "Y");
		if(orderList.isEmpty()){
			//check inactive orders
			int intType = 0;
			List<Order> inactiveOrderList = orderDAOImpl.getAllOrderSeqNoInfo(conn, studParam, orderIntake, "N");
			for(Order o : inactiveOrderList){
				if(o.getPaidCurrency() != null){
					if(o.getPaidStatus().equals("Y")){
						OrDetail od = new OrDetail();
						od.setActInd("N");
						od.setOrderSeqNo(o.getOrderSeqNo());
						List onlyList = new ArrayList();
						List<OrDetail> inactiveOrdetailList = orderDetailDAOImpl.getOrDetailInfo(conn, od);
						if(inactiveOrdetailList != null){
							for(OrDetail ordetail : inactiveOrdetailList){
								if(!"STUDENT".equals(ordetail.getUpdUid()))
									onlyList.add(ordetail);
							}							
						}
						if(onlyList != null && onlyList.size() == 1){
							intType = 2;
							break;
						}
					}
				}
			}
			out.print(intType);
		}else{
			int intType = 0;
			for(Order o : orderList){
				if (o.getPaidCurrency() != null) {
					if (o.getPaidStatus().equals("Y")) {
						intType = 2;	//進入版面
						break;
					}
				} else {
					if(o.getPaidStatus().equals("N")){
						intType = 1;	//提示信息
						break;
					}
				}
			}
			if(intType == 0){
				out.print(2);	//all the orders are paid
			}else if(intType == 2){
				out.print(2);
			}else{
				out.print(1);	//one order is not paid
			}
		}
	}

	private void dealDifference(Connection conn, HttpServletRequest request, HttpServletResponse response, 
			String orderIntake, String userId, String studNo, String applicantNo) throws Exception {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("ContentType","text/xml");
		OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
		OrderDetailDAOImpl odImpl = new OrderDetailDAOImpl();
		TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
		PrintWriter out = response.getWriter();
		
		String netCurrency = null;
		if(request.getParameter("netCurrency") != null){
			netCurrency = request.getParameter("netCurrency");
		}
		String mopRate = null;
		if(request.getParameter("mopRate") != null){
			mopRate = request.getParameter("mopRate");
		}
		String rmbRate = null;
		if(request.getParameter("rmbRate") != null){
			rmbRate = request.getParameter("rmbRate");
		}
		String hkdRate = null;
		if(request.getParameter("hkdRate") != null){
			hkdRate = request.getParameter("hkdRate");
		}
		List tranList = null;
		if(request.getSession().getAttribute("tranList") != null){
			tranList = (List)request.getSession().getAttribute("tranList");
		}else{
			tranList = new ArrayList();
		}
		
		List<Difference> differ = (List)request.getSession().getAttribute("differList");
		List<Difference> dealList = new ArrayList();
		List odList = new ArrayList();
		int orderSeqNo = 0;
		if(differ != null && !differ.isEmpty()){
			for(Difference d : differ){
				if(d.getDifference() != 0){
					dealList.add(d);
					List bookPriceList = new ArrayList();
					for(int i=0; i<d.getIsbnList().size(); i++){
						String isbnValue = (String)d.getIsbnList().get(i);
						double bookPriceValue = (Double)d.getBookPriceList().get(i);
						OrDetail od = new OrDetail();
						od.setOrderSeqNo(d.getOrderSeqNo());
						od.setOrderIntake(orderIntake);
						od.setIsbn(isbnValue);
						od.setUpdDate(new Date());
						od.setUpdUid(userId);
						od.setPaidAmount(bookPriceValue);
						bookPriceList.add(od);
					}
					odList.add(bookPriceList);
				}else if(d.getDifference() == 0){
					dealList.add(d);
					List bookPriceList = new ArrayList();
					for(int i=0; i<d.getIsbnList().size(); i++){
						String isbnValue = (String)d.getIsbnList().get(i);
						double bookPriceValue = (Double)d.getBookPriceList().get(i);
						OrDetail od = new OrDetail();
						od.setOrderSeqNo(d.getOrderSeqNo());
						od.setOrderIntake(orderIntake);
						od.setIsbn(isbnValue);
						od.setUpdDate(new Date());
						od.setUpdUid(userId);
						od.setPaidAmount(bookPriceValue);
						bookPriceList.add(od);
					}
					odList.add(bookPriceList);
				}
			}
		}
		boolean transactionFlag = true;
		boolean updateFlag = true;
		if(dealList != null && !dealList.isEmpty()){
			orderSeqNo = (int)dealList.get(0).getOrderSeqNo();
			//若訂單的補差價為零，不需要update到TORDER表內
			updateFlag = orderDAOImpl.updateOrderDifference(conn, orderIntake, dealList, userId);
			
			for(Difference d : dealList){
				Transaction tra = new Transaction();
				tra.setTransactionNo(0);
				tra.setOrderSeqNo(String.valueOf(d.getOrderSeqNo()));
				tra.setCashier(userId);
				tra.setPaidDate(new Date());
				tra.setPaidMentType("補差價");
				tra.setPaidCurrency(netCurrency);
				if("MOP".equals(netCurrency)){
					tra.setPay(d.getDifferenceMop());
				} else if ("RMB".equals(netCurrency)){
					tra.setPay(new BigDecimal(d.getDifferenceMop()).multiply(new BigDecimal(rmbRate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
				} else {
					tra.setPay(new BigDecimal(d.getDifferenceMop()).multiply(new BigDecimal(hkdRate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
				}				
				tra.setStudentNo(d.getStudentNo());
				tra.setStatus("Y");
				//當書價與預付價一樣的時候，補差價記錄都會記錄在交易表內
				transactionFlag = transactionDAOImpl.addTransactionInfo(conn, tra);
				if(!transactionFlag){
					transactionFlag = false;
					break;
				}
			}
		}
		
		boolean tranFlag = true;
		Date date = null;
		String chauid = null;
		if(tranList != null && !tranList.isEmpty()){
			for(int j=0; j<tranList.size(); j++){
				Transaction transaction = (Transaction)tranList.get(j);
				double netPay = 0;
				
				if("MOP".equals(netCurrency)){
					netPay = transaction.getPay();
				}else if("RMB".equals(netCurrency)){
					netPay = new BigDecimal(transaction.getPay()).multiply(new BigDecimal(Double.valueOf(rmbRate))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				} else {
					netPay = new BigDecimal(transaction.getPay()).multiply(new BigDecimal(Double.valueOf(hkdRate))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
				
				//Default Paid Currency is MOP
				transaction.setPay(transaction.getPay());
				transaction.setPaidCurrency(transaction.getPaidCurrency());
				transaction.setRemarks(transaction.getRemarks() + "(" + netCurrency + ":" + netPay + ")");
				transaction.setPaidDate(new Date());
				date = transaction.getPaidDate();
				chauid = transaction.getCashier();						
				tranFlag = transactionDAOImpl.addTransactionInfo(conn, transaction);
				if(!tranFlag){
					tranFlag = false;
					break;
				}
			}
		}
		
		List<Transaction> rePrintFeeList = (List)request.getSession().getAttribute("rePrintFeeList");
		List<Transaction> doRePrintFeeList = (List)request.getSession().getAttribute("doRePrintFeeList");
		boolean rePrintFlag = true;
		if(doRePrintFeeList != null && !doRePrintFeeList.isEmpty()){
			for(Transaction tra : doRePrintFeeList){
				//M 表示重印收據的費用已Mark，但未收費狀態
				if("M".equals(tra.getStatus())){
					//重印收據的幣種默認為MOP
					if(!tra.getPaidCurrency().equals(netCurrency)){
						String remarks = tra.getPaidCurrency() + ":" + tra.getPay();
						if("RMB".equals(netCurrency)){							
							tra.setPaidCurrency(netCurrency);
							Double pay = new BigDecimal(tra.getPay()).multiply(new BigDecimal(Double.valueOf(rmbRate))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
							tra.setPay(pay);
							tra.setRemarks(remarks);
						}
						if("HKD".equals(netCurrency)){							
							tra.setPaidCurrency(netCurrency);
							Double pay = new BigDecimal(tra.getPay()).multiply(new BigDecimal(Double.valueOf(hkdRate))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
							tra.setPay(pay);
							tra.setRemarks(remarks);
						}
					} else {
					    tra.setPaidCurrency(netCurrency);
	                    tra.setPay(tra.getPay());
	                    tra.setRemarks(tra.getPaidCurrency() + ":" + tra.getPay());
					}
					tra.setStatus("Y");
					tra.setPaidDate(new Date());
					tra.setCashier(userId);
					rePrintFlag = transactionDAOImpl.updTransactionInfo(conn, tra);
					if(!rePrintFlag){
						rePrintFlag = false;
						break;
					}
				}
			}
		}
		
		boolean updateBookPriceFlag = true;
		if(!odList.isEmpty()){
			for(int i=0; i<odList.size(); i++){
				List bookPriceList = (List)odList.get(i);
				updateBookPriceFlag = odImpl.updateOdBookPrice(conn, bookPriceList);
				if(!updateBookPriceFlag){
					updateBookPriceFlag = false;
					break;
				}
			}
		}
		if(updateFlag && transactionFlag && tranFlag && updateBookPriceFlag && rePrintFlag){
			conn.commit();
			request.getSession().removeAttribute("differList");
			//更新成功后獲取order信息
			Order updOrder = new Order();
			if(!dealList.isEmpty() && !tranList.isEmpty()){
				updOrder = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNo);
			}else if(!dealList.isEmpty() && tranList.isEmpty()){
				updOrder = orderDAOImpl.getOrderBySeqNo(conn, orderSeqNo);
			}else if(dealList.isEmpty() && !tranList.isEmpty()){
				updOrder.setUpdDate(date);
				updOrder.setChaUid(chauid);
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuilder xml = new StringBuilder();
			xml.append("<?xml version='1.0' encoding='UTF-8'?>");
			xml.append("<results>");
			xml.append("<result>");
			xml.append("<commit>"+1+"</commit>");
			xml.append("<chauid>"+updOrder.getChaUid()+"</chauid>");
			xml.append("<upddate>"+dateFormat.format(updOrder.getUpdDate())+"</upddate>");
			xml.append("</result>");
			xml.append("</results>");
			out.print(xml.toString());
		}else{
			conn.rollback();
			//更新不成功時輸出的XML信息
			StringBuilder xml = new StringBuilder();
			xml.append("<?xml version='1.0' encoding='UTF-8'?>");
			xml.append("<results>");
			xml.append("<result>");
			xml.append("<commit>"+0+"</commit>");
			xml.append("<chauid></chauid>");
			xml.append("<upddate></upddate>");
			xml.append("</result>");
			xml.append("</results>");
			out.print(xml.toString());
		}
	}

	private void printDifferReceipt(Connection conn, HttpServletRequest request, HttpServletResponse response,
			String orderIntake, String userId, String studNo, String applicantNo) throws Exception {
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		String netCurrency = request.getParameter("netCurrency");
		
		//獲取該學生的vbookfeeforapp表的記錄
		VBookFeeForAppDAOImpl vbookfeeforappDAOImpl = new VBookFeeForAppDAOImpl();
		VBookFeeForApp v = new VBookFeeForApp();
		v.setStudentNo(studNo);
		v.setApplicantNo(applicantNo);
		v.setPaidIntake(orderIntake);
		VBookFeeForApp vbookfeeforapp = vbookfeeforappDAOImpl.getVBookFeeForAppInfo(conn, v);
		//計算補差價
		List<Difference> differList = Calculate.getDifferAmount(conn, studNo+","+applicantNo, orderIntake, vbookfeeforapp.getPaidAmount());
		
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
		if(differConfig1.getScValue1()!=null){
			differ1 = Double.parseDouble(differConfig1.getScValue1());
		}
		
		SysConfig differConfig2 = new SysConfig();
		differConfig2.setScType(scType2);
		differConfig2.setScKey(scKey);
		differConfig2.setActInd("Y");
		differConfig2 = sysConfigDAOImpl.getSysConfig(conn, differConfig2);	//退款參數
		double differ2 = 0;
		if(differConfig2.getScValue1()!=null){
			differ2 = Double.parseDouble(differConfig2.getScValue1());
		}
		
		List tranList = (List)request.getSession().getAttribute("tranList");
		List<Difference> differListSession = (List)request.getSession().getAttribute("differListSession");
		String chauid = "";
		if(request.getParameter("chauid")!=null){
			chauid = request.getParameter("chauid");
		}
		String upddate = "";
		if(request.getParameter("upddate")!=null){
			upddate = request.getParameter("upddate");
		}
		StudentDAOImpl stuImpl = new StudentDAOImpl();
		List stuDetList =  stuImpl.showStudentDetail(conn, studNo+","+applicantNo);
		request.setAttribute("stuDetList", stuDetList);
		
		//檢查是否處理過補差價
		double mopPrice = 0, rmbPrice = 0;
		TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl ();
		List<Integer> seqNoList = new ArrayList<Integer>();
		for(Difference d : differListSession){
			if(d.getPaidcurrency().equals("MOP")){
				mopPrice += d.getDifference();
			}else{
				rmbPrice += d.getDifference();
			}
			seqNoList.add(d.getOrderSeqNo());
		}
		
		//獲取系統設置的調整金額選項
		SysConfig adjust = new SysConfig();
		adjust.setScType("ADJUSTITEM");
		adjust.setScKey("ITEM");
		adjust.setActInd("Y");
		SysConfig adjustConfig = sysConfigDAOImpl.getSysConfig(conn, adjust);
		
		List itemList = new ArrayList();
		if(adjustConfig!=null && adjustConfig.getScValue1().indexOf("/")>0){
			String item[] = adjustConfig.getScValue1().split("/");
			for(int i=0; i<item.length; i++){
				itemList.add(item[i]);
			}
		}
		itemList.add("保管費");
		
		SysConfig rateConfig = new SysConfig();
		rateConfig.setActInd("Y");
		rateConfig.setScType(orderIntake);
		List<SysConfig> rateList = sysConfigDAOImpl.getSysConfigList(conn, rateConfig);
		String rmbRate = "";
		String hkdRate = "";
		if(rateList != null && !rateList.isEmpty()){
			for(SysConfig config : rateList){
				if("HKD".equals(config.getScKey())){
					hkdRate = config.getScValue1();
				} else if("RMB".equals(config.getScKey())){
					rmbRate = config.getScValue1();
				}
			}
		}
		request.setAttribute("rmbRate", rmbRate);
		request.setAttribute("hkdRate", hkdRate);
		
		List itemFeeList = new ArrayList();
		List rePrintFeeList = new ArrayList();
		if(!seqNoList.isEmpty()){
			itemFeeList = transactionDAOImpl.getTransactionByItem(conn, seqNoList, itemList, "Y");
			rePrintFeeList = transactionDAOImpl.getTransactionByItem(conn, seqNoList, Arrays.asList("REPRINTFEE"), null);
		}
		boolean dealMop = false, dealRmb = false;
		
		if(mopPrice<0){
			if(Math.abs(mopPrice)>=differ2){
				dealMop = true;
			}
		}else if(mopPrice>=0){
			if(mopPrice>=differ1){
				dealMop = true;
			}
		}
		if(rmbPrice<0){
			if(Math.abs(rmbPrice)>=differ2){
				dealRmb = true;
			}
		}else if(rmbPrice>=0){
			if(rmbPrice>=differ1){
				dealRmb = true;
			}
		}
		request.setAttribute("chauid", chauid);
		request.setAttribute("upddate", upddate);
		request.setAttribute("userId", userId);
		request.setAttribute("curIntake", orderIntake);
		request.setAttribute("currency", netCurrency);
		request.setAttribute("paidamount", vbookfeeforapp.getPaidAmount());
		request.setAttribute("dealMop", dealMop);
		request.setAttribute("dealRmb", dealRmb);
		request.setAttribute("differList", differListSession);
		request.setAttribute("tranList", tranList);
		request.setAttribute("rePrintFeeList", rePrintFeeList);
		request.setAttribute("itemFeeList", itemFeeList);
		request.getRequestDispatcher("viewDifferReceipt.jsp").forward(request, response);
	}

	private void forwardPage(Connection conn, String orderIntake, String userId, HttpServletRequest request,
			HttpServletResponse response, String studNo, String applicantNo) throws Exception {
		SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
		
		//獲取該學生的vbookfeeforapp表的記錄
		VBookFeeForAppDAOImpl vbookfeeforappDAOImpl = new VBookFeeForAppDAOImpl();
		VBookFeeForApp v = new VBookFeeForApp();
		v.setStudentNo(studNo);
		v.setApplicantNo(applicantNo);
		v.setPaidIntake(orderIntake);
		VBookFeeForApp vbookfeeforapp = vbookfeeforappDAOImpl.getVBookFeeForAppInfo(conn, v);
		
		//計算補差價
		List<Difference> differList = Calculate.getDifferAmount(conn, studNo+","+applicantNo, orderIntake, vbookfeeforapp.getPaidAmount());
		
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
		
		request.getSession().removeAttribute("tranList");
		
		//學生詳細資料
		StudentDAOImpl stuImpl = new StudentDAOImpl();
		List stuDetList =  stuImpl.showStudentDetail(conn, studNo+","+applicantNo);
		request.setAttribute("stuDetList", stuDetList);
		
		//獲取系統設置的調整金額選項
		SysConfig adjust = new SysConfig();
		adjust.setScType("ADJUSTITEM");
		adjust.setScKey("ITEM");
		adjust.setActInd("Y");
		SysConfig adjustConfig = sysConfigDAOImpl.getSysConfig(conn, adjust);
		
		List itemList = new ArrayList();
		if(adjustConfig != null && adjustConfig.getScValue1().indexOf("/") > 0){
			String item[] = adjustConfig.getScValue1().split("/");
			for(int i=0; i<item.length; i++){
				itemList.add(item[i]);
			}
		}
		itemList.add("保管費");
		
		/* 不使用EXCHANGERATE参数的汇率
		SysConfig rateConfig = new SysConfig();
		rateConfig.setActInd("Y");
		rateConfig.setScType("EXCHANGERATE");
		List<SysConfig> exchangeRateList = sysConfigDAOImpl.getSysConfigList(conn, rateConfig);
		String rmbRate = "";
		String mopRate = "";
		if(exchangeRateList != null && !exchangeRateList.isEmpty()){
			for(SysConfig config : exchangeRateList){
				if("MOP".equals(config.getScKey())){
					mopRate = config.getScValue1();
				} else if("RMB".equals(config.getScKey())){
					rmbRate = config.getScValue1();
				}
			}
		}
		request.setAttribute("rmbRate", rmbRate);
		request.setAttribute("mopRate", mopRate);
		*/
		
		TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
		List<Integer> seqNoList = new ArrayList<Integer>();
		
		//檢查是否處理過補差價
		double mopPrice = 0, rmbPrice = 0;
		for(Difference d : differList){
			if(d.getPaidcurrency().equals("MOP")){
				mopPrice += d.getDifference();
			}else{
				rmbPrice += d.getDifference();
			}
			seqNoList.add(d.getOrderSeqNo());
		}
		List itemFeeList = new ArrayList();
		List rePrintFeeList = new ArrayList();
		List doRePrintFeeList = new ArrayList();
		if(seqNoList != null && !seqNoList.isEmpty()){
			itemFeeList = transactionDAOImpl.getTransactionByItem(conn, seqNoList, itemList, "Y");
			rePrintFeeList = transactionDAOImpl.getTransactionByItem(conn, seqNoList, Arrays.asList("REPRINTFEE"), null);
			doRePrintFeeList = transactionDAOImpl.getTransactionByItem(conn, seqNoList, Arrays.asList("REPRINTFEE"), null);
		}
		
		boolean dealMop = false, dealRmb = false;
		if(mopPrice < 0){
			if(Math.abs(mopPrice) >= differ2){
				dealMop = true;
			}
		}else if(mopPrice >= 0){
			if(mopPrice >= differ1){
				dealMop = true;
			}
		}
		if(rmbPrice < 0){
			if(Math.abs(rmbPrice) >= differ2){
				dealRmb = true;
			}
		}else if(rmbPrice >= 0){
			if(rmbPrice >= differ1){
				dealRmb = true;
			}
		}
		request.setAttribute("currency", vbookfeeforapp.getPaidCurrency());
		request.setAttribute("paidamount", vbookfeeforapp.getPaidAmount());
		request.setAttribute("dealMop", dealMop);
		request.setAttribute("dealRmb", dealRmb);
		request.getSession().setAttribute("differList", differList);
		request.getSession().setAttribute("differListSession", differList);
		request.setAttribute("differList", differList);
		request.setAttribute("itemFeeList", itemFeeList);
		request.setAttribute("rePrintFeeList", rePrintFeeList);
		//用於打印補差價收據
		request.getSession().setAttribute("rePrintFeeList", rePrintFeeList);
		//用於在補差價時，處理重印收據收費
		request.getSession().setAttribute("doRePrintFeeList", doRePrintFeeList);
		request.setAttribute("adjustConfig", adjustConfig);
		request.setAttribute("seqNoList", seqNoList);
		request.getRequestDispatcher("dealDifference.jsp").forward(request, response);
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