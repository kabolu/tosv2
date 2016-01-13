<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上載領書、付款時段</title>
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
   			if(confirm("圖書資料是否上載到當前 "+tip+" 學期！")){
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
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href = 'login.jsp';
</script>
<% } %>
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
<p align="center"><span class="blackBold16px">領書、付款時段導入</span><br>
  <br>
</p>
<form name="form1" id="form1" onSubmit="return checkFile();" action="UploadTimesDataServlet" method="post" enctype="multipart/form-data">
  <table width="618" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td colspan="2" align="left">&nbsp;</td>
    </tr>
    <tr>
      <td width="168" align="left">選擇要導入的時段資訊檔:&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td width="450" align="left">
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
1、請選擇正確格式的時段資料Excel檔；
<br>
2、該Excel檔欄位包括：時段種類、日期、時段從、時段至、人數限制和顯示/不顯示標識等六個欄位；
<br>
3、時段種類分為R（領書時段）和P（付款時段）；日期格式為yyyy/MM/dd，設置Excel單元格格式；時段從和時段至的格式為hh24:mi，設置Excel單元格格式；人數限制為數字，不能超過3位；
<br>
4、顯示/不顯示標識分為Y和N；
<br>
5、若使用Excel導入時，系統不會進行時段衝突的檢測；
<br>
6、所上載的記錄是不會取代原來的記錄，只會在原有的記錄中繼續添加上去的；
</p>
</div>
<p align="center">
<input type="button" name="returnBtn" value="返 回" style="width:50px;padding:0px 0px;" onClick="history.back();" />
</p>
</body>
</html>
