
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />

<% String studentIdLabelName = (String)request.getAttribute("studentIdLabelName");
%>

<input type="hidden" id="stuCountId" value=<lb:label key="scoring.msg.orgStuCount" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdLoginId" value=<lb:label key="scoring.stuGrid.loginId" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdStdName" value=<lb:label key="scoring.stuGrig.stdName" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdGroup" value=<lb:label key="scoring.common.group" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdGrade" value=<lb:label key="scoring.stuGrig.grade" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdGender" value=<lb:label key="scoring.stuGrig.gender" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdSessionName" value=<lb:label key="scoring.common.sessionName" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdTestName" value=<lb:label key="scoring.common.testName" prefix="'" suffix="'"/>/>
<input type="hidden" id="studentIdLabelName"  value = '<%=studentIdLabelName %>' />
<input type="hidden" id="noStudentTitle" value=<lb:label key="scoring.noStuSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentMsg" value=<lb:label key="scoring.noStuSelected.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGridCaption" value=<lb:label key="scoring.stuGrig.caption" prefix="'" suffix="'"/>/>
<input type="hidden" id="gridShowBy" value=<lb:label key="scoring.common.showBy" prefix="'" suffix="'"/>/>
<input type="hidden" id="gridShowByStu" value=<lb:label key="scoring.showBy.student" prefix="'" suffix="'"/>/>
<input type="hidden" id="gridShowBySes" value=<lb:label key="scoring.showBy.session" prefix="'" suffix="'"/>/>
<input type="hidden" id="sesGridCaption" value=<lb:label key="scoring.sesGrid.caption" prefix="'" suffix="'"/>/>
<input type="hidden" id="sesGridMyRole" value=<lb:label key="scoring.sesGrid.myRole" prefix="'" suffix="'"/>/>
<input type="hidden" id="sesGridStatus" value=<lb:label key="scoring.sesGrid.status" prefix="'" suffix="'"/>/>
<input type="hidden" id="sesGridStartDate" value=<lb:label key="scoring.sesGrid.startDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="sesGridEndDate" value=<lb:label key="scoring.sesGrid.endDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="noSessionTitle" value=<lb:label key="scoring.noSesSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noSessionMessage" value=<lb:label key="scoring.noSesSelected.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="confirmAlrt" value=<lb:label key="scoring.alert.confirm" prefix="'" suffix="'"/>/>
<input type="hidden" id="scorPopupTitle" value=<lb:label key="scoring.menu.scoring" prefix="'" suffix="'"/>/>
<input type="hidden" id="sessionScorPopupTitle" value=<lb:label key="scoring.menu.scoring" prefix="'" suffix="'"/>/>
<input type="hidden" id="searchStudentSession" value=<lb:label key="common.label.search" prefix="'" suffix="'"/>/>
<input type="hidden" id="scoringPopupTitle" value=<lb:label key="scoring.page.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemListGripCap" value=<lb:label key="scoring.itemGrid.caption" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripItemNo" value=<lb:label key="scoring.itemGrid.ItemNo" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripSubtest" value=<lb:label key="scoring.itemGrid.subtestName" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripScoreItm" value=<lb:label key="scoring.itemGrid.scoreItem" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripManual" value=<lb:label key="scoring.itemGrid.manualStatus" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripMaxScr" value=<lb:label key="scoring.itemGrid.maxScore" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemGripObtained" value=<lb:label key="scoring.itemGrid.scorObtn" prefix="'" suffix="'"/>/>
<input type="hidden" id="sbsGridFirstName" value=<lb:label key="dialog.myProfile.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="sbsGridLastName" value=<lb:label key="dialog.myProfile.lastName" prefix="'" suffix="'"/>/>
<input type="hidden" id="sbsGridOnStatus" value=<lb:label key="scoring.itemGrid.onTestStatus" prefix="'" suffix="'"/>/>
<input type="hidden" id="sbsEmptyGrid" value=<lb:label key="scoring.sbs.noStuFound.message" prefix="'" suffix="'"/>/>

<table class="transparent" width="97%" style="margin:15px auto;"> 
	<tr class="transparent">
		<td>
    		<table class="transparent">
				<tr class="transparent">
					<td>
			    		<h1><lb:label key="scoring.page.title" /></h1>
					</td>
				</tr>
				<tr> 
					<td class="subtitle">  
						<lb:label key="scoring.page.subtitle" />
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
									<div id= "contentMain"></div>
								</td>
							</tr>
						</table>
					</div>	       			
					<div id="score" style="float:right;visibility:hidden;">
						<a href="#" id="scoreButton" onclick="displayListPopup(this); return false;" class="rounded {transparent} button"><lb:label key="scoring.button.value" /></a>
					</div> 
					<div style="clear:both;">
				</td>
		   	</tr>
	    	<tr class="transparent">
		        <td style="vertical-align:top; width:16%;" align="left">
			      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="scoring.label.tree.search" /></div>
			    	<script>populateStudentScoringTree()</script>
			
			    	<div id="outertreebcgdiv" class="treeCtrl" style="height:512px !important">
				    	<div id = "scoringOrgNode" style="width:auto;height:auto;display:table">
						</div>
					</div>
			 	</td>
			 	
	 			<td class="transparent" width="5px">&nbsp;</td>
		 	
		 		<td style="vertical-align:top;" id="jqGrid-content-section">
		 			<div id="studentView">
		      			<table id="studentScoringGrid" class="gridTable"></table>
						<div id="studentScoringPager" class="gridTable"></div>
					</div>
					<div id="sessionView">
						<table id="sessionScoringGrid" class="gridTable"></table>
						<div id="sessionScoringPager" class="gridTable"></div>
					</div>
		 		</td>
	    	</tr>
		</table>
        </td>
    </tr>
</table>

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
			<lb:label key="scoring.topNodeSelection.message" />
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

<jsp:include page="/studentScoringOperation/find_student_scoring.jsp" />
<jsp:include page="/studentScoringOperation/session_scoring.jsp" />