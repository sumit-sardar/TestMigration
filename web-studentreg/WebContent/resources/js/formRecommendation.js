function onNextFromShowBySessionPopUp () {
	if(isPopupOnByepassFR ||isPopupOnFRAccept||isPopupOnFRNotAccept || (currentView != "student" && isTabeAdaptiveProduct)){
		openModifyTestPopup(null);
	}
	else {
		onNextFromChckFormRecommendation();		
	
	}			
	
}
    

    function onNextFromChckFormRecommendation(){
	    var param = "&studentId="+selectedStudentId;
		$("#showBySessionFRButtonDiv").hide();
		$("#showByStudentFRButtonDiv").hide();
		UIBlock();   
	    $.ajax({
				async:		true,
				beforeSend :	function(){
								},
				url:		'gotoStudentRegistrationPopup.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){	
								if(data.studentSessionData.entry){									
									$("#studentName").text(data.studentSessionData.entry[0].studentName);
									$("#sessionName").text(data.studentSessionData.entry[0].testSessionName);
									$("#testName").text(data.studentSessionData.entry[0].productName);
									$("#completedDate").text(data.studentSessionData.entry[0].completionDate);
									$("#recommendedTest").text(data.studentSessionData.entry[0].recommendedProductName);
									$("#recommendedProductId").val(data.studentSessionData.entry[0].recommendedProductId);
			 
			 						$("#recommendedDialogID").dialog( {
		        						title: $("#formRecommendationCaption").val(),
								        resizable: false,
								        autoOpen: true,
								        width: '400px',
								        modal: true,
								        open: function(event, ui) {
		            						$(".ui-dialog-titlebar-close").hide();
									        }
									    });
									
									//Rows needs to be deleted, since dynamically new rows are added everytime
									$("#subtestList tr:not(:first)").remove();
									
										for(var i=0;i<data.studentSessionData.entry.length;i++) {	
										$("#subtestList tr:last").
															after('<tr><td width="90%"><span >'+
																data.studentSessionData.entry[i].itemSetName+
																	'</span></td><td>&nbsp;</td><td align="right"><span>'+
																		data.studentSessionData.entry[i].itemSetLevel+
																			'</span></td></tr>');
										}									
	 
	 									var toppos = ($(window).height() - 400) /2 + 'px';
										var leftpos = ($(window).width() - $("#recommendedDialogID").height()) /2 + 'px';
										$("#recommendedDialogID").parent().css("top",toppos);
										$("#recommendedDialogID").parent().css("left",leftpos);
										
										if(currentView == "student"){
										 	//$("#showBySessionFRButtonDiv").hide();
										 	$("#showByStudentFRButtonDiv").show();									 	
										}
										else{
											//$("#showByStudentFRButtonDiv").hide();
										 	$("#showBySessionFRButtonDiv").show();
										}
										 	
										
										$.unblockUI();
									}else{
										
										if(currentView == "student"){
											isPopupOnByepassFR = true;
										 	sessionPopupFromStudentView();
										 	$.unblockUI();
										 }
										 else{
										 	openModifyTestPopup(null);
										 }
										 	
								         //$.unblockUI();
									}
									
									
								 //$.unblockUI();	
											
								},
					error  :    function(XMLHttpRequest, textStatus, errorThrown){
					             $.unblockUI();
									window.location.href="/SessionWeb/logout.do";
								}
					
					}
				);
    
    }

	
	var isPopupOnFRAccept = false;
	var isPopupOnFRNotAccept = false;
	var isPopupOnByepassFR = false;
	
	function sessionListPopupOnFRAcceptForSession(){
		closePopUp('recommendedDialogID');
		$('#secondaryJQGridDiv').show();
		$('#primaryJQGridDiv').hide();
	    isPopupOnFRAccept = true;
	    gridloadedStdFromSes = false;
	    disableButton('nextButtonStdPopup');
	    gridloadedStdFromSesInSecondaryDiv = false;
	    if(currentView == "session" && isPopupOnFRAccept)
	    	enableButton('backButtonSPFPopup');
	    sessionPopupFromStudentView();
	}
	
	function sessionListPopupOnFRAccept(){
		closePopUp('recommendedDialogID');
		isPopupOnFRAccept = true;
		$('#secondaryJQGridDiv').show();
		$('#primaryJQGridDiv').hide();
		sessionPopupFromStudentView();
	}
	
	function sessionListPopupOnFRNotAccept(){
		closePopUp('recommendedDialogID');
		isPopupOnFRNotAccept = true;
		$('#secondaryJQGridDiv').show();
		$('#primaryJQGridDiv').hide();
		sessionPopupFromStudentView();
	}

