<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>時段統計總表</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />		
<script language="javascript">
$(document).ready(function(){
	$('#display').show();
	$('#have').click(function(){
		$('#display').show();
	})
	$('#leave').click(function(){
		$('#display').hide();
	})
	$('.selectedSubmit').click(function(){
		var intake = $('#intakeList').val();
		var status = $('input[@name=status][@checked]').val();
		if(status == undefined){
			alert("請選擇報表類型！");
			return false;
		}
		var periodType = $('input[@name=periodType][@checked]').val();
		if(periodType==undefined){
			alert("請選擇時段類型！");
			return false;
		}
		
		var fromDate = $('#fromDate').val();
		if(fromDate==""){
			alert("請輸入時段從！");
			return false;
		}
		var toDate = $('#toDate').val();
		if(toDate==""){
			alert("請輸入時段至！");
			return false;
		}
		if(fromDate!="" && toDate!=""){
			e = fromDate+" 00:00:00";
			f = toDate+" 23:59:59";
			var d1 = new Date(e.replace(/\-/g, "\/"));
			var d2 = new Date(f.replace(/\-/g, "\/"));
			if(Date.parse(d1) - Date.parse(d2)>0){
				alert(e+"晚於"+f);
			    return false;
			}
		}
		var faculty = $('#faculty').val();
		if(status == "Y"){
			window.location.href='StatisticReceivePeriod?intake='+intake+'&faculty='+faculty+'&fromDate='+fromDate+'&toDate='+toDate+'&periodType='+periodType;
		}else{
			window.location.href='StatisticReceivePeriodLeavings?intake='+intake+'&fromDate='+fromDate+'&toDate='+toDate+'&periodType='+periodType;
		}
	})
})
</script>
<%
if (session.getAttribute("userId") == null) {
%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<%}%>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h2>時段統計總表</h2>
<%
List facultyList = null;
if(request.getAttribute("facultyList")!=null){
	facultyList = (List)request.getAttribute("facultyList");
}
String intake = null;
if(request.getAttribute("intake")!=null){
	intake = (String)request.getAttribute("intake");
}
List intakeList = null;
if(request.getAttribute("intakeList")!=null){
	intakeList = (List)request.getAttribute("intakeList");
}
SysConfig receiveSC = (SysConfig)request.getAttribute("sc");
String fromDate = "";
String toDate = "";
if(receiveSC!=null && receiveSC.getScValue1()!=null){
	fromDate = receiveSC.getScValue1().substring(0, 10);
}
if(receiveSC!=null && receiveSC.getScValue2()!=null){
	toDate = receiveSC.getScValue2().substring(0, 10);
}
%>
<form action="" name="searchForm" id="searchForm">
  <table align="center" border="0" cellpadding="0" cellspacing="0" width="720" id="selected">
    <tr>
      <td align="left" colspan="6" height="50">&nbsp;報表類型：
        <input type="radio" name="status" value="Y" id="have" style="background-color: #F5F2DA; border: 0;">選取人數&nbsp;&nbsp;
        <input type="radio" name="status" value="N" id="leave" style="background-color: #F5F2DA; border: 0;">人數餘額&nbsp;&nbsp;
      </td>
    </tr>
    <tr>
      <td align="left" colspan="6" height="50">&nbsp;學期：
        <select name="intakeList" id="intakeList">
          <%
          if (intakeList.isEmpty()) {
          %>
          <option value="">==請選擇==</option>
          <%
          } else {
        	  for (int i = 0; i < intakeList.size(); i++) {
        		  SysConfig sc = (SysConfig) intakeList.get(i);
          %>
          <option value="<%=sc.getScValue1()%>" <%if(sc.getScValue1().equals(intake)){out.print("selected");} %>><%=sc.getScValue1()%></option>
          <%
          	  }
          }
          %>
        </select>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;時段類型：
        <input type="radio" name="periodType" value="R" style="background-color: #F5F2DA; border: 0;">領書時段&nbsp;&nbsp;
        <input type="radio" name="periodType" value="P" style="background-color: #F5F2DA; border: 0;">付款時段&nbsp;&nbsp;
        <br>
        <br>時段從：
        <input name="fromDate" value="<%=fromDate %>" size="8" class="inp" type="text" id="fromDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly"/>
        &nbsp;&nbsp;時段至：
        <input name="toDate" value="<%=toDate %>" size="8" class="inp" type="text" id="toDate" onClick="calendar.show(this);" maxlength="10" readonly="readonly"/>
      </td>
    </tr>
    <tr>
      <td align="left" colspan="6" height="50">
        <table id="display">
          <tr>
            <td>學院名稱：
              <select name="faculty" id="faculty">
                <%
                if (facultyList.isEmpty()) {
                %>
                <option value="">===請選擇===	</option>
                <%
                } else {
                %>
                <option value="">===所有===</option>
                <%
                	for (int i = 0; i < facultyList.size(); i++) {
                		Faculty faculty = (Faculty) facultyList.get(i);
                %>
                <option value="<%=faculty.getFacultyCode()%>"><%=faculty.getChineseName()%></option>
                <%
                	}
                }
                %>
              </select>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td colspan="6" height="55" align="center">
        <input type="button" value="提 交" name="submit" class="selectedSubmit">
        &nbsp;&nbsp;
        <input type="reset" value="重 置" name="reset" class="reset">
        &nbsp;&nbsp;
        <input type="button" value="返 回" name="back">
      </td>
    </tr>
  </table>
</form>
</body>
</html>