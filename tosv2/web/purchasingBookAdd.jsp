<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.must.tos.bean.BookPurchasingBean"%>
<%@page import="edu.must.tos.bean.BookSupplier"%>
<%@page import="edu.must.tos.util.ToolsOfString"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>代購圖書</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/tos.js"></script>
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
<script language="javascript">
$(document).ready(function(){
	var size = $('input[@name=size]').val();
	if(size!=0){
	    var i = size - 1;
		document.forms["resultForm"].elements["favourablePrice"+i].select();
	}
})
function checkItem(val){
	var orderBy = val;
	if(orderBy == "OTHER"){
		$('input[@name=orderPp]').show();
	}else{
		$('input[@name=orderPp]').hide();
		$('input[@name=orderPp]').attr("value", '');
	}
}
function saveRecord(){
	if(document.isbnForm.orderDate.value == ""){
		alert("請輸入日期！");
		return false;
	}
	if(document.isbnForm.orderBy.value == ""){
		alert("請選擇學院/教師選項！");
		return false;
	}else if(document.isbnForm.orderBy.value == "OTHER"){
		if(document.isbnForm.orderPp.value == ""){
			alert("請輸入其他資料！");
			return false;
		}
	}
	var flag = 0;
	$('input[@name^=quantity]').each(function(){
		var quantity = $(this).val();
		if(quantity == 0 || quantity == ""){
			alert("請檢查數量填寫是否完整，後保存記錄！");
			flag = 1;
		}
	})
	if(flag == 0){
		document.resultForm.orderDate.value = document.isbnForm.orderDate.value;
		document.resultForm.orderBy.value = document.isbnForm.orderBy.value;
		document.resultForm.orderPp.value = document.isbnForm.orderPp.value;
		document.resultForm.action = "PurchasingBookServlet";
		document.resultForm.submit();
	}
}
function saveSession(param, i, val){
	$.post(
		"PurchasingBookServlet",
		{
			type: "saveSession",
			i: i,
			param: param,
			value: val
		},
		function(){}
	)
}
function checkForm(){
	var isbn = document.isbnForm.isbn.value;
	if(isbn == ""){
		alert("請輸入ISBN編號！");
		return false;
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
String curIntake = null;
if(session.getAttribute("curIntake") != null){
	curIntake = (String)session.getAttribute("curIntake");
}
String orderDate = "";
if(session.getAttribute("orderDate") != null){
	orderDate = (String)session.getAttribute("orderDate");
}
int orderBy = 0;
if(session.getAttribute("orderBy") != null && !session.getAttribute("orderBy").equals("") && !session.getAttribute("orderBy").equals("OTHER")){
	orderBy = Integer.parseInt(session.getAttribute("orderBy").toString());
}
String orderPp = null;
if(session.getAttribute("orderPp") != null){
	orderPp = (String)session.getAttribute("orderPp");
}
int size = 0;
List resultList = null;
if(session.getAttribute("resultList") != null){
	resultList = (List)session.getAttribute("resultList");
	if(resultList != null){
		size = resultList.size();
	}
}
List<BookSupplier> bookSupplierList = null;
if(request.getAttribute("bookSupplierList") != null){
	bookSupplierList = (List)request.getAttribute("bookSupplierList");
}
%>
<h2>新增代購圖書資料</h2>
<form action="PurchasingBookServlet" method="post" name="isbnForm" onsubmit="return checkForm();">
<input type="hidden" name="type" value="searchIsbn"/>
<input type="hidden" name="intake" value="<%=curIntake %>"/>
<table border="0" cellpadding="0" cellspacing="0" width="600">
  <tr>
    <td width="10%">日期：</td>
    <td width="25%">
      <input name="orderDate" type="text" class="inp" value="<%=orderDate %>" onclick="new Calendar(null, null, 3).show(this);" readonly="readonly" size="8"/>
    </td>
    <td width="15%">學院/教師：</td>
    <td width="50%">
      <select name="orderBy" id="orderBy" onchange="checkItem(this.value);">
        <option value="">==請選擇==</option>
        <%
        if(bookSupplierList != null && !bookSupplierList.isEmpty()){
        	for(BookSupplier supplier : bookSupplierList){
        		%>
        <option value="<%=supplier.getSupplierNo() %>" <%if(supplier.getSupplierNo()==orderBy){out.print("selected");} %>><%=supplier.getSupplierName() %></option>
        		<%
        	}
        }
        %>
        <option value="OTHER" <%if(orderPp!=null){out.print("selected");} %>>其他</option>
      </select>
      <%
      if(orderPp != null){
    	  %>
    	  <input type="text" name="orderPp" class="inp" value="<%=orderPp %>"/>
    	  <%
      }else{
    	  %>
    	  <input type="text" name="orderPp" class="inp" style="display:none;"/>
    	  <%
      }
      %>
    </td>
  </tr>
  <tr>
    <td>ISBN:</td>
    <td colspan="3">
      <input type="text" name="isbn" class="inp"/>
      <input type="submit" name="submit" value=" 確定 "/>
      <%
      if(request.getAttribute("searchIsbnResult") != null){
    	  %>
    	  <label style="color:red;"><%=request.getAttribute("searchIsbnResult") %></label>
    	  <%
      }
      %>
    </td>
  </tr>
</table>
</form>

<form action="" method="post" name="resultForm">
<input type="hidden" name="size" value="<%=size %>"/>
<input type="hidden" name="type" value="saveRecord"/>
<input type="hidden" name="orderDate" value=""/>
<input type="hidden" name="orderBy" value=""/>
<input type="hidden" name="orderPp" value=""/>
<input type="hidden" name="issued" value="N"/>
<input type="hidden" name="intake" value="<%=curIntake %>"/>
<table cellpadding="0" cellspacing="0" border="0" width="99%">
  <%
  if(resultList != null && !resultList.isEmpty()){
	  %>
  <tr align="center">
    <td width="10%" height="28" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">ISBN</td>
    <td width="32%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">書名</td>
    <td width="15%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">作者</td>
    <td width="7%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">版次</td>
    <td width="8%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">優惠價</td>
    <td width="8%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">數量</td>
    <td width="15%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">備註</td>
    <td width="5%" style="background-color: #F9F8EB;border: 1px solid #FFDEAD;">操作</td>
  </tr>
	  <%
	  for(int i=resultList.size()-1; i>=0; i--){
		  BookPurchasingBean bean = (BookPurchasingBean)resultList.get(i);
		  %>
  <tr>
    <td style="border: 1px solid #FFDEAD;"><%=bean.getBook().getIsbn() %></td>
    <td style="border: 1px solid #FFDEAD;"><%=bean.getBook().getTitle() %></td>
    <td style="border: 1px solid #FFDEAD;"><%=bean.getBook().getAuthor() %></td>
    <td style="border: 1px solid #FFDEAD;"><%=bean.getBook().getEdition() %></td>
    <td align="center" style="border: 1px solid #FFDEAD;"> 
      <input type="text" class="inp" name="favourablePrice<%=i %>" value="<%=bean.getBookPurchase().getCostPrice() %>" maxlength="4" size="5" onblur="saveSession('favourablePrice', '<%=i %>', this.value);"/>
    </td>
    <td align="center" style="border: 1px solid #FFDEAD;"> 
      <input type="text" class="inp" name="quantity<%=i %>" value="<%=bean.getBookPurchase().getQuantity() %>" maxlength="4" size="5" onkeyup="saveSession('quantity', '<%=i %>', this.value);" onkeydown="return NumWithoutMinus(event);"/>
    </td>
    <td style="border: 1px solid #FFDEAD;">
      <input type="text" class="inp" name="remarks<%=i %>" value="<%=ToolsOfString.chkNullString(bean.getBookPurchase().getRemarks()) %>" maxlength="15" style="width:130px;" onblur="saveSession('remarks', '<%=i %>', this.value);"/>
    </td>
    <td style="border: 1px solid #FFDEAD;" align="center">
      <input type="button" class="inp" name="removeBtn" value="撤銷" onclick="window.location.href='PurchasingBookServlet?type=remove&i=<%=i %>'"/>
    </td>
  </tr>
		  <%
	  }
	  %>
  <tr>
    <td align="center" colspan="8">
      <input type="button" name="saveBtn" value=" 提交 " onclick="saveRecord();"/>
    </td>
  </tr>
	  <%
  }else{
	  %>
  <tr>
    <td colspan="8">還沒有錄入圖書信息，請輸入ISBN編號進行代購圖書！</td>
  </tr>
	  <%
  }
  %>
  
</table>
</form>
</body>
</html>