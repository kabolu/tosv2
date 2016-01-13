<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>圖書信息查詢--頂部</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function addBookFrame(){
	window.open('AddBookServlet?type=addPage','','width=800,height=580,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no');
}
function exportBookInfo(){
	window.location.href = "ExportBookServlet";
}
function exportBookPriceInfo(){
	window.location.href = "ExportBookPriceServlet";
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<%
List<Faculty> facultyList = (List)session.getAttribute("facultyList");
%>
<body >
<form id="form1" name="form1" method="post" target="bottom" action="BookSearchServlet">
  <table width="540" cellpadding="0" cellspacing="0" border="0" align="center" >
    <tr>
      <td align="left" height="30" width="90">學院：</td>
      <td width="180">
        <select name="facultyCode">
          <option value="">請選擇學院</option>
          <%
          if(facultyList != null) {
        	  for(Faculty fa : facultyList){
          %>
          <option value="<%=fa.getFacultyCode() %>"><%=fa.getChineseName() %></option>
          <%
        	  }
          }
          %>
        </select>
      </td>
      <td align="left" width="90">科目編號：</td>
      <td width="180">
        <input type="text" name="courseCode" class="inp"/>
      </td>
    </tr>
    <tr>
      <td height="30">圖書ISBN：</td>
      <td>
        <input type="text" name="isbn" class="inp" />
      </td>
      <td>書名：</td>
      <td>
        <input type="text" name="title" class="inp" />
      </td>
    </tr>
    <tr>
      <td height="30">作者：</td>
      <td>
        <input type="text" name="author" class="inp" />
      </td>
      <td>出版商：</td>
      <td>
        <input type="text" name="publisher"  class="inp"/>
      </td>
    </tr>
    <tr>
      <td colspan="4" align="center" height="30">
      <input type="submit" name="find"  value="查 找" />&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="reset" name="reset" value="重 置" />&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" name="addBook" value="添加新書" onclick="addBookFrame();"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" name="exportBooks" title="導出所有有效的圖書資料" value="導出圖書" onclick="exportBookInfo();"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" name="exportPrice" title="導出當前學期有效圖書的價格資料" value="導出圖書價格" onclick="exportBookPriceInfo();"/>
      </td>
    </tr>
  </table>
</form>
</body>
</html>

