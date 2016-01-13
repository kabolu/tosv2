<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<title>取消整體訂購</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h2>取消整體訂購</h2>
<form name="form1" method="post" action="DelOrderListServlet" target="bottom">
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td width="10%" align="right">圖書ISBN:</td>
      <td width="15%" align="left"><input name="isbn" type="text" id="isbn"></td>
      <td width="10%" align="right">科目編號：</td>
      <td width="15%" align="left"><input name="courseCode" type="text" id="courseCode"></td>
      <td width="50%" align="left">
        <input type="submit" name="Submit" value="查 找">
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" name="Submit2" value="重 置">
      </td>
    </tr>
  </table>
</form>
</body>
</html>