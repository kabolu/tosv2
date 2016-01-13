<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9">
<title>訂書管理系統</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<% if (session.getAttribute("userId") == null) {%>
	<script>
		alert('登陸超時！請重新登陸');
		window.parent.location.href='login.jsp';
    </script>
<% } %>
</head>
<script>
if(window.screen.width<'1024'){switchSysBar()}
</script>
<script>
function switchSysBar(){
	if (document.getElementById('switchPoint').className=='dis_down'){
		document.getElementById('switchPoint').className='dis_on';
		document.all("frmTitle").style.display="none"
	}else{
		document.getElementById('switchPoint').className='dis_down';
		document.all("frmTitle").style.display=""
	}
}
</script>
<body scroll="no" onResize="javascript:parent.carnoc.location.reload();">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
	<td align="center" valign="middle" bgcolor="#FEFCE5" width="250" id="frmTitle">
    <iframe frameBorder="0" id="carnoc" name="carnoc" scrolling="auto" src="leftmenu.jsp" style="height:100%;visibility:inherit;width:250px;z-index:2;">
    </iframe>
	</td> 
    <td width="20" background="images/dis_bk.gif" valign="middle">
      <table border="0" cellPadding="0" cellSpacing="0" height="104" width="100%">
        <tr>
          <td style="height:100%" onClick="switchSysBar()" id="switchPoint" class="dis_down">&nbsp;</td>
      	</tr>
      </table>
    </td>			
    <td align="center" valign="middle" bgcolor="#FEFCE5" width="90%">
	<iframe frameborder="0" id="main" name="main" scrolling="yes" src="main.jsp" style="height:100%;width:100%;visibility:inherit;z-index:0;"></iframe>
	</td>
  </tr>
</table>
<%
String PAID2 = null;
if(session.getAttribute("PAID2") != null && !session.getAttribute("PAID2").equals("")){
	PAID2 = (String)session.getAttribute("PAID2");
}
if("Y".equals(PAID2)){
	out.print("<script language='javascript'>alert('請修改在系數管理中的罰款費用的百份率');</script>");
}
%>
</body>
</html>

