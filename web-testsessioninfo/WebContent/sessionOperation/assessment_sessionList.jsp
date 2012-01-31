<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />
<netui-data:declareBundle bundlePath="webResources" name="web"/>

<input type="hidden" id="fieldDisabled" name="fieldDisabled" value=<lb:label key="session.edit.fieldDisable" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuLogged" name="stuLogged" value=<lb:label key="session.edit.stuLogged" prefix="'" suffix="'"/>/>
<input type="hidden" id="sessionEnd" name="sessionEnd" value=<lb:label key="session.edit.sessionEnded" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentLogged" name="noStudentLogged" value=<lb:label key="session.edit.noStudentLogged" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentLogged2" name="noStudentLogged2" value=<lb:label key="session.edit.noStudentLoggedMsg" prefix="'" suffix="'"/>/>
<input type="hidden" id="noPermission" name="noPermission" value=<lb:label key="session.edit.noPermission" prefix="'" suffix="'"/>/>
<input type="hidden" id="sessionList" name="sessionList" value=<lb:label key="homePage.grid.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="sessionName" name="sessionName" value=<lb:label key="homepage.grid.sessionName" prefix="'" suffix="'"/>/>
<input type="hidden" id="testName" name="testName" value=<lb:label key="homepage.grid.testName" prefix="'" suffix="'"/>/>
<input type="hidden" id="organization" name="organization" value=<lb:label key="homepage.grid.org" prefix="'" suffix="'"/>/>
<input type="hidden" id="myRole" name="myRole" value=<lb:label key="homepage.grid.myRole" prefix="'" suffix="'"/>/>
<input type="hidden" id="startDateGrid" name="startDateGrid" value=<lb:label key="homepage.grid.startDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="endDateGrid" name="endDateGrid" value=<lb:label key="homepage.grid.endDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="procListGrid" name="procListGrid" value=<lb:label key="proc.grid.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuListGrid" name="stuListGrid" value=<lb:label key="session.menu.studentList" prefix="'" suffix="'"/>/>
<input type="hidden" id="dupStuResolve" name="dupStuResolve" value=<lb:label key="stu.dupStudent.resolve" prefix="'" suffix="'"/>/>
<input type="hidden" id="editTestSn" name="editTestSn" value=<lb:label key="sessionList.editTest.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="printTT" name="printTT" value=<lb:label key="sessionList.testTicket.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="testStuFN" name="testStuFN" value=<lb:label key="stu.info.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="testStuLN" name="testStuLN" value=<lb:label key="stu.info.lastName" prefix="'" suffix="'"/>/>
<input type="hidden" id="searchSessionID" name = "searchSessionID" value=<lb:label key="testSession.label.searchSessID" prefix="'" suffix="'"/>/>
<input type="hidden" id="testStuMI" name="testStuMI" value=<lb:label key="stu.info.mi" prefix="'" suffix="'"/>/>
<input type="hidden" id="testStuAccom" name="testStuAccom" value=<lb:label key="stu.info.accom" prefix="'" suffix="'"/>/>
<input type="hidden" id="testStuForm" name="testStuForm" value=<lb:label key="stu.info.form" prefix="'" suffix="'"/>/>
<input type="hidden" id="testListGrid" name="testListGrid" value=<lb:label key="testList.grid.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="testDetLevel" name="testDetLevel" value=<lb:label key="testSession.grid.level" prefix="'" suffix="'"/>/>
<input type="hidden" id="testDetGrade" name="testDetGrade" value=<lb:label key="testSession.grid.grade" prefix="'" suffix="'"/>/>
<input type="hidden" id="testDetsubTest" name="testDetsubTest" value=<lb:label key="testSession.grid.subtest" prefix="'" suffix="'"/>/>
<input type="hidden" id="testDetDuration" name="testDetDuration" value=<lb:label key="selectTest.label.duration" prefix="'" suffix="'"/>/>
<input type="hidden" id="confirmAlrt" name="confirmAlrt" value=<lb:label key="common.alert.confirmAlert" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuRos" name="stuRos" value=<lb:label key="viewStatus.dialog.stuRos" prefix="'" suffix="'"/>/>
<input type="hidden" id="schSession" name="schSession" value=<lb:label key="homepage.button.scheduleSession" prefix="'" suffix="'"/>/>
<input type="hidden" id="schViewSts" name="schViewSts" value=<lb:label key="homepage.button.viewStatus" prefix="'" suffix="'"/>/>
<input type="hidden" id="reqTextmsg" name="reqTextmsg" value=<lb:label key="common.requiredText.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="reqTextMultimsg" name="reqTextMultimsg" value=<lb:label key="common.requiredTextMulti.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="invDateMsg" name="invDateMsg" value=<lb:label key="common.invalidDates.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacSixCharsMsg" name="tacSixCharsMsg" value=<lb:label key="common.tacSixChars.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacSpCharHdrMsg" name="tacSpCharHdrMsg" value=<lb:label key="common.tacSpCharHdr.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacSpCharNAMsg" name="tacSpCharNAMsg" value=<lb:label key="common.tacSpCharNA.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacIdentTacHdrMsg" name="tacIdentTacHdrMsg" value=<lb:label key="common.tacIdentTacHdr.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacIdentTac1Msg" name="tacIdentTac1Msg" value=<lb:label key="common.tacIdentTac1.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacIdentTac2Msg" name="tacIdentTac2Msg" value=<lb:label key="common.tacIdentTac2.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="missTacHdrMsg" name="missTacHdrMsg" value=<lb:label key="common.missTacHdr.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="missTacMsg" name="missTacMsg" value=<lb:label key="common.missTac.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="missTacHdrMulMsg" name="missTacHdrMulMsg" value=<lb:label key="common.missTacHdrMulti.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="missTac1Msg" name="missTac1Msg" value=<lb:label key="common.missTac1.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="missTac2Msg" name="missTac2Msg" value=<lb:label key="common.missTac2.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuDelMsg" name="stuDelMsg" value=<lb:label key="common.stuDel.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuAddedMsg" name="stuAddedMsg" value=<lb:label key="common.stuAdded.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuDelAllMsg" name="stuDelAllMsg" value=<lb:label key="common.stuDelAll.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="procDelMsg" name="procDelMsg" value=<lb:label key="common.procDel.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="procAddMsg" name="procAddMsg" value=<lb:label key="common.procAdded.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="procDelAllMsg" name="procDelAllMsg" value=<lb:label key="common.procDelAll.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="snNmInChHdrMsg" name="snNmInChHdrMsg" value=<lb:label key="common.snNmInChHdr.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="snNmInChBdyMsg" name="snNmInChBdyMsg" value=<lb:label key="common.snNmInChBdy.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tstLcnInChHdrMsg" name="tstLcnInChHdrMsg" value=<lb:label key="common.tstLcnInChHdr.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tstLcnInChBdyMsg" name="tstLcnInChBdyMsg" value=<lb:label key="common.tstLcnInChBdy.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="monitorStsValidMsg" name="monitorStsValidMsg" value=<lb:label key="monitorSts.snValid.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="doNotScoreMsg" name="doNotScoreMsg" value=<lb:label key="donotScore.snValid.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="delSessionTitle" name="delSessionTitle" value=<lb:label key="sessionList.deleteTest.title" prefix="'" suffix="'"/>/>	
<input type="hidden" id="deleteSuccessMsg" name="deleteSuccessMsg" value=<lb:label key="session.msg.deleteSuccess" prefix="'" suffix="'"/>/>
<input type="hidden" id="deleteFailureMsg" name="deleteFailureMsg" value=<lb:label key="session.msg.deleteFailure" prefix="'" suffix="'"/>/>
<input type="hidden" id="hashDisplay" name="hashDisplay" value=<lb:label key="selectTest.label.hash" prefix="'" suffix="'"/>/>
<input type="hidden" id="subtestNameDisplay" name="subtestNameDisplay" value=<lb:label key="selectTest.label.subtestName" prefix="'" suffix="'"/>/>
<input type="hidden" id="acsCodeDisplay" name="acsCodeDisplay" value=<lb:label key="selectTest.label.tstAccessCode" prefix="'" suffix="'"/>/>
<input type="hidden" id="durationDisplay" name="durationDisplay" value=<lb:label key="selectTest.label.duration" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentValidMsg" name="noStudentValidMsg" value=<lb:label key="noStudents.snValid.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="noTestSelected" name="noTestSelected" value=<lb:label key="scheduleTest.noTestSelected" prefix="'" suffix="'"/>/>
<input type="hidden" id="restrictedTitle" name="restrictedTitle" value=<lb:label key="showrestrictedstudents.window.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="resSessionName" name="resSessionName" value=<lb:label key="restricted.testSesionName" prefix="'" suffix="'"/>/>
<input type="hidden" id="resStartDate" name="resStartDate" value=<lb:label key="restricted.startDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="resEndDate" name="resEndDate" value=<lb:label key="restricted.endDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="resGridTitle" name="resGridTitle" value=<lb:label key="resStu.grid.title" prefix="'" suffix="'"/>/>

	<%
	 Boolean canRegisterStudent = (Boolean) session.getAttribute("canRegisterStudent");
	 Boolean userScheduleAndFindSessionPermission = (Boolean) session.getAttribute("userScheduleAndFindSessionPermission");
	 String studentIdLabelName = (String) session.getAttribute("studentIdLabelName");
	 Boolean supportAccommodations  = (Boolean) session.getAttribute("supportAccommodations");
	 String schedulerFirstName = (String) session.getAttribute("schedulerFirstName");
	 String schedulerLastName = (String) session.getAttribute("schedulerLastName");
	 String schedulerUserId = (String) session.getAttribute("schedulerUserId");
	 String schedulerUserName = (String) session.getAttribute("schedulerUserName");
	 Boolean isDeleteSessionEnable = (Boolean)session.getAttribute("isDeleteSessionEnable");
	%>
	
	<input type="hidden" id="canRegisterStudent" name = "canRegisterStudent" value='<%=canRegisterStudent %>'/>
	<input type="hidden" id="userScheduleAndFindSessionPermission" name = "userScheduleAndFindSessionPermission" value='<%=userScheduleAndFindSessionPermission %>'/>
	<input type="hidden" id="studentIdLabelName" name = "studentIdLabelName" value='<%=studentIdLabelName %>'/>
	<input type="hidden" id="supportAccommodations" name = "supportAccommodations" value='<%=supportAccommodations %>'/>
	
	<input type="hidden" id="schedulerFirstName" name = "schedulerFirstName" value='<%=schedulerFirstName %>'/>
	<input type="hidden" id="schedulerLastName" name = "schedulerLastName" value='<%=schedulerLastName %>'/>
	<input type="hidden" id="schedulerUserId" name = "schedulerUserId" value='<%=schedulerUserId %>'/>
	<input type="hidden" id="schedulerUserName" name = "schedulerUserId" value='<%=schedulerUserName %>'/>
	<input type="hidden" id="isDeleteSessionEnable" name="isDeleteSessionEnable" value='<%=isDeleteSessionEnable %>'/> 
	 
	 <div style="clear:both;float:left;width:100%;text-align: left;"> 
			<p class="subtitle"><lb:label key="homepage.viewTestSessions.message" />
				<%if(canRegisterStudent) { %>
					<lb:label key="homepage.rapidRegister.message" />
				<%} if (userScheduleAndFindSessionPermission) {%>
					<lb:label key="homepage.OrgSearchInfo.message" />
				<% } %>
			</p>
	</div>
	<div id="showSaveTestMessage" style="float:left; padding-top:5px;" > 
			<table>
				<tr>
					<td width="18" valign="top">
					<div id="errorIcon" style="display:none;">
                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
					</div>
					<div id="infoIcon" style="display:none;">
                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif" border="0" width="16" height="16">
					</div>					
					</td>
					<td class="saveMsgs" valign="top">
					   	<div id="saveTestTitle" style="display:none;font-weight:bold;"></div>
					   	<div id="saveTestContent" style="display:none;"></div>
					   	<div id="saveTestMessage" style="display:none;"></div>
					</td>
				</tr>
		</table>
			
	</div>
	<jsp:include page="/sessionOperation/print_ticket.jsp" /> 
	<table style="clear:both;float:left" width= "100%"> 
		   	<tr >
		   		<td style="clear: both; min-height: 25px; height: 25px;">
		       		<div style="float:left;width:1200px;padding-top: 5px;">
						<div  style="float:left;width:200px;white-space: nowrap;" class="transparent">
							<a  href="#" onclick="javascript:reloadHomePage(); return false;" style="display: block; width:50%; float:left; text-align: center;" class="rounded {transparent} button"><lb:label key="homepage.button.mySession" /></a> 
						</div>  
						<div id="ShowButtons" style="width:1000px; display:none; float:left;">
							<div id="viewStatus" style="float:right;padding-left:5px;">
								<a href="#" id="viewStatusButton" onclick="javascript:viewTestStatus(); return false;" class="rounded {transparent} button"><lb:label key="homepage.button.viewStatus" /></a>
							</div> 
						    <%if(canRegisterStudent) { %>
							    <div id="registerStudent" style="float:right;padding-left:5px;">
								<a href="#" id="registerStudentButton" onclick="" class="rounded {transparent} button"><lb:label key="homepage.button.registerStudent" /></a>
							    </div>
						    <%} if (userScheduleAndFindSessionPermission) {%>
							    <div id="scSession" style="float:right;padding-left:5px;">
								<a href="#" id="scSessionButton" onclick="javascript:scheduleNewSession(); return false;" class="rounded {transparent} button"><lb:label key="homepage.button.scheduleSession" /></a>
							    </div> 
						    <%} %>	
						    <div id="printTicket" style="float:right;padding-left:5px;">
								<a href="#" id="printTicketButton" onclick="printTTicket(this);" class="rounded {transparent} button"><lb:label key="homepage.button.printTicket" /></a>
							</div> 		
						</div>  
					</div>
	     	   </td>
		   	</tr>
	</table>
	
	<div style="float:left;width:1200px;padding-left:2px;">
	<%if(userScheduleAndFindSessionPermission) { %>
		<div id="show" style="display: block;width:25px;float:left; padding: 3px 0 3px 3px;" class="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br ui-widget-header " title="${bundle.web['homepage.icon.showOrganization']}">
   			<a href="#" onclick="showTreeSlider();" style=" width:100%; " >>></a>
   		</div>
   	<%} %>
		<div id="orgSlider" style="float:left;width:0px;display:none;white-space: nowrap;" class="transparent">
			<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader" style="text-align:left;">
				<div style="float:left;" >&nbsp;<lb:label key="homepage.hierarchy.title" /></div> 
				<div style="float:right;" title="${bundle.web['homepage.icon.hideOrganization']}"><a id="hide" href="#" onclick="hideTreeSlider();" style="display: none; width:100%;" >&nbsp;&lt;&lt;&nbsp;</a></div>
				<div style="clear:both;"></div>     
			</div>
	    	<div id="outertreebgdiv" class="treeCtrl" style="text-align: left !important;">
			    <div id="orgNodeHierarchy" style="width:auto;height:auto;display:table">
				</div>
			</div> 
		</div>	  
	<%if(userScheduleAndFindSessionPermission) { %>		     
		<div id="sessionGrid" style="float:right;width:1150px;position:relative;z-index:100;vertical-align:top;"> 
	<%} else {%>
		<div id="sessionGrid" style="float:right;width:1200px;position:relative;z-index:100;vertical-align:top;"> 	
	<% } %>		      		    				
					<div id="accordion" style="width:100%;position:relative;">							
						<div style="position:relative;">
						  	<h3><a href="#"><lb:label key="homepage.tab.currentAndFuture" /></a></h3>
							<div id="CurrentFuture" style="background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
								<table id="list2" class="gridTable"></table>
								<div id="pager2" class="gridTable"></div>		
							</div>								
						</div>
						<div style="position:relative;">
							<h3><a href="#"><lb:label key="homepage.tab.completed" /></a></h3>
							<div id="Completed" style="background-color: #FFFFFF;overflow-y: hidden !important; overflow-x: hidden !important;">
								<table id="list3" class="gridTable"></table>
								<div id="pager3" class="gridTable"></div>	
							</div>									
						</div>							
					</div>
				
		</div>
	</div>
	
	
				
<div id="confirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="common.topNodeSelection.message" />
		</div>
	</div>
	<div style="padding:10px;">		
		<center>
			<input type="button"  value=<lb:label key="common.button.yes" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:fetchDataOnConfirmation(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.no" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUp('confirmationPopup'); return false;" class="ui-widget-header">
		</center>
	</div>	
</div>
<div id="searchUserByKeywordList2"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="sessionList.search.info.message"/></p>
	</div>
	<div class="searchInputBoxContainer" id="searchInputBoxContainerList2">
		<center>
			<input type="text" name="searchUserByKeywordInput" id="searchUserByKeywordInputList2" onkeypress="trapEnterKeyList2(event);"/>
		</center>	
	</div>
	<div style="padding-bottom:20px;">
		<center>
			<input type="button"  value="Reset" onclick="javascript:resetSearchList2(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value="Search" onclick="javascript:searchUserByKeywordList2(); return false;" class="ui-widget-header">
		</center>
	</div>
</div>
<div id="searchUserByKeywordList3"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="sessionList.search.info.message"/></p>
	</div>
	<div class="searchInputBoxContainer" id="searchInputBoxContainerList3">
		<center>
			<input type="text" name="searchUserByKeywordInput" id="searchUserByKeywordInputList3" onkeypress="trapEnterKeyList3(event);"/>
		</center>	
	</div>
	<div style="padding-bottom:20px;">
		<center>
			<input type="button"  value="Reset" onclick="javascript:resetSearchList3(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value="Search" onclick="javascript:searchUserByKeywordList3(); return false;" class="ui-widget-header">
		</center>
	</div>
</div>


<div id="statusLegend" style="display:none;" class="statusLegendDiv">
<table width="360" cellspacing="2" cellpadding="2" border="0" class="statusLegendTable">
	<tbody>
		<tr align="center">
			<td colspan="2" align="left"><lb:label key="selectstudentpage.Msg.statusKey" /></td>
		</tr>
		<tr align="center">
			<td align="left" width="30"><lb:label key="selectstudentpage.Msg.completed" /></td>
			<td align="left" width="320"><lb:label key="selectstudentpage.Msg.completedMsg" /></td>
		</tr>
		<tr align="center">
			<td align="left" width="30"><lb:label key="selectstudentpage.Msg.inprogress" /></td>
			<td align="left" width="320"><lb:label key="selectstudentpage.Msg.inprogressMsg" /></td>
		</tr>
		<tr align="center">
			<td align="left" width="30"><lb:label key="selectstudentpage.Msg.schedule" /></td>
			<td align="left" width="320"><lb:label key="selectstudentpage.Msg.scheduleMsg" /></td>
		</tr>
		<tr align="center">
			<td align="left" width="30"><lb:label key="selectstudentpage.Msg.Preschedule" /></td>
			<td align="left" width="320"><lb:label key="selectstudentpage.Msg.PrescheduleMsg" /></td>
		</tr>
	</tbody>
</table>  
</div>

<div id="deleteSessionPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="session.msg.delete" />
		</div>
	</div>
	<div style="padding:10px;">		
		<center>
			<input type="button"  value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="deleteTestSession();" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="closePopUp('deleteSessionPopup');" class="ui-widget-header">				
		</center>
	</div>		
</div>

<div id="nodataSelectedPopUp"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;">
		<center>
			<lb:label key="sessionList.no.data.selected"/>
		</center>	
	</div>
</div>

<jsp:include page="/sessionOperation/schedule_session.jsp" />
<jsp:include page="/sessionOperation/duplicate_student.jsp" />
<jsp:include page="/sessionOperation/view_test_session.jsp" />
<jsp:include page="/sessionOperation/restricted_student.jsp" />