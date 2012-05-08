var leafNodeCategoryId;
var jsonData;
var stuThreshold = 10000;
var gridloadedStu = false;
var gradeOptions = ":Any";
var testNameOptions = ":Any";
var contentAreaOptions = ":Any";
var immdRptSelectedRosterId;
var immdRptSelectedTestAdminId;
var prvGrade 		= "";
var prvCatalog 		= "";
var prvContentArea	= "";


function populateUserOrgHierarchy() {
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'getOrgNodeHierarchy.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI(); 
						leafNodeCategoryId = data.leafNodeCategoryId;
						jsonData = data.data;
						getRootNodeDetails();
						createSingleNodeScoringTree("imdRptOrgTree", data);
						populateDropDowns();
						$("#imdRptOrgTreeHeader").css("visibility","visible");	
						$("#imdRptOrgTree").css("visibility","visible");	
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

 function processStudentTable() {
 	if(parseInt(rootNodeId) == parseInt(SelectedOrgNodeId)){
		var postDataObject = {};
		postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
		$.ajax({
			async:		true,
			beforeSend:	function(){	UIBlock();	},
			url:		'getStudentCountForOrgNode.do', 
			type:		'POST',
			data:		 postDataObject,
			dataType:	'json',
			success:	function(data, textStatus, XMLHttpRequest){	
							$.unblockUI();
							topOrgNodeStuCount = data;
							if(parseInt(topOrgNodeStuCount) > stuThreshold){
								showPopup(topOrgNodeStuCount);
							}else{
								processGrid();
							}
						},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();  
							window.location.href="/SessionWeb/logout.do";
						}
			});		
	}else{
	    processGrid();
	} 
 }
  
 function processGrid(){
 	UIBlock();
	resetFilters();
	if(!gridloadedStu) {
		gridloadedStu = true;
		populateGrid();
	} else {
		var grid = $("#immdRptGrid"); 
		$("#immdRptGridSearhInputParam").val('');
		if(grid[0] != undefined && grid[0].p != undefined) {
			 grid[0].p.search = false;
		 }
		gridScoringStudentReload();
	}
	enableDisableImmediateReportButton(false);
}

function resetFilters() {
	$("#gs_grade").val("Any");
	$("#gs_testCatalogName").val("Any");
	$("#gs_contentAreaString").val("Any");
}

function populateGrid() {
	var studentIdTitle = $("#studentIdLabelName").val();
	var postDataObject = {};
 	postDataObject.q = 2;
 	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
 	$("#immdRptGrid").jqGrid({         
       	url:'getAllCompletedStudentForOrgNode.do', 
	 	mtype:   'POST',
	 	postData: postDataObject,
	 	datatype: "json",         
       	colNames:[$("#stuGrdLoginId").val(),$("#stuGrdStdName").val(), $("#grdGroup").val(), $("#stuGrdGrade").val(), studentIdTitle, $("#grdSessionName").val(), $("#grdTestName").val(), $("#contentArea").val(), ''],
	   	colModel:[
	   		{name:'userName',   	index:'userName', 			width:120, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		{name:'studentName',	index:'studentName',		width:100, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		{name:'orgNameList',	index:'orgNameList',		width:100, editable: true, align:"left",sorttype:'text',search: false,				 cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		{name:'grade',			index:'grade',				width:50,  editable: true, align:"left",				search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: gradeOptions } },
	   		{name:'studentNumber',	index:'studentNumber', 		width:80,  editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		{name:'testSessionName',index:'testSessionName',	width:170, editable: true, align:"left",				search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
	   		{name:'testCatalogName',index:'testCatalogName',	width:200, editable: true, align:"left", 				search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: testNameOptions } },
	   		{name:'contentAreaString',index:'contentAreaString',width:140, editable: true, align:"left",sorttype:'text',search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['cn'], value: contentAreaOptions } },
	   	    {name:'testAdminId',	index:'testAdminId', 		width:0,   editable: true, align:"left",hidden: true,	search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
	   	],
	   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"rosterId",
	   	records: function(obj) {} },
	   	loadui: "disable",
		rowNum:20,
		loadonce:true, 
		multiselect:false,
		pager: '#immdRptGridPager', 
		sortname: 'studentNumber', 
		viewrecords: true, 
		sortorder: "asc",
		height: 370,
		width: 958,
		caption:$("#imdRptStuListGridCaption").val(),
		onPaging: function() {
			var reqestedPage = parseInt($('#immdRptGrid').getGridParam("page"));
			var maxPageSize = parseInt($('#sp_1_immdRptGridPager').text());
			var minPageSize = 1;
			if(reqestedPage > maxPageSize){
				$('#immdRptGrid').setGridParam({"page": maxPageSize});
			}
			if(reqestedPage <= minPageSize){
				$('#immdRptGrid').setGridParam({"page": minPageSize});
			}
			enableDisableImmediateReportButton(false);
		},
		onSortCol : function(index, columnIndex, sortOrder) { 
				enableDisableImmediateReportButton(false);
		},
		onSelectRow: function (rowId) {
			immdRptSelectedRosterId = rowId;
			selectedRData = $("#immdRptGrid").getRowData(rowId);
			immdRptSelectedTestAdminId = selectedRData.testAdminId;
			enableDisableImmediateReportButton(true);
		},
		loadComplete: function () {
			if ($('#immdRptGrid').getGridParam('records') === 0) {
           		$('#sp_1_immdRptGridPager').text("1");
           		$('#next_immdRptGridPager').addClass('ui-state-disabled');
           		$('#last_immdRptGridPager').addClass('ui-state-disabled');
           		$('#immdRptGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
		 		$('#immdRptGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/StudentWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsg").val()+"</td></tr></tbody></table></td></tr>");
           	}

			var tdList = ("#immdRptGridPager_left table.ui-pg-table  td");
			for(var i=0; i < tdList.length; i++){
				$(tdList).eq(i).attr("tabIndex", i+1);
			}
			if((prvGrade != $("#gs_grade").val()) || (prvCatalog != $("#gs_testCatalogName").val()) || (prvContentArea != $("#gs_contentAreaString").val())) {
				enableDisableImmediateReportButton(false);
			}
			prvGrade 		= $("#gs_grade").val();
			prvCatalog 		= $("#gs_testCatalogName").val();
			prvContentArea	= $("#gs_contentAreaString").val();
			//$("#immdRptGrid").setGridWidth($("#jqGrid-content-section").width());
			$.unblockUI(); 
		},
		loadError: function(XMLHttpRequest, textStatus, errorThrown){
			$.unblockUI();  
			window.location.href="/SessionWeb/logout.do";
		}
	 });  

    jQuery("#immdRptGrid").jqGrid('filterToolbar',{
    	afterSearch : function(){
    		immdRptGridSearh();
    	}});  
    
  	jQuery("#immdRptGrid").navGrid('#immdRptGridPager',{
  			search: false,add:false,edit:false,del:false 	
		}).jqGrid('navButtonAdd',"#immdRptGridPager",{
			caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
				$("#immdRptGridSearhPopup").dialog({  
					title:$("#searchStudentSession").val(),  
					resizable:false,
					autoOpen: true,
					width: '300px',
					modal: true,
					closeOnEscape: false,
					open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
				});
			 }, position: "one-before-last", title:"Search Student", cursor: "pointer"
		}).jqGrid('navSeparatorAdd',"#immdRptGridPager",{position: "first"
	});
	jQuery(".ui-icon-refresh").bind("click",function(){
		$("#immdRptGridSearhInputParam").val('');
		enableDisableImmediateReportButton(false);
	}); 
}

function gridScoringStudentReload(){
	var postDataObject = {};
	postDataObject.q = 2;
	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
	jQuery("#immdRptGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
	jQuery("#immdRptGrid").jqGrid('setGridParam', {url:'getAllCompletedStudentForOrgNode.do',postData:postDataObject,page:1}).trigger("reloadGrid");
	jQuery("#immdRptGrid").sortGrid('studentNumber',true,'asc');
}

function enableDisableImmediateReportButton(enabled){
	
	document.getElementById('viewReportButton').style.visibility = "visible";
	document.getElementById('generateCSVButton').style.visibility = "visible";
	document.getElementById('generatePDFButton').style.visibility = "visible";
	if(enabled){
		setAnchorButtonState('viewReportButton', false);
		setAnchorButtonState('generateCSVButton', false);
		setAnchorButtonState('generatePDFButton', false);
	}else {
		setAnchorButtonState('viewReportButton', true);
		setAnchorButtonState('generateCSVButton', true);
		setAnchorButtonState('generatePDFButton', true);
	}
}

function showPopup(stuCount){
	var msg = $("#stuCountId").val();
	stuCount = addCommas(stuCount);//to add commas at 1000th places
	var countMsg = msg.replace("XXXX",stuCount);
	
	$("#stuThresholdExceedPopup").dialog({  
			title:'Alert',  
			resizable:false,
			autoOpen: true,
			modal: true,
			closeOnEscape: false,
			open: function(event, ui) {
				$("#stuThresholdExceedMsg").text(countMsg);
				$(".ui-dialog-titlebar-close").hide(); 
			}
	});
}

function populateDropDowns() {
	var postDataObject = {};
	$.ajax({
		async:		true,
		beforeSend:	function(){	UIBlock();	},
		url:		'populateGridDropDowns.do', 
		type:		'POST',
		data:		 postDataObject,
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){
							$.unblockUI();
							if(data != undefined) {
								if(data.gradeOptions != undefined) {
									var gOptionsArr = data.gradeOptions;
									var gOptions = ":Any;";
									for(var i=0; i<gOptionsArr.length; i++){
										gOptions = gOptions+gOptionsArr[i] +":"+gOptionsArr[i]+";";
									}
				 					if(gOptions != ""){
				 						gradeOptions = gOptions.substring(0,gOptions.length-1);
				 					}
								}
								if(data.testCatalogOptions != undefined) {
									var testOptionsArr = data.testCatalogOptions;
									var testOptions = ":Any;";
									for(var i=0; i<testOptionsArr.length; i++){
										testOptions = testOptions+testOptionsArr[i] +":"+testOptionsArr[i]+";";
									}
						 			if(testOptions != ""){
						 				testNameOptions = testOptions.substring(0,testOptions.length-1);
						 			}
								}
								if(data.contentAreaOptions != undefined) {
									var contentAreaOptionsArr = data.contentAreaOptions;
									var contentOptions = ":Any;";
									for(var i=0; i<contentAreaOptionsArr.length; i++){
										contentOptions = contentOptions+contentAreaOptionsArr[i] +":"+contentAreaOptionsArr[i]+";";
									}
						 			if(contentOptions != ""){
						 				contentAreaOptions = contentOptions.substring(0,contentOptions.length-1);
						 			}
								}
							}
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
					}
	});		
}

function immdRptGridresetSearch(){
	var grid = $("#immdRptGrid"); 
	$("#immdRptGridSearhInputParam").val('');
	 //grid[0].p.search = false;
	 var g = {groupOp:"AND",rules:[],groups:[]};
 	 g.rules.push({field:"grade",op:"bw",data:$("#gs_grade").val()});
	 g.rules.push({field:"testCatalogName",op:"bw",data:$("#gs_testCatalogName").val()});
	 g.rules.push({field:"contentAreaString",op:"cn",data:$("#gs_contentAreaString").val()});
	 grid[0].p.search = true;
	 grid[0].p.ignoreCase = true;			 
	 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
	 grid.trigger("reloadGrid",[{page:1,current:true}]); 
	 closePopUp('immdRptGridSearhPopup');
	 //grid[0].triggerToolbar();
	 $("#immdRptGrid").resetSelection(); 
	 enableDisableImmediateReportButton(false);
}

function immdRptGridSearh(){
		 var searchFiler = $.trim($("#immdRptGridSearhInputParam").val()), f;
		 var grid = $("#immdRptGrid"); 
		 if (searchFiler.length === 0) {
			 //grid[0].p.search = false;
			 //grid[0].triggerToolbar();
			 var g = {groupOp:"AND",rules:[],groups:[]};
		 	 g.rules.push({field:"grade",op:"bw",data:$("#gs_grade").val()});
			 g.rules.push({field:"testCatalogName",op:"bw",data:$("#gs_testCatalogName").val()});
			 g.rules.push({field:"contentAreaString",op:"cn",data:$("#gs_contentAreaString").val()});
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;			 
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
		 }else {
		 	 var g = {groupOp:"AND",rules:[],groups:[]};
		 	 g.rules.push({field:"grade",op:"bw",data:$("#gs_grade").val()});
			 g.rules.push({field:"testCatalogName",op:"bw",data:$("#gs_testCatalogName").val()});
			 g.rules.push({field:"contentAreaString",op:"cn",data:$("#gs_contentAreaString").val()});
		 	 f = {groupOp:"OR",rules:[]};
			 f.rules.push({field:"userName",op:"cn",data:searchFiler});
			 f.rules.push({field:"studentName",op:"cn",data:searchFiler});
			 f.rules.push({field:"orgNodeNamesStr",op:"cn",data:searchFiler});
			 f.rules.push({field:"grade",op:"cn",data:searchFiler});
			 f.rules.push({field:"studentNumber",op:"cn",data:searchFiler});
			 f.rules.push({field:"testSessionName",op:"cn",data:searchFiler}); 
			 f.rules.push({field:"testCatalogName",op:"cn",data:searchFiler});
			 f.rules.push({field:"contentAreaString",op:"cn",data:searchFiler}); 
			 g.groups.push(f);   
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
		 }
		grid.trigger("reloadGrid",[{page:1,current:true}]);
		$("#immdRptGrid").resetSelection(); 
		enableDisableImmediateReportButton(false);
		closePopUp('immdRptGridSearhPopup');
}
	
function trapEnterKey(e){
	var key;
	if(window.event) {//IE
		key = window.event.keyCode;     
	} else {
		key = e.which;     
	}
	if(key == 13){
		immdRptGridSearh();
	}
}

function viewHtmlReport(element){
	if (isButtonDisabled(element)) 
		return true;
	resetViewReportPopupData();
	getAndPopulateStudentScoreDetails();
}

function showHtmlReportPopup(){
	$("#immdRptScorePopup").dialog( {
	        title: "Immediate Report",
	        resizable: false,
	        autoOpen: true,
	        width: '740px',
	        modal: true,
	        open: function(event, ui) {
	            $(".ui-dialog-titlebar-close").hide();
	        }
	    });
	var toppos = (($(window).height() - 530) / 2) + 50 + 'px';
	var leftpos = (($(window).width() - 740) / 2) + 'px';
	$("#immdRptScorePopup").parent().css("top", toppos);
	$("#immdRptScorePopup").parent().css("left", leftpos);
}

function getAndPopulateStudentScoreDetails(){
	var postDataObject = {};
	postDataObject.rosterId   = immdRptSelectedRosterId;
	postDataObject.testAdminId= immdRptSelectedTestAdminId;
	$.ajax({
		async:		true,
		beforeSend:	function(){	UIBlock();	},
		url:		'getStudentScoreDetails.do', 
		type:		'POST',
		data:		 postDataObject,
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){
					populateReportHeader(data);	
					populateReportTable(data);	
					showHtmlReportPopup();
					$.unblockUI();		
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
					}
	});
}

function populateReportHeader(data){
	if(data.studentName != undefined) 	$("#stdNameVal").text(data.studentName); else $("#stdNameVal").text("");
	if(data.studentExtPin1 != undefined) 	$("#idVal").text(data.studentExtPin1); else $("#idVal").text("");
	if(data.testAdminStartDateString != undefined) 	$("#testDateVal").text(data.testAdminStartDateString); else $("#testDateVal").text("");
	if(data.form != undefined) 			$("#formVal").text(data.form); else $("#formVal").text("");
	if(data.district != undefined) 		$("#districtVal").text(data.district); else $("#districtVal").text("");
	if(data.school != undefined) 		$("#schoolVal").text(data.school); else $("#schoolVal").text("");
	if(data.grade != undefined) 		$("#gradeVal").text(data.grade); else $("#gradeVal").text("");
	if(data.testAdminName != undefined) $("#testNameVal").text(data.testAdminName);  else $("#testNameVal").text("");
}	

function populateReportTable(data){ 
	var scoreReportTable = getScoreReportTableHeader();
   	if(data.studentReportIrsScore !=undefined && data.studentReportIrsScore.length>0){
   		for(var ii=0, jj=data.studentReportIrsScore.length; ii<jj; ii++ ){
   			scoreReportTable += populateReportTableRow(data.studentReportIrsScore[ii]);
   		}
   	}
   $("#immdRptScoreTable").html(scoreReportTable);
}

 function getScoreReportTableHeader (){
 	var table = '<table > <tr>	<th width="150px" style="border-style: none !important;">&nbsp;</th>'
 	+ '<th class="sortable alignCenter" width="120px"> '+ $("#immdRptTabHeaderRawScore").val()+' </th>'
 	+' <th class="sortable alignCenter" width="120px">'	+ $("#immdRptTabHeaderScaleScore").val()+' </th>' 
 	+'<th class="sortable alignCenter" width="150px">' 	+ $("#immdRptTabHeaderProficiencyLevel").val()+' </th> </tr>';
  	return table;
 }
 
 function populateReportTableRow(rowdata) {
    var tr = '<tr ' ;
    if(rowdata.contentAreaName != undefined && (rowdata.contentAreaName =='Comprehension'  || rowdata.contentAreaName =='Comprensión' || rowdata.contentAreaName =='Oral' || rowdata.contentAreaName=='Overall')){
    	tr += ' bgcolor="#C3D599" >';
    } else { tr += ' >'; }
    tr +='<td align="left" class="transparent" style="border-style: solid !important; border-width: thin !important; ">'+ rowdata.contentAreaName + '</td>';
    tr +='<td align="left" class="transparent" style="border-style: solid !important; border-width: thin !important; ">'+ rowdata.rawScore + '</td>';
    tr +='<td align="left" class="transparent" style="border-style: solid !important; border-width: thin !important;">'+ rowdata.scaleScore + '</td>';
	tr +='<td align="left" class="transparent" style="border-style: solid !important; border-width: thin !important;">'+ rowdata.proficiencyLevel + '</td>';
 	tr += '</tr>';
 	return tr;
 }

 function downloadImmediatePDFReport(element) {
	if (isButtonDisabled(element)) 
		return true;
	$("#rosterId").val(immdRptSelectedRosterId);
    $("#testAdminId").val(immdRptSelectedTestAdminId);
    var element = document.getElementById("downloadImmediateReport");
    element.form.action = "studentsImmediateScoreReportInPDF.do";
    element.form.submit();
	return false;
 }
 
 function downloadImmediateCSVReport(element) {
 	if (isButtonDisabled(element)) 
		return true;
    $("#rosterId").val(immdRptSelectedRosterId);
    $("#testAdminId").val(immdRptSelectedTestAdminId);
    var element = document.getElementById("downloadImmediateReport");
    element.form.action = "studentsImmediateScoreReportInCSV.do";
    element.form.submit();
	return false;
 }

 function resetViewReportPopupData(){
 	$("#stdNameVal").text("");
 	$("#idVal").text("");
 	$("#testDateVal").text("");
 	$("#formVal").text("");
 	$("#districtVal").text("");
 	$("#schoolVal").text("");
 	$("#gradeVal").text("");
 	$("#testNameVal").text("");
 	 $("#immdRptScoreTable").html("");
 }
 
 /************************ COMMON METHOD *************************************/
function UIBlock(){
	$.blockUI({ message: '<img src="/ImmediateReportingWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}

function closePopUp(dailogId){
	$("#"+dailogId).dialog("close");
} 
 /************************ COMMON METHOD *************************************/