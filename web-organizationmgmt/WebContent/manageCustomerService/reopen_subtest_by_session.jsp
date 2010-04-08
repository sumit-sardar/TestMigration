<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />

<table class="sortable">

	<tr class="sortableControls">
		<td class="sortableControls">
			<BR/><BR/>
		</td>
	</tr>
	<tr class="sortable">
		<td class="sortable">
		<b>Step1:</b> Specify a test access code and click <b>Search</b>.
		<BR /><BR />
			<table class="transparent">
				<tr class="transparent">
					<td class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Access Code:</td>
					<td class="transparent"><netui:textBox tagId="accessCode" dataSource="actionForm.testAccessCode"
						tabindex="1" maxlength="32" /></td>
					<td class="transparent" width="*"><netui:button styleClass="button" value="Search" type="submit"
						onClick="setElementValue('currentAction', 'applySearch');" tabindex="2" /></td>
				</tr>
			</table>
		<BR />
		<c:if test="${pageFlow.testDeliveryItemList != null}">
		<BR />
			<b>Step2:</b> Select a subtest to reset.
		<BR />
			<table class="transparent">
				<tr class="transparent">
					<td class="transparent alignRight">
						<netui:span value="${bundle.web['selecttest.label.subtestName']}:" />
					</td>
					<td class="transparent">
						<c:if test="${!hideProductNameDropDown}">
							<netui:select dataSource="actionForm.selectedSubtestName" optionsDataSource="${pageFlow.subtestNameToIndexHash}" defaultValue="${actionForm.selectedSubtestName}"
								size="1" multiple="false" onChange="setElementValueAndSubmit('currentAction', 'changeSubtest');">
							</netui:select>
							
						</c:if> 
						<c:if test="${hideProductNameDropDown}">
							<div class="formValue">
								<netui:span value="${selectedItemSetName}" styleClass="formValue" />
							</div>
						</c:if>
					</td>
				</tr>
			</table>
		</c:if> 
		<!-- START: Student search result -->
		<c:if test="${pageFlow.studentStatusDetailsList != null}">
		<a name="studentSearchResult"></a>
		<BR />
		<BR />		
		<b>Step3:</b> Select one or more students to reset for the selected section.
		<BR />
		<BR />
		<table class="sortable">
			<tr class="sortable">
				<td class="sortableControls" colspan="7">
				<table class="tableFilter">
					<tr class="tableFilter">
						<td class="tableFilter"><netui:button styleClass="button" tagId="selectAllStudents"
							value="${bundle.widgets['button.selectAllStudents']}" type="submit" action="selectAllStudents"  onClick="setElementValue('{actionForm.currentAction}', 'selectAllStudents');"/> <netui:button
							styleClass="button" tagId="deselectAllStudents " value="${bundle.widgets['button.deselectAllStudents']}"
							type="submit" action="deselectAllStudents" onClick="setElementValue('{actionForm.currentAction}', 'deselectAllStudents');"/></td>
					</tr>
				</table>
				</td>
			</tr>

			<netui-data:repeater dataSource="pageFlow.studentStatusDetailsList">
				<netui-data:repeaterHeader>

					<tr class="sortable">
						<ctb:tableSortColumnGroup columnDataSource="actionForm.studentSortColumn"
							orderByDataSource="actionForm.studentSortOrderBy" anchorName="studentSearchResult">
							<th class="sortable alignCenter" width="5%" nowrap><netui:content value="${bundle.web['common.column.select']}" /></th>
							<th class="sortable alignLeft" width="15%" nowrap><ctb:tableSortColumn value="StudentName">Student Name</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="15%" nowrap><ctb:tableSortColumn value="StudentLoginName">Login Name</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="15%" nowrap><ctb:tableSortColumn value="ExternalStudentId">Student ID</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="15%" nowrap><netui:content value="&nbsp;&nbsp;Organization"/></th>
							<th class="sortable alignLeft" width="35%" nowrap><ctb:tableSortColumn value="ItemSetName">Section Name</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="CompletionStatus">Section Status</ctb:tableSortColumn></th>
						</ctb:tableSortColumnGroup>
					</tr>

				</netui-data:repeaterHeader>
				<netui-data:repeaterItem>


					<tr class="sortable">
						<td class="sortable alignCenter" width="5%"><netui:checkBoxGroup dataSource="actionForm.selectedStudentItemId">
                &nbsp;
                <c:if
								test="${container.item.completionStatus != 'Completed' && container.item.completionStatus != 'In Progress'}">
								<netui:checkBoxOption value="${container.item.studentItemId}" disabled="true">&nbsp;</netui:checkBoxOption>
							</c:if>
							<c:if
								test="${container.item.completionStatus == 'Completed' || container.item.completionStatus == 'In Progress'}">
								<netui:checkBoxOption value="${container.item.studentItemId}" disabled="false" onClick="toggleShowButton(this, ${requestScope.disableShowDetailsButton});">&nbsp;</netui:checkBoxOption>
							</c:if>
						</netui:checkBoxGroup></td>
						<td class="sortable alignLeft" width="15%"><netui:span value="${container.item.studentName}" /></td>
						<td class="sortable alignLeft" width="15%"><netui:span value="${container.item.studentLoginName}" /></td>
						<td class="sortable alignLeft" width="15%"><netui:span value="${container.item.externalStudentId}" /></td>
						<td class="sortable alignLeft" width="15%"><netui:span value="${container.item.org_name}" /></td>
						<td class="sortable alignLeft" width="35%"><netui:span value="${container.item.itemSetName}" /></td>
						<td class="sortable alignLeft" width="10%"><netui:span value="${container.item.completionStatus}" /></td>
					</tr>

				</netui-data:repeaterItem>
				<netui-data:repeaterFooter>

					<tr class="sortable">
						<td class="sortableControls" colspan="7"><ctb:tablePager dataSource="actionForm.studentPageRequested"
							summary="request.studentPagerSummary" objectLabel="${bundle.oas['object.students']}" foundLabel="Found"
							id="studentSearchResult" anchorName="studentSearchResult" /></td>
					</tr>
					<tr class="sortable">
						<td class="sortable" colspan="7"><netui:button styleClass="button" tagId="showDetails"
							value="${bundle.widgets['button.showDetails']}" type="button" disabled="${requestScope.disableShowDetailsButton}"
							onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'showDetails','studentStatusSearchResult');" /></td>
					</tr>
				</netui-data:repeaterFooter>

			</netui-data:repeater>

		</table>
	</c:if>
	<!-- END: Student search result -->

	<c:if test="${pageFlow.showStudentDeatilsList != null}">
		<a name="studentStatusSearchResult"></a>
		<BR />
		<BR />
		<b>Step 4:</b> Provide reset request information. Enter a brief description of the reason for the request to reset.
			<BR />
		<BR />
		<table class="sortable">
				<tr class="sortable">
					<td class="sortableControls" colspan="7">
					<table class="tableFilter">
						<tr class="tableFilter">
							<td class="tableFilter">
								<table class="tableFilter" width="300">
									<tr class="tableFilter">
										<td class="tableFilter" align="right" width="100">Ticket ID:</td>
										<td class="tableFilter" width="*">
											<netui:textBox tagId="ticketId" dataSource="actionForm.ticketId" tabindex="3" maxlength="32" />
										</td>
									</tr>
									<tr class="tableFilter">
										<td class="tableFilter" align="right" width="100">Requestor:</td>
										<td class="tableFilter" width="*">
											<netui:textBox tagId="serviceRequestor"
												dataSource="actionForm.serviceRequestor" tabindex="4" maxlength="32" />
										</td>
									</tr>
									<tr class="tableFilter">
										<td class="tableFilter" width="100" align="left" valign="baseline">
											<netui:button action="reOpenSubtestForStudents" styleClass="button" type="submit" tabindex="6" 
											value="Reset" onClick="setElementValue('{actionForm.currentAction}', 'reOpenSubtestForStudents');" />
										</td>
										<td class="tableFilter" width="*"></td>
									</tr>
								</table>
							</td>
							<td class="tableFilter" valign="top">
								<table class="tableFilter">
									<tr class="tableFilter">
										<td class="tableFilter" align="right" width="100">Reason for reset:</td>
										<td class="tableFilter" rowspan="3" width="*" valign="top">
											<netui:textArea style="font-family: Arial; font-size: 10pt" tagId="requestDescription" 
												rows="3" tabindex="5" cols="50" dataSource="actionForm.requestDescription" onBlur="return checkAndTruncate(this,10)" 
													onKeyUp="checkAndTruncate(this,255)" onKeyPress="return blockEventOnMaxLength(this,255)"/>
										</td>
									</tr>
									<tr class="tableFilter"><td class="tableFilter">&nbsp;</td></tr>
								</table>
							</td>
							
						</tr>
					</table>
					</td>
				</tr>
			
			<netui-data:repeater dataSource="pageFlow.showStudentDeatilsList">
				<netui-data:repeaterHeader>
					<tr class="sortable">
						<ctb:tableSortColumnGroup columnDataSource="actionForm.studentStatusSortColumn"
							orderByDataSource="actionForm.studentStatusSortOrderBy" anchorName="studentStatusSearchResult">
							<th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="StudentLoginName">Student</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="30%"nowrap><ctb:tableSortColumn value="ItemSetName">Section Name</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="10%"nowrap><ctb:tableSortColumn value="CompletionStatus">Section Status</ctb:tableSortColumn>
							<th class="sortable alignCenter" width="12%" nowrap><ctb:tableSortColumn value="StartDateTime">Start Date</ctb:tableSortColumn>
							<th class="sortable alignCenter" width="11%" nowrap><ctb:tableSortColumn value="CompletionDateTime">Completion Date</ctb:tableSortColumn>
							<th class="sortable alignCenter" width="12%" nowrap><netui:content value="Item Answered"/>
							<th class="sortable alignCenter" width="10%" nowrap><netui:content value="Time Spent"/>
						</ctb:tableSortColumnGroup>
					</tr>


				</netui-data:repeaterHeader>
				<netui-data:repeaterItem>
					<tr class="tableFilter">
						<td class="sortable alignLeft" width="15%"><netui:span value="${container.item.studentLoginName}" /></td>
						<td class="sortable alignLeft" width="30%"><netui:span value="${container.item.itemSetName}" /></td>
						<td class="sortable alignLeft" width="10%"><netui:span value="${container.item.completionStatus}" /></td>
						<td class="sortable alignCenter" width="12%"><netui:span value="${container.item.startDateTime}" defaultValue="--"/></td>
						<td class="sortable alignCenter" width="11%"><netui:span value="${container.item.completionDateTime}" defaultValue="--"/></td>
						<td class="sortable alignCenter" width="12%"><netui:span value="${container.item.itemAnswered}" /></td>
						<td class="sortable alignCenter" width="10%"><netui:span value="${container.item.timeSpentForDisplay}" /></td>

					</tr>


				</netui-data:repeaterItem>
				<netui-data:repeaterFooter>

					<tr class="sortable">
						<td class="sortableControls" colspan="7"><ctb:tablePager dataSource="actionForm.studentStatusPageRequested"
							summary="request.studentStatusDetailsPagerSummary" objectLabel="${bundle.oas['object.students']}" foundLabel="Found"
							id="studentStatusDetailsSearchResult" anchorName="studentStatusSearchResult" /></td>
					</tr>

				</netui-data:repeaterFooter>

			</netui-data:repeater>
		</table>
	</c:if>
		</td>
</tr>

</table>

<BR/>
<c:if test="${searchResultEmpty != null}">
	<ctb:message title="Search Result" style="informationMessage">
		<netui:content value="${requestScope.searchResultEmpty}" />
	</ctb:message>
</c:if>
<c:if test="${searchResultForStudent != null}">
	<ctb:message title="Search Result" style="informationMessage">
		<netui:content value="${requestScope.searchResultForStudent}" />
	</ctb:message>
</c:if>
