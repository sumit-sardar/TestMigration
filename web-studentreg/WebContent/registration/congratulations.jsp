<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['congratulations.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.testTicket']}"/>
<netui-template:section name="bodySection">

<% 
    String testAdminId = (String)request.getAttribute("testAdminId");
    String orgNodeId = (String)request.getAttribute("orgNodeId");
    String studentId = (String)request.getAttribute("studentId");
    
    Boolean hasLocatorSubtest = (Boolean)request.getAttribute("hasLocatorSubtest");    
    Boolean isLocatorTest = (Boolean)request.getAttribute("isLocatorTest");    
%>

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<!--Start change for licnese--> 
<table width="100%" cellpadding="0" cellspacing="0" class="transparent">
    <tr>
        <td nowrap="">
            <h1>
             <netui:content value="Register Student: Congratulations!"/>
            </h1>
        </td>
    
        <td width="65%" class="transparent"></td>
        <td width="100%" rowspan="2" align="right" valign="top">
        	&nbsp;
        </td>
    
        <td rowspan="2">
            <table width="25"><tr><td></td></tr></table>
        </td>
    </tr>
    <!--change for licnese-->
    <tr>   
        <td width="65%" class="transparent">
            <p>
                <netui:content value="You've successfully registered this student. Review the student's assignments for accuracy."/><br/>
            </p>
        </td>
        <td width="62%" class="transparent"></td> 
    </tr>
</table>
<!--End of change for licnese-->




<!-- start form -->
<netui:form action="congratulations">

<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>

<netui:hidden dataSource="actionForm.selectedTab"/>

<netui:hidden dataSource="actionForm.studentMaxPage"/> 
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/> 

<netui-data:getData resultId="showAccessCode" value="${requestScope.showAccessCode}"/>

<input type="hidden" id="testAdminId" name="testAdminId" value="<%= testAdminId %>" />
<input type="hidden" id="orgNodeId" name="orgNodeId" value="<%= orgNodeId %>" />
<input type="hidden" id="studentId" name="studentId" value="<%= studentId %>" />


<netui-data:getData resultId="autoLocator" value="${actionForm.autoLocator}"/>
<netui-data:getData resultId="enforceBreak" value="${requestScope.enforceBreak}"/>

<!-- message -->
<jsp:include page="/registration/show_message.jsp" />


<!-- student section -->
<p>
<ctb:showHideSection sectionId="studentSectionVisible" sectionTitle="Student" sectionVisible="actionForm.studentSectionVisible">
<table class="simple" width="100%">
<tr class="transparent">
<td class="transparent" width="50%">
    <table class="transparent">
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Student name:"/></td>
            <td class="transparent" width="*"><netui:span value="${requestScope.studentName}"/></td>
        </tr>
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Test access code:"/></td>
<c:if test="${enforceBreak == 'No'}">                                            
            <td class="transparent" width="*"><netui:span value="${requestScope.testAccessCode}"/></td>
</c:if>            
<c:if test="${enforceBreak == 'Yes'}">                                            
            <td class="transparent" width="*"><netui:span value=""/></td>
</c:if>            
        </tr>
    </table>
</td>
<td class="transparent" width="50%">
    <table class="transparent">
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Login ID:"/></td>
            <td class="transparent" width="*"><netui:span value="${requestScope.loginName}"/></td>
        </tr>
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Password:"/></td>
            <td class="transparent" width="*"><netui:span value="${requestScope.password}"/></td>
        </tr>
    </table>
</td>
</tr>
</table>
</ctb:showHideSection>
<p/>




<!-- test section -->
<p>
<ctb:showHideSection sectionId="testSectionVisible" sectionTitle="Test Session" sectionVisible="actionForm.testSectionVisible">
<table class="simple" width="100%">
<tr class="transparent">
<td class="transparent" width="50%">
    <table class="transparent">
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Test name:"/></td>
            <td class="transparent" width="*"><netui:span value="${requestScope.testName}"/></td>
        </tr>
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Organization:"/></td>
            <td class="transparent" width="*"><netui:span value="${requestScope.creatorOrgNodeName}"/></td>
        </tr>
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Start date:"/></td>
            <td class="transparent" width="*"><netui:span value="${requestScope.startDate}"/></td>
        </tr>
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Login start time:"/></td>
            <td class="transparent" width="*"><netui:span value="${requestScope.startTime}"/></td>
        </tr>
<c:if test="${! isLocatorTest}">             
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Locator Test:"/></td>
            <td class="transparent" width="*"><netui:span value="${actionForm.autoLocatorDisplay}"/></td>
        </tr>
</c:if>        
    </table>
</td>
<td class="transparent" width="50%">
    <table class="transparent">
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Test session name:"/></td>
            <td class="transparent" width="*"><netui:span value="${requestScope.testAdminName}"/></td>
        </tr>
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Test Session Id:"/></td>
            <td class="transparent" width="*"><netui:span value="${requestScope.sessionNumber}"/></td>
        </tr>
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="End date:"/></td>
            <td class="transparent" width="200"><netui:span value="${requestScope.endDate}"/></td>
        </tr>
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Login end time:"/></td>
            <td class="transparent" width="200"><netui:span value="${requestScope.endTime}"/></td>
        </tr>
<c:if test="${! isLocatorTest}">             
        <tr class="transparent">
            <td class="transparent" width="120"><netui:span value="Allows Breaks:"/></td>
            <td class="transparent" width="200"><netui:span value="${requestScope.enforceBreak}"/></td>
        </tr>
</c:if>        
    </table>
</td>
</tr>
</table>
</ctb:showHideSection>
<p/>





<!-- test structure section -->
<p>
<ctb:showHideSection sectionId="testStructureSectionVisible" sectionTitle="Test Structure" sectionVisible="actionForm.testStructureSectionVisible">

<c:if test="${hasLocatorSubtest != null}">     
<c:if test="${autoLocator == 'true'}">     
                           
<table class="sortable">
    <tr class="sortable">
        <th class="sortable alignLeft" height="25" width="*">&nbsp;<span>Subtest</span></th>
<c:if test="${enforceBreak == 'Yes'}">                                
        <th class="sortable alignCenter" height="25">&nbsp;<span>Test Access Code</span></th>
</c:if>                  
    </tr>
    <tr class="sortable">
        <td class="sortable alignLeft" width="*"><netui:span value="${pageFlow.locatorSubtest.subtestName}"/></td>
<c:if test="${enforceBreak == 'Yes'}">                                
        <td class="sortable alignCenter" width="120"><netui:span value="${pageFlow.locatorSubtest.testAccessCode}"/></td>
</c:if>                  
    </tr>
</table>
<br/>

</c:if>
</c:if>




<table class="sortable">        
<netui-data:repeater dataSource="requestScope.selectedSubtestList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <th class="sortable alignCenter" height="25"><netui:content value="Order"/></th>                
        <th class="sortable alignLeft" height="25">&nbsp;&nbsp;<netui:content value="Subtests"/></th>   
<c:if test="${enforceBreak == 'Yes'}">                                
        <th class="sortable alignCenter" height="25"><netui:content value="Test Access Code"/></th>      
</c:if>                  
<c:if test="${! isLocatorTest}">             
<c:if test="${autoLocator == 'false'}">                                
        <th class="sortable alignCenter" height="25"><netui:content value="Level"/></th>      
</c:if>                  
</c:if>                  
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter" width="50">
            <netui:span value="${container.item.sequence}"/>
        </td>        
        <td class="sortable" width="*">
            <netui:span value="${container.item.subtestName}"/>
        </td>
<c:if test="${enforceBreak == 'Yes'}">                                
        <td class="sortable alignCenter" width="120">
            <netui:span value="${container.item.testAccessCode}"/>
        </td>        
</c:if>                  
<c:if test="${! isLocatorTest}">             
<c:if test="${autoLocator == 'false'}">                                
        <td class="sortable alignCenter" width="50">
            <netui:span value="${container.item.level}"/>
        </td>        
</c:if>                  
</c:if>                  
    </tr>
    
    </netui-data:repeaterItem>
</netui-data:repeater>
</table>
</ctb:showHideSection>
<p/>



<!-- report -->
<c:if test="${showAccessCode}"> 
<p>
<netui:content value="Do you want to print the Test Access Code on your Individual or Multiple Test Tickets?"/><br>
<input type="radio" id="allow" name="individualAccess" value="Yes" onclick="accessCode()">Yes</input>
<input type="radio" id="deny" name="individualAccess" value="No"  onclick="accessCode()" checked="checked">No</input>
</p>
</c:if>
<p align="right">
    <netui:anchor href="#" onClick="return openTestTicketIndividual(this, document.getElementById('testAdminId').value, document.getElementById('orgNodeId').value, document.getElementById('studentId').value);">Individual Test Ticket</netui:anchor>
    &nbsp;<img src="/StudentRegistrationWeb/resources/images/misc/logo_pdf.gif" border="0">
</p>



<!-- buttons -->
<p>
    <netui:button type="submit" value="Register Another Student" action="enterMoreStudent" style="width=180px" disabled="${requestScope.DisableRegisterAnotherStudent}"/>&nbsp;
<c:if test="${hideModifyTestButton == null}">             
    <netui:button type="submit" value="Modify Test" action="toModifyTestFromCongratulation"/>&nbsp;
</c:if>                  
    <netui:button type="submit" value="Finish" action="gotoHomePage"/>
</p>

</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
