<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>訂購圖書</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<style type="text/css">
<!--
.textlen {
	height: 18px;
	width: 20px;
}
.btnlen {
	height: 19px;
	width: 20px;
	padding-bottom: 0px;
	padding-top: 0px;
}
-->
</style>
<script language="javascript">
$(document).ready(function(){
	$('#other').hide();
	$('select[@name=facultyCode]').change(function(){
		var other = $(this).val();
		if(other == "other"){
			$('#other').attr("value", '');
			$('#other').show();
			$('#other').focus();
		}else{
			$('#other').attr("value", '');
			$('#other').hide();
		}
	})
	var flag = $('select[@name=facultyCode]').val();
	if(flag == "other"){
		$('#other').attr("value", '');
		$('#other').show();
		$('#other').focus();
	}
})

//查看可訂書
function searchAvailableBooks() {
	var facultyCode = document.searchAvailableBook.facultyCode.value;
	
	var other = document.searchAvailableBook.other.value;
    if(facultyCode == "") {
    	alert("請選擇學院！");
     	return false;
    }else{
    	if(facultyCode == "other"){
    		if(other == ""){
    			alert("請輸入ISBN關鍵字進行搜索！");
    			return false;
    		}
    	}
    }
    document.searchAvailableBook.action = "StudentBookListServlet";
    document.searchAvailableBook.target = "_parent";
    document.searchAvailableBook.submit();
}
//查看已訂圖書
function orderedBookList() {
	var studentNo = document.orderedBook.studentNo.value;
	var applicantNo = document.orderedBook.applicantNo.value;
    window.open("OrderedBookListServlet?studentNo="+studentNo+"&applicantNo="+applicantNo+"","bookorder","Width=750,Height=500,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no");
}
//加1
function plus(id) {
	var count = parseInt(document.getElementById(id).value);
    document.getElementById(id).value=count+1;
}
//減1
function minus(id) {
	var count = parseInt(document.getElementById(id).value);
   	if(count > 0) {
   		document.getElementById(id).value=count-1;
	} else {
	 	alert("訂書數不能小於零！");
	}	
}
//selected book
function addBook(studentNo, isbn, courseCode, majorCode, year, mopFuturePrice,mopNetPrice,rmbFuturePrice, title, id, supplement){
	var confirmId = parseInt(id, 10)-parseInt(2, 10);
	var confirmQty = parseInt(document.getElementById('confirm'+confirmId).value);
	if(confirmQty==0){
    	alert("請填寫數量");
    	return;
   	}
   	document.form1.studentNo.value = studentNo;
   	document.form1.isbn.value = isbn;
   	document.form1.courseCode.value = courseCode;
   	document.form1.majorCode.value = majorCode;
   	document.form1.year.value = year;
   	document.form1.mopFuturePrice.value = mopFuturePrice;
   	document.form1.mopNetPrice.value= mopNetPrice;
   	document.form1.rmbFuturePrice.value = rmbFuturePrice;
   	document.form1.title.value = title;
   	document.form1.confirmQty.value = confirmQty;
   	document.form1.supplement.value = supplement;
   	document.form1.submit(); 
} 
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<%
List<Faculty> facultyList = (List)session.getAttribute("facList");
List stuDetList = (List)session.getAttribute("stuDetList");
List availableBooklist = (List)session.getAttribute("availableBooklist");
List selectedBookList = (List)session.getAttribute("selectedBookList");

String facultyCode = (String)session.getAttribute("facultyCode");
Student stu = null;
Program pro = null;
String studentNo = null;
String applicantNo = null;
if(stuDetList != null && stuDetList.size() >= 3 ) {
	stu = (Student)stuDetList.get(0);
	studentNo = stu.getStudentNo();
	applicantNo = stu.getApplicantNo();
	pro = (Program)stuDetList.get(2);
}

String retail = null;
if(session.getAttribute("retail") != null){
	retail = (String)session.getAttribute("retail");
}

List<Book> bookList = null;
List<Price> priceList = null;
List<Course> courseList = null;
List<BookTempl> bookTemplList = null;
if(availableBooklist != null && availableBooklist.size()>=2) {
	bookList = (List)availableBooklist.get(0);
	priceList = (List)availableBooklist.get(1);
	courseList = (List)availableBooklist.get(2);
	bookTemplList = (List)availableBooklist.get(3);
}
%>
<body>
<table width="450" border="0" cellpadding="0" cellspacing="0" >
  <% if(stu != null && pro != null) { %>
  <tr>
    <td align="center" height="20"><strong>學號</strong></td>
    <td align="center"><strong>姓名</strong></td>
    <td align="center"><strong>課程</strong></td>
  </tr>
  <tr>
    <td height="20" align="center">
    <%if(stu.getStudentNo() != null){out.print(stu.getStudentNo());}else{out.print(stu.getApplicantNo());} %>
    </td>
    <td align="center"><%=stu.getChineseName() %></td>
    <td align="center"><%=pro.getChineseName() %></td>
  </tr>
  <%} %>
  <% if(retail != null){ %>
  <tr>
    <td align="left" height="20" class="blackBold14px">
      <strong>零 售 訂 書</strong>
    </td>
  </tr>
  <%} %>
</table>

<table width="99.9%" align="center" border="0" cellspacing="1" cellpadding="1" >
  <tr bgcolor="#FFFBEF">
    <td colspan="9" align="left">
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td width="70%">
            <form action="" name="searchAvailableBook" target="" method="post" >
            <% if(stu!=null) { %>
            <input type="hidden" name="studentNo" value="<%=studentNo %>">
            <input type="hidden" name="applicantNo" value="<%=applicantNo %>">
            <%} %>
              <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="15%" height="28">
                    <select name="facultyCode">
		            <%
		            if(facultyList != null && facultyList.size()>0) {
		            	for(Faculty fa : facultyList) {
		            		if(facultyCode != null && facultyCode.equals(fa.getFacultyCode())) {
		            		%>
				             <option value="<%=fa.getFacultyCode() %>" selected><%=fa.getChineseName() %></option>
				            <%
				            continue;
				            }
				            %>
				             <option value="<%=fa.getFacultyCode() %>" ><%=fa.getChineseName() %></option>
			        <%
			        	}
		            }
		            %>
		            </select>
		          </td>
		          <td width="30%"><input type="text" name="other" id="other" class="inp"></td>
		          <td width="10%" align="right"><input type="button" name="Submit4" value="確 定" onClick="searchAvailableBooks();" ></td>
		          <td width="45%" align="center"><span class="blackBold16px">您可以訂購以下圖書</span></td>
		        </tr>
		      </table>
		    </form>
		  </td>
		  <td width="30%" align="center" >
		    <form action="" name="orderedBook" target="_blank">
		      <input type="hidden" name="studentNo" value="<%=studentNo %>">
		      <input type="hidden" name="applicantNo" value="<%=applicantNo %>">
		      <%
		      if(!"RETAIL".equals(retail)){
		      %>
	    	  <input type="button" name="viewButton" value="查看已訂圖書" onClick="orderedBookList()" />
	    	  <%
	    	  }
		      %>
		    </form>
		  </td>
		</tr>
	  </table>
    </td>
  </tr>
  <tr bgcolor="#C6D6FD">
    <td width="15%" align="center">編號</td>
    <td width="35%" align="center">書名</td>
    <td width="5%"  align="center">補訂標識</td>
    <td width="5%"  align="center">科目編號</td>
    <td width="5%"  align="center">學院編號</td>
    <td width="5%"  align="center">預估價格(MOP)</td>
    <td width="5%"  align="center">實際價格(MOP)</td>
    <td width="10%" align="center">確認</td>
    <td width="15%" align="center">操作</td>
  </tr>
  <%
  if(bookList != null && priceList!=null && courseList!=null && bookTemplList!=null) {
	  int m=0;
	  for(int i=0; i<bookList.size(); i++) {
		  Book book = (Book)bookList.get(i);
		  Price price = (Price)priceList.get(i);
		  Course course = (Course)courseList.get(i);
		  BookTempl bookTempl = (BookTempl)bookTemplList.get(i);
		  boolean exist = false;
		  if(selectedBookList!=null){
			  for(int j=0; j<selectedBookList.size(); j++) {
				  OrderInfo info = (OrderInfo)selectedBookList.get(j);
				  if(book.getIsbn().equals(info.getIsbn())) {
					  exist = true;
        		  }
              }
		  }
		  if(!exist){
			  double mopPrice = 0;
			  double rmbPrice = 0;
			  double netmopPrice = 0;
			  String shownetmopPrice = "";
			  if(price.getMopNetPrice()==0 || price.getMopFuturePrice() == price.getFuturePrice()){
				  shownetmopPrice = "";
				  netmopPrice = mopPrice = price.getMopFuturePrice();
			  }else{
				  shownetmopPrice = ""+price.getMopNetPrice();
				  netmopPrice =price.getMopNetPrice();
			  	  mopPrice = price.getMopFuturePrice();
			  }
			  if(price.getRmbNetPrice()==0){
				  rmbPrice = price.getRmbFuturePrice();
			  }else{
				  rmbPrice = price.getRmbNetPrice();
			  }
  %>
  
  <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td align="left" height="55">
      <a href="BookDetailServlet?isbn=<%=book.getIsbn() %>" target="_blank"><%=book.getIsbn() %></a>
    </td>
    <td align="left"><%=book.getTitle() %></td>
    <td align="left"><%if("Y".equals(book.getSupplement())){ out.print("是"); }else if("N".equals(book.getSupplement())){ out.print("否");}else{out.print("N/A");} %></td>
    <td align="left"><%=course.getCourseCode()%></td>
    <td align="left"><%=bookTempl.getFacultyCode() %></td>
    <td align="left"><%=mopPrice %></td>
    <td align="left"><%=shownetmopPrice %></td>
    <td align="center">
      <table width="50" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td rowspan="2">
            <input name="confirmQty" type="text" class="textlen" id="confirm<%=m %>" value="1" readonly="readonly" />
          </td>
          <td align="left" valign="middle">
            <input type="button" name="Submit22" value="+" class="btnlen" onClick="plus('confirm<%=m %>');" />
          </td>
        </tr>
        <tr>
          <td align="left" valign="middle">
            <input type="button" name="Submit22" value="-" class="btnlen" onClick="minus('confirm<%=m %>');" />
          </td>
        </tr>
      </table>
    </td>
    <td align="center">
      <!-- 將javascript的單引號 ' 轉換為 \\' -->
      <input type="button" name="Submit3" value="添加"  onclick="addBook('<%=studentNo %>','<%=book.getIsbn() %>','<%=course.getCourseCode() %>','<%=bookTempl.getMajorCode() %>','<%=bookTempl.getYear() %>','<%=netmopPrice %>','<%=mopPrice %>','<%=rmbPrice %>','<%=book.getTitle().replace("'", "\\'") %>',<%=m+2 %>,'<%=book.getSupplement() %>')"/>
      <input type="reset" name="Submit2" value="重置"/>
    </td>
  </tr>
  
  <%
  m++;
  		  } 
      }
  }
  %>
</table>
<form id="form1" name="form1" method="post" target="_parent" action="OrderIngBookServlet" >
  <input type="hidden" name="studentNo" value="" />
  <input type="hidden" name="isbn" value="" />
  <input type="hidden" name="courseCode" value="" />
  <input type="hidden" name="majorCode" value="" />
  <input type="hidden" name="year" value="" />
  <input type="hidden" name="mopFuturePrice" value="" />
  <input type="hidden" name="mopNetPrice" value="" />  
  <input type="hidden" name="rmbFuturePrice" value="" />
  <input type="hidden" name="title" value="" />
  <input type="hidden" name="confirmQty" value="0" />
  <input type="hidden" name="supplement" value="" />
</form>
</body>
</html>

