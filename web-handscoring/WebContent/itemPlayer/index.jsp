
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<TITLE> Presentation Canvas </TITLE>
<META NAME="Generator" CONTENT="Microsoft FrontPage 4.0">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">

<!--
 Changed the embed.js path to local folder.
=>STARTS
-->
<style type="text/css">
       html, body
         {
             /* http://www.quirksmode.org/css/100percheight.html */
             height: 100%;
             /* prevent browser decorations */
             margin: 0;
             padding: 0;
             border: 0 none;
             /*text-rendering:optimizeLegibility;*/
             font-family: CTB !important;
         }
         body {
             background-color: #6691b4;
             -webkit-font-smoothing: antialiased;
			/* -moz-font-smoothing: antialiased; - No longer available in FF */
			font-smoothing: antialiased;
			/*webkit-text-stroke: 1px transparent;*/
	       /*text-shadow: 0 0 1px rgba(0,0,0,0.1);*/
	       /*letter-spacing: -0.06em;*/
        }
        img { border: 0 none; }
         
     </style>

<SCRIPT SRC="includes/embed.js" type="text/javascript"></SCRIPT>
<SCRIPT SRC="includes/laslinks_utils.js" type="text/javascript">
</SCRIPT>
<script type="text/javascript" src="lps/includes/embed-compressed.js"></script>
<script type="text/javascript" src="lps/jquery-1.5.2.js"></script>
<script type="text/javascript" src="lps/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="assets/jquery-1.8.3.js"></script>
<script type="text/javascript" src="assets/jquery-ui-1.9.2.custom.min.js"></script>
<!--  <script type="text/javascript" src="lps/resources/lps/includes/excanvas.js"></script> -->
<SCRIPT LANGUAGE="JavaScript">
xmlStr="";
var laszlouid = 0;
var req;
var xmlDoc;

<%
    String eliminatorResource = "eliminator.swf";
	String itemNumber = request.getParameter("itemNumber");
	String itemSortNumber = request.getParameter("itemSortNumber");	
	String url = request.getRequestURL().toString().trim();
    int lastSlash = url.lastIndexOf("/");
    url = url.substring(0,lastSlash).trim();
    //alert("url "+url);
%>
 var xscalefactorjs;
var yscalefactorjs;
var LASAssetPath = "http:items/"; 
var currentLasAssetItemId;
var autoPlayEvent = "false";

function getLasAssetPath(){
	lz.embed.setCanvasAttribute("LASAssetPath", LASAssetPath);
}

	function showFootnote(header)
	{
	   	lzSetCanvasAttribute("footnotedata", header);
	}
	
	
	function closeBrowser()
	{
	    parent.close();
	}
	
	function updateLDB()
	{
	    // do nothing, need this since TestClient.lzx.swf calls it. 
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

var currentFrameId;
function frameID(frameid){
	currentFrameId = frameid;
}

function hideScrollbarForCRitem(arg){
    var crItemid = document.getElementById(arg);
    var fontcolor = getCRcolor();
    crItemid.setAttribute('style','overflow-x: hidden; overflow-y: scroll; color: '+fontcolor+';');
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

function checkTextDragging(){
	jQuery("textarea").on("dragstart", function(e) {
				e.preventDefault();
			});
}

function setCurLasItemId(id) {
	currentLasAssetItemId = id;
	assetCount = 0;
	checkAnswered = false;
	//answerClicked = null;
	//canNotAnswerFlag = false;
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
 		/*if(autoPlayEvent == 'false') {
 			unlockResponseArea(currIframeId);
 			}*/
 		 		
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
	 		/*if(answerClicked != null){
	 			checkValIfAnswered(answerClicked);
	 			answerClicked = null;
	 		}*/
 		
 		//restrictNavigation('unlock');
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

function enableHotKeys(arg) {
	//do Nothing
}
  	

	function load() {
	    <%
	    String parentProductId = request.getParameter("parentProductId");
	    %>
	     var productTypeVal = "<%=parentProductId%>";	     
	     if(productTypeVal == '7500'){
	     	loadJSApplication();
	     }
	     else {
	      	loadSWFApplication();
	     }
	}
	
$(document).ready(function() {
	if($.browser.mozilla){
		$("head").append("<link rel='stylesheet' href='css/moz-font.css' type='text/css' />");
	}else{
		$("head").append("<link rel='stylesheet' href='css/tdc-font.css' type='text/css' />");
	}
});
</SCRIPT>

</HEAD>



<BODY topmargin=0 leftmargin=0 rightmargin=0 bottommargin=0 scroll=no onload="load()" bgcolor="#6691B4">
<div id="flashcontent">
</div>
<script type="text/javascript" defer>
				  var jQ = jQuery.noConflict();
function loadJSApplication(){
     		//alert("js");     		 
	        //lz.embed.dhtml({url: 'ItemViewer.js?itemNum=<%=itemNumber%>&visibleItemNum=<%=itemSortNumber%>&servletUrl=<%=url%>&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('includes/h.html?h='),lfcurl: 'lps/includes/lfc/LFCdhtml.js', serverroot: 'lps/resources/', bgcolor: '#6691b4', width: '100%', height: '100%', appenddivid: 'appcontainer'});
	        //lz.embed.dhtml({url: 'ItemViewer.js?lfcurl: 'lps/includes/lfc/LFCdhtml.js', serverroot: 'lps/resources/', bgcolor: '#6691b4', width: '100%', height: '100%', id: 'lzapp', accessible: 'true', cancelmousewheel: false, cancelkeyboardcontrol: false, skipchromeinstall: false, usemastersprite: false, approot: ''});
	        lz.embed.dhtml({url: 'ItemViewer.js?servletUrl=<%=url%>&itemNum=<%=itemNumber%>&visibleItemNum=<%=itemSortNumber%>',lfcurl: 'lps/includes/lfc/LFCdhtml.js', serverroot: 'lps/resources/', bgcolor: '#6691b4', width: '100%', height: '100%', id: 'lzapp', accessible: 'false', cancelmousewheel: false, cancelkeyboardcontrol: false, skipchromeinstall: false, usemastersprite: false, approot: '', appenddivid: 'appcontainer'});
	       // lzHistEmbed('includes');
    }
  	function loadSWFApplication(){
  	  	  //alert("swf");
		  lzEmbed({url: 'ItemViewer.lzx.swf?lzt=swf&itemNum=<%=itemNumber%>&visibleItemNum=<%=itemSortNumber%>&servletUrl=<%=url%>&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('includes/h.html?h='), bgcolor: '#6691B4"',  width: '100%', height: '100%'});
	      lzHistEmbed('includes');
    }
</script>
</BODY>
</HTML>
