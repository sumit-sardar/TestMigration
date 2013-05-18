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

<netui-template:template templatePage="/resources/jsp/oas_template_manage_license.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['manageLicense.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.testLicense']}"/>
<netui-template:section name="bodySection">
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="organizationApplicationResource" />

<link href="<%=request.getContextPath()%>/resources/css/jtip.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jtip.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/licenses.js"></script>

<input type="hidden" id="orgNodeName" name = "orgNodeName" value=<lb:label key="license.orgNodeName" prefix="'" suffix="'"/>/>
<input type="hidden" id="scheduled" name = "scheduled" value=<lb:label key="license.scheduled" prefix="'" suffix="'"/>/>
<input type="hidden" id="consumed" name = "consumed" value=<lb:label key="license.consumed" prefix="'" suffix="'"/>/>
<input type="hidden" id="available" name = "available" value=<lb:label key="license.available" prefix="'" suffix="'"/>/>

<input type="hidden" id="currentEditing" name="currentEditing" value="false" />

<input type="hidden" id="noOrgTitle" name="noOrgTitle" value=<lb:label key="org.noOrgSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noOrgMsg" name="noOrgMsg" value=<lb:label key="org.noOrgSelected.message" prefix="'" suffix="'"/>/>

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<netui:form action="services_manageLicenses">

<input type="hidden" id="menuId" name="menuId" value="manageLicensesLink" />
<input type="hidden" id="treeOrgNodeId" />


<table class="transparent" width="97%" style="margin:15px auto;">  
	<tr class="transparent">
        <td>
		<table class="transparent">
			<tr class="transparent">
				<td>
				<h1><lb:label key="services.license.title" /></h1>
				</td>
			</tr>
			<tr>
				<td class="subtitle">
						<div id="contentMain">This customer doesn't have license configured properly. Contact Customer Support for more information.</div>
				</td>
			</tr>
		</table>
		</td>
    </tr>


</table>


</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", "manageLicensesLink");
}); 
</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


