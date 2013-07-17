
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="immediateReportingResources" />

<% 
	String studentIdLabelName = (String)request.getAttribute("studentIdLabelName");
	Integer productId = Integer.parseInt(request.getAttribute("productId").toString());
%>
<input type="hidden" id="stuCountId" value=<lb:label key="immediate.report.msg.orgStuCount" prefix="'" suffix="'"/>/>
<input type="hidden" id="studentIdLabelName"  value = '<%=studentIdLabelName %>' />
<input type="hidden" id="productId" value='<%=productId %>' />



<input type="hidden" id="imdRptStuListGridCaption" value=<lb:label key="immediate.report.stu.grid.caption" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdLoginId" value=<lb:label key="immediate.report.stuGrig.loginId" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdStdName" value=<lb:label key="immediate.report.stuGrig.stdName" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdGroup" value=<lb:label key="immediate.report.stuGrig.teacherName" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuGrdGrade" value=<lb:label key="immediate.report.stuGrig.grade" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdSessionName" value=<lb:label key="immediate.report.stuGrig.sessionName" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdTestName" value=<lb:label key="immediate.report.stuGrig.testName" prefix="'" suffix="'"/>/>
<input type="hidden" id="contentArea" value=<lb:label key="immediate.report.stuGrig.contentArea" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentTitle" value=<lb:label key="immediate.report.noStuSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentMsg" value=<lb:label key="immediate.report.noStuSelected.message" prefix="'" suffix="'"/>/>

<input type="hidden" id="immdRptTabHeaderRawScore" value=<lb:label key="immediate.report.score.table.header.rawscore" prefix="'" suffix="'"/>/>
<input type="hidden" id="immdRptTabHeaderScaleScore" value=<lb:label key="immediate.report.score.table.header.scalescore" prefix="'" suffix="'"/>/>
<input type="hidden" id="immdRptTabHeaderProficiencyLevel" value=<lb:label key="immediate.report.score.table.header.proficiencylevel" prefix="'" suffix="'"/>/>

<input type="hidden" id="immdRptTabHeaderSpeaking" value=<lb:label key="immediate.report.score.table.header.speaking" prefix="'" suffix="'"/>/>
<input type="hidden" id="immdRptTabHeaderListening" value=<lb:label key="immediate.report.score.table.header.listening" prefix="'" suffix="'"/>/>
<input type="hidden" id="immdRptTabHeaderReading" value=<lb:label key="immediate.report.score.table.header.reading" prefix="'" suffix="'"/>/>
<input type="hidden" id="immdRptTabHeaderWriting" value=<lb:label key="immediate.report.score.table.header.writing" prefix="'" suffix="'"/>/>

<input type="hidden" id="immdRptTabHeaderPtsPossible" value=<lb:label key="immediate.report.score.table.header.PtsPossible" prefix="'" suffix="'"/>/>
<input type="hidden" id="immdRptTabHeaderPtsObtained" value=<lb:label key="immediate.report.score.table.header.PtsObtained" prefix="'" suffix="'"/>/>
<input type="hidden" id="immdRptTabHeaderPerCorrect" value=<lb:label key="immediate.report.score.table.header.PerCorrect" prefix="'" suffix="'"/>/>
<!--<input type="hidden" id="immdRptTabHeaderTotalSpeaking" value=<lb:label key="immediate.report.score.table.header.TotalSpeaking" prefix="'" suffix="'"/>/>
<input type="hidden" id="immdRptTabHeaderTotalListening" value=<lb:label key="immediate.report.score.table.header.TotalListening" prefix="'" suffix="'"/>/>
<input type="hidden" id="immdRptTabHeaderTotalReading" value=<lb:label key="immediate.report.score.table.header.TotalReading" prefix="'" suffix="'"/>/>
<input type="hidden" id="immdRptTabHeaderTotalWriting" value=<lb:label key="immediate.report.score.table.header.TotalWriting" prefix="'" suffix="'"/>/>
--><input type="hidden" id="immdRptTabTotalScoreMsg" value=<lb:label key="immediate.report.academic.total.score" prefix="'" suffix="'"/>/>

<input type="hidden" id="immdRptTabSearchPopupTitle" value=<lb:label key="common.label.search" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdForm" value=<lb:label key="immediate.report.stuGrig.form" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdAdministrationDate" value=<lb:label key="immediate.report.stuGrig.administrationDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdCreatedBy" value=<lb:label key="immediate.report.stuGrig.createdBy" prefix="'" suffix="'"/>/>


<table class="transparent" width="97%" style="margin:15px auto;"> 
	<tr class="transparent">
		<td>
    		<table class="transparent">
				<tr class="transparent">
					<td>
			    		<h1><lb:label key="immediate.report.page.titleSecond" /></h1>
					</td>
				</tr>
				<tr> 
					<td class="subtitle">  
						<lb:label key="immediate.report.page.subtitleSecond" />
					</td>	
				</tr>
			</table>		
		</td>
	</tr>
    <tr class="transparent">
        <td align="center">        
		<table width="100%" >
		   	
		   	<tr >
		   		<td colspan="3" class="buttonsRow" id="buttonRow">
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
					<div id="generate_pdf" style="float:right;visibility:hidden; padding-right: 5px; padding-top: 5px;">
						<a href="#" id="generatePDFButton" onclick="" class="rounded {transparent} button"><lb:label key="immediate.report.generate.pdf.button.value" /></a>
					</div>
					<div id="generate_csv" style="float:right;visibility:hidden;  padding-right: 5px; padding-top: 5px;">
						<a href="#" id="generateCSVButton" onclick="" class="rounded {transparent} button"><lb:label key="immediate.report.generate.excel.button.value" /></a>
					</div>
					<div id="view_report" style="float:right;visibility:hidden; padding-right: 5px; padding-top: 5px;">
						<a href="#" id="viewReportButton" onclick="javascript:viewHtmlReportAcademic(this); return false;" class="rounded {transparent} button"><lb:label key="immediate.report.view.report.button.value" /></a>
					</div>
		   		</td>
		   	
		   	</tr>
	    	<tr class="transparent">
		        <td style="vertical-align:top; width:16%;" align="left">
			      	<div  id= "imdRptOrgTreeHeader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="immediate.report.label.tree.search" /></div>
			    	<script>populateUserOrgHierarchy()</script>
			
			    	<div id="outertreebcgdiv" class="treeCtrl" style="height:535px !important">
				    	<div id = "imdRptOrgTree" style="width:auto;height:auto;display:table">
						</div>
					</div>
			 	</td>
			 	
	 			<td class="transparent" width="5px">&nbsp;</td>
		 	
		 		<td style="vertical-align:top;" id="jqGrid-content-section">
		 			 <div id="studentView" style="float: right;">
		      			<table id="secondImmdRptGrid" class="gridTable"></table>
						<div id="immdRptGridPager" class="gridTable"></div>
					</div>
		 		</td>
	    	</tr>
		</table>
        </td>
    </tr>
</table>
 
<div id="stuThresholdExceedPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;" id="stuThresholdExceedMsg">
		</div>
	</div>
	<div style="padding:10px;">
		<center>		
			<input type="button"  value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/> class="ui-widget-header" onclick="closePopUp('stuThresholdExceedPopup');">
		</center>
	</div>
</div>

<div id="immdRptGridSearhPopup"	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="immediate.report.search.info.message"/></p>
	</div>
	<div class="searchInputBoxContainer" id="searchInputBoxContainer">
		<center>
			<input type="text" name="immdRptGridSearhInputParam" id="immdRptGridSearhInputParam" onkeypress="trapEnterKey(event);"/>
		</center>	
	</div>
	<div style="padding-bottom:20px;"> 
		<center>
			<input type="button"  value=<lb:label key="common.button.clear" prefix="'" suffix="'"/> onclick="javascript:immdRptGridresetSearchForAcademic(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.search" prefix="'" suffix="'"/> onclick="javascript:immdRptGridSearhForAcademic(); return false;" class="ui-widget-header">
		</center>
	</div>
</div>

 <jsp:include page="view_students_academic_report.jsp" /> 
