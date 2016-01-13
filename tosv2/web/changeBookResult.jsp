<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.OrDetail"%>
<%@page import="edu.must.tos.bean.BookPurchasingBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>置換圖書</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
$(document).ready(function(){
	$('#image').hide();
	
	$('input[@name=reset]').click(function(){
		$('#image').hide();
		$('#submit').show();
		$('#msg').text("");
	})
	
	$('#submit').click(function(){
		var searchIsbn = $('#searchIsbn').val();
		if(searchIsbn == ""){
			alert("請搜索要置換的圖書ISBN!");
			return false;
		}
		var sId = document.getElementsByName("sId");
		var sValue = "";
		for(var i=0; i<sId.length; i++){
			if(sId[i].checked == true){
				sValue += i + ";";
			}
		}
		if(sValue == ""){
			alert("請勾選置換的記錄!");
			return false;
		}
		var changetype = $('#changetype1').val();
		var changeIsbn = $('#changeIsbn').val();
		var fromDate = $('#fromDate').val();
		var toDate = $('#toDate').val();
		if(changeIsbn == ""){
			alert("請輸入置換的圖書ISBN!");
			return false;
		}else{
			if(searchIsbn == changeIsbn){
				alert("置換圖書的ISBN一樣，請檢查!");
				return false;
			}
			if(confirm("是否更換這些圖書ISBN?")){
				$.post(
					"ChangeBookServlet",
					{
						type: "checkChangeIsbn",
						isbn: changeIsbn,
						changetype: changetype
					},
					function(result){
						if(result == "N"){
							alert("該圖書ISBN不存在!");
							return false;
						}else{
							var remarks = $('#remarks').val();
							window.location.href = "ChangeBookServlet?type=changeBook&fromDate="+fromDate+"&toDate="+toDate+"&isbn="+changeIsbn+"&changetype="+changetype+"&sId="+sValue+"&remarks="+remarks;
						}
					}
				)
			}
		}
	})
})

function checkall(){
	var all = document.getElementById("allCheck");
	var sId = document.getElementsByName("sId");
	if(all.checked == true){
		for(var i=0; i<sId.length; i++){
			sId[i].checked = true;
		}
	}else{
		for(var i=0; i<sId.length; i++){
			sId[i].checked = false;
		}
	}
}

</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
List list = null;

String changetype = null;
changetype = (String)request.getAttribute("changetype");
if (request.getAttribute("list") != null) {
	list = (List) request.getAttribute("list");
}	

String isbn = "";
if (request.getAttribute("isbn") != null) {
	isbn = (String) request.getAttribute("isbn");
}

String fromDate = "";
if(request.getAttribute("fromDate") != null){
	fromDate = (String)request.getAttribute("fromDate");
}

String toDate = "";
if(request.getAttribute("toDate") != null){
	toDate = (String)request.getAttribute("toDate");
}
%>
<form action="ChangeBookServlet" method="post" id="changeForm" name="changeForm">
  <table width="99.9%" cellpadding="0" cellspacing="1" border="0">
    <%
    if ("S".equals(changetype)){
    %>
    <tr bgcolor="#C6D6FD">
      <th width="10%">全選<input name="allCheck" id="allCheck" type="checkbox" class="checkbox" onClick="checkall();"></th>
      <th height="25" width="15%">訂單編號</th>
      <th width="25%">學生編號</th>
      <th width="25%">ISBN</th>
      <th width="25%">圖書名稱</th>
    </tr>
    <%
    }else{
    %>
    <tr bgcolor="#C6D6FD">
      <th width="10%">全選<input name="allCheck" id="allCheck" type="checkbox" class="checkbox" onClick="checkall();"></th>
      <th height="25" width="25%">代購編號</th>
      <th width="15%">ISBN</th>
      <th width="25%">圖書名稱</th>
      <th width="25%">代購數量</th>
    </tr>
    <%
    }
    %>
    <%
    if (list != null && !list.isEmpty() && "S".equals(changetype)) {
    	for (int i=0; i<list.size(); i++) {
    		OrDetail od = (OrDetail)list.get(i);
    %>
    <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
      <td align="center">
        <input type="checkbox" class="checkbox" name="sId" value="<%=i %>">
      </td>
      <td height="20">
        <%=od.getOrderSeqNo() %>
        <input type="hidden" name="orderseqno" value="<%=od.getOrderSeqNo() %>"/>
      </td>
      <td><%=od.getStudentNo() %></td>
      <td><%=od.getIsbn() %><input type="hidden" name="oldIsbn" value="<%=od.getIsbn()%>"/></td>
      <td><%=od.getBookTitle() %></td>
    </tr>
    <%
    	}
    %>
    <tr>
      <td colspan="5" height="25">本學期訂購該ISBN圖書並未領取的有<%=list.size()%>個記錄！</td>
    </tr>
    <tr>
      <td>置換備註：</td>
      <td colspan="5">
        <input type="text" name="remarks" id="remarks" maxlength="50" size="80" class="inp">
      </td>
    </tr>
    <tr>
      <td colspan="5">
      <input type="hidden" name="changetype"  id="changetype1" value="<%=changetype %>"/>
        <input type="hidden" name="searchIsbn" id="searchIsbn" value="<%=isbn %>" />
        <input type="hidden" name="fromDate" id="fromDate" value="<%=fromDate %>" />
        <input type="hidden" name="toDate" id="fromDate" value="<%=toDate %>" />
        &nbsp;&nbsp;置換圖書ISBN[<font color="red"><%=isbn%></font>]至:
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="text" name="isbn" class="inp" id="changeIsbn" />
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" name="submit" id="submit" value="更換" />
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" name="reset" value="重置" />
      </td>
    </tr>
    <%
    }else if("S".equals(changetype)){
    %>
    <tr>
      <td colspan="5">本學期沒有訂單訂購該ISBN圖書！</td>
    </tr>
    <%
    }    
    if (list != null && !list.isEmpty() && "P".equals(changetype)) {
    	for (int i=0; i<list.size(); i++) {
    		BookPurchasingBean bPurchasingBean = (BookPurchasingBean)list.get(i);
    %>
    <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
      <td align="center">
        <input type="checkbox" class="checkbox" name="sId" value="<%=i %>">
      </td>      
      <td height="20">
        <%=bPurchasingBean.getOrderNo() %>
        <input type="hidden" name="orderseqno" value="<%=bPurchasingBean.getBook().getIsbn() %>"/>
      </td>
      <td><%=bPurchasingBean.getBook().getIsbn() %><input type="hidden" name="oldIsbn" value="<%=bPurchasingBean.getBook().getIsbn() %>"/></td>
      <td><%=bPurchasingBean.getBook().getTitle() %></td>
      <td><%=bPurchasingBean.getBookPurchase().getQuantity() %></td>
    </tr>
    <%
    	}
    %>
    <tr>
      <td colspan="5" height="25">本學期代購該ISBN圖書的有<%=list.size()%>個記錄！</td>
    </tr>
    <tr>
      <td>置換備註：</td>
      <td colspan="5">
        <input type="text" name="remarks" id="remarks" maxlength="50" size="80" class="inp">
      </td>
    </tr>
    <tr>
      <td colspan="5">
      <input type="hidden" name="changetype"  id="changetype1" value="<%=changetype %>" />
        <input type="hidden" name="searchIsbn" id="searchIsbn" value="<%=isbn %>" />
        <input type="hidden" name="fromDate" id="fromDate" value="<%=fromDate %>" />
        <input type="hidden" name="toDate" id="fromDate" value="<%=toDate %>" />
        &nbsp;&nbsp;置換圖書ISBN[<font color="red"><%=isbn%></font>]至:
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="text" name="isbn" class="inp" id="changeIsbn" />
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" name="submit" id="submit" value="更換" />
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" name="reset" value="重置" />
      </td>
    </tr>
    <%
    } else if ("P".equals(changetype)){
    %>
    <tr>
      <td colspan="5">本學期沒有該ISBN圖書的代購記錄！</td>
    </tr>
    <%
    }
    %>
  </table>
</form>
</body>
</html>