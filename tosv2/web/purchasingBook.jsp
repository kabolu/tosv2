<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.BookSupplier"%>
<%@page import="edu.must.tos.bean.BookPurchasing"%>
<%@page import="java.util.HashSet"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>代購圖書</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
<script language="javascript">
$(document).ready(function(){
	$('.B').hide();
	$('input[@name=searchType]').click(function(){
		var type = $("input[name='searchType'][checked]").val();
		if(type=="A"){
			$('.A').show();
			$('.B').hide();
			$('#BfromDate').attr("value", '');
			$('#BtoDate').attr("value", '');
			$('#supplierNo').attr("value", '0');
		}else{
			$('.A').hide();
			$('.B').show();
			$('#AfromDate').attr("value", '');
			$('#AtoDate').attr("value", '');
			$('input[@name=orderNo]').attr("value", '');
		}
	})
})
function submitForm(val){
	var BfromDate = document.searchForm.BfromDate.value;
	var BtoDate = document.searchForm.BtoDate.value;
	var supplierNo = document.searchForm.supplierNo.value;
	if(BfromDate != "" && BtoDate != ""){
		var d1 = new Date(BfromDate.replace(/\-/g, "\/"));
		var d2 = new Date(BtoDate.replace(/\-/g, "\/"));
		if(Date.parse(d1) - Date.parse(d2)>0){
			alert(BfromDate+"晚於"+BtoDate);
			return false;
		}
	}else if(BfromDate != "" || BtoDate != ""){
		alert("請輸入完整的日期範圍值！");
		return false;
	}
	if(supplierNo == "0"){
		alert("請選擇書商資料！");
		return false;
	}
	$.post(
		"PurchasingBookServlet",
		{
			type: "checkSearchList",
			BfromDate: BfromDate,
			BtoDate: BtoDate,
			param: val,
			supplierNo: supplierNo
		},
		function(result){
			if(result == 0){
				alert("沒有附件條件的出單記錄！");
				return false;
			}else{
				if(val == "N"){
					document.searchForm.param.value = val;
					document.searchForm.action = "PurchasingBookServlet";
					document.searchForm.submit();
				}else{
					$.post(
						"PurchasingBookServlet",
						{
							type: "checkCondition",
							BfromDate: BfromDate,
							BtoDate: BtoDate,
							param: val,
							supplierNo: supplierNo
						},
						function(data){
							var msg = "";
							$(data).find("results result").each(function(){
								var credate = $(this).find("credate").text();
								var fromdate = $(this).find("fromdate").text();
								var todate = $(this).find("todate").text();
								msg += "選擇日期為"+fromdate+"~"+todate+" 出單日期為："+credate+"\n";
							})
							if(msg!=""){
								if(confirm("出單條件曾與以下記錄有衝突：\n"+msg+"是否確定出單操作？")){
									document.searchForm.param.value = val;
									document.searchForm.action = "PurchasingBookServlet";
									document.searchForm.submit();
								}
							}else{
								if(confirm("是否確定出單操作？")){
									document.searchForm.param.value = val;
									document.searchForm.action = "PurchasingBookServlet";
									document.searchForm.submit();
								}
							}
						}
					)
				}
			}
		}
	)
}
function forwardPage(val){
	document.forwardForm.orderNo.value = val;
	document.forwardForm.action = "PurchasingBookServlet";
	document.forwardForm.submit();
}
function checkForm(){
	var type = $("input[name='searchType'][checked]").val();
	if(type == "A"){
		var AfromDate = document.searchForm.AfromDate.value;
		var AtoDate = document.searchForm.AtoDate.value;
		if(AfromDate != "" && AtoDate != ""){
			var d1 = new Date(AfromDate.replace(/\-/g, "\/"));
			var d2 = new Date(AtoDate.replace(/\-/g, "\/"));
			if(Date.parse(d1) - Date.parse(d2)>0){
				alert(AfromDate+"晚於"+AtoDate);
				return false;
			}
		}else if(AfromDate != "" || AtoDate != ""){
			alert("請輸入完整的日期範圍值！");
			return false;
		}
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
List<BookSupplier> supplierList = null;
if(session.getAttribute("supplierList") != null){
	supplierList = (List)session.getAttribute("supplierList");
}
List purchasingBookList = null;
if(request.getAttribute("list") != null){
	purchasingBookList = (List)request.getAttribute("list");
}
%>
<h2>代購圖書</h2>
<form action="PurchasingBookServlet" method="post" name="searchForm" onsubmit="return checkForm();">
<input type="hidden" name="type" value="searchData"/>
<input type="hidden" name="param" value=""/>
<table border="0" cellpadding="0" cellspacing="0" width="660">
  <tr>
    <td colspan="2" width="150">
      <input type="radio" class="radio" name="searchType" value="A" checked="checked"/>查詢代購信息
    </td>
    <td colspan="2" width="150">
      <input type="radio" class="radio" name="searchType" value="B"/>查詢訂單
    </td>
    <td width="360" colspan="3" align="right">
      <input type="button" name="add" value="代購圖書" onclick="window.location.href='PurchasingBookServlet?type=addPage'"/>
    </td>
  </tr>
  <tr class="A">
    <td width="90">代購訂單編號：</td>
    <td>
      <input type="text" name="orderNo" class="inp"/>
    </td>
    <td width="90" align="center">日期時段由：</td>
    <td width="180">
      <input name="AfromDate" type="text" class="inp" id="AfromDate" onclick="new Calendar(null, null, 3).show(this);" readonly="readonly" size="8"/>
      至
      <input name="AtoDate" type="text" class="inp" id="AtoDate" onclick="new Calendar(null, null, 3).show(this);" readonly="readonly" size="8"/>
    </td>
    <td colspan="3">
      <input type="submit" name="submitBtn" value="確 定"/>
    </td>
  </tr>
  <tr class="B">
    <td>開始日期：</td>
    <td>
      <input name="BfromDate" type="text" class="inp" id="BfromDate" onclick="new Calendar(null, null, 3).show(this);" readonly="readonly" size="8"/>
    </td>
    <td>結束日期：</td>
    <td>
      <input name="BtoDate" type="text" class="inp" id="BtoDate" onclick="new Calendar(null, null, 3).show(this);" readonly="readonly" size="8"/>
    </td>
    <td>書商：</td>
    <td>
      <select name="supplierNo" id="supplierNo">
        <option value="0">==請選擇==</option>
        <%
        if(supplierList != null && !supplierList.isEmpty()){
        	for(BookSupplier supplier : supplierList){
        		%>
        		<option value="<%=supplier.getSupplierNo() %>"><%=supplier.getSupplierName() %></option>
        		<%
        	}
        }
        %>
        <option value="OTHER">其他</option>
      </select>
    </td>
    <td>
      <input type="button" name="queryBtn" value="查 詢" onclick="submitForm('N');"/>
      <input type="button" name="exportBtn" value="發出訂單" onclick="submitForm('Y');"/>
    </td>
  </tr>
</table>
</form>
<form action="" method="post" name="forwardForm">
<input type="hidden" name="type" value="forwardPage"/>
<input type="hidden" name="orderNo" value=""/>
<table width="99%" border="0" cellspacing="1" cellpadding="0">
<%
HashSet set = new HashSet();
if(purchasingBookList != null && !purchasingBookList.isEmpty()){
	%>
  <tr bgcolor="#C6D6FD">
    <th height="25">學期</th>
    <th>代購編號</th>
    <th>書商</th>
    <th>代購日期</th>
    <th>創建用戶</th>
    <th>操作</th>
  </tr>
	<%
	for(int i=0; i<purchasingBookList.size(); i++){
		BookPurchasing bookPurchasing = (BookPurchasing)purchasingBookList.get(i);
		if(!set.contains(bookPurchasing.getOrderNo())){
			set.add(bookPurchasing.getOrderNo());
			%>
  <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td height="22" align="center"><%=bookPurchasing.getIntake() %></td>
    <td><%=bookPurchasing.getOrderNo() %></td>
    <td><%=bookPurchasing.getOrderPp() %></td>
    <td align="center"><%=bookPurchasing.getOrderDate() %></td>
    <td align="center"><%=bookPurchasing.getCreUid() %></td>
    <td align="center"><input type="button" name="editBtn" value="編輯" class="inp" onclick="forwardPage('<%=bookPurchasing.getOrderNo() %>');"/></td>
  </tr>		
			<%
		}
	}
}
%>
<%
if(request.getAttribute("searchResult") != null){
	%>
  <tr>
    <td colspan="6"><%=request.getAttribute("searchResult") %></td>
  </tr>
	<%
}
%>
</table>
</form>
</body>
</html>