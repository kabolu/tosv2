<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%@ page import="java.math.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>確認購買零售圖書清單</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/ext-all.css" />
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<style type="text/css">
<!--
#textlen {
	height: 18px;
	width: 60px;
}
#textlen2 {
	height: 18px;
	width: 30px;
}
-->
</style>
<script language="javascript">
function printReceipt(){
	document.otherForm.target = "_blank";
	document.otherForm.action = "RetailBookServlet";
	document.otherForm.submit();
}
function payRetail(){
	var orderSeqNo = document.otherForm.orderSeqNo.value;
	window.location.href = "RetailBookServlet?type=payOrderPage&orderSeqNo="+orderSeqNo;
}
function receiveBook(){
	var orderSeqNo = document.otherForm.orderSeqNo.value;
	$.post(
		"RetailBookServlet",
		{
			type: "checkRetailOrder",
			orderSeqNo: orderSeqNo
		},
		function(result){
			if(result == "N"){
				alert("該圖書訂單並未付款，不能進行此操作！");
				return false;
			}else{
				window.location.href = "RetailBookServlet?type=receiveBookPage&orderSeqNo="+orderSeqNo;
			}
		}
	)
}
</script>
<script language="javascript">
document.onkeydown = function() {
	if(event.keyCode == 116) {
		event.keyCode = 0;
		event.returnValue = false;
	}
}
document.oncontextmenu = function() {event.returnValue = false;}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
session.removeAttribute("selectedBookList");
int orderSeqNo = 0;
String retail = null;
if(request.getAttribute("retail") != null){
	retail = (String)request.getAttribute("retail");
}
if(retail != null && "RETAIL".equals(retail)){
	orderSeqNo = Integer.parseInt(request.getAttribute("orderSeqNo").toString());
}
String oprType = null;
if(request.getAttribute("oprType") != null){
	oprType = (String)request.getAttribute("oprType");
}
List orderedBookList = null;
if(session.getAttribute("orderedBookList") != null){
	orderedBookList = (List)session.getAttribute("orderedBookList");
}
Double hkdRate = null;
if(request.getAttribute("hkdRate") != null){
	hkdRate = Double.parseDouble(request.getAttribute("hkdRate").toString());
}
Double rmbRate = null;
if(request.getAttribute("rmbRate") != null){
	rmbRate = Double.parseDouble(request.getAttribute("rmbRate").toString());
}
List<Book> bookList = null;
List<Price> priceList = null;
List<OrDetail> qtyList = null;
if(orderedBookList != null) {
	bookList = (List)orderedBookList.get(0);
    priceList = (List)orderedBookList.get(1);
    qtyList = (List)orderedBookList.get(2); 
    orderSeqNo = (int)qtyList.get(0).getOrderSeqNo();
}

double totalMop = 0.0, totalRmb = 0.0, totalHkd = 0.0;
for(int i=0; i<bookList.size(); i++) {
	OrDetail orDetail = (OrDetail)qtyList.get(i);
  	Price price = (Price)priceList.get(i);
  	BigDecimal v1 = new BigDecimal(Integer.toString(orDetail.getConfirmQty()));
  	BigDecimal v2 = new BigDecimal(Integer.toString(orDetail.getUnconfirmQty()));
  	
  	BigDecimal v3 = v1.add(v2);
  	
  	double mopPrice = 0;
	if(price.getMopNetPrice() == 0){
		mopPrice = price.getMopFuturePrice();
	}else{
		mopPrice = price.getMopNetPrice();
	}
  	
  	BigDecimal v4 = new BigDecimal(Double.toString(mopPrice));
  	
  	BigDecimal v5 = v3.multiply(v4);
  	
  	BigDecimal v6 = new BigDecimal(Double.toString(totalMop));
  	
  	totalMop = v6.add(v5).doubleValue();
}
if(hkdRate != null){
	totalHkd = totalMop * hkdRate;
}
if(rmbRate != null){
	totalRmb = totalMop * rmbRate;
}
%>
<table width="405" border="0" cellpadding="0" cellspacing="0"  class="table">
  <%
  if(retail != null && "RETAIL".equals(retail)){
	  %>
	  <tr>
	   <td align="left" height="28" class="blackBold14px"><strong><b>&nbsp;&nbsp;零售書單</b></strong></td>
	   <td align="left">訂單編號為：<%=orderSeqNo %></td>
	  </tr>
	  <%
  }
  %>
</table>
<form id="form1" name="form1" method="post" target="_blank" action="OrderedBookListServlet">
<input type="hidden" name="oprType" value="bookInvoice" />
<input type="hidden" name="orderSeqNo" value="<%=orderSeqNo %>"/>
  <p align="center" class="style1">您已選擇購買的圖書清單如下：</p>
  <p align="center" class="style1">&nbsp;</p>
  <table width="99.9%"  align="center" border="0" cellspacing="1" cellpadding="0" id="the-table">
    <tr bgcolor="#C6D6FD">
      <td width="15%" align="center"><strong>編號</strong></td>
      <td width="50%" align="center"><strong>書名</strong></td>
      <td width="5%" align="center"><strong>預估價格（MOP）</strong></td>
      <td width="5%" align="center"><strong>實價價格（MOP）</strong></td>
      <td width="5%" align="center"><strong>確認</strong></td>
      <td width="10%" align="center"><strong>總金額(MOP)</strong></td>
    </tr>
    <%
    if(bookList != null && priceList != null && qtyList != null) {
    	for(int i=0; i<bookList.size(); i++){
    		Book book = (Book)bookList.get(i);
    		OrDetail orDetail = (OrDetail)qtyList.get(i);
    		Price price = (Price)priceList.get(i);
    		%>
    		<tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    		 <td width="15%"><a href="BookDetailServlet?isbn=<%=book.getIsbn() %>" target="_blank"><%=book.getIsbn() %></a></td>
    		 <td width="50%"><%=book.getTitle() %></td> 
    		 <td align="left" width="5%">
    		 <%
    		 double mopPrice = 0;
    		 if(price.getMopNetPrice() == 0){
    			 mopPrice = price.getMopFuturePrice();
    		 } else {
    			 mopPrice = price.getMopNetPrice();
    		 }
    		 out.print(price.getMopFuturePrice());
    		 %>
    		 </td>
    		 <td align="left" width="5%">
    		 <%
    		 if(price.getMopNetPrice() != 0 && (price.getMopFuturePrice()-price.getMopNetPrice()) != 0){
    			 out.print(price.getRmbFuturePrice());
    		 }
    		 %>
    		 </td>
    		 <td align="center" width="5%">
    		 <input name="confirmQty" type="text" value="<%=orDetail.getConfirmQty() %>" id="textlen2" size="5" readonly="readonly" />
    		 </td>
    		 <td align="center" width="10%">
    		 <input name="simpleTotal" type="text" id="textlen"  value="<%=(orDetail.getConfirmQty()+orDetail.getUnconfirmQty())*mopPrice %>" size="15" readonly="readonly" />
    		 </td>
    		</tr>
    		<%
        }	
    }
	%>
   <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td colspan="5" align="right">葡紙MOP合計：</td>
    <td align="center">
      <input type="text" id="textlen" name="totalMop" value="<%=totalMop %>" readonly="readonly"/>
    </td>
   </tr>
   <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td align="right">港紙HKD合計：</td>
    <td align="center">
      <input type="text" id="textlen" name="totalMop" value="<%=totalHkd %>" readonly="readonly"/>
    </td>
   </tr>
   <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td align="right">人民幣RMB合計：</td>
    <td align="center">
      <input type="text" id="textlen" name="totalMop" value="<%=totalRmb %>" readonly="readonly"/>
    </td>
   </tr>
  </table>
  <p align="center" class="style1">&nbsp;</p>
  <p align="center" class="style1">
    <label>
    <%
    if(bookList != null && !bookList.isEmpty()) {
    	if(retail != null && "RETAIL".equals(retail)){
    		%>
    		<input type="button" name="payButton" value="收費" onclick="payRetail();"/>
    		&nbsp;
    		<input type="button" name="receiptButton" value="列印收據" onclick="printReceipt();"/>
    		&nbsp;
    		<input type="button" name="receiveButton" value="領書" onclick="receiveBook();"/>
    		<%
    	}
    } 
    %>
    </label>
  </p>
</form>
<form action="" method="post" name="otherForm">
<input type="hidden" name="type" value="retailReceipt" />
<input type="hidden" name="orderSeqNo" value="<%=orderSeqNo %>"/>
</form>
</body>
</html>

