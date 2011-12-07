<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web" />

<div id="duplicateStudent"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>
		 <div style="width:965px;text-align: left;background-color: #FFFFFF; padding:5px;"> 
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;">
					<netui:content value="${bundle.web['homepage.deuplicatestudent.message']}"/>
			</p>
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"> 
					<netui:content value="${bundle.web['homepage.deuplicatestudent2.message']}"/>
			</p>
	 	</div>	
			<br>
			<table id="dupStudentlist" class="gridTable"></table>
			<div id="dupStudentpager" class="gridTable"></div>	
			<div>
				<table cellspacing="0" cellpadding="0" border="0"  class="EditTable" width="100%">
					<tbody>
						<br>
						<tr align="center">
							
							<td  width="100%">
								<center>
								<input type="button"  id="dsData" value="&nbsp;Apply&nbsp;"  onclick="javascript:updateDupStudent(); return false;" class="ui-widget-header">
								<input type="button"  id="dcData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:closePopUp('duplicateStudent'); return false;" class="ui-widget-header">
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