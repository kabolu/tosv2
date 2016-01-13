<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<%if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時,請重新登陸!');
	window.parent.location.href = 'login.jsp';
</script>
<% } %> 
<script type="text/javascript">
$(document).ready(function(){
	$('#regionInfo').hide();
	$('#yearInfo').hide();
	
	$('input[@name=studType]').click(function(){
		var type = $(this).val();
		if(type == "new"){
			$('#regionInfo').show();
			$('#yearInfo').hide();
			$('#year')[0].selectedIndex = 0;
			$('input[@name=paidstatus]').attr("checked", false);
		}else if(type == "old"){
			$('#yearInfo').show();
			$('#regionInfo').hide();
			$('#region')[0].selectedIndex = 0;
			$('input[@name=paidstatus]').attr("checked", false);
			$('input[@name=paidstatus]').get(1).checked = true;
			$('input[@name=isDiffer]').attr("checked", false);
		}
	})
}) 

function checkData() {
	var intake = document.form1.intake.value;
	if(intake.length != 4 || isNaN(intake) || parseInt(intake.substring(2)) > 12) {
		alert("學期格式有誤，請重新輸入!");
		return false;
	}
	var studType = $('input[@name=studType][@checked]').val();
	if(studType == undefined){
	  	studType = "";
	}
	var fromdate = document.form1.fromDate.value;
	var todate = document.form1.toDate.value;
	if(fromdate != "" && todate != ""){
		var date1 = new Date(fromdate.replace(/\-/g, "\/"));
	  	var date2 = new Date(todate.replace(/\-/g, "\/"));
	  	if(Date.parse(date1) - Date.parse(date2) > 0){	  		
	  		alert(fromdate+"晚於"+todate);
	  		return false;
   		}
	}else if(fromdate == "" && todate == ""){
		return true;
	}else{
	  	alert("請輸入完整的開始日期和結束日期！");
	  	return false;
	}
}

function programList() {
	var facultyCode = document.form1.facultyCode.value;
	document.form1.action = "ProgramListServlet?prosearchType=totalReport";
	document.form1.target = "_self";
	document.form1.submit();
}

function resetFaculty() {
	document.form1.action = "reset.jsp?searchType=totalReport"
    document.form1.target = "_self";
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
String curIntake = (String)session.getAttribute("curIntake");
List<Faculty> facultyList = (List)session.getAttribute("facultyList");
List<Program> programList = (List)request.getAttribute("programList");
Program p1  = null;
if(programList != null && !programList.isEmpty()) {
	p1 = programList.get(0);
}
String program = null;
if(request.getAttribute("program") != null){
	Program p = (Program)request.getAttribute("program");
	if(p != null){
		program = p.getFacultyCode();
	}
}
List<String> regionList = null;
if(request.getAttribute("regionList") != null){
	regionList = (List)request.getAttribute("regionList");
}
%>
<body>
<h2>綜合訂書報表</h2>
<form name="form1" onSubmit="return checkData();" target="_blank" method="post" action="TotalOrdBooksReportServlet">
  <table width="95%" border="0" cellpadding="0" cellspacing="0" align="center">
    <tr>
      <td height="30" width="15%">學期：</td>
      <td width="18%">
        <input name="intake" type="text" id="intake" value="<%=curIntake %>" size="3" maxlength="4" class="inp">
      </td>
      <td align="left" width="10%">學院：</td>
      <td width="">
        <select name="facultyCode" id="facultyCode" onChange="programList();">
          <option value="" >請選擇學院</option>
          <%
          if(facultyList != null) {
        	  for(Faculty fa : facultyList) {
        		  if(program == null){
        			  %>
        			  <option value="<%=fa.getFacultyCode() %>" ><%=fa.getChineseName() %></option>
        			  <%
        		  }else {
        			  if(program != null && fa.getFacultyCode().equals(program)){
        				  %>
        				  <option value="<%=fa.getFacultyCode() %>" selected="selected"><%=fa.getChineseName() %></option>
        				  <%
        			  }else{
        				  %>
        				  <option value="<%=fa.getFacultyCode() %>" ><%=fa.getChineseName() %></option>
        				  <%
        			  }
        		  }
        		  
        	  }
          } 
          %>
        </select>
      </td>
      <td width="10%" align="right">課程：</td>
      <td width="">
        <select name="programCode" id="programCode">
          <option value="" >請選擇課程</option>
          <%
          if(programList != null) {
        	  for(Program pro : programList) {
        		  %>
        		  <option value="<%=pro.getProgramCode() %>" ><%=pro.getChineseName() %></option>
        		  <%
        	  }
          } 
          %>
        </select>
      </td>
    </tr>
    <tr>
      <td height="30">訂購日期：</td>
      <td colspan="5">
        <input name="fromDate" type="text" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" size="9" maxlength="10" class="inp"/>
        至
        <input name="toDate" type="text" id="toDate" onClick="new Calendar(null, null, 3).show(this);" size="9" maxlength="10" class="inp"/>
      </td>
    </tr>
    <tr>
      <td colspan="6">
        <input type="checkbox" name="printParam" value="Y" style="border:0px;background-color:#F5F2DA;">按學員訂單序號列印
      </td>
    </tr>
    <tr>
      <td>學員類別：</td>
      <td colspan="5">新生
        <input type="radio" name="studType" value="new"	style="background-color: #F5F2DA; border: 0;">舊生
        <input type="radio" name="studType" value="old"	style="background-color: #F5F2DA; border: 0;">
      </td>
    </tr>
    <tr>
      <td colspan="6" height="30">
        <div id="yearInfo">
          所有：<input type="radio" name="paidstatus" value="A"	style="background-color: #F5F2DA; border: 0;">
          已付款：<input type="radio" name="paidstatus" value="N" checked="checked" style="background-color: #F5F2DA; border: 0;">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          年級：&nbsp;&nbsp;&nbsp;&nbsp;
          <select name="year" id="year">
            <option value="">請選擇年級</option>
            <option value="1">一年級</option>
            <option value="2">二年級</option>
            <option value="3">三年級</option>
            <option value="4">四年級</option>
            <option value="5">五年級</option>
          </select>
        </div>
        <div id="regionInfo">
        地區：&nbsp;&nbsp;&nbsp;&nbsp;
          <select name="region" id="region">
            <option value="all">ALL</option>
            <%
            if(regionList != null){
            	for(String str : regionList){
            		%>
            		<option value="<%=str %>"><%=str %></option>
            		<%
                }
            }
            %>
          </select>
	        
	       <!-- 
	        &nbsp;&nbsp;
	        付款狀態：&nbsp;&nbsp;
	        <input type="radio" name="paidstatus" value="Y">已付
	        &nbsp;&nbsp;
	        <input type="radio" name="paidstatus" value="N">未付
	        &nbsp;&nbsp;&nbsp;&nbsp;
	        補差價：&nbsp;&nbsp;
	        <input type="radio" name="isDiffer" value="N">是
	        &nbsp;&nbsp;
	        <input type="radio" name="isDiffer" value="Y">否
	        -->
        </div>
      </td>
    </tr>
    <tr>
      <td colspan="6" align="center">
        <p>&nbsp;</p>
        <p>
          <input type="submit" name="Submit" value="列印報表">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input type="button" name="Submit2" value="重 置" onclick = "resetFaculty();">
        </p>
      </td>
    </tr>
  </table>
</form>
</body>
</html>