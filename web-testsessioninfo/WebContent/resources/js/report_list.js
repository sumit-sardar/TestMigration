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
    var url = "/SessionWeb/sessionOperation/getReportList.do";    
    
    var programIndex = 0;
    var programControl = getSafeElement("wlw-select_key:{requestScope.program}");          
    if (programControl != null) 
        programIndex = programControl.options.selectedIndex;         

    var organizationIndex = 0;
    var orgControl = getSafeElement("wlw-select_key:{requestScope.organization}");        
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
            	document.getElementById('reportlists').innerHTML = value;
            }
            else {
                // redirect to logout page
				window.location.href="/SessionWeb/logout.do";
            }
        } 
        else {
            // there was a problem retrieving the XML data
			window.location.href="/SessionWeb/logout.do";
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
 * isValidResponse
 */
function isValidResponse(responseText) 
{
    var index = responseText.indexOf("Forgot Your Password?");
    if (index > 0) {                
        return false;   // this is logout page
    }
    index = responseText.indexOf("[[ERROR]]");
    if (index > 0) {                
        return false;   // error text returned from server
    }
    index = responseText.indexOf("system error has occurred");
    if (index > 0) {                
        return false;   // error text returned from jsp
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
