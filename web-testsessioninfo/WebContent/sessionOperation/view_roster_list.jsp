<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<table width="100%" border="0">
<tr><td>
	<div style="margin-bottom: 2px;" width="920" class="subtitle">
		<lb:label key="viewStatus.rosterList.instText" />
	</div>
	<div id = "displayMessageViewTestRoster" 
			style="display: none; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; border:1px solid #D4ECFF; margin-bottom:5px; padding:2px;">	
		<table width="920">
			<tbody>
				<tr>
					<td valign="middle" width="18">
						<img height="16" src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif">
					</td>
					<td valign="middle" style="font-weight: bold;">
						<span id = 'rosterMessage'></span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</td></tr>
<tr><td style="font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table id="labelTbl" style="margin-top: 5px; width: 924px;">
		<tr>
			<td align="left" width="14%"><lb:label key="viewStatus.testName" /></td>
			<td width="80%"><span id = "rosterTestName"></span></td>
		</tr>
	</table>
	<table id="buttonTbl" style="width: 924px;">
		<tr>
			<td align="left" width="15%"><lb:label key="viewStatus.testAccessCode" /></td>
			<td width="15%"><span id = "rosterTestAccessCode"></span></td>
			<td align="left" width="15%"><lb:label key="viewStatus.totalStudents" /></td>
			<td width="10%"><span id = "rosterTotalStudents"></span></td>
			<td width="45%" class="buttonsRow">
				<div id="refreshList" style="float:right;padding-left:5px;">
					<a href="#" id="refreshListButton" onclick="refreshRosterList()" class="rounded {transparent} button"><lb:label key="viewStatus.refreshList.button" /></a>
					<input type="hidden" name="subtestValidationAllowed" id="subtestValidationAllowed" value="" />
				</div> 
				<div id="toggleValidation" style="float:right;padding-left:5px;display:none;">
					<a href="#" id="toggleValidationButton" onclick="javascript:toggleValidationStatus(); return false;" class="rounded {transparent} button"><lb:label key="viewStatus.toggleValidation.button" /></a>
				</div>
				<div id="doNotScore" style="float:right;padding-left:5px;display:none;">
					<a href="#" id="doNotScoreButton" onclick="javascript:toggleDonotScoreStatus(); return false;" class="rounded {transparent} button"><lb:label key="viewStatus.doNotScore.button" /></a>
				</div>
			</td>
		</tr>
	</table>
	<table id="rosterList" class="gridTable"></table>
	<div id="rosterPager" class="gridTable"></div>
	
	<div>
		<input type="hidden" name="lastNameLbl" id="lastNameLbl" value=<lb:label key="viewStatus.text.lastNameLbl" prefix="'" suffix="'"/>/>
		<input type="hidden" name="firstNameLbl" id="firstNameLbl" value=<lb:label key="viewStatus.text.firstNameLbl" prefix="'" suffix="'"/>/>
		<input type="hidden" name="studentIdLbl" id="studentIdLbl" value=<lb:label key="viewStatus.text.studentId" prefix="'" suffix="'"/>/>
		<input type="hidden" name="loginIdLbl" id="loginIdLbl" value=<lb:label key="viewStatus.text.loginId" prefix="'" suffix="'"/>/>
		<input type="hidden" name="passwordLbl" id="passwordLbl" value=<lb:label key="viewStatus.text.password" prefix="'" suffix="'"/>/>
		<input type="hidden" name="validationStatusLbl" id="validationStatusLbl" value=<lb:label key="viewStatus.text.validationStatus" prefix="'" suffix="'"/>/>
		<input type="hidden" name="onlineTestStausLbl" id="onlineTestStausLbl" value=<lb:label key="viewStatus.text.onlineTestStatus" prefix="'" suffix="'"/>/>
		<input type="hidden" name="dnsLbl" id="dnsLbl" value=<lb:label key="viewStatus.text.dnsLbl" prefix="'" suffix="'"/>/>
	</div>
</td>
</tr>
</table>