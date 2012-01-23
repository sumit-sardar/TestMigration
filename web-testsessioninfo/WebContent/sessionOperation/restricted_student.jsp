<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web" />

<div id="restrictedStudent"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>
		 <div style="width:965px;text-align: left;background-color: #FFFFFF; padding:5px;"> 
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;">
					<lb:label key="homepage.deuplicatestudent.message"/>
			</p>
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"> 
					<lb:label key="homepage.deuplicatestudent2.message"/>
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
								<input type="button"  id="rsSaveData" value=<lb:label key="common.button.apply" prefix="'&nbsp;" suffix="&nbsp;'"/>  onclick="javascript:updateDupStudent(); return false;" class="ui-widget-header">
								<input type="button"  id="rsCancelData" value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUp('duplicateStudent'); return false;" class="ui-widget-header">
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