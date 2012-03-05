var bulkMoveGridLoaded = false;
var bulkMoveStuCounterPage = 0;
var allStudentInGrid = [];
var selectedStudentForMove = [];
var finalSelectedNode;
var isPopUp = false;


function populateBulkMoveTree() {
	isBulkMove = true;
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'userOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI(); 
						leafNodeCategoryId = data.leafNodeCategoryId;
						orgTreeHierarchy = data;
						jsonData = orgTreeHierarchy.data;
						getRootNodeDetails();
						createSingleNodeBulkMoveTree(orgTreeHierarchy);
						$("#searchheader").css("visibility","visible");	
						$("#bulkStudentMoveOrgNode").css("visibility","visible");	
												
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


function createSingleNodeBulkMoveTree(jsondata) {
	   $("#bulkStudentMoveOrgNode").jstree({
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
	    
	    $("#bulkStudentMoveOrgNode").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#orgNodeHierarchy ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
	    
	    $("#bulkStudentMoveOrgNode").delegate("a","click", function(e) {
	    	submittedSuccesfully = "";
	    	$("#displayMessageMain").hide();
	    	determineStudentSel(selectedStudentForMove, 'bulkMoveButton');
			SelectedOrgNodeId = $(this).parent().attr("id");
			var topNodeSelected = $(this).parent().attr("cid");
			if(topNodeSelected == leafNodeCategoryId || topNodeSelected == (leafNodeCategoryId -1)) {
 		    $("#selectedBulkTreeOrgNodeId").val(SelectedOrgNodeId);
 		     UIBlock();
 		  	if(!bulkMoveGridLoaded) {
 		  		bulkMoveGridLoaded = true;
 		  			populateBulkMoveStudentGrid();
 		  		}
			else
				gridReloadForBulkMoveStudent();
				
				 if(bulkMoveGridLoaded) {
				 	document.getElementById('viewStatus').style.visibility = "visible";
				}
			}
			totalRowSelectedOnPage = 0;
	    	onNodechange = true;
	    });
		
	registerDelegate("bulkStudentMoveOrgNode");		
	   
}

	function gridReloadForBulkMoveStudent(){
		UIBlock();
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.stuForOrgNodeId = $("#selectedBulkTreeOrgNodeId").val();
       jQuery("#studentBulkMoveGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
       jQuery("#studentBulkMoveGrid").jqGrid('setGridParam', {url:'getStudentForSelectedNode.do',postData:postDataObject,page:1}).trigger("reloadGrid");
       jQuery("#studentBulkMoveGrid").sortGrid('lastName',true,'asc');
      
  	}


function populateBulkMoveStudentGrid() {
 		UIBlock();
 		bulkMoveGridLoaded = true;
 		var studentIdTitle = $("#studentIdLabelName").val();
 		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.stuForOrgNodeId = $("#selectedBulkTreeOrgNodeId").val();

       $("#studentBulkMoveGrid").jqGrid({         
          url: 'getStudentForSelectedNode.do',
		  mtype:   'POST',
		  datatype: "json",
		  postData: postDataObject,
          colNames:[$("#jqgLastNameID").val(),$("#jqgFirstNameID").val(), $("#jqgMiddleIniID").val(), $("#jqgGradeID").val(),$("#jqgOrgID").val(), $("#jqgGenderID").val(), $("#jqgAccoID").val(), $("#jqgLoginID").val(), studentIdTitle, 'orgId'],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:110, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:110, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleInitial',index:'middleInitial', width:30, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:70, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeName',index:'orgNodeNamesStr', width:110, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'gender',index:'gender', width:75, editable: true, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations', width:120, editable: true, align:"left", sortable:true, title:false,
		   							cellattr: function (rowId, tv, rawObject, cm, rdata) {
		   									var returnStr = '';
		   									if(rawObject.hasAccommodations == 'Yes'){
		   										returnStr = 'style="white-space: normal;" onmouseover="showAccoToolTip('+rowId+',event);" onmouseout="hideAccoToolTipPopUp();"' ; 
		   									} else { returnStr = 'style="white-space: normal;"' ;}  
		   									return returnStr;
		   							}},
		   		{name:'userName',index:'userName',editable: true, width:160, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentNumber',index:'studentNumber',editable: true, width:140, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeId',index:'orgNodeId', width:0, hidden: true, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   		
		   	],
		   		jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",
		   	records: function(obj) { 
		   	 	allStudentInGrid = obj.studentProfileInformation;
		   	 	accomodationMap = obj.accomodationMap;
		   	 } },
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:true,
			pager: '#studentBulkMovePager', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 390, 
			width: $("#jqGrid-content-section").width(), 
			caption:$("#moveStuGrid").val(),
			onPaging: function() {
				$("#displayMessageMain").hide();
				var reqestedPage = parseInt($('#studentBulkMoveGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_studentBulkMovePager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#studentBulkMoveGrid').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#studentBulkMoveGrid').setGridParam({"page": minPageSize});
				}
			},
			gridComplete: function() {
			var gridreloadRowCount = bulkMoveStuCounterPage;
			var allRowsInGrid = $('#studentBulkMoveGrid').jqGrid('getDataIDs');
			 	$('.cbox').attr('checked', false); 
			 	for(var i=0; i<allRowsInGrid.length; i++) {
			 		if(selectedStudentForMove[allRowsInGrid[i]] != undefined) {
					 	$("#"+selectedStudentForMove[allRowsInGrid[i]]+" td input").attr('checked', true).trigger('click').attr('checked', true);
					 	$("#"+selectedStudentForMove[allRowsInGrid[i]]).addClass("ui-state-highlight").attr({
			               "aria-selected": true,
			               tabindex: "0"
			           });
			        } else {
		           		$("#"+selectedStudentForMove[allRowsInGrid[i]]+" td input").attr('checked', false).trigger('click').attr('checked', false);
		           }
		        }
				bulkMoveStuCounterPage = gridreloadRowCount;
				if(onNodechange || applyChange){
					bulkMoveStuCounterPage = 0;
					for(var i=0; i<allStudentInGrid.length; i++) {
						if (selectedStudentForMove[allStudentInGrid[i].studentId] != undefined)
							bulkMoveStuCounterPage = bulkMoveStuCounterPage + 1;
					}
					 if(onNodechange){
						 onNodeChangeEmptyMsg = true;	
					 }
					 applyChange = false;
					//onNodechange = false;
				}
				determineStudentSel(selectedStudentForMove, 'bulkMoveButton');
				
				 if(bulkMoveStuCounterPage == allStudentInGrid.length ) {
				 	$('#cb_studentBulkMoveGrid').attr('checked', true);
				 } else {
				 	$('#cb_studentBulkMoveGrid').attr('checked', false);
				 }
				if(allRowsInGrid.length <= 0)
					$('#cb_studentBulkMoveGrid').attr('checked', false);
			},
			onSelectAll: function (rowIds, status) {
					$("#displayMessageMain").hide();
					if(!status) {
						UIBlock();
						for(var i=0; i<allStudentInGrid.length; i++){
							delete selectedStudentForMove[allStudentInGrid[i].studentId];
						}
						bulkMoveStuCounterPage = 0;
						$.unblockUI();  
					} else {
						UIBlock();
						for(var i=0; i<allStudentInGrid.length; i++){
							selectedStudentForMove[parseInt(allStudentInGrid[i].studentId)] = parseInt(allStudentInGrid[i].studentId);
						}
						bulkMoveStuCounterPage = allStudentInGrid.length;
						$.unblockUI();
						//The below condition is used to stop select all in grid if the grid is empty
						if(allStudentInGrid == undefined || allStudentInGrid.length <=0) {
							var noStudents = $('.ui-state-highlight');
							if(noStudents.length > 0) {
								for(var k = 0; k < noStudents.length; k++) {
									$(noStudents[k]).removeClass('ui-state-highlight');
								}
							}
						}
					}
					determineStudentSel(selectedStudentForMove, 'bulkMoveButton');
			},
			onSelectRow: function (rowid, status) {
				$("#displayMessageMain").hide();
				var selectedRowId = rowid;
				var isRowSelected = $("#studentBulkMoveGrid").jqGrid('getGridParam', 'selrow');
				if(status) {
					bulkMoveStuCounterPage = bulkMoveStuCounterPage+1;
					selectedStudentForMove[selectedRowId]= selectedRowId;
				} else {
					bulkMoveStuCounterPage = bulkMoveStuCounterPage-1;
					 delete selectedStudentForMove[selectedRowId]; 
				}
				
				if(bulkMoveStuCounterPage == allStudentInGrid.length ) {
				 	$('#cb_studentBulkMoveGrid').attr('checked', true);
				 } else {
				 	$('#cb_studentBulkMoveGrid').attr('checked', false);
				 }
				 determineStudentSel(selectedStudentForMove, 'bulkMoveButton');
			},
			loadComplete: function () {
				var isSAGridEmpty = false;
				if ($('#studentBulkMoveGrid').getGridParam('records') === 0) {
					isSAGridEmpty = true;
            		$('#sp_1_studentBulkMovePager').text("1");
            		$('#next_studentBulkMovePager').addClass('ui-state-disabled');
            	 	$('#last_studentBulkMovePager').addClass('ui-state-disabled');
            	} else {
            		isSAGridEmpty = false;
            	}
            	if(onNodechange){
            		onNodeChangeEmptyMsg = false;
            		onNodechange = false;
            	}
            	if(isSAGridEmpty) {
            		$('#studentBulkMoveGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#studentBulkMoveGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/StudentWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsg").val()+"</td></tr></tbody></table></td></tr>");
            	}
            	$.unblockUI();
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 
}


function openBulkMovePopup(element) {
	if (isButtonDisabled(element))
		return true;
		
	removeBulkPopupMessage();
	if($("#innerID ul li") == undefined || $("#innerID ul li").length <= 0) {
		createMultiNodeBulkMoveTree(orgTreeHierarchy);
	}
	finalSelectedNode = undefined;
	isPopUp = true;
	$("#moveStudentPopup").dialog({  
		title:$("#moveStuDialogTitle").val(),  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '480px',
	 	modal: true,
	 	closeOnEscape: false,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 $("#moveStudentPopup").css('height',390);
		 var toppos = ($(window).height() - 475) /2 + 'px';
		 var leftpos = ($(window).width() - 550) /2 + 'px';
		 $("#moveStudentPopup").parent().css("top",toppos);
		 $("#moveStudentPopup").parent().css("left",leftpos);
		 $("#displayMessageMain").hide();	
  }



function createMultiNodeBulkMoveTree(jsondata) {
	var styleClass;
	
	$("#innerID").jstree({
        "json_data" : {	             
            "data" : rootNode,
			"progressive_render" : true,
			"progressive_unload" : true
        },
        "ui" : {
        	"select_limit" : 1
        },
        "checkbox" : {
        "two_state" : true
        }, 
        	
			"themes" : {
			"theme" : "apple",
			"dots" : false,
			"icons" : false
			},         	
         	
		"plugins" : [ "themes", "json_data","ui","checkbox","crrm"]
    });
	   	$("#innerID").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel != leafNodeCategoryId) {
						$("#innerID ul li").eq(i).find('a').find('.jstree-checkbox:first').hide();
		    		} else {
		    			$("#innerID ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
			registerDelegate("innerID");
		$("#innerID").delegate("li a","click", 
			function(e) {
				styleClass = $(this.parentNode).attr('class');
				var orgcategorylevel = $(this.parentNode).attr("cid");
				if(orgcategorylevel == leafNodeCategoryId) {
					if(styleClass.indexOf("unchecked") > 0){
						$('#innerID').jstree('uncheck_all');
						$(this.parentNode).removeClass("jstree-unchecked").addClass("jstree-checked");
						$(this.parentNode).find('a').addClass("jstree-clicked");
						finalSelectedNode = $(this.parentNode).attr('id');
					}else {
						$(this.parentNode).removeClass("jstree-checked").addClass("jstree-unchecked");
						finalSelectedNode = undefined;
						$(this.parentNode).find('a').removeClass("jstree-clicked");
					}
				} else 
					return false;
			}
			);			
			
			$("#innerID").bind("change_state.jstree",
		  		function (e, d) {
			  		if(isAction){
			  			removeBulkPopupMessage();
				    	var elementId = d.rslt[0].getAttribute("id");
						var isChecked = $(d.rslt[0]).hasClass("jstree-checked");
						if (isChecked){
							finalSelectedNode = elementId;
							$(d.rslt[0]).find('a').addClass("jstree-clicked");
						} else {
							$(d.rslt[0]).find('a').removeClass("jstree-clicked");
						}
    				}
				}
			);		
}

function closeBulkMovePopup() {
	if(finalSelectedNode == undefined) {
		hideBulkMovePopup();
	} else {
		$("#unSaveBulkConfirmationPopup").dialog({  
			title:$("#confirmMoveAlert").val(),  
			resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
		    $("#unSaveBulkConfirmationPopup").css('height',120);
			var toppos = ($(window).height() - 290) /2 + 'px';
			var leftpos = ($(window).width() - 410) /2 + 'px';
			$("#unSaveBulkConfirmationPopup").parent().css("top",toppos);
			$("#unSaveBulkConfirmationPopup").parent().css("left",leftpos);
	}
}

function closeUnsaveBulkConfirmationPopup() {
	$('#innerID').jstree('close_all', -1);
	closePopUp('unSaveBulkConfirmationPopup');
	hideBulkMovePopup();
}

function hideBulkMovePopup() {
	$('#innerID').jstree('close_all', -1);
	isPopUp = false;
	$("#moveStudentPopup").dialog("close");
	removeBulkPopupMessage();	
}

function removeBulkPopupMessage() {
	$("#contentBulkMovePopup").text("");
	$("#errorImgPopup").hide();
	$('#displayBMPopupMessage').attr("style",'');
}

function saveBulkMoveData() {

	var param = {};
	var studentIds = "";
	for(var key in selectedStudentForMove){
		studentIds =  selectedStudentForMove[key] + "," + studentIds;
	}
	studentIds = studentIds.substring(0,studentIds.length-1);
	param.studentIds = studentIds;
	param.selectedOrgId = finalSelectedNode;
	if(finalSelectedNode != undefined && finalSelectedNode.length > 0) {
		$.ajax(
		{
				async:		false,//asynchronous calls do not sent post data in MAC safari
				beforeSend:	function(){
								UIBlock();
							},
				url:		'saveBulkStudentData.do',
				type:		'POST',
				data:		 param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){
									selectedStudentForMove = [];
									bulkMoveStuCounterPage = 0;
									//$("#innerID").undelegate();
									//$("#innerID").unbind();
									$('#innerID').jstree('close_all', -1);
									gridReloadForBulkMoveStudent();
									hideBulkMovePopup();
									$("#displayMessageMain").show();
									$("#contentMain").text($("#stuBulkMovedID").val());
									setAnchorButtonState('bulkMoveButton', true);
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
									$.unblockUI();  
								window.location.href="/SessionWeb/logout.do";
							},
				complete :  function(){
								//$.unblockUI();  
							}
		});
	} else {
		setBulkPopupMessage();
		$('#displayBMPopupMessage').attr("style",'border:2px solid #D4ECFF');
		
	}
}

function setBulkPopupMessage(){
	$("#contentBulkMovePopup").text($("#noBulkMoveID").val());
	$("#errorImgPopup").show();
}

function determineStudentSel(selectedStdArry, buttonId) { // Added for defect #67829
	var present = false;
	for(var key in selectedStdArry) {
		present = true;
		break;
	}
	if(present) {
		setAnchorButtonState(buttonId, false);
	} else {
		setAnchorButtonState(buttonId, true);
	}
}