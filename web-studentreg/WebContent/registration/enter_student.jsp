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
<netui-template:setAttribute name="title" value="${bundle.web['registerstudent.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.registerStudent']}"/>
<netui-template:section name="bodySection">


<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->


<!--Beginning of change License-->

<table width="100%" cellpadding="0" cellspacing="0" class="transparent">

    <tr>
        <td nowrap="">
            <h1>
                <netui:content value="Register Student: Enter Student Information"/>
            </h1>
        </td>
        <td  class="transparent"></td>
        <td width="100%" rowspan="2" align="right" valign="top">
        	&nbsp;
        </td>
        <td rowspan="2">
            <table width="25"><tr><td></td></tr></table>
        </td>
    </tr>
    
    
    <!--change for licnese-->
    
    <tr>   
        <td width="75%" class="transparent">
            <p>
                <c:if test="${selectedTab == 'findStudentAction'}">     
                    <netui:content value="To see a list of all students, click Search. To find a specific student, enter the known information on which to search."/><br/>
                    <netui:content value=" If you do not find the student you want to register, click the Add Student tab."/><br/>
                </c:if>
                <c:if test="${selectedTab == 'addStudentAction'}">     
                    <netui:content value="Enter information about the student in the form below. Required fields are marked by a blue asterisk *."/><br/>
                    <netui:content value="Use the organization selector on the right to assign an organization for the student."/>
                </c:if>
            </p>
        </td>
        <td width="62%" class="transparent"></td> 
    </tr>

</table>


  <!--End of change License-->

<p>
<table class="transparent">
<tr>
    <td class="transparent"><netui:span value="Session name:"/></td>
    <td class="transparent"><netui:span value="${requestScope.testAdminName}"/></td>
</tr>
</table>
</p>

<!-- start form -->
<netui:form action="enterStudent">

<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>


<netui:hidden dataSource="actionForm.orgMaxPage"/> 
<netui:hidden dataSource="actionForm.studentMaxPage"/> 
<netui:hidden dataSource="actionForm.autoLocator"/> 




<c:if test="${errorMessage != null}">
    <ctb:message title="" style="errorMessage">
        <netui:content value="${requestScope.errorMessage}"/>
    </ctb:message><br/>
</c:if>        

<!-- message -->
<jsp:include page="/registration/show_message.jsp" />


<!-- tabs -->      
<ctb:tableTabGroup dataSource="actionForm.selectedTab">
    <ctb:tableTab value="findStudentAction"><netui:content value="Find Student"/></ctb:tableTab>
    <ctb:tableTab value="addStudentAction"><netui:content value="Add Student"/></ctb:tableTab>
</ctb:tableTabGroup>


<!-- include pages -->      
<ctb:switch dataSource="actionForm.selectedTab">
    <ctb:case value="findStudentAction">
        <jsp:include page="/registration/find_student.jsp" />
    </ctb:case>
    <ctb:case value="addStudentAction">
        <jsp:include page="/registration/add_student.jsp" />
    </ctb:case>
</ctb:switch>




</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
