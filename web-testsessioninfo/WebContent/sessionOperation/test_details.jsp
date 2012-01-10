<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>
 <br>
 	<table>
			<tr>
				<td>
					<div style="clear:both;float:left;width:875px;padding: 0 5px 5px 0;">
						<div id="endTest" style="float:right;padding-left:5px;">
							<a href="#" id="endTestButton" onclick="endTestSession();" class="rounded {transparent} button"><lb:label key="sessionDetails.button.endTestSession" /></a>
						</div> 
					</div>
				</td>
			</tr>
	</table>
 <table width="900" height="360" align="left" class="shadowBorderFull">
	<tbody>
		<tr>
			<td align="center" style="vertical-align: top; padding:5px;">
			 <br>
			<table width="100%" cellpadding="0" cellspacing="2">
				<tbody>
					<tr class="transparent">
						<td width="171" valign="top" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="testDet.label.sessionName" /> </td>
					  <td colspan="2" valign="top" class="transparent alignLeft"><input type="text" style="width: 200px;" 
							id="testSessionName" name="testSessionName" maxlength="64" ></td>
					</tr>
					<tr class="transparent">
						<td width="171" valign="top" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="testDet.label.startDate" /></td>
					  <td colspan="2" valign="top" class="transparent alignLeft"><input name="startDate" id="startDate" type="text" size="12" maxlength="10" readonly="readonly"  /></td>
					</tr>
					<tr class="transparent">
						<td width="171" valign="top" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="testDet.label.endDate" /></td>
					  <td colspan="2" valign="top" class="transparent alignLeft"><input name="endDate" id="endDate" type="text" size="12" maxlength="10" readonly="readonly"/></td>
					</tr>
					<tr class="transparent">
						<td width="171" height="25" valign="middle" nowrap="" class="transparent alignRight"><span class="asterisk">*</span><lb:label key="testDet.label.timeWindow" /></td>
						<td colspan="2" valign="middle" class="transparent alignLeft"><table width="100%" border="0">
                            <tr>
                            <td>
                            	<table>
                            		<tr>
                            			<td valign="bottom" style="vertical-align: bottom;">
                            			<div style="width: 400px;" align="center">
                            				<span id="time">8:00 AM - 5:00 PM</span>
                            			</div>
                            			</td>
                            		</tr>
                            		<tr>
                            			<td width="548" align="left" valign="bottom">
                            				<table width="100%" border="0" cellpadding="0" cellspacing="0">
                                				<tr>
                                  					<td width="400"><div id="slider-range" style="width: 400px; vertical-align:top;"></div></td>
                                				</tr>
                                					<td>
                                						<table width = "100%">
                                							<tr>
                                								<td>
                                									12:00 AM
                                								</td>                                									
                                								<td align="center">
                                									11:45 PM&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                								</td>
                                							</tr>
                                						</table>
                                					</td>
                                				<tr>
                                				</tr>
                              				</table>
                              			</td>
                            		</tr>
                            	</table>
                            </td>
                            </tr>
                          </table></td>
				    </tr>
					<tr class="transparent">
						<td width="171" valign="top" class="transparent alignRight">&nbsp;<lb:label key="testDet.label.timeZone" /></td>
						<td colspan="2" valign="top" class="transparent alignLeft"><select id="timeZoneList" name="timeZoneList">
                        </select></td>
					</tr>
					<tr class="transparent">
						<td width="171" valign="top" nowrap="" class="transparent alignRight">&nbsp;<lb:label key="testDet.label.testLocation" /></td>
					  <td colspan="2" valign="top" class="transparent alignLeft"><input type="text" id="testLocation" name="testLocation" style="width: 202px;" maxlength="64" /></td>
					</tr>

					<!--ext_pin1 is added for DEX CR-->
					<tr class="transparent">
						<td width="171" valign="top" class="transparent alignRight">*</span>&nbsp;<lb:label key="testDet.label.orgMayView" /></td>
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