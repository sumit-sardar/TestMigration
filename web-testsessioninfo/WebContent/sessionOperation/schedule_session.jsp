<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<netui-data:declareBundle bundlePath="webResources" name="web" />

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
			<div id="selectTestId">
				<h3><a href="#" >Select Test</a></h3>
				<div id="Select_Test" style="background-color: #FFFFFF;">
					<div id="noTestDiv" style="display:none;">
					<table width="100%" height="300" cellpadding="0" cellspacing="0" class="transparent">
					
					
						<tr>
							<td style='width: 100%;' colspan='6' align="center">
							<table>
								<tbody>
									<tr width='100%'>
										<th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23'
											src='/SessionWeb/resources/images/messaging/icon_info.gif'></th>
										<th colspan='6' align="left"><netui:content value="${bundle.web['sessionList.selectTest.noTestExistsHead']}" /></th>
									</tr>
									<tr width='100%'>
										<td colspan='6'><netui:content value="${bundle.web['sessionList.selectTest.noTestExists']}" /></td>
									</tr>
								</tbody>
							</table>
							</td>
						</tr>
					</table>
					</div>
					<div id="testDiv" style="display:none;">
						<jsp:include page="/sessionOperation/select_test.jsp" />
					</div>
				</div>
			</div>
			<div id="testDetailId">
				<h3><a href="#" >Test Detail</a></h3>
				<div id="Test_Detail" style="overflow-y: scroll !important; overflow-x: hidden !important;">
					<div align="center">
						<jsp:include page="/sessionOperation/test_details.jsp" />
					</div>
				</div>
			</div>
			<div id="addStudentId">
				<h3><a href="#" >Add Student</a></h3>
				<div id="Add_Student" style="background-color: #FFFFFF; overflow-x: scroll !important; overflow-y: hidden !important;">
					<div id="Student_Tab" style ="display:block;">
						<jsp:include page="/sessionOperation/add_student_tab.jsp" />
					</div>
					<div id="Select_Student_Tab" style ="display:none;">
						<jsp:include page="/sessionOperation/select_student.jsp" />	
					</div>
				</div>
			</div>
			<div id="addProctorId">
				<h3><a href="#" >Add Proctor</a></h3>
				<div id="Add_Proctor" style="background-color: #FFFFFF; overflow-x: scroll !important; overflow-y: hidden !important;">
					<div id="Proctor_Tab" style ="display:block;">
						<jsp:include page="/sessionOperation/add_proctor_tab.jsp" />
					</div>
					<div id="Select_Proctor_Tab" style ="display:none;">
						<jsp:include page="/sessionOperation/select_proctor.jsp" />	
					</div>
				</div>
				
			</div>
			
			<div>
				<table cellspacing="0" cellpadding="0" border="0" id="TblGrid_list2_2" class="EditTable" width="100%">
					<tbody>
						<br>
						<tr id="Act_Buttons" align="center">
							<td  width="3%" id="preButton" style= "visibility:hidden"><a class="fm-button ui-state-default ui-corner-left" id="pData" href="javascript:pDataClick('Edit');"><span
								class="ui-icon ui-icon-triangle-1-w"></span></a></td><td id="nextButton" style="visibility:hidden"><a class="fm-button ui-state-default ui-corner-right" id="nData"
								href="javascript:nDataClick('Edit');"><span class="ui-icon ui-icon-triangle-1-e"></span></a></td>
								<td>&nbsp;</td>
							<td  width="100%">
								<center>
								<input type="button"  id="sData" value="&nbsp;Save&nbsp;" onclick="javascript:saveTest(); return false;" disabled="disabled" class="ui-widget-header">
								<input type="button"  id="cData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:onCloseScheduleSessionPopUp(); return false;" class="ui-widget-header">
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


<div id="closeScheduleSessionPopup" style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
				<p><netui:content value="${bundle.web['scheduleTest.cancelConfirmation.message']}"/></p>
				<p></p>
			</td>
		</tr>
		<tr>
			<td >
				<center>
					<input type="button"  value="${bundle.web['homepage.button.ok']}" onclick=" javascript:closeScheduleSessionPopup();return false;" class="ui-widget-header" style="width:60px">&nbsp;
					<input type="button"  value="${bundle.web['homepage.button.cancel']}" onclick="javascript:closePopUp('closeScheduleSessionPopup'); return false;" class="ui-widget-header" style="width:60px">
				</center>
			</td>
		</tr>
		
	</table>
</div>