<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>
 <br>
 <table width="600" height="360" align="center" class="shadowBorderFull">
	<tbody>
		<tr>
			<td align="center" style="vertical-align: top; padding:5px;">
			 <br>
			<table width="100%" cellpadding="2" cellspacing="2">
				<tbody>
					<tr class="transparent">
						<td width="232" valign="top" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Test Session
						Name</td>
						<td width="22" align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td width="316" valign="top" class="transparent alignLeft"><input type="text" style="width: 200px;" 
							id="testSessionName" name="testSessionName" ></td>
					</tr>
					<tr class="transparent">
						<td width="232" valign="top" nowrap="" class="transparent alignRight">* Start Date</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td valign="top" class="transparent alignLeft"><input name="startDate" id="startDate" type="text" size="12" maxlength="10"  /></td>
					</tr>
					<tr class="transparent">
						<td width="232" valign="top" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;End Date</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td valign="top" class="transparent alignLeft"><input name="endDate" id="endDate" type="text" size="12" maxlength="10" /></td>
					</tr>
					<tr class="transparent">
						<td width="232" height="25" valign="top" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>Time Window</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
					    <td valign="top" class="transparent alignLeft"><span id="time">9:00 AM - 5:00 PM</span></td>
					</tr>
					<tr class="transparent">
                      <td height="25" valign="top" nowrap="" class="transparent alignRight">&nbsp;</td>
					  <td align="center" valign="middle" class="transparent alignLeft">&nbsp;</td>
					  <td valign="top" class="transparent alignLeft"><table width="100%" border="0" cellpadding="0" cellspacing="2" class="transparent">
                          <tr>
                            <td height="20" class="transparent alignLeft"><span>Drag the slider to get the time window - </span> </td>
                          </tr>
                          <tr>
                            <td height="5" class="transparent alignLeft"><div id="slider-range" style="width: 200px; vertical-align:middle;"></div></td>
                          </tr>
                      </table></td>
				  </tr>
					<tr class="transparent">
						<td width="232" valign="top" class="transparent alignRight">Time Zone</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
					    <td valign="top" class="transparent alignLeft"><select id="timeZoneList" name="timeZoneList">
                        </select></td>
					</tr>
					<tr class="transparent">
						<td width="232" valign="top" nowrap="" class="transparent alignRight">Test Location</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
				      <td valign="top" class="transparent alignLeft"><input type="text" id="testLocation" name="testLocation" style="width: 202px;" /></td>
					</tr>

					<!--ext_pin1 is added for DEX CR-->
					<tr class="transparent">
						<td width="232" valign="top" class="transparent alignRight">*</span>&nbsp;Organization may view</td>
						<td align="center" valign="middle" class="transparent alignLeft">:</td>
					    <td valign="top" class="transparent alignLeft"><select id="topOrgNode" name="topOrgNode">
                        </select></td>
					</tr>
				</tbody>
			</table>
		  </td>
		</tr>
	</tbody>
</table>