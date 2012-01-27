<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="userApplicationResource" />

<div id="changeUserPassword"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	
	<div id="displayMessageChangePassword" class="roundedMessage" style="display:none; width:99.5%;background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; border: 1px solid #A6C9E2;">
			<table>
				<tr>
					<td rowspan="3" valign="top">
                   	<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
					</td>
					<td>
						<table>
							<tr><td><font style="color: red; font-size:12px; font-weight:bold"><div id="titlePWD"></div></font></td></tr>
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
				<tr class="transparent">
					<td class="transparent" colspan="2"><p><lb:label key="user.msg.newpassword" /></p></td>
				</tr>
	 			<tr class="transparent">
					<td class="transparent" align="right" style="padding: 10px"><span class="asterisk">*</span>&nbsp;<lb:label key="user.newpassword" suffix=":"/></td>
					<td class="transparent"><input type="password" style="width: 200px; font-family: sans-serif;" maxlength="64" id="newPassword" name="newPassword"></td>
				</tr>
				<tr class="transparent">
					<td class="transparent" align="right" style="padding: 10px"><span class="asterisk">*</span>&nbsp;<lb:label key="user.confirmpassword" suffix=":"/></td>
					<td class="transparent"><input type="password" style="width: 200px; font-family: sans-serif;" maxlength="64" id="confirmPassword" name="confirmPassword"></td>
				</tr>
			</table>
		</div>
		<br>
		<div>
			<table cellspacing="0" cellpadding="0" border="0" id="TblGrid_list2_2" class="EditTable" width="100%">
					<tr id="Act_Buttons" align="center">
						<td  width="100%">
							<center>
							<input type="button" id="sData" value="&nbsp;Save&nbsp;" onclick="javascript:saveChangePassword(); return false;" class="ui-widget-header" style="width:60px">
							<input type="button" id="cData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:onChangePasswordCancel(); return false;" class="ui-widget-header" style="width:60px">
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
