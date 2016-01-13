<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>圖書實付價導入</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function checkFile(){
	a = document.getElementById("fileName").value;
	b = a.substring(a.length-4, a.length);
	if(a == ""){
		alert("請選擇文件！");
		return;
	}else{
		if(b == ".xls" || b ==".XLS"){
			document.form1.action = "UploadNetPriceServlet";
			document.form1.submit();
		}else{
			alert("你選擇的不是指定檔，請重新選擇！");
			return;
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
<p class="boldTitle">圖書實付價導入</p>
<form name="form1" method="post" enctype="multipart/form-data">
  <table width="618" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td width="200" align="left">選擇要導入的圖書實付價格文件:</td>
      <td width="418" align="left">
        <input type="file" id="fileName" name="file"> （注意：文件必須是一個Excel文件）
      </td>
    </tr>
    <tr>
      <td colspan="2" height="35" align="center">
        <input onClick="checkFile();" type="button" value=" 導 入 ">
      </td>
    </tr>
  </table>
</form>
<div id="remarks">
<p>
備注：
<br>
1、請選擇正確格式的圖書實付價Excel檔；
<br>
2、該檔欄位包括ISBN、Title、Author、Publisher、PublishYear、Edition、NetPrice(MOP)和NetPrice(RMB)，共八個欄位；
<br>
3、ISBN是由數字、英文字母和$符號組成；PublishYear為數字；NetPrice(MOP)和NetPrice(RMB)是由數字和小數點組成，並且小數點前的位數不能超過5位；
<br>
4、該文件可由：圖書資訊管理&gt;&gt;圖書信息查詢&gt;&gt;導出圖書的功能導出Excel表，再在該檔裏的欄位後添加兩列NetPrice(MOP)和NetPrice(RMB)並填寫數值即可；
<br>
</p>
</div>
</body>
</html>
