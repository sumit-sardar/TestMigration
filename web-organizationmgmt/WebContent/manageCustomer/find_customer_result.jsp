<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>



<table class="sortable">

    <tr class="sortable">
      <%--  <td class="sortableControls" colspan="12" height="30" >&nbsp;
            <netui:button tagId="beginEditCustomer" type="submit" value="Edit Profile" onClick="setElementValue('{actionForm.currentAction}', 'editCustomer');" disabled="{request.disableEditProfileButton}"/>
            <netui:button tagId="beginEditFramework" type="submit" value="Edit Framework" onClick="setElementValue('{actionForm.currentAction}', 'beginEditFramework');" disabled="{request.disableEditFrameworkButton}"/>
            <netui:button tagId="createAdministrator" type="submit" value="Create Administrator" onClick="setElementValue('{actionForm.currentAction}', 'createAdministrator');"  disabled="{request.disableCreateAdministratorButton}" />
            <netui:button  tagId="manageOrganization" type="submit" value="Manage Organization" onClick="setElementValue('{actionForm.currentAction}', 'manageOrganization');" disabled="{request.disableManageOrganizationButton}"/>
        </td>--%>
        <td class="sortableControls" colspan="12" height="30">&nbsp;
            <netui:button type="submit" value="View Customer" action="beginViewCustomer" tagId="view" disabled="${requestScope.disableViewProfileButton}"/>
            <netui:button type="submit" value="Edit Customer" action="beginEditCustomer" tagId="editProfile" disabled="${requestScope.disableEditProfileButton}"/>
            <netui:button type="submit" value="Edit Framework" action="beginEditFramework" tagId="editFramework" disabled="${requestScope.disableEditFrameworkButton}"/>  
            <netui:button type="submit" value="Manage Organization" action="manageOrganization" tagId="manageOrg" disabled="${requestScope.disableManageOrganizationButton}"/>
            <netui:button type="submit" value="Add Administrator" action="createAdministrator" tagId="createAdmin" disabled="${requestScope.disableCreateAdministratorButton}"/>
            <netui:button type="submit" value="Manage Licenses" action="manageLicense" tagId="manageLicense" disabled="${requestScope.disableManageLicenseButton}"/>
        </td>
    </tr>
    
    <netui-data:repeater dataSource="pageFlow.customerList">
        <netui-data:repeaterHeader>
        <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.customerSortColumn" orderByDataSource="actionForm.customerSortOrderBy" anchorName="customerSearchResult">
            <th class="sortable alignCenter">&nbsp;<netui:content value="${bundle.web['common.column.select']}"/>&nbsp;</th>                
            <th class="sortable alignLeft" width="35%" nowrap><ctb:tableSortColumn value="CustomerName">Customer Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="25%" nowrap><ctb:tableSortColumn value="ExtCustomerId">Customer ID</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StateDesc">State</ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
       <tr class="sortable">
            <td class="sortable alignCenter">
                <netui:radioButtonGroup dataSource="actionForm.selectedCustomerId">
                    &nbsp;<netui:radioButtonOption value="${container.item.id}" alt="${container.item.actionPermission}" onClick="enableButton(this.alt,'manageLicense');   return enableAllButtons(); ">&nbsp;</netui:radioButtonOption>  
                  
                </netui:radioButtonGroup>
            </td>        
            <td class="sortable">
                <netui:span value="${container.item.name}"/>
            </td>
            <td class="sortable">
                <netui:span value="${container.item.code}"/>
            </td>
            <td class="sortable">
                <netui:span value="${container.item.state}"/>
            </td>
       </tr>
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
        <tr class="sortable">
            <td class="sortableControls" colspan="7">
                <ctb:tablePager dataSource="actionForm.customerPageRequested" summary="request.customerPagerSummary" objectLabel="${bundle.oas['object.customers']}" foundLabel="Found" id="customerSearchResult" anchorName="customerSearchResult"/>
            </td>
        </tr>    
    </netui-data:repeaterFooter>
    </netui-data:repeater>
    
   </table>




