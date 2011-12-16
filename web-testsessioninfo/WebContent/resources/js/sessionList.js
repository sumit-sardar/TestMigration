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
var previousValue = "";
var previousSubtest = "";
var testJSONValue = "";
var selectedTestId = "";
var selectedSubtestId = "";

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
					$('#showSaveTestMessage').hide();
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
            	 	$('#next_pager2').addClass('ui-state-disabled');
            	 	$('#last_pager2').addClass('ui-state-disabled');
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
				$('#showSaveTestMessage').hide();
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
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
					$('#showSaveTestMessage').hide();
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
            		$('#next_pager3').addClass('ui-state-disabled');
            	 	$('#last_pager3').addClass('ui-state-disabled');
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
				
				$('#showSaveTestMessage').hide();
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
		$('#showSaveTestMessage').hide();
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
		 $('#showSaveTestMessage').hide();
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
						window.location.href="/SessionWeb/logout.do";
						
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
	    	//loadSessionGrid = true;
	    	$('#showSaveTestMessage').hide();
	    	var SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#treeOrgNodeId").val(SelectedOrgNodeId);
	    	var topNodeSelected = $(this).parent().attr("categoryId");
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
      	$('#list6').GridUnload();		
      	populateSelectedStudent();
      	
      	
      	/*UIBlock();
	      if(addStudent){
	      	jQuery("#list6").jqGrid('setGridParam',{datatype:'local'});     
	      } else {
	      	jQuery("#list6").jqGrid('setGridParam',{datatype:'json'}); 
	      	jQuery("#list6").jqGrid('setGridParam',{postData:{'selectedStudentList':AddStudentLocaldata} });   
	      }
	     
	  	   var urlVal = 'getSelectedStudentList.do?q=2';
     	   jQuery("#list6").jqGrid('setGridParam', {url:urlVal ,page:1}).trigger("reloadGrid");
           var sortArrowPA = jQuery("#list6");
           jQuery("#list6").sortGrid('lastName',true);
         	var arrowElementsPA = sortArrowPA[0].grid.headers[0].el.lastChild.lastChild;
           $(arrowElementsPA.childNodes[0]).removeClass('ui-state-disabled');
           $(arrowElementsPA.childNodes[1]).addClass('ui-state-disabled'); */

      }
      
      function gridReloadSelectStu(){ 
      	  UIBlock();
	      jQuery("#selectStudent").jqGrid('setGridParam',{datatype:'json'});    
	       var urlVal = 'getStudentForList.do?q=2&stuForOrgNodeId='+$("#stuForOrgNodeId").val()+'&blockOffGradeTesting='+blockOffGradeTesting+'&selectedLevel='+selectedLevel;
     	   jQuery("#selectStudent").jqGrid('setGridParam', {url:urlVal ,page:1}).trigger("reloadGrid");
           var sortArrowPA = jQuery("#selectStudent");
           jQuery("#selectStudent").sortGrid('lastName',false);
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
			var str = new Array();
			var indexOfId;
			var found = false;
			for(var i=0; i<sessionList.length ;i++){
				if(sessionList[i].id == id){
				    found = true;
				    blockOffGradeTesting = sessionList[i].offGradeTestingDisabled;
				    if(blockOffGradeTesting == null || blockOffGradeTesting == undefined || blockOffGradeTesting == "")
				    	blockOffGradeTesting = false;
				    selectedLevel = sessionList[i].level;
				    if(selectedLevel == null || selectedLevel == undefined || selectedLevel == "")
				    	selectedLevel = "Show All"
				    
					document.getElementById("aCode").style.visibility = "visible";
					if(sessionList[i].subtests.length>0)  {
						document.getElementById("aCode").value = ProductData.accessCodeList[0];
					} else {
						document.getElementById("aCode").value = "";
					}
					document.getElementById("testSessionName_lbl").innerHTML = sessionList[i].testName;
					document.getElementById("testSessionName").value = sessionList[i].testName;	
					str = sessionList[i].subtests;
					if(sessionList[i].isRandomize == "Y" ){
						$("#randomDis").show();	
						$("#randDisLbl").show();
						$("#randomDis").val("Y");
						//$("#randomDistDiv").show();
						document.getElementById("randomDis").checked = true;
					}else if(sessionList[i].isRandomize == "N" ){
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
					document.getElementById("startDate").value = sessionList[i].startDate;
					document.getElementById("endDate").value = sessionList[i].endDate;
					currDate = sessionList[i].startDate;
					nextDate = sessionList[i].endDate;
					$( "#startDate" ).datepicker( "option" , "minDate" , currDate ) ;
					$( "#endDate" ).datepicker( "option" , "minDate" , currDate ) ;
					$( "#endDate" ).datepicker( "refresh" );
					$( "#startDate" ).datepicker( "refresh" );
					//$("#endDate").val(nextDate);
					break;					
				}
			}
			if(!found) {
				$("#randomDis").hide();	
				$("#randDisLbl").hide();
				$("#randomDis").val("");
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

		}
		$("#"+dailogId).dialog("close");
	}
	
	
	function fetchDataOnConfirmation() {
		closePopUp('confirmationPopup');
		reset();
		//if(loadSessionGrid) {
 			gridReload(false);
 		//} else {
 		//	gridReloadSelectStu();
 		//}
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
	
	function removeStuConfirmationPopup(){
	$("#removeStuConfirmationPopup").dialog({  
		title:"Confirmation Alert",  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '400px',
	 	height: '160px',
	 	modal: true,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 $("#removeStuConfirmationPopup").css('height','110px');
		 var toppos = ($(window).height() - 290) /2 + 'px';
		 var leftpos = ($(window).width() - 410) /2 + 'px';
		 $("#removeStuConfirmationPopup").parent().css("top",toppos);
		 $("#removeStuConfirmationPopup").parent().css("left",leftpos);	
		 
	}	
	
	
	function reloadHomePage(){
		
		reset();
		hideTreeSlider();
		gridReload(true);
		$('#showSaveTestMessage').hide();
	}	
	
	
	
	function populateSelectedStudent() {
 		UIBlock();
 		var studentIdTitle = $("#studentIdLabelName").val();
 		delStuIdObjArray = [];
 		stuGridloaded = true;
 		$("#list6").jqGrid({  
 		 data:  AddStudentLocaldata,
         datatype: 'local',         
          colNames:[ 'Last Name','First Name', 'M.I.', studentIdTitle, 'Accommodations', leafNodeCategoryName , 'Form', 'studentId'],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:130, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:130, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:120, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userName',index:'userName', width:275, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations', width:165, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter: 'link' },
		   		{name:'orgNodeName',index:'orgNodeName',editable: true, width:150, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetForm',index:'itemSetForm',editable: true, width:75, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentId',index:'studentId',editable: false, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
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
			height: 151,  
			caption:"Student List",
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
			gridComplete: function() {
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
				delStuIdObjArray = [];
				deleteStudentCounter = 0;
				if(status) {
					selectAllForDelete = true;
					for(var i = 0; i < AddStudentLocaldata.length; i++) {
						delStuIdObjArray[deleteStudentCounter] = AddStudentLocaldata[i].studentId;
						deleteStudentCounter++;
					}
				} else {
					selectAllForDelete = false;					
				}
			},
			onSelectRow: function (rowid, status) {
				//selectAllForDelete = false;
				var selectedRowData = $("#list6").getRowData(rowid);
				var selectedRowId = selectedRowData.studentId;
				if(status) {
					if(!checkPresenceInDelStuIdObjArray(selectedRowId)) {
						delStuIdObjArray[deleteStudentCounter] = selectedRowId;
						deleteStudentCounter++;
					}
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
				}
			},
			loadComplete: function () {
            	if ($('#list6').getGridParam('records') === 0) {
				 	isStuGridEmpty = true;
				 	studentTempMap = new Map();
				 	studentMap = new Map();
				 	studentTempIndexMap = new Map();
				 	studentIndexMap = new Map();
				 	studentTempIndexCount = 0;
				 	studentIndexCount = 0;
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
		    	width = width - 72; // Fudge factor to prevent horizontal scrollbars
		    
				if(isTabeProduct) {
					
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
	 jQuery("#list6").navGrid('#pager6', {
				delfunc: function() {
				if(delStuIdObjArray.length > 0 ) {
					removeStuConfirmationPopup();
					
				}
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
						if(allSelectOrg[k] == (studentTempMap.get(delStuIdObjArray[i])).orgNodeId) {
							allSelectOrg.splice(k,1);
							countAllSelect--;
						}
					}
				}
				studentTempMap.put(delStuIdObjArray[i],null);
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
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'selectTest.do?currentAction=init',
		type:		'POST',
		dataType:	'json',
		contentType: 'application/json; charset=UTF-8', 
		success:	function(data, textStatus, XMLHttpRequest){
						ProductData = data;
						
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
							fillDropDown("topOrgNode",data.topNodeDropDownList)
							processStudentAccordion();
							processProctorAccordion();
							$("#productType").val(data.product[0].productType);
							$("#showStudentFeedback").val(data.product[0].showStudentFeedback);
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
		//var optionList = data.product;
		if(optionList.length < 1) {
			optionHtml += "<option  value='Select a product'>Show All</option>";
		} else {
			for(var i = 0; i < optionList.length; i++ ) {
				if(selectedProductId==optionList[i].productId) { 	     
					optionHtml += "<option  value='"+ optionList[i].productId+"'selected >"+ optionList[i].productName+"&nbsp;&nbsp;</option>";
					//fillDropDown("grade", optionList[i].gradeDropDownList);
					if(!(optionList[i].isTabeProduct)) {
						isTabeProduct = false;
						isTabeLocatorProduct=false;
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
						isTabeProduct = true;
						document.getElementById("gradeDiv").style.display ="none";
						document.getElementById("levelDiv").style.display ="none";
						document.getElementById("level").style.visibility ="hidden";
					}
					
				} else {
					optionHtml += "<option  value='"+ optionList[i].productId+"'>"+ optionList[i].productName+"&nbsp;&nbsp;</option>";
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
		if(AddStudentLocaldata != undefined && AddStudentLocaldata.length > 0) {
			$("#subtestChangeConfirmationPopup").dialog({  
			title:"Confirmation Alert",  
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
		 }
	}
	
	function changeProductConfirmed() {
		if(AddStudentLocaldata != undefined && AddStudentLocaldata.length > 0) {
			$("#productChangeConfirmationPopup").dialog({  
			title:"Confirmation Alert",  
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
		 } else {
		 	changeGradeAndLevel();
		 }
	}
	
	function closeSubtestConfirmPopup() {
		 closePopUp('subtestChangeConfirmationPopup');
		 subtestChangeProcess();
		 resetStudentSelection();
		 hideSelectStudent();
		 $('#list6').GridUnload();
		 populateSelectedStudent();
		 previousValue = $("#testGroupList").val();
		 selectedSubtestId = selectedTestId;
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
		$("#"+selectedSubtestId).trigger('click');
	}

	function  changeGradeAndLevel(){
		var e = document.getElementById("testGroupList");  
		var selectProductId = e.options[e.selectedIndex].value;
		var optionList = ProductData.product
		for(var i = 0; i < optionList.length; i++ ) {
			if(selectProductId==optionList[i].productId) { 	     
				$("#productType").val(optionList[i].productType);
				$("#showStudentFeedback").val(optionList[i].showStudentFeedback); 	     
				if(!(optionList[i].isTabeProduct)) {
					isTabeProduct = false;
					isTabeLocatorProduct=false;
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
					isTabeProduct = true;
					if(optionList[i].isTabeLocatorProduct){ 
					   isTabeLocatorProduct = true;
					 } else {
					    isTabeLocatorProduct = false;
					 }
					document.getElementById("gradeDiv").style.display ="none";
					document.getElementById("levelDiv").style.display ="none";
					document.getElementById("level").style.visibility ="hidden";
				}
				var tList = optionList[i].testSessionList;
				var noOfRows = tList.length;
				reloadGrids(tList,optionList[i].showLevelOrGrade );
				
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
			
			document.getElementById("aCode").value = ProductData.accessCodeList[0];
			
			var testBreak = document.getElementById("testBreak");
			if(!testBreak.checked){		
				isTestBreak = false;
				createSubtestGrid();
				//document.getElementById("aCodeHead").style.visibility = "hidden";
				document.getElementById("aCode").style.visibility = "visible";			
							
				/*for(var i=0;i<subtestLength;i++){
					document.getElementById("aCodeDiv"+i).style.visibility = "hidden";
					
				}*/
				$("#hasTestBreak").val("F"); 
			}else{
				isTestBreak = true;
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
	    //console.log(startTime);
	    //console.log(endTime);
	    $('#Schedule tbody').append('<tr>' +
	        '<td>' + startTime + '</td>' +
	        '<td>' + endTime + '</td>' +
	        '</tr>');
	}
	
	function populateTestListGrid(testSessionlist, testGroupLoad, showLevelOrGrade) {
		isTestGroupLoad  = testGroupLoad;
 		UIBlock();
 		var levelOrGradeTitle = "None";
 		var istabe = false;
 		if(isTabeProduct == undefined || isTabeProduct =='undefined') {
 			istabe = false;
 		} else {
 			istabe = isTabeProduct;
 		}
 		if (!istabe) {
	 		if(showLevelOrGrade== undefined || showLevelOrGrade=='undefined'){
	 			levelOrGradeTitle = 'None';
	 		} else if (showLevelOrGrade=='level') {
	 			levelOrGradeTitle = "Level";
	 		} else if (showLevelOrGrade=='grade') {
	 			levelOrGradeTitle = "Grade";
	 		} 
 		} 
 		//reset();
 		$("#testList").jqGrid({         
        	data: testSessionlist,
			datatype: "local",         
			colNames:['Test Name',levelOrGradeTitle, 'Subtest', 'Duration'],
		   	colModel:[
		   		{name:'testName',index:'testName', width:55, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor: pointer;' } },
		   		{name:'level',index:'level', width:18, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor: pointer;' } },
		   		{name:'subtestCount',index:'subtestCount', width:20, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor: pointer;' } },
		   		{name:'duration',index:'duration',width:22, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor: pointer;' } },
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
			sortname: 'testName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 150,
			caption:"Tests",
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
					selectedTestId = $("#testList").jqGrid('getGridParam', 'selrow');
					$('#displayMessage').hide();
					testJSONValue = getDataFromTestJson(selectedTestId, testSessionlist);
					// Added to refresh student list grid if user changes tests
					if(AddStudentLocaldata != undefined && AddStudentLocaldata.length > 0 && blockOffGradeTesting) {
						if(selectedSubtestId != selectedTestId)
		 					changeSubtestConfirmPopup();
					} else {
						subtestChangeProcess();
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
	 if(istabe || columnName=="None") {
	 	myGrid.jqGrid('hideCol', myGrid.getGridParam("colModel")[colPos].name);  
	 }
	 		
	}
	
	
	function createSubtestGrid(){
		var subtestArr = new Array();
		subtestArr = subtestDataArr;
		var subtestData = '';
		//alert(subtestGridLoaded);
		if(!subtestGridLoaded && subtestArr.length > 0){
			subtestLength = subtestArr.length;
			document.getElementById("subtestGrid").style.display = "";
			document.getElementById("noSubtest").style.display = "none";
			var tr = '';
			var th = '';
			subtestData +='<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#A6C9E2"><tr><td><table width="100%" class="ts" cellpadding="0" cellspacing="1">';
							
			th +='<tr class="subtestHeader" >';
			th +='<th width="24" height="23" align="center"><strong>#</strong></th>';
			if(isTestBreak){
				th +='<th width="289" height="23" align="left" style="padding-left:5px;"><strong>Subtest Name </strong></th>';				
				th +='<th width="130" height="23"><div align="center" id="aCodeHead"><strong>Access Code </strong></div></th>';
			}else{
				th +='<th width="419" height="23" align="left" style="padding-left:5px;"><strong>Subtest Name </strong></th>';
			}
			th +='<th width="82" height="23" align="center"><strong>Duration</strong></th>';
			if(isTabeProduct && !isTabeLocatorProduct ){
				th +='<th width="34" height="23">&nbsp;</th>';
			}
			th +='</tr>';
			subtestData += th;
			for(var i=0;i<subtestArr.length; i++){	
				tr = ''			
				tr +='<tr>';
				tr +='<td height="23" width="24" bgcolor="#FFFFFF">';
				tr +='<div align="center" id="num'+i+'">'+parseInt(i+1)+'<input type="hidden" id="actionTaken'+i+'" value="1"/></div>';
				tr +='</td>';
				if(isTestBreak){
					tr +='<td height="23" width="289" bgcolor="#FFFFFF" style="padding-left:5px;">';
					tr +='<div align="left" id="sName'+i+'">'+subtestArr[i].subtestName+'</div>';
					tr +='</td>';
					tr +='<td height="23" width="130" align="center" bgcolor="#FFFFFF">';
					tr +='<div align="center" id="aCodeDiv'+i+'">';
					tr +='<input name="aCodeB'+i+'" type="text" size="13" id="aCodeB'+i+'" value="'+ProductData.accessCodeList[i]+'" onblur="javascript:trimTextValue(this); return false;" style="padding-left:2px;" maxlength="32" /></div>';
					tr +='</td>';
				}else{
					tr +='<td height="23" width="419" bgcolor="#FFFFFF" style="padding-left:5px;">';
					tr +='<div align="left" id="sName'+i+'">'+subtestArr[i].subtestName+'</div>';
					tr +='</td>';
				}
				tr +='<td height="23" width="82" align="center" bgcolor="#FFFFFF">';
				tr +='<div align="center" id="duration'+i+'">'+subtestArr[i].duration+'</div>';
				tr +='</td>';
				if(isTabeProduct && !isTabeLocatorProduct){
					tr +='<td height="23" align="center" width="34" bgcolor="#FFFFFF">';
					tr +='<div align="center">';
					tr +='<img id="imgMin'+i+'" src="/SessionWeb/resources/images/minus.gif" width="14" title="Remove Subtest" onclick="javascript:removeSubtestOption(0,'+i+');" />';
					tr +='<img id="imgPlus'+i+'" src="/SessionWeb/resources/images/icone_plus.gif" width="14" title="Add Subtest" onclick="javascript:removeSubtestOption(1,'+i+');" style="display: none;" />';
					tr +='</div>';
					tr +='</td>';
				}
				tr +='</tr>';				
				subtestData += tr;		
			}
			subtestData +='</table></td></tr></table>';
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
		
		document.getElementById("subtestGrid").style.display = "none";
		document.getElementById("noSubtest").style.display = "";
		document.getElementById("testSessionName_lbl").innerHTML = "No test selected";
		document.getElementById("testSessionName").value = "";
		document.getElementById("startDate").value = "";			
		document.getElementById("endDate").value = "";			
		document.getElementById("time").innerHTML = "8:00 AM - 5:00 PM";
		document.getElementById("testLocation").value = "";											
		$("#randomDis").hide();	
		$("#randDisLbl").hide();		
		$("#randomDis").val("");		
		//$("#randomDistDiv").hide();								
					
	}
	
	function resetPopup() { 
		$("#sessionListDiv").hide();
		$('#testList').GridUnload();
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
		document.getElementById("testSessionName_lbl").innerHTML = "No test selected";
		document.getElementById("testSessionName").value = "";
		document.getElementById("startDate").value = "";			
		document.getElementById("endDate").value = "";			
		document.getElementById("time").innerHTML = "8:00 AM - 5:00 PM";
		document.getElementById("testLocation").value = "";											
		$("#randomDis").hide();	
		$("#randDisLbl").hide();
		$("#randomDis").val("");
		//$("#randomDistDiv").hide();
 		
 		
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
					setMessage("Missing required field", requiredFields, "errorMessage", REQUIRED_TEXT);       
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
		
			if(testSessionName == "" || startDate == "" || endDate == "" || !validateDate(startDate,endDate) || !validNameString(testLocation)){				
				return false;
			}

			var invalidCharFields = verifyTestInfo(testSessionName,"","");
				var invalidString = "";                        
				if (invalidCharFields.length > 0) {
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
			 	setMessage(INVALID_DATES_MSG, "", "errorMessage", "");       
		}	
			
		var invalidCharFields = verifyTestInfo(testSessionName, "", testLocation);
 		var invalidString = "";                        
    	if (invalidCharFields.length > 0) {
    		setMessage(invalid_char_message, invalidCharFields, "errorMessage",INVALID_NAME_CHARS);
		}
			
		if (requiredFieldCount > 0) {
			if (requiredFieldCount == 1) {
				setMessage("Missing required field", requiredFields, "errorMessage", REQUIRED_TEXT);
			}
			else {
				setMessage("Missing required fields", requiredFields, "errorMessage", REQUIRED_TEXT_MULTIPLE);
			}
			//return false;
		}
	//return true;
	 }
	 
	function verifyTestInfo(testSessionName, accessCode, testLocation)
    {
        var invalidCharFields = "";
        var invalidCharFieldCount = 0;

        if (testSessionName != "" && !validNameString(testSessionName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Test Session Name", invalidCharFieldCount, invalidCharFields);       
        }
        
        if (accessCode != "" && !validNameString(accessCode) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Access Code", invalidCharFieldCount, invalidCharFields);       
        }
        
        if (testLocation != "" && !validNameString(testLocation) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Test Location", invalidCharFieldCount, invalidCharFields);       
        }
            
        return invalidCharFields;
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
						setMessage(TAC_MISSING_TEST_ACCESSCODE_HEADER, TAC_MISSING_TEST_ACCESSCODE, "errorMessage","");
					} else if(!validateAccessCodeMinLength( false) ) {
						setMessage(TAC_SIXCHARS, "", "errorMessage","");
					} else if(!validateAccessCodeSpecialChar(false)) {
						setMessage(TAC_SPECIAL_CHAR_NOTALLOWED_HEADER, TAC_SPECIAL_CHAR_NOTALLOWED, "errorMessage","");
					}
				}else{
					if(!validateMissingTestAccessCodes()) {
						setMessage(TAC_MISSING_TEST_ACCESSCODES_HEADER, TAC_MISSING_TEST_ACCESSCODES1, "errorMessage",TAC_MISSING_TEST_ACCESSCODES2);
					} else if(!validateAccessCodeMinLength( true) ) { 
						setMessage(TAC_SIXCHARS, "", "errorMessage","");
					} else if(!validateAccessCodeSpecialChar(true)) {
						setMessage(TAC_SPECIAL_CHAR_NOTALLOWED_HEADER, TAC_SPECIAL_CHAR_NOTALLOWED, "errorMessage","");
					}else if(!validateIdenticalTestAccessCodes()) {
						setMessage(TAC_IDENTICAL_TESTACCESSCODES_HEADER , TAC_IDENTICAL_TEST_ACCESSCODES1, "errorMessage", TAC_IDENTICAL_TEST_ACCESSCODES2);
					}
					
				}
			} else {
				
				if(!validateMissingTestAccessCode()) {
					setMessage(TAC_MISSING_TEST_ACCESSCODE_HEADER, TAC_MISSING_TEST_ACCESSCODE, "errorMessage","");
				} else if(!validateAccessCodeMinLength( false) ) {
					setMessage(TAC_SIXCHARS, "", "errorMessage","");
				} else if(!validateAccessCodeSpecialChar(false)) {
					setMessage(TAC_SPECIAL_CHAR_NOTALLOWED_HEADER, TAC_SPECIAL_CHAR_NOTALLOWED, "errorMessage","");
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
		var selectProductId = ep.options[ep.selectedIndex].value;
		var optionList = ProductData.product
		
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
	
	function populateDates(){
		document.getElementById("startDate").value = currDate;
		document.getElementById("endDate").value = nextDate;
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
			accessCodes[i]= document.getElementById("aCodeB"+i).value;
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
	      
	      jQuery("#selectProctor").jqGrid('setGridParam',{datatype:'json'});    
	      var urlVal = 'getProctorList.do?q=2&proctorOrgNodeId='+$("#proctorOrgNodeId").val(); 
     	  jQuery("#selectProctor").jqGrid('setGridParam', {url:urlVal ,page:1}).trigger("reloadGrid");
          var sortArrowPA = jQuery("#selectProctor");
          jQuery("#selectProctor").sortGrid('lastName',false);
          var arrowElementsPA = sortArrowPA[0].grid.headers[0].el.lastChild.lastChild;
          $(arrowElementsPA.childNodes[0]).removeClass('ui-state-disabled');
          $(arrowElementsPA.childNodes[1]).addClass('ui-state-disabled');

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

 		if(noOfProctorAdded == 0) {
	 		var jsondata = {};
	 		jsondata['userId'] = schedulerUserId;
	 		jsondata['lastName'] = schedulerLastName;
	 		jsondata['firstName'] = schedulerFirstName;
	 		jsondata['defaultScheduler'] = "T";
	 		jsondata['userName'] = schedulerUserName;
	 		
	 		var val=[] ;
		 	val.push(jsondata);
		    
		    addProctorLocaldata = val;
		 	
			proctorIdObjArray = {};
			proctorIdObjArray[schedulerUserId] = jsondata;
			//alert(JSON.stringify(proctorIdObjArray));	
		 	
	 	}
	 	
	 	noOfProctorAdded = addProctorLocaldata.length;
		$("#totalAssignedProctors").text(noOfProctorAdded);
	 	
	 	//$("#testSchedulerId").text(schedulerFirstName + ' ' + schedulerLastName);
	 	 		
 		$("#listProctor").jqGrid({         
         data:  addProctorLocaldata,
		 datatype: "local",         
          colNames:[ 'Last Name','First Name','Default Scheduler','User Id'],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:130, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:130, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'defaultScheduler',index:'defaultScheduler', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userId',index:'userId', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
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
			height: 151,  
			caption:"Proctor List",
			onPaging: function() {
				var reqestedPage = parseInt($('#listProctor').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_listProctor').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#listProctor').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#listProctor').setGridParam({"page": minPageSize});
				}
			
			},
			gridComplete: function() {
					var allRowsInGrid = $('#listProctor').jqGrid('getDataIDs');
					var selectedRowData;
					for(var i = 0; i < allRowsInGrid.length; i++) {
					
						selectedRowData = $("#listProctor").getRowData(allRowsInGrid[i]);
						if (selectedRowData.defaultScheduler == 'T') {
				 			$("#"+allRowsInGrid[i]+" td input").attr("disabled", true);
				 			$("#"+allRowsInGrid[i]).addClass('ui-state-disabled');
				 		//	$("#listProctor").jqGrid('editRow',allRowsInGrid[i],false);
				 		}
					
					}
			},
			
			onSelectAll: function (rowids, status) {

				if(status) {
					var tmpselectedRowId = "";
					selectAllForDeleteProctor = true;
					var allRowsInGrid = $('#listProctor').jqGrid('getRowData');
						var selectedRowData;
						for(var i = 0; i < addProctorLocaldata.length; i++) {
							
							selectedRowData = addProctorLocaldata[i];
							if (selectedRowData.defaultScheduler == 'F') {
							
								selectedRowData = addProctorLocaldata[i];
								$("#"+selectedRowData.userId+" td input").attr("checked", true); 
								
								tmpselectedRowId = selectedRowData.userId;
					 			delProctorIdObjArray[selectedRowData.userId]=selectedRowData;
					 			//pdindex++;
								/*if (deletedProctorIds == "") {
										deletedProctorIds = allRowsInGrid[i]+"_"+pdindex;
										pdindex++;
								} else {
										deletedProctorIds = deletedProctorIds +"|"+allRowsInGrid[i]+"_"+pdindex;
										pdindex++;
								}*/
					 		}
						
						}
				} else {
				
					var tmpselectedRowId = "";
					selectAllForDeleteProctor = false;
					var allRowsInGrid = $('#listProctor').jqGrid('getDataIDs');
						var selectedRowData;
						for(var i = 0; i < addProctorLocaldata.length; i++) {
							
							selectedRowData = addProctorLocaldata[i];
							if (selectedRowData.defaultScheduler == 'F') {
							
								selectedRowData = addProctorLocaldata[i];
								$("#"+selectedRowData.userId+" td input").attr("checked", false); 
								
								tmpselectedRowId = selectedRowData.userId;
								delete delProctorIdObjArray[selectedRowData.userId];
					 		}
						}
				
					//selectAllForDeleteProctor = false;
					//var indx = getProctorIDIndex(selectedRowId);
					//delProctorIdObjArray[indx]=null;
					
					//deletedProctorIds = updateRule(deletedProctorIds,indx);
					
				} 
				
			}, 
			onSelectRow: function (rowid, status) {
				var tmpselectedRowId = "";
				selectAllForDeleteProctor = false;
				var selectedRowData = $("#listProctor").getRowData(rowid);
				tmpselectedRowId = selectedRowData.userId;
								
				if(status) {					
					delProctorIdObjArray[tmpselectedRowId]=selectedRowData;					
				} else {				
					delete delProctorIdObjArray[tmpselectedRowId];
					
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
		    	
		    	jQuery("#listProctor").setGridWidth(width,true);
		    	
		    	$("#testSchedulerId").text(schedulerFirstName + ' ' + schedulerLastName);
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#listProctor").navGrid('#pagerProctor', {
			delfunc: function() {
				if(!isEmpty(delProctorIdObjArray)) {
					removeProctorConfirmationPopup();
				}
		    }	    	
	 });
		var element = document.getElementById('add_listProctor');
		element.style.display = 'none'; 
		var element = document.getElementById('edit_listProctor');
		element.style.display = 'none'; 
		var element = document.getElementById('search_listProctor');
		element.style.display = 'none';  
		var element = document.getElementById('del_listProctor');
		element.title = 'Remove Proctor'; 
	}
	
	function removeSelectedProctor() {
		for(var i in delProctorIdObjArray) {		
			delete proctorIdObjArray[i];
			delete tempProctorData[i];
			for(var count=0;count<addProctorLocaldata.length;count++){
				if(i == addProctorLocaldata[count].userId){
					addProctorLocaldata.splice(count,1);
				}
			}
		}
		for(var j in allSelectOrgProctor){
			allSelectOrgProctor[j] = false;
			if(tempAllSelectOrgProctor[j]){
				tempAllSelectOrgProctor[j] = false;
			}
		}
		
		noOfProctorAdded = addProctorLocaldata.length;
		//$('#totalAssignedProctors').text(noOfProctorAdded);
		closePopUpForProctor('removeProctorConfirmationPopup');
		delProctorIdObjArray = {};
		gridReloadProctor(false);
	}
	
	function removeProctorConfirmationPopup(){
	$("#removeProctorConfirmationPopup").dialog({  
		title:"Confirmation Alert",  
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
		title:"Confirmation Alert",  
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
    	if( sessionName!= null && $.trim(sessionName).length == 0 ){
    		closePopUp('scheduleSession');
    	} else {
    		openCloseScheduleSessionPopup();
    	}
    }
    
    function saveTest() {
    $('#displayMessage').hide();
    $('#showSaveTestMessage').hide();
	var param;
	var param1 =$("#testDiv *").serialize(); 
    var param2 = $("#Test_Detail *").serialize();
    var time = document.getElementById("time").innerHTML;
    var timeArray = time.split("-");
    param = param1+"&"+param2+"&startTime="+$.trim(timeArray[0])+"&endTime="+$.trim(timeArray[1]);
    var selectedstudent = getStudentListArray(AddStudentLocaldata);
    param = param+"&students="+selectedstudent.toString();
    
    var selectedProctors =getProctorListArray(addProctorLocaldata);
    param = param+"&proctors="+selectedProctors.toString();
    
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'saveTest.do?'+param,
		type:		'POST',
		data:		 param,
		dataType:	'json',
		contentType: 'application/json; charset=UTF-8', 
		success:	function(data, textStatus, XMLHttpRequest){
					   
					   
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
					  } else {
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
				  } else {
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
			$("#saveTestTitle").html(title);
			$("#saveTestContent").html(content);
			$("#saveTestMessage").html(message);
			if(type=="errorMessage"){
				$('#errorIcon').show();
			} else {
				$('#errorIcon').hide();
			}
	}
	
	
	function trimTextValue(element){
	       element.value=$.trim(element.value);
	}

	function  resetOnSelectTestSessionData() {
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
	
	function closePopUpForProctor(dailogId){
 		if(dailogId == 'removeProctorConfirmationPopup') {
			$("#"+dailogId).dialog("close");
		}
		
	}

	function resetStudentSelection() {
		studentMap = new Map();
		studentIndexMap = new Map();
		studentTempMap = new Map();
		studentTempIndexMap = new Map();
		studentIndexCount = 0;
		studentTempIndexCount = 0;
		allStudentIds = [];
		allSelectOrg = [];
		countAllSelect = 0;
		AddStudentLocaldata = [];
		studentWithaccommodation = 0;
		orgBeforeBack = [];
		orgBeforeBackCount = 0;
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
		subtestDataArr = testJSONValue;
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
    
    
					 