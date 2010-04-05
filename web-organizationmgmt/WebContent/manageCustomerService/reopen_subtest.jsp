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

<netui-template:template templatePage="/resources/jsp/template_reopen_test_session.jsp">


<netui-template:setAttribute name="title" value="${bundle.web['reopen.testSession.window.title']}"/>
<!--<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findStudent']}"/>-->
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${bundle.web['reopen.testSession.title']}"/>
</h1>


<!-- title -->

    <p>
        <netui:content value="Select Student tab to reset a subtest for a single student.select Session tab to reset a subtest for multiple students in the same test session."/>

    </p>





<!-- start form -->
<netui:form action="findTestSessionByStudent">

<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.studentProfile.studentLoginId}" />

<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden tagId="testAccessCode" dataSource="actionForm.testAccessCode"/> 
<netui:hidden tagId="testAdminName" dataSource="actionForm.testAdminName"/>
<netui:hidden tagId="creatorOrgNodeId" dataSource="actionForm.creatorOrgNodeId"/>

<netui:hidden tagId="testAdminId" dataSource="actionForm.testAdminId"/> 
<netui:hidden tagId="studentId" dataSource="actionForm.selectedStudentId"/>
<netui:hidden tagId="studentLoginId" dataSource="actionForm.studentProfile.studentLoginId"/> 
<netui:hidden tagId="studentId" dataSource="actionForm.selectedStudentId"/>
<netui:hidden tagId="subtestMaxPage" dataSource="actionForm.subtestMaxPage"/>
<netui:hidden tagId="studentMaxPage" dataSource="actionForm.studentMaxPage"/> 
<netui:hidden tagId="testSessionMaxPage" dataSource="actionForm.testSessionMaxPage"/>
<netui:hidden tagId="customerId" dataSource="actionForm.customerId"/>


<!-- message -->
<jsp:include page="/manageCustomerService/show_message.jsp" />


<!-- tabs -->      
<ctb:tableTabGroup dataSource="actionForm.selectedTab">
    <ctb:tableTab value="moduleStudentTestSession"><netui:content value="Student"/></ctb:tableTab>
    <ctb:tableTab value="moduleTestSession"><netui:content value="Session"/></ctb:tableTab>
</ctb:tableTabGroup>


<!-- include pages -->      
<ctb:switch dataSource="${actionForm.selectedTab}">
    <ctb:case value="moduleStudentTestSession">
        <jsp:include page="/manageCustomerService/reopen_subtest_by_student.jsp" />
    </ctb:case>
    <ctb:case value="moduleTestSession">
        <jsp:include page="/manageCustomerService/reopen_subtest_by_test_session.jsp" />
    </ctb:case>
</ctb:switch>



<br/>

</netui:form>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>