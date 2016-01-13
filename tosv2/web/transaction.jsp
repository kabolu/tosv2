<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易項目資訊</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
function checkForm1(){
	if(searchForm1.fromDate.value == "" && searchForm1.toDate.value == ""){
		alert("請輸入開始時間和結束時間！");
		return false;
	}else if(searchForm1.fromDate.value != "" && searchForm1.toDate.value != ""){
		a = searchForm1.fromDate.value;
		b = searchForm1.toDate.value;
		var d1 = new Date(a.replace(/\-/g, "\/"));
		var d2 = new Date(b.replace(/\-/g, "\/"));
		if(Date.parse(d1) - Date.parse(d2)>0){
			alert(a+"晚於"+b);
			return false;
		}else{
			return true;
		}
	}else {
		alert("請完整填寫開始時間和結束時間！");
		return false;
	}
}
function checkForm2(){
	if(searchForm2.fromDate.value == "" && searchForm2.toDate.value == ""){
		alert("請輸入開始時間和結束時間！");
		return false;
	}else if(searchForm2.fromDate.value != "" && searchForm2.toDate.value != ""){
		a = searchForm2.fromDate.value;
		b = searchForm2.toDate.value;
		var d1 = new Date(a.replace(/\-/g, "\/"));
		var d2 = new Date(b.replace(/\-/g, "\/"));
		if(Date.parse(d1) - Date.parse(d2)>0){
			alert(a+"晚於"+b);
			return false;
		}else{
			return true;
		}
	}else {
		alert("請完整填寫開始時間和結束時間！");
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
<h2>交易項目資訊導出</h2>
<form name="searchForm1" action="TransactionServlet" method="post" onSubmit="return checkForm1();">
  <input type="hidden" name="type" value="search">
  <table border="0" cellpadding="0" cellspacing="0" width="60%">
    <tr>
      <td height="30">輸入查詢時間段：</td>
      <td>從：
        <input name="fromDate" size="10" type="text" class="inp" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
        &nbsp;~&nbsp;到：
        <input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
      </td>
    </tr>
    <tr>
      <td colspan="2" height="50">
        <input type="submit" value="提 交">
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" value="重 置">
        &nbsp;&nbsp;&nbsp;&nbsp; 注：交易項目為收費時，才顯示訂書冊數和退訂冊數！
      </td>
    </tr>
  </table>
</form>

<br/>
<h2>學生收費滙總</h2>
<form name="searchForm2" action="TransactionServlet" method="post" onSubmit="return checkForm2();">
  <input type="hidden" name="type" value="studPaySumm">
  <table border="0" cellpadding="0" cellspacing="0" width="60%">
    <tr>
      <td height="30">輸入查詢時間段：</td>
      <td>從：
        <input name="fromDate" size="10" type="text" class="inp" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
        &nbsp;~&nbsp;到：
        <input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
      </td>
    </tr>
    <tr>
      <td colspan="2" height="50">
        <input type="submit" value="提 交">
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" value="重 置">
      </td>
    </tr>
  </table>
</form>

<br/>
<h2>書單收費匯總</h2>
<form name="searchForm3" action="TransactionServlet" method="post" >
  <input type="hidden" name="type" value="orderPaySumm">
  <table border="0" cellpadding="0" cellspacing="0" width="60%">
    <tr>
      <td colspan="2" height="50">
        <input type="submit" value=" 書單收費匯總 ">
      </td>
    </tr>
  </table>
</form>
<%
String flag = (String)request.getAttribute("flag");
if(flag != null && flag.equals("false")){
	%>
	<script language="javascript">
	alert("選擇的時間段內沒有符合的記錄，請重新搜索！");
	history.back();
	</script>
	<%
}
%>
</body>
</html>