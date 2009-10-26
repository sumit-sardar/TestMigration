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
<netui-template:setAttribute name="title" value="${bundle.web['changepassword.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.changePassword']}"/>
<netui-template:section name="bodySection">



<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="The password must contain at least eight characters. At least one character must be a number and at least one character must be a letter."/><br/>
    <netui:content value="New password cannot be any of five previous passwords."/>
    <netui:content value="Required fields are marked by a blue asterisk "/><span class="asterisk">*</span><netui:content value="."/>
</p>



<!-- start form -->
<netui:form action="changePassword">

<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.userProfile.userPassword.newPassword}" />

<netui:hidden dataSource="actionForm.actionElement"/> 
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedUserName"/>
<netui:hidden dataSource="actionForm.userProfile.firstName"/>
<netui:hidden dataSource="actionForm.userProfile.lastName"/>
<netui:hidden dataSource="actionForm.selectedUserId"/> 

<!-- message -->
<jsp:include page="/manageUser/show_message.jsp" />

<p>
<table class="transparent"><tr><td class="transparent" width="400">

<table class="simple"><tr class="transparent"><td class="transparent-top">
<table class="transparent">    
    <tr class="transparent">
        <td class="transparent alignRight" width="120"><span class="asterisk">*</span>&nbsp;<netui:content value="New Password:"/></td>
        <td class="transparent">
            <netui:textBox password="true" tagId="newPassword" dataSource="actionForm.userProfile.userPassword.newPassword" tabindex="1" maxlength="32" style="width:230px"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="120"><span class="asterisk">*</span>&nbsp;<netui:content value="Confirm Password:"/></td>
        <td class="transparent">
            <netui:textBox password="true" tagId="confirmPassword" dataSource="actionForm.userProfile.userPassword.confirmPassword" tabindex="2" maxlength="32" style="width:230px"/>
        </td>
    </tr>
</table>
</td></tr></table>

</td></tr></table>
</p>



<!-- buttons -->
<p>
    <netui:button type="submit" value="Save" action="savePassword"/>
    <netui:button type="submit" value="Cancel" action="cancelCurrentAction"/>
</p>


</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
