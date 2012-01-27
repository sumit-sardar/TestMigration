<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="organizationApplicationResource" />

<input type="hidden" id="orgNameID" name = "orgNameID" value=<lb:label key="org.name" prefix="'" suffix="'"/>/>
<input type="hidden" id="orgCodeID" name = "orgCodeID" value=<lb:label key="org.Code" prefix="'" suffix="'"/>/>
<input type="hidden" id="orgLayerID" name = "orgLayerID" value=<lb:label key="org.layer" prefix="'" suffix="'"/>/>
<input type="hidden" id="orgParentID" name = "orgParentID" value=<lb:label key="org.parentOrg" prefix="'" suffix="'"/>/>
<input type="hidden" id="editOrgID" name = "editOrgID" value=<lb:label key="org.msg.editOrganization" prefix="'" suffix="'"/>/>
<input type="hidden" id="addOrgID" name = "addOrgID" value=<lb:label key="org.msg.addOrganization" prefix="'" suffix="'"/>/>
<input type="hidden" id="confirmID" name = "confirmID" value=<lb:label key="org.msg.alertConfirm" prefix="'" suffix="'"/>/>
<input type="hidden" id="requiredID" name = "requiredID" value=<lb:label key="org.msg.requiredText" prefix="'" suffix="'"/>/>
<input type="hidden" id="searchOrgID" name = "searchOrgID" value=<lb:label key="org.label.searchOrgID" prefix="'" suffix="'"/>/>
<input type="hidden" id="mRequiredID" name = "mRequiredID" value=<lb:label key="org.msg.multiple.requiredText" prefix="'" suffix="'"/>/>
<input type="hidden" id="mdrNumberID" name = "mdrNumberID" value=<lb:label key="org.msg.invalidMDRNumber" prefix="'" suffix="'"/>/>
<input type="hidden" id="dupFormatID" name = "dupFormatID" value=<lb:label key="org.msg.invalidDupFormatTitle" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidNameCharsID" name = "invalidNameCharsID" value=<lb:label key="org.msg.invalidNameCharsOrg" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidFormatID" name = "invalidFormatID" value=<lb:label key="org.msg.invalidFormatTitle" prefix="'" suffix="'"/>/>
<input type="hidden" id="mdrID" name = "mdrID" value=<lb:label key="org.mdrNumber" prefix="'" suffix="'"/>/>
<input type="hidden" id="layerID" name = "layerID" value=<lb:label key="org.layer" prefix="'" suffix="'"/>/>
<input type="hidden" id="pOrgID" name = "pOrgID" value=<lb:label key="org.parentOrg" prefix="'" suffix="'"/>/>
<input type="hidden" id="missRequiredID" name = "missRequiredID" value=<lb:label key="org.msg.missingField" prefix="'" suffix="'"/>/>
<input type="hidden" id="mMissRequiredID" name = "mMissRequiredID" value=<lb:label key="org.msg.missingField" prefix="'" suffix="s'"/>/>
<input type="hidden" id="orgListID" name = "orgListID" value=<lb:label key="org.label.orgList" prefix="'" suffix="'"/>/>
<input type="hidden" id="deleteOrgTitle" name = "deleteOrgTitle" value=<lb:label key="org.delete" prefix="'" suffix="'"/>/>
<input type="hidden" id="deleteOrgTitleMsg" name = "deleteOrgTitleMsg" value=<lb:label key="org.msg.delete" prefix="'" suffix="'"/>/>

<table class="transparent" width="97%" style="margin:15px auto;">  
	<tr class="transparent">
        <td>
		<table class="transparent">
			<tr class="transparent">
				<td>
				<h1><lb:label key="org.title" /></h1>
				</td>
			</tr>
			<tr>
				<td class="subtitle">
				<lb:label key="org.msg.pos.button" />
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
				      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="org.label.search" /></div>
				    	  <script>populateTree();</script>
				    	<div id="outertreebgdiv" class="treeCtrl">
					    	<div id="orgNodeHierarchy" style="width:auto;height:auto;display:table">
							</div>
						</div>
				    </td>
					    
					<td class="transparent" width="5px">&nbsp;</td>
					 	 
					<td style="vertical-align:top;" id="jqGrid-content-section">      
					    <table id="list2" class="gridTable"></table>
						<div id="pager2" class="gridTable" ></div>
					</td>
						
				</tr>
			</table>
        </td>
    </tr>
</table>

<jsp:include page="/orgOperation/add_edit_org_detail.jsp" />
			
<div id="confirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<p><lb:label key="org.msg.notSave" /></p>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeConfirmationPopup(); return false;" class="ui-widget-header">
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closePopUp('confirmationPopup'); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>

<div id="confirmationPopupNavigation"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<br/>
			<p><lb:label key="org.msg.notSave" /></p>
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeConfirmationPopup(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closePopUp('confirmationPopupNavigation'); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>

<div id="deleteConfirmation"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<br/>
			<p><lb:label key="org.msg.delete" /></p>
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Ok&nbsp;" onclick="javascript:deleteOrganizationDetail(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:closePopUp('deleteConfirmation'); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>

<div id="searchUserByKeyword"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="org.search.info.message"/></p>
	</div>
	<div class="searchInputBoxContainer" id="searchInputBoxContainer">
		<center>
			<input type="text" name="searchUserByKeywordInput" id="searchUserByKeywordInput" onkeypress="trapEnterKey(event);"/>
		</center>	
	</div>
	<div style="padding-bottom:20px;">
		<center>
			<input type="button"  value="Reset" onclick="javascript:resetSearch(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value="Search" onclick="javascript:searchUserByKeyword(); return false;" class="ui-widget-header">
		</center>
	</div>
</div>
<div id="nodataSelectedPopUp"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;">
		<lb:label key="orgList.no.data.selected"/>
	</div>
</div>

				