

//ARIJIT & PIJUSH



		
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
		   	
		   	    {name:'jobId',index:'jobId', width:130, editable: true,align:"left",sorttype:'number',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
				{name:'createdDateTime',index:'createdDateTime', width:130, editable: true,align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentCount',index:'studentCount', width:130, editable: true,align:"left",sorttype:'number',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'jobStatus',index:'jobStatus',editable: true, width:200, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   		
		   		
		   	],
		   	jsonReader: { repeatitems : false, root:"manageJobData", id:"jobId", records: function(obj) {} },
		   	 
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'createdDateTime', 
			viewrecords: true, 
			sortorder: "desc",
			height: 155,
			width: 900, 
			editurl: 'getExportStatus.do',
			ondblClickRow: function(rowid) {},
			caption:$("#statusListID").val(),
			//caption:"Export Data:View Status",
			onPaging: function() { },
			onSelectRow: function () { },
			loadComplete: function () { },
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
			 });
				
		$("#list2").jqGrid('navGrid','#pager2',{edit:false,add:false,del:false});        
         
      }
         		
			