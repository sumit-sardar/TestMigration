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
    <netui-template:setAttribute name="title" value="${bundle.web['browseTestSessions.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findTestSession']}"/>
<netui-template:section name="bodySection">


<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
             

<netui:form action="view_test_sessions">

    <netui:hidden dataSource="actionForm.actionElement"/>
    <netui:hidden dataSource="actionForm.currentAction"/>
    <netui:hidden dataSource="actionForm.sessionMaxPage"/>
    <netui:hidden dataSource="actionForm.selectedOrgNodeName"/>

    <netui-data:getData resultId="enableRegisterStudent" value="${requestScope.enableRegisterStudent}"/>
	<!-- Change for License Management LM14 -->
    <table width="100%" border="0">
        <tr valign="top">
            <td valign="top">             
                <h1><netui:content value="${bundle.web['browseTestSessions.title']}"/></h1>
                <p><netui:content value="${bundle.web['browseTestSessions.title.message']}"/></p>
            </td>
            <td valign="top" align="right">
                <c:if test="${ sessionScope.hasLicenseConfig }">                 
                    <table width="280">
                        <tr>
                            <td>
                                <ctb:showHideSection sectionId="customerLicenses" sectionTitle="View Licenses" sectionVisible="actionForm.licenseVisible">
                                    <jsp:include page="../homepage/license.jsp" />
                                </ctb:showHideSection>
                            </td>
                        </tr>
                    </table>
                </c:if>
            </td>
        </tr>
    </table>    
<h2><netui:content value="${bundle.web['browseTestSessions.selectGroup.title']}"/></h2>
<p><netui:content value="${bundle.web['browseTestSessions.selectGroup.title.message']}"/></p>

<a name="tablePathListAnchor"><!-- tablePathListAnchor --></a>    
    
<p><table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="3">
            <ctb:tablePathList valueDataSource="actionForm.orgNodeId" 
            				   labelDataSource="actionForm.orgNodeName" 
            				   pathList="request.orgNodePath" />
        </td>
    </tr>

<netui-data:repeater dataSource="requestScope.orgNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.orgSortColumn" 
        						  orderByDataSource="actionForm.orgSortOrderBy" >
            <th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
            <th class="sortable alignLeft" width="73%" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
            <th class="sortable alignRight" width="22%" nowrap><netui:content value="${bundle.web['common.column.totalTestSessions']}"/></th>
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
        <td class="sortable alignRight">
            <netui:span value="${container.item.sessionCount}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="7">
                <ctb:tablePager dataSource="actionForm.orgPageRequested" 
                				summary="request.orgPagerSummary" 
                				objectLabel="${bundle.oas['object.organizations']}" 
                				id="tablePathListAnchor" 
                				anchorName="tablePathListAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    
</table></p>

<br><h2><netui:span value="${actionForm.selectedOrgNodeName}"/> <netui:content value="${bundle.web['browseTestSessions.selectTest.title']}"/></h2>

<c:if test="${! sessionScope.canRegisterStudent}">
    <p><netui:content value="${bundle.web['homepage.viewTestSessions.message']}"/></p>
</c:if>

<c:if test="${sessionScope.canRegisterStudent}">
    <p><netui:content value="${bundle.web['homepage.viewTestSessions.tabe.message']}"/></p>
</c:if>

<a name="tableSessionAnchor"><!-- tableSessionAnchor --></a>    

<ctb:tableTabGroup dataSource="actionForm.sessionFilterTab" anchorName="tableSessionAnchor">
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
                
                    <netui:button styleClass="button" 
                    			  tagId="sessionViewStatus" 
                    			  value="${bundle.widgets['button.viewStatus']}" 
                    			  type="submit" 
                    			  onClick="setElementValueAndSubmit('{actionForm.currentAction}', 'sessionViewStatus');" 
                    			  disabled="true" />
                    
                    <netui:button styleClass="button" 
                    			  tagId="sessionEdit" 
                    			  value="${bundle.widgets['button.edit']}" 
                    			  type="submit" 
                    			  onClick="setElementValueAndSubmit('{actionForm.currentAction}', 'sessionEdit');" 
                    			  disabled="true" />
                    			  
                <c:if test="${sessionScope.canRegisterStudent}">
                    <netui:button styleClass="button" 
                    			  tagId="registerStudent" 
                    			  value="${bundle.widgets['button.registerStudent']}" 
                    			  type="submit" 
                    			  onClick="setElementValueAndSubmit('{actionForm.currentAction}', 'registerStudent');" 
                    			  disabled="true"/>
                </c:if>
                </td>
            </tr>
            </table>
        </td>
    </tr>

    
<netui-data:repeater dataSource="requestScope.sessionList">
    <netui-data:repeaterHeader>
    
        <tr class="sortable">
            <ctb:tableSortColumnGroup columnDataSource="actionForm.sessionSortColumn" 
            						  orderByDataSource="actionForm.sessionSortOrderBy" 
            						  anchorName="tableSessionAnchor" >            
                <th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="TestAdminName"><netui:content value="${bundle.web['common.column.sessionName']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="TestName"><netui:content value="${bundle.web['common.column.testName']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="CreatorOrgNodeName"><netui:content value="${requestScope.testSessionOrgCategoryName}"/></ctb:tableSortColumn></th>
                <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="LoginStartDate"><netui:content value="${bundle.web['common.column.startDate']}"/></ctb:tableSortColumn></th>
                <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="LoginEndDate"><netui:content value="${bundle.web['common.column.endDate']}"/></ctb:tableSortColumn></th>                
            </ctb:tableSortColumnGroup>
        </tr>
        
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
        <tr class="sortable">
            <td class="sortable alignCenter">
            <c:if test="${enableRegisterStudent}">                                    
                <netui:radioButtonGroup dataSource="actionForm.sessionId">
                    &nbsp;<netui:radioButtonOption value="${container.item.testAdminId}" alt="${container.item.isRegisterStudentEnable}" onClick="enableElementById(getNetuiTagName('sessionViewStatus')); enableElementById(getNetuiTagName('sessionEdit')); enableButton(this.alt,getNetuiTagName('registerStudent'));">&nbsp;</netui:radioButtonOption> <!-- Changes for RegisterAnotherStudent Button -->
                </netui:radioButtonGroup>
            </c:if>
            <c:if test="${! enableRegisterStudent}">                                    
                <netui:radioButtonGroup dataSource="actionForm.sessionId">
                    &nbsp;<netui:radioButtonOption value="${container.item.testAdminId}" onClick="enableElementById(getNetuiTagName('sessionViewStatus')); enableElementById(getNetuiTagName('sessionEdit')); ">&nbsp;</netui:radioButtonOption>
                </netui:radioButtonGroup>
            </c:if>
            </td>
            <td class="sortable">
                <netui-data:getData resultId="testAdminId" value="${container.item.testAdminId}"/>  
                <% String href = "goto_view_session_information.do?testAdminId=" + pageContext.getAttribute("testAdminId"); %> 
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
                    <ctb:tablePager dataSource="actionForm.sessionPageRequested" 
                    				summary="request.sessionPagerSummary" 
                    				objectLabel="${bundle.oas['object.testSessions']}" 
                    				anchorName="tableSessionAnchor" 
                    				id="tableSessionAnchor" />
                </td>
            </tr>    
        </netui-data:repeaterFooter>
    </netui-data:repeater>
    <ctb:tableNoResults dataSource="request.sessionList">
        <tr class="sortable">
            <td class="sortable" colspan="7">
                <ctb:switch dataSource="${actionForm.sessionFilterTab}">
                    <ctb:case value="CU">
                        <ctb:message title="${bundle.web['common.message.browse.noCurrentTestSessions.title']}" style="tableMessage">
                            <netui:content value="${bundle.web['common.message.browse.noCurrentTestSessions.message']}"/>
                        </ctb:message>
                    </ctb:case>
                    <ctb:case value="FU">
                        <ctb:message title="${bundle.web['common.message.browse.noFutureTestSessions.title']}" style="tableMessage">
                            <netui:content value="${bundle.web['common.message.browse.noFutureTestSessions.message']}"/>
                        </ctb:message>
                    </ctb:case>
                    <ctb:case value="PA">
                        <ctb:message title="${bundle.web['common.message.browse.noCompletedTestSessions.title']}" style="tableMessage">
                            <netui:content value="${bundle.web['common.message.browse.noCompletedTestSessions.message']}"/>
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

