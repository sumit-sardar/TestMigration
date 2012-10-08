<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>


<%

Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); 
String []studentIdArrValue = (String[])request.getAttribute("studentIdArrValue");
Integer selectedRosterIds = (Integer)request.getAttribute("selectedRosterIds"); 

%>
<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['generateReportFile.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.reports']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
             
<h1><netui:content value="${bundle.web['generateReportFile.title']}"/>: <netui:span value="${requestScope.testSession.testAdminName}"/></h1>
<p><netui:content value="${bundle.web['generateReportFile.title.message']}"/></p>



<h2><netui:content value="${bundle.web['viewMonitorStatus.testDetails.title']}"/></h2>

<netui:form action="generate_report_file">

<table class="transparent">

<tr class="transparent">
	<td class="transparent" width="45%">
		<table class="transparent">

			<tr class="transparent">
			    <td class="transparent">
			        <netui:content value="${bundle.web['viewMonitorStatus.testDetails.testAccessCode']}"/>: 
			    </td>
			    <td class="transparent">
			        <div class="formValue"><netui:span value="${requestScope.testSession.accessCode}" styleClass="formValue"/></div>
			    </td>
			</tr>

			<tr class="transparent">
			    <td class="transparent">
			        <netui:content value="${bundle.web['viewMonitorStatus.testDetails.totalStudents']}"/>: 
			    </td>
			    <td class="transparent">    
			        <div class="formValue"><netui:span value="${requestScope.totalStudents}" styleClass="formValue"/></div>
			    </td>
			</tr>

			<tr class="transparent">
			    <td class="transparent">
			        <netui:content value="Total Selected Students"/>: 
			    </td>
			    <td class="transparent">    
			        <div class="formValue"><span class="formValue" id="selectedRosterIds"><%= selectedRosterIds %></span></div>
			    </td>
			</tr>
		</table>
	</td>

	<td class="transparent" width="10%">&nbsp;</td>

	<td class="transparent" width="45%">
		<table class="transparent">
		<tr class="transparent">
			<td class="transparent">
				*Report File Name:
			</td>
			<td class="transparent">
                <netui:textBox tagId="fileName" dataSource="pageFlow.fileName"  style="width:200px;" />
			</td>
		</tr>

		<tr class="transparent">
			<td class="transparent">
				*Report File Type:
			</td>
			<td class="transparent">
        <netui:select dataSource="pageFlow.fileType" optionsDataSource="${pageFlow.fileTypeOptions}" style="width:200px;" />
 			</td>
		</tr>

		<tr class="transparent">
			<td class="transparent">
				Email:
			</td>
			<td class="transparent">
                <netui:textBox tagId="email" dataSource="pageFlow.userEmail" style="width:200px;" />
			</td>
		</tr>
		</table>
	</td>
</tr>
</table><br/>

 

    <netui:hidden dataSource="actionForm.actionElement"/>
    <netui:hidden dataSource="actionForm.currentAction"/>
    <netui:hidden dataSource="actionForm.maxPage"/>
    
<h2><netui:content value="${bundle.web['viewMonitorStatus.testRoster.title']}"/></h2>
    <netui-data:getData resultId="isSubtestValidationAllowed" value="${pageFlow.subtestValidationAllowed}"/>
    <netui-data:getData resultId="showStudentReportButton" value="${pageFlow.showStudentReportButton}"/>
	<netui-data:getData resultId="formIsClean" value="${requestScope.formIsClean}"/>

<c:if test="${(formIsClean != null) && (! formIsClean)}">
    <p><ctb:message title="${bundle.web['common.message.form.invalidCharacters']}" style="alertMessage"></ctb:message></p>
</c:if>

<a name="tableAnchor"><!-- tableAnchor --></a>    

<table class="sortable">
    <tr class="sortable">
        <td class="sortableControls" colspan="7">
            <table class="tableFilter">
            <tr class="tableFilter">
                <td class="tableFilter">
                    <netui:button styleClass="button" tagId="selectAll" value="Select All" type="submit" onClick="setElementValue('{actionForm.currentAction}', 'selectAll');" disabled="${requestScope.disableSelectAllButton}" />&nbsp;                                 
                    <netui:button styleClass="button" tagId="deselectAll" value="Deselect All" type="submit" onClick="setElementValue('{actionForm.currentAction}', 'deselectAll');" disabled="${requestScope.disableDeselectAllButton}" />&nbsp;                                 
                </td>
            </tr>
            </table>
        </td>
    </tr>

<netui-data:repeater dataSource="requestScope.rosterList">
    <netui-data:repeaterHeader>    
        <tr class="sortable">
            <ctb:tableSortColumnGroup columnDataSource="actionForm.sortColumn" 
            						  orderByDataSource="actionForm.sortOrderBy" 
            						  anchorName="tableAnchor" >
                <th class="sortable alignCenter" style="width:50px;" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="LastName"><netui:content value="${bundle.web['common.column.lastName']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="FirstName"><netui:content value="${bundle.web['common.column.firstName']}"/></ctb:tableSortColumn></th>
                <c:if test="${isStudentIdConfigurable}">   
                	<th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="ExtPin1"><netui:content value="${studentIdArrValue[0]}"/></ctb:tableSortColumn></th>
	            </c:if>
	            <c:if test="${!isStudentIdConfigurable}">   
	            	<th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="ExtPin1"><netui:content value="${bundle.web['common.column.studentId']}"/></ctb:tableSortColumn></th>
	            </c:if>
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="UserName"><netui:content value="${bundle.web['common.column.loginName']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="Password"><netui:content value="${bundle.web['common.column.password']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="TestCompletionStatus"><netui:content value="${bundle.web['common.column.testStatus']}"/></ctb:tableSortColumn></th>
            </ctb:tableSortColumnGroup>
        </tr>
    </netui-data:repeaterHeader>
    <netui-data:getData resultId="currentRosterId" value="${actionForm.testRosterId}"/>
    <netui-data:repeaterItem>
        <tr class="sortable">
            <td class="sortable alignCenter">
                <netui-data:getData resultId="rosterId" value="${container.item.testRosterId}"/>
                
            <netui:checkBoxGroup dataSource="actionForm.selectedTestRosterIds" >
               	&nbsp;<netui:checkBoxOption value="${container.item.testRosterId}" onclick="getRosterId(this);" >&nbsp;</netui:checkBoxOption>
            </netui:checkBoxGroup>
            </td>
            <td class="sortable alignLeft"><netui:span value="${container.item.lastName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.firstName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.studentNumber}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.loginName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.password}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter">
                <netui:span value="${container.item.testStatus}" defaultValue="&nbsp;"/>
            </td>
        </tr>
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
        <tr class="sortable">
            <td class="sortableControls" colspan="9">
                <ctb:tablePager dataSource="actionForm.pageRequested" 
                				summary="request.pagerSummary" 
                				objectLabel="${bundle.oas['object.students']}" 
                				anchorName="tableAnchor"/>
            </td>
        </tr>             
    </netui-data:repeaterFooter>
</netui-data:repeater>

    <ctb:tableNoResults dataSource="request.rosterList">
        <tr class="sortable">
            <td class="sortable" colspan="9">
                <ctb:message title="${bundle.web['common.message.noStudent.title']}" style="tableMessage">
                    <netui:content value="${bundle.web['common.message.noStudent.message']}"/>
                </ctb:message>
            </td>
        </tr>
    </ctb:tableNoResults>

</table>
    
<p>
<br>
<netui:button type="submit" value="${bundle.web['common.button.home']}" action="redirect_to_homepage"/>&nbsp;           
<netui:button styleClass="button" tagId="generateReportFile" value="View Report" type="button" onClick="return validateInfo();" disabled="${requestScope.disableGenerateReportFileButton}" />&nbsp;                                 
</p>

</netui:form>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
        
    </netui-template:section>
</netui-template:template>
