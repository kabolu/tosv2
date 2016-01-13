<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>新增用戶信息</title>
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
	if(document.addForm.remarks.value.length > 50){
		alert("字數已經超過！！");
		document.addForm.remarks.value = document.addForm.remarks.value.substring(0, 50);
	}
	return true;
}

function checkForm(){
	var userId = document.addForm.userId.value;
	if(userId == ""){
		alert("請輸入用戶ID！");
		document.addForm.userId.focus();
		return false;
	}
	//var password = document.addForm.password.value;
	//if(password == ""){
	//	alert("用戶密碼不能設置為空！");
	//	document.addForm.password.focus();
	//	return false;
	//}
}

$(document).ready(function(){
	$('#userId').focus();
	
	$('#userId').bind("blur", function(){
		var value = $(this).val();
		if(value != ""){
			$.post(
				"SysUserServlet",
				{
					type: "checkUserId",
					value: value
				},
				function(result){
					if(result == 1){
						$('.msg').text("該用戶ID已經存在，請修改！");
						$('#userId').select();
					}else{
						$('.msg').hide();
					}
				}
			)
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
<h2>新增用戶資訊</h2>
<form action="SysUserServlet" method="post" name="addForm" id="addForm" onSubmit="return checkForm();">
  <input type="hidden" name="type" value="add">
  <table width="90%" cellpadding="0" cellspacing="0" border="0" align="center">
    <tr>
      <th colspan="2" align="left" height="45">--請填寫以下用戶資訊--</th>
    </tr>
    <tr>
      <td height="35">用戶ID：</td>
      <td>
        <input type="text" name="userId" id="userId" class="inp" maxlength="20">
        &nbsp;&nbsp;
        <font color="red"><label class="msg"></label></font>
      </td>
    </tr>
    <!-- -
    <tr>
      <td height="35">用戶密碼：</td>
      <td>
        <input type="password" name="password" id="password" class="inp" maxlength="150">
      </td>
    </tr>
     -->
    <tr>
      <td height="35">用戶名稱：</td>
      <td>
        <input type="text" name="userName" id="userName" class="inp" maxlength="20">
      </td>
    </tr>
    <tr>
      <td height="35">電郵地址：</td>
      <td>
        <input type="text" name="email" id="email" class="inp" maxlength="35" style="width:200px;">
      </td>
    </tr>
    <tr>
      <td height="35">聯絡電話：</td>
      <td>
        <input type="text" name="contactNo" id="contactNo" class="inp" maxlength="15">
      </td>
    </tr>
    <tr>
      <td height="35">傳真號碼：</td>
      <td>
        <input type="text" name="faxNo" id="faxNo" class="inp" maxlength="15">
      </td>
    </tr>
    <tr>
      <td height="35">所在地址：</td>
      <td>
        <input type="text" name="address" id="address" class="inp" maxlength="60" style="width:250px;">
      </td>
    </tr>
    <tr>
      <td height="35">所在部門：</td>
      <td>
        <input type="text" name="department" id="department" class="inp" maxlength="20">
      </td>
    </tr>
    <tr>
      <td height="35">狀態：</td>
      <td>
        <input type="radio" name="status" value="A" checked="checked" style="background-color:#F5F2DA;border:0px;">有效&nbsp;&nbsp;
        <input type="radio" name="status" value="L" style="background-color:#F5F2DA;border:0px;">凍結&nbsp;&nbsp;
        <input type="radio" name="status" value="N" style="background-color:#F5F2DA;border:0px;">無效
      </td>
    </tr>
    <tr>
      <td height="35">備注：</td>
      <td>
        <textarea rows="2" cols="50" id="remarks" name="remarks" onKeyUp="countlen()"></textarea>
      </td>
    </tr>
    <tr>
      <td height="45" colspan="2">
        <input type="submit" name="submit" id="submit" value="提 交">&nbsp;&nbsp;
        <input type="reset" name="reset" value="重 置">&nbsp;&nbsp;
        <input type="button" name="back" value="返 回" onClick="javascript:history.back();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
