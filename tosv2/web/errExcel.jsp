<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@page import="edu.must.tos.bean.BookImport"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>導入文件數據錯誤</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
String type = (String)request.getAttribute("type");
try{
	List errSameIsbn = (List)request.getAttribute("errSameIsbn");
	List errIsbn = (List)request.getAttribute("errIsbn");
	List errIsbnFormat = (List)request.getAttribute("errIsbnFormat");
	List errCourseCode = (List)request.getAttribute("errCourseCode");
	List errTypeandPrice = (List)request.getAttribute("errTypeandPrice");
	List errPublishYearAndPrice = (List)request.getAttribute("errYearAndPrice");
	List errYear = (List)request.getAttribute("resultErrYear");
	List errTemplCourseCode = (List)request.getAttribute("templCourseCode");
	List resultErrStud_grp = (List)request.getAttribute("resultErrStud_grp");
	List majorCodeErrList = (List)request.getAttribute("majorCodeErrList");
	if(type != null && type.equals("uploadBook")){
		List indexList = (List)request.getAttribute("indexList");
		List errsList = (List)request.getAttribute("errsList");
		List bookList = (List)request.getAttribute("bookList");
%>
<table border="1" cellpadding="1" cellspacing="0">
  <tr>
    <td height="25" colspan="4">
      <p>
        <b>圖書信息表檢查結果：</b>
      </p>
    </td>
  </tr>
  <%
  if(bookList != null && !bookList.isEmpty()){
	  for(int i=0; i<bookList.size(); i++ ){
		  Integer index = (Integer)indexList.get(i);
		  List<String> errList = (List)errsList.get(i);
		  BookImport bookImport = (BookImport)bookList.get(i);
  %>
  <tr>
    <td>第<%=index+2 %>行</td>
    <td><%=bookImport.getIsbn() %></td>
    <td><%=bookImport.getTitle() %></td>
    <td>
    <%
    for(String str : errList){
    	out.println(str+"<br>");
    }
    %>
    </td>
  </tr>
  <%
	  }
  }
  %>
</table>
	<%
	} else if(type != null && type.equals("uploadPrice")){
	%>
	<table>
	<tr>
	 <td height="25">
	  <b>圖書實付價格表檢查結果：</b>
	 </td>
	</tr>
	<%
	if(errPublishYearAndPrice!=null && errPublishYearAndPrice.size()!=0){
		for(int i=0;i<errPublishYearAndPrice.size();i++){
			int a=Integer.parseInt(errPublishYearAndPrice.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+1%>行的isbn不存在或實付價格填寫有誤，請檢查其中一項！</td>
			</tr>
			<%
		}
	}else{
		%>
		<tr>
		 <td>isbn和實付價格都是合法的。</td>
		</tr>
		<%
	}
	%>
	</table>
	<br>
	<table>
	<tr>
	 <td height="25">
	  <b>isbn格式檢查結果：</b>
	 </td>
	</tr>
	<%
	if(errIsbnFormat != null && !errIsbnFormat.isEmpty()){
		for(int i=0;i<errIsbnFormat.size();i++){
			int a=Integer.parseInt(errIsbnFormat.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+2%>行的isbn格式填寫有誤，請檢查！</td>
			</tr>
			<%
		}
	}else{
		%>
		<tr>
		 <td>isbn格式無誤。</td>
		</tr>
		<%
	}
	%>
	</table>
	<%
	} else if(type != null && "uploadBookTempl".equals(type)){
	%>
	<table>
	<tr>
	 <td height="25">
	  <b>科表信息表檢查結果：：</b>
	 </td>
	</tr>
	<%
	if(errTemplCourseCode != null && !errTemplCourseCode.isEmpty()){
		for(int i=0; i<errTemplCourseCode.size(); i++){
			int a = Integer.parseInt(errTemplCourseCode.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+1 %>行的科目編號與該學院的科目不對應，請檢查！</td>
			</tr>
			<%
		}
	} else {
		%>
		<tr>
		 <td>該科目編號都是合法的。</td>
		</tr>
		<%
	}
	%>
	</table>
	<br>
	<table>
	<tr>
	 <td height="25">
	  <b>年級檢查結果：</b>
	 </td>
	</tr>
	<%
	if(errYear != null && !errYear.isEmpty()){
		for(int i=0; i<errYear.size(); i++){
			int a = Integer.parseInt(errYear.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+1%>行的年級填寫有誤，請檢查！</td>
			</tr>
			<%
		}
	} else {
		%>
		<tr>
		 <td>年級填寫無誤。</td>
		</tr>
		<%
	}
	%>
	</table>
	<br>
	<table>
	 <tr>
	  <td height="25">
	  <b>課程標識檢查結果：</b>
	  </td>
	 </tr>
	<%
	if(resultErrStud_grp != null && !resultErrStud_grp.isEmpty()){
		for(int i=0; i<resultErrStud_grp.size(); i++){
			int a = Integer.parseInt(resultErrStud_grp.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+1%>行的課程標識填寫有誤，請檢查！</td>
			</tr>
			<%
		}
	}else{
		%>
		<tr>
		 <td>課程標識填寫無誤。</td>
		</tr>
		<%
	}
	%>
	</table>
	<br>
	<table>
	 <tr>
	  <td height="25">
	  <b>專業編號檢查結果：</b>
	  </td>
	 </tr>
	<%
	if(majorCodeErrList != null && !majorCodeErrList.isEmpty()){
		for(int i=0; i<majorCodeErrList.size(); i++){
			int a = Integer.parseInt(majorCodeErrList.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+1%>行的專業編號不屬於該學院，請檢查！</td>
			</tr>
			<%
		}
	}else{
		%>
		<tr>
		 <td>專業編號填寫無誤。</td>
		</tr>
		<%
	}
	%>
	</table>
	<%
	} else if (type != null && "uploadFuturePrice".equals(type)){
	%>
	<table>
	<tr>
	 <td height="25">
	  <b>圖書預付價格表檢查結果：</b>
	 </td>
	</tr>
	<%
	if(errPublishYearAndPrice != null && errPublishYearAndPrice.size() != 0){
		for(int i=0; i<errPublishYearAndPrice.size(); i++){
			int a = Integer.parseInt(errPublishYearAndPrice.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+1%>行的isbn不存在或預付價格填寫有誤，請檢查其中一項！</td>
			</tr>
			<%
		}
	} else {
		%>
		<tr>
		 <td>isbn和價格都是合法的。</td>
		</tr>
		<%
	}
	%>
	</table>
	<br>
	<table>
	<tr>
	 <td height="25">
	  <b>isbn格式檢查結果：</b>
	 </td>
	</tr>
	<%
	if(errIsbnFormat != null && !errIsbnFormat.isEmpty()){
		for(int i=0; i<errIsbnFormat.size(); i++){
			int a = Integer.parseInt(errIsbnFormat.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+2%>行的isbn格式填寫有誤，請檢查！</td>
			</tr>
			<%
		}
	}else{
		%>
		<tr>
		 <td>isbn填寫無誤。</td>
		</tr>
		<%
	}
	%>
	</table>
	<%
	}else if(type != null && type.equals("uploadUpdBook")){
	%>
	<table>
	 <tr>
	  <td height="25">
	  <p>
	  <b>圖書資訊更新表檢查結果：</b>
	  <br><br>
	  <b>Excel表內isbn檢查結果：</b>
	  </td>
	</tr>
	<%
	if(errSameIsbn != null && errSameIsbn.size() != 0){
		for(int i=0;i<errSameIsbn.size();i++){
			int a = Integer.parseInt(errSameIsbn.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+2%>行的isbn有重複值！</td>
			</tr>
			<%
		}
	} else {
		%>
		<tr>
		 <td>Excel表內isbn沒有重複值。</td>
		</tr>
		<%
	}
	%>
	</table>
	<br>
	<table>
	 <tr>
	  <td height="25"><b>isbn格式檢查結果：</b></td>
	 </tr>
	<%
	if(errIsbnFormat != null && !errIsbnFormat.isEmpty()){
		for(int i=0; i<errIsbnFormat.size(); i++){
			int a = Integer.parseInt(errIsbnFormat.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+2%>行的isbn格式填寫錯誤，請檢查！</td>
			</tr>
			<%
		}
	} else {
		%>
		<tr>
		 <td>Excel表內isbn格式無誤。</td>
		</tr>
		<%
	}
	%>
	</table>
	<br>
	<table>
	 <tr>
	  <td height="25"><b>isbn檢查結果：</b></td>
	 </tr>
	<%
	if(errIsbn != null && errIsbn.size() != 0){
		for(int i=0; i<errIsbn.size(); i++){
			int a = Integer.parseInt(errIsbn.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+2%>行的isbn不存在數據庫中，請檢查！</td>
			</tr>
			<%
		}
	} else {
		%>
		<tr>
		 <td>Excel表內isbn是正確的。</td>
		</tr>
		<%
	}
	%>
	</table>
	<br>
	<table>
	<tr>
	 <td height="25">
	  <b>出版日期、圖書語言、圖書類型和可退訂項檢查結果：</b>
	 </td>
	</tr>
	<%
	if(errTypeandPrice != null && errTypeandPrice.size() != 0){
		for(int i=0; i<errTypeandPrice.size(); i++){
			int a = Integer.parseInt(errTypeandPrice.get(i).toString());
			%>
			<tr>
			 <td>第<%=a+1%>行的出版日期、圖書語言、圖書類型或可退訂項填寫有誤，請檢查其中一項！</td>
			</tr>
			<%
		}
	} else {
		%>
		<tr>
		 <td>出版日期、圖書語言、圖書類型和可退訂項填寫合法的。</td>
		</tr>
		<%
	}
	%>
	</table>
	<%
	} else if (type != null && (type.equals("uploadNewCourseInfo") || type.equals("uploadNewStudOrder") || 
			type.equals("updateStock") || type.equals("uploadTimes"))){
		Map map = (HashMap)request.getAttribute("map");
		Object key = null;
		Object value = null;
		List list = new ArrayList(map.keySet());
		Collections.sort(list);
		Iterator it = list.iterator();
		while(it.hasNext()){
			key = it.next();
			String keyStr = " 第"+key+"行===&gt;&gt;";
			Map err = (HashMap)map.get(key);
			if(err == null || err.isEmpty()){
				
			}else{
				out.print("&nbsp;"+keyStr);
				out.print("<br>");
				List errList = new ArrayList(err.keySet());
				Collections.sort(errList);
				Iterator errIt = errList.iterator();
				while(errIt.hasNext()){
					key = errIt.next();
					value = err.get(key);
					out.print("------"+value);
					out.print("<br>");
				}
				out.print("----------------------------------------------------------------------------------------");
				out.print("<br>");
			}
		}
	}
}catch(Exception e){
	e.printStackTrace();
}
%>
<p>
<%if(type != null && type.equals("uploadNewStudOrder")){
	%>
	<input type="button" value="返 回" onClick="window.location.href='uploadNewStudOrderInfo.jsp'">
	<%
} else if (type != null && type.equals("updateStock")){
	%>
	<input type="button" value="返 回" onClick="window.location.href='updateStock.jsp'">
	<%
} else if (type != null && type.equals("uploadTimes")){
	%>
	<input type="button" value="返 回" onClick="window.location.href='uploadTimes.jsp'">
	<%
} else {
	%>
	<input type="button" value="返 回" onClick="history.back();">
	<%
} %>
</body>
</html>