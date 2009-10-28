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

<netui-template:template templatePage="/resources/jsp/template_my_profile.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['changepassword.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.changeYourPassword']}"/>
<netui-template:section name="bodySection">



<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Required fields are marked by a blue asterisk "/><span class="asterisk">*</span><netui:content value="."/><br/>
    <netui:content value="New password cannot be any of five previous passwords."/>
</p>



<!-- start form -->
<netui:form action="changePassword">

<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.userProfile.userPassword.oldPassword}" />

<netui:hidden dataSource="actionForm.actionElement"/> 
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedUserName"/>


<!-- message -->
<jsp:include page="/manageUser/show_message.jsp" />


<p>
<table class="transparent"><tr><td class="transparent" width="370">

<table class="simple"><tr class="transparent"><td class="transparent-top">
<table class="transparent">
    <tr> 
        <td class="transparent alignRight" width="130"><span class="asterisk">*</span>&nbsp;<netui:content value="Old Password:"/></td>
        <td class="transparent">
            <netui:textBox password="true" tagId="newPassword" dataSource="actionForm.userProfile.userPassword.oldPassword" tabindex="1" maxlength="32" style="width:290px"/>
        </td>
    </tr>
       
    <tr> 
        <td class="transparent" colspan="2"><hr size="1"></td>
    </tr>
   
    
    <tr class="transparent">
        <td class="transparent alignRight" width="130"><span class="asterisk">*</span>&nbsp;<netui:content value="New Password:"/></td>
        <td class="transparent">
            <netui:textBox password="true" tagId="newPassword" dataSource="actionForm.userProfile.userPassword.newPassword" tabindex="2" maxlength="32" style="width:290px"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="130" nowrap><span class="asterisk">*</span>&nbsp;<netui:content value="Confirm Password:"/></td>
        <td class="transparent">
            <netui:textBox password="true" tagId="confirmPassword" dataSource="actionForm.userProfile.userPassword.confirmPassword" tabindex="3" maxlength="32" style="width:290px"/>
        </td>
    </tr>
    <tr> 
        <td class="transparent" colspan="2"><hr size="1"></td>
    </tr>

    <tr class="transparent">
        <td class="transparent alignRight" width="130"><span class="asterisk">*</span>&nbsp;<netui:content value="Hint Question:"/></td>
        <td class="transparent">
            <netui:select optionsDataSource="${pageFlow.hintQuestionOptions}" dataSource="actionForm.userProfile.userPassword.hintQuestionId" tabindex="4" size="1" style="width:290px" defaultValue="${actionForm.userProfile.userPassword.hintQuestionId}"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="130"><span class="asterisk">*</span>&nbsp;<netui:content value="Hint Answer:"/></td>
        <td class="transparent">
            <netui:textBox tagId="confirmPassword" dataSource="actionForm.userProfile.userPassword.hintAnswer" tabindex="5" maxlength="255" style="width:290px"/>
        </td>
    </tr>   
</table>
</td></tr></table>
</td>

<td class="transparent-top">
<table class="transparent">
    
        <tr>
            <td class="transparent">
                <ul><li>
                    Enter old password for verification.
                </li></ul>
            </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        
        <tr>
            <td class="transparent">
                <ul><li>
                    The password must contain at least eight characters.<br/>At least one character must be a number and at least one character must be a letter.
                </li></ul>
            </td>
       </tr>
        <tr><td>&nbsp;</td></tr>
       
       <tr>
        <td class="transparent">
            <ul><li>
                Remember your hint answer.<br/>It can be used to validate you as a user in the event you forget your password.
            </li></ul>
        </td>
    </tr>
</table>
</td>

</tr></table>
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
