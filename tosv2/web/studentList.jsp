<%@ page language="java" import="java.util.*,edu.must.tos.bean.*,edu.must.tos.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>學生列表</title>
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
	  if(parseInt(document.form1.instart.value)>totalPages) {
	   alert("輸入的頁碼已超出最大頁,請重新輸入");
	    document.form1.instart.focus();
		document.form1.instart.select();
	   return;
	  }
	  document.form1.start.value = (parseInt(document.form1.instart.value)-1)*10;
	}
	document.form1.action.value = "StudentListServlet";
	document.form1.submit();
}
//跳轉按鈕獲得焦點
function changeFocus() {  
  document.form1.jump.focus();
}
//頁面加載獲取焦點
function instartFocus() {
  document.form1.instart.focus();
}
//查看學員詳細信息
function openDetail(theURL, winName, features) {
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
   List<Student> list = (List)request.getAttribute("studentList"); 
   Student student = (Student)request.getAttribute("student");
   double totalPages = Double.parseDouble(request.getAttribute("totalPages").toString());
   String start = (String)request.getAttribute("start");
%>
<body onload="instartFocus();">                                
  <form name="form1" action="" method="post">
  <%
  if(student.getStudentNo() != null) {
  %>
  <input type="hidden" name="studentNo" value="<%=student.getStudentNo() %>" />
  <%
  }
  if(student.getChineseName() != null) {
  %>
  <input type="hidden" name="chineseName" value="<%=student.getChineseName() %>" />
  <%
  }
  if(student.getFacultyCode() != null) {
  %>
  <input type="hidden" name="facultyCode" value="<%=student.getFacultyCode() %>" />
  <%
  } 
  if(student.getIdNo() != null) {
  %>
  <input type="hidden" name="idNo" value="<%=student.getIdNo() %>" />
  <%
  }
  if(student.getProgramCode() != null) {
  %>
  <input type="hidden" name="programCode" value="<%=student.getProgramCode() %>" />
  <%
  }
  %>
  <input type="hidden" name="start" value="<%=start %>" />
  <input type="hidden" name="totalPages" value="<%=totalPages %>" />
  
  <table width="99%" align="center" border="0" cellspacing="1" cellpadding="0">
    <tr bgcolor="#C6D6FD">
      <th width="20%" height="25">學號</th>
      <th width="8%" align="center">中文名</th>
      <th width="16%" align="center">英文名</th>
      <th width="8%" align="center">學院編號</th>
      <th width="8%" align="center">課程編號</th>
      <th width="25%" align="center">電郵地址</th>
      <th width="15%" align="center">聯系電話</th>
    </tr>
    <% 
    if(list != null) {
    	for(Student stu : list) {
    		%>
    		<tr  onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF" >
		      <td height="25">
		        <a href="#"><span onclick="openDetail('StudentDetailServlet?psStudentNo=<%=stu.getStudentNo() %>','','width=700,height=400,toolbar=no,status=no,menubar=no,scrollbars=no,resizable=no')"><%=stu.getStudentNo() %></span></a>
		      </td>
		      <td><%=stu.getChineseName() %></td>
		      <td><%=stu.getEnglishName() %></td>
		      <td><%=stu.getFacultyCode() %></td>
		      <td><%=stu.getProgramCode() %></td>
		      <td><%=stu.getEmail() %><br/><%=stu.getEmail2() %></td>
		      <td><%=stu.getContactNo() %><br/><%=stu.getContactNo2() %></td>
		    </tr>
		    <%
		}
    }
    %>
    <tr>
	  <td align="center" colspan="7">
	    <%
        List<PageBean> pagelist=(List)request.getAttribute("page");
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
           &nbsp;&nbsp;
           <input type="button" name="jump" onclick="page('page')" value="跳轉" />
        </label>
      </td>
    </tr>
  </table>
</form>
</body>
</html>

