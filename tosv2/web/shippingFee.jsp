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
<script language="javascript">
function forwardPage(feeNo){
	document.forwardForm.feeNo.value = feeNo;
	document.forwardForm.action = "ShippingFeeServlet";
	document.forwardForm.submit();
}
function doExport(){
	if(document.feeForm.intake.value != ""){
		document.form1.intake.value = document.feeForm.intake.value; 
	}
	 
	document.form1.action = "ShippingFeeServlet?type=report";
	document.form1.submit();
}
function page(start){
	document.form1.start.value = start;
	var totalPages = parseInt(document.form1.totalPages.value);
	if(start == "page") {
		if(document.form1.instart.value.replace(/ /g,"") == "") {
			alert("頁碼不能為空！");
			document.form1.instart.focus();
			document.form1.instart.select();
			return;
		}
		if(isNaN(document.form1.instart.value)) {
			alert("請輸入整數!");
			document.form1.instart.focus();
			document.form1.instart.select();
			return;
		}
		if(parseInt(document.form1.instart.value)>totalPages) {
			alert("輸入的頁碼已超出最大頁,請重新輸入");
			document.form1.instart.focus();
			document.form1.instart.select();
			return;
		}
		document.form1.start.value = (parseInt(document.form1.instart.value)-1)*10;
	}
	document.form1.action.value = "ShippingFeeServlet";
	document.form1.submit();
}
function checkForm(){
	a = document.getElementById("fromDate").value;
	b = document.getElementById("toDate").value;
	if(a != "" && b != ""){
		return checkDateValue(a, b);
	}else if(a == "" && b == ""){
		return true;
	}else{
		alert("請輸入完整日期時間查詢！");
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
List<ShippingFee> list = (List)request.getAttribute("shippingFeeList");
String intake = null;
if(request.getAttribute("intake") != null){
	intake = (String)request.getAttribute("intake");
}
String fromDate = null;
if(request.getAttribute("fromDate") != null){
	fromDate = (String)request.getAttribute("fromDate");
}
String toDate = null;
if(request.getAttribute("toDate") != null){
	toDate = (String)request.getAttribute("toDate");
}
double totalPages = Double.parseDouble(request.getAttribute("totalPages").toString());
%>
<h2>費用記錄</h2>
<form action="ShippingFeeServlet" method="post" name="feeForm" onsubmit="return checkForm();">
<table border="0" cellpadding="0" cellspacing="0" width="80%">
  <tr>
    <td width="5%">學期：</td>
    <td width="10%">
      <input type="text" name="intake" value="<%=intake %>" class="inp" size="6"/>
    </td>
    <td width="10%" align="right">開始日期：</td>
    <td width="10%">
      <input name="fromDate" type="text" id="fromDate" onclick="new Calendar(null, null, 3).show(this);" class="inp" size="9" readonly="readonly"/>
    </td>
    <td width="10%" align="right">結束日期：</td>
    <td width="10%">
      <input name="toDate" type="text" id="toDate" onclick="new Calendar(null, null, 3).show(this);" class="inp" size="9" readonly="readonly"/>
    </td>
    <td width="45%" align="center">
      <input type="submit" name="button" value="查 找"/>
      <input type="button" name="button" value=" 導出費用記錄表 " onclick="doExport();"/>
      <input type="button" name="button" value=" 新增 " onclick="forwardPage(0);"/>
    </td>
  </tr>
</table>
</form>
<p></p>
<form name="forwardForm" action="" method="post">
<input type="hidden" name="type" value="forward"/>
<input type="hidden" name="feeNo" value=""/>
</form>
<form name="form1" action="" method="post">
<%
if(intake != null){
%>
<input type="hidden" name="intake" value="<%=intake %>" />
<%
}
if(fromDate != null){
%>
<input type="hidden" name="fromDate" value="<%=fromDate %>" />
<%
}
if(toDate != null){
%>
<input type="hidden" name="toDate" value="<%=toDate %>" />
<%
}
%>
<input type="hidden" name="start" value="" />
<input type="hidden" value="<%=totalPages %>" name="totalPages" />
<table width="99.9%"  align="center" border="0" cellspacing="1" cellpadding="0">
  <tr bgcolor="#C6D6FD">
    <th width="7%">學期</th>
    <th width="15%">公司名稱</th>
    <th width="20%">單號</th>
    <th width="15%">費用類型</th>
    <th width="7%">RMB</th>
    <th width="7%">MOP</th>
    <th width="9%">折成MOP</th>
    <th width="10%">發票日期</th>
    <th width="10%">操作</th>
  </tr>
  <%
  if(list != null && !list.isEmpty()){
	  for(ShippingFee fee : list){
  %>
  <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td><%=fee.getIntake() %></td>
    <td><%=fee.getCompanyName() %></td>
    <td><%=fee.getPrNum() %></td>
    <td><%=fee.getFeeType() %></td>
    <td><%=fee.getRmbPrice() %></td>
    <td><%=fee.getMopPrice() %></td>
    <td><%=fee.getToMopPrice() %></td>
    <td><%=fee.getInvoiceDate() %></td>
    <td align="center">
      <input type="button" name="button" value="編輯" class="inp" onclick="forwardPage('<%=fee.getFeeNo() %>');"/>
    </td>
  </tr>
  <%
	  }
  %>
  <tr>
    <td align="center" colspan="9">
      <%
      List<PageBean> pagelist = (List)request.getAttribute("page");
      for(PageBean pageBean : pagelist){
    	  if (pageBean.getPageType().equals("prev")){
    		  out.print("<a href='javascript:page("+pageBean.getOffset()+");' >上一頁</a>&nbsp;");
    	  }
    	  if (pageBean.getPageType().equals("cur")){
    		  out.print("<a href='javascript:page("+pageBean.getOffset()+");' ><font color=#d20000 ><b> "+pageBean.getPage()+"</b></font></a>&nbsp;");
    	  }
    	  if (pageBean.getPageType().equals("pages")){
    		  out.print("<a href='javascript:page("+pageBean.getOffset()+");' >"+pageBean.getPage()+"</a>&nbsp;");
    	  }
    	  if (pageBean.getPageType().equals("next")){
    		  out.print("<a href='javascript:page("+pageBean.getOffset()+");' >下一頁</a>&nbsp;");
    	  }
      }
      %>
      <label>
        &nbsp;&nbsp;
        <input type="text" name="instart" id="len" onchange="changeFocus();" />
        &nbsp;
        <input type="button" name="jump" onclick="page('page')" value="跳轉" />
      </label>
    </td>
  </tr>
  <%
  }else{
  %>
  <tr>
    <td colspan="9">沒有任何記錄！</td>
  </tr>
  <%
  }
  %>
</table>
</form>

</body>
</html>