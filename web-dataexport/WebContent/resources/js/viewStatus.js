
		
 	$(document).ready(function(){
 	 populateGridData();
          });
          
          
          function populateGridData() {
 	   	
        /*jQuery("#list2").jqGrid({
         url:'getExportStatus.do',
          
		 datatype: "json",         
         
          colNames:[$("#jqgJobID").val(),$("#jqgJobSubmissionDate").val(),$("#jqgStudentCount").val(), $("#jqgJobStatus").val()], 
          colModel:[ {name:'jobId',index:'jobId', width:55}, 
          {name:'studentCount',index:'studentCount', width:90}, 
          {name:'createdDateTime',index:'createdDateTime asc, invdate', width:100}, 
          {name:'jobStatus',index:'jobStatus', width:80}], 
          rowNum:10, 
          rowList:[10,20,30], 
          pager: '#pager2', 
          sortname: 'id',
          viewrecords: true,
          sortorder: "desc",
          caption:"Export Data:View Status" });
          jQuery("#list2").jqGrid('navGrid','#pager2',{edit:false,add:false,del:false});
          
          */
          
          
          
          
          
          
          $("#list2").jqGrid({         
         url:'getExportStatus.do', 
		 mtype:   'POST',
		 datatype: "json",         
         //postData: postDataObject,       
          colNames:[$("#jqgJobID").val(),$("#jqgJobSubmissionDate").val(),$("#jqgStudentCount").val(), $("#jqgJobStatus").val()],
		   	colModel:[
		   	
		   	    {name:'jobId',index:'jobId', width:130, editable: true,align:"left", sorttype :"number",sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
				{name:'createdDateTime',index:'createdDateTime', width:130, editable: true,align:"left",sorttype:'date',datefmt: "m/d/Y",sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentCount',index:'studentCount', width:130, editable: true,align:"left",sorttype:'number',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'jobStatus',index:'jobStatus',editable: true, width:200, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   		
		   		
		   	],
		   	jsonReader: { repeatitems : false, root:"manageJobData", id:"jobId", records: function(obj) {} },
		   	 
		   	loadui: "disable",
			rowNum:10,
			loadonce:true,
			sortable:true,
			multiselect:false,
			pager: '#pager2', 
			sortname: 'createdDateTime', 
			viewrecords: true, 
			sortorder: "desc",
			height: 155,
			width: $('#viewStstusDesc').width(), 
			editurl: 'getExportStatus.do',
			ondblClickRow: function(rowid) {},
			caption:$("#statusListID").val(),
			//caption:"Export Data:View Status",
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
			onSelectRow: function () { },
			loadComplete: function () {
				if ($('#list2').getGridParam('records') === 0) {
            		$('#sp_1_pager2').text("1");
            		$('#next_pager2').addClass('ui-state-disabled');
            		$('#last_pager2').addClass('ui-state-disabled');
            		$('#list2').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 		$('#list2').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/ExportWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentExportTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentExportMsg").val()+"</td></tr></tbody></table></td></tr>");
            	}
				$.unblockUI();
			},	 
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
			 });
				
		jQuery("#list2").jqGrid('navGrid','#pager2',{search:false,edit:false,add:false,del:false});
		jQuery("#list2").jqGrid('navButtonAdd',"#pager2",{
			    caption:"",
			    buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchUserByKeyword").dialog({  
						title:$("#searchStudentID").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide();}
					 	});
			    }, position: "one-before-last", title:"Search Student", cursor: "pointer"
			})
		        
        jQuery(".ui-icon-refresh").bind("click",function(){
				gridReload();
				$("#displayMessageMain").hide();
			});
      }
  function searchUserByKeyword(){
  	$("#displayMessageMain").hide();
	 var searchFiler = $.trim($("#searchUserByKeywordInput").val()), f;
	 var grid = $("#list2"); 
		 
		 if (searchFiler.length === 0) {
			 grid[0].p.search = false;
		 }else {
		 	 f = {groupOp:"OR",rules:[]};
			 f.rules.push({field:"jobId",op:"cn",data:searchFiler});
			 f.rules.push({field:"createdDateTime",op:"cn",data:searchFiler});
			 f.rules.push({field:"studentCount",op:"cn",data:searchFiler});
			 f.rules.push({field:"jobStatus",op:"cn",data:searchFiler}); 
			 grid[0].p.search = true;
			 grid[0].p.ignoreCase = true;
			 $.extend(grid[0].p.postData,{filters:JSON.stringify(f)});
		 }
		 grid.trigger("reloadGrid",[{page:1,current:true}]);
		 closePopUp('searchUserByKeyword');
	}
	
	function resetSearch(){
		var grid = $("#list2"); 
		$("#searchUserByKeywordInput").val('');
		 grid[0].p.search = false;
		 grid.trigger("reloadGrid",[{page:1,current:true}]); 
		 closePopUp('searchUserByKeyword');
	}
	
		function trapEnterKey(e){
		var key;
	   if(window.event)
	        key = window.event.keyCode;     //IE
	   else
	        key = e.which;     //firefox
	        
	   if(key == 13){
	   		searchUserByKeyword();
	   }
	}
	
	function closePopUp(dailogId){
	$("#"+dailogId).dialog("close");
	}
	 
	function gridReload(){ 
	  	   resetSearchCrit();
           jQuery("#list2").jqGrid('setGridParam',{datatype:'json',mtype:'POST'}); 
           jQuery("#list2").jqGrid('setGridParam', {url:'getExportStatus.do',page:1}).trigger("reloadGrid");
           jQuery("#list2").sortGrid('createdDateTime',true,'desc');

      }
      function resetSearchCrit(){
		$("#searchUserByKeywordInput").val('');
		var grid = $("#list2");
	    grid.jqGrid('setGridParam',{search:false});	
	    var postData = grid.jqGrid('getGridParam','postData');
	    $.extend(postData,{filters:""});
	    UIBlock()

	}
      
      
         	
			