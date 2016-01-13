<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*,java.text.*,edu.must.tos.bean.*" errorPage="" %>
<%@ page import="java.math.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列印發票</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
#len {
	width: 20px;
}
.style2 {
	font-size: 12px
}
.style5 {
	font-size: 16px
}
.style7 {
	font-size: 18px
}
-->
</style>
</head>
<%

String curInRMBRate = (String)request.getAttribute("curInRMBRate");
String curInHKDRate = (String)request.getAttribute("curInHKDRate");
Double rmbRate = Double.parseDouble(curInRMBRate);
Double hkdRate = Double.parseDouble(curInHKDRate);

List stuDetList = null;
if(request.getAttribute("stuDetList")!=null){
	stuDetList = (List)request.getAttribute("stuDetList");
}

int orderSeqNo = 0;
if(request.getAttribute("orderSeqNo")!=null){
	orderSeqNo = Integer.parseInt(request.getAttribute("orderSeqNo").toString());
}

String curIntake = null;
if(request.getAttribute("curIntake")!=null){
	curIntake = (String)request.getAttribute("curIntake");
}

List orderedBookList = null;
if(request.getAttribute("orderedBookList")!=null){
	orderedBookList = (List)request.getAttribute("orderedBookList");
}
List<Book> bookList = null;
List<Price> priceList = null;
List<OrDetail> qtyList = null;

if(orderedBookList != null) {
	bookList = (List)orderedBookList.get(0);
  	priceList = (List)orderedBookList.get(1);
  	qtyList = (List)orderedBookList.get(2); 
}
Student stu = null;
Program pro = null;
if(stuDetList != null) {
   	stu = (Student)stuDetList.get(0);
   	pro = (Program)stuDetList.get(2);
}
double totalMop = 0.0, totalRmb = 0.0, totalHkd=0.0;
int totalConfirm = 0, totalWithdraw = 0, totalBooks = 0;
for(int i=0; i<bookList.size(); i++) {
	OrDetail orDetail = (OrDetail)qtyList.get(i);
	Price price = (Price)priceList.get(i);
	totalConfirm += orDetail.getConfirmQty();
	totalWithdraw += orDetail.getWithdrawQty();
	
	BigDecimal v1 = new BigDecimal(Integer.toString(orDetail.getConfirmQty()));
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
	BigDecimal v2 = new BigDecimal(Double.toString(mopPrice));
	BigDecimal v3 = new BigDecimal(Double.toString(rmbPrice));
	
	BigDecimal v4 = v1.multiply(v2);
	BigDecimal v5 = v1.multiply(v3);
	
	BigDecimal v6 = new BigDecimal(Double.toString(totalMop));
	BigDecimal v7 = new BigDecimal(Double.toString(totalRmb));
	
	totalMop = v6.add(v4).doubleValue();
	//totalRmb = v7.add(v5).doubleValue();
	totalRmb = new BigDecimal(totalMop*rmbRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
	totalHkd = new BigDecimal(totalMop*hkdRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
	
}
totalBooks = totalConfirm ;
Date now = new Date(); 
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //define the print of the date
String dateFormate = format.format(now); 
//String term = dateFormate.substring(0,2)+curIntake.substring(0,2)+"/"+dateFormate.substring(0,2)+String.valueOf(Integer.parseInt(curIntake.substring(0,2))+1);
String termVal = "";
if(request.getAttribute("term") != null){
	termVal = (String)request.getAttribute("term");
}
%>
<body>
<p align="center">
  <span class="style5">
    <span class="style7">澳門科技大學<%=termVal %>訂書單</span>
  </span>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <span class="style2">訂單編號</span>:<%=orderSeqNo %>
</p>
<table width="100%" border="0" align="center">
  <tr>
    <td align="right" width="8%">學號：</td>
    <td width="26%"><%if(stu.getStudentNo() != null){out.print(stu.getStudentNo());}else{out.print(stu.getApplicantNo());} %></td>
    <td align="right" width="13%">學生姓名：</td>
    <td width="15%"><%if(stu.getChineseName() != null){out.print(stu.getChineseName());}else{out.print(stu.getEnglishName());} %></td>
    <td align="right" width="13%">列印日期：</td>
    <td width="25%"><%=dateFormate %></td>
  </tr>
</table>
<br />
<table width="100%" border="0">
  <tr>
    <td>
      <table width="100%" border="1" align="center" cellspacing="0" bordercolor="#330000" >
        <tr>
          <td width="35%" align="center">書名</td>
          <td width="10%" align="center">作者</td>
          <td width="25%" align="center">出版商</td>
          <td width="15%" align="center">ISBN</td>
          <td width="5%" align="center">預估價MOP</td>
          <td width="5%" align="center">實際價MOP</td>
          <td width="5%" align="center">確定訂書數</td>
        </tr>
        <%
        if(bookList != null && priceList != null && qtyList != null ){
        	for(int i=0; i<bookList.size(); i++){
        		Book book = (Book)bookList.get(i);
        		OrDetail orDetail = (OrDetail)qtyList.get(i);
        		Price price = (Price)priceList.get(i);
        		%>
	    <tr>
	      <td><%=book.getTitle() %></td>
	      <td><%=book.getAuthor() %></td>
	      <td><%=book.getPublisher() %></td>
	      <td><%=book.getIsbn() %></td>
	      <td>
	      <%

	    	  out.print(price.getMopFuturePrice());
	    
	      %>
	      </td>
	      <td>
<%--	      <%--%>
<%--	      if(price.getRmbNetPrice() == 0){--%>
<%--	    	  out.print(price.getRmbFuturePrice());--%>
<%--	      }else{--%>
<%--	    	  out.print(price.getRmbNetPrice());--%>
<%--	      }--%>
<%--	      %>--%>
		  <%
	      if(price.getMopNetPrice() == 0 || price.getMopFuturePrice()==price.getMopNetPrice()){
	    	  out.print("");
	      }else{
	    	  out.print(price.getMopNetPrice());
	      }
	      %>
	      </td>
	      <td><%=orDetail.getConfirmQty() %></td>
	    </tr>
	    <% 
			} 
		}
        %>
      </table>
      <table width="100%" align="center">
        <tr>
          <td width="35%">&nbsp;</td>
          <td width="10%">&nbsp;</td>
          <td width="25%">&nbsp;</td>
          <td width="15%">&nbsp;</td>
          <td width="1%">&nbsp;</td>
          <td width="9%" align="right">小計:</td>
          <td width="5%" align="left" ><%=totalConfirm %></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>
      <table width="100%" border="0">
        <tr>
          <td align="right">&nbsp;</td>
          <td align="right">&nbsp;</td>
          <td align="right">&nbsp;</td>
          <td align="right">&nbsp;</td>
          <td align="right">總冊數():</td>
          <td align="left"><%=totalBooks %></td>
          <td>實際總金額（MOP）:</td>
          <td><%=totalMop %></td>
        </tr>
        <tr>
          <td width="110" align="right">&nbsp;</td>
          <td width="110" align="right">&nbsp;</td>
          <td width="238" align="right">&nbsp;</td>
          <td width="238" align="right">&nbsp;</td>
          <td width="203" align="right">&nbsp;</td>
          <td width="103" align="right">&nbsp;</td>
          <td width="160">對應總金額（RMB）:</td>
<%--          <td width="104"><%=totalRmb %></td>--%>
          <td width="104"><%=totalRmb %></td>
        </tr>
        <tr>
          <td width="110" align="right">&nbsp;</td>
          <td width="110" align="right">&nbsp;</td>
          <td width="238" align="right">&nbsp;</td>
          <td width="238" align="right">&nbsp;</td>
          <td width="203" align="right">&nbsp;</td>
          <td width="103" align="right">&nbsp;</td>
          <td width="160">對應總金額（HKD）:</td>
          <td width="104"><%=totalHkd %></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br />
<br />
<br />
<table width="100%" border="0" align="center">
  <tr>
    <td width="12%" align="right">學生簽名</td>
    <td width="23%">&nbsp;</td>
    <td width="26%" align="right">中心蓋章</td>
    <td width="35%">&nbsp;</td>
    <td width="4%">&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="center" valign="top"><hr /></td>
    <td>&nbsp;</td>
    <td align="center" valign="top">
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td align="center" valign="top"><hr /></td>
        </tr>
        <tr>
          <td>[澳門科技大學圖書出版及供應中心蓋章]</td>
        </tr>
      </table>
    </td>
    <td>&nbsp;</td>
  </tr>
</table>
<br />
<br />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td colspan="2" align="left">注意事項：</td>
  </tr>
  <tr>
    <td>1.</td>
    <td>本次訂書時同學必須從訂書系統內選擇付款時間和領書時間，同學必按照自己預約的時間前往付款和領書;若有同學屆時</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>未能按規定時間付款和領書，中心將按照『訂書、繳交書費及領書須知』中的有關規定處以罰金。</td>
  </tr>
  <tr>
    <td>2.</td>
    <td>此次訂書期間提供之書價為預估價，中心先按預估價收取書費。如有需要，將會於同學取書時根據教材實際價格及匯率作出多退少補處理。</td>
  </tr>
  <tr>
    <td>3.</td>
    <td>有關圖書出版及供應之最新通告，請同學們隨時查看大學網站圖書出版及供應中心公告欄</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>網址：http://www.must.edu.mo/1200C3.jsp</td>
  </tr>
  <tr>
    <td>4.</td>
    <td>付款後請妥善保管好收據，開學領書時必須憑已付款收據按時領書。</td>
  </tr>
</table>
<br />
<br />
<br />
<hr />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="10%" align="right" height="20">學號：</td>
    <td width="30%" align="left"><%if(stu.getStudentNo()!=null){out.print(stu.getStudentNo());}else{out.print(stu.getApplicantNo());} %></td>
    <td width="5%" align="left">&nbsp;</td>
    <td width="10%" align="right">訂單編號：</td>
    <td width="10%" align="left"><%=orderSeqNo %></td>
    <td width="10%" align="right">列印日期:</td>
    <td width="25%" align="left"><%=dateFormate %></td>
  </tr>
  <tr>
    <td align="right" height="20">學生姓名：</td>
    <td align="left"><%=stu.getChineseName() %></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td rowspan="2" align="right">確定訂數：</td>
    <td rowspan="2" align="left"><%=totalConfirm %></td>
    <td colspan="2" rowspan="2" align="center">學生簽名：</td>
    <td colspan="3" align="center" valign="bottom">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="3" align="center" valign="top"><hr /></td>
  </tr>
  <tr>
    <td align="right" height="20">總冊數：</td>
    <td align="left"><%=totalBooks %></td>
    <td colspan="2" align="center">假期可聯系電話：</td>
    <td colspan="3">&nbsp;</td>
  </tr>
  <tr>
    <td align="right" height="20">&nbsp;</td>
    <td align="left">&nbsp;</td>
    <td colspan="2" align="center">假期可聯系Email:</td>
    <td colspan="3">&nbsp;</td>
  </tr>
</table>
</body>
</html>