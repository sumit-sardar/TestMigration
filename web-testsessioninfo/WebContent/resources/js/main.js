
function submitPage()
{
	UIBlock();
	
    document.body.style.cursor = 'wait';
   	document.forms[0].submit();
}    

  
function gotoAction(action)
{
	UIBlock();

    document.body.style.cursor = 'wait';
    if (action != null) {
    	document.forms[0].action = action;
    }
   	document.forms[0].submit();
}    
 
function gotoMenuAction(action, menuId)
{
	UIBlock();
	
    document.body.style.cursor = 'wait';
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
	$.blockUI({ message: '<img src="/TestSessionInfoWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'0px',  top:  ($(window).height() - 0) /2 + 'px', left: ($(window).width() - 0) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}
	
