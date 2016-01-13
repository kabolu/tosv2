<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>確認刪除圖書資料</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<h2>刪書提示</h2>
<%
String isbn = null;
if(request.getAttribute("isbn") != null){
	isbn = (String)request.getAttribute("isbn");
}
String msg = "";
if(request.getAttribute("msg") != null){
	msg = (String)request.getAttribute("msg");
}
%>
<p align="center" class="blackBold14px"><%=msg %></p>
<form action="DeleteBookServlet" method="post" >
  <div align="center">
    <input type="hidden" name="isbn" value="<%=isbn %>">
    <input type="submit" value="確 定">
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="返 回" onclick="history.back();">
  </div>
</form>
</body>
</html>
