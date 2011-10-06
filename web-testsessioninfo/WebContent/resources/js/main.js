
function submitPage()
{
	showLoading();
   	document.forms[0].submit();
}    

  
function gotoAction(action)
{
	showLoading();
    if (action != null) {
    	document.forms[0].action = action;
    }
   	document.forms[0].submit();
}    
 
function gotoMenuAction(action, menuId)
{	
	showLoading();
    if (action != null) {
    	if (menuId != null) {
    		action = action + "?menuId=" + menuId;
    	}
    	document.forms[0].action = action;
    }
   	document.forms[0].submit();
}    
 
function showLoading()
{	
	$(document).ajaxStop($.unblockUI); 
 	$.blockUI({ message: '<img src="/TestSessionInfoWeb/resources/images/loading.gif" />',
		css: {
		border: '0px',
		width:'0px',  
		top:  ($(window).height() - 0) /2 + 'px', 
		left: ($(window).width() - 0) /2 + 'px'}}); 				 
}
	

function handleEnterKey( e, element ) {
    var keyId = (window.event) ? event.keyCode : e.which;
    var keyValue = String.fromCharCode( keyId );
    var regExp = /\d/;
    var results = false;
    
    if ( keyId == 13 ) {
        element.form.submit();
        return false;
    }
         
    return true;
}


function handleFocus( e, element ) {
	element.className = "buttonFocus";
}

function handleBlur( e, element ) {
	element.className = "button";
}
	