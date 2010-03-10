<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/report_template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['reports.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.home']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<%
    List reportList = (List)request.getAttribute("reportList");
    Boolean multipleProgram = (Boolean)request.getAttribute("multipleProgram");
    Boolean multipleOrganizations = (Boolean)request.getAttribute("multipleOrganizations");
    Boolean singleProgOrg = (Boolean)request.getAttribute("singleProgOrg");
%>


<h1><netui:content value="Reports"/></h1>
<p>
<c:if test="${singleProgOrg}"> 
    <netui:content value="Click a report name to view the report."/>
</c:if>
    
<c:if test="${multipleProgram || multipleOrganizations}"> 
    <netui:content value="Select program and/or organization to view a list of associated reports. Click a report name to view the report."/>
</c:if>
        
</p>
 
<p>
<table class="transparent">
<tr class="transparent">
    <td class="transparent">Customer Program:</td>
    <td class="transparent">
<c:if test="${multipleProgram}">    
        <netui:select tagId="program" optionsDataSource="${requestScope.programList}" dataSource="requestScope.program" onChange="getReportList();" size="1" style="width:300px"/>
</c:if>        
<c:if test="${! multipleProgram}">    
        <netui:span value="${requestScope.program}"/>
</c:if>        
    </td>
</tr>    
<tr class="transparent">
    <td class="transparent">User Organization:</td>
    <td class="transparent">
<c:if test="${multipleOrganizations}">    
        <netui:select tagId="organization" optionsDataSource="${requestScope.organizationList}" dataSource="requestScope.organization" onChange="getReportList();" size="1" style="width:300px"/>
</c:if>        
<c:if test="${! multipleOrganizations}">    
        <netui:span value="${requestScope.organization}"/>
</c:if>        
    </td>
</tr>    
</table>    
</p>
 

<!-- TURNLEAF REPORT LIST -->
<div id="reportlists" style="height: 500px">
    <jsp:include page="turnleaf_report_list.jsp" />  
</div>

<img width="0" height="0" border="0" id="TLLogout"  src="<%= System.getProperty("TLLogoutURL") %>?fncv=<%= System.currentTimeMillis() %>" />

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>

