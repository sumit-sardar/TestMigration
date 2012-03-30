<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web" />
	<div class="roundedMessage ui-corner-all" style="padding-bottom: 0px !important;">
		<div style="clear: both; width: 99.99%; text-align: left;" class= "blueSubHeading">
			<p id="mmsModifyStdSubtestMsg" style="font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-top: 2pt; margin-bottom: 5pt; padding: 5">
			<lb:label key="midify.student.manifest.select.student.message" />
			</p>
		</div>
	 </div>
	 <div style="padding-bottom: 5px !important;">
		 <table id="msmLabelTb" style="margin-top: 5px; width: 800px;">
			<tr>
				<td align="left" width="16%"  style="padding: 2px !important;"><lb:label key="viewStatus.testName" /></td>
				<td width="80%"><span id = "msmTestName"></span></td>
			</tr>
			<tr>
				<td align="left" width="16%" style="padding: 2px !important;"><lb:label key="viewStatus.subtest.testSessionName" /></td>
				<td width="80%"><span id = "msmTestSessionName"></span></td>
			</tr>
		</table>
	</div>
	<div id="mmSessionStudentList" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
		<table id="mmStdList" class="gridTable"></table>
		<div id="mmStdlistPager" class="gridTable"></div>
	</div>
	

	
	