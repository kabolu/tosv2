<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*,edu.must.tos.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用戶資訊搜索</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.inp{
	height:18px;
	width:130px;
}
.table th{
	background:#F4A460;
	color:#fff;
	line-height:20px;
	height:30px;
}
.table td{
	padding:3px 5px;
	border-bottom:1px solid #B8860B;
	text-align:;
	vertical-align:center;
}
-->
</style>
<script language="javascript">
function page(number){
	document.resultForm.i.value = number;
	document.resultForm.action.value = "SysUserServlet";
	document.resultForm.submit();
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h2>用戶資訊搜索</h2>
<%
List userList = null;
if(request.getAttribute("userList") != null){
	userList = (List)request.getAttribute("userList");
}
SysUser user = null;
if(request.getAttribute("user") != null){
	user = (SysUser)request.getAttribute("user");
}
int number = 0;
if(request.getAttribute("number") != null && !request.getAttribute("number").equals("")){
	number = Integer.parseInt((String)request.getAttribute("number"));
}
int maxPage = 0;
if(request.getAttribute("maxPage") != null && !request.getAttribute("maxPage").equals("")){
	maxPage = Integer.parseInt((String)request.getAttribute("maxPage"));
}
int pageNumber = 0;
if(request.getAttribute("pageNumber") != null && !request.getAttribute("pageNumber").equals("")){
	pageNumber = Integer.parseInt((String)request.getAttribute("pageNumber"));
}
int start = number * 10;
int over = (number + 1) * 10;
int count = pageNumber - over;
if(count <= 0){
	over = pageNumber;
}
%>
<form action="" method="post" name="resultForm">
<%
if(user != null){
	if(user.getUserId() != null && !user.getUserId().equals("")){
%>
  <input type="hidden" name="userId" value="<%=user.getUserId() %>">
<%
	}
	if(user.getUserName() != null && !user.getUserName().equals("")){
%>
  <input type="hidden" name="userName" value="<%=user.getUserName() %>">
<%
	}
	if(user.getDepartment() != null && !user.getDepartment().equals("")){
%>
  <input type="hidden" name="department" value="<%=user.getDepartment() %>">
<%
	}
	if(user.getStatus() != null && !user.getStatus().equals("")){
%>
  <input type="hidden" name="status" value="<%=user.getStatus() %>">
<%
	}
	if(user.getRole() != null && !user.getRole().equals("")){
%>
  <input type="hidden" name="role" value="<%=user.getRole() %>">
<%
	}
}
%>
  <input type="hidden" name="i" value="">
  <input type="hidden" name="type" value="search">
</form>
<form action="SysUserServlet" method="post" name="searchForm">
  <input type="hidden" name="type" value="search">
  <table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
    <tr>
      <td height="30">用戶ID：</td>
      <td><input type="text" name="userId" class="inp"></td>
      <td>用戶名稱：</td>
      <td><input type="text" name="userName" class="inp"></td>
    </tr>
    <tr>
      <td height="30">所在部門：</td>
      <td><input type="text" name="department" class="inp"></td>
      <td>狀態：</td>
      <td>
        <input type="radio" name="status" value="A" style="background-color:#F5F2DA;border:0px;">有效&nbsp;&nbsp;
        <input type="radio" name="status" value="L" style="background-color:#F5F2DA;border:0px;">鎖定&nbsp;&nbsp;
        <input type="radio" name="status" value="N" style="background-color:#F5F2DA;border:0px;">無效
      </td>
    </tr>
    <tr>
      <td height="35" colspan="4">
        <input type="submit" name="search" value="搜 索">&nbsp;&nbsp;
        <input type="reset" name="reset" value="重 置">&nbsp;&nbsp;
        <input type="button" name="back" value="返 回" onClick="window.location.href='SysUserServlet'">
      </td>
    </tr>
  </table>
</form>
<table width="98%" border="0" cellpadding="0" cellspacing="0" class="table" align="center">
  <%
  if(userList != null){
  %>
  <tr>
    <th width="25%">用戶ID</th>
    <th width="25%">用戶名稱</th>
    <th width="15%">所在部門</th>
    <!--
    <th>用戶角色</th>
    -->
    <th width="15%">備注</th>
    <th width="10%">狀態</th>
    <th width="10%">操作</th>
  </tr>
  <%
  for(int i=start; i<over; i++){
	  SysUser userInfo = new SysUser();
	  userInfo = (SysUser)userList.get(i);
  %>
  <tr>
    <td height="25" style="border-left:1px solid #B8860B;"><%=userInfo.getUserId() %></td>
    <td style="border-left:1px solid #B8860B;"><%=ToolsOfString.NulltoEmpty(userInfo.getUserName()) %></td>
    <td style="border-left:1px solid #B8860B;"><%=ToolsOfString.NulltoEmpty(userInfo.getDepartment()) %></td>
    <!--
    <td style="border-left:1px solid #B8860B;"><%=ToolsOfString.NulltoEmpty(userInfo.getRole()) %></td>
    -->
    <td style="border-left:1px solid #B8860B;"><%=ToolsOfString.NulltoEmpty(userInfo.getRemarks()) %></td>
    <td style="border-left:1px solid #B8860B;" align="center">
    <%
    if("N".equals(userInfo.getStatus())){
    	out.print("<font color='red'>無效</font>");
    }else if("L".equals(userInfo.getStatus())){
    	out.print("<font color='blue'>凍結</font>");
    }else{
    	out.print("有效");
    }
    %>
    </td>
    <td style="border-left:1px solid #B8860B;border-right:1px solid #B8860B;" align="center">
      <a href="SysUserServlet?type=modify&userId=<%=userInfo.getUserId() %>">修改</a>
    </td>
  </tr>
  <%
  }
  %>
  <tr>
    <td colspan="6" height="30" align="right" style="border-bottom:0px;">
      共有<%=maxPage %>頁&nbsp;&nbsp;&nbsp;&nbsp;
      共有<%=pageNumber %>條記錄&nbsp;&nbsp;&nbsp;&nbsp;
      當前為第<%=number+1 %>頁&nbsp;&nbsp;&nbsp;&nbsp;
      <a class="prev" href="javascript:page('<%=0 %>')">首頁</a>&nbsp;&nbsp;&nbsp;&nbsp;
      <%if((number+1)==1){ %>
       上一頁
      <%}else{ %>
      <a class="prev" href="javascript:page('<%=number-1 %>')">上一頁</a>
      <%} %>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <%if(maxPage <= (number+1)){ %>
      下一頁
      <%}else{ %>
      <a class="next" href="javascript:page('<%=number+1 %>')">下一頁</a>
      <%} %>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <a class="prev" href="javascript:page('<%=maxPage-1 %>')">尾頁</a>
    </td>
  </tr>
  <%
}
%>
</table>
</body>
</html>
