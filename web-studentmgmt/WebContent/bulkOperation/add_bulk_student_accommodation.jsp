<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentApplicationResource" />

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">

<netui-template:setAttribute name="title" value="${bundle.web['assignAccommodation.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.assignAccommodations']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->





<!-- start form -->
<netui:form action="beginAddBulkStudent">
<input type="hidden" id="menuId" name="menuId" value="bulkAccomLink" />
<input type="hidden" id="selectedBulkTreeOrgNodeId" />
		<%@include file="/bulkOperation/assign_bulk_accommodation.jsp"%>
</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("organizations", "bulkAccomLink");
});
</script>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
