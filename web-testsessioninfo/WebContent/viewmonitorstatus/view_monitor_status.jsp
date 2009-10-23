<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-compat-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['viewMonitorStatus.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.viewStatus']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
             
<h1><netui:content value="${bundle.web['viewMonitorStatus.title']}"/>: <netui:span value="${requestScope.testSession.testAdminName}"/></h1>
<p><netui:content value="${bundle.web['viewMonitorStatus.title.message']}"/></p>


<!-- ********************************************************************************************************************* -->
<!-- Session Information -->
<!-- ********************************************************************************************************************* -->
<p align="right">
<netui:anchor formSubmit="true" action="goto_session_information"><netui:content value="${bundle.web['viewMonitorStatus.link.sessionInformation']}"/></netui:anchor>
</p>

<h2><netui:content value="${bundle.web['viewMonitorStatus.testDetails.title']}"/></h2>
<table class="transparent">
<tr class="transparent">
    <td class="transparent">
        <netui:content value="${bundle.web['viewMonitorStatus.testDetails.totalStudents']}"/>: 
    </td>
    <td class="transparent">    
        <div class="formValue"><netui:span value="${requestScope.totalStudents}" styleClass="formValue"/></div>
    </td>
</tr>
<ctb:switch dataSource="${request.hasBreak}">
<ctb:case value="false">
<tr class="transparent">
    <td class="transparent">
        <netui:content value="${bundle.web['viewMonitorStatus.testDetails.testAccessCode']}"/>: 
    </td>
    <td class="transparent">
        <div class="formValue"><netui:span value="${requestScope.testSession.accessCode}" styleClass="formValue"/></div>
    </td>
</tr>
</ctb:case>
</ctb:switch>  
<!--RD Changes-->
<netui-data:getData resultId="randomDistractorAllow" value="${requestScope.testSession.randomDistractorStatus}"/>
<c:if test="${randomDistractorAllow != null}">  
    <tr class="transparent">
            <td class="transparent"  width="250" nowrap>
                <netui:content value="${bundle.web['selecttest.rdOptions.message1']}"/>
            </td>
            <td class="transparent"><div class="formValue"><netui:span value="${requestScope.testSession.randomDistractorStatus}" styleClass="formValue"/></div></td> 
           
</c:if>      
</table><br/>

<ctb:switch dataSource="${request.hasBreak}">
<ctb:case value="multiAccesscodes">
<table class="sortable">
<netui-data:repeater dataSource="requestScope.subtestList">
    <netui-data:repeaterHeader>    
        <tr class="sortable">
            <th class="sortable alignCenter" height="24"><netui:content value="Sequence"/></th>
            <th class="sortable alignLeft"><netui:content value="Subtest Name"/></th>
            <th class="sortable alignLeft"><netui:content value="Duration"/></th>
            <th class="sortable alignLeft"><netui:content value="Test Access Code"/></th>
        </tr>    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
        <tr class="sortable">
            <td class="sortable alignCenter"><netui:span value="${container.item.sequence}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.subtestName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.duration}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.accessCode}" defaultValue="&nbsp;"/></td>
        </tr>
    </netui-data:repeaterItem>
</netui-data:repeater>
</table>
</ctb:case>
<ctb:case value="singleAccesscode">
<table class="sortable">
<netui-data:repeater dataSource="requestScope.subtestList">
    <netui-data:repeaterHeader>    
        <tr class="sortable">
            <th class="sortable alignCenter" height="24"><netui:content value="Sequence"/></th>
            <th class="sortable alignLeft"><netui:content value="Subtest Name"/></th>
            <th class="sortable alignLeft"><netui:content value="Duration"/></th>
        </tr>    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
        <tr class="sortable">
            <td class="sortable alignCenter"><netui:span value="${container.item.sequence}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.subtestName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.duration}" defaultValue="&nbsp;"/></td>
        </tr>
    </netui-data:repeaterItem>
</netui-data:repeater>
</table>
</ctb:case>
</ctb:switch>                


<netui:form action="view_monitor_status">
    <netui:hidden dataSource="actionForm.actionElement"/>
    <netui:hidden dataSource="actionForm.currentAction"/>
    <netui:hidden dataSource="actionForm.maxPage"/>
    <netui:hidden dataSource="actionForm.filterVisible"/>
    
<h2><netui:content value="${bundle.web['viewMonitorStatus.testRoster.title']}"/></h2>
    <netui-data:getData resultId="isShowToggleForCustomerFlag" value="${pageFlow.setCustomerFlagToogleButton}"/>
    <netui-data:getData resultId="isSubtestValidationAllowed" value="${pageFlow.subtestValidationAllowed}"/>
    <netui-data:getData resultId="showStudentReportButton" value="${pageFlow.showStudentReportButton}"/>

<netui-compat-template:visible visibility="{request.formIsClean}" negate="true">
    <p><ctb:message title="${bundle.web['common.message.form.invalidCharacters']}" style="alertMessage"></ctb:message></p>
</netui-compat-template:visible>

<a name="tableAnchor"><!-- tableAnchor --></a>    

<table class="sortable">
    <tr class="sortable">
        <td class="sortableControls" colspan="9">
            <ctb:tableFilter dataSource="${actionForm.filterVisible}">
                <table class="tableFilter">
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft" colspan="6"><netui:content value="${bundle.web['viewMonitorStatus.filter.title']}"/></td>
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.lastName']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testRosterFilter.lastNameFilterType" size="1" defaultValue="${actionForm.testRosterFilter.lastNameFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testRosterFilter.lastName"/></td>                    
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.password']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testRosterFilter.passwordFilterType" size="1" defaultValue="${actionForm.testRosterFilter.passwordFilterType}" style="width:120px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testRosterFilter.password"/></td>                    
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.firstName']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testRosterFilter.firstNameFilterType" size="1" defaultValue="${actionForm.testRosterFilter.firstNameFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testRosterFilter.firstName"/></td>                    
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.validationStatus']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.validationStatusOptions}" dataSource="actionForm.testRosterFilter.validationStatusFilterType" size="1" defaultValue="${actionForm.testRosterFilter.validationStatusFilterType}" style="width:120px"/></td>
                    <td class="tableFilter alignLeft">&nbsp;</td>                    
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.loginName']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testRosterFilter.loginNameFilterType" size="1" defaultValue="${actionForm.testRosterFilter.loginNameFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testRosterFilter.loginName"/></td>                    
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.testStatus']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.testStatusOptions}" dataSource="actionForm.testRosterFilter.testStatusFilterType" size="1" defaultValue="${actionForm.testRosterFilter.testStatusFilterType}" style="width:120px"/></td>
                    <td class="tableFilter alignLeft">&nbsp;</td>                    
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignRight" colspan="6">
                        <netui:button styleClass="button" value="${bundle.widgets['button.apply']}" type="submit" onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'applyFilters', 'tableAnchor');"/>&nbsp;
                        <netui:button styleClass="button" value="${bundle.widgets['button.clearAll']}" type="submit" onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'clearFilters', 'tableAnchor');"/>&nbsp;
                    </td>                        
                </tr>
                </table>
                <hr class="sortableControls"/>
            </ctb:tableFilter>
            <table class="tableFilter">
            <tr class="tableFilter">
                <td class="tableFilter">
                    <ctb:tableFilterToggle dataSource="${actionForm.filterVisible}" />&nbsp;
                <c:if test="${isSubtestValidationAllowed}">                     
                    <netui:button styleClass="button" tagId="viewDetails" value="${bundle.web['common.button.viewDetails']}" type="submit" onClick="setElementValue('{actionForm.currentAction}', 'validationDetails');" disabled="${requestScope.disableToogleButton}"/>&nbsp;                                 
                </c:if>
                <c:if test="${! isSubtestValidationAllowed}">                     
                    <netui:button styleClass="button" tagId="viewDetails" value="${bundle.web['common.button.viewDetails']}" type="submit" onClick="setElementValue('{actionForm.currentAction}', 'viewDetails');" disabled="${requestScope.disableToogleButton}"/>&nbsp;                                 
                    <netui:button styleClass="button" tagId="toggleValidationStatus" value="${bundle.web['common.button.toggleValidationStatus']}" type="submit" onClick="setElementValue('{actionForm.currentAction}', 'toggleValidationStatus');" disabled="${requestScope.disableToogleButton}"/>&nbsp;                                 
                    <c:if test="${isShowToggleForCustomerFlag == 'true'}"> 
                        <netui:button styleClass="button" tagId="toggleIrregularStatus" value="${bundle.web['common.button.toggleIrregularStatus']}" type="submit" onClick="setElementValue('{actionForm.currentAction}', 'toggleCustomerFlagStatus');" disabled="${requestScope.disableToogleButton}"/>&nbsp;                                 
                    </c:if>
                </c:if>
                <c:if test="${showStudentReportButton}">
                    <netui:button styleClass="button" tagId="studentReport" value="${bundle.web['common.button.studentReport']}" type="submit" onClick="setElementValue('{actionForm.currentAction}', 'viewIndividualReport');" disabled="${requestScope.disableToogleButton}"/>&nbsp;                                 
                </c:if>
                <netui:button styleClass="button" tagId="refreshList" value="${bundle.web['common.button.refreshList']}" type="submit" onClick="setElementValue('{actionForm.currentAction}', 'refreshList');" disabled="${requestScope.disableRefreshButton}"/>&nbsp;                                 
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
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="ExtPin1"><netui:content value="${bundle.web['common.column.studentId']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="UserName"><netui:content value="${bundle.web['common.column.loginName']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="Password"><netui:content value="${bundle.web['common.column.password']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="ValidationStatus"><netui:content value="${bundle.web['common.column.validationStatus']}"/></ctb:tableSortColumn></th>
                <c:if test="${isShowToggleForCustomerFlag == 'true'}">   
                    <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="CustomerFlagStatus"><netui:content value="${bundle.web['common.column.irregularStatus']}"/></ctb:tableSortColumn></th>
                </c:if>
                <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="TestCompletionStatus"><netui:content value="${bundle.web['common.column.testStatus']}"/></ctb:tableSortColumn></th>
            </ctb:tableSortColumnGroup>
        </tr>
    </netui-data:repeaterHeader>
    <netui-data:getData resultId="currentRosterId" value="${actionForm.testRosterId}"/>
    <netui-data:repeaterItem>
        <tr class="sortable">
            <td class="sortable alignCenter">
                <netui-data:getData resultId="testStatus" value="${container.item.testStatus}"/>
                <netui-data:getData resultId="rosterId" value="${container.item.testRosterId}"/>
                <c:if test="${testStatus == 'Scheduled' || testStatus == 'Not taken'}">
                        <netui:radioButtonGroup dataSource="actionForm.testRosterId">
                        &nbsp;<netui:radioButtonOption value="${container.item.testRosterId}" onClick="enableElementById(getNetuiTagName('toggleValidationStatus')); enableElementById(getNetuiTagName('toggleIrregularStatus')); enableElementById(getNetuiTagName('viewDetails')); disableElementById(getNetuiTagName('studentReport'));">&nbsp;</netui:radioButtonOption>
                        </netui:radioButtonGroup>
                        <c:if test="${rosterId == currentRosterId}">
                            <script type="text/javascript">
                                disableElementById(getNetuiTagName('studentReport'));
                            </script>
                        </c:if>
               </c:if>
               <c:if test="${testStatus != 'Scheduled' && testStatus != 'Not taken'}">
                        <netui:radioButtonGroup dataSource="actionForm.testRosterId">
                        &nbsp;<netui:radioButtonOption value="${container.item.testRosterId}" onClick="enableElementById(getNetuiTagName('toggleValidationStatus')); enableElementById(getNetuiTagName('toggleIrregularStatus')); enableElementById(getNetuiTagName('viewDetails')); enableElementById(getNetuiTagName('studentReport'));">&nbsp;</netui:radioButtonOption>
                        </netui:radioButtonGroup>
               </c:if>
            </td>
            <td class="sortable alignLeft"><netui:span value="${container.item.lastName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.firstName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.studentNumber}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.loginName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.password}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter">    
                <ctb:switch dataSource="${container.item.validationStatus}">
                    <ctb:case value="Valid"><netui:span value="${container.item.validationStatus}" defaultValue="&nbsp;"/></ctb:case>
                    <ctb:case value="Invalid"><font color="red"><netui:span value="${container.item.validationStatus}" defaultValue="&nbsp;"/></font></ctb:case>
                    <ctb:case value="Partially Invalid"><font color="red"><netui:span value="${container.item.validationStatus}" defaultValue="&nbsp;"/></font></ctb:case>
                </ctb:switch>                
            </td>  
            <c:if test="${isShowToggleForCustomerFlag == 'true'}">           
                <td class="sortable alignCenter">
                    <netui:span value="${container.item.customerFlagStatus}"/>                   
                </td>
             </c:if>  
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
<br><netui:button type="submit" value="${bundle.web['common.button.home']}" action="redirect_to_homepage"/>           
</p>

</netui:form>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
        
    </netui-template:section>
</netui-template:template>
