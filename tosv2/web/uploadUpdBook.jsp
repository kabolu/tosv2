<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>圖書資訊更新導入</title>
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
			document.updForm.action = "UploadUpdBookServlet";
			document.updForm.submit();
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
<p class="boldTitle">圖書資訊更新導入</p>
<form name="updForm" method="post" enctype="multipart/form-data">
  <table width="618" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td width="208" align="left">選擇要導入的圖書資訊更新檔:</td>
      <td width="410" align="left">
        <input type="file" id="fileName" name="file">（注意：文件必須是一個Excel文件）
      </td>
    </tr>
    <tr>
      <td colspan="2" height="30" align="center">
        <input onClick="checkFile();" type="button" value=" 導 入 ">
      </td>
    </tr>
  </table>
</form>
<div id="remarks">
<p>
備注：
<br>
1、請選擇正確格式的更新圖書資訊Excel檔；
<br>
2、該Excel檔欄位包括ISBN、Title、Author、Publisher、PublishYear、Edition、Language、Remarks、WithdrawInd、SupplierCode1、SupplierCode2和Supplement共十二個欄位；
<br>
3、ISBN是由數字、英文字母和$符號組成；PublishYear為數字；
<br>
4、Language欄位值為BIG5、GB或ENG；BookType欄位值為RB、CB和TB；WithdrawInd欄位值為Y或N；Supplement是補訂標識，欄位值為Y、N或空值；
<br>
</p>
</div>
</body>
</html>
