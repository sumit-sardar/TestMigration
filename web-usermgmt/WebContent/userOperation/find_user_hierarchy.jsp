<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>   
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="userApplicationResource" />

<input type="hidden" id="roleNameID" name = "roleName" value='<%=session.getAttribute("userRole") %>'/>
<input type="hidden" id="userEditTitleID" name = "userEditTitleID" value=<lb:label key="user.label.titleEdit" prefix="'" suffix="'"/>/>
<input type="hidden" id="userViewTitleID" name = "userViewTitleID" value=<lb:label key="user.label.titleView" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgFirstNameID" name = "jqgFirstNameID" value=<lb:label key="user.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgLastNameID" name = "jqgLastNameID" value=<lb:label key="user.lastName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgLoginID" name = "jqgLoginID" value=<lb:label key="user.loginID" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgRoleID" name = "jqgRoleID" value=<lb:label key="user.role" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgEmailID" name = "jqgEmailID" value=<lb:label key="user.email" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgOrgID" name = "jqgOrgID" value=<lb:label key="user.organization" prefix="'" suffix="'"/>/>
<input type="hidden" id="editRecordID" name = "editRecordID" value=<lb:label key="user.label.titleEditRecord" prefix="'" suffix="'"/>/>
<input type="hidden" id="chgPwdID" name = "chgPwdID" value=<lb:label key="user.changepassword" prefix="'" suffix=": '"/>/>
<input type="hidden" id="extID" name = "extID" value=<lb:label key="user.ext" prefix="' " suffix=": '"/>/>
<input type="hidden" id="confirmID" name = "confirmID" value=<lb:label key="user.msg.alert.confirm" prefix="'" suffix="'"/>/>
<input type="hidden" id="emailAlertID" name = "emailAlertID" value=<lb:label key="user.msg.alert.email" prefix="'" suffix="'"/>/>
<input type="hidden" id="addUserID" name = "addUserID" value=<lb:label key="user.label.titleADD" prefix="'" suffix="'"/>/>
<input type="hidden" id="mRequiredID" name = "mRequiredID" value=<lb:label key="user.msg.missingRequesdField" prefix="'" suffix="'"/>/>
<input type="hidden" id="tZoneID" name = "tZoneID" value=<lb:label key="user.label.timeZone" prefix="'" suffix="'"/>/>
<input type="hidden" id="oAssignID" name = "oAssignID" value=<lb:label key="user.label.orgAssignment" prefix="'" suffix="'"/>/>
<input type="hidden" id="zipID" name = "zipID" value=<lb:label key="user.zip" prefix="'" suffix="'"/>/>
<input type="hidden" id="pPhoneID" name = "pPhoneID" value=<lb:label key="user.primaryphone" prefix="'" suffix="'"/>/>
<input type="hidden" id="sPhoneID" name = "sPhoneID" value=<lb:label key="user.secondaryphone" prefix="'" suffix="'"/>/>
<input type="hidden" id="faxID" name = "faxID" value=<lb:label key="user.faxnumber" prefix="'" suffix="'"/>/>
<input type="hidden" id="addrID1" name = "addrID1" value=<lb:label key="user.addressLine1" prefix="'" suffix="'"/>/>
<input type="hidden" id="addrID2" name = "addrID2" value=<lb:label key="user.addressLine2" prefix="'" suffix="'"/>/>
<input type="hidden" id="mNameID" name = "mNameID" value=<lb:label key="user.middleName" prefix="'" suffix="'"/>/>
<input type="hidden" id="cityID" name = "cityID" value=<lb:label key="user.middleName" prefix="'" suffix="'"/>/>
<input type="hidden" id="inNameCharID" name = "inNameCharID" value=<lb:label key="user.msg.invalidNameChars" prefix="'" suffix="'"/>/>
<input type="hidden" id="sRequiredID" name = "sRequiredID" value=<lb:label key="user.msg.requiredField" prefix="'" suffix="'"/>/>
<input type="hidden" id="mRequiredID" name = "mRequiredID" value=<lb:label key="user.msg.multiple.requiredField" prefix="'" suffix="'"/>/>
<input type="hidden" id="inCharID" name = "inCharID" value=<lb:label key="user.msg.invalid.chars" prefix="'" suffix="'"/>/>
<input type="hidden" id="inEmailID" name = "inEmailID" value=<lb:label key="user.msg.invalid.email" prefix="'" suffix="'"/>/>
<input type="hidden" id="inFormatID" name = "inFormatID" value=<lb:label key="user.msg.invalid.format" prefix="'" suffix="'"/>/>
<input type="hidden" id="inAddressID" name = "inAddressID" value=<lb:label key="user.msg.invalid.address" prefix="'" suffix="'"/>/>
<input type="hidden" id="inNemericFormatID" name = "inNemericFormatID" value=<lb:label key="user.msg.invalid.numericFormat" prefix="'" suffix="'"/>/>
<table> 
	<tr>
		<td style="padding-left:5px;">
    		<h1><lb:label key="user.title" /></h1>
		</td>
	</tr>
	<tr> 
		<td style="font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;padding-left:6px;">  
			<lb:label key="user.msg.pos.button" />
		</td>	
	</tr>
</table>
<table class="transparent">
    <tr class="transparent">
        <td style="border-color : #2E6E9E;padding-left:5px;">        
		<table> 
		<tr width="100%" >
		       <td align="right" colspan="12" style="padding: 0 0 0px 8px; clear:both;min-height:25px; height: 25px; ">
		       <div id="displayMessageMain" style="display:none; width:50%; float:left; text-align:left; background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold; border: 0px solid #A6C9E2;">
					<div id= "contentMain" style="padding-top: 5px;">	</div>
				</div>
		       	<div id="changePW" style="display:none;">
		       		<div id="cpw">
                		<a href="#" id="changePWButton" onclick="changePwdForUser(this);" class="rounded {transparent} button"><lb:label key="user.changepassword" /></a>
                	</div>      
                </div>          		     	        
	     	   </td>
		   	</tr>
		  
	      <tr class="transparent">
	        <td style="vertical-align:top;">
		      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="user.label.usersearch" /></div>
		    	  <script>populateTree();</script>
		    	  <div id = "orgNodeHierarchy"  class="treeCtrl">
				</div> 
		    </td>
		    
		 	<td class="transparent" width="5px">&nbsp;</td>
		 	 
		 	<td >	      
		    	<table id="list2" class="gridTable"></table>
				<div id="pager2"  class="gridTable"></div>
			</td>
			
	      </tr>
	    </table>
        </td>
    </tr>
</table>

<jsp:include page="/userOperation/add_edit_user_detail.jsp" />
<jsp:include page="/userOperation/change_password.jsp" />
			
<div id="confirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<br/>
			<p><lb:label key="user.msg.notsave" /></p>
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeConfirmationPopup(); return false;" class="ui-widget-header">&nbsp;
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
			<p><lb:label key="user.msg.notsave" /></p>
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


<div id="EmailWarning"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<p><lb:label key="user.msg.emailprovide" /></p>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closePopUp('EmailWarning'); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closeEmailWarningPopup(); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>
				