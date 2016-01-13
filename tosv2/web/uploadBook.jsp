<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>圖書信息導入</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
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
<%
String intake = (String)session.getAttribute("curIntake");
%>
<input type="hidden" name="tip" id="tip" value="<%=intake %>">
<p class="boldTitle">圖書信息導入</p>
<form name="form1" id="form1" onSubmit="return checkFile();" action="UploadBookServlet" method="post" enctype="multipart/form-data">
  <table width="620" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td width="170" align="left">選擇要導入的圖書資訊檔:</td>
      <td width="450" align="left" height="30">
        <input type="file" id="fileName" name="file">
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="submit" value=" 導 入 ">
      </td>
    </tr>
    <tr>
      <td colspan="2" align="left" height="45">
        <font class="blackBold12px">當前學期：<%=intake %>（若學期值不對，請到系統管理先作當前學期值的設置）</font>
      </td>
    </tr>
  </table>
</form>
<br/>
<div id="remarks">
<p>
備注：
<br>
1、請選擇正確格式的圖書資訊Excel檔；
<br>
2、該Excel檔欄位包括CourseCode、MajorCode、Grade、C/E、Language、BookType、Isbn、Title、Author、Publisher、PublishYear、Edition、Remarks、Currency、UnitPrice、DisCount、
WithdrawInd、SupplierCode1、SupplierCode2、Supplement、FuturePrice(MOP)、FuturePrice(RMB)和FavourablePrice，一共23個欄位；
<br/>
3、CourseCode和MajorCode欄位只能填入各自一個編號；
<br>
4、Grade表示年級，C/E表示必修/選修；
<br>
5、Isbn是由數字、英文字母和$符號組成；PublishYear為數字；FuturePrice(MOP)和FuturePrice(RMB)是由數字和小數點組成，並且小數點前的位數不能超過5位；
<br>
6、Currency（入貨單價貨幣）、UnitPrice（圖書入貨單價）、DisCount（圖書入貨折扣）欄位為圖書入貨價錢信息,若有入貨資料,入貨價*折扣*等值葡幣匯率的價錢不能大於預售價MOP；
<br/>
7、Language欄位值為BIG5、GB或ENG；BookType欄位值為RB、CB或TB；WithdrawInd欄位值為Y或N；Supplement是補訂標識，欄位值為Y、N或空值；
<br>
8、若要修改預估價或實付價，請使用“圖書實付價導入”和“圖書預估價導入”功能；
<br>
9、SupplierCode1和SupplierCode2為書商1和書商2的編號，不能超過20位；
<br/>
10、FavourablePrice欄位為優惠價價錢；
<br/>
11、通識教育部(GS)的科目對應的專業編號可填寫ALL；
</p>
</div>
</body>
</html>
