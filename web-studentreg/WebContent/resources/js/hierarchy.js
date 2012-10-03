
var leafNodeCategoryId;
var orgTreeHierarchy;
var jsonData;
var assignedOrgNodeIds = "";
var SelectedOrgNodeId;
var selectedOrgNodeIdInPopup;
var selectedOrgNodeIdInSecondaryDiv;
var SelectedOrgNode;
//var SelectedOrgNodes = [];
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
var statusOptions = ":Any;Current:Current;Future:Future";
var currentView = "session";
var selectedRData = {};
var stuItemGridLoaded = false;
var itemgridLoaded = false;
var scoreByStdGridLoaded = false;
var sbsItemGridLoaded = false;
var sbiStudentGridLoaded = false;
var isScoreByItemClicked = false;
var data1 = null;
var gridloadedStdFromSes = false;
var gridloadedStdFromSesInSecondaryDiv = false;
var gridloadedSessionFromStd = false;
var gridloadedSessionFromStdInSecondaryDiv = false;
var registerBySessionGridFromStudent = false; 
var requetForStudent  = "";
var studentGradeOptions = []; 
var studentGenderOptions = []; 
var dayOptions = []; 
var yearOptions = []; 
var monthOptions = []; 
var testPurposeOptions = [];
var isAddStudent = false; 
var isValueChanged = false;
var customerDemographicValue = "";
var profileEditable = "true";
var leafNodePathMap = {};	
var leafNodeTextMap = {};
var isAction = true;
var isAddStudPopup = false;
var organizationNodes = [];

var isTabeProduct = false;
var isTabeAdaptiveProduct = false;
var isTabeProductFinal = false;
var isTabeAdaptiveProductFinal = false;
var selectedItemSetIdTC = null;
var selectedItemSetIdTCFinal = null;
var selectedTestAdminId = null;
var selectedTestAdminIdFinal = null;
var selectedTestAdminName;
var selectedTestAdminNameFinal;

var accomodationMap = {};
function populateStudentRegTree() {
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'getOrgNodeHierarchyList.do',
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
						$("#stdRegOrgNode").css("visibility","visible");	
						$("#stdFromSessionOrgSearchHeader").css("visibility","visible");						
						$("#secondaryOrgSearchHeader").css("visibility","visible");
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
	   $("#stdRegOrgNode").jstree({
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

		$("#stdRegOrgNode").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#stdRegOrgNode ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
		
		$("#stdRegOrgNode").delegate("a","click", function(e) {
  			SelectedOrgNodeId = $(this).parent().attr("id");
  			$("#displayMessageMain").hide();
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
		
		registerDelegate("stdRegOrgNode");

}

function populateGrid(){
	UIBlock();
	resetFilters();
	if(currentView=="student") {
		if(!gridloadedStu) {
	  		gridloadedStu = true;
	  		populateTreeSelect(); 
	  		populateStudentTreeSelect();
	  		populateTreeSelectSecondary(); 
			populateReportingStudentGrid();
		} else {
			var grid = $("#studentRegistrationGrid"); 
			$("#searchStudentByKeywordInput").val('');
			if(grid[0] != undefined && grid[0].p != undefined) {
			 grid[0].p.search = false;
			 }
			gridScoringStudentReload();
		}
		if(gridloadedStu) {
			document.getElementById('registerButton').style.visibility = "visible";
			setAnchorButtonState('registerButton', true);
		}
	} else {
		if(!gridloadedSes) {
	  		gridloadedSes = true;
	  		populateTreeSelect();
	  		populateTreeSelectSecondary(); 
			populateRegistrationSessionGrid();
		} else {
			var grid = $("#sessionRegistrationGrid"); 
			$("#searchSessionByKeywordInput").val('');
			if(grid[0] != undefined && grid[0].p != undefined) {
			 grid[0].p.search = false;
			 }
			reloadRegistrationSessionGrid();
		}
		if(gridloadedSes) {
			document.getElementById('registerButton').style.visibility = "visible";
			setAnchorButtonState('registerButton', true);
		}
	}
}

	/*
	rapid registration start
	*/
	function populateStudentTreeSelect() {
			$("#notSelectedOrgNode").css("display","inline");
			$("#selectedOrgNode").text("");	
			$("#orgInnerID").undelegate();
			$("#orgInnerID").unbind();
			createMultiNodeSelectedTreeForStudent(orgTreeHierarchy);
		}
	/*rapid registration end
	*/

	function populateDropDowns() {
	
	    var postDataObject = {};

	    $.ajax({
	        async: true,
	        beforeSend: function () {
	            UIBlock();
	        },
	        url: 'populateGridDropDowns.do',
	        type: 'POST',
	        data: postDataObject,
	        dataType: 'json',
	        success: function (data, textStatus, XMLHttpRequest) {
	            $.unblockUI();
	            if (data != undefined) {
	                if (data.gradeOptions != undefined) {
	                    var gOptionsArr = data.gradeOptions;
	                    var gOptions = ":Any;";
	                    for (var i = 0; i < gOptionsArr.length; i++) {
	                        gOptions = gOptions + gOptionsArr[i] + ":" + gOptionsArr[i] + ";";
	                    }
	                    if (gOptions != "") {
	                        gradeOptions = gOptions.substring(0, gOptions.length - 1);
	                    }
	                }
	                if (data.testCatalogOptions != undefined) {
	                    var testOptionsArr = data.testCatalogOptions;
	                    var testOptions = ":Any;";
	                    for (var i = 0; i < testOptionsArr.length; i++) {
	                        testOptions = testOptions + testOptionsArr[i] + ":" + testOptionsArr[i] + ";";
	                    }
	                    if (testOptions != "") {
	                        testNameOptions = testOptions.substring(0, testOptions.length - 1);
	                    }
	                }
	            }
	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            $.unblockUI();
	            window.location.href = "/SessionWeb/logout.do";
	
	        }
	    });
	}


function viewBySession() {
	UIBlock();
	currentView = "session";
	$("#studentView").hide();
	resetFilters();
	populateGrid();
	document.getElementById("showBySession").options[0].selected = true;
	$("#sessionView").show();
	setAnchorButtonState('registerButton', true);
}

function viewByStudent() {
	UIBlock();
	currentView = "student";
	$("#sessionView").hide();
	resetFilters();
	populateGrid();
	document.getElementById("showByStudent").options[0].selected = true;
	$("#studentView").show();
	setAnchorButtonState('registerButton', true);
}

function resetFilters() {
	$("#gs_grade option:eq(0)").attr('selected','Any'); 
	$("#gs_gender option:eq(0)").attr('selected','Any'); 
	$("#gs_testCatalogName option:eq(0)").attr('selected','Any'); 
	$("#gs_testName option:eq(0)").attr('selected','Any'); 
	$("#gs_AssignedRole option:eq(0)").attr('selected','Any'); 
	$("#gs_testAdminStatus option:eq(0)").attr('selected','Any'); 
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
// Confirmation Popup For Top Node selection
function openConfirmationPopup(){
	if(isAddStudent) {
		$("#studentConfirmationPopup").dialog({  
		title:$("#confirmAlrt").val(),  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '400px',
	 	modal: true,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 $("#studentConfirmationPopup").css('height',120);
		 var toppos = ($(window).height() - 290) /2 + 'px';
		 var leftpos = ($(window).width() - 410) /2 + 'px';
		 $("#studentConfirmationPopup").parent().css("top",toppos);
		 $("#studentConfirmationPopup").parent().css("left",leftpos);
	}
	else {
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
	}
		
function fetchDataOnConfirmation() {
		if(isAddStudent){
			closePopUp('studentConfirmationPopup');
			closePopUp('studentAddEditDetail');
		} else {
		closePopUp('confirmationPopup');
		populateGrid();
		}
}

function hidePopup(dailogId){
    if(dailogId !=null && dailogId!=undefined && $("#"+dailogId).length ==1 )
		$("#"+dailogId).parent().hide();

}

function displayPopup(dailogId){
    if(dailogId !=null && dailogId!=undefined && $("#"+dailogId).length ==1 )
		$("#"+dailogId).parent().show();
}

function closePopUp(dailogId){
	$('.ui-widget-overlay').css('height', '100%');
	if(dailogId == 'sessionStudRegId') {
		isScoreByItemClicked = false;
		$('#list2').GridUnload();//to destroy the grid instance
		gridloadedStdFromSes = false;
		gridloadedSessionFromStd = false;
		$('#innerOrgNodeHierarchyForStd').jstree('close_all', -1);//to reset the popup tree
		isPopupOnFRAccept = false;
		isPopupOnFRNotAccept = false;
		isPopupOnByepassFR = false;
		if(currentView == "student"){
			$("#studentRegistrationGrid").jqGrid('resetSelection');
		}else {
			$("#sessionRegistrationGrid").jqGrid('resetSelection');
		}
		$('#primaryJQGridDiv').show();
		$('#secondaryJQGridDiv').hide()
		$('#list3').GridUnload();//to destroy the grid instance
		$('#secondaryInnerOrgNodeHierarchy').jstree('close_all', -1);
		gridloadedStdFromSesInSecondaryDiv = false;
		gridloadedSessionFromStdInSecondaryDiv = false;
		setAnchorButtonState('registerButton', true);
	}
	if(dailogId=='sessionStudRegId' || dailogId==''){
		isPopUp = false;
		$('#list2').GridUnload();//to destroy the grid instance
		gridloadedStdFromSes = false;
		gridloadedSessionFromStd = false;
		$('#innerOrgNodeHierarchyForStd').jstree('close_all', -1);//to reset the popup tree
	}

	if(dailogId == 'studentAddEditDetail') {
			$('#accordion').accordion('activate', 0 );
			$("#Student_Info").scrollTop(0);
			$("#Student_Additional_Info").scrollTop(0);
			$("#Student_Accommodation_Info").scrollTop(0);
			$('#Student_Additional_Info').hide();
			$('#Student_Accommodation_Info').hide();
			//populateTreeSelect();
			$('#orgInnerID').jstree('close_all', -1);
			$("#notSelectedOrgNode").css("display","inline");
			$("#selectedOrgNode").text("");	
			isPopUp = false;
			isAddStudPopup = false;
			checkedListObject = {};
			$('#orgInnerID').jstree('uncheck_all');
		}
	if(dailogId == 'modifyTestPopup'){
		licenseData = null;
	  	licenseDataMap =  null;
	}	


	$("#"+dailogId).dialog("close");
	
}

function displayListPopup(element) {
	if (isButtonDisabled(element))
		return true;
	
	populateGridAsPerView();
}

function populateGridAsPerView() {
	if(isButtonDisabled(document.getElementById('registerButton'))) {
		return true;
	}
	$("#displayMessageMain").hide();
	disableButton('nextButtonStdPopup');
	disableButton('backButtonSPFPopup');
	if(currentView == "student") {
		onNextFromShowBySessionPopUp();
	} else {
		sessionScoring();
	}
	isPopUp = true;
}

 	function sessionScoring() {
    	clearMessage();
    	if(currentView == "student"){
		   	$("#sessionListSubTitle").show();
		   	$("#studentListSubTitle").hide();
		   	$("#sessionListTitle").show();
		   	$("#studentListTitle").hide();
    	} else {
    		$("#sessionListSubTitle").hide();
		   	$("#studentListSubTitle").show();
		   	$("#sessionListTitle").hide();
		   	$("#studentListTitle").show();
    	}
		$("#sessionStudRegId").dialog({  
			title:$("#sessionStudRegPopupTitle").val(),  
			resizable:false,
			autoOpen: true,
			width: '1024px',
			modal: true,
			closeOnEscape: false,
			open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		});
			
		if(!scoreByStdGridLoaded)
           	populateScoreByStudentGrid();
        else
          	scoreByStudentGridReload();
			setPopupPosition('sessionStudRegId'); 
	}
	
	function sessionPopupFromStudentView(){
		clearMessage();
		if(currentView == "student"){
		   	$("#sessionListSubTitle").show();
		   	$("#studentListSubTitle").hide();
		   	$("#sessionListTitle").show();
		   	$("#studentListTitle").hide();
    	} else if(currentView != "student" && (isPopupOnByepassFR ||isPopupOnFRAccept||isPopupOnFRNotAccept)) {
    		$("#sessionListSubTitle").show();
		   	$("#studentListSubTitle").hide();	
		   	$("#sessionListTitle").show();
		   	$("#studentListTitle").hide();	   	    		
    	} else {
    		$("#sessionListSubTitle").hide();
		   	$("#studentListSubTitle").show();
		   	$("#sessionListTitle").hide();
		   	$("#studentListTitle").show();
    	}
		$("#sessionStudRegId").dialog({  
			title:$("#sessionStudRegPopupTitle").val(),  
			resizable:false,
			autoOpen: true,
			width: '1024px',
			modal: true,
			closeOnEscape: false,
			open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		});
		
		setPopupPosition('sessionStudRegId'); 
		
	}
	
function setPopupPosition(popupId){
		var toppos = ($(window).height() - 650) /2 + 'px';
		var leftpos = ($(window).width() - 1024) /2 + 'px';
		$("#"+popupId).parent().css("top",toppos);
		$("#"+popupId).parent().css("left",leftpos);	
	}

	function scoreObtainedFormatter(cellvalue, options, rowObject) {
		var val = cellvalue;
		var completed = rowObject.scoreStatus;
		var answered = rowObject.answered;
		if(completed == "Incomplete") {
			if(answered != undefined && answered == "Answered") {
				val = "-";
			} else {
				val = "0";
			}
		}
		return val;
	}
		
	
/******JqGrid Population and Reloads*****/
// For landing "show by student" grid
function populateReportingStudentGrid() {
		
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
        $("#studentRegistrationGrid").jqGrid({         
          url:'getStudentForReportingGrid.do', 
		 mtype:   'POST',
		 postData: postDataObject,
		 datatype: "json",         
          colNames:[$("#stuGrdLoginId").val(),$("#stuGrdStdName").val(), $("#grdGroup").val(), $("#stuGrdGrade").val(),$("#stuGrdGender").val(), studentIdTitle, "",""],
		   	colModel:[
		   		{name:'userName',index:'userName', width:150, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentName',index:'studentName', width:150, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr', width:150, editable: true, align:"left",sorttype:'text',search: false,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:100, align:"left",search: true, sortable:true,sorttype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: gradeOptions } },
		   		{name:'gender',index:'gender', width:125, editable: true, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: genderOptions } },
		   		{name:'studentNumber',index:'studentNumber', width:125, editable: true, align:"left",sorttype:'text',search: false,sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeIdList',index:'orgNodeIdList',hidden: true, editable: false, width:5, align:"left", sortable:false,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesList',index:'orgNodeNamesList',hidden: true, editable: false, width:5, align:"left", sortable:false,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	
		   		
		   	],
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",
		   	records: function(obj) {} },
		   	
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#studentRegistrationPager', 
			sortname: 'userName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: $("#jqGrid-content-section").width(),
			ondblClickRow: function(rowid) {populateGridAsPerView();},
			editurl: 'getStudentForReportingGrid.do',
			caption:studentGridTitle,
			onPaging: function() {
				var reqestedPage = parseInt($('#studentRegistrationGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_studentRegistrationPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#studentRegistrationGrid').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#studentRegistrationGrid').setGridParam({"page": minPageSize});
				}
				setAnchorButtonState('registerButton', true);
			},
			onSortCol : function(index, columnIndex, sortOrder) { 
				setAnchorButtonState('registerButton', true);
			},
			onSelectRow: function (rowId) {
					$("#displayMessageMain").hide();
					osterId = rowId;
					selectedRData = $("#studentRegistrationGrid").getRowData(rowId);
					selectedStudentId = rowId;
					selectedStudentOrgNodeName = selectedRData.orgNodeNamesList;
					selectedStudentOrgNodeid   = selectedRData.orgNodeIdList;
					selectedStudentNameFromSessionPopup = selectedRData.studentName;
					setAnchorButtonState('registerButton', false);
			},
			loadComplete: function () {
				setAnchorButtonState('registerButton', true);
				$("#studentRegistrationGrid").jqGrid('resetSelection');
				if ($('#studentRegistrationGrid').getGridParam('records') === 0) {
            		$('#sp_1_studentRegistrationPager').text("1");
            		$('#next_studentRegistrationPager').addClass('ui-state-disabled');
            		$('#last_studentRegistrationPager').addClass('ui-state-disabled');
            		$('#studentRegistrationGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#studentRegistrationGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/StudentWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsg").val()+"</td></tr></tbody></table></td></tr>");
            	}
				$.unblockUI();  
				$("#studentRegistrationGrid").setGridParam({datatype:'local'});
				var tdList = ("#studentRegistrationPager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });  
	 jQuery("#studentRegistrationGrid").jqGrid('filterToolbar',{
	 	afterSearch: function(){ 
	 		searchStudentByKeyword();
	 		setAnchorButtonState('registerButton', true);
	 	}});
			jQuery("#studentRegistrationGrid").navGrid('#studentRegistrationPager',{
				search: false,add:false,edit:false,del:false 	
			}).jqGrid('navButtonAdd',"#studentRegistrationPager",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchStudentByKeyword").dialog({  
						title:$("#searchStudentSession").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide();}
					 	});
			    }, position: "one-before-last", title:"Search Student", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#studentRegistrationPager",{position: "first"

			}).jqGrid('navButtonAdd',"#studentRegistrationPager",{
			    caption:"", buttonicon:"ui-icon-plus", onClickButton:function(){
			    	 requetForStudent = "";
		    		AddStudentDetail();
			    }, position: "first", title:"Add Student", cursor: "pointer",id:"add_list2"
			});
			
			jQuery(".ui-icon-refresh").bind("click",function(){
				$("#searchStudentByKeywordInput").val('');
				setAnchorButtonState('registerButton', true);
			}); 
			
}

function gridScoringStudentReload(){
	var postDataObject = {};
	postDataObject.q = 2;
	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
	jQuery("#studentRegistrationGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
	jQuery("#studentRegistrationGrid").jqGrid('setGridParam', {url:'getStudentForReportingGrid.do',postData:postDataObject,page:1}).trigger("reloadGrid");
	jQuery("#studentRegistrationGrid").sortGrid('userName',true,'asc');
	$("#displayMessageMain").hide();

}

// used for landing page by session
function populateRegistrationSessionGrid() {
		
		var gridCntWidth = 0;
		gridCntWidth = $("#jqGrid-content-section").width();
		gridCntWidth = parseInt(gridCntWidth) - 180;
		var sessionGridTitle = '<table width="'+$("#jqGrid-content-section").width()+'px">'
								+ '<tbody><tr><td align="left"><span>'+$("#sesGridCaption").val()+'</span>'
								+ '</td><td align="right"><span style="float: right;"><b>'
								+$("#gridShowBy").val()+'</b>:<select id="showBySession" '
								+ ' onchange="viewByStudent();"><option>'+$("#gridShowBySes").val()
								+'&nbsp;</option><option>'+$("#gridShowByStu").val()+'&nbsp;</option>'
								+ '</select>&nbsp;&nbsp;</span></td></tr></tbody></table>';
	
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
        $("#sessionRegistrationGrid").jqGrid({         
          url:'getSessionForReportingGrid.do', 
		 mtype:   'POST',
		 postData: postDataObject,
		 datatype: "json",         
          colNames:[$("#grdSessionName").val(),$("#grdTestName").val(), $("#grdGroup").val(), $("#sesGridMyRole").val(),$("#sesGridStatus").val(), $("#sesGridStartDate").val(), $("#sesGridEndDate").val(), '', '', '','','',''],
		   	colModel:[
		   		{name:'testAdminName',index:'testAdminName', width:160, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testName',index:'testName', width:160, editable: true, align:"left",sorttype:'text',search: true,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: testNameOptions } },
		   		{name:'creatorOrgNodeName',index:'creatorOrgNodeName', width:100, editable: true, align:"left",sorttype:'text',search: false,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'AssignedRole',index:'AssignedRole',editable: true, width:60, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: myRoleOptions } },
		   		{name:'testAdminStatus',index:'testAdminStatus', width:80, editable: true, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: statusOptions } },
		   		{name:'loginStartDate',index:'loginStartDate', width:80, editable: true, align:"left",search: false, sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'}, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginEndDate',index:'loginEndDate', width:80, editable: true, align:"left",search: false,sorttype:'date', formatter:'date', formatoptions: {srcformat:'M d, Y h:i:s', newformat:'m/d/y'}, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'productType',index:'productType',editable: true, hidden:true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }},
		   	    {name:'isTabeProduct',index:'isTabeProduct',editable: true, hidden:true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }},
		   	    {name:'isTabeAdaptiveProduct',index:'isTabeAdaptiveProduct',editable: true, hidden:true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }},
		   	    {name:'itemSetId',index:'itemSetId',editable: true, hidden:true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }},
		   	    {name:'loginStartDateString',index:'loginStartDateString',editable: true, hidden:true, width:0, align:"left",search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginEndDateString',index:'loginEndDateString',editable: true, hidden:true, width:0, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"testSessionCUPA", id:"testAdminId",
		   	records: function(obj) {} },
		   	
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#sessionRegistrationPager', 
			sortname: 'testAdminName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: $("#jqGrid-content-section").width(), 
			editurl: 'getSessionForReportingGrid.do',
			ondblClickRow: function(rowid) {populateGridAsPerView();},
			caption:sessionGridTitle,
			onPaging: function() {
				var reqestedPage = parseInt($('#sessionRegistrationGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_sessionRegistrationPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#sessionRegistrationGrid').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#sessionRegistrationGrid').setGridParam({"page": minPageSize});
				}
				setAnchorButtonState('registerButton', true);
			},
			onSortCol : function(index, columnIndex, sortOrder) { 
				setAnchorButtonState('registerButton', true);
			},
			onSelectRow: function (rowId) {
					setAnchorButtonState('registerButton', false);
					$("#displayMessageMain").hide();
					var selectedRowData = $("#sessionRegistrationGrid").getRowData(rowId);
					selectedTestAdminName = selectedRowData.testAdminName;
					selectedTestAdminId = rowId;
					selectedItemSetIdTC = selectedRowData.itemSetId;
					if(selectedRowData.isTabeAdaptiveProduct == 'true' ){
						isTabeProduct = false;
						isTabeAdaptiveProduct = true;
					} else {
						isTabeProduct = true;
						isTabeAdaptiveProduct = false;
					} 
				},
			loadComplete: function () {
				setAnchorButtonState('registerButton', true);
				$("#sessionRegistrationGrid").jqGrid('resetSelection');
				if ($('#sessionRegistrationGrid').getGridParam('records') === 0) {
            		$('#sp_1_sessionRegistrationPager').text("1");
            		$('#next_sessionRegistrationPager').addClass('ui-state-disabled');
            		$('#last_sessionRegistrationPager').addClass('ui-state-disabled');
            		$('#sessionRegistrationGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#sessionRegistrationGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/ScoringWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noSessionTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noSessionMessage").val()+"</td></tr></tbody></table></td></tr>");
            	}
				$.unblockUI();  
				$("#sessionRegistrationGrid").setGridParam({datatype:'local'});
				var tdList = ("#sessionRegistrationPager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });  
	 jQuery("#sessionRegistrationGrid").jqGrid('filterToolbar',{
	 	afterSearch: function(){ 
	 		searchSessionByKeyword();
	 		setAnchorButtonState('registerButton', true);
	 	}
	 });
			jQuery("#sessionRegistrationGrid").navGrid('#sessionRegistrationPager',{
				search: false,add:false,edit:false,del:false 	
			}).jqGrid('navButtonAdd',"#sessionRegistrationPager",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchSessionByKeyword").dialog({  
						title:$("#searchStudentSession").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide();}
					 	});
			    }, position: "one-before-last", title:"Search Session", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#sessionRegistrationPager",{position: "first"
			});
			
			jQuery(".ui-icon-refresh").bind("click",function(){
				$("#searchSessionByKeywordInput").val('');
				setAnchorButtonState('registerButton', true);
			}); 
			
					
}

// for landing page of session grid
function reloadRegistrationSessionGrid(){
	var postDataObject = {};
	postDataObject.q = 2;
	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
	jQuery("#sessionRegistrationGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
	jQuery("#sessionRegistrationGrid").jqGrid('setGridParam', {url:'getSessionForReportingGrid.do',postData:postDataObject,page:1}).trigger("reloadGrid");
	jQuery("#sessionRegistrationGrid").sortGrid('testAdminName',true,'asc');
	$("#displayMessageMain").hide();
}



function populateScoreByStudentGrid() {
		
		scoreByStdGridLoaded = true;
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.testAdminId = selectedTestAdminId;
        $("#scoreByStudentListGrid").jqGrid({         
          url:'getStudentListForScoreByStudent.do', 
		 mtype:   'POST',
		 postData: postDataObject,
		 datatype: "json",         
          colNames:[$("#stuGrdLoginId").val(),$("#sbsGridFirstName").val(), $("#sbsGridLastName").val(), $("#studentIdLabelName").val(),$("#stuGrdGrade").val(), $("#itemGripManual").val(), $(/*"#sbsGridOnStatus"*/).val(), '', '', '', ''],
		   	colModel:[
		   		{name:'userName',index:'userName', width:200, editable: true, align:"left", sorttype:'text', sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'lastName',index:'lastName', width:150, editable: true, align:"left", sorttype:'text', sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:150, editable: true, align:"left", sorttype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'extPin1',index:'extPin1',editable: true, width:160, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade', width:90, editable: true, align:"left", sortable:true,sorttype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'scoringStatus',index:'scoringStatus',editable: true, width:250, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testCompletionStatusDesc',index:'testCompletionStatusDesc',editable: true, width:200, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentName',index:'studentName',editable: true, width:200, align:"left", sortable:true, hidden:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentUserName',index:'studentUserName',editable: true, width:10, align:"left", sortable:true, hidden:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testRosterId',index:'testRosterId',editable: true, width:10, align:"left", sortable:true, hidden:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetIdTC',index:'itemSetIdTC',editable: true, width:200, align:"left", sortable:true, hidden:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"scoreByStudentList", id:"testRosterId",
		   	records: function(obj) {} },
		   	
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			pager: '#scoreByStudentListPager', 
			sortname: 'userName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 170,
			width: 920,
			hoverrows: false,
			editurl: 'getStudentListForScoreByStudent.do',
			caption:$("#stuGridCaption").val(),
			onPaging: function() {
				var reqestedPage = parseInt($('#scoreByStudentListGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_scoreByStudentListPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#scoreByStudentListGrid').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#scoreByStudentListGrid').setGridParam({"page": minPageSize});
				}
				
			},
			onSelectRow: function (rowId) {
					$("#"+rowId).removeClass('ui-state-highlight');
			},
			loadComplete: function () {
				if ($('#scoreByStudentListGrid').getGridParam('records') === 0) {
            		$('#sp_1_scoreByStudentListPager').text("1");
            		$('#next_scoreByStudentListPager').addClass('ui-state-disabled');
            		$('#last_scoreByStudentListPager').addClass('ui-state-disabled');
            		$('#scoreByStudentListGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#scoreByStudentListGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/ScoringWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$(/*"#sbsEmptyGrid"*/).val()+"</td></tr></tbody></table></td></tr>");
            	}
				$.unblockUI();  
				$("#scoreByStudentListGrid").setGridParam({datatype:'local'});
				$("#scoreByStudentListPager .ui-pg-input").attr("style", "position: relative; z-index: 100000;");
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
		
	}
	
function scoreByStudentGridReload(){
	var postDataObject = {};
	postDataObject.q = 2;
 	postDataObject.testAdminId = selectedTestAdminId;
	jQuery("#scoreByStudentListGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
	jQuery("#scoreByStudentListGrid").jqGrid('setGridParam', {url:'getStudentListForScoreByStudent.do',postData:postDataObject,page:1}).trigger("reloadGrid");
	jQuery("#scoreByStudentListGrid").sortGrid('userName',true,'asc');
	$("#scoreByStudentListPager .ui-pg-input").attr("style", "position: relative; z-index: 100000;");

}





function gridItemStudentReloadSBI(itemSetId, itemId){

	var postDataObject = {};
	postDataObject.q = 2;
 	postDataObject.testAdminId = selectedTestAdminId;
 	postDataObject.itemSetId = itemSetId;
 	postDataObject.itemId = itemId;
	jQuery("#itemStudentListGridSBI").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
	jQuery("#itemStudentListGridSBI").jqGrid('setGridParam', {url:'getStudentListForItem.do',postData:postDataObject,page:1}).trigger("reloadGrid");
	jQuery("#itemStudentListGridSBI").sortGrid('userName',true,'asc');
	$("#itemStudentListPagerSBI .ui-pg-input").attr("style", "position: relative; z-index: 100000;");
}
	
/**
* Scoring PopUp Implementation
*
**/
	function isObject(obj) {
	    return obj.toString() === '[object Object]';
	}
	
	function handleSpecialCharactersNewUI(s) {
		s= s.replace(/&nbsp;/g,' ').split('');
		var k;
		for(var i= 0; i<s.length; i++){
			k= s[i];
			c= k.charCodeAt(0);
			s[i]= (function(){
				switch(c){
					case 60: return "&lt;";
					case 62: return "&gt;";
					case 34: return "&quot;";
					case 38: return "&amp;";
					case 39: return "&#39;";
					//For IE
					case 65535: {
						if(!((s[i-1].charCodeAt(0)>=65 && s[i-1].charCodeAt(0)<=90) || (s[i-1].charCodeAt(0)>=97 && s[i-1].charCodeAt(0)<=122)) || !((s[i+1].charCodeAt(0)>=65 && s[i+1].charCodeAt(0)<=90) || (s[i+1].charCodeAt(0)>=97 && s[i+1].charCodeAt(0)<=122)))
							return "&quot;";
						else
							return "&#39;";
					}
					//For Mozila and Safari
					case 65533: {
						if(!((s[i-1].charCodeAt(0)>=65 && s[i-1].charCodeAt(0)<=90) || (s[i-1].charCodeAt(0)>=97 && s[i-1].charCodeAt(0)<=122)) || !((s[i+1].charCodeAt(0)>=65 && s[i+1].charCodeAt(0)<=90) || (s[i+1].charCodeAt(0)>=97 && s[i+1].charCodeAt(0)<=122)))
							return "&quot;";
						else
							return "&#39;";
					}
					default: return k;
				}
			})();
		}
		return s.join('');
	}
	
	function hideMessage(){
		clearMessage();
	}
	
	
				
		
	function getItemSetIdTC(itemId) {
	    var rowElement = $('#' + itemId, '#itemListGrid');
	    rowElement = rowElement[0];
	
	    if (rowElement) {
	        var itemSetIdTC = rowElement.lastChild.innerHTML;
	        return itemSetIdTC;
	    }
	
	}
		

 function clearMessage(){
 //document.getElementById('displayMessageForQues').style.display = "none";	
 //document.getElementById('displayMessageStudent').style.display = "none";	
 //document.getElementById('displayMessageSession').style.display = "none";	 
 }
 
	 function buildMessage(argObject, element, status) {
	    document.getElementById(argObject.messageElement).style.display = "block";
	    if (status) {
	        $("#" + argObject.infoElement).show();
	        $("#" + argObject.errorElement).hide();
	    } else {
	        $("#" + argObject.infoElement).hide();
	        $("#" + argObject.errorElement).show();
	    }
	    var textMessage = $("#" + element).val();
	    $("#" + argObject.contentElement).text(textMessage);
	}
/******JqGrid Search Implementation*****/

	function searchStudentByKeyword(){
		var searchFiler = $.trim($("#searchStudentByKeywordInput").val()), f;
		var grid = $("#studentRegistrationGrid"); 
		if (searchFiler.length === 0) {
			//grid[0].p.search = false;
			//grid[0].triggerToolbar();// to trigger previously applied filters
			var g = {groupOp:"AND",rules:[]};
			//g.rules.push({field:"testCatalogName",op:"bw",data:$("#gs_testCatalogName").val()});
			g.rules.push({field:"gender",op:"bw",data:$("#gs_gender").val()});
			g.rules.push({field:"grade",op:"bw",data:$("#gs_grade").val()});
			grid[0].p.search = true;
			grid[0].p.ignoreCase = true;			 
			$.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
		}else {
			var g = {groupOp:"AND",rules:[],groups:[]};
			//g.rules.push({field:"testCatalogName",op:"bw",data:$("#gs_testCatalogName").val()});
			g.rules.push({field:"gender",op:"bw",data:$("#gs_gender").val()});
			g.rules.push({field:"grade",op:"bw",data:$("#gs_grade").val()});			 
			f = {groupOp:"OR",rules:[]};
			f.rules.push({field:"userName",op:"cn",data:searchFiler});
			f.rules.push({field:"studentName",op:"cn",data:searchFiler});
			f.rules.push({field:"orgNodeNamesStr",op:"cn",data:searchFiler});
			f.rules.push({field:"grade",op:"cn",data:searchFiler});
			f.rules.push({field:"gender",op:"cn",data:searchFiler});
			f.rules.push({field:"studentNumber",op:"cn",data:searchFiler});
			//f.rules.push({field:"testSessionName",op:"cn",data:searchFiler}); 
			//f.rules.push({field:"testCatalogName",op:"cn",data:searchFiler});			   
		 	g.groups.push(f);		 	
			grid[0].p.search = true;
			grid[0].p.ignoreCase = true;
			$.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
		 }
		 grid.trigger("reloadGrid",[{page:1,current:true}]);
		 closePopUp('searchStudentByKeyword');
	}
	
	function searchSessionByKeyword(){
		 var searchFiler = $.trim($("#searchSessionByKeywordInput").val()), f;
		 var grid = $("#sessionRegistrationGrid"); 
		 
		 if (searchFiler.length === 0) {
			 //grid[0].p.search = false;
			 //grid[0].triggerToolbar();// to trigger previously applied filters
			 var g = {groupOp:"AND",rules:[],groups:[]};
			 g.rules.push({field:"testName",op:"cn",data:$("#gs_testName").val()});
			 g.rules.push({field:"AssignedRole",op:"cn",data:$("#gs_AssignedRole").val()});
			 g.rules.push({field:"testAdminStatus",op:"cn",data:$("#gs_testAdminStatus").val()});
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;			 
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
		 }else {
		 	 var g = {groupOp:"AND",rules:[],groups:[]};
			 g.rules.push({field:"testName",op:"cn",data:$("#gs_testName").val()});
			 g.rules.push({field:"AssignedRole",op:"cn",data:$("#gs_AssignedRole").val()});
			 g.rules.push({field:"testAdminStatus",op:"cn",data:$("#gs_testAdminStatus").val()});			 
		 	 f = {groupOp:"OR",rules:[]};
			 f.rules.push({field:"testAdminName",op:"cn",data:searchFiler});
			 f.rules.push({field:"testName",op:"cn",data:searchFiler});
			 f.rules.push({field:"creatorOrgNodeName",op:"cn",data:searchFiler});
			 f.rules.push({field:"AssignedRole",op:"cn",data:searchFiler});
			 f.rules.push({field:"testAdminStatus",op:"cn",data:searchFiler});
			 f.rules.push({field:"loginStartDateString",op:"cn",data:searchFiler});
			 f.rules.push({field:"loginEndDateString",op:"cn",data:searchFiler}); 			 
			 g.groups.push(f);			 
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;			 
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
		 }
		 grid.trigger("reloadGrid",[{page:1,current:true}]);
		 closePopUp('searchSessionByKeyword');
	}
	
	function resetSearch(){
		if(currentView == "student") {
			var grid = $("#studentRegistrationGrid"); 
			$("#searchStudentByKeywordInput").val('');
			 //grid[0].p.search = false;
			 var g = {groupOp:"AND",rules:[]};
			// g.rules.push({field:"testCatalogName",op:"bw",data:$("#gs_testCatalogName").val()});
			 g.rules.push({field:"gender",op:"bw",data:$("#gs_gender").val()});
			 g.rules.push({field:"grade",op:"bw",data:$("#gs_grade").val()});
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;			 
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
			 grid.trigger("reloadGrid",[{page:1,current:true}]); 
			 closePopUp('searchStudentByKeyword');
			 //grid[0].triggerToolbar();// to trigger previously applied filters
		 } else {
		 	var grid = $("#sessionRegistrationGrid"); 
			$("#searchSessionByKeywordInput").val('');
			 //grid[0].p.search = false;
			 var g = {groupOp:"AND",rules:[],groups:[]};
			 g.rules.push({field:"testName",op:"cn",data:$("#gs_testName").val()});
			 g.rules.push({field:"AssignedRole",op:"cn",data:$("#gs_AssignedRole").val()});
			 g.rules.push({field:"testAdminStatus",op:"cn",data:$("#gs_testAdminStatus").val()});
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;			 
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
			 grid.trigger("reloadGrid",[{page:1,current:true}]); 
			 closePopUp('searchSessionByKeyword');
			 //grid[0].triggerToolbar();// to trigger previously applied filters
		 }
	}

function trapEnterKey(e){
	var key;
	if(window.event)
		key = window.event.keyCode;     //IE
	else
		key = e.which;     //firefox
	        
		if(key == 13){
			if(currentView == "student")
				searchStudentByKeyword();
	   		else
	   			searchSessionByKeyword();
	   }
	}
	
	

/******Jstree Methods*****/

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
				switch(currentCategoryLevel){
					
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

	//method triggered from library
	  function customLoad(){
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
			else {
				/*currentElement = currentElement[1];*/ 
				if($('#secondaryJQGridDiv').is(':visible')){
					currentElement  = $("#secondaryOuterTreeFromSessionDiv li[id="+currentNodeId+"]")[0];
				}
				else {
					currentElement  = $("#outerTreeForStudentFromSessionDiv li[id="+currentNodeId+"]")[0];
				}				
			}
				
		/* rapid registration start*/		
		} else if(isAddStudPopup) {
			/*currentElement = $('li[id='+ currentNodeId+ ']');
			if(currentElement.length < 3) currentElement = currentElement[1];
			else currentElement = currentElement[2];*/
			currentElement  = $("#innertreebgdiv li[id="+currentNodeId+"]")[0];
		}
		 /* rapid registration end*/
		else {		
			currentElement = document.getElementById(currentNodeId);
		}
	
		currentElement.className = "jstree-open jstree-unchecked";
		var fragment = document.createDocumentFragment();
		var ulElement = document.createElement('ul');
		
		if(type == "innerOrgNodeHierarchyForStd" || type == "secondaryInnerOrgNodeHierarchy"){
			stream(objArray,ulElement,fragment,streamPush, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
			 //currentElement.childNodes[1].firstChild.style.display = "none";
		 });	
		 }
		 /* rapid registration start*/
		 else if(type == "orgInnerID"){
			stream(objArray,ulElement,fragment,streamInnerPush1, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
			 //currentElement.childNodes[1].firstChild.style.display = "none";
		 });	
		 }
		 /* rapid registration end*/
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
		/*if (objArray.attr.cid == leafNodeCategoryId){
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: inline-block;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}else{
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display:        none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}
		if(profileEditable === "false"  && $("#classReassignable").val() === "true") {
			liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}*/
		    liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		ulElement.appendChild(liElement);
		fragment.appendChild(ulElement);
	  }
   function streamInnerPush1(objArray,ulElement,fragment){
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
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display:        none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}
		if(profileEditable === "false"  && $("#classReassignable").val() === "true") {
			liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}
		    //liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> "; // commented for overwriting innerHTML
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
  
  
  
	function prepareData(classState, currentCategoryLevel, currentNodeId, element) {
	
	    if (classState == false || !classState || classState == 'false') {
	        var cacheData = map.get(currentNodeId);
	        if (cacheData != null) {
	            currentTreeArray = cacheData;
	        }
	        if (cacheData == null) {
	            if (currentCategoryLevel == "1") {
	                dataObj2 = [];
	                var indexOfRoot = getIndexOfRoot(currentNodeId);
	                populateTreeImmediate(currentNodeId, currentCategoryLevel, indexOfRoot);
	            }
	            switch (String(currentCategoryLevel)) {
	
	            case "2":
	                dataObj3 = getObject(jsonData, currentNodeId, currentCategoryLevel, element);
	                currentIndex = dataObj3.index;
	                currentTreeArray = dataObj3.jsonData;
	                map.put(currentNodeId, currentTreeArray);
	                break;
	            case "3":
	                dataObj4 = map.get(element);
	                currentTreeArray = getObject(dataObj4, currentNodeId, currentCategoryLevel);
	                currentIndex = currentTreeArray.index;
	                currentTreeArray = currentTreeArray.jsonData;
	                map.put(currentNodeId, currentTreeArray);
	                break;
	            case "4":
	                dataObj5 = map.get(element);
	                currentTreeArray = getObject(dataObj5, currentNodeId, currentCategoryLevel);
	                currentIndex = currentTreeArray.index;
	                currentTreeArray = currentTreeArray.jsonData;
	                map.put(currentNodeId, currentTreeArray);
	                break;
	            case "5":
	                dataObj6 = map.get(element);
	                currentTreeArray = getObject(dataObj6, currentNodeId, currentCategoryLevel);
	                currentIndex = currentTreeArray.index;
	                currentTreeArray = currentTreeArray.jsonData;
	                map.put(currentNodeId, currentTreeArray);
	                break;
	            case "6":
	                dataObj7 = map.get(element);
	                currentTreeArray = getObject(dataObj7, currentNodeId, currentCategoryLevel);
	                currentIndex = currentTreeArray.index;
	                currentTreeArray = currentTreeArray.jsonData;
	                map.put(currentNodeId, currentTreeArray);
	                break;
	            case "7":
	                dataObj8 = map.get(element);
	                currentTreeArray = getObject(dataObj8, currentNodeId, currentCategoryLevel);
	                currentIndex = currentTreeArray.index;
	                currentTreeArray = currentTreeArray.jsonData;
	                map.put(currentNodeId, currentTreeArray);
	                break;
	            case "8":
	                dataObj9 = map.get(element);
	                currentTreeArray = getObject(dataObj9, currentNodeId, currentCategoryLevel);
	                currentIndex = currentTreeArray.index;
	                currentTreeArray = currentTreeArray.jsonData;
	                map.put(currentNodeId, currentTreeArray);
	                break;
	
	            }
	        }
	
	    }
	
	}
  
  
  function openNextLevel(asyncOver) {
      if (leafParentOrgNodeId.length - 1 > asyncOver) {
          var tmpNode = leafParentOrgNodeId[asyncOver];
          currentCategoryLevel = String(asyncOver + 1);
          currentNodeId = tmpNode;
          currentTreeArray = map.get(currentNodeId);
          $('#innerOrgNodeHierarchyForStd').jstree('open_node', "#" + currentNodeId);
          $('#orgInnerID').jstree('open_node', "#"+currentNodeId); // rapid registration
          if($('#secondaryJQGridDiv').is(':visible'))
          	$('#secondaryInnerOrgNodeHierarchy').jstree('open_node', "#" + currentNodeId);
      }
  }
  
   function isWindows() {
		return navigator.userAgent.indexOf('Win') > -1;
	}
	

	/////  added 
	
	function populateTreeSelect() {
	/* rapid registration start*/
	if(customerDemographicValue == "")
  			customerDemographicValue = $("#studentAddEditDetail *").serializeArray(); 
  	/* rapid registration end*/
		$("#notSelectedOrgNodes").css("display","inline");
		$("#selectedOrgNodesName").text("");	
		$("#innerOrgNodeHierarchyForStd").undelegate();
		$("#innerOrgNodeHierarchyForStd").unbind();
		createSingleNodeSelectionTreeForStudent (orgTreeHierarchy);
	}
	

	function populateTreeSelectSecondary() {
		//if(customerDemographicValue == "")
  			//customerDemographicValue = $("#studentAddEditDetail *").serializeArray(); 
		$("#notSelectedOrgNodes").css("display","inline");
		$("#selectedOrgNodesName").text("");	
		$("#secondaryInnerOrgNodeHierarchy").undelegate();
		$("#secondaryInnerOrgNodeHierarchy").unbind();
		createSingleNodeSelectionTreeForStudentSecondary (orgTreeHierarchy);
	}
	
	function hideLocatorMessage() {
	    clearTimeout(htimer);
	    $('#statusLocatorLegend').hide();
	    //document.getElementById("statusLocatorLegend").style.display = "none";
	}
	
	function showLocatorMessage(event) {
	    var isIE = document.all ? true: false;
	    var tempX = 0;
	    var tempY = 0;
	    var legendDiv = null;
	    var padding = 15;
	    
	    if (isIE) {
	        tempX = event.clientX + document.documentElement.scrollLeft;
	        tempY = event.clientY + document.documentElement.scrollTop;
	    } else {
	        tempX = event.pageX;
	        tempY = event.pageY;
	    }
	    legendDiv = document.getElementById("statusLocatorLegend");
	    legendDiv.style.left = (tempX - $(legendDiv).width() / 3) + "px";
	    legendDiv.style.top = (tempY - $(legendDiv).height() - padding) + "px";
	    legendDiv.style.display = "block";
	    htimer = setTimeout("hideLocatorMessage()", 10000);
	}
	/* rapid registration start*/
		
	function onCancel() {
		isValueChanged = false;
		
	if(isAddStudent){
		  if($("#studentRegFirstName").val()!= "" 
			 || $("#studentRegMiddleName").val()!= ""
			 || $("#studentRegLastName").val()!= ""
			 || $("#genderOptions").val() != "Select a gender"
			 || $("#gradeOptions").val() != "Select a grade" ) {
			 isValueChanged = true;	
			 }
		 if(!isValueChanged) {	
			 var radiofields = $("#studentAddEditDetail :radio"); 
	       	 for (var i=0; i<radiofields.length; i++) {
			   	if (radiofields[i].value != "None" && radiofields[i].checked == true && radiofields[i].checked != ""  && radiofields[i].disabled == false && radiofields[i].getAttribute("disabled") == null) {
					isValueChanged = true;
					break;
				}
			}
		 }
		 if(!isValueChanged) {
			var allSelects = $("#studentAddEditDetail select");
			for(var count = 0; count < allSelects.length ; count++) {
				var selectedInd = $(allSelects[count]).attr("selectedIndex");
				if(selectedInd != 0 && ($(allSelects[count]).attr("disabled") == null  || $(allSelects[count]).attr("disabled") == false)) {
					isValueChanged = true;
					break;
				}
			}
		}
		if(!isValueChanged) {
		var customerValCheckbox = [];
			var counter=0;
		   var checkBoxfields = $("#studentAddEditDetail :checkbox"); 
	       for(var i=0; i < checkBoxfields.length; i++){
		       for(var j=0; j<customerDemographicValue.length; j++) {
		          	if($(checkBoxfields).eq(i).attr('name') == customerDemographicValue[j].name) {
		          	       // For bug MQC-67095
			       			if($(checkBoxfields).eq(i).attr('checked')== false && !($(checkBoxfields).eq(i).attr('disabled')))
			       			{	
			       				isValueChanged = true;
			       				break;
			       			}
			       			customerValCheckbox[counter] =  customerDemographicValue[j].name;
							counter++;
					}
		       	}
			} 
			if(!isValueChanged && customerValCheckbox.length > 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
			       for(var j=0; j<customerValCheckbox.length; j++) {
				       	if(!isExist( $(checkBoxfields).eq(i).attr('name') ,customerValCheckbox)) { //
				       			if($(checkBoxfields).eq(i).attr('checked')== true && !($(checkBoxfields).eq(i).attr('disabled')))
				       			{
				       				isValueChanged = true;
				       				break;
				       			}
				       			
						}
			       	}
				} 
			}
			if(!isValueChanged && customerValCheckbox.length == 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
				 // For bug MQC-67095
					if($(checkBoxfields).eq(i).attr('checked')== true && !($(checkBoxfields).eq(i).attr('disabled'))) {
						isValueChanged = true;
						break;
					}
				}
			}
			
		} 
		if(!isValueChanged){
			if(assignedOrgNodeIds != ""){
				isValueChanged = true;
			}
		}
		
		if(isValueChanged) {
		//UIBlock();
			openConfirmationPopup();	 
		} else {
			closePopUp('studentAddEditDetail');
		}
	
	} 
		
	}
	
	function reset() {
    	assignedOrgNodeIds = "";
    	
       jQuery.each(customerDemographicValue, function(i, field){         
      		$("#"+field.name).val(field.value);
      		
       });  
       var customerValCheckbox = [];
	   var counter=0;
       var checkBoxfields = $(":checkbox"); 
	       for(var i=0; i < checkBoxfields.length; i++){
		       for(var j=0; j<customerDemographicValue.length; j++) {
			       	if($(checkBoxfields).eq(i).attr('name') == customerDemographicValue[j].name) {
			       			customerValCheckbox[counter] =  customerDemographicValue[j].name;
							counter++;
					}
		       	}
			} 
			if(customerValCheckbox.length > 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
			       for(var j=0; j<customerValCheckbox.length; j++) {
				       	if(!isExist( $(checkBoxfields).eq(i).attr('name') ,customerValCheckbox)) { //
				       			$(checkBoxfields).eq(i).attr('checked', false); 
				    	} else {
				    			$(checkBoxfields).eq(i).attr('checked', true); 
				    	}
			       	}
				} 
			}
			if(customerValCheckbox.length == 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
					$(checkBoxfields).eq(i).attr('checked', false); 
				}
			} 
		var radiofields = $(":radio"); 
       for (var i=0; i<radiofields.length; i++) {
			if (radiofields[i].value== "None") { 
				radiofields[i].checked = true;
			}
		}

		var allSelects = $("#studentAddEditDetail select");
		for(var count = 0; count < allSelects.length ; count++) {
			$(allSelects[count]).find("option:eq(0)").attr("selected","true");
		}

		
   }
   
   function fillDropDown( elementId, optionList) {
	var ddl = document.getElementById(elementId);
	var optionHtml = "";
	for(var i = 0; i < optionList.length; i++) {
		optionHtml += "<option  value='"+optionList[i]+"'>"+optionList[i]+"</option>";	
	}
	$(ddl).html(optionHtml);
}

function isExist(val, customerValCheckbox){
	if(String(customerValCheckbox.length) == "undefined" && customerValCheckbox != null){
		if(val == customerValCheckbox){
		 return true;
		 }
	}
		for(var i=0; i < customerValCheckbox.length; i++){
			if(val == customerValCheckbox[i]){
				return true;
			}
		}
		return false;
	}
		
	function openTreeNodes(orgNodeId) {
	var isopened = false;
	asyncOver = 0;
	leafParentOrgNodeId = "";
	var par = null;
	var correctId;
	$('#orgInnerID').jstree('close_all');
		var isIdExist = $('#orgInnerID', '#'+assignedOrgNodeIds).length;
			if(isIdExist > 0){
				$('#orgInnerID').jstree('check_node', "#"+assignedOrgNodeIds);
				isopened = true; 
			} else {
				leafParentOrgNodeId = "";
				for(var i=0; i< organizationNodes.length; i++){
						if(orgNodeId == organizationNodes[i].orgNodeId){
							var leafOrgNodePath = organizationNodes[i].leafNodePath;
							 leafParentOrgNodeId = leafOrgNodePath.split(",");
							break;
						}
					}
					type = "orgInnerID";
					
					outerLoop: for(var key in rootMap){
						for(var i =0; i < leafParentOrgNodeId.length; i++ ){
							if (key == leafParentOrgNodeId[i]){
								correctId = i;
							break outerLoop;
							}
						}
					}
					leafParentOrgNodeId = leafParentOrgNodeId.slice(correctId,leafParentOrgNodeId.length);
					
					if(leafParentOrgNodeId.length > 0) {
						for(var count = 0; count < leafParentOrgNodeId.length - 1; count++) {
				  		 	var tmpNode = leafParentOrgNodeId[count];

				  		 	currentCategoryLevel = String(count+1);
				  		 	currentNodeId = tmpNode;
				  		 	if(count != 0) { 
				  		 		par = leafParentOrgNodeId[count-1];		 						  		 			
				  		 	} else {
				  		 		par = leafParentOrgNodeId[count];
				  		 	}
				  		 	prepareData(false,currentCategoryLevel,currentNodeId,par);
				  		 }	 			
				  		 	var tmpNode = leafParentOrgNodeId[0];	
				  		 	currentCategoryLevel = "1";
				  		 	currentNodeId = tmpNode;
				  		 	currentTreeArray = map.get(currentNodeId);
				  		 	$('#orgInnerID').jstree('open_node', "#"+currentNodeId); 
							
				  		 	$('#orgInnerID').jstree('check_node', "#"+orgNodeId);
				  		 	$("#"+orgNodeId).focus();
				  		 	isopened = true; 
			  		 }
		 		 hideCheckBox();
		 }
		
	}
	
	function hideCheckBox(){
		$("#orgInnerID li").not(".jstree-le").each(function() {
    			var orgcategorylevel = $(this).attr("cid");
    			if(orgcategorylevel != leafNodeCategoryId) {
	    		  $("a ins.jstree-checkbox", this).first().hide();
	    		  }
	    		  if(profileEditable === "false" && $("#studentClassReassignable").val() === "true") {
		    			$("a ins.jstree-checkbox", this).first().hide();
		    		}
	  	});
	
	}
	
	function setMessageMain(title, content, type, message){
			$("#contentMain").text(content);
		}
		
	function displayStudConfirmation() {
		$("#studentConfirmation").dialog({  
			title: 'Register Student',  
			resizable:false,
			autoOpen: true,
			width: '1024px',
			modal: true,
			closeOnEscape: false,
			open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		});
		
	}
	
	function trapEnterKeyInStudentPopup(e){
		var key;
		if(window.event)
			key = window.event.keyCode;     //IE
		else
			key = e.which;     //firefox
		        
		if(key == 13){
			searchStudentSessionByKeyword();
	    }
	}
	
	function resetFiltersInPopupFromStudentView() { // need to call this to reset popup filters from student view
		$("#gview_list2 select[id=gs_testName] option:eq(0)").attr('selected','Any'); 
		$("#gview_list2 select[id=gs_testAdminStatus] option:eq(0)").attr('selected','Any');
	}  
	
	function resetSearchCritInPopupFromStudentView(){
		$("#searchStudentSessionByKeywordInput").val('');
		var grid = $("#list2"); 
		grid.jqGrid('setGridParam',{search:false});	
	    var postData = grid.jqGrid('getGridParam','postData');
	    $.extend(postData,{filters:""});
	}
	
	function resetSessionSearch(){
			 	var grid = $("#list2"); 
				$("#searchStudentSessionByKeywordInput").val('');
				 //grid[0].p.search = false;
				 var g = {groupOp:"AND",rules:[],groups:[]};
				 g.rules.push({field:"testName",op:"cn",data:$("#gview_list2 select[id=gs_testName]").val()});
				 g.rules.push({field:"testAdminStatus",op:"cn",data:$("#gview_list2 select[id=gs_testAdminStatus]").val()});
				 grid[0].p.search = true;
				 grid[0].p.ignoreCase = true;			 
				 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
				 grid.trigger("reloadGrid",[{page:1,current:true}]); 
				 closePopUp('searchStudentSessionByKeyword');
				 //grid[0].triggerToolbar();// to trigger previously applied filters
		
	}
	
	function searchStudentSessionByKeyword(){
		 var searchFiler = $.trim($("#searchStudentSessionByKeywordInput").val()), f;
		 	var grid = $("#list2"); 
		 
		 if (searchFiler.length === 0) {
			 //grid[0].p.search = false;
			 //grid[0].triggerToolbar();// to trigger previously applied filters
			 var g = {groupOp:"AND",rules:[],groups:[]};			 
			 g.rules.push({field:"testName",op:"cn",data:$("#gview_list2 select[id=gs_testName]").val()});
			  g.rules.push({field:"AssignedRole",op:"cn",data:$("#gview_list2 select[id=gs_AssignedRole]").val()});
			 g.rules.push({field:"testAdminStatus",op:"cn",data:$("#gview_list2 select[id=gs_testAdminStatus]").val()});
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;			 
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
		 }else {
		 	 var g = {groupOp:"AND",rules:[],groups:[]};			 
		 	 g.rules.push({field:"testName",op:"cn",data:$("#gview_list2 select[id=gs_testName]").val()});
		 	 g.rules.push({field:"AssignedRole",op:"cn",data:$("#gview_list2 select[id=gs_AssignedRole]").val()});
			 g.rules.push({field:"testAdminStatus",op:"cn",data:$("#gview_list2 select[id=gs_testAdminStatus]").val()});
		 	 f = {groupOp:"OR",rules:[]};
			 f.rules.push({field:"testAdminName",op:"cn",data:searchFiler});
			 f.rules.push({field:"testName",op:"cn",data:searchFiler});
			 f.rules.push({field:"creatorOrgNodeName",op:"cn",data:searchFiler});
			 f.rules.push({field:"AssignedRole",op:"cn",data:searchFiler});
			 f.rules.push({field:"testAdminStatus",op:"cn",data:searchFiler});
			 f.rules.push({field:"loginStartDateString",op:"cn",data:searchFiler});
			 f.rules.push({field:"loginEndDateString",op:"cn",data:searchFiler}); 			 
			 g.groups.push(f);			 
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;			 
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
		 }
		 grid.trigger("reloadGrid",[{page:1,current:true}]);
		 closePopUp('searchStudentSessionByKeyword');
	}

  
