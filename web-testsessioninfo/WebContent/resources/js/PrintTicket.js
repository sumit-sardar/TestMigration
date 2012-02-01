var orgTktTreeHierarchy;
var selectedListId;
var selectedRowInListId;

function printTTicket(element){
	if (isButtonDisabled(element))
	return true;
	
	document.getElementById('printTicket').style.display = "block";
	populateTestTicketTree();
}
	
function openTestTicketIndividual( anchor, testAdminId, orgNodeId ) {
	var url = "/SessionWeb/testTicketOperation/individualTestTicket.do";
	return openTestTicket( "individual", anchor, url, testAdminId, orgNodeId );
}
//START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
function openTestTicketMultiple( anchor, testAdminId, orgNodeId ) {
	var url = "/SessionWeb/testTicketOperation/individualTestTicket.do";
	return openTestTicket( "multiple", anchor, url, testAdminId, orgNodeId );
}
//END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)

function openTestTicketSummary( anchor, testAdminId, orgNodeId ) {
	//alert("openTestTicketSummary orgNodeId:"+orgNodeId);
	var url = "/SessionWeb/testTicketOperation/summaryTestTicket.do";
	return openTestTicket( "summary", anchor, url, testAdminId, orgNodeId );
}

function openTestTicketSummaryInExcel( anchor, testAdminId, orgNodeId ) {
    var url = "/SessionWeb/testTicketOperation/summaryTestTicketInExcel.do";
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
	var postDataObject = {};
 	postDataObject.testAdminId = document.getElementById('selectedTestSessionId').value;
 	postDataObject.orgNodeId = document.getElementById('scheduleUserOrgNode').value;
 	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'userTreeOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		data:		postDataObject,
		success:	function(data, textStatus, XMLHttpRequest){	
						//alert('in');
						//$.unblockUI();  
						//leafNodeCategoryId = data.leafNodeCategoryId;
						if(data.isStudentExist == "false"){
							$("#noStudent").css('display', 'block');
							$("#studentExists").css('display', 'none');
							//fix for Session list tree being hidden if test ticket does not have any students 
							//$("#orgNodeHierarchy").css("visibility","hidden");	
							$("#printTestTicket").css("height",'330px');
						}else{
							orgTktTreeHierarchy = data;
							jsonData = orgTktTreeHierarchy.data;
							rootNode = [];
							getRootNodeDetails();
							createSingleNodeSelectedTktTree(orgTktTreeHierarchy);
							$("#noStudent").css('display', 'none');
							$("#studentExists").css('display', 'block');
							$("#orgNodeHierarchy").css("visibility","visible");	
							$("#printTestTicket").css("height",'430px');
						
						}
						$("#ticktSearchheader").css("visibility","visible");	
						$("#printTestTicket").dialog({
							title:$("#printTT").val(),  
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
						setTicketPopupPosition();		
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
				var toppos = ($(window).height() - 440) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#printTestTicket").parent().css("top",toppos);
				$("#printTestTicket").parent().css("left",leftpos);	
				
	}

function createSingleNodeSelectedTktTree(jsondata) {
	   $("#orgTktTreeDiv").jstree({
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
	   
	  $("#orgTktTreeDiv").delegate("a","click", function(e) {
	    	//$('#orgTktTreeDiv a.jstree-clicked').parent('li').attr('id')
	    	var SelectedTktOrgNodeId = $(this).parent().attr("id");
 		    $("#scheduleUserOrgNode").val(SelectedTktOrgNodeId); 		 	
		});
		
		$("#orgTktTreeDiv").bind("loaded.jstree", 
		 	function (event, data) {
		 		for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#orgTktTreeDiv ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
	  
	  	  registerDelegate("orgTktTreeDiv");
}

function testTicketPopupValues(rowId,listId){
	$("#selectedTestSessionId").val(rowId);
	$("#"+listId+" #"+rowId).addClass("ui-state-highlight");	
	var selectedRowData = $("#"+listId).getRowData(rowId);
	$("#adminTestName_val").text(selectedRowData.testAdminName);
	$("#testName_val").text(selectedRowData.testName);					
	$("#scheduleUserOrgNode").val(selectedRowData.creatorOrgNodeId); 				
	setAnchorButtonState('viewStatusButton', false);
	setAnchorButtonState('printTicketButton', false);
	selectedListId = listId;
	selectedRowInListId = rowId;
}

function closeTTPopup() {
	$("#orgTktTreeDiv").undelegate();
 	$("#orgTktTreeDiv").unbind();
 	var selectedRowData = $("#"+selectedListId).getRowData(selectedRowInListId);				
	$("#scheduleUserOrgNode").val(selectedRowData.creatorOrgNodeId); 
	closePopUp('printTestTicket');
}
