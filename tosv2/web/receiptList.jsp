<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 1);
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收據列表</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
List<StudReceipt> receiptList = null;
if(request.getAttribute("receiptList") != null){
	receiptList = (List)request.getAttribute("receiptList");
}
String studentNo = null;
if(request.getAttribute("studentNo") != null){
	studentNo = request.getAttribute("studentNo").toString();
}
String applicantNo = null;
if(request.getAttribute("applicantNo") != null){
	applicantNo = request.getAttribute("applicantNo").toString();
}
%>
<table width="90%" cellpadding="0" cellspacing="0" border="0" align="center">
  <tr>
    <th width="40%" height="25">學員編號</th>
    <th width="20%">學員名稱</th>
    <th width="40%">訂單編號</th>
  </tr>
  <%
  if(receiptList != null){
	  for(StudReceipt s : receiptList){
  %>
  <tr>
    <td height="30"><%=s.getStudentNo() %></td>
    <td><%=s.getChineseName() %></td>
    <td align="center">
      <a href="ViewReceiptServlet?orderSeqNo=<%=s.getOrderSeqno() %>&studentNo=<%=studentNo %>&applicantNo=<%=applicantNo %>"><%=s.getOrderSeqno() %></a>
    </td>
  </tr>
  <%
  	  }
  }
  %>
</table>
<p align="center">
  <input type="button" name="close" value="關閉" onclick="window.close();">
</p>
</body>
</html>
