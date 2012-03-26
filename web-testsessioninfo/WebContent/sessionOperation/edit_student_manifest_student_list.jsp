<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web" />
	<div class="roundedMessage ui-corner-all">
		<div style="clear: both; width: 99.99%; text-align: left;" class= "blueSubHeading">
			<p id="mmsModifyStdSubtestMsg" style="font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-top: 2pt; margin-bottom: 5pt; padding: 5">
			<lb:label key="midify.student.manifest.select.student.message" />
			</p>
		</div>
	 </div>
	<div id="mmSessionStudentList" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
		<table id="mmStdList" class="gridTable"></table>
		<div id="mmStdlistPager" class="gridTable"></div>
	</div>
	

	
	