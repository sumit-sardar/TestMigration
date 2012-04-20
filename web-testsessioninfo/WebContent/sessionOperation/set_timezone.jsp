<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.io.*, java.util.*"%>
<%@ page import="com.ctb.util.web.sanitizer.JavaScriptSanitizer"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%
	String errorMsg = (String)request.getAttribute("errorMsg");
%>
<html>

<head>
  	<title>Set Timezone</title>

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


<form method="post" action="saveTimeZone.do">

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
    <lb:label key="timezone.title"/>
</h1>      

<p>
    <lb:label key="timezone.message"/>
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
<table class="transparent" border=0>
<tr>
<td class="class="transparent-top"" width="500">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="First Name:"/></td>
        <td class="transparent"><netui:label value="${pageFlow.userProfile.firstName}"/></td>
    </tr>
    <!-- 
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Middle Name:"/></td>
        <td class="transparent"><netui:label value="${pageFlow.userProfile.middleName}"/></td>
    </tr>
     -->
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Last Name:"/></td>
        <td class="transparent"><netui:label value="${pageFlow.userProfile.lastName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Login ID:"/></td>
        <td class="transparent"><netui:label value="${pageFlow.userProfile.loginId}"/></td>
    </tr>
    <%--CR Dex --%>
    <tr class="transparent">
        <td class="transparent-top" width="100"><netui:content value="Organization:"/></td>
        <td class="transparent-top">
            <table class="transparent">
            <netui-data:repeater dataSource="requestScope.organizationNodes">
                <netui-data:repeaterItem>            
                <tr class="transparent">
                    <td class="transparent-small">
                    <netui:content value="${container.item.orgNodeName}"/>
                    </td>
                </tr>
                </netui-data:repeaterItem>
            </netui-data:repeater>
            </table>  
        </td>
    </tr>
</table>
</td>
<td class="class="transparent-top"" width="500">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Email:"/></td>
        <td class="transparent"><netui:label value="${pageFlow.userProfile.email}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Role:"/></td>
        <td class="transparent"><netui:label value="${pageFlow.userProfile.roleInitCap}"/></td>
    </tr>
     <%--CR Dex --%>
    <tr class="transparent">
        <td class="transparent" width="100" nowrap><netui:content value="External User Id:"/></td>
        <td class="transparent"><netui:label value="${pageFlow.userProfile.extPin1}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Time Zone:"/></td>
        <td class="transparent">
            <netui:select optionsDataSource="${pageFlow.timeZoneOptions}" dataSource="pageFlow.userProfile.timeZone" size="1" style="width:200px"/>
            &nbsp;
            <a href="#" onclick="submitPage();" class="rounded {transparent} button" 
	 		onfocus="handleFocus(event, this);" onblur="handleBlur(event, this);"
	 		onkeypress="return handleEnterKey(event, this);" >
	 		<lb:label key="common.button.save"/>
	 		</a>                	
            
        </td>                                
    </tr>
    
</table>
</td>

</tr>


</table>

                </td>
            </tr>
            </table>
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
						        <lb:label key="common.footer1.message"/>&copy;<lb:label key="common.footer2.message" prefix="&nbsp;"/>
						    </span>
						
						    <span>
						        <lb:label key="common.footer3.message" suffix="&nbsp;"/><a href="<%=request.getContextPath()%>/resources/html/terms_of_use.html" onClick="showTermsOfUseWindow(this.href); return false;"><lb:label key="common.footer4.message"/></a>.
						    </span>
						
						    <span>
						        <lb:label key="common.footer5.message" suffix="&nbsp;"/><a href="<%=request.getContextPath()%>/resources/html/privacy_policy.html" onClick="showPrivacyPolicyWindow(this.href); return false;"><lb:label key="common.footer6.message"/></a>.
						    </span>
						
						    <span>
						        <lb:label key="common.footer7.message" suffix="&nbsp;"/><a href="<%=request.getContextPath()%>/resources/html/coppa_policy.html" onClick="showCOPPAWindow(this.href); return false;"><lb:label key="common.footer8.message"/></a>.
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