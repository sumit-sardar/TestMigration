<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
	<netui-template:setAttribute name="title" value="${bundle.web['scoring.menu.studentScoring']}" />
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.studentScoring']}"/>
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