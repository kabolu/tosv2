<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="edu.must.tos.bean.Order"%>
<%@page import="java.util.List;"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>零售圖書</title>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function checkForm(){
	var searchOrderSeqNo = searchForm.orderSeqNo.value;
}
function orderBook() {  
	window.location.href = "RetailBookServlet?type=retailBookSearch&retail=RETAIL";
}
function printReceipt(orderSeqNo){
	document.otherForm.orderSeqNo.value = orderSeqNo;
	document.otherForm.target = "_blank";
	document.otherForm.action = "RetailBookServlet";
	document.otherForm.submit();
}
function payRetail(orderSeqNo){
	window.location.href = "RetailBookServlet?back=searchPage&type=payOrderPage&orderSeqNo="+orderSeqNo;
}
function receiveBook(orderSeqNo){
	$.post(
		"RetailBookServlet",
		{
			type: "checkRetailOrder",
			orderSeqNo: orderSeqNo
		},
		function(result){
			if(result == "N"){
				alert("該圖書訂單並未付款，不能進行此操作！");
				return false;
			}else{
				window.location.href = "RetailBookServlet?back=searchPage&type=receiveBookPage&orderSeqNo="+orderSeqNo;
			}
		}
	)
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
List list = null;
if(session.getAttribute("list") != null){
	list = (List)session.getAttribute("list");
}
%>
<h2>零售圖書</h2>
<form action="RetailBookServlet" method="post" name="searchForm" onSubmit="return checkForm();">
<input type="hidden" name="type" value="searchOrderSeqNo">
<table width="880" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td width="80">訂單序號：</td>
    <td width="150">
      <input type="text" name="orderSeqNo" class="inp">
    </td>
    <td width="80">訂購日期由：</td>
    <td width="100">
      <input name="fromDate" type="text" id="fromDate" class="inp" onClick="new Calendar(null, null, 3).show(this);" size="9" readonly="readonly"/>
    </td>
    <td width="30">至：</td>
    <td width="100">
      <input name="toDate" type="text" id="toDate" class="inp" onClick="new Calendar(null, null, 3).show(this);" size="9" readonly="readonly"/>
    </td>
    <td width="40">
      <input type="submit" name="submit" value=" 搜索 ">
    </td>
    <td width="300" align="left">
      <input type="button" name="otherOrderBooks" value="零售訂書" onClick="orderBook();"/>
    </td>
  </tr>
</table>
</form>
<!-- ================RESULT================ -->
<table width="99.9%" cellpadding="0" cellspacing="1" border="0">
  <tr bgcolor="#C6D6FD">
    <th height="25" width="20%">訂單編號</th>
    <th width="20%">付款狀態</th>
    <th width="20%">付款幣種</th>
    <th width="20%">付款金額</th>
    <th width="20%">操作</th>
  </tr>
  <%
  if(list != null && !list.isEmpty()){
	  for(int i=0; i<list.size(); i++){
		  Order order = (Order)list.get(i);
		  String orderSeqNo = "";
		  String paidStatus = "";
		  String paidCurrency = "";
		  double paidAmount = 0;
		  if(order != null && order.getStudentNo().equals("RETAIL")){
			  orderSeqNo = String.valueOf(order.getOrderSeqNo());
			  paidStatus = order.getPaidStatus();
			  paidCurrency = order.getPaidCurrency();
			  paidAmount = order.getPaidAmount();
			  %>
			  <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
			    <td height="20"><%=orderSeqNo %></td>
			    <td align="center"><%=paidStatus %></td>
			    <td align="center"><%=paidCurrency==null?"N/A":paidCurrency %></td>
			    <td align="center"><%=paidAmount %></td>
			    <td align="center">
			      <input type="button" name="printBtn" value="打印收據" onClick="printReceipt('<%=orderSeqNo %>');">
			      <input type="button" name="payBtn" value="收費" onClick="payRetail('<%=orderSeqNo %>');">
			      <input type="button" name="receiveBtn" value="領書" onClick="receiveBook('<%=orderSeqNo %>');">
			    </td>
			  </tr>
			  <%
			}
		}
	}else{
		%>
		<tr>
		  <td colspan="5">沒有任何資料！</td>
		</tr>
		<%
	}
	%>
	
</table>
<form action="" method="post" name="otherForm">
  <input type="hidden" name="type" value="retailReceipt" />
  <input type="hidden" name="orderSeqNo" value=""/>
</form>
</body>
</html>