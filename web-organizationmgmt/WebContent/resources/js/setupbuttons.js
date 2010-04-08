var req;

function setupButtons(url) 
{
    var inputs = document.getElementsByTagName("input");
    var orgNodeId = null;
    
    for (var i=0; i < inputs.length; i++) {
        if ((inputs[i].getAttribute("type") == "radio") && 
            (inputs[i].getAttribute("name").indexOf('selectedOrgNodeId') > 0) &&
            (inputs[i].checked == true)) {
            orgNodeId = inputs[i].value;
            break;
        }
    }

    if (orgNodeId != null) {
        var callback = setupCallback;
        executeXhr(callback, url, orgNodeId);
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

function executeXhr(callback, url, orgNodeId) 
{
    if (window.XMLHttpRequest) 
    {
        req = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) 
    {
        req = new ActiveXObject("Microsoft.XMLHTTP");
    }
    else
    {
        alert("Your browser does not support AJAX!");    
    }
    req.onreadystatechange = callback;
    req.open("GET", url, true);
    req.setRequestHeader("orgNodeId", orgNodeId);
    
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
    enableElement("Add", str);
}

function enableAllButtons()
{   
    var element1 = document.getElementById("view");
     //alert("enter inside"+element1);
    element1.removeAttribute("disabled"); 
    var element2 = document.getElementById("editProfile");
    element2.removeAttribute("disabled"); 
    var element3 = document.getElementById("editFramework");
    element3.removeAttribute("disabled"); 
    var element4 = document.getElementById("createAdmin");
    element4.removeAttribute("disabled"); 
    var element5 = document.getElementById("manageOrg");
    element5.removeAttribute("disabled"); 
   
}

function toggleShowButton(element,flag) {
//alert(flag);
	var showButton = document.getElementById("showDetails");
	var eName = element.name;
	var allElements = document.getElementsByName(eName);
    
    if(element.checked || flag == false) {
    	showButton.removeAttribute("disabled");
    } 
    else {
    	if(!hasSelectedElements(allElements, flag)) {
    		showButton.setAttribute("disabled",true);
    	}
    }
}

function hasSelectedElements(allElements, flag) {
	var selectedCount = 0;
	var sElement = null;

	for (var i = 0; i < allElements.length; i++) {

		sElement = allElements[i];
		if(sElement.checked || flag == false) {
			selectedCount++;						
		}
	}
	return selectedCount > 0;
}

function enableButton(tokens, elementId) {   
   
    var element = document.getElementById(elementId);    
    var strToken = tokens.substr(5, 1);
    if( tokens != null && strToken == 'T') 
         element.removeAttribute("disabled");   
   else
        element.setAttribute("disabled", "true");   
    document.getElementById("actionPermission").value = strToken;
}


function enableFileButtons(tokens)
{         
    var str = tokens.substr(0, 1);
    enableElement("deleteFile", str);
    
    str = tokens.substr(1, 1);
    enableElement("getFailedRecords", str);
    
   /* str = tokens.substr(2, 1);
    enableElement("actionOverride:viewUploads", str);*/
    
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

function enableDownload()
{ 
   var element = getSafeElement("actionOverride:downloadTemplate");
   element.removeAttribute("disabled"); 
}

function enableDownloadData()
{
    var element = document.getElementById("download");
    element.removeAttribute("disabled"); 
}

function enableUpload()
{
    var fileName = document.getElementById("inputbox").value;
    var element = document.getElementById("upload");
    if (fileName != "") {  
        element.removeAttribute("disabled");   
    } else {
        element.setAttribute("disabled", "true"); 
    }
    
}


function isFilePathValid(path)
{
	if(path == "") {
		return false;
	}
    if (isBrowerTypeIE()) {
		if((path.charAt(0) != "\\" && path.charAt(1) != "\\") && (path.charAt(0) != "/" && path.charAt(1) != "/"))
		{
			if(!path.charAt(0).match(/^[a-zA-z]/))  
			{
				return false;
			}
			if(path.charAt(1) == "" ||!path.charAt(1).match(/^[:]/) || !path.charAt(2).match(/^[\/\\]/))
			{
				return false;
			}
		}
	}    
    return true;
}


//to remove whitespaces from start and end of a string
function trim( str ) {
    if(str == null){
        str = '';
        return str;
    }
    while (str.substr(0,1) == ' ' || str.substr(0,1)=='\n') {
       str = str.substr(1, str.length);
    }
    while (str.substr(str.length-1, str.length) == ' '|| str.substr(str.length-1, str.length) =='\n') {
       str = str.substr(0,str.length-1);
    }
    return str;		
}

function nullOrSpace( s ) {
    return s == null || s == '' || s == ' ';
}


//As per Windows, to check disallowed characters in naming a file 
function hasInvalidCharacters(str) {

    if (isBrowerTypeMac())        
        return false; // do not check on Mac

	var charSet = '\\ / : * ? " < > |';
	var charArr = charSet.split(" ");
	for (var i = 0; i < charArr.length; i++) {
		if(str.indexOf(charArr[i]) > -1) {//if string has any of the disallowed characters, index will be > -1
			return true;
		}
	}
    return false;
}


//Mother function for checking complete valid file paths 
function checkFileType()
{
    var errorMsg = 'Please select a valid path and .xls data file to continue.';
    var filePath= document.getElementById("inputbox").value;
	filePath= trim(filePath);
	var extnIndex = 0; //file extension index within string 
	var extnLength = 4;//extension length ".xls" will be 4
	var strLength = 0;

	//if path is not valid 
	if(!isFilePathValid(filePath)) {
		alert(errorMsg);
		return false;
	}
	else {
		var lastBslashIndex = filePath.lastIndexOf("\\");//last index of back slash
		var pathStrLength = filePath.length;
		var fileName = filePath.substr(lastBslashIndex + 1, pathStrLength);// to get the file name
	}

	//if file name is null or blank 
	if(nullOrSpace(fileName)) {
		alert(errorMsg);
		return false;
	}

	//if file name has invalid characters(as per allowed chars in windows)
	if(hasInvalidCharacters(fileName)) {
		alert(errorMsg);
		return false;
	}

	extnIndex = fileName.indexOf(".xls");
	strLength = fileName.length;

	//if index is < 1, it means it may be at 0 or may not be present (at -1)
	//in case of 0 also error will be thrown as there should be some character before file extn
	if(extnIndex < 1) {
		alert(errorMsg);
	}
	else {
		fName = fileName.substr(0, extnIndex);//there is some character before file extn, remove extn

		//check if there is any .(dot) For example: "abc.xyz.xls" 
		//file name cannot start with .(dot)
		nameb4dot = fName.split(".")[0];//nameb4dot cannot be null or blank in order to have valid file name

		if((extnIndex + extnLength) == strLength && !nullOrSpace(nameb4dot)) {

				showPopWin('progress_modal.jsp', 400, 150, null);
				return true;
		}
		else {
			alert(errorMsg);
		}	
	}
	return false;
}
