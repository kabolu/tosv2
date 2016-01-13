<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*,java.text.*,edu.must.tos.bean.*" %>
<%@page import="edu.must.tos.util.ToolsOfString"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>系數設置</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/tos.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
h4{
	font-size:16px;
	margin:10px 0 5px 5px;
	border-bottom:2px solid #FFD673;
}
.inp{
	height:18px;
	border:1;
	width:30px;
}
.date{
	height:18px;
	border:1;
	width:80px;
}
.view{
	height:18px;
	border:0;
	width:37px;
	background:#F5F2DA
}
.viewDate{
	height:18px;
	border:0;
	width:80px;
	background:#F5F2DA
}
table td{
	padding: 0px 2px 0px 5px;
	height: 22px;
}
-->
</style>
<script language="javascript">
$(document).ready(function(){
	$('#intakeList').hide();
	$('.saveIntake').hide();
	$('.back').hide();
	
	$('.saveShippingFee').hide();
	$('.cancelShippingFee').hide();
	
	$('.saveAmerce').hide();
	$('.cancelAmerce').hide();
		
	$('a[@name=saveDiffer1]').hide();
	$('a[@name=cancelDiffer1]').hide();
	
	$('a[@name=saveDiffer2]').hide();
	$('a[@name=cancelDiffer2]').hide();
	
	$('.savePrepaid').hide();
	$('.cancelPrepaid').hide();
	
	$('.saveRePrintFee').hide();
	$('.cancelRePrintFee').hide();
	
	$('.saveRate').hide();
	$('.cancelRate').hide();
	
	$('input[@name=rePrintFee]').addClass("view");
	$('input[@name=rePrintFee]').attr("readonly", "readonly");
	$('input[@name=updRePrintFeeUid]').attr("readonly", "readonly");
	$('input[@name=updRePrintFeeDate]').attr("readonly", "readonly");
	
	$('input[@name^=fee]').addClass("view");
	$('input[@name^=fee]').attr("readonly", "readonly");
	
	$('a[@name^=feeSave]').hide();
	$('a[@name^=cancel]').hide();
	
	$('#amercePercent').addClass("view");
	$('#amercePercent').attr("readonly", "readonly");
		
	$('#differ1').addClass("view");
	$('#differ1').attr("readonly", "readonly");
	
	$('#differ2').addClass("view");
	$('#differ2').attr("readonly", "readonly");
	
	$('input[@name^=prepaid]').addClass("view");
	$('input[@name^=prepaid]').attr("readonly", "readonly");
	
	$('input[@name^=currency]').addClass("view");
	$('input[@name^=currency]').attr("readonly", "readonly");
	
	$('input[@name^=rate]').addClass("view");
	$('input[@name^=rate]').attr("readonly", "readonly");
	
	$('.saveWithdrawForCarry').hide();
	$('.cancelWithdrawForCarry').hide();
	
	$('#withdrawForCarry').addClass("view");
	$('#withdrawForCarry').attr("readonly", "readonly");
	
	$('#withdrawForCarryDate').addClass("viewDate");
	$('#withdrawForCarryDate').attr("readonly", "readonly");
	
	$('#shippingFee').addClass("view");
	$('#shippingFee').attr("readonly", "readonly");
	
	$('#rePrintAmerceCurrency').hide();
	$('.editRePrintFee').click(function(){
		$('.saveRePrintFee').show();
		$('.cancelRePrintFee').show();
		$('.editRePrintFee').hide();
		
		$('input[@name=rePrintFee]').removeClass("view");
		$('input[@name=rePrintFee]').addClass("inp");
		$('input[@name=rePrintFee]').attr("readonly", '');
		$('#rePrintAmerceCurrency').show();
		$('.currencyValue').hide();
	})
	
	$('.editShippingFee').click(function(){
		$('.saveShippingFee').show();
		$('.cancelShippingFee').show();
		$('.editShippingFee').hide();
		
		$('#shippingFee').removeClass("view");
		$('#shippingFee').addClass("inp");
		$('#shippingFee').attr("readonly", '');
	})
	
	$('.cancelShippingFee').click(function(){
		var oldShippingFee = $('#oldShippingFee').val();
		
		$('.saveShippingFee').hide();
		$('.cancelShippingFee').hide();
		$('.editShippingFee').show();
		
		$('#shippingFee').removeClass("inp");
		$('#shippingFee').addClass("view");
		$('#shippingFee').attr("readonly", 'readonly');
		$('#shippingFee').attr("value", oldShippingFee);
	})
	
	$('.cancelRePrintFee').click(function(){
		var oldRePrintFee = $('input[@name=oldRePrintFee]').val();
		$('input[@name=rePrintFee]').removeClass("inp");
		$('input[@name=rePrintFee]').addClass("view");
		$('input[@name=rePrintFee]').attr("readonly", "readonly");
		$('input[@name=rePrintFee]').attr("value", oldRePrintFee);
		
		var oldCurrencyValue = $('input[@name=oldCurrencyValue]').val();
		$('#rePrintAmerceCurrency').hide();
		$('.currencyVlaue').show();
		if(oldCurrencyValue!=""){
			$('#rePrintAmerceCurrency').val(oldCurrencyValue);
		}else{
			$('#rePrintAmerceCurrency').val("MOP");
		}
		
		$('.saveRePrintFee').hide();
		$('.cancelRePrintFee').hide();
		$('.editRePrintFee').show();
	})
	
	$('#beginDate').hide();
	$('.editWithdrawForCarry').click(function(){
		var date = $('#withdrawForCarryDate').val();
		$('.saveWithdrawForCarry').show();
		$('.cancelWithdrawForCarry').show();
		$('.editWithdrawForCarry').hide();
		
		$('#withdrawForCarry').removeClass("view");
		$('#withdrawForCarry').addClass("inp");
		$('#withdrawForCarry').attr("readonly", '');
		
		$('#withdrawForCarryDate').hide();
		$('#beginDate').attr("value", date);
		$('#beginDate').show();
	})
	
	$('.editIntake').click(function(){
		$('.currIntake').hide();
		$('.editIntake').hide();
		$('.saveIntake').show();
		$('#intakeList').show();
		$('.back').show();
		var intake = $('.currIntake').val();
		$.post(
			"SysConfTimeServlet",
			{
				type:"intakeParam",
				currIntake:intake
			},
			function(data){
				$(data).find("results result").each(function(){
					var type = $(this).find("scKey").text();
					var value1 = $(this).find("scValue1").text();
					//alert(type+" "+value1);
					//$('#intakeList').add(new Option(value1, value1));
					document.getElementById("intakeList").add(new Option(value1, value1));
				})
			}
		)
	})
	
	$('.saveIntake').click(function(){
		var oldIntake = $('.currIntake').val();
		var editIntake = $('#intakeList').val();
		if(editIntake == ""){
			alert("請選擇學期！");
			return false;
		}
		$.post(
			"SysConfTimeServlet",
			{
				type:"intakeModify",
				oldIntake:oldIntake,
				editIntake:$('#intakeList').val()
			},
			function(result){
				if(result==1){
					alert("學期修改成功！");
					$('#intakeList').hide();
					$('.currIntake').attr("value", '');
					$('.currIntake').attr("value", editIntake);
					$('.currIntake').show();
					$('.editIntake').show();
					$('.saveIntake').hide();
					$('.back').hide();
					document.getElementById("intakeList").options.length=0;
    				document.getElementById("intakeList").add(new Option("---請選擇---",""));
				}else{
					alert("修改失敗！");
				}
			}
		)
		
	})
	
	$('.back').click(function(){
		$('#intakeList').hide();
		$('.back').hide();
		$('.currIntake').show();
		$('.editIntake').show();
		$('.saveIntake').hide();
		document.getElementById("intakeList").options.length=0;
  		document.getElementById("intakeList").add(new Option("---請選擇---",""));
	})
	
	$('.editAmerce').click(function(){
		$('.saveAmerce').show();
		$('.cancelAmerce').show();
		$('.editAmerce').hide();
		$('#amercePercent').removeClass("view");
		$('#amercePercent').addClass("inp");
		$('#amercePercent').attr("readonly", '');
		
	});
	
	$('.saveAmerce').click(function(){
		var amerce = $('#amercePercent').val();
		if(isNaN(amerce)){
			alert("請輸入數字！");
			return false;
		}
		$.post(
			"SysConfTimeServlet",
			{
				type:"saveAmerce",
				amerce:amerce
			},
			function(result){
				var upduid;
				var upddate;
				var commit;
				$(result).find("results result").each(function(){
					upduid = $(this).find("upduid").text();
					upddate = $(this).find("upddate").text();
					commit = $(this).find("commit").text();
				})
			
				if(commit==1){
					alert("逾期繳費百分率設置成功！");
					$('#amercePercent').addClass("view");
					$('#amercePercent').attr("readonly", "readonly");
					$('.saveAmerce').hide();
					$('.cancelAmerce').hide();
					$('.editAmerce').show();
					$('#oldAmerce').attr("value", amerce);
					$('#upduid').attr("value", upduid);
					$('#upddate').attr("value", upddate);
				}else{
					alert("逾期繳費百分率設置失敗！");
				}
			}
		)
		
	})
	
	$('.cancelAmerce').click(function(){
		var oldAmerce = $('#oldAmerce').val();
		$('#amercePercent').removeClass("inp");
		$('#amercePercent').addClass("view");
		$('#amercePercent').attr("readonly", "readonly");
		$('.saveAmerce').hide();
		$('.cancelAmerce').hide();
		$('.editAmerce').show();
		
		$('#amercePercent').attr("value", oldAmerce);
	})
	
	$('.cancelWithdrawForCarry').click(function(){
		var oldWithdrawForCarry = $('#oldWithdrawForCarry').val();
		var oldWithdrawFroCarryDate = $('#oldWithdrawFroCarryDate').val();
		$('#withdrawForCarry').removeClass("inp");
		$('#withdrawForCarry').addClass("view");
		$('#withdrawForCarry').attr("readonly", "readonly");
		
		$('.saveWithdrawForCarry').hide();
		$('.cancelWithdrawForCarry').hide();
		$('.editWithdrawForCarry').show();
		
		$('#withdrawForCarry').attr("value", oldWithdrawForCarry);
		
		$('#beginDate').attr("value", '');
		$('#beginDate').hide();
		//$('#withdrawForCarryDate').attr("value", '');
		$('#withdrawForCarryDate').attr("value", oldWithdrawFroCarryDate);
		$('#withdrawForCarryDate').show();
	})
	
	$('.saveWithdrawForCarry').click(function(){
		var withdrawForCarry = $('#withdrawForCarry').val();
		var beginDate = $('#beginDate').val();
		if(isNaN(withdrawForCarry)){
			alert("請輸入正確的數值！");
			return false;
		}else{
			if(withdrawForCarry < 0){
				alert("退運費百分比為正數！");
				return false;
			}
		}
		if(beginDate==""){
			alert("請輸入退運開始日期！");
			return false;
		}
		$.post(
			"SysConfTimeServlet",
			{
				type:"withdrawForCarry",
				fee:withdrawForCarry,
				beginDate:beginDate
			},
			function(result){
				var upduid;
				var upddate;
				var commit;
				$(result).find("results result").each(function(){
					upduid = $(this).find("upduid").text();
					upddate = $(this).find("upddate").text();
					commit = $(this).find("commit").text();
				})
				if(commit==1){
					alert("退運費百分比設置成功！");
					$('#withdrawForCarry').addClass("view");
					$('#withdrawForCarry').attr("readonly", "readonly");
					$('.saveWithdrawForCarry').hide();
					$('.cancelWithdrawForCarry').hide();
					$('.editWithdrawForCarry').show();
					$('#oldWithdrawForCarry').attr("value", withdrawForCarry);
					$('#oldWithdrawForCarryDate').attr("value", beginDate);
					$('#withdrawForCarryDate').attr("value", beginDate);
					$('#withdrawForCarryDate').show();
					$('#beginDate').hide();
					$('#updWithdrawForCarryUid').attr("value", upduid);
					$('#updWithdrawForCarryDate').attr("value", upddate);
				}else{
					alert("退運費百分比設置失敗！");
				}
			}
		)
	})
	
	$('.saveRePrintFee').click(function(){
		var rePrintFee = $('input[@name=rePrintFee]').val();
		var oldRePrintFee = $('input[@name=oldRePrintFee]').val();
		var oldCurrencyValue = $('input[@name=oldCurrencyValue]').val();
		var rePrintAmerceCurrency = $('#rePrintAmerceCurrency').val();
		if(rePrintFee==""){
			alert("重印收據罰款金額不能為空！");
			return false;
		}else{
			$.post(
				"SysConfTimeServlet",
				{
					type:"rePrintFee",
					rePrintFee:rePrintFee,
					rePrintAmerceCurrency:rePrintAmerceCurrency
				},
				function(result){
					var upduid;
					var upddate;
					var commit;
					$(result).find("results result").each(function(){
						upduid = $(this).find("upduid").text();
						upddate = $(this).find("upddate").text();
						commit = $(this).find("commit").text();
					})
					if(commit==1){
						alert("重印收據罰款金額設置成功！");
						$('input[@name=rePrintFee]').addClass("view");
						$('input[@name=rePrintFee]').attr("readonly", "readonly");
						$('.saveRePrintFee').hide();
						$('.cancelRePrintFee').hide();
						$('.editRePrintFee').show();
						$('input[@name=oldRePrintFee]').attr("value", rePrintFee);
						$('input[@name=updRePrintFeeUid').attr("value", upduid);
						$('input[@name=updRePrintFeeDate]').attr("value", upddate);
						
						$('input[@name=oldCurrencyValue]').attr("value", rePrintAmerceCurrency);
						$('.currencyValue').text(rePrintAmerceCurrency);
						$('.currencyValue').show();
						$('#rePrintAmerceCurrency').hide();
					}else{
						alert("重印收據罰款金額設置失敗！");
						$('input[@name=rePrintFee]').addClass("view");
						$('input[@name=rePrintFee]').attr("readonly", "readonly");
						$('.saveRePrintFee').hide();
						$('.cancelRePrintFee').hide();
						$('.editRePrintFee').show();
						$('input[@name=rePrintFee]').attr("value", oldRePrintFee);
						
						$('#rePrintAmerceCurrency').hide();
						$('#rePrintAmerceCurrency').val(oldCurrencyValue);
						$('.currencyValue').show();
					}
				}
			)
		}
	})
	
	$('.saveShippingFee').click(function(){
		var shippingFee = $('#shippingFee').val();
		var oldShippingFee = $('#oldShippingFee').val();
		
		$.post(
			"SysConfTimeServlet",
			{
				type: "shippingFee",
				shippingFee: shippingFee
			},
			function(result){
				if(result=="false"){
					alert("補訂運費設置失敗！");
					$('#shippingFee').removeClass("inp");
					$('#shippingFee').addClass("view");
					$('#shippingFee').attr("readonly", 'readonly');
					$('#shippingFee').attr("value", oldShippingFee);
				}else{
					alert("補訂運費設置成功！");
					$('#shippingFee').removeClass("inp");
					$('#shippingFee').addClass("view");
					$('#shippingFee').attr("readonly", 'readonly');
					$('#oldShippingFee').attr("value", shippingFee);
				}
				$('.saveShippingFee').hide();
				$('.cancelShippingFee').hide();
				$('.editShippingFee').show();
			}
		)
	})
})

function feeSet(i){
	$('input[@name^=fee][@id=fee'+i+']').removeClass("view");
	$('input[@name^=fee][@id=fee'+i+']').addClass("inp");
	$('input[@name^=fee][@id=fee'+i+']').attr("readonly", '');
	
	$('.edit'+i).hide();
	$('a[@name=feeSave'+i+']').show();
	$('a[@name=cancel'+i+']').show();
}

function feeSave(key, i){
	var fee = $('#fee'+i).val();
	if(isNaN(fee)){
		alert("請輸入數字！");
		return false;
	}
	$.post(
		"SysConfTimeServlet",
		{
			type:"withdrawFee",
			sckey:key,
			scvalue1:fee
		},
		function(result){
			if(result==1){
				alert("類型為"+key+"的退書費修改成功！");
				$('input[@name^=fee][@id=fee'+i+']').addClass("view");
				$('input[@name^=fee][@id=fee'+i+']').attr("readonly", "readonly");
				$('a[@name=feeSave'+i+']').hide();
				$('a[@name=cancel'+i+']').hide();
				$('.edit'+i).show();
				$('input[@name^=oldFee][@id=oldFee'+i+']').attr("value", fee);
			}else{
				alert("修改失敗！");
			}
		}
	)
}

function cancel(i){
	var oldFee = $('input[@name^=oldFee][@id=oldFee'+i+']').val();
	$('input[@name^=fee][@id=fee'+i+']').addClass("view");
	$('input[@name^=fee][@id=fee'+i+']').attr("readonly", "readonly");
	$('a[@name=feeSave'+i+']').hide();
	$('a[@name=cancel'+i+']').hide();
	$('.edit'+i).show();
	$('input[@name^=fee][@id=fee'+i+']').attr("value", oldFee);
}

function DifferOperation(opt, i){
	if(opt=="edit"){
		$('input[@name=differ'+i+']').removeClass("view");
		$('input[@name=differ'+i+']').addClass("inp");
		$('input[@name=differ'+i+']').attr("readonly", '');
	
		$('a[@name=editDiffer'+i+']').hide();
		$('a[@name=saveDiffer'+i+']').show();
		$('a[@name=cancelDiffer'+i+']').show();
	}else if(opt=="cancel"){
		var oldDiffer = $('input[@name=oldDiffer'+i+']').val();
	
		$('input[@name=differ'+i+']').addClass("view");
		$('input[@name=differ'+i+']').removeClass("inp");
		$('input[@name=differ'+i+']').attr("readonly", 'readonly');
		$('input[@name=differ'+i+']').attr("value", oldDiffer);
		
		$('a[@name=editDiffer'+i+']').show();
		$('a[@name=saveDiffer'+i+']').hide();
		$('a[@name=cancelDiffer'+i+']').hide();
		
	}else if(opt=="save"){
		var differ = $('input[@name=differ'+i+']').val();
		if(isNaN(differ)){
			alert("請輸入數字！");
			return false;
		}else {
			if(differ<0){
				alert("所輸入的數字必須為正數！");
				return false;
			}else{
				$.post(
					"SysConfTimeServlet",
					{
						type:"saveDifferParam",
						i:i,
						differ:differ
					},
					function(result){
						if(result==1){
							alert("參數值設置成功！");
							$('input[@name=differ'+i+']').addClass("view");
							$('input[@name=differ'+i+']').attr("readonly", "readonly");
							$('a[@name=saveDiffer'+i+']').hide();
							$('a[@name=cancelDiffer'+i+']').hide();
							$('a[@name=editDiffer'+i+']').show();
							$('input[@name=oldDiffer'+i+']').attr("value", differ);
						}else{
							alert("參數值設置失敗！");
						}
					}
				)
			}
		}
	}
}

function savePrepaid(i){
	var prepaid = $('input[@name=prepaid'+i+']').val();
	var currency = $('input[@name=currency'+i+']').val();
	var faculty = $('input[@name=faculty'+i+']').val();
	
	if(currency==""){
		alert("請輸入幣種（MOP）或（RMB）！");
		return false;
	}else{
		if(currency=="MOP" || currency=="RMB"){
			
		}else{
			alert("請輸入幣種（MOP）或（RMB）！");
			return false;
		}
	}
	if(prepaid==""){
		alert("請輸入學院預付金額費用值！");
		return false;
	}else{
		if(isNaN(prepaid)){
			alert("請輸入數字！");
			return false;
		}else{
			if(prepaid<0){
				alert("金額數值需要正數！");
				return false;
			}else{
				$.post(
					"SysConfTimeServlet",
					{
						type:"savePrepaid",
						faculty:faculty,
						prepaid:prepaid,
						currency:currency
					},
					function(result){
						if(result==1){
							alert("學院預設金額成功！");
							$('a[@name=savePrepaid'+i+']').hide();
							$('a[@name=cancelPrepaid'+i+']').hide();
							$('a[@name=editPrepaid'+i+']').show();
	
							$('input[@name=prepaid'+i+']').removeClass("inp");
							$('input[@name=prepaid'+i+']').addClass("view");
							$('input[@name=prepaid'+i+']').attr("readonly", 'readonly');
							$('input[@name=prepaid'+i+']').attr("value", prepaid);
	
							$('input[@name=currency'+i+']').removeClass("inp");
							$('input[@name=currency'+i+']').addClass("view");
							$('input[@name=currency'+i+']').attr("readonly", 'readonly');
							$('input[@name=currency'+i+']').attr("value", currency);
						}else{
							alert("學院預設金額失敗！");
							return false;
						}
					}
				)
			}
		}
	}
}
function editPrepaid(i){
	$('a[@name=savePrepaid'+i+']').show();
	$('a[@name=cancelPrepaid'+i+']').show();
	$('a[@name=editPrepaid'+i+']').hide();
	
	$('input[@name=prepaid'+i+']').removeClass("view");
	$('input[@name=prepaid'+i+']').addClass("inp");
	$('input[@name=prepaid'+i+']').attr("readonly", '');
	
	$('input[@name=currency'+i+']').removeClass("view");
	$('input[@name=currency'+i+']').addClass("inp");
	$('input[@name=currency'+i+']').attr("readonly", '');
}
function cancelPrepaid(i){
	var prepaid = $('input[@name=oldPrepaid'+i+']').val();
	var currency = $('input[@name=oldCurrency'+i+']').val();
	
	$('a[@name=savePrepaid'+i+']').hide();
	$('a[@name=cancelPrepaid'+i+']').hide();
	$('a[@name=editPrepaid'+i+']').show();
	
	$('input[@name=prepaid'+i+']').removeClass("inp");
	$('input[@name=prepaid'+i+']').addClass("view");
	$('input[@name=prepaid'+i+']').attr("readonly", 'readonly');
	$('input[@name=prepaid'+i+']').attr("value", prepaid);
	
	$('input[@name=currency'+i+']').removeClass("inp");
	$('input[@name=currency'+i+']').addClass("view");
	$('input[@name=currency'+i+']').attr("readonly", 'readonly');
	$('input[@name=currency'+i+']').attr("value", currency);
}

function saveRate(i){
	var rate = $('input[@name=rate'+i+']').val();
	var currencyRate = $('input[@name=currencyRate'+i+']').val();
	var currencyType = $('input[@name=currencyType'+i+']').val();
	if(rate == ""){
		alert("請輸入匯率值！");
		return false;
	}else{
		if(isNaN(rate)){
			alert("請輸入數字！");
			return false;
		}else{
			if(rate < 0){
				alert("數值需要正數！");
				return false;
			}else{
				$.post(
					"SysConfTimeServlet",
					{
						type: "saveRate",
						rate: rate,
						currencyRate: currencyRate,
						currencyType: currencyType
					},
					function(result){
						if(result==1){
							alert("匯率設置成功！");
							$('a[@name=saveRate'+i+']').hide();
							$('a[@name=cancelRate'+i+']').hide();
							$('a[@name=editRate'+i+']').show();
	
							$('input[@name=rate'+i+']').removeClass("inp");
							$('input[@name=rate'+i+']').addClass("view");
							$('input[@name=rate'+i+']').attr("readonly", 'readonly');
							$('input[@name=rate'+i+']').attr("value", rate);
						}else{
							alert("匯率設置失敗！");
							return false;
						}
					}
				)
			}
		}
	}
}

function editRate(i){
	$('a[@name=saveRate'+i+']').show();
	$('a[@name=cancelRate'+i+']').show();
	$('a[@name=editRate'+i+']').hide();
	
	$('input[@name=rate'+i+']').removeClass("view");
	$('input[@name=rate'+i+']').addClass("inp");
	$('input[@name=rate'+i+']').attr("readonly", '');
}

function cancelRate(i){
	var rate = $('input[@name=oldRate'+i+']').val();
	
	$('a[@name=saveRate'+i+']').hide();
	$('a[@name=cancelRate'+i+']').hide();
	$('a[@name=editRate'+i+']').show();
	
	$('input[@name=rate'+i+']').removeClass("inp");
	$('input[@name=rate'+i+']').addClass("view");
	$('input[@name=rate'+i+']').attr("readonly", 'readonly');
	$('input[@name=rate'+i+']').attr("value", rate);
}

function delAdjustItem(){
	var len = $('input[@name=itemBox][@checked]').length;
	if(len==0){
		alert("請點選選項值！");
		return false;
	}else{
		var itemValue = "";
		$('input[@name=itemBox][@checked]').each(function(){
			itemValue += $(this).val()+",";
		})
		if(confirm('是否確認刪除這些選項資訊！')){
			$.post(
				"SysConfTimeServlet",
				{
					type:"delAdjustItem",
					itemValue:itemValue
				},
				function(result){
					if(result=="true"){
						alert("選項值刪除成功！");
						window.location.href="SysConfTimeServlet?type=param";
					}else{
						alert("選項值刪除失敗！");
						$($('input[@name=itemBox]')).attr("checked", false);
						return false;
					}
				}
			)
		}		
	}
}

function addAdjustItem(){
	var causeItem = $('#causeItem').val();
	if(causeItem==""){
		alert("原因選項值不能為空，請輸入值！");
		$('#causeItem').focus();
		return false;
	}
	$.post(
		"SysConfTimeServlet",
		{
			type:"adjustItemParam",
			adjustItem:causeItem
			
		},
		function (result){
			if(result=="true"){
				alert("添加選項值成功！");
				window.location.href="SysConfTimeServlet?type=param";
			}else{
				alert("添加選項值失敗！");
				return false;
			}
		}
	)
}


</script>
<%
if (session.getAttribute("userId") == null) {
%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
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
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
List withdrawFeeList = (List)request.getAttribute("withdrawFeeList");
SysConfig sc = (SysConfig)request.getAttribute("intakeConfig");
float amercePercent = 0;
String amerceUid = "";
String amerceDate = "";
if(request.getAttribute("amerceInfo")!=null){
	SysConfig amerceInfo = (SysConfig)request.getAttribute("amerceInfo");
	if(amerceInfo!=null && amerceInfo.getScValue1()!=null){
		amercePercent = Float.parseFloat(amerceInfo.getScValue1());
	}
	if(amerceInfo!=null && amerceInfo.getUpdUid()!=null){
		amerceUid = amerceInfo.getUpdUid();
	}
	if(amerceInfo!=null && amerceInfo.getUpdUid()!=null){
		amerceDate = dateFormat.format(amerceInfo.getUpdDate());
	}
}
String payAmercePeriod = "";
if(request.getAttribute("paid2Period")!=null){
	SysConfig paid2Period = (SysConfig)request.getAttribute("paid2Period");
	if(paid2Period!=null && paid2Period.getScValue1()!=null){
		payAmercePeriod = paid2Period.getScValue1();
	}
}
List prepaidList = null;
if(request.getAttribute("prepaidList")!=null){
	prepaidList = (List)request.getAttribute("prepaidList");
}
List curInRateList = null;
if(request.getAttribute("curInRateList")!=null){
	curInRateList = (List)request.getAttribute("curInRateList");
}
List rateList = null;
if(request.getAttribute("rateList")!=null){
	rateList = (List)request.getAttribute("rateList");
}
double differ1 = 0;
if(request.getAttribute("differ1")!=null){
	differ1 = (Double)request.getAttribute("differ1");
}
double differ2 = 0;
if(request.getAttribute("differ2")!=null){
	differ2 = (Double)request.getAttribute("differ2");
}
float withdrawForCarryFee = 0;
String withdrawForCarryDate = "";
String updWithdrawForCarryUid = "";
String updWithdrawForCarryDate = "";
if(request.getAttribute("withdrawForCarry")!=null){
	SysConfig withdrawForCarry = (SysConfig)request.getAttribute("withdrawForCarry");
	updWithdrawForCarryUid = withdrawForCarry.getUpdUid();
	updWithdrawForCarryDate = dateFormat.format(withdrawForCarry.getUpdDate());
	withdrawForCarryFee = Float.parseFloat(withdrawForCarry.getScValue1());
	withdrawForCarryDate = withdrawForCarry.getScValue2(); 
}
int paidAmerceParam = 0;
int keepingDay = 0;
String keepingPeriod = "";
String updPaidAmerceParamUid = "";
String updPaidAmerceParamDate = "";
if(request.getAttribute("lateReceive")!=null){
	SysConfig lateReceive = (SysConfig)request.getAttribute("lateReceive");
	paidAmerceParam = Integer.parseInt(lateReceive.getScValue1());
	keepingDay = Integer.parseInt(lateReceive.getScValue2());
	keepingPeriod = lateReceive.getScValue3();
	updPaidAmerceParamUid = lateReceive.getUpdUid();
	updPaidAmerceParamDate = dateFormat.format(lateReceive.getUpdDate());
}
SysConfig rePrintFee = null;
String currencyValue = "";
if(request.getAttribute("rePrintFee")!=null){
	rePrintFee = (SysConfig)request.getAttribute("rePrintFee");
	if(rePrintFee.getScValue2()!=null && !rePrintFee.getScValue2().equals("")){
		currencyValue = rePrintFee.getScValue2();
	}
}
SysConfig adjustConfig = (SysConfig)request.getAttribute("adjustConfig");

SysConfig shippingFeeConfig = null;
double shippingFee = 0;
if(request.getAttribute("shippingFeeConfig")!=null){
	shippingFeeConfig = (SysConfig)request.getAttribute("shippingFeeConfig");
	if(shippingFeeConfig.getScValue1()!=null){
		shippingFee = Double.parseDouble(shippingFeeConfig.getScValue1());
	}
}

//退書開始日期
String withdrawPeriod = "";
if(request.getAttribute("withdrawPeriod")!=null){
	SysConfig withdraw = (SysConfig)request.getAttribute("withdrawPeriod");
	if(withdraw!=null && withdraw.getScValue1()!=null){
		withdrawPeriod = withdraw.getScValue1();
	}
}
%>
<h4>學期設置</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <tr>
  <td width="20%">當前學期為:</td>
  <td width="30%">
  <input class="currIntake" name="currIntake" value="<%=sc.getScValue1() %>" style="height:18px;border:0;background:#F5F2DA;" readonly="readonly"/>
  <select id="intakeList" name="intakeList">
    <option value="">---請選擇---</option>
   </select>
  </td>
  <td width="50%" align="right">
  <a href="#" class="saveIntake">保存</a>&nbsp;
  <a href="#" class="editIntake">編輯</a>&nbsp;
  <a href="#" class="back">取消</a>
  </td>
 </tr>
</table>
<br>
<h4>退書費用</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <%
 if(withdrawFeeList!=null && !withdrawFeeList.isEmpty()){
	 for(int i=0;i<withdrawFeeList.size();i++){
		 SysConfig feeConfig = new SysConfig();
		 feeConfig = (SysConfig)withdrawFeeList.get(i);
		 %>
		 <tr>
	  	  <td width="20%" >
	  	  <%
	  	  if(feeConfig.getScKey().equals("BIG5")){
	  		  %>
	  		  <label>繁體書籍(<%=feeConfig.getScKey() %>)</label>
	  		  <%
	  	  }else if(feeConfig.getScKey().equals("ENG")){
	  		  %>
	  		  <label>英文書籍(<%=feeConfig.getScKey() %>)</label>
	  		  <%
	  	  }else if(feeConfig.getScKey().equals("GB")){
	  		  %>
	  		  <label>簡體書籍(<%=feeConfig.getScKey() %>)</label>
	  		  <%
	  	  }
	  	  %>
	  	  </td>
	  	  <td width="30%" align="left">
	  	  <input type="hidden" name="oldFee<%=i %>" id="oldFee<%=i %>" value="<%=feeConfig.getScValue1() %>">
	  	  <input type="text" name="fee<%=i %>" id="fee<%=i %>" value="<%=feeConfig.getScValue1() %>" maxlength="4">
	  	  </td>
	  	  <td width="50%" align="right">
	  	  <a href="#" name="feeSave<%=i %>" class="feeSave<%=i %>" onClick="feeSave('<%=feeConfig.getScKey() %>','<%=i %>');">保存</a>&nbsp;
	  	  <a href="#" class="edit<%=i %>" onClick="feeSet('<%=i %>');">編輯</a>&nbsp;
	  	  <a href="#" name="cancel<%=i %>" class="cancel<%=i %>" onClick="cancel('<%=i %>','<%=feeConfig.getScValue1() %>');">取消</a>
	  	  </td>
	 	 </tr>
		 <%
	 }
 }
 %>
 <tr>
  <td>退書開始日期:</td>
  <td>
  <input type="hidden" name="oldWithdrawPeriod" id="oldWithdrawPeriod" value="<%=withdrawPeriod %>">
  <label id="withdrawPeriodValue"><%=withdrawPeriod %></label>
  <input name="withdrawPeriod" type="text" id="withdrawPeriod" class="date" value="" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
  </td>
  <td align="right">
  <a href="#" name="saveWithdrawPeriod" id="saveWithdrawPeriod">保存</a>&nbsp;
  <a href="#" name="editWithdrawPeriod" id="editWithdrawPeriod">編輯</a>&nbsp;
  <a href="#" name="cancelWithdrawPeriod" id="cancelWithdrawPeriod">取消</a>
  </td>
 </tr>
</table>
<br>
<h4>罰款費用</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <tr>
  <td width="20%">逾期繳費（百分率）:</td>
  <td width="30%">
  <input type="hidden" name="oldAmerce" id="oldAmerce" value="<%=amercePercent %>">
  <input type="text" name="amercePercent" id="amercePercent" value="<%=amercePercent %>" maxlength="4">
  </td>
  <td width="50%" align="right">
  <a href="#" class="saveAmerce">保存</a>&nbsp;
  <a href="#" class="editAmerce">編輯</a>&nbsp;
  <a href="#" class="cancelAmerce">取消</a>
  </td>
 </tr>
 <tr>
  <td>最後修改用戶：</td>
  <td><input type="text" name="upduid" id="upduid" value="<%=amerceUid %>" style="height:18px;border:0;width:122px;background:#F5F2DA"></td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>最後修改日期：</td>
  <td><input type="text" name="upddate" id="upddate" class="view" value="<%=amerceDate %>" style="height:18px;border:0;width:150px;background:#F5F2DA"></td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>逾期付款罰款開始日期：</td>
  <td>
  <input type="hidden" name="oldPayAmercePeriod" id="oldPayAmercePeriod" value="<%=payAmercePeriod %>">
  <label id="payAmercePeriodValue"><%=payAmercePeriod %></label>
  <input name="payAmercePeriod" type="text" id="payAmercePeriod" class="date" value="" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
  </td>
  <td align="right">
  <a href="#" name="savePayAmercePeriod" id="savePayAmercePeriod">保存</a>&nbsp;
  <a href="#" name="editPayAmercePeriod" id="editPayAmercePeriod">編輯</a>&nbsp;
  <a href="#" name="cancelPayAmercePeriod" id="cancelPayAmercePeriod">取消</a>
  </td>
 </tr>
</table>
<br>
<h4>補差價參數值</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <tr>
  <td width="80%">
  實價總金額 - 預付價總金額 小於(<) <input type="text" name="differ1" id="differ1" value="<%=differ1 %>" maxlength="4">，不作追討處理
  <input type="hidden" name="oldDiffer1" id="oldDiffer1" value="<%=differ1 %>">
  </td>
  <td width="20%" align="right">
  <a href="#" name="saveDiffer1" onClick="DifferOperation('save', 1);">保存</a>&nbsp;
  <a href="#" name="editDiffer1" onClick="DifferOperation('edit', 1);">編輯</a>&nbsp;
  <a href="#" name="cancelDiffer1" onClick="DifferOperation('cancel', 1);">取消</a>
  </td>
 </tr>
 <tr>
  <td >
  預付價總金額 - 實價總金額 小於(<) <input type="text" name="differ2" id="differ2" value="<%=differ2 %>" maxlength="4">，不作退款處理
  <input type="hidden" name="oldDiffer2" id="oldDiffer2" value="<%=differ2 %>">
  </td>
  <td align="right">
  <a href="#" name="saveDiffer2" onClick="DifferOperation('save', 2);">保存</a>&nbsp;
  <a href="#" name="editDiffer2" onClick="DifferOperation('edit', 2);">編輯</a>&nbsp;
  <a href="#" name="cancelDiffer2" onClick="DifferOperation('cancel', 2);">取消</a>
  </td>
 </tr>
</table>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <tr>
  <td>調整金額原因選項：</td>
  <td>
  <input type="text" name="causeItem" id="causeItem" style="height:18px;width:150px;" maxlength="15">
  <input type="button" name="addItem" id="addItem" value="添 加" onClick="addAdjustItem();">
  </td>
 </tr>
 <tr>
  <td colspan="2">
  <%
  if(adjustConfig.getScValue1()!=null){
	  String adjustItem = adjustConfig.getScValue1();
	  if(adjustItem.length()!=0){
		  String adjust[] = adjustItem.split("/");
		  for(int i=0; i<adjust.length; i++){
			  out.print("<input type='checkbox' name='itemBox' value='"+i+"' class='checkbox'>");
			  out.print((i+1)+"、"+adjust[i]+"; ");
		  }
	  }
  }
  %>&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" value="刪 除" onClick="delAdjustItem();">
  </td>
 </tr>
</table>

<br>
<h4>退運費用</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <tr>
  <td width="20%">退運費（百分率）:</td>
  <td width="30%">
  <input type="hidden" name="oldWithdrawForCarry" id="oldWithdrawForCarry" value="<%=withdrawForCarryFee %>">
  <input type="text" name="withdrawForCarry" id="withdrawForCarry" value="<%=withdrawForCarryFee %>" maxlength="5">
  </td>
  <td width="50%" align="right" rowspan="2">
  <a href="#" class="saveWithdrawForCarry">保存</a>&nbsp;
  <a href="#" class="editWithdrawForCarry">編輯</a>&nbsp;
  <a href="#" class="cancelWithdrawForCarry">取消</a>
  </td>
 </tr>
 <tr>
  <td >退運開始日期:</td>
  <td>
  <input type="hidden" name="oldWithdrawForCarryDate" id="oldWithdrawForCarryDate" value="<%=withdrawForCarryDate %>">
  <input type="text" name="withdrawForCarryDate" id="withdrawForCarryDate" value="<%=withdrawForCarryDate %>" >
  <input name="beginDate" size="10" type="text" class="date" id="beginDate" value="" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
  </td>
 </tr>
 <tr>
  <td>最後修改用戶：</td>
  <td><input type="text" name="updWithdrawForCarryUid" id="updWithdrawForCarryUid" value="<%=updWithdrawForCarryUid %>" style="height:18px;border:0;width:122px;background:#F5F2DA"></td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>最後修改日期：</td>
  <td><input type="text" name="updWithdrawForCarryDate" id="updWithdrawForCarryDate" class="view" value="<%=updWithdrawForCarryDate %>" style="height:18px;border:0;width:150px;background:#F5F2DA"></td>
  <td>&nbsp;</td>
 </tr>
</table>

<br>
<h4>補訂運費</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <tr>
  <td width="20%">運費（百分率）:</td>
  <td width="30%">
  <input type="hidden" name="oldShippingFee" id="oldShippingFee" value="<%=shippingFee %>">
  <input type="text" name="shippingFee" id="shippingFee" value="<%=shippingFee %>" maxlength="5" onKeyUp="value=value.replace(/[^\d]/g,'')">
  </td>
  <td width="50%" align="right" rowspan="2">
  <a href="#" class="saveShippingFee">保存</a>&nbsp;
  <a href="#" class="editShippingFee">編輯</a>&nbsp;
  <a href="#" class="cancelShippingFee">取消</a>
  </td>
 </tr>
</table>

<br>
<h4>保管費用</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <tr>
  <td width="20%">保管費:</td>
  <td width="30%">
  <input type="hidden" name="oldKeepingFee" id="oldKeepingFee" value="<%=paidAmerceParam %>">
  <input type="text" name="keepingFee" id="keepingFee" value="<%=paidAmerceParam %>" maxlength="5">
  </td>
  <td width="50%" align="right" rowspan="3">
  <a href="#" id="saveKeepingParam">保存</a>&nbsp;
  <a href="#" id="editKeepingParam">編輯</a>&nbsp;
  <a href="#" id="cancelKeepingParam">取消</a>
  </td>
 </tr>
 <tr>
  <td>保管期:</td>
  <td>
  <input type="hidden" name="oldKeepingDay" id="oldKeepingDay" value="<%=keepingDay %>">
  <input type="text" name="keepingDay" id="keepingDay" value="<%=keepingDay %>" maxlength="4">
  </td>
 </tr>
 <tr>
  <td>免收保管費截止日期:</td>
  <td>
  <input type="hidden" name="oldKeepingPeriod" id="oldKeepingPeriod" value="<%=keepingPeriod %>">
  <label id="keepingPeriodValue"><%=keepingPeriod %></label>
  <input name="keepingPeriod" type="text" id="keepingPeriod" class="date" value="" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
  </td>
 </tr>
 <tr>
  <td>最後修改用戶：</td>
  <td><input type="text" name="updKeepingFeeUid" id="updKeepingFeeUid" value="<%=updPaidAmerceParamUid %>" style="height:18px;border:0;width:122px;background:#F5F2DA"></td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>最後修改日期：</td>
  <td><input type="text" name="updKeepingFeeDate" id="updKeepingFeeDate" class="view" value="<%=updPaidAmerceParamDate %>" style="height:18px;border:0;width:150px;background:#F5F2DA"></td>
  <td>&nbsp;</td>
 </tr>
</table>

<br>
<h4>重印收據罰款</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <tr>
  <td width="20%">重印收據罰款金額：</td>
  <td width="30%">
  <input type="hidden" name="oldRePrintFee" value="<%=rePrintFee.getScValue1() %>">
  <input type="text" name="rePrintFee" value="<%=rePrintFee.getScValue1() %>">
  <input type="hidden" name="oldCurrencyValue" value="<%=currencyValue %>">
  <label class="currencyValue"><%=currencyValue %></label>
  <select name="rePrintAmerceCurrency" id="rePrintAmerceCurrency">
   <option value="MOP" <%if(currencyValue.equals("MOP")){out.print("selected");} %>>MOP</option>
   <option value="RMB" <%if(currencyValue.equals("RMB")){out.print("selected");} %>>RMB</option>
  </select>
  </td>
  <td width="50%" align="right">
  <a href="#" class="saveRePrintFee">保存</a>&nbsp;
  <a href="#" class="editRePrintFee">編輯</a>&nbsp;
  <a href="#" class="cancelRePrintFee">取消</a>
  </td>
 </tr>
 <tr>
  <td>最後修改用戶：</td>
  <td colspan="2">
  <input type="text" name="updRePrintFeeUid" value="<%=rePrintFee.getUpdUid() %>" style="height:18px;border:0;width:122px;background:#F5F2DA">
  </td>
 </tr>
 <tr>
  <td>最後修改日期：</td>
  <td colspan="2">
  <input type="text" name="updRePrintFeeDate" value="<%=dateFormat.format(rePrintFee.getUpdDate()) %>" style="height:18px;border:0;width:150px;background:#F5F2DA">
  </td>
 </tr>
</table>
<br>
<h4>當前學期MOP 兌換 RMB HKD 匯率設置</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <%
 if(curInRateList == null || curInRateList.isEmpty()){
	 %>
	 <tr>
	   <td>沒有參數!</td>
	 </tr>
	 <%
 }else{
	 for(int i=0; i<curInRateList.size(); i++){
		 SysConfig inRate = (SysConfig)curInRateList.get(i);
		 %>
		 <tr>
		   <td width="20%" >
		     <%=inRate.getScKey() %>（<%=inRate.getScChnDesc() %>）
		     <input type="hidden" name="currencyRateIn<%=i %>" value="<%=inRate.getScKey() %>">
		     <input type="hidden" name="currencyTypeIn<%=i %>" value="<%=inRate.getScType() %>">
		   </td>
		   <td width="15%">
		     <input type="hidden" name="oldRateIn<%=i %>" class="oldRate<%=i %>" value="<%=ToolsOfString.NulltoEmpty(inRate.getScValue1()) %>">
		     <input type="text" name="rateIn<%=i %>" class="rate<%=i %>" value="<%=ToolsOfString.NulltoEmpty(inRate.getScValue1()) %>" maxlength="6">
		   </td>
		   <td width="50%" align="right">
		     <a href="#" name="saveRateIn<%=i %>" class="saveRate" onClick="saveRate('<%="In"+i %>');">保存</a>&nbsp;
		     <a href="#" name="editRateIn<%=i %>" class="editRate" onClick="editRate('<%="In"+i %>');">編輯</a>&nbsp;
		     <a href="#" name="cancelRateIn<%=i %>" class="cancelRate" onClick="cancelRate('<%="In"+i %>');">取消</a>
		   </td>
		 </tr>
		 <%
	 }
 }
 %>
</table>
<br>
<h4>RMB、HKD、USD、MOP匯率設置</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <%
 if(rateList == null){
	 %>
	 <tr>
	  <td>沒有參數!</td>
	 </tr>
	 <%
 }else{
	 for(int i=0; i<rateList.size(); i++){
		 SysConfig rate = (SysConfig)rateList.get(i);
		 %>
		 <tr>
		  <td width="20%" >
		   <%=rate.getScKey() %>（<%=rate.getScChnDesc() %>）
		   <input type="hidden" name="currencyRate<%=i %>" value="<%=rate.getScKey() %>">
		    <input type="hidden" name="currencyType<%=i %>" value="<%=rate.getScType() %>">
		  </td>
		  <td width="15%">
		   <input type="hidden" name="oldRate<%=i %>" class="oldRate<%=i %>" value="<%=rate.getScValue1() %>">
		   <input type="text" name="rate<%=i %>" class="rate<%=i %>" value="<%=rate.getScValue1() %>" maxlength="6">
		  </td>
		  <td width="50%" align="right">
		   <a href="#" name="saveRate<%=i %>" class="saveRate" onClick="saveRate('<%=i %>');">保存</a>&nbsp;
		   <a href="#" name="editRate<%=i %>" class="editRate" onClick="editRate('<%=i %>');">編輯</a>&nbsp;
		   <a href="#" name="cancelRate<%=i %>" class="cancelRate" onClick="cancelRate('<%=i %>');">取消</a>
		  </td>
		 </tr>
		 <%
	 }
 }
 %>
</table>
<br>
<h4>學院預繳書費值</h4>
<table border="0" width="70%" cellpadding="0" cellspacing="0">
 <%
 if(prepaidList == null){
	 %>
	 <tr>
	  <td>沒有參數!</td>
	 </tr>
	 <%
 }else{
	 for(int i=0; i<prepaidList.size(); i++){
		 SysConfig prepaid = (SysConfig)prepaidList.get(i);
		 %>
		 <tr>
		  <td width="20%" ><%=prepaid.getScKey() %>
		  <input type="hidden" name="faculty<%=i %>" value="<%=prepaid.getScKey() %>">
		  </td>
		  <td width="15%">
		  <input type="hidden" name="oldPrepaid<%=i %>" class="oldPrepaid<%=i %>" value="<%=prepaid.getScValue1() %>">
		  <input type="text" name="prepaid<%=i %>" class="prepaid<%=i %>" value="<%=prepaid.getScValue1() %>" maxlength="5">
		  </td>
		  <td width="15%">
		  <input type="hidden" name="oldCurrency<%=i %>" class="oldCurrency<%=i %>" value="<%=prepaid.getScValue2() %>">
		  <input type="text" name="currency<%=i %>" class="currency<%=i %>" value="<%=prepaid.getScValue2() %>" maxlength="3">
		  </td>
		  <td width="50%" align="right">
		  <a href="#" name="savePrepaid<%=i %>" class="savePrepaid" onClick="savePrepaid('<%=i %>');">保存</a>&nbsp;
  		  <a href="#" name="editPrepaid<%=i %>" class="editPrepaid" onClick="editPrepaid('<%=i %>');">編輯</a>&nbsp;
          <a href="#" name="cancelPrepaid<%=i %>" class="cancelPrepaid" onClick="cancelPrepaid('<%=i %>');">取消</a>
		  </td>
		 </tr>
		 <%
	 }
 }
 %>
</table>
</body>
</html>
