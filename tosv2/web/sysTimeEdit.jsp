<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import = "edu.must.tos.bean.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改系統時間設置</title>
<script type="text/javascript" src="js/calendar.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function checkForm(){
	if(dateForm.fromDate.value == ""){
		alert("請選擇開始日期！");
		dateForm.fromDate.focus();
		return false;
	}
	if(dateForm.toDate.value == ""){
		alert("請選擇結束日期！");
		dateForm.toDate.focus();
		return false;
	}
	if(dateForm.fromTime.value == ""){
		alert("請填寫開始時間！");
		dateForm.fromTime.focus();
		return false;
	}
	if(dateForm.toTime.value == ""){
		alert("請填寫結束時間！");
		dateForm.toTime.focus();
		return false;
	}
	a = document.getElementById("fromDate").value;
	b = document.getElementById("fromTime").value;
	c = document.getElementById("toDate").value;
	d = document.getElementById("toTime").value;
	if(a != null && b != null && c != null && d != null){
		e = a+" "+b;
		f = c+" "+d;
		var d1 = new Date(e.replace(/\-/g, "\/"));
		var d2 = new Date(f.replace(/\-/g, "\/"));
		if(Date.parse(d1) - Date.parse(d2)>0){
			alert(e+"晚於"+f);
			return false;
		}else if(d1.toString() == d2.toString()){
			alert("你設置的時間是相同的，請檢查！");
			return false;
		}else{
			return true;
		}
	}
}

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
			if(isNaN(hh) == true){
				alert("請輸入時間格式為(hh:mm:ss)!");
				checktext.focus();
				return false;
			}
			pone = timetext.substring(2,3);
			mm = timetext.substring(3,5);
			if(isNaN(mm) == true){
				alert("請輸入時間格式為(hh:mm:ss)!");
				checktext.focus();
				return false;
			}
			ptwo = timetext.substring(5,6);
			ss = timetext.substring(6,8);
			if(isNaN(ss) == true){
				alert("請輸入時間格式為(hh:mm:ss)!");
				checktext.focus();
				return false;
			}
			if((pone == ":") && (ptwo == ":")){
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
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h3>系統時間修改</h3>
<hr>
<%
List timeList = (List)request.getAttribute("timeList");
int id = Integer.parseInt(request.getAttribute("id").toString());
SysConfig sc = (SysConfig)timeList.get(id);
%>
<form action="SysConfTimeServlet" method="post" name="dateForm" onSubmit="return checkForm();">
  <input type="hidden" name="type" value="editSysTime">
  <table>
    <tr>
      <td>時段：</td>
	  <td>
	    <%=sc.getScChnDesc() %>
	    <input type="hidden" name="scType" value="<%=sc.getScType() %>">
	    <input type="hidden" name="chnDesc" value="<%=sc.getScChnDesc()%>">
	    <input type="hidden" name="engDesc" value="<%=sc.getScEngDesc() %>">
	    <input type="hidden" name="creUid" value="<%=sc.getCreUid() %>">
	    <input type="hidden" name="creDate" value="<%=sc.getCreDate() %>">
	  </td>
	</tr>
	<tr>
	  <td>學期：</td>
	  <td><input type="text" class="inp" size="6" name="intake" maxlength="4" readonly="readonly" value="<%=sc.getScKey() %>"></td>
	</tr>
	<tr>
	  <td>從：</td>
	  <td>
	    <input name="fromDate" size="8" class="inp" type="text" id="fromDate" onClick="calendar.show(this);" value="<%=sc.getScValue1().substring(0,10) %>" maxlength="10" readonly="readonly" />
	    <input type="text" name="fromTime" class="inp" id="fromTime" size="6" maxlength="8" value="<%=sc.getScValue1().substring(11, sc.getScValue1().length()) %>" onBlur="return isTime(this)">
	    <font color="red">（注：日期時間格式為yyyy-mm-dd hh:mm:ss）</font>
	  </td>
	</tr>
	<tr>
	  <td>至：</td>
	  <td>
	    <input name="toDate" size="8" class="inp" type="text" id="toDate" value="<%=sc.getScValue2().substring(0,10) %>" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
	    <input type="text" name="toTime" class="inp" id="toTime" size="6" maxlength="8" value="<%=sc.getScValue2().substring(11, sc.getScValue2().length()) %>" onBlur="return isTime(this)">
	    <font color="red">（注：日期時間格式為yyyy-mm-dd hh:mm:ss）</font>
	  </td>
	</tr>
	<%
	if("ONSALE".equals(sc.getScType())){
	%>
	<tr>
	  <td>&nbsp;</td>
	  <td>
	    <input type="checkbox" name="scValue3" value="Y" class="checkbox" <%if("Y".equals(sc.getScValue3())){out.print("checked");} %>>
	    <font color="red">（只開放給研究生新生）</font>
	  </td>
	</tr>
	<%	
	}
	%>
	<tr>
	  <td colspan="2">
	    <input type="submit" value="保 存">&nbsp;&nbsp;
	    <input type="reset" value="重 置">&nbsp;&nbsp;
	    <input type="button" value="返 回" onClick="history.back();">
	  </td>
	</tr>
  </table>
</form>
</body>
</html>