<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />
<netui-data:declareBundle bundlePath="helpResources" name="help" />

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
	<netui-template:setAttribute name="title" value="${bundle.web['installClient.window.title']}" />
	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.installSoftware']}" />
	<netui-template:setAttribute name="helpLinkLinux" value="${bundle.help['help.topic.installClientLinux']}" />
	<netui-template:section name="bodySection">

<netui:form action="eMetric_user_accounts_detail">
<input type="hidden" id="menuId" name="menuId" value="showAccountFileDownloadLink" />
		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->
		<table width="99%" align="center" class="transparent" style="margin: 15px auto;">
			<tr>
				<td>
				    <h1><lb:label key="emetricfile.list.title" /></h1>
				</td>
			</tr>
			<tr>
				<td>
				<div id='jqGrid-content-section'><jsp:include page="emetric_account_file_list_gridView.jsp" /></div>
				<script>getEmetricFileList();</script></td>
			</tr>

		</table>

		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", "showAccountFileDownloadLink");
});
</script>
	</netui-template:section>
</netui-template:template>

