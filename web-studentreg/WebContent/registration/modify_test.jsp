<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['modifytest.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.modifyTest']}"/>
<netui-template:section name="bodySection">

<% 
    List allSubtests = (List)request.getAttribute("allSubtests");
    List availableSubtests = (List)request.getAttribute("availableSubtests");
    List selectedSubtests = (List)request.getAttribute("selectedSubtests");
    
	List levelOptions = (List)request.getAttribute("levelOptions");
    Boolean showLevel = (Boolean)request.getAttribute("showLevel");
    String showDropDown = (String)request.getAttribute("showDropDown");
    
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

    Boolean multipleSubtest = (Boolean)request.getAttribute("multipleSubtest");    
    
    Boolean hasLocatorSubtest = (Boolean)request.getAttribute("hasLocatorSubtest");    
    Boolean hideBackButton = (Boolean)request.getAttribute("hideBackButton");    
    Boolean isLocatorTest = (Boolean)request.getAttribute("isLocatorTest");    
    
    String locatorSessionInfo = (String)request.getAttribute("locatorSessionInfo");    
    
    Boolean unlimitedLicenses = (Boolean)request.getAttribute("unlimitedLicenses");
    
    
    Boolean checkboxDisabled = Boolean.FALSE;
    if (isLocatorTest.booleanValue()) {
        checkboxDisabled = Boolean.TRUE;
        checked = Boolean.TRUE;
    }
    
    
%>

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<c:if test="${multipleSubtest != null}">     
    <h1>
    <netui:content value="Register Student: Modify Test"/>
    </h1>
    <p>
    <netui:content value="You may change the order in which subtests will be presented for testing, remove subtests from the test, or re-select them from the Available Subtests list. For tests that do not include the Locator Test, you may also select the difficulty level for each subtest."/><br/>
    </p>
</c:if>

<!-- message -->
<jsp:include page="/registration/show_message.jsp" />

<c:if test="${multipleSubtest == null}">     
    <c:if test="${showDropDown != null}">     
        <h1>
        <netui:content value="Register Student: Modify Organization"/>
        </h1>
        <p>
        <netui:content value="This student is assigned to more than one organization. Select the organization you want to use for reporting the student's test results."/><br/>
        </p>
    </c:if>
    <c:if test="${showDropDown == null}">   
        <h1>
        <netui:content value="Register Student: View Test"/>
        </h1>
        <p>
        <netui:content value="Click Next to continue."/><br/>
        </p>
    </c:if>
</c:if>





<!-- start form -->
<netui:form action="modifyTest">


<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedTab"/>
<netui:hidden dataSource="actionForm.selectedStudentId"/>

<netui:hidden dataSource="actionForm.studentMaxPage"/> 

<!-- selection information -->
<p>
<table class="transparent" width="100%">
<tr><td valign="top" width="50%">

<!-- session table -->
<table class="transparent">
<tr>
    <td class="transparent"><netui:span value="Session name:"/></td>
    <td class="transparent"><netui:span value="${requestScope.testAdminName}"/></td>
</tr>
<tr>
    <td class="transparent"><netui:span value="Student name:"/></td>
    <td class="transparent"><netui:span value="${requestScope.studentName}"/></td>
</tr>
<tr>
    <td class="transparent"><netui:span value="Organization:"/></td>
    <td class="transparent">
<c:if test="${showDropDown != null}">     
    <netui:select optionsDataSource="${pageFlow.orgNodeNames}" dataSource="actionForm.selectedOrgNodeName" size="1"/>
</c:if>
<c:if test="${showDropDown == null}">   
    <netui:span value="${actionForm.selectedOrgNodeName}"/>
    <netui:hidden dataSource="actionForm.selectedOrgNodeName"/> 
</c:if>
    </td>
</tr>
<c:if test="${locatorSessionInfo != null}">
<tr>
    <td class="transparent-top" width="100"><netui:span value="Locator Session:"/></td>
    <td class="transparent"><netui:span value="${requestScope.locatorSessionInfo}"/></td>
</tr>
</c:if>
</table>




</td>

</tr>
</table>

</p>




<!-- Locator Test-->
<c:if test="${hasLocatorSubtest != null}">     
<a name="autoLocator"><!-- autoLocator --></a>    
<p>
<h4><netui:span value="Locator Test"/></h4>
<c:if test="${! isLocatorTest}">  
    <p>Select the checkbox below to include Locator Test. The student's performance on the Locator Test determines the difficulty level of the other subtests.</p>
</c:if>
<ctbweb:autoLocatorTag subtestName = "<%= subtestName %>" 
                       levelName = "<%= levelName %>" 
                       showLevel = "<%= Boolean.FALSE %>" 
                       checked = "<%= checked %>" 
                       checkboxDisabled = "<%= checkboxDisabled %>" />
</p>
</c:if>



<!-- subtestOrderList custom tag -->
<c:if test="${multipleSubtest != null}">     
<p>
<h4><netui:span value="Subtest Details"/></h4>
<p>Select a subtest and use the controls in the table to change the subtest order, remove the subtest from the test, or add the subtest back into the test. If the Locator test is not scheduled, you may also select the level of difficulty for each subtest.</p>

<ctbweb:subtestOrderList allSubtests = "<%= allSubtests %>" 
                         availableSubtests = "<%= availableSubtests %>"
                         selectedSubtests = "<%= selectedSubtests %>"
                         levelOptions = "<%= levelOptions %>" 
                         showLevel = "<%= showLevel %>" 
                         broswerType = "<%= broswerType %>" />
</p>
</c:if>


<p>
    <netui:button type="submit" value="Cancel" action="gotoHomePage" onClick="return verifyExitRegisterStudent();"/>&nbsp;
<c:if test="${! hideBackButton}">     
    <netui:button type="submit" value="Back" action="backToEnterStudent"/>&nbsp;
</c:if>    
    <netui:button type="submit" value="Next" action="nextToCongratulations"/>
</p>

                
</netui:form>
        
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
