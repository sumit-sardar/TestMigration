


function populateSessionGridInPopupForFR(){
		resetSearchCritInPopupFromStudentView();
 		resetFiltersInPopupFromStudentView();
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.treeOrgNodeId     = $("#treeOrgNodeId").val();
 		postDataObject.selectedProductId = $("#recommendedProductId").val();
        $("#list2").jqGrid({         
          url:'getRecommendedSessionForReportingGrid.do', 
		 mtype:   'POST',
		 postData: postDataObject,
		 datatype: "json",         
          colNames:[$("#grdSessionName").val(),$("#grdTestName").val(), $("#sesGridStatus").val(), $("#sesGridStartDate").val(), $("#sesGridEndDate").val(), ''],
		   	colModel:[
		   		{name:'testAdminName',index:'testAdminName', width:160, editable: true, align:"left",sorttype:'text',search: false,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'testName',index:'testName', width:160, editable: true, align:"left",sorttype:'text',search: true,sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: testNameOptions } },
		   		{name:'testAdminStatus',index:'testAdminStatus', width:80, editable: true, align:"left",search: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: statusOptions } },
		   		{name:'loginStartDateString',index:'loginStartDateString',editable: true, width:80, align:"left",search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginEndDateString',index:'loginEndDateString',editable: true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   	    {name:'itemSetId',index:'itemSetId',editable: true, hidden:true, width:80, align:"left",search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }}
		   	],
		   	jsonReader: { repeatitems : false, root:"testSession", id:"testAdminId",
		   	records: function(obj) {} },
		   	
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'testAdminName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: 760, 
			editurl: 'getRecommendedSessionForReportingGrid.do',
			ondblClickRow: function(rowid) {/*populateGridAsPerView();*/},
			caption:$("#sesGridCaption").val(),
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
			onSortCol : function(index, columnIndex, sortOrder) { 
				
				disableButton('nextButtonStdPopup');
			},
			onSelectRow: function (rowId) {
					enableButton('nextButtonStdPopup');
					$("#displayMessageMain").hide();
					var selectedRowData = $("#list2").getRowData(rowId);
					selectedTestAdminName = selectedRowData.testAdminName;
					selectedTestAdminId = rowId;
					selectedItemSetIdTC = selectedRowData.itemSetId;
				},
			loadComplete: function () {
				if ($('#list2').getGridParam('records') === 0) {
            		$('#sp_1_pager2').text("1");
            		$('#next_pager2').addClass('ui-state-disabled');
            		$('#last_pager2').addClass('ui-state-disabled');
            		$('#list2').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#list2').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/ScoringWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noSessionTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noSessionMessage").val()+"</td></tr></tbody></table></td></tr>");
            	}
				$.unblockUI();  
				$("#list2").setGridParam({datatype:'local'});
				var tdList = ("#pager2_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				$("#pager2 .ui-pg-input").attr("style", "position: relative; z-index: 100000;");
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#list2").jqGrid('filterToolbar',{
	 	afterSearch: function(){ 
	 		searchStudentSessionByKeyword();
	 		setAnchorButtonState('registerButton', true);
	 	}
	 });
	 	jQuery("#list2").navGrid('#pager2',{
				search: false,add:false,edit:false,del:false 	
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchStudentSessionByKeyword").dialog({  
						title:$("#searchStudentSession").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
					 	});
			    }, position: "one-before-last", title:"Search Session", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#pager2",{position: "first"
			});
			
			jQuery(".ui-icon-refresh").bind("click",function(){
				$("#searchStudentSessionByKeywordInput").val('');
				setAnchorButtonState('registerButton', true);
			}); 
	 
	}
	
	
	function reloadSessionGridFromStdOnFR(){
		//resetSearchCrit();
		resetSearchCritInPopupFromStudentView();
 		resetFiltersInPopupFromStudentView();
  	    var postDataObject = {};
		postDataObject.q = 2;
		postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
			
        jQuery("#list2").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});     
  	   //var sortArrow = jQuery("#list2");
        jQuery("#list2").jqGrid('setGridParam', {url:'getRecommendedSessionForReportingGrid.do',postData:postDataObject,page:1}).trigger("reloadGrid");
        jQuery("#list2").sortGrid($("#grdSessionName").val(),true,'asc');
	}