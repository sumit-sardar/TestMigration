<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-compat-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
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
<netui-template:setAttribute name="title" value="${bundle.web['selecttest.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.scheduleTestSessionSelectTest']}"/>
<netui-template:section name="bodySection">

<!--End of change for MQC defect  55837 -->

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<netui-data:getData resultId="action" value="${pageFlow.action}"/>

<netui:form action="selectTest">


<netui-compat-data:getData resultId="hasBreak" value="{actionForm.hasBreak}"/>
<netui-compat-data:getData resultId="showCancel" value="{pageFlow.condition.showCancelOnFirstPage}"/>
<netui-data:getData resultId="hideTestOptions" value="${requestScope.hideTestOptions}"/>
<netui-data:getData resultId="hideProductNameDropDown" value="${requestScope.hideProductNameDropDown}"/>
<netui-data:getData resultId="hideLevelDropDown" value="${requestScope.hideLevelDropDown}"/>
<netui-data:getData resultId="showLevelOrGrade" value="${pageFlow.showLevelOrGrade}"/>
<netui-data:getData resultId="acknowledgmentsURL" value="${requestScope.acknowledgmentsURL}"/>
<netui-data:getData resultId="isTabeProduct" value="${requestScope.isTabeProduct}"/>

<netui-data:getData resultId="productType" value="${requestScope.productType}"/>
<!--Change For License-->
<netui-compat-data:getData  resultId="licenseConfig" value="{session.disableAvailableBarTestSeesion}"/>
<netui-compat-data:getData  resultId="licenseAdminConfig" value="{session.disableAvailableBarSubtest}"/>

<netui-compat-data:getData  resultId="displayLicenseBar" value="{session.displayLicenseBar}"/>

<netui-compat-data:getData  resultId="licensebarColor" value="{pageFlow.licenseBarColor}"/>

<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/>
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
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
<netui:hidden dataSource="actionForm.studentStatePathList.sortColumn"/>
<netui:hidden dataSource="actionForm.studentStatePathList.sortOrderBy"/>
<!--added for License Management-->
<netui:hidden dataSource="actionForm.licensePercentage"/>
<netui-data:getData resultId="alertMessage" value="${requestScope.alertMessage}"/>
<netui-data:getData resultId="informationMessage" value="${requestScope.informationMessage}"/>
<netui-data:getData resultId="errorMessage" value="${requestScope.errorMessage}"/>
<netui:hidden dataSource="actionForm.testAdmin.productId"/>

<netui-compat-data:getData resultId="autoLocator" value="{actionForm.autoLocator}"/>




<!--change for licnese-->
 <%!String color = "red";%>
  <c:if test="${licensebarColor =='RED'}"> 
        <% color = "red";%>
  </c:if>
  <c:if test="${licensebarColor =='YELLOW'}"> 
    <% color = "yellow";%>
  </c:if>
  <c:if test="${licensebarColor =='GREEN'}">
    <% color = "green";%>
  </c:if>


<table width="100%" cellpadding="0" cellspacing="0" class="transparent">
    
	<tr>
		<td nowrap="">
			<font size="6"><b>
			
				<c:if test="${action=='edit'}"> 
					<h1><netui:span value="${bundle.web['selecttest.title.edit']}"/></h1>
				</c:if>	

				<c:if test="${action!='edit'}">
					<h1><netui:span value="${bundle.web['selecttest.title.schedule']}"/></h1>
				</c:if>

			</b></font>
		</td>
		
		<td width="65%" class="transparent"></td>
		<td width="100%" rowspan="2" align="right" valign="top">

			<c:if test="${displayLicenseBar}">
				<table width="150" height="100%" cellpadding="0" cellspacing="2">
					<tr >
						<td  class="transparent-label" width="100%" height="100%" align="left" nowrap=""><netui:span value="${bundle.web['licenses.title']}"/></td>
					</tr>

					<tr>
						<td class="transparent" height="100%" align="center" nowrap="" bgcolor="<%=color%>">
							<c:if test="${licensebarColor =='RED'}"> 
							
								<netui:span value="${actionForm.licensePercentage}" style="background-color:#ff0000;color:#ffffff"/>
							</c:if>
							
							<c:if test="${licensebarColor =='YELLOW'}">
							
								<netui:span value="${actionForm.licensePercentage}" style="background-color:#ffff00"/>
							</c:if> 
							
							<c:if test="${licensebarColor =='GREEN'}">
						 
								<netui:span value="${actionForm.licensePercentage}" style="background-color:#347C17;color:#ffffff"/>
							</c:if>
						</td>
					</tr>
				</table>
			</c:if>

		</td>

		<td rowspan="2">
			<table width="25"><tr><td></td></tr></table>
		</td>

	</tr>
		

	<!--change for licnese-->

	<tr>   
    
        <td width="62%" class="transparent">
        <c:if test="${action=='edit'}"> 
            <netui:content value="${bundle.web['selecttest.selectTest.edit.message']}"/>
        </c:if>	

        <c:if test="${action!='edit'}">
            <netui:content value="${bundle.web['selecttest.selectTest.message']}"/>
        </c:if>
        </td>
		<td  class="transparent"></td> 
	</tr>

</table>


  <!--End of change License-->
	<!-- message -->
  <jsp:include page="/scheduleTestPageflow/show_message.jsp" />
  
  <c:if test="${errorMessage != null}">
    <ctb:message title="" style="errorMessage">
        <netui:content value="${requestScope.errorMessage}"/>
    </ctb:message><br/>
  </c:if>        

<c:if test="${errorMessage == null}">

    <c:if test="${alertMessage != null}">
        <ctb:message title="" style="alertMessage">
            <netui:content value="${requestScope.alertMessage}"/>
        </ctb:message><br/>
    </c:if>        
    
    <c:if test="${informationMessage != null}">
        <ctb:message title="" style="informationMessage">
            <netui:content value="${requestScope.informationMessage}"/>
        </ctb:message><br/>
    </c:if>   

</c:if>  
<c:if test="${acknowledgmentsURL!=null && acknowledgmentsURL != ''}">
<p align="right">
    <a href="<c:out value="${acknowledgmentsURL}"/>" onClick="openAcknowledgmentsWindow(this.href);return false;"><netui:content value="${bundle.web['selecttest.viewProductAcknowledgements']}"/></a>
</p>    
</c:if>        

<!-- ******************************************************************************** -->
<!-- Test Group -->
<!-- ******************************************************************************** -->
<table class="transparent" width="100%">
<tr class="transparent">


    <!-- Test Group -->
    <td class="transparent" width="300">
    <netui:span value="${bundle.web['selecttest.label.productName']}"/>
    <br/>
    <c:if test="${!hideProductNameDropDown}"> 
        <netui:select dataSource="actionForm.selectedProductName" optionsDataSource="${pageFlow.productNameList}" size="1" multiple="false" onChange="setElementValueAndSubmit('currentAction', 'changeProduct');">
        </netui:select>
    </c:if>        
    <c:if test="${hideProductNameDropDown}"> 
            <div class="formValue"><netui:span value="${actionForm.selectedProductName}" styleClass="formValue"/></div> 
            <netui:hidden dataSource="actionForm.selectedProductName"/>
    </c:if>        
    </td>


<!-- Non TABE -->
<c:if test="${! isTabeProduct}"> 
    <c:if test="${showLevelOrGrade == 'level' || showLevelOrGrade == 'grade'}"> 
        <td class="transparent" width="200">
        <c:if test="${showLevelOrGrade == 'level'}"> 
            <netui:span value="${bundle.web['selecttest.label.level']}"/>
        </c:if>        
        <c:if test="${showLevelOrGrade == 'grade'}"> 
            <netui:span value="${bundle.web['selecttest.label.grade']}"/>
        </c:if>        
        <br/>
        <c:if test="${!hideLevelDropDown}"> 
            <netui:select dataSource="actionForm.selectedLevel" optionsDataSource="${pageFlow.levelList}" size="1" multiple="false" onChange="setElementValueAndSubmit('currentAction', 'changeLevel');">
            </netui:select>
        </c:if>        
        <c:if test="${hideLevelDropDown}"> 
                <div class="formValue"><netui:span value="${actionForm.selectedLevel}" styleClass="formValue"/></div> 
                <netui:hidden dataSource="actionForm.selectedLevel"/>
        </c:if>        
        </td>
    </c:if>        
</c:if>        


<!-- TABE -->
<c:if test="${isTabeProduct}"> 
    <!-- remove Grade column -->
    <td class="transparent" width="200">&nbsp;</td>
</c:if>     
</tr>
</table>







<!-- ******************************************************************************** -->
<!-- Tests -->
<!-- ******************************************************************************** -->
<p>
<h4><netui:span value="${bundle.web['selecttest.tests.title']}"/></h4>
<c:if test="${selectedTestName != null}"> 
<p>Selected: <netui:span value="${requestScope.selectedTestName}"/></p>
</c:if>
</p>
<%-- Use for show all level--%>
<netui-data:getData resultId="islevelShowall" value="${actionForm.selectedLevel}"/>
<netui-data:getData resultId="selectedTestId" value="${actionForm.selectedTestId}"/>
<%-- End show all level--%>
<table class="sortable">
    
<netui-data:repeater dataSource="pageFlow.testList">
<netui-data:repeaterHeader>

<tr class="sortable">
    <ctb:tableSortColumnGroup columnDataSource="{actionForm.testStatePathList.sortColumn}" orderByDataSource="{actionForm.testStatePathList.sortOrderBy}" >
        <th class="sortable alignCenter" nowrap><netui:span value="${bundle.web['common.column.select']}"/></th>                
        <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="ItemSetName"><netui:span value="${bundle.web['common.column.testName']}"/></ctb:tableSortColumn></th>
        
<!-- Non TABE -->
<c:if test="${! isTabeProduct}"> 
        <c:if test="${showLevelOrGrade == 'level'}"> 
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="ItemSetLevel"><netui:span value="${bundle.web['common.column.level']}"/></ctb:tableSortColumn></th>
        </c:if>            
        <c:if test="${showLevelOrGrade == 'grade'}"> 
            <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="Grade"><netui:span value="${bundle.web['common.column.grade']}"/></ctb:tableSortColumn></th>
        </c:if>            
</c:if>            

        <th class="sortable alignLeft" style="padding: 5px;"><netui:span value="${bundle.web['common.column.subtests']}"/></th>
        <th class="sortable alignLeft" nowrap><ctb:tableSortColumn value="TimeLimit"><netui:span value="${bundle.web['common.column.duration']}"/></ctb:tableSortColumn></th>                
    </ctb:tableSortColumnGroup>
</tr>
</netui-data:repeaterHeader>
<netui-data:repeaterItem>
<tr class="sortable">
    <td class="sortable alignCenter">
        <netui:radioButtonGroup dataSource="actionForm.selectedTestId">
            &nbsp;<netui:radioButtonOption value="${container.item.id}" onClick="setElementValueAndSubmit('currentAction', 'selectTest');">&nbsp;</netui:radioButtonOption>
        </netui:radioButtonGroup>
    </td>
    <td class="sortable alignLeft"><netui:span value="${container.item.testName}" defaultValue="&nbsp;"/></td>

<!-- Non TABE -->
<c:if test="${! isTabeProduct}">     
    <c:if test="${showLevelOrGrade == 'level' || showLevelOrGrade == 'grade'}"> 
        <td class="sortable alignLeft"><netui:span value="${container.item.level}" defaultValue="&nbsp;"/></td>
    </c:if> 
</c:if> 

    <td class="sortable alignLeft"><netui:content value="${container.item.subtestsString}" defaultValue="&nbsp;"/></td>
    <td class="sortable alignLeft"><netui:span value="${container.item.duration}" defaultValue="&nbsp;"/></td>
</tr>
<!--Add the logic including section of Randomized Distractor Allowable -->

<c:if test="${islevelShowall == 'Show All'}">
    <netui-data:getData resultId="testId" value="${container.item.id}"/>
    <c:if test = "${testId == selectedTestId}">
        <netui-data:getData resultId="rdAllow" value="${container.item.isRandomize}"/>
    </c:if>
</c:if>
<c:if test="${islevelShowall != 'Show All'}">
    <netui-data:getData resultId="rdAllow" value="${container.item.isRandomize}"/>    
</c:if>
</netui-data:repeaterItem>
<netui-data:repeaterFooter>
<tr class="sortable">
    <td class="sortableControls" colspan="5">
        <ctb:tablePager dataSource="actionForm.testStatePathList.pageRequested" summary="request.testPagerSummary" objectLabel="${bundle.web['selecttest.selectTest.tests']}" />
    </td>
</tr>             
</netui-data:repeaterFooter>
</netui-data:repeater>
<ctb:tableNoResults dataSource="{pageFlow.testList}">
    <tr class="sortable">
        <td class="sortable" colspan="5">
            <ctb:message title="{bundle.web['common.message.title.noTests']}" style="tableMessage">
                <netui:content value="${bundle.web['selecttest.tests.noTestsMessage']}"/>
            </ctb:message>
        </td>
    </tr>
</ctb:tableNoResults>
 
</table>
<br/>
<br/>





<!-- Test Security -->
<c:if test="${hideTestOptions}"> 
    <netui:hidden dataSource="actionForm.testAdmin.accessCode"/>   
</c:if>


<c:if test="${!hideTestOptions}"> 



<!-- ******************************************************************************** -->
<!-- Test Security -->
<!-- ******************************************************************************** -->

<!-- Single subtest -->
<c:if test="${!hasMultipleSubtests}"> 
    <h4><netui:span value="${bundle.web['selecttest.testOptions.title']}"/></h4> 
        <p>
        <netui:span value="${bundle.web['selecttest.testOptions.message']}"/>  
        <netui:textBox dataSource="actionForm.testAdmin.accessCode" maxlength="32" onKeyPress="return constrainEnterKeyEvent(event);"/>
        </p>
</c:if>


<!-- Multiple subtest -->
<c:if test="${hasMultipleSubtests}"> 
    <h4><netui:span value="${bundle.web['selecttest.subtestOptions.title']}"/></h4>   
        <p>
        <netui:content value="${bundle.web['selecttest.subtestOptions.message1']}"/>
        </p>   
        <p>
        <netui:content value="${bundle.web['selecttest.subtestOptions.message2']}"/>
        <br/>   
        <netui:radioButtonGroup dataSource="actionForm.hasBreak">
            <netui:radioButtonOption value="false" onClick="submit()"><netui:span value="${bundle.web['selecttest.label.no']}"/></netui:radioButtonOption>
            <c:if test="${!hasBreak}"> 
            <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<netui:span value="${bundle.web['selecttest.subtestOptions.message3']}"/>
            <netui:textBox dataSource="actionForm.testAdmin.accessCode" maxlength="32" onKeyPress="return constrainEnterKeyEvent(event);"/>
            </c:if>   
            <br/>
            
            <netui:radioButtonOption value="true" onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'changeHasBreakToYes', 'subtestDetailsAnchor');"><netui:span value="${bundle.web['selecttest.label.yes']}"/></netui:radioButtonOption>
            <c:if test="${hasBreak}"> 
            <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<netui:span value="${bundle.web['selecttest.subtestOptions.message4']}"/>
            <netui:hidden dataSource="actionForm.testAdmin.accessCode"/>   
            </c:if>   
        </netui:radioButtonGroup>
        </p>
</c:if>

<!--Add the logic including section of Randomized Distractor Allowable -->
<netui-data:getData resultId="sessionRanodmization" value="${actionForm.hasSessionRandomizedDistractor}"/>
<netui-data:getData resultId="isChangeProduct" value="${actionForm.productChanged}"/>

 
<c:if test="${isChangeProduct}">

	<c:if test="${sessionRanodmization}">
   
		<c:if test="${rdAllow == 'N'  }">
        
			<p><netui:content value="${bundle.web['selecttest.rdOptions.message']}"/>
			<br/><netui:radioButtonGroup dataSource="actionForm.testAdmin.isRandomize" defaultValue="N">

				<netui:radioButtonOption value="N">
					<netui:span value="${bundle.web['selecttest.label.no']}"/>
				</netui:radioButtonOption>
				<br />
				<netui:radioButtonOption value="Y">
					<netui:span value="${bundle.web['selecttest.label.yes']}"/>
				</netui:radioButtonOption>
				<br />

			</netui:radioButtonGroup></p>
		</c:if>

		<c:if test="${rdAllow == 'Y'}">

			<p><netui:content value="${bundle.web['selecttest.rdOptions.message']}"/>
			<br/><netui:radioButtonGroup dataSource="actionForm.testAdmin.isRandomize" defaultValue="Y">

				<netui:radioButtonOption value="N">
					<netui:span value="${bundle.web['selecttest.label.no']}"/>
				</netui:radioButtonOption>
				<br />
				<netui:radioButtonOption value="Y">
					<netui:span value="${bundle.web['selecttest.label.yes']}"/>
				</netui:radioButtonOption>
				<br />

			</netui:radioButtonGroup></p>
		</c:if>
        
	</c:if>
</c:if>



<c:if test="${!isChangeProduct}">

	<c:if test="${sessionRanodmization}">

		<c:if test="${rdAllow == 'N'  }">

			<p><netui:content value="${bundle.web['selecttest.rdOptions.message']}"/>
			<br/><netui:radioButtonGroup dataSource="actionForm.testAdmin.isRandomize" defaultValue="N">

				<netui:radioButtonOption value="N">
					<netui:span value="${bundle.web['selecttest.label.no']}"/>
				</netui:radioButtonOption>
				<br />
				<netui:radioButtonOption value="Y">
					<netui:span value="${bundle.web['selecttest.label.yes']}"/>
				</netui:radioButtonOption>
				<br />

			</netui:radioButtonGroup></p>
		</c:if>

		<c:if test="${rdAllow == 'Y'}">

			<p><netui:content value="${bundle.web['selecttest.rdOptions.message']}"/>
			<br/><netui:radioButtonGroup dataSource="actionForm.testAdmin.isRandomize" defaultValue="Y">

				<netui:radioButtonOption value="N">
					<netui:span value="${bundle.web['selecttest.label.no']}"/>
				</netui:radioButtonOption>
				<br />
				<netui:radioButtonOption value="Y">
					<netui:span value="${bundle.web['selecttest.label.yes']}"/>
				</netui:radioButtonOption>
				<br />

			</netui:radioButtonGroup></p>
		</c:if>
		<c:if test="${rdAllow == null ||  rdAllow == ''}">

			<p><netui:content value="${bundle.web['selecttest.rdOptions.message']}"/>
			<br/><netui:radioButtonGroup dataSource="actionForm.testAdmin.isRandomize" defaultValue="Y">

				<netui:radioButtonOption value="N">
					<netui:span value="${bundle.web['selecttest.label.no']}"/>
				</netui:radioButtonOption>
				<br />
				<netui:radioButtonOption value="Y">
					<netui:span value="${bundle.web['selecttest.label.yes']}"/>
				</netui:radioButtonOption>
				<br />

			</netui:radioButtonGroup></p>


		</c:if>
	</c:if>
</c:if>


<!--End for Randomized Distractor -->
        



<!-- ******************************************************************************** -->
<!-- Test Options -->
<!-- ******************************************************************************** -->
<!-- TABE -->
<!-- Multiple subtest -->
<c:if test="${isTabeProduct}">     
    <c:if test="${productType == 'tabeBatterySurveyProductType'}">     
    <br/>
    <h4><netui:span value="Locator Test"/></h4>
    <p>Select the checkbox below to include the Locator Test. The student's performance on the Locator Test determines the difficulty level of the other subtests.</p>
    <table class="sortable">
        <tr class="sortable">
            <th class="sortable alignCenter" height="25" width="60"><netui:span value="Select"/></th>
            <th class="sortable alignLeft" height="25" width="*">&nbsp;<netui:span value="${bundle.web['common.column.subtestName']}"/></th>
            <th class="sortable alignLeft" height="25" width="100">&nbsp;<netui:span value="${bundle.web['common.column.duration']}"/></th>
            <c:if test="${hasBreak}"> 
            <th class="sortable alignLeft" height="25" width="120">&nbsp;<netui:span value="${bundle.web['common.column.testAccessCode']}"/></th>
            </c:if>   
        </tr>
        <tr class="sortable"> 
            <td class="sortable alignCenter" width="60">
                <netui:checkBox dataSource="actionForm.autoLocator" onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'toogleAutoLocator', 'subtestDetailsAnchor');"/>
            </td>
            
            <td class="sortable alignLeft" width="*"><netui:label value="${pageFlow.locatorSubtest.subtestName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft" width="100"><netui:label  value="${pageFlow.locatorSubtest.duration}" defaultValue="&nbsp;"/></td>
            <c:if test="${hasBreak}"> 
                <c:if test="${autoLocator}"> 
                    <td class="sortable alignLeft" width="120"><netui:textBox dataSource="pageFlow.locatorSubtest.testAccessCode" maxlength="32" onKeyPress="return constrainEnterKeyEvent(event);"/></td>
                </c:if>   
                <c:if test="${! autoLocator}"> 
                    <td class="sortable alignLeft" width="120"><netui:textBox dataSource="{pageFlow.blankInputString}" defaultValue="" maxlength="32" onKeyPress="return constrainEnterKeyEvent(event);"/></td>
                </c:if>   
            </c:if>   
        </tr>
    </table>
    <br/>
    </c:if>
</c:if>







<!-- ******************************************************************************** -->
<!-- Subtest Details -->
<!-- ******************************************************************************** -->
<c:if test="${hasMultipleSubtests}"> 
<a name="subtestDetailsAnchor"><!-- subtestDetailsAnchor --></a>    
<br/><p>        
<h4><netui:span value="${bundle.web['selecttest.subtestDetails.title']}"/></h4>

<c:if test="${isTabeProduct}">             
    <p>Click the Modify Test button below to select subtests, difficulty level, or subtest order.</p>
</c:if>
    
    
<netui-data:getData resultId="subtestWarningMessage" value="${actionForm.subtestValidationMessage}"/>
<c:if test="${subtestWarningMessage != null}">
    <ctb:message title="" style="alertMessage">
        <netui:content value="${actionForm.subtestValidationMessage}"/>
    </ctb:message><br/>
</c:if>
    
    
<table class="sortable">
<netui-data:repeater dataSource="pageFlow.defaultSubtests">
<netui-data:repeaterHeader>
        

<!-- TABE -->
<c:if test="${isTabeProduct}">             
        <tr class="sortable">
            <td class="sortableControls" colspan="4">
                &nbsp;<netui:button type="submit" value="Modify Test" action="toModifySubtests"/>
            </td>
        </tr>
</c:if>

        
        <tr class="sortable">
            <th class="sortable alignCenter" height="25" width="60">&nbsp;<netui:span value="${bundle.web['common.column.sequence']}"/></th>
            <th class="sortable alignLeft" height="25" width="*">&nbsp;<netui:span value="${bundle.web['common.column.subtestName']}"/></th>
            <th class="sortable alignLeft" height="25" width="100">&nbsp;<netui:span value="${bundle.web['common.column.duration']}"/></th>
            <c:if test="${hasBreak}"> 
            <th class="sortable alignLeft" height="25" width="120">&nbsp;<netui:span value="${bundle.web['common.column.testAccessCode']}"/></th>
            </c:if>   
        </tr>
        
        </netui-data:repeaterHeader>
        
        <netui-data:repeaterItem>
        <tr class="sortable">
            <td class="sortable alignCenter" width="60"><netui:span value="${container.item.sequence}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft" width="*"><netui:span value="${container.item.subtestName}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignLeft" width="100"><netui:span value="${container.item.duration}" defaultValue="&nbsp;"/></td>
            <c:if test="${hasBreak}"> 
            <td class="sortable alignLeft" width="120"><netui:textBox dataSource="container.item.testAccessCode" maxlength="32" onKeyPress="return constrainEnterKeyEvent(event);"/></td>
            </c:if>   
        </tr>
        </netui-data:repeaterItem>
    
        </netui-data:repeater>
        </table>
        
</c:if>   
    
</c:if> <!-- hideTestOptions -->
      
<br/>


<!-- ******************************************************************************** -->
<!-- Buttons -->
<!-- ******************************************************************************** -->
<c:if test="${action=='edit'}">    
    <netui:button styleClass="button" type="submit" tagId="Next" value="${bundle.web['common.button.ok']}" action="selectTestDone" disabled="${requestScope.disableNextButton}"/>
</c:if>
<c:if test="${action!='edit'}">    
    <netui:button styleClass="button" type="submit" tagId="Next" value="${bundle.web['common.button.next']}" action="selectTestDone" disabled="${requestScope.disableNextButton}"/>
</c:if>

<c:if test="${showCancel}">
    <netui:button styleClass="button" type="submit" value="${bundle.web['common.button.cancel']}" action="selectTestCancel"/>
</c:if>   
</netui:form>
        
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
