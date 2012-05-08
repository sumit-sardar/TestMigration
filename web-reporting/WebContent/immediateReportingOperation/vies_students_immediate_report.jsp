<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="immediateReportingResources" />

<div id="immdRptScorePopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
<br />
	<div class="roundedMessage">
		<table class="transparent" width="100%">
		
			<tr class="transparent">
				<td class="transparent"><lb:label key="immediate.report.stdName" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="stdNameVal" styleClass="formValueLarge"> </span></div>
				</td>
			</tr>
		
			<tr class="transparent">
				<td class="transparent"><lb:label key="immediate.report.Id" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="idVal" styleClass="formValueLarge"> </span></div>
				</td>
			</tr>
			<tr class="transparent">
				<td class="transparent"><lb:label key="immediate.report.TestDate" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="testDateVal" styleClass="formValueLarge"> </span></div>
		
				</td>
			</tr>
			<tr class="transparent">
				<td class="transparent"><lb:label key="immediate.report.Form" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="formVal" styleClass="formValueLarge"> </span></div>
				</td>
			</tr>
			<tr class="transparent">
				<td class="transparent"><lb:label key="immediate.report.District" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="districtVal" styleClass="formValueLarge"> </span></div>
				</td>
			</tr>
			<tr class="transparent">
				<td class="transparent"><lb:label key="immediate.report.School" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="schoolVal" styleClass="formValueLarge"> </span></div>
		
				</td>
			</tr>
			<tr class="transparent">
				<td class="transparent"><lb:label key="immediate.report.Grade" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="gradeVal" styleClass="formValueLarge"> </span></div>
		
				</td>
			</tr>
			<tr class="transparent">
				<td class="transparent"><lb:label key="immediate.report.TestName" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="testNameVal" styleClass="formValueLarge"> </span></div>
		
				</td>
			</tr>
		
		</table>
		
		<br/>
		<table>
			<tr>
				<td width="50px">
				</td>
				<td>
					<div id="immdRptScoreTable" style="height: 223">	</div>
				</td>
				</tr>
		</table>
		<br/>
	    <%-- For button --%>
		
		<div id="generate_csv_pop" style="float:left; padding-right: 15px;">
				<a href="#" id="generateCSVButtonPop" onclick="return downloadImmediateCSVReport(this);" class="rounded {transparent} button" style="color: #0000FF !important;"><lb:label key="immediate.report.generate.csv.button.value" /></a>
		</div>
		<div id="generate_pdf_pop" style="float:left; padding-right: 5px;">
			<a href="#" id="generatePDFButtonPop" onclick="return downloadImmediatePDFReport(this);" class="rounded {transparent} button" style="color: #0000FF !important;"><lb:label key="immediate.report.generate.pdf.button.value" /></a>
		</div>
		<BR/>
		<BR/>

	</div>
	<BR/>
	<div style="padding-bottom: 5px;">
	<center><input type="button" value=<lb:label key="common.button.ok" prefix="'" suffix="'"/>
		onclick="closePopUp('immdRptScorePopup');" class="ui-widget-header"></center>
	</div>
</div>