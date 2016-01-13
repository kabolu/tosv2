<%@ page language="java" import="java.util.*,edu.must.tos.bean.*,edu.must.tos.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科目列表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
<script language="javascript">
function page(start){
	document.form1.start.value=start;
	var totalPages = parseInt(document.form1.totalPages.value);
	if(start=="page") {
		if(document.form1.instart.value.replace(/ /g,"")=="") {
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
		if(parseInt(document.form1.instart.value)>totalPages) {
			alert("輸入的頁碼已超出最大頁,請重新輸入");
			document.form1.instart.focus();
			document.form1.instart.select();
			return;
		}
		document.form1.start.value=(parseInt(document.form1.instart.value)-1)*10;
	}
	document.form1.action.value="CourseListServlet";
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
function openDetail(theURL,winName,features) {
	window.open(theURL, winName, features);
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<%
List<Course> list = (List)request.getAttribute("courseList");
Course co = (Course)request.getAttribute("course");
double totalPages = Double.parseDouble(request.getAttribute("totalPages").toString());
%>
<body onload="instartFocus();">
<form name="form1" action="" method="post">
<%
if(co.getChineseName() != null) {
%>
  <input type="hidden" name="facultyCode" value="<%=co.getChineseName() %>" />
<%
} if(co.getEnglishName() != null) {
%>
  <input type="hidden" name="englishName" value="<%=co.getEnglishName() %>" />
<%
} if(co.getCourseCode() != null) {
%>
  <input type="hidden" name="chineseName" value="<%=co.getCourseCode() %>" />
<%
} if(co.getFacultyCode() != null) {
%>
  <input type="hidden" name="facultyCode" value="<%=co.getFacultyCode() %>" />
<%
}
%>
  <input type="hidden" name="start" value="" />
  <input type="hidden" value="<%=totalPages %>" name="totalPages" />
  <table width="100%"  align="center" border="0" cellspacing="1" cellpadding="0">
    <tr bgcolor="#C6D6FD">
      <th width="15%">科目編號</th>
      <th width="70%">中文名(英文名)</th>
      <th width="15%">學院編號</th>
    </tr>
    <%for(Course course : list) { %>
    <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
      <td>
        <a href="#">
          <span onclick="openDetail('BookSearchServlet?type=searchCosBooks&courseCode=<%=course.getCourseCode() %>','','width=700,height=300,toolbar=no,status=no,menubar=no,scrollbars=no,resizable=no')">
            <%=course.getCourseCode() %>
          </span>
        </a>
      </td>
      <td><%=course.getChineseName() %><br/><%=course.getEnglishName() %></td>
      <td><%=course.getFacultyCode() %></td>
    </tr>
    <%} %>
    <tr>
      <td align="center" colspan="8">
      <%
      List<PageBean> pagelist = (List)request.getAttribute("page");
      for(PageBean pageBean : pagelist){
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
      %>
        <label>
          &nbsp;&nbsp;
          <input type="text" name="instart" id="len" onchange="changeFocus();" />
          &nbsp;
          <input type="button" name="jump" onclick="page('page')" value="跳轉" />
        </label>
      </td>
    </tr>
  </table>
</form>
</body>
</html>