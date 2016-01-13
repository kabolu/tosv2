<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>學生選科信息查詢</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
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
</script>
<%
if (session.getAttribute("userId") == null) {
%>
<script>
	alert('登陸超時！請重新登陸');
	window.location.href='login.jsp';
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
<form action="StudentEnrolServlet" name="searchForm" id="searchForm" target="bottom">
<table align="center" border="0" cellpadding="0" cellspacing="0" width="720">
  <tr>
    <td width="11%" height="40">&nbsp;學期：</td>
    <td width="9%">
      <select name="intakeList" id="intakeList">
        <%if(intakeList.isEmpty()){ %>
        <option value="">==請選擇==</option>
        <%}else{
        	for(int i=0; i<intakeList.size(); i++){
        		SysConfig sc = (SysConfig)intakeList.get(i);
        %>
        <option value="<%=sc.getScValue1() %>" <%if(sc.getScValue1().equals(intake)){out.print("selected");} %>><%=sc.getScValue1() %></option>
		<%  }
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
	    <%for(int i=0; i<facultyList.size(); i++){
	    	Faculty faculty = (Faculty)facultyList.get(i);
	    %>
	    <option value="<%=faculty.getFacultyCode() %>"><%=faculty.getChineseName() %></option>
	    <%}
        }%>
      </select>
    </td>
    <td width="10	%">課程名稱：</td>
    <td width="35%">
      <select name="prog" id="prog">
        <option value="">===請選擇===</option>
      </select>
    </td>
  </tr>
  <tr>
    <td height="35">&nbsp;學號：</td>
    <td colspan="5">
      <input type="text" name="studentNo" id="studentNo" style="height:18px;">
    </td>
  </tr>
  <tr>
    <td colspan="6" height="35" align="center">
      <input type="submit" value="提 交" name="submit" class="submit">
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="reset" value="重 置" name="reset" class="reset">
    </td>
  </tr>
</table>
</form>
</body>
</html>