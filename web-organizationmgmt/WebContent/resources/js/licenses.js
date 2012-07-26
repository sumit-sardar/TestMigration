
var jsonData;
var rootNode = [];
var rootMap = {};

var orgTreeHierarchy;
var SelectedOrgNodeId ;
var leafNodeCategoryId;
var isLeafNodeAdmin;
var gridloaded = false;
var orgNodeId ;
var errorMsg;

var reloadChildren;
var editingId = null;
var editingRow = null;
var editingCol = null;
var requestedPage = 1;

function loadOrgNodeTree() {
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'loadOrgNodeTree.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI(); 
						orgTreeHierarchy = data;
						leafNodeCategoryId = data.leafNodeCategoryId;
						isLeafNodeAdmin = data.isLeafNodeAdmin;
						jsonData = orgTreeHierarchy.data;
						getRootNodeDetails();						
						createOrgNodeTree (orgTreeHierarchy);
						$("#searchheader").css("visibility","visible");	
						$("#orgNodeHierarchy").css("visibility","visible");			
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

function getRootNodeDetails(){
	var noOfRoots = jsonData.length;
		for (var i = 0,j = noOfRoots; i < j; i++ ){
			rootMap[jsonData[i].attr.id] = jsonData[i].attr.cid;
			rootNode.push({data: jsonData[i].data,attr:{id: jsonData[i].attr.id,cid:jsonData[i].attr.cid,tcl:jsonData[i].attr.tcl},children : [{}]});
		}
}

function createOrgNodeTree(jsondata) {
	   $("#orgNodeHierarchy").jstree({
	        "json_data" : {	             
	            "data" : rootNode,
				"progressive_render" : true,
				"progressive_unload" : true
	        },
	        "ui" : {  
	           "select_limit" : 1
         	},
         	"themes" : {
				"theme" : "apple",
				"dots" : false,
				"icons" : false
			},  
				"plugins" : [ "themes", "json_data", "ui"]  
				
	    });
	    
	    $("#orgNodeHierarchy").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#orgNodeHierarchy ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
	    
	    $("#orgNodeHierarchy").delegate("a","click", function(e) {
	    	document.getElementById('displayMessageMain').style.display = "none";
	    	
			if (isEditing()) {
				//jQuery('#orgNodeGrid').jqGrid("restoreCell", editingRow, editingCol); 				
				return true;
			}
	    	
			currentClickedId = this.parentNode.id;
			currentClickedTcl = this.parentNode.getAttribute("tcl");	 
  			SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#treeOrgNodeId").val(SelectedOrgNodeId);
 		     		    
 		    if(!gridloaded) {
 		    	UIBlock();
			    loadOrgNodeLicense();
			}
			else {
 		    	UIBlock();
				orgNodeLicenseReload();
			}
			
		});
	    registerDelegate("orgNodeHierarchy");
}
	
function registerDelegate(tree){

	$("#"+tree).delegate("li ins","click", function(e) {
		type = tree;
		var x = this.parentNode;		
		var categoryId  = x.getAttribute("tcl");
		if(categoryId != null || categoryId != undefined){
			currentCategoryLevel = categoryId ; 
		}
		else{
			currentCategoryLevel ="1";
		}
		currentNodeId = x.id;			
		var classState = $(x).hasClass("jstree-open");			
		var rootCategoryLevel = rootMap[currentNodeId];
	
		if (classState == false){
			if (currentCategoryLevel == 1) {	
				dataObj2 = [];	
				var indexOfRoot = getIndexOfRoot(currentNodeId);
				populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot);
			}
	
			var cacheData = map.get(currentNodeId);
			if (cacheData != null){
				currentTreeArray = cacheData;			
			}
			if (cacheData == null){
				switch(currentCategoryLevel){
					
					//Not caching at initial level because the whole data will be put in cache which may increase the cache size
					//considerably
					
					case "2": 	dataObj3 =getObject(jsonData,currentNodeId,currentCategoryLevel,x.parentNode.parentNode.id);
								currentIndex = dataObj3.index;
								currentTreeArray = dataObj3.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//alert("case2");
							break;
							
					case "3": 	dataObj4 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj4,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//alert("case3");
							break;
					case "4": 	dataObj5 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj5,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							//	alert("case4");
							break;
					case "5": 	dataObj6 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj6,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "6": 	dataObj7 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj7,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "7": 	dataObj8 =map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj8,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								
							break;	
					case "8": 	dataObj9 =map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj9,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;						
					
				}
			}

		}
			
	});
}

function loadOrgNodeLicense() {

		 var postDataObject = {};
 		 postDataObject.q = 2;
 		 postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
 		 
         $("#orgNodeLicenseGrid").jqGrid({    
    
         url:'loadOrgNodeLicense.do', 
		 mtype:   'POST',
		 datatype: "json",         
         postData: postDataObject,       
          colNames:[$("#orgNodeName").val(),$("#scheduled").val(),$("#consumed").val(), $("#available").val()],
		   	colModel:[
		   	
		   	    {name:'name', index:'name', width:290, editable: false, align:"left", sortable:false },
		   		{name:'reserved', index:'reserved', width:100, editable:false, align:"left", sortable:false },
		   		{name:'consumed', index:'consumed', width:100, editable:false, align:"left", sortable:false },
		   		{name:'available', index:'available', width:100, editable: false, align:"left", sortable:false }
		   		
		   	],
		   	jsonReader: { repeatitems : false, root:"orgNodeLicenses", id:"id",records: function(obj) { if(obj.orgNodeLicenses.length > 0) isGridEmpty = false; else isGridEmpty = true;  return obj.orgNodeLicenses.length; } },
		   	
		   	
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			viewrecords: true, 
			sortorder: "asc",
			height: '50px',
			width: $("#jqGrid-content-section").width(), 
			caption:'Parent Group',
            cellEdit: true,
		   	
			beforeSelectRow: function (rowid) {
				return null;
			},		   	
			loadComplete: function () {
			
				document.getElementById('licenseModelDiv').style.display = "block";	
			
		    	if(!gridloaded) {            	
					loadChildrenOrgNodeLicense();
		  			gridloaded = true;
					reloadChildren = true;		  			
				}
	           	else {
	           		if (reloadChildren) {
						childrenOrgNodeLicenseReload();
					}
					else {
						highLightNoAvailable();
					}
					reloadChildren = true;
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
}

function orgNodeLicenseReload(){ 
	var postDataObject = {};
	postDataObject.q = 2;
	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
	jQuery("#orgNodeLicenseGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
	jQuery("#orgNodeLicenseGrid").jqGrid('setGridParam', {url:'loadOrgNodeLicense.do',postData: postDataObject,page:1}).trigger("reloadGrid");
}

function loadChildrenOrgNodeLicense() {
		 var postDataObject = {};
 		 postDataObject.q = 2;
 		 postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
 		 
         $("#orgNodeGrid").jqGrid({    
    
         url:'loadChildrenOrgNodeLicense.do', 
		 mtype:   'POST',
		 datatype: "json",         
         postData: postDataObject,    
         
         async:	false,
            
          colNames:[$("#orgNodeName").val(),$("#scheduled").val(),$("#consumed").val(), $("#available").val()],
		   	colModel:[
		   	
		   	    {name:'name', index:'name', width:290, editable: false, align:"left", sorttype:'text', sortable:true },
		   		{name:'reserved', index:'reserved', width:100, editable:false, align:"left", sortable:true },
		   		{name:'consumed', index:'consumed', width:100, editable:false, align:"left", sortable:true },
		   		{name:'available', index:'available', width:100, editable: true, align:"left", sortable:true }
		   		
		   	],
		   	jsonReader: { repeatitems : false, root:"orgNodeLicenses", id:"id",records: function(obj) { if(obj.orgNodeLicenses.length > 0) isGridEmpty = false; else isGridEmpty = true;  return obj.orgNodeLicenses.length; } },
		   	
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			pager: '#orgNodePager', 
			sortname: 'name', 
			viewrecords: true, 
			sortorder: "asc",
			height: 150,
			width: $("#jqGrid-content-section").width(), 
			caption:'Group List',
					   	
            cellEdit: true,
		   	cellurl: 'updateCellValue.do',
            cellsubmit: "remote",
		   	 
          	beforeEditCell : function(rowid,cellname,value,iRow,iCol) { 
              	//alert('beforeEditCell: ' + rowid + " - " + value);
              	setEditingInfo(iRow, iCol, value);
          	},
          	beforeSaveCell : function(rowid,cellname,value,iRow,iCol) { 
              	//alert('beforeSaveCell: ' + rowid + " - " + value);
              	setEditingInfo(null, null, value);
				
              	errorMsg = "";
              	if (! validNumber(value)) {
              		errorMsg = "Enter a number that is 0 or greater.";
					return 'ERROR';
				}
				var intValue = str2num(value);
				value = intValue.toString();
				
				return value; 
          	},
          	beforeSubmitCell : function(rowid,cellname,value,iRow,iCol) { 
              	//alert('beforeSubmitCell: ' + rowid + " - " + value);
              	setEditingInfo(null, null, value);

              	if (errorMsg.length > 0) {
              		alert(errorMsg);
              	}              	
          	},
          	afterSubmitCell : function(serverresponse,rowid,cellname,value,iRow,iCol) { 
              	//alert('afterSubmitCell: ' + serverresponse.responseText );
              	setEditingInfo(null, null, value);

              	if (serverresponse.responseText == 'OK') {
              	
              		reloadChildren = false;
              		orgNodeLicenseReload();
              		
              		return [true, ""];
              	}
              	else {
              		if (serverresponse.responseText != 'ERROR') 
              			alert(serverresponse.responseText);
              		return [false, "", ""];
              	}
          	},
          	afterSaveCell : function(rowid,cellname,value,iRow,iCol) { 
              	//alert('afterSaveCell: ' + rowid + " - " + value);
              	setEditingInfo(null, null, value);
          	},
			/*			
			onCellSelect: function (rowid, iCol, cellcontent, e) {
			},		   	
			*/	   	 
			onPaging: function() {

				if (isEditing()) {
					jQuery('#orgNodeGrid').jqGrid("restoreCell", editingRow, editingCol); 				
					$('#orgNodeGrid').setGridParam({"page": requestedPage});
					return 'stop';
				}
						
				requestedPage = parseInt($('#orgNodeGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_orgNodePager').text());
				var minPageSize = 1;
				if(requestedPage > maxPageSize){
					requestedPage = maxPageSize;
					$('#orgNodeGrid').setGridParam({"page": maxPageSize});
				}
				if(requestedPage <= minPageSize){
					requestedPage = minPageSize;
					$('#orgNodeGrid').setGridParam({"page": minPageSize});
				}
				
			},
			loadComplete: function () {
				if ($('#orgNodeGrid').getGridParam('records') === 0) {
					document.getElementById('orgNodeGridSection').style.display = "none";	
					isGridEmpty = true;
            		$('#sp_1_orgNodePager').text("1");
            		$('#next_orgNodePager').addClass('ui-state-disabled');
            		$('#last_orgNodePager').addClass('ui-state-disabled');
	    			document.getElementById('noDataGridSection').style.display = "block";
            	}
				else {
	    			document.getElementById('orgNodeGridSection').style.display = "block";
	    			document.getElementById('noDataGridSection').style.display = "none";
				}
				
				highLightNoAvailable();
			
				$.unblockUI();  
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
			}
	 });
}



function childrenOrgNodeLicenseReload(){ 
	var postDataObject = {};
	postDataObject.q = 2;
	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
	jQuery("#orgNodeGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
	jQuery("#orgNodeGrid").jqGrid('setGridParam', {url:'loadChildrenOrgNodeLicense.do',postData: postDataObject,page:1}).trigger("reloadGrid");
	jQuery("#orgNodeGrid").sortGrid('name',true,'asc');
}

function isEditing() {
   	if (editingId != null) {
   		var r = confirm("Do you want to discard your change?");
   		if (r) {
   			editingId = null;
			document.getElementById('currentEditing').value = 'false';
   		}
   		return !r;
   	}  		
   	return false;
}

function setEditingId(id) {
	editingId = id;
}

function setEditingInfo(iRow, iCol, value) {
	if (iRow != null) {
		editingId = iRow + "_available";
		document.getElementById('currentEditing').value = 'true';
	}
	else { 
		editingId = null;
		document.getElementById('currentEditing').value = 'false';
	}
   	editingRow = iRow;
   	editingCol = iCol;
}

function highLightNoAvailable() {
	$("table tr").each(function(){   
	
		if( $(this).text().indexOf('0') != -1 ) {  
		
			if (this.className.indexOf('ui-widget-content jqgrow ui-row-ltr') != -1) {
				var el = $('td:eq(3)', $(this));                 
				el.removeAttr('bgcolor');
				if (el.text() == '0') {                        
					el.attr('bgcolor', '#ff0000');
				}
			}
			         
		}	
		
	});
}

function validNumber(sText) {
    var ValidChars = "0123456789.";
    var Char;

    if (sText == "") {
    	return false;	
    }
	sText = $.trim(sText);
    for (i = 0; i < sText.length; i++) {
        Char = sText.charAt(i);
		if (ValidChars.indexOf(Char) == -1) {
		    return false;
		}
    }
    return true;
}

function str2num(sText) {
	sText = $.trim(sText);
   	return parseInt(sText, 10);
}


function saveLicenses() {

	var result = confirm("Click 'OK' to save your changes. Click 'Cancel' to continue to edit license information.");
	if (result) {
		editingId = null;
		document.getElementById('currentEditing').value = 'false';
	 
		$.ajax({
			async:		false,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'saveLicenses.do',
			type:		'POST',
			dataType:	'json',
			success:	function(data, textStatus, XMLHttpRequest){	
							$.unblockUI(); 
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
}
                
function verifyEditLicenseAndGotoMenuAction(action, menuId){
     
	var currentEditing = document.getElementById('currentEditing').value;
    if (currentEditing == 'true') { 
	    var ret = confirm("Click 'OK' to quit editing license information. Any changes you've made will be lost.");
	    if (ret) {
	    	editingId = null;
			document.getElementById('currentEditing').value = 'false';
	    	gotoMenuAction(action, menuId);
   		}
	    else {
	    	return true;
	    }
	}
	else {
    	gotoMenuAction(action, menuId);
    }
}

                