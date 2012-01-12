<%@ page import="java.io.*, java.util.*,com.ctb.bean.studentManagement.CustomerConfiguration"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentApplicationResource" />
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
	Boolean supportAccommodations = (Boolean)request.getAttribute("supportAccommodations");
	String showEditButton = (String)request.getAttribute("showEditButton"); 
	String showDeleteButton = (String)request.getAttribute("showDeleteButton"); 
	Integer stuCreatedBy = (Integer)session.getAttribute("createdBy"); 
	

%>
<div>
<input type="hidden" id="isLasLinkCustomer"  value = '<%=isLasLinkCustomer %>' />
<input type="hidden" id="isMandatoryBirthDate"  value = '<%=isMandatoryBirthDate %>' />
<input type="hidden" id="isMandatoryStudentId" value = '<%=isMandatoryStudentId %>' />
<input type="hidden" id="isStudentIdConfigurable" value = '<%=isStudentIdConfigurable %>' />
<input type="hidden" id="isStudentId2Configurable" value = '<%=isStudentId2Configurable %>' />
<input type="hidden" id="studentIdLabelName"  value ='<%=studentIdArrValue[0] %>' />
<input type="hidden" id="isStudentIdNumeric" value = '<%=studentIdArrValue[4] %>' />
<input type="hidden" id="studentIdMinLength" value = '<%=studentIdArrValue[3] %>' />
<input type="hidden" id="studentId2LabelName"  value = '<%=studentId2ArrValue[0] %>' />
<input type="hidden" id="isStudentId2Numeric" value = '<%=studentId2ArrValue[3] %>' />
<input type="hidden" id="studentId2MinLength" value = '<%=studentId2ArrValue[2] %>' />
<input type="hidden" id="supportAccommodations" value = '<%=supportAccommodations %>' />
<input type="hidden" id="showEditButton" value = '<%=showEditButton %>' />
<input type="hidden" id="showDeleteButton" value = '<%=showDeleteButton %>' />
<input type="hidden" id="stuCreatedBy" value = '<%=stuCreatedBy %>' />
</div>

<div id="addEditStudentDetail"
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
				<h3><a href="#"><lb:label key="stu.label.info" /></a></h3>
				
					<div id="Student_Information" style="background-color: #FFFFFF;">
	
						<table class="transparent">
							<tbody>
								<tr class="transparent">
								<td style ="vertical-align: top;">
								<table class="transparent" width="350">
								<tbody>
									<tr class="transparent">
										<td width="110" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="stu.info.firstName" suffix=":"/></td>
										<td class="transparent"><input type="text" style="width: 200px;" tabindex="0" maxlength="32" id="studentFirstName" name="studentFirstName">
										</td>
									</tr>
									<tr class="transparent">
										<td width="110" nowrap="" class="transparent alignRight"><lb:label key="stu.info.middleName" suffix=":"/></td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="studentMiddleName" name="studentMiddleName"></td>
									</tr>
									<tr class="transparent">
										<td width="110" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="stu.info.lastName" suffix=":"/></td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="32" id="studentLastName" name="studentLastName"></td>
									</tr>
									<tr class="transparent">
										<td width="110" nowrap="" class="transparent alignRight">
										<%if(isMandatoryBirthDate) { %>
											<span class="asterisk">*</span>
										<% }%>
										<lb:label key="stu.info.dateOfBirth" suffix=":"/></td>
										<td nowrap="" class="transparent">    
											<select style="width: 65px;"   id="monthOptions" name="monthOptions">		
											</select>
													
											<select style="width: 65px;"  id="dayOptions" name="dayOptions">
											</select>
											
											<select style="width: 66px;"  id="yearOptions" name="yearOptions">
											</select>
										</td>                    
						   
									</tr>
									<tr class="transparent">
										<td width="110" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="stu.info.grade" suffix=":"/></td>
										<td class="transparent">  
											<select id="gradeOptions" name="gradeOptions" style="width: 202px;">
											</select> 
										</td>
									</tr>
											<tr class="transparent">
										<td width="110" nowrap="" class="transparent alignRight"><span class="asterisk">*</span>&nbsp;<lb:label key="stu.info.gender" suffix=":"/></td>
										<td class="transparent">
										 <select id="genderOptions"  name="genderOptions" style="width: 202px;">
										 </select> 
										</td>

									</tr>
									
									<!--ext_pin1 is added for DEX CR-->
									<tr class="transparent">
										<td  width="110" class="transparent alignRight">	
										 <%if(isMandatoryStudentId) { %>
											<span class="asterisk">*</span>&nbsp;
										  <%} %>	
										 <%if(isStudentIdConfigurable) {%> 
											 <%=studentIdArrValue[0] %>	:
										 <%} else {%> 
											<lb:label key="stu.info.studentID" suffix=":"/>	
										 <%} %>
										 </td>
										
										</td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="<%= isStudentIdConfigurable ? new Integer(studentIdArrValue[1]).intValue()>0 && new Integer(studentIdArrValue[1]).intValue()<32 ? new Integer(studentIdArrValue[1]).intValue() : 32 : 32 %>" id="studentExternalId" name="studentExternalId"></td>
									</tr>
									<tr class="transparent">
										<td  width="110" class="transparent alignRight">
										<%if(isStudentId2Configurable) {%> 
											<%=studentId2ArrValue[0] %>	: 
										<%} else {%> 
										<lb:label key="stu.info.studentID2" suffix=":"/>
										<%} %>
										</td>
										<td class="transparent"><input type="text" style="width: 200px;" maxlength="<%=isStudentId2Configurable ? new Integer(studentId2ArrValue[1]).intValue()>0 && new Integer(studentId2ArrValue[1]).intValue()<32 ? new Integer(studentId2ArrValue[1]).intValue() : 32 : 32 %>" id="studentExternalId2" name="studentExternalId2"></td>
									</tr>
									<%if(isLasLinkCustomer) { %>
									 <tr class="transparent">
										<td nowrap="" width="110" class="transparent alignRight"><lb:label key="stu.info.purposeTest" suffix=":"/></td>
										
										<td nowrap="" class="transparent">    
											<select style="width: 200px;"   id="testPurposeOptions" name="testPurposeOptions">		
											</select>
										</td>       					   
									</tr>
								   <%} %>
								   	<tr style="display: table-row;" id="message" class="transparent">
								   		<td width="110" class="transparent alignRight"  style="vertical-align: top;"><span class="asterisk">*</span>&nbsp;<lb:label key="stu.info.org" suffix=":"/></td>
										<td class="transparent-small" style="padding: 5px 5px 0 0">
											<div id="notSelectedOrgNodes" style="width:200px; visibility:visible; padding-left: 4px"><font color="gray"><lb:label key="stu.msg.noneSelected" /></font>
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
													<div style="width: 150px;visibility:visible;" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="stu.label.selNode" /></div>
												</td>
											</tr>
											<tr>
												<td >
												<div  id="innertreebgdiv" class="innertreeCtrl treeCtrl">
													<div id="innerID" style="width:100%;height:auto;display:table">
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
			<div>
				<h3><a href="#"><lb:label key="stu.label.extraInfo" /></a></h3>
				<div id="Student_Additional_Information" style="overflow-y: scroll !important; overflow-x: hidden !important;"><!-- changes for defect #66994 -->
					<jsp:include page="/studentOperation/add_edit_student_by_demographic.jsp" />				
				</div>
			</div>
			<div>
				<h3><a href="#"><lb:label key="stu.label.specificAccoInfo" /></a></h3>
				<div id="Student_Accommodation_Information" style="overflow-y: scroll !important; overflow-x: hidden !important;"><!-- changes for defect #66994 -->
					<jsp:include page="/studentOperation/add_edit_student_by_accommodation.jsp" />
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
					<input type="button"  id="sData" value=<lb:label key="common.button.save" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:studentDetailSubmit(); return false;" class="ui-widget-header">
					<input type="button"  id="cData" value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:onCancel(); return false;" class="ui-widget-header">
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
