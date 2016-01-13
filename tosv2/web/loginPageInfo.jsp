<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.util.List"/>
<jsp:directive.page import="edu.must.tos.bean.SysConfig"/>
<jsp:directive.page import="edu.must.tos.util.FunctionUtil"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登錄頁面信息</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
<script language="javascript">
function addInfo(oprType){
	document.pageForm.action = "LoginPageInfoServlet";
	document.pageForm.oprType.value = oprType;
	document.pageForm.submit();
}
function editPage(scType, scKey, oprType){
	document.pageForm.action = "LoginPageInfoServlet";
	document.pageForm.oprType.value = oprType;
	document.pageForm.scType.value = scType;
	document.pageForm.scKey.value = scKey;
	document.pageForm.submit();
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
List<SysConfig> infoList = null;
if(request.getAttribute("infoList") != null){
	infoList = (List)request.getAttribute("infoList");
}
%>
<form action="" method="post" name="pageForm">
  <input type="hidden" name="oprType" value="">
  <input type="hidden" name="scType" value="">
  <input type="hidden" name="scKey" value="">
</form>
<h2>登入版面訊息設置</h2>
<table width="98%" border="0" cellpadding="1" cellspacing="1">
  <tr bgcolor="#C6D6FD">
    <th width="15%" height="25">序號</th><th width="55%">內容</th><th width="15%">狀態</th><th width="15%">操作</th>
  </tr>
  <%
  if(infoList!=null && !infoList.isEmpty()){
	  for(SysConfig s : infoList){
  %>
  <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td height="20" align="center"><%=s.getScKey() %></td>
    <td>
    <%
    String str = FunctionUtil.Html2Text(s.getScChnDesc());
    if(str != null && str.length() > 0){
    	if(str.length() > 36){
    		out.print(str.substring(0, 36) + "...");
    	}else{
    		out.print(str);
    	}
    }
    %>
    </td>
    <td align="center"><%if(s.getActInd().equals("Y")){out.print("顯示");}else {out.print("不顯示");} %></td>
    <td align="center">
      <a href="#" onClick="editPage('<%=s.getScType() %>', '<%=s.getScKey() %>', 'editPage');">編輯</a>
    </td>
  </tr>
  <%
  	  }
  }else {
  %>
  <tr>
    <td colspan="4" height="25">沒有任何資料可顯示！</td>
  </tr>
  <%
  }
  %>
</table>
<p><input type="button" value="新 增" onClick="addInfo('addPage');">
</body>
</html>
