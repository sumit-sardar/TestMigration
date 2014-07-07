<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<lb:bundle baseName="testsessionApplicationResource" />

<%
	Integer broadcastMessages = (Integer)session.getAttribute("broadcastMessages");
	if (broadcastMessages == null) broadcastMessages = new Integer(0);
	
	boolean is3to8Selected=(session.getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(session.getAttribute("is3to8Selected").toString()))? true: false;
	boolean isEOISelected=(session.getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(session.getAttribute("isEOISelected").toString()))? true: false;
	boolean isUserLinkSelected=(session.getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(session.getAttribute("isUserLinkSelected").toString()))? true: false;
	
%>

<table class="headerLayout">
	<tr>
		<% if(is3to8Selected || isEOISelected || isUserLinkSelected) {%>
		<td align="left" width="65%" style="padding: 3px 0px 0px 3px;"><img src="<%=request.getContextPath()%>/resources/images/ctb_oas_logo.png"></td>
		<% } else { %>
		<td align="left" width="70%" style="padding: 3px 0px 0px 3px;"><img src="<%=request.getContextPath()%>/resources/images/ctb_oas_logo.png"></td>
		<%} %>
		<% if(is3to8Selected || isEOISelected || isUserLinkSelected) {%>
		<td align="left" width="35%">
		<% } else { %>
		<td align="left" width="30%">
		<%} %>
			<table border="0" cellpadding="0" cellspacing="0">
			<tr height="22">
				<td align="center">
					<b>You are logged in as <%=session.getAttribute("userName")%></b>
				</td>
			</tr>
			<tr>
				<td>
					<div class="roundedHeader">
					&nbsp;&nbsp;
					<% 
						if(is3to8Selected || isEOISelected || isUserLinkSelected) {
					%>
						<a href="#" onclick="viewSwitchPrograms();"><b>Switch Programs</b></a>&nbsp;
						<img src="<%=request.getContextPath()%>/resources/images/dotdot.jpg"/>&nbsp;&nbsp;
					<% } %>
					<a href="#" onclick="viewBroadcastMessage();"><b>Messages</b></a>
					<% if (broadcastMessages.intValue() > 0) { %>
						<span class="messageheader"><%=broadcastMessages.toString()%></span>
					<% } %>
					&nbsp;								
					<img src="<%=request.getContextPath()%>/resources/images/dotdot.jpg"/>&nbsp;&nbsp;
					<% 
						Boolean hasBlockUserManagement=(Boolean)session.getAttribute("hasBlockUserManagement");
						Boolean hasBlockUserModifications=(Boolean)session.getAttribute("hasBlockUserModifications");
						if((hasBlockUserManagement==null) || (hasBlockUserManagement!=null && hasBlockUserManagement==false) || ((session.getAttribute("is3to8Selected") != null) && !is3to8Selected)) {
						if ((hasBlockUserModifications==null) || (hasBlockUserModifications!=null && hasBlockUserModifications==false)){
					%>
						<a href="#" onClick="viewMyProfile();"><b>My Profile</b></a>&nbsp;&nbsp;
						<img src="<%=request.getContextPath()%>/resources/images/dotdot.jpg"/>&nbsp;&nbsp;
					<% }} %>
                	<a href="<netui-template:attribute name="helpLink"/>" onClick="return showHelpWindow(this.href);"><b>Help</b></a>&nbsp;&nbsp;
					<img src="<%=request.getContextPath()%>/resources/images/dotdot.jpg"/>&nbsp;&nbsp;
					<a href="#" onclick="gotoAction('logout.do');"><b>Logout</b></a>&nbsp;&nbsp;
				</div>
				</td>
			</tr>
			</table>
		</td>
	</tr>
</table>
 
 <!-- Switch Programs Dialog for EOI user login -->
<div id="switchProgramsDialogId"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>
	<div class="feature" style="padding: 10px; background-color: #ffffff;" id="switchProgramsBody">
		<table class="simpleBlock" width="100%" cellpadding="5">
            <tr>
                <td colspan="2">
                	<br/><h1>Switch Administration</h1>
                	  <% 
                	 	String custName="";
						if(is3to8Selected) {
							custName="EOI";
						} else {
							custName="grade 6-8";
						}
					%>
                	<p style="color: #476CB5;">Click one of the links below to manage the <%=custName%> online test administration.</p>
                </td>                
            </tr>
            <tr>
            <td>
            &nbsp;&nbsp;
            </td>
            </tr>
         </table>
		<table style="margin-left:40px;">       
		   <% 
				if(is3to8Selected) {
			%>
            <tr>
                <td><h4>
	                <li style="list-style-type: square;">
	                	<a onclick="showLoading();" href="<%=request.getContextPath()%>/sessionOperation/switchToLinkSelected.do?selectedLink=EOI_Link">Manage the Oklahoma EOI online test administration</a>
	                </li>
                </h4></td>
            </tr>
            <% } else { %>
            <tr>
	            <td><h4>
	                <li style="list-style-type: square;">
	                	<a onclick="showLoading();" href="<%=request.getContextPath()%>/sessionOperation/switchToLinkSelected.do?selectedLink=3-8_Link">Manage the Oklahoma 6-8 online test administration</a>
	                </li>
	            </h4></td>
            </tr>
            <% } %>
            <ctb:auth roles="Administrator">
            <tr>
                <td><h4>
                <li style="list-style-type: square;">	
                	<a onclick="showLoading();" href="<%=request.getContextPath()%>/sessionOperation/switchToLinkSelected.do?selectedLink=UserLink">Manage user accounts (shared between EOI and 6-8)</a>
                </li>
            	</h4></td>
            </tr>
            </ctb:auth>
         </table>
	</div>
	<br>
	<div>
		<table cellspacing="0" cellpadding="0" border="0" width="100%">
				<tr align="center"><td>
					<input type="button" value="&nbsp;Close&nbsp;" onclick="javascript:closeSwitchPrograms(); return false;" class="ui-widget-header" style="width:60px">
				</td></tr>
				<tr><td>&nbsp;</td></tr>
		</table>
	</div>	
</div>
 
<!-- Broadcast Message Dialog -->
<div id="broadcastMsgDialogId"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>
	<div class="feature" style="padding: 10px; background-color: #ffffff;" id="broadcastMsgBody">
	</div>
	<br>
	<div>
		<table cellspacing="0" cellpadding="0" border="0" width="100%">
				<tr align="center"><td>
					<input type="button" value="&nbsp;Close&nbsp;" onclick="javascript:closeBroadcastMessage(); return false;" class="ui-widget-header" style="width:60px">
				</td></tr>
				<tr><td>&nbsp;</td></tr>
		</table>
	</div>	
</div>
 
<!-- My profile Dialog -->
<div id="myProfileDialog"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div id="displayMessageMyProfile" class="roundedMessage" style="width:802px;margin-bottom:2px;">
			<table>
				<tr>
					<td rowspan="3" valign="top">
                   	<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
					</td>
					<td>
						<table>
							<tr><td><font style="color: red; font-size:12px; font-weight:bold"><div id="titleProf"></div></font></td></tr>
							<tr><td><div id= "contentProf"></div></td></tr>
							<tr><td><div id= "messageProf"></div></td></tr>
						</table>
					</td>
				</tr>
			</table>
	</div>
	<div id="profileAccordion" style="width:99.5%;">
			<div>
				<h3><a href="#" ><lb:label key="dialog.myProfile.userInfo" /></a></h3>
				<div id="User_Info" style="background-color: #FFFFFF;">					
					<div id="infoDiv" style="display:inline;">
						<jsp:include page="oas_user_info.jsp" />
					</div>
				</div>
			</div>
			<div>
				<h3><a href="#" ><lb:label key="dialog.myProfile.contactInfo" /></a></h3>
				<div id="Contact_Info" style="overflow-y: scroll !important; overflow-x: hidden !important;">
					<div>
						<jsp:include page="oas_contact_info.jsp" />
					</div>
				</div>
			</div>
			<div>
				<h3><a href="#" ><lb:label key="dialog.myProfile.changePassword" /></a></h3>
				<div id="Change_Pwd" style="background-color: #FFFFFF; overflow-x: hidden !important; overflow-y: hidden !important;">
					<div>
						<table class="transparent">
							<tr class="transparent">
								<td class="subtitle" colspan="3" style="padding-bottom:5px;">
								<lb:label key="dialog.myProfile.msg.changepassword" />
								</td>
							</tr>
							<tr>
								<td style="vertical-align: top; width:57%">
								<table>
									<tr class="transparent">
										<td class="transparent alignRight" style="padding: 10px"><lb:label key="dialog.myProfile.oldpassword"
											suffix=":" /></td>
										<td><input type="password" style="width: 200px; font-family: sans-serif;" maxlength="32"
											id="profileOldPassword" name="profileOldPassword"></td>
									</tr>
									<tr class="transparent">
										<td class="transparent alignRight" style="padding: 10px"><lb:label key="dialog.myProfile.newpassword"
											suffix=":" /></td>
										<td><input type="password" style="width: 200px; font-family: sans-serif;" maxlength="32"
											id="profileNewPassword" name="profileNewPassword"></td>
									</tr>
									<tr class="transparent">
										<td class="transparent alignRight" style="padding: 10px"><lb:label key="dialog.myProfile.confirmpassword"
											suffix=":" /></td>
										<td><input type="password" style="width: 200px; font-family: sans-serif;" maxlength="32"
											id="profileConfirmPassword" name="profileConfirmPassword"></td>
									</tr>
									<tr class="transparent">
										<td class="transparent alignRight" style="padding: 10px"><lb:label key="dialog.myProfile.hintQues" suffix=":" /></td>
										<td><select style="width: 290px; font-family: sans-serif;" id="profileHintQues"
											name="profileHintQues"></select></td>
									</tr>
									<tr class="transparent">
										<td class="transparent alignRight" style="padding: 10px"><lb:label key="dialog.myProfile.hintAns" suffix=":" /></td>
										<td><input type="text" style="width: 200px; font-family: sans-serif;" maxlength="255"
											id="profileHintAns" name="profileHintAns"></td>
									</tr>
								</table>
								</td>
								<td width="2"></td>
								<td style ="vertical-align: top;" >
								<table>
									<tr>
										<td class="profileBullet">
										&raquo;
										</td>
										<td class="profileTransparent">
										<lb:label key="dialog.myProfile.msg.oldPassword" suffix="" />
										</td>
									</tr>
									<tr>
										<td colspan="2" class="profileTransparent">&nbsp;</td>
									</tr>						
									<tr>
										<td class="profileBullet">										
										&raquo;
										</td>
										<td class="profileTransparent">
										<lb:label key="dialog.myProfile.msg.confPassword" suffix="" />
										</td>
									</tr>
									<tr>
										<td colspan="2" class="profileTransparent">&nbsp;</td>
									</tr>						
									<tr>
										<td class="profileBullet">
										&raquo;
										</td>
										<td class="profileTransparent">										
										<lb:label key="dialog.myProfile.msg.hint" suffix="" />
										</td>
									</tr>
								</table>
								</td>
							</tr>							
						</table>
					</div>
				</div>
			</div>
			
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="EditTable" width="100%">
					<tbody>
						<br>
						<tr id="Act_Buttons" align="center">
							<td  width="100%">
								<center>
								<input type="button"  value=<lb:label key="common.button.save" prefix="&nbsp;" suffix="&nbsp;"/> onclick="javascript:verifyUserDetails(); return false;" class="ui-widget-header">
								<input type="button"  value=<lb:label key="common.button.cancel" prefix="&nbsp;" suffix="&nbsp;"/> onclick="javascript:onCancelProfile(); return false;" class="ui-widget-header">
								</center>
								<br>
							</td>
						</tr>
						<tr class="binfo" style="display: none;">
							<td colspan="2" class="bottominfo"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>	
</div>

<div id="profileConfirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="dialog.myProfile.msg.notsave" />
		</div>
	</div>
	<div style="padding:10px;">		
		<center>
			<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeProfileConfirmation(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closeProfilePopup('profileConfirmationPopup'); return false;" class="ui-widget-header">
		</center>
	</div>	
</div>

<div id="profileEmailWarning"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="dialog.myProfile.msg.emailprovide" />
		</div>
	</div>
	<div style="padding:10px;">		
		<center>
			<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeProfilePopup('profileEmailWarning'); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closeProfileEmailWarning(); return false;" class="ui-widget-header">
		</center>
	</div>	
</div>
