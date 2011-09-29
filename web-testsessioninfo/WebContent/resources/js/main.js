
function submitPage()
{
	showLoadingProgress();
	
    document.body.style.cursor = 'wait';
   	document.forms[0].submit();
}    

  
function gotoAction(action)
{
	showLoadingProgress();

    document.body.style.cursor = 'wait';
    if (action != null) {
    	document.forms[0].action = action;
    }
   	document.forms[0].submit();
}    
 
function gotoMenuAction(action, menuId)
{
	showLoadingProgress();
	
    document.body.style.cursor = 'wait';
    if (action != null) {
    	if (menuId != null) {
    		action = action + "?menuId=" + menuId;
    	}
    	document.forms[0].action = action;
    }
   	document.forms[0].submit();
}    

function showLoadingProgress()
{	
	var blockDiv = document.getElementById("blockDiv");
	if (blockDiv != null) {
		blockDiv.style.display = "block";
    	document.body.style.cursor = 'wait';
	}
}
	
function hideLoadingProgress()
{
	var blockDiv = document.getElementById("blockDiv");
	if (blockDiv != null) {
		blockDiv.style.display = "none";
    	document.body.style.cursor = 'default';
	}
}
