<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<% response.setContentType("application/x-javascript"); %>

var acknowledgmentsWindow = null;

function setFocusFirstElement() { 
    var element = document.getElementById("firstFocusId");
    if (element) {
        setFocus(element.value);
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
            var actionElement = document.getElementById('actionElement');
            if(actionElement) {
                actionElement.value = elementId;
            }
            element.form.submit();
            return false;
        }        
  }
  return true;
}

function openTestTicketIndividual( anchor, testAdminId, orgNodeId ) {
    var url = "/iknow/reports/runEmbeddedReport?__executableName=/CTB/TestTicketIndividual.rox&reportType=TestTicketIndividual&reportFormat=PDF";
    return openTestTicket( "individual", anchor, url, testAdminId, orgNodeId );
}

function openTestTicketSummary( anchor, testAdminId, orgNodeId ) {
    var url = "/iknow/reports/runEmbeddedReport?__executableName=/CTB/TestTicketSummary.rox&reportType=TestTicketSummary&reportFormat=PDF";
    return openTestTicket( "summary", anchor, url, testAdminId, orgNodeId );
}

function openTestTicket( ticketType, anchor, url, testAdminId, orgNodeId ) {
    anchor.href  = url;
    anchor.href += "&rptTestAdmin=" + testAdminId;
    anchor.href += "&rptOrgNodeId=" + orgNodeId;
    
    var targetWindowName = ticketType + orgNodeId;
    anchor.target = targetWindowName;
    return true;
}

function verifyDeleteStudent(){
    var ret = confirm("Click 'OK' to delete this student's profile from your organization.\nThe student will be removed and excluded from future tests. Previous test records will be retained.");    
    if (ret == true) {
        setElementValue('currentAction', 'deleteStudent');    
        return true;
    }
    return false;    
}

function verifyExitScheduleTest(){
    return confirm("Click 'OK' to quit scheduling this test session. Any changes you've made will be lost.");
}

function verifyExitEditTest(){
    return confirm("Click 'OK' to quit editing this test session. Any changes you've made will be lost.");
}

function verifyDeleteTest(){
    return confirm("Click 'OK' to delete this test session.\nNo students have logged into the test, so you may safely remove this test session record from the database.");
}

function verifyEndTest(){
    return confirm("Click 'OK' to end this test session.\nAt least one student has logged into this test session. Clicking 'OK' will close the login window so no additional students may log in and take the test.");
}

function verifyRemoveAllStudents(anchorName){
    answer = confirm("Are you sure you want to remove all students? \nClick 'OK' to remove or click 'Cancel' to continue to schedule test.");
    if (answer)
        setElementValueAndSubmitWithAnchor('currentAction', 'removeAllStudents', anchorName);
    return answer;
}

function verifyRemoveAllProctors(anchorName){
    answer = confirm("Are you sure you want to remove all proctors? \nClick 'OK' to remove or click 'Cancel' to continue to schedule test.");
    if (answer)
        setElementValueAndSubmitWithAnchor('currentAction', 'removeAllProctors', anchorName);
    return answer;
}

function verifyCancelAddStudents(){
    return confirm("Click 'OK' to return to the Find Student page.\nAny changes you've made to the student Accommodation will be lost.\n");
}

function verifyCancelAddProctors(){
    return confirm("Click 'OK' to return to the Test Session Settings page.\nAny changes you've made to the roster session list will be lost.\n");
}

function verifyCancelResolveAssignments(){
    return confirm("Click 'OK' to return to the Select Students page.\nAny changes you've made to the student test roster list will be lost.\n");
}


//for bulk accomodation

function verifyBulkAccommodation(){
    return confirm("Click 'OK' to quit,assigning student accomodation information. Any changes you've made will be lost.");
    }
function openAcknowledgmentsWindow(url) {
    if( !acknowledgmentsWindow || acknowledgmentsWindow.closed ) {
        acknowledgmentsWindow = window.open(url, 'Acknowledgments', 'toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=560, height=430');
    } else {
        acknowledgmentsWindow.location = url;
    }
    acknowledgmentsWindow.focus();
    return false;
}

/////////////////////// color settings /////////////////////////////
var colorPreviewWindow = null;

function openColorPreviewWindow()
{
	var param = "?param=";
	var srcCtrl = document.getElementById("question_bgrdColor");
	var index = srcCtrl.selectedIndex;
	param = param + srcCtrl.options[index].text + ",";
	srcCtrl = document.getElementById("question_fontColor");
	index = srcCtrl.selectedIndex;
	param = param + srcCtrl.options[index].text + ",";

	srcCtrl = document.getElementById("answer_bgrdColor");
	index = srcCtrl.selectedIndex;
	param = param + srcCtrl.options[index].text + ",";
	srcCtrl = document.getElementById("answer_fontColor");
	index = srcCtrl.selectedIndex;
	param = param + srcCtrl.options[index].text + ",";
    
	srcCtrl = document.getElementById("standartFont").checked;
    if (srcCtrl == true)
        param = param + "1" + ",";
    else
        param = param + "1.5" + ",";

	var firstName = document.getElementById("studentFirstName").value;
    firstName = encodeString(firstName);
    param = param + firstName + ",";
        
	var lastName = document.getElementById("studentLastName").value;
    lastName = encodeString(lastName);
    param = param + lastName + ",";

   	var location = "colorFontPreview.do" + param;
    
	var windowName = "colorPreviewWindow"; 
    colorPreviewWindow = window.open(location, windowName,'toolbar=no,location=no,directories=no,status=yes,scrollbars=yes,menubar=no,resizable=yes,width=850, height=600');
    colorPreviewWindow.focus();
    
    return false;
}

function closeColorPreviewWindow()
{
    if (colorPreviewWindow != null)
        colorPreviewWindow.close();
}

/////////////////////// accommodations /////////////////////////////

function enableAccommodationCheckBoxes(enabled)
{
    var inputs = document.getElementsByTagName("input");
    for(var i=0;i < inputs.length;i++) {
        var chkbox = inputs[i];
        if ((chkbox != null) && (chkbox.getAttribute("type") == "checkbox")) {
      		if(chkbox.name.indexOf("pageFlow.accommodations") != -1) {
	        	if (enabled == "true") {
	        		chkbox.removeAttribute("disabled");
	        	}
	        	else {
	        		chkbox.setAttribute("disabled", "true");
	        		chkbox.checked = false;
	        	}
	        }
        }
    }
   	if (enabled == "false") {
    	enableColorSettingsLink(enabled);
    }
}
  
function toogleColorSettingsLink(elementId)
{
    var chkBox = document.getElementById(elementId);
	var checked = chkBox.checked;
	if (checked == true) 
		enableColorSettingsLink("true");
	else 
		enableColorSettingsLink("false");
}
 
function enableColorSettingsLink(enabled)
{
    var questionBg = document.getElementById("question_bgrdColor");
    var questionFor = document.getElementById("question_fontColor");
    var answerBg = document.getElementById("answer_bgrdColor");
    var answerFor = document.getElementById("answer_fontColor");
    var standartFont = document.getElementById("standartFont");
    var largeFont = document.getElementById("largeFont");
    var previewColor = document.getElementById("previewColor");
    
    if (enabled == "true") {
        questionBg.removeAttribute("disabled");
        questionFor.removeAttribute("disabled");
        answerBg.removeAttribute("disabled");
        answerFor.removeAttribute("disabled");
        standartFont.removeAttribute("disabled");
        largeFont.removeAttribute("disabled");
        previewColor.removeAttribute("disabled");
    }
    else {    
        questionBg.selectedIndex = 5;   
        questionFor.options.length = 0;
        questionFor.options[questionFor.options.length] = new Option('Black', 'Black');
        questionFor.options[questionFor.options.length] = new Option('Dark blue', 'Dark blue');
        questionFor.options[questionFor.options.length] = new Option('Dark brown', 'Dark brown');
        questionFor.selectedIndex = 0;       
        setQuestionColorBox();
        var questionBox = document.getElementById("questionBox");
        setBackgroundColor(questionBox, 'White');
        
        answerBg.selectedIndex = 4;           
        answerFor.options.length = 0;
        answerFor.options[answerFor.options.length] = new Option('Black', 'Black');
        answerFor.options[answerFor.options.length] = new Option('Dark blue', 'Dark blue');
        answerFor.options[answerFor.options.length] = new Option('Dark brown', 'Dark brown');
        answerFor.selectedIndex = 0;   
        setAnswerColorBox();
        var answerBox = document.getElementById("answerBox");
        setBackgroundColor(answerBox, 'Light yellow');
        
        var standartFont = document.getElementById("standartFont");
        standartFont.checked = true;
        setFontSize('14px'); 

        questionBg.setAttribute("disabled", "true");
        questionFor.setAttribute("disabled", "true");
        answerBg.setAttribute("disabled", "true");
        answerFor.setAttribute("disabled", "true");
        standartFont.setAttribute("disabled", "true");
        largeFont.setAttribute("disabled", "true");
        previewColor.setAttribute("disabled", "true");
    }

   	return true;
} 


function setQuestionColorOptions() 
{
    var bgrdbox = document.getElementById("question_bgrdColor");
    var selIndex = bgrdbox.selectedIndex;
    var option = bgrdbox.options[selIndex];
    var chosen = option.value;
    
    var selbox = document.getElementById("question_fontColor"); 
    selbox.options.length = 0;

    if (chosen == "Dark blue") {
      selbox.options[selbox.options.length] = new Option('White', 'White');
    }
    else
    if (chosen == "Black") {
      selbox.options[selbox.options.length] = new Option('Green', 'Green');
      selbox.options[selbox.options.length] = new Option('White', 'White');
      selbox.options[selbox.options.length] = new Option('Yellow', 'Yellow');
    }
    else {
      selbox.options[selbox.options.length] = new Option('Black', 'Black');
      selbox.options[selbox.options.length] = new Option('Dark blue', 'Dark blue');
      selbox.options[selbox.options.length] = new Option('Dark brown', 'Dark brown');
    }
    
    var questionBox = document.getElementById("questionBox");
    setBackgroundColor(questionBox, chosen);

    var colorbox = document.getElementById("question_fontColor"); 
    var selectedIndex = colorbox.selectedIndex;
    var colorOption = colorbox.options[selectedIndex];
    var colorValue = colorOption.value;
    setColor(questionBox, colorValue);        
}

function setAnswerColorOptions() 
{
    var bgrdbox = document.getElementById("answer_bgrdColor");
    var selIndex = bgrdbox.selectedIndex;
    var option = bgrdbox.options[selIndex];
    var chosen = option.value;
    
    var selbox = document.getElementById("answer_fontColor"); 
    selbox.options.length = 0;

    if (chosen == "Dark blue") {
      selbox.options[selbox.options.length] = new Option('White', 'White');
    }
    else
    if (chosen == "Black") {
      selbox.options[selbox.options.length] = new Option('Green', 'Green');
      selbox.options[selbox.options.length] = new Option('White', 'White');
      selbox.options[selbox.options.length] = new Option('Yellow', 'Yellow');
    }
    else {
      selbox.options[selbox.options.length] = new Option('Black', 'Black');
      selbox.options[selbox.options.length] = new Option('Dark blue', 'Dark blue');
      selbox.options[selbox.options.length] = new Option('Dark brown', 'Dark brown');
    }
    
    var answerBox = document.getElementById("answerBox");
    setBackgroundColor(answerBox, chosen);

    var colorbox = document.getElementById("answer_fontColor"); 
    var selectedIndex = colorbox.selectedIndex;
    var colorOption = colorbox.options[selectedIndex];
    var colorValue = colorOption.value;
    setColor(answerBox, colorValue);    
}

function setQuestionColorBox() 
{
    var questionBox = document.getElementById("questionBox");
    var colorbox = document.getElementById("question_fontColor"); 
    var selectedIndex = colorbox.selectedIndex;
    var colorOption = colorbox.options[selectedIndex];
    var colorValue = colorOption.value;
    setColor(questionBox, colorValue);    
}
 
function setAnswerColorBox() 
{
    var answerBox = document.getElementById("answerBox");
    var colorbox = document.getElementById("answer_fontColor"); 
    var selectedIndex = colorbox.selectedIndex;
    var colorOption = colorbox.options[selectedIndex];
    var colorValue = colorOption.value;
    setColor(answerBox, colorValue);    
}

function setFontSize(size) 
{
    var questionBox = document.getElementById("questionBox");
    var answerBox = document.getElementById("answerBox");    
    questionBox.style.fontSize = size;
    answerBox.style.fontSize = size;
}

function setColor(control, color)
{
    if (color == "Light blue") 
        control.style.color  = '#CCECFF';
    if (color == "Light pink") 
        control.style.color  = '#FFCCCC';
    if (color == "Light yellow") 
        control.style.color  = '#FFFFB0';
    if (color == "White") 
        control.style.color  = '#FFFFFF';
    if (color == "Black") 
        control.style.color  = '#000000';
    if (color == "Dark blue") 
        control.style.color  = '#000080';
    if (color == "Dark brown") 
        control.style.color  = '#663300';
    if (color == "Yellow") 
        control.style.color  = '#FFFF99';
    if (color == "Green") 
        control.style.color  = '#00CC00';
}

function setBackgroundColor(control, color)
{
    if (color == "Light blue") 
        control.style.backgroundColor  = '#CCECFF';
    if (color == "Light pink") 
        control.style.backgroundColor  = '#FFCCCC';
    if (color == "Light yellow") 
        control.style.backgroundColor  = '#FFFFB0';
    if (color == "White") 
        control.style.backgroundColor  = '#FFFFFF';
    if (color == "Black") 
        control.style.backgroundColor  = '#000000';
    if (color == "Dark blue") 
        control.style.backgroundColor  = '#000080';
    if (color == "Dark brown") 
        control.style.backgroundColor  = '#663300';
    if (color == "Yellow") 
        control.style.backgroundColor  = '#FFFF99';
    if (color == "Green") 
        control.style.backgroundColor  = '#00CC00';
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
            if (id == orgNodeId) {
                var selRow = document.getElementById(orgNodeId);
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
    
    var actionElement = document.getElementById('actionElement');
    actionElement.value = 'setupOrgNodePath';
    
    var currentAction = document.getElementById('currentAction');
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

function verifyExitAddEditStudent(){
    return confirm("Click 'OK' to quit editing student's information. Any changes you've made will be lost.");
}


function enableAudioFiles() {
	
	var audioFiles = document.getElementById('music_files');
	var musicPlayer = document.getElementById('Auditory_Calming');
	if(musicPlayer.checked)
		audioFiles.removeAttribute("disabled");
	else {
		audioFiles.setAttribute("disabled", "true");
		audioFiles.selectedIndex=0;
	}
}