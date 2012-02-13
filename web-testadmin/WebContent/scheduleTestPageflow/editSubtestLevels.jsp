<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
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
<netui-template:setAttribute name="title" value="${bundle.web['selecttest.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.scheduleTestSessionSelectTest']}"/>
<netui-template:section name="bodySection">

<!--End of Change MQC defect  55837 -->

<% 
    List allSubtests = (List)request.getAttribute("allSubtests");
    List availableSubtests = (List)request.getAttribute("availableSubtests");
    List selectedSubtests = (List)request.getAttribute("selectedSubtests");    
	List levelOptions = (List)request.getAttribute("levelOptions");

    Boolean showLevel = (Boolean)request.getAttribute("showLevel");
    
    String broswerType = "msie";    
    String userAgent = request.getHeader("User-Agent").toLowerCase();
    if (userAgent.indexOf("firefox") != -1) {
        broswerType = "firefox";
    }
    if (userAgent.indexOf("mac") != -1) {
        broswerType = "mac";
    }

    String subtestName = (String)request.getAttribute("subtestName");
    String levelName = (String)request.getAttribute("levelName");
    Boolean checked = (Boolean)request.getAttribute("checked");
    
    String locatorSessionInfo = (String)request.getAttribute("locatorSessionInfo");        
    
%>

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="Edit Student: Modify Test"/>
</h1>
<p>
<c:if test="${! isTabeAdaptiveProduct}">     
    <netui:content value="You may change the order in which subtests will be presented for testing, remove subtests from the test, or re-select them from the Available Subtests list. For tests that do not include the Locator Test, you may also select the difficulty level for each subtest."/><br/>
</c:if>    
<c:if test="${isTabeAdaptiveProduct}">     
    <netui:content value="You may change the order in which subtests will be presented for testing, remove subtests from the test, or re-select them from the Available Subtests list."/><br/>
</c:if>    
</p>





<!-- start form -->
<netui:form action="returnFromEditSubtestLevels">

<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/>
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/>
<netui:hidden dataSource="actionForm.testAdmin.testName"/>
<netui:hidden dataSource="actionForm.testAdmin.sessionName"/>
<netui:hidden dataSource="actionForm.testAdmin.level"/>   
<netui:hidden dataSource="actionForm.testAdmin.location"/>   
<netui:hidden dataSource="actionForm.testAdmin.accessCode"/>   
<netui:hidden dataSource="actionForm.testAdmin.timeZone"/>   
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

<netui:hidden dataSource="actionForm.orgStatePathList.maxPageRequested"/>

<netui:hidden dataSource="actionForm.selectedStudentId"/>   
<netui:hidden dataSource="actionForm.selectedOrgNodeId"/>   

<netui:hidden dataSource="actionForm.studentStatePathList.sortColumn"/>   
<netui:hidden dataSource="actionForm.studentStatePathList.sortOrderBy"/>   
<netui:hidden dataSource="actionForm.studentStatePathList.pageRequested"/>
<netui:hidden dataSource="actionForm.studentStatePathList.maxPageRequested"/>
<!--License-->
<netui:hidden dataSource="actionForm.licenseAvailable"/>
<netui:hidden dataSource="actionForm.licensePercentage"/>
<netui:hidden dataSource="actionForm.testAdmin.productId"/>

<input type="hidden" id="validateTest" name="validateTest" value="noValidateTest" />

<netui-data:getData resultId="isTabeAdaptiveProduct" value="${requestScope.isTabeAdaptiveProduct}"/>
<netui-data:getData resultId="isLasLinksProduct" value="${requestScope.isLasLinksProduct}"/>

<p>
<table class="transparent">
<tr>
    <td class="transparent" width="100"><netui:span value="Session Name: "/></td>
    <td class="transparent"><netui:span value="${requestScope.sessionName}"/></td>
<tr/>
<tr>
    <td class="transparent" width="100"><netui:span value="Student Name: "/></td>
    <td class="transparent"><netui:span value="${requestScope.studentName}"/></td>
<tr/>
<c:if test="${locatorSessionInfo != null}">
<tr>
    <td class="transparent-top" width="100"><netui:span value="Locator Session:"/></td>
    <td class="transparent"><netui:span value="${requestScope.locatorSessionInfo}"/></td>
</tr>
</c:if>
</table>
</p>


<jsp:include page="/scheduleTestPageflow/show_message.jsp" />



<!-- Locator Test-->
<a name="autoLocator"><!-- autoLocator --></a>    
<c:if test="${!isTabeAdaptiveProduct && !isLasLinksProduct}">     
<p>
<h4><netui:span value="Locator Test"/></h4>
<p>Select the checkbox below to include the Locator Test. Each student's performance on the Locator Test determines the difficulty level of the other subtests for that student.</p>

<ctbweb:autoLocatorTag subtestName = "<%= subtestName %>" 
                       levelName = "<%= levelName %>" 
                       showLevel = "<%= Boolean.FALSE %>" 
                       checked = "<%= checked %>" />
</p>
</c:if>


<!-- subtestOrderList custom tag -->
<p>
<ctbweb:subtestOrderList allSubtests = "<%= allSubtests %>" 
                         availableSubtests = "<%= availableSubtests %>"
                         selectedSubtests = "<%= selectedSubtests %>"
                         levelOptions = "<%= levelOptions %>" 
                         showLevel = "<%= showLevel %>" 
                         broswerType = "<%= broswerType %>" />
</p>


<p>
    <netui:button type="button" value="Cancel" onClick="setElementValueAndSubmit('currentAction', 'cancelEditSubtests');"/>&nbsp;
    <netui:button type="button" value="Done" onClick="setElementValueAndSubmit('currentAction', 'doneEditSubtests');"/>&nbsp;
</p>

                
</netui:form>
        
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
