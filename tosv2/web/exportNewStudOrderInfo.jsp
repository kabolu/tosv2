<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新生訂書資訊導出</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>

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
<body>
<%
String intake = "";
if(session.getAttribute("curIntake")!=null){
	intake = (String)session.getAttribute("curIntake");
}
%>
<h2>新生訂書資訊導出</h2>
<form name="searchForm" action="ExportNewStudOrderInfoServlet" method="post">
  <input type="hidden" name="oprType" value="export">
  <table width="60%" cellpadding="0" cellspacing="0" border="0" >
    <tr>
      <td height="30" width="17%">當前學期：</td>
      <td colspan="3" width="83%">
        <%=intake %>(若學期值不對，請到系統管理先作當前學期值的設置)
      </td>
    </tr>
    <tr>
      <td height="30">類型：</td>
      <td colspan="3">
        <input type="radio" name="exportType" value="A" checked="checked" style="border:0px;background-color:#F5F2DA;">導出新生訂書資訊
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="radio" name="exportType" value="B" checked="checked" style="border:0px;background-color:#F5F2DA;">導出新生訂書資訊 + 會計收款資訊
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
<div id="remarks">
<p>
備注：
<br>
1、‘導出新生訂書資訊’根據學生選課資料組合出新生訂書的詳細記錄；
<br>
2、‘導出新生訂書資訊 + 會計收款資訊’根據學生選課資料組合出新生訂書記錄，同時包括新生在會計處所付款的記錄；
<br>
3、在導出的Excel表內，‘訂書日期’欄表示該新生訂書記錄的創建時間，若有時間顯示則表示已存在於系統內，若無時間，則表示該新生訂書記錄未進行導入創建新生訂單；
<br>
4、所導出的Excel表內資料都是未在‘補差價’功能內操作過的記錄；
<br>
</p>
</div>
<%
String flag = (String)request.getAttribute("flag");
if(flag != null && flag.equals("false")){
	%>
	<script language="javascript">
	alert("沒有該搜索條件的記錄，請重新搜索！");
	history.back();
	</script>
	<%
}
%>
</body>
</html>
