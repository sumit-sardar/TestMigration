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
					<p style="color:#000"><lb:label key="tabe.bulk.state.report.msg" /></p> 
					<br/>		
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
							style="width: 104px; color: black; display: none;" onchange="javascript: getOrgListForBulkReport();"></select></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>

		<table id="displayMessageBulkReport" style="display: none;">
			<tbody>
				<tr>
					<td collspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td width="18">
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="18" valign="top" rowspan="3">
						<img width="16" height="16" border="0" src="/SessionWeb/resources/images/messaging/icon_error.gif">&nbsp;&nbsp;
					</td>
					<td valign="top">
						<table>
							<tbody>
								<tr>
									<td><font style="color: red; font-size:12px; font-weight:bold"><div id="messageBulkReport"></div></font></td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>



		<table width="65%" height="40%" style="margin-left:10px; padding: 5px; auto;" border="0">
			<tr class="transparent">
				<td>
				<div id="export_Report" style="float: right; padding-right: 5px; padding-top: 5px;"><a href="#"
					id="stateExportBulkReport" onclick="javascript:downloadBulkReportCSV(this);" class="rounded {transparent} button"><lb:label
					key="bulk.state.report.exportButton.title" /></a></div>
				</td>
			</tr>
			<tr class="transparent">
				<td style="padding: 5px;">
				<div id="exportCriteriaBulkReport"><jsp:include page="bulk_state_report.jsp" /></div>
				</td>

			</tr>

		</table>
		<input type="hidden" id="dateFlagBulkReport" name="dateFlagBulkReport" />
		<input type="hidden" id="startDtBulkReport" name="startDtBulkReport" />
		<input type="hidden" id="endDtBulkReport" name="endDtBulkReport" />
		<input type="hidden" id="orgArrBulkReport" name="orgArrBulkReport" />
	</netui:form>
	
	<script type="text/javascript">
			$(document).ready( 
				function(){ setMenuActive("reports", "reportsLink"); }				
			);
			
			$("#startDateBulkReport").datepicker({
					inline: true,
					clickInput:true,
					maxDate: 'today',
					dateFormat: 'mm/dd/y'				

				});
				
			$("#endDateBulkReport").datepicker({
				inline: true,
				clickInput:true,
				maxDate: 'today',
				dateFormat: 'mm/dd/y'					
			});
			
			var d = populateDataOptionsBulkReport();
			// console.log('received: ' + d);
			$("#startDateBulkReport").datepicker("setDate", new Date(d.getFullYear(), d.getMonth() - 1, d.getDate()));
			$("#endDateBulkReport").datepicker("setDate", d);
		</script>
	
	</netui-template:section>
</netui-template:template>
