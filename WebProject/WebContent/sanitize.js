function submitWS(action)
{
	document.forms[0].action = action;
   	document.forms[0].submit();
}    

function submitLogin()
{

	//var username = document.getElementById('j_username').value.trim();//trim() does not work in IE
	var username = $.trim(document.getElementById('j_username').value);
	document.getElementById('j_username').value = username;
	
	//var password = document.getElementById('j_password').value.trim();//trim() does not work in IE
	var password = $.trim(document.getElementById('j_password').value);
	document.getElementById('j_password').value = password;
	
	showLoading();
   	document.forms[0].submit();
}    

function sanitizeLogin() 
{
    if (! sanitize(document.getElementById('j_username').value)) {
        return false;
    }    
    if (! sanitize(document.getElementById('j_password').value)) {
        return false;
    }
    return true;
}


function sanitize(str) 
{
    if (str.indexOf("%27+%2B+%27") >= 0 || 
        str.indexOf("'+'") >= 0 ||
        str.indexOf("'%2B'") >= 0 ||
        str.indexOf("%27%2B%27") >= 0 ||
        str.indexOf("%27+%27") >= 0 ||
        str.indexOf("%22%27") >= 0 ||
        str.indexOf("%3D%22") >= 0 ||
        str.indexOf("+and+") >= 0 || 
        str.indexOf("%2Band%2B") >= 0 || 
        str.indexOf("+%7C%7C+") >= 0 || 
        str.indexOf("%2B%7C%7C%2B") >= 0 || 
        str.indexOf("%2B||%2B") >= 0 || 
        str.indexOf("+||+") >= 0 ||
        str.indexOf("javascript:") >= 0 ||
        str.indexOf("javascript%3A") >= 0 ||
        str.indexOf("<script>") >= 0 || 
        str.indexOf("<//script>") >= 0) {
            alert("XSS or SQL Injection attack detected!");
            return false;
    }        
    return true;
}

function toggleAccomm(element) 
{
	var divTR = document.getElementById("accommDiv");
	if (element.checked) {
		divTR.style.display = "block";
	}
	else {
		divTR.style.display = "none";
	}
	return true;
}
