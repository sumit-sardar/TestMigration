<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--<%@ taglib uri="c.tld" prefix="c"%> -->
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>


<!--Change MQC defect  55837 -->

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
<netui-template:setAttribute name="title" value="${bundle.web['selecttest.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.scheduleTestSessionSelectTest']}"/>
<netui-template:section name="bodySection">

<!--End of change for MQC defect  55837 -->

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
    
%>

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<netui-data:getData resultId="action" value="${pageFlow.action}"/>

<c:if test="${action=='edit'}">    
    <h1><netui:span value="Edit a Test Session: Modify Test"/></h1>
</c:if>
<c:if test="${action!='edit'}">    
    <h1><netui:span value="Schedule a Test Session: Modify Test"/></h1>
</c:if>
<p>
    <netui:content value="You may change the order in which the subtests will be presented for testing, remove subtests from the test, or re-select them from the Available Subtests list. <br/>For tests that do not include the Locator Test, you may also select the difficulty level for each subtest."/><br/>
</p>



<!-- start form -->
<netui:form action="modifySubtests">


<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/>
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.testAdmin.testName"/>
<netui:hidden dataSource="actionForm.testAdmin.level"/>
<netui:hidden dataSource="actionForm.testAdmin.sessionName"/>   
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
<netui:hidden dataSource="actionForm.studentStatePathList.sortColumn"/>
<netui:hidden dataSource="actionForm.studentStatePathList.sortOrderBy"/>

<netui:hidden dataSource="actionForm.testStatePathList.sortColumn"/>
<netui:hidden dataSource="actionForm.testStatePathList.sortOrderBy"/>
<netui:hidden dataSource="actionForm.testStatePathList.pageRequested"/>

<netui:hidden dataSource="actionForm.testAdmin.accessCode"/>   
<netui:hidden dataSource="actionForm.hasBreak"/>   

<netui:hidden dataSource="actionForm.selectedProductName"/>
<netui:hidden dataSource="actionForm.selectedTestId"/>

<netui:hidden dataSource="actionForm.autoLocator"/>
<netui:hidden dataSource="actionForm.testAdmin.isRandomize"/>

<!--License-->
<netui:hidden dataSource="actionForm.licenseAvailable"/>
<netui:hidden dataSource="actionForm.licensePercentage"/>
<netui:hidden dataSource="actionForm.testAdmin.productId"/>
 


<netui-data:getData resultId="errorMessage" value="${requestScope.errorMessage}"/>
<c:if test="${errorMessage != null}">
    <ctb:message title="Subtest Validation Failed." style="errorMessage">
        <netui:content value="${requestScope.errorMessage}"/>
    </ctb:message><br/>
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
    <netui:button type="button" value="Cancel" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'cancelEditSubtests', 'subtestDetailsAnchor');"/>&nbsp;
    <netui:button type="button" value="Done" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'doneEditSubtests', 'subtestDetailsAnchor');"/>&nbsp;
</p>

                
</netui:form>
        
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
