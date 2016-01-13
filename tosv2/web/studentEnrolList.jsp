<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*,edu.must.tos.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>學生選科列表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function page(start){
	document.form1.start.value = start;
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
	  	document.form1.start.value=(parseInt(document.form1.instart.value)-1)*10;
	}
	document.form1.action.value = "StudentEnrolServlet";
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
//查看學員詳細信息
function openDetail(theURL,winName,features) { //v2.0
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
List studEnrolList = (List)request.getAttribute("studEnrolList");
double totalPages = Double.parseDouble(request.getAttribute("totalPages").toString());
String start = (String)request.getAttribute("start");
%>
<body onLoad="instartFocus();">
  <form name="form1" action="" method="post" >
  <%
  if(request.getAttribute("studentNo") != null) {
  %>
  <input type="hidden" name="studentNo" value="<%=request.getAttribute("studentNo") %>" />
  <%
  }
  if(request.getAttribute("prog") != null){
  %>
  <input type="hidden" name="prog" value="<%=request.getAttribute("prog") %>" />
  <%
  }
  if(request.getAttribute("faculty") != null) {
  %>
  <input type="hidden" name="faculty" value="<%=request.getAttribute("faculty") %>" />
  <%
  } 
  if(request.getAttribute("intakeList") != null) {
  %>
  <input type="hidden" name="intakeList" value="<%=request.getAttribute("intakeList") %>" />
  <%
  }
  %>
  <input type="hidden" name="start" value="<%=start %>" />
  <input type="hidden" name="totalPages" value="<%=totalPages %>" />
  
  <table width="99.9%" align="center" border="0" cellspacing="1" cellpadding="0">
    <tr bgcolor="#C6D6FD">
      <th width="20%" height="25">學員編號</th>
      <th width="15%">學員中文姓名</th>
      <th width="15%">學員英文姓名</th>
      <th width="10%">科目編號</th>
      <th width="25%">科目名稱</th>
      <th width="8%">班級編號</th>
      <th width="7%">必修科</th>
    </tr>
    <% 
    if(studEnrolList != null) {
    	List enlList = (List)studEnrolList.get(0);
    	List stuList = (List)studEnrolList.get(1);
    	List cosList = (List)studEnrolList.get(2);
    	for(int i=0; i<enlList.size(); i++){
    		Studentenrol enl = (Studentenrol)enlList.get(i);
    		Student stu = (Student)stuList.get(i);
    		Course cos = (Course)cosList.get(i);
    		%>
    		<tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
      		 <td height="25">
      		   <a href="#">
      		     <span onClick="openDetail('StudentDetailServlet?psStudentNo=<%=enl.getStudentNO() %>','','width=700,height=400,toolbar=no,status=no,menubar=no,scrollbars=no,resizable=no')"><%=enl.getStudentNO() %></span>
      		   </a>
      		 </td>
      		 <td><%=ToolsOfString.NulltoEmpty(stu.getChineseName()) %></td>
      		 <td><%=ToolsOfString.NulltoEmpty(stu.getEnglishName()) %></td>
      		 <td><%=cos.getCourseCode() %></td>
      		 <td><%=ToolsOfString.NulltoEmpty(cos.getChineseName()) %></td>
      		 <td><%=enl.getClassCode() %></td>
      		 <td><%=enl.getCreditType() %></td>
    		</tr>
    		<%
    	}
    }
    %>
    <tr>
    <td align="center" colspan="7">
    <%
    List<PageBean> pagelist=(List)request.getAttribute("page");
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
    %>
      <label>
        &nbsp;&nbsp;
        <input type="text" name="instart" id="len" onChange="changeFocus();"/>
        &nbsp;
        <input type="button" name="jump" onClick="page('page')" value="跳轉" />
      </label>
    </td>
  </tr>
</table>
</form>
</body>
</html>
