<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<%
	 Boolean supportAccommodations  = (Boolean) session.getAttribute("supportAccommodations");
%>
	<div id = "studentAddDeleteInfo" 
		style="display: none; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">	
	<table>
		<tbody>
			<tr width='100%'>
				<th style='padding-right: 6px; text-align: right;' rowspan='2'>
					<img height='23' src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif">
				</th>
			</tr>
			<tr width='100%'>
				<td>
					<span id = 'addDeleteStud'></span>
				</td>
			</tr>
		</tbody>
	</table>
	</div>

	<div style="width:100%;text-align: left;"> 
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;padding: 0 5px 4px 0;">
				 
					<lb:label key="sessionList.studentTab.totalStu" /> <span id = "totalStudent"></span>	
			</p>
			<% if (supportAccommodations) {%>
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"> 
				
					<lb:label key="sessionList.studentTab.stuWithAcc" /> <span id = "stuWithAcc"></span>	
				
			</p>
			<% } %>
	</div>
	<table>
			<tr>
				<td>
					<div style="clear:both;float:left;width:925px;padding: 0 5px 5px 0;">
						<div id="addStudent" style="float:right;padding-left:5px;">
							<a href="#" id="addStudentButton" onclick="showSelectStudent();" class="rounded {transparent} button"><lb:label key="session.accordion.addStudent" /></a>
						</div> 
					</div>
				</td>
			</tr>
	</table>
	
	
		<table id="list6" class="gridTable"></table>
		<div id="pager6" class="gridTable"></div>	
	


				
<div id="removeStuConfirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<p><lb:label key="scheduleTest.delStuConfirmation.message" /></p>
			<p><lb:label key="scheduleTest.delStuConfirmation2.message" /></p>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:removeSelectedStudent(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUp('removeStuConfirmationPopup'); return false;" class="ui-widget-header">
				</center>
		</td>
		
		</tr>
		
	</table>
</div>
