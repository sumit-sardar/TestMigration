<!DOCTYPE html>
<html><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>Web Audio Widget </title>
    <link rel="stylesheet" href="BMTPlayer/webaudio.css">
    <style type="text/css">
	    audio {
	 		 width: 100%;
		}
    </style>
    
    <script src="../resources/js/hierarchy.js"></script>
    
</head>

<body onload="myPageLoad()">

<!-- Add your site or application content here -->
<div id="displayMessageForQues" class="roundedMessage" style="display:none;"> 
						<table>
							<tr>
								<td width="18" valign="middle">
									<div id="errorIconQues">
				                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
									</div>
									
								</td>
								<td class="saveMsgs" valign="middle">
									<div id= "contentMainQues"></div>
								</td>
							</tr>
						</table>
</div>	



<div id="audioContent" class="main-container">
       <div class="section-container">
        <h2>Audio player widget</h2>
         <!-- player icon -->
        <button id="playIcon" onclick="playAudio()" class="playIcon"></button>
        <button id="pauseIcon" onclick="pauseAudio()" class="pauseIconDisable"></button>
        <button id="stopIcon" onclick="stopAudio()" class="stopIconDisable"></button>
        
        
        <audio controls="controls" id="myaudio" preload="none" style="display:none;">
            <source id="mp3Source" src="" type="audio/mp3">
            Your browser does not support the <code>audio</code> element.
        </audio>     
        
        	<br>
        <!-- Player button -->
        <!-- button onclick="playAudio()" class="ui-widget-header">play</button>
        <button onclick="pauseAudio()" class="ui-widget-header">pause</button>
        <button onclick="stopAudio()" class="ui-widget-header">stop</button-->
        <button onclick="volumeUp()" class="ui-widget-header">volumeUp</button>
        <button onclick="volumeDown()" class="ui-widget-header">volumeDown</button>

        <br>
        	
	
    </div>


</div>

<script>
    var audio = document.getElementById("myaudio");
    var responseString=window.parent.s3AudioURl;
    var isInternalStop=false;//to fix close pop up on cancel button in safari
    if(responseString.indexOf(".mp3") > -1)
	{
		audio.src=responseString;
	}else{
		document.getElementById("audioContent").style.display="none";
		document.getElementById("contentMainQues").innerHTML=responseString;
		document.getElementById("displayMessageForQues").style.display="block";
	}
      	
    audio.addEventListener("canplay", function() { updateStatus("canplay"); }, true);
    audio.addEventListener("ended", function() { updateStatus("ended"); }, true);
    audio.addEventListener("pause", function() { updateStatus("pause"); }, true);
    audio.addEventListener("play", function() { updateStatus("play"); }, true)
    audio.addEventListener("playing", function() { updateStatus("playing"); }, true);
    audio.addEventListener("timeupdate", function() { updateTimeElapsed(audio.currentTime); }, true);
	
	function disableEnableIcon(val){
		var play=document.getElementById("playIcon");
		var pause=document.getElementById("pauseIcon");
		var stop=document.getElementById("stopIcon");
		
		switch(val) {
		
		case "play": 
			
			play.className ="playIconDisable";
			pause.className ="pauseIcon";
			stop.className ="stopIcon";
			break;
		case "pause": 
			play.className ="playIcon";
			pause.className ="pauseIconDisable";
			if(stop.className != "stopIconDisable")
				stop.className ="stopIcon";
			break;
		case "stop": 
			play.className ="playIcon";
			pause.className ="pauseIconDisable";
			stop.className ="stopIconDisable";
			break;
		case "reset": 
			play.className ="playIcon";
			pause.className ="pauseIconDisable";
			stop.className ="stopIconDisable";
		 }
	}
    function playAudio()
    {	isInternalStop=true;
    	disableEnableIcon("play");
    	var audio = document.getElementById("myaudio");
    	audio.play();
    	parent.document.getElementById("pointsDropDown").removeAttribute("disabled");
		parent.document.getElementById("Question").removeAttribute("disabled");
		parent.$('#Question').removeClass('ui-state-disabled'); 
    }

    function pauseAudio()
    {
    	isInternalStop=true;
    	disableEnableIcon("pause");
        var audio = document.getElementById("myaudio");
        audio.pause();
    }
	
    function stopAudio()
    {	
    	 var audio = document.getElementById("myaudio");
    	 if(audio!=null ){
    	 	disableEnableIcon("stop");
         	audio.pause();
         	if(isInternalStop && audio.currentTime!=0){
         		audio.currentTime = 0;
         	}
         }
    }

    function volumeUp()
    {
        var audio = document.getElementById("myaudio");
        audio.volume+=0.1;
    }

    function volumeDown()
    {
        var audio = document.getElementById("myaudio");
        audio.volume-=0.1;
    }

    function toggleControls() {
        if (audio.hasAttribute("controls")) {
            audio.removeAttribute("controls")
        } else {
            audio.setAttribute("controls","controls")
        }
    }

    function updateStatus(strStatus)
    {	
    	if(strStatus == "ended")
    	{
    		disableEnableIcon("reset");
    	}	
    		
    	if( document.getElementById("status")){
        document.getElementById("status").innerHTML = strStatus;
        }
        //console.log("updateStatus***********"+strStatus);
        parent.document.getElementById("playingStatus").value=strStatus;
    }

    function updateTimeElapsed(elapsedTime)
    {	
    	if(document.getElementById("timeelapsed")){
        document.getElementById("timeelapsed").innerHTML = elapsedTime;
        }
       //console.log("updateTimeElapsed***********"+elapsedTime);
    }

    function setAudioSource(audioSource)
    {
        var audio = document.getElementById("myaudio");

        audio.setAttribute('src', audioSource);
        audio.load()
        audio.addEventListener("load", function() {
            audio.play();
            //$(".duration span").html(audioElement.duration);
            //$(".filename span").html(audioElement.src);
        }, true);
        document.getElementById("sourcefile").innerHTML = audioSource;

    }

    function myPageLoad()
    {
        //var audio = document.getElementById("myaudio");
        var source = document.getElementById('mp3Source');
        //var mySource = audio.src;
		if(document.getElementById("sourcefile")){
        	document.getElementById("sourcefile").innerHTML = source.src;
        }
        toggleControls();
        //disableEnableIcon("reset");
        window.parent.$.unblockUI();
    }

</script>



</body></html>