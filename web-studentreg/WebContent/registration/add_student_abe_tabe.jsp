<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>


<!-- collapsible sections -->
<a name="moduleStudentProfile"><!-- moduleStudentProfile --></a>    
<p style="margin: 0 0 1px;!important"> 
    <ctb:showHideSection sectionId="moduleStudentProfile" sectionTitle="Student Information" sectionVisible="actionForm.byStudentProfileVisible">
        <jsp:include page="/registration/add_student_abe_tabe_by_profile.jsp" />
    </ctb:showHideSection>
</p>

<a name="moduleStudentContact"><!-- moduleStudentDemographic --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentContact" sectionTitle="Contact Information" sectionVisible="actionForm.byStudentProfileVisible">
        <jsp:include page="/registration/add_student_by_contact.jsp" />
    </ctb:showHideSection>
</p>

<a name="moduleStudentDemographic"><!-- moduleStudentContact --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentDemographic" sectionTitle="Demographics" sectionVisible="actionForm.byStudentDemographicVisible">
        <jsp:include page="/registration/add_student_by_demographic.jsp" />
    </ctb:showHideSection>
</p>

<a name="moduleStudentSupplementData"><!-- moduleStudentContact --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentSupplementData" sectionTitle="Supplemental Data for Workforce Student" sectionVisible="actionForm.byStudentDemographicVisible">
        <jsp:include page="/registration/add_student_supplement_data_workforce.jsp" />
    </ctb:showHideSection>
</p>


<a name="moduleStudentEduInstruction"><!-- moduleStudentEduInstruction --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentEduInstruction" sectionTitle="Education And Instruction" sectionVisible="actionForm.byStudentDemographicVisible">
        <jsp:include page="/registration/add_student_Edu_Intruction.jsp" />
    </ctb:showHideSection>
</p>

<a name="moduleStudentProgramsGoal"><!-- moduleStudentProgramsGoal --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentProgramsGoal" sectionTitle="Programs And Goal" sectionVisible="actionForm.byStudentDemographicVisible">
        <jsp:include page="/registration/add_student_by_programs_goal.jsp" />
    </ctb:showHideSection>
</p>

<a name="moduleStudentAccommodation"><!-- moduleStudentAccommodation --></a>    
<p>
    <ctb:showHideSection sectionId="moduleStudentAccommodation" sectionTitle="Specific Accommodations" sectionVisible="actionForm.byStudentAccommodationVisible">
        <jsp:include page="/registration/add_student_by_accommodation.jsp" />
    </ctb:showHideSection>
</p>

                
<!-- buttons -->
<p>
    <netui:button type="submit" value="Cancel" action="gotoHomePage" onClick="return verifyExitRegisterStudent();"/>
    <netui:button type="submit" value="Next" action="toModifyTestFromAdd"/>
</p>