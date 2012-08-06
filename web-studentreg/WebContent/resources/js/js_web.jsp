<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
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

function openTestTicketIndividual_old( anchor, testAdminId, orgNodeId ) {
    var url = "/iknow/reports/runEmbeddedReport?__executableName=/CTB/TestTicketIndividual.rox&reportType=TestTicketIndividual&reportFormat=PDF";
    return openTestTicket_old( "individual", anchor, url, testAdminId, orgNodeId );
}

function openTestTicketSummary_old( anchor, testAdminId, orgNodeId ) {
    var url = "/iknow/reports/runEmbeddedReport?__executableName=/CTB/TestTicketSummary.rox&reportType=TestTicketSummary&reportFormat=PDF";
    return openTestTicket_old( "summary", anchor, url, testAdminId, orgNodeId );
}

function openTestTicket_old( ticketType, anchor, url, testAdminId, orgNodeId ) {
    anchor.href  = url;
    anchor.href += "&rptTestAdmin=" + testAdminId;
    anchor.href += "&rptOrgNodeId=" + orgNodeId;
    
    var targetWindowName = ticketType + orgNodeId;
    anchor.target = targetWindowName;
    return true;
}

function verifyExitScheduleTest(){
    return confirm("Click 'OK' to quit scheduling this test session. Any changes you've made will be lost.");
}


function verifyExitScoringStudent(){
    return confirm("Click 'OK' to quit Manual Scoring for Student. Any changes you've made will be lost.");
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
    return confirm("Click 'OK' to return to the Test Session Settings page.\nAny changes you've made to the student test roster will be lost.\n");
}

function verifyCancelAddProctors(){
    return confirm("Click 'OK' to return to the Test Session Settings page.\nAny changes you've made to the roster session list will be lost.\n");
}

function verifyCancelResolveAssignments(){
    return confirm("Click 'OK' to return to the Select Students page.\nAny changes you've made to the student test roster list will be lost.\n");
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

function openTestTicketIndividual( anchor, testAdminId, orgNodeId ) {
    var url = "/TestAdministrationWeb/testTicket/individualTestTicket.do";
    return openTestTicket( "individual", anchor, url, testAdminId, orgNodeId );
}
//START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
function openTestTicketMultiple( anchor, testAdminId, orgNodeId ) {
    var url = "/TestAdministrationWeb/testTicket/individualTestTicket.do";
    return openTestTicket( "multiple", anchor, url, testAdminId, orgNodeId );
}
//END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)

function openTestTicketSummary( anchor, testAdminId, orgNodeId ) {
    var url = "/TestAdministrationWeb/testTicket/summaryTestTicket.do";
    return openTestTicket( "summary", anchor, url, testAdminId, orgNodeId );

}

function openTestTicket( ticketType, anchor, url, testAdminId, orgNodeId ) {
    anchor.href  = url;
    anchor.href += "?testAdminId=" + testAdminId;
    anchor.href += "&orgNodeId=" + orgNodeId;
    anchor.href += "&ticketType=" + ticketType;    //Added For CR ISTEP2011CR007 (Multiple Test Ticket)
//    var targetWindowName = ticketType + orgNodeId;
//    anchor.target = targetWindowName;
    return true;
}




function submitWithAction(actionFunc) 
{
    document.forms[0].action = actionFunc;
    document.forms[0].submit();
}

function disableRemoveStudentButton()
{
    var element = document.getElementById("removeSelectedStudents");
    if (element) {
        element.setAttribute("disabled", "true");   
    }
    return true;
}
var colorPreviewWindow = null;
	
function openViewQuestionWindow(itemId,itemNumber)
{

	var param = "?param=";
	param = param + itemId +"&itemSortNumber="+itemNumber  ;
	
   	var location = "viewQuestionWindow.do" + param;
    var theTop=(screen.height/2)-(600/2);
	var theLeft=(screen.width/2)-(800/2);
	var windowName = "ViewQuestionWindow"; 
    ViewQuestionWindow = window.open(location, windowName,"toolbar=no,location=no,directories=no,status=no,scrollbars=no,menubar=no,maximize=no,resizable=no,width=900px, height=700px,top="+ theTop +" ,left="+ theLeft);
    ViewQuestionWindow.focus();
    
    return false;
}

function closeViewQuestionWindow()
{
    if (ViewQuestionWindow != null)
        ViewQuestionWindow.close();
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
        previewColor.className = "ui-widget-header";
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
        previewColor.className = "ui-widget-header ui-state-disabled";
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



