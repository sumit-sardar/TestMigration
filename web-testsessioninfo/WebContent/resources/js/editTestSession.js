    /**
    *Edit Test Session
    **/   
    var selectedTestAdminId = null;
    var state = null;
    var stdsLogIn = false;
    var selectedTestSession;
    var editDataCache = new Map();
    var editDataMrkStds = new Map();
    var isStdDetClicked = false;
    var isProcDetClicked = false;
  
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
								disableInEdit();
							} else {
								removeDisableInEdit();
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
							open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); },
							 beforeClose: function(event, ui) { resetEditTestSession();
							 }
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
		$("#selectedTestId").val(data.savedTestDetails.testSession.itemSetId);
  	}

    
    function populateSelectTestGrid(wizard,index){
    
    	var testSessionList ={};
    	
    if(editDataCache.get(index)!= null || editDataCache.get(index)!= undefined){   
    	  if(stdsLogIn){
    	  	disableSelectTest(); 
    	  }else{
    	  removeDisableInEdit();    	  
    	  }
  		  wizard.accordion("activate", index);		
    }else{
		 	$.ajax({
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'getUserProductsDetails.do?testAdminId=' +selectedTestAdminId ,
				type:		'POST',
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){
								editDataCache.put(index,"selectTestGrid");
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
														
								if(stdsLogIn){					
								disableSelectTest();
								}else{
								removeDisableInEdit();
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
    }
    
    function populateStudentGrid(wizard,index){
    
	  if(editDataCache.get(index)!= null || editDataCache.get(index)!= undefined){
	  	 	//processStudentAccordion();	   
	  	 	wizard.accordion("activate", index);					
	  }else{
	   	  $.ajax({
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'getScheduledStudents.do?testAdminId=' +selectedTestAdminId ,
				type:		'POST',
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){
								var stAccom = 0;
								if (data.status.isSuccess){
									editDataCache.put(index,data.savedStudentsDetails);	
									AddStudentLocaldata = data.savedStudentsDetails;
									studentMap = new Map();
									for(var i =0,j = AddStudentLocaldata.length ; i< j; i++ ) {									
										studentMap.put(AddStudentLocaldata[i].studentId, AddStudentLocaldata[i]);
										var hasAccom = AddStudentLocaldata[i].hasAccommodations;
										if(hasAccom == 'Yes') {										
								 	 		stAccom++;
								 	 	}
																				
									}
									
								    $('#stuWithAcc').text(stAccom);
									$('#totalStudent').text(AddStudentLocaldata.length);	
									processStudentAccordion();   				
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
    
	  
	        
    }
    
    function populateProctorGrid(wizard,index){
        
	    if(editDataCache.get(index)!= null || editDataCache.get(index)!= undefined){
	    		//processProctorAccordion();
	    		wizard.accordion("activate", index);					
	    }else{
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
										editDataCache.put(index,data.savedProctorsDetails.proctors);	
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
    }
    
 
    function saveEditTestData(){   
    
    	var param;
		var param1 =$("#testDiv *").serialize(); 
	    var param2 = $("#Test_Detail *").serialize();
	    var time = document.getElementById("time").innerHTML;
	    var timeArray = time.split("-");
	    param = param1+"&"+param2+"&startTime="+$.trim(timeArray[0])+"&endTime="+$.trim(timeArray[1]);
	    
	    
	    var selectedstudent = getStudentListArray(AddStudentLocaldata);
	    param = param+"&students="+selectedstudent.toString();
	    
	    var selectedProctors =getProctorListArray(addProctorLocaldata);
	    param = param+"&proctors="+selectedProctors.toString();
	    
	    
	    
	    $.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'saveTest.do',
			type:		'POST',
			data:		 param,
			dataType:	'json',
			success:	function(data, textStatus, XMLHttpRequest){						   
						   
					
								$.unblockUI();
						
							  	closePopUp("scheduleSession");
					
							 						
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
    
  	function resetEditTestSession(){
	  	$('#ssAccordion').accordion('activate', 1 );
	  	selectedTestAdminId = null;
	    state = null;
	    stdsLogIn = false;
	    studentMap = new Map();
	    selectedTestSession = null;
	    addProctorLocaldata = [];
	    AddStudentLocaldata = {};
	    ProductData = null;
		editDataCache = new Map();
  	}
  	
  	function calculateTimeInMin(val){
	  	var time = val.split(" ");
	  	var afterOrPost = time[1];
	  	var timeValue = time[0]
	  	var timeVal = timeValue.split(":");
	  	var hour = eval(timeVal[0]);
	  	var minutes = eval(timeVal[1]);
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
  	

  	function disableInEdit() {
  		disableTestDetails();
  		//disableSelectTest();
  	}
  	
  	function disableSelectTest() {
  		$('#testGroupList').attr("disabled",true);
  		$('#level').attr("disabled",true);
  		$('#testBreak').attr("disabled",true);
  		if($('#aCode') != undefined && $('#aCode') != null) {
  			$('#aCode').attr("disabled",true);
  		}
  		if($('#randomDis') != undefined && $('#randomDis') != null) {
  			$('#randomDis').attr("disabled",true);
  		}
  		if($('#testBreak') != undefined && $('#testBreak') != null) {
  			$('#testBreak').attr("disabled",true);
  		}
  		var allRows = $('#testList').jqGrid('getDataIDs');
  		for(var i = 0; i < allRows.length; i++) {
  			$('#'+allRows[i]).addClass("ui-state-disabled");
  		}
  		$($("#testPager input")[0]).attr("disabled",true);
  		if($("#subtestGrid") != undefined)
  			$("#subtestGrid").addClass("ui-state-disabled");
  		if($("#noSubtest") != undefined)
  			$("#noSubtest").addClass("ui-state-disabled");
  	}
  	
  	function disableTestDetails() {
  		$('#testSessionName').attr("disabled",true);
  		$('#startDate').attr("disabled",true);
  	}
  
	function removeDisableInEdit() {
		$('#testGroupList').removeAttr("disabled");
		$('#level').removeAttr("disabled");
		$('#testBreak').removeAttr("disabled");
		$('#aCode').removeAttr("disabled");
		$('#testSessionName').removeAttr("disabled");
		$('#startDate').removeAttr("disabled");
		if($('#aCode') != undefined && $('#aCode') != null) {
  			$('#aCode').removeAttr("disabled");
  		}
  		if($('#randomDis') != undefined && $('#randomDis') != null) {
  			$('#randomDis').removeAttr("disabled");
  		}
  		if($('#testBreak') != undefined && $('#testBreak') != null) {
  			$('#testBreak').removeAttr("disabled");
  		}
  		var allRows = $('#testList').jqGrid('getDataIDs');
  		for(var i = 0; i < allRows.length; i++) {
  			$('#'+allRows[i]).removeClass("ui-state-disabled");
  		}
  		$($("#testPager input")[0]).removeAttr("disabled");
  		if($("#subtestGrid") != undefined)
  			$("#subtestGrid").removeClass("ui-state-disabled");
  		if($("#noSubtest") != undefined)
  			$("#noSubtest").removeClass("ui-state-disabled");
	}