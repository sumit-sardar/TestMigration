<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%--<%@ taglib uri="c.tld" prefix="c"%>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['reviewassignment.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.scheduleTestSelectStudents.ResolveMultipleAssignments']}"/>
<netui-template:section name="bodySection">


<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
      

<netui:form action="reviewAssignment" onSubmit="this.action+='#studentTableAnchor'; return true;">

<netui:hidden dataSource="actionForm.actionElement"/>
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/>
<netui:hidden dataSource="actionForm.testAdmin.testName"/>
<netui:hidden dataSource="actionForm.testAdmin.sessionName"/>
<netui:hidden dataSource="actionForm.testAdmin.level"/>   
<netui:hidden dataSource="actionForm.testAdmin.accessCode"/>   
<netui:hidden dataSource="actionForm.testAdmin.timeZone"/>   
<netui:hidden dataSource="actionForm.testAdmin.location"/>   
<netui:hidden dataSource="actionForm.selectedProductName"/>        
<netui:hidden dataSource="actionForm.selectedLevel"/>        
<netui:hidden dataSource="actionForm.hasBreak"/>   
<netui:hidden dataSource="actionForm.selectedTestId"/>   

<netui:hidden dataSource="actionForm.startDate"/>   
<netui:hidden dataSource="actionForm.endDate"/>   
<netui:hidden dataSource="actionForm.startTime"/>   
<netui:hidden dataSource="actionForm.endTime"/>   
<netui:hidden dataSource="actionForm.formOperand"/>  
<netui:hidden dataSource="actionForm.formAssigned"/>  
<netui:hidden dataSource="actionForm.action"/> 
<netui:hidden dataSource="actionForm.creatorOrgNodeId"/>  
<netui:hidden dataSource="actionForm.creatorOrgNodeName"/>   

<netui:hidden dataSource="actionForm.selectedOrgNodeId"/>

<netui:hidden dataSource="actionForm.orgNodeName"/>
<netui:hidden dataSource="actionForm.orgNodeId"/>
<netui:hidden dataSource="actionForm.accommodationOperand"/>
<netui:hidden dataSource="actionForm.orgStatePathList.pageRequested"/>
<netui:hidden dataSource="actionForm.orgStatePathList.sortColumn"/>
<netui:hidden dataSource="actionForm.orgStatePathList.sortOrderBy"/>

<netui:hidden dataSource="actionForm.accommodationOperand"/>
<netui:hidden dataSource="actionForm.selectedGrade"/>
<netui:hidden dataSource="actionForm.selectedAccommodationElements"/>
<netui:hidden dataSource="actionForm.isCopyTest"/>
<netui:hidden dataSource="actionForm.filterVisible"/>

<netui:hidden dataSource="actionForm.autoLocator"/>
<netui:hidden dataSource="actionForm.licensePercentage"/>
<netui:hidden dataSource="actionForm.testAdmin.productId"/>

<input type="hidden" id="validateTest" name="validateTest" value="noValidateTest" />

<!-- ********************************************************************************************************************* -->
<!-- Change Assignment -->
<!-- ********************************************************************************************************************* -->
<ctb:switch dataSource="{actionForm.action}">
<ctb:case value="schedule">
    <c:if test="${isCopyTest}">        
        <h1><netui:span value="${bundle.web['selectstudents.title.copy']}"/></h1>
    </c:if>
    <c:if test="${! isCopyTest}">        
        <h1><netui:span value="${bundle.web['selectstudents.title.schedule']}"/></h1>
    </c:if>
</ctb:case>    
<ctb:case value="edit">
    <h1><netui:span value="${bundle.web['selectstudents.title.edit']}"/></h1>
</ctb:case>    
<ctb:case value="view">
    <h1><netui:span value="${bundle.web['selectstudents.title.view']}"/></h1>
</ctb:case>    
</ctb:switch>   

<br/><h2><netui:span value="${bundle.web['reviewassignment.changeassignment.title']}"/></h2>
<p><netui:content value="${bundle.web['reviewassignment.changeassignment.introtext1']}"/></p>
<p><netui:content value="${bundle.web['reviewassignment.changeassignment.introtext2']}"/></p>

<p>
<table class="transparent">
    <tr class="transparent">
        <td class="transparent"><netui:span value="${bundle.web['reviewassignment.totalStudentSelected']}"/></td>
        <td class="transparent"><div class="formValue"><netui:span value="${requestScope.totalStudentSelected}" styleClass="formValue"/></div></td>
    </tr>
    <tr class="transparent">
        <td class="transparent"><netui:span value="${bundle.web['reviewassignment.totalStudentDuplicated']}"/></td>
        <td class="transparent"><div class="formValue"><netui:span value="${requestScope.totalStudentDuplicated}" styleClass="formValue"/></div></td>
    </tr>
</table>
</p>

<a name="tableAnchor"><!-- tableAnchor --></a>    
<table class="sortable">

<netui-data:repeater dataSource="pageFlow.pageDuplicateStudentList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.studentStatePathList.sortColumn" orderByDataSource="actionForm.studentStatePathList.sortOrderBy" anchorName="tableAnchor">
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="LastName"><netui:span value="Last Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="FirstName"><netui:span value="First Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignCenter" width="10%" nowrap><ctb:tableSortColumn value="MiddleName"><netui:span value="M.I"/></ctb:tableSortColumn></th>            
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:span value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    <tr class="sortable">
        <td class="sortable alignLeft"><netui:span value="${container.item.lastName}"/></td>
        <td class="sortable alignLeft"><netui:span value="${container.item.firstName}"/></td>
        <td class="sortable alignCenter"><netui:span value="${container.item.middleName}"/></td>
        <td class="sortable alignLeft">
            <netui:select optionsDataSource="${container.item.orgNodeNames}" dataSource="container.item.selectedOrgNodeName" style="width: 280px" size="1" multiple="false"/>
        </td>
    </tr>    
    </netui-data:repeaterItem>
    
    <netui-data:repeaterFooter>    
        <tr class="sortable">
            <td class="sortableControls" colspan="5">
                <ctb:tablePager dataSource="actionForm.studentStatePathList.pageRequested" summary="request.studentPagerSummary" objectLabel="Students" anchorName="tableAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    <ctb:tableNoResults dataSource="pageFlow.dupStudentList">
        <tr class="sortable">
            <td class="sortable" colspan="5">
                <ctb:message title="{bundle.web['common.message.title.noStudents']}" style="tableMessage">
                    <netui:content value="${bundle.web['selectstudents.students.messageInfo']}"/>
                </ctb:message>
            </td>
        </tr>
    </ctb:tableNoResults>              
</table>


<br><p>
<netui:button type="submit" value="${bundle.web['common.button.apply']}" action="applyAssignment"/>
<netui:button type="submit" value="${bundle.web['common.button.cancel']}" action="cancelAssignment" onClick="return verifyCancelResolveAssignments();"/>
</p>
</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
