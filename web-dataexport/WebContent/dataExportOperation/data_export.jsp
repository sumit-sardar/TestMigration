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
	<netui-template:setAttribute name="title" value="${bundle.web['data.exports.window.title']}" /> 
<%-- 	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.dataExport']}" />		--%>
	<netui-template:section name="bodySection">
		<%@ taglib uri="label.tld" prefix="lb"%>
		<lb:bundle baseName="dataExportApplicationResource" />
		
		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->
		<netui:form action="dataExport">
			<input type="hidden" id="menuId" name="menuId" value="exportDataLink" />
			<input type="hidden" id="captionToBeExportGrid" value=<lb:label key="data.export.caption.export.grid" prefix="'" suffix="'"/>/>
			<input type="hidden" id="dataExportSearchResultTitle" value=<lb:label key="data.export.search.result" prefix="'" suffix="'"/>/>
			<input type="hidden" id="exportStudentTestSessionSearchResultEmpty" value=<lb:label key="export.student.testSession.search.result.empty" prefix="'" suffix="'"/>/>
			<input type="hidden" id="exportUnscoredStudentSearchResultEmpty" value=<lb:label key="export.unscored.student.search.result.empty" prefix="'" suffix="'"/>/>
			<input type="hidden" id="menuId" name="menuId" value="hasDataExportConfigures" />
			<input type="hidden" id="titleSessionName" value=<lb:label key="data.export.column.title.test.session.name" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleStartDate" value=<lb:label key="data.export.column.title.start.date" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleEndDate" value=<lb:label key="data.export.column.title.end.date" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleToBeExport" value=<lb:label key="data.export.column.title.to.be.export" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleCompleted" value=<lb:label key="data.export.column.title.completed" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleScheduled" value=<lb:label key="data.export.column.title.scheduled" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleStudentStop" value=<lb:label key="data.export.column.title.student.stop" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleSystemStop" value=<lb:label key="data.export.column.title.system.stop" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleNotTaken" value=<lb:label key="data.export.column.title.not.taken" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleIncompleted" value=<lb:label key="data.export.column.title.incompleted" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleLoginId" value=<lb:label key="data.export.column.title.login.id" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleStudentName" value=<lb:label key="data.export.column.title.student.name" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleGrade" value=<lb:label key="data.export.column.title.grade" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleStudentId" value=<lb:label key="data.export.column.title.student.id" prefix="'" suffix="'"/>/>
			<input type="hidden" id="titleStatus" value=<lb:label key="data.export.column.title.status" prefix="'" suffix="'"/>/>
			<input type="hidden" id="scoringPopupTitle" value=<lb:label key="scoring.page.title" prefix="'" suffix="'"/>/>
			<input type="hidden" id="itemListGripCap" value=<lb:label key="scoring.itemGrid.caption" prefix="'" suffix="'"/>/>
			<input type="hidden" id="itemGripItemNo" value=<lb:label key="scoring.itemGrid.ItemNo" prefix="'" suffix="'"/>/>
			<input type="hidden" id="itemGripSubtest" value=<lb:label key="scoring.itemGrid.subtestName" prefix="'" suffix="'"/>/>
			<input type="hidden" id="itemGripScoreItm" value=<lb:label key="scoring.itemGrid.scoreItem" prefix="'" suffix="'"/>/>
			<input type="hidden" id="itemGripManual" value=<lb:label key="scoring.itemGrid.manualStatus" prefix="'" suffix="'"/>/>
			<input type="hidden" id="itemGripMaxScr" value=<lb:label key="scoring.itemGrid.maxScore" prefix="'" suffix="'"/>/>
			<input type="hidden" id="itemGripObtained" value=<lb:label key="scoring.itemGrid.scorObtn" prefix="'" suffix="'"/>/>
			<input type="hidden" id="sbsGridFirstName" value=<lb:label key="dialog.myProfile.firstName" prefix="'" suffix="'"/>/>
			<input type="hidden" id="sbsGridLastName" value=<lb:label key="dialog.myProfile.lastName" prefix="'" suffix="'"/>/>
			<input type="hidden" id="sbsGridOnStatus" value=<lb:label key="scoring.itemGrid.onTestStatus" prefix="'" suffix="'"/>/>
			<input type="hidden" id="sbsEmptyGrid" value=<lb:label key="scoring.sbs.noStuFound.message" prefix="'" suffix="'"/>/>
			<input type="hidden" id="sbiItemEmptyGrid" value=<lb:label key="scoring.sbi.itemSearchEmpty" prefix="'" suffix="'"/>/>
			<input type="hidden" id="sbiItemEmptyGridTitle" value=<lb:label key="scoring.noItems.title" prefix="'" suffix="'"/>/>
			<input type="hidden" id="sbiGridItemType" value=<lb:label key="scoring.itemGrid.itemType" prefix="'" suffix="'"/>/>
			<input type="hidden" id="sbsGridItemEmpty" value=<lb:label key="scoring.sbs.noItemsFoundMessage" prefix="'" suffix="'"/>/>
			<input type="hidden" id="responsePopupTitl" value=<lb:label key="questionpopup.header" prefix="'" suffix="'"/>/>
			<input type="hidden" id="itemGripViewRubric" value=<lb:label key="scoring.itemGrid.viewRubric" prefix="'" suffix="'"/>/>
			<input type="hidden" id="itemGripViewQues" value=<lb:label key="scoring.itemGrid.viewQues" prefix="'" suffix="'"/>/>
			<input type="hidden" id="questionPopupTitle" value=<lb:label key="questionpopup.quesTitle" prefix="'" suffix="'"/>/>
			<input type="hidden" id="rubricPopupTitle" value=<lb:label key="questionpopup.rubricTitle" prefix="'" suffix="'"/>/>
			<input type="hidden" id="ScoringStatusTitleId" value=<lb:label key="scoring.stuGrid.scoringStatusTitle" prefix="'" suffix="'"/>/>
			<input type="hidden" id="sesGridStatus" value=<lb:label key="scoring.stuGrid.status" prefix="'" suffix="'"/>/>
			<input type="hidden" id="confirmAlrt" value=<lb:label key="scoring.alert.confirm" prefix="'" suffix="'"/>/>
			<input type="hidden" id="incStudentCaption" value=<lb:label key="data.export.incomplete.scored.student" prefix="'" suffix="'"/>/>
			<table class="transparent" width="97%" style="margin: 15px auto;">
				<tr class="transparent">
					<td>
					<table class="transparent">
						<tr class="transparent">
							<td>
							<h1><lb:label key="data.export.title" /></h1>
							</td>
						</tr>
						<tr>
							<td class="subtitle"></td>
						</tr>
					</table>
					</td>
				</tr>

			</table>

			<div id = "displayMessage" 
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
				<!--<tr>
					<td width="100%">
						<div id ="resetTestId" class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix"
							style="padding-left: 5px; padding-right: 5px;">
						</div>
					</td>
				</tr>
				-->
			<tr > 
					<td>
						<div id = 'jqGrid-content-section'>
							<jsp:include page="session_student_for_export.jsp" />
						</div>
						<script>getStudentList();</script> 
					</td>
				</tr>
				
			</table> 
			
			<jsp:include page="score_by_item_details.jsp" />
			<jsp:include page="question_answer.jsp" />
			<jsp:include page="question.jsp" />
			<jsp:include page="rubric.jsp" />
			
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