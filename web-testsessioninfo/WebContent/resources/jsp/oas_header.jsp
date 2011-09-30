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
					<!-- 
                	<a href="<netui-template:attribute name="helpLink"/>" onClick="return showHelpWindow(this.href);"><b>Help</b></a>&nbsp;&nbsp;
					 -->
                	<a href="/TestSessionInfoWeb/homepage/HomePageController.jpf?OldUI=true"><b>Help</b></a>&nbsp;&nbsp;
					<img src="<%=request.getContextPath()%>/resources/images/dotdot.jpg"/>&nbsp;&nbsp;
					<a href="#" onclick="gotoAction('logout.do');"><b>Logout</b></a>
				</div>
				</td>
			</tr>
			</table>
		</td>
	</tr>
</table>

 
<!-- Broadcast Message Dialog -->
<div id="broadcastMsgDialogId" style="display: none; background-color: #d4ecff; padding: 10px;">
	<div class="feature" style="padding: 10px;">
	<h2>Broadcast Message</h2><br/>
	<p>
		<b>On August 26, 2011 at 5pm PDT to 9pm August 28, 2011 the TerraNova Online testing system will be unavailable as we undergo scheduled maintenance.</b>
		<br/>Once the maintenance is complete on Monday August 29, 2011, all TerraNova Online customers will need to <a href="https://oas.ctb.com/help/uninstalling_the_test_client_software.htm" target="uninstall_TDC">uninstall</a> the version 8.7 Test Delivery Client (TDC) and download and <a href="https://oas.ctb.com/help/installing_the_online_assessment_software_on_student_workstations.htm" target="install_TDC">install</a> the new version 9.0.
		<br/><b>All uninstall/reinstall updates will need to be completed by October 31, 2011.</b>
	</p>
	</div>
</div>


<!-- My profile Dialog -->
<div id="myProfileDialogId" style="display: none; background-color: #d4ecff; padding: 10px;">
	<div class="feature" style="padding: 10px;">
	<h2>My Profile</h2>
	<p>Review your information listed below.</p>
	
	<table>		
		<tr><td width="150">First Name</td><td>Tai</td></tr>
		<tr><td>Middle Name</td><td></td></tr>
		<tr><td>Last Name</td><td>Tabe</td></tr>
		<tr><td>Login ID</td><td>tai_tabe</td></tr>
		<tr><td>Organization</td><td>TABE Customer</td></tr>
	</table>
	</div>
</div>


<!-- BLOCK DIV -->
<div id="blockDiv" style="display:none; background-color: #d0e5f5; opacity:0.5; position:fixed;top:0;left:0;width:100%;height:100%;z-index:9999">
	<img src="<%=request.getContextPath()%>/resources/images/loading.gif" style="left:50%;top:40%;position:absolute;"/>
</div>


