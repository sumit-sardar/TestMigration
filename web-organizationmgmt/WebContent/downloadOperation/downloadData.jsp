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
<netui-template:setAttribute name="title" value="${bundle.web['download.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.downloadData']}"/>
<netui-template:section name="bodySection">
 
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->


<netui:form action="manageDownload">
<input type="hidden" id="menuId" name="menuId" value="downloadDataLink" />

<input type="hidden" id="downloadFile" name="downloadFile" value="userFile" />
 
<table width="97%" style="margin:15px auto;" border="0"> 
	<tr>
		<td style="padding-left:5px;">
    		<h1><lb:label key="services.export.title" /></h1>
			<p style="color:#000"><lb:label key="services.export.msg" /></p> 
			<br/>		
		</td>
	</tr>
	<tr>
		<td class="buttonsRow">
			<div id="displayMessageMain" class="errMsgs" style="display: none; width: 50%; float: left;">
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
							<div id="contentMain"></div>
						</td>
					</tr>
				</table>				
			</div>
		</td>
	</tr>
	<tr>
		<td align="right">
			<div style="float:left; width:1070px;">
        	<a href="#" id="exportDataButton" onclick="return downloadData(this);" class="rounded {transparent} buttonDisabled" style="text-decoration: none;" >
          		<lb:label key="services.export.button" />
           	</a>
            </div>               	                                    
		</td>
	</tr>
	<tr> 
		<td style="padding-left:6px;">
			<div id="downloadData" style="float:left; width:1275px; background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
				<table id="downloadDataListId" class="gridTable"></table>
				<script>populateDownloadListGrid();</script>
			</div>								
		</td>
	</tr>
</table>

<br/><br/>

</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", "downloadDataLink");
});
</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


