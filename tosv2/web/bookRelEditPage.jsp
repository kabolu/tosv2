<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import ="java.util.*,edu.must.tos.bean.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改圖書科目關係</title>
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
		document.form1.action = "UpdateBookServlet";
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
<h2>修改圖書科目關係</h2>
<%
List<Major> majorList = null;
if(session.getAttribute("majorList") != null){
	majorList = (List)session.getAttribute("majorList");
}
int m = 0;
try{
	String isbn = (String)session.getAttribute("isbn");
	List courseNameList = (List)session.getAttribute("courseNameList");
	List bookRelList = (List)session.getAttribute("bookRelList");
	List<BookRelInfo> relInfoList = (List)session.getAttribute("relInfoList");
%>
<form method="post" name="form1">
  <table align="center" width="780" cellpadding="0" cellspacing="0" border="1">
    <tr>
      <td height="25" colspan="6">&gt;&gt;&gt;&gt;&gt;&gt;圖書編號為<%=isbn%>，請編輯其科目關係！</td>
      <td align="center">
        <input type="button" name="addCourseCode" value="添加科目" onclick="window.open('bookRelEdit.jsp','','width=550,height=600,scrollbars=yes');"/>
      </td>
    </tr>
    <tr>
      <td colspan="7" height="20"></td>
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
    if(bookRelList == null && bookRelList.isEmpty() && courseNameList == null && courseNameList.isEmpty()){
    %>
    <tr>
      <td height="20" colspan ="4">你還沒有選擇關聯的科目，請點擊添加科目鏈接！</td>
    </tr>
	<%
	}else{
		m = bookRelList.size();
		for(int i=0; i<bookRelList.size(); i++){
			BookRel br = (BookRel)bookRelList.get(i);
			String courseName = (String)courseNameList.get(i);
			String grade = "", core = "", bookType = "";
			for(BookRelInfo info : relInfoList){
				if(info != null)	
					if(br.getIntake().equals(info.getIntake()) && br.getIsbn().equals(info.getIsbn()) 
							&& br.getCourseCode().equals(info.getCourseCode()) && br.getMajorCode().equals(info.getMajorCode())){
						grade = info.getGrade() == null ? "" : info.getGrade();
						core = info.getCe();
						bookType = info.getBookType();
					}
			}
			
	%>
	<tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#F5F2DA'" bgcolor="#F5F2DA">
	  <td height="20">
	    <input type="hidden" name="m" value="<%=m %>">
	    <%=br.getCourseCode() %>
	  </td>
	  <td><%=courseName %></td>
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
	    <input type="text" maxlength="1" name="grade" class="inp" size="4" value="<%=grade %>">
	  </td>
	  <td>
	    <select name="core">
	      <option value="E" <%if("E".equals(core)){out.print("selected");} %>>選修</option>
	      <option value="C" <%if("C".equals(core)){out.print("selected");} %>>必修</option>
	    </select>
	  </td>
	  <td>
	    <select name="bookType">
	      <option value="RB" <%if("RB".equals(bookType)){out.print("selected");} %>>RB</option>
	      <option value="CB" <%if("CB".equals(bookType)){out.print("selected");} %>>CB</option>
	      <option value="TB" <%if("TB".equals(bookType)){out.print("selected");} %>>TB</option>
	    </select>
	  </td>
	  <td align="center">
	    <input type="button" name="remove" value="X" onclick="window.location.href='BookRelServlet?type=removeBookRel&id=<%=i%>'"/>
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
	  <td colspan="7" align="center" height="30">
	    <input type="button" value=" 保存 " onClick="submitForm(<%=m %>);">
	    &nbsp;&nbsp;&nbsp;&nbsp;
	    <input type="button" value="上一步" onclick="window.location.href='EditBookBackServlet'">
	  </td>
	</tr>
  </table>
</form>
<%
String flag = (String)request.getAttribute("flag");
if("true".equals(flag)){
	%>
	<script language="javascript">
	alert("已有學生訂購了該科目的圖書，請返回！");
	history.back();
	</script>
	<%
}
%>
</body>
</html>
