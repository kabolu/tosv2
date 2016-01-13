<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="edu.must.tos.bean.*,java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Modify Receipt Book Time</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
$(document).ready(function(){
	$('#maxNo').blur(function(){
		var max = $('#maxNo').val();
		$.post(
			"ReceiptBookTimeServlet",
			{
				type: "checkMaxNo",
				periodNo: $('#periodNo').val()
			},
			function(result){
				if(max < result){
					alert("當前時段已有"+result+"個學生選取，請檢查！");
					$('#maxNo').focus();
				}
			}
		)
	})

	$('#submit').click(function(){
		var fromDate = $('#fromDate').val();
		if(fromDate == ""){
			alert("開始日期不能為空，請輸入！");
			return false;
		}
		var fromTime = $('#fromTime').val();
		if(fromTime == ""){
			alert("請輸入時段從！");
			$('#fromTime').focus();
			return false;
		}else{
			if(fromTime.length != 5){
				alert("“時段從”中所輸入的格式不符合，請重新輸入！");
				$('#fromTime').focus();
				return false;
			}else{
				var temp = fromTime.indexOf(":");
				if(temp != 2){
					alert("“時段從”中所輸入的格式不符合，請重新輸入！");
					$('#fromTime').focus();
					return false;
				}else{
					var hh = fromTime.substring(0, fromTime.indexOf(":"));
					var mi = fromTime.substring(fromTime.indexOf(":")+1, fromTime.length);
					if(parseInt(hh, 10)>24 || parseInt(mi, 10)>60){
						alert("“時段從”中小時或分鐘填寫不正確，請檢查！");
						$('#fromTime').focus();
						return false;
					}
				}
			}
		}
		var toDate = $('#toDate').val();
		if(toDate == ""){
			alert("結束日期不能為空，請輸入！");
			return false;
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
						$('#toTime').focus();
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
		if(fromDate != "" && fromTime != "" && toDate != "" && toTime != ""){
			e = fromDate+" "+fromTime;
			f = toDate+" "+toTime;
			var d1 = new Date(e.replace(/\-/g, "\/"));
			var d2 = new Date(f.replace(/\-/g, "\/"));
			if(Date.parse(d1) - Date.parse(d2)>0){
				alert(e+"晚於"+f);
			    return false;
			}else if(Date.parse(d1) - Date.parse(d2)==0){
				alert("“時段由”與“時段至”的時間值相等，請重新輸入！");
			    return false;
			}
		}
		var display = $('input[@name=display][@checked]').val();
		var intake = $('#intake').val();
		var periodNo = $('#periodNo').val();
		var periodType = $('input[@name=periodType][@checked]').val();
		$.post(
			"ReceiptBookTimeServlet",
			{
				type: "check",
				oprType: "edit",
				periodType: periodType,
				intake: intake,
				periodNo: periodNo,
				date: "",
				fromDate: fromDate,
				toDate: toDate,
				fromTime: fromTime,
				toTime: toTime,
				maxNo: maxNo
			},
			function(result){
				if(result > 0){
					alert("所輸入的時段跟原有的系統記錄發生衝突，不會被存於系統內，請重新輸入！");
					return false;
				}else{
					$.post(
						"ReceiptBookTimeServlet",
						{
							type: "checkModify",
							periodType: periodType,
							periodNo: periodNo
						},
						function(check){
							if(check == 1){
								if(confirm("當前時段已有學生選取了，是否確定修改內容？")){
									window.location.href = 'ReceiptBookTimeServlet?type=editReceiptBookTime&intake='+intake+'&fromDate='+fromDate+'&toDate='+toDate+'&maxNo='+maxNo+'&fromTime='+fromTime+'&toTime='+toTime+'&periodType='+periodType+'&periodNo='+periodNo+'&display='+display;
								}
							}else{
								window.location.href = 'ReceiptBookTimeServlet?type=editReceiptBookTime&intake='+intake+'&fromDate='+fromDate+'&toDate='+toDate+'&maxNo='+maxNo+'&fromTime='+fromTime+'&toTime='+toTime+'&periodType='+periodType+'&periodNo='+periodNo+'&display='+display;
							}
						}
					)
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
<h2>修改時段管理</h2>
<%
Period period = (Period)request.getAttribute("period");
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String fromDate = format.format(period.getStartTime());
String toDate = format.format(period.getEndTime());
%>
<form action="" method="post" id="modifyForm" name="modifyForm">
<table width="90%" border="0" cellpadding="0" cellspacing="0" >
   <tr>
     <td width="15%" height="30">&nbsp;&nbsp;種類：<input type="hidden" id="periodNo" name="periodno" value="<%=period.getPeriodNo() %>"></td>
     <td width="85%">
       <input type="radio" name="periodType" value="R" <%if(period.getType().equals("R")){out.print("checked");} %> style="background-color: #F5F2DA; border: 0;">領書時段&nbsp;&nbsp;
       <input type="radio" name="periodType" value="P" <%if(period.getType().equals("P")){out.print("checked");} %> style="background-color: #F5F2DA; border: 0;">付款時段&nbsp;&nbsp;
     </td>
   </tr>
   <tr>
     <td height="30">&nbsp;&nbsp;學期：</td>
     <td>
       <input type="text" size="2" class="inp" name="intake" id="intake" value="<%=period.getIntake() %>" readonly="readonly">
     </td>
   </tr>
   <tr>
     <td height="30">&nbsp;&nbsp;時段從：</td>
     <td>
       <input value="<%=fromDate.substring(0, 10) %>" name="fromDate" size="8" class="inp" type="text" id="fromDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
       <input value="<%=fromDate.substring(11, 16) %>" type="text" name="fromTime" class="inp" id="fromTime" size="4" maxlength="5">
       <font color="red">（注：日期時間格式為yyyy-mm-dd hh:mm）</font>
     </td>
   </tr>
   <tr>
     <td height="30">&nbsp;&nbsp;時段至：</td>
     <td>
       <input value="<%=toDate.substring(0, 10) %>" name="toDate" size="8" class="inp" type="text" id="toDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
       <input value="<%=toDate.substring(11, 16) %>" type="text" name="toTime" class="inp" id="toTime" size="4" maxlength="5">
       <font color="red">（注：日期時間格式為yyyy-mm-dd hh:mm）</font>
     </td>
   </tr>
   <tr>
     <td height="30">&nbsp;&nbsp;人數限制：</td>
     <td>
       <input value="<%=period.getMaxNo() %>" type="text" class="inp" name="maxNo" id="maxNo" size="1" maxlength="3">
     </td>
   </tr>
   <tr>
     <td colspan="2" height="30">&nbsp;&nbsp;
       <input type="radio" name="display" value="Y" <%if(period.getActInd().equals("Y")){out.print("checked");} %> style="background-color: #F5F2DA; border: 0;">&nbsp;顯示
       <input type="radio" name="display" value="N" <%if(period.getActInd().equals("N")){out.print("checked");} %> style="background-color: #F5F2DA; border: 0;">&nbsp;不顯示
     </td>
   </tr>
   <tr>
     <td colspan="2" height="40">&nbsp;&nbsp;
       <input type=button name="submit" id="submit" value="保 存">&nbsp;&nbsp;
       <input type="reset" name="reset" value="重 置">&nbsp;&nbsp;
       <input type="button" name="back" value="返 回" onClick="window.location.href='ReceiptBookTimeServlet'">
     </td>
   </tr>
</table>
</form>
</body>
</html>
