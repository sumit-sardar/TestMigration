<div id="scheduleSession"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>
		<div id="ssAccordion" style="width:99.5%;">
			<div>
				<h3><a href="#">Select Test</a></h3>
				<div id="select_Test" style="background-color: #FFFFFF;">
				</div>
			</div>
			<div>
				<h3><a href="#">Test Detail</a></h3>
				<div id="test_Detail" style="overflow-y: scroll !important; overflow-x: hidden !important;">
								
				</div>
			</div>
			<div>
				<h3><a href="#">Add Student</a></h3>
				<div id="add_Student" style="background-color: #FFFFFF; overflow-y: scroll !important; overflow-x: hidden !important;">
					<jsp:include page="/sessionOperation/add_student_tab.jsp" />
				</div>
			</div>
			<div>
				<h3><a href="#">Add Proctor</a></h3>
				<div id="add_Proctor" style="overflow-y: scroll !important; overflow-x: hidden !important;">
					
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
								<input type="button"  id="sData" value="&nbsp;Save&nbsp;" onclick="" class="ui-widget-header">
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