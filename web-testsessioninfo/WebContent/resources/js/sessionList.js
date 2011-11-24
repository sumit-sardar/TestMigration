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

function UIBlock(){
	$.blockUI({ message: '<img src="/SessionWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'45px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}


function populateSessionListGrid(homePageLoad) {
		ishomepageload  = homePageLoad;
 		UIBlock();
 		//populateTree();
 		reset();
 		$("#list2").jqGrid({         
          url: 'getSessionForUserHomeGrid.do', 
		 type:   'POST',
		 datatype: "json",         
          colNames:['Session Name','Test Name', 'Organization', 'My Role','Start Date', 'End Date'],
		   	colModel:[
		   		{name:'testAdminName',index:'testAdminName', width:250, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testName',index:'testName', width:225, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'creatorOrgNodeName',index:'creatorOrgNodeName', width:175, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'AssignedRole',index:'AssignedRole',editable: true, width:100, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginStartDate',index:'loginStartDate', width:175, editable: true, align:"left", sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'}, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginEndDate',index:'loginEndDate', width:175, editable: true, align:"left",sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'}, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"testSessionCUFU", id:"testAdminId",
		   	records: function(obj) { 
		   	 sessionListCUFU = JSON.stringify(obj.testSessionCUFU);
		   	 if(ishomepageload){
		   	 	leafNodeCategoryId = obj.orgNodeCategory.categoryLevel;
		   	 	leafNodeCategoryName = obj.orgNodeCategory.categoryName;
		   	 }
		   	 } },
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
			caption:"Session List",
		//	ondblClickRow: function(rowid) {viewEditStudentPopup();},
			onPaging: function() {
				//clearMessage();
				var reqestedPage = parseInt($('#list2').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_pager2').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#list2').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#list2').setGridParam({"page": minPageSize});
				}
				
			},
			onSelectRow: function () {
					setAnchorButtonState('viewStatusButton', false);
					if($("#canRegisterStudent").val() == 'true'){
						var selectedTestSessionId = $("#list2").jqGrid('getGridParam', 'selrow');
						 var val = getDataFromJson(selectedTestSessionId, sessionListCUFU);
						 if(val == '"F"'){	
			 				setAnchorButtonState('registerStudentButton', true);
			 			 } else {
			 			 	setAnchorButtonState('registerStudentButton', false);
			 			 }
			 		}
			},
			loadComplete: function () {
				if ($('#list2').getGridParam('records') === 0) {
				 	isGridEmpty = true;
            	 	$('#sp_1_pager2').text("1");
            	} else {
            		isGridEmpty = false;
            	}
            	if(!gridloaded) {
            		populateCompletedSessionListGrid();
            	} else {
            		gridReloadPA();
            	}
            	
            	var width = jQuery("#sessionGrid").width();
			    width = width - 80; // Fudge factor to prevent horizontal scrollbars
			    jQuery("#list2").setGridWidth(width);
			    jQuery("#list3").setGridWidth(width);
			    setEmptyListMessage('CUFU');
				$.unblockUI();  
				$("#list2").setGridParam({datatype:'local'});
				var tdList = ("#pager2_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/TestSessionInfoWeb/logout.do";
						
			}
	 });
	 jQuery("#list2").navGrid('#pager2', {
				addfunc: function() {
					scheduleSession();
		    	}	    	
			}); 
}


function populateCompletedSessionListGrid() {
 		//UIBlock();
 		gridloaded = true;
 		reset();
       $("#list3").jqGrid({         
          url: 'getCompletedSessionForGrid.do', 
		  type:   'POST',
		  datatype: "json",          
          colNames:['Session Name','Test Name', 'Organization', 'My Role','Start Date', 'End Date'],
		   	colModel:[
		   		{name:'testAdminName',index:'testAdminName', width:250, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testName',index:'testName', width:225, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'creatorOrgNodeName',index:'creatorOrgNodeName', width:175, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'AssignedRole',index:'AssignedRole',editable: true, width:100, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginStartDate',index:'loginStartDate', width:175, editable: true, align:"left", sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'}, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginEndDate',index:'loginEndDate', width:175, editable: true, align:"left", sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'},sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"testSessionPA", id:"testAdminId",
		   	records: function(obj) { 
		   	 sessionListPA = JSON.stringify(obj.testSessionPA);
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
			caption:"Session List",
		//	ondblClickRow: function(rowid) {viewEditStudentPopup();},
			onPaging: function() {
				//clearMessage();
				var reqestedPage = parseInt($('#list3').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_pager3').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#list3').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#list3').setGridParam({"page": minPageSize});
				}
			},
			onSelectRow: function () {
					setAnchorButtonState('viewStatusButton', false);
					if($("#canRegisterStudent").val() == 'true'){
			 			var selectedTestSessionId = $("#list3").jqGrid('getGridParam', 'selrow');
						 var val = getDataFromJson(selectedTestSessionId, sessionListPA);
						 if(val == '"F"'){	
			 				setAnchorButtonState('registerStudentButton', true);
			 			 } else {
			 			 	setAnchorButtonState('registerStudentButton', false);
			 			 }
			 		}
			},
			loadComplete: function () {
				if ($('#list3').getGridParam('records') === 0) {
					isPAGridEmpty = true;
            		$('#sp_1_pager3').text("1");
            	} else {
            		isPAGridEmpty = false;
            	}
            	setEmptyListMessage('PA');
            	$.unblockUI();  
				$("#list3").setGridParam({datatype:'local'});
				var tdList = ("#pager3_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#list3").jqGrid('navGrid','#pager3',{});
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
	 	 	$('#totalStudent').text("0");
	 	 	$('#stuWithAcc').text("0");
		 	$('#list6').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
		 	$('#list6').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='6'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/SessionWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsg").val()+"</td></tr></tbody></table></td></tr>");
		 }
	 }
	 
	 }
	
	
	function setupButtonPerUserPermission() {
	var element = document.getElementById('add_list3');
	element.style.display = 'none'; 
	var userScheduleAndFindSessionEnable = $("#userScheduleAndFindSessionPermission").val();
		if (userScheduleAndFindSessionEnable == 'false') {	
			var element = document.getElementById('add_list2');
			element.style.display = 'none';
		}
	}
	function showTreeSlider() {
		openTreeRequested = true;
		 if(isTreePopulated) {
			 $("#show").css('display', 'none');
			 $("#hide").css('display', 'block');
			 $("#gap").width("3%");
			 $("#orgSlider").width("210");
			 $("#sessionGrid").width("990");
			 document.getElementById('orgSlider').style.display = 'block';
			// $("#sessionGrid").css('padding-left',10);
			$('#orgSlider').show('slide', {direction: 'left'}, 50);
			var width = jQuery("#sessionGrid").width();
		    width = width - 80; // Fudge factor to prevent horizontal scrollbars
		    
		    jQuery("#list2").setGridWidth(916);
		    jQuery("#list3").setGridWidth(916);
	    } else {
	    	UIBlock();
	    	populateTree();
	    }
	    
	}
	
	function hideTreeSlider() {
		 openTreeRequested  = false;
		 $("#hide").css('display', 'none');
		 $("#show").css('display', 'block');
		 $("#gap").width("0%");
		 $("#orgSlider").width("0");
		 $("#sessionGrid").width("1175");
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
		async:		true,
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
						createSingleNodeSelectedTree (orgTreeHierarchy);
						$("#searchheader").css("visibility","visible");	
						$("#orgNodeHierarchy").css("visibility","visible");	
												
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						//$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					},
		complete :  function(){
						// $.unblockUI();  
					}
	});

}


function createSingleNodeSelectedTree(jsondata) {
	   $("#orgNodeHierarchy").jstree({
	        "json_data" : {	             
	            "data" : jsondata.data,
				"progressive_render" : true,
				"progressive_unload" : false
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
	    	//clearMessage();
	    	SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#treeOrgNodeId").val(SelectedOrgNodeId);
	    	topNodeSelected = $(this).parent().attr("categoryId");
	    	if(topNodeSelected == '1'){
	    	 	openConfirmationPopup();
	    	} else {
	    		reset();
 		    	gridReload(false);
	    	}
  			
 		   
 		 	
		});
	   $("#orgNodeHierarchy").bind("loaded.jstree", 
		 	function (event, data) {
		 	    isTreePopulated = true; 
				if(openTreeRequested){
					$.unblockUI();  
					showTreeSlider();
				}
			}
		);
}

	
	 function reset() { 
		$('#accordion').accordion('activate', 0 );
		$("#CurrentFuture").scrollTop(0);
		$("#Completed").scrollTop(0);
		
		document.getElementById('ShowButtons').style.display = "block";
 		setAnchorButtonState('viewStatusButton', true);
 		
 		 if($("#canRegisterStudent").val() == 'true'){
 			setAnchorButtonState('registerStudentButton', true);
 		}
 		
 		
	}
	
	 function gridReload(homePageLoad){ 
	 	   ishomepageload = homePageLoad;
	  		UIBlock();
           jQuery("#list2").jqGrid('setGridParam',{datatype:'json'});     
     	    var sortArrow = jQuery("#list2");
     	   var urlVal = 'getSessionForUserHomeGrid.do';
     	   if(!homePageLoad) {
 				urlVal = 'getSessionForSelectedOrgNodeGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val();
	 		}
     	   jQuery("#list2").jqGrid('setGridParam', {url:urlVal ,page:1}).trigger("reloadGrid");
     	   
           jQuery("#list2").sortGrid('loginEndDate',false);
           var arrowElements = sortArrow[0].grid.headers[0].el.lastChild.lastChild;
           $(arrowElements.childNodes[0]).removeClass('ui-state-disabled');
           $(arrowElements.childNodes[1]).addClass('ui-state-disabled');
      }
      
      function gridReloadPA(homePageLoad){ 
	  	   jQuery("#list3").jqGrid('setGridParam',{datatype:'json'});     
     	   var urlVal = 'getCompletedSessionForGrid.do';
     	   jQuery("#list3").jqGrid('setGridParam', {url:urlVal ,page:1}).trigger("reloadGrid");
           var sortArrowPA = jQuery("#list3");
           jQuery("#list3").sortGrid('loginEndDate',false);
         	var arrowElementsPA = sortArrowPA[0].grid.headers[0].el.lastChild.lastChild;
           $(arrowElementsPA.childNodes[0]).removeClass('ui-state-disabled');
           $(arrowElementsPA.childNodes[1]).addClass('ui-state-disabled');

      }
      
      function gridReloadStu(addStudent){ 
	      if(addStudent){
	      	jQuery("#list6").jqGrid('setGridParam',{datatype:'local'});     
	      } else {
	      	jQuery("#list6").jqGrid('setGridParam',{datatype:'json'});    
	      }
	  	   var urlVal = 'getCompletedSessionForGrid.do';
     	   jQuery("#list6").jqGrid('setGridParam', {url:urlVal ,page:1}).trigger("reloadGrid");
           var sortArrowPA = jQuery("#list6");
           jQuery("#list6").sortGrid('lastName',false);
         	var arrowElementsPA = sortArrowPA[0].grid.headers[0].el.lastChild.lastChild;
           $(arrowElementsPA.childNodes[0]).removeClass('ui-state-disabled');
           $(arrowElementsPA.childNodes[1]).addClass('ui-state-disabled');

      }
		
		
		function getDataFromJson(id, sessionList){
			var str = sessionList;
			var isRegisterStudentEnable = "";
			var indexOfId = str.indexOf(id);
			var indexOfisRegisterStudentEnable = -1;
			var indexOfComma = -1;
			if(indexOfId > 0){
				str = str.substring(parseInt(indexOfId), str.length);
				indexOfisRegisterStudentEnable = str.indexOf("isRegisterStudentEnable");
				indexOfComma = str.indexOf(',', parseInt(indexOfisRegisterStudentEnable));
				indexOfisRegisterStudentEnable += 25;
				isRegisterStudentEnable = str.substring(parseInt(indexOfisRegisterStudentEnable), parseInt(indexOfComma));
				isRegisterStudentEnable = $.trim(isRegisterStudentEnable);
			}else{
				
			}
			return isRegisterStudentEnable;
	}
 
 
 	function closePopUp(dailogId){
 		if(dailogId == 'scheduleSession') {
			$('#ssAccordion').accordion('activate', 0 );
			$("#select_Test").scrollTop(0);
			$("#test_Detail").scrollTop(0);
			$("#add_Student").scrollTop(0);
			$('#add_Proctor').scrollTop(0);
			$("#test_Detail").hide();
			$("#add_Student").hide();
			$('#add_Proctor').hide();
		}
		$("#"+dailogId).dialog("close");
	}
	
	
	function fetchDataOnConfirmation() {
		closePopUp('confirmationPopup');
		reset();
 		gridReload(false);
	}
	
	function openConfirmationPopup(){
	$("#confirmationPopup").dialog({  
		title:"Confirmation Alert",  
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
	
	
	function scheduleSession(){
		if(!stuGridloaded) {
	   		populateSelectedStudent();
	   	} else {
	   		gridReloadStu(true);
	   	}
		$("#scheduleSession").dialog({  
			title:"Schedule Session",  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '1200px',
		 	modal: true,
		 	closeOnEscape: false,
		 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		});
		var width = jQuery("#scheduleSession").width();
	    width = width - 72; // Fudge factor to prevent horizontal scrollbars
	    
		if(isTabeProduct) {
			
			$("#list6").jqGrid("hideCol","itemSetForm"); 
			jQuery("#list6").setGridWidth(width,true);
		} else {
			
			$("#list6").jqGrid("showCol","itemSetForm"); 
			jQuery("#list6").setGridWidth(width,false);
		}
		$("#select_Test").css('height',400);
		$("#test_Detail").css('height',400);
		$("#add_Student").css('height',400);
		$("#add_Proctor").css('height',400);
		
				
		 var toppos = ($(window).height() - 700) /2 + 'px';
		 var leftpos = ($(window).width() - 1200) /2 + 'px';
		 $("#scheduleSession").parent().css("top",toppos);
		 $("#scheduleSession").parent().css("left",leftpos);		
	
	}
			
	function reloadHomePage(){
		reset();
		hideTreeSlider();
		gridReload(true);
	}	
	
	
	
function populateSelectedStudent() {
 		UIBlock();
 		var studentIdTitle = $("#studentIdLabelName").val();
 		stuGridloaded = true;
 		$("#list6").jqGrid({         
          url: 'getSelectedStudentList.do', 
		 type:   'POST',
		 datatype: "local",         
          colNames:[ 'Last Name','First Name', 'M.I.', studentIdTitle, 'Accommodations', leafNodeCategoryName , 'Form'],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:150, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:150, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:150, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentId',index:'studentId', width:225, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations', width:145, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeName',index:'orgNodeName',editable: true, width:150, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetForm',index:'itemSetForm',editable: true, width:75, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"studentNode", id:"studentId",
		   	records: function(obj) { 
		   	 //sessionListCUFU = JSON.stringify(obj.studentNode);
		   	 } },
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:true,
			pager: '#pager6', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,  
			editurl: 'getSelectedStudentList.do',
			caption:"Session List",
		//	ondblClickRow: function(rowid) {viewEditStudentPopup();},
			onPaging: function() {
				//clearMessage();
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
			loadComplete: function () {
				if ($('#list6').getGridParam('records') === 0) {
				 	isStuGridEmpty = true;
            	 	$('#sp_1_pager6').text("1");
            	} else {
            		isStuGridEmpty = false;
            	}
            	
            	
			    setEmptyListMessage('studentGrid');
				$.unblockUI();  
				$("#list6").setGridParam({datatype:'local'});
				var tdList = ("#pager6_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/TestSessionInfoWeb/logout.do";
						
			}
	 });
	 jQuery("#list6").navGrid('#pager6', {
				addfunc: function() {
					
		    	}	    	
			});
	var element = document.getElementById('add_list6');
	element.style.display = 'none'; 
	var element = document.getElementById('edit_list6');
	element.style.display = 'none'; 
	var element = document.getElementById('search_list6');
	element.style.display = 'none';  
	var element = document.getElementById('del_list6');
	element.title = 'Remove Student'; 
}
		
				 