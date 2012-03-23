<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web" />

	
	
<div id="modifyStudentManifestPopup"	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
<br>
	<div id="displayMsmMsg" class="roundedMessage" style="display:none; margin-bottom: 3px;"> 
			<table>
				<tr>
					<td id="displayMsmMsgErrorIcon"  rowspan="3" valign="top" width="18" style="display:none;">
                   	<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
					</td>
					<td id="displayMsmMsgInfoIcon"  rowspan="3" valign="top" width="18" style="display:none;">
                   	<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif" border="0" width="16" height="16">&nbsp;&nbsp;
					</td>
					<td valign="top">
						<table>
							<tr><td><font style=" font-size:12px; font-weight:bold"><div id="msmSubTitle"></div></font></td></tr>
							<tr><td><div id= "msmSubContent">	</div></td></tr>
						</table>
					</td>
				</tr>
			</table>
	</div>
<div id="modifyStudentManifestAccordion" style="width:99.5%;">
	<div id="mStdMStudentListId">
		<h3 id="mStdMStudentListHeader" ><a href="#"><lb:label key="viewStatus.rosDet.title" /></a></h3>
		<div id="mStdMStudentListContent" style="background-color: #FFFFFF; overflow-x: hidden !important; overflow-y: scroll !important;">
			<jsp:include page="/sessionOperation/edit_student_manifest_student_list.jsp" />
		</div>
	</div>
	<div id="mStdMStudentDetailId">
		<h3 id="mStdMStudentDetailHeader"><a href="#"><lb:label key="viewStatus.subDet.title" /></a></h3>
		<div id="mStdMStudentDetailContent" style="background-color: #FFFFFF; overflow-x: hidden !important; overflow-y: scroll !important; padding-top: 0px !important;">
			<jsp:include page="/sessionOperation/edit_student_manifest_manifest_detail.jsp" />
		</div>
	</div>
	<div>
		<table cellspacing="0" cellpadding="0" border="0" width="100%">
			<tbody>
				<br>
				<tr  align="center">
					<td  width="100%">
						<center>
							<input id="msmSaveButton" type="button" disabled="disabled" value=<lb:label key="common.button.save" prefix="'&nbsp;" suffix="&nbsp;'"/>	onclick=" javascript:validateAndUpdateStudentSubtest( );return false;" class="ui-state-disabled" style="width: 60px">
							&nbsp; 
							<input type="button"	value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/>	onclick="javascript:closemodifyStudentManifestPopup(); return false;" class="ui-widget-header" style="width: 60px ">
						</center>
						<br>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
</div>



