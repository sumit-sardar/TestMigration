
var gPopupMask = null;
var gPopupContainer = null;
var gPopFrame = null;
var gReturnFunc;
var gPopupIsShown = false;
var gHideSelects = false;
var gLoading = "progress_modal.jsp";

var gTabIndexes = new Array();
// Pre-defined list of tags we want to disable/enable tabbing into
var gTabbableTags = new Array("A","BUTTON","TEXTAREA","INPUT","IFRAME");	

// trap invalid keys
document.onkeypress = keyPressHandler;

/**
 * Override the loading page from loading.html to something else
 */
function setPopUpLoadingPage(loading) {
	gLoading = loading;
}

/**
 * Initializes popup code on load.	
 */
function initPopUp() {
	// Add the HTML to the body
	var body = document.getElementsByTagName('body')[0];
	var popmask = document.createElement('div');
	popmask.id = 'popupMask';
	var popcont = document.createElement('div');
	popcont.id = 'popupContainer';
    
	popcont.innerHTML = '' +
		'<div id="popupInner">' +
			'<div id="popupTitleBar">' +
				'<div id="popupTitle"></div>' +
                /*
				'<div id="popupControls">' +
					'<a onclick="hidePopWin(true);"><span>Close</span></a>' +
				'</div>' +
                */
			'</div>' +
			'<iframe src="'+gLoading+'" style="width:100%;height:100%;background-color:transparent;" scrolling="auto" frameborder="0" allowtransparency="true" id="popupFrame" name="popupFrame" width="100%" height="100%"></iframe>' +
		'</div>';

	body.appendChild(popmask);
	body.appendChild(popcont);
	
	gPopupMask = document.getElementById("popupMask");
	gPopupContainer = document.getElementById("popupContainer");
	gPopFrame = document.getElementById("popupFrame");	
	
    var popupDocument = gPopFrame.document;        
    if (popupDocument != null) {
        popupDocument.onkeypress = keyPressHandler;
    }
    
    // to resolve defect on Safari 2.0 on Mac OS
    if (isOldSafariOnMac()) {
        gPopupContainer.style.display = "block";	
        gPopupContainer.style.top = "0px";
        gPopupContainer.style.left =  "-20px";
        gPopupContainer.style.width = "0px";
        gPopupContainer.style.height =  "0px";
    }
    
	// check to see if this is IE version 6 or lower. hide select boxes if so
	// maybe they'll fix this in version 7?
	var brsVersion = parseInt(window.navigator.appVersion.charAt(0), 10);
	if (brsVersion <= 6 && window.navigator.userAgent.indexOf("MSIE") > -1) {
		gHideSelects = true;
	}
	
	// Add onclick handlers to 'a' elements of class submodal or submodal-width-height
	var elms = document.getElementsByTagName('a');
	for (i = 0; i < elms.length; i++) {
		if (elms[i].className.indexOf("submodal") >= 0) { 
			elms[i].onclick = function(){
				// default width and height
				var width = 400;
				var height = 150;
				// Parse out optional width and height from className
				var startIndex = this.className.indexOf("submodal");
				var endIndex = this.className.indexOf(" ", startIndex);
				if (endIndex < 0) {
					endIndex = this.className.length;
				}
				var clazz = this.className.substring(startIndex, endIndex);
				params = clazz.split('-');
				if (params.length == 3) {
					width = parseInt(params[1]);
					height = parseInt(params[2]);
				}
				showPopWin(this.href,width,height,null); return false;
			}
		}
	}
}
addEvent(window, "load", initPopUp);

 /**
	* @argument width - int in pixels
	* @argument height - int in pixels
	* @argument url - url to display
	* @argument returnFunc - function to call when returning true from the window.
	*/

function showPopWin(url, width, height, returnFunc) {

	gPopupIsShown = true;
	disableTabIndexes();
	gPopupMask.style.display = "block";
	gPopupContainer.style.display = "block";
	centerPopWin(width, height);
	var titleBarHeight = parseInt(document.getElementById("popupTitleBar").offsetHeight, 10);
	gPopupContainer.style.width = width + "px";
	gPopupContainer.style.height = (height+titleBarHeight) + "px";
	gPopFrame.style.width = parseInt(document.getElementById("popupTitleBar").offsetWidth, 10) + "px";
	gPopFrame.style.height = (height) + "px";
	gPopFrame.src = url;
	gReturnFunc = returnFunc;
	// for IE
	if (gHideSelects == true) {
		hideSelectBoxes();
	}

	window.setTimeout("setPopTitleAndRewriteTargets();", 100);
}

//
var gi = 0;
function centerPopWin(width, height) {
	if (gPopupIsShown == true) {
		if (width == null || isNaN(width)) {
			width = gPopupContainer.offsetWidth;
		}
		if (height == null) {
			height = gPopupContainer.offsetHeight;
		}
		var fullHeight = getViewportHeight();
		var fullWidth = getViewportWidth();
		// scLeft and scTop changes by Thomas Risberg
		var scLeft,scTop;
		if (self.pageYOffset) {
			scLeft = self.pageXOffset;
			scTop = self.pageYOffset;
		} else if (document.documentElement && document.documentElement.scrollTop) {
			scLeft = document.documentElement.scrollLeft;
			scTop = document.documentElement.scrollTop;
		} else if (document.body) {
			scLeft = document.body.scrollLeft;
			scTop = document.body.scrollTop;
		} 
		gPopupMask.style.height = fullHeight + "px";
		gPopupMask.style.width = fullWidth + "px";
		gPopupMask.style.top = scTop + "px";
		gPopupMask.style.left = scLeft + "px";
		//window.status = gPopupMask.style.top + " " + gPopupMask.style.left + " " + gi++;
		var titleBarHeight = parseInt(document.getElementById("popupTitleBar").offsetHeight, 10);
		var topMargin = scTop + ((fullHeight - (height+titleBarHeight)) / 2);
		if (topMargin < 0) { topMargin = 0; }
		gPopupContainer.style.top = topMargin + "px";
		gPopupContainer.style.left =  (scLeft + ((fullWidth - width) / 2)) + "px";
	}
}
addEvent(window, "resize", centerPopWin);
//addEvent(window, "scroll", centerPopWin);
window.onscroll = centerPopWin;

/**
 * @argument callReturnFunc - bool - determines if we call the return function specified
 * @argument returnVal - anything - return value 
 */
function hidePopWin(callReturnFunc) {
    
	gPopupIsShown = false;
	restoreTabIndexes();
	if (gPopupMask == null) {
		return;
	}
	gPopupMask.style.display = "none";
	gPopupContainer.style.display = "none";
	if (callReturnFunc == true && gReturnFunc != null) {
		gReturnFunc(window.frames["popupFrame"].returnVal);
	}
	gPopFrame.src = gLoading;
	// display all select boxes
	if (gHideSelects == true) {
		displaySelectBoxes();
	}
    
	if (callReturnFunc == true) {
        cancelProcess();
    }    
}

/**
 * Sets the popup title based on the title of the html document it contains.
 * Also adds a base attribute so links and forms will jump out out of the iframe
 * unless a base or target is already explicitly set.
 * Uses a timeout to keep checking until the title is valid.
 */
function setPopTitleAndRewriteTargets() {
	if (window.frames["popupFrame"].document.title == null) {
		window.setTimeout("setPopTitleAndRewriteTargets();", 10);
	} else {
		var popupDocument = window.frames["popupFrame"].document;        
		document.getElementById("popupTitle").innerHTML = popupDocument.title;    
		if (popupDocument.getElementsByTagName('base').length < 1) {
			var aList  = window.frames["popupFrame"].document.getElementsByTagName('a');
			for (var i = 0; i < aList.length; i++) {
				if (aList.target == null) aList[i].target='_parent';
			}
			var fList  = window.frames["popupFrame"].document.getElementsByTagName('form');
			for (i = 0; i < fList.length; i++) {
				if (fList.target == null) fList[i].target='_parent';
			}
		}
        /*        
        var theFileName = document.getElementById("{actionForm.theFile}").value;        
        var tDiv = popupDocument.getElementById("titleDiv");
        if (tDiv != null) {
            tDiv.innerHTML = theFileName;  
        }        
        */
	}
}


// @argument e - event - keyboard event that caused this function to be called.
function keyPressHandler(e) {
    if (gPopupIsShown) { 
        var keyId = (window.event) ? event.keyCode : e.which;    
        if (keyId == 27) {
            hidePopWin(false);
            return false;
        }
    }
}

// For IE.  Go through predefined tags and disable tabbing into them.
function disableTabIndexes() {
	if (document.all) {
		var i = 0;
		for (var j = 0; j < gTabbableTags.length; j++) {
			var tagElements = document.getElementsByTagName(gTabbableTags[j]);
			for (var k = 0 ; k < tagElements.length; k++) {
				gTabIndexes[i] = tagElements[k].tabIndex;
				tagElements[k].tabIndex="-1";
				i++;
			}
		}
	}
}

// For IE. Restore tab-indexes.
function restoreTabIndexes() {
	if (document.all) {
		var i = 0;
		for (var j = 0; j < gTabbableTags.length; j++) {
			var tagElements = document.getElementsByTagName(gTabbableTags[j]);
			for (var k = 0 ; k < tagElements.length; k++) {
				tagElements[k].tabIndex = gTabIndexes[i];
				tagElements[k].tabEnabled = true;
				i++;
			}
		}
	}
}

/**
* Hides all drop down form select boxes on the screen so they do not appear above the mask layer.
* IE has a problem with wanted select form tags to always be the topmost z-index or layer
* Thanks for the code Scott!
*/
function hideSelectBoxes() {
	for(var i = 0; i < document.forms.length; i++) {
		for(var e = 0; e < document.forms[i].length; e++){
			if(document.forms[i].elements[e].tagName == "SELECT") {
				document.forms[i].elements[e].style.visibility="hidden";
			}
		}
	}
}

/**
* Makes all drop down form select boxes on the screen visible so they do not reappear after the dialog is closed.
* IE has a problem with wanted select form tags to always be the topmost z-index or layer
*/
function displaySelectBoxes() {
	for(var i = 0; i < document.forms.length; i++) {
		for(var e = 0; e < document.forms[i].length; e++){
			if(document.forms[i].elements[e].tagName == "SELECT") {
			document.forms[i].elements[e].style.visibility="visible";
			}
		}
	}
}

/**
 * X-browser event handler attachment and detachment
 * @argument obj - the object to attach event to
 * @argument evType - name of the event - DONT ADD "on", pass only "mouseover", etc
 * @argument fn - function to call
 */
function addEvent(obj, evType, fn){
 if (obj.addEventListener){
    obj.addEventListener(evType, fn, false);
    return true;
 } else if (obj.attachEvent){
    var r = obj.attachEvent("on"+evType, fn);
    return r;
 } else {
    return false;
 }
}

/**
 * Code below taken from - http://www.evolt.org/article/document_body_doctype_switching_and_more/17/30655/ *
 * Modified 4/22/04 to work with Opera/Moz (by webmaster at subimage dot com)
 * Gets the full width/height because it's different for most browsers.
 */
function getViewportHeight() {
	if (window.innerHeight!=window.undefined) return window.innerHeight;
	if (document.compatMode=='CSS1Compat') return document.documentElement.clientHeight;
	if (document.body) return document.body.clientHeight; 
	return window.undefined; 
}

function getViewportWidth() {
	if (window.innerWidth!=window.undefined) return window.innerWidth; 
	if (document.compatMode=='CSS1Compat') return document.documentElement.clientWidth; 
	if (document.body) return document.body.clientWidth; 
	return window.undefined; 
}

function isOldSafariOnMac()
{
    var agt = navigator.userAgent.toLowerCase();
    var mac =(agt.indexOf("mac") != -1);
    var safari =(agt.indexOf("safari") != -1);
    var oldVersion =(agt.indexOf("418") != -1);
    
    return (mac && safari && oldVersion);    
}

function cancelProcess() {

    //document.location.href = "/OrganizationManagementWeb/manageUpload/cancelUploadData.do";    
    
    //document.forms[0].action = "cancelUploadData.do";
    //document.forms[0].submit();    
}

