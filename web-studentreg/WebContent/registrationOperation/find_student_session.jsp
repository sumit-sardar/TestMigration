
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentRegistrationResource" />

<% String studentIdLabelName = (String)request.getAttribute("studentIdLabelName");
%>

<input type="hidden" id="stuCountId" value=<lb:label key="student.registration.msg.orgStuCount" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdLoginId" value=<lb:label key="student.registration.stuGrid.loginId" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdStdName" value=<lb:label key="student.registration.stuGrig.stdName" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdGroup" value=<lb:label key="student.registration.common.group" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdGrade" value=<lb:label key="student.registration.stuGrig.grade" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdGender" value=<lb:label key="student.registration.stuGrig.gender" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdSessionName" value=<lb:label key="student.registration.common.sessionName" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdTestName" value=<lb:label key="student.registration.common.testName" prefix="'" suffix="'"/>/>
<input type="hidden" id="studentIdLabelName"  value = '<%=studentIdLabelName %>' />
<input type="hidden" id="noStudentTitle" value=<lb:label key="student.registration.noStuSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentMsg" value=<lb:label key="student.registration.noStuSelected.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGridCaption" value=<lb:label key="student.registration.stuGrig.caption" prefix="'" suffix="'"/>/>
<input type="hidden" id="gridShowBy" value=<lb:label key="student.registration.common.showBy" prefix="'" suffix="'"/>/>
<input type="hidden" id="gridShowByStu" value=<lb:label key="student.registration.showBy.student" prefix="'" suffix="'"/>/>
<input type="hidden" id="gridShowBySes" value=<lb:label key="student.registration.showBy.session" prefix="'" suffix="'"/>/>
<input type="hidden" id="sesGridCaption" value=<lb:label key="student.registration.sesGrid.caption" prefix="'" suffix="'"/>/>
<input type="hidden" id="sesGridMyRole" value=<lb:label key="student.registration.sesGrid.myRole" prefix="'" suffix="'"/>/>
<input type="hidden" id="sesGridStatus" value=<lb:label key="student.registration.sesGrid.status" prefix="'" suffix="'"/>/>
<input type="hidden" id="sesGridStartDate" value=<lb:label key="student.registration.sesGrid.startDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="sesGridEndDate" value=<lb:label key="student.registration.sesGrid.endDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="noSessionTitle" value=<lb:label key="student.registration.noSesSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noSessionMessage" value=<lb:label key="student.registration.noSesSelected.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="confirmAlrt" value=<lb:label key="student.registration.alert.confirm" prefix="'" suffix="'"/>/>
<input type="hidden" id="studRegPopupTitle" value=<lb:label key="student.registration.menu" prefix="'" suffix="'"/>/>
<input type="hidden" id="sessionStudRegPopupTitle" value=<lb:label key="student.registration.menu" prefix="'" suffix="'"/>/>
<input type="hidden" id="searchStudentSession" value=<lb:label key="common.label.search" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemListGripCap" value=<lb:label key="student.registration.itemGrid.caption" prefix="'" suffix="'"/>/> 

<%-- <input type="hidden" id="itemGripItemNo" value=<lb:label key="scoring.itemGrid.ItemNo" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripSubtest" value=<lb:label key="scoring.itemGrid.subtestName" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripScoreItm" value=<lb:label key="scoring.itemGrid.scoreItem" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripManual" value=<lb:label key="scoring.itemGrid.manualStatus" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripMaxScr" value=<lb:label key="scoring.itemGrid.maxScore" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripObtained" value=<lb:label key="scoring.itemGrid.scorObtn" prefix="'" suffix="'"/>/> --%>

<input type="hidden" id="sbsGridFirstName" value=<lb:label key="dialog.myProfile.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="sbsGridLastName" value=<lb:label key="dialog.myProfile.lastName" prefix="'" suffix="'"/>/>
<%-- <input type="hidden" id="sbsGridOnStatus" value=<lb:label key="scoring.itemGrid.onTestStatus" prefix="'" suffix="'"/>/> 
<input type="hidden" id="sbsEmptyGrid" value=<lb:label key="scoring.sbs.noStuFound.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="sbiItemEmptyGrid" value=<lb:label key="scoring.sbi.itemSearchEmpty" prefix="'" suffix="'"/>/>
<input type="hidden" id="sbiItemEmptyGridTitle" value=<lb:label key="scoring.noItems.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="sbiGridItemType" value=<lb:label key="scoring.itemGrid.itemType" prefix="'" suffix="'"/>/>
<input type="hidden" id="sbsGridItemEmpty" value=<lb:label key="scoring.sbs.noItemsFoundMessage" prefix="'" suffix="'"/>/>
<input type="hidden" id="responsePopupTitl" value=<lb:label key="questionpopup.header" prefix="'" suffix="'"/>/> --%>
<input type="hidden" id="studentPopupSubtitle" value=<lb:label key="student.popup.subtitle" prefix="'" suffix="'"/>/>

<input type="hidden" id="formRecommendationCaption" value=<lb:label key="registration.formrecommendation.caption" prefix="'" suffix="'"/>/>
<input type="hidden" id="tabeModifySubtestMsg"  value=<lb:label key="session.modifySubtest.tabe.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tabeAdaptiveModifySubtestMsg" value=<lb:label key="session.modifySubtest.tabeadaptive.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="addStuID" name="addStuID" value=<lb:label key="stu.label.addStu" prefix="'" suffix="'"/>/>
<input type="hidden" id="studentClassReassignable" name="classReassignable" value='<%=session.getAttribute("isClassReassignable") %>'/>

<input type="hidden" id="jqgFirstNameID" name="jqgFirstNameID" value=<lb:label key="stu.info.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgLastNameID" name="jqgLastNameID" value=<lb:label key="stu.info.lastName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgMiddleIniID" name="jqgMiddleIniID" value=<lb:label key="stu.label.midInitial" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgGradeID" name="jqgGradeID" value=<lb:label key="stu.info.grade" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgGenderID" name="jqgGenderID" value=<lb:label key="stu.info.gender" prefix="'" suffix="'"/>/>
<input type="hidden" id="dateOfBirthID" name="dateOfBirthID" value=<lb:label key="stu.info.dateOfBirth" prefix="'" suffix="'"/>/>
<input type="hidden" id="orgAssingID" name="orgAssingID" value=<lb:label key="stu.label.orgAssing" prefix="'" suffix="'"/>/>
<input type="hidden" id="missRequiredFieldID" name="missRequiredFieldID" value=<lb:label key="stu.msg.missRequiredField" prefix="'" suffix="'"/>/>
<input type="hidden" id="reqTextID" name="reqTextID" value=<lb:label key="stu.msg.reqText" prefix="'" suffix="'"/>/>
<input type="hidden" id="mReqTextID" name="mReqTextID" value=<lb:label key="stu.msg.mReqText" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidCharID" name="invalidCharID" value=<lb:label key="stu.msg.invalidChar" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidNameID" name="invalidNameID" value=<lb:label key="stu.msg.invalidName" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidBirthDayID" name="invalidBirthDayID" value=<lb:label key="stu.msg.invalidBirthDay" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidDateID" name="invalidDateID" value=<lb:label key="stu.msg.invalidDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="alphaNumericCharsID" name="alphaNumericCharsID" value=<lb:label key="stu.msg.alphaNumericChars" prefix="'" suffix="'"/>/>

<table class="transparent" width="97%" style="margin:15px auto;"> 
	<tr class="transparent">
		<td>
    		<table class="transparent">
				<tr class="transparent">
					<td>
			    		<h1><lb:label key="student.registration.menu" /></h1>
					</td>
				</tr>
				<tr> 
					<td class="subtitle">  
						<lb:label key="student.registration.page.subtitle" />
					</td>	
				</tr>
			</table>		
		</td>
	</tr>
    <tr class="transparent">
        <td align="center">        
		<table width="100%">
		   <tr>
				<td colspan="3" class="buttonsRow">
     				<div id="displayMessageMain" class="errMsgs" style="display: none; width: 50%; float: left;">
						<table>
							<tr>
								<td width="18" valign="middle">
									<div id="errorIcon" style="display:none;">
				                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
									</div>
									<div id="infoIcon" style="display:none;">
										<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif" border="0" width="16" height="16">
									</div>
								</td>
								<td class="saveMsgs" valign="middle">
									<div id="contentMain"></div>
								</td>
							</tr>
						</table>				
					</div>
					<div id="StudRegister" style="float:right;visibility:hidden;">
						<a href="#" id="registerButton" onclick="displayListPopup(this); return false;" class="rounded {transparent} button"><lb:label key="student.registration.button.value" /></a>
					</div> 
					<div style="clear:both;">
				</td>
		   	</tr>
	    	<tr class="transparent">
		        <td style="vertical-align:top; width:16%;" align="left">
			      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="stdt.reg.label.org.search" /></div>
			    	<script>populateStudentRegTree()</script>
			
			    	<div id="outertreebcgdiv" class="treeCtrl" style="height:512px !important">
				    	<div id = "stdRegOrgNode" style="width:auto;height:auto;display:table">
						</div>
					</div>
			 	</td>
			 	
	 			<td class="transparent" width="5px">&nbsp;</td>
		 	
		 		<td style="vertical-align:top;" id="jqGrid-content-section">
		 		    <%--  for student grid --%>
		 			<div id="studentView">
		      			<table id="studentRegistrationGrid" class="gridTable"></table>
						<div id="studentRegistrationPager" class="gridTable"></div>
					</div>
					<%--  for session grid --%>
					<div id="sessionView">
						<table id="sessionRegistrationGrid" class="gridTable"></table>
						<div id="sessionRegistrationPager" class="gridTable"></div>
					</div>
		 		</td>
	    	</tr>
		</table>
        </td>
    </tr>
</table>

<%--  message popup when  root node selected for tree  --%>
<div id="rootNodePopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;" id="exceedMsg">
		</div>
	</div>
	<div style="padding:10px;">
		<center>		
			<input type="button"  value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/> class="ui-widget-header" onclick="closePopUp('rootNodePopup');">
		</center>
	</div>
</div>

<div id="confirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="registration.topNodeSelection.message" />
		</div>
	</div>
	<div style="padding:10px;">		
		<center>
			<input type="button"  value=<lb:label key="common.button.yes" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:fetchDataOnConfirmation(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.no" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUp('confirmationPopup'); return false;" class="ui-widget-header">
		</center>
	</div>	
</div>

<div id="searchStudentByKeyword"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="stu.search.info.message"/></p>
	</div>
	<div class="searchInputBoxContainer" id="searchInputBoxContainer">
		<center>
			<input type="text" name="searchStudentByKeywordInput" id="searchStudentByKeywordInput" onkeypress="trapEnterKey(event);"/>
		</center>	
	</div>
	<div style="padding-bottom:20px;"> 
		<center>
			<input type="button"  value=<lb:label key="common.button.clear" prefix="'" suffix="'"/> onclick="javascript:resetSearch(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.search" prefix="'" suffix="'"/> onclick="javascript:searchStudentByKeyword(); return false;" class="ui-widget-header">
		</center>
	</div>
</div>

<div id="searchSessionByKeyword"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="ses.search.info.message"/></p>
	</div>
	<div class="searchInputBoxContainer" id="searchInputBoxContainer">
		<center>
			<input type="text" name="searchSessionByKeywordInput" id="searchSessionByKeywordInput" onkeypress="trapEnterKey(event);"/>
		</center>	
	</div>
	<div style="padding-bottom:20px;"> 
		<center>
			<input type="button"  value=<lb:label key="common.button.clear" prefix="'" suffix="'"/> onclick="javascript:resetSearch(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.search" prefix="'" suffix="'"/> onclick="javascript:searchSessionByKeyword(); return false;" class="ui-widget-header">
		</center>
	</div>
</div>

<div id="searchStudentSessionByKeyword"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="ses.search.info.message"/></p>
	</div>
	<div class="searchInputBoxContainer" id="searchInputBoxContainer">
		<center>
			<input type="text" name="searchSessionByKeywordInput" id="searchStudentSessionByKeywordInput" onkeypress="trapEnterKey(event);"/>
		</center>	
	</div>
	<div style="padding-bottom:20px;"> 
		<center>
			<input type="button"  value=<lb:label key="common.button.clear" prefix="'" suffix="'"/> onclick="javascript:resetSessionSearch(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.search" prefix="'" suffix="'"/> onclick="javascript:searchStudentSessionByKeyword(); return false;" class="ui-widget-header">
		</center>
	</div>
</div>


<div id="statusLocatorLegend" style="display:none;" class="statusLegendDiv">
<table width="360" cellspacing="2" cellpadding="2" border="0" class="statusLegendTable">
	<tbody>
		<tr align="center">
			<td colspan="2" align="left"><lb:label key="session.locatorSubtest.message" /></td>
		</tr>
	</tbody>
</table>  
</div>

<div id="studentConfirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="stu.msg.notsave" />
		</div>
	</div>		
	<div style="padding:10px;">
		<center>
			<input type="button"  value=<lb:label key="common.button.yes" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:fetchDataOnConfirmation(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.no" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUp('studentConfirmationPopup'); return false;" class="ui-widget-header">
		</center>
	</div>	
</div>
<jsp:include page="student_popup_from_session.jsp" />
<jsp:include page="formRecommendation.jsp" />
<jsp:include page="editSubtest.jsp" />
<%@include file="/registrationOperation/student_add_edit_detail.jsp" %>
<%@include file="/registrationOperation/student_registration_confirmation.jsp" %>
