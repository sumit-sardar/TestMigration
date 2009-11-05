<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="dto.PathNode"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<%            
    List selectedOrgNodes = (List)request.getAttribute("selectedOrgNodes"); 
    List orgNodesForSelector = (List)request.getAttribute("orgNodesForSelector");             
    String userAgent = request.getHeader("User-Agent");
%>


<table class="simple">
    <tr class="transparent">
        
        
<!-- User Information -->
<td class="simple" width="310" valign="top">


<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="First Name:"/></td>
        <td class="transparent">
            <netui:textBox tagId="userFirstName" dataSource="actionForm.userProfile.firstName" maxlength="32" style="width:200px" tabindex="0"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Middle Name:"/></td>
        <td class="transparent">
            <netui:textBox dataSource="actionForm.userProfile.middleName" maxlength="32" style="width:200px"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="Last Name:"/></td>
        <td class="transparent">
            <netui:textBox tagId="userLastName" dataSource="actionForm.userProfile.lastName" maxlength="32" style="width:200px"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Email:"/></td>
        <td class="transparent">
            <netui:textBox tagId="userEmail" dataSource="actionForm.userProfile.email" maxlength="64" style="width:200px"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="Time Zone:"/></td>
        <td class="transparent">
            <netui:select optionsDataSource="${pageFlow.timeZoneOptions}" dataSource="actionForm.userProfile.timeZone" size="1" style="width:200px" defaultValue="${actionForm.userProfile.timeZone}"/>
        </td>                                
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="Role:"/></td>
        <c:if test="${ isAddAdministrator }">   
             <td class="transparent">
               <netui:label value="${userProfileData.role}"/>
               <netui:hidden dataSource="actionForm.userProfile.roleId"/>               
            </td> 
        </c:if>
        <c:if test="${ ! isAddAdministrator }">
            <td class="transparent">
                <netui:select optionsDataSource="${pageFlow.roleOptions}" dataSource="actionForm.userProfile.roleId" size="1" style="width:200px" defaultValue="${actionForm.userProfile.roleId}"/>
            </td> 
        </c:if>                                   
    </tr>
    <!--ext_pin1 is added for DEX CR-->
     <tr class="transparent">
        <td class="transparent alignRight" nowrap width="110"><netui:content value="External User Id:"/></td>
        <td class="transparent">
            <netui:textBox tagId="userExternalId" dataSource="actionForm.userProfile.extPin1" maxlength="20" style="width:200px"/>
        </td>
    </tr> 
    <tr class="transparent">
        <td class="transparent-top alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="Organization:"/></td>
        <td class="transparent-top">        
             <c:if test="${ isAddAdministrator }">   
                <%                 
                    PathNode node = (PathNode)selectedOrgNodes.get(0);
                    String orgNodeName = node.getName();
                %>    
                <%=orgNodeName%>
            </c:if>   


            <c:if test="${ ! isAddAdministrator }">                
                <ctbweb:selectedOrganizations selectedOrgNodes="<%=selectedOrgNodes%>" orgNodesForSelector="<%=orgNodesForSelector%>" userAgent="<%=userAgent%>" />
            </c:if>    
        </td>
    </tr>
    
</table>
</td>


    
<!-- OrgNode PathList -->
<td class="transparent-control" width="*">

<c:if test="${ ! isAddAdministrator }">
<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="2">
            <ctb:tablePathList valueDataSource="actionForm.orgNodeId" labelDataSource="actionForm.orgNodeName" pathList="request.orgNodePath" />
        </td>
    </tr>

<netui-data:repeater dataSource="requestScope.orgNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.orgSortColumn" orderByDataSource="actionForm.orgSortOrderBy" >
            <th class="sortable alignCenter" width="5%">&nbsp;<netui:content value="${bundle.web['common.column.select']}"/>&nbsp;</th>                
            <th class="sortable alignLeft" width="*"><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter" >
            <netui-data:getData resultId="isSelectable" value="${container.item.selectable}"/>
                
            
            <c:if test="${isSelectable == 'false'}">                            
                <netui:checkBoxGroup dataSource="pageFlow.currentOrgNodeIds" disabled="true">
                    &nbsp;<netui:checkBoxOption value="${container.item.id}">&nbsp;</netui:checkBoxOption>                
                </netui:checkBoxGroup>
            </c:if>
            <c:if test="${isSelectable == 'true'}">   
            
                <netui-data:getData resultId="orgNodeName" value="${container.item.name}"/>
                <netui-data:getData resultId="orgNodeId" value="${container.item.id}"/>
                <% 
                    String orgNodeName = (String)pageContext.getAttribute("orgNodeName"); 
                    Integer orgNodeId = (Integer)pageContext.getAttribute("orgNodeId"); 
                    String orgNodeIdStr = orgNodeId.toString(); 
                %> 
                <input type="hidden" id="<%=orgNodeId%>" name="<%=orgNodeId%>" value="<%=orgNodeName%>">
                <netui:checkBoxGroup dataSource="pageFlow.currentOrgNodeIds">
                    &nbsp;<netui:checkBoxOption value="${container.item.id}" onClick="updateOrgNodeSelection(this);">&nbsp;</netui:checkBoxOption>                
                </netui:checkBoxGroup>
                
            </c:if>
        </td>        
        <td class="sortable alignLeft" >     
            <ctb:tablePathEntry srcLabelDataSource="${container.item.name}" srcValueDataSource="${container.item.id}" dstLabelDataSource="actionForm.orgNodeName" dstValueDataSource="actionForm.orgNodeId" shownAsLink="${container.item.hasChildren}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="2">
                <ctb:tablePager dataSource="actionForm.orgPageRequested" summary="request.orgPagerSummary" objectLabel="${bundle.oas['object.organizations']}" id="tablePathListAnchor" anchorName="tablePathListAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>

</table>
</c:if>
&nbsp;

</td>
</tr>


</table>

<br/>
