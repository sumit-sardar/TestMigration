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

		<p><netui:content value="${bundle.web['dataexport.exportSummary.title.message']}" /><br />
		</p>

		<!-- start form -->
		<netui:form action="gotoSummary">
			<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement" />
			<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction" />
			<netui-data:getData resultId="jobId" value="${requestScope.jobId}" /> 
 								
			<br />
				<h2><netui:content value="${bundle.web['dataexport.window.subHeading']}" /></h2>
					<table class="transparent" width="100%">
						<tr class="transparent">
							<td class="transparent">Total Students Being Exported:</td>
							<td class="transparent">
							<netui:span value="${pageFlow.totalStudentCount}" styleClass="formValueLarge" />
							</td>
						</tr>
						<tr class="transparent">
							<td class="transparent">Total Students Scheduled:</td>
							<td class="transparent">
							<netui:span value="${pageFlow.scheduledStudentCount}" styleClass="formValueLarge" />
							</td>
						</tr>
						<tr class="transparent">
							<td class="transparent">Total Students Not Taken:</td>
							<td class="transparent">
							<netui:span value="${pageFlow.notTakenStudentCount}" styleClass="formValueLarge" />
							</td>
						</tr>
						<tr class="transparent">
							<td class="transparent">Total Students Not Completed:</td>
							<td class="transparent">
							<netui:span value="${pageFlow.notCompletedStudentCount}" styleClass="formValueLarge" />
							</td>
						</tr>		
					<c:if test="${jobId != null}">
						<tr class="transparent">
							<td class="transparent">Job Id:</td>
							<td class="transparent">
							<netui:span value= "${jobId}" styleClass="formValueLarge" />
							</td>
						</tr>					
					 </c:if>
					</table>
			</br>
			</br>
			<c:if test="${jobId == null}">
			<netui:button type="submit" value="${bundle.web['common.button.back']}" action="backToPreviousPage"/>
			<c:if test="${pageFlow.totalStudentCount > 0 }">
			<netui:button type="submit" value="Submit" action="submitJob"/>
			</c:if>
			<c:if test="${pageFlow.totalStudentCount <= 0 }">
			<netui:button type="submit" value="Submit" action="submitJob" disabled="true"/>
			</c:if>
			<netui:button type="submit" value="${bundle.web['common.button.cancel']}" action="goto_home"/></p>
			</c:if>
			<c:if test="${submitJobResult != null}">
				<ctb:message title="Export Job Successfully Submited." style="informationMessage">
					<netui:content value="${requestScope.submitJobResult}" />
				</ctb:message>
			</c:if>
			<br>
			<c:if test="${jobId != null}">
			<netui:button type="submit" value="View Status" action="getExportStatus"/>
			</c:if>
		</netui:form>
   			

		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
	</netui-template:section>
</netui-template:template>
