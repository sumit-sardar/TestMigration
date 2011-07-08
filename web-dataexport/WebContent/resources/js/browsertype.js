/**
 * Browsertype.js
 *
 * @author  Actuate Corporation
 * @version 1.0
 * $Revision$
 */

// NOTE: The Browser Type class and the functions in this file are to be used with the
// DOM-compliant browsers, namely IE 4 and above, and Netscape 6.  The Netscape 4.x code is
// separated from the rest of the code using the variable (ns4).

function BrowserType ()
{
	//////////////////// Name, Version, Platform /////////////////
	if ( parseInt(navigator.appVersion.charAt(0)) >= 4 ) 
	{
	    this.mozillaVersion = 0.0;
		if (navigator.appName == "Netscape") {
			this.name = "Netscape";
		
			this.version = parseFloat(window.navigator.appVersion);

			// Platform names should follow a standard
			this.platform = window.navigator.platform;
			
        	n = navigator.userAgent.indexOf("rv:");
			this.mozillaVersion = parseFloat(navigator.userAgent.substring(n + 3 , navigator.userAgent.indexOf(")", n + 3) ));
		}

		if (navigator.appName == "Microsoft Internet Explorer") {
			this.name = "IE";

			// Parse version string
			var msie  = (window.navigator.appVersion.split(';'))[1];
			var iever = (msie.split(' '))[2];
			this.version = parseFloat(iever);

			// Platform names should follow a standard
			this.platform = window.navigator.platform;
		}

		if (this.platform.indexOf("SunOS") != -1) 
			this.platform = "SunOS"

		if (this.platform.indexOf("Mac") != -1) 
			this.platform = "Mac"
	}
	
	///////////////////////////////////////// FEATURES /////////////////////////////////////////////////////////

	//////////////////// document.all feature ///////////////										   
	this.needsDocumentAll = (window.document.all && !window.document.getElementById)? true:false;

	//////////////////// insertAdjacentHTML(), insertAdjacentText() insertAdjacentElement() features /////////////////////
	this.needsInsertAdjacent = ((this.name == "IE") && (this.version < 5))? true:false;
	this.supportsInsertAdjacent = (this.name == "IE")? true:false;

	//////////////////// offsetParent feature ////////////////////
	this.needsOffsetParent = ((this.name == "IE") && (this.version < 5))? true:false;

	//////////////////// event.srcElement feature /////////////// 
	this.needsEventSrcElement = (this.name == "IE")? true:false;

	//////////////////// event.keyCode feature ////////////////////
	this.needsEventKeyCode = (this.name == "IE") ? true:false;

	//////////////////// Unescaping hyperlink parameters feature /////////////// 
	this.unescapesHyperlinkParams = (this.name == "IE")? true:false;


	/////////////////////////////////////////// BUGS ///////////////////////////////////////////////////////

	//////////////////// Dynamic Frameset adjustment bug ////////////////////
	// Netscape 6 cannot dynamically adjust nested framesets in Javascript, e.g. you cannot change a frameset from
	// rows='60%, *' to rows='30%, *' unless it is a top-level frameset.
	this.cannotDynamicallyAdjustFrameset = ((this.name == "Netscape") && (this.version == 5) && (this.mozillaVersion < 1.1))? true:false;

	//////////////////// "Image Dimensions in points" bug /////////////// 
	// For MSIE 5.0 on MacOS, the width and height of the image should be in points and should be exactly
	// equal to the height and width of the DIV containing it, to avoid overflow.
	this.needsImageDimensions = ((this.name == "IE") && (this.platform == "Mac"))? true:false;	

	//////////////////// Onload handler not called bug ///////////////
	// Netscape 6 does not always call the onLoad handler for frames.
	this.doesNotAlwaysCallOnloadHandler = ((this.name == "Netscape") && (this.version == 5))? true:false;

	//////////////////// Frame not loaded bug ///////////////
	// Netscape 6 does not always load frames when the location.replace() function is called.
	// One way to work around this is to do a dummy replace first
	this.doesNotAlwaysLoadFrame = ((this.name == "Netscape") && (this.version == 5))? true:false;

	//////////////////// DIV Does not adjust width,height dynamically bug ////////////////////
	// When the contents of a variable-width DIV are redrawn, the adjacent DIVs should be redrawn 
	// (e.g. to collapse an item in the TOC). Netscape 6 does not do this until the DIV's dimensions
	// are changed.
	this.doesNotAdjustDimensionsDynamically = ((this.name == "Netscape") && (this.version == 5))? true:false;

	//////////////////// Submits form items in opposite order from how they are added -- bug? ////////////////////
	// Netscape 6 POSTs the items in a form in the opposite order from which they appear in the form.
	// This can lead to search results being displayed backward.
	this.submitsFormItemsBackward = ((this.name == "Netscape") && (this.version == 5))? true:false;

	//////////////////// Browser crashes when window.open is called with default params -- bug ////////////////////
	// Netscape 6 on SunOS crashes when opening a new window, unless a set of parameters determining the window
	// appearance is passed into the window.open() call.
	this.defaultWindowOpenCrashesBrowser = ((this.name == "Netscape") && (this.version == 5) && (this.platform == "SunOS"))? true:false;

	//////////////////// Browser cannot extract search Reults in CSV, TSV formats -- bug ////////////////////
	// Netscape 6 either crashes or fails to get the data when we try to extract Search results
	// in CSV or TSV.
	// We are no longer support NS 6.0.  NS 6.2 and up are working fine with POST.  We changed this to false by default
	this.postedDataLostGettingCSV = false;

	//////////////////// Replacing a frame causes history list to malfunction ////////////////////
	// Netscape 6 history list cannot go back to a location that was replace()d before navigating elewhere
	this.replaceFrameBreaksHistoryList = ((this.name == "Netscape") && (this.version == 5))? true:false;
}

var g_browserType = new BrowserType;




///////////////////// Functions for general use /////////////////////////

////////////// Use this instead of document.all for browsers except Netscape 4.x
function elementLocate ( targetDocument, theId )
{
	if (g_browserType.needsDocumentAll)
	{
		return targetDocument.all[theId];
	}
	else
	{
		var candidate;
		candidate = targetDocument.getElementById(theId);
		if (!candidate)
		{	
			candidate = (targetDocument.getElementsByName(theId))[0];
		}
		return candidate;
	}
}

///////////////////////////////////////////////////////////////////////////////

////////////// Use this instead of insertAdjacentHTML()
function HTMLInsert (element, where, htmlStr)
{
	if (g_browserType.supportsInsertAdjacent)
	{
		element.insertAdjacentHTML(where, htmlStr);
	}
	else
	{
		var r = element.ownerDocument.createRange();
		r.setStartBefore(element);
		var parsedHTML = r.createContextualFragment(htmlStr);
		elementInsert(element, where, parsedHTML);
	}
}

///////////////////////////////////////////////////////////////////////////////

////////////// Use this instead of insertAdjacentElement()
function elementInsert(parentElement, where, parsedNode)
{
	if (g_browserType.supportsInsertAdjacent) 
	{
		parentElement.insertAdjacentElement(where, parsedNode);	
	}
	else 
	{
		switch (where){
			case 'BeforeBegin':
				parentElement.parentNode.insertBefore(parsedNode,parentElement);
				break;
			case 'AfterBegin':
				parentElement.insertBefore(parsedNode,parentElement.firstChild);
				break;
			case 'BeforeEnd':
				parentElement.appendChild(parsedNode);
				break;
			case 'AfterEnd':
				if (parentElement.nextSibling){
					parentElement.parentNode.insertBefore (parsedNode,parentElement.nextSibling);
				} 
				else {
					parentElement.parentNode.appendChild(parsedNode);
				}
				break;
		}
	}
}
///////////////////////////////////////////////////////////////////////////////

////////////// Use this to delete a DIV
function elementDelete(element)
{
	if (g_browserType.supportsInsertAdjacent) 
	{
		element.style.visibility = "hidden";
		element.innerHTML = "";
		element.outerHTML = "";
	}
	else
	{
		getElementParent(element).removeChild(element);
	}
}

///////////////////////////////////////////////////////////////////////////////

////////////// Use this instead of .offsetParent for the parent of a DIV
function getElementParent (element)
{
	if (g_browserType.needsOffsetParent)
		return element.offsetParent;
	else
		return element.parentNode;
}


///////////////////////////////////////////////////////////////////////////////

////////////// Use this instead of event.target or event.srcElement
function getEventTarget (event)
{
	if (g_browserType.needsEventSrcElement)
	{
		return event.srcElement;
	}
	else
	{
		return event.target;
	}
}

////////////// Use this instead of event.keycode or event.which
function getEventKeyCode (keypressed)
{
	if (g_browserType.needsEventKeyCode) 
	{
		return window.event.keyCode;
	}
	else 
	{
		return keypressed.which;		
	}
}


///////////// Use this instead of window.open
function openNewDefaultWindow(url, windowName)
{
	if (g_browserType.defaultWindowOpenCrashesBrowser)
		return window.open( url, windowName, "scrollbars=yes, toolbar=yes, menubar=yes, resizable=yes,status=yes,location=yes,directories=yes");
	else 
		return window.open( url, windowName);
}

//////////// window.open, center on opener, and focus it
function openPopupWindow( url, windowName, newWidth, newHeight, parameters )
{

	var newTop = 0, newLeft = 0;
	

	if (  newWidth != '' &&  newHeight != '' )
	{
		if (window.outerHeight) {
			newTop = window.screenY + ((window.outerHeight - newHeight) / 2);
		}
		 else {
			newTop = top.screenTop + ((top.document.body.offsetHeight - newHeight) /2);
		}
		if (window.outerWidth) {
			newLeft = window.screenX + ((window.outerWidth - newWidth) / 2);
		}
		 else {
			newLeft = top.screenLeft + ((top.document.body.offsetWidth - newWidth) /2);
		}
		
		popupWindow = window.open(url,windowName,'width=' + newWidth + ',height='+newHeight+',top='+newTop+',left='+newLeft+',' + parameters);
		popupWindow.focus( );
		return popupWindow;
	}
	else
	{

		popupWindow = window.open(url,windowName, parameters);
		popupWindow.focus( );
		return popupWindow;
	}
} 

function autoCenter( newWidth, newHeight )
{
	var newTop = 0, newLeft = 0;
	if (window.outerHeight) {
		newTop = window.screenY + ((window.outerHeight - newHeight) / 2);
	} else {
		newTop = window.screenTop + ((document.body.offsetHeight - newHeight) /
	2);
	}
	if (window.outerWidth) {
		newLeft = window.screenX + ((window.outerWidth - newWidth) / 2);
	} else {
		newLeft = window.screenLeft + ((document.body.offsetWidth - newWidth) /
	2);
	}
	
	self.moveTo( newLeft, newTop );
	window.resizeTo( newWidth, newHeight );
}
///////////// Use this as a dummy HREF
function myVoid()
{
}


