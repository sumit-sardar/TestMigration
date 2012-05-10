<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />

<div id="scoreByStudentPage">
<div style="float:left;" align="left">
	<lb:label key="scoring.scoreByStuPopup.msg" />
</div>
<br>
<br>
<table class="transparent" width="100%">
			<tr class="transparent">
				<td class="transparent">
					<lb:label key="scoring.student.popup.accessCode" />
				</td>
				<td class="transparent">
					<div class="formValueLarge">
						<span id="sbsAccessCode" class="formValueLarge">
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
						<span id="sbsTestSessionName" class="formValueLarge">
						</span>
					</div>
				</td>
				</tr>
		</table>
		<br>
		<table width="100%">
			<tr>
				<td>
					<div id="score-by-student-jqgrid">
						<table id="scoreByStudentListGrid" class="gridTable"></table>
						<div id="scoreByStudentListPager" class="gridTable"></div>
					</div>
				</td>
			</tr>
		</table>
</div>

<div id="scoreByStudentItemPage" style="display:none;">

	<div style="float:left;" align="left">
			<lb:label key="scoring.stuPopup.message" />
	</div>
		<br>
		<br>
		<br>
		<table class="transparent" width="100%">
			<tr class="transparent">
				<td class="transparent">
					<lb:label key="scoring.student.popup.studentName" />
				</td>
				<td class="transparent">
					<div class="formValueLarge">
						<span id="studentNameSBS" class="formValueLarge">
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
						<span id="loginNameSBS" class="formValueLarge">
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
							<span id="sessionNameSBS" class="formValueLarge">
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
							<span id="accessCodeSBS" class="formValueLarge">
							</span>
						</div>
					</td>
				</tr>
		</table>
		
		<table width="100%">
			<tr>
				<td align="right">
					<a href="#" id="processScoreSBS" class="rounded {transparent} button" onclick="processScore()" ><lb:label key="scoring.button.process.score" /></a>
				</td>
			</tr>
			<tr>
				<td>
					<div id="jqGrid-content-section-scoring">
						<table id="studentItemListGridSBS" class="gridTable"></table>
						<div id="studentItemListPagerSBS" class="gridTable"></div>
					</div>
				</td>
			</tr>
		</table>
		<br>
		<div id="backButton" style="float:left;" align="left">
			<input type="button"  id="popupBackBtn" value=<lb:label key="common.button.back" prefix="'" suffix="'" /> onclick="javascript:toggleScoreByStudent(); return false;" class="ui-widget-header" style="width:60px">
		</div>
	</div>
