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
<%! static String null_href = "location.href='null'"; %>
<% 
	Boolean isISTEPCustomer = (Boolean)request.getAttribute("isISTEPCustomer");
	Boolean isLasLinkCustomer = (Boolean)request.getAttribute("isLasLinkCustomer");
%>
        
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
			<br/>
			<p style="color:#000"><netui:content value="${bundle.web['installClient.viewSystemRequirements.message']}"/></p>
			<p align="right" style="padding-top:5px;">
               <% if (isLasLinkCustomer.booleanValue()) { %>
			    	<a href="#" onClick="newWindow('/help/pdfs/min_sys_req_client_pc_LAS.pdf');return false;"><netui:content value="${bundle.web['installClient.viewSystemRequirements']}"/></a>
               <% } else { %>
			    	<a href="#" onClick="newWindow('/help/pdfs/min_sys_req_client_pc.pdf');return false;"><netui:content value="${bundle.web['installClient.viewSystemRequirements']}"/></a>
               <% } %>
			
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

            <h2><netui:content value="${bundle.web['installClient.java.title.Las']}"/></h2>
            <p><netui:content value="${bundle.web['installClient.java.message.Las']}"/></p>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent-top"></td>
        <td class="transparent-top">
            <table class="transparent">
                <tr id="installJavaRow" class="transparent">
                   <td class="transparent-top">
                        <netui:content value="<b>For PC and MAC</b>: : Go to this Java website and download the supported Java SE Runtime Environment for free."/>
                        <br>
                        <a href="#" onclick="newWindow(' http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html#jre-7u45-oth-JPR');"><netui:content value=" http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html#jre-7u45-oth-JPR"/></a>
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

            <h2><netui:content value="${bundle.web['installClient.addtionalSoftware.title.Las']}"/></h2>
            <p><netui:content value="${bundle.web['installClient.addtionalSoftware.message.Las']}"/></p>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent-top"></td>
        
        
        <td class="transparent-top">
            <table class="transparent">
                <tr id="installJavaRow" class="transparent">
                    <td class="transparent-top">
                        <netui:content value="<b>For PC and MAC</b>: Go to this Adobe website and download the supported Adobe AIR for free."/>
                        <br>
                        <a href="#" onclick="newWindow('http://helpx.adobe.com/air/kb/archived-air-sdk-version.html');"><netui:content value="http://helpx.adobe.com/air/kb/archived-air-sdk-version.html"/></a>
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
	

		<%-- Check Flash and Flash Plug-in Versions --%>

    <tr class="transparent">

        <td class="transparent-top" width="20"><font size="6" color="#999999">3</font></td>    

        <td class="transparent-top">

            <h2><netui:content value="${bundle.web['installClient.Flash.checkversion.title']}"/></h2>

            <p><netui:content value="${bundle.web['installClient.Flash.checkversion.message']}"/></p>

        </td>

    </tr>

	

	<tr class="transparent">

        <td class="transparent-top"></td>

               

        <td class="transparent-top">

            <table class="transparent">

                <tr id="installFlashPlayer" class="transparent">

                   <td class="transparent-top">

                   		<netui:content value="<b>For PC and MAC </b> - Go to this Adobe website and download the supported Flash player for free"/>

                        <br>

                        <netui:content value="<b>Flash Player: </b>"/>

                        <br> 

                        <a href="#" onclick="newWindow('http://helpx.adobe.com/flash-player/kb/archived-flash-player-versions.html#Flash%20Player%20archives');"><netui:content value="http://helpx.adobe.com/flash-player/kb/archived-flash-player-versions.html#Flash%20Player%20archives"/></a>

                        <br><br>                       
						
						<netui:content value="<b>For PC </b> - Go to this Adobe Website and download the supported “Flash Player Plugin content debugger (for Netscape-compatible browsers)” for free"/>
						
						<br>
							
                         <netui:content value="<b>Flash _Player Plug-in:</b>"/>

                        <br>

                        <a href="#" onclick="newWindow('https://www.adobe.com/support/flashplayer/downloads.html');"><netui:content value="https://www.adobe.com/support/flashplayer/downloads.html"/></a>
                      
                        
                        <br><br>                       
						
						<netui:content value="<b>For MAC </b> -  Go to this Adobe Website and download the supported “Macintosh Flash Player Plugin content debugger” for free"/>
						
						<br>
							
                         <netui:content value="<b>Flash Player Plug-in:</b>"/>

                        <br>

                        <a href="#" onclick="newWindow('https://www.adobe.com/support/flashplayer/ ');"><netui:content value="https://www.adobe.com/support/flashplayer/"/></a>

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

	
    <% String href_PC = (String)request.getAttribute("downloadURI_PC");
       String href_RUNPC = (String)request.getAttribute("runURI_PC");
       String href_MAC = (String)request.getAttribute("downloadURI_MAC");
       String href_RUNMAC = (String)request.getAttribute("runURI_MAC");
       String href_LINUX = (String)request.getAttribute("downloadURI_LINUX");
       String href_RUNLINUX = (String)request.getAttribute("runURI_LINUX");
       boolean isStdPcInstallMissing = !(href_PC!=null && !href_PC.isEmpty()&& !null_href.equals(href_PC));
       boolean isStdMacInstallMissing =!(href_MAC!=null && !href_MAC.isEmpty()&& !null_href.equals(href_MAC));
       boolean isStdLinuxInstallMissing= !(href_LINUX!=null && !href_LINUX.isEmpty() && !null_href.equals(href_LINUX));
       boolean isStandardInstallerPresent = !null_href.equals(href_PC) || !null_href.equals(href_MAC) || !null_href.equals(href_LINUX);
       boolean isCustomInstallerPresent = !null_href.equals(href_RUNPC) || !null_href.equals(href_RUNMAC) || !null_href.equals(href_RUNLINUX);
       boolean isInstallerPresent = isStandardInstallerPresent || isStandardInstallerPresent;
       
       if(isInstallerPresent)
       {
    %>
	
   	<%-- Install Online Assessment Software --%>
    <tr class="transparent">
        <td class="transparent-top" width="20"><font size="6" color="#999999">4</font></td>    
        <td class="transparent-top">
            <h2><netui:content value="${bundle.web['installClient.installClient.title']}"/></h2>
            <p><netui:content value="${bundle.web['installClient.title.message.Las']}"/></p>
        </td>
    </tr>
    	<%} %>	
    <tr class="transparent">
        <td class="transparent-top">
        </td>
        <td class="transparent-top">
            
            <table class="transparent" width="100%">

                <tr class="transparent">
                <% if(isStandardInstallerPresent){ %>
                    <td class="transparent-top" width="5%">&nbsp;</td>
                    <td class="transparent" width="40%">
                    	<h2><netui:content value="${bundle.web['installClient.standardInstall.header']}"/></h2>
                    </td>
                 <%} %>   
                    <td class="transparent-top" width="10%">&nbsp;</td>
                    <% if (isCustomInstallerPresent){ 
                     if (!isLasLinkCustomer.booleanValue()) { %>
                    <td class="transparent-top" width="5%">&nbsp;</td>
                    <td class="transparent" width="40%">
                    	<h2><netui:content value="${bundle.web['installClient.customInstall.header']}"/></h2>
                    </td>
                     <% }} %>
                </tr>
            
            	<%-- PC --%>
                <tr id="installPCClientRow" class="transparent">
                <% 
                   if(!isStdPcInstallMissing)
                   { %>
                   <td class="transparent-top" width="5%">
                        <img class="transparent-top" src="../resources/images/legacy/icon_pc.gif" width="52" height="33"/>
                   </td>
                   <td class="transparent-top" width="40%">
                        <b><netui:content value="${bundle.web['installClient.windows.clientName']}"/></b><br>
                        <i><netui:content value="${bundle.web['installClient.windows.OS.Las']}"/></i><br>
                        <a href="#" onclick="<%= href_PC %>" class="rounded {transparent} button" tabindex="1" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText']}"/>
	                	</a>                	                                    
                    </td>
                    <%}
                    
                     if(href_RUNPC!=null && !href_RUNPC.isEmpty() && !null_href.equals(href_RUNPC))
                     { 
                    	 if(isStdPcInstallMissing)
                    	 {
                     %>
                      <td class="transparent-top" width="5%">
                      <td class="transparent-top" width="40%">
                      
                      <%} %>
                      
                    <td class="transparent-top" width="10%">&nbsp;</td>
                     <% if (!isLasLinkCustomer.booleanValue()) { %>
                    <td class="transparent-top" width="5%">
                        <img class="transparent-top" src="../resources/images/legacy/icon_pc.gif" width="52" height="33"/>
                    </td>
                   <td class="transparent-top" width="40%">
                        <b><netui:content value="${bundle.web['installClient.windows.clientName2']}"/></b><br>
                        <i><netui:content value="${bundle.web['installClient.windows.OS']}"/></i><br>
                                            
	                	<a href="#" onclick="<%= href_RUNPC %>" class="rounded {transparent} button" tabindex="1" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText2']}"/>
	                	</a>                	                                    
                    </td>
                    <% } }%>
                </tr>         
                
                <tr class="transparent">
                    <td class="transparent" height="25" colspan="5"/>
                </tr>
                
            	<%-- Mac OS --%>
		        <tr id="installMacClientRow" class="transparent">
		        <%  
            	 if(!isStdMacInstallMissing)
                 {
            	%>
		            <td class="transparent-top" width="5%">
		                <img class="transparent" src="../resources/images/legacy/icon_macX.gif"/>
		            </td>
		            <td class="transparent-top" width="40%">
		                <b><netui:content value="${bundle.web['installClient.mac.clientName']}"/></b><br>
		                <i><netui:content value="${bundle.web['installClient.mac.OS.Las']}"/></i><br>
                        <a href="#" onclick="<%= href_MAC %>" class="rounded {transparent} button" tabindex="2" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText']}"/>
	                	</a>                	                                    
		            </td>
		            <td class="transparent-top" width="10%">&nbsp;</td>
		        <%}
            	     
            	      if(href_RUNMAC!=null && !href_RUNMAC.isEmpty() && !null_href.equals(href_RUNMAC))
                      {
            	    	  if(isStdMacInstallMissing){
            	     %>            
                    <td class="transparent-top" width="5%"></td>
                    <td class="transparent-top" width="40%"></td>
                    <td class="transparent-top" width="10%">&nbsp;</td>
                    <%} %> 
                     <% if (!isLasLinkCustomer.booleanValue()) { %>
		            <td class="transparent-top" width="5%">
		                <img class="transparent" src="../resources/images/legacy/icon_macX.gif"/>
		            </td>
		            <td class="transparent-top" width="40%">
		                <b><netui:content value="${bundle.web['installClient.mac.clientName2']}"/></b><br>
		                <i><netui:content value="${bundle.web['installClient.mac.OS']}"/></i><br>
                        <a href="#" onclick="<%= href_RUNMAC %>" class="rounded {transparent} button" tabindex="2" >
	                		<netui:content value="${bundle.web['installClient.windows.buttonText2']}"/>
	                	</a>                	                                    
		            </td>
		            <% }} %>
		        </tr>     

                <tr class="transparent">
                    <td class="transparent" height="25" colspan="5"/>
                </tr>
                
            	<%-- Linux --%>
    <% if (!isLasLinkCustomer.booleanValue()) { %>            	
		        <tr id="installLinuxClientRow" class="transparent">
		        <%if(!isStdLinuxInstallMissing)
			             { 
			             %>
		            <td class="transparent-top" width="5%">
		                <img class="transparent" src="../resources/images/legacy/icon_linux.png"/>
		            </td>
		            <td class="transparent-top" width="40%">
		                <b><netui:content value="${bundle.web['installClient.linux.clientName']}"/></b><br>
		                <i><netui:content value="${bundle.web['installClient.linux.OS']}"/></i><br>
<div id="allowDownload" style="display:none">		            
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
                    <%} 
        	         if(href_RUNLINUX!=null && !href_RUNLINUX.isEmpty()&& !null_href.equals(href_RUNLINUX))
        	         {
        	        	 if(isStdLinuxInstallMissing){
                    %>
                    <td class="transparent-top" width="5%">
                     <td class="transparent-top" width="40%">
                    <td class="transparent-top" width="10%">&nbsp;</td>
                    <%} %>
                    
		            <td class="transparent-top" width="5%">
		                <img class="transparent" src="../resources/images/legacy/icon_linux.png"/>
		            </td>
		            <td class="transparent-top" width="40%">
		                <b><netui:content value="${bundle.web['installClient.linux.clientName2']}"/></b><br>
		                <i><netui:content value="${bundle.web['installClient.linux.OS']}"/></i><br>
                        <div id="allowDownload2" style="display:none">		            
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
		            <%} %>
		        </tr>     
 <% } %>

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


