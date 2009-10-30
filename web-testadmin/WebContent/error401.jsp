<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%
// If not authenticated, redirect to the login page.
response.sendRedirect("/");
%>