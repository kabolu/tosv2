<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import = "edu.must.tos.bean.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add System Time</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
function Trim(str){
	if(str.charAt(0) == ""){
		str = str.slice(1);
		str = Trim(str);
	}
	return str;
}

function isTime(checktext){
	var timetext;
	var hh,mm,ss;
	var pone,ptwo;
	if(Trim(checktext.value)!=""){
		timetext = Trim(checktext.value);
		if(timetext.length == 8){
			hh = timetext.substring(0,2);
			if(isNaN(hh)==true){
				alert("請輸入時間格式為(hh:mm:ss)!");
				checktext.focus();
				return false;
			}
			pone = timetext.substring(2,3);
			mm = timetext.substring(3,5);
			if(isNaN(mm)==true){
				alert("請輸入時間格式為(hh:mm:ss)!");
				checktext.focus();
				return false;
			}
			ptwo = timetext.substring(5,6);
			ss = timetext.substring(6,8);
			if(isNaN(ss)==true){
				alert("請輸入時間格式為(hh:mm:ss)!");
				checktext.focus();
				return false;
			}
			if((pone==":") && (ptwo==":")){
				if(hh<1 || hh>24){
					alert("小時為24小時內！");
					checktext.focus();
					return false;
				}
				if(mm<0 || mm>60){
					alert("分鐘為60分鐘內！");
					checktext.focus();
					return false;
				}
				if(ss<0 || ss>60){
					alert("秒為60秒內！");
					checktext.focus();
					return false;
				}
			}else{
				alert("請輸入時間格式為(hh:mm:ss)!");
				checktext.focus();
				return false;
			}
		}else{
			alert("請輸入時間格式為(hh:mm:ss)!");
			checktext.focus();
			return false;
		}
	}else{
		return true;
	}
	return true;
}
</script>
<script language="javascript">
function checkForm(){
	if(dateForm.period.value==""){
		alert("請選擇時段！");
		dateForm.period.focus();
		return false;
	}
	if(dateForm.intake.value==""){
		alert("請輸入學期！");
		dateForm.intake.focus();
		return false;
	}else{
		intake = dateForm.intake.value;
		if(intake.length<=3){
			alert("學期位數為4位！");
			dateForm.intake.focus();
			return false;
		}else{
			if(isNaN(intake)==true){
				alert("請輸入正確的學期！");
				dateForm.intake.focus();
				return false;
			}else{
				var x ;
				if(intake.substring(0,1)==0){
					x = parseInt(intake.substring(1,2));
				}else{
					x = parseInt(intake.substring(0,2));
				}
				//    if(x<=8){
				//     alert("請設置當前學期以後的學期，請檢查！");
				//    dateForm.intake.focus();
				//     return false;
				//    }
				var y ;
				if(intake.substring(2,3)==0){
					y = parseInt(intake.substring(3,4));
				}else{
					y = parseInt(intake.substring(2,4));
				}
				if(y > 12){
					alert("你設置的學期月份有誤，請檢查！");
					dateForm.intake.focus();
					return false;
				}
			}
		}
	}
	if(dateForm.fromDate.value==""){
		alert("請選擇開始日期！");
		dateForm.fromDate.focus();
		return false;
	}
	if(dateForm.toDate.value==""){
		alert("請選擇結束日期！");
		dateForm.toDate.focus();
		return false;
	}
	if(dateForm.fromTime.value==""){
		alert("請填寫開始時間！");
		dateForm.fromTime.focus();
		return false;
	}
	if(dateForm.toTime.value==""){
		alert("請填寫結束時間！");
		dateForm.toTime.focus();
		return false;
	}
	a = document.getElementById("fromDate").value;
	b = document.getElementById("fromTime").value;
	c = document.getElementById("toDate").value;
	d = document.getElementById("toTime").value;
	if(a !=null && b !=null && c!=null && d!=null){
		e = a+" "+b;
		f = c+" "+d;
		var d1 = new Date(e.replace(/\-/g, "\/"));
		var d2 = new Date(f.replace(/\-/g, "\/"));
		if(Date.parse(d1) - Date.parse(d2)>0){
			alert(e+"晚於"+f);
			return false;
		}else{
			return true;
		}
	}
}
function displayList(val){
	var type
	if(val != "")
		type = val.substring(0, val.indexOf(','));
	if(type == "ONSALE"){
		document.getElementById("scValue3").style.display = "block";
	} else {
		document.getElementById("scValue3").style.display = "none";
	}
}
</script>
<%
if (session.getAttribute("userId") == null) {
%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href = 'login.jsp';
</script>
<%}%>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h2>系統時間添加</h2>
<%
List timeList = (List)request.getAttribute("timeList");
List scTypeList = (List)request.getAttribute("scType");
if(timeList == null && timeList.isEmpty()){
	out.print("系統數據出錯，請檢查數據庫！");
}else{
	%>
<form action="SysConfTimeServlet" method="post" name="dateForm" onSubmit="return checkForm();">
  <input type="hidden" name="type" value="addSysTime">
  <table>
    <tr>
      <td>時段：</td>
	  <td>
	    <select name="period" onchange="displayList(this.value);">
	      <option value="" >==請選族==</option>
	      <%
	      for(int i=0; i<scTypeList.size(); i++){
	    	  SysConfig sc = (SysConfig)scTypeList.get(i);
	    	  String scType = sc.getScType();
	    	  String scChnDesc = sc.getScChnDesc();
	    	  String scEngDesc = sc.getScEngDesc();
	    	  String period = scType+","+scChnDesc+","+scEngDesc+",";
	      %>
	      <option value="<%=period %>"><%=sc.getScChnDesc() %></option>
	      <%
	      }
	      %>
	    </select>
	  </td>
	</tr>
	<tr>
	  <td>學期：</td><td><input type="text" size="6" class="inp" name="intake" maxlength="4"></td>
	</tr>
	<tr>
	  <td>從：</td>
	  <td>
	    <input name="fromDate" size="8" class="inp" type="text" id="fromDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
	    <input type="text" name="fromTime" class="inp" id="fromTime" size="6" maxlength="8" value="09:00:00" onChange="return isTime(this)">
	    <font color="red">（注：日期時間格式為yyyy-mm-dd hh:mm:ss）</font>
	  </td>
	</tr>
	<tr>
	  <td>至：</td>
	  <td>
	    <input name="toDate" size="8" class="inp" type="text" id="toDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
	    <input type="text" name="toTime" class="inp" id="toTime" size="6" maxlength="8" value="09:00:00" onChange="return isTime(this)">
	    <font color="red">（注：日期時間格式為yyyy-mm-dd hh:mm:ss）</font>
	  </td>
	</tr>
	<tr id="scValue3" style="display: none">
	  <td>&nbsp;</td>
	  <td>
	    <input type="checkbox" name="scValue3" value="Y" class="checkbox">
	    <font color="red">（只開放給研究生新生）</font>
	  </td>
	</tr>
	<tr>
	  <td colspan="2">
	    <input type="submit" value="保 存">&nbsp;&nbsp;
	    <input type="reset" value="重 置">&nbsp;&nbsp;
	    <input type="button" value="返 回" onClick="history.back();">
	  </td>
	</tr>
  </table>
</form>
	<%
}
%>
</body>
</html>