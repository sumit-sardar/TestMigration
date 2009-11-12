<%@ page import="java.io.*, java.util.*"%>
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

<netui-template:template templatePage="/resources/jsp/template.jsp">

<netui-template:setAttribute name="title" value="${bundle.web['manageProgram.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.programStatus']}"/>
<netui-template:section name="bodySection">

<% 
    Boolean noPrograms = (Boolean)request.getAttribute("noPrograms");
    Boolean hasClickableSubtest = (Boolean)request.getAttribute("hasClickableSubtest"); 
    Boolean noTest = (Boolean)request.getAttribute("noTest");
%> 

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<h1><netui:content value="${bundle.web['manageProgram.page.title']}"/></h1>

<c:if test="${noPrograms}">
<p>
    <netui:content value="${bundle.web['manageProgram.noProgram.message']}"/><br/>
</p>
</c:if>
<c:if test="${!noPrograms}">
<p>
    <netui:content value="${bundle.web['manageProgram.page.message']}"/><br/>
</p>

<!-- start form -->
<netui:form action="manageProgram">

<netui:hidden dataSource="actionForm.actionElement"/> 
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedProgramName"/>
<netui:hidden dataSource="actionForm.selectedTestName"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/>

<p>
<!-- select_test_organization.jsp -->
<% 
    Boolean multiplePrograms = (Boolean)request.getAttribute("multiplePrograms"); 
    Boolean multipleTests = (Boolean)request.getAttribute("multipleTests"); 
    Boolean singleTest = (Boolean)request.getAttribute("singleTest");
%> 
        

<table class="sortable">

    <tr class="sortable">
        <th class="sortable alignLeft" height="25" colspan="2">&nbsp;&nbsp;<span>Program Information</span></th>
    </tr>

    <tr class="normal" valign="top">
        <td class="normal" width="350" valign="top">
    
            <table class="transparent">
                <tr class="normal">
                    <td class="normal">Customer:</td>
                    <td class="normal"><netui:span value="${pageFlow.customerName}" defaultValue="&nbsp;"/></td>
                </tr>
                <tr class="normal">
                    <td class="normal">Program:</td>
                    <td class="normal">
                        <c:if test="${!multiplePrograms}">
                            <netui:span value="${pageFlow.programName}" defaultValue="&nbsp;"/>
                            <netui:hidden dataSource="actionForm.selectedProgramId"/>
                        </c:if>
                        <c:if test="${multiplePrograms}">
                        <netui:select optionsDataSource="${pageFlow.programList}" dataSource="actionForm.selectedProgramId" size="1" onChange="setElementValueAndSubmit('{actionForm.currentAction}', 'onProgramChange');"/>
                        </c:if>
                    </td>
                </tr>
                <tr class="normal">
                    <td class="normal">Organization:</td>
                    <td class="normal"><netui:span value="${actionForm.selectedOrgNodeName}" defaultValue="&nbsp;"/></td>
                </tr>
                <tr class="normal">
                    <td class="normal">Test:</td>
                    <td class="normal">
                        <c:if test="${noTest}">
                            <netui:span value="${bundle.web['manageProgram.noTest.label']}" defaultValue="&nbsp;"/>
                        </c:if>
                        <c:if test="${singleTest}">
                            <netui:span value="${pageFlow.testName}" defaultValue="&nbsp;"/>
                            <netui:hidden dataSource="actionForm.selectedTestId"/>
                        </c:if>
                        <c:if test="${multipleTests}">
                        <netui:select optionsDataSource="${pageFlow.testList}" dataSource="actionForm.selectedTestId" size="1" onChange="setElementValueAndSubmit('{actionForm.currentAction}', 'onTestChange');"/>
                        </c:if>
                    </td>
                </tr>    
            </table>

        </td>

        
        <td class="normal2" valign="top">        

            <table class="sortable">
                <tr class="sortable">
                    <td class="sortableControls" colspan="3">
                        <ctb:tablePathList 	valueDataSource="actionForm.orgNodeId" 
                        					labelDataSource="actionForm.orgNodeName" 
                        					pathList="request.orgNodePath" />
                    </td>
                </tr>
            
            <netui-data:repeater dataSource="requestScope.orgNodes">
                <netui-data:repeaterHeader>
                
                <tr class="sortable">
                    <ctb:tableSortColumnGroup columnDataSource="actionForm.orgSortColumn" 
                    						  orderByDataSource="actionForm.orgSortOrderBy" >
                        <th class="sortable alignCenter" width="50" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
                        <th class="sortable alignLeft" width="100%" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
                        <th class="sortable alignLeft" width="100%" nowrap><ctb:tableSortColumn value="OrgNodeCode"><netui:content value="${bundle.web['common.column.orgCode']}"/></ctb:tableSortColumn></th>
                    </ctb:tableSortColumnGroup>
                </tr>
                
                </netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                
                <tr class="sortable">
                    <td class="sortable alignCenter">
                        <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId">
                            &nbsp;<netui:radioButtonOption value="${container.item.id}" onClick="setElementValueAndSubmit('{actionForm.actionElement}', '{actionForm.actionElement}');">&nbsp;</netui:radioButtonOption>                
                        </netui:radioButtonGroup>
                    </td>        
                    <td class="sortable alignLeft">     
                        <ctb:tablePathEntry srcLabelDataSource="${container.item.name}" 
                        					srcValueDataSource="${container.item.id}" 
                        					dstLabelDataSource="actionForm.orgNodeName" 
                        					dstValueDataSource="actionForm.orgNodeId" 
                        					shownAsLink="${container.item.hasChildren}"/>
                    </td>
                    <td class="sortable alignLeft">     
                        <netui:span value="${container.item.orgCode}" defaultValue="&nbsp;"/>
                    </td>
                </tr>
                
                </netui-data:repeaterItem>
                <netui-data:repeaterFooter>
                
                    <tr class="sortable">
                        <td class="sortableControls" colspan="3">
                            <ctb:tablePager dataSource="actionForm.orgPageRequested" 
                            				summary="request.orgPagerSummary" 
                            				objectLabel="${bundle.oas['object.organizations']}" id="tablePathListAnchor" anchorName="tablePathListAnchor"/>
                        </td>
                    </tr>         
                         
                </netui-data:repeaterFooter>
            </netui-data:repeater>
                
            </table>
                        
        </td>
    </tr>

</table>
</p>


<c:if test="${!noTest}">
<!-- subtest structure -->
<br/>
<h2>
    <netui:content value="${requestScope.testStatusTitle}" defaultValue="&nbsp;"/>
</h2>
<c:if test="${hasClickableSubtest}">     
<p>
    <netui:content value="${bundle.web['manageProgram.subtestStructure.message']}"/><br/>
</p>
</c:if>


<p>
<!-- subtest_structure_info.jsp -->
<a name="subtestTableAnchor"><!-- subtestTableAnchor --></a>    

<table class="sortable">
        <tr class="sortable">
            <th class="sortable alignLeft" height="25" width="*">&nbsp;&nbsp;<span><netui:content value="${bundle.web['common.column.test']}" defaultValue="&nbsp;"/></span></th>
            <th class="sortable alignCenter" height="25" width="10%">&nbsp;&nbsp;<span><netui:content value="${bundle.web['common.column.scheduled']}" defaultValue="&nbsp;"/></span></th>
            <th class="sortable alignCenter" height="25" width="10%">&nbsp;&nbsp;<span><netui:content value="${bundle.web['common.column.attempted']}" defaultValue="&nbsp;"/></span></th>
            <th class="sortable alignCenter" height="25" width="10%">&nbsp;&nbsp;<span><netui:content value="${bundle.web['common.column.completed']}" defaultValue="&nbsp;"/></span></th>
        </tr>
        <tr class="sortable">
            <td class="sortable alignLeft"><netui:content value="${requestScope.testStatus.name}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter"><netui:content value="${requestScope.testStatus.scheduled}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter"><netui:content value="${requestScope.testStatus.attempted}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter"><netui:content value="${requestScope.testStatus.completed}" defaultValue="&nbsp;"/></td>
        </tr>
<netui-data:repeater dataSource="requestScope.subtestList">
    <netui-data:repeaterHeader>
        <tr class="sortable">
            <th class="sortable alignLeft" height="25">&nbsp;&nbsp;<span><netui:content value="${bundle.web['common.column.subtestBreakdown']}" defaultValue="&nbsp;"/></span></th>
            <th class="sortable alignCenter">&nbsp;&nbsp;<span><netui:content value="&nbsp;"/></span></th>
            <th class="sortable alignCenter">&nbsp;&nbsp;<span><netui:content value="&nbsp;"/></span></th>
            <th class="sortable alignCenter">&nbsp;&nbsp;<span><netui:content value="&nbsp;"/></span></th>
        </tr>
    </netui-data:repeaterHeader>
    
    <netui-data:repeaterItem>
    
        <netui-data:getData resultId="isParent" value="${container.item.isParent}"/>  
        <netui-data:getData resultId="hasCompletedLink" value="${container.item.hasCompletedLink}"/>  
        <netui-data:getData resultId="hasScheduledLink" value="${container.item.hasScheduledLink}"/>  
        <netui-data:getData resultId="hasAttemptedLink" value="${container.item.hasAttemptedLink}"/>  
        <netui-data:getData resultId="subtestName" value="${container.item.name}"/>  
        <netui-data:getData resultId="subtestId" value="${container.item.id}"/>  
        <% 
            Boolean isParent = (Boolean)pageContext.getAttribute("isParent"); 
            String tdClass = "sortable";
            if (isParent.booleanValue()) {
                tdClass = "sortableGrey";
            }            
            Boolean hasScheduledLink = (Boolean)pageContext.getAttribute("hasScheduledLink");
            Boolean hasCompletedLink = (Boolean)pageContext.getAttribute("hasCompletedLink");
            Boolean hasAttemptedLink = (Boolean)pageContext.getAttribute("hasAttemptedLink");
            String subtestName = (String)pageContext.getAttribute("subtestName"); 
            String subtestId = (String)pageContext.getAttribute("subtestId"); 
        %> 
        
        <tr class="sortable">
            <td class="<%= tdClass %>">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<netui:span value="${container.item.name}" defaultValue="&nbsp;"/>
            </td>
            <td class="<%= tdClass %>" align="center">
<c:if test="${hasScheduledLink}"> 
        <c:if test="${viewSubtestStatus == subtestName && currentStatus == 'Scheduled'}">                 
                    <font color="red"><netui:content value="${container.item.scheduled}" defaultValue="&nbsp;"/></font>
        </c:if>            
        <c:if test="${viewSubtestStatus != subtestName || currentStatus != 'Scheduled'}">                 
                <a href="#" onclick="return submitToAction('viewSubtestStatus.do#subtestTableAnchor', '<%= subtestId %>', 'Scheduled');">             
                    <netui:content value="${container.item.scheduled}" defaultValue="&nbsp;"/>
                </a>
        </c:if>    
</c:if>
<c:if test="${!hasScheduledLink}"> 
        <netui:content value="${container.item.scheduled}" defaultValue="&nbsp;"/>
</c:if>   
            </td>
            <td class="<%= tdClass %>" align="center">
<c:if test="${hasAttemptedLink}"> 
        <c:if test="${viewSubtestStatus == subtestName && currentStatus == 'Started'}">                 
                    <font color="red"><netui:content value="${container.item.attempted}" defaultValue="&nbsp;"/></font>
        </c:if>            
        <c:if test="${viewSubtestStatus != subtestName || currentStatus != 'Started'}">                             
                <a href="#" onclick="return submitToAction('viewSubtestStatus.do#subtestTableAnchor', '<%= subtestId %>', 'Started');">             
                    <netui:content value="${container.item.attempted}" defaultValue="&nbsp;"/>
                </a>
        </c:if>     
</c:if>       
<c:if test="${!hasAttemptedLink}"> 
        <netui:content value="${container.item.attempted}" defaultValue="&nbsp;"/>
</c:if>   
            </td>
            <td class="<%= tdClass %>" align="center">
<c:if test="${hasCompletedLink}"> 
        <c:if test="${viewSubtestStatus == subtestName && currentStatus == 'Completed'}">                 
                    <font color="red"><netui:content value="${container.item.completed}" defaultValue="&nbsp;"/></font>
        </c:if>            
        <c:if test="${viewSubtestStatus != subtestName || currentStatus != 'Completed'}">                             
                <a href="#" onclick="return submitToAction('viewSubtestStatus.do#subtestTableAnchor', '<%= subtestId %>', 'Completed');">             
                    <netui:content value="${container.item.completed}" defaultValue="&nbsp;"/>
                </a>
        </c:if>      
</c:if>
<c:if test="${!hasCompletedLink}"> 
        <netui:content value="${container.item.completed}" defaultValue="&nbsp;"/>     
</c:if>            
           </td>            
        </tr>
        </netui-data:repeaterItem>
    </netui-data:repeater>
</table>
</p>


<c:if test="${viewSubtestStatus != null}">     
<br/>
<h2>
    <netui:content value="${requestScope.sessionsForTitle}" defaultValue="&nbsp;"/>
</h2>
<p>
    <netui:content value="${bundle.web['manageProgram.studentInfo.message']}"/><br/>
</p>
   
<p>

<!-- student_info_by_subtest.jsp -->
<netui:hidden dataSource="actionForm.subtestId"/> 
<netui:hidden dataSource="actionForm.status"/> 
<netui:hidden dataSource="actionForm.statusMaxPage"/>
<input type="hidden" id="subtestId" name="subtestId" value="<netui:content value="${actionForm.subtestId}"/>"/>
<input type="hidden" id="status" name="status"   value="<netui:content value="${actionForm.status}"/>"/>

<netui-data:getData resultId="allowExport" value="${requestScope.allowExport}"/>
<netui-data:getData resultId="formIsClean" value="${requestScope.formIsClean}"/>

<c:if test="${(formIsClean != null) && (! formIsClean)}">
    <p><ctb:message title="{bundle.web['common.message.form.invalidCharacters']}" style="alertMessage"></ctb:message></p>
</c:if>

<a name="programStatusAnchor"><!-- programStatusAnchor --></a>    

<div id="exportError" style="display:none">
    <p><ctb:message title="{bundle.web['manageProgram.export.support']}" style="alertMessage"></ctb:message></p>
</div>

<table class="sortable">
    <tr class="sortable">
        <td class="sortableControls" colspan="5">
            <ctb:tableFilter dataSource="actionForm.filterVisible" >
                <table class="tableFilter">
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft" colspan="6"><netui:content value="${bundle.web['manageProgram.filter.title']}"/></td>
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.sessionName']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testStatusFilter.sessionNameFilterType" size="1" defaultValue="${actionForm.testStatusFilter.sessionNameFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testStatusFilter.sessionName" onChange="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'changeFilter', 'programStatusAnchor');"/></td>                    
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.loginId']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testStatusFilter.loginIdFilterType" size="1" defaultValue="${actionForm.testStatusFilter.loginIdFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testStatusFilter.loginId" onChange="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'changeFilter', 'programStatusAnchor');"/></td>                    
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.sessionId']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testStatusFilter.sessionNumberFilterType" size="1" defaultValue="${actionForm.testStatusFilter.sessionNumberFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testStatusFilter.sessionNumber" onChange="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'changeFilter', 'programStatusAnchor');"/></td>                    
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.password']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testStatusFilter.passwordFilterType" size="1" defaultValue="${actionForm.testStatusFilter.passwordFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testStatusFilter.password" onChange="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'changeFilter', 'programStatusAnchor');"/></td>                    
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft">&nbsp;</td>
                    <td class="tableFilter alignLeft">&nbsp;</td>
                    <td class="tableFilter alignLeft">&nbsp;</td>                    
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.accessCode']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testStatusFilter.accessCodeFilterType" size="1" defaultValue="${actionForm.testStatusFilter.accessCodeFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testStatusFilter.accessCode" onChange="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'changeFilter', 'programStatusAnchor');"/></td>                    
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignRight" colspan="6">
                        <netui:button styleClass="button" value="${bundle.widgets['button.apply']}" type="submit" onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'applyFilters', 'programStatusAnchor');"/>&nbsp;
                        <netui:button styleClass="button" value="${bundle.widgets['button.clearAll']}" type="submit" onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'clearFilters', 'programStatusAnchor');"/>&nbsp;
                    </td>                        
                </tr>
                </table>
                <hr class="sortableControls"/>
            </ctb:tableFilter>
            <table class="tableFilter">
            <tr class="tableFilter">
                <td class="tableFilter">
                    <ctb:tableFilterToggle dataSource="actionForm.filterVisible" />&nbsp;
                    <c:if test="${allowExport}">
                        <netui:button tagId="exportToExcel" type="submit" value="${bundle.web['common.button.exportToExcel']}" action="exportToExcel"/>
                    </c:if>
                    <c:if test="${!allowExport}">
                        <netui:button styleClass="button" tagId="exportToExcel" value="${bundle.web['common.button.exportToExcel']}" type="button" onClick="var ele = document.getElementById('exportError');  ele.style.display='inline';"/>&nbsp;                                 
                    </c:if>
                </td>
            </tr>
            </table>
        </td>
    </tr>

<netui-data:repeater dataSource="requestScope.statusList">
    <netui-data:repeaterHeader>
    
        <tr class="sortable">
            <ctb:tableSortColumnGroup columnDataSource="actionForm.statusSortColumn" 
            						  orderByDataSource="actionForm.statusSortOrderBy"  
            						  anchorName="programStatusAnchor" >    
                <th class="sortable alignLeft" nowrap width="*"><ctb:tableSortColumn value="SessionName"><netui:content value="${bundle.web['common.column.sessionName']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap width="19%"><ctb:tableSortColumn value="SessionNumber"><netui:content value="${bundle.web['common.column.sessionId']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap width="19%"><ctb:tableSortColumn value="LoginId"><netui:content value="${bundle.web['common.column.loginId']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap width="13%"><ctb:tableSortColumn value="Password"><netui:content value="${bundle.web['common.column.password']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap width="16%"><ctb:tableSortColumn value="AccessCode"><netui:content value="${bundle.web['common.column.accessCode']}"/></ctb:tableSortColumn></th>
            </ctb:tableSortColumnGroup>
        </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
        <tr class="sortable">
            <td class="sortable alignLeft">
                <netui-data:getData resultId="testAdminId" value="${container.item.testAdminId}"/>  
                <netui-data:getData resultId="canViewSession" value="${container.item.canViewSession}"/>  
                <c:if test="${canViewSession}">
                <% String href = "goto_user_view_session_information.do?testAdminId=" + pageContext.getAttribute("testAdminId"); %> 
                <netui:anchor href="<%= href %>">
                   <netui:span value="${container.item.sessionName}" defaultValue="&nbsp;"/>
                </netui:anchor>        
                </c:if>        
                <c:if test="${!canViewSession}">
                   <netui:span value="${container.item.sessionName}" defaultValue="&nbsp;"/>
                </c:if>        
            </td>
            <td class="sortable alignLeft"><netui:span value="${container.item.sessionNumber}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.loginId}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.password}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.accessCode}" defaultValue="&nbsp;"/></td>
        </tr>
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
        <tr class="sortable">
            <td class="sortableControls" colspan="5">
            	<ctb:tablePager dataSource="actionForm.statusPageRequested" 
                				summary="request.statusPagerSummary" 
                				objectLabel="${bundle.oas['object.students']}" 
                				anchorName="programStatusAnchor"/> 
            </td>
        </tr>             
    </netui-data:repeaterFooter>
</netui-data:repeater>
    <ctb:tableNoResults dataSource="request.statusList">
        <tr class="sortable">
            <td class="sortable" colspan="5">
                <ctb:message title="{bundle.web['common.message.noStudent.title']}" style="tableMessage">
                    <netui:content value="${bundle.web['common.message.noStudent.message']}"/>
                </ctb:message>
            </td>
        </tr>
    </ctb:tableNoResults>
</table>
</p>

</c:if>            
</c:if>
</netui:form>
</c:if>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>







