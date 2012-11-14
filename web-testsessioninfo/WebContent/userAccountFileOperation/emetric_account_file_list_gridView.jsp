<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="testsessionApplicationResource" />

</div>
<div id="session_student_for_export" style="">
	
	<table id="session_student_for_export_table">
	<!--  Steps 1 Starts Here -->
	<tr id="data_export_step1" style="display: none">
		<td width="10px;"> </td>
		<td class="transparent"><lb:label key="emetricfile.step.message" /> <BR />
			<BR />
				<div id="data_export_session_student_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
					<table id="to_be_export_student_list" class="gridTable"></table>
					<div id="to_be_export_student_list_pager" class="gridTable"></div>
					<BR />
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
		
		<td class="transparent"><lb:label key="" /> <BR />
			<BR />
				<div id="dataExportDetailsLbl">
					<table style="margin-bottom: 10px; width: 924px;">
						<tr>
							<td class="transparent"><lb:label key="" /></td>
							<td class="transparent">
								<div class="formValueLarge">
								<span class="formValueLarge" id=""></span>
								</div>
							</td>
						</tr>
						<tr>
							<td class="transparent"><lb:label key="" /></td>
							<td class="transparent">
								<div class="formValueLarge">
									<span class="formValueLarge" id=""></span>
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
	
	<!-- Steps 3 ends Here -->
	</table>
</div>
	