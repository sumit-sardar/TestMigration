var req;

function setupButtons(url) 
{
    var inputs = document.getElementsByTagName("input");
    var userId = null;
    
    for (var i=0; i < inputs.length; i++) {
        if ((inputs[i].getAttribute("type") == "radio") && 
            (inputs[i].getAttribute("name").indexOf('selectedUserId') > 0) &&
            (inputs[i].checked == true)) {
            userId = inputs[i].value;
            break;
        }
    }

    if (userId != null) {
        var callback = setupCallback;
        executeXhr(callback, url, userId);
    }
    
    return true;
}

function setupCallback() 
{
    // only if req shows "loaded"
    if (req.readyState == 4) {
        document.body.style.cursor = 'default';
        // only if "OK"
        if (req.status == 200) {
            var tokens = req.responseText;  
            enableButtons(tokens);
        } 
        else {
            alert("There was a problem retrieving the XML data:\n" + req.statusText);
        }
    }
} 

function executeXhr(callback, url, userId) 
{
    if (window.XMLHttpRequest) 
        req = new XMLHttpRequest();
    else 
    if (window.ActiveXObject) 
        req = new ActiveXObject("Microsoft.XMLHTTP");
    else
        alert("Your browser does not support AJAX!");    
                
    req.onreadystatechange = callback;
    req.open("GET", url, true);
    req.setRequestHeader("userId", userId);
    
    document.body.style.cursor = 'wait';
    
    req.send(null);
}

function enableButtons(tokens) 
{
    var str = tokens.substr(0, 1);
    enableElement("View", str);
    
    str = tokens.substr(1, 1);
    enableElement("Edit", str);
    
    str = tokens.substr(2, 1);
    enableElement("Delete", str);

    str = tokens.substr(3, 1);
    enableElement("changePassword", str);
}

function enableElement(elementId, enabled) 
{
    var element = document.getElementById(elementId);    
    if (element != null) {
        if (enabled == "T")
            element.removeAttribute("disabled");      
        else
            element.setAttribute("disabled", "true");      
    }
}    

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
