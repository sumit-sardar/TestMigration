
function submitStatus(status)
{
	var element = document.getElementById('status');
	element.value = status;
   	document.forms[0].submit();
}    


function gotoAction(action)
{
    document.body.style.cursor = 'wait';
    if (action != null) {
    	document.forms[0].action = action;
    }
   	document.forms[0].submit();
}    

function gotoMenuAction(action, menuId)
{
    document.body.style.cursor = 'wait';
    if (action != null) {
    	if (menuId != null) {
    		action = action + "?menuId=" + menuId;
    	}
    	document.forms[0].action = action;
    }
   	document.forms[0].submit();
}    

function blockUI()
{	
	$("body").append('<div id="blockDiv" style="background:url(/TestSessionInfoWeb/resources/images/transparent.gif);position:fixed;top:0;left:0;width:100%;height:100%;z-index:9999"><img src="/TestSessionInfoWeb/resources/images/loading.gif" style="left:50%;top:40%;position:absolute;"/></div>');
	$("#blockDiv").css("cursor","wait");
	
}
	
function unblockUI()
{
	$("#blockDiv").css("cursor","normal");
	$("#blockDiv").remove();
}

function viewBroadcastMessage() {
	
	var param = "param";
	var isHidden = true; // $('#broadcastMsgDialogId').is(':hidden');  
	
	if (isHidden) {
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
								blockUI();
							},
				url:		'broadcastMessage.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){									
								 	
								$("#broadcastMsgDialogId").dialog({   
									 title:"Broadcast Message", 
									 resizable:false,
									 autoOpen: true
 									});
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
							},
				complete :  function(){
								unblockUI();
							}
				}
			);
	}	
}


function viewMyProfile() {
	
	var param = "param";
	var isHidden = true; // $('#broadcastMsgDialogId').is(':hidden');  
	
	if (isHidden) {
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
								blockUI();
							},
				url:		'myProfile.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){									
								 	
								$("#myProfileDialogId").dialog({   
									 title:"My Profile", 
									 resizable:false,
									 autoOpen: true
 									});
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
							},
				complete :  function(){
								unblockUI();
							}
				}
			);
	}	
}
