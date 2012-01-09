<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<div id = "displayMessageViewTestSubtest" 
		style="display: none; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;padding-bottom:5px;">	
<table>
	<tbody>
		<tr width='100%'>
			<th style='padding-right: 6px; text-align: right;' rowspan='2'>
				<img height='23' src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif">
			</th>
		</tr>
		<tr width='100%'>
			<td>
				<span id = 'subtestMessage'></span>
			</td>
		</tr>
	</tbody>
</table>
</div>
<table width="928px" style="margin-bottom: 10px;">
	<tr>
		<td><netui:content value="${bundle.web['viewStatus.subtest.loginName']}"/></td>
		<td><span id = "loginName"></span></td>
	</tr>
	<tr>
		<td><netui:content value="${bundle.web['viewStatus.subtest.password']}"/></td>
		<td><span id = "password"></span></td>
	</tr>
	<tr>
		<td><netui:content value="${bundle.web['viewStatus.subtest.testSessionName']}"/></td>
		<td><span id = "testAdminName"></span></td>
	</tr>
	<tr>
		<td><netui:content value="${bundle.web['viewStatus.subtest.testName']}"/></td>
		<td><span id = "subTestName"></span></td>
	</tr>
	<tr>
		<td><netui:content value="${bundle.web['viewStatus.subtest.testStatus']}"/></td>
		<td><span id = "testStatus"></span></td>
	</tr>
</table>
<table width="928px" style="margin-bottom: 10px;">
	<tr>
		<td>
			<div id="toggleValidationSubTest" style="float:left;padding-top:5px;display:none">
				<a href="#" id="toggleValidationSubtestButton" onclick="javascript:toggleSubtestValidationStatus(); return false;" class="rounded {transparent} button"><netui:content value="${bundle.web['viewStatus.toggleValidation.button']}"/></a>
			</div> 
		</td>
	</tr>
</table>

<table id="subtestList" class="rosterSubtestTable"></table>
<input type="hidden" name="itemsSelectLbl" id="itemsSelectLbl" value="${bundle.web['common.column.select']}"/>
<input type="hidden" name="subtestNameLbl" id="subtestNameLbl" value="${bundle.web['ViewSubtestDetails.text.subtestName']}"/>
<input type="hidden" name="subtestLevelLbl" id="subtestLevelLbl" value="${bundle.web['ViewSubtestDetails.text.subtestLevel']}"/>
<input type="hidden" name="subtestStatusLbl" id="subtestStatusLbl" value="${bundle.web['ViewSubtestDetails.text.subtestStatus']}"/>
<input type="hidden" name="validationStatusLbl" id="validationStatusLbl" value="${bundle.web['ViewSubtestDetails.text.validationStatus']}"/>
<input type="hidden" name="startDateLbl" id="startDateLbl" value="${bundle.web['ViewSubtestDetails.text.startDate']}"/>
<input type="hidden" name="completionDateLbl" id="completionDateLbl" value="${bundle.web['ViewSubtestDetails.text.completionDate']}"/>
<input type="hidden" name="totalItemsLbl" id="totalItemsLbl" value="Total Items"/>
<input type="hidden" name="itemsCorrectLbl" id="itemsCorrectLbl" value="Items Correct"/>
<input type="hidden" name="itemsScoredLbl" id="itemsScoredLbl" value="Items to be Scored"/>

<div id="subtestPager" class="gridTable"></div>