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
var delProctorIdObjArray = [];
var isOnBackProctor = false;

var allProctorIds = {};
var allSelectOrgProctor = {};
var countAllSelectProctor = 0;
var orgDataInform = {};

var tempOrgDataInform = {};
var tempProctorData = {};
var tempAllSelectOrgProctor= {};
var onloadGrid = false;
var crossOrgProctors = {};

function showSelectProctor(){
	$("#Proctor_Tab").css('display', 'none');
	$("#Select_Proctor_Tab").css('display', 'block');
	if(orgTreeHierarchy == "" || orgTreeHierarchy ==undefined) {
		populateProctorTree();
	} else {
		loadInnerProctorOrgTree();
	}
	
}
function hideSelectedProctor (){	
	//backProctorRule();
	delete orgDataInform;
	delete allSelectOrgProctor;
	delete proctorIdObjArray;
	
	orgDataInform = {};
	allSelectOrgProctor = {};
	
	var jsonDataOrg = {};	
	for (var i in tempProctorData) {		
		var objstr = tempProctorData[i];
		var jsonDataUser = {};
		for(var j in objstr){
			jsonDataUser[j] = objstr[j];
		}
		jsonDataOrg[i] = jsonDataUser;	
		if(i != 'All') {
			if(tempAllSelectOrgProctor[i]){
				allSelectOrgProctor[i] = true;
			}else {
				allSelectOrgProctor[i] = false;
			}
			var count = parseInt(tempOrgDataInform[i]);
			orgDataInform[i] = count;
		}
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

function getLength(obj){
	var cnt = 0;
	for( var i in obj ) {
	cnt++;
	}
	return cnt;
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
		   	if(obj.userProfileInformation != null && obj.userProfileInformation != undefined && obj.userProfileInformation.length > 0) {
		   	 	allProctorIds = {};
		   	 	allProctorIds['dataList'] = {};
			   	 for(var i = 0; i <obj.userProfileInformation.length; i++) {
			   	 	allProctorIds['dataList'][obj.userProfileInformation[i].userId] = obj.userProfileInformation[i];
			   	 }
			   	 allProctorIds['dataLength'] = i;
		   	 }
		   	 
		   	return obj.userProfileInformation.length; } },

		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:true,
			pager: '#selectProctorPager', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 151,  
			caption:"Proctor List",
			onPaging: function() {
			
			var reqestedPage = parseInt($('#selectProctor').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_selectProctor').text());
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
			 		if(crossOrgProctors[allRowsInGrid[i]]){
			 			var seledtedRowId = allRowsInGrid[i];							
						if(!($("#"+allRowsInGrid[i]+" td input").attr('checked'))){
							$("#"+allRowsInGrid[i]+" td input").attr('checked', true).trigger('click').attr('checked', true);
						}	
						if(proctorIdObjArray[proctorForSelectedOrg]){
							proctorIdObjArray[proctorForSelectedOrg][seledtedRowId] = crossOrgProctors[allRowsInGrid[i]];
						}else {
							proctorIdObjArray[proctorForSelectedOrg] = {};
							orgDataInform[proctorForSelectedOrg] = 0;
							proctorIdObjArray[proctorForSelectedOrg][seledtedRowId] = crossOrgProctors[allRowsInGrid[i]];
						}
						
						if(parseInt(orgDataInform[proctorForSelectedOrg])  + 1 == parseInt(allProctorIds['dataLength'])){
							allSelectOrgProctor[proctorForSelectedOrg] = true; 
							$("#cb_selectProctor").attr('checked', true).trigger('click').attr('checked', true);					
						}
						orgDataInform[proctorForSelectedOrg] = parseInt(orgDataInform[proctorForSelectedOrg]) + 1;
						
						
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
								/*var jsondata = {};
								jsondata[key] = allProctorIds['dataList'][key];	*/
								if(proctorIdObjArray[proctorForSelectedOrg]){
									proctorIdObjArray[proctorForSelectedOrg][key] = allProctorIds['dataList'][key];
								}else {
									proctorIdObjArray[proctorForSelectedOrg] = {};
									proctorIdObjArray[proctorForSelectedOrg][key] = allProctorIds['dataList'][key];
								}
								crossOrgProctors[key] = allProctorIds['dataList'][key];																									
								
					 		}				
						}						
						orgDataInform[proctorForSelectedOrg] = allProctorIds['dataLength'];
						// Added to handle multiple organization select All	
						allSelectOrgProctor[proctorForSelectedOrg] = true; 	
					} else {
						orgDataInform[proctorForSelectedOrg] = 0 ;
						allSelectOrgProctor[proctorForSelectedOrg] = false;
						for(var i in proctorIdObjArray[proctorForSelectedOrg]){
							delete crossOrgProctors[i];
						}
						delete proctorIdObjArray[proctorForSelectedOrg];								
					}		
				}
						
			},
			onSelectRow: function (rowid, status) {
				if(!onloadGrid){
					var selectedRowId = rowid;
					if(status) {
						var selectedRowData = $("#selectProctor").getRowData(selectedRowId);
						if(proctorIdObjArray[proctorForSelectedOrg]){
							proctorIdObjArray[proctorForSelectedOrg][selectedRowId] = selectedRowData;
						}else {
							proctorIdObjArray[proctorForSelectedOrg] = {};
							orgDataInform[proctorForSelectedOrg] = 0;
							proctorIdObjArray[proctorForSelectedOrg][selectedRowId] = selectedRowData;
						}
						crossOrgProctors[selectedRowId] = selectedRowData;
						
						if(parseInt(orgDataInform[proctorForSelectedOrg])  + 1 == parseInt(allProctorIds['dataLength'])){
							allSelectOrgProctor[proctorForSelectedOrg] = true; 
							$("#cb_selectProctor").attr('checked', true).trigger('click').attr('checked', true);					
						}
						orgDataInform[proctorForSelectedOrg] = parseInt(orgDataInform[proctorForSelectedOrg]) + 1;
						
					} else {
						if(proctorIdObjArray[proctorForSelectedOrg]){
							delete proctorIdObjArray[proctorForSelectedOrg][selectedRowId];
							delete crossOrgProctors[selectedRowId];
							orgDataInform[proctorForSelectedOrg] = parseInt(orgDataInform[proctorForSelectedOrg]) - 1;
						}
						
						if(orgDataInform[proctorForSelectedOrg] == 0){
							delete proctorIdObjArray[proctorForSelectedOrg];
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

function getRuleLength () {

	var tmpArr = selectedProctorIds.split("|");
	return tmpArr.length;

}

function getProctorIDIndex(selectedRowId) {

	var pIDs = selectedProctorIds.split("|");
	var pid = "";
	for (var i = 0; i < pIDs.length; i++) {		
		pid = pIDs[i];
		if (pid.match(selectedRowId) != null) {
			return pid.substring((selectedRowId+"_").length,pid.length).split('_')[0];//added split to remove '_tmp'
		}
	}
	
	return -1;
}

function getProctorRowID (index) {

	index = "_"+index;
	var pIDs = selectedProctorIds.split("|");
	var pid = "";
	for (var i = 0; i < pIDs.length; i++) {
		
		pid = pIDs[i];
		if (pid.match(index) != null) {
			
			var end = pid.indexOf(index);
			var selectedRowID = pid.substring(0,end);
			return selectedRowID;
		}
	}
	
	return "-1";
	

}

function removeProctorByIndex (arrayIndex) {

	//proctorIdObjArray.splice(arrayIndex,1);
	proctorIdObjArray[arrayIndex]=null;

}


function updateRule (rule,index) {

	/*index = "_"+index;
	var pIDs = rule.split("|");
	var pid = "";
	for (var i = 0; i < pIDs.length; i++) {
		
		pid = pIDs[i];
		if (pid.match(index) != null) {
			
			pIDs[i] = "deleted"
			break;
			
		}
	}
	
	for (var i = 0; i < pIDs.length; i++) {
	
		pid = pIDs[i];
		
		if (i == 0) {
			rule = pid;
		} else {
			rule = rule +"|"+pid;
		}
		
	}*/
	var pIDs = rule.split("|");
	var preIndex = 0;
	var prePipePos = 0;
	var nxtPipePos = 0;
	var len = pIDs.length - 1;
	var part1 = "";
	var part2 = "";
	var flag = false;
	
	var lpid = pIDs[len];
	var pos = lpid.indexOf("_");
	var lindex = lpid.substring(pos + 1,lpid.length).split('_')[0];//added split to remove '_tmp';
	
	if (lindex == index) {
	
		flag = true;	
	}
	
	if (index > 0 && !flag) {
	
		preIndex = index - 1;
		
		var rule1 = rule.substring(0,rule.indexOf("_"+index+"|"));
		prePipePos = rule1.lastIndexOf("|");
		
		//prePipePos = rule.indexOf("|",rule.indexOf("_"+preIndex));
		nxtPipePos = rule.indexOf("|",rule.indexOf("_"+index+"|"));
		var part1 = rule.substring(0,(prePipePos));
		var part2 = rule.substring(nxtPipePos,rule.length);
		rule = part1 + part2;
	
	} else {
	
		preIndex = index - 1;
		//prePipePos = rule.indexOf("|",rule.indexOf("_"+preIndex));
		prePipePos = rule.lastIndexOf("|");
		part1 = rule.substring(0,(prePipePos));
		rule = part1;
	}
	
	return rule;
} 

function include(arr,obj) {
     //return (arr.indexOf(obj) != -1);
    var indx =  jQuery.inArray(obj, arr); 
     if(indx != -1)
      return true;
     else
      return false;
     
}

function confirmProctorRule () {

	/*var pIDs = selectedProctorIds.split("|");
	var pid = "";
	for (var i = 0; i < pIDs.length; i++) {
		
		pid = pIDs[i];
		if (pid.match("_tmp") != null) {
			
			pid = pid.replace("_tmp","");
			pIDs[i] = pid;
						
		}
	}
	
	for (var i = 0; i < pIDs.length; i++) {
	
		pid = pIDs[i];
		
		if (i == 0) {
			selectedProctorIds = pid;
		} else {
			selectedProctorIds = selectedProctorIds +"|"+pid;
		}
		
	}*/
	
	selectedProctorIds = selectedProctorIds.replace(/_tmp/gi,"");
	
}

function backProctorRule () {

	var pIDs = selectedProctorIds.split("|");
	var pid = "";
	for (var i = 0; i < pIDs.length; i++) {
		
		pid = pIDs[i];
		if (pid.match("_tmp") != null) {
			
			pIDs[i] = "deleted";
			removeProctorByIndex(i);
						
		} else {
		
			if (i == 0) {
				selectedProctorIds = pid;
			} else {
			
				if (pid !="deleted") {
			
					selectedProctorIds = selectedProctorIds +"|"+pid;
			
				}
			
			}
		
		}
	}
	
	/*for (var i = 0; i < pIDs.length; i++) {
	
		pid = pIDs[i];
		
		if (i == 0) {
			selectedProctorIds = pid;
		} else {
			
			if (pid !="deleted") {
			
				selectedProctorIds = selectedProctorIds +"|"+pid;
			
			}
			
		}
		
	}*/
}
	
	
function returnSelectedProctor() {

	confirmProctorRule();
	var val= [] ;
	delete tempOrgDataInform;
	delete tempAllSelectOrgProctor;
	delete tempProctorData;
	
	tempOrgDataInform = {};
	tempAllSelectOrgProctor= {};
	var jsonDataOrg = {};
	
	for (var i in proctorIdObjArray) {		
		var objstr = proctorIdObjArray[i];
		var jsonDataUser = {};
		for(var j in objstr){
			jsonDataUser[j] = objstr[j];
			val.push(objstr[j]);
		}
		jsonDataOrg[i] = jsonDataUser;	
		if(i != 'All') {
			if(allSelectOrgProctor[i]){
				tempAllSelectOrgProctor[i] = true;
			}else {
				tempAllSelectOrgProctor[i] = false;
			}
			var count = parseInt(orgDataInform[i]);
			tempOrgDataInform[i] = count;
		}
	}
	tempProctorData = jsonDataOrg;
	
	addProctorLocaldata = val;
	noOfProctorAdded = addProctorLocaldata.length;
	isOnBackProctor = true;
	$("#Proctor_Tab").css('display', 'block');
	$("#Select_Proctor_Tab").css('display', 'none');
	gridReloadProctor(false);
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
	//proctorIdObjArray = {};
	delProctorIdObjArray = [];
	isOnBackProctor = false;
	noOfProctorAdded = 0;
	proctorGridloaded = false;
	$('#listProctor').GridUnload();
	$('#selectProctor').GridUnload();
	allRowSelectedPro = false;
	allProctorIds = [];
	allSelectOrgProctor = {};
	//proctorIdObjArray = {};
	countAllSelectProctor = 0;
}

