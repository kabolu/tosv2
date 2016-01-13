<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系統時間設置</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
$(document).ready(function(){
	$('#searchForm').submit(function(){
		var searchIntake = $('#searchIntake').val();
		if(searchIntake.length >= 1 && searchIntake.length != 4){
			alert("查詢時學期為四位，請重新輸入！");
			return false;
		}
	})
})
function edit(intake, i){
	window.location.href = "SysConfTimeServlet?intake="+intake+"&type=editSearch&id="+i;
}
</script>
<%
if (session.getAttribute("userId") == null ) {
%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href = 'login.jsp';
</script>
<%}%>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h2>系統時間設置</h2>
<form action="SysConfTimeServlet" method="post" name="searchForm" id="searchForm">
  &nbsp;&nbsp;請輸入學期：
  <input type="text" class="inp" size="2" name="searchIntake" id="searchIntake" maxlength="4"/>&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="hidden" name="type" value="search"/>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="submit" value="搜 索"/>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" name="addbtn" value="添 加" onclick="window.location.href='SysConfTimeServlet?type=add'"/>
</form>
<p>
<%
String type = (String)request.getAttribute("type");

List timeList = new ArrayList();
if(type != null && type.equals("searchResult")){
	timeList = (List)request.getAttribute("timeList");	
	if(timeList != null && !timeList.isEmpty()){
		String searchIntake = (String)request.getAttribute("searchIntake");
%>
<table cellpadding="0" cellspacing="0" border="0" width="700">
  <tr>
    <td colspan="4">搜索學期為：<%=searchIntake %></td>
  </tr>
  <%
  for(int i=0; i<timeList.size(); i++){
	  SysConfig sc = (SysConfig)timeList.get(i);
  %>
  <tr>
	<td width="100" height="30"><%=sc.getScChnDesc() %></td>
	<td width="250">開始時間：<%=sc.getScValue1() %></td>
	<td width="250">結束時間：<%=sc.getScValue2() %></td>
	<td width="100" align="center">
	  <input type="button" name="editbtn" value="編 輯" onclick="edit('<%=searchIntake %>', <%=i %>);"/>
	</td>
  </tr>
  <%
  }
  %>
</table>
<%
	}else{
		out.print("沒有該學期的系統時間，請重新搜索！");
	}
}else{
	timeList = (List)request.getAttribute("timeList");
	String intake = (String)session.getAttribute("curIntake");
	if(timeList == null && timeList.isEmpty()){
		out.print("當前系統時間還沒設置！");
	}else{
%>
<table cellpadding="0" cellspacing="0" border="0" width="700">
  <tr>
    <td colspan="4">當前學期為：<%=intake %></td>
  </tr>
  <%
  for(int i=0; i<timeList.size();i++){
	  SysConfig sc = (SysConfig)timeList.get(i);
  %>
  <tr>
    <td width="100" height="30"><%=sc.getScChnDesc() %></td>
    <td width="250">開始時間：<%=sc.getScValue1() %></td>
    <td width="250">結束時間：<%=sc.getScValue2() %></td>
    <td width="100" align="center">
      <input type="button" name="editbtn" value="編 輯" onclick="edit('<%=intake %>', <%=i %>);"/>
    </td>
  </tr>
  <%
  }
  %>
</table>
		<%
	}
}
%>
</body>
</html>
