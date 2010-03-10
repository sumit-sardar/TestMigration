/**
 * dynamicly retrieve report list using ajax
 * @author: Tai Truong
 */

var req;


/**
 * getReportList
 */
function getReportList() 
{
    var url = "/TestSessionInfoWeb/homepage/getReportList.do";    
    
    var programIndex = 0;
    var programControl = document.getElementById("wlw-select_key:{requestScope.program}");          
    if (programControl != null) 
        programIndex = programControl.options.selectedIndex;         

    var organizationIndex = 0;
    var orgControl = document.getElementById("wlw-select_key:{requestScope.organization}");        
    if (orgControl != null)
        organizationIndex = orgControl.options.selectedIndex;

    var callback = processgetReportList;
    executeXhr(callback, url, programIndex, organizationIndex);
}
 

/**
 * processgetReportList
 */
function processgetReportList() 
{
    // only if req shows "loaded"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
            var value = req.responseText;  
            if (isValidResponse(value)) {
                $('reportlists').innerHTML = value;
            }
            else {
                // redirect to logout page
                document.location.href='logout.do';
            }
        } 
        else {
            // there was a problem retrieving the XML data
            alert("There was a problem retrieving the data from server.");
        }
    }
} 


/**
 * executeXhr
 */
function executeXhr(callback, url, programIndex, organizationIndex) 
{
    req = getXmlHttpRequestObject();

    if (req != null) {
        req.onreadystatechange = callback;
        req.open("GET", url, true);
        req.setRequestHeader("programIndex", programIndex);
        req.setRequestHeader("organizationIndex", organizationIndex);
        
        req.send(null);
    }
    else {
        alert("Your browser does not support AJAX!");    
    }
}


/**
 * $()
 */
function $() 
{
    var elements = new Array();
  
    for (var i = 0; i < arguments.length; i++) {
        var element = arguments[i];
        if (typeof element == 'string')
            element = document.getElementById(element);

        if (arguments.length == 1) 
            return element;
      
        elements.push(element);
    }
  
    return elements;
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

/**
 * getXmlHttpRequestObject 
 */
function getXmlHttpRequestObject()
{	
    if (window.XMLHttpRequest) 
        return new XMLHttpRequest();
    else 
    if (window.ActiveXObject) 
        return new ActiveXObject("Microsoft.XMLHTTP");
    else
        return null;
}
