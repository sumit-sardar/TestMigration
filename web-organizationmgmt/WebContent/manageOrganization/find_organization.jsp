<%@ page import="java.io.*, java.util.*"%>
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

<netui-template:setAttribute name="title" value="${bundle.web['findorg.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findOrganization']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<!-- start form -->
<netui:form action="findOrganization">

<netui:hidden  dataSource="actionForm.actionElement"/> 
<netui:hidden  dataSource="actionForm.currentAction"/>

<netui:hidden dataSource="actionForm.orgMaxPage"/> 
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/>
<%--<netui:hidden dataSource="{actionForm.previousParentName}" />--%>



<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>

<!-- title -->
<p>
    <netui:content value="Select an organization to view, edit, delete, or add a suborganization. Click a link to display suborganizations."/><br/>
</p>


<netui-data:getData resultId="messageType" value="${pageMessage.type}"/>
<c:if test="${messageType != null}">     
<p>
    <% String style = (String)pageContext.getAttribute("messageType"); %> 
    <ctb:message title="${pageMessage.title}" style="<%= style %>" >
          <netui:content value="${pageMessage.content}"/>
    </ctb:message>
</p>
</c:if> 


<table class="sortable">

    
    <tr class="sortable">
        <td class="sortableControls">

<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="3" height="30">
            <ctb:tablePathList valueDataSource="actionForm.orgNodeId" labelDataSource="actionForm.orgNodeName" pathList="request.orgNodePath" /> 
        </td>
    </tr>

    <tr class="sortable">
        <td class="sortableControls" colspan="3" height="30" style="border-style: solid; border-width: 1px">&nbsp;
            <netui:button tagId="View" type="submit" value=" View " onClick="setElementValue('{actionForm.currentAction}', 'viewOrganization');" disabled="${requestScope.disableViewButton}"/>
            <netui:button tagId="Edit" type="submit" value=" Edit " onClick="setElementValue('{actionForm.currentAction}', 'editOrganization');" disabled="${requestScope.disableEditButton}"/>
            <netui:button tagId="Delete" type="submit" value="Delete" onClick="return verifyDeleteOrganization();" disabled="${requestScope.disableDeleteButton}"/>
            <netui:button tagId="Add" type="submit" value="Add Organization" onClick="setElementValue('{actionForm.currentAction}', 'addOrganization');" disabled="${requestScope.disableADDButton}"/>
        </td>
    </tr>

<% String orgCategoryName = (String)request.getAttribute("orgCategoryName"); 
   System.out.println("### OrgCategoryName: " + orgCategoryName);
%> 

<netui-data:repeater dataSource="requestScope.orgNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.orgSortColumn" orderByDataSource="actionForm.orgSortOrderBy" >
            <th class="sortable alignCenter" nowrap>&nbsp;<netui:content value="${bundle.web['common.column.select']}"/>&nbsp;</th>                
            <th class="sortable alignLeft" width="100%" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="<%= orgCategoryName %>"/></ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="ChildNodeCount"><netui:content value="Suborganizations"/></ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter">
            <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId">
                &nbsp;<netui:radioButtonOption value="${container.item.id}" alt="${container.item.actionPermission}" onClick="return enableButtons(this.alt);">&nbsp;</netui:radioButtonOption>                
                 
            </netui:radioButtonGroup>
             
        </td>        
        <td class="sortable alignLeft">  
              
            <ctb:tablePathEntry srcLabelDataSource="${container.item.name}" srcValueDataSource="${container.item.id}" dstLabelDataSource="actionForm.orgNodeName" dstValueDataSource="actionForm.orgNodeId" shownAsLink="${container.item.hasChildren}"/>
            <netui-data:getData resultId="orgNodeName" value="${container.item.name}"/>
           
            
        </td>
        <td class="sortable alignCenter">
             <netui:span value="${container.item.childrenNodeCount}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>

        <tr class="sortable">
            <td class="sortableControls" colspan="3">
                <ctb:tablePager dataSource="actionForm.orgPageRequested" summary="request.orgPagerSummary" objectLabel="${bundle.oas['object.organizations']}" id="tablePathListAnchor" anchorName="tablePathListAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    
</table>

        </td>
    </tr>
</table>




</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
