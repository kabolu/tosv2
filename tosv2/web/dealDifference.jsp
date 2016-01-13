<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*,edu.must.tos.bean.*,java.text.*,java.math.BigDecimal" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>補差價</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.table{
	border: 1px solid #FFA54F;
}
.table th{
	background-color: #FFA54F;
}
.table td{
	background-color: #FAFAD2;
	border-bottom: 1px solid #FFA54F;
}
-->
</style>
<script language="javascript">
$(document).ready(function(){
	$('.info').hide();
	$('#deal').hide();
	
	if($('#dealMop').val() == "true" && $('#dealRmb').val() == "true"){
		$('#deal').show();
	}else if($('#dealMop').val() == "true" || $('#dealRmb').val() == "true"){
		$('#deal').show();
	}else{
		if($('#flag').val() == "true"){
			$('#deal').show();
		}
	}
	
	$('#deal').click(function(){
		var differ = $('#differ').val();
		var paidAmount = $('#paidAmount').val();
		var studNo = $('#studNo').val();
		var appNo = $('#appNo').val();
		var mopRate = $('#mopRate').val();
		var rmbRate = $('#rmbRate').val();
		var hkdRate = $('#hkdRate').val();
		var currency = "MOP";
		var netCurrency = $('input[@name=currencyBtn][checked]').val();
		if(netCurrency == undefined){
			alert("請選擇付款幣種!");
			return false;
		}
		$('#deal').attr("disabled", true);
		$.post(
			"DifferenceServlet",
			{
				type: "dealDifference",
				studNo: studNo,
				applicantNo: appNo,
				differ: differ,
				paidAmount: paidAmount,
				currency: currency,
				netCurrency: netCurrency,
				mopRate: mopRate,
				rmbRate: rmbRate,
				hkdRate: hkdRate
			},
			function(result){
				var chauid = "";
				var upddate = "";
				var commit = "";
				$(result).find("results result").each(function(){
					chauid = $(this).find("chauid").text();
					upddate = $(this).find("upddate").text();
					commit = $(this).find("commit").text();
				})				
				if(commit == 1){
					$('.msg').text("交易成功！請列印補差價收據！");
					$('.chauid').text(chauid);
					$('.upddate').text(upddate);
					$('input[@name=chauidValue]').attr("value", chauid);
					$('input[@name=upddateValue]').attr("value", upddate);
					$('#deal').hide();
					$('.info').show();
				}else{
					alert("交易失敗！");
				}
			}
		)
	})
	
	$('input[@name=adjustButton]').click(function(){
		var studNo = $('#studNo').val();
		var appNo = $('#appNo').val();
		var item = $('#item').val();
		var currency = $('#currency').val();
		var cause = $('#cause').val();
		var remarks = $('#remarks').val();
		var dealMop = $('#dealMop').val();
		var dealRmb = $('#dealRmb').val();

		var dealHkd = $('#dealHkd').val();
		
		var seqNo = $('#orderSeqNo').val();
		var mopRate = $('#mopRate').val();
		var rmbRate = $('#rmbRate').val();
		var hkdRate = $('#hkdRate').val();
		if(seqNo == ""){
			alert("請選擇訂單編號！");
			return false;
		}
		if(item == 0){
			alert("請選擇調整項目！");
			return false;
		}
		var currencyType = 1;   //MOP
		if(currency == ""){
			alert("請選擇貨幣幣種！");
			return false;
		}else {
			if(currency == "RMB"){
				currencyType = 2;   //RMB
			}
		}
		if(cause == ""){
			alert("金額不能為空，請輸入！");
			$('#cause').focus();
			return false;
		}else{
			if(isNaN(cause)){
				alert("請填入正確的金額值！");
				$('#cause').attr("value", '');
				$('#cause').focus();
				return false;
			}else{
				if(cause == 0){
					alert("調整金額為零，不作收費項目！");
					$('#cause').attr("value", '');
		            $('#item')[0].selectedIndex = 0;
		            $('#currency')[0].selectedIndex = 0;
					return false;
				}
			}
		}
		var i = parseInt($('#i').val(), 10) + 1;
		var differMop = $('.differMop').text();
		var differRmb = $('.differRmb').text();
		var differHkd = $('.differHkd').text();
		$.post(
			"TransactionServlet?d="+Math.random(),
			{
				type : "saveSession",
				i : i,
				orderSeqNo : seqNo,
				studNo : studNo,
				appNo : appNo,
				item : item,
				rmbRate: rmbRate,
				hkdRate: hkdRate,
				currency : currency,
				cause : cause,
				remarks : remarks
			},
			function(){

				$('#i').attr("value", i);
				$('.table').append('<tr>'+
				'<td align="center">'+seqNo+'</td>'+
				'<td>原因：'+item+'</td>'+
				'<td>幣種：'+currency+'</td>'+
				'<td>金額：'+cause+'</td>'+
				'<td>備注：'+remarks+'</td>'+
				''+
				'<td align=right><input type="button" value=" X " onclick="delt(this, '+i+', '+currencyType+', '+cause+')"></td></tr>');
				if(currency == "MOP"){
					
					$('.differMop').text(parseFloat(differMop) + parseFloat(cause));
					$('.differRmb').text(Math.round(parseFloat(differRmb) + parseFloat(cause) * parseFloat(rmbRate)));
					$('.differHkd').text(Math.round(parseFloat(differHkd) + parseFloat(cause) * parseFloat(hkdRate)));
				}else{
					$('.differMop').text(Math.round(parseFloat(differMop) + parseFloat(cause) * parseFloat(rmbRate)));
					$('.differRmb').text(parseFloat(differRmb) + parseFloat(cause));
				}
				$('#cause').attr("value", '');
		        $('#item')[0].selectedIndex = 0;
		        $('#currency')[0].selectedIndex = 0;
		        
		        if(dealRmb == "false" && dealMop == "false" && dealHkd == "false"){
		        	$('#deal').show();
		        }
			}
		)
	})
})
function receiptPrint(){
	var studNo = $('#studNo').val();
	var appNo = $('#appNo').val();
	var chauid = $('input[@name=chauidValue]').val();
	var upddate = $('input[@name=upddateValue]').val();
	var currency = "MOP";
	var netCurrency = $('input[@name=currencyBtn][checked]').val();
	if(netCurrency == undefined)
		currency = "MOP";
	document.printForm.target = "_blank";
	document.printForm.action = "DifferenceServlet?type=printDifferReceipt&upddate="+upddate+"&netCurrency="+netCurrency+"&currency="+currency+"&chauid="+chauid+"&studNo="+studNo+"&applicantNo="+appNo+"";
	document.printForm.submit();
}
function itemListener(type, val){
	var seqNo = $('#orderSeqNo').val();
	if(type == "item"){
		if(val == '保管費'){
			if(seqNo == ""){
				alert("請選擇訂單編號！");
				return false;
			}
			$.post(
				"DifferenceServlet",
				{
					type: "keepingFee",
					seqNoStr: seqNo
				},
				function (data){
					$('#cause').attr("value", data);
				}
			)
		}
	}else if(type == "seqNo"){
		var item = $('#item').val();
		if(val != ""){
			if(item == '保管費'){
				$.post(
					"DifferenceServlet",
					{
						type: "keepingFee",
						seqNoStr: val
					},
					function (data){
						$('#cause').attr("value", data);
					}
				)
			}
		}
	}
}
function back(){
	document.backForm.submit();
}
function delt(obj, i, t, cause){
	var differMop = $('.differMop').text();
	var differRmb = $('.differRmb').text();
	var differHkd = $('.differHkd').text();
	var rmbRate = $('#rmbRate').val();
	var mopRate = $('#mopRate').val();
	var hkdRate = $('#hkdRate').val();
	$.post(
		"TransactionServlet",
		{
			type : "removeSession",
			i : i 
		},
		function(){
			$(obj).parent().parent().remove();
			if(t == 1){
				$('.differMop').text(parseFloat(differMop) - parseFloat(cause));
				$('.differRmb').text(parseFloat(differRmb) - Math.round(parseFloat(cause) * parseFloat(rmbRate)));
				$('.differHkd').text(parseFloat(differHkd) - Math.round(parseFloat(cause) * parseFloat(hkdRate)));
			}else{
				$('.differRmb').text(parseFloat(differRmb) - parseFloat(cause));
				$('.differMop').text(parseFloat(differMop) - Math.round(parseFloat(cause) * parseFloat(rmbRate)));
			}
			if($('.differMop').text() == 0 && $('.differRmb').text() == 0){
				$('#deal').hide();
			}
		}
	)
}
function openWindow(orderSeqNo){
	window.open('OrderDetailServlet?orderSeqNo='+orderSeqNo,'','width=600,height=300,scrollbars=yes');
}
</script>
<% if (session.getAttribute("userId") == null) {%>
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
<input type="hidden" value="0" name="i" id="i">
<%
double paidamount = 0;
if(request.getAttribute("paidamount") != null){
	paidamount = (Double)request.getAttribute("paidamount");
}
String currency = null;
if(request.getAttribute("currency") != null){
	currency = request.getAttribute("currency").toString();
}
Order order = null;
String chauid = "";
String upddate = "";
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
if(request.getAttribute("order") != null){
	order = (Order)request.getAttribute("order");
	chauid = order.getChaUid();
	upddate = dateFormat.format(order.getUpdDate());
}
List stuDetList = null;
if(request.getAttribute("stuDetList") != null){
	stuDetList = (List)request.getAttribute("stuDetList");
}
Student stu = null;
Program pro = null;
String studNo = "";
if(stuDetList != null ) {
	stu = (Student)stuDetList.get(0);
	if(stu!=null){
		if(stu.getStudentNo() != null){
	    	studNo = stu.getStudentNo();
	    }else{
	    	studNo = stu.getApplicantNo();
	    }
	}
	pro = (Program)stuDetList.get(2);
}
List<Transaction> itemFeeList = null;
if(request.getAttribute("itemFeeList") != null){
	itemFeeList = (List)request.getAttribute("itemFeeList");
}
List<Transaction> rePrintFeeList = null;
if(request.getAttribute("rePrintFeeList") != null){
	rePrintFeeList = (List)request.getAttribute("rePrintFeeList");
}
List<Difference> differList = null;
if(request.getAttribute("differList") != null){
	differList = (List)request.getAttribute("differList");
}
String currencyMop = "", currencyRmb = "";
double mopPaidAmount = 0, rmbPaidAmount = 0;
double mopShouldPaid = 0, rmbShouldPaid = 0;
double mopDiffer = 0, rmbDiffer = 0;

List<Integer> seqNoList = new ArrayList();
if(!differList.isEmpty()){
	for(Difference d : differList){
		if("MOP".equals(d.getPaidcurrency())){
			currencyMop = d.getPaidcurrency();
			mopPaidAmount += d.getPaidAmount();
			mopShouldPaid += d.getShoulePayAmount();
			mopDiffer += d.getDifference();
		}else{
			currencyRmb = d.getPaidcurrency();
			rmbPaidAmount += d.getPaidAmount();
			rmbShouldPaid += d.getShoulePayAmount();
			rmbDiffer += d.getDifference();
		}
		seqNoList.add(d.getOrderSeqNo());
	}
}
boolean dealMop = false, dealRmb = false;
if(request.getAttribute("dealMop") != null){
	dealMop = (Boolean)request.getAttribute("dealMop");
}
if(request.getAttribute("dealRmb") != null){
	dealRmb = (Boolean)request.getAttribute("dealRmb");
}
SysConfig adjustConfig = null;
if(request.getAttribute("adjustConfig") != null){
	adjustConfig = (SysConfig)request.getAttribute("adjustConfig");
}
boolean flag = false;
if(!differList.isEmpty()){
	for(Difference d : differList){
		if(d.getDifference() != 0){
			flag = true;
			break;
		}
	}
}
String isNewStud = (String)request.getAttribute("isNewStud");
String rmbRate = null;
if(request.getAttribute("curInRMBRate") != null){
	rmbRate = (String)request.getAttribute("curInRMBRate");
}
String hkdRate = null;
if(request.getAttribute("curInHKDRate") != null){
	hkdRate = (String)request.getAttribute("curInHKDRate");
}

String curInRMB = (String)request.getAttribute("curInRMBRate");
String curInHKD = (String)request.getAttribute("curInHKDRate");
Double curInrmbRate = Double.parseDouble(curInRMB);
Double curInhkdRate = Double.parseDouble(curInHKD);

%>
<%--<input type="hidden" name="rmbRate" id="rmbRate" value="<%=rmbRate %>">--%>
<%--<input type="hidden" name="mopRate" id="mopRate" value="<%=mopRate %>">--%>
<input type="hidden" name="rmbRate" id="rmbRate" value="<%=curInrmbRate %>">
<input type="hidden" name="mopRate" id="mopRate" value="<%=1 %>">
<input type="hidden" name="hkdRate" id="hkdRate" value="<%=curInhkdRate %>">

<input type="hidden" name="flag" id="flag" value="<%=flag %>">
<input type="hidden" name="dealMop" id="dealMop" value="<%=dealMop %>">
<input type="hidden" name="dealRmb" id="dealRmb" value="<%=dealRmb %>">
<form action="" method="post" name="printForm"></form>
<table width="90%" border="1" cellpadding="0" cellspacing="0" align="center" class="table">
  <tr background="#000">
    <th align="center" width="35%" height="35" colspan="2"><strong>學號</strong></th>
    <th align="center" width="30%" colspan="2"><strong>姓名</strong></th>
    <th align="center" width="35%" colspan="2"><strong>課程</strong></th>
  </tr>
  <% if(stu != null && pro != null) {%>
  <tr>
    <td height="35" colspan="2" style="border-right: 1px solid #FFA54F;" align="center">
      <%=studNo %>
      <input type="hidden" name="studNo" id="studNo" value="<%=stu.getStudentNo() %>">
      <input type="hidden" name="appNo" id="appNo" value="<%=stu.getApplicantNo() %>">
    </td>
    <td colspan="2" style="border-right: 1px solid #FFA54F;" align="center">
    <%
    if(stu.getChineseName() != null){
    	out.print(stu.getChineseName());
    }else{
    	out.print(stu.getEnglishName());
    }
    %>
    </td>
    <td align="center" colspan="2"><%=pro.getChineseName() %></td>
  </tr>
  <%} %>
  <tr>
    <td colspan="6" align="left" height="35">
      &nbsp;&nbsp;&nbsp;&nbsp;<b>補差價信息</b>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <%
      if(!dealMop && !dealRmb){
      %>
	  <label style="color:red;font-size:16px;font-weight: bold;">不需補差價</label>
	  <%
	  }
      %>
    </td>
  </tr>
  <%
  if("Y".equals(isNewStud)){
  %>
  <tr>
    <td height="35" style="border-right: 1px solid #FFA54F;">繳交書費：</td>
    <td colspan="5">
    <%
    if(paidamount != 0)
    	out.print(currency + ":" + paidamount);
    else
    	out.print("該學員未繳交書費！");
    %>
    </td>
  </tr>
  <%
  }
  %>
  
  <%
  double differMop = 0, differRmb = 0, differHkd = 0;
  if(differList != null && !differList.isEmpty()){
  %>
  <tr>
    <th height="30">訂單編號</th>
    <th>付款幣種</th>
    <th>已付金額</th>
    <th>需付金額</th>
    <th colspan="2">補差價</th>
  </tr>
  <%
      for(Difference d : differList){
    	  differMop = new BigDecimal(Double.toString(differMop)).add(new BigDecimal(Double.toString(d.getDifferenceMop()))).doubleValue();
  %>
  <tr>
    <td align="center" height="30">
      <input type="button" name="orderSeqNoVlaue" value="<%=d.getOrderSeqNo() %>" onClick="openWindow('<%=d.getOrderSeqNo() %>');"/>
    </td>
    <td align="center"><%=d.getPaidcurrency() %></td>
    <td align="center"><%=d.getPaidAmount() %></td>
    <td align="center"><%=new BigDecimal(Double.toString(d.getShoulePayAmount())).doubleValue() %></td>
    <td align="center" colspan="2"><%=d.getDifference() %></td>
  </tr>
  <%
      }
  }
  if(itemFeeList != null && !itemFeeList.isEmpty()){
	  for(Transaction t : itemFeeList){
  %>
  <tr>
    <td align="center" height="30"><%=t.getOrderSeqNo() %>(<%=t.getPaidMentType() %>)</td>
    <td align="center"><%=t.getPaidCurrency() %></td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center" colspan="2">0.0</td>
  </tr>
  <%
      }
  }
  if(rePrintFeeList != null && !rePrintFeeList.isEmpty()){
	  for(Transaction t : rePrintFeeList){
  %>
  <tr>
    <td align="center" height="30"><%=t.getOrderSeqNo() %>(<%=t.getPaidMentType() %>)</td>
    <td align="center"><%=t.getPaidCurrency() %></td>
    <%
    if("Y".equals(t.getStatus())){
    %>
    <td align="center"><%=t.getPay() %></td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center" colspan="2">0.0</td>
    <%	
    } else {
    	differMop += t.getPay();
		differRmb = new BigDecimal(differRmb).add(new BigDecimal(Double.valueOf(rmbRate)).multiply(new BigDecimal(Double.valueOf(t.getPay()))))
				.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    %>
    <td align="center">0.0</td>
    <td align="center"><%=t.getPay() %></td>
    <td align="center" colspan="2"><%=t.getPay() %></td>
    <%
    }
    %>
    
  </tr>
  <%
	  }
  }
  %>
</table>
<table width="90%" border="0" cellpadding="0" cellspacing="0" align="center" style="border: 1px solid #FFA54F;">
  <tr bgcolor="#FAFAD2">
    <td height="30" colspan="4" width="47%">&nbsp;</td>
	<td colspan="2" width="17%">
	  <input type="radio" name="currencyBtn" value="MOP" class="radio" <%if("MOP".equals("MOP")){out.print("checked");} %>>
	  MOP:
	  <%
	  differMop = new BigDecimal(differMop).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	  if(differMop > 0){ 
	  %>
	  <label class="differMop" style="color:blue;font-size:14px;font-weight:bold;border-bottom:1px solid #D2691E;"><%=differMop %></label>
	  <%}else{%>
	  <label class="differMop" style="color:#EE0000;font-size:14px;font-weight:bold;border-bottom:1px solid #D2691E;"><%=differMop %></label>
	  <%} %>
	</td>
	<td width="18%">
	  <input type="radio" name="currencyBtn" value="RMB" class="radio" >
	  RMB:
	  <%
	  differRmb = new BigDecimal(differMop*curInrmbRate).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	  if(differRmb > 0){ 
	  %>
	  <label class="differRmb" style="color:blue;font-size:14px;font-weight:bold;border-bottom:1px solid #D2691E;"><%=differRmb %></label>
	  <%}else{%>
	  <label class="differRmb" style="color:#EE0000;font-size:14px;font-weight:bold;border-bottom:1px solid #D2691E;"><%=differRmb %></label>
	  <%} %>
	</td>
	<td width="18%">
	  <input type="radio" name="currencyBtn" value="HKD" class="radio" >
	  HKD:
	  <%
	  differHkd = new BigDecimal(differMop*curInhkdRate).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	  if(differHkd > 0){ 
	  %>
	  <label class="differHkd" style="color:blue;font-size:14px;font-weight:bold;border-bottom:1px solid #D2691E;"><%=differHkd%></label>
	  <%}else{%>
	  <label class="differHkd" style="color:#EE0000;font-size:14px;font-weight:bold;border-bottom:1px solid #D2691E;"><%=differHkd%></label>
	  <%} %>
	</td>
  </tr>
</table>
<table width="90%" border="0" cellpadding="0" cellspacing="0" align="center">
  <tr class="info">
    <td colspan="6" height="30" align="center">
      <label class="msg" style="color:red;">已完成交易！</label>&nbsp;&nbsp;&nbsp;&nbsp;
      操作員：<label class="chauid" style="color:#123456;"><%=chauid %></label>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="hidden" name="chauidValue" value="<%=chauid %>">
      操作日期：：<label class="upddate" style="color:#123456;"><%=upddate %></label>
      <input type="hidden" name="upddateValue" value="<%=upddate %>">
    </td>
  </tr>
  <tr>
    <td colspan="6" align="center" height="30">
      <input type="button" name="deal" id="deal" value=" 確定交易 ">&nbsp;&nbsp;
      <input type="button" name="receiptPrint" value="補差價收據" onClick="receiptPrint();">
      &nbsp;&nbsp;
      <input type="button" name="back" value=" 返 回 " onClick="back();">
    </td>
  </tr>
</table>

<br>
<br>

<table width="90%" border="0" cellpadding="0" cellspacing="0" align="center">
  <tr bgcolor="#FAFAD2">
    <td height="35" width="10%" style="border: 1px solid #FFA54F;">&nbsp;調整金額：</td>
    <td width="25%" style="border: 1px solid #FFA54F;">&nbsp;訂單序號：
      <select name="orderSeqNo" id="orderSeqNo" onChange="itemListener('seqNo',this.value);">
        <%
        if(seqNoList != null && seqNoList.size() == 1){
        	for(Integer s : seqNoList){
   		%>
   		<option value="<%=s %>"><%=s %></option>
   		<%
        	}
        }else if(seqNoList != null && seqNoList.size() > 1){
        	for(Integer s : seqNoList){
        %>
        <option value="<%=s %>"><%=s %></option>
        <%
        	}
        }
        %>
      </select>
    </td>
    <td style="border: 1px solid #FFA54F;" width="20%">&nbsp;原因：
      <select name="item" id="item" onChange="itemListener('item',this.value);">
        <option value="0">==請選擇==</option>
        <option value="保管費">保管費</option>
        <%
        if(adjustConfig.getScValue1() != null){
        	String adjustItem[] = adjustConfig.getScValue1().split("/");
        	for(int i=0; i<adjustItem.length; i++){
        		String item = adjustItem[i];
        %>
        <option value="<%=item %>"><%=item %></option>
        <%
        	}
        }
        %>
      </select>
    </td>
    <td style="border: 1px solid #FFA54F;" width="20%">&nbsp;幣種：
      <select name="currency" id="currency">
        <option value="MOP">MOP</option>
      </select>
    </td>
    <td style="border: 1px solid #FFA54F;" width="25%">
      <input type="text" name="cause" id="cause" style="width:150px;height:19px;" maxlength="7">
    </td>
  </tr>
  <tr bgcolor="#FAFAD2">
    <td style="border: 1px solid #FFA54F;" height="35">&nbsp;備注：</td>
    <td style="border: 1px solid #FFA54F;" colspan="4">
      <input type="text" name="remarks" id="remarks" style="width:250px;height:19px;" maxlength="50">
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" name="adjustButton" value="確 定">
    </td>
  </tr>
</table>
<form action="StudentListServlet" method="post" name="backForm">
<%
String oprType = (String)session.getAttribute("oprType");
Student student = (Student)session.getAttribute("student");
double totalPages = Double.parseDouble(session.getAttribute("totalPages").toString());
String start = (String)session.getAttribute("start");
if(student.getStudentNo()!=null) {
%>
  <input type="hidden" name="studentNo" value="<%=student.getStudentNo() %>" />
<%
}
if(student.getChineseName()!=null){
%>
  <input type="hidden" name="chineseName" value="<%=student.getChineseName() %>" />
<%
} 
if(student.getFacultyCode()!=null) {
%>
  <input type="hidden" name="facultyCode" value="<%=student.getFacultyCode() %>" />
<%
} 
if(student.getIdNo()!=null) {
%>
  <input type="hidden" name="idNo" value="<%=student.getIdNo() %>" />
<%
} 
if(student.getProgramCode()!=null) {
%>
  <input type="hidden" name="programCode" value="<%=student.getProgramCode() %>" />
<%
}
%>
  <input type="hidden" name="back" value="back" />
  <input type="hidden" name="searchType" value="orderStudent" />
  <input type="hidden" name="oprType" value="<%=oprType %>" />
  <input type="hidden" name="start" value="<%=start %>" />
  <input type="hidden" name="totalPages" value="<%=totalPages %>" />
</form>
</body>
</html>
