<%@ page import="java.io.*, java.util.*"%>
<%
    Boolean isLasLinkCustomer = (Boolean) request.getAttribute("isLasLinkCustomer");
	Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); 
	String []studentIdArrValue = (String[])request.getAttribute("studentIdArrValue");
	
	

%>
<input type="hidden" id="isLasLinkCustomer"  value = '<%=isLasLinkCustomer %>' />
<input type="hidden" id="isStudentIdConfigurable" value = '<%=isStudentIdConfigurable %>' />





<div id="viewStudentDetail"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	
	
		<br>
	<div id="viewaccordion" style="width:99.5%;">
			
			<div>
				<h3><a href="#">Student Information</a></h3>
				
					<div id="view_Student_Information" style="background-color: #FFFFFF;">
	
						<table class="transparent" style="width:100%">
							<tbody>
								<tr class="transparent">
								<td style ="vertical-align: top; width:50%">
								<table class="transparent" >
								<tbody>
									<tr class="transparent">
										<td width="110" nowrap="" class="transparent alignRight">First Name:</td>
										<td class="transparent"><label style="width: 200px;" tabindex="0" maxlength="32" id="studentFirstNameView" name="studentFirstNameView">
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" nowrap="" class="transparent alignRight">Middle Name:</td>
										<td class="transparent"><label style="width: 200px;" maxlength="32" id="studentMiddleNameView" name="studentMiddleNameView"></td>
									</tr>
									<tr class="transparent">
										<td width="110" nowrap="" class="transparent alignRight">Last Name:</td>
										<td class="transparent"><label style="width: 200px;" maxlength="32" id="studentLastNameView" name="studentLastNameView"></td>
									</tr>
									<tr class="transparent">
										<td width="110" nowrap="" class="transparent alignRight">Login ID:</td>
										<td class="transparent"><label style="width: 200px;" maxlength="32" id="studentUserNameView" name="studentUserNameView"></td>
									</tr>
									<tr class="transparent">
										<td width="110" nowrap="" class="transparent alignRight">Date of  Birth:</td>
										<td nowrap="" class="transparent">    
											<label style="width: 200px;" maxlength="32" id="birthdateStringView" name="birthdateStringView">
										</td>                    
						   
									</tr>
								</tbody>
								</table>
								</td>
								<td style ="vertical-align: top; width:50%">
								<table class="transparent" >
								<tbody>
									
									    <tr class="transparent">
									        <td width="110" nowrap="" class="transparent alignRight">Grade:</td>
											<td class="transparent"><label style="width: 200px;" maxlength="32" id="studentgradeView" name="studentgradeView"></td>
									    </tr>
									    <tr class="transparent">
									    	<td width="110" nowrap="" class="transparent alignRight">Gender:</td>
											<td class="transparent"><label style="width: 200px;" maxlength="32" id="studentgenderView" name="studentgenderView"></td>
									    </tr>
									    <tr class="transparent">
									        <td width="110"  class="transparent alignRight">
									        
									         <%if(isStudentIdConfigurable) {%> 
												 <%=studentIdArrValue[0] %>	:
											 <%} else {%> 
												Student ID:	
											 <%} %>
									        <td class="transparent"><label style="width: 200px;" maxlength="32" id="studentNumberView" name="studentNumberView"></td>
									    </tr>
									    
									    <%if(isLasLinkCustomer) { %>
									    <tr class="transparent">
									    	<td width="110" nowrap="" class="transparent alignRight">Purpose of Test:</td>
											<td class="transparent"><label style="width: 200px;" maxlength="32" id="studenttestPurposeView" name="studenttestPurposeView"></td>
									    </tr>
									    <%} %>
									    
									    <tr class="transparent">
									        <td width="110" nowrap="" class="transparent-top alignRight">Organization:</td>
									        <td class="transparent-top">
									            <label style="width: 200px;" maxlength="32" id="orgNodeNameView" name="orgNodeNameView">
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
				<h3><a href="#">Student Additional Information</a></h3>
				<div id="view_Student_Additional_Information" style="overflow-y: scroll !important; overflow-x: hidden !important;"><!-- changes for defect #66994 -->
					<jsp:include page="/studentOperation/view_student_by_demographic.jsp" />				
				</div>
			</div>
			<div>
				<h3><a href="#">Specific Accommodations</a></h3>
				<div id="view_Student_Accommodation_Information" style="overflow-y: scroll !important; overflow-x: hidden !important;"><!-- changes for defect #66994 -->
					<jsp:include page="/studentOperation/view_student_by_accommodation.jsp" />
				</div>
			</div>
			
			<div>
	<table cellspacing="0" cellpadding="0" border="0" id="TblGrid_list2_2" class="EditTable" width="100%">
		<tbody>
			<br>
			<tr id="Act_Buttons" align="center">
				<td  width="3%" id="viewPreButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-left" id="viewpData" href="javascript:pDataClick('View');"><span
					class="ui-icon ui-icon-triangle-1-w"></span></a></td><td id="viewNextButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-right" id="viewnData"
					href="javascript:nDataClick('View');"><span class="ui-icon ui-icon-triangle-1-e"></span></a></td>
					<td>&nbsp;</td>
				<td  width="100%">
					<center>
					<input type="button"  id="cData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:closePopUp('viewStudentDetail'); return false;" class="ui-widget-header">
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
