<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<%
	 Boolean supportAccommodations  = (Boolean) session.getAttribute("supportAccommodations");
%>
	<div style="width:100%;text-align: left;"> 
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;padding: 0 5px 4px 0;">
				 
					<netui:content value="${bundle.web['sessionList.studentTab.totalStu']}"/> <span id = "totalStudent"></span>	
			</p>
			<% if (supportAccommodations) {%>
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"> 
				
					<netui:content value="${bundle.web['sessionList.studentTab.stuWithAcc']}"/> <span id = "stuWithAcc"></span>	
				
			</p>
			<% } %>
	</div>
	<table>
			<tr>
				<td>
					<div style="clear:both;float:left;width:925px;padding: 0 5px 5px 0;">
						<div id="addStudent" style="float:right;padding-left:5px;">
							<a href="#" id="addStudentButton" onclick="showSelectStudent();" class="rounded {transparent} button"><netui:content value="${bundle.web['homepage.button.addStudent']}"/></a>
						</div> 
					</div>
				</td>
			</tr>
	</table>
	
	
		<table id="list6" class="gridTable"></table>
		<div id="pager6" class="gridTable"></div>	
	


				
<div id="removeStuConfirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; height: 160px !important;">
	<table>
		<tr>
			<td colspan="2">
			<br/>
			<p><netui:content value="${bundle.web['homepage.delStuConfirmation.message']}"/></p>
			<p><netui:content value="${bundle.web['homepage.delStuConfirmation2.message']}"/></p>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;${bundle.web['homepage.button.ok']}&nbsp;" onclick="javascript:removeSelectedStudent(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;${bundle.web['homepage.button.cancel']}&nbsp;&nbsp;" onclick="javascript:closePopUp('removeStuConfirmationPopup'); return false;" class="ui-widget-header">
				</center>
		</td>
		
		</tr>
		
	</table>
</div>
