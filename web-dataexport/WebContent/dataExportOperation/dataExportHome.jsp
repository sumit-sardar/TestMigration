<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="dataExportApplicationResource" />
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['data.exports.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.reports']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<%
    List exportList = (List)request.getAttribute("exportList");
%>

<netui:form action="dataExport">
<input type="hidden" id="menuId" name="menuId" value="exportDataLink" />

<table border="0" width="97%" style="margin:15px auto;">
<tr><td>

<h1><lb:label key="data.export.generic.title" /></h1>
<p>
    <lb:label key="data.export.generic.description" />
</p>
<br/>
<br/>
<div id="exportListDiv" style="height: auto;">
<table border="0" width="97%" style="margin:15px auto;">
    <netui-data:repeater dataSource="requestScope.exportList">
        <netui-data:repeaterItem>
  		<tr class="transparent">
			<td class="transparent">
            	<netui-data:getData resultId="exportUrl" value="${container.item.exportURL}"/>  
				<li style="list-style-type: square;">
				<a onclick="if(!(event.ctrlKey||event.shiftKey||event.altKey))UIBlock();" href="<%= pageContext.getAttribute("exportUrl") %>" style="display: inline;">
					<netui:content value="${container.item.exportName}" defaultValue="&nbsp;" />
				</a>
				</li>
			</td>
		</tr>
  		<tr class="transparent">
			<td class="transparent">
				<netui:content value="${container.item.exportDescription}" defaultValue="&nbsp;" />
			</td>
		</tr>
        </netui-data:repeaterItem>
    </netui-data:repeater>
</table>

</div>

</td></tr></table>

</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", null);
});
</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


