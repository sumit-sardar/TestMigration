<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['homepage.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.manageSessions']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<jsp:include page="/sessionOperation/oas_message.jsp" />
 
<netui:form action="assessments_sessions">
<input type="hidden" id="menuId" name="menuId" value="sessionsLink" />
</netui:form>
<input type="hidden" id="treeOrgNodeId" />
<input type="hidden" id="stuForOrgNodeId" />
<input type="hidden" id="selectedTestId" />
<input type="hidden" id="proctorOrgNodeId" />
<table border="0" width="97%" style="margin:15px auto;">
<tr>
<td>
    <h1><lb:label key="assessments.sessions.title" /></h1>
</td>
</tr>
<tr height="400" align="center">
<td>
	<jsp:include page="/sessionOperation/assessment_sessionList.jsp" />
	<script>populateSessionListGrid(true);</script>
</td>    
</tr>
</table>
    




<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("assessments", "sessionsLink");
	autoShowTreeSliderPageLoaded();
});
</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


