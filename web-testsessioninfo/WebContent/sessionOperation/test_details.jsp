<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>

<table width="600" align="center" class="transparent">
	<tbody>
		<tr class="shadowborder">
			<td style="vertical-align: top;">
			<table width="100%" cellpadding="2" cellspacing="2" class="transparent">
				<tbody>
					<tr class="transparent">
						<td width="232" valign="top" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Test Session
						Name</td>
						<td width="22" align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td width="316" valign="top" class="transparent alignLeft"><input type="text" style="width: 200px;" tabindex="0"
							maxlength="32" id="testSessionName" name="testSessionName"></td>
					</tr>
					<tr class="transparent">
						<td width="232" valign="top" nowrap="" class="transparent alignRight">* Start Date</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td valign="top" class="transparent alignLeft"><input name="startDate" id="startDate" type="text" class="norBox" size="12" /></td>
					</tr>
					<tr class="transparent">
						<td width="232" valign="top" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;End Date</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td valign="top" class="transparent alignLeft"><input name="endDate" id="endDate" type="text" class="norBox" size="12" /></td>
					</tr>
					<tr class="transparent">
						<td width="232" height="25" valign="top" nowrap="" class="transparent alignRight"><span class="asterisk">*</span> Time Window</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
					    <td valign="top" class="transparent alignLeft"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="transparent">
                            <tr>
                              <td class="transparent alignCenter"><span>Drag the slider to get the time window - </span>
                                  <div id="slider-range" style="width: 200px;" class="transparent alignCenter"></div>
                                <span id="SlideMax"></span></td>
                            </tr>
                            <tr>
                              <td width="150" class="transparent alignCenter"><span id="time">9:00 AM - 5:00 PM</span></td>
                            </tr>
                        </table></td>
					</tr>
					<tr class="transparent">
						<td width="232" valign="top" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Time Zone</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
					    <td valign="top" class="transparent alignLeft"><select id="timeZoneList" name="timeZone">
                        </select></td>
					</tr>
					<tr class="transparent">
						<td width="232" valign="top" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Test Location</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td valign="top" class="transparent alignLeft"><input type="text" id="testLocation" name="testLocation"
							style="width: 202px;" /></td>
					</tr>

					<!--ext_pin1 is added for DEX CR-->
					<tr class="transparent">
						<td width="232" valign="top" class="transparent alignRight">*</span>&nbsp;Organization may view</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
					    <td valign="top" class="transparent alignLeft"><select id="select" name="select">
                        </select></td>
					</tr>
				</tbody>
			</table>
			</td>
		</tr>
	</tbody>
</table>