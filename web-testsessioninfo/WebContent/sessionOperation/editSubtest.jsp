<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web" />

<div id="modifyTestPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">

<div id="displaySubtestValidationMsg" class="roundedMessage" style="display:none; margin-bottom: 3px;"> 
			<table>
				<tr>
					<td rowspan="3" valign="top" width="18">
                   	<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
					</td>
					<td valign="top">
						<table>
							<tr><td><font style="color: red; font-size:12px; font-weight:bold"><div id="subTitle"></div></font></td></tr>
							<tr><td><div id= "subContent">	</div></td></tr>
						</table>
					</td>
				</tr>
			</table>
			
		</div>
<div class="roundedMessage ui-corner-all">
	<div style="clear: both; width: 99.99%; text-align: left;" class= "blueSubHeading">
		<p id="modifySubtestMsg" style="font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-top: 2pt; margin-bottom: 5pt; padding: 5">
		<lb:label key="session.modifySubtest.tabe.message" />
		</p>
	</div>

<table class="layout ">
	<tbody>
		<tr class="layout">
			<td width="48%" valign="top" class="layout">
			<table class="columnLayout" id=leftTableContainer>
				<tbody>
					<tr class="columnLayoutRow">
						<td class="">
						<table class="dynamicHeader">
							<tbody>
								<tr class="dynamicHeader">
									<th height="25" class="subtestHeader">
									<input type="hidden" value="0" name="numberOfRows" id="numberOfRows">
									<lb:label key="session.modifySubtest.header.availableSubtest" /> 
								</th>
								</tr>
							</tbody>
						</table>
						<table onselectstart="return false;" class="dynamic">
							<tbody id="availableSubtestsTable">
							
							
							
							</tbody>
						</table>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
			<td width="4%" valign="top" class="layout">
			<table class="columnButtonLayout">
				<tbody>
					<tr class="columnButtonLayout">
						<td class="columnButtonLayout">
						<table class="transparent">
							<tbody>
								<tr class="transparent">
									<td class="transparent"><input type="button" class ="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br" style="width: 30px;"
										onclick="return addSubtestSelectedRow(chosenRow);" disabled="true" id="addRow" name="addRow" value="&gt;"></td>
								</tr>
								<tr class="transparent">
									<td class="transparent"><input type="button" style="width: 30px;"  class ="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br"
										onclick="return addSubtestAllSelectedRows(chosenRow);" disabled="true" id="addAllRows" name="addAllRows"
										value="&gt;&gt;"></td>
								</tr>
								<tr class="transparent">
									<td class="transparent">&nbsp;</td>
								</tr>
								<tr class="transparent">
									<td class="transparent"><input type="button" style="width: 30px;" class ="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br"
										onclick="return removeSubtestSelectedRow(chosenRow);" disabled="true" id="removeRow" name="removeRow" value="&lt;"></td>
								</tr>
								<tr class="transparent">
									<td class="transparent"><input type="button" style="width: 30px;" class ="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br"
										onclick="return removeSubtestAllSelectedRows(chosenRow);" disabled="true" id="removeAllRows" name="removeAllRows" value="&lt;&lt;"></td>
								</tr>
							</tbody>
						</table>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
			<td width="48%" valign="top" class="layout">
			<table class="columnLayout" id="rightTableContainer">
				<tbody>
					<tr class="columnLayoutRow">
						<td class="">
						<table class="dynamicHeader">
							<tbody>
								<tr class="dynamicHeader">
									<th height="25" class="subtestHeader"><lb:label key="session.modifySubtest.header.selectedSubtest" /> </th>
									<th height="25" id = "modifyTestLevel" class="subtestHeader" style="display: none;"><lb:label key="session.modifySubtest.header.selectedSubtestlevel" /> </th>
								</tr>
							</tbody>
						</table>
						<table onselectstart="return false;" class="dynamic">
							<tbody id="selectedSubtestsTable">
							
							
							
							</tbody>
						</table>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
		</tr>
		<tr class="layoutButtom">
			<td colspan="3" class="layoutButtom">
			<table align="right" class="transparent">
				<tbody>
					<tr class="transparent">
						<td class="transparent"><input type="button"
							style="font-family: Arial, Verdana, Sans Serif; font-size: 11px;" disabled="disabled" onclick="return moveSubtestRow( chosenRow, -1 );"
							id="moveUp" name="moveUp" value=<lb:label key="session.modifySubtest.moveup" prefix="'&nbsp;" suffix="&nbsp;'"/> >&nbsp;
							<input type="button"
							style="font-family: Arial, Verdana, Sans Serif; font-size: 11px;" disabled="disabled" onclick="return moveSubtestRow( chosenRow, 1 );"
							id="moveDown" name="moveDown" value=<lb:label key="session.modifySubtest.movedown" prefix="'&nbsp;" suffix="&nbsp;'"/> disabled="true">&nbsp;</td>
					</tr>
				</tbody>
			</table>
			</td>
		</tr>
	</tbody>
</table>
</div>
<div style="padding: 10px;">
<center><input type="button" value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/>
	onclick=" javascript:validateAndUpdateSubtest('modifyTestPopup');return false;" class="ui-widget-header" style="width: 60px">&nbsp;
<input type="button" value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/>
	onclick="javascript:closePopUp('modifyTestPopup'); return false;" class="ui-widget-header" style="width: 60px">
</center>
</div>

</div>



