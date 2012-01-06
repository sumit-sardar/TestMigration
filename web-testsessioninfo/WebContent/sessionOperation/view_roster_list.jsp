<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<div id="displayMessageViewTestRoster" class="roundedMessage" style="display:none; margin-bottom: 15px;"> 
	<table>
		<tr>
			<td rowspan="3" valign="top">
                 	<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
			</td>
			<td>
				<table>
					<tr><td><font style="color: red; font-size:12px; font-weight:bold"><div id="rosterTitle"></div></font></td></tr>
					<tr><td><div id= "rosterContent">	</div></td></tr>
					<tr><td><div id= "rosterMessage" style="color: red; font-size:12px; font-weight:bold">	</div></td></tr>
				</table>
			</td>
		</tr>
	</table>
</div>	
<table width="865px" style="margin-bottom: 10px;">
	<tr>
		<td align="left" width="20%"><netui:content value="${bundle.web['viewStatus.testName']}"/></td>
		<td width="80%"><span id = "testName"></span></td>
	</tr>
	<tr>
		<td align="left"><netui:content value="${bundle.web['viewStatus.testAccessCode']}"/></td>
		<td><span id = "testAccessCode"></span></td>
	</tr>
	<tr>
		<td align="left"><netui:content value="${bundle.web['viewStatus.totalStudents']}"/></td>
		<td><span id = "totalStudents"></span></td>
	</tr>
</table>
<table width="865px" style="margin-bottom: 10px;">
	<tr>
		<td>
			<div id="toggleValidation" style="float:left;padding-top:5px;padding-right:5px;display:none;">
				<a href="#" id="toggleValidationButton" onclick="javascript:toggleValidationStatus(); return false;" class="rounded {transparent} button"><netui:content value="${bundle.web['viewStatus.toggleValidation.button']}" /></a>
			</div>
			<div id="refreshList" style="float:left;padding-top:5px;padding-right:5px;">
				<a href="#" id="refreshListButton" onclick="refreshRosterList()" class="rounded {transparent} button"><netui:content value="${bundle.web['viewStatus.refreshList.button']}"/></a>
				<input type="hidden" name="subtestValidationAllowed" id="subtestValidationAllowed" value="" />
			</div> 
		</td>
	</tr>
</table>
<table id="rosterList" class="gridTable"></table>
<div id="rosterPager" class="gridTable"></div>