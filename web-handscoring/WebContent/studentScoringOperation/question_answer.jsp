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

		if(isWindows()) {
	        var myApp = document.getElementById("myApp");
	       	myApp.script.stopAudio("");
		}
		else {
			subIframe[0].contentWindow.stopAudio();
			//alert(subIframe);
		}
       	/*if(subIframe != '' || subIframe != null) {
		$(subIframe).attr('src', "#");
		}*/	
											
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
				document.getElementById("Question").setAttribute("disabled",true);
			}else{
				document.getElementById("pointsDropDown").removeAttribute("disabled");
				document.getElementById("Question").removeAttribute("disabled");
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
	
	function checkPlay(){
		//alert("playCompleted in checkplay : "+playCompleted);
	
		if(document.getElementById("itemType").value == "AI"){			
				if(playCompleted == true){	
					formSave();
				}else{
					var confSave = confirm("Are you sure you want to score before listening to the entire response?");
					if(confSave == true){
					formSave();
					}
				}
		}else{
				formSave();
		}
	}
	
	
	function passAudioString(){
		var audioResponseString = document.getElementById("audioResponseString").value;
		return audioResponseString;
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
	
	<div id="quesAnsAccordion" style="width:99.5%;">
			
			<div id="scoringQues">
				<h3><a href="#">Question</a></h3>
				
					<div id="questionInformation" style="background-color: #FFFFFF;height: 535px !important;">
					
					</div>
				
			</div>
			<div id="scoringRubric">
				<h3><a href="#">Rubric</a></h3>
				<div id="rubricInformation" style="overflow-y: scroll !important; overflow-x: hidden !important;"><!-- changes for defect #66994 -->
						<iframe id="rubricIframe" src="<%=request.getContextPath() %>/studentScoringOperation/rubricNew.jsp" style=" font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; width: 100%; height: 95%; " frameborder="1" scrollable="yes"></iframe>
				</div>
			</div>
			<div id="scoringAns">
				<h3><a href="#">Answer</a></h3>
				<div id="answerInformation" style="overflow-y: scroll !important; overflow-x: hidden !important;"><!-- changes for defect #66994 -->
					<table border="0" width="100%">
							<tr width="100%">
								<td class="transparent" style="width: 10%;"><span><b> Answer :</b></span></td>
							</tr>
							<tr width="100%">
								<td class="transparent" style="width: 90%;padding-left:5px;" id="dialogIdDiv">
									<textarea id="crText" width="70%" cols="85" rows="8" readonly="readonly"></textarea>
									<div id="audioPlayer">
										<script>
											//getAudioPlayer('audioPlayer');//javafx({archive: "JavaFXApplication1.jar",width: 250,height: 80,code: "javafxapplication1.Main",name: "fxApp",id: "fxApp"});
										</script>
									
									</div>
								<div id="iframeDiv">
								<iframe id="iframeAudio" src="about:blank" height="70" width="200" frameborder="0" scrolling="no" >
								</iframe>
								</div>
								</td>
							</tr>
							<tr width="100%">
								<td class="transparent" style="width: 10%;"><span><b> Score :</b></span></td>

							</tr>
							<tr width="100%">
								<td class="transparent" style="padding-left:5px;">
								<div><select id="pointsDropDown" onChange="hideMessage();" ></select>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="Question" width="60" class="ui-widget-header" value="Save" onclick="checkPlay();" /></div>
								</td>							
							</tr>							
					</table>
				</div>
			</div>			
	</div>
	<br>
	<center>
		<input type="button"  id="popupCancelBtnSBS" value=<lb:label key="common.button.cancel" prefix="'" suffix="'" /> onclick="javascript:closePopUp('questionAnswerDetail'); return false;" class="ui-widget-header" style="width:60px">
	</center>
	<br>
		
</div>