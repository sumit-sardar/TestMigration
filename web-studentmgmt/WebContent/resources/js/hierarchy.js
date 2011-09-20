
var gridjsondata;
var idarray =[];
var gridloaded = false;
function populateTree() {
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						
						$.blockUI({ message: null }); 
					},
		url:		'userOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						//alert('in');
						$.unblockUI();  
						createSingleNodeSelectedTree (data);
						$("#searchheader").css("visibility","visible");	
												
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

function blockUI(){	
	$("body").append('<div id="blockDiv" style="position:fixed;top:0;left:0;width:100%;height:100%;z-index:9999"><img src="/StudentManagementWeb/resources/images/loading.gif" style="left:50%;top:40%;position:absolute;"/></div>');
	$("#blockDiv").css("cursor","wait");		
}
			
function unblockUI(){
	$("#blockDiv").css("cursor","normal");
	$("#blockDiv").remove();
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
  			var SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#treeOrgNodeId").val(SelectedOrgNodeId);
 		     $.blockUI({ message: null }); 
 		  	if(!gridloaded) {
 		  		gridloaded = true;
				populateGrid();
				
				}
			else
				gridReload();
		});
	   
}
	
	
	  function gridReload(){ 
           jQuery("#list2").jqGrid('setGridParam',{datatype:'json'});
           jQuery("#list2").jqGrid('setGridParam', {url:'getStudentForSelectedOrgNodeGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(),page:1}).trigger("reloadGrid");
      }


function  getIDList() {
      for(var i=0;i<gridjsondata.studentProfileInformation.length;i++) {
       	idarray[i] = gridjsondata.studentProfileInformation[i].studentId;
       }
}		
			
function populateGrid() {

         $("#list2").jqGrid({         
          url:'getStudentForSelectedOrgNodeGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(), 
		 type:   'POST',
	     datatype: "json",         
          colNames:['Student Name','Organization', 'Login ID','Grade', 'Student ID'],
		   	colModel:[
		   	
		   		{name:'displayName',index:'displayName', width:230, editable: true, align:"left",sorttype:'text'},
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr', width:150, editable: true, align:"left", sortable:false},
		   		{name:'userName',index:'userName',editable: true, width:200, align:"left", sortable:true},
		   		{name:'grade',index:'grade',editable: true, width:70, align:"left", sortable:true},
		   		{name:'studentNumber',index:'studentNumber',editable: true, width:150, align:"left", sortable:true}
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",records: function(obj) { return obj.studentProfileInformation.length; } },
		   	 
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'displayName', 
			viewrecords: true, 
			sortorder: "asc",
			height: "100%",  
			editurl: 'getStudentForSelectedOrgNodeGrid.do',
			caption:"Search Result",
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



	
	
	
	
	
	
	
	






			
		



