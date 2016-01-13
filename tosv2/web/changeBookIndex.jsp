<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>置換圖書框架</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
</head>
<frameset rows="280,*" border="1" frameborder="no"  >
  <frame src="changeBookSearch.jsp" scrolling="no" name="top"/>
  <frame src="null.jsp" name="bottom" scrolling="yes"/>
</frameset>

<noframes>
<body>
</body>
</noframes>
</html>

