<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.action.ActionForm"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Path and List Table"/>
    <netui-template:section name="bodySection">

    <h1><netui:span value="Path and List Table"/></h1>
    <p>
    The "path and list" table is typically used for organizational traversing or any other
    bread-crumb navigation.
    </p>

    <netui:form action="viewPathListTable">    

        <table class="sortable">
        <tr class="sortable">
            <td class="sortableControls" colspan="4">
            
                <ctb:tablePathList valueDataSource="actionForm.orgNodeId" 
                				   labelDataSource="actionForm.orgNodeName" 
                				   pathList="request.orgNodePath" />
                
            </td>
        </tr>
        <tr class="sortable">
            <ctb:tableSortColumnGroup columnDataSource="actionForm.sortColumn" orderByDataSource="actionForm.sortOrderBy" >
            <th class="sortable alignCenter radioColumn"><netui:span value="Select"/></th>
            <th class="sortable alignLeft"><ctb:tableSortColumn value="something"><netui:span value="Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignRight"><ctb:tableSortColumn value="whatever"><netui:span value="Students Within"/></ctb:tableSortColumn></th>
            <th class="sortable alignRight"><ctb:tableSortColumn value="whomever"><netui:span value="Students Assigned"/></ctb:tableSortColumn></th>
            </ctb:tableSortColumnGroup>
        </tr>
        <netui-data:repeater dataSource="pageFlow.orgNodes">
            <netui-data:repeaterItem>
                <tr class="sortable">
                    <td class="sortable alignCenter">
                        <netui:radioButtonGroup dataSource="actionForm.childOrgNodeId">
                            <netui:radioButtonOption value="${container.item.orgNodeId}" styleClass="radioButton">&nbsp;</netui:radioButtonOption>
                        </netui:radioButtonGroup>
                    </td>
                    <td class="sortable alignLeft"><ctb:tablePathEntry srcLabelDataSource="${container.item.orgNodeName}" srcValueDataSource="${container.item.orgNodeId}" dstLabelDataSource="${actionForm.orgNodeName}" dstValueDataSource="${actionForm.orgNodeId}" /></td>
                    <td class="sortable alignRight"><netui:span value="${container.item.totalStudentsWithin}" defaultValue="&nbsp;"/></td>
                    <td class="sortable alignRight"><netui:span value="${container.item.totalStudentsAssigned}" defaultValue="&nbsp;"/></td>
                </tr>
            </netui-data:repeaterItem>
            <netui-data:repeaterFooter>
                <tr class="sortable">
                    <td class="sortableControls" colspan="4">
                        <ctb:tablePager dataSource="actionForm.pageRequested" summary="request.pathPagerSummary" objectLabel="${request.nodeLabel}"/>
                    </td>
                </tr>
            </netui-data:repeaterFooter>
        </netui-data:repeater>
        <ctb:tableNoResults dataSource="request.noStudents">
            <tr class="sortable">
                <td class="sortable" colspan="4">
                    <ctb:message title="No Students Found." style="tableMessage">
                        <netui:span value="Sorry, no students were found.  Please try again."/>
                    </ctb:message>
                </td>
            </tr>
        </ctb:tableNoResults>
        </table>
    </netui:form>


    </netui-template:section>
</netui-template:template>
