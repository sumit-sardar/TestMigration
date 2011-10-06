<%@ page import="java.io.*, java.util.*"%>
<%@ page import="com.ctb.util.web.sanitizer.JavaScriptSanitizer"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%
	java.util.ResourceBundle rb = java.util.PropertyResourceBundle.getBundle("errorMessages");

    String username = (String)request.getAttribute("username");
    if (username == null)
        username = "";
    username = JavaScriptSanitizer.sanitizeInput(username);

    Cookie[] cookies = (Cookie[])request.getCookies();
    if (cookies != null && cookies.length > 0 ) {
	    for (int i=0 ; i<cookies.length ; i++) {
	        Cookie c = cookies[i];
	        if ((c != null) && c.getName().equals("_WL_AUTHCOOKIE_TAS_SESSIONID")) {
                Cookie r = new Cookie(c.getName(), null);
                r.setMaxAge(0);
                r.setPath("/");
                response.addCookie(r);
                cookies[i] = null;
	            String message = rb.getString("sessionTimeout");
	            request.setAttribute("message", message);
	            response.sendRedirect("/TestSessionInfoWeb/logout2.jsp?timeout=true");
	        }

	    }  
    }
%>

<html>

<head>
  	<title>User Login</title>

    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
    
	<link href="<%=request.getContextPath()%>/resources/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/main.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/roundCorners.css" rel="stylesheet" type="text/css" />
          
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.6.2.min.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.blockUI.min.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.corners.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/roundCorners.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/main.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/sanitize.js"></script>    
     
</head>

<body leftmargin="0" topmargin="0" onload="document.getElementById('j_username').focus();">


<!-- BLOCK DIV -->
<div id="blockDiv" style="display:none; background-color: #d0e5f5; opacity:0.5; position:fixed;top:0;left:0;width:100%;height:100%;z-index:9999">
	<img src="/TestSessionInfoWeb/resources/images/loading.gif" style="left:50%;top:40%;position:absolute;"/>
</div>


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
    <td width="500" style="padding-left: 80px;">
        <!-- Begin content-->
        
		<form method="post" name="login" action="j_security_check" onsubmit="return sanitizeLogin();">
        <table class="simpleBlock" width="100%" cellpadding="5">
            <tr>
                <td colspan="2">
                <br/><h1>User Login</h1><br/><br/></td>
            </tr>
            <tr height="32">
                <td width="65px" align="right"><b>Username:</b></td>
                <td>
                	<input type="text" id="j_username" name="j_username" value="<%= username %>" 
                	maxlength="32" style="width: 180px" tabindex="1"
                	onkeypress="return handleEnterKey(event, this);" />
                </td>
            </tr>
            <tr height="32">
                <td width="65px" align="right"><b>Password:</b></td>
                <td>
                	<input type="password" id="j_password" name="j_password" value=""  
                	maxlength="32" style="width: 180px" tabindex="2"
                	onkeypress="return handleEnterKey(event, this);" />
                </td>
            </tr>
            <tr height="32">
                <td>&nbsp;</td>
                <td>                  
                	<a href="#" onclick="submitPage();" class="rounded {transparent} button" tabindex="3"
                	onfocus="handleFocus(event, this);" onblur="handleBlur(event, this);"
                	onkeypress="return handleEnterKey(event, this);" >
                	Log In
                	</a>                	
                </td>
            </tr>
            <tr height="32"> 
                <td colspan="2"><hr size="1"></td>
            </tr>
            <tr> 
                <td colspan="2"><h2>Forgot Your Password?</h2>
                <p class="hierarchyTableHeader">
                	<br/>If you forgot your password please enter your username <br>and click the "Get New Password" button. 
                	<br/>
                    <br/>If you do not know your username, contact your administrator.
                </p>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>
                	<a href="#" onclick="showLoading(); document.location = '/TestSessionInfoWeb/findPassword/FindPasswordController.jpf?username=' + document.getElementById('j_username').value" 
                	class="rounded {transparent} button" tabindex="4"
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
 
    <td width="50">&nbsp;</td>
 
    <!--Begin privacy Statement-->
    <td width="150" valign="top">
        <table border="0" cellspacing="0" cellpadding="0" class="hierarchyTableHeader">
        <tr>
            <td colspan="3" valign="middle"><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0">
           	<div class="roundedPrivacy">
            	About Your Privacy
            </div>
            </td>
        </tr>
        <tr>
            <td rowspan="3"><img src="<%=request.getContextPath()%>/resources/images/vertical_dotted_line.gif" width="1" height="185" border="0"></td>
            <td><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0"></td>
            <td><br>Any information that you send to CTB/McGraw-Hill via email or web-form will be used only for the purpose of processing your request.<br></td>
        </tr>
        <tr>
            <td><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0"></td>
            <td><a href="html/privacy_policy.html" onclick="return openPrivacyPolicyWindow('html/privacy_policy.html');" >Read our Privacy Policy</a></td>
        </tr>
        <tr>  
            <td><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0"></td>
            <td><a href="html/coppa_policy.html" onClick="return openCOPPAWindow('/TestSessionInfoWeb/resources/html/coppa_policy.html');">Review COPPA Policies</a> and its requirements for parental consents to collect or use personal information concerning children. </td>
        </tr>
        </table>
        <!--End privacy Statement-->
    </td>
</tr>


<tr>
<td align="left" valign="top" style="padding-left: 80px;">
<table class="simpleBlock" width="550" >
<tr>
    <td>
        <img src="<%=request.getContextPath()%>/resources/images/horizontal_dotted_line.gif" width="525" height="1" border="0">
    </td>
</tr>
 
<tr>
    <td>
        <span class="hierarchyTableHeader">Developed and published by CTB/McGraw-Hill LLC, a subsidiary of the McGraw-Hill
        Companies, Inc., 20 Ryan Ranch Road, Monterey, California 93940-5703.
        Copyright &copy; 2008 by CTB/McGraw-Hill LLC.  All rights reserved.  Only
        authorized customers may copy, download and print portions of the document
        located at www.ctb.com.  Any other use or reproduction of this document, in
        whole or in part, requires written permission of the publisher. 
        </span>
    </td>
</tr>
</table>
</td>
</tr>




</table>
<!--end main table-->
<br/><br/>

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
						        Subject to <a href="/html/terms_of_use.html" onClick="showTermsOfUseWindow(this.href); return false;">Terms of Use</a>.
						    </span>
						
						    <span>
						        Read our <a href="/html/privacy_policy.html" onClick="showPrivacyPolicyWindow(this.href); return false;">Privacy Policy Online</a>.
						    </span>
						
						    <span>
						        Review <a href="/html/coppa_policy.html" onClick="showCOPPAWindow(this.href); return false;">COPPA Policy</a>.
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