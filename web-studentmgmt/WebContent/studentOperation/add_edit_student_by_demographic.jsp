<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<% 
    List demographics = (List)request.getAttribute("demographics");
	Boolean viewOnly = (Boolean) request.getAttribute("viewOnly");
	Boolean studentImported = (Boolean) request.getAttribute("studentImported");
	String demographicKey = (String)request.getAttribute("currentKey");
	String groupName = "";
	if(null != demographicKey)
		groupName = "_" + demographicKey;
	
	//check for OK only
	Map<String,List> dempgraphicMap1 = (Map<String,List>)request.getAttribute("okDemographicMap");
	if(null != dempgraphicMap1)
		demographics = dempgraphicMap1.get(demographicKey);
%>

<!-- Student Demographic Information -->
<ctbweb:studentDemographics demographics = "<%= demographics %>" 
                            viewOnly = "<%= viewOnly %>" 
                            studentImported = "<%= studentImported %>" 
                            demoCategory = "<%= groupName %>" />


<br/>
