<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="edu.must.tos.bean.Book"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.BookInventory"%>
<%@page import="edu.must.tos.bean.BookStockInBean"%>
<%@page import="edu.must.tos.bean.SysConfig"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>圖書&科目列表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<script language="javascript">
function doSearch(val){
	document.forms[0].submit();
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
<body>
<%
List cosBooksList = null;
if(request.getAttribute("cosBooksList") != null){
	cosBooksList = (List)request.getAttribute("cosBooksList");
}
String intake = null;
if(request.getAttribute("intake") != null){
	intake = (String)request.getAttribute("intake");
}
String courseCode = null;
if(request.getAttribute("courseCode") != null){
	courseCode = (String)request.getAttribute("courseCode");
}
List intakeList = null;
if(request.getAttribute("intakeList") != null){
	intakeList = (List)request.getAttribute("intakeList");
}
%>
<h2>圖書與科目列表</h2>
<form name="searchForm" action="BookSearchServlet" method="post" >
  <input type="hidden" name="type" value="searchCosBooks">
  <input type="hidden" name="courseCode" value="<%=courseCode %>">
  <table width="60%" cellpadding="0" cellspacing="0" border="0" class="searchTable">
    <tr>
      <td height="30" width="15%">科目編號：</td>
      <td width="35%"><%=courseCode %></td>
      <td width="10%">學期：</td>
      <td width="40%">
        <select name="intake" onChange="doSearch(this.value);">
          <%
          for(int i=0, j=intakeList.size(); i<j; i++){
        	  SysConfig sc = (SysConfig)intakeList.get(i);
          %>
          <option value="<%=sc.getScValue1() %>" <%if(intake.equals(sc.getScValue1())){out.print("selected");}%> ><%=sc.getScValue1() %></option>
          <%
          }
          %>
        </select>
      </td>
    </tr>
  </table>
</form>

<table width="99.9%" cellpadding="0" cellspacing="1" border="0">
  <tr bgcolor="#C6D6FD">
    <th height="25" width="15%">圖書ISBN</th>
    <th width="30%">書名</th>
    <th width="20%">作者</th>
    <th width="25%">出版社</th>
    <th width="5%">版本</th>
    <th width="5%">語言</th>
  </tr>
  <%
  if(cosBooksList != null && !cosBooksList.isEmpty()){
	  for(int i=0, j=cosBooksList.size(); i<j; i++){
		  Book book = (Book)cosBooksList.get(i);
  %>
  <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td height="25"><%=book.getIsbn() %></td>
    <td><%=book.getTitle() %></td>
    <td align="center"><%=book.getAuthor() %></td>
    <td align="center"><%=book.getPublisher() %></td>
    <td align="center"><%=book.getEdition() %></td>
    <td align="center"><%=book.getLanguage() %></td>
  </tr>
  <%
	  }	  
  }else{
  %>
  <tr bgcolor="#FFFBEF">
    <td height="25" colspan="6">沒有記錄!</td>
  </tr>
  <%
  }
  %>  
</table>
</body>
</html>
