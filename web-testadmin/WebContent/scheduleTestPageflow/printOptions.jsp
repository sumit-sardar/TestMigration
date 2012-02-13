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

<!--Change MQC defect  55837 -->

<%String templatePage = "/resources/jsp/template.jsp";%>
<ctb:switch dataSource="${pageFlow.action}">
    <ctb:case value="edit">
        <% templatePage="/resources/jsp/editTemplate.jsp";%>
    </ctb:case>
    <ctb:case value="view">
        <% templatePage="/resources/jsp/viewTemplate.jsp";%>
    </ctb:case>
</ctb:switch> 


<netui-template:template templatePage="<%=templatePage%>">
<netui-template:setAttribute name="title" value="${bundle.web['printoptions.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.printOptions']}"/>
<netui-template:section name="bodySection">

<!--End of change for MQC defect  55837 -->

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
 

<netui:form action="printOptions">

<netui:hidden dataSource="actionForm.creatorOrgNodeName"/>   
<netui:hidden dataSource="actionForm.studentStatePathList.maxPageRequested"/>
<netui:hidden dataSource="actionForm.orgStatePathList.maxPageRequested"/>
<netui:hidden dataSource="actionForm.testAdmin.isRandomize"/><!--Change for Random Distractor-->

<netui:hidden dataSource="actionForm.autoLocator"/>

<netui-data:getData resultId="isTabeProduct" value="${requestScope.isTabeProduct}"/>
<netui-data:getData resultId="isTabeAdaptiveProduct" value="${requestScope.isTabeAdaptiveProduct}"/>
<netui-data:getData resultId="isLasLinksProduct" value="${requestScope.isLasLinksProduct}"/>
<netui-data:getData resultId="hasStudentSelected" value="${requestScope.hasStudentSelected}"/>

<netui-data:getData resultId="showAccessCode" value="${requestScope.showAccessCode}"/>

<netui:hidden dataSource="actionForm.testAdmin.productId"/>
<!-- Non TABE -->
<c:if test="${!isTabeProduct && !isTabeAdaptiveProduct}"> 
	<ctb:switch dataSource="${pageFlow.action}">

	<table width="100%" cellpadding="0" cellspacing="0" class="transparent">
		<tr>
			<td nowrap="">
				<font size="6">
					<ctb:case value="schedule">
					  <h1><netui:span value="${bundle.web['printoptions.title.schedule']}"/></h1>
					</ctb:case>    
					<ctb:case value="edit">
						<h1><netui:span value="${bundle.web['printoptions.title.edit']}"/></h1>
					</ctb:case>    
					<ctb:case value="view">
					  <h1><netui:span value="${bundle.web['printoptions.title.view']}"/></h1>
					</ctb:case> 
				</font>
			</td>
			<td class="transparent"></td>
			<td width="100%" rowspan="2" align="right" valign="top">
			&nbsp;
			</td>
			<td rowspan="2">
				<table width="25"><tr><td></td></tr></table>
			</td>
		</tr>
		<!--change for licnese-->
	</table>
	<!--End of change License-->
	  
	</ctb:switch>   
	<p><netui:content value="${bundle.web['printoptions.printOptions.message']}"/></p>
</c:if>


<!-- TABE -->
<c:if test="${isTabeProduct || isTabeAdaptiveProduct}"> 
	<ctb:switch dataSource="${pageFlow.action}">
	<table width="100%" cellpadding="0" cellspacing="0" class="transparent">
		<tr>
			<td nowrap="">
				<font size="6">
				<ctb:case value="schedule">
					<h1><netui:span value="${bundle.web['options.title.schedule']}"/></h1>
				</ctb:case>    
				<ctb:case value="edit">
				   <h1><netui:span value="${bundle.web['options.title.edit']}"/></h1>
				</ctb:case>    
				<ctb:case value="view">
				   <h1><netui:span value="${bundle.web['options.title.view']}"/></h1>
				</ctb:case>  
         		</font>
			</td>
			<td class="transparent"></td>
			<td width="100%" rowspan="2" align="right" valign="top">
			&nbsp;
			</td>
			<td rowspan="2">
				<table width="25"><tr><td></td></tr></table>
			</td>
		</tr>
	<!--change for licnese-->
	</table>
	<!--End of change License-->
	</ctb:switch>   
	<p><netui:content value="${bundle.web['options.printOptions.message']}"/></p>
</c:if>



<netui:hidden tagId= "actionElement" dataSource="actionForm.actionElement"/>
<netui:hidden tagId= "currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.testAdmin.testName"/>   
<netui:hidden dataSource="actionForm.testAdmin.level"/>   
<netui:hidden dataSource="actionForm.testAdmin.accessCode"/>   
<netui:hidden dataSource="actionForm.testAdmin.sessionName"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/>
<netui:hidden dataSource="actionForm.selectedProductName"/>        
<netui:hidden dataSource="actionForm.selectedLevel"/>        
<netui:hidden dataSource="actionForm.hasBreak"/>   
<netui:hidden dataSource="actionForm.selectedTestId"/> 
<netui:hidden dataSource="actionForm.action"/> 
<netui:hidden dataSource="actionForm.creatorOrgNodeId"/>  
 
<netui:hidden dataSource="actionForm.accommodationOperand"/>
<netui:hidden dataSource="actionForm.selectedGrade"/>
<netui:hidden dataSource="actionForm.selectedAccommodationElements"/>
<netui:hidden dataSource="actionForm.filterVisible"/>

<input type="hidden" id="printTestAdminId" name="printTestAdminId" value="<netui:content value="${pageFlow.testAdminId}"/>"/>
<input type="hidden" id="printOrgNodeId"   name="printOrgNodeId"   value="<netui:content value="${actionForm.selectedOrgNodeId}"/>"/>

<netui-data:getData resultId="showTicketLink" value="${requestScope.showTicketLink}"/>

<netui-data:getData resultId="informationMessage" value="${requestScope.informationMessage}"/>
<netui-data:getData resultId="errorMessage" value="${requestScope.errorMessage}"/>
<netui-data:getData resultId="subtestWarningMessage" value="${requestScope.subtestValidationMessage}"/>

<c:if test="${errorMessage!=null}">
<ctb:message title="" style="errorMessage">
    <netui:content value="${requestScope.errorMessage}"/>
</ctb:message>
</c:if> 
       
<c:if test="${informationMessage!=null}">
<ctb:message title="" style="informationMessage">
    <netui:content value="${requestScope.informationMessage}"/>
</ctb:message>
</c:if>      

<c:if test="${subtestWarningMessage != null}">
    <ctb:message title="" style="alertMessage">
        <netui:content value="${actionForm.subtestValidationMessage}"/>
    </ctb:message><br/>
</c:if>
  
<br/>

<h3><netui:span value="${bundle.web['printoptions.testInformation.title']}"/></h3>
<br/>
<p>
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" nowrap><netui:span value="${bundle.web['printoptions.label.testName']}"/></td>
        <td class="transparent" ><div class="formValueMedium"><netui:label value="${scheduledTestSessionData.testName}" styleClass="formValueMedium"/></div></td>
        <td class="transparent" nowrap><netui:span value="${bundle.web['printoptions.label.testSessionName']}"/></td>
        <td class="transparent" ><div class="formValueMedium"><netui:label value="${scheduledTestSessionData.sessionName}" styleClass="formValueMedium"/></div></td>
	</tr>
</table>




<!-- TABE -->
<!-- ***************** Modify Student's Test *********** -->
<c:if test="${hasStudentSelected}">
<c:if test="${isTabeProduct || isTabeAdaptiveProduct || isLasLinksProduct}">
<h3><netui:span value="Modify Student's Test"/></h3>
<p><netui:content value="Select a student and click Modify Test to change the subtests, difficulty level, or subtest order for an individual student."/></p>
<p>
<table class="sortable">
<netui-data:repeater dataSource="pageFlow.studentNodes">
    <netui-data:repeaterHeader>
    <tr class="sortable">
        <td class="sortableControls" colspan="7">
            &nbsp;<netui:button tagId="modifyTest" type="button" value="Modify Test" onClick="submitWithAction('gotoEditSubtestLevels.do'); return false;" disabled="${requestScope.disableModifyTestButton}"/>
        </td>
    </tr>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.studentStatePathList.sortColumn" orderByDataSource="actionForm.studentStatePathList.sortOrderBy" anchorName="studentTableAnchor">
            <th class="sortable alignCenter" nowrap><netui:span value="${bundle.web['common.column.select']}"/></th>                
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="LastName"><netui:span value="Last Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="FirstName"><netui:span value="First Name"/></ctb:tableSortColumn></th>
            <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="MiddleName"><netui:span value="M.I."/></ctb:tableSortColumn></th>
<c:if test="${sessionScope.supportAccommodations}">                                
            <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="HasAccommodations"><netui:span value="Accommodations"/></ctb:tableSortColumn></th>
</c:if>            
            <th class="sortable alignCenter" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.studentOrgCategoryName}"/></ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    <tr class="sortable">
        <td class="sortable alignCenter">        
        <ctb:switch dataSource="${container.item.status.editable}">
            <ctb:case value="T">        
                <netui:radioButtonGroup dataSource="actionForm.selectedStudentId">
                    &nbsp;<netui:radioButtonOption value="${container.item.studentId}" onClick="enableElementById('modifyTest'); setFocus('modifyTest');">&nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>
            </ctb:case>
            <ctb:case value="F">
                <netui:radioButtonGroup dataSource="actionForm.selectedStudentId" disabled="true">
                    &nbsp;<netui:radioButtonOption value="${container.item.studentId}">&nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>
            </ctb:case>
        </ctb:switch>            
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
</p>
</c:if>
</c:if>  <!-- end of Modify Student's Test -->
</p>
<h3><netui:span value="${bundle.web['printoptions.organizations.title']}"/></h3>
<p><netui:content value="${bundle.web['printoptions.organizations.message']}"/></p>
<p>

<a name="tableAnchor"><!-- tableAnchor --></a>    
<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="4">
            <ctb:tablePathList valueDataSource="actionForm.orgNodeId" labelDataSource="actionForm.orgNodeName" pathList="request.orgNodePath" />
        </td>
    </tr>
    
<netui-data:repeater dataSource="requestScope.orgNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.orgStatePathList.sortColumn" orderByDataSource="actionForm.orgStatePathList.sortOrderBy" anchorName="tableAnchor">
            <th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
            <th class="sortable alignLeft" width="73%" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
            <th class="sortable alignRight" width="22%" nowrap><ctb:tableSortColumn value="RosterCount"><netui:content value="${bundle.web['common.column.totalSelectedStudents']}"/></ctb:tableSortColumn></th>
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
            <ctb:switch dataSource="${container.item.hasChildren}">
                <ctb:case value="true"><ctb:tablePathEntry srcLabelDataSource="${container.item.name}" srcValueDataSource="${container.item.id}" dstLabelDataSource="actionForm.orgNodeName" dstValueDataSource="actionForm.orgNodeId" /></ctb:case>
                <ctb:case value="false"><netui:span value="${container.item.name}"/></ctb:case>
            </ctb:switch>                
       </td>
        <td class="sortable alignRight">
            <netui:span value="${container.item.selectedCount}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="3">
                <ctb:tablePager dataSource="actionForm.orgStatePathList.pageRequested" summary="request.orgPagerSummary" objectLabel="${bundle.oas['object.organizations']}" anchorName="tableAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    <ctb:tableNoResults dataSource="request.orgNodes">
        <tr class="sortable">
            <td class="sortable" colspan="4">
                <ctb:message title="${bundle.web['printoptions.organizations.messageTitle']}" style="tableMessage">
                    <netui:content value="${bundle.web['printoptions.organizations.messageInfo']}"/>
                </ctb:message>
            </td>
        </tr>
    </ctb:tableNoResults>

              
</table>

</p>


<c:if test="${showTicketLink}">
<br/>

<h3><netui:span value="${bundle.web['printoptions.testSessionDocuments.title']}"/></h3>
<p>
<c:if test="${showAccessCode}"> 
<netui:content value="${bundle.web['printoptions.testSessionDocuments.message.displayAccessCode']}"/>
<input type="radio" id="allow" name="individualAccess" value="Yes" onclick="accessCode()">Yes</input>
<input type="radio" id="deny" name="individualAccess" value="No" onclick="accessCode()" checked="checked">No</input>
</c:if>
<br>
<br>
<netui:content value="${bundle.web['printoptions.testSessionDocuments.message']}"/><br>
</p>
<p>
<netui:anchor href="#" onClick="return openTestTicketIndividual(this, document.getElementById('printTestAdminId').value, document.getElementById('printOrgNodeId').value);">Individual Test Tickets</netui:anchor>
&nbsp;
<img src="/TestAdministrationWeb/resources/images/logo_pdf.gif" border="0">
<br/>
<netui:content value="${bundle.web['printoptions.testSessionDocuments.message.testTickets']}"/>
<br>

</p>

<!--START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)-->
<p>
<netui:anchor href="#" onClick="return openTestTicketMultiple(this, document.getElementById('printTestAdminId').value, document.getElementById('printOrgNodeId').value);">Multiple Test Tickets</netui:anchor>
&nbsp;
<img src="/TestAdministrationWeb/resources/images/logo_pdf.gif" border="0">
<br/>
<netui:content value="${bundle.web['printoptions.testSessionDocuments.message.multipleTestTickets']}"/>
</p>
<!--END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)-->

<p>
<netui:anchor href="#" onClick="return openTestTicketSummary(this, document.getElementById('printTestAdminId').value, document.getElementById('printOrgNodeId').value);">Summary Test Ticket</netui:anchor>
&nbsp;<img src="/TestAdministrationWeb/resources/images/logo_pdf.gif" border="0">
<br/>
<netui:content value="${bundle.web['printoptions.testSessionDocuments.message.summarySheet']}"/>
</p>
</c:if>
 
 
 
 
 
<hr/>
<netui:button type="submit" value="${bundle.web['common.button.back']}" action="backToSelectSettings"/>
<netui:button type="submit" value="${bundle.web['common.button.finish']}" action="goToHomePage"/>

</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>
