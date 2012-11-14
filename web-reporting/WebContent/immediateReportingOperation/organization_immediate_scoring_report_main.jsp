<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
	<netui-template:section name="bodySection">

		<netui:form action="organizationImmediateReporting">
			<input type="hidden" id="menuId" name="menuId" value="reportsLink" />
			<input type="hidden" id="treeOrgNodeId" />
			<input id="downloadImmediateReport" type="hidden" value="userFile" name="downloadImmediateReport">
			<input type="hidden" id="rosterId"  name="rosterId"/>
			<input type="hidden" id="testAdminId" name="testAdminId"/>
			<jsp:include page="/immediateReportingOperation/organization_immediate_scoring_report_org_list.jsp" />
		</netui:form>
		
		<script type="text/javascript">
			$(document).ready( 
				function(){ setMenuActive("reports", "reportsLink"); }
				);
		</script>

	</netui-template:section>
</netui-template:template>
