<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.text.*,edu.must.tos.bean.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/timeout.js"></script>
<script type="text/javascript" src="js/tos.js"></script>
<title>圖書庫存管理</title>
<style type="text/css">
<!--
input{
	height: 18px;
}
.isbnForm{
	margin: 5px 0px 5px 0px;
}
.tableInfo{
	margin: 10px 0px 0px 0px;
}
.view{
	border:0;
	background:#F5F2DA
}
-->
</style>
<script language="javascript">
$(document).ready(function(){
	var focusOn = $('#focusOn').val();
	
	var size = $('#size').val();
	if(focusOn == "N" && size == 0){
		$('#prNum').focus();
	}else if(focusOn == "Y" && size == 0){
		$('#isbn').focus();
	}else if(focusOn == "Y" && size != 0){
		var i = size - 1;
		document.forms["resultForm"].elements["purchasePrice"+i].select();
	}
	
	$('input[@name^=cancle]').hide();
	$('input[@name^=save]').hide();
	
	$('input[@name^=adjnum]').hide();
	
	$('input[@name^=sPurchasePrice]').addClass("view");
	$('input[@name^=sDiscount]').addClass("view");
	$('input[@name^=sCostPrice]').addClass("view");
	$('input[@name^=sRemarks]').addClass("view");
	
	$('input[@name^=sPurchasePrice]').attr("readonly", "readonly");
	$('input[@name^=sDiscount]').attr("readonly", "readonly");
	$('input[@name^=sCostPrice]').attr("readonly", "readonly");
	$('input[@name^=sRemarks]').attr("readonly", "readonly");
	
	$('.isbnForm').submit(function(){
		var prDate = $('#prDate').val();
		var prNum = $('#prNum').val();
		var isbn = $('#isbn').val();
		if(prNum == ""){
			alert("入貨單編號不能為空！");
			$('#prNum').focus();
			return false;
		}
		if(prDate == ""){
			alert("入貨日期不能為空！");
			$('#prDate').focus();
			return false;
		}
		if(isbn == ""){
			alert("圖書編號不能為空！");
			$('#isbn').focus();
			return false;
		}
		$('#prNumParam').attr("value", prNum);
		var paidCurrency = $('#paidCurrency').val();
		var paidDate = $('#paidDate').val();
		var paidStatus = $('#paidStatus').val();
		var supplierNo = $('#supplierNo').val();
		var invoiceDate = $('#invoiceDate').val();
		$.post(
			"BookInventoryServlet",
			{
				type: "saveSession",
				prDate: prDate,
				prNum: prNum,
				paidCurrency: paidCurrency,
				paidDate: paidDate,
				paidStatus: paidStatus,
				supplierNo: supplierNo,
				invoiceDate: invoiceDate
			},
			function(){}
		)
	})
	
	$('.submit').click(function(){
		var prDate = $('#prDate').val();
		var prNum = $('#prNum').val();
		var supplierNo = $('#supplierNo').val();
		var paidStatus = $('#paidStatus').val();
		var paidDate = $('#paidDate').val();
		var paidCurrency = $('#paidCurrency').val();
		var invoiceDate = $('#invoiceDate').val();
		var searchSize = $('#searchSize').val();
		var temp = 1;
		$('.stockIn').each(function(){
			var quantity = $(this).val();
			if(quantity == 0 || quantity == "" || quantity == "-"){
				alert("請檢查入貨數量填寫是否完整，後保存記錄！");
				temp = 0;
				return false;
			}
		})
		$('.costPrice').each(function(){
			var costPrice = $(this).val();
			if(costPrice == ""){
				alert("請檢查入貨價填寫是否完整，後保存記錄！");
				temp = 0;
				return false;
			}
		})
		if(temp == 1){
			if(confirm("是否確定保存該批入貨記錄？")){
				$.post(
					"BookInventoryServlet",
					{
						type: "saveRecords",
						prDate: prDate,
						prNum: prNum,
						supplierNo: supplierNo,
						paidStatus: paidStatus,
						paidDate: paidDate,
						paidCurrency: paidCurrency,
						invoiceDate: invoiceDate,
						searchSize: searchSize
					},
					function(result){
						if(result == 1){
							alert("該入貨記錄保存成功！");
							window.location.href="BookInventoryServlet?type=cleanSession";
						}else{
							alert("該入貨記錄保存失敗！");
						}
					}
				)
			}
		}
	})
})

function openEdit(i){
	$('input[@name=edit'+i+']').hide();
	$('input[@name=cancle'+i+']').show();
	$('input[@name=save'+i+']').show();
	
	$('input[@name=adjnum'+i+']').show();
	
	$('input[@name=sPurchasePrice'+i+']').removeClass("view");
	$('input[@name=sDiscount'+i+']').removeClass("view");
	$('input[@name=sCostPrice'+i+']').removeClass("view");
	$('input[@name=sRemarks'+i+']').removeClass("view");
	
	$('input[@name=sPurchasePrice'+i+']').attr("readonly", "");
	$('input[@name=sDiscount'+i+']').attr("readonly", "");
	$('input[@name=sCostPrice'+i+']').attr("readonly", "");
	$('input[@name=sRemarks'+i+']').attr("readonly", "");
}

function closeEdit(i){
	var oldSPurchasePrice = $('input[@name=oldSPurchasePrice'+i+']').val();
	var oldSDiscount = $('input[@name=oldSDiscount'+i+']').val();
	var oldSCostPrice = $('input[@name=oldSCostPrice'+i+']').val();
	var oldSRemarks = $('input[@name=oldSRemarks'+i+']').val();
	var oldAdjnum = $('input[@name=oldAdjnum'+i+']').val();
	
	$('input[@name=sPurchasePrice'+i+']').addClass("view");
	$('input[@name=sDiscount'+i+']').addClass("view");
	$('input[@name=sCostPrice'+i+']').addClass("view");
	$('input[@name=sRemarks'+i+']').addClass("view");
	
	$('input[@name=sPurchasePrice'+i+']').attr("readonly", "readonly");
	$('input[@name=sDiscount'+i+']').attr("readonly", "readonly");
	$('input[@name=sCostPrice'+i+']').attr("readonly", "readonly");
	$('input[@name=sRemarks'+i+']').attr("readonly", "readonly");
	
	$('input[@name=sPurchasePrice'+i+']').attr("value", oldSPurchasePrice);
	$('input[@name=sDiscount'+i+']').attr("value", oldSDiscount);
	$('input[@name=sCostPrice'+i+']').attr("value", oldSCostPrice);
	$('input[@name=sRemarks'+i+']').attr("value", oldSRemarks);
	
	$('input[@name=adjnum'+i+']').hide();
	$('input[@name=adjnum'+i+']').attr("value", '');
	
	$('input[@name=edit'+i+']').show();
	$('input[@name=cancle'+i+']').hide();
	$('input[@name=save'+i+']').hide();
}

function saveEdit(i){
	var no = $('input[@name=sNo'+i+']').val();
	var prNum = $('input[@name=sPrNum'+i+']').val();
	var isbn = $('input[@name=sIsbn'+i+']').val();
	var sPurchasePrice = $('input[@name=sPurchasePrice'+i+']').val();
	var sDiscount = $('input[@name=sDiscount'+i+']').val();
	var sCostPrice = $('input[@name=sCostPrice'+i+']').val();
	var sRemarks = $('input[@name=sRemarks'+i+']').val();
	var adjnum = $('input[@name=adjnum'+i+']').val();
	var oldAdjnum = $('input[@name=oldAdjnum'+i+']').val();
	if(adjnum != ""){
		if(/^(\-?)(\d+)$/.test(adjnum)){
			
		}else{
			alert("修改入貨數格式不正確！");
			document.forms["resultForm"].elements["adjnum"+i].value = "";
			document.forms["resultForm"].elements["adjnum"+i].select();
			return false;
		}
	}
	$.post(
		"BookInventoryServlet",
		{
			type: "updateStockIn",
			no: no,
			prNum: prNum,
			isbn: isbn,
			sPurchasePrice: sPurchasePrice,
			sDiscount: sDiscount,
			sCostPrice: sCostPrice,
			sRemarks: sRemarks,
			i: i,
			adjnum: adjnum,
			oldAdjnum: oldAdjnum
		},
		function(result){
			if(result == "true"){
				alert("資料修改成功！");
				$('input[@name=sPurchasePrice'+i+']').addClass("view");
				$('input[@name=sDiscount'+i+']').addClass("view");
				$('input[@name=sCostPrice'+i+']').addClass("view");
				$('input[@name=sRemarks'+i+']').addClass("view");
				
				$('input[@name=sPurchasePrice'+i+']').attr("readonly", "readonly");
				$('input[@name=sDiscount'+i+']').attr("readonly", "readonly");
				$('input[@name=sCostPrice'+i+']').attr("readonly", "readonly");
				$('input[@name=sRemarks'+i+']').attr("readonly", "readonly");
				
				$('input[@name=oldSPurchasePrice'+i+']').attr("value", sPurchasePrice);
				$('input[@name=oldSDiscount'+i+']').attr("value", sDiscount);
				$('input[@name=oldSCostPrice'+i+']').attr("value", sCostPrice);
				$('input[@name=oldSRemarks'+i+']').attr("value", sRemarks);
				
				$('input[@name=adjnum'+i+']').attr("value", "");
				$('input[@name=adjnum'+i+']').hide();
				
				$('input[@name=edit'+i+']').show();
				$('input[@name=cancle'+i+']').hide();
				$('input[@name=save'+i+']').hide();
			}else if(result == "false"){
				alert("資料修改失敗！");
				var oldSPurchasePrice = $('input[@name=oldSPurchasePrice'+i+']').val();
				var oldSDiscount = $('input[@name=oldSDiscount'+i+']').val();
				var oldSCostPrice = $('input[@name=oldSCostPrice'+i+']').val();
				var oldSRemarks = $('input[@name=oldSRemarks'+i+']').val();
				
				$('input[@name=sPurchasePrice'+i+']').addClass("view");
				$('input[@name=sDiscount'+i+']').addClass("view");
				$('input[@name=sCostPrice'+i+']').addClass("view");
				$('input[@name=sRemarks'+i+']').addClass("view");
				
				$('input[@name=sPurchasePrice'+i+']').attr("readonly", "readonly");
				$('input[@name=sDiscount'+i+']').attr("readonly", "readonly");
				$('input[@name=sCostPrice'+i+']').attr("readonly", "readonly");
				$('input[@name=sRemarks'+i+']').attr("readonly", "readonly");
				
				$('input[@name=sPurchasePrice'+i+']').attr("value", oldSPurchasePrice);
				$('input[@name=sDiscount'+i+']').attr("value", oldSDiscount);
				$('input[@name=sCostPrice'+i+']').attr("value", oldSCostPrice);
				$('input[@name=sRemarks'+i+']').attr("value", oldSRemarks);
				
				$('input[@name=adjnum'+i+']').attr("value", "");
				$('input[@name=adjnum'+i+']').hide();
				
				$('input[@name=edit'+i+']').show();
				$('input[@name=cancle'+i+']').hide();
				$('input[@name=save'+i+']').hide();
			}else if(result == "negative"){
				alert("修改入貨數量導致該圖書出現負數！");
				var oldSPurchasePrice = $('input[@name=oldSPurchasePrice'+i+']').val();
				var oldSDiscount = $('input[@name=oldSDiscount'+i+']').val();
				var oldSCostPrice = $('input[@name=oldSCostPrice'+i+']').val();
				var oldSRemarks = $('input[@name=oldSRemarks'+i+']').val();
				
				$('input[@name=sPurchasePrice'+i+']').addClass("view");
				$('input[@name=sDiscount'+i+']').addClass("view");
				$('input[@name=sCostPrice'+i+']').addClass("view");
				$('input[@name=sRemarks'+i+']').addClass("view");
				
				$('input[@name=sPurchasePrice'+i+']').attr("readonly", "readonly");
				$('input[@name=sDiscount'+i+']').attr("readonly", "readonly");
				$('input[@name=sCostPrice'+i+']').attr("readonly", "readonly");
				$('input[@name=sRemarks'+i+']').attr("readonly", "readonly");
				
				$('input[@name=sPurchasePrice'+i+']').attr("value", oldSPurchasePrice);
				$('input[@name=sDiscount'+i+']').attr("value", oldSDiscount);
				$('input[@name=sCostPrice'+i+']').attr("value", oldSCostPrice);
				$('input[@name=sRemarks'+i+']').attr("value", oldSRemarks);
				
				$('input[@name=adjnum'+i+']').hide();
				$('input[@name=adjnum'+i+']').attr("value", '');
				
				$('input[@name=edit'+i+']').show();
				$('input[@name=cancle'+i+']').hide();
				$('input[@name=save'+i+']').hide();
			}else{
				alert("資料修改成功！");
				window.location.href = "BookInventoryServlet?type=searchPrNum&prNum="+prNum;
			}
		}
	)
}

function remarkInfo(i){
	var remarks = $('#remarks'+i).val();
	var purchasePrice = $('#purchasePrice'+i).val();
	var discount = $('#discount'+i).val();
	var costPrice = $('#costPrice'+i).val();
	$.post(
		"BookInventoryServlet",
		{
			type: "sessionRemarks",
			i: i,
			remarks: remarks,
			purchasePrice: purchasePrice,
			discount: discount,
			costPrice: costPrice
		},
		function(){}
	)
}

function checkAdjnum(value, i){
	if(value.indexOf("-") < 0){
	
	}else{
		
	}
}

function checkStockIn(stock, i){
	var stockIn = 0;
	if(stock.length == 0){
		alert("入貨數量不能為空，請輸入數量！");
		document.forms["resultForm"].elements["stockIn"+i].value = 0;
		document.forms["resultForm"].elements["stockIn"+i].select();
		return false;
	}else{
		if(stock.substring(0,1) == "-"){
			if(isNaN(stock.substring(2, stock.length))){
				alert("入貨數量格式不符合，請重新輸入！");
				document.forms["resultForm"].elements["stockIn"+i].value = 0;
				document.forms["resultForm"].elements["stockIn"+i].select();
				return false;
			}else{
				if(stock.substring(1, 2) == "0"){
					document.forms["resultForm"].elements["stockIn"+i].value = "-" ;
				}
			}
			var number = "0123456789";
			for(j=0; j<stock.substring(1, stock.length).length; j++){
				var point = stock.substring(1, stock.length).charAt(j);
				if(number.indexOf(point) == -1){
					alert("請輸入正確的入貨數量！");
					document.forms["resultForm"].elements["stockIn"+i].value = 0;
					document.forms["resultForm"].elements["stockIn"+i].select();
					return false;
				}
			}
			stockIn = stock;
		}else{
			if(isNaN(stock)){
				alert("isNaN");
				alert("入貨數量格式不符合，請重新輸入！");
				document.forms["resultForm"].elements["stockIn"+i].value = 0;
				document.forms["resultForm"].elements["stockIn"+i].select();
				return false;
			}else{
				if(stock.substring(0, 1) == 0){
					if(stock.length == 1){
						document.forms["resultForm"].elements["stockIn"+i].value = 0;
					}else {
						document.forms["resultForm"].elements["stockIn"+i].value = stock.substring(1, stock.length);
					}
					document.forms["resultForm"].elements["stockIn"+i].select();
				}
			}
			var number = "0123456789";
			for(j=0; j<stock.length; j++){
				var point = stock.charAt(j);
				if(number.indexOf(point) == -1){
					alert("請輸入正確的入貨數量！");
					document.forms["resultForm"].elements["stockIn"+i].value = 0;
					document.forms["resultForm"].elements["stockIn"+i].select();
					return false;
				}
			}
			stockIn = stock;
		}
		$.post(
			"BookInventoryServlet",
			{
				type:"sessionAdjnum",
				i:i,
				adjnum:stockIn
			},
			function(){}
		)
	}
}

function calculateSPrice(value, e1, e2, i){
	document.forms["resultForm"].elements["sCostPrice"+i].value = 0;
	var element = document.forms["resultForm"].elements[e2+""+i].value ;
	if(value != ""){
		if(isNaN(value)){
			alert("請輸入正確的數字！");
			document.forms["resultForm"].elements["sCostPrice"+i].value = 0;
			document.forms["resultForm"].elements[e1+""+i].value = 0;
			document.forms["resultForm"].elements[e1+""+i].select() ;
			return false;
		}
	}else{
		value = 0;
	}
	if(element != ""){
		if(isNaN(element)){
			alert("請輸入正確的數字！");
			document.forms["resultForm"].elements["sCostPrice"+i].value = 0;
			document.forms["resultForm"].elements[e2+""+i].value = 0;
			document.forms["resultForm"].elements[e2+""+i].select() ;
			return false;
		}
	}else{
		element = 0;
	}
	var costPrice = accMul(element, value);
	document.forms["resultForm"].elements["sCostPrice"+i].value = costPrice;
}

function writeSCostPrice(i){
	var costPrice = document.forms["resultForm"].elements["sCostPrice"+i].value;
	var purchasePrice = document.forms["resultForm"].elements["sPurchasePrice"+i].value;
	var discount = document.forms["resultForm"].elements["sDiscount"+i].value;
	if(costPrice != ""){
		if(isNaN(costPrice)){
			alert("請輸入正確的數字！");
			if(purchasePrice!="" && discount!=""){
				document.forms["resultForm"].elements["sCostPrice"+i].value = accMul(purchasePrice, discount);
			}else{
				document.forms["resultForm"].elements["sCostPrice"+i].value = 0;
			}
			return false;
		}
	}
}

function calculatePrice(value, e1, e2, i){
	document.forms["resultForm"].elements["costPrice"+i].value = 0;
	var element = document.forms["resultForm"].elements[e2+""+i].value ;
	if(value != ""){
		if(isNaN(value)){
			alert("請輸入正確的數字！");
			document.forms["resultForm"].elements["costPrice"+i].value = 0;
			document.forms["resultForm"].elements[e1+""+i].value = 0;
			document.forms["resultForm"].elements[e1+""+i].select() ;
			return false;
		}
	}else{
		value = 0;
	}
	if(element != ""){
		if(isNaN(element)){
			alert("請輸入正確的數字！");
			document.forms["resultForm"].elements["costPrice"+i].value = 0;
			document.forms["resultForm"].elements[e2+""+i].value = 0;
			document.forms["resultForm"].elements[e2+""+i].select() ;
			return false;
		}
	}else{
		element = 0;
	}
	var costPrice = accMul(element, value);
	document.forms["resultForm"].elements["costPrice"+i].value = costPrice;
}
//重寫js乘法運算
function accMul(arg1, arg2){
    var m = 0,s1 = arg1.toString(), s2 = arg2.toString();
    try{m += s1.split(".")[1].length}catch(e){}
    try{m += s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".","")) * Number(s2.replace(".", ""))/Math.pow(10,m);
}

function writeCostPrice(i){
	var costPrice = document.forms["resultForm"].elements["costPrice"+i].value;
	var purchasePrice = document.forms["resultForm"].elements["purchasePrice"+i].value;
	var discount = document.forms["resultForm"].elements["discount"+i].value;
	if(costPrice != ""){
		if(isNaN(costPrice)){
			alert("請輸入正確的數字！");
			if(purchasePrice!="" && discount!=""){
				document.forms["resultForm"].elements["costPrice"+i].value = accMul(purchasePrice, discount);
			}else{
				document.forms["resultForm"].elements["costPrice"+i].value = 0;
			}
			return false;
		}
	}
}
function searchStockIn(){
	var prNum = $('#prNum').val();
	if(prNum == ""){
		alert("請輸入入貨單編號進行查詢！");
		return false;
	}else{
		window.location.href = "BookInventoryServlet?type=searchPrNum&prNum="+prNum;
	}
}
function checkNum(value, e, i){
	if(value != ""){
		if(isNaN(value)){
			alert("請輸入正確的數字！");
		}
	}
}
function focusIsbn(){
	$('#isbn').focus();
}

function selectItem(name, i){
	document.resultForm.elements[name+""+i].select();
}
</script>
<% if (session.getAttribute("userId") == null) {%>
	<script>
		alert('登陸超時！請重新登陸');
		window.parent.location.href='login.jsp';
    </script>
<% } %>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
int mark = 0;
if(session.getAttribute("search")!=null && session.getAttribute("search").equals("search")){
	mark = 1;
}
List<BookSupplier> supplierList = null;
if(request.getAttribute("supplierList")!=null){
	supplierList = (List)request.getAttribute("supplierList");
}
BookStockInOrder bookStockInOrder = null;
String prDate = null;
String searchPrNum = "";
if(request.getAttribute("searchPrNum")!=null){
	searchPrNum = (String)request.getAttribute("searchPrNum");
}
String prNum = null;
Integer supplierNo = 0;
String paidStatus = null;

Date now = new Date();
SimpleDateFormat dealtimeFormat = new SimpleDateFormat("yyyy-MM-dd");
String paidDate = "";
String paidCurrency = null;
String invoiceDate = null;
if(session.getAttribute("bookStockInOrder")!=null){
	bookStockInOrder = (BookStockInOrder)session.getAttribute("bookStockInOrder");
	if(bookStockInOrder.getInDate()!=null){
		prDate = bookStockInOrder.getInDate();
	}
	if(bookStockInOrder.getPrnum()!=null){
		prNum = bookStockInOrder.getPrnum();
	}
	if(bookStockInOrder.getPrnum()!=null && bookStockInOrder.getSupplierNo()!=0){
		supplierNo = bookStockInOrder.getSupplierNo();
	}
	if(bookStockInOrder.getPaidstatus()!=null){
		paidStatus = bookStockInOrder.getPaidstatus();
	}
	if(bookStockInOrder.getPaidDate()!=null){
		paidDate = bookStockInOrder.getPaidDate();
	}
	if(bookStockInOrder.getPaidcurrency()!=null){
		paidCurrency = bookStockInOrder.getPaidcurrency();
	}
	if(bookStockInOrder.getInvoiceDate()!=null){
		invoiceDate = bookStockInOrder.getInvoiceDate();
	}
}
%>
<table border="0" width="90%" cellpadding="0" cellspacing="0" class="tableInfo">
  <tr valign="middle">
    <td width="10%" height="28">入貨日期：</td>
    <td width="15%">
      <input type="hidden" name="focusOn" id="focusOn" value="<%if(prDate==null){out.print("N");}else{out.print("Y");} %>">
      <input name="prDate" type="text" id="prDate" onClick="new Calendar(null, null, 3).show(this);" value="<%if(prDate==null){out.print(dealtimeFormat.format(now));}else{out.print(prDate);} %>" size="9" readonly="readonly"/>
    </td>
    <td width="10%">入貨單編號：</td>
    <td width="28%">
    <%
    if(prNum == null){
    %>
	  <input type="text" name="prNum" id="prNum" value="<%=searchPrNum %>">
	<%
	}else{
		out.print(prNum);
	%>
	  <input type="hidden" name="prNum" id="prNum" value="<%=prNum %>">
	<%
	}
    %>
      <img src="images/search.gif" height="15" width="15" border="0" onClick="searchStockIn();" style="cursor:hand">
    </td>
    <td width="8%" align="center">書商：</td>
    <td width="29%">
      <select name="supplierName" id="supplierNo">
        <option value="">==請選擇==</option>
        <%
        if(supplierList!=null && !supplierList.isEmpty()){
        	for(BookSupplier s : supplierList){
        %>
        <option value="<%=s.getSupplierNo() %>" <%if(supplierNo!=0 && String.valueOf(supplierNo).equals(String.valueOf(s.getSupplierNo()))){out.print("selected");} %>><%=s.getSupplierName() %></option>
        <%
        	}
        }
        %>
      </select>
    </td>
  </tr>
  <tr>
    <td height="28">付款狀態：</td>
    <td>
      <select name="paidStatus" id="paidStatus">
        <option value="N" <%if(paidStatus!=null && "N".equals(paidStatus)){out.print("selected");} %>>待付</option>
        <option value="Y" <%if(paidStatus!=null && "Y".equals(paidStatus)){out.print("selected");} %>>已付</option>
      </select>
    </td>
    <td>付款日期：</td>
    <td>
      <input name="paidDate" type="text" id="paidDate" onClick="new Calendar(null, null, 3).show(this);" value="<%=paidDate %>" size="9" readonly="readonly"/>
    </td>
    <td>付款幣種：</td>
    <td>
      <select name="paidCurrency" id="paidCurrency">
        <option value="" >==請選擇==</option>
        <option value="MOP" <%if(paidCurrency!=null && "MOP".equals(paidCurrency)){out.print("selected");} %>>葡幣</option>
        <option value="RMB" <%if(paidCurrency!=null && "RMB".equals(paidCurrency)){out.print("selected");} %>>人民幣</option>
        <option value="HKD" <%if(paidCurrency!=null && "HKD".equals(paidCurrency)){out.print("selected");} %>>港幣</option>
        <option value="USD" <%if(paidCurrency!=null && "USD".equals(paidCurrency)){out.print("selected");} %>>美元</option>
      </select>
    </td>
  </tr>
  <tr>
    <td height="28">發票日期：</td>
    <td colspan="5">
      <input name="invoiceDate" type="text" id="invoiceDate" onClick="new Calendar(null, null, 3).show(this);" value="<%if(invoiceDate==null){out.print("");}else{out.print(invoiceDate);} %>" size="9" readonly="readonly"/>
    </td>
  </tr>
</table>
<form action="BookInventoryServlet" method="post" name="isbnForm" class="isbnForm">
  <table border="0" width="70%" cellpadding="0" cellspacing="0">
    <tr>
      <td width="9%">ISBN:</td>
      <td width="61%">
        <input type="text" name="isbn" id="isbn">&nbsp;
        <input type="hidden" name="mark" id="mark" value="<%=mark %>">
        <input type="hidden" name="prNumParam" id="prNumParam" value="">
        <input type="submit" value="確定" style="height:22px;">
        <%
        String wrong = "";
        if(request.getAttribute("wrong")!=null){
        	wrong = (String)request.getAttribute("wrong");
        	out.print(wrong);
        }
        %>
      </td>
    </tr>
  </table>
</form>
<%
List searchInfoList = (List)session.getAttribute("searchInfoList");

int searchSize = 0;
int size = 0;
List infoList = (List)session.getAttribute("infoList");
if((infoList==null || infoList.size()==0) && (searchInfoList==null || searchInfoList.size()==0)){
%>
<hr>
<input type="hidden" name="size" id="size" value="<%=size %>">
<font style="font-size:12px;">還沒有錄入圖書信息，請輸入ISBN編號進行入貨！</font>
<%
}else{
	if(infoList!=null && !infoList.isEmpty()){
		size = infoList.size();
	}
	if(searchInfoList!=null && !searchInfoList.isEmpty()){
		searchSize = searchInfoList.size();
	}
%>
<input type="hidden" name="searchSize" id="searchSize" value="<%=searchSize %>">
<input type="hidden" name="size" id="size" value="<%=size %>">
<form action="" method="post" name="resultForm" id="resultForm">
  <table border="0" cellpadding="0" cellspacing="0" width="99.9%">
    <tr align="center">
	  <td width="15%" height="28" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">圖書編號</td>
	  <td width="22%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">圖書名稱</td>
	  <td width="6%"  style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">定價</td>
	  <td width="6%"  style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">折扣</td>
	  <td width="7%"  style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">入貨價</td>
	  <td width="15%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">入貨數</td>
	  <td width="17%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">備註</td>
	  <td width="12%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">操作</td>
	</tr>
	<%
	if(searchInfoList!=null && !searchInfoList.isEmpty()){
		for(int j=searchInfoList.size()-1; j>=0; j--){
			BookStockInBean searchInfo = new BookStockInBean();
			searchInfo = (BookStockInBean)searchInfoList.get(j);
			String remarks = searchInfo.getRemarks();
			if(remarks==null){
				remarks = "";
			}
	%>
	<tr>
	  <td height="25" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <%=searchInfo.getIsbn() %>
	    <input type="hidden" name="sNo<%=j %>" value="<%=searchInfo.getBookStockIn().getNo() %>">
	    <input type="hidden" name="sIsbn<%=j %>" value="<%=searchInfo.getIsbn() %>">
	    <input type="hidden" name="sPrNum<%=j %>" value="<%=searchInfo.getPrNum() %>">
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <%=searchInfo.getBook().getTitle() %>
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" value="<%=searchInfo.getPurchasePrice() %>" name="oldSPurchasePrice<%=j %>" >
	    <input type="text" value="<%=searchInfo.getPurchasePrice() %>" name="sPurchasePrice<%=j %>" id="sPurchasePrice<%=j %>" onKeyUp="calculateSPrice(this.value, 'sPurchasePrice', 'sDiscount', '<%=j %>');" style="width: 40px" onFocus="selectItem('sPurchasePrice', '<%=j %>');">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" value="<%=searchInfo.getDiscount() %>" name="oldSDiscount<%=j %>" >
	    <input type="text" value="<%=searchInfo.getDiscount() %>" name="sDiscount<%=j %>" id="sDiscount<%=j %>" onKeyUp="calculateSPrice(this.value, 'sDiscount', 'sPurchasePrice', '<%=j %>');" style="width: 40px" onFocus="selectItem('sDiscount', '<%=j %>');">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" value="<%=searchInfo.getCostPrice() %>" name="oldSCostPrice<%=j %>" >
	    <input type="text" value="<%=searchInfo.getCostPrice() %>" name="sCostPrice<%=j %>" id="sCostPrice<%=j %>" onkeyup="writeSCostPrice('<%=j %>');" value="0" style="width: 40px">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <%=searchInfo.getAdjNum() %>
	    <input type="hidden" value="<%=searchInfo.getAdjNum() %>" name="oldAdjnum<%=j %>">
	    <input type="text" name="adjnum<%=j %>" id="adjnum<%=j %>" onKeyPress="return NumOnly(event)" onKeyUp="checkAdjnum(this.value, '<%=j %>');" style="width: 40px" maxlength="4">
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" value="<%=remarks %>" name="oldSRemarks<%=j %>" >
	    <input type="text" value="<%=remarks %>" name="sRemarks<%=j %>" id="sRemarks<%=j %>" style="width: 160px" maxlength="50">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="button" name="edit<%=j %>" value="修改" onClick="openEdit('<%=j %>');">
	    <input type="button" name="save<%=j %>" value="保存" onClick="saveEdit('<%=j %>');">
	    <input type="button" name="cancle<%=j %>" value="取消" onClick="closeEdit('<%=j %>');">
	  </td>
	</tr>
	<%
		}
	}
	if(infoList!=null && !infoList.isEmpty()){
		for(int i=infoList.size()-1; i>=0; i--){
			BookInventoryInfo info = new BookInventoryInfo();
			info = (BookInventoryInfo)infoList.get(i);
	%>
	<tr>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <%=info.getIsbn() %>
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <%=info.getBook().getTitle() %>
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input value="<%=info.getPurchasePrice() %>" name="purchasePrice<%=i %>" id="purchasePrice<%=i %>" onBlur="remarkInfo('<%=i %>');" onKeyUp="calculatePrice(this.value, 'purchasePrice', 'discount', '<%=i %>');" style="width: 40px" onFocus="selectItem('purchasePrice', '<%=i %>');">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input value="<%=info.getDiscount() %>" name="discount<%=i %>" id="discount<%=i %>" onBlur="remarkInfo('<%=i %>');" onKeyUp="calculatePrice(this.value, 'discount', 'purchasePrice', '<%=i %>');" style="width: 40px" onFocus="selectItem('discount', '<%=i %>');">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input value="<%=info.getCostPrice() %>" name="costPrice<%=i %>" class="costPrice" id="costPrice<%=i %>" onblur="remarkInfo('<%=i %>');" onkeyup="writeCostPrice('<%=i %>');" value="0" style="width: 40px">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="text" name="stockIn<%=i %>" class="stockIn" id="stockIn<%=i %>" value="<%=info.getAdjnum() %>" onKeyUp="checkStockIn(this.value, '<%=i %>');" style="width: 40px;" maxlength="5" onFocus="selectItem('stockIn', '<%=i %>');">
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="text" name="remarks<%=i %>" id="remarks<%=i %>" value="<%=info.getRemarks()==null?"":info.getRemarks() %>" maxlength="50" onKeyUp="remarkInfo('<%=i %>');" style="width: 160px">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="button" value="刪除" onClick="window.location.href='BookInventoryServlet?type=remove&i=<%=i %>'" style="height: 22px">
	  </td>
	</tr>
	<%
		}
	}
	%>
	<tr>
	  <td colspan="8" height="30" valign="bottom">
	    <input type="button" value="保 存" class="submit" style="height: 22px;">
	  </td>
	</tr>
  </table>
</form>
<%
}
%>
<label id="err"></label>
</body>
</html>
