<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>


	<table style="margin-bottom: 10px;">
		<tr>
			<td>
				<div id="proctorInformationText">
					<span style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;">
						<lb:label key="scheduleTest.addProc.message1"/>
						<lb:label key="scheduleTest.addProc.message2"/>
					</span>
				</div>
			</td>
		</tr>
	</table>
	
	<div id = "proctorAddDeleteInfo" 
		style="display: none; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">	
	<table style="margin-bottom: 10px;">
		<tr width='100%'>
				<th style='padding-right: 6px; text-align: right;' rowspan='2'>
					<img height='23' src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif">
				</th>
			</tr>
			<tr width='100%'>
				<td>
					<span id = 'addDeleteProc'></span>
				</td>
			</tr>
	</table>
</div>

	
	<table width="928px" style="margin-bottom: 5px;">
		<tr>
			<td>
				<div style="float:left;"> 
						<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;">
								<lb:label key="sessionList.proctors.assigned"/> 
								<span id = "totalAssignedProctors"></span>	
						</p>
			
						<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"> 
								<lb:label key="sessionList.proctors.testscheduler"/>  
								<span id = "testSchedulerId"></span>	
						</p>
				</div>
			</td>
			<td>
				<div id="addProctor" style="float:right;padding-top:5px;">
					<a href="#" id="addProctorButton" onclick="showSelectProctor();" class="rounded {transparent} button"><lb:label key="session.accordion.addProctor"/></a>
				</div> 
			</td>
		</tr>
	</table>
	

	
	<table id="listProctor" class="gridTable"></table>
	
	<div id="pagerProctor" class="gridTable"></div>
	
	
	<div id="removeProctorConfirmationPopup" style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
				<p><lb:label key="homepage.delProctorConfirmation1.message"/></p>
				<p><lb:label key="homepage.delProctorConfirmation2.message"/></p>
			</td>
		</tr>
		<tr>
			<td >
				<center>
					<input type="button"  value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:removeSelectedProctor(); return false;" class="ui-widget-header" style="width:60px">&nbsp;
					<input type="button"  value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUpForProctor('removeProctorConfirmationPopup'); return false;" class="ui-widget-header" style="width:60px">
				</center>
			</td>
		</tr>
		
	</table>
</div>
	


