<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New Student Order Info</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
      javascript:window.history.forward(1);
</script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
String type = null;
if(request.getAttribute("type") != null){
	type = (String)request.getAttribute("type");
}
String msg = "";
if(request.getAttribute("msg") != null){
	msg = (String)request.getAttribute("msg");
}
String error = "";
if(request.getAttribute("error") != null){
	error = (String)request.getAttribute("error");
}
List<String> errorList = null;
if(request.getAttribute("errorList") != null){
	errorList = (List)request.getAttribute("errorList");
}
%>
<br><br><br><br><br>
<h3 align="center"><%=msg %></h3><br><%=error %><br>
<%
if(errorList != null && errorList.size() != 0){
	out.print("以下新生資料，系統並沒有作處理：");
	out.print("<br>");
	for(String errStr : errorList){
		out.print("&nbsp;&nbsp;&nbsp;&nbsp;-->> "+errStr);
		out.print("<br>");
	}
}
%>
<h3 align="center"><a href="uploadNewStudOrderInfo.jsp" target="_self">返回</a></h3>
</body>
</html>
