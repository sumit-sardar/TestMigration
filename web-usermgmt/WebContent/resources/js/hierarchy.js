
var gridjsondata;
var idarray =[];
var gridloaded = false;
var leafNodeCategoryId;
var orgTreeHierarchy;
var SelectedOrgNodeId;

function populateTree() {
	
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
						leafNodeCategoryId = data.leafNodeCategoryId;
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
	$.blockUI({ message: '<img src="/UserManagementWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'45px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}
			

function createSingleNodeSelectedTree(jsondata) {
	   $("#orgNodeHierarchy").jstree({
	        "json_data" : {	             
	            "data" : jsondata.data,
				"progressive_render" : true,
				"progressive_unload" : true
	        },
	        "ui" : {  
	           "select_limit" : 1
         	}, 
				"plugins" : [ "themes", "json_data", "ui"]  
				
	    });
	    
	    $("#orgNodeHierarchy").delegate("a","click", function(e) {
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

	$("#innerID").bind("loaded.jstree", function (event, data) {  
		 $('#innerID').jstree('open_node', '#'+SelectedOrgNodeId);   
     });  


  $("#innerID").jstree({
        "json_data" : {	             
            "data" : jsondata.data,
			"progressive_render" : true,
			"progressive_unload" : true
        },
        "ui" : {  
           "select_limit" : 1
        	}, 
        	
			"themes" : {
			"theme" : "apple",
			"dots" : false,
			"icons" : true
			},         	
         	
		"plugins" : [ "themes", "json_data", "checkbox"]
    });
    
    
    
		
		
		 $("#innerID").delegate("a","click", function(e) {
		 	
		 	var checked_ids = [];
			var currentlySelectedNode ="";
			var checked_ids = $("#innerID .jstree-checked a");
			for(var i= 0; i<checked_ids.length; i++){
				currentlySelectedNode = currentlySelectedNode + checked_ids[i].text();
				 $("#selectedOrgNodesName").text(currentlySelectedNode);
			}
		
  			
  		
		});
    
    
   
}


function  searchUser(){
	SelectedOrgNodeId= $("#orgNodeHierarchy a.jstree-clicked").parent().attr("id");
	$("#treeSelectedOrgNodeId").val(SelectedOrgNodeId);
	setElementValueAndSubmitWithAnchor('actionElement', 'actionElement', 'userSearchResult');
	
}

function populateTreeSelect() {
			//$("#notSelectedOrgNodes").css("visibility","visible");
			$("#selectedOrgNodesName").text("");	
			createMultiNodeSelectedTree (orgTreeHierarchy);	

}



 
		
			
function populateGrid() {

         $("#list2").jqGrid({         
         url:'userOrgNodeHierarchyGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(), 
		 type:   'POST',
		 datatype: "json",         
                
          colNames:['Last Name','First Name','Login ID', 'Role', 'Email','Organization'],
		   	colModel:[
		   	
		   	    {name:'lastName',index:'lastName', width:130, editable: true,align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:130, editable: true,align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'loginId',index:'loginId', width:130, editable: true,align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'role',index:'role',editable: true, width:200, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		     	{name:'email',index:'email',editable: true, width:150,align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr',editable: true, align:"left" ,width:200, edittype:"newtree",editoptions:{readonly:true},sortable:true,sorttype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   		
		   	],
		   	jsonReader: { repeatitems : false, root:"userProfileInformation", id:"userId",records: function(obj) { return obj.userProfileInformation.length; } },
		   	 
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'empId', 
			viewrecords: true, 
			sortorder: "desc",
			height: 370,  
			editurl: 'userOrgNodeHierarchyGrid.do',
			caption:"Search Result",
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
				 
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
	 });
			jQuery("#list2").jqGrid('navGrid','#pager2',{});  
			$('.ui-jqgrid-titlebar-close',"#list2").remove();
			
			
}

 function gridReload(){ 
           jQuery("#list2").jqGrid('setGridParam',{datatype:'json'});
           jQuery("#list2").jqGrid('setGridParam', {url:'userOrgNodeHierarchyGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(),page:1}).trigger("reloadGrid");
      }


	
	function userDetailEdit(){
	
	 for(var i=0;i<gridjsondata.userProfileInformation.length;i++) {
		 if(SelectedUserId == gridjsondata.userProfileInformation[i].userId) {									
			$("#userFirstName").val(gridjsondata.userProfileInformation[i].firstName);		
			$("#userMiddleName").val(gridjsondata.userProfileInformation[i].middleName);		
			$("#userLastName").val(gridjsondata.userProfileInformation[i].lastName);		
			$("#userEmail").val(gridjsondata.userProfileInformation[i].email);		
			$("#userExternalId").val(gridjsondata.userProfileInformation[i].addressLine1);		
			$("#addressLine1").val(gridjsondata.userProfileInformation[i].userContact.addressLine1);		
			$("#addressLine2").val(gridjsondata.userProfileInformation[i].userContact.addressLine2);		
			$("#city").val(gridjsondata.userProfileInformation[i].city);
			$("#secondaryPhone4").val(gridjsondata.userProfileInformation[i].userContact.secondaryPhone4);	
			$("#faxNumber1").val(gridjsondata.userProfileInformation[i].userContact.faxNumber1);	
			$("#faxNumber2").val(gridjsondata.userProfileInformation[i].userContact.faxNumber2);	
			$("#faxNumber3").val(gridjsondata.userProfileInformation[i].userContact.faxNumber3);									
		}	

	}
	$("#editUserDetail").dialog({  
								title:"Edit Record",  
							 	resizable:false,
							 	autoOpen: true,
							 	width: '600px'
							 	});	
	$("#preButton").css("visibility","visible");	
	$("#nextButton").css("visibility","visible");	
	$('#accordion ul:eq(0)').show();
	
	}
	
	function userDetailAdd(){
	$("#userFirstName").val("");	
	$("#userLastName").val("");	
	$("#userMiddleName").val("");		
	$("#userEmail").val("");		
	$("#userExternalId").val("");		
	$("#addressLine1").val("");		
	$("#addressLine2").val("");		
	$("#city").val("");
	$("#secondaryPhone4").val("");	
	$("#faxNumber1").val("");	
	$("#faxNumber2").val("");	
	$("#faxNumber3").val("");		
	$("#preButton").css("visibility","hidden");	
	$("#nextButton").css("visibility","hidden");	
	$("#editUserDetail").dialog({  
								title:"Add Record",  
							 	resizable:false,
							 	autoOpen: true,
							 	width: '600px'
							 	});	
	$('#accordion ul:eq(0)').show();
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
	
	function closePopUp(){
		$("#editUserDetail").dialog("close");
	}
	
	function userDetailSubmit(){
		$("#"+SelectedUserId+" td:eq(2)").text($("#userEmail").val());
		//$("#"+SelectedUserId+" td").each(function() { 
		//    alert($(this).attr('title'));
		//});
	}
	
	
	
	
	
	
	
	
	






			
		



