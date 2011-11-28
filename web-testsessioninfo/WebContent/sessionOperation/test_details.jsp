<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>
 <br>
 <table width="900" height="360" align="left" class="shadowBorderFull">
	<tbody>
		<tr>
			<td align="center" style="vertical-align: top; padding:5px;">
			 <br>
			<table width="100%" cellpadding="0" cellspacing="2">
				<tbody>
					<tr class="transparent">
						<td width="171" align="left" valign="top" nowrap="" class="transparent"><span class="asterisk">*</span>&nbsp;Test Session
						Name</td>
						<td width="14" align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td colspan="2" valign="top" class="transparent alignLeft"><input type="text" style="width: 200px;" 
							id="testSessionName" name="testSessionName" ></td>
					</tr>
					<tr class="transparent">
						<td width="171" align="left" valign="top" nowrap="" class="transparent">* Start Date</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td colspan="2" valign="top" class="transparent alignLeft"><input name="startDate" id="startDate" type="text" size="12" maxlength="10"  /></td>
					</tr>
					<tr class="transparent">
						<td width="171" align="left" valign="top" nowrap="" class="transparent"><span class="asterisk">*</span>&nbsp;End Date</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td colspan="2" valign="top" class="transparent alignLeft"><input name="endDate" id="endDate" type="text" size="12" maxlength="10" /></td>
					</tr>
					<tr class="transparent">
						<td width="171" height="25" align="left" valign="middle" nowrap="" class="transparent"><span class="asterisk">*</span>Time Window</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
					    <td colspan="2" valign="middle" class="transparent alignLeft"><table width="100%" border="0">
                            <tr>
                              <td width="128" align="left"><span id="time">9:00 AM - 5:00 PM</span></td>
                              <td width="548" align="left"><table width="320" border="0" cellpadding="0" cellspacing="0" class="transparent">
                                <tr>
                                  <td width="50" height="5" align="right" class="transparent alignLeft"><span class="style3">12:00 AM</span> </td>
                                  <td width="200" class="transparent alignLeft"><div id="slider-range" style="width: 200px; vertical-align:bottom;"></div></td>
                                  <td width="50" class="transparent alignLeft"><span class="style3">11:45 PM</span></td>
                                </tr>
                              </table></td>
                            </tr>
                          </table></td>
				    </tr>
					<tr class="transparent">
						<td width="171" align="left" valign="top" class="transparent">&nbsp;Time Zone</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
					    <td colspan="2" valign="top" class="transparent alignLeft"><select id="timeZoneList" name="timeZoneList">
                        </select></td>
					</tr>
					<tr class="transparent">
						<td width="171" align="left" valign="top" nowrap="" class="transparent">&nbsp;Test Location</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td colspan="2" valign="top" class="transparent alignLeft"><input type="text" id="testLocation" name="testLocation" style="width: 202px;" maxlength="64" /></td>
					</tr>

					<!--ext_pin1 is added for DEX CR-->
					<tr class="transparent">
						<td width="171" align="left" valign="top" class="transparent">*</span>&nbsp;Organization may view</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
					    <td colspan="2" valign="top" class="transparent alignLeft"><select id="topOrgNode" name="topOrgNode">
                        </select></td>
					</tr>
					
					<!--tr>
					<td valign="top">&nbsp;</td>
					<td align="center">&nbsp;</td>
					<td width="110" valign="top">&nbsp;</td>
					<td width="548" valign="top"><table width="320" border="0">
                      <tr>
                        <td align="left" class="transparent">(Drag the slider to set the time)</td>
                      </tr>
                    </table></td>
					</tr-->
				</tbody>
			</table>
		  </td>
		</tr>
	</tbody>
</table>