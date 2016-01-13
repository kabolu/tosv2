<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>置換圖書</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function checkForm(){
	var searchIsbn = searchForm.isbn.value;
	if(searchIsbn == ""){
		alert("請輸入搜索的圖書ISBN編號！");
		return false;
	}
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h2>置換圖書</h2>
<form action="ChangeBookServlet" method="post" name="searchForm" target="bottom" style="margin:0px;" onSubmit="return checkForm();">
  <table border="0">
    <tr>
      <td width="100">
        <input type="radio" name="changetype" value="S" checked="checked">
        <label>學生</label>
      </td>
      <td width="100" align="right">開始日期：</td>
      <td>
        <input name="fromDate" size="10" type="text" class="inp" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" style="height:18px;" readonly="readonly" />
      </td>
      <td width="100" align="right">結束日期：</td>
      <td>
        <input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" style="height:18px;" readonly="readonly" />
      </td>
    </tr>
    <tr>
      <td>
        <input type="radio" name="changetype" value="P" >
        <label>代購</label>
      </td>
      <td align="right">ISBN：</td>
      <td colspan="3">
        <input type="text" name="isbn" class="inp">        
      </td>
    </tr>    
    <tr>
      <td height="50">&nbsp;</td>
      <td colspan="4" align="center">
        <input type="submit" name="submit" value=" 搜索 ">
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" name="reset" value=" 重置 ">
      </td>
    </tr>
    
  </table>
</form>
</body>
</html>
