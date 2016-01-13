<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="edu.must.tos.bean.*,edu.must.tos.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改用戶資訊</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.inp{
	height:18px;
	width:120px;
}
-->
</style>
<script language="javascript">
function countlen(){
	if(document.modifyForm.remarks.value.length>50){
		alert("字數已經超過！！");
		document.modifyForm.remarks.value = document.modifyForm.remarks.value.substring(0, 50);
	}
	return true;
}

function checkForm(){
	//var password = document.modifyForm.password.value;
	//if(password == ""){
	//	alert("用戶密碼不能設置為空！");
	//	document.modifyForm.password.focus();
	//	return false;
	//}
}
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
SysUser user = null;
if(request.getAttribute("user") != null){
	user = (SysUser)request.getAttribute("user");
}
%>
<h2>修改用戶資訊</h2>
<form action="SysUserServlet" method="post" name="modifyForm" onSubmit="return checkForm();">
  <input type="hidden" name="type" value="update">
  <table width="90%" cellpadding="0" cellspacing="0" border="0" align="center">
    <tr>
      <th colspan="2" align="left" height="45">--請修改以下用戶資訊--</th>
    </tr>
    <tr>
      <td height="35">用戶ID：</td>
      <td><%=user.getUserId() %><input type="hidden" name="userId" id="userId" value="<%=user.getUserId() %>"></td>
    </tr>
    <!-- 
    <tr>
      <td height="35">用戶密碼：</td>
      <td>
        <input type="password" name="password" id="password" class="inp" value="<%=user.getPasswd() %>" maxlength="150">
      </td>
    </tr>
     -->
    <tr>
      <td height="35">用戶名稱：</td>
      <td>
        <input type="text" name="userName" id="userName" class="inp" value="<%=ToolsOfString.NulltoEmpty(user.getUserName()) %>" maxlength="20">
      </td>
    </tr>
    <tr>
      <td height="35">電郵地址：</td>
      <td>
        <input type="text" name="email" id="email" class="inp" value="<%=ToolsOfString.NulltoEmpty(user.getEmail()) %>" maxlength="35" style="width:200px;">
      </td>
    </tr>
    <tr>
      <td height="35">聯絡電話：</td>
      <td>
        <input type="text" name="contactNo" id="contactNo" class="inp" value="<%=ToolsOfString.NulltoEmpty(user.getContactNo()) %>" maxlength="15">
      </td>
    </tr>
    <tr>
      <td height="35">傳真號碼：</td>
      <td>
        <input type="text" name="faxNo" id="faxNo" class="inp" value="<%=ToolsOfString.NulltoEmpty(user.getFaxNo()) %>" maxlength="15">
      </td>
    </tr>
    <tr>
      <td height="35">所在地址：</td>
      <td>
        <input type="text" name="address" id="address" class="inp" value="<%=ToolsOfString.NulltoEmpty(user.getAddress()) %>" maxlength="60" style="width:250px;">
      </td>
    </tr>
    <tr>
      <td height="35">所在部門：</td>
      <td>
        <input type="text" name="department" id="department" class="inp" value="<%=ToolsOfString.NulltoEmpty(user.getDepartment()) %>" maxlength="20">
      </td>
    </tr>
    <tr>
      <td height="35">狀態：</td>
      <td>
        <input type="radio" name="status" value="A" <%if(user.getStatus()!=null && user.getStatus().equals("A")){out.print("checked");} %> style="background-color:#F5F2DA;border:0px;">有效&nbsp;&nbsp;
        <input type="radio" name="status" value="L" <%if(user.getStatus()!=null && user.getStatus().equals("L")){out.print("checked");} %> style="background-color:#F5F2DA;border:0px;">凍結&nbsp;&nbsp;
        <input type="radio" name="status" value="N" <%if(user.getStatus()!=null && user.getStatus().equals("N")){out.print("checked");} %> style="background-color:#F5F2DA;border:0px;">無效
      </td>
    </tr>
    <!--
    <tr>
      <td height="35">用戶角色：</td>
      <td>
        <select>
          <option value="0">==請選擇==</option>
          <option value="1">user</option>
          <option value="2">other</option>
          <option value="3">admin</option>
        </select>
      </td>
    </tr>
    -->
    <tr>
      <td height="35">備注：</td>
      <td>
        <textarea rows="2" cols="50" id="remarks" name="remarks" onKeyUp="countlen()"><%=ToolsOfString.NulltoEmpty(user.getRemarks()) %></textarea>
      </td>
    </tr>
    <tr>
      <td height="45" colspan="2">
        <input type="submit" name="submit" value="提 交">&nbsp;&nbsp;
        <input type="reset" name="reset" value="重 置">&nbsp;&nbsp;
        <input type="button" name="back" value="返 回" onClick="javascript:history.back();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
