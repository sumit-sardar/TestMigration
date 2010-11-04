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
<netui-template:setAttribute name="title" value="${bundle.web['viewstudent.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.viewStudent']}"/>
<netui-template:section name="bodySection">



<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Review the student information listed below."/> 
</p>

<%
  	Boolean isABECustomer = (Boolean)request.getAttribute("isABECustomer");
	
%>
<!-- start form -->
<netui:form action="viewStudent">

<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedStudentId"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/> 
<netui:hidden dataSource="actionForm.selectedTab"/> 



<!-- message -->
<jsp:include page="/manageStudent/show_message.jsp" />




<!-- buttons -->
<p>
<c:if test="${showBackButton == 'returnToFindStudent'}">     
    <netui:button type="submit" value="Back" action="returnToFindStudent"/>
</c:if>
<c:if test="${showBackButton == 'beginAddStudent'}">     
<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator">
    <netui:button type="submit" value="Back" action="beginAddStudent"/>
</ctb:auth>    
</c:if>
<c:if test="${showEditButton == 'true'}">                 
<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator">
    <netui:button type="submit" value="Edit" action="beginEditStudent"/>
</ctb:auth>    
</c:if>
<c:if test="${showDeleteButton == 'true'}">                 
<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator">
    <netui:button type="submit" value="Delete" action="beginDeleteStudent" onClick="return verifyDeleteStudent();"/>
</ctb:auth>    
</c:if>
</p>




<!-- collapsible sections -->
<c:if test="${!isABECustomer}"> 
<p>
    <ctb:showHideSection sectionId="moduleStudentProfile" sectionTitle="Student Information" sectionVisible="actionForm.byStudentProfileVisible">
        <jsp:include page="/manageStudent/view_student_by_profile.jsp" />
    </ctb:showHideSection>
</p>
</c:if> 

<c:if test="${isABECustomer}">       
<p>
    <ctb:showHideSection sectionId="moduleStudentProfile" sectionTitle="Student Information" sectionVisible="actionForm.byStudentProfileVisible">
        <jsp:include page="/manageStudent/view_student_by_profile.jsp" />
    </ctb:showHideSection>
</p>
</c:if> 

<c:if test="${isABECustomer}">   
<p>
    <ctb:showHideSection sectionId="moduleStudentContact" sectionTitle="Contact Information" sectionVisible="actionForm.byStudentAccommodationVisible">
        <jsp:include page="/manageStudent/view_student_by_contact.jsp" />
    </ctb:showHideSection>
</p>
</c:if>

<c:if test="${demographicVisible == 'T'}">                 
<p>
    <ctb:showHideSection sectionId="moduleStudentDemographic" sectionTitle="Additional Student Information" sectionVisible="actionForm.byStudentDemographicVisible">
        <jsp:include page="/manageStudent/add_edit_student_by_demographic.jsp" />
    </ctb:showHideSection>
</p>
</c:if>


<c:if test="${isABECustomer}"> 
<a name="moduleStudentSupplementData"><!-- moduleStudentContact --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentSupplementData" sectionTitle="Supplemental Data for Workforce Student" sectionVisible="actionForm.byStudentAccommodationVisible">
        <jsp:include page="/manageStudent/add_edit_student_supplement_data_workforce.jsp" />
    </ctb:showHideSection>
</p>
</c:if> 

<c:if test="${isABECustomer}">       
<a name="moduleStudentEduInstruction"><!-- moduleStudentEduInstruction --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentEduInstruction" sectionTitle="Education  And Instruction Information" sectionVisible="actionForm.byStudentAccommodationVisible">
        <jsp:include page="/manageStudent/add_edit_student_Edu_Intruction.jsp" />
    </ctb:showHideSection>
</p>
</c:if>

<c:if test="${isABECustomer}">     
<a name="moduleStudentProgramsGoal"><!-- moduleStudentProgramsGoal --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentProgramsGoal" sectionTitle="Programs And Goal" sectionVisible="actionForm.byStudentAccommodationVisible">
        <jsp:include page="/manageStudent/add_edit_student_by_programs_goal.jsp" />
    </ctb:showHideSection>
</p>
</c:if>

<c:if test="${hideAccommodations == 'F'}">                 
<p>
    <ctb:showHideSection sectionId="moduleStudentAccommodation" sectionTitle="Specific Accommodations" sectionVisible="actionForm.byStudentAccommodationVisible">
        <jsp:include page="/manageStudent/add_edit_student_by_accommodation.jsp" />
    </ctb:showHideSection>
</p>
</c:if>


<!-- buttons -->
<p>
<c:if test="${showBackButton == 'returnToFindStudent'}">     
    <netui:button type="submit" value="Back" action="returnToFindStudent"/>
</c:if>
<c:if test="${showBackButton == 'beginAddStudent'}">     
<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator">
    <netui:button type="submit" value="Back" action="beginAddStudent"/>
</ctb:auth>    
</c:if>
<c:if test="${showEditButton == 'true'}">                 
<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator">
    <netui:button type="submit" value="Edit" action="beginEditStudent"/>
</ctb:auth>    
</c:if>
<c:if test="${showDeleteButton == 'true'}">                 
<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator">
    <netui:button type="submit" value="Delete" action="beginDeleteStudent" onClick="return verifyDeleteStudent();"/>
</ctb:auth>    
</c:if>
</p>



</netui:form>
        
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
