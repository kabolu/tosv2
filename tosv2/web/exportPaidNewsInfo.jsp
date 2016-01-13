<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>導出已交款新生名單</title>
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
<h2>導出已交款新生名單</h2>
<form name="searchForm" action="ExportPaidNewsServlet" method="post">
  <input type="hidden" name="oprType" value="export">
  <table width="60%" cellpadding="0" cellspacing="0" border="0">
    <tr>
      <td height="30" width="20%">付款類型：</td>
      <td colspan="3">
        <input type="radio" name="paidStatus" value="Y" style="border:0px;background-color:#F5F2DA;">已付
        <input type="radio" name="paidStatus" value="A" style="border:0px;background-color:#F5F2DA;">所有
      </td>
    </tr>
    <tr>
      <td height="30">到期日：</td>
      <td colspan="3">
        <input name="date" size="10" type="text" class="inp" onClick="new Calendar(null, null, 3).show(this);" readonly="readonly" style="height:18px;"/>
      </td>
    </tr>
    <tr>
      <td height="30">交款期日：</td>
      <td colspan="3">
        <input name="tranDate" size="10" type="text" class="inp" onClick="new Calendar(null, null, 3).show(this);" readonly="readonly" style="height:18px;"/>
        (若選擇該日期，系統不會以申請編號為當前學期為首的條件導出記錄)
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
1、所導出的Excel表內資料都是會計處所登記新生交款名單記錄；
<br>
2、‘已付’為所付金額大於零的記錄，‘所有’包括所付金額為零的記錄；
<br>
3、‘到期日’表示付款日期在該日期之前的新生名單記錄；
<br>
4、‘交款期日’表示學生付款的日期；
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
