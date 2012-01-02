
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
	
