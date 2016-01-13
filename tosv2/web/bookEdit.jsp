<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.must.tos.bean.Book,edu.must.tos.bean.Price" %>
<%@page import="edu.must.tos.bean.BookSupplier"%>
<%@page import="edu.must.tos.bean.Major"%>
<%@page import="edu.must.tos.bean.SysConfig"%>
<%@page import="edu.must.tos.util.ToolsOfString"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>編輯圖書</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
function checkForm(){
	if (form1.isbn.value == ""){
		alert("請輸入圖書編號！");
		form1.isbn.focus();
		return;
	}
	if(form1.title.value == ""){
		alert("請輸入書名！");
		form1.title.focus();
		return;
	}
	if(form1.author.value == ""){
		alert("請輸入作者！");
		form1.author.focus();
		return;
	}
	if(form1.publisher.value == ""){
		alert("請輸入出版商！");
		form1.publisher.focus();
		return;
	}
	if(form1.publishYear.value == ""){
		alert("請輸入出版日期！");
		form1.publishYear.focus();
		return;
	}
	if(form1.language.value == ""){
		alert("請選擇圖書語言！");
		form1.language.focus();
		return;
	}
	if(form1.MOPfuturePrice.value == ""){
		alert("請輸入價錢！");
		form1.MOPfuturePrice.focus();
		return;
	}else{
		var flag = checkPrice(form1.MOPfuturePrice.value);
		if(!flag){
			alert("請正確填寫葡幣預估價價錢，不要超過五位數且小數點前實數不要超過五位！");
			form1.MOPfuturePrice.focus();
			return ;
		}
	}
	if(form1.MOPnetPrice.value == ""){
		alert("請輸入價錢！");
		form1.MOPnetPrice.focus();
		return;
	}else{
		var flag = checkPrice(form1.MOPnetPrice.value);
		if(!flag){
			alert("請正確填寫葡幣實價價錢，不要超過五位數且小數點前實數不要超過五位！");
			form1.MOPnetPrice.focus();
			return ;
		}
	}
	if(form1.RMBfuturePrice.value == ""){
		alert("請輸入價錢！");
		form1.RMBfuturePrice.focus();
		return;
	}else{
		var flag = checkPrice(form1.RMBfuturePrice.value);
		if(!flag){
			alert("請正確填寫人民幣預估價價錢，不要超過五位數且小數點前實數不要超過五位！");
			form1.RMBfuturePrice.focus();
			return ;
		}
	}
	if(form1.RMBnetPrice.value == ""){
		alert("請輸入價錢！");
		form1.RMBnetPrice.focus();
		return;
	}else{
		var flag = checkPrice(form1.RMBnetPrice.value);
		if(!flag){
			alert("請正確填寫人民幣實價價錢，不要超過五位數且小數點前實數不要超過五位！");
			form1.RMBnetPrice.focus();
			return ;
		}
	}
	
	if(form1.currency.value != ""){
		if(form1.unitPrice.value == ""){
			alert("入貨單價不能為空！");
			form1.unitPrice.focus();
			return;
		} else {
			var flag = checkPrice(form1.unitPrice.value);
			if(!flag){
				alert("請正確填寫入貨單價，不要超過五位數且小數點前實數不要超過五位！");
				form1.unitPrice.focus();
				return ;
			}
		}
		if(form1.disCount.value == ""){
			alert("請輸入入貨折扣！");
			form1.disCount.focus();
			return;
		}else{
			var flag = checkPrice(form1.disCount.value);
			if(!flag){
				alert("請正確填寫入貨折扣，不要超過五位數且小數點前實數不要超過五位！");
				form1.disCount.focus();
				return ;
			}
		}
	}
	
	if(form1.favourablePrice.value == ""){
		alert("請輸入優惠價價錢！");
		form1.favourablePrice.focus();
		return;
	}else{
		var flag = checkPrice(form1.favourablePrice.value);
		if(!flag){
			alert("請正確填寫優惠價價錢，不要超過五位數且小數點前實數不要超過五位！");
			form1.favourablePrice.focus();
			return ;
		}
	}
	var currency = form1.currency.value;
	var unitPrice = form1.unitPrice.value;
	var disCount = form1.disCount.value;
	var mopfuturePrice = form1.MOPfuturePrice.value;
	if(form1.MOPnetPrice.value != "" && parseInt(form1.MOPnetPrice.value) > 0){
		mopfuturePrice = form1.MOPnetPrice.value;
	}
	if(currency != ""){
		var price = parseFloat(unitPrice, 10) * parseFloat(disCount, 10);
		if(currency == "MOP"){
			if(price > mopfuturePrice){
				alert("入貨價不能大於預售價！");
				return false;
			}
		} else if(currency == "HKD"){
			var rate = form1.HKD.value;
			if((price * rate) > mopfuturePrice){
				alert("入貨價不能大於預售價！");
				return false;
			}
		} else if(currency == "USD"){
			var rate = form1.USD.value;
			if((price * rate) > mopfuturePrice){
				alert("入貨價不能大於預售價！");
				return false;
			}
		} else if(currency == "RMB"){
			var rate = form1.RMB.value;
			if((price * rate) > mopfuturePrice){
				alert("入貨價不能大於預售價！");
				return false;
			}
		}
	}
	if(form1.isbn.value == form1.oldIsbn.value){
		form1.changeIsbn.value = "N";
	} else {
		if(confirm("圖書ISBN已改變, 新增請按確定, 繼續編輯請按取消!")){
			form1.changeIsbn.value = "Y";
		} else {
			form1.changeIsbn.value = "N";
		}
	}
	document.form1.action = "EditBookInfoServlet";
	document.form1.submit();
}
function checkPrice(val){
	if(isNaN(val)){
		return false;
	}else{
		var tmp = val.indexOf(".");
		if(tmp < 0){
			if(val.length >= 6)
				return false;
		}else{
			var b = val.substring(0, val.indexOf("."));
			if(b.length >= 6)
				return false;
		}
	}
	return true;
}
function countlen(){
	if(document.form1.remarks.value.length > 60){
		alert("字數已經超過！！");
		document.form1.remarks.value=document.form1.remarks.value.substring(0,60);
	}
	return true;
}
</script>
</head>
<body>
<h2>修改圖書資訊</h2>
<form method="post" name="form1">
<input type="hidden" name="type" value="saveInSession">
<%
try{
	Book book = (Book)session.getAttribute("book");
	List langList = (List)session.getAttribute("langList");
	List priceList = (List)session.getAttribute("priceList");
	List<BookSupplier> supplierList = null;
	if(session.getAttribute("supplierList") != null){
		supplierList = (List)session.getAttribute("supplierList");
	}
	List<Major> majorList = null;
	if(session.getAttribute("majorList") != null){
		majorList = (List)session.getAttribute("majorList");
	}
	List<SysConfig> rateList = null;
	if(request.getAttribute("rateList") != null){
		rateList = (List)request.getAttribute("rateList");
	}
%>
<%
for(SysConfig rate : rateList){
	%>
	<input type="hidden" name="<%=rate.getScKey() %>" value="<%=rate.getScValue1() %>"/>
	<%
}
%>
<table align="center" width="550" border="0">
  <tr>
    <td width="100">圖書編號：</td>
    <td colspan="3">
      <input type="hidden" name="changeIsbn" value="N" />
      <input type="hidden" name="oldIsbn" value="<%=book.getIsbn()%>" />
      <input type="text" name="isbn" value="<%=book.getIsbn()%>" class="inp">
    </td>
  </tr>
  <tr>
    <td>書名：</td>
    <td colspan="3">
      <input type="text" class="textInput" name="title" maxlength="50" value="<%=book.getTitle() %>" >
    </td>
  </tr>
  <tr>
    <td>作者：</td>
    <td colspan="3">
      <input type="text" class="textInput" name="author" maxlength="50" value="<%=book.getAuthor() %>">
    </td>
  </tr>
  <tr>
    <td>出版商：</td>
    <td colspan="3">
      <input type="text" class="textInput" name="publisher" maxlength="50" value="<%=book.getPublisher() %>"">
    </td>
  </tr>
  <tr>
    <td>出版年份：</td>
    <td><input maxlength="20" type="text" name="publishYear" value="<%=book.getPublishYear() %>" class="inp"></td>
    <td width="90">版本：</td>
    <td><input maxlength="20" type="text" name="edition" value="<%=book.getEdition() %>" class="inp"></td>
  </tr>
  <tr>
    <td>圖書語言：</td>
    <td>
      <select name="language">
        <option value="<%=book.getLanguage() %>"><%=book.getLanguage() %></option>
        <%
        for(int i=0;i<langList.size();i++){
        %>
        <option value="<%=langList.get(i) %>"><%=langList.get(i) %></option>
        <%
        }
        %>
      </select>
    </td>
    <td>
      <label><%--圖書類型：--%></label>
    </td>
    <td>
      <%--
      <select name="bookType">
        <option value="<%=book.getBookType() %>"><%=book.getBookType() %></option>
        <option value="RB">RB</option>
        <option value="CB">CB</option>
        <option value="TB">TB</option>
      </select>
      --%>
    </td>
  </tr>
  <tr>
    <td>是否可退書：</td>
    <td >
      <input name="withdrawInd" type="radio" class="radio" value="Y" <%if("Y".equals(book.getWithdrawInd())){out.print(" checked");}%>>是
      <input name="withdrawInd" type="radio" class="radio" value="N" <%if("N".equals(book.getWithdrawInd())){out.print(" checked");}%>>否
    </td>
     <td>是否可補訂：</td>
    <td >
      <input name="supplement" type="radio" class="radio" value="Y" <%if("Y".equals(book.getSupplement())){out.print(" checked");}%>>是
      <input name="supplement" type="radio" class="radio" value="N" <%if("N".equals(book.getSupplement())){out.print(" checked");}%>>否
      </td>
  </tr>
 
  <tr>
    <td>書商1：</td>
    <td>
      <select name="supplierCode1">
        <option value="">==請選擇==</option>
        <%
	    if(supplierList!=null && !supplierList.isEmpty()){
	    	for(BookSupplier b : supplierList){
	    		%>
	    <option value="<%=b.getSupplierCode() %>" <%if(b.getSupplierCode().equals(book.getSupplierCode1())){out.print("selected");} %>><%=b.getSupplierName() %></option>
			    <%
			}
	    }
        %>
      </select>
    </td>
    <td>書商2：</td>
    <td>
      <select name="supplierCode2">
        <option value="">==請選擇==</option>
        <%
        if(supplierList!=null && !supplierList.isEmpty()){
        	for(BookSupplier b : supplierList){
        		%>
        <option value="<%=b.getSupplierCode() %>" <%if(b.getSupplierCode().equals(book.getSupplierCode2())){out.print("selected");} %>><%=b.getSupplierName() %></option>
			    <%
			}
        }
        %>
      </select>
    </td>
  </tr>
  <tr>
    <td>備注：</td>
    <td colspan="3"><textarea rows="2" cols="54" name="remarks" onKeyUp="countlen()"><%=book.getRemarks() %></textarea></td>
  </tr>
  <tr>
    <td>入貨單價：</td>
    <td>
      <input type="text" maxlength="8" name="unitPrice" value="<%=book.getUnitPrice() %>" class="inp">
    </td>
    <td>入貨幣種：</td>
    <td>
      <select name="currency">
        <option value="">==請選擇==</option>
        <option value="MOP" <%if("MOP".equals(book.getCurrency())){out.print("selected");} %>>MOP</option>
        <option value="HKD" <%if("HKD".equals(book.getCurrency())){out.print("selected");} %>>HKD</option>
        <option value="USD" <%if("USD".equals(book.getCurrency())){out.print("selected");} %>>USD</option>
        <option value="RMB" <%if("RMB".equals(book.getCurrency())){out.print("selected");} %>>RMB</option>
      </select>
    </td>
  </tr>
  <tr>
    <td>入貨折扣：</td>
    <td>
      <input type="text" maxlength="8" name="disCount" value="<%=book.getDisCount() %>" class="inp">
    </td>
    <td>優惠價：</td>
    <td>
      <input type="text" maxlength="8" name="favourablePrice" value="<%=book.getFavourablePrice() %>" class="inp">
    </td>
  </tr>
  <tr>
    <td>
      <%--<label>年級：</label>--%>
    </td>
    <td>
      <%--<input type="text" maxlength="1" name="grade" class="inp" size="4" value="<%=ToolsOfString.chkNullString(book.getGrade()) %>">--%>
    </td>
    <td>
      <%--<label>選修/必修：</label>--%>
    </td>
    <td>
      <%--
      <input name="core" type="radio" class="radio" value="E" <%if("E".equals(book.getCore())){out.print("checked");} %>>選修
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input name="core" type="radio" class="radio" value="C" <%if("C".equals(book.getCore())){out.print("checked");} %>>必修
      --%>
    </td>
  </tr>
</table>
<table align="center" width="550" cellpadding="1" cellspacing="0" border="0">
  <tr>
    <td width="120">&nbsp;</td>
    <td>預估價</td>
    <td>實價</td>
  </tr>
  <tr>
  <%
  if(priceList == null || priceList.isEmpty()){
  %>
  <tr>
  	<td>澳門幣（MOP）：</td>
  	<td align="left"><input type="text" maxlength="8" name="MOPfuturePrice" id="mopfuturePrice" class="inp"></td>
  	<td align="left"><input type="text" maxlength="8" name="MOPnetPrice" id="mopnetPrice" class="inp"></td>
  </tr>
  <tr>
  	<td>人民幣（RMB）：</td>
  	<td align="left"><input type="text" maxlength="8" name="RMBfuturePrice" id="rmbfuturePrice" class="inp"></td>
  	<td align="left"><input type="text" maxlength="8" name="RMBnetPrice" id="rmbnetPrice" class="inp"></td>
  </tr>
  <%
  }else{
	  for(int i=0; i<priceList.size(); i++){
		  Price p = (Price)priceList.get(i);
  %>
  <tr>
    <td><%if("MOP".equals(p.getCurrency())){out.print("澳門幣");}else{out.print("人民幣");} %>(<%=p.getCurrency()%>)</td>
    <td><input type="text" maxlength="8" name="<%=p.getCurrency()%>futurePrice" value="<%=p.getFuturePrice() %>" style="height:18px;"></td>
    <td><input type="text" maxlength="8" name="<%=p.getCurrency()%>netPrice" value="<%=p.getNetPrice() %>" style="height:18px;"></td>
  </tr>
  <%
      }
  }
  %>
</table>
<%
	}catch(Exception e){
		e.printStackTrace();
	}
%>
<p align="center">
  <input type="button" value="下一步" onClick="checkForm()">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" value="關閉該頁面" onClick="window.close();">
</p>
</form>
</body>
</html>
