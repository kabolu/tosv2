<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.must.tos.bean.BookRel" %>
<%@page import="edu.must.tos.bean.Major"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加圖書與科目關系版面</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script language="javascript">
function submitForm(m){
	if(m == 0){
		alert("你還未選擇科目，請添加科目！");
		return;
	}
	var flag = false;
	$('.majorCode').each(function(){
		var majorCode = $(this).val();
		if(majorCode == ""){
			alert("請選擇對應的專業編號，後保存記錄！");
			flag = false;
			return false;
		} else {
			flag = true;
		}
	})
	if(flag){
		document.form1.action = "AddBookServlet";
		document.form1.submit();
	}
}
function saveSession(id, val){
	$.post(
		"BookRelServlet",
		{
			type: "saveSession4Maj",
			id: id,
			majorCode: val
		},
		function(){}
	)
}
</script>
</head>
<body>
<%
List<Major> majorList = null;
if(request.getSession().getAttribute("majorList") != null){
	majorList = (List)request.getSession().getAttribute("majorList");
}
%>
<h2>添加圖書與科目關系</h2>
<form method="post" name="form1">
  <table align="center" width="780" cellpadding="0" cellspacing="0" border="1">
  <%
  int m = 0;
  try{
	  String isbn = (String)session.getAttribute("isbn");
  %>
    <tr>
      <td colspan="6" height="25">&gt;&gt;&gt;&gt;&gt;&gt;<b>圖書編號為<%=isbn %>，請添加其科目關係！</b></td>
      <td align="center">
        <input type="button" name="addCourseCode" value="添加科目" onclick="window.open('bookRelAdd.jsp','','width=550,height=600,scrollbars=yes');"/>
      </td>
	</tr>
	<tr>
	  <td colspan="7" height="20">&nbsp;</td>
	</tr>
	<tr>
	  <th width="13%">科目編號</th>
	  <th width="25%">科目中文名稱</th>
	  <th width="25%">專業編號</th>
	  <th width="10%">年級</th>
	  <th width="14%">選修/必修</th>
	  <th width="10%">類型</th>
	  <th width="3%" align="center">撤銷</th>
 	</tr>
 	<%
 	  String other = null;
 	  if(session.getAttribute("other") != null){
 		  other = (String)session.getAttribute("other");
 	  }
 	  List bookrelList = (List)session.getAttribute("bookrellist");
 	  List courseNameList = (List)session.getAttribute("courseNameList");
 	  if(bookrelList == null && courseNameList == null && other == null){
 	%>
 	<tr>
 	  <td height="20" colspan="7">你還沒有選擇關聯的科目，請點擊添加科目鏈接！</td>
 	</tr>
 	<%
 	  }else{
 		  if(other != null){
 	%>
 	<tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#F5F2DA'" bgcolor="#F5F2DA">
 	  <td height="20"><%=other %></td>
 	  <td><%=other %></td>
 	  <td>
 	    <select name="majorCode" class="majorCode" onchange="saveSession('OTHER', this.value);">
 	      <option value="">---</option>
 	      <option value="ALL" <%if("ALL".equals(other)){out.print("selected");} %>>ALL （用於通識教育部的科目）</option>
 	      <%
 	      if(majorList != null && !majorList.isEmpty()){
 	    	  for(Major maj : majorList){
 	      %>
 	      <option value="<%=maj.getMajorCode() %>" <%if(other.equals(maj.getMajorCode())){out.print("selected");} %>>
 	      <%=maj.getMajorCode() + " " + maj.getChineseName() %>
 	      </option>
 	      <%
 	      	  }
 	      }
 	      %>
 	    </select>
 	  </td>
 	  <td>
	    <input type="text" maxlength="1" name="grade" class="inp" size="4" value="">
	  </td>
	  <td>
	    <select name="core">
	      <option value="E">選修</option>
	      <option value="C">必修</option>
	    </select>
	  </td>
	  <td>
	    <select name="bookType">
	      <option value="RB" >RB</option>
	      <option value="CB" >CB</option>
	      <option value="TB" >TB</option>
	    </select>
	  </td>
 	  <td align="center">
 	    <input type="button" name="remove" value="X" onclick="window.location.href='BookRelServlet?type=removeSession&other=<%=other %>'"/>
 	  </td>
 	</tr>
 	<%
 	      }
 		  if(bookrelList != null){
 			  m = bookrelList.size();
 		  }
 		  if(m == 0){
 			  if(other != null){
 				  m = 1;
 			  }
 		  }
 		  for(int i=0; i<bookrelList.size(); i++){
 			  BookRel br = (BookRel)bookrelList.get(i);
 	%>
 	<tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#F5F2DA'" bgcolor="#F5F2DA">
 	  <td height="20"><%=br.getCourseCode() %></td>
 	  <td><%=courseNameList.get(i) %></td>
 	  <td>
 	    <select name="majorCode" class="majorCode" onchange="saveSession('<%=i %>', this.value);">
 	      <option value="">---</option>
 	      <option value="ALL" <%if("ALL".equals(br.getMajorCode())){out.print("selected");} %>>ALL （用於通識教育部的科目）</option>
 	      <%
 	      if(majorList != null && !majorList.isEmpty()){
 	    	  for(Major maj : majorList){
 	      %>
 	      <option value="<%=maj.getMajorCode() %>" <%if(maj.getMajorCode().equals(br.getMajorCode())){out.print("selected");} %>>
 	      <%=maj.getMajorCode() + " " + maj.getChineseName() %>
 	      </option>
 	      <%
 	      	  }
 	      }
 	      %>
 	    </select>
 	  </td>
 	  <td>
	    <input type="text" maxlength="1" name="grade" class="inp" size="4" value="">
	  </td>
	  <td>
	    <select name="core">
	      <option value="E">選修</option>
	      <option value="C">必修</option>
	    </select>
	  </td>
	  <td>
	    <select name="bookType">
	      <option value="RB" >RB</option>
	      <option value="CB" >CB</option>
	      <option value="TB" >TB</option>
	    </select>
	  </td>
 	  <td align="center">
 	    <input type="button" name="remove" value="X" onclick="window.location.href='BookRelServlet?type=removeSession&id=<%=i%>'"/>
 	  </td>
 	</tr>
 	<%
 	      }
 	  }
  }catch(Exception e){
	  e.printStackTrace();
  }
%>
  <tr>
    <td height="50" colspan="7" align="center">
      <input type="button" onClick="submitForm(<%=m %>)" value="保 存">
    </td>
  </tr>
</table>
</form>
</body>
</html>
