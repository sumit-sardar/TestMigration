<div id="scheduleSession"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>
		<div id="displayMessage" class="roundedMessage" style="display:none; margin-bottom: 15px;"> 
			<table>
				<tr>
					<td rowspan="3" valign="top">
                   	<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
					</td>
					<td>
						<table>
							<tr><td><font style="color: red; font-size:12px; font-weight:bold"><div id="title"></div></font></td></tr>
							<tr><td><div id= "content">	</div></td></tr>
							<tr><td><div id= "message">	</div></td></tr>
						</table>
					</td>
				</tr>
			</table>
			
		</div>	
		
		<div id="ssAccordion" style="width:99.5%;">
			<div>
				<h3><a href="#" >Select Test</a></h3>
				<div id="Select_Test" style="background-color: #FFFFFF;">
					<jsp:include page="/sessionOperation/select_test.jsp" />
				</div>
			</div>
			<div>
				<h3><a href="#" >Test Detail</a></h3>
				<div id="Test_Detail" style="overflow-y: scroll !important; overflow-x: hidden !important;">
					<div align="center">
						<jsp:include page="/sessionOperation/test_details.jsp" />
					</div>
				</div>
			</div>
			<div>
				<h3><a href="#" >Add Student</a></h3>
				<div id="Add_Student" style="background-color: #FFFFFF; overflow-y: scroll !important; overflow-x: hidden !important;">
					<jsp:include page="/sessionOperation/add_student_tab.jsp" />
				</div>
			</div>
			<div>
				<h3><a href="#" >Add Proctor</a></h3>
				<div id="Add_Proctor" style="overflow-y: scroll !important; overflow-x: hidden !important;">
					
				</div>
			</div>
			
			<div>
				<table cellspacing="0" cellpadding="0" border="0" id="TblGrid_list2_2" class="EditTable" width="100%">
					<tbody>
						<br>
						<tr id="Act_Buttons" align="center">
							<td  width="3%" id="preButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-left" id="pData" href="javascript:pDataClick('Edit');"><span
								class="ui-icon ui-icon-triangle-1-w"></span></a></td><td id="nextButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-right" id="nData"
								href="javascript:nDataClick('Edit');"><span class="ui-icon ui-icon-triangle-1-e"></span></a></td>
								<td>&nbsp;</td>
							<td  width="100%">
								<center>
								<input type="button"  id="sData" value="&nbsp;Save&nbsp;" onclick="ValidateSave();" class="ui-widget-header" disabled="disabled">
								<input type="button"  id="cData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:closePopUp('scheduleSession'); return false;" class="ui-widget-header">
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