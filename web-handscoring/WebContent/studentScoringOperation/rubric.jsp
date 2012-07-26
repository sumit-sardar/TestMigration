<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />
<script>
	
	 //This Function  will be called by javafx at runtime
    function show_alert() {
    	var audioResponseString = document.getElementById("audioResponseString").value;
    	var regExp = /\s+/g;
		var stringFX = audioResponseString.replace(regExp,'');
    	//alert("audio" + audioResponseString);
    	return stringFX;
	}
	
</script>
<input type="hidden" id="itemSetId" />
<input type="hidden" id="itemId" />
<input type="hidden" id="itemNumber" />
<input type="hidden" id="messageStatus" />
<input type="hidden" id="itemType" />
<input type="hidden" id="audioResponseString" />
<input type="hidden" id="rowNo" />
<input type="hidden" id="itemSetIdTC" />
<div id="answerDetail"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">

	<div id="ansAccordion" style="width:99.5%;">
		<div id="scoringRubric">
			<div id="rubricInfo" style="background-color: #FFFFFF; overflow-y: scroll !important; overflow-x: hidden !important; height: 400px !important;"><!-- changes for defect #66994 -->
				<TABLE>
					<TR>
						<div id='outerdiv' style="width:750px;height:380px; overflow-x: hidden;"><iframe id="rubricIframe"
							src="<%=request.getContextPath() %>/studentScoringOperation/rubricNew.jsp"
							style="font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; width: 99%; height: 98%;"
							frameborder="1" scrollable="yes"></iframe></div>
						</TD>
					</TR>
				</TABLE>
			</div>
		</div>
	</div>
	<br>
	<center>
		<input type="button"  id="popupCancelBtnSBS" value=<lb:label key="common.button.cancel" prefix="'" suffix="'" /> onclick="javascript:closePopUp('answerDetail'); return false;" class="ui-widget-header" style="width:60px">
	</center>
	<br>
		
</div>

