<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加領書/付款時段</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
$(document).ready(function(){
	$('#submit').click(function(){
		var periodType = $('input[@name=periodType][@checked]').val();
		if(periodType == undefined){
			alert("請選擇時段種類！");
			return false;
		}
		var intake = $('#intake').val();
		if(intake == ""){
			alert("請輸入學期！");
			$('#intake').focus();
			return false;
		}else{
			if(isNaN(intake)){
				alert("學期：只可輸入數字來設定，請重新輸入！");
				$('#intake').attr("value", '');
				$('#intake').focus();
				return false;
			}else{
				if(intake.length!=4){
					alert("學期要為四位數字，請檢查！");
					$('#intake').focus();
					return false;
				}
			}
		}
		
		var date = $('#date').val();
		if(date == ""){
			alert("請輸入日期！");
			return false;
		}
		
		var fromTime = $('#fromTime').val();
		if(fromTime == ""){
			alert("請輸入領書時段從！");
			$('#fromTime').focus();
			return false;
		}else{
			if(fromTime.length != 5){
				alert("“時段從”中所輸入的格式不符合，請重新輸入！");
				$('#fromTime').focus();
				return false;
			}else{
				var temp = fromTime.indexOf(":");
				if(temp!=2){
					alert("“時段從”中所輸入的格式不符合，請重新輸入！");
					$('#fromTime').focus();
					return false;
				}else{
					var hh = fromTime.substring(0, fromTime.indexOf(":"));
					var mi = fromTime.substring(fromTime.indexOf(":")+1, fromTime.length);
					if(parseInt(hh, 10)>24 || parseInt(mi, 10)>60){
						alert("“時段從”中小時或分鐘填寫不正確，請檢查！");
						return false;
					}
				}
			}
		}
		var toTime = $('#toTime').val();
		if(toTime == ""){
			alert("請輸入時段至！");
			$('#toTime').focus();
			return false;
		}else{
			if(toTime.length != 5){
				alert("“時段至”中所輸入的格式不符合，請重新輸入！");
				$('#toTime').focus();
				return false;
			}else{
				var temp = toTime.indexOf(":");
				if(temp != 2){
					alert("“時段至”中所輸入的格式不符合，請重新輸入！");
					$('#toTime').focus();
					return false;
				}else{
					var hh = toTime.substring(0, toTime.indexOf(":"));
					var mi = toTime.substring(toTime.indexOf(":")+1, toTime.length);
					if(parseInt(hh, 10)>24 || parseInt(mi, 10)>60){
						alert("“時段至”中小時或分鐘填寫不正確，請檢查！");
						return false;
					}
				}
			}
		}
		var maxNo = $('#maxNo').val();
		if(maxNo == ""){
			alert("請輸入限制人數！");
			$('#maxNo').focus();
			return false;
		}else{
			if(isNaN(maxNo)){
				alert("人數限制：只可輸入數字來設定，請重新輸入！");
				$('#maxNo').attr("value", '');
				$('#maxNo').focus();
				return false;
			}
		}
		if(fromTime != "" && toTime != ""){
			var fromhh = fromTime.substring(0, fromTime.indexOf(":"));
			var frommi = fromTime.substring(fromTime.indexOf(":")+1, fromTime.length);
			var tohh = toTime.substring(0, toTime.indexOf(":"));
			var tomi = toTime.substring(toTime.indexOf(":")+1, toTime.length);
			if(parseInt(fromhh, 10)>parseInt(tohh, 10)){
				alert("“時段至”早於“時段從”，不會被存於系統內，請重新輸入！");
				$('#fromTime').focus();
				return false;
			}else if(parseInt(fromhh, 10) == parseInt(tohh, 10)){
				if(parseInt(frommi, 10) > parseInt(tomi, 10)){
					alert("“時段至”早於“時段從”，不會被存於系統內，請重新輸入！");
					$('#fromTime').focus();
					return false;
				}else if(parseInt(frommi, 10) == parseInt(tomi, 10)) {
					alert("“時段至”與“時段從”為相同時間，不會被存於系統內，請重新輸入！");
					$('#fromTime').focus();
					return false;
				}
			}
		}
		
		var display = $('input[@name=display][@checked]').val();
		if(display == undefined){
			alert("請選擇狀態顯示或不顯示！");
			return false;
		}
		
		$.post(
			"ReceiptBookTimeServlet",
			{
				type: "check",
				oprType: "add",
				periodType: periodType,
				intake: intake,
				date: date,
				fromTime: fromTime,
				toTime: toTime,
				maxNo: maxNo
			},
			function(result){
				if(result > 0){
					alert("所輸入的時段跟原有的系統記錄發生衝突，不會被存於系統內，請重新輸入！");
					return false;
				}else{
					window.location.href = 'ReceiptBookTimeServlet?type=addReceiptBookTime&intake='+intake+'&date='+date+'&maxNo='+maxNo+'&fromTime='+fromTime+'&toTime='+toTime+'&periodType='+periodType+'&display='+display;
				}
			}
		)
	})
})
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
<h3>添加領書/付款時段</h3>
<hr>
<form action="" method="post" name="addForm" id="addForm">
<table width="90%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="15%">&nbsp;&nbsp;種類：</td>
    <td width="85%">
      <input type="radio" name="periodType" value="R" style="background-color: #F5F2DA; border: 0;">領書時段&nbsp;&nbsp;
      <input type="radio" name="periodType" value="P" style="background-color: #F5F2DA; border: 0;">付款時段&nbsp;&nbsp;
    </td>
  </tr>
  <tr>
    <td>&nbsp;&nbsp;學期：</td>
    <td>
      <input type="text" size="2" class="inp" name="intake" value="<%=(String)session.getAttribute("curIntake") %>" id="intake" maxlength="4">
    </td>
  </tr>
  <tr>
    <td>&nbsp;&nbsp;日期：</td>
    <td>
      <input name="date" size="8" class="inp" type="text" id="date" onClick="calendar.show(this);" maxlength="10" readonly="readonly"/>
    </td>
  </tr>
  <tr>
    <td>&nbsp;&nbsp;時段從：</td>
    <td>
      <input type="text" class="inp" name="fromTime" id="fromTime" size="8" maxlength="5">&nbsp;&nbsp;&nbsp;&nbsp;
      <font color="red">(*時間格式為 hh:mi)</font>
    </td>
  </tr>
  <tr>
    <td>&nbsp;&nbsp;時段至：</td>
    <td>
      <input type="text" class="inp" name="toTime" id="toTime" size="8" maxlength="5">&nbsp;&nbsp;&nbsp;&nbsp;
      <font color="red">(*時間格式為 hh:mi)</font>
    </td>
  </tr>
  <tr>
    <td>&nbsp;&nbsp;人數限制：</td>
    <td><input type="text" class="inp" name="maxNo" id="maxNo" size="1" maxlength="3"></td>
  </tr>
  <tr>
    <td colspan="2" height="30">&nbsp;&nbsp;
      <input type="radio" name="display" value="Y" style="background-color: #F5F2DA; border: 0;" checked="checked">&nbsp;顯示
      <input type="radio" name="display" value="N" style="background-color: #F5F2DA; border: 0;">&nbsp;不顯示
    </td>
  </tr>
  <tr>
    <td colspan="2" height="40">&nbsp;&nbsp;
      <input type="button" name="submit" id="submit" value="保 存">&nbsp;&nbsp;
      <input type="reset" name="reset" value="重 置">&nbsp;&nbsp;
      <input type="button" name="back" value="返 回" onClick="window.location.href='ReceiptBookTimeServlet'">
    </td>
  </tr>
</table>
</form>
</body>
</html>
