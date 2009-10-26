<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-compat-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template_edit_my_profile.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['adduser.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.myProfile']}"/>
<netui-template:section name="bodySection">



<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="Edit My Profile"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Enter information about yourself in the form below. Required fields are marked by a blue asterisk *."/><br/>
    <netui:content value="Although Email is not required, it is recommended that a valid email address be entered."/>
</p>



<!-- start form -->
<netui:form action="addEditUser">

<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.userProfile.firstName}" />

<netui:hidden dataSource="actionForm.actionElement"/> 
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedUserId"/>
<netui:hidden dataSource="actionForm.userProfile.loginId"/>
<netui:hidden dataSource="actionForm.userProfile.addressId"/> 
<netui:hidden dataSource="actionForm.userProfile.role"/>
<netui:hidden dataSource="actionForm.userProfile.roleId"/>
<%-- CR Dex --%>
<netui:hidden dataSource="actionForm.userProfile.extPin1"/>
<!-- message -->
<jsp:include page="/manageUser/show_message.jsp" />



<!-- buttons -->
<p>
    <netui:button type="submit" value="Save" action="saveMyProfile"/>
    <netui:button type="submit" value="Cancel" action="cancelCurrentAction"/>
</p>


<!-- collapsible sections -->
<a name="moduleUserProfile"><!-- moduleUserProfile --></a>    
<p>
    <ctb:showHideSection sectionId="moduleUserProfile" sectionTitle="User Information" sectionVisible="{actionForm.byUserProfileVisible}">
        <jsp:include page="/manageUser/edit_my_profile_info.jsp" />
    </ctb:showHideSection>
</p>


<a name="moduleUserContact"><!-- moduleUserContact --></a>    
<p>
    <ctb:showHideSection sectionId="moduleUserContact" sectionTitle="Contact Information" sectionVisible="{actionForm.byUserContactVisible}">
        <jsp:include page="/manageUser/add_edit_user_by_contact.jsp" />
    </ctb:showHideSection>
</p>



<!-- buttons -->
<p>
    <netui:button type="submit" value="Save" action="saveMyProfile"/>
    <netui:button type="submit" value="Cancel" action="cancelCurrentAction"/>
</p>


</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
