
function populateDownloadListGrid() {
 		$("#list2").jqGrid({         
         url: 'populateDownloadListGrid.do', 
		 type:   'POST',
		 datatype: "json",         
          colNames:['Name', 'Description'],
		   	colModel:[
		   		{name:'cid',index:'cid', width:200, align:"left", editable:false, sortable:false},
		   		{name:'tcl',index:'tcl', width:850, align:"left", editable:false, sortable:false}
		   	],
		   	loadui: "disable",
			rowNum:2,
			loadonce:true, 
			multiselect:false,
			viewrecords: true, 
			height: '30px',  			
			caption: "Download Data",
			onSelectRow: function (rowId) {
			   	var userFile = document.getElementById('userFile').value;
			   	var studentFile = document.getElementById('studentFile').value;
				if (rowId == 1)  
					document.getElementById('downloadFile').value = userFile;
				else 
					document.getElementById('downloadFile').value = studentFile;
				setAnchorButtonState('exportDataButton', false);
			},
			loadComplete: function () {
			/*
            	var width = jQuery("#downloadData").width();
			    width = width - 80; // Fudge factor to prevent horizontal scrollbars
			    jQuery("#list2").setGridWidth(width);
			*/
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
}



function populateDownloadTemplateListGrid() {
 		$("#list2").jqGrid({         
         url: 'populateDownloadTemplateListGrid.do', 
		 type:   'POST',
		 datatype: "json",         
          colNames:['Name', 'Description'],
		   	colModel:[
		   		{name:'cid',index:'cid', width:200, align:"left", editable:false, sortable:false},
		   		{name:'tcl',index:'tcl', width:790, align:"left", editable:false, sortable:false}
		   	],
		   	loadui: "disable",
			rowNum:2,
			loadonce:true, 
			multiselect:false,
			viewrecords: true, 
			height: '30px',  			
			caption: "Download Template",
			onSelectRow: function (rowId) {
				setAnchorButtonState('exportDataButton', false);
			},
			loadComplete: function () {
			/*
            	var width = jQuery("#templates").width();
			    width = width - 80; // Fudge factor to prevent horizontal scrollbars
			    jQuery("#list2").setGridWidth(width);
			*/
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
}

