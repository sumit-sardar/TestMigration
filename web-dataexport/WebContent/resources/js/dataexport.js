var isGridToBeExportStudentLoaded = false;
var isUnscoredStudentListLoaded = false;
var stuItemGridLoaded = false;
var toBeExportStudentRosterList = new Array();
var selectedRowData = {};
var selectedRowObjectScoring = {};
var isRubricPopulated = false;
var itemIdRub = 0;
var itemNumberRub = 0;
var itemTypeRub = '';
var testRosterIdRub = 0;
var itemSetIdRub = 0;
var parentDivId = '';
var data1 = null;
var selectedRosterId;
var selectedDeliveryClientId;
var selectedRData = {};
var selectedItemSetTCVal;
var notCompletedStudentCountVal = 0;
var notTakenStudentCountVal = 0;
var scheduledStudentCountVal = 0;
var totalExportedStudentCountVal = 0;
var testAdminIdsMap = new Map();
var allSelectedTestAdminIds = {};
var testSessionLocalData = {};
var isDataExportBySession = 'false';
var parentProductId;

var deliveryClientId = 0;
var testAdminId = 0;
var extItemSetId = 0;
var tryCount = 0;
var retryLimit = 3;
var isAudioItemLoading= false;

function getStudentList() { 
		var postDataObject = {};
		postDataObject.frameworkProductId=$('#frameworkProductId').val();
		postDataObject.productId=$('#formSelect').val();
		
		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'getStudentForExport.do',
			type:		'POST',
			dataType:	'json',
			data:		 postDataObject,
			success:	function(data, textStatus, XMLHttpRequest){	
							$.unblockUI(); 
							if(data.testSessionList.length>0){
								populate_to_be_export_student_grid(data.testSessionList);
								$('#data_export_step1').show();
								showHideMessage(false,$("#dataExportSearchResultTitle").val(),$("#exportStudentTestSessionSearchResultEmpty").val());
								$('#step1msg').show();
								$('#data_export_session_student_list_div').show();
								$('#data_export_step2').hide();
								$('#data_export_step3').hide();
								notCompletedStudentCountVal = data.notCompletedStudentCount;
								notTakenStudentCountVal = data.notTakenStudentCount;
								scheduledStudentCountVal = data.scheduledStudentCount;
								totalExportedStudentCountVal = data.studentBeingExportCount;
							} else {
								showHideMessage(true,$("#dataExportSearchResultTitle").val(),$("#exportStudentTestSessionSearchResultEmpty").val());
								$('#data_export_step1').show();
								$('#step1msg').hide();
								$('#data_export_session_student_list_div').hide();
								$('#data_export_step2').hide();
								$('#data_export_step3').hide();
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

function populate_to_be_export_student_grid(testSessionList){
		testAdminIdsMap = new Map();
		testSessionLocalData = testSessionList;
		if(isGridToBeExportStudentLoaded ){
			reload_populate_reset_by_session_step3_student_grid(testSessionList);
		} else {
			load_populate_to_be_export_student_grid(testSessionList);
		}
		window.scroll(0,100000);
		
	}
	
function reload_populate_reset_by_session_step3_student_grid(testSessionList){
      $('#to_be_export_student_list').jqGrid('clearGridData') ;
      jQuery("#to_be_export_student_list").jqGrid('setGridParam',{ datatype: 'local'});    
   	  jQuery("#to_be_export_student_list").jqGrid('setGridParam', {data: testSessionList,page:1}).trigger("reloadGrid");
      jQuery("#to_be_export_student_list").sortGrid('testSessionName',true,'asc');
}
		
	
function load_populate_to_be_export_student_grid(testSessionList){
		testSessionLocalData = testSessionList;
		isGridToBeExportStudentLoaded = true;
		$("#to_be_export_student_list").jqGrid({
      	  data: testSessionList,         
          datatype: 'local',          
          colNames:[  $("#titleSessionName").val(), $("#titleStartDate").val(), $("#titleEndDate").val(), $("#titleToBeExport").val(), $("#titleCompleted").val(), $("#titleScheduled").val(), $("#titleStudentStop").val(), $("#titleSystemStop").val(), $("#titleNotTaken").val(), $("#titleIncompleted").val(),''],
		   	colModel:[
		   		{name:'testSessionName',	index:'testSessionName',	width:300, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'startDateString',			index:'startDateString', 			width:100, editable: true, align:"left", sortable:true, sorttype:'date',datefmt: "m/d/Y",search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'endDateString',			index:'endDateString',		 	width:100, editable: true, align:"left", sortable:true,	sorttype:'date',datefmt: "m/d/Y",search: false,	cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'toBeExported',		index:'toBeExported',		width:100,   editable: false,align:"left", sortable:false, search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'complete',			index:'complete', 			width:90,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'scheduled',			index:'scheduled', 			width:90,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentStop',		index:'studentStop', 		width:100,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'systemStop',			index:'systemStop', 		width:100,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'notTaken',			index:'notTaken', 			width:90,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'incomplete',			index:'incomplete', 		width:90,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testAdminId',		index:'testAdminId', 		width:1,   editable: false, align:"left", sortable:false, hidden:true,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		
		   		
		   		
		   	],
		   		jsonReader: { repeatitems : false, root:"rows", id:"testAdminId",
		   	records: function(obj) { 
		   	 } },
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			sortname: 'testSessionName',
			pager: '#to_be_export_student_list_pager', 
			viewrecords: true, 
			sortorder: "asc",
			height: 165,
			hoverrows: false,
			multiselect: true,
			caption: $("#captionToBeExportGrid").val(),
			onPaging: function() {
				var reqestedPage = parseInt($('#to_be_export_student_list').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_to_be_export_student_list_pager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#to_be_export_student_list').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#to_be_export_student_list').setGridParam({"page": minPageSize});
				}
				//$("#dataExportBySession").addClass('ui-state-disabled');
				//$("#dataExportBySession").attr('disabled','true');
				
			},gridComplete: function() {
				var allRowsInGridPresent = $('#to_be_export_student_list').jqGrid('getDataIDs');
				var checkedCount = 0;
				for(var k = 0; k < allRowsInGridPresent.length; k++) {
					var selectedRowData = $("#to_be_export_student_list").getRowData(allRowsInGridPresent[k]);
					var testAdminData = testAdminIdsMap.get(selectedRowData.testAdminId);
					if(testAdminData == null && testAdminData == undefined){
						$("#"+allRowsInGridPresent[k]+" td input").attr("checked", false);
						//$("#"+allRowsInGridPresent[k]).trigger('click');
						//$("#"+allRowsInGridPresent[k]+" td input").attr("checked", true);											
					}
					else{						
						$("#"+allRowsInGridPresent[k]+" td input").attr("checked", true);
						$("#"+allRowsInGridPresent[k]).trigger('click');
						$("#"+allRowsInGridPresent[k]+" td input").attr("checked", true);
						checkedCount++;	
					}		
				}
				/*if(checkedCount == allRowsInGridPresent.length){
					$("#cb_to_be_export_student_list").attr("checked", true);
					$("#cb_to_be_export_student_list").trigger('click');
					$("#cb_to_be_export_student_list").attr("checked", true);
				}*/
				if(testAdminIdsMap.count == testSessionLocalData.length){
					$("#cb_to_be_export_student_list").attr("checked", true);
					$("#cb_to_be_export_student_list").trigger('click');
					$("#cb_to_be_export_student_list").attr("checked", true);
				}
				/*else{
					$("#cb_to_be_export_student_list").attr("checked", false);
					$("#cb_to_be_export_student_list").trigger('click');
					$("#cb_to_be_export_student_list").attr("checked", false);
				}*/
				if(testAdminIdsMap.count > 0){
					$("#dataExportBySession").removeClass('ui-state-disabled');
					$('#dataExportBySession').removeAttr('disabled');
					$("#dataExportNextButton").addClass('ui-state-disabled');
					$("#dataExportNextButton").attr('disabled','true');
				}else{
					$("#dataExportBySession").addClass('ui-state-disabled');
					$("#dataExportBySession").attr('disabled','true');
					$("#dataExportNextButton").removeClass('ui-state-disabled');
					$('#dataExportNextButton').removeAttr('disabled');
				}
			},onSelectRow: function (rowId, status, e) {
				//$("#"+rowId).removeClass('ui-state-highlight');				
				var selectedRowId = rowId;
				var selectedRowData = $("#to_be_export_student_list").getRowData(selectedRowId);
				var testAdminData = testAdminIdsMap.get(selectedRowData.testAdminId);
				if(status){
					//allSelectedTestAdminIds[selectedRowData.testAdminId] = selectedRowData.testAdminId;
					if(testAdminData == null && testAdminData == undefined)
						testAdminIdsMap.put(selectedRowData.testAdminId, selectedRowData);
				}else{
					//delete allSelectedTestAdminIds[selectedRowData.testAdminId];
					if(testAdminData != null && testAdminData != undefined)
						testAdminIdsMap.remove(selectedRowData.testAdminId);
				}
				if(testAdminIdsMap.count == testSessionLocalData.length){
					$("#cb_to_be_export_student_list").attr("checked", true);
					//$("#cb_to_be_export_student_list").trigger('click');
					//$("#cb_to_be_export_student_list").attr("checked", true);
				}else{					
					$("#cb_to_be_export_student_list").attr("checked", false);
				}
				if(testAdminIdsMap.count > 0){
					$("#dataExportBySession").removeClass('ui-state-disabled');
					$('#dataExportBySession').removeAttr('disabled');
					$("#dataExportNextButton").addClass('ui-state-disabled');
					$("#dataExportNextButton").attr('disabled','true');
				}else{
					$("#dataExportBySession").addClass('ui-state-disabled');
					$("#dataExportBySession").attr('disabled','true');
					$("#dataExportNextButton").removeClass('ui-state-disabled');
					$('#dataExportNextButton').removeAttr('disabled');
				}
									
									
			},onSortCol : function(index, columnIndex, sortOrder) { 
				//isSortingEvent = true;
			},onSelectAll: function (aRowids, status) {
				/*var allRowsInGrid = $('#to_be_export_student_list').jqGrid('getDataIDs');
				for(var i = 0; i < allRowsInGrid.length; i++){
					var selectedRowData = $("#to_be_export_student_list").getRowData(allRowsInGrid[i]);
					var testAdminData = testAdminIdsMap.get(selectedRowData.testAdminId);
					if(status){
						if(testAdminData == null && testAdminData == undefined)
							testAdminIdsMap.put(selectedRowData.testAdminId, selectedRowData);
					}
					else{
						if(testAdminData != null && testAdminData != undefined)
							testAdminIdsMap.remove(selectedRowData.testAdminId);
					}
				}*/
				for(var i = 0; i < testSessionLocalData.length; i++){
					var selectedRowData = testSessionLocalData[i];
					var testAdminData = testAdminIdsMap.get(selectedRowData.testAdminId);
					if(status){
						//allSelectedTestAdminIds[selectedRowData.testAdminId] = selectedRowData.testAdminId;
						$("#dataExportBySession").removeClass('ui-state-disabled');
						$('#dataExportBySession').removeAttr('disabled');
						$("#dataExportNextButton").addClass('ui-state-disabled');
						$("#dataExportNextButton").attr('disabled','true');
						if(testAdminData == null && testAdminData == undefined)
							testAdminIdsMap.put(selectedRowData.testAdminId, selectedRowData);
					}
					else{
						//delete allSelectedTestAdminIds[selectedRowData.testAdminId];
						$("#dataExportBySession").addClass('ui-state-disabled');
						$("#dataExportBySession").attr('disabled','true');
						$("#dataExportNextButton").removeClass('ui-state-disabled');
						$('#dataExportNextButton').removeAttr('disabled');
						if(testAdminData != null && testAdminData != undefined)
							testAdminIdsMap.remove(selectedRowData.testAdminId);
					}
				}
					
							
			},loadComplete: function () {
				if ($('#to_be_export_student_list').getGridParam('records') === 0) {
            		$('#sp_1_to_be_export_student_list_pager').text("1");
            		$('#next_to_be_export_student_list_pager').addClass('ui-state-disabled');
            	 	$('#last_to_be_export_student_list_pager').addClass('ui-state-disabled');
            	} 
            	$.unblockUI();  
				$("#to_be_export_student_list").setGridParam({datatype:'local'});
				var tdList = ("#to_be_export_student_list_pager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				if(testAdminIdsMap.count > 0){
					$("#dataExportBySession").removeClass('ui-state-disabled');
					$('#dataExportBySession').removeAttr('disabled');
					$("#dataExportNextButton").addClass('ui-state-disabled');
					$("#dataExportNextButton").attr('disabled','true');
				}else{
					$("#dataExportBySession").addClass('ui-state-disabled');
					$("#dataExportBySession").attr('disabled','true');
					$("#dataExportNextButton").removeClass('ui-state-disabled');
					$('#dataExportNextButton').removeAttr('disabled');
				}
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
	
	
	}

function getUnscoredStudentDetails(isDataExportForSelectedSessions) {
	var postDataObject = {};
	var selectedTestSessionIds = '';		
	if(isDataExportForSelectedSessions == 'true'){
		isDataExportBySession = 'true';
		for (var i = 0; i < testSessionLocalData.length; i++){
				var selectedRowData = testSessionLocalData[i];
				var testAdminData = testAdminIdsMap.get(selectedRowData.testAdminId);
				if(testAdminData != null && testAdminData != undefined){
					tempTestAdminId = selectedRowData.testAdminId + ',';
					selectedTestSessionIds = selectedTestSessionIds + tempTestAdminId;
				}	
		}
		selectedTestSessionIds = selectedTestSessionIds.substr(0,selectedTestSessionIds.length -1);
		postDataObject.selectedTestSessionIds = selectedTestSessionIds; // need to set the selectedTestSessionIds here
	}	
	else{
		isDataExportBySession = 'false';
		postDataObject.selectedTestSessionIds = '';
	}
	postDataObject.frameworkProductId = $('#frameworkProductId').val();	
	postDataObject.productId=$('#formSelect').val();
	
	$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'getUnscoredStudentDetails.do',
			type:		'POST',
			dataType:	'json',
			data:		 postDataObject,
			success:	function(data, textStatus, XMLHttpRequest){	
							$.unblockUI(); 
							if(data.studentList != undefined && data.studentList.length>0 || (data.studentBeingExportCount != undefined && data.studentBeingExportCount > 0)) {
								if(data.unscoredStudentCount != undefined && data.unscoredStudentCount>0)
								{
									$("#studentBeingExptd").text(data.studentBeingExportCount);
									$("#incScoredStudent").text(data.unscoredStudentCount);
									populate_unscored_student_grid(data.studentList);
									$("#data_export_step2").show();
									$("#data_export_step3").hide();
									window.scroll(0,100000);
								} else {
									if(isDataExportForSelectedSessions == 'true'){ 
										$("#studentBeingExptdStep3").text(data.studentBeingExportCount);
										$("#scheduledStudent").text(data.scheduledStudentCount);
										$("#notTakenStudent").text(data.notTakenStudentCount);
										$("#notCompleteStudent").text(data.notCompletedStudentCount);
									}else{										
										$("#studentBeingExptdStep3").text(totalExportedStudentCountVal);
										$("#scheduledStudent").text(scheduledStudentCountVal);
										$("#notTakenStudent").text(notTakenStudentCountVal);
										$("#notCompleteStudent").text(notCompletedStudentCountVal);
									}
									$("#data_export_step3").show();
									$("#data_export_step2").hide();
									$('#dataExportSubmitButton').show();
									if(data.studentBeingExportCount != undefined && parseInt(data.studentBeingExportCount) > 0) {
										$("#dataExportSubmitButton").removeClass('ui-state-disabled');
										$("#dataExportSubmitButton").removeAttr('disabled');
									} else {
										$("#dataExportSubmitButton").addClass('ui-state-disabled');
										$("#dataExportSubmitButton").attr('disabled','true');
									}
									window.scroll(0,100000);
								}		
							}
							else {
								//showHideMessage(true,$("#dataExportSearchResultTitle").val(),$("#exportUnscoredStudentSearchResultEmpty").val());
									if(isDataExportForSelectedSessions == 'true'){ 
										$("#studentBeingExptdStep3").text(data.studentBeingExportCount);
										$("#scheduledStudent").text(data.scheduledStudentCount);
										$("#notTakenStudent").text(data.notTakenStudentCount);
										$("#notCompleteStudent").text(data.notCompletedStudentCount);
									}else{										
										$("#studentBeingExptdStep3").text(totalExportedStudentCountVal);
										$("#scheduledStudent").text(scheduledStudentCountVal);
										$("#notTakenStudent").text(notTakenStudentCountVal);
										$("#notCompleteStudent").text(notCompletedStudentCountVal);
									}
									$("#data_export_step3").show();
									$("#data_export_step2").hide();
									$('#dataExportSubmitButton').show();
									$('#dataExportViewButton').hide();
									$('#submitJobTop').hide();
									$('#submitJobBottom').hide();
									$('#jobIdDisplay').hide();
									$("#dataExportSubmitButton").addClass('ui-state-disabled');
									$("#dataExportSubmitButton").attr('disabled','true');
									window.scroll(0,100000);
							}
							//reseting dataExportSummaryLbl div
							$("#jobId").text('');
							$('#jobIdDisplay').hide();
							$('#dataExportViewButton').hide();
							$('#submitJobTop').hide();
							$('#submitJobBottom').hide();
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

function populate_unscored_student_grid(studentList) {
		if(isUnscoredStudentListLoaded){
			reload_load_populate_unscored_student_grid(studentList);
		} else {
			load_populate_unscored_student_grid(studentList);
		}
		window.scroll(0,100000);
}

function reload_load_populate_unscored_student_grid(studentList){
      $('#data_export_scoring_incomplete_student_list').jqGrid('clearGridData') ;
      jQuery("#data_export_scoring_incomplete_student_list").jqGrid('setGridParam',{ datatype: 'local'});    
   	  jQuery("#data_export_scoring_incomplete_student_list").jqGrid('setGridParam', {data: studentList,page:1}).trigger("reloadGrid");
      jQuery("#data_export_scoring_incomplete_student_list").sortGrid('testSessionName',true,'asc');
}
function load_populate_unscored_student_grid(studentList) {
		isUnscoredStudentListLoaded = true;
		$("#data_export_scoring_incomplete_student_list").jqGrid({
      	  data: studentList,         
          datatype: 'local',          
          colNames:[  $("#titleLoginId").val(), $("#titleStudentName").val(), $("#titleSessionName").val(), $("#titleGrade").val(), $("#titleStudentId").val(), $("#titleStatus").val(),'','','','',''],
		   	colModel:[
		   		{name:'loginId',		index:'loginId',		width:350, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:updateFieldFormat },
		   		{name:'studentName',	index:'studentName', 	width:150, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testSessionName',index:'testSessionName',width:150, editable: true, align:"left", sortable:true,	sorttype:'text',search: false,	cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',			index:'grade',			width:90,   editable: false,align:"left", sortable:true, sorttype:'text', search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentIdNumber',index:'studentIdNumber',width:150,   editable: false, align:"left", sortable:true,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'scoringStatus',	index:'scheduled', 		width:150,   editable: false, align:"left", sortable:true,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testAdminId',	index:'testAdminId', 	width:1,   editable: false, hidden:true,  align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'rosterId',	index:'rosterId', 			width:1,   editable: false, hidden:true,  align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetIdTC',	index:'itemSetIdTC', 	width:1,   editable: false, hidden:true,  align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentId',	index:'studentId', 			width:1,   editable: false, hidden:true,  align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'accessCode',	index:'accessCode', 			width:1,   editable: false, hidden:true,  align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   		
		   	],
		   		jsonReader: { repeatitems : false, root:"rows", id:"rosterId",
		   	records: function(obj) { 
		   	 } },
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			sortname: 'testSessionName',
			pager: '#data_export_scoring_incomplete_student_list_pager', 
			viewrecords: true, 
			sortorder: "asc",
			height: 165,
			hoverrows: false,
			width:$("#gbox_to_be_export_student_list").width(),
			caption : $('#incStudentCaption').val(),
			onPaging: function() {
				var reqestedPage = parseInt($('#data_export_scoring_incomplete_student_list').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_data_export_scoring_incomplete_student_list_pager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#data_export_scoring_incomplete_student_list').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#data_export_scoring_incomplete_student_list').setGridParam({"page": minPageSize});
				}
				
			},gridComplete: function() {
				var allRowsInGridPresent = $('#data_export_scoring_incomplete_student_list').jqGrid('getDataIDs');
				for(var k = 0; k < allRowsInGridPresent.length; k++) {
					var selectedRowData = $("#data_export_scoring_incomplete_student_list").getRowData(allRowsInGridPresent[k]);
					
				}
			},
			onSelectRow: function (rowId) {
				$("#"+rowId).removeClass('ui-state-highlight');
			},loadComplete: function () {
				if ($('#data_export_scoring_incomplete_student_list').getGridParam('records') === 0) {
            		$('#sp_1_data_export_scoring_incomplete_student_list_pager').text("1");
            		$('#next_data_export_scoring_incomplete_student_list_pager').addClass('ui-state-disabled');
            	 	$('#last_data_export_scoring_incomplete_student_list_pager').addClass('ui-state-disabled');
            		$('#data_export_scoring_incomplete_student_list').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#data_export_scoring_incomplete_student_list').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/ExportWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitleGrd").val()+"</th></tr><tr width='100%'><td colspan='6'>"+"No records found"+"</td></tr></tbody></table></td></tr>"); 	
            	} 
            	$.unblockUI();
           
				$("#data_export_scoring_incomplete_student_list").setGridParam({datatype:'local'});
				var tdList = ("#data_export_scoring_incomplete_student_list_pager_left table.ui-pg-table  td");
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
		 		$("#message").html(message);
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
	
	function updateFieldFormat (cellvalue, options, rowObject){
		var val = cellvalue;
			val = "<a href='#' style='color:blue; text-decoration:underline;' onClick = 'javascript:showStudnedItemList(\""+rowObject.loginId+"\",\""+
						rowObject.testAdminId+"\",\""+rowObject.itemSetIdTC+"\",\""+rowObject.accessCode+"\",\""+rowObject.testSessionName+"\",\""+rowObject.studentName+"\",\""+rowObject.rosterId+"\"); return false;'>"+cellvalue+"</a>";
	return val;	
	
	}
	function setPopupPosition(popupId){
		var toppos = ($(window).height() - 145) /2 + 'px';
		var leftpos = ($(window).width() - 1024) /2 + 'px';
		$("#"+popupId).parent().css("top",toppos);
		$("#"+popupId).parent().css("left",leftpos);	
	}
	
	function showStudnedItemList(loginId, testAdminId, itemSetIdTC, accessCode, testSessionName, studentName, rosterId) {
		selectedRowData.loginId = loginId;
		selectedRowData.testAdminId = testAdminId;
		selectedRowData.itemSetIdTC = itemSetIdTC;
		selectedRowData.rosterId = rosterId;
		selectedRosterId = rosterId;
		selectedRData = {};
		selectedRData.userName = loginId;
		selectedRData.studentName = studentName;
		selectedRData.testSessionName = testSessionName;
		selectedRData.accessCode = accessCode;
		selectedItemSetTCVal = itemSetIdTC;
		fillStudentFields();
		openHandScorePopup(rosterId,itemSetIdTC);
	}
	
	function fillStudentFields(){
		$("#studentNameScr").text(selectedRData.studentName);
		$("#loginNameScr").text(selectedRData.userName);
		$("#sessionNameScr").text(selectedRData.testSessionName);
		$("#accessCodeScr").text(selectedRData.accessCode);
	}
	
	
	function openHandScorePopup(rosterId,itemSetIdTC){
	//
		UIBlock();
	 	$("#studentScoringId").dialog({  
				title:$("#scoringPopupTitle").val(),  
				resizable:false,
				autoOpen: true,
				width: '1024px',
				modal: true,
				closeOnEscape: false,
				open: function(event, ui) {$(".ui-dialog-titlebar-close").hide();
					$(".ui-pg-input",this).css('position','relative').css('z-index',2000);
				}
			});	
		setPopupPosition('studentScoringId');		
		populateStudentItemList(rosterId,itemSetIdTC);
	}
	
	 function populateStudentItemList(rosterId,itemSetIdTC) {
	 	if(!stuItemGridLoaded){
	 		loadStudentItemList(rosterId,itemSetIdTC);
	 	}else {
	 		reloadStudentItemList(rosterId,itemSetIdTC);
	 	}
	 }
	
	function reloadStudentItemList(rosterId,itemSetIdTC) {
		var postDataObject = {};
		postDataObject.q = 2;
	 	postDataObject.rosterId = rosterId;
 		postDataObject.itemSetIdTC = itemSetIdTC;
		jQuery("#studentItemListGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
		jQuery("#studentItemListGrid").jqGrid('setGridParam', {url:'beginDisplayStudItemList.do',postData:postDataObject,page:1}).trigger("reloadGrid");
		jQuery("#studentItemListGrid").sortGrid('itemSetName',true,'asc');

	}
 function loadStudentItemList(rosterId,itemSetIdTC) {
	stuItemGridLoaded = true;
	var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.rosterId = rosterId;
 		postDataObject.itemSetIdTC = itemSetIdTC;
 		$("#studentItemListGrid").jqGrid({         
          url:'beginDisplayStudItemList.do', 
		 mtype:   'POST',
		 postData: postDataObject,
		 datatype: "json",                   
          colNames:[$("#itemGripItemNo").val(),$("#itemGripSubtest").val(), $("#itemGripScoreItm").val(), $("#itemGripViewQues").val(), $("#itemGripViewRubric").val(), $("#sesGridStatus").val(),$("#itemGripManual").val(), $("#itemGripMaxScr").val(), $("#itemGripObtained").val(), "itemSetId" , "parentProductId", "deliveryClientId", "testAdminId", "extItemSetId"],
		   	colModel:[
		   		{name:'itemSetOrder',index:'itemSetOrder', width:120, editable: true, align:"left", sorttype:'int', sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetName',index:'itemSetName', width:180, editable: true, align:"left", sorttype:'text', sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemType',index:'itemType', width:180, editable: true, align:"left", sorttype:'text', formatter:responseLinkFmatter, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemType',index:'itemType', width:180, editable: true, align:"left", sorttype:'text', formatter:quesLinkFmatter, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemType',index:'itemType', width:180, editable: true, align:"left", sorttype:'text', formatter:rubricLinkFmatter, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'answered',index:'answered',editable: true, width:160, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'scoreStatus',index:'scoreStatus', width:260, editable: true, align:"left", sorttype:scoreStatusUnformatter, sortable:true, formatter:scoreStatusFormatter, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'maxPoints',index:'maxPoints',editable: true, width:150, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'scorePoint',index:'scorePoint',editable: true, width:150, align:"left",sorttype:scoreObtainedUnformatter, sortable:true, formatter:scoreObtainedFormatter, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetId',index:'itemSetId',editable: true, width:0,hidden: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'parentProductId',index:'parentProductId',editable: true, hidden: true, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'deliveryClientId',index:'deliveryClientId',editable: true, width:0,hidden: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testAdminId',index:'testAdminId',editable: true, width:0,hidden: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'extItemSetId',index:'extItemSetId',editable: true, width:0,hidden: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"scorableItems", id:"itemId",
		    records: function(obj) {
		   	if (obj.processScoreBtn == "false"){
		   		//Process button disable
		   		setAnchorButtonState("processScoreSBS", true);
		   		setAnchorButtonState("processScore", true);
		   	}else{
		   		setAnchorButtonState("processScoreSBS", false);
		   		setAnchorButtonState("processScore", false);
		   	}
		   	} },
		   	
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			pager: '#studentItemListPager', 
			sortname: 'itemSetName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 170,
			width: 960,
			hoverrows: false,
			editurl: 'beginDisplayStudItemList.do',
			caption:$("#itemListGripCap").val(),
			onPaging: function() {
				var reqestedPage = parseInt($('#studentItemListGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_studentItemListPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#studentItemListGrid').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#studentItemListGrid').setGridParam({"page": minPageSize});
				}
				
			},
			onSelectRow: function (rowId) {
					$("#"+rowId).removeClass('ui-state-highlight');
			},
			loadComplete: function () {
			var $this = $(this),
				        datatype = $this.getGridParam('datatype');
				
				    if (datatype === "xml" || datatype === "json") {
				        setTimeout(function () {
				            $this.trigger("reloadGrid");
				        }, 100);
				    }
				if ($('#studentItemListGrid').getGridParam('records') === 0) {
            		$('#sp_1_studentItemListPager').text("1");
            		$('#next_studentItemListPager').addClass('ui-state-disabled');
            		$('#last_studentItemListPager').addClass('ui-state-disabled');
            		$('#studentItemListGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#studentItemListGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/ScoringWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#sbiItemEmptyGridTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#sbsGridItemEmpty").val()+"</td></tr></tbody></table></td></tr>");
            	}
				$.unblockUI();  
				$("#studentItemListGrid").setGridParam({datatype:'local'});
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	  
}

function responseLinkFmatter(cellvalue, options, rowObject){
		var val = cellvalue;
		var answered = rowObject.answered;
		var itemSetIdTD = rowObject.itemSetId;
		var itemId = null;
		if(rowObject._id_){
			itemId = rowObject._id_;
		}else{
			itemId = rowObject.itemId;
		}
		parentProductId = rowObject.parentProductId;
		deliveryClientId  = rowObject.deliveryClientId;
		var type;
		if(cellvalue=="AI") {
        	type = "Audio Response";
        } else {
        	type = "Text Response";
        }
        if(answered != undefined && answered == "Answered") {
        	val = "<a href='#' style='color:blue; text-decoration:underline;' onClick='javascript:showQuesAnsPopup(\"" + itemId +"\",\""+
        	 rowObject.itemSetOrder + "\",\"" + rowObject.itemType + "\",\"" + selectedRosterId + "\", \"" +
        	   itemSetIdTD + "\",\"" + rowObject.maxPoints + "\",\"" + rowObject.scorePoint + "\",\"" + rowObject.scoreStatus+"\",\"" + parentProductId + "\",\"" + rowObject.deliveryClientId + "\",\"" + rowObject.testAdminId + "\",\"" + rowObject.extItemSetId + "\"); return false;'>"+type+"</a>";
        } else {
        	val = "<span style='color:#999999; text-decoration:underline;'>"+type+"</span>";
        }
		return val;
	}
	
	function quesLinkFmatter(cellvalue, options, rowObject){
		var val = cellvalue;
		var answered = rowObject.answered;
		var itemSetIdTD = rowObject.itemSetId;
		var itemId = null;
		if(rowObject._id_){
			itemId = rowObject._id_;
		}else{
			itemId = rowObject.itemId;
		}
		parentProductId = rowObject.parentProductId;
		deliveryClientId = rowObject.deliveryClientId;
		testAdminId = rowObject.testAdminId;
		//console.log("testAdminId=="+testAdminId);
		extItemSetId = rowObject.extItemSetId;
		//console.log("extItemSetId=="+extItemSetId);
		var type = "View Question";
		
        	val = "<a href='#' style='color:blue; text-decoration:underline;' onClick='javascript:showQuesPopup(\"" + itemId +"\",\""+
        	 rowObject.itemSetOrder + "\",\"" + rowObject.itemType + "\",\"" + selectedRosterId + "\", \"" +
        	  itemSetIdTD + "\",\"" + rowObject.maxPoints + "\",\"" + rowObject.scorePoint + "\",\"" + rowObject.scoreStatus+ "\",\"" + rowObject.parentProductId  + "\",\"" + rowObject.deliveryClientId + "\",\"" + rowObject.testAdminId + "\",\"" + rowObject.extItemSetId + "\"); return false;'>"+type+"</a>";
      
		return val;
	}
	
	function rubricLinkFmatter(cellvalue, options, rowObject){
		var val = cellvalue;
		var answered = rowObject.answered;
		var itemSetIdTD = rowObject.itemSetId;
		var itemId = null;
		if(rowObject._id_){
			itemId = rowObject._id_;
		}else{
			itemId = rowObject.itemId;
		}

		var type = "View";
		
        	val = "<a href='#' style='color:blue; text-decoration:underline;' onClick='javascript:showAnswerPopup(\"" + itemId +"\",\""+
        	 rowObject.itemSetOrder + "\",\"" + rowObject.itemType + "\",\"" + selectedRosterId + "\", \"" +
        	  itemSetIdTD + "\",\"" + rowObject.deliveryClientId + "\"); return false;'>"+type+"</a>";
       
		return val;
	}

 function submitJobDetails(){
 		closePopUp('confirmationPopupOnSubmit')
 		var postDataObject = {};
 		postDataObject.frameworkId = $('#frameworkProductId').val();
 		postDataObject.isDataExportBySession =  isDataExportBySession;
		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'submitJob.do',
			type:		'POST',
			dataType:	'json',
			data:		 postDataObject,
			success:	function(data, textStatus, XMLHttpRequest){	
							$.unblockUI();
						 	$("#jobId").text(data.jobId);
							$('#dataExportSubmitButton').hide();
							$('#dataExportViewButton').show();
							$('#submitJobTop').show();
							$('#submitJobBottom').show();
							$('#jobIdDisplay').show();
							$('#dataExportViewButton').click(function() {
            					$("html,body").animate({scrollTop:0},100);
       							 });
						},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();  
							window.location.href="error.do";
						},
			complete :  function(XMLHttpRequest, textStatus){
							 $.unblockUI();  
							 if(textStatus == "success")
							 	refreshStudentList();
						}
 });
 }
 
 
 	function scoreStatusUnformatter(cellvalue, options){
		var formattedCellValue = cellvalue;
		var answered = options.answered;
		if(answered != undefined && answered == "Not Answered") {
			formattedCellValue = "Complete";
		}
		return formattedCellValue;
	}
	
	function scoreStatusFormatter(cellvalue, options, rowObject) {
		var val = cellvalue;
		var answered = rowObject.answered;
		if(answered != undefined && answered == "Not Answered") {
			val = "Complete";
		}
		return val;
	}
	
	
	function scoreObtainedFormatter(cellvalue, options, rowObject) {
		var val = cellvalue;
		var completed = rowObject.scoreStatus;
		var answered = rowObject.answered;
		if(completed == "Incomplete") {
			if(answered != undefined && answered == "Answered") {
				val = "-";
			} else {
				val = "0";
			}
		}
		return val;
	}
	function scoreObtainedUnformatter(cellvalue, options){
		var formattedCellValue = cellvalue;
		var completed = options.scoreStatus;
		var answered = options.answered;
		if(completed == "Incomplete") {
			if(answered != undefined && answered == "Answered") {
				formattedCellValue = "-";
			} else {
				formattedCellValue = "0";
			}
		}
		return formattedCellValue;
	}
	function updateMaxPoints(scoreCutOff){
		var select = document.getElementById('pointsDropDown');
		 select.options.length = 0; 
		 addOption(select , "Please Select", "" );
		
		  for(var i=0; i <= scoreCutOff; i++) {  
		    addOption(select,i,i);
		     } 
	}
	
	
	function getBMTItemURL (rosterIid, testAdminId, itemId, extTstItemSetId, elementDiv, iframe) {
 	var param = {};
 	param.oasRosterId = rosterIid;
	param.oasTestAdministrationID = testAdminId;
	param.itemid = itemId;
	param.oasTestId = extTstItemSetId;
 	
 	var initparam = {};
 	initparam.testAdminId = testAdminId;
 	
 	var bmtURL = '';
 	var bmtResponseURL = '';
 	var isSuccess = false;
 	
 	var dynTable = document.createElement('table');
 	dynTable.id = "bmtMessages";
 	dynTable.setAttribute("style","margin: auto;position: relative;top: 50%;");
 	
 	
		$.ajax({
				async:		false,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'getBMTApiUrl.do',
				type:		'POST',
				data:		initparam,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){	
								if(data.bmtAPIUrl){
									bmtURL = data.bmtAPIUrl;
									if(bmtURL.indexOf("https") > -1){
										elementDiv.innerHTML = "";
										isSuccess = true;
									}else{
										$("#bmtFrame").remove();
										dynTable.innerHTML = "<tr><td><div><img src='../resources/images/messaging/icon_error.gif' border='0' width='16' height='16'></div></td><td><div>"+$("#bmtNetworkFailure").val()+"</div></td></tr>";
					        			elementDiv.appendChild(dynTable);
					        			$.unblockUI();
									}
								}else{
									$("#bmtFrame").remove();
									dynTable.innerHTML = "<tr><td><div><img src='../resources/images/messaging/icon_error.gif' border='0' width='16' height='16'></div></td><td><div>"+$("#bmtNetworkFailure").val()+"</div></td></tr>";
				        			elementDiv.appendChild(dynTable);
				        			$.unblockUI();
								}	
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								setTimeout(function(){ iframe.contentWindow.location.href="/SessionWeb/logout.do"; }, 100); //added a timeout as at times the login screen does not show in the popup.
								$.unblockUI();					
							}
			});
 	
 	
 	if(isSuccess){
	
		$.ajax({
					async:		true,
					beforeSend:	function(){
								UIBlock();
							},
					url:		bmtURL,
					type:		'POST',
					data:		JSON.stringify(param),
					dataType:	'json',
					contentType: 'application/json',
					success:	function(data, status, jqXHR){
									bmtResponseURL = data.url;
									iframe.src = bmtResponseURL;
									elementDiv.appendChild(iframe);
								},
					error  :    function(jqXHR, textStatus, errorThrown){
																		
									if (textStatus == 'timeout') {
							            if (tryCount < retryLimit) {
							            	 tryCount++;
							            	 setTimeout(getBMTItemURL(rosterIid, testAdminId, itemId, extTstItemSetId, elementDiv),15000);
							            }else{
							            	$("#bmtFrame").remove();
							            	dynTable.innerHTML = "<tr><td><div><img src='../resources/images/messaging/icon_error.gif' border='0' width='16' height='16'></div></td><td><div>"+$("#bmtNetworkFailure").val()+"</div></td></tr>";
							        		elementDiv.appendChild(dynTable);
							            }
							        }else if(textStatus == 'error'){
							        	$("#bmtFrame").remove();
							        	if(jqXHR.status == 404){
							        		dynTable.innerHTML = "<tr><td><div><img src='../resources/images/messaging/icon_error.gif' border='0' width='16' height='16'></div></td><td><div>"+$("#bmtItemUnavailable").val()+"</div></td></tr>";
							        		elementDiv.appendChild(dynTable);
							        	}else if(jqXHR.status == 500){
							        		dynTable.innerHTML = "<tr><td><div><img src='../resources/images/messaging/icon_error.gif' border='0' width='16' height='16'></div></td><td><div>"+$("#bmtNetworkFailure").val()+"</div></td></tr>";
							        		elementDiv.appendChild(dynTable);
							        	}else{
											dynTable.innerHTML = "<tr><td><div><img src='../resources/images/messaging/icon_error.gif' border='0' width='16' height='16'></div></td><td><div>"+$("#bmtNetworkFailure").val()+"</div></td></tr>";
							        		elementDiv.appendChild(dynTable);					        	
							        	}
							        }else{ 
							        	return;
							        }
								},
					complete :  function(){
									 $.unblockUI();  
								}
			});
		}	
	}
	
	
	function showQuesPopup(id,itemSetOrder,itemType,testRosterId,itemSetId, maxPoints, scoreObtained, scoringStatus, parentProductId, deliveryClientId, testAdminId, extItemSetId){
			var score= null;
			var status = null;
			selectedRowObjectScoring.id = id;
			selectedRowObjectScoring.itemSetOrder = itemSetOrder;
			selectedRowObjectScoring.itemType = itemType
			selectedRowObjectScoring.testRosterId = testRosterId;
			selectedRowObjectScoring.itemSetId = itemSetId;
			selectedRowObjectScoring.maxPoints = maxPoints;
			selectedRowObjectScoring.parentProductId = parentProductId;
			
			selectedRowObjectScoring.deliveryClientId = deliveryClientId;
			selectedRowObjectScoring.testAdminId = testAdminId;
			selectedRowObjectScoring.extItemSetId = extItemSetId;
			
			document.getElementById('displayMessageForQues').style.display = "none";
		 	var element = document.getElementById('questionInfo');
		 	
		 	var iframe = document.createElement('iframe');		
		 	//console.log("deliveryClientId=="+deliveryClientId);
		 	
		 
		 	
		 	 if(deliveryClientId == 2){
		 	 	iframe.name = "bmtFrame";
				iframe.id = "bmtFrame";
				iframe.width = "100%";
				iframe.height = "100%";
				element.appendChild(iframe);
				getBMTItemURL(selectedRowObjectScoring.testRosterId, selectedRowObjectScoring.testAdminId, selectedRowObjectScoring.id, selectedRowObjectScoring.extItemSetId, element, iframe);
		 	 }else{
		 		if (parentProductId == 7500){
					iframe.name = "jsFrame"; //For Laslinks A/B/Espanol A
				}else {
					iframe.name = "swfFrame"; //For Laslinks C/D/Espanol B
				}
				iframe.src="/ScoringWeb/itemPlayer/index.jsp?itemSortNumber=" + itemSetOrder +"&itemNumber=" + id + "&parentProductId=" + parentProductId + "";
				iframe.width = "900";
				iframe.height = "530";
				element.appendChild(iframe);
		 	 }
			updateMaxPoints(maxPoints);		
			var scoreAndPoints =  getScorePoints();
			score = scoreAndPoints[0];
			status = scoreAndPoints[1];
			selectedRowObjectScoring.scoreObtained = score;
			selectedRowObjectScoring.scoringStatus = status;
		 	
		 	clearMessage();
			$("#questionDetail").dialog({  
				title:$("#questionPopupTitle").val() + itemSetOrder,  
				resizable:false,
				autoOpen: true,
				width: '1024px',
				modal: true,
				closeOnEscape: false,
				open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
			});	
		 	
}
 function showQuesAnsPopup(id,itemSetOrder,itemType,testRosterId,itemSetId, maxPoints, scoreObtained, scoringStatus , parentProductId, deliveryClientId, testAdminId, extItemSetId ){
			var score= null;
			var status = null;
			selectedRowObjectScoring.id = id;
			selectedRowObjectScoring.itemSetOrder = itemSetOrder;
			selectedRowObjectScoring.itemType = itemType
			selectedRowObjectScoring.testRosterId = testRosterId;
			selectedRowObjectScoring.itemSetId = itemSetId;
			selectedRowObjectScoring.maxPoints = maxPoints;
			selectedRowObjectScoring.parentProductId = parentProductId;
			selectedRowObjectScoring.deliveryClientId = deliveryClientId;
			selectedRowObjectScoring.testAdminId = testAdminId;
			selectedRowObjectScoring.extItemSetId = extItemSetId;				
			$("#crText").hide();
			$("#audioPlayer").hide();
				
			document.getElementById('displayMessageForQues').style.display = "none";
		 	var element = document.getElementById('questionInformation');
		 	var iframe = document.createElement('iframe');
		 	/*added for BMT audio player*/
		 	if(deliveryClientId == 2){
		 	 		iframe.name = "bmtFrame";
					iframe.id = "bmtFrame";
					iframe.width = "100%";
					iframe.height = "100%";
					element.appendChild(iframe);
					getBMTItemURL(selectedRowObjectScoring.testRosterId, selectedRowObjectScoring.testAdminId, selectedRowObjectScoring.id, selectedRowObjectScoring.extItemSetId, element, iframe);
		 	 }else{
		 	 	// If not BMT. OAS Item will load.
			 		if (parentProductId == 7500){
						iframe.name = "jsFrame"; //For Laslinks A/B/Espanol A
					}else {
						iframe.name = "swfFrame"; //For Laslinks C/D/Espanol B
					}
					iframe.src="/ScoringWeb/itemPlayer/index.jsp?itemSortNumber=" + itemSetOrder +"&itemNumber=" + id + "&parentProductId=" + parentProductId + "";
					iframe.width = "900";
					iframe.height = "530";
					element.appendChild(iframe);
		 	 }	
			updateMaxPoints(maxPoints);		
			if (scoreObtained == null || scoreObtained == undefined || scoringStatus == null || scoringStatus == undefined){
				var scoreAndPoints =  getScorePoints();
				score = scoreAndPoints[0];
				status = scoreAndPoints[1];
			}else{
			  	score = scoreObtained;
			  	status = scoringStatus;
			}
			selectedRowObjectScoring.scoreObtained = score;
			selectedRowObjectScoring.scoringStatus = status;				
		

			updateScore(score, status);
			//viewRubricNewUI(id,itemSetOrder, itemType, testRosterId, itemSetId);
		 	itemIdRub = id;
		 	itemNumberRub = itemSetOrder;
		 	itemTypeRub = itemType;
		 	testRosterIdRub = testRosterId;
		 	itemSetIdRub = itemSetId;
		 	//setTimeout("activateAccordion()", 2000);
		 	
		 	clearMessage();
			$("#questionAnswerDetail").dialog({  
				title:$("#responsePopupTitl").val() + itemSetOrder,  
				resizable:false,
				autoOpen: true,
				width: '1024px',
				modal: true,
				closeOnEscape: false,
				open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
			});	
		 	
}

function getScorePoints(){
				var rowElement = document.getElementById(selectedRowObjectScoring.id);
				var pointStatus = [];
				if(rowElement){				
					
					var score = rowElement.lastChild.previousSibling.previousSibling.previousSibling.previousSibling.previousSibling.innerHTML;
					var status = rowElement.lastChild.previousSibling.previousSibling.previousSibling.previousSibling.previousSibling.previousSibling.previousSibling.innerHTML;
					if ( isNaN(status)){
						score = trim(score);
						status = trim(status);
						pointStatus[0] = score;
						pointStatus[1] = status;
						}else{
							rowElement = $('#'+selectedRowObjectScoring.testRosterId+',#studentItemListGrid');
							rowElement = rowElement[0];
							
							if(rowElement){
								score = rowElement.lastChild.previousSibling.innerHTML;
								status = rowElement.lastChild.previousSibling.previousSibling.previousSibling.innerHTML;
								score = trim(score);
								status = trim(status);
								pointStatus[0] = score;
								pointStatus[1] = status;							
							}
					}
				}else{
					rowElement = $('#'+selectedRowObjectScoring.testRosterId,'#studentItemListGrid');
					rowElement = rowElement[0];

					if(rowElement){
					var score = rowElement.lastChild.innerHTML;
					var status = rowElement.lastChild.previousSibling.previousSibling.innerHTML;
					score = trim(score);
					status = trim(status);
					pointStatus[0] = score;
					pointStatus[1] = status;					
					}
				}
				
				return pointStatus;
			}

function updateScore(score,status){
		
			var complete = trim(status);
			if(complete =="Complete"){
		
				var select = document.getElementById('pointsDropDown');
				
				for(var i=0; i< select.options.length; i++){
				if(select.options[i].value == trim(score)){
						select.options[i].selected = 'true';
						break;
					}
				
				}
			}
		}
	
		function addOption(selectbox,text,value )
		{
			var optn = document.createElement("OPTION");
			optn.text = text;
			optn.value = value;
			selectbox.options.add(optn);
		}


	
 function formSave() {
			closePopUp('confirmationPopupQues');

			
			var itemId =  selectedRowObjectScoring.id ;
			var itemSetId = selectedRowObjectScoring.itemSetId  ;
			var messageObject = {messageElement:"displayMessageForQues",
													infoElement:"infoIconQues",
													errorElement:"errorIconQues",
													contentElement:"contentMainQues"
													};

			var param = "&itemId="+itemId+"&itemSetId="+itemSetId+"&rosterId="+selectedRowObjectScoring.testRosterId + "&score="+$("#pointsDropDown option:selected").val()+"&itemSetIdTC="+selectedItemSetTCVal;    
			var optionValue = $("#pointsDropDown option:selected").val();

			if($("#pointsDropDown option:selected").val() != ''){
					$.ajax(
						{
								async:		true,
								beforeSend:	function(){											
												UIBlock();												
											},
								url:		'saveDetails.do',
								type:		'POST',
								data:		param,
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
													//Message
												
													if(data.SaveStatus.isSuccess){
													var dataToBeAdded = {scoreStatus:"Complete",scorePoint:$("#pointsDropDown option:selected").val()};
				
													jQuery("#studentItemListGridSBS").setRowData(itemId, dataToBeAdded, "first");
													jQuery("#studentItemListGrid").setRowData(itemId, dataToBeAdded, "first");	
													var dataToBeAddedItem = {scoringStatus:"Complete",scorePoint:$("#pointsDropDown option:selected").val()};
													jQuery("#itemStudentListGridSBI").setRowData(selectedRowObjectScoring.testRosterId,dataToBeAddedItem,"first");	
													var dataForScoreByStudentGrid;	
													
													if (data.SaveStatus.completionStatus == 'CO'){
													dataForScoreByStudentGrid	= {scoringStatus:'Complete'};
													}else {
													dataForScoreByStudentGrid	= {scoringStatus:'Incomplete'};			
													}
													jQuery("#scoreByStudentListGrid").setRowData(selectedRowObjectScoring.testRosterId,dataForScoreByStudentGrid,"first");
													
													
													if(data.SaveStatus.completionStatus){
														
														if(data.SaveStatus.completionStatus == 'CO'){
															setAnchorButtonState("processScoreSBS", false);
															setAnchorButtonState("processScore", false);
														}else{
															setAnchorButtonState("processScoreSBS", true);
															setAnchorButtonState("processScore", true);
															}
														}	
													buildMessage(messageObject,"scoreSuccess", true);
													}else{
													//Message
										
													buildMessage(messageObject,"scoringError",false);
													}
																			
													$.unblockUI(); 
												
											},
								error  :    function(XMLHttpRequest, textStatus, errorThrown){
												//changes for defect #66003
												window.location.href="/SessionWeb/logout.do";
											},
								complete :  function(){
												//alert('after complete....');
											//	$.unblockUI(); 
											}
						}
					);
				}
 }
 
  function buildMessage(argObject,element, status){
	 document.getElementById(argObject.messageElement).style.display = "block";	
	 if(status){
	 	$("#"+argObject.infoElement).show();
	 	$("#"+argObject.errorElement).hide();
	 }else{
		 $("#"+argObject.infoElement).hide();
	 	$("#"+argObject.errorElement).show();
	 }
	 var textMessage = $("#"+element).val();
	$("#"+argObject.contentElement).text(textMessage);
 }
 
function processScore(element){
				
			var messageObject = {messageElement:"displayMessageStudent",
													infoElement:"infoIconStu",
													errorElement:"errorIconStu",
													contentElement:"contentMainStu"
													};
		if (isButtonDisabled(element))
				return true; 
				
		var param = "&rosterId="+selectedRosterId;
		
		$.ajax(
						{
								async:		true,
								beforeSend:	function(){											
												UIBlock();												
											},
								url:		'rescoreStudent.do',
								type:		'POST',
								data:		param,
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
													//Message
												
													if(data.SaveStatus.isSuccess){
																			
													buildMessage(messageObject,"processSuccessful", true);
													
													}else{
													//Message
													buildMessage(messageObject,"processError", false);
													}
																			
													$.unblockUI(); 
												
											},
								error  :    function(XMLHttpRequest, textStatus, errorThrown){
												//changes for defect #66003
												window.location.href="/SessionWeb/logout.do";
											},
								complete :  function(){
												//alert('after complete....');
											//	$.unblockUI(); 
											}
						}
					);
		}
		
 function clearMessage(){
 document.getElementById('displayMessageForQues').style.display = "none";	
 document.getElementById('displayMessageStudent').style.display = "none";	 
 }
 function stopBMTAudio(){
	$("#iframeAudio").contents().find('#stopIcon').trigger('click');
}
 
 function closePopUp(dailogId){
	$('.ui-widget-overlay').css('height', '100%');
	if(dailogId == 'sessionScoringId') {
		isScoreByItemClicked = false;
	}
	if (dailogId == 'questionAnswerDetail'){
		if(isRubricPopulated) {
			var x = document.getElementById('iframeDiv');
			if(x != null && x != undefined) {
				stopAudio();
			}
		}
		/*added to stop the bmt audio on close of popup*/
		if(deliveryClientId==2){
			stopBMTAudio();
		}
		$('#quesAnsAccordion').accordion('activate', 0 );
		var element = document.getElementById('questionInformation');
		while(element.hasChildNodes()){
			if(element.lastChild.name == "bmtFrame"){
				element.lastChild.src = "";
			}
			element.removeChild(element.lastChild);
		}
		
		if(document.getElementById("itemType").value == "AI" ){
			if(deliveryClientId==2){
				$("#iframeAudio").removeAttr('style');
			}else{
				document.getElementById("iframeAudio").contentWindow.clearApplet();
			}
		}
		isRubricPopulated = false;
		data1 = null;
		parentDivId = '';
		
		var subIframe = $('#rubricIframe','#rubricInformation');
		var xsrc = $('#rubricIframe','#rubricInformation').attr('src');
		if(subIframe != undefined) {
			var iFrameObj = subIframe.contents();
			if(iFrameObj != undefined) {
				iFrameObj.find("#rubricTable tr:not(:first)").remove();
				iFrameObj.find("#exemplarsTable tr:not(:first)").remove();
			}
		}
		$('#rubricIframe','#rubricInformation').attr('src','');
		$('#rubricIframe','#rubricInformation').attr('src',xsrc);
		selectedRowObjectScoring = {};
	}
	
	if (dailogId == 'questionDetail'){
		
		$('#questionAccordion').accordion('activate', 0 );
		var element = document.getElementById('questionInfo');
		while(element.hasChildNodes()){
			if(element.lastChild.name == "bmtFrame"){
				element.lastChild.src = "";
			}
			element.removeChild(element.lastChild);
		}
		data1 = null;
		selectedRowObjectScoring = {};
	}
	
	if (dailogId == 'answerDetail'){
		
	/*	$('#ansAccordion').accordion('activate', 0 );
		var element = document.getElementById('rubricInfo');
		while(element.hasChildNodes()){
			element.removeChild(element.lastChild);
		}*/
		
		isRubricPopulated = false;
		data1 = null;
		parentDivId = '';
		
		var subIframe = $('#rubricIframe', '#rubricInfo');
		var xsrc = $('#rubricIframe', '#rubricInfo').attr('src')
		if(subIframe != undefined) {
			var iFrameObj = subIframe.contents();
			if(iFrameObj != undefined) {
				iFrameObj.find("#rubricTable tr:not(:first)").remove();
				iFrameObj.find("#exemplarsTable tr:not(:first)").remove();
			}
		}
		$('#rubricIframe', '#rubricInfo').attr('src','');
		$('#rubricIframe', '#rubricInfo').attr('src',xsrc);
		//selectedRowObjectScoring = {};
	}
	
	if (dailogId == 'studentScoringId') {
		// need to reset the selectedTestSessionIds here
		if(isDataExportBySession == 'true')
			getUnscoredStudentDetails('true'); // Currently if the scoring popup is closed, grid reload function will be called and it will be a server side call
		else	
			getUnscoredStudentDetails('false');		
	}
	
	$("#"+dailogId).dialog("close");
}

function showAnswerPopup(id,itemSetOrder,itemType,testRosterId,itemSetId,deliveryClientId){

		 	clearMessage();
			$("#answerDetail").dialog({  
				title:$("#rubricPopupTitle").val() + itemSetOrder,  
				resizable:false,
				autoOpen: true,
				width: '850px',
				modal: true,
				closeOnEscape: false,
				open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
			});	
			parentDivId = $("#answerDetail").attr('id');
			
			//alert("parentDivId -> " + parentDivId);
		 	viewRubric(id,itemSetOrder, itemType, testRosterId, itemSetId);
}

function viewRubric(itemIdRubric, itemNumber, itemType, testRosterId, itemSetId) {
	UIBlock();
	var param = "&itemId="+itemIdRubric+"&itemNumber="+itemNumber+"&itemType="+itemType+"&testRosterId="+testRosterId+"&itemSetId="+itemSetId+"&deliveryClientId="+deliveryClientId;
	var itemId = itemIdRubric; 
	var itemNumber = itemNumber;
	

	$.ajax(
		{
				async:		true,
				beforeSend:	function(){
								UIBlock();
								//$("#audioPlayer").hide();
								//$("#crText").hide();
							},
				url:		'showQuestionAnswer.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){									
								 var questionNumber = itemNumber;
								 data1 = data.questionAnswer;
								 
								populateTableNew();
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								$.unblockUI();  
								window.location.href="/SessionWeb/logout.do";
							},
				complete :  function(){
								//$.unblockUI(); 
							}
				}
			);
			
	
	}
	
	
function populateTableNew() {
		var subIframe;
		if(parentDivId != null && parentDivId != '') {
			if (parentDivId == "answerDetail") {
				subIframe = $('#rubricInfo .rubricIframePage');
			}
		}
		else {
			subIframe = $('#rubricIframe','#rubricInformation');
		}
		
		var iFrameObj = subIframe.contents();
											
		var counter = 0;
		var rowCountRubric = iFrameObj.find("#rubricTable tr").length;
											
		var rowCountExemplar = iFrameObj.find("#exemplarsTable tr").length;								
		var cellCountExemplar = iFrameObj.find("#exemplarsTable tr td").length;							
																			
		//Rows needs to be deleted, since dynamically new rows are added everytime
		iFrameObj.find("#rubricTable tr:not(:first)").remove();
		iFrameObj.find("#exemplarsTable tr:not(:first)").remove();
												
		if(cellCountExemplar == 1)	 {
			iFrameObj.find("#rubricExemplarId").hide();
			iFrameObj.find("#exemplarNoDataId").show();
		} else {
			iFrameObj.find("#rubricExemplarId").show();
			iFrameObj.find("#exemplarNoDataId").hide();
		}	

		if(data1.rubricData.entry) {
			iFrameObj.find("#rubricNoDataId").hide();
			iFrameObj.find("#rubricTableId").show();
			
			if(iFrameObj.find('#rubricTableId').length==0)
			{
				//if nothing loaded it means the iframe's content is not loaded yet
				//bind  this function to the iframe's load event
				subIframe.bind('load',function(){
				populateTableNew();	
				subIframe.unbind('load'); //unbind once done
				//$.unblockUI();
				});
				
				//return to prevent the rest of the function from executing now
				return;
				
			}
			
			
																		 
			for(var i=0;i<data1.rubricData.entry.length;i++) {									
				var description = handleSpecialCharactersNewUI(data1.rubricData.entry[i].rubricDescription);
				description=description.replace(/\n/g,'<br/>');
				iFrameObj.find("#rubricTable tr:last").
					after('<tr><td><center><small>'+
						data1.rubricData.entry[i].score+
							'</small></center></td><td><small>'+description+'</small></td></tr>');

				if(data1.rubricData.entry[i].rubricExplanation){
					var explanation = handleSpecialCharactersNewUI(data1.rubricData.entry[i].rubricExplanation);
					explanation=explanation.replace(/\n/g,'<br/>');
					var response = handleSpecialCharactersNewUI(data1.rubricData.entry[i].sampleResponse);
					response=response.replace(/\n/g,'<br/>');
					iFrameObj.find("#exemplarsTable tr:last").
						after('<tr><td><center><small>'+
							data1.rubricData.entry[i].score+
								'</small></center></td><td><small>'+
									response+
										'</small></td><td><small>'+
											explanation+
												'</small></td></tr>');																		
				} else {
					counter++;
					if(counter==data1.rubricData.entry.length) {
						iFrameObj.find("#exemplarNoDataId").show();
						iFrameObj.find("#rubricExemplarId").hide();
					}
				}
			}
		} else {
			iFrameObj.find("#exemplarNoDataId").show();
			iFrameObj.find("#rubricNoDataId").show();
			iFrameObj.find("#rubricExemplarId").hide();
			iFrameObj.find("#rubricTableId").hide();									
		}
								
		if(deliveryClientId!=2 || !isAudioItemLoading){
				$.unblockUI();
		}
	}
	
	
	function viewRubricNewUI (itemIdRubric, itemNumber, itemType, testRosterId, itemSetId) {
	UIBlock();
	var param = "&itemId="+itemIdRubric+"&itemNumber="+itemNumber+"&itemType="+itemType+"&testRosterId="+testRosterId+"&itemSetId="+itemSetId+"&deliveryClientId="+deliveryClientId;
	var itemId = itemIdRubric; 
	var itemNumber = itemNumber;

	$.ajax(
		{
				async:		true,
				beforeSend:	function(){
								UIBlock();
								$("#audioPlayer").hide();
								$("#crText").hide();
							},
				url:		'showQuestionAnswer.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){									
								 var questionNumber = itemNumber;
								 data1 = data.questionAnswer;
								 
								var crTextResponse = "";
								var isAudioItem = data1.scrContent.isAudioItem;
								isAudioItemLoading=isAudioItem;
								var linebreak ="\n\n";
								
								if(isAudioItem){
								$("#crText").hide();
								document.getElementById("itemType").value = "AI";
								/*added for BMT audio player*/
									var audioResponseString;
									if(deliveryClientId==2){
										audioResponseString=data1.scrContent.s3AudioUrl;
										window.s3AudioURl=audioResponseString;
									}else{
									/*For OAS player*/
										 audioResponseString = data1.scrContent.audioItemContent;
										 audioResponseString = audioResponseString.substr(13);
										 audioResponseString = audioResponseString.split("%3C%2F");
										document.getElementById("audioResponseString").value = audioResponseString[0];
									}	
									
								document.getElementById("deliveryClientIdQA").value = deliveryClientId ;
								document.getElementById("pointsDropDown").setAttribute("disabled",true);								
								$('#Question').addClass('ui-state-disabled');
								$("#audioPlayer").hide();
								$("#iframeDiv").show();
								var iframe = $("#iframeAudio");
								/*added for BMT audio player*/
								if(deliveryClientId==2){
									   	$(iframe).attr('src', "audioPlayerBmt.jsp");
										$(iframe).css({'margin-left':'-43px','width':'422px','height':'300px'});
								}else{
									 /*For OAS player*/
										$(iframe).removeAttr('style'); 
										$(iframe).attr('src', "audioPlayer.jsp");
								}
							
								
								}else{
								document.getElementById("itemType").value = "CR";								
								var crResponses =data1.scrContent.cRItemContent.string.length;
								for(var i = 0; i < crResponses; i++){
									if( i == (crResponses-1)){
										linebreak ="";
										document.getElementById("pointsDropDown").removeAttribute("disabled");
										document.getElementById("Question").removeAttribute("disabled");
										$('#Question').removeClass('ui-state-disabled'); 
									}
									var crResponseToShow = data1.scrContent.cRItemContent.string[i];
									if(isObject(crResponseToShow))
										crResponseToShow = "";
									crTextResponse = crTextResponse + crResponseToShow + linebreak;
								  }
								$("#crText").show();
								$("#crText").val(crTextResponse);
								}
								

								
								 populateTableNew();
									// $.unblockUI(); 
								 //$("#rubricDialogID").dialog("open");
								

								 						 						
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								$.unblockUI();  
								window.location.href="/SessionWeb/logout.do";
							},
				complete :  function(){
								//$.unblockUI(); 
							}
				}
			);
			
	
	}
	
	function isObject (obj){
			return obj.toString() === '[object Object]';
	}
	
	
	function handleSpecialCharactersNewUI(s) {
		s= s.replace(/&nbsp;/g,' ').split('');
		var k;
		for(var i= 0; i<s.length; i++){
			k= s[i];
			c= k.charCodeAt(0);
			s[i]= (function(){
				switch(c){
					case 60: return "&lt;";
					case 62: return "&gt;";
					case 34: return "&quot;";
					case 38: return "&amp;";
					case 39: return "&#39;";
					//For IE
					case 65535: {
						if(!((s[i-1].charCodeAt(0)>=65 && s[i-1].charCodeAt(0)<=90) || (s[i-1].charCodeAt(0)>=97 && s[i-1].charCodeAt(0)<=122)) || !((s[i+1].charCodeAt(0)>=65 && s[i+1].charCodeAt(0)<=90) || (s[i+1].charCodeAt(0)>=97 && s[i+1].charCodeAt(0)<=122)))
							return "&quot;";
						else
							return "&#39;";
					}
					//For Mozila and Safari
					case 65533: {
						if(!((s[i-1].charCodeAt(0)>=65 && s[i-1].charCodeAt(0)<=90) || (s[i-1].charCodeAt(0)>=97 && s[i-1].charCodeAt(0)<=122)) || !((s[i+1].charCodeAt(0)>=65 && s[i+1].charCodeAt(0)<=90) || (s[i+1].charCodeAt(0)>=97 && s[i+1].charCodeAt(0)<=122)))
							return "&quot;";
						else
							return "&#39;";
					}
					default: return k;
				}
			})();
		}
		return s.join('');
	}
	
	function hideMessage(){
		clearMessage();
	}
	
	function openConfirmationPopupSubmit(){
		if(isDataExportBySession == 'true'){
			$("#confirmationPopupOnSubmit").dialog({  
					title:$("#onSubmitConfirmID").val(),  
				 	resizable:false,
				 	autoOpen: true,
				 	width: '400px',
				 	modal: true,
				 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
					});	
			 $("#confirmationPopupOnSubmit").css('height',130);
			 var toppos = ($(window).height() - 290) /2 + 'px';
			 var leftpos = ($(window).width() - 410) /2 + 'px';
			 $("#confirmationPopupOnSubmit").parent().css("top",toppos);
			 $("#confirmationPopupOnSubmit").parent().css("left",leftpos);	
		}else{
			submitJobDetails();
		}		 
	}
	
	function refreshStudentList() { 
		var postDataObject = {};		
		postDataObject.frameworkProductId=$('#frameworkProductId').val();
		postDataObject.productId=$('#formSelect').val();
		
		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'getStudentForExport.do',
			type:		'POST',
			dataType:	'json',
			data:		 postDataObject,
			success:	function(data, textStatus, XMLHttpRequest){	
							$.unblockUI(); 
							if(data.testSessionList.length>0){							
								//populate_to_be_export_student_grid(data.testSessionList);
								reload_populate_reset_by_session_step3_student_grid(data.testSessionList);
								//$('#data_export_step1').show();
								notCompletedStudentCountVal = data.notCompletedStudentCount;
								notTakenStudentCountVal = data.notTakenStudentCount;
								scheduledStudentCountVal = data.scheduledStudentCount;
								totalExportedStudentCountVal = data.studentBeingExportCount;
							} else {
								showHideMessage(true,$("#dataExportSearchResultTitle").val(),$("#exportStudentTestSessionSearchResultEmpty").val());
								//$('#data_export_step1').hide();
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

function isWindows() {
		return navigator.userAgent.indexOf('Win') > -1;
	}