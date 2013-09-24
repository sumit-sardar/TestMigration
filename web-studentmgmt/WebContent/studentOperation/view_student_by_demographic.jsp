<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<% 
    List demographics1 = (List)request.getAttribute("demographics");
	Boolean viewOnly1 = (Boolean) request.getAttribute("viewOnly");
	Boolean studentImported1 = (Boolean) request.getAttribute("studentImported");
	Boolean isLASCust = (Boolean) request.getAttribute("isLasLinkCustomer");
%>

<!-- Student Demographic Information -->
<ctbweb:viewStudentDemographics demographics = "<%= demographics1 %>" 
                            viewOnly = "<%= viewOnly1 %>" 
                            studentImported = "<%= studentImported1 %>"
							isLaslink = "<%=isLASCust%>" />

<br/>
