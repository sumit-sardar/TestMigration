<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<% 
    List demographics = (List)request.getAttribute("demographics");
	Boolean viewOnly = (Boolean) request.getAttribute("viewOnly");
	Boolean studentImported = (Boolean) request.getAttribute("studentImported");
%>

<!-- Student Demographic Information -->
<ctbweb:studentDemographics demographics = "<%= demographics %>" 
                            viewOnly = "<%= viewOnly %>" 
                            studentImported = "<%= studentImported %>" />


<br/>
