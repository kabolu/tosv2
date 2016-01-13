<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>已選圖書信息</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.textlen {
	height: 18px;
	width: 20px;
}
.btnlen {
	height: 18px;
	width: 20px;
	padding-bottom: 0px;
	padding-top: 0px;
}
-->
</style>
<script language="javascript">
javascript:window.history.forward(1);
 
//加1
function plus(id) {
	var count = parseInt(document.getElementById(id).value);
    document.getElementById(id).value = count+1;
}
//減1
function minus(id) {
	var count = parseInt(document.getElementById(id).value);
   	if(count > 1) {
    	document.getElementById(id).value = count-1;
	} else {
	 	alert("訂書數不能小於1！若不想購買，請直接撤銷！");
	}	
}
 
function confirmOrderBook() {
	document.confirmForm.action = "ConfirmOrderBookServlet";
   	document.confirmForm.submit();
}

function confirmRetailBook() {
	if(confirm("是否確定提交零售訂購的資料？")){
		document.confirmForm.action = "RetailBookServlet?type=retailBookOrder";
   		document.confirmForm.submit();
	}
}

function cancelBook(id) {
	document.confirmForm.action = "OrderIngBookServlet?oprType=cancel&id="+id+"";
   	document.confirmForm.submit();
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
String retail = (String)session.getAttribute("retail");
List<OrderInfo> selectedBookList = (List)session.getAttribute("selectedBookList");
%>
<form action="" target="_parent" method="post" name="confirmForm" >
  <p align="center" class="blackBold16px">您已選擇購買的圖書清單如下：</p>
  <table width="99.9%" align="center" border="0" cellspacing="1" cellpadding="1" >
    <tr bgcolor="#C6D6FD">
      <td width="15%" align="center">編號</td>
      <td width="35%" align="center">書名</td>
      <td width="5%"  align="center">補訂標識</td>
      <td width="10%" align="center">科目編號</td>
      <td width="5%"  align="center">預估價格(MOP)</td>  
      <td width="5%"  align="center">實際價格(MOP)</td>
      <td width="10%" align="center" colspan="2">確認</td>
      <td width="15%" align="center">操作</td>
    </tr>
    <%
     if(selectedBookList != null) {
    	 int i=0;
    	 for(OrderInfo info : selectedBookList) {
    %>
    <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
      <td align="left">
        <a href="BookDetailServlet?isbn=<%=info.getIsbn() %>" target="_blank"><%=info.getIsbn() %></a>
      </td>
      <td align="left"><%=info.getTitle() %></td>
      <td align="left"><%if(info.getSupplement().equals("Y")){ out.print("是"); }else if(info.getSupplement().equals("N")){ out.print("否");}else{out.print("N/A");} %></td>
      <td align="left"><%=info.getCourseCode() %></td>
      <td align="left"><%=info.getMopFuturePrice() %></td>
<%--      <td align="left"><%=info.getRmbFuturePrice() %></td>--%>
      <td align="left"><%if(info.getMopFuturePrice()==info.getMopNetPrice()){
    	  out.print("");
      }else{
    	  out.print(info.getMopNetPrice());
      }  %></td>
      
     
      <td colspan="2" align="center">
        <table width="50" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td rowspan="2">
              <input name="confirmQty" type="text" class="textlen" id="confirm<%=i %>" value="<%=info.getConfirmQty() %>" readonly />
            </td>
            <td align="left" valign="middle">
              <input type="button" name="Submit22" value="+" class="btnlen" onclick="plus('confirm<%=i %>');" />
            </td>
          </tr>
          <tr>
            <td align="left" valign="middle">
              <input type="button" name="Submit22" value="-" class="btnlen" onclick="minus('confirm<%=i %>');" />
            </td>
          </tr>
        </table>
      </td>
      <td align="center" >
        <input type="button" name="Submit3" value="撤銷" onclick="cancelBook(<%=i %>)" />
        <input type="reset" name="Submit2" value="重置" />
      </td>
    </tr>
    <%
    	 i++;
         }
     }
    %>
  </table>
  <p align="center">
  <%
  if(selectedBookList != null && !selectedBookList.isEmpty()) {
	  if(retail != null && "RETAIL".equals(retail)){
		  %>
		  備註：<input type="text" name="remarks" class="inp" style="width:180px;" maxlength="15"/>
		  <br/><br/>
		  <input type="button" name="Submit" value="確認購買" onclick="confirmRetailBook();" />
		  <%
	  }else{
		  %>
		  <input type="button" name="Submit" value="確認購買" onclick="confirmOrderBook();" />
		  <%
      }
  }
  %>
  </p>
</form>
</body>
</html>