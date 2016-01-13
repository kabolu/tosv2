<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.BookSupplier"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出貨匯總表和圖書出庫詳細表</title>
<script type="text/javascript" src="js/calendar.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<script type="text/javascript">
function checkForm(){
	if(searchForm.beginDate.value != "" && searchForm.toDate.value != ""){
		var a = document.getElementById("beginDate").value;
		var b = document.getElementById("toDate").value;
		var date1 = new Date(a.replace(/\-/g, "\/"));
		var date2 = new Date(b.replace(/\-/g, "\/"));
		if(Date.parse(date1) - Date.parse(date2)>0){
			alert(a + " 晚於 " + b);
			return false;
		}else{
			return true;
		}
	}else if(searchForm.beginDate.value == "" && searchForm.toDate.value == "" ){
		return true;
	}else{
		alert("請完整填寫出貨日期！");
		return false;
	}
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
<body>
<%
String beginDate = null;
if(request.getAttribute("beginDate") != null){
	beginDate = (String)request.getAttribute("beginDate");
}
String toDate = null;
if(request.getAttribute("toDate") != null){
	toDate = (String)request.getAttribute("toDate");
}
%>
<% if (session.getAttribute("userId") == null) {%>
	<script>
		alert('登陸超時！請重新登陸');
		window.parent.location.href='login.jsp';
    </script>
<% } %>
<%
List<BookSupplier> supplierList = null;
if(request.getAttribute("supplierList") != null){
	supplierList = (List)request.getAttribute("supplierList");
}
String intake = "";
if(session.getAttribute("curIntake") != null){
	intake = (String)session.getAttribute("curIntake");
}
%>
<h2>出貨匯總表和圖書出庫詳細表</h2>
<form name="searchForm" action="ExportBookStockOutServlet" method="post" onSubmit="return checkForm();">
  <input type="hidden" name="type" value="export">
  <table width="60%" cellpadding="0" cellspacing="0" border="0">
    <tr>
      <td>學期：</td>
      <td colspan="3">
        <input type="text" name="intake" maxlength="4" class="inp" value="<%=intake %>">
      </td>
    </tr>
    <tr>
      <td>出貨單標號：</td>
      <td colspan="3">
        <input type="text" name="prnum" maxlength="35" class="inp">
      </td>
    </tr>
    <tr>
      <td height="30">書商：</td>
      <td colspan="3">
        <select name="supplier" id="supplier">
          <option value="">==請選擇==</option>
          <%
          if(supplierList != null && !supplierList.isEmpty()){
        	  for(BookSupplier s : supplierList){
          %>
          <option value="<%=s.getSupplierNo() %>"><%=s.getSupplierName() %></option>
          <%
              }
          }
          %>
        </select>
      </td>
    </tr>
    <tr>
      <td height="30" width="20%">出貨開始日期：</td>
      <td width="25%">
        <input name="beginDate" size="10" type="text" class="inp" id="beginDate" onClick="new Calendar(null, null, 3).show(this);" readonly="readonly"/>
      </td>
      <td width="15%">結束日期：</td>
      <td width="20%">
        <input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" readonly="readonly"/>
      </td>
      <td width="20%">
        <input type="submit" name="submit" value="确 定">&nbsp;&nbsp;
        <input type="reset" name="reset" value="重 置">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
