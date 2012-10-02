	
	var isGridShowBySessionStep3Loaded = false;
	var isGridShowBySessionStep4Loaded = false;
	
	var allTDAndStudentData = new Map();
	var selectedTDAndStudentData =new Map();
	var selectedStudentToResetTest = new Array();
	var customerID = "";
	var creatorOrgId = "";

	
	function updateView() {
	  if($("#resetTestBy").val()=='ses'){
	  	$("#reset_show_by_session").show();
	  	$("#reset_show_by_student").hide();
	  } else {
	  	hideStepsShowBySession(true, true, true, true);
	  	$("#reset_show_by_student").show();
	  	$("#reset_show_by_session").hide();
	  }
	}
	
	function getTDAndStudentList() { 
		var postDataObject = {};
		postDataObject.testAccessCode = $("#bySessionTestAccessCode").val();
		hideStepsShowBySession(false, true, true, true);
		showHideMessage(false, "", "");
		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'findSubtestListBySession.do',
			type:		'POST',
			dataType:	'json',
			data:		 postDataObject,
			success:	function(data, textStatus, XMLHttpRequest){	
							$.unblockUI(); 
							if(data.deliverableItemSetList.length>0){
							    customerID = data.deliverableItemSetList[0].customerId;
								creatorOrgId = data.deliverableItemSetList[0].orgNodeId;
								populate_reset_by_session_step2_dropdown(data.deliverableItemSetList, data.selectedTestAdmin, data.selectedItemSetId );
								$("#reset_by_session_step2").show();
								if(data.studentDetailsList.length>0){
									populate_reset_by_session_step3_student_grid(data.studentDetailsList);
									populateAllTDAndStudentDataMap(data.studentDetailsList);
									$("#reset_by_session_step3").show();
								} else {
									showHideMessage(true,$("#resetTestSearchResultTitle").val(),$("#resetTestBySessionStudentNotFound").val());
								}
								
							} else {
								showHideMessage(true,$("#resetTestSearchResultTitle").val(),$("#resetTestBySessionAccessCodeNotFound").val());
							}
						},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();  
							window.location.href="error.do";
						},
			complete :  function(){
							 $.unblockUI();  
						}
		});
	
	}
	
	function populate_reset_by_session_step2_dropdown( deliverableItemSetList, selectedTestAdmin , selectedItemSetId) {
	    
		var dropdown = '<select id="reset_by_session_step2_subtest_drop_down" onchange="updateStudentListBySession();">';
		for (var kk=0, len=deliverableItemSetList.length; kk<len; kk++ ){
			if(deliverableItemSetList[kk].testAdminId==selectedTestAdmin && deliverableItemSetList[kk].itemSetId == selectedItemSetId ){
		  		dropdown += '<option value="'+deliverableItemSetList[kk].testAdminId+'-'+ deliverableItemSetList[kk].itemSetId+'" selected >&nbsp;'+deliverableItemSetList[kk].itemSetName+'&nbsp;&nbsp;</option>';
		  	} else {
		  		dropdown +='<option value="'+deliverableItemSetList[kk].testAdminId+'-'+ deliverableItemSetList[kk].itemSetId+'" >&nbsp;'+deliverableItemSetList[kk].itemSetName+'&nbsp;&nbsp;</option>';
		  	}
		}
		dropdown +='</select>';
		$("#reset_by_session_step2_subtest_name").html(dropdown);
		
	}
	
	function populate_reset_by_session_step3_student_grid(studentDetailsList){
		$("#reset_by_session_step3_next").addClass("ui-state-disabled");		
		document.getElementById("reset_by_session_step3_next").disabled=true;	
		
		if(isGridShowBySessionStep3Loaded ){
			reload_populate_reset_by_session_step3_student_grid(studentDetailsList);
		} else {
			load_populate_reset_by_session_step3_student_grid(studentDetailsList);
		}
		
	}
	function load_populate_reset_by_session_step3_student_grid(studentDetailsList){
	
		isGridShowBySessionStep3Loaded = true;
		var studentIdTitle = $("#studentIdLabelName").val();
		
		 $("#by_session_step3_student_list").jqGrid({
      	  data: studentDetailsList,         
          datatype: 'local',          
          colNames:[ 'Student Name','Login Name', studentIdTitle , 'Group' , 'Subtest Name', 'Subtest Status','', '', ''],
		   	colModel:[
		   		{name:'studentName',		index:'studentName', 		width:200, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentLoginName',	index:'studentLoginName', 	width:200, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'externalStudentId',	index:'externalStudentId', 	width:150, editable: true, align:"left", sortable:true,	sorttype:'text',search: false,	cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'org_name',			index:'org_name', 			width:150, editable: true, align:"left", sortable:false, 				 search: false,	cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetName',		index:'itemSetName',		width:400, editable: false,align:"left", sortable:true, sorttype:'text', search: false,	cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'completionStatus',	index:'completionStatus',	width:100, editable: false,align:"left", sortable:true, sorttype:'text', search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetLevel',		index:'itemSetLevel', 		width:0,   editable: false, align:"left", sortable:false,hidden:true,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testRosterId',		index:'testRosterId', 		width:0,   editable: false, align:"left", sortable:false,hidden:true,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentItemId',		index:'studentItemId', 		width:0,   editable: false, align:"left", sortable:false,hidden:true,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   		 
		   	],
		   		jsonReader: { repeatitems : false, root:"rows", id:"testRosterId",
		   	records: function(obj) { 
		   	 } },
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:true,
			sortname: 'studentName',
			pager: '#by_session_step3_student_list_pager', 
			viewrecords: true, 
			sortorder: "asc",
			height: 165,  
			onPaging: function() {
				var reqestedPage = parseInt($('#by_session_step3_student_list').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_by_session_step3_student_list_pager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#by_session_step3_student_list').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#by_session_step3_student_list').setGridParam({"page": minPageSize});
				}
				
			},onSelectRow: function (rowid, status) {
				var selectedRowData = $("#by_session_step3_student_list").getRowData(rowid);
				if(status) {
				    var temp = new Object();
				        temp.status="SUN";
					selectedTDAndStudentData.put(selectedRowData.studentItemId, temp);
				} else {
					selectedTDAndStudentData.remove(selectedRowData.studentItemId);
					$("#cb_by_session_step3_student_list").attr("checked", false);
				}
				
				disableEnableNextButtonShowBySession();
				
			},onSelectAll: function (rowids, status) {
				if(status) {
					var keys = allTDAndStudentData.getKeys();
					for(var i=0 ; i<keys.length; i++) {
					  if( selectedTDAndStudentData.get(keys[i]) == null && isShowBySessionStudentEnabled(allTDAndStudentData.get(keys[i]))){
					  	var temp = new Object();
				        temp.status="SUN";
				        selectedTDAndStudentData.put(keys[i], temp);
					  }
					}
					
				} else {
					selectedTDAndStudentData = new Map();
				}
				disableEnableNextButtonShowBySession();
			
			},gridComplete: function() {
				var allRowsInGridPresent = $('#by_session_step3_student_list').jqGrid('getDataIDs');
				for(var k = 0; k < allRowsInGridPresent.length; k++) {
					var selectedRowData = $("#by_session_step3_student_list").getRowData(allRowsInGridPresent[k]);
					if(selectedRowData.completionStatus != 'Completed' && selectedRowData.completionStatus != 'In Progress'){
						$("#"+(k+1)+" td input","#by_session_step3_student_list").attr("disabled", true);
						$("#"+(k+1), "#by_session_step3_student_list").addClass('ui-state-disabled');
					} else if (selectedRowData.itemSetLevel == 'L' && (selectedRowData.completionStatus != 'Completed' || selectedRowData.completionStatus != 'In Progress')){
						$("#"+(k+1)+" td input","#by_session_step3_student_list").attr("disabled", true);
						$("#"+(k+1), "#by_session_step3_student_list").addClass('ui-state-disabled');
					} else if (selectedTDAndStudentData.get(selectedRowData.studentItemId) != null){
						$("#"+(k+1)+" td input").attr("checked", true);
						$("#"+(k+1)).trigger('click');
						$("#"+(k+1)+" td input").attr("checked", true);
					
					}
				}
			},loadComplete: function () {
				if ($('#by_session_step3_student_list').getGridParam('records') === 0) {
            		$('#sp_1_by_session_step3_student_list_pager').text("1");
            		$('#next_by_session_step3_student_list_pager').addClass('ui-state-disabled');
            	 	$('#last_by_session_step3_student_list_pager').addClass('ui-state-disabled');
            	} else {
            		
            	}
            	$.unblockUI();  
				$("#by_session_step3_student_list").setGridParam({datatype:'local'});
				var tdList = ("#by_session_step3_student_list_pager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
	
	
	}
	
	function reload_populate_reset_by_session_step3_student_grid(studentDetailsList){
      $('#by_session_step3_student_list').jqGrid('clearGridData') ;
      jQuery("#by_session_step3_student_list").jqGrid('setGridParam',{ datatype: 'local'});    
   	  jQuery("#by_session_step3_student_list").jqGrid('setGridParam', {data: studentDetailsList,page:1}).trigger("reloadGrid");
      jQuery("#by_session_step3_student_list").sortGrid('studentName',true,'asc');
	}
	
	function updateStudentListBySession(){
	 	var newVal = $("#reset_by_session_step2_subtest_drop_down").val();
	 	hideStepsShowBySession(false, false, false, true);
	 	showHideMessage(false, "", "");
	 	var ids    = newVal.split("-");
	 	var postDataObject = {};
		postDataObject.testAdminId = ids[0];
		postDataObject.itemSetId = ids[1];
		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'findSubtestListBySessionTD.do',
			type:		'POST',
			dataType:	'json',
			data:		 postDataObject,
			success:	function(data, textStatus, XMLHttpRequest){	
							$.unblockUI(); 
							if(data.studentDetailsList.length>0){
								populate_reset_by_session_step3_student_grid(data.studentDetailsList);
								populateAllTDAndStudentDataMap(data.studentDetailsList);
								$("#reset_by_session_step3").show();
							} else {
								hideStepsShowBySession(false,false,true,true);
								showHideMessage(true,$("#resetTestSearchResultTitle").val(),$("#resetTestBySessionStudentNotFound").val());
							}
						},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();  
							window.location.href="error.do";
						},
			complete :  function(){
							 $.unblockUI();  
						}
		});
	}
	
	function populateAllTDAndStudentDataMap(studentDetailsList){
	 	allTDAndStudentData = new Map();
	 	selectedTDAndStudentData = new Map();
	 	for(var kk = 0 ; kk < studentDetailsList.length ; kk++  ){
			allTDAndStudentData.put(studentDetailsList[kk].studentItemId, studentDetailsList[kk]);
		}
	}
	
	function isShowBySessionStudentEnabled(rowData){
		var status = false;
		if(rowData == null){
			status = false;
		} else if(rowData.completionStatus != 'Completed' && rowData.completionStatus != 'In Progress'){
			status = false;
		} else if (rowData.itemSetLevel == 'L' && (rowData.completionStatus != 'Completed' || rowData.completionStatus != 'In Progress')){
			status = false;
		} else {
			status = true;
		}
		return status;
	}
	
	function populateAndDisplayStep4(){
		$("#reset_by_session_step4").show();
		$("#reset_by_session_ticket_id").val("");
		$("#reset_by_session_service_requestor").val("");
		$("#reset_by_session_request_description").val("");
		selectedStudentToResetTest = new Array();
		var keys = selectedTDAndStudentData.getKeys();
		for(var i=0 ; i<keys.length; i++) {
			selectedStudentToResetTest[i]=allTDAndStudentData.get(keys[i]);
		}
		populate_reset_by_session_step4_student_grid(selectedStudentToResetTest);
	}
	
	function populate_reset_by_session_step4_student_grid(vselectedStudentToResetTest){
		if(isGridShowBySessionStep4Loaded ){
			reload_populate_reset_by_session_step4_student_grid(vselectedStudentToResetTest);
		} else {
			load_populate_reset_by_session_step4_student_grid(vselectedStudentToResetTest);
		}
	}
	
	function reload_populate_reset_by_session_step4_student_grid(vselectedStudentToResetTest) {
	 	$('#by_session_step4_student_list').jqGrid('clearGridData') ;
	      jQuery("#by_session_step4_student_list").jqGrid('setGridParam',{ datatype: 'local'});    
     	  jQuery("#by_session_step4_student_list").jqGrid('setGridParam', {data: vselectedStudentToResetTest,page:1}).trigger("reloadGrid");
          jQuery("#by_session_step4_student_list").sortGrid('studentLoginName',true,'asc');
	}
	
	function load_populate_reset_by_session_step4_student_grid(vselectedStudentToResetTest) {
		
		isGridShowBySessionStep4Loaded = true;
		var studentIdTitle = $("#studentIdLabelName").val();
		
		 $("#by_session_step4_student_list").jqGrid({
      	  data: vselectedStudentToResetTest,         
          datatype: 'local',       
          colNames:[ 'Student','Subtest Name','Subtest Status' , 'Start Date' , 'Completion Date', 'Items Answered','Time Spent'],   
		   	colModel:[
		   		{name:'studentLoginName',	index:'studentLoginName', 	width:200, editable: true, align:"left", sortable:true, sorttype:'text', search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetName',		index:'itemSetName',		width:300, editable: false,align:"left", sortable:true, sorttype:'text', search: false,	cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'completionStatus',	index:'completionStatus',	width:150, editable: false,align:"left", sortable:true, sorttype:'text', search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'startDateTime',		index:'startDateTime', 		width:150, editable: false, align:"center",sortable:true,	sorttype:'text', search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'completionDateTime',	index:'completionDateTime', width:150, editable: false, align:"center",sortable:true,	sorttype:'text', search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemAnswered',		index:'itemAnswered', 		width:150, editable: false, align:"center",sortable:false,				 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'timeSpentForDisplay',index:'timeSpentForDisplay',width:100, editable: false, align:"center",sortable:false,				 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   		 
		   	],
		   		jsonReader: { repeatitems : false, root:"rows", id:"testRosterId",
		   	records: function(obj) { 
		   	 } },
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			sortname: 'studentLoginName',
			pager: '#by_session_step4_student_list_pager', 
			viewrecords: true, 
			sortorder: "asc",
			height: 165,  
			onPaging: function() {
				var reqestedPage = parseInt($('#by_session_step4_student_list').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_by_session_step4_student_list_pager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#by_session_step4_student_list').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#by_session_step4_student_list').setGridParam({"page": minPageSize});
				}
				
			},loadComplete: function () {
				if ($('#by_session_step4_student_list').getGridParam('records') === 0) {
            		$('#sp_1_by_session_step4_student_list_pager').text("1");
            		$('#next_by_session_step4_student_list_pager').addClass('ui-state-disabled');
            	 	$('#last_by_session_step4_student_list_pager').addClass('ui-state-disabled');
            	}
            	$.unblockUI();  
				$("#by_session_step4_student_list").setGridParam({datatype:'local'});
				var tdList = ("#by_session_step4_student_list_pager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });

	}
	
	function hideStepsShowBySession ( hideStep1, hideStep2, hideStep3, hideStep4){
		if(hideStep1){
			hideStep1ShowBySession();
		}
		if (hideStep2){
			hideStep2ShowBySession();
		}
		if (hideStep3){
			hideStep3ShowBySession();
		}
		if (hideStep4){
			hideStep4ShowBySession();
		}
	}
	
	function disableEnableNextButtonShowBySession (){
		if(selectedTDAndStudentData.count>0){
			disableEnableButton(false,"reset_by_session_step3_next");
		} else {
			disableEnableButton(true, "reset_by_session_step3_next");
		}
	}
	
	function disableEnableButton(disable , id){
		if(disable){
			$("#"+id).addClass("ui-state-disabled");		
			document.getElementById(id).disabled=true;	
		} else {
			document.getElementById(id).disabled=false;	
			$("#"+id).removeClass("ui-state-disabled");
		}
		
	}
	
	function hideStep1ShowBySession (){
		$("#bySessionTestAccessCode").val("");
		
	}
	function hideStep2ShowBySession (){
		$("#reset_by_session_step2").hide();
		$("#reset_by_session_step2_subtest_name").html("");
		allTDAndStudentData = new Map();
		selectedTDAndStudentData = new Map();
	}
	
	function hideStep3ShowBySession (){
		$("#reset_by_session_step3").hide();
		 $('#by_session_step3_student_list').jqGrid('clearGridData') ;
		 allTDAndStudentData = new Map();
		 selectedTDAndStudentData = new Map();
	}
	
	function hideStep4ShowBySession (){
		$("#reset_by_session_step4").hide();
		$('#by_session_step4_student_list').jqGrid('clearGridData') ;
		$("#reset_by_session_ticket_id").val("");
		$("#reset_by_session_service_requestor").val("");
		$("#reset_by_session_request_description").val("");
	}
	
	function confirmAndResetTestBySession(){
		$("#confirmResetTestBySessionPopup").dialog({  
			title:$("#confirmAlrt").val(),  
	 		resizable:false,
	 		autoOpen: true,
	 		width: '400px',
	 		modal: true,
	 		open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 $("#confirmResetTestBySessionPopup").css('height',120);
		 var toppos = ($(window).height() - 290) /2 + 'px';
		 var leftpos = ($(window).width() - 410) /2 + 'px';
		 $("#confirmResetTestBySessionPopup").parent().css("top",toppos);
		 $("#confirmResetTestBySessionPopup").parent().css("left",leftpos);	
		 $(window).scrollTop(0);
	}
	
	function showHideMessage(show, messageTitle, message){
		 if(show ) {
		 	$("#displayMessage").show();
		 	if(messageTitle!=null && messageTitle != undefined && messageTitle.length>0){
		 		$("#messageTitle").show();
		 		$("#messageTitle").text(messageTitle);
		 	} else {
		 		$("#messageTitle").hide();
		 	}
		 	
		 	if(message!=null && message != undefined && message.length>0){
		 		$("#message").show();
		 		$("#message").text(message);
		 	} else {
		 		$("#message").hide();
		 	}
		 	
		 } else{
			$("#displayMessage").hide();
			$("#message").hide();
			$("#messageTitle").hide();
			$("#message").val("");
			$("#messageTitle").val("");
		 }
	}
	
	function resetTestBySession (){ 
		var newVal = $("#reset_by_session_step2_subtest_drop_down").val();
		var ids    = newVal.split("-");
		var selectedStudentIDs= "";
		for(var i=0 ; i<selectedStudentToResetTest.length; i++) {
			if(i !=0 ){
				selectedStudentIDs +=",";
			}
			selectedStudentIDs +=selectedStudentToResetTest[i].studentItemId;
		}
		closePopUp('confirmResetTestBySessionPopup');
		
	 	var postDataObject = {};
		postDataObject.testAdminId = ids[0];
		postDataObject.itemSetId = ids[1];
		postDataObject.resetStudentDataList = selectedStudentIDs;
		postDataObject.requestDescription = $("#reset_by_session_request_description").val();
		postDataObject.serviceRequestor = $("#reset_by_session_service_requestor").val();
		postDataObject.ticketId = $("#reset_by_session_ticket_id").val();
		postDataObject.customerID = customerID;
		postDataObject.creatorOrgId = creatorOrgId;
		
		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'resetSubtestForStudents.do',
			type:		'POST',
			dataType:	'json',
			data:		 postDataObject,
			success:	function(data, textStatus, XMLHttpRequest){	
							$.unblockUI(); 
							if(data != undefined && data != null && data.studentDetailsList.length>0){
							
								populate_reset_by_session_step3_student_grid(data.studentDetailsList);
								populateAllTDAndStudentDataMap(data.studentDetailsList);
								$("#reset_by_session_step3").show();
								hideStepsShowBySession(false,false,false,true);
								showHideMessage(true, $("#resetTestTitle").val(), $("#resetTestBySessionSuccessMessage").val());
							} else {
								hideStepsShowBySession(false,false,false,false);
							}
							
						},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();  
							window.location.href="error.do";
						},
			complete :  function(){
							 $.unblockUI();  
						}
		});
	
	
	}
	