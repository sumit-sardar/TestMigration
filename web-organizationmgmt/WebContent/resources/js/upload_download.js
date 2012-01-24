
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
				if (rowId == 1)  
					document.getElementById('downloadFile').value = "userFile";
				else 
					document.getElementById('downloadFile').value = "studentFile";
				setAnchorButtonState('exportDataButton', false);
			},
			loadComplete: function () {
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();
				alert("failed to load : populateDownloadListGrid");  
				//window.location.href="/SessionWeb/logout.do";
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
				if (rowId == 1)  
					document.getElementById('downloadFile').value = "userFile";
				else 
					document.getElementById('downloadFile').value = "studentFile";
				setAnchorButtonState('exportDataButton', false);
			},
			loadComplete: function () {
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				alert("failed to load : populateDownloadTemplateListGrid");  
				//window.location.href="/SessionWeb/logout.do";
			}
	 });
}

function populateUploadListGrid() {
 		$("#list3").jqGrid({         
         url: 'populateUploadListGrid.do', 
		 type:   'POST',
		 datatype: "json",         
          colNames:['Upload Date', 'File Name', 'Records Uploaded', 'Records Failed', 'Status'],
		   	colModel:[
		   		{name:'createdDateTime',index:'createdDateTime', width:200, align:"left", editable:false, sorttype:'date', sortable:true},
		   		{name:'uploadFileName',index:'uploadFileName', width:420, align:"left", editable:false, sorttype:'text', sortable:true},
		   		{name:'uploadFileRecordCount',index:'uploadFileRecordCount', width:150, align:"left", editable:false, sorttype:'text', sortable:true},
		   		{name:'failedRecordCount',index:'failedRecordCount', width:150, align:"left", editable:false, sorttype:'text', sortable:true},
		   		{name:'status',index:'status', width:150, align:"left", editable:false, sorttype:'text', sortable:true}
		   	],
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			viewrecords: true, 
			height: 150,			
			caption: "Download Template",
			sortname: 'createdDateTime', 
			sortorder: "asc",
			pager: '#pager3', 
			onPaging: function() {
			},
			onSelectRow: function (rowId) {
				var selectedId = $("#list3").getGridParam('selrow');
				document.getElementById('selectedId').value = selectedId;

				if (selectedId.indexOf("_SC") > 0) {
					setAnchorButtonState('deleteFile', false);
					setAnchorButtonState('downloadErrorFile', true);
				}				
				if (selectedId.indexOf("_FL") > 0) {
					setAnchorButtonState('deleteFile', false);
					setAnchorButtonState('downloadErrorFile', false);
				}				
				if (selectedId.indexOf("_IN") > 0) {
					setAnchorButtonState('deleteFile', true);
					setAnchorButtonState('downloadErrorFile', true);
				}				
				
			},
			loadComplete: function () {
				width = 997;
			    jQuery("#list3").setGridWidth(width);
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				alert("failed to load : populateUploadListGrid");  
				//window.location.href="/SessionWeb/logout.do";
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
	handleUploadMessages(null);
}


function downloadData()
{
    var element = document.getElementById("downloadFile");
    element.form.action = "downloadData.do";
    element.form.submit();
	return false;
}

/*
function downloadData()
{
	var downloadFile = document.getElementById('downloadFile').value;
	
	$.ajax({
		async:		false,
		beforeSend:	function(){
					},
		url:		'downloadData.do' + '?downloadFile=' + downloadFile,
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
					},
		complete :  function(){
					}
	});

	return false;		
}
*/


function downloadTemplate()
{
    var element = document.getElementById("downloadFile");
    element.form.action = "downloadTemplate.do";
    element.form.submit();
	return false;
}

function deleteFile()
{
    var element = document.getElementById("downloadFile");
    element.form.action = "deleteErrorDataFile.do";
    element.form.submit();
	return false;
}

function downloadErrorFile()
{
    var element = document.getElementById("downloadFile");
    element.form.action = "getErrorDataFile.do";
    element.form.submit();
	return false;
}

function refresh()
{
    var element = document.getElementById("downloadFile");
    element.form.action = "manageUpload.do?showViewUpload=true";
    element.form.submit();
	return false;
}

function uploadFile()
{
	showLoadingProgress('<br/><b>File Uploading...</b><br/>');
    var element = document.getElementById("downloadFile");
    element.form.action = "uploadData.do";
    element.form.submit();
	return false;
}

function showLoadingProgress(msg)
{	
	$.blockUI({ message: msg, css: { height: '50px'} }); 		 
}

function handleUploadMessages(uploadMsg){
	if (uploadMsg == null || uploadMsg.length == 0) {
		document.getElementById('infoMessage').style.display = "none";
		document.getElementById('errorMessage').style.display = "none";
	}
	else {	
		if (uploadMsg.indexOf("successfully") > 0) {
			document.getElementById('errorMessage').style.display = "none";
			$("#infoMsg").html(uploadMsg);
			document.getElementById('infoMessage').style.display = "block";
		}
		else {
			document.getElementById('infoMessage').style.display = "none";
			$("#errorMsg").html(uploadMsg);
			document.getElementById('errorMessage').style.display = "block";
		}
	}	  			
}
