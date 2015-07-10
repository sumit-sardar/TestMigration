<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['reports.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.reports']}"/>
    <netui-template:section name="bodySection">
    <netui:form action="tabeBulkStateReporting">
    <input type="hidden" id="menuId" name="menuId" value="reportsLink" />
    <input type="hidden" id="invalidDatesBulkReport" value=<lb:label key="common.invalidDates.message" prefix="'" suffix="'"/> />
	<table width="75%" border="0" style="margin-left: 15px;"> 
		<tr>
			<td style="padding-left:5px;">
	    		<h1 style="padding-top: 10px;"><lb:label key="tabe.bulk.state.report.link" /></h1>
	    		<br/>
	    		<br/>		
			</td>
		</tr>
		<tr>
			<br/>
		</tr>
	</table>
	<style type="text/css">
		#bulkExportReportBody {height: auto!important;}
		#viewExportStatus {height: auto!important;}
	</style>

	<table width="55%" border="0" style="margin-left: 15px; visibility: hidden;" id="bulkExportReportAcorTable">
	<tr>
	<td style="padding-left:6px;">
      <div id="bulkExportReportAcor" style="width:100%; position:relative;">
		<div id="bulkExportReport" style="position:relative; width:800px;">
		<h3 id="bulkExportReportHeader"><a href="#"><lb:label key="bulk.state.reporting.exportCriteria" /></a></h3>
		<div id="bulkExportReportBody" style="background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;" >
			<table width="75%" height="50%" style="margin-left:10px; padding: 5px; auto;" border="0">
				<tr>
					<td style="padding-left:5px;">
						<p style="color:#000"><lb:label key="tabe.bulk.state.report.msg" /></p>		
					</td>
				</tr>
				<tr class="transparent">
					<td>
					<table class="transparent">
						<tr>
							<td>
							<div id="orgNameOptionsBulkReportLbl" style="padding-left: 5px;color: black; display: none;"><lb:label key="bulk.state.multiple.orgTitle"
								prefix="" suffix="&nbsp;:" /></div>
							</td>
							<td>&nbsp;&nbsp;<select id="orgNameOptionsBulkReport" name="orgNameOptionsBulkReport" width="70px"
								style="width: 104px; color: black; display: none;" onchange="javascript: getOrgListForBulkReport();"></select>
							</td>
						</tr>
					</table>
					</td>
				</tr>				
				<tr class="transparent" height="50px">
					<td style="padding-top : 10px;">
					 <div id="displayMessageBulkReport">
					 	<div id="messageIconErrorBulkReport" style="display: none; float: left;"><img width="16" height="16" border="0" src="/SessionWeb/resources/images/messaging/icon_error.gif">&nbsp;&nbsp;</div>
					 	<div id="messageIconInfoBulkReport" style="display: none; float: left;"><img width="16" height="16" border="0" src="/SessionWeb/resources/images/messaging/icon_info.gif">&nbsp;&nbsp;</div>
					 	<div id="messageBulkReport" style="display: none;"></div>
					 </div>
					<div id="export_Report" style="float: right; padding-right: 5px; padding-top: 5px;"><a href="#"
						id="stateExportBulkReport" onclick="javascript:submitBulkReportRequest(this);" class="rounded {transparent} button"><lb:label
						key="bulk.state.report.exportButton.title" /></a></div>
					</td>
				</tr>
				<tr class="transparent">
					<td style="padding: 5px;"><div id="exportCriteriaBulkReport"><jsp:include page="bulk_state_report.jsp" /></div>
					</td>
				</tr>
			</table>
		</div>
		</div>
		<div id="viewBulkExportReport" style="position:relative; width:800px;">
			<h3 id="viewBulkExportReportHeader"><a href="#"><lb:label key="session.menu.dataExport.2" /></a></h3>
			<div id="viewExportStatus" style="background-color:#FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
				<table width="75%" style="margin-left:10px; padding: 5px; auto;" border="0"> 
					<tr>
						</td>
						<td style="padding-left:5px;"><lb:label key="tabe.bulk.state.report.exportStatus" /> 		
					</tr>
					<tr class="transparent" height="50px">
						<td style="padding-top : 15px;">
							<div id="export_ReportStatus" style="float: right; padding-right: 5px; padding-top: 5px;"><a href="#" id="downloadBulkExportFileButton" onclick="javascript:downloadBulkReportCSV(this);" class="rounded {transparent} buttonDisabled"><lb:label key="bulk.state.report.downloadFileButton.title" /></a></div>
							<input type="hidden" id="downloadBulkExportFileFlag" value="0" />
							<input type="hidden" id="downloadBulkExportFileName" value="" />
						</td>
					</tr>
					<tr> 
						<td style="padding-left:6px;">
							<div id="viewExportedData">
								<table id="viewBulkExportStatusList" class="gridTable"></table>
								<div id="viewBulkExportStatusPager" class="gridTable" ></div>
							</div>								
						</td>
					</tr>
				</table>
			</div>  <!-- end of populate export status grid -->
		</div> <!-- end of div for div style -->
	  </div> <!-- end of div accordion -->
	  </td>
	</tr> <!-- end of tr -->
	  
	<tr>  <!-- next 3 trs have blank spaces -->
		<td class="buttonsRow">
			<div id="displayMessageMain" class="errMsgs" style="display: none; width: 50%; float: left;"></div>
		</td>
	</tr>
	</table>
		<input type="hidden" id="dateFlagBulkReport" name="dateFlagBulkReport" />
		<input type="hidden" id="startDtBulkReport" name="startDtBulkReport" />
		<input type="hidden" id="endDtBulkReport" name="endDtBulkReport" />
		<input type="hidden" id="orgArrBulkReport" name="orgArrBulkReport" />
		<input type="hidden" id="exportRequestId" name="exportRequestId" />
	</netui:form>
	<script type="text/javascript">
		$(document).ready(
			function(){ setMenuActive("reports", "reportsLink"); }				
		);
		
		$("#startDateBulkReport").datepicker({
				inline: true,
				clickInput:true,
				dateFormat: 'mm/dd/y',
				changeMonth: true,
            	changeYear: true
			});
			
		$("#endDateBulkReport").datepicker({
			inline: true,
			clickInput:true,
			dateFormat: 'mm/dd/y',
			changeMonth: true,
            changeYear: true
		});
		populateDataOptionsBulkReport();
		$("#bulkExportReportAcorTable").css("visibility", "visible");
	</script>
				
	</netui-template:section>
</netui-template:template>