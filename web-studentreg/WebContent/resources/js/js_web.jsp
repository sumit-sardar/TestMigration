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

function handleEnterKey(elementId, value) {
  if (window.event && window.event.keyCode == 13) {
        var element = document.getElementById(elementId);
        if(element) {
            element.value = value;
            var actionElement = document.getElementById('{actionForm.actionElement}');
            if(actionElement) {
                actionElement.value = elementId;
            }
            element.form.submit();
            return false;
        }        
  }
  return true;
}


function verifyExitRegisterStudent(){
    return confirm("Click 'OK' to quit registering student. Any changes you've made will be lost.");
}
 
/////////////////////// test ticket report /////////////////////////////
function openTestTicketIndividual( anchor, testAdminId, orgNodeId, studentId ) {
    var url = "/TestAdministrationWeb/testTicket/individualTestTicket.do";
    return openTestTicket( "individual", anchor, url, testAdminId, orgNodeId, studentId );
}

function openTestTicket( ticketType, anchor, url, testAdminId, orgNodeId, studentId ) {
    anchor.href  = url;
    anchor.href += "?testAdminId=" + testAdminId;
    anchor.href += "&orgNodeId=" + orgNodeId;
    anchor.href += "&studentId=" + studentId;
    
//    var targetWindowName = ticketType + orgNodeId;
//    anchor.target = targetWindowName;
    return true;
}
