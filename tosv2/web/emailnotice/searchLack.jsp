<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="edu.must.tos.bean.Student"%>
<%@page import="edu.must.tos.util.PageBean"%>
<%@page import="edu.must.tos.util.ToolsOfString"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>    
    <title>缺書名單搜索</title>
	<link href="css/style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
	<script type="text/javascript" src="js/calendar.js"></script>
  </head>
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
    //$("#showload").html("<div><img src='images/ajax-loading.gif'/>正在發送...</div>");
	document.location.href = "EmailNoticeServlet?studentNos="+studentNos+"&orderSeqNos="+orderSeqNos+"&isbn="+isbn+"";
}

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
function checkForm(){
    var a = document.getElementById("fromDate").value;
    var b = document.getElementById("toDate").value;
    if(a != "" && b != ""){
    	var d1 = new Date(a.replace(/\-/g, "\/"));
		var d2 = new Date(b.replace(/\-/g, "\/"));
    	if(Date.parse(d1) - Date.parse(d2)>0){
			alert(a + "晚於" + b);
			return false;
		}else{
			return true;
		}
    } else {
    	alert("请输入完整日期查询!");
    	return false;
    }
}
</script> 
<body>
  <h2>教材到貨通知</h2>
  	<form action="StudentLackBookServlet" method="post" name="searchForm" onsubmit="return checkForm();">
  	  <input type="hidden" name="type" value="SEARCHARRIVAL"/>
  	  <table border="0">
  	    <tr>
  	      <td>日期：</td>
  	      <td>
  	        <input name="fromDate" size="8" type="text" class="inp" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
  	        &nbsp;~&nbsp;
  	        <input name="toDate" size="8" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
  	      </td>
  	    </tr>
  	    <tr>
  	      <td>圖書ISBN：</td>
  	      <td>
  	        <input type="text" class="inp" name="isbn" />
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
List<Student> list = null;
if(request.getAttribute("studentList") != null){
	list = (List)request.getAttribute("studentList");
}
String isbn = (String)request.getAttribute("isbn");
%>
<form name="form1" action="" method="post">
  <input type="hidden" name="isbn" value="<%=isbn %>" />
  <%
  if(list != null && !list.isEmpty()){
  %>
  <table id="table1" width="99%" align="center" border="0" cellspacing="1" cellpadding="0">
    <tr bgcolor="#C6D6FD">
      <th width="5%" align="center">
      	<input type="checkbox" id="CheckAll" value="" class="checkbox"/>
      </th>
      <th width="17%" height="25">學號</th>
      <th width="8%" align="center">中文名</th>
      <th width="15%" align="center">英文名</th>
      <th width="7%" align="center">學院編號</th>
      <th width="8%" align="center">課程編號</th>
      <th width="15%" align="center">聯系電話</th>
      <th width="25%" align="center">電郵地址</th>
    </tr>
    <%
    for(Student stu : list) {
    	%>
    	<tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF" >
    	  <td align="center">
    	    <input type="checkbox" name="emailbox" value="<%=stu.getStudentNo() %>" class="checkbox"/>
    	    <input type="hidden" name="orderSeqNo" value="<%=stu.getOrder().getOrderSeqNo() %>" />
    	  </td>
    	  <td height="25">
    	    <a href="#"><span onclick="openDetail('StudentDetailServlet?psStudentNo=<%=stu.getStudentNo() %>','','width=700,height=400,toolbar=no,status=no,menubar=no,scrollbars=no,resizable=no')"><%=stu.getStudentNo() %></span></a>
    	  </td>
    	  <td><%=stu.getChineseName() %></td>
    	  <td><%=stu.getEnglishName() %></td>
    	  <td><%=stu.getFacultyCode() %></td>
    	  <td><%=stu.getProgramCode() %></td>
    	  <td><%if(!ToolsOfString.isNull(stu.getContactNo2())){out.print(stu.getContactNo2());}else{out.print(stu.getContactNo());} %></td>
    	  <td><%if(!ToolsOfString.isNull(stu.getEmail2())){out.print(stu.getEmail2());}else{out.print(stu.getEmail());} %></td>
    	</tr>
    	<%
    }
    %>
  </table>
</form>
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