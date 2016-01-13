<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
<title>訂書系統-登陸版面</title>
<!-- VER 2.8 -->
<style type="text/css">
.textlen {
	height: 18px;
	width: 150px;
}
</style>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
javascript:window.history.forward(1);
     
function init(){
	document.getElementById("psUserId").focus();
}
</script>
</head>
<%
String error = (String)request.getAttribute("error");
%>
<body onload="init()">
<form id="form1" name="form1" method="post" action="LoginServlet">
  <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
     <tr>
       <td height="44" style="background:url(images/login_bg_01.gif)"></td>
     </tr>
     <tr>
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="244">&nbsp;</td>
            <td width="838" style="background:url(images/login_bg_03.gif)"></td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td height="191" style="background:url(images/login_bg_05.gif)">&nbsp;</td>
            <td width="838" style="background:url(images/login_bg_06.gif)" align="center">
              <table width="838" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="176">&nbsp;</td>
                  <td width="518">
                    <table width="518" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td height="35" width="218" align="right">用&nbsp;戶&nbsp;名：</td>
                        <td width="300" align="left">
                          <input type="text" name="psUserId" id="psUserId" class="textlen"/>
                        </td>
                      </tr>
                      <tr>
                        <td height="35" align="right">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;碼：</td>
                        <td align="left">
                          <input type="password" name="psPassWord" id="psPassWord" class="textlen"/>
                        </td>
                      </tr>
                      <tr>
                        <td align="center" colspan="2" height="50">
                          <input type="submit" name="submit" value="登 錄" />
                          &nbsp;&nbsp;&nbsp;&nbsp;
                          <input type="reset" name="reset" value="重 置" />
                        </td>
                      </tr>
                    </table>
                  </td>
                  <td width="142">&nbsp;</td>
                </tr>
              </table>
            </td>
            <td style="background:url(images/login_bg_07.gif)">&nbsp;</td>
          </tr>
          <tr>
            <td height="57">&nbsp;</td>
            <td width="519" style="background:url(images/login_bg_09.gif)"></td>
            <td>&nbsp;</td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <p>&nbsp;</p>
  <% if(error!=null && !error.equals("")) { %>
  <p align=center><font color="red" size=3 >用戶名或密碼不正確，請重新輸入!</font></p>
  <% } %>
</form>
</body>
</html>

