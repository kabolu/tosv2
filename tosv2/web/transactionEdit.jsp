<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.Transaction"%>
<%@page import="edu.must.tos.util.ToolsOfString"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更改交易項目</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
function checkForm1(){
	if(searchForm1.fromDate.value == "" && searchForm1.toDate.value == ""){
		alert("請輸入開始時間和結束時間!");
		return false;
	}else if(searchForm1.fromDate.value != "" && searchForm1.toDate.value != ""){
		a = searchForm1.fromDate.value;
		b = searchForm1.toDate.value;
		var d1 = new Date(a.replace(/\-/g, "\/"));
		var d2 = new Date(b.replace(/\-/g, "\/"));
		if(Date.parse(d1) - Date.parse(d2)>0){
			alert(a + "晚於" + b);
			return false;
		}else{
			return true;
		}
	}else {
		alert("請完整填寫開始時間和結束時間!");
		return false;
	}
}
function cancelTransaction(transactionNo){
	window.location.href = "TransactionServlet?transactionNo="+transactionNo+"&type=cancelRecd";
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<h2>更改交易項目</h2>
<form name="searchForm1" action="TransactionServlet" method="post" onSubmit="return checkForm1();">
  <input type="hidden" name="type" value="searchRecord">
  <table border="0" cellpadding="0" cellspacing="0" width="60%">
    <tr>
      <td height="30" width="110">輸入查詢時間段:</td>
      <td>
        <label>從:</label>
        <input name="fromDate" size="10" type="text" class="inp" id="fromDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
        &nbsp;~&nbsp;
        <label>到:</label>
        <input name="toDate" size="10" type="text" class="inp" id="toDate" onClick="new Calendar(null, null, 3).show(this);" maxlength="10" readonly="readonly" />
      </td>
    </tr>
    <tr>
      <td height="30">訂單編號:</td>
      <td>
        <input type="text" class="inp" name="orderSeqNo" maxlength="10"/>
      </td>
    </tr>
    <tr>
      <td height="30">交易項目:</td>
      <td>
        <select name="items">
          <option value="">---</option>
          <option value="保管費">保管費</option>
          <option value="補差價">補差價</option>
          <option value="REPRINTFEE">重印收據</option>
          <option value="調整金額">調整金額</option>
        </select>
      </td>
    </tr>
    <tr>
      <td colspan="2" height="50">
        <input type="submit" name="searchBtn" value=" 搜  索 ">
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" name="resetBtn" value=" 重  置 ">
      </td>
    </tr>
  </table>
</form>
<%
List<Transaction> list = null;
if(request.getAttribute("list") != null){
	list = (List)request.getAttribute("list");
}
%>
<table width="99%">
  <%
  if(list == null){
  %>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <%	  
  } else {
	  if(list.isEmpty()){
	  %>
	  <tr>
	    <td>沒有相關記錄!</td>
	  </tr>
	  <%
	  } else {
	  %>
	  <tr>
	    <td>
	      <table width="100%" align="center" border="0" cellspacing="1" cellpadding="1">
	        <tr bgcolor="#C6D6FD">
	          <th height="30">訂單編號</th>
	          <th>交易時間</th>
	          <th>收款人</th>
	          <th>交易項目</th>
	          <th>交易金額</th>
	          <th>&nbsp;</th>
	        </tr>
	        <%
	        for(Transaction t : list){
	        %>
	        <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
	          <td height="25"><%=t.getOrderSeqNo() %></td>
	          <td><%=t.getPaidDate() %></td>
	          <td><%=t.getCashier() %></td>
	          <td><%=t.getPaidMentType() %></td>
	          <td><%=t.getPay() + " " + t.getPaidCurrency() %></td>
	          <td align="center">
	            <input type="button" name="Submit5" value=" 刪 除 " style="width:auto;padding:0px 0px;" onclick="cancelTransaction('<%=t.getTransactionNo() %>');" />
	          </td>
	        </tr>
	        <%
	        }
	        %>
	      </table>
	    </td>
	  </tr>
	  <%
	  }
  }
  %>
  <tr>
    <td></td>
  </tr>
</table>
<%
String msg = (String)request.getAttribute("msg");
if(msg != null && !"".equals(msg)){
%>
<script language="javascript">
alert('<%=msg %>');
//history.back();
</script>
<%
}
%>
</body>
</html>