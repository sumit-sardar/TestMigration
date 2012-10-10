var orgTktTreeHierarchy;
var selectedListId;
var selectedRowInListId;

var currentNodeIdPrint ;
var currentCategoryLevelPrint;
var currentTreeArrayPrint;
var currentIndexPrint;
var dataObj2Print=[];
var dataObj3Print=[];
var dataObj4Print;
var dataObj5Print;
var dataObj6Print;
var dataObj7Print;
var dataObj8Print;
var rootMapPrint = {};
var jsonDataPrint;
var mapPrint = new Map();
var rootNodePrint = [];
var typePrint;
var asyncOverPrint = 0;
var leafParentOrgNodeIdPrint = "";
var displayAccessCodeOnTicket = false;	//Changes for access code display in test ticket

function printTTicket(element){
	if (isButtonDisabled(element))
	return true;
	isPrintTicket = true;
	document.getElementById('printTicket').style.display = "block";
	populateTestTicketTree();
}
	
function openTestTicketIndividual( anchor, testAdminId, orgNodeId ) {
	if(orgNodeId.indexOf("_") != -1)
		orgNodeId = orgNodeId.substring(0, orgNodeId.indexOf("_"));
	var url = "/SessionWeb/testTicketOperation/individualTestTicket.do";
	return openTestTicket( "individual", anchor, url, testAdminId, orgNodeId );
}
//START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
function openTestTicketMultiple( anchor, testAdminId, orgNodeId ) {
	if(orgNodeId.indexOf("_") != -1)
		orgNodeId = orgNodeId.substring(0, orgNodeId.indexOf("_"));
	var url = "/SessionWeb/testTicketOperation/individualTestTicket.do";
	return openTestTicket( "multiple", anchor, url, testAdminId, orgNodeId );
}
//END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)

function openTestTicketSummary( anchor, testAdminId, orgNodeId ) {
	if(orgNodeId.indexOf("_") != -1)
		orgNodeId = orgNodeId.substring(0, orgNodeId.indexOf("_"));
	//alert("openTestTicketSummary orgNodeId:"+orgNodeId);
	var url = "/SessionWeb/testTicketOperation/summaryTestTicket.do";
	return openTestTicket( "summary", anchor, url, testAdminId, orgNodeId );
}

function openTestTicketSummaryInExcel( anchor, testAdminId, orgNodeId ) {
	if(orgNodeId.indexOf("_") != -1)
		orgNodeId = orgNodeId.substring(0, orgNodeId.indexOf("_"));
    var url = "/SessionWeb/testTicketOperation/summaryTestTicketInExcel.do";
    return openTestTicket( "summary", anchor, url, testAdminId, orgNodeId );

}


function openTestTicket( ticketType, anchor, url, testAdminId, orgNodeId ) {
	url += "?testAdminId=" + testAdminId;
	url += "&orgNodeId=" + orgNodeId;
	url += "&ticketType=" + ticketType;    //Added For CR ISTEP2011CR007 (Multiple Test Ticket)
	url += "&displayAccess=" + displayAccessCodeOnTicket;
	anchor.href = url;
	return anchor;
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
						initDisplayAccessCodeOnTicket();
						
						if(data.isStudentExist == "false"){
							$("#noStudent").css('display', 'block');
							$("#studentExists").css('display', 'none');
							//fix for Session list tree being hidden if test ticket does not have any students 
							//$("#orgNodeHierarchy").css("visibility","hidden");	
							$("#printTestTicket").css("height",'330px');
						}else{
							orgTktTreeHierarchy = data;
							jsonDataPrint = orgTktTreeHierarchy.data;
							//rootNode = [];
							getRootNodeDetailsPrint();
							createSingleNodeSelectedTktTree(orgTktTreeHierarchy);
							$("#noStudent").css('display', 'none');
							$("#studentExists").css('display', 'block');
							$("#orgNodeHierarchy").css("visibility","visible");	
							$("#printTestTicket").css("height",'430px');
							displayAccessCode = data.showAccessCode;
							if(displayAccessCode != undefined && (displayAccessCode || displayAccessCode == 'true')) {
								$("#displayAccessCodes").show();
							} else {
								$("#displayAccessCodes").hide();
							}
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
	            "data" : rootNodePrint,
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
	  
	   $("#orgTktTreeDiv").delegate("li ins","click", function(e) {
		var x = this.parentNode;	
		typePrint = "orgTktTreeDiv";	
		var categoryId  = x.getAttribute("tcl");
			if(categoryId != null || categoryId != undefined){
			currentCategoryLevelPrint = categoryId ; 
			}
			else{
			currentCategoryLevelPrint ="1";
			}
			currentNodeIdPrint = x.id;			
			var classState = $(x).hasClass("jstree-open");			
			var rootCategoryLevel = rootMapPrint[currentNodeIdPrint];
	
		if (classState == false){
			if (currentCategoryLevelPrint == 1) {	
			dataObj2Print = [];	
			var indexOfRoot = getIndexOfRootPrint(currentNodeIdPrint);
			populateTreeImmediatePrint(currentNodeIdPrint,currentCategoryLevelPrint,indexOfRoot);
			}
	
			var cacheData = mapPrint.get(currentNodeIdPrint);
			if (cacheData != null){
				currentTreeArrayPrint = cacheData;			
			}
			if (cacheData == null){
				switch(currentCategoryLevelPrint){
					
					//Not caching at initial level because the whole data will be put in cache which may increase the cache size
					//considerably
					
					case "2": 	dataObj3Print =getObjectPrint(jsonDataPrint,currentNodeIdPrint,currentCategoryLevelPrint,x.parentNode.parentNode.id);
								currentIndexPrint = dataObj3Print.index;
								currentTreeArrayPrint = dataObj3Print.jsonData;
								mapPrint.put(currentNodeIdPrint,currentTreeArrayPrint);
							break;
							
					case "3": 	dataObj4Print = mapPrint.get(x.parentNode.parentNode.id);
								currentTreeArrayPrint =getObjectPrint(dataObj4Print,currentNodeIdPrint,currentCategoryLevelPrint);
								currentIndexPrint = currentTreeArrayPrint.index;
								currentTreeArrayPrint = currentTreeArrayPrint.jsonData;
								mapPrint.put(currentNodeIdPrint,currentTreeArrayPrint);
							break;
					case "4": 	dataObj5Print = mapPrint.get(x.parentNode.parentNode.id);
								currentTreeArrayPrint =getObjectPrint(dataObj5Print,currentNodeIdPrint,currentCategoryLevelPrint);
								currentIndexPrint = currentTreeArrayPrint.index;
								currentTreeArrayPrint = currentTreeArrayPrint.jsonData;
								mapPrint.put(currentNodeIdPrint,currentTreeArrayPrint);
							break;
					case "5": 	dataObj6Print = mapPrint.get(x.parentNode.parentNode.id);
								currentTreeArrayPrint =getObjectPrint(dataObj6Print,currentNodeIdPrint,currentCategoryLevelPrint);
								currentIndexPrint = currentTreeArrayPrint.index;
								currentTreeArrayPrint = currentTreeArrayPrint.jsonData;
								mapPrint.put(currentNodeIdPrint,currentTreeArrayPrint);
							break;
					case "6": 	dataObj7Print = mapPrint.get(x.parentNode.parentNode.id);
								currentTreeArrayPrint =getObjectPrint(dataObj7Print,currentNodeIdPrint,currentCategoryLevelPrint);
								currentIndexPrint = currentTreeArrayPrint.index;
								currentTreeArrayPrint = currentTreeArrayPrint.jsonData;
								mapPrint.put(currentNodeIdPrint,currentTreeArrayPrint);
							break;
					case "7": 	dataObj8Print =mapPrint.get(x.parentNode.parentNode.id);
								currentTreeArrayPrint =getObjectPrint(dataObj8Print,currentNodeIdPrint,currentCategoryLevelPrint);
								currentIndexPrint = currentTreeArrayPrint.index;
								currentTreeArrayPrint = currentTreeArrayPrint.jsonData;
								mapPrint.put(currentNodeIdPrint,currentTreeArrayPrint);
								
							break;	
					case "8": 	dataObj9Print =mapPrint.get(x.parentNode.parentNode.id);
								currentTreeArrayPrint =getObjectPrint(dataObj9,currentNodeIdPrint,currentCategoryLevelPrint);
								currentIndexPrint = currentTreeArrayPrint.index;
								currentTreeArrayPrint = currentTreeArrayPrint.jsonData;
								mapPrint.put(currentNodeIdPrint,currentTreeArrayPrint);
							break;						
					
				}
			}

		}
			
	});
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
		isPrintTicket = false;
		resetTestTicket();
	}


	function getRootNodeDetailsPrint(){
		var noOfRoots = jsonDataPrint.length;
			for (var i = 0,j = noOfRoots; i < j; i++ ){
				rootMapPrint[jsonDataPrint[i].attr.id] = jsonDataPrint[i].attr.cid;
				rootNodePrint.push({data: jsonDataPrint[i].data,attr:{id: jsonDataPrint[i].attr.id,cid:jsonDataPrint[i].attr.cid,tcl:jsonDataPrint[i].attr.tcl},children : [{}]});
			}
	}
	
	function getIndexOfRootPrint(currentNodeIdPrint){
		var numberOfRoots = jsonDataPrint.length;
	
		for (var i = 0,j = numberOfRoots; i < j; i++ ){
			if (jsonDataPrint[i].attr.id == currentNodeIdPrint){				
				return i;
			}
		}	
	}
	
		function populateTreeImmediatePrint(currentNodeId,currentCategoryLevel,indexOfRoot){	
	//TODO : Updation in root node is a problem need to work on that but this method is also necessary for populating the immediate //tree because if we use the cache all the 2nd level objects will be stored in cache which makes the cache very heavy
		var jsonObject = jsonDataPrint;
		jsonObject = jsonObject[indexOfRoot];
		if (dataObj2Print.length == 0 && jsonObject != undefined){
			for (var i = 0, j = jsonObject.children.length; i < j; i++ ){
					dataObj2Print.push({data: jsonObject.children[i].data,attr:{id: jsonObject.children[i].attr.id,cid:jsonObject.children[i].attr.cid, tcl:jsonObject.children[i].attr.tcl,chlen:jsonObject.children[i].hasOwnProperty("children")},children : [{}]});	
			}
		}
	}
  
  	/************Tree Utils*****************/
	function getObjectPrint(jsonObject,currentNodeId,currentCategoryLevel, parentNodeId){
			var obj = new Object();
			var indexRoot = getIndexOfRootPrint(parentNodeId);
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
		
		function resetTestTicket(){
		 currentNodeIdPrint ="";
		 currentCategoryLevelPrint="";
		 currentTreeArrayPrint={};
		 currentIndexPrint;
		 dataObj2Print=[];
		 dataObj3Print=[];
		 dataObj4Print=[];
		 dataObj5Print=[];
		 dataObj6Print=[];
		 dataObj7Print=[];
		 dataObj8Print=[];
		 rootMapPrint = {};
		 jsonDataPrint;
		 mapPrint = new Map();
		 rootNodePrint = [];
		 typePrint = "";
		 asyncOverPrint = 0;
		 leafParentOrgNodeIdPrint = "";		
		}
	
function accessCode() {
	
	var checkAccess = document.getElementsByName("individualAccess");
	displayAccessCodeOnTicket = checkAccess[0].checked;
}

function initDisplayAccessCodeOnTicket() {
	displayAccessCodeOnTicket = false;
	var checkAccess = document.getElementsByName("individualAccess");
	checkAccess[1].checked = true;		
}
														
