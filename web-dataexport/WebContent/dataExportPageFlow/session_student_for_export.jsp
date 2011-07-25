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

<netui-template:template templatePage="/resources/jsp/template.jsp">
	<!-- 
template_find_student.jsp
-->

	<netui-template:setAttribute name="title" value="${bundle.web['dataexports.window.title']}" />
	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findStudent']}" />
	<netui-template:section name="bodySection">

		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->
		<h1><netui:content value="${pageFlow.pageTitle}" /></h1>


		<!-- title -->

		<p><netui:content value="${bundle.web['dataexport.testSessionStudentForExport.title.message']}" /><br />

		</p>

		<!-- start form -->
		<netui:form action="getStudentForExport">
			<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement" />
			<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction" />
			<netui-data:getData resultId="testSessionlistSize" value="${requestScope.testSessionlistSize}" /> 
			
			<br />
			<c:if test="${testSessionList != null}">
				<p>
				 <netui:hidden dataSource="actionForm.testSessionStudentMaxPage" /> 
				<table class="sortable">
					<netui-data:repeater dataSource="requestScope.testSessionList">
						<netui-data:repeaterHeader>

							<tr class="sortable">

								<ctb:tableSortColumnGroup columnDataSource="actionForm.testSessionStudentSortColumn"
									orderByDataSource="actionForm.testSessionStudentSortOrderBy" anchorName="studentSearchResult">
									<th class="sortable alignLeft" width="22%" nowrap><ctb:tableSortColumn value="TestSessionName">Test Session Name</ctb:tableSortColumn></th>
									<th class="sortable alignLeft" width="15%" nowrap><ctb:tableSortColumn value="StartDate">Start Date</ctb:tableSortColumn></th>
									<th class="sortable alignLeft" width="15%" nowrap><ctb:tableSortColumn value="EndDate">End Date</ctb:tableSortColumn></th>
									<th class="sortable alignLeft" width="8%" nowrap>&nbsp;To Be Exported&nbsp;</th>
									<th class="sortable alignLeft" width="8%" nowrap>&nbsp;Complete&nbsp;</th>
									<th class="sortable alignLeft" width="8%" nowrap>&nbsp;Scheduled&nbsp;</th>
									<th class="sortable alignLeft" width="8%" nowrap>&nbsp;Student Stop&nbsp;</th>
									<th class="sortable alignLeft" width="8%" nowrap>&nbsp;System Stop&nbsp;</th>
									<th class="sortable alignLeft" width="8%" nowrap>&nbsp;Not Taken&nbsp;</th>
									<th class="sortable alignLeft" width="8%" nowrap>&nbsp;Incomplete&nbsp;</th>
									

								</ctb:tableSortColumnGroup>
							</tr>

						</netui-data:repeaterHeader>
						<netui-data:repeaterItem>

							<tr class="sortable">

								<td class="sortable"><netui:content value="${container.item.testSessionName}" /></td>
								<td class="sortable"><netui:content value="${container.item.startDate}" /></td>
                                <td class="sortable"><netui:span value="${container.item.endDate}" /></td>
                                <td class="sortable"><netui:span value="${container.item.toBeExported}" /></td>
                                <td class="sortable"><netui:span value="${container.item.complete}" /></td>
								<td class="sortable"><netui:span value="${container.item.scheduled}" /></td>
								<td class="sortable"><netui:span value="${container.item.studentStop}" /></td>
								<td class="sortable"><netui:span value="${container.item.systemStop}" /></td>
								<td class="sortable"><netui:span value="${container.item.notTaken}" /></td>
								<td class="sortable"><netui:span value="${container.item.incomplete}" /></td>
							</tr>

						</netui-data:repeaterItem>
						<netui-data:repeaterFooter>

							<tr class="sortable">
								<td class="sortableControls" colspan="10"><ctb:tablePager dataSource="actionForm.testSessionStudentPageRequested"
									summary="request.testSessionPagerSummary" objectLabel="${bundle.oas['object.testSessions']}" 
									id="studentSearchResult" anchorName="studentSearchResult" /></td>
							</tr>

						</netui-data:repeaterFooter>

					</netui-data:repeater>

				</table>
				</br>
				</br>
				<c:if test="${testSessionlistSize > 0}">
				<netui:button type="submit" value="${bundle.web['common.button.next']}" action="validateAndConfirm" />
				</c:if>
				<c:if test="${testSessionlistSize <= 0}">
				<netui:button type="submit" value="${bundle.web['common.button.next']}" action="validateAndConfirm"  disabled="true"/>
				</c:if>
				</p>
			</c:if>
			</br>
			</br>
			<c:if test="${searchResultEmpty != null}">

				<ctb:message title="Data Export : Test Session " style="informationMessage">
					<netui:content value="${requestScope.searchResultEmpty}" />
				</ctb:message>
			</c:if>

			<c:if test="${searchResultEmpty == null}">

			</c:if>


		</netui:form>

		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
	</netui-template:section>
</netui-template:template>
