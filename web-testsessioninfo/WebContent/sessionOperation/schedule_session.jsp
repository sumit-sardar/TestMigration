<div id="scheduleSession"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>
		<div id="ssAccordion" style="width:99.5%;">
			<div>
				<h3><a href="#">Select Test</a></h3>
				<div id="Select_Test" style="background-color: #FFFFFF;">
					<table class="transparent" align="center">
						<tbody>
							<tr class="transparent">
								<td>
								<table class="transparent" width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td>
										<table width="100%" border="0" cellpadding="0" cellspacing="0">
											<tr>
											  <td width="46%" valign="top" class="divider" style="padding-right:5px;"><table width="100%" border="0">
                                                <tr>
                                                  <td>
												  <table width="100%" border="0" cellpadding="0" cellspacing="4">
                                                    <tr class="transparent">
                                                      <td class="transparent" width="16%">Test Group :</td>
                                                      <td width="84%">
                                                      <select name="testGroupList" class="bigBox" id="testGroupList" onchange="javascript:changeGradeAndLevel(); return false;">
                                                          
                                                      </select>
                                                      </td>
                                                    </tr>
                                                    <tr class="transparent">
                                                      <td class="transparent">
                                                      <div id="gradeDiv" style="display:none;">
                                                      	Grade :
                                                      </div>
													  <div id="levelDiv" style="display:none;">Level : </div></td>
                                                      <td>
                                                      	<div id="levelDropDown"> 
	                                                      	<select name="level" class="norBox" id="level"></select>
	                                                   	</div>
	                                                   </td>
                                                    </tr>
                                                    <!--tr class="transparent">
                                                      <td class="transparent">Level :</td>
                                                      <td><select name="level" id="level" class="norBox">
                                                      </select></td>
                                                    </tr-->
                                                  </table></td>
                                                </tr>
                                                <tr>												  
												  		<td height="10" colspan="2"></td>
				  								</tr>
                                                <tr>
                                                  <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                      <td><table width="100%" cellpadding="2" cellspacing="1" class="shadowBorder">
                                                        <tr class="subtestHeader">
                                                          <th width="22" height="20" align="center" class="transparent">#</th>
                                                          <th width="290" height="20" align="left" class="transparent">Test Session  Name</th>
                                                          <th width="64" height="20" class="transparent">Level</th>
                                                          <th width="64" height="20" class="transparent">Subtests</th>
                                                          <th width="76" height="20" align="center" class="transparent">Dur(mins)</th>
                                                        </tr>
                                                        <tr>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent"><input name="radiobutton" type="radio" value="radiobutton"></td>
                                                          <td height="20" bgcolor="#FFFFFF" class="transparent">Tabe 9 & 10 Online Complete Baterry</td>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent">5</td>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent">5</td>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent">190 </td>
                                                        </tr>
                                                        <tr>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent"><input name="radiobutton" type="radio" value="radiobutton"></td>
                                                          <td height="20" bgcolor="#FFFFFF" class="transparent">Las Links Form A L1 </td>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent">5</td>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent">5</td>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent">190 </td>
                                                        </tr>
                                                        <tr>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent"><input name="radiobutton" type="radio" value="radiobutton"></td>
                                                          <td height="20" bgcolor="#FFFFFF" class="transparent">Las Links Form A L1 </td>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent">5</td>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent">5</td>
                                                          <td height="20" align="center" bgcolor="#FFFFFF" class="transparent">190 </td>
                                                        </tr>
                                                      </table></td>
                                                    </tr>
                                                  </table></td>
                                                </tr>
                                              </table></td>										
   												<td width="1"></td>
												<td width="54%" style="padding-left:7px;">
												<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td height="20" colspan="3" style="border-bottom: #0099FF 1px groove;"><strong>Select Test Group from the left and	modify test details below</strong></td>
													</tr>
													<tr>												  
												  		<td height="10" colspan="2"></td>
					  								</tr>
													<tr>
														<td height="20" colspan="2">Selected Test : Las Links Form A - Level L</td>
													</tr>
													<tr>												  
												  		<td height="10" colspan="2"></td>
					  								</tr>
													<tr>
														<td height="20">
														<input type="checkbox" name="randomDis" value="1" class="norBox">														</td>
														<td class="transparent">Random Distractor</td>
													</tr>
													<tr>
														<td width="31" height="20" align="left" valign="middle">
														<input type="checkbox" id="testBreak" value="1"  onClick="toggleAccessCode();" class="norBox" checked="checked" />														</td>
														<td width="864" class="transparent">Allow Test Breaks <input name="textfield8" type="text"
															class="textbox" id="aCode" value="WORM121323" style="display: none;" /></td>
													</tr>
													<tr>
													  <td height="10" colspan="2"></td>
												  </tr>
													<tr>
													  <td height="10" colspan="2"><table width="100%" cellpadding="2" cellspacing="1" class="shadowBorder">
                                                        <tr class="subtestHeader">
                                                          <th width="24" height="20" align="center"> <strong>#</strong> </th>
                                                          <th width="287" height="20" align="left"><strong>Subtest Name </strong></th>
                                                          <th width="144" height="20"> <div align="center" id="aCodeH"><strong>Access Code </strong></div></th>
                                                          <th width="73" height="20" align="center"> <strong>Duration</strong> </th>
                                                          <th width="34" height="20">&nbsp;</th>
                                                        </tr>
                                                        <tr>
                                                          <td height="20" class="transparent"><div align="center" id="text11">1</div></td>
                                                          <td height="20" class="transparent"><div align="left" id="text12">Locator Test - Las Links Form A Level L</div></td>
                                                          <td height="20" class="transparent"><div align="center" id="aCodeB1">
                                                              <input name="textfield" type="text" class="textbox" id="text14" value="WORM121323" />
                                                          </div></td>
                                                          <td height="20" class="transparent"><div align="center" id="text13">10</div></td>
                                                          <td height="20" class="transparent"><div align="center"><img id="imgM1" src="images/minus.gif" width="14" title="Remove"
															onclick="toggleRows('0','1');" /> <img id="imgP1" src="images/icone_plus.gif" width="14" title="Add"
															onclick="toggleRows('1','1');" style="display: none;" /></div></td>
                                                        </tr>
                                                        <tr>
                                                          <td height="20"><div align="center" id="text21">2</div></td>
                                                          <td height="20"><div align="left" id="text22">Listening</div></td>
                                                          <td height="20"><div align="center" id="aCodeB2">
                                                              <input name="textfield2" type="text" class="textbox" id="text24" value="HANDFUL10" />
                                                          </div></td>
                                                          <td height="20"><div align="center" id="text23">15</div></td>
                                                          <td height="20"><div align="center"><img id="imgM2" src="images/minus.gif" width="14" title="Remove"
															onclick="toggleRows('0','2');" /> <img id="imgP2" src="images/icone_plus.gif" width="14" title="Add"
															onclick="toggleRows('1','2');" style="display: none;" /></div></td>
                                                        </tr>
                                                        <tr>
                                                          <td height="20" class="transparent"><div align="center" id="text11">3</div></td>
                                                          <td height="20" class="transparent"><div align="left" id="text12">Locator Test - Las Links Form A Level L</div></td>
                                                          <td height="20" class="transparent"><div align="center" id="aCodeB1">
                                                              <input name="textfield" type="text" class="textbox" id="text14" value="WORM121323" />
                                                          </div></td>
                                                          <td height="20" class="transparent"><div align="center" id="text13">10</div></td>
                                                          <td height="20" class="transparent"><div align="center"><img id="imgM1" src="images/minus.gif" width="14" title="Remove"
															onclick="toggleRows('0','1');" /> <img id="imgP1" src="images/icone_plus.gif" width="14" title="Add"
															onclick="toggleRows('1','1');" style="display: none;" /></div></td>
                                                        </tr>
                                                        <tr>
                                                          <td height="20"><div align="center" id="text21">4</div></td>
                                                          <td height="20"><div align="left" id="text22">Listening</div></td>
                                                          <td height="20"><div align="center" id="aCodeB2">
                                                              <input name="textfield2" type="text" class="textbox" id="text24" value="HANDFUL10" />
                                                          </div></td>
                                                          <td height="20"><div align="center" id="text23">15</div></td>
                                                          <td height="20"><div align="center"><img id="imgM2" src="images/minus.gif" width="14" title="Remove"
															onclick="toggleRows('0','2');" /> <img id="imgP2" src="images/icone_plus.gif" width="14" title="Add"
															onclick="toggleRows('1','2');" style="display: none;" /></div></td>
                                                        </tr>
                                                        <tr>
                                                          <td height="20" class="transparent"><div align="center" id="text11">5</div></td>
                                                          <td height="20" class="transparent"><div align="left" id="text12">Locator Test - Las Links Form A Level L</div></td>
                                                          <td height="20" class="transparent"><div align="center" id="aCodeB1">
                                                              <input name="textfield" type="text" class="textbox" id="text14" value="WORM121323" />
                                                          </div></td>
                                                          <td height="20" class="transparent"><div align="center" id="text13">10</div></td>
                                                          <td height="20" class="transparent"><div align="center"><img id="imgM1" src="images/minus.gif" width="14" title="Remove"
															onclick="toggleRows('0','1');" /> <img id="imgP1" src="images/icone_plus.gif" width="14" title="Add"
															onclick="toggleRows('1','1');" style="display: none;" /></div></td>
                                                        </tr>
                                                        <tr>
                                                          <td height="20"><div align="center" id="text21">6</div></td>
                                                          <td height="20"><div align="left" id="text22">Listening</div></td>
                                                          <td height="20"><div align="center" id="aCodeB2">
                                                              <input name="textfield2" type="text" class="textbox" id="text24" value="HANDFUL10" />
                                                          </div></td>
                                                          <td height="20"><div align="center" id="text23">15</div></td>
                                                          <td height="20"><div align="center"><img id="imgM2" src="images/minus.gif" width="14" title="Remove"
															onclick="toggleRows('0','2');" /> <img id="imgP2" src="images/icone_plus.gif" width="14" title="Add"
															onclick="toggleRows('1','2');" style="display: none;" /></div></td>
                                                        </tr>
                                                      </table></td>
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
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div>
				<h3><a href="#">Test Detail</a></h3>
				<div id="Test_Detail" style="overflow-y: scroll !important; overflow-x: hidden !important;">
					<div align="center">
						<jsp:include page="/sessionOperation/test_details.jsp" />
					</div>
				</div>
			</div>
			<div>
				<h3><a href="#">Add Student</a></h3>
				<div id="Add_Student" style="background-color: #FFFFFF; overflow-y: scroll !important; overflow-x: hidden !important;">
					<jsp:include page="/sessionOperation/add_student_tab.jsp" />
				</div>
			</div>
			<div>
				<h3><a href="#">Add Proctor</a></h3>
				<div id="Add_Proctor" style="overflow-y: scroll !important; overflow-x: hidden !important;">
					
				</div>
			</div>
			
			<div>
				<table cellspacing="0" cellpadding="0" border="0" id="TblGrid_list2_2" class="EditTable" width="100%">
					<tbody>
						<br>
						<tr id="Act_Buttons" align="center">
							<td  width="3%" id="preButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-left" id="pData" href="javascript:pDataClick('Edit');"><span
								class="ui-icon ui-icon-triangle-1-w"></span></a></td><td id="nextButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-right" id="nData"
								href="javascript:nDataClick('Edit');"><span class="ui-icon ui-icon-triangle-1-e"></span></a></td>
								<td>&nbsp;</td>
							<td  width="100%">
								<center>
								<input type="button"  id="sData" value="&nbsp;Save&nbsp;" onclick="" class="ui-widget-header">
								<input type="button"  id="cData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:closePopUp('scheduleSession'); return false;" class="ui-widget-header">
								</center>
								<br>
							</td>
						</tr>
						<tr class="binfo" style="display: none;">
							<td colspan="2" class="bottominfo"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
</div>