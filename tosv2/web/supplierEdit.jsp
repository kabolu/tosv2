<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="edu.must.tos.bean.BookSupplier"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改書商資料</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
td{
	padding: 3px 0px;
}
</style>
<script language="javascript">
function checkSubmit(){
	if(editForm.name.value == ""){
		alert("書商中文名稱不能為空！");
		editForm.name.focus();
		return false;
	}
}
function countLength(element){
	if(element == "resume"){
		var length = document.editForm.resume.value.length;
		if(length > 200){
			alert("書商簡介已超過200個字元！");
			document.editForm.resume.value = document.editForm.resume.value.substring(0, 200);
		}
		return true;
	}else{
		var length = document.editForm.remarks.value.length;
		if(length > 100){
			alert("備注已超過100個字元！");
			document.editForm.remarks.value = document.editForm.remarks.value.substring(0, 100);
		}
		return true;
	}
}
</script>
<%if (session.getAttribute("userId") == null) {%>
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
String code = "";
String name = "";
String engName = "";
String contactName = "";
String tel_1 = "";
String tel_2 = "";
String fax_1 = "";
String fax_2 = "";
String email = "";
String address = "";
String resumeInfo = "";
String remarks = "";
String io = "";
String inner = "";
BookSupplier supplier = null;
if(request.getAttribute("supplier") != null){
	supplier = (BookSupplier)request.getAttribute("supplier");
	if(supplier.getSupplierCode() != null){
		code = supplier.getSupplierCode();
	}
	if(supplier.getSupplierName() != null){
		name = supplier.getSupplierName();
	}
	if(supplier.getSupplierEngName() != null){
		engName = supplier.getSupplierEngName();
	}
	if(supplier.getContactName() != null){
		contactName = supplier.getContactName();
	}
	if(supplier.getSupplierTel_1() != null){
		tel_1 = supplier.getSupplierTel_1();
	}
	if(supplier.getSupplierTel_2() != null){
		tel_2 = supplier.getSupplierTel_2();
	}
	if(supplier.getSupplierFax_1() != null){
		fax_1 = supplier.getSupplierFax_1();
	}
	if(supplier.getSupplierFax_2() != null){
		fax_2 = supplier.getSupplierFax_2();
	}
	if(supplier.getSupplierEmail() != null){
		email = supplier.getSupplierEmail();
	}
	if(supplier.getAddress() != null){
		address = supplier.getAddress();
	}
	if(supplier.getResumeInfo() != null){
		resumeInfo = supplier.getResumeInfo();
	}
	if(supplier.getRemarks() != null){
		remarks = supplier.getRemarks();
	}
	if(supplier.getIo() != null){
		io = supplier.getIo();
	}
	if(supplier.getInner() != null){
		inner = supplier.getInner();		
	}
}
%>
<h2>修改書商資料</h2>
<form action="BookSupplierServlet" method="post" name="editForm" onSubmit="return checkSubmit();">
  <input type="hidden" name="type" value="edit">
  <input type="hidden" name="no" value="<%=supplier.getSupplierNo() %>">
  <table align="center" cellpadding="0" cellspacing="0" border="0" width="90%">
    <tr>
      <td width="120">書商編號：</td>
      <td>
        <input type="text" name="code" value="<%=code %>" class="inp">
      </td>
      <td>書商中文名稱：</td>
      <td>
        <input type="text" name="name" value="<%=name %>" style="width:180px" class="inp">
      </td>
    </tr>
    <tr>
      <td>書商英文名稱：</td>
      <td>
        <input type="text" name="engName" value="<%=engName %>" style="width:180px" class="inp">
      </td>
      <td>聯系人姓名：</td>
      <td>
        <input type="text" name="contactName" value="<%=contactName %>" class="inp">
      </td>
    </tr>
    <tr>
      <td>聯繫電話1：</td>
      <td>
        <input type="text" name="contactTel1" value="<%=tel_1 %>" class="inp">
      </td>
      <td>聯繫電話2：</td>
      <td>
        <input type="text" name="contactTel2" value="<%=tel_2 %>" class="inp">
      </td>
    </tr>
    <tr>
      <td>傳真號碼1：</td>
      <td>
        <input type="text" name="contactFax1" value="<%=fax_1 %>" class="inp">
      </td>
      <td>傳真號碼2：</td>
      <td>
        <input type="text" name="contactFax2" value="<%=fax_2 %>" class="inp">
      </td>
    </tr>
    <tr>
      <td>電郵地址：</td>
      <td colspan="3">
        <input type="text" name="email" value="<%=email %>" style="width:200px" class="inp">
      </td>
    </tr>
    <tr>
      <td>書商地址：</td>
      <td colspan="3">
        <input type="text" name="address" value="<%=address %>" style="width:260px" class="inp">
      </td>
    </tr>
    <tr>
      <td>書商簡介：<br>(不要超過200個字元)</td>
      <td colspan="3">
        <textarea rows="3" cols="56" name="resume" onKeyUp="countLength('resume');"><%=resumeInfo %></textarea>
      </td>
    </tr>
    <tr>
      <td>備注：<br>(不要超過100個字元)</td>
      <td colspan="3">
        <textarea rows="3" cols="56" name="remarks" onKeyUp="countLength('remarks');"><%=remarks %></textarea>
      </td>
    </tr>
    <tr>
      <td colspan="4">
        <input type="checkbox" class="checkbox" name="io" value="I" <%if("I".equals(io) || "IO".equals(io)){out.print("checked");} %>>入貨書商
        <input type="checkbox" class="checkbox" name="io" value="O" <%if("O".equals(io) || "IO".equals(io)){out.print("checked");} %>>出貨書商
      </td>
    </tr>
    <tr>
      <td colspan="4">
        <input type="checkbox" class="checkbox" name="inner" value="Y" <%if("Y".equals(inner)){out.print("checked");} %>>學院/書商（學校內部）
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
