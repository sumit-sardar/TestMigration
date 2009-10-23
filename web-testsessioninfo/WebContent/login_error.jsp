<%@ page import="java.io.*, java.util.*"%>
<%@ page import="com.ctb.util.web.sanitizer.JavaScriptSanitizer"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%
	java.util.ResourceBundle rb = java.util.PropertyResourceBundle.getBundle("errorMessages");

    String username = (String)request.getParameter("j_username");
    if (username == null)
        username = (String)request.getAttribute("username");
    if (username == null)
        username = "";
    username = JavaScriptSanitizer.sanitizeInput(username);

    String password = (String)request.getParameter("j_password");
    if (password == null)
        password = "";
    password = JavaScriptSanitizer.sanitizeInput(password);
        
    String errorMsg = (String)request.getAttribute("errorMsg");
    if (errorMsg == null)
        errorMsg = rb.getString("invalidCredential");
        
    Cookie[] cookies = (Cookie[])request.getCookies();
    if (cookies != null && cookies.length > 0 ) {    
        for (int i=0 ; i<cookies.length ; i++) {
            Cookie c = cookies[i];
            if (c != null) {
                if (c.getName().equals("TAS_SESSIONID") || c.getName().equals("_wl_authcookie_")) {                
                    Cookie r = new Cookie(c.getName(), null);
                    r.setMaxAge(0);
                    r.setPath("/");
                    response.addCookie(r);
                    cookies[i] = null;
                }
            }
        }
    }        
%>

<html>

<head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">

  <title>User Login</title>
  <link href="<%=request.getContextPath()%>/resources/css/R1_stylesheet.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.js"></script>  
  <script type="text/javascript" src="<%=request.getContextPath()%>/sanitize.js"></script>  
</head>

<body leftmargin="0" topmargin="0">

<!-- H E A D E R -->
<table border="0" cellpadding="0" cellspacing="0" width="100%"><!--table 1 start-->
<tr valign="bottom">
  <td align="left" width="176"><img src="<%=request.getContextPath()%>/resources/images/ctb_logo.gif" width="176" height="71" border="0" alt="CTB/McGraw-Hill" /></td>
  <td><IMG height=24 src="<%=request.getContextPath()%>/resources/images/transparent.gif" width=75 border=0></td>
</tr>
</table>
<!-- H E A D E R -->

<table border="0" cellpadding="0" cellspacing="0" width="100%" class="bgwhite"><!--table 3 (menu/body) start-->
<tr valign="top">
	<td width="50"><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="50"></td>
	<td width="700">
	

<!-- BEGIN   M A I N    B O D Y     CONTENT ******************************** -->
<table border="0" cellspacing="0" cellpadding="0">
<tr>
    <td>
        <!-- Begin content-->
        <form method="post" name="login" action="j_security_check" onsubmit="return sanitizeLogin();">
            <table border="0" cellspacing="0" cellpadding="0" width="325">
            <tr>
                <td colspan="3">
                <br><span class="headerart">User Login</span><br></td>
            </tr>            
            <tr>
                <td colspan="3">
                <br/>
                <div style="background-color: #ffc; border-color: #000; border-style: solid; border-width: 1px; padding: 5px;">
                    <table>
                        <tr>
                            <td valign="middle">
                                <img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="32" height="32">
                            </td>
                            <td valign="middle">
                                <font color="red"><span><%= errorMsg %></span></font>
                            </td>
                        </tr>
                    </table>
                </div>
                <br/>
                </td>
            </tr>
            <tr>
                <td class="infotablelabels">Username:</td>
                <td><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0"></td>
                <td class="inputtext"><input type="text" id="j_username" name="j_username" value="<%= username %>" size="32" maxlength="32" style="width: 180px" /></td>
            </tr>
            <tr>
                <td class="infotablelabels">Password:</td>
                <td><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0"></td>
                <td class="inputtext"><input type="password" id="j_password" name="j_password" value="<%= password %>" size="32" maxlength="32" style="width: 180px" /></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0"></td>
                <td>
                    <br/>
                    <netui:button type="submit" value="Log In"/>    
                </td>
            </tr>
            <tr> 
                <td colspan="3"><hr size="1"></td>
            </tr>
            <tr>
                <td class="hierarchyTableWhite" colspan="3"><span class="headerart2">Forgot Your Password?</span><br>If you forgot your password please enter your username <br>and click the "Get New Password" button. <br>
                    <br>If you do not know your username, contact your administrator.
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
                <td class="hierarchyTableWhite" colspan="3">
                    <br/>
                    <netui:button type="button" value="Get New Password" onClick="document.location = '/TestSessionInfoWeb/findPassword/FindPasswordController.jpf?username=' + document.getElementById('j_username').value"/>
                </td> 
            </tr>
            </table>
        </form>
        <br>
        <br>
        <br>
        <!--End content-->
    </td>
    <!--begin spacer colum-->
    <td width="75">&nbsp;</td>
    <!--end spacer colum-->

    <!--Begin privacy Statement-->
    <td width="150" valign="top">
        <table border="0" cellspacing="0" cellpadding="0">
        <tr class="HierarchyTableHeader">
            <td colspan="3" valign="middle"><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0">About Your Privacy</td>
        </tr>
        <tr>
            <td rowspan="3"><img src="<%=request.getContextPath()%>/resources/images/vertical_dotted_line.gif" width="1" height="185" border="0"></td>
            <td><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0"></td>
            <td><br>Any information that you send to CTB/McGraw-Hill via email or web-form will be used only for the purpose of processing your request.<br></td>
        </tr>
        <tr>
            <td><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0"></td>
            <td><a href="<%=request.getContextPath()%>/resources/html/privacy_policy.html" onClick="return openPrivacyPolicyWindow('<%=request.getContextPath()%>/resources/html/privacy_policy.html')">Read our Privacy Policy</a></td>
        </tr>
        <tr>  
            <td><img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="6" height="24" border="0"></td>
            <td><a href="<%=request.getContextPath()%>/resources/html/coppa_policy.html" onClick="return openCOPPAWindow('<%=request.getContextPath()%>/resources/html/coppa_policy.html')">Review COPPA Policies</a> and its requirements for parental consents to collect or use personal information concerning children. </td>
        </tr>
        </table>
        <!--End privacy Statement-->
    </td>
</tr>
</table>
<!--end main table-->

<!--begin copyright table-->
<table cellspacing="0" cellpadding="0" border="0" width="550">
<tr>
    <td>
        <img src="<%=request.getContextPath()%>/resources/images/horizontal_dotted_line.gif" width="325" height="1" border="0">
    </td>
</tr>
<%
	java.util.ResourceBundle bundle = java.util.PropertyResourceBundle.getBundle("oasResources");
	String tllogout = bundle.getString("ctb.turnleaf.logouturl");
%>
<tr>
    <td>
        <span class="hierarchyTableWhite">Developed and published by CTB/McGraw-Hill LLC, a subsidiary of the McGraw-Hill
        Companies, Inc., 20 Ryan Ranch Road, Monterey, California 93940-5703.
        Copyright &copy; 2008 by CTB/McGraw-Hill LLC.  All rights reserved.  Only
        authorized customers may copy, download and print portions of the document
        located at www.ctb.com.  Any other use or reproduction of this document, in
        whole or in part, requires written permission of the publisher. 
        </span>
    </td>
</tr>
</table>
<!-- END   M A I N    B O D Y     CONTENT ******************************** -->
	</td>
</tr>
</table>


<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
    <td class="bgtaupe">
    <br>
    <img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="12" height="1" border="0" />
    <span class="smlight">Copyright &copy; 2008 by CTB/McGraw-Hill LLC. All rights reserved. &nbsp; 
    Subject to <a href="<%=request.getContextPath()%>/resources/html/terms_of_use.html" onClick="return openTermsOfUseWindow('<%=request.getContextPath()%>/resources/html/terms_of_use.html')" class="smlight">Terms of Use</a>. &nbsp; 
    Read our <a href="<%=request.getContextPath()%>/resources/html/privacy_policy.html" onClick="return openPrivacyPolicyWindow('<%=request.getContextPath()%>/resources/html/privacy_policy.html')" class="smlight">Privacy Policy Online</a>. &nbsp; 
    Review <a href="<%=request.getContextPath()%>/resources/html/coppa_policy.html" onClick="return openCOPPAWindow('<%=request.getContextPath()%>/resources/html/coppa_policy.html')" class="smlight">COPPA Policy</a>.<br />
    <img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="1" height="6" border="0" alt="" /></span>
    </td>
</tr>
<tr>
    <td><img src="<%=request.getContextPath()%>/resources/images/mh_logo_v2.jpg" width="221" height="16" border="0" /></td>
</tr>
</table>

</body>
</html>