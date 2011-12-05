<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web" />

<table class="transparent" align="center">
	<tbody>
		<tr class="transparent">
			<td>
			<table class="transparent" width="100%" cellpadding="0" cellspacing="0">
				<tr>
				<td height="20" colspan="3" class="blueSubHeading"><netui:content value="${bundle.web['sessionList.selectTest.testSelect.title']}"/></td>
				</tr>
				<tr>
							<td height="10" colspan="3"></td>
						</tr>
				<tr>
					<td style="vertical-align: top;">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="46%" valign="top" class="divider" style="padding-right: 5px;">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td height="60" valign="top">
									<table width="100%" border="0">
										<tr class="transparent">
											<td width="22%" height="20" valign="top" class="transparent" align="left">Test Group :</td>
											<td width="78%" height="20" valign="middle" align="left"><select name="testGroupList" id="testGroupList"
												onchange="javascript:changeGradeAndLevel(); return false;">

										  </select></td>
										</tr>
										<tr class="transparent">
											<td height="20" valign="top" class="transparent" align="left">
											<div id="gradeDiv" style="display:none">Grade :</div>
											<div id="levelDiv" style="display:none">Level :</div>
										  </td>
											<td height="20" valign="middle" align="left">
											<select name="level" id="level" style="visibility:hidden;" onChange="javascript:changeSessionList(); return false;" ></select>
											
										  </td>
										</tr>
										
									</table>
								  </td>
								</tr>
								<tr>
									<td height="8" colspan="2"></td>
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
						  <td height="60" colspan="2" valign="top" width="895"><table width="100%" border="0">
                            <tr>
							<td height="20" colspan="4">Selected Test : <span id="testSessionName_lbl" class="lblDisabled">No Test selected</span></td>
						</tr>
						<tr>
							<td height="8" colspan="4"></td>
						</tr>
						<tr>
							<td width="0" height="20" align="left" valign="middle">
								<input type="checkbox" id="testBreak" value="1" disabled="disabled" onClick="toggleAccessCode();"/>							</td>
							<td width="294" height="20" valign="middle" class="transparent">Allow Test Breaks 
								<span style="padding-left:5px;">
						  <input name="aCode" type="text" id="aCode" style="visibility:hidden; padding-left:2px;" size="16" maxlength="32" /></span>							</td>
					      <td width="0" height="20" valign="middle"><input type="checkbox" id="randomDis" name="randomDis" value="1"></td>
						    <td width="200" height="20" valign="middle" class="transparent"><span id="randDisLbl">Random Distractor</span></td>
						</tr>
                          </table></td>
					  </tr>
						<tr>
							<td height="5" colspan="2"></td>
						</tr>
						<tr>
							<td height="10" colspan="2" valign="top">
							<div id="noSubtest">
							
								<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#A6C9E2">
								<tr>
								<td>
								<table width="100%" class="ts" cellpadding="0" cellspacing="1">
									<tr class="ui-corner-tl ui-corner-tr ui-widget-header subtestHeader">
										<th width="24" height="20" align="center" valign="middle"><strong>#</strong></th>
										<th width="419" height="20" align="left" valign="middle" style="padding-left:5px;"><strong>Subtest Name </strong></th>									
										<th width="82" height="20" align="center" valign="middle"><strong>Duration</strong></th>
									</tr>
									<!-- <tr>
										<td height="20" align="center" bgcolor="#FFFFFF">&nbsp;</td>
										<td height="20" align="center" bgcolor="#FFFFFF">&nbsp;</td>
										<td height="20" align="center" bgcolor="#FFFFFF">&nbsp;</td>
									</tr> -->
									<tr>
										
										<td height="60" align="center" bgcolor="#FFFFFF" colspan="3">No test selected</td>
										
									</tr>
									<!-- <tr>
										<td height="20" align="center" bgcolor="#FFFFFF">&nbsp;</td>
										<td height="20" align="center" bgcolor="#FFFFFF">&nbsp;</td>
										<td height="20" align="center" bgcolor="#FFFFFF">&nbsp;</td>
									</tr> -->
								</table>
								</td>
								</tr>
								</table>							
							</div>
							<div id="subtestGrid" style="display:none;"></div>							</td>
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