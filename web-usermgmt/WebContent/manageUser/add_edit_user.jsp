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

<netui-template:template templatePage="/resources/jsp/template_add_user.jsp">
<netui-template:setAttribute name="title" value="${pageFlow.webTitle}"/>
<netui-template:setAttribute name="helpLink" value="${pageFlow.helpLink}"/>
<netui-template:section name="bodySection">



<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Enter information about the user in the form below. Required fields are marked by a blue asterisk *."/>
    <c:if test="${isAddAdministrator }"> 
        <br /> 
        <br /> 
        <netui:content value="Email is used to send users their login information. If no email address is provided, the Account Manager is responsible for communicating login information to the user."/>
    </c:if> 
    <c:if test="${ ! isAddAdministrator }">                
        <netui:content value="Use the organization selector on the right to assign at least one organization for the user."/>
        <br /> 
        <br /> 
        <c:if test="${ isAddUser }">                
            Email is used to send users their login information. If no email address is provided, the administrator is responsible for communicating login information to the user.
        </c:if>    
        <c:if test="${ isEditUser }">                
            Although Email is not required, it is recommended that a valid email address be entered.
        </c:if>    
    </c:if>    
      
   
</p>



<!-- start form -->
<netui:form action="addEditUser">

<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.userProfile.firstName}" />

<netui:hidden dataSource="actionForm.actionElement"/> 
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.userProfile.loginId"/>
<netui:hidden dataSource="actionForm.userProfile.userId"/>
<netui:hidden dataSource="actionForm.orgMaxPage"/> 
<netui:hidden dataSource="actionForm.userMaxPage"/> 
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/> 
<netui:hidden dataSource="actionForm.selectedTab"/> 
<netui:hidden dataSource="actionForm.userProfile.addressId"/> 
<netui:hidden dataSource="actionForm.selectedUserName"/> 
<netui:hidden dataSource="actionForm.selectedUserId"/> 


<!-- message -->
<jsp:include page="/manageUser/show_message.jsp" />



<!-- buttons -->
<p>
    <netui:button type="submit" value="Save" action="saveAddEditUser"/>
<c:if test = "${! isAddAdministrator}">
    <c:if test="${ isAddUser }">                
        <netui:button type="submit" value="Cancel" action="beginFindUser"/>
    </c:if>  
</c:if>
<c:if test = "${isAddAdministrator}">
    <netui:button type="submit" value="Cancel" action="cancelCurrentAction"/>
</c:if>
<c:if test="${ isEditUser }">
    <netui:button type="submit" value="Delete" action="beginDeleteUser" onClick="return verifyDeleteUser();"/>                   
    <netui:button type="submit" value="Cancel" action="cancelCurrentAction"/>
</c:if>    
</p>


<!-- collapsible sections -->
<a name="moduleUserProfile"><!-- moduleUserProfile --></a>    
<p>
    <ctb:showHideSection sectionId="moduleUserProfile" sectionTitle="User Information" sectionVisible="{actionForm.byUserProfileVisible}">
        <jsp:include page="/manageUser/add_edit_user_by_profile.jsp" />
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
    <netui:button type="submit" value="Save" action="saveAddEditUser"/>
<c:if test = "${! isAddAdministrator}">
    <c:if test="${ isAddUser }">                
        <netui:button type="submit" value="Cancel" action="beginFindUser"/>
    </c:if>  
</c:if>
<c:if test = "${isAddAdministrator}">
    <netui:button type="submit" value="Cancel" action="cancelCurrentAction"/>
</c:if>
<c:if test="${ isEditUser }">
    <netui:button type="submit" value="Delete" action="beginDeleteUser" onClick="return verifyDeleteUser();"/>                   
    <netui:button type="submit" value="Cancel" action="cancelCurrentAction"/>
</c:if>    
</p>

</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
