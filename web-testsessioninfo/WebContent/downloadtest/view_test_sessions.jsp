<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['loadTest.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.loadTestViewTestSessions']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<h1><netui:content value="${bundle.web['loadTest.viewTestSessions.title']}"/></h1>
<p><netui:content value="${bundle.web['loadTest.viewTestSessions.title.message1']}"/></p>
<p><netui:content value="${bundle.web['loadTest.viewTestSessions.title.message2']}"/></p>
<p><b>
    <netui:content value="${bundle.web['loadTest.viewTestSessions.tableTitle']}"/>
    <netui:content value="${requestScope.itemSetName}"/>
</b></p>

<table class="sortable">
<netui:form action="view_test_sessions">
    <netui:hidden dataSource="actionForm.actionElement"/>
    <netui:hidden dataSource="actionForm.maxPage"/>
<netui-data:repeater dataSource="requestScope.sessions">
    <netui-data:repeaterHeader>

        <tr class="sortable">
            <ctb:tableSortColumnGroup columnDataSource="actionForm.sessionSortColumn" 
                                      orderByDataSource="actionForm.sessionSortOrderBy" >
                <th class="sortable alignLeft" nowrap>
                    <ctb:tableSortColumn value="TestAdminName">
                            <netui:span value="${bundle.web['common.column.sessionName']}"/>
                    </ctb:tableSortColumn>
                </th>
                <th class="sortable alignLeft" nowrap>
                    <ctb:tableSortColumn value="LoginStartDate">
                        <netui:span value="${bundle.web['common.column.startDate']}"/>
                    </ctb:tableSortColumn>
                </th>
                <th class="sortable alignLeft" nowrap>
                    <ctb:tableSortColumn value="LoginEndDate">
                        <netui:span value="${bundle.web['common.column.endDate']}"/>
                    </ctb:tableSortColumn>
                </th>
                <th class="sortable alignLeft" nowrap>
                    <ctb:tableSortColumn value="OrgNodeName">
                        <netui:span value="${bundle.web['common.column.organization']}"/>
                    </ctb:tableSortColumn>
                </th>
           </ctb:tableSortColumnGroup>
        </tr>
            
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>

        <tr class="sortable">
            <td class="sortable alignLeft">
                <netui:span value="${container.item.testAdminName}" defaultValue="&nbsp;">
                </netui:span>
            </td>
            <td class="sortable alignLeft">
                <netui:span value="${container.item.loginStartDate}" defaultValue="&nbsp;">
                    <netui:formatDate pattern="MM/dd/yy"/>
                </netui:span>
            </td>
            <td class="sortable alignLeft">
                <netui:span value="${container.item.loginEndDate}" defaultValue="&nbsp;">
                    <netui:formatDate pattern="MM/dd/yy"/>
                </netui:span>
            </td>                         
            <td class="sortable alignLeft">
                <netui:span value="${container.item.orgNodeName}" defaultValue="&nbsp;">
                </netui:span>
            </td>                         
        </tr>

    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
        <tr class="sortable">
            <td class="sortableControls" colspan="4">
                <ctb:tablePager dataSource="actionForm.sessionPageRequested" 
                                summary="request.sessionPagerSummary" 
                                objectLabel="${bundle.oas['object.testSessions']}" />
            </td>
        </tr>       
    </netui-data:repeaterFooter>
    
</netui-data:repeater>
    
</netui:form>
</table>
<br>

<netui:button type="button" value="Back" onClick="submitFormAndSwitchPage('returnto_select_tests.do')"/>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>

