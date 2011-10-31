
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
var isLasLinkCustomer = false;

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
						createSingleNodeSelectedTree (orgTreeHierarchy);
						$("#searchheader").css("visibility","visible");	
						$("#orgNodeHierarchy").css("visibility","visible");						
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					},
		complete :  function(){
						 $.unblockUI(); 
					}
	});

}

function UIBlock(){
	$.blockUI({ message: '<img src="/OrganizationManagementWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
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
				"plugins" : [ "themes", "json_data", "ui"]  
				
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
         	
		"plugins" : [ "themes", "json_data","ui","checkbox"]
    }).bind("open_node.jstree", function (e, data){
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
					if(styleClass.indexOf("unchecked") > 0){
					$(this.parentNode).removeClass("jstree-unchecked").addClass("jstree-checked");
					}else {
					$(this.parentNode).removeClass("jstree-checked").addClass("jstree-unchecked");
					}
					
					var isChecked = $(element).hasClass("jstree-checked");
    			
					updateOrganizationAndLayer(element,isChecked);
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
				$("#innerID").find(".jstree-checked").each(function(i, element){
					
			
							if(currentlySelectedNode=="") {
								currentlySelectedNode += "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ getText(element)+"</a>";	
							} 
			
				    		if(assignedOrgNodeIds=="") {
								assignedOrgNodeIds = $(element).attr("id");
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
						window.location.href="/TestSessionInfoWeb/logout.do";
						
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
				 	$('#list2').append("<tr width = '100%'><td colspan = '4'><br><br><center><div style = 'border: solid 3px; width: 80%;'><span><h2>There are no records associated with the selected organization</h2></span></div></center></td></tr>");
				 }
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
	 });
			jQuery("#list2").jqGrid('navGrid','#pager2',{
				addfunc: function() {
					requetForOrganization = "";
		    		AddOrganizationDetail();
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
	
	
	function setPopupPosition(){
				var toppos = ($(window).height() - 610) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#addEditOrganizationDetail").parent().css("top",toppos);
				$("#addEditOrganizationDetail").parent().css("left",leftpos);		 	 
				//$("#Organization_Information").css("height",'300px');
				//$("#Organization_Information").css("overflow",'auto');
				
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
}
	
	
	function onCancel() {
		var isValueChanged = false;
		if(isAddOrganization) {
			if($.trim($("#orgName").val()) != ""
			|| $.trim($("#orgCode").val()) != ""
			|| $.trim($("#parentOrgName").html()) != defaultParent)
				isValueChanged = true;
			if(isLasLinkCustomer == true && $.trim($("#mdrNumber").val()) != "")
				isValueChanged = true;
		}

			if(isValueChanged) {
				openConfirmationPopup();	 
			} else {
				closePopUp('addEditOrganizationDetail');
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
		if(isLasLinkCustomer == true)
			$("#mdrNumber").val("");
		assignedOrgNodeIds = "";
		populateTreeSelect();
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
	}
	
	function closeConfirmationPopup() {
		if(isAddOrganization){
			closePopUp('confirmationPopup');
			closePopUp('addEditOrganizationDetail');
		}
	}
	
	function orgDetailSubmit(){
		var validflag = VerifyOrgDetail(assignedOrgNodeIds, isLasLinkCustomer);
		if(!validflag)
			document.getElementById('displayMessage').style.display = "block";
		else
			saveOrgDetail()
	}
	
	function setPopupPosition(isAddUser){
				var toppos = ($(window).height() - 610) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#addEditOrganizationDetail").parent().css("top",toppos);
				$("#addEditOrganizationDetail").parent().css("left",leftpos);		 	 
				$("#Organization_Information").css("height",'300px');
				$("#Organization_Information").css("overflow",'auto');
				if(isAddUser) {
					$("#preButton").css("visibility","hidden");	
					$("#nextButton").css("visibility","hidden");
				} 
	}
	
	
	function saveOrgDetail(){
	var param;
	
	if(isAddOrganization){
		param = $("#addEditOrganizationDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds + "&isLaslinkCustomer="+$("#isLasLinkCustomer").val();
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
												$.unblockUI();  

													closePopUp('addEditOrganizationDetail');
													setMessageMain("Add Organization", "Organization was added successfully.", "", "");
													
													document.getElementById('displayMessageMain').style.display = "block";	
													assignedOrgNodeIds = "";

																								
											},
								error  :    function(XMLHttpRequest, textStatus, errorThrown){
													$.unblockUI();  
												window.location.href="/TestSessionInfoWeb/logout.do";
											},
								complete :  function(){
												$.unblockUI();  
											}
						}
					);
	}
