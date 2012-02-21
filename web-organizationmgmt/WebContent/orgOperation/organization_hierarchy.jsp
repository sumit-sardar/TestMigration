<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 <%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="organizationApplicationResource" />


<netui-template:template templatePage="/resources/jsp/oas_template.jsp">

<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->





<!-- start form -->
<netui:form action="findOrganizationHierarchy">
<input type="hidden" id="menuId" name="menuId" value="organizationsLink" />
<input type="hidden" id="treeOrgNodeId" />
		<%@include file="/orgOperation/find_organization_hierarchy.jsp" %>
</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("organizations", "organizationsLink");
});
</script>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
