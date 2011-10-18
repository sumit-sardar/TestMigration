<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<% 
    List demographics = (List)request.getAttribute("demographics");
	Boolean viewOnly = (Boolean) request.getAttribute("viewOnly");
	Boolean studentImported = (Boolean) request.getAttribute("studentImported");
%>

<!-- Student Demographic Information -->
<ctbweb:viewStudentDemographics demographics = "<%= demographics %>" 
                            viewOnly = "<%= viewOnly %>" 
                            studentImported = "<%= studentImported %>" />


<br/>
