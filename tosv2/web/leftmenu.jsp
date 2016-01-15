<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>左側--功能菜單欄</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<script type="text/javascript"> 
javascript:window.history.forward(1);

function show(id){ 
	//欄位數量
	myitem = 2;
	for(j=0;j<myitem;j++){
		if(id==j){  
			if(b[j].style.display=="none"){
				b[j].style.display="";
			}else{
				b[j].style.display="none";
			}
		}  
		if(id!=j){
			b[j].style.display="none";
		}
	}
}

function showx(id) {
	var a = document.getElementById(id);
	if(a.style.display=="none") {
		a.style.display="";
	} else {
		a.style.display="none";
	}
	var arr = new Array("a","b","c","d","e","f","g","h","i","l");
	  
	for(var i = 0;i < arr.length; i++) {
		if(arr[i]==id) {
			continue;
		}
	    document.getElementById(arr[i]).style.display = "none";
	}
}
//檢查是否是領書期
function checkReceive(id){
	document.getElementById(id).href="orderbookindex.jsp?oprType=received";
	return;
}
 
function checkOrderBooks(id){
	document.getElementById(id).href="orderbookindex.jsp?oprType=orderBooks";
	return; 
}

//delete order
function checkDelOrders(id){
	document.getElementById(id).href="deleteOrderIndex.jsp";
	return;
}
</script>
<body class="left_bk">
<table width="95%" border="0" align="center" cellpadding="4" cellspacing="0">
 <tr>
    <td height="62">
    <p align=center><img src="images/logo.gif" width="66" height="66" alt="logo"></p>
    </td>
  </tr>
  <tr>
    <td>
     <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="9"><img src="images/menu1_left.jpg" width="9" height="35" /></td>
        <td align="center" background="images/menu1_bk.jpg"><img src="images/menu1_pic1.jpg" width="107" height="35" /></td>
        <td width="9"><img src="images/menu1_right.jpg" width="9" height="35" /></td>
      </tr>
    </table>
   </td>
  </tr>
  <tr>
   <td>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td style="background:url(images/menu1_bk.jpg) repeat ;" align="center" height="20">
          <%
          String curIntake = "";
          if(session.getAttribute("curIntake")!=null){
        	  curIntake = (String)session.getAttribute("curIntake");
          }
          %>
          <b>當前學期：<%=curIntake %></b>
        </td>
      </tr>
    </table>
   </td>
  </tr>
  <tr>
    <td>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="7" height="7"><img src="images/menu_lefttop.jpg" width="7" height="7" /></td>
        <td height="7" background="images/menu_bktop.jpg"></td>
        <td width="7"><img src="images/menu_righttop.jpg" width="7" height="7" /></td>
      </tr>  
      <tr>
        <td width="7" background="images/menu_bkdown_l.jpg">&nbsp;</td>
        <td bgcolor="#FFFFFF" valign="top">
        
        <!--  圖書資訊管理  -->
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		  <tr>
			<td height="25"><a href="main.jsp" target="main" class="menu_button" onClick="showx('a')">圖書資訊管理</a></td>
		  </tr>
		  <tr style="display:none" id="a">
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
			  <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
			  <a href="uploadBook.jsp" target="main">圖書信息導入</a>
			  </td>
			 </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
			  <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
			  <a href="book.jsp" target="main">圖書信息查詢</a>
			  </td>
			 </tr>    
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
			  <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
				<a href="uploadPrice.jsp" target="main">圖書實付價導入</a></td>
			 </tr>    
			</table>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
			  <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
				<a href="uploadFuturePrice.jsp" target="main">圖書預估價導入</a></td>
			 </tr>    
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
			  <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
				<a href="FacultyListServlet?type=uploadBookTempl" target="main">科表信息導入</a></td>
			 </tr>    
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
			  <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
				<a href="uploadUpdBook.jsp" target="main">圖書資訊更新導入</a></td>
			 </tr>    
		    </table>
		    </td>
		  </tr>			  		  	  		  
		</table>
		
		
		<!--  圖書訂購管理  -->
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		 <tr>
		  <td height="25"><a href="main.jsp" target="main" class="menu_button" onClick="showx('b')">圖書訂購管理</a></td>
		 </tr>
		 <tr style="display:none" id="b">
		  <td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
		       <a href="" target="main" id="onsale" onClick="checkOrderBooks(this.id);" >訂購圖書</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
		       <a href="" target="main" id="onDel" onClick="checkDelOrders(this.id);" >取消整體訂購</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
		       <a href="DelUnpaidOrderServlet" target="main">取消未付款訂單</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
		       <a href="changeBookIndex.jsp" target="main">置換圖書</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
		       <a href="" id="receive" target="main" onClick="checkReceive(this.id);" >領取圖書</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
		       <a href="PurchasingBookServlet" id="receive" target="main" >代購圖書</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
		       <a href="RetailBookServlet" target="main" >零售訂書</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
		       <a href="EditCurrencyServlet" target="main">更改付款幣種</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
		       <a href="TransactionServlet?type=editTransaction" target="main">更改交易項目</a></td>
		     </tr>												
			</table>
			</td>
		   </tr>			  		  	  		  
		  </table>

		<!-- 新生訂書 -->
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		 <tr>
		  <td><a href="main.jsp" class="menu_button"  target="main" onClick="showx('h')">新生訂書管理</a></td>
		 </tr>
		 <tr style="display:none" id="h">
		  <td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="uploadNewCourseInfo.jsp" target="main">新生選科資訊導入</a>
		      </td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="ExportNewStudOrderInfoServlet" target="main">新生訂書資訊導出</a>
		      </td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="uploadNewStudOrderInfo.jsp" target="main">新生訂書資訊導入</a>
		      </td>
		     </tr>												
			</table>
			<!-- 
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="updateNewStudNo.jsp" target="main">新生學號更新</a>
		      </td>
		     </tr>												
			</table>
			 -->
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="ExportPaidNewsServlet" target="main">導出已交款新生名單</a>
		      </td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="NewStudAccountServlet" target="main">會計處新生收款報表</a>
		      </td>
		     </tr>												
			</table>
		  </td>
		 </tr>			  		  	  		  
		</table>

		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		 <tr>
		  <td><a href="main.jsp" class="menu_button"  target="main" onClick="showx('i')">倉存管理</a></td>
		 </tr>
		 <tr style="display:none" id="i">
		  <td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
			  <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
				<a href="BookInventoryServlet?type=cleanSession" target="main">圖書入庫管理</a></td>
			 </tr>    
		    </table>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
			  <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
				<a href="BookStockOutServlet?type=cleanSession" target="main">圖書出庫管理</a></td>
			 </tr>    
		    </table>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
			  <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp; 
				<a href="BookInventoryServlet?type=searchPrNumList" target="main">出/入貨單查詢</a></td>
			 </tr>    
		    </table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="ExportBookInventoryServlet" target="main">圖書庫存信息表</a></td>
		     </tr>												
		    </table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="updateStock.jsp" target="main">導入盤點資料</a></td>
		     </tr>												
			</table>
			<!-- 
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="ExportStocktakeServlet" target="main">盤點記錄</a></td>
		     </tr>												
			</table>
			-->
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="BookSupplierServlet" target="main">書商資料</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="ExportBookStockInServlet" target="main">導出入庫信息表</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="bookStockOutDetail.jsp" target="main">出庫記錄查詢</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="ExportBookStockOutServlet" target="main">出貨匯總表和圖書出庫詳細表</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="ShippingFeeServlet" target="main">費用記錄</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="bookStockDetail.jsp" target="main">圖書出入貨詳情</a></td>
		     </tr>												
			</table>
		  </td>
		 </tr>			  		  	  		  
		</table>

		<!--  一般資訊查詢  -->
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		 <tr>
		  <td><a href="main.jsp" class="menu_button"  target="main" onClick="showx('c')">一般資訊查詢</a></td>
		 </tr>
		 <tr style="display:none" id="c">
		  <td>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="student.jsp" target="main">學生信息查詢</a></td>
		   </tr>
		  </table>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="studEnrolInfo.jsp" target="main">學生選科信息查詢</a></td>
		   </tr>
		  </table>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="FacultyListServlet" target="main">學院信息查詢</a></td>
		   </tr>												
		  </table>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="course.jsp" target="main">科目信息查詢</a></td>
		   </tr>												
		  </table>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="program.jsp" target="main">課程信息查詢</a></td>
		   </tr>												
		  </table> 
		  </td>
		 </tr>		  	  		  
		</table>

		<!--  信息統計  -->
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		 <tr>
		  <td><a href="main.jsp" class="menu_button"  target="main" onClick="showx('d')">信息統計</a></td>
		 </tr>
		 <tr style="display:none" id="d">
		  <td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
			  <a href="statisticByBook.jsp" target="main">圖書訂購信息統計</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
			  <a href="StatisticByBookServlet?type=page" target="main">圖書訂購信息簡表</a></td>
		     </tr>												
			</table>	
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="statisticStudentInfo.jsp" target="main">學生訂書信息表</a></td>
		     </tr>												
			</table>	
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="FacultyListServlet?type=studOrdBookSummary" target="main">學生訂書簡詳表</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="FacultyListServlet?type=studDeliverBook" target="main">學生領書記錄表</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="FacultyListServlet?type=studReceiveTimeRecord" target="main">學生時段信息記錄</a></td>
		     </tr>												
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="FacultyListServlet?type=statisticReceivePeriod" target="main">時段統計總表</a></td>
		     </tr>												
			</table> 
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="TransactionServlet" target="main">交易項目記錄表</a></td>
		     </tr>												
		    </table>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="WithdrawServlet" target="main">退書記錄表</a></td>
		     </tr>												
		    </table>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="RetailBookServlet?type=retailBookReport" target="main">零售記錄表</a></td>
		     </tr>												
		    </table>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="statisticPurchasingBook.jsp" target="main">代購記錄表</a></td>
		     </tr>												
		    </table>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		      <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		      <a href="TosSurveyServlet" target="main">調查問卷記錄表</a></td>
		     </tr>												
		    </table>
		  </td>
		 </tr>		
		</table>	
		
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		  <tr>
		    <td><a href="main.jsp" class="menu_button"  target="main" onClick="showx('l')">郵件通知</a></td>
		  </tr>
		  <tr  style="display:none" id="l">
		  <td>
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="StudentLackBookServlet" target="main">教材到貨通知</a>
		     </td>
		    </tr>												
		   </table>
		  
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="StudentLackBookServlet?type=overduePage" target="main">逾期領取教材通知</a>
		     </td>
		    </tr>												
		   </table>	
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="StudentLackBookServlet?type=supplementPage" target="main">補訂教材到貨通知</a>
		     </td>
		    </tr>												
		   </table>
		   
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="StudentLackBookServlet?type=drawbackPage" target="main">教材退款通知</a>
		     </td>
		    </tr>												
		   </table>
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="StudentLackBookServlet?type=changedPage" target="main">教材價格變動通知</a>
		     </td>
		    </tr>												
		   </table>		
		   
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="StudentLackBookServlet?type=teachingChangePage" target="main">更換教材通知</a>
		     </td>
		    </tr>												
		   </table>		
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="StudentLackBookServlet?type=unpaidOrderPage" target="main">未付款教材訂單的通知</a>
		     </td>
		    </tr>												
		   </table>		
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="StudentLackBookServlet?type=cancelPage" target="main">取消逾期未付款訂單通知</a>
		     </td>
		    </tr>												
		   </table>		
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="StudentLackBookServlet?type=preBookPage" target="main">預訂教材通知</a>
		     </td>
		    </tr>												
		   </table>		
		  </td>
		</table>

		<!--  報表列印  -->
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		 <tr>
		  <td><a href="main.jsp" class="menu_button"  target="main" onClick="showx('e')">書單列印</a></td>
		 </tr>
		 <tr  style="display:none" id="e">
		  <td>
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="stuOrdBooksReport.jsp" target="main">學員訂書報表</a></td>
		    </tr>												
		   </table>	
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		     <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		     <a href="FacultyListServlet?type=totalReport" target="main">綜合訂書報表</a></td>
		    </tr>												
		   </table> 
		  </td>
		 </tr>		
		</table>	

		<!--  系統管理  -->
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		 <tr>
		  <td><a href="main.jsp" class="menu_button"  target="main" onClick="showx('f')">系統管理</a></td>
		 </tr>
		 <tr style="display:none" id="f">
		  <td>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="SysConfTimeServlet?type=<%="view" %>" target="main">系統狀態設置</a></td>
		   </tr>												
		  </table>	
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="ReceiptBookTimeServlet" target="main">時段設置</a></td>
		   </tr>												
		  </table>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="SysConfTimeServlet?type=param" target="main">系數管理</a></td>
		   </tr>												
		  </table>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="LoginPageInfoServlet" target="main">登入版面訊息</a></td>
		   </tr>												
		  </table>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="QuestionServlet" target="main">常見問題設置</a></td>
		   </tr>												
		  </table>
		  </td>
		 </tr>		  	  		  
		</table>

		<!--  用戶管理  -->
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		 <tr>
		  <td><a href="main.jsp" class="menu_button"  target="main" onClick="showx('g')">用戶管理</a></td>
		 </tr>
		 <tr style="display:none" id="g">
		  <td>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		   <tr>
		    <td height="25">
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    <img src="images/pic_icon3.gif" width="5" height="5" border="0">&nbsp;
		    <a href="SysUserServlet" target="main">用戶列表</a></td>
		   </tr>												
		  </table>
		  </td>
		 </tr>		  	  		  
		</table>

		<!-- 退出管理 -->  
		<table width="100%" border="0" cellspacing="1" cellpadding="0">		  		  
		 <tr>
		  <td>
		  <a class="menu_button" onclick='{if(confirm("您確定退出管理嗎?")){return true;}return false;}' href="logout.jsp"  target="_top">退出管理</a>
		  </td>
		 </tr>	  		  
		</table>		
		</td>
		<td background="images/menu_rightbk2.jpg">&nbsp;</td>
		</tr>
		<tr>
		 <td width="7" height="7"><img src="images/menu_downleft.jpg" width="7" height="7" /></td>
		 <td height="7" background="images/menu_leftbk2.jpg"></td>
		 <td width="7" height="7"><img src="images/menu_rightdown.jpg" width="7" height="7" /></td>
		</tr>
		</table>
    </td>
  </tr>
  <tr>
    <td align="center"> 
      <!-- 3.0 -->
      <p>訂書管理系統  version 3.0</p>
    </td>
  </tr>
</table>
</body>
</html>
