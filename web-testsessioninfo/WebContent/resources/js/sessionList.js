var leafNodeCategoryId;
var orgTreeHierarchy;
var gridloaded = false;

var isTreePopulated = false;
var openTreeRequested = false;

function UIBlock(){
	$.blockUI({ message: '<img src="/SessionWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'45px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}


function populateSessionListGrid(homePageLoad) {
 		UIBlock();
 		//populateTree();
 		reset();
 		$("#list2").jqGrid({         
          url: 'getSessionForUserHomeGrid.do', 
		 type:   'POST',
		 datatype: "json",         
          colNames:['Session Name','Test Name', 'State', 'My Role','Start Date', 'End Date'],
		   	colModel:[
		   		{name:'testAdminName',index:'testAdminName', width:225, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testName',index:'testName', width:225, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'creatorOrgNodeName',index:'creatorOrgNodeName', width:175, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'AssignedRole',index:'AssignedRole',editable: true, width:100, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginStartDate',index:'loginStartDate', width:175, editable: true, align:"left", sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d,Y h:i:s', newformat:'m/d/Y'}, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginEndDate',index:'loginEndDate', width:200, editable: true, align:"left",sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d,Y h:i:s', newformat:'m/d/Y'}, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"testSessionCUFU", id:"testAdminId",
		   	records: function(obj) { 
		   	if(homePageLoad) {
		   		//populateTree();
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
				//if ($("#roleNameID").val() == 'Administrator') {
					setAnchorButtonState('viewStatusButton', false);
					if($("#userScheduleAndFindSessionPermission").val() == 'true'){
			 			setAnchorButtonState('scSessionButton', false);
			 		}
					if($("#canRegisterStudent").val() == 'true'){
			 			setAnchorButtonState('registerStudentButton', false);
			 		}
				//}
				//document.getElementById('displayMessageMain').style.display = "none";	
			},
			loadComplete: function () {
				if ($('#list2').getGridParam('records') === 0) {
            	$('#sp_1_pager2').text("1");
            	}
            	if(!gridloaded) {
            		populateCompletedSessionListGrid();
            	} else {
            		gridReloadPA();
            	}
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
	 jQuery("#list2").jqGrid('navGrid','#pager2',{});  
}


function populateCompletedSessionListGrid() {
 		//UIBlock();
 		gridloaded = true;
 		reset();
       $("#list3").jqGrid({         
          url: 'getCompletedSessionForGrid.do', 
		  type:   'POST',
		  datatype: "json",          
          colNames:['Session Name','Test Name', 'State', 'My Role','Start Date', 'End Date'],
		   	colModel:[
		   		{name:'testAdminName',index:'testAdminName', width:225, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testName',index:'testName', width:225, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'creatorOrgNodeName',index:'creatorOrgNodeName', width:175, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'AssignedRole',index:'AssignedRole',editable: true, width:100, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginStartDate',index:'loginStartDate', width:175, editable: true, align:"left", sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d,Y h:i:s', newformat:'m/d/Y'}, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginEndDate',index:'loginEndDate', width:200, editable: true, align:"left", sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d,Y h:i:s', newformat:'m/d/Y'},sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"testSessionPA", id:"testAdminId" },
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
				//if ($("#roleNameID").val() == 'Administrator') {
					setAnchorButtonState('viewStatusButtonPA', false);
					if($("#canRegisterStudent").val() == 'true'){
			 			setAnchorButtonState('registerStudentButtonPA', false);
			 		}
				//}
				//document.getElementById('displayMessageMain').style.display = "none";	
			},
			loadComplete: function () {
				if ($('#list3').getGridParam('records') === 0) {
            	$('#sp_1_pager3').text("1");
            	}
            	$.unblockUI();  
				$("#list3").setGridParam({datatype:'local'});
				var tdList = ("#pager3_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#list3").jqGrid('navGrid','#pager3',{});
	 setupButtonPerUserPermission();
	 
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
			 $("#hide").css('display', 'inline-block');
			 $("#gap").width("3%");
			 $("#orgSlider").width("17%");
			 $("#sessionGrid").width("82%");
			 document.getElementById('orgSlider').style.display = 'block';
			 $("#sessionGrid").css('padding-left',10);
			$('#orgSlider').show('slide', {direction: 'left'}, 50);
			var width = jQuery("#sessionGrid").width();
		    width = width - 80; // Fudge factor to prevent horizontal scrollbars
		    
		    jQuery("#list2").setGridWidth(width);
		    jQuery("#list3").setGridWidth(width);
	    } else {
	    	UIBlock();
	    	populateTree();
	    }
	    
	}
	
	function hideTreeSlider() {
		 openTreeRequested  = false;
		 $("#hide").css('display', 'none');
		 $("#show").css('display', 'inline-block');
		 $("#gap").width("0%");
		 $("#orgSlider").width("0%");
		 $("#sessionGrid").width("100%");
		 $("#sessionGrid").css('padding-left',0);
		$('#orgSlider').show('slide', {direction: 'right'}, 1000);
		document.getElementById('orgSlider').style.display = 'none';
		var width = jQuery("#sessionGrid").width();
	    width = width -80; // Fudge factor to prevent horizontal scrollbars
	    
	    jQuery("#list2").setGridWidth(width);
	    jQuery("#list3").setGridWidth(width);
	    
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
 		    reset();
 		    gridReload(false);
 		 	
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
 		 if($("#userScheduleAndFindSessionPermission").val() == 'true'){
 			setAnchorButtonState('scSessionButton', true);
 		}
 		 if($("#canRegisterStudent").val() == 'true'){
 			setAnchorButtonState('registerStudentButton', true);
 		}
 		
 		document.getElementById('ShowButtonsPA').style.display = "block";
 		setAnchorButtonState('viewStatusButtonPA', true);
 		 if($("#canRegisterStudent").val() == 'true'){
 			setAnchorButtonState('registerStudentButtonPA', true);
 		}
	}
	
	 function gridReload(homePageLoad){ 
	  		UIBlock();
           jQuery("#list2").jqGrid('setGridParam',{datatype:'json'});     
     	    var sortArrow = jQuery("#list2");
     	   var urlVal = 'getSessionForUserHomeGrid.do';
     	   if(!homePageLoad) {
 				urlVal = 'getSessionForSelectedOrgNodeGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val();
	 		}
     	   jQuery("#list2").jqGrid('setGridParam', {url:urlVal ,page:1}).trigger("reloadGrid");
     	   
           jQuery("#list2").sortGrid('loginEndDate',true);
           var arrowElements = sortArrow[0].grid.headers[0].el.lastChild.lastChild;
           $(arrowElements.childNodes[0]).removeClass('ui-state-disabled');
           $(arrowElements.childNodes[1]).addClass('ui-state-disabled');
      }
      
      function gridReloadPA(homePageLoad){ 
	  	   jQuery("#list3").jqGrid('setGridParam',{datatype:'json'});     
     	   var urlVal = 'getCompletedSessionForGrid.do';
     	   jQuery("#list3").jqGrid('setGridParam', {url:urlVal ,page:1}).trigger("reloadGrid");
           var sortArrowPA = jQuery("#list3");
           jQuery("#list3").sortGrid('loginEndDate',true);
         	//var arrowElementsPA = sortArrowPA[0].grid.headers[0].el.lastChild.lastChild;
           //$(arrowElementsPA.childNodes[0]).removeClass('ui-state-disabled');
          // $(arrowElementsPA.childNodes[1]).addClass('ui-state-disabled');

      }
	
 