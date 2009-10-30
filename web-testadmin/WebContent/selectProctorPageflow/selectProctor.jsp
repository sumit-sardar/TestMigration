<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--<%@ taglib uri="c.tld" prefix="c"%> -->
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<!-- Change for MQC defect  55837 -->
<%String templatePage = "/resources/jsp/template.jsp";%>
<ctb:switch dataSource="{pageFlow.action}">
    <ctb:case value="edit">
        <% templatePage="/resources/jsp/editTemplate.jsp";%>
    </ctb:case>
    <ctb:case value="view">
        <% templatePage="/resources/jsp/viewTemplate.jsp";%>
    </ctb:case>
</ctb:switch> 

<netui-template:template templatePage="<%=templatePage%>">
<netui-template:setAttribute name="title" value="${bundle.web['selectproctors.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.scheduleTestSessionSelectProctors']}"/>
<netui-template:section name="bodySection">

<!--End of change for MQC defect  55837 -->

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<netui:form action="selectProctor" onSubmit="this.action = addAnchor(this.action, 'proctorTableAnchor'); return true;">
<ctb:switch dataSource="{actionForm.action}">
<ctb:case value="schedule">
    <h1><netui:span value="${bundle.web['selectproctors.title.schedule']}"/></h1>
</ctb:case>    
<ctb:case value="edit">
    <h1><netui:span value="${bundle.web['selectproctors.title.edit']}"/></h1>
</ctb:case>    
<ctb:case value="view">
    <h1><netui:span value="${bundle.web['selectproctors.title.view']}"/></h1>
</ctb:case>    
</ctb:switch>   

<p>
<netui:content value="${bundle.web['selectproctors.selectProctors.introtext']}"/>
</p>
        

<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/>
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/>
<netui:hidden dataSource="actionForm.selectedProductName"/>        
<netui:hidden dataSource="actionForm.selectedLevel"/>        
<netui:hidden dataSource="actionForm.selectedTestId"/>  

<netui:hidden dataSource="actionForm.hasBreak"/>   
<netui:hidden dataSource="actionForm.testAdmin.accessCode"/>  
<netui:hidden dataSource="actionForm.testAdmin.isRandomize"/>   <!--added for randomize distractor-->
<netui:hidden dataSource="actionForm.testAdmin.testName"/>
<netui:hidden dataSource="actionForm.testAdmin.level"/>
<netui:hidden dataSource="actionForm.testAdmin.sessionName"/>   
<netui:hidden dataSource="actionForm.testAdmin.timeZone"/>   
<netui:hidden dataSource="actionForm.testAdmin.location"/>   
<netui:hidden dataSource="actionForm.startDate"/>   
<netui:hidden dataSource="actionForm.endDate"/>   
<netui:hidden dataSource="actionForm.startTime"/>   
<netui:hidden dataSource="actionForm.endTime"/>   
<netui:hidden dataSource="actionForm.formOperand"/>  
<netui:hidden dataSource="actionForm.formAssigned"/> 
<netui:hidden dataSource="actionForm.action"/>  
<netui:hidden dataSource="actionForm.creatorOrgNodeId"/>  
<netui:hidden dataSource="actionForm.creatorOrgNodeName"/>   

<netui:hidden dataSource="actionForm.accommodationOperand"/>
<netui:hidden dataSource="actionForm.selectedGrade"/>
<netui:hidden dataSource="actionForm.selectedAccommodationElements"/>
<netui:hidden dataSource="actionForm.filterVisible"/>

<netui:hidden dataSource="actionForm.orgStatePathList.maxPageRequested"/>
<netui:hidden dataSource="actionForm.proctorStatePathList.maxPageRequested"/>

<netui:hidden dataSource="actionForm.studentStatePathList.sortColumn"/>   
<netui:hidden dataSource="actionForm.studentStatePathList.sortOrderBy"/>   
<netui:hidden dataSource="actionForm.studentStatePathList.pageRequested"/>
<netui:hidden dataSource="actionForm.studentStatePathList.maxPageRequested"/>

<!--added for License managment LM2-->
<netui:hidden dataSource="actionForm.licensePercentage"/>
<netui:hidden dataSource="actionForm.testAdmin.productId"/>

<netui:hidden dataSource="actionForm.autoLocator"/>

<table class="transparent">
    <tr class="transparent">
        <td class="transparent"><netui:span value="${bundle.web['selectproctors.selectProctors.fieldNoEdit.test']}"/></td>
        <td class="transparent"><div class="formValue"><netui:span value="${actionForm.testAdmin.testName}" styleClass="formValue"/></div></td>
        <td class="transparent"><netui:span value="${bundle.web['selectproctors.selectProctors.fieldNoEdit.testSessionName']}"/></td>
        <td class="transparent"><div class="formValue"><netui:span value="${actionForm.testAdmin.sessionName}" styleClass="formValue"/></div></td>
    </tr>
    <tr class="transparent">
        <td class="transparent"><netui:span value="${bundle.web['selectproctors.selectProctors.fieldNoEdit.assignedProctors']}"/></td>
        <td class="transparent"><div class="formValue"><netui:span value="${actionForm.selectedProctorCount}" styleClass="formValue"/></div></td>
        <td class="transparent"><netui:span value="${bundle.web['selectproctors.selectProctors.fieldNoEdit.testScheduler']}"/></td>
        <td class="transparent"><div class="formValue"><netui:span value="${sessionScope.schedulerFirstLastName}" styleClass="formValue"/></div></td>
    </tr>
</table>

<br/>

<h4><netui:span value="${bundle.web['selectproctors.organizations.title']}"/></h4>
<p>
<netui:content value="${bundle.web['selectproctors.organizations.introtext']}"/>
</p>

<a name="pathListAnchor"><!-- pathListAnchor --></a>    
<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="3">
            <ctb:tablePathList valueDataSource="actionForm.orgNodeId" labelDataSource="actionForm.orgNodeName" pathList="request.orgNodePath" />
        </td>
    </tr>
    
<netui-data:repeater dataSource="requestScope.orgNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="{actionForm.orgStatePathList.sortColumn}" orderByDataSource="{actionForm.orgStatePathList.sortOrderBy}" anchorName="pathListAnchor">
            <th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
            <th class="sortable alignLeft" width="65%" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
            <th class="sortable alignRight" width="*" nowrap><ctb:tableSortColumn value="UserCount"><netui:content value="Available Proctors At & Below"/></ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter">
            <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId">
                &nbsp;<netui:radioButtonOption value="${container.item.id}" onClick="setElementValueAndSubmit('actionElement', 'actionElement');">&nbsp;</netui:radioButtonOption>                
            </netui:radioButtonGroup>
        </td>        
        <td class="sortable alignLeft">     
            <ctb:switch dataSource="{container.item.hasChildren}">
                <ctb:case value="true"><ctb:tablePathEntry srcLabelDataSource="${container.item.name}" srcValueDataSource="${container.item.id}" dstLabelDataSource="actionForm.orgNodeName" dstValueDataSource="actionForm.orgNodeId" /></ctb:case>
                <ctb:case value="false"><netui:span value="${container.item.name}"/></ctb:case>
            </ctb:switch>                
        </td>
        <td class="sortable alignRight">
            <netui:span value="${container.item.filteredCount}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="4">
                <ctb:tablePager dataSource="actionForm.orgStatePathList.pageRequested" summary="request.orgPagerSummary" objectLabel="${bundle.oas['object.organizations']}" anchorName="pathListAnchor" id="pathListAnchor" />
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    <ctb:tableNoResults dataSource="request.orgNodes">
        <tr class="sortable">
            <td class="sortable" colspan="4">
                <ctb:message title="{bundle.web['common.message.title.noOrganizations']}" style="tableMessage">
                    <netui:content value="${bundle.web['selectproctors.organizations.messageInfo']}"/>
                </ctb:message>
            </td>
        </tr>
    </ctb:tableNoResults>

              
</table>


<br/>
<br/>
<c:if test="${nodeContainsProctors}">        


<h4><netui:span value="${actionForm.selectedOrgNodeName} ${bundle.web['selectproctors.proctors.title']}"/></h4>
<p>
<netui:content value="${bundle.web['selectproctors.proctors.introtext']}"/>
</p>

<a name="proctorTableAnchor"><!-- proctorTableAnchor --></a>    
<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="3">
            <netui:button type="submit" value="Add All Proctors" onClick="setElementValue('currentAction', 'addAll');"/>              
            <netui:button type="button" value="Update Total" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'updateTotal', 'topPage');"/>              
        </td>
    </tr>
    
<netui-data:repeater dataSource="requestScope.proctorNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="{actionForm.proctorStatePathList.sortColumn}" orderByDataSource="{actionForm.proctorStatePathList.sortOrderBy}" anchorName="proctorTableAnchor">
            <th class="sortable alignCenter" nowrap><netui:span value="${bundle.web['common.column.select']}"/></th>                
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="LastName"><netui:span value="Last Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="FirstName"><netui:span value="First Name"/></ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    <tr class="sortable">
        <td class="sortable alignCenter">
            <ctb:switch dataSource="{container.item.editable}">
                <ctb:case value="T">
                    <netui:checkBoxGroup dataSource="actionForm.selectedProctorIds">
                        &nbsp;<netui:checkBoxOption value="${container.item.userId}">&nbsp;</netui:checkBoxOption>                
                    </netui:checkBoxGroup>
                </ctb:case>
                <ctb:case value="F">
                    <netui:checkBoxGroup dataSource="actionForm.selectedProctorIds" disabled="true">
                        &nbsp;<netui:checkBoxOption value="${container.item.userId}">&nbsp;</netui:checkBoxOption>                
                    </netui:checkBoxGroup>
                </ctb:case>
            </ctb:switch>                        
        </td>        
        <td class="sortable alignLeft">     
            <netui:span value="${container.item.lastName}"/>
        </td>
        <td class="sortable alignLeft">     
            <netui:span value="${container.item.firstName}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="10">
                <ctb:tablePager dataSource="actionForm.proctorStatePathList.pageRequested" summary="request.proctorPagerSummary" objectLabel="Proctors" id="proctorTableAnchor" anchorName="proctorTableAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    <ctb:tableNoResults dataSource="request.proctorNodes">
        <tr class="sortable">
            <td class="sortable" colspan="10">
                <ctb:message title="{bundle.web['common.message.title.noProctors']}" style="tableMessage">
                    <netui:content value="${bundle.web['selectproctors.proctors.messageInfo']}"/>
                </ctb:message>
            </td>
        </tr>
    </ctb:tableNoResults>
              
</table>

</c:if>
                  
<p><br>
<netui:button type="submit" value="${bundle.web['common.button.ok']}" action="selectProctorDone"/>
<netui:button type="submit" value="${bundle.web['common.button.cancel']}" action="selectProctorCancel" onClick="return verifyCancelAddProctors();"/>
</p>
</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
