<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
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