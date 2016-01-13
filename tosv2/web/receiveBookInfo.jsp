<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>領書資料</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/ext-all.css" />
<script language="javascript">
//更新欠書數目
function saveOrUpdateNotEnQty(j) {
	for(i=0; i<document.forms.length; i++){
		if(j == i){
			var temp = document.forms[j].result.value;
			var seqNo = document.forms[j].orderSeqNo.value;
			if(temp == seqNo){
				alert("訂書序號為" + seqNo + "的書單需要補差價處理！");
				return false;
			}
			if(document.forms[j].notEnoughQty.length > 0) {
				for(var k=0; k<document.forms[i].notEnoughQty.length; k++){
			    	var value = document.forms[j].notEnoughQty[k].value;
			    	if(value != ""){
			    		if(isNaN(value)){
			    			alert("請檢查欠數數量必須為整數!");
			      			return ;
			    		}else{
			    			if(value < 0){
			    				alert("不能輸入負數！");
			    				return ;
			    			}else{
			    				if(parseInt(document.forms[j].notEnoughQty[k].value) > parseInt(document.forms[j].confirmQty[k].value)){
			    					alert("缺書數量不能超過確認數量!");
			      					return ;
			    				}
			    			}
			    		}
			    	}
			   }
			} else {
				var value = document.forms[j].notEnoughQty.value;
				if(value != ""){
					if(isNaN(value)) {
			      		alert("請檢查欠數數量必須為整數!");
				  		return ;
			    	} else {
				    	if(value < 0){
				    		alert("不能輸入負數");
				    		return ;
				    	}else{
				    		if(parseInt(document.forms[j].notEnoughQty.value) > parseInt(document.forms[j].confirmQty.value)){
				    			alert("缺書數量不能超過確認數量!");
				      			return ;
				    		}
				    	}
			    	}
				}
			}
			ShowConfirmClose(false);
			document.forms[j].action = "UpdateNotEnQtyServlet";
		   	document.forms[j].submit();
		}
	}
}

function setNotEnoughQty(j){
	for(i=0; i<document.forms.length; i++){
		if(j == i){
			var len = document.forms[i].notEnoughQty.length;
			if(len == undefined){
				document.forms[i].notEnoughQty.value = 0;
			}else{
				for(var k=0; k<len; k++){
					document.forms[i].notEnoughQty[k].value = 0;
				}
			}
		}
	}
}
</script>
<script language="javascript">
var pb_strConfirmCloseMessage;
var pb_blnCloseWindow = false;
pb_strConfirmCloseMessage = '是否已確認領書的資料?';
function ConfirmClose() {
	window.event.returnValue = pb_strConfirmCloseMessage;
	pb_blnCloseWindow = true;
}
function ShowConfirmClose(blnValue) {
	if(blnValue) {
		document.body.onbeforeunload = ConfirmClose;
	} else {
		document.body.onbeforeunload = null;
	}
}
</script>
<script language="JavaScript">
function initArray(){
	for (var i = 0; i < initArray.arguments.length; i++){
		this[i] = initArray.arguments[i];
	}
	this.length = initArray.arguments.length;
}
var colors = new initArray("#FF0000","#FF0000","#FF3333","#FFEEEE");
delay = 1; // seconds
link = 0;
function linkDance(){
	link = (link+1)%colors.length;
	document.getElementById("tip01").style.color = colors[link];
	document.getElementById("tip02").style.color = colors[link];
	setTimeout("linkDance()", delay*500);
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
<body onload='ShowConfirmClose(true);linkDance();'>
<%
String result = "";
if(request.getAttribute("result") != null){
	result = (String)request.getAttribute("result");
}
float amercePercent = 0;

List stuDetList = (List)session.getAttribute("stuDetList");
String oprType = (String)request.getAttribute("oprType");
String period = (String)session.getAttribute("period");
String payResult = (String)request.getAttribute("payResult");
String oprStep = (String)request.getAttribute("oprStep");
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
}

List seqNoList = new ArrayList();
int orderSeqNo = 0;
if(qtyList != null && !qtyList.isEmpty()){
	orderSeqNo = (qtyList.get(0)).getOrderSeqNo();
	for(int i=0; i<qtyList.size(); i++){
		OrDetail o = (OrDetail)qtyList.get(i);
		if(!seqNoList.contains(o.getOrderSeqNo())){
			seqNoList.add(o.getOrderSeqNo());
		}
	}
}
Student stu = null;
Program pro = null;
if(stuDetList != null && stuDetList.size() >= 3) {
	stu = (Student)stuDetList.get(0);
	pro = (Program)stuDetList.get(2);
}
String updateResult = (String)request.getAttribute("updateResult");
if(updateResult != null ) { 
%>
   <script>
   alert('<%=updateResult%>');
   </script>
<%
} 
%>
<table width="650" border="0" cellpadding="0" cellspacing="0" align="center">
  <tr>
    <td align="center" width="150" height="28"><strong>學號</strong></td>
    <td align="center" width="100"><strong>姓名</strong></td>
    <td align="center" width="200"><strong>課程</strong></td>
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
  </tr>
  <%} %>
</table>
<div id="tip01">
  <%if(!"".equals(result) && !"1".equals(result)){%>
  <p align="left">訂書序號為<%=result %>的書單需要補差價處理！</p>
  <%}%>
</div>
<p align="center">您已選擇購買的圖書清單如下：</p>

<table width="99.9%" align="center" border="0" cellspacing="1" cellpadding="0">
  <%
  boolean showOne = false, showTwo = false;
  List tmpList = Arrays.asList(
          						"86349", "86352", "86358", "86373", "86375",
								"86381", "86382", "86386", "86410", "86419",
								"86422", "86430", "86431", "86436", "86460",
								"86475", "86496", "86507", "86532", "86539",
								"86621", "86630", "86700", "86758", "86863",
								"87072", "87074", "87138", "87142", "87314",
								"87349", "87365", "87374", "87382", "87387",
								"87441", "87445", "87452", "87497", "87528",
								"87534", "87613", "87632", "87674", "87676",
								"87708", "87713"
							  );
  
  if(bookList != null && priceList != null && qtyList != null){
	  if(seqNoList != null && !seqNoList.isEmpty()){
		  for(int j=0; j<seqNoList.size(); j++){
			  int seqNo = (Integer)seqNoList.get(j);
			  if (tmpList.contains(String.valueOf(seqNo))) 
			      showOne = true;
  %>
  <tr>
    <td colspan="11">
      <form id="form<%=j %>" name="form<%=j %>" method="post" target="_self" action="OrderedBookListServlet">
        <input type="hidden" name="oprType" value="bookInvoice" />
        <input type="hidden" name="studentNo" value="<%=stu.getStudentNo() %>" />
        <input type="hidden" name="applicantNo" value="<%=stu.getApplicantNo() %>" />
        <input type="hidden" name="result" value="<%=result %>" />
        <table width="100%" border="0" cellspacing="1" cellpadding="0">
          <tr bgcolor="#C6D6FD">
            <td width="10%" align="center"><strong>訂單號</strong></td>
            <td width="12%" align="center"><strong>編號</strong></td>
            <td width="33%" align="center"><strong>書名</strong></td>
            <td width="5%"  align="center"><strong>預估價格<br/>(MOP)</strong></td>
            <td width="5%"  align="center"><strong>實際價格<br/>(MOP)</strong></td>
            <td width="4%"  align="center"><strong>確認</strong></td>
            <td width="4%"  align="center"><strong>退訂</strong></td>
            <td width="5%"  align="center"><strong>缺書</strong></td>
            <td width="5%"  align="center"><strong>退訂費用</strong></td>
            <td width="10%" align="center"><strong>預估購買金額<br/>(MOP)</strong></td>
            <td width="12%" align="center"><strong>實際購買金額<br/>(MOP)</strong></td>
          </tr>
          <%
          for(int i=0; i<bookList.size(); i++){
        	  Book book = (Book)bookList.get(i);
        	  if (showTwo == false && "9781308713335".equals(book.getIsbn())) {
        	      showTwo = true;
        	  }
        	      
        	  Price price = (Price)priceList.get(i);
        	  OrDetail orDetail = (OrDetail)qtyList.get(i);
        	  if(seqNo == orDetail.getOrderSeqNo()){
          %>
          <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
            <td>
              <input type="hidden" name="orderSeqNo" value="<%=orDetail.getOrderSeqNo() %>" /><%=orDetail.getOrderSeqNo() %>
            </td>
            <td>
              <input type="hidden" name="isbn" value="<%=book.getIsbn() %>" />
              <a href="BookDetailServlet?isbn=<%=book.getIsbn() %>" target="_blank"><%=book.getIsbn() %></a>
            </td>
            <td ><%=book.getTitle() %></td>
            <td align="left" >
            <%
            double mopPrice = 0;

            	mopPrice = price.getMopFuturePrice();
            	out.print(price.getMopFuturePrice());
            
            %>
            </td>
            <td align="left" >
            <%
            if(price.getMopNetPrice() == 0 || price.getMopFuturePrice()==price.getMopNetPrice()){
            	mopPrice = price.getMopFuturePrice();
            	out.print("");
            }else{
            	mopPrice = price.getMopNetPrice();
            	out.print(price.getMopNetPrice());
            }
            %>
            </td>
            <td align="center">
              <input type="hidden" name="confirmQty" value="<%=orDetail.getConfirmQty() %>" /><%=orDetail.getConfirmQty() %>
            </td>
            <td align="center" ><%=orDetail.getWithdrawQty() %></td>
            <%
            String notenoughqty = "";
            if(orDetail.getNotEnoughQty() >= 0){
            	notenoughqty = String.valueOf(orDetail.getNotEnoughQty());
			}
            %>
            <td align="center">
              <input type="text" id="len" name="notEnoughQty" value="<%=notenoughqty %>" />
              <input type="hidden" id="old" name="oldNotEnoughQty" value="<%=notenoughqty %>" style="width:20px;"/>
            </td>
            <td align="center" ><%=(orDetail.getWithdrawQty()*price.getWithdrawPrice()) %></td>
            <td><%=((orDetail.getConfirmQty()+orDetail.getUnconfirmQty())*mopPrice) %></td>
            <td><%=((orDetail.getConfirmQty()+orDetail.getUnconfirmQty())*mopPrice) %></td>
          </tr>
          <%
          	  }
          }
          %>
          <tr bgcolor="#C6D6FD">
            <td colspan="11">&nbsp;</td>
          </tr>
	    </table>
	    <%
	    if(!String.valueOf(seqNo).equals(result)){
	    	if(bookList != null && bookList.size() > 0 && payResult != null && oprStep != null && oprStep.equals("step3")) {
	    %>
	    <p align="center">
	      <input type="button" name="allCheck" value="全 取" onclick="setNotEnoughQty('<%=j %>');"/>
	      &nbsp;&nbsp;&nbsp;&nbsp;
	      <input type="button" name="Submit" value="保存欠書數量" onclick="saveOrUpdateNotEnQty('<%=j %>');"/>
	      &nbsp;&nbsp;&nbsp;&nbsp;
	      <input type="reset" name="reset" value="重 設" />
	      &nbsp;&nbsp;&nbsp;&nbsp;
	      <input type="button" name="Submit" value="返 回" onclick="window.location.href='orderbookindex.jsp?oprType=received'" />
	    </p>
	    <%
	    	}
	    } 
	    %>
	  </form>
    </td>
  </tr>
  <%
    	  }
      }
  }
  %>
</table>
<div id="tip02">
<%
if (showOne){
%>
<p align="left">圖書(9781259252778)已到貨,請派書!</p>
<%
} 
%>
<%
if (showTwo){
%>
<p align="left">圖書(9781308713335)已到貨,請派書!</p>
<%
} 
%>
</div>
</body>
</html>