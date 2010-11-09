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

<netui-template:template templatePage="/resources/jsp/template_add_edit_student.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['followupstudent.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.addStudent']}"/>
<netui-template:section name="bodySection">



<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="Follow Up Data: Test Stu"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Edit the student data. Required fields are marked by a blue asterisk *."/>
    <br />
    <netui:content value=""/>
</p>



<!-- start form -->
<netui:form action="followUpStudent">

<%
    Boolean profileEditable = (Boolean)request.getAttribute("profileEditable"); 
	Boolean isABECustomer = (Boolean)request.getAttribute("isABECustomer");
	
%>
<c:if test="${ profileEditable }">            
    <input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.studentProfile.firstName}" />
</c:if>
 <!-- Added tagId to resolve javascript isssue  for webLogic 10.3-->    
<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/>
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedStudentId"/>
<netui:hidden dataSource="actionForm.studentProfile.firstName"/>
<netui:hidden dataSource="actionForm.studentProfile.createBy"/>
<netui:hidden dataSource="actionForm.orgMaxPage"/> 
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/> 

<netui:hidden dataSource="actionForm.studentIdLabelName"/>
<netui:hidden dataSource="actionForm.studentId2LabelName"/>
<netui:hidden dataSource="actionForm.ABECustomer"/>


<!-- message -->
<jsp:include page="/manageStudentFollowUp/show_message.jsp" />

<p>
        <netui:button type="submit" value="Save" action="saveFollowUpData"/>   
        <netui:button type="submit" value="Cancel" action="returnToFindStudent"/>
</p>

<!-- collapsible sections -->
<a name="moduleStudentProfile"><!-- moduleStudentProfile --></a>  
      
<p>
    <ctb:showHideSection sectionId="moduleStudentFollowUpProfile" sectionTitle="Student Information" sectionVisible="actionForm.byStudentProfileVisible">
        <jsp:include page="/manageStudentFollowUp/follow_up_student_by_profile.jsp" />
    </ctb:showHideSection>
</p>

<p>
    <ctb:showHideSection sectionId="moduleStudentFollowUpAdditionalInformation" sectionTitle="Additional Information" sectionVisible="actionForm.byStudentAccommodationVisible">
        <jsp:include page="/manageStudentFollowUp/follow_up_student_by_other_information.jsp" />
    </ctb:showHideSection>
</p>     
<p>
    <ctb:showHideSection sectionId="moduleStudentFollowUpWorkforce" sectionTitle="Supplemental Data for Workforce Student" sectionVisible="actionForm.byStudentAccommodationVisible">
        <jsp:include page="/manageStudentFollowUp/follow_up_student_by_workforce.jsp" />
    </ctb:showHideSection>
</p>


<p>
         <netui:button type="submit" value="Save" action="saveFollowUpData"/>
         <netui:button type="submit" value="Cancel" action="returnToFindStudent"/>
</p>



</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
