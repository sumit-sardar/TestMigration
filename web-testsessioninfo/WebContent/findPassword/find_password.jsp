<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%
    String errorMsg = (String)request.getAttribute("errorMsg");
%>

<html>
<head>
  <title>Get New Password</title>
  <link href="<%=request.getContextPath()%>/resources/css/R1_stylesheet.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.js"></script>  
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
        <!--Begin content-->
        <!--Begin content-->
        <form method="post" name="findpassword" action="getPassword.do">
            <table border="0" cellspacing="0" cellpadding="5">
            <tr>
                <td colspan="2">
                    <span class="headerart">Get New Password</span><br/>
                    Required fields are marked by a blue asterisk <span class="asterisk">*</span>.
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
                                <img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="32" height="32">
                            </td>
                            <td align="center">
                                <font color="red"><span><%= errorMsg %></span></font>
                            </td>
                        </tr>
                    </table>
                </div>
                <br/>
                </td>
            </tr>
<% } %>            
            <tr>
                <td width="100" align="right">Username:</td>
                <td width="*" align="left"><netui:span value="${pageFlow.user.userName}"/></td>
            </tr>
            <tr>
                <td width="100" align="right">Hint:</td>
                <td width="*" align="left"><netui:span value="${pageFlow.user.passwordHintQuestion}"/></td>
            </tr>
            <tr>
                <td width="100" align="right"><span class="asterisk">*</span> Hint Answer:</td>
                <td width="*" align="left">
                    <netui:textBox dataSource="pageFlow.hintAnswer" maxlength="255" style="width: 200px" onKeyPress="return ignoreEnterKey( event );"/>
                </td>
            </tr>
            <tr>
                <td width="100" align="right"><span class="asterisk">*</span> Email:</td>
                <td width="*" align="left">
                    <netui:textBox dataSource="pageFlow.email" maxlength="64" style="width: 200px" onKeyPress="return ignoreEnterKey( event );"/>
                </td>
            </tr>
            
            <tr>
                <td colspan="2">
                    <br/>
                    <netui:button type="submit" value="Cancel" action="backToLogin"/>
                    <netui:button type="submit" value="Get New Password" action="getPassword"/>
                </td>
            </tr>
            </table>
        </form>
    </td>
</tr>
</table>
<!--end main table-->

<!-- END   M A I N    B O D Y     CONTENT ******************************** -->
	</td>
</tr>
</table>


<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
    <td class="bgtaupe">
    <br>
    <img src="<%=request.getContextPath()%>/resources/images/transparent.gif" width="12" height="1" border="0" />
    <span class="smlight">Copyright &copy; 2006 by CTB/McGraw-Hill LLC. All rights reserved. &nbsp; 
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