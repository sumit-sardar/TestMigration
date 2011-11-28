var REQUIRED_TEXT = "Please enter/select this value to continue.";
var REQUIRED_TEXT_MULTIPLE = "Please enter/select these values to continue.";
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
	 }else if (requestedTab == 'TSL'){
	 	 if(isTestGridEmpty) {
		 	$('#testList').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
		 	$('#testList').append("<tr><td style='width: 100%;' align='center'  colspan='6'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/SessionWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noTestMsg").val()+"</th></tr></tbody></table></td></tr>");
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
			var found = false;
			for(var i=0; i<sessionList.length ;i++){
				if(sessionList[i].id == id){
				    found = true;
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
						document.getElementById("randomDis").checked = true;
					}else if(sessionList[i].isRandomize == "N" ){
						$("#randomDis").show();	
						$("#randDisLbl").show();	
						document.getElementById("randomDis").checked = false;
					} else {
						document.getElementById("randomDis").checked = false;
						$("#randomDis").hide();	
						$("#randDisLbl").hide();	
					}
					break;					
				}
			}
			if(!found) {
				$("#randomDis").hide();	
				$("#randDisLbl").hide();
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
		   		{name:'lastName',index:'lastName', width:130, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:130, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:120, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentId',index:'studentId', width:275, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations', width:165, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
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
						fillDropDownWithDefaultValue("timeZoneList",data.testZoneDropDownList, data.userTimeZone);
						reloadGrids(data.product[0].testSessionList,data.product[0].showLevelOrGrade);
						//populateTestListGrid(data.product[0].testSessionList,true,data.product[0].showLevelOrGrade);
						fillDropDown("topOrgNode",data.topNodeDropDownList)
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
						isTabeLocatorProduct=false;
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
					    if(optionList[i].isTabeLocatorProduct){ 
					    	isTabeLocatorProduct = true;
					    } else {
					    	isTabeLocatorProduct = false;
					    }
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
					optionHtml += "<option  value='"+ optionList[i].id+"' selected>"+ optionList[i].name+"</option>";	
				} else {
					optionHtml += "<option  value='"+ optionList[i].id+"'>"+ optionList[i].name+"</option>";	
				}		     
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
				reloadGrids(tList,optionList[i].showLevelOrGrade );
				
				break;
			} 
			isTestSelected = false;		
		}
		processStudentAccordion();

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
			createSubtestGrid();
			document.getElementById("aCode").value = ProductData.accessCodeList[0];
			
			var testBreak = document.getElementById("testBreak");
			if(!testBreak.checked){		
				isTestBreak = false;
				document.getElementById("aCodeHead").style.visibility = "hidden";
				document.getElementById("aCode").style.visibility = "visible";			
							
				for(var i=0;i<subtestLength;i++){
					document.getElementById("aCodeDiv"+i).style.visibility = "hidden";
					
				}
			}else{
				isTestBreak = true;
				document.getElementById("aCodeHead").style.visibility = "visible";
				document.getElementById("aCode").style.visibility = "hidden";
				for(var i=0;i<subtestLength;i++){
					document.getElementById("aCodeDiv"+i).style.visibility = "visible";
					if(document.getElementById("actionTaken"+i).value == "1"){
						document.getElementById("aCodeB"+i).removeAttribute("disabled");
						document.getElementById("aCodeB"+i).className = "";
					}else{
						document.getElementById("aCodeB"+i).setAttribute("disabled",true);
						document.getElementById("aCodeB"+i).className = "textboxDisabled";
					}
				}
			}
		}
	}
	
	function stopSlide(event, ui) {
	 	var minutes0Stop = parseInt($("#slider-range").slider("values", 0) % 60, 10);
		var hours0Stop = parseInt($("#slider-range").slider("values", 0) / 60 % 24, 10);
		var minutes1Stop = parseInt($("#slider-range").slider("values", 1) % 60, 10);
		var hours1Stop = parseInt($("#slider-range").slider("values", 1) / 60 % 24, 10);
		if((minutes0Stop == 0 && hours0Stop == 0 && !(minutes0 == 0 && hours0 == 0)) || (hours1Stop == 23 && minutes1Stop == 45 && !(hours1 == 23 && minutes1 == 45))) {
			startTime = getTime(hours0Stop, minutes0Stop);
			endTime = getTime(hours1Stop, minutes1Stop);
			$("#time").text(startTime + ' - ' + endTime);
		}
	
	}

	function slideTime(event, ui){
	    minutes0 = parseInt($("#slider-range").slider("values", 0) % 60, 10);
	    hours0 = parseInt($("#slider-range").slider("values", 0) / 60 % 24, 10);
	    minutes1 = parseInt($("#slider-range").slider("values", 1) % 60, 10);
	    hours1 = parseInt($("#slider-range").slider("values", 1) / 60 % 24, 10);
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
		   		{name:'testName',index:'testName', width:55, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'level',index:'level', width:12, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'subtestCount',index:'subtestCount', width:15, editable: false, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'duration',index:'duration',editable: false, width:25, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
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
					subtestGridLoaded = false;
					var selectedTestId = $("#testList").jqGrid('getGridParam', 'selrow');
					$('#displayMessage').hide();
					var testBreak = document.getElementById("testBreak");
					isTestSelected = true;
					if(testBreak.checked)testBreak.checked = false;
					//document.getElementById("aCode").style.display = "none";
					populateDates();
					var val = getDataFromTestJson(selectedTestId, testSessionlist);
					subtestDataArr = val;
					createSubtestGrid();
			 		
			},
			loadComplete: function () {
				if ($('#testList').getGridParam('records') === 0) {
				 	isTestGridEmpty = true;
				 	$('#sp_1_testPager').text("1");
            	} else {
            		isTestGridEmpty = false;
            	}
            	
			
            	var width = jQuery("#Select_Test").width();
			    width = width/2 - 20; // Fudge factor to prevent horizontal scrollbars
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
				window.location.href="/TestSessionInfoWeb/logout.do";
						
			}
	 });
	 jQuery("#testList").jqGrid('navGrid', '#testPager', { edit: false, add: false, del: false, search: false, refresh: false });
	 
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
			subtestData +='<table width="100%" cellpadding="3" cellspacing="0" class="shadowBorder">';
							
			th +='<tr class="subtestHeader" >';
			th +='<th width="24" height="23" align="center"><strong>#</strong></th>';
			th +='<th width="289" height="23" align="left"><strong>Subtest Name </strong></th>';
			th +='<th width="130" height="23"><div align="center" id="aCodeHead" style="visibility:hidden;"><strong>Access Code </strong></div></th>';
			th +='<th width="82" height="23" align="center"><strong>Duration</strong></th>';
			if(isTabeProduct){
				th +='<th width="34" height="23">&nbsp;</th>';
			}
			th +='</tr>';
			subtestData += th;
			for(var i=0;i<subtestArr.length; i++){	
				tr = ''			
				tr +='<tr class="subtestCols">';
				tr +='<td height="23" width="24" class="subtestCols">';
				tr +='<div align="center" id="num'+i+'">'+parseInt(i+1)+'<input type="hidden" id="actionTaken'+i+'" value="1"/></div>';
				tr +='</td>';
				tr +='<td height="23" width="289" class="subtestCols">';
				tr +='<div align="left" id="sName'+i+'">'+subtestArr[i].subtestName+'</div>';
				tr +='</td>';
				tr +='<td height="23" width="130" align="center" class="subtestCols">';
				tr +='<div align="center" id="aCodeDiv'+i+'" style="visibility:hidden;">';
				tr +='<input name="aCodeB'+i+'" type="text" size="13" id="aCodeB'+i+'" value="'+ProductData.accessCodeList[i]+'" style="padding-left:2px;"/></div>';
				tr +='</td>';
				tr +='<td height="23" width="82" align="center" class="subtestCols">';
				tr +='<div align="center" id="duration'+i+'">'+subtestArr[i].duration+'</div>';
				tr +='</td>';
				if(isTabeProduct){
					tr +='<td height="23" align="center" width="34" class="subtestCols">';
					tr +='<div align="center">';
					tr +='<img id="imgMin'+i+'" src="/SessionWeb/resources/images/minus.gif" width="14" title="Remove Subtest" onclick="javascript:removeSubtestOption(0,'+i+');" />';
					tr +='<img id="imgPlus'+i+'" src="/SessionWeb/resources/images/icone_plus.gif" width="14" title="Add Subtest" onclick="javascript:removeSubtestOption(1,'+i+');" style="display: none;" />';
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
	
	function removeSubtestOption(action,rowId){
		if(action == '0'){
			document.getElementById("imgPlus"+rowId).style.display = "inline";
			document.getElementById("imgMin"+rowId).style.display = "none";
			document.getElementById("num"+rowId).className = "lblDisabled";
			document.getElementById("sName"+rowId).className = "lblDisabled";
			document.getElementById("duration"+rowId).className = "lblDisabled";
			if(document.getElementById("aCodeDiv"+rowId).style.display == "inline"){
				document.getElementById("aCodeB"+rowId).setAttribute("disabled",true);
				document.getElementById("aCodeB"+rowId).className = "textboxDisabled";
			}
			document.getElementById("actionTaken"+rowId).value = "0";
			
		}else{
			document.getElementById("imgPlus"+rowId).style.display = "none";
			document.getElementById("imgMin"+rowId).style.display = "inline";
			document.getElementById("num"+rowId).className = "";
			document.getElementById("sName"+rowId).className = "";
			document.getElementById("duration"+rowId).className = "";
			if(document.getElementById("aCodeDiv"+rowId).style.display == "inline"){
				document.getElementById("aCodeB"+rowId).removeAttribute("disabled");
				document.getElementById("aCodeB"+rowId).className = "";
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
		
		subtestLength = 0;
		var testBreak = document.getElementById("testBreak");
		isTestSelected = false;
		if(testBreak.checked) testBreak.checked = false;
		document.getElementById("aCode").style.visibility = "hidden";
		
		document.getElementById("subtestGrid").style.display = "none";
		document.getElementById("noSubtest").style.display = "";
		document.getElementById("testSessionName_lbl").innerHTML = "No Test is selected";
		document.getElementById("testSessionName").value = "";
		document.getElementById("startDate").value = "";			
		document.getElementById("endDate").value = "";			
		document.getElementById("time").innerHTML = "9:00 AM - 5:00 PM";
		document.getElementById("testLocation").value = "";	
		$("#randomDis").hide();	
		$("#randDisLbl").hide();										
					
	}
	
	function resetPopup() { 
		$('#ssAccordion').accordion('activate', 0 );
		$("#Select_Test").scrollTop(0);
		$("#Test_Detail").scrollTop(0);
		$("#Add_Student").scrollTop(0);
		$("#Add_Proctor").scrollTop(0);
		
		document.getElementById('ShowButtons').style.display = "block";
 		setAnchorButtonState('viewStatusButton', true);
 		
 		if($("#canRegisterStudent").val() == 'true'){
 			setAnchorButtonState('registerStudentButton', true);
 		}
 		
 		
	}
	
	function validateTest(){
		var acc   = $("#ssAccordion");
		var testSessionName = trim(document.getElementById("testSessionName").value);
		var startDate = trim(document.getElementById("startDate").value);
		var endDate = trim(document.getElementById("endDate").value);	
		
		if(!isTestSelected){
			return false;
		}else{
			if(testSessionName == "" || startDate == "" || endDate == ""){				
				return false;
			}
			if(subtestLength > 0){
				var testBreak = document.getElementById("testBreak");
				if(!testBreak.checked){	
					var accessCode = trim(document.getElementById("aCode").value);	
					if(accessCode == "") return false;	
				}else{
					for(var i=0;i<subtestLength;i++){
						if(trim(document.getElementById("aCodeB"+i).value) == ""){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	 function verifyTestDetails(){
	 	var requiredFields = "";
     	var requiredFieldCount = 0; 
     	var testSessionName = trim(document.getElementById("testSessionName").value);
		var startDate = trim(document.getElementById("startDate").value);
		var endDate = trim(document.getElementById("endDate").value);
		var testBreakVal = 0;
		
		if(!isTestSelected){
			requiredFields = "";
			if ( testSessionName.length == 0 ) {
					requiredFieldCount += 1;            
					requiredFields = buildErrorString("Test Session Name", requiredFieldCount, requiredFields);       
				}
		}else{
			requiredFields = "";
			if(subtestLength > 0){
				var testBreak = document.getElementById("testBreak");
				if(!testBreak.checked){	
					var accessCode = trim(document.getElementById("aCode").value);	
					if(accessCode.length == 0){
						requiredFieldCount += 1;            
						requiredFields = buildErrorString("Access Code", requiredFieldCount, requiredFields);
					}	
				}else{
					for(var i=0;i<subtestLength;i++){
						if(trim(document.getElementById("aCodeB"+i).value) == ""){
							testBreakVal += 1;
						}
					}
					if(testBreakVal > 0){
						requiredFieldCount += 1;            
						requiredFields = buildErrorString("Access Code", requiredFieldCount, requiredFields);
					}
				}
			}
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
					 