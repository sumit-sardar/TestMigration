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
    <netui-template:setAttribute name="title" value="${bundle.web['homepage.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.home']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->



<netui:form action="home_page">

    <netui:hidden dataSource="actionForm.actionElement"/>
    <netui:hidden dataSource="actionForm.userMaxPage"/>
    <netui:hidden dataSource="actionForm.proctorMaxPage"/>
    
    <netui-data:getData resultId="enableUserRegisterStudent" value="${requestScope.enableUserRegisterStudent}"/>
    <netui-data:getData resultId="enableProctorRegisterStudent" value="${requestScope.enableProctorRegisterStudent}"/>


<!-- ********************************************************************************************************************* -->
<!-- Licenses  -->
<!-- ********************************************************************************************************************* -->

<table width="100%" border="0">
<tr valign="top">
<td valign="top">
    <h1><netui:content value="${bundle.web['homepage.title']}"/></h1>
    <p><netui:content value="${bundle.web['homepage.title.message']}"/></p>
</td>

<td valign="top" align="right">
<c:if test="${ sessionScope.hasLicenseConfig}"> 
<table width="280">
<tr>
    <td>
    <ctb:showHideSection sectionId="customerLicenses" sectionTitle="View Licenses" sectionVisible="actionForm.licenseVisible">
        <jsp:include page="license.jsp" />
    </ctb:showHideSection>
    </td>
</tr>
</table>
</c:if>
</td>
</tr>
</table>
    

<!-- ********************************************************************************************************************* -->
<!-- Broadcast Message  -->
<!-- ********************************************************************************************************************* -->
<c:if test="${broadcastMessages != null}">
    <table class="grouping" width="100%">
    <netui-data:repeater dataSource="requestScope.broadcastMessages">
        <netui-data:repeaterHeader>
        <h2><netui:content value="${bundle.web['homepage.broadcastMessages.title']}"/></h2>
        <p><netui:content value="${bundle.web['homepage.broadcastMessages.information']}"/></p>
            <tr class="grouping">
                <th class="grouping"><netui:content value="${bundle.web['homepage.broadcastMessages.message']}"/></th>
                <th class="grouping"><netui:content value="${bundle.web['homepage.broadcastMessages.date']}"/></th>
            </tr>
        </netui-data:repeaterHeader>
        <netui-data:repeaterItem>
            <tr class="sortable">    
                <td class="sortable alignLeft"><netui:content value="${container.item.message}" defaultValue="&nbsp;"/></td>
                <td class="sortable alignLeft"><netui:span value="${container.item.createdDateTime}" defaultValue="&nbsp;"><netui:formatDate pattern="MM/dd/yy"/></netui:span></td>
            </tr>
        </netui-data:repeaterItem>
    </netui-data:repeater>
    </table><br><br>
</c:if>


<!-- ********************************************************************************************************************* -->
<!-- View Your Test Sessions -->
<!-- ********************************************************************************************************************* -->
<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator">

<h2><netui:content value="${bundle.web['homepage.viewTestSessions.title']}"/></h2>

<c:if test="${! sessionScope.canRegisterStudent}">
    <p><netui:content value="${bundle.web['homepage.viewTestSessions.message']}"/></p>
</c:if>

<c:if test="${sessionScope.canRegisterStudent}">
    <p><netui:content value="${bundle.web['homepage.viewTestSessions.tabe.message']}"/></p>
</c:if>

<a name="tableUserAnchor"><!-- tableUserAnchor --></a>    
    
    <ctb:tableTabGroup dataSource="actionForm.userSessionFilterTab">
        <ctb:tableTab value="CU"><netui:content value="${bundle.widgets['tab.current']}"/></ctb:tableTab>
        <ctb:tableTab value="FU"><netui:content value="${bundle.widgets['tab.future']}"/></ctb:tableTab>
        <ctb:tableTab value="PA"><netui:content value="${bundle.widgets['tab.completed']}"/></ctb:tableTab>
    </ctb:tableTabGroup>

    <table class="sortable">
    
        <tr class="sortable">
            <td class="sortableControls" colspan="7">
                <table class="tableFilter">
                <tr class="tableFilter">
                    <td class="tableFilter">        
                        <netui:button styleClass="button" tagId="userSessionViewStatus" value="${bundle.widgets['button.viewStatus']}" type="submit" action="goto_view_user_session_monitor_status" disabled="${requestScope.userSessionDisableButton}"/>
                        <netui:button styleClass="button" tagId="userSessionEdit" value="${bundle.widgets['button.edit']}" type="submit" action="goto_user_edit_session_information" disabled="${requestScope.userSessionDisableButton}"/>
                    <c:if test="${sessionScope.canRegisterStudent}">
                        <netui:button styleClass="button" tagId="userRegisterStudent" value="${bundle.widgets['button.registerStudent']}" type="submit" action="goto_user_register_student" disabled="${requestScope.userSessionDisableButton}"/>
                    </c:if>
                    </td>
                </tr>
                </table>
            </td>
        </tr>
         
    <netui-data:repeater dataSource="requestScope.userSessions">
        <netui-data:repeaterHeader>
    
            <tr class="sortable">
                <ctb:tableSortColumnGroup columnDataSource="actionForm.userSessionSortColumn" 
                						  orderByDataSource="actionForm.userSessionSortOrderBy" >
                    <th class="sortable alignCenter" nowrap><netui:span value="${bundle.web['common.column.select']}"/></th>                
                    <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="TestAdminName"><netui:span value="${bundle.web['common.column.sessionName']}"/></ctb:tableSortColumn></th>
                    <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="TestName"><netui:span value="${bundle.web['common.column.testName']}"/></ctb:tableSortColumn></th>
                    <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="CreatorOrgNodeName"><netui:content value="${requestScope.userOrgCategoryName}"/></ctb:tableSortColumn></th>
                    <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="LoginStartDate"><netui:span value="${bundle.web['common.column.startDate']}"/></ctb:tableSortColumn></th>
                    <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="LoginEndDate"><netui:span value="${bundle.web['common.column.endDate']}"/></ctb:tableSortColumn></th>                 
                </ctb:tableSortColumnGroup>
            </tr>
                
        </netui-data:repeaterHeader>
        <netui-data:repeaterItem>
    
            <tr class="sortable">
                <td class="sortable alignCenter">
                    <c:if test="${enableUserRegisterStudent}">                
                    <netui:radioButtonGroup dataSource="actionForm.userSessionId">
                        &nbsp;<netui:radioButtonOption value="${container.item.testAdminId}" alt="${container.item.isRegisterStudentEnable}" onClick="enableElementById(getNetuiTagName('userSessionViewStatus')); enableElementById(getNetuiTagName('userSessionEdit')); enableButton(this.alt,getNetuiTagName('userRegisterStudent'));">&nbsp;</netui:radioButtonOption> <!-- Changes for RegisterAnotherStudent Button -->
                    </netui:radioButtonGroup>
                    </c:if>
                    <c:if test="${! enableUserRegisterStudent}">                
                    <netui:radioButtonGroup dataSource="actionForm.userSessionId">
                        &nbsp;<netui:radioButtonOption value="${container.item.testAdminId}" onClick="enableElementById(getNetuiTagName('userSessionViewStatus')); enableElementById(getNetuiTagName('userSessionEdit')); ">&nbsp;</netui:radioButtonOption>
                    </netui:radioButtonGroup>
                    </c:if>
                </td>
                <td class="sortable">
                    <netui-data:getData resultId="testAdminId" value="${container.item.testAdminId}"/>  
                    <% String href = "goto_user_view_session_information.do?testAdminId=" + pageContext.getAttribute("testAdminId"); %> 
                    <netui:anchor href="<%= href %>">
                        <netui:span value="${container.item.testAdminName}" defaultValue="&nbsp;"/>
                    </netui:anchor>
                </td> 
                <td class="sortable"><netui:span value="${container.item.testName}" defaultValue="&nbsp;"/></td>
                <td class="sortable alignLeft"><netui:span value="${container.item.creatorOrgNodeName}" defaultValue="&nbsp;"/></td>
                <td class="sortable alignCenter"><netui:span value="${container.item.loginStartDate}" defaultValue="&nbsp;"><netui:formatDate pattern="MM/dd/yy"/></netui:span></td>
                <td class="sortable alignCenter"><netui:span value="${container.item.loginEndDate}" defaultValue="&nbsp;"><netui:formatDate pattern="MM/dd/yy"/></netui:span></td>                         
    
            </tr>
    
        </netui-data:repeaterItem>
            <netui-data:repeaterFooter>
                <tr class="sortable">
                    <td class="sortableControls" colspan="7">
                        <ctb:tablePager dataSource="actionForm.userSessionPageRequested" 
                        				summary="request.userSessionPagerSummary" 
                        				objectLabel="${bundle.oas['object.testSessions']}" 
                        				anchorName="tableUserAnchor"/>                     
                    </td>
                </tr>     
            </netui-data:repeaterFooter>
        </netui-data:repeater>
        <ctb:tableNoResults dataSource="request.userSessions">
            <tr class="sortable">
                <td class="sortable" colspan="7">
                    <ctb:switch dataSource="${actionForm.userSessionFilterTab}">
                        <ctb:case value="CU">
                            <ctb:message title="${bundle.web['common.message.user.noCurrentTestSessions.title']}" style="tableMessage">
                                <netui:content value="${bundle.web['common.message.user.noCurrentTestSessions.message']}"/>
                            </ctb:message>
                        </ctb:case>
                        <ctb:case value="FU">
                            <ctb:message title="${bundle.web['common.message.user.noFutureTestSessions.title']}" style="tableMessage">
                                <netui:content value="${bundle.web['common.message.user.noFutureTestSessions.message']}"/>
                            </ctb:message>
                        </ctb:case>
                        <ctb:case value="PA">
                            <ctb:message title="${bundle.web['common.message.user.noCompletedTestSessions.title']}" style="tableMessage">
                                <netui:content value="${bundle.web['common.message.user.noCompletedTestSessions.message']}"/>
                            </ctb:message>
                        </ctb:case>
                    </ctb:switch>
                </td>
            </tr>
        </ctb:tableNoResults>
    </table>
    <br><br>
</ctb:auth>

<!-- ********************************************************************************************************************* -->
<!-- View Your Proctor Assignments -->
<!-- ********************************************************************************************************************* -->
<h2><netui:content value="${bundle.web['homepage.viewProctorAssignments.title']}"/></h2>

<c:if test="${! sessionScope.canRegisterStudent}">
    <p><netui:content value="${bundle.web['homepage.viewProctorAssignments.message']}"/></p>
</c:if>
<c:if test="${sessionScope.canRegisterStudent}">
    <p><netui:content value="${bundle.web['homepage.viewProctorAssignments.tabe.message']}"/></p>
</c:if>


<a name="tableProctorAnchor"><!-- tableProctorAnchor --></a>    

<ctb:tableTabGroup dataSource="actionForm.proctorSessionFilterTab" anchorName="tableProctorAnchor">
    <ctb:tableTab value="CU"><netui:content value="${bundle.widgets['tab.current']}"/></ctb:tableTab>
    <ctb:tableTab value="FU"><netui:content value="${bundle.widgets['tab.future']}"/></ctb:tableTab>
    <ctb:tableTab value="PA"><netui:content value="${bundle.widgets['tab.completed']}"/></ctb:tableTab>
</ctb:tableTabGroup>

<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="7">
            <table class="tableFilter">
            <tr class="tableFilter">
                <td class="tableFilter">        
                    <netui:button styleClass="button" tagId="proctorSessionViewStatus" value="${bundle.widgets['button.viewStatus']}" type="submit" action="goto_view_proctor_session_monitor_status" disabled="${requestScope.proctorSessionDisableButton}"/>
                    <netui:button styleClass="button" tagId="proctorSessionEdit" value="${bundle.widgets['button.edit']}" type="submit" action="goto_proctor_edit_session_information" disabled="${requestScope.proctorSessionDisableButton}"/>
                <c:if test="${sessionScope.canRegisterStudent}">
                    <netui:button styleClass="button" tagId="proctorRegisterStudent" value="${bundle.widgets['button.registerStudent']}" type="submit" action="goto_protor_register_student" disabled="${requestScope.proctorSessionDisableButton}"/>
                </c:if>
                </td>
            </tr>
            </table>
        </td>
    </tr>
    
<netui-data:repeater dataSource="requestScope.proctorAssignmentSessions">
    <netui-data:repeaterHeader>
    
        <tr class="sortable">
            <ctb:tableSortColumnGroup columnDataSource="actionForm.proctorSessionSortColumn" orderByDataSource="actionForm.proctorSessionSortOrderBy" anchorName="tableProctorAnchor">            
                <th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="TestAdminName"><netui:span value="${bundle.web['common.column.sessionName']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="TestName"><netui:span value="${bundle.web['common.column.testName']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="CreatorOrgNodeName"><netui:content value="${requestScope.proctorOrgCategoryName}"/></ctb:tableSortColumn></th>
                <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="LoginStartDate"><netui:span value="${bundle.web['common.column.startDate']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="LoginEndDate"><netui:span value="${bundle.web['common.column.endDate']}"/></ctb:tableSortColumn></th>                
            </ctb:tableSortColumnGroup>
        </tr>
        
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
        <tr class="sortable">
            <td class="sortable alignCenter">
                <c:if test="${enableProctorRegisterStudent}">                            
                <netui:radioButtonGroup dataSource="actionForm.proctorSessionId">
                    &nbsp;<netui:radioButtonOption value="${container.item.testAdminId}" alt="${container.item.isRegisterStudentEnable}" onClick="enableElementById(getNetuiTagName('proctorSessionViewStatus')); enableElementById(getNetuiTagName('proctorSessionEdit')); enableButton(this.alt,getNetuiTagName('proctorRegisterStudent'));">&nbsp;</netui:radioButtonOption>
                </netui:radioButtonGroup>
                </c:if>
                <c:if test="${! enableProctorRegisterStudent}">                            
                <netui:radioButtonGroup dataSource="actionForm.proctorSessionId">
                    &nbsp;<netui:radioButtonOption value="${container.item.testAdminId}" onClick="enableElementById(getNetuiTagName('proctorSessionViewStatus')); enableElementById(getNetuiTagName('proctorSessionEdit'));">&nbsp;</netui:radioButtonOption>
                </netui:radioButtonGroup>
                </c:if>
            </td>
            <td class="sortable">
                <netui-data:getData resultId="testAdminId" value="${container.item.testAdminId}"/>  
                <% String href = "goto_proctor_view_session_information.do?testAdminId=" + pageContext.getAttribute("testAdminId"); %> 
                <netui:anchor href="<%= href %>">
                    <netui:span value="${container.item.testAdminName}" defaultValue="&nbsp;"/>
                </netui:anchor>                
            </td>
            <td class="sortable"><netui:span value="${container.item.testName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft"><netui:span value="${container.item.creatorOrgNodeName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter"><netui:span value="${container.item.loginStartDate}" defaultValue="&nbsp;"><netui:formatDate pattern="MM/dd/yy"/></netui:span></td>
            <td class="sortable alignCenter"><netui:span value="${container.item.loginEndDate}" defaultValue="&nbsp;"><netui:formatDate pattern="MM/dd/yy"/></netui:span></td>
            
        </tr>
        
        </netui-data:repeaterItem>
        <netui-data:repeaterFooter>
            <tr class="sortable">
                <td class="sortableControls" colspan="7">
                    <ctb:tablePager dataSource="actionForm.proctorSessionPageRequested" 
                    				summary="request.proctorSessionPagerSummary" 
                    				objectLabel="${bundle.oas['object.testSessions']}" 
                    				anchorName="tableProctorAnchor"/>
                </td>
            </tr>       
        </netui-data:repeaterFooter>
    </netui-data:repeater>
    <ctb:tableNoResults dataSource="request.proctorAssignmentSessions">
        <tr class="sortable">
            <td class="sortable" colspan="7">
                <ctb:switch dataSource="${actionForm.proctorSessionFilterTab}">
                    <ctb:case value="CU">
                        <ctb:message title="${bundle.web['common.message.proctor.noCurrentTestSessions.title']}" style="tableMessage">
                            <netui:content value="${bundle.web['common.message.proctor.noCurrentTestSessions.message']}"/>
                        </ctb:message>
                    </ctb:case>
                    <ctb:case value="FU">
                        <ctb:message title="${bundle.web['common.message.proctor.noFutureTestSessions.title']}" style="tableMessage">
                            <netui:content value="${bundle.web['common.message.proctor.noFutureTestSessions.message']}"/>
                        </ctb:message>
                    </ctb:case>
                    <ctb:case value="PA">
                        <ctb:message title="${bundle.web['common.message.proctor.noCompletedTestSessions.title']}" style="tableMessage">
                            <netui:content value="${bundle.web['common.message.proctor.noCompletedTestSessions.message']}"/>
                        </ctb:message>
                    </ctb:case>
                </ctb:switch>
            </td>
        </tr>
    </ctb:tableNoResults>
</table>

</netui:form>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


