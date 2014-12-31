<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="testsessionApplicationResource" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<style type="text/css">
.zIndex{
	z-index:999999;
}
</style>
<table width="100%" border="0">
	<tr>
		<td>
		<div style="margin-bottom: 2px; width: 920px;" class="subtitle"><lb:label key="viewStatus.scoreList.message" />
		</div>
		</td>
	</tr>
</table>
<table style="margin-bottom: 10px; width: 924px;">
	<tr>
		<td width="135"><lb:label key="viewStatus.subtest.loginName" /></td>
		<td><span id="scoreLoginName"></span></td>
	</tr>
	<tr>
		<td><lb:label key="viewStatus.subtest.password" /></td>
		<td><span id="scorePassword"></span></td>
	</tr>
	<tr>
		<td><lb:label key="viewStatus.subtest.sessionName" /></td>
		<td><span id="scoreTestAdminName_acco"></span></td>
	</tr>
	<tr>
		<td><lb:label key="viewStatus.subtest.testName" /></td>
		<td><span id="scoreTestName"></span></td>
	</tr>
	<tr>
		<td><lb:label key="viewStatus.subtest.testStatus" /></td>
		<td><span id="scoreTestStatus"></span></td>
	</tr>
	<tr id="scoreTestGradeRow" style="display: none;">
		<td><lb:label key="viewStatus.subtest.testGrade" /></td>
		<td><span id="scoreTestGrade"></span></td>
	</tr>
	<tr id="scoreTestLevelRow" style="display: none;">
		<td><lb:label key="viewStatus.subtest.testLevel" /></td>
		<td><span id="scoreTestLevel"></span></td>
	</tr>
</table>

<div id="scoreListDetails" class="scoreListAccordianPanel"></div>

<div id="scoreResponsePopup"
	 style="display: none; background-color: rgb(212, 236, 255); font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; width: auto; min-height: 106.267px; height: auto;"
	 scrolltop="0" scrollleft="0"><br/>
	
	<div id="responseBox" 
		 style="background-color: #FFFFFF; overflow-x: hidden !important; height: 200px; padding-top: 20px; overflow-y: scroll; margin-left:10px; padding-left:5px; word-wrap: break-word;">
		<span id="responseMessage"></span>
	</div>
	<!--<div id="responseBox"  style=" overflow-x: hidden !important; overflow-y: hidden !important;">
		<textarea id="responseMessage" spellcheck="false" disabled="true" 
			style="background-color: #FFFFFF; height: 200px; padding-top: 20px; padding-left: 5px; margin-left: 10px; width: 750px; resize: none; color: #000000 !important;"></textarea>
	</div>-->

	<div style="padding-bottom: 20px; padding-top: 20px;">
	<center>
	<input type="button" value=<lb:label key="common.button.close" prefix="'&nbsp;" suffix="&nbsp;'"/>
	onclick=" javascript:closeScoreResponsePopup();return false;" class="ui-widget-header" style="width: 60px"></center>
</div>
</div>

<div>
	<input type="hidden" name="contentDomainLbl" id="contentDomainLbl" value=<lb:label key="ViewScoreDetails.text.contentDomain" prefix="'" suffix="'"/> />
	<input type="hidden" name="itemOrderLbl" id="itemOrderLbl" value=<lb:label key="ViewScoreDetails.text.itemOrder" prefix="'" suffix="'"/> />
	<input type="hidden" name="itemTypeLbl" id="itemTypeLbl" value=<lb:label key="ViewScoreDetails.text.itemType" prefix="'" suffix="'"/> />
	<input type="hidden" name="itemRowScoreLbl" id="itemRowScoreLbl" value=<lb:label key="ViewScoreDetails.text.rawScore" prefix="'" suffix="'"/> />
	<input type="hidden" name="itemResponseLbl" id="itemResponseLbl" value=<lb:label key="ViewScoreDetails.text.response" prefix="'" suffix="'"/> />
	<input type="hidden" name="scoreTableTitle" id="scoreTableTitle" value=<lb:label key="viewStatus.score.table.title" prefix="'" suffix="'"/> />
	<input type="hidden" id="scoreResponseTitle"value=<lb:label key="viewStatus.score.response.title" prefix="'" suffix="'"/> />
	<input type="hidden" id="locatorNotCompleted"value=<lb:label key="viewStatus.score.grid.msgtb" prefix="'" suffix="'"/> />
	<input type="hidden" id="subtestNotCompleted"value=<lb:label key="viewStatus.score.grid.msg" prefix="'" suffix="'"/> />
	
</div>