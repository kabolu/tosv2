<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>學生選科信息查詢</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<frameset rows="120,*" border="1" frameborder="yes">
  <frame src="FacultyListServlet?type=studEnrolSearch" scrolling="no" name="top"/>
  <frame src="null.jsp"   name="bottom" scrolling="yes" />
</frameset>

<noframes>
<body>
</body>
</noframes>
</html>

