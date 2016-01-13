<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>學生時段信息記錄</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
#course {
	height: 150px;
	width: 200px;
}
-->
</style>
<script language="javascript">
var XMLHttpReq;
var currentSort;
//創建XMLHttpRequest對象       
function createXMLHttpRequest() {
	if(window.XMLHttpRequest) { //Mozilla 流覽器
		XMLHttpReq = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) { // IE流覽器
		try {
			XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {}
		}
	}
}
//發送請求函數
function sendRequest(url, val) {
	createXMLHttpRequest();
	XMLHttpReq.open("GET", url, true);
	XMLHttpReq.onreadystatechange = function(){processResponse(val);};//指定回應函數
	XMLHttpReq.send(null);  // 發送請求
}
// 處理返回資訊函數
function processResponse(val) {
   	if (XMLHttpReq.readyState == 4) { // 判斷物件狀態
       	if (XMLHttpReq.status == 200) { // 資訊已經成功返回，開始處理資訊
			updateMenu(val);
	    } else { //頁面不正常
	      	alert("您所請求的頁面有異常。");
	    }
	}
}
//更新功能表函數
function updateMenu(val) {
    var prog_code=XMLHttpReq.responseXML.getElementsByTagName("prog_code")
	var prog_name=XMLHttpReq.responseXML.getElementsByTagName("prog_name")
    var list = document.getElementById(val);
    list.options.length=0;
    list.add(new Option("===所有===",""));
    for(var i=0;i<prog_code.length;i++){
      	list.add(new Option(prog_name[i].firstChild.data, prog_code[i].firstChild.data));
    }
}
// 創建級聯功能表函數
function showSubMenu(obj, val) {
	sendRequest("ProgramListServlet?prosearchType=statistic&facultyCode=" + obj, val);
}
</script>
<script language="javascript">
$(document).ready(function(){
	$('.reset').click(function(){
		var prog = document.getElementById("prog");
    	prog.options.length=0;
    	prog.add(new Option("===所有===",""));
	})
	
	$('#unselected').hide();
	$('#selected').show();
		
	$('#Y').click(function(){
		var reportType = $('input[@name=reportType][@checked]').val();
		$('#selected').fadeIn("fast");
		$('#unselected').hide();
		$('#faculty')[0].selectedIndex = 0;
		var prog = document.getElementById("prog");
		prog.options.length=0;
		prog.add(new Option("===所有===",""));
	});
	$('#N').click(function(){
		var reportType = $('input[@name=reportType][@checked]').val();
		$('#selected').hide();
		$('#unselected').fadeIn("fast");
		$('#unselFaculty')[0].selectedIndex = 0;
		var prog = document.getElementById("unselProg");
	   	prog.options.length=0;
	   	prog.add(new Option("===所有===",""));
	});
		
	$('.selectedSubmit').click(function(){
		var intake = $('#intakeList').val();
		
		var periodType = $('input[@name=periodType][@checked]').val();
		if(periodType==undefined){
			alert("請選擇時段類型！");
			return false;
		}
				
		var fromDate = $('#fromDate').val();
		if(fromDate==""){
			alert("請輸入時段從！");
			return false;
		}
		var toDate = $('#toDate').val();
		if(toDate==""){
			alert("請輸入時段至！");
			return false;
		}
		if(fromDate!="" && toDate!=""){
			e = fromDate+" 00:00:00";
			f = toDate+" 23:59:59";
			var d1 = new Date(e.replace(/\-/g, "\/"));
			var d2 = new Date(f.replace(/\-/g, "\/"));
			if(Date.parse(d1) - Date.parse(d2)>0){
				alert(e+"晚於"+f);
			    return false;
			}
		}
			
		var year = $('#academicYear').val();
		var faculty = $('#faculty').val();
		var program = $('#prog').val();
		$.post(
			"CheckSearchServlet",
			{
				type:"StudReceiveTimeRecord",
				intake:intake,
				fromDate:fromDate,
				toDate:toDate,
				year:year,
				faculty:faculty,
				program:program,
				periodType:periodType
			},
			function(result){
				if(result==1){
					alert("沒有符合條件的記錄，請繼續查詢！");
					return false;
				}else{
					window.location.href='StatisticStudReceiveTimeRecord?intake='+intake+'&faculty='+faculty+'&program='+program+'&year='+year+'&fromDate='+fromDate+'&toDate='+toDate+'&periodType='+periodType;
				}
			}
		)
	})
	$('.unselectedSubmit').click(function(){
		var intake = $('#unselIntake').val();
		var faculty = $('#unselFaculty').val();
		var program = $('#unselProg').val();
		var year = $('#unselYear').val();
		var unselperiodType = $('input[@name=unselperiodType][@checked]').val();
		if(unselperiodType==undefined){
			alert("請選擇時段類型！");
			return false;
		}
		$.post(
			"CheckSearchServlet",
			{
				type:"UnselectedReceiveTimeRecord",
				intake:intake,
				year:year,
				unselperiodType:unselperiodType,
				faculty:faculty,
				program:program
			},
			function(result){
				if(result==1){
					alert("沒有符合條件的記錄，請繼續查詢！");
					return false;
				}else{
					window.location.href='StatisticUnSelReceiveTimeRecord?unselperiodType='+unselperiodType+'&intake='+intake+'&faculty='+faculty+'&program='+program+'&year='+year;
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
<h2>學生時段信息記錄</h2>
<%
List facultyList = null;
if(request.getAttribute("facultyList")!=null){
	facultyList = (List)request.getAttribute("facultyList");
}
String intake = null;
if(request.getAttribute("intake")!=null){
	intake = (String)request.getAttribute("intake");
}
List intakeList = null;
if(request.getAttribute("intakeList")!=null){
	intakeList = (List)request.getAttribute("intakeList");
}
SysConfig receiveSC = (SysConfig)request.getAttribute("sc");
String fromDate = "";
String toDate = "";
if(receiveSC!=null && receiveSC.getScValue1()!=null && receiveSC.getScValue2()!=null){
	fromDate = receiveSC.getScValue1().substring(0, 10);
	toDate = receiveSC.getScValue2().substring(0, 10);
}
%>
<table>
  <tr>
    <td>&nbsp;狀態：
      <input type="radio" id="Y" name="reportType" value="Y" style="background-color: #F5F2DA; border: 0;" checked="checked">已選
      &nbsp;&nbsp;
      <input type="radio" id="N" name="reportType" value="N" style="background-color: #F5F2DA; border: 0;">未選
    </td>
  </tr>
</table>
<form action="" name="searchForm" id="searchForm">
  <table align="center" border="0" cellpadding="0" cellspacing="0" width="720" id="selected">
    <tr>
      <td align="left" colspan="6" height="50">&nbsp;學期：
        <select name="intakeList" id="intakeList">
          <%
          if (intakeList.isEmpty()) {
          %>
          <option value="">==請選擇==</option>
          <%
          } else {
        	  for (int i = 0; i < intakeList.size(); i++) {
        		  SysConfig sc = (SysConfig) intakeList.get(i);
          %>
          <option value="<%=sc.getScValue1()%>" <%if(sc.getScValue1().equals(intake)){out.print("selected");} %>><%=sc.getScValue1()%></option>
          <%
          	  }
          }
          %>
        </select>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="radio" name="periodType" value="R" style="background-color: #F5F2DA; border: 0;">領書時段&nbsp;&nbsp;
        <input type="radio" name="periodType" value="P" style="background-color: #F5F2DA; border: 0;">付款時段&nbsp;&nbsp;
		<br><br>時段從：
		<input name="fromDate" value="<%=fromDate %>" size="8" class="inp" type="text" id="fromDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly"/>
		&nbsp;&nbsp;時段至：
		<input name="toDate" value="<%=toDate %>" size="8" class="inp" type="text" id="toDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly"/>
	  </td>
	</tr>
	<tr>
	  <td align="left" colspan="6" height="50">學院名稱：
	    <select	onChange="showSubMenu(this.options[this.options.selectedIndex].value, 'prog')" name="faculty" id="faculty">
		  <%
		  if (facultyList.isEmpty()) {
		  %>
		  <option value="">===請選擇===</option>
		  <%
		  } else {
		  %>
		  <option value="">===所有===</option>
		  <%
		  	  for (int i = 0; i < facultyList.size(); i++) {
		  		  Faculty faculty = (Faculty) facultyList.get(i);
		  %>
		  <option value="<%=faculty.getFacultyCode()%>"><%=faculty.getChineseName()%></option>
		  <%
		  	  }
		  }
		  %>
		</select>
		&nbsp;&nbsp;&nbsp;&nbsp;課程名稱：
		<select name="prog" id="prog">
		  <option value="">===所有===</option>
		</select>
		&nbsp;&nbsp;&nbsp;&nbsp;年級：
		<select name="academicYear" id="academicYear">
		  <option value="">===所有===</option>
		  <option value="1">一年級</option>
		  <option value="2">二年級</option>
		  <option value="3">三年級</option>
		  <option value="4">四年級</option>
		  <option value="5">五年級</option>
		</select>
	  </td>
	</tr>
	<tr>
	  <td colspan="6" height="55" align="center">
	    <input type="button" value="提 交" name="submit" class="selectedSubmit">
	    &nbsp;&nbsp;
	    <input type="reset" value="重 置" name="reset" class="reset">
	    &nbsp;&nbsp;
	    <input type="button" value="返 回" name="back">
	  </td>
	</tr>
  </table>
</form>
<!-- 未選************************************************************ -->
<form action="" name="unselectedForm" id="unselectedForm">
  <table align="center" border="0" cellpadding="0" cellspacing="0" width="720" id="unselected">
    <tr>
      <td align="left" colspan="6" height="50">&nbsp;學期：
        <select name="unselIntake" id="unselIntake">
          <%
          if (intakeList.isEmpty()) {
          %>
          <option value="">==請選擇==</option>
          <%
          } else {
        	  for (int i = 0; i < intakeList.size(); i++) {
        		  SysConfig sc = (SysConfig) intakeList.get(i);
          %>
          <option value="<%=sc.getScValue1()%>" <%if(sc.getScValue1().equals(intake)){out.print("selected");} %>><%=sc.getScValue1()%></option>
		  <%
		  	  }
          }
          %>
        </select>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="radio" name="unselperiodType" value="R" style="background-color: #F5F2DA; border: 0;">領書時段&nbsp;&nbsp;
        <input type="radio" name="unselperiodType" value="P" style="background-color: #F5F2DA; border: 0;">付款時段&nbsp;&nbsp;
	  </td>
	</tr>
	<tr>
	  <td align="left" colspan="6" height="50">學院名稱：
	    <select	onChange="showSubMenu(this.options[this.options.selectedIndex].value, 'unselProg')" name="unselFaculty" id="unselFaculty">
	      <%
	      if (facultyList.isEmpty()) {
	      %>
	      <option value="">===請選擇===</option>
	      <%
	      } else {
	      %>
	      <option value="">===所有===</option>
	      <%
	      	  for (int i = 0; i < facultyList.size(); i++) {
	      		  Faculty faculty = (Faculty) facultyList.get(i);
	      %>
	      <option value="<%=faculty.getFacultyCode()%>"><%=faculty.getChineseName()%></option>
	      <%
	      	  }
	      }
	      %>
	    </select>
	    &nbsp;&nbsp;&nbsp;&nbsp;課程名稱：
	    <select name="unselProg" id="unselProg">
	      <option value="">===所有===</option>
	    </select>
	    &nbsp;&nbsp;&nbsp;&nbsp;年級：
	    <select name="unselYear" id="unselYear">
	      <option value="">===所有===</option>
	      <option value="1">一年級</option>
	      <option value="2">二年級</option>
	      <option value="3">三年級</option>
	      <option value="4">四年級</option>
	      <option value="5">五年級</option>
	    </select>
	  </td>
	</tr>
	<tr>
	  <td colspan="6" height="55" align="center">
	    <input type="button" value="提 交" name="submit" class="unselectedSubmit">
	    &nbsp;&nbsp;
	    <input type="reset" value="重 置" name="reset" class="reset">
	    &nbsp;&nbsp;
	    <input type="button" value="返 回" name="back">
	  </td>
	</tr>
  </table>
</form>
</body>
</html>