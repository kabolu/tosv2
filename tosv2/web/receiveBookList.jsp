<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%@ page import="java.math.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收費版面</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/ext-all.css" />
<script language="javascript">
function bookInvoice() {
	window.location.href = "OrderedBookListServlet?oprType=bookInvoice";
}
//繳納書費
function pay() {
	if(!document.getElementById("mop").checked && !document.getElementById("rmb").checked && !document.getElementById("hkd").checked) {
   		alert("請選擇使用哪種貨幣付款！");
   		return ;
   	}
   	document.getElementById("payButton").disabled = true;
   	document.form1.action = "ReceiveBookServlet";
   	document.form1.submit();
}
  
//列印收據
function printReceipt() {
	document.form1.target = "_blank";
	document.form1.action = "ViewReceiptServlet";
   	document.form1.submit();
}
//更新欠書數目
function saveOrUpdateNotEnQty() {
	if(document.form1.notEnoughQty.length > 0) {
		for(var i = 0; i < document.form1.notEnoughQty.length; i++){
	    	var value = document.form1.notEnoughQty[i].value;
	    	if(value != ""){
	    		if(isNaN(value)){
	    			alert("請檢查欠數數量必須為整數!");
	      			return ;
	    		}else{
	    			if(value < 0){
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
	document.form1.action = "UpdateNotEnQtyServlet";
   	document.form1.submit();
}

function setNotEnoughQty(){
	var len = document.form1.notEnoughQty.length;
	for(var i=0; i<len; i++){
		document.form1.notEnoughQty[i].value = 0;
	}
}
</script>
<%if (session.getAttribute("userId") == null) {%>
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

String curInRMBRate = (String)request.getAttribute("curInRMBRate");
String curInHKDRate = (String)request.getAttribute("curInHKDRate");
Double rmbRate = Double.parseDouble(curInRMBRate);
Double hkdRate = Double.parseDouble(curInHKDRate);

float amercePercent = 0;

Order order = null;
if(request.getAttribute("order") != null){
	order = (Order)request.getAttribute("order");
}
List stuDetList = (List)session.getAttribute("stuDetList");
String oprType = (String)request.getAttribute("oprType");
String period = (String)session.getAttribute("period");
String payResult = (String)request.getAttribute("payResult");
String oprStep = (String)request.getAttribute("oprStep");
List orderedBookList = (List)session.getAttribute("orderedBookList");
double amerceMount = Double.parseDouble(session.getAttribute("amerceMount").toString());

double feePercent = 0;
if(request.getAttribute("feePercent") != null){
	feePercent = Double.parseDouble(request.getAttribute("feePercent").toString());
}

double shippingFee = 0;
if(request.getAttribute("shippingFee") != null){
	shippingFee = new BigDecimal(request.getAttribute("shippingFee").toString()).multiply(new BigDecimal(0.01)).doubleValue();
}
String paidCurrency = null;
if(request.getAttribute("paidCurrency") != null){
	paidCurrency = (String)request.getAttribute("paidCurrency");
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
if(qtyList != null){
	orderSeqNo = qtyList.get(0).getOrderSeqNo();
}
Student stu = null;
Program pro = null;
if(stuDetList != null && stuDetList.size()>=3) {
	stu = (Student)stuDetList.get(0);
	pro = (Program)stuDetList.get(2);
}

double totalMop = 0.0,totalRmb = 0.0,totalWithdrawMoney = 0.0;
double bookMopSum = 0, bookRmbSum = 0; 
int totalNotEnoughQty = 0;
int totalConfirm = 0, totalWithdraw = 0;

double feeMopPrice = 0;
double feeRmbPrice = 0;

double shippingFeeMop = 0;
double shippingFeeRmb = 0;

double bookMopPrice = 0;
double bookRmbPrice = 0;

if(bookList != null && !bookList.isEmpty()){
	for(int i=0; i<bookList.size(); i++) {
		OrDetail orDetail = (OrDetail)qtyList.get(i);
		Price price = (Price)priceList.get(i);
		totalConfirm += orDetail.getConfirmQty();
		totalWithdraw += orDetail.getWithdrawQty();
		if(orDetail.getNotEnoughQty() >= 0){
			totalNotEnoughQty += orDetail.getNotEnoughQty();
		}
		
		double mopPrice = 0;
		if(price.getMopNetPrice() == 0){
			mopPrice = price.getMopFuturePrice();
		}else{
			mopPrice = price.getMopNetPrice();
		}
		double rmbPrice = 0;
		if(price.getRmbNetPrice() == 0){
			rmbPrice = price.getRmbFuturePrice();
		}else{
			rmbPrice = price.getRmbNetPrice();
		}
		
		BigDecimal v1 = new BigDecimal(Integer.toString(orDetail.getWithdrawQty()));
		BigDecimal v2 = new BigDecimal(Double.toString(price.getWithdrawPrice()));
		
		//計算退書費用
		BigDecimal v3 = v1.multiply(v2);
		
		BigDecimal v4 = new BigDecimal(Double.toString(totalWithdrawMoney));
		//總的退書費用
		
		feeMopPrice = new BigDecimal(Integer.toString(orDetail.getWithdrawQty2())).multiply(new BigDecimal(Double.toString(mopPrice))).multiply(new BigDecimal(Double.toString(feePercent))).multiply(new BigDecimal(Double.toString(0.01))).doubleValue();
		if(0<feeMopPrice && feeMopPrice<1){
			feeMopPrice = 1;
		}else{
			feeMopPrice = new BigDecimal(Double.toString(feeMopPrice)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		feeRmbPrice = new BigDecimal(Integer.toString(orDetail.getWithdrawQty2())).multiply(new BigDecimal(Double.toString(rmbPrice))).multiply(new BigDecimal(Double.toString(feePercent))).multiply(new BigDecimal(Double.toString(0.01))).doubleValue();
		if(0<feeRmbPrice && feeRmbPrice<1){
			feeRmbPrice = 1;
		}else{
			feeRmbPrice = new BigDecimal(Double.toString(feeRmbPrice)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		totalWithdrawMoney = v4.add(v3).doubleValue();
		
		BigDecimal v5 = new BigDecimal(Integer.toString(orDetail.getConfirmQty()));
		BigDecimal qty = v1.add(v5);

		BigDecimal v6 = new BigDecimal(Double.toString(mopPrice));
		BigDecimal v7 = new BigDecimal(Double.toString(rmbPrice));
		
		bookMopPrice = (v5.multiply(v6)).add(new BigDecimal(Double.toString(bookMopPrice))).doubleValue();
		bookRmbPrice = (v5.multiply(v7)).add(new BigDecimal(Double.toString(bookRmbPrice))).doubleValue();
		
		BigDecimal v8 = new BigDecimal(Double.toString(totalMop));
		BigDecimal v9 = new BigDecimal(Double.toString(totalRmb));
		//計算金額總額
		totalMop = (v5.multiply(v6)).add(v8).add(new BigDecimal(Double.toString(feeMopPrice))).doubleValue();
		totalRmb = (v5.multiply(v7)).add(v9).add(new BigDecimal(Double.toString(feeRmbPrice))).doubleValue();
		
		//計算書本確定數和退書數的總金額
		BigDecimal v10 = new BigDecimal(Double.toString(bookMopSum));
		BigDecimal v11 = new BigDecimal(Double.toString(bookRmbSum));
		
		bookMopSum = qty.multiply(v6).add(v10).doubleValue();
		bookRmbSum = qty.multiply(v7).add(v11).doubleValue();
	}
}

shippingFeeMop = new BigDecimal(Double.toString(bookMopPrice)).multiply(new BigDecimal(Double.toString(shippingFee))).doubleValue();
shippingFeeRmb = new BigDecimal(Double.toString(bookRmbPrice)).multiply(new BigDecimal(Double.toString(shippingFee))).doubleValue();
if(0<shippingFeeMop && shippingFeeMop<1){
	shippingFeeMop = 1;
}else{
	shippingFeeMop = new BigDecimal(Double.toString(shippingFeeMop)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
}
if(0<shippingFeeRmb && shippingFeeRmb<1){
	shippingFeeRmb = 1;
}else{
	shippingFeeRmb = new BigDecimal(Double.toString(shippingFeeRmb)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
}

double per = 0.0;
%>
<%
String updateResult = (String)request.getAttribute("updateResult");
if(updateResult != null ) { 
%>
<script>
	alert('<%=updateResult%>');
</script>
<%} %>
<% 
if(payResult != null  && !payResult.equals("") && oprStep.equals("step2")){ 
%>
<script language="javascript">
    alert('<%=payResult%>');
</script>
<%}%>
<%
if(payResult == null || payResult.equals("")){
	amercePercent = Float.parseFloat(request.getAttribute("amercePercent").toString());
	BigDecimal value = new BigDecimal(Double.toString(amercePercent));
	BigDecimal percent = new BigDecimal(Double.toString(0.01));
	per = Double.parseDouble((percent.multiply(value)).toString());
}
%>
<table width="650" border="0" cellpadding="0" cellspacing="0" class="table">
  <tr>
    <td align="center" width="150" height="28"><b>學號</b></td>
    <td align="center" width="100"><b>姓名</b></td>
    <td align="center" width="200"><b>課程</b></td>
    <td align="center" width="200"><b>訂單編號</b></td>
  </tr>
  <% if(stu != null && pro != null) {%>
  <tr>
    <td align="center" height="30">
    <%
    if(stu.getStudentNo() != null){
    	out.print(stu.getStudentNo());
    }else{
    	out.print(stu.getApplicantNo());
    }
    %>
    </td>
    <td align="center"><%=stu.getChineseName() %></td>
    <td align="center"><%=pro.getChineseName() %></td>
    <td align="center"><%=orderSeqNo %></td>
  </tr>
  <%} %>
</table>
<form id="form1" name="form1" method="post" target="_self" action="OrderedBookListServlet">
  <input type="hidden" name="oprType" value="bookInvoice" />
  <input type="hidden" name="studentNo" value="<%=stu.getStudentNo() %>" />
  <input type="hidden" name="applicantNo" value="<%=stu.getApplicantNo() %>" />
  <input type="hidden" name="orderSeqNo" value="<%=orderSeqNo %>" />
  <input type="hidden" name="shippingFee" value="<%=shippingFee %>"/>
  <p align="center">您已選擇購買的圖書清單如下：</p>
  <p align="center">&nbsp;</p>
  <table width="99.9%"  align="center" border="0" cellspacing="1" cellpadding="0" id="the-table">
    <tr bgcolor="#C6D6FD">
      <td width="17%" align="center"><strong>編號</strong></td>
      <td width="38%" align="center"><strong>書名</strong></td>
      <td width="5%" align="center"><strong>預估價格(MOP)</strong></td>
      <td width="5%" align="center"><strong>實際價格(MOP)</strong></td>
      <td width="4%" align="center"><strong>確認</strong></td>
	  <td width="4%" align="center"><strong>退訂</strong></td>
	  <%if(payResult != null && oprStep != null && oprStep.equals("step3")) { %>
	  <td width="5%" align="center"><strong>缺書</strong></td>
	  <%} %>
	  <td width="5%" align="center"><strong>退訂費用</strong></td>
      <td width="10%" align="center"><strong>預估購買金額(MOP)</strong></td>
      <td width="12%" align="center"><strong>實際購買金額(MOP)</strong></td>
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
        <input type="hidden" name="isbn" value="<%=book.getIsbn() %>" />
        <a href="BookDetailServlet?isbn=<%=book.getIsbn() %>" target="_blank"><%=book.getIsbn() %></a>
      </td>
      <td ><%=book.getTitle() %></td>
      <td align="left" >
        <%
        double fumopPrice = 0;
        fumopPrice = price.getMopFuturePrice();
        out.print(fumopPrice);
        %>
        <input type="hidden" name="fumopPrice" value="<%=fumopPrice %>" />
      </td>
      <td align="left" >
        <%
        double mopPrice = 0;
        if(price.getMopNetPrice() == 0 || price.getMopFuturePrice()==price.getMopNetPrice()){
        	mopPrice = price.getMopFuturePrice();
        	out.print("");
        }else{
        	mopPrice = price.getMopNetPrice();
        	out.print(price.getMopNetPrice());
        }
        %>
        <input type="hidden" name="mopPrice" value="<%=mopPrice %>" />
        <input type="hidden" name="rmbPrice" value="<%=mopPrice*rmbRate %>" />
        <input type="hidden" name="hkdPrice" value="<%=mopPrice*hkdRate %>" />
      </td>
      <!-- 
      <td align="left" >
        <%
        double rmbPrice = 0;
        if(price.getRmbNetPrice() == 0){
        	rmbPrice = price.getRmbFuturePrice();
        	out.print(price.getRmbFuturePrice());
        }else{
        	rmbPrice = price.getRmbNetPrice();
        	out.print(price.getRmbNetPrice());
        }
        %>
        <input type="hidden" name="rmbPrice" value="<%=rmbPrice %>" />
      </td>
      -->
      <td align="center" >
        <input type="hidden" name="confirmQty" value="<%=orDetail.getConfirmQty() %>" /><%=orDetail.getConfirmQty() %>
      </td>
      <td align="center" >
        <%=orDetail.getWithdrawQty() %>
      </td>
      <% 
      if(payResult != null && oprStep != null && oprStep.equals("step3")) {
    	  String notenoughqty = "";
    	  if(orDetail.getNotEnoughQty() >= 0){
    		  notenoughqty = String.valueOf(orDetail.getNotEnoughQty());
    	  }
      %>
	  <td align="center">
	    <input type="text" id="len" name="notEnoughQty" value="<%=notenoughqty %>" />
	    <input type="hidden" id="old" name="oldNotEnoughQty" value="<%=notenoughqty %>" />
	  </td>
      <%} %>
	  <td align="center" >
	    <%=(orDetail.getWithdrawQty()*price.getWithdrawPrice()) %>
	  </td>
      <td align="center" >
<%--        <%--%>
<%--        feeMopPrice = new BigDecimal(Integer.toString(orDetail.getWithdrawQty2())).multiply(new BigDecimal(Double.toString(mopPrice))).multiply(new BigDecimal(Double.toString(feePercent))).multiply(new BigDecimal(Double.toString(0.01))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();--%>
<%--    	if(0 < feeMopPrice && feeMopPrice < 1){--%>
<%--    		feeMopPrice = 1;--%>
<%--    	}--%>
<%--    	feeRmbPrice = new BigDecimal(Integer.toString(orDetail.getWithdrawQty2())).multiply(new BigDecimal(Double.toString(rmbPrice))).multiply(new BigDecimal(Double.toString(feePercent))).multiply(new BigDecimal(Double.toString(0.01))).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();--%>
<%--    	if(0 < feeRmbPrice && feeRmbPrice < 1){--%>
<%--    		feeRmbPrice = 1;--%>
<%--    	}--%>
<%--        %>  --%>
        <%=((orDetail.getConfirmQty() + orDetail.getUnconfirmQty()) * mopPrice + feeMopPrice) %>
      </td>
      <td align="center" >
<%--        <%=((orDetail.getConfirmQty() + orDetail.getUnconfirmQty()) * rmbPrice + feeRmbPrice) %>--%>
        <%=((orDetail.getConfirmQty() + orDetail.getUnconfirmQty()) * fumopPrice + feeMopPrice) %>
      </td>
    </tr>
    <%
    	}
    }
    %>
    <tr bgcolor="#C6D6FD">
      <td colspan="4" align="right">合計：</td>
      <td align="center"><%=totalConfirm %></td>
      <td align="center"><%=totalWithdraw %></td>
      <% if(payResult != null && oprStep != null && oprStep.equals("step3")) { %>
      <td align="center"><%=totalNotEnoughQty %></td>
      <%} %>
      <td align="center"><%=totalWithdrawMoney %></td>
      <td></td>

<%--      <td align="center"><%=totalRmb %></td>--%>
      <td align="center"><%=totalMop %></td>
    </tr>
    <tr bgcolor="#FF9900">
      <% if(payResult != null && oprStep != null && oprStep.equals("step3")) { %>
      <td colspan="9" align="right">罰款：</td>
      <%} else { %>
      <td colspan="8" align="right">罰款：</td>
      <%} %>
      <td align="center">
      <%
      out.print(new BigDecimal(Double.toString(amerceMount)).add(new BigDecimal(Double.toString(shippingFeeMop))).doubleValue());
	  %>
	  </td>
<%--	  <td align="center">--%>
<%--	  <%--%>
<%--	  if(paidCurrency == null){--%>
<%--		  out.print(new BigDecimal(Double.toString(amerceMount)).add(new BigDecimal(Double.toString(shippingFeeRmb))).doubleValue());--%>
<%--	  }else{--%>
<%--		  if("RMB".equals(paidCurrency)){--%>
<%--			  out.print(new BigDecimal(Double.toString(amerceMount)).add(new BigDecimal(Double.toString(shippingFeeRmb))).doubleValue());--%>
<%--	      }else{--%>
<%--	    	  out.print(0.0);--%>
<%--	      }--%>
<%--	  }--%>
<%--	  %>--%>
<%--	  </td>--%>
	</tr>
	<tr bgcolor="#FF99FF">
	  <% if(payResult != null && oprStep != null && oprStep.equals("step3")) { %>
	  <td colspan="9" align="right">逾期付款罰款：</td>
	  <%} else { %>
	  <td colspan="8" align="right">逾期付款罰款：</td>
	  <%} %>
	  <td align="center">
	  <%
	  //計算逾期付款罰款，罰款小於1作1計算，大於1的進行四捨五入計算
	  BigDecimal value1 = new BigDecimal(Double.toString(bookMopSum));
	  BigDecimal value2 = new BigDecimal(Double.toString(bookRmbSum));
	  
	  BigDecimal percent = new BigDecimal(Double.toString(per));
	  double sum1 = 0;
	  double sum2 = 0;
	  if(percent.doubleValue() != 0){
		  if(0 < (value1.multiply(percent)).doubleValue() && (value1.multiply(percent)).doubleValue() < 1){
			  sum1 = 1;
		  }else{
			  sum1 = (value1.multiply(percent)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		  }
		  if(0 < (value2.multiply(percent)).doubleValue() && (value2.multiply(percent)).doubleValue() < 1){
			  sum2 = 1;
		  }else{
			  sum2 = (value2.multiply(percent)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		  }
	  }
	  
	  if(order != null){
		  if(order.getPaidStatus().equals("Y")){
			  sum1 = order.getFineforlatepay();
		  }
	  }
	  %>
	    <%=sum1 %>
	    <input type="hidden" name="amerceMop" value="<%=sum1 %>"/>
	    <input type="hidden" name="amerceRmb" value="<%=new BigDecimal(sum1*rmbRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue() %>"/>
	    <input type="hidden" name="amerceHkd" value="<%=new BigDecimal(sum1*hkdRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue() %>"/>
	  </td>
<%--	  <td align="center">--%>
<%--	    <%=sum2 %>--%>
<%--	    <input type="hidden" name="amerceRmb" value="<%=sum2 %>"/>--%>
<%--	  </td>--%>
	</tr>
	<tr bgcolor="#CCCC33">
	  <% if(payResult != null && oprStep != null && oprStep.equals("step3")) { %>
	  <td colspan="9" align="right">總計：</td>
	  <%} else { %>
	  <td colspan="8" align="right">總計：</td>
	  <%} %>
	  <%
	  BigDecimal n1 = new BigDecimal(totalMop);
	  BigDecimal n2 = new BigDecimal(totalWithdrawMoney);
	  BigDecimal n3 = new BigDecimal(amerceMount);
	  BigDecimal n4 = new BigDecimal(sum1);
	  BigDecimal n5 = new BigDecimal(totalRmb);
	  BigDecimal n6 = new BigDecimal(sum2);
	  double allMOP = n1.add(n2).add(n3).add(n4).add(new BigDecimal(Double.toString(shippingFeeMop))).doubleValue();
	  double allRMB = n5.add(n2).add(n3).add(n6).add(new BigDecimal(Double.toString(shippingFeeRmb))).doubleValue();
	  %>
	  <td align="center">
	    <input type="hidden" name="shipFineMop" value="<%=shippingFeeMop %>"/>
	    <input type="hidden" name="totalMop" value="<%=allMOP %>"  />
	    <%
	    out.print(allMOP);
	    %>
	  </td>
<%--	  <td align="center">--%>
<%--	    <input type="hidden" name="shipFineRmb" value="<%=shippingFeeRmb %>"/>--%>
<%--	    <input type="hidden" name="totalRmb" value="<%=allRMB %>"  />--%>
<%--	    <input type="hidden" name="shipFineRmb" value="<%=shippingFeeMop*rmbRate%>"/>--%>
<%--	    <input type="hidden" name="totalRmb" value="<%=allMOP*rmbRate %>"  />--%>
<%--	    <%--%>
<%--	    if(paidCurrency == null){--%>
<%--	    	out.print(allRMB);--%>
<%--	    }else{--%>
<%--	    	if("RMB".equals(paidCurrency)){--%>
<%--	    		out.print(allRMB);--%>
<%--	    	}else{--%>
<%--	    		out.print(0.0);--%>
<%--	    	}--%>
<%--	    }--%>
<%--	    %>--%>

<%--	  </td>--%>
	</tr>
  </table>
  
  <% if(bookList != null && !bookList.isEmpty() && payResult == null && oprStep != null && oprStep.equals("step2") ) { %>
  <p align="center">
  <table width="200" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td colspan="2" height="35" align="left">請選擇貨幣:</td>
    </tr>
    <tr>
      <td width="100">
        <input name="paidCurrency" id="mop" type="radio" value="MOP" class="radio"/>
        <label>澳門幣MOP</label>
      </td>
      <td width="100" align="center">
        <label><%=allMOP %></label>
      </td>
    </tr>
    <tr>
      <td>
        <input name="paidCurrency" id="rmb" type="radio" value="RMB" class="radio"/>
        <label>人民幣RMB</label>
      </td>
      <td align="center">
        <label><%=new BigDecimal(allMOP*rmbRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue() %></label>
        <input type="hidden" name="totalRmb" value="<%=new BigDecimal(allMOP*rmbRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue() %>" />
        <input type="hidden" name="shipFineRmb" value="<%=new BigDecimal(shippingFeeMop*rmbRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue() %>"/>
      </td>
    </tr>
    <tr>
      <td>
        <input name="paidCurrency" id="hkd" type="radio" value="HKD" class="radio"/>
        <label>港幣HKD</label>
      </td>
      <td align="center">
        <label><%=new BigDecimal(allMOP*hkdRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue() %></label>
        <input type="hidden" name="totalHkd" value="<%=new BigDecimal(allMOP*hkdRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue() %>"/>
        <input type="hidden" name="shipFineHkd" value="<%=new BigDecimal(shippingFeeMop*hkdRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue() %>"/>
      </td>
    </tr>
  </table>
  </p>
  <p align="center">
    <input type="button" name="Submit2" id="payButton" value="確認繳費" onclick="pay();" />
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="reset" name="Submit3" value="重 設" /> 
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" name="Submit" value="返 回" onclick="window.location.href='orderbookindex.jsp?oprType=received'"  />
  </p>
  <%
  }
  %>
  <%if(bookList != null && !bookList.isEmpty() && payResult != null && oprStep != null && oprStep.equals("step3")) {  %>
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
    <input type="button" name="Submit" value="返 回" onclick="window.location.href='orderbookindex.jsp?oprType=received'"/>
  </p>
  <%} if(bookList != null && !bookList.isEmpty() && ( payResult != null && oprStep != null && oprStep.equals("step2")) || (payResult == null && oprStep != null && oprStep.equals("step3"))) {  %>
  <br/>
  <br/>
  <br/>
  <p align = "center">
    <input type="button" name="Submit" value="返 回" onclick="window.location.href='orderbookindex.jsp?oprType=received'"/>
  </p>
  <% }  if(bookList == null || bookList.size() == 0) { %>
  <br/>
  <br/>
  <br/>
  <br/>
  <h5 align="center">該學員本學期尚未訂購任何圖書！</h5>
  <br/>
  <br/>
  <p align = "center"> <input type="button" name="Submit" value="返 回" onclick="window.location.href='orderbookindex.jsp?oprType=received'"/>
    &nbsp;
  </p>
  <% } %> 
</form>
</body>
</html>

