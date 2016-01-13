<%@ page language="java" import="java.util.*,edu.must.tos.bean.*,edu.must.tos.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>學生訂書列表</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
javascript:window.history.forward(1);
function page(start){
	document.form1.start.value = start;
	var totalPages = parseInt(document.form1.totalPages.value);
	
	if(start == "page") {
		if(document.form1.instart.value.replace(/ /g,"") == "") {
			alert("頁碼不能為空！");
	    	document.form1.instart.focus();
			document.form1.instart.select();
	    	return;
		}
		if(isNaN(document.form1.instart.value)) {
			alert("請輸入整數!");
			document.form1.instart.focus();
			document.form1.instart.select();
			return;
		}
		if(parseInt(document.form1.instart.value) > totalPages) {
			alert("輸入的頁碼已超出最大頁,請重新輸入");
	    	document.form1.instart.focus();
			document.form1.instart.select();
	    	return;
		}
		document.form1.start.value = (parseInt(document.form1.instart.value)-1)*10;
	}
	document.form1.action = "StudentListServlet";
	document.form1.target = "_self";
	document.form1.submit();
}
//跳轉按鈕獲得焦點
function changeFocus() {
	document.form1.jump.focus();
}
// 頁面加載獲取焦點
function instartFocus() {
	document.form1.instart.focus();
}
//選擇學號
function selectStudentNo(studentNo) {
	document.form1.selectedStudentNo.value = studentNo;
}
//訂書
function orderBook(studentNo, applicantNo) {  
	//alert(studentNo+" "+applicantNo);
	window.parent.location.href = "FacultyListServlet?type=orderedBookSearch&studentNo="+studentNo+"&applicantNo="+applicantNo+"";
	//window.parent.location.href="StudentBookListServlet?studentNo="+studentNo+"";
}


//查看訂書信息
function searchOrderedBooks(studentNo, applicantNo) { 
	window.parent.location.href = "ConfirmOrderBookServlet?studentNo="+studentNo+"&applicantNo="+applicantNo+"&oprType=search";
}
//領取圖書
function receiveBooks(studentNo, applicantNo, oprStep) {
	//收費步驟
	if(oprStep == "step2"){
		//檢查該學員是否購買了圖書
		$.post(
			"ConfirmOrderBookServlet",
			{
				oprType: "checkOrder",
				studentNo: studentNo,
				applicantNo: applicantNo
			},
			function(result){
				if(result == 0){
					alert("該學員沒有未付款訂書記錄，不能進行此操作！");
					return false;
				}else if(result == 2){
					alert("該學員存在兩個未付款記錄，請進行檢查！");
					return false;
				}else if(result == 3){
					alert("請到補差價版面處理！");
					return false;
				}else{
					window.parent.location.href = "ConfirmOrderBookServlet?applicantNo="+applicantNo+"&studentNo="+studentNo+"&oprType=receive&oprStep="+oprStep+"";
				}
			}
		)
	}else {	//領書步驟
		//檢查該學員是否訂購了圖書
		$.post(
			"ConfirmOrderBookServlet",
			{
				oprType: "checkReceiveOrder",
				studentNo: studentNo,
				applicantNo: applicantNo
			},
			function(result){
				if(result == 0){
					alert("該學員沒有購書訂單，不能進行此操作！");
					return false;
				}else if(result == 2){
					alert("該學員的購書訂單還未收費或要補差價，請先收費或補差價操作！");
					return false;
				}else{
					if(result != 1){
						alert("訂書序號為" + result + "的書單需要補差價處理！");
					}
					window.parent.location.href = "ConfirmOrderBookServlet?applicantNo="+applicantNo+"&studentNo="+studentNo+"&result="+result+"&searchType=receive&oprType=receive&oprStep="+oprStep+"";
				}
			}
		)
	}	
}
 //列印收據
function printReceipt(studentNo, applicantNo) {
	$.post(
		"ViewReceiptServlet",
		{
			type: "search",
			studentNo: studentNo,
			applicantNo: applicantNo
		},
		function(result){
			if(result == 0){
				alert("該學員沒有未處理的訂單收據可列印!");
				return false;
			}else if(result == 1){
				window.showModalDialog("ViewReceiptServlet?type=show&studentNo="+studentNo+"&applicantNo="+applicantNo,"","resizable=no;dialogHeight=460px;dialogWidth=480px;location=no;menubar=no;toolbar=no;status=no");
			}else{
				window.open('ViewReceiptServlet?orderSeqNo='+result+'&studentNo='+studentNo+'&applicantNo='+applicantNo,'','width=780,height=600,scrollbars=yes');
				//document.form1.target = "_blank";
				//document.form1.action = "ViewReceiptServlet?orderSeqNo="+result+"&studentNo="+studentNo+"&applicantNo="+applicantNo+"";
				//document.form1.submit();
			}
		}
	)
}

function reprint(studNo, appNo){
	$.post(
		"ViewReceiptServlet",
		{
			type: "reprint",
			method: "searchRePrint",
			studentNo: studNo,
			applicantNo: appNo
		},
		function(result){
			if(result == 0){
				alert("該學員沒有已付的訂單收據可重印!");
				return false;
			}else{
				window.parent.location.href = "ViewReceiptServlet?applicantNo="+appNo+"&studentNo="+studNo+"&type=reprint";
			}
		}
	)
}
  
//查看學員詳細信息
function openDetail(theURL, winName, features) {
	window.open(theURL,winName,features);
}

//處理補差價
function calculateDiffer(studNo, applicantNo){
	$.post(
		"DifferenceServlet",
		{
			type: "checkOrdersPaidStatus",
			studNo: studNo,
			applicantNo: applicantNo
		},
		function(result){
			if(result == 0){
				alert("該學員還沒購書，不能進行此操作！");
				return false;
			}else if(result == 1){
				alert("該學員還有訂單未收，請進入‘收費’功能進行收費！");
				return false;
			}else{
				window.parent.location.href = "DifferenceServlet?type=view&applicantNo="+applicantNo+"&studNo="+studNo+"";
			}
		}
	)
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<% 
String period = (String)session.getAttribute("period");
LinkedHashMap studReceOrPaidPeriodList = (LinkedHashMap)session.getAttribute("studReceOrPaidPeriodList");
if(studReceOrPaidPeriodList == null && studReceOrPaidPeriodList.isEmpty()){
	studReceOrPaidPeriodList = new LinkedHashMap();
}
String oprType = (String)session.getAttribute("oprType");
List<Student> list = (List)session.getAttribute("studentList"); 
Student student = (Student)session.getAttribute("student");
double totalPages = Double.parseDouble(session.getAttribute("totalPages").toString());
String start = (String)session.getAttribute("start");
//delete the data in session about selected Books
session.removeValue("selectedBookList");
%>
<body onload="instartFocus();">
<form name="form1" action="" method="post" >
  <input type="hidden" name="selectedStudentNo" value="" />
  <%
  if(student.getStudentNo() != null) {
  %>
  <input type="hidden" name="studentNo" value="<%=student.getStudentNo() %>" />
  <%
  }
  if(student.getChineseName() != null){
  %>
  <input type="hidden" name="chineseName" value="<%=student.getChineseName() %>" />
  <%
  }
  if(student.getFacultyCode() != null) {
  %>
  <input type="hidden" name="facultyCode" value="<%=student.getFacultyCode() %>" />
  <%
  }
  if(student.getIdNo() != null) {
  %>
  <input type="hidden" name="idNo" value="<%=student.getIdNo() %>" />
  <%
  }
  if(student.getProgramCode() != null) {
  %>
  <input type="hidden" name="programCode" value="<%=student.getProgramCode() %>" />
  <%
  }
  %>
  <input type="hidden" name="searchType" value="orderStudent" />
  <input type="hidden" name="oprType" value="<%=oprType %>" />
  <input type="hidden" name="start" value="<%=start %>" />
  <input type="hidden" name="totalPages" value="<%=totalPages %>" />
  <input type="hidden" name="orderSeqNo" value="" />
  <table width="99.9%"  align="center" border="0" cellspacing="1" cellpadding="0" id="the-table">
  <caption style="margin:5px 0px;"><font size="3"><b>學 生 信 息</b></font></caption>
    <tr bgcolor="#C6D6FD">
      <td align="center" width="20%" height="25"><strong>學號</strong></td>
      <td align="center" width="7%"><strong>中文名</strong></td>
      <td align="center" width="8%"><strong>學院編號</strong></td>
      <td align="center" width="8%"><strong>課程編號</strong></td>
      <%
      if(oprType != null && oprType.equals("orderBooks")){
      %>
      <td align="center"  width="22%"><strong>聯系電話</strong></td>
      <%    	  
      }else if(oprType != null && oprType.equals("received")){
   	  %>
   	  <td align="center" width="22%"><strong>付款/領書時段</strong></td>
   	  <%
      }
      %>
      <td align="center" width="35%"><strong>操作</strong></td>
    </tr>
    <% 
    if(list != null) {
    	int i = 1;
    	for(Student stu : list){
    %>
    <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
      <td>
        <a href="#">
          <span onclick="openDetail('StudentDetailServlet?psStudentNo=<%=stu.getStudentNo() %>&psapplicantNo=<%=stu.getApplicantNo() %>','','width=700,height=400,toolbar=no,status=no,menubar=no,scrollbars=no,resizable=no')">
          <%
          if(stu.getStudentNo() != null){
        	  out.print(stu.getStudentNo());
          }else{
        	  out.print(stu.getApplicantNo());
          }
          %>
          </span>
        </a>
      </td>
      <td><%=stu.getChineseName() %></td> 
      <td><%=stu.getFacultyCode() %></td>   
      <td><%=stu.getProgramCode() %></td>
      <%
      if(oprType != null && oprType.equals("orderBooks")){
      %>
      <td><%=stu.getContactNo() %></td>
      <%
      }else if(oprType != null && oprType.equals("received")){
      %>
      <td>
      <%
      if(studReceOrPaidPeriodList != null && !studReceOrPaidPeriodList.isEmpty()){
    	  Iterator it = studReceOrPaidPeriodList.entrySet().iterator();
    	  while(it.hasNext()){
    		  Map.Entry entry = (Map.Entry)it.next();
    		  String key = entry.getKey().toString();
    		  String studNo = key.substring(0, key.indexOf("_"));
    		  String type = key.substring(key.indexOf("_")+1, key.length());
    		  if(studNo.equals(stu.getStudentNo())){
    			  if("R".equals(type)){
    				  out.print("<br>("+type+")"+entry.getValue().toString());
    			  }else{
    				  out.print("("+type+")"+entry.getValue().toString());
    			  }
    		  }
    	  }
      }
      %>
      </td>
      <%
      }
      %>
      <td align=center>
      <%
      if(oprType != null && oprType.equals("orderBooks")) {
      %>
        <input type="button" name="Submit2" value="訂書" onclick="orderBook('<%=stu.getStudentNo() %>', '<%=stu.getApplicantNo() %>');" />
      
        <input type="button" name="Submit2" value="訂書列表" onclick="openDetail('StudentBookServlet?psStudentNo=<%=stu.getStudentNo() %>&psApplicantNo=<%=stu.getApplicantNo() %>','','width=800,height=600,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no')" />
      
      <%
      } else if(oprType != null && oprType.equals("received")) {
      %>
        <input type="button" name="Submit1" value="列印收據" style="width:60px;padding:0px 0px;" onclick="printReceipt('<%=stu.getStudentNo() %>', '<%=stu.getApplicantNo() %>');" />
        <input type="button" name="Submit2" value="重印收據" style="width:60px;padding:0px 0px;" onclick="reprint('<%=stu.getStudentNo() %>', '<%=stu.getApplicantNo() %>');" />
        <input type="button" name="Submit3" value="收費" style="width:30px;padding:0px 0px;" onclick="receiveBooks('<%=stu.getStudentNo() %>', '<%=stu.getApplicantNo() %>', 'step2');" />
        <input type="button" name="Submit4" value="補差價" style="width:40px;padding:0px 0px;" onclick="calculateDiffer('<%=stu.getStudentNo() %>', '<%=stu.getApplicantNo() %>');" />
        <input type="button" name="Submit5" value="領書" style="width:30px;padding:0px 0px;" onclick="receiveBooks('<%=stu.getStudentNo() %>', '<%=stu.getApplicantNo() %>', 'step3');" />
      <%
      } else if(oprType != null && oprType.equals("search")) {
      %>
        <input type="button" name="Submit2" value="查看" onclick="searchOrderedBooks('<%=stu.getStudentNo() %>', '<%=stu.getApplicantNo() %>');" />
      <%
      } 
      %>
      </td>
    </tr>
    <%
    	i++;
    	}
    }
    %>
    <tr>
      <td align="center" colspan="6">
      <%
      List<PageBean> pagelist = (List)session.getAttribute("page");
      for(PageBean pageBean : pagelist){
    	  if (pageBean.getPageType().equals("prev")){
    		  out.print(" <a href='javascript:page("+pageBean.getOffset()+");' >上一頁</a>&nbsp;");
    	  }
    	  if (pageBean.getPageType().equals("cur")){
    		  out.print("<a href='javascript:page("+pageBean.getOffset()+");' ><font color=#d20000 ><b> "+pageBean.getPage()+"</b></font></a>&nbsp;");
    	  }
    	  if (pageBean.getPageType().equals("pages")){
    		  out.print(" <a href='javascript:page("+pageBean.getOffset()+");' >"+pageBean.getPage()+"</a>&nbsp;");
    	  }
    	  if (pageBean.getPageType().equals("next")){
    		  out.print("<a href='javascript:page("+pageBean.getOffset()+");' >下一頁</a>&nbsp;");
    	  }
      }
      %>
        <label>
          &nbsp;&nbsp;
          <input type="text" name="instart" id="len" onchange = "changeFocus();" />
          &nbsp;
          <input type="button" name="jump" onclick="page('page')" value="跳轉" />
        </label>
      </td>
    </tr>
  </table>
</form>
</body>
</html>

