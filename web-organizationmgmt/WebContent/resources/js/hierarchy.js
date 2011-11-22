
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
//
var isLeafNodeAdmin;

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
				"plugins" : [ "themes", "json_data", "ui", "crrm"]  
				
	    });
	    
	    $("#orgNodeHierarchy").delegate("a","click", function(e) {
	    	document.getElementById('displayMessageMain').style.display = "none";
			 	 
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
	   
}
	
	
	
	
	
function createMultiNodeSelectedTree(jsondata) {
var styleClass;
  $("#innerID").jstree({
        "json_data" : {	             
            "data" : jsondata.data,
			"progressive_render" : true
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
    }).bind("open_node.jstree", function (e, data){
    
    	hideCheckBox();
         isTreeExpandIconClicked = true;
    	 $(this).find("li").each(function(i, element) { 
    		 var childOrgId = $(element).attr("id");
    		 if(assignedOrgNodeIds != ""){
    		 
    		 if(String(assignedOrgNodeIds).indexOf(",") > 0) {
				 var orgList = assignedOrgNodeIds.split(",");
				 for(var key=0; key < orgList.length; key++){
				 	var keyVal = $.trim(orgList[key]);
				  	if(keyVal == childOrgId)
				  	{
				  		isAction = false;
				  		data.inst.check_node("#"+keyVal, true);  
				  		isAction = true;
				  	}
				  				
				}
			} else {
			isAction = false;
				  data.inst.check_node("#"+ assignedOrgNodeIds, true); 
				  isAction = true; 
			}
    		 
    		 
    		 }
    	 
    	 });
    	    isTreeExpandIconClicked = false;
    
    });
    
    	$("#innerID").delegate("li a","click",
    		 function(e) {
    				styleClass = $(this.parentNode).attr('class');
    				var element = this.parentNode;
    				var orgcategorylevel = $(element).attr("categoryID");
					if(styleClass.indexOf("unchecked") > 0){
						$('#innerID').jstree('uncheck_all');
						$(this.parentNode).removeClass("jstree-unchecked").addClass("jstree-checked");
					}else {
						$(this.parentNode).removeClass("jstree-checked").addClass("jstree-unchecked");
					}
					
					var isChecked = $(element).hasClass("jstree-checked");
					
					if(orgcategorylevel != leafNodeCategoryId) {
						updateOrganizationAndLayer(element,isChecked);							
					} 
						
			   	 }
			  );
    
   		 $("#innerID").bind("change_state.jstree",
   		 	 function (e, d) {
   		 	 if(isAction){ 
				var isChecked = $(d.rslt[0]).hasClass("jstree-checked");
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
	
	if(isChecked) {
		$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'populateLayer.do?selectedParentNode='+$(element).attr("id"),
		type:		'POST',
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

function populateTreeSelect() {
			$("#notSelectedOrgNodes").css("display","inline");
			$("#selectedOrgNodesName").text("");	
			$("#innerID").undelegate();
			createMultiNodeSelectedTree (orgTreeHierarchy);	
}



 
	var isGridEmpty = true;
			
function populateGrid() {

         $("#list2").jqGrid({         
         url:'orgNodeHierarchyGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(), 
		 type:   'POST',
		 datatype: "json",         
                
          colNames:['Name','Org Code','Layer', 'Parent Org'],
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
			width: 975, 
			editurl: 'OrgNodeHierarchyGrid.do',
			ondblClickRow: function(rowid) {EditOrganizationDetail();},
			caption:"Organization List",
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
	
	
	function disablenextprev(selectedPosition,maxlength) {
                    selectedPosition == 0 ? $("#pData").addClass("ui-state-disabled") : $("#pData").removeClass("ui-state-disabled");
                    selectedPosition == maxlength? $("#nData").addClass("ui-state-disabled") : $("#nData").removeClass("ui-state-disabled");
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
						originalParentOrgId = data.parentOrgNodeId;
						$("#orgName").val(data.orgNodeName);
						$("#orgCode").val(data.orgNodeCode);
						//alert(data.orgNodeCategoryName);
						//$("#layerOptions").val(data.orgNodeCategoryName);
						$("#layerOptions").html("<option  value='"+ data.orgNodeCategoryId+"'>"+ data.orgNodeCategoryName+"</option>");
						if(assignedOrgNodeIds == "") {
							//assignedOrgNodeIds = data.parentOrgNodeId ;
							assignedOrgNodeIds = originalParentOrgId;
						}
						
						var innerHtml = "<a style='color: blue;text-decoration:underline'  href=javascript:openTreeNodes('"+data.parentOrgNodeId+"');>"+trim(data.parentOrgNodeName)+"</a>";	
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
								title:"Edit Organization",  
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
							
							parentNodeId = data.parentOrgNodeId;
							setPopupPosition(isAddOrganization);
							hideLeafAdminCheckBox();
							checkOpenNode(parentNodeId);
							dbOrgDetails = $("#addEditOrganizationDetail *").serializeArray(); 
								
		 						
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
								title:"Add Organization",  
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
	
	 if(isTreeExpandIconClicked )
	    return;
	
		var isIdExist = $('#innerID', '#'+assignedOrgNodeIds).length;
			if(isIdExist > 0){
				$('#innerID').jstree('check_node', "#"+assignedOrgNodeIds);
				isopened = true; 
			} else {
				
			  		 	$('#innerID').jstree('open_node', "#"+orgNodeId); 
			  		 	 $('#innerID').jstree('check_node', "#"+orgNodeId);
		 }
		 if(!isopened) {
			var parentOrgNodeId = $("#" + orgNodeId).parent("ul");
			
			var ancestorNodes = parentOrgNodeId.parentsUntil(".jstree","li");
			//open tree nodes from root to the clicked node	
			if(ancestorNodes.length > 0) {
				for(var count = ancestorNodes.length - 1; count >= 0; --count) {
		  		 		var tmpNode = ancestorNodes[count].id;
						$('#innerID').jstree('open_node', "#"+tmpNode); 
					
		  		 }
		  		 $('#innerID').jstree('check_node', "#"+orgNodeId); 
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
				title:"Confirmation Alert",  
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
			title:"Confirmation Alert",  
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
												
												//move_node solution start
													if (assignedOrgNodeIds != originalParentOrgId) {
													
																								
														$('#innerID').jstree('open_node', "#"+originalParentOrgId); 
														$(thisObj,"#innerID").remove();
														if ($("#"+originalParentOrgId,"#innerID").has("li").length == 0) {
														
															$("#"+originalParentOrgId,"#innerID").removeClass("jstree-closed");
															$("#"+originalParentOrgId,"#innerID").addClass("jstree-leaf");
																														
														}
														
														var nodeData = data.baseTree.data[0];
														var pNode = assignedElement;
														var id;
														while (true) {
															$("#innerID").jstree("create_node",pNode, "inside",  {"data":nodeData.data,
															"attr" : {"id" : nodeData.attr.id, "categoryID" : nodeData.attr.categoryID}});
															id=nodeData.attr.id;
															if (nodeData.children.length > 0) {
															
																nodeData = nodeData.children[0];
																
																pNode = "#"+id;
															} else {
															
																break;
															}
														}
														
													}
														
												//move_node solution end		
												}
												else{
													$("#innerID").jstree("create_node",assignedElement, "inside",  {"data":data.organizationDetail.orgNodeName,
														"attr" : {"id" : data.organizationDetail.orgNodeId, "categoryID" : data.organizationDetail.categoryLevel}});
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
			
			var orgcategorylevelId = $(this).attr("categoryID");
    			if(orgcategorylevelId == leafNodeCategoryId) {
	    			$("a ins.jstree-checkbox", this).first().hide();
	    		}
			}); 
		}
	}
	
	function hideCheckBox(){
		$("#innerID li").each(function() {
    			var orgcategorylevel = $(this).attr("categoryID");
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
			assignedOrgNodeIds = "";
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
	assignedOrgNodeIds = "";
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









