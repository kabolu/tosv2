<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="edu.must.tos.bean.BookSupplier"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看書商資料</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
input{
	height: 18px;
}
td{
	padding: 3px 0px;
}
</style>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
BookSupplier supplier = null;
String code = "";
String chnName = "";
String engName = "";
String contactName = "";
String tel1 = "";
String tel2 = "";
String fax1 = "";
String fax2 = "";
String email = "";
String address = "";
String resume = "";
String remarks = "";
if(request.getAttribute("supplier") != null){
	supplier = (BookSupplier)request.getAttribute("supplier");
	if(supplier.getSupplierCode() != null){
		code = supplier.getSupplierCode();
	}
	if(supplier.getSupplierName() != null){
		chnName = supplier.getSupplierName();
	}
	if(supplier.getSupplierEngName() != null){
		engName = supplier.getSupplierEngName();
	}
	if(supplier.getContactName() != null){
		contactName = supplier.getContactName();
	}
	if(supplier.getSupplierTel_1() != null){
		tel1 = supplier.getSupplierTel_1();
	}
	if(supplier.getSupplierTel_2() != null){
		tel2 = supplier.getSupplierTel_2();
	}
	if(supplier.getSupplierFax_1() != null){
		fax1 = supplier.getSupplierFax_1();
	}
	if(supplier.getSupplierFax_2() != null){
		fax2 = supplier.getSupplierFax_2();
	}
	if(supplier.getSupplierEmail() != null){
		email = supplier.getSupplierEmail();
	}
	if(supplier.getAddress() != null){
		address = supplier.getAddress();
	}
	if(supplier.getResumeInfo() != null){
		resume = supplier.getResumeInfo();
	}
	if(supplier.getRemarks() != null){
		remarks = supplier.getRemarks();
	}
}
%>
<h4 style="border-bottom:1px #000 solid;margin:5px;">查看書商資料</h4>
<form action="BookSupplierServlet" method="post" name="editForm" onSubmit="return checkSubmit();">
<table align="center" cellpadding="0" cellspacing="0" border="0" width="90%">
  <tr>
    <td width="90">書商編號：</td>
    <td><%=code %></td>
    <td width="90">書商中文名稱：</td>
    <td><%=chnName %></td>
  </tr>
  <tr>
    <td>書商英文名稱：</td>
    <td><%=engName %></td>
    <td>聯系人姓名：</td>
    <td><%=contactName %></td>
  </tr>
  <tr>
    <td>聯繫電話1：</td>
    <td><%=tel1 %></td>
    <td>聯繫電話2：</td>
    <td><%=tel2 %></td>
  </tr>
  <tr>
    <td>傳真號碼1：</td>
    <td><%=fax1 %></td>
    <td>傳真號碼2：</td>
    <td><%=fax2 %></td>
  </tr>
  <tr>
    <td>電郵地址：</td>
    <td colspan="3"><%=email %></td>
  </tr>
  <tr>
    <td>書商地址：</td>
    <td colspan="3"><%=address %></td>
  </tr>
  <tr>
    <td>書商簡介：</td>
    <td colspan="3"><%=resume %></td>
  </tr>
  <tr>
    <td>備注：</td>
    <td colspan="3"><%=remarks %></td>
  </tr>
  <tr>
    <td colspan="4" height="30" align="center">
      <input type="button" name="back" value="返 回" style="height: 22px;" onClick="history.back();">
    </td>
  </tr>
</table>
</form>
</body>
</html>
