<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>搜索</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#textlen {
	height: 18px;
	width: 150px;
}
#textchinese {
	height: 18px;
	width: 120px;
}
#textenglish {
	height: 18px;
	width: 150px;
}
</style>
<script language="javascript">
function programList() {
	if(document.form1.facultyCode.value == "") {
		document.form1.action = "reset.jsp";
		document.form1.target = "top";
		document.form1.submit();
	} else {
		document.form1.action = "ProgramListServlet";
		document.form1.target = "top";
		document.form1.submit();
	}
}

function resetFaculty(){
	var searchType = document.form1.searchType.value;
    document.form1.action = "reset.jsp?searchType="+searchType+"";
    document.form1.target = "top";
    document.form1.submit();
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<%
 List<Faculty> facultyList = (List)session.getAttribute("facultyList");
 List<Program> programList = (List)request.getAttribute("programList");
 Program p1 = null;
 if(programList != null && !programList.isEmpty()) {
	 p1 = (Program)programList.get(0);
 }	
 String searchType = null;
 searchType = (String)request.getAttribute("searchType");
 if(searchType == null) {
	 searchType = request.getParameter("searchType");
 }
 String oprType = null;
 oprType = (String)session.getAttribute("oprType");
 if(oprType == null){
	 oprType = request.getParameter("oprType");
 }
%>
<body>
<form id="form1" name="form1" method="post" target="bottom" action="StudentListServlet">
  <input type="hidden" name="prosearchType" value="programList" />
  <%
  if (searchType != null && searchType.equals("orderStudent") && oprType != null && oprType.equals("orderBooks")) {
  %>
  <input type="hidden" name="searchType" value="orderStudent" />
  <input type="hidden" name="oprType" value="orderBooks" />
  <%
  } else if(searchType != null && searchType.equals("orderStudent") && oprType != null && oprType.equals("received")){
  %>
  <input type="hidden" name="remove" value="remove" />
  <input type="hidden" name="searchType" value="orderStudent" />
  <input type="hidden" name="oprType" value="received" />
  <%
  } else if(searchType != null && searchType.equals("orderStudent") && oprType != null && oprType.equals("search")){
  %>
  <input type="hidden" name="searchType" value="orderStudent" />
  <input type="hidden" name="oprType" value="search" />
  <%
  } else {
  %>
  <input type="hidden" name="searchType" value="" />
  <%
  }
  %>
  <table width="730" border="0" align="center" cellpadding="0" cellspacing="0" style="margin:10px 0px 0px 0px;">
    <tr>
      <td height="30" width="100" align="right">學&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;院：</td>
      <td width="120">
      <select name="facultyCode" onchange="programList();">
        <option value="">請選擇學院</option>
        <%
        for(Faculty fa : facultyList) {
        	if(p1 != null && p1.getFacultyCode().equalsIgnoreCase(fa.getFacultyCode())) {
        		%>
        		<option value="<%=fa.getFacultyCode() %>" selected="selected" ><%=fa.getChineseName() %></option>
        		<%
        	} else {
        		if(p1 == null && fa.getFacultyCode().equals("GS") && programList != null ) {
        			%>
        			<option value="<%=fa.getFacultyCode() %>" selected="selected" ><%=fa.getChineseName() %></option>
        			<%
        		} else {
        			%>
        			<option value="<%=fa.getFacultyCode() %>" ><%=fa.getChineseName() %></option>
        			<%
        		}
        	}
        }
        %>    
      </select>
      </td>
      <td width="100" align="right">課程編號：</td>
      <td width="200">
      <select name="programCode" id="programCode">
        <option value="">請選擇課程</option>
        <%
        if(programList != null) {
        	for(Program pro : programList) {
        		%>
        		<option value="<%=pro.getProgramCode() %>"><%=pro.getChineseName() %></option>
        		<%
        	}
        }
        %>
      </select>
      </td>
      <td width="90" align="right">學&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;號：</td>
      <td width="120"><input type="text" name="studentNo" id="textlen" onchange="this.value=this.value.toUpperCase()" /></td>
    </tr>
    <tr>
      <td height="28" align="right">中&nbsp;文&nbsp;名：</td>
      <td><input type="text" name="chineseName" id="textchinese" /></td>
      <td align="right">英&nbsp;文&nbsp;&nbsp;名：</td>
      <td><input type="text" name="englishName" id="textenglish" /></td>
      <td align="right">身份證號：</td>
      <td><input type="text" name="idNo" id="textlen"/></td>
    </tr>
    <tr>
      <td colspan="6" align="center" height="28">
      <input type="submit" name="Submit" value="查 找"/>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="reset" name="Submit2" value="重 置" onclick="resetFaculty();"/>
      </td>
    </tr>
  </table>
</form>
</body>
</html>

