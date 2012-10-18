<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="scoring.successful" />
<script>

	function showScoreSelect(disableStatus){
		if(document.getElementById("itemType").value == "AI"){
			if(disableStatus == "true"){
				document.getElementById("pointsDropDown").setAttribute("disabled",true);
				$('#Question').addClass('ui-state-disabled'); 
			}else{
				document.getElementById("pointsDropDown").removeAttribute("disabled");
				$('#Question').removeClass('ui-state-disabled'); 
			}
		}
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
<div id="questionDetail"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div id="questionAccordion" style="width:99.5%;">			
			<div id="scoringQues">			
					<div id="questionInfo" style="background-color: #FFFFFF; height: 540px !important;">					
					</div>				
			</div>
	</div>
	<br>
	<center>
		<input type="button"  id="popupCancelBtnSBS" value=<lb:label key="common.button.cancel" prefix="'" suffix="'" /> onclick="javascript:closePopUp('questionDetail'); return false;" class="ui-widget-header" style="width:60px">
	</center>
	<br>
		
</div>

