<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />
<netui-data:declareBundle bundlePath="helpResources" name="help" />

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
	<netui-template:setAttribute name="title" value="${bundle.web['dataexports.window.title']}" />
	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.dataExport']}" />
	<netui-template:section name="bodySection">
		<%@ taglib uri="label.tld" prefix="lb"%>
		<lb:bundle baseName="dataExportApplicationResource" />
		
		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->
		<netui:form action="dataExport">
			
			<input type="hidden" id="dataExportSearchResultTitle" value=<lb:label key="data.export.search.result" prefix="'" suffix="'"/>/>
			<input type="hidden" id="exportStudentTestSessionSearchResultEmpty" value=<lb:label key="export.student.testSession.search.result.empty" prefix="'" suffix="'"/>/>
			<input type="hidden" id="menuId" name="menuId" value="hasDataExportConfigures" />
			<input type="hidden" id="titleSessionName" value=<lb:label key="data.export.column.title.test.session.name" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleStartData" value=<lb:label key="data.export.column..title.start.date" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleEndData" value=<lb:label key="data.export.column.title.end.date" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleToBeExport" value=<lb:label key="data.export.column.title.to.be.export" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleCompleted" value=<lb:label key="data.export.column.title.completed" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleScheduled" value=<lb:label key="data.export.column.title.scheduled" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleStudentStop" value=<lb:label key="data.export.column.title.student.stop" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleSystemStop" value=<lb:label key="data.export.column.title.system.stop" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleNotTaken" value=<lb:label key="data.export.column.title.not.taken" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleIncompleted" value=<lb:label key="data.export.column.title.incompleted" prefix="'" suffix="'"/>/>
			
			<table class="transparent" width="97%" style="margin: 15px auto;">
				<tr class="transparent">
					<td>
					<table class="transparent">
						<tr class="transparent">
							<td>
							<h1><lb:label key="dataExport.title" /></h1>
							</td>
						</tr>
						<tr>
							<td class="subtitle"><lb:label key="dataExport.message" />
						</tr>
					</table>
					</td>
				</tr>

			</table>

			<<div id = "displayMessage" 
					style="display: none; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; margin-bottom:5px; padding:2px 2px 2px 10px; color: black;">	
				<table width="99.5%">
					<tbody>
						<tr>
							<td valign="middle" width="18">
								<img height="16" src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif">
							</td>
							<td valign="middle" >
								<div id="messageTitle" style="display:none;font-weight:bold;"></div>
								<div id="message" style="display:none;"></div>
								
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<table width="99%" align="center" class="transparent"  style="margin: 15px auto;" >
				<tr>
					<td width="100%">
						<div id ="resetTestId" class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix"
							style="padding-left: 5px; padding-right: 5px;">
						</div>
					</td>
				</tr>
				<tr >
					<td>
						<jsp:include page="session_student_for_export.jsp" />
					</td>
				</tr>
			</table>
		</netui:form>
		
		<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", "exportDataLink");
}); 
</script>

		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
	</netui-template:section>
</netui-template:template>