<%@ page import="java.io.*"%>
<%@ page import=" java.util.*"%>
<% 
// Fake mime type.
response.setContentType("text/xml"); 
%>
<%= request.getAttribute("item") %>
