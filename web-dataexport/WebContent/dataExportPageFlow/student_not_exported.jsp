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
	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.dataExport']}" />
	<netui-template:section name="bodySection">

		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->
		<h1><netui:content value="${pageFlow.pageTitle}" /></h1>


		<!-- title -->

		<p><netui:content
			value="${bundle.web['dataexport.studentNotExported.title.message']}" /><br />

		</p>

		<!-- start form -->
		<netui:form action="getStudentNotToBeExported">
			<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement" />
			<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction" />
			<br />
			<c:if test="${studentList != null}">

				<p>
				<%
					    Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); //Start Change For CR - GA2011CR001
					%> <netui:hidden dataSource="actionForm.studentMaxPage" /> <!--  studentList table -->
				<table class="sortable">
					<netui-data:repeater dataSource="requestScope.studentList">
						<netui-data:repeaterHeader>

							<tr class="sortable">

								<ctb:tableSortColumnGroup columnDataSource="actionForm.studentSortColumn"
									orderByDataSource="actionForm.studentSortOrderBy" anchorName="studentSearchResult">
									<th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="LoginId">Login ID</ctb:tableSortColumn></th>
									<th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="StudentName">Student Name</ctb:tableSortColumn></th>
									<th class="sortable alignLeft" width="25%" nowrap><ctb:tableSortColumn value="TestSessionName">Test Session Name</ctb:tableSortColumn></th>
									<th class="sortable alignLeft" width="5%" nowrap><ctb:tableSortColumn value="Grade">Grade</ctb:tableSortColumn></th>
									<c:if test="${isStudentIdConfigurable}">
										<th class="sortable alignLeft" width="10%" nowrap>${studentIdArrValue[0]}</th>
									</c:if>
									<c:if test="${!isStudentIdConfigurable}">
										<th class="sortable alignLeft" width="10%" nowrap>Student ID</th>
									</c:if>
									<th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="ExportStatus">To Be Exported</ctb:tableSortColumn></th>
									<th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="TestCompletionStatus">Test Completion Status</ctb:tableSortColumn></th>
								</ctb:tableSortColumnGroup>
							</tr>

						</netui-data:repeaterHeader>
						<netui-data:repeaterItem>

							<tr class="sortable">
								<td class="sortable"><netui:content value="${container.item.loginId}" /></td>
								<td class="sortable"><netui:content value="${container.item.studentName}" /></td>
								<td class="sortable"><netui:span value="${container.item.testSessionName}" /></td>

								<td class="sortable"><netui:span value="${container.item.grade}" /></td>
								
								<td class="sortable"><netui:span value="${container.item.studentIdNumber}" /></td>
								<td class="sortable"><netui:span value="${container.item.exportStatus}" /></td>
								<td class="sortable"><netui:span value="${container.item.testCompletionStatus}" /></td>

							</tr>

						</netui-data:repeaterItem>
						<netui-data:repeaterFooter>

							<tr class="sortable">
								<td class="sortableControls" colspan="7"><ctb:tablePager dataSource="actionForm.studentPageRequested"
									summary="request.studentPagerSummary" objectLabel="${bundle.oas['object.students']}" foundLabel="Found"
									id="studentSearchResult" anchorName="studentSearchResult" /></td>
							</tr>

						</netui-data:repeaterFooter>

					</netui-data:repeater>

				</table>
			</c:if>
			<c:if test="${searchResultEmpty != null}">

				<ctb:message title="Search Result" style="informationMessage">
					<netui:content value="${requestScope.searchResultEmpty}" />
				</ctb:message>
			</c:if>
			</br>
			</br>
			<netui:button type="submit" value="${bundle.web['common.button.back']}" action="getStudentForExport" />
			<netui:button type="submit" value="${bundle.web['common.button.next']}" action="validateAndConfirm" />
			</p>
		</netui:form>

		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
	</netui-template:section>
</netui-template:template>
