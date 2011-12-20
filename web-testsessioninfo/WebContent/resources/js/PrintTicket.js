function loadTicketOrgTree() {

	$.ajax({
		async:		true,		
		url:		'userTreeOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){							
						loadTicketOrgTreeData(data);
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){						
						window.location.href="/SessionWeb/logout.do";						
					}		
	});
} 

function loadTicketOrgTreeData(jsondata) {

	$("#orgTktTreeDiv").jstree({
	        "json_data" : {	             
	            "data" : jsondata.data,
				"progressive_render" : true,
				"progressive_unload" : false
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
}