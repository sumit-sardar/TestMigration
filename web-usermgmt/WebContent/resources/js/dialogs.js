var orgNodeIds="";
var profileTimeZoneOptions=[];
var profileStateOptions=[];
var profileHintQuesOptions=[];
var dbProfileDetails;
var isProfileChanged = false;

function viewBroadcastMessage() {
	
	var param = "param";
	var isHidden = true; // $('#broadcastMsgDialogId').is(':hidden');  
	
	if (isHidden) {
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
							},
				url:		'/UserWeb/userOperation/broadcastMessage.do',
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
	resetErrorMessages();
	
	if (isHidden) {
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'myProfile.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){
								$.unblockUI(); 
								resetProfile();
								setUserDetails(data);															
								$('#myProfileDialog').dialog({
									resizable:false,
									autoOpen: true,
									title:$("#mpDialogID").val(), 
									modal: true,
									width: 840,
									open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
								});
								$('#myProfileDialog').bind('keydown', function(event) {
				 				  var code = (event.keyCode ? event.keyCode : event.which);
	  								if(code == 27){
					  				  	if($("#profileConfirmationPopup").parent().css('display') == 'block') {
					  				  		return false;
					  				  	}else {
					  				  		onCancelProfile();
					  				 		return false;
					  				  	}
					 				 }
				 				
								});
								setProfilePopupPosition();
								dbProfileDetails = $("#myProfileDialog *").serializeArray();
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								//alert("error");
								$.unblockUI();
								window.location.href="/SessionWeb/logout.do";
							},
				complete :  function(){
							}
				}
			);
	}	
}

function setUserDetails(data){
	document.getElementById('displayMessageMyProfile').style.display = "none";
	setMessageProfile("", "", "", "");
	
	var primaryPhn3 = trim(data.userContact.primaryPhone3);
	var primaryPhoneExtn = trim(data.userContact.primaryPhone4);
	var secondaryPhn3 = trim(data.userContact.secondaryPhone3);
	var secondaryPhoneExtn = trim(data.userContact.secondaryPhone4);							
	var organizationNodes = data.organizationNodes;
	
	profileTimeZoneOptions=data.optionList.timeZoneOptions;
	profileStateOptions=data.optionList.stateOptions;
	profileHintQuesOptions = data.optionList.hintQuesOptions;
	
	fillProfileDropDown("profileTimeZoneOptions", profileTimeZoneOptions);
	fillProfileDropDown("profileStateOptions", profileStateOptions);
	fillProfileDropDown("profileHintQues", profileHintQuesOptions);
	
	// user details
	$("#loginUserId").val(data.userId);
	$("#loginUserName").val(data.userName);
	$("#profileFirstName").val(data.firstName);
	$("#profileMiddleName").val(data.middleName);
	$("#profileLastName").val(data.lastName);
	$("#profileLoginId").text(data.loginId);
	
	getOrgNodes("orgNodesName", data.organizationNodes);
	$("#profileEmail").val(data.email);
	$("#profileTimeZoneOptions").val(data.timeZone);							
	$("#role").text(data.role);
	$("#externalId").text(data.extPin1);
	$("#profileRoleName").val(data.role);
	$("#profileRoleOptions").val(data.roleId);
	$("#profileExternalId").val(data.extPin1);
							
	//contact details
	$("#profileAddressLine1").val(data.userContact.addressLine1);
	$("#profileAddressLine2").val(data.userContact.addressLine2);
	$("#profileCity").val(data.userContact.city);
	$("#profileStateOptions").val(data.userContact.state);
	$("#profileZipCode1").val(data.userContact.zipCode1);
	$("#profileZipCode2").val(data.userContact.zipCode2);
	$("#profilePrimaryPhone1").val(data.userContact.primaryPhone1);
	$("#profilePrimaryPhone2").val(data.userContact.primaryPhone2);
	$("#profilePrimaryPhone3").val(primaryPhn3);
	$("#profilePrimaryPhone4").val(primaryPhoneExtn);
	$("#profileSecondaryPhone1").val(data.userContact.secondaryPhone1);
	$("#profileSecondaryPhone2").val(data.userContact.secondaryPhone2);
	$("#profileSecondaryPhone3").val(secondaryPhn3);
	$("#profileSecondaryPhone4").val(secondaryPhoneExtn);
	$("#profileFaxNumber1").val(data.userContact.faxNumber1);
	$("#profileFaxNumber2").val(data.userContact.faxNumber2);
	$("#profileFaxNumber3").val(data.userContact.faxNumber3);
	
	$("#profileHintQues").val(data.userPassword.hintQuestionId);
	$("#profileHintAns").val(data.userPassword.hintAnswer);
}


function saveUserInfo(){
	$('#myProfileDialog').dialog("close");
}

function blockBackground(){	
	$("body").append('<div id="bkgDiv" style="opacity: 0.5; background-color: #d0e5f5;position: absolute;top:0;left:0;width:100%;height:100%;z-index:10"></div>');
}

function releaseBackground(){
	$("#bkgDiv").remove();
}

function getOrgNodes( elementId, orgList) {
	var ddl = document.getElementById(elementId);
	optionHtml = "";
	orgNodeIds = "";
	for(var i = 0; i < orgList.length; i++) {
	
		if(orgNodeIds == "") {
			orgNodeIds = orgList[i].orgNodeId ;
		} else {
			orgNodeIds = orgNodeIds + "," + orgList[i].orgNodeId ; 
		}
		if(optionHtml == "") {
			optionHtml += trim(orgList[i].orgNodeName);	
		} else {
			optionHtml = optionHtml +" , " + trim(orgList[i].orgNodeName);	
		}
		
	}
	$(ddl).html(optionHtml);
	
	if(orgList.length > 0 ) {
		$("#profileNoOrgNode").css("display","none");
	} else {
		$("#profileNoOrgNode").css("display","inline");
		$("#orgNodesName").text("");	
	}
}


function verifyUserDetails(){
	var validflag = VerifyLoginUserDetail(orgNodeIds);
	if(validflag){
		if($("#profileEmail").val()== "") {
			$("#profileEmailWarning").dialog({  
				title:$("#mpEmailAlertID").val(),  
			 	resizable:false,
			 	autoOpen: true,
			 	width: '400px',
			 	modal: true,
			 	closeOnEscape: false,
			 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});
			
			$("#profileEmailWarning").css('height',150);
			var toppos = ($(window).height() - 290) /2 + 'px';
			var leftpos = ($(window).width() - 410) /2 + 'px';
			$("#profileEmailWarning").parent().css("top",toppos);
			$("#profileEmailWarning").parent().css("left",leftpos);
			
		}else{
			saveLoginUserDetail();
		}
	}else {
		document.getElementById('displayMessageMyProfile').style.display = "block";
	}
}

function saveLoginUserDetail(){
	var param;	
	param = $("#myProfileDialog *").serialize()+ "&assignedOrgNodeIds="+orgNodeIds+"&userId="+$("#loginUserId").val();

	$.ajax(
		{
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'saveUserProfile.do',
		type:		'POST',
		data:		param,
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI();  
						var errorFlag = data.errorFlag;
						var successFlag = data.successFlag;
						if(successFlag) {
							closeProfilePopup('myProfileDialog');
							//setMessageMain(data.title, data.content, data.type, "");
							$("#contentMain").text(data.content);
							$('#errorIcon').hide();
							$('#infoIcon').show();
							document.getElementById('displayMessageMain').style.display = "block";
							//orgNodeIds = "";							
						}
  						else{
  							setMessageProfile(data.title, data.content, data.type, "");    
  							$("#contentMain").text(data.content);
							$('#errorIcon').show();
							$('#infoIcon').hide();
  							document.getElementById('displayMessageMyProfile').style.display = "block";					
  						}
																		
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();
							//alert("error");  
						window.location.href="/SessionWeb/logout.do";
					},
		complete :  function(){
						$.unblockUI();  
					}
		}
	);
}

function resetErrorMessages(){
	$("#contentMain").text("");
	$('#errorIcon').hide();
	$('#infoIcon').hide();
	document.getElementById('displayMessageMain').style.display = "none";										
}

 function resetProfile() {
 		$("#loginUserId").val("");
		$("#profileFirstName").val("");	
		$("#profileLastName").val("");	
		$("#profileMiddleName").val("");		
		$("#profileEmail").val("");
		$("#profileExternalId").val("");		
		$("#profileAddressLine1").val("");		
		$("#profileAddressLine2").val("");		
		$("#profileCity").val("");
		$("#profileState").val("");
		$("#profileZipCode1").val("");
		$("#profileZipCode2").val("");
		$("#profilePrimaryPhone1").val("");
		$("#profilePrimaryPhone2").val("");
		$("#profilePrimaryPhone3").val("");
		$("#profilePrimaryPhone4").val("");
		$("#profileSecondaryPhone1").val("");
		$("#profileSecondaryPhone2").val("");
		$("#profileSecondaryPhone3").val("");
		$("#profileSecondaryPhone4").val("");
		$("#profileFaxNumber1").val("");	
		$("#profileFaxNumber2").val("");	
		$("#profileFaxNumber3").val("");
		$("#profileLoginId").text("");	
		$("#role").text("");
		$("#externalId").text("");
		$("#profileOldPassword").val("");
		$("#profileNewPassword").val("");
		$("#profileConfirmPassword").val("");		
		$("#profileHintAns").val("");
		
		var allSelects = $("#myProfileDialog select");
		for(var count = 0; count < allSelects.length ; count++) {
			$(allSelects[count]).find("option:eq(0)").attr("selected","true");
		}		
		//orgNodeIds = "";		
		$("#profileNoOrgNode").css("display","inline");
		$("#orgNodesName").text("");
		
}

function onCancelProfile() {	
	isProfileChanged = false;
	
  	isProfileChanged = isProfileDataChanged();
    if(isProfileChanged) {
    	openProfileConfirmationPopup();	
	} else {
		closeProfilePopup('myProfileDialog');
	}	
}

function isProfileDataChanged(){	
	var profileValue = $("#myProfileDialog *").serializeArray(); 
	isProfileChanged = false;	
	
	if(dbProfileDetails.length != profileValue.length) {
		isProfileChanged = true;
	}
	if(!isProfileChanged) {
		for(var key = 0; key <dbProfileDetails.length ; key++) {
			if(profileValue[key].name != dbProfileDetails[key].name) {
				isProfileChanged = true;
				break;
			}
			if(trim(profileValue[key].value) != trim(dbProfileDetails[key].value)){
				isProfileChanged = true;
				break;
			}	 
		}
	}
	return  isProfileChanged;
}

function openProfileConfirmationPopup(){
	$("#profileConfirmationPopup").dialog({  
		title:$("#mpConfirmID").val(),  
		resizable:false,
		autoOpen: true,
		width: '400px',
		modal: true,
		closeOnEscape: false,
		open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
	});	
	$("#profileConfirmationPopup").css('height',120);
	var toppos = ($(window).height() - 290) /2 + 'px';
	var leftpos = ($(window).width() - 410) /2 + 'px';
	$("#profileConfirmationPopup").parent().css("top",toppos);
	$("#profileConfirmationPopup").parent().css("left",leftpos);
}

function closeProfilePopup(dailogId){
	if(dailogId == 'myProfileDialog') {
			$('#profileAccordion').accordion('activate', 0);			
			$("#User_Info").scrollTop(0);
			$("#Contact_Info").scrollTop(0);
			$('#Contact_Info').hide();
			$("#Change_Pwd").scrollTop(0);
			$('#Change_Pwd').hide();
		
			$("#profileNoOrgNode").css("display","inline");
			$("#orgNodesName").text("");
			
	}
	$("#"+dailogId).dialog("close");
	if(dailogId == 'profileConfirmationPopup'){
		$("#profileAccordion").find(".ui-accordion-content-active").find(":text, :password").eq(0).focus();
	}
}

function closeProfileConfirmation() {
	closeProfilePopup('profileConfirmationPopup');
	closeProfilePopup('myProfileDialog');
	
}

function closeProfileEmailWarning() {
	closeProfilePopup('profileEmailWarning');
	saveLoginUserDetail();	
}

function fillProfileDropDown( elementId, optionList) {
	var ddl = document.getElementById(elementId);
	var optionHtml = "" ;
	for(var i = 0; i < optionList.length; i++ ) {
	     var val = optionList[i].split("|");
	     
		optionHtml += "<option  value='"+ val[0]+"'>"+ val[1]+"</option>";	
	}
	$(ddl).html(optionHtml);
}

function setProfilePopupPosition(){
	$("#User_Info").css("height",'250px');
	$("#Contact_Info").css("height",'250px');
	$("#Change_Pwd").css("height",'250px');
	var toppos = ($(window).height() - 490) /2 + 'px';
	var leftpos = ($(window).width() - 780) /2 + 'px';
	$("#myProfileDialog").parent().css("top",toppos);
	$("#myProfileDialog").parent().css("left",leftpos);
}
	