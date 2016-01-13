<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.util.Date,java.text.*" %>
<%@ page import="java.math.*" %>
<%@ page import="edu.must.tos.bean.StudReceipt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>零售收據資訊</title>
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
.table {
	border-collapse:collapse; 	
	border:solid #999999; 	
	border-width:1px 0 0 1px; 	
	border-left: #999999 1px solid;
	border-right: #999999 1px solid;
	border-top: #999999 1px solid;
	border-bottom: #999999 1px solid;
}
.table th {
	border:solid #999999;border-width:0 1px 1px 0;padding:2px;
}
.table td {
	border:solid #999999;border-width:0 1px 1px 0;padding:2px;
}
p{
	margin-left: 10px;
	margin-top: 5px;
	margin-right: 10px;
	margin-bottom: 5px;
}
.number{
	font-family:Arial, Helvetica, sans-serif;
	font-size:12px;
	color:#000;
}
.button{
	width:auto;
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
<input id="button" type="button" class="button" name="button" onClick="doPrint()" value="列印收據">
<!--startprint-->
<%
List receiptList = null;
if(request.getAttribute("receiptList") != null ){
	receiptList = (List)request.getAttribute("receiptList");
}
String userId = null;
if(session.getAttribute("userId") != null){
	userId = (String)session.getAttribute("userId");
}
StudReceipt sr = (StudReceipt)receiptList.get(0);

String scintake = null;
if(sr != null && sr.getOrderIntake() != null){
	scintake = sr.getOrderIntake();
}

Double hkdRate = null;
if(request.getAttribute("hkdRate") != null){
	hkdRate = Double.parseDouble(request.getAttribute("hkdRate").toString());
}
Double rmbRate = null;
if(request.getAttribute("rmbRate") != null){
	rmbRate = Double.parseDouble(request.getAttribute("rmbRate").toString());
}

float amercePercent = 0;

//退運費百分比參數
double feePercent = 0;

double shippingFee = 0;

String currency = null;

double differMop = 0;

Date now = new Date();
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String Datetime = df.format(now);
%>
<table width="100%" border="0">
  <tr align="left">
    <td>訂書序號:<%=sr.getOrderSeqno() %></td>
  </tr>
</table>
<p align="center">
  <strong><font size="3">澳門科技大學圖書出版及供應中心</font></strong>
  <br>
  <strong><font size="3"><%=scintake %>學期學生書費收據(第一聯)</font></strong>
</p>
<p align="center">
校園卡號：<u><span class="number"><%=sr.getStudentNo() %></span></u>&nbsp;&nbsp;
學生姓名：<u><span class="number"><%if(sr.getChineseName() != null){out.print(sr.getChineseName());}else{out.print("");} %>/<%if(sr.getEnglishName()!=null){out.print(sr.getEnglishName());}else{out.print("");}%></span></u>&nbsp;&nbsp;
列印日期：<u><span class="number"><%=Datetime %></span></u>
<hr/>
<p align="left"><font size="3">※第一步：列印領書程式表及領書收據</font></p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;學生必須憑由列印員簽署之<u>學生書費收據（共二聯）</u>到下步驟收款處付款。付款後請妥善保管好由會計人員簽署並蓋章後的收據(第一聯)，開學初領書時必須憑已付款收據領書。並請同學按照收據上的時間、日期領書，否則按『訂書、繳交書費及領書須知』有關規定處以罰金。</p>
<br>
列印員簽名：<u><%=userId %></u>&nbsp;&nbsp;&nbsp;&nbsp;
日期：<u><%=Datetime %></u>
<hr>
<p align="left"><font size="3">※第二步：繳款</font></p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;請預備零錢並按應繳金額繳付</p>
<table width="98%" class="table" border="1" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td rowspan="2" width="36%" align="center">書名</td>
    <td rowspan="2" width="14%" align="center"><span class="number">ISBN</span></td>
    <td colspan="2" width="12%" align="center">單價</td>
    <td colspan="2" width="11%" align="center">訂書數</td>
    <td width="13%" align="center">總金額</td>
    <td width="5%" align="center">缺書</td>
    <td width="6%" align="center" rowspan="2">備注</td>
  </tr>
  <tr>
    <td align="center"><span class="number">預估MOP</span></td>
    <td align="center"><span class="number">實價MOP</span></td>
    <td align="center">確定</td>
    <td align="center">退訂</td>
    <td align="center"><span class="number">MOP</span></td>
    <td align="center">(Y/N)</td>
  </tr>
  <%
  double sumMop = 0.0;
  double sumRmb = 0.0;
  double amerceMop = 0.0;
  double mop = 0;
  
  double shippingFeeMop = 0;
  
  int confirm = 0;
  for(int i=0; i<receiptList.size(); i++) {
	  StudReceipt studreceipt = (StudReceipt)receiptList.get(i);
	  currency = studreceipt.getPaidCurrency();
	  //判斷書本實價狀況
	  double bookMopPrice = 0;	//書本MOP價錢
	  if(studreceipt.getMopNetPrice() == 0 || studreceipt.getMopNetPrice() == null){
		  bookMopPrice = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
	  }else{
		  bookMopPrice = new BigDecimal(Double.toString(studreceipt.getMopNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
	  }
	  /*
	  if(studreceipt.getPaid() != null ){
		  bookMopPrice = new BigDecimal(Double.toString(studreceipt.getPaidAmount())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
	  }
	  */
	  BigDecimal v1 = new BigDecimal(Double.toString(bookMopPrice));
	  	  
	  //v1、v2分別為MOP、RMB書本金額與確定數量的乘積
	  //v3、v4分別為MOP、RMB退書費和退運費的和
	  mop = v1.doubleValue();
	  
	  BigDecimal v5 = new BigDecimal(Double.toString(sumMop));
	  
	  sumMop = v1.add(v5).doubleValue();
	  confirm = confirm + studreceipt.getConfirmQty();
  %>
  <tr>
    <td><%=studreceipt.getTitle() %></td>
    <td><span class="number"><%=studreceipt.getIsbn()%></span></td>
    <td align="right"><span class="number">
    <%
    double mopPrice = 0;
    BigDecimal mopFuture = null;
	if(studreceipt.getMopFuturePrice() != 0){
		mopFuture = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice()));
	}else{
		mopFuture = new BigDecimal(0);
	}
	mopPrice = mopFuture.doubleValue();
	if(studreceipt.getPaidCurrency() != null ){
    	mopPrice = studreceipt.getPaidAmount();
    }
    out.print(mopPrice);    
    %>
    </span>
    </td>
    <td align="right"><span class="number">
    <%
    double mopNetPrice = 0;
    if(studreceipt.getMopNetPrice() != 0 && (studreceipt.getMopNetPrice() - studreceipt.getMopFuturePrice()) != 0){
    	BigDecimal mopNet = null;
    	if(studreceipt.getMopNetPrice() != 0){
    		mopNet = new BigDecimal(Double.toString(studreceipt.getMopNetPrice()));
    	}else{
    		mopNet = new BigDecimal(0);
    	}
    	mopNetPrice = mopNet.doubleValue();
        out.print(mopNetPrice);
    }
    %>
    </span></td>
    <td align="center"><span class="number"><%=studreceipt.getConfirmQty() %></span></td>
    <td align="center"><span class="number"><%=studreceipt.getWithDrawQty() %></span></td>
    <td align="right"><span class="number"><%=mop %></span></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <%
  } 
  %>
  <tr>
    <td rowspan="7">總冊數：<u><span class="number"><%=confirm %></span></u>冊</td>
    <td colspan="5" align="right">實際書本金額：</td>
    <td align="right"><span class="number"><%out.print(sumMop); %></span></td>
    <td colspan="2" rowspan="7"></td>
  </tr>
  <tr>
    <td colspan="5" align="right">罰款金額：</td>
    <td align="right"><span class="number">
      <%
      out.print(new BigDecimal(Double.toString(sr.getAmercemount())).doubleValue()); 
      %></span>
    </td>
  </tr>
  <tr>
    <td colspan="5" align="right">逾期付款罰款金額：</td>
    <td align="right"><span class="number">
    <%
    //計算逾期付款罰款，罰款小於1作1計算，大於1的進行四捨五入計算
    double amerceMopSum = 0;
    out.print(amerceMopSum);
    %>
    </span></td>
  </tr>
  <tr>
    <td colspan="5" align="right">補差價：</td>
    <%
    if(sr.getPaidCurrency() != null){
    	differMop = new BigDecimal(Double.toString(sumMop)).add(new BigDecimal(Double.toString(sr.getAmercemount()))).add(new BigDecimal(Double.toString(amerceMopSum))).subtract(new BigDecimal(sr.getNetPaidAmount())).doubleValue();
    }
    %>
    <td align="right"><span class="number"><%out.print(differMop); %></span></td>
  </tr>
  <tr>
    <td colspan="5" align="right">應總繳<span class="number">MOP</span>金額：</td>
    <%
    double payMOP = (new BigDecimal(Double.toString(sumMop)).add(new BigDecimal(Double.toString(sr.getAmercemount()))).add(new BigDecimal(Double.toString(amerceMopSum)))).doubleValue();
    if(sr.getNetPaidAmount() != 0){
    	payMOP = new BigDecimal(Double.toString(sr.getNetPaidAmount())).subtract(new BigDecimal(Double.toString(payMOP))).doubleValue();
    	payMOP = Math.abs(payMOP);
    }
    double paidMOP = 0;
    double paidRMB = 0;
    double paidHKD = 0;
    if("MOP".equals(currency)){
    	paidMOP = sr.getPrePaidMop();
    }
    if("RMB".equals(currency)){
    	paidRMB = sr.getPrePaidMop();
    }
    if("HKD".equals(currency)){
    	paidHKD = sr.getPrePaidMop();
    }
    %>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidMOP);} else {out.print(payMOP);} %></span></td>
  </tr>
  <tr>
    <td colspan="5" align="right">應總繳<span class="number">RMB</span>金額：</td>
    <%
    double payRMB = new BigDecimal(payMOP).multiply(new BigDecimal(rmbRate)).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
    %>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidRMB);} else {out.print(payRMB);} %></span></td>
  </tr>
  <tr>
    <td colspan="5" align="right">應總繳<span class="number">HKD</span>金額：</td>
    <%
    double payHKD = new BigDecimal(payMOP).multiply(new BigDecimal(hkdRate)).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
    %>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidHKD);} else {out.print(payHKD);} %></span></td>
  </tr>
</table>

<br>
會計簽署：<u>____________________</u>　
蓋章：<u>____________________</u>　
收款日期：<u>____________________</u>
<br>
<br>
<p align="center"><strong><u><font size="3">備注：第一聯收據由收款員留存</font></u></strong></p>

<div style="page-break-after:always"></div>

<table width="100%" border="0">
  <tr align="left">
    <td>訂書序號:<%=sr.getOrderSeqno() %></td>
  </tr>
</table>
<p align="center">
  <strong><font size="3">澳門科技大學圖書出版及供應中心</font></strong>
  <br>
  <strong><font size="3"><%=scintake %>學期學生書費收據(第二聯)</font></strong>
</p>
<p align="center">
校園卡號：<u><span class="number"><%=sr.getStudentNo() %></span></u>&nbsp;&nbsp;
學生姓名：<u><span class="number"><%=sr.getChineseName() %>/<%=sr.getEnglishName() %></span></u>&nbsp;&nbsp;
列印日期：<u><span class="number"><%=Datetime %></span></u></p>
<hr>
<p align="left"><u>※第一步：列印領書程式表及領書收據</u></p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;學生必須憑由列印員簽署之<u>學生書費收據（共二聯）</u>到下步驟收款處付款。付款後請妥善保管好由會計人員簽署並蓋章後的收據(第一聯)，開學初領書時必須憑已付款收據領書。並請同學按照收據上的時間、日期領書，否則按『訂書、繳交書費及領書須知』有關規定處以罰金。</p>
<br>
列印員簽名：<u><%=userId %></u>&nbsp;&nbsp;&nbsp;&nbsp;
日期：<u><%=Datetime %></u>
<hr>
<p align="left"><u>※第二步：繳款(請預備零錢並按應繳金額繳付)</u></p>
<table width="98%" class="table" border="1" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td rowspan="2" width="36%" align="center">書名</td>
    <td rowspan="2" width="14%" align="center"><span class="number">ISBN</span></td>
    <td colspan="2" width="12%" align="center">單價</td>
    <td colspan="2" width="11%" align="center">訂書數</td>
    <td width="13%" align="center">總金額</td>
    <td width="5%" align="center">領書</td>
    <td width="6%" align="center" rowspan="2">備注</td>
  </tr>
  <tr>
    <td align="center"><span class="number">預估MOP</span></td>
    <td align="center"><span class="number">實價MOP</span></td>
    <td align="center">確定</td>
    <td align="center">退訂</td>
    <td align="center"><span class="number">MOP</span></td>
    <td align="center">(Y/N)</td>
  </tr>
  <%
  double mop1 = 0;
  for(int i=0; i<receiptList.size(); i++) {
	  StudReceipt studreceipt = (StudReceipt)receiptList.get(i);
	  //判斷書本實價狀況
	  double bookMopPrice = 0;	//書本MOP價錢
	  if(studreceipt.getMopNetPrice() == 0 || studreceipt.getMopNetPrice() == null){
		  bookMopPrice = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
	  }else{
		  bookMopPrice = new BigDecimal(Double.toString(studreceipt.getMopNetPrice())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
	  }
	  if(studreceipt.getPaidCurrency()!=null && "MOP".equals(studreceipt.getPaidCurrency())){
		  bookMopPrice = new BigDecimal(Double.toString(studreceipt.getPaidAmount())).multiply(new BigDecimal(Integer.toString(studreceipt.getConfirmQty()))).doubleValue();
	  }
	  BigDecimal v1 = new BigDecimal(Double.toString(bookMopPrice));
	  mop1 = v1.doubleValue();
  %>
  <tr>
    <td><%=studreceipt.getTitle() %></td>
    <td><span class="number"><%=studreceipt.getIsbn() %></span></td>
    <td align="right"><span class="number">
    <%
    double mopPrice = 0;
    BigDecimal mopFuture = null;
	if(studreceipt.getMopFuturePrice() != 0){
		mopFuture = new BigDecimal(Double.toString(studreceipt.getMopFuturePrice()));
	}else{
		mopFuture = new BigDecimal(0);
	}
	mopPrice = mopFuture.doubleValue();
    if(studreceipt.getPaidCurrency() != null ){
    	mopPrice = studreceipt.getPaidAmount();
    }
    out.print(mopPrice);
    %>
    </span></td>
    <td align="right"><span class="number">
    <%
    double mopNetPrice = 0;
    if(studreceipt.getMopNetPrice() != 0 && (studreceipt.getMopNetPrice() - studreceipt.getMopFuturePrice()) != 0){
    	BigDecimal mopNet1 = new BigDecimal(Double.toString(studreceipt.getMopNetPrice()));
    	mopNetPrice = mopNet1.doubleValue();
    	if(studreceipt.getPaidCurrency() != null ){
    		mopNetPrice = studreceipt.getPaidAmount();
        }
    	out.print(mopNetPrice);
    }
    %>
    </span></td>
    <td align="center"><span class="number"><%=studreceipt.getConfirmQty() %></span></td>
    <td align="center"><span class="number"><%=studreceipt.getWithDrawQty() %></span></td>
    <td align="right"><span class="number"><%=mop1 %></span></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <%
  } 
  %>
  <tr>
    <td rowspan="7">總冊數：<u><span class="number"><%=confirm %></span></u>冊</td>
    <td colspan="5" align="right">書本金額：</td>
    <td align="right"><span class="number"><%out.print(sumMop); %></span></td>
    <td colspan="2" rowspan="7"></td>
  </tr>
  <tr>
    <td colspan="5" align="right">罰款金額：</td>
    <td align="right"><span class="number"><%out.print(new BigDecimal(Double.toString(sr.getAmercemount())).doubleValue()); %></span></td>
  </tr>
  <tr>
    <td colspan="5" align="right">逾期付款罰款金額：</td>
    <td align="right"><span class="number">
    <%
    out.print(amerceMopSum);
    %>
    </span></td>
  </tr>
  <tr>
    <td colspan="5" align="right">補差價：</td>
    <td align="right"><span class="number"><%out.print(differMop); %></span></td>
  </tr>
  <tr>
    <td colspan="5" align="right">應繳總<span class="number">MOP</span>金額：</td>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidMOP);} else {out.print(payMOP);} %></span></td>
  </tr>
  <tr>
    <td colspan="5" align="right">應繳總<span class="number">RMB</span>金額：</td>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidRMB);} else {out.print(payRMB);} %></span></td>
  </tr>
  <tr>
    <td colspan="5" align="right">應繳總<span class="number">HKD</span>金額：</td>
    <td align="right"><span class="number"><% if(currency != null){out.print(paidHKD);} else {out.print(payHKD);} %></span></td>
  </tr>
</table>

<br>會計簽署：<u>____________________</u>　
蓋章：<u>____________________</u>
收款日期：<u>____________________</u>
<hr>
<p><u>※第三步：領書</u></p>
<p>
&nbsp;&nbsp;&nbsp;&nbsp;1．派書員必須認真核實學生的校園卡號，姓名及訂書序號，確實無誤後方可派發書本。<br />
&nbsp;&nbsp;&nbsp;&nbsp;2．請派書員在第二步領書單中領書（Y/N)處填寫，已派書填(Y)，否則填(N)。<br />
&nbsp;&nbsp;&nbsp;&nbsp;3．學生領取書本請現場即時核實，如發現任何問題，請聯絡派書員進行更換或補發。</p>
<br>
<p align="left">派書員簽名：<u>____________________</u>
日期：<u>____________________</u></p>
<br>
<p align="left">
學生簽名：<u>____________________</u>
取書日期：<u>____________________</u>
學生聯系電話：<u>____________________</u></p>

<p align="center"><strong><u><font size="2">備注：第二聯收據由學生留存，學生必須憑本收據於規定時間領書</font></u></strong></p>

<!--endprint-->
</body>
</html>
