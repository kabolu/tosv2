<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新生選科資料導入</title>
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
   			if(confirm("新生選科資料是否上載到當前 "+tip+" 學期！")){
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
<p align="center">
  <span class="blackBold16px">新生選科資訊導入</span>
  <br>
  <br>
</p>
<form name="form1" id="form1" onSubmit="return checkFile();" action="UploadNewCourseServlet" method="post" enctype="multipart/form-data">
  <table width="618" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td colspan="2" align="left">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" align="left"></td>
    </tr>
    <tr>
      <td width="188" align="left">選擇要導入的新生選科資訊檔:&nbsp;&nbsp;&nbsp;</td>
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
1、該Excel檔欄位一共8個欄位，包括Region、Faculty、Program、Major、Status、ISBN、Course和Reward；
<br>
2、Region表示為地區，由英文字母組成；Faculty表示學院編號，由英文字母組成；Program表示課程編號，由英文字母組成；
Major表示專業編號，由英文字母組成；
Status表示為錄取狀況，由英文字母組成；ISBN表示為圖書編號，由數字、英文字母和$符號組成；Course表示為科目編號，由數字和英文字母組成；
Reward表示課程類別，由英文字母組成；
<br>
</p>
</div>
</body>
</html>
