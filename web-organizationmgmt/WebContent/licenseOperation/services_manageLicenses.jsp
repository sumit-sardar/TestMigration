<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template_manage_license.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['manageLicense.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.testLicense']}"/>
<netui-template:section name="bodySection">
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="organizationApplicationResource" />

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/licenses.js"></script>

<input type="hidden" id="orgNodeName" name = "orgNodeName" value=<lb:label key="license.orgNodeName" prefix="'" suffix="'"/>/>
<input type="hidden" id="scheduled" name = "scheduled" value=<lb:label key="license.scheduled" prefix="'" suffix="'"/>/>
<input type="hidden" id="consumed" name = "consumed" value=<lb:label key="license.consumed" prefix="'" suffix="'"/>/>
<input type="hidden" id="available" name = "available" value=<lb:label key="license.available" prefix="'" suffix="'"/>/>

<input type="hidden" id="currentEditing" name="currentEditing" value="false" />

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<%
	String subtestModel = (String)request.getAttribute("subtestModel");
	if (subtestModel.equals("T")) 
		subtestModel = "Subtest";
	else
		subtestModel = "Session";
%>

<netui:form action="services_manageLicenses">

<input type="hidden" id="menuId" name="menuId" value="manageLicensesLink" />
<input type="hidden" id="treeOrgNodeId" />


<table class="transparent" width="97%" style="margin:15px auto;">  
	<tr class="transparent">
        <td>
		<table class="transparent">
			<tr class="transparent">
				<td>
				<h1><lb:label key="services.license.title" /></h1>
				</td>
			</tr>
			<tr>
				<td class="subtitle">
				<lb:label key="services.license.message" />
			</tr>
		</table>
		</td>
    </tr>
	
	<tr class="transparent">
        <td align="center">        
			<table width="100%"> 
		      	<tr>
					<td colspan="3" class="buttonsRow">
			        <div id="displayMessageMain" class="errMsgs" style="display:none; width:99.5%;float:left;">
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
				<tr class="transparent">
			        <td style="vertical-align:top; width:16%;" align="left">
				      	<div id="searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="org.label.search" /></div>
				    	<div id="outertreebgdiv" class="treeCtrl">
					    	<div id="orgNodeHierarchy" style="width:auto;height:auto;display:table">
							</div>
						</div>
				    </td>
					    
					<td class="transparent" width="5px">&nbsp;</td>
					 	 
					<td style="vertical-align:top;" id="jqGrid-content-section">
						<div id="outerInfo">
							<p id="licenseModelDiv" class="subtitle" style="display: none">
								License model: <b><%= subtestModel %></b>
							</p> 
						   	<table id="orgNodeLicenseGrid" class="gridTable"></table>
						</div>
						<br/><br/>
						<div id="orgNodeGridSection" style="display: none">
							<table class="subtitle" border="0" width="100%"><tr>
							<td width="*" align="left">
							Click on the cell under <b>Available</b> column to edit the license quatity. Hit <b>Enter</b> to commit the value. Click <b>Save</b> button to save your changes.
							</td>
							<td width="20%" align="right">
							<a href="#" id="saveLicenses" onclick="return saveLicenses();" class="rounded {transparent} button" style="text-decoration: none;" >
          						<lb:label key="license.save.button" />
           					</a>
           					&nbsp;
							<a href="#" id="cancelEditing" onclick="return verifyEditLicenseAndGotoMenuAction('services.do', 'manageLicensesLink');" class="rounded {transparent} button" style="text-decoration: none;" >
          						<lb:label key="license.cancel.button" />
           					</a>
							</td>
							</tr></table>
							<br/>
							<div id="outerGrid"> 
					    		<table id="orgNodeGrid" class="gridTable"></table>
								<div id="orgNodePager" class="gridTable" ></div>
							</div>
						</div>
						
						<div id="noDataGridSection" style="display: none">
<table class="transparent"><tbody>
<tr width="100%">
	<th rowspan="2" style="padding-right: 12px; text-align: right;"><img height="23" src="/OrganizationWeb/resources/images/messaging/icon_info.gif"></th>
	<th class="subtitle" align="left">No groups found.</th>
</tr>
<tr class="subtitle" width="100%">
	<td colspan="2">To edit the license quatity for selected group, please select the parent group.</td>
</tr>
</tbody></table>			 				
						</div>
						
					</td>
						
				</tr>
			</table>
        </td>
    </tr>
</table>


</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", "manageLicensesLink");
	loadOrgNodeTree();
}); 
</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


