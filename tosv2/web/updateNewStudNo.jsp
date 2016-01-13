<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>更新新生學號</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
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
<h2>更新新生訂書學號</h2>
<%
String intake = (String)session.getAttribute("curIntake");
String msg = "";
if(request.getAttribute("msg") != null){
	msg = request.getAttribute("msg").toString();
}
%>
<div align="center">
  <form action="UpdateNewStudNoServlet" method="post" name="updateForm">
    <input type="hidden" name="updIntake" value="<%=intake %>">
    <input type="submit" name="button" value="更 新">
  </form>
  <br>
  <br>
  <b><%=msg %></b>
</div>
</body>
</html>
