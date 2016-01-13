<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>訂書頁面框架</title>
<% 
if (session.getAttribute("userId") == null ) {
%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
</head>

<frameset rows="260,*" border="1" frameborder="yes"><br />
  <frame src="orderbook.jsp" scrolling="yes" name="toppart" />
  <frame src="selectedBookList.jsp" name="bottom" scrolling="yes"/>
</frameset>

<noframes>
<body>
</body>
</noframes>
</html>

