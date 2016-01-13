<%@ page language="java" import="java.util.*,edu.must.tos.bean.*,edu.must.tos.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查詢圖書列表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function page(start){
	document.form1.start.value=start;
	var totalPages = parseInt(document.form1.totalPages.value);
	if(start=="page") {
		if(document.form1.instart.value.replace(/ /g,"") == "") {
			alert("頁碼不能為空！");
			document.form1.instart.focus();
			document.form1.instart.select();
			return;
		}
		if(isNaN(document.form1.instart.value)) {
			alert("請輸入整數!");
			document.form1.instart.focus();
			document.form1.instart.select();
			return;
		}
		if(parseInt(document.form1.instart.value) > totalPages) {
			alert("輸入的頁碼已超出最大頁,請重新輸入");
			document.form1.instart.focus();
			document.form1.instart.select();
			return;
		}
		document.form1.start.value = (parseInt(document.form1.instart.value)-1)*10;
	}
	document.form1.action.value = "BookSearchServlet";
	document.form1.submit();
}
//跳轉按鈕獲得焦點
function changeFocus() {
	document.form1.jump.focus();
}
// 頁面加載獲取焦點
function instartFocus() {
	document.form1.instart.focus();
}

function openDetail(theURL,winName,features) { //v2.0
	window.open(theURL,winName,features);
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body onload="instartFocus();">
<%
List<Book> list = (List)request.getAttribute("booklist");
Book book = (Book)request.getAttribute("book");
double totalPages = Double.parseDouble(request.getAttribute("totalPages").toString());
String courseCode = (String)request.getAttribute("courseCode");
String facultyCode = (String)request.getAttribute("facultyCode");
%>
<form action="" name="form1" method="post" >
<%
if(book.getIsbn() != null) {
%>
<input type="hidden" name="isbn" value="<%=book.getIsbn() %>" />
<%
} if(book.getTitle() != null) {
%>
<input type="hidden" name="title" value="<%=book.getTitle() %>" />
<%
} if(book.getAuthor() != null) {
%>
<input type="hidden" name="author" value="<%=book.getAuthor() %>" />
<%
} if(book.getPublisher() != null) {
%>
<input type="hidden" name="publisher" value="<%=book.getPublisher() %>" />
<%
} if(courseCode != null) {
%>
<input type="hidden" name="courseCode" value="<%=courseCode %>"/>
<%
} if(facultyCode != null) {
%>
<input type="hidden" name="facultyCode" value="<%=facultyCode %>"/>
<%
}
%>
<input type="hidden" name="start" value="" />
<input type="hidden" name="totalPages" value="<%=totalPages %>" />

<table width="99.9%" align="center" border="0" cellspacing="1" cellpadding="1" >
  <tr bgcolor="#C6D6FD">
    <th width="15%" height="25">編號</th>
    <th width="50%" >書名</th>
    <th width="15%" >出版商</th>
    <th width="20%" align="center">操作</th>
  </tr>
  <% 
  if(list != null && !list.isEmpty()) {
	  for(Book bookinfo : list) {
	  %>
  <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
	<td height="20">
	  <a href="#">
	    <span onclick="openDetail('BookDetailServlet?isbn=<%=bookinfo.getIsbn() %>','','width=700,height=650,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no')"><%=bookinfo.getIsbn() %></span>
	  </a>
	</td>
	<td><%=bookinfo.getTitle() %></td>
	<td><%=bookinfo.getPublisher() %></td>
	<td align="center">
	  <a href="#" style="BORDER: #b4b4b4 1px solid;">
	    <span onclick="javascript:window.open('EditBookInfoServlet?isbn=<%=bookinfo.getIsbn() %>','','width=800,height=500,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no')">編輯</span>
	  </a>
	  &nbsp;&nbsp;
	  <a style="BORDER: #b4b4b4 1px solid;" title="設置圖書資料為無效" href="CheckIsbnServlet?isbn=<%=bookinfo.getIsbn() %>">刪除</a>
	</td>
  </tr>
	   <%
	  }
  }
  %>
  <tr>
    <td align="center" colspan="4">
    <%
    List<PageBean> pagelist = (List)request.getAttribute("page");
    if(pagelist != null){
    	for(PageBean  pageBean : pagelist){
    		if (pageBean.getPageType().equals("prev")){
    			out.print(" <a href='javascript:page("+pageBean.getOffset()+");' >上一頁</a>&nbsp;");
    		}
    		if (pageBean.getPageType().equals("cur")){
    			out.print("<a href='javascript:page("+pageBean.getOffset()+");' ><font color=#d20000 ><b> "+pageBean.getPage()+"</b></font></a>&nbsp;");
    		}
    		if (pageBean.getPageType().equals("pages")){
    			out.print(" <a href='javascript:page("+pageBean.getOffset()+");' >"+pageBean.getPage()+"</a>&nbsp;");
    		}
    		if (pageBean.getPageType().equals("next")){
    			out.print("<a href='javascript:page("+pageBean.getOffset()+");' >下一頁</a>&nbsp;");
    		}
    	}
    }
    %>
      <label>
        <input type="text" name="instart" id="len" onchange="changeFocus();"/>
        &nbsp;&nbsp;
        <input type="button" name="jump" onclick="page('page')" checked="checked" value="跳轉" />
      </label>
    </td>
  </tr>
</table>
</form>
</body>
</html>