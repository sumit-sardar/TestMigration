<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />
<netui-data:declareBundle bundlePath="webResources" name="web"/>


<%
	 Boolean showStudentReportButton = (Boolean) session.getAttribute("showStudentReportButton");
	 Boolean userScheduleAndFindSessionPermission = (Boolean) session.getAttribute("userScheduleAndFindSessionPermission");
	 String studentIdLabelName = (String) session.getAttribute("studentIdLabelName");
	 Boolean supportAccommodations  = (Boolean) session.getAttribute("supportAccommodations");
	 String schedulerFirstName = (String) session.getAttribute("schedulerFirstName");
	 String schedulerLastName = (String) session.getAttribute("schedulerLastName");
	 String schedulerUserId = (String) session.getAttribute("schedulerUserId");
	 String schedulerUserName = (String) session.getAttribute("schedulerUserName");
	 Boolean isDeleteSessionEnable = (Boolean)session.getAttribute("isDeleteSessionEnable");
	 Boolean showModifyManifest = (Boolean) session.getAttribute("showModifyManifest");
	 String pageSize = (String)session.getAttribute("pageSize");
%>
	
	<input type="hidden" id="userScheduleAndFindSessionPermission" value='<%=userScheduleAndFindSessionPermission %>'/>
	<input type="hidden" id="studentIdLabelName" value='<%=studentIdLabelName %>'/>
	<input type="hidden" id="supportAccommodations" value='<%=supportAccommodations %>'/>
	
	<input type="hidden" id="schedulerFirstName" name = "schedulerFirstName" value='<%=schedulerFirstName %>'/>
	<input type="hidden" id="schedulerLastName" name = "schedulerLastName" value='<%=schedulerLastName %>'/>
	<input type="hidden" id="schedulerUserId" name = "schedulerUserId" value='<%=schedulerUserId %>'/>
	<input type="hidden" id="schedulerUserName" name = "schedulerUserId" value='<%=schedulerUserName %>'/>
	<input type="hidden" id="isDeleteSessionEnable" name="isDeleteSessionEnable" value='<%=isDeleteSessionEnable %>'/>
	<input type="hidden" id="loggedInUserId" name = "loggedInUserId" value='<%=schedulerUserId %>'/> 
	<input type="hidden" id="loggedInUserName" name = "loggedInUserName" value='<%=schedulerUserName %>'/>
	<input type="hidden" id="loggedInFirstName" name = "loggedInFirstName" value='<%=schedulerFirstName %>'/>
	<input type="hidden" id="loggedInLastName" name = "loggedInLastName" value='<%=schedulerLastName %>'/>
	<input type="hidden" id="pageSize" name = "pageSize" value='<%=pageSize %>'/>
	 
	<table width="100%" cellspacing="0" cellpadding="0"> 
		<tr>
			<td>			
				 <div style="clear:both;float:left;width:100%;text-align: left;"> 
						<p class="subtitle"><lb:label key="homepage.viewTestSessions.message" />
							<% if (userScheduleAndFindSessionPermission) {%>
								<lb:label key="homepage.OrgSearchInfo.message" />
							<% } %>
						</p>
				</div>
			</td>
		</tr>
		<tr>
			<td class="sessionMsgRow">
				<div id="showSaveTestMessage" style="float:left;"> 
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
			</td>
		</tr>
		<tr>
			<td>
				<jsp:include page="/sessionOperation/print_ticket.jsp" /> 
				<table style="clear:both;float:left" width= "100%"> 
					   	<tr >
					   		<td style="clear: both; min-height: 25px; height: 25px;">
					       		<div style="float:left;width:1200px;">
									<div  style="float:left;width:200px;white-space: nowrap;" class="transparent">
										<a  href="#" onclick="javascript:reloadHomePage(); return false;" style="display: block; width:50%; float:left; text-align: center;" class="rounded {transparent} button"><lb:label key="homepage.button.mySession" /></a> 
									</div>  
									<div id="ShowButtons" style="width:1000px; display:none; float:left;">
									    <% if (showStudentReportButton) { %>
										    <div id="profileReportSessionDiv" style="float:right;padding-left:5px;">
											<a href="#" id="profileReportSessionButton" onclick="javascript:viewIndividualReport(this, 'session'); return false;" class="rounded {transparent} button"><lb:label key="homepage.button.studentReport" /></a>
										    </div>
										<% } %>
										<div id="viewStatus" style="float:right;padding-left:5px;">
											<a href="#" id="viewStatusButton" onclick="javascript:viewTestStatus(); return false;" class="rounded {transparent} button"><lb:label key="homepage.button.viewStatus" /></a>
										</div> 
									    <% if (userScheduleAndFindSessionPermission) {%>
										    <div id="scSession" style="float:right;padding-left:5px;">
											<a href="#" id="scSessionButton" onclick="javascript:scheduleNewSession(); return false;" class="rounded {transparent} button"><lb:label key="homepage.button.scheduleSession" /></a>
										    </div> 
									    <% } %>	
									    <div id="printTicket" style="float:right;padding-left:5px;">
											<a href="#" id="printTicketButton" onclick="printTTicket(this);" class="rounded {transparent} button"><lb:label key="homepage.button.printTicket" /></a>
										</div> 	
									    <% if (userScheduleAndFindSessionPermission) {%>
										<div id="copySession" style="float:right;padding-left:5px;display: block;">
											<a href="#" id="copySessionButton" onclick="javascript:copyTestSession(this); return false;" class="rounded {transparent} button"><lb:label key="homepage.button.copySession" /></a>
										</div>
									    <% } %>	
										<% if (showModifyManifest) {%>
										    <div id="mStdManifest" style="float:right;padding-left:5px; display: block;">
											    <a href="#" id="modifyStdManifestButton" onclick="javascript:openModifyStdManifestPopup(this); return false;" class="rounded {transparent} button" >
											   		<lb:label key="homepage.button.modifyTest" />
											    </a>
											</div> 
									    <% } %>	
									</div>  
								</div>
				     	   </td>
					   	</tr>
				</table>
				
				<div style="float:left;width:1200px;padding-left:2px;">
				<%if(userScheduleAndFindSessionPermission) { %>
					<div id="show" style="display: block;width:25px;float:left; padding: 3px 0 3px 3px;" class="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br ui-widget-header " title="${bundle.web['homepage.icon.showOrganization']}">
			   			<a id="showTreeSliderID" href="#" onclick="showTreeSlider();" style=" width:100%; " >>></a>
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
			</td>
		</tr>
	</table>				
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
			<input type="button"  value="Clear" onclick="javascript:resetSearchList2(); return false;" class="ui-widget-header">&nbsp;
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
<div id="accommodationToolTip" style="display:none;" class="statusLegendDiv">
<table border="0" class="statusLegendTable"  cellspacing="0">
	<tbody>
		<tr align="center" id="screenReaderStatus" style="display: none;">
			<td align="left" style="padding-top:5px;"><lb:label key="student.accommodationToolTip.screenReader" /></td>
		</tr>
		<tr align="center" id="calculatorStatus" style="display: none;">
			<td align="left"><lb:label key="student.accommodationToolTip.calculator" /></td>
		</tr>
		<tr align="center" id="testPauseStatus" style="display: none;">
			<td align="left"><lb:label key="student.accommodationToolTip.testPause" /></td>
		</tr>
		<tr align="center" id="untimedTestStatus" style="display: none;">
			<td align="left"><lb:label key="student.accommodationToolTip.untimedTest" /></td>
		</tr>
		<tr align="center" id="highLighterStatus" style="display: none;">
			<td align="left"><lb:label key="student.accommodationToolTip.highlighter" /></td>
		</tr>
		<tr align="center" id="maskingRularStatus" style="display: none;">
			<td align="left"><lb:label key="student.accommodationToolTip.maskingRuler" /></td>
		</tr>
		<tr align="center" id="magnifyingGlassStatus" style="display: none;">
			<td align="left"><lb:label key="student.accommodationToolTip.magnifyingGlass" /></td>
		</tr>
		<tr align="center" id="hasColorFontAccommodationsStatus" style="display: none;">
			<td align="left"><lb:label key="student.accommodationToolTip.colorNFont" /></td>
		</tr>
		<tr align="center" id="auditoryCalmingStatus" style="display: none;">
			<td align="left"><lb:label key="student.accommodationToolTip.auditoryCalming" /></td>
		</tr>
		<tr align="center" id="extendedTimeAccomStatus" style="display: none;">
			<td align="left"><lb:label key="student.accommodationToolTip.extendedTime" /></td>
		</tr>
		<tr align="center" id="maskingToolStatus" style="display: none;">
			<td align="left"><lb:label key="student.accommodationToolTip.maskingTool" /></td>
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
	<div style="padding-top:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="sessionList.no.data.selected"/>
		</div>
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

<jsp:include page="/sessionOperation/schedule_session.jsp" />
<jsp:include page="/sessionOperation/duplicate_student.jsp" />
<jsp:include page="/sessionOperation/view_test_session.jsp" />
<jsp:include page="/sessionOperation/restricted_student.jsp" />
<jsp:include page="/sessionOperation/editSubtest.jsp" />

<jsp:include page="/sessionOperation/edit_student_manifest_main.jsp" />