
<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
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


<link href="<%=request.getContextPath()%>/resources/css/widgets_NEW.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/embed.js"></script>    


<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<%
	List reportList = (List)request.getAttribute("reportList");
	String selectedReport = (String)request.getAttribute("selectedReport");
	String reportUrl = (String)request.getAttribute("reportUrl");
	String testAdminId = (String)request.getAttribute("testAdminId");

    System.out.println("JSP : reportUrl = " + reportUrl);    
%>

<netui:form action="turnLeafReport">
<input type="hidden" id="menuId" name="menuId" value="reportsLink" />

<script type="text/javascript">
	UIBlock();
</script>

<table border="0" width="97%" style="margin:15px auto;">
<!-- TURNLEAF REPORT LIST -->
<% if (reportList != null) { %>
<tr>
    <td>
        <table class="reportNavLayout">
        <tr>
            <td>
                <ctbweb:reporNavigation reportList="<%= reportList %>" selectedReport="<%= selectedReport %>" testAdminId="<%= testAdminId %>"/>
            </td>
        </tr>
        </table>
    </td>
</tr>
<% } %>
<!-- TURNLEAF REPORT CONTENT -->
<tr>
    <td style="background-color: #fff; width: 100%; height: 100%; vertical-align: top; margin: 0px; padding: 0px;">
        <iframe name="turnleafReportIframe" id="turnleafReportIframe" src="<%= reportUrl %>" frameborder="0" height="500px" width="100%"></iframe>
    </td>
</tr>

</table>

</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("reports", null);	
	document.getElementById("turnleafReportIframe").onload = function() { $.unblockUI(); };	
	$.unblockUI();	
});
</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


