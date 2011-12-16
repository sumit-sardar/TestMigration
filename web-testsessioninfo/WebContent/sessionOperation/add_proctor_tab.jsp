<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>


	<table>
		<tr>
			<td>
				<div id="proctorInformationText">
					<span style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;">
						The person scheduling the test session is automatically assigned as a proctor.
						Click the Add Proctor button to select additional proctors for this session.
					</span>
				</div>
			</td>
		</tr>
	</table>
	
	<br/>

	
	<table width="928px">
		<tr>
			<td>
				<div style="float:left;"> 
						<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;padding: 0 5px 5px 0;">
								<netui:content value="${bundle.web['sessionList.proctors.assigned']}"/> 
								<span id = "totalAssignedProctors"></span>	
						</p>
			
						<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"> 
								<netui:content value="${bundle.web['sessionList.proctors.testscheduler']}"/> 
								<span id = "testSchedulerId"></span>	
						</p>
				</div>
			</td>
			<td>
				<div id="addProctor" style="float:right;padding-top:5px;">
					<a href="#" id="addProctorButton" onclick="showSelectProctor();" class="rounded {transparent} button"><netui:content value="${bundle.web['homepage.button.addProctor']}"/></a>
				</div> 
			</td>
		</tr>
	</table>
	
	<br/>
	
	<table id="listProctor" class="gridTable"></table>
	
	<div id="pagerProctor" class="gridTable"></div>
	
	
	<div id="removeProctorConfirmationPopup" style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
				<p><netui:content value="${bundle.web['homepage.delProctorConfirmation1.message']}"/></p>
				<p><netui:content value="${bundle.web['homepage.delProctorConfirmation2.message']}"/></p>
			</td>
		</tr>
		<tr>
			<td >
				<center>
					<input type="button"  value="${bundle.web['homepage.button.ok']}" onclick="javascript:removeSelectedProctor(); return false;" class="ui-widget-header" style="width:60px">&nbsp;
					<input type="button"  value="${bundle.web['homepage.button.cancel']}" onclick="javascript:closePopUpForProctor('removeProctorConfirmationPopup'); return false;" class="ui-widget-header" style="width:60px">
				</center>
			</td>
		</tr>
		
	</table>
</div>
	


