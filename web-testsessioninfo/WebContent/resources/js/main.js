
function submitPage()
{
	showLoading();
   	document.forms[0].submit();
}    

  
function gotoAction(action)
{
	if (action != null) {
    	document.forms[0].action = action;
	}
	showLoading();
	document.forms[0].submit();
}

function gotoMenuAction(action, menuId)
{	
	var element = document.getElementById("menuId");
	if (action != null) {
    	if (menuId != null) {
    		element.value = menuId;
    	}
    	element.form.action = action;
    }
	showLoading();
    element.form.submit();   	
}    
 
function showLoading()
{	
    $(".sub_menu").hide();
	//document.body.style.cursor = 'wait';
    $(document).ajaxStop($.unblockUI); 
	$.blockUI({ message: '<img src="/SessionWeb/resources/images/loading.gif" />',
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

$().ready(function(){
 	var lastWindowHeight = $(window).height();
    var lastWindowWidth = $(window).width();
	$(window).resize(function() {
		if($(window).height()!=lastWindowHeight || $(window).width()!=lastWindowWidth){
			lastWindowHeight = $(window).height();
	        lastWindowWidth = $(window).width();
			if($('.ui-dialog').length > 0){
				var left = (lastWindowWidth - $('.ui-dialog').outerWidth())/2 ;
				$('.ui-dialog').css({top:parseInt(lastWindowHeight*.1) + 'px',left:left + 'px'});
			}
			
		}
	});
	
});

var PrismOnlineReporting;
function openPrismApplication(location)
{
	//**[IAA] defect#76008: TASC - 2013 Op - 07 - SSO to Prism parameters :-Session Time out  Message is not getting displayed on clicking on the  Prism Online Report link on Report page even when the TAS appication is kept idle for more than  20 min on the Report page
	var sc_null = false;
	var _date = new Date();
	var _timer = _date.getTime();
	$.ajax({
		async: false,
		type: "get",
		url: "/SessionWeb/sessionOperation/check_session.jsp?t="+_timer,
		success: function(response,status,xhr){
			sessionCookie = getCookie("_WL_AUTHCOOKIE_TAS_SESSIONID");
			if (sessionCookie == null)
			{
				if (PrismOnlineReporting && !PrismOnlineReporting.closed) PrismOnlineReporting.close();
				window.location.href='/SessionWeb/logout2.jsp?timeout=true';
				sc_null = true;
				return false;
			}
		},
		error: function() {}
	});
	
	if (!sc_null)
	{
		//[IAA]: defect#75805: IE disallows spaces and other characters in window name (second argument). "Prism Online Reporting" => "PrismOnlineReporting"
    	PrismOnlineReporting = window.open(location, "PrismOnlineReporting",'toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=800, height=600');
    }
    return false;
}

function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
       var c = ca[i];
       while (c.charAt(0)==' ') c = c.substring(1,c.length);
       if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}
