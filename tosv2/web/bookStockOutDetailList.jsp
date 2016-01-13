<%@ page language="java" import="java.util.List,java.util.*,edu.must.tos.bean.*,edu.must.tos.impl.BookDAOImpl" pageEncoding="UTF-8"%>
<%@page import="java.math.BigDecimal"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>圖書出版及供應中心發書單</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function doPrint(){
	button.style.display = 'none';
	closeButton.style.display = 'none';
	print();
}

function closeWindow(){
	window.close();
}
</script>
</head>
<%
List stockOutList = null;
if(request.getAttribute("stockOutList")!=null){
	stockOutList = (List)request.getAttribute("stockOutList");
}
String paidCurrency = null;
String curIntake = null;
String supplierName = null;
String prNum = null;
String outDate = null;
if(stockOutList != null){
	BookStockOutBean bean = (BookStockOutBean)stockOutList.get(0);
	curIntake = bean.getIntake();
	supplierName = bean.getSupplierName();
	prNum = bean.getPrNum();
	outDate = bean.getOutDate();
	paidCurrency = bean.getPaidCurrency();
}
%>
<body>
<br/>
<h3>
  <p align="center">
    <img src="images/logo.gif" width="60"/><br/><br/>澳  門  科  技 大 學 
  </p>
</h3>
<h5>
  <p align="center">圖書出版及供應中心發書單<br/>
    Macau University of Science and Technology Books Delivery Notes of CBPS
  </p>
</h5>	
<table align="center" cellpadding="5" cellspacing="0">
  <tr>
    <td width="50%">當前學期：<%=curIntake %></td>
    <td width="50%">收貨部門/學院 ：<% if(supplierName != null){ out.print(supplierName); }else{  out.print("--"); }%></td>
  </tr>
  <tr>
    <td width="50%">發貨編號：<% if(prNum != null){ out.print(prNum); }else{ out.print("--"); }%></td>
    <td width="50%">出貨日期：<% if(outDate != null){ out.print(outDate); }else{ out.print("--"); } %></td>
  </tr>
</table>					
<p/>
<table width="99%"  border="1" align="center" cellpadding="5" cellspacing="0">
  <tr bgcolor="#FFCC99">
    <td width="6%"  align="center">序號</td>
    <td width="10%" align="center">代訂購編號</td>
    <td width="14%" align="center">書名</td>
    <td width="10%"  align="center">ISBN</td>
    <td width="10%" align="center">作者</td>
    <td width="10%" align="center">出版社</td>
    <td width="8%"  align="center">出版年</td>
    <td width="8%"  align="center">授課<br/>老師</td>
    <td width="8%"  align="center">學校<br/>價格<br/><%=paidCurrency %></td>
    <td width="8%"  align="center">訂購數</td>
    <td width="8%"  align="center">金額</td>
  </tr>
<%
int totalCount = 0;
double totalCost = 0, cost = 0;

if(stockOutList!=null && !stockOutList.isEmpty()){ //BookStockOutServlet->Purchasing
	for(int i=0; i<stockOutList.size(); i++){
		BookStockOutBean info = (BookStockOutBean)stockOutList.get(i);
		cost = new BigDecimal(Double.toString(info.getCostPrice())).multiply(new BigDecimal(Integer.toString(info.getAdjNum()))).doubleValue(); 
		totalCount += info.getAdjNum();
		totalCost = new BigDecimal(Double.toString(cost)).add(new BigDecimal(Double.toString(totalCost))).doubleValue();
%>
  <tr>
    <td align="center"><% out.print(i+1); %></td>
  	<td align="center"><%=info.getPrNum() %></td>
  	<td align="center"><%=info.getBook().getTitle() %></td>
  	<td align="center"><%=info.getIsbn() %></td>
  	<td align="center"><%=info.getBook().getAuthor() %></td>
  	<td align="center"><%=info.getBook().getPublisher() %></td>
  	<td align="center"><%=info.getBook().getPublishYear() %></td>
  	<td align="center"><%=info.getLectCode() %></td>
  	<td align="center"><%=info.getCostPrice() %></td>
  	<td align="center"><%=info.getAdjNum() %></td>
  	<td align="center"><% out.print(cost); %></td>
  </tr>
<%
	}
}
if(stockOutList != null && !stockOutList.isEmpty()){
%>
  <tr>
    <td colspan="9" align="right">合計：</td>
    <td align="center"><% out.print(totalCount); %></td>
    <td align="center"><% out.print(totalCost); %></td>
  </tr>
</table>
<br/>
<br/>
<table align="center" width="98%" cellpadding="5" cellspacing="5">
  <tr>
    <td align="center" width="50%">簽收：______________________</td>
    <td align="center" width="50%">日期：______________________</td>
  </tr>
</table>	 
<% 
}else{ 
%>
  <tr>
    <td>沒有資料</td>
  </tr>
</table>
<% 
} 
%>
<p align="center" style="margin:6px;">
  <input type="button" name="button" onClick="doPrint()" value="列印收據">
  <input type="button" name="closeButton" onClick="closeWindow();" value="關 閉">
</p>
</body>
</html>