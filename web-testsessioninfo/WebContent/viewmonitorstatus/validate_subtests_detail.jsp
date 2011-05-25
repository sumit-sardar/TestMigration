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
    <netui-template:setAttribute name="title" value="${bundle.web['ValidateSubtestDetails.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.subtestValidation']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<h1><netui:content value="${bundle.web['ValidateSubtestDetails.title']}"/>: <netui:span value="${requestScope.studentName}"/></h1>
<p><netui:content value="${bundle.web['ValidateSubtestDetails.title.message']}"/></p>

<netui:form action="to_validate_subtests_detail">
    <netui:hidden dataSource="actionForm.actionElement"/>
    <netui:hidden dataSource="actionForm.currentAction"/>
    <netui:hidden dataSource="actionForm.testRosterId"/>
    <netui:hidden dataSource="actionForm.studentId"/>

    <netui-data:getData resultId="isTabeSession" value="${requestScope.isTabeSession}"/>
    <netui-data:getData resultId="isShowScores" value="${requestScope.isShowScores}"/>
        <netui-data:getData resultId="isLaslinkSession" value="${requestScope.isLaslinkSession}"/>
    <netui-data:getData resultId="isShowToggleForCustomerFlag" value="${pageFlow.setCustomerFlagToogleButton}"/>

<%
    String numberColumn = (String)request.getAttribute("numberColumn");
    int value = Integer.valueOf(numberColumn).intValue() + 1;
    String colSpan = Integer.toString(value);
%>

<table class="transparent" width="100%">
<tr class="transparent">
    <td class="transparent" width="140"><netui:span value="${bundle.web['ViewSubtestDetails.text.loginName']}"/>:</td>
    <td class="transparent" width="*">
        <div class="formValueLarge"><netui:span value="${requestScope.loginName}" styleClass="formValueLarge"/></div>    
    </td>
</tr>
<tr class="transparent">
    <td class="transparent" width="140"><netui:span value="${bundle.web['ViewSubtestDetails.text.password']}"/>:</td>
    <td class="transparent" width="*">
        <div class="formValueLarge"><netui:span value="${requestScope.password}" styleClass="formValueLarge"/></div>    
    </td>
</tr>
<tr class="transparent">
    <td class="transparent" width="140"><netui:span value="${bundle.web['ViewSubtestDetails.text.testSessionName']}"/>:</td>
    <td class="transparent" width="*">
        <div class="formValueLarge"><netui:span value="${requestScope.testSessionName}" styleClass="formValueLarge"/></div>    
    </td>
</tr>
<tr class="transparent">
    <td class="transparent" width="140"><netui:span value="${bundle.web['ViewSubtestDetails.text.testName']}"/>:</td>
    <td class="transparent" width="*">
        <div class="formValueLarge"><netui:span value="${requestScope.testName}" styleClass="formValueLarge"/></div>    
    </td>
</tr>
<c:if test="${testGrade != null}"> 
<tr class="transparent">
    <td class="transparent" width="140"><netui:span value="${bundle.web['ViewSubtestDetails.text.testGrade']}"/>:</td>
    <td class="transparent" width="*">
        <div class="formValueLarge"><netui:span value="${requestScope.testGrade}" styleClass="formValueLarge"/></div>    
    </td>
</tr>
</c:if>
<c:if test="${testLevel != null}"> 
<tr class="transparent">
    <td class="transparent" width="140"><netui:span value="${bundle.web['ViewSubtestDetails.text.testLevel']}"/>:</td>
    <td class="transparent" width="*">
        <div class="formValueLarge"><netui:span value="${requestScope.testLevel}" styleClass="formValueLarge"/></div>    
    </td>
</tr>
</c:if>
<tr class="transparent">
    <td class="transparent" width="140"><netui:span value="${bundle.web['ViewSubtestDetails.text.testStatus']}"/>:</td>
    <td class="transparent" width="*">
        <div class="formValueLarge"><netui:span value="${requestScope.testCompletionStatus}" styleClass="formValueLarge"/></div>    
    </td>
</tr>
</table>
<br/>


<table class="transparent" width="100%">
        <tr class="sortable">
            <td class="sortableGrey" colspan="<%= colSpan %>">
                <netui:button type="button" tagId="toggleSubtestValidation" value="Toggle Validation" onClick="setElementValueAndSubmit('{actionForm.currentAction}', 'toggleSubtestValidation', this);" disabled="${requestScope.disableToogleButton}"/>
                  <c:if test="${isLaslinkSession == 'true'}"> 
                 	<netui:button type="button" tagId="toggleSubtestExemption" value="Toggle Exemption" onClick="setElementValueAndSubmit('{actionForm.currentAction}', 'toggleSubtestExemption', this);" disabled="${requestScope.disableToogleButton}"/>           
                  	<netui:button type="button" tagId="toggleSubtestAbsent" value="Toggle Absent" onClick="setElementValueAndSubmit('{actionForm.currentAction}', 'toggleSubtestAbsent', this);" disabled="${requestScope.disableToogleButton}"/>
                </c:if>             
            	<c:if test="${isShowToggleForCustomerFlag == 'true'}">                 
                	<netui:button type="button" tagId="toggleSubtestCustom" value="Toggle Custom" onClick="setElementValueAndSubmit('{actionForm.currentAction}', 'toggleSubtestCustom', this);" disabled="${requestScope.disableToogleButton}"/>           
            	</c:if>
            </td>
        </tr>
<netui-data:repeater dataSource="requestScope.studentStatusSubtests">
    <netui-data:repeaterHeader>
        <tr class="sortable">
            <th class="sortable alignCenter" style="width:50px;" nowrap>
                <netui:content value="${bundle.web['common.column.select']}"/>
            </th>                      
            <th class="sortable alignLeft" height="25" width="*">
                &nbsp;&nbsp;<netui:span value="${bundle.web['ViewSubtestDetails.text.subtestName']}"/>
            </th>
			<c:if test="${isTabeSession == 'true'}"> 
	            <th class="sortable alignCenter" height="25" width="6%">
	                &nbsp;&nbsp;<netui:span value="${bundle.web['ViewSubtestDetails.text.subtestLevel']}"/>
	            </th>
			</c:if>            
            <th class="sortable alignCenter" height="25" width="10%">
                &nbsp;&nbsp;<netui:span value="${bundle.web['ViewSubtestDetails.text.validationStatus']}"/>
            </th>
            <c:if test="${isLaslinkSession == 'true'}"> 
	            <th class="sortable alignCenter" height="25" width="10%">
	            	&nbsp;&nbsp;<netui:span value="${bundle.web['common.column.testExemptions']}"/>
	             </th>
	            <th class="sortable alignCenter" height="25" width="10%">
	            	&nbsp;&nbsp;<netui:span value="${bundle.web['common.column.Absent']}"/>
	            </th> 
            </c:if>    
			<c:if test="${isShowToggleForCustomerFlag == 'true'}">                             
	            <th class="sortable alignCenter" height="25" width="10%">
	                &nbsp;&nbsp;<netui:span value="${bundle.web['ViewSubtestDetails.text.customStatus']}"/>
	            </th>
			</c:if>            
            <th class="sortable alignCenter" height="25" width="10%">
                &nbsp;&nbsp;<netui:span value="${bundle.web['ViewSubtestDetails.text.subtestStatus']}"/>
            </th>
            <th class="sortable alignCenter" height="25" width="10%">
                &nbsp;&nbsp;<netui:span value="${bundle.web['ViewSubtestDetails.text.startDate']}"/>
            </th>
            <th class="sortable alignCenter" height="25" width="10%">
                &nbsp;&nbsp;<netui:span value="${bundle.web['ViewSubtestDetails.text.completionDate']}"/>
            </th>
<c:if test="${isShowScores == 'true'}">             
            <th class="sortable alignCenter" height="25" width="10%">
                &nbsp;&nbsp;<netui:span value="Total Items"/>
            </th>
            <th class="sortable alignCenter" height="25" width="10%">
                &nbsp;&nbsp;<netui:span value="Items Correct"/>
            </th>
            <th class="sortable alignCenter" height="25" width="10%">
                &nbsp;&nbsp;<netui:span value="Items to be Scored"/>
            </th>
            </c:if>
        </tr>
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>    
        <netui-data:getData resultId="itemSetType" value="${container.item.itemSetType}"/>
    <c:if test="${itemSetType == 'TS'}">        
        <tr class="sortable">
            <td class="sortable alignCenter">&nbsp;</td>        
            <td class="sortable alignLeft" colspan="<%= numberColumn %>">
                <netui:span value="${container.item.subtestName}" defaultValue="&nbsp;"/>&nbsp;&nbsp;
                (test access code: <netui:span value="${container.item.accessCode}" defaultValue="&nbsp;"/>)
            </td>            
        </tr>            
    </c:if>
    <c:if test="${itemSetType != 'TS'}">      
        <tr class="sortable">
        	<td class="sortable alignCenter">
                <netui:checkBoxGroup dataSource="actionForm.selectedItemSetIds">&nbsp;
                    <netui:checkBoxOption value="${container.item.itemSetId}" onClick="enableDeselectAllTestButton('toggleSubtestValidation'); enableDeselectAllTestButton('toggleSubtestCustom'); enableDeselectAllTestButton('toggleSubtestExemption'); enableDeselectAllTestButton('toggleSubtestAbsent');">&nbsp;
                    </netui:checkBoxOption>
                </netui:checkBoxGroup>
            </td>
        
            <td class="sortable alignLeft"><netui:content value="${container.item.subtestName}" defaultValue="&nbsp;"/></td>
			<c:if test="${isTabeSession == 'true'}"> 
           		<td class="sortable alignCenter"><netui:span value="${container.item.level}" defaultValue="&nbsp;"/></td>
			</c:if>            
            <td class="sortable alignCenter">
                <ctb:switch dataSource="${container.item.validationStatus}">
                    <ctb:case value="Valid"><netui:span value="${container.item.validationStatus}" defaultValue="&nbsp;"/></ctb:case>
                    <ctb:case value="Invalid"><font color="red"><netui:span value="${container.item.validationStatus}" defaultValue="&nbsp;"/></font></ctb:case>
                    <ctb:case value="Partially Invalid"><font color="red"><netui:span value="${container.item.validationStatus}" defaultValue="&nbsp;"/></font></ctb:case>
                </ctb:switch>                
            </td>
             <c:if test="${isLaslinkSession == 'true'}">  
	 			<td class="sortable alignCenter">
	                <ctb:switch dataSource="${container.item.testExemptions}">
	                    <ctb:case value="Y"><netui:span value="Yes" defaultValue="&nbsp;"/></ctb:case>
	                    <ctb:case value="N"><netui:span value="No" defaultValue="&nbsp;"/></ctb:case>
	                </ctb:switch>                
	            </td>
	            <td class="sortable alignCenter">
	                <ctb:switch dataSource="${container.item.absent}">
	                    <ctb:case value="Y"><netui:span value="Yes" defaultValue="&nbsp;"/></ctb:case>
	                    <ctb:case value="N"><netui:span value="No" defaultValue="&nbsp;"/></ctb:case>
	                 </ctb:switch>                
	            </td>
 			</c:if>
			<c:if test="${isShowToggleForCustomerFlag == 'true'}">                                         
            	<td class="sortable alignCenter"><netui:span value="${container.item.customStatus}" defaultValue="&nbsp;"/></td>
			</c:if>            
            <td class="sortable alignCenter"><netui:span value="${container.item.completionStatus}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter"><netui:span value="${container.item.startDate}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter"><netui:span value="${container.item.endDate}" defaultValue="&nbsp;"/></td>
			<c:if test="${isShowScores == 'true'}">             
            	<td class="sortable alignCenter"><netui:span value="${container.item.displayMaxScore}" defaultValue="&nbsp;"/></td>
            	<td class="sortable alignCenter"><netui:span value="${container.item.displayRawScore}" defaultValue="&nbsp;"/></td>
           		<td class="sortable alignCenter"><netui:span value="${container.item.displayUnScored}" defaultValue="&nbsp;"/></td>
			</c:if>  
 			
        </tr>            
    </c:if>
    </netui-data:repeaterItem>
</netui-data:repeater>
</table>

<p>
<br>

    <netui:button type="button" tagId="done" value="Done" onClick="setElementValueAndSubmit('{actionForm.currentAction}', 'done', this);"/>           
</p>

</netui:form>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
        
    </netui-template:section>
</netui-template:template>
