<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>領書框架</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<%
if (session.getAttribute("userId") == null ) {
%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<%}%>
</head>
<%
session.removeAttribute("availableBooklist");
String oprType = request.getParameter("oprType");
 
session.setAttribute("oprType", oprType);
String pageInfo = "null.jsp";
if(request.getAttribute("back") != null && request.getAttribute("back").equals("back")){
	pageInfo = "orderstudentlist.jsp";
}
if(oprType != null  && oprType.equals("orderBooks")){
%>
<frameset rows="100,*" border="1" frameborder="yes" >
  <frame src="FacultyListServlet?type=studentSearch&searchType=orderStudent&oprType=orderBooks" scrolling="no" name="top"/>
  <frame src="null.jsp" name="bottom" scrolling="yes"/>
</frameset>
<%
} else if(oprType != null && oprType.equals("received")) {
%>
<frameset rows="100,*" border="1" frameborder="yes" >
  <frame src="FacultyListServlet?type=studentSearch&searchType=orderStudent&oprType=received" scrolling="no" name="top"/>
  <frame src="<%=pageInfo %>" name="bottom" scrolling="yes"/>
</frameset>
<%
} else if(oprType != null && oprType.equals("search")) {
%>
<frameset rows="100,*" border="1" frameborder="yes" >
  <frame src="FacultyListServlet?type=studentSearch&searchType=orderStudent&oprType=search" scrolling="no" name="top"/>
  <frame src="null.jsp" name="bottom" scrolling="yes"/>
</frameset>
<% 
} else { 
%>
<frameset rows="100,*" border="1" frameborder="yes" >
  <frame src="FacultyListServlet?type=studentSearch&searchType=orderStudent&oprType=received" scrolling="no" name="top"/>
  <frame src="null.jsp" name="bottom" scrolling="yes"/>
</frameset>
<%
}
%>

<noframes>
<body>
</body>
</noframes>
</html>

