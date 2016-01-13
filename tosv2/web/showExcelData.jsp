<%@ page language="java" import="java.util.*,edu.must.tos.bean.Model"  pageEncoding="UTF-8"%>
<%@page import="edu.must.tos.bean.BookImport"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上傳圖書資料</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body >
<%
List titleList = (List)session.getAttribute("titleList");
List bookImportList = (List)session.getAttribute("bookImportList");
%>
<p></p>
<form action="SaveBookServlet" method="get" name="form">
  <table border="1" cellpadding="0" cellspacing="0" bordercolor="#EDA938" align="center" width="90%">
  <% if(titleList != null && !titleList.isEmpty()) { %>
    <tr>
      <th height="25"><%=titleList.get(0) %></th>
      <th><%=titleList.get(1) %></th>
      <th><%=titleList.get(2) %></th>
      <th><%=titleList.get(3) %></th>
      <th><%=titleList.get(4) %></th>
      <th><%=titleList.get(5) %></th>
      <th><%=titleList.get(6) %></th>
      <th><%=titleList.get(7) %></th>
      <th><%=titleList.get(8) %></th>
      <th><%=titleList.get(9) %></th>
      <th><%=titleList.get(10) %></th>
      <th><%=titleList.get(11) %></th>
    </tr>
  <%
  }
  if(bookImportList != null && !bookImportList.isEmpty()) {
	  for(int i=0; i<bookImportList.size(); i++){
		  BookImport bookImport = (BookImport)bookImportList.get(i);
		  String courseCode = bookImport.getCourseCode();
		  String majorCode = bookImport.getMajorCode();
		  String language = bookImport.getLanguage();
		  String bookType = bookImport.getBookType();
		  String isbn = bookImport.getIsbn();
		  String title = bookImport.getTitle();
		  String author = bookImport.getAuthor();
		  String publisher = bookImport.getPublisher();
		  String edition = bookImport.getEdition();
		  String publishYear = bookImport.getPublishYear();
		  String grade = bookImport.getGrade();
		  String ce = bookImport.getCore();
		  
  %>
    <tr>
      <td height="20"><%=courseCode %></td>
	  <td><%=majorCode %></td>
	  <td><%=grade %></td>
	  <td><%=ce %></td>
	  <td><%=language %></td>
	  <td><%=bookType %></td>
	  <td><%=isbn %></td>
	  <td><%=title %></td>
	  <td><%=author %></td>
	  <td><%=publisher %></td>
	  <td><%=edition %></td>
	  <td><%=publishYear %></td>
	</tr>
  <%
  	  } 
  }
  %>
  </table>
  <p>
  <div align="center">
    <input type="button" value="保 存" onclick="location.replace('SaveBookServlet')" />
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="返 回" onclick="history.back();"/>
  </div>
  </p>
</form>
</body>
</html>