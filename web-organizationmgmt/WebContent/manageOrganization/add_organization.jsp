<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-compat-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template_add_edit_organization.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['addorg.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.addOrganization']}"/>
<netui-template:section name="bodySection">


<%            
    List selectedOrgNodes = (List)request.getAttribute("selectedOrgNodes"); 
    List orgNodesForSelector = (List)request.getAttribute("orgNodesForSelector");             
    String userAgent = request.getHeader("User-Agent");
%>
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Enter information about the organization in the form below. Required fields are marked by a blue asterisk *."/>
    <br/> 
    <br/> 
    <netui:content value='Use the organization selector on the right to select the "Parent" organization to which you are adding this new member organization.'/>
</p>



<!-- start form -->
<netui:form action="addOrganization">
<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.selectedOrgName}" />

<netui:hidden dataSource="actionForm.actionElement"/> 
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.previousParentId"/>
<netui:hidden dataSource="actionForm.previousParentName"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/>
<netui:hidden dataSource="actionForm.orgMaxPage"/> 
<netui:hidden dataSource="actionForm.previousAction"/>
<netui:hidden dataSource="actionForm.tempOrgChildNodeId"/>


<jsp:include page="/manageOrganization/show_message.jsp" />

<!-- buttons -->
<p>
    <netui:button type="submit" value="Save" action="saveOrganization"/>
    <netui:button type="submit" value="Cancel" action="returnToPreviousAction"/>
</p>

<p>
<table class="simple">
    <tr class="transparent">
        
        
<!-- User Information -->
<td class="simple" width="280" valign="top">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="100"><span class="asterisk">*</span>&nbsp;<netui:content value="Name:"/></td>
        <td class="transparent">
            <netui:textBox tagId="orgName" dataSource="actionForm.selectedOrgName" maxlength="50" style="width:180px" tabindex="1"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="100"><netui:content value="Org Code:"/></td>
        <td class="transparent">
            <netui:textBox dataSource="actionForm.selectedOrgNodeCode" maxlength="32" style="width:180px" tabindex="2"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="100"><span class="asterisk">*</span>&nbsp;<netui:content value="Layer:"/></td>
        <td class="transparent">
                <netui:select dataSource="actionForm.selectedOrgNodeTypeId" optionsDataSource="${pageFlow.orgLevelOptions}" defaultValue="${actionForm.selectedOrgNodeTypeId}" size="1" style="width:180px" tabindex="3"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent-top alignRight" width="100" nowrap><span class="asterisk">*</span>&nbsp;<netui:content value="Parent Org:"/></td>        
            <td class="transparent">
                <ctbweb:selectedOrganizations selectedOrgNodes="<%=selectedOrgNodes%>" orgNodesForSelector="<%=orgNodesForSelector%>" userAgent="<%=userAgent%>" />
            </td>      
    </tr>    
</table>
</td>


    
<!-- OrgNode PathList -->
<td class="transparent-control" width="*">
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
            <th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}"/>&nbsp;</th>                
            <th class="sortable alignLeft" width="100%" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter">
            <netui-data:getData resultId="isSelectable" value="${container.item.selectable}"/>
        
            <c:if test="${isSelectable == 'false'}">                            
                <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId" disabled="true">
                    &nbsp;<netui:radioButtonOption value="${container.item.id}">&nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>
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
                <c:if test="${orgNodeName == 'CTB'}">
                 <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId" disabled="true">
                    &nbsp;<netui:radioButtonOption value="${container.item.id}"> &nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>                
              
                </c:if>
                <c:if test="${orgNodeName != 'CTB'}">
                <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId">
                    &nbsp;<netui:radioButtonOption value="${container.item.id}" onClick="updateOrgNodeSelection(this); setElementValueAndSubmitWithAnchor('{actionForm.actionElement}', '${actionForm.actionElement}', 'userSearchResult');">&nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>
                </c:if>                
            </c:if>
        </td>        
        <td class="sortable alignLeft">     
            <ctb:tablePathEntry srcLabelDataSource="{container.item.name}" srcValueDataSource="{container.item.id}" dstLabelDataSource="{actionForm.orgNodeName}" dstValueDataSource="{actionForm.orgNodeId}" shownAsLink="{container.item.hasChildren}"/>
       
         
            
          </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="7">
                <ctb:tablePager dataSource="actionForm.orgPageRequested" summary="request.orgPagerSummary" objectLabel="${bundle.oas['object.organizations']}" id="tablePathListAnchor" anchorName="tablePathListAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>

</table>


</td>
</tr>


</table>
</p>

<!-- buttons -->
<p>
    <netui:button type="submit" value="Save" action="saveOrganization"/>
    <netui:button type="submit" value="Cancel" action="returnToPreviousAction"/>
</p>

</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
