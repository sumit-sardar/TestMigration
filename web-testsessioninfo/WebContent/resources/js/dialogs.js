 
function viewBroadcastMessage() {
	
	var param = "param";
	var isHidden = true; // $('#broadcastMsgDialogId').is(':hidden');  
	
	if (isHidden) {
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
								blockBackground();
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
											releaseBackground();
											$(this).dialog("close"); 
										}
									}
								});
								
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
							},
				complete :  function(){
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
								blockBackground();
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
											releaseBackground();
											$(this).dialog("close"); 
										},
										"Cancel": function() { 
											releaseBackground();
											$(this).dialog("close"); 
										}
									}
								});
								 	
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
							},
				complete :  function(){
							}
				}
			);
	}	
}

function blockBackground(){	
	$("body").append('<div id="bkgDiv" style="opacity: 0.5; background-color: #d0e5f5;position: absolute;top:0;left:0;width:100%;height:100%;z-index:10"></div>');
}

function releaseBackground(){
	$("#bkgDiv").remove();
}

