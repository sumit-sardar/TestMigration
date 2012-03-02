<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="organizationApplicationResource" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<div>
<input type="hidden" id="profileExternalId" name="profileExternalId"/>
<input type="hidden" id="profileRoleOptions" name="profileRoleOptions"/>
<input type="hidden" id="loginUserId" name="loginUserId"/>
<input type="hidden" id="loginUserName" name="loginUserName"/>
<input type="hidden" id="profileRoleName" name="profileRoleName"/>
<input type="hidden" id="mpDialogID" name = "mpDialogID" value=<lb:label key="dialog.myProfile.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpOldPwd" name = "mpOldPwd" value=<lb:label key="dialog.myProfile.oldpassword" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpHintAns" name = "mpHintAns" value=<lb:label key="dialog.myProfile.hintAns" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpFirstNameID" name = "mpFirstNameID" value=<lb:label key="dialog.myProfile.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpLastNameID" name = "mpLastNameID" value=<lb:label key="dialog.myProfile.lastName" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpConfirmID" name = "mpConfirmID" value=<lb:label key="myProfile.alert.confirm" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpEmailAlertID" name = "mpEmailAlertID" value=<lb:label key="myProfile.alert.email" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpRequiredID" name = "mpRequiredID" value=<lb:label key="myProfile.missingRequesdField" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpRequiredIDs" name = "mpRequiredIDs" value=<lb:label key="myProfile.missingRequesdFields" prefix="'" suffix="'"/>/>
<input type="hidden" id="mptZoneID" name = "mptZoneID" value=<lb:label key="dialog.myProfile.timeZone" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpZipID" name = "mpZipID" value=<lb:label key="dialog.myProfile.zip" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpPPhoneID" name = "mpPPhoneID" value=<lb:label key="dialog.myProfile.primaryPhone" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpSPhoneID" name = "mpSPhoneID" value=<lb:label key="dialog.myProfile.secondaryPhone" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpFaxID" name = "mpFaxID" value=<lb:label key="dialog.myProfile.faxNumber" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpAddrID1" name = "mpAddrID1" value=<lb:label key="dialog.myProfile.addressLine1" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpAddrID2" name = "mpAddrID2" value=<lb:label key="dialog.myProfile.addressLine2" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpMidNameID" name = "mpMidNameID" value=<lb:label key="dialog.myProfile.midName" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpCityID" name = "mpCityID" value=<lb:label key="dialog.myProfile.city" prefix="'" suffix="'"/>/>
<input type="hidden" id="inNameCharID" name = "inNameCharID" value=<lb:label key="myProfile.invalidNameChars" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpSRequiredID" name = "mpSRequiredID" value=<lb:label key="myProfile.requiredField" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpInCharID" name = "mpInCharID" value=<lb:label key="myProfile.invalid.chars" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpInEmailID" name = "mpInEmailID" value=<lb:label key="myProfile.invalid.email" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpInFormatID" name = "mpInFormatID" value=<lb:label key="myProfile.invalid.format" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpInAddressID" name = "mpInAddressID" value=<lb:label key="myProfile.invalid.address" prefix="'" suffix="'"/>/>
<input type="hidden" id="mpInNemericFormatID" name = "mpInNemericFormatID" value=<lb:label key="myProfile.invalid.numericFormat" prefix="'" suffix="'"/>/>
</div>
<table>
	<tr class="transparent">
		<!-- column 1 -->
		<td style ="vertical-align: top;" width="310">
			<table class="transparent">
			<tbody>
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.firstName" suffix=":" prefix="* "/></td>
					<td class="transparent"><input type="text" style="width: 200px;" tabindex="0" maxlength="32" id="profileFirstName" name="profileFirstName"></td>
				</tr>
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.midName" suffix=":" /></td>
					<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="profileMiddleName"	name="profileMiddleName"></td>
				</tr>
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.lastName" suffix=":" prefix="* "/></td>
					<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="profileLastName" name="profileLastName"></td>
				</tr>			
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.loginId" suffix=":" prefix=""/></td>
					<td class="transparent"><div id="profileLoginId"></div></td>
				</tr>
				<tr class="transparent">
					<td width="110" class="transparent alignRight" style="vertical-align: top;"><lb:label key="dialog.myProfile.org" suffix=":" /></td>
					<td class="transparent-small">					
					<div id="profileNoOrgNode" style="padding-left: 4px"><font color="gray"><lb:label key="dialog.myProfile.msg.notSelectedOrgNodes" /></font></div>
					<div id="orgNodesName" style="padding-left: 4px"></div>
					</td>
				</tr>
			</tbody>
		</table>
		</td>
		<td style ="vertical-align: top;" width="350">
			<table class="transparent">
			<tbody>
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.emailId" suffix=":" /></td>
					<td class="transparent"><input type="text" style="width: 200px;" maxlength="64" id="profileEmail" name="profileEmail"></td>
				</tr>

				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.timeZone" suffix=":" prefix="* "/></td>
					<td class="transparent"><select id="profileTimeZoneOptions" name="profileTimeZoneOptions" style="width: 202px;"></select></td>
				</tr>
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.role" suffix=":" /></td>
					<td class="transparent"><div id="role"></div></td>

				</tr>
				<!--ext_pin1 is added for DEX CR-->

				<tr class="transparent">
					<td nowrap="" width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.extId" suffix=":" /></td>
					<td class="transparent"><div id="externalId"></div></td>
				</tr>
				<!--ext_pin1 is added for DEX CR-->
				<tr class="transparent">
					<td width="110" class="transparent alignRight"></td>
					<td class="transparent"></td>
				</tr>
				
			</tbody>
		</table>
		</td>
	</tr>
</table>