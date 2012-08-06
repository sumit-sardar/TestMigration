<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="studentRegistrationResource" />

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


<!--<div class="roundedMessage ui-corner-all" style="padding-top: 5px !important; margin-bottom: 5px !important;">
	
 </div>
 
 --><div style="padding-bottom: 5px !important;" class="roundedMessage ui-corner-all">
 <div style="clear: both; width: 99.99%; text-align: left;" class= "blueSubHeading">
 			<table class="transparent">
				<tr class="transparent">
					<td>
			    		<h1><lb:label key="registration.modifyManifest.caption" /></h1>
					</td>
				</tr>
				<tr> 
					<td class="mmsModifySubtestMsg">  
						<lb:label key="student.registration.page.subtitle" />
					</td>	
				</tr>
			</table>	
		 <!--<p id="mmsModifySubtestMsg" style="font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-top: 2pt; margin-bottom: 0pt;">
		</p>
	--></div>
		 <table id="msmLabelTb" style="margin-top: 10px; width: 808px;">
			<tr>
				<td align="left" width="16%"  style="padding: 2px !important;"><lb:label key="viewStatus.subtest.testSessionName" /></td>
				<td width="80%"><span id = "msmTestSessionName1"></span></td>
			</tr>
			<tr>
				<td align="left" width="16%" style="padding: 2px !important;"><lb:label key="common.lebel.name.student.name" /></td>
				<td width="80%"><span id = "msmStudentName"></span></td>
			</tr>
			<tr>
				<td align="left" width="16%" style="padding: 2px !important;"><lb:label key="common.lebel.name.org.name" /></td>
				<td width="80%"><span id = "msmOrgName"></span></td>
			</tr>
			<tr >
					<td id="msmLocatorInfo"   style="display: none;" align="left" width="16%" style="padding: 2px !important;"><lb:label key="common.lebel.name.locator.session" /></td>
					<td id="msmLocatorInfo1" style="display: none;" width="80%"><span id = "msmLocatorDetail"></span></td>
			</tr >

		</table>
	</div>
 
 <div id="mStdMlocatorDiv" class="roundedMessage ui-corner-all" style="display: block; padding-top: 5px !important;">
	<div style="font-weight: bold; color: rgb(46, 110, 158);" id="mStdMlocatorSubtestGridHeading"> 
		Locator Test
		<img src="/SessionWeb/resources/images/questionmark.jpg" onmouseover="showLocatorMessage(event);" onmouseout="hideLocatorMessage();" id="mStdMlocatorSubtestGridToolTip"> 
	</div>
	<div id="mStdMlocatorContentDiv" style="clear: both; width: 99.99%; text-align: left;" class= "blueSubHeading">
		
	</div>
 </div>
<div id="subtestTableDiv">
<table class="layout ">
	<tbody>
		<tr class="layout">
			<td width="48%" valign="top" class="layout">
			<table class="columnLayout" id=leftTableContainerMsm>
				<tbody>
					<tr class="columnLayoutRow">
						<td class="">
						<table class="dynamicHeader">
							<tbody>
								<tr class="dynamicHeader">
									<th height="25" class="subtestHeader">
									<input type="hidden" value="0" name="numberOfRowsMsm" id="numberOfRowsMsm">
									<lb:label key="session.modifySubtest.header.availableSubtest" /> 
								</th>
								</tr>
							</tbody>
						</table>
						<table onselectstart="return false;" class="dynamic">
							<tbody id="availableSubtestsTableMsm">
							
							
							
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
										onclick="return addSubtestSelectedRow(chosenRow);" disabled="true" id="addRowMsm" name="addRowMsm" value="&gt;"></td>
								</tr>
								<tr class="transparent">
									<td class="transparent"><input type="button" style="width: 30px;"  class ="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br"
										onclick="return addSubtestAllSelectedRows(chosenRow);" disabled="true" id="addAllRowsMsm" name="addAllRowsMsm"
										value="&gt;&gt;"></td>
								</tr>
								<tr class="transparent">
									<td class="transparent">&nbsp;</td>
								</tr>
								<tr class="transparent">
									<td class="transparent"><input type="button" style="width: 30px;" class ="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br"
										onclick="return removeSubtestSelectedRow(chosenRow);" disabled="true" id="removeRowMsm" name="removeRowMsm" value="&lt;"></td>
								</tr>
								<tr class="transparent">
									<td class="transparent"><input type="button" style="width: 30px;" class ="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br"
										onclick="return removeSubtestAllSelectedRows(chosenRow);" disabled="true" id="removeAllRowsMsm" name="removeAllRowsMsm" value="&lt;&lt;"></td>
								</tr>
							</tbody>
						</table>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
			<td width="48%" valign="top" class="layout">
			<table class="columnLayout" id="rightTableContainerMsm">
				<tbody>
					<tr class="columnLayoutRow">
						<td class="">						
						<table onselectstart="return false;" class="dynamic">
							<thead>
								<tr class="dynamicHeader">
									<th height="25" class="subtestHeader">
									<lb:label key="session.modifySubtest.header.selectedSubtest" />
									 </th>
									<th height="25" id = "modifyTestLevelMsm" class="subtestHeader" style="display: none;" width = "10px">
									<lb:label key="session.modifySubtest.header.selectedSubtestlevel" />
									 </th>
								</tr>
							</thead>
							<tbody id="selectedSubtestsTableMsm">
							
							
							
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
						<td class="transparent">
						<input type="button"
							style="font-family: Arial, Verdana, Sans Serif; font-size: 11px;" disabled="disabled" onclick="return moveSubtestRow( chosenRow, -1 );"
							id="moveUpMsm" name="moveUp" value=<lb:label key="session.modifySubtest.moveup" prefix="'&nbsp;" suffix="&nbsp;'"/> >&nbsp;
							<input type="button"
							style="font-family: Arial, Verdana, Sans Serif; font-size: 11px;" disabled="disabled" onclick="return moveSubtestRow( chosenRow, 1 );"
							id="moveDownMsm" name="moveDown" value=<lb:label key="session.modifySubtest.movedown" prefix="'&nbsp;" suffix="&nbsp;'"/> disabled="true">&nbsp;</td>
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



</div>



