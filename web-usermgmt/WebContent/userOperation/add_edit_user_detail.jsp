<%@ page import="java.io.*, java.util.*"%>




<div id="addEditUserDetail"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	
	<div id="displayMessage" style="display:none; width:99.5%; height:55px; background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold; border: 1px solid #A6C9E2;">
			<table>
				<tr>
					<td rowspan="3"><div id= "messageType">	</div></td>
					<td>
						<table>
							<tr><td><div id="title"></div></td></tr>
							<tr><td><div id= "content">	</div></td></tr>
							<tr><td><div id= "message">	</div></td></tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<br>
	<div id="accordion" style="width:99.5%;">
			
			<div>
				<h3><a href="#">User Information</a></h3>
				
					<div id="User_Information" style="background-color: #FFFFFF;">
	
						<table class="transparent">
							<tbody>
								<tr class="transparent">
								<td style ="vertical-align: top;">
								<table class="transparent">
								<tbody>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;First Name:</td>
										<td class="transparent"><input type="text" style="width: 200px;" tabindex="0" maxlength="32" id="userFirstName" name="userFirstName">
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight">Middle Name:</td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="userMiddleName" name="userMiddleName" ></td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Last Name:</td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="userLastName" name="userLastName"></td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight">Email:</td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="64" id="userEmail" name="userEmail"></td>
									</tr>

									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Time Zone:</td>
										<td class="transparent">
											<select style="width: 200px;"   id="timeZone" name="timeZone">	
											<option style="width: 200px;" selected="" value="">Select a time zone</option>
											<option style="width: 200px;" value="America/Los_Angeles">Pacific</option>
											<option style="width: 200px;" value="America/Denver">Mountain</option>
											<option style="width: 200px;" value="America/Phoenix">Phoenix</option>
											<option style="width: 200px;" value="America/Chicago">Central</option>
											<option style="width: 200px;" value="America/Indianapolis">Indianapolis</option>
											<option style="width: 200px;" value="America/New_York">Eastern</option>
											<option style="width: 200px;" value="GMT">Greenwich Mean</option>
											<option style="width: 200px;" value="Pacific/Honolulu">Hawaii</option>
											<option style="width: 200px;" value="America/Adak">Aleutian</option>
											<option style="width: 200px;" value="America/Anchorage">Alaska</option>	
											</select> 
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Role:</td>
										<td class="transparent">
											<select style="width: 200px;"  id="roleId" name="roleId">	
										 	<option style="width: 200px;" selected="" value="">Select a role</option>
											<option style="width: 200px;" value="1005">Administrator</option>
											<option style="width: 200px;" value="1016">Administrative Coordinator</option>
											<option style="width: 200px;" value="1006">Coordinator</option>
											<option style="width: 200px;" value="1009">Proctor</option>
										</select></td>
						
									</tr>
									<!--ext_pin1 is added for DEX CR-->
									
								   	<tr style="display: table-row;" id="message" class="transparent">
								   		<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Organization:</td>
										<td class="transparent-small">
											<div id="notSelectedOrgNodes" style="width:200px; visibility:visible; padding-left: 4px"><font color="gray">None selected. Use the control on the right to
											select.</font>
											</div>
											<div id="selectedOrgNodesName" style="width:200px, padding-left: 4px"></div>
										
										</td>
									</tr>
								</tbody>
								</table>
								</td>
								<td style ="vertical-align: top;">
								<table class="transparent">
								<tbody>
									
									<tr class="transparent" >
										<td class="transparent-top">

										<table class="transparent" id="orgTable">
											<tbody>
											<tr>
											<td>
											<div   style=" background:#D4ECFF; height:25px; color:#4297D7; width: 350px; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold; vertical-align:middle;">&nbsp;Select OrgNode</div>
											</td>
											</tr>
												<tr>
													<td >
														<div  id="innerID" 
																style="overflow:auto; height: 235px;  width: 350px; font-family: Arial, Verdana, Sans Serif; font-size: 13px; font-style: normal; font-weight: normal; position: absolute;">
														</div>
													
													</td>
												</tr>
											</tbody>
										</table>

										</td>
									</tr>

								</tbody>
								</table>
								</td>
								</tr>
							</tbody>
						</table>


					</div>
				
			</div>
			<div>
				<h3><a href="#">Contact Information</a></h3>
				<div id="Contact_Information" style="overflow-y: scroll !important; overflow-x: hidden !important;"><!-- changes for defect #66994 -->
					<table class="transparent">
						<tbody>
						 <tr class="transparent">
							 <td style ="vertical-align: top;">
							 	<table>
							 		<tbody>
							 			<tr class="transparent">
											<td nowrap="" width="110" class="transparent alignRight">Address Line 1:</td>
											<td class="transparent"><input type="text" style="width: 200px;" maxlength="64" id="addressLine1" name="addressLine1"></td>
										</tr>
										<tr class="transparent">
											<td nowrap="" width="110" class="transparent alignRight">Address Line 2:</td>
											<td class="transparent"><input type="text" style="width: 200px;" maxlength="64" id="addressLine2" name="addressLine2"></td>
										</tr>
										<tr class="transparent">
											<td nowrap="" width="110" class="transparent alignRight">City:</td>
											<td class="transparent"><input type="text" style="width: 200px;" maxlength="64" id="city" name="city"></td>
										</tr>
										<tr class="transparent">
											<td width="110" class="transparent alignRight">State:</td>
											<td class="transparent">
												<select style="width: 200px;"  id="state" name="state"> 
												<option style="width: 200px;" selected="" value="">Select a state</option>
												<option style="width: 200px;" value="AL">Alabama</option>
												<option style="width: 200px;" value="AK">Alaska</option>
												<option style="width: 200px;" value="AZ">Arizona</option>
												<option style="width: 200px;" value="AR">Arkansas</option>
												<option style="width: 200px;" value="CA">California</option>
												<option style="width: 200px;" value="CO">Colorado</option>
												<option style="width: 200px;" value="CT">Connecticut</option>
												<option style="width: 200px;" value="DE">Delaware</option>
												<option style="width: 200px;" value="DC">District of Columbia</option>
												<option style="width: 200px;" value="FL">Florida</option>
												<option style="width: 200px;" value="GA">Georgia</option>
												<option style="width: 200px;" value="HI">Hawaii</option>
												<option style="width: 200px;" value="ID">Idaho</option>
												<option style="width: 200px;" value="IL">Illinois</option>
												<option style="width: 200px;" value="IN">Indiana</option>
												<option style="width: 200px;" value="IA">Iowa</option>
												<option style="width: 200px;" value="KS">Kansas</option>
												<option style="width: 200px;" value="KY">Kentucky</option>
												<option style="width: 200px;" value="LA">Louisiana</option>
												<option style="width: 200px;" value="ME">Maine</option>
												<option style="width: 200px;" value="MD">Maryland</option>
												<option style="width: 200px;" value="MA">Massachusetts</option>
												<option style="width: 200px;" value="MI">Michigan</option>
												<option style="width: 200px;" value="MN">Minnesota</option>
												<option style="width: 200px;" value="MS">Mississippi</option>
												<option style="width: 200px;" value="MO">Missouri</option>
												<option style="width: 200px;" value="MT">Montana</option>
												<option style="width: 200px;" value="NE">Nebraska</option>
												<option style="width: 200px;" value="NV">Nevada</option>
												<option style="width: 200px;" value="NH">New Hampshire</option>
												<option style="width: 200px;" value="NJ">New Jersey</option>
												<option style="width: 200px;" value="NM">New Mexico</option>
												<option style="width: 200px;" value="NY">New York</option>
												<option style="width: 200px;" value="NC">North Carolina</option>
												<option style="width: 200px;" value="ND">North Dakota</option>
												<option style="width: 200px;" value="OH">Ohio</option>
												<option style="width: 200px;" value="OK">Oklahoma</option>
												<option style="width: 200px;" value="OR">Oregon</option>
												<option style="width: 200px;" value="PA">Pennsylvania</option>
												<option style="width: 200px;" value="RI">Rhode Island</option>
												<option style="width: 200px;" value="SC">South Carolina</option>
												<option style="width: 200px;" value="SD">South Dakota</option>
												<option style="width: 200px;" value="TN">Tennessee</option>
												<option style="width: 200px;" value="TX">Texas</option>
												<option style="width: 200px;" value="UT">Utah</option>
												<option style="width: 200px;" value="VT">Vermont</option>
												<option style="width: 200px;" value="VA">Virginia</option>
												<option style="width: 200px;" value="WA">Washington</option>
												<option style="width: 200px;" value="WV">West Virginia</option>
												<option style="width: 200px;" value="WI">Wisconsin</option>
												<option style="width: 200px;" value="WY">Wyoming</option>
												<option style="width: 200px;" value="AS">American Samoa</option>
												<option style="width: 200px;" value="FM">F.S. of Micronesia</option>
												<option style="width: 200px;" value="GU">Guam</option>
												<option style="width: 200px;" value="MH">Marshall Islands</option>
												<option style="width: 200px;" value="MP">North Mariana Islands</option>
												<option style="width: 200px;" value="PW">Palau</option>
												<option style="width: 200px;" value="PR">Puerto Rico</option>
												<option style="width: 200px;" value="VI">Virgin Islands</option>
											</select></td>
										</tr>
										<tr class="transparent">
											<td width="110" class="transparent alignRight">Zip:</td>
											<td class="transparent">
												<input type="text" onkeypress="return constrainNumericChar(event);"	 style="width: 50px;" maxlength="5" id="zipCode1" name="zipCode1">
												 - <input type="text" onkeypress="return constrainNumericChar(event);" style="width: 50px;" maxlength="5" id="zipCode2" name="zipCode2">
											</td>
										</tr>
							 		</tbody>
							 	</table>
							 </td>
							 
							  <td style ="vertical-align: top;">
							  	<table>
							  	<tbody>
							  		<tr class="transparent">
										<td width="110" class="transparent alignRight">Primary Phone:</td>
										<td class="transparent">
											<input type="text" onkeypress="return constrainNumericChar(event);"	  style="width: 40px;" maxlength="3" id="primaryPhone1" name="primaryPhone1">
											- <input type="text" onkeypress="return constrainNumericChar(event);"   style="width: 40px;" maxlength="3" id="primaryPhone2" name="primaryPhone2">
											- <input type="text" onkeypress="return constrainNumericChar(event);"   style="width: 40px;" maxlength="4" id="primaryPhone3" name="primaryPhone3"> 
											Ext: <input	type="text" onkeypress="return constrainNumericChar(event);" style="width: 40px;" maxlength="4" id="primaryPhone4" name="primaryPhone4">
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight">Secondary Phone:</td>
										<td class="transparent">
											<input type="text" onkeypress="return constrainNumericChar(event);"	  style="width: 40px;" maxlength="3" id="secondaryPhone1" name="secondaryPhone1">
											- <input type="text" onkeypress="return constrainNumericChar(event);"   style="width: 40px;" maxlength="3" id="secondaryPhone2" name="secondaryPhone2">
											- <input type="text" onkeypress="return constrainNumericChar(event);"   style="width: 40px;" maxlength="4" id="secondaryPhone3" name="secondaryPhone3">
											Ext: <input	type="text" onkeypress="return constrainNumericChar(event);" style="width: 40px;" maxlength="4"	id="secondaryPhone4" name="secondaryPhone4">
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight">Fax Number:</td>
										<td class="transparent">
											<input type="text" onkeypress="return constrainNumericChar(event);"	 style="width: 40px;" maxlength="3" id="faxNumber1" name="faxNumber1">
											- <input type="text" onkeypress="return constrainNumericChar(event);"   style="width: 40px;" maxlength="3" id="faxNumber2" name="faxNumber2">
											- <input type="text" onkeypress="return constrainNumericChar(event);" style="width: 40px;" maxlength="4" id="faxNumber3" iname="faxNumber3">
										</td>
									</tr>
							  	</tbody>
							  	</table>
							 </td>
							 </tr>
						</tbody>
					</table>
				</div>
			</div>
			
			
			<div>
	<table cellspacing="0" cellpadding="0" border="0" id="TblGrid_list2_2" class="EditTable" width="100%">
		<tbody>
			<br>
			<tr id="Act_Buttons" align="center">
				<td  width="0%" id="preButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-left" id="pData" href="javascript:void(0)"><span
					class="ui-icon ui-icon-triangle-1-w"></span></a></td><td id="nextButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-right" id="nData"
					href="javascript:void(0)"><span class="ui-icon ui-icon-triangle-1-e"></span></a></td>
					<td>&nbsp;</td>
				<td  width="100%">
					<center>
					<input type="button"  id="sData" value="&nbsp;Save&nbsp;" onclick="javascript:userDetailSubmit(); return false;" class="ui-widget-header">
					<input type="button"  id="cData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:onCancel(); return false;" class="ui-widget-header">
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
