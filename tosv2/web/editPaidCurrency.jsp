<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%@page import="java.math.BigDecimal"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>更改付款幣種</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
<script language="javascript">
function doChange() {
	var orgPaidCurrency = document.getElementById("orgPaidCurrency").value;
	var paidCurrency = "";
	var currency = document.getElementsByName("paidCurrency");
	for(i=0; i<currency.length; i++){
		if(currency[i].checked)
			paidCurrency = currency[i].value;
	}
	if(paidCurrency == orgPaidCurrency){
		alert("當前轉換幣種與付款幣種一致，請選擇！");
		return false;
	}
   	document.getElementById("payButton").disabled = true;
   	document.form1.action = "EditCurrencyServlet";
   	document.form1.submit();
}
</script>
</head>
<body>
<h2>更改付款幣種</h2>
<%
String curInRMBRate = null;
Double rmbRate = null;
if(request.getAttribute("curInRMBRate") != null){
	curInRMBRate = (String)request.getAttribute("curInRMBRate");
	rmbRate = Double.parseDouble(curInRMBRate);
}
String curInHKDRate = null;
Double hkdRate = null;
if(request.getAttribute("curInHKDRate") != null){
	curInHKDRate = (String)request.getAttribute("curInHKDRate");
	hkdRate = Double.parseDouble(curInHKDRate);
}

float amercePercent = 0;

Order order = null;
if(request.getAttribute("order") != null){
	order = (Order)request.getAttribute("order");
}
String period = (String)session.getAttribute("period");
String payResult = "Y";

List orderedBookList = (List)request.getAttribute("orderedBookList");
double amerceMount = 0;
if(request.getAttribute("amerceMount") != null){
	amerceMount = (Double)request.getAttribute("amerceMount"); 
}

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

double totalMop = 0.0, totalRmb = 0.0, totalWithdrawMoney = 0.0;
double bookMopSum = 0, bookRmbSum = 0; 
int totalNotEnoughQty = 0;
int totalConfirm = 0, totalWithdraw = 0;

double feeMopPrice = 0;

double shippingFeeMop = 0;

double bookMopPrice = 0;

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
		totalWithdrawMoney = v4.add(v3).doubleValue();
		
		BigDecimal v5 = new BigDecimal(Integer.toString(orDetail.getConfirmQty()));
		BigDecimal qty = v1.add(v5);

		BigDecimal v6 = new BigDecimal(Double.toString(mopPrice));
		BigDecimal v7 = new BigDecimal(Double.toString(rmbPrice));
		
		bookMopPrice = (v5.multiply(v6)).add(new BigDecimal(Double.toString(bookMopPrice))).doubleValue();
		
		BigDecimal v8 = new BigDecimal(Double.toString(totalMop));
		BigDecimal v9 = new BigDecimal(Double.toString(totalRmb));
		//計算金額總額
		totalMop = (v5.multiply(v6)).add(v8).add(new BigDecimal(Double.toString(feeMopPrice))).doubleValue();
		
		//計算書本確定數和退書數的總金額
		BigDecimal v10 = new BigDecimal(Double.toString(bookMopSum));
		BigDecimal v11 = new BigDecimal(Double.toString(bookRmbSum));
		
		bookMopSum = qty.multiply(v6).add(v10).doubleValue();
		bookRmbSum = qty.multiply(v7).add(v11).doubleValue();
	}
}
if(order != null){
	shippingFee = order.getShippingFee();
}
shippingFeeMop = new BigDecimal(Double.toString(bookMopPrice)).multiply(new BigDecimal(Double.toString(shippingFee))).doubleValue();
if(0<shippingFeeMop && shippingFeeMop<1){
	shippingFeeMop = 1;
}else{
	shippingFeeMop = new BigDecimal(Double.toString(shippingFeeMop)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
}

double per = 0.0;

if(payResult == null || payResult.equals("")){
	amercePercent = Float.parseFloat(request.getAttribute("amercePercent").toString());
	BigDecimal value = new BigDecimal(Double.toString(amercePercent));
	BigDecimal percent = new BigDecimal(Double.toString(0.01));
	per = Double.parseDouble((percent.multiply(value)).toString());
}
%>
<form id="EditForm" name="EditForm" method="post" action="EditCurrencyServlet">  
  <input type="hidden" name="type" value="SEARCH"/>
  <table width="350" border="0" cellpadding="0" cellspacing="0" align="left">
    <tr>
      <td height="30" align="center">訂單編號：</td>
      <td>
        <input type="text" name="orderSeqNo" class="inp"/>
      </td>
      <td align="left">
        <input type="submit" name="Submit"  value=" 查 找 " />
      </td>
    </tr>
  </table>
</form>
<br></br>
<hr/>
<%
if(order == null){
%>
<p align="center"><b>沒有該訂書編號的資料！</b></p>
<%	
}
if(order != null && (order.getDifference() != null && order.getDifference() != 0)){
%>
<p align="center"><b>該訂書編號的記錄已作補差價，不能更換幣種操作！</b></p>
<%
}
if(order != null && (order.getDifference() == null || order.getDifference() == 0)){
%>
<form id="form1" name="form1" method="post" target="_self" action="OrderedBookListServlet">
  <input type="hidden" name="type" value="CHANGE" />
  <input type="hidden" name="orderSeqNo" value="<%=orderSeqNo %>" />
  <input type="hidden" name="orgPaidCurrency" id="orgPaidCurrency" value="<%=order.getPaidCurrency() %>"/>
  <input type="hidden" name="shippingFee" value="<%=shippingFee %>"/>
  <p align="center"><b>訂單編號為 <%=orderSeqNo %> 的圖書清單</b></p>
  <table width="99.9%"  align="center" border="0" cellspacing="1" cellpadding="0" id="the-table">
    <tr bgcolor="#C6D6FD">
      <td width="17%" align="center"><strong>編號</strong></td>
      <td width="38%" align="center"><strong>書名</strong></td>
      <td width="5%" align="center"><strong>預估價格(MOP)</strong></td>
      <td width="5%" align="center"><strong>實際價格(MOP)</strong></td>
      <td width="4%" align="center"><strong>確認</strong></td>
	  <td width="4%" align="center"><strong>退訂</strong></td>
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
        double rmbPrice = 0;
        double hkdPrice = 0;
        if(rmbRate != null){
        	rmbPrice = mopPrice * rmbRate;
        }
        if(hkdRate != null){
        	hkdPrice = mopPrice * hkdRate;
        }
        %>
        <input type="hidden" name="mopPrice" value="<%=mopPrice %>" />
        <input type="hidden" name="rmbPrice" value="<%=rmbPrice %>" />
        <input type="hidden" name="hkdPrice" value="<%=hkdPrice %>" />
      </td>
      <td align="center" >
        <input type="hidden" name="confirmQty" value="<%=orDetail.getConfirmQty() %>" />
        <%=orDetail.getConfirmQty() %>
      </td>
      <td align="center" >
        <%=orDetail.getWithdrawQty() %>
      </td>
	  <td align="center" >
	    <%=(orDetail.getWithdrawQty()*price.getWithdrawPrice()) %>
	  </td>
      <td align="center" >
        <%=((orDetail.getConfirmQty() + orDetail.getUnconfirmQty()) * mopPrice + feeMopPrice) %>
      </td>
      <td align="center" >
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
      <td align="center"><%=totalWithdrawMoney %></td>
      <td></td>
      <td align="center"><%=totalMop %></td>
    </tr>
    <tr bgcolor="#FF9900">
      <td colspan="8" align="right">罰款：</td>
      <td align="center">
      <%
      out.print(new BigDecimal(Double.toString(amerceMount)).add(new BigDecimal(Double.toString(shippingFeeMop))).doubleValue());
	  %>
	  </td>
	</tr>
	<tr bgcolor="#FF99FF">
	  <td colspan="8" align="right">逾期付款罰款：</td>
	  <td align="center">
	  <%
	  //計算逾期付款罰款，罰款小於1作1計算，大於1的進行四捨五入計算
	  BigDecimal value1 = new BigDecimal(Double.toString(bookMopSum));
	  BigDecimal value2 = new BigDecimal(Double.toString(bookRmbSum));
	  
	  BigDecimal percent = new BigDecimal(Double.toString(per));
	  double amerceMop = 0;
	  double sum2 = 0;
	  if(percent.doubleValue() != 0){
		  if(0 < (value1.multiply(percent)).doubleValue() && (value1.multiply(percent)).doubleValue() < 1){
			  amerceMop = 1;
		  }else{
			  amerceMop = (value1.multiply(percent)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		  }
	  }
	  
	  if(order != null){
		  if(order.getPaidStatus().equals("Y")){
			  amerceMop = order.getFineforlatepay();
		  }
	  }
	  double amerceRmb = 0;
	  double amerceHkd = 0;
	  if(rmbRate != null){
		  amerceRmb = new BigDecimal(amerceMop * rmbRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
	  }
	  if(hkdRate != null){
		  amerceHkd = new BigDecimal(amerceMop * hkdRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
	  }
	  %>
	    <%=amerceMop %>
	    <input type="hidden" name="amerceMop" value="<%=amerceMop %>"/>
	    <input type="hidden" name="amerceRmb" value="<%=amerceRmb %>"/>
	    <input type="hidden" name="amerceHkd" value="<%=amerceHkd %>"/>
	  </td>
	</tr>
	<tr bgcolor="#CCCC33">
	  <td colspan="8" align="right">總計：</td>
	  <%
	  BigDecimal n1 = new BigDecimal(totalMop);
	  BigDecimal n2 = new BigDecimal(totalWithdrawMoney);
	  BigDecimal n3 = new BigDecimal(amerceMount);
	  BigDecimal n4 = new BigDecimal(amerceMop);
	  BigDecimal n6 = new BigDecimal(sum2);
	  double allMOP = n1.add(n2).add(n3).add(n4).add(new BigDecimal(Double.toString(shippingFeeMop))).doubleValue();
	  %>
	  <td align="center">
	    <input type="hidden" name="shipFineMop" value="<%=shippingFeeMop %>"/>
	    <input type="hidden" name="totalMop" value="<%=allMOP %>"  />
	    <%
	    out.print(allMOP);
	    %>
	  </td>
	</tr>
  </table>
  
  <p align="center">
  <table width="200" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td colspan="2" height="35" align="left">請選擇貨幣:</td>
    </tr>
    <tr>
      <td width="100">
        <input name="paidCurrency" id="mop" type="radio" value="MOP" class="radio" <%if("MOP".equals(order.getPaidCurrency())){out.print("checked");} %>/>
        <label>澳門幣MOP</label>
      </td>
      <td width="100" align="center">
        <label><%=allMOP %></label>
      </td>
    </tr>
    <tr>
      <td>
        <input name="paidCurrency" id="rmb" type="radio" value="RMB" class="radio" <%if("RMB".equals(order.getPaidCurrency())){out.print("checked");} %>/>
        <label>人民幣RMB</label>
      </td>
      <td align="center">
        <%
        double allRMB = 0;
        double shipFineRmb =0 ;
        if(rmbRate != null){
        	allRMB = new BigDecimal(allMOP*rmbRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
        	shipFineRmb = new BigDecimal(shippingFeeMop*rmbRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        %>
        <label><%=allRMB %></label>
        <input type="hidden" name="totalRmb" value="<%=allRMB %>" />
        <input type="hidden" name="shipFineRmb" value="<%=shipFineRmb %>"/>
      </td>
    </tr>
    <tr>
      <td>
        <input name="paidCurrency" id="hkd" type="radio" value="HKD" class="radio" <%if("HKD".equals(order.getPaidCurrency())){out.print("checked");} %>/>
        <label>港幣HKD</label>
      </td>
      <td align="center">
        <%
        double allHKD = 0;
        double shipFineHkd =0 ;
        if(hkdRate != null){
        	allHKD = new BigDecimal(allMOP*hkdRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
        	shipFineHkd = new BigDecimal(shippingFeeMop*hkdRate).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        %>
        <label><%=allHKD %></label>
        <input type="hidden" name="totalHkd" value="<%=allHKD %>"/>
        <input type="hidden" name="shipFineHkd" value="<%=shipFineHkd %>"/>
      </td>
    </tr>
  </table>
  </p>
  <%
  if(request.getAttribute("payResult") != null){
  %>
  <p align="center"><b><%=request.getAttribute("payResult").toString() %></b></p>
  <%
  } else {
  %>
  <p align="center">
    <input type="button" name="Submit2" id="payButton" value=" 確認更改 " onclick="doChange();" />
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="reset" name="Submit3" value="重 設" /> 
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" name="Submit" value="返 回" onclick="window.location.href='EditCurrencyServlet'"  />
  </p>
  <%	  
  }
  %>
   
</form>
<%
}
%>
</body>
</html>