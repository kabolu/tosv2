<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%@page import="edu.must.tos.util.ToolsOfString"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>學生資料</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function closeStudentDetail() {
	window.close();
}
</script>
</head>
<%
List studentDetailList = (List)request.getAttribute("studentDetailList");
Student student = null;
Faculty faculty = null;
Program program = null;
if(studentDetailList.size()>=3) {
	student = (Student)studentDetailList.get(0);
	faculty = (Faculty)studentDetailList.get(1);
	program = (Program)studentDetailList.get(2);
}
%>
<body>
<h2>學生詳細信息</h2>
<%
if(student!=null && faculty!=null && program!=null ) {
%>
<table width="600" border="1" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="20" width="100" align="right">學號：&nbsp;</td>
    <td><%if(student.getStudentNo()!=null){out.print(student.getStudentNo());}else{out.print("&nbsp;");} %></td>
  </tr>
  <tr>
    <td height="20" align="right">申請編號：&nbsp;</td>
    <td><%if(student.getApplicantNo()!=null){out.print(student.getApplicantNo());}else{out.print("&nbsp;");} %></td>
  </tr>
  <tr>
    <td height="20" align="right">中文名：&nbsp;</td>
    <td><%=student.getChineseName() %></td>
  </tr>
  <tr>
    <td height="20" align="right">英文名：&nbsp;</td>
    <td><%=student.getEnglishName() %></td>
  </tr>
  <tr>
    <td height="20" align="right">身份證號碼：&nbsp;</td>
    <td><%=student.getIdNo() %></td>
  </tr>
  <tr>
    <td height="20" align="right">電子郵箱：&nbsp;</td>
    <td><%=ToolsOfString.NulltoEmpty(student.getEmail())  %></td>
  </tr>
  <tr>
    <td height="20" align="right">聯系電話：&nbsp;</td>
    <td><%=ToolsOfString.NulltoEmpty(student.getContactNo()) %></td>
  </tr>
  <tr>
    <td height="20" align="right">年級：&nbsp;</td>
    <td><%=student.getAcademicYear() %></td>
  </tr>
  <tr>
    <td height="20" align="right">標識：&nbsp;</td>
    <td><%=student.getStud_grp() %></td>
  </tr>
  <tr>
    <td height="20" align="right">專業編號：&nbsp;</td>
    <td><%=student.getMajorCode() %></td>
  </tr>
  <tr>
    <td height="20" align="right">學院中文名：&nbsp;</td>
    <td><%=faculty.getChineseName() %> (<%=student.getFacultyCode() %>)</td>
  </tr>
  <tr>
    <td height="20" align="right">學院英文名：&nbsp;</td>
    <td><%=faculty.getEnglishName() %></td>
  </tr>
  <tr>
    <td height="20" align="right">課程中文名：&nbsp;</td>
    <td><%=program.getChineseName() %> (<%=student.getProgramCode() %>)</td>
  </tr>
  <tr>
    <td height="20" align="right">課程英文名：&nbsp;</td>
    <td><%=program.getEnglishName() %></td>
  </tr>
</table>
<%
} 
%>
<p align="center" style="margin:6px;">
  <input type="button" name="close" onClick="closeStudentDetail();" value="關 閉">
</p>
</body>
</html>