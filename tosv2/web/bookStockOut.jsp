<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.text.*,edu.must.tos.bean.*" %>
<%@page import="edu.must.tos.util.ToolsOfString"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/tos.js"></script>
<title>圖書出庫管理</title>
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
//查看學員詳細信息
function openDetail(theURL, winName, features) {
	window.open(theURL, winName, features);
}

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
		var paidCurrency = $('#paidCurrency').val();
		if(prDate == ""){
			alert("出貨日期不能為空！");
			$('#prDate').focus();
			return false;
		}
		if(isbn == ""){
			alert("圖書編號不能為空！");
			$('#isbn').focus();
			return false;
		}
		$('#prNumParam').attr("value", prNum);
		var supplierNo = $('#supplierNo').val();
		if(supplierNo == ""){
			alert("學院/書商不能為空！");
			return false;
		}
		$.post(
			"BookStockOutServlet",
			{
				type: "saveSession",
				prDate: prDate,
				prNum: prNum,
				paidCurrency: paidCurrency,
				supplierNo: supplierNo
			},
			function(){}
		)
	})
	
	$('.purchaseOutSubmit').click(function(){
		var prDate = $('#prDate').val();
		var prNum = $('#prNum').val();
		var supplierNo = $('#supplierNo').val();
		var paidCurrency = $('#paidCurrency').val();
		var checkFlag = true;
		var i = 0;
		$('input[@name=isCheck][@checked]').each(function(){
			i++;
			var id = $(this).val();
			var isbn = $('input[@name=sIsbn'+id+']').val();
			var pStockOut = $('#pStockOut'+id).val();
			if(pStockOut == ""){
				alert("請輸入完整的出貨數！");
				$('#pStockOut'+id).focus();
				checkFlag = false;
				return false;
			}
			if(checkFlag){
				var pDate = $('#pDate'+id).val();
				if(pDate == ""){
					alert("請輸入完整的出貨日期！");
					$('#pDate'+id).focus();
					checkFlag = false;
					return false;
				}
			}
		})
		if(i == 0){
			alert("請勾選需要出貨的記錄！");
			return false;
		}
		if(checkFlag){
			if(confirm("是否確定保存該批出貨記錄？")){
				var idStr = "";
				var pStockOutStr = "";
				var pDateStr = "";
				var purchasePriceStr = "";
				var disCountStr = "";
				var costPriceStr = "";
				var crsCodes = "";
				var lectCodes = "";
				$('input[@name=isCheck][@checked]').each(function(){
					var id = $(this).val();
					idStr += id + ";";
					var pStockOut = $('#pStockOut'+id).val();
					pStockOutStr += pStockOut + ";";
					var pDate = $('#pDate'+id).val();
					pDateStr += pDate + ";";
					var sPurchasePrice = $('input[@name=sPurchasePrice'+id+']').val();
					purchasePriceStr += sPurchasePrice + ";"; 
					var sDiscount = $('input[@name=sDiscount'+id+']').val();
					disCountStr += sDiscount + ";";
					var sCostPrice = $('input[@name=sCostPrice'+id+']').val();
					costPriceStr += sCostPrice + ";";
					var crsCode = $('#sCrsCode'+id).val();
					crsCodes += crsCode + ";";
					var lectCode = $('#sLectCode'+id).val();
					lectCodes += lectCode + ";";
				})
				$.post(
					"BookStockOutServlet",
					{
						type: "purchaseOut",
						prDate: prDate,
						prNum: prNum,
						supplierNo: supplierNo,
						paidCurrency: paidCurrency,
						idStr: idStr,
						pStockOutStr: pStockOutStr,
						pDateStr: pDateStr,
						purchasePriceStr: purchasePriceStr,
						disCountStr: disCountStr,
						costPriceStr: costPriceStr,
						crsCodes: crsCodes,
						lectCodes: lectCodes
					},
					function(result){
						if(result!=0){
							alert("該出貨記錄保存成功！");
							//Rainbow add 
							//openDetail('bookStockOutDetailList.jsp?pStockOutStr='+pStockOutStr+'&costPriceStr='+costPriceStr+'&idStr='+idStr,'','width=800,height=600,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no')
							window.location.href = "BookStockOutServlet?type=cleanSession";
							openDetail('BookStockOutServlet?type=printStockOutList&prNum='+result,'','width=800,height=600,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no');
						}else{
							alert("該出貨記錄保存失敗！");
						}
					}
				)			
			}
		}
	})
	
	$('.submit').click(function(){
		var prDate = $('#prDate').val();
		var prNum = $('#prNum').val();
		var supplierNo = $('#supplierNo').val();
		var searchSize = $('#searchSize').val();
		var paidCurrency = $('#paidCurrency').val();
		var isp = $('input[@name=isp]').val();
		
		var temp = 1;
		$('.stockOut').each(function(){
			var quantity = $(this).val();
			if(quantity == 0 || quantity == "" || quantity == "-"){
				alert("請檢查出貨數量填寫是否完整，後保存記錄！");
				temp = 0;
				return false;
			}
		})
		$('.costPrice').each(function(){
			var costPrice = $(this).val();
			if(costPrice == ""){
				alert("請檢查出貨價填寫是否完整，後保存記錄！");
				temp = 0;
				return false;
			}
		})
		if(temp == 1){
			if(confirm("是否確定保存該批出貨記錄？")){
				$.post(
					"BookStockOutServlet",
					{
						type: "saveRecords",
						prDate: prDate,
						prNum: prNum,
						supplierNo: supplierNo,
						paidCurrency: paidCurrency,
						searchSize: searchSize
					},
					function(result){
						if(result == 1){
							alert("該出貨記錄保存成功！");
							//Rainbow add 
							//openDetail('bookStockOutDetailList.jsp','','width=800,height=600,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no');
							
							openDetail('BookStockOutServlet?type=printStockOutList&prNum='+prNum,'','width=800,height=600,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no');
							window.location.href = "BookStockOutServlet?type=cleanSession";
						}else{
							alert("該出貨記錄保存失敗！");
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
	
	$('#sCrsCode'+i).removeAttr("disabled");
	$('#sLectCode'+i).attr("disabled", false);
	$('#selectLectBtn'+i).removeAttr("disabled");
}

function closeEdit(i){
	var oldSPurchasePrice = $('input[@name=oldSPurchasePrice'+i+']').val();
	var oldSDiscount = $('input[@name=oldSDiscount'+i+']').val();
	var oldSCostPrice = $('input[@name=oldSCostPrice'+i+']').val();
	var oldSRemarks = $('input[@name=oldSRemarks'+i+']').val();
	var oldAdjnum = $('input[@name=oldAdjnum'+i+']').val();
	var oldCrsCode = $('#sOldCrsCode'+i).val();
	var oldLectCode = $('input[@name=sOldLectCode'+i+']').val();
	
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
	
	$('#sCrsCode'+i).val(oldCrsCode);
	$('#sCrsCode'+i).attr("disabled", true);
	$('#sLectCode'+i).attr("value", oldLectCode);
	$('#sLectCode'+i).attr("disabled", true);
	$('#selectLectBtn'+i).attr("disabled", true);
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
	var crsCode = $('#sCrsCode'+i).val();
	var oldCrsCode = $('#sOldCrsCode'+i).val();
	var lectCode = $('input[@name=sLectCode'+i+']').val();
	var oldLectCode = $('input[@name=sOldLectCode'+i+']').val();
	
	if(adjnum != ""){
		if(/^(\-?)(\d+)$/.test(adjnum)){
			
		}else{
			alert("修改出貨數格式不正確！");
			document.forms["resultForm"].elements["adjnum"+i].value = "";
			document.forms["resultForm"].elements["adjnum"+i].select();
			return false;
		}
	}
	$.post(
		"BookStockOutServlet",
		{
			type: "updateStockOut",
			no: no,
			prNum: prNum,
			isbn: isbn,
			sPurchasePrice: sPurchasePrice,
			sDiscount: sDiscount,
			sCostPrice: sCostPrice,
			sRemarks: sRemarks,
			i: i,
			adjnum: adjnum,
			oldAdjnum: oldAdjnum,
			crsCode: crsCode,
			lectCode: lectCode
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
				
				$('#sCrsCode'+i).attr("disabled", true);
				$('#sLectCode'+i).attr("disabled", true);
				$('#selectLectBtn'+i).attr("disabled", true);
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
				
				$('#sCrsCode'+i).val(oldCrsCode);
				$('#sCrsCode'+i).attr("disabled", true);
				$('#sLectCode'+i).attr("value", oldLectCode);
				$('#sLectCode'+i).attr("disabled", true);
				$('#selectLectBtn'+i).attr("disabled", true);
			}else if(result == "negative"){
				alert("修改出貨數量導致該圖書出現負數！");
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
				
				$('#sCrsCode'+i).val(oldCrsCode);
				$('#sCrsCode'+i).attr("disabled", true);
				$('#sLectCode'+i).attr("value", oldLectCode);
				$('#sLectCode'+i).attr("disabled", true);
				$('#selectLectBtn'+i).attr("disabled", true);
			}else{
				alert("資料修改成功！");
				window.location.href = "BookStockOutServlet?type=searchPrNum&prNum="+prNum;
			}
		}
	)
}

function remarkInfo(i){
	var remarks = $('#remarks'+i).val();
	var purchasePrice = $('#purchasePrice'+i).val();
	var discount = $('#discount'+i).val();
	var costPrice = $('#costPrice'+i).val();
	var crsCode = document.getElementById("crsCode"+i).value;
	var lectCode = $('#lectCode'+i).val();
	$.post(
		"BookStockOutServlet",
		{
			type: "sessionRemarks",
			i: i,
			remarks: remarks,
			purchasePrice: purchasePrice,
			discount: discount,
			costPrice: costPrice,
			courseCode: crsCode,
			lectCode: lectCode
		},
		function(){}
	)
}

function checkAdjnum(value, i, isbn){
	$.post(
		"BookStockOutServlet",
		{
			type: "checkOutAdjnum",
			i: i,
			isbn: isbn,
			adjnum: value,
			oldAdjnum: $('input[@name=oldAdjnum'+i+']').val()
		},
		function(result){
			if(result == 0){
				alert("修改出貨數量超出庫存數量！");
				document.forms["resultForm"].elements["adjnum"+i].value = "";
				document.forms["resultForm"].elements["adjnum"+i].select();
				return false;
			}
		}
	)
}

function checkPStockOut(value, i, isbn){
	var purchase = $('input[@name=purchase'+i+']').val();
	var leave = $('input[@name=leave'+i+']').val();
	if(value > 0){
		if(parseInt(leave, 10) < parseInt(value, 10)){
			alert("出貨數量超過代訂購數量！");
			document.forms["resultForm"].elements["pStockOut"+i].value = "";
			document.forms["resultForm"].elements["pStockOut"+i].select();
			return false;
		}else{
			var sum = 0;
			$('input[@name=isCheck][@checked]').each(function(){
				var id = $(this).val();
				var sIsbn = $('input[@name=sIsbn'+id+']').val();
				var quantity = $('#pStockOut'+id).val();
				if(sIsbn == isbn){
					sum += parseInt(quantity, 10);
				}
			})
			$.post(
				"BookStockOutServlet",
				{
					type: "checkOutPurchase",
					i: i,
					isbn: isbn,
					adjnum: sum
				},
				function(result){
					if(result == 0){
						alert("出貨數量已超出庫存數量！");
						document.forms["resultForm"].elements["pStockOut"+i].value = "";
						document.forms["resultForm"].elements["pStockOut"+i].select();
						return false;
					}
				}
			)
		}
	}
}

function checkStockOut(stock, i){
	var stockOut = 0;
	if(stock.length == 0){
		alert("出貨數量不能為空，請輸入數量！");
		document.forms["resultForm"].elements["stockOut"+i].value = 0;
		document.forms["resultForm"].elements["stockOut"+i].select();
		return false;
	}else{
		if(stock.substring(0,1) == "-"){
			if(isNaN(stock.substring(2, stock.length))){
				alert("出貨數量格式不符合，請重新輸入！");
				document.forms["resultForm"].elements["stockOut"+i].value = 0;
				document.forms["resultForm"].elements["stockOut"+i].select();
				return false;
			}else{
				if(stock.substring(1, 2) == "0"){
					document.forms["resultForm"].elements["stockOut"+i].value = "-" ;
				}
			}
			var number = "0123456789";
			for(j=0; j<stock.substring(1, stock.length).length; j++){
				var point = stock.substring(1, stock.length).charAt(j);
				if(number.indexOf(point) == -1){
					alert("請輸入正確的出貨數量！");
					document.forms["resultForm"].elements["stockOut"+i].value = 0;
					document.forms["resultForm"].elements["stockOut"+i].select();
					return false;
				}
			}
			stockIn = stock;
		}else{
			if(isNaN(stock)){
				alert("出貨數量格式不符合，請重新輸入！");
				document.forms["resultForm"].elements["stockOut"+i].value = 0;
				document.forms["resultForm"].elements["stockOut"+i].select();
				return false;
			}else{
				if(stock.substring(0, 1) == 0){
					if(stock.length == 1){
						document.forms["resultForm"].elements["stockOut"+i].value = 0;
					}else {
						document.forms["resultForm"].elements["stockOut"+i].value = stock.substring(1, stock.length);
					}
					document.forms["resultForm"].elements["stockOut"+i].select();
				}
			}
			var number = "0123456789";
			for(j=0; j<stock.length; j++){
				var point = stock.charAt(j);
				if(number.indexOf(point) == -1){
					alert("請輸入正確的出貨數量！");
					document.forms["resultForm"].elements["stockOut"+i].value = 0;
					document.forms["resultForm"].elements["stockOut"+i].select();
					return false;
				}
			}
			stockIn = stock;
		}
		$.post(
			"BookStockOutServlet",
			{
				type: "sessionAdjnum",
				i: i,
				adjnum: stockIn
			},
			function(result){
				if(result == 0){
					alert("所輸入的出貨數量已超過庫存數量！");
					document.forms["resultForm"].elements["stockOut"+i].value = 0;
					document.forms["resultForm"].elements["stockOut"+i].select();
					return false;
				}
			}
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
			if(purchasePrice != "" && discount != ""){
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
			if(purchasePrice != "" && discount != ""){
				document.forms["resultForm"].elements["costPrice"+i].value = accMul(purchasePrice, discount);
			}else{
				document.forms["resultForm"].elements["costPrice"+i].value = 0;
			}
			return false;
		}
	}
}
function searchStockOut(){
	var prDate = $('#prDate').val();
	var paidCurrency = $('#paidCurrency').val();
	var supplierNo = $('#supplierNo').val();
	if(supplierNo == ""){
		alert("請輸入學院/書商進行查詢！");
		return false;
	}else{
		window.location.href = "BookStockOutServlet?type=searchSupplier&prDate="+prDate+"&paidCurrency="+paidCurrency+"&supplierNo="+supplierNo;
	}
}
function searchStockOutbyPrNum(){
	var prDate = $('#prDate').val();
	var paidCurrency = $('#paidCurrency').val();
	var supplierNo = $('#supplierNo').val();

	var prNum = $('#prNum').val();
	if(prNum == ""){
		alert("請輸入出貨單編號進行查詢！");
		return false;
	}else{
		window.location.href = "BookStockOutServlet?type=searchPrNum&prNum="+prNum+"&prDate="+prDate+"&paidCurrency="+paidCurrency+"&supplierNo="+supplierNo;
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

function setEnable(i){
	if($('.isCheck'+i).attr("checked") == true){
		$('input[@name=sPurchasePrice'+i+']').removeClass("view");
		$('input[@name=sDiscount'+i+']').removeClass("view");
		$('input[@name=sCostPrice'+i+']').removeClass("view");
		
		$('input[@name=sPurchasePrice'+i+']').attr("readonly", "");
		$('input[@name=sDiscount'+i+']').attr("readonly", "");
		$('input[@name=sCostPrice'+i+']').attr("readonly", "");
		
		$('#sCrsCode'+i).attr("disabled", false);
		$('#sLectCode'+i).attr("disabled", false);
		$('#selectLectBtn'+i).attr("disabled", false);
		
		document.getElementById("pStockOut"+i).disabled = false;
		document.getElementById("pDate"+i).disabled = false;
		
		var isbn = $('input[@name=sIsbn'+i+']').val();
		var sum = 0;
		$('input[@name=isCheck][@checked]').each(function(){
			var id = $(this).val();
			var sIsbn = $('input[@name=sIsbn'+id+']').val();
			var quantity = $('#pStockOut'+id).val();
			if(sIsbn == isbn){
				sum += parseInt(quantity, 10);
			}
		})
		$.post(
			"BookStockOutServlet",
			{
				type: "checkOutPurchase",
				i: i,
				isbn: isbn,
				adjnum: sum
			},
			function(result){
				if(result == 0){
					alert("出貨數量已超出庫存數量！");
					document.forms["resultForm"].elements["pStockOut"+i].value = "";
					document.forms["resultForm"].elements["pStockOut"+i].select();
					return false;
				}
			}
		)
	}else{
		var oldSPurchasePrice = $('input[@name=oldSPurchasePrice'+i+']').val();
		var oldSDiscount = $('input[@name=oldSDiscount'+i+']').val();
		var oldSCostPrice = $('input[@name=oldSCostPrice'+i+']').val();
		var purchase = $('input[@name=purchase'+i+']').val();
		var leave = $('input[@name=leave'+i+']').val();
		
		$('input[@name=sPurchasePrice'+i+']').addClass("view");
		$('input[@name=sDiscount'+i+']').addClass("view");
		$('input[@name=sCostPrice'+i+']').addClass("view");
		
		$('input[@name=sPurchasePrice'+i+']').attr("readonly", "readonly");
		$('input[@name=sDiscount'+i+']').attr("readonly", "readonly");
		$('input[@name=sCostPrice'+i+']').attr("readonly", "readonly");
		
		$('input[@name=sPurchasePrice'+i+']').attr("value", oldSPurchasePrice);
		$('input[@name=sDiscount'+i+']').attr("value", oldSDiscount);
		$('input[@name=sCostPrice'+i+']').attr("value", oldSCostPrice);
		
		document.getElementById("pStockOut"+i).value = leave==0?purchase:leave;
		document.getElementById("pStockOut"+i).disabled = true;
		document.getElementById("pDate"+i).value = "";
		document.getElementById("pDate"+i).disabled = true;
	}
}
function openWindow(i, type) {
	var crsCode = "";
	if(type == "search"){
		crsCode = $("#sCrsCode"+i).val();		
	} else {
		crsCode = $("#crsCode"+i).val();
	}
	if(crsCode == ""){
		alert("請選擇科目編號！");
		return false;
	} else {
		var result  = window.showModalDialog("ExassLectServlet?type=showLecturers&courseCode="+crsCode,"","resizable=no;dialogHeight=260px;dialogWidth=480px;location=no;menubar=no;toolbar=no;status=no");
		if(result){
			if(type == "search"){
				document.getElementById("sLectCode"+i).value = result;
				document.getElementById("sLectCode"+i).focus();
			} else {
				document.getElementById("lectCode"+i).value = result;
				document.getElementById("lectCode"+i).focus();
			}
		}
	}
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
String isp = null;
if(request.getAttribute("isp") != null){
	isp = (String)request.getAttribute("isp");
}
int mark = 0;
if(session.getAttribute("search") != null && session.getAttribute("search").equals("search")){
	mark = 1;
}
List<BookSupplier> supplierList = null;
if(request.getAttribute("supplierList") != null){
	supplierList = (List)request.getAttribute("supplierList");
}
BookStockOutOrder bookStockOutOrder = null;
String prDate = null;
String searchPrNum = "";
if(request.getAttribute("searchPrNum") != null){
	searchPrNum = (String)request.getAttribute("searchPrNum");
}
String prNum = null;
Integer supplierNo = 0;
String paidCurrency = null;
Date now = new Date();
SimpleDateFormat dealtimeFormat = new SimpleDateFormat("yyyy-MM-dd");
if(session.getAttribute("bookStockOutOrder") != null){
	bookStockOutOrder = (BookStockOutOrder)session.getAttribute("bookStockOutOrder");
	if(bookStockOutOrder.getOutDate() != null){
		prDate = bookStockOutOrder.getOutDate();
	}
	if(bookStockOutOrder.getPrnum() != null){
		prNum = bookStockOutOrder.getPrnum();
	}
	if(bookStockOutOrder.getSupplierNo() != null && bookStockOutOrder.getSupplierNo()!=0){
		supplierNo = bookStockOutOrder.getSupplierNo();
	}
	if(bookStockOutOrder.getPaidCurrency() != null){
		paidCurrency = bookStockOutOrder.getPaidCurrency();
	}
}
%>
<input type="hidden" name="isp" value="<%=isp %>">
<table border="0" width="90%" cellpadding="0" cellspacing="0" class="tableInfo">
  <tr valign="middle">
    <td width="100" height="28">出貨單日期：</td>
    <td width="180">
      <input type="hidden" name="focusOn" id="focusOn" value="<%if(prDate==null){out.print("N");}else{out.print("Y");} %>">
      <input name="prDate" type="text" id="prDate" onClick="new Calendar(null, null, 3).show(this);" value="<% if(prDate!=null && !"".equals(prDate)){ out.print(prDate); } else{out.print(dealtimeFormat.format(now));}%>"  readonly="readonly"/>
    </td>
    <td width="80">學院/書商：</td>
    <td >
      <select name="supplierName" id="supplierNo" <% if(supplierNo != 0 || (isp != null && "Y".equals(isp))){out.print("disabled");} %>>
        <option value="">==請選擇==</option>
        <%
        if(supplierList != null && !supplierList.isEmpty()){
        	for(BookSupplier s : supplierList){
        %>
        <option value="<%=s.getSupplierNo() %>" <%if(supplierNo!=0 && String.valueOf(supplierNo).equals(String.valueOf(s.getSupplierNo()))){out.print("selected");} %>><%=s.getSupplierName() %></option>
        <%
        	}
        }
        %>
      </select>
      <img src="images/search.gif" height="15" width="15" border="0" onClick="searchStockOut();" style="cursor:hand">    
    </td>
    <td width="80">付款幣種：</td>
    <td >
      <select name="paidCurrency" id="paidCurrency">
        <option value="MOP" <%if(paidCurrency!=null && "MOP".equals(paidCurrency)){out.print("selected");} %>>葡幣     </option>
        <option value="RMB" <%if(paidCurrency!=null && "RMB".equals(paidCurrency)){out.print("selected");} %>>人民幣</option>
        <option value="HKD" <%if(paidCurrency!=null && "HKD".equals(paidCurrency)){out.print("selected");} %>>港幣     </option>
        <option value="USD" <%if(paidCurrency!=null && "USD".equals(paidCurrency)){out.print("selected");} %>>美元     </option>
      </select>
    </td>
  </tr>
</table>
<%if(isp != null && "N".equals(isp)){ %>
<form action="BookStockOutServlet" method="post" name="isbnForm" class="isbnForm">
  <table border="0" width="90%" cellpadding="0" cellspacing="0">   
    <tr>
      <td width="100" height="28">出貨單編號：</td>
      <td width="180">
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
        <img src="images/search.gif" height="15" width="15" border="0" onClick="searchStockOutbyPrNum();" style="cursor:hand">
      </td>
      <td width="80">ISBN:</td>
      <td>
        <input type="text" name="isbn" id="isbn">&nbsp;
        <input type="hidden" name="mark" id="mark" value="<%=mark %>">
        <input type="hidden" name="prNumParam" id="prNumParam" value="">
        <input type="submit" value="確定" style="height:22px;">
        <%
        String wrong = "";
        if(request.getAttribute("wrong") != null){
        	wrong = (String)request.getAttribute("wrong");
        	out.print(wrong);
        }
        %>
      </td>
    </tr>
  </table>
</form>
<%} %>
<%
List searchInfoList = (List)session.getAttribute("searchInfoList");

int searchSize = 0;
int size = 0;
List infoList = (List)session.getAttribute("infoList");
List courses = (List)session.getAttribute("courses");
List sCourses = (List)session.getAttribute("sCourses");
List lecturers = (List)session.getAttribute("lecturers");
if((infoList == null || infoList.size() == 0) && (searchInfoList == null || searchInfoList.size() == 0)){
%>
<hr>
<input type="hidden" name="size" id="size" value="<%=size %>">
<font style="font-size:12px;">請選擇學院/書商，或輸入出貨單編號及ISBN進行出貨！</font>
<%
}else{
	if(infoList != null && !infoList.isEmpty()){
		size = infoList.size();
	}
	if(searchInfoList != null && !searchInfoList.isEmpty()){
		searchSize = searchInfoList.size();
	}
%>
<input type="hidden" name="searchSize" id="searchSize" value="<%=searchSize %>">
<input type="hidden" name="size" id="size" value="<%=size %>">
<form action="" method="post" name="resultForm" id="resultForm">
  <table border="0" cellpadding="0" cellspacing="0" width="99.9%">
    <tr align="center">
      <%
	  if(isp != null && "Y".equals(isp)){
	  %>
	  <td width="10%" height="28" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">代購編號</td>
	  <td width="10%" height="28" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">圖書編號</td>
	  <%
	  }else{
	  %>
	  <td width="11%" height="28" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">圖書編號</td>
      <%
      } 
      %>
	  <td width="20%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">圖書名稱</td>
	  <%
	  if(isp != null && "Y".equals(isp)){
	  %>
	  <td width="5%"  style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">優惠價</td>
	  <%
	  } else {
	  %>
	  <td width="5%"  style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">定價</td>
	  <td width="5%"  style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">折扣</td>
	  <td width="5%"  style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">出貨價</td>  
	  <%
	  } 
	  %>
	  
	  <%
	  if(isp != null && "Y".equals(isp)){
	  %>
	  <td width="5%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">代購冊數</td>
	  <td width="5%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">已出冊數</td>
	  <td width="4%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">出貨數</td>
	  <td width="7%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">出貨日期</td>
	  <td width="7%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">科目</td>
	  <td width="10%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">授課老師</td>
	  <td width="1%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">操作</td>
	  <%
	  }else{
	  %>
	  <td width="5%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">出貨數</td>
	  <td width="11%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">備註</td>
	  <td width="7%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">科目</td>
	  <td width="14%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">授課老師</td>
	  <td width="5%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">操作</td>
	  <%
	  }
	  %>
	</tr>
	<%
	if(searchInfoList != null && !searchInfoList.isEmpty()){
		for(int j=searchInfoList.size()-1; j>=0; j--){
			BookStockOutBean searchInfo = new BookStockOutBean();
			searchInfo = (BookStockOutBean)searchInfoList.get(j);
			List<BookRel> courseList = null;
			if(sCourses != null && !sCourses.isEmpty())
				courseList = (List)sCourses.get(j);
			String remarks = searchInfo.getRemarks();
			if(remarks == null){
				remarks = "";
			}
	%>
	<tr>
	  <%
	  if(isp != null && "Y".equals(isp)){
	  %>
	  <td align="center" height="25" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <%=searchInfo.getPrNum() %>
	  </td>
	  <% } %>
	  <td align="center" height="25" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <%=searchInfo.getIsbn() %>
	    <input type="hidden" name="sNo<%=j %>" value="<%=searchInfo.getOutNo() %>">
	    <input type="hidden" name="sIsbn<%=j %>" value="<%=searchInfo.getIsbn() %>">
	    <input type="hidden" name="sPrNum<%=j %>" value="<%=searchInfo.getPrNum() %>">
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <%=searchInfo.getBook().getTitle() %>
	  </td>
	  <%
	  if(isp != null && "Y".equals(isp)){
	  %>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" value="<%=searchInfo.getCostPrice() %>" name="oldSCostPrice<%=j %>" >
	    <input type="text" value="<%=searchInfo.getCostPrice() %>" name="sCostPrice<%=j %>" id="sCostPrice<%=j %>" onkeyup="writeSCostPrice('<%=j %>');" value="0" style="width: 40px">
	  </td>
	  <%  
	  }else{
	  %>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" value="<%=searchInfo.getPurchasePrice() %>" name="oldSPurchasePrice<%=j %>" >
	    <input type="text" value="<%=searchInfo.getPurchasePrice() %>" name="sPurchasePrice<%=j %>" id="sPurchasePrice<%=j %>" onKeyUp="calculateSPrice(this.value, 'sPurchasePrice', 'sDiscount', '<%=j %>');" style="width: 40px" onFocus="selectItem('sPurchasePrice', '<%=j %>');">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" value="<%=searchInfo.getDisCount() %>" name="oldSDiscount<%=j %>" >
	    <input type="text" value="<%=searchInfo.getDisCount() %>" name="sDiscount<%=j %>" id="sDiscount<%=j %>" onKeyUp="calculateSPrice(this.value, 'sDiscount', 'sPurchasePrice', '<%=j %>');" style="width: 40px" onFocus="selectItem('sDiscount', '<%=j %>');">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" value="<%=searchInfo.getCostPrice() %>" name="oldSCostPrice<%=j %>" >
	    <input type="text" value="<%=searchInfo.getCostPrice() %>" name="sCostPrice<%=j %>" id="sCostPrice<%=j %>" onkeyup="writeSCostPrice('<%=j %>');" value="0" style="width: 40px">
	  </td>
	  <%
	  }
	  %>
	  <%
	  if(isp != null && "Y".equals(isp)){
	  %>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" name="purchase<%=j %>" id="purchase<%=j %>" value="<%=searchInfo.getAdjNum() %>" >
	    <%=searchInfo.getAdjNum() %>
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" name="leave<%=j %>" id="leave<%=j %>" value="<%=searchInfo.getPurchaseLeave() %>" >
	    <%=searchInfo.getAdjNum()-searchInfo.getPurchaseLeave() %>
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="text" name="pStockOut<%=j %>" id="pStockOut<%=j %>" value="<%=searchInfo.getPurchaseLeave() %>" onKeyUp="checkPStockOut(this.value, '<%=j %>', '<%=searchInfo.getIsbn() %>');" style="width: 40px;" disabled="disabled" maxlength="4" onKeyPress="return NumWithoutMinus(event)">
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;" align="center">
	    <input type="text" name="pDate<%=j %>" id="pDate<%=j %>" onClick="new Calendar(null, null, 3).show(this);" value="<%if(prDate==null){out.print(dealtimeFormat.format(now));}else{out.print(prDate);} %>" size="9" readonly="readonly" disabled="disabled"/>
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <select id="sCrsCode<%=j %>" name="sCrsCode<%=j %>" disabled>
	      <option value="">---</option>
	      <%
	      if(courseList != null && !courseList.isEmpty()){
	    	  for(BookRel rel : courseList){
	      %>
	      <option value="<%=rel.getCourseCode() %>" <%if(searchInfo.getCourseCode() != null && searchInfo.getCourseCode().equals(rel.getCourseCode())){out.print("selected");} %>>
	        <%=rel.getCourseCode() %>
	      </option>
	      <%
	    	  }
	      }
	      %>
	    </select>
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="text" name="sLectCode<%=j %>" id="sLectCode<%=j %>" value="<%=ToolsOfString.NulltoEmpty(searchInfo.getLectCode()) %>" style="width: 55px;" disabled="disabled">
	    <input type="button" value="--" id="selectLectBtn<%=j %>" onClick="openWindow('<%=j %>', 'search')" style="height: 22px;width: 20px;" disabled="disabled">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	  <%
	  if(searchInfo.getPurchaseLeave() != 0){
	  %>
	    <input type="checkbox" name="isCheck" class="isCheck<%=j %>" value="<%=j %>" onClick="setEnable('<%=j %>');">
	  <%
	  }
	  %>
	  </td>
	  <%
	  }else{
	  %>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <%=searchInfo.getAdjNum() %>
	    <input type="hidden" value="<%=searchInfo.getAdjNum() %>" name="oldAdjnum<%=j %>">
	    <input type="text" name="adjnum<%=j %>" id="adjnum<%=j %>" onKeyPress="return NumWithoutMinus(event)" onKeyUp="checkAdjnum(this.value, '<%=j %>', '<%=searchInfo.getIsbn() %>');" style="width: 40px" maxlength="4">
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="hidden" value="<%=remarks %>" name="oldSRemarks<%=j %>" >
	    <input type="text" value="<%=remarks %>" name="sRemarks<%=j %>" id="sRemarks<%=j %>" style="width: 120px" maxlength="50">
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <select id="sCrsCode<%=j %>" name="sCrsCode<%=j %>" disabled>
	      <option value="">---</option>
	      <%
	      if(!courseList.isEmpty()){
	    	  for(BookRel rel : courseList){
	      %>
	      <option value="<%=rel.getCourseCode() %>" <%if(searchInfo.getCourseCode() != null && searchInfo.getCourseCode().equals(rel.getCourseCode())){out.print("selected");} %>>
	        <%=rel.getCourseCode() %>
	      </option>
	      <%
	    	  }
	      }
	      %>
	    </select>
	    <input type="hidden" name="" id="sOldCrsCode<%=j %>" name="sOldCrsCode<%=j %>" value="<%=searchInfo.getCourseCode() %>"/>
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="text" name="sLectCode<%=j %>" id="sLectCode<%=j %>" value="<%=ToolsOfString.NulltoEmpty(searchInfo.getLectCode()) %>" style="width: 80px;" disabled="disabled">
	    <input type="hidden" name="sOldLectCode<%=j %>" id="sOldLectCode<%=j %>" value="<%=ToolsOfString.NulltoEmpty(searchInfo.getLectCode()) %>"/>
	    <input type="button" value="---" id="selectLectBtn<%=j %>" onClick="openWindow('<%=j %>', 'search')" style="height: 22px;" disabled="disabled">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="button" name="edit<%=j %>" value="修改" onClick="openEdit('<%=j %>');">
	    <input type="button" name="save<%=j %>" value="保存" onClick="saveEdit('<%=j %>');">
	    <input type="button" name="cancle<%=j %>" value="取消" onClick="closeEdit('<%=j %>');">
	  </td>
	  <%
	  }
	  %>
	</tr>
	<%
		}
	}
	if(infoList != null && !infoList.isEmpty()){
		for(int i=infoList.size()-1; i>=0; i--){
			BookStockOutBean info = (BookStockOutBean)infoList.get(i);
			List<BookRel> courseList = null;
			if(courses != null && !courses.isEmpty())
				courseList = (List)courses.get(i);
			List<ExassLect> lecturerList = null;
			if(lecturers != null && !lecturers.isEmpty())
				lecturerList = (List)lecturers.get(i);
	%>
	<tr>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;" align="center">
	    <%=info.getIsbn() %>
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <%=info.getBook().getTitle() %>
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input value="<%=info.getPurchasePrice() %>" name="purchasePrice<%=i %>" id="purchasePrice<%=i %>" onBlur="remarkInfo('<%=i %>');" onKeyUp="calculatePrice(this.value, 'purchasePrice', 'discount', '<%=i %>');" style="width: 40px" onFocus="selectItem('purchasePrice', '<%=i %>');">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input value="<%=info.getDisCount() %>" name="discount<%=i %>" id="discount<%=i %>" onBlur="remarkInfo('<%=i %>');" onKeyUp="calculatePrice(this.value, 'discount', 'purchasePrice', '<%=i %>');" style="width: 40px" onFocus="selectItem('discount', '<%=i %>');">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input value="<%=info.getCostPrice() %>" name="costPrice<%=i %>" class="costPrice" id="costPrice<%=i %>" onblur="remarkInfo('<%=i %>');" onkeyup="writeCostPrice('<%=i %>');" value="0" style="width: 40px">
	  </td>
	  <%  
	  if(isp != null && "N".equals(isp)){
	  %>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="text" name="stockOut<%=i %>" class="stockOut" id="stockOut<%=i %>" value="<%=info.getAdjNum() %>" onKeyUp="checkStockOut(this.value, '<%=i %>');" style="width: 40px;" maxlength="5" onFocus="selectItem('stockOut', '<%=i %>');">
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="text" name="remarks<%=i %>" id="remarks<%=i %>" value="<%=info.getRemarks()==null?"":info.getRemarks() %>" maxlength="50" onKeyUp="remarkInfo('<%=i %>');" style="width: 120px">
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <select id="crsCode<%=i %>" name="crsCode<%=i %>" onChange="remarkInfo('<%=i %>');">
	      <option value="">---</option>
	      <%
	      if(courseList != null && !courseList.isEmpty()){
	    	  for(BookRel rel : courseList){
	      %>
	      <option value="<%=rel.getCourseCode() %>" <%if(info.getCourseCode() != null && info.getCourseCode().equals(rel.getCourseCode())){out.print("selected");} %>>
	        <%=rel.getCourseCode() %>
	      </option>
	      <%
	    	  }
	      }
	      %>
	    </select>
	  </td>
	  <td style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="text" name="lectCode<%=i %>" id="lectCode<%=i %>" value="<%=ToolsOfString.NulltoEmpty(info.getLectCode()) %>" style="width: 80px;" onBlur="remarkInfo('<%=i %>');">
	    <input type="button" value="---" id="lectBtn" onClick="openWindow('<%=i %>', '')" style="height: 22px">
	  </td>
	  <td align="center" style="border-left: 1px solid #FFDEAD;border-bottom: 1px solid #FFDEAD;">
	    <input type="button" value="刪除" onClick="window.location.href='BookStockOutServlet?type=remove&i=<%=i %>'" style="height: 22px">
	  </td>
	  <%
	  }
	  %>	  
	</tr>
	<%
		}
	}
	%>
	<tr>
	  <td colspan="8" height="30" valign="bottom">
	    <%
	    if(isp != null && "Y".equals(isp)){
	    	%>
	    	<input type="button" value="保  存" class="purchaseOutSubmit" style="height: 22px;">
	    	<%
	    }else{
	    	%>
	    	<input type="button" value="保  存" class="submit" style="height: 22px;">
	    	<%
	    }
	    %>
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