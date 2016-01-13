<%@ page language="java" import="java.util.*,edu.must.tos.bean.*,edu.must.tos.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>課程信息列表</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function page(start){
	document.form1.start.value = start;
	var totalPages = parseInt(document.form1.totalPages.value);
	
	if(start == "page") {
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
	document.form1.action.value = "ProgramListServlet";
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
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<%
List<Program> list = (List)request.getAttribute("programList");
Program pro = (Program)request.getAttribute("program");
double totalPages = Double.parseDouble(request.getAttribute("totalPages").toString());
%>
<body onload="instartFocus();">

<form action="" name="form1" method="post">
<input type="hidden" value="<%=pro.getFacultyCode() %>" name="facultyCode" />
<input type="hidden" value="<%=pro.getChineseName() %>" name="chineseName" />
<input type="hidden" value="<%=pro.getEnglishName() %>" name="englishName" />
<input type="hidden" value="<%=pro.getProgramCode() %>" name="programCode" />
<input type="hidden" value="" name="start" />
<input type="hidden" value="<%=totalPages %>" name="totalPages" />

<table width="100%"  align="center" border="0" cellspacing="1" cellpadding="0">
  <tr bgcolor="#C6D6FD">
    <th width="15%">課程編號</th>
    <th width="70%">中文名(英文名)</th>
    <th width="15%">學院編號</th>
  </tr>
  <%for(Program program:list) { %>
  <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td><%=program.getProgramCode() %></td>
    <td><%=program.getChineseName() %><br/><%=program.getEnglishName() %></td>
    <td><%=program.getFacultyCode() %></td>
  </tr>
  <%} %>
  <tr>
    <td align="center" colspan="8">
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