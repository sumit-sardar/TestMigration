<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">

<netui-template:setAttribute name="title" value="${bundle.web['license.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.NodeLicense']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<!-- start form -->
<netui:form action="manageLicense">


<h1>
    <netui:content value="${bundle.web['license.title']}"/>
</h1>
<p>
    <netui:content value="${bundle.web['license.title.message']}"/><br/>
</p>


<p>
<table class="transparent">
<tr class="transparent"><td class="transparent" width="100"><b>License Model:</b></td><td class="transparent"><netui:span value="${actionForm.parentLicenseNode.subtestModel}" /></td></tr>
<tr class="transparent"><td class="transparent" width="100"><b>Organization:</b></td><td class="transparent"><netui:span value="${actionForm.parentLicenseNode.name}" /></td></tr>
<tr class="transparent"><td class="transparent" width="100"><b>Scheduled:</b></td><td class="transparent"><netui:span value="${actionForm.parentLicenseNode.reserved}" /></td></tr>
<tr class="transparent"><td class="transparent" width="100"><b>Consumed:</b></td><td class="transparent"><netui:span value="${actionForm.parentLicenseNode.consumed}" /></td></tr>
<tr class="transparent"><td class="transparent" width="100"><b>Available:</b></td><td class="transparent"><netui:span tagId="availId" value="${actionForm.parentLicenseNode.available}" /></td></tr>
</table>
</p>

<p>
<br/>
	<netui:button type="submit" value="${bundle.web['common.button.back']}" action="goToSystemAdministration"/>&nbsp;	             
</p>


</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
