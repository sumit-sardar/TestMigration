<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<table class="headerLayout">
	<tr>
		<td align="left" width="70%" style="padding: 3px 0px 0px 3px;"><img src="<%=request.getContextPath()%>/resources/images/ctb_oas_logo.png"></td>
		<td align="left" width="30%">
			<table border="0" cellpadding="0" cellspacing="0">
			<tr height="22">
				<td align="center">
					<b>You are logged in as <%=session.getAttribute("userName")%></b>
				</td>
			</tr>
			<tr>
				<td>
					<div class="roundedHeader">
					&nbsp;&nbsp;<a href="#" onClick="viewBroadcastMessage();"><b>Home</b></a>&nbsp;&nbsp;
					<img src="<%=request.getContextPath()%>/resources/images/dotdot.jpg"/>&nbsp;&nbsp;
					<a href="#" onClick="viewMyProfile();"><b>My Profile</b></a>&nbsp;&nbsp;
					<img src="<%=request.getContextPath()%>/resources/images/dotdot.jpg"/>&nbsp;&nbsp;
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

 
<!-- Broadcast Message Dialog -->
<div id="broadcastMsgDialogId" style="display: none; background-color: #d4ecff; padding: 10px;">
	<div class="feature" style="padding: 10px;" id="broadcastMsgBody">
		<div id="broadcastMsgContent">
		</div>
	</div>
</div>
 
<!-- My profile Dialog -->
<div id="myProfileDialogId" style="display: none; background-color: #d4ecff; padding: 10px;">
	<div class="feature" style="padding: 10px;">
		<div id="displayName"></div><br/>
		<div id="fileName"></div><br/>
		<div id="size"></div><br/>
	</div>
</div>



