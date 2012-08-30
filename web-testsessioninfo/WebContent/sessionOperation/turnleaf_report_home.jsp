<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />
  
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['reports.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.reports']}"/>
<netui-template:section name="bodySection">
 
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/report_list.js"></script>  

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<%
    List reportList = (List)request.getAttribute("reportList");
    Boolean multipleProgram = (Boolean)request.getAttribute("multipleProgram");
    Boolean multipleOrganizations = (Boolean)request.getAttribute("multipleOrganizations");
    Boolean singleProgOrg = (Boolean)request.getAttribute("singleProgOrg");
%>

<netui:form action="turnLeafReport">
<input type="hidden" id="menuId" name="menuId" value="reportsLink" />

<table border="0" width="97%" style="margin:15px auto;">
<tr><td>

<h1><lb:label key="report.generic.title" /></h1>
<c:if test="${singleProgOrg}"> 
<p>
    <lb:label key="report.generic.description" />
</p>
</c:if>

<c:if test="${multipleProgram || multipleOrganizations}"> 
<p>    
	<lb:label key="report.mutipleProducts.description" />
</p>
</c:if>


<br/>
<p>
<table class="transparent">
<tr class="transparent">
    <td class="transparent"><lb:label key="report.customerProgram" />:</td>
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
    <td class="transparent"><lb:label key="report.userOrganization" />:</td>
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
<br/>


<!-- TURNLEAF REPORT LIST -->
<div id="reportlists" style="height: auto">
    <jsp:include page="turnleaf_report_list.jsp" />  
</div>

</td></tr></table>

</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("reports", null);
});
</script>

<!-- 
<img width="0" height="0" border="0" id="TLLogout"  src="< %= System.getProperty("TLLogoutURL") %>?fncv=< %= System.currentTimeMillis() %>" />
-->

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


