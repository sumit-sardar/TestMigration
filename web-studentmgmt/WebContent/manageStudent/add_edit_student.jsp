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
<netui-template:setAttribute name="title" value="${bundle.web['addstudent.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.addStudent']}"/>
<netui-template:section name="bodySection">



<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Enter information about the student in the form below. Required fields are marked by a blue asterisk *."/>
    <br />
    <netui:content value="Use the organization selector on the right to assign at least one organization for the student."/>
</p>



<!-- start form -->
<netui:form action="addEditStudent">

<%
    Boolean profileEditable = (Boolean)request.getAttribute("profileEditable"); 
	Boolean isABECustomer = (Boolean)request.getAttribute("isABECustomer");
	Boolean disableDeleteButton = (Boolean)request.getAttribute("disableDeleteButton");
	
%>
<c:if test="${ profileEditable }">            
    <input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.studentProfile.firstName}" />
</c:if>
 <!-- Added tagId to resolve javascript isssue  for webLogic 10.3-->    
<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedStudentId"/>
<netui:hidden dataSource="actionForm.studentProfile.userName"/>
<netui:hidden dataSource="actionForm.studentProfile.createBy"/>
<netui:hidden dataSource="actionForm.orgMaxPage"/> 
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/> 
<netui:hidden dataSource="actionForm.selectedTab"/> 
<netui:hidden dataSource="actionForm.studentIdLabelName"/>
<netui:hidden dataSource="actionForm.studentId2LabelName"/>
<netui:hidden dataSource="actionForm.ABECustomer"/>
<netui:hidden tagId="laborForceId" dataSource="actionForm.laborForceValue" />

<!-- message -->
<jsp:include page="/manageStudent/show_message.jsp" />



<!-- buttons -->
<p>
<c:if test="${viewOnly == 'false'}">     
    <c:if test="${isAddStudent != null}">     
        <netui:button type="submit" value="Save" action="saveAddEditStudent"/>
    </c:if> 
    <c:if test="${isEditStudent != null}">     
        <netui:button type="submit" value="Save" action="saveAddEditStudent"/>
         <c:if test="${disableDeleteButton}">             
			<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator"> 
			    	<netui:button type="submit" value="Delete" action="beginDeleteStudent" disabled="true" onClick="return verifyDeleteStudent();"/>
			</ctb:auth> 
		</c:if>   
	
		<c:if test="${!disableDeleteButton}">             
			<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator"> 
			    	<netui:button type="submit" value="Delete" action="beginDeleteStudent"  onClick="return verifyDeleteStudent();"/>
			</ctb:auth> 
		</c:if>      
    </c:if> 
</c:if> 
    <netui:button type="submit" value="Cancel" action="returnToFindStudent"/>
</p>



<!-- collapsible sections -->
<a name="moduleStudentProfile"><!-- moduleStudentProfile --></a>  
<c:if test="${!isABECustomer}">       
<p>
    <ctb:showHideSection sectionId="moduleStudentProfile" sectionTitle="Student Information" sectionVisible="actionForm.byStudentProfileVisible">
        <jsp:include page="/manageStudent/add_edit_student_by_profile.jsp" />
    </ctb:showHideSection>
</p>
</c:if> 
<c:if test="${isABECustomer}">       
<p>
    <ctb:showHideSection sectionId="moduleStudentProfile" sectionTitle="Student Information" sectionVisible="actionForm.byStudentProfileVisible">
        <jsp:include page="/manageStudent/dyn_add_edit_student_by_profile.jsp" />
    </ctb:showHideSection>
</p>
</c:if> 
<c:if test="${isABECustomer}">   
<p>
    <ctb:showHideSection sectionId="moduleStudentContact" sectionTitle="Contact Information" sectionVisible="actionForm.byStudentContactVisible">
        <jsp:include page="/manageStudent/add_edit_student_by_contact.jsp" />
    </ctb:showHideSection>
</p>
</c:if>


<c:if test="${demographicVisible == 'T'}">                 
<a name="moduleStudentDemographic"><!-- moduleStudentDemographic --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentDemographic" sectionTitle="Additional Student Information" sectionVisible="actionForm.byStudentDemographicVisible">
       <jsp:include page="/manageStudent/add_edit_student_by_demographic.jsp" />
    </ctb:showHideSection>
</p>
</c:if>

<c:if test="${isABECustomer}">     
<a name="moduleStudentWorkforceGoal"><!-- moduleStudentProgramsGoal --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentWorkforceGoal" sectionTitle="Supplement data for Workforce Student" sectionVisible="actionForm.byStudentWorkforceVisible">
        <jsp:include page="/manageStudent/add_edit_student_supplement_data_workforce.jsp" />
    </ctb:showHideSection>
</p>
</c:if>

<c:if test="${isABECustomer}">     
<a name="moduleStudentEduAndInstr"><!-- moduleStudentProgramsGoal --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentEduAndInstr" sectionTitle="Education And Instruction" sectionVisible="actionForm.byStudentEduInstrucVisible">
        <jsp:include page="/manageStudent/add_edit_student_edu_instru.jsp" />
    </ctb:showHideSection>
</p>
</c:if>





<c:if test="${isABECustomer}">     
<a name="moduleStudentProgramsGoal"><!-- moduleStudentProgramsGoal --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentProgramsGoal" sectionTitle="Program And Goals" sectionVisible="actionForm.byStudentProgramGoalVisible">
        <jsp:include page="/manageStudent/add_edit_student_by_programs_goal.jsp" />
    </ctb:showHideSection>
</p>
</c:if>


<c:if test="${hideAccommodations == 'F'}">                 
<a name="moduleStudentAccommodation"><!-- moduleStudentAccommodation --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentAccommodation" sectionTitle="Specific Accommodations" sectionVisible="actionForm.byStudentAccommodationVisible">
        <jsp:include page="/manageStudent/add_edit_student_by_accommodation.jsp" />
    </ctb:showHideSection>
</p>
</c:if>


<!-- buttons -->
<p>
<c:if test="${viewOnly == 'false'}">     
    <c:if test="${isAddStudent != null}">     
        <netui:button type="submit" value="Save" action="saveAddEditStudent"/>
    </c:if> 
    <c:if test="${isEditStudent != null}">     
        <netui:button type="submit" value="Save" action="saveAddEditStudent"/> 
         <c:if test="${disableDeleteButton}">             
			<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator"> 
			    	<netui:button type="submit" value="Delete" action="beginDeleteStudent" disabled="true" onClick="return verifyDeleteStudent();"/>
			</ctb:auth> 
		</c:if>   
	
		<c:if test="${!disableDeleteButton}">             
			<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator"> 
			    	<netui:button type="submit" value="Delete" action="beginDeleteStudent"  onClick="return verifyDeleteStudent();"/>
			</ctb:auth> 
		</c:if>      
        
    </c:if> 
</c:if> 
    <netui:button type="submit" value="Cancel" action="returnToFindStudent"/>
</p>


</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
