<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>訂單價格詳情</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
table th{
	height: 25px;
}
table td{
	height: 20px;
}
</style>
<script language="javascript">
function closeWindow() {
	window.close();
}
</script>
</head>
<%
List<OrderDetailPrice> list = null;
if(request.getAttribute("list") != null){
	list = (List)request.getAttribute("list");
}
%>
<body>
<h3 align="center">價格詳細信息</h3>
<table width="550" border="1" align="center" cellpadding="1" cellspacing="0">
<%
if(list != null && !list.isEmpty() ) {
%>
  <tr>
    <th width="23%">ISBN</th>
    <th width="37%">圖書書名</th>
    <th width="10%">訂書數</th>
    <th width="10%">預估價</th>
    <th width="10%">實價</th>
    <th width="10%">已款價</th>
  </tr>
  <%
	for(OrderDetailPrice odp : list){
  %>
  <tr>
    <td><%=odp.getIsbn() %></td>
    <td><%=odp.getTitle() %></td>
    <td align="center"><%=odp.getConfirmqty() %></td>
    <td align="center"><%=odp.getFutureprice() %></td>
    <td align="center">
    <% 
    	if(odp.getNetprice()==0 || odp.getNetprice()==odp.getFutureprice()){
    		out.print("");
    	}else{
    		out.print(odp.getNetprice());
    	}
    %></td>
    <td align="center"><%=odp.getPaidamount() %></td>
  </tr>
  <%
	}
} 
%>
</table>
<p align="center" style="margin:6px;">
  <input type="button" name="close" onClick="closeWindow();" value="關  閉">
</p>
</body>
</html>