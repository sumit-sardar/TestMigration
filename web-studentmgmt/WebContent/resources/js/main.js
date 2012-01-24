
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
    //document.body.style.cursor = 'wait';
    $(".sub_menu").hide();
	$(document).ajaxStop($.unblockUI); 
	$.blockUI({ message: '<img src="/StudentWeb/resources/images/loading.gif" />',
		css: {
		border: '0px',
		backgroundColor: '#aaaaaa', 
		opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050});
	
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