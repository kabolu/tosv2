<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.ShippingFee"%>
<%@page import="edu.must.tos.util.PageBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>費用記錄</title>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/tos.js"></script>
<style type="text/css">
table{
	margin: 0px 0px 0px 15px;
	border-collapse: collapse;
}
table td{
	border: 1px #F5F2DA solid;
	padding: 2px 5px;
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
ShippingFee shippingFee = null;
if(request.getAttribute("shippingFee") != null){
	shippingFee = (ShippingFee)request.getAttribute("shippingFee");
}
int feeNo = 0;
String company = "";
String prNum = "";
String feeType = "";
String rmbPrice = "";
String mopPrice = "";
String toMopPrice = "";
String invoiceDate = "";
String remarks = "";
if(shippingFee != null){
	feeNo = shippingFee.getFeeNo();
	company = shippingFee.getCompanyName();
	prNum = shippingFee.getPrNum();
	feeType = shippingFee.getFeeType();
	rmbPrice = String.valueOf(shippingFee.getRmbPrice());
	mopPrice = String.valueOf(shippingFee.getMopPrice());
	toMopPrice = String.valueOf(shippingFee.getToMopPrice());
	invoiceDate = shippingFee.getInvoiceDate();
	remarks = shippingFee.getRemarks();
}
%>
<h2>費用記錄</h2>
<form action="ShippingFeeServlet" method="post" name="form">
<input type="hidden" name="type" value="edit"/>
<input type="hidden" name="feeNo" value="<%=feeNo %>"/>
<table border="0" cellpadding="0" cellspacing="0" width="60%">
  <tr>
    <td width="15%">公司名稱：</td>
    <td width="85%">
      <input name="company" class="inp" value="<%=company %>"/>
    </td>
  </tr>
  <tr>
    <td>單號：</td>
    <td>
      <input name="prnum" class="inp" value="<%=prNum %>"/>
    </td>
  </tr>
  <tr>
    <td>費用類型：</td>
    <td>
      <select name="feeType">
        <option value="">==請選擇==</option>
        <option value="運費" <%if("運費".equals(feeType)){out.print("selected");} %> >運費</option>
        <option value="上樓費" <%if("上樓費".equals(feeType)){out.print("selected");} %>>上樓費</option>
        <option value="手續費" <%if("手續費".equals(feeType)){out.print("selected");} %>>手續費</option>
        <option value="其他" <%if("其他".equals(feeType)){out.print("selected");} %>>其他</option>
      </select>
    </td>
  </tr>
  <tr>
    <td>RMB：</td>
    <td>
      <input name="rmb" class="inp" value="<%=rmbPrice %>" maxlength="5" size="5" onkeypress="return NumWithoutMinus(event)"/>
    </td>
  </tr>
  <tr>
    <td>MOP：</td>
    <td>
      <input name="mop" class="inp" value="<%=mopPrice %>" maxlength="5" size="5" onkeypress="return NumWithoutMinus(event)"/>
    </td>
  </tr>
  <tr>
    <td>折成MOP：</td>
    <td>
      <input name="toMop" class="inp" value="<%=toMopPrice %>" maxlength="5" size="5" onkeypress="return NumWithoutMinus(event)"/>
    </td>
  </tr>
  <tr>
    <td>發票日期：</td>
    <td>
      <input name="invoiceDate" type="text" id="invoiceDate" value="<%=invoiceDate %>" onclick="new Calendar(null, null, 3).show(this);" class="inp" size="9" readonly="readonly"/>
    </td>
  </tr>
  <tr>
    <td>備註：</td>
    <td>
      <textarea name="remarks" rows="5" cols="60"><%=remarks %></textarea>
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <input type="submit" name="submit" value="提 交"/>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="reset" name="reset" value="重 置"/>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" name="back" value="返 回" onclick="history.back();"/>
    </td>
  </tr>
</table>
</form>
</body>
</html>