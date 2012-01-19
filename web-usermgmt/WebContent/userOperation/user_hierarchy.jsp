<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">

<netui-template:setAttribute name="title" value="${bundle.web['finduser.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.manageUsers']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->





<!-- start form -->
<netui:form action="findUserHierarchy">
<input type="hidden" id="treeOrgNodeId" />

        <jsp:include page="/userOperation/find_user_hierarchy.jsp" />

</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("organizations", "usersLink");
});
</script>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
