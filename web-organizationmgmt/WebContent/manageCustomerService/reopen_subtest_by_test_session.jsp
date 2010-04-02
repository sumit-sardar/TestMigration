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
		<td class="sortable" align="left">
		<b>Step1:</b> Specify access code and click <b>Search</b> button
		<BR />
			<table class="tableFilter">
				<tr class="tableFilter">
					<td class="tableFilter" align="left">Access Code:</td>
					<td class="tableFilter" align="left"><netui:textBox tagId="accessCode" dataSource="actionForm.testAccessCode"
						tabindex="1" maxlength="32" /></td>
					<td class="tableFilter" width="*" align="left"><netui:button styleClass="button" value="Search" type="submit"
						onClick="setElementValue('currentAction', 'applySearch');" tabindex="8" /></td>
				</tr>
			</table>

		<c:if test="${pageFlow.testDeliveryItemList != null}">
		<BR />
		<BR />
			<b>Step2:</b> Select a subtest which needs to reset
		<BR />
			<table class="transparent">
				<tr class="transparent">
					<td class="transparent">
						<netui:span value="${bundle.web['selecttest.label.subtestName']}" />
					</td>
					<td class="transparent">
						<c:if test="${!hideProductNameDropDown}">
							<netui:select dataSource="actionForm.selectedSubtestName" optionsDataSource="${pageFlow.subtestNameToIndexHash}" defaultValue="${actionForm.selectedSubtestName}"
								size="1" multiple="false" onChange="setElementValueAndSubmit('currentAction', 'changeSubtest');">
							</netui:select>
							
						</c:if> 
						<c:if test="${hideProductNameDropDown}">
							<div class="formValue">
								<netui:span value="${actionForm.selectedSubtestName}" styleClass="formValue" />
							</div>
						</c:if>
					</td>
				</tr>
			</table>
		</c:if> 

		<c:if test="${pageFlow.studentStatusDetailsList != null}">
		<BR />
		<BR />		
		<b>Step3:</b> Select one or more students to reset the subtest status
		<BR />
		<table class="sortable">
			<tr class="sortable">
				<td class="sortableControls" colspan="7">
				<table class="tableFilter">
					<tr class="tableFilter">
						<td class="tableFilter"><netui:button styleClass="button" tagId="selectAllStudents"
							value="${bundle.widgets['button.selectAllStudents']}" type="submit" action="selectAllStudents" /> <netui:button
							styleClass="button" tagId="deselectAllStudents " value="${bundle.widgets['button.deselectAllStudents']}"
							type="submit" action="deselectAllStudents" /></td>
					</tr>
				</table>
				</td>
			</tr>

			<netui-data:repeater dataSource="pageFlow.studentStatusDetailsList">
				<netui-data:repeaterHeader>

					<tr class="sortable">
						<ctb:tableSortColumnGroup columnDataSource="actionForm.studentSortColumn"
							orderByDataSource="actionForm.studentSortOrderBy" anchorName="studentSearchResult">
							<th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}" /></th>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StudentName">Student Name</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StudentLoginName">Login Name</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="ExternalStudentId">Student Id</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="Org_name">Class</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="ItemSetName">Subtest Name</ctb:tableSortColumn>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="CompletionStatus">Completion Status</ctb:tableSortColumn>
						</ctb:tableSortColumnGroup>
					</tr>

				</netui-data:repeaterHeader>
				<netui-data:repeaterItem>


					<tr class="sortable">
						<td class="sortable alignCenter"><netui:checkBoxGroup dataSource="actionForm.selectedStudentItemId">
                &nbsp;
                <c:if
								test="${container.item.completionStatus != 'Completed' && container.item.completionStatus != 'In Progress'}">
								<netui:checkBoxOption value="${container.item.studentItemId}" disabled="true">&nbsp;</netui:checkBoxOption>
							</c:if>
							<c:if
								test="${container.item.completionStatus == 'Completed' || container.item.completionStatus == 'In Progress'}">
								<netui:checkBoxOption value="${container.item.studentItemId}" disabled="false">&nbsp;</netui:checkBoxOption>
							</c:if>
						</netui:checkBoxGroup></td>
						<td class="sortable"><netui:span value="${container.item.studentName}" /></td>
						<td class="sortable"><netui:span value="${container.item.studentLoginName}" /></td>
						<td class="sortable"><netui:span value="${container.item.externalStudentId}" /></td>
						<td class="sortable"><netui:span value="${container.item.org_name}" /></td>
						<td class="sortable"><netui:span value="${container.item.itemSetName}" /></td>
						<td class="sortable"><netui:span value="${container.item.completionStatus}" /></td>
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
							value="${bundle.widgets['button.showDetails']}" type="submit"
							onClick="setElementValue('currentAction', 'showDetails');" /></td>
					</tr>
				</netui-data:repeaterFooter>

			</netui-data:repeater>

		</table>
	</c:if>

	<!--END: Subtest Search Result -->

	<c:if test="${pageFlow.showStudentDeatilsList != null}">
		<a name="subtestDetailsResult"></a>
		<BR />
		<BR />
		<b>Step 4:</b> Provide request information, confirm the data, and click <b>Reset</b> button to reset its status
			<BR />
		<BR />
		<table class="sortable">
				<tr class="sortable">
					<td class="sortableControls" colspan="6">
					<table class="tableFilter">
						<tr class="tableFilter">
							<td class="tableFilter" align="left">
								<table class="tableFilter" width="300">
									<tr class="tableFilter">
										<td class="tableFilter" width="100" align="left">Ticket ID:</td>
										<td class="tableFilter" width="*" align="left">
											<netui:textBox tagId="ticketId" dataSource="actionForm.ticketId" tabindex="1" />
										</td>
									</tr>
									<tr class="tableFilter">
										<td class="tableFilter" width="100" align="left">Requestor:</td>
										<td class="tableFilter" width="*" align="left">
											<netui:textBox tagId="serviceRequestor"
												dataSource="actionForm.serviceRequestor" tabindex="5" />
										</td>
									</tr>
									<tr class="tableFilter">
										<td class="tableFilter" width="100" align="left" valign="baseline">
											<netui:button action="reOpenSubtest" styleClass="button" type="submit"
											value="Reset" onClick="setElementValue('{actionForm.currentAction}', 'reOpenSubtestForStudents');" />
										</td>
										<td class="tableFilter" width="*"></td>
									</tr>
								</table>
							</td>
							<td class="tableFilter" valign="top">
								<table class="tableFilter">
									<tr class="tableFilter">
										<td class="tableFilter" width="100" align="left">Request Description:</td>
										<td class="tableFilter" rowspan="3" width="*" valign="top">
											<netui:textArea tagId="requestDescription" rows="3"
												cols="70" dataSource="actionForm.requestDescription" onKeyDown="limitText(this,255)"/>
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
						<ctb:tableSortColumnGroup columnDataSource="actionForm.subtestSortColumn"
							orderByDataSource="actionForm.subtestSortOrderBy" anchorName="subtestStatusSearchResult">
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StudentLoginName">Student</ctb:tableSortColumn></th>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="CompletionStatus">Subtest Status</ctb:tableSortColumn>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StartDateTime">Start Date</ctb:tableSortColumn>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="CompletionDateTime">Completion Date</ctb:tableSortColumn>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="ItemAnswered">Item Answered</ctb:tableSortColumn>
							<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="TimeSpent">Time Spent</ctb:tableSortColumn>
						</ctb:tableSortColumnGroup>
					</tr>


				</netui-data:repeaterHeader>
				<netui-data:repeaterItem>
					<tr class="tableFilter">
						<td class="sortable"><netui:span value="${container.item.studentLoginName}" /></td>
						<td class="sortable"><netui:span value="${container.item.completionStatus}" /></td>
						<td class="sortable"><netui:span value="${container.item.startDateTime}" /></td>
						<td class="sortable"><netui:span value="${container.item.completionDateTime}" /></td>
						<td class="sortable"><netui:span value="${container.item.itemAnswered}" /></td>
						<td class="sortable"><netui:span value="${container.item.timeSpent}" /></td>

					</tr>


				</netui-data:repeaterItem>
				<netui-data:repeaterFooter>

					<tr class="sortable">
						<td class="sortableControls" colspan="7"><ctb:tablePager dataSource="actionForm.subtestPageRequested"
							summary="request.subtestDetailsPagerSummary" objectLabel="${bundle.oas['object.students']}" foundLabel="Found"
							id="studentSearchResult" anchorName="studentSearchResult" /></td>
					</tr>

				</netui-data:repeaterFooter>

			</netui-data:repeater>
		</table>
	</c:if>
		</td>
</tr>

</table>
<c:if test="${searchResultEmpty != null}">
	<ctb:message title="Search Result" style="informationMessage">
		<netui:content value="${requestScope.searchResultEmpty}" />
	</ctb:message>
</c:if>
