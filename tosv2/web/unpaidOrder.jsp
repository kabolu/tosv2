<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>取消未付款訂單</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
$(document).ready(function(){
	$('input[@name=submit]').click(function(){
		var intake = $('input[@name=intake]').val();
		if(intake == ""){
			alert("學期不能為空！");
			return false;
		}
		if(intake.length != 4){
			alert("學期值需為四位！");
			return false;
		}
		var endDate = $('input[@name=endDate]').val();
		if(endDate == ""){
			alert("訂購日期不能為空！");
			return false;
		}
		var result  = window.showModalDialog("confirmInfo.jsp","","resizable=no;dialogHeight=160px;dialogWidth=460px;location=no;menubar=no;toolbar=no;status=no");   
		if(result == "undefined"){
			return false;
		}else if(result == "yes"){
			$.post(
				"DelUnpaidOrderServlet",
				{
					oprType: "confirm",
					intake: intake,
					endDate: endDate
				},
				function(result){
					$('#msg').text(result);
					$('#msg').fadeIn();
					setTimeout(function(){
						$('#msg').fadeOut();
					}, 5000);
				}
			)
		}else {
			return false;
		}
	})
})
</script>
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href = 'login.jsp';
</script>
<% } %>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
String curIntake = "";
if(request.getAttribute("curIntake") != null){
	curIntake = request.getAttribute("curIntake").toString();
}
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
String onSaleEndDate = "";
if(request.getAttribute("onSaleEndDate") != null){
	onSaleEndDate = request.getAttribute("onSaleEndDate").toString();
	onSaleEndDate = df.format(df.parse(onSaleEndDate));
}
%>
<h2>取消未付款訂單</h2>
<form action="" method="post" name="delForm">
  <table align="center" cellpadding="0" cellspacing="0" border="0" width="60%">
    <tr>
      <td width="25%" height="30" align="right">學期：</td>
      <td width="25%"><input type="text" name="intake" value="<%=curIntake %>" class="inp" size="4" maxlength="4"></td>
      <td width="25%" align="right">訂購最後一天：</td>
      <td width="25%">
        <input name="endDate" size="10" type="text" class="inp" value="<%=onSaleEndDate %>" id="endDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
      </td>
    </tr>
    <tr>
      <td colspan="4" align="center" height="30">
        <input type="button" name="submit" value="確 定">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" name="reset" value="重 置">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" name="back" value="返 回" onClick="window.location.href='main.jsp'">
      </td>
    </tr>
  </table>
</form>
<p align="center">
<label id="msg" style="font-size:16pt;font-weight:bold;"></label>
</p>
<div id="remarks">
<p>
備注：
<br>
1、操作後，系統會將所選‘訂購最後一天’（該日期當天23:59:59之前），訂單付款狀態是未付款的記錄，包括訂單和細明記錄設置為無效；
<br>
2、學生所選的付款時段和領書時段同樣被設定無效；
</p>
</div>
</body>
</html>