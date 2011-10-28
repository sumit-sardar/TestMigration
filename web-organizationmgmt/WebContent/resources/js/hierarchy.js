
var gridjsondata;
var idarray =[];
var gridloaded = false;

var orgTreeHierarchy;
var SelectedOrgNodeId ;
var assignedOrgNodeIds="";

var isPopUp = false;

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
						UIBlock()
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
				"icons" : true
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
        "checkbox" : {
        "two_state" : true
        }, 
        	
			"themes" : {
			"theme" : "apple",
			"dots" : false,
			"icons" : true
			},         	
         	
		"plugins" : [ "themes", "json_data", "checkbox"]
    });
    
   
    	$("#innerID").delegate("li a","click",
    		 function(e) {
    				styleClass = $(this.parentNode).attr('class');
					if(styleClass.indexOf("unchecked") > 0){
					$(this.parentNode).removeClass("jstree-unchecked").addClass("jstree-checked");
					}else {
					$(this.parentNode).removeClass("jstree-checked").addClass("jstree-unchecked");
					}
					updateOrganization();
			   	 }
			  );
    
   		 $("#innerID").bind("change_state.jstree",
   		 	 function (e, d) { 
				updateOrganization();
        		}
        	);
}


	function updateOrganization(){
	    	var currentlySelectedNode ="";
	    	assignedOrgNodeIds = "";
	        
			$("#innerID").find(".jstree-checked").each(
				function(i, element){
						if(currentlySelectedNode=="") {
							currentlySelectedNode = getText(element);
						} else {
							currentlySelectedNode = currentlySelectedNode + " , " + getText(element);
						}
			    		if(assignedOrgNodeIds=="") {
							assignedOrgNodeIds = $(element).attr("id")+ "|" + $(element).attr("customerId");
						} else {
							assignedOrgNodeIds = $(element).attr("id")+ "|" + $(element).attr("customerId") +"," + assignedOrgNodeIds;
						}
					}
				);
				
			if(currentlySelectedNode.length > 0 ) {
					$("#notSelectedOrgNodes").css("display","none");
					$("#selectedOrgNodesName").text(currentlySelectedNode);	
				} else {
					$("#notSelectedOrgNodes").css("display","inline");
					$("#selectedOrgNodesName").text("");	
			}
	        	
	}


function getText(element){
	var elementText  = element.childNodes[1].lastChild.data;
	return elementText;
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
			jQuery("#list2").jqGrid('navGrid','#pager2',{});  
			 
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
	
