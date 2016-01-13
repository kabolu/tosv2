<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>資訊提示</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
try{
	String para = (String)request.getAttribute("para");
	int id = Integer.parseInt(para);
	switch(id){
	case 1:
		%>
		<script language="javascript">
		alert("=====添加成功=====");
		opener.location.href="bookRelAddPage.jsp";
		window.close();
		</script>
		<%break;
	case 2:
		%>
		<script language="javascript">
		alert("=====添加失敗=====");
		opener.location.reload();
		window.close();
		</script>
		<%break;
	
	case 3:
		%>
		<script language="javascript">
		alert("=====添加成功=====");
		opener.location.href="bookRelEditPage.jsp";
		
		window.close();
		</script>
		<%break;
	case 4:
		%>
		<script language="javascript">
		alert("=====添加失敗=====");
		opener.location.reload();
		window.close();
		</script>
		<%break;
		}
}catch(Exception e){
	e.printStackTrace();
}
%>
</body>
</html>
