<html>
<head>


<script type="text/javascript">
	function getAudioPlayer(parentObj) {
	  
		var sng = parent.passAudioString();	  
		var audioResponseString = sng;
		var regExp = /\s+/g;
		var stringFX = audioResponseString.replace(regExp,'');
		var fxstring = '<applet CODEBASE = "/ScoringWeb/resources/fxResources/" mayscript="true" scriptable="true" archive="audioPlayer.jar,jspeex.jar" code="com.audioPlayer.class" width="200"	height="60" name= "myApplet" id= "myApp"><PARAM name="byteString" value="'+stringFX+'" ></applet>';
		document.getElementById(parentObj).innerHTML = fxstring;
		parent.playCompleted = false;
	}
	
	function getAudioString() {
		var audioResponseString = document.getElementById("audioResponseString").value;
		audioResponseString = audioResponseString.substr(13);
		audioResponseString = audioResponseString.split("%3C%2F");
		var regExp = /\s+/g;
		var stringFX = audioResponseString[0].replace(regExp,'');
		return stringFX;			
	}
	
	function getPlayCompleted(playStatus){
		//alert("playStatus : "+playStatus);
		parent.getPlayCompleted(playStatus);
	}
	
	function showScoreSelect(disableStatus){
		parent.showScoreSelect(disableStatus);
	}
	
	function stopAudio() {
		//alert(document.myApplet);
		document.myApplet.stopAudio();
	}
	
	window.clearApplet= function(){
	var appletElement = document.getElementById('audioPlayer');
		if(appletElement.hasChildNodes()){
			appletElement.removeChild(appletElement.childNodes[0]);
		}
	
	}
</script>

</head>
<body>

<div id="audioPlayer"
	style="width: 100%; height: 90%;font-family: Arial, Verdana, Sans Serif; font-size: 8px; font-weight: normal;">
	<script>
		getAudioPlayer('audioPlayer');
	</script>
</div>

</body>
</html>