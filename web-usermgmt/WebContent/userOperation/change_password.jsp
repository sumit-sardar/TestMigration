<%@ page import="java.io.*, java.util.*"%>

<div id="changeUserPassword"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	
	<div id="displayMessageChangePassword" style="display:none; width:99.5%;background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold; border: 1px solid #A6C9E2;">
			<table>
				<tr>
					<td rowspan="3"><div id= "messageType">	</div></td>
					<td>
						<table>
							<tr><td><div id="titlePWD"></div></td></tr>
							<tr><td><div id= "contentPWD"></div></td></tr>
							<tr><td><div id= "messagePWD"></div></td></tr>
						</table>
					</td>
				</tr>
			</table>
	</div>
	<div id="accordion" style="width:99.5%;">
		<div id="new_user_password" style="overflow-y: hidden !important; overflow-x: hidden !important;">
			<table class="transparent">
				<tr class="transparent" width="50%">
					<td class="transparent" colspan="2"><p>The password must contain at least eight characters. At least one character must be a number and at least one character must be a letter.
						New password cannot be any of five previous passwords. Required fields are marked by a blue asterisk *.</p></td>
				</tr>
	 			<tr class="transparent" width="50%">
					<td class="transparent" align="right" width="50%"><span class="asterisk">*</span>&nbsp;New Password:</td>
					<td class="transparent"><input type="password" style="width: 200px;" maxlength="64" id="newPassword" name="newPassword"></td>
				</tr>
				<tr class="transparent" width="50%">
					<td class="transparent" align="right" width="50%"><span class="asterisk">*</span>&nbsp;Confirm Password:</td>
					<td class="transparent"><input type="password" style="width: 200px;" maxlength="64" id="confirmPassword" name="confirmPassword"></td>
				</tr>
			</table>
		</div>
		<br>
		<div>
			<table cellspacing="0" cellpadding="0" border="0" id="TblGrid_list2_2" class="EditTable" width="100%">
					<tr id="Act_Buttons" align="center">
						<td  width="0%" id="preButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-left" id="pData" href="javascript:void(0)"><span
							class="ui-icon ui-icon-triangle-1-w"></span></a></td><td id="nextButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-right" id="nData"
							href="javascript:void(0)"><span class="ui-icon ui-icon-triangle-1-e"></span></a></td>
							<td>&nbsp;</td>
						<td  width="100%">
							<center>
							<input type="button"  id="sData" value="&nbsp;Save&nbsp;" onclick="javascript:saveChangePassword(); return false;" class="ui-widget-header">
							<input type="button"  id="cData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:onChangePasswordCancel(); return false;" class="ui-widget-header">
							</center>
							<br>
						</td>
					</tr>
					<tr class="binfo" style="display: none;">
						<td colspan="2" class="bottominfo"></td>
					</tr>
			</table>
		</div>
	</div>
</div>
