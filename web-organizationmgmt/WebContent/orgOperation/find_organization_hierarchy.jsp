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

<table> 
	<tr>
		<td style="padding-left:5px;">
    		<h1><lb:label key="org.title" /></h1>
		</td>
	</tr>
	<tr> 
		<td style="font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;padding-left:6px;">  
			<lb:label key="org.msg.pos.button" />
		</td>	
	</tr>
</table>
<br/>
<table class="transparent">

    <tr class="transparent">
        <td style="border-color : #2E6E9E;">
        <table width="100%">
        <tr width="100%" >
		   <td align="left" style="padding: 0 0 0 8px;">
		      	<div id="displayMessageMain" style="display:none; width:99.5%; background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold;">
					<div id= "contentMain" style="padding-bottom: 5px"> </div>
				</div>
			</td>
		</tr>
		</table>
		<table>  
			<!-- <tr class="transparent">
	        	<td  colspan="12" style="height:5px; color: #336699; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold;">&nbsp;</td>
   	 		</tr> -->
		  
	      <tr class="transparent">
	        <td class="transparent" valign="middle">
		      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="org.label.search" /></div>
		    	  <script>populateTree();</script>
		    	  <div id = "orgNodeHierarchy"  class="treeCtrl">
				</div> 
		    </td>
		    
		 	<td class="transparent" width="8px">&nbsp;</td>
		 	 
		 	<td >	      
		    	<table id="list2" class="gridTable"></table>
				<div id="pager2" class="gridTable" ></div>
			</td>
			
	      </tr>
	      <!-- 
	      <tr class="transparent">
	        <td  colspan="12" style="height:5px; color: #336699; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold;">&nbsp;</td>
   	 	  </tr>
   	 	   -->
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
				