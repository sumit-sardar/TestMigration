<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />
<netui-data:declareBundle bundlePath="webResources" name="web" />

<div id="viewTestSessionId"	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
<br>

<div id="viewTestSessionAccordion" style="width:99.5%;">
	<div id="rosterDetailsSectionId">
		<h3><a href="#"><lb:label key="viewStatus.rosDet.title" /></a></h3>
		<div id="View_Roster" style="background-color: #FFFFFF; overflow-x: scroll !important; overflow-y: scroll !important;">
			<jsp:include page="/sessionOperation/view_roster_list.jsp" />
		</div>
	</div>
	<div id="subtestDetailsSectionId">
		<h3><a href="#"><lb:label key="viewStatus.subDet.title" /></a></h3>
		<div id="View_Subtest" style="background-color: #FFFFFF; overflow-x: scroll !important; overflow-y: scroll !important;">
			<jsp:include page="/sessionOperation/view_subtest_details.jsp" />
		</div>
	</div>
	<div>
		<table cellspacing="0" cellpadding="0" border="0" id="TblGrid_list2_2" class="EditTable" width="100%">
			<tbody>
				<br>
				<tr id="Act_Buttons" align="center">
					<td  width="3%" id="preButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-left" id="pData" href="javascript:pDataClick('Edit');"><span
						class="ui-icon ui-icon-triangle-1-w"></span></a></td><td id="nextButton" style="visibility:hidden"><a class="fm-button ui-state-default ui-corner-right" id="nData"
						href="javascript:nDataClick('Edit');"><span class="ui-icon ui-icon-triangle-1-e"></span></a></td>
						<td>&nbsp;</td>
					<td  width="100%">
						<center>
							<input type="button"  id="cDataView" value=<lb:label key="common.button.close" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closeViewStatusPopup(); return false;" class="ui-widget-header" style="width:60px">
						</center>
						<br>
					</td>
				</tr>
				<tr class="binfo" style="display: none;">
					<td colspan="2" class="bottominfo"></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
</div>