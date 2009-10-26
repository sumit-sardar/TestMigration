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


<netui-template:template templatePage="/resources/jsp/template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['viewuser.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.viewUser']}"/>
<netui-template:section name="bodySection">

 

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Review the user information listed below."/> 
</p>

<!-- start form -->
<netui:form action="viewUser">

<netui:hidden tagId="{actionForm.actionElement}" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="{actionForm.currentAction}" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedUserName"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/> 
<netui:hidden dataSource="actionForm.selectedTab"/> 
<netui:hidden dataSource="actionForm.selectedUserId"/> 


<!-- message -->
<jsp:include page="/manageUser/show_message.jsp" />




<!-- buttons -->
<p>
    <netui:button type="submit" value="Back" action="handleBackButton"/>
    <netui:button type="submit" value="Edit" action="beginEditUser" disabled="${requestScope.disableEditButton}"/>
    <netui:button type="submit" value="Delete" action="beginDeleteUser" disabled="${requestScope.disableDeleteButton}" onClick="return verifyDeleteUser();"/>
    <%--<netui:button type="submit" value="Change Password" action="beginChangePassword" disabled="{request.disableChangePasswordButton}" />--%>
    <netui:button type="submit" value="Change Password" action="beginChangePassword" disabled="${requestScope.disableChangePasswordButton}"/>
</p>



<!-- collapsible sections -->
<a name="moduleUserProfile"><!-- moduleUserProfile --></a>    
<p>
    <ctb:showHideSection sectionId="moduleUserProfile" sectionTitle="User Information" sectionVisible="{actionForm.byUserProfileVisible}">
        <jsp:include page="/manageUser/view_user_by_profile.jsp" />
    </ctb:showHideSection>
</p>


<a name="moduleUserContact"><!-- moduleUserContact --></a>    
<p>
    <ctb:showHideSection sectionId="moduleUserContact" sectionTitle="Contact Information" sectionVisible="{actionForm.byUserContactVisible}">
        <jsp:include page="/manageUser/view_user_by_contact.jsp" />
    </ctb:showHideSection>
</p>


<!-- buttons -->
<p>
    <netui:button type="submit" value="Back" action="handleBackButton"/>
    <netui:button type="submit" value="Edit" action="beginEditUser" disabled="${requestScope.disableEditButton}"/>
    <netui:button type="submit" value="Delete" action="beginDeleteUser" disabled="${requestScope.disableDeleteButton}" onClick="return verifyDeleteUser();"/>
    <%--<netui:button type="submit" value="Change Password" action="beginChangePassword" disabled="{request.disableChangePasswordButton}" />--%>
    <netui:button type="submit" value="Change Password" action="beginChangePassword" disabled="${requestScope.disableChangePasswordButton}"/>
</p>



</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
