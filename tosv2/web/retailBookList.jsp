<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%@ page import="java.math.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>零售收費版面</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<style type="text/css">
<!--
#textlen {
	height: 18px;
	width: 50px;
}
#len {
	width: 20px;
}
-->
</style>
<script language="javascript">
//繳納書費
function pay() {
	if(!document.getElementById("mop").checked && !document.getElementById("rmb").checked && !document.getElementById("hkd").checked) {
   		alert("請選擇使用哪種貨幣付款！");
   		return false;
   	}else{
   		var flag = 0;
		$('input[@name^=mopPrice]').each(function(){
			var quantity = $(this).val();
			if(quantity == 0 || quantity == "" || isNaN(quantity)){
				alert("請檢查數圖書價格填寫是否正確完整，後保存記錄！");
				flag = 1;
				$(this).select();
				return false;
			}
		})
		if(flag == 0){
			$('input[@name^=mopNetPrice]').each(function(){
				var quantity = $(this).val();
				if(isNaN(quantity)){
					alert("請檢查數圖書價格填寫是否正確完整，後保存記錄！");
					flag = 1;
					$(this).select();
					return false;
				}
			})
		}
		if(flag == 0){
			document.getElementById("payButton").disabled = true;
		   	document.form1.action = "RetailBookServlet?type=payOrder";
		   	document.form1.submit();
		}
   	}
}

//更新欠書數目
function saveOrUpdateNotEnQty() {
	if(document.form1.notEnoughQty.length > 0) {
		for(var i = 0; i < document.form1.notEnoughQty.length; i++){
	    	var value = document.form1.notEnoughQty[i].value;
	    	if(value!=""){
	    		if(isNaN(value)){
	    			alert("請檢查欠數數量必須為整數!");
	      			return ;
	    		}else{
	    			if(value<0){
	    				alert("不能輸入負數！");
	    				return ;
	    			}else{
	    				if(parseInt(document.form1.notEnoughQty[i].value)>parseInt(document.form1.confirmQty[i].value)){
	    					alert("缺書數量不能超過確認數量!");
	      					return ;
	    				}
	    			}
	    		}
	    	}
	   }
	} else {
		var value = document.form1.notEnoughQty.value;
		if(value != ""){
			if(isNaN(value)) {
	      		alert("請檢查欠數數量必須為整數!");
		  		return ;
	    	} else {
		    	if(value < 0){
		    		alert("不能輸入負數");
		    		return ;
		    	}else{
		    		if(parseInt(document.form1.notEnoughQty.value) > parseInt(document.form1.confirmQty.value)){
		    			alert("缺書數量不能超過確認數量!");
		      			return ;
		    		}
		    	}
	    	}
		}
	}
	document.form1.action = "RetailBookServlet?type=receiveRetailBook";
   	document.form1.submit();
}

function setNotEnoughQty(){
	var len = document.form1.notEnoughQty.length;
	if(len == undefined){
		document.form1.notEnoughQty.value = 0;
	}else{
		for(var i=0; i<len; i++){
			document.form1.notEnoughQty[i].value = 0;
		}
	}
}

function returnBack(step){
	document.backForm.oprStep.value = step;
	document.backForm.action = "RetailBookServlet";
	document.backForm.submit();
}

function Number(e) {
   var KeyCode = (e.keyCode) ? e.keyCode : e.which;
   return ((KeyCode == 8)    // backspace
        || (KeyCode == 9)    // tab
        || (KeyCode == 37)   // left arrow
        || (KeyCode == 39)   // right arrow
        || (KeyCode == 46)   // delete
        || (KeyCode == 190)  // .
        || (KeyCode == 110)  // .
        || ((KeyCode > 47) && (KeyCode < 58))     // 0 - 9
        || ((KeyCode >= 96) && (KeyCode <= 105))  // 0 - 9
   );
}

function countSum(val, i){
	var size = $('input[@name=size]').val();
	if(val == "MOP"){
		var mopPrice = $('#mopPrice'+i).val();
		var mopNetPrice = $('#mopNetPrice'+i).val();
		var confirmQty = $('#confirmQty'+i).val();
		alert(mopPrice+" "+mopNetPrice+" "+confirmQty);
		if(mopNetPrice != ""){
			$('#aMopPriceSum'+i).attr("value", mopNetPrice*confirmQty);
		} else {
			$('#aMopPriceSum'+i).attr("value", mopPrice*confirmQty);
		}
		var mopPriceSum = 0;
		for(var i=0; i<size; i++){
			mopPriceSum = parseFloat(mopPriceSum, 10) + parseFloat($('#aMopPriceSum'+i).val(), 10);
		}
		$('input[@name=mopPriceSum]').attr("value", mopPriceSum);
		$('input[@name=totalMop]').attr("value", mopPriceSum);
	}
}
</script>
<% 
if (session.getAttribute("userId") == null) {
%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href='login.jsp';
</script>
<% } %>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
float amercePercent = 0;
if(request.getAttribute("amercePercent") != null){
	amercePercent = Float.parseFloat(request.getAttribute("amercePercent").toString());
}

String paidStatus = null;
String paidCurrency = null;
Order order = null;
if(request.getAttribute("order") != null){
	order = (Order)request.getAttribute("order");
	if(order != null){
		paidStatus = order.getPaidStatus();
		paidCurrency = order.getPaidCurrency();
	}
}
String retail = null;
if(request.getAttribute("retail") != null){
	retail = request.getAttribute("retail").toString();
}
String oprType = (String)request.getAttribute("oprType");

String payResult = (String)request.getAttribute("payResult");

String oprStep = (String)request.getAttribute("oprStep");

List orderedBookList = (List)session.getAttribute("orderedBookList");

String back = null;
if(request.getAttribute("back") != null){
	back = (String)request.getAttribute("back");
}

Double rmbRate = null;
if(request.getAttribute("rmbRate") != null){
	rmbRate = Double.parseDouble(request.getAttribute("rmbRate").toString());
}
Double hkdRate = null;
if(request.getAttribute("hkdRate") != null){
	hkdRate = Double.parseDouble(request.getAttribute("hkdRate").toString());
}

double amerceMount = 0;
if(session.getAttribute("amerceMount") != null){
	amerceMount = Double.parseDouble(session.getAttribute("amerceMount").toString());
}

double feePercent = 0;
if(request.getAttribute("feePercent") != null){
	feePercent = Double.parseDouble(request.getAttribute("feePercent").toString());
}
double shippingFee = 0;
if(request.getAttribute("shippingFee") != null){
	shippingFee = new BigDecimal(request.getAttribute("shippingFee").toString()).multiply(new BigDecimal(0.01)).doubleValue();
}

List<Book> bookList = null;
List<Price> priceList = null;
List<OrDetail> qtyList = null;
if(orderedBookList != null && orderedBookList.size() >= 3) {
	bookList = (List)orderedBookList.get(0);
	priceList = (List)orderedBookList.get(1);
	qtyList = (List)orderedBookList.get(2); 
}
int orderSeqNo = 0;
if(request.getAttribute("orderSeqNo") != null){
	orderSeqNo = Integer.parseInt(request.getAttribute("orderSeqNo").toString());
}

double totalMop = 0.0, totalRmb = 0.0, totalWithdrawMoney = 0.0;
double bookMopSum = 0, bookRmbSum = 0; 
int totalNotEnoughQty = 0;
int totalConfirm = 0, totalUnconfirm = 0, totalWithdraw = 0;

double feeMopPrice = 0;

double shippingFeeMop = 0;

double bookMopPrice = 0;

for(int i=0; i<bookList.size(); i++) {
	OrDetail orDetail = (OrDetail)qtyList.get(i);
	Price price = (Price)priceList.get(i);
	totalConfirm += orDetail.getConfirmQty();
	totalWithdraw += orDetail.getWithdrawQty();
	double mopPrice = 0;
	if(orDetail.getNotEnoughQty() >= 0){
		totalNotEnoughQty += orDetail.getNotEnoughQty();
	}
	
	if(price.getMopNetPrice() == 0){
		mopPrice = price.getMopFuturePrice();
	}else{
		mopPrice = price.getMopNetPrice();
	}
	BigDecimal v5 = new BigDecimal(Integer.toString(orDetail.getConfirmQty()));

	BigDecimal v6 = new BigDecimal(Double.toString(mopPrice));
	
	bookMopPrice = (v5.multiply(v6)).add(new BigDecimal(Double.toString(bookMopPrice))).doubleValue();
	
	BigDecimal v8 = new BigDecimal(Double.toString(totalMop));
	//計算金額總額
	totalMop = (v5.multiply(v6)).add(v8).doubleValue();
	
	//計算書本確定數和退書數的總金額
	BigDecimal v10 = new BigDecimal(Double.toString(bookMopSum));
	
	bookMopSum = v5.multiply(v6).add(v10).doubleValue();
}

double per = 0.0;
%>
<%
String updateResult = (String)request.getAttribute("updateResult");
if(updateResult != null ) { %>
   <script>
   alert('<%=updateResult%>');
   </script>
<%
} 
%>
<% 
if(payResult != null  && !payResult.equals("") && oprStep != null && oprStep.equals("PAY")){ 
%>
    <script language="javascript">
    alert('<%=payResult%>');
    </script>
<%
}
%>
<form action="" name="backForm" method="post" >
  <input type="hidden" name="type" value="returnBack"/>
  <input type="hidden" name="oprStep" value=""/>
  <input type="hidden" name="back" value="<%=back %>"/>
  <input type="hidden" name="orderSeqNo" value="<%=orderSeqNo %>"/>
</form>
<table width="650" border="0" cellpadding="0" cellspacing="0" class="table">
  <tr>
    <td align="left" height="28"><strong>訂單編號&nbsp;&nbsp;<%=orderSeqNo %></strong></td>
  </tr>
</table>
<form id="form1" name="form1" method="post" target="_self" action="OrderedBookListServlet">
  <input type="hidden" name="back" value="<%=back %>" />
  <input type="hidden" name="retail" value="<%=retail %>" />
  <input type="hidden" name="orderSeqNo" value="<%=orderSeqNo %>" />
  <p align="center">您已選擇購買的圖書清單如下：</p>
  <p align="center">&nbsp;</p>
  <table width="99.9%" align="center" border="0" cellspacing="1" cellpadding="0" >
    <tr bgcolor="#C6D6FD">
      <td width="15%" align="center"><strong>編號</strong></td>
      <td width="40%" align="center"><strong>書名</strong></td>
      <td width="6%" align="center"><strong>預估價格<br/>(MOP)</strong></td>
      <td width="6%" align="center"><strong>實價價格<br/>(MOP)</strong></td>
      <td width="5%" align="center"><strong>確認</strong></td>
	  <%if(payResult != null && oprStep != null && oprStep.equals("RECEIVE")) { %>
	    <td width="6%" align="center"><strong>缺書</strong></td>
	  <%} %>
      <td width="15%" align="center"><strong>購買金額<br/>(MOP)</strong></td>
    </tr>
    <%
    if(bookList != null && priceList != null && qtyList != null) {
    	for(int i=0; i<bookList.size(); i++){
    		Book book = (Book)bookList.get(i);
    		OrDetail orDetail = (OrDetail)qtyList.get(i);
    		Price price = (Price)priceList.get(i);
    		%>
    <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
      <td>
        <input type="hidden" name="size" value="<%=bookList.size() %>" />
        <input type="hidden" name="isbn" value="<%=book.getIsbn() %>" />
        <a href="BookDetailServlet?isbn=<%=book.getIsbn() %>" target="_blank"><%=book.getIsbn() %></a>
      </td>
      <td><%=book.getTitle() %></td>
      <td align="left" >
        <%
        double mopPrice = price.getMopFuturePrice();
		if(paidCurrency != null ){
			mopPrice = orDetail.getPaidAmount();
		}
        %>
        <input type="text" name="mopPrice" id="mopPrice<%=i %>" value="<%=mopPrice %>" class="inp" size="4" maxlength="6" onkeyup="countSum('MOP', '<%=i %>');" onkeydown="return Number(event);"/>
      </td>
      <td align="left">
        <%
        double mopNetPrice = 0;
		if(price.getMopNetPrice() != 0 && (price.getMopNetPrice() - price.getMopFuturePrice()) != 0){
			mopNetPrice = price.getMopNetPrice();
		}
		String mopNetPriceStr = "";
		if(mopNetPrice != 0 && (mopNetPrice - mopPrice) != 0){
			mopNetPriceStr = String.valueOf(mopNetPrice);
		}
		double bookPriceSum = 0;
		if(price.getMopNetPrice() != 0 && (price.getMopNetPrice() - price.getMopFuturePrice()) != 0){
			bookPriceSum = orDetail.getConfirmQty() * mopNetPrice;
		} else {
			bookPriceSum = orDetail.getConfirmQty() * mopPrice;
		}
        %>
        <input type="text" name="mopNetPrice" id="mopNetPrice<%=i %>" value="<%=mopNetPriceStr %>" class="inp" size="4" maxlength="6" onkeyup="countSum('MOP', '<%=i %>');" onkeydown="return Number(event);"/>
	  </td>
      <td align="center">
        <input type="hidden" name="confirmQty" id="confirmQty<%=i %>" value="<%=orDetail.getConfirmQty() %>" />
        <%=orDetail.getConfirmQty() %>
      </td>
      <%
      if(payResult != null && oprStep != null && oprStep.equals("RECEIVE")) {
    	  String notenoughqty = "";
    	  if(orDetail.getNotEnoughQty() >= 0){
    		  notenoughqty = String.valueOf(orDetail.getNotEnoughQty());
    	  }
    	  %>
	  <td align="center">
	    <input type="text" id="len" name="notEnoughQty" value="<%=notenoughqty %>" />
	    <input type="hidden" id="old" name="oldNotEnoughQty" value="<%=notenoughqty %>" />
	  </td>
      <%
      } 
      %>
      <td align="center" >
        <input type="text" name="aMopPriceSum" id="aMopPriceSum<%=i %>" class="inp" size="4" readonly="readonly" value="<%=bookPriceSum %>"/>
      </td>
    </tr>
    <%
        }
    }
    %>
    <tr bgcolor="#C6D6FD">
      <td colspan="4" align="right">合計：</td>
      <td align="center"><%=totalConfirm %></td>
      <% if(payResult != null && oprStep != null && oprStep.equals("RECEIVE")) { %>
      <td align="center"><%=totalNotEnoughQty %></td>
      <%} %>
      <td align="center">
        <input type="text" name="mopPriceSum" value="<%=totalMop %>" readonly="readonly" class="inp" size="4"/>
      </td>
    </tr>
    <%
      //計算逾期付款罰款，罰款小於1作1計算，大於1的進行四捨五入計算
      BigDecimal value1 = new BigDecimal(Double.toString(bookMopSum));
      BigDecimal percent = new BigDecimal(Double.toString(per));
      double sum = 0;
      if(percent.doubleValue() != 0){
    	  if(0 < (value1.multiply(percent)).doubleValue() && (value1.multiply(percent)).doubleValue() < 1){
    		  sum = 1;
    	  }else{
    		  sum = (value1.multiply(percent)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    	  }
      }
      
      if(order != null){
    	  if(order.getPaidStatus().equals("Y")){
    		  sum = order.getFineforlatepay();
    	  }
      }
      %>
    <tr bgcolor="#CCBB66">
      <% if(payResult != null && oprStep != null && oprStep.equals("RECEIVE")) { %>
      <td colspan="7" align="right">&nbsp;</td>
      <%} else { %>
      <td colspan="6" align="right">&nbsp;</td>
      <%} %>
      <%
      BigDecimal n1 = new BigDecimal(totalMop);
      BigDecimal n2 = new BigDecimal(totalWithdrawMoney);
      BigDecimal n3 = new BigDecimal(amerceMount);
      BigDecimal n4 = new BigDecimal(sum);
      BigDecimal n5 = new BigDecimal(totalRmb);
      double allMOP = n1.add(n2).add(n3).add(n4).add(new BigDecimal(Double.toString(shippingFeeMop))).doubleValue();
      %>
    </tr>
  </table>
  <p align="center">
  <% if(bookList != null && !bookList.isEmpty() && paidStatus != null && paidStatus.equals("N") && payResult == null && oprStep != null && oprStep.equals("PAY") ) { %>
  <table width="360" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td width="130" height="35" align="center">請選擇貨幣:</td>
      <td width="230" align="left" valign="middle">
        <%
        double allRMB = 0;
        double allHKD = 0;
        if(rmbRate != null){
        	allRMB = new BigDecimal(allMOP).multiply(new BigDecimal(rmbRate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        if(hkdRate != null){
        	allHKD = new BigDecimal(allMOP).multiply(new BigDecimal(hkdRate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        %>
        <input name="paidCurrency" id="mop" type="radio" class="radio" value="MOP" />&nbsp;&nbsp;澳門幣<label class="number"><%=allMOP %></label>
        <input type="hidden" name="totalMop" value="<%=allMOP %>" />
        <input type="hidden" name="mopRate" value="1" />
        <br/>
        <input name="paidCurrency" id="rmb" type="radio" class="radio" value="RMB" />&nbsp;&nbsp;人民幣<label class="number"><%=allRMB %></label>
        <input type="hidden" name="totalRmb" value="<%=allRMB %>" />
        <input type="hidden" name="rmbRate" value="<%=rmbRate %>" />
        <br/>
        <input name="paidCurrency" id="hkd" type="radio" class="radio" value="HKD" />&nbsp;&nbsp;港紙&nbsp;&nbsp;&nbsp;<label class="number"><%=allHKD %></label>
        <input type="hidden" name="totalHkd" value="<%=allHKD %>" />
        <input type="hidden" name="hkdRate" value="<%=hkdRate %>" />
      </td>
    </tr>
  </table>
  <br/>
  <p align="center">
    <input type="button" name="Submit2" id="payButton" value="確認繳費" onclick="pay();" />
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="reset" name="Submit3" value="重 設" /> 
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" name="backButton" value="返 回" onclick="returnBack('<%=oprStep %>');"  />
  </p>
  <%
  } 
  if(bookList != null && !bookList.isEmpty() && oprStep != null && oprStep.equals("RECEIVE")) {  %>
  <br/>
  <br/>
  <br/>
  <p align = "center">
    <input type="button" name="allCheck" value="全 取" onclick="setNotEnoughQty();"/>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" name="Submit" value="保存欠書數量" onclick="saveOrUpdateNotEnQty();"/>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="reset" name="reset" value="重 設" />
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" name="backButton" value="返 回" onclick="returnBack('<%=oprStep %>');"  />
  </p>
  <%
  } 
  if(bookList != null && !bookList.isEmpty() && paidStatus != null && paidStatus.equals("Y") && oprStep != null && oprStep.equals("PAY") ) {  %>
  <p align = "center">
    <input type="button" name="backButton" value="返 回" onclick="returnBack('<%=oprStep %>');"/>
  </p>
  <% 
  }  
  %>
</form>
</body>
</html>