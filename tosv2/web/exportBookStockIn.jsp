<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.must.tos.bean.BookSupplier" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>入庫信息表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<style type="text/css">
<!--
#course {
	height: 150px;
	width: 200px;
}
#width{
	height:18px;
	width: 180px;
}
.searchTable{
	margin:5px 0 5px 5px;
}
-->
</style>
<script language="javascript">
function checkForm(){
	if(searchForm.fromDate.value != "" && searchForm.toDate.value != ""){
		a = document.getElementById("fromDate").value;
		b = document.getElementById("toDate").value;
		var d1 = new Date(a.replace(/\-/g, "\/"));
		var d2 = new Date(b.replace(/\-/g, "\/"));
		if(Date.parse(d1) - Date.parse(d2)>0){
			alert(a +" 晚於 "+ b);
			return false;
		}else{
			return true;
		}
	}else if(searchForm.fromDate.value == "" && searchForm.toDate.value == "" ){
		if(searchForm.supplier.value != ""){
			alert("根據書商搜索時需要輸入入貨日期由和至");
			return false;
		}
	}else{
		alert("請完整填寫入貨日期由和至！");
		return false;
	}
	if(searchForm.supplier.value != ""){
		if(searchForm.fromDate.value == "" && searchForm.toDate.value == ""){
			alert("根據書商搜索時需要輸入入貨日期由和至");
			return false;
		}else if(searchForm.fromDate.value != "" || searchForm.toDate.value != ""){
			alert("請完整填寫入貨日期由和至！");
			return false;
		}
	}
	//return false;
}
function onFocus(){
	document.searchForm.prNum.focus();
}
</script>
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body onLoad="onFocus()">
<%
List<BookSupplier> supplierList = null;
if(request.getAttribute("supplierList") != null){
	supplierList = (List)request.getAttribute("supplierList");
}
%>
<h2>入庫信息表</h2>
<form name="searchForm" action="ExportBookStockInServlet" method="post" onSubmit="return checkForm();">
  <input type="hidden" name="type" value="search">
  <table width="60%" cellpadding="0" cellspacing="0" border="0" class="searchTable">
    <tr>
      <td height="30" width="20%">入貨單編號：</td>
      <td colspan="3"><input type="text" name="prNum" id="width" maxlength="25"></td>
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
      <td height="30">付款狀態：</td>
      <td colspan="3">
        <select name="paidStatus" id="paidStatus">
          <option value="">==請選擇==</option>
          <option value="Y"> 已付 </option>
          <option value="N"> 待付 </option>
        </select>
      </td>
    </tr>
    <tr>
      <td height="30">入貨日期由：</td>
      <td>
        <input name="fromDate" size="10" type="text" class="inp" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" readonly="readonly" style="height:18px;"/>
      </td>
      <td>入貨日期至：</td>
      <td>
        <input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" readonly="readonly" style="height:18px;"/>
      </td>
    </tr>
    <tr>
      <td colspan="4" height="35">
        <input type="submit" name="submit" value="提 交">
        &nbsp;&nbsp;
        <input type="reset" name="reset" value="重 置">
      </td>
    </tr>
  </table>
</form>
<%
String flag = (String)request.getAttribute("flag");
if(flag != null && flag.equals("false")){
%>
<script language="javascript">
	alert("沒有該入貨單編號的記錄，請重新搜索！");
	history.back();
</script>
<%
}
%>
</body>
</html>
