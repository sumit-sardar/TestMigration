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

var isTestGroupLoad = false;
var testGridLoaded = false;
var subtestGridLoaded = false;
var isTestGridEmpty = true;
var subtestLength = 0;
var ProductData;


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
					scheduleNewSession();
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
	
	function getDataFromTestJson(id, sessionList){
			var sessionListArr = new Array();
			sessionListArr = sessionList;
			var str = new Array();;
			var indexOfId;
			for(var i=0; i<sessionList.length ;i++){
				if(sessionList[i].id == id){
					document.getElementById("aCode").style.display = "";
					document.getElementById("aCode").value = sessionList[i].accessCode;
					document.getElementById("testSessionName_lbl").innerHTML = sessionList[i].testName;
					document.getElementById("testSessionName").value = sessionList[i].testName;					
					str = sessionList[i].subtests;					
					break;					
				}
			}
			return str;
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
		 	width: '1024px',
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
		setPopupPosition();
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
	
	function scheduleNewSession() {

	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'selectTest.do?currentAction=init',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						ProductData = data;
						var selectedproductId= data.selectedProductId;
						fillProductGradeLevelDropDown('testGroupList',data.product,selectedproductId);
						fillDropDown("timeZoneList",data.testZoneDropDownList);
						fillDropDown("topOrgNode",data.topNodeDropDownList)
						populateTestListGrid(data.product[0].testSessionList,true);
						processStudentAccordion();
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
		title:"Schedule Session",  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '1024px',
	 	modal: true,
	 	closeOnEscape: false,
	 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
	});
	setPopupPosition();
	}
	
	function processStudentAccordion() {
		if(!stuGridloaded) {
   			populateSelectedStudent();
   		} else {
   			gridReloadStu(true);
   		}
   		
   		var width = jQuery("#scheduleSession").width();
    	width = width - 72; // Fudge factor to prevent horizontal scrollbars
    
		if(isTabeProduct) {
			
			$("#list6").jqGrid("hideCol","itemSetForm"); 
			jQuery("#list6").setGridWidth(width,true);
		} else {
			
			$("#list6").jqGrid("showCol","itemSetForm"); 
			jQuery("#list6").setGridWidth(width,true);
		}
	}

	function fillProductGradeLevelDropDown( elementId, optionList, selectedProductId) {
		var ddl = document.getElementById(elementId);
		var optionHtml = "" ;
		//var optionList = data.product;
		if(optionList.length < 1) {
			optionHtml += "<option  value='Select a product'>Show All</option>";
		} else {
			for(var i = 0; i < optionList.length; i++ ) {
				if(selectedProductId==optionList[i].productId) { 	     
					optionHtml += "<option  value='"+ optionList[i].productId+"'selected >"+ optionList[i].productName+"</option>";
					//fillDropDown("grade", optionList[i].gradeDropDownList);
					if(!(optionList[i].isTabeProduct)) {
						isTabeProduct = false;
						if(!(optionList[i].hideLevelDropDown)) {
							if(optionList[i].showLevelOrGrade=='level') {
								document.getElementById("levelDiv").style.display ="block";
								document.getElementById("level").style.display = "block";
								document.getElementById("gradeDiv").style.display = "none";
								fillDropDown("level",optionList[i].levelDropDownList);	
							} else if (optionList[i].showLevelOrGrade=='grade') {
								document.getElementById("gradeDiv").style.display ="block";
								document.getElementById("level").style.display = "block";
								document.getElementById("levelDiv").style.display = "none";
								fillDropDown("level",optionList[i].levelDropDownList);	
							} else { 
							document.getElementById("gradeDiv").style.display = "none";
							document.getElementById("levelDiv").style.display = "none";
							document.getElementById("level").style.display = "none";
							}
						} else {
							//fillDropDown("level",optionList[i].levelDropDownList);
							document.getElementById("gradeDiv").style.display = "none";
							document.getElementById("levelDiv").style.display = "none";
							document.getElementById("level").style.display = "none";	
						}				
					
					} else {
						isTabeProduct = true;
						document.getElementById("gradeDiv").style.display = "none";
						document.getElementById("levelDiv").style.display = "none";
						document.getElementById("level").style.display = "none";
					}
					
				} else {
					optionHtml += "<option  value='"+ optionList[i].productId+"'>"+ optionList[i].productName+"</option>";
				}
				
			}
		}
		$(ddl).html(optionHtml);
	}
	


	function fillDropDown( elementId, optionList) {
		var ddl = document.getElementById(elementId);
		var optionHtml = "" ;
		if(optionList.length < 1) {
			optionHtml += "<option  value='Select'>Select</option>";
		} else {
			for(var i = 0; i < optionList.length; i++ ) {		     
				optionHtml += "<option  value='"+ optionList[i].id+"'>"+ optionList[i].name+"</option>";	
			}
		}
		$(ddl).html(optionHtml);
	}

	function  changeGradeAndLevel(){
		var e = document.getElementById("testGroupList");  
		var selectProductId = e.options[e.selectedIndex].value;
		var optionList = ProductData.product
		for(var i = 0; i < optionList.length; i++ ) {
			if(selectProductId==optionList[i].productId) { 	     
				if(!(optionList[i].isTabeProduct)) {
					isTabeProduct = false;
					if(!(optionList[i].hideLevelDropDown)) {
						if(optionList[i].showLevelOrGrade=="level") {
							document.getElementById("levelDiv").style.display ="block";
							document.getElementById("level").style.display = "block";
							document.getElementById("gradeDiv").style.display = "none";
							fillDropDown("level",optionList[i].levelDropDownList);	
						} else if (optionList[i].showLevelOrGrade=="grade") {
							document.getElementById("gradeDiv").style.display ="block";
							document.getElementById("level").style.display = "block";
							document.getElementById("levelDiv").style.display = "none";
							fillDropDown("level",optionList[i].levelDropDownList);	
						} else { 
							//fillDropDown("level",optionList[i].levelDropDownList);	
							document.getElementById("gradeDiv").style.display = "none";
							document.getElementById("levelDiv").style.display = "none";
							document.getElementById("level").style.display = "none";
						}
					} else {
						document.getElementById("gradeDiv").style.display = "none";
						document.getElementById("levelDiv").style.display = "none";
						document.getElementById("level").style.display = "none";
						//fillDropDown("level",optionList[i].levelDropDownList);	
					}
						
				} else {
					isTabeProduct = true;
					document.getElementById("gradeDiv").style.display = "none";
					document.getElementById("levelDiv").style.display = "none";
					document.getElementById("level").style.display = "none";
				}
				var tList = optionList[i].testSessionList;
				var noOfRows = tList.length;
				
				$('#testList').GridUnload();
				
				populateTestListGrid(tList, true);
				break;
			} 
					
		}
		processStudentAccordion();

	}

	function setPopupPosition(){
				var toppos = ($(window).height() - 512) /2 + 'px';
				var leftpos = ($(window).width() - 1024) /2 + 'px';
				$("#Select_Test").parent().css("top",toppos);
				$("#Select_Test").parent().css("left",leftpos);
				$("#Select_Test").css("overflow",'auto');
				$("#Select_Test").css("height",'400px');
				$("#Test_Detail").css("overflow",'auto');
				$("#Test_Detail").css("height",'400px');
				$("#Add_Student").css("overflow",'auto');
				$("#Add_Student").css("height",'400px');
				$("#Add_Proctor").css("overflow",'auto');
				$("#Add_Proctor").css("height",'400px');			
				
	}
	
	function toggleAccessCode(){
		if(subtestLength > 0){
			var testBreak = document.getElementById("testBreak");
			if(!testBreak.checked){		
				document.getElementById("aCodeHead").style.display = "none";
				document.getElementById("aCode").style.display = "inline";			
				for(var i=0;i<subtestLength;i++){
					document.getElementById("aCodeB"+i).style.display = "none";
				}
			}else{
				document.getElementById("aCodeHead").style.display = "inline";		
				document.getElementById("aCode").style.display = "none";
				for(var i=0;i<subtestLength;i++){
					document.getElementById("aCodeB"+i).style.display = "inline";
				}
			}
		}
	}


	function slideTime(event, ui){
	    var minutes0 = parseInt($("#slider-range").slider("values", 0) % 60);
	    var hours0 = parseInt($("#slider-range").slider("values", 0) / 60 % 24);
	    var minutes1 = parseInt($("#slider-range").slider("values", 1) % 60);
	    var hours1 = parseInt($("#slider-range").slider("values", 1) / 60 % 24);
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
	    if (hours > 12) {hours = hours - 12; }
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
	    //console.log(startTime);
	    //console.log(endTime);
	    $('#Schedule tbody').append('<tr>' +
	        '<td>' + startTime + '</td>' +
	        '<td>' + endTime + '</td>' +
	        '</tr>');
	}
	
	function populateTestListGrid(testSessionlist, testGroupLoad) {
		isTestGroupLoad  = testGroupLoad;
 		UIBlock();
 		//populateTree();
 		reset();
 		$("#testList").jqGrid({         
        	data: testSessionlist,
			datatype: "local",         
			colNames:['Test Name','Level', 'Subtest', 'Dur(mins)'],
		   	colModel:[
		   		{name:'testName',index:'testName', width:60, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'level',index:'level', width:15, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'subtestCount',index:'subtestCount', width:15, editable: false, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'duration',index:'duration',editable: false, width:15, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   	],
		   	jsonReader: { repeatitems : false, root:"rows", id:"id",
		   		records: function(obj) { 
		   	 		//sessionListCUFU = JSON.stringify(obj.studentNode);
		   	 	} },
		   	
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			shrinkToFit: true,
			pager: '#testPager', 
			sortname: 'testName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 150,
			caption:"Test List",
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
					subtestGridLoaded = false;
					var selectedTestId = $("#testList").jqGrid('getGridParam', 'selrow');
					var testBreak = document.getElementById("testBreak");
					if(testBreak.checked)
						testBreak.checked = false;
					var val = getDataFromTestJson(selectedTestId, testSessionlist);
					createSubtestGrid(val);
			 		
			},
			loadComplete: function () {
				if ($('#testList').getGridParam('records') === 0) {
				 	isTestGridEmpty = true;
				 	$('#sp_1_testPager').text("1");
            	} else {
            		isTestGridEmpty = false;
            	}
            	
			
            	var width = jQuery("#Select_Test").width();
			    width = width/2 - 30; // Fudge factor to prevent horizontal scrollbars
			    jQuery("#testList").setGridWidth(width);
			    setEmptyListMessage('CUFU');
				$.unblockUI();  
				$("#testList").setGridParam({datatype:'local'});
				var tdList = ("#testPager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/TestSessionInfoWeb/logout.do";
						
			}
	 });
	 jQuery("#testList").jqGrid('navGrid', '#testPager', { edit: false, add: false, del: false, search: false, refresh: false });
	 		
	}
	
	
	function createSubtestGrid(subtestsArr){
		var subtestArr = new Array();
		subtestArr = subtestsArr;
		var subtestData = '';
		//alert(subtestGridLoaded);
		if(!subtestGridLoaded && subtestArr.length > 0){
			subtestLength = subtestArr.length;
			document.getElementById("subtestGrid").style.display = "";
			document.getElementById("noSubtest").style.display = "none";
			var tr = '';
			var th = '';
			subtestData +='<table width="100%" cellpadding="2" cellspacing="1" class="shadowBorder">';
							
			th +='<tr class="subtestHeader">';
			th +='<th width="24" height="20" align="center"><strong>#</strong></th>';
			th +='<th width="287" height="20" align="left"><strong>Subtest Name </strong></th>';
			th +='<th width="144" height="20"><div align="center" id="aCodeHead" style="display:none;"><strong>Access Code </strong></div></th>';
			th +='<th width="73" height="20" align="center"><strong>Duration</strong></th>';
			if(isTabeProduct){
				th +='<th width="34" height="20" id="removeSubtestHead">&nbsp;</th>';
			}
			th +='</tr>';
			subtestData += th;
			for(var i=0;i<subtestArr.length; i++){	
				tr = ''			
				tr +='<tr>';
				tr +='<td height="20" class="transparent">';
				tr +='<div align="center" id="num'+i+'">'+parseInt(i+1)+'</div>';
				tr +='</td>';
				tr +='<td height="20" class="transparent">';
				tr +='<div align="left" id="sName'+i+'">'+subtestArr[i].subtestName+'</div>';
				tr +='</td>';
				tr +='<td height="20" class="transparent">';
				tr +='<div align="center" id="aCodeDiv'+i+'">';
				tr +='<input name="aCodeB'+i+'" type="text" class="norBox" size="16" id="aCodeB'+i+'" value="'+subtestArr[i].testAccessCode+'" style="display: none; padding-left:2px;"/></div>';
				tr +='</td>';
				tr +='<td height="20" class="transparent">';
				tr +='<div align="center" id="duration'+i+'">'+subtestArr[i].duration+'</div>';
				tr +='</td>';
				if(isTabeProduct){
					tr +='<td height="20" class="transparent" id="removeSubtestCol'+i+'">';
					tr +='<div align="center">';
					tr +='<img id="imgMin" src="images/minus.gif" width="14" title="Remove" onclick="toggleRows(0,1);" />';
					tr +='<img id="imgPlus" src="images/icone_plus.gif" width="14" title="Add" onclick="toggleRows(1,1);" style="display: none;" />';
					tr +='</div>';
					tr +='</td>';
				}
				tr +='</tr>';				
				subtestData += tr;		
			}
			subtestData +='</table>';
			document.getElementById("subtestGrid").innerHTML = subtestData;
			subtestGridLoaded = true;
		}/*else{
			subtestLength = 0;
			subtestGridLoaded = false;
			subtestData = "";
			document.getElementById("subtestGrid").style.display = "none";
			document.getElementById("noSubtest").style.display = "";
		}*/
	}
	
	function removeSubtestOption(removeSubtest,subtestCount){
		if(removeSubtest){
			document.getElementById('removeSubtestHead').style.display = "none";
			for(var i=0; i<subtestCount; i++){
				document.getElementById('removeSubtestCol'+[i]).style.display = "none";
			}
		}else{
			document.getElementById('removeSubtestHead').style.display = "";
			for(var i=0; i<subtestCount; i++){
				document.getElementById('removeSubtestCol'+[i]).style.display = "";
			}
			
		}
	}
			
					 