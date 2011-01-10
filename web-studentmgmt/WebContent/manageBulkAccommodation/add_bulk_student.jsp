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



<%String templatePage = "/resources/jsp/template_BulkAccommodation.jsp";%>
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

	

		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->


		<netui:form action="findBulkStudent" onSubmit="this.action = addAnchor(this.action, 'studentTableAnchor'); return true;">

			<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement" />
			<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction" />
			<netui:hidden dataSource="actionForm.selectedOrgNodeName" />
			
			<netui:hidden dataSource="actionForm.action" />
			<netui:hidden dataSource="actionForm.creatorOrgNodeId" />
			<netui:hidden dataSource="actionForm.creatorOrgNodeName" />

			<netui:hidden dataSource="actionForm.selectedAccommodationElements" />
		
			<netui:hidden dataSource="actionForm.filterVisible" />

			<netui:hidden dataSource="actionForm.orgMaxPage" />
			<netui:hidden dataSource="actionForm.studentMaxPage" />

			


			<input type="hidden" id="validateTest" name="validateTest" value="noValidateTest" />
			<input type="hidden" name="firstName" id="firstName" value="ISTEP" />
			<input type="hidden" name="lastName" id="lastName" value="ISTEP" />
			<input type="hidden" name="middleName" id="middleName" value="ISTEP" />

			<netui-data:getData resultId="showAccommodations" value="${actionForm.showAccommodations}" />
			<netui-data:getData resultId="TotalSelectedStudents" value="${actionForm.studentPagerSummary.totalSelectedObjects}" />

			<netui-data:getData resultId="selectedDemo1" value="${actionForm.selectedDemo1}"/>
			<netui-data:getData resultId="selectedDemo2" value="${actionForm.selectedDemo2}"/>
			<netui-data:getData resultId="selectedDemo3" value="${actionForm.selectedDemo3}"/>
			<%
				String selectedDemo1 = (String) pageContext.getAttribute("selectedDemo1");
				String selectedDemo2 = (String) pageContext.getAttribute("selectedDemo2");
				String selectedDemo3 = (String) pageContext.getAttribute("selectedDemo3");
				Integer	TotalSelectedStudents = (Integer) pageContext.getAttribute("TotalSelectedStudents");
			%>

				<table width="100%" cellpadding="0" cellspacing="0" class="transparent">

					<tr>
						<td nowrap="">
							<font size="6"> 
									<h1><netui:span value="${bundle.web['selectstudents.title.BulkAccommodation']}" /></h1>
							</font>
						</td>
					</tr>
					<tr><td>&nbsp;&nbsp;</td></tr>
					<tr>
						<td nowrap="">
									<h3><netui:span value="${bundle.web['selectstudents.title.BulkAccommodation.selectStudent']}" /></h3>
						</td>
					</tr>
					

				</table>

				<p><netui:content value="${bundle.web['selectstudents.selectStudents.introtext']}" /></p>

				<!-- successfull message for bulk accommodation -->
				
				<jsp:include page="/manageBulkAccommodation/show_message.jsp" />
				<br />

				<table class="transparent">
					<tr class="transparent">
						<td class="transparent" style="vertical-align: top" width="40%"><img
							src="/StudentManagementWeb/resources/images/legacy/step1_off.gif" border="0" height="15" width="15">&nbsp;
						<b><netui:span value="${bundle.web['selectstudents.filter.title']}" /></b>
						<p><netui:span value="${bundle.web['selectstudents.filter.introtext']}" /> <c:if
							test="${offGradeTestingDisabled}">
							<netui:span value="${bundle.web['selectstudents.selectStudents.gradePreselected']}" />
						</c:if></p>
						<table class="sortable">
							<tr class="sortableControls">
								<td class="sortableControls" style="padding: 5px;"><netui:span
									value="${bundle.web['selectstudents.filter.fieldDrop.demographics']}" /><br>
									<netui:select optionsDataSource="${pageFlow.demographic1}" dataSource="actionForm.selectedDemo1"
									 size="1" multiple="false" onChange="setElementValueAndSubmit('currentAction', 'getDemoData');"/>
								</td>
								<td class="sortableControls" style="padding: 5px;"><netui:span
									value="${bundle.web['selectstudents.filter.fieldDrop.demographicsValues']}" /><br>
									<c:if test="${selectedDemo1 == 'Show All'}">
										<netui:select optionsDataSource="${pageFlow.demographicValues1}" dataSource="actionForm.selectedDemoValue1"
										 size="1" multiple="false" disabled="true"/>
									</c:if>
									<c:if test="${selectedDemo1 != 'Show All'}">
										<netui:select optionsDataSource="${pageFlow.demographicValues1}" dataSource="actionForm.selectedDemoValue1"
										 size="1" multiple="false"/>
									</c:if>
								</td>
							</tr>
							<tr class="sortableControls">
								<td class="sortableControls" style="padding: 5px;"><netui:span
									value="${bundle.web['selectstudents.filter.fieldDrop.demographics']}" /><br>
									<netui:select optionsDataSource="${pageFlow.demographic2}" dataSource="actionForm.selectedDemo2"
									 size="1" multiple="false" onChange="setElementValueAndSubmit('currentAction', 'getDemoData');"/>
								</td>
								<td class="sortableControls" style="padding: 5px;"><netui:span
									value="${bundle.web['selectstudents.filter.fieldDrop.demographicsValues']}" /><br>
									<c:if test="${selectedDemo2 == 'Show All'}">
										<netui:select optionsDataSource="${pageFlow.demographicValues2}" dataSource="actionForm.selectedDemoValue2"
										 size="1" multiple="false" disabled="true"/>
									</c:if>
									<c:if test="${selectedDemo2 != 'Show All'}">
										<netui:select optionsDataSource="${pageFlow.demographicValues2}" dataSource="actionForm.selectedDemoValue2"
										 size="1" multiple="false"/>
									</c:if>
								</td>
							</tr>
							<tr class="sortableControls">
								<td class="sortableControls" style="padding: 5px;"><netui:span
									value="${bundle.web['selectstudents.filter.fieldDrop.demographics']}" /><br>
									<netui:select optionsDataSource="${pageFlow.demographic3}" dataSource="actionForm.selectedDemo3"
									 size="1" multiple="false" onChange="setElementValueAndSubmit('currentAction', 'getDemoData');"/>
								</td>
								<td class="sortableControls" style="padding: 5px;"><netui:span
									value="${bundle.web['selectstudents.filter.fieldDrop.demographicsValues']}" /><br>
									<c:if test="${selectedDemo3 == 'Show All'}">
										<netui:select optionsDataSource="${pageFlow.demographicValues3}" dataSource="actionForm.selectedDemoValue3"
										 size="1" multiple="false" disabled="true"/>
									</c:if>
									<c:if test="${selectedDemo3 != 'Show All'}">
										<netui:select optionsDataSource="${pageFlow.demographicValues3}" dataSource="actionForm.selectedDemoValue3"
										 size="1" multiple="false"/>
									</c:if>
								</td>
							</tr>
							<tr class="sortableControls">

								<td class="sortableControls" style="padding: 5px;"><netui:span
									value="${bundle.web['selectstudents.filter.fieldDrop.grade']}" /><br>
								<netui:select optionsDataSource="${pageFlow.gradeOptions}" dataSource="actionForm.selectedGrade"
									 size="1" multiple="false" /></td>

							<!--  </tr>-->

							<c:if test="${sessionScope.supportAccommodations}">
								<!-- <tr class="sortableControls"> -->
									<td class="sortableControls" style="padding: 5px;"><netui:span
										value="${bundle.web['selectstudents.accommodations']}" />:<br />
									<netui:select optionsDataSource="${pageFlow.accommodationOperandOptions}"
										dataSource="actionForm.accommodationOperand"  size="1" multiple="false"
										onChange="setElementValueAndSubmit('currentAction', 'changeAccommodation');" /> <%
        Boolean showAccommodationsBoolean = (Boolean) pageContext.getAttribute("showAccommodations");
        boolean showAccommodations = showAccommodationsBoolean == null? false:showAccommodationsBoolean.booleanValue();
        
        if (showAccommodations) { %> <br />
									<netui:select optionsDataSource="${pageFlow.selectedAccommodationsOptions}"
										dataSource="actionForm.selectedAccommodations"  size="4" multiple="true"
										onClick="enableElementById('applyFilter');" /><br />
									<br />
									<netui:span value="${bundle.web['selectstudents.accommodations.info']}" /> <%      } %>
									</td>
								<!-- </tr> -->
							</c:if>
							</tr>
							<tr class="sortableControls">
								<td class="sortableControls aligncenter" colspan="2"><br>
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
							src="/StudentManagementWeb/resources/images/legacy/step2_off.gif" border="0" width="15" height="15">&nbsp;
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
										<ctb:tableSortColumnGroup columnDataSource="actionForm.orgSortColumn"
											orderByDataSource="actionForm.orgSortOrderBy" anchorName="pathListAnchor">
											<th class="sortable alignCenter" width="8%" nowrap><netui:content
												value="${bundle.web['common.column.select']}" /></th>
											<th class="sortable alignLeft" width="65%" nowrap><ctb:tableSortColumn value="OrgNodeName">
												<netui:content value="${requestScope.orgCategoryName}" />
											</ctb:tableSortColumn></th>
											<c:if test="${!appliedFilterFlag}">
												<th class="sortable alignRight" width="*" nowrap><netui:content
													value="${bundle.web['selectstudents.selectStudents.totalStudents']}" /></th>
											</c:if>
											<c:if test="${appliedFilterFlag}">
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
										<td class="sortable alignLeft"><ctb:switch dataSource="${container.item.clickable}">
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
											dataSource="actionForm.orgPageRequested" summary="request.orgPagerSummary"
											objectLabel="${bundle.oas['object.organizations']}" anchorName="pathListAnchor" id="pathListAnchor" /></td>
									</tr>

								</netui-data:repeaterFooter>
							</netui-data:repeater>
							<ctb:tableNoResults dataSource="request.orgNodes">
								<tr class="sortable">
									<td class="sortable" colspan="4"><ctb:message title="${bundle.web['common.message.title.noOrganizations']}"
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

					<h4><img src="/StudentManagementWeb/resources/images/legacy/step3_off.gif" border="0" height="15" width="15">&nbsp;
					<netui:span value="${requestScope.selectedOrgNodeName}" />&nbsp;<netui:span
						value="${bundle.web['selectstudents.student.studentText']}" /></h4>


					<table class="sortable">

						<tr class="sortable">
							<c:if test="${hasStudentNodes != null}">
								<td class="sortableControls" colspan="12"><netui:button type="submit" value="Add All Students"
									onClick="setElementValue('currentAction', 'addAll');" /> </td>
							</c:if>
							<c:if test="${hasStudentNodes == null}">
								<td class="sortableControls" colspan="12" height="25">&nbsp;</td>
							</c:if>
						</tr>

						<netui-data:repeater dataSource="requestScope.studentNodes">
							<netui-data:repeaterHeader>

								<tr class="sortable">
									<ctb:tableSortColumnGroup columnDataSource="actionForm.studentSortColumn"
										orderByDataSource="actionForm.studentSortOrderBy" anchorName="studentTableAnchor">
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
											<th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="UntimedTest">
												<netui:span value="HighLighter" />
											</ctb:tableSortColumn></th>
										</c:if>
									</ctb:tableSortColumnGroup>
								</tr>
							</netui-data:repeaterHeader>
							<netui-data:repeaterItem>
								<tr class="sortable">
									<td class="sortable alignCenter">
											<netui:checkBoxGroup  id="bulkCheckBox"  dataSource="actionForm.selectedStudentOrgList">
                        &nbsp;<netui:checkBoxOption  value="${container.item.studentId}" onClick="toggleShowButton(this)">&nbsp;</netui:checkBoxOption>
											</netui:checkBoxGroup>
										</td>
									<td class="sortable alignLeft"><netui:span value="${container.item.lastName}" /></td>
									<td class="sortable alignLeft"><netui:span value="${container.item.firstName}" /></td>
									<td class="sortable alignCenter"><netui:span value="${container.item.middleName}" /></td>
									<td class="sortable alignCenter"><netui:span value="${container.item.grade}" /></td>
								
									<c:if test="${sessionScope.supportAccommodations}">
										<td class="sortable alignCenter"><ctb:switch dataSource="${container.item.calculator}">
											<ctb:case value="T">
												<netui:image src="/StudentManagementWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
										<td class="sortable alignCenter"><ctb:switch dataSource="${container.item.hasColorFontAccommodations}">
											<ctb:case value="true">
												<netui:image src="/StudentManagementWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
										<td class="sortable alignCenter"><ctb:switch dataSource="{container.item.testPause}">
											<ctb:case value="T">
												<netui:image src="/StudentManagementWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
										<td class="sortable alignCenter"><ctb:switch dataSource="{container.item.screenReader}">
											<ctb:case value="T">
												<netui:image src="/StudentManagementWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
										<td class="sortable alignCenter"><ctb:switch dataSource="{container.item.untimedTest}">
											<ctb:case value="T">
												<netui:image src="/StudentManagementWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
										<td class="sortable alignCenter"><ctb:switch dataSource="{container.item.highLighter}">
											<ctb:case value="T">
												<netui:image src="/StudentManagementWeb/resources/images/check.gif" width="20" height="20" />
											</ctb:case>
										</ctb:switch></td>
									</c:if>
								</tr>

							</netui-data:repeaterItem>

							<netui-data:repeaterFooter>

								<tr class="sortable">
									<td class="sortableControls" colspan="12"><ctb:tablePager
										dataSource="actionForm.studentPageRequested" summary="request.studentPagerSummary"
										objectLabel="Students" anchorName="studentTableAnchor" id="studentTableAnchor" /></td>
								</tr>

							</netui-data:repeaterFooter>
						</netui-data:repeater>
						<ctb:tableNoResults dataSource="request.studentNodes">
							<tr class="sortable">
								<td class="sortable" colspan="12"><ctb:message title="${bundle.web['common.message.title.noStudents']}"
									style="tableMessage">
									<netui:content value="${bundle.web['selectstudents.students.messageInfo']}" />
								</ctb:message></td>
							</tr>
						</ctb:tableNoResults>

					</table>

					<br />

				</c:if>
				<%-- nodeContainsStudents != null --%>

				<jsp:include page="/manageBulkAccommodation/bulkAccommodationSelection.jsp" />

				<p>
					<br>
					<c:if test="${TotalSelectedStudents <= 0}">
						<netui:button tagId ="bulkSubmit" type="submit" value="${bundle.web['common.button.done']}" action="saveBulkStudentData"  disabled ="true"/>
				
					</c:if>
					<c:if test="${TotalSelectedStudents > 0}">
						<netui:button tagId ="bulkSubmit" type="submit" value="${bundle.web['common.button.done']}" action="saveBulkStudentData" />
				
					</c:if>
						<netui:button type="submit" value="${bundle.web['common.button.cancel']}" action="selectStudentCancel"
						onClick="return verifyCancelAddStudents();" />
				</p>
		</netui:form>

		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
	</netui-template:section>
</netui-template:template>
