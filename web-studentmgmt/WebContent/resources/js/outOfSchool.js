
var oosStudentGridLoaded = false;
var oosStuCounterPage = 0;
var selectedStudentForOOSIds = [];



function populateOutOfSchoolTree() {
	isBulkMove = true;
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'oosOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI(); 
						leafNodeCategoryId = data.leafNodeCategoryId;
						orgTreeHierarchy = data;
						jsonData = orgTreeHierarchy.data;
						getRootNodeDetails();
						createSingleNodeOOSTree(orgTreeHierarchy);
						$("#searchheader").css("visibility","visible");	
						$("#outOfSchoolOrgNode").css("visibility","visible");	
												
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



function createSingleNodeOOSTree(jsondata) {
	   $("#outOfSchoolOrgNode").jstree({
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
	    
	    $("#outOfSchoolOrgNode").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#outOfSchoolOrgNode ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
	    
	    $("#outOfSchoolOrgNode").delegate("a","click", function(e) {
	    	submittedSuccesfully = "";
	    	$("#displayMessageMain").hide();
	    	determineStudentSel(selectedStudentForOOSIds, 'OOSButton');
			SelectedOrgNodeId = $(this).parent().attr("id");
			var topNodeSelected = $(this).parent().attr("cid");
			if(topNodeSelected == leafNodeCategoryId || topNodeSelected == (leafNodeCategoryId -1)) {
 		    $("#outOfSchoolOrgNode").val(SelectedOrgNodeId);
 		     UIBlock();
 		  	if(!oosStudentGridLoaded) {
 		  		oosStudentGridLoaded = true;
 		  			populateOOSStudentGrid();
 		  		}
			else
				gridReloadForOOSStudent();
			}
			if(oosStudentGridLoaded) {
				 document.getElementById('viewStatus').style.visibility = "visible";
			}
			totalRowSelectedOnPage = 0;
	    	onNodechange = true;
	    });
		
	registerDelegate("outOfSchoolOrgNode");		
	   
}



function populateOOSStudentGrid() {
 		UIBlock();
 		oosStudentGridLoaded = true;
 		var studentIdTitle = $("#studentIdLabelName").val();
 		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.stuForOrgNodeId = $("#outOfSchoolOrgNode").val();

       $("#outOfSchoolGrid").jqGrid({         
          url: 'getStudentForSelectedNode.do',
		  mtype:   'POST',
		  datatype: "json",
		  postData: postDataObject, 
          colNames:[$("#jqgLastNameID").val(),$("#jqgFirstNameID").val(), $("#jqgMiddleIniID").val(), $("#jqgGradeID").val(),$("#jqgOrgID").val(), $("#jqgGenderID").val(), $("#jqgAccoID").val(), $("#jqgLoginID").val(), studentIdTitle, 'orgId', $("#oosID").val()],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:100, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:100, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:100, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:70, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeName',index:'orgNodeNamesStr', width:110, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'gender',index:'gender', width:80, editable: true, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations', width:125, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userName',index:'userName',editable: true, width:150, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentNumber',index:'studentNumber',editable: true, width:100, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeId',index:'orgNodeId', width:0, hidden: true, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'outOfSchool',index:'outOfSchool', width:100, editable: true, align:"left",sorttype:'text', sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   		
		   	],
		   		jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",
		   	records: function(obj) { 
		   	 	allStudentInGrid = obj.studentProfileInformation;
		   	 } },
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:true,
			pager: '#outOfSchoolPager', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 390, 
			width: $("#jqGrid-content-section").width(), 
			caption:$("#moveStuGrid").val(),
			onPaging: function() {
				$("#displayMessageMain").hide();
				var reqestedPage = parseInt($('#outOfSchoolGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_outOfSchoolPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#outOfSchoolGrid').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#outOfSchoolGrid').setGridParam({"page": minPageSize});
				}
			},
			gridComplete: function() {
			var gridreloadRowCount = oosStuCounterPage;
			var allRowsInGrid = $('#outOfSchoolGrid').jqGrid('getDataIDs');
			 	$('.cbox').attr('checked', false); 
			 	for(var i=0; i<allRowsInGrid.length; i++) {
			 		if(selectedStudentForOOSIds[allRowsInGrid[i]] != undefined) {
					 	$("#"+selectedStudentForOOSIds[allRowsInGrid[i]]+" td input").attr('checked', true).trigger('click').attr('checked', true);
					 	$("#"+selectedStudentForOOSIds[allRowsInGrid[i]]).addClass("ui-state-highlight").attr({
			               "aria-selected": true,
			               tabindex: "0"
			           });
			        } else {
		           		$("#"+selectedStudentForOOSIds[allRowsInGrid[i]]+" td input").attr('checked', false).trigger('click').attr('checked', false);
		           }
		        }
				oosStuCounterPage = gridreloadRowCount;
				if(onNodechange || applyChange){
					oosStuCounterPage = 0;
					for(var i=0; i<allStudentInGrid.length; i++) {
						if (selectedStudentForOOSIds[allStudentInGrid[i].studentId] != undefined)
							oosStuCounterPage = oosStuCounterPage + 1;
					}
					 if(onNodechange){
						 onNodeChangeEmptyMsg = true;	
					 }
					 applyChange = false;
					//onNodechange = false;
				}
				determineStudentSel(selectedStudentForOOSIds, 'OOSButton');
				
				 if(oosStuCounterPage == allStudentInGrid.length ) {
				 	$('#cb_outOfSchoolGrid').attr('checked', true);
				 } else {
				 	$('#cb_outOfSchoolGrid').attr('checked', false);
				 }
				if(allRowsInGrid.length <= 0)
					$('#cb_outOfSchoolGrid').attr('checked', false);
			},
			onSelectAll: function (rowIds, status) {
					$("#displayMessageMain").hide();
					if(!status) {
						UIBlock();
						for(var i=0; i<allStudentInGrid.length; i++){
							delete selectedStudentForOOSIds[allStudentInGrid[i].studentId];
						}
						oosStuCounterPage = 0;
						$.unblockUI();  
					} else {
						UIBlock();
						for(var i=0; i<allStudentInGrid.length; i++){
							selectedStudentForOOSIds[parseInt(allStudentInGrid[i].studentId)] = parseInt(allStudentInGrid[i].studentId);
						}
						oosStuCounterPage = allStudentInGrid.length;
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
					determineStudentSel(selectedStudentForOOSIds, 'OOSButton');
			},
			onSelectRow: function (rowid, status) {
				$("#displayMessageMain").hide();
				var selectedRowId = rowid;
				var isRowSelected = $("#outOfSchoolGrid").jqGrid('getGridParam', 'selrow');
				if(status) {
					oosStuCounterPage = oosStuCounterPage+1;
					selectedStudentForOOSIds[selectedRowId] = selectedRowId;
				} else {
					oosStuCounterPage = oosStuCounterPage-1;
					 delete selectedStudentForOOSIds[selectedRowId]; 
				}
				
				if(oosStuCounterPage == allStudentInGrid.length ) {
				 	$('#cb_outOfSchoolGrid').attr('checked', true);
				 } else {
				 	$('#cb_outOfSchoolGrid').attr('checked', false);
				 }
				 determineStudentSel(selectedStudentForOOSIds, 'OOSButton');
			},
			loadComplete: function () {
				var isSAGridEmpty = false;
				if ($('#outOfSchoolGrid').getGridParam('records') === 0) {
					isSAGridEmpty = true;
            		$('#sp_1_outOfSchoolPager').text("1");
            		$('#next_outOfSchoolPager').addClass('ui-state-disabled');
            	 	$('#last_outOfSchoolPager').addClass('ui-state-disabled');
            	} else {
            		isSAGridEmpty = false;
            	}
            	if(onNodechange){
            		onNodeChangeEmptyMsg = false;
            		onNodechange = false;
            	}
            	if(isSAGridEmpty) {
            		$('#outOfSchoolGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#outOfSchoolGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/StudentWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsg").val()+"</td></tr></tbody></table></td></tr>");
            	}
            	$.unblockUI();
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 
}

function gridReloadForOOSStudent(){
		UIBlock();
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.stuForOrgNodeId = $("#outOfSchoolOrgNode").val();
       jQuery("#outOfSchoolGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
       jQuery("#outOfSchoolGrid").jqGrid('setGridParam', {url:'getStudentForSelectedNode.do',postData: postDataObject,page:1}).trigger("reloadGrid");
       jQuery("#outOfSchoolGrid").sortGrid('lastName',true,'asc');
      
  	}

function outOfSchoolOperation(element) {
	if (isButtonDisabled(element))
		return true;
	var finalDataOOS = "";
	for(var key in selectedStudentForOOSIds) {
		finalDataOOS = finalDataOOS + selectedStudentForOOSIds[key]+",";
	}
	finalDataOOS = finalDataOOS.substring(0,finalDataOOS.length-1);
	var param = "oosSelected="+finalDataOOS;
	
	$.ajax(
		{
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'updateOutOfSchoolData.do',
				type:		'POST',
				data:		 param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){
									if(data.successFlag) {
										selectedStudentForOOSIds = [];
										oosStuCounterPage = 0;
										gridReloadForOOSStudent();
										$("#displayMessageMain").show();
										$("#infoIcon").show();
										$("#errorIcon").hide();
										$("#contentMain").text($("#oosToggleSuccess").val());
										setAnchorButtonState('OOSButton', true);
									} else {
										$("#displayMessageMain").show();
										$("#errorIcon").show();
										$("#infoIcon").hide();
										$("#contentMain").text($("#oosFailed").val());
									}
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
									$.unblockUI();  
								window.location.href="/SessionWeb/logout.do";
							},
				complete :  function(){
								//$.unblockUI();  
							}
		});
	
	
}