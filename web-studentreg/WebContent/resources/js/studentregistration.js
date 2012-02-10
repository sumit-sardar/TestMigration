$(document).ready(function() {  
	
	
	
	$('#Yes').click(function() {  
	
		var param = $("#studentId").val(); 
		 $.unblockUI();  
		 setElementValueAndSubmit('currentAction', 'goto_recommended_find_test_sessions');    
		
	});
	     
	   $('#No').click(function() {  
           $.unblockUI(); 
           setElementValueAndSubmit('currentAction', 'toModifyTestFromFind');       
		  
		});  
	}); 
	
	
	function closePopup(){
	 $.unblockUI();  
	}
	
function viewStudentDetail () {
	
	var param = "&studentId="+$("#studentId").val();
	
	
		$.ajax({
				async:		false,
				beforeSend :	function(){
									
								},
				url:		'goto_student_registration_popup.do',
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
								$("#recommendedProductId").val();
		 
								
								//Rows needs to be deleted, since dynamically new rows are added everytime
								$("#subtestList tr:not(:first)").remove();
								
									for(var i=0;i<data.studentSessionData.entry.length;i++) {	
									$("#subtestList tr:last").
														after('<tr><td width="90%"><span  style=" font-size: 75%;">'+
															data.studentSessionData.entry[i].itemSetName+
																'</span></td><td>&nbsp;</td><td align="right"><span style=" font-size: 75%;">'+
																	data.studentSessionData.entry[i].itemSetLevel+
																		'</span></td></tr>');
									}									
									var popupHeight = $("#recommendedDialogID").height();
									var popupWidth = $("#recommendedDialogID").width();
	
									$.blockUI({message: $('#recommendedDialogID'), css: { width: '0%', height: '0%',
																							top: $(window).height()/2-popupHeight/2,
																							left: $(window).width()/2-popupWidth/2}});  
								}else{
									
									  setElementValueAndSubmit('currentAction', 'toModifyTestFromFind');
							           
								}
								
								
								
										
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								window.location.href="/TestSessionInfoWeb/logout.do";
							}
				
				}
			);
	
}