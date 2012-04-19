<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />

<input type="hidden" id="itemListGridCaption" value=<lb:label key="scoring.itemGrid.caption" prefix="'" suffix="'"/>/>


<div id="itemGridDisplaySBI">
	<div style="float:left;" align="left">
		<lb:label key="scoring.scoreByStuPopup.msg" />
	</div>
	<br>
	<br>
	<table class="transparent" width="100%">
		<tr class="transparent">
			<td>
	  			<lb:label key="scoring.student.popup.testAccessCode" />
	  		</td>
			<td class="transparent">
				<div class="formValueLarge">
					<span class="formValueLarge" id="testAccessCodeSBI"></span>
				</div>
			</td>
		</tr>
		<tr class="transparent">
			<td class="transparent">
				<lb:label key="scoring.student.popup.testSessionName" />
			</td>
			<td class="transparent">
				<div class="formValueLarge">
					<span class="formValueLarge" id="testSessionNameSBI" ></span>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%">		
		<tr>
    		<td>
    			<div id="jqGrid-content-section-SBI">
					<table id="itemListGrid" class="gridTable"></table>
					<div id="itemListPager" class="gridTable"></div>
				</div>				
			</td>
		</tr>
	</table>		
</div>

<div id="itemStudentGridDisplaySBI" style="display: none;">
	<div style="float:left;" align="left">
		<lb:label key="scoring.itemStuSBI.msg" />
	</div>
	<br>
	<br>
	<table class="transparent" width="100%">
		<tr class="transparent">
        	<td  nowrap valign="top" class="transparent" colspan="2">
        		<h2>
        			<lb:label key="scoring.item.student.testDetails" />
        		</h2>
        	</td>
		</tr>
		<tr class="transparent">
			<td>
	  			<lb:label key="scoring.itemStudent.popup.itemNumber" />
	  		</td>
			<td class="transparent">
				<div class="formValueLarge">
					<span class="formValueLarge" id="itemNumberSBIStu"></span>
				</div>
			</td>
		</tr>
		<tr class="transparent">
			<td>
				<lb:label key="scoring.itemStudent.popup.maximumScore" />
			</td>
			<td class="transparent">
				<div class="formValueLarge">
					<span class="formValueLarge" id="maximumScoreSBIStu" ></span>
				</div>
			</td>
		</tr>
		<tr class="transparent">
			<td>
				<lb:label key="scoring.itemStudent.popup.subtestName" />
			</td>
			<td class="transparent">
				<div class="formValueLarge">
					<span class="formValueLarge" id="subtestNameSBIStu" ></span>
				</div>
			</td>
		</tr>
		<tr class="transparent">
			<td>
				<lb:label key="scoring.student.popup.testAccessCode" />
			</td>
			<td class="transparent">
				<div class="formValueLarge">
					<span class="formValueLarge" id="testAccessCodeSBIStu" ></span>
				</div>
			</td>
		</tr>
		<tr class="transparent">
			<td>
				<lb:label key="scoring.student.popup.testSessionName" />
			</td>
			<td class="transparent">
				<div class="formValueLarge">
					<span class="formValueLarge" id="testSessionNameSBIStu" ></span>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%">
		<tr>
			<td valign="top" class="transparent" colspan="2">
				<h2>
					<lb:label key="scoring.item.student.testRoster" />
				</h2>
			</td>
		</tr>
		<tr>
    		<td>
    			<div id="jqGridItemStudentDetailsGrid">
					<table id="itemStudentListGridSBI" class="gridTable"></table>
					<div id="itemStudentListPagerSBI" class="gridTable"></div>
				</div>				
			</td>
		</tr>
	</table>
	<br>
		<div id="backButtonItem" style="float:left;" align="left">
			<input type="button"  id="popupBackBtnItem" value=<lb:label key="common.button.back" prefix="'" suffix="'" /> onclick="javascript:toggleScoreByItem(); return false;" class="ui-widget-header" style="width:60px">
		</div>
</div>
