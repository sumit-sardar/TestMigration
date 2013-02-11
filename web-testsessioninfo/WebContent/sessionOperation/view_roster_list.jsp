<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<link href="<%=request.getContextPath()%>/resources/css/jtip.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jtip.js"></script>

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<table width="100%" border="0">
<tr><td>
	<div style="margin-bottom: 2px;width:920px;" class="subtitle">
		<lb:label key="viewStatus.rosterList.instText" /><div id="assignFormMsg" style="display: none"><lb:label key="viewStatus.rosterList.assignForm"/></div>	
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
		<tr height="25">
			<td align="left" width="14%"><lb:label key="viewStatus.testName" /></td>
			<td width="80%"><span id = "rosterTestName"></span></td>
		</tr>
		<tr height="25">
			<td align="left" width="14%"><lb:label key="viewStatus.subtest.sessionName" /></td>
			<td width="80%">
			<span id="testAdminName"></span>
			<span id="subtestDetailTooltip" style="display: none">&nbsp;&nbsp;&lt<a href="showSubtestDetails.do?width=600" class="jTip" id="tooltip" name="Subtest Details">Subtest Details</a>&gt</span>
			</td>
		</tr>
	</table>
	<table id="buttonTbl" style="width: 924px;">
		<tr>
			<td align="left" width="10%"><lb:label key="viewStatus.totalStudents" /></td>
			<td width="12%"><span id = "rosterTotalStudents"></span></td>
			<td width="45%" class="buttonsRow">
				<div id="assignFormButton" style="float:right;padding-left:5px;display:none;">
					<a href="#" id="assignRosterForm" onclick="javascript:openAssignFormPopup()" class="rounded {transparent} button"><lb:label key="viewStatus.assignRosterForm.text" /></a>
				</div>
				<div id="refreshList" style="float:right;padding-left:5px;">
					<a href="#" id="refreshListButton" onclick="refreshRosterList()" class="rounded {transparent} button"><lb:label key="viewStatus.refreshList.button" /></a>
					<input type="hidden" name="subtestValidationAllowed" id="subtestValidationAllowed" value="" />
				</div> 
			    <div id="profileReportStudentDiv" style="float:right;padding-left:5px;display:none;">
					<a href="#" id="profileReportStudentButton" onclick="javascript:viewIndividualReport(this, 'student'); return false;" class="rounded {transparent} button"><lb:label key="homepage.button.studentReport" /></a>
			    </div>
				<div id="toggleValidation" style="float:right;padding-left:5px;display:none;">
					<a href="#" id="toggleValidationButton" onclick="javascript:toggleValidationStatus(); return false;" class="rounded {transparent} button"><lb:label key="viewStatus.toggleValidation.button" /></a>
				</div>
				<div id="doNotScore" style="float:right;padding-left:5px;display:none;">
					<a href="#" id="doNotScoreButton" onclick="javascript:showDNSConfirmationPopup(); return false;" class="rounded {transparent} button"><lb:label key="viewStatus.doNotScore.button" /></a>
				</div>
			</td>
		</tr>
	</table>
	<table id="rosterList" class="gridTable"></table>
	<div id="rosterPager" class="gridTable"></div>
	<div id="dNSconfirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
		<div style="padding:10px;text-align:center;">
			<div style="text-align: left;">
				<lb:label key="viewStatus.doNotScore.confirmation.message" />
			</div>
		</div>
		<div style="padding:10px;">		
			<center>
				<br>
				<br>
				<input type="button"  value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:toggleDonotScoreStatus(); return false;" class="ui-widget-header">&nbsp;
				<input type="button"  value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closeDNSConfirmationPopup(); return false;" class="ui-widget-header">
			</center>
		</div>	
	</div>
	<div id="assignFormPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
		<div style="padding:10px;text-align:left;">
			<div style="margin-bottom: 5px;width:265px;" class="subtitle">
				<lb:label key="viewStatus.assignRosterForm.message" />
			</div>
			<div style="text-align: left; font-size: 12px; font-style: normal; font-weight: normal; border:1px solid #D4ECFF;">
				<table width="100%" border="0" style= "margin-top: 5px">
					<tr class="transparent">
						<td width="35%" height="20" valign="top" class="transparent" align="left"><lb:label key="viewStatus.label.form" /></td>
						<td width="65%" height="20" valign="middle" align="left"><select name="testFormList" id="testFormList">
					  </select></td>
					</tr>
				</table>
			</div>
		</div>
		<div style="padding:10px;">		
			<center>
				<input type="button"  value=<lb:label key="common.button.save" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:assignRosterForm(); return false;" class="ui-widget-header">&nbsp;
				<input type="button"  value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closeAssignFormPopup(); return false;" class="ui-widget-header">
			</center>
		</div>	
	</div> 
	<div>
		<input type="hidden" name="lastNameLbl" id="lastNameLbl" value=<lb:label key="viewStatus.text.lastNameLbl" prefix="'" suffix="'"/>/>
		<input type="hidden" name="firstNameLbl" id="firstNameLbl" value=<lb:label key="viewStatus.text.firstNameLbl" prefix="'" suffix="'"/>/>
		<input type="hidden" name="studentIdLbl" id="studentIdLbl" value=<lb:label key="viewStatus.text.studentId" prefix="'" suffix="'"/>/>
		<input type="hidden" name="loginIdLbl" id="loginIdLbl" value=<lb:label key="viewStatus.text.loginId" prefix="'" suffix="'"/>/>
		<input type="hidden" name="passwordLbl" id="passwordLbl" value=<lb:label key="viewStatus.text.password" prefix="'" suffix="'"/>/>
		<input type="hidden" name="validationStatusLbl" id="validationStatusLbl" value=<lb:label key="viewStatus.text.validationStatus" prefix="'" suffix="'"/>/>
		<input type="hidden" name="onlineTestStausLbl" id="onlineTestStausLbl" value=<lb:label key="viewStatus.text.onlineTestStatus" prefix="'" suffix="'"/>/>
		<input type="hidden" name="dnsLbl" id="dnsLbl" value=<lb:label key="viewStatus.text.dnsLbl" prefix="'" suffix="'"/>/>
		<input type="hidden" name="dNSConfirmationPopupLbl" id="dNSConfirmationPopupLbl" value=<lb:label key="viewStatus.text.dNSConfirmationPopupLbl" prefix="'" suffix="'"/>/>
		<input type="hidden" name="rosterFormLbl" id="rosterFormLbl" value=<lb:label key="viewStatus.text.formLbl" prefix="'" suffix="'"/>/>		
		<input type="hidden" name="assignFormPopupLbl" id="assignFormPopupLbl" value=<lb:label key="viewStatus.assignRosterForm.text" prefix="'" suffix="'"/>/>
	</div>
</td>
</tr>
</table>