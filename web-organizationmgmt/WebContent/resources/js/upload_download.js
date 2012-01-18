
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
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
}

function populateUploadListGrid() {
 		$("#list3").jqGrid({         
         url: 'populateUploadListGrid.do', 
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
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
}

function enableUpload()
{
    var fileName = document.getElementById("inputbox").value;
    var element = document.getElementById("upload");
    if (fileName != "") {  
		setAnchorButtonState('upload', false);
    } else {
		setAnchorButtonState('upload', true);
    }
    
}


function downloadData()
{
    var element = document.getElementById("downloadFile");
    //alert(element);
    //alert(element.value);
    element.form.action = "downloadData.do";
    element.form.submit();
    
	//document.form.action = 'downloadData.do';
	//document.form.submit();
	
	return false;
}

function ajaxDownloadData()
{
	$.ajax({
		async:		false,
		beforeSend:	function(){
					},
		url:		'downloadData.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						alert(data);
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						alert(textStatus);
						//window.location.href="/TestSessionInfoWeb/logout.do";						
					},
		complete :  function(){
						alert("ok");
					}
	});

	return false;		
}
