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

<!--Change MQC defect  55837 -->

<%String templatePage = "/resources/jsp/template.jsp";%>
<ctb:switch dataSource="${pageFlow.action}">
	<ctb:case value="edit">
		<% templatePage="/resources/jsp/editTemplate.jsp";%>
	</ctb:case>
	<ctb:case value="view">
		<% templatePage="/resources/jsp/viewTemplate.jsp";%>
	</ctb:case>
</ctb:switch>



<netui-template:template templatePage="<%=templatePage%>">
	<netui-template:setAttribute name="title" value="${bundle.web['selectstudents.window.title']}" />
	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.scheduleTestSessionSelectStudents']}" />
	<netui-template:section name="bodySection">

		<!--End of change for MQC defect  55837 -->

		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->


		<netui:form action="selectStudent" onSubmit="this.action = addAnchor(this.action, 'studentTableAnchor'); return true;">

			<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement" />
			<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction" />
			<netui:hidden dataSource="actionForm.selectedOrgNodeName" />
			<netui:hidden dataSource="actionForm.testAdmin.testName" />
			<netui:hidden dataSource="actionForm.testAdmin.sessionName" />
			<netui:hidden dataSource="actionForm.selectedProductName" />
			<netui:hidden dataSource="actionForm.selectedLevel" />
			<netui:hidden dataSource="actionForm.hasBreak" />
			<netui:hidden dataSource="actionForm.testAdmin.level" />
			<netui:hidden dataSource="actionForm.testAdmin.accessCode" />
			<netui:hidden dataSource="actionForm.testAdmin.isRandomize" />
			<!--added for randomize distractor-->
			<netui:hidden dataSource="actionForm.selectedTestId" />
			<netui:hidden dataSource="actionForm.selectedGrade" />

			<netui:hidden dataSource="actionForm.startDate" />
			<netui:hidden dataSource="actionForm.endDate" />
			<netui:hidden dataSource="actionForm.startTime" />
			<netui:hidden dataSource="actionForm.endTime" />
			<netui:hidden dataSource="actionForm.testAdmin.timeZone" />
			<netui:hidden dataSource="actionForm.testAdmin.location" />
			<netui:hidden dataSource="actionForm.formOperand" />
			<netui:hidden dataSource="actionForm.formAssigned" />
			<netui:hidden dataSource="actionForm.action" />
			<netui:hidden dataSource="actionForm.creatorOrgNodeId" />
			<netui:hidden dataSource="actionForm.creatorOrgNodeName" />

			<netui:hidden dataSource="actionForm.selectedAccommodationElements" />
			<netui:hidden dataSource="actionForm.isCopyTest" />
			<netui:hidden dataSource="actionForm.filterVisible" />

			<netui:hidden dataSource="actionForm.orgStatePathList.maxPageRequested" />
			<netui:hidden dataSource="actionForm.studentStatePathList.maxPageRequested" />

			<netui:hidden dataSource="actionForm.autoLocator" />
			<!--Change For License LM2-->
			<netui:hidden dataSource="actionForm.licensePercentage" />
			<netui:hidden dataSource="actionForm.testAdmin.productId" />


			<input type="hidden" id="validateTest" name="validateTest" value="noValidateTest" />

			<netui-data:getData resultId="showAccommodations" value="${actionForm.showAccommodations}" />
			<netui-data:getData resultId="offGradeTestingDisabled" value="${pageFlow.offGradeTestingDisabled}" />
			<netui-data:getData resultId="displayLicenseBar" value="${sessionScope.displayLicenseBar}" />
			<netui-data:getData resultId="licensebarColor" value="${pageFlow.licenseBarColor}" />


			<ctb:switch dataSource="${actionForm.action}">



				<!--change for licnese-->
				<%!String color = "red";%>
				<c:if test="${licensebarColor =='RED'}">
					<% color = "red";%>
				</c:if>
				<c:if test="${licensebarColor =='YELLOW'}">
					<% color = "yellow";%>
				</c:if>
				<c:if test="${licensebarColor =='GREEN'}">
					<% color = "green";%>
				</c:if>


				<table width="100%" cellpadding="0" cellspacing="0" class="transparent">

					<tr>
						<td nowrap=""><font size="6"> <ctb:case value="schedule">
							<c:if test="${isCopyTest}">
								<h1><netui:span value="${bundle.web['selectstudents.title.copy']}" /></h1>
							</c:if>
							<c:if test="${! isCopyTest}">
								<h1><netui:span value="${bundle.web['selectstudents.title.schedule']}" /></h1>
							</c:if>
						</ctb:case> <ctb:case value="edit">
							<h1><netui:span value="${bundle.web['selectstudents.title.edit']}" /></h1>
						</ctb:case> <ctb:case value="view">
							<h1><netui:span value="${bundle.web['selectstudents.title.view']}" /></h1>
						</ctb:case> </ctb:switch> </font></td>

						<td class="transparent"></td>
						<td width="100%" rowspan="2" align="right" valign="top"><c:if test="${displayLicenseBar}">
							<table width="150" height="100%" cellpadding="0" cellspacing="2">
								<tr bgcolor="green">
									<td class="transparent-label" width="100%" height="100%" align="left" nowrap="" bgcolor="#FFFFFF"><netui:span
										value="${bundle.web['licenses.title']}" /></td>
								</tr>

								<tr>
									<td height="100%" align="center" nowrap="" class="transparent" bgcolor="<%=color%>"><c:if
										test="${licensebarColor =='RED'}">

										<netui:span value="${actionForm.licensePercentage}" style="background-color:#ff0000;color:#ffffff;width:30px" />
									</c:if> <c:if test="${licensebarColor =='YELLOW'}">

										<netui:span value="${actionForm.licensePercentage}" style="background-color:#ffff00" />
									</c:if> <c:if test="${licensebarColor =='GREEN'}">

										<netui:span value="${actionForm.licensePercentage}" style="background-color:#347C17;color:#ffffff" />
									</c:if></td>
								</tr>
							</table>
						</c:if></td>

						<td rowspan="2">
						<table width="25">
							<tr>
								<td></td>
							</tr>
						</table>
						</td>

					</tr>


					<!--change for licnese-->



				</table>


				<!--End of change License-->


				<p><netui:content value="${bundle.web['selectstudents.selectStudents.introtext']}" /></p>

				<!-- message for licnese -->
				<jsp:include page="/selectStudentPageflow/show_message.jsp" />



				<table class="transparent" width="100%">
					<tr class="transparent">

						<td class="transparent" width="75%"><!-- add table-->
						<table class="transparent" width="75%">

							<tr class="transparent">
								<td class="transparent"><netui:span
									value="${bundle.web['selectstudents.selectStudents.fieldNoEdit.testName']}" /></td>
								<td class="transparent">
								<div class="formValueLarge"><netui:span value="${actionForm.testAdmin.testName}"
									styleClass="formValueLarge" /></div>
								</td>
							</tr>
							<tr class="transparent">
								<td class="transparent"><netui:span
									value="${bundle.web['selectstudents.selectStudents.fieldNoEdit.testSessionName']}" /></td>
								<td class="transparent">
								<div class="formValueLarge"><netui:span value="${actionForm.testAdmin.sessionName}"
									styleClass="formValueLarge" /></div>
								</td>
							</tr>
							<tr class="transparent">
								<td class="transparent"><netui:span
									value="${bundle.web['selectstudents.selectStudents.fieldNoEdit.totalStudents']}" /></td>
								<td class="transparent">
								<div class="formValue"><netui:span value="${actionForm.selectedStudentCount}" styleClass="formValue" /></div>
								</td>
							</tr>
							<tr class="transparent">
								<c:if test="${sessionScope.supportAccommodations}">
									<td class="transparent"><netui:span
										value="${bundle.web['selectstudents.selectStudents.fieldNoEdit.studentsWAccomm']}" /></td>
									<td class="transparent">
									<div class="formValue"><netui:span value="${actionForm.selectedStudentWithAccommodationCount}"
										styleClass="formValue" /></div>
									</td>
								</c:if>
								<c:if test="${! sessionScope.supportAccommodations}">
									<td class="transparent">&nbsp;</td>
									<td class="transparent">&nbsp;</td>
								</c:if>
							</tr>

						</table>


						</td>



					</tr>
				</table>


				<br />

				<table class="transparent">
					<tr class="transparent">
						<td class="transparent" style="vertical-align: top" width="40%"><img
							src="/TestAdministrationWeb/resources/images/legacy/step1_off.gif" border="0" height="15" width="15">&nbsp;
						<b><netui:span value="${bundle.web['selectstudents.filter.title']}" /></b>
						<p><netui:span value="${bundle.web['selectstudents.filter.introtext']}" /> <c:if
							test="${offGradeTestingDisabled}">
							<netui:span value="${bundle.web['selectstudents.selectStudents.gradePreselected']}" />
						</c:if></p>
						<table class="sortable">
							<tr class="sortableControls">

								<td class="sortableControls" style="padding: 5px;"><netui:span
									value="${bundle.web['selectstudents.filter.fieldDrop.grade']}" /><br>
								<netui:select optionsDataSource="${pageFlow.gradeOptions}" dataSource="actionForm.selectedGrade"
									style="width: 260px" size="1" multiple="false" disabled="${pageFlow.offGradeTestingDisabled}" /></td>

							</tr>

							<c:if test="${sessionScope.supportAccommodations}">
								<tr class="sortableControls">
									<td class="sortableControls" style="padding: 5px;"><netui:span
										value="${bundle.web['selectstudents.accommodations']}" />:<br />
									<netui:select optionsDataSource="${pageFlow.accommodationOperandOptions}"
										dataSource="actionForm.accommodationOperand" style="width: 260px" size="1" multiple="false"
										onChange="setElementValueAndSubmit('currentAction', 'changeAccommodation');" /> <%
        Boolean showAccommodationsBoolean = (Boolean) pageContext.getAttribute("showAccommodations");
        boolean showAccommodations = showAccommodationsBoolean == null? false:showAccommodationsBoolean.booleanValue();
        
        if (showAccommodations) { %> <br />
									<netui:select optionsDataSource="${pageFlow.selectedAccommodationsOptions}"
										dataSource="actionForm.selectedAccommodations" style="width: 260px" size="4" multiple="true"
										onClick="enableElementById('applyFilter');" /><br />
									<br />
									<netui:span value="${bundle.web['selectstudents.accommodations.info']}" /> <%      } %>
									</td>
								</tr>
							</c:if>

							<tr class="sortableControls">
								<td class="sortableControls aligncenter"><br>
								<netui:button type="button" tagId="applyFilter" value="Apply"
									onClick="setElementValueAndSubmit('currentAction', 'apply'); return false;"
									disabled="${requestScope.disableApply}" /> <netui:button type="button" tagId="clearFilter" value="Clear"
									onClick="setElementValueAndSubmit('currentAction', 'clear'); return false;" /><br>
								&nbsp;</td>
							</tr>
						</table>
						</td>

						<td class="transparent" width="10">&nbsp;</td>

						<td class="transparent" style="vertical-align: top"><img
							src="/TestAdministrationWeb/resources/images/legacy/step2_off.gif" border="0" width="15" height="15">&nbsp;
						<b><netui:span value="${bundle.web['selectstudents.organizations.title']}" /></b>
						<p><netui:span value="${bundle.web['selectstudents.organizations.introtext']}" /></p>

						<a name="pathListAnchor"><!-- pathListAnchor --></a>
						<table class="sortable">

							<tr class="sortable">
								<td class="sortableControls" colspan="4"><ctb:tablePathList valueDataSource="actionForm.orgNodeId"
									labelDataSource="actionForm.orgNodeName" pathList="request.orgNodePath" /></td>
							</tr>

							<netui-data:repeater dataSource="requestScope.orgNodes">
								<netui-data:repeaterHeader>

									<tr class="sortable">
										<ctb:tableSortColumnGroup columnDataSource="actionForm.orgStatePathList.sortColumn"
											orderByDataSource="actionForm.orgStatePathList.sortOrderBy" anchorName="pathListAnchor">
											<th class="sortable alignCenter" width="8%" nowrap><netui:content
												value="${bundle.web['common.column.select']}" /></th>
											<th class="sortable alignLeft" width="65%" nowrap><ctb:tableSortColumn value="OrgNodeName">
												<netui:content value="${requestScope.orgCategoryName}" />
											</ctb:tableSortColumn></th>
											<c:if test="${noFilterApplied != null}">
												<th class="sortable alignRight" width="*" nowrap><netui:content
													value="${bundle.web['selectstudents.selectStudents.totalStudents']}" /></th>
											</c:if>
											<c:if test="${noFilterApplied == null}">
												<th class="sortable alignRight" width="*" nowrap><netui:content
													value="${bundle.web['selectstudents.selectStudents.filteredStudents']}" /></th>
											</c:if>
										</ctb:tableSortColumnGroup>
									</tr>

								</netui-data:repeaterHeader>
								<netui-data:repeaterItem>

									<tr class="sortable">
										<td class="sortable alignCenter"><netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId">
                &nbsp;<netui:radioButtonOption value="${container.item.id}"
												onClick="setElementValueAndSubmit('actionElement', 'actionElement');">&nbsp;</netui:radioButtonOption>
										</netui:radioButtonGroup></td>
										<td class="sortable alignLeft"><ctb:switch dataSource="{container.item.clickable}">
											<ctb:case value="true">
												<ctb:tablePathEntry srcLabelDataSource="${container.item.name}" srcValueDataSource="${container.item.id}"
													dstLabelDataSource="actionForm.orgNodeName" dstValueDataSource="actionForm.orgNodeId" />
											</ctb:case>
											<ctb:case value="false">
												<netui:span value="${container.item.name}" />
											</ctb:case>
										</ctb:switch></td>
										<td class="sortable alignRight"><netui:span value="${container.item.filteredCount}" /></td>
									</tr>

								</netui-data:repeaterItem>
								<netui-data:repeaterFooter>

									<tr class="sortable">
										<td class="sortableControls" colspan="4"><ctb:tablePager
											dataSource="actionForm.orgStatePathList.pageRequested" summary="request.orgPagerSummary"
											objectLabel="${bundle.oas['object.organizations']}" anchorName="pathListAnchor" id="pathListAnchor" /></td>
									</tr>

								</netui-data:repeaterFooter>
							</netui-data:repeater>
							<ctb:tableNoResults dataSource="request.orgNodes">
								<tr class="sortable">
									<td class="sortable" colspan="4"><ctb:message title="{bundle.web['common.message.title.noOrganizations']}"
										style="tableMessage">
										<netui:content value="${bundle.web['selectstudents.organizations.messageInfo']}" />
									</ctb:message></td>
								</tr>
							</ctb:tableNoResults>

						</table>

						</td>
					</tr>
				</table>

				<br />

				<!-- LM10: when error occurs, show error div instead of student table -->
				<netui-data:getData resultId="messageType" value="${actionForm.message}" />
				<c:if test="${messageType == null}">
					<a name="studentTableAnchor"><!-- studentTableAnchor --></a>
				</c:if>

				<c:if test="${nodeContainsStudents != null}">

					<h4><img src="/TestAdministrationWeb/resources/images/legacy/step3_off.gif" border="0" height="15" width="15">&nbsp;
					<netui:span value="${requestScope.selectedOrgNodeName}" />&nbsp;<netui:span
						value="${bundle.web['selectstudents.student.studentText']}" /></h4>


					<c:if test="${hasStudentNodes != null}">
						<p><netui:content value="${bundle.web['selectstudents.student.introtext']}" /></p>
					</c:if>

					<table class="sortable">

						<tr class="sortable">
							<c:if test="${hasStudentNodes != null}">
								<td class="sortableControls" colspan="12"><netui:button type="submit" value="Add All Students"
									onClick="setElementValue('currentAction', 'addAll');" /> <netui:button type="button"
									value="Update Total"
									onClick="setElementValueAndSubmitWithAnchor('currentAction', 'updateTotal', 'topPage');" /></td>
							</c:if>
							<c:if test="${hasStudentNodes == null}">
								<td class="sortableControls" colspan="12" height="25">&nbsp;</td>
							</c:if>
						</tr>

						<netui-data:repeater dataSource="requestScope.studentNodes">
							<netui-data:repeaterHeader>

								<tr class="sortable">
									<ctb:tableSortColumnGroup columnDataSource="actionForm.studentStatePathList.sortColumn"
										orderByDataSource="actionForm.studentStatePathList.sortOrderBy" anchorName="studentTableAnchor">
										<th class="sortable alignCenter" nowrap><netui:span value="${bundle.web['common.column.select']}" /></th>
										<th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="LastName">
											<netui:span value="Last Name" />
										</ctb:tableSortColumn></th>
										<th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="FirstName">
											<netui:span value="First Name" />
										</ctb:tableSortColumn></th>
										<th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="MiddleName">
											<netui:span value="M.I" />
										</ctb:tableSortColumn></th>
										<th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="Grade">
											<netui:span value="Grade" />
										</ctb:tableSortColumn></th>
										<th class="sortable alignCenter" nowrap><netui:span value="Status" /></th>
										<c:if test="${sessionScope.supportAccommodations}">
											<th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="Calculator">
												<netui:span value="Calculator" />
											</ctb:tableSortColumn></th>
											<th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="HasColorFontAccommodations">
												<netui:span value="Color/Font" />
											</ctb:tableSortColumn></th>
											<th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="TestPause">
												<netui:span value="Pause" />
											</ctb:tableSortColumn></th>
											<th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="ScreenReader">
												<netui:span value="Reader" />
											</ctb:tableSortColumn></th>
											<th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="UntimedTest">
												<netui:span value="Untimed" />
											</ctb:tableSortColumn></th>
										</c:if>
									</ctb:tableSortColumnGroup>
								</tr>
							</netui-data:repeaterHeader>
							<netui-data:repeaterItem>
								<tr class="sortable">
									<td class="sortable alignCenter"><ctb:switch dataSource="{container.item.status.editable}">
										<ctb:case value="T">
											<netui:checkBoxGroup dataSource="actionForm.selectedStudentOrgList">
                        &nbsp;<netui:checkBoxOption value="${container.item.extElmId}">&nbsp;</netui:checkBoxOption>
											</netui:checkBoxGroup>
										</ctb:case>
										<ctb:case value="F">
											<netui:checkBoxGroup dataSource="actionForm.selectedStudentOrgList" disabled="true">
                        &nbsp;<netui:checkBoxOption value="${container.item.extElmId}">&nbsp;</netui:checkBoxOption>
											</netui:checkBoxGroup>
										</ctb:case>
									</ctb:switch></td>
									<td class="sortable alignLeft"><netui:span value="${container.item.lastName}" /></td>
									<td class="sortable alignLeft"><netui:span value="${container.item.firstName}" /></td>
									<td class="sortable alignCenter"><netui:span value="${container.item.middleName}" /></td>
									<td class="sortable alignCenter"><netui:span value="${container.item.grade}" /></td>
									<td class="sortable alignCenter"><netui-data:getData resultId="statusCode"
										value="${container.item.status.code}" /> <netui-data:getData resultId="statusToolTip"
										value="${container.item.extPin2}" /> <%
                String toolTipMsg = (String)pageContext.getAttribute("statusToolTip");
            %> <c:if test="${statusCode != 'Ses'}">
										<netui:content value="${container.item.status.code}" />
									</c:if> <c:if test="${statusCode == 'Ses'}">
										<a href="#" style="text-decoration: none" title="<%= toolTipMsg %>" onclick="return false;"><netui:content
											value="${container.item.status.code}" /></a>
									</c:if></td>
									<c:if test="${sessionScope.supportAccommodations}">
										<td class="sortable alignCenter"><ctb:switch dataSource="{container.item.calculator}">
											<ctb:case value="T">
												<netui:image src="/TestAdministrationWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
										<td class="sortable alignCenter"><ctb:switch dataSource="{container.item.hasColorFontAccommodations}">
											<ctb:case value="true">
												<netui:image src="/TestAdministrationWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
										<td class="sortable alignCenter"><ctb:switch dataSource="{container.item.testPause}">
											<ctb:case value="T">
												<netui:image src="/TestAdministrationWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
										<td class="sortable alignCenter"><ctb:switch dataSource="{container.item.screenReader}">
											<ctb:case value="T">
												<netui:image src="/TestAdministrationWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
										<td class="sortable alignCenter"><ctb:switch dataSource="{container.item.untimedTest}">
											<ctb:case value="T">
												<netui:image src="/TestAdministrationWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
									</c:if>
								</tr>

							</netui-data:repeaterItem>

							<netui-data:repeaterFooter>

								<tr class="sortable">
									<td class="sortableControls" colspan="12"><ctb:tablePager
										dataSource="actionForm.studentStatePathList.pageRequested" summary="request.studentPagerSummary"
										objectLabel="Students" anchorName="studentTableAnchor" id="studentTableAnchor" /></td>
								</tr>

							</netui-data:repeaterFooter>
						</netui-data:repeater>
						<ctb:tableNoResults dataSource="request.studentNodes">
							<tr class="sortable">
								<td class="sortable" colspan="12"><ctb:message title="{bundle.web['common.message.title.noStudents']}"
									style="tableMessage">
									<netui:content value="${bundle.web['selectstudents.students.messageInfo']}" />
								</ctb:message></td>
							</tr>
						</ctb:tableNoResults>

					</table>

					<br />

					<table class="transparent" cellspacing="0" cellpadding="0" border="0">
						<tr class="transparent">
							<td class="transparent" colspan="2">Status Key:</td>
						</tr>
						<tr class="transparent">
							<td class="transparent" width="20">Cmp</td>
							<td class="transparent" width="500">Completed the test.</td>
						</tr>
						<tr class="transparent">
							<td class="transparent" width="20">Inp</td>
							<td class="transparent" width="500">In progress, taking the test.</td>
						</tr>
						<tr class="transparent">
							<td class="transparent" width="20">Org</td>
							<td class="transparent" width="500">Scheduled through other organization. Cannot override.</td>
						</tr>
						<tr class="transparent">
							<td class="transparent" width="20">Ses</td>
							<td class="transparent" width="500">Previously scheduled for this test in different test session.</td>
						</tr>
					</table>

					<hr>
				</c:if>
				<%-- nodeContainsStudents != null --%>


				<p><br>
				<netui:button type="submit" value="${bundle.web['common.button.ok']}" action="selectStudentDone" /> <netui:button
					type="submit" value="${bundle.web['common.button.cancel']}" action="selectStudentCancel"
					onClick="return verifyCancelAddStudents();" /></p>
		</netui:form>

		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
	</netui-template:section>
</netui-template:template>
