var orgTktTreeHierarchy;

function printTTicket(element){
	if (isButtonDisabled(element))
	return true;
	
	document.getElementById('printTicket').style.display = "block";
	populateTestTicketTree();
}
	
function openTestTicketIndividual( anchor, testAdminId, orgNodeId ) {
	var url = "/TestWeb/testTicket/individualTestTicket.do";
	return openTestTicket( "individual", anchor, url, testAdminId, orgNodeId );
}
//START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
function openTestTicketMultiple( anchor, testAdminId, orgNodeId ) {
	var url = "/TestWeb/testTicket/individualTestTicket.do";
	return openTestTicket( "multiple", anchor, url, testAdminId, orgNodeId );
}
//END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)

function openTestTicketSummary( anchor, testAdminId, orgNodeId ) {
	//alert("openTestTicketSummary orgNodeId:"+orgNodeId);
	var url = "/TestWeb/testTicket/summaryTestTicket.do";
	return openTestTicket( "summary", anchor, url, testAdminId, orgNodeId );
}

function openTestTicket( ticketType, anchor, url, testAdminId, orgNodeId ) {
	anchor.href  = url;
	anchor.href += "?testAdminId=" + testAdminId;
	anchor.href += "&orgNodeId=" + orgNodeId;
	anchor.href += "&ticketType=" + ticketType;    //Added For CR ISTEP2011CR007 (Multiple Test Ticket)
	anchor.href += "&displayAccess=false";// + displayAccessCode;
	//    var targetWindowName = ticketType + orgNodeId;
	//    anchor.target = targetWindowName;
	return true;
}
	
function populateTestTicketTree() {
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'userTreeOrgNodeHierarchyList.do?testAdminId='+document.getElementById('selectedTestSessionId').value,
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						//alert('in');
						//$.unblockUI();  
						//leafNodeCategoryId = data.leafNodeCategoryId;
						if(data.isStudentExist == "false"){
							$("#noStudent").css('display', 'block');
							$("#studentExists").css('display', 'none');
							$("#orgNodeHierarchy").css("visibility","hidden");	
							$("#printTestTicket").css("height",'330px');
						}else{
							orgTktTreeHierarchy = data;
							createSingleNodeSelectedTktTree(orgTktTreeHierarchy);
							$("#noStudent").css('display', 'none');
							$("#studentExists").css('display', 'block');
							$("#orgNodeHierarchy").css("visibility","visible");	
							$("#printTestTicket").css("height",'430px');
						
						}
						$("#searchheader").css("visibility","visible");	
						$("#printTestTicket").dialog({
							title:"Print Test Ticket ",  
							resizable:false,
							autoOpen: true,
							width: '820px',
							modal: true,
							closeOnEscape: false,
							open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
						});	
						$('#printTestTicket').bind('keydown', function(event) {
				 			var code = (event.keyCode ? event.keyCode : event.which);
							if(code == 27){
								return false;
				 			}
						});
						//setTicketPopupPosition();		
						$.unblockUI();					
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						//$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					},
		complete :  function(){
						// $.unblockUI();  
					}
	});

}

function setTicketPopupPosition(){
				//$("#printTestTicket").css("height",'320px');
				var toppos = ($(window).height() - 510) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#printTestTicket").parent().css("top",toppos);
				$("#printTestTicket").parent().css("left",leftpos);	
				
	}

function createSingleNodeSelectedTktTree(jsondata) {
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
	   
	  $("#orgTktTreeDiv").delegate("a","click", function(e) {
	    	//$('#orgTktTreeDiv a.jstree-clicked').parent('li').attr('id')
	    	var SelectedTktOrgNodeId = $(this).parent().attr("id");
 		    $("#scheduleUserOrgNode").val(SelectedTktOrgNodeId); 		 	
		});
}
