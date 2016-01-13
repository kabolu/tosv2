<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*,edu.must.tos.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用戶信息列表</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.table th{
	background:#F4A460;
	color:#fff;
	line-height:20px;
	height:30px;
}
.table td{
	padding:2px 2px;
	border-bottom:1px solid #B8860B;
	text-align: auto;
	vertical-align:center;
}
-->
</style>
<script language="javascript">
$(document).ready(function(){
	$('.btn_modify').click(function(){
		var len = $('input[@type=checkbox][@checked]').length;
		if(len == 0){
			alert('你還沒有選擇用戶！');
		}else if(len >= 2){
			alert('只能選擇一個用戶！');
		}else {
			var value = $('input[@type=checkbox][@checked]').val();
			window.location.href="SysUserServlet?userId="+value+"&type=modify";
		}		
	})
})
</script>
<%if (session.getAttribute("userId") == null ) {%>
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
<%
List userList = null;
if(request.getAttribute("userList") != null){
	userList = (List)request.getAttribute("userList");
}
int number = Integer.parseInt((String)request.getAttribute("number"));
int maxPage = Integer.parseInt((String)request.getAttribute("maxPage"));
int pageNumber = Integer.parseInt((String)request.getAttribute("pageNumber"));
int start = number * 15;
int over = (number + 1) * 15;
int count = pageNumber - over;
if(count <= 0){
	over = pageNumber;
}
%>
<h2>用戶資訊列表</h2>
<table width="99.9%" cellpadding="0" cellspacing="0" border="0" class="table">
  <tr>
    <th>&nbsp;</th>
    <th>用戶ID</th>
    <th>用戶名稱</th>
    <th>所在部門</th>
    <!--<th>用戶角色</th> -->
    <th>備注</th>
    <th>狀態</th>
  </tr>
  <%
  if(userList == null ){
  %>
  <tr>
    <td colspan="7" height="25">沒有相關用戶信息！</td>
  </tr>
  <%
  }else{
	  for(int i=start; i<over; i++){
		  SysUser user = (SysUser)userList.get(i);
  %>
  <tr>
    <td align="center">
      <input type="checkbox" name="box" value="<%=user.getUserId() %>" class="checkbox">
    </td>
    <td style="border-left:1px solid #B8860B;"><%=user.getUserId() %></td>
    <td style="border-left:1px solid #B8860B;"><%=ToolsOfString.NulltoEmpty(user.getUserName()) %></td>
    <td style="border-left:1px solid #B8860B;"><%=ToolsOfString.NulltoEmpty(user.getDepartment()) %></td>
    <!-- <td style="border-left:1px solid #B8860B;"><%=ToolsOfString.NulltoEmpty(user.getRole()) %></td> -->
    <td style="border-left:1px solid #B8860B;"><%=ToolsOfString.NulltoEmpty(user.getRemarks()) %></td>
    <td style="border-left:1px solid #B8860B;" align="center">
    <%
    if(ToolsOfString.NulltoEmpty(user.getStatus()).equals("A")){
    	out.print("有效");
    }else if(ToolsOfString.NulltoEmpty(user.getStatus()).equals("L")){
    	out.print("<font color='blue'>凍結</font>");
    }else if(ToolsOfString.NulltoEmpty(user.getStatus()).equals("N")){
    	out.print("<font color='red'>無效</font>");
    }else{
    	out.print("&nbsp;");
    }
    %>
    </td>
  </tr>
  <%
      }
  }
  %>
  <tr>
    <td colspan="3">
      <input type="button" value="新 增" onClick="window.location.href='userAdd.jsp'">&nbsp;
      <input type="button" value="修 改" class="btn_modify">&nbsp;
      <input type="button" value="搜 索" onClick="window.location.href='userSearch.jsp'">&nbsp;
    </td>
    <td colspan="3" align="right">共有<%=maxPage %>頁
      &nbsp;&nbsp;&nbsp;&nbsp;共有<%=pageNumber %>條記錄
      &nbsp;&nbsp;&nbsp;&nbsp;當前為第<%=number+1 %>頁
      &nbsp;&nbsp;&nbsp;&nbsp;
	  <a href="SysUserServlet?i=<%=0 %>">首頁</a>
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  <%if((number+1)==1){ %>上一頁
	  <%}else{ %>
	  <a href="SysUserServlet?i=<%=number-1 %>">上一頁</a>
	  <%} %>
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  <%if(maxPage <= (number+1)){ %>下一頁
	  <%}else{ %>
	  <a href="SysUserServlet?i=<%=number+1 %>">下一頁</a>
	  <%} %>
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  <a href="SysUserServlet?i=<%=maxPage-1 %>">尾頁</a>
	</td>
  </tr>
</table>
</body>
</html>