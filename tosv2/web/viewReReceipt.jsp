<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*,java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重印收據</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.table td {
	border:1px solid #F5DEB3;
}
.table th {
	border:1px solid #F5DEB3;
}
-->
</style>
<script language="javascript">
$(document).ready(function(){
	$('input[@name=payAmerce]').click(function(){
		var len = $('input[@name=box][@checked]').length;
		var feeValue = $('input[@name=feeValue]').val();
		var feeCurrency = $('input[@name=feeCurrency]').val();
		var studNo = $('input[@name=studNo]').val();
		if(len == 0){
			alert("您還未選擇重印收據項，請先選擇後再操作！");
			return false;
		}else{
			if(confirm("是否確定收取重印收據罰款？")){
				var value = "";
				$('input[@name=box][@checked]').each(function(){
					value += $(this).val()+",";
				})
				$.post(
					"ViewReceiptServlet",
					{
						type:"reReceiptFee",
						orderSeqNoParam:value,
						feeValue:feeValue,
						feeCurrency:feeCurrency,
						studNo:studNo
					},
					function(result){
						if(result==1){
							alert("重印收據罰款收取成功！");
							$('input[@name=box]').each(function(){
								$(this).attr("checked", false);
							})
							$('input[@name=fee]').attr("value", 0);
						}else{
							alert("重印收據罰款收取失敗！");
							$('input[@name=box]').each(function(){
								$(this).attr("checked", false);
							})
							$('input[@name=fee]').attr("value", 0);
						}
					}
				)
			}
		}
	})
	
	$('input[@name=box]').click(function(){
		var orderseqno = $(this).val();
		var feeValue = $('input[@name=feeValue]').val();
		var flag = "";
		var total = 0;
		if($(this).attr('checked') == true){
			flag = "Y";
			total = (parseFloat($('input[@name=fee]').val()) + parseFloat(feeValue));
		}else{
			total = (parseFloat($('input[@name=fee]').val()) - parseFloat(feeValue));
		}
		$('input[@name=fee]').attr("value", total);
	})
	
	$('input[@name=reset]').click(function(){
		$('input[@name=box]').each(function(){
			$(this).attr("checked", false);
		})
		$('input[@name=fee]').attr("value", 0);
	})
})
function rePrintReceipt(orderSeqNo, studentNo, appNo){
	window.open('ViewReceiptServlet?orderSeqNo='+orderSeqNo+'&rePrint=Y&studentNo='+studentNo+'&applicantNo='+appNo,'','width=780,height=600,scrollbars=yes');
}
function back(){
	document.backForm.submit();
}
</script>
<%if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href = 'login.jsp';
</script>
<%}%>
</head>
<body>
<%
SysConfig sc = null;
double fee = 0;
String currency = "";
if(request.getAttribute("sysconfig") != null){
	sc = (SysConfig)request.getAttribute("sysconfig");
	fee = Double.parseDouble(sc.getScValue1());
	currency = sc.getScValue2();
}
List<Order> rePrintList = null;
if(request.getAttribute("rePrintList") != null){
	rePrintList = (List)request.getAttribute("rePrintList");
}
List stuDetList = null;
if(request.getAttribute("stuDetList") != null){
	stuDetList = (List)request.getAttribute("stuDetList");
}
String studentName = "";
String programName = "";
String studNo = "";
String studentNo = null;
String appNo = null;
if(stuDetList != null) {
	Student stu = (Student)stuDetList.get(0);
	if(stu != null){
		studentName = stu.getChineseName();
		if(stu.getStudentNo() != null){
	    	studNo = stu.getStudentNo();
	    	studentNo = stu.getStudentNo();
	    }else{
	    	studNo = stu.getApplicantNo();
	    	appNo = stu.getApplicantNo();
	    }
	}
	Program pro = (Program)stuDetList.get(2);
	if(pro != null){
		programName = pro.getChineseName();
	}
}
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<table align="center" cellpadding="0" cellspacing="0" border="0" width="90%">
  <tr bgcolor="#FFE4B5">
    <th height="30" width="30%">學員編號</th>
    <th width="35%">學員名稱</th>
    <th width="35%">課程名稱</th>
  </tr>
  <tr>
    <td height="30" align="center"><%=studNo %></td>
    <td align="center"><%=studentName %></td>
    <td align="center"><%=programName %></td>
  </tr>
</table>
<input type="hidden" name="studNo" id="studNo" value="<%=studNo %>">
<table align="center" cellpadding="0" cellspacing="0" border="0" width="90%" class="table">
  <tr>
    <td colspan="4" height="30" style="border:1px solid #F5DEB3;">===<b>訂單資料</b>====</td>
  </tr>
  <tr>
    <th height="30">訂單號</th>
    <th>學期</th>
    <th>訂購日期</th>
    <th>操作</th>
  </tr>
  <%
  if(!rePrintList.isEmpty()){
	  for(Order o : rePrintList){
  %>
  <tr>
    <td align="center" height="22">
      <a target="_blank" style="cursor:hand;" onclick="rePrintReceipt('<%=o.getOrderSeqNo() %>','<%=studentNo %>','<%=appNo %>');">
      <%=o.getOrderSeqNo() %>
      </a>
    </td>
    <td align="center"><%=o.getOrderIntake() %></td>
    <td align="center"><%=df.format(o.getCreDate()) %></td>
    <td align="center">
      <input type="checkbox" name="box" value="<%=o.getOrderSeqNo() %>">
    </td>
  </tr>
  <%
	 }
  }
  %>
  <tr>
    <td colspan="4" align="center" height="30"></td>
  </tr>
</table>
<table align="center" cellpadding="0" cellspacing="0" border="0" width="90%" class="table">
  <tr>
    <td colspan="3">
      <input type="hidden" name="feeValue" value="<%=fee %>">重印收據每項罰款金額為<%=fee %>，幣種為<%=currency %>；
    </td>
  </tr>
  <tr>
    <td width="18%">罰款金額：</td>
    <td width="32%">
      <input type="text" name="fee" value="0" style="height:18px;width:50px;" readonly="readonly">
      <input type="text" name="feeCurrency" value="<%=currency %>" style="height:18px;width:40px;" readonly="readonly">
    </td>
    <td width="50%">
      <input type="button" name="payAmerce" value=" 記錄重印罰款 ">
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" name="reset" value="重 置">
      &nbsp;&nbsp;&nbsp;&nbsp;
      <!-- <input type="button" name="back" value="返 回" onclick="window.location.href='orderbookindex.jsp?oprType=received'"> -->
      <input type="button" name="back" value="返 回" onclick="back();">
    </td>
  </tr>
</table>
<br/>
<br/>
<div id="remarks">
  <p>
  備注：
  <br>
  1、記錄重印罰款操作後，請到補價差功能上收取該項費用；
  <br>
  </p>
</div>
<form action="StudentListServlet" method="post" name="backForm">
<%
String oprType = (String)session.getAttribute("oprType");
Student student = (Student)session.getAttribute("student");
double totalPages = Double.parseDouble(session.getAttribute("totalPages").toString());
String start = (String)session.getAttribute("start");
%>
<%
if(student.getStudentNo() != null) {
%>
<input type="hidden" name="studentNo" value="<%=student.getStudentNo() %>" />
<%
}
if(student.getChineseName() != null){
%>
<input type="hidden" name="chineseName" value="<%=student.getChineseName() %>" />
<%
}
if(student.getEnglishName() != null){
%>
<input type="hidden" name="englishName" value="<%=student.getEnglishName() %>" />
<%	
}
if(student.getFacultyCode() != null) {
%>
<input type="hidden" name="facultyCode" value="<%=student.getFacultyCode() %>" />
<%
} 
if(student.getIdNo() != null) {
%>
<input type="hidden" name="idNo" value="<%=student.getIdNo() %>" />
<%
} 
if(student.getProgramCode() != null) {
%>
<input type="hidden" name="programCode" value="<%=student.getProgramCode() %>" />
<%
}
%>
<input type="hidden" name="back" value="back" />
<input type="hidden" name="searchType" value="orderStudent" />
<input type="hidden" name="oprType" value="<%=oprType %>" />
<input type="hidden" name="start" value="<%=start %>" />
<input type="hidden" name="totalPages" value="<%=totalPages %>" />
</form>
</body>
</html>
