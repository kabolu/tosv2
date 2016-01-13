function PriceOnly(e) {
	var KeyCode = (e.keyCode) ? e.keyCode : e.which;
	return ((KeyCode == 8) // backspace
			|| (KeyCode == 9) // tab
			|| (KeyCode == 37) // left arrow
			|| (KeyCode == 39) // right arrow
			|| (KeyCode == 46) // delete
			|| ((KeyCode == 110) && (KeyCode == 190)) // .
			|| ((KeyCode > 47) && (KeyCode < 58)) // 0 - 9
			|| ((KeyCode >= 96) && (KeyCode <= 105))  // 0 - 9
		);
}

function NumOnly(e) {
	var KeyCode = (e.keyCode) ? e.keyCode : e.which;
	return ((KeyCode == 8) // backspace
			|| (KeyCode == 9) // tab
			|| (KeyCode == 37) // left arrow
			|| (KeyCode == 39) // right arrow
			|| (KeyCode == 46) // delete
			|| (KeyCode == 45) // -
			|| ((KeyCode > 47) && (KeyCode < 58)) // 0 - 9
			|| ((KeyCode >= 96) && (KeyCode <= 105))  // 0 - 9
		);
}

function NumWithoutMinus(e) {
	var KeyCode = (e.keyCode) ? e.keyCode : e.which;
	return ((KeyCode == 8) // backspace
			|| (KeyCode == 9) // tab
			|| (KeyCode == 37) // left arrow
			|| (KeyCode == 39) // right arrow
			|| (KeyCode == 46) // delete
			|| ((KeyCode > 47) && (KeyCode < 58)) // 0 - 9
			|| ((KeyCode >= 96) && (KeyCode <= 105))  // 0 - 9
		);
}

//check fromDate and toDate
function checkDateValue(fromDate, toDate){
	a = fromDate;
	b = toDate;
	var d1 = new Date(a.replace(/\-/g, "\/"));
	var d2 = new Date(b.replace(/\-/g, "\/"));
	if(Date.parse(d1) - Date.parse(d2)>0){
		alert(a+"晚於"+b);
		return false;
	}
}

//----------keep fee period
$(document).ready(function(){
	$('#keepingPeriod').addClass('viewDate');
	$('#keepingPeriod').hide();
	
	$('#keepingFee').addClass("view");
	$('#keepingFee').attr("readonly", "readonly");
	
	$('#keepingDay').addClass("view");
	$('#keepingDay').attr("readonly", "readonly");
	
	$('#editKeepingParam').show();
	$('#saveKeepingParam').hide();
	$('#cancelKeepingParam').hide();
	
	$('#editKeepingParam').click(function(){
		$('#saveKeepingParam').show();
		$('#cancelKeepingParam').show();
		$('#editKeepingParam').hide();
		
		$('#keepingFee').removeClass("view");
		$('#keepingFee').addClass("inp");
		$('#keepingFee').attr("readonly", '');
		
		$('#keepingDay').removeClass("view");
		$('#keepingDay').addClass("inp");
		$('#keepingDay').attr("readonly", '');
		
		var keepingPeriodValue = $('#keepingPeriodValue').text();
		$('#keepingPeriodValue').text('');
		$('#keepingPeriodValue').hide();
		$('#keepingPeriod').removeClass('viewDate');
		$('#keepingPeriod').attr('value', keepingPeriodValue);
		$('#keepingPeriod').show();
	})
	
	$('#cancelKeepingParam').click(function(){
		var oldKeepingFee = $('#oldKeepingFee').val();
		var oldKeepingDay = $('#oldKeepingDay').val();
		var oldKeepingPeriod = $('#oldKeepingPeriod').val();
		
		$('#keepingPeriod').attr('value', '');
		$('#keepingPeriod').hide();
		
		$('#keepingPeriodValue').text(oldKeepingPeriod);
		$('#keepingPeriodValue').show();
		
		$('#keepingFee').removeClass("inp");
		$('#keepingFee').addClass("view");
		$('#keepingFee').attr("readonly", "readonly");
		
		$('#keepingDay').removeClass("inp");
		$('#keepingDay').addClass("view");
		$('#keepingDay').attr("readonly", "readonly");
		
		$('#saveKeepingParam').hide();
		$('#cancelKeepingParam').hide();
		$('#editKeepingParam').show();
		
		$('#keepingFee').attr("value", oldKeepingFee);
		$('#keepingDay').attr("value", oldKeepingDay);
	})
	
	$('#saveKeepingParam').click(function(){
		var keepingFee = $('#keepingFee').val();
		var oldKeepingFee = $('#oldKeepingFee').val();
		var keepingDay = $('#keepingDay').val();
		var oldKeepingDay = $('#oldKeepingDay').val();
		var keepingPeriod = $('#keepingPeriod').val();
		var oldKeepingPeriod = $('#oldKeepingPeriod').val();
		if(keepingFee!="" && keepingDay!="" && keepingPeriod!=""){
			if(isNaN(keepingFee)){
				alert("保管費參數設置需為整數！");
				$('#keepingFee').focus();
				return false;
			}else{
				if(keepingFee.indexOf(".")>0){
					alert("保管費參數設置需為整數!")
					$('#keepingFee').focus();
					return false;
				}else{
					if(keepingFee<0){
						alert("保管費參數設置需為正整數！");
						$('#keepingFee').focus();
						return false;
					}
				}
			}
			if(isNaN(keepingDay)){
				alert("保管期參數設置需為整數！");
				$('#keepingDay').focus();
				return false;
			}else{
				if(keepingDay.indexOf(".")>0){
					alert("保管期參數設置需為整數！");
					$('#keepingDay').focus();
					return false;
				}else{
					if(keepingDay<0){
						alert("保管期參數設置需為正整數！");
						$('#keepingDay').focus();
						return false;
					}
				}
			}
			$.post(
				"SysConfTimeServlet",
				{
					type: "keepingFeeParam",
					keepingFee: keepingFee,
					keepingDay: keepingDay,
					keepingPeriod: keepingPeriod
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
						alert("保管費參數設置成功！");
						$('#keepingFee').addClass("view");
						$('#keepingFee').attr("readonly", "readonly");
						
						$('#keepingDay').addClass("view");
						$('#keepingDay').attr("readonly", "readonly");
						
						$('#oldKeepingFee').attr("value", keepingFee);
						$('#oldKeepingDay').attr("value", keepingDay);
						
						$('#updKeepingFeeUid').attr("value", upduid);
						$('#updKeepingFeeDate').attr("value", upddate);
						
						$('#oldKeepingPeriod').attr('value', keepingPeriod);
						$('#keepingPeriodValue').text(keepingPeriod);
						$('#keepingPeriodValue').show();
						
						$('#keepingPeriod').attr('value', '');
						$('#keepingPeriod').hide();
					}else{
						alert("保管費參數設置失敗！");
						$('#keepingFeeValue').text(oldKeepingFee);
						$('#keepingFeeValue').show();
						
						$('#keepingPeriod').attr('value', '');
						$('#keepingPeriod').hide();
					}
					$('#saveKeepingParam').hide();
					$('#cancelKeepingParam').hide();
					$('#editKeepingParam').show();
				}
			)
		}else{
			alert("保管費、保管期和保管收費開始日期參數設置不能為空！");
			return false;
		}
	})
})


//----------pay amerce period
$(document).ready(function(){
	$('#payAmercePeriod').addClass('viewDate');
	$('#payAmercePeriod').hide();
	
	$('#savePayAmercePeriod').hide();
	$('#cancelPayAmercePeriod').hide();
	$('#editPayAmercePeriod').show();
	
	$('#editPayAmercePeriod').click(function(){
		var payAmercePeriodValue = $('#payAmercePeriodValue').text();
		$('#payAmercePeriodValue').text('');
		$('#payAmercePeriodValue').hide();
		$('#payAmercePeriod').removeClass('viewDate');
		$('#payAmercePeriod').attr('value', payAmercePeriodValue);
		$('#payAmercePeriod').show();
		
		$('#savePayAmercePeriod').show();
		$('#cancelPayAmercePeriod').show();
		$('#editPayAmercePeriod').hide();
	})
	
	$('#cancelPayAmercePeriod').click(function(){
		var oldPayAmercePeriod = $('#oldPayAmercePeriod').val();
		
		$('#payAmercePeriod').attr('value', '');
		$('#payAmercePeriod').hide();
		
		$('#payAmercePeriodValue').text(oldPayAmercePeriod);
		$('#payAmercePeriodValue').show();
		
		$('#savePayAmercePeriod').hide();
		$('#cancelPayAmercePeriod').hide();
		$('#editPayAmercePeriod').show();
	})
	
	$('#savePayAmercePeriod').click(function(){
		var oldPayAmercePeriod = $('#oldPayAmercePeriod').val();
		var payAmercePeriod = $('#payAmercePeriod').val();
		if(payAmercePeriod!=""){
			$.post(
				"SysConfTimeServlet",
				{
					type: "periodParams",
					scType: "PAID2",
					scValue: payAmercePeriod
				},
				function(result){
					if(result==0){
						alert("保存失敗！");
						$('#payAmercePeriodValue').text(oldPayAmercePeriod);
						$('#payAmercePeriodValue').show();
						
						$('#payAmercePeriod').attr('value', '');
						$('#payAmercePeriod').hide();
					}else{
						alert("保存成功！");
						
						$('#oldPayAmercePeriod').attr('value', payAmercePeriod);
						$('#payAmercePeriodValue').text(payAmercePeriod);
						$('#payAmercePeriodValue').show();
						
						$('#payAmercePeriod').attr('value', '');
						$('#payAmercePeriod').hide();
					}
					$('#savePayAmercePeriod').hide();
					$('#cancelPayAmercePeriod').hide();
					$('#editPayAmercePeriod').show();
				}
			)
		}else{
			alert("請輸入逾期付款罰款開始日期！");
			return false;
		}
	})
})

//----------withdraw period
$(document).ready(function(){
	$('#withdrawPeriod').addClass('viewDate');
	$('#withdrawPeriod').hide();
	
	$('#saveWithdrawPeriod').hide();
	$('#cancelWithdrawPeriod').hide();
	$('#editWithdrawPeriod').show();
	
	$('#editWithdrawPeriod').click(function(){
		var withdrawPeriodValue = $('#withdrawPeriodValue').text();
		$('#withdrawPeriodValue').text('');
		$('#withdrawPeriodValue').hide();
		$('#withdrawPeriod').removeClass('viewDate');
		$('#withdrawPeriod').attr('value', withdrawPeriodValue);
		$('#withdrawPeriod').show();
		
		$('#saveWithdrawPeriod').show();
		$('#cancelWithdrawPeriod').show();
		$('#editWithdrawPeriod').hide();
	})
	
	$('#cancelWithdrawPeriod').click(function(){
		var oldWithdrawPeriod = $('#oldWithdrawPeriod').val();
		
		$('#withdrawPeriod').attr('value', '');
		$('#withdrawPeriod').hide();
		
		$('#withdrawPeriodValue').text(oldWithdrawPeriod);
		$('#withdrawPeriodValue').show();
		
		$('#saveWithdrawPeriod').hide();
		$('#cancelWithdrawPeriod').hide();
		$('#editWithdrawPeriod').show();
	})
	
	$('#saveWithdrawPeriod').click(function(){
		var oldWithdrawPeriod = $('#oldWithdrawPeriod').val();
		var withdrawPeriod = $('#withdrawPeriod').val();
		if(withdrawPeriod!=""){
			$.post(
				"SysConfTimeServlet",
				{
					type: "periodParams",
					scType: "WITHDRAW",
					scValue: withdrawPeriod
				},
				function(result){
					if(result==0){
						alert("保存失敗！");
						$('#withdrawPeriodValue').text(oldWithdrawPeriod);
						$('#withdrawPeriodValue').show();
						
						$('#withdrawPeriod').attr('value', '');
						$('#withdrawPeriod').hide();
					}else{
						alert("保存成功！");
						
						$('#oldWithdrawPeriod').attr('value', withdrawPeriod);
						$('#withdrawPeriodValue').text(withdrawPeriod);
						$('#withdrawPeriodValue').show();
						
						$('#withdrawPeriod').attr('value', '');
						$('#withdrawPeriod').hide();
					}
					$('#saveWithdrawPeriod').hide();
					$('#cancelWithdrawPeriod').hide();
					$('#editWithdrawPeriod').show();
				}
			)
		}else{
			alert("請輸入退書開始日期！");
			return false;
		}
	})
})