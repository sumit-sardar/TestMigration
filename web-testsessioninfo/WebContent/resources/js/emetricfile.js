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
var selectedRData = {};
var selectedItemSetTCVal;
var notCompletedStudentCountVal = 0;
var notTakenStudentCountVal = 0;
var scheduledStudentCountVal = 0;
var totalExportedStudentCountVal = 0;


function getEmetricFileList() { 
		var postDataObject = {};
		
		
		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'getAccountFileList.do',
			type:		'POST',
			dataType:	'json',
			data:		 postDataObject,
			success:	function(data, textStatus, XMLHttpRequest){	
							$.unblockUI(); 
							if(data.accountFileList.length>0){
								populate_to_be_export_student_grid(data.accountFileList);
								$('#data_export_step1').show();								
							} else {
								showHideMessage(true,$("#dataExportSearchResultTitle").val(),$("#exportStudentTestSessionSearchResultEmpty").val());
								$('#data_export_step1').hide();
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

function populate_to_be_export_student_grid(accountFileList){
		if(isGridToBeExportStudentLoaded ){
			reload_populate_to_be_export_student_grid(accountFileList);
		} else {
			load_populate_to_be_export_student_grid(accountFileList);
		}
		window.scroll(0,100000);
		
	}
	
function reload_populate_reset_by_session_step3_student_grid(testSessionList){
      $('#to_be_export_student_list').jqGrid('clearGridData') ;
      jQuery("#to_be_export_student_list").jqGrid('setGridParam',{ datatype: 'local'});    
   	  jQuery("#to_be_export_student_list").jqGrid('setGridParam', {data: accountFileList,page:1}).trigger("reloadGrid");
      jQuery("#to_be_export_student_list").sortGrid('testSessionName',true,'asc');
}
		
	
function load_populate_to_be_export_student_grid(accountFileList){
	
		isGridToBeExportStudentLoaded = true;
		$("#to_be_export_student_list").jqGrid({
      	  data: accountFileList,         
          datatype: 'local',          
          colNames:[  "File Name","Owner Email","Administrator Email", "File Size(KB)", "Created On","Download"],
		   	colModel:[
		   		{name:'fileName',	index:'fileName',	width:250, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'ownerEmail',	index:'ownerEmail',	width:200, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'adminEmail',	index:'adminEmail',	width:200, editable: true, align:"left", sortable:true, sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'fileSize',			index:'fileSize', 			width:100, editable: false, align:"left", sortable:true, sorttype:'date',datefmt: "m/d/Y",search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'updateDate',			index:'updateDate',		 	width:100, editable: false, align:"left", sortable:true,	sorttype:'date',datefmt: "m/d/Y",search: false,	cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'downloadPath',		index:'downloadPath', 		width:75,   editable: false, align:"center", sortable:false,  search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } ,formatter: fmt_downloadfile},
		   		
		   	],
		   		jsonReader: { repeatitems : false, root:"rows", id:"fileName",
		   	records: function(obj) { 
		   	 } },
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			sortname: 'fileName',
			pager: '#to_be_export_student_list_pager', 
			viewrecords: true, 
			sortorder: "asc",
			height: 220,
			hoverrows: true,
			caption:"eMetric Account Files",			
			
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
				
			},gridComplete: function() {
				var allRowsInGridPresent = $('#to_be_export_student_list').jqGrid('getDataIDs');
				for(var k = 0; k < allRowsInGridPresent.length; k++) {
					var selectedRowData = $("#to_be_export_student_list").getRowData(allRowsInGridPresent[k]);
					
				}
			},onSelectRow: function (rowId) {
				$("#"+rowId).removeClass('ui-state-highlight');
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
	
	function fmt_downloadfile(){
		formatted_cellval = "<img src = '../resources/images/download.jpg' onclick='#' title='Click To Download'>";
		return formatted_cellval;
	}
}

