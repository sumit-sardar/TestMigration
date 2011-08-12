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
template_find_job.jsp
-->

	<netui-template:setAttribute name="title" value="${bundle.web['dataexports.window.title']}" />
	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.dataExport']}" />
	<netui-template:section name="bodySection">

		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->
		<h1><netui:content value="${pageFlow.pageTitle}" /></h1>

		<!-- start form -->
		<netui:form action="getExportStatus">
			<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement" />
			<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction" />
			<br />
			<c:if test="${jobList != null}">
				<p>
				<netui:hidden dataSource="actionForm.jobMaxPage" /> <!--  jobList table -->
				<table class="sortable">
					<netui-data:repeater dataSource="requestScope.jobList">
						<netui-data:repeaterHeader>

							<tr class="sortable">
								<ctb:tableSortColumnGroup columnDataSource="actionForm.jobSortColumn"
									orderByDataSource="actionForm.jobSortOrderBy" anchorName="jobSearchResult">
									<th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="JobId">Job ID</ctb:tableSortColumn></th>
									<th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="CreatedDateTime">Job Submission Date</ctb:tableSortColumn></th>
									<th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="StudentCount">Student Count</ctb:tableSortColumn></th>
									<th class="sortable alignLeft" width="5%" nowrap><ctb:tableSortColumn value="JobStatus">Job Status</ctb:tableSortColumn></th>					</ctb:tableSortColumnGroup>
							</tr>

						</netui-data:repeaterHeader>
						<netui-data:repeaterItem>								
							<tr class="sortable">
								<td class="sortable"><netui:content value="${container.item.jobId}" /></td>
								<td class="sortable"><netui:content value="${container.item.createdDateTime}" /></td>
								<td class="sortable"><netui:span value="${container.item.studentCount}" /></td>
								<td class="sortable"><netui:span value="${container.item.jobStatus}" /></td>
							</tr>
						</netui-data:repeaterItem>
						<netui-data:repeaterFooter>

							<tr class="sortable">
								<td class="sortableControls" colspan="7"><ctb:tablePager dataSource="actionForm.jobPageRequested"
									summary="request.jobPagerSummary" objectLabel="${bundle.oas['object.jobs']}" foundLabel="Found"
									id="jobSearchResult" anchorName="jobSearchResult" /></td>
							</tr>

						</netui-data:repeaterFooter>

					</netui-data:repeater>

				</table>
				</br>
				</br>
				<netui:button type="submit" value="${bundle.web['common.button.next']}" action="getjobNotToBeExported" />
				</p>
			</c:if>
			</br>
			</br>
			<c:if test="${searchResultEmpty != null}">

				<ctb:message title="Data Export : Search Result" style="informationMessage">
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
