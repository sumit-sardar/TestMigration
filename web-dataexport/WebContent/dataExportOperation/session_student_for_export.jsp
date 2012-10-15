<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="dataExportApplicationResource" />

<div id="session_student_for_export" style="">

	<table class="sortable" id="reset_show_by_session_table">
	<!--  Steps 1 Starts Here -->
	<tr id="dataExportStep1" style="">
		<td width="10px;"> </td>
		<td class="transparent"><lb:label key="data.export.step1.message" /> <BR />
			<BR />
				<div id="data_export_session_student_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
					<table id="to_be_export_student_list" class="gridTable"></table>
					<div id="to_be_export_student_list_pager" class="gridTable"></div>
					<BR />
					<input id="data_export_step1_next"   type="button"  class="ui-widget-header" value=<lb:label key="data.export.title.next" prefix="'&nbsp;&nbsp;&nbsp;" suffix="&nbsp;&nbsp;&nbsp;'"/>/>
				</div>
				<script>getStudentList(true);</script>
			<BR />
		</td>
	</tr>
	</table>
</div>
	<!--  Steps 1 ends Here -->