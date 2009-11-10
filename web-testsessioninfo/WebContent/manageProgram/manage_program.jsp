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

<!-- select a test and an organization -->
<p>
	<jsp:include page="/manageProgram/select_test_organization.jsp" /> 
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
	<jsp:include page="/manageProgram/subtest_structure_info.jsp" />	 
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







