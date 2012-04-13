
var leafNodeCategoryId;
var orgTreeHierarchy;
var jsonData;
var assignedOrgNodeIds = "";
var SelectedOrgNodeId;
var SelectedOrgNode;
var SelectedOrgNodes = [];
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
var rootNodeId;
var map = new Map();
var rootNode = [];
var checkedListObject = {};
var type;
var asyncOver = 0;
var leafParentOrgNodeId = "";
var isPopUp = false;
var stuThreshold = 10000;
var gridloadedStu = false;
var gridloadedSes = false;
var gradeOptions = ":Any";
var genderOptions = ":Any;Male:Male;Female:Female;Unknown:Unknown";
var testNameOptions = ":Any";
var myRoleOptions = ":Any;Owner:Owner;Proctor:Proctor";
var statusOptions = ":Any;Completed:Completed;Current:Current";
var currentView = "session";
var selectedRosterId;

function populateStudentScoringTree() {
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'scoringOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI(); 
						leafNodeCategoryId = data.leafNodeCategoryId;
						orgTreeHierarchy = data;
						jsonData = orgTreeHierarchy.data;
						getRootNodeDetails();
						createSingleNodeScoringTree(orgTreeHierarchy);
						populateDropDowns();
						$("#searchheader").css("visibility","visible");	
						$("#scoringOrgNode").css("visibility","visible");	
												
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

function createSingleNodeScoringTree(jsondata) {
	   $("#scoringOrgNode").jstree({
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

		$("#scoringOrgNode").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#scoringOrgNode ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
		
		$("#scoringOrgNode").delegate("a","click", function(e) {
  			SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#treeOrgNodeId").val(SelectedOrgNodeId);
 		    if(currentView == "student") {
		 		if(parseInt(rootNodeId) == parseInt(SelectedOrgNodeId)){
		 			var postDataObject = {};
		 			postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
		 		   $.ajax({
						async:		true,
						beforeSend:	function(){									
										UIBlock();
									},
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
											populateGrid();
										}
									},
						error  :    function(XMLHttpRequest, textStatus, errorThrown){
										$.unblockUI();  
										window.location.href="/SessionWeb/logout.do";
										
									}
					});		
		 		 }else{
		 		    populateGrid();
		 		} 		  		
			} else {
				var topNodeSelected = $(this).parent().attr("cid");
				if(topNodeSelected == '1'){
	    	 		openConfirmationPopup();
	    		} else {
 		    		populateGrid();
	    		}
			}
		});
		
		
		registerDelegate("scoringOrgNode");

}


function populateGrid(){
	UIBlock();
	if(currentView=="student") {
		if(!gridloadedStu) {
	  		gridloadedStu = true;
			populateScoringStudentGrid();
		} else {
			gridScoringStudentReload();
		}
		if(gridloadedSes) {
			document.getElementById('scoreButton').style.visibility = "visible";
			setAnchorButtonState('scoreButton', true);
		}
	} else {
		if(!gridloadedSes) {
	  		gridloadedSes = true;
			populateScoringSessionGrid();
		} else {
			gridScoringSessionReload();
		}
		if(gridloadedSes) {
			document.getElementById('scoreButton').style.visibility = "visible";
			setAnchorButtonState('scoreButton', true);
		}
	}
}


function populateDropDowns() {

	var postDataObject = {};
	
	$.ajax({
					async:		true,
					beforeSend:	function(){
									UIBlock();
								},
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
									}
									
								},
					error  :    function(XMLHttpRequest, textStatus, errorThrown){
									$.unblockUI();  
									window.location.href="/SessionWeb/logout.do";
									
								}
				});		
}


function populateScoringStudentGrid() {
		
		var studentGridTitle = '<table width="'+$("#jqGrid-content-section").width()+'px">'
								+ '<tbody><tr><td align="left"><span>'+$("#stuGridCaption").val()+'</span>'
								+ '</td><td align="right"><span style="float: right;"><b>'
								+$("#gridShowBy").val()+'</b>:<select id="showByStudent"'
								+ ' onchange="viewBySession();"><option>'+$("#gridShowByStu").val()
								+'&nbsp;</option><option>'+$("#gridShowBySes").val()+'&nbsp;</option>'
								+ '</select>&nbsp;&nbsp;</span></td></tr></tbody></table>';
	
		var studentIdTitle = $("#studentIdLabelName").val();
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
        $("#studentScoringGrid").jqGrid({         
          url:'getStudentForScoringGrid.do', 
		 mtype:   'POST',
		 postData: postDataObject,
		 datatype: "json",         
          colNames:[$("#stuGrdLoginId").val(),$("#stuGrdStdName").val(), $("#grdGroup").val(), $("#stuGrdGrade").val(),$("#stuGrdGender").val(), studentIdTitle, $("#grdSessionName").val(), $("#grdTestName").val()],
		   	colModel:[
		   		{name:'userName',index:'userName', width:110, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentName',index:'studentName', width:110, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr', width:100, editable: true, align:"left",sorttype:'text',search: false,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:60, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: gradeOptions } },
		   		{name:'gender',index:'gender', width:60, editable: true, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: genderOptions } },
		   		{name:'studentNumber',index:'studentNumber', width:75, editable: true, align:"left",sorttype:'text',search: false,sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testSessionName',index:'testSessionName',editable: true, width:150, align:"left",search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testCatalogName',index:'testCatalogName',editable: true, width:150, align:"left",search: true, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: testNameOptions } }
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"rosterId",
		   	records: function(obj) {} },
		   	
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#studentScoringPager', 
			sortname: 'studentNumber', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: $("#jqGrid-content-section").width(), 
			editurl: 'getStudentForSelectedOrgNodeGrid.do',
			ondblClickRow: function(rowid) {viewEditStudentPopup();},
			caption:studentGridTitle,
			onPaging: function() {
				var reqestedPage = parseInt($('#studentScoringGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_studentScoringPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#studentScoringGrid').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#studentScoringGrid').setGridParam({"page": minPageSize});
				}
				
			},
			onSelectRow: function (rowId) {
					setAnchorButtonState('scoreButton', false);
					selectedRosterId = rowId;
					alert("selectedRosterId -> " + selectedRosterId);
			},
			loadComplete: function () {
				if ($('#studentScoringGrid').getGridParam('records') === 0) {
            		$('#sp_1_studentScoringPager').text("1");
            		$('#next_studentScoringPager').addClass('ui-state-disabled');
            		$('#last_studentScoringPager').addClass('ui-state-disabled');
            		$('#studentScoringGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#studentScoringGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/StudentWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsg").val()+"</td></tr></tbody></table></td></tr>");
            	}
				$.unblockUI();  
				$("#studentScoringGrid").setGridParam({datatype:'local'});
				var tdList = ("#studentScoringPager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });  
	 jQuery("#studentScoringGrid").jqGrid('filterToolbar');
			jQuery("#studentScoringGrid").navGrid('#studentScoringPager',{
				search: false,add:false,edit:false,del:false 	
			}).jqGrid('navButtonAdd',"#studentScoringPager",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchUserByKeyword").dialog({  
						title:$("#searchStudentID").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
					 	});
			    }, position: "one-before-last", title:"Search Student", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#studentScoringPager",{position: "first"
			});
			
			jQuery(".ui-icon-refresh").bind("click",function(){
				$("#searchUserByKeywordInput").val('');
			}); 
			
					
	}

function gridScoringStudentReload(){
	var postDataObject = {};
	postDataObject.q = 2;
	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
	jQuery("#studentScoringGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
	jQuery("#studentScoringGrid").jqGrid('setGridParam', {url:'getStudentForScoringGrid.do',postData:postDataObject,page:1}).trigger("reloadGrid");
	jQuery("#studentScoringGrid").sortGrid('studentNumber',true,'asc');

}


function populateScoringSessionGrid() {
		
		var gridCntWidth = 0;
		gridCntWidth = $("#jqGrid-content-section").width();
		gridCntWidth = parseInt(gridCntWidth) - 180;
		var sessionGridTitle = '<table width="'+$("#jqGrid-content-section").width()+'px">'
								+ '<tbody><tr><td align="left"><span>'+$("#sesGridCaption").val()+'</span>'
								+ '</td><td align="right"><span style="float: right;"><b>'
								+$("#gridShowBy").val()+'</b>:<select id="showBySession"'
								+ ' onchange="viewByStudent();"><option>'+$("#gridShowBySes").val()
								+'&nbsp;</option><option>'+$("#gridShowByStu").val()+'&nbsp;</option>'
								+ '</select>&nbsp;&nbsp;</span></td></tr></tbody></table>';
	
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
        $("#sessionScoringGrid").jqGrid({         
          url:'getSessionForScoringGrid.do', 
		 mtype:   'POST',
		 postData: postDataObject,
		 datatype: "json",         
          colNames:[$("#grdSessionName").val(),$("#grdTestName").val(), $("#grdGroup").val(), $("#sesGridMyRole").val(),$("#sesGridStatus").val(), $("#sesGridStartDate").val(), $("#sesGridEndDate").val()],
		   	colModel:[
		   		{name:'testAdminName',index:'testAdminName', width:160, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testName',index:'testName', width:160, editable: true, align:"left",sorttype:'text',search: true,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: testNameOptions } },
		   		{name:'creatorOrgNodeName',index:'creatorOrgNodeName', width:100, editable: true, align:"left",sorttype:'text',search: false,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'AssignedRole',index:'AssignedRole',editable: true, width:60, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: myRoleOptions } },
		   		{name:'testAdminStatus',index:'testAdminStatus', width:80, editable: true, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: statusOptions } },
		   		{name:'loginStartDateString',index:'loginStartDateString',editable: true, width:80, align:"left",search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginEndDateString',index:'loginEndDateString',editable: true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"testSessionCUPA", id:"testAdminId",
		   	records: function(obj) {} },
		   	
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#sessionScoringPager', 
			sortname: 'testAdminName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: $("#jqGrid-content-section").width(), 
			editurl: 'getSessionForSelectedOrgNodeGrid.do',
			ondblClickRow: function(rowid) {viewEditStudentPopup();},
			caption:sessionGridTitle,
			onPaging: function() {
				var reqestedPage = parseInt($('#sessionScoringGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_sessionScoringPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#sessionScoringGrid').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#sessionScoringGrid').setGridParam({"page": minPageSize});
				}
				
			},
			onSelectRow: function (rowId) {
					setAnchorButtonState('scoreButton', false);
			},
			loadComplete: function () {
				if ($('#sessionScoringGrid').getGridParam('records') === 0) {
            		$('#sp_1_sessionScoringPager').text("1");
            		$('#next_sessionScoringPager').addClass('ui-state-disabled');
            		$('#last_sessionScoringPager').addClass('ui-state-disabled');
            		$('#sessionScoringGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#sessionScoringGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/ScoringWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noSessionTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noSessionMessage").val()+"</td></tr></tbody></table></td></tr>");
            	}
				$.unblockUI();  
				$("#sessionScoringGrid").setGridParam({datatype:'local'});
				var tdList = ("#sessionScoringPager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });  
	 jQuery("#sessionScoringGrid").jqGrid('filterToolbar');
			jQuery("#sessionScoringGrid").navGrid('#sessionScoringPager',{
				search: false,add:false,edit:false,del:false 	
			}).jqGrid('navButtonAdd',"#sessionScoringPager",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchUserByKeyword").dialog({  
						title:$("#searchStudentID").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
					 	});
			    }, position: "one-before-last", title:"Search Session", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#sessionScoringPager",{position: "first"
			});
			
			jQuery(".ui-icon-refresh").bind("click",function(){
				$("#searchUserByKeywordInput").val('');
			}); 
			
					
	}

function gridScoringSessionReload(){
	var postDataObject = {};
	postDataObject.q = 2;
	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
	jQuery("#sessionScoringGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
	jQuery("#sessionScoringGrid").jqGrid('setGridParam', {url:'getSessionForScoringGrid.do',postData:postDataObject,page:1}).trigger("reloadGrid");
	jQuery("#sessionScoringGrid").sortGrid('studentNumber',true,'asc');

}


function viewBySession() {
	UIBlock();
	currentView = "session";
	$("#studentView").hide();
	$("#showBySession").val($("#gridShowBySes").val()+'&nbsp;');
	resetFilters();
	populateGrid();
	$("#sessionView").show();
	setAnchorButtonState('scoreButton', true);
}

function viewByStudent() {
	UIBlock();
	currentView = "student";
	$("#sessionView").hide();
	$("#showByStudent").val($("#gridShowByStu").val()+'&nbsp;');
	resetFilters();
	populateGrid();
	$("#studentView").show();
	setAnchorButtonState('scoreButton', true);
}

function resetFilters() {
	$("#gs_grade").val("Any");
	$("#gs_gender").val("Any");
	$("#gs_testCatalogName").val("Any");
	$("#gs_testName").val("Any");
	$("#gs_AssignedRole").val("Any");
	$("#gs_testAdminStatus").val("Any");
}



function UIBlock(){
	$.blockUI({ message: '<img src="/ScoringWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}


function showPopup(stuCount){
	var msg = $("#stuCountId").val();
	stuCount = addCommas(stuCount);//to add commas at 1000th places
	var countMsg = msg.replace("XXXX",stuCount);
	
	$("#rootNodePopup").dialog({  
			title:'Alert',  
			resizable:false,
			autoOpen: true,
			modal: true,
			closeOnEscape: false,
			open: function(event, ui) {
				$("#exceedMsg").text(countMsg);
				$(".ui-dialog-titlebar-close").hide(); 
			}
	});
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
	
function fetchDataOnConfirmation() {
		closePopUp('confirmationPopup');
		populateGrid();
 		
	}


function closePopUp(dailogId){

	$("#"+dailogId).dialog("close");
	
}


function displayListPopup(element) {
	if (isButtonDisabled(element))
		return true;
	
	if(currentView = "student") {
		studentScoring();
	} else {
		sessionScoring();
	}
}

function studentScoring() {
    
			$("#studentScoringId").dialog({  
				title:$("#scorPopupTitle").val(),  
				resizable:false,
				autoOpen: true,
				width: '1024px',
				modal: true,
				closeOnEscape: false,
				open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
			});		
	
			setPopupPositionHandScoring(); 
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
			//currentCategoryLevel = currentCategoryLevel - (rootCategoryLevel -1);			
		//console.log("currentCategoryLevel" + currentCategoryLevel);
	
		if (classState == false){
			if (currentCategoryLevel == 1) {	
			dataObj2 = [];	
			var indexOfRoot = getIndexOfRoot(currentNodeId);
			populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot);
			//console.log("category Level action");
			}
	
			var cacheData = map.get(currentNodeId);
			if (cacheData != null){
				currentTreeArray = cacheData;			
			}
			if (cacheData == null){
			//console.log("before swuitch");
				switch(currentCategoryLevel){
					
					//Not caching at initial level because the whole data will be put in cache which may increase the cache size
					//considerably
					
					case "2": 	dataObj3 =getObject(jsonData,currentNodeId,currentCategoryLevel,x.parentNode.parentNode.id);
								currentIndex = dataObj3.index;
								currentTreeArray = dataObj3.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//console.log("case2");
								
								//leafClass = 'jstree-leaf';
							break;
							
					case "3": 	dataObj4 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj4,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//console.log("case3");
							break;
					case "4": 	dataObj5 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj5,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//console.log("case4");
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


/******Jstree Methods*****/
	//method triggered from library
	  function customLoad(){
	  	//console.log("Custom Load called");
	    pushInsideElement(currentNodeId,currentCategoryLevel,dataObj2,currentTreeArray);
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
		
		updateTree(objArray);	
	  }
	  function updateTree(objArray){
	  	//timer.setStartTime();
	  	var currentElement;
		if (isPopUp){
			currentElement = $('li[id='+ currentNodeId+ ']');
			if(currentElement.length < 2) currentElement = currentElement[0];
			else currentElement = currentElement[1];
				
		}else {		
			currentElement = document.getElementById(currentNodeId);
		}
	
		currentElement.className = "jstree-open jstree-unchecked";
		var fragment = document.createDocumentFragment();
		var ulElement = document.createElement('ul');
		
		if(type.indexOf("innerID") < 0){
		stream(objArray,ulElement,fragment,streamPush, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
			 //currentElement.childNodes[1].firstChild.style.display = "none";
		 });	
		 }
		 else{
		 stream(objArray,ulElement,fragment,streamInnerPush, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
			asyncOver++;
			openNextLevel(asyncOver);
			// currentElement.childNodes[1].firstChild.style.display = "none";
		 });	
		 }
	  }

	  //Seperated outer tree push and inner tree push because each method is a performance critical operation conditions will increase the 
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
	  
	  function streamInnerPush(objArray,ulElement,fragment){
	  	var liElement = document.createElement('li');
		liElement.setAttribute("id", objArray.attr.id);
		liElement.setAttribute("cid" , objArray.attr.cid);
		liElement.setAttribute("tcl" , objArray.attr.tcl);
		var condition = objArray.attr.chlen;
		
		switch(condition){
		case false: liElement.className = "jstree-leaf jstree-"+ getCheckedStatus(objArray.attr.id);
					break;
		case true:  liElement.className = "jstree-closed jstree-"+ getCheckedStatus(objArray.attr.id);
					break;
		case undefined: var con = objArray.hasOwnProperty("children");
							switch(con){
								case false: liElement.className = "jstree-leaf jstree-"+ getCheckedStatus(objArray.attr.id);
											break;
								case true:  liElement.className = "jstree-closed jstree-"+ getCheckedStatus(objArray.attr.id);
											break;						
							}
						  break;
		
		}	
		if (objArray.attr.cid == leafNodeCategoryId){
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: inline-block;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}else{
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}
		if(profileEditable === "false"  && $("#classReassignable").val() === "true") {
			liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}
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
				if(jsonData[i].attr.cid == "1"){
					rootNodeId = jsonData[i].attr.id;
				}					
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
	
	function getCheckedStatus(id){
    if (checkedListObject[id] == "checked"){
		return "checked";
	}
  		return "unchecked";
  }
  
  
  
function prepareData(classState,currentCategoryLevel,currentNodeId,element){
  
		if (classState == false || !classState || classState == 'false'){

			var cacheData = map.get(currentNodeId);
			if (cacheData != null){
				currentTreeArray = cacheData;			
			}
					
			if (cacheData == null){
		if (currentCategoryLevel == "1") {	
			dataObj2 = [];	
			var indexOfRoot = getIndexOfRoot(currentNodeId);
			populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot);
		}
				switch(String(currentCategoryLevel)){
					
					case "2": 	dataObj3 =getObject(jsonData,currentNodeId,currentCategoryLevel,element);
								currentIndex = dataObj3.index;
								currentTreeArray = dataObj3.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;							
					case "3": 	dataObj4 = map.get(element);
								currentTreeArray =getObject(dataObj4,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "4": 	dataObj5 = map.get(element);
								currentTreeArray =getObject(dataObj5,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "5": 	dataObj6 = map.get(element);
								currentTreeArray =getObject(dataObj6,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "6": 	dataObj7 = map.get(element);
								currentTreeArray =getObject(dataObj7,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "7": 	dataObj8 =map.get(element);
								currentTreeArray =getObject(dataObj8,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);								
							break;	
					case "8": 	dataObj9 =map.get(element);
								currentTreeArray =getObject(dataObj9,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;						
					
				}
			}

		}
  
  }
  
  
  function openNextLevel(asyncOver){
  		if(leafParentOrgNodeId.length - 1 > asyncOver) {
	  		var tmpNode = leafParentOrgNodeId[asyncOver];	
			currentCategoryLevel = String(asyncOver + 1);
			currentNodeId = tmpNode;
			currentTreeArray = map.get(currentNodeId);
			$('#innerID').jstree('open_node', "#"+currentNodeId);
		}
  }