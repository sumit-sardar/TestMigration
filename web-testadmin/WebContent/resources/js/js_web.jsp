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

