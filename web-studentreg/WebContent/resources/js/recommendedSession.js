


function populateSessionGridInPopupForFR(){
		resetSearchCritInSecondaryDiv();
		resetFiltersInSecondaryDiv();
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.treeOrgNodeId     = $("#treeOrgNodeIdInSecondaryDiv").val();
 		postDataObject.selectedProductId = $("#recommendedProductId").val();
 		postDataObject.studentId = selectedStudentId;
 		disableButton('nextButtonStdPopup');
        $("#list3").jqGrid({         
          url:'getRecommendedSessionForReportingGrid.do', 
		 mtype:   'POST',
		 postData: postDataObject,
		 datatype: "json",         
          colNames:[$("#grdSessionName").val(),$("#grdTestName").val(), $("#grdGroup").val(), $("#sesGridMyRole").val() , $("#sesGridStatus").val(), $("#sesGridStartDate").val(), $("#sesGridEndDate").val(), '', '', '', '',''],
		   	colModel:[
		   		{name:'testAdminName',index:'testAdminName', width:160, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testName',index:'testName', width:160, editable: true, align:"left",sorttype:'text',search: true,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: testNameOptions } },
		   		{name:'creatorOrgNodeName',index:'creatorOrgNodeName', width:100, editable: true, align:"left",sorttype:'text',search: false,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
				{name:'AssignedRole',index:'AssignedRole',editable: true, width:60, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: myRoleOptions } },
		   		{name:'testAdminStatus',index:'testAdminStatus', width:80, editable: true, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: statusOptions } },
		   		{name:'loginStartDateString',index:'loginStartDateString',editable: true, width:80, align:"left",search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginEndDateString',index:'loginEndDateString',editable: true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'productType',index:'productType',editable: true, hidden:true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }},
		   	    {name:'isTabeProduct',index:'isTabeProduct',editable: true, hidden:true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }},
		   	    {name:'isTabeAdaptiveProduct',index:'isTabeAdaptiveProduct',editable: true, hidden:true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }},
		   	    {name:'itemSetId',index:'itemSetId',editable: true, hidden:true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }},
		   	    {name:'isStudentsSession',index:'isStudentsSession',editable: true, hidden:true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }}
		   	],
		   	jsonReader: { repeatitems : false, root:"testSession", id:"testAdminId",
		   	records: function(obj) {} },
		   	
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager3', 
			sortname: 'testAdminName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: 760, 
			editurl: 'getRecommendedSessionForReportingGrid.do',
			ondblClickRow: function(rowid) {/*populateGridAsPerView();*/},
			caption:$("#sesGridCaption").val(),
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
			onSortCol : function(index, columnIndex, sortOrder) { 
				
				disableButton('nextButtonStdPopup');
			},
			onSelectRow: function (rowId) {
					$("#displayMessageMain").hide();
					var selectedRowData = $("#list3").getRowData(rowId);
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
					
					if(selectedRowData.isStudentsSession=="T"){
						disableButton('nextButtonStdPopup');
					} else{
						enableButton('nextButtonStdPopup');
					} 

				},
			loadComplete: function () {
				disableButton('nextButtonStdPopup');
				$("#list3").jqGrid('resetSelection');
				if ($('#list3').getGridParam('records') === 0) {
            		$('#sp_1_pager3').text("1");
            		$('#next_pager3').addClass('ui-state-disabled');
            		$('#last_pager3').addClass('ui-state-disabled');
            		$('#list3').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#list3').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/ScoringWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noSessionTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noSessionMessage").val()+"</td></tr></tbody></table></td></tr>");
            	}
				$.unblockUI();  
				$("#list3").setGridParam({datatype:'local'});
				var tdList = ("#pager3_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				$("#pager3 .ui-pg-input").attr("style", "position: relative; z-index: 100000;");
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#list3").jqGrid('filterToolbar',{
	 	afterSearch: function(){
	 		searchByKeywordInSecondaryDiv();
	 		setAnchorButtonState('registerButton', true);
	 	}
	 });
	 	jQuery("#list3").navGrid('#pager3',{
				search: false,add:false,edit:false,del:false 	
			}).jqGrid('navButtonAdd',"#pager3",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchByKeywordInSecondaryDiv").dialog({ 
						title:$("#searchStudentSession").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide();}
					 	});
			    }, position: "one-before-last", title:"Search Session", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#pager3",{position: "first"
			});
			
			jQuery(".ui-icon-refresh").bind("click",function(){
				$("#searchByKeywordInSecondaryDivInput").val('');
				setAnchorButtonState('registerButton', true);
			}); 
	 
	}
	
	
	function reloadSessionGridFromStdOnFR(){
		//resetSearchCrit();
		resetSearchCritInSecondaryDiv();
		resetFiltersInSecondaryDiv();
  	    var postDataObject = {};
		postDataObject.q = 2;
		//postDataObject.treeOrgNodeId = $("#treeOrgNodeIdInPopup").val();
		postDataObject.treeOrgNodeId     = $("#treeOrgNodeIdInSecondaryDiv").val();// added for new changes
		postDataObject.studentId = selectedStudentId;	
        jQuery("#list3").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});     
  	   //var sortArrow = jQuery("#list3");
        jQuery("#list3").jqGrid('setGridParam', {url:'getRecommendedSessionForReportingGrid.do',postData:postDataObject,page:1}).trigger("reloadGrid");
        jQuery("#list3").sortGrid($("#grdSessionName").val(),true,'asc');
        disableButton('nextButtonStdPopup');
	}