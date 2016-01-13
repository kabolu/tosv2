<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代購記錄表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<style type="text/css">
<!--
#width{
	height:18px;
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
			alert(a+"晚於"+b);
			return false;
		}else{
			return true;
		}
	}else if(searchForm.fromDate.value == "" && searchForm.toDate.value == "" ){
		return true;
	}else{
		alert("請完整填寫查詢日期由和至！");
		return false;
	}
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h2>代購記錄表</h2>
<form name="searchForm" action="PurchasingBookServlet" method="post" onSubmit="return checkForm();">
<input type="hidden" name="type" value="report">
<table width="60%" cellpadding="0" cellspacing="0" border="0" class="searchTable">
  <tr>
    <td height="30" width="100">查詢日期由：</td>
    <td>
      <input name="fromDate" size="10" type="text" class="inp" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" style="height:18px;" readonly="readonly" />
    </td>
    <td width="100">查詢日期至：</td>
    <td>
      <input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" style="height:18px;" readonly="readonly" />
    </td>
  </tr>
  <tr>
    <td colspan="4" height="35">
      <input type="submit" value="提 交">
      &nbsp;&nbsp;
      <input type="reset" value="重 置">
    </td>
  </tr>
</table>
</form>
<%
String flag = (String)request.getAttribute("flag");
if(flag != null && flag.equals("false")){
	%>
	<script language="javascript">
	alert("沒有該學期的記錄，請重新搜索！");
	history.back();
	</script>
	<%
}
%>
</body>
</html>