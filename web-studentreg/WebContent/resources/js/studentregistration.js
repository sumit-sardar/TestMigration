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
								$("#completedDate").text(data.studentSessionData.entry[0].completionDateTime);
								$("#recommendedTest").text(data.studentSessionData.entry[0].recommendedProductName);
								$("#recommendedProductId").val();
		 
								
								//Rows needs to be deleted, since dynamically new rows are added everytime
								$("#subtestList tr:not(:first)").remove();
								
									for(var i=0;i<data.studentSessionData.entry.length;i++) {	
									$("#subtestList tr:last").
														after('<tr><td width="80%"><span><font size="1">'+
															data.studentSessionData.entry[i].itemSetName+
																'</font></span></td><td><span><font size="1">'+
																	data.studentSessionData.entry[i].itemSetLevel+
																		'</font></span></td></tr>');
									}									
									//$.blockUI({ message: $('#recommendedDialogID')});	
									$.blockUI({message: $('#recommendedDialogID'), css: { width: '0%' }});  
								}else{
									//$.unblockUI();
									  setElementValueAndSubmit('currentAction', 'toModifyTestFromFind');
							           
								}
								
								
								 //$.unblockUI(); 
										
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								alert("Error...");	
							}
				
				}
			);
	
}