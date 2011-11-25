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
											<td class="transparent" width="16%">Test Group :</td>
											<td width="84%"><select name="testGroupList" class="bigBox" id="testGroupList"
												onchange="javascript:changeGradeAndLevel(); return false;">

											</select></td>
										</tr>
										<tr class="transparent">
											<td class="transparent" height="20">
											<div id="gradeDiv" style="display: none;">Grade :</div>
											<div id="levelDiv" style="display: none;">Level :</div>
											</td>
											<td>
											<div id="levelDropDown"><select name="level" class="norBox" id="level"></select></div>
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
									<table id="testList" class="gridTable"></table>
									<div id="testPager" class="gridTable"></div>
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
							<td height="20" colspan="3" style="border-bottom: #0099FF 1px groove;"><strong>Select Test Group
							from the left and modify test details below</strong></td>
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
							<td height="20"><input type="checkbox" name="randomDis" value="1" class="norBox"></td>
							<td class="transparent">Random Distractor</td>
						</tr>
						<tr>
							<td width="31" height="20" align="left" valign="middle">
								<input type="checkbox" id="testBreak" value="1"	onClick="toggleAccessCode();" class="norBox"/>
							</td>
							<td width="864" class="transparent">Allow Test Breaks 
								<span style="padding-left:5px;"><input name="aCode" type="text" class="norBox" id="aCode" style="display:none; padding-left:2px;" size="16" /></span>
							</td>
						</tr>
						<tr>
							<td height="10" colspan="2"></td>
						</tr>
						<tr>
							<td height="10" colspan="2">
							<div id="noSubtest">
							<table width="100%" cellpadding="2" cellspacing="1" class="shadowBorder">
								<tr class="subtestHeader">
									<th width="24" height="20" align="center"><strong>#</strong></th>
									<th width="287" height="20" align="left"><strong>Subtest Name </strong></th>
									<th width="144" height="20">
									<div align="center"><strong>Access Code </strong></div>
									</th>
									<th width="73" height="20" align="center"><strong>Duration</strong></th>
								</tr>
								<tr>
									<td height="20" align="center">&nbsp;</td>
									<td height="20" align="left">&nbsp;</td>
									<td height="20">&nbsp;</td>
									<td height="20" align="center">&nbsp;</td>
								</tr>
								<tr>
									<td height="20" align="center">&nbsp;</td>
									<td height="20" align="left">No Test Session is selected !!</td>
									<td height="20">&nbsp;</td>
									<td height="20" align="center">&nbsp;</td>
								</tr>
								<tr>
									<td height="20" align="center">&nbsp;</td>
									<td height="20" align="left">&nbsp;</td>
									<td height="20">&nbsp;</td>
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