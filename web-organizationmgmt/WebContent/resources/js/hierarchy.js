
var gridjsondata;
var idarray =[];
var gridloaded = false;

var orgTreeHierarchy;
var SelectedOrgNodeId ;
var assignedOrgNodeIds="";

var layerOptions=[];
var categoryIds=[];
var isPopUp = false;
var requetForOrganization = "";
var isTreeExpandIconClicked = false;
var isAction = true;
var isAddOrganization = true;
var defaultParent = "<font color=\"gray\">None selected. Use the control on the right to select.</font>";
var defaultParentIE = "<FONT color=gray>None selected. Use the control on the right to select.</FONT>";
var isLasLinkCustomer = false;
var assignedElement = "";
var leafNodeCategoryId;
//added on 09.11.2011
var dbOrgDetails;
var isValueChanged = false;
var organizationNodes = [];
var originalParentOrgId;
var isParentChange = false;
var isLeafNodeAdmin;

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
var currentNodeIdForEdit = ""; //added to bypass layer population in case of self parent problem
var prevOrgNodeParentId;
var prevOrgNodeParent;
var prevElementLeafNodePath;
var currentClickedId;
var currentClickedTcl;
$(document).bind('keydown', function(event) {
		
	      var code = (event.keyCode ? event.keyCode : event.which);
	      if(code == 27){
	      		if(isPopUp){
				
	      			onCancel();
	      		}
	            return false;
	      }
	  });





function populateTree() {
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'organizationOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI(); 
						orgTreeHierarchy = data;
						leafNodeCategoryId = data.leafNodeCategoryId;
						isLeafNodeAdmin = data.isLeafNodeAdmin;
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

function UIBlock(){
	$.blockUI({ message: '<img src="/OrganizationWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}
			

function createSingleNodeSelectedTree(jsondata) {
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
				"plugins" : [ "themes", "json_data", "ui", "crrm"]  
				
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
			currentClickedId = this.parentNode.id;
			currentClickedTcl = this.parentNode.getAttribute("tcl");	 
  			SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#treeOrgNodeId").val(SelectedOrgNodeId);
 		    UIBlock();
 		    if(!gridloaded) {
 		  		gridloaded = true;
 		        populateTreeSelect();
			    populateGrid();
			}
			else
				gridReload();
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
	
	
	
function createMultiNodeSelectedTree(jsondata) {
var styleClass;
  $("#innerID").jstree({
        "json_data" : {	             
            "data" : rootNode,
			"progressive_render" : true,
			"progressive_unload" : true
        },
        "ui" : {
        	"select_limit" : 1
        },
        "checkbox" : {
        "two_state" : true
        }, 
        	
			"themes" : {
			"theme" : "apple",
			"dots" : false,
			"icons" : false
			},         	
         	
		"plugins" : [ "themes", "json_data","ui","checkbox","crrm"]
    });
    
    $("#innerID").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#innerID ul li").eq(i).find('a').find('.jstree-checkbox:first').hide();
						$("#innerID ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
			registerDelegate("innerID");
    
    	$("#innerID").delegate("li a","click",
    		 function(e) {
    		 
    				styleClass = $(this.parentNode).attr('class');
    				var orgcategorylevel = $(this.parentNode).attr("cid");
					var elementId = $(this.parentNode).attr('id');
					var currentlySelectedNode ="";
    				var element = this.parentNode;
    				currentId = $(this.parentNode).attr("id");
    				var orgcategorylevel = $(element).attr("cid");
					if(styleClass.indexOf("unchecked") > 0){
						$('#innerID').jstree('uncheck_all');
						$(this.parentNode).removeClass("jstree-unchecked").addClass("jstree-checked");
					}else {
						$(this.parentNode).removeClass("jstree-checked").addClass("jstree-unchecked");
					}
					
					var isChecked = $(element).hasClass("jstree-checked");
					checkedListObject = {};
					checkedListObject[elementId] = 'checked';
					
					if(currentCategoryLevel != leafNodeCategoryId) {
						updateOrganizationAndLayer(element,isChecked);							
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
						checkedListObject = {};
						checkedListObject[elementId] = 'checked';
						checkedListObject[elementId] = "checked" ;
					}else{					
						checkedListObject[elementId] = "unchecked" ;
					}
					updateOrganizationAndLayer(d.rslt[0],isChecked);
    			}
        		}
        	);
}

	function getText(element){
		var elementText  = element.childNodes[1].lastChild.data;
		return elementText;
	}


	function updateOrganizationAndLayer(element,isChecked){
		layerOptions=[];
		categoryIds=[];
		var currentlySelectedNode ="";
				assignedOrgNodeIds = "";
				assignedElement = "";
				$("#innerID").find(".jstree-checked").each(function(i, element){
					
			
							if(currentlySelectedNode=="") {
								currentlySelectedNode += "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ getText(element)+"</a>";	
							} 
			
				    		if(assignedOrgNodeIds=="") {
								assignedOrgNodeIds = $(element).attr("id");
								assignedElement = element;
							} 
			    		
					});
				
				if(currentlySelectedNode.length > 0 ) {
					$("#parentOrgName").html(currentlySelectedNode);	
				} else {
					$("#parentOrgName").html(defaultParent);	
				}
		if(currentNodeIdForEdit != $(element).attr("id")){  //added to bypass layer population in case of self parent problem
			if(isChecked) {
				var param;
				
				if(isAddOrganization){
					param = "selectedParentNode="+$(element).attr("id")+"&isAddOrganization="+isAddOrganization;
				}
				else{
					param = "selectedParentNode="+$(element).attr("id")+"&currentNodeIdForEdit="+currentNodeIdForEdit+"&isAddOrganization="+isAddOrganization;
				}

					$.ajax({
					async:		true,
					beforeSend:	function(){
									UIBlock();
								},
					url:		'populateLayer.do',
					type:		'POST',
					data:		param,
					dataType:	'json',
					success:	function(data, textStatus, XMLHttpRequest){	
									
									for(var i = 0; i < data.length; i++) {
										layerOptions[i] = data[i].categoryName;
										categoryIds[i] = data[i].orgNodeCategoryId;
									}
									fillDropDown('layerOptions',layerOptions);
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
			
			else
				fillDropDown('layerOptions',layerOptions);
		}
		else
			fillDropDown('layerOptions',layerOptions);		
	        	
	}

function populateTreeSelect() {
			$("#notSelectedOrgNodes").css("display","inline");
			$("#selectedOrgNodesName").text("");	
			$("#innerID").undelegate();
			$("#innerID").unbind();
			createMultiNodeSelectedTree (orgTreeHierarchy);	
}



 
	var isGridEmpty = true;
			
function populateGrid() {

         $("#list2").jqGrid({         
         url:'orgNodeHierarchyGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(), 
		 type:   'POST',
		 datatype: "json",         
                
          colNames:[$("#orgNameID").val(),$("#orgCodeID").val(),$("#orgLayerID").val(), $("#orgParentID").val()],
		   	colModel:[
		   	
		   	    {name:'orgNodeName',index:'orgNodeName', width:130, editable: true,align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeCode',index:'orgNodeCode', width:130, editable: true,align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeCategoryName',index:'orgNodeCategoryName', width:130, editable: true,align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'parentOrgNodeName',index:'parentOrgNodeName',editable: true, width:200, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   		
		   		
		   	],
		   	jsonReader: { repeatitems : false, root:"organizationProfileInformation", id:"orgNodeId",records: function(obj) { if(obj.organizationProfileInformation.length > 0) isGridEmpty = false; else isGridEmpty = true;  return obj.organizationProfileInformation.length; } },
		   	 
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'orgNodeName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: $("#jqGrid-content-section").width(), 
			editurl: 'OrgNodeHierarchyGrid.do',
			ondblClickRow: function(rowid) {EditOrganizationDetail();},
			caption:$("#orgListID").val(),
			onPaging: function() {
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
				document.getElementById('displayMessageMain').style.display = "none";	
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
				 if(isGridEmpty) {
				 	$('#list2').append("<tr width = '100%'><td colspan = '4'><br><br><center><div><table><tr><td><img height='23' src='/OrganizationWeb/resources/images/messaging/icon_info.gif'></td><td>&nbsp;</td><td style='padding-top: 5px'><span>There are no records associated with the selected organization</span></td></tr></table></div></center></td></tr>");
				 }
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
	 });
			jQuery("#list2").jqGrid('navGrid','#pager2',{
				addfunc: function() {
					requetForOrganization = "";
		    		AddOrganizationDetail();
		    	},
		    	editfunc: function() {
		    		 requetForOrganization = "";
		    		 EditOrganizationDetail();
		    	}
			});  
			 
}

function gridReload(){ 
           jQuery("#list2").jqGrid('setGridParam',{datatype:'json'});
           var sortArrow = jQuery("#list2");
           jQuery("#list2").jqGrid('setGridParam', {url:'orgNodeHierarchyGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(),page:1}).trigger("reloadGrid");
      	   jQuery("#list2").sortGrid('orgNodeName',true);  
           var arrowElements = sortArrow[0].grid.headers[0].el.lastChild.lastChild;
           $(arrowElements.childNodes[0]).removeClass('ui-state-disabled');
           $(arrowElements.childNodes[1]).addClass('ui-state-disabled'); 
      }
	
	
	/* function disablenextprev(selectedPosition,maxlength) {
                    selectedPosition == 0 ? $("#pData").addClass("ui-state-disabled") : $("#pData").removeClass("ui-state-disabled");
                    selectedPosition == maxlength? $("#nData").addClass("ui-state-disabled") : $("#nData").removeClass("ui-state-disabled");
                } */
    
    /* function highlightnextprev(prevSelectedRow,nextSelectedRow) {            
		   $("#"+prevSelectedRow).removeClass("ui-state-highlight").attr({
               "aria-selected": "false",
               tabindex: "-1"
           });
           $("#"+nextSelectedRow).addClass("ui-state-highlight").attr({
               "aria-selected": true,
               tabindex: "0"
           });
	} */

	
function fillDropDown( elementId, optionList) {
	var ddl = document.getElementById(elementId);
	var optionHtml = "" ;
	if(optionList.length < 1) {
		optionHtml += "<option  value='Select a layer'>Select a layer</option>";
	} else {
		for(var i = 0; i < optionList.length; i++ ) {		     
			optionHtml += "<option  value='"+ categoryIds[i]+"'>"+ optionList[i]+"</option>";	
		}
	}
	$(ddl).html(optionHtml);
}

/*
*Added on 08.11.2011 for editOrganizationDetail functionality
*/
function EditOrganizationDetail(selectedOrgId){
	isPopUp	= true;
	isAddOrganization = false;
	var rowId;
	isLasLinkCustomer = $("#isLasLinkCustomer").val();
	document.getElementById('displayMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";
	var parentNodeId ;
	if(selectedOrgId == undefined){
		rowId = $("#list2").jqGrid('getGridParam', 'selrow');
	}
	else{
		rowId = selectedOrgId;
	}
	
		$.ajax({
		async:		true,
		beforeSend:	function(){
						
						UIBlock();
					},
		url:		'getOrgDetailsForEdit.do?isLasLinkCustomer='+$("#isLasLinkCustomer").val()+'&selectedOrgId='+rowId, 
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						
						$.unblockUI();
						organizationNodes = data.organizationNodes;
						originalParentOrgId = data.organizationDetail.parentOrgNodeId;
						$("#orgName").val(data.organizationDetail.orgNodeName);
						$("#orgCode").val(data.organizationDetail.orgNodeCode);
						if(isLasLinkCustomer){
							$("#mdrNumber").val(data.organizationDetail.mdrNumber);
						}
						//alert(data.organizationDetail.orgNodeCategoryName);
						//$("#layerOptions").val(data.organizationDetail.orgNodeCategoryName);
						$("#layerOptions").html("<option  value='"+ data.organizationDetail.orgNodeCategoryId+"'>"+ data.organizationDetail.orgNodeCategoryName+"</option>");
						if(assignedOrgNodeIds == "") {
							//assignedOrgNodeIds = data.organizationDetail.parentOrgNodeId ;
							assignedOrgNodeIds = originalParentOrgId;
						} else if (isParentChange) {
							
							assignedOrgNodeIds = originalParentOrgId;
							isParentChange = false;
						}
						prevOrgNodeParentId = originalParentOrgId;
						//console.log("prevOrgNodeElement" + prevOrgNodeParentId);
						prevOrgNodeElement = document.getElementById(assignedOrgNodeIds);
						var innerHtml = "<a style='color: blue;text-decoration:underline'  href=javascript:openTreeNodes('"+data.organizationDetail.parentOrgNodeId+"');>"+trim(data.organizationDetail.parentOrgNodeName)+"</a>";	
						$("#parentOrgName").html(innerHtml);
						
					// for enable next and prev			
						var pageDataIds = $("#list2").jqGrid('getDataIDs'); 
						var str = pageDataIds;
						//var indexOfId = str.indexOf(selectedUserId);
						var indexOfId = -1;
						if(!Array.indexOf) {
							indexOfId = findIndexFromArray (str , rowId);
						} else {
							indexOfId = str.indexOf(rowId);
						}
						disablenextprev (indexOfId,(str.length) - 1);
					// for enable next and prev
						
						$("#addEditOrganizationDetail").dialog({  
								title:$("#editOrgID").val(),  
							 	resizable:false,
							 	autoOpen: true,
							 	width: '800px',
							 	modal: true,
								closeOnEscape: false,
							 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide();}
							 	});
						$('#addEditOrganizationDetail').bind('keydown', function(event) {
			 				  var code = (event.keyCode ? event.keyCode : event.which);
  							  if(code == 27){
			  				  onCancel();
			  				  return false;
			 				 }
			 				
							});	
							
							parentNodeId = data.organizationDetail.parentOrgNodeId;
							setPopupPosition(isAddOrganization);
							hideLeafAdminCheckBox();
							checkOpenNode(parentNodeId);
							dbOrgDetails = $("#addEditOrganizationDetail *").serializeArray(); 
							prepareCheckedList(); //added on 12.12.2011	
							currentNodeIdForEdit = data.organizationDetail.orgNodeId //added to bypass layer population in case of self parent problem						
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
		
	});
}

//upto this on 08.11.2011

function AddOrganizationDetail() {

	isPopUp	= true;
	isAddOrganization = true;
	isLasLinkCustomer = $("#isLasLinkCustomer").val();
	document.getElementById('displayMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";
	
						$("#addEditOrganizationDetail").dialog({  
								title:$("#addOrgID").val(),  
							 	resizable:false,
							 	autoOpen: true,
							 	width: '800px',
							 	modal: true,
								closeOnEscape: false,
							 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide();}
							 	});	
						$('#addEditOrganizationDetail').bind('keydown', function(event) {
			 				  var code = (event.keyCode ? event.keyCode : event.which);
  							  if(code == 27){
			  				  onCancel();
			  				  return false;
			 				 }
			 				
							});
	setPopupPosition(isAddOrganization);
	hideLeafAdminCheckBox();
}
	
	
	function onCancel() {
		isValueChanged = false;
		if(isAddOrganization) {
			if($("#orgName").val() != ""
			|| $("#orgCode").val() != ""
			|| ($.trim($("#parentOrgName").html()) != defaultParent) && $.trim($("#parentOrgName").html()) != defaultParentIE)
				isValueChanged = true;
			if((isLasLinkCustomer == true || isLasLinkCustomer == "true") && $.trim($("#mdrNumber").val()) != "")
				isValueChanged = true;
			
			if(isValueChanged) {
				openConfirmationPopup();	 
			} else {
				closePopUp('addEditOrganizationDetail');
			}
		}
		else {
		  	isValueChanged = isEditOrgDataChanged();
	      	if(isValueChanged) {
				openConfirmationPopup();	
			} else {
				closePopUp('addEditOrganizationDetail');
			}
		}

	}


function openTreeNodes(orgNodeId) {
	var isopened = false;
	asyncOver = 0;
	leafParentOrgNodeId = "";
	var par = null;

	$('#innerID').jstree('close_all');
	 if(isTreeExpandIconClicked )
	    return;
	
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
				  		 	
				  		 	//$("#"+currentNodeId).scroll();
							
				  		 	//$('#innerID').jstree('check_node', "#"+orgNodeId); //commented on 12.12.2011
				  		 	//$('#innerID').jstree('_fix_scroll',"#"+orgNodeId);
				  		 	//$("#"+orgNodeId).focus();
				  		 	//$("#"+orgNodeId).scroll();
				  		 	isopened = true; 
			  		 }
		 }

		
	}
	
	function reset() {
	
		$("#orgName").val("");
		$("#orgCode").val("");
		$("#layerOptions").html("<option  value='Select a layer'>Select a layer</option>");
		$("#parentOrgName").html(defaultParent);
		if(isLasLinkCustomer == true || isLasLinkCustomer == "true")
			$("#mdrNumber").val("");
		assignedOrgNodeIds = "";
		dbOrgDetails = $("#addEditOrganizationDetail *").serializeArray();//resetting dbOrgDetails for on escape problem
		originalParentOrgId = "";//resetting originalParentOrgId for on escape problem
		//populateTreeSelect();
		$('#innerID').jstree('uncheck_all');
		$('#innerID').jstree('close_all', -1);
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

	function closePopUp(dailogId){
		if(dailogId == 'addEditOrganizationDetail') {
			isPopUp = false;
				reset();			
		}
		$("#"+dailogId).dialog("close");
		
		if(dailogId == 'confirmationPopup') {
			$("#orgName").trigger("focus");
		} 
		if(dailogId == 'confirmationPopupNavigation') {
			requetForOrganization = "";				
		}
	}
	
	function closeConfirmationPopup() {
		if(isAddOrganization){
			closePopUp('confirmationPopup');
			closePopUp('addEditOrganizationDetail');
		}
		else{
			var navFlow = requetForOrganization;
			closePopUp('confirmationPopup');
			closePopUp('confirmationPopupNavigation');
			if(navFlow == "Next"){
				 fetchNextData('Edit');
			} else if(navFlow == "Previous"){
				fetchPreviousData('Edit');
			} else{
				closePopUp('addEditOrganizationDetail');
			}
		}
	}
	
	function orgDetailSubmit(){
		var validflag = VerifyOrgDetail(assignedOrgNodeIds, isLasLinkCustomer);
		if(!validflag)
			document.getElementById('displayMessage').style.display = "block";
		else
			saveOrgDetail();
	}
	
	function setPopupPosition(isAddOrganization){
				var toppos = ($(window).height() - 610) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#addEditOrganizationDetail").parent().css("top",toppos);
				$("#addEditOrganizationDetail").parent().css("left",leftpos);		 	 
				$("#Organization_Information").css("height",'300px');
				$("#Organization_Information").css("overflow",'auto');
				if(isAddOrganization) {
					$("#preButton").css("visibility","hidden");	
					$("#nextButton").css("visibility","hidden");
				}
				else{
					$("#preButton").css("visibility","visible");	
					$("#nextButton").css("visibility","visible");
				} 
	}
	
	
	function saveOrgDetail(){
	var param;
	
	if(isAddOrganization){
		param = $("#addEditOrganizationDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds + "&isLaslinkCustomer="+$("#isLasLinkCustomer").val();
	}
	else{
		var selectedOrgId = $("#list2").jqGrid('getGridParam', 'selrow');
		param = $("#addEditOrganizationDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds + "&isLaslinkCustomer="+$("#isLasLinkCustomer").val()+"&selectedOrgId="+selectedOrgId;
	}

				$.ajax(
						{
								async:		true,
								beforeSend:	function(){
												
												UIBlock();
											},
								url:		'saveAddEditOrg.do',
								type:		'POST',
								data:		param,
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
												 var parentElement =  assignedElement;
												 var thisObj = "#"+data.organizationDetail.orgNodeId;

												if(data.isEdit){
																								
												var orgArray = organizationNodes; 
												//move_node solution start
													if (assignedOrgNodeIds != originalParentOrgId) {
													
														deleteTree(data.organizationDetail.orgNodeId,data.organizationDetail.parentOrgNodeId);										
														addTree(data.organizationDetail.parentOrgNodeId,data.organizationDetail.orgNodeName,data.organizationDetail.orgNodeId,data.organizationDetail.categoryLevel);
														
													}
														
												//move_node solution end		
												}
												else{
												//alert(data.organizationDetail.parentOrgNodeId+", "+data.organizationDetail.orgNodeName+", "+data.organizationDetail.orgNodeId+", "+data.organizationDetail.categoryLevel);
												addTree(data.organizationDetail.parentOrgNodeId,data.organizationDetail.orgNodeName,data.organizationDetail.orgNodeId,data.organizationDetail.categoryLevel);
													//$("#innerID").jstree("create_node",assignedElement, "inside",  {"data":data.organizationDetail.orgNodeName,
														//"attr" : {"id" : data.organizationDetail.orgNodeId, "cid" : data.organizationDetail.categoryLevel}});
												}	
												
												if(data.organizationDetail.orgcategorylevel == leafNodeCategoryId) {
												    	assignedElement.first().hide();
												}
														
												$.unblockUI();
													
												var errorFlag = data.errorFlag;
												var successFlag = data.successFlag;
												if(successFlag) {
													closePopUp('addEditOrganizationDetail');
													//setMessageMain(data.title, data.content, data.type, "");
													$("#contentMain").text(data.content);												
													document.getElementById('displayMessageMain').style.display = "block";	
													assignedOrgNodeIds = "";
													assignedElement = "";
													
													var dataToBeAdded = {orgNodeName:data.organizationDetail.orgNodeName,
																		 orgNodeCode:data.organizationDetail.orgNodeCode,
																		 orgNodeCategoryName:data.organizationDetail.orgNodeCategoryName,
																		 parentOrgNodeName:data.organizationDetail.parentOrgNodeName};
													
													var sortOrd = jQuery("#list2").getGridParam("sortorder");
													var sortCol = jQuery("#list2").getGridParam("sortname");	
													
													if(data.isEdit) {
														jQuery("#list2").setRowData(data.organizationDetail.orgNodeId, dataToBeAdded, "first");
													}
													else {
														jQuery("#list2").addRowData(data.organizationDetail.orgNodeId, dataToBeAdded, "first");
													}
											
													jQuery("#list2").sortGrid(sortCol,true);
													checkedListObject = {}; // Added to clear checkedListObject if organization saved successfully.

												}
												else{
													setMessage(data.title, data.content, data.type, "");     
        											document.getElementById('displayMessage').style.display = "block";
												}
													
												//	$("#orgNodeHierarchy").jstree("create",$("#"+parentElement.id), "inside",  {"data":data.orgNodeName,
												//		"attr" : {"id" : data.orgNodeId, "categoryid" : data.categoryLevel}});
													
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
	}
	
	function hideLeafAdminCheckBox () {
		if(isLeafNodeAdmin) {
			$("#innerID li.jstree-leaf.jstree-unchecked").each(function(){
			
			var orgcategorylevelId = $(this).attr("cid");
    			if(orgcategorylevelId == leafNodeCategoryId) {
	    			$("a ins.jstree-checkbox", this).first().hide();
	    		}
			}); 
		}
	}
	
	function hideCheckBox(){
		$("#innerID li").each(function() {
    			var orgcategorylevel = $(this).attr("cid");
    			if(orgcategorylevel == leafNodeCategoryId) {
	    		  $("a ins.jstree-checkbox", this).first().hide();
	    		}
	  	});
	
	}
//added on 09.11.2011
function isEditOrgDataChanged(){

		var newUserValue = $("#addEditOrganizationDetail *").serializeArray(); 
		isValueChanged = false;	
		
		if(dbOrgDetails.length != newUserValue.length) {
			isValueChanged = true;
		}
		 if(!isValueChanged) {
		      for(var key = 0; key <dbOrgDetails.length ; key++) {
		       if(newUserValue[key].name != dbOrgDetails[key].name) {
		       	isValueChanged = true;
		       	break;
		       }
		       if(trim(newUserValue[key].value) != trim(dbOrgDetails[key].value)){
		      		isValueChanged = true;
		      		break;
		      	}
		      	 
		      }
	      }
	      //var assignedOrgNodeIdsList = [];
	          /*if(String(assignedOrgNodeIds).indexOf(",") > 0) {
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
		     	} else {*/
		     		if(assignedOrgNodeIds != ""){
					      if(String(originalParentOrgId) != assignedOrgNodeIds) {
					      		isValueChanged = true;
					      }
 	
				     } 
				     else {
				     		if(String(originalParentOrgId)!= "")//added for escape problem
				     			isValueChanged = true;
				     }
      	return  isValueChanged;
}

//Added on 10.11.2011

function nDataClick(popupname) {
	requetForOrganization = "Next";
	var isValueChanged = false;
		if(popupname == 'Edit') {
			isValueChanged = isEditOrgDataChanged();
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
           	requetForOrganization = "Previous";
           		var isValueChanged = false;
	if(popupname == 'Edit') {
		isValueChanged = isEditOrgDataChanged();
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
			//$("#displayMessage").text("");
			//assignedOrgNodeIds = "";
			if (assignedOrgNodeIds != originalParentOrgId) {
							
				isParentChange = true;
			}
			
			var selectedOrgId = $("#list2").jqGrid('getGridParam', 'selrow');
			var pageRows = $("#list2").jqGrid('getGridParam','rowNum');
			var curPage = parseInt($('#list2').jqGrid('getGridParam','page')); 
	        var str = idarray;
			var nextOrgId ;
			var pageDataIds = $("#list2").jqGrid('getDataIDs'); 
			//alert("Selected:["+selectedOrgId+"]fetchNextData:"+pageDataIds);
			str = pageDataIds;
			//var indexOfId = str.indexOf(selectedOrgId);
			var indexOfId = -1;
			if(!Array.indexOf) {
				indexOfId = findIndexFromArray (str , selectedOrgId);
			} else {
				indexOfId = str.indexOf(selectedOrgId);
			}
			//alert("indexOfId :"+indexOfId+"::"+str.length+"::"+pageRows+"::"+curPage);
	
			if(indexOfId != str.length-1) {
				if(indexOfId%pageRows != pageRows-1){	//if(indexOfId != (pageRows*curPage)-1){
					nextOrgId = str[indexOfId + 1];
					highlightnextprev(selectedOrgId, nextOrgId);
		      			if(popupname == 'Edit')
		      				EditOrganizationDetail(nextOrgId);
		      			//else
		      				//viewUserDetail(nextOrgId);
		      			disablenextprev(indexOfId+1,str.length-1);
		      			populateTreeSelect();
		      			$("#list2").setSelection(nextOrgId, true); 
	      		}
			}
			requetForOrganization = "";
		}
		
function fetchPreviousData(popupname){
	//$("#displayMessage").text("");
	//assignedOrgNodeIds = "";
	if (assignedOrgNodeIds != originalParentOrgId) {
							
			isParentChange = true;
	}
	var selectedOrgId = $("#list2").jqGrid('getGridParam', 'selrow');
	var pageRows = $("#list2").jqGrid('getGridParam','rowNum');
	var curPage = parseInt($('#list2').jqGrid('getGridParam','page'));
          var str = idarray;
	var preOrgId ;
	var pageDataIds = $("#list2").jqGrid('getDataIDs'); 
	//alert("Selected:["+selectedOrgId+"fetchPreviousData:"+pageDataIds);
	str = pageDataIds;
	var indexOfId = -1;
	if(!Array.indexOf) {
		indexOfId = findIndexFromArray (str , selectedOrgId);
	} else {
		indexOfId = str.indexOf(selectedOrgId);;
	}

	//alert("indexOfId :"+indexOfId+"::"+str.length+"::"+pageRows+"::"+curPage);

	if(indexOfId%pageRows != 0){	//if(indexOfId != (pageRows*(curPage-1))){
			preOrgId = str[indexOfId - 1];
			highlightnextprev(selectedOrgId, preOrgId);
       		if(popupname == 'Edit')
       			EditOrganizationDetail(preOrgId);
       		//else
       			//viewUserDetail(preOrgId);
       		disablenextprev(indexOfId-1,str.length-1);
       		populateTreeSelect();
       		$("#list2").setSelection(preOrgId, true); 
    }	
	requetForOrganization = "";
}

function findIndexFromArray (myArray, obj) {
    
    for(var i=0; i<myArray.length; i++){
	            if(myArray[i]==obj){
	                return i;
	            }
	        }
	        return -1;   
}

function highlightnextprev(prevSelectedRow,nextSelectedRow) {            
		   $("#"+prevSelectedRow,"#list2").removeClass("ui-state-highlight").attr({
               "aria-selected": "false",
               tabindex: "-1"
           });
           $("#"+nextSelectedRow,"#list2").addClass("ui-state-highlight").attr({
               "aria-selected": true,
               tabindex: "0"
           });
}
			
function disablenextprev(selectedPosition,maxlength) {
               if(selectedPosition == 0 || ((selectedPosition%20)==0)) {
                	$("#pData").addClass("ui-state-disabled") ;
                    //$("#viewpData").addClass("ui-state-disabled");
              
               } else {
						$("#pData").removeClass("ui-state-disabled");
                       // $("#viewpData").removeClass("ui-state-disabled");
               }
               
                if ((selectedPosition == maxlength) || (((selectedPosition+1)%20) ==0)) {
               		$("#nData").addClass("ui-state-disabled");
                   // $("#viewnData").addClass("ui-state-disabled");
               
               } else {
               		$("#nData").removeClass("ui-state-disabled");
                    //$("#viewnData").removeClass("ui-state-disabled");
               }
}

function checkOpenNode(parentOrgNodeId){

		var isIdExist = $('#'+parentOrgNodeId,'#innerID').length;
			if(isIdExist > 0){
				$("#"+parentOrgNodeId,'#innerID').removeClass("jstree-unchecked").addClass("jstree-checked");
				$('#innerID').jstree('close_all', -1);
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
	
		currentElement.className = "jstree-open jstree-"+ getCheckedStatus(currentNodeId);
		var fragment = document.createDocumentFragment();
		var ulElement = document.createElement('ul');
		
		if(type.indexOf("innerID") < 0){
		stream(objArray,ulElement,fragment,streamPush, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
		 });	
		 }
		 else{
		 stream(objArray,ulElement,fragment,streamInnerPush, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading');
			asyncOver++;
			openNextLevel(asyncOver);
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
		if (objArray.attr.cid != leafNodeCategoryId){
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: inline-block;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}else{
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}
		ulElement.appendChild(liElement);
		fragment.appendChild(ulElement);
	  }
  
  	function populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot){	
	//TODO : Updation in root node is a problem need to work on that but this method is also necessary for populating the immediate //tree because if we use the cache all the 2nd level objects will be stored in cache which makes the cache very heavy
		var jsonObject = jsonData;
		jsonObject = jsonObject[indexOfRoot];
		if (dataObj2.length == 0){
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
 //added on 12.12.2011
	function prepareCheckedList(){	
		var orgList = assignedOrgNodeIds;
		orgList = String(orgList).split(",");	
		for(var i = 0; i < orgList.length; i++) {	
			checkedListObject[orgList[i]] = "checked";	
		}
	}
////added on 08.12.2011  
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
								//alert("case 2 -> " + map.get(currentNodeId));
							break;							
					case "3": 	dataObj4 = map.get(element);
								currentTreeArray =getObject(dataObj4,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//alert("case 3 -> " + map.get(currentNodeId));
							break;
					case "4": 	dataObj5 = map.get(element);
								currentTreeArray =getObject(dataObj5,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//alert("case 4 -> " + map.get(currentNodeId));
							break;
					case "5": 	dataObj6 = map.get(element);
								currentTreeArray =getObject(dataObj6,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//alert("case 5 -> " + map.get(currentNodeId));
							break;
					case "6": 	dataObj7 = map.get(element);
								currentTreeArray =getObject(dataObj7,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "7": 	dataObj8 =map.get(element.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj8,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);								
							break;	
					case "8": 	dataObj9 =map.get(element.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj9,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;						
					
				}
			}

		}
 }  
  
////
  function openNextLevel(asyncOver){ 

  		if(leafParentOrgNodeId.length - 1 > asyncOver) {
	  		var tmpNode = leafParentOrgNodeId[asyncOver];	
			currentCategoryLevel = String(asyncOver + 1);
			currentNodeId = tmpNode;
			currentTreeArray = map.get(currentNodeId);
			//$("#"+currentNodeId).scroll();
			$('#innerID').jstree('open_node', "#"+currentNodeId);
		}
  }
  
		/******CRUD Operations****/
	function addTree(currentSelectedId,addedOrgName,addedOrgId,addedOrgCategoryId){
		var parentElementBeforeAdd = document.getElementById(currentSelectedId);
		var tclIndex  = parseInt(parentElementBeforeAdd.getAttribute("tcl"));
		if (addedOrgCategoryId == null || addedOrgCategoryId == undefined ){
		addedOrgCategoryId = parseInt(parentElementBeforeAdd.getAttribute("cid")) + 1;
		}
		prepareData(false,parentElementBeforeAdd.getAttribute("cid"),currentSelectedId,parentElementBeforeAdd.parentNode.parentNode.id);
		var obj = map.get(currentSelectedId);
		var addedObject = map.get(addedOrgId);

		var index = getIndexOfRoot(currentSelectedId);
		var isLeaf = $(parentElementBeforeAdd).hasClass("jstree-leaf");	
		var isTreeOpen =  $(parentElementBeforeAdd).hasClass("jstree-open");	
			 if (isLeaf){
			 $(parentElementBeforeAdd).removeClass("jstree-leaf").addClass("jstree-closed");	
			 }
			 else{
			 //Nothing
			 }
		 //Need to use updateJsonData also for the root alone because second node is populated from json data and not the cache
		if (parentElementBeforeAdd.getAttribute("cid") ==1){
			//console.log("AddObject" + obj);		
			if(obj == null || obj == undefined)
				obj = [];
	     	obj.push({data: addedOrgName,attr:{id: addedOrgId,cid: addedOrgCategoryId,tcl: String(tclIndex+1),chlen:undefined}});
	     	obj.children = [];
			jsonData[index].children.push({data: addedOrgName,attr:{id: addedOrgId,cid: addedOrgCategoryId,tcl: String(tclIndex+1),chlen:undefined}});
		}
		
		//In all the other cases we need to just update the cache since data is already in cache and its populated only from cache which is not the case for 2nd level
		//For levels 3 to n we have to consider two things before adding the elements to cache first whether the element has children or not.
		if (parentElementBeforeAdd.getAttribute("cid") > 1 && obj.hasOwnProperty("children")){
			if (addedObject != null && addedObject != undefined && addedObject.children != undefined){
			 obj.children.push({data: addedOrgName,attr:{id: addedOrgId,cid: addedOrgCategoryId,tcl: String(tclIndex+1),chlen:undefined},children: addedObject.children}); 
			 }else {
			 obj.children.push({data: addedOrgName,attr:{id: addedOrgId,cid: addedOrgCategoryId,tcl: String(tclIndex+1),chlen:undefined}});
			 }		 
		 map.put(currentSelectedId,obj);
		 tclIndex = tclIndex +1;
		}
		else if (parentElementBeforeAdd.getAttribute("cid") > 1) {
		 obj.children = [];
		 obj.children.push({data: addedOrgName,attr:{id: addedOrgId,cid: addedOrgCategoryId,tcl: String(tclIndex+1),chlen:undefined}});	 
		 map.put(currentSelectedId,obj);
		 tclIndex = tclIndex +1;
		}
		 
		 currentNodeId = currentSelectedId;
		 if (isTreeOpen){
		  if (addedObject != null && addedObject.hasOwnProperty("children")){
				$("#"+currentNodeId+ "> ul").append("<li id ="+addedOrgId+" class=\"jstree-closed jstree-unchecked\" cid="+addedOrgCategoryId + " tcl="+tclIndex + "><ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + addedOrgName  + "</a></li> ");			
			}else{
				$("#"+currentNodeId+ "> ul").append("<li id ="+addedOrgId+" class=\"jstree-leaf jstree-unchecked\" cid="+addedOrgCategoryId + " tcl="+tclIndex + "><ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + addedOrgName  + "</a></li> ");
			}
			
		 }
		}

		function deleteTree(currentNodeId){
		  
		  var prevElement =$('#'+currentNodeId,'#orgNodeHierarchy'); 	
		  if (prevElement != undefined || prevElement != null )	prevElement = prevElement[0];	

		 var path="";
		 var currentClickedElement =$('#'+prevOrgNodeParentId,'#orgNodeHierarchy'); 
		 if (currentClickedElement != undefined || currentClickedElement != null )	currentClickedElement = currentClickedElement[0];	
		 var currClickElmtPrntId = currentClickedElement.parentNode.parentNode.id;
 
		 //preparing cache for the current clicked element and the node that has to be moved
		 if (currentClickedTcl > 1){
		  prepareData(false,currentClickedTcl,currentClickedId,currClickElmtPrntId);
		  var nextTcl = parseInt(currentClickedTcl)+1;
		  prepareData(false,String(nextTcl),currentNodeId,prevOrgNodeParentId);	
		  var obj= map.get(prevOrgNodeParentId); 
		  var index = getIndexFromCache(obj.children,currentNodeId);
		  obj.children.splice(index,1);
		  map.put(prevOrgNodeParentId,obj);			   	
		  }
		  //Preparing cache only for the node that has to be moved because the current clicked element is not 
		  //cached sicne its category level 1		  
		  if (currentClickedTcl == 1){		  
		  var nextTcl = parseInt(currentClickedTcl)+1;
		  prepareData(false,String(nextTcl),currentNodeId,prevOrgNodeParentId);	
		  var index = getIndexOfRoot(prevOrgNodeParentId);
    	  	for (var i = 0, j = jsonData[index].children.length; i < j; i++ ){
				var attrId = jsonData[index].children[i].attr.id;
					if (attrId == currentNodeId){
						map.put(currentNodeId,jsonData[index].children[i]);
						jsonData[index].children.splice(i,1);
					}
				}
		  }
	 
		  if(prevElement != null || prevElement != undefined){
		   var prevElementParent =prevElement.parentNode;	
		   var refPrevElement = prevElementParent.removeChild(prevElement);
		  
		  }
		}

	//Returns elements index in cache of json data
	function getIndexFromCache(obj,elementId){	
		for (var i=0, j = obj.length; i <j;i++){
			if(obj[i].attr.id == elementId){
			return i;
			}
		}
	}





