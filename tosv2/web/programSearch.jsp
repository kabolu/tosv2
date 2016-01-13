<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>澳門科技大學----課程資訊查詢</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
#textlen {
	height: 18px;
	width: 150px;
}
#textchinese {
	height: 18px;
	width: 108px;
}
-->
</style>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<% List<Faculty> list = (List)session.getAttribute("facultyList"); %>
<body>
<form id="form1" name="form1" method="post" target="bottom" action="ProgramListServlet" >
  <table width="500" border="0" align="center">
    <tr>
      <td>學&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 院：</td>
      <td>
        <select name="facultyCode">
          <option value="">選選擇學院</option>
          <%
          for(Faculty fa : list) {
          %>
          <option value="<%=fa.getFacultyCode() %>"><%=fa.getChineseName() %></option>
          <%
          }
          %>
        </select>
      </td>
      <td>課程編號：</td>
      <td>
        <label>
          <input type="text" name="programCode" id="textchinese" />
        </label>
      </td>
    </tr>
    <tr>
      <td>中 文 名：</td>
      <td><input type="text" name="chineseName" id="textchinese" /></td>
      <td>英 文 名：</td>
      <td>
        <label>
          <input type="text" name="englishName" id="textlen" />
        </label>
      </td>
    </tr>
    <tr>
      <td colspan="4" align="center">
        <input type="submit" name="Submit"  value="查找" />
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" name="Submit2" value="重置" />
      </td>
    </tr>
  </table>
</form>
</body>
</html>