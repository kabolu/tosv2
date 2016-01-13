<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.text.*,edu.must.tos.bean.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>時間設置</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
$(document).ready(function(){
	$('.search').click(function(){
		var searchIntake = $('#searchIntake').val();
		
		if(searchIntake != ""){
			if(isNaN(searchIntake)){
				alert("學期：只可輸入數字來設定，請重新輸入！");
				$('#searchIntake').attr("value", '');
				$('#searchIntake').focus();
				return false;
			}else{
				if(searchIntake.length != 4){
					alert("學期要為四位數字，請檢查！");
					$('#searchIntake').focus();
					return false;
				}
			}
		}		
	})	
})
</script>
<script language="javascript">
function Del(value){
	$.post(
		"ReceiptBookTimeServlet",
		{
			type: "checkDel",
			periodNo: value
		},
		function(result){
			if(result == 1){
				alert("該時段已有學生選取了，不能進行刪除！");
			}else{
				if(confirm('是否確定刪除？')){
					window.location.href = "ReceiptBookTimeServlet?type=del&id="+value;
				}
			}
		}
	)
}
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
<%
String type = (String)request.getAttribute("type");

%>
<h2>時段設置</h2>
<form action="ReceiptBookTimeServlet" method="post" name="searchForm">
  <input type="hidden" name="type" value="search">
  &nbsp;&nbsp;請輸入學期：
  <input type="text" size="2" maxlength="4" class="inp" id="searchIntake" name="searchIntake">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <input type="radio" name="periodType" value="R" style="background-color: #F5F2DA; border: 0;">領書時段
  &nbsp;&nbsp;
  <input type="radio" name="periodType" value="P" style="background-color: #F5F2DA; border: 0;">付款時段
  &nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="submit" name="search" class="search" value="搜 索">&nbsp;&nbsp;
  <input type="reset" name="reset" class="" value="重 置">
</form>
<p>
<%
if(type != null && type.equals("currPage")){
	List timeList = (List)request.getAttribute("timeList");
	%>
	
<table width="98%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td colspan="6" align="left" height="30">&nbsp;&nbsp;當前學期為：<%=session.getAttribute("curIntake") %></td>
  </tr>
  <tr>
    <td colspan="6" height="30">
      &nbsp;
      <input type="button" name="addBtn" value="添 加" style="width:50px;padding:0px 0px;" onClick="window.location.href='receiptBookTimeAdd.jsp'" />
      &nbsp;&nbsp;
      <input type="button" name="importBtn" value="選擇Excel文件導入" style="width:150px;padding:0px 0px;" onClick="window.location.href='uploadTimes.jsp'" />
    </td>
  </tr>
  <%
  if(timeList.isEmpty()){
  %>
  <tr>
    <td colspan="6" height="35">&nbsp;&nbsp;當前學期的時段還沒設置，請添加！</td>
  </tr>
  <%
  }else{
	  for(int i=0; i<timeList.size(); i++){
		  Period period = (Period)timeList.get(i);
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  %>
  <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td width="15%" height="20">&nbsp;&nbsp;種類：<%if(period.getType().equals("R")){out.print("領書時段");}else{out.print("付款時段");} %></td>
    <td width="15%">人數限制：<%=period.getMaxNo() %></td>
    <td width="30%">開始時間：<%=format.format(period.getStartTime()) %></td>
    <td width="30%">結束時間：<%=format.format(period.getEndTime()) %></td>
    <td width="5%" align="center"><a href="ReceiptBookTimeServlet?type=edit&id=<%=period.getPeriodNo() %>">編輯</a></td>
    <td width="5%" align="center"><a href="javascript:Del('<%=period.getPeriodNo() %>')">刪除</a></td>
  </tr>
  <%
  	  }
  }
  %>
</table>
<%
}else if(type != null && type.equals("searchPage")){
	List resultList = (List)request.getAttribute("resultList");
%>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td colspan="6" align="left" height="30">&nbsp;&nbsp;搜索的學期為：<%=request.getAttribute("searchIntake") %></td>
  </tr>
  <tr>
    <td colspan="6" height="30">
      &nbsp;
      <input type="button" name="addBtn" value="添 加" style="width:50px;padding:0px 0px;" onClick="window.location.href='receiptBookTimeAdd.jsp'" />
	  &nbsp;&nbsp;
	  <input type="button" name="importBtn" value="選擇Excel文件導入" style="width:150px;padding:0px 0px;" onClick="window.location.href='uploadTimes.jsp'" />
	</td>
  </tr>
  <%
  if(resultList.isEmpty()){
  %>
  <tr>
    <td colspan="6" height="35">&nbsp;&nbsp;當前搜索學期的時段還沒設置，請添加！</td>
  </tr>
  <%
  }else{
	  for(int i=0; i<resultList.size(); i++){
		  Period period = (Period)resultList.get(i);
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  %>
  <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td width="15%" height="20">&nbsp;&nbsp;種類：<%if(period.getType().equals("R")){out.print("領書時段");}else{out.print("付款時段");} %></td>
    <td width="15%">人數限制：<%=period.getMaxNo() %></td>
    <td width="30%">開始時間：<%=format.format(period.getStartTime()) %></td>
    <td width="30%">結束時間：<%=format.format(period.getEndTime()) %></td>
    <td width="5%" align="center"><a href="ReceiptBookTimeServlet?type=edit&id=<%=period.getPeriodNo() %>">編輯</a></td>
    <td width="5%" align="center"><a href="javascript:Del('<%=period.getPeriodNo() %>')">刪除</a></td>
  </tr>
  <%
	  }
  }
  %>
</table>	
<%
}
%>
</body>
</html>