<%@ page import="java.io.*, java.util.*"%>
<%
    Boolean isMandatoryBirthDate = (Boolean)request.getAttribute("isMandatoryBirthDate"); //GACRCT2010CR007 - Disable Mandatory Birth Date 
	Boolean isLasLinkCustomer = (Boolean) request.getAttribute("isLasLinkCustomer");
	Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); 
	Boolean isStudentId2Configurable = (Boolean)request.getAttribute("isStudentId2Configurable"); 
	String []studentIdArrValue = (String[])request.getAttribute("studentIdArrValue");
	String []studentId2ArrValue = (String[])request.getAttribute("studentId2ArrValue");
	boolean isMandatoryStudentId = false;
	if(studentIdArrValue != null && studentIdArrValue[2] != null && studentIdArrValue[2]!= "") {
		if (studentIdArrValue[2].equalsIgnoreCase("T")) {
			isMandatoryStudentId = true;
		}
	}

%>

<div id="addEditStudentDetail"
	style="display: none; background-color: #FFFFCC; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div id="displayMessage" style="display:none; width:100%; height:25px; background-color: #CCCC99; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold;">
	</div>
<ul id="accordion">

	<li>
	<div
		style=" width:170px; background-color: #CCCC99; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">Student Information</div>
	<div>
	
<table class="transparent">
	<tbody>
		<tr class="transparent">
		<td style ="vertical-align: top;">
		<table class="transparent">
		<tbody>
			<tr class="transparent">
				<td width="110" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;First Name:</td>
				<td class="transparent"><input type="text" style="width: 200px;" tabindex="0" maxlength="32" id="studentFirstName" name="studentFirstName">
				</td>
			</tr>
			<tr class="transparent">
				<td width="110" nowrap="" class="transparent alignRight">Middle Name:</td>
				<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="studentMiddleName" name="studentMiddleName"></td>
			</tr>
			<tr class="transparent">
				<td width="110" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Last Name:</td>
				<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="studentLastName" name="studentLastName"></td>
			</tr>
			<tr class="transparent">
				<td width="110" nowrap="" class="transparent alignRight">
				<%if(isMandatoryBirthDate) { %>
					<span class="asterisk">*</span>
				<% }%>
				Date of  Birth:</td>
				<td nowrap="" class="transparent">    
					<select style="width: 60px;"   id="monthOptions" name="monthOptions">		
					</select>
					        
					<select style="width: 45px;"  id="dayOptions" name="dayOptions">
					</select>
					
					<select style="width: 68px;"  id="yearOptions" name="yearOptions">
					</select>
		        </td>                    
   
			</tr>
			<tr class="transparent">
				<td width="110" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Gender:</td>
				<td class="transparent">
				 <select id="genderOptions"  name="genderOptions" style="width: 200px;">
	    		 </select> 
				</td>

			</tr>
		</tbody>
		</table>
		</td>
		<td>
		<table class="transparent">
		<tbody>
			<tr class="transparent">
				<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;Grade:</td>
				<td class="transparent">  
					<select id="gradeOptions" name="gradeOptions" style="width: 200px;">
					</select> 
				</td>
			</tr>
			
			<!--ext_pin1 is added for DEX CR-->
			<tr class="transparent">
				<td nowrap="" width="110" class="transparent alignRight">	
				 <%if(isMandatoryStudentId) { %>
        			<span class="asterisk">*</span>&nbsp;
       			  <%} %>	
		         <%if(isStudentIdConfigurable) {%> 
		        	 <%=studentIdArrValue[0] %>	:
		         <%} else {%> 
		       		Student ID:	
		      	 <%} %>
		      	 </td>
		        
				</td>
				<td class="transparent"><input type="text" style="width: 200px;" maxlength="<%= isStudentIdConfigurable ? new Integer(studentIdArrValue[1]).intValue()>0 && new Integer(studentIdArrValue[1]).intValue()<32 ? new Integer(studentIdArrValue[1]).intValue() : 32 : 32 %>" id="studentExternalId" name="studentExternalId"></td>
			</tr>
			<tr class="transparent">
				<td nowrap="" width="110" class="transparent alignRight">
				<%if(isStudentId2Configurable) {%> 
		        	<%=studentId2ArrValue[0] %>	: 
		        <%} else {%> 
         		Student Id 2:
         		<%} %>
         		</td>
				<td class="transparent"><input type="text" style="width: 200px;" maxlength="<%=isStudentId2Configurable ? new Integer(studentId2ArrValue[1]).intValue()>0 && new Integer(studentId2ArrValue[1]).intValue()<32 ? new Integer(studentId2ArrValue[1]).intValue() : 32 : 32 %>" id="studentExternalId2" name="studentExternalId2"></td>
			</tr>
			<%if(isLasLinkCustomer) { %>
		     <tr class="transparent">
		     	<td nowrap="" width="110" class="transparent alignRight">Purpose of Test:</td>
		     	<td class="transparent"><input type="text" style="width: 200px;"  id="testPurpose" name="testPurpose"></td>
		                               
		    </tr>
		   <%} %>
			<tr class="transparent" >
				<td width="110" class="transparent-top alignRight"><span class="asterisk">*</span>&nbsp;Organization:</td>
				<td class="transparent-top">

				<table class="transparent" id="orgTable">
					<tbody>
						<tr style="display: table-row;" id="message" class="transparent">
							<td class="transparent-small">
								<div id="notSelectedOrgNodes" style="width:200px; visibility:visible;"><font color="gray">None selected. Use the control on the right to
								select.</font>
								</div>
								<div id="selectedOrgNodesName" style="width:200px"></div>
							
							</td>
						</tr>
						<tr>
							<td >
								
							   <p class="flip">
							   		<div  id= "trail"
										style="background-color: #CCCC99; width: 200px; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
										Select OrgNode</div>
									<div  class="panel" id="innerID" 
										style="display:none; background:#ffffee; overflow:auto; height: 250px;  width: 200px; font-family: Arial, Verdana, Sans Serif; font-size: 13px; font-style: normal; font-weight: normal; position: absolute;">
									</div>
								</p>
							
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
	</li>
	<li>
		<div
		style=" width:170px; background-color: #CCCC99; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">Student Additional Information</div>
		<div>
		<div>
			 <jsp:include page="/studentOperation/add_edit_student_by_demographic.jsp" />
		</div>
	</li>
	
	<li>
	<div>
	<table cellspacing="0" cellpadding="0" border="0" id="TblGrid_list2_2" class="EditTable">
		<tbody>
			<tr>
				<td width="100%" colspan="2">
				   <hr style="margin: 1px;" class="ui-widget-content">
				</td>
			</tr>
			<tr id="Act_Buttons" >
				<td  id="preButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-left" id="pData" href="javascript:void(0)"><span
					class="ui-icon ui-icon-triangle-1-w"></span></a></td><td id="nextButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-right" id="nData"
					href="javascript:void(0)"><span class="ui-icon ui-icon-triangle-1-e"></span></a></td>
					<td>&nbsp;</td>
				<td ><a id="sData" href="javascript:studentDetailSubmit();"><div
					style="background-color: #CCCC99; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
					Submit</div></a></td><td><a id="cData" href="javascript:closePopUp();"><div
					style="background-color: #CCCC99; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
					Cancel</div></a></td>
			</tr>
			<tr class="binfo" style="display: none;">
				<td colspan="2" class="bottominfo"></td>
			</tr>
		</tbody>
	</table>
	</div>
	</li>

</ul>

</div>
