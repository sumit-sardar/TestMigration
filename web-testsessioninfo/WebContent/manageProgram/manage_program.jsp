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

<netui-template:setAttribute name="title" value="${bundle.web['manageProgram.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.programStatus']}"/>
<netui-template:section name="bodySection">

<% 
    Boolean noPrograms = (Boolean)request.getAttribute("noPrograms");
    Boolean hasClickableSubtest = (Boolean)request.getAttribute("hasClickableSubtest"); 
    Boolean noTest = (Boolean)request.getAttribute("noTest");
%> 

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<h1><netui:content value="${bundle.web['manageProgram.page.title']}"/></h1>

<c:if test="${noPrograms}">
<p>
    <netui:content value="${bundle.web['manageProgram.noProgram.message']}"/><br/>
</p>
</c:if>
<c:if test="${!noPrograms}">
<p>
    <netui:content value="${bundle.web['manageProgram.page.message']}"/><br/>
</p>

<!-- start form -->
<netui:form action="manageProgram">

<netui:hidden dataSource="actionForm.actionElement"/> 
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedProgramName"/>
<netui:hidden dataSource="actionForm.selectedTestName"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/>

<!-- select a test and an organization -->
<p>
	<jsp:include page="/manageProgram/select_test_organization.jsp" /> 
</p>

<c:if test="${!noTest}">
<!-- subtest structure -->
<br/>
<h2>
    <netui:content value="${requestScope.testStatusTitle}" defaultValue="&nbsp;"/>
</h2>
<c:if test="${hasClickableSubtest}">     
<p>
    <netui:content value="${bundle.web['manageProgram.subtestStructure.message']}"/><br/>
</p>
</c:if>
<p>
	<jsp:include page="/manageProgram/subtest_structure_info.jsp" />	 
</p>


<c:if test="${viewSubtestStatus != null}">     
<br/>
<h2>
    <netui:content value="${requestScope.sessionsForTitle}" defaultValue="&nbsp;"/>
</h2>
<p>
    <netui:content value="${bundle.web['manageProgram.studentInfo.message']}"/><br/>
</p>
   
<p>
	<jsp:include page="/manageProgram/student_info_by_subtest.jsp" />	
</p>

</c:if>            
</c:if>
</netui:form>
</c:if>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>







