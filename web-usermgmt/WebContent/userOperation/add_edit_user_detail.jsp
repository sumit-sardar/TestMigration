<%@ page import="java.io.*, java.util.*"%>




<div id="addEditUserDetail"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	
	<div id="displayMessage" class="roundedMessage"> 
			<table>
				<tr>
					<td rowspan="3" valign="top">
                   	<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
					</td>
					<td>
						<table>
							<tr><td><font style="color: red; font-size:12px; font-weight:bold"><div id="title"></div></font></td></tr>
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
								<table class="transparent" width="350">
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
											<select id="timeZoneOptions"  name="timeZoneOptions" style="width: 202px;"></select>
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Role:</td>
										<td class="transparent">
											<select id="roleOptions"  name="roleOptions" style="width: 202px;"></select>
										</td>
						
									</tr>
									<!--ext_pin1 is added for DEX CR-->
									
									<tr class="transparent">
										<td nowrap="" width="110" class="transparent alignRight">External User Id:</td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="20" id="userExternalId" name="userExternalId"></td>
									</tr>
										
														<!--ext_pin1 is added for DEX CR-->
									
								   	<tr style="display: table-row;" id="message" class="transparent">
								   		<td width="110" class="transparent alignRight" style="vertical-align: top;"><span class="asterisk">*</span>&nbsp;Organization:</td>
										<td class="transparent-small" style="padding: 5px 5px 0 0">
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
																style="overflow:auto; height: 200px;  width: 350px; font-family: Arial, Verdana, Sans Serif; font-size: 13px; font-style: normal; font-weight: normal; position: absolute;">
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
												<select id="stateOptions"  name="stateOptions" style="width: 202px;"></select>	
											</td>
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
											- <input type="text" onkeypress="return constrainNumericChar(event);" style="width: 40px;" maxlength="4" id="faxNumber3" name="faxNumber3">
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
				<!--  <td  width="0%" id="preButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-left" id="pData" href="javascript:void(0)"><span
					class="ui-icon ui-icon-triangle-1-w"></span></a></td><td id="nextButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-right" id="nData"
					href="javascript:void(0)"><span class="ui-icon ui-icon-triangle-1-e"></span></a></td>
					<td>&nbsp;</td> -->
				<td  width="3%" id="preButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-left" id="pData" href="javascript:pDataClick('Edit');"><span
					class="ui-icon ui-icon-triangle-1-w"></span></a></td><td id="nextButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-right" id="nData"
					href="javascript:nDataClick('Edit');"><span class="ui-icon ui-icon-triangle-1-e"></span></a></td>
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
