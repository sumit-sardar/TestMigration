<%@ page language="java" contentType="text/html;charset=UTF-8"%>


<input type="hidden" id="addStudentEnable" name="addStudentEnable" value='<%=session.getAttribute("addStudentEnable") %>'/>
<input type="hidden" id="deleteStudentEnable" name="deleteStudentEnable" value='<%=session.getAttribute("deleteStudentEnable") %>'/>
<input type="hidden" id="jqgFirstNameID" name="jqgFirstNameID" value=<lb:label key="stu.info.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgLastNameID" name="jqgLastNameID" value=<lb:label key="stu.info.lastName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgMiddleIniID" name="jqgMiddleIniID" value=<lb:label key="stu.label.midInitial" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgGradeID" name="jqgGradeID" value=<lb:label key="stu.info.grade" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgOrgID" name="jqgOrgID" value=<lb:label key="stu.info.org" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgGenderID" name="jqgGenderID" value=<lb:label key="stu.info.gender" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgAccoID" name="jqgAccoID" value=<lb:label key="stu.label.acco" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgLoginID" name="jqgLoginID" value=<lb:label key="stu.info.loginID" prefix="'" suffix="'"/>/>
<input type="hidden" id="addStuID" name="addStuID" value=<lb:label key="stu.label.addStu" prefix="'" suffix="'"/>/>
<input type="hidden" id="editStuID" name="editStuID" value=<lb:label key="stu.label.editStu" prefix="'" suffix="'"/>/>
<input type="hidden" id="viewStuID" name="viewStuID" value=<lb:label key="stu.label.viewStu" prefix="'" suffix="'"/>/>
<input type="hidden" id="confirmID" name="confirmID" value=<lb:label key="stu.label.confirm" prefix="'" suffix="'"/>/>
<input type="hidden" id="delStuID" name="delStuID" value=<lb:label key="stu.label.delStu" prefix="'" suffix="'"/>/>
<input type="hidden" id="delStuSuccessID" name="delStuSuccessID" value=<lb:label key="stu.msg.stuDelSuccess" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuEditAccoID" name="stuEditAccoID" value=<lb:label key="stu.label.editAcco" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuNoAccoID" name="stuNoAccoID" value=<lb:label key="stu.msg.noAcco" prefix="'" suffix="'"/>/>
<input type="hidden" id="searchStudentID" name = "searchStudentID" value=<lb:label key="stu.label.searchStudentID" prefix="'" suffix="'"/>/>
<input type="hidden" id="miID" name="miID" value=<lb:label key="stu.label.mi" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuListID" name="stuListID" value=<lb:label key="stu.label.list" prefix="'" suffix="'"/>/>
<input type="hidden" id="mprofileID" name="mprofileID" value=<lb:label key="stu.label.mprofile" prefix="'" suffix="'"/>/>
<input type="hidden" id="bcastMsgID" name="bcastMsgID" value=<lb:label key="stu.label.bcastMsg" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidNumCharID" name="invalidNumCharID" value=<lb:label key="stu.msg.invalidNumChar" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidNameID" name="invalidNameID" value=<lb:label key="stu.msg.invalidName" prefix="'" suffix="'"/>/>
<input type="hidden" id="reqTextID" name="reqTextID" value=<lb:label key="stu.msg.reqText" prefix="'" suffix="'"/>/>
<input type="hidden" id="mReqTextID" name="mReqTextID" value=<lb:label key="stu.msg.mReqText" prefix="'" suffix="'"/>/>
<input type="hidden" id="alphaNumericCharsID" name="alphaNumericCharsID" value=<lb:label key="stu.msg.alphaNumericChars" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidNumFrmtID" name="invalidNumFrmtID" value=<lb:label key="stu.msg.invalidNumFrmt" prefix="'" suffix="'"/>/>
<input type="hidden" id="minLengthFormatID" name="minLengthFormatID" value=<lb:label key="stu.msg.minLengthFormat" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidDateID" name="invalidDateID" value=<lb:label key="stu.msg.invalidDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidCharID" name="invalidCharID" value=<lb:label key="stu.msg.invalidChar" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidBirthDayID" name="invalidBirthDayID" value=<lb:label key="stu.msg.invalidBirthDay" prefix="'" suffix="'"/>/>
<input type="hidden" id="dateOfBirthID" name="dateOfBirthID" value=<lb:label key="stu.info.dateOfBirth" prefix="'" suffix="'"/>/>
<input type="hidden" id="orgAssingID" name="orgAssingID" value=<lb:label key="stu.label.orgAssing" prefix="'" suffix="'"/>/>
<input type="hidden" id="missRequiredFieldID" name="missRequiredFieldID" value=<lb:label key="stu.msg.missRequiredField" prefix="'" suffix="'"/>/>
<input type="hidden" id="missRequiredFieldsID" name="missRequiredFieldsID" value=<lb:label key="stu.msg.missRequiredFields" prefix="'" suffix="s'"/>/>
<input type="hidden" id="middleNameID" name="middleNameID" value=<lb:label key="stu.info.middleName" prefix="'" suffix="'"/>/>
<input type="hidden" id="assignAccoID" name="assignAccoID" value=<lb:label key="stu.label.assignAcco" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuCapId" name="stuCapId" value=<lb:label key="stu.menu.studentList" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuCountId" name="stuCountId" value=<lb:label key="stu.msg.orgStuCount" prefix="'" suffix="'"/>/>
<input type="hidden" id="classReassignable" name="classReassignable" value='<%=session.getAttribute("isClassReassignable") %>'/>
<input type="hidden" id="noStudentTitle" name = "noStudentTitle" value=<lb:label key="student.noStuSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentMsg" name = "noStudentMsg" value=<lb:label key="student.noStuSelected.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="filterNoStuSelected" name = "filterNoStuSelected" value=<lb:label key="student.filterNoStuSelected.message" prefix="'" suffix="'"/>/> 

<%
	 Boolean canRegisterStudent = (Boolean) session.getAttribute("canRegisterStudent");
%>
<input type="hidden" id="canRegisterStudent" value='<%=canRegisterStudent %>'/>

<table class="transparent" width="97%" style="margin:15px auto;"> 
	<tr class="transparent">
        <td>
		<table class="transparent">
			<tr class="transparent">
				<td>
		    		<h1><lb:label key="stu.students.title" /></h1>
				</td>
			</tr>
			<tr> 
				<td class="subtitle">  
					<lb:label key="stu.students.subtitle" />
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
				        <div id="displayMessageMain" style="display:none; width:99.5%;" class="errMsgs">
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
			    <% if (canRegisterStudent) { %>
						<div id="registerStudentDiv" style="visibility:hidden; float: right;">
							<div id="cpw">
								<a href="#" id="registerStudentButton" onclick="javascript:rapidRegistration(); return false;" class="rounded {transparent} button">
									<lb:label key="common.button.registerStudent" />
								</a>
							</div>
						</div>
			    <% } %>
						<div style="clear:both;"></div>
					</td>
				</tr>
				<!-- 
		    < % if(canRegisterStudent) { %>
				<tr class="transparent"> visibility:hidden;
			        <td colspan="3" align="right">
						<div id="ShowButtons" style="display:block; float:right;">
							<a href="#" id="registerStudentButton" onclick="" class="rounded {transparent} button"><lb:label key="common.button.registerStudent" /></a>
						</div>
					</td>
				</tr>
		    < % } %>
				 -->
								
				<tr class="transparent">
		        <td style="vertical-align:top; width:16%;" align="left">
			      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="stu.label.search" /></div>
			    	<script>populateTree();</script>	
			    	<div id="outertreebgdiv" class="treeCtrl">	
				    	<div id="orgNodeHierarchy"  style="width:auto;height:auto;display:table">
						</div>
					</div>
				 </td>
				 
			 	 <td class="transparent" width="5px">&nbsp;</td>
			 	
				 <td style="vertical-align:top;"  id="jqGrid-content-section">
			      	<table id="list2" class="gridTable"></table>
					<div id="pager2" class="gridTable"></div>			
				</td>
				
				</tr>
			</table>
        </td>
    </tr>
</table>

<%@include file="/studentOperation/add_edit_student_detail.jsp" %>
<%@include file="/studentOperation/view_student_detail.jsp" %>
<div id="confirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="stu.msg.notsave" />
		</div>
	</div>		
	<div style="padding:10px;">
		<center>
			<input type="button"  value=<lb:label key="common.button.yes" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closeConfirmationPopup(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.no" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUp('confirmationPopup'); return false;" class="ui-widget-header">
		</center>
	</div>	
</div>
<div id="confirmationPopupNavigation"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="stu.msg.notsave" />
		</div>
	</div>
	<div style="padding:10px;">		
		<center>
			<input type="button"  value=<lb:label key="common.button.yes" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closeConfirmationPopup(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.no" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUp('confirmationPopupNavigation'); return false;" class="ui-widget-header">
		</center>
	</div>	
</div>
<div>
	<input type="hidden" id="deleteStatus" name="deleteStatus" value=""/>
</div>
<div id="deleteStudentPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="stu.msg.delete" />
		</div>
	</div>		
	<div style="padding:10px;">
		<center>	
			<input type="button"  value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="submitDeleteStudentPopup(); showDeleteStudentStatus();" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="closePopUp('deleteStudentPopup');" class="ui-widget-header">				
		</center>
	</div>		
</div>
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

<div id="searchUserByKeyword"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="stu.search.info.message"/></p>
	</div>
	<div class="searchInputBoxContainer" id="searchInputBoxContainer">
		<center>
			<input type="text" name="searchUserByKeywordInput" id="searchUserByKeywordInput" onkeypress="trapEnterKey(event);"/>
		</center>	
	</div>
	<div style="padding-bottom:20px;"> 
		<center>
			<input type="button"  value="Clear" onclick="javascript:resetSearch(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value="Search" onclick="javascript:searchUserByKeyword(); return false;" class="ui-widget-header">
		</center>
	</div>
</div>
<div id="nodataSelectedPopUp"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding-top:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="studentList.no.data.selected"/>
		</div>
	</div>
</div>
