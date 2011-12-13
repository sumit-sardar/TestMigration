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
var pdindex = 0;
var proctorIdObjArray = {};
var delProctorIdObjArray = [];
var isOnBackProctor = false;

var allProctorIds = [];
var allSelectOrgProctor = {};
var countAllSelectProctor = 0;

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

	backProctorRule();
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
 		    UIBlock();
 		    if(!selectProctorGridLoaded) {
 		        populateSelectProctorGrid();
			}
			else
				gridReloadSelectProctor();
		});
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

		   	jsonReader: { repeatitems : false, root:"userProfileInformation", id:"userId", records: function(obj) { userList = JSON.stringify(obj.userProfileInformation);
		   	
		   	if(obj.userProfileInformation != null && obj.userProfileInformation != undefined && obj.userProfileInformation.length > 0) {
		   	 	allProctorIds = [];
			   	 for(var i = 0; i <obj.userProfileInformation.length; i++) {
			   	 	allProctorIds.push(obj.userProfileInformation[i]);
			   	 }
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
				
				if(allRowSelectedPro) {
					var sessions  = jQuery('#selectProctor').jqGrid('getGridParam','selarrrow');	
					if(selectedSessionPro.length > 0) {
					var existlen = selectedSessionPro.length;
					for(var i=0; i<sessions.length; i++) {
						if(!include(selectedSessionPro, sessions[i])) {
							selectedSessionPro[existlen] = sessions[i] ;
							existlen++;
						}
					}
					 var allRowsInGrid = $('#selectProctor').jqGrid('getDataIDs');
					
					} else {
						selectedSessionPro = sessions;
					} 
				}
			},
			gridComplete: function() {
			
			var allRowChecked = false;
			if(allSelectOrgProctor != null && allSelectOrgProctor.length > 0) {
				for(var i = 0; i < allSelectOrgProctor.length; i++) {
					if(allSelectOrgProctor[i] != null && allSelectOrgProctor[i] == proctorForSelectedOrg)
						allRowChecked = true;
				}
			} 
			
			if(allRowChecked) { 
				 	$("#cb_selectProctor").attr("checked", true);
				 	$("#cb_selectProctor").trigger('click');
				 	$("#cb_selectProctor").attr("checked", true);
				 	allRowSelected = true;
				 	
				 	var allRowsInGrid = $('#selectProctor').jqGrid('getDataIDs');
				 	for(var i = 0; i < allRowsInGrid.length; i++) {
						var selectedRowData = $("#selectProctor").getRowData(allRowsInGrid[i]);
						if (selectedRowData.defaultScheduler == 'T') {
				 			$("#"+allRowsInGrid[i]+" td input").attr("disabled", true);
				 			$("#"+allRowsInGrid[i]).addClass('ui-state-disabled');
				 			
				 		}
				 	}
			 } 
			 
			 /*else {
			 	if(proctorForSelectedOrg != preSelectedOrgPro) {
				
					if(addProctorLocaldata != null && addProctorLocaldata.length > 0) {
						$('.cbox').attr('checked', false); 
						for(var i = 0; i < addProctorLocaldata.length; i++) {
							var proctorObj = addProctorLocaldata[i];
							if(proctorObj != null && proctorObj != undefined) {
								$("#"+proctorObj.userId+" td input").attr("checked", true);
								$("#"+proctorObj.userId).trigger('click');
								$("#"+proctorObj.userId+" td input").attr("checked", true); 
							} 					
						}
					} else { 
						$('.cbox').attr('checked', false); 
						if(isOnBackProctor) {
							hideSelectedProctor();
						}
					}
				 	
				 }
			 }*/
			 
			 else {
			 	var allRowsInGrid = $('#selectProctor').jqGrid('getDataIDs');
					var selectedRowData;
					for(var i = 0; i < allRowsInGrid.length; i++) {
						
						selectedRowData = $("#selectProctor").getRowData(allRowsInGrid[i]);
						//$("#"+allRowsInGrid[i]).unbind("clicked");
						if (selectedRowData.defaultScheduler == 'T') {
				 			$("#"+allRowsInGrid[i]+" td input").attr("disabled", true);
				 			$("#"+allRowsInGrid[i]).addClass('ui-state-disabled');
				 		}
				 		
				 		for (var j in proctorIdObjArray) {
						var objstr = proctorIdObjArray[j];
							if (objstr != null) {
								var key = getProctorRowID (j);
								if( key == allRowsInGrid[i]) {
									//$("#"+allRowsInGrid[i]+" td input").attr("checked", true);
									
									$("#"+allRowsInGrid[i]+" td input").attr("checked", false); 
									$("#"+allRowsInGrid[i]+" td input").unbind('click');
						
									//var isAlreadyChecked1 = $("#"+allRowsInGrid[i]).is(":checked");
									//var isAlreadyChecked2 = jQuery(this).find('#'+allRowsInGrid[i]+' input[type=checkbox]').prop('checked'); 
									
									$("#"+allRowsInGrid[i]+" td input").attr("checked", true); 
						 			$("#"+allRowsInGrid[i]+" td input").trigger('click'); 
						 			$("#"+allRowsInGrid[i]+" td input").attr("checked", true);
									
									break;
								}
							}
						}
					}
			 }
				
			},
			onSelectAll: function (rowIds, status) {
				
				//if(preSelectedOrgPro = proctorForSelectedOrg) {
					//selectedProctorIds = "";
					//pindex = 0;
					//proctorIdObjArray = [];
				//}
				
				if(status) {
					allRowSelectedPro = true;
					for(var i = 0; i < allProctorIds.length; i++) {
						if(getProctorIDIndex(allProctorIds[i].userId) < 0) {							
							if (allProctorIds[i].defaultScheduler == 'F') {
								var selectedRowData = allProctorIds[i];
								if (selectedProctorIds == "") {
										selectedProctorIds = allProctorIds[i].userId+"_"+pindex+"_tmp";										
								} else {
										selectedProctorIds = selectedProctorIds +"|"+allProctorIds[i].userId+"_"+pindex+"_tmp";
								}																
								proctorIdObjArray[pindex]=selectedRowData;
								pindex = pindex + 1;
					 		}
						}						
					}	
					
					
					// Added to handle multiple organization select All	
					var present = false;
					if(countAllSelectProctor > 0) {
						for(var i = 0; i < allSelectOrgProctor.length; i++) {
							if(allSelectOrgProctor[i] != null && allSelectOrgProctor[i] == proctorForSelectedOrg)
								present = true;
						}
						if(!present) {							
							allSelectOrgProctor[countAllSelectProctor] = proctorForSelectedOrg;
							countAllSelectProctor++;
						}
					} else {
						allSelectOrgProctor[countAllSelectProctor] = proctorForSelectedOrg;
						countAllSelectProctor++;
					}			
				} else {
					allRowSelectedPro = false;	
													
					for(var i = 0; i < allProctorIds.length; i++) {
						if (allProctorIds[i].defaultScheduler == 'F') {
							var selectedRowData = allProctorIds[i];							
							var indx = getProctorIDIndex(selectedRowData.userId);							
							removeProctorByIndex(indx); 							
							selectedProctorIds = updateRule(selectedProctorIds,indx);							
						}						
					}
					
					for(var i = 0; i < allSelectOrgProctor.length; i++) {
						if(allSelectOrgProctor[i] != null && allSelectOrgProctor[i] == proctorForSelectedOrg)
							allSelectOrgProctor[i] = null;
					}									
				}
			},
			onSelectRow: function (rowid, status) {
				var selectedRowId = rowid;
				var alreadyExists = false;

				for (var j in proctorIdObjArray) {
					var existingSelectedRowId = getProctorRowID (j);
					if( existingSelectedRowId == selectedRowId){
						alreadyExists = true;
						break;
					}
					
				}
				
				if(status && !alreadyExists) {
					var selectedRowData = $("#selectProctor").getRowData(selectedRowId);
					proctorIdObjArray[pindex]=selectedRowData;
					
					if (selectedProctorIds == "") {
							selectedProctorIds = selectedRowId+"_"+pindex+"_tmp";
							pindex++;
					} else {
							selectedProctorIds = selectedProctorIds +"|"+selectedRowId+"_"+pindex+"_tmp";
							pindex++;
					}
					
				} else if (!status && alreadyExists){
				
					var indx = getProctorIDIndex(selectedRowId);
					removeProctorByIndex(indx); 
					selectedProctorIds = updateRule(selectedProctorIds,indx);
										
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

function getProctorIDIndex(selectedRowId) {

	var pIDs = selectedProctorIds.split("|");
	var pid = "";
	for (var i = 0; i < pIDs.length; i++) {		
		pid = pIDs[i];
		if (pid.match(selectedRowId) != null) {
			return i;
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

	index = "_"+index;
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

	var pIDs = selectedProctorIds.split("|");
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
		
	}
}

function backProctorRule () {

	var pIDs = selectedProctorIds.split("|");
	var pid = "";
	for (var i = 0; i < pIDs.length; i++) {
		
		pid = pIDs[i];
		if (pid.match("_tmp") != null) {
			
			pIDs[i] = "deleted";
			removeProctorByIndex(i);
						
		}
	}
	
	for (var i = 0; i < pIDs.length; i++) {
	
		pid = pIDs[i];
		
		if (i == 0) {
			selectedProctorIds = pid;
		} else {
			selectedProctorIds = selectedProctorIds +"|"+pid;
		}
		
	}
}
	
	
function returnSelectedProctor() {

	confirmProctorRule();
	var val= [] ;
	for (var i in proctorIdObjArray) {
		
		var objstr = proctorIdObjArray[i];
		
		if (objstr != null) {			
			var key = getProctorRowID (i);
			objstr['userId'] = key;
			val.push(objstr);
		
		}
		
	}
	
	addProctorLocaldata = val;
	noOfProctorAdded = addProctorLocaldata.length;
	hideSelectedProctor();
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
	selectedProctorIds = "";
	deletedProctorIds = "";
	pindex = 0;
	pdindex = 0;
	proctorIdObjArray = {};
	delProctorIdObjArray = [];
	isOnBackProctor = false;
	noOfProctorAdded = 0;
	proctorGridloaded = false;
	$('#listProctor').GridUnload();
	$('#selectProctor').GridUnload();
	allRowSelectedPro = false;
	allProctorIds = [];
	allSelectOrgProctor = {};
	countAllSelectProctor = 0;
}

