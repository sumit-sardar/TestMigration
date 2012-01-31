<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['installClient.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.installSoftware']}"/>
    <netui-template:setAttribute name="helpLinkLinux" value="${bundle.help['help.topic.installClientLinux']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<netui:form action="services_installSoftware">
<input type="hidden" id="menuId" name="menuId" value="installSoftwareLink" />
<table width="97%" style="margin:15px auto;" border="0"> 
	<tr>
		<td style="padding-left:5px;" colspan="2">
    		<h1><lb:label key="services.installSoftware.title" /></h1>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="buttonsRow">
			<div id="showSaveTestMessage" class="errMsgs" style="display: none; width: 50%; float: left;">
				<table>
					<tr>
						<td width="18" valign="middle">
							<div id="errorIcon" style="display:none;">
		                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
							</div>
							<div id="infoIcon" style="display:none;">
								<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif" border="0" width="16" height="16">
							</div>
						</td>
						<td class="saveMsgs" valign="middle">
							<div id="saveTestTitle"></div>
						</td>
					</tr>
				</table>				
			</div>
		</td>
	</tr>
	<tr> 
		<td style="padding-left:6px;" colspan="2">
			<p style="color:#000"><netui:content value="${bundle.web['installClient.message']}"/></p>
			<p align="right">
			    <a href="#" onClick="newWindow('/help/pdfs/min_sys_req_client_pc.pdf');return false;"><netui:content value="${bundle.web['installClient.viewSystemRequirements']}"/></a>
			</p> 
		</td>
	</tr>
	<tr class="transparent">
        <td class="transparent-top" colspan="2">&nbsp;</td>
    </tr>
				

   	<%-- Check Java Version --%>
    <tr class="transparent">
        <td class="transparent-top" width="20"><font size="6" color="#999999">1</font></td>    
        <td class="transparent-top">

            <h2><netui:content value="${bundle.web['installClient.java.title']}"/></h2>
            <p><netui:content value="${bundle.web['installClient.java.message']}"/></p>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent-top"></td>
        <td class="transparent-top">
            <table class="transparent">
                <tr id="installJavaRow" class="transparent">
                   <td class="transparent-top">
                        <netui:content value="<b>For PC</b>: Go to the Java website and download the current JRE update for free."/>
                        <br>
                        <a href="#" onclick="newWindow('http://www.java.com/en/');"><netui:content value="http://www.java.com/en/"/></a>
                        <br>
                    </td>
                </tr>
                <tr class="transparent">
                   <td class="transparent-top">
                        <netui:content value="<b>For Mac</b>: Open Software Update on the Mac workstation to update the JRE."/>
                        <br>
                    </td>
                </tr>
                <tr class="transparent">
                   <td class="transparent-top">
                        <netui:content value="<b>For Linux</b>: Use your distribution's package manager or software update tools (e.g., apt/yum/zypper) to install the JRE."/>
                        <br>
                    </td>
                </tr>
            </table>  
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent-top" colspan="2">
            <hr/>
        </td>
    </tr>


   	<%-- Additional Software --%>
    <tr class="transparent">
        <td class="transparent-top" width="20"><font size="6" color="#999999">2</font></td>    
        <td class="transparent-top">

            <h2><netui:content value="${bundle.web['installClient.addtionalSoftware.title']}"/></h2>
            <p><netui:content value="${bundle.web['installClient.addtionalSoftware.message']}"/></p>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent-top"></td>
        
        
        <td class="transparent-top">
            <table class="transparent">
                <tr id="installJavaRow" class="transparent">
                   <td class="transparent-top">
                        <netui:content value="<b>For PC and Linux</b>: Go to the Adobe website and download the latest Adobe AIR for free. Then log in to OAS again to install Online Assessment Software."/>
                        <br>
                        <a href="#" onclick="newWindow('http://www.adobe.com/go/getair');"><netui:content value="http://www.adobe.com/go/getair"/></a>
                        <br>
                    </td>
                </tr>
                <tr class="transparent">
                   <td class="transparent-top">
                        <netui:content value="<b>For Mac</b>: Go to the Adobe website and download the latest Adobe Flash for free."/>
                        <br>
                        <a href="#" onclick="newWindow('http://www.adobe.com/products/flashplayer/');"><netui:content value="http://www.adobe.com/products/flashplayer/"/></a>
                        <br>
                    </td>
                </tr>
            </table>  
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent-top" colspan="2">
            <hr/>
        </td>
    </tr>


   	<%-- Install Online Assessment Software --%>
    <tr class="transparent">
        <td class="transparent-top" width="20"><font size="6" color="#999999">3</font></td>    
        <td class="transparent-top">
            <h2><netui:content value="${bundle.web['installClient.installClient.title']}"/></h2>
            <p><netui:content value="${bundle.web['installClient.title.message']}"/></p>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent-top">
        </td>
        <td class="transparent-top">
            
            <table class="transparent" width="100%">

                <tr class="transparent">
                    <td class="transparent-top" width="5%">&nbsp;</td>
                    <td class="transparent" width="40%">
                    	<h2><netui:content value="${bundle.web['installClient.standardInstall.header']}"/></h2>
                    </td>
                    <td class="transparent-top" width="10%">&nbsp;</td>
                    <td class="transparent-top" width="5%">&nbsp;</td>
                    <td class="transparent" width="40%">
                    	<h2><netui:content value="${bundle.web['installClient.customInstall.header']}"/></h2>
                    </td>
                </tr>
            
            	<%-- PC --%>
                <tr id="installPCClientRow" class="transparent">
                   <td class="transparent-top" width="5%">
                        <img class="transparent-top" src="../resources/images/legacy/icon_pc.gif" width="52" height="33"/>
                   </td>
                   <td class="transparent-top" width="40%">
                        <b><netui:content value="${bundle.web['installClient.windows.clientName']}"/></b><br>
                        <netui:content value="${bundle.web['installClient.windows.version']}"/><br>
                        <i><netui:content value="${bundle.web['installClient.windows.OS']}"/></i><br>
                        <% String href_PC = (String)request.getAttribute("downloadURI_PC"); %>                    
	                	<a href="#" onclick="<%= href_PC %>" class="rounded {transparent} button" tabindex="1" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText']}"/>
	                	</a>                	                                    
                    </td>
                    <td class="transparent-top" width="10%">&nbsp;</td>
                    <td class="transparent-top" width="5%">
                        <img class="transparent-top" src="../resources/images/legacy/icon_pc.gif" width="52" height="33"/>
                    </td>
                   <td class="transparent-top" width="40%">
                        <b><netui:content value="${bundle.web['installClient.windows.clientName2']}"/></b><br>
                        <netui:content value="${bundle.web['installClient.windows.version']}"/><br>
                        <i><netui:content value="${bundle.web['installClient.windows.OS']}"/></i><br>
                        <% String href_RUNPC = (String)request.getAttribute("runURI_PC"); %>                    
	                	<a href="#" onclick="<%= href_RUNPC %>" class="rounded {transparent} button" tabindex="1" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText2']}"/>
	                	</a>                	                                    
                    </td>
                </tr>         
                
                <tr class="transparent">
                    <td class="transparent" height="25" colspan="5"/>
                </tr>
                
            	<%-- Mac OS --%>
		        <tr id="installMacClientRow" class="transparent">
		            <td class="transparent-top" width="5%">
		                <img class="transparent" src="../resources/images/legacy/icon_macX.gif"/>
		            </td>
		            <td class="transparent-top" width="40%">
		                <b><netui:content value="${bundle.web['installClient.mac.clientName']}"/></b><br>
		                <netui:content value="${bundle.web['installClient.mac.version']}"/><br>
		                <i><netui:content value="${bundle.web['installClient.mac.OS']}"/></i><br>
                        <% String href_MAC = (String)request.getAttribute("downloadURI_MAC"); %>
	                	<a href="#" onclick="<%= href_MAC %>" class="rounded {transparent} button" tabindex="2" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText']}"/>
	                	</a>                	                                    
		            </td>
                    <td class="transparent-top" width="10%">&nbsp;</td>
		            <td class="transparent-top" width="5%">
		                <img class="transparent" src="../resources/images/legacy/icon_macX.gif"/>
		            </td>
		            <td class="transparent-top" width="40%">
		                <b><netui:content value="${bundle.web['installClient.mac.clientName2']}"/></b><br>
		                <netui:content value="${bundle.web['installClient.mac.version']}"/><br>
		                <i><netui:content value="${bundle.web['installClient.mac.OS']}"/></i><br>
                        <% String href_RUNMAC = (String)request.getAttribute("runURI_MAC"); %>
	                	<a href="#" onclick="<%= href_RUNMAC %>" class="rounded {transparent} button" tabindex="2" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText2']}"/>
	                	</a>                	                                    
		            </td>
		        </tr>     

                <tr class="transparent">
                    <td class="transparent" height="25" colspan="5"/>
                </tr>
                
            	<%-- Linux --%>
		        <tr id="installLinuxClientRow" class="transparent">
		            <td class="transparent-top" width="5%">
		                <img class="transparent" src="../resources/images/legacy/icon_linux.png"/>
		            </td>
		            <td class="transparent-top" width="40%">
		                <b><netui:content value="${bundle.web['installClient.linux.clientName']}"/></b><br>
		                <netui:content value="${bundle.web['installClient.linux.version']}"/><br>
		                <i><netui:content value="${bundle.web['installClient.linux.OS']}"/></i><br>
<div id="allowDownload" style="display:none">		            
                        <% String href_LINUX = (String)request.getAttribute("downloadURI_LINUX"); %>
	                	<a href="#" onclick="<%= href_LINUX %>" class="rounded {transparent} button" tabindex="3" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText']}"/>
	                	</a>                	                                    
</div>		     
<div id="notAllowDownload" style="display:none">		            
                        <% String alertMsg = "alert('Use Mozilla Firefox to download the installer.');"; %>
	                	<a href="#" onclick="<%= alertMsg %>" class="rounded {transparent} button" tabindex="3" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText']}"/>
	                	</a>                	                                    
</div>		     		                
Use <a href="<netui-template:attribute name="helpLinkLinux"/>" onClick="return showHelpWindow(this.href);">root / sudo access</a> to install.
		            </td>
                    <td class="transparent-top" width="10%">&nbsp;</td>
		            <td class="transparent-top" width="5%">
		                <img class="transparent" src="../resources/images/legacy/icon_linux.png"/>
		            </td>
		            <td class="transparent-top" width="40%">
		                <b><netui:content value="${bundle.web['installClient.linux.clientName2']}"/></b><br>
		                <netui:content value="${bundle.web['installClient.linux.version']}"/><br>
		                <i><netui:content value="${bundle.web['installClient.linux.OS']}"/></i><br>
<div id="allowDownload2" style="display:none">		            
                        <% String href_RUNLINUX = (String)request.getAttribute("runURI_LINUX"); %>
	                	<a href="#" onclick="<%= href_RUNLINUX %>" class="rounded {transparent} button" tabindex="3" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText2']}"/>
	                	</a>                	                                    
</div>		     
<div id="notAllowDownload2" style="display:none">		            
                        <% String alertMsg2 = "alert('Use Mozilla Firefox to download the installer.');"; %>
	                	<a href="#" onclick="<%= alertMsg2 %>" class="rounded {transparent} button" tabindex="3" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText2']}"/>
	                	</a>                	                                    
</div>		     		                
		            </td>
		        </tr>     

            </table>
			
</td>	
	</tr>
</table>
</netui:form>


<script type="text/javascript">
	allowDownLoadBinFile();
</script>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", "installSoftwareLink");
});
</script>


<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


