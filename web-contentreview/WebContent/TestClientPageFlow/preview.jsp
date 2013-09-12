
<%@ page import="java.io.*"%>
<%@ page import=" java.util.*"%>
<%@ page import=" javax.servlet.http.HttpSession"%>
<%@ page import=" javax.servlet.http.HttpServletRequest"%>

<%
    String eliminatorResource = "eliminator.swf";
    String url = request.getRequestURL().toString().trim();
    int lastSlash = url.lastIndexOf("/");
    url = url.substring(0,lastSlash).trim().replaceAll("https:","http:").replaceAll(":443",":80").replaceAll(":7002",":7001");
    System.out.println(url);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>

<TITLE>Presentation Canvas</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="chrome=1">
<link rel="SHORTCUT ICON"
	href="http://www.laszlosystems.com/favicon.ico">
<meta name="viewport" content="width=device-width; initial-scale=1.0;">
<title>Online Assessment System</title>
<script type="text/javascript" src="../includes/embed-compressed.js"></script>
<script type="text/javascript" src="../includes/manipulative_manager.js"></script>
<script type="text/javascript" src="../lps/includes/laslinks_utils.js"></script>
<script type="text/javascript" src="../lps/includes/jquery-ui-1.9.2.custom.min.js"></script>
<script type="text/javascript" src="../lps/jquery.min.js"></script>
<script type="text/javascript" src="lps/jquery.min.js"></script>
<script type="text/javascript" src="../ContentReviewPageFlow/assets/jquery-1.8.3.js"></script>

<script type="text/javascript" src="lps/jquery.magnifier.js"></script>
<script type="text/javascript" src="../ContentReviewPageFlow/assets/imageZoomer.js"></script>
<style type="text/css">
html,body { /* http://www.quirksmode.org/css/100percheight.html */
	height: 100%;
	/* prevent browser decorations */
	margin: 0;
	padding: 0;
	border: 0 none;
	text-rendering: optimizeLegibility;
	font-family: CTB !important;
}

body {
	background-color: #ffffff;
	-webkit-font-smoothing: antialiased;
	/* -moz-font-smoothing: antialiased; - No longer available in FF */
	font-smoothing: antialiased; //
	text-rendering: optimizeLegibility;
}

img {
	border: 0 none;
}

@font-face {
	font-family: "CTB";
	src: local('OASmathv3'), url('ctbmodules/resources/fonts/OASmathv3.svg') format('svg');
	font-weight: normal;
	font-style: normal;
}

@font-face {
	font-family: 'CTB';
	src: local('OASmathv3 Bold'), url('ctbmodules/resources/fonts/OASmathv3Bold.svg') format('svg'); 
	font-weight: bold;
    font-style: normal;
}

@font-face {
	font-family: 'CTB';
	src: local('OASmathv3 Italic'), url('ctbmodules/resources/fonts/OASmathv3Italic.svg') format('svg'); 
    font-style: italic;
}

@font-face {
	font-family: 'CTB';
	src: local('OASmathv3 BoldItalic'), url('ctbmodules/resources/fonts/OASmathv3BoldItalic.svg') format('svg'); 
	font-weight: bold;
    font-style: italic;
}

@font-face {
	font-family: 'CTB';
	src: local('OASmathv3 Italic'), url('ctbmodules/resources/fonts/OASmathv3Italic.svg') format('svg'); 
    font-style: italic;
}

@font-face {
	font-family: 'oasmathv3';
	src: local('OASmathv3'), url('ctbmodules/resources/fonts/OASmathv3.svg') format('svg'); 
	font-weight: normal;
    font-style: normal;
}
@font-face {
	font-family: 'oasmathv3';
	src: local('OASmathv3 Bold'), url('ctbmodules/resources/fonts/OASmathv3Bold.svg') format('svg'); 
	font-weight: bold;
    font-style: normal;
}
@font-face {
	font-family: 'oasmathv3';
	src: local('OASmathv3 BoldItalic'), url('ctbmodules/resources/fonts/OASmathv3BoldItalic.svg') format('svg'); 
	font-weight: bold;
    font-style: italic;
}
@font-face {
	font-family: 'oasmathv3';
	src: local('OASmathv3 Italic'), url('ctbmodules/resources/fonts/OASmathv3Italic.svg') format('svg'); 
    font-style: italic;
}
</style>
<!--[if IE]>
        <style type="text/css">
            /* Fix IE scrollbar braindeath */
            html { overflow: auto; overflow-x: hidden; }
        </style>
        <![endif]-->
<script type="text/javascript">
<!--
var _LDB_BypassSetFocusOnPageLoad = 0;
var spText;
var fontAccom;
var backColorString;
var xscalefactor = 1;
var yscalefactor = 1;
var currentLasAssetItemId;
var autoPlayEvent = "false";
var isMagnifierVisible = "false";
var forLaslinksLayout = null;
var xscalefactorjs;
var yscalefactorjs;
var LASAssetPath = "/ContentReviewWeb/ContentReviewPageFlow/items/"; 
var TEAssetPath = "/ContentReviewWeb/ContentReviewPageFlow/items/";

function getTEAssetPath(){
	lz.embed.setCanvasAttribute("TEAssetPath", TEAssetPath);
}

function getLasAssetPath(){
	lz.embed.setCanvasAttribute("LASAssetPath", LASAssetPath);
}

function isAppPreviewer(){
	lz.embed.setCanvasAttribute("isPreviewer", true);
}

//This is to revert the change for the header group in javascript side. Because this is not going to be parsed by Flash.
String.prototype.replaceAll = function(stringToFind,stringToReplace){
    var temp = this;
    var index = temp.indexOf(stringToFind);
        while(index != -1){
            temp = temp.replace(stringToFind,stringToReplace);
            index = temp.indexOf(stringToFind);
        }
        return temp;
    }
function showFootnote(header)
{
    header = header.replaceAll("&amp;","&");
	lz.embed.setCanvasAttribute("footnotedata", header);
}
function load() {
    <%
    String productType = (String) session.getAttribute( "productType" );
    %>
     var productTypeVal = "<%=productType%>";
     //alert("prodtype******"+productTypeVal);
     if(productTypeVal == 'Laslinks'|| productTypeVal== 'laslinksLayout'){
        forLaslinksLayout = productTypeVal;
        //alert("forLaslinksLayout****"+forLaslinksLayout)
        loadJsApplication();
     }else if(productTypeVal == 'TeIstep'){
            loadTeJsApplication();
     }
     else {
        loadSwfApplication();
     }
}


function isMac(){
	return (window.navigator.platform.indexOf("Mac") != -1);
}

function closeBrowser()
{
	if(isMac()){
		location.href="close.html";
	}
	else{
		window.close();
	}
}

function updateLDB(){
	_LDB_BypassSetFocusOnPageLoad = 1;
}
function showElement(element){
    element.style.display="block";
}

var myflashinstalled = 0;
var myflashversion = 0;
var myMSDetect = "false";

function mydetectFlash(){
    mydetectFlashUsingJavascript();
    if(myMSDetect == "true"){
        mydetectFlashUsingVBScript();
    }
    myflashversion = new Number(myflashversion);
}

function mydetectFlashUsingJavascript(){
    if (navigator.plugins && navigator.plugins.length)
    {
        navigator.plugins.refresh(true);
        x = navigator.plugins["Shockwave Flash"];
        if (x)
        {
            myflashinstalled = 2;
            if (x.description)
            {
                y = x.description;
                myflashversion = y.charAt(y.indexOf('.')-1);
            }
        }
        else
            myflashinstalled = 1;
        if (navigator.plugins["Shockwave Flash 2.0"])
        {
            myflashinstalled = 2;
            myflashversion = 2;
        }
    }
    else if (navigator.mimeTypes && navigator.mimeTypes.length)
    {
        x = navigator.mimeTypes['application/x-shockwave-flash'];
        if (x && x.enabledPlugin)
            myflashinstalled = 2;
        else
            myflashinstalled = 1;
    }
    else
        myMSDetect = "true";
}

function setSPText(arg){
	if(arg=="")
     	arg=" ";
    spText = arg; 	
    lz.embed.setCanvasAttribute('sptext',arg);
}

function storeScratchPadText(args){	
	spText = args;
}

function mydetectFlashUsingVBScript() {
    document.write('<SCRIPT LANGUAGE="VBScript">on error resume next\nIf myMSDetect = "true" Then\nFor i = 2 to 8\nIf Not(IsObject(CreateObject("ShockwaveFlash.ShockwaveFlash." & i))) Then\nElse\nmyflashinstalled = 2\nmyflashversion = i\nEnd If\nNext\nEnd If\nIf myflashinstalled = 0 Then\nmyflashinstalled = 1\nEnd If</SCRIPT>');
}

function setManipState(args){
	lz.embed.setCanvasAttribute('manipstate',args);
}

function setTTStext(args){
	lz.embed.setCanvasAttribute('ttstext',args);
}

function setAnswerNow(args){
	lz.embed.setCanvasAttribute('setanswer',args);
}
function getReadable(){
	return lz.embed.lzapp.getCanvasAttribute('readable');
}

function freezeUI(){
if(document.getElementById('__lz0')){
		document.getElementById('__lz0').contentWindow.freezeUI();
	}
}

function unlockUI(){
if(document.getElementById('__lz0')){
		document.getElementById('__lz0').contentWindow.unlockUI();
	}
}

function setScaleFactor(args){

	args = args.split('|');
	xscalefactor = args[0];
	yscalefactor = args[1];
	
}
function setScaleFactor(xscalefactor,yscalefactor){
	if(!xscalefactor){
    	xscalefactorjs = 1.2;
    }else{
    	xscalefactorjs = xscalefactor;
    }
    if(!yscalefactor){
    	yscalefactorjs = 1.2;
    }else{
    	yscalefactorjs = yscalefactor;
    }
}

/*
function getFontAccomodation(){
	var fontAccom = lz.embed.lzapp.getCanvasAttribute('fontString');
	fontAccom = fontAccom.split("|");
	var fontObj = new Object();
	fontObj.hasFont = fontAccom[0];
	fontObj.bgcolor = fontAccom[1].replace('0x', '#');
	fontObj.fgcolor = fontAccom[2].replace('0x', '#');
	fontObj.hasFontMag = fontAccom[3];
	return fontObj;
}
*/

function getFontAccomodation(){
	var fontObj = new Object();
	var questionBgColor = gController.questionBgColor;
	var questionFontColor = gController.questionFontColor;
	
	if(typeof questionBgColor == "number"){
		questionBgColor = questionBgColor.toString(16);
		while(questionBgColor.length<6)
        {
            questionBgColor='0'+questionBgColor;
        }
		if(questionBgColor.indexOf("0x") < 0){
			questionBgColor = "0x"+ questionBgColor;
		}
	}
	
	if(typeof questionFontColor == "number"){
		questionFontColor = questionFontColor.toString(16);
		while(questionFontColor.length<6)
        {
            questionFontColor='0'+questionFontColor;
        }
		if(questionFontColor.indexOf("0x") < 0){
			questionFontColor = "0x"+ questionFontColor;
		}
	}
	fontObj.bgcolor = questionBgColor.replace('0x', '#');
	fontObj.fgcolor = questionFontColor.replace('0x', '#');
	fontObj.hasFontMag = gController.hasFontAccommodation;
	return fontObj;
}

/*
function getBackColorAccomodation(){
	var backColorString = lz.embed.lzapp.getCanvasAttribute('backColorString');
	backColorString = backColorString.split("|");
	var bgColorObj = new Object();
	bgColorObj.stemArea = backColorString[0].replace('0x', '#');
	bgColorObj.responseArea = backColorString[1].replace('0x', '#');
	return bgColorObj;
}
*/
function getBackColorAccomodation(){
	var bgColorObj = new Object();
	var questionBgColor = gController.questionBgColor;
	var answerBgColor = gController.answerBgColor;
	
	if(typeof questionBgColor == "number"){
		questionBgColor = questionBgColor.toString(16);
		while(questionBgColor.length<6)
        {
            questionBgColor='0'+questionBgColor;
        }
		if(questionBgColor.indexOf("0x") < 0){
			questionBgColor = "0x"+ questionBgColor;
		}
	}
	
	if(typeof answerBgColor == "number"){
		answerBgColor = answerBgColor.toString(16);
		while(answerBgColor.length<6)
        {
            answerBgColor='0'+answerBgColor;
        }
		if(answerBgColor.indexOf("0x") < 0){
			answerBgColor = "0x"+ answerBgColor;
		}
	}
	
	bgColorObj.stemArea = questionBgColor.replace('0x', '#');
	bgColorObj.responseArea = answerBgColor.replace('0x', '#');
	return bgColorObj;
}
function setHtmlGeneralManip(htmlGeneralManip){
  	lz.embed.setCanvasAttribute('htmlManip',htmlGeneralManip);
}

function exitPleaseWaitPopup(){
	lz.embed.setCanvasAttribute('exitPleaseWaitPopup','true');
}

function getOpenManipData(){
	return lz.embed.lzapp.getCanvasAttribute('htmlOpenManip');
}
function setFootnoteText(arg){
	lz.embed.setCanvasAttribute('footnotetext',arg);
}

function setFocusOnScratchpad(){
document.getElementById('__lz0').contentWindow.setFocus();
}

function setCurLasItemId(id) {
	currentLasAssetItemId = id;
	assetCount = 0;
	checkAnswered = false;
	answerClicked = null;
	canNotAnswerFlag = false;
	currentPlayOrder = 0;
}


function eventMonitor(id,event) {

	var currIframeId = frameFolderObject[id];
 	if(event == 'play' || event == 'jsplay') {
 		setPlayingAttr(event,'true');
 		if(!checkAllPlayedOnce())
 		disableAssets();
 		iframeObject[currentLasAssetItemId][currIframeId]['playEvent'] = true;
 		playSingleAsset(currIframeId);
 		if(iframeObject[currentLasAssetItemId]) {
 			if(iframeObject[currentLasAssetItemId][currIframeId]['folder'] == id) {
 				iframeObject[currentLasAssetItemId][currIframeId]['clickedOnce'] = true;	// To know whether it is clicked once or not.
 				 getCurrentPlayOrder(currIframeId);
 			}
 		}
 		if(autoPlayEvent == 'false') {
 			unlockResponseArea(currIframeId);
 			}
 		 		
 	} else if(event == 'finished' || event == 'endTrack' || event == 'pause') {
 		if( event == 'endTrack' || event == 'pause'){
 			setPlayingAttr(event,'false');
 		}
 			if(!checkAllPlayedOnce())
 			enableAssets();
	 		iframeObject[currentLasAssetItemId][currIframeId]['playEvent'] = false;
	 		//console.log("event -- ",event);
	 		if(iframeObject[currentLasAssetItemId]) {
	 			if(iframeObject[currentLasAssetItemId][currIframeId]['folder'] == id) {
	 				if(iframeObject[currentLasAssetItemId][currIframeId]['clickedOnce']) {
	 					iframeObject[currentLasAssetItemId][currIframeId]['playedOnce'] = true; // To know whether the asset is atleast played once or not.
	 				}
	 			}
	 		}
	 		iframeFolderId = null;
	 		enableNextButton(id);
	 		if(autoPlayEvent == 'true') {
	 			autoPlayEvent = "false";
	 		}
	 		if(answerClicked != null){
	 			checkValIfAnswered(answerClicked);
	 			answerClicked = null;
	 		}
 		
 		restrictNavigation('unlock');
 		resetAllAssets();
 	} else if (event == 'reset') {
 		//setPlayingAttr('false');
 	}
 	else if (event == 'autoplayEndTrack') {
 		setPlayingAttr('false');
 	  	autoPlayEvent = "false";	
 	}
 	//lz.embed.setCanvasAttribute("bringBackFocus", 'true');
}

function callManipOutOfFrameCheck(testClientMouseUp) {
	var elementArr = document.getElementsByTagName('iframe');
	if(elementArr[0] != null && elementArr[0] != undefined && elementArr[0].id) {
		document.getElementById(elementArr[0].id).contentWindow.manipOutOfFrameCheck(testClientMouseUp);
	}
}
//------------------
function callTestMouseUp1() {
   	lz.embed.setCanvasAttribute('testMouseUp','true');
}

<!-- following functions are using for CR item scrolledittext scrollbar -->

function hideScrollbarForCRitem(arg){
    var crItemid = document.getElementById(arg);
    var fontcolor = getCRcolor();
    crItemid.setAttribute('style','overflow-x: hidden; overflow-y: scroll; color: '+fontcolor+';');
}
function setCRscrollEdittextWidth(elmId,comWidth,fontSize){
    var crItemid = document.getElementById(elmId);
    var fontcolor = getCRcolor();
    crItemid.setAttribute('style','overflow-x: hidden; overflow-y: scroll; color: '+fontcolor+'; width: '+comWidth+'; font-size: '+fontSize+';');
}
function setCRscrollEdittextHeight(elmId,comHeight,fontSize){
    var crItemid = document.getElementById(elmId);
    var fontcolor = getCRcolor();
    crItemid.setAttribute('style','overflow-x: hidden; overflow-y: scroll; color: '+fontcolor+'; height: '+comHeight+'; font-size: '+fontSize+';');
}

function getCRcolor(){
	var fontcolor;
	var answerFontColor = gController.answerFontColor;
	if(Number(answerFontColor) == 0)
		answerFontColor = "0x000000";
	if(answerFontColor.indexOf('0x') != -1) {
		fontcolor = answerFontColor.replace('0x', '#');
	}
	else {
		fontcolor = answerFontColor;
	}
	return fontcolor;
}

<!-- End CR item scrolledittext scrollbar functions-->

<!-- following functions are using for scratchpad scrollbar-->

function hideScrollbar(arg){
    var scrid = document.getElementById(arg);
    scrid.setAttribute('style',"overflow-x: hidden; white-space: pre-line; overflow-y: scroll;");
}

function setScrollEdittextWidth(elmId,comWidth,fontSize,textFontColor){
    var scrid = document.getElementById(elmId);
    scrid.setAttribute('style','overflow-x: hidden;  overflow-y: scroll; color: '+textFontColor+'; font-size: '+fontSize+'; width: '+comWidth+'');
}

function setScrollEdittextHeight(elmId,comHeight,fontSize,textFontColor){
    var scrid = document.getElementById(elmId);
    scrid.setAttribute('style','overflow-x: hidden;  overflow-y: scroll; color: '+textFontColor+'; font-size: '+fontSize+'; height: '+comHeight+'');
}

<!-- End scratchpad scrollbar functions-->

function fromWrapper() {
	lz.embed.setCanvasAttribute('fromWrapper','true');
}


function showMagnify() {		
		isMagnifierVisible = "true";
		if(jQuery("#magnifierWindow").length < 1){
			jQuery('body').addpowerzoom({
					defaultpower: 1.5,
					powerrange: [1.5,2],
					largeimage: null,
					magnifiersize: [300,100] 
				});
		}			
		jQuery.ajax({
			url: "servlet/PersistenceServlet.do?method=captureScreenshot",
			beforeSend: function() {
				jQuery(ddpowerzoomer.$magnifier.outer).hide();
			},
			dataType: "xml",
			success: function(data) {
				//xmlDoc = jQuery.parseXML( data ),
		    	xml = jQuery( data );
		    	ok = xml.find( "OK" );
				//alert(ok);
				var timeStamp = ok.text();//jQuery(jQuery.parseXML(data)).find("ok").text();
				//alert(timeStamp);
				var imageName = "cache/screenshot"+timeStamp+".png"
			
				jQuery(ddpowerzoomer.$magnifier.outer).css('left',(document.body.clientWidth/2 - 165)+ 'px');
				jQuery(ddpowerzoomer.$magnifier.outer).css('top',(document.body.clientHeight/2 - 75) + 'px');
				jQuery(ddpowerzoomer.$magnifier.outer).show();
				jQuery(ddpowerzoomer).initMagnify({largeimage:imageName});
			}
		});
			
		
	}
function hideMagnify() {		
	isMagnifierVisible = "false";
	jQuery(ddpowerzoomer.$magnifier.outer).hide();
}

/* Method to reinstate the magnifier to previous state */
function displayMagnifier() {	
	isMagnifierVisible = "true";
	jQuery(ddpowerzoomer.$magnifier.outer).show();
}

function setMouseUpVal() {
	lz.embed.setCanvasAttribute('mouseUpFired','false');
}

var currentFrameId;
function frameID(frameid){
	currentFrameId = frameid;
}
function isSPRequired(){
	var isSP = lz.embed.lzapp.getCanvasAttribute('scratchpadRequired');
	return isSP;
}
function enableHotKeys(arg) {
	gHotKeys.handleHtmlKeyPress(arg);
}
function enableManipBar(arg) {
	lz.embed.setCanvasAttribute('enableManipBar',arg);
}
function setIframeIsLoaded(){
	lz.embed.setCanvasAttribute('iframeIsLoaded',true);
}

function hideTooltip() {
    var isTooltip = lz.embed.lzapp.getCanvasAttribute('isTooltip');
    if(isTooltip == true || isTooltip == 'true')
		lz.embed.setCanvasAttribute('hideTooltip',"hildTooltip");
}

function enablePasswordFocus(){
	hideMagnify();
    var myFlash = document.getElementById( 'lzapp' );
     if ( myFlash ) { 
      myFlash.focus(); 
     }
}

jQuery(document).ready(function() {
 	jQuery('textarea').each(function(){
   		var $this = jQuery(this);
		jQuery(this).setAttribute('style',"overflow-x: hidden; overflow-y: hidden;");
	});
 });
 
 function destroyIframe(){
	var objIfrArr = jQ('body').find('iframe');
	for(var i=0; i < jQ(objIfrArr).size(); i++) {
		var obj = jQ(objIfrArr).eq(i);
		jQ(obj).remove();
	}
}

function checkForAssetArea()
{
	gController.setAttribute("mouseUpEventFired",true);
	
}

function checkTextDragging(){
	//jQuery("textarea").attr('disabled','disabled');
	jQuery("textarea").on("dragstart", function(e) {
				e.preventDefault();
			});
}

function preventTextDragging(){
	jQuery('.lzswfinputtext').on("dragstart", function(e) {
				e.preventDefault();
			});
}


function buttonClicked(magnifierPosition){
  	if(magnifierPosition == "T") {
  		$.setPreviousPosition();
  	}
	document.getElementById('mybutton').click();
	
}
 function closeMagnifier(){
    var olddiv = document.getElementById('mborder');
    if (olddiv && olddiv.parentNode && olddiv.parentNode.removeChild)
		olddiv.parentNode.removeChild(olddiv);
	$.closeMagnifier();
}

function disableShortcuts() {
	var keystroke = String.fromCharCode(event.keyCode).toLowerCase();
	if (event.ctrlKey) {
		switch(keystroke){
			case 'l':
			    if((gItemInterface.visible == true || gItemInterface.visible == 'true') &&  gRevisitFalsePopup.visible == false && gPleaseWaitPopup.visible == false) {
					gHotKeys.toggleMark();
					event.preventDefault(); // disable Ctrl+L
				}	
				break;
			case 's':
			    if((gItemInterface.visible == true || gItemInterface.visible == 'true') &&  gRevisitFalsePopup.visible == false && gPleaseWaitPopup.visible == false) {
					gHotKeys.stop();
					event.preventDefault(); // disable Ctrl+S
				}	
				break;
			case 'u':
			    if((gItemInterface.visible == true || gItemInterface.visible == 'true') &&  gRevisitFalsePopup.visible == false && gPleaseWaitPopup.visible == false) {
					gHotKeys.pause();
					event.preventDefault(); // disable Ctrl+U
				}	
				break;		
			case 'd':
				event.preventDefault(); // disable Ctrl+D
				break;
			case 'h':
				event.preventDefault(); // disable Ctrl+H
				break;
			case 'b':
				event.preventDefault(); // disable Ctrl+B
				break;
			case 'v':
				if(window.navigator.platform.indexOf('Mac')>=0){
					event.preventDefault(); // disable Ctrl+V
				}
				break;
			default:
				break;
				}
	}
}

function getOS() {
	var os = window.navigator.platform;
	return os;
}

//Changes for Te Item

function enableEraser(isEnabled){
	var iframe = $("iframe")[0];
	if(iframe){
		if(iframe.contentWindow.accomPkg!= null && iframe.contentWindow.accomPkg!=undefined){
			iframe.contentWindow.accomPkg.enableEraser(isEnabled);
			if(isEnabled){
				iframe.contentWindow.accomPkg.removeHighlighterCursor();
				iframe.contentWindow.accomPkg.setEraserCursor("../../includes/cursor_images/cursor_eraser.png");
			}else{
				iframe.contentWindow.accomPkg.removeHighlighterCursor();
			}
		}
	}
}

//var iframeLoaded = false;
 function iframeState(){
     	var iframe = $("iframe")[0];
     	console.log("iframe state");
     	if(iframe){
			  
		    setAnswerNow('complete');
		    //check whether iFrame loaded successfully or not
		    if(iframe != null
	    		&& iframe.contentWindow != null
	    		&& typeof iframe.contentWindow.accomPkg != 'undefined') {
	    		console.log("if");
		    	//iframeLoaded = true;
		    }
		    
     		var fontObj = getFontAccomodation();	
     		var bgColorObj = getBackColorAccomodation();				
     		
    		/*if(fontObj.hasFontMag){
    			iframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '18px',bgColorObj);
    		}
    		else {
    			iframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '12px',bgColorObj);
    		}*/
	    	
	    	//iframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '12px',bgColorObj);
	    	/*var xscalefact = (780 * xscalefactorjs)/800;
	    	var yscalefact = (450 * yscalefactorjs)/462;*/
	    	var xscalefact = 780/800;
	    	var yscalefact = 450/462;
	    	iframe.contentWindow.translate(xscalefact, yscalefact);
	    	lz.embed.setCanvasAttribute('frameLoaded',true);
	    	
	}
}

function isAnswered(){
 var elem = $("iframe")[0];
	if(elem){
	if(elem.contentWindow.accomPkg)
 	return elem.contentWindow.accomPkg.isItemAnswered();
  }        	
 
 }
 
 function enableHighlighter(isEnabled){
	var iframe = $("iframe")[0];
	if(iframe){
		if(iframe.contentWindow.accomPkg!= null && iframe.contentWindow.accomPkg!=undefined){
			iframe.contentWindow.accomPkg.enableHighlighter(isEnabled);
			if(isEnabled){
				iframe.contentWindow.accomPkg.removeHighlighterCursor();	
				iframe.contentWindow.accomPkg.setHighlighterCursor("../../includes/cursor_images/cursor_highliter.png");
			}else{
				iframe.contentWindow.accomPkg.removeHighlighterCursor();
			}
		}
	}
}

 function getState(){ 
  var elem = $("iframe")[0];
  if(elem){
 
 		if(elem.contentWindow.accomPkg){
	 		//console.log("state****"+JSON.stringify(elem.contentWindow.accomPkg.getState()));
	 		return elem.contentWindow.accomPkg.getState();
 		}
 	}     
 }
 
  function setState(htmlContent,jsonContent,checkedVals){
 	var elem = $("iframe")[0];
 	//console.log("inside setState");
    if(elem){	
        if(elem.contentWindow){
            if(elem.contentWindow.accomPkg){
                elem.contentWindow.accomPkg.setState(htmlContent,jsonContent,checkedVals);
            }else{
                setTimeout(function(){
                setState(htmlContent,jsonContent,checkedVals);
                }, 500);
            }
        }else{
            setTimeout(function(){
            setState(htmlContent,jsonContent,checkedVals);
            }, 500);
        }
    } else{
        setTimeout(function(){
        setState(htmlContent,jsonContent,checkedVals);
        }, 500);
    }
   gController.isAnsweredDelegate();         
 }


//-->
</script>
<style type="text/css">
body {
	background-color: #6691B4;
	margin: 0px;
	overflow: hidden;
}
#magnifierWindow {cursor: pointer;}
</style>
</HEAD>

<!--SA041005 start -->

<BODY onload="load()" onkeydown="disableShortcuts()" oncontextmenu="javascript:return false;">
<button id="mybutton" style="visibility:hidden; width:0px; height:0px; display:none;">clickMe </button>
<div id="needFlash9" style="display: none">
<table height="100%" width="100%">
	<tr>
		<td height="30%" width="100%" colspan="3">&nbsp;</td>
	</tr>
	<tr>
		<td height="40%" width="20%">&nbsp;</td>
		<td height="40%" width="60%" bgcolor="#527DA4">
		<table>
			<tr>
				<td valign="top" style="padding: 10px"><font color="white"
					size="6" face="Arial">The test cannot start because this
				computer is missing Flash 9.</font><br />
				</td>
			</tr>
			<tr>
				<td valign="top" style="padding: 20px"><font color="white"
					size="5" face="Arial">Please show this screen to the person
				in charge of testing and request a Flash 9 installation.</font></td>

			</tr>
			<tr>
				<td valign="bottom" style="padding: 20px" width="100%" align="right">
				<input type="image" src="images/close.png" onclick="closeBrowser();" />
				</td>
			</tr>
		</table>
		</td>
		<td height="40%" width="20%"></td>
	</tr>
	<tr>
		<td height="10%" width="20%">&nbsp;</td>
		<td height="10%" width="60%" valign="top"><font color="#527DA4"
			size="2" face="Arial">CTB/McGraw-Hill</font></td>
		<td height="10%" width="20%"></td>
	</tr>
	<tr>
		<td height="20%" width="100%" colspan="3">&nbsp;</td>
	</tr>
</table>
</div>

<SCRIPT type="text/javaScript">
		if(!isMac()){
			document.write('<OBJECT ID="LDBJSBridgeCTL" CLASSID="CLSID:56871AC2-4683-4D8E-B5EB-E7E3AA40DD52" VIEWASTEXT WIDTH=0 HEIGHT = 0></OBJECT>');
		}
	</SCRIPT>

<div id="appcontainer"></div>
<div id="lzsplash"
	style="z-index: 10000000; top: 0; left: 0; width: 100%; height: 100%; position: fixed; display: table">
<p style="display: table-cell; vertical-align: middle; align: center;">
<div id="lzsplashtext"
	style="display: block; margin: 20% auto; font-size: 12px; font-family: Helvetica, sans-serif;"
	align="center">Loading...</div>
</p>
</div>
<script type="text/javascript" defer>
 function loadTeJsApplication(){
 						try {
	                  lz.embed.dhtml({url: '/ContentReviewWeb/TestClientPageFlow/TestClient_ISTEP.js?servletUrl=/ContentReviewWeb/TestClientPageFlow', lfcurl: '/ContentReviewWeb/TestClientPageFlow/lps/includes/lfc/LFCdhtml.js', serverroot: '/ContentReviewWeb/TestClientPageFlow/lps/resources/', bgcolor: '#6691b4', width: '100%', height: '100%', id: 'lzapp', accessible: 'false', cancelmousewheel: false, cancelkeyboardcontrol: false, skipchromeinstall: false, usemastersprite: false, approot: '', appenddivid: 'appcontainer'});
                       setTimeout(function(){
                    	removeStatusDiv();
                        }, 500);
	                  lz.embed.applications.lzapp.onload = function loaded() {
	                    // called when this application is done loading
	                    //removeStatusDiv();
	                  }
	                  } catch (exception) {
	                  	//alert("Exception"+ exception.message);
	                  }
                  }
 function loadJsApplication(){
 						try {
	                  lz.embed.dhtml({url: '/ContentReviewWeb/TestClientPageFlow/TestClient.js?servletUrl=/ContentReviewWeb/TestClientPageFlow', lfcurl: '/ContentReviewWeb/TestClientPageFlow/lps/includes/lfc/LFCdhtml.js', serverroot: '/ContentReviewWeb/TestClientPageFlow/lps/resources/', bgcolor: '#6691b4', width: '100%', height: '100%', id: 'lzapp', accessible: 'false', cancelmousewheel: false, cancelkeyboardcontrol: false, skipchromeinstall: false, usemastersprite: false, approot: '', appenddivid: 'appcontainer'});
                       setTimeout(function(){
                    	removeStatusDiv();
                        }, 500);
	                  lz.embed.applications.lzapp.onload = function loaded() {
	                    // called when this application is done loading
	                    //removeStatusDiv();
	                  }
	                  } catch (exception) {
	                  	//alert("Exception"+ exception.message);
	                  }
                  }
                  function loadSwfApplication(){
                      lz.embed.resizeWindow('100%', '100%');
	                  lz.embed.swf({url: '/ContentReviewWeb/TestClientPageFlow/TestClient.lzx.swf?&lzr=swf10&servletUrl=<%=url%>&eliminatorResource=http://oascqa-ewdc.ctb.com/ContentReviewWeb/resources/eliminator.swf', allowfullscreen: 'true', bgcolor: '#6691B4', width: '100%', height: '100%', id: 'lzapp', accessible: 'false', cancelmousewheel: false, appenddivid: 'appcontainer', wmode: 'transparent'});
					  //lz.embed.swf({url: 'TestClient.lzx.swf?&lzr=swf10&folder=calif&servletUrl=http://192.168.2.2:12345/servlet/fixed&eliminatorResource=resources/eliminator.swf', allowfullscreen: 'false', bgcolor: '#6691B4', width: '100%', height: '100%', id: 'lzapp', accessible: 'false', cancelmousewheel: false, appenddivid: 'appcontainer'});
					  	
	                  lz.embed.applications.lzapp.onloadstatus = function loadstatus(p) {
	                    // called with a percentage (0-100) indicating load progress
	                    var el = document.getElementById('lzsplashtext');
	                    if (el) {
	                        if (p == 100) {
	                            var splash = document.getElementById('lzsplash');
	                            if (splash) {
	                                splash.parentNode.removeChild(splash);
	                            }
	                        } else {
	                            el.innerHTML = p + '% loaded'
	                        }
	                    }
	                  }
	
	                  lz.embed.applications.lzapp.onload = function loaded() {
	                    // called when this application is done loading and the 
	                    // canvas has initted
	                  }
	              }
                  
                  function removeStatusDiv() {
                    var el = document.getElementById('lzsplashtext');
                        if (el) {
                            var splash = document.getElementById('lzsplash');
                            if (splash) {
                                splash.parentNode.removeChild(splash);
                            }
	                    }
                  }
   /*               lz.embed.resizeWindow('100%', '100%');
                  lz.embed.swf({url: '/ContentReviewWeb/TestClientPageFlow/TestClient.lzx.swf?&lzr=swf10&servletUrl=<%=url%>&eliminatorResource=http://oascqa-ewdc.ctb.com/ContentReviewWeb/resources/eliminator.swf', allowfullscreen: 'true', bgcolor: '#6691B4', width: '100%', height: '100%', id: 'lzapp', accessible: 'false', cancelmousewheel: false, appenddivid: 'appcontainer', wmode: 'transparent'});
				  //lz.embed.swf({url: 'TestClient.lzx.swf?&lzr=swf10&folder=calif&servletUrl=http://192.168.2.2:12345/servlet/fixed&eliminatorResource=resources/eliminator.swf', allowfullscreen: 'false', bgcolor: '#6691B4', width: '100%', height: '100%', id: 'lzapp', accessible: 'false', cancelmousewheel: false, appenddivid: 'appcontainer'});
				  	
                  lz.embed.applications.lzapp.onloadstatus = function loadstatus(p) {
                    // called with a percentage (0-100) indicating load progress
                    var el = document.getElementById('lzsplashtext');
                    if (el) {
                        if (p == 100) {
                            var splash = document.getElementById('lzsplash');
                            if (splash) {
                                splash.parentNode.removeChild(splash);
                            }
                        } else {
                            el.innerHTML = p + '% loaded'
                        }
                    }
                  }

                  lz.embed.applications.lzapp.onload = function loaded() {
                    // called when this application is done loading and the 
                    // canvas has initted
                  }
                  
      */            
                </script>
<noscript>Please enable JavaScript in order to use this
application.</noscript>
</body>
</html>