
function populateDownloadListGrid() {
 		$("#downloadDataListId").jqGrid({         
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
				//window.location.href="/SessionWeb/logout.do";
			}
	 });
}

 

function populateDownloadTemplateListGrid() {
 		$("#uploadDataListId").jqGrid({         
         url: 'populateDownloadTemplateListGrid.do', 
		 type:   'POST',
		 datatype: "json",         
          colNames:['Name', 'Description'],
		   	colModel:[
		   		{name:'templateName', index:'templateName', width:200, align:"left", editable:false, sortable:false},
		   		{name:'templateDesc', index:'templateDesc', width:790, align:"left", editable:false, sortable:false}
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
				//window.location.href="/SessionWeb/logout.do";
			}
	 });
}

function populateUploadListGrid() {
 		$("#viewUploadsListId").jqGrid({         
         url: 'populateUploadListGrid.do', 
		 type:   'POST',
		 datatype: "json",         
          colNames:['Upload Date', 'File Name', 'Records Uploaded', 'Records Failed', 'Status'],
		   	colModel:[
		   		{name:'createdDateTime',index:'createdDateTime', width:200, align:"left", editable:false, sorttype:'datetime', sortable:true},
		   		{name:'uploadFileName',index:'uploadFileName', width:340, align:"left", editable:false, sorttype:'text', sortable:true},
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
			caption: "Files Uploaded",
			sortname: 'createdDateTime', 
			sortorder: "desc",
			pager: '#viewUploadsListPager', 
			onPaging: function() {
				$('#del_viewUploadsListId').addClass('ui-state-disabled');					
				setAnchorButtonState('downloadErrorFile', true);
				document.getElementById('colorErrors').style.display = "none";
			},
			onSelectRow: function (rowId) {
				var selectedId = $("#viewUploadsListId").getGridParam('selrow');
				document.getElementById('selectedId').value = selectedId;

				if (selectedId.indexOf("_SC") > 0) {
					$('#del_viewUploadsListId').removeClass('ui-state-disabled');					
					setAnchorButtonState('downloadErrorFile', true);
					document.getElementById('colorErrors').style.display = "none";
				}				
				if (selectedId.indexOf("_FL") > 0) {
					$('#del_viewUploadsListId').removeClass('ui-state-disabled');					
					setAnchorButtonState('downloadErrorFile', false);
					document.getElementById('colorErrors').style.display = "block";
				}				
				if (selectedId.indexOf("_IN") > 0) {
					$('#del_viewUploadsListId').addClass('ui-state-disabled');					
					setAnchorButtonState('downloadErrorFile', true);
					document.getElementById('colorErrors').style.display = "none";
				}				
				
			},
			loadComplete: function () {
				$('#del_viewUploadsListId').addClass('ui-state-disabled');					
				document.getElementById('del_viewUploadsListId').title = "Delete File";					
				setAnchorButtonState('downloadErrorFile', true);
				document.getElementById('colorErrors').style.display = "none";			    
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				//window.location.href="/SessionWeb/logout.do";
			}
	 });
	 
	 jQuery("#viewUploadsListId").navGrid('#viewUploadsListPager', {			
		    	delfunc: function() {
		    		deleteFile();
		    	},
		    	search: false     	
			})

			jQuery("#refresh_viewUploadsListId").bind("click",function(){
				refresh();
			});

	 		var addButton = document.getElementById('add_viewUploadsListId');
			addButton.style.display = 'none';
	 		var editButton = document.getElementById('edit_viewUploadsListId');
			editButton.style.display = 'none';
	 
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


function downloadData(element)
{
	if (isButtonDisabled(element)) 
		return true;

    var element = document.getElementById("downloadFile");
    element.form.action = "downloadData.do";
    element.form.submit();
	return false;
}


function downloadTemplate(element)
{
	if (isButtonDisabled(element)) 
		return true;

    var element = document.getElementById("downloadFile");
    element.form.action = "downloadTemplate.do";
    element.form.submit();
	return false;
}

function deleteFile()
{
   	document.forms[0].action = "deleteErrorDataFile.do";
    document.forms[0].submit();
	return false;
}

function downloadErrorFile(element)
{
	if (isButtonDisabled(element)) 
		return true;

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

function uploadFile(element)
{
	if (isButtonDisabled(element)) 
		return true;

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
	if (uploadMsg == null) {
		document.getElementById('infoMessage').style.display = "none";
		document.getElementById('errorMessage').style.display = "none";
	}
	else {	
		if (uploadMsg.length == 0) {
			document.getElementById('infoMessage').style.display = "none";
			document.getElementById('errorMessage').style.display = "none";
		}
		else
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
