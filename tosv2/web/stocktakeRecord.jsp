<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*,java.text.*"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>盤點資料記錄查詢</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.searchTable{
	margin:15px 0 0 0;
}
-->
</style>
<script type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
function checkForm(){
	if(searchForm.fromDate.value == "" && searchForm.toDate.value == ""){
		alert("請選擇日期時間！");
		return false;
	}else if(searchForm.fromDate.value != "" && searchForm.toDate.value != ""){
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
	}else {
		alert("請選擇日期時間！");
		return false;
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
<body>
<%
List timeList = null;
if(request.getAttribute("timeList")!=null){
	timeList = (List)request.getAttribute("timeList");
}
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<h2>盤點資料記錄查詢</h2>
<form name="searchForm" action="ExportStocktakeServlet" method="post" onsubmit="return checkForm();">
<input name="type" type="hidden" value="export" />
<table border="0" cellpadding="0" cellspacing="0" class="searchTable" width="90%" align="center">
  <tr>
    <td height="30">輸入查詢日期時段：</td>
    <td>開始日期：
      <select name="fromDate" id="fromDate">
        <option value="">==請選擇==</option>
        <%
        if(!timeList.isEmpty()){
        	for(int i=0;i<timeList.size();i++){
        		String time = df.format(timeList.get(i));
        %>
        <option value="<%=time %>"><%=time %></option>
        <%
        	}
        }
        %>
      </select>
      &nbsp;~&nbsp;結束日期：
      <select name="toDate" id="toDate">
        <option value="">==請選擇==</option>
        <%
        if(!timeList.isEmpty()){
        	for(int i=0;i<timeList.size();i++){
        		String time = df.format(timeList.get(i));
        %>
        <option value="<%=time %>"><%=time %></option>
        <%
        	}
        }
        %>
      </select>
    </td>
  </tr>
  <tr>
    <td colspan="2" height="50" align="center">
      <input type="submit" value="提 交" />
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="reset" value="重 置" />
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