<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="dataExportApplicationResource" />
<div id="submitJobTop" style="display: none;font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; margin-bottom:5px; padding:2px 2px 2px 10px; color: black;">
		<lb:label key="data.export.submitJobResult"/>
</div>
<div id="session_student_for_export" style="">

		<%
			Integer frameworkProductId = Integer.parseInt(request.getAttribute("frameworkProductId").toString());
			Boolean isFormA = Boolean.parseBoolean(request.getAttribute("isFormA").toString());
			Boolean isFormB = Boolean.parseBoolean(request.getAttribute("isFormB").toString());
			Boolean isEspA = Boolean.parseBoolean(request.getAttribute("isEspA").toString());
			Boolean isFormC = Boolean.parseBoolean(request.getAttribute("isFormC").toString());
			Boolean isEspB = Boolean.parseBoolean(request.getAttribute("isEspB").toString());
		%>
	
	<table id="session_student_for_export_table">
	<!--  Steps 1 Starts Here -->
	<tr id="data_export_step1" style="display: none">
		<td width="10px;"> </td>
		<td class="transparent"><div id="step1msg"><lb:label key="data.export.step1.message" /> </div>
			<BR />
			<b><lb:label key="data.export.formSelect.message" /></b>
			&nbsp;&nbsp;<select id="formSelect" onchange="return getStudentList();">
			<% if(frameworkProductId==7000) { %>
				<% if(isFormA) { %>
					<option id="7001" value="7001">Form A</option>
				<% } %>
				<% if(isFormB) { %>
					<option id="7002" value="7002">Form B</option>
				<% } %>
				<% if(isEspA) { %>
					<option id="7003" value="7003">Form Espa&#xf1;ol A</option>
				<% } %>
			<% } else if(frameworkProductId==7500) { %>
				<% if(isFormC) { %>
					<option id="7501" value="7501">Form C</option>
				<% } %>
				<% if(isEspB) { %>
					<option id="7502" value="7502">Form Espa&#xf1;ol B</option>
				<% } %>
			<% } %>
</select>
			<BR />
			<BR />
				<div id="data_export_session_student_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
					<table id="to_be_export_student_list" class="gridTable"></table>
					<div id="to_be_export_student_list_pager" class="gridTable"></div>
					<BR />
					<lb:label key="data.export.refresh.message" /> <BR /><BR />
					<input id ="dataExportNextButton" type="button"  class="ui-widget-header" value=<lb:label key="data.export.title.next" prefix="'&nbsp;&nbsp;&nbsp;" suffix="&nbsp;&nbsp;&nbsp;'" /> onClick="getUnscoredStudentDetails('false'); return false;"/>
					<input id ="dataExportBySession" type="button"  class="ui-widget-header" value="Export by Session" onClick="getUnscoredStudentDetails('true'); return false;"/>
					<input id ="dataExportRefresh" type="button"  class="ui-widget-header" value="Refresh" onClick="refreshStudentList(); return false;"/>
				</div>
			<BR />
		</td>
	</tr>
	<!--  Steps 1 ends Here -->
	
	<!--  Steps 2 Starts Here -->
	<tr height="30px" id="data_export_step2" style="display: none;>
	
	</tr>
	<tr id="data_export_step2" style="display: none;">
		<td width="10px;"> </td>
		
		<td class="transparent"><lb:label key="data.export.step2.message" /> <BR />
			<BR />
				<div id="dataExportDetailsLbl">
					<table style="margin-bottom: 10px; width: 924px;">
						<tr>
							<td class="transparent"><lb:label key="student.being.exported" /></td>
							<td class="transparent">
								<div class="formValueLarge">
								<span class="formValueLarge" id="studentBeingExptd"></span>
								</div>
							</td>
						</tr>
						<tr>
							<td class="transparent"><lb:label key="incomeple.scored.student" /></td>
							<td class="transparent">
								<div class="formValueLarge">
									<span class="formValueLarge" id="incScoredStudent"></span>
								</div>
							</td>
						</tr>
					</table>
				</div>
				<div id="data_export_scoring_incomplete_student_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
					<table id="data_export_scoring_incomplete_student_list" class="gridTable"></table>
					<div id="data_export_scoring_incomplete_student_list_pager" class="gridTable"></div>
					<BR />
					<%--  <input id ="dataExportNextButton" type="button"  class="ui-widget-header" value=<lb:label key="data.export.title.next" prefix="'&nbsp;&nbsp;&nbsp;" suffix="&nbsp;&nbsp;&nbsp;'" /> onClick="getUnscoredStudentDetails(); return false;"/>
					--%>
				</div>
			<BR />
		</td>
	</tr>
	<!--  Steps 2 ends Here -->
	<!-- Step 3 starts Here -->
	<tr id="data_export_step3" style="display: none;">
		<td width="10px;"> </td>
		
		<td class="transparent"><lb:label key="data.export.step3.message" /> <BR />
			<BR />
			<div id="dataExportSummaryLbl">
					<table style="margin-bottom: 10px; width: 924px;">
						<tr>
							<td class="transparent"><lb:label key="student.being.exported" /></td>
							<td class="transparent">
							<div class="formValueLarge">
							<span class="formValueLarge" id="studentBeingExptdStep3"></span>
							</div>
							</td>
						</tr>
						<tr>
							<td class="transparent"><lb:label key="total.student.scheduled" /></td>
							<td class="transparent">
							<div class="formValueLarge">
							<span class="formValueLarge" id="scheduledStudent"></span>
							</div>
							</td>
						</tr>
						<BR />
						<tr>
							<td class="transparent"><lb:label key="total.student.not.taken" /></td>
							<td class="transparent">
							<div class="formValueLarge">
							<span class="formValueLarge" id="notTakenStudent"></span>
							</div>
							</td>
						</tr>
						<BR />
						<tr>
							<td class="transparent"><lb:label key="total.student.not.complete" /></td>
							<td class="transparent">
							<div class="formValueLarge">
							<span class="formValueLarge" id="notCompleteStudent"></span>
							</div>
							</td>
						</tr>
						<BR />
						<tr id="jobIdDisplay" style="display: none;">
								<td class="transparent"><lb:label key="dataexport.info.jobid" /></td>
								<td class="transparent">
								<div class="formValueLarge">
								<span class="formValueLarge" id="jobId"></span>
								</div>
								</td>
						</tr>
						
					</table>
			</div>
			<BR />
			<input id ="dataExportSubmitButton" type="button"  class="ui-widget-header" value=<lb:label key="data.export.title.submit" prefix="'&nbsp;&nbsp;&nbsp;" suffix="&nbsp;&nbsp;&nbsp;'" /> onClick="openConfirmationPopupSubmit(); return false;"/>
			<div id="submitJobBottom" style="display: none;font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; margin-bottom:5px; padding:2px 2px 2px 10px; color: black;">
				<lb:label key="data.export.submitJobResult"/>
			</div>
			<BR />
			<div id="dataExportViewButton" style="display:none;">
			<input id ="dataExportViewButton" type="button"  class="ui-widget-header" value=<lb:label key="data.export.title.view.status" prefix="'&nbsp;&nbsp;&nbsp;" suffix="&nbsp;&nbsp;&nbsp;'" /> onClick="gotoMenuAction('services.do', 'viewStatusLink');"/>
			</div>
		</td>
	</tr>
	
	<!-- Steps 3 ends Here -->
	</table>
	<div id="confirmationPopupOnSubmit"	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="data.export.onsubmit.warning.message" />
		</div>
	</div>
	<div style="padding:10px;">	
		<center>
			<input type="button"  value="&nbsp;Yes&nbsp;" onclick="submitJobDetails(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="closePopUp('confirmationPopupOnSubmit'); return false;" class="ui-widget-header">
		</center>
	</div>	
	
</div>
</div>
	