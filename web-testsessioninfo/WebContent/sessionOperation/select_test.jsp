<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web" />

<table class="transparent" align="center">
	<tbody>
		<tr class="transparent">
			<td>
			<table class="transparent" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td style="vertical-align: top;">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="46%" valign="top" class="divider" style="padding-right: 5px;">
							<table width="100%" border="0">
								<tr>
									<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="4">
										<tr class="transparent">
											<td class="transparent" width="20%">Test Group :</td>
											<td width="80%"><select name="testGroupList" id="testGroupList"
												onchange="javascript:changeGradeAndLevel(); return false;">

											</select></td>
										</tr>
										<tr class="transparent">
											<td class="transparent" height="20">
											<div id="gradeDiv" style="display: none;">Grade :</div>
											<div id="levelDiv" style="display: none;">Level :</div>
											</td>
											<td>
											<div id="levelDropDown"><select name="level" id="level" style="display: none;" onchange="javascript:changeSessionList(); return false;" ></select></div>
											</td>
										</tr>
										
									</table>
									</td>
								</tr>
								<tr>
									<td height="10" colspan="2"></td>
								</tr>
								<tr>
									<td>
									<div id="sessionListDiv">
										<table id="testList" class="gridTable"></table>
										<div id="testPager" class="gridTable"></div>
									</div>
									</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
					<td width="1"></td>
					<td width="54%" style="padding-left: 7px; vertical-align: top;">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="20" colspan="3" style="border-bottom: #0099FF 1px groove;"><strong><netui:content value="${bundle.web['sessionList.selectTest.testSelect.title']}"/></strong></td>
						</tr>
						<tr>
							<td height="10" colspan="2"></td>
						</tr>
						<tr>
							<td height="20" colspan="2">Selected Test : <span id="testSessionName_lbl" class="lblDisabled">No Test Session is selected</span></td>
						</tr>
						<tr>
							<td height="10" colspan="2"></td>
						</tr>
						<tr>
							<td height="20"><input type="checkbox" id="randomDis" name="randomDis" value="1"></td>
							<td class="transparent"><span id="randDisLbl">Random Distractor</span></td>
						</tr>
						<tr>
							<td width="31" height="20" align="left" valign="middle">
								<input type="checkbox" id="testBreak" value="1"	onClick="toggleAccessCode();"/>
							</td>
							<td width="864" class="transparent">Allow Test Breaks 
								<span style="padding-left:5px;"><input name="aCode" type="text" id="aCode" style="visibility:hidden; padding-left:2px;" size="16" /></span>
							</td>
						</tr>
						<tr>
							<td height="10" colspan="2"></td>
						</tr>
						<tr>
							<td height="10" colspan="2" valign="top">
							<div id="noSubtest">
							<table width="100%" cellpadding="2" cellspacing="0" class="shadowBorder">
								<tr class="subtestHeader">
									<th width="4%" height="20" align="center"><strong>#</strong></th>
									<th width="33%" height="20" align="left"><strong>Subtest Name </strong></th>									
									<th width="33%" height="20" align="center"><strong>Duration</strong></th>
								</tr>
								<tr>
									<td height="20" align="center">&nbsp;</td>
									<td height="20" align="left">&nbsp;</td>
									<td height="20" align="center">&nbsp;</td>
								</tr>
								<tr>
									<td height="20" align="center">&nbsp;</td>
									<td height="20" align="left">No Test is selected !!</td>
									<td height="20" align="center">&nbsp;</td>
								</tr>
								<tr>
									<td height="20" align="center">&nbsp;</td>
									<td height="20" align="left">&nbsp;</td>
									<td height="20" align="center">&nbsp;</td>
								</tr>
							</table>
							</div>
							<div id="subtestGrid" style="display:none;"></div>
							
							</td>
						</tr>
						<tr>
							<td height="10" colspan="2"></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>

			</td>
		</tr>
</table>