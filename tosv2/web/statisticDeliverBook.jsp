<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>學生領書記錄表</title>
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
	})
})

function checkForm(){
	var status = $('input[@name=status][@checked]').val();
	if(status == undefined){
		alert("請選擇領書狀況！");
		return false;
	}
	var fromDate = $('#fromDate').val();
	var toDate = $('#toDate').val();
	if(fromDate != "" && toDate != ""){
		var d1 = new Date(fromDate.replace(/\-/g, "\/"));
		var d2 = new Date(toDate.replace(/\-/g, "\/"));
		
		if(status == "4"){
			var fromYear = fromDate.substring(0, 4);
			var toYear = toDate.substring(0, 4);
			var fromMonth = fromDate.substring(5, 7);
			var toMonth = toDate.substring(5, 7);
			if(fromYear == toYear){
				if(parseInt(fromMonth, 10) != parseInt(toMonth, 10)){
					alert("請輸入同年分同月份日期查詢!");
					return false;	
				}
			} else {
				alert("請輸入同年分同月份日期查詢!");
				return false;
			}
		}
		
		if(Date.parse(d1) - Date.parse(d2)>0){
			alert(fromDate+"晚於"+toDate);
			return false;
		}
	}else if(fromDate == "" && toDate == ""){
		return true;
	}else{
		alert("請完整填寫訂書日期由和至！");
		return false;
	}
	return true;
}

function checkSummaryForm(){
	if($('input[name=reportType][checked]').val() == undefined){
		alert("請選擇報表類型！");
		return false;
	}	
}
</script>
<%
if (session.getAttribute("userId") == null ) {
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
<h2>學生領書記錄表</h2>
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
<form action="StatisticDeliverBookServlet" name="searchForm" id="searchForm" method="post" onSubmit="return checkForm();">
<table align="center" border="0" cellpadding="0" cellspacing="0" width="750">
  <tr>
    <td width="12%" height="40">&nbsp;學年/學期：</td>
    <td width="9%">
      <select name="intakeList" id="intakeList">
        <%if(intakeList.isEmpty()){ %>
        <option value="">==請選擇==</option>
        <%}else{
        	for(int i=0; i<intakeList.size(); i++){
        		SysConfig sc = (SysConfig)intakeList.get(i);
        %>
        <option value="<%=sc.getScValue1() %>" <%if(sc.getScValue1().equals(intake)){out.print("selected");} %>><%=sc.getScValue1() %></option>
        <%
        	}
        } 
        %>
      </select>
    </td>
    <td width="12%" align="right">學院名稱：</td>
    <td width="23%">
      <select onChange="showSubMenu(this.options[this.options.selectedIndex].value)" name="faculty" id="faculty">
        <%if(facultyList.isEmpty()){ %>
        <option value="">===請選擇===</option>
        <%}else{ %>
        <option value="">===所有===</option>
        <%
        	for(int i=0; i<facultyList.size(); i++){
        		Faculty faculty = (Faculty)facultyList.get(i);
        %>
        <option value="<%=faculty.getFacultyCode() %>"><%=faculty.getChineseName() %></option>
        <%
        	}
        }%>
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
    <td height="40">&nbsp;領書日期由：</td>
    <td colspan="2">
      <input name="fromDate" size="10" type="text" class="inp" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" readonly="readonly" />
    </td>
    <td colspan="2">領書日期至：
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" readonly="readonly" />
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
    <td align="left" colspan="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;領書狀況：&nbsp;&nbsp;
      <input type="radio" name="status" value="1" style="background-color: #F5F2DA;border:0;">已領書&nbsp;&nbsp;
      <input type="radio" name="status" value="2" style="background-color: #F5F2DA;border:0;">未領書&nbsp;&nbsp;
      <input type="radio" name="status" value="3" style="background-color: #F5F2DA;border:0;">缺書&nbsp;&nbsp;
      <!-- <input type="radio" name="status" value="4" style="background-color: #F5F2DA;border:0;">領書簡表 -->
    </td>
  </tr>
  <tr>
    <td colspan="6" height="55" align="center">
      <input type="submit" value="提 交" name="submit" class="submit">&nbsp;&nbsp;
      <input type="reset" value="重 置" name="reset" class="reset">&nbsp;&nbsp;
      <input type="button" value="返 回" name="back">
    </td>
  </tr>
</table>
</form>

<h2>領書匯總報表</h2>
<form action="StatisticDeliverBookServlet" method="post" name="formName" onSubmit="return checkSummaryForm();">
<input type="hidden" name="type" value="summary">
<table border="0" cellpadding="0" cellspacing="0" width="99.9%">
  <tr>
    <td>
      <input type="radio" class="radio" name="reportType" value="A">Report A(根據當日領書情況，導出以下資料：日期、派書人、課程、單數；注：學生全缺書亦會統計在內。)
    </td>
  </tr>
  <tr>
    <td>
      <input type="radio" class="radio" name="reportType" value="B">Report B(根據當日領書情況，導出以下資料：日期、課程、單數；注：學生全缺書亦會統計在內。)
    </td>
  </tr>
  <tr>
    <td>
      <input type="radio" class="radio" name="reportType" value="C">Report C(每日派書統計，導出以下資料：派書日期、訂單序號、學生編號、已派總冊數、派書人)
    </td>
  </tr>
  <tr>
    <td>
      <input type="submit" name="submit" value="提 交">
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="reset" name="reset" value="重 置">
    </td>
  </tr>
</table>
</form>
<%
String error = null;
error = (String)request.getAttribute("error");
if(error!=null && error.equals("error")){
	out.println("<script language='javascript'>alert('沒有符合條件的記錄，請繼續查詢！');history.back();</script>");
}
%>
</body>
</html>