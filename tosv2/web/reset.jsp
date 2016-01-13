<%@ page language="java" pageEncoding="UTF-8"%>
<%
  session.removeAttribute("programList");
  String searchType = request.getParameter("searchType");
  if(searchType != null && searchType.equals("totalReport")) {
     response.sendRedirect("FacultyListServlet?type=totalReport");
  } else if (searchType != null && !searchType.equals("") && searchType.equals("orderStudent")) {
	 response.sendRedirect("studentsearch.jsp?searchType=orderStudent");
  } else {
	 response.sendRedirect("studentsearch.jsp");
  }
  
%>

