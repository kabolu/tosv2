<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新生訂書資料導入</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<style type="text/css">
<!--
#remarks {
	width:618px;
	margin:0 auto;
	border:1px solid #EDAF47;
}
#remarks p{
	font-size: 1em;
	color: #000;
	line-height:20px;
	padding:5px 0 5px 5px;
}
-->
</style>
<script type="text/javascript">
function checkFile(){
	a = document.getElementById("fileName").value;
	b = a.substring(a.length-4, a.length);
	if(a == ""){
		alert("請選擇文件！");
   		return false;
  	}else{
   		if(b == ".xls" || b == ".XLS"){
   			var tip = document.getElementById("tip").value;
   			if(confirm("新生訂書資料是否上載到當前 "+tip+" 學期！")){
   				return true;
   			}else{
   				return false;
   			}
   		}else{
    		alert("你選擇的不是指定檔，請重新選擇！");
    		return false;
   		}
  	}
}
</script>
<%if (session.getAttribute("userId") == null ) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href = 'login.jsp';
</script>
<% } %>
<script language="javascript">
      javascript:window.history.forward(1);
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
String intake = (String)session.getAttribute("curIntake");
%>
<input type="hidden" name="tip" id="tip" value="<%=intake %>">
<p align="center">&nbsp;</p>
<p align="center">
  <span class="blackBold16px">新生訂書資訊導入</span>
  <br>
  <br>
</p>
<form name="form1" id="form1" onSubmit="return checkFile();" action="UploadNewStudOrderServlet" method="post" enctype="multipart/form-data">
  <table width="618" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td colspan="2" align="left">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" align="left"></td>
    </tr>
    <tr>
      <td width="188" align="left">選擇要導入的新生訂書資訊檔:&nbsp;&nbsp;&nbsp;</td>
      <td width="430" align="left">
        <input type="file" id="fileName" name="file">&nbsp;&nbsp;&nbsp;
        <input type="submit"  value=" 導入 ">
      </td>
    </tr>
    <tr>
      <td colspan="2" align="left" height="45">
        <font style="font-size:12px;font-weight:bold;">當前學期：<%=intake %>（若學期值不對，請到系統管理先作當前學期值的設置）</font>
      </td>
    </tr>
  </table>
  <br>
</form>
<div id="remarks">
<p>
備注：
<br>
1、該導入文件是在“新生訂書管理”--“新生訂書資訊導出”功能導出的，而欄位一共有13欄，分別為：學期、學員編號、學員姓名、學員英文名稱、學院編號、課程編號、專業編號、
申請編號、錄取狀況、地區、圖書ISBN、科目編號和確定數量；
<br>
2、重複導入時，只會對記錄進行更新處理；
<br>
</p>
</div>
</body>
</html>
