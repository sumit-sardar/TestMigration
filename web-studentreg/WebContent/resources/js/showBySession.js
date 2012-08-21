var gradeOptionsInSessionPopup = ":Any;AD:AD;JV:JV";
var genderOptionsInSessionPopup = ":Any;Male:Male;Female:Female;Unknown:Unknown";
var selectedStudentId ;

function createSingleNodeSelectionTreeForStudent(jsondata) {
 		$("#innerOrgNodeHierarchyForStd").jstree({
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
	    $("#innerOrgNodeHierarchyForStd").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#innerOrgNodeHierarchyForStd ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
	    $("#innerOrgNodeHierarchyForStd").delegate("a","click", function(e) {

	    	//clear message before moving to pther node
	    	clearMessage();
  			selectedOrgNodeIdInPopup = $(this).parent().attr("id");
 		    $("#treeOrgNodeIdInPopup").val(selectedOrgNodeIdInPopup);
            stuForSelectedOrg = $(this).parent().attr("id");
            var topNodeSelected = $(this).parent().attr("cid");
 		   /* 
	 		if(parseInt(rootNodeId) == parseInt(selectedOrgNodeIdInPopup)){
	 			var postDataObject = {};
	 			postDataObject.treeOrgNodeId = $("#treeOrgNodeIdInPopup").val();
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
										showGrid();
									}
								},
					error  :    function(XMLHttpRequest, textStatus, errorThrown){
									$.unblockUI();  
									window.location.href="/SessionWeb/logout.do";
									
								}
				});		
	 		 }else{
	 		    showGrid();
	 		} 	*/	
	 		//if(topNodeSelected == String(leafNodeCategoryId)) {
	 		    UIBlock();
	 		    
		 		

	 			if(currentView == "student"){
		 			if(!gridloadedSessionFromStd){
		 				if(isPopupOnFRNotAccept || isPopupOnByepassFR)
		 					populateSessionGridInPopup();
						else if(isPopupOnFRAccept)
							populateSessionGridInPopupForFR();		 					
		 				gridloadedSessionFromStd = true;
		 			} else {
		 				if(isPopupOnFRNotAccept || isPopupOnByepassFR)
		 					reloadSessionGridFromStd();
						else if(isPopupOnFRAccept)
							reloadSessionGridFromStdOnFR();		 					
		 			}
	 			} else {
	 				if(!gridloadedStdFromSes){
	 				    setAnchorButtonState('registerButton', false);
	 				    if(isPopupOnFRAccept){
	 				    	populateSessionGridInPopupForFR();
	 				    } else {
	 				    	populateStudentGridInPopup();
	 				    }
		 				
		 				gridloadedStdFromSes = true;
			 		} else {
			 			 if(isPopupOnFRAccept){
			 			 	reloadSessionGridFromStdOnFR();	
			 			 } else {
			 			 	reloadStudentGridFromSes();
			 			 }
			 			
			 		}
	 			}

	 		//} 
	 		
		});
		
		$("#outerTreeForStudentFromSessionDiv").height(510);
		
	registerDelegate("innerOrgNodeHierarchyForStd");	
	
}




	function populateStudentGridInPopup() {

		disableButton('nextButtonStdPopup');

		resetSearchCrit();
		resetFiltersInSessionPopup();
		
		var studentIdTitle = $("#studentIdLabelName").val();
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.treeOrgNodeId = $("#treeOrgNodeIdInPopup").val();
 		postDataObject.testAdminId = selectedTestAdminId;
        $("#list2").jqGrid({         
          url:'getStudentForSelectedOrgNodeGrid.do', 
		 mtype:   'POST',
		 postData: postDataObject,
		 datatype: "json",         
          colNames:[$("#stuGrdLoginId").val(), $("#stuGrdStdName").val(),$("#grdGroup").val(), $("#stuGrdGrade").val(), $("#stuGrdGender").val(), studentIdTitle,"","",""],
		   	colModel:[
		   		{name:'userName',index:'userName',editable: true, width:130, align:"left", sortable:true, search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentName',index:'studentName', width:130, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr', width:140, editable: true, align:"left", search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:50, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } , stype: 'select', searchoptions:{ sopt:['eq'], value: gradeOptionsInSessionPopup } },
		   		{name:'gender',index:'gender', width:50, editable: true, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } , stype: 'select', searchoptions:{ sopt:['eq'], value: genderOptionsInSessionPopup } },
		   		{name:'studentNumber',index:'studentNumber',editable: true, width:110, align:"left", search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'isSessionStudent',index:'isSessionStudent',hidden: true, editable: false, width:5, align:"left", sortable:false,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeIdList',index:'orgNodeIdList',hidden: true, editable: false, width:5, align:"left", sortable:false,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesList',index:'orgNodeNamesList',hidden: true, editable: false, width:5, align:"left", sortable:false,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",
		   	records: function(obj) { 
		   //	studentList = obj.studentProfileInformation;
		   	//idarray = obj.studentIdArray.split(",");
		   	//accomodationMap = obj.accomodationMap;
		   	return obj.studentProfileInformation.length; } },
		   	
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: $("#stuGrdLoginId").val(), 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: 760, 
			editurl: 'getStudentForSelectedOrgNodeGrid.do',
			caption:$("#stuGridCaption").val(),
			onPaging: function() {
				clearMessage();
				var reqestedPage = parseInt($('#list2').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_pager2').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#list2').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#list2').setGridParam({"page": minPageSize});
				}
				disableButton('nextButtonStdPopup');
			},
			onSelectRow: function (rowId) {
				clearMessage();
				enableButton('nextButtonStdPopup');
				var selectedRData = $("#list2").getRowData(rowId);
				selectedStudentId = rowId;
				selectedStudentNameFromSessionPopup = selectedRData.studentName;
				selectedStudentOrgNodeName = selectedRData.orgNodeNamesList;
				selectedStudentOrgNodeid   = selectedRData.orgNodeIdList;
				if(selectedRData.isSessionStudent=='T'){
					disableButton('nextButtonStdPopup');
				}else {
					enableButton('nextButtonStdPopup');
				}
			},
			gridComplete: function() {
				var allRowsInGridPresent = $('#list2').jqGrid('getDataIDs');
				for(var k = 0; k < allRowsInGridPresent.length; k++) {
					var selectedRowData = $("#list2").getRowData(allRowsInGridPresent[k]);
					if(selectedRowData.isSessionStudent=='T'){
						//$("#"+(k+1)+" td input","#list2").attr("disabled", true);
				 		$("#"+(k+1), "#list2").addClass('ui-state-disabled');
					}			
				}
			},
			onSortCol: function() {
 				disableButton('nextButtonStdPopup');
			},			
			loadComplete: function () {
				if ($('#list2').getGridParam('records') === 0) {
            		$('#sp_1_pager2').text("1");
            		$('#next_pager2').addClass('ui-state-disabled');
            		$('#last_pager2').addClass('ui-state-disabled');
            		$('#list2').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#list2').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/StudentWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsg").val()+"</td></tr></tbody></table></td></tr>");
            	}
				$.unblockUI();  
				$("#list2").setGridParam({datatype:'local'});
				var tdList = ("#pager2_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				$("#pager2 .ui-pg-input").attr("style", "position: relative; z-index: 100000;");
				if (document.getElementById('registerStudentDiv') != null) 
					document.getElementById('registerStudentDiv').style.visibility = "visible";
				disableButton('nextButtonStdPopup');
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
			
		
			
		// added on 30.07.2012
			jQuery("#list2").jqGrid('filterToolbar',{
		 	afterSearch: function(){ 
		 		searchInSessionPopupByKeyword();
		 		setAnchorButtonState('registerButton', true);
		 	}});
		 	
			jQuery("#list2").navGrid('#pager2',{
				search: false,add:false,edit:false,del:false 	
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchInSessionPopupByKeyword").dialog({  
						title:$("#searchStudentSession").val(), 
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
					 	});
			    }, position: "one-before-last", title:"Search Student", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#pager2",{position: "first"
			});
			
			jQuery(".ui-icon-refresh").bind("click",function(){
				$("#searchInSessionPopupByKeywordInput").val('');
				setAnchorButtonState('registerButton', true);
			}); 
		//
			
		
					
	}
	
	
	
	 function reloadStudentGridFromSes(){ 
	 	resetSearchCrit();
	 	resetFiltersInSessionPopup();
  	    var postDataObject = {};
		postDataObject.q = 2;
		postDataObject.treeOrgNodeId = $("#treeOrgNodeIdInPopup").val();
		postDataObject.testAdminId = selectedTestAdminId;
			
        jQuery("#list2").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});     
  	   //var sortArrow = jQuery("#list2");
        jQuery("#list2").jqGrid('setGridParam', {url:'getStudentForSelectedOrgNodeGrid.do',postData:postDataObject,page:1}).trigger("reloadGrid");
        jQuery("#list2").sortGrid($("#stuGrdLoginId").val(),true,'asc');

      }
      
	function trapEnterKeyInSessionPopup(e){
		var key;
		if(window.event)
			key = window.event.keyCode;     //IE
		else
			key = e.which;     //firefox
		        
			if(key == 13){
				searchInSessionPopupByKeyword();
		   }
	} 
	
	function resetSearchInSessionPopup(){		
			 var grid = $("#list2"); 
			 $("#searchInSessionPopupByKeywordInput").val('');
			 //grid[0].p.search = false;// uncommented to test search only w/o filters
			 var g = {groupOp:"AND",rules:[]};
			 g.rules.push({field:"gender",op:"bw",data:$("#gview_list2 select[id=gs_gender]").val()});
			 g.rules.push({field:"grade",op:"bw",data:$("#gview_list2 select[id=gs_grade]").val()});
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;			 
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
			 grid.trigger("reloadGrid",[{page:1,current:true}]); 
			 closePopUp('searchInSessionPopupByKeyword');
			 //grid[0].triggerToolbar();// to trigger previously applied filters
	}
	
	function searchInSessionPopupByKeyword(){
	
		 var searchFiler = $.trim($("#searchInSessionPopupByKeywordInput").val()), f;
		 var grid = $("#list2"); 
		 
		 if (searchFiler.length === 0) {
			 //grid[0].p.search = false; // uncommented to test search only w/o filters
			 //grid[0].triggerToolbar();// to trigger previously applied filters
			 var g = {groupOp:"AND",rules:[],groups:[]};
			 g.rules.push({field:"grade",op:"bw",data:$("#gview_list2 select[id=gs_grade]").val()});
			 g.rules.push({field:"gender",op:"bw",data:$("#gview_list2 select[id=gs_gender]").val()});			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;			 
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
		 }else {
		 	 var g = {groupOp:"AND",rules:[],groups:[]};
			 g.rules.push({field:"grade",op:"bw",data:$("#gview_list2 select[id=gs_grade]").val()});
			 g.rules.push({field:"gender",op:"bw",data:$("#gview_list2 select[id=gs_gender]").val()});
			 			 
		 	 f = {groupOp:"OR",rules:[]};
			 f.rules.push({field:"userName",op:"cn",data:searchFiler});
			 f.rules.push({field:"studentName",op:"cn",data:searchFiler});
			 f.rules.push({field:"orgNodeNamesStr",op:"cn",data:searchFiler});
			 f.rules.push({field:"grade",op:"eq",data:searchFiler});
			 f.rules.push({field:"gender",op:"eq",data:searchFiler});
			 f.rules.push({field:"studentNumber",op:"cn",data:searchFiler});			 
			 g.groups.push(f); // comment 0n 30.07.2012			 
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;			 
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(g)});
		 }
		 grid.trigger("reloadGrid",[{page:1,current:true}]);
		 closePopUp('searchInSessionPopupByKeyword');	
	}  
	
	function resetFiltersInSessionPopup() { // need to call this to reset popup filters 
		$("#gview_list2 select[id=gs_grade] option:eq(0)").attr('selected','Any'); 
		$("#gview_list2 select[id=gs_gender] option:eq(0)").attr('selected','Any');
	}  
	
	function resetSearchCrit(){
		$("#searchInSessionPopupByKeywordInput").val('');
		var grid = $("#list2"); 
		grid.jqGrid('setGridParam',{search:false});	
	    var postData = grid.jqGrid('getGridParam','postData');
	    $.extend(postData,{filters:""});
	}
	
	
	function enableButton(elementid) {
	    var element = $("#"+elementid);
		element.removeClass('ui-state-disabled');
		element.addClass('ui-widget-header');
		element.attr('disabled', false);
	
	}
	
	function disableButton(elementid) {
		var element = $("#"+elementid);
		element.removeClass('ui-widget-header');
		element.addClass('ui-state-disabled');
		element.attr('disabled', 'disabled');
	
	}