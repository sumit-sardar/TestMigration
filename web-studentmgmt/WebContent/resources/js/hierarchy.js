
var gridjsondata;
var idarray =[];
var gridloaded = false;

var gradeOptions = []; 
var genderOptions = []; 
var dayOptions = []; 
var yearOptions = []; 
var monthOptions = []; 
var testPurposeOptions = [];
var orgTreeHierarchy;
var assignedOrgNodeIds = "";
var customerDemographicValue = "";
var isValueChanged = false;
var leafNodeCategoryId;
var SelectedOrgNodeId;
var SelectedOrgNode;
var SelectedOrgNodes = [];
var optionList = [];
var isAddStudent = true;
var stuDemographic ;
var stuAccommodation;
var loginId = "";
var profileEditable = "true";
var isPopUp = false;
var dbStudentDetails;
var organizationNodes = [];
var optionHtml ="";
var requetForStudent  = "";
var isViewStudent = false;
var isAction = true;
var currentNodeId ;
var currentCategoryLevel;
var currentTreeArray;
var currentIndex;
var dataObj2=[];
var dataObj3=[];
var dataObj4;
var dataObj5;
var dataObj6;
var dataObj7;
var dataObj8;
var rootMap = {};
var jsonData;
var map = new Map();
var rootNode = [];
var checkedListObject = {};
var type;
var asyncOver = 0;
var leafParentOrgNodeId = "";
var isBulkMove = false;
var rootNodeId;
var stuThreshold = 10000;	
var topOrgNodeStuCount = 0;				


$(document).bind('keydown', function(event) {
		
	      var code = (event.keyCode ? event.keyCode : event.which);
	      if(code == 27){
	      		if(isPopUp){	
	      			if(!isViewStudent){
	      			onCancel();
	      			}
	      		}
	            return false;
	      }
	  });


function UIBlock(){
	$.blockUI({ message: '<img src="/StudentWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}


			
function populateTree() {
	isBulkMove = false;
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'userOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						//alert('in');
						$.unblockUI();  
						leafNodeCategoryId = data.leafNodeCategoryId;
						orgTreeHierarchy = data;
						jsonData = orgTreeHierarchy.data;
						getRootNodeDetails();
						createSingleNodeSelectedTree (orgTreeHierarchy);
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


function overlayblockUI(){	
	$("body").append('<div id="blDiv" style="opacity: 0.6; background-color: #d0e5f5;position: absolute;top:0;left:0;width:100%;height:100%;z-index:10"></div>');
	$("#blDiv").css("cursor","wait");		
						
}

function overlayunblockUI(){
	$("#blDiv").css("cursor","normal");
	$("#blDiv").remove(); 
}



function createSingleNodeSelectedTree(jsondata) {
	   $("#orgNodeHierarchy").jstree({
	        "json_data" : {	             
	            "data" : rootNode,
				"progressive_render" : true,
				"progressive_unload" : true
	        },
            "themes" : {
			    "theme" : "apple",
			    "dots" : false,
			    "icons" : false
			},       
	        "ui" : {  
	           "select_limit" : 1
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
	    	//SelectedOrgNode = $(this).parent();
	    	//SelectedOrgNodes = SelectedOrgNode.parentsUntil(".jstree","li");
	    	//clear message before moving to pther node
	    	clearMessage();
  			SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#treeOrgNodeId").val(SelectedOrgNodeId);
 		    
	 		if(parseInt(rootNodeId) == parseInt(SelectedOrgNodeId)){
	 			var postDataObject = {};
	 			postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
	 		   $.ajax({
					async:		true,
					beforeSend:	function(){									
									UIBlock();
								},
					url:		'getStudentCountForOrgNode.do', 
					type:		'POST',
					data:		 postDataObject,
					dataType:	'json',
					success:	function(data, textStatus, XMLHttpRequest){	
									$.unblockUI();
									topOrgNodeStuCount = data;
									if(parseInt(topOrgNodeStuCount) > stuThreshold){
										showPopup(topOrgNodeStuCount);
									}else{
										showGrid();
									}
								},
					error  :    function(XMLHttpRequest, textStatus, errorThrown){
									$.unblockUI();  
									window.location.href="/SessionWeb/logout.do";
									
								}
				});		
	 		 }else{
	 		    showGrid();
	 		} 		  		
				
		});
		
	registerDelegate("orgNodeHierarchy");		
	   
}

function showGrid(){
	UIBlock();
	if(!gridloaded) {
  		gridloaded = true;
  		populateTreeSelect();
  		var hideAccommodation = $("#supportAccommodations").val();
  		if(customerDemographicValue == "")
  			customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
  		if(hideAccommodation == "false")
  			populateGridWithoutAccommodation();
  		else
		populateGrid();
	}
	else
		gridReload();
}

function showPopup(stuCount){
	var msg = $("#stuCountId").val();
	var countMsg = msg.replace("XXXX",stuCount);
	
	$("#rootNodePopup").dialog({  
			title:'Alert',  
			resizable:false,
			autoOpen: true,
			modal: true,
			closeOnEscape: false,
			open: function(event, ui) {
				$("#exceedMsg").text(countMsg);
				$(".ui-dialog-titlebar-close").hide(); 
			}
	});
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
			//currentCategoryLevel = currentCategoryLevel - (rootCategoryLevel -1);			
		//console.log("currentCategoryLevel" + currentCategoryLevel);
	
		if (classState == false){
			if (currentCategoryLevel == 1) {	
			dataObj2 = [];	
			var indexOfRoot = getIndexOfRoot(currentNodeId);
			populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot);
			//console.log("category Level action");
			}
	
			var cacheData = map.get(currentNodeId);
			if (cacheData != null){
				currentTreeArray = cacheData;			
			}
			if (cacheData == null){
			//console.log("before swuitch");
				switch(currentCategoryLevel){
					
					//Not caching at initial level because the whole data will be put in cache which may increase the cache size
					//considerably
					
					case "2": 	dataObj3 =getObject(jsonData,currentNodeId,currentCategoryLevel,x.parentNode.parentNode.id);
								currentIndex = dataObj3.index;
								currentTreeArray = dataObj3.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//console.log("case2");
								
								//leafClass = 'jstree-leaf';
							break;
							
					case "3": 	dataObj4 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj4,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//console.log("case3");
							break;
					case "4": 	dataObj5 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj5,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//console.log("case4");
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

	function clearMessage(){
		document.getElementById('displayMessage').style.display = "none";	
		document.getElementById('displayMessageMain').style.display = "none";	
	  			
	}
	
function createMultiNodeSelectedTree(jsondata) {

	
	
 	$("#innerID").jstree({
	        "json_data" : {	             
	            "data" : rootNode,
				"progressive_render" : true,
				"progressive_unload" : true
				
	        },
	         "checkbox" : {
        "two_state" : true
        },  
	            "themes" : {
			    "theme" : "apple",
			    "dots" : false,
			    "icons" : false
			},       
	 			"plugins" : [ "themes", "json_data", "ui", "checkbox"]
	   });	   
	   	$("#innerID").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel != leafNodeCategoryId) {
						$("#innerID ul li").eq(i).find('a').find('.jstree-checkbox:first').hide();
		    		} else {
		    			$("#innerID ul li").eq(i).find('.jstree-icon').hide();
		    		}
		    		if(profileEditable === "false"  && $("#classReassignable").val() === "true") {
		    			$("#innerID ul li").eq(i).find('a').find('.jstree-checkbox:first').hide();
		    		}
				}
			}
		);
			registerDelegate("innerID");
		
		$("#innerID").delegate("li a","click", 
			function(e) {
				if(profileEditable === "false"  && $("#classReassignable").val() === "true") {
					return true;
				}
				styleClass = $(this.parentNode).attr('class');
				var orgcategorylevel = $(this.parentNode).attr("cid");
				var elementId = $(this.parentNode).attr('id');
				var currentlySelectedNode ="";
				isexist = false;
				currentId = $(this.parentNode).attr("id");
				var element = this.parentNode;
    			if(orgcategorylevel == leafNodeCategoryId) {
    				if ($(element).hasClass("jstree-checked")){
						$(element).removeClass("jstree-checked").addClass("jstree-unchecked");
						checkedListObject[element.id] = "unchecked" ;
					}else{
						$(element).removeClass("jstree-unchecked").addClass("jstree-checked");
						checkedListObject[element.id] = "checked" ;
						}
				var isChecked = $(element).hasClass("jstree-checked");
    			updateOrganization(this.parentNode,isChecked);
    			}
			}
			);
			
			
			$("#innerID").bind("change_state.jstree",
		  		function (e, d) { 
			  		if(isAction){
			    	var orgcategorylevel = d.rslt[0].getAttribute("cid");
			    	var elementId = d.rslt[0].getAttribute("id");
			    	var currentlySelectedNode="";
					var isChecked = $(d.rslt[0]).hasClass("jstree-checked");
					//console.log("isChecked" + isChecked);
					if (isChecked){
					checkedListObject[elementId] = "checked" ;
					}else{					
					checkedListObject[elementId] = "unchecked" ;
					}
					if(orgcategorylevel == leafNodeCategoryId) {
						updateOrganization(d.rslt[0],isChecked);
					}
    			}
			}
			);
			
		
		
}


function updateOrganization(element, isChecked){
	
	
		
	  	if($(element).attr("cid") == leafNodeCategoryId){
	  		var currentlySelectedNode ="";
			isexist = false;
			currentId = $(element).attr("id");
			if(!isAddStudent){
				if(assignedOrgNodeIds != "") {
						if(String(assignedOrgNodeIds).indexOf(",") > 0) {
					  			var orgList = assignedOrgNodeIds.split(",");
					  			for(var key=0; key<orgList.length; key++){
					  				if(trim(orgList[key]) == currentId){
					  					isexist = true; 
					  					break; 
					  				}
					  			}
					  		} else {
					  			if(assignedOrgNodeIds == currentId){
					  				isexist = true;  
					  			}
					  		}
					}
				if(isChecked) {
					if(!isexist) {
						currentlySelectedNode = optionHtml;
						if(currentlySelectedNode=="") {
							currentlySelectedNode += "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ trim($(element).text())+"</a>";	
						} else {
							currentlySelectedNode = currentlySelectedNode + " , " + "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ trim($(element).text())+"</a>";
						}
						if(assignedOrgNodeIds=="") {
							assignedOrgNodeIds = $(element).attr("id");
						} else {
							assignedOrgNodeIds = $(element).attr("id") +"," + assignedOrgNodeIds; 
						}
						optionHtml = currentlySelectedNode;
					} else{
						currentlySelectedNode = optionHtml;
					}
					
				} else {
				if(isexist){
					currentlySelectedNode ="";
						//var newcurrentlySelectedNode = "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(this).attr("id")+"');>"+ trim($(this).text())+"</a>";
						 var nodeId = trim($(element).attr("id"));	
						var opList = optionHtml.split(",");
						for(var key=0; key<opList.length; key++){
							var opListVal = $.trim(opList[key]);
							 var resLen = opListVal.split(nodeId);
						//	if($.trim(newcurrentlySelectedNode) != opListVal){
						 	if(resLen.length < 2) {
								if(currentlySelectedNode=="") {
								currentlySelectedNode = opListVal;	
								} else {
								currentlySelectedNode = opListVal + " , " + currentlySelectedNode;
							}
							}
							
						}
						optionHtml = currentlySelectedNode;
					var newassignedOrgNodeIds ="";
						if(assignedOrgNodeIds != ""){
							if(String(assignedOrgNodeIds).indexOf(",") > 0) {
							var orgList = assignedOrgNodeIds.split(",");
								for(var key=0; key<orgList.length; key++){
								var orgListVal = $.trim(orgList[key]);
								 var esLen = orgListVal.split(nodeId);
									//if(orgListVal != currentId){
									if(esLen.length < 2) {
										if(newassignedOrgNodeIds=="") {
											newassignedOrgNodeIds = orgListVal;
										} else {
											newassignedOrgNodeIds = orgListVal +"," + newassignedOrgNodeIds; 
										}
									}
									
								}
							}
							 if(newassignedOrgNodeIds == "") {
								assignedOrgNodeIds = "";
							} else {
								assignedOrgNodeIds = newassignedOrgNodeIds;
							}
						}
					} else {
						currentlySelectedNode = optionHtml;
					}
				}
			}
			if(isAddStudent){
				var currentlySelectedNode ="";
				assignedOrgNodeIds = "";
				$("#innerID").find(".jstree-checked").each(function(i, element){
					
					var orgcategorylevel = $(element).attr("cid");
						if(orgcategorylevel == leafNodeCategoryId) {
							if(currentlySelectedNode=="") {
								currentlySelectedNode += "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ trim($(element).text())+"</a>";	
							} else {
								currentlySelectedNode = currentlySelectedNode + " , " + "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ trim($(element).text())+"</a>";
							}
			
				    		if(assignedOrgNodeIds=="") {
								assignedOrgNodeIds = $(element).attr("id");
							} else {
								assignedOrgNodeIds = $(element).attr("id") +"," + assignedOrgNodeIds; 
							}
			    		}
					});
			}
			
				if(currentlySelectedNode.length > 0 ) {
					$("#notSelectedOrgNodes").css("display","none");
					$("#selectedOrgNodesName").html(currentlySelectedNode);
					//$("#selectedOrgNodesName").text(currentlySelectedNode);	
				} else {
					$("#notSelectedOrgNodes").css("display","inline");
					$("#selectedOrgNodesName").text("");	
				}
		
			}

}
		function populateTreeSelect() {
			$("#notSelectedOrgNodes").css("display","inline");
			$("#selectedOrgNodesName").text("");	
			$("#innerID").undelegate();
			$("#innerID").unbind();
			createMultiNodeSelectedTree (orgTreeHierarchy);
			
		}




	
	  function gridReload(){ 
	  	   resetSearchCrit();
	  	   var postDataObject = {};
 			postDataObject.q = 2;
 			postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
           jQuery("#list2").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});     
     	   //var sortArrow = jQuery("#list2");
           jQuery("#list2").jqGrid('setGridParam', {url:'getStudentForSelectedOrgNodeGrid.do',postData:postDataObject,page:1}).trigger("reloadGrid");
           jQuery("#list2").sortGrid('lastName',true,'asc');
         	//For MQC Defect - 67122
           //var arrowElements = sortArrow[0].grid.headers[0].el.lastChild.lastChild;
           //$(arrowElements.childNodes[0]).removeClass('ui-state-disabled');
           //$(arrowElements.childNodes[1]).addClass('ui-state-disabled');

      }

	function resetSearchCrit(){
		$("#searchUserByKeywordInput").val('');
		var grid = $("#list2");
	    grid.jqGrid('setGridParam',{search:false});	
	    var postData = grid.jqGrid('getGridParam','postData');
	    $.extend(postData,{filters:""});

	}
		
			
	function populateGrid() {
	
		//$("#searchresultheader").css("visibility","visible");	
		resetSearchCrit();
		var studentIdTitle = $("#studentIdLabelName").val();
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
        $("#list2").jqGrid({         
          url:'getStudentForSelectedOrgNodeGrid.do', 
		 mtype:   'POST',
		 postData: postDataObject,
		 datatype: "json",         
          colNames:[$("#jqgLastNameID").val(),$("#jqgFirstNameID").val(), $("#jqgMiddleIniID").val(), $("#jqgGradeID").val(),$("#jqgOrgID").val(), $("#jqgGenderID").val(), $("#jqgAccoID").val(), $("#jqgLoginID").val(), studentIdTitle],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:110, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:110, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:30, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:50, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr', width:110, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'gender',index:'gender', width:75, editable: true, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations', width:120, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userName',index:'userName',editable: true, width:150, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentNumber',index:'studentNumber',editable: true, width:150, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",
		   	records: function(obj) { 
		   //	studentList = obj.studentProfileInformation;
		   	idarray = obj.studentIdArray.split(",");
		   	return obj.studentProfileInformation.length; } },
		   	
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: $("#jqGrid-content-section").width(), 
			editurl: 'getStudentForSelectedOrgNodeGrid.do',
			ondblClickRow: function(rowid) {viewEditStudentPopup();},
			caption:$("#stuCapId").val(),
			onPaging: function() {
				clearMessage();
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
			onSelectRow: function () {
				clearMessage();
			},
			loadComplete: function () {
				if ($('#list2').getGridParam('records') === 0) {
            	$('#sp_1_pager2').text("1");
            	}
				$.unblockUI();  
				$("#list2").setGridParam({datatype:'local'});
				var tdList = ("#pager2_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
			//jQuery("#list2").jqGrid('navGrid','#pager2',{});  
			jQuery("#list2").navGrid('#pager2',{
				search: false,add:false,edit:false,del:false 	
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchUserByKeyword").dialog({  
						title:$("#searchStudentID").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
					 	});
			    }, position: "one-before-last", title:"Search Student", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#pager2",{position: "first"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-trash", onClickButton:function(){
			    	var rowid = $("#list2").jqGrid('getGridParam', 'selrow');
			    	if(rowid == ''|| rowid == null){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 220,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		requetForStudent = "";
		    		 	deleteStudentPopup();
			    	}
			    	
			    }, position: "first", title:"Delete Student", cursor: "pointer",id:"del_list2"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-pencil", onClickButton:function(){
			    	var rowid = $("#list2").jqGrid('getGridParam', 'selrow');
			    	if(rowid == ''|| rowid == null){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 220,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		 requetForStudent = "";
		    		 	 viewEditStudentPopup();
			    	}
			    	 
			    }, position: "first", title:"View/Edit Student", cursor: "pointer",id:"edit_list2"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-plus", onClickButton:function(){
			    	 requetForStudent = "";
		    		AddStudentDetail();
			    }, position: "first", title:"Add Student", cursor: "pointer",id:"add_list2"
			}); 
			
			jQuery(".ui-icon-refresh").bind("click",function(){
				$("#searchUserByKeywordInput").val('');
			}); 
			
		setupButtonPerUserPermission();
					
	}
	
	function searchUserByKeyword(){
		 var searchFiler = $.trim($("#searchUserByKeywordInput").val()), f;
		 var grid = $("#list2"); 
		 
		 if (searchFiler.length === 0) {
			 grid[0].p.search = false;
		 }else {
		 	 f = {groupOp:"OR",rules:[]};
			 f.rules.push({field:"lastName",op:"cn",data:searchFiler});
			 f.rules.push({field:"firstName",op:"cn",data:searchFiler});
			 f.rules.push({field:"middleName",op:"cn",data:searchFiler});
			 f.rules.push({field:"grade",op:"cn",data:searchFiler});
			 f.rules.push({field:"orgNodeNamesStr",op:"cn",data:searchFiler});
			 f.rules.push({field:"gender",op:"cn",data:searchFiler});
			 f.rules.push({field:"hasAccommodations",op:"cn",data:searchFiler}); 
			 f.rules.push({field:"userName",op:"cn",data:searchFiler});
			 f.rules.push({field:"studentNumber",op:"cn",data:searchFiler});  
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
	
	function populateGridWithoutAccommodation() {
	//$("#searchresultheader").css("visibility","visible");	
	resetSearchCrit();
	var studentIdTitle = $("#studentIdLabelName").val();
	var postDataObject = {};
 	postDataObject.q = 2;
 	postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
         $("#list2").jqGrid({         
          url:'getStudentForSelectedOrgNodeGrid.do', 
		 mtype:   'POST',
		 datatype: "json",
		 postData:	postDataObject,
          colNames:[$("#jqgLastNameID").val(),$("#jqgFirstNameID").val(), $("#jqgMiddleIniID").val(), $("#jqgGradeID").val(),$("#jqgOrgID").val(), $("#jqgGenderID").val(), $("#jqgAccoID").val(), $("#jqgLoginID").val(), studentIdTitle],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:120, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:120, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:30, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:50, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr', width:150, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'gender',index:'gender', width:80, editable: true, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userName',index:'userName',editable: true, width:200, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentNumber',index:'studentNumber',editable: true, width:170, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: space:nowrap;' } }
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",
		   	records: function(obj) { 
		   	//studentList = JSON.stringify(obj.studentProfileInformation);
		   	idarray = obj.studentIdArray.split(",");
		   	return obj.studentProfileInformation.length; } },
		   	 
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: $("#jqGrid-content-section").width(), 
			editurl: 'getStudentForSelectedOrgNodeGrid.do',
			ondblClickRow: function(rowid) {viewEditStudentPopup();},
			caption:$("#stuCapId").val(),
			onPaging: function() {
				clearMessage();
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
			loadComplete: function () {
				if ($('#list2').getGridParam('records') === 0) {
            	$('#sp_1_pager2').text("1");
            	}
				$.unblockUI();  
				$("#list2").setGridParam({datatype:'local'});
				var tdList = ("#pager2_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				 
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
	 });
			//jQuery("#list2").jqGrid('navGrid','#pager2',{});  
			jQuery("#list2").navGrid('#pager2',{
				search: false,add:false,edit:false,del:false    	
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchUserByKeyword").dialog({  
						title:$("#searchStudentID").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
					 	});
			    }, position: "one-before-last", title:"Search Student", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#pager2",{position: "first"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-trash", onClickButton:function(){
			    	var rowid = $("#list2").jqGrid('getGridParam', 'selrow');
			    	if(rowid == ''|| rowid == null){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 220,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		requetForStudent = "";
		    		 	deleteStudentPopup();
			    	}
			    	
			    }, position: "first", title:"Delete Student", cursor: "pointer",id:"del_list2"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-pencil", onClickButton:function(){
			    	var rowid = $("#list2").jqGrid('getGridParam', 'selrow');
			    	if(rowid == ''|| rowid == null){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 220,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		 requetForStudent = "";
		    		 	 viewEditStudentPopup();
			    	}
			    	 
			    }, position: "first", title:"View/Edit Student", cursor: "pointer",id:"edit_list2"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-plus", onClickButton:function(){
			    	 requetForStudent = "";
		    		AddStudentDetail();
			    }, position: "first", title:"Add Student", cursor: "pointer",id:"add_list2"
			});
			
			jQuery(".ui-icon-refresh").bind("click",function(){
				$("#searchUserByKeywordInput").val('');
			});
	}
	


function AddStudentDetail(){
isAddStudent = true;
isPopUp = true;
document.getElementById('displayMessage').style.display = "none";	
document.getElementById('displayMessageMain').style.display = "none";	
	if(!(gradeOptions.length > 0 
		&& genderOptions.length > 0
			&& dayOptions.length > 0
				&& monthOptions.length > 0 
					&& yearOptions.length > 0)){
					
 		var postDataObject = {};
 		postDataObject.isLasLinkCustomer = $("#isLasLinkCustomer").val();
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						
						UIBlock();
					},
		url:		'getOptionList.do', 
		type:		'POST',
		data:		postDataObject,
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						//alert('in');
						$.unblockUI();
						//UIBlock();
						//overlayblockUI(); 
						gradeOptions = data.gradeOptions;
						genderOptions = data.genderOptions;
						dayOptions = data.dayOptions; 
						monthOptions = data.monthOptions;
						yearOptions = data.yearOptions; 
						testPurposeOptions = data.testPurposeOptions;
						fillDropDown("gradeOptions", gradeOptions);
						fillDropDown("genderOptions", genderOptions);
						fillDropDown("dayOptions", dayOptions);
						fillDropDown("monthOptions", monthOptions);
						fillDropDown("yearOptions", yearOptions);
						if($("#isLasLinkCustomer").val() =="true")
							fillDropDown("testPurposeOptions", testPurposeOptions);
						//customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
						
						$("#addEditStudentDetail").dialog({  
													title:$("#addStuID").val(),  
												 	resizable:false,
												 	autoOpen: true,
												 	width: '800px',
												 	modal: true,
												 	closeOnEscape: false,
												 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		 											});	
						$('#addEditStudentDetail').bind('keydown', function(event) {
							
 							var code = (event.keyCode ? event.keyCode : event.which);
 							if(code == 27){
		  				  	onCancel();
		  				 	return false;
		 				 }
		 				
						});
						setPopupPosition(isAddStudent);	
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
		
	});

	} else {
		$('#Student_Information :checkbox').attr('disabled', false); 
		$('#Student_Information :radio').attr('disabled', false); 
		$('#Student_Information select').attr('disabled', false);
		$('#Student_Information :input').attr('disabled', false);
		
		$("#studentFirstName").val(""); 
		$("#studentMiddleName").val(""); 
		$("#studentLastName").val(""); 
		$("#studentExternalId").val(""); 
		$("#studentExternalId2").val(""); 
						
		reset();
		if($("#isLasLinkCustomer").val() =="true")
			fillDropDown("testPurposeOptions", testPurposeOptions);
		$("#addEditStudentDetail").dialog({  
			title:$("#addStuID").val(),  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '800px',
		 	modal: true,
		 	closeOnEscape: false,
		 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		 	});	
		 	$('#addEditStudentDetail').bind('keydown', function(event) {
		 						  //alert("key up 2");
 							var code = (event.keyCode ? event.keyCode : event.which);
 							if(code == 27){
		  				  	onCancel();
		  				 	return false;
		 				 }
		 				
						});
		setPopupPosition(isAddStudent);	
	}	
	
}

	function setPopupPosition(isAddStudent){
				var toppos = ($(window).height() - 610) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#addEditStudentDetail").parent().css("top",toppos);
				$("#addEditStudentDetail").parent().css("left",leftpos);		 	 
				$("#viewStudentDetail").parent().css("top",toppos);
				$("#viewStudentDetail").parent().css("left",leftpos);		 	 
				$("#view_Student_Accommodation_Information").css("overflow",'auto');
				if(isAddStudent) {
					$("#preButton").css("visibility","hidden");	
					$("#nextButton").css("visibility","hidden");
					$("#viewPreButton").css("visibility","hidden");	
					$("#viewNextButton").css("visibility","hidden");
				} else {
					$("#preButton").css("visibility","visible");	
					$("#nextButton").css("visibility","visible");
					$("#viewPreButton").css("visibility","visible");	
					$("#viewNextButton").css("visibility","visible");
					
				}
	}
	
    function reset() {
    	assignedOrgNodeIds = "";
    	
       jQuery.each(customerDemographicValue, function(i, field){         
      		$("#"+field.name).val(field.value);
      		
       });  
       var customerValCheckbox = [];
	   var counter=0;
       var checkBoxfields = $(":checkbox"); 
	       for(var i=0; i < checkBoxfields.length; i++){
		       for(var j=0; j<customerDemographicValue.length; j++) {
			       	if($(checkBoxfields).eq(i).attr('name') == customerDemographicValue[j].name) {
			       			customerValCheckbox[counter] =  customerDemographicValue[j].name;
							counter++;
					}
		       	}
			} 
			if(customerValCheckbox.length > 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
			       for(var j=0; j<customerValCheckbox.length; j++) {
				       	if(!isExist( $(checkBoxfields).eq(i).attr('name') ,customerValCheckbox)) { //
				       			$(checkBoxfields).eq(i).attr('checked', false); 
				    	} else {
				    			$(checkBoxfields).eq(i).attr('checked', true); 
				    	}
			       	}
				} 
			}
			if(customerValCheckbox.length == 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
					$(checkBoxfields).eq(i).attr('checked', false); 
				}
			}
		var radiofields = $(":radio"); 
       for (var i=0; i<radiofields.length; i++) {
			if (radiofields[i].value== "None") { 
				radiofields[i].checked = true;
			}
		}
		//change for defect #67004
		var allSelects = $("#addEditStudentDetail select");
		for(var count = 0; count < allSelects.length ; count++) {
			$(allSelects[count]).find("option:eq(0)").attr("selected","true");
		}
		
		enableColorSettingsLink("false");
		enableAudioFiles();
		
   }


			

function fillDropDown( elementId, optionList) {
	var ddl = document.getElementById(elementId);
	var optionHtml = "";
	for(var i = 0; i < optionList.length; i++) {
		optionHtml += "<option  value='"+optionList[i]+"'>"+optionList[i]+"</option>";	
	}
	$(ddl).html(optionHtml);
	
}

function fillselectedOrgNode( elementId, orgList) {
	var ddl = document.getElementById(elementId);
	optionHtml = "";
	assignedOrgNodeIds = "";
	for(var i = 0; i < orgList.length; i++) {
		if(assignedOrgNodeIds == "") {
			assignedOrgNodeIds = orgList[i].orgNodeId;
		} else {
			assignedOrgNodeIds = assignedOrgNodeIds + "," + orgList[i].orgNodeId; 
		}
		if(optionHtml == "") {
			optionHtml += "<a style='color: blue;text-decoration:underline'  href=javascript:openTreeNodes('"+orgList[i].orgNodeId+"');>"+trim(orgList[i].orgNodeName)+"</a>";	
		} else {
			optionHtml = optionHtml +" , " + "<a style='color: blue;text-decoration:underline'  href=javascript:openTreeNodes('"+orgList[i].orgNodeId+"');>"+ trim(orgList[i].orgNodeName)+"</a>";	
		}
		
	}
	$(ddl).html(optionHtml);
	
	if(orgList.length > 0 ) {
		$("#notSelectedOrgNodes").css("display","none");
	} else {
		$("#notSelectedOrgNodes").css("display","inline");
		$("#selectedOrgNodesName").text("");	
	}
}

	function closePopUp(dailogId){
	//alert("closePopupfired: " + dailogId);
	 	
		if(dailogId == 'addEditStudentDetail') {
			$('#accordion').accordion('activate', 0 );
			$("#Student_Information").scrollTop(0);
			$("#Student_Additional_Information").scrollTop(0);
			$("#Student_Accommodation_Information").scrollTop(0);
			$('#Student_Additional_Information').hide();
			$('#Student_Accommodation_Information').hide();
			//populateTreeSelect();
			$('#innerID').jstree('close_all', -1);
			$("#notSelectedOrgNodes").css("display","inline");
			$("#selectedOrgNodesName").text("");	
			isPopUp = false;
			checkedListObject = {};
			$('#innerID').jstree('uncheck_all');
		}
		
		if(dailogId == 'viewStudentDetail') {
			$('#viewaccordion').accordion('activate', 0 );
			$("#view_Student_Information").scrollTop(0);
			$("#view_Student_Additional_Information").scrollTop(0);
			$("#view_Student_Accommodation_Information").scrollTop(0);
			$('#view_Student_Additional_Information').hide();
			$('#view_Student_Accommodation_Information').hide();
			//populateTreeSelect();
			isPopUp = false;
			isViewStudent = false;		
			checkedListObject = {};
			$('#innerID').jstree('uncheck_all');	
		}
			
		if(dailogId == 'confirmationPopup') {
		//	$('#accordion').accordion('activate', 0 );
		//	$("#Student_Information").scrollTop(0);
		//	$("#Student_Additional_Information").scrollTop(0);
		//	$("#Student_Accommodation_Information").scrollTop(0);
		//	$('#Student_Additional_Information').hide();
		//	$('#Student_Accommodation_Information').hide();
		//	$('#studentFirstName').trigger("focus");		
		}
		if(dailogId == 'confirmationPopupNavigation') {
		//	$('#accordion').accordion('activate', 0 );
		//	$("#Student_Information").scrollTop(0);
		//	$("#Student_Additional_Information").scrollTop(0);
		//	$("#Student_Accommodation_Information").scrollTop(0);
		//	$('#Student_Additional_Information').hide();
		//	$('#Student_Accommodation_Information').hide();
		//	$('#studentFirstName').trigger("focus");
			requetForStudent = "";				
		}
		if(dailogId == 'AssignAccommPopup') {
			resetRadioAccommodation();
			resetBulk();
		}
		
		$("#"+dailogId).dialog("close");
		 
	}
	
	function closeConfirmationPopup() {
		if(isAddStudent){
			closePopUp('confirmationPopup');
			closePopUp('confirmationPopupNavigation');
			closePopUp('addEditStudentDetail');
		} else {
			
			//added temp variable here becasue requetForStudent is getting reset in closePopUp func. Need to handle this more elegantly.
			var navFlow = requetForStudent;
			closePopUp('confirmationPopup');
			closePopUp('confirmationPopupNavigation');
			if(navFlow == "Next"){
				 fetchNextData('Edit');
			} else if(navFlow == "Previous"){
				fetchPreviousData('Edit');
			} else{
				closePopUp('addEditStudentDetail');
			}
			
		}
	}
	
	function onCancel() {
		isValueChanged = false;

	if(isAddStudent){
		  if($("#studentFirstName").val()!= "" 
			 || $("#studentMiddleName").val()!= ""
			 || $("#studentLastName").val()!= ""
			 || $("#studentExternalId").val()!= ""
		     || $("#studentExternalId2").val()!= ""
			 || $("#genderOptions").val() != "Select a gender"
			 || $("#gradeOptions").val() != "Select a grade" ) {
			 isValueChanged = true;	
			 }
		 if(!isValueChanged) {	
			 var radiofields = $("#addEditStudentDetail :radio"); 
	       	 for (var i=0; i<radiofields.length; i++) {
			   	if (radiofields[i].value != "None" && radiofields[i].checked == true && radiofields[i].checked != ""  && radiofields[i].disabled == false && radiofields[i].getAttribute("disabled") == null) { 
					isValueChanged = true;
					break;
				}
			}
		 }
		 if(!isValueChanged) {
			var allSelects = $("#addEditStudentDetail select");
			for(var count = 0; count < allSelects.length ; count++) {
				var selectedInd = $(allSelects[count]).attr("selectedIndex");
				if(selectedInd != 0 && ($(allSelects[count]).attr("disabled") == null  || $(allSelects[count]).attr("disabled") == false)) {
					isValueChanged = true;
					break;
				}
			}
		}
		if(!isValueChanged) {
		var customerValCheckbox = [];
			var counter=0;
		   var checkBoxfields = $("#addEditStudentDetail :checkbox"); 
	       for(var i=0; i < checkBoxfields.length; i++){
		       for(var j=0; j<customerDemographicValue.length; j++) {
		          	if($(checkBoxfields).eq(i).attr('name') == customerDemographicValue[j].name) {
		          	       // For bug MQC-67095
			       			if($(checkBoxfields).eq(i).attr('checked')== false && !($(checkBoxfields).eq(i).attr('disabled')))
			       			{
			       				isValueChanged = true;
			       				break;
			       			}
			       			customerValCheckbox[counter] =  customerDemographicValue[j].name;
							counter++;
					}
		       	}
			} 
			if(!isValueChanged && customerValCheckbox.length > 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
			       for(var j=0; j<customerValCheckbox.length; j++) {
				       	if(!isExist( $(checkBoxfields).eq(i).attr('name') ,customerValCheckbox)) { //
				       			if($(checkBoxfields).eq(i).attr('checked')== true && !($(checkBoxfields).eq(i).attr('disabled')))
				       			{
				       				isValueChanged = true;
				       				break;
				       			}
				       			
						}
			       	}
				} 
			}
			if(!isValueChanged && customerValCheckbox.length == 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
				 // For bug MQC-67095
					if($(checkBoxfields).eq(i).attr('checked')== true && !($(checkBoxfields).eq(i).attr('disabled'))) {
						isValueChanged = true;
						break;
					}
				}
			}
			
		}
		if(!isValueChanged){
			if(assignedOrgNodeIds != ""){
				isValueChanged = true;
			}
		}
		
		if(isValueChanged) {
		//UIBlock();
			openConfirmationPopup();	 
		} else {
			closePopUp('addEditStudentDetail');
		}
	
	} else {
	
			if (!isViewStudent){
		  		isValueChanged = isEditStudentDataChanged();
		  	}else {
		  		isValueChanged = false;
		  	}
	      	if(isValueChanged) {
				//UIBlock();
				openConfirmationPopup();	
			} else {
				if (!isViewStudent){
				closePopUp('addEditStudentDetail');
				}else{
				closePopUp('viewStudentDetail');
				}
			}
		}
		
	}
	
	function isEditStudentDataChanged(){
		var newStudentValue = $("#addEditStudentDetail *").serializeArray(); 
		isValueChanged = false;	
		
		if(dbStudentDetails.length != newStudentValue.length) {
			isValueChanged = true;
		}
		 if(!isValueChanged) {
		      for(var key = 0; key <dbStudentDetails.length ; key++) {
		       if(newStudentValue[key].name != dbStudentDetails[key].name) {
		       	isValueChanged = true;
		       	break;
		       }
		       if(trim(newStudentValue[key].value) != trim(dbStudentDetails[key].value)){
		      		isValueChanged = true;
		      		break;
		      	}
		      	 
		      }
	      }
	      var assignedOrgNodeIdsList = [];
	          if(String(assignedOrgNodeIds).indexOf(",") > 0) {
		     	assignedOrgNodeIdsList = assignedOrgNodeIds.split(",");
		     		if(!isValueChanged) {
				      	var count = 0;
				      	 for ( var i= 0; i<organizationNodes.length;i++) {
				         	if(isExist(organizationNodes[i].orgNodeId, assignedOrgNodeIdsList)){
				         		count++;
				         	}
				      	}
				      	if(count != assignedOrgNodeIdsList.length) {
				      		isValueChanged = true;
				      	}
			      	}
		     	} else {
		     		if(assignedOrgNodeIds != ""){
		     		 	if(organizationNodes.length > 1) {
			     		   	if(organizationNodes.length != 1) {
					      		isValueChanged = true;
					      	} 
					    } else {
					      		if(String(organizationNodes[0].orgNodeId) != assignedOrgNodeIds) {
					      			isValueChanged = true;
					      		}
					      	}
				      	
				     } else {
				     		isValueChanged = true;
				     }
		     	
		     	}
	     
	       
      	
      	return  isValueChanged;
	}
	
	function isExist(val, customerValCheckbox){
	if(String(customerValCheckbox.length) == "undefined" && customerValCheckbox != null){
		if(val == customerValCheckbox){
		 return true;
		 }
	}
		for(var i=0; i < customerValCheckbox.length; i++){
			if(val == customerValCheckbox[i]){
				return true;
			}
		}
		return false;
	}
	
	
	function studentDetailSubmit(){
	
	var param;
	var createBy = "";
	var assignedOrg = $('#selectedOrgNodesName').text();
	var showStudentInGrid = false;
		
	if(profileEditable === "false") {
		resetDisabledFields();
	}
	
	if(isAddStudent){
		param = $("#addEditStudentDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds+ "&studentIdLabelName=" +
		 $("#studentIdLabelName").val()+ "&studentIdConfigurable=" + $("#isStudentIdConfigurable").val() + 
		 "&isAddStudent=" + isAddStudent +"&createBy="+createBy ;
	}else{
		var selectedStudentId = $("#list2").jqGrid('getGridParam', 'selrow');
		createBy = getCreatedBy();
		if(createBy == null || createBy == 'undefined') {
			createBy = $("#stuCreatedBy").val();
		}
		param = $("#addEditStudentDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds+ "&studentIdLabelName=" +
			 $("#studentIdLabelName").val()+ "&studentIdConfigurable=" + $("#isStudentIdConfigurable").val()+
			  "&selectedStudentId=" + selectedStudentId + "&isAddStudent=" + isAddStudent + "&createBy="+createBy + "&loginId=" + loginId ;
	}
	 var validflag = VerifyStudentDetail(assignedOrgNodeIds);
	 
	if(validflag) {
					$.ajax(
						{
								async:		true,
								beforeSend:	function(){
											
												
												UIBlock();
												//alert('before send....');
											},
								url:		'saveAddEditStudent.do',
								type:		'POST',
								data:		 param,
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
												var orgs;  
												var errorFlag = data.errorFlag;
												var successFlag = data.successFlag;
												if(successFlag) {
													closePopUp('addEditStudentDetail');
													$('#errorIcon').hide();
													$('#infoIcon').show();
													setMessageMain(data.title, data.content, data.type, "");
													document.getElementById('displayMessageMain').style.display = "block";	
													//For edit student save defect
													if(String(assignedOrgNodeIds).indexOf(",") > 0) { 
														orgs = assignedOrgNodeIds.split(",");
													}
													else {
														orgs = [assignedOrgNodeIds];
													}
													
													if(orgs.length > 0 || orgs != null || orgs != "undefined") {
													 if(isExist(SelectedOrgNodeId,orgs)){
															assignedOrg = $("#" +SelectedOrgNodeId).text();
															showStudentInGrid = true;
														} else {
														if ($("#" +SelectedOrgNodeId).attr("cid") < leafNodeCategoryId) {
														var tempAssignedOrg = "";
																for(var i=0; i<orgs.length; i++){
																	var parentOrgNodeId = $("#" + orgs[i]).parent("ul");
																	var ancestorNodes = parentOrgNodeId.parentsUntil(".jstree","li");
																	for(var count = ancestorNodes.length - 1; count >= 0; --count) {
															  		 		var tmpNode = ancestorNodes[count].id;
																			if($.trim(tmpNode) == $.trim(SelectedOrgNodeId)){
																			 if(tempAssignedOrg == ""){
																			 	tempAssignedOrg =  tempAssignedOrg + $("#" +orgs[i]).text() ;
																			 } else {
																			 	tempAssignedOrg =  tempAssignedOrg + "," + $("#" +orgs[i]).text() ;
																			 }
																				
																				showStudentInGrid = true;
																				break;
																			}
																	 }
																	
																}
															assignedOrg	= tempAssignedOrg;
															} else {
																showStudentInGrid = false;
															}
														}
													} 
													
													if(showStudentInGrid) {
														var dataToBeAdded = {lastName:$("#studentLastName").val(),
																			firstName:$("#studentFirstName").val(),
																			middleName:$("#studentMiddleName").val(),
																			grade:$("#gradeOptions").val(),
																			orgNodeNamesStr:$.trim(assignedOrg),
																			gender:$("#genderOptions").val(),
																			hasAccommodations:data.hasAccommodation,
																			userName:data.studentLoginId,
																			studentNumber:$("#studentExternalId").val()};
														
														var sortOrd = jQuery("#list2").getGridParam("sortorder");
														var sortCol = jQuery("#list2").getGridParam("sortname");	
														if(!isAddStudent) {
															jQuery("#list2").setRowData(data.studentId, dataToBeAdded, "first");
														}
														else {
															jQuery("#list2").addRowData(data.studentId, dataToBeAdded, "first");
														}
															jQuery("#list2").sortGrid(sortCol,true);
													} else {
														if(!isAddStudent) {
															var oldOrganizationList = "";
															for(var i = 0; i < organizationNodes.length; i++) {
																oldOrganizationList = organizationNodes[i].orgNodeId+",";
															}
															oldOrganizationList = oldOrganizationList.substring(0,oldOrganizationList.length - 1);
															if(oldOrganizationList != assignedOrgNodeIds)
																jQuery("#list2").delRowData(data.studentId);
														}
													}
													assignedOrgNodeIds = "";
													$.unblockUI();			
        										}
        										else{
        											setMessage(data.title, data.content, data.type, "");
        											$('#errorIcon').show();
													$('#infoIcon').hide();
        											document.getElementById('displayMessage').style.display = "block";
        											if(profileEditable === "false") {
														disableAllNonEdFlds();
													} else {
														resetDisabledFields();
													}
        											$.unblockUI();
        										}
																								
											},
								error  :    function(XMLHttpRequest, textStatus, errorThrown){
													$.unblockUI();  
												window.location.href="/SessionWeb/logout.do";
											},
								complete :  function(){
												$.unblockUI();  
											}
						}
					);
	
	} else {
			document.getElementById('displayMessage').style.display = "block";
			if(profileEditable === "false") {
				disableAllNonEdFlds();
			} else {
				resetDisabledFields();
			}
	}
	}
	
	function resetDisabledFields(){
		$('#Student_Information :checkbox').attr('disabled', false); 
		$('#Student_Information :radio').attr('disabled', false); 
		$('#Student_Information select').attr('disabled', false);
		$('#Student_Information :input').attr('disabled', false);
		$('#Student_Additional_Information :checkbox').attr('disabled', false); 
		$('#Student_Additional_Information :radio').attr('disabled', false); 
		$('#Student_Additional_Information select').attr('disabled', false);
		$('#Student_Additional_Information :input').attr('disabled', false);
	}
	
	
	
	function openTreeNodes(orgNodeId) {
	var isopened = false;
	asyncOver = 0;
	leafParentOrgNodeId = "";
	var par = null;
	var correctId;
	$('#innerID').jstree('close_all');
		var isIdExist = $('#innerID', '#'+assignedOrgNodeIds).length;
			if(isIdExist > 0){
				$('#innerID').jstree('check_node', "#"+assignedOrgNodeIds);
				isopened = true; 
			} else {
				leafParentOrgNodeId = "";
				for(var i=0; i< organizationNodes.length; i++){
						if(orgNodeId == organizationNodes[i].orgNodeId){
							var leafOrgNodePath = organizationNodes[i].leafNodePath;
							 leafParentOrgNodeId = leafOrgNodePath.split(",");
							break;
						}
					}
					type = "innerID";
					
					outerLoop: for(var key in rootMap){
						for(var i =0; i < leafParentOrgNodeId.length; i++ ){
							if (key == leafParentOrgNodeId[i]){
								correctId = i;
							break outerLoop;
							}
						}
					}
					leafParentOrgNodeId = leafParentOrgNodeId.slice(correctId,leafParentOrgNodeId.length);
					
					if(leafParentOrgNodeId.length > 0) {
						for(var count = 0; count < leafParentOrgNodeId.length - 1; count++) {
				  		 	var tmpNode = leafParentOrgNodeId[count];

				  		 	currentCategoryLevel = String(count+1);
				  		 	currentNodeId = tmpNode;
				  		 	if(count != 0) { 
				  		 		par = leafParentOrgNodeId[count-1];		 						  		 			
				  		 	} else {
				  		 		par = leafParentOrgNodeId[count];
				  		 	}
				  		 	prepareData(false,currentCategoryLevel,currentNodeId,par);
				  		 }	 			
				  		 	var tmpNode = leafParentOrgNodeId[0];	
				  		 	currentCategoryLevel = "1";
				  		 	currentNodeId = tmpNode;
				  		 	currentTreeArray = map.get(currentNodeId);
				  		 	$('#innerID').jstree('open_node', "#"+currentNodeId); 
							
				  		 	$('#innerID').jstree('check_node', "#"+orgNodeId);
				  		 	$("#"+orgNodeId).focus();
				  		 	isopened = true; 
			  		 }
		 		 hideCheckBox();
		 }
		
	}
	
	function hideCheckBox(){
		$("#innerID li").not(".jstree-le").each(function() {
    			var orgcategorylevel = $(this).attr("cid");
    			if(orgcategorylevel != leafNodeCategoryId) {
	    		  $("a ins.jstree-checkbox", this).first().hide();
	    		  }
	    		  if(profileEditable === "false" && $("#classReassignable").val() === "true") {
		    			$("a ins.jstree-checkbox", this).first().hide();
		    		}
	  	});
	
	}
	
	function disablenextprev(selectedPosition,maxlength) {
               if(selectedPosition == 0 || ((selectedPosition%20)==0)) {
                	$("#pData").addClass("ui-state-disabled") ;
                    $("#viewpData").addClass("ui-state-disabled");
              
               } else {
						$("#pData").removeClass("ui-state-disabled");
                        $("#viewpData").removeClass("ui-state-disabled");
               }
               
                if ((selectedPosition == maxlength) || (((selectedPosition+1)%20) ==0)) {
               		$("#nData").addClass("ui-state-disabled");
                    $("#viewnData").addClass("ui-state-disabled");
               
               } else {
               		$("#nData").removeClass("ui-state-disabled");
                    $("#viewnData").removeClass("ui-state-disabled");
               }
     }
    
    function highlightnextprev(prevSelectedRow,nextSelectedRow) {            
		   $("#"+prevSelectedRow).removeClass("ui-state-highlight").attr({
               "aria-selected": "false",
               tabindex: "-1"
           });
           $("#"+nextSelectedRow).addClass("ui-state-highlight").attr({
               "aria-selected": true,
               tabindex: "0"
           });
	}

	function editStudentDetail(SelectedStudentId){
	isPopUp = true;
	var rowid;
	isAddStudent = false;
	document.getElementById('displayMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";	
	if(SelectedStudentId == undefined){
		rowid = $("#list2").jqGrid('getGridParam', 'selrow');
	} else {
		rowid = SelectedStudentId;
	}
	
    var createBy =  getCreatedBy();
 	var postDataObject = {};
 	postDataObject.studentID = rowid;
 	postDataObject.createBy = createBy;
 		
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'getStudentDataForSelectedStudent.do', 
		type:		'POST',
		data:		postDataObject,
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI();
						gradeOptions = data.optionList.gradeOptions;
						genderOptions = data.optionList.genderOptions;
						dayOptions = data.optionList.dayOptions; 
						monthOptions = data.optionList.monthOptions;
						yearOptions = data.optionList.yearOptions; 
						testPurposeOptions = data.optionList.testPurposeOptions;
						loginId = data.userName;
						fillDropDown("gradeOptions", gradeOptions);
						fillDropDown("genderOptions", genderOptions);
						fillDropDown("dayOptions", dayOptions);
						fillDropDown("monthOptions", monthOptions);
						fillDropDown("yearOptions", yearOptions);
						if($("#isLasLinkCustomer").val() =="true")
							fillDropDown("testPurposeOptions", testPurposeOptions);
							
						$("#studentFirstName").val(data.firstName);
						$("#studentMiddleName").val(data.middleName);
						$("#studentLastName").val(data.lastName);
						$("#monthOptions").val(data.month);
						$("#dayOptions").val(data.day);
						$("#yearOptions").val(data.year);
						$("#genderOptions").val(data.gender);
						$("#gradeOptions").val(data.grade);
						$("#studentExternalId").val(data.studentNumber);
						$("#studentExternalId2").val(data.studentSecondNumber);
						if($("#isLasLinkCustomer").val() == "true")
							$("#testPurposeOptions").val(data.testPurpose);
							
						profileEditable = String(data.optionList.profileEditable);
						
						stuDemographic = data.stuDemographic;
						stuAccommodation = data.stuAccommodation;
						organizationNodes = data.organizationNodes;
						fillselectedOrgNode("selectedOrgNodesName", organizationNodes);
						prepareCheckedList();
						setEditStudentDetail(rowid);
						if(profileEditable === "false") {
							$('#Student_Information :checkbox').attr('disabled', true); 
							$('#Student_Information :radio').attr('disabled', true); 
							$('#Student_Information select').attr('disabled', true);
							$('#Student_Information :input').attr('disabled', true);
						} else {
							resetDisabledFields();
						}
						$.unblockUI();  
						$("#addEditStudentDetail").dialog({  
													title:$("#editStuID").val(),  
												 	resizable:false,
												 	autoOpen: true,
												 	width: '800px',
												 	modal: true,
													closeOnEscape: false,												 	
												 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		 	
												 	});	
												 	 
						setPopupPosition(isAddStudent);
						checkOpenNode(organizationNodes);
						dbStudentDetails = 	$("#addEditStudentDetail *").serializeArray();

						
				/*var SelectedStudentId = $("#list2").jqGrid('getGridParam', 'selrow');
	        	var str = idarray;
				var nextStudentId ;
				var indexOfId = -1;
				if(!Array.indexOf) {
					indexOfId = findIndexFromArray (str , SelectedStudentId);
				} else {
					indexOfId = str.indexOf(SelectedStudentId);;
				}
						
				disablenextprev(indexOfId, str.length-1);
						*/	 		
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
		
	});
	
}

	function prepareCheckedList(){	
		var orgList = assignedOrgNodeIds;
		orgList = String(orgList).split(",");	
		for(var i = 0; i < orgList.length; i++) {	
			checkedListObject[orgList[i]] = "checked";	
		}
	}
	
	function checkOpenNode(organizationNodes){
	for(var i=0; i<organizationNodes.length; i++){
		var assignedOrg = organizationNodes[i].orgNodeId;
		var isIdExist = $('#'+assignedOrg,'#innerID').length;
			if(isIdExist > 0){
				$('#innerID').jstree('check_node', "#"+assignedOrg); 
			}
		}
	}
	
	function viewEditStudentPopup(){
		var editStudentPermission = $("#showEditButton").val();
		//var editStudentPermission = "false";
		if(editStudentPermission == "true"){
			editStudentDetail();	
		} else {
			viewStuDetail();		
		}
	}
	
	
	
	function viewStuDetail(SelectedStudentId){
	var rowid;
	isPopUp = true;
	isAddStudent = false;
	document.getElementById('displayMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";	
	if(SelectedStudentId == undefined){
		rowid = $("#list2").jqGrid('getGridParam', 'selrow');
	} else {
		rowid = SelectedStudentId;
	}
	var createBy =  getCreatedBy();
	
	var postDataObject = {};
 	postDataObject.studentID = rowid;
 	postDataObject.createBy = createBy;
    
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'getViewStudentData.do', 
		type:		'POST',
		data:		postDataObject,
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI();
						$("#studentFirstNameView").text(data.firstName);
						$("#studentMiddleNameView").text(data.middleName);
						$("#studentLastNameView").text(data.lastName);
						$("#studentUserNameView").text(data.userName);
						$("#birthdateStringView").text(data.birthdateString);
						$("#studentgradeView").text(data.grade);
						$("#studentgenderView").text(data.gender);
						$("#studentNumberView").text(data.studentNumber);
						$("#studenttestPurposeView").text(data.testPurpose);
						$("#orgNodeNameView").text(data.orgNodeNamesStr);
						stuDemographic = data.stuDemographic;
						stuAccommodation = data.stuAccommodation;
						setViewStudentDetail(rowid);
						//disableFields('viewStudentDetail', true);
						$('#viewStudentDetail :checkbox').attr('disabled', true); 
						$('#viewStudentDetail :radio').attr('disabled', true); 
						$('#viewStudentDetail select').attr('disabled', true); 
						$.unblockUI();  
						//customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
						// For MQC Defect - 67150
						$("#viewStudentDetail").dialog({  
													title:$("#viewStuID").val(),  
												 	resizable:false,
												 	autoOpen: true,
												 	width: '800px',
												 	modal: true,
												 	closeOnEscape: true,
												 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
												 	beforeClose: function(event, ui) {
                 									if(event.keyCode == 27) {  
                     								closePopUp('viewStudentDetail');
                     								event.preventDefault();
                 									} 
   													}		 	
												 	});	
												 	 
							 setPopupPosition(isAddStudent);
							 
							 isViewStudent = true;
							 // to handle enabling/disabling of next/prev button 
							 //when this metod is invoked by view next button
							if(SelectedStudentId == undefined) { 
	        					var str = $("#list2").jqGrid('getDataIDs'); 
								var nextStudentId ;
								var indexOfId = -1;
								if(!Array.indexOf) {
									indexOfId = findIndexFromArray (str , rowid);
								} else {
									indexOfId = str.indexOf(rowid);;
								}
									
								disablenextprev(indexOfId, str.length-1);
							}
							
							 		
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
		
	});
	
}
		
		/*disableFields(dailogId, flag){
			$("#"+dailogId + ' :checkbox').attr('disabled', flag); 
			$("#"+dailogId + ' :radio').attr('disabled', flag); 
			$("#"+dailogId + ' :select').attr('disabled', flag); 
		}*/
	
				function nDataClick(popupname) {
					requetForStudent = "Next";
					var isValueChanged = false;
						if(popupname == 'Edit') {
							isValueChanged = isEditStudentDataChanged();
							if(isValueChanged) {
								//UIBlock();
								openConfirmationPopup('alternateMessage');
								} 
						}
						if(!isValueChanged) {
							fetchNextData(popupname);							
						}
               }
               
               function pDataClick(popupname) {
               		requetForStudent = "Previous";
               		var isValueChanged = false;
					if(popupname == 'Edit') {
						isValueChanged = isEditStudentDataChanged();
						if(isValueChanged) {
							//UIBlock();
							openConfirmationPopup('alternateMessage');	
							
							} 
					}
					if(!isValueChanged) {
						fetchPreviousData(popupname);
				  }
               }
	
		
		function fetchNextData(popupname){
			var SelectedStudentId = $("#list2").jqGrid('getGridParam', 'selrow');
			var pageRows = $("#list2").jqGrid('getGridParam','rowNum');
			var curPage = parseInt($('#list2').jqGrid('getGridParam','page')); 
	        var str = idarray;
			var nextStudentId ;
			var pageDataIds = $("#list2").jqGrid('getDataIDs'); 
			//alert("Selected:["+SelectedStudentId+"]fetchNextData:"+pageDataIds);
			str = pageDataIds;
			//var indexOfId = str.indexOf(SelectedStudentId);
			var indexOfId = -1;
			if(!Array.indexOf) {
				indexOfId = findIndexFromArray (str , SelectedStudentId);
			} else {
				indexOfId = str.indexOf(SelectedStudentId);
			}
			//alert("indexOfId :"+indexOfId+"::"+str.length+"::"+pageRows+"::"+curPage);
	
			if(indexOfId != str.length-1) {
				if(indexOfId%pageRows != pageRows-1){	//if(indexOfId != (pageRows*curPage)-1){
					nextStudentId = str[indexOfId + 1];
					highlightnextprev(SelectedStudentId, nextStudentId);
		      			if(popupname == 'Edit')
		      				editStudentDetail(nextStudentId);
		      			else
		      				viewStuDetail(nextStudentId);
		      			disablenextprev(indexOfId+1,str.length-1);
		      			$('#innerID').jstree('close_all');
		      			checkedListObject = {};
		      			populateTreeSelect();
		      			$("#list2").setSelection(nextStudentId, true); 
	      		}
			}
			requetForStudent = "";
		}
		
		function fetchPreviousData(popupname){
			var SelectedStudentId = $("#list2").jqGrid('getGridParam', 'selrow');
			var pageRows = $("#list2").jqGrid('getGridParam','rowNum');
			var curPage = parseInt($('#list2').jqGrid('getGridParam','page'));
            var str = idarray;
			var preStudentId ;
			var pageDataIds = $("#list2").jqGrid('getDataIDs'); 
			//alert("Selected:["+SelectedStudentId+"fetchPreviousData:"+pageDataIds);
			str = pageDataIds;
			var indexOfId = -1;
			if(!Array.indexOf) {
				indexOfId = findIndexFromArray (str , SelectedStudentId);
			} else {
				indexOfId = str.indexOf(SelectedStudentId);;
			}

			//alert("indexOfId :"+indexOfId+"::"+str.length+"::"+pageRows+"::"+curPage);
	
			if(indexOfId%pageRows != 0){	//if(indexOfId != (pageRows*(curPage-1))){
					preStudentId = str[indexOfId - 1];
					highlightnextprev(SelectedStudentId, preStudentId);
		       		if(popupname == 'Edit')
		       			editStudentDetail(preStudentId);
		       		else
		       			viewStuDetail(preStudentId);
		       		disablenextprev(indexOfId-1,str.length-1);
		       		$('#innerID').jstree('close_all');
		      		checkedListObject = {};
		       		populateTreeSelect();
		       		$("#list2").setSelection(preStudentId, true); 
		    }	
			requetForStudent = "";
		}
		
		
		function openConfirmationPopup(args){
		var arg= args;
		if(arg == null || arg == undefined){
			$("#confirmationPopup").dialog({  
				title:$("#confirmID").val(),  
			 	resizable:false,
			 	autoOpen: true,
			 	width: '400px',
			 	modal: true,
			 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
				});	
				 $("#confirmationPopup").css('height',120);
				 var toppos = ($(window).height() - 290) /2 + 'px';
				 var leftpos = ($(window).width() - 410) /2 + 'px';
				 $("#confirmationPopup").parent().css("top",toppos);
				 $("#confirmationPopup").parent().css("left",leftpos);		
				 
		 }
		 else{
		  $("#confirmationPopupNavigation").dialog({  
			title:$("#confirmID").val(),  
			resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
		    $("#confirmationPopupNavigation").css('height',120);
			var toppos = ($(window).height() - 290) /2 + 'px';
			var leftpos = ($(window).width() - 410) /2 + 'px';
			$("#confirmationPopupNavigation").parent().css("top",toppos);
			$("#confirmationPopupNavigation").parent().css("left",leftpos);	
			
		} 	
	}
		
		
		
	function getCreatedBy(){			
		return $("#stuCreatedBy").val();
	}
	
	function setViewStudentDetail(SelectedStudentId) {
	
	if(isViewStudent){
    	assignedOrgNodeIds = "";
    	var str = idarray;
		//var indexOfId = str.indexOf(SelectedStudentId);
		var pageDataIDs = $("#list2").jqGrid('getDataIDs'); 
		//alert("SelectedStudentId:"+SelectedStudentId+"setViewStudentDetail:"+pageDataIDs);
		str = pageDataIDs;
		var indexOfId = -1;
		var noRadioData = true;
		if(!Array.indexOf) {
			indexOfId = findIndexFromArray (str , SelectedStudentId);
		} else {
			indexOfId = str.indexOf(SelectedStudentId);;
		}


		disablenextprev(indexOfId,str.length-1);
		
      for(var key in stuAccommodation) {
      		var keyInUI = getRealValue(key);	// Added as field name and object key has different value
             $("#view_Student_Accommodation_Information :checkbox[name='" + "view" + keyInUI+ "']").attr('checked', stuAccommodation[key]);
		     $("#view_Student_Accommodation_Information select[name='" + "view" + keyInUI+ "']").val(stuAccommodation[key]);
		}
			setQuestionColorOptionsInView();
			setAnswerColorOptionsInView();
		
		for(var count=0; count< stuDemographic.length; count++) {
			if(stuDemographic[count]['studentDemographicValues'].length == 1){
		     	var dynKey = stuDemographic[count]['labelName'] + "_" + stuDemographic[count]['studentDemographicValues'][0]['valueName'] ;
		     	if(trim(stuDemographic[count]['studentDemographicValues'][0]['selectedFlag']) == 'true'){
			     	$("#view_Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', true);
			     }else {
			     	$("#view_Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', false);
			     }
		     } else {
				var valueCardinality = stuDemographic[count]['valueCardinality'];
				 if(valueCardinality == 'SINGLE'){
					for(var innerCount = 0 ; innerCount < stuDemographic[count]['studentDemographicValues'].length; innerCount++){
				     		if(trim(stuDemographic[count]['studentDemographicValues'][innerCount]['selectedFlag']) == 'true'){
				     			//$("#view_Student_Additional_Information select[name='" + stuDemographic[count]['labelName']+ "']").val(stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']);
				     			var selectElement = document.getElementById(stuDemographic[count]['labelName']);
			     			    if(selectElement!=null && String(selectElement.options) !="undefined"){
			     			     setSelectedValue(selectElement, stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']);
			     			    }else{		     			
				     			$("#view_Student_Additional_Information :radio[value='" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']+ "']").attr('checked',true);
				     			noRadioData = false;
				     			}
				     			break;
				     		} else {
				     			if (noRadioData){
				     			$("#view_Student_Additional_Information :radio[value='None']").attr('checked',true);
				     			}
				     			$("#view_Student_Additional_Information select[name='" + stuDemographic[count]['labelName']+ "']").find("option:eq(0)").attr("selected","true");
				     		}
				     	}
				
				}
			     	
			    if(valueCardinality == 'MULTIPLE'){
					for(var innerCount = 0 ; innerCount < stuDemographic[count]['studentDemographicValues'].length; innerCount++){
			     		var dynKey = stuDemographic[count]['labelName'] + "_" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName'] ;
			     		if(trim(stuDemographic[count]['studentDemographicValues'][innerCount]['selectedFlag']) == 'true'){
			     		$("#view_Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', true);
				     }else {
				     	$("#view_Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', false);
				     }
			     	}
			     }
			 }
		  }
		
		var radiofields = $("#view_Student_Additional_Information :radio"); 
		var flag = false;
       	for (var i=0; i<radiofields.length; i++) {
			if (radiofields[i].checked == true  && radiofields[i].value != "None") { 
				flag = true;
			}
		}
		if(!flag) {
		for (var i=0; i<radiofields.length; i++) {
			if(radiofields[i].value == "None")
				radiofields[i].checked = true;
		}
		}
		}
   }
   
   
	function findIndexFromArray (myArray, obj) {    
    for(var i=0, j= myArray.length; i<j; i++){
	            if(myArray[i]==obj){
	                return i;
	            }
	        }
	        return -1;
   
   }
   


   function setEditStudentDetail(SelectedStudentId) {
   		var str = idarray;
   		var isColorFontChecked; 
		//var indexOfId = str.indexOf(SelectedStudentId);
		var indexOfId = -1;
		
		var pageDataIds = $("#list2").jqGrid('getDataIDs'); 
		//alert("Selected:["+SelectedStudentId+"]setEditStudentDetail:"+pageDataIds);
		var noRadioData = true;
		str = pageDataIds;
		if(!Array.indexOf) {
			indexOfId = findIndexFromArray (str , SelectedStudentId);
		} else {
			indexOfId = str.indexOf(SelectedStudentId);;
		}
		disablenextprev(indexOfId,(str.length) - 1);
					
    	for(var key in stuAccommodation) {
    		if(key == 'colorFont'){
    			isColorFontChecked = stuAccommodation[key];
    		}
    		var keyInUI = getRealValue(key);	// Added as field name and object key has different value
		     $("#Student_Accommodation_Information :checkbox[name='" + keyInUI+ "']").attr('checked', stuAccommodation[key]);
		     $("#Student_Accommodation_Information select[name='" + keyInUI+ "']").val(stuAccommodation[key]);
		}
		if(isColorFontChecked){
			enableColorSettingsLink("true");
			setQuestionColorOptions();
			setAnswerColorOptions();
		}else{
			enableColorSettingsLink("false");
		}

		
		for(var count=0; count< stuDemographic.length; count++) {
		
		if(stuDemographic[count]['studentDemographicValues'].length == 1){
		     	var dynKey = stuDemographic[count]['labelName'] + "_" + stuDemographic[count]['studentDemographicValues'][0]['valueName'] ;
		     	if(trim(stuDemographic[count]['studentDemographicValues'][0]['selectedFlag']) == 'true'){
			     	$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', true);
			     }else {
			     	$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', false);
			     }
			     if(stuDemographic[count].importEditable == 'F' && profileEditable === "false") {
			     	$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('disabled', true);
			     } else {
			     	$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('disabled', false);
			     }
		     }else {
				var valueCardinality = stuDemographic[count]['valueCardinality'];
				if(valueCardinality == 'SINGLE'){
					for(var innerCount = 0 ; innerCount < stuDemographic[count]['studentDemographicValues'].length; innerCount++){
				     		if(trim(stuDemographic[count]['studentDemographicValues'][innerCount]['selectedFlag']) == 'true'){
				     			//$("#Student_Additional_Information select[name='" + stuDemographic[count]['labelName']+ "']").val(stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']);
				     			var selectElement = document.getElementById(stuDemographic[count]['labelName']);
				     			
				     		    if(selectElement!=null && String(selectElement.options) !="undefined"){
			     			     	setSelectedValue(selectElement, stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']);
			     			     }else {
			     			     	$("#Student_Additional_Information :radio[value='" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']+ "']").attr('checked',true);
			     			    	noRadioData = false;
			     			     }
			     			  	break;
				     		} else {
				     			//The following line is setting none for all the radio values even once it comes to the else block
				     			if(noRadioData){
				     				$("#Student_Additional_Information :radio[value='None']").attr('checked',true);
				     			}
				     			$("#Student_Additional_Information select[name='" + stuDemographic[count]['labelName']+ "']").find("option:eq(0)").attr("selected","true");
				     		}
				     	}
						if(profileEditable === "false" && stuDemographic[count].importEditable == 'F') {
			     			$("#"+stuDemographic[count]['labelName']).attr('disabled', true);
			     			var selectArray = $("#Student_Additional_Information select");
			     			for(var k = 0; k < selectArray.length; k++) {
			     				if($(selectArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(selectArray).eq(k).attr("disabled", true);
			     				} 
			     			}
			     			var radioArray = $("#Student_Additional_Information :radio");
			     			for(var k = 0; k < radioArray.length; k++) {
			     				if($(radioArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(radioArray).eq(k).attr("disabled", true);
			     				} 
			     			}
			     		} else {
			     			$("#"+stuDemographic[count]['labelName']).attr('disabled', false);
			     			var selectArray = $("#Student_Additional_Information select");
			     			for(var k = 0; k < selectArray.length; k++) {
			     				if($(selectArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(selectArray).eq(k).attr("disabled", false);
			     				} 
			     			}
			     			var radioArray = $("#Student_Additional_Information :radio");
			     			for(var k = 0; k < radioArray.length; k++) {
			     				if($(radioArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(radioArray).eq(k).attr("disabled", false);
			     				} 
			     			}
			     		}
				}
				if(valueCardinality == 'MULTIPLE'){
					for(var innerCount = 0 ; innerCount < stuDemographic[count]['studentDemographicValues'].length; innerCount++){
			     		var dynKey = stuDemographic[count]['labelName'] + "_" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName'] ;
			     		if(trim(stuDemographic[count]['studentDemographicValues'][innerCount]['selectedFlag']) == 'true'){
			     		$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', true);
					     }else {
					     $("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', false);
					     }
					     if(stuDemographic[count].importEditable == 'F' && profileEditable === "false"){
			     		$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('disabled', true);
					     }else {
					     $("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('disabled', false);
					     }
			     	}
			    }
		   }
		}
		
		
  }

function setSelectedValue(selectObj, valueToSet) {
    for (var i = 0, j =selectObj.options.length; i < j; i++) {
        if (selectObj.options[i].text== valueToSet) {
            selectObj.options[i].selected = true;
            return;
        }
    }    
}

	function deleteStudentPopup() {
	
		clearMessage();
	
		$("#deleteStudentPopup").dialog({  
			title:$("#delStuID").val(),  
			resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});
				
	    $("#deleteStudentPopup").css('height',130);
		var toppos = ($(window).height() - 130) /2 + 'px';
		var leftpos = ($(window).width() - 400) /2 + 'px';
		$("#deleteStudentPopup").parent().css("top",toppos);
		$("#deleteStudentPopup").parent().css("left",leftpos);	
	}

	function submitDeleteStudentPopup() {

		closePopUp('deleteStudentPopup');

		var studentId = $("#list2").jqGrid('getGridParam', 'selrow');
		
		var param = {};
		param.studentID = studentId;
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'deleteStudent.do',
				type:		'POST',
				data:		param,
				dataType:	'html',
				success:	function(data, textStatus, XMLHttpRequest){	
								
							    $("#deleteStatus").val(data);
																														 						
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
							},
				complete :  function(){
							}
				}
			);
	}

	function showDeleteStudentStatus(){ 
	
		var deleteStatus = $("#deleteStatus").val();
		var delStuMsg = $("#delStuID").val();
		setMessageMain(delStuMsg, deleteStatus, '', '');
		document.getElementById('displayMessageMain').style.display = "block";	
		var delStuSuccess=$("#delStuSuccessID").val();
		if (deleteStatus == delStuSuccess) {
	        $('#errorIcon').hide();
			$('#infoIcon').show();  
			var studentId = $("#list2").jqGrid('getGridParam', 'selrow');
			jQuery("#list2").delRowData(studentId);
			jQuery("#list2").trigger("reloadGrid");
		}else{
			$('#errorIcon').show();
			$('#infoIcon').hide();
		}
		$.unblockUI();
   	}

	function setupButtonPerUserPermission() {

		var addStudentEnable = $("#addStudentEnable").val();
		if (addStudentEnable == 'false') {	
			var element = document.getElementById('add_list2');
			element.style.display = 'none';
		}
		var deleteStudentEnable = $("#deleteStudentEnable").val();
		if (deleteStudentEnable == 'false') {	
			var element = document.getElementById('del_list2');
			element.style.display = 'none';
		}
	}
	/******Jstree Methods*****/
	//method triggered from library
	  function customLoad(){
	  	//console.log("Custom Load called");
	    pushInsideElement(currentNodeId,currentCategoryLevel,dataObj2,currentTreeArray);
	  }
  
    function pushInsideElement(currentNodeId,currentCategoryLevel,dataObj2,currentTreeArray){
		var objArray;
		switch(currentCategoryLevel){
		case "1": objArray = dataObj2;				  
				break;
		case "2": objArray = currentTreeArray.children;
				break;
				
		case "3": objArray = currentTreeArray.children;
				break;
		case "4": objArray = currentTreeArray.children;
				break;
		case "5": objArray = currentTreeArray.children;
				break;
		case "6": objArray = currentTreeArray.children;
				break;
		case "7": objArray = currentTreeArray.children;
				break;
		case "8": objArray = currentTreeArray.children;
				break;
		}
		
		updateTree(objArray);	
	  }
	  function updateTree(objArray){
	  	//timer.setStartTime();
	  	var currentElement;
		if (isPopUp){
			currentElement = $('li[id='+ currentNodeId+ ']');
			if(currentElement.length < 2) currentElement = currentElement[0];
			else currentElement = currentElement[1];
				
		}else {		
			currentElement = document.getElementById(currentNodeId);
		}
	
		currentElement.className = "jstree-open jstree-unchecked";
		var fragment = document.createDocumentFragment();
		var ulElement = document.createElement('ul');
		
		if(type.indexOf("innerID") < 0){
		stream(objArray,ulElement,fragment,streamPush, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
			 //currentElement.childNodes[1].firstChild.style.display = "none";
		 });	
		 }
		 else{
		 stream(objArray,ulElement,fragment,streamInnerPush, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
			asyncOver++;
			openNextLevel(asyncOver);
			// currentElement.childNodes[1].firstChild.style.display = "none";
		 });	
		 }
	  }

	  //Seperated outer tree push and inner tree push because each method is a performance critical operation conditions will increase the 
	  // tree population.	  
	  function streamPush(objArray,ulElement,fragment){
	  	var liElement = document.createElement('li');
		liElement.setAttribute("id", objArray.attr.id);
		liElement.setAttribute("cid" , objArray.attr.cid);
		liElement.setAttribute("tcl" , objArray.attr.tcl);
		var condition = objArray.attr.chlen;
		
		switch(condition){
		case false: liElement.className = "jstree-leaf jstree-unchecked";
					break;
		case true:  liElement.className = "jstree-closed jstree-unchecked";
					break;
		case undefined: var con = objArray.hasOwnProperty("children");
							switch(con){
								case false: liElement.className = "jstree-leaf jstree-unchecked";
											break;
								case true:  liElement.className = "jstree-closed jstree-unchecked";
											break;						
							}
						  break;
					}
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		ulElement.appendChild(liElement);
		fragment.appendChild(ulElement);
	  }
	  
	  function streamInnerPush(objArray,ulElement,fragment){
	  	var liElement = document.createElement('li');
		liElement.setAttribute("id", objArray.attr.id);
		liElement.setAttribute("cid" , objArray.attr.cid);
		liElement.setAttribute("tcl" , objArray.attr.tcl);
		var condition = objArray.attr.chlen;
		
		switch(condition){
		case false: liElement.className = "jstree-leaf jstree-"+ getCheckedStatus(objArray.attr.id);
					break;
		case true:  liElement.className = "jstree-closed jstree-"+ getCheckedStatus(objArray.attr.id);
					break;
		case undefined: var con = objArray.hasOwnProperty("children");
							switch(con){
								case false: liElement.className = "jstree-leaf jstree-"+ getCheckedStatus(objArray.attr.id);
											break;
								case true:  liElement.className = "jstree-closed jstree-"+ getCheckedStatus(objArray.attr.id);
											break;						
							}
						  break;
		
		}	
		if (objArray.attr.cid == leafNodeCategoryId){
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: inline-block;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}else{
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}
		if(profileEditable === "false"  && $("#classReassignable").val() === "true") {
			liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}
		ulElement.appendChild(liElement);
		fragment.appendChild(ulElement);
	  }
  
  	function populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot){	
	//TODO : Updation in root node is a problem need to work on that but this method is also necessary for populating the immediate //tree because if we use the cache all the 2nd level objects will be stored in cache which makes the cache very heavy
		var jsonObject = jsonData;
		jsonObject = jsonObject[indexOfRoot];
		if (dataObj2.length == 0 && jsonObject != undefined){
			for (var i = 0, j = jsonObject.children.length; i < j; i++ ){
					dataObj2.push({data: jsonObject.children[i].data,attr:{id: jsonObject.children[i].attr.id,cid:jsonObject.children[i].attr.cid, tcl:jsonObject.children[i].attr.tcl,chlen:jsonObject.children[i].hasOwnProperty("children")},children : [{}]});	
			}
		}
	}
  
  	/************Tree Utils*****************/
	function getObject(jsonObject,currentNodeId,currentCategoryLevel, parentNodeId){
			var obj = new Object();
			var indexRoot = getIndexOfRoot(parentNodeId);
			if(currentCategoryLevel == 2){
				for (var i = 0, j = jsonObject[indexRoot].children.length; i < j; i++ ){
				var attrId = jsonObject[indexRoot].children[i].attr.id;
					if (attrId == currentNodeId){
					obj.jsonData = jsonObject[indexRoot].children[i];
					obj.index = i;
					return obj;
					}
				}
			}
			else {
				for (var i = 0, j = jsonObject.children.length; i < j; i++ ){
				var attrId = jsonObject.children[i].attr.id;
					if (attrId == currentNodeId){
					obj.jsonData = jsonObject.children[i];
					obj.index = i;
					return obj;
					}
				}	
			}
		}
	
	function getRootNodeDetails(){
		var noOfRoots = jsonData.length;
			for (var i = 0,j = noOfRoots; i < j; i++ ){
				rootMap[jsonData[i].attr.id] = jsonData[i].attr.cid;
				if(jsonData[i].attr.cid == "1"){
					rootNodeId = jsonData[i].attr.id;
				}					
				rootNode.push({data: jsonData[i].data,attr:{id: jsonData[i].attr.id,cid:jsonData[i].attr.cid,tcl:jsonData[i].attr.tcl},children : [{}]});
			}
	}
	


	function getIndexOfRoot(currentNodeId){
	var numberOfRoots = jsonData.length;
	
		for (var i = 0,j = numberOfRoots; i < j; i++ ){
			if (jsonData[i].attr.id == currentNodeId){				
				return i;
			}
		}
	
	}
	
	function stream(array,element,fragment,process,context,callback){
	 var treeData = array.concat();   
	
		setTimeout(function(){

			var start = +new Date();

			do {
				 process.call(context, treeData.shift(),element,fragment);
			} while (treeData.length > 0 && (+new Date() - start < 50));

			if (treeData.length > 0){
				setTimeout(arguments.callee, 25);
			} else {
				callback(array);
			}
		}, 25);    
	
	}	
	
	function getCheckedStatus(id){
    if (checkedListObject[id] == "checked"){
		return "checked";
	}
  		return "unchecked";
  }
  
  
  
function prepareData(classState,currentCategoryLevel,currentNodeId,element){
  
		if (classState == false || !classState || classState == 'false'){

			var cacheData = map.get(currentNodeId);
			if (cacheData != null){
				currentTreeArray = cacheData;			
			}
					
			if (cacheData == null){
		if (currentCategoryLevel == "1") {	
			dataObj2 = [];	
			var indexOfRoot = getIndexOfRoot(currentNodeId);
			populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot);
		}
				switch(String(currentCategoryLevel)){
					
					case "2": 	dataObj3 =getObject(jsonData,currentNodeId,currentCategoryLevel,element);
								currentIndex = dataObj3.index;
								currentTreeArray = dataObj3.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;							
					case "3": 	dataObj4 = map.get(element);
								currentTreeArray =getObject(dataObj4,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "4": 	dataObj5 = map.get(element);
								currentTreeArray =getObject(dataObj5,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "5": 	dataObj6 = map.get(element);
								currentTreeArray =getObject(dataObj6,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "6": 	dataObj7 = map.get(element);
								currentTreeArray =getObject(dataObj7,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "7": 	dataObj8 =map.get(element);
								currentTreeArray =getObject(dataObj8,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);								
							break;	
					case "8": 	dataObj9 =map.get(element);
								currentTreeArray =getObject(dataObj9,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;						
					
				}
			}

		}
  
  }
  
  
  function openNextLevel(asyncOver){
  		if(leafParentOrgNodeId.length - 1 > asyncOver) {
	  		var tmpNode = leafParentOrgNodeId[asyncOver];	
			currentCategoryLevel = String(asyncOver + 1);
			currentNodeId = tmpNode;
			currentTreeArray = map.get(currentNodeId);
			$('#innerID').jstree('open_node', "#"+currentNodeId);
		}
  }
  
  
  
  
	/******CRUD Operations****/
		function addTree(currentNodeId){
		//TODO :  
		}

		function deleteTree(currentNodeId){
		//TODO :  
		}

		function moveTree(currentNodeId){
		deleteTree(currentNodeId);
		addTree(currentNodeId);
		}

		function hideCheckBox(){
		//TODO: Use the pushInside data.attr.children == null add leaf class.

		}

		function openTree(){
		//TODO: need to work on;
		} 
	// Added as field name and object key has different value
	function getRealValue(keyVal) {
		if(keyVal == 'extendedTime')
			return 'ExtendedTime';
		if(keyVal == 'maskingRuler')
			return 'MaskingRuler';
		if(keyVal == 'magnifyingGlass')
			return 'MagnifyingGlass';
		if(keyVal == 'auditoryCalming')
			return 'AuditoryCalming';
		if(keyVal == 'maskingTool')
			return 'MaskingTool';
		return keyVal;
	}
			 
	function disableAllNonEdFlds() {
		
		$('#Student_Information :checkbox').attr('disabled', true); 
		$('#Student_Information :radio').attr('disabled', true); 
		$('#Student_Information select').attr('disabled', true);
		$('#Student_Information :input').attr('disabled', true);
		var noRadioData = true;
		for(var count=0; count< stuDemographic.length; count++) {
		
		if(stuDemographic[count]['studentDemographicValues'].length == 1){
		     	var dynKey = stuDemographic[count]['labelName'] + "_" + stuDemographic[count]['studentDemographicValues'][0]['valueName'] ;
			     if(stuDemographic[count].importEditable == 'F' && profileEditable === "false") {
			     	$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('disabled', true);
			     } else {
			     	$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('disabled', false);
			     }
		     }else {
				var valueCardinality = stuDemographic[count]['valueCardinality'];
				if(valueCardinality == 'SINGLE'){
						if(profileEditable === "false" && stuDemographic[count].importEditable == 'F') {
			     			$("#"+stuDemographic[count]['labelName']).attr('disabled', true);
			     			var selectArray = $("#Student_Additional_Information select");
			     			for(var k = 0; k < selectArray.length; k++) {
			     				if($(selectArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(selectArray).eq(k).attr("disabled", true);
			     				} 
			     			}
			     			var radioArray = $("#Student_Additional_Information :radio");
			     			for(var k = 0; k < radioArray.length; k++) {
			     				if($(radioArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(radioArray).eq(k).attr("disabled", true);
			     				} 
			     			}
			     		} else {
			     			$("#"+stuDemographic[count]['labelName']).attr('disabled', false);
			     			var selectArray = $("#Student_Additional_Information select");
			     			for(var k = 0; k < selectArray.length; k++) {
			     				if($(selectArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(selectArray).eq(k).attr("disabled", false);
			     				} 
			     			}
			     			var radioArray = $("#Student_Additional_Information :radio");
			     			for(var k = 0; k < radioArray.length; k++) {
			     				if($(radioArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(radioArray).eq(k).attr("disabled", false);
			     				} 
			     			}
			     		}
				}
				if(valueCardinality == 'MULTIPLE'){
					for(var innerCount = 0 ; innerCount < stuDemographic[count]['studentDemographicValues'].length; innerCount++){
			     		var dynKey = stuDemographic[count]['labelName'] + "_" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName'] ;
					     if(stuDemographic[count].importEditable == 'F' && profileEditable === "false"){
			     		$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('disabled', true);
					     }else {
					     $("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('disabled', false);
					     }
			     	}
			    }
		   }
		}
	}
			
		



