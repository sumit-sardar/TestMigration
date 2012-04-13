<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
<netui-template:section name="bodySection">

<netui:form action="beginStudentScoring">
<input type="hidden" id="menuId" name="menuId" value="studentScoringLink" />
<input type="hidden" id="treeOrgNodeId" />
	<jsp:include page="/studentScoringOperation/find_student_session_scoring.jsp" />
</netui:form>


<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("scoring", "studentScoringLink");
});
</script>

</netui-template:section>
</netui-template:template>