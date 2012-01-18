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

<netui:form action="manageUpload">

<table width="97%" style="margin:15px auto;" border="0"> 
	<tr>
		<td style="padding-left:5px;">
    		<h1><lb:label key="services.import.title" /></h1>
			<p style="color:#000"><lb:label key="services.import.msg" /></p> 		
		</td>
	</tr>
	<tr> 
		<td style="padding-left:6px;">
					<div id="accordion" style="width:100%;position:relative;">							
						<div style="position:relative;">
						  	<h3><a href="#"><lb:label key="services.import.templates" /></a></h3>
							<div id="templates" style="background-color: #FFFFFF;" >							
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
        	<a href="#" id="exportDataButton" onclick="submitPage();" class="rounded {transparent} buttonDisabled" style="text-decoration: none;" >
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
	<tr>
		<td style="padding-left:5px;">
			<br/><br/>
	  		<span class="headerTag"><lb:label key="services.import.uploadFile" /></span><br/>
			<lb:label key="services.import.uploadfile.msg" /><br/><br/>		
		</td>
	</tr>
	
	<tr> 
		<td style="padding-left:6px;">
			<div style="float:left; width:1000px; font-family: Arial, Helvetica, Sans Serif; font-size:11px; font-weight:normal; ">
        	<netui:fileUpload tagId="inputbox" dataSource="actionForm.theFile" size="64" style="height:24" onKeyPress="return constrainEnterKeyEvent();" onChange="return enableUpload();" onKeyUp="return enableUpload();"/>
            &nbsp;
        	<a href="#" id="upload" onclick="submitPage();" class="rounded {transparent} buttonDisabled" style="text-decoration: none;" >
          		<lb:label key="services.import.button" />
           	</a>
           	</div>
			<br/><br/>
       </td>
    </tr>
	
</table>
							</div>
						</div>
						
						
						<div style="position:relative;">
							<h3><a href="#"><lb:label key="services.import.viewUploads" /></a></h3>
							<div id="viewUploads" style="background-color: #FFFFFF;">
<table> 
	<tr>
		<td style="padding-left:5px;">
			<lb:label key="services.import.viewUploads.msg" /> 		
		</td>
	</tr>
	<tr>
		<td align="right" class="transparent">
			<div style="float:left; width:1000px; font-family: Arial, Helvetica, Sans Serif; font-size:11px; font-weight:normal;">
        	<a href="#" id="Button1" onclick="submitPage();" class="rounded {transparent} buttonDisabled" style="text-decoration: none;" >
          		<lb:label key="services.export.button" />
           	</a>
            </div>               	                                    
		</td>
	</tr>
	<tr> 
		<td style="padding-left:6px;">
			<div id="viewUploads" style="float:left; width:1210px; background-color: #FFFFFF;">
				<table id="list3" class="gridTable"></table>
				<script>populateUploadListGrid();</script>
			</div>								
		</td>
	</tr>
</table>
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

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


