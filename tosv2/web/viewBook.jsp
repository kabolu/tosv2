<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.must.tos.bean.Model" %>
<%@ page import="edu.must.tos.bean.BookRel" %>
<%@ page import="edu.must.tos.bean.Price" %>
<%@ page import="java.util.*" %>
<%@page import="edu.must.tos.bean.BookSupplier"%>
<%@page import="edu.must.tos.util.ToolsOfString"%>
<%@page import="edu.must.tos.bean.BookRelInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>圖書詳細信息</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<br>
<h2>當前學期圖書詳細信息</h2>
<div align="center">
<%
Model m = (Model)request.getAttribute("bookModel");
String isbn = m.getBook().getIsbn();
List<BookSupplier> supplierList = null;
if(request.getAttribute("supplierList") != null){
	supplierList = (List)request.getAttribute("supplierList");
}
%>
  <table width="550" border="1" cellpadding="0" cellspacing="0" bordercolor="#EDA938">
    <tr>
      <td width="100" height="25" align="left">圖書編號：</td><td width="450" colspan="3"><%=isbn%></td>
    </tr>
    <tr>
      <td align="left" height="25">書名：</td><td colspan="3"><%=m.getBook().getTitle() %></td>
    </tr>
    <tr>
      <td align="left" height="25">作者：</td><td colspan="3"><%=m.getBook().getAuthor() %></td>
    </tr>
    <tr>
      <td align="left" height="25">出版商：</td><td colspan="3"><%=m.getBook().getPublisher() %></td>
    </tr>
    <tr>
      <td align="left" height="25">出版年份：</td><td width="175"><%=m.getBook().getPublishYear() %></td>
      <td align="left" width="100">版本：</td><td width="175"><%=m.getBook().getEdition() %></td>
    </tr>
    <tr>
      <td align="left" height="25">圖書語言：</td><td><%=m.getBook().getLanguage() %></td>
      <td align="left">
        <%--<label>圖書類型：</label>--%>
      </td>
      <td>
        <%--<%=m.getBook().getBookType() %>--%>
      </td>
    </tr>
    <tr>
      <td align="left" height="25">是否可退書：</td><td ><%if("Y".equals(m.getBook().getWithdrawInd())){ out.print("是"); }else {out.print("否");}%></td>
      <td align="left" height="25">是否可補訂：</td><td ><%if("Y".equals(m.getBook().getSupplement())){ out.print("是"); }else if("N".equals(m.getBook().getSupplement())){out.print("否");}%>&nbsp;</td>
    </tr>
    <tr>
      <td align="left" height="25">書商1：</td>
      <td>
      <%
      if(supplierList != null && !supplierList.isEmpty()){
    	  for(BookSupplier b : supplierList){
    		  if(b.getSupplierCode().equals(m.getBook().getSupplierCode1())){
    			  out.print(b.getSupplierName());
    		  }
    	  }
      }
      %>
      </td>
      <td align="left" width="100">書商2：</td>
      <td>
      <%
      if(supplierList != null && !supplierList.isEmpty()){
    	  for(BookSupplier b : supplierList){
    		  if(b.getSupplierCode().equals(m.getBook().getSupplierCode2())){
    			  out.print(b.getSupplierName());
    		  }
    	  }
      }
      %>
      </td>
    </tr>
    <tr>
      <td align="left" height="25">備注：</td>
      <td colspan="3" align="left"><%=m.getBook().getRemarks() %></td>
    </tr>
    <tr>
      <td align="left" height="25">入貨單價：</td>
      <td><%=m.getBook().getUnitPrice() %></td>
      <td align="left">入貨幣種：</td>
      <td><%=ToolsOfString.NulltoEmpty(m.getBook().getCurrency()) %></td>
    </tr>
    <tr>
      <td align="left" height="25">入貨折扣：</td>
      <td><%=m.getBook().getDisCount() %></td>
      <td align="left">優惠價：</td>
      <td><%=m.getBook().getFavourablePrice() %></td>
    </tr>
  </table>
</div>
<p align="center">圖書價格</p>
<div align="center">
  <table width="550" border="1" cellpadding="0" cellspacing="0" bordercolor="#EDA938">
    <tr>
      <th width="150">&nbsp;</th><th>預估價</th><th>實價</th>
    </tr>
    <%
    for(int i=0; i<m.getPrice().size(); i++){
    	Price price = (Price)m.getPrice().get(i);
    %>
    <tr>
      <td height="20">
      <%if("MOP".equals(price.getCurrency())){out.print("澳門幣");}else{out.print("人民幣");}%>(<%=price.getCurrency() %>)</td>
      <td><%=price.getFuturePrice() %></td>
      <td><%=price.getNetPrice() %></td>
    </tr>
    <%
    }
    %>
  </table>
</div>
<p align="center">與圖書相關的科目</p>
<div align="center">
  <table width="550" border="1" cellpadding="0" cellspacing="0" bordercolor="#EDA938">
    <tr>
      <th height="20">科目編號</th>
      <th>科目名稱</th>
      <th>專業編號</th>
      <th>年級</th>
      <th>選修/必修</th>
      <th>類型</th>
    </tr>
    <%
    List courseCNameList = (List)request.getAttribute("courseCNameList");
    List<BookRelInfo> relInfoList = (List)request.getAttribute("relInfoList");
    for(int i=0; i<m.getBookRel().size(); i++){
    	String grade = "", core = "", bookType = "";
    	BookRel bookrel = (BookRel)m.getBookRel().get(i);
    	for(BookRelInfo info : relInfoList){
    		if(info.getCourseCode().equals(bookrel.getCourseCode()) && info.getMajorCode().equals(bookrel.getMajorCode())){
    			grade = info.getGrade() ;
    			core = info.getCe() ;
    			bookType = info.getBookType() ;
    		}
    	}
    %>
    <tr>
      <td><%=bookrel.getCourseCode()%></td>
      <td><%=courseCNameList.get(i) %></td>
      <td><%=ToolsOfString.NulltoEmpty(bookrel.getMajorCode()) %></td>
      <td align="center"><%=ToolsOfString.NulltoEmpty(grade) %></td>
      <td align="center"><%=ToolsOfString.NulltoEmpty(core) %></td>
      <td align="center"><%=ToolsOfString.NulltoEmpty(bookType) %></td>
    </tr>
    <%
    }
    %>
  </table>
  <br>
  <input type="button" value="關閉窗口" onclick="window.close();">
</div>
</body>
</html>
