<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>學院列表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<%
List<Faculty> list = (List)session.getAttribute("facultyList");
%>
<body>
<table width="99.9%"  align="center" border="0" cellspacing="1" cellpadding="0">
  <tr bgcolor="#C6D6FD">
    <td height="35" width="20%" align="center"><strong>學院編號</strong></td>
    <td width="25%" align="center"><strong>學院中文名</strong></td>
    <td width="55%" align="center"><strong>學院英文名</strong></td>
  </tr>
  <%
  if(list != null) {
	  for(Faculty faculty : list){
  %>
  <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td height="25"><%=faculty.getFacultyCode() %></td>
    <td><%=faculty.getChineseName() %></td>
    <td><%=faculty.getEnglishName() %></td>
  </tr>
  <%
	  }
  }
  %>
</table>
</body>
</html>