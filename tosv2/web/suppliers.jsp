<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.must.tos.bean.BookSupplier,edu.must.tos.util.PageBean" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>書商資料</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
input{
	height: 18px;
}
</style>
<script language="javascript">
function addSupplierPage(){
	window.location.href = 'supplierAdd.jsp';
}
function editPage(id){
	window.location.href = 'BookSupplierServlet?type=editPage&no='+id;
}
function deleteFun(id){
	if(confirm("是否確定刪除該書商資料？")){
		$.post(
			"BookSupplierServlet",
			{
				type: "delete",
				no: id
			},
			function(data){
				if(data == "false"){
					alert("書商資料刪除失敗！");
				}else{
					alert("書商資料刪除成功！");
					window.location.href = 'BookSupplierServlet';
				}
			}
		)
	}
}
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
	document.form1.action.value = "BookSupplierServlet";
	document.form1.submit();
}
</script>
<% if (session.getAttribute("userId") == null) {%>
<script>
	alert('登陸超時！請重新登陸');
	window.parent.location.href = 'login.jsp';
</script>
<% } %>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
List<BookSupplier> list = null;
if(request.getAttribute("bookSuppliersList") != null){
	list = (List)request.getAttribute("bookSuppliersList");
}
BookSupplier supplier = null;
if(request.getAttribute("supplier") != null){
	supplier = (BookSupplier)request.getAttribute("supplier");
}
double totalPages = Double.parseDouble(request.getAttribute("totalPages").toString());
%>
<h2>供應商信息</h2>
<form action="BookSupplierServlet" method="post" name="searchForm">
<input type="hidden" name="type" value="searchInfo"/>
<table width="66%" align="center" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>書商編號：</td>
    <td>
      <input type="text" name="code" maxlength="25"/>
    </td>
    <td>書商名稱：</td>
    <td>
      <input type="text" name="name" maxlength="25"/>
    </td>
  </tr>
  <tr>
    <td colspan="4" align="center" height="30">
      <input type="submit" name="submit" value="搜 索" style="height:22px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="reset" name="reset" value="重 設" style="height:22px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" name="add" value="新 增" style="height:22px;" onclick="addSupplierPage();"/>
    </td>
  </tr>
</table>
</form>
<form action="" method="post" name="delForm">
<input type="hidden" name="type" value="delete"/>
<input type="hidden" name="supplierNo" value=""/>
</form>
<form action="" method="post" name="form1">
<%
if(supplier.getSupplierCode() != null){
%>
  <input type="hidden" name="code" value="<%=supplier.getSupplierCode() %>"/>
<%
}
if(supplier.getSupplierName() != null){
%>
  <input type="hidden" name="name" value="<%=supplier.getSupplierName() %>"/>
<%
}
%>
<input type="hidden" name="start" value=""/>
<input type="hidden" value="<%=totalPages %>" name="totalPages" />
<table width="100%" align="center" border="0" cellspacing="1" cellpadding="0">
  <tr bgcolor="#C6D6FD">
    <td align="center" height="25"><strong>書商編號</strong></td>
    <td align="center"><strong>書商名稱</strong></td>
    <td align="center"><strong>英文名稱</strong></td>
    <td align="center"><strong>聯系人</strong></td>
    <td align="center"><strong>聯繫電話</strong></td>
    <td align="center"><strong>學校內部</strong></td>
    <td align="center" width="90"><strong>操作</strong></td>
  </tr>
  <%
  if(list != null && !list.isEmpty()){
	  for(BookSupplier s : list){
		  String engName = "";
		  if(s.getSupplierEngName() != null){
			  engName = s.getSupplierEngName();
		  }
		  String cintactName = "";
		  if(s.getContactName() != null){
			  cintactName = s.getContactName();
		  }
		  String tel_1 = "";
		  if(s.getSupplierTel_1() != null){
			  tel_1 = s.getSupplierTel_1();
		  }
  %>
  <tr onmouseover="this.bgColor='#FFFFFF'" onmouseout="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td height="22">
      <a href="BookSupplierServlet?type=viewInfo&no=<%=s.getSupplierNo() %>"><%=s.getSupplierCode() %></a>
    </td>
    <td><%=s.getSupplierName() %></td>
    <td><%=engName %></td>
    <td><%=cintactName %></td>
    <td><%=tel_1 %></td>
    <td align="center"><%=s.getInner() %></td>
    <td align="center">
      <input type="button" name="edit" value="編輯" onclick="editPage('<%=s.getSupplierNo() %>');"/>
      <input type="button" name="del" value="刪除" onclick="deleteFun('<%=s.getSupplierNo() %>');"/>
    </td>
  </tr>
  <%
	  }
  }else{
  %>
  <tr bgcolor="#FFFBEF">
    <td colspan="6" height="22">沒有書商的資料！</td>
  </tr>
  <%
  }
  %>
  <tr>
    <td align="center" colspan="6" height="30">
    <%
    List<PageBean> pageList = (List)request.getAttribute("page");
    if(pageList != null && !pageList.isEmpty()){
    	for(PageBean pageBean : pageList){
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
    }
    %>
    <label>
      &nbsp;&nbsp;
      <input type="text" name="instart" id="len" onchange="changeFocus();" />
      &nbsp;
      <input type="button" name="jump" onclick="page('page')" value="跳轉" />
    </label>
    </td>
  </tr>
</table>
</form>
</body>
</html>

