<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.must.tos.bean.Course" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>圖書科目關係</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function checkBox(){
	var boxes = document.form1.box;
	var flag = false;
	for(var i=0; i<boxes.length; i++){
		if(boxes[i].checked ){
			flag = true;
			break;
		}
	}
	if(document.form1.box.checked) {
		flag = true;
	}
	if(document.form1.other.checked) {
		flag = true;
	}
	if(flag){
		document.form1.action = "BookRelServlet";
		document.form1.submit();
	}else{
		alert("至少選擇一項！");
		return;
	}
}
</script>
</head>
<body>
<form action="CourseSearchServlet" method="post" style="margin:0px;padding:0px;">
  <input type="hidden" name="oprType" value="add" >
  <table width="500" align="center" border="0">
    <tr>
      <td width="110" height="50">請輸入查詢條件：</td>
      <td width="240"><input type="text" name="cond" style="width:180px;height:18px;" maxlength="12"></td>
      <td width="150"><input type="submit" value="搜 索"></td>
    </tr>
  </table>
</form>
<hr>
<form method="post" name="form1" style="margin:0px;padding:0px;">
  <input type="hidden" name="type" value="saveSession">
  <table width="500" align="center" border="0" cellpadding="0" cellspacing="0">
  <%
  try{
	  String isbn = (String)session.getAttribute("isbn");
	  List courseList = (List)request.getAttribute("courselist");
	  if(courseList == null || courseList.isEmpty()){
  %>
    <tr>
      <td colspan="3">沒有相關資訊顯示，請輸入條件進行搜索！</td>
    </tr>
    <%
      }else{
    %>
    <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#F5F2DA'" bgcolor="#F5F2DA">
      <td>OTHER</td>
      <td>OTHER</td>
      <td>
        <input type="checkbox" class="checkbox" name="other" value="OTHER"/>
      </td>
    </tr>
    <%
          session.setAttribute("courselist", courseList);
    	  for(int i=0; i<courseList.size(); i++){
    		  Course course = (Course)courseList.get(i);
    %>
    <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#F5F2DA'" bgcolor="#F5F2DA">
      <td height="25" width="100">
        <input type="hidden" name="isbn" value="<%=isbn%>"><%=course.getCourseCode() %>
      </td>
      <td><%=course.getChineseName() %></td>
      <td><input type="checkbox" class="checkbox" name="box" value="<%=i%>"></td>
    </tr>
    <%
    	  }
	%>
	<tr>
	  <td colspan="3" align="center"><input type="button" onclick="checkBox();" value="添 加" ></td>
	</tr>
	<%
	  }
  }catch(Exception e){
	  e.printStackTrace();
  }
  %>
  </table>
</form>
</body>
</html>
