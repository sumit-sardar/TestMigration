<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<% response.setContentType("application/x-javascript"); %>

var acknowledgmentsWindow = null;

function constrainNumericChar(e) 
{
    var keyId = (window.event) ? event.keyCode : e.which;
    var keyValue = String.fromCharCode( keyId );
    var regExp = /\d/;    
    var results = false;
   
    if ( keyId == null || keyId == 0 || keyId == 8 || keyId == 9 || keyId == 13 || keyId == 27 ) {        
        results = true; // allow control characters
    } 
    else 
    if ( regExp.test(keyValue) ) {
        results = true;    
    }
    
    return results;
}

function focusNextControl(element)
{   
    if (element.value.length < element.getAttribute('maxlength')) 
        return;

    var f = element.form;
    var els = f.elements;
    var len = els.length;
    var x, nextEl;
    
    for (var i=0 ; i < len ; i++) {
        x = els[i];
            if (element == x && (nextEl = els[i+1])) {
                if(nextEl.value == ""){
                    if (nextEl.focus) 
                        nextEl.focus();
                    return;
                }
            }
        }
}

function enableButtons() { 
    enableElementById("View");
    enableElementById("Edit");
    enableElementById("Delete");
    enableElementById("Add");
}

function getSafeElement( elementId ) {
	var element = document.getElementById(elementId);
	if (element == null) {
		var elements = document.getElementsByName(elementId);
		if (elements != null) {
			element = elements[0];
		}
	}
	return element;
}

function openFile() {    
    var inputs=document.getElementsByTagName("input");
    for (var i=0; i < inputs.length; i++) {
        if (inputs[i].getAttribute("type") == "radio") {
            if (inputs[i].checked == true) {
                var filename = inputs[i].value;
                var url = "/mock/" + filename;
                location.href  = url;
            }
        }
    }
}

function submitToAction(toAction, subtestId, status) {
    document.body.style.cursor = 'wait';
    
    var actionElement = getSafeElement('{actionForm.actionElement}');
    actionElement.value = subtestId;
    
    var currentAction = getSafeElement('{actionForm.currentAction}');
    currentAction.value = status;
    
    actionElement.form.action = toAction;
    actionElement.form.submit();
    return false;
}

function setupStates(name) { 
    enableElementById("View");
    enableElementById("Edit");
    enableElementById("Delete");
    enableElementById("Add");

    var element = getSafeElement("{actionForm.selectedOrgNodeName}");
    if (element) {
        element.value = name;
    }
}

function setFocusFirstElement() { 

    var element = getSafeElement("firstFocusId");
    if (element) {
        try{
            setFocus(element.value);
        }
        catch(err){
        }
    }
}
 
function setFocus(id) { 
    var element = document.getElementById(id);
    if (element != null) {
        element.focus();
    }
}

function selectAllCheckboxes() { 
    var d=document;
    var inputs=d.getElementsByTagName("input");
    for(var i=0; i < inputs.length; i++) {
        if(inputs[i].getAttribute("type") == "checkbox"){
          inputs[i].checked=true;
        }
    }
}


function clearAllCheckboxes() { 
    var d=document;
    var inputs=d.getElementsByTagName("input");
    for(var i=0; i < inputs.length; i++) {
        if(inputs[i].getAttribute("type") == "checkbox"){
          inputs[i].checked=false;
        }
    }
}

function handleEnterKey(elementId, value) {
  if (window.event && window.event.keyCode == 13) {
        var element = document.getElementById(elementId);
        if(element) {
            element.value = value;
            var actionElement = getSafeElement('{actionForm.actionElement}');
            if(actionElement) {
                actionElement.value = elementId;
            }
            element.form.submit();
            return false;
        }        
  }
  return true;
}

function verifyDeleteOrganization(){
    var ret = confirm("Click 'OK' to delete this Organization member.");    
    if (ret == true) {
        setElementValue('{actionForm.currentAction}', 'deleteOrganization')    
        return true;
    }
    return false;    
}

function verifyDeleteFile(){
    var ret = confirm("Click 'OK' to delete this File.");    
    if (ret == true) {
        setElementValue('{actionForm.currentAction}', 'deleteFile')    
        return true;
    }
    return false;    
}

function isBrowerTypeIE()
{
    var agt = navigator.userAgent.toLowerCase();
    return (agt.indexOf("msie") != -1);
}

function isBrowerTypeFirefox()
{
    var agt = navigator.userAgent.toLowerCase();
    return (agt.indexOf("firefox") != -1);
}

function isBrowerTypeMac()
{
    var agt = navigator.userAgent.toLowerCase();
    return (agt.indexOf("mac") != -1);
}

function updateOrgNodeSelection(element)
{
    var showType = "block";    
    if (isBrowerTypeFirefox()) {
        showType = "table-row";
    }
    if (isBrowerTypeMac()) {
        showType = "table-row";
    }
        
	var checked = element.checked;
    var orgNodeId = element.value;    
    
    var messageRow = document.getElementById("message");
    var table = document.getElementById("orgTable");

	if (checked == true) {
        messageRow.style.display = "none";

        for(var i=0; i < table.rows.length; i++) {
            var row = table.rows[i];
            var id = row.id;
            var selRow = document.getElementById(id);
            selRow.style.display = "none";
            if (id == orgNodeId) {
                selRow.style.display = showType;
            }
        }        
    }
	else {
        var checkedCount = 0;    
        for(var i=0; i < table.rows.length; i++) {
            var row = table.rows[i];
            var id = row.id;
            if (id == orgNodeId) {
                var selRow = document.getElementById(orgNodeId);
                selRow.style.display = "none";
            }
            if ((row.style.display == "block") || (row.style.display == "table-row")) {
                checkedCount = checkedCount + 1;
            }
        }        
        
        if (checkedCount == 0) {
            messageRow.style.display = showType;
        }
                
    }    
}

/////////////////////// path list /////////////////////////////

function setupOrgNodePath(orgId) {
    document.body.style.cursor = 'wait';
    
    var actionElement = getSafeElement('{actionForm.actionElement}');
    actionElement.value = 'setupOrgNodePath';
    
    var currentAction = getSafeElement('{actionForm.currentAction}');
    currentAction.value = orgId;
    
    actionElement.form.submit();
    return false;
}


/**************************************************************************
 Contains javascript encode and unencode functions.
***************************************************************************/

var hex = new Array(
	"%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07",
	"%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e", "%0f",
	"%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
	"%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f",
	"%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27",
	"%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f",
	"%30", "%31", "%32", "%33", "%34", "%35", "%36", "%37",
	"%38", "%39", "%3a", "%3b", "%3c", "%3d", "%3e", "%3f",
	"%40", "%41", "%42", "%43", "%44", "%45", "%46", "%47",
	"%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f",
	"%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57",
	"%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f",
	"%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
	"%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f",
	"%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77",
	"%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f",
	"%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87",
	"%88", "%89", "%8a", "%8b", "%8c", "%8d", "%8e", "%8f",
	"%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97",
	"%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f",
	"%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7",
	"%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af",
	"%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7",
	"%b8", "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf",
	"%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7",
	"%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf",
	"%d0", "%d1", "%d2", "%d3", "%d4", "%d5", "%d6", "%d7",
	"%d8", "%d9", "%da", "%db", "%dc", "%dd", "%de", "%df",
	"%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6", "%e7",
	"%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
	"%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7",
	"%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff"
);

/**
* Encode a string to the "x-www-form-urlencoded" form, enhanced
* with the UTF-8-in-URL proposal. This is what happens:
*
* The ASCII characters 'a' through 'z', 'A' through 'Z',
*        and '0' through '9' remain the same.
*
* The unreserved characters - _ . ! ~ * ' ( ) remain the same.
*
* The space character ' ' is converted into a plus sign '+'.
*
* All other ASCII characters are converted into the
*        3-character string "%xy", where xy is
*        the two-digit hexadecimal representation of the character
*        code
*
* All non-ASCII characters are encoded in two steps: first
*        to a sequence of 2 or 3 bytes, using the UTF-8 algorithm;
*        secondly each of these bytes is encoded as "%xx".
*
* @param s The string to be encoded
* @return The encoded string
*/

function encodeString(s)
{
	s = new String(s);
	var sbuf = new String("");

	var len = s.length;
	for (var i = 0; i < len; i++)
	{
		var ch = s.charAt(i);
		var chCode = s.charCodeAt(i);
        
        if (',' == ch)
        {
			sbuf += '^';
        }
		else if ('A' <= ch && ch <= 'Z')    // 'A'..'Z'
		{	
			sbuf += ch;
		}
		else if ('a' <= ch && ch <= 'z')	// 'a'..'z'
		{
			sbuf += ch;
		}
		else if ('0' <= ch && ch <= '9')	// '0'..'9'
		{
			sbuf += ch;
		}
		else if (chCode <= 0x007f)	// other ASCII
		{
			sbuf += hex[chCode];
		}
		else if (chCode <= 0x07FF)		// non-ASCII <= 0x7FF
		{
			sbuf += (hex[0xc0 | (chCode >> 6)]);
			sbuf += (hex[0x80 | (chCode & 0x3F)]);
		}
		else					// 0x7FF < ch <= 0xFFFF
		{
			sbuf += (hex[0xe0 | (chCode >> 12)]);
			sbuf += (hex[0x80 | ((chCode >> 6) & 0x3F)]);
			sbuf += (hex[0x80 | (chCode & 0x3F)]);
		}
	}
	return sbuf;
}

function decodeString(s)
{
	s = new String(s);
	var sbuf = new String("") ;
	var l  = s.length;
	var ch = -1 ;
	var b, sumb = 0;
	for (var i = 0, more = -1 ; i < l ; i++)
	{
		switch (ch = s.charAt(i))
		{
			case '%':
				 ch = s.charAt (++i) ;
				 var hb = (isDigit (ch)
						   ? ch - '0'
							 : 10+ (toLowerCase(ch)).charCodeAt(0) - (new String('a')).charCodeAt(0)) & 0xF ;
				 ch = s.charAt (++i) ;
				 var lb = (isDigit (ch)
						   ? ch - '0'
							 : 10+(toLowerCase(ch)).charCodeAt(0) - (new String('a')).charCodeAt(0)) & 0xF ;
				 b = (String.fromCharCode((hb << 4) | lb)).charCodeAt(0) ;
				 break ;
			case '+':
				 b = ' '.charCodeAt(0) ;
				 break ;
			default:
				 b = ch.charCodeAt(0);
		}
		if ((b & 0xc0) == 0x80)		// 10xxxxxx (continuation byte)
		{
			sumb = String.fromCharCode(((sumb.charCodeAt(0) << 6) | (b & 0x3f))) ;	// Add 6 bits to sumb
			if (--more == 0)
			{
				sbuf += sumb;
			}
		}
		else if ((b & 0x80) == 0x00)		// 0xxxxxxx (yields 7 bits)
		{
			sbuf+=String.fromCharCode(b);
		}
		else if ((b & 0xe0) == 0xc0)		// 110xxxxx (yields 5 bits)
		{
			sumb = String.fromCharCode(b & 0x1f);
			more = 1;						// Expect 1 more byte
		}
		else if ((b & 0xf0) == 0xe0)		// 1110xxxx (yields 4 bits)
		{
			sumb = String.fromCharCode(b & 0x0f);
			more = 2;						// Expect 2 more bytes
		}
		else if ((b & 0xf8) == 0xf0)		// 11110xxx (yields 3 bits)
		{
			sumb = String.fromCharCode(b & 0x07);
			more = 3;						// Expect 3 more bytes
		}
		else if ((b & 0xfc) == 0xf8)		// 111110xx (yields 2 bits)
		{
			sumb = String.fromCharCode(b & 0x03);
			more = 4;						// Expect 4 more bytes
		}
		else 								// 1111110x (yields 1 bit)
		{
			sumb = String.fromCharCode(b & 0x01);
			more = 5;						// Expect 5 more bytes
		}
	}
	return sbuf;
}

//returns true or false.
function isDigit(charVal)
{
	return (charVal >= '0' && charVal <= '9');
}


function gotoNextStep(step, id, anchorName) 
{
    var currentAction = getSafeElement('{actionForm.currentAction}');
    var actionElement = getSafeElement('{actionForm.actionElement}');

    currentAction.value = step;
    actionElement.value = id;

    if ((anchorName != null) && (anchorName != "null")) {            
        var index = actionElement.form.action.indexOf("#");
        if (index == -1) {                
            actionElement.form.action += ("#" + anchorName); 
        }
    }

    document.body.style.cursor = 'wait';
    actionElement.form.submit();        
}


function verifyExitAddCustomer(){
    return confirm("Click 'OK' to quit editing customer's information. Any changes you've made will be lost.");
}

function verifyExitAddOrganization(){
    return confirm("Click 'OK' to quit editing organization's information. Any changes you've made will be lost.");
}

function verifyExitReopenTestSession(){
    return confirm("Click 'OK' to quit resetting test session. Any changes you've made will be lost.");
}

function showLegend(elementId, tokens) {
    var element = document.getElementById(elementId);
    var enabled = tokens.substr(1, 1);
    if (enabled == "T") {
        element.style.display = "block";  
    }
    else {
        element.style.display = "none";  
    }
}

function blockEventOnMaxLength(element,len) {
	return element.value.length < len;
}

function checkAndTruncate(element,len) {

	if (element.value.length > len) {

		element.value = element.value.substring(0, len);		
	}	
}