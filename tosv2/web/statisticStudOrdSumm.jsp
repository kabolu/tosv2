<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>統計學生訂書表頁面</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
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
function sendRequest(url) {
	createXMLHttpRequest();
	XMLHttpReq.open("GET", url, true);
	XMLHttpReq.onreadystatechange = processResponse;//指定回應函數
	XMLHttpReq.send(null);  // 發送請求
}
// 處理返回資訊函數
function processResponse() {
   	if (XMLHttpReq.readyState == 4) { // 判斷物件狀態
       	if (XMLHttpReq.status == 200) { // 資訊已經成功返回，開始處理資訊
			updateMenu();
	    } else { //頁面不正常
	      	alert("您所請求的頁面有異常。");
	    }
	}
}
//更新功能表函數
function updateMenu() {
    var prog_code=XMLHttpReq.responseXML.getElementsByTagName("prog_code")
	var prog_name=XMLHttpReq.responseXML.getElementsByTagName("prog_name")
    var list = document.getElementById("prog");
    list.options.length=0;
	list.add(new Option("===所有===",""));
	for(var i=0;i<prog_code.length;i++){
	  	list.add(new Option(prog_name[i].firstChild.data, prog_code[i].firstChild.data));
	}
}
// 創建級聯功能表函數
function showSubMenu(obj) {
	sendRequest("ProgramListServlet?prosearchType=statistic&facultyCode=" + obj);
}
</script>
<script language="javascript">
$(document).ready(function(){
	$('.reset').click(function(){
		var prog = document.getElementById("prog");
    	prog.options.length=0;
    	prog.add(new Option("===所有===",""));
    	
    	$('#academicYear').attr("disabled", false);
    	$('#faculty').attr("disabled", false);
    	$('#prog').attr("disabled", false);
    	$('#fromDate').attr("disabled", false);
    	$('#toDate').attr("disabled", false);
    	$('input[@name=StudType]').attr("disabled", false);
    })
    
    $('#fromDate').click(function(){
    	$('#fromOrderDate').attr("value", '');
    	$('#toOrderDate').attr("value", '');
    })
    $('#toDate').click(function(){
    	$('#fromOrderDate').attr("value", '');
    	$('#toOrderDate').attr("value", '');
    })
    $('#fromOrderDate').click(function(){
    	$('#fromDate').attr("value", '');
    	$('#toDate').attr("value", '');
    })
    $('#toOrderDate').click(function(){
    	$('#fromDate').attr("value", '');
    	$('#toDate').attr("value", '');
    })
			
	$('.submit').click(function(){
		var fromDate = $('#fromDate').val();				
		var toDate = $('#toDate').val();
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
			
		var fromReceiveDate = $('#fromReceiveDate').val();
		var toReceiveDate = $('#toReceiveDate').val();
		if(fromReceiveDate!="" && toReceiveDate!=""){
			e = fromReceiveDate+" 00:00:00";
			f = toReceiveDate+" 23:59:59";
			var d1 = new Date(e.replace(/\-/g, "\/"));
			var d2 = new Date(f.replace(/\-/g, "\/"));
			if(Date.parse(d1) - Date.parse(d2)>0){
				alert(e+"晚於"+f);
			    return false;
			}
		}
				
		var fromOrderDate = $('#fromOrderDate').val();
		var toOrderDate = $('#toOrderDate').val();
		if(fromOrderDate!="" && toOrderDate!=""){
			e = fromOrderDate+" 00:00:00";
			f = toOrderDate+" 23:59:59";
			var d1 = new Date(e.replace(/\-/g, "\/"));
			var d2 = new Date(f.replace(/\-/g, "\/"));
			if(Date.parse(d1) - Date.parse(d2)>0){
				alert(e+"晚於"+f);
			    return false;
			}
		}
			
		var reportType = $('input[@name=reportType][@checked]').val();
		if(reportType == undefined){
			alert("請選擇報表類型！");
			return false;
		}
		var intake = $('#intakeList').val();
		//var intake = $('select[@name=intakeList]option[@selected]').val();
		var num = parseInt(intake.substring(2, 4));
		if(num > 1){
			var academicYear = $('#academicYear').val();
			var studType = $('input[@name=StudType][@checked]').val();
			if(studType != undefined && studType=="new" && academicYear!=1){
				alert("該學期不存在新生記錄，請檢查後再查詢！");
				return false;
			}
		}
					
		var year = $('#academicYear').val();
		var faculty = $('#faculty').val();
		var program = $('#prog').val();
		var studType = $('input[@name=StudType][@checked]').val();
		
		window.location.href='StatisticStudOrdServlet?intakeList='+intake+'&faculty='+faculty+'&prog='+program+'&academicYear='+year+'&StudType='+studType+'&reportType='+reportType+'&fromReceiveDate='+fromReceiveDate+'&toReceiveDate='+toReceiveDate+'&fromOrderDate='+fromOrderDate+'&toOrderDate='+toOrderDate+'&fromDate='+fromDate+'&toDate='+toDate;
	})
	
	$('#receiveDate').hide();
	
	$('input[@name=reportType]').click(function(){
		var type = $(this).val();
		if(type=="detail"){
			$('#receiveDate').show();
			$('#fromReceiveDate').attr("value", '');
			$('#toReceiveDate').attr("value", '');
			
			$('#academicYear').attr("disabled", false);
			$('#faculty').attr("disabled", false);
			$('#prog').attr("disabled", false);
			$('#fromDate').attr("disabled", false);
			$('#toDate').attr("disabled", false);
			$('input[@name=StudType]').attr("disabled", false);
		}else if(type=="summary"){
			$('#receiveDate').hide();
			$('#fromReceiveDate').attr("value", '');
			$('#toReceiveDate').attr("value", '');
			
			$('#academicYear').attr("disabled", false);
			$('#faculty').attr("disabled", false);
			$('#prog').attr("disabled", false);
			$('#fromDate').attr("disabled", false);
			$('#toDate').attr("disabled", false);
			$('input[@name=StudType]').attr("disabled", false);
		}else if(type=="summary_only"){
			$('#receiveDate').hide();
			
			$('#academicYear').attr("value", '');
			$('#academicYear').attr("disabled", true);
			$('#faculty').attr("value", '');
			$('#faculty').attr("disabled", true);
			$('#prog').attr("disabled", true);
			var prog = document.getElementById("prog");
			prog.options.length=0;
			prog.add(new Option("===所有===",""));
			$('#fromDate').attr("value", '');
			$('#fromDate').attr("disabled", true);
			$('#toDate').attr("value", '');
			$('#toDate').attr("disabled", true);
			$('input[@name=StudType]').get(2).checked = true;
			$('input[@name=StudType]').attr("disabled", true);
		}
	})
			
	$('#academicYear').change(function(){
		var year = $(this).val();
		if(year == 1){
			var studType = $('input[@name=StudType][@checked]').val();
			if(studType!=undefined){
				var value = $('input[@name=StudType][@checked]').val();
				if(value!="new"){
					$('input[@type=radio]').attr("checked",'all');
				}
			}
		}else if(year >= 2){
			var studType = $('input[@name=StudType][@checked]').val();
			if(studType!=undefined){
				var value = $('input[@name=StudType][@checked]').val();
				if(value=="new"){
					$('input[@type=radio]').attr("checked",'all');
				}
			}
		}
	})
		
	$('input[@name=StudType]').click(function(){
		var value = $(this).val();
		//var year = $('select[@name=academicYear]option[@selected]').val();
		var year = $('#academicYear').val();
		if(value=="new"){
			if(year>=2){
				$('#academicYear')[0].selectedIndex = 1;
			}
		}else if(value=="old"){
			if(year==1 || year==""){
				$('#academicYear')[0].selectedIndex = 0;
			}
		}
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
<h2>學生訂書統計表</h2>
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
%>
<form action="" name="searchForm" id="searchForm">
  <table align="center" border="0" cellpadding="0" cellspacing="0" width="720">
    <tr>
      <td width="11%" height="40">&nbsp;學年/學期：</td>
      <td width="9%">
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
      </td>
      <td width="12%" align="right">學院名稱：</td>
      <td width="23%">
        <select	onChange="showSubMenu(this.options[this.options.selectedIndex].value)" name="faculty" id="faculty">
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
      </td>
      <td width="10%">課程名稱：</td>
      <td width="35%">
        <select name="prog" id="prog">
          <option value="">===請選擇===</option>
        </select>
      </td>
    </tr>
    <tr>
      <td height="50">&nbsp;年級：</td>
      <td>
        <select name="academicYear" id="academicYear">
          <option value="">===所有===</option>
          <option value="1">一年級</option>
          <option value="2">二年級</option>
          <option value="3">三年級</option>
          <option value="4">四年級</option>
          <option value="5">五年級</option>
        </select>
      </td>
      <td align="left" colspan="4">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;付款更新日期從：
        <input name="fromDate" size="10" type="text" class="inp" id="fromDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
        &nbsp;&nbsp;&nbsp;&nbsp;付款更新日期至：
        <input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
      </td>
    </tr>
    <tr>
      <td colspan="6" height="55" align="left">
        &nbsp;訂書日期從：
        <input name="fromOrderDate" size="10" type="text" class="inp" id="fromOrderDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
        &nbsp;&nbsp;&nbsp;&nbsp;訂書日期至：
        <input name="toOrderDate" size="10" type="text" class="inp" id="toOrderDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
      </td>
    </tr>
    <tr>
      <td colspan="6" height="55" align="left">
        &nbsp;學生類型：&nbsp;&nbsp;
        <input type="radio" name="StudType" value="new"	class="radio">
        &nbsp;新生&nbsp;&nbsp;
        <input type="radio" name="StudType" value="old"	class="radio">
        &nbsp;舊生&nbsp;&nbsp;
        <input type="radio" name="StudType" value="all"	class="radio" checked="checked">
        &nbsp;所有(已有學生証號碼的學生)
      </td>
    </tr>
    <tr>
      <td colspan="6" height="55" align="left">
        &nbsp;報表類型：
        <input type="radio" name="reportType" value="summary" class="radio">
        &nbsp;簡表&nbsp;&nbsp;
        <input type="radio" name="reportType" value="detail" class="radio">
        &nbsp;詳表&nbsp;&nbsp;
        <!--
        <input type="radio" name="reportType" value="summary_only" class="radio">簡表(只與學期作搜索)
		-->
	  </td>
	</tr>
	<tr id="receiveDate">
	  <td colspan="6" height="55" align="left">
	    &nbsp;&nbsp;領書日期從：
	    <input name="fromReceiveDate" size="10" type="text" class="inp" id="fromReceiveDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
	    &nbsp;&nbsp;&nbsp;&nbsp;領書日期至：
	    <input name="toReceiveDate" size="10" type="text" class="inp" id="toReceiveDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly" />
	  </td>
	</tr>
	<tr>
	  <td colspan="6" height="35" align="center">
	    <input type="button" value="提 交" name="submit" class="submit">
	    &nbsp;&nbsp;
	    <input type="reset" value="重 置" name="reset" class="reset">
	    &nbsp;&nbsp;
	    <input type="button" value="返 回" name="back" onClick="window.location.href='main.jsp'">
	  </td>
	</tr>
  </table>
</form>
<div id="msg"></div>
<div id="remarks">
<p>
備注：
<br>
1、付款更新日期和訂書日期只能選取一項作為條件，並不能同時作為條件查詢；
<br>
2、若選取報表類型為‘詳表’的領書日期條件，所查詢的記錄是已領書和缺書的記錄；
</p>
</div>
</body>
</html>
<%
String error = null;
if(request.getAttribute("error")!=null){
	error = (String)request.getAttribute("error");
}
if(error!=null && "error".equals(error)){
	%>
	<script language="javascript">
	alert("沒有所查詢的記錄，請重新搜索！");
	history.back();
	</script>
	<%
}
%>