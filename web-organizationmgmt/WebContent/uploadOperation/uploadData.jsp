<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="organizationApplicationResource" />

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['upload.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.uploadData']}"/>
<netui-template:setAttribute name="templateHelpLink" value="${bundle.help['help.topic.howToUseTemplate']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<netui:form action="manageUpload" enctype="multipart/form-data" method="post">

<input type="hidden" id="downloadFile" name="downloadFile" value="userFile" />
<input type="hidden" id="selectedId" name="selectedId" value="" />

<table width="97%" style="margin:15px auto;" border="0"> 
	<tr>
		<td style="padding-left:5px;">
    		<h1><lb:label key="services.import.title" /></h1>
			<p style="color:#000"><lb:label key="services.import.msg" /></p> 		
		</td>
	</tr>
	<tr> 
		<td style="padding-left:6px;">
					<div id="accordion" style="width:85%; position:relative;">							
						<div style="position:relative;">
						  	<h3><a href="#"><lb:label key="services.import.templates" /></a></h3>
							<div id="templates" style="background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;" >							
<table> 
	<tr>
		<td style="padding-left:5px;">
	  		<span class="headerTag"><lb:label key="services.export.downloadtemplate" /></span><br/>
			<lb:label key="services.import.template.msg" /><br/> 
		</td>
	</tr>
	<tr>
		<td align="right" class="transparent">
			<div style="float:left; width:1000px; font-family: Arial, Helvetica, Sans Serif; font-size:11px; font-weight:normal; ">
        	<a href="#" id="exportDataButton" onclick="return downloadTemplate(this);" class="rounded {transparent} buttonDisabled" style="text-decoration: none;" >
          		<lb:label key="services.export.button" />
           	</a>
            </div>               	                                    
		</td>
	</tr>
	<tr> 
		<td style="padding-left:6px;">
			<div id="uploadData" style="float:left; width:1210px; background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
				<table id="list2" class="gridTable"></table>
				<script>populateDownloadTemplateListGrid();</script>
			</div>								
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>	
	<tr>
		<td style="padding-left:5px;">
			<br/><br/>
	  		<span class="headerTag"><lb:label key="services.import.uploadFile" /></span><br/>
			<lb:label key="services.import.uploadfile.msg" /><br/><br/>		
		</td>
	</tr>	
	<tr>
		<td>
			<div id="errorMessage" class="roundedMessage" style="display: none" > 
					<table>
						<tr>
							<td valign="top">
		                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
							</td>
							<td>
								<div id="errorMsg"></div>
							</td>
						</tr>
					</table>
			</div>
			<div id="infoMessage" class="roundedMessage" style="display: none" > 
					<table>
						<tr>
							<td valign="top">
		                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif" border="0" width="16" height="16">&nbsp;&nbsp;
							</td>
							<td>
								<div id="infoMsg"></div>
							</td>
						</tr>
					</table>
			</div>
		</td>
	</tr>
	<tr> 
		<td style="padding-left:6px;">
			<div style="float:left; width:1000px; font-family: Arial, Helvetica, Sans Serif; font-size:11px; font-weight:normal;">
        	<netui:fileUpload tagId="inputbox" dataSource="pageFlow.theFile" size="64" style="height:22px;" onKeyPress="return constrainEnterKeyEvent();" onChange="return enableUpload();" onKeyUp="return enableUpload();"/>
            &nbsp;
        	<a href="#" id="upload" onclick="return uploadFile(this);" class="rounded {transparent} buttonDisabled" style="text-decoration: none;" >
          		<lb:label key="services.import.button" />
           	</a>
           	</div>
			<br/><br/>
       </td>
    </tr>

	<tr><td>&nbsp;</td></tr>
	
</table>
							</div>
						</div>
						
						
						<div style="position:relative;">
							<h3><a href="#"><lb:label key="services.import.viewUploads" /></a></h3>
							<div id="viewUploads" style="background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;" >
<table> 
	<tr>
		<td style="padding-left:5px;">
			<lb:label key="services.import.viewUploads.msg" /> 		
		</td>
	</tr>
	<tr>
		<td align="right" class="transparent">
			<div style="float:left; width:1000px; font-family: Arial, Helvetica, Sans Serif; font-size:11px; font-weight:normal;">
        	<a href="#" id="deleteFile" onclick="return deleteFile(this);" class="rounded {transparent} buttonDisabled" style="text-decoration: none;" >
          		<lb:label key="services.export.button.delete" />
           	</a>
           	&nbsp;
        	<a href="#" id="downloadErrorFile" onclick="return downloadErrorFile(this);" class="rounded {transparent} buttonDisabled" style="text-decoration: none;" >
          		<lb:label key="services.export.button.downloadErrorFile" />
           	</a>
           	&nbsp;
        	<a href="#" id="refresh" onclick="return refresh();" class="rounded {transparent} button" style="text-decoration: none;" >
          		<lb:label key="services.export.button.refresh" />
           	</a>
            </div>               	                                    
		</td>
	</tr>
	<tr> 
		<td style="padding-left:6px;">
			<div id="viewUploads" style="float:left; width:1210px; background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
				<table id="list3" class="gridTable"></table>
				<div id="pager3" class="gridTable" ></div>
				<script>populateUploadListGrid();</script>
			</div>								
		</td>
	</tr>
</table>



<div id="colorErrors" style="display: none">
<br/>
<p>
<netui:content value="Errors in the downloaded error file are highlighted in different colors."/><br/>
<netui:content value="Use this key to help decide how to correct the data."/><br/>
</p>

<table class="simpleSmall" width="40%">
    <tr class="simple">
        <td class="simpleHeader">Error Description</td>
        <td class="simpleHeader">Error Color</td> 
    </tr>
    <tr class="simple">
        <td class="simple">Less characters than allowed</td>
        <td class="simpleColored" bgcolor="#FF69B4">&nbsp;&nbsp;</td>
    </tr>
    <tr class="simple">
        <td class="simple">More characters than allowed</td>
        <td class="simpleColored" bgcolor="#FBFF73">&nbsp;&nbsp;</td>
    </tr>
    <tr class="simple">
        <td class="simple">Invalid characters</td>
        <td class="simpleColored" bgcolor="#99FF99">&nbsp;&nbsp;</td>
    </tr>
    <tr class="simple">
        <td class="simple">Missing required values</td>
        <td class="simpleColored" bgcolor="#00CCFF">&nbsp;&nbsp;</td>
    </tr>
    <tr class="simple">
        <td class="simple">Logical errors</td>
        <td class="simpleColored" bgcolor="#FF9900">&nbsp;&nbsp;</td>
    </tr>
</table>
</div>


							</div>									
						</div>							
					</div>
					
		</td>
	</tr>
 			    
</table>

</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", "uploadDataLink");
});
</script>

<%
	String showViewUpload = (String)request.getAttribute("showViewUpload");
%>
<%	if (showViewUpload != null) { %>
		<script type="text/javascript">
		$(document).ready(function(){
			$('#accordion').accordion('activate', 1);
		});
		</script>		
<%	} %>

<%
	String uploadMsg = (String)request.getAttribute("uploadMsg");
	System.out.println(uploadMsg);
%>
<script type="text/javascript">
	handleUploadMessages("<%= uploadMsg %>");
</script>		

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>
