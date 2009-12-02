<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<% response.setContentType("application/x-javascript"); %>

function setFocusFirstElement() { 
    var element = document.getElementById("firstFocusId");
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

function verifyDeleteUser(){
    var ret = confirm("Click 'OK' to delete this user's profile from your organization.");    
    if (ret == true) {
        setElementValue('currentAction', 'deleteUser')    
        return true;
    }
    return false;    
}

function verifyExitAddUser(){
    return confirm("Click 'OK' to quit editing user's information. Any changes you've made will be lost.");
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

function setupOrgNodePath(orgId) 
{
    document.body.style.cursor = 'wait';
    
     var actionElement = document.getElementById('actionElement');
    actionElement.value = 'setupOrgNodePath';
    
    var currentAction = document.getElementById('currentAction');
    currentAction.value = orgId;
    
    actionElement.form.submit();
    return false;
}


function isDigit(charVal) 
{
	return (charVal >= '0' && charVal <= '9');
}

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
            if (nextEl.focus) 
                nextEl.focus();
            return;
        }
    }
}



