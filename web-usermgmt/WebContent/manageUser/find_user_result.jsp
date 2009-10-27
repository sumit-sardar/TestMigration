<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>



<!--  userList table -->
<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="12" height="30">&nbsp;
            <netui:button tagId="View" type="submit" value="View" onClick="setElementValue('{actionForm.currentAction}', 'viewUser');" disabled="${requestScope.disableViewButton}"/>              
            <netui:button tagId="Edit" type="submit" value=" Edit " onClick="setElementValue('{actionForm.currentAction}', 'editUser');" disabled="${requestScope.disableEditButton}"/>
            <netui:button tagId="Delete" type="submit" value="Delete" onClick="return verifyDeleteUser();" disabled="${requestScope.disableDeleteButton}"/>             
            <%--<netui:button tagId="changePassword" type="submit" value="Change Password" onClick="setElementValue('{actionForm.currentAction}', 'changePassword');" disabled="{request.disableChangePasswordButton}"/>--%>
            <netui:button tagId="changePassword" type="submit" value="Change Password" onClick="setElementValue('{actionForm.currentAction}', 'changePassword');" disabled="${requestScope.disableChangePasswordButton}"/>
        </td>
    </tr>
        
        
<netui-data:repeater dataSource="pageFlow.userList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.userSortColumn" orderByDataSource="actionForm.userSortOrderBy" anchorName="userSearchResult">
            <th class="sortable alignCenter" nowrap>&nbsp;<netui:content value="${bundle.web['common.column.select']}"/>&nbsp;</th>                
            <th class="sortable alignLeft" width="25%" nowrap><ctb:tableSortColumn value="DisplayUserName">User Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="UserName">Login ID</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="Email">Email</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="25%" nowrap>&nbsp;&nbsp;Organization</th>
            <th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="RoleName">Role</ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter">
            <netui:radioButtonGroup dataSource="actionForm.selectedUserId">
                &nbsp;<netui:radioButtonOption value="${container.item.userId}" alt="${container.item.actionPermission}" onClick="return enableButtons(this.alt);">&nbsp;</netui:radioButtonOption>                
            </netui:radioButtonGroup>
        </td>        
        <td class="sortable">
            <netui:span value="${container.item.displayName}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.loginId}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.email}"/>
        </td>
        <td class="sortable">
            <netui:content value="${container.item.orgNodeNamesString}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.role}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="7">
                <ctb:tablePager dataSource="actionForm.userPageRequested" summary="request.userPagerSummary" objectLabel="${bundle.oas['object.users']}" foundLabel="Found" id="userSearchResult" anchorName="userSearchResult"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    
</table>
