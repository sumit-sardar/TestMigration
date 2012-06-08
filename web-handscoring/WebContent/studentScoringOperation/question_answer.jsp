<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />
<script>
var playCompleted = false;
function stopAudio(){
	try {
       var subIframe = $('#iframeAudio');
		subIframe[0].contentWindow.stopAudio();
	}catch (e) {
        
    }
       
 }
 
			
	 //This Function  will be called by javafx at runtime
    function show_alert() {
    	var audioResponseString = document.getElementById("audioResponseString").value;
    	var regExp = /\s+/g;
		var stringFX = audioResponseString.replace(regExp,'');
    	//alert("audio" + audioResponseString);
    	return stringFX;
	}
	
	function getAudioPlayer(parentObj) {
	  var fxstring = javafxString(
	    {
	            archive: "/ScoringWeb/resources/fxResources/JavaFXApplication1.jar",
	            visible: false,	            
	            width: 250,
	            height: 80,
	            code: "javafxapplication1.Main",
	            name: "myApplet",
	            id: "myApp"
	    }
	  );
	  document.getElementById(parentObj).innerHTML = fxstring;
	  playCompleted = false;
	}

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
		
	function getPlayCompleted(playStatus){
		//alert("getPlayCompleted in parent:"+playStatus);
		/*try {
			var myApp = document.getElementById("myApp"); 
			if(playStatus == true && myApp.script.playCalledflag){			
        			myApp.script.stop.disable = true;
       				myApp.script.pause.disable = true;
       			}			
			}
		}catch (e) {}*/
		playCompleted = playStatus;
		if(isWindows()) {
			stopAudio();//to retain the default state of player
		}
		//alert("inside getPlayCompleted");
	}
	
	function checkPlay(element){
		//alert("playCompleted in checkplay : "+playCompleted);
	if(element.className.indexOf('disabled') > 0){
		return true;
	}
		if(document.getElementById("itemType").value == "AI"){			
				if(playCompleted == true){	
					formSave();
				}else{
					openConfirmationPopupQues();
				}
		}else{
				formSave();
		}
	}
	
	
	function passAudioString(){
		var audioResponseString = document.getElementById("audioResponseString").value;
		return audioResponseString;
	}
	
	
	function openConfirmationPopupQues(){
	$("#confirmationPopupQues").dialog({  
		title:$("#confirmAlrt").val(),  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '400px',
	 	modal: true,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 $("#confirmationPopup").css('height',120);
		 var toppos = ($(window).height() - 290) /2 + 'px';
		 var leftpos = ($(window).width() - 410) /2 + 'px';
		 $("#confirmationPopup").parent().css("top",toppos);
		 $("#confirmationPopup").parent().css("left",leftpos);	
		 
	}
	
  
  var iframeQuestion;
  function unlinkQuestionIframe() {
		var element = document.getElementById('questionInformation');
		while(element.hasChildNodes()){
			if (element.lastChild.src != undefined) {
				if (element.lastChild.src.indexOf("itemPlayer") > 0) {
					iframeQuestion = element.lastChild;
				}
			}
			element.removeChild(element.lastChild);
		}
  }  

  function linkQuestionIframe() {
	 	var element = document.getElementById('questionInformation');
		while(element.hasChildNodes()){
			if (element.lastChild.src != undefined) {
				if (element.lastChild.src.indexOf("itemPlayer") > 0) {
					return;
				}
			}
			element.removeChild(element.lastChild);
		}
		element.appendChild(iframeQuestion);
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
<div id="questionAnswerDetail"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">

	<div id="displayMessageForQues" class="roundedMessage"> 
						<table>
							<tr>
								<td width="18" valign="middle">
									<div id="errorIconQues" style="display:none;">
				                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
									</div>
									<div id="infoIconQues" style="display:none;">
										<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif" border="0" width="16" height="16">
									</div>
								</td>
								<td class="saveMsgs" valign="middle">
									<div id= "contentMainQues"></div>
								</td>
							</tr>
						</table>
	</div>	
	<div id="confirmationPopupQues"
		style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style="padding:10px;text-align:center;">
		<div style="text-align: left;">
			<lb:label key="confirmmessage.audio" />
		</div>
	</div>
	<div style="padding:10px;">		
		<center>
			<input type="button"  value=<lb:label key="common.button.yes" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:formSave(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.no" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:closePopUp('confirmationPopupQues'); return false;" class="ui-widget-header">
		</center>
	</div>	
</div>
	
	<div id="quesAnsAccordion" style="width:99.5%;">
			
			<div id="scoringQues">
				<h3><a href="#" onclick="linkQuestionIframe(); return false;"><lb:label key="questionpopup.question" /></a></h3>
				
					<div id="questionInformation" style="background-color: #FFFFFF; height: 500px !important;">
					
					</div>
				
			</div>
			
			<div id="scoringRubric">
				<h3><a href="#" onclick="unlinkQuestionIframe(); return false;" ><lb:label key="questionpopup.rubric" /></a></h3>
					<div id="rubricInformation" style="overflow-y: scroll !important; overflow-x: hidden !important; height: 500px !important;"><!-- changes for defect #66994 -->
								
								<TABLE>
									<TR>
										<td class="transparent" style="width: 10%;"><span><b><lb:label key="questionpopup.answer" /> :</b></span></td>
										<TD rowspan=4>
										<div id='outerdiv' style="width:545px;height:440px; overflow-x: hidden;"><iframe id="rubricIframe"
											src="<%=request.getContextPath() %>/studentScoringOperation/rubricNew.jsp"
											style="font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; width: 99%; height: 98%;"
											frameborder="1" scrollable="yes"></iframe></div>
										</TD>
									</TR>
									<TR>
										<td class="transparent" style="width: 50%; padding-left: 5px;vertical-align: top;" id="dialogIdDiv"><textarea id="crText" width="70%"
											cols="50" rows="20" readonly="readonly"></textarea>
										<div id="iframeDiv"><iframe id="iframeAudio" src="about:blank" height="70" width="200" frameborder="0"
											scrolling="no"> </iframe></div>
										</td>
									</tr>
									<TR>
										<td class="transparent" style="width: 10%;"><span><b><lb:label key="questionpopup.score" /> :</b></span></td>
									</tr>
									<TR>
										<td class="transparent" style="padding-left: 10px;vertical-align: top;">
										<div><select id="pointsDropDown" onChange="hideMessage();"></select>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"
											id="Question" width="60" class="ui-widget-header" value="Save" onclick="checkPlay(this);" /></div>
										</td>
									</tr>
								</TABLE>
					
				</div>
			</div>
		
		
	</div>
	<br>
	<center>
		<input type="button"  id="popupCancelBtnSBS" value=<lb:label key="common.button.cancel" prefix="'" suffix="'" /> onclick="javascript:closePopUp('questionAnswerDetail'); return false;" class="ui-widget-header" style="width:60px">
	</center>
	<br>
		
</div>

