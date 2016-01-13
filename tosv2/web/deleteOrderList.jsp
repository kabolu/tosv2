<%@ page language="java" import="java.util.*,edu.must.tos.bean.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
javascript:window.history.forward(1);
  
function checkCount(id, count) {
	if(document.getElementById(id).checked == true) {
		if(parseInt(count) > 1) {
			var result = confirm("該學員對應該書的科目多於一個，是否真的刪除訂單？");
			if(result) {
				document.getElementById(id).checked = true;
			} else {
				document.getElementById(id).checked = false;
			}
		}
	}
}

function checkall(){
	var all = document.getElementById("allCheck");
	var sub = document.getElementsByName("delbox");
	if(all.checked == true){
		for(var i=0; i<sub.length; i++){
			sub[i].checked=true;
		}
	}else{
		for(var i=0; i<sub.length; i++){
			sub[i].checked=false;
		}
	}
}
function checkBoxs(){
	var size = 0;
	var flag = false;
	var delboxs = document.getElementsByName("delbox");
	for(var i=0; i<delboxs.length; i++) {
		if(delboxs[i].checked == true) {
			flag = true;
			size++;
		}
	}
	alert(size);
}
//check at least one item is selected 
function checkDelbox() {
	var flag = false;
	var delboxs = document.getElementsByName("delbox");
	var count = 0;
	for(var i=0; i<delboxs.length; i++) {
		if(delboxs[i].checked == true) {
			count++;
		}
	}
	if(count != 0) {
		if(confirm("已選擇"+count+"記錄，是否確定刪除這些訂書記錄？")){
			return true;
		}else{
			return false;
		}
	} else {
		alert("請選擇要刪除的訂書記錄！");
		return false;
	}
 }
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<%
int size = 0;
List<DelOrder> delOrderList = (List)session.getAttribute("delOrderList");
if(delOrderList != null && !delOrderList.isEmpty()) {
	size = delOrderList.size();
}
%>
<body>
<p align="center" class="blackBold12px">一共有<%=size %>條記錄！</p>
<form name="form1" method="post" action="DeleteOrdersServlet" onSubmit="return checkDelbox();">
  <table width="100%" border="1" cellpadding="0" cellspacing="0">
    <tr bgcolor="#C6D6FD">
      <td><div align="center">學號</div></td>
      <td><div align="center">圖書ISBN</div></td>
      <td><div align="center">圖書名稱</div></td>
      <td><div align="center">訂單號</div></td>
      <td><div align="center">全選<input name="allCheck" id="allCheck" type="checkbox" class="checkbox" onClick="checkall();"></div></td>
    </tr>
    <%
    if(delOrderList != null && !delOrderList.isEmpty()) {
    	int i = 0;
    	for(DelOrder delOrder : delOrderList) {
    %>	
    <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
      <td><%=delOrder.getStudentNo() %></td>
      <td><%=delOrder.getIsbn() %></td>
      <td><%=delOrder.getTitle() %></td>
      <td><%=delOrder.getOrderSeqNo() %></td>
      <td align="center">
        <input name="delbox" type="checkbox" class="checkbox" id="delbox<%=i %>" onClick="checkCount(this.id,'<%=delOrder.getCourseBookCount() %>');" value="<%=i %>">
      </td>
    </tr>
    <%
    		i++;
    	}
    } 
    %>
  </table>
  <p align="center">
    <input type="submit" name="Submit" value="確定刪除">
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="reset" name="Submit2" value="重置">
  </p>
</form>
</body>
</html>