<%@ page language="java" contentType="text/html;charset=UTF-8" import="com.ctb.bean.testAdmin.StudentReportIrsScore"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/immediateReport_template.jsp">
<!-- 
template_find_student.jsp
-->

<netui-template:setAttribute name="title" value="${bundle.web['findstudentscoring.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.handScoring']}"/>
<netui-template:section name="bodySection">

<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>


<netui:form action="beginDisplayStudScoringReport">

	<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement" />
	<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction" />
	<netui:hidden tagId="studentNameRe" dataSource="actionForm.studentNameRe" />
	<netui:hidden tagId="studentExtPin1" dataSource="actionForm.studentExtPin1" />
	<netui:hidden tagId="testAdminStartDate" dataSource="actionForm.testAdminStartDate" />
	<netui:hidden tagId="form" dataSource="actionForm.form" />
	<netui:hidden tagId="district" dataSource="actionForm.district" />
	<netui:hidden tagId="school" dataSource="actionForm.school" />
	<netui:hidden tagId="grade" dataSource="actionForm.grade" />

	<table class="transparent" width="100%">

				<tr class="transparent">
					<td class="transparent"><netui:content
						value="${bundle.web['individualStudentScoring.StudentDetails.StudentName']}" /></td>
					<td class="transparent">
					<div class="formValueLarge"><netui:span value="${requestScope.studentName}" styleClass="formValueLarge" /></div>
					</td>
				</tr>


				<tr class="transparent">
					<td class="transparent"><netui:content
						value="${bundle.web['individualStudentScoring.StudentDetails.Id']}" /></td>
					<td class="transparent">
					<div class="formValueLarge"><netui:span value="${requestScope.studentExtPin}" styleClass="formValueLarge" /></div>
					</td>
				</tr>
				<tr class="transparent">
					<td class="transparent"><netui:content
						value="${bundle.web['individualStudentScoring.StudentDetails.TestDate']}" /></td>
					<td class="transparent">
					<div class="formValueLarge"><netui:span value="${requestScope.startDate}" styleClass="formValueLarge" /></div>

					</td>
				</tr>
				<tr class="transparent">
					<td class="transparent"><netui:content
						value="${bundle.web['individualStudentScoring.StudentDetails.Form']}" /></td>
					<td class="transparent">
					<div class="formValueLarge"><netui:span value="${requestScope.formRe}" styleClass="formValueLarge" /></div>

					</td>
				</tr>
				<tr class="transparent">
					<td class="transparent"><netui:content
						value="${bundle.web['individualStudentScoring.StudentDetails.District']}" /></td>
					<td class="transparent">
					<div class="formValueLarge"><netui:span value="${requestScope.district}" styleClass="formValueLarge" /></div>

					</td>
				</tr>
				<tr class="transparent">
					<td class="transparent"><netui:content
						value="${bundle.web['individualStudentScoring.StudentDetails.School']}" /></td>
					<td class="transparent">
					<div class="formValueLarge"><netui:span value="${requestScope.school}" styleClass="formValueLarge" /></div>

					</td>
				</tr>
				<tr class="transparent">
					<td class="transparent"><netui:content
						value="${bundle.web['individualStudentScoring.StudentDetails.Grade']}" /></td>
					<td class="transparent">
					<div class="formValueLarge"><netui:span value="${requestScope.grade}" styleClass="formValueLarge" /></div>

					</td>
				</tr>
				
			</table>
			<br/>
			<table>
				<tr>
					<td width="50px">
					</td>
					<td>
						<table>
							<tr>
								<th width="150px" style="border-style: none !important;">&nbsp;</th>
            					<th class="sortable alignCenter" width="120px">Raw Score</th>
            					<th class="sortable alignCenter" width="120px">Scale Score </th>
            					<th class="sortable alignCenter" width="150px">Proficiency Level</th>
							</tr>
							<%
								StudentReportIrsScore[] stuScoreVal = (StudentReportIrsScore[])request.getAttribute("irsScores");
								if(stuScoreVal != null) {
									for(int i = 0; i < stuScoreVal.length; i++) {
										if(stuScoreVal[i] != null && stuScoreVal[i].getContentAreaName() != null) {
							%>
							<tr <%if(stuScoreVal[i].getContentAreaName().equalsIgnoreCase("Comprehension") ||
									stuScoreVal[i].getContentAreaName().equalsIgnoreCase("Oral") ||
									stuScoreVal[i].getContentAreaName().equalsIgnoreCase("Overall")) { %>
									bgcolor="#C3D599"
									<% }%>>
								<td align="left" class="transparent" style="border-style: solid !important; border-width: thin !important;"><%=stuScoreVal[i].getContentAreaName()%>
								</td>
								<td align="center" class="transparent" style="border-style: solid !important; border-width: thin !important;"><%=stuScoreVal[i].getRawScore()%>
								</td>
								<td align="center" class="transparent" style="border-style: solid !important; border-width: thin !important;"><%=stuScoreVal[i].getScaleScore()%>
								</td>
								<td align="center" class="transparent" style="border-style: solid !important; border-width: thin !important;"><%=stuScoreVal[i].getProficiencyLevel()%>
								</td>
							</tr>
							<%
									}
								}
							}
						%>
						</table>
					</td>
				</tr>
			</table>
			<br/>
			<netui:button styleClass="button" type="submit" value="${bundle.web['common.button.back']}" action="returnToFindStudent" />
			&nbsp;&nbsp;
			<netui:button styleClass="button" value="Generate PDF" />
			&nbsp;&nbsp;
			<netui:button styleClass="button" value="Generate CSV" />
</netui:form>


	</netui-template:section>
</netui-template:template>
