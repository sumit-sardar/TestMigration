<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="c.tld" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['showrestrictedstudents.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.scheduleTestSessionSelectTest']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<netui-data:getData resultId="action" value="${pageFlow.action}"/>

<c:if test="${action=='edit'}">    
    <h1><netui:span value="${bundle.web['selecttest.title.edit']}"/></h1>
</c:if>
<c:if test="${action!='edit'}">    
    <h1><netui:span value="${bundle.web['selecttest.title.schedule']}"/></h1>
</c:if>

<p><netui:content value="${bundle.web['showrestrictedstudents.message1']}"/></p>
<p><netui:content value="${requestScope.restrictedCount} of ${requestScope.totalCount} ${bundle.web['showrestrictedstudents.message2']}"/></p>
<p><netui:content value="${bundle.web['showrestrictedstudents.message3']}"/></p>

<netui:form action="showRestrictedStudents">

<netui:hidden dataSource="actionForm.actionElement"/>
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.testAdmin.testName"/>
<netui:hidden dataSource="actionForm.testAdmin.level"/>
<netui:hidden dataSource="actionForm.testAdmin.sessionName"/>   
<netui:hidden dataSource="actionForm.testAdmin.accessCode"/>
<netui:hidden dataSource="actionForm.testAdmin.location"/>   
<netui:hidden dataSource="actionForm.testAdmin.timeZone"/>   
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

<netui:hidden dataSource="actionForm.selectedProductName"/>
<netui:hidden dataSource="actionForm.selectedLevel"/>
<netui:hidden dataSource="actionForm.selectedTestId"/>
<netui:hidden dataSource="actionForm.testStatePathList.pageRequested"/>
<netui:hidden dataSource="actionForm.testStatePathList.sortColumn"/>
<netui:hidden dataSource="actionForm.testStatePathList.sortOrderBy"/>
<netui:hidden dataSource="actionForm.hasBreak"/>

<!--Change For License LM2-->
<netui:hidden dataSource="actionForm.licensePercentage"/>
<netui:hidden dataSource="actionForm.testAdmin.productId"/>

<netui:hidden dataSource="actionForm.studentStatePathList.maxPageRequested"/>

<c:if test="${errorMessage!=null}">
<ctb:message title="" style="errorMessage">
    <netui:content value="${requestScope.errorMessage}"/>
</ctb:message>
</c:if>        
<c:if test="${alertMessage!=null}">
<ctb:message title="" style="alertMessage">
    <netui:content value="${requestScope.alertMessage}"/>
</ctb:message>
</c:if>        
<c:if test="${informationMessage!=null}">
<ctb:message title="" style="informationMessage">
    <netui:content value="${requestScope.informationMessage}"/>
</ctb:message>
</c:if>        

<p>
<h4><netui:span value="${bundle.web['showrestrictedstudents.studetns.title']}"/></h4>
</p>

<table class="sortable">

<netui-data:repeater dataSource="requestScope.studentNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="{actionForm.studentStatePathList.sortColumn}" orderByDataSource="{actionForm.studentStatePathList.sortOrderBy}" anchorName="studentTableAnchor">
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="LastName"><netui:span value="Last Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="FirstName"><netui:span value="First Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="MiddleName"><netui:span value="M.I"/></ctb:tableSortColumn></th>
            <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="Grade"><netui:span value="Grade"/></ctb:tableSortColumn></th>
            <th class="sortable alignLeft" nowrap style="padding: 5px;"><netui:span value="Test Session Name"/></th>
            <th class="sortable alignCenter" nowrap><netui:span value="Start Date"/></th>
            <th class="sortable alignCenter" nowrap><netui:span value="End Date"/></th>
            
        </ctb:tableSortColumnGroup>
    </tr>
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    <tr class="sortable">
        <td class="sortable alignLeft">     
            <netui:span value="${container.item.lastName}"/>
        </td>
        <td class="sortable alignLeft">     
            <netui:span value="${container.item.firstName}"/>
        </td>
        <td class="sortable alignCenter">     
            <netui:span value="${container.item.middleName}"/>
        </td>
        <td class="sortable alignCenter">
            <netui:span value="${container.item.grade}"/>
        </td>
        <td class="sortable alignLeft">
            <netui:span value="${container.item.status.priorSession.testAdminName}"/>
        </td>
        <td class="sortable alignCenter">
            <netui:span value="${container.item.status.priorSession.loginStartDate}"><netui:formatDate pattern="MM/dd/yy"/></netui:span>
        </td>
        <td class="sortable alignCenter">
            <netui:span value="${container.item.status.priorSession.loginEndDate}"><netui:formatDate pattern="MM/dd/yy"/></netui:span>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="7">
                <ctb:tablePager dataSource="actionForm.studentStatePathList.pageRequested" summary="request.studentPagerSummary" objectLabel="Students" anchorName="studentTableAnchor" id="studentTableAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>

</table>

      
<hr/>

<netui:button styleClass="button" type="submit" value="${bundle.web['common.button.ok']}" action="removeRestrictedStudents"/>

<netui:button styleClass="button" type="submit" value="${bundle.web['common.button.cancel']}" action="selectTest"/>
          

</netui:form>
        
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
