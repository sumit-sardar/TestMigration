
function viewBroadcastMessage() {
	
	var param = "param";
	var isHidden = true; // $('#broadcastMsgDialogId').is(':hidden');  
	
	if (isHidden) {
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
								//showLoadingProgress();
							},
				url:		'broadcastMessage.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){									
								 	
								// Broadcast Dialog			
								$('#broadcastMsgDialogId').dialog({
									autoOpen: true,
								    title:"Broadcast Message", 
									width: 600,
									buttons: {
										"Close": function() { 
											$(this).dialog("close"); 
										}
									}
								});
								
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
							},
				complete :  function(){
								//hideLoadingProgress();
							}
				}
			);
	}	
}


function viewMyProfile() {
	
	var param = "param";
	var isHidden = true; // $('#myProfileDialogId').is(':hidden');  
	
	if (isHidden) {
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
								//showLoadingProgress();
							},
				url:		'myProfile.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){									
								 	
								// My Profile Dialog			
								$('#myProfileDialogId').dialog({
									autoOpen: true,
								    title:"My Profile", 
									width: 600,
									height: 400,
									buttons: {
										"Ok": function() { 
											$(this).dialog("close"); 
										},
										"Cancel": function() { 
											$(this).dialog("close"); 
										}
									}
								});
								 	
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
							},
				complete :  function(){
								//hideLoadingProgress();
							}
				}
			);
	}	
}
