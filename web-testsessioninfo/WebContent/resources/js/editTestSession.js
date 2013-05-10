    /**
    *Edit Test Session
    **/   
    var selectedTestAdminId = null;
    var state = null;
    var stdsLogIn = false;
    var isTestExpired = false;
    var selectedTestSession;
    var editDataCache = new Map();
    var editDataMrkStds = new Map();
    var isStdDetClicked = false;
    var isProcDetClicked = false;
    var isTestDataExported = false;
    var isEndTestSession = false;
    var isSortable = true;
  	var isProctor = false;
	var selectTestChange = "F";
	var testDtlChange = "F";
	var studentDtlChange = "F";
	var proctorDtlChange = "F";
	var cacheObjVal = {};
	var savedStudentMap = new Map();//used to track student is old or new
	var isCopySession = false; //added  for copy test session
	var isSelectTestDetClicked = false; //added  for copy test session
	var isOKEQProduct = false; // Added for Oklahoma customer
		
	var forceTestBreak = false;
	var selectGE = null;
	var savedAssignedRole = "Owner";
		
  function editTestSession(action){  
  	var activeJQGrid;
  	var rowID;
  	var rowData;
  	if($('#list2').is(':visible'))
  		activeJQGrid = "list2";
  	else if($('#list3').is(':visible'))
  		activeJQGrid = "list3";
  		
  	rowID = $('#'+activeJQGrid).jqGrid('getGridParam', 'selrow');
	rowData = $('#'+activeJQGrid).getRowData(rowID); 
	
	savedAssignedRole = rowData.AssignedRole;
	
	isOKEQActionPerformed = false; // Added for oklahoma customer Equivalent form
	hideProctorDelButton = false;
	hideStudentDelButton = false;
	
	if(rowData.isSTabeProduct == "true")
		isTabeProduct = true;
	else if (rowData.isSTabeAdaptiveProduct == "true")		 		
		isTabeAdaptiveProduct = true;	  		
  
     resetEditSessionPopulatedData();
     $("#showSaveTestMessage").hide();
     $("#endTest").hide();
     cacheObjVal = {};
     var param = {};
     param.testAdminId = selectedTestAdminId;
     if(action != undefined){
     	param.action = action;
     }
 	
 	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'getTestDetails.do',
		type:		'POST',
		dataType:	'json',
		data:		param,
		success:	function(data, textStatus, XMLHttpRequest){
						
						if (data.status.isSuccess){	
							//added for copy test session
							if(data.isCopySession){
								isCopySession = true;
							}else{
								isCopySession = false;
							}
							
							forceTestBreak = data.forceTestBreak;
							selectGE = data.selectGE;
							
							//Added for Oklahoma customer
							isOKAdmin = data.isOkAdmin;
							if(data.savedTestDetails != undefined && data.savedTestDetails.testSession != undefined && 
									(parseInt(data.savedTestDetails.testSession.productId) == 9003 || 
									parseInt(data.savedTestDetails.testSession.productId) == 9007)) {
								isOKEQProduct = true;
								isOKEQActionPerformed = true;
							} else {
								isOKEQProduct = false;
							}
							
							if(data.noTestExists == true){
								noTestExist = true;
								document.getElementById("testDiv").style.display = "none";
								document.getElementById("noTestDiv").style.display = "inline";
							}else{
								noTestExist = false;
								document.getElementById("noTestDiv").style.display = "none";
								document.getElementById("testDiv").style.display = "inline";
							}
															
							if(data.testSessionExpired){							
								isTestExpired = true;
								if (data.savedTestDetails.testSession.isTestSessionDataExported == "T"){
								isTestDataExported == true;
								}						
							}
							if(data.userRoleName.toUpperCase() == "PROCTOR"){
								isProctor = true;
							}
							if(data.savedTestDetails.studentsLoggedIn > 0){
								stdsLogIn = true;
								if(!isTestExpired && !isProctor){
								$("#endTest").show();
								}	
							}
							if (stdsLogIn || isTestExpired || isProctor){
								isSortable = false;								
								disableInEdit();							
							} else {
								removeDisableInEdit();
							}
							if(!noTestExist){
								
								state = "EDIT";
								populateTestDetailGrid(data);
							    createSubtestGridInEdit(data.savedTestDetails, data.hasLocator , data.locatorId);
							    $("#selectedNewTestId").val(data.savedTestDetails.testSession.itemSetId);
							    if(data.savedTestDetails.testSession.showStudentFeedback != undefined && data.savedTestDetails.testSession.showStudentFeedback =="T"){
							     	$("#showStudentFeedback").val(true);
							    } else {
							    	$("#showStudentFeedback").val(false);
							    }
							    var optionHtml = "<option  value='"+data.savedTestDetails.testSession.productId+"'>"+data.savedTestDetails.testSession.testName+"</option>";
     						    $("#testGroupList").html(optionHtml);
							    $("#sData").removeClass("ui-state-disabled");
							    $("#productType").val(data.productType);
								document.getElementById("sData").disabled=false;
							}
							if(data.savedTestDetails.testSession.offGradeBlocked == 'T') {
								blockOffGradeTesting = true;
								if(data.savedTestDetails.scheduledUnits[0].itemSetLevel == undefined) {
									selectedLevel = data.savedTestDetails.scheduledUnits[0].grade;
								}
								else {
									selectedLevel = data.savedTestDetails.scheduledUnits[0].itemSetLevel;
								}
							} else {
								blockOffGradeTesting = false;
							}
							
						onChangeHandler.register("testDetailId");
							
						} else if (data.status.IsSystemError) {
							 var length = 0;
							 
							 if(data.status.validationFailedInfo.message!=undefined){
								   	length= data.status.validationFailedInfo.message.length;
						 	 }
								if(length==0) {
									setSessionSaveMessage(data.status.validationFailedInfo.messageHeader, "", "errorMessage","");
									$('#displayMessage').show(); 
								} else if (length==1) {
									setSessionSaveMessage(data.status.validationFailedInfo.messageHeader, data.status.validationFailedInfo.message[0], "errorMessage","");
									$('#displayMessage').show(); 
								} else  {
									setSessionSaveMessage(data.status.validationFailedInfo.messageHeader,  data.status.validationFailedInfo.message[0], "errorMessage", data.status.validationFailedInfo.message[1]);
									$('#displayMessage').show(); 
								}
								$.unblockUI();
								$('#showSaveTestMessage').show();
							  	closePopUp("scheduleSession");
							  	return;
					  }
						 var titleHeader;
						 if(isCopySession){
						 	titleHeader = $("#copyTestSn").val();
						 } else{
						 	titleHeader = $("#editTestSn").val();
						 }
						 $("#scheduleSession").dialog({  
							title:titleHeader,  
							resizable:false,
							autoOpen: true,
							width: '1024px',
							modal: true,
							closeOnEscape: false,
							open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); 
							$("#displayEditInfo").show();
							if(isProctor){
								$("#displayEditInfo").show();
								$("#titleEditInfo").html($("#fieldDisabled").val());
								$("#messageEditInfo").html($("#noPermission").val());
								$("#contentEditInfo").hide();
							}
							else if(isTestExpired) {
								$("#displayEditInfo").show();
								$("#titleEditInfo").html($("#fieldDisabled").val());
								$("#messageEditInfo").html($("#sessionEnd").val());
								$("#contentEditInfo").hide();
							} else {
								if(stdsLogIn) {
									$("#displayEditInfo").show();
									$("#titleEditInfo").html($("#fieldDisabled").val());
									$("#messageEditInfo").html($("#stuLogged").val());
									$("#contentEditInfo").hide();
								} else {
									if(data.isCopySession){
										$("#displayEditInfo").show();
										$("#titleEditInfo").html("");
										$("#contentEditInfo").show();
										$("#contentEditInfo").html(data.status.successInfo.messageHeader);
									}else{
										$("#displayEditInfo").show();
										$("#titleEditInfo").html("");
										$("#contentEditInfo").show();
										$("#contentEditInfo").html($("#noStudentLogged").val());
										$("#messageEditInfo").html($("#noStudentLogged2").val());
									}
								}
							}
							isPopUp = true;						
							},
							 beforeClose: function(event, ui) { resetEditTestSession();
							 removeDisableInEdit();
							 }
							});	
						setPopupPosition();
						updateLocatorSubtestsList();
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
	
		$("#testSessionName").val(data.savedTestDetails.testSession.testAdminName);
		
		$( "#startDate" ).datepicker( "option" , "minDate" , data.startDate ) ;
		$( "#endDate" ).datepicker( "option" , "minDate" , data.startDate ) ;
		if(data.minLoginEndDate != undefined){//for override login end date
			$( "#startDate" ).datepicker( "option" , "maxDate" , data.minLoginEndDate ) ;
			$( "#endDate" ).datepicker( "option" , "maxDate" , data.minLoginEndDate ) ;
		} else {
			$( "#startDate" ).datepicker( "option" , "maxDate" , null ) ;
			$( "#endDate" ).datepicker( "option" , "maxDate" , null ) ;
		}
		$( "#endDate" ).datepicker( "refresh" );
		$( "#startDate" ).datepicker( "refresh" );
		if(data.isCopySession){
			fillDropDownWithDefaultValue("timeZoneList",data.testZoneDropDownList, data.userTimeZone);
			document.getElementById("startDate").value = data.startDate;
			document.getElementById("endDate").value = data.endDate;
		}else {
			fillDropDownWithDefaultValue("timeZoneList",data.testZoneDropDownList, data.savedTestDetails.testSession.timeZone);
			document.getElementById("startDate").value = data.savedTestDetails.testSession.loginStartDateString;
			document.getElementById("endDate").value = data.savedTestDetails.testSession.loginEndDateString;
		}

		fillDropDownWithDefaultValue("topOrgNode",data.topNodeDropDownList,data.savedTestDetails.testSession.creatorOrgNodeId);
		if(data.topNodeDropDownList == undefined || data.topNodeDropDownList.length<2 || stdsLogIn || isTestExpired || isProctor){
			$("#topOrgNode").attr("disabled",true);
		} else {
			$("#topOrgNode").removeAttr("disabled");
		}
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
    	isPopUp = false;
    if(editDataCache.get(index)!= null && editDataCache.get(index)!= undefined){   
    	  if(stdsLogIn || isTestExpired || isProctor){
    	  	disableSelectTest(); 
    	  }else{
    	  removeDisableInEdit();    	  
    	  }
    	  isPopUp = true;
  		  wizard.accordion("activate", index);		
    }else{
    		var param = {};
    		param.testAdminId = selectedTestAdminId;
    		//added for copy test session
	  		if(isCopySession){
	  			param.action = "copySession";
	  		}
	  		//    		
		 	$.ajax({
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'getUserProductsDetails.do',
				type:		'POST',
				dataType:	'json',
				data:		param,
				success:	function(data, textStatus, XMLHttpRequest){
								cacheObjVal.indexValue = "selectTestGrid";
								editDataCache.put(index,cacheObjVal);
								ProductData = data.productsDetails;
								
								 var selectedProdIndex = fillProductGradeLevelDropDown('testGroupList', data.productsDetails.product,selectedTestSession.testSession.productId);
													 
								//$('#testList').GridUnload();				
								reloadGrids(ProductData.product[selectedProdIndex].testSessionList, ProductData.product[selectedProdIndex].showLevelOrGrade);
								displayProductAcknowledgement(ProductData.product[selectedProdIndex].acknowledgmentsURL);
								// Start : to show the test as selected when it appears in next page
								var curPage = parseInt($('#testList').jqGrid('getGridParam','page'));
								//selectedTestId = selectedTestSession.testSession.itemSetId;
								selectedSubtestId = selectedTestSession.testSession.itemSetId;
								while(!isTestExistInCurrentPage(selectedTestSession.testSession.itemSetId)){
								   curPage = eval(curPage)+eval(1);
								   jQuery("#testList").jqGrid('setGridParam', {page:curPage}).trigger("reloadGrid");
								   var loadedPage = parseInt($('#testList').jqGrid('getGridParam','page')); 
								   if(loadedPage != curPage) {
								   		break;
								   }
								}
								offGradeSubtestChanged = true;
								firstTimeOpen = true;
								$("#"+selectedTestSession.testSession.itemSetId).trigger('click');
								resetSubtestDetails();
								// End : to show the test as selected when it appears in next page 
								if(selectedTestSession.testSession.isRandomize == 'T'){
									$("#randomDis").show();	
									$("#randDisLbl").show();		
									$("#randomDis").val("Y");
									$('#randomDis').attr('checked','checked')							
								}else if(selectedTestSession.testSession.isRandomize == 'F') {
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
									$("#testBreak").removeAttr('disabled');	
									if( selectedTestSession.testSession.enforceBreak == 'T'){
										//$('#testBreak').attr('checked','checked');
										$("#testBreak").attr("checked", true);
					 					$("#testBreak").trigger('click');
					 					$("#testBreak").attr("checked", true);
					 					if(!isCopySession){//added for copy test session
					 						fillAccessCode(selectedTestSession);
					 					}
									} else {
										$('#testBreak').removeAttr('checked');
										if(!isCopySession){ //added for copy test session
											$('#aCode').val(selectedTestSession.testSession.accessCode);
										}
										
									}
									
								} else {
									if(!isCopySession){ //added for copy test session
										$('#aCode').val(selectedTestSession.testSession.accessCode);
									}
									$("#testBreak").attr('disabled', true);	
								}
														
								if(stdsLogIn || isTestExpired || isProctor){					
									disableSelectTest();
								}else{
									removeDisableInEdit();
									if(subtest.length <= 1) {
										$("#testBreak").attr('disabled', true);
									}
									if(locatorOnlyTest){
										$("#hasAutolocator").attr('disabled', true);
										$("#testBreak").attr('disabled', true);
									}
								}
								
								if (forceTestBreak) {
									$("#testBreak").attr('disabled', true);	
								}
								
								onChangeHandler.register("Select_Test");
								isPopUp = true;
								wizard.accordion("activate", index);
								enableDisableLocatorCheckBox();
								populateCheckboxSelection(selectedTestSession.scheduledUnits);
								updateLocatorSubtestsList();				
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
    	isPopUp = false;
	  if(editDataCache.get(index)!= null && editDataCache.get(index)!= undefined){
	  	 	//processStudentAccordion();	   
	  	 	wizard.accordion("activate", index);					
	  }else{
	  	// Added for Oklahoma customer
		// All user roles except proctor who are associated with the test session should be able to add/remove student
		if(isOKEQProduct && !isOKAdmin) {
			var param = {};
			param.selectedTestAdminId = selectedTestAdminId;
			$.ajax({
				async:		false,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'checkSameLevelNonProctor.do',
				type:		'POST',
				dataType:	'json',
				data:		param,
				success:	function(data, textStatus, XMLHttpRequest){
								if(data != null && data != undefined && parseInt(data) > 0) {
									$("#addStudent").show();
									hideStudentDelButton = false;
								} else if (data != null && data != undefined && parseInt(data) == 0) {
									$("#addStudent").hide();
									hideStudentDelButton = true;
								} else {
									$("#addStudent").show();
									hideStudentDelButton = false;
								}
								//$.unblockUI(); 
											
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								$.unblockUI();
								window.location.href="/SessionWeb/logout.do";
								
							},
				complete :  function(){
								 //$.unblockUI(); 
							}
			}); 
		}
	  	if(!offGradeSubtestChanged) {
	  		var param = {};
	  		param.testAdminId = selectedTestAdminId;
	  		//added for copy test session
	  		if(isCopySession){
	  			param.action = "copySession";
	  		}
	  		//
	  		$.ajax({
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'getScheduledStudents.do',
				type:		'POST',
				dataType:	'json',
				data:		param,
				success:	function(data, textStatus, XMLHttpRequest){
								var stAccom = 0;
								savedStudentMap = new Map();
								if (data.status.isSuccess){
									//editDataCache.put(index,data.savedStudentsDetails);
									cacheObjVal.indexValue = "studentDetails";
									editDataCache.put(index,cacheObjVal);
									accomodationMapExisting = data.accomodationMap;
									AddStudentLocaldata = data.savedStudentsDetails;
									studentMap = new Map();
									for(var i =0,j = AddStudentLocaldata.length ; i< j; i++ ) {									
										studentMap.put(AddStudentLocaldata[i].studentId, AddStudentLocaldata[i]);
										savedStudentMap.put(AddStudentLocaldata[i].studentId, AddStudentLocaldata[i]);
										var hasAccom = AddStudentLocaldata[i].hasAccommodations;
										if(hasAccom == 'Yes') {										
								 	 		stAccom++;
								 	 	}
																				
									}
									
								    $('#stuWithAcc').text(stAccom);
									$('#totalStudent').text(AddStudentLocaldata.length);	
									processStudentAccordion();   				
								}
								onChangeHandler.register("addStudentId");
								isPopUp = true;
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
	  	} else {
	  		offGradeSubtestChanged = false;
	  		resetStudentSelection();
	  		$('#stuWithAcc').text(studentWithaccommodation);
			$('#totalStudent').text(AddStudentLocaldata.length);	
			processStudentAccordion();
			onChangeHandler.register("addStudentId");
			isPopUp = true;
			wizard.accordion("activate", index);					
			$.unblockUI();
			cacheObjVal.indexValue = "studentDetails";
			editDataCache.put(index,cacheObjVal);
	  	}
	   	
	   }	  
	        
    }
    
    function populateProctorGrid(wizard,index){
        isPopUp = false;
        if(isProctor) {
        	$("#addProctorButton").hide();
        	$("#addProcMsg2").hide();
        } else {
        	if(isOKEQProduct && !isOKAdmin) {
        	  	if(editDataCache.get(index)== null || editDataCache.get(index)== undefined) {
        		// Added for Oklahoma customer
				// Only state level admin should able to add proctor
					var param = {};
					param.selectedTestAdminId = selectedTestAdminId;
					$.ajax({
						async:		false,
						beforeSend:	function(){
										UIBlock();
									},
						url:		'checkSameLevelNonProctor.do',
						type:		'POST',
						dataType:	'json',
						data:		param,
						success:	function(data, textStatus, XMLHttpRequest){
										if(data != null && data != undefined && parseInt(data) >= 0) {
											$("#addProctorButton").hide();
											$("#addProcMsg2").hide();
											hideProctorDelButton = true;
										} else {
											$("#addProctorButton").show();
											$("#addProcMsg2").show();
											hideProctorDelButton = false;
										}
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
        	} else {
        		$("#addProctorButton").show();
        		$("#addProcMsg2").show();
        	}
        }
	    if(editDataCache.get(index)!= null && editDataCache.get(index)!= undefined){
	    		//processProctorAccordion();
	    		isPopUp = true;
	    		wizard.accordion("activate", index);					
	    }else{
	    		var param = {};
	    		param.testAdminId = selectedTestAdminId;
	    		//added for copy test session
		  		if(isCopySession){
		  			param.action = "copySession";
		  		}
		  		if(isOKEQProduct && isOKAdmin) {
		  			param.okQEAdmin = 'true';
		  		}
		  		//
			    $.ajax({
					async:		true,
					beforeSend:	function(){
									UIBlock();
								},
					url:		'getScheduleProctor.do',
					type:		'POST',
					dataType:	'json',
					data:		param,
					success:	function(data, textStatus, XMLHttpRequest){
									
									if (data.status.isSuccess){	
										//editDataCache.put(index,data.savedProctorsDetails);
										cacheObjVal.indexValue = "proctorDetails";
										editDataCache.put(index,cacheObjVal);	
										addProctorLocaldata = data.savedProctorsDetails;
										previousSavedProctorData = addProctorLocaldata;
										noOfProctorAdded = 	addProctorLocaldata.length;	
										processProctorAccordion();
									}									
									onChangeHandler.register("addProctorId");
									isPopUp = true;
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
        
    function endTestSession(){
    	isEndTestSession = true;
    	saveTest();        
    }
   	function endTestSessionConfirmation(){
    	$("#endTestSessionConfirmationPopUp").dialog({  
			title:$("#confirmAlrt").val(),  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 $("#endTestSessionConfirmationPopUp").css('height',120);
		 var toppos = ($(window).height() - 290) /2 + 'px';
		 var leftpos = ($(window).width() - 410) /2 + 'px';
		 $("#endTestSessionConfirmationPopUp").parent().css("top",toppos);
		 $("#endTestSessionConfirmationPopUp").parent().css("left",leftpos);	  		
   		
   	}
   
    function setSelectedTestAdminId(id){    
   	 selectedTestAdminId = id;
    }
    
  	function resetEditTestSession(){
	  	$('#ssAccordion').accordion('activate', 0 );
	  	//selectedTestAdminId = null;
	    state = null;
	    stdsLogIn = false;
	    studentMap = new Map();
	    selectedTestSession = null;
	    addProctorLocaldata = [];
	    AddStudentLocaldata = {};
	    ProductData = null;
		editDataCache = new Map();
		removeDisableInEdit();
		isTestExpired = false;
		isEndTestSession = false;
		$("#endTest").hide();
		isSortable = true;
		editDataCache = new Map();
        editDataMrkStds = new Map();
        cacheObjVal = {};
        isCopySession = false; //added for copy test session
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
  		var isChecked = $("#testBreak").attr('checked');
  		if(isChecked != undefined && (isChecked == "checked" || isChecked == true)) {
  			var allAccessCodes = $("input[name = aCodeB]");
  			for(var k = 0; k < allAccessCodes.length; k++) {
  				$(allAccessCodes[k]).attr('disabled', true);
  			}
  		}
  		$('#hasAutolocator').attr("disabled","disabled");
  		$('#aCodeB_l').attr("disabled","disabled");
  		
  		$('#modifyTest').attr("disabled",true);
  		setAnchorButtonState('modifyTestButton', true);
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
  		
  		//Not needed
  		//if($("#subtestGrid") != undefined)
  		//	$("#subtestGrid").addClass("ui-state-disabled");
  		//if($("#noSubtest") != undefined)
  		//	$("#noSubtest").addClass("ui-state-disabled");
  	}
  	
  	function disableTestDetails() {
  		$('#testSessionName').attr("disabled",true);
  		$('#startDate').attr("disabled",true);
  		if(isTestExpired || isProctor){  
	  		if(isTestDataExported || isProctor){		
	  			$('#endDate').attr("disabled",true);
	  		}  
  		$( "#slider-range" ).slider( "option", "disabled", true );	
  		$('#timeZoneList').attr("disabled",true);  
  		$('#testLocation').attr("disabled",true);  
  		$('#topOrgNode').attr("disabled",true);
	  		if(!isProctor){
	  			$("#addStudent").hide();
	  		}  		
  		$("#addProctor").hide();  	
  		}
  	}
  
	function removeDisableInEdit() {
		$('#testGroupList').removeAttr("disabled");
		$('#level').removeAttr("disabled");
		if(locatorOnlyTest != undefined && !locatorOnlyTest)
			$('#testBreak').removeAttr("disabled");
		$('#aCode').removeAttr("disabled");
		$('#testSessionName').removeAttr("disabled");
		$('#startDate').removeAttr("disabled");
		$('#endDate').removeAttr("disabled"); 
		if(allSubtests.length != 0 && locatorOnlyTest != undefined && !locatorOnlyTest)
			$('#hasAutolocator').removeAttr("disabled");
  		$('#aCodeB_l').removeAttr("disabled");
		setAnchorButtonState('modifyTestButton', false);
		if($('#modifyTest').length>0) {	$('#modifyTest').attr("disabled",false);}
		if(isTestExpired){
			$( "#slider-range" ).slider( "option", "disabled", false );	
	  		$('#timeZoneList').removeAttr("disabled",true);  
	  		$('#testLocation').removeAttr("disabled",true);  
	  		$('#topOrgNode').removeAttr("disabled",true);
  		}    	 
		if($('#aCode') != undefined && $('#aCode') != null) {
  			$('#aCode').removeAttr("disabled");
  		}
  		if($('#randomDis') != undefined && $('#randomDis') != null) {
  			$('#randomDis').removeAttr("disabled");
  		}
  		if($('#testBreak') != undefined && $('#testBreak') != null && locatorOnlyTest != undefined && !locatorOnlyTest) {
  			$('#testBreak').removeAttr("disabled");
  		}
  		var isChecked = $("#testBreak").attr('checked');
  		if(isChecked != undefined && (isChecked == "checked" || isChecked == true)) {
  			var allAccessCodes = $("input[name = aCodeB]");
  			for(var k = 0; k < allAccessCodes.length; k++) {
  				$(allAccessCodes[k]).removeAttr('disabled');
  			}
  		}
  		var allRows = $('#testList').jqGrid('getDataIDs');
  		for(var i = 0; i < allRows.length; i++) {
  			$('#'+allRows[i]).removeClass("ui-state-disabled");
  		}
  		$($("#testPager input")[0]).removeAttr("disabled");
  		
  		//Not needed
  		//if($("#subtestGrid") != undefined)
  		//	$("#subtestGrid").removeClass("ui-state-disabled");
  		//if($("#noSubtest") != undefined)
  		//	$("#noSubtest").removeClass("ui-state-disabled");
  		$("#del_list6").removeClass('ui-state-disabled');
   	   	$("#addStudent").show();	
  		$("#del_listProctor").removeClass('ui-state-disabled');	
  		$("#addProctor").show(); 
	}
	
	function createSubtestGridInEdit(savedTestDetails , hasLocator , locatorId ){

        var subtestArr = savedTestDetails.scheduledUnits;
        var locatorArr = savedTestDetails.hasLocatorSubtestList;
		var subtestData = '';
		var subtestLength = 0;
		//alert(subtestGridLoaded);
		if(savedTestDetails.testSession.enforceBreak != undefined && savedTestDetails.testSession.enforceBreak == "T"){
			document.getElementById("aCode").value = savedTestDetails.testSession.accessCode;
			$("#hasTestBreak").val("T");
		} else {
			document.getElementById("aCode").value = savedTestDetails.testSession.accessCode;
			$("#hasTestBreak").val("F");
		}
		if (savedTestDetails.testSession.isRandomize == "T"){
			$("#randomDis").val("T");
		} else if (savedTestDetails.testSession.isRandomize == "F"){
			$("#randomDis").val("F");
		} else {
			$("#randomDis").val("");
		}
		if( subtestArr.length > 0){
			subtestLength = subtestArr.length;
			document.getElementById("subtestGrid").style.display = "";
			document.getElementById("noSubtest").style.display = "none";
			var tr = '';
			var th = '';
			subtestData +='<table>';
			th +='<tr class="subtestHeader" >';
			th +='</tr>';
			subtestData += th;
			if(hasLocator && locatorId!=undefined){
				for(var j=0; j<locatorArr.length; j++){
					tr = ''			
					tr +='<tr>';
					tr +='<td >';
					tr +='<input type = "hidden" id ="locator_'+locatorArr[j].itemSetName+'" name ="locatorItemTD" value ="'+selectedTestSession.hasLocatorSubtestList[j].itemSetId+'~'+selectedTestSession.hasLocatorSubtestList[j].itemSetName+'" disabled="disabled" />';
					tr +='</td>';
					tr +='</tr>';				
					subtestData += tr;
				}
			}
			for(var i=0;i<subtestArr.length; i++){
				tr = ''			
				tr +='<tr>';
				tr +='<td >';
				if( hasLocator && locatorId!=undefined &&  subtestArr[i].itemSetId == locatorId) {
					tr +='<input type = "hidden" name ="hasAutolocator" value ="true" />';
					tr +='<input type = "hidden" name ="itemSetIdTD_l" value ="'+subtestArr[i].itemSetId+'" />';
					if(subtestArr[i].itemSetForm != undefined){
						tr +='<input type = "hidden" name ="itemSetForm_l" value ="'+subtestArr[i].itemSetForm+'" />';
					} else {
						tr +='<input type = "hidden" name ="itemSetForm_l" value ="" />';
					}

					if(subtestArr[i].sessionDefault != undefined){
						tr +='<input type = "hidden" name ="sessionDefault_l" value ="'+subtestArr[i].sessionDefault+'" />';
					} else {
						tr +='<input type = "hidden" name ="sessionDefault_l" value ="" />';
					}
					if(savedTestDetails.testSession.enforceBreak != undefined && savedTestDetails.testSession.enforceBreak == "T"){
						tr +='<input type="hidden"  name="aCodeB_l"  value="'+subtestArr[i].accessCode+'"  /></div>';
					}
					if(sessionHasLocator) {
						tr +='<input type = "hidden" id = "locatorCheckbox'+i+'"  name="locatorCheckbox" />';
					}
					
				} else {
					tr +='<input type = "hidden" name ="itemSetIdTD" value ="'+subtestArr[i].itemSetId+'" />';
					if(subtestArr[i].itemSetForm != undefined){
						tr +='<input type = "hidden" name ="itemSetForm" value ="'+subtestArr[i].itemSetForm+'" />';
					} else {
						tr +='<input type = "hidden" name ="itemSetForm" value ="" />';
					}

					if(subtestArr[i].sessionDefault != undefined){
						tr +='<input type = "hidden" name ="sessionDefault" value ="'+subtestArr[i].sessionDefault+'" />';
					} else {
						tr +='<input type = "hidden" name ="sessionDefault" value ="" />';
					}
					if(savedTestDetails.testSession.enforceBreak != undefined && savedTestDetails.testSession.enforceBreak == "T"){
						tr +='<input name="aCodeB" type="text" id="aCodeB'+i+'" value="'+subtestArr[i].accessCode+'"  /></div>';
					}
						
				}
				tr +='</td>';
				tr +='</tr>';				
				subtestData += tr;
					
			}
			subtestData +='</table>';
			document.getElementById("subtestGrid").innerHTML = subtestData;
			if(selectedTestSession.locatorDeliverableUnit.length > 0 ){
				updateLocatorSubtestsForEdit(savedTestDetails.locatorDeliverableUnit ,locatorArr);
			}else{
				updateLocatorSubtestsForEdit(subtestArr ,locatorArr);
			}
			
			//subtestGridLoaded = true;
		}/*else{
			subtestLength = 0;
			subtestGridLoaded = false;
			subtestData = "";
			document.getElementById("subtestGrid").style.display = "none";
			document.getElementById("noSubtest").style.display = "";
		}*/
	}
	
	
	function resetEditSessionPopulatedData(){
	    state = null;
		isStdDetClicked = false;
     	isProcDetClicked = false;
     	$("#selectedNewTestId").val("");
	 	$("#showStudentFeedback").val("");
     	$("#testGroupList").html("");
      	$("#productType").val("");
      	stdsLogIn = false;
      	editDataCache = new Map();
        editDataMrkStds = new Map();
		offGradeSubtestChanged = false;
		cacheObjVal = {};
		savedStudentMap = new Map();
		isFirstAccordSelected = false;
		isSecondAccordSelected = true;
		isCopySession = false;//added for copy test session
		isSelectTestDetClicked = false;//added for copy test session
	}
	function isTestExistInCurrentPage(itemSetId){
		var isetIdArray = $('#testList').jqGrid('getDataIDs');
		if(isetIdArray.length == 0 ){
			return true;
		}
		var found= false;
		for(var ll =0, len =isetIdArray.length; ll<len; ll++ ){
			if(eval(isetIdArray[ll])== eval(itemSetId)){
				found = true;
				break;
			}
		}
	return found;
	
	}
	
	
	var onChangeHandler = (function(){
		var editPopUpChange = "F";
	
		return{
			register: function(args){
				$('#'+args).change(function(e){ 
					switch(args){
						case "Select_Test":
											editPopUpChange = "T";
											break;
						case "testDetailId":
											editPopUpChange = "T";
											break;
						case "addStudentId":
											editPopUpChange = "T";
											break;
						case "addProctorId":
											editPopUpChange = "T";
											break;			
					}				
				});			
			},
			getData: function(){
				return editPopUpChange;				
			},
			reset: function(){		
				editPopUpChange = "F";	
			}
		}		
	})();
	
	function resetSubtestDetails(){
		 hasAutolocator = false;
		 subtestGridLoaded = false;
		 if(locatorSubtest!=null && locatorSubtest!=undefined && locatorSubtest.id !=undefined ) {
		    var savedLocator =  getValueFromDetails(selectedTestSession.scheduledUnits , locatorSubtest.id , "itemSetId");
		    if(savedLocator !=null ) {
		    	hasAutolocator = true;
		    }
		 }
		selectedSubtests  = new Array();
		if(locatorOnlyTest != undefined && locatorOnlyTest){
			if(selectedTestSession.locatorDeliverableUnit!= null && selectedTestSession.locatorDeliverableUnit!= undefined && selectedTestSession.locatorDeliverableUnit != ""){
				prepareSelectedSubtestsFromSavedDetails(allSubtests, selectedTestSession.locatorDeliverableUnit);
			}else{
				prepareSelectedSubtestsFromSavedDetails(allSubtests, allSubtests);
			}
		}else{
			prepareSelectedSubtestsFromSavedDetails(allSubtests, selectedTestSession.scheduledUnits );
		}
		updateAllSubtests(allSubtests, selectedSubtests);
		populateAllSubtestMap(allSubtests)
		createSubtestGrid();
	
	}

	function prepareSelectedSubtestsFromSavedDetails(allSubtests , savedSelectedSubtests){
		selectedSubtests  = new Array();
		var indx = 0;
		for (var ii=0; ii<savedSelectedSubtests.length; ii++ ) {
			  var subtest;  
			  if(locatorOnlyTest != undefined && locatorOnlyTest && selectedTestSession.locatorDeliverableUnit.length == 0){
			  	subtest = getValueFromDetails(allSubtests , savedSelectedSubtests[ii].id, "id" );
			  }else{
			  	subtest = getValueFromDetails(allSubtests , savedSelectedSubtests[ii].itemSetId, "id" );
			  }
			  if(subtest !=null ){
			  	selectedSubtests[indx++]= subtest;
			  	if(savedSelectedSubtests[ii].itemSetForm != undefined)
			  		subtest.level = savedSelectedSubtests[ii].itemSetForm;
			  }
		}
	}
	
	function getValueFromDetails(srcArray , id, property){
		for (var ii=0; ii<srcArray.length; ii++ ) {
			if(srcArray[ii] [property] == id) {
				return srcArray[ii];
			}
		}
		return null;	
	}
	
	function populateCheckboxSelection(scheduledUnits){
		for(var indx=0; indx<scheduledUnits.length;indx++){
			if(document.getElementById(scheduledUnits[indx].itemSetName) != undefined && document.getElementById(scheduledUnits[indx].itemSetName) != null 
				&& scheduledUnits[indx].islocatorChecked != undefined && scheduledUnits[indx].islocatorChecked == "T" || scheduledUnits[indx].islocatorChecked == "Yes"){
				document.getElementById(scheduledUnits[indx].itemSetName).checked = "true";
				selectedLocatorMap.put(scheduledUnits[indx].itemSetName, true);
			}
			if(document.getElementById(scheduledUnits[indx].itemSetName) != undefined && document.getElementById(scheduledUnits[indx].itemSetName) != null 
				&& scheduledUnits[indx].islocatorChecked != undefined && scheduledUnits[indx].islocatorChecked == "F" || scheduledUnits[indx].islocatorChecked == "No"){
				document.getElementById(scheduledUnits[indx].itemSetName).checked = "";
				selectedLocatorMap.put(scheduledUnits[indx].itemSetName, false);
			}
		}
	}
	
	function updateLocatorSubtestsList(){
		prepareDeselectSubtest(selectedSubtests);
		populateLocatorSubtest();
		var found1=false;
		var found2=false;
		
		for(var indx=0; indx<selectedSubtests.length;indx++){
			if(document.getElementById(selectedSubtests[indx].subtestName) != null && document.getElementById(selectedSubtests[indx].subtestName) != undefined) {
				if(selectedSubtests[indx].subtestName.indexOf("Reading") != -1 && !(document.getElementById(selectedSubtests[indx].subtestName).checked)){
					for(var j=0; j<locatorTDList.length;j++){
					if((locatorTDList[j].subtestName).indexOf("Reading") != -1){
						var id = "locator_"+locatorTDList[j].subtestName;
						if(document.getElementById(id) != undefined && document.getElementById(id) != null)
						document.getElementById(id).disabled = "true";
					}
					}
				}else if(selectedSubtests[indx].subtestName.indexOf("Language") != -1 && (selectedSubtests[indx].subtestName.indexOf("Mechanics") == -1) && !(document.getElementById(selectedSubtests[indx].subtestName).checked)){
					for(var j=0; j<locatorTDList.length;j++){
					if((locatorTDList[j].subtestName).indexOf("Language") != -1){
						var id = "locator_"+locatorTDList[j].subtestName;
						if(document.getElementById(id) != undefined && document.getElementById(id) != null)
						document.getElementById(id).disabled = "true";
					}
					}
				} else if(selectedSubtests[indx].subtestName.indexOf("Applied")!= -1 && !(document.getElementById(selectedSubtests[indx].subtestName).checked)){
					found1=true;
				}else if(selectedSubtests[indx].subtestName.indexOf("Computation") != -1 && !(document.getElementById(selectedSubtests[indx].subtestName).checked)){
					found2=true;
				}
			}
		}
		
		if(found1 || found2) {
			for(var j=0; j<locatorTDList.length;j++){
				if((locatorTDList[j].subtestName).indexOf("Computation") != -1 || (locatorTDList[j].subtestName).indexOf("Applied") != -1){
					var id = "locator_"+locatorTDList[j].subtestName;
					if(document.getElementById(id) != undefined && document.getElementById(id) != null)
					document.getElementById(id).disabled = "true";
				}
			}
		}
	}
	
	function updateLocatorSubtestsForEdit(selectedSubtest, locatorTDList){
		prepareDeselectSubtest(selectedSubtest);
		var validate = false;
		for(var indx=0; indx<selectedSubtest.length;indx++){
			var subtestName = selectedSubtest[indx].itemSetName.replace("TABE","");
			if(locatorTDList != undefined && locatorTDList != null){
				for(var j=0; j<locatorTDList.length;j++){
					if((locatorTDList[j].itemSetName).indexOf(subtestName) != -1){
						for(var z=0; z<selectedTestSession.scheduledUnits.length; z++){
							var name = selectedTestSession.scheduledUnits[z].itemSetName.replace("TABE","");
							if(locatorTDList[j].itemSetName.indexOf(name) != -1){
								if(selectedTestSession.scheduledUnits[z].islocatorChecked != undefined && selectedTestSession.scheduledUnits[z].islocatorChecked == "T" || selectedTestSession.scheduledUnits[z].islocatorChecked == "Yes"){
										validate= true;
								}else{
										validate= false;
								}
							}	
						}
						if(validate){
							var id = "locator_"+locatorTDList[j].itemSetName;
							if(document.getElementById(id) != undefined && document.getElementById(id) != null)
							document.getElementById(id).disabled = "";
						}
					}
				}
			}
		}
		
		if(locatorTDList != undefined && locatorTDList != null){
			for(var x=0;x<locatorTDList.length;x++) {
				if(locatorTDList[x].itemSetName.indexOf("Applied")!=-1) {
					var id = "locator_"+locatorTDList[x].itemSetName;
					if(document.getElementById(id) != undefined && document.getElementById(id) != null && (document.getElementById(id).disabled == "" || document.getElementById(id).disabled == false)) {
						for(var y=0;y<locatorTDList.length;y++) {
							var idd = "locator_"+locatorTDList[y].itemSetName;
							if(document.getElementById(idd) != undefined && document.getElementById(idd) != null && idd.indexOf("Computation")!=-1)
								document.getElementById(idd).disabled = "";
						}
					}
				} else if(locatorTDList[x].itemSetName.indexOf("Computation")!=-1) {
					var id = "locator_"+locatorTDList[x].itemSetName;
					if(document.getElementById(id) != undefined && document.getElementById(id) != null && (document.getElementById(id).disabled == "" || document.getElementById(id).disabled == false)) {
						for(var y=0;y<locatorTDList.length;y++) {
							var idd = "locator_"+locatorTDList[y].itemSetName;
							if(document.getElementById(idd) != undefined && document.getElementById(idd) != null && idd.indexOf("Applied")!=-1)
								document.getElementById(idd).disabled = "";
						}
					}
				}
					
			}
		}
	}
