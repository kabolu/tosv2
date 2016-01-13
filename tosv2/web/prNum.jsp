<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.BookStockInOrder"%>
<%@page import="edu.must.tos.bean.BookStockOutOrder"%>
<%@page import="edu.must.tos.bean.BookStockOutBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出入貨單查詢</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
<style type="text/css">
<!--
.view{
	border:0;
	background:#fffbef;
}
-->
</style>
<script language="javascript">
$(document).ready(function(){
	$('input[@name^=prNum]').addClass("view");
	$('input[@name^=prNum]').attr("readonly", "readonly");
	
	$('input[@name^=submit]').hide();
	$('input[@name^=cancel]').hide();
})
function openDetail(theURL, winName, features) {
	window.open(theURL,winName,features);
}
function modify(i){
	$('input[@name=prNum'+i+']').removeClass("view");
	$('input[@name=prNum'+i+']').attr("readonly", "");
	
	$('input[@name=submit'+i+']').show();
	$('input[@name=cancel'+i+']').show();
	$('input[@name=modify'+i+']').hide();
}
function cancel(i){
	var oldPrNum = $('input[@name=oldPrNum'+i+']').val();
	$('input[@name=prNum'+i+']').attr("value", oldPrNum);
	$('input[@name=prNum'+i+']').addClass("view");
	$('input[@name=prNum'+i+']').attr("readonly", "readonly");
	$('input[@name=submit'+i+']').hide();
	$('input[@name=cancel'+i+']').hide();
	$('input[@name=modify'+i+']').show();
}
function del(i){
	var outNo = $('input[@name=outNo'+i+']').val();
	var prNum = $('input[@name=prNum]').val();
	if(confirm("是否確定刪除該記錄？")){
		$.post(
			"BookStockOutServlet",
			{
				type: "deleteStockOut",
				outNo: outNo
			},
			function(result){
				if(result==0){
					alert("該記錄已成功刪除！");
					document.searchForm.searchPrNum.value = prNum;
					document.searchForm.submit();
				}else{
					alert("刪除操作失敗！");
					return false;
				}
			}
		)
	}
}
function receipt(i){ //rainbow add
	var prNum = $('input[@name=prNum'+i+']').val();
	$.post(
		"BookInventoryServlet",
		{
			type: "showReceipt",
			prNum: prNum
		},
		function(result){
			if(result==0){
				//openDetail('bookStockOutDetailList.jsp?showReceipt=Y','','width=800,height=600,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no');
				openDetail('BookStockOutServlet?type=printStockOutList&prNum='+prNum,'','width=800,height=600,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no');
			}else{
				alert("操作失敗！");
				return false;
			}
		}
	)
	
}
function submit(i, val){
	var oldPrNum = $('input[@name=oldPrNum'+i+']').val();
	var prNum = $('input[@name=prNum'+i+']').val();
	var stockNo = $('input[@name=stockNo'+i+']').val();
	
	if(prNum == ""){
		if(val == "I")
		 alert("請填寫入貨單編號！");
		else
		 alert("請填寫出貨單編號！");
	}else{
		$.post(
			"BookInventoryServlet",
			{
				type: "modifyPrNum",
				prNum: prNum,
				oldPrNum: oldPrNum,
				stockNo: stockNo,
				inOrOut: val
			},
			function(result){
				if(result == "0"){
					alert("單編號修改成功！");
					$('input[@name=oldPrNum'+i+']').attr("value", prNum);
					$('input[@name=prNum'+i+']').attr("value", prNum);
					$('input[@name=prNum'+i+']').addClass("view");
					$('input[@name=prNum'+i+']').attr("readonly", "readonly");
					
					$('input[@name=submit'+i+']').hide();
					$('input[@name=cancel'+i+']').hide();
					$('input[@name=modify'+i+']').show();
				}else if(result == "1"){
					alert("修改的單編號已存在！");
					$('input[@name=submit'+i+']').show();
					$('input[@name=cancel'+i+']').show();
					$('input[@name=modify'+i+']').hide();
				}else if(result == "2"){
					alert("所提交的單編號與原來相同，系統不作處理！！");
					$('input[@name=prNum'+i+']').addClass("view");
					$('input[@name=prNum'+i+']').attr("readonly", "readonly");
					
					$('input[@name=submit'+i+']').hide();
					$('input[@name=cancel'+i+']').hide();
					$('input[@name=modify'+i+']').show();
				}
			}
		)
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
List list = null;
if(request.getAttribute("list") != null){
	list = (List)request.getAttribute("list");	
}
String inOrOut = null;
if(request.getAttribute("inOrOut") != null){
	inOrOut = (String)request.getAttribute("inOrOut");
}
String prNum = "";
if(request.getAttribute("prNum") != null){
	prNum = (String)request.getAttribute("prNum");
}
%>
<h2>出/入貨單查詢</h2>
<form name="searchForm" action="BookInventoryServlet" method="post" >
  <input type="hidden" name="type" value="searchPrNumList">
  <input type="hidden" name="noIntake" value="Y">
  <table border="0" cellpadding="0" cellspacing="0" width="60%">
    <tr>
      <td height="30">
        <label>類型：</label>
      </td>
      <td colspan="2">
        <input type="radio" name="inOrOut" value="I" <%if("I".equals(inOrOut)){out.print("checked");} %>>
        <label>入貨</label>&nbsp;&nbsp;
        <input type="radio" name="inOrOut" value="O" <%if("O".equals(inOrOut)){out.print("checked");} %>>
        <label>出貨</label>&nbsp;&nbsp;
        <input type="radio" name="inOrOut" value="P" <%if("P".equals(inOrOut)){out.print("checked");} %>>
        <label>代購出貨</label>
      </td>
    </tr>
    <tr>
      <td height="30" width="10%">單編號：</td>
      <td width="45%">
        <input type="text" name="searchPrNum" class="inp" value="<%=prNum %>">
      </td>
      <td width="45%">
        <input type="submit" value="提 交">&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="重 置">
      </td>
    </tr>
  </table>
</form>

<table width="99.9%" align="center" border="0" cellspacing="1" cellpadding="0" id="the-table">
<%
if(list != null && !list.isEmpty()){
	if(inOrOut!=null && "I".equals(inOrOut)){
%>
  <tr bgcolor="#C6D6FD">
    <th height="25" width="30%">入貨單編號</th>
    <th width="10%">學期</th>
    <th width="10%">付款狀態</th>
    <th width="15%">付款日期</th>
    <th width="10%">幣種</th>
    <th width="25%">操作</th>
  </tr>
  <%
		for(int i=0; i<list.size(); i++){
			BookStockInOrder b = (BookStockInOrder)list.get(i);
			String paidStatus = "待付";
			String paidDate = "N/A";
			String paidCurrency = "N/A";
			if(b.getPaidstatus() != null && "Y".equals(b.getPaidstatus())){
				paidStatus = "已付";
			}
			 if(b.getPaidDate() != null){
				 paidDate = b.getPaidDate();
			 }
			 if(b.getPaidcurrency() != null){
				 paidCurrency = b.getPaidcurrency();
			 }
			 %>
			 <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
			  <td height="22">
			  <input name="stockNo<%=i %>" type="hidden" value="<%=b.getStockInNo() %>">
			  <input name="oldPrNum<%=i %>" type="hidden" value="<%=b.getPrnum() %>">
			  <input name="prNum<%=i %>" type="text" value="<%=b.getPrnum() %>" class="view" style="height:18px;width:150px;">
			  </td>
			  <td align="center"><%=b.getIntake() %></td>
			  <td align="center"><%=paidStatus %></td>
			  <td align="center"><%=paidDate %></td>
			  <td align="center"><%=paidCurrency %></td>
			  <td align="center">
			  <input type="button" name="modify<%=i %>" value="修改編號" onClick="modify('<%=i %>');" />
			  <input type="button" name="submit<%=i %>" value="提交" onClick="submit('<%=i %>', 'I');" />
			  <input type="button" name="cancel<%=i %>" value="取消" onClick="cancel('<%=i %>');" />
			  </td>
			 </tr>
			 <%
		 }
	 }else if(inOrOut != null && "O".equals(inOrOut)){
		 %>
		 <tr bgcolor="#C6D6FD">
		  <th height="25" width="30%">出貨單編號</th>
		  <th width="10%">學期</th>
		  <th width="10%" colspan="2">出貨日期</th>
		  <th width="10%">幣種</th>
		  <th width="25%">操作</th>
		 </tr>
		 <%
		 for(int i=0; i<list.size(); i++){
			 BookStockOutOrder b = (BookStockOutOrder)list.get(i);
			 String paidCurrency = "N/A";
			 if(b.getPaidCurrency() != null){
				 paidCurrency = b.getPaidCurrency();
			 }
			 %>
			 <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
			  <td height="22">
			  <input name="stockNo<%=i %>" type="hidden" value="<%=b.getStockOutNo() %>">
			  <input name="oldPrNum<%=i %>" type="hidden" value="<%=b.getPrnum() %>">
			  <input name="prNum<%=i %>" type="text" value="<%=b.getPrnum() %>" class="view" style="height:18px;width:150px;">
			  </td>
			  <td align="center"><%=b.getIntake() %></td>
			  <td align="center" colspan="2"><%=b.getOutDate()%></td>
			  <td align="center"><%=paidCurrency %></td>
			  <td align="center">
			  <input type="button" name="modify<%=i %>" value="出貨單" onClick="receipt('<%=i %>');" />
			  <input type="button" name="modify<%=i %>" value="修改編號" onClick="modify('<%=i %>');" />
			  <input type="button" name="submit<%=i %>" value="提交" onClick="submit('<%=i %>', 'O');" />
			  <input type="button" name="cancel<%=i %>" value="取消" onClick="cancel('<%=i %>');" />
			  </td>
			 </tr>
			 <%
		 }
	 }else if(inOrOut != null && "P".equals(inOrOut)){
		 %>
		 <tr bgcolor="#C6D6FD">
		  <th height="25" width="20%">出貨單編號<input type="hidden" name="prNum" value="<%=prNum %>"/></th>
		  <th width="10%">學期</th>
		  <th width="10%">ISBN</th>
		  <th width="20%">書名</th>
		  <th width="10%">出貨數</th>
		  <th width="10%" colspan="2">出貨日期</th>
		  <th width="10%">操作</th>
		 </tr>
		 <%
		 for(int i=0; i<list.size(); i++){
			 BookStockOutBean bean = (BookStockOutBean)list.get(i);
			 %>
			 <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
			  <td height="22">
			  <input name="outNo<%=i %>" type="hidden" value="<%=bean.getOutNo() %>">
			  <input name="prNum<%=i %>" type="hidden" value="<%=bean.getPrNum() %>" >
			  <%=bean.getPrNum() %>
			  </td>
			  <td align="center"><%=bean.getIntake() %></td>
			  <td align="center"><%=bean.getIsbn() %></td>
			  <td align="center"><%=bean.getBook().getTitle()%></td>
			  <td align="center"><%=bean.getAdjNum() %></td>
			  <td align="center" colspan="2"><%=bean.getIspDate() %></td>
			  <td align="center">
			    <input type="button" name="delete<%=i %>" value="刪除" onClick="del('<%=i %>');" />
			  </td>
			 </tr>
			 <%
		 }
	 }
 }else{
	 %>
	 <tr>
	  <td colspan="6">沒有相關資料！</td>
	 </tr>
	 <%
 }
 %>
</table>
</body>
</html>
