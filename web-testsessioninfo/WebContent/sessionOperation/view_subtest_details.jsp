<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<div id = "displayMessageViewTestSubtest" 
		style="display: none; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; border:1px solid #D4ECFF; margin-bottom:5px; padding:2px;">	
	<table width="99.5%">
		<tbody>
			<tr>
				<td valign="middle" width="18">
					<img height="16" src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif">
				</td>
				<td valign="middle" style="font-weight: bold;">
					<span id="subtestMessage"></span>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<table style="margin-bottom: 10px; width: 924px;">
	<tr>
		<td width="135"><lb:label key="viewStatus.subtest.loginName"/></td>
		<td><span id = "loginName"></span></td>
	</tr>
	<tr>
		<td><lb:label key="viewStatus.subtest.password"/></td>
		<td><span id = "password"></span></td>
	</tr>
	<tr>
		<td><lb:label key="viewStatus.subtest.sessionName"/></td>
		<td><span id = "testAdminName_acco"></span></td>
	</tr>
	<tr>
		<td><lb:label key="viewStatus.subtest.testName"/></td>
		<td><span id = "subTestName"></span></td>
	</tr>
	<tr>
		<td><lb:label key="viewStatus.subtest.testStatus"/></td>
		<td><span id = "testStatus"></span></td>
	</tr>
	<tr id="testGradeRow" style="display: none;">
		<td><lb:label key="viewStatus.subtest.testGrade"/></td>
		<td><span id = "testGrade"></span></td>
	</tr>
	<tr id="testLevelRow" style="display: none;">
		<td><lb:label key="viewStatus.subtest.testLevel"/></td>
		<td><span id = "testLevel"></span></td>
	</tr>
</table>
<table style="margin-bottom: 10px; width: 350px;">
	<tr>
		<td>
			<div id="toggleValidationSubTest" style="float:left;padding-top:5px;display:none">
				<a href="#" id="toggleValidationSubtestButton" onclick="javascript:toggleSubtestValidationStatus(); return false;" class="rounded {transparent} button"><lb:label key="viewStatus.toggleValidation.button"/></a>
			</div> 
		</td>
		<td>
			<div id="toggleExemtionSubTest" style="float:left;padding-top:5px;display:none">
				<a href="#" id="toggleExemtionSubtestButton" onclick="javascript:toggleExemtionValidationStatus(); return false;" class="rounded {transparent} button"><lb:label key="viewStatus.toggleExemtion.button"/></a>
			</div> 
		</td>
		<td>
			<div id="toggleAbsentSubTest" style="float:left;padding-top:5px;display:none">
				<a href="#" id="toggleAbsentSubtestButton" onclick="javascript:toggleAbsentValidationStatus(); return false;" class="rounded {transparent} button"><lb:label key="viewStatus.toggleAbsent.button"/></a>
			</div> 
		</td>
	</tr>
</table>

<table id="subtestList" class="rosterSubtestTable"></table>
<div>
	<input type="hidden" name="itemsSelectLbl" id="itemsSelectLbl" value=<lb:label key="common.column.select" prefix="'" suffix="'"/>/>
	<input type="hidden" name="subtestNameLbl" id="subtestNameLbl" value=<lb:label key="ViewSubtestDetails.text.subtestName" prefix="'" suffix="'"/>/>
	<input type="hidden" name="subtestLevelLbl" id="subtestLevelLbl" value=<lb:label key="ViewSubtestDetails.text.subtestLevel" prefix="'" suffix="'"/>/>
	<input type="hidden" name="subtestStatusLbl" id="subtestStatusLbl" value=<lb:label key="ViewSubtestDetails.text.subtestStatus" prefix="'" suffix="'"/>/>
	<input type="hidden" name="validationStatusLbl" id="validationStatusLbl" value=<lb:label key="ViewSubtestDetails.text.validationStatus" prefix="'" suffix="'"/>/>
	<input type="hidden" name="exemtionStatusLbl" id="exemtionStatusLbl" value=<lb:label key="ViewSubtestDetails.text.exemtionStatus" prefix="'" suffix="'"/>/>
	<input type="hidden" name="absentStatusLbl" id="absentStatusLbl" value=<lb:label key="ViewSubtestDetails.text.absentStatus" prefix="'" suffix="'"/>/>
	<input type="hidden" name="startDateLbl" id="startDateLbl" value=<lb:label key="homepage.grid.startDate" prefix="'" suffix="'"/>/>
	<input type="hidden" name="completionDateLbl" id="completionDateLbl" value=<lb:label key="ViewSubtestDetails.text.completionDate" prefix="'" suffix="'"/>/>
	<input type="hidden" name="totalItemsLbl" id="totalItemsLbl" value=<lb:label key="ViewSubtestDetails.text.totalItems" prefix="'" suffix="'"/>/>
	<input type="hidden" name="itemsCorrectLbl" id="itemsCorrectLbl" value=<lb:label key="ViewSubtestDetails.text.itemCorrect" prefix="'" suffix="'"/>/>
	<input type="hidden" name="itemsScoredLbl" id="itemsScoredLbl" value=<lb:label key="ViewSubtestDetails.text.itemToBeScored" prefix="'" suffix="'"/>/>
</div>
<div id="subtestPager" class="gridTable"></div>