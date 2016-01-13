<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.ExassLect"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>顯示授課老師</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/tos.js"></script>
</head>
<script language="javascript">
$(document).ready(function(){
	$('input[@name=submitBtn]').click(function(){
		var type = $('input[@name=type]').val();
		var courseCode = $('input[@name=courseCode]').val();
		var lectCode = $('#lectCode').val(); 
		$.ajax({
			url: 'ExassLectServlet',
			type: 'post',
			data:{
				type: type,
				courseCode: courseCode,
				lectCode: lectCode
			},
			success:function(result){
				window.returnValue = result;
        		window.close();
			}
		})		
	})
})
function setFalse(){
	return false;
}
</script>
<body>
<%
List<ExassLect> lectList = null;
if(request.getAttribute("lectList") != null){
	lectList = (List)request.getAttribute("lectList");
}
String courseCode = null;
if(request.getAttribute("courseCode") != null){
	courseCode = (String)request.getAttribute("courseCode");
}
%>
<form action="ExassLectServlet" name="submitForm" onsubmit="return setFalse();">
  <input type="hidden" name="type" value="submitLect">
  <input type="hidden" name="courseCode" value="<%=courseCode %>">
  <table border="0" width="90%" cellpadding="0" cellspacing="0" align="center">
    <tr>
      <td width="40%" align="right" height="50">授課老師：</td>
      <td width="60%" align="left">
        <select name="lectCode" id="lectCode">
          <%
          if(lectList != null){
        	  for(ExassLect lect : lectList){
          %>
          <option value="<%=lect.getLecturer() %>"><%=lect.getLecturer() + " " + lect.getLectChnName() %></option>
          <%
              }
          } else {
          %>
          <option value="">---</option>
          <%
          }
          %>
        </select>
      </td>
    </tr>
    <tr>
      <td colspan="2" align="center">
        <input type="button" name="submitBtn" value=" 提交 ">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" name="closeBtn" value=" 關閉 " onClick="window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>