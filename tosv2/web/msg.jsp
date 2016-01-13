<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.OrDetail"%>
<%@page import="edu.must.tos.bean.BookPurchasingBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>資訊提示</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
      javascript:window.history.forward(1);
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h2>資訊提示</h2>
<%
String msg = (String)request.getAttribute("msg");
String type = (String)request.getAttribute("type");
%>
<h3 align="center"><%=msg %></h3>
<br>
<%
if(type != null && type.equals("uploadBook")){
%>
<h3 align="center"><a href="uploadBook.jsp" target="_self">返回</a></h3>
<%
} 
else if(type != null && type.equals("uploadPrice")) {
%>
<h3 align="center"><a href="javascript:history.back();" target="_parent">返回</a></h3>
<%
} 
else if (type != null && type.equals("uploadBookTempl")) {
%>
<h3 align="center"> <a href="javascript:history.back();" target="_parent">返回</a></h3>
<%
} 
else if(type != null && type.equals("addBook")) {
%>
<h3 align="center"> <a href="javascript:window.close();" target="_parent">關閉</a></h3>
<%
} 
else if(type != null && type.equals("editBook")) {
%>
<h3 align="center"> <a href="javascript:window.close();" target="_parent">關閉</a></h3>
<%
} 
else if(type != null && type.equals("delBook")) {
%>
<h3 align="center"><a href="book.jsp" target="_parent">返回</a></h3>
<%
} 
else if(type != null && type.equals("AddSysTime")){
%>
<h3 align="center"><a href="SysConfTimeServlet?type=<%="view" %>" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("editSysTime")){
%>
<h3 align="center"><a href="SysConfTimeServlet?type=<%="view" %>" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("uploadFuturePrice")){
%>
<h3 align="center"><a href="uploadFuturePrice.jsp" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("uploadUpdBook")){
%>
<h3 align="center"><a href="uploadUpdBook.jsp" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("sysTime")){
%>
<h3 align="center"><a href="javascript:history.back();" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("addReceiptBookTime")){
%>
 <h3 align="center"><a href="ReceiptBookTimeServlet" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("modifyReceiptBookTime")){ 
%>
 <h3 align="center"><a href="ReceiptBookTimeServlet" target="_self">返回</a></h3>
<%
} 
else if(type != null&& type.equals("delReceiptBookTime")){
%>
 <h3 align="center"><a href="ReceiptBookTimeServlet" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("addUser")){
%>
<h3 align="center"><a href="SysUserServlet" target="_self">返回</a></h3>  
<%
}
else if(type != null && type.equals("modifyUser")){
%>
<h3 align="center"><a href="SysUserServlet" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("uploadNewCourseInfo")){
%>
<h3 align="center"><a href="uploadNewCourseInfo.jsp" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("uploadNewStudOrder")){
%>
<h3 align="center"><a href="uploadNewStudOrderInfo.jsp" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("inventoryMsg")){
%>
<h3 align="center"><a href="BookInventoryServlet?type=cleanSession" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("inventoryRecord")){
%>
<h3 align="center"><a href="ExportBookInventoryServlet" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("stockOut")){
%>
<h3 align="center"><a href="ExportBookStockOutServlet" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("updateStock")){
%>
<h3 align="center"><a href="updateStock.jsp" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("addBookSupplier")){
%>
<h3 align="center"><a href="BookSupplierServlet" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("editBookSupplier")){
%>
<h3 align="center"><a href="BookSupplierServlet" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("AddLoginPageInfo")){
%>
<h3 align="center"><a href="LoginPageInfoServlet" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("shippingFee")){
%>
<h3 align="center"><a href="ShippingFeeServlet" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("BookPurchasing")){
	if(request.getAttribute("issued").equals("M")){
%>
    <h3 align="center"><a href="PurchasingBookServlet" target="_self">返回</a></h3>
<%
	}else{
%>
	<h3 align="center"><a href="PurchasingBookServlet?type=addPage" target="_self">返回</a></h3>
<%
	}
}
else if(type != null && type.equals("uploadTimes")){
%>
<h3 align="center"><a href="ReceiptBookTimeServlet" target="_self">返回</a></h3>
<%
}
else if(type != null && type.equals("changeBook")){
	String changetype = (String)request.getAttribute("changetype");
	List unUpdateList = null;
	
	if(request.getAttribute("unUpdateList") != null){
		unUpdateList = (List)request.getAttribute("unUpdateList");
	}
	if(unUpdateList != null && !unUpdateList.isEmpty() ){
	%>
    <table width="500" cellpadding="0" cellspacing="0" border="1" align="center">
      <%
      if(changetype.equals("S")){
      %>
      <tr>
        <th colspan="4" height="25">以下訂單信息存在與置換圖書的相同的ISBN記錄，系統並不對其置換操作！</th>
      </tr>
      <tr>
        <td height="23" align="center">訂單序號</td>
        <td align="center">學生編號</td>
        <td align="center">ISBN</td>
        <td align="center">書名</td>
      </tr>
      <%
    	  for(int i=0; i<unUpdateList.size(); i++){
    		  OrDetail od = (OrDetail)unUpdateList.get(i);
      %>
      <tr>
        <td height="20"><%=od.getOrderSeqNo() %></td>
        <td><%=od.getStudentNo() %></td>
        <td><%=od.getIsbn() %></td>
        <td><%=od.getBookTitle() %></td>
      </tr>
      <%
    	  }
      } else{
      %>
      <tr>
        <th colspan="4" height="25">以下代購記錄已有出貨記錄，系統並不對其置換操作！</th>
      </tr>
      <tr>
        <td height="23" align="center">代購編號</td>
        <td align="center">ISBN</td>
        <td align="center">圖書名稱</td>
        <td align="center">代購數量</td>
      </tr>
      <%
    	  for(int i=0; i<unUpdateList.size(); i++){
    		  BookPurchasingBean od = (BookPurchasingBean)unUpdateList.get(i);
      %>
      <tr>
        <td height="20"><%=od.getOrderNo() %></td>
        <td><%=od.getBook().getIsbn() %></td>
        <td><%=od.getBook().getTitle() %></td>
        <td><%=od.getBookPurchase().getQuantity() %></td>
      </tr>
      <%
    	  }
      }
      %>
    </table>
    <%
		
	}
	%>
	<h3 align="center"><a href="changeBookIndex.jsp" target="_parent">返回</a></h3>
<%
}

%>
</body>
</html>