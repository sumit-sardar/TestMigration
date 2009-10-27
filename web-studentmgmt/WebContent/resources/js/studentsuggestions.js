/**
 * Auto suggestion for user search
 * @author: Tai Truong
 */


var req;
var autoSuggestControl;
var sTextboxValue;
var typeAhead;


/**
 * StudentSuggestions
 */
function StudentSuggestions(id, url) 
{
    this.controlId = id;
    this.actionUrl = url;
    sTextboxValue = "";
    typeAhead = true;
}


/**
 * Request suggestions for the given autosuggest control. 
 */
StudentSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl, bTypeAhead) 
{
    req = this.getXmlHttpRequestObject();

    if (req != null) {
        autoSuggestControl = oAutoSuggestControl;
        sTextboxValue = autoSuggestControl.textbox.value;
        typeAhead = bTypeAhead;
    
        if (sTextboxValue.length > 0) 
            this.findSuggestions(this.controlId, this.actionUrl); 
        else 
            autoSuggestControl.hideSuggestions();
    }
    else {
        alert("Your browser does not support AJAX!");    
    }
}


/**
 * findSuggestions 
 */
StudentSuggestions.prototype.findSuggestions = function(controlId, actionUrl) 
{
    var control = document.getElementById(controlId);  
    var controlValue = control.value;         
    var callback = this.processFindSuggestions;
    this.executeXhr(callback, actionUrl, controlId, controlValue);
}


/**
 * executeXhr 
 */
StudentSuggestions.prototype.executeXhr = function(callback, url, controlId, controlValue) 
{
    if (req.readyState == 4 || req.readyState == 0) {
        req.onreadystatechange = callback;
        req.open("GET", url, true);
        req.setRequestHeader("controlId", controlId);
        req.setRequestHeader("controlValue", controlValue);
        req.send(null);
    }
}


/**
 * processFindSuggestions 
 */
StudentSuggestions.prototype.processFindSuggestions = function()
{
    // only if req shows "loaded"
    if (req.readyState == 4) {
    
        // only if "OK"
        if (req.status == 200) {
        
            var sTextboxValueLC = sTextboxValue.toLowerCase(); 
            var aSuggestions = [];
            var resultText = req.responseText;     
            
            if (isValidResponse(resultText)) {                                 
                var splitValues = resultText.split(",");            
                for (var i=0; i<splitValues.length; i++) { 
                    var valueLC = splitValues[i];
                    valueLC = valueLC.replace(/^\s+|\s+$/g,"");                
                    valueLC = valueLC.toLowerCase();
                    var index = valueLC.indexOf(sTextboxValueLC);
                    if (index == 0) {
                        aSuggestions.push(splitValues[i]);
                    }
                }                
                //provide suggestions to the control
                autoSuggestControl.autosuggest(aSuggestions, typeAhead);
            }
            else {
                // redirect to logout page
                document.location.href='logout.do';
            }
        } 
        else {
            // there was a problem retrieving the XML data
            autoSuggestControl.hideSuggestions();
            alert("There was a problem retrieving the data from server.");
        }
    }
}
 

/**
 * getXmlHttpRequestObject 
 */
StudentSuggestions.prototype.getXmlHttpRequestObject = function()
{	
    if (window.XMLHttpRequest) 
        return new XMLHttpRequest();
    else 
    if (window.ActiveXObject) 
        return new ActiveXObject("Microsoft.XMLHTTP");
    else
        return null;
}


/**
 * createAutoSuggestControl 
 */
function createAutoSuggestControl(controlId) 
{
    var url = "/StudentManagementWeb/manageStudent/findSuggestions.do";    
    var element = document.getElementById(controlId);
    element.setAttribute("autocomplete", "off");

    return new AutoSuggestControl(element, new StudentSuggestions(controlId, url));
}


/**
 * initStudentSuggestions 
 */
function initStudentSuggestions() 
{
    createAutoSuggestControl("{actionForm.userProfile.firstName}");
    createAutoSuggestControl("{actionForm.userProfile.lastName}");
    createAutoSuggestControl("{actionForm.userProfile.loginId}");
    createAutoSuggestControl("{actionForm.userProfile.email}");
}


/**
 * isValidResponse 
 */
function isValidResponse(responseText) 
{
    var index = responseText.indexOf("Forgot Your Password?");
    if (index > 0) {                
        return false;   // this is logout page
    }
    var index = responseText.indexOf("[[ERROR]]");
    if (index > 0) {                
        return false;   // error text returned from server
    }
    return true;
}
