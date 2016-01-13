<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.util.Date,java.text.*" %>
<%@ page import="java.math.*" %>
<%@ page import="edu.must.tos.bean.StudReceipt,edu.must.tos.bean.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收據信息</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
body {
	margin-left: 10px;
	margin-top: 5px;
	margin-right: 10px;
	margin-bottom: 5px;
	font-family: Verdana, Arial, Helvetica, sans-serif, "宋體";
	font-size: 13px;
	font-style: normal;
}
p{
	margin-top: 5px 10px 5px 10px;
}
.number{
	font-family:Arial, Helvetica, sans-serif;
	font-size:12px;
	color:#000;
}
</style>
<script language=javascript> 
function doPrint(){
	var btn = document.getElementById("button");
	btn.style.display = 'none';
	print();
}
</script>
</head>
<body>
<input id="button" type="button" name="button" onClick="doPrint()" value="列印收據">
<!--startprint-->
<%
//是否重啟收據標識
String rePrint = "";
if(request.getAttribute("rePrint") != null){
	rePrint = request.getAttribute("rePrint").toString();
}
//收據細明記錄
List receiptList = null;
if(request.getAttribute("receiptList") != null ){
	receiptList = (List)request.getAttribute("receiptList");
}
String userId = null;
if(session.getAttribute("userId") != null){
	userId = (String)session.getAttribute("userId");
}
//會計處新生收款金額
double vbookfeeforappAmount = 0;
if(request.getAttribute("vbookfeeforappAmount") != null){
	vbookfeeforappAmount = Double.parseDouble(request.getAttribute("vbookfeeforappAmount").toString());
}
//若訂單金額為零，顯示會計處收款的金額數值
StudReceipt sr = (StudReceipt)receiptList.get(0);
if(sr != null && sr.getPrePaidMop() == 0){
	sr.setPrePaidMop(vbookfeeforappAmount);
}
if(sr != null && sr.getPrePaidRmb() == 0){
	sr.setPrePaidRmb(vbookfeeforappAmount);
}

String studNo = null;
if(request.getAttribute("studentNo") != null){
	studNo = (String)request.getAttribute("studentNo");
}
//當前學期
String scintake = null;
if(request.getAttribute("scintake") != null){
	scintake = (String)request.getAttribute("scintake");
}
//領書時間
Period receivedPeriod = null;
if(request.getAttribute("receivedPeriod") != null){
	receivedPeriod = (Period)request.getAttribute("receivedPeriod");
}
//逾期付款罰款
float amercePercent = 0;
amercePercent = Float.parseFloat(request.getAttribute("amercePercent").toString());
BigDecimal amerce = new BigDecimal(Double.toString(amercePercent));

double fineforlatepay = 0;
if(request.getAttribute("fineforlatepay") != null){
	fineforlatepay = Double.parseDouble(request.getAttribute("fineforlatepay").toString());
}
//退運費百分比參數
double feePercent = 0;
if(request.getAttribute("ratePercent") != null){
	feePercent = Double.parseDouble(request.getAttribute("ratePercent").toString());
}
//已收取的補訂運費百分比
double shippingFee = 0;
if(request.getAttribute("shippingFee") != null){
	shippingFee = Double.parseDouble(request.getAttribute("shippingFee").toString());
}

String currency = null;

double differMop = 0;
double differRmb = 0;

String fromDate = "";
String toDate = "";

Date now = new Date();
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String Datetime = df.format(now);
if(receivedPeriod != null){
	fromDate = df.format(receivedPeriod.getStartTime());
	toDate = df.format(receivedPeriod.getEndTime());
}
%>
<table width="100%" border="0">
  <tr>
    <td colspan="3" align="right">
    <%
    if("Y".equals(rePrint)){
    	out.print("【重印】");
    }
    %>
    </td>
  </tr>
  <tr align="left">
    <td width="18%"><img src="<%=request.getContextPath() %>/barcode?msg=<%=studNo %>" height="45px" width=180px/></td>
    <td width="40%">訂書序號:<%=sr.getOrderSeqno() %></td>
    <td width="42%">領書開始時間：<%=fromDate%><br>領書結束時間：<%=toDate%></td>
  </tr>
</table>
<p align="center">
  <strong><font size="3">澳門科技大學圖書出版及供應中心
  <br>
  <%=scintake %>學期學生書費收據(第一聯)</font>
  </strong>
</p>
<p align="center">
校園卡號：<u><span class="number"><%=sr.getStudentNo()%></span></u>&nbsp;&nbsp;
學生姓名：<u><span class="number"><%if(sr.getChineseName()!=null){out.print(sr.getChineseName());}else{out.print("");}%>/<%if(sr.getEnglishName()!=null){out.print(sr.getEnglishName());}else{out.print("");}%></span></u>&nbsp;&nbsp;
列印日期：<u><span class="number"><%=Datetime%></span></u>
<br/>
專業：<u><span class="number"><%=sr.getMajorName() %></span></u>
<hr/>
<p align="left"><font size="3">※第一步：列印領書程式表及領書收據</font></p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;學生必須憑由列印員簽署之<u>學生書費收據（共二聯）</u>到下步驟收款處付款。付款後請妥善保管好由會計人員簽署並蓋章後的收據(第二聯)，開學初領書時必須憑已付款收據領書。並請同學按照收據上的時間、日期領書，否則按『訂書、繳交書費及領書須知』有關規定處以罰金。</p>
<br>
列印員簽名：<u><%=userId %></u>&nbsp;&nbsp;&nbsp;&nbsp;日期：<u><%=Datetime %></u>
<hr>
<p align="left"><font size="3">※第二步：繳款</font></p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;請預備零錢並按應繳金額繳付</p>
<table width="98%" class="table" border="1" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td rowspan="2" width="36%" align="center">書名</td>
    <td rowspan="2" width="14%" align="center"><span class="number">ISBN</span></td>
    <td colspan="1" rowspan="2"  width="6%" align="center">預估單價<span class="number">MOP</span></td>
    <td colspan="1" rowspan="2"  width="6%" align="center">實際單價<span class="number">MOP</span></td>
    <td colspan="2" width="11%" align="center">訂書數</td>
    <td colspan="1" rowspan="2" width="6.5%" align="center">預估總價<span class="number">MOP</span></td>
    <td colspan="1" rowspan="2" width="6.5%" align="center">實際總價<span class="number">MOP</span></td>
    <td width="5%" align="center">缺書</td>
    <td width="6%" align="center" rowspan="2">備注</td>
  </tr>
  <tr>
    <td align="center">確定</td>
    <td align="center">退訂</td>
    <td align="center">(Y/N)</td>
  </tr>
  <%
  double sumMop = 0.0;
  
  double amerceMop = 0.0;
  
  double mop = 0;
  
  double shippingFeeMop = 0;
  
  double netPaidAmount = 0;
  
  double paidAmount = 0;
  
  double amerceAmount = 0;
  
  String curInRMBRate = (String)request.getAttribute("curInRMBRate");
  String curInHKDRate = (String)request.getAttribute("curInHKDRate");
  Double rmbRate = Double.parseDouble(curInRMBRate);
  Double hkdRate = Double.parseDouble(curInHKDRate);
  Double rate = new Double(1);
  int confirm = 0;
  for(int i=0; i<receiptList.size(); i++) {
	  StudReceipt studreceipt = (StudReceipt)receiptList.get(i);
	  currency = studreceipt.getPaidCurrency();
	  if(currency != null){
		  amerce = new BigDecimal(0);
		  rate = studreceipt.getCurRate();
  	  }
	  //判斷書本實價狀況
	  double bookMopPrice = 0;	//書本MOP價錢
	  double bookMopWithPrice = 0;	//退書MOP價錢
	  double bookOldWithPrice = 0;
	  double withdrawCarryMopPrice = 0;
	  String shownetMop = "";
	  if(studreceipt.getMopNetPrice() == 0 || studreceipt.getMopNetPrice() == null || studreceipt.getMopFuturePrice() == studreceipt.getMopNetPrice()){
		  shownetMop = "";
		  bookMopPrice = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
		  bookMopWithPrice = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty()))).doubleValue();
		  withdrawCarryMopPrice = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty2()))).doubleValue();
	  }else{
		  bookMopPrice = new BigDecimal(Double.toString(studreceipt.getMopNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
		  bookMopWithPrice = new BigDecimal(Double.toString(studreceipt.getMopNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty()))).doubleValue();
		  withdrawCarryMopPrice = new BigDecimal(Double.toString(studreceipt.getMopNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty2()))).doubleValue();
	  }
	  //System.out.println("bookMopPrice="+bookMopPrice+" bookMopWithPrice="+bookMopWithPrice+" withdrawCarryMopPrice="+withdrawCarryMopPrice);
	  BigDecimal v1 = new BigDecimal(Double.toString(bookMopPrice));
	  
	  //退書費用
	  double withdrawMopPrice = new BigDecimal(Integer.toString(studreceipt.getWithDrawQty())).multiply(new BigDecimal(Double.toString(studreceipt.getMopWithPrice()))).doubleValue();
	  double feeMopPrice = new BigDecimal(Double.toString(withdrawCarryMopPrice)).multiply(new BigDecimal(Double.toString(feePercent))).multiply(new BigDecimal(Double.toString(0.01))).doubleValue();
	  if(0 < feeMopPrice && feeMopPrice < 1){
		  feeMopPrice = 1;
	  }else{
		  feeMopPrice = new BigDecimal(Double.toString(feeMopPrice)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	  }
	  BigDecimal v3 = new BigDecimal(Double.toString(withdrawMopPrice)).add(new BigDecimal(Double.toString(feeMopPrice)));	  
	  
	  //v1、v2分別為MOP、RMB書本金額與確定數量的乘積
	  //v3、v4分別為MOP、RMB退書費和退運費的和
	  mop = v1.add(v3).doubleValue();
	  
	  //System.out.println("mop="+mop);
	  
	  shippingFeeMop = new BigDecimal(Double.toString(shippingFeeMop)).add(v1).add(new BigDecimal(Double.toString(bookMopWithPrice))).doubleValue();
	  
	  BigDecimal v5 = new BigDecimal(Double.toString(sumMop));
	  
	  sumMop = v1.add(v3).add(v5).doubleValue();
	  
	  BigDecimal v7 = new BigDecimal(Double.toString(amerceMop));
	  
	  BigDecimal v9 = new BigDecimal(Double.toString(bookMopWithPrice));
	  
	  //計算逾期付款罰款金額時需把退書數的罰款都計算在內
	  amerceMop = v7.add(v1).add(v9).doubleValue();
	  
	  confirm = confirm + studreceipt.getConfirmQty();
  %>
  <tr>
    <td><%=studreceipt.getTitle() %></td>
    <td><span class="number"><%=studreceipt.getIsbn()%></span></td>
    <td align="right"><span class="number">
    <%
    	double fumopPrice = 0;
    	BigDecimal mopFuture = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice()));
    	out.print(mopFuture.doubleValue());
    	fumopPrice = mopFuture.doubleValue();
    %>
    </span></td>
    <td align="right"><span class="number">
	<%
	double mopPrice = 0;
    if(studreceipt.getMopNetPrice() == 0 || studreceipt.getMopNetPrice() == null || studreceipt.getMopFuturePrice() == studreceipt.getMopNetPrice()){
    	if(studreceipt.getMopFuturePrice() != 0 ){
    		mopFuture = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice()));
    		fumopPrice = mopFuture.doubleValue();
    		out.print("");
    	}else{
    		out.print("N/A");
    		mopFuture = new BigDecimal(0);
    	}
    	mopPrice = mopFuture.doubleValue();
    }else{
    	BigDecimal mopNet = new BigDecimal(Double.toString(studreceipt.getMopNetPrice()));
    	out.print(mopNet.doubleValue());
    	mopPrice = mopNet.doubleValue();
    }
	%>
    </span></td>
    <td align="center"><span class="number"><%=studreceipt.getConfirmQty() %></span></td>
    <td align="center"><span class="number"><%=studreceipt.getWithDrawQty() %></span></td>
    <td align="right"><span class="number"><%=fumopPrice*(studreceipt.getConfirmQty()-studreceipt.getWithDrawQty()) %></span></td>
	<%-- <td align="right"><span class="number"><%=rmb %></span></td> --%>
    <td align="right"><span class="number"><%=mop %></span></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <%
  } 
  %>
  <%
  double shippingMop = 0;
  
  shippingMop = new BigDecimal(Double.toString(shippingFeeMop)).multiply(new BigDecimal(Double.toString(shippingFee))).multiply(new BigDecimal(0.01)).doubleValue();
  
  if(0 < shippingMop && shippingMop < 1){
	  shippingMop = 1;
  }else{
	  shippingMop = new BigDecimal(Double.toString(shippingMop)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
  }
  
  sumMop = new BigDecimal(sumMop).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
  netPaidAmount = new BigDecimal(sr.getNetPaidAmount()).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
  paidAmount = new BigDecimal(sr.getPrePaidMop()).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
  amerceAmount = (new BigDecimal(Double.toString(sr.getAmercemount())).add(new BigDecimal(Double.toString(shippingMop)))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
  %>
  <tr>
    <td rowspan="7">總冊數：<u><span class="number"><%=confirm %></span></u>冊</td>
    <td colspan="6" align="right">實際書本金額：</td>
    <td align="right"><span class="number"><% out.print(sumMop); %></span></td>
    <td colspan="2" rowspan="8"></td>
  </tr>
  <%-- 
  <tr>
    <td colspan="6" align="right">預付金額：</td>
    <td align="right"><span class="number"><% out.print(netPaidAmount); %></span></td>
  </tr>
  --%>
  <tr>
    <td colspan="6" align="right">罰款金額：</td>
    <td align="right"><span class="number"><% out.print(amerceAmount); %></span></td>
  </tr>
  <tr>
    <td colspan="6" align="right">逾期付款罰款金額：</td>
    <td align="right"><span class="number">
    <%
    BigDecimal amerceValue1 = new BigDecimal(Double.toString(amerceMop));
    
    BigDecimal percent = new BigDecimal(Double.toString(0.01));
    //計算逾期付款罰款，罰款小於1作1計算，大於1的進行四捨五入計算
    double amerceMopSum = 0;
    double amerceRmbSum = 0;
    
    if(amerce.doubleValue() != 0){
    	if(0<(amerce.multiply(amerceValue1).multiply(percent)).doubleValue() && (amerce.multiply(amerceValue1).multiply(percent)).doubleValue()<1){
    		amerceMopSum = 1;
    	}else{
    		amerceMopSum = (amerce.multiply(amerceValue1).multiply(percent)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    	}
    }
    //若該學員已經付款過，逾期罰款金額則顯示第一收費時所繳的金額，不再另外計算
    String remarks = "應繳";
    if(currency != null){
    	amerceMopSum = fineforlatepay;
    	remarks = "已繳";
    }
    amerceMopSum = new BigDecimal(amerceMopSum).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    out.print(amerceMopSum);
    %>
    </span></td>
  </tr>
  <tr>
    <td colspan="6" align="right">補差價：</td>
    <%
    //differMop = new BigDecimal(Double.toString(sr.getPrePaidMop())).subtract(new BigDecimal(Double.toString(sumMop))).subtract(new BigDecimal(Double.toString(sr.getAmercemount()))).subtract(new BigDecimal(Double.toString(amerceMopSum))).doubleValue();
    differMop = new BigDecimal(Double.toString(sumMop)).add(new BigDecimal(Double.toString(amerceAmount))).add(new BigDecimal(Double.toString(amerceMopSum))).subtract(new BigDecimal(netPaidAmount)).doubleValue();
    %>
    <td align="right"><span class="number"><%if(currency != null){out.print(differMop);} else {out.print(0.0);} %></span></td>
  </tr>
  <%
    double payMOP = (new BigDecimal(Double.toString(sumMop)).add(new BigDecimal(Double.toString(amerceAmount))).add(new BigDecimal(Double.toString(amerceMopSum)))).doubleValue();
    if(sr.getPrePaidMop() != 0){
    	payMOP = new BigDecimal(Double.toString(payMOP)).subtract(new BigDecimal(Double.toString(netPaidAmount))).doubleValue();
    }
    double payRMB = new BigDecimal(payMOP * rmbRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
    double payHKD = new BigDecimal(payMOP * hkdRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
    double paidMOP = 0;
    double paidRMB = 0;
    double paidHKD = 0;
    if("MOP".equals(currency)){
    	paidMOP = paidAmount;
    }
    if("RMB".equals(currency)){
    	paidRMB = paidAmount;
    }
    if("HKD".equals(currency)){
    	paidHKD = paidAmount;
    }
  %>
  <tr>
    <td colspan="6" align="right"><%=remarks %>繳<span class="number">MOP</span>金額：</td>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidMOP);} else {out.print(payMOP);} %></span></td>
  </tr>
  <tr>
    <td colspan="6" align="right"><%=remarks %>繳<span class="number">RMB</span>金額：</td>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidRMB);} else {out.print(payRMB);} %></span></td>
  </tr>
  <tr>
    <td colspan="6" align="right"><%=remarks %>繳<span class="number">HKD</span>金額：</td>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidHKD);} else {out.print(payHKD);} %></span></td>
  </tr>
</table>

<br>
會計簽署：<u>___________</u>蓋章：<u>___________</u>收款日期：<u>___________</u>
<br><br>
<p align="center"><strong><u><font size="3">備注：第一聯收據由收款員留存</font></u></strong></p>

<div style="page-break-after:always"></div>

<table width="100%" border="0">
  <tr>
    <td colspan="3" align="right">
    <%
    if("Y".equals(rePrint)){
    	out.print("【重印】");
    }
    %>
    </td>
  </tr>
  <tr align="left">
    <td width="18%"><img src="<%=request.getContextPath() %>/barcode?msg=<%=studNo %>" height="45px" width=180px/></td>
    <td width="40%">訂書序號:<%=sr.getOrderSeqno() %></td>
    <td width="42%">領書開始時間：<%=fromDate%><br>領書結束時間：<%=toDate%></td>
  </tr>
</table>
<p align="center">
  <strong><font size="3">澳門科技大學圖書出版及供應中心
  <br>
  <%=scintake%>學期學生書費收據(第二聯)</font>
  </strong>
</p>
<p align="center">
校園卡號：<u><span class="number"><%=sr.getStudentNo()%></span></u>&nbsp;&nbsp;
學生姓名：<u><span class="number"><%=sr.getChineseName()%>/<%=sr.getEnglishName()%></span></u>&nbsp;&nbsp;
列印日期：<u><span class="number"><%=Datetime%></span></u>
<br/>
專業：<u><span class="number"><%=sr.getMajorName() %></span></u>
</p>
<hr>
<u>※第一步：列印領書程式表及領書收據</u>
<p>
&nbsp;&nbsp;&nbsp;&nbsp;學生必須憑由列印員簽署之<u>學生書費收據（共二聯）</u>到下步驟收款處付款。付款後請妥善保管好由會計人員簽署並蓋章後的收據(第二聯)，開學初領書時必須憑已付款收據領書。並請同學按照收據上的時間、日期領書，否則按『訂書、繳交書費及領書須知』有關規定處以罰金。
</p>

列印員簽名：<u><%=userId %></u>&nbsp;&nbsp;&nbsp;&nbsp;
日期：<u><%=Datetime %></u>
<hr>
<p align="left"><u>※第二步：繳款(請預備零錢並按應繳金額繳付)</u></p>
<table width="98%" class="table" border="1" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td rowspan="2" width="36%" align="center">書名</td>
    <td rowspan="2" width="14%" align="center"><span class="number">ISBN</span></td>
    <td colspan="1" rowspan="2"  width="6%" align="center">預估單價<span class="number">MOP</span></td>
    <td colspan="1" rowspan="2"  width="6%" align="center">實際單價<span class="number">MOP</span></td>
    <td colspan="2" width="11%" align="center">訂書數</td>
    <td colspan="1" rowspan="2" width="6.5%" align="center">預估總價<span class="number">MOP</span></td>
    <td colspan="1" rowspan="2" width="6.5%" align="center">實際總價<span class="number">MOP</span></td>
    <td width="5%" align="center">領書</td>
    <td width="6%" align="center" rowspan="2">備注</td>
  </tr>
  <tr>

    <td align="center">確定</td>
    <td align="center">退訂</td>

    <td align="center">(Y/N)</td>
  </tr>
  <%
  double mop1 = 0,rmb1=0;
  for(int i=0; i<receiptList.size(); i++) {
	  StudReceipt studreceipt = (StudReceipt)receiptList.get(i);
	  //判斷書本實價狀況
	  double bookMopPrice = 0;	//書本MOP價錢
	  double bookMopWithPrice = 0;	//退書MOP價錢
	  double withdrawCarryMopPrice = 0;
	  if(studreceipt.getMopNetPrice() == 0 || studreceipt.getMopNetPrice() == null){
		  bookMopPrice = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
		  bookMopWithPrice = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty()))).doubleValue();
		  withdrawCarryMopPrice = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty2()))).doubleValue();
	  }else{
		  bookMopPrice = new BigDecimal(Double.toString(studreceipt.getMopNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
		  bookMopWithPrice = new BigDecimal(Double.toString(studreceipt.getMopNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty()))).doubleValue();
		  withdrawCarryMopPrice = new BigDecimal(Double.toString(studreceipt.getMopNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty2()))).doubleValue();
	  }
	  
	  BigDecimal v1 = new BigDecimal(Double.toString(bookMopPrice));
	  double bookRmbPrice = 0;	//書本RMB價錢
	  double bookRmbWithPrice = 0;	//退書RMB價錢
	  double withdrawCarryRmbPrice = 0;
	  if(studreceipt.getRmbNetPrice() == 0 || studreceipt.getRmbNetPrice() == null){
		  bookRmbPrice = new BigDecimal(Double.toString(studreceipt.getRmbFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
		  bookRmbWithPrice = new BigDecimal(Double.toString(studreceipt.getRmbFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty()))).doubleValue();
		  withdrawCarryRmbPrice = new BigDecimal(Double.toString(studreceipt.getRmbFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty2()))).doubleValue();
	  }else{
		  bookRmbPrice = new BigDecimal(Double.toString(studreceipt.getRmbNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
		  bookRmbWithPrice = new BigDecimal(Double.toString(studreceipt.getRmbNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty()))).doubleValue();
		  withdrawCarryRmbPrice = new BigDecimal(Double.toString(studreceipt.getRmbNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getWithDrawQty2()))).doubleValue();
	  }
	  BigDecimal v2 = new BigDecimal(Double.toString(bookRmbPrice));
	  //退書費用
	  double withdrawMopPrice = new BigDecimal(Integer.toString(studreceipt.getWithDrawQty())).multiply(new BigDecimal(Double.toString(studreceipt.getMopWithPrice()))).doubleValue();
	  double feeMopPrice = new BigDecimal(Double.toString(withdrawCarryMopPrice)).multiply(new BigDecimal(Double.toString(feePercent))).multiply(new BigDecimal(Double.toString(0.01))).doubleValue();
	  if(0 < feeMopPrice && feeMopPrice < 1){
		  feeMopPrice = 1;
	  }else{
		  feeMopPrice = new BigDecimal(Double.toString(feeMopPrice)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	  }
	  BigDecimal v3 = new BigDecimal(Double.toString(withdrawMopPrice)).add(new BigDecimal(Double.toString(feeMopPrice)));
	  
	  double withdrawRmbPrice = new BigDecimal(Integer.toString(studreceipt.getWithDrawQty())).multiply(new BigDecimal(Double.toString(studreceipt.getRmbWithPrice()))).doubleValue();
	  double feeRmbPrice = new BigDecimal(Double.toString(withdrawCarryRmbPrice)).multiply(new BigDecimal(Double.toString(feePercent))).multiply(new BigDecimal(Double.toString(0.01))).doubleValue();
	  if(0 < feeRmbPrice && feeRmbPrice < 1){
		  feeRmbPrice = 1;
	  }else{
		  feeRmbPrice = new BigDecimal(Double.toString(feeRmbPrice)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	  }
	  BigDecimal v4 = new BigDecimal(Double.toString(withdrawRmbPrice)).add(new BigDecimal(Double.toString(feeRmbPrice)));
	  
	  mop1 = v1.add(v3).doubleValue();
	  rmb1 = v2.add(v4).doubleValue();
  %>
  <tr>
    <td><%=studreceipt.getTitle() %></td>
    <td><span class="number"><%=studreceipt.getIsbn() %></span></td>
    <td align="right"><span class="number">
    <%
    double fumopPrice = 0;
    
    	BigDecimal mopFuture = null;
    	if(studreceipt.getMopFuturePrice() != 0){
    		mopFuture = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice()));
    		out.print(mopFuture.doubleValue());
    	}else{
    		out.print("N/A");
    		mopFuture = new BigDecimal(0);
    	}
    	fumopPrice = mopFuture.doubleValue();
    
    %>
    </span></td>
    <td align="right"><span class="number">
	<%
	double mopPrice = 0;
    if(studreceipt.getMopNetPrice() == 0 || studreceipt.getMopNetPrice() == null || studreceipt.getMopFuturePrice()==studreceipt.getMopNetPrice()){
    	BigDecimal mopFuture1 = null;
    	if(studreceipt.getMopFuturePrice() != 0){
    		mopFuture1 = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice()));
    		out.print("");
    	}else{
    		out.print("N/A");
    		mopFuture1 = new BigDecimal(0);
    	}
    	mopPrice = mopFuture1.doubleValue();
    }else{
    	BigDecimal mopFuture1 = new BigDecimal(Double.toString(studreceipt.getMopNetPrice()));
    	out.print(mopFuture1.doubleValue());
    	mopPrice = mopFuture1.doubleValue();
    }
	%>
    </span></td>
    <td align="center"><span class="number"><%=studreceipt.getConfirmQty() %></span></td>
    <td align="center"><span class="number"><%=studreceipt.getWithDrawQty() %></span></td>
    <td align="right"><span class="number"><%=fumopPrice*(studreceipt.getConfirmQty()-studreceipt.getWithDrawQty()) %></span></td>
<%--    <td align="right"><span class="number"><%=rmb1 %></span></td>--%>
    <td align="right"><span class="number"><%=mop1 %></span></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <%
  } 
  %>
  <tr>
    <td rowspan="7">總冊數：<u><span class="number"><%=confirm %></span></u>冊</td>
    <td colspan="6" align="right">實際書本金額：</td>
    <td align="right"><span class="number"><% out.print(sumMop); %></span></td>
    <td colspan="2" rowspan="8"></td>
  </tr>
  <%-- 
  <tr>
    <td colspan="6" align="right">預付金額：</td>
    <td align="right"><span class="number"><% out.print(netPaidAmount); %></span></td>
  </tr>
  --%>
  <tr>
    <td colspan="6" align="right">罰款金額：</td>
    <td align="right"><span class="number"><% out.print(amerceAmount); %></span></td>
  </tr>
  <tr>
    <td colspan="6" align="right">逾期付款罰款金額：</td>
    <td align="right"><span class="number">
    <%
    out.print(amerceMopSum);
    %>
    </span></td>
  </tr>
  <tr>
    <td colspan="6" align="right">補差價：</td>
    <td align="right"><span class="number"><% if(currency != null){out.print(differMop);} else {out.print(0.0);} %></span></td>
  </tr>
  <tr>
    <td colspan="6" align="right"><%=remarks %>總<span class="number">MOP</span>金額：</td>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidMOP);} else {out.print(payMOP);} %></span></td>  	
  </tr>
  <tr>
    <td colspan="6" align="right"><%=remarks %>總<span class="number">RMB</span>金額：</td>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidRMB);} else {out.print(payRMB);} %></span></td>
    
  </tr>
  <tr>
    <td colspan="6" align="right"><%=remarks %>總<span class="number">HKD</span>金額：</td>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidHKD);} else {out.print(payHKD);} %></span></td>

  </tr>
</table>
會計簽署：<u>___________</u> 蓋章：<u>___________</u> 收款日期：<u>___________</u>
<hr />
<u>※第三步：領書</u>
<br/>
&nbsp;&nbsp;&nbsp;&nbsp;1．派書員必須認真核實學生的校園卡號，姓名及訂書序號，確實無誤後方可派發書本。<br />
&nbsp;&nbsp;&nbsp;&nbsp;2．請派書員在第二步領書單中領書（Y/N)處填寫，已派書填(Y)，否則填(N)。<br />
&nbsp;&nbsp;&nbsp;&nbsp;3．學生領取書本請現場即時核實，如發現任何問題，請聯絡派書員進行更換或補發。
<br/>
派書員簽名：<u>___________</u> 日期：<u>___________</u>
<br/>
學生簽名：<u>___________</u> 取書日期：<u>___________</u> 學生聯系電話：<u>___________</u>
<p align="center"><strong><u><font size="2">備注：第二聯收據由學生留存，學生必須憑本收據於規定時間領書</font></u></strong></p>

<!--endprint-->
</body>
</html>
