<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%
    String studentIdLabelName = (String)request.getAttribute("studentIdLabelName");
	Boolean supportAccommodations = (Boolean)request.getAttribute("supportAccommodations");
	
%>

<input type="hidden" id="studentIdLabelName"  value ='Student ID' />
<input type="hidden" id="supportAccommodations" value = '<%=supportAccommodations %>' />
<input type="hidden" id="noStudentTitle" name = "noStudentTitle" value=<lb:label key="student.noStuSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentMsg" name = "noStudentMsg" value=<lb:label key="student.noStuSelected.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="filterNoStuSelected" name = "filterNoStuSelected" value=<lb:label key="student.filterNoStuSelected.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="accomStuGrid" name="accomStuGrid" value=<lb:label key="stu.label.list" prefix="'" suffix="'"/>/>
<input type="hidden" id="selectAccomPopup" name="selectAccomPopup" value=<lb:label key="stu.label.accomPopup" prefix="'" suffix="'"/>/>
<input type="hidden" id="confirmAlert" name="confirmAlert" value=<lb:label key="stu.label.confirmAlert" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgLastNameID" name="jqgLastNameID" value=<lb:label key="stu.info.lastName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgFirstNameID" name="jqgFirstNameID" value=<lb:label key="stu.info.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgMiddleIniID" name="jqgMiddleIniID" value=<lb:label key="stu.label.mi" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgGradeID" name="jqgGradeID" value=<lb:label key="stu.info.grade" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuAssEditAccoID" name="stuAssEditAccoID" value=<lb:label key="stu.label.editAcco" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuAssNoAccoID" name="stuAssNoAccoID" value=<lb:label key="stu.msg.noAcco" prefix="'" suffix="'"/>/>
	
<table class="transparent" width="97%" style="margin:15px auto;"> 
	<tr class="transparent">
        <td>
		<table class="transparent">
			<tr class="transparent">
				<td>
		    		<h1><lb:label key="stu.stuAccom.title" /></h1>
				</td>
			</tr>
			<tr> 
				<td class="subtitle">  
					<lb:label key="stu.msg.AssignAccomText" />
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
						<div id="displayMessageMain" class="errMsgs" style="display:none; width: 50%; float: left;"> 
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
						<div id="viewStatus" style="float:right;visibility:hidden;">
							<a href="#" id="assignAccommButton" onclick="javascript:openAssignAccommPopup(this); return false;" class="rounded {transparent} button"><lb:label key="stu.label.assignAcom" /></a>
						</div>
						<div style="clear:both;"></div>
					</td>					
				</tr>
				<tr class="transparent">
				
			        <td style="vertical-align:top; width:16%;" align="left">
				      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="stu.label.search" /></div>
				    	<script>populateBulkAccommTree();</script>
				    	<div id="outertreebgdiv" class="treeCtrl" style="height:568px !important">
					    	<div id="studentBulkOrgNode" style="width:auto;height:auto;display:table">
							</div>
						</div> 					
				 	</td>
				 	
	 				<td class="transparent" width="5px">&nbsp;</td>
	 				
		 			<td style="vertical-align:top;" id="jqGrid-content-section" align="left">
	      				<table id="studentAccommGrid" class="gridTable"></table>
						<div id="studentAccommpager" class="gridTable"></div>			
		 			</td>
		 			
	    		</tr>
			</table>
        </td>
    </tr>
</table>

				
<div id="AssignAccommPopup"
	style="width:98%; display: none; border:8px solid #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table width="100%">
		<tr><td>
			<div id="displayPopupMessage" style="display:none; border:1px solid #D4ECFF; padding:2px;"> 
				<table>
					<tr>
						<td valign="top" width="18">
	                   	<img  id="errorImgPopup"  src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
						</td>
						<td style="font-size: 12px; font-weight: bold;" valign="top">
						<div id= "contentBulkPopup"></div>		
						</td>
					</tr>
				</table>
		</div>
		</td></tr>
		<tr>
			<td colspan="2">
			<br/>
				<%@include file="/bulkOperation/bulkAccommodationSelection.jsp"%>
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value=<lb:label key="common.button.reset" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:resetAccommpopup(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value=<lb:label key="common.button.submit" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:saveBulkStudentData(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:cancelAssignAccom(); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>

<div id="unSaveConfirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="stu.msg.assignAccomConfirm" />
		</div>
	</div>
	<div style="padding:10px;">		
		<center>
			<input type="button"  value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closeUnsaveConfirmationPopup(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUp('unSaveConfirmationPopup'); return false;" class="ui-widget-header">
		</center>
	</div>	
</div>

<DIV style="Z-INDEX: 1051; BORDER-BOTTOM: medium none; POSITION: fixed; opacity:0.5;FILTER:  alpha(opacity=50); BORDER-LEFT: medium none; PADDING-BOTTOM: 0px; BACKGROUND-COLOR: #aaaaaa; MARGIN: 0px; PADDING-LEFT: 0px; WIDTH: 100%; PADDING-RIGHT: 0px; ZOOM: 1; HEIGHT: 100%; BORDER-TOP: medium none; TOP: 0px; CURSOR: wait; BORDER-RIGHT: medium none; PADDING-TOP: 0px; LEFT: 0px; display:none;" class="blockUI blockOverlay"></DIV>

<DIV style="Z-INDEX: 1062; BORDER-BOTTOM: 0px; POSITION: fixed; TEXT-ALIGN: center; FILTER:  alpha(opacity=50); BORDER-LEFT: 0px; PADDING-BOTTOM: 0px; BACKGROUND-COLOR: #aaaaaa; MARGIN: 0px; PADDING-LEFT: 0px; WIDTH: 0px; PADDING-RIGHT: 0px; ZOOM: 1; COLOR: #000; BORDER-TOP: 0px; TOP: 202px; BORDER-RIGHT: 0px; PADDING-TOP: 0px; LEFT: 607px;display:none;" class="blockUI blockMsg blockPage"><IMG src="/SessionWeb/resources/images/loading.gif"/></DIV>