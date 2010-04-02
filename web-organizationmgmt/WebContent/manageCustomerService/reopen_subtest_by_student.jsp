<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />

<!-- Parent table start-->
<table class="sortable">

	<tr class="sortableControls">
		<td class="sortableControls">
			<BR/><BR/>
		</td>
	</tr>
	<tr class="sortable">
		<td class="sortable">
			<b>Step1:</b> Specify student login (required) and test access code (optional) and click <b>Search</b> button
			<BR/>
			<table class="tableFilter">
				<tr class="tableFilter">
					<td class="tableFilter" width="100" align="right">* Student Login:</td>
					<td class="tableFilter" width="*"><netui:textBox tagId="studentLogin"
						dataSource="actionForm.studentProfile.studentLoginId" tabindex="1" /></td>
				</tr>
				<tr class="tableFilter">
					<td class="tableFilter" width="100" align="right">Access Code:</td>
					<td class="tableFilter" width="*"><netui:textBox tagId="accessCode"
						dataSource="actionForm.testAccessCode" tabindex="5" /></td>
					<td class="tableFilter" width="*">&nbsp;&nbsp; <netui:button styleClass="button" value="Search" type="submit"
						onClick="setElementValue('currentAction', 'applySearch');" tabindex="8" /></td>
				</tr>
			</table>
		
		<BR/>
		<BR/>

		<!--START: Test session search result --> 
		<c:if test="${pageFlow.testSessionList != null}">
			<b>Step 2:</b> Select a test session to view its contents
		<BR />
			<BR />
			<table class="sortable">

				<netui-data:repeater dataSource="pageFlow.testSessionList">
					<netui-data:repeaterHeader>

						<tr class="sortable">
							<ctb:tableSortColumnGroup columnDataSource="actionForm.testSessionSortColumn"
								orderByDataSource="actionForm.testSessionSortOrderBy" anchorName="testSessionSearchResult">
								<th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}" /></th>
								<th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="TestAdminName">
									<netui:content value="${bundle.web['common.column.sessionName']}" />
								</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="AccessCode">
									<netui:content value="${bundle.web['common.column.accessCode']}" />
								</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="TestName">
									<netui:content value="${bundle.web['common.column.testName']}" />
								</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="Scheduler">
									<netui:content value="${bundle.web['common.column.scheduler']}" />
								</ctb:tableSortColumn></th>
							</ctb:tableSortColumnGroup>
						</tr>

					</netui-data:repeaterHeader>

					<netui-data:repeaterItem>

						<tr class="sortable">
							<td class="sortable alignCenter"><netui:radioButtonGroup dataSource="actionForm.selectedTestSessionId">
                    &nbsp;<netui:radioButtonOption value="${container.item.testAdminId}"
									onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'findSubtestByTestSessionId', 'subtestSearchResult');">
                     &nbsp;</netui:radioButtonOption>

							</netui:radioButtonGroup></td>
							<td class="sortable"><netui-data:getData resultId="testAdminId" value="${container.item.testAdminId}" /> <%%>
							<netui:span value="${container.item.testAdminName}" defaultValue="&nbsp;" /></td>

							<td class="sortable alignLeft"><netui:span value="${container.item.accessCode}" defaultValue="&nbsp;" /></td>
							<td class="sortable"><netui:span value="${container.item.testName}" defaultValue="&nbsp;" /></td>
							<td class="sortable alignCenter"><netui:span value="${container.item.scheduler}" defaultValue="&nbsp;">

							</netui:span></td>
						</tr>

					</netui-data:repeaterItem>
					<netui-data:repeaterFooter>
						<tr class="sortable">
							<td class="sortableControls" colspan="7"><ctb:tablePager dataSource="actionForm.testSessionPageRequested"
								summary="request.testSessionPagerSummary" objectLabel="${bundle.oas['object.testSessions']}"
								anchorName="testSessionSearchResult" id="testSessionSearchResult" /></td>
						</tr>
					</netui-data:repeaterFooter>
				</netui-data:repeater>
			</table>
		</c:if> 
		<!--  END:Test session search result-->

		
		<!--START: Subtest Search Result -->
		<c:if test="${pageFlow.subtestList != null}">   
			<a name="subtestSearchResult"></a>
			<BR/>
			<BR/>
				<b>Step 3:</b> Select the subtest which you want to reset its status
			<BR/>
			<BR/>
			<table class="sortable">
	
				<netui-data:repeater dataSource="pageFlow.subtestList">
					<netui-data:repeaterHeader>
	
						<tr class="sortable">
							<ctb:tableSortColumnGroup columnDataSource="actionForm.subtestSortColumn"
								orderByDataSource="actionForm.subtestSortOrderBy" anchorName="subtestSearchResult">
								<th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}" /></th>
								<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="ItemSetName">Subtest Name</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="CompletionStatus">Subtest Status</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StartDateTime">Start Date</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="CompletionDateTime">Completion Date</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="ItemsAnswered">Items Answered</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="TimeSpent">Time Spent</ctb:tableSortColumn></th>
							</ctb:tableSortColumnGroup>
						</tr>
	
					</netui-data:repeaterHeader>
					<netui-data:repeaterItem>
	
						<tr class="sortable">
							<td class="sortable alignCenter">
							<netui:radioButtonGroup dataSource="actionForm.selectedItemSetId">&nbsp;

							<c:if test="${container.item.completionStatus != 'Completed' && container.item.completionStatus != 'In Progress'}">	                		
				                <netui:radioButtonOption value="${container.item.itemSetId}" onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'showStudentTestStatusDetails', 'subtestDetailsResult');" disabled="true">&nbsp;</netui:radioButtonOption>
							</c:if>
							<c:if test="${container.item.completionStatus == 'Completed' || container.item.completionStatus == 'In Progress'}">	                		
				                <netui:radioButtonOption value="${container.item.itemSetId}" onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'showStudentTestStatusDetails', 'subtestDetailsResult');">&nbsp;</netui:radioButtonOption>
							</c:if>
							
							</netui:radioButtonGroup></td>
							<td class="sortable"><netui:span value="${container.item.itemSetName}" /></td>
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
								summary="request.subtestPagerSummary" objectLabel="${bundle.oas['object.deliverabletestItems']}" foundLabel="Found"
								id="subtestSearchResult" anchorName="subtestSearchResult" /></td>
						</tr>
	
					</netui-data:repeaterFooter>
				</netui-data:repeater>
	
			</table>
		</c:if> 
		<!--END: Subtest Search Result -->
		
		<c:if test="${pageFlow.studentTestStatusDetailsList != null}">
			<a name="subtestDetailsResult"></a>
			<BR/>
			<BR/>
				<b>Step 4:</b> Provide request information, confirm the data, and click <b>Reset</b> button to reset its status
			<BR/>
			<BR/>
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
											value="Reset" onClick="setElementValue('{actionForm.currentAction}', 'reOpenSubtest');" />
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
												cols="70" dataSource="actionForm.requestDescription" />
										</td>
									</tr>
									<tr class="tableFilter"><td class="tableFilter">&nbsp;</td></tr>
								</table>
							</td>
							
						</tr>
					</table>
					</td>
				</tr>
				
				<netui-data:repeater dataSource="pageFlow.studentTestStatusDetailsList">
				<netui-data:repeaterHeader>
				<tr class="sortable">
					<th class="sortable alignLeft" nowrap>&nbsp;&nbsp;<netui:content value="Student" /></th>
					<th class="sortable alignLeft" nowrap>&nbsp;&nbsp;<netui:content value="Test Session" /></th>
					<th class="sortable alignLeft" nowrap>&nbsp;&nbsp;<netui:content value="Subtest" /></th>
				</tr>
				</netui-data:repeaterHeader>
				<netui-data:repeaterItem>
				<tr class="tableFilter">
					<td class="tableFilter"><netui:span value="Name: ${container.item.studentName}" /></td>
					<td class="tableFilter"><netui:span value="Name: ${selectedTestSessionName}" /></td>
					<td class="tableFilter"><netui:span value="Name: ${container.item.itemSetName}" /></td>
				</tr>

				<tr class="tableFilter">
					<td class="tableFilter"><netui:span value="Login: ${container.item.studentLoginName}" /></td>
					<td class="tableFilter"><netui:span value="Access Code: ${container.item.testAccessCode}" /></td>
					<td class="tableFilter"><netui:span value="Status: ${container.item.completionStatus}" /></td>
				</tr>

				<tr class="tableFilter">
					<td class="tableFilter"><netui:span value="ID: ${container.item.externalStudentId}" /></td>
					<td class="tableFilter"><netui:span value="ID: ${selectedTestSessionId}" /></td>
					<td class="tableFilter"><netui:span value="Order: ${container.item.itemSetOrder}" /></td>
				</tr>
				</netui-data:repeaterItem>
				</netui-data:repeater>
			</table>
		</c:if>
		</td>
	</tr>
</table>
<!-- Parent table end -->

<br/>
<a name="testSessionSearchResult"></a>
<c:if test="${searchResultEmpty != null}">
	<ctb:message title="Search Result" style="informationMessage">
		<netui:content value="${requestScope.searchResultEmpty}" />
	</ctb:message>
</c:if>
