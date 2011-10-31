<%@ page import="java.io.*, java.util.*"%>

<%
Boolean isLasLinkCustomer = (Boolean) request.getAttribute("isLasLinkCustomer");
%>

<input type="hidden" id="isLasLinkCustomer"  value = '<%=isLasLinkCustomer %>' />



<div id="addEditOrganizationDetail"
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
	
	<span style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px;"><b>Enter information about the organization in the form below. Required fields are marked by a blue asterisk *. 
Use the organization selector on the right to select the "Parent" organization to which you are adding this new member organization.</b></span>
<br>
<br>

	<div id="accordion" style="width:99.5%;">
			
			<div>
				<h3><a href="#">Organization Information</a></h3>
				
					<div id="Organization_Information" style="background-color: #FFFFFF;">
	
						<table class="transparent">
							<tbody>
								<tr class="transparent">
								<td style ="vertical-align: top;">
								<table class="transparent" width="350">
								<tbody>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Name:</td>
										<td class="transparent"><input type="text" style="width: 200px;" tabindex="0" maxlength="32" id="orgName" name="orgName">
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight">Org Code:</td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="orgCode" name="orgCode" ></td>
									</tr>
									<%if(isLasLinkCustomer) { %>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;MDR Number:</td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="mdrNumber" name="mdrNumber"></td>
									</tr>
									<%} %>
									

									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Layer:</td>
										<td class="transparent">
											<select id="layerOptions"  name="layerOptions" style="width: 202px;">
												<option value = "Select a layer">Select a layer</option>
											</select>
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Parent Org:</td>
										<td class="transparent">
											<div id="parentOrgName" name="parentOrgName">
												<font color="gray">None selected. Use the control on the right to select.</font>
											</div>
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


	</div>




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
					<input type="button"  id="sData" value="&nbsp;Save&nbsp;" onclick="javascript:orgDetailSubmit(); return false;" class="ui-widget-header">
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