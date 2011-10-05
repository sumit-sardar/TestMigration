
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
