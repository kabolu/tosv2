<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.BookStockOutBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>圖書出貨記錄查詢</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
String prNum = null;
if(request.getAttribute("prNum")!=null){
	prNum = (String)request.getAttribute("prNum");
}
List list = null;
if(request.getAttribute("detailList")!=null){
	list = (List)request.getAttribute("detailList");
}
%>
<h2>圖書出貨記錄查詢</h2>
<form name="searchForm" action="BookStockOutServlet" method="post" >
  <input type="hidden" name="type" value="stockOutDetail">
  <table width="60%" cellpadding="0" cellspacing="0" border="0">
    <tr>
      <td height="30" width="15%">出貨單編號：</td>
      <td width="35%">
        <input type="text" class="inp" name="prNum">
      </td>
      <td>
        <input type="submit" name="submit" value="确 定">
      </td>
    </tr>
  </table>
</form>
<%
if(prNum!=null && !"".equals(prNum)){
	if(list!=null && !list.isEmpty()){
%>
<table width="99.9%" cellpadding="0" cellspacing="1" border="0">
  <tr bgcolor="#C6D6FD">
    <th width="6%" height="25">學期</th>
    <th width="15%">出貨單編號</th>
    <th width="9%">出貨日期</th>
    <th width="9%">付款幣種</th>
    <th width="12%">ISBN</th>
    <th width="17%">書名</th>
    <th width="8%">定價</th>
    <th width="8%">折扣</th>
    <th width="8%">出貨價</th>
    <th width="8%">出貨數</th>
  </tr>
  <%
  		for(int i=0; i<list.size(); i++){
  			BookStockOutBean info = (BookStockOutBean)list.get(i);
  %>
  <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td height="25"><%=info.getIntake() %></td>
    <td><%=info.getPrNum() %></td>
    <td><%=info.getOutDate() %></td>
    <td align="center"><%=info.getPaidCurrency() %></td>
    <td align="center"><%=info.getIsbn() %></td>
    <td><%=info.getBook().getTitle() %></td>
    <td align="center"><%=info.getPurchasePrice() %></td>
    <td align="center"><%=info.getDisCount() %></td>
    <td align="center"><%=info.getCostPrice() %></td>
    <td align="center"><%=info.getAdjNum() %></td>
  </tr>
  <%
  		}
  %>
</table>
<%
	}else{
		out.print("沒有任何出貨記錄！");
	}
}else{
	out.print("請輸入出貨單編號查詢！");
}
%>
</body>
</html>
