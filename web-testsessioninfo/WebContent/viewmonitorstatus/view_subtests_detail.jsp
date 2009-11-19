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
    <netui-template:setAttribute name="title" value="${bundle.web['ViewSubtestDetails.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.viewStatus']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<h1><netui:content value="${bundle.web['ViewSubtestDetails.title']}"/>: <netui:span value="${requestScope.studentName}"/></h1>
<p><netui:content value="${bundle.web['ViewSubtestDetails.title.message']}"/></p>
<br/>

<netui:form action="from_view_subtests_detail">
    <netui:hidden dataSource="actionForm.actionElement"/>
    <netui:hidden dataSource="actionForm.currentAction"/>

    <netui-data:getData resultId="isTabeSession" value="${requestScope.isTabeSession}"/>
    <netui-data:getData resultId="isShowScores" value="${requestScope.isShowScores}"/>

<%
    String numberColumn = (String)request.getAttribute("numberColumn");
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
<br/><br/>


<h2><netui:content value="${bundle.web['ViewSubtestDetails.subtestDetails.title']}"/></h2>

<table class="transparent" width="100%">
<netui-data:repeater dataSource="requestScope.studentStatusSubtests">
    <netui-data:repeaterHeader>
        <tr class="sortable">
            <th class="sortable alignLeft" height="25" width="*">
                &nbsp;&nbsp;<netui:span value="${bundle.web['ViewSubtestDetails.text.subtestName']}"/>
            </th>
<c:if test="${isTabeSession == 'true'}"> 
            <th class="sortable alignCenter" height="25" width="6%">
                &nbsp;&nbsp;<netui:span value="${bundle.web['ViewSubtestDetails.text.subtestLevel']}"/>
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
            <td class="sortable alignLeft" colspan="<%= numberColumn %>">
                <netui:span value="${container.item.subtestName}" defaultValue="&nbsp;"/>&nbsp;&nbsp;
                (test access code: <netui:span value="${container.item.accessCode}" defaultValue="&nbsp;"/>)
            </td>            
        </tr>            
    </c:if>
    <c:if test="${itemSetType != 'TS'}">        
        <tr class="sortable">
            <td class="sortable alignLeft"><netui:content value="${container.item.subtestName}" defaultValue="&nbsp;"/></td>
<c:if test="${isTabeSession == 'true'}"> 
            <td class="sortable alignCenter"><netui:span value="${container.item.level}" defaultValue="&nbsp;"/></td>
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
<br><netui:button type="button" value="${bundle.web['common.button.back']}" onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'none', 'tableAnchor', this);"/>           
</p>

</netui:form>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
        
    </netui-template:section>
</netui-template:template>
