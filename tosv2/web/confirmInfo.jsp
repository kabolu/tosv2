<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>確定窗口</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function returnMsg(result){
	window.returnValue = result;
    window.close();
}
</script>
</head>
<body>
<table align="center">
  <tr>
    <th height="55"><img src="images/information.gif">是否確定取消記錄？</th>
  </tr>
  <tr>
    <td align="center" height="35" valign="bottom">
      <input type="button" name="cancel" value="  否  " onclick="returnMsg('no');">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" name="right" value="  是  " onclick="returnMsg('yes');">
    </td>
  </tr>
</table>
</body>
</html>
