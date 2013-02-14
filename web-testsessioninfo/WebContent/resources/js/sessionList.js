var leafNodeCategoryId;
var leafNodeCategoryName = "Organization";
var orgTreeHierarchy;
var gridloaded = false;
var stuGridloaded = false;
var sessionListCUFU;
var sessionListPA;
var isGridEmpty = true;
var isPAGridEmpty = true;
var isStuGridEmpty = true;
var isTreePopulated = false;
var openTreeRequested = false;
var isTabeProduct = false;
var ishomepageload = false;
var loadSessionGrid = false;
var isTestGroupLoad = false;
var testGridLoaded = false;
var subtestGridLoaded = false;
var isTestGridEmpty = true;
var subtestLength = 0;
var subtestDataArr;
var isTestSelected = false;
var currentTime = new Date();
var month = currentTime.getMonth() + 1;
var day = currentTime.getDate();
var dayNext = currentTime.getDate() + 1;
var year = currentTime.getFullYear();
var currDate = month + "/" + day + "/" + year;
var nextDate = month + "/" + dayNext + "/" + year;
var ProductData;
var isTestBreak = false;
var requiredFields = "";
var minutes0 = 0;
var hours0 = 0;
var minutes1 = 0;
var hours1 = 0;
var isTabeLocatorProduct = false;
var isTabeAdaptiveProduct = false;
var noTestExist = false;
var isFirstAccordSelected = true;
var isSecondAccordSelected = false;
var isThirdAccordSelected = false;
var isFourthAccordSelected = false;

var proctorGridloaded = false;
var isProctorGridEmpty = true;
var noOfProctorAdded = 0;

var blockOffGradeTesting = false;
var selectedLevel;
var dropListToDisplay;

var selectAllForDelete = false;
var selectAllForDeleteProctor = false;
var allLocalProctorIds = [];
var allProctorSelected = false;
var proctorSelectedLength = 1;

var previousValue = "";
var previousSubtest = "";
var testJSONValue = "";
var selectedTestId = "";
var selectedSubtestId = "";
var onloadProctorListGrid = false;

var currentNodeId ;
var currentCategoryLevel;
var currentTreeArray;
var currentIndex;
var dataObj2=[];
var dataObj3=[];
var dataObj4;
var dataObj5;
var dataObj6;
var dataObj7;
var dataObj8;
var rootMap = {};
var jsonData;
var map = new Map();
var rootNode = [];
var type;
var asyncOver = 0;
var leafParentOrgNodeId = "";


var selectedTestRosterId = "";
var gridSelectedToDelete = "";
var offGradeSubtestChanged = false;
var offGradeCancled = false;
var testSessionListRequired;
var orgSelectVar = false;
var mySessionCliked = false;
var isPopUp = false;
var isPrintTicket = false;
var accomodationMap={};
var accomodationMapExisting={};
var firstTimeOpen = false;
var invalidationReasonList = [];
var invalidationReasonIDList = [];
var invalidationReasonCodeDetails = [];
var selectProductId = null;

var isOKAdmin = false;
var isOkCustomer = false;
var isOKAdminCord = false;
var isOKEqTestSelected = false;
var isOKProductChanged = false;
var isOKEQDefaultSelected = false; // Needed to verify whether the default selection in select test dropdown is EQ test or not.
var isOKEQActionPerformed = false; // As per new requirement, proctors will be added only the first time EQ test is selected.
var hideProctorDelButton = false; // Only state level admin for oklahoma should be able to add/del proctors
var hideStudentDelButton = false; // Only state level admin, admin coordinator, coordinator should be able to add/del students

var forceTestBreak = false;
var selectGE = null;

var isLasLinksProduct = false; // add for laslink modification
var isLaslinkCustomer = false;
var isPagesizeLabelpopulated = false;
var isForRefreshRoster = false;
var pageSizeSelected = 10;
var scrollPosition=0;

var assignedFormList = [];
var hasAssignFormConfig = false;
var rosterFormMapOld = new Map();
var selectedForm = "";
var updateSuccess = false;
var testStatusMap = new Map();
var testStatus = "";

$(document).bind('keydown', function(event) {		
	      var code = (event.keyCode ? event.keyCode : event.which);
	      if(code == 27){
	      		if(isPopUp){				
	      			onCloseScheduleSessionPopUp();
	      		}
	            return false;
	      }
	  });

function UIBlock(){
	$.blockUI({ message: '<img src="/SessionWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}


function populateSessionListGrid(homePageLoad) {
		ishomepageload  = homePageLoad;
 		UIBlock();
 		resetSearchCrit();
 		reset();
 		$("#list2").jqGrid({         
          url: 'getSessionForUserHomeGrid.do', 
		 mtype:   'POST',
		 datatype: "json",         
          colNames:[$("#sessionName").val(),$("#testName").val(), $("#organization").val(), $("#myRole").val(),$("#startDateGrid").val(), $("#endDateGrid").val(),'','','','','','',''],
		   	colModel:[
		   		{name:'testAdminName',index:'testAdminName', width:250, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'testName',index:'testName', width:225, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'creatorOrgNodeName',index:'creatorOrgNodeName', width:175, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'AssignedRole',index:'AssignedRole',editable: true, width:100, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'loginStartDate',index:'loginStartDate', width:175, editable: true, align:"left", sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'}, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'loginEndDate',index:'loginEndDate', width:175, editable: true, align:"left",sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'}, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'loginStartDateString',index:'loginStartDateString', width:0,editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'loginEndDateString',index:'loginEndDateString', width:0,editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'creatorOrgNodeId',index:'creatorOrgNodeId', width:0, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'isSTabeProduct',index:'isSTabeProduct', width:0,editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'isSTabeAdaptiveProduct',index:'isSTabeAdaptiveProduct', width:0,editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'productType',index:'productType', width:0,editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'copyable',index:'copyable', width:0,editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"testSessionCUFU", id:"testAdminId",
			   	records: function(obj) { 
			   	 sessionListCUFU = obj.sessionListCUFUMap;
			   	 $("#list2").jqGrid("hideCol","creatorOrgNodeId");
			   	 $("#list2").jqGrid("hideCol","loginStartDateString");
			   	 $("#list2").jqGrid("hideCol","loginEndDateString");
			   	 $("#list2").jqGrid("hideCol","isSTabeProduct");
			   	 $("#list2").jqGrid("hideCol","isSTabeAdaptiveProduct");
			   	 $("#list2").jqGrid("hideCol","productType");
			   	 $("#list2").jqGrid("hideCol","copyable");
				   	 if(ishomepageload && obj.orgNodeCategory != undefined){
				   	 	leafNodeCategoryId = obj.orgNodeCategory.categoryLevel;
				   	 	leafNodeCategoryName = obj.orgNodeCategory.categoryName;
				   	 }
			   	 }
		   	 },
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'loginEndDate', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,  
			editurl: 'getSessionForUserHomeGrid.do',
			caption: $("#sessionList").val(),
			ondblClickRow: function(rowid) {
				setSelectedTestAdminId(rowid);
				editTestSession();
			},
			onPaging: function() {
				var reqestedPage = parseInt($('#list2').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_pager2').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#list2').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#list2').setGridParam({"page": minPageSize});
				}				
				setAnchorButtonState('viewStatusButton', true);
				setAnchorButtonState('printTicketButton', true);
 				setAnchorButtonState('profileReportSessionButton', true);
			},
			onSortCol:function(){
				setAnchorButtonState('viewStatusButton', true);
				setAnchorButtonState('printTicketButton', true);
 				setAnchorButtonState('profileReportSessionButton', true);
			},
			onSelectRow: function (rowId) {
					setSelectedTestAdminId(rowId);
					testTicketPopupValues(rowId,'list2');
					var selectedRData = $("#list2").getRowData(rowId);
					if(selectedRData.isSTabeProduct == "false" && selectedRData.isSTabeAdaptiveProduct == "false" && selectedRData.copyable == 'T'){
						updateCopySessionButton(true);
					}else{
						updateCopySessionButton(false);
					}
					initializeModifyTestPopup(rowId,'list2',selectedRData.isSTabeProduct, selectedRData.isSTabeAdaptiveProduct,selectedRData.productType, selectedRData.testName, selectedRData.testAdminName);	
					selectedTestAdminId = rowId;						
					$('#showSaveTestMessage').hide();
					if (selectedRData.isSTabeProduct == "true") {
	 					setAnchorButtonState('profileReportSessionButton', false);
	 				}
	 				else {
	 					setAnchorButtonState('profileReportSessionButton', true);
	 				}
			},
			loadComplete: function () {
 				updateModifyStdManifestButton(false);
 				updateCopySessionButton(false);
 				setAnchorButtonState('profileReportSessionButton', true);
 				
				$("#list2").jqGrid("hideCol","creatorOrgNodeId");
				if ($('#list2').getGridParam('records') === 0) {
				 	isGridEmpty = true;
            	 	$('#sp_1_pager2').text("1");
            	 	$('#next_pager2').addClass('ui-state-disabled');
            	 	$('#last_pager2').addClass('ui-state-disabled');
            	} else {
            		isGridEmpty = false;
            	}
            	if(orgSelectVar){
            		orgSelectVar = false;
            		if(!gridloaded) {
	            		populateCompletedSessionListGrid();
	            	} else {
	            		gridReloadPA();
	            	}
            	}else if(mySessionCliked){
            		mySessionCliked = false;
            		if(!gridloaded) {
			       		populateCompletedSessionListGrid();
			       	} else {
			       		gridReloadPA();
			       	}
            	}else {
            		if(!gridloaded) {
	            		populateCompletedSessionListGrid();
	            	} /*else {
	            		gridReloadPA();
	            	}*/
            	}
            	
            	
            	
            	
            	var width = jQuery("#sessionGrid").width();
			    width = width - 80; // Fudge factor to prevent horizontal scrollbars
			    jQuery("#list2").setGridWidth(width);
			    jQuery("#list3").setGridWidth(width);
			    setEmptyListMessage('CUFU');
				$("#list2").setGridParam({datatype:'local'});
				var tdList = ("#pager2_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				$('#showSaveTestMessage').hide();
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#list2").navGrid('#pager2', {
			search: false,add:false,edit:false,del:false     	
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchUserByKeywordList2").dialog({  
						title:$("#searchSessionID").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
					 	});
			    }, position: "one-before-last", title:"Search Session", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#pager2",{position: "first"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-trash", onClickButton:function(){
			    	if(selectedTestAdminId == ''|| selectedTestAdminId == null){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 220,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		gridSelectedToDelete = "list2";
		    			deleteSessionPopup();	
			    	}
			    	
			    }, position: "first", title:"Delete Session", cursor: "pointer",id:"del_list2"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-pencil", onClickButton:function(){
			    	if(selectedTestAdminId == ''|| selectedTestAdminId == null){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 220,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		editTestSession();
			    	}
			    	 
			    }, position: "first", title:"Edit Session", cursor: "pointer",id:"edit_list2"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-plus", onClickButton:function(){
			    	 scheduleNewSession();
			    }, position: "first", title:"Schedule Session", cursor: "pointer",id:"add_list2"
			});  
			jQuery("#refresh_list2").bind("click",function(){
				$("#searchUserByKeywordInputList2").val('');
				setAnchorButtonState('viewStatusButton', true);
				setAnchorButtonState('printTicketButton', true);
 				setAnchorButtonState('profileReportSessionButton', true);
				selectedTestAdminId = null;
			});
}

function searchUserByKeywordList2(){
	 var searchFiler = $.trim($("#searchUserByKeywordInputList2").val()), f;
	 var grid = $("#list2"); 
	 
	 if (searchFiler.length === 0) {
		 grid[0].p.search = false;
	 }else {
	 	 f = {groupOp:"OR",rules:[]};
		 f.rules.push({field:"testAdminName",op:"cn",data:searchFiler});
		 f.rules.push({field:"testName",op:"cn",data:searchFiler});
		 f.rules.push({field:"creatorOrgNodeName",op:"cn",data:searchFiler});
		 f.rules.push({field:"AssignedRole",op:"cn",data:searchFiler});
		 f.rules.push({field:"loginStartDateString",op:"cn",data:searchFiler});
		 f.rules.push({field:"loginEndDateString",op:"cn",data:searchFiler});
		 grid[0].p.search = true;
		 grid[0].p.ignoreCase = true;
		 $.extend(grid[0].p.postData,{filters:JSON.stringify(f)});
	 }
	 grid.trigger("reloadGrid",[{page:1,current:true}]); 
	 closePopUp('searchUserByKeywordList2');
	 selectedTestAdminId = null;
	 $("#list2").jqGrid('resetSelection');	//Removing grid selection after search
}

function resetSearchCrit(){
	$("#searchUserByKeywordInputList2").val('');
	var grid = $("#list2"); 
	grid.jqGrid('setGridParam',{search:false});	
    var postData = grid.jqGrid('getGridParam','postData');
    $.extend(postData,{filters:""});
}

function resetSearchCritList3(){
	$("#searchUserByKeywordInputList3").val('');
	var grid = $("#list3"); 
	grid.jqGrid('setGridParam',{search:false});	
    var postData = grid.jqGrid('getGridParam','postData');
    $.extend(postData,{filters:""});
}

function resetSearchList2(){
	var grid = $("#list2"); 
	$("#searchUserByKeywordInputList2").val('');
	 grid[0].p.search = false;
	 grid.trigger("reloadGrid",[{page:1,current:true}]); 
	 closePopUp('searchUserByKeywordList2');
}

function trapEnterKeyList2(e){
	var key;
   if(window.event)
        key = window.event.keyCode;     //IE
   else
        key = e.which;     //firefox
        
   if(key == 13){
   		searchUserByKeywordList2();
   }
}

function searchUserByKeywordList3(){
	 var searchFiler = $.trim($("#searchUserByKeywordInputList3").val()), f;
	 var grid = $("#list3"); 
	 
	 if (searchFiler.length === 0) {
		 grid[0].p.search = false;
	 }else {
	 	 f = {groupOp:"OR",rules:[]};
		 f.rules.push({field:"testAdminName",op:"cn",data:searchFiler});
		 f.rules.push({field:"testName",op:"cn",data:searchFiler});
		 f.rules.push({field:"creatorOrgNodeName",op:"cn",data:searchFiler});
		 f.rules.push({field:"AssignedRole",op:"cn",data:searchFiler});
		 f.rules.push({field:"loginStartDateString",op:"cn",data:searchFiler});
		 f.rules.push({field:"loginEndDateString",op:"cn",data:searchFiler});
		 grid[0].p.search = true;
		 grid[0].p.ignoreCase = true;
		 $.extend(grid[0].p.postData,{filters:JSON.stringify(f)});
	 }
	 grid.trigger("reloadGrid",[{page:1,current:true}]); 	
	 closePopUp('searchUserByKeywordList3');
	 selectedTestAdminId = null;
	 $("#list3").jqGrid('resetSelection');	//Removing grid selection after search
}

function resetSearchList3(){
	var grid = $("#list3"); 
	$("#searchUserByKeywordInputList3").val('');
	 grid[0].p.search = false;
	 grid.trigger("reloadGrid",[{page:1,current:true}]); 
	 closePopUp('searchUserByKeywordList3');
}

function trapEnterKeyList3(e){
	var key;
   if(window.event)
        key = window.event.keyCode;     //IE
   else
        key = e.which;     //firefox
        
   if(key == 13){
   		searchUserByKeywordList3();
   }
}

function populateCompletedSessionListGrid() {
 		//UIBlock();
 		gridloaded = true;
 		reset();
 		resetSearchCritList3();
       $("#list3").jqGrid({         
          url: 'getCompletedSessionForGrid.do', 
		  mtype:   'POST',
		  datatype: "json",          
          colNames:[$("#sessionName").val(),$("#testName").val(), $("#organization").val(), 'creatorOrgNodeId', $("#myRole").val(),$("#startDateGrid").val(), $("#endDateGrid").val(),'loginStartDateString','loginEndDateString','','',''],
		   	colModel:[
		   		{name:'testAdminName',index:'testAdminName', width:250, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'testName',index:'testName', width:225, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'creatorOrgNodeName',index:'creatorOrgNodeName', width:175, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'creatorOrgNodeId',index:'creatorOrgNodeId', width:0, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'AssignedRole',index:'AssignedRole',editable: true, width:100, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'loginStartDate',index:'loginStartDate', width:175, editable: true, align:"left", sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'}, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'loginEndDate',index:'loginEndDate', width:175, editable: true, align:"left", sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'},sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'loginStartDateString',index:'loginStartDateString', width:0},
		   		{name:'loginEndDateString',index:'loginEndDateString', width:0},
		   		{name:'copyable',index:'copyable', width:10,editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'isSTabeProduct',index:'isSTabeProduct', width:10,editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'isSTabeAdaptiveProduct',index:'isSTabeAdaptiveProduct', width:10,editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"testSessionPA", id:"testAdminId",
		   	records: function(obj) { 
		   	 $("#list3").jqGrid("hideCol","creatorOrgNodeId");
		   	 $("#list3").jqGrid("hideCol","loginStartDateString");
			 $("#list3").jqGrid("hideCol","loginEndDateString");
			 $("#list3").jqGrid("hideCol","copyable");
			 $("#list3").jqGrid("hideCol","isSTabeProduct");
			 sessionListPA = obj.sessionListPAMap;
		   	 } },
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager3', 
			sortname: 'loginEndDate', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,  
			caption: $("#sessionList").val(),
			ondblClickRow: function(rowid) {setSelectedTestAdminId(rowid);editTestSession();},
			onPaging: function() {
				var reqestedPage = parseInt($('#list3').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_pager3').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#list3').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#list3').setGridParam({"page": minPageSize});
				}
				setAnchorButtonState('viewStatusButton', true);
				setAnchorButtonState('printTicketButton', true);
 				setAnchorButtonState('profileReportSessionButton', true);
			},
			onSortCol:function(){
				setAnchorButtonState('viewStatusButton', true);
				setAnchorButtonState('printTicketButton', true);
 				setAnchorButtonState('profileReportSessionButton', true);
			},
			onSelectRow: function (rowId) {
					setSelectedTestAdminId(rowId);
					testTicketPopupValues(rowId,'list3');					
					$('#showSaveTestMessage').hide();
			 		var selectedRData = $("#list3").getRowData(rowId);
					if(selectedRData.isSTabeProduct == "false" && selectedRData.isSTabeAdaptiveProduct == "false" && selectedRData.copyable == 'T'){
						updateCopySessionButton(true);
					}else{
						updateCopySessionButton(false);
					}
					if (selectedRData.isSTabeProduct == "true") {
	 					setAnchorButtonState('profileReportSessionButton', false);
	 				}
	 				else {
	 					setAnchorButtonState('profileReportSessionButton', true);
	 				}
			},
			loadComplete: function () {
				updateModifyStdManifestButton(false);
				updateCopySessionButton(false);
 				setAnchorButtonState('profileReportSessionButton', true);
				
				$("#list3").jqGrid("hideCol","creatorOrgNodeId");
				$("#list3").jqGrid("hideCol","isSTabeAdaptiveProduct");
				if ($('#list3').getGridParam('records') === 0) {
					isPAGridEmpty = true;
            		$('#sp_1_pager3').text("1");
            		$('#next_pager3').addClass('ui-state-disabled');
            	 	$('#last_pager3').addClass('ui-state-disabled');
            	} else {
            		isPAGridEmpty = false;
            	}
            	var width = jQuery("#sessionGrid").width();
			    width = width - 80; // Fudge factor to prevent horizontal scrollbars
			    jQuery("#list3").setGridWidth(width);
			    
            	setEmptyListMessage('PA');
            	$.unblockUI();  
				$("#list3").setGridParam({datatype:'local'});
				var tdList = ("#pager3_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
				$('#showSaveTestMessage').hide();
				
				if (! isTreePopulated) {
		 			openTreeRequested = true;
 					populateTree();
				}				
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#list3").navGrid('#pager3', {
		    	search: false,add:false,edit:false,del:false 
			}).jqGrid('navButtonAdd',"#pager3",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchUserByKeywordList3").dialog({  
						title:$("#searchSessionID").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
					 	});
			    }, position: "one-before-last", title:"Search Session", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#pager3",{position: "first"
			}).jqGrid('navButtonAdd',"#pager3",{
			    caption:"", buttonicon:"ui-icon-trash", onClickButton:function(){
			    	if(selectedTestAdminId == ''|| selectedTestAdminId == null){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 220,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		gridSelectedToDelete = "list3";
		    			deleteSessionPopup();	
			    	}
			    	
			    }, position: "first", title:"Delete Session", cursor: "pointer",id:"del_list3"
			}).jqGrid('navButtonAdd',"#pager3",{
			    caption:"", buttonicon:"ui-icon-pencil", onClickButton:function(){
			    	if(selectedTestAdminId == ''|| selectedTestAdminId == null){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 220,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		editTestSession();
			    	}
			    	 
			    }, position: "first", title:"Edit Session", cursor: "pointer",id:"edit_list3"
			});  
			
			jQuery("#refresh_list3").bind("click",function(){
				$("#searchUserByKeywordInputList3").val('');
				setAnchorButtonState('viewStatusButton', true);
				setAnchorButtonState('printTicketButton', true);
 				setAnchorButtonState('profileReportSessionButton', true);
				selectedTestAdminId = null;
			});
	 setupButtonPerUserPermission();
	 
}
	
	 function setEmptyListMessage(requestedTab){
	 if(requestedTab == 'CUFU') {
		 if(isGridEmpty) {
		 	$('#list2').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
		 	$('#list2').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='6'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/SessionWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noCurrentOrFutureTestSessions").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noTestSessions").val()+"</td></tr></tbody></table></td></tr>");
		 }
	 } else if (requestedTab == 'PA'){
	 	 if(isPAGridEmpty) {
		 	$('#list3').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
		 	$('#list3').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='6'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/SessionWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noCompletedTestSessions").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noTestSessions").val()+"</td></tr></tbody></table></td></tr>");
		 }
	 } else if (requestedTab == 'studentGrid'){
	 	 if(isStuGridEmpty) {
	 	 	$('#list6').jqGrid('clearGridData') ;
	 	 	$('#totalStudent').text("0");
	 	 	if($("#supportAccommodations").val() != 'false')
	 	 	 	$('#stuWithAcc').text("0");
		 	$('#list6').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
		 	$('#list6').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='6'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/SessionWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsg").val()+"</td></tr></tbody></table></td></tr>");
		 }
	 }else if (requestedTab == 'TSL'){
	 	 if(isTestGridEmpty) {
		 	$('#testList').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
		 	$('#testList').append("<tr><td style='width: 100%;' align='center'  colspan='6'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/SessionWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noTestMsg").val()+"</th></tr></tbody></table></td></tr>");
		 }
	 }else if (requestedTab == 'proctorGrid'){
	 	 if(isProctorGridEmpty) {
		 	$('#listProctor').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
		 	$('#listProctor').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='6'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/SessionWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noProctorTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noProctorMsg").val()+"</td></tr></tbody></table></td></tr>");
		 }
	 }
	 
	 }
	
	
	function setupButtonPerUserPermission() {
		var userScheduleAndFindSessionEnable = $("#userScheduleAndFindSessionPermission").val();
		if (userScheduleAndFindSessionEnable == 'false') {	
			var element = document.getElementById('add_list2');
			element.style.display = 'none';
		}
		var isDeleteSessionEnable = $("#isDeleteSessionEnable").val();
		if(isDeleteSessionEnable == 'false'){
			var element1 = document.getElementById('del_list2');
			element1.style.display = 'none';
			var element2 = document.getElementById('del_list3');
			element2.style.display = 'none';
		}
	}

	function showTreeSlider() {
		openTreeRequested = true;
		$('#showSaveTestMessage').hide();
		 if(isTreePopulated) {
			 $("#show").css('display', 'none');
			 $("#hide").css('display', 'block');
			 $("#gap").width("3%");
			 $("#orgSlider").width("200");
			 $("#sessionGrid").width("980");
			 document.getElementById('orgSlider').style.display = 'block';
			// $("#sessionGrid").css('padding-left',10);
			$('#orgSlider').show('slide', {direction: 'left'}, 50);
			var width = jQuery("#sessionGrid").width();
		    width = width - 80; // Fudge factor to prevent horizontal scrollbars
		    
		    jQuery("#list2").setGridWidth(916);
		    jQuery("#list3").setGridWidth(916);
	    } else {
	    	if(orgTreeHierarchy){
	    		createSingleNodeSelectedTree(orgTreeHierarchy);
	    		$("#searchheader").css("visibility","visible");	
				$("#orgNodeHierarchy").css("visibility","visible");	
	    	}else{
	    		populateTree();
	    	}
	    }
	}
	
	function hideTreeSlider() {
		 $('#showSaveTestMessage').hide();
		 openTreeRequested  = false;
		 $("#hide").css('display', 'none');
		 $("#show").css('display', 'block');
		 $("#gap").width("0%");
		 $("#orgSlider").width("0");
		 $("#sessionGrid").width("1150");
		// $("#sessionGrid").css('padding-left',0);
		$('#orgSlider').show('slide', {direction: 'right'}, 1000);
		document.getElementById('orgSlider').style.display = 'none';
		var width = jQuery("#sessionGrid").width();
	    width = width -80; // Fudge factor to prevent horizontal scrollbars
	    
	    jQuery("#list2").setGridWidth(1099);
	    jQuery("#list3").setGridWidth(1099);
	    
	}
	
	
	function populateTree() {
	$.ajax({
		async:		false,
		beforeSend:	function(){
						//UIBlock();
					},
		url:		'userOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						//alert('in');
						//$.unblockUI();  
						//leafNodeCategoryId = data.leafNodeCategoryId;
						orgTreeHierarchy = data;
						jsonData = orgTreeHierarchy.data;
						getRootNodeDetails();
						createSingleNodeSelectedTree (orgTreeHierarchy);
						$("#searchheader").css("visibility","visible");	
						$("#orgNodeHierarchy").css("visibility","visible");	
												
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						//$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					},
		complete :  function(){
						//$.unblockUI();  
					}
	});

}


function createSingleNodeSelectedTree(jsondata) {
	   $("#orgNodeHierarchy").jstree({
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
	   
	  $("#orgNodeHierarchy").delegate("a","click", function(e) {
	    	//loadSessionGrid = true;
	    	$('#showSaveTestMessage').hide();
	    	var SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#treeOrgNodeId").val(SelectedOrgNodeId);
	    	var topNodeSelected = $(this).parent().attr("cid");
	    	if(topNodeSelected == '1'){
	    	 	openConfirmationPopup();
	    	} else {
	    		selectedTestAdminId = null;
	    		reset();
	    		orgSelectVar = true;
 		    	gridReload(false);
	    	}
  			
 		   
 		 	
		});
	   $("#orgNodeHierarchy").bind("loaded.jstree", 
		 	function (event, data) {
		 		for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#orgNodeHierarchy ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
		 	    isTreePopulated = true; 
				if(openTreeRequested){
					showTreeSlider();
				}
			}
		);
		
		registerDelegate("orgNodeHierarchy");
}

function registerDelegate(tree){

	$("#"+tree).delegate("li ins","click", function(e) {
		type = tree;
		var x = this.parentNode;		
		var categoryId  = x.getAttribute("tcl");
			if(categoryId != null || categoryId != undefined){
			currentCategoryLevel = categoryId ; 
			}
			else{
			currentCategoryLevel ="1";
			}
			currentNodeId = x.id;			
			var classState = $(x).hasClass("jstree-open");			
			var rootCategoryLevel = rootMap[currentNodeId];
	
		if (classState == false){
			if (currentCategoryLevel == 1) {	
			dataObj2 = [];	
			var indexOfRoot = getIndexOfRoot(currentNodeId);
			populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot);
			}
	
			var cacheData = map.get(currentNodeId);
			if (cacheData != null){
				currentTreeArray = cacheData;			
			}
			if (cacheData == null){
				switch(currentCategoryLevel){
					
					//Not caching at initial level because the whole data will be put in cache which may increase the cache size
					//considerably
					
					case "2": 	dataObj3 =getObject(jsonData,currentNodeId,currentCategoryLevel,x.parentNode.parentNode.id);
								currentIndex = dataObj3.index;
								currentTreeArray = dataObj3.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
							
					case "3": 	dataObj4 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj4,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "4": 	dataObj5 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj5,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "5": 	dataObj6 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj6,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "6": 	dataObj7 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj7,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "7": 	dataObj8 =map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj8,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								
							break;	
					case "8": 	dataObj9 =map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj9,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;						
					
				}
			}

		}
			
	});

}

	
	 function reset() { 
		$('#accordion').accordion('activate', 0 );
		$("#CurrentFuture").scrollTop(0);
		$("#Completed").scrollTop(0);
		
		document.getElementById('ShowButtons').style.display = "block";
 		setAnchorButtonState('viewStatusButton', true);
 		setAnchorButtonState('printTicketButton', true);
 		setAnchorButtonState('profileReportSessionButton', true);
	}
	
	 function gridReload(homePageLoad){ 
	 	   ishomepageload = homePageLoad;
	  		UIBlock();
	  		resetSearchCrit();
	  		var postDataObject = {};
           jQuery("#list2").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
     	   var urlVal = 'getSessionForUserHomeGrid.do';
     	   if(!homePageLoad) {
     	   		postDataObject.q = 2;
 				postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
 				urlVal = 'getSessionForSelectedOrgNodeGrid.do';
	 		}
     	   	jQuery("#list2").jqGrid('setGridParam', {url:urlVal,postData:postDataObject,page:1}).trigger("reloadGrid");
     	   	$("#searchUserByKeywordInputList2").val('');
			var grid = $("#list2"); 
			grid.jqGrid('setGridParam',{search:false});	
			jQuery("#list2").sortGrid('loginEndDate',true,'asc');
      }
      
      function gridReloadPA(homePageLoad){ 
      	   resetSearchCritList3();
	  	   jQuery("#list3").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});     
     	   var urlVal = 'getCompletedSessionForGrid.do';
     	   jQuery("#list3").jqGrid('setGridParam', {url:urlVal,page:1}).trigger("reloadGrid");
           jQuery("#list3").sortGrid('loginEndDate',true,'asc');
      }
      
      function gridReloadStu(addStudent){ 
      	$('#list6').GridUnload();		
      	populateSelectedStudent();
      }
      
      function gridReloadSelectStu(){ 
      	  UIBlock();
      	  var postDataObject = {};
 		  postDataObject.q = 2;
 		  postDataObject.stuForOrgNodeId = $("#stuForOrgNodeId").val();
 		  postDataObject.selectedTestId = $("#selectedTestId").val();
 		  postDataObject.blockOffGradeTesting = blockOffGradeTesting;
 		  postDataObject.selectedLevel = selectedLevel;
 		  
	      jQuery("#selectStudent").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});    
	       var urlVal = 'getStudentForList.do';
	       if(state == "EDIT"){
	       		postDataObject.testAdminId = selectedTestAdminId;
	       }
     	   delete jQuery("#selectStudent").jqGrid('getGridParam' ,'postData' )["filters"];
     	   jQuery("#selectStudent").jqGrid('setGridParam', {url:urlVal, postData:postDataObject, sortname: 'lastName' ,page:1}).trigger("reloadGrid");
     	   jQuery("#selectStudent").sortGrid('lastName',true,'asc');

      }
		
		
		function getDataFromJson(id, sessionListMap){
			
			var isRegisterStudentEnable = "";
			if(sessionListMap[id]){
				isRegisterStudentEnable = sessionListMap[id].isRegisterStudentEnable;
			}
			return isRegisterStudentEnable;
	}
	
	function sessionListDataReqBeforeConfirm(sessionListData) {
		blockOffGradeTesting = sessionListData.offGradeTestingDisabled;
		if(blockOffGradeTesting == null || blockOffGradeTesting == undefined || blockOffGradeTesting == "")
			blockOffGradeTesting = false;
		selectedLevel = sessionListData.level;
		if(selectedLevel == null || selectedLevel == undefined || selectedLevel == "")
			selectedLevel = "Show All"
	}
	
	function sessionListRelatedData(sessionListData) {
		var str = new Array();
		document.getElementById("aCode").style.visibility = "visible";
		if(sessionListData.subtests.length>0)  {
			document.getElementById("aCode").value = ProductData.accessCodeList[0];
		} else if( locatorSubtest!=undefined && locatorSubtest != null && locatorSubtest.id != undefined){
			document.getElementById("aCode").value = ProductData.accessCodeList[0];
		}else {
			document.getElementById("aCode").value = "";
		}
		document.getElementById("testSessionName_lbl").innerHTML = sessionListData.testName;
		if(state != "EDIT")
			document.getElementById("testSessionName").value = sessionListData.testName;

		if (selectGE != null) {
			if (isTabeProduct && (! isTabeLocatorProduct)) { 
				$("#selectGE").show();	
				$("#selectGELbl").show();		
				document.getElementById("selectGE").checked = selectGE;
				document.getElementById("selectGE").removeAttribute("disabled");
				if (state == "EDIT") {
					$("#selectGE").attr("disabled", stdsLogIn);
				}
			}
		}
		
		str = sessionListData.subtests;
					if(sessionListData.isRandomize == "Y" ){
						$("#randomDis").show();	
						$("#randDisLbl").show();
						$("#randomDis").val("Y");
						//$("#randomDistDiv").show();
						document.getElementById("randomDis").checked = true;
					}else if(sessionListData.isRandomize == "N" ){
						$("#randomDis").show();	
						$("#randDisLbl").show();
						$("#randomDis").val("N");
						//$("#randomDistDiv").show();
						document.getElementById("randomDis").checked = false;
					} else {
						document.getElementById("randomDis").checked = false;
						$("#randomDis").hide();	
						$("#randDisLbl").hide();
						$("#randomDis").val("");	
						//$("#randomDistDiv").hide();
					}
					currDate = sessionListData.startDate;
					nextDate = sessionListData.endDate;
					if(state != "EDIT"){
						$( "#startDate" ).datepicker( "option" , "minDate" , currDate ) ;
						$( "#endDate" ).datepicker( "option" , "minDate" , currDate ) ;
						if(sessionListData.minLoginEndDate != undefined){
							$( "#startDate" ).datepicker( "option" , "maxDate" , sessionListData.minLoginEndDate ) ;
							$( "#endDate" ).datepicker( "option" , "maxDate" , sessionListData.minLoginEndDate ) ;
						} else {
							$( "#startDate" ).datepicker( "option" , "maxDate" , null ) ;
							$( "#endDate" ).datepicker( "option" , "maxDate" , null ) ;
						}
						$( "#endDate" ).datepicker( "refresh" );
						$( "#startDate" ).datepicker( "refresh" );
						document.getElementById("startDate").value = sessionListData.startDate;
						document.getElementById("endDate").value = sessionListData.endDate;
					}
					//$("#endDate").val(nextDate);
	}
	
	
	function getDataFromTestJson(id, sessionList){
	
			var found = false;
			for(var i=0; i<sessionList.length ;i++){
				if(sessionList[i].id == id){
				    found = true;
				    return sessionList[i];
					//break;					
				}
			}
			if(!found) {
				$("#randomDis").hide();	
				$("#randDisLbl").hide();
				$("#randomDis").val("");
			}
			//return str;
			return new Array();
	}
 
 
 	function closePopUp(dailogId){
 		if(dailogId == 'scheduleSession') {
			$('#ssAccordion').accordion('activate', 0 );
			$("#Select_Test").scrollTop(0);
			$("#Test_Detail").scrollTop(0);
			$("#Add_Student").scrollTop(0);
			$('#Add_Proctor').scrollTop(0);
			$("#Test_Detail").hide();
			$("#Add_Student").hide();
			$('#Add_Proctor').hide();
			$("#Student_Tab").css('display', 'block');
			$("#Select_Student_Tab").css('display', 'none');
			$("#Proctor_Tab").css('display', 'block');
			$("#Select_Proctor_Tab").css('display', 'none');
			selectedOrg = [];
			orgCheckedStudent = [];
			orgunCheckedStudent = [];
			$('#selectStudent').GridUnload();
			selectStudentgridLoaded = false;				
			$("#slider-range").slider("option", "values", [540, 1020]);
			resetPopup();
			AddStudentLocaldata ={};
			delStuIdObjArray = [];
			isOnBack = false;
			document.getElementById("testDiv").style.display = "none";
			pindex = 0;
			pdindex = 1;
			//resetProctor();
			resetStudentSelection();
			resetOnSelectTestSessionData();
			resetProctor();
			$('#studentAddDeleteInfo').hide();
			$("#proctorAddDeleteInfo").hide();
			stdsLogIn = false;
			selectAllForDelete = false;
			noOfProctorAdded = 0;
			resetEditSessionPopulatedData();
			$("#displayEditInfo").hide();
			hideSubtestWarningMessage();
			document.getElementById("modifyTestDiv").style.display = "none";
			$("#titleEditInfo").html("");
			$("#messageEditInfo").html("");
			$("#endTest").hide();
			$("#stuOrgNodeHierarchy").undelegate();
			$("#proctorOrgNodeHierarchy").undelegate();
			onChangeHandler.reset();
			isPopUp = false;
			isTabeProduct = false;
		    isTabeAdaptiveProduct = false; 
		    isLasLinksProduct = false;
		}
		$("#"+dailogId).dialog("close");
	}
	
	
	function fetchDataOnConfirmation() {
		closePopUp('confirmationPopup');
		reset();
		selectedTestAdminId = null;
		orgSelectVar = true;
		//if(loadSessionGrid) {
 		gridReload(false);
 			
 		//} else {
 		//	gridReloadSelectStu();
 		//}
	}
	
	function openConfirmationPopup(){
	$("#confirmationPopup").dialog({  
		title:$("#confirmAlrt").val(),  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '400px',
	 	modal: true,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 $("#confirmationPopup").css('height',120);
		 var toppos = ($(window).height() - 290) /2 + 'px';
		 var leftpos = ($(window).width() - 410) /2 + 'px';
		 $("#confirmationPopup").parent().css("top",toppos);
		 $("#confirmationPopup").parent().css("left",leftpos);	
		 
	}	
	
	function removeStuConfirmationPopup(){
	$("#removeStuConfirmationPopup").dialog({  
		title:$("#confirmAlrt").val(),  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '400px',
	 	modal: true,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 $("#removeStuConfirmationPopup").css('height','110px');
		 var toppos = ($(window).height() - 290) /2 + 'px';
		 var leftpos = ($(window).width() - 410) /2 + 'px';
		 $("#removeStuConfirmationPopup").parent().css("top",toppos);
		 $("#removeStuConfirmationPopup").parent().css("left",leftpos);	
		 if(state == "EDIT") {
		 	$("#editRemove").show();
		 	$("#scheduleRemove").hide();
		 } else {
		 	$("#scheduleRemove").show();
		 	$("#editRemove").hide();
		 }
	}	
	
	
	function reloadHomePage(){
		
	 	var grid = $("#list2"); 
	 	grid[0].p.search = false;
		$("#searchUserByKeywordInputList2").val('');
		
		selectedTestAdminId = null;
		reset();
		hideTreeSlider();
		mySessionCliked = true;
		gridReload(true);
		$('#showSaveTestMessage').hide();
	}	
	
	
	function showAccoToolTip(rowId,event){
		if(accomodationMap[rowId]){
			var obj = accomodationMap[rowId];
			for(var key in obj){
				if(obj[key] == "T"){
					$("#"+key+"Status").show();
				}else {
					$("#"+key+"Status").hide();
				}
			}
			showAccoToolTipPopUp(event);
		}else {
			if(accomodationMapExisting[rowId]){
				var obj = accomodationMapExisting[rowId];
				for(var key in obj){
					if(obj[key] == "T"){
						$("#"+key+"Status").show();
					}else {
						$("#"+key+"Status").hide();
					}
				}
				showAccoToolTipPopUp(event);
			}
		}
	}
		
	var htimer;
	
	function showAccoToolTipPopUp(event) {
		var isIE = document.all?true:false;
		var tempX = 0;
		var tempY = 0;
		var legendDiv = null;
		var padding = 15;
		
		if (isIE) { 
			tempX = event.clientX + document.documentElement.scrollLeft;
			tempY = event.clientY + document.documentElement.scrollTop;
		}
		else { 
			tempX = event.pageX;
			tempY = event.pageY;
		}  
		legendDiv = document.getElementById("accommodationToolTip");		
		legendDiv.style.left = (tempX - $(legendDiv).width() / 3)+"px" ;
		legendDiv.style.top = (tempY - $(legendDiv).height() - padding)+"px"; 
		legendDiv.style.display = "block";
		$("td","#accommodationToolTip").css('padding-top','0px');
		$("td:visible","#accommodationToolTip").eq(0).css('padding-top','5px');
		htimer = setTimeout("hideAccoToolTipPopUp()", 50000);
	}
	
	function hideAccoToolTipPopUp() {
		clearTimeout(htimer);
		$("tr[id$='Status']","#accommodationToolTip").hide();
		document.getElementById("accommodationToolTip").style.display = "none";
	}
	
	var selectAllRow = false;
	var studentCheckCounter = 0;
	function populateSelectedStudent() {
 		UIBlock();
 		var studentIdTitle = $("#studentIdLabelName").val();
 		delStuIdObjArray = [];
 		stuGridloaded = true;
 		$("#list6").jqGrid({  
 		 data:  AddStudentLocaldata,
         datatype: 'local',         
          colNames:[ $("#testStuLN").val(),$("#testStuFN").val(), $("#testStuMI").val(), studentIdTitle, $("#testStuAccom").val(), leafNodeCategoryName , $("#testStuForm").val(), 'studentId', 'testCompletionStatus','Editable'],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:130, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'firstName',index:'firstName', width:130, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'middleName',index:'middleName', width:120, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'extPin1',index:'extPin1', width:275, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;'} },
		   		{name:'hasAccommodations',index:'hasAccommodations', width:165, editable: true, align:"left", sortable:true, title:false,
		   			cellattr: function (rowId, tv, rawObject, cm, rdata) { 
		   				var returnStr = '';
						if(rawObject.hasAccommodations == 'Yes'){
							returnStr = 'style="white-space: normal;cursor:pointer;" onmouseover="showAccoToolTip('+rawObject.studentId+',event);" onmouseout="hideAccoToolTipPopUp();"' ;
						}else {
							returnStr = 'style="white-space: normal;cursor:pointer;"' ;
						}	
						return returnStr;				
		   		}},
		   		{name:'orgNodeName',index:'orgNodeName',editable: true, width:150, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'itemSetForm',index:'itemSetForm',editable: true, width:75, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'studentId',index:'studentId',editable: false, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'testCompletionStatus',index:'testCompletionStatus',editable: false,hidden:true, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;width=0px!important' } },
		   		{name:'statusEditable',index:'statusEditable',editable: false,hidden:true, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;width=0px!important' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"rows", id:"studentId",
		   	records: function(obj) { 
		   	 //sessionListCUFU = JSON.stringify(obj.studentNode);
		   	 } },
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:true,
			pager: '#pager6', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 162,  
			caption:$("#stuListGrid").val(),
			onPaging: function() {
				//clearMessage();
				$("#studentAddDeleteInfo").hide();
				var reqestedPage = parseInt($('#list6').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_pager6').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#list6').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#list6').setGridParam({"page": minPageSize});
				}
				
			},
			gridComplete: function() {
				if(state=="EDIT"){
					var allRowsInGridPresent = $('#list6').jqGrid('getDataIDs');
					for(var k = 0; k < allRowsInGridPresent.length; k++) {
						var selectedRowData = $("#list6").getRowData(allRowsInGridPresent[k]);
						if(selectedRowData.statusEditable =="F"){
							$("#"+(k+1)+" td input","#list6").attr("disabled", true);
				 			$("#"+(k+1), "#list6").addClass('ui-state-disabled');
				 			editDataMrkStds.put(selectedRowData.studentId,selectedRowData.studentId);
						}				
					}				
				}
				if(selectAllForDelete) {
					$("#cb_list6").attr("checked", true);
				 	$("#cb_list6").trigger('click');
				 	$("#cb_list6").attr("checked", true);
				} else {
					var allRowsInGridPresent = $('#list6').jqGrid('getDataIDs');
					for(var k = 0; k < allRowsInGridPresent.length; k++) {
						var selectedRowData = $("#list6").getRowData(allRowsInGridPresent[k]);
						var selectedRowId = selectedRowData.studentId;
						if(delStuIdObjArray != undefined && delStuIdObjArray.length > 0) {
							for(var j = 0; j < delStuIdObjArray.length; j++) {
								if(delStuIdObjArray[j] != undefined && delStuIdObjArray[j] == selectedRowId) {
									$("#"+allRowsInGridPresent[k]+" td input").attr("checked", true);
						 			$("#"+allRowsInGridPresent[k]).trigger('click');
						 			$("#"+allRowsInGridPresent[k]+" td input").attr("checked", true);
								}
							}
						}
					}
					
				}
			},
			onSelectAll: function (rowIds, status) {
				$("#studentAddDeleteInfo").hide();
				delStuIdObjArray = [];
				deleteStudentCounter = 0;
				if(status) {
					selectAllForDelete = true;
					for(var i = 0; i < AddStudentLocaldata.length; i++) {
					    if(AddStudentLocaldata[i].statusEditable =="T"){
							delStuIdObjArray[deleteStudentCounter] = AddStudentLocaldata[i].studentId;
							deleteStudentCounter++;
						}
					}
					selectAllRow = true;
					studentCheckCounter = AddStudentLocaldata.length;
				} else {
					selectAllForDelete = false;	
					selectAllRow = false;	
					studentCheckCounter = 0;			
				}
			},
			onSelectRow: function (rowid, status) {
				//selectAllForDelete = false;
				$("#studentAddDeleteInfo").hide();
				var selectedRowData = $("#list6").getRowData(rowid);
				var selectedRowId = selectedRowData.studentId;
				if(status) {
					if(!checkPresenceInDelStuIdObjArray(selectedRowId)) {
						delStuIdObjArray[deleteStudentCounter] = selectedRowId;
						deleteStudentCounter++;
					}
					++studentCheckCounter;
				} else {
					selectAllForDelete = false;
					var delIndx = -1;
					for(var k = 0; k < delStuIdObjArray.length; k++) {
						if(delStuIdObjArray[k] == selectedRowId) {
							delIndx = k;
						}
					}
					if(delIndx != -1) {
						delStuIdObjArray.splice(delIndx,1);
						deleteStudentCounter--;
					}
					--studentCheckCounter;
				}
				if(selectAllRow && !status) {
					selectAllRow = false;
					$("#cb_list6").attr("checked", false);
				}
				if(AddStudentLocaldata.length == studentCheckCounter) {
					selectAllRow = true;
					$("#cb_list6").attr('checked', true).trigger('click').attr('checked', true);
				}
			},
			loadComplete: function () {
            	if ($('#list6').getGridParam('records') === 0) {
				 	isStuGridEmpty = true;
				 	studentTempMap = new Map();
				 	studentMap = new Map();

            	} else {
            		isStuGridEmpty = false;
            		cloneStudentMapToTemp();
            	}
            	
			    setEmptyListMessage('studentGrid');
				$.unblockUI();  
				$("#list6").setGridParam({datatype:'local'});
				var tdList = ("#pager6_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				var width = jQuery("#scheduleSession").width();
		    	width = width - 85; // Fudge factor to prevent horizontal scrollbars
		    
				if(isTabeProduct || isTabeAdaptiveProduct) {
					
					$("#list6").jqGrid("hideCol","itemSetForm"); 
				} else {
					$("#list6").jqGrid("showCol","itemSetForm"); 
				}
				
				var showAccommodations = $("#supportAccommodations").val();
				if(showAccommodations  == 'false') {
					$("#list6").jqGrid("hideCol","hasAccommodations"); 
				} else {
					$("#list6").jqGrid("showCol","hasAccommodations"); 
				}
				$("#list6").jqGrid("hideCol","studentId");
				jQuery("#list6").setGridWidth(width,true);
				
				if ($('#list6').getGridParam('records') === 0) {
            	 	$('#sp_1_pager6').text("1");
            	 	$('#next_pager6').addClass('ui-state-disabled');
            	 	$('#last_pager6').addClass('ui-state-disabled');
            	}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#list6").navGrid('#pager6',{del:false,add:false,edit:false,view:false,search:false,refresh:false})
	 		.jqGrid('navButtonAdd',"#pager6",{
			    caption:"", buttonicon:"ui-icon-trash", onClickButton:function(){
			    	if(delStuIdObjArray && delStuIdObjArray.length > 0){
			    		removeStuConfirmationPopup();
			    	}else {			    		
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 220,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}
			    	
			    }, position: "first", title:"Remove Student", cursor: "pointer",id:"del_list6"
			});
		if(hideStudentDelButton) {
			$("#del_list6").hide();
		} else {
			$("#del_list6").show();
		}
	}
	
	function checkPresenceInDelStuIdObjArray(deleteId) {
		var present = false;
		if (delStuIdObjArray == undefined || delStuIdObjArray.length  <= 0)
			return present;
		for(var i = 0; i < delStuIdObjArray.length; i++) {
			if(delStuIdObjArray[i] == deleteId) {
				present = true;
				break;
			}
		}
		return present;
	}
	
	function removeSelectedStudent() {
		for(var i = 0; i < delStuIdObjArray.length; i++) {
			if(studentTempMap != undefined && studentTempMap.get(delStuIdObjArray[i]) != null && studentTempMap.get(delStuIdObjArray[i]) != undefined) {				
				if(allSelectOrg != undefined && allSelectOrg.length > 0) {
					for(var k = 0; k < allSelectOrg.length; k++) {
					    if(allSelectOrg[k] == null || allSelectOrg[k] == undefined){
					    	continue;
					    }
						if(allSelectOrg[k] == (studentTempMap.get(delStuIdObjArray[i])).orgNodeId || allSelectOrg[k] ==(studentTempMap.get(delStuIdObjArray[i])).orgNodeId+"_f") {
							allSelectOrg.splice(k,1);
							countAllSelect--;
						}
					}
				}
				delete accomodationMap[delStuIdObjArray[i]];
				studentTempMap.remove(delStuIdObjArray[i]);
				savedStudentMap.remove(delStuIdObjArray[i]);
			}
		}
		closePopUp('removeStuConfirmationPopup');
		returnSelectedStudent();
				
		delStuIdObjArray = [];
		if(selectAllForDelete) {
			resetStudentSelection();
		} else {
			cloneStudentMapToTemp();
		}
		$('#totalStudent').text(AddStudentLocaldata.length);
		if($("#supportAccommodations").val() != 'false')
	 		 $('#stuWithAcc').text(studentWithaccommodation);
		$('#list6').GridUnload();
		populateSelectedStudent();
		
	}
	
	
	
	function scheduleNewSession() {
	$('#showSaveTestMessage').hide();
	hideSubtestWarningMessage();
	document.getElementById("modifyTestDiv").style.display = "none";
	$("#endTest").hide();
	state = "ADD";
	isOKEQActionPerformed = false;
	isFirstAccordSelected = true; // As initially the first accordion will be displayed
	isSecondAccordSelected = false;
	isThirdAccordSelected = false;
	isFourthAccordSelected = false;
	OKPreviousProduct = undefined;
	OKCurrentProduct = undefined;
	var postDataObject = {};
 	postDataObject.currentAction = 'init';
 	savedStudentMap = new Map();
 	isCopySession = false; //added  for copy test session
 	isSelectingStudent = false;
 	hideProctorDelButton = false;
	hideStudentDelButton = false;
 	if(orgDataInform != undefined) {
 		orgDataInform = {};
 	}
 	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'selectTest.do',
		type:		'POST',
		dataType:	'json',
		data:		postDataObject,
		contentType: 'application/json; charset=UTF-8', 
		success:	function(data, textStatus, XMLHttpRequest){
						ProductData = data;
						isOKAdmin = data.isOkAdmin;
						forceTestBreak = data.forceTestBreak;
						selectGE = data.selectGE;
						state = "SCHEDULE";
						
						if(ProductData.noTestExists == true){
							noTestExist = true;
							document.getElementById("testDiv").style.display = "none";
							document.getElementById("noTestDiv").style.display = "inline";
						}else{
							noTestExist = false;
							document.getElementById("noTestDiv").style.display = "none";
							document.getElementById("testDiv").style.display = "inline";
						}
						if(!noTestExist){
							var selectedproductId= data.selectedProductId;
							fillProductGradeLevelDropDown('testGroupList',data.product,selectedproductId);
							fillDropDownWithDefaultValue("timeZoneList",data.testZoneDropDownList, data.userTimeZone);
							reloadGrids(data.product[0].testSessionList,data.product[0].showLevelOrGrade);
							//populateTestListGrid(data.product[0].testSessionList,true,data.product[0].showLevelOrGrade);
							fillDropDown("topOrgNode",data.topNodeDropDownList);
							var productSelected = $("#testGroupList").val();
							if(productSelected == '9003') {
								isOKEQDefaultSelected = true;
							} else {
								isOKEQDefaultSelected = false;
							}
							processStudentAccordion();
							processProctorAccordion();
							$("#productType").val(data.product[0].productType);
							$("#showStudentFeedback").val(data.product[0].showStudentFeedback);
							displayProductAcknowledgement(ProductData.product[0].acknowledgmentsURL); //Added for view product Acknowledgement link
							
						}
						isPopUp = true;	
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
		$("#scheduleSession").dialog({  
		title:$("#schSession").val(),  
		resizable:false,
		autoOpen: true,
		width: '1024px',
		modal: true,
		closeOnEscape: false,
		open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		});						
		$("#sData").addClass("ui-state-disabled");		
		document.getElementById("sData").disabled=true;	
		setPopupPosition();
	}
	
	function processStudentAccordion() {
		if(!stuGridloaded) {
   			populateSelectedStudent();
   		} else {
   			gridReloadStu(true);
   		}
   		
   		
	}

	function fillProductGradeLevelDropDown( elementId, optionList, selectedProductId) {
		var ddl = document.getElementById(elementId);
		var optionHtml = "" ;
		var selectProductIndex;
		//var optionList = data.product;
		if(optionList.length < 1) {
			optionHtml += "<option  value='Select a product'>Show All</option>";
		} else {
			for(var i = 0; i < optionList.length; i++ ) {
				if((optionList[i].productId == '9003') && !isOKAdmin) {
					continue; // Only state level oklahoma admin should be able to see/schedule/edit 9003 product
				}
				if(selectedProductId==optionList[i].productId) { 	     
					optionHtml += "<option  value='"+ optionList[i].productId+"'selected >"+ optionList[i].productName+"&nbsp;&nbsp;</option>";
					//fillDropDown("grade", optionList[i].gradeDropDownList);
					if(optionList[i].isLasLinksProduct){
					 	isLasLinksProduct = true;
					 }
					if(!(optionList[i].isTabeProduct || optionList[i].isTabeAdaptiveProduct )) {
						isTabeProduct = false;
						isTabeLocatorProduct=false;
						isTabeAdaptiveProduct = false;
						if(!(optionList[i].hideLevelDropDown)) {
							if(optionList[i].showLevelOrGrade=='level') {
								document.getElementById("levelDiv").style.display ="inline";
								document.getElementById("level").style.visibility ="visible";
								document.getElementById("gradeDiv").style.display ="none";
								fillDropDown("level",optionList[i].levelDropDownList);	
								dropListToDisplay = optionList[i].levelDropDownList;
							} else if (optionList[i].showLevelOrGrade=='grade') {
								document.getElementById("gradeDiv").style.display ="inline";
								document.getElementById("level").style.visibility ="visible";
								document.getElementById("levelDiv").style.display ="none";
								fillDropDown("level",optionList[i].levelDropDownList);	
								dropListToDisplay = optionList[i].levelDropDownList;
							} else { 
							document.getElementById("gradeDiv").style.display ="none";
							document.getElementById("levelDiv").style.display ="none";
							document.getElementById("level").style.visibility ="hidden";
							}
						} else {
							//fillDropDown("level",optionList[i].levelDropDownList);
							document.getElementById("gradeDiv").style.display ="none";
							document.getElementById("levelDiv").style.display ="none";
							document.getElementById("level").style.visibility ="hidden";
						}				
					
					} else {
					    if(optionList[i].isTabeLocatorProduct){ 
					    	isTabeLocatorProduct = true;
					    } else {
					    	isTabeLocatorProduct = false;
					    }

						if( optionList[i].isTabeAdaptiveProduct) {
							isTabeAdaptiveProduct = true;
							isTabeProduct = false;
						} else {
							isTabeAdaptiveProduct = false;
							isTabeProduct = true;
						}
						
						document.getElementById("gradeDiv").style.display ="none";
						document.getElementById("levelDiv").style.display ="none";
						document.getElementById("level").style.visibility ="hidden";
					}
					selectProductIndex = i;
					
				} else {
					optionHtml += "<option  value='"+ optionList[i].productId+"'>"+ optionList[i].productName+"&nbsp;&nbsp;</option>";
				}
				
			}
		}
		$(ddl).html(optionHtml);
		return selectProductIndex;
	}
	


	function fillDropDown( elementId, optionList) {
		var ddl = document.getElementById(elementId);
		var optionHtml = "" ;
		if(optionList.length < 1) {
			optionHtml += "<option  value='Select'>Select</option>";
		} else {
			for(var i = 0; i < optionList.length; i++ ) {		     
				optionHtml += "<option  value='"+ optionList[i].id+"'>"+ optionList[i].name+"&nbsp;&nbsp;</option>";	
			}
		}
		$(ddl).html(optionHtml);
		if(optionList.length==1) {
			ddl.disabled=true;
		} else {
			ddl.disabled=false;
		}
	}
	
	
	function fillDropDownWithDefaultValue( elementId, optionList, defVal) {
		var ddl = document.getElementById(elementId);
		var optionHtml = "" ;
		if(optionList.length < 1) {
			optionHtml += "<option  value='Select'>Select</option>";
		} else {
			for(var i = 0; i < optionList.length; i++ ) {
				if(optionList[i].id==defVal) {
					optionHtml += "<option  value='"+ optionList[i].id+"' selected>"+ optionList[i].name+"&nbsp;&nbsp;</option>";	
				} else {
					optionHtml += "<option  value='"+ optionList[i].id+"'>"+ optionList[i].name+"&nbsp;&nbsp;</option>";	
				}		     
			}
		}
		$(ddl).html(optionHtml);
	}
	function changeSubtestConfirmPopup() {
		if(((state == "EDIT" && !isStdDetClicked) || (state == "EDIT" && isStdDetClicked && AddStudentLocaldata != undefined && AddStudentLocaldata.length > 0)) || (AddStudentLocaldata != undefined && AddStudentLocaldata.length > 0)) {
			$("#subtestChangeConfirmationPopup").dialog({  
			title:$("#confirmAlrt").val(),  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
			 $("#subtestChangeConfirmationPopup").css('height',120);
			 var toppos = ($(window).height() - 290) /2 + 'px';
			 var leftpos = ($(window).width() - 410) /2 + 'px';
			 $("#subtestChangeConfirmationPopup").parent().css("top",toppos);
			 $("#subtestChangeConfirmationPopup").parent().css("left",leftpos);
		 } else {
		 	subtestChangeProcess();
		 	selectedSubtestId = selectedTestId;
		 	sessionListRelatedData(testSessionListRequired);
		 }
	}
	
	function changeProductConfirmed() {
		/*if(AddStudentLocaldata != undefined && AddStudentLocaldata.length > 0) {
			$("#productChangeConfirmationPopup").dialog({  
			title:$("#confirmAlrt").val(),  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
			 $("#productChangeConfirmationPopup").css('height',120);
			 var toppos = ($(window).height() - 290) /2 + 'px';
			 var leftpos = ($(window).width() - 410) /2 + 'px';
			 $("#productChangeConfirmationPopup").parent().css("top",toppos);
			 $("#productChangeConfirmationPopup").parent().css("left",leftpos);
		 } else {*/
		 //Added for Oklahoma customer
		 if(isOKAdmin) {
			 isOKProductChanged = true;
			 var productSelected = $("#testGroupList").val();
			 if(productSelected == '9003') {
			 	isOKEqTestSelected = true;
			 } else {
			 	isOKEqTestSelected = false;
			 }
		 }
		  changeGradeAndLevel();
		 //}
		 hideSubtestWarningMessage();
		 document.getElementById("modifyTestDiv").style.display = "none";
	}
	
	function closeSubtestConfirmPopup() {
		 closePopUp('subtestChangeConfirmationPopup');
		 subtestChangeProcess();
		 $('#studentAddDeleteInfo').hide();
		 resetStudentSelection();
		 hideSelectStudent();
		 $('#list6').GridUnload();
		 populateSelectedStudent();
		 previousValue = $("#testGroupList").val();
		 selectedSubtestId = selectedTestId;
		 offGradeSubtestChanged = true;
		 sessionListRelatedData(testSessionListRequired);
		 if(state == "EDIT"){
		  	isStdDetClicked = true;
		 }
		 selectedAccessCodeMap = new Map();
		
	}
	
	function closeProductConfirmPopup() {
		 closePopUp('productChangeConfirmationPopup');
		 changeGradeAndLevel();
		 resetStudentSelection();
		 hideSelectStudent();
		 $('#list6').GridUnload();
		 populateSelectedStudent();
		 previousValue = $("#testGroupList").val();
	}
	
	function closeProductConfirmationPopUp() {		
		closePopUp('productChangeConfirmationPopup');		
		$("#testGroupList").val(previousValue);
	}
	
	function closeSubtestConfirmationPopUp() {		
		closePopUp('subtestChangeConfirmationPopup');
		offGradeCancled = true;
		if(selectedSubtestId != "")
			$("#"+selectedSubtestId).trigger('click');
		else
			$("#testList").jqGrid('resetSelection');
	}

	function  changeGradeAndLevel(){
		selectedSubtestId = "";
		selectedTestId = "";
		var e = document.getElementById("testGroupList");  
		selectProductId = e.options[e.selectedIndex].value;
		var optionList = ProductData.product
		for(var i = 0; i < optionList.length; i++ ) {
			if((optionList[i].productId == '9003') && !isOKAdmin) {
				continue; // Only state level oklahoma admin should be able to see 9003 product
			}
			if(selectProductId==optionList[i].productId) { 	     
				$("#productType").val(optionList[i].productType);
				$("#showStudentFeedback").val(optionList[i].showStudentFeedback); 
				if(optionList[i].isLasLinksProduct){ 
					   isLasLinksProduct = true;
				} else {
					    isLasLinksProduct = false;
					 }	     
				if(!(optionList[i].isTabeProduct || optionList[i].isTabeAdaptiveProduct )) {
					isTabeProduct = false;
					isTabeLocatorProduct=false;
					isTabeAdaptiveProduct = false;
					if(!(optionList[i].hideLevelDropDown)) {
						if(optionList[i].showLevelOrGrade=="level") {
							document.getElementById("levelDiv").style.display ="inline";
							document.getElementById("level").style.visibility ="visible";
							document.getElementById("gradeDiv").style.display ="none";
							fillDropDown("level",optionList[i].levelDropDownList);	
							dropListToDisplay = optionList[i].levelDropDownList;
						} else if (optionList[i].showLevelOrGrade=="grade") {
							document.getElementById("gradeDiv").style.display ="inline";
							document.getElementById("level").style.visibility ="visible";
							document.getElementById("levelDiv").style.display ="none";
							fillDropDown("level",optionList[i].levelDropDownList);
							dropListToDisplay = optionList[i].levelDropDownList;	
						} else { 
							//fillDropDown("level",optionList[i].levelDropDownList);	
							document.getElementById("gradeDiv").style.display ="none";
							document.getElementById("levelDiv").style.display ="none";
							document.getElementById("level").style.visibility ="hidden";
						}
					} else {
						document.getElementById("gradeDiv").style.display ="none";
						document.getElementById("levelDiv").style.display ="none";
						document.getElementById("level").style.visibility ="hidden";
						//fillDropDown("level",optionList[i].levelDropDownList);	
					}
						
				} else {
					
					if(optionList[i].isTabeLocatorProduct){ 
					   isTabeLocatorProduct = true;
					 } else {
					    isTabeLocatorProduct = false;
					 }

					 if( optionList[i].isTabeAdaptiveProduct) {
						isTabeProduct = false;
						isTabeAdaptiveProduct = true;
					 } else {
						isTabeProduct = true;
						isTabeAdaptiveProduct = false;
					 
					 }
					document.getElementById("gradeDiv").style.display ="none";
					document.getElementById("levelDiv").style.display ="none";
					document.getElementById("level").style.visibility ="hidden";
				}
				var tList = optionList[i].testSessionList;
				var noOfRows = tList.length;
				reloadGrids(tList,optionList[i].showLevelOrGrade );
				displayProductAcknowledgement(optionList[i].acknowledgmentsURL); //Added for view product Acknowledgement link
				break;
			} 
			isTestSelected = false;		
		}
		resetOnSelectTestSessionData();
		processStudentAccordion();
		processProctorAccordion();

	}

	function setPopupPosition(){
				$("#Select_Test").css("height",'400px');
				$("#Test_Detail").css("height",'400px');
				$("#Add_Student").css("height",'400px');
				$("#Add_Proctor").css("height",'400px');
				var toppos = ($(window).height() - 650) /2 + 'px';
				var leftpos = ($(window).width() - 1024) /2 + 'px';
				$("#scheduleSession").parent().css("top",toppos);
				$("#scheduleSession").parent().css("left",leftpos);	
				
	}
	
	function toggleAccessCode(){
		if(subtestLength > 0){
			subtestGridLoaded = false;
			
			
			var testBreak = document.getElementById("testBreak");
			if(!testBreak.checked){		
				isTestBreak = false;
				populateAccessCodeMap(isTestBreak);
				createSubtestGrid();
				//document.getElementById("aCodeHead").style.visibility = "hidden";
				document.getElementById("aCode").style.visibility = "visible";			
							
				/*for(var i=0;i<subtestLength;i++){
					document.getElementById("aCodeDiv"+i).style.visibility = "hidden";
					
				}*/
				$("#hasTestBreak").val("F"); 
			}else{
				isTestBreak = true;
				populateAccessCodeMap(isTestBreak);
				createSubtestGrid();
				//document.getElementById("aCodeHead").style.visibility = "visible";
				document.getElementById("aCode").style.visibility = "hidden";
				$("#hasTestBreak").val("T");
				/*for(var i=0;i<subtestLength;i++){
					//document.getElementById("aCodeDiv"+i).style.visibility = "visible";
					if(document.getElementById("actionTaken"+i).value == "1"){
						document.getElementById("aCodeB"+i).removeAttribute("disabled");
						document.getElementById("aCodeB"+i).className = "";
					}else{
						document.getElementById("aCodeB"+i).setAttribute("disabled",true);
						document.getElementById("aCodeB"+i).className = "textboxDisabled";
					}
				}*/
			}
			 var accessCode = selectedAccessCodeMap.get("AccessCode");
			 if(accessCode==null){
			 	accessCode = ProductData.accessCodeList[0];
			 }
			document.getElementById("aCode").value = accessCode; 

		}
	}
	
	function stopSlide(event, ui) {
	 	var minutes0Stop = parseInt($("#slider-range").slider("values", 0) % 60, 10);
		var hours0Stop = parseInt($("#slider-range").slider("values", 0) / 60 % 24, 10);
		var minutes1Stop = parseInt($("#slider-range").slider("values", 1) % 60, 10);
		var hours1Stop = parseInt($("#slider-range").slider("values", 1) / 60 % 24, 10);
			startTime = getTime(hours0Stop, minutes0Stop);
			endTime = getTime(hours1Stop, minutes1Stop);
			$("#time").text(startTime + ' - ' + endTime);
	}

	function slideTime(event, ui){
	    minutes0 = parseInt($("#slider-range").slider("values", 0) % 60, 10);
	    hours0 = parseInt($("#slider-range").slider("values", 0) / 60 % 24, 10);
	    minutes1 = parseInt($("#slider-range").slider("values", 1) % 60, 10);
	    hours1 = parseInt($("#slider-range").slider("values", 1) / 60 % 24, 10);
	    if(ui.values[0] - ui.values[1] < 60 && ui.values[0] - ui.values[1] > -60) {
	    	return false;
	    }
	    startTime = getTime(hours0, minutes0);
	    endTime = getTime(hours1, minutes1);
	    $("#time").text(startTime + ' - ' + endTime);	
	}
	
	
	
	function getTime(hours, minutes) {
	    var time = null;
	    minutes = minutes + "";
	    if (hours < 12) {time = "AM";}
	    else {  time = "PM";}
		
	    if (hours == 0) {hours = 12;}
	    else if (hours > 12) {hours = hours - 12; }
	    if (minutes.length == 1) {minutes = "0" + minutes;}
		return hours + ":" + minutes + " " + time;
	}
	
	function checkMax() {
	    var size = $("#slider-range").slider("values", 1) - $("#slider-range").slider("values", 0);
	    if( size >= 1435) {
	        $("#slider-range div").addClass("ui-state-error");
	        $("#slider-range div").removeClass("ui-widget-header");
	        /*$("#scheduleSubmit").attr("disabled","disabled");
	        $("#scheduleSubmit").addClass("ui-state-disabled");
	        $("#scheduleSubmit").removeClass("ui-state-default");*/
	        $("#SlideMax").text("Cannot be more than 24 hours");
	    }
	    else {
	        $("#slider-range div").addClass("ui-widget-header");
	        $("#slider-range div").removeClass("ui-state-error");
	        /*$("#scheduleSubmit").attr("disabled","");
	        $("#scheduleSubmit").addClass("ui-state-default");
	        $("#scheduleSubmit").removeClass("ui-state-disabled");*/
	        $("#SlideMax").text("");
	    }
	}
	function add() {
	    $('#Schedule tbody').append('<tr>' +
	        '<td>' + startTime + '</td>' +
	        '<td>' + endTime + '</td>' +
	        '</tr>');
	}
	
	function populateTestListGrid(testSessionlist, testGroupLoad, showLevelOrGrade) {
		isTestGroupLoad  = testGroupLoad;
		selectedTestId = ""; // resetting old selected test id
 		UIBlock();
 		var levelOrGradeTitle = "None";
 		var istabe = false;
		var istabeadaptive = false;
		var islasLinksProduct = false;
		
		if(isLasLinksProduct == undefined || isLasLinksProduct =='undefined') {
 			islasLinksProduct = false;
 		} else {
 			islasLinksProduct = isLasLinksProduct;
 		}

 		if(isTabeProduct == undefined || isTabeProduct =='undefined') {
 			istabe = false;
 		} else {
 			istabe = isTabeProduct;
 		}

		if(isTabeAdaptiveProduct == undefined || isTabeAdaptiveProduct =='undefined') {
 			istabeadaptive = false;
 		} else {
 			istabeadaptive = isTabeAdaptiveProduct;
 		}


 		if (!istabe && !istabeadaptive) {
	 		if(showLevelOrGrade== undefined || showLevelOrGrade=='undefined'){
	 			levelOrGradeTitle = 'None';
	 		} else if (showLevelOrGrade=='level') {
	 			levelOrGradeTitle = $("#testDetLevel").val();
	 		} else if (showLevelOrGrade=='grade') {
	 			levelOrGradeTitle = $("#testDetGrade").val();
	 		} 
 		} 
 		//reset();
 		$("#testList").jqGrid({         
        	data: testSessionlist,
			datatype: "local",         
			colNames:[$("#testName").val(),levelOrGradeTitle, $("#testDetsubTest").val(), $("#testDetDuration").val()],
		   	colModel:[
		   		{name:'testName',index:'testName', width:55, align:"left",sorttype:'text',sortable:isSortable, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor: pointer;' } },
		   		{name:'level',index:'level', width:18, align:"left",sorttype:'text',sortable:isSortable,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor: pointer;' } },
		   		{name:'subtestCount',index:'subtestCount', width:20, align:"left",sorttype:'text',sortable:isSortable,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor: pointer;' } },
		   		{name:'duration',index:'duration',width:22, align:"left", sortable:isSortable, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor: pointer;' } },
		   	],
		   	jsonReader: { repeatitems : false, root:"rows", id:"id",
		   		records: function(obj) { 
		   	 		
		   	 	} },
		   	
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			//shrinkToFit: true,
			pager: '#testPager', 
			sortname: 'level', 
			viewrecords: true, 
			sortorder: "asc",
			height: 150,
			caption:$("#testListGrid").val(),
		//	ondblClickRow: function(rowid) {viewEditStudentPopup();},
			onPaging: function() {
				//clearMessage();
				var reqestedPage = parseInt($('#testList').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_testPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#testList').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#testList').setGridParam({"page": minPageSize});
				}
				
			},
			onSelectRow: function () {
					previousValue = $("#testGroupList").val();
					subtestGridLoaded = false;
					if(selectedTestId == $("#testList").jqGrid('getGridParam', 'selrow')) {
						$('#displayMessage').hide();
						return;
					}
					hideSubtestWarningMessage();
					document.getElementById("modifyTestDiv").style.display = "none";
					selectedTestId = $("#testList").jqGrid('getGridParam', 'selrow');
					$('#displayMessage').hide();
					var previousOffGrade = blockOffGradeTesting;
					testSessionListRequired = getDataFromTestJson(selectedTestId, testSessionlist);
					testJSONValue = testSessionListRequired.subtests;
					if(testSessionListRequired.autoLocator){
						hasAutolocator = true;
						locatorSubtest = testSessionListRequired.locatorSubtest;
					} else {
						hasAutolocator = false;
						locatorSubtest = {};
					}
					//testJSONValue = getDataFromTestJson(selectedTestId, testSessionlist);
					sessionListDataReqBeforeConfirm(testSessionListRequired);
					// Added to refresh student list grid if user changes tests
					if((state == 'EDIT' && blockOffGradeTesting) || (AddStudentLocaldata != undefined && AddStudentLocaldata.length > 0 && blockOffGradeTesting)) {
						if(selectedSubtestId != selectedTestId)
		 					changeSubtestConfirmPopup();
		 				else {
		 					if(!offGradeCancled) {
		 						subtestChangeProcess();
		 					} else {
		 						offGradeCancled = false;
		 					}
		 					selectedSubtestId = selectedTestId;
		 					if(offGradeSubtestChanged) {
		 						sessionListRelatedData(testSessionListRequired);
		 						offGradeSubtestChanged = false;
		 					}
		 				}
					} else {
						subtestChangeProcess();
						if(state != 'EDIT' && (AddStudentLocaldata == undefined || AddStudentLocaldata.length == undefined || AddStudentLocaldata.length <= 0) 
							&& (previousOffGrade || blockOffGradeTesting)) {
								hideSelectStudent();
						}
						selectedSubtestId = selectedTestId;
						if(!offGradeCancled) {
							sessionListRelatedData(testSessionListRequired);
							offGradeSubtestChanged = false;
						} else {
							offGradeCancled = false;
						}
					}
					if(state == 'EDIT' && isStdDetClicked && (AddStudentLocaldata == undefined || AddStudentLocaldata.length == undefined || AddStudentLocaldata.length <= 0) 
						&& (previousOffGrade || blockOffGradeTesting) && !firstTimeOpen) {
							hideSelectStudent();
					}
					if(firstTimeOpen)
						firstTimeOpen = false;
					
					// Force Test Breaks, Access Codes	
					if (forceTestBreak) {
						setTestBreakForCustomer();
					}
					if(state != 'EDIT' && previousValue == '9003' && !isOKEQActionPerformed && isOKAdmin) {
						isOKEQActionPerformed = true;
						isOKEqTestSelected = true;
						$("#Proctor_Tab").css('display', 'block');
						$("#Select_Proctor_Tab").css('display', 'none');	
						$("#proctorOrgNodeHierarchy").undelegate();
						var schedulerId = $("#schedulerUserId").val();
		 				addAllProcsForOklahoma(schedulerId);
						returnSelectedProctor(); // Need to update temp variables also
					}
			},
			loadComplete: function () {
				if ($('#testList').getGridParam('records') === 0) {
				 	isTestGridEmpty = true;
				 	$('#sp_1_testPager').text("1");
				 	$('#next_testPager').addClass('ui-state-disabled');
            	 	$('#last_testPager').addClass('ui-state-disabled');
            	} else {
            		isTestGridEmpty = false;
            	}
            	
			
            	var width = jQuery("#Select_Test").width();
			    width = width/2 - 15; // Fudge factor to prevent horizontal scrollbars
			    jQuery("#testList").setGridWidth(width);
			    setEmptyListMessage('TSL');
			    $.unblockUI();  
				$("#testList").setGridParam({datatype:'local'});
				var tdList = ("#testPager_center table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#testList").jqGrid('navGrid', '#testPager', { edit: false, add: false, del: false, search: false, refresh: false });
	 $("#testPager_left").width(75);
	 var colPos = 1;
	 var myGrid = $('#testList'); 
	 var columnName = myGrid.getGridParam("colModel")[colPos].name;
	 if(istabe || istabeadaptive || columnName=="None") {
	 	myGrid.jqGrid('hideCol', myGrid.getGridParam("colModel")[colPos].name);  
	 }
	 		
	}
	
	function setTestBreakForCustomer(){
		var testBreak = document.getElementById("testBreak");
		if (! testBreak.disabled) {
			testBreak.checked = true;
			toggleAccessCode();
			document.getElementById("aCode").style.visibility = "hidden";
			testBreak.disabled = true;
		}
	}
	
	function createSubtestGrid(){
		//var subtestArr = new Array();
		//subtestArr = subtestDataArr;
		var subtestArr = selectedSubtests;
		var subtestData = '';
		var locatorSubtestTableHtml = '';
		//alert(subtestGridLoaded);
		var indexFactor = 0;
		if(locatorSubtest!=undefined && locatorSubtest.id !=undefined ) {
			indexFactor = 1;
			locatorSubtestTableHtml += '<td height="5" colspan="2">';
			locatorSubtestTableHtml +='<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#A6C9E2"><tr><td><table width="100%" class="ts" cellpadding="0" cellspacing="1">';
	    	locatorSubtestTableHtml +='<tr class="subtestHeader" >';
	    	locatorSubtestTableHtml +='<th width="24" height="23" align="center"><strong>'+$("#textSelectMsg").val()+'</strong></th>';
	    	//locatorSubtestTableHtml +='<th width="24" height="23" align="center"><strong>'+$("#hashDisplay").val()+'</strong></th>';
	    	/*if(isTestBreak){
	    		locatorSubtestTableHtml +='<th width="289" height="23" align="left" style="padding-left:5px;"><strong>'+$("#subtestNameDisplay").val() +'</strong></th>';
	    		locatorSubtestTableHtml +='<th width="130" height="23"><div align="center" id="aCodeHead"><strong>'+ $("#acsCodeDisplay").val() +' </strong></div></th>';
	    	
	    	} else {
	    		locatorSubtestTableHtml +='<th width="419" height="23" align="left" style="padding-left:5px;"><strong>'+$("#subtestNameDisplay").val() +'</strong></th>';
	    	}*/
	    	
	    	if(isTestBreak){
	    		locatorSubtestTableHtml +='<th width="289" height="23" align="left" style="padding-left:5px;"><strong>'+$("#subtestNameDisplay").val() +'</strong></th>';
	    		locatorSubtestTableHtml +='<th width="130" height="23"><div align="center" id="aCodeHead"><strong>'+ $("#acsCodeDisplay").val() +' </strong></div></th>';
	    	
	    	} else {
	    		locatorSubtestTableHtml +='<th width="419" height="23" align="left" style="padding-left:5px;"><strong>'+$("#subtestNameDisplay").val() +'</strong></th>';
	    		//locatorSubtestTableHtml +='<th id ="locatorAccessCodeHeader" width="130" height="23" style="display:none"><div align="center" id="aCodeHead"><strong>'+ $("#acsCodeDisplay").val() +' </strong></div></th>';
	    	}
	    	
	    	locatorSubtestTableHtml +='<th width="82" height="23" align="center"><strong>'+$("#durationDisplay").val()+'</strong></th>';
			locatorSubtestTableHtml +='</tr>';
			
			locatorSubtestTableHtml +='<tr >';
			locatorSubtestTableHtml +='<td height="23" width="24" bgcolor="#FFFFFF" style="padding-left:10px;" >';
			var disabled = "";
			if(allSubtests!=null && allSubtests!= undefined && allSubtests.length<1){
				disabled = 'disabled ="disabled"';
			}
			if(hasAutolocator) {
				locatorSubtestTableHtml +='<input id = "hasAutolocator" type = "checkBox" name="hasAutolocator" checked="checked"'+ disabled+ ' value="true" onClick="javascript:updateLocatorValue(); " />';
			} else {
				locatorSubtestTableHtml +='<input id="hasAutolocator" type = "checkBox" name="hasAutolocator"  '+ disabled+ '  value="false" onClick="javascript:updateLocatorValue(); "/>';
			}
			
			
			//locatorSubtestTableHtml +='<div align="center" id="num'+i+'">'+parseInt(i+1)+'<input type="hidden" id="actionTaken'+i+'" value="1"/></div>';
			locatorSubtestTableHtml +='<input type = "hidden" id ="itemSetIdTD" name ="itemSetIdTD_l" value ="'+locatorSubtest.id+'" />';
			if(locatorSubtest.level != undefined){
				locatorSubtestTableHtml +='<input type = "hidden" id ="itemSetForm" name ="itemSetForm_l" value ="'+locatorSubtest.level+'" />';
			} else {
				locatorSubtestTableHtml +='<input type = "hidden" id ="itemSetForm" name ="itemSetForm_l" value ="" />';
			}
				
			if(locatorSubtest.sessionDefault != undefined){
				locatorSubtestTableHtml +='<input type = "hidden" id ="sessionDefault" name ="sessionDefault_l" value ="'+locatorSubtest.sessionDefault+'" />';
			} else {
				locatorSubtestTableHtml +='<input type = "hidden" id ="sessionDefault" name ="sessionDefault_l" value ="" />';
			}
			locatorSubtestTableHtml +='</td>';
			
			if(isTestBreak ){
					locatorSubtestTableHtml +='<td height="23" width="289" bgcolor="#FFFFFF" style="padding-left:5px;">';
					locatorSubtestTableHtml +='<div align="left" id="sName">'+locatorSubtest.subtestName+'</div>';
					locatorSubtestTableHtml +='</td>';
					locatorSubtestTableHtml +='<td height="23" width="130" align="center" bgcolor="#FFFFFF">';
					locatorSubtestTableHtml +='<div align="center" id="'+locatorSubtest.id+'">';
					if(hasAutolocator){
					    var accessCode = selectedAccessCodeMap.get(locatorSubtest.id);
					    if(accessCode == null){
					    	accessCode = getSelectUniqueAccessCode();
					    	if(accessCode == undefined){
					    		accessCode = selectedAccessCodeMap.get("AccessCode");
					    	}
					    	selectedAccessCodeMap.put(locatorSubtest.id, accessCode);
					    }
						locatorSubtestTableHtml +='<input name="aCodeB_l" type="text" size="13" id="aCodeB_l" value="'+accessCode+'" onblur="javascript:trimTextValue(this); return false;" style="padding-left:2px;" maxlength="32" /></div>';
					} else {
						locatorSubtestTableHtml +='<input name="aCodeB_l" type="text" size="13" id="aCodeB_l" value="" onblur="javascript:trimTextValue(this); return false;" style="padding-left:2px;" maxlength="32" /></div>';
					}
					
					locatorSubtestTableHtml +='</td>';
				}else{
					locatorSubtestTableHtml +='<td height="23" width="419" bgcolor="#FFFFFF" style="padding-left:5px;">';
					locatorSubtestTableHtml +='<div align="left" id="sName">'+locatorSubtest.subtestName+'</div>';
					locatorSubtestTableHtml +='</td>';
					
					//locatorSubtestTableHtml +='<td id ="locatorAccessCodeBody" height="23" width="130" align="center" bgcolor="#FFFFFF" style="display:none">';
					//locatorSubtestTableHtml +='<div align="center" id="'+locatorSubtest.id+'">';
					//locatorSubtestTableHtml +='<input name="aCodeB" type="text" size="13" id="aCodeB_l" value="'+ProductData.accessCodeList[0]+'" onblur="javascript:trimTextValue(this); return false;" style="padding-left:2px;" maxlength="32" /></div>';
					//locatorSubtestTableHtml +='</td>';
				}
				locatorSubtestTableHtml +='<td height="23" width="82" align="center" bgcolor="#FFFFFF">';
				locatorSubtestTableHtml +='<div align="center" id="duration">'+locatorSubtest.duration+'</div>';
				locatorSubtestTableHtml +='</td>';
				
				locatorSubtestTableHtml +='</tr>';	
				locatorSubtestTableHtml +='</table></td></tr></table>';
				document.getElementById("locatorSubtestGrid").innerHTML = locatorSubtestTableHtml;
				document.getElementById("locatorSubtestGrid").style.display = "";
				document.getElementById("locatorSubtestGridHeading").style.display = "";
				if(allSubtests.length==0){
					document.getElementById("locatorSubtestGridToolTip").style.display = "none";
				} else {
					document.getElementById("locatorSubtestGridToolTip").style.display = "";
				}
				
		
		} else {
			document.getElementById("locatorSubtestGrid").innerHTML = "";
			document.getElementById("locatorSubtestGrid").style.display = "none";
			document.getElementById("locatorSubtestGridHeading").style.display = "none";
			
			
		}
		
		if(allSubtests.length > 1 &&  (isTabeProduct || isTabeAdaptiveProduct || isLasLinksProduct)){
		   document.getElementById("modifyTestDiv").style.display = "";
		} else {
		 	document.getElementById("modifyTestDiv").style.display = "none";
		}
		if(!subtestGridLoaded && subtestArr.length > 0){
			subtestLength = subtestArr.length;
			document.getElementById("subtestGrid").style.display = "";
			document.getElementById("noSubtest").style.display = "none";
			var tr = '';
			var th = '';
			subtestData +='<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#A6C9E2"><tr><td><table id ="subtestTable" width="100%" class="ts" cellpadding="0" cellspacing="1">';
							
			th +='<tr class="subtestHeader" >';
			th +='<th width="24" height="23" align="center"><strong>'+$("#hashDisplay").val()+'</strong></th>';
			if(isTestBreak){
				th +='<th width="289" height="23" align="left" style="padding-left:5px;"><strong>'+$("#subtestNameDisplay").val() +'</strong></th>';				
				th +='<th width="130" height="23"><div align="center" id="aCodeHead"><strong>'+ $("#acsCodeDisplay").val() +' </strong></div></th>';
			}else{
				th +='<th width="419" height="23" align="left" style="padding-left:5px;"><strong>'+$("#subtestNameDisplay").val() +'</strong></th>';
				//th +='<th width="130" id ="subtestAccessCodeHeader" height="23" style="display:none"><div align="center" id="aCodeHead"><strong>'+ $("#acsCodeDisplay").val() +' </strong></div></th>';
			}
			th +='<th width="82" height="23" align="center"><strong>'+$("#durationDisplay").val()+'</strong></th>';
			/*if(isTabeProduct && !isTabeLocatorProduct ){
				th +='<th width="34" height="23">&nbsp;</th>';
			}*/
			th +='</tr>';
			subtestData += th;
			
			for(var i=0;i<subtestArr.length; i++){	
			    
				tr = ''			
				tr +='<tr bgcolor="#FFFFFF" >';
				tr +='<td height="23" width="24" >';
				tr +='<div align="center" id="num'+i+'">'+parseInt(i+1)+'<input type="hidden" id="actionTaken'+i+'" value="1"/></div>';
				tr +='<input type = "hidden" id ="itemSetIdTD'+i+'" name ="itemSetIdTD" value ="'+subtestArr[i].id+'" />';
				if(subtestArr[i].level != undefined){
					tr +='<input type = "hidden" id ="itemSetForm" name ="itemSetForm" value ="'+subtestArr[i].level+'" />';
				} else {
					tr +='<input type = "hidden" id ="itemSetForm" name ="itemSetForm" value ="" />';
				}
				
				if(subtestArr[i].sessionDefault != undefined){
					tr +='<input type = "hidden" id ="sessionDefault" name ="sessionDefault" value ="'+subtestArr[i].sessionDefault+'" />';
				} else {
					tr +='<input type = "hidden" id ="sessionDefault" name ="sessionDefault" value ="" />';
				}
				
				tr +='</td>';
				if(isTestBreak){
					var accessCode = selectedAccessCodeMap.get(subtestArr[i].id);
					if(accessCode == null){
						accessCode = getSelectUniqueAccessCode();
						if(accessCode == undefined){
					    	accessCode = selectedAccessCodeMap.get("AccessCode");
					    }
						selectedAccessCodeMap.put(subtestArr[i].id, accessCode);
					}
					tr +='<td height="23" width="289"  style="padding-left:5px;">';
					tr +='<div align="left" id="sName'+i+'">'+subtestArr[i].subtestName+'</div>';
					tr +='</td>';
					tr +='<td height="23" width="130" align="center" >';
					tr +='<div align="center" id="'+subtestArr[i].id+'">';
					tr +='<input name="aCodeB" type="text" size="13" id="aCodeB'+i+'" value="'+accessCode+'" onblur="javascript:trimTextValue(this); return false;" style="padding-left:2px;" maxlength="32" /></div>';
					tr +='</td>';
				}else{
					tr +='<td height="23" width="419"  style="padding-left:5px;">';
					tr +='<div align="left" id="sName'+i+'">'+subtestArr[i].subtestName+'</div>';
					tr +='</td>';
					
					//tr +='<td id ="subtestAccessCodeBody" height="23" width="130" align="center" style="display:none">';
					//tr +='<div align="center" id="'+subtestArr[i].id+'">';
					//tr +='<input name="aCodeB" type="text" size="13" id="aCodeB'+i+'" value="'+ProductData.accessCodeList[i+indexFactor]+'" onblur="javascript:trimTextValue(this); return false;" style="padding-left:2px;" maxlength="32" /></div>';
					//tr +='</td>';
				}
				tr +='<td height="23" width="82" align="center" >';
				tr +='<div align="center" id="duration'+i+'">'+subtestArr[i].duration+'</div>';
				tr +='</td>';
				/*if(isTabeProduct && !isTabeLocatorProduct){
					tr +='<td height="23" align="center" width="34" >';
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
			subtestGridLoaded = true;
		}else if (subtestArr.length == 0  ){
			document.getElementById("noSubtest").style.display = "none";
			document.getElementById("subtestGrid").style.display = "none";
			document.getElementById("subtestGrid").innerHTML = "";

		}/*else{
			subtestLength = 0;
			subtestGridLoaded = false;
			subtestData = "";
			document.getElementById("subtestGrid").style.display = "none";
			document.getElementById("noSubtest").style.display = "";
		}*/
	}
	
	function removeSubtestOption(action,rowId){
		if(action == '0'){
			document.getElementById("imgPlus"+rowId).style.display = "inline";
			document.getElementById("imgMin"+rowId).style.display = "none";
			document.getElementById("num"+rowId).className = "lblDisabled";
			document.getElementById("sName"+rowId).className = "lblDisabled";
			document.getElementById("duration"+rowId).className = "lblDisabled";
			if(isTestBreak){
				//if(document.getElementById("aCodeDiv"+rowId).style.display == "inline"){
					document.getElementById("aCodeB"+rowId).setAttribute("disabled",true);
					document.getElementById("aCodeB"+rowId).className = "textboxDisabled";
				//}
			}
			document.getElementById("actionTaken"+rowId).value = "0";
			
		}else{
			document.getElementById("imgPlus"+rowId).style.display = "none";
			document.getElementById("imgMin"+rowId).style.display = "inline";
			document.getElementById("num"+rowId).className = "";
			document.getElementById("sName"+rowId).className = "";
			document.getElementById("duration"+rowId).className = "";
			if(isTestBreak){
				//if(document.getElementById("aCodeDiv"+rowId).style.display == "inline"){
					document.getElementById("aCodeB"+rowId).removeAttribute("disabled");
					document.getElementById("aCodeB"+rowId).className = "";
				//}
			}
			document.getElementById("actionTaken"+rowId).value = "1";
		}
	}
	
	function reloadGrids(tList, showlevelOrGrade){
	  	$("#sessionListDiv").hide();
		$('#testList').GridUnload();				
		populateTestListGrid(tList, true, showlevelOrGrade);
		$("#sessionListDiv").show();
		$('#displayMessage').hide();
		$("#sData").addClass("ui-state-disabled");
		document.getElementById("sData").disabled=true;

		subtestLength = 0;
		var testBreak = document.getElementById("testBreak");
		isTestSelected = false;
		if(testBreak.checked) testBreak.checked = false;
		document.getElementById("testBreak").disabled=true;
		$("#hasTestBreak").val("F");
		document.getElementById("aCode").style.visibility = "hidden";
		document.getElementById("testSessionName_lbl").innerHTML = $("#noTestSelected").val();
		document.getElementById("subtestGrid").style.display = "none";
		document.getElementById("noSubtest").style.display = "";
		document.getElementById("locatorSubtestGrid").innerHTML = "";
		document.getElementById("locatorSubtestGrid").style.display = "none";
		document.getElementById("locatorSubtestGridHeading").style.display = "none";
		locatorSubtest={};
		hasAutolocator =false;
		
		if(state != "EDIT"){
			document.getElementById("testSessionName").value = "";
			document.getElementById("startDate").value = "";			
			document.getElementById("endDate").value = "";			
			document.getElementById("time").innerHTML = "8:00 AM - 5:00 PM";
			document.getElementById("testLocation").value = "";	
		}
		$("#randomDis").hide();	
		$("#randDisLbl").hide();		
		$("#randomDis").val("");		
		//$("#randomDistDiv").hide();								
		$("#selectGE").hide();	
		$("#selectGELbl").hide();		
					
	}
	
	function resetPopup() { 
		$("#sessionListDiv").hide();
		//$('#testList').GridUnload();
		$('#displayMessage').hide();		
		subtestLength = 0;
		var testBreak = document.getElementById("testBreak");
		isTestSelected = false;
		if(testBreak.checked) testBreak.checked = false;
		document.getElementById("testBreak").disabled=true;	
		$("#hasTestBreak").val("F");
		document.getElementById("aCode").style.visibility = "hidden";		
		document.getElementById("subtestGrid").style.display = "none";
		document.getElementById("noSubtest").style.display = "";
		document.getElementById("locatorSubtestGrid").innerHTML = "";
		document.getElementById("locatorSubtestGrid").style.display = "none";
		locatorSubtest = {};
		hasAutolocator = false;
		document.getElementById("modifyTestDiv").style.display = "none";
		document.getElementById("testSessionName_lbl").innerHTML = $("#noTestSelected").val();
		document.getElementById("testSessionName").value = "";
		document.getElementById("startDate").value = "";			
		document.getElementById("endDate").value = "";			
		document.getElementById("time").innerHTML = "8:00 AM - 5:00 PM";
		document.getElementById("testLocation").value = "";											
		$("#randomDis").hide();	
		$("#randDisLbl").hide();
		$("#randomDis").val("");
		//$("#randomDistDiv").hide();
		$("#selectGE").hide();	
		$("#selectGELbl").hide();		
		
		document.getElementById("locatorSubtestGridHeading").style.display = "none";
 		
 		
	}
	
	function validateTest(){
		//var acc   = $("#ssAccordion");
		var testSessionName = trim(document.getElementById("testSessionName").value);
		var requiredFields = "";
     	var requiredFieldCount = 0; 
		//var startDate = trim(document.getElementById("startDate").value);
		//var endDate = trim(document.getElementById("endDate").value);	
		//var accessCode = trim(document.getElementById("aCode").value);
		
		if(!isTestSelected){
			requiredFields = "";
			if ( testSessionName.length == 0 ) {
					requiredFieldCount += 1;            
					requiredFields = buildErrorString("Test Name", requiredFieldCount, requiredFields);
					setMessage("Missing required field", requiredFields, "errorMessage", $("#reqTextmsg").val());       
				}
			return false;
		}
		else if(!validateAccessCodeInformation()){
			verifyAccessCodeDetails();
		}
		else {
			return true;
		}
	}
	
	// Added Nov-29
	
	function validateTestInformation(){
		var testSessionName = trim(document.getElementById("testSessionName").value);
		var startDate = trim(document.getElementById("startDate").value);
		var endDate = trim(document.getElementById("endDate").value);	
		var accessCode = trim(document.getElementById("aCode").value);
		var testLocation = trim(document.getElementById("testLocation").value);
		
			if(testSessionName == "" || startDate == "" || endDate == "" || !validateDate(startDate,endDate) || !validString(testLocation)){				
				return false;
			} else if(!validString(testSessionName)){
				return false;
			}
		return true;
	}
	
	 function verifyTestDetails(){
	 	var requiredFields = "";
     	var requiredFieldCount = 0; 
     	var testSessionName = trim(document.getElementById("testSessionName").value);
		var startDate = trim(document.getElementById("startDate").value);
		var endDate = trim(document.getElementById("endDate").value);
		//var testBreakVal = 0;
		//var accessCode = trim(document.getElementById("aCode").value);
		var testLocation = trim(document.getElementById("testLocation").value);
		
		if ( testSessionName.length == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("Test Session Name", requiredFieldCount, requiredFields);       
		}
		
	   	if ( startDate.length == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("Test Start Date", requiredFieldCount, requiredFields);       
		}
	
	   	if ( endDate.length == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("Test End Date", requiredFieldCount, requiredFields);       
		} 
			
		if(startDate.length != 0 && endDate.length != 0 && !validateDate(startDate,endDate)){
			 	setMessage($("#invDateMsg").val(), "", "errorMessage", "");       
		}	
		if (requiredFieldCount > 0) {
			if (requiredFieldCount == 1) {
				setMessage("Missing required field", requiredFields, "errorMessage", $("#reqTextmsg").val());
			}
			else {
				setMessage("Missing required fields", requiredFields, "errorMessage", $("#reqTextMultimsg").val());
			}
			return;
		}
		
		 if (testSessionName != "" && !validString(testSessionName) ) {
		 	setMessage($("#snNmInChHdrMsg").val(), $("#snNmInChBdyMsg").val() , "errorMessage","");
		 } else if(testLocation != "" && !validString(testLocation)){
		 	setMessage($("#tstLcnInChHdrMsg").val(), $("#tstLcnInChBdyMsg").val() , "errorMessage","");
		 }	

	 }
	 

    // Added to validate Access Code : Start
    
    function validateAccessCodeInformation(){

		var accessCode = trim(document.getElementById("aCode").value);
		
			if(subtestLength > 0){
				var testBreak = document.getElementById("testBreak");
				if(!testBreak.checked){						
					if(!validateMissingTestAccessCode()|| !validateAccessCodeMinLength( false) || !validateAccessCodeSpecialChar(false))
					   return false;	
				}else{
					if(!validateMissingTestAccessCodes()|| !validateAccessCodeMinLength( true) || !validateIdenticalTestAccessCodes()|| !validateAccessCodeSpecialChar(true))
					   return false;
				}
			} else {
				if(!validateMissingTestAccessCode()|| !validateAccessCodeMinLength( false)|| !validateAccessCodeSpecialChar(false))
					   return false;
			}
		return true;
	}
	
	 function verifyAccessCodeDetails(){
	 	var requiredFields = "";
     	var requiredFieldCount = 0; 
		var testBreakVal = 0;
		var accessCode = trim(document.getElementById("aCode").value);
		
			if(subtestLength > 0){
				var testBreak = document.getElementById("testBreak");
				if(!testBreak.checked){	
					if(!validateMissingTestAccessCode()) {
						setMessage($("#missTacHdrMsg").val(), $("#missTacMsg").val(), "errorMessage","");
					} else if(!validateAccessCodeMinLength( false) ) {
						setMessage($("#tacSixCharsMsg").val(), "", "errorMessage","");
					} else if(!validateAccessCodeSpecialChar(false)) {
						setMessage($("#tacSpCharHdrMsg").val(), $("#tacSpCharNAMsg").val(), "errorMessage","");
					}
				}else{
					if(!validateMissingTestAccessCodes()) {
						setMessage($("#missTacHdrMulMsg").val(), $("#missTac1Msg").val(), "errorMessage", $("#missTac2Msg").val());
					} else if(!validateAccessCodeMinLength( true) ) { 
						setMessage($("#tacSixCharsMsg").val(), "", "errorMessage","");
					} else if(!validateAccessCodeSpecialChar(true)) {
						setMessage($("#tacSpCharHdrMsg").val(), $("#tacSpCharNAMsg").val(), "errorMessage","");
					}else if(!validateIdenticalTestAccessCodes()) {
						setMessage($("#tacIdentTacHdrMsg").val() , $("#tacIdentTac1Msg").val(), "errorMessage", $("#tacIdentTac2Msg").val());
					}
					
				}
			} else {
				
				if(!validateMissingTestAccessCode()) {
					setMessage($("#missTacHdrMsg").val(), $("#missTacMsg").val(), "errorMessage","");
				} else if(!validateAccessCodeMinLength( false) ) {
					setMessage($("#tacSixCharsMsg").val(), "", "errorMessage","");
				} else if(!validateAccessCodeSpecialChar(false)) {
					setMessage($("#tacSpCharHdrMsg").val(), $("#tacSpCharNAMsg").val(), "errorMessage","");
				}
			
			}
	 }
	 
    function buildErrorString(field, count, str){
        var result = str;
        if (count == 1) {
            result += field;
        }else {
            result += (", " + field);            
        }        
        return result;
    }
    
    // Added to validate Access Code
    
    function validateDate(sDate, eDate){
    	var sdate = new Date(sDate);
    	var edate = new Date(eDate);
    	
    	if(sdate > edate)
        	return false;
        	
        return true;
       
    }
	 
	  function buildErrorString(field, count, str){
        var result = str;
        if (count == 1) {
            result += field;
        }else {
            result += (", " + field);            
        }        
        return result;
    }
	 
	function ValidateSave(){
		if(!validateTest()){
			$('#displayMessage').show();
			verifyTestDetails();			
			e.stopPropagation(); 
		}else{
			$('#displayMessage').hide();
		}
	}	
	
	
	function  changeSessionList() {
		var el = document.getElementById("level");  
		var selectlevel = el.options[el.selectedIndex].value;
		
		var ep = document.getElementById("testGroupList");  
		selectProductId = ep.options[ep.selectedIndex].value;
		var optionList = ProductData.product
		hideSubtestWarningMessage();
		document.getElementById("modifyTestDiv").style.display = "none";
		for(var i = 0; i < optionList.length; i++ ) {
			if(selectProductId==optionList[i].productId) { 	     
				var tList = optionList[i].testSessionList;
				if(!(selectlevel=="Show all")) {
					tList =filterSessionList(tList,selectlevel);
				}
				reloadGrids(tList,optionList[i].showLevelOrGrade );
				break;
			} 
			isTestSelected = false;		
		}
		resetOnSelectTestSessionData();
	
	}
	
	function  filterSessionList(testSessionList, gradeOrLevel) {
		var newSessionList = new Array();
		var counter = 0;
		for(var i = 0; i < testSessionList.length; i++ ) {
				if(testSessionList[i].level==gradeOrLevel) {
					newSessionList[counter]=testSessionList[i];
					counter= counter+1;
				}
		}
		return newSessionList;
	}
	
	function setMessage(title, content, type, message){
			$("#title").text(title);
			$("#content").html(content);
			$("#message").text(message);
	
		}
	

	function trim(str, chars) {
		return ltrim(rtrim(str, chars), chars);
	}
 
	function ltrim(str, chars) {
		chars = chars || "\\s";
		return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
	}
 
	function rtrim(str, chars) {
		chars = chars || "\\s";
		return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
	}

    function validString(str){
        var characters = [];
        characters = toCharArray(str);
        for (var i=0 ; i<characters.length ; i++) {
            var character = characters[i];
            if (containsInvalidCharacter(character))
                return false;
        }
        return true;
    }
	  
	  function toCharArray(str){
       var charArr=new Array();
       for(var i=0;i<str.length;i++){
            charArr[i]= str.charAt(i);
            }
            return charArr;
            
      }
      
       function containsInvalidCharacter(str){
        var ch = toascii(str);
        var invalidChar = ((str == '!') || 
                         (str == '@') || 
                         (str == '#') || 
                         (str == '$') || 
                         (str == '%') || 
                         (str == ':') || 
                         (str == '<') || 
                         (str == '>') || 
                         (str == '(') || 
                         (str == ')') || 
                         (str == '^'));
        
        return (invalidChar);
    }
    
    function toascii (c){
	c = c . charAt (0);
	var i;
	for (i = 0; i < 256; ++ i)
	{
		var h = i . toString (16);
		if (h . length == 1)
			h = "0" + h;
		h = "%" + h;
		h = unescape (h);

		if (h == c)
			break;
	}
	return i;
}
    
     function requestHasInvalidParameters(str){
       
        var invalidChars = [];
        invalidChars [0] = "<script>";
		invalidChars [1] = "javascript:";
        
            for( var i=0; i<invalidChars.length; i++ )
            {
                if ( str.indexOf(invalidChars[i]) != -1 )
                {
                    return true;                
                }
            }
          
        return false;
    }
    
    function validateAccessCodeSpecialChar(hasBreak) {
        var validStatus = true;
	    
	    if(!hasBreak) {
	      	var val = document.getElementById("aCode").value;
		    validStatus = validAccessCodeNameString(trim(val));
	        
	    } else {
	      	for(var i=0;i<subtestLength;i++){	
		      	var val = document.getElementById("aCodeB"+i).value;
		      	validStatus = validAccessCodeNameString(trim(val));
		        if(!validStatus)
		        	break;
			}
			 
			if(validStatus && locatorSubtest!=null && locatorSubtest!=undefined && locatorSubtest.id != undefined ){
				var locator = document.getElementById("hasAutolocator");
				if(locator.checked != null && locator.checked !=undefined && locator.checked){	
					var lAccessCode = $("#"+locatorSubtest.id+" input").val();
					validStatus = validAccessCodeNameString(trim(lAccessCode));	
					
				}
			}
			
			
	    }
    	return validStatus;
    }
    
     function validAccessCodeNameString(str){
        var characters = [];
        characters = toCharArray(str);
        for (var i=0 ; i<characters.length ; i++) {
            var character = characters[i];
            if (! validAccessCodeNameCharacter(character))
                return false;
        }
        return !requestHasInvalidParameters(str);
    }
     
     function validAccessCodeNameCharacter(str){
        var ch = toascii(str);
        var A_Z = ((ch >= 65) && (ch <= 90));
        var a_z = ((ch >= 97) && (ch <= 122));
        var zero_nine = ((ch >= 48) && (ch <= 57));
        return (zero_nine || A_Z || a_z );
    }
    
    function validateIdenticalTestAccessCodes () {
    	var accessCodes=new Array();
    	var validStatus = true;
    	for(var i=0;i<subtestLength;i++){
			accessCodes[i]= document.getElementById("aCodeB"+i).value.toLowerCase();
		}
		
		if(validStatus && locatorSubtest!=null && locatorSubtest!=undefined && locatorSubtest.id != undefined ){
			var locator = document.getElementById("hasAutolocator");
			if(locator.checked != null && locator.checked !=undefined && locator.checked){	
				var lAccessCode = $("#"+locatorSubtest.id+" input").val();
				var arraylength = accessCodes.length + 1;
				accessCodes[arraylength]=  lAccessCode.toLowerCase();
				
			}
		}
		
		
		var sorted_arr = accessCodes.sort(); 
		for (var i = 0; i < accessCodes.length - 1; i++) {
			if (sorted_arr[i + 1] == sorted_arr[i]) {
		    	validStatus = false; 
		        break;   
		 	} 
		} 
    	return validStatus;
    }
    
    function validateMissingTestAccessCode ( ) {
    	 var accessCode = document.getElementById("aCode").value;
    	 var validStatus = true;
    	if(trim(accessCode)==""){
    		validStatus = false;
    	} 
    	return validStatus;
    }
    
    function validateMissingTestAccessCodes ( ) {
    	var validStatus = true;
    	for(var i=0;i<subtestLength;i++){
			if(trim(document.getElementById("aCodeB"+i).value) == "" ){
				validStatus = false;
				break;
			} 
		}
		
		if(validStatus && locatorSubtest!=null && locatorSubtest!=undefined && locatorSubtest.id != undefined ){
			var locator = document.getElementById("hasAutolocator");
			if(locator.checked != null && locator.checked !=undefined && locator.checked){	
				var lAccessCode = $("#"+locatorSubtest.id+" input").val();
				if(trim(lAccessCode) == "" ){
					validStatus = false;
			} 	
				
			}
		}
		return validStatus;
    }
    
     function validateAccessCodeMinLength ( hasBreak ) {
    	var validStatus = true;
    	var accessCode = trim(document.getElementById("aCode").value);
    	if (!hasBreak) {
    		if(accessCode.length<6) {
    			validStatus = false;
    		}
    	} else {
    		for(var i=0;i<subtestLength;i++){
				if(trim(document.getElementById("aCodeB"+i).value).length<6){
					validStatus = false;
					break;
				} 
			}
			
			if(validStatus && locatorSubtest!=null && locatorSubtest!=undefined && locatorSubtest.id != undefined ){
				var locator = document.getElementById("hasAutolocator");
				if(locator.checked != null && locator.checked !=undefined && locator.checked){	
					var lAccessCode = $("#"+locatorSubtest.id+" input").val();
					if(trim(lAccessCode).length<6 ){
						validStatus = false;
				} 	
					
				}
			
			}
    	}
    	
		return validStatus;
    }
    
  // ----------------- Added for Proctor changes ---------------------//
  
  function processProctorAccordion() {
		if(!proctorGridloaded) {
   			populateSelectedProctor();
   		} else {
   			gridReloadProctor(true);
   		}
   		
   		var width = jQuery("#scheduleSession").width();
    	width = width - 72; // Fudge factor to prevent horizontal scrollbars
    
		jQuery("#listProctor").setGridWidth(width,true);
	}
	     
    function gridReloadSelectProctor(){ 
	     
	      var postDataObject = {};
 		  postDataObject.q = 2;
 		  postDataObject.proctorOrgNodeId = $("#proctorOrgNodeId").val();
 		  postDataObject.isOKEqFormAdmin = isOKAdmin && isOKEqTestSelected; // Added for Oklahoma customer
	      jQuery("#selectProctor").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});    
	      var urlVal = 'getProctorList.do'; 
     	  jQuery("#selectProctor").jqGrid('setGridParam', {url:urlVal,postData:postDataObject,page:1}).trigger("reloadGrid");
          jQuery("#selectProctor").sortGrid('lastName',true,'asc');

    }
     
     function gridReloadProctor(addProctor){ 
      	$('#listProctor').GridUnload();	
      	populateSelectedProctor();
      	
     }
	
    
  	function populateSelectedProctor() {
 		UIBlock();
 		
 		var schedulerFirstName = $("#schedulerFirstName").val();
		var schedulerLastName = $("#schedulerLastName").val();
		var schedulerUserId = $("#schedulerUserId").val();
		var schedulerUserName = $("#schedulerUserName").val();
 		
 		proctorGridloaded = true;
		var productSelected = $("#testGroupList").val();
		if(productSelected == '9003') {
			isOKEqTestSelected = true;
		} else {
			isOKEqTestSelected = false;
		}
 		if(state != "EDIT" && noOfProctorAdded == 0) {
	 		var jsondata = {};
	 		jsondata['userId'] = schedulerUserId;
	 		jsondata['lastName'] = schedulerLastName;
	 		jsondata['firstName'] = schedulerFirstName;
	 		jsondata['defaultScheduler'] = "T";
	 		jsondata['userName'] = schedulerUserName;
	 		
	 		var val=[] ;
		 	val.push(jsondata);
		    proctorIdObjArray = {};	
		    proctorIdObjArray[schedulerUserId] = jsondata;	 	
		 	addProctorLocaldata = val;
	 		
	 		//Changes for Oklahoma customer
	 		productSelected = $("#testGroupList").val();
	 	//alert(JSON.stringify(proctorIdObjArray));	
		 	
	 	}else if(state == "EDIT"){
	 		var val=[] ;
			
			val = val.concat(addProctorLocaldata);
			//added for copy test session	
			if(isCopySession){
				schedulerFirstName = $("#loggedInFirstName").val();
				schedulerLastName = $("#loggedInLastName").val();
				schedulerUserId = $("#loggedInUserId").val();
				schedulerUserName = $("#loggedInUserName").val();
				$("#schedulerFirstName").val(schedulerFirstName);
				$("#schedulerLastName").val(schedulerLastName);
				$("#schedulerUserId").val(schedulerUserId);
				$("#schedulerUserName").val(schedulerUserName);
				for (var i = 0, j = val.length; i < j; i++){
				 	proctorIdObjArray[val[i].userId] = val[i];
				} 		
			}else{
				for (var i = 0, j = val.length; i < j; i++) {
					proctorIdObjArray[val[i].userId] = val[i];
					if(proctorIdObjArray[val[i].userId].defaultScheduler == "T") {
						schedulerFirstName = proctorIdObjArray[val[i].userId].firstName;
						schedulerLastName = proctorIdObjArray[val[i].userId].lastName;
						schedulerUserId = proctorIdObjArray[val[i].userId].userId;
						schedulerUserName = proctorIdObjArray[val[i].userId].userName;
						$("#schedulerFirstName").val(schedulerFirstName);
						$("#schedulerLastName").val(schedulerLastName);
						$("#schedulerUserId").val(schedulerUserId);
						$("#schedulerUserName").val(schedulerUserName);
				 	}
				}
			}
			 addProctorLocaldata = val;
		 }
	 	
	 	noOfProctorAdded = addProctorLocaldata.length;
		$("#totalAssignedProctors").text(noOfProctorAdded);
	 	
	 	//$("#testSchedulerId").text(schedulerFirstName + ' ' + schedulerLastName);
	 	 		
 		$("#listProctor").jqGrid({         
         data:  addProctorLocaldata,
		 datatype: "local",         
          colNames:[ $("#testStuLN").val(),$("#testStuFN").val(),'Default Scheduler','User Id','Editable'],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:130, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'firstName',index:'firstName', width:130, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'defaultScheduler',index:'defaultScheduler', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'userId',index:'userId', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'editable',index:'editable', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"rows", id:"userId", records: function(obj) {} },
		   	//jsonReader: { repeatitems : false, root:"userProfileInformation", id:"userId", records: function(obj) { userList = JSON.stringify(obj.userProfileInformation);return obj.userProfileInformation.length; } },
		   	
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:true,
			pager: '#pagerProctor', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 162,  
			caption:$("#procListGrid").val(),
			onPaging: function() {
				$("#proctorAddDeleteInfo").hide();
				var reqestedPage = parseInt($('#listProctor').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_pagerProctor').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#listProctor').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#listProctor').setGridParam({"page": minPageSize});
				}
			
			},
			gridComplete: function() {
					onloadProctorListGrid = true;
					var allRowsInGrid = $('#listProctor').jqGrid('getDataIDs');
					var selectedRowData;
					var loggedInUserId = $("#loggedInUserId").val();
					for(var i = 0; i < allRowsInGrid.length; i++) {
						selectedRowData = $("#listProctor").getRowData(allRowsInGrid[i]);
						//added for copy test session
						if(isCopySession){
							if ((selectedRowData.editable == "F") || (selectedRowData.userId == loggedInUserId)) {
								$("#"+allRowsInGrid[i]+" td input","#listProctor").attr("disabled", true);
					 			$("#"+allRowsInGrid[i], "#listProctor").addClass('ui-state-disabled');
					 		//	$("#listProctor").jqGrid('editRow',allRowsInGrid[i],false);
					 		}else {
					 			if(!allProctorSelected && delProctorIdObjArray[selectedRowData.userId]){
					 				$("#jqg_listProctor_"+ parseInt(i+1)).attr('checked', true).trigger('click').attr('checked', true);
					 			}
					 		}
						} else{
							if (selectedRowData.defaultScheduler == 'T' || (selectedRowData.editable == "F") || (selectedRowData.userId == loggedInUserId)) {
								$("#"+allRowsInGrid[i]+" td input","#listProctor").attr("disabled", true);
					 			$("#"+allRowsInGrid[i], "#listProctor").addClass('ui-state-disabled');
					 		//	$("#listProctor").jqGrid('editRow',allRowsInGrid[i],false);
					 		}else {
					 			if(!allProctorSelected && delProctorIdObjArray[selectedRowData.userId]){
					 				$("#jqg_listProctor_"+ parseInt(i+1)).attr('checked', true).trigger('click').attr('checked', true);
					 			}
					 		}
				 		}
					}
					if(allProctorSelected){
						$("#cb_listProctor").attr('checked', true).trigger('click').attr('checked', true);
					}
					onloadProctorListGrid = false;	
			},
			
			onSelectAll: function (rowids, status) {
				$("#proctorAddDeleteInfo").hide();
				if(!onloadProctorListGrid){
					var loggedInUserId = $("#loggedInUserId").val();
					if(status) {
						var tmpselectedRowId = "";
						selectAllForDeleteProctor = true;
						allProctorSelected = true;
						
						var selectedRowData;
						for(var i = 0; i < addProctorLocaldata.length; i++) {						
							selectedRowData = addProctorLocaldata[i];
							if(isCopySession){
								if (selectedRowData.editable == "T") {
									if(selectedRowData.userId != loggedInUserId){
										selectedRowData = addProctorLocaldata[i];
										$("#"+selectedRowData.userId+" td input").attr("checked", true); 
										
										tmpselectedRowId = selectedRowData.userId;
							 			delProctorIdObjArray[selectedRowData.userId]=selectedRowData;
						 			}
						 			
						 		}
							}else{
								if (selectedRowData.defaultScheduler == 'F' && selectedRowData.editable == "T") {
									if(selectedRowData.userId != loggedInUserId){
										selectedRowData = addProctorLocaldata[i];
										$("#"+selectedRowData.userId+" td input").attr("checked", true); 
										
										tmpselectedRowId = selectedRowData.userId;
							 			delProctorIdObjArray[selectedRowData.userId]=selectedRowData;
						 			}
						 			
						 		}
					 		}
						}
						proctorSelectedLength = addProctorLocaldata.length;
					} else {				
						var tmpselectedRowId = "";
						selectAllForDeleteProctor = false;
						allProctorSelected= false;
						var selectedRowData;
						for(var i = 0; i < addProctorLocaldata.length; i++) {
							
							selectedRowData = addProctorLocaldata[i];
							if (selectedRowData.defaultScheduler == 'F' && selectedRowData.editable == "T") {
								if(selectedRowData.userId != loggedInUserId){
									selectedRowData = addProctorLocaldata[i];
									$("#"+selectedRowData.userId+" td input").attr("checked", false); 
									
									tmpselectedRowId = selectedRowData.userId;
									delete delProctorIdObjArray[selectedRowData.userId];
								}
					 		}
						}
						proctorSelectedLength = 1;
					} 
				}	
				
			}, 
			onSelectRow: function (rowid, status) {
				$("#proctorAddDeleteInfo").hide();
				if(!onloadProctorListGrid){
					var tmpselectedRowId = "";
					selectAllForDeleteProctor = false;
					var selectedRowData = $("#listProctor").getRowData(rowid);
					tmpselectedRowId = selectedRowData.userId;
					
					if(status) {					
						delProctorIdObjArray[tmpselectedRowId]=selectedRowData;	
						proctorSelectedLength = parseInt(proctorSelectedLength) + 1;
						if(parseInt(noOfProctorAdded) == proctorSelectedLength){
							allProctorSelected = true;
						}
						if(allProctorSelected){
							$("#cb_listProctor").attr('checked', true).trigger('click').attr('checked', true);
						}				
					} else {
						proctorSelectedLength = parseInt(proctorSelectedLength) - 1;
						allProctorSelected	= false;	
						$("#cb_listProctor").attr('checked', false);		
						delete delProctorIdObjArray[tmpselectedRowId];
						
					} 
				}
				
			},
			loadComplete: function () {
				if ($('#listProctor').getGridParam('records') === 0) {
					isPAGridEmpty = true;
            		$('#sp_1_pagerProctor').text("1");
            		$('#next_pagerProctor').addClass('ui-state-disabled');
            	 	$('#last_pagerProctor').addClass('ui-state-disabled');
            	} else {
            		isPAGridEmpty = false;
            	}
            	//setEmptyListMessage('PA');
            	$.unblockUI();  
				$("#listProctor").setGridParam({datatype:'local'});
				var tdList = ("#pagerProctor_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				var width = jQuery("#scheduleSession").width();
		    	width = width - 72; // Fudge factor to prevent horizontal scrollbars
		    	$("#listProctor").jqGrid("hideCol","defaultScheduler");
		    	$("#listProctor").jqGrid("hideCol","userId");
		    	$("#listProctor").jqGrid("hideCol","editable");
		    	
		    	jQuery("#listProctor").setGridWidth(width,true);
		    	
		    	$("#testSchedulerId").text(schedulerFirstName + ' ' + schedulerLastName);
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#listProctor").navGrid('#pagerProctor',{del:false,add:false,edit:false,view:false,search:false,refresh:false}
	 ).jqGrid('navButtonAdd',"#pagerProctor",{
			    caption:"", buttonicon:"ui-icon-trash", onClickButton:function(){
			    	if(isEmpty(delProctorIdObjArray)){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 220,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		removeProctorConfirmationPopup();	
			    	}
			    	
			    }, position: "first", title:"Remove Proctor", cursor: "pointer",id:"del_listProctor"
			});
		
		if(state == "EDIT" && isProctor) {
			var element = document.getElementById('del_listProctor');
			element.style.display = 'none'; 
		}
		if(isTestExpired){		    	 
          	$("#del_listProctor").addClass('ui-state-disabled');	
        }else{
        	$("#del_listProctor").removeClass('ui-state-disabled');	
        }
        if(hideProctorDelButton || isProctor) {
        	$("#del_listProctor").hide();
        } else {
        	$("#del_listProctor").show();
        }
	}	
	
	function addAllProcsForOklahoma(schedulerUserId) {
		var param = {};
		var jsondata = {};
		var val = [];
		val = addProctorLocaldata;
	 		param.userId = 	schedulerUserId; // Assuming that only state can schedule EQ test.
	 			
	 		$.ajax({
			async:		false,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'getAllBelowLevelUsers.do',
			type:		'POST',
			data:		 param,
			dataType:	'json',
			success:	function(vdata, textStatus, XMLHttpRequest){
			
						   if(vdata != null && vdata != undefined && vdata.length > 0) {
						   		for(var i = 0; i < vdata.length; i++) {
						   			// Previously added proctors should be taken care of
						   			if(proctorIdObjArray[vdata[i].userId] == null || proctorIdObjArray[vdata[i].userId] == undefined) {
							   			jsondata = {};
							   			jsondata['userId'] = vdata[i].userId;
		 								jsondata['lastName'] = vdata[i].lastName;
		 								jsondata['firstName'] = vdata[i].firstName;
		 								jsondata['defaultScheduler'] = vdata[i].defaultScheduler;
		 								jsondata['userName'] = vdata[i].userName;
		 								jsondata['editable'] = 'T'; // As per new requirement, need not make them disabled
		 								val.push(jsondata);
		 								proctorIdObjArray[vdata[i].userId] = jsondata;
	 								}
						   		}
						   		addProctorLocaldata = val;
						   }
						   
						   
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
	
	function removeSelectedProctor() {
	var deletedProcCount = 0;
	var message = "";
		for(var i in delProctorIdObjArray) {		
			delete proctorIdObjArray[i];
			delete tempProctorData[i];
			for(var count=0;count<addProctorLocaldata.length;count++){
				if(i == addProctorLocaldata[count].userId){
					addProctorLocaldata.splice(count,1);
				}
			}
			deletedProcCount++;
			proctorSelectedLength = parseInt(proctorSelectedLength) - 1;
		}
		for(var j in allSelectOrgProctor){
			allSelectOrgProctor[j] = false;
			if(tempAllSelectOrgProctor[j]){
				tempAllSelectOrgProctor[j] = false;
			}
		}
		
		if (allProctorSelected && deletedProcCount > 0){
			message = deletedProcCount + "  " + $("#procDelAllMsg").val();
		}else {
			message = deletedProcCount + "  " + $("#procDelMsg").val();
		}
		noOfProctorAdded = addProctorLocaldata.length;
		allProctorSelected	= false;	
		$("#cb_listProctor").attr('checked', false);
		//$('#totalAssignedProctors').text(noOfProctorAdded);
		closePopUpForProctor('removeProctorConfirmationPopup');
		delProctorIdObjArray = {};
		gridReloadProctor(false);
		$("#proctorAddDeleteInfo").show();
		$("#addDeleteProc").text(message);	
	}
	
	function removeProctorConfirmationPopup(){
	$("#removeProctorConfirmationPopup").dialog({  
		title:$("#confirmAlrt").val(),  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '400px',
	 	modal: true,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 $("#removeProctorConfirmationPopup").css('height',120);
		 var toppos = ($(window).height() - 290) /2 + 'px';
		 var leftpos = ($(window).width() - 410) /2 + 'px';
		 $("#removeProctorConfirmationPopup").parent().css("top",toppos);
		 $("#removeProctorConfirmationPopup").parent().css("left",leftpos);	
		 
	}
	function closeScheduleSessionPopup() {
	    closePopUp('closeScheduleSessionPopup');
		closePopUp('scheduleSession');
	
	}
	 
	function openCloseScheduleSessionPopup(){
		$("#closeScheduleSessionPopup").dialog({  
		title:$("#confirmAlrt").val(),  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '400px',
	 	modal: true,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 $("#closeScheduleSessionPopup").css('height',120);
		 var toppos = ($(window).height() - 290) /2 + 'px';
		 var leftpos = ($(window).width() - 410) /2 + 'px';
		 $("#closeScheduleSessionPopup").parent().css("top",toppos);
		 $("#closeScheduleSessionPopup").parent().css("left",leftpos);	
		 
	}
    
    function onCloseScheduleSessionPopUp() {    	
    	var sessionName = document.getElementById("testSessionName").value;	
    	var selectedId1 = $("#list2").jqGrid('getGridParam', 'selrow');
		if(selectedId1 != null && $.trim(selectedId1) != '') {
			setAnchorButtonState('viewStatusButton', false);
			setAnchorButtonState('printTicketButton', false);
		}	
		if (state == "EDIT"){
			if (onChangeHandler.getData() == "T"){
				openCloseScheduleSessionPopup();
			}else{
				closePopUp('scheduleSession');
			}
    	}else{		
	    	if( sessionName!= null && $.trim(sessionName).length == 0 ){
	    		closePopUp('scheduleSession');
	    	} else {
	    		openCloseScheduleSessionPopup();
	    	}
    	}
    }
    
    function saveTest(checkRestricted) {
        
        $('#displayMessage').hide();
	    $('#showSaveTestMessage').hide();
	    
		if (isSelectingStudent) {
			setMessage("Action required commit", "", "errorMessage", "Click OK to commit adding students to continue.");       
			$('#displayMessage').show();
			return false;
		}
	    
	    var param;
   	 if(state != "EDIT"){
		
		var param1 =$("#testDiv *").serialize(); 
	    var param2 = $("#Test_Detail *").serialize();
	    var time = document.getElementById("time").innerHTML;
	    var timeArray = time.split("-");
	    param = param1+"&"+param2+"&startTime="+$.trim(timeArray[0])+"&endTime="+$.trim(timeArray[1]);
	    var selectedstudent = getStudentListArray(AddStudentLocaldata);
	    param = param+"&students="+selectedstudent.toString();

	    var selectedProctors =getProctorListArray(addProctorLocaldata);
	    param = param+"&proctors="+selectedProctors.toString();
	    param = param+"&action=ADD";
	 } else {
	 		$("#timeZoneList").removeAttr("disabled");
		    var param1 =$("#testDiv *").serialize(); 
		    param1 = param1 +serializeDisabledFieldFromDiv("testDiv");
		    
		    var param2 = $("#Test_Detail *").serialize();
		    param1 = param1 +serializeDisabledFieldFromDiv("Test_Detail");
		     
		    var time = document.getElementById("time").innerHTML;
		    var timeArray = time.split("-");
		    param = param1+"&"+param2+"&startTime="+$.trim(timeArray[0])+"&endTime="+$.trim(timeArray[1]);
		    var selectedstudent = getStudentListArray(AddStudentLocaldata);
		    param = param+"&students="+selectedstudent.toString();
		    
		    var selectedProctors =getProctorListArray(addProctorLocaldata);
		    param = param+"&proctors="+selectedProctors.toString() ;
		    //added for copy test session
		    if(isCopySession){
		    	param = param+"&action=COPY";
		    }else{
		    	param = param+"&action=EDIT";
		    }
		    //
		   	param = param+"&isStudentUpdated="+isStdDetClicked;
		   	param = param+"&isProctorUpdated="+isProcDetClicked ;
		   	param = param+"&testAdminId=" +selectedTestAdminId;
		   	param = param+"&isEndTestSession="+isEndTestSession;
		   	param = param+"&isSelectTestUpdated="+isSelectTestDetClicked;
	 }
	 var selectedId1 = $("#list2").jqGrid('getGridParam', 'selrow');
	 if(selectedId1 != null && $.trim(selectedId1) != '') {
	 		setAnchorButtonState('viewStatusButton', false);
			setAnchorButtonState('printTicketButton', false);
	 }
	 param = param+"&randomDis="+$('#randomDis').val();
	 param = param+"&checkRestricted="+checkRestricted;
	 if (selectGE != null) {
	 	if (document.getElementById("selectGE").checked)
	 		param = param+"&includeGE=GE-Yes";
	 	else
	 		param = param+"&includeGE=GE-No";
	 }
	 
		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'saveTest.do',
			type:		'POST',
			data:		 param,
			dataType:	'json',
			success:	function(vdata, textStatus, XMLHttpRequest){
						   
						   var data = vdata.status;
						   
						   var successMessage;
						   var key;
						   var messageHeader;
						   var messageArray;
						   var length = 0;
						   
						  if(data.isSuccess){
						  	successMessage   = data.successMessage;
							key 		     = data.successInfo.key;
							messageHeader 	 = data.successInfo.messageHeader;
							messageArray     = data.successInfo.message;
						  } else if(vdata.restrictedStudents == undefined){
						  	key 		    = data.validationFailedInfo.key;
							messageHeader 	= data.validationFailedInfo.messageHeader;
							messageArray    = data.validationFailedInfo.message;
						  }
						  if(messageArray!=undefined){
								   	length= messageArray.length;
						  }
						   
						  if(data.isSuccess){
								if(length==0) {
								   setSessionSaveMessage(messageHeader, "", "infoMessage","");
								   	$('#displayMessage').show(); 
								} else if (length==1) {
								   	setSessionSaveMessage(messageHeader, messageArray[0], "infoMessage","");
								   	$('#displayMessage').show(); 
								} else  {
								   	setSessionSaveMessage(messageHeader,  messageArray[0], "infoMessage", messageArray[1]);
								   	$('#displayMessage').show(); 
								 } 
								 if(state == "EDIT"){
									var updatedPrType = $("#productType").val(); 
									var updatedSessionName = $("#testSessionName").val();
									var oplist = $("#testGroupList option");
									var oplistval = $("#testGroupList").val();
									var updatedTestName = $($('#testGroupList option[value='+oplistval+']')).text()
									reInitializeModifyTestPopup(updatedPrType, updatedTestName, updatedSessionName);
								 }
								var sortCol = jQuery("#list2").getGridParam("sortname");
								var sortCol1 = jQuery("#list3").getGridParam("sortname");
								if(vdata.testSession != null) {
							  		var testSessionData = vdata.testSession;
							  		
							  		var assignedRole = testSessionData.AssignedRole;
								 	if ((state == "EDIT") && (savedAssignedRole != undefined)) {
								 		assignedRole = savedAssignedRole;
								 	}
							  		
							  		var dataToBeAdded = {testAdminName:testSessionData.testAdminName,
							  							 testName:testSessionData.testName,
							  							 creatorOrgNodeName:testSessionData.creatorOrgNodeName,
							  							 creatorOrgNodeId:testSessionData.creatorOrgNodeId,
							  							 AssignedRole:assignedRole,
							  							 loginStartDate:$.datepicker.formatDate('mm/dd/y', new Date(testSessionData.loginStartDate)),
							  							 loginEndDate:$.datepicker.formatDate('mm/dd/y', new Date(testSessionData.loginEndDate)),
							  							 loginStartDateString:testSessionData.loginStartDateString,
							  							 loginEndDateString:testSessionData.loginEndDateString,
							  							 isSTabeProduct:testSessionData.isSTabeProduct,
							  							 isSTabeAdaptiveProduct:testSessionData.isSTabeAdaptiveProduct,
							  							 productType:testSessionData.productType,
							  							 copyable: testSessionData.copyable};
							  		if(state == "EDIT") {
							  			if(testSessionData.testAdminStatus == 'CU' || testSessionData.testAdminStatus == 'FU') {
							  				if(isCopySession) {
							  				
								  				jQuery("#list2").addRowData(testSessionData.testAdminId, dataToBeAdded, "first");
								  				jQuery("#list2").sortGrid(sortCol, true);
								  			} else if(!isCopySession 
								  				&& jQuery("#list2").getInd(testSessionData.testAdminId) > 0
								  				&& !jQuery("#list3").getInd(testSessionData.testAdminId)) {
								  				
								  				jQuery("#list2").setRowData(testSessionData.testAdminId, dataToBeAdded, "first");
								  				jQuery("#list2").sortGrid(sortCol, true);
								  			} else if(!isCopySession 
								  				&& jQuery("#list3").getInd(testSessionData.testAdminId) > 0
								  				&& !jQuery("#list2").getInd(testSessionData.testAdminId)) {
								  				
								  				jQuery("#list3").delRowData(testSessionData.testAdminId);
								  				jQuery("#list2").addRowData(testSessionData.testAdminId, dataToBeAdded, "first");
								  				setAnchorButtonState('viewStatusButton', true);
												setAnchorButtonState('printTicketButton', true);
	 											setAnchorButtonState('profileReportSessionButton', true);
												updateModifyStdManifestButton(false);
												updateCopySessionButton(false);
								  			}
							  			} else if(testSessionData.testAdminStatus == 'PA') {
							  				
							  				if(jQuery("#list3").getInd(testSessionData.testAdminId) > 0) {
								  				
								  				jQuery("#list3").setRowData(testSessionData.testAdminId, dataToBeAdded, "first");
								  				jQuery("#list3").sortGrid(sortCol1, true);
								  			} else if(jQuery("#list2").getInd(testSessionData.testAdminId) > 0) {
								  			
								  				jQuery("#list2").delRowData(testSessionData.testAdminId);
								  				jQuery("#list3").addRowData(testSessionData.testAdminId, dataToBeAdded, "first");
								  				setAnchorButtonState('viewStatusButton', true);
												setAnchorButtonState('printTicketButton', true);
	 											setAnchorButtonState('profileReportSessionButton', true);
												updateModifyStdManifestButton(false);
												updateCopySessionButton(false);
								  			}
							  			}
							  		} else {
							  			jQuery("#list2").addRowData(testSessionData.testAdminId, dataToBeAdded, "first");
							  			jQuery("#list2").sortGrid(sortCol, true);
								  	}
							  	}
							  	$('#showSaveTestMessage').show();
							  	$.unblockUI();
							  	closePopUp("scheduleSession");
						  } else if (data.IsSystemError) {
								if(length==0) {
									setSessionSaveMessage(messageHeader, "", "errorMessage","");
									$('#displayMessage').show(); 
								} else if (length==1) {
									setSessionSaveMessage(messageHeader, messageArray[0], "errorMessage","");
									$('#displayMessage').show(); 
								} else  {
									setSessionSaveMessage(messageHeader,  messageArray[0], "errorMessage", messageArray[1]);
									$('#displayMessage').show(); 
								}
								$.unblockUI();
								$('#showSaveTestMessage').show();
							  	closePopUp("scheduleSession");
					  	} else if(!data.isSuccess && !data.IsSystemError && vdata.restrictedStudents != undefined) {
					  		openRestrictedStudentPopUp(vdata.restrictedStudents, vdata.totalStudent);
					  	
					  	}else {
								if(length==0) {
									setMessage(messageHeader, "", "errorMessage","");
									$('#displayMessage').show(); 
								} else if (length==1) {
									setMessage(messageHeader, messageArray[0], "errorMessage","");
									$('#displayMessage').show(); 
								} else  {
									setMessage(messageHeader,  messageArray[0], "errorMessage", messageArray[1]);
									$('#displayMessage').show(); 
								} 
							  
					  	}
					  	},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();
							window.location.href="/SessionWeb/logout.do";
							
						},
			complete :  function(){
							 $.unblockUI(); 
						}
		});
		//}
		//else{
		//	saveEditTestData();
		//}
	}
	
	function formatJSONDate(jsonDate){
	    var newDate = dateFormat(jsonDate, "mm/dd/yyyy");
	    return newDate;
	
	}
	
	function compareDate(jsonDate){
	    var newDate = $.datepicker.formatDate('mm/dd/y', new Date(jsonDate));
	    var today = $.datepicker.formatDate('mm/dd/y', new Date());
	    if(newDate >= today) {
	    	return true;
	    }
	    return false;
	
	}
	
	function toggleRandomDisVal() {
			var randomDisVal = document.getElementById("randomDis");
			if(!randomDisVal.checked){		
				$("#randomDis").val("N"); 
			} else {
				$("#randomDis").val("Y"); 
			}
	}
	
	function setSessionSaveMessage(title, content, type, message){
		if(title != ""){
			$("#saveTestTitle").html(title);
			$("#saveTestTitle").show();
		}else{
			$("#saveTestTitle").html("");
			$("#saveTestTitle").hide();
		}
		
		if(content != ""){
			$("#saveTestContent").html(title);
			$("#saveTestContent").show();
		}else{
			$("#saveTestContent").html("");
			$("#saveTestContent").hide();
		}
		
		if(message != ""){
			$("#saveTestMessage").html(title);
			$("#saveTestMessage").show();
		}else{
			$("#saveTestMessage").html("");
			$("#saveTestMessage").hide();
		}
			if(type=="errorMessage"){
				$('#errorIcon').show();
				$('#infoIcon').hide();
			} else {
				$('#errorIcon').hide();
				$('#infoIcon').show();
			}
	}
	
	
	function trimTextValue(element){
	       element.value=$.trim(element.value);
	}

	function  resetOnSelectTestSessionData() {
		if(state != "EDIT"){
			document.getElementById("testSessionName").value = "";
			document.getElementById("startDate").value = "";			
			document.getElementById("endDate").value = "";			
			document.getElementById("time").innerHTML = "8:00 AM - 5:00 PM";
			document.getElementById("endDate").value = "";
			document.getElementById("aCode").value = "";
			isTestSelected = false;
			document.getElementById("testBreak").checked = false;
	    	isTestBreak = false;
		    $("#hasTestBreak").val("F");
		    document.getElementById("sData").disabled=true;
	    }
	    offGradeCancled = false;
	    offGradeSubtestChanged = false;
	}
	
	function closePopUpForProctor(dailogId){
 		if(dailogId == 'removeProctorConfirmationPopup') {
			$("#"+dailogId).dialog("close");
		}
		
	}

	function resetStudentSelection() {
		studentMap = new Map();
		studentTempMap = new Map();
		allStudentIds = [];
		allSelectOrg = [];
		countAllSelect = 0;
		AddStudentLocaldata = [];
		studentWithaccommodation = 0;
	}
    
    function subtestChangeProcess() {
    	$("#selectedTestId").val(selectedTestId);					
		var testBreak = document.getElementById("testBreak");
		isTestSelected = true;
		if(testBreak.checked)testBreak.checked = false;
		isTestBreak = false;
		$("#hasTestBreak").val("F");
		//document.getElementById("aCode").style.display = "none";
		//populateDates();
		 selectedAccessCodeMap = new Map();
		subtestDataArr = testJSONValue;
		allSubtests    = getCopy(testJSONValue);
		selectedSubtests = testJSONValue;
		
		allSubtestMap = new Map();
		selectedSubtestsMap = new Map();
		populateAllSubtestMap( testJSONValue);
		populateSelectedSubtestsMap (testJSONValue);
		
		if(subtestDataArr!= undefined && subtestDataArr.length>1){
			document.getElementById("testBreak").disabled=false;
		} else {
			document.getElementById("testBreak").disabled=true;
		}						
		createSubtestGrid();
		$("#selectedNewTestId").val(selectedTestId);
		$("#sData").removeClass("ui-state-disabled");
		document.getElementById("sData").disabled=false;
    }
	
   /******Jstree Methods*****/
	//method triggered from library
	  function customLoad(){
	  if(!isPrintTicket){
	    	pushInsideElement(currentNodeId,currentCategoryLevel,dataObj2,currentTreeArray);
	    }else{
	    	pushInsideElement(currentNodeIdPrint,currentCategoryLevelPrint,dataObj2Print,currentTreeArrayPrint);
	    }
	  }
  
    function pushInsideElement(currentNodeId,currentCategoryLevel,dataObj2,currentTreeArray){
		var objArray;
		switch(currentCategoryLevel){
		case "1": objArray = dataObj2;				  
				break;
		case "2": objArray = currentTreeArray.children;
				break;
				
		case "3": objArray = currentTreeArray.children;
				break;
		case "4": objArray = currentTreeArray.children;
				break;
		case "5": objArray = currentTreeArray.children;
				break;
		case "6": objArray = currentTreeArray.children;
				break;
		case "7": objArray = currentTreeArray.children;
				break;
		case "8": objArray = currentTreeArray.children;
				break;
		}
		
		
		updateTree(objArray,currentNodeId);	
	  }
	  function updateTree(objArray,currentNodeId){
		
	
	  	var currentElement;	
	  	if (isPrintTicket){
			currentElement = $('#'+currentNodeId, '#'+typePrint);
		}else{
			currentElement = $('#'+currentNodeId, '#'+type);
		}		
		currentElement = currentElement[0];
		currentElement.className = "jstree-open jstree-unchecked";
		var fragment = document.createDocumentFragment();
		var ulElement = document.createElement('ul');

		stream(objArray,ulElement,fragment,streamPush, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading');
			//if(type == 'stuOrgNodeHierarchy') {
			//$('#stuOrgNodeHierarchy').jstree('open_node', "#"+currentNodeId);
			//}
		 });

	  }
 
	  // tree population.	  
	  function streamPush(objArray,ulElement,fragment){
	  	var liElement = document.createElement('li');
		liElement.setAttribute("id", objArray.attr.id);
		liElement.setAttribute("cid" , objArray.attr.cid);
		liElement.setAttribute("tcl" , objArray.attr.tcl);
		var condition = objArray.attr.chlen;
		
		switch(condition){
		case false: liElement.className = "jstree-leaf jstree-unchecked";
					break;
		case true:  liElement.className = "jstree-closed jstree-unchecked";
					break;
		case undefined: var con = objArray.hasOwnProperty("children");
							switch(con){
								case false: liElement.className = "jstree-leaf jstree-unchecked";
											break;
								case true:  liElement.className = "jstree-closed jstree-unchecked";
											break;						
							}
						  break;
					}
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		ulElement.appendChild(liElement);
		fragment.appendChild(ulElement);
	  }

  	function populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot){	
	//TODO : Updation in root node is a problem need to work on that but this method is also necessary for populating the immediate //tree because if we use the cache all the 2nd level objects will be stored in cache which makes the cache very heavy
		var jsonObject = jsonData;
		jsonObject = jsonObject[indexOfRoot];
		if (dataObj2.length == 0 && jsonObject != undefined){
			for (var i = 0, j = jsonObject.children.length; i < j; i++ ){
					dataObj2.push({data: jsonObject.children[i].data,attr:{id: jsonObject.children[i].attr.id,cid:jsonObject.children[i].attr.cid, tcl:jsonObject.children[i].attr.tcl,chlen:jsonObject.children[i].hasOwnProperty("children")},children : [{}]});	
			}
		}
	}
  
  	/************Tree Utils*****************/
	function getObject(jsonObject,currentNodeId,currentCategoryLevel, parentNodeId){
			var obj = new Object();
			var indexRoot = getIndexOfRoot(parentNodeId);
			if(currentCategoryLevel == 2){
				for (var i = 0, j = jsonObject[indexRoot].children.length; i < j; i++ ){
				var attrId = jsonObject[indexRoot].children[i].attr.id;
					if (attrId == currentNodeId){
					obj.jsonData = jsonObject[indexRoot].children[i];
					obj.index = i;
					return obj;
					}
				}
			}
			else {
				for (var i = 0, j = jsonObject.children.length; i < j; i++ ){
				var attrId = jsonObject.children[i].attr.id;
					if (attrId == currentNodeId){
					obj.jsonData = jsonObject.children[i];
					obj.index = i;
					return obj;
					}
				}	
			}
		}
	
	function getRootNodeDetails(){
		var noOfRoots = jsonData.length;
			for (var i = 0,j = noOfRoots; i < j; i++ ){
				rootMap[jsonData[i].attr.id] = jsonData[i].attr.cid;
				rootNode.push({data: jsonData[i].data,attr:{id: jsonData[i].attr.id,cid:jsonData[i].attr.cid,tcl:jsonData[i].attr.tcl},children : [{}]});
			}
	}
	


	function getIndexOfRoot(currentNodeId){
	var numberOfRoots = jsonData.length;
	
		for (var i = 0,j = numberOfRoots; i < j; i++ ){
			if (jsonData[i].attr.id == currentNodeId){				
				return i;
			}
		}
	
	}
	
	function stream(array,element,fragment,process,context,callback){
	 var treeData = array.concat();   
	
		setTimeout(function(){

			var start = +new Date();

			do {
				 process.call(context, treeData.shift(),element,fragment);
			} while (treeData.length > 0 && (+new Date() - start < 50));

			if (treeData.length > 0){
				setTimeout(arguments.callee, 25);
			} else {
				callback(array);
			}
		}, 25);    
	}
	var statusWizard;
	// Added for TAS View-Montitor Student Test Status user story: Start
    function viewTestStatus() {
    	if(!$("#viewStatusButton").hasClass("buttonDisabled")) {
			$('#displayMessageViewTestSession').hide();
			$("#displayMessageViewTestRoster").hide();
			$("#displayMessageViewTestSubtest").hide();
			statusWizard = $("#viewTestSessionAccordion").accordion({ header: "h3", active: 0, event:false });
			$("h3", statusWizard).each(function(index) {
				$(this).bind("click", function(e) {
				  if($(this).parent().attr("id") == 'subtestDetailsSectionId') {
					$("#loginName").text("");
					$("#password").text("");
					$("#testAdminName").text("");
					$("#testAdminName_acco").text("");
					$("#subTestName").text("");
					$("#testStatus").text("");
					$("#testGrade").text("");
					$("#testLevel").text("");
					$("#testGradeRow").hide();
					$("#testLevelRow").hide();
					$("#toggleValidationSubTest").hide();
					$("#toggleExemtionSubTest").hide();
					$("#toggleAbsentSubTest").hide();
					$("#subtestList").html("");
					if($.trim(selectedTestRosterId) != "") {
						viewSubtestDetails(index);
					}
				  } else {
					statusWizard.accordion("activate", index);
					$("#rosterList").closest(".ui-jqgrid-bdiv").scrollTop(scrollPosition);
				  }
				  $("#displayMessageViewTestRoster").hide();
				  $("#displayMessageViewTestSubtest").hide();
			  });
			});
			$("#viewTestSessionId").dialog({  
				title:$("#schViewSts").val(),  
				resizable:false,
				autoOpen: true,
				width: '1024px',
				modal: true,
				closeOnEscape: false,
				open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
			});		
			populateRosterList();
			setPopupPositionViewStatus();
		}
	}
	
	function refreshRosterList() {
		$("#displayMessageViewTestRoster").hide();
		setRetainedRowListValue();
		$('#rosterList').GridUnload();
    	if(selectedTestAdminId != "") {
			$('#displayMessageViewTestSession').hide();
			isForRefreshRoster = true;
			populateRosterList();
		}
	}
	
	function setPopupPositionViewStatus(){
		$("#View_Roster").css("height",'400px');
		$("#View_Subtest").css("height",'400px');
		var toppos = ($(window).height() - 650) /2 + 'px';
		var leftpos = ($(window).width() - 1024) /2 + 'px';
		$("#viewTestSessionId").parent().css("top",toppos);
		$("#viewTestSessionId").parent().css("left",leftpos);	
	}
	
	function closeViewStatusPopup() {
		scrollPosition=0;
		$("#rosterList").closest(".ui-jqgrid-bdiv").scrollTop(scrollPosition);
		closePopUp('viewTestSessionId');
		$("#rosterTestName").text('');
		$("#testAdminName").text('');
		$("#testAdminName_acco").text('');
 	    $("#subtestDetailTooltip").css('display', 'none');
		$("#rosterTotalStudents").text('');
		$('#rosterList').GridUnload();
		$("#labelTbl").width(924);
		$("#buttonTbl").width(924);
		$("#subtestSectionHeader").unbind("click");
		$("#viewTestSessionAccordion").accordion("destroy");
		pageSizeSelected = 10;	
		isPagesizeLabelpopulated = false;
		rosterFormMapOld.clear();
	}
	
	function populateRosterList() {
 	   UIBlock();
 	  	var postDataObject = {};
 		postDataObject.testAdminId = selectedTestAdminId;
 		var rowListValue = $("#pageSize").val();
 		var pageConfigval = $("#pageConfig").val();
		if (rowListValue!=undefined && rowListValue!=null && !isForRefreshRoster && pageConfigval==="true")
		{
		  if (rowListValue==="10" || rowListValue==="50" || rowListValue==="200" || rowListValue==="500")
			pageSizeSelected=rowListValue;
		}
       $("#rosterList").jqGrid({   
       	  url:	  'getRosterDetails.do',   
          mtype:   "POST",
		  datatype: "json",
		  postData:	postDataObject,
          colNames:[ $("#lastNameLbl").val(),$("#firstNameLbl").val(),$("#studentIdLbl").val(),$("#loginIdLbl").val(),$("#passwordLbl").val(),$("#validationStatusLbl").val(),$("#onlineTestStausLbl").val(), $("#dnsLbl").val(), $("#rosterFormLbl").val()],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:90, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:90, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentNumber',index:'studentNumber', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginName',index:'loginName', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'password',index:'password', width:120, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'validationStatus',index:'validationStatus', width:120, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testStatus',index:'testStatus', width:120, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'dnsStatus',index:'dnsStatus', width:70, hidden: true, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'assignedForm',index:'assignedForm', width:70, hidden: true, eidtable: true, align:"left", sorttype:'text', sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }}
		   	],

		   	jsonReader: { repeatitems : false, root:"rosterElement", id:"testRosterId", records: function(obj) {
		   		 var subtestValAllowed = JSON.stringify(obj.subtestValidationAllowed);
		   		 var toggleIsOkCustomer = JSON.stringify(obj.isOkCustomer);
		   		 assignedFormList = obj.assignFormList;
		   		 hasAssignFormConfig = obj.hasAssignFormRosterConfig;
		   		 if(toggleIsOkCustomer == 'true') {
		   		 	isOkCustomer = true;
		   		 } else {
		   		 	isOkCustomer = false;
		   		 }
		   		 var toggleIsTopLevelAdmin = JSON.stringify(obj.isTopLevelAdmin);
		   		 if(toggleIsTopLevelAdmin == 'true') {
		   		 	isOKAdmin = true;
		   		 } else {
		   		 	isOKAdmin = false;
		   		 }
		   		 var toggleIsTopLevelAdminCord = JSON.stringify(obj.isTopLevelAdminCord);
		   		 if(toggleIsTopLevelAdminCord == 'true') {
		   		 	isOKAdminCord = true;
		   		 } else {
		   		 	isOKAdminCord = false;
		   		 }
		   		 if(subtestValAllowed == 'false') {
		   		 	if(isOkCustomer) { // Added for Oklahoma customer as only top level admin can toggle validation for a student.
		   		 		if(isOKAdmin || isOKAdminCord) {
		   		 			$("#toggleValidation").show();
		   		 		} else {
		   		 			$("#toggleValidation").hide();
		   		 		}
		   		 	} else {
						$("#toggleValidation").show();
					}
				 } else {
					$("#toggleValidation").hide();
				 }
				 if(hasAssignFormConfig && hasAssignFormConfig!=undefined) {
					$("#rosterList").jqGrid("showCol","assignedForm");
					if ((assignedFormList.length > 1) && obj.rosterElement.length > 0) {
						$("#assignFormButton").show();
						$("#assignFormMsg").show();					
					}else {
						$("#assignFormButton").hide();
						$("#assignFormMsg").hide();
					}
				 } else {
					$("#rosterList").jqGrid("hideCol","assignedForm"); 
					$("#assignFormButton").hide(); 
				 }
				 var donotScoreAllowed = JSON.stringify(obj.donotScoreAllowed);
				 if(donotScoreAllowed == 'true') {
					$("#doNotScore").show();
					$('#rosterList').jqGrid('showCol', $('#rosterList').getGridParam("colModel")[7].name);
					$("#viewTestSessionId").parent().width(1086);
					$("#rosterDetailsSectionId").width(1058);
					$("#subtestDetailsSectionId").width(1058);
					$("#labelTbl").width(982);
					$("#buttonTbl").width(982);
		         }
				 $("#rosterTestName").text(obj.testSession.testName);
				 $("#testAdminName").text(obj.testSession.testAdminName);
			 	 $("#subtestDetailTooltip").css('display', 'inline');
			 	 
			 	 isTabeProduct = JSON.stringify(obj.testSession.isSTabeProduct);
			 	 invalidationReasonCodeDetails =obj.invalidateReasonList;
			 	 
			 	 //console.log(invalidationReasonCodeDetails);
			 	 invalidationReasonIDList[0] = 'PS';
			 	 invalidationReasonList[0] = 'Please Select';
			 	 for(i=0;i<invalidationReasonCodeDetails.length;i++){
					var tempArr = [];
					var n=invalidationReasonCodeDetails[i].indexOf("_"); 
					invalidationReasonIDList[i+1]=invalidationReasonCodeDetails[i].substr(0,n);
					invalidationReasonList[i+1]	=invalidationReasonCodeDetails[i].substr(n+1);
					
				 }
				 for(var i=0; i<obj.rosterElement.length; i++) {
				 	var testRosterId = obj.rosterElement[i].testRosterId;
				 	var assignedForm = obj.rosterElement[i].assignedForm;
				 	var testStatus = obj.rosterElement[i].testStatus;
				 	rosterFormMapOld.put(testRosterId,assignedForm);
				 	testStatusMap.put(testRosterId, testStatus);
				 }
		   	}},
		   	loadui: "disable",
			rowNum:pageSizeSelected,
			rowList:[10,50,200,500],
			loadonce:true, 
			//multiselect:true,
			pager: '#rosterPager', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			shrinkToFit: false,
			height: 162,
			width: 920,
			caption:$("#stuRos").val(),
			onPaging: function() {
				var reqestedPage = parseInt($('#rosterList').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_rosterPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#rosterList').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#rosterList').setGridParam({"page": minPageSize});
				}
			},
			gridComplete: function() {
				
       		},
			onSelectAll: function (rowIds, status) {
						
			},
			onSelectRow: function (rowid) {
				selectedTestRosterId = rowid;
				//testStatus = testStatusMap.get(selectedTestRosterId);
				$("#displayMessageViewTestRoster").hide();
				var cellData = $('#rosterList').getCell(selectedTestRosterId, '5');
				if($.trim($(cellData).text()) != 'Partially Invalid') {
					setAnchorButtonState('toggleValidationButton', false);
				} else {
					setAnchorButtonState('toggleValidationButton', true);
				}
				var testStatus = $('#rosterList').getCell(selectedTestRosterId, '6');
				if ((testStatus == 'Scheduled') || (testStatus == 'Not taken') ||
					(testStatus == 'In progress') || (testStatus == 'Test locked')) {
					setAnchorButtonState('profileReportStudentButton', true);
				} else {
					setAnchorButtonState('profileReportStudentButton', false);
				}
				scrollPosition = $("#rosterList").closest(".ui-jqgrid-bdiv").scrollTop();
				if(testStatus == "Scheduled")
					testStatus = "SC";
				if(testStatus != undefined && testStatus != "SC") {
					setAnchorButtonState('assignRosterForm', true);
				}else {
					setAnchorButtonState('assignRosterForm', false);
				}
			},
			loadComplete: function () {
				
				if (isTabeProduct == 'true') {
					$("#profileReportStudentDiv").show();
				}
				else {
					$("#profileReportStudentDiv").hide();
				}
				
				if ($('#rosterList').getGridParam('records') === 0) {
					isPAGridEmpty = true;
            		$('#sp_1_rosterPager').text("1");
            		$('#next_rosterPager').addClass('ui-state-disabled');
            	 	$('#last_rosterPager').addClass('ui-state-disabled');
            	 	$("#displayMessageViewTestRoster").show();
					$("#rosterMessage").html($("#noStudentValidMsg").val());
            	} else {
            		isPAGridEmpty = false;
            	}
            	$("#rosterTotalStudents").text($('#rosterList').getGridParam('records'));
				var topRowid = $('#rosterList tr:nth-child(2)').attr('id');
            	$("#rosterList").setSelection(topRowid, true);
            	selectedTestRosterId = topRowid;
            	var status = $('#rosterList').getCell(selectedTestRosterId, '5');
				if($.trim($(status).text()) != 'PI') {
					setAnchorButtonState('toggleValidationButton', false);
				} else {
					setAnchorButtonState('toggleValidationButton', true);
				}
				
	           	$("#rosterList").setGridParam({datatype:'local'});
            	var tdList = ("#rosterPager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				var cellData = null;
				var gridIds = $("#rosterList").getDataIDs();
				for(var i=0; i<gridIds.length; i++) {
					cellData = $.trim($('#rosterList').getCell(gridIds[i], '5'));
					if(cellData == 'VA') {
						$("#rosterList").setCell(gridIds[i], '5', 'Valid', "", "", true);
					} else if(cellData == 'IN') {
						$("#rosterList").setCell(gridIds[i], '5', '<font color="red">Invalid</font>', "", "", true);
					} else if(cellData == 'PI') {
						$("#rosterList").setCell(gridIds[i], '5', '<font color="red">Partially Invalid</font>', "", "", true);
					}
					cellData = $.trim($('#rosterList').getCell(gridIds[i], '6'));
					if(cellData == 'SC') {
						$("#rosterList").setCell(gridIds[i], '6', 'Scheduled', "", "", true);
					} else if(cellData == 'CO') {
						$("#rosterList").setCell(gridIds[i], '6', 'Completed', "", "", true);
					} else if(cellData == 'IN') {
						$("#rosterList").setCell(gridIds[i], '6', 'System stop', "", "", true);
					} else if(cellData == 'NT') {
						$("#rosterList").setCell(gridIds[i], '6', 'Not taken', "", "", true);
					} else if(cellData == 'IP') {
						$("#rosterList").setCell(gridIds[i], '6', 'In progress', "", "", true);
					} else if(cellData == 'IC') {
						$("#rosterList").setCell(gridIds[i], '6', 'Incomplete', "", "", true);
					} else if(cellData == 'IS') {
						$("#rosterList").setCell(gridIds[i], '6', 'Student stop', "", "", true);
					} else if(cellData == 'CL') {
						$("#rosterList").setCell(gridIds[i], '6', 'Test locked', "", "", true);
					} else if(cellData == 'OC') {
						$("#rosterList").setCell(gridIds[i], '6', 'Completed', "", "", true);
					} else if(cellData == 'AB') {
						$("#rosterList").setCell(gridIds[i], '6', 'Session abandoned', "", "", true);
					} else if(cellData == 'SP') {
						$("#rosterList").setCell(gridIds[i], '6', 'Student pause', "", "", true);
					}
					cellData = $.trim($('#rosterList').getCell(gridIds[i], '7'));
					if(cellData == '') {
						$("#rosterList").setCell(gridIds[i], '7', 'N', "", "", true);
					}
				}
				
				var testStatus = $('#rosterList').getCell(selectedTestRosterId, '6');
				if ((testStatus == 'Scheduled') || (testStatus == 'Not taken') ||
					(testStatus == 'In progress') || (testStatus == 'Test locked')) {
					setAnchorButtonState('profileReportStudentButton', true);
				} else {
					setAnchorButtonState('profileReportStudentButton', false);
				}
				//$("option[value=10000]").text('All');
				if ((!isPagesizeLabelpopulated || isForRefreshRoster) && pageConfigval ==="true")
				{
				$('.ui-pg-selbox').closest("td").before("<td id='rosterPageSize' dir='ltr' style='width:75px'>Page size</td>");
				$("#rosterPager .ui-pg-selbox").val(pageSizeSelected);
				//$("#rosterRowList").val(pageSizeSelected);
				isPagesizeLabelpopulated=true;
				isForRefreshRoster=false;
				}
				else if (pageConfigval==="false")
				{
				$('.ui-pg-selbox').hide();
				}
				var testRosterStatus = testStatusMap.get(selectedTestRosterId);
				if(testRosterStatus == "Scheduled")
					testRosterStatus = "SC"
				if(testRosterStatus != undefined && testRosterStatus != "SC") {
					setAnchorButtonState('assignRosterForm', true);
				}else {
					setAnchorButtonState('assignRosterForm', false);
				}
				$.unblockUI();
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				//$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
	  jQuery("#rosterList").jqGrid('navGrid','#rosterPager',{edit:false,add:false,del:false,search:false,refresh:false});
	}
	
	function openAssignFormPopup(element){
		if (isButtonDisabled(element) ) {
			return true;
		} 
		var optionHtml = "";
		$("#displayMessageViewTestRoster").hide();
		$("#assignFormPopup").dialog({  
			title:$("#assignFormPopupLbl").val(),  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
			 $("#assignFormPopup").css('height',120);
			 var toppos = ($(window).height() - 290) /2 + 'px';
			 var leftpos = ($(window).width() - 410) /2 + 'px';
			 $("#assignFormPopup").parent().css("top",toppos);
			 $("#assignFormPopup").parent().css("left",leftpos);
		if ( assignedFormList != null || assignedFormList != undefined ) {
			for (var i=0; i<assignedFormList.length; i++) {
		            optionHtml += '<option value="'+ assignedFormList[i] +'" selected ="selected">'+ assignedFormList[i] +'</option>';
			}
		}
		$("#testFormList").html(optionHtml);
		selectedForm = rosterFormMapOld.get(selectedTestRosterId);
		$("#testFormList").val(selectedForm);
	}
	
	function closeAssignFormPopup() {
		$("#assignFormPopup").hide();	    
		$("#assignFormPopup").dialog('close');
		$.unblockUI(); 	
	}
	
	function assignRosterForm() {
		UIBlock();
    	var postDataObject = {};
    	newAssignedForm = $("#testFormList").val();
    	oldAssignedForm = selectedForm;
    	var rowid = $("#rosterList").jqGrid('getGridParam', 'selrow');
    	if (newAssignedForm != oldAssignedForm && newAssignedForm != null) {
   	    	postDataObject.testRosterId = selectedTestRosterId;
   			postDataObject.assignedForm = newAssignedForm;
		   	$.ajax({
						async:		true,
						beforeSend:	function(){
										UIBlock();
									},
						url:		'updateRosterForm.do',
						type:		'POST',
						dataType:	'json',
						data:		postDataObject,
						success:	function(data, textStatus, XMLHttpRequest) {
										$.unblockUI();
										updateSuccess = true;
										rosterFormMapOld.remove(selectedTestRosterId);
										rosterFormMapOld.put(selectedTestRosterId,newAssignedForm);
 										$("#displayMessageViewTestRoster").show();
										$("#rosterMessage").html($("#assignFormUpdateMsg").val());
										jQuery('#rosterList').setCell(rowid,'assignedForm',newAssignedForm);
									},
						error  :    function(XMLHttpRequest, textStatus, errorThrown){
										$.unblockUI();
										window.location.href="/SessionWeb/logout.do";
									},
						complete :  function(){
										 $.unblockUI();
										 closeAssignFormPopup();
									}
					});
		}else {
			closeAssignFormPopup();
		}
    }

	function viewSubtestDetails(index) {
		var postDataObject = {};
 		postDataObject.testRosterId = selectedTestRosterId;
 		
		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'getSubtestDetails.do',
			type:		'POST',
			dataType:	'json',
			data:		postDataObject,
			success:	function(data, textStatus, XMLHttpRequest){
							$("#loginName").text(data.loginName);
							$("#password").text(data.password);
							$("#testAdminName").text(data.testSession.testAdminName);
							$("#testAdminName_acco").text(data.testSession.testAdminName);
							$("#subTestName").text(data.testSession.testName);
							$("#testStatus").text(data.testStatus);
							if(data.isLaslinkSession) {
							 isLaslinkCustomer = true;
							}
							if(data.testGrade != null) {
								$("#testGradeRow").show();
								$("#testGrade").text(data.testGrade);
							}
							if(data.testLevel != null) {
								$("#testLevelRow").show();
								$("#testLevel").text(data.testLevel);
							}
							
							var html = '<tr class="rosterSubtestHeader">';
							if(data.subtestValidationAllowed) {
								html += '<th class="alignCenter rosterSubtestHeader"><span>'+$("#itemsSelectLbl").val()+'</span></th>';
							}
           					html += '<th class="alignLeft rosterSubtestHeader" height="25" width="*">';
               				html += '&nbsp;&nbsp;<span>'+$("#subtestNameLbl").val()+'</span>';
           					html += '</th>';
           					if(data.isTabeSession) {
            					html += '<th class="alignCenter rosterSubtestHeader" height="25" width="6%">';
                				html += '&nbsp;&nbsp;<span>'+$("#subtestLevelLbl").val()+'</span>';
            					html += '</th>';
           					}
           					if(data.subtestValidationAllowed) {
           						html += '<th class="alignCenter rosterSubtestHeader" height="25" width="10%">';
               					html += '&nbsp;&nbsp;<span>'+$("#validationStatusLbl").val()+'</span>';
           						html += '</th>';
           					}
           					if(data.isLaslinkSession) {
	           					html += '<th class="alignCenter rosterSubtestHeader" height="25" width="10%">';
	               				html += '&nbsp;&nbsp;<span>'+$("#exemtionStatusLbl").val()+'</span>';
	           					html += '</th>';
	           					
	           					html += '<th class="alignCenter rosterSubtestHeader" height="25" width="10%">';
	               				html += '&nbsp;&nbsp;<span>'+$("#absentStatusLbl").val()+'</span>';
	           					html += '</th>';
           					}
           					html += '<th class="alignCenter rosterSubtestHeader" height="25" width="10%">';
               				html += '&nbsp;&nbsp;<span>'+$("#subtestStatusLbl").val()+'</span>';
           					html += '</th>';
           					
           					html += '<th class="alignCenter rosterSubtestHeader" height="25" width="10%">';
               				html += '&nbsp;&nbsp;<span>'+$("#startDateLbl").val()+'</span>';
           					html += '</th>';
           					
           					html += '<th class="alignCenter rosterSubtestHeader" height="25" width="10%">';
               				html += '&nbsp;&nbsp;<span>'+$("#completionDateLbl").val()+'</span>';
           					html += '</th>';
           					if(data.isShowScores) {
            					html += '<th class="alignCenter rosterSubtestHeader" height="25" width="10%">';
                				html += '&nbsp;&nbsp;<span>'+$("#totalItemsLbl").val()+'</span>';
            					html += '</th>';
            					
            					html += '<th class="alignCenter rosterSubtestHeader" height="25" width="10%">';
                				html += '&nbsp;&nbsp;<span>'+$("#itemsCorrectLbl").val()+'</span>';
            					html += '</th>';
            					
            					html += '<th class="alignCenter rosterSubtestHeader" height="25" width="10%">';
                				html += '&nbsp;&nbsp;<span>'+$("#itemsScoredLbl").val()+'</span>';
            					html += '</th>';
           					}
           					if(data.subtestValidationAllowed && data.isLaslinkSession) {
           						html += '<th class="alignCenter rosterSubtestHeader" height="25" width="10%">';
               					html += '&nbsp;&nbsp;<span>'+$("#invalidationReasonLbl").val()+'</span>';
           						html += '</th>';
           					}
           					html += '</tr>';
            				var row = "";
							for(var i=0; i<data.testElement.length; i++) {
								row = data.testElement[i];
								html += '<tr class="sortable" id="'+row.itemSetId+'">';
								if(row.itemSetType == 'TS') {
									if(data.subtestValidationAllowed) {
										html += '<td class="sortable alignCenter">&nbsp;</td>';
									}
									html += '<td class="sortable alignLeft" colspan="'+data.numberColumn+'">';
									html += '<span>'+row.subtestName+'</span>&nbsp;&nbsp; <span>(test access code: '+row.accessCode+')</span></td>';
								} else {
									if(data.subtestValidationAllowed) {
										html += '<td class="sortable alignCenter"><input type="checkbox" name="toggleSubtest" class="toggleSubtest" onclick="changeToggleButton()" value="'+row.itemSetId+'"/></td>';
									}
									html += '<td class="sortable alignCenter"> <span>'+row.subtestName+'</span></td>';
									if(data.isTabeSession) {
										if(row.level != null && row.level != "") {
											html += '<td class="sortable alignCenter"> <span>'+row.level+'</span></td>';
										} else {
											html += '<td class="sortable alignCenter"> <span>--</span></td>';
										}
									}
									if(data.subtestValidationAllowed) {
										if(row.validationStatus != 'Invalid') {
											html += '<td class="sortable alignCenter" id ="'+row.itemSetId+'_validationStatus">'+row.validationStatus+'</td>';
										} else{
											html += '<td class="sortable alignCenter" id ="'+row.itemSetId+'_validationStatus"> <font color="red">'+row.validationStatus+'</font></td>';
										}
									}
									if(data.isLaslinkSession) {
										if(row.testExemptions == 'Y') {
											html += '<td class="sortable alignCenter"><span>Yes</span></td>';
										} else {
											html += '<td class="sortable alignCenter"><span>No</span></td>';
										}
										if(row.absent == 'Y') {
											html += '<td class="sortable alignCenter"><span>Yes</span></td>';
										} else {
											html += '<td class="sortable alignCenter"><span>No</span></td>';
										}
									}
									html += '<td class="sortable alignCenter"> <span>'+row.completionStatus+'</span></td>';
									if(row.startDate != null && row.startDate != "") {
										html += '<td class="sortable alignCenter"> <span>'+row.startDate+'</span></td>';
									} else {
										html += '<td class="sortable alignCenter"> <span>--</span></td>';
									}
									if(row.endDate != null && row.endDate != "") {
										html += '<td class="sortable alignCenter"> <span>'+row.endDate+'</span></td>';
									} else {
										html += '<td class="sortable alignCenter"> <span>--</span></td>';
									}
									if(data.isShowScores) {
										if(row.maxScore == null || row.maxScore === "" || row.maxScore == undefined) {
											html += '<td class="sortable alignCenter"> <span>--</span></td>';
										} else {
											html += '<td class="sortable alignCenter"> <span>'+row.maxScore+'</span></td>';
										}
										if(row.rawScore == null || row.rawScore === "" || row.rawScore == undefined) {
											html += '<td class="sortable alignCenter"> <span>--</span></td>';
										} else {
											html += '<td class="sortable alignCenter"> <span>'+row.rawScore+'</span></td>';
										}
										if(row.unScored == null || row.unScored === "" || row.unScored == undefined) {
											html += '<td class="sortable alignCenter"> <span>--</span></td>';
										} else {
											html += '<td class="sortable alignCenter"> <span>'+row.unScored+'</span></td>';
										}
									}
									
									if(data.subtestValidationAllowed && data.isLaslinkSession) {
										if(row.invalidationReason == undefined || row.invalidationReason == null ) {
											var temp = 'PS';
											html += '<td class="sortable alignCenter"><font weight="normal" family="Arial"><Select id ="'+row.itemSetId+'_select" class="sortable alignLeft" style="font-family: Arial,Verdana,Sans Serif;font-size: 12px;" disabled = "disabled">';
										
												for(j=0;j<invalidationReasonIDList.length;j++){
													if(temp === invalidationReasonIDList[j]){
													html +='<option selected ="selected" value="'+invalidationReasonIDList[j]+'">'+invalidationReasonList[j]+'</option>';
													}
													else{
													html +='<option value="'+invalidationReasonIDList[j]+'">'+invalidationReasonList[j]+'</option>'
													}
												//x += ((temp === invalidationReasonIDList[i])?'<option selected ="selected" value="'+invalidationReasonIDList[i]+'">'+invalidationReasonList[i]+'</option>':'<option value="'+invalidationReasonIDList[i]+'">'+invalidationReasonList[i]+'</option>')
												}
											html +='</select></font></td>';	
											
										}
										else{
											var temp = row.invalidationReason;
											html += '<td class="sortable alignCenter"><font weight="normal" family="Arial"><Select id ="'+row.itemSetId+'_select" class="sortable alignLeft" style="font-family: Arial,Verdana,Sans Serif;font-size: 12px;" disabled = "disabled">';
												
												for(j=0;j<invalidationReasonIDList.length;j++){
													
													if(temp === invalidationReasonIDList[j]){
													html +='<option selected ="selected" value="'+invalidationReasonIDList[j]+'">'+invalidationReasonList[j]+'</option>';
													}
													else{
													html +='<option value="'+invalidationReasonIDList[j]+'">'+invalidationReasonList[j]+'</option>'
													}
												//y += ((temp === invalidationReasonIDList[i])?'<option selected ="selected" value="'+invalidationReasonIDList[i]+'">'+invalidationReasonList[i]+'</option>':'<option value="'+invalidationReasonIDList[i]+'">'+invalidationReasonList[i]+'</option>')
												
												//	html += '<option value="null">Please Select</option>'+((temp === 'Absent')?'<option selected ="selected" value="Absent">Absent</option>':'<option value="Absent">Absent</option>')+((temp ==='Exempt')?'<option selected ="selected" value="Exempt">Exempt</option>':'<option value="Exempt">Exempt</option>')+''+((temp === 'Cheating')?'<option selected ="selected" value="Cheating">Cheating</option>':'<option value="Cheating">Cheating</option>')+''+((temp === 'Sound_Quality')?'<option selected ="selected" value="Sound_Quality">Sound Quality</option>':'<option value="Sound_Quality">Sound Quality</option>')+
												}
											html +='</select></font></td>';
										
										
										}
									}
									
								}
								html += '</tr>';
							}
							$("#subtestList").html(html);
							if(data.subtestValidationAllowed) {
								if(isOkCustomer) { // Added for Oklahoma customer as only state level admin can invalidate a student.
									if(isOKAdmin || isOKAdminCord) {
										$("#toggleValidationSubTest").show();
										setAnchorButtonState('toggleValidationSubtestButton', true);
									} else {
										$("#toggleValidationSubTest").hide();
									}
								} else {
									$("#toggleValidationSubTest").show();
									setAnchorButtonState('toggleValidationSubtestButton', true);
								}
							} else {
								$("#toggleValidationSubTest").hide();
							}
							if(data.isLaslinkSession) {
								$("#toggleExemtionSubTest").show();
								$("#toggleAbsentSubTest").show();
								setAnchorButtonState('toggleExemtionSubtestButton', true);
								setAnchorButtonState('toggleAbsentSubtestButton', true);
							} else {
								$("#toggleExemtionSubTest").hide();
								$("#toggleAbsentSubTest").hide();
							}
							statusWizard.accordion("activate", index);
						},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();
							window.location.href="/SessionWeb/logout.do";
						},
			complete :  function(){
							$.unblockUI(); 
						}
		});
	
		setPopupPositionViewStatus();
	}
	
	function toggleValidationStatus(){
		selectedTestRosterId = $("#rosterList").jqGrid('getGridParam', 'selrow');
		if(selectedTestRosterId != null && $.trim(selectedTestRosterId) != "") {
			$("#displayMessageViewTestRoster").hide();
		 	var cellData = $('#rosterList').getCell(selectedTestRosterId, '5');
		 	if($.trim($(cellData).text()) != 'Partially Invalid') {
		 		var postDataObject = {};
	 			postDataObject.testRosterId = selectedTestRosterId;
		 		$.ajax({
					async:		true,
					beforeSend:	function(){
									UIBlock();
								},
					url:		'toggleValidationStatus.do?testRosterId='+selectedTestRosterId,
					type:		'POST',
					dataType:	'json',
					data:		postDataObject,
					success:	function(data, textStatus, XMLHttpRequest) {	
									var validationStatus = $('#rosterList').jqGrid('getCell',selectedTestRosterId,'5');
									if($.trim(validationStatus) == 'Valid'){
										$('#rosterList').jqGrid('setCell',selectedTestRosterId,'5','<font color="red">Invalid</font>');
									} else {
										$('#rosterList').jqGrid('setCell',selectedTestRosterId,'5','Valid');
									}
									$("#displayMessageViewTestRoster").show();
									$("#rosterMessage").html($("#monitorStsValidMsg").val());
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
	}
	
	function showReasonPopup(){
		var count=0;
		if (isLaslinkCustomer){
			$("input[name=toggleSubtest]").each(function(idx) {
				var rowId = $(this).val();
				var cell = null;
				//cell = $("#" + rowId).children('td').eq(2);								
				if(this.checked) {
					if($("#"+rowId+"_validationStatus").text()== 'Valid' && $("#"+rowId+"_select").val() == 'PS'){
					count++;
					}
				}
			});
		}
		if(count > 0 ) {
	    $("#invalidationReason").show();	    
		$("#invalidationReason").dialog({  
					title:$("#invalidationReasonPopupLbl").val(),  
				 	resizable:false,
				 	autoOpen: true,
				 	width: '400px',
				 	modal: true,
				 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
					});	
		$("#invalidationReason").css('height',120);
		var toppos = ($(window).height() - 290) /2 + 'px';
		var leftpos = ($(window).width() - 410) /2 + 'px';
		$("#invalidationReason").parent().css("top",toppos);
		$("#invalidationReason").parent().css("left",leftpos);
		
		}
		else{
		toggleSubtestValidationStatus();
		}
		
	}
		function closeReasonPopup(){
	 		$("#invalidationReason").hide();	    
			$("#invalidationReason").dialog('close');
			$.unblockUI(); 	
		}
	
	
	function toggleSubtestValidationStatus(){
		closeReasonPopup();
		$("#displayMessageViewTestSubtest").hide();
		var invalidCount = 0;
		var subTestCount = 0;
		selectedTestRosterId = $("#rosterList").jqGrid('getGridParam', 'selrow');
		var reasons ="";
		var itemSetIds = "";
		var subtestIdAndReasonList = "";
		$("input[name=toggleSubtest]").each(function(idx) {
			var rowId = $(this).val();
			var cell = null;
			var sel_comment=null;								
			cell = $("#" + rowId).children('td').eq(2);								
			if(this.checked) {
				if ($.trim($(cell).text()) == 'Valid') {											
					$("#InvalidationText").append(","+rowId);
				}
			}
			if($(this).attr("checked")) {
				reasons=$("#"+rowId+"_select").val();			
				itemSetIds += $(this).val() + "|";
				subtestIdAndReasonList += $(this).val() + "$"+reasons+"|"; 
			
			}
        });
		if(itemSetIds.length > 1) {
			itemSetIds = itemSetIds.substr(0, itemSetIds.length - 1);
			var postDataObject = {};
 			postDataObject.testRosterId = selectedTestRosterId;
 			postDataObject.itemSetIds = itemSetIds;
 			postDataObject.subtestIdAndReasonList = subtestIdAndReasonList.substr(0,subtestIdAndReasonList.length -1);
 			//postDataObject.reason = reasons;
 			
			$.ajax({
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'toggleSubtestValidationStatus.do',
				type:		'POST',
				dataType:	'json',
				data:		postDataObject,
				success:	function(data, textStatus, XMLHttpRequest){	
								var itemSetIds = "";
								$("input[name=toggleSubtest]").each(function(idx) {
									var rowId = $(this).val();
									var cell = null;
									if(data.isTabeSession) {
										cell = $("#" + rowId).children('td').eq(3);
									} else {
										cell = $("#" + rowId).children('td').eq(2);
									}
									if(this.checked) {
										if($.trim($(cell).text()) == 'Valid') {
											$(cell).html('<font color="red">Invalid</font>');
											if(isLaslinkCustomer)
											{
											  $("#"+rowId+"_select").attr("disabled","disabled");
											}
										} else if ($.trim($(cell).text()) == 'Invalid') {
												$(cell).html('Valid');
												if(isLaslinkCustomer){
												$("#"+rowId+"_select").val('PS');
												$("#"+this.value+"_select").removeAttr("disabled");
												}
										}
									} else {
											 if($.trim($(cell).text()) == 'Valid' && data.isLaslinkSession){
												$("#"+rowId+"_select").val('PS');
									  			}
									  	}
									if($.trim($(cell).text()) == 'Invalid') {
										invalidCount++;
									}
									subTestCount++;
								});
								if(invalidCount == 0) {
									$("#rosterList").setCell(selectedTestRosterId, '5', 'Valid', "", "", true);
								} else if(invalidCount < subTestCount) {
									$("#rosterList").setCell(selectedTestRosterId, '5', '<font color="red">Partially Invalid</font>', "", "", true);
								} else if(invalidCount == subTestCount) {
									$("#rosterList").setCell(selectedTestRosterId, '5', '<font color="red">Invalid</font>', "", "", true);
								}
								$("#displayMessageViewTestSubtest").show();
								$("#subtestMessage").html($("#monitorStsValidMsg").val());
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
	
	function changeToggleButton() {
		$("#displayMessageViewTestSubtest").hide();
		var statusFlag = false;
		$("input[name=toggleSubtest]").each(function(idx) {
			if(this.checked) {
				statusFlag = true;
				var a=$("#"+this.value+"_validationStatus").text();
				if(a==='Valid'){
				 $("#"+this.value+"_select").removeAttr("disabled");
				 }
			} else {
				$("#"+this.value+"_select").attr("disabled","disabled");
			}
		});
		if(statusFlag) {
			setAnchorButtonState('toggleValidationSubtestButton', false);
			setAnchorButtonState('toggleExemtionSubtestButton', false);
			setAnchorButtonState('toggleAbsentSubtestButton', false);
		} else {
			setAnchorButtonState('toggleValidationSubtestButton', true);
			setAnchorButtonState('toggleExemtionSubtestButton', true);
			setAnchorButtonState('toggleAbsentSubtestButton', true);
		}
	}
	// Added for TAS View-Montitor Student Test Status user story: End

		
	function serializeDisabledFieldFromDiv(element) {
		var disParam = "";
		 $("#"+element+" *").find(":disabled").each( function() {
		        if( this.name != undefined &&  this.name!=null && this.name!="")
		    	 	disParam = disParam + '&' + this.name + '=' + $(this).val();
		    });
		 
		 return disParam;

	}
	
	function deleteSessionPopup(){
		$('#showSaveTestMessage').hide();
				
		$("#deleteSessionPopup").dialog({  
			title:$("#delSessionTitle").val(),  
			resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});
				
	    $("#deleteSessionPopup").css('height',130);
		var toppos = ($(window).height() - 130) /2 + 'px';
		var leftpos = ($(window).width() - 400) /2 + 'px';
		$("#deleteSessionPopup").parent().css("top",toppos);
		$("#deleteSessionPopup").parent().css("left",leftpos);
			
	}
	
	function deleteTestSession(){
		closePopUp('deleteSessionPopup');		
		var testAdminIdToDelete = $("#"+gridSelectedToDelete).jqGrid('getGridParam', 'selrow');
		var postDataObject = {};
 		postDataObject.testAdminId = testAdminIdToDelete;
		$.ajax(
			{
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'deleteTest.do?&testAdminId=' + testAdminIdToDelete,
				type:		'POST',
				dataType:	'json',
				data:		postDataObject,
				success:	function(data, textStatus, XMLHttpRequest){	
								if(data.isSuccess){
									var successMsg = $("#deleteSuccessMsg").val(); 
									setSessionSaveMessage(successMsg, "", "infoMessage","" );
									$('#showSaveTestMessage').show();
									jQuery("#"+gridSelectedToDelete).delRowData(testAdminIdToDelete);
									setAnchorButtonState('viewStatusButton', true);
									setAnchorButtonState('printTicketButton', true);
 									setAnchorButtonState('profileReportSessionButton', true);
									updateModifyStdManifestButton(false);
									updateCopySessionButton(false);
								} else {
									var failureMsg = $("#deleteFailureMsg").val();
									setSessionSaveMessage(failureMsg, "", "errorMessage","");
									$('#showSaveTestMessage').show();
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
	
	function showDNSConfirmationPopup(){
	
	    $("#dNSconfirmationPopup").show();	    
		$("#dNSconfirmationPopup").dialog({  
					title:$("#dNSConfirmationPopupLbl").val(),  
				 	resizable:false,
				 	autoOpen: true,
				 	width: '400px',
				 	modal: true,
				 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
					});	
		$("#dNSconfirmationPopup").css('height',120);
		var toppos = ($(window).height() - 290) /2 + 'px';
		var leftpos = ($(window).width() - 410) /2 + 'px';
		$("#dNSconfirmationPopup").parent().css("top",toppos);
		$("#dNSconfirmationPopup").parent().css("left",leftpos);		
	}
		
		function closeDNSConfirmationPopup(){
	 		$("#dNSconfirmationPopup").hide();	    
			$("#dNSconfirmationPopup").dialog('close');
			$.unblockUI(); 	
		}
	
	
	
	
	
	
	function toggleDonotScoreStatus(){
		closeDNSConfirmationPopup();
		selectedTestRosterId = $("#rosterList").jqGrid('getGridParam', 'selrow');
		if(selectedTestRosterId != null && $.trim(selectedTestRosterId) != "") {
			var dnsStatus = $('#rosterList').getCell(selectedTestRosterId, 7);
			if($.trim(dnsStatus) == "Y") {
				dnsStatus = "N";
			} else {
				dnsStatus = "Y";
			}
			var postDataObject = {};
 			postDataObject.testRosterId = selectedTestRosterId;
 			postDataObject.dnsStatus = dnsStatus;
			$.ajax({
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'toggleDonotScoreStatus.do',
				type:		'POST',
				dataType:	'json',
				data:		postDataObject,
				success:	function(data, textStatus, XMLHttpRequest) {	
								$("#displayMessageViewTestRoster").show();
								$("#rosterMessage").html($("#doNotScoreMsg").val());
								$('#rosterList').jqGrid('setCell', selectedTestRosterId, '7', dnsStatus);
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
	
	//Added for view product Acknowledgement link
	function displayProductAcknowledgement(AckValue) {
	
		if(AckValue != undefined && AckValue != null && AckValue != '') {
			$("#productAckUrl").show();
			$("#productAckUrl").attr('href',AckValue);
			$("#productAckUrl").attr('style', "color: #0000FF !important");
		} else {
			$("#productAckUrl").hide();
		}
	}
		
function validNameString(str){
    var characters = [];
    characters = toCharArray(str);
    for (var i=0 ; i<characters.length ; i++) {
        var character = characters[i];
        if (! validNameCharacter(character))
            return false;
    }
    return !requestHasInvalidParameters(str);
}

function toCharArray(str){
	var charArr=new Array();
	for(var i=0;i<str.length;i++){
		charArr[i]= str.charAt(i);
	}
	return charArr;      
}

function requestHasInvalidParameters(str){       
	var invalidChars = [];
	invalidChars [0] = "<script>";
	invalidChars [1] = "javascript:";
      
	for( var i=0; i<invalidChars.length; i++ )
	{
	    if ( str.indexOf(invalidChars[i]) != -1 )
	    {
	        return true;                
	    }
	}
      
    return false;
}
  
function validNameCharacter(str){
    var ch = toascii(str);
    var A_Z = ((ch >= 65) && (ch <= 90));
    var a_z = ((ch >= 97) && (ch <= 122));
    var zero_nine = ((ch >= 48) && (ch <= 57));
    var validChar = ((str == '/') || 
                     (str == '\'') || 
                     (str == '-') || 
                     (str == '\\') || 
                     (str == '.') || 
                     (str == '(') || 
                     (str == ')') || 
                     (str == '&') || 
                     (str == '+') || 
                     (str == ',') || 
                     (str == ' '));
    
    return (zero_nine || A_Z || a_z || validChar);
}

function validAddressString(str)
{
    str = trim(str);
    var characters = [];
    characters = toCharArray(str);
    for (var i=0 ; i<characters.length ; i++) {
        var character = characters[i];
        if (! validAddressCharacter(character) )
            return false;
    }
    return !requestHasInvalidParameters(str);
}

function validAddressCharacter(ch)
{
    var valid = validNameCharacter(ch);
	if (ch == '#')
       valid = true; 

    return valid;
}

function validNumber(str){
	str = trim(str);
	var characters = [];
	characters = toCharArray(str);

	for (var i=0 ; i<str.length ; i++) {
	var charCode = str.charCodeAt(i);
		if (!((charCode >= 48) && (charCode <= 57))) {
			return false;
		}
	} 
	return true;
}    

	function updateLocatorValue(){
		UIBlock();
		var locator = document.getElementById("hasAutolocator");
		if(!locator.checked){	
				locator.value = "false";	
				hasAutolocator = false;
				if(isTestBreak){
						$("#"+locatorSubtest.id+" input").val("");
				}

				
		} else {
			locator.value = "true";	
			hasAutolocator = true;
			if(isTestBreak){
				var accesscode = ProductData.accessCodeList[0];
				var localAccesCodeMap = new Map();
				for(var j=0; j<selectedSubtests.length; j++) {
					var id = ($("#"+selectedSubtests[j].id).children() [0]).value.toLowerCase(); 
					var val = ($("#"+selectedSubtests[j].id).children() [0]); 
					localAccesCodeMap.put(id, val);
				} 
				for(var i=0; i<ProductData.accessCodeList.length; i++) {
					var code = localAccesCodeMap.get(ProductData.accessCodeList[i].toLowerCase());
					if( code == null )  {
						accesscode = ProductData.accessCodeList[i];
						break;
					}
				
				}
				
			    $("#"+locatorSubtest.id+" input").val(accesscode);
			}
			var allForm = $("[name=itemSetForm]");
			for(var ii=0; ii<allForm.length; ii++){
				allForm[ii].value = "";
			}
			$("#itemSetForm")[0].value = "";//locator
		}
		
		$.unblockUI();  
	
	}

	function populateAccessCodeMap(isTestBreak){
	  	var subtestLength = selectedSubtests.length;
	  	
	  	if(!isTestBreak ){
	  		for(var i=0;i<subtestLength;i++){	
		   			var itemSetIdTd = document.getElementById("itemSetIdTD"+i).value;
			      	var selectedAccessCode = document.getElementById("aCodeB"+i).value;
			      	selectedAccessCodeMap.put(itemSetIdTd, selectedAccessCode);
			}
			if(locatorSubtest!=null && locatorSubtest!= undefined && locatorSubtest.id!=undefined && hasAutolocator) {
				var itemSetIdTd = document.getElementById("itemSetIdTD").value;
			    var selectedAccessCode = document.getElementById("aCodeB_l").value;
				selectedAccessCodeMap.put(itemSetIdTd, selectedAccessCode);
			}
	  	} else {
	  		var selectedAccessCode = document.getElementById("aCode").value;
			selectedAccessCodeMap.put('AccessCode', selectedAccessCode);

	  	}

	}
	
	
	function copyTestSession(element){
	    if (isButtonDisabled(element) ) {
			return true;
		}
	    var action = "copySession"; 
	    editTestSession(action);
	}
	function updateCopySessionButton(isEnable){
		enableOrDisableAnchorButton('copySessionButton', 'copySession', isEnable);
	}

	function toggleExemtionValidationStatus(){
		$("#displayMessageViewTestSubtest").hide();
		selectedTestRosterId = $("#rosterList").jqGrid('getGridParam', 'selrow');
		var itemSetIds = "";
		$("input[name=toggleSubtest]").each(function(idx) {
			if($(this).attr("checked")) {
				itemSetIds += $(this).val() + "|";
			}
        });
		if(itemSetIds.length > 1) {
			itemSetIds = itemSetIds.substr(0, itemSetIds.length - 1);
			var postDataObject = {};
 			postDataObject.testRosterId = selectedTestRosterId;
 			postDataObject.itemSetIds = itemSetIds;
 			
			$.ajax({
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'toggleExemtionValidationStatus.do',
				type:		'POST',
				dataType:	'json',
				data:		postDataObject,
				success:	function(data, textStatus, XMLHttpRequest){	
								var itemSetIds = "";
								$("input[name=toggleSubtest]").each(function(idx) {
									var rowId = $(this).val();
									var cell = null;
									if(data.isTabeSession) {
										cell = $("#" + rowId).children('td').eq(4);
									} else {
										cell = $("#" + rowId).children('td').eq(3);
									}
									if(this.checked) {
										if($.trim($(cell).text()) == 'No') {
											$(cell).html('Yes');
										} else if ($.trim($(cell).text()) == 'Yes') {
											$(cell).html('No');
										}
									}
								});
								$("#displayMessageViewTestSubtest").show();
								$("#subtestMessage").html($("#monitorStsValidMsg").val());
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
	
	function toggleAbsentValidationStatus(){
		$("#displayMessageViewTestSubtest").hide();
		var invalidCount = 0;
		var subTestCount = 0;
		selectedTestRosterId = $("#rosterList").jqGrid('getGridParam', 'selrow');
		var itemSetIds = "";
		$("input[name=toggleSubtest]").each(function(idx) {
			if($(this).attr("checked")) {
				itemSetIds += $(this).val() + "|";
			}
        });
		if(itemSetIds.length > 1) {
			itemSetIds = itemSetIds.substr(0, itemSetIds.length - 1);
			var postDataObject = {};
 			postDataObject.testRosterId = selectedTestRosterId;
 			postDataObject.itemSetIds = itemSetIds;
 			
			$.ajax({
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'toggleAbsentValidationStatus.do',
				type:		'POST',
				dataType:	'json',
				data:		postDataObject,
				success:	function(data, textStatus, XMLHttpRequest){	
								var itemSetIds = "";
								$("input[name=toggleSubtest]").each(function(idx) {
									var rowId = $(this).val();
									var cell = null;
									if(data.isTabeSession) {
										cell = $("#" + rowId).children('td').eq(5);
									} else {
										cell = $("#" + rowId).children('td').eq(4);
									}
									if(this.checked) {
										if($.trim($(cell).text()) == 'No') {
											$(cell).html('Yes');
										} else if ($.trim($(cell).text()) == 'Yes') {
											$(cell).html('No');
										}
									}
								});
								$("#displayMessageViewTestSubtest").show();
								$("#subtestMessage").html($("#monitorStsValidMsg").val());
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
	
	function isTutorialOrLocatorProduct() {
		if ((selectProductId == 4013) || (selectProductId == 4008))
			return true;
		else 
			return false;			
	}
	
	function viewIndividualReport(element, accessBy){
	    if (isButtonDisabled(element) ) {
			return true;
		}
		var accessByParam = "accessBy=" + accessBy;
		var sessionParam = "testAdminId=" + selectedTestAdminId;
		var rosterParam = "rosterId=" + selectedTestRosterId;		
   		document.forms[0].action = "viewIndividualReport.do" + "?" + accessByParam + "&" + sessionParam + "&" + rosterParam;   	
		document.forms[0].submit();
	}
	
	function setRetainedRowListValue() {
		pageSizeSelected = $("#rosterPager .ui-pg-selbox").val(); 
	}