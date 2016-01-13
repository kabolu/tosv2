<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>訂購圖書信息</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
String message = "";
if(request.getAttribute("message") != null){
	message = (String)request.getAttribute("message");
}
%>
<div align="center" class="blackBold14px">
  <p>&nbsp;</p>
  <p>&nbsp;</p>
  <p><%=message %></p>
  <p>&nbsp;</p>
  <p>&nbsp;</p>
  <p>
  <input type="button" name="button" value="返 回" onclick="window.location.href='orderbookindex.jsp?oprType=orderBooks'"/>
  </p>
</div>
</body>
</html>

