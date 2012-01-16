 
function viewBroadcastMessage() {
	
	var param = "param";
	var isHidden = true; // $('#broadcastMsgDialogId').is(':hidden');  
	
	if (isHidden) {
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
							},
				url:		'broadcastMessage.do',
				type:		'POST',
				data:		param,
				dataType:	'html',
				success:	function(data, textStatus, XMLHttpRequest){									
								 	
								//alert(data);
    							var broadcastMsgBody = document.getElementById("broadcastMsgBody");
    							broadcastMsgBody.innerHTML = data;
									 	
								// Broadcast Dialog			
								$('#broadcastMsgDialogId').dialog({
									autoOpen: true,
									modal: true,
								    title:"Broadcast Message", 
									width: "800px",
									resizable: false,
									open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
								});
								
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								window.location.href="/SessionWeb/logout.do";
							},
				complete :  function(){
							}
				}
			);
	}	
}

function closeBroadcastMessage() {
	$('#broadcastMsgDialogId').dialog("close");
}


function viewMyProfile() {
	
	var param = "param";
	var isHidden = true; // $('#myProfileDialogId').is(':hidden');  
	
	if (isHidden) {
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
							},
				url:		'myProfile.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){									
								 	
								//alert(data.entry);
								 	
								// My Profile Dialog			
								$('#myProfileDialogId').dialog({
									autoOpen: true,
								    title:"My Profile", 
									modal: true,
									width: 600,
									resizable: false,
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

