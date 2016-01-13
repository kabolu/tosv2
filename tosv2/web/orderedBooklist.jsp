<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 1);

String period = null;

String withdraw = null;
if(request.getAttribute("withdraw") != null){
	withdraw = (String)request.getAttribute("withdraw");
}
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>已訂圖書信息</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/ext-all.css" />
<style type="text/css">
<!--
.style1 {
	font-size: 14px
}
.textlen {
	height: 18px;
	width: 20px;
}
.btnlen {
	height: 20px;
	width: 20px;
}
.table td{
	border: #EEC591 1px solid;
}
-->
</style>
<script language="javascript">
function orderbook(){
	window.parent.main.location.href = "orderbook.jsp";
}
function cancelBook(isbn) {
	document.form1.action = "CancelOrderBookServlet?isbn="+isbn+""
	document.form1.submit();
}
  //加1
function plus(causeId, flagId, confirmId, oldConfirmId, withdrawId, beforeWithdraw, differId, isbn, orderSeqNo, paidStatus, notEnoughQty) {
	var confirmCount = parseInt(document.getElementById(confirmId).value);
    var oldConfirmCount = parseInt(document.getElementById(oldConfirmId).value);
    var withdrawCount =  parseInt(document.getElementById(withdrawId).value);
    var beforeWithdraw =  parseInt(document.getElementById(beforeWithdraw).value);
    var isbn = isbn;
    var differ = parseInt(document.getElementById(differId).value);
    var cause = document.getElementsByName(causeId);
    var causeValue = "";	//causeValue contain values: NoBookStorage, StudentDo;
    for(i=0; i<cause.length; i++){
    	if(cause[i].checked){
    		causeValue = cause[i].value;
    	}
    }
    if(causeValue == "NoBookStorage"){
    	alert("已選擇為‘無貨退書’，不能進行此操作！");
    	return false;
    }
    //alert(notEnoughQty+" -->0: notenoughqty is not null; --->-1:notenoughqty is null!");
    if(notEnoughQty == 0){
    	alert("該圖書已被領取，不可作添加書本數量！");
    	return false;
    }else{
    	if(paidStatus == "N"){
			document.getElementById(confirmId).value = confirmCount+1;
		    if('<%=withdraw%>'=="Y") {
			    if((withdrawCount-1) >= beforeWithdraw){
					document.getElementById(withdrawId).value = withdrawCount - 1;
			    }
			}
    	}else{
    		if((confirmCount+1)>oldConfirmCount){
				alert("該訂單為已付過款，不能在此增加圖書數目！");
			}else{
				document.getElementById(confirmId).value = confirmCount+1;
				if((withdrawCount-1) >= beforeWithdraw){
					document.getElementById(withdrawId).value = withdrawCount - 1;
			    }
			}
    	}
    	document.getElementById(differId).value = differ - 1;
    	if(parseInt(document.getElementById(confirmId).value) < oldConfirmCount){
    		document.getElementById(flagId).value = "false";
    	}else{
    		document.getElementById(flagId).value = "true";
    	}
    }
}
  
  //減1
function confirmMinus(causeId, flagId, confirmId, oldConfirmId, withdrawId, beforeWithdraw, differId, isbn, orderSeqNo, paidStatus, notEnoughQty, withdrawInd) {
	var confirmCount = parseInt(document.getElementById(confirmId).value);
    var oldConfirmCount = parseInt(document.getElementById(oldConfirmId).value);
    var withdrawCount =  parseInt(document.getElementById(withdrawId).value);
    var beforeWithdraw =  parseInt(document.getElementById(beforeWithdraw).value);
    var differ = parseInt(document.getElementById(differId).value);
    var isbn = isbn;
    var cause = document.getElementsByName(causeId);
    var causeValue = "";	//causeValue contain values: NoBookStorage, StudentDo;
    for(i=0; i<cause.length; i++){
    	if(cause[i].checked){
    		causeValue = cause[i].value;
    	}
    }
    //alert("confirmCount="+confirmCount+" oldConfirmCount="+oldConfirmCount+" withdrawCount="+withdrawCount+" beforeWithdraw="+beforeWithdraw);
    //alert(notEnoughQty+" -->0: notenoughqty is not null; --->-1:notenoughqty is null!");
    if(causeValue == "NoBookStorage"){
    	alert("已選擇為‘無貨退書’，不能進行此操作！");
    	return false;
    }
    if(confirmCount == 0){
    	alert("確定數已為0，不能進行此操作！");
    	return false;
    }
    if(notEnoughQty > 0){
    	var sub = differ + 1;
    	document.getElementById(differId).value = sub;
    	//alert("differ="+document.getElementById(differId).value);
    	if(sub > notEnoughQty){
    		document.getElementById(differId).value = notEnoughQty;
    		alert("該圖書存在缺書數且超過可退數量！");
    		return false;
    	}
    	if(withdrawInd == "Y"){	//可退書
    		if(paidStatus == "N"){
    			if('<%=period %>' == "ONSALE"){
    				if((confirmCount-1) >= oldConfirmCount){
    					document.getElementById(confirmId).value = confirmCount-1;
    				}else{
    					alert("請學員自行在訂書系統網上版上修改訂書內容");
	   					return false;
    				}
   				}else{
   					if(confirmCount > 0) {
						document.getElementById(confirmId).value = confirmCount-1;
					} else {
						alert("訂書數不能小於零！");
						return false;
					}
					if('<%=withdraw%>' == "Y") {
						if(parseInt(document.getElementById(confirmId).value) < oldConfirmCount){
							document.getElementById(withdrawId).value = withdrawCount+1;
						}
						if((withdrawCount+1) < beforeWithdraw){
							document.getElementById(withdrawId).value = withdrawCount+1;
						}
					}
   				}
    		}else{	//已付款訂單
    			if(confirmCount > 0) {
					document.getElementById(confirmId).value = confirmCount-1;
				} else {
					alert("訂書數不能小於零！");
					return false;
				}
				if('<%=withdraw%>' == "Y") {
					if(parseInt(document.getElementById(confirmId).value) < oldConfirmCount){
						document.getElementById(withdrawId).value = withdrawCount+1;
					}
					if((withdrawCount+1) < beforeWithdraw){
						document.getElementById(withdrawId).value = withdrawCount+1;
					}
				}
    		}
    		if(parseInt(document.getElementById(confirmId).value) < oldConfirmCount){
	    		document.getElementById(flagId).value = "false";
	    	}else{
	    		document.getElementById(flagId).value = "true";
	    	}
    	}else{	//不可退書
    		if(causeValue == "StudentDo"){
    			alert("該圖書為不可退書的！");
    		}
    	}
    }else if(notEnoughQty == 0){
    	alert("該圖書已被領取，不可作刪減書本數量！");
    	return false;
    }else{
    	if(withdrawInd == "Y"){	//可退書
    		if(paidStatus == "N"){
    			if('<%=period %>' == "ONSALE"){
    				if((confirmCount-1) >= oldConfirmCount){
    					document.getElementById(confirmId).value = confirmCount-1;
    				}else{
    					alert("請學員自行在訂書系統網上版上修改訂書內容");
	   					return false;
    				}
   				}else{
   					if(confirmCount > 0) {
						document.getElementById(confirmId).value = confirmCount-1;
					} else {
						alert("訂書數不能小於零！");
						return false;
					}
					if('<%=withdraw%>' == "Y") {
						if(parseInt(document.getElementById(confirmId).value) < oldConfirmCount){
							document.getElementById(withdrawId).value = withdrawCount+1;
						}
						if((withdrawCount+1) < beforeWithdraw){
							document.getElementById(withdrawId).value = withdrawCount+1;
						}
					}
   				}
    		}else{	//已付款訂單
    			if(confirmCount > 0) {
					document.getElementById(confirmId).value = confirmCount-1;
				} else {
					alert("訂書數不能小於零！");
					return false;
				}
				if('<%=withdraw%>' == "Y") {
					if(parseInt(document.getElementById(confirmId).value) < oldConfirmCount){
						document.getElementById(withdrawId).value = withdrawCount+1;
					}
					if((withdrawCount+1) < beforeWithdraw){
						document.getElementById(withdrawId).value = withdrawCount+1;
					}
				}
    		}
    		if(parseInt(document.getElementById(confirmId).value) < oldConfirmCount){
	    		document.getElementById(flagId).value = "false";
	    	}else{
	    		document.getElementById(flagId).value = "true";
	    	}
    	}else{	//不可退書
    		if(causeValue == "StudentDo"){
    			alert("該圖書為不可退書的！");
    		}
    	}
    }    
}

function saveUpdate(size) {
	var studentNo =  document.form1.studentNo.value;
	var applicantNo =  document.form1.applicantNo.value;
	var checkSubmit = true;
	for(var i=0; i<size; i++){
		var value = document.getElementById("flag"+i).value;
		var cause = document.getElementsByName("cause"+i);
	    var causeValue = "";	//causeValue contain values: NoBookStorage, StudentDo;
	    for(var j=0; j<cause.length; j++){
	    	if(cause[j].checked){
	    		causeValue = cause[j].value;
	    	}
	    }
		if(value == "false" && causeValue == ""){
			checkSubmit = false;
			break;
		}
	}
	if(checkSubmit){
		document.form1.submit();
	}else{
		alert("有退書的記錄，請選擇退書原因！");
		return false;
	}
}
function causeClick(value, i, confirmId, oldConfirmId, withdrawId, beforeWithdraw, differId, notenoughqty){
    var oldConfirmCount = parseInt(document.getElementById(oldConfirmId).value);
    var beforeWithdraw =  parseInt(document.getElementById(beforeWithdraw).value);
    if(value == "NoBookStorage"){
		if(notenoughqty > 0){
			document.getElementById(confirmId).value = oldConfirmCount - notenoughqty;
		}else if(notenoughqty == 0){
			alert("該圖書已領取，不能進行“無貨退書”操作！");
			$('#StudentDo'+i+'').attr('checked', true);
			$('#NoBookStorage'+i+'').attr('checked', false);
			return false;
		}else{
			document.getElementById(confirmId).value = 0;
		}
		document.getElementById(withdrawId).value = beforeWithdraw;
		document.getElementById(differId).value = 0;
	}else if(value == "StudentDo"){
		alert("已選擇“學生退書”操作，請修改確認數目！");
		document.getElementById(confirmId).value = oldConfirmCount;
		document.getElementById(withdrawId).value = beforeWithdraw;
		document.getElementById(differId).value = 0;
	}
}
</script>
</head>
<body>
<%
List<Order> seqNoList = null;
if(request.getAttribute("seqNoList") != null){
	seqNoList = (List)request.getAttribute("seqNoList");
}

String oprType = null;
if(request.getAttribute("oprType") != null){
	oprType = (String)request.getAttribute("oprType");
}

String isPay = null;
if(request.getAttribute("isPay") != null){
	isPay = (String)request.getAttribute("isPay");
}

List orderedBookList = null;
if(request.getAttribute("orderedBookList") != null){
	orderedBookList = (List)request.getAttribute("orderedBookList");
}
List stuDetList = null;
if(request.getAttribute("stuDetList") != null){
	stuDetList = (List)request.getAttribute("stuDetList"); 
}

List<Book> bookList = null;
List<Price> priceList = null;
List<OrDetail> qtyList = null;
List<Order> oList = null;
int orderSeqNo = 0;
Order order = null;
if(orderedBookList != null) {
	bookList = (List)orderedBookList.get(0);
   	priceList = (List)orderedBookList.get(1);
   	qtyList = (List)orderedBookList.get(2);
   	oList = (List)orderedBookList.get(3);
   	if(!qtyList.isEmpty()){
   	   	OrDetail orDetail = (OrDetail)qtyList.get(0);
   	   	orderSeqNo = orDetail.getOrderSeqNo();
   	   	order = (Order)oList.get(0);
   	}
}
Student stu = null;
Program pro = null;
String studNo = "null";
String appNo = "null";

if(stuDetList != null) {
	stu = (Student)stuDetList.get(0);
	studNo = stu.getStudentNo();
	appNo = stu.getApplicantNo();
   	pro = (Program)stuDetList.get(2);
}
%>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
  <tr bgcolor="#F4A460">
    <td align="center" width="20%" height="28"><b>學號</b></td>
    <td align="center" width="15%"><b>姓名</b></td>
    <td align="center" width="20%"><b>課程</b></td>
    <td align="center" width="45%"><b>圖書訂單編號</b></td>
  </tr>
  <tr>
  <% 
  if(stu != null && pro != null) { 
  %>
    <td align="center" height="22">
    <%if(stu.getStudentNo() != null){out.print(stu.getStudentNo());}else{out.print(stu.getApplicantNo());} %>
    </td>
    <td align="center"><%=stu.getChineseName() %></td>
    <td align="center"><%=pro.getChineseName() %></td>
  <%
  } 
  %>
    <td align="center">
  <%
  if(seqNoList != null && !seqNoList.isEmpty()){
	  for(Order o : seqNoList){
		  out.print("<a href='OrderedBookListServlet?orderSeqNo="+o.getOrderSeqNo()+"&studentNo="+studNo+"&applicantNo="+appNo+"'>"+o.getOrderSeqNo()+"</a>"+"&nbsp;&nbsp;&nbsp;&nbsp;");
	  }
  }else{
	  %>&nbsp;<%
  }
  %>
  	</td>
  </tr>
</table>
<form id="form1" name="form1" method="post" action="UpdateOrderedBookServlet">
<%
if(stu != null){
%>
  <input type="hidden" name="studentNo" value="<%=stu.getStudentNo() %>" />
  <input type="hidden" name="applicantNo" value="<%=stu.getApplicantNo() %>" />
<%
}
%>
  <p align="center" style="margin:10px 0px 5px 0px;font-size:14px;">圖書訂單編號為
    <font style="font-size:14pt;font-sytle:normal;font-weight:bold;color:red;"><%=orderSeqNo==0?"N/A":orderSeqNo %></font>
    <%
    if(isPay != null && isPay.equals("N")){
    	out.print("(未付款)");
    }else{
    	if(orderSeqNo == 0){
    		out.print("");
    	}else{
    		out.print("(已付款)");
    	}
    }
    %>的圖書清單如下：
  </p>
  <input type="hidden" name="orderSeqNo" id="orderSeqNo" value="<%=orderSeqNo %>"/>
  <input type="hidden" name="paidStatus" id="paidStatus" value="<%=isPay %>"/>
  <table width="100%"  align="center" border="1" cellspacing="1" cellpadding="0" id="the-table">
    <tr bgcolor="#C6D6FD">
      <td width="15%" align="center" height="26"><strong>圖書編號</strong></td>
      <td width="35%" align="center"><strong>書名</strong></td>
      <td width="7%"  align="center"><strong>預估價格(MOP)</strong></td>  
      <td width="7%"  align="center"><strong>實際價格(MOP)</strong></td>
      <td width="10%" align="center" colspan="2" ><strong>確認</strong></td>
      <% 
      if(withdraw != null && withdraw.equals("Y")) {
      %>
	  <td align="center" width="7%"><strong>退訂</strong></td>
	  <td align="center" width="12%"><strong>原因</strong></td>
	  <td align="center" width="7%" ><strong>操作</strong></td>
	  <%
	  }else{
	  %>
	  <td align="center" width="12%"><strong>原因</strong></td>
	  <td width="14%" align="center"><strong>操作</strong></td>
	  <% 
	  } 
	  %>
    </tr>
    <%
    int size = 0;
    if(bookList != null && priceList != null && qtyList != null){
    	size = bookList.size();
    	for(int i=0; i<bookList.size(); i++) {
    		Book book = (Book)bookList.get(i);
    		Price price = (Price)priceList.get(i);
    		OrDetail orDetail = (OrDetail)qtyList.get(i);
    %>
    <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
      <td height="60" align="left" width="15%" >
        <a href="BookDetailServlet?isbn=<%=book.getIsbn() %>" target="_blank"><%=book.getIsbn() %></a>
      </td>
      <td align="left" width="40%" >
        <%=book.getTitle() %>
        <input type="hidden" name="oprType" value="cancel" />
        <input type="hidden" name="isbn" value="<%=book.getIsbn() %>" />
      </td>
      <td  align="left" width="5%" >
<%--      <%--%>
<%--      if(price.getMopNetPrice() == 0){--%>
<%--    	  out.print(price.getMopFuturePrice());--%>
<%--      }else{--%>
<%--    	  out.print(price.getMopNetPrice());--%>
<%--      }--%>
<%--	  %>--%>
	  <%=price.getMopFuturePrice() %>
      </td>
      <td align="left" width="5%">
<%--      <%--%>
<%--      if(price.getRmbNetPrice() == 0){--%>
<%--    	  out.print(price.getRmbFuturePrice());--%>
<%--      }else{--%>
<%--    	  out.print(price.getRmbNetPrice());--%>
<%--      }--%>
<%--	  %>--%>
	  <%
      if(price.getMopNetPrice() == 0 || price.getMopNetPrice()== price.getFuturePrice()){
    	  out.print("");
      }else{
    	  out.print(price.getMopNetPrice());
      }
	  %>
      </td>
      <td colspan="2" align="center" width="10%">
        <table width="50" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td rowspan="2">
              <input name="confirmQty" type="text" class="textlen" id="confirm<%=i %>" value="<%=orDetail.getConfirmQty() %>" readonly="readonly"/>
              <input name="oldConfirmQty" type="hidden" class="textlen" id="oldConfirm<%=i %>" value="<%=orDetail.getConfirmQty() %>" />
            </td>
            <td align="left" valign="middle">
              <input type="button" name="Submit22" value="+" class="btnlen" onclick="plus('cause<%=i %>', 'flag<%=i %>', 'confirm<%=i %>', 'oldConfirm<%=i %>', 'withdrawQty<%=i %>', 'beforeWithdrawQty<%=i %>', 'differ<%=i %>', '<%=book.getIsbn() %>', '<%=orderSeqNo %>', '<%=order.getPaidStatus() %>', '<%=orDetail.getNotEnoughQty() %>');" />
            </td>
          </tr>
          <tr>
            <td align="left" valign="middle">
              <input type="button" name="Submit22" value="-" class="btnlen" onclick="confirmMinus('cause<%=i %>', 'flag<%=i %>', 'confirm<%=i %>', 'oldConfirm<%=i %>', 'withdrawQty<%=i %>', 'beforeWithdrawQty<%=i %>', 'differ<%=i %>', '<%=book.getIsbn() %>', '<%=orderSeqNo %>', '<%=order.getPaidStatus() %>', '<%=orDetail.getNotEnoughQty() %>', '<%=book.getWithdrawInd() %>');" />
            </td>
          </tr>
        </table>
        <input type="hidden" name="oldWithdrawQty" id="oldwithdrawQty<%=i %>" value="<%=orDetail.getWithdrawQty() %>" class="textlen" />
        <input type="hidden" name="beforeWithdrawQty" id="beforeWithdrawQty<%=i %>" value="<%=orDetail.getWithdrawQty() %>" class="textlen" />
        <input type="hidden" name="flag" id="flag<%=i %>" value=""/>
        <input type="hidden" name="differ" id="differ<%=i %>" value="0"/>
      </td>
      <%
      if(withdraw != null && withdraw.equals("Y")) {
      %>
      <td align="center" width="10%">
        <input type="text" name="newWithdrawQty" id="withdrawQty<%=i %>" value="<%=orDetail.getWithdrawQty() %>" class="textlen" readonly="readonly" />
      </td>
	  <td>
	    <input type="radio" id="NoBookStorage<%=i %>" name="cause<%=i %>" value="NoBookStorage" onclick="causeClick(this.value, '<%=i %>', 'confirm<%=i %>', 'oldConfirm<%=i %>', 'withdrawQty<%=i %>', 'beforeWithdrawQty<%=i %>', 'differ<%=i %>', '<%=orDetail.getNotEnoughQty() %>');" style="border:0px;background-color: #FFFBEF;"/>無貨退書
	    <br/>
	    <input type="radio" id="StudentDo<%=i %>" checked="checked" name="cause<%=i %>" value="StudentDo" onclick="causeClick(this.value, '<%=i %>', 'confirm<%=i %>', 'oldConfirm<%=i %>', 'withdrawQty<%=i %>', 'beforeWithdrawQty<%=i %>', 'differ<%=i %>', '<%=orDetail.getNotEnoughQty() %>');" style="border:0px;background-color: #FFFBEF;"/>學生退書
	  </td>
	  <td>
	  <%
	  if("ONSALE".equals(period) || "ONSALE_PAID".equals(period)) {
	  %>
	    <input type="button" name="Submit3" value="撤 銷"  onclick="cancelBook('<%=book.getIsbn() %>')"/>
	  <%
	  } 
	  %>
	    <input type="reset" name="Submit2" value="重 置"/>
	  </td>
	  <%
	  } else {
	  %>
	  <td>
	    <input type="hidden" name="newWithdrawQty" id="withdrawQty<%=i %>" value="<%=orDetail.getWithdrawQty() %>" class="textlen" readonly="readonly" />
	    <input type="radio" id="NoBookStorage<%=i %>" name="cause<%=i %>" value="NoBookStorage" onclick="causeClick(this.value, '<%=i %>', 'confirm<%=i %>', 'oldConfirm<%=i %>', 'withdrawQty<%=i %>', 'beforeWithdrawQty<%=i %>', 'differ<%=i %>', '<%=orDetail.getNotEnoughQty() %>');" style="border:0px;background-color: #FFFBEF;"/>無貨退書
	    <br/>
	    <input type="radio" id="StudentDo<%=i %>" checked="checked" name="cause<%=i %>" value="StudentDo" onclick="causeClick(this.value, '<%=i %>', 'confirm<%=i %>', 'oldConfirm<%=i %>', 'withdrawQty<%=i %>', 'beforeWithdrawQty<%=i %>', 'differ<%=i %>', ''<%=orDetail.getNotEnoughQty() %>'');" style="border:0px;background-color: #FFFBEF;"/>學生退書
	  </td>
	  <td align="center">
	  <% 
	  if("ONSALE".equals(period)) {
	  %>
	    <input type="button" name="Submit3" value="撤 銷"  onclick="cancelBook('<%=book.getIsbn() %>')"/>
	  <%
	  } 
	  %>
	    <input type="reset" name="Submit2" value="重 置"/>
      </td>
	  <%
	  }
	  %>
    </tr>
    <%
     }
    }
    %>
  </table>
  <p align="center" class="style1">&nbsp;</p>
  <p align="center" class="style1">
    <label>
    <%
    if(oprType != null && oprType.equals("search")) {
    	%>
    	<input type="button" name="Submit" value=" 返 回 " onclick="javascript:history.back();" />&nbsp;&nbsp;&nbsp;&nbsp;
    	<% 
    	if(bookList != null && !bookList.isEmpty()) {
    		%>
    		<input type="button" name="Submit" value=" 列印發票 " onclick="" />
    		<%
    	}
    } else {
    	if(bookList != null && !bookList.isEmpty()) {
    		%>
    		<input type="button" name="Submit" value=" 保 存 " onclick="saveUpdate('<%=size %>');" style="padding: 3px;"/>
    		<%
    	}
    }
    %>
    </label>
  </p>
</form>
</body>
</html>