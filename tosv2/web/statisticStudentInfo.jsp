<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>學生訂書信息統計</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.searchTable{
	margin:5px 0 5px 5px;
}
-->
</style>
<script type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
function checkForm(){
	if(searchForm.intake.value != "" && searchForm.fromDate.value == "" && searchForm.toDate.value == ""){
		if(isNaN(searchForm.intake.value) == true){
			alert("學期填寫有誤，請檢查！");
			searchForm.intake.focus();
			return false ;
		}else{
			return true;
		}
	}else if(searchForm.intake.value == "" && searchForm.fromDate.value != "" && searchForm.toDate.value != ""){
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
	}else if(searchForm.intake.value == "" && searchForm.fromDate.value == "" && searchForm.toDate.value == ""){
		alert("請選擇查詢條件！");
		return false;
	}else if(searchForm.intake.value != "" && searchForm.fromDate.value != "" && searchForm.toDate.value == ""){
		alert("請選擇結束日期！");
		return false;
	}else if(searchForm.intake.value != "" && searchForm.fromDate.value == "" && searchForm.toDate.value != ""){
		alert("請選擇開始日期！");
		return false;
	}else if(searchForm.intake.value == "" && searchForm.fromDate.value != "" && searchForm.toDate.value == ""){
		alert("請選擇結束日期！");
		return false;
	}else if(searchForm.intake.value == "" && searchForm.fromDate.value == "" && searchForm.toDate.value != ""){
		alert("請選擇開始日期！");
		return false ;
	}else {
		if(isNaN(searchForm.intake.value) == true){
			alert("學期填寫有誤！");
			searchForm.intake.focus();
			return false ;
		}else{
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
		}
	}
}
function onFocus(){
	document.searchForm.intake.focus();
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body onLoad="onFocus()">
<h2>學生訂書信息統計</h2>
<form name="searchForm" action="StatisticStudentInfoServlet" method="post" onSubmit="return checkForm();">
  <table border="0" cellpadding="0" cellspacing="0" class="searchTable" width="60%">
    <tr>
      <td height="30" width="100">輸入查詢學期：</td>
      <td>
        <input size="3" type="text" class="inp" id="intake" name="intake" maxlength="4">
      </td>
    </tr>
    <tr>
      <td height="30">輸入查詢時段：</td>
      <td>
        從：<input name="fromDate" size="10" type="text" class="inp" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
        &nbsp;~&nbsp;
        到：<input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
      </td>
    </tr>
    <tr>
      <td colspan="2" height="50">
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
	alert("沒有符合條件的記錄，請重新搜索！");
	history.back();
	</script>
	<%
}
%>
</body>
</html>
