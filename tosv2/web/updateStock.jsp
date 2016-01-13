<%@ page contentType="text/html; charset=utf-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>盤點資料導入</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
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
<script language="javascript">
function checkFile(){
	a = document.getElementById("fileName").value;
	b = a.substring(a.length-4, a.length);
	if(a == ""){
		alert("請選擇文件！");
		return;
	}else{
		if(b == ".xls" || b ==".XLS"){
			document.updForm.action="UpdateStockServlet";
			document.updForm.submit();
		}else{
			alert("你選擇的不是指定檔，請重新選擇！");
			return;
		}
	}
}
</script>
<%if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<p align="center">&nbsp;</p>
<p align="center">
  <span class="blackBold16px">盤點資料導入更新</span>
  <br/>
  <br/>
</p>
<form name="updForm" method="post" enctype="multipart/form-data">
  <table width="618" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td colspan="2" align="left">&nbsp;</td>
    </tr>
    <tr>
      <td width="208" align="left">選擇要導入的盤點資料更新檔:&nbsp;&nbsp;&nbsp;</td>
      <td width="410" align="left">
        <input type="file" id="fileName" name="file" />（注意：文件必須是一個Excel文件）
      </td>
    </tr>
    <tr>
      <td colspan="2" height="30" align="center">
        <input onclick="checkFile();" type="button" value="導 入" />
      </td>
    </tr>
  </table>
  <br/>
</form>
<div id="remarks">
<p>
備注：
<br/>
1、請選擇正確格式的盤點資料Excel檔；
<br/>
2、該Excel檔欄位包括ISBN、當前庫存量、實際庫存量共三個欄位；
<br/>
3、ISBN是由數字、英文字母和$符號組成；當前庫存量和實際庫存量為正整數數字；
<br/>
4、系統若檢測到Excel文件內的圖書ISBN編號在圖書庫存資料內沒有對應的記錄，則系統會將此Excel表該圖書ISBN編號記錄保存到圖書庫存資料內；
<br/>
</p>
</div>
</body>
</html>

