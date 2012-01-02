
function submitPage()
{
	showLoading();
   	document.forms[0].submit();
}

function gotoAction(action)
{
	//UIBlock();
	showLoading();
    if (action != null) {
    	document.forms[0].action = action;
	}
	//document.location = location;
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
    $(".sub_menu").hide();
	//document.body.style.cursor = 'wait';
    $(document).ajaxStop($.unblockUI); 
	$.blockUI({ message: '<img src="/UserWeb/resources/images/loading.gif" />',
		css: {
		border: '0px',
		backgroundColor: '#aaaaaa', 
		opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050});
		 
}

function openWindow(location)
{
    window.open(location,"help",'toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=560, height=430');
    return false;
}

function UIBlock(){


	$(document).ajaxStop($.unblockUI); 
	
	$.blockUI({ message: '<img src="images/loading.gif" />',
				css: {
				border: '0px',
				//backgroundColor: '#ff00ff', 
				//opacity:  0.5, 
				width:'0px',  
				top:  ($(window).height() - 0) /2 + 'px', 
				left: ($(window).width() - 0) /2 + 'px'}}); 
}



function handleEnterKeyEvent( e, element, location ) {
    var keyId = (window.event) ? event.keyCode : e.which;
    var keyValue = String.fromCharCode( keyId );
    var regExp = /\d/;
    var results = false;
    
    if ( keyId == 13 ) {
        //element.form.submit();
		document.location = location;
        return false;
    }
         
    return true;
}


function handleFocus( element ) {
	element.className = "buttonFocus";
}

function handleBlur( element ) {
	element.className = "button";
}
