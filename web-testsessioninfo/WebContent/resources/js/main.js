
function submitPage()
{
	UIBlock();
   	document.forms[0].submit();
}    

  
function gotoAction(action)
{
	UIBlock();
    if (action != null) {
    	document.forms[0].action = action;
    }
   	document.forms[0].submit();
}    
 
function gotoMenuAction(action, menuId)
{	
	UIBlock();
    if (action != null) {
    	if (menuId != null) {
    		action = action + "?menuId=" + menuId;
    	}
    	document.forms[0].action = action;
    }
   	document.forms[0].submit();
}    
 
function UIBlock()
{	
	$(document).ajaxStop($.unblockUI); 
 	$.blockUI({ message: '<img src="/TestSessionInfoWeb/resources/images/loading.gif" />',
		css: {
		border: '0px',
		width:'0px',  
		top:  ($(window).height() - 0) /2 + 'px', 
		left: ($(window).width() - 0) /2 + 'px'}}); 				 
}
	
