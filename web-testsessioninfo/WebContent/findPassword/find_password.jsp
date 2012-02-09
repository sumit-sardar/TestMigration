<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%
    String errorMsg = (String)request.getAttribute("errorMsg");
%>

<html>

<head>
  <title>Get New Password</title>
  
	<link href="<%=request.getContextPath()%>/resources/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/main.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/roundCorners.css" rel="stylesheet" type="text/css" />
          
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.6.2.min.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.blockUI.min.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.corners.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/roundCorners.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/main.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/sanitize.js"></script>    
  	  
</head>
<body leftmargin="0" topmargin="0">


<script type="text/javascript">
function ignoreEnterKey( e ) {
    var keyId = (window.event) ? event.keyCode : e.which;
    if( keyId == 13 )
        return false;
    else
        return true;
}
</script>

<!-- MAIN BODY -->

<table  class="simpleBody">
 
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
    <td width="500"  style="padding-left: 80px;">
        <!-- Begin content-->
        <form method="post" name="findpassword" action="getPassword.do">
		
        <table class="simpleBlock" width="100%" cellpadding="5">
            <tr>
                <td colspan="2">
                <br/><h1>Get new Password</h1>
                Required fields are marked by an asterisk <span class="asterisk">*</span>. 
                <br/><br/>
                </td>
            </tr>
            
<% if (errorMsg != null) { %>            
            <tr>
                <td colspan="2">
                <div style="background-color: #ffc; border-color: #000; border-style: solid; border-width: 1px; padding: 5px;">
                    <table>
                        <tr>
                            <td align="center">
                                <img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
                            </td>
                            <td align="center">
                                <font style="color: red; font-size:12px; font-weight:bold"><span><%= errorMsg %></span></font>
                            </td>
                        </tr>
                    </table>
                </div>
                <br/>
                </td>
            </tr>
<% } %>            
            
            <tr height="32">
                <td width="80" align="right"><b>Username:</b></td>
                <td><netui:span value="${pageFlow.user.userName}"/></td>
            </tr>
            <tr height="32">
                <td width="80" align="right"><b>Hint:</b></td>
                <td><netui:span value="${pageFlow.user.passwordHintQuestion}"/></td>
            </tr>
            <tr height="32">
                <td width="80" align="right"><b>* Hint Answer:</b></td>
                <td>
                    <netui:textBox dataSource="pageFlow.hintAnswer" maxlength="255" style="width: 180px" tabindex="1" onKeyPress="return ignoreEnterKey( event );"/>
                </td>
            </tr>
            <tr height="32">
                <td width="80" align="right"><b>* Email:</b></td>
                <td>
                    <netui:textBox dataSource="pageFlow.email" maxlength="64" style="width: 180px" tabindex="2" onKeyPress="return ignoreEnterKey( event );"/>
                </td>
            </tr>
            <tr height="32">
                <td>&nbsp;</td>
                <td>    
                	<a href="#" onclick="gotoAction('backToLogin.do');" class="rounded {transparent} button" tabindex="3"
                	onfocus="handleFocus(event, this);" onblur="handleBlur(event, this);"
                	onkeypress="return handleEnterKey(event, this);" >
                	Cancel
                	</a>                	
                	&nbsp;                	
                	<a href="#" onclick="gotoAction('getPassword.do');" class="rounded {transparent} button" tabindex="3"
                	onfocus="handleFocus(event, this);" onblur="handleBlur(event, this);"
                	onkeypress="return handleEnterKey(event, this);" >
                	Get New Password
                	</a>                	
                </td>
            </tr>
            </table>
        </form>
        <br>
        <!--End content-->
    </td>

</tr>



</table>
<!--end main table-->
<br/>
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
						        Copyright &copy; 2012 by CTB/McGraw-Hill LLC. All rights reserved.
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

</body>
</html>