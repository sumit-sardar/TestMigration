<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['homepage.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.home']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<%
    List reportList = (List)request.getAttribute("reportList");
%>

<netui:form action="LasLinksReport">
<input type="hidden" id="menuId" name="menuId" value="reportsLink" />

<table border="0" width="97%" style="margin:15px auto;">
<tr><td>

<h1><netui:content value="Reports"/></h1>
<p>
    <netui:content value="Click a report name to view the report."/>
</p>



<p>
<table class="transparent">
<tr class="transparent">
    <td class="transparent">Customer Program:</td>
    <td class="transparent">
        <netui:span value="${requestScope.program}"/>
    </td>
</tr>    
<tr class="transparent">
    <td class="transparent">User Organization:</td>
    <td class="transparent">
        <netui:span value="${requestScope.organization}"/>
    </td>
</tr>    
</table>    
</p>


<div id="reportlists" style="height: auto;">
<table class="transparent">
<tbody>
<tr class="transparent">
<td class="transparent" valign="top" width="32">&nbsp;</td>
<td class="transparent" valign="top" width="650">
<li style="list-style-type: square;"><a href="/SessionWeb/sessionOperation/immediateReport.do" style="display: inline;">Immediate Scores</a>
</li>Displays the immediate scores obtained by the student in standard format.
</td>		
</tr>
</table>
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


