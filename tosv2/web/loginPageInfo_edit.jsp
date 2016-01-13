<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="edu.must.tos.bean.SysConfig"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登錄頁面信息</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
<script language="javascript">
$(document).ready(function(){
	$('#errors').text("");
	$('input[@name=reset]').click(function(){
		$('#errors').text("");
	})
})
function countlen(){
	if(document.addForm.scchndesc.value.length > 765){
		alert("字數已經超過765！！");
		document.addForm.scchndesc.value = document.addForm.scchndesc.value.substring(0, 765);
	}
	return true;
}
function checkInput(){
	var key = $('input[@name=key]').val();
	var scchndesc = $('input[@name=scchndesc]').val();
	$('#errors').text("");
	if(key==""){
		$('#errors').text("* 請輸入序號！");
		return false;
	}
	if(scchndesc == ""){
		$('#errors').text("* 請輸入內容！");
		return false;
	}
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
String sckey = "";
String sctype = "";
String scchndesc = "";
String scengdesc = "";
String actind = "";
SysConfig conf = null;
if(request.getAttribute("conf") != null){
	conf = (SysConfig)request.getAttribute("conf");
	if(conf != null){
		if(conf.getScKey() != null){
			sckey = conf.getScKey();
		}
		if(conf.getScType() != null){
			sctype = conf.getScType();
		}
		if(conf.getScChnDesc() != null){
			scchndesc = conf.getScChnDesc();
		}
		if(conf.getScEngDesc() != null){
			scengdesc = conf.getScEngDesc();
		}
		if(conf.getActInd() != null){
			actind = conf.getActInd();
		}
	}
}
%>
<h2>編輯登入版面訊息</h2>
<form action="LoginPageInfoServlet" method="post" name="addForm" onSubmit="return checkInput();">
  <input type="hidden" name="oprType" value="updateInfo">
  <input type="hidden" name="scType" value="<%=sctype %>">
  <input type="hidden" name="scKey" value="<%=sckey %>">
  <table width="80%" border="0" cellpadding="0" cellspacing="0" id="the-table" align="center">
    <tr>
      <td width="10%">序號：</td>
      <td width="50%">
        <input type="text" name="key" value="<%=sckey %>" style="width:80px;height:18px;" onKeyUp="value=value.replace(/[^\d]/g,'')"/>
      </td>
      <td width="10%">狀態：</td><td width="30%">
        <select name="actind">
          <option value="Y" <%if("Y".equals(actind)){out.print("selected");} %>>顯示</option>
          <option value="N" <%if("N".equals(actind)){out.print("selected");} %>>不顯示</option>
        </select>
      </td>
    </tr>
    <tr>
      <td>中文內容：</td>
      <td colspan="3">
        <textarea rows="10" cols="60" name="scchndesc" onKeyUp="countlen();"><%=scchndesc %></textarea>
      </td>
    </tr>
    <tr>
      <td>英文內容：</td>
      <td colspan="3">
        <textarea rows="10" cols="60" name="scengdesc" onKeyUp="countlen();"><%=scengdesc %></textarea>
      </td>
    </tr>
  </table>
  <p style="margin:10px 0px 0px 10px;" align="center">
    <input type="submit" name="submit" value="保 存">&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="reset" name="reset" value="重 置">&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" name="back" value="返 回" onClick="history.back();">&nbsp;&nbsp;&nbsp;&nbsp;
    <font color="red"><label id="errors"></label></font>
  </p>
</form>
</body>
</html>
