var selectProctorGridLoaded = false;
var allRowSelectedPro = false;
var selectedSessionPro = {};
var unCheckedSessionPro = {};
//var categoriesStr = ":All;JV:JV;AD:AD"; 
//var AccommOption = ":Any;T:Yes;F:No";

var addProctorLocaldata =[];
var proctorForSelectedOrg;
var preSelectedOrgPro;
var selectedProctorIds = "";
var deletedProctorIds = "";
var pindex = 0;
var pdindex = 1;
var proctorIdObjArray = {};
var delProctorIdObjArray = {};
var isOnBackProctor = false;

var allProctorIds = {};
var allSelectOrgProctor = {};
var countAllSelectProctor = 0;
var orgDataInform = {};

var tempOrgDataInform = {};
var tempProctorData = {};
var tempAllSelectOrgProctor= {};
var onloadGrid = false;

function showSelectProctor(){
	$("#proctorAddDeleteInfo").hide();
	$("#Proctor_Tab").css('display', 'none');
	$("#Select_Proctor_Tab").css('display', 'block');
	if(orgTreeHierarchy == "" || orgTreeHierarchy ==undefined) {
		populateProctorTree();
	} else {
		loadInnerProctorOrgTree();
	}
	
}
function hideSelectedProctor (){	

	delete orgDataInform;
	delete allSelectOrgProctor;
	delete proctorIdObjArray;
	
	orgDataInform = {};
	allSelectOrgProctor = {};
	
	var jsonDataOrg = {};	
	for (var i in tempProctorData) {		
		var objstr = tempProctorData[i];		
		jsonDataOrg[i] = objstr;
	}
	
	for(var j in tempAllSelectOrgProctor){
		if(tempAllSelectOrgProctor[j]){
			allSelectOrgProctor[j] = true;
		}else {
			allSelectOrgProctor[j] = false;
		}
		var count = parseInt(tempOrgDataInform[j]);
		orgDataInform[j] = count;
	}
	
	proctorIdObjArray = jsonDataOrg;	
	
	isOnBackProctor = true;
	$("#Proctor_Tab").css('display', 'block');
	$("#Select_Proctor_Tab").css('display', 'none');	
	
}

function loadInnerProctorOrgTree() {
	
	if(!isOnBackProctor) {
		createSingleNodeSelectedTreeForProctor (orgTreeHierarchy);
	} else{
		$("#proctorOrgNodeHierarchy").jstree("close_all");
		$("#selectProctor").GridUnload();
		selectProctorGridLoaded = false;
	}
	$("#innerProctorSearchheader").css("visibility","visible");	
	$("#proctorOrgNodeHierarchy").css("visibility","visible");	

}

function populateProctorTree() {
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock()
					},
		url:		'userOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI(); 
						orgTreeHierarchy = data;
						createSingleNodeSelectedTreeForProctor (orgTreeHierarchy);
						$("#innerProctorSearchheader").css("visibility","visible");	
						$("#proctorOrgNodeHierarchy").css("visibility","visible");							
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

function createSingleNodeSelectedTreeForProctor(jsondata) {
	   $("#proctorOrgNodeHierarchy").jstree({
	        "json_data" : {	             
	            "data" : jsondata.data,
				"progressive_render" : true,
				"progressive_unload" : false
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
	    
	    $("#proctorOrgNodeHierarchy").delegate("a","click", function(e) {
			 	 
  			proctorForSelectedOrg = $(this).parent().attr("id");
 		    $("#proctorOrgNodeId").val(proctorForSelectedOrg);
 		    allRowSelectedPro = false;
 		    UIBlock();
 		    if(!selectProctorGridLoaded) {
 		        populateSelectProctorGrid();
			}
			else
				gridReloadSelectProctor();
		});
}

function isEmpty(obj){
	var empty = true;
	for( var i in obj ) {
		empty = false;
		break;
	}
	return empty;
}

function populateSelectProctorGrid() {
 		UIBlock();
 		
 		selectProctorGridLoaded = true;
 		//reset();
       $("#selectProctor").jqGrid({         
          url: 'getProctorList.do?q=2&proctorOrgNodeId='+$("#proctorOrgNodeId").val(), 
		  type:   'POST',
		  datatype: "json",          
          colNames:[ 'Last Name','First Name','Default Scheduler','User Id',"User Name","Copyable"],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:90, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:90, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'defaultScheduler',index:'defaultScheduler', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userId',index:'userId', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userName',index:'userName', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'copyable',index:'copyable', width:130, editable: false, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	],

		   	jsonReader: { repeatitems : false, root:"userProfileInformation", id:"userId", records: function(obj) {		   	 
		   //	if(obj.userProfileInformation != null && obj.userProfileInformation != undefined && obj.userProfileInformation.length > 0) {
		   	 	allProctorIds = {};
		   	 	allProctorIds['dataList'] = {};
		   	 	var i = 0;
			   	 for(i = 0; i <obj.userProfileInformation.length; i++) {
			   	 	allProctorIds['dataList'][obj.userProfileInformation[i].userId] = obj.userProfileInformation[i];
			   	 }
			   	 allProctorIds['dataLength'] = i;
		   	 //}
		   	 
		   	return obj.userProfileInformation.length; } },

		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:true,
			pager: '#selectProctorPager', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 162,  
			caption:"Proctor List",
			onPaging: function() {
			
			var reqestedPage = parseInt($('#selectProctor').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_selectProctorPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#selectProctor').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#selectProctor').setGridParam({"page": minPageSize});
				}
				
				
				
				
			},
			gridComplete: function() {		
			//alert(JSON.stringify(proctorIdObjArray));
			onloadGrid = true;
			if(allSelectOrgProctor[proctorForSelectedOrg]) { 
				 	$("#cb_selectProctor").attr('checked', true).trigger('click').attr('checked', true);
				 	var defaultUserId = $("#schedulerUserId").val();
				 	$("#"+defaultUserId+" td input").attr('checked',true).attr("disabled", true);
				 	$("#"+ defaultUserId).addClass('ui-state-disabled');
			 } 		 
			 else {
			 	var allRowsInGrid = $('#selectProctor').jqGrid('getDataIDs');
				var defaultUserId = $("#schedulerUserId").val();
			 	$("#"+defaultUserId+" td input").attr("disabled", true);
			 	$("#"+ defaultUserId).addClass('ui-state-disabled');
			 	
				for(var i = 0; i < allRowsInGrid.length; i++) {
			 		if(proctorIdObjArray[allRowsInGrid[i]]){
			 			var seledtedRowId = allRowsInGrid[i];							
						if(!($("#"+allRowsInGrid[i]+" td input").attr('checked'))){
							$("#"+allRowsInGrid[i]+" td input").attr('checked', true).trigger('click').attr('checked', true);
						}
			 		}else {
			 			if($("#"+allRowsInGrid[i]+" td input").attr('checked')){
							$("#"+allRowsInGrid[i]+" td input").attr('checked', false).trigger('click').attr('checked', false);
						}
			 		}			 		
				}
			 }
			 
			 onloadGrid = false;
				
			},
			onSelectAll: function (rowIds, status) {
				if(!onloadGrid){
					if(status) {
						for(var key in allProctorIds['dataList']) {
							if (allProctorIds['dataList'][key].defaultScheduler == 'F') {
								proctorIdObjArray[key] = allProctorIds['dataList'][key];																							
								
					 		}				
						}						
						orgDataInform[proctorForSelectedOrg] = allProctorIds['dataLength'];
						// Added to handle multiple organization select All	
						allSelectOrgProctor[proctorForSelectedOrg] = true; 	
					} else {
						//orgDataInform[proctorForSelectedOrg] = 1 ;
						orgDataInform[proctorForSelectedOrg] = 0;
						allSelectOrgProctor[proctorForSelectedOrg] = false;
						for(var key in allProctorIds['dataList']) {
							if (allProctorIds['dataList'][key].defaultScheduler == 'F') {
								delete proctorIdObjArray[key];
							}
							else if (allProctorIds['dataList'][key].defaultScheduler == 'T') {
								orgDataInform[proctorForSelectedOrg] = 1 ;
							}	
						}							
					}		
				}
						
			},
			onSelectRow: function (rowid, status) {
				if(!onloadGrid){
					var selectedRowId = rowid;
					if(status) {
						var selectedRowData = $("#selectProctor").getRowData(selectedRowId);
						proctorIdObjArray[selectedRowId] = selectedRowData;
						
						orgDataInform[proctorForSelectedOrg] = parseInt(orgDataInform[proctorForSelectedOrg]) + 1;
						if(parseInt(orgDataInform[proctorForSelectedOrg]) == parseInt(allProctorIds['dataLength'])){
							allSelectOrgProctor[proctorForSelectedOrg] = true; 
							$("#cb_selectProctor").attr('checked', true).trigger('click').attr('checked', true);					
						}
						//orgDataInform[proctorForSelectedOrg] = parseInt(orgDataInform[proctorForSelectedOrg]) + 1;
						
					} else {
						if(proctorIdObjArray[selectedRowId]){
							delete proctorIdObjArray[selectedRowId];
							orgDataInform[proctorForSelectedOrg] = parseInt(orgDataInform[proctorForSelectedOrg]) - 1;
						}
						
						allSelectOrgProctor[proctorForSelectedOrg] = false; 
						$("#cb_selectProctor").attr('checked', false);
					} 
				}	
				
			},
			loadComplete: function () {
				if ($('#selectProctor').getGridParam('records') === 0) {
					isPAGridEmpty = true;
            		$('#sp_1_selectProctorPager').text("1");
            		$('#next_selectProctorPager').addClass('ui-state-disabled');
            	 	$('#last_selectProctorPager').addClass('ui-state-disabled');
            	} else {
            		isPAGridEmpty = false;
            	}
            	var selectedCount = 0;
            	for(var key in allProctorIds['dataList']) {
			 		if(proctorIdObjArray[key]){
						selectedCount++;
			 		}			 		
				}
				
				orgDataInform[proctorForSelectedOrg] = selectedCount;
				
				if(parseInt(orgDataInform[proctorForSelectedOrg]) > 0 && (parseInt(orgDataInform[proctorForSelectedOrg]) == parseInt(allProctorIds['dataLength']))){
					allSelectOrgProctor[proctorForSelectedOrg] = true; 
					$("#cb_selectProctor").attr('checked', true).trigger('click').attr('checked', true);					
				}
            	
            	//setEmptyListMessage('PA');
            	$.unblockUI();  
				$("#selectProctor").setGridParam({datatype:'local'});
				var tdList = ("#selectProctorPager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				var width = jQuery("#scheduleSession").width();
		    	width = width - 280; // Fudge factor to prevent horizontal scrollbars
		    	$("#selectProctor").jqGrid("hideCol","defaultScheduler");
		    	$("#selectProctor").jqGrid("hideCol","userId");
		    	$("#selectProctor").jqGrid("hideCol","userName");
		    	$("#selectProctor").jqGrid("hideCol","copyable");
		    	jQuery("#selectProctor").setGridWidth(width,true);
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	  jQuery("#selectProctor").jqGrid('navGrid','#selectProctorPager',{edit:false,add:false,del:false,search:false,refresh:false});
	  //jQuery("#selectProctor").jqGrid('filterToolbar');

}

function include(arr,obj) {
     //return (arr.indexOf(obj) != -1);
    var indx =  jQuery.inArray(obj, arr); 
     if(indx != -1)
      return true;
     else
      return false;
     
}
	
function returnSelectedProctor() {

	var val= [] ;
	var previousDataCount = 0;
	var previousData = tempProctorData;
	
	
	for (var i in previousData) {		
		previousDataCount++;
	}
	
	
	var message = "";
		
	delete tempOrgDataInform;
	delete tempAllSelectOrgProctor;
	delete tempProctorData;
	
	tempOrgDataInform = {};
	tempAllSelectOrgProctor= {};
	var jsonDataOrg = {};
	
	for (var i in proctorIdObjArray) {		
		var objstr = proctorIdObjArray[i];
		jsonDataOrg[i] = objstr;
		val.push(objstr);
	}
	for(var j in allSelectOrgProctor){
		if(allSelectOrgProctor[j]){
			tempAllSelectOrgProctor[j] = true;
		}else {
			tempAllSelectOrgProctor[j] = false;
		}
		var count = parseInt(orgDataInform[j]);
		tempOrgDataInform[j] = count;
	}
	$("#proctorAddDeleteInfo").show();
	if (val.length > previousDataCount && val.length > 1){	
		if (previousDataCount == 0){
			previousDataCount = 1;
		}
		var value = val.length - previousDataCount;
		message = value + "  " + PROCTORS_ADDED;			
	}
	else if(val.length < previousDataCount) {
		var value = previousDataCount - val.length;
		message = value + "  " + PROCTORS_DELETED;			
	}
	else {
		$("#proctorAddDeleteInfo").hide();
	}
	tempProctorData = jsonDataOrg;
	
	addProctorLocaldata = val;
	noOfProctorAdded = addProctorLocaldata.length;
	isOnBackProctor = true;
	$("#Proctor_Tab").css('display', 'block');
	$("#Select_Proctor_Tab").css('display', 'none');
	gridReloadProctor(false);
	//$("#proctorAddDeleteInfo").show();
	$("#addDeleteProc").text(message);	
	$("#totalAssignedProctors").text(noOfProctorAdded);
	
			 
}

function getProctorListArray(proctorArray) {
	  var resultProcArray = [];
	  if (proctorArray==undefined) {
	  	return resultProcArray;
	  }
	  var l = 0;
	  for (var i=0; i<proctorArray.length; i++) {
	    if(proctorArray[i] != undefined && proctorArray[i] != null){
		    var val = new ProctorAssignment(proctorArray[i].userId, proctorArray[i].userName, proctorArray[i].copyable);
		  	resultProcArray[l]= val.toString();
		  	++l;
	    }
	  }
	  return resultProcArray;
	}
	
	
function ProctorAssignment(userId, userName, copyable) {
	   this.userId = userId;
	   this.userName = userName;
	   this.copyable = "";
	   if(copyable != undefined)
	   		this.copyable = copyable;

	}
	
	ProctorAssignment.prototype.toString = function () {
  		return ( ""+"userId="+this.userId +":userName=" +this.userName + ":copyable="+this.copyable+":");
	};
	
function resetProctor() {

	selectProctorGridLoaded = false;
	allRowSelectedPro = false;
	selectedSessionPro = {};
	unCheckedSessionPro = {};
	addProctorLocaldata =[];
	proctorForSelectedOrg;
	preSelectedOrgPro;
	//selectedProctorIds = "";
	deletedProctorIds = "";
	pindex = 0;
	pdindex = 1;
	proctorIdObjArray = {};
	delProctorIdObjArray = {};
	isOnBackProctor = false;
	noOfProctorAdded = 0;
	proctorGridloaded = false;
	$('#listProctor').GridUnload();
	$('#selectProctor').GridUnload();
	allRowSelectedPro = false;
	allProctorIds = [];
	allSelectOrgProctor = {};
	countAllSelectProctor = 0;
	tempOrgDataInform = {};
	tempProctorData = {};
	tempAllSelectOrgProctor= {};
	allProctorSelected = false;
	proctorSelectedLength = 1;
	
}

