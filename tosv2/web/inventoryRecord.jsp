<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*,java.text.*"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>圖書庫存信息表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
List timeList = null;
if(request.getAttribute("timeList") != null){
	timeList = (List)request.getAttribute("timeList");
}
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<h2>圖書庫存信息表</h2>
<form name="searchForm" action="ExportBookInventoryServlet" method="post" >
  <input name="type" type="hidden" value="Export" />
  <table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
    <tr>
      <td height="30" width="10%">期初日期：</td>
      <td width="20%">
        <select name="fromDate" id="fromDate">
          <option value="">==請選擇==</option>
          <%
          if(!timeList.isEmpty()){
        	  for(int i=0; i<timeList.size(); i++){
        		  String time = df.format(timeList.get(i));
          %>
          <option value="<%=time %>"><%=time %></option>
          <%
        	  }
          }
          %>
        </select>
      </td>
      <td>
        <input type="submit" name="submit" value="提 交" />
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" type="reset" value="重 置" />
      </td>
    </tr>
  </table>
</form>
<%
String flag = (String)request.getAttribute("flag");
if("false".equals(flag)){
%>
<script language="javascript">
	alert("沒有符合條件的記錄，請重新搜索！");
	history.back();
</script>
<%
}
%>
</body>
</html>

