
 function processGridOrg(){
 	UIBlock();
	resetFilters();
	if(!gridloadedStu) {
		gridloadedStu = true;
		populateGridOrg();
	} else {
		var gridOrg = $("#orgImmdRptGrid"); 
		//$("#immdRptGridSearhInputParam").val('');
		if(gridOrg[0] != undefined) {
			 gridOrg[0].p.search = false;
		 }
		gridScoringStudentReloadOrg();
	}
	enableDisableImmediateReportButtonOrg(false);
}

function resetFilters() {
	$("#gs_grade option:eq(0)").attr('selected','Any'); 
	$("#gs_testCatalogName option:eq(0)").attr('selected','Any'); 
	$("#gs_contentAreaString option:eq(0)").attr('selected','Any');
	$("#gs_form option:eq(0)").attr('selected','Any'); 
}

function populateGridOrg() {
	var studentIdTitle = $("#studentIdLabelName").val();
	var postDataObject = {};
 	postDataObject.q = 2;
 	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
 	$("#orgImmdRptGrid").jqGrid({         
       	url:'getAllCompletedStudentForOrgNode.do', 
	 	mtype:   'POST',
	 	postData: postDataObject,
	 	datatype: "json",         
       	colNames:[$("#stuGrdLoginId").val(),$("#stuGrdStdName").val(), $("#grdGroup").val(), studentIdTitle , $("#grdSessionName").val(), $("#grdAdministrationDate").val(), $("#grdCreatedBy").val(),'', ''],
	   	colModel:[
	   		{name:'userName',   	index:'userName', 			width:110, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		{name:'studentName',	index:'studentName',		width:120, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		{name:'orgNameList',	index:'orgNameList',		width:120, editable: true, align:"left",sorttype:'text',search: false,				 cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		//{name:'grade',			index:'grade',				width:70,  editable: true, align:"left",				search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: gradeOptions } },
	   		{name:'studentNumber',	index:'studentNumber', 		width:100,  editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		{name:'testSessionName',index:'testSessionName',	width:140, editable: true, align:"left",				search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		//{name:'testCatalogName',index:'testCatalogName',	width:200, editable: true, align:"left", 				search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: testNameOptions } },
	   		//{name:'contentAreaString',index:'contentAreaString',width:140, editable: true, align:"left",sorttype:'text',search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['cn'], value: contentAreaOptions } },
	   		//{name:'form',	index:'form', width:65, editable: true, align:"left",sorttype:'text',search: true,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: allFormOptions } },
	   		{name:'administrationDate',	index:'administrationDate', width:120, fixed:true, editable: true, align:"left",sorttype:'date',formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'},search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		{name:'defaultScheduler',	index:'defaultScheduler', width:120, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   	    {name:'administrationDateString',	index:'administrationDateString', width:1,   editable: true, align:"left",hidden: true,	search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   	    {name:'testAdminId',	index:'testAdminId', 		width:1,   editable: true, align:"left",hidden: true,	search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
	   	],
	   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"rosterId",
	   	records: function(obj) {} },
	   	loadui: "disable",
		rowNum:20,
		loadonce:true, 
		multiselect:false,
		pager: '#orgImmdRptGridPager', 
		sortname: 'userName', 
		viewrecords: true, 
		sortorder: "asc",
		height: 370,
		width: $("#jqGrid-content-section").width(),
		shrinkToFit: false, 
		caption:$("#imdRptStuListGridCaption").val(),
		hoverrows: false,
		//ondblClickRow: function(rowid) {viewHtmlReport(); $("#displayMessageMain").hide();},
		onPaging: function() {
			var reqestedPage = parseInt($('#orgImmdRptGrid').getGridParam("page"));
			var maxPageSize = parseInt($('#sp_1_immdRptGridPager').text());
			var minPageSize = 1;
			if(reqestedPage > maxPageSize){
				$('#orgImmdRptGrid').setGridParam({"page": maxPageSize});
			}
			if(reqestedPage <= minPageSize){
				$('#orgImmdRptGrid').setGridParam({"page": minPageSize});
			}
			enableDisableImmediateReportButtonOrg(false);
		},
		onSortCol : function(index, columnIndex, sortOrder) { 
				enableDisableImmediateReportButtonOrg(false);
		},
		//onSelectRow: function (rowId) {
			//immdRptSelectedRosterId = rowId;
			//selectedRData = $("#orgImmdRptGrid").getRowData(rowId);
			//immdRptSelectedTestAdminId = selectedRData.testAdminId;
			//enableDisableImmediateReportButtonOrg(true);
			//$("#displayMessageMain").hide();
		//},
		onSelectRow: function(rowid, status) {
   			 $('#'+rowid).removeClass('ui-state-highlight');
			},
		loadComplete: function () {
			if ($('#orgImmdRptGrid').getGridParam('records') === 0) {
           		$('#sp_1_immdRptGridPager').text("1");
           		$('#next_immdRptGridPager').addClass('ui-state-disabled');
           		$('#last_immdRptGridPager').addClass('ui-state-disabled');
           		$('#orgImmdRptGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
		 		$('#orgImmdRptGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/StudentWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsg").val()+"</td></tr></tbody></table></td></tr>");
		 		enableDisableImmediateReportButtonOrg(false);
           	}else {
           		enableDisableImmediateReportButtonOrg(true);
           	}

			var tdList = ("#immdRptGridPager_left table.ui-pg-table  td");
			for(var i=0; i < tdList.length; i++){
				$(tdList).eq(i).attr("tabIndex", i+1);
			}
			//if((prvGrade != $("#gs_grade").val()) || (prvCatalog != $("#gs_testCatalogName").val()) || (prvContentArea != $("#gs_contentAreaString").val()) || (prvForm != $("#gs_form").val())) {
				//enableDisableImmediateReportButtonOrg(false);
			//}
			//prvGrade 		= $("#gs_grade").val();
			//prvCatalog 		= $("#gs_testCatalogName").val();
			//prvContentArea	= $("#gs_contentAreaString").val();
			//prvForm	= $("#gs_form").val();
			//$("#immdRptGrid").setGridWidth($("#jqGrid-content-section").width());
			$.unblockUI(); 
		},
		loadError: function(XMLHttpRequest, textStatus, errorThrown){
			//$.unblockUI();  
			window.location.href="error.do";
		}
	 });  

    jQuery("#orgImmdRptGrid").jqGrid('filterToolbar',{
    	afterSearch : function(){
    		immdRptGridSearh();
    	}});  
    
  	jQuery("#orgImmdRptGrid").navGrid('#orgImmdRptGridPager',{
  			search: false,add:false,edit:false,del:false 	
		}).jqGrid('navButtonAdd',"#orgImmdRptGridPager",{
			caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
				$("#immdRptGridSearhPopup").dialog({  
					title:$("#immdRptTabSearchPopupTitle").val(),  
					resizable:false,
					autoOpen: true,
					width: '300px',
					modal: true,
					closeOnEscape: false,
					open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
				});
			 }, position: "one-before-last", title:"Search Student", cursor: "pointer"
		}).jqGrid('navSeparatorAdd',"#orgImmdRptGridPager",{position: "first"
	});
	jQuery(".ui-icon-refresh").bind("click",function(){
		$("#immdRptGridSearhInputParam").val('');
		enableDisableImmediateReportButtonOrg(false);
	}); 
}

function gridScoringStudentReloadOrg(){
	var postDataObject = {};
	postDataObject.q = 2;
	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
	jQuery("#orgImmdRptGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
	jQuery("#orgImmdRptGrid").jqGrid('setGridParam', {url:'getAllCompletedStudentForOrgNode.do',postData:postDataObject,page:1}).trigger("reloadGrid");
	jQuery("#orgImmdRptGrid").sortGrid('userName',true,'asc');
	$("#displayMessageMain").hide();
}

function enableDisableImmediateReportButtonOrg(enabled){
	
	//document.getElementById('viewReportButton').style.visibility = "visible";
	document.getElementById('generateCSVButtonOrg').style.visibility = "visible";
	document.getElementById('generatePDFButtonOrg').style.visibility = "visible";
	if(enabled){
		//setAnchorButtonState('viewReportButton', false);
		setAnchorButtonState('generateCSVButtonOrg', false);
		setAnchorButtonState('generatePDFButtonOrg', false);
	}else {
		//setAnchorButtonState('viewReportButton', true);
		setAnchorButtonState('generateCSVButtonOrg', true);
		setAnchorButtonState('generatePDFButtonOrg', true);
	}
}


