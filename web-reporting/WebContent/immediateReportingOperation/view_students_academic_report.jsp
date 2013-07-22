<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="immediateReportingResources" />

<div id="immdRptScorePopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
<br />
	<div id="parent" class="roundedMessage">
		<table id="table1" class="transparent" width="100%">
			<tr class="transparent"> <td width="5"> </td> <td colspan="3" class="transparent"> </td></tr>
		    <tr class="transparent">
		       <td width="15px"> </td>
		       <td colspan="3"> <h1>
		       		<lb:label key="immediate.report.view.report.titleAcademic" /></h1>
		       </td>
		    </tr>
		    <tr class="transparent"> <td width="5"> </td> <td colspan="3" class="transparent"> </td></tr>
			<tr class="transparent">
				<td width="15px"> </td>
				<td class="transparent"><lb:label key="immediate.report.stdName" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="stdNameVal" styleClass="formValueLarge"> </span></div>
				</td>
			</tr>
		
			<tr class="transparent">
				<td width="15px"> </td>
				<td class="transparent"><lb:label key="immediate.report.Id" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="idVal" styleClass="formValueLarge"> </span></div>
				</td>
			</tr>
			<tr class="transparent">
				<td width="15px"> </td>
				<td class="transparent"><lb:label key="immediate.report.TestDate" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="testDateVal" styleClass="formValueLarge"> </span></div>
		
				</td>
			</tr>
			<tr class="transparent">
				<td width="15px"> </td>
				<td class="transparent"><lb:label key="immediate.report.Form" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="formVal" styleClass="formValueLarge"> </span></div>
				</td>
			</tr>
			<tr class="transparent">
				<td width="15px"> </td>
				<td class="transparent"><lb:label key="immediate.report.District" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="districtVal" styleClass="formValueLarge"> </span></div>
				</td>
			</tr>
			<tr class="transparent">
				<td width="15px"> </td>
				<td class="transparent"><lb:label key="immediate.report.School" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="schoolVal" styleClass="formValueLarge"> </span></div>
		
				</td>
			</tr>
			<tr class="transparent">
				<td width="15px"> </td>
				<td class="transparent"><lb:label key="immediate.report.Grade" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="gradeVal" styleClass="formValueLarge"> </span></div>
		
				</td>
			</tr>
			<tr class="transparent">
				<td width="15px"> </td>
				<td class="transparent"><lb:label key="immediate.report.TestName" /></td>
				<td class="transparent">
				<div class="formValueLarge"><span id="testNameVal" styleClass="formValueLarge"> </span></div>
		
				</td>
			</tr>
		
		</table>
		
		<br/>
		<table id="table2">
			<tr>
				<td width="10px">
				</td>
				<td>
					<div id="AcademicScoreTable" style="height: 375">	</div>
				</td>
				</tr>
		</table>
		<br/>
	    <%-- For button --%>
		
		<div id="generate_csv_pop" style="float:left; padding-left: 25px;">
				<a href="#" id="generateCSVButtonPop" onclick="javascript:downloadAcademicLanguageCVSReport(this);" class="rounded {transparent} button" style="color: #0000FF !important;"><lb:label key="immediate.report.generate.excel.button.value" /></a>
		</div>
		<div id="generate_pdf_pop" style="float:left; padding-left: 5px;">
			<a href="#" id="generatePDFButtonPop" onclick="javascript:downloadAcademicLanguagePDFReport(this);" class="rounded {transparent} button" style="color: #0000FF !important;"><lb:label key="immediate.report.generate.pdf.button.value" /></a>
		</div>
		<BR/>
		<BR/>

	</div>
	<BR/>
	<div style="padding-bottom: 5px;">
	<center><input type="button" value=<lb:label key="common.button.ok" prefix="'&nbsp;&nbsp;&nbsp;" suffix="&nbsp;&nbsp;&nbsp;'"/>
		onclick="closePopUp('immdRptScorePopup');" class="ui-widget-header"></center>
	</div>
</div>