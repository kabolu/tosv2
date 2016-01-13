<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<title>學員訂書報表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<%if (session.getAttribute("userId") == null ) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href = 'login.jsp';
</script>
<%} %>
<script type="text/javascript">
function checkData() {
	var studentNo = document.form1.studentNo.value;
	var intake = document.form1.intake.value;
	if(studentNo == "" || studentNo.length == 0) {
		alert("請輸入學號!");
		return false;
	}
	if(intake.length != 4 || isNaN(intake) ) {
		alert("學期格式有誤，請重新輸入!");
		return false;
	}
	return true;
}
function Focus(){
	document.form1.studentNo.focus();
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<%
String curIntake = (String)session.getAttribute("curIntake");
%>
<body onLoad="Focus();">
<h2>學員訂書報表</h2>
<form name="form1" onSubmit="return checkData();" target="_blank" method="post" action="StuOrdBooksReportServlet">
  <table width="620" border="0" cellpadding="0" cellspacing="0" align="center">
    <tr>
      <td colspan="4" align="center">所有：
        <input type="radio" name="paidstatus" value="A"	class="radio">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;已付款：
        <input type="radio" name="paidstatus" value="N" checked="checked" class="radio">
      </td>
    </tr>
    <tr>
      <td width="25%" align="right">學號：</td>
      <td width="25%" align="left">
        <input name="studentNo" type="text" id="studentNo" class="inp">
      </td>
      <td width="10%" align="right">學期：</td>
      <td width="40%" align="left">
        <input name="intake" type="text" id="intake" value="<%=curIntake %>" maxlength="4" size="2" class="inp">
      </td>
    </tr>
    <tr>
      <td colspan="4" align="center">
        <p>
          <input type="submit" name="Submit" value="列印報表">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input type="reset" name="Submit2" value="重 置">
        </p>
      </td>
    </tr>
  </table>
</form>
<div id="remarks">
<p>
備注：
<br>
1、‘所有’-->包括‘已付款’和‘未付款’的書單細明記錄；
<br>
2、打印的書單細明記錄包括訂書數目大於0，並且是未領書狀態的；
<br>
3、若有多個訂書訂單，相同的圖書數目會進行統計一起；
</p>
</div>
</body>
</html>