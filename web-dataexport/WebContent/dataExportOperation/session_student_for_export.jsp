<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="dataExportApplicationResource" />

<div id="session_student_for_export" style="">

	<table class="sortable" id="session_student_for_export_table">
	<!--  Steps 1 Starts Here -->
	<tr id="data_export_step1" style="">
		<td width="10px;"> </td>
		<td class="transparent"><lb:label key="data.export.step1.message" /> <BR />
			<BR />
				<div id="data_export_session_student_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
					<table id="to_be_export_student_list" class="gridTable"></table>
					<div id="to_be_export_student_list_pager" class="gridTable"></div>
					<BR />
					<input id ="dataExportNextButton" type="button"  class="ui-widget-header" value=<lb:label key="data.export.title.next" prefix="'&nbsp;&nbsp;&nbsp;" suffix="&nbsp;&nbsp;&nbsp;'" /> onClick="getUnscoredStudentDetails(); return false;"/>
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
							<td width="135"><lb:label key="student.being.exported" /></td>
							<td><span id="studentBeingExptd"></span></td>
						</tr>
						<tr>
							<td width="135"><lb:label key="incomeple.scored.student" /></td>
							<td><span id="incScoredStudent"></span></td>
						</tr>
					</table>
				</div>
				<div id="data_export_scoring_incomplete_student_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
					<table id="data_export_scoring_incomplete_student_list" class="gridTable"></table>
					<div id="data_export_scoring_incomplete_student_list_pager" class="gridTable"></div>
					<BR />
				</div>
			<BR />
		</td>
	</tr>
	<!--  Steps 2 ends Here -->
	
	</table>
</div>
	