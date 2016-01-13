<%@ page language="java" import="java.util.List,java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>學生訂書列表</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
function closeStudentBookList() {
	window.close();
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<%
List studentBookList = (List)request.getAttribute("studentBookList");
String studNo = (String)request.getAttribute("studNo");
%>
<body>
<h2>學生訂書列表</h2>
<p align="center" >學號：<%if(studNo!=null){out.print(studNo);}else{out.print("&nbsp;");} %></p>
<table width="720"  border="1" align="center" cellpadding="5" cellspacing="0">
  <tr>
    <td width="18%" align="center">ISBN</td>
    <td width="22%" align="center">教科書書名</td>
    <td width="20%" align="center">科目編號</td>
    <td width="5%" align="center">類別</td>
    <td width="5%" align="center">報讀狀況</td>
    <td width="5%" align="center">書本類型</td>
    <td width="7%" align="center">備注</td>
    <td width="8%" align="center">預估價<br/>(澳門幣)</td>
    <td width="8%" align="center">預估價<br/>(人民幣)</td>
  </tr>
  <%
  if(studentBookList!=null && !studentBookList.isEmpty()) {
	  for(int i=0; i<studentBookList.size(); i++){
		  StudentBookList book = (StudentBookList)studentBookList.get(i);
  %>
  <tr>
    <td align="center"><%=book.getIsbn()%></td>
    <td><%=book.getBookTitle()%></td>
    <td align="center"><%=book.getCourseCode()%><br/><%=book.getCrsChineseName()%></td>
    <td align="center"><%=book.getCourseType()%></td>
    <td align="center"><% if(book.getIsEnrolled()){out.print("Y");}else{out.print("N");}%></td>
    <td align="center"><%=book.getBookType()%></td>
    <td><%=book.getRemarks()%></td>
    <td align="center"><%=book.getMopPrice()%></td>
    <td align="center"><%=book.getNetMopPrice()%></td>
  </tr>
  <%
		}
	}else{
  %>
  <tr>
    <td colspan="9" align="center">沒有資料</td>
  </tr>
  <% 
  } 
  %>
</table>
<p align="center" style="margin:6px;">
  <input type="button" name="close" onClick="closeStudentBookList();" value=" 關 閉 ">
</p>
</body>
</html>