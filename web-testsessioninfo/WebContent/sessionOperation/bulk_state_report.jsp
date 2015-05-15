<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.io.*,java.util.*"%>
<%@taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="testsessionApplicationResource" />

<input type="hidden" id="editEndDateBulkReport" name="editEndDateBulkReport" value="" />
<input type="hidden" id="labelBulkReport" value=<lb:label key="bulk.state.reporting.hello" prefix="'" suffix="'"/> />

<table class="simpletable" style="margin: 5px auto; height: 190px;">
	<tbody>
		<tr class="tableHeader">
			<th style="padding-left: 5px; text-align: left" colspan="2"><lb:label key="bulk.state.reporting.exportCriteria" /></th>
		</tr>
		<tr style="padding-left: 5px;">
			<td width="50%" style="border-right: 1px #999999 solid;">
			<table>
				<!-- Radio Button Groups -->
				<tr class="simpletable">
					<td class="simpletable" style="border: none"><input id="bukExportAllDates" class="radioDate" type="radio"
						name="bukExportDateSelection" value="AllDates" checked="checked" />&nbsp;&nbsp;<lb:label
						key="bulk.state.reporting.allDates" /></td>
				</tr>
				<tr class="simpletable">
					<td class="simpletable" style="border: none"><input id="bukExportDateRange" class="radioDate" type="radio"
						name="bukExportDateSelection" value="DateRange" />&nbsp;&nbsp;<lb:label key="bulk.state.reporting.dateSelection" /></td>
				</tr>

				<!-- Date Range selector -->
				<tr class="simpletable">
					<td valign="top" nowrap="" class="transparent alignRight">&nbsp;<lb:label key="bulk.state.reporting.startDate"
						prefix="" suffix="&nbsp;:" /></td>
					<td colspan="2" valign="top" class="transparent alignLeft"><input name="startDateBulkReport"
						id="startDateBulkReport" type="text" size="12" maxlength="10" readonly="readonly" disabled="disabled" /></td>
				</tr>
				<tr class="simpletable">
					<td valign="top" nowrap="" class="transparent alignRight">&nbsp;<lb:label key="bulk.state.reporting.endDate"
						prefix="" suffix="&nbsp;:" /></td>
					<td colspan="2" valign="top" class="transparent alignLeft"><input name="endDateBulkReport"
						id="endDateBulkReport" type="text" size="12" maxlength="10" readonly="readonly" disabled="disabled" /></td>


				</tr>

			</table>
			</td>
			<td width="50%" style="border-left: 1px #999999 solid;">
			<table id="criteriaOrgListBulkReport" width="100%" class="bulkReportTable">
			</table>
			</td>
		</tr>
	</tbody>
</table>


