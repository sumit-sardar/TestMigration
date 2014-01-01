<%@ page import="java.io.*, java.util.*"%>
<%@ page import="com.ctb.util.web.sanitizer.JavaScriptSanitizer"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<lb:bundle baseName="testsessionApplicationResource" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>

<head>
  	<title>Choose Customer to Login</title>

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
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/sanitize.js"></script>    
     
</head>

<body leftmargin="0" topmargin="0">


<!-- BLOCK DIV -->
<div id="blockDiv" style="display:none; background-color: #d0e5f5; opacity:0.5; position:fixed;top:0;left:0;width:100%;height:100%;z-index:9999">
	<img src="/SessionWeb/resources/images/loading.gif" style="left:50%;top:40%;position:absolute;"/>
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
    <td width="700" style="padding-left: 80px; padding-bottom: 150px">
        <!-- Begin content-->
        <table class="simpleBlock" width="100%" cellpadding="5">
            <tr>
                <td colspan="2">
                	<br/><h1>Switch Administration</h1>
                	<p style="color: #476CB5;">Click one of the links below to manage the EOI or grade 6-8 online test administration.</p>
                </td>                
            </tr>
         </table>
         <table style="margin-left:40px;">          
            <tr>
                <td><h4>
	                <li style="list-style-type: square;">
	                	<a onclick="showLoading();" href="/SessionWeb/sessionOperation/switchToLinkSelected.do?selectedLink=EOI_Link">Manage the Oklahoma EOI online test administration</a>
	                </li>
                </h4></td>
            </tr>
            <tr>
	            <td><h4>
	                <li style="list-style-type: square;">
	                	<a onclick="showLoading();" href="/SessionWeb/sessionOperation/switchToLinkSelected.do?selectedLink=3-8_Link">Manage the Oklahoma 6-8 online test administration</a>
	                </li>
	            </h4></td>
            </tr>
            <ctb:auth roles="Administrator">
            <tr>
                <td><h4>
                <li style="list-style-type: square;">	
                	<a onclick="showLoading();" href="/SessionWeb/sessionOperation/switchToLinkSelected.do?selectedLink=UserLink">Manage user accounts (shared between EOI and 6-8)</a>
                </li>
            	</h4></td>
            </tr>
            </ctb:auth>
         </table>       		
        <!--End content-->
    </td>
 
   
				</tr>
				<tr><td></td></tr>
				<tr><td></td></tr>					
			</table>
		</td>
	</tr>
	
	<!-- FOOTER SECTION -->
				<tr>
				  <td align="left" valign="top">
				  
					<table class="footerLayout">
					  <tr>
						<td class="footerLayout">
						    <span>
						        <lb:label key="common.footer1.message"/> &copy; <lb:label key="common.footer2.message"/>
						    </span>
						
						    <span>
						        <lb:label key="common.footer3.message"/> <a href="<%=request.getContextPath()%>/resources/html/terms_of_use.html" onClick="showTermsOfUseWindow(this.href); return false;"><lb:label key="common.footer4.message"/></a>.
						    </span>
						
						    <span>
						        <lb:label key="common.footer5.message"/> <a href="<%=request.getContextPath()%>/resources/html/privacy_policy.html" onClick="showPrivacyPolicyWindow(this.href); return false;"><lb:label key="common.footer6.message"/></a>.
						    </span>
						
						    <span>
						        <lb:label key="common.footer7.message"/> <a href="<%=request.getContextPath()%>/resources/html/coppa_policy.html" onClick="showCOPPAWindow(this.href); return false;"><lb:label key="common.footer8.message"/></a>.
						    </span>
						</td>
					  </tr>
					</table>
				  </td>
				</tr>
	


</table>

</body>
</html>