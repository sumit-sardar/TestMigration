<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls">

<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="3">
            <ctb:tablePathList valueDataSource="actionForm.orgNodeId" labelDataSource="actionForm.orgNodeName" pathList="request.orgNodePath" />
        </td>
    </tr>

<netui-data:repeater dataSource="requestScope.orgNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.orgSortColumn" orderByDataSource="actionForm.orgSortOrderBy" >
            <th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
            <th class="sortable alignLeft" width="75%" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
            <th class="sortable alignCenter" width="20%" nowrap><netui:content value="Users At & Below"/></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter">
            <netui-data:getData resultId="isSelectable" value="${container.item.selectable}"/>
            <netui-data:getData resultId="hasChildren" value="${container.item.hasChildren}"/>
            
            <c:if test="${isSelectable == 'false'}">                                    
                <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId" disabled="true">
                    &nbsp;<netui:radioButtonOption value="${container.item.id}">&nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>
            </c:if>
            <c:if test="${isSelectable == 'true'}">                            
                <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId">
                    &nbsp;<netui:radioButtonOption value="${container.item.id}" onClick="setElementValueAndSubmitWithAnchor('{actionForm.actionElement}', '{actionForm.actionElement}', 'userSearchResult');">&nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>
            </c:if>
        </td>        
        <td class="sortable alignLeft">     
            <ctb:tablePathEntry srcLabelDataSource="${container.item.name}" srcValueDataSource="${container.item.id}" dstLabelDataSource="actionForm.orgNodeName" dstValueDataSource="actionForm.orgNodeId" shownAsLink="${container.item.hasChildren}"/>
        </td>
        <td class="sortable alignCenter">
            <netui:span value="${container.item.studentCount}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="7">
                <ctb:tablePager 
                	dataSource="actionForm.orgPageRequested" 
                	summary="request.orgPagerSummary" 
                	objectLabel="${bundle.oas['object.organizations']}" 
                	id="tablePathListAnchor" 
                	anchorName="tablePathListAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    
</table>

        </td>
    </tr>
</table>


