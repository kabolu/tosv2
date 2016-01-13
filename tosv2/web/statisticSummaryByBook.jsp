<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>圖書訂購信息簡表</title>
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
	if(searchForm.intake.value == ""){
		alert("請輸入學期搜索！");
		searchForm.intake.focus();
		return false;
	}
	var flag1 = true;
	if(searchForm.fromDate.value != "" && searchForm.toDate.value != ""){
		a = document.getElementById("fromDate").value;
		b = document.getElementById("toDate").value;
		var d1 = new Date(a.replace(/\-/g, "\/"));
		var d2 = new Date(b.replace(/\-/g, "\/"));
		if(Date.parse(d1) - Date.parse(d2)>0){
			alert(a+"晚於"+b);
			flag1 = false;
		}else{
			flag1 = true;
		}
	}else if(searchForm.fromDate.value == "" && searchForm.toDate.value == "" ){
		alert("請完整填寫訂書日期由和至！");
		flag1 = false;
	}else{
		alert("請完整填寫訂書日期由和至！");
		flag1 = false;
	}
	var flag2 = true;
	if(searchForm.pFromDate.value != "" && searchForm.pToDate.value != ""){
		c = document.getElementById("pFromDate").value;
		d = document.getElementById("pToDate").value;
		var d3 = new Date(c.replace(/\-/g, "\/"));
		var d4 = new Date(d.replace(/\-/g, "\/"));
		if(Date.parse(d3) - Date.parse(d4)>0){
			alert(c+"晚於"+d);
			flag2 = false;
		}else{
			flag2 = true;
		}
	}else if(searchForm.pFromDate.value == "" && searchForm.pToDate.value == "" ){
		alert("請完整填寫代購日期由和至！");
		flag2 =  false;
	}else{
		alert("請完整填寫代購日期由和至！");
		flag2 =  false;
	}
	return (flag1 && flag2);
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
<%
List timeList = null;
if(request.getAttribute("timeList")!=null){
	timeList = (List)request.getAttribute("timeList");
}
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<h2>圖書訂購信息簡表</h2>
<form name="searchForm" action="StatisticByBookServlet" method="post" onSubmit="return checkForm();">
<input type="hidden" name="type" value="summary">
<table width="60%" cellpadding="0" cellspacing="0" border="0" class="searchTable">
  <tr>
    <td height="30" width="20%">輸入查詢學期：</td>
    <td colspan="3"><input type="text" name="intake" id="width" size="4" maxlength="4"></td>
  </tr>
  <tr>
    <td height="30">訂書日期由：</td>
    <td>
      <input name="fromDate" size="10" type="text" class="inp" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" style="height:18px;" readonly="readonly" />
    </td>
    <td>訂書日期至：</td>
    <td>
      <input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" style="height:18px;" readonly="readonly" />
    </td>
  </tr>
  <tr>
    <td height="30">代購日期由：</td>
    <td>
      <input name="pFromDate" size="10" type="text" class="inp" id="pFromDate" onClick="new Calendar(null, null, 3).show(this);" style="height:18px;" readonly="readonly" />
    </td>
    <td>代購日期至：</td>
    <td>
      <input name="pToDate" size="10" type="text" class="inp" id="pToDate" onClick="new Calendar(null, null, 3).show(this);" style="height:18px;" readonly="readonly" />
    </td>
  </tr>
  <tr>
    <td height="30">付款日期由：</td>
    <td>
      <input name="paidFromDate" size="10" type="text" class="inp" id="paidFromDate" onClick="new Calendar(null, null, 3).show(this);" style="height:18px;" readonly="readonly" />
    </td>
    <td>付款日期至：</td>
    <td>
      <input name="paidToDate" size="10" type="text" class="inp" id="paidToDate" onClick="new Calendar(null, null, 3).show(this);" style="height:18px;" readonly="readonly" />
    </td>
  </tr>
  <tr>
    <td height="30">期初日期：</td>
    <td colspan="3">
      <select name="stockFormDate" id="stockFormDate">
        <option value="">==請選擇==</option>
        <%
        if(timeList != null && !timeList.isEmpty()){
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
    <td colspan="4" height="35">
      <input type="submit" value="提 交">
      &nbsp;&nbsp;
      <input type="reset" value="重 置">
    </td>
  </tr>
</table>
</form>
<div id="remarks">
<p>
備注：
<br>
1、付款日期為訂單作"收費"時所登記的日期時間；
<br/>
2、付款日期可選填，若不選擇付款日期，統計資料則不按此條件過濾；
<br/>
3、剩餘期初數量 = 週期期初數量 - 時間前的訂書數量(付款訂單訂書數) - 時間前的代購數量；
<br/>
<br/>
</p>
</div>
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