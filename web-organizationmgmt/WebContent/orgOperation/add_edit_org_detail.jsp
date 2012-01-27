<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="organizationApplicationResource" />
<%
Boolean isLasLinkCustomer = (Boolean) request.getAttribute("isLasLinkCustomer");
%>

<input type="hidden" id="isLasLinkCustomer"  value = '<%=isLasLinkCustomer %>' />



<div id="addEditOrganizationDetail"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	
	<div id="displayMessage" class="roundedMessage"> 
			<table>
				<tr>
					<td rowspan="3" valign="top" width="18">
                   	<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
					</td>
					<td valign="top">
						<table>
							<tr><td><font style="color: red; font-size:12px; font-weight:bold"><div id="title"></div></font></td></tr>
							<tr><td><div id= "content">	</div></td></tr>
							<tr><td><div id= "message">	</div></td></tr>
						</table>
					</td>
				</tr>
			</table>
	</div>

	<table style="padding-bottom: 5px;width:99.5%;">
		<tr>
			<td width="770px">
				<div id="editOrgDisplayId">
					<span style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px;line-height:17px;">
						<b><lb:label key="org.label.AddEdit" /></b>
					</span>
				</div>
			</td>
		</tr>
	</table>

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
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="org.name" suffix=":"/></td>
										<td class="transparent"><input type="text" style="width: 200px;" tabindex="0" maxlength="50" id="orgName" name="orgName">
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><lb:label key="org.Code" suffix=":"/></td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="orgCode" name="orgCode" ></td>
									</tr>
									<%if(isLasLinkCustomer) { %>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="org.mdrNumber" suffix=":"/></td>
										<td class="transparent"><input type="text" style="width: 200px;" id="mdrNumber" name="mdrNumber" maxlength="8" onKeyPress="return constrainNumericChar(event);"></td>
									</tr>
									<%} %>
									

									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="org.layer" suffix=":"/></td>
										<td class="transparent">
											<select id="layerOptions"  name="layerOptions" style="width: 202px;">
												<option value = "Select a layer">Select a layer</option>
											</select>
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="org.parentOrg" suffix=":"/></td>
										<td class="transparent">
											<div id="parentOrgName" name="parentOrgName">
												<font color="gray"><lb:label key="org.msg.noneSelected" /></font>
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
											<div style="width: 347px;visibility: visible;" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="org.label.selOrg" /></div>
											</td>
											</tr>
												<tr>
													<td >
														<div  id="innertreebgdiv" class="innertreeCtrl treeCtrl">
															<div id="innerID" style="width:auto;height:auto;display:table">
															</div>
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
					<td style="width:1%;">&nbsp;</td>
				<td  style="width:96%; text-align: left;">
					<input type="button"  id="sData" value="&nbsp;Save&nbsp;" onclick="javascript:orgDetailSubmit(); return false;" class="ui-widget-header" style="width:60px; margin-right: 10px; margin-left: 250px">
					<input type="button"  id="cData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:onCancel(); return false;" class="ui-widget-header" style="width:60px;">
					<br>
				</td>
			</tr>
			<tr class="binfo" style="display: none;">
				<td colspan="2" class="bottominfo"></td>
			</tr>
		</tbody>
	</table>

</div>