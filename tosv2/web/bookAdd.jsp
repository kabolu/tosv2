<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@page import="edu.must.tos.bean.BookSupplier"%>
<%@page import="edu.must.tos.bean.SysConfig"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加新書</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
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
		alert("請輸入出版社！");
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
	if(form1.mopfuturePrice.value == ""){
		alert("請輸入葡幣預估價價錢！");
		form1.mopfuturePrice.focus();
		return;
	}else{
		var flag = checkPrice(form1.mopfuturePrice.value);
		if(!flag){
			alert("請正確填寫葡幣預估價價錢，不要超過五位數且小數點前實數不要超過五位！");
			form1.mopfuturePrice.focus();
			return ;
		}
	}
	if(form1.mopnetPrice.value == ""){
		alert("請輸入葡幣實價價錢！");
		form1.mopnetPrice.focus();
		return;
	}else{
		var flag = checkPrice(form1.mopnetPrice.value);
		if(!flag){
			alert("請正確填寫葡幣實價價錢，不要超過五位數且小數點前實數不要超過五位！");
			form1.mopnetPrice.focus();
			return ;
		}
	}
	if(form1.rmbfuturePrice.value == ""){
		alert("請輸入人民幣預估價價錢！");
		form1.rmbfuturePrice.focus();
		return;
	}else{
		var flag = checkPrice(form1.rmbfuturePrice.value);
		if(!flag){
			alert("請正確填寫人民幣預估價價錢，不要超過五位數且小數點前實數不要超過五位！");
			form1.rmbfuturePrice.focus();
			return ;
		}
	}
	if(form1.rmbnetPrice.value == ""){
		alert("請輸入人民幣實價價錢！");
		form1.rmbnetPrice.focus();
		return;
	}else{
		var flag = checkPrice(form1.rmbnetPrice.value);
		if(!flag){
			alert("請正確填寫人民幣實價價錢，不要超過五位數且小數點前實數不要超過五位！");
			form1.rmbnetPrice.focus();
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
	var mopfuturePrice = form1.mopfuturePrice.value;
	if(form1.mopnetPrice.value != "" && parseInt(form1.mopnetPrice.value) > 0){
		mopfuturePrice = form1.mopnetPrice.value;
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
	document.form1.action = "AddBookServlet";
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
		document.form1.remarks.value = document.form1.remarks.value.substring(0, 60);
	}
	return true;
}
</script>
</head>
<body>
<h2>添加圖書</h2>
<%
session.removeAttribute("other");
List langList = null;
List<BookSupplier> supplierList = null;
List<SysConfig> rateList = null;
try{
	langList = (List)request.getAttribute("langList");
	if(request.getAttribute("supplierList") != null){
		supplierList = (List)request.getAttribute("supplierList");
	}
	if(request.getAttribute("rateList") != null){
		rateList = (List)request.getAttribute("rateList");
	}
}catch(Exception e){e.printStackTrace();}
%>
<form name="form1" method="post">
  <input type="hidden" name="type" value="AddBookInSession"/>
  <%
  for(SysConfig rate : rateList){
  %>
  <input type="hidden" name="<%=rate.getScKey() %>" value="<%=rate.getScValue1() %>"/>
  <%
  }
  %>
  <table width="600" align="center" cellpadding="0" cellspacing="1" border="0">
    <tr>
      <td height="40" colspan="4"><b>&gt;&gt;&gt;&gt;&gt;&gt;請輸入圖書的基本資料</b></td>
    </tr>
    <tr>
      <td width="100">ISBN：</td>
      <td colspan="3">
        <input type="text" id="isbn" name="isbn" maxlength="30" onKeyUp="value=value.replace(/[^$\w\.\/]/ig,'')" class="inp">
        <font color="red">*</font>
      </td>
    </tr>
    <tr>
      <td>書名：</td>
      <td colspan="3">
        <input maxlength="50" type="text" name="title" class="textInput" >
        <font color="red">*</font>
      </td>
    </tr>
    <tr>
      <td>作者：</td>
      <td colspan="3">
        <input maxlength="50" class="textInput" type="text" name="author" >
        <font color="red">*</font>
      </td>
    </tr>
    <tr>
      <td>出版商：</td>
      <td colspan="3">
        <input maxlength="50" type="text" class="textInput" name="publisher" >
        <font color="red">*</font>
      </td>
    </tr>
    <tr>
    <td>出版年份：</td>
      <td>
        <input maxlength="20" type="text" name="publishYear" class="inp">
        <font color="red">*</font>
      </td>
      <td>版本：</td>
      <td>
        <input type="text" name="edition" maxlength="20" class="inp">
      </td>
    </tr>
    <tr>
      <td height="25">圖書語言：</td>
      <td>
        <select name="language">
          <option value="" selected>=請選擇語言=</option>
          <%
          if(langList!=null){
        	  for(int i=0;i<langList.size();i++){
        		  %>
        		  <option value="<%=langList.get(i) %>"><%=langList.get(i) %></option>
        		  <%
        	  }
          }
          %>
        </select>
        <font color="red">*</font>
      </td>
      <td>
        <%--<label>圖書類型：</label>--%>
      </td>
      <td>
        <%--
        <select name="bookType">
          <option value="" selected>=請選擇圖書類型=</option>
          <option value="RB">RB</option>
          <option value="CB">CB</option>
          <option value="TB">TB</option>
        </select>
        <font color="red">*</font>
        --%>
      </td>
    </tr>
    <tr>
      <td>是否可退書：</td>
      <td >
        <input name="withdrawInd" type="radio" class="radio" value="Y" checked="checked">是&nbsp;
        <input name="withdrawInd" type="radio" class="radio" value="N">否
      </td>
       <td>是否可補訂：</td>
      <td >
        <input name="supplement" type="radio" class="radio" value="Y">是&nbsp;
        <input name="supplement" type="radio" class="radio" value="N">否
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
        		  <option value="<%=b.getSupplierCode() %>"><%=b.getSupplierName() %></option>
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
           		<option value="<%=b.getSupplierCode() %>"><%=b.getSupplierName() %></option>
           		<%
           	}
          }
          %>
        </select>
      </td>
    </tr>
    <tr>
      <td>備注：</td>
      <td colspan="3">
        <textarea rows="2" cols="54" name="remarks" onKeyUp="countlen()"></textarea>
      </td>
    </tr>
    <tr>
      <td>入貨單價：</td>
      <td>
        <input type="text" maxlength="8" name="unitPrice" class="inp">
      </td>
      <td>入貨幣種：</td>
      <td>
        <select name="currency">
          <option value="">==請選擇==</option>
          <option value="MOP">MOP</option>
          <option value="HKD">HKD</option>
          <option value="USD">USD</option>
          <option value="RMB">RMB</option>
        </select>
      </td>
    </tr>
    <tr>
      <td>入貨折扣：</td>
      <td>
        <input type="text" maxlength="8" name="disCount" class="inp">
      </td>
      <td>優惠價：</td>
      <td>
        <input type="text" maxlength="8" name="favourablePrice" class="inp">
      </td>
    </tr>
    <tr>
      <td>
        <%--<label>年級：</label>--%>
      </td>
      <td>
        <%--<input type="text" maxlength="1" name="grade" class="inp" size="4">--%>
      </td>
      <td>
        <%--<label>選修/必修：</label>--%>
      </td>
      <td>
        <%--
        <input name="core" type="radio" class="radio" value="E">選修&nbsp;
        <input name="core" type="radio" class="radio" value="C">必修
        --%>
      </td>
    </tr>
  </table>
  <table width="600" align="center">
    <tr>
      <td height="50" colspan="3"><b>&gt;&gt;&gt;&gt;&gt;&gt;請填寫圖書的價格</b></td>
    </tr>
	<tr>
	  <td width="100">&nbsp;</td><td align="left">預估價</td>
	  <td align="left">實價</td>
	</tr>
	<tr>
	  <td>澳門幣（MOP）：</td>
	  <td align="left">
	    <input type="text" maxlength="8" name="mopfuturePrice" id="mopfuturePrice" class="inp">
	  </td>
	  <td align="left">
	    <input type="text" maxlength="8" name="mopnetPrice" id="mopnetPrice" class="inp">
	  </td>
	</tr>
	<tr>
	  <td>人民幣（RMB）：</td>
	  <td align="left">
	    <input type="text" maxlength="8" name="rmbfuturePrice" id="rmbfuturePrice" class="inp">
	  </td>
	  <td align="left">
	    <input type="text" maxlength="8" name="rmbnetPrice" id="rmbnetPrice" class="inp">
	  </td>
	</tr>
	<tr>
	  <td height="50" colspan="3" align="center">
	    <input type="button" value="下一步" onClick="checkForm();">
	    &nbsp;&nbsp;&nbsp;&nbsp;
	    <input type="reset" value=" 重置 ">
	  </td>
	</tr>
  </table>
</form>
</body>
</html>