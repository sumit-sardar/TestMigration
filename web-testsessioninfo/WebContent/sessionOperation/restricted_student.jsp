<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web" />

<div id="restrictedStudent"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>
		 <div style="width:890px;text-align: left;background-color: #FFFFFF; padding:5px;">
		 	<span>
		 	<h2><span id = "addOperation" style="display: none"><lb:label key="resStu.title.Add"/></span></h2>
		 	<h2><span id = "editOperation" style="display: none"><lb:label key="resStu.title.Edit"/></span></h2> 
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;">
					<lb:label key="showrestrictedstudents.message1"/>
			</p>
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"> 
					<span>
						<span id = "restrictedCount"></span>
						<lb:label key="showRestricted.of" prefix="&nbsp;" suffix="&nbsp;"/>
						<span id="totalCountStu"></span>
						<lb:label key="showrestrictedstudents.message2" prefix="&nbsp;"/>
					</span>
			</p>
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;">
					<lb:label key="showrestrictedstudents.message3"/>
			</p>
	 	</div>	
			<br>
			<table id="restrictedStudentlist" class="gridTable"></table>
			<div id="restrictedStudentpager" class="gridTable"></div>	
			<div>
				<table cellspacing="0" cellpadding="0" border="0"  class="EditTable" width="100%">
					<tbody>
						<br>
						<tr align="center">
							
							<td  width="100%">
								<center>
								<input type="button"  id="rsSaveData" value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/>  onclick="javascript:saveTest(false); javascript:closePopUp('restrictedStudent');return false;" class="ui-widget-header">
								<input type="button"  id="rsCancelData" value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUp('restrictedStudent'); return false;" class="ui-widget-header">
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