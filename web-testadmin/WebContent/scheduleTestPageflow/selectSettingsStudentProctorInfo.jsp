<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-compat-template"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<netui-data:getData resultId="action" value="${pageFlow.action}"/>
<netui-data:getData resultId="hasBreak" value="${actionForm.hasBreak}"/>
<netui-data:getData resultId="formOperand" value="${requestScope.formOperand}"/>  <!-- Changes for defect 61543 -->
<netui-data:getData resultId="studentCount" value="${requestScope.studentCount}"/>
<netui-data:getData resultId="showAccommodations" value="${requestScope.testRosterFilter.showAccommodations}"/>
<netui-data:getData resultId="isFormEditable" value="${requestScope.isFormEditable}"/>
<netui-data:getData resultId="displayFormList" value="${requestScope.displayFormList}"/>
<netui-data:getData resultId="hasStudentLoggedIn" value="${pageFlow.condition.hasStudentLoggedIn}"/>
<netui-data:getData resultId="testSessionExpired" value="${pageFlow.condition.testSessionExpired}"/>
<netui-data:getData resultId="showFormAssignment" value="${requestScope.showFormAssignment}"/>

<netui-data:getData resultId="isTabeProduct" value="${requestScope.isTabeProduct}"/>
<netui-data:getData resultId="productType" value="${requestScope.productType}"/>
<!--<netui:hidden dataSource="actionForm.creatorOrgNodeId"/>  --><!-- Changes for defect 60455 -->
<a name="studentTableAnchor"><!-- studentTableAnchor --></a>    
<h3><netui:span value="${bundle.web['selectsettings.students.title']}"/></h3>
<% String formOperand = (String)pageContext.getAttribute("formOperand");%>  <!-- Changes for defect 61543 -->


<c:if test="${! isTabeProduct}">                 
<p>    
<c:if test="${action=='view'}">  
    <c:if test="${showFormAssignment == 'hidden'}">  
        <netui:hidden dataSource="actionForm.formOperand"/>   
        <netui:hidden dataSource="actionForm.formAssigned"/>   
    </c:if>  
    <c:if test="${showFormAssignment == 'noneditable'}">    
        <netui:span value="${bundle.web['selectsettings.students.message1']}"/>
        <br/>
        <ctb:switch dataSource="<%=formOperand %>">
            <ctb:case value="roundrobin">
                <div class="formValue"><netui:span value="${bundle.web['selectsettings.students.form.option1']}" styleClass="formValue"/></div>
            </ctb:case>
            <ctb:case value="samesame">
                <div class="formValue"><netui:span value="${bundle.web['selectsettings.students.form.option2']}: ${actionForm.formAssigned}" styleClass="formValue"/></div>
            </ctb:case>
            <ctb:case value="manual">
                <div class="formValue"><netui:span value="${bundle.web['selectsettings.students.form.option3']}" styleClass="formValue"/></div>
            </ctb:case>
        </ctb:switch>                        
        
        <netui:hidden dataSource="actionForm.formOperand"/>   
        <netui:hidden dataSource="actionForm.formAssigned"/>   
    </c:if>    
</c:if>    
<c:if test="${action!='view'}">      
    <c:if test="${showFormAssignment == 'hidden'}">  
        <netui:hidden dataSource="actionForm.formOperand"/>   
        <netui:hidden dataSource="actionForm.formAssigned"/>   
    </c:if>  
    <c:if test="${showFormAssignment == 'noneditable'}">    
        <netui:span value="${bundle.web['selectsettings.students.message1']}"/>
        <br/>
        <ctb:switch dataSource= "<%=formOperand %>" >
             <ctb:case value="roundrobin" >
                <div class="formValue"><netui:span value="${bundle.web['selectsettings.students.form.option1']}" styleClass="formValue"/></div>
            </ctb:case>
            <ctb:case value="samesame">
                <div class="formValue"><netui:span value="${bundle.web['selectsettings.students.form.option2']}: ${actionForm.formAssigned}" styleClass="formValue"/></div>
            </ctb:case>
            <ctb:case value="manual">
                <div class="formValue"><netui:span value="${bundle.web['selectsettings.students.form.option3']}" styleClass="formValue"/></div>
            </ctb:case>
        </ctb:switch>                        
        
        <netui:hidden dataSource="actionForm.formOperand"/>   
        <netui:hidden dataSource="actionForm.formAssigned"/>   
    </c:if>  
    <c:if test="${showFormAssignment == 'noneditable_samesame'}">    
        <netui:span value="${bundle.web['selectsettings.students.message1']}"/>
        <br/>
        <ctb:switch dataSource="<%=formOperand %>">
            <ctb:case value="roundrobin">
                <div class="formValue"><netui:span value="${bundle.web['selectsettings.students.form.option1']}" styleClass="formValue"/></div>
            </ctb:case>
            <ctb:case value="samesame">
                <div class="formValue"><netui:span value="${bundle.web['selectsettings.students.form.option2']}" styleClass="formValue"/></div>
                <c:if test="${displayFormList}">                 
                     <netui:select dataSource="actionForm.formAssigned" optionsDataSource="${pageFlow.formList}" size="1" multiple="false">
                     </netui:select>
                <br/>                
                </c:if>               
                <c:if test="${!displayFormList}">                 
                    <netui:hidden dataSource="actionForm.formAssigned"/>   
                </c:if>               
            </ctb:case>
            <ctb:case value="manual">
                <div class="formValue"><netui:span value="${bundle.web['selectsettings.students.form.option3']}" styleClass="formValue"/></div>
            </ctb:case>
        </ctb:switch>                        
        
        <netui:hidden dataSource="actionForm.formOperand"/>   
    </c:if>    
    <c:if test="${showFormAssignment == 'editable'}">    
        <netui:span value="${bundle.web['selectsettings.students.message1']}"/>
        <br/>
        <netui:radioButtonGroup dataSource="actionForm.formOperand">
            <netui:radioButtonOption value="roundrobin" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'roundrobin', 'studentTableAnchor');"><netui:span value="${bundle.web['selectsettings.students.form.option1']}"/></netui:radioButtonOption>
            <br/>                 
            <netui:radioButtonOption value="samesame" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'samesame', 'studentTableAnchor');"><netui:span value="${bundle.web['selectsettings.students.form.option2']}"/></netui:radioButtonOption>
            <br/>
            <c:if test="${displayFormList}">                 
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                
                 <netui:select dataSource="actionForm.formAssigned" optionsDataSource="${pageFlow.formList}" size="1" multiple="false">
                 </netui:select>
            <br/>                
            </c:if>               
            <c:if test="${!displayFormList}">                 
                <netui:hidden dataSource="actionForm.formAssigned"/>   
            </c:if>               
            <netui:radioButtonOption value="manual" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'manual', 'studentTableAnchor');"><netui:span value="${bundle.web['selectsettings.students.form.option3']}"/></netui:radioButtonOption>
        </netui:radioButtonGroup>
    </c:if>    
</c:if>
</p>   
</c:if>    


<c:if test="${action=='view'}">    
<h4><netui:span value="${bundle.web['selectsettings.students.testsessionroster.title.view']}"/></h4>
<p>
    <c:if test="${studentCount>0}">
    <netui:content value="${bundle.web['selectsettings.students.testsessionroster.message.view']}"/>
    </c:if>
</c:if>
<c:if test="${action!='view'}">    
<h4><netui:span value="${bundle.web['selectsettings.students.testsessionroster.title']}"/></h4>
<p>
    <c:if test="${studentCount>0}">
    <netui:content value="${bundle.web['selectsettings.students.testsessionroster.message']}"/>
    </c:if>
</c:if>

<table class="transparent">
<tr class="transparent">
    <td class="transparent"><netui:span value="${bundle.web['selectsettings.students.totalSelectedStudents']}"/> </td>
    <td class="transparent"><div class="formValue"><netui:span value="${requestScope.studentCount}" styleClass="formValue"/></div></td>
<c:if test="${sessionScope.supportAccommodations}">                                    
    <td class="transparent"><netui:span value="${bundle.web['selectsettings.students.studentsWithAccommodations']}"/></td>
    <td class="transparent"><div class="formValue"><netui:span value="${requestScope.studentWithAccommodationsCount}" styleClass="formValue"/></div></td>
</c:if>    
</tr>
</table>

<netui-data:getData resultId="removeStudentMessage" value="${requestScope.removeStudentMessage}"/>

<c:if test="${removeStudentMessage!=null}">
<ctb:message title="" style="informationMessage">
    <netui:content value="${requestScope.removeStudentMessage}"/>
</ctb:message>
</c:if>        

<netui-data:getData resultId="studentInformationMessage" value="${requestScope.studentInformationMessage}"/>

<c:if test="${studentInformationMessage!=null}">
<ctb:message title="" style="informationMessage">
    <netui:content value="${requestScope.studentInformationMessage}"/>
</ctb:message>
</c:if>        
</p>

<!-- solution for Deferred defect no 49942-->
<c:if test="${studentCount==0 && action!='view' && testSessionExpired }">
<p>  
<ctb:message title="${bundle.web['common.message.noStudents.title']}" style="informationMessage">
   
</ctb:message>
</p>
</c:if>

<c:if test="${studentCount==0 && action!='view' && !testSessionExpired }">
<p>  
<ctb:message title="${bundle.web['common.message.noStudents.title']}" style="informationMessage">
    <netui:content value="${bundle.web['common.message.noStudents.message']}"/>
</ctb:message>
</p>
</c:if>
<!--end solution-->
<c:if test="${action!='view' && !testSessionExpired}">    
    <p><netui:button type="submit" value="${bundle.web['common.button.addStudents']}" action="gotoSelectStudentPageflow" disabled="false"/></p>
</c:if>
    
<c:if test="${studentCount>0}">
<netui-compat-template:visible visibility="{request.formIsClean}" negate="true">
    <p><ctb:message title="{bundle.web['common.message.form.invalidCharacters']}" style="alertMessage"></ctb:message></p>
</netui-compat-template:visible>

<table class="sortable">
    <tr class="sortable">
        <td class="sortableControls" colspan="7">
            <ctb:tableFilter dataSource="actionForm.filterVisible">
                <table class="tableFilter">
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft" colspan="6"><netui:content value="${bundle.web['selectsettings.students.filter.title']}"/></td>
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.lastName']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testRosterFilter.lastNameFilterType" size="1" defaultValue="${actionForm.testRosterFilter.lastNameFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testRosterFilter.lastName" onKeyPress="return handleEnterKey('currentAction', 'applyFilters');"/></td>                    
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.organization']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testRosterFilter.organizationFilterType" size="1" defaultValue="${actionForm.testRosterFilter.organizationFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testRosterFilter.organization" onKeyPress="return handleEnterKey('currentAction', 'applyFilters');"/></td>                    
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.firstName']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.nameOptions}" dataSource="actionForm.testRosterFilter.firstNameFilterType" size="1" defaultValue="${actionForm.testRosterFilter.firstNameFilterType}" style="width:100px"/></td>
                    <td class="tableFilter alignLeft"><netui:textBox dataSource="actionForm.testRosterFilter.firstName" onKeyPress="return handleEnterKey('currentAction', 'applyFilters');"/></td>                    
<c:if test="${isTabeProduct}">                                         
                    <td class="tableFilter alignLeft">&nbsp;</td>
                    <td class="tableFilter alignLeft">&nbsp;</td>
</c:if>                                    
<c:if test="${! isTabeProduct}">                                         
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.form']}"/>:</td>
                    <td class="tableFilter alignLeft"><netui:select optionsDataSource="${pageFlow.formOptions}" dataSource="actionForm.testRosterFilter.formFilterType" size="1" defaultValue="${actionForm.testRosterFilter.formFilterType}" style="width:100px"/></td>
</c:if>                                    
                    <td class="tableFilter alignLeft">&nbsp;</td>                    
                </tr>
<c:if test="${sessionScope.supportAccommodations}">                                
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft"><netui:content value="${bundle.web['common.column.accommodations']}"/>:</td>
                    <td class="tableFilter alignLeft" colspan="2"><netui:select optionsDataSource="${pageFlow.accommodationTypeOptions}" dataSource="actionForm.testRosterFilter.accommodationFilterType" size="1" defaultValue="${actionForm.testRosterFilter.accommodationFilterType}" onChange="setElementValueAndSubmitWithAnchor('currentAction', 'changeAccommodation', 'studentTableAnchor');" style="width:270px"/></td>
                    <td class="tableFilter alignLeft" colspan="2">&nbsp;</td>
                </tr>
                <tr class="tableFilter">
                    <td class="tableFilter alignLeft">&nbsp;</td>
                    <td class="tableFilter alignLeft" colspan="2">
                    <c:if test="${showAccommodations}"> 
                        <netui:select optionsDataSource="${pageFlow.selectedAccommodationsOptions}" dataSource="actionForm.testRosterFilter.selectedAccommodations" size="4" multiple="true" defaultValue="${actionForm.testRosterFilter.selectedAccommodations}" onClick="enableElementById('applyFilter');" style="width:270px"/><br/><br/>
                        <netui:span value="${bundle.web['selectstudents.accommodations.info']}"/>
                    </c:if>
                    </td>
                    <td class="tableFilter alignLeft" colspan="2">&nbsp;</td>
                </tr>
</c:if>                
                <tr class="tableFilter">
                    <td class="tableFilter alignRight" colspan="6">
                        <netui:button tagId="applyFilter" value="${bundle.widgets['button.apply']}" type="button" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'applyFilters', 'studentTableAnchor');" disabled="${requestScope.disableApply}"/>&nbsp;                        
                        <netui:button tagId="clearFilter" value="${bundle.widgets['button.clearAll']}" type="button" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'clearFilters', 'studentTableAnchor');"/>&nbsp;
                    </td>                        
                </tr>
                </table>
                <hr class="sortableControls"/>
            </ctb:tableFilter>
            <table class="tableFilter">
            <tr class="tableFilter">
                <td class="tableFilter">
                    <ctb:tableFilterToggle dataSource="actionForm.filterVisible" />&nbsp;
                    <c:if test="${action != 'view'}">     
                        <netui:button type="button" tagId="removeSelectedStudents" value="${bundle.web['common.button.removeSelectedStudents']}" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'removeSelectedStudents', 'studentTableAnchor');" disabled="${requestScope.disableRemoveSelectedStudents}"/>              
                        <netui:button type="submit" value="${bundle.web['common.button.removeAllStudents']}" onClick="return verifyRemoveAllStudents('studentTableAnchor');"/>              
                    </c:if>
                </td>
            </tr>
            </table>
            
        </td>
    </tr>
        
            
<netui-data:repeater dataSource="pageFlow.studentNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.studentStatePathList.sortColumn" orderByDataSource="actionForm.studentStatePathList.sortOrderBy" anchorName="studentTableAnchor">
            <th class="sortable alignCenter" nowrap><netui:span value="${bundle.web['common.column.select']}"/></th>                
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="LastName"><netui:span value="Last Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="FirstName"><netui:span value="First Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="MiddleName"><netui:span value="M.I."/></ctb:tableSortColumn></th>
<c:if test="${sessionScope.supportAccommodations}">                                
            <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="HasAccommodations"><netui:span value="Accommodations"/></ctb:tableSortColumn></th>
</c:if>            
            <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
<c:if test="${! isTabeProduct}">                 
            <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="ItemSetForm"><netui:span value="Form"/></ctb:tableSortColumn></th>
</c:if>            
        </ctb:tableSortColumnGroup>
    </tr>
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    <tr class="sortable">
        <td class="sortable alignCenter">
        <c:if test="${action=='view'}"> 
            <netui:checkBoxGroup dataSource="actionForm.selectedStudentIds" disabled="true">
                &nbsp;<netui:checkBoxOption value="${container.item.studentId}">&nbsp;</netui:checkBoxOption>                
            </netui:checkBoxGroup>
        </c:if>       
        <c:if test="${action!='view'}"> 
            <ctb:switch dataSource="${container.item.status.editable}">
                <ctb:case value="T">
                    <netui:checkBoxGroup dataSource="actionForm.selectedStudentIds">
                        &nbsp;<netui:checkBoxOption value="${container.item.studentId}" onClick="enableElementById(getNetuiTagName('removeSelectedStudents'));">&nbsp;</netui:checkBoxOption>                
                    </netui:checkBoxGroup>
                </ctb:case>
                <ctb:case value="F">
                    <netui:checkBoxGroup dataSource="actionForm.selectedStudentIds" disabled="true">
                        &nbsp;<netui:checkBoxOption value="${container.item.studentId}">&nbsp;</netui:checkBoxOption>                
                    </netui:checkBoxGroup>
                </ctb:case>
            </ctb:switch>                        
        </c:if>       
        </td>        
        <td class="sortable alignLeft">     
            <netui:span value="${container.item.lastName}"/>
        </td>
        <td class="sortable alignLeft">     
            <netui:span value="${container.item.firstName}"/>
        </td>
        <td class="sortable alignCenter">
            <netui:span value="${container.item.middleName}"/>
        </td>
<c:if test="${sessionScope.supportAccommodations}">                                        
        <td class="sortable alignCenter">
            <netui-data:getData resultId="accommodations" value="${container.item.extPin3}"/>
            <%
                String toolTipMsg = (String)pageContext.getAttribute("accommodations");
            %>
            <ctb:switch dataSource="${container.item.hasAccommodations}">
                <ctb:case value="true">
                <a href="#" style="text-decoration: none" title="<%= toolTipMsg %>" onclick="return false;">Yes</a>
                </ctb:case>
                <ctb:case value="false"><netui:span value="--"/></ctb:case>
            </ctb:switch>                       
        </td>
</c:if>        
        <td class="sortable alignCenter">
            <netui:span value="${container.item.orgNodeName}"/>
        </td>
<c:if test="${! isTabeProduct}">                 
        <td class="sortable alignCenter">
            <ctb:switch dataSource="${container.item.status.editable}">
                <ctb:case value="T">
                    <c:if test="${!isFormEditable}">
                        <netui:span value="${container.item.itemSetForm}"/>
                    </c:if>    
                    <c:if test="${isFormEditable}">
                        <netui:select dataSource="container.item.itemSetForm" defaultValue="${container.item.itemSetForm}" optionsDataSource="${pageFlow.formList}" size="1" multiple="false">
                        </netui:select>
                    </c:if>    
                </ctb:case>
                <ctb:case value="F">
                    <netui:span value="${container.item.itemSetForm}"/>
                </ctb:case>
            </ctb:switch>                        
        </td>
</c:if>        
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
<ctb:tableNoResults dataSource="{pageFlow.studentNodes}">
    <tr class="sortable">
        <td class="sortable" colspan="7">
            <ctb:message title="${bundle.web['common.message.title.noStudents']}" style="tableMessage">
                <netui:content value="${bundle.web['selectsettings.students.noStudentMessageInfo']}"/>
            </ctb:message>
        </td>
    </tr>
</ctb:tableNoResults>
              
</table>
</p>
</c:if>


<a name="proctorTableAnchor"><!-- proctorTableAnchor --></a>    
<c:if test="${action=='view'}">    
<h3><netui:span value="${bundle.web['selectsettings.proctorAssignments.title.view']}"/></h3>
<p>
    <netui:content value="${bundle.web['selectsettings.proctorAssignments.message.view']}"/>
</c:if>
<!-- solution for defect no 49963-->
<c:if test="${action!='view'}">  
    <ctb:auth roles="Proctor">  
        <h3><netui:span value="${bundle.web['selectsettings.proctorAssignments.title.view']}"/></h3>
        <p>
            <netui:content value="${bundle.web['selectsettings.proctorAssignments.message.view']}"/>
    </ctb:auth>
</c:if>
<!-- end solution -->
<c:if test="${action!='view'}">
<!-- solution for Deferred defect no 49963-->
 <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">    
<!-- end solution -->
    <h3><netui:span value="${bundle.web['selectsettings.proctorAssignments.title']}"/></h3>
    <p>
        <c:if test="${testSessionExpired}">    
            <netui:content value="${bundle.web['selectsettings.proctorAssignments.message.view']}"/>
        </c:if>
        <c:if test="${!testSessionExpired}">    
            <netui:content value="${bundle.web['selectsettings.proctorAssignments.message']}"/>
        </c:if>
<!-- solution for defect no 49963-->
    </ctb:auth>
<!-- end solution -->
</c:if>
</p>
<p>
<netui-data:getData resultId="proctorInformationMessage" value="${requestScope.proctorInformationMessage}"/>

<c:if test="${proctorInformationMessage!=null}">
<ctb:message title="" style="informationMessage">
    <netui:content value="${requestScope.proctorInformationMessage}"/>
</ctb:message>
<br/>
</c:if>        

<table class="transparent">
<tr class="transparent">
    <td class="transparent"><netui:span value="${bundle.web['selectsettings.label.assignedProctors']}"/> </td>
    <td class="transparent"><div class="formValue"><netui:span value="${requestScope.proctorCount}" styleClass="formValue"/></div></td>
    <td class="transparent"><netui:span value="${bundle.web['selectsettings.label.testScheduler']}"/></td>
    <td class="transparent"><div class="formValue"><netui:span value="${sessionScope.schedulerFirstLastName}" styleClass="formValue"/></div></td>
</tr>
</table>
<c:if test="${action=='schedule'}">
    <netui:button type="submit" value="${bundle.web['common.button.addProctors']}" action="gotoSelectProctorPageflow"/>
</c:if>
<c:if test="${action=='edit' && !testSessionExpired}">    
    <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
        <netui:button type="submit" value="${bundle.web['common.button.addProctors']}" action="gotoSelectProctorPageflow"/>
    </ctb:auth>    
</c:if>

</p>

<p>  
<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="3">
        &nbsp;
        <c:if test="${action=='schedule'}">
            <netui:button type="button" tagId="removeSelectedProctors" value="${bundle.web['common.button.removeSelectedProctors']}" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'removeSelectedProctors', 'proctorTableAnchor');" disabled="${requestScope.disableRemoveSelectedProctors}"/>              
            <netui:button type="submit" value="${bundle.web['common.button.removeAllProctors']}" onClick="return verifyRemoveAllProctors('proctorTableAnchor');"/>              
        </c:if>
        <c:if test="${action=='edit' && !testSessionExpired}">    
            <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
                <netui:button type="button" tagId="removeSelectedProctors" value="${bundle.web['common.button.removeSelectedProctors']}" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'removeSelectedProctors', 'proctorTableAnchor');" disabled="${requestScope.disableRemoveSelectedProctors}"/>              
                <netui:button type="submit" value="${bundle.web['common.button.removeAllProctors']}" onClick="return verifyRemoveAllProctors('proctorTableAnchor');"/>              
            </ctb:auth>    
        </c:if>
        </td>
    </tr>
    
<netui-data:repeater dataSource="requestScope.proctorNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.proctorStatePathList.sortColumn" orderByDataSource="actionForm.proctorStatePathList.sortOrderBy" anchorName="proctorTableAnchor">
            <th class="sortable alignCenter" nowrap><netui:span value="${bundle.web['common.column.select']}"/></th>                
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="LastName"><netui:span value="Last Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="FirstName"><netui:span value="First Name"/></ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    <tr class="sortable">
        <td class="sortable alignCenter">
        <c:if test="${action=='view'}"> 
            <netui:checkBoxGroup dataSource="actionForm.selectedProctorIds" disabled="true">
                &nbsp;<netui:checkBoxOption value="${container.item.userId}">&nbsp;</netui:checkBoxOption>                
            </netui:checkBoxGroup>
        </c:if>       
        <c:if test="${action!='view'}"> 
            <ctb:switch dataSource="${container.item.editable}">
                <ctb:case value="T">
                    <netui:checkBoxGroup dataSource="actionForm.selectedProctorIds">
                        &nbsp;<netui:checkBoxOption value="${container.item.userId}" onClick="enableElementById(getNetuiTagName('removeSelectedProctors'));">&nbsp;</netui:checkBoxOption>                
                    </netui:checkBoxGroup>
                </ctb:case>
                <ctb:case value="F">
                    <netui:checkBoxGroup dataSource="actionForm.selectedProctorIds" disabled="true">
                        &nbsp;<netui:checkBoxOption value="${container.item.userId}">&nbsp;</netui:checkBoxOption>                
                    </netui:checkBoxGroup>
                </ctb:case>
            </ctb:switch>                        
        </c:if>       
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
            <td class="sortableControls" colspan="3">
                <ctb:tablePager dataSource="actionForm.proctorStatePathList.pageRequested" summary="request.proctorPagerSummary" objectLabel="Proctors" anchorName="proctorTableAnchor" id="proctorTableAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>

              
</table>
</p>
       
