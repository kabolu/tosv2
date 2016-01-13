<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="edu.must.tos.bean.OrDetail"%>
<%@page import="edu.must.tos.bean.Student"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>教材退款通知</title>
</head>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script language="javascript">
$(document).ready(function() {
    $('input[id="CheckAll"]').click(function() {
        var isChecked = $(this).attr('checked'); //先抓取CheckAll checked屬性的值
        var tbl = $('table[id ="table1"]');
        tbl.each(function() {
            $(this).find('tr').each(function() {
                $(this).find('td').each(function() {
                    //CheckAll的屬性值，其他checkbox的屬性值跟著CheckAll跑
                    $(this).find('input[type="checkbox"]').attr('checked', isChecked);                 
                });
            });
        });
     });
})

function sendEmail(){
	var studentNos = new Array();
	var orderSeqNos = new Array();
	var i = 0;
	var j = 0;
	var isbn = $("#bookisbn").val();
	var tbl = $('table[id ="table1"]');
    tbl.each(function() {
        $(this).find('tr').each(function() {
            $(this).find('td').each(function() {
                var checkput = $(this).find('input[type="checkbox"]');
                var orderSeqNo = $(this).find('input[name="orderSeqNo"]');
                if(checkput.attr('checked')){
                    studentNos[i++] = checkput.val();
                    orderSeqNos[j++] = orderSeqNo.val();
                }          
            })
        })
    })
    if(i<1){
		alert("請選擇要通知的學生！");
		return false;
    }
    $('input[id="send"]').attr('disabled','disabled');
	document.location.href = "EmailNoticeServlet?type=DRAWBACK&studentNos="+studentNos+"&orderSeqNos="+orderSeqNos+"&isbn="+isbn+"";
}

//查看學員詳細信息
function openDetail(theURL, winName, features) {
  window.open(theURL, winName, features);
}
function checkForm(){
    var isbn = document.getElementById("isbn").value;
    if(isbn == "") {
    	alert("请圖書ISBN查詢!");
    	return false;
    }
}
</script>
<body>
<h2>教材退款通知</h2>
<form action="StudentLackBookServlet" method="post" name="searchForm" onsubmit="return checkForm();">
<input type="hidden" name="type" value="SEARCHDRAWBACK"/>
<table border="0">
  <tr>
    <td>圖書ISBN：</td>
    <td>
      <input type="text" class="inp" name="isbn" id="isbn"/>
    </td>
  </tr>
  <tr>
    <td colspan="2" align="right">
      <input type="submit" name="submit" value=" 查找 "/>
    </td>
  </tr>
</table>
</form>
<%
List detailList = null;
if(request.getAttribute("detailList") != null){
	detailList = (List)request.getAttribute("detailList");
}
List studList = null;
if(request.getAttribute("studList") != null){
	studList = (List)request.getAttribute("studList");
}
String isbn = null;
if(request.getAttribute("isbn") != null){
	isbn = (String)request.getAttribute("isbn");
}
%>
<%
if(detailList != null && !detailList.isEmpty()){
	%>
	<table id="table1" width="99%" align="center" border="0" cellspacing="1" cellpadding="0">
    <tr bgcolor="#C6D6FD">
      <th width="5%" align="center">
      	<input type="checkbox" id="CheckAll" value="" class="checkbox"/>
      </th>
      <th width="20%" height="25">學號</th>
      <th width="10%" align="center">中文名</th>
      <th width="20%" align="center">英文名</th>
      <th width="7%" align="center">學院編號</th>
      <th width="8%" align="center">課程編號</th>
      <th width="10%" align="center">訂單編號</th>
      <th width="20%" align="center">圖書ISBN</th>
    </tr>
	<%
	int size = detailList.size();
	for(int i=0; i<size; i++){
		OrDetail od = (OrDetail)detailList.get(i);
		Student stud = (Student)studList.get(i);
		%>
		<tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF" >
    	  <td align="center">
    	    <input type="checkbox" name="emailbox" value="<%=stud.getStudentNo() %>" class="checkbox"/>
    	    <input type="hidden" name="orderSeqNo" value="<%=od.getOrderSeqNo() %>" />
    	  </td>
    	  <td height="25">
    	    <a href="#"><span onclick="openDetail('StudentDetailServlet?psStudentNo=<%=stud.getStudentNo() %>','','width=700,height=400,toolbar=no,status=no,menubar=no,scrollbars=no,resizable=no')">
    	    <%=stud.getStudentNo() %>
    	    </span></a>
    	  </td>
    	  <td><%=stud.getChineseName() %></td>
    	  <td><%=stud.getEnglishName() %></td>
    	  <td><%=stud.getFacultyCode() %></td>
    	  <td><%=stud.getProgramCode() %></td>
    	  <td><%=od.getOrderSeqNo() %></td>
    	  <td><%=od.getIsbn() %></td>
    	</tr>
		<%
	}
	%>
	</table>
	<table align="center">
	  <tr>
	    <td>
	      <input type="hidden" id="bookisbn" value="<%=isbn %>"/>
	      <input type="button" id="send" onclick="sendEmail();" value=" 發送 "/>
	    </td>
	  </tr>
	</table>
	<%
}
%>
</body>
</html>
