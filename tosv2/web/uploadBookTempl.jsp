<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>科表資訊導入版面</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function checkForm(){
	a = document.getElementById("fileName").value;
	b = a.substring(a.length-4,a.length);
	if(form1.facultyCode.value==""){
		alert("請選擇學院！");
		form1.facultyCode.focus();
		return false;
	}
	if(a == ""){
		alert("請選擇文件！");
		return false;
	}else{
		if(b == ".xls" || b ==".XLS"){
			return true;
		}else{
			alert("你選擇的不是指定檔，請重新選擇！");
			return false;
		}
	}
	return true;
}
</script>
<% if (session.getAttribute("userId") == null) {%>
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
<%
List<Faculty> facultyList = null;
if(request.getAttribute("facultyList") != null){
	facultyList = (List)request.getAttribute("facultyList");
}
%>
<body>
<p class="boldTitle">科表信息導入</p>
<form action="UploadBookTemplServlet" method="post" name="form1" enctype="multipart/form-data" onSubmit="return checkForm();">
  <table width="618" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td width="208" height="30" align="right">學院：</td>
      <td width="410" align="left">
        <select name="facultyCode">
          <option value="">==請選擇學院==</option>
          <% 
          if(facultyList != null && !facultyList.isEmpty()) {
        	  for(Faculty fa:facultyList){
          %>
          <option value="<%=fa.getFacultyCode() %>"><%=fa.getChineseName() %> (<%=fa.getFacultyCode() %>)</option>
          <%
        	  }
          }
          %>
		</select>
	  </td>
    </tr>
    <tr>
      <td align="right" height="30">選擇要導入的科表資訊檔：</td>
      <td>
        <input type="file" id="fileName" name="file">（注意：文件必須是一個Excel文件）
      </td>
    </tr>
	<tr>
      <td colspan="2" align="center" height="30"><input type="submit" value=" 導 入" ></td>
    </tr>
  </table>
</form>
<div id="remarks">
<p>
備注：
<br>
1、請選擇正確格式的科表資訊Excel檔；
<br>
2、該檔欄位包括Year、Course Code、Stud_grp和MajorCode四個欄位；
<br>
3、Year為數字並且長度為1的數字，代表年級；Course Code為科目編號，該科目編號必填寫正確；Stud_grp為課程標識，允許填入01、02、03和04這四種數值，
01代表預科(PREU)、02代表本科、03代表碩士生、04代表博士生，MajorCode為該科目對應的專業編號；
<br>
4、導入文件時要注意對應所選的學院，否則無法導入成功的！
<br>
</p>
</div>
</body>
</html>
