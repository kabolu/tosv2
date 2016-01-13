<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*,java.text.*,java.math.BigDecimal" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>補差價收據</title>
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
	margin-top: 15px;
	margin-right: 10px;
	margin-bottom: 15px;
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
<%
List stuDetList = null;
if(request.getAttribute("stuDetList") != null){
	stuDetList = (List)request.getAttribute("stuDetList");
}
Student stu = null;
Program pro = null;
String studNo = "";
if(stuDetList != null) {
	stu = (Student)stuDetList.get(0);
	if(stu!=null){
		if(stu.getStudentNo() != null){
	    	studNo = stu.getStudentNo();
	    }else{
	    	studNo = stu.getApplicantNo();
	    }
	}
	pro = (Program)stuDetList.get(2);
}
String currency = null;
if(request.getAttribute("currency") != null){
	currency = request.getAttribute("currency").toString();
}
double paidAmount = 0;
if(request.getAttribute("paidamount") != null){
	paidAmount = Double.parseDouble(request.getAttribute("paidamount").toString());
}
boolean dealMop = false, dealRmb = false;
if(request.getAttribute("dealMop") != null){
	dealMop = (Boolean)request.getAttribute("dealMop");
}
if(request.getAttribute("dealRmb") != null){
	dealRmb = (Boolean)request.getAttribute("dealRmb");
}
List<Difference> differList = null;
if(request.getAttribute("differList") != null){
	differList = (List)request.getAttribute("differList");
}
List<Transaction> itemFeeList = null;
if(request.getAttribute("itemFeeList") != null){
	itemFeeList = (List)request.getAttribute("itemFeeList");
}
List<Transaction> rePrintFeeList = null;
if(session.getAttribute("rePrintFeeList") != null){
	rePrintFeeList = (List)session.getAttribute("rePrintFeeList");
}

List<Transaction> tranList = null;
if(request.getAttribute("tranList") != null){
	tranList = (List)request.getAttribute("tranList");
}
String curIntake = "";
if(request.getAttribute("curIntake") != null){
	curIntake = request.getAttribute("curIntake").toString();
}
String userId = "";
if(request.getAttribute("userId") != null){
	userId = request.getAttribute("userId").toString();
}
String chauid = "";
if(request.getAttribute("chauid") != null){
	chauid = request.getAttribute("chauid").toString();
}
String upddate = "";
if(request.getAttribute("upddate") != null){
	upddate = request.getAttribute("upddate").toString();
}
Date now = new Date();
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String Datetime = df.format(now);

String isNewStud = (String)request.getAttribute("isNewStud");
String rmbRate = null;
if(request.getAttribute("rmbRate") != null){
	rmbRate = (String)request.getAttribute("rmbRate");
}
String hkdRate = null;
if(request.getAttribute("hkdRate") != null){
	hkdRate = (String)request.getAttribute("hkdRate");
}
String paidCurrency = null;
if(request.getAttribute("paidCurrency") != null){
	paidCurrency = (String)request.getAttribute("paidCurrency");
}
%>
<input id="button" type="button" class="button" name="button" onClick="doPrint()" value="列印收據">
<!--startprint-->
<p align="center">
  <strong><font size="3">澳門科技大學圖書出版及供應中心</font></strong>
  <br>
  <strong><font size="3"><%=curIntake%>學期學生補差價收據(第一聯)</font></strong>
</p>
<p align="center">
<%
String studentName = "";
String programName = "";
if(stu != null && pro != null){
	studentName = stu.getChineseName()!=null?stu.getChineseName():stu.getEnglishName();
	programName = pro.getChineseName();
}
%>
學號:<u><span class="number"><%=studNo %></span></u>&nbsp;&nbsp;&nbsp;&nbsp;
姓名:<u><span class="number"><%=studentName %></span></u>&nbsp;&nbsp;&nbsp;&nbsp;
課程:<u><span class="number"><%=programName %></span></u>
</p>
<table width="90%" class="table" border="0" align="center" cellpadding="0" cellspacing="0">
  <%
  if("Y".equals(isNewStud)){
  %>
  <tr>
    <td colspan="6" height="25"><b style="font-size:14px;">繳交書費：</b><%=paidAmount!=0?"("+currency+") "+paidAmount:"該學員未繳交書費！" %></td>
  </tr>
  <%
  }else{
  %>
  <tr>
    <td colspan="6" height="25"><b style="font-size:14px;">&nbsp;</b></td>
  </tr>
  <%
  }
  %>
  <%
  if(!differList.isEmpty()){
  %>
  <tr>
    <th height="25">訂單編號</th>
    <th>付款幣種</th>
    <th>已付金額</th>
    <th>需付金額</th>
    <th colspan="2">補差價</th>
  </tr>
  <%
  double differMop = 0;
  for(Difference d : differList){
	  differMop = new BigDecimal(Double.toString(d.getDifferenceMop())).add(new BigDecimal(Double.toString(differMop))).doubleValue();
  %>
  <tr>
    <td align="center" height="22"><%=d.getOrderSeqNo() %></td>
    <td align="center"><%=d.getPaidcurrency() %></td>
    <td align="center"><%=d.getPaidAmount() %></td>
    <td align="center"><%=d.getShoulePayAmount() %></td>
    <td align="center" colspan="2"><%=d.getDifference() %></td>
  </tr>
  <%
  }
  if(itemFeeList != null && !itemFeeList.isEmpty()){
	  for(Transaction t : itemFeeList){
  %>
  <tr>
    <td align="center" height="22"><%=t.getOrderSeqNo() %>(<%=t.getPaidMentType() %>)</td>
    <td align="center"><%=t.getPaidCurrency() %></td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center" colspan="2">0.0</td>
  </tr>
  <%
  	  }
  }
  if(rePrintFeeList != null && !rePrintFeeList.isEmpty()){
	  for(Transaction t : rePrintFeeList){
  %>
  <tr>
    <td align="center" height="30"><%=t.getOrderSeqNo() %>(<%=t.getPaidMentType() %>)</td>
    <td align="center"><%=t.getPaidCurrency() %></td>
    <%
    if("Y".equals(t.getStatus())){
    %>
    <td align="center"><%=t.getPay() %></td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center" colspan="2">0.0</td>
    <%
    } else {
    	differMop += t.getPay();
    %>
    <td align="center">0.0</td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center" colspan="2"><%=t.getPay() %></td>
    <%
    }
    %>
  </tr>
  <%
      }
  }
  if(tranList != null && !tranList.isEmpty()){
  %>
  <tr>
    <th height="22">調整項目名稱</th>
    <th>付款幣種</th>
    <th colspan="2">應付金額</th>
    <th colspan="2">備註</th>
  </tr>
  <%
  for(Transaction t : tranList){
	  if("MOP".equals(t.getPaidCurrency())){
		  differMop = new BigDecimal(Double.toString(t.getPay())).add(new BigDecimal(Double.toString(differMop))).doubleValue();
	  } else if ("RMB".equals(t.getPaidCurrency())){
		  differMop = new BigDecimal(Double.toString(t.getPay())).multiply(new BigDecimal(Double.valueOf(rmbRate)))
		  			.add(new BigDecimal(Double.toString(differMop))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	  } else if ("HKD".equals(t.getPaidCurrency())){
		  differMop = new BigDecimal(Double.toString(t.getPay())).multiply(new BigDecimal(Double.valueOf(hkdRate)))
			.add(new BigDecimal(Double.toString(differMop))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	  }
  %>
  <tr>
    <td align="center" height="22"><%=t.getOrderSeqNo()+"("+t.getPaidMentType()+")" %></td>
    <td align="center"><%=t.getPaidCurrency() %></td>
    <td align="center" colspan="2"><%=t.getPay() %></td>
    <td align="center" colspan="2"><%=t.getRemarks() %></td>
  </tr>
  <%
      }
  }
  %>
  <tr>
	  <td height="22" colspan="4">&nbsp;</td>
	  <td colspan="2">
	  <%
	  if(currency == null || "MOP".equals(currency)){
	  %>
	  MOP:
	  <label style="font-size:14px;font-weight:bold;border-bottom:1px solid #000;"><%=differMop %></label>
	  <%	  
	  }
	  %>
	  <%
	  if(currency == null || "RMB".equals(currency)){
	  %>
	  RMB:
	  <label style="font-size:14px;font-weight:bold;border-bottom:1px solid #000;">
	  <%
	  double differRmb = new BigDecimal(differMop).multiply(new BigDecimal(Double.valueOf(rmbRate))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	  out.print(differRmb); 
	  %>
	  </label>
	  <%
	  }
	  %>
	  <%
	  if(currency == null || "HKD".equals(currency)){
	  %>
	  HKD:
	  <label style="font-size:14px;font-weight:bold;border-bottom:1px solid #000;">
	  <%
	  double differHkd = new BigDecimal(differMop).multiply(new BigDecimal(Double.valueOf(hkdRate))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	  out.print(differHkd); 
	  %>
	  </label>
	  <%
	  }
	  %>
	  </td>
	 </tr>
	 <%
 }
 %>
</table>
<p align="center">
收款員名稱：<u><span class="number"><%=chauid %></span></u>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
收款日期：<u><span class="number"><%=upddate %></span></u>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
列印日期：<u><span class="number"><%=Datetime %></span></u>
</p>
<p align="center"><strong><u><font size="3">備注：第一聯收據由收款員留存</font></u></strong></p>

<!-- 第二聯補差價收據 -->
<p style="margin:15px 5px;" align="center">----------------------------------------------------------------------------------</p>

<p align="center">
  <strong><font size="3">澳門科技大學圖書出版及供應中心</font></strong>
  <br>
  <strong><font size="3"><%=curIntake%>學期學生補差價收據(第二聯)</font></strong>
</p>
<p align="center">
學號:<u><span class="number"><%=studNo %></span></u>&nbsp;&nbsp;&nbsp;&nbsp;
姓名:<u><span class="number"><%=studentName %></span></u>&nbsp;&nbsp;&nbsp;&nbsp;
課程:<u><span class="number"><%=programName %></span></u>
</p>
<table width="90%" class="table" border="0" align="center" cellpadding="0" cellspacing="0">
  <%
  if("Y".equals(isNewStud)){
  %>
  <tr>
    <td colspan="6" height="25"><b style="font-size:14px;">繳交書費：</b><%=paidAmount!=0?"("+currency+") "+paidAmount:"該學員未繳交書費！" %></td>
  </tr>
  <%
  }else{
  %>
  <tr>
    <td colspan="6" height="25"><b style="font-size:14px;">&nbsp;</b></td>
  </tr>
  <%
  }
  %>
  <%
  if(!differList.isEmpty()){
  %>
  <tr>
    <th height="25">訂單編號</th>
    <th>付款幣種</th>
    <th>已付金額</th>
    <th>需付金額</th>
    <th colspan="2">補差價</th>
  </tr>
  <%
  double differMop = 0;
  for(Difference d : differList){
	  differMop = new BigDecimal(Double.toString(d.getDifferenceMop())).add(new BigDecimal(Double.toString(differMop))).doubleValue();
  %>
  <tr>
    <td align="center" height="22"><%=d.getOrderSeqNo() %></td>
    <td align="center"><%=d.getPaidcurrency() %></td>
    <td align="center"><%=d.getPaidAmount() %></td>
    <td align="center"><%=d.getShoulePayAmount() %></td>
    <td align="center" colspan="2"><%=d.getDifference() %></td>
  </tr>
  <%
  }
  if(itemFeeList != null && !itemFeeList.isEmpty()){
	  for(Transaction t : itemFeeList){
  %>
  <tr>
    <td align="center" height="22"><%=t.getOrderSeqNo() %>(<%=t.getPaidMentType() %>)</td>
    <td align="center"><%=t.getPaidCurrency() %></td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center" colspan="2">0.0</td>
  </tr>
  <%
      }
  }
  if(rePrintFeeList != null && !rePrintFeeList.isEmpty()){
	  for(Transaction t : rePrintFeeList){
  %>
  <tr>
    <td align="center" height="30"><%=t.getOrderSeqNo() %>(<%=t.getPaidMentType() %>)</td>
    <td align="center"><%=t.getPaidCurrency() %></td>
    <%
    if("Y".equals(t.getStatus())){
    %>
    <td align="center"><%=t.getPay() %></td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center" colspan="2">0.0</td>
    <%
    }else{
    	differMop += t.getPay();
    %>
    <td align="center">0.0</td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center" colspan="2"><%=t.getPay() %></td>
    <%
    }
    %>
  </tr>
  <%
	  }
  }
  if(tranList != null && !tranList.isEmpty()){
  %>
  <tr>
    <th height="22">調整項目名稱</th>
    <th>付款幣種</th>
    <th colspan="2">應付金額</th>
    <th colspan="2">備註</th>
  </tr>
  <%
  for(Transaction t : tranList){
	  if("MOP".equals(t.getPaidCurrency())){
		  differMop = new BigDecimal(Double.toString(t.getPay())).add(new BigDecimal(Double.toString(differMop))).doubleValue();
	  }else if ("RMB".equals(t.getPaidCurrency())){
		  differMop = new BigDecimal(t.getPay()).multiply(new BigDecimal(Double.valueOf(rmbRate)))
		  			.add(new BigDecimal(Double.toString(differMop))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	  }else if ("HKD".equals(t.getPaidCurrency())){
		  differMop = new BigDecimal(t.getPay()).multiply(new BigDecimal(Double.valueOf(hkdRate)))
			.add(new BigDecimal(Double.toString(differMop))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	  }
  %>
  <tr>
    <td align="center" height="22"><%=t.getOrderSeqNo()+"("+t.getPaidMentType()+")" %></td>
    <td align="center"><%=t.getPaidCurrency() %></td>
    <td align="center" colspan="2"><%=t.getPay() %></td>
    <td align="center" colspan="2"><%=t.getRemarks() %></td>
  </tr>
  <%
  }
  }
  %>
  <tr>
    <td height="22" colspan="4">&nbsp;</td>
    <td colspan="2">
      <%
	  if(currency != null && "MOP".equals(currency)){
	  %>
	  MOP:
	  <label style="font-size:14px;font-weight:bold;border-bottom:1px solid #000;"><%=differMop %></label>
	  <%	  
	  }
	  %>
	  <%
	  if(currency != null && "RMB".equals(currency)){
	  %>
	  RMB:
	  <label style="font-size:14px;font-weight:bold;border-bottom:1px solid #000;">
	  <%
	  double differRmb = new BigDecimal(differMop).multiply(new BigDecimal(Double.valueOf(rmbRate))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	  out.print(differRmb);
	  %>
	  </label>
	  <%
	  }
	  %>
	  <%
	  if(currency != null && "HKD".equals(currency)){
	  %>
	  HKD:
	  <label style="font-size:14px;font-weight:bold;border-bottom:1px solid #000;">
	  <%
	  double differHkd = new BigDecimal(differMop).multiply(new BigDecimal(Double.valueOf(hkdRate))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	  out.print(differHkd); 
	  %>
	  </label>
	  <%
	  }
	  %>
    </td>
  </tr>
  <%
 }
 %>
</table>
<p align="center">
收款員名稱：<u><span class="number"><%=chauid %></span></u>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
收款日期：<u><span class="number"><%=upddate %></span></u>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
列印日期：<u><span class="number"><%=Datetime %></span></u>
</p>
<p align="center"><strong><u><font size="3">備注：第二聯收據由學員留存</font></u></strong></p>
<!--endprint-->
</body>
</html>
