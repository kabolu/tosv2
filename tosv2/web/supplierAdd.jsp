<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加書商資料</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
td{
	padding: 3px 0px;
}
</style>
<script language="javascript">
function checkSubmit(){
	if(addForm.code.value == ""){
		alert("書商編號不能為空！");
		addForm.code.focus();
		return false;
	}
	if(addForm.name.value == ""){
		alert("書商中文名稱不能為空！");
		addForm.name.focus();
		return false;
	}
}
function countLength(element){
	if(element=="resume"){
		var length = document.addForm.resume.value.length;
		if(length > 200){
			alert("書商簡介已超過200個字元！");
			document.addForm.resume.value = document.addForm.resume.value.substring(0, 200);
		}
		return true;
	}else{
		var length = document.addForm.remarks.value.length;
		if(length > 100){
			alert("備注已超過100個字元！");
			document.addForm.remarks.value = document.addForm.remarks.value.substring(0, 100);
		}
		return true;
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
<h2>添加書商資料</h2>
<form action="BookSupplierServlet" method="post" name="addForm" onSubmit="return checkSubmit();">
  <input type="hidden" name="type" value="add">
  <table align="center" cellpadding="0" cellspacing="0" border="0" width="90%">
    <tr>
      <td width="120">書商編號：</td>
      <td>
        <input type="text" name="code" class="inp">
      </td>
      <td>書商中文名稱：</td>
      <td>
        <input type="text" name="name" class="inp" style="width:180px">
      </td>
    </tr>
    <tr>
      <td>書商英文名稱：</td>
      <td>
        <input type="text" name="engName" class="inp" style="width:180px">
      </td>
      <td>聯系人姓名：</td>
      <td>
        <input type="text" name="contactName" class="inp">
      </td>
    </tr>
    <tr>
      <td>聯繫電話1：</td>
      <td>
        <input type="text" name="contactTel1" class="inp">
      </td>
      <td>聯繫電話2：</td>
      <td>
        <input type="text" name="contactTel2" class="inp">
      </td>
    </tr>
    <tr>
      <td>傳真號碼1：</td>
      <td>
        <input type="text" name="contactFax1" class="inp">
      </td>
      <td>傳真號碼2：</td>
      <td>
        <input type="text" name="contactFax2" class="inp">
      </td>
    </tr>
    <tr>
      <td>電郵地址：</td>
      <td colspan="3">
        <input type="text" name="email" style="width:200px" class="inp">
      </td>
    </tr>
    <tr>
      <td>書商地址：</td>
      <td colspan="3">
        <input type="text" name="address" style="width:260px" class="inp">
      </td>
    </tr>
    <tr>
      <td>書商簡介：<br>(不要超過200個字元)</td>
      <td colspan="3">
        <textarea rows="3" cols="56" name="resume" ></textarea>
      </td>
    </tr>
    <tr>
      <td>備注：<br>(不要超過100個字元)</td>
      <td colspan="3">
        <textarea rows="3" cols="56" name="remarks" ></textarea>
      </td>
    </tr>
    <tr>
      <td colspan="4">
        <input type="checkbox" class="checkbox" name="io" value="I">入貨書商
        <input type="checkbox" class="checkbox" name="io" value="O">出貨書商
      </td>
    </tr>
    <tr>
      <td colspan="4">
        <input type="checkbox" class="checkbox" name="inner" value="Y">學院/書商（學校內部）
      </td>
    </tr>
    <tr>
      <td colspan="4" height="30" align="center">
        <input type="submit" name="submit" value="提 交" style="height: 22px;">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" name="reset" value="重 設" style="height: 22px;">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" name="back" value="返 回" style="height: 22px;" onClick="history.back();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
