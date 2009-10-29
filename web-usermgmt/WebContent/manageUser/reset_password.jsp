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

<netui:html>
  
<head>
    <netui:base/>
    <title><netui-template:attribute name="title"/></title>
    <link href="<%=request.getContextPath()%>/resources/css/legacy.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/widgets.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/autosuggest.css" type="text/css" rel="stylesheet" />
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.jsp"></script>   
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/setupbuttons.js"></script>         
</head>

<body>

<jsp:include page="/resources/jsp/header.jsp" />

<table class="legacyBodyLayout">
<tr><td>&nbsp;</td></tr>


<tr><td id="legacyBody">
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="You must change your password to continue. Either this is your first login, your password has expired, or your administrator has changed your password."/><br/>
    <netui:content value="Required fields are marked by a blue asterisk "/><span class="asterisk">*</span><netui:content value=". New password cannot be any of five previous passwords."/>
</p>



<!-- start form -->
<netui:form action="changePassword">

<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.userProfile.userPassword.oldPassword}" />

<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
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
</p>


</netui:form>

</td></tr></table>

<jsp:include page="/resources/jsp/footer.jsp" />  

</body>
</netui:html>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
