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
<title>確認購買圖書清單</title>
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
function orderbook(){
	window.parent.main.location.href = "orderbook.html";
}
function unconfirm() {
	window.parent.toppart.location.href = "orderbook3.html";
	window.parent.bottom.location.href = "orderbooklist3.html";
}
function bookInvoice() {
    window.location.href = "OrderedBookListServlet?oprType=bookInvoice";
}
//返回訂購頁面
function  backOrderBook() {
	window.location.href = "orderbookindex.jsp?oprType=orderBooks" ;
}
//返回訂購查詢頁面
function  backSearchOrderBook() {
	window.location.href = "orderbookindex.jsp?oprType=search" ;
}
function printReceipt(){
	document.otherForm.target = "_blank";
	document.otherForm.action = "ViewReceiptServlet";
	document.otherForm.submit();
}
function payRetail(){
	var orderSeqNo = document.otherForm.orderSeqNo.value;
	window.location.href = "ConfirmOrderBookServlet?oprStep=step2&oprType=retailBooks&orderSeqNo="+orderSeqNo;
}
function receiveBook(){
	var orderSeqNo = document.otherForm.orderSeqNo.value;
	$.post(
		"ConfirmOrderBookServlet",
		{
			oprType: "checkRetailOrder",
			orderSeqNo: orderSeqNo
		},
		function(result){
			if(result == "N"){
				alert("該圖書訂單並未付款，不能進行此操作！");
				return false;
			}else{
				window.location.href = "ConfirmOrderBookServlet?searchType=receive&oprType=retailBooks&oprStep=step3&orderSeqNo="+orderSeqNo;
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
  
List stuDetList = null;
Student stu = null;
Program pro = null;
String studentNo = null;
String applicantNo = null;
int orderSeqNo = 0;

String curInRMBRate = (String)request.getAttribute("curInRMBRate");
String curInHKDRate = (String)request.getAttribute("curInHKDRate");

Double rmbRate = Double.parseDouble(curInRMBRate);
Double hkdRate = Double.parseDouble(curInHKDRate);

if(session.getAttribute("stuDetList") != null){
	stuDetList = (List)session.getAttribute("stuDetList");
	stu = (Student)stuDetList.get(0);
	pro = (Program)stuDetList.get(2);
}
if(stu != null){
	studentNo = stu.getStudentNo();
	applicantNo = stu.getApplicantNo();
}
String oprType = null;
if(request.getAttribute("oprType") != null){
	oprType = (String)request.getAttribute("oprType");
}
List orderedBookList = null;
if(session.getAttribute("orderedBookList") != null){
	orderedBookList = (List)session.getAttribute("orderedBookList");
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

double totalMop = 0.0, totalRmb = 0.0, totalMopFu = 0.0, totalHkd = 0.0;
for(int i=0; i<bookList.size(); i++) {
	OrDetail orDetail = (OrDetail)qtyList.get(i);
  	Price price = (Price)priceList.get(i);
  	BigDecimal v1 = new BigDecimal(Integer.toString(orDetail.getConfirmQty()));
  	BigDecimal v2 = new BigDecimal(Integer.toString(orDetail.getUnconfirmQty()));
  	
  	BigDecimal v3 = v1.add(v2);
  	
  	double mopPrice = 0;
  	double fumopPrice = 0;
  	fumopPrice = price.getMopFuturePrice();
	if(price.getMopNetPrice() == 0){
		mopPrice = fumopPrice;
	}else{
		mopPrice = price.getMopNetPrice();
	}
	double rmbPrice = 0;
	if(price.getRmbNetPrice() == 0){
		rmbPrice = price.getRmbFuturePrice();
	}else{
		rmbPrice = price.getRmbNetPrice();
	}
  	
  	BigDecimal v4 = new BigDecimal(Double.toString(mopPrice));
  	BigDecimal v5 = new BigDecimal(Double.toString(rmbPrice));
  	BigDecimal bd = new BigDecimal(Double.toString(fumopPrice));
  	
  	BigDecimal v6 = v3.multiply(v4);
  	BigDecimal v7 = v3.multiply(v5);
  	BigDecimal bd2 = v3.multiply(bd);
  	
  	BigDecimal v8 = new BigDecimal(Double.toString(totalMop));
  	BigDecimal v9 = new BigDecimal(Double.toString(totalRmb));
  	BigDecimal bd3 = new BigDecimal(Double.toString(totalMopFu));
  	
  	totalMop = v8.add(v6).doubleValue();
  	//totalRmb = v9.add(v7).doubleValue();
  	totalMopFu = bd3.add(bd2).doubleValue();
  	
  	totalRmb = new BigDecimal(totalMop*rmbRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
	totalHkd = new BigDecimal(totalMop*hkdRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
}
%>
<table width="405" border="0" cellpadding="0" cellspacing="0"  class="table">
  <tr>
    <td align="center" width="155" height="28"><strong>學號</strong></td>
    <td align="center" width="50"><strong>姓名</strong></td>
    <td align="center" width="200"><strong>課程</strong></td>
  </tr>
  <tr>
    <td><%if(stu.getStudentNo() != null){out.print(stu.getStudentNo());}else{out.print(stu.getApplicantNo());} %></td>
    <td><%if(stu.getChineseName() != null){out.print(stu.getChineseName());}else{out.print(stu.getEnglishName());} %></td>
    <td><%=pro.getChineseName() %></td>
  </tr>
</table>
<form id="form1" name="form1" method="post" target="_blank" action="OrderedBookListServlet">
  <input type="hidden" name="oprType" value="bookInvoice" />
  <input type="hidden" name="studentNo" value="<%=studentNo %>" />
  <input type="hidden" name="applicantNo" value="<%=applicantNo %>" />
  <input type="hidden" name="orderSeqNo" value="<%=orderSeqNo %>"/>
  <p align="center" class="blackBold14px">您已選擇購買的圖書清單如下：</p>
  <p align="center" class="style1">&nbsp;</p>
  <table width="99.9%" align="center" border="0" cellspacing="1" cellpadding="0" id="the-table">
    <tr bgcolor="#C6D6FD">
      <td width="15%" align="center"><strong>編號</strong></td>
      <td width="50%" align="center"><strong>書名</strong></td>
      <td width="5%" align="center"><strong>預估價格（MOP）</strong></td>
      <td width="5%" align="center"><strong>實際價格（MOP）</strong></td>
      <td width="5%" align="center"><strong>確認</strong></td>
      <td width="10%" align="center"><strong>購買預估總金額(MOP)</strong></td>
      <td width="10%" align="center"><strong>購買實際總金額(MOP)</strong></td>
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
      	  double fumopPrice = 0;
      	  fumopPrice = price.getMopFuturePrice();
    	  out.print(fumopPrice);
    
      %>
      </td>
      <td align="left" width="5%">
<%--      <%--%>
<%--      double rmbPrice = 0;--%>
<%--      if(price.getRmbNetPrice() == 0){--%>
<%--    	  rmbPrice = price.getRmbFuturePrice();--%>
<%--    	  out.print(price.getRmbFuturePrice());--%>
<%--      }else{--%>
<%--    	  rmbPrice = price.getRmbNetPrice();--%>
<%--    	  out.print(price.getRmbNetPrice());--%>
<%--      }--%>
<%--      %>--%>
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
      </td>
      <td align="center" width="5%">
        <input name="confirmQty" type="text" value="<%=orDetail.getConfirmQty() %>" id="textlen2" size="5" readonly="readonly" />
      </td>
      <td align="center" width="10%">
        <input name="simpleTotal" type="text" id="textlen"  value="<%=(orDetail.getConfirmQty()+orDetail.getUnconfirmQty())*fumopPrice %>" size="15" readonly="readonly" />
      </td>
      <td align="center" width="10%">
<%--        <input name="simpleTotal" type="text" id="textlen"  value="<%=(orDetail.getConfirmQty()+orDetail.getUnconfirmQty())*rmbPrice %>" size="15" readonly="readonly"/>--%>
        <input name="simpleTotal" type="text" id="textlen"  value="<%=(orDetail.getConfirmQty()+orDetail.getUnconfirmQty())*mopPrice %>" size="15" readonly="readonly"/>
      </td>
    </tr>
    <%
    	}
    }
	%>
	<tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
	  <td colspan="5" align="right">合計（MOP）：</td>
	  <td align="center">
	    <input type="text" id="textlen" name="totalMop" value="<%=totalMopFu %>" readonly="readonly"/>
	  </td>
	  <td align="center">
<%--	    <input type="text" id="textlen" name="totalRmb" value="<%=totalRmb %>" readonly="readonly"/>--%>
	    <input type="text" id="textlen" name="totalMop" value="<%=totalMop %>" readonly="readonly"/>
	  </td>
	</tr>
	<tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
	  <td colspan="5" align="right">對應合計（RMB）：</td>
	  <td align="center">
	    <input type="text" id="textlen" name="totalRmb" value="<%=totalRmb %>" readonly="readonly"/>
	  </td>
	  <td align="center">
<%--	    <input type="text" id="textlen" name="totalRmb" value="<%=totalRmb %>" readonly="readonly"/>--%>
	    <input type="text" id="textlen" name="totalRmb" value="<%=totalRmb %>" readonly="readonly"/>
	  </td>
	</tr>
	<tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
	  <td colspan="5" align="right">對應合計（HKD）：</td>
	  <td align="center">
	    <input type="text" id="textlen" name="totalHKD" value="<%=totalHkd %>" readonly="readonly"/>
	  </td>
	  <td align="center">
<%--	    <input type="text" id="textlen" name="totalRmb" value="<%=totalRmb %>" readonly="readonly"/>--%>
	  	<input type="text" id="textlen" name="totalHKD" value="<%=totalHkd %>" readonly="readonly"/>
	  </td>
	</tr>
  </table>
  <p align="center" class="style1">&nbsp;</p>
  <p align="center" class="style1">
  <% if(oprType != null && "search".equals(oprType)) { %>
    <input type="button" name="back" value="返 回" onclick="backSearchOrderBook();"  />
  <%} else { %>
    <input type="button" name="back" value="返 回" onclick="backOrderBook();" />
  <%} %>
    <label>
    <%
    if(bookList != null && !bookList.isEmpty()) {
    %>
      <input type="submit" name="Submit" value="列印發票" />
    <%
    }
    %>
    </label>
  </p>
</form>
<form action="" method="post" name="otherForm">
  <input type="hidden" name="type" value="retailReceipt" />
  <input type="hidden" name="oprType" value="retailBooks"/>
  <input type="hidden" name="orderSeqNo" value="<%=orderSeqNo %>"/>
</form>
</body>
</html>

