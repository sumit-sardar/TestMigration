<%@ page import="java.io.*, java.util.*"%>
<%@ page import="com.ctb.util.web.sanitizer.JavaScriptSanitizer"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%
	String errorMsg = (String)request.getAttribute("errorMsg");
%>
<html>

<head>
  	<title>Change Password</title>

    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
    
	<link href="<%=request.getContextPath()%>/resources/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/main.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/roundCorners.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/widgets_NEW.css" rel="stylesheet" type="text/css" />
          
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.6.2.min.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.blockUI.min.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.corners.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/roundCorners.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/main.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/sanitize.js"></script>    
     
</head>

<body>


<form method="post" action="savePassword.do">

<!-- MAIN BODY -->
<table class="simpleBody">
 
	<tr>
		<td align="center" valign="top" >
			<table class="bodyLayout">

				<!-- HEADER SECTION -->
				<tr class="bodyLayout">
					<td>
					 
						<table class="headerLayout" >
							<tr>
								<td align="left" width="70%"><img src="<%=request.getContextPath()%>/resources/images/ctb_oas_logo.png"></td>
							</tr>
						</table>
					</td>
				</tr>


				<!-- BODY SECTION -->
				<tr>
				  	<td align="left" valign="top">

<div class="feature">

  	<div class="feature" style="background-color: #ffffff; border:1px; padding: 5px;">
      	
<table class="simpleBlock">
<tr>
    <td style="padding-left: 20px;">
        <!-- Begin content-->
        
        <table width="100%" cellpadding="5">
            <tr>
                <td colspan="2">
                
                
<h1>
    <netui:content value="${requestScope.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="You must change your password to continue. Either this is your first login, your password has expired, or your administrator has changed your password."/><br/>
    <netui:content value="Required fields are marked by a blue asterisk "/><span class="asterisk">*</span><netui:content value=". New password cannot be any of five previous passwords."/>
</p>


<% if (errorMsg != null) { %>
                <div style="background-color: #ffc; border-color: #000; border-style: solid; border-width: 1px; padding: 5px;">
                    <table>
                        <tr>
                            <td valign="center">
                                <img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
                            </td>
                            <td valign="center">
                                <font style="color: red; font-size:12px; font-weight:bold"><span><%= errorMsg %></span></font>
                            </td>
                        </tr>
                    </table>
                </div>
<% } %>

<p>
<table class="transparent">
<tr>
	<td class="transparent" width="50%">

<table class="transparent">
    <tr> 
        <td class="transparent alignRight" width="130"><span class="asterisk">*</span>&nbsp;<netui:content value="Old Password:"/></td>
        <td class="transparent">
            <netui:textBox password="true" tagId="newPassword" dataSource="pageFlow.userProfile.userPassword.oldPassword" tabindex="1" maxlength="32" style="width:290px"/>
        </td>
    </tr>
       
    <tr> 
        <td class="transparent" colspan="2"><hr size="1"></td>
    </tr>
   
    
    <tr class="transparent">
        <td class="transparent alignRight" width="130"><span class="asterisk">*</span>&nbsp;<netui:content value="New Password:"/></td>
        <td class="transparent">
            <netui:textBox password="true" tagId="newPassword" dataSource="pageFlow.userProfile.userPassword.newPassword" tabindex="2" maxlength="32" style="width:290px"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="130" nowrap><span class="asterisk">*</span>&nbsp;<netui:content value="Confirm Password:"/></td>
        <td class="transparent">
            <netui:textBox password="true" tagId="confirmPassword" dataSource="pageFlow.userProfile.userPassword.confirmPassword" tabindex="3" maxlength="32" style="width:290px"/>
        </td>
    </tr>
    <tr> 
        <td class="transparent" colspan="2"><hr size="1"></td>
    </tr>

    <tr class="transparent">
        <td class="transparent alignRight" width="130"><span class="asterisk">*</span>&nbsp;<netui:content value="Hint Question:"/></td>
        <td class="transparent">
            <netui:select optionsDataSource="${pageFlow.hintQuestionOptions}" dataSource="pageFlow.userProfile.userPassword.hintQuestionId" tabindex="4" size="1" style="width:290px" defaultValue="${pageFlow.userProfile.userPassword.hintQuestionId}"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="130"><span class="asterisk">*</span>&nbsp;<netui:content value="Hint Answer:"/></td>
        <td class="transparent">
            <netui:textBox tagId="confirmPassword" dataSource="pageFlow.userProfile.userPassword.hintAnswer" tabindex="5" maxlength="255" style="width:290px"/>
        </td>
    </tr>   
</table>
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
   	<a href="#" onclick="submitPage();" class="rounded {transparent} button" 
   	onfocus="handleFocus(event, this);" onblur="handleBlur(event, this);"
   	onkeypress="return handleEnterKey(event, this);" >
   	Save
   	</a>                	
</p>



                
                </td>
            </tr>
            </table>
        <br>
        <!--End content-->
    </td>
</tr>
</table>
</td>
</tr>




</table>
<!--end main table-->

	</div>
</div>


					</td>
				</tr>
			


				<!-- FOOTER SECTION -->
				<tr>
				  <td align="left" valign="top">
				  
					<table class="footerLayout">
					  <tr>
						<td class="footerLayout">
						    <span>
						        Copyright &copy; 2008 by CTB/McGraw-Hill LLC. All rights reserved.
						    </span>
						
						    <span>
						        Subject to <a href="<%=request.getContextPath()%>/resources/html/terms_of_use.html" onClick="showTermsOfUseWindow(this.href); return false;">Terms of Use</a>.
						    </span>
						
						    <span>
						        Read our <a href="<%=request.getContextPath()%>/resources/html/privacy_policy.html" onClick="showPrivacyPolicyWindow(this.href); return false;">Privacy Policy Online</a>.
						    </span>
						
						    <span>
						        Review <a href="<%=request.getContextPath()%>/resources/html/coppa_policy.html" onClick="showCOPPAWindow(this.href); return false;">COPPA Policy</a>.
						    </span>
						</td>
					  </tr>
					</table>
				  </td>
				</tr>

			</table>
		</td>
	</tr>


</table>

</form>

</body>
</html>