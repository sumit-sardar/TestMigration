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

<netui-template:template templatePage="/resources/jsp/template_manage_license.jsp">

<netui-template:setAttribute name="title" value="${bundle.web['license.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.NodeLicense']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<!-- start form -->
<netui:form action="manageLicense">




<h1>
    <netui:content value="${bundle.web['license.title']}"/>
</h1>
<p>
    <netui:content value="${bundle.web['license.title.message']}"/><br/>
</p>


<c:if test="${multipleProducts}">     
<p>
<table class="transparent">
<tr class="transparent">
    <td class="transparent"><b>Product:</b></td>
    <td class="transparent">
        <netui:select dataSource="pageFlow.productName" optionsDataSource="${pageFlow.productNameOptions}" size="1" multiple="false" onChange="verifyChangeProduct(this, '{actionForm.currentAction}', 'changeProduct');">
        </netui:select>
    </td>
</tr>
</table>
</p>
</c:if> 

<p>
<table class="transparent">
<tr class="transparent"><td class="transparent" width="100"><b>License Model:</b></td><td class="transparent"><netui:span value="${actionForm.parentLicenseNode.subtestModel}" /></td></tr>
<tr class="transparent"><td class="transparent" width="100"><b>Scheduled:</b></td><td class="transparent"><netui:span value="${actionForm.parentLicenseNode.reserved}" /></td></tr>
<tr class="transparent"><td class="transparent" width="100"><b>Consumed:</b></td><td class="transparent"><netui:span value="${actionForm.parentLicenseNode.consumed}" /></td></tr>
<tr class="transparent"><td class="transparent" width="100"><b>Available:</b></td><td class="transparent"><netui:span tagId="availId" value="${actionForm.parentLicenseNode.available}" /></td></tr>
</table>
</p>


<table class="sortable">

    
    <tr class="sortable">
        <td class="sortableControls">

<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="4" height="30">
            <ctb:tablePathList valueDataSource="actionForm.orgNodeId" labelDataSource="actionForm.orgNodeName" pathList="request.orgNodePath" /> 
        </td>
    </tr>

<netui-data:repeater dataSource="requestScope.orgNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.orgSortColumn" orderByDataSource="actionForm.orgSortOrderBy" >
            <th class="sortable alignLeft" width="64%" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
            <th class="sortable alignCenter" width="12%" nowrap>Scheduled</th>
            <th class="sortable alignCenter" width="12%" nowrap>Consumed</th>
            <th class="sortable alignCenter" width="12%" nowrap>Available</th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignLeft">  
            <ctb:tablePathEntry srcLabelDataSource="${container.item.name}" srcValueDataSource="${container.item.id}" dstLabelDataSource="actionForm.orgNodeName" dstValueDataSource="actionForm.orgNodeId" shownAsLink="${container.item.hasChildren}"/>
            <netui-data:getData resultId="orgNodeName" value="${container.item.name}"/>
        </td>
        <td class="sortable alignCenter">
             <netui:span value="${container.item.reserved}"/>
        </td>
        <td class="sortable alignCenter">
             <netui:span value="${container.item.consumed}"/>
        </td>
        <td class="sortable alignCenter">
       		<netui:span value="${container.item.available}"/>        
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>

        <tr class="sortable">
            <td class="sortableControls" colspan="4">
                <ctb:tablePager dataSource="actionForm.orgPageRequested" summary="request.orgPagerSummary" objectLabel="${bundle.oas['object.organizations']}" id="tablePathListAnchor" anchorName="tablePathListAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    
</table>

        </td>
    </tr>
</table>

<p>
<br/>
	<netui:button type="submit" value="${bundle.web['common.button.back']}" action="goToSystemAdministration"/>&nbsp;	             
</p>
 

</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
