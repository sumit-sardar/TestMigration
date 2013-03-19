var isBottomTwoLevels = false;
var showSubtestStatus = false;
var selectedOrgNodeIdPS;
var selectedOrgNodeNamePS="";
var subtestStatusAction = "default";
var selectedProgramIdPS;
var selectedTestIdPS = "";
var isPSGridEmpty = true;
var selectedProgramNamePS;
var selectedTestNamePS;
var selectedSubtestIdPS;
var selectedSubtestStatusPS;
var sessionsForTitle;
var testNameOptions=[];
var testIds=[];

function UIBlock(){
	$.blockUI({ message: '<img src="/SessionWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}
function populateProgramStatusTree() {

	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'programStatusOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI(); 
						//$("#programInfo").hide();
						leafNodeCategoryId = data.leafNodeCategoryId;
						orgTreeHierarchy = data;
						jsonData = orgTreeHierarchy.data;
						getRootNodeDetails();
						createSingleNodePSTree(orgTreeHierarchy);
						$("#searchheader").css("visibility","visible");	
						$("#programStatusOrgNode").css("visibility","visible");	
												
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

function createSingleNodePSTree(jsondata) {
	   $("#programStatusOrgNode").jstree({
	        "json_data" : {	             
	            "data" : rootNode,
				"progressive_render" : true,
				"progressive_unload" : true
	        },
            "themes" : {
			    "theme" : "apple",
			    "dots" : false,
			    "icons" : false
			},       
	        "ui" : {  
	           "select_limit" : 1
         	}, 
				"plugins" : [ "themes", "json_data", "ui"]  
				
	    });
	    
	    $("#programStatusOrgNode").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#programStatusOrgNode ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
	    
	    $("#programStatusOrgNode").delegate("a","click", function(e) {
	    
	    	var categoryIdOfSelectedNode = $(this).parent().attr("cid");
	    	if(categoryIdOfSelectedNode == leafNodeCategoryId || categoryIdOfSelectedNode ==(leafNodeCategoryId -1)){
	    		isBottomTwoLevels = true;
	    	}
	    	else{
	    		isBottomTwoLevels = false;
	    	}
	    	selectedOrgNodeIdPS = $(this).parent().attr("id");	
	    	
	    	//selectedOrgNodeNamePS = this.childNodes[1].textContent; 
	    	selectedOrgNodeNamePS = this.lastChild.data; 
	    	
	    	$("#showSaveTestMessage").hide();
	    	$("#subtestStatusInfo").hide();
	    	$("#testStatusInfo").hide();
	    	$("#programInfo").hide();
	    	$("#testStatusTitleID").hide();
	    	$("#clickableSubtestMsg").hide();
	    	//selectedProgramIdPS="";
	    	//selectedTestIdPS="";
	    	selectedProgramNamePS="";
	    	selectedTestNamePS="";
	    	//selectedSubtestIdPS="";
	    	selectedSubtestStatusPS="";
	    	sessionsForTitle="";
	    	testNameOptions=[];
	    	testIds=[];
	    	  
	    	populateProgramStatusDetails();
	    });
	$("#programInfo").hide();
	registerDelegate("programStatusOrgNode");	   
}

function populateProgramStatusDetails(){
	//var programIdPS;
	//var testIdPS;

	selectedProgramIdPS=$('#programIdPS').val();
	//selectedTestIdPS=$('#selectedTestIdPS').val();
	var selectedTestCatalogId = $("#testNameOptions").val();

	var params = "selectedProgramId="+selectedProgramIdPS+"&selectedOrgNodeId="+selectedOrgNodeIdPS+"&selectedOrgNodeName="+selectedOrgNodeNamePS+"&isBottomTwoLevels="+isBottomTwoLevels+"&selectedTestId="+selectedTestIdPS+"&subtestStatusAction="+subtestStatusAction;
			$.ajax(
			{
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'getProgramStatusDetailsForOrgNode.do?',
				type:		'POST',
				dataType:	'json',
				data:		params,
				success:	function(data, textStatus, XMLHttpRequest){	
								var viewSubtestStatus;
								var subtestName;
								var subtestId;
								var isParent;
								var currentStatus;
								
								selectedProgramIdPS = data.selectedProgramId;
								selectedTestIdPS = data.selectedTestId;
								selectedProgramNamePS = data.programName;
								selectedTestNamePS = data.testName;
								var customerNamePS = $("#customerNamePS").val();

								$("#customerNameId").text(customerNamePS);
								$("#programNameId").text(data.programName);
								$("#orgNameId").text(selectedOrgNodeNamePS);
								
								if(data.noTest){
									$("#testNameId").text($("#noTestMsgID").val());
								}
								if(data.singleTest){
									$("#testNameId").text(data.testName);
								}
								if(data.multipleTests){
									for(var i = 0; i < data.testList.length; i++) {
										testNameOptions[i] = data.testList[i].itemSetName;
										testIds[i] = data.testList[i].itemSetId;
									}
									fillTestNameOptions('testNameOptions',testNameOptions);
									$("#testNameOptions").val(selectedTestCatalogId);
									$("#testNameOptions").show();
								}
								
								$("#programInfo").show();
								
								if(!data.noTest){
									$("#testStatusTitleID").text(data.testStatusTitle);
									$("#testStatusTitleID").show();
									//$("#testStatusInfo").show();
								//}
									if(data.hasClickableSubtest){
										$("#clickableSubtestMsg").text($("#clickableSubtestMsgID").val());
										$("#clickableSubtestMsg").show();
									}
	
									var subtestList = data.subtestList;
									
									var html = '<tr class="subtestHeader">';
									html += '<th  height="23" width="40%" align="center" style="text-align:center">'+$("#tblHeaderTitle1").val()+'</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">'+$("#tblHeaderTitle2").val()+'</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">'+$("#tblHeaderTitle3").val()+'</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">'+$("#tblHeaderTitle4").val()+'</th>';								
									html += '</tr>';
									
									html += '<tr class="" id="">';
									html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px;">&nbsp;&nbsp;'+data.testStatus.name+'</td>';
									html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+data.testStatus.scheduled+'</td>';
									html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+data.testStatus.attempted+'</td>';
									html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+data.testStatus.completed+'</td>';
									html += '</tr>';
									
									html += '<tr class="subtestHeader">';
									html += '<th height="23" width="40%" align="center" style="text-align:center">'+$("#tblHeaderTitle5").val()+'</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">&nbsp;</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">&nbsp;</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">&nbsp;</th>';
									html += '</tr>';
									
									var row = "";
									for(var i=0; i<data.subtestList.length; i++) {
										row = data.subtestList[i];
										subtestName = row.name;
										subtestId = row.id;
										isParent = row.isParent;
										
										html += '<tr class="" id="">';
										html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+row.name+'</td>';
										
										if(row.hasScheduledLink){
											/*if(viewSubtestStatus == subtestName && currentStatus == 'Scheduled'){
												html += '<td class="" height="23" width="24" bgcolor="#FFFFFF"><span><font color="red">'+row.scheduled+'</font></span></td>';
											}*/
											if(viewSubtestStatus != subtestName || currentStatus != 'Scheduled'){
												html += '<td class="" style="height:23pt;  bgcolor:#FFFFFF; font-size: 12px; text-align:center; color:#0000FF"><a href="#" style="text-decoration:underline;" onclick="javascript:viewSubtestStatusDetails('+subtestId+',\'Scheduled\',\''+subtestName+'\');">'+row.scheduled+'</a></td>';
											}
										}
										if(!row.hasScheduledLink){
											html += '<td class="" style="height:23pt;  bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+row.scheduled+'</td>';
										}
										
										if(row.hasAttemptedLink){
											/*if(viewSubtestStatus == subtestName && currentStatus == 'Started'){
												html += '<td class="" height="23" width="24" bgcolor="#FFFFFF"><span><font color="red">'+row.scheduled+'</font></span></td>';
											}*/
											if(viewSubtestStatus != subtestName || currentStatus != 'Started'){
												html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center; color:#0000FF"><a href="#" style="text-decoration:underline;" onclick="javascript:viewSubtestStatusDetails('+subtestId+',\'Started\',\''+subtestName+'\');">'+row.attempted+'</a></td>';
											}
										}
										if(!row.hasAttemptedLink){
											html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+row.attempted+'</td>';
										}
										
										if(row.hasCompletedLink){
											/*if(viewSubtestStatus == subtestName && currentStatus == 'Completed'){
												html += '<td class="" height="23" width="24" bgcolor="#FFFFFF" align="center"><span><font color="red">'+row.completed+'</font></span></td>';
											}*/
											if(viewSubtestStatus != subtestName || currentStatus != 'Completed'){
												html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center; color:#0000FF"><a href="#" style="text-decoration:underline;" onclick="javascript:viewSubtestStatusDetails('+subtestId+',\'Completed\',\''+subtestName+'\');">'+row.completed+'</a></td>';
											}
										}
										if(!row.hasCompletedLink){
											html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+row.completed+'</td>';
										}
										
										html += '</tr>';								
									}
									$("#subtestInfoList").html(html);
									$("#testStatusInfo").show();
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
			}
		);
}

//function fillDropDown(elementId, optionList) {
function fillTestNameOptions(elementId, optionList) {
	var selectElement = document.getElementById(elementId);
	var optionHtml = "" ;
	for(var i = 0; i < optionList.length; i++ ) {		     
		optionHtml += "<option  value='"+ testIds[i]+"'>"+ optionList[i]+"</option>";	
	}
	$(selectElement).html(optionHtml);
}

function viewSubtestStatusDetails(subtestId,status,subtestName){
	selectedSubtestIdPS=subtestId;
	selectedSubtestStatusPS = status;
	var sessionsForTitle = status +" students for : "+subtestName;
	UIBlock();
	$("#subtestStatusInfo").hide();
	$('#subtestStatusInfoList').GridUnload();
	resetSearchCrit();
	$("#subtestStatusInfo").show();
	$("#subtestStatusInfoList").jqGrid({   
       	  url:	  'viewSubtestStatus.do?selectedProgramId='+selectedProgramIdPS+'&selectedOrgNodeId='+selectedOrgNodeIdPS+'&selectedTestId='+selectedTestIdPS+'&subtestId='+subtestId+'&status='+status,   
          type:   "POST",
		  datatype: "json", 
		  colNames:[ $("#jqgSessionName").val(),$("#jqgSessionID").val(),$("#jqgLoginID").val(),$("#jqgPassword").val(),$("#jqgAccessCode").val()],
		   	colModel:[
		   		{name:'sessionName',index:'sessionName', width:210, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'sessionNumber',index:'sessionNumber', width:90, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginId',index:'loginId', width:90, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'password',index:'password', width:90, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'accessCode',index:'accessCode', width:90, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   	],

		   	jsonReader: { repeatitems : false, root:"statusList",id:"loginId", records: function(obj) {
		   		 var allowExport = JSON.stringify(obj.allowExport);
		   		 if(allowExport == 'true') {
					$("#exportToExcel").show();
				 } else {
					$("#exportToExcel").hide();
				 }
		   	}},
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			//multiselect:true,
			pager: '#subtestStatusInfoPager', 
			sortname: 'sessionName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 162,
			width: 920,
			caption:sessionsForTitle,
			onPaging: function() {
				var reqestedPage = parseInt($('#subtestStatusInfoList').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_subtestStatusInfoPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#subtestStatusInfoList').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#subtestStatusInfoList').setGridParam({"page": minPageSize});
				}
			},
			gridComplete: function() {
				
       		},
			onSelectAll: function (rowIds, status) {
						
			},
			onSelectRow: function (rowid) {
			},
			loadComplete: function () {
				
				if ($('#subtestStatusInfoList').getGridParam('records') === 0) {
					isPSGridEmpty = true;
            		$('#sp_1_subtestStatusInfoPager').text("1");
            		$('#next_subtestStatusInfoPager').addClass('ui-state-disabled');
            	 	$('#last_subtestStatusInfoPager').addClass('ui-state-disabled');
            	} else {
            		isPSGridEmpty = false;
            	}
            	
            	var tdList = $("#subtestStatusInfoPager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				$.unblockUI();
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
	  jQuery("#subtestStatusInfoList")
	  			.jqGrid('navGrid','#subtestStatusInfoPager',{edit:false,add:false,del:false,search:false,refresh:false})
	  			.jqGrid('navButtonAdd',"#subtestStatusInfoPager",{
				    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
				    	$("#searchGridByKeyword").dialog({  
							title:$("#searchSessionID").val(),  
						 	resizable:false,
						 	autoOpen: true,
						 	width: '300px',
						 	modal: true,
							closeOnEscape: false,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
				    }, position: "first", title:"Search Grid", cursor: "pointer"
				});
}

function searchGridByKeyword(){
	 var searchFiler = $.trim($("#searchGridByKeywordInput").val()), f;
	 var grid = $("#subtestStatusInfoList"); 
	 
	 if (searchFiler.length === 0) {
		 grid[0].p.search = false;
	 }else {
	 	 f = {groupOp:"OR",rules:[]};
		 f.rules.push({field:"sessionName",op:"cn",data:searchFiler});
		 f.rules.push({field:"sessionNumber",op:"cn",data:searchFiler});
		 f.rules.push({field:"loginId",op:"cn",data:searchFiler});
		 f.rules.push({field:"password",op:"cn",data:searchFiler});
		 f.rules.push({field:"accessCode",op:"cn",data:searchFiler});
		 
		 grid[0].p.search = true;
		 grid[0].p.ignoreCase = true;
		 $.extend(grid[0].p.postData,{filters:JSON.stringify(f)});
	 }
	 grid.trigger("reloadGrid",[{page:1,current:true}]); 
	 $("#searchGridByKeyword").dialog("close");
}

function resetSearchCrit(){
	$("#searchGridByKeywordInput").val('');
	var grid = $("#subtestStatusInfoList"); 
	grid.jqGrid('setGridParam',{search:false});	
    var postData = grid.jqGrid('getGridParam','postData');
    $.extend(postData,{filters:""});
}

function resetSearchList(){
	var grid = $("#subtestStatusInfoList"); 
	$("#searchGridByKeywordInput").val('');
	 grid[0].p.search = false;
	 grid.trigger("reloadGrid",[{page:1,current:true}]); 
	 $("#searchGridByKeyword").dialog("close");
}

function trapEnterKey(e){
	var key;
   if(window.event)
        key = window.event.keyCode;     //IE
   else
        key = e.which;     //firefox
        
   if(key == 13){
   		searchGridByKeyword();
   }
}


function exportToExcel(){

	location.href='exportToExcel.do?selectedProgramId='+selectedProgramIdPS+'&selectedOrgNodeId='+selectedOrgNodeIdPS+'&selectedTestId='+selectedTestIdPS+'&subtestId='+selectedSubtestIdPS+'&status='+selectedSubtestStatusPS+'&selectedProgramName='+selectedProgramNamePS+'&selectedOrgNodeName='+selectedOrgNodeNamePS+'&selectedTestName='+selectedTestNamePS;
}

function getSubtestDetailsForSelectedTest(){
	var selectElement = document.getElementById("testNameOptions");
	var chosenOption = selectElement.options[selectElement.selectedIndex];
	selectedTestIdPS = chosenOption.value;
	selectedTestNamePS = chosenOption.text;
	$("#testStatusTitleID").hide();
	$("#clickableSubtestMsg").hide();
	$("#testStatusInfo").hide();
	$("#subtestStatusInfo").hide();
	$('#subtestStatusInfoList').GridUnload();
	
	var params = "selectedProgramId="+selectedProgramIdPS+"&selectedOrgNodeId="+selectedOrgNodeIdPS+"&selectedOrgNodeName="+selectedOrgNodeNamePS+"&isBottomTwoLevels="+isBottomTwoLevels+"&selectedTestId="+selectedTestIdPS;
			$.ajax(
			{
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'getSubtestDetailsByTestId.do?',
				type:		'POST',
				dataType:	'json',
				data:		params,
				success:	function(data, textStatus, XMLHttpRequest){	
								var viewSubtestStatus;
								var subtestName;
								var subtestId;
								var isParent;
								var currentStatus;
								$("#testStatusTitleID").text(data.testStatusTitle);
								$("#testStatusTitleID").show();
								
								if(data.hasClickableSubtest){
									$("#clickableSubtestMsg").text($("#clickableSubtestMsgID").val());
									$("#clickableSubtestMsg").show();
								}

								var subtestList = data.subtestList;
								
								var html = '<tr class="subtestHeader">';
									html += '<th  height="23" width="40%" align="center" style="text-align:center">'+$("#tblHeaderTitle1").val()+'</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">'+$("#tblHeaderTitle2").val()+'</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">'+$("#tblHeaderTitle3").val()+'</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">'+$("#tblHeaderTitle4").val()+'</th>';								
									html += '</tr>';
									
									html += '<tr class="" id="">';
									html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px;">&nbsp;&nbsp;'+data.testStatus.name+'</td>';
									html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+data.testStatus.scheduled+'</td>';
									html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+data.testStatus.attempted+'</td>';
									html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+data.testStatus.completed+'</td>';
									html += '</tr>';
									
									html += '<tr class="subtestHeader">';
									html += '<th height="23" width="40%" align="center" style="text-align:center">'+$("#tblHeaderTitle5").val()+'</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">&nbsp;</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">&nbsp;</th>';
									html += '<th height="23" width="20%" align="center" style="text-align:center">&nbsp;</th>';
									html += '</tr>';
									
									var row = "";
									for(var i=0; i<data.subtestList.length; i++) {
										row = data.subtestList[i];
										subtestName = row.name;
										subtestId = row.id;
										isParent = row.isParent;
										
										html += '<tr class="" id="">';
										html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+row.name+'</td>';
										
										if(row.hasScheduledLink){
											/*if(viewSubtestStatus == subtestName && currentStatus == 'Scheduled'){
												html += '<td class="" height="23" width="24" bgcolor="#FFFFFF"><span><font color="red">'+row.scheduled+'</font></span></td>';
											}*/
											if(viewSubtestStatus != subtestName || currentStatus != 'Scheduled'){
												html += '<td class="" style="height:23pt;  bgcolor:#FFFFFF; font-size: 12px; text-align:center; color:#0000FF"><a href="#" style="text-decoration:underline;" onclick="javascript:viewSubtestStatusDetails('+subtestId+',\'Scheduled\',\''+subtestName+'\');">'+row.scheduled+'</a></td>';
											}
										}
										if(!row.hasScheduledLink){
											html += '<td class="" style="height:23pt;  bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+row.scheduled+'</td>';
										}
										
										if(row.hasAttemptedLink){
											/*if(viewSubtestStatus == subtestName && currentStatus == 'Started'){
												html += '<td class="" height="23" width="24" bgcolor="#FFFFFF"><span><font color="red">'+row.scheduled+'</font></span></td>';
											}*/
											if(viewSubtestStatus != subtestName || currentStatus != 'Started'){
												html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center; color:#0000FF"><a href="#" style="text-decoration:underline;" onclick="javascript:viewSubtestStatusDetails('+subtestId+',\'Started\',\''+subtestName+'\');">'+row.attempted+'</a></td>';
											}
										}
										if(!row.hasAttemptedLink){
											html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+row.attempted+'</td>';
										}
										
										if(row.hasCompletedLink){
											/*if(viewSubtestStatus == subtestName && currentStatus == 'Completed'){
												html += '<td class="" height="23" width="24" bgcolor="#FFFFFF" align="center"><span><font color="red">'+row.completed+'</font></span></td>';
											}*/
											if(viewSubtestStatus != subtestName || currentStatus != 'Completed'){
												html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center; color:#0000FF"><a href="#" style="text-decoration:underline;" onclick="javascript:viewSubtestStatusDetails('+subtestId+',\'Completed\',\''+subtestName+'\');">'+row.completed+'</a></td>';
											}
										}
										if(!row.hasCompletedLink){
											html += '<td class="" style="height:23pt; bgcolor:#FFFFFF; font-size: 12px; text-align:center">'+row.completed+'</td>';
										}
										
										html += '</tr>';								
									}
									$("#subtestInfoList").html(html);
									$("#testStatusInfo").show();
								
							    $.unblockUI(); 																														 						
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								$.unblockUI();
								window.location.href="/SessionWeb/logout.do";
							},
				complete :  function(){
								$.unblockUI();
							}
			}
		);

}