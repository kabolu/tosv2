<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="edu.must.tos.bean.Book"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.BookInventory"%>
<%@page import="edu.must.tos.bean.BookStockInBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>圖書出入貨詳情</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
String isbn = "";
String title = "";
if(request.getAttribute("book") != null){
	Book book = (Book)request.getAttribute("book");
	if(book.getIsbn() != null){
		isbn = book.getIsbn();
	}
	if(book.getTitle() != null){
		title = book.getTitle();
	}
}
List stockInList = null;
if(request.getAttribute("stockInList") != null){
	stockInList = (List)request.getAttribute("stockInList");
}
int stockQty = 0;
if(request.getAttribute("bookInventory") != null){
	BookInventory bookInventory = (BookInventory)request.getAttribute("bookInventory");
	if(bookInventory != null){
		stockQty = bookInventory.getStock();
	}
}
int receiveQty = 0;
if(request.getAttribute("receiveQty") != null){
	receiveQty = Integer.parseInt(request.getAttribute("receiveQty").toString());
}
int retailQty = 0;
if(request.getAttribute("retailQty") != null){
	retailQty = Integer.parseInt(request.getAttribute("retailQty").toString());
}
int stockOutQty = 0;
if(request.getAttribute("stockOutQty") != null){
	stockOutQty = Integer.parseInt(request.getAttribute("stockOutQty").toString());
}
String bookFlag = null;
if(request.getAttribute("bookFlag") != null){
	bookFlag = (String)request.getAttribute("bookFlag");
}
%>
<h2>圖書出入貨詳情</h2>
<form name="searchForm" action="BookInventoryServlet" method="post" >
  <input type="hidden" name="type" value="stockDetail">
  <table width="60%" cellpadding="0" cellspacing="0" border="0" class="searchTable">
    <tr>
      <td height="30" width="15%">圖書ISBN：</td>
      <td width="35%">
        <input type="text" class="inp" name="isbn">
      </td>
      <td>
        <input type="submit" name="submit" value="确 定">
      </td>
    </tr>
  </table>
</form>
<%
if(bookFlag != null && bookFlag.equals("true")){
%>
<table width="99.9%" cellpadding="0" cellspacing="1" border="0">
  <tr bgcolor="#C6D6FD">
    <th height="25" width="15%">圖書ISBN</th>
    <th width="25%">書名</th>
    <th width="15%">當前庫存量</th>
    <th width="15%">學生已領書數</th>
    <th width="15%">零售數</th>
    <th width="15%">出庫數</th>
  </tr>
  <tr bgcolor="#FFFBEF">
    <td height="25"><%=isbn %></td>
    <td><%=title %></td>
    <td align="center"><%=stockQty %></td>
    <td align="center"><%=receiveQty %></td>
    <td align="center"><%=retailQty %></td>
    <td align="center"><%=stockOutQty %></td>
  </tr>
</table>
<table width="99.9%" cellpadding="0" cellspacing="1" border="0">
  <%
  if(stockInList != null && !stockInList.isEmpty()){
  %>
  <tr bgcolor="#C6D6FD">
    <th height="25" width="10%">學期</th>
    <th width="20%">入貨單編號</th>
    <th width="10%">入貨日期</th>
    <th width="10%">付款狀態</th>
    <th width="10%">付款幣種</th>
    <th width="10%">入貨數</th>
    <th width="10%">定價</th>
    <th width="10%">折扣</th>
    <th width="10%">入貨價</th>
  </tr>
  <%
  	  for(int i=0; i<stockInList.size(); i++){
  		  BookStockInBean in = (BookStockInBean)stockInList.get(i);
  		  String paidStatus  = "未付";
  		  String paidCurrency = "N/A";
  		  if(in.getPaidStatus() != null && "Y".equals(in.getPaidStatus())){
  			  paidStatus  = "已付";
  			  paidCurrency = in.getPaidCurrency();
  		  }
  %>
  <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td height="25"><%=in.getIntake() %></td>
    <td><%=in.getPrNum() %></td>
    <td><%=in.getIndate() %></td>
    <td align="center"><%=paidStatus %></td>
    <td align="center"><%=paidCurrency %></td>
    <td align="center"><%=in.getAdjNum() %></td>
    <td align="center"><%=in.getPurchasePrice() %></td>
    <td align="center"><%=in.getDiscount() %></td>
    <td align="center"><%=in.getCostPrice() %></td>
  </tr>
  <%
  	  }
  }else{
  %>
  <tr>
    <td colspan="5">
      <b>沒有該圖書的入貨記錄！</b>
    </td>
  </tr>
  <%
  }
  %>
</table>
<%
}
%>
</body>
</html>
