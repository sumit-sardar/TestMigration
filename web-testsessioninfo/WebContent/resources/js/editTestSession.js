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
  
  function editTestSession(){  
     resetEditSessionPopulatedData();
     $("#showSaveTestMessage").hide();
     $("#endTest").hide(); 
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
															
							if(data.testSessionExpired){							
								isTestExpired = true;
								if (data.savedTestDetails.testSession.isTestSessionDataExported == "T"){
								isTestDataExported == true;
								}						
							}
							if(data.savedTestDetails.studentsLoggedIn > 0){
								stdsLogIn = true;
								if(!isTestExpired){
								$("#endTest").show();
								}	
							}							
							if (stdsLogIn || isTestExpired){
								isSortable = false;								
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
							    createSubtestGridInEdit(data.savedTestDetails);
							    $("#selectedNewTestId").val(data.savedTestDetails.testSession.itemSetId);
							    $("#showStudentFeedback").val(data.savedTestDetails.testSession.showStudentFeedback);
							    var optionHtml = "<option  value='"+data.savedTestDetails.testSession.productId+"'>"+data.savedTestDetails.testSession.productId+"</option>";
     						    $("#testGroupList").html(optionHtml);
							    $("#sData").removeClass("ui-state-disabled");
							    $("#productType").val(data.productType);
								document.getElementById("sData").disabled=false;
							}
							
						}
					
						 $("#scheduleSession").dialog({  
							title:"Edit a Test Session:",  
							resizable:false,
							autoOpen: true,
							width: '1024px',
							modal: true,
							open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); 
							$("#displayEditInfo").show();
							if(isTestExpired) {
								$("#displayEditInfo").show();
								$("#titleEditInfo").html($("#fieldDisabled").val());
								$("#messageEditInfo").html($("#sessionEnd").val());
							} else {
								if(stdsLogIn) {
									$("#displayEditInfo").show();
									$("#titleEditInfo").html($("#fieldDisabled").val());
									$("#messageEditInfo").html($("#stuLogged").val());
								} else {
									$("#displayEditInfo").show();
									$("#titleEditInfo").html("");
									$("#contentEditInfo").html($("#noStudentLogged").val());
									$("#messageEditInfo").html($("#noStudentLogged2").val());
								}
							}
							},
							 beforeClose: function(event, ui) { resetEditTestSession();
							 removeDisableInEdit();
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
								// Start : to show the test as selected when it appears in next page 
								var curPage = parseInt($('#testList').jqGrid('getGridParam','page')); 
								while(!isTestExistInCurrentPage(selectedTestSession.testSession.itemSetId)){
								   curPage = eval(curPage)+eval(1);
								   jQuery("#testList").jqGrid('setGridParam', {page:curPage}).trigger("reloadGrid");
								   var loadedPage = parseInt($('#testList').jqGrid('getGridParam','page')); 
								   if(loadedPage != curPage) {
								   		break;
								   }
								}
								$("#"+selectedTestSession.testSession.itemSetId).trigger('click');
								// End : to show the test as selected when it appears in next page 
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
														
								if(stdsLogIn || isTestExpired){					
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
										editDataCache.put(index,data.savedProctorsDetails);	
										addProctorLocaldata = data.savedProctorsDetails;
										noOfProctorAdded = 	addProctorLocaldata.length;	
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
        
    function endTestSession(){
    	isEndTestSession = true;
    	saveTest();    
    }
   
    function setSelectedTestAdminId(id){    
   	 selectedTestAdminId = id;
    }
    
  	function resetEditTestSession(){
	  	$('#ssAccordion').accordion('activate', 0 );
	  	selectedTestAdminId = null;
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
  		if(isTestExpired){  
	  		if(isTestDataExported){		
	  			$('#endDate').attr("disabled",true);
	  		}  
  		$( "#slider-range" ).slider( "option", "disabled", true );	
  		$('#timeZoneList').attr("disabled",true);  
  		$('#testLocation').attr("disabled",true);  
  		$('#topOrgNode').attr("disabled",true);
  		$("#addStudent").hide();  		
  		$("#addProctor").hide();  	
  		}
  	}
  
	function removeDisableInEdit() {
		$('#testGroupList').removeAttr("disabled");
		$('#level').removeAttr("disabled");
		$('#testBreak').removeAttr("disabled");
		$('#aCode').removeAttr("disabled");
		$('#testSessionName').removeAttr("disabled");
		$('#startDate').removeAttr("disabled");
		$('#endDate').removeAttr("disabled"); 
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
  		$("#del_list6").removeClass('ui-state-disabled');
   	   	$("#addStudent").show();	
  		$("#del_listProctor").removeClass('ui-state-disabled');	
  		$("#addProctor").show(); 
	}
	
	function createSubtestGridInEdit(savedTestDetails){

        var subtestArr = savedTestDetails.scheduledUnits;
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
			subtestData +='<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#A6C9E2"><tr><td><table width="100%" class="ts" cellpadding="0" cellspacing="1">';
							
			th +='<tr class="subtestHeader" >';
			th +='<th width="24" height="23" align="center"><strong>#</strong></th>';
			if(savedTestDetails.testSession.enforceBreak!= undefined && savedTestDetails.testSession.enforceBreak == "T"){
				th +='<th width="289" height="23" align="left" style="padding-left:5px;"><strong>Subtest Name </strong></th>';				
				th +='<th width="130" height="23"><div align="center" id="aCodeHead"><strong>Access Code </strong></div></th>';
			}else{
				th +='<th width="419" height="23" align="left" style="padding-left:5px;"><strong>Subtest Name </strong></th>';
			}
			th +='<th width="82" height="23" align="center"><strong>Duration</strong></th>';
			/*if(isTabeProduct && !isTabeLocatorProduct ){
				th +='<th width="34" height="23">&nbsp;</th>';
			}*/
			th +='</tr>';
			subtestData += th;
			for(var i=0;i<subtestArr.length; i++){	
				tr = ''			
				tr +='<tr>';
				tr +='<td height="23" width="24" bgcolor="#FFFFFF">';
				tr +='<div align="center" id="num'+i+'">'+parseInt(i+1)+'<input type="hidden" id="actionTaken'+i+'" value="1"/></div>';
				tr +='<input type = "hidden" name ="itemSetIdTD" value ="'+subtestArr[i].itemSetId+'" />';
				if(subtestArr[i].level != undefined){
					tr +='<input type = "hidden" name ="itemSetForm" value ="'+subtestArr[i].itemSetForm+'" />';
				} else {
					tr +='<input type = "hidden" name ="itemSetForm" value ="" />';
				}
				
				if(subtestArr[i].sessionDefault != undefined){
					tr +='<input type = "hidden" name ="sessionDefault" value ="'+subtestArr[i].sessionDefault+'" />';
				} else {
					tr +='<input type = "hidden" name ="sessionDefault" value ="" />';
				}
				
				tr +='</td>';
				if(savedTestDetails.testSession.enforceBreak != undefined && savedTestDetails.testSession.enforceBreak == "T"){
					tr +='<td height="23" width="289" bgcolor="#FFFFFF" style="padding-left:5px;">';
					tr +='<div align="left" id="sName'+i+'">'+subtestArr[i].itemSetName+'</div>';
					tr +='</td>';
					tr +='<td height="23" width="130" align="center" bgcolor="#FFFFFF">';
					tr +='<div align="center" id="'+subtestArr[i].itemSetId+'">';
					tr +='<input name="aCodeB" type="text" size="13" id="aCodeB'+i+'" value="'+subtestArr[i].accessCode+'" onblur="javascript:trimTextValue(this); return false;" style="padding-left:2px;" maxlength="32" /></div>';
					tr +='</td>';
				}else{
					tr +='<td height="23" width="419" bgcolor="#FFFFFF" style="padding-left:5px;">';
					tr +='<div align="left" id="sName'+i+'">'+subtestArr[i].itemSetName+'</div>';
					tr +='</td>';
				}
				tr +='<td height="23" width="82" align="center" bgcolor="#FFFFFF">';
				//tr +='<div align="center" id="duration'+i+'">'+subtestArr[i].duration+'</div>';
				tr +='</td>';
				/*if(isTabeProduct && !isTabeLocatorProduct){
					tr +='<td height="23" align="center" width="34" bgcolor="#FFFFFF">';
					tr +='<div align="center">';
					tr +='<img id="imgMin'+i+'" src="/SessionWeb/resources/images/minus.gif" width="14" title="Remove Subtest" onclick="javascript:removeSubtestOption(0,'+i+');" />';
					tr +='<img id="imgPlus'+i+'" src="/SessionWeb/resources/images/icone_plus.gif" width="14" title="Add Subtest" onclick="javascript:removeSubtestOption(1,'+i+');" style="display: none;" />';
					tr +='</div>';
					tr +='</td>';
				}*/
				tr +='</tr>';				
				subtestData += tr;		
			}
			subtestData +='</table></td></tr></table>';
			document.getElementById("subtestGrid").innerHTML = subtestData;
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