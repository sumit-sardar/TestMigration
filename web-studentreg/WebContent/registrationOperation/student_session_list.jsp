<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentRegistrationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
	<netui-template:setAttribute name="title" value="${bundle.web['registerstudent.title']}" />
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.registerStudent']}"/>
<netui-template:section name="bodySection">



<netui:form action="beginStudentRegistration">
<input type="hidden" id="menuId" name="menuId" value="studentRegistrationLink" />
<input type="hidden" id="treeOrgNodeId" />
<input type="hidden" id="treeOrgNodeIdInPopup">
<input type="hidden" id="treeOrgNodeIdInSecondaryDiv">
	<jsp:include page="/registrationOperation/find_student_session.jsp" />
</netui:form>




<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("assessments", "sessionsLink");
});
</script>

</netui-template:section>
</netui-template:template>