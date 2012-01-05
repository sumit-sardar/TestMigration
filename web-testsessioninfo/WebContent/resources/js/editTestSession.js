    /**
    *Edit Test Session
    **/   
    var selectedTestAdminId = null;
    var state = null;
    var stdsLogIn = false;
    var selectedTestSession;
    function editTestSession(){  
    
 	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'getTestDetails.do?testAdminId=' +selectedTestAdminId ,
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){
						
						if (data.status.isSuccess){			
							if(data.noTestExists == true){
								noTestExist = true;
								document.getElementById("testDiv").style.display = "none";
								document.getElementById("noTestDiv").style.display = "inline";
							}else{
								noTestExist = false;
								document.getElementById("noTestDiv").style.display = "none";
								document.getElementById("testDiv").style.display = "inline";
							}
							
							if (data.savedTestDetails.studentsLoggedIn > 0){
								stdsLogIn = true;
							}
							if(!noTestExist){
								
								state = "EDIT";
								populateTestDetailGrid(data);
								//processStudentAccordion();
								//processProctorAccordion();
							//	$("#productType").val(data.product[0].productType);
							//	$("#showStudentFeedback").val(data.product[0].showStudentFeedback);
							}
							
						}
					
						 $("#scheduleSession").dialog({  
							title:"Edit a Test Session:",  
							resizable:false,
							autoOpen: true,
							width: '1024px',
							modal: true,
							closeOnEscape: false,
							open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
							});	
						setPopupPosition();
							$('#ssAccordion').accordion('activate', 1 );	
						$.unblockUI(); 
									
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();
						window.location.href="/SessionWeb/logout.do";
						
					},
		complete :  function(){
						 $.unblockUI(); 
					}
	}); 
    
   
 		
   
    }
    
    
    function populateTestDetailGrid(data){
    	selectedTestSession =  data.savedTestDetails;	  	
		fillDropDownWithDefaultValue("timeZoneList",data.testZoneDropDownList, data.savedTestDetails.testSession.timeZone);
		$("#testSessionName").val(data.savedTestDetails.testSession.testAdminName);
		$( "#startDate" ).datepicker( "option" , "minDate" , data.savedTestDetails.testSession.loginStartDateString ) ;
		$( "#endDate" ).datepicker( "option" , "minDate" , data.savedTestDetails.testSession.loginEndDateString ) ;
		$( "#endDate" ).datepicker( "refresh" );
		$( "#startDate" ).datepicker( "refresh" );
		document.getElementById("startDate").value = data.savedTestDetails.testSession.loginStartDateString;
		document.getElementById("endDate").value = data.savedTestDetails.testSession.loginEndDateString;
		fillDropDownWithDefaultValue("topOrgNode",data.topNodeDropDownList,data.savedTestDetails.testSession.creatorOrgNodeId);
		if(data.savedTestDetails.testSession.location != undefined){
			$("#testLocation").val(data.savedTestDetails.testSession.location);
		}
		document.getElementById("time").innerHTML = data.savedTestDetails.testSession.dailyLoginStartTimeString  +" - "+ data.savedTestDetails.testSession.dailyLoginEndTimeString ;
		var sliderLeft =  calculateTimeInMin(data.savedTestDetails.testSession.dailyLoginStartTimeString);
		var sliderRight = calculateTimeInMin( data.savedTestDetails.testSession.dailyLoginEndTimeString);
		$("#slider-range").slider("option", "values", [sliderLeft, sliderRight]);
		$("#selectedTestId").val(data.savedTestDetails.testSession.testCatalogId);
  	}
    
    //Ajax Call
    
    function populateSelectTestGrid(wizard,index){
    	var testSessionList ={};
	 	$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'getUserProductsDetails.do?testAdminId=' +selectedTestAdminId ,
			type:		'POST',
			dataType:	'json',
			success:	function(data, textStatus, XMLHttpRequest){
							
							ProductData = data.productsDetails;
							 var selectedProdIndex = fillProductGradeLevelDropDown('testGroupList', data.productsDetails.product,selectedTestSession.testSession.productId);
												 
							//$('#testList').GridUnload();				
							reloadGrids(ProductData.product[selectedProdIndex].testSessionList, ProductData.product[selectedProdIndex].showLevelOrGrade);
							$("#"+selectedTestSession.testSession.itemSetId).trigger('click');
							if(selectedTestSession.testSession.isRandomize == 'Y'){
								$("#randomDis").show();	
								$("#randDisLbl").show();		
								$("#randomDis").val("Y");
								$('#randomDis').attr('checked','checked')							
							}else if(selectedTestSession.testSession.isRandomize == 'N') {
								$("#randomDis").show();	
								$("#randDisLbl").show();		
								$("#randomDis").val("N");
								$('#randomDis').removeAttr('checked');		
							}else {							
								$("#randomDis").hide();	
								$("#randDisLbl").hide();		
								$("#randomDis").val("");
							}
							var subtest = getSubtestTestSession(ProductData.product[selectedProdIndex].testSessionList,selectedTestSession.testSession.itemSetId);
							if(subtest.length>1){
								document.getElementById("testBreak").disabled=false;
								if( selectedTestSession.testSession.enforceBreak == 'T'){
									//$('#testBreak').attr('checked','checked');
									$("#testBreak").attr("checked", true);
				 					$("#testBreak").trigger('click');
				 					$("#testBreak").attr("checked", true);
				 					fillAccessCode(selectedTestSession);
								} else {
									$('#testBreak').removeAttr('checked');
									$('#aCode').val(selectedTestSession.testSession.accessCode);
									
								}
								
							} else {
								$('#aCode').val(selectedTestSession.testSession.accessCode);	
								document.getElementById("testBreak").disabled=true;	
							}
													
												
					
							wizard.accordion("activate", index);					
							$.unblockUI(); 
										
						},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();
							window.location.href="/SessionWeb/logout.do";
							
						},
			complete :  function(){
							 $.unblockUI(); 
						}
		}); 
    }
    
    function populateStudentGrid(wizard,index){
	    $.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'getScheduledStudents.do?testAdminId=' +selectedTestAdminId ,
			type:		'POST',
			dataType:	'json',
			success:	function(data, textStatus, XMLHttpRequest){
							
							if (data.status.isSuccess){			
								AddStudentLocaldata = data.savedStudentsDetails;
								studentMap = new Map();
								
								for(var i =0,j = AddStudentLocaldata.length ; i< j; i++ ) {
									studentMap.put(AddStudentLocaldata[i].studentId, AddStudentLocaldata[i]);
								}
								//console.log("Before" + studentTempMap.count);
								processStudentAccordion();
								//console.log("After" + studentTempMap.count);
							}
							wizard.accordion("activate", index);					
							$.unblockUI(); 
										
						},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();
							window.location.href="/SessionWeb/logout.do";
							
						},
			complete :  function(){
							 $.unblockUI(); 
						}
		}); 
	        
    }
    
    function populateProctorGrid(wizard,index){
	    $.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'getScheduleProctor.do?testAdminId=' +selectedTestAdminId ,
			type:		'POST',
			dataType:	'json',
			success:	function(data, textStatus, XMLHttpRequest){
							
							if (data.status.isSuccess){			
								addProctorLocaldata = data.savedProctorsDetails.proctors;
								processProctorAccordion();
							}
							wizard.accordion("activate", index);					
							$.unblockUI(); 
										
						},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();
							window.location.href="/SessionWeb/logout.do";
							
						},
			complete :  function(){
							 $.unblockUI(); 
						}
		});     
    }
    
 
       
    
    function setSelectedTestAdminId(id){    
   	 selectedTestAdminId = id;
    }
    
  	function resetEditPopUp(){
  	$('#displayMessage').hide();
  	$('#ssAccordion').accordion('activate', 1 );
  	}
  	
  	function calculateTimeInMin(val){
	  	var time = val.split(" ");
	  	var afterOrPost = time[1];
	  	var timeValue = time[0]
	  	var timeVal = timeValue.split(":");
	  	var hour = parseInt(timeVal[0]);
	  	var minutes = parseInt(timeVal[1]);
	  	var calculatedValue = 0;
	  	if (afterOrPost == "AM" ){
	  		if(hour == 12) {
	  			hour = 0;
	  		}
	  	} else {
	  		if(hour != 12) {
	  			hour = hour + 12;
	  		}
	  	}
	  	calculatedValue = hour * 60 + minutes;
	  	return calculatedValue;
  	}
  	
  	function getSubtestTestSession(tList,id){  	  	
	  	for (var i =0,j =tList.length; i < j; i++ ){
	  		if(tList[i].id == id){	  		
	  		return tList[i].subtests;
	  		}  	
	  	}
  	}
  	
  	function fillAccessCode(tList){
  		for (var i =0, j = tList.scheduledUnits.length; i < j; i++ ){
  			  ($("#"+tList.scheduledUnits[i].itemSetId).children() [0]).value = tList.scheduledUnits[i].accessCode;
  			}
  		}
  	

  	
  	
  	
  
  
    
