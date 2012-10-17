var isGridToBeExportStudentLoaded = false;
var isUnscoredStudentListLoaded = false;
var toBeExportStudentRosterList = new Array();
var testAdminId = null;
var testRosterId = null;
var itemSetIdTC =  null;




function getStudentList() { 
		var postDataObject = {};
		
		
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
								load_populate_to_be_export_student_grid(data.testSessionList);
							} else {
								showHideMessage(true,$("#dataExportSearchResultTitle").val(),$("#exportStudentTestSessionSearchResultEmpty").val());
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

	
function load_populate_to_be_export_student_grid(testSessionList){
	
		isGridToBeExportStudentLoaded = true;
		$("#to_be_export_student_list").jqGrid({
      	  data: testSessionList,         
          datatype: 'local',          
          colNames:[  $("#titleSessionName").val(), $("#titleStartDate").val(), $("#titleEndDate").val(), $("#titleToBeExport").val(), $("#titleCompleted").val(), $("#titleScheduled").val(), $("#titleStudentStop").val(), $("#titleSystemStop").val(), $("#titleNotTaken").val(), $("#titleIncompleted").val(),''],
		   	colModel:[
		   		{name:'testSessionName',	index:'testSessionName',	width:350, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'startDate',			index:'startDate', 			width:100, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'endDate',			index:'endDate',		 	width:100, editable: true, align:"left", sortable:true,	sorttype:'text',search: false,	cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'toBeExported',		index:'toBeExported',		width:100,   editable: false,align:"left", sortable:true, sorttype:'text', search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'complete',			index:'complete', 			width:100,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'scheduled',			index:'scheduled', 			width:100,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentStop',		index:'studentStop', 		width:100,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'systemStop',			index:'systemStop', 		width:100,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'notTaken',			index:'notTaken', 			width:100,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'incomplete',			index:'incomplete', 		width:100,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testAdminId',		index:'testAdminId', 		width:1,   editable: false, align:"left", sortable:false, hidden:true,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		
		   		
		   		
		   	],
		   		jsonReader: { repeatitems : false, root:"rows", id:"testAdminId",
		   	records: function(obj) { 
		   	 } },
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			sortname: 'studentName',
			pager: '#to_be_export_student_list_pager', 
			viewrecords: true, 
			sortorder: "asc",
			height: 165,
			width : $("#jqGrid-content-section").width(),
			caption: $("#captionToBeExportGrid").val(),
			onPaging: function() {
				var reqestedPage = parseInt($('#to_be_export_student_list').getGridParam("page"));
				var maxPageSize = parseInt($('#to_be_export_student_list_pager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#to_be_export_student_list').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#to_be_export_student_list').setGridParam({"page": minPageSize});
				}
				
			},gridComplete: function() {
				var allRowsInGridPresent = $('#to_be_export_student_list').jqGrid('getDataIDs');
				for(var k = 0; k < allRowsInGridPresent.length; k++) {
					var selectedRowData = $("#to_be_export_student_list").getRowData(allRowsInGridPresent[k]);
					
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
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
	
	
	}

function getUnscoredStudentDetails() {
	var postDataObject = {};
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
							$("#studentBeingExptd").text(data.studentBeingExportCount);
							$("#incScoredStudent").text(data.unscoredStudentCount);
							if(data.studentList.length>0) {
								load_populate_unscored_student_grid(data.studentList);
								$("#data_export_step2").show();
								window.scroll(0,100000);
							}
							else {
								showHideMessage(true,$("#dataExportSearchResultTitle").val(),$("#exportUnscoredStudentSearchResultEmpty").val());
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
	
function load_populate_unscored_student_grid(studentList) {
		isUnscoredStudentListLoaded = true;
		$("#data_export_scoring_incomplete_student_list").jqGrid({
      	  data: studentList,         
          datatype: 'local',          
          colNames:[  $("#titleLoginId").val(), $("#titleStudentName").val(), $("#titleSessionName").val(), $("#titleGrade").val(), $("#titleStudentId").val(), $("#titleStatus").val(),'','',''],
		   	colModel:[
		   		{name:'loginId',		index:'loginId',		width:350, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } ,formatter:updateField},
		   		{name:'studentName',	index:'studentName', 	width:150, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testSessionName',index:'testSessionName',width:150, editable: true, align:"left", sortable:true,	sorttype:'text',search: false,	cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',			index:'grade',			width:0,   editable: false,align:"left", sortable:true, sorttype:'text', search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentIdNumber',index:'studentIdNumber',width:0,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'scoringStatus',	index:'scheduled', 		width:0,   editable: false, align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testAdminId',	index:'testAdminId', 	width:0,   editable: false, hidden:true,  align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'rosterId',	index:'rosterId', 			width:0,   editable: false, hidden:true,  align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetIdTC',	index:'itemSetIdTC', 	width:0,   editable: false, hidden:true,  align:"left", sortable:false,	 search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   		
		   	],
		   		jsonReader: { repeatitems : false, root:"rows", id:"loginId",
		   	records: function(obj) { 
		   	 } },
		   	loadui: "disable",
			rowNum:5,
			loadonce:true, 
			sortname: 'studentName',
			pager: '#data_export_scoring_incomplete_student_list_pager', 
			viewrecords: true, 
			sortorder: "asc",
			height: 165,
			width : $("#jqGrid-content-section").width(),
			caption : '',
			onPaging: function() {
				var reqestedPage = parseInt($('#data_export_scoring_incomplete_student_list').getGridParam("page"));
				var maxPageSize = parseInt($('#data_export_scoring_incomplete_student_list_pager').text());
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
			},loadComplete: function () {
				if ($('#data_export_scoring_incomplete_student_list').getGridParam('records') === 0) {
            		$('#sp_1_data_export_scoring_incomplete_student_list_pager').text("1");
            		$('#next_data_export_scoring_incomplete_student_list_pager').addClass('ui-state-disabled');
            	 	$('#last_data_export_scoring_incomplete_student_list_pager').addClass('ui-state-disabled');
            		$('#data_export_scoring_incomplete_student_list').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#data_export_scoring_incomplete_student_list').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/ExportWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitleGrd").val()+"</th></tr><tr width='100%'><td colspan='6'>"+"No records found"+"</td></tr></tbody></table></td></tr>"); 	
            	} 
            	$.unblockUI();
           /* 	var myGrid = $("#data_export_scoring_incomplete_student_list");
            	var ids = myGrid.getDataIDs();
            	for (var i = 0, idCount = ids.length; i < idCount; i++) {
        				$("#"+ids[i]+" a",myGrid[0]).click(function(e) {
           				var hash=e.currentTarget.hash;// string like "#?id=0"
            			if (hash.substring(0,5) === '#?id=') {
                			var id = hash.substring(5,hash.length);
                			var text = this.textContent;
                			location.href="/HandScoringWeb/scorebystudent/beginDisplayStudItemList.do"
            			}
            			e.preventDefault();
        				});
				}  */
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
	
	function updateField (cellvalue, options, rowObject){
		var updatedField = "<a> Hi Sid</a>";
	
	   return updatedField;
	
	}

	
