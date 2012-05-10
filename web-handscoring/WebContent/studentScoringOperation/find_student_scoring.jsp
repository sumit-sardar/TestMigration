<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />
<input type="hidden" id="scoreSuccess" name="scoreSuccess" value=<lb:label key="scoring.successful" prefix="'" suffix="'"/>/>
<input type="hidden" id="scoringError" name="scoringError" value=<lb:label key="scoring.error" prefix="'" suffix="'"/>/>
<input type="hidden" id="processSuccessful" name="processSuccessful" value=<lb:label key="process.successful" prefix="'" suffix="'"/>/>
<input type="hidden" id="processError" name="processError" value=<lb:label key="process.error" prefix="'" suffix="'"/>/>
<input type="hidden" id="questionPopUpHeader" name="questionPopUpHeader" value=<lb:label key="questionpopup.header" prefix="'" suffix="'"/>/>
<input type="hidden" id="questionPopUpAnswer" name="questionPopUpAnswer" value=<lb:label key="questionpopup.answer" prefix="'" suffix="'"/>/>
<input type="hidden" id="questionPopUpScore" name="questionPopUpScore" value=<lb:label key="questionpopup.score" prefix="'" suffix="'"/>/>
<input type="hidden" id="questionPopUpQuestion" name="questionPopUpQuestion" value=<lb:label key="questionpopup.question" prefix="'" suffix="'"/>/>
<input type="hidden" id="questionPopUpRubric" name="questionPopUpRubric" value=<lb:label key="questionpopup.rubric" prefix="'" suffix="'"/>/>

<div id="studentScoringId"	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>

			<div id="displayMessageStudent" class="roundedMessage">
						<table>
							<tr>
								<td width="18" valign="middle">
									<div id="errorIconStu" style="display:none;">
				                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
									</div>
									<div id="infoIconStu" style="display:none;">
										<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif" border="0" width="16" height="16">
									</div>
								</td>
								<td class="saveMsgs" valign="middle">
									<div id= "contentMainStu"></div>
								</td>
							</tr>
						</table>
					</div>	 
	<div id="handScoringDiv" style="background-color: #FFFFFF; padding:20px; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
		<p>
			<lb:label key="scoring.stuPopup.message" />
		</p>
		
		<br>
		
		<table class="transparent" width="100%">
			<tr class="transparent">
				<td class="transparent">
					<lb:label key="scoring.student.popup.studentName" />
				</td>
				<td class="transparent">
					<div class="formValueLarge">
						<span id="studentNameScr" class="formValueLarge">
						</span>
					</div>
				</td>
			</tr>
			<tr class="transparent">
				<td class="transparent">
					<lb:label key="scoring.student.popup.loginName" />
				</td>
				<td class="transparent">
					<div class="formValueLarge">
						<span id="loginNameScr" class="formValueLarge">
						</span>
					</div>
				</td>
				</tr>
				<tr class="transparent">
					<td class="transparent">
						<lb:label key="scoring.student.popup.testSessionName" />
					</td>
					<td class="transparent">
						<div class="formValueLarge">
							<span id="sessionNameScr" class="formValueLarge">
							</span>
						</div>
					</td>
				</tr>
				<tr class="transparent">
					<td class="transparent">
						<lb:label key="scoring.student.popup.accessCode" />
					</td>
					<td class="transparent">
						<div class="formValueLarge">
							<span id="accessCodeScr" class="formValueLarge">
							</span>
						</div>
					</td>
				</tr>
		</table>
		
		<table width="100%">
			<tr>
				<td align="right">
					<a href="#" id="processScore" class="rounded {transparent} button" onclick="processScore(this)"><lb:label key="scoring.button.process.score" /> </a>
				</td>
			</tr>
			<tr>
				<td>
					<div id="jqGrid-content-section-scoring">
						<table id="studentItemListGrid" class="gridTable"></table>
						<div id="studentItemListPager" class="gridTable"></div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div>
		<br>
		<center>
			<input type="button"  id="popupCloseBtn" value=<lb:label key="common.button.close" prefix="'" suffix="'" /> onclick="javascript:closePopUp('studentScoringId'); return false;" class="ui-widget-header" style="width:60px">
		</center>
		<br>
	</div>
</div>