<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>圖書信息查詢 </title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<% if (session.getAttribute("userId") == null) {%>
	<script>
		alert('登陸超時！請重新登陸');
		window.parent.location.href = 'login.jsp';
    </script>
<% } %>

<frameset rows="150,*" border="1" frameborder="yes">
  <frame src="FacultyListServlet?type=bookSearch" scrolling="no" name="top"/>
  <frame src="null.jsp" name="bottom" scrolling="yes"/>
</frameset>

<noframes>
<body>
</body>
</noframes>
</html>

