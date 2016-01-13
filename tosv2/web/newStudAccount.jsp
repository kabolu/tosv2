<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>會計處新生收款報表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h2>會計處新生收款報表</h2>
<form action="NewStudAccountServlet" method="post" name="formName">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td>
      <input type="radio" class="radio" name="type" value="A" checked="checked">新生收費重複記錄
    </td>
  </tr>
  <tr>
    <td>
      <input type="radio" class="radio" name="type" value="B">新生收費未補差價記錄
    </td>
  </tr>
  <tr>
    <td>
      <input type="radio" class="radio" name="type" value="C">研究生新生收費記錄
    </td>
  </tr>
  <tr>
    <td>
      <input type="submit" name="submit" value="提 交">
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="reset" name="reset" value="重 置">
    </td>
  </tr>
</table>
</form>
</body>
</html>