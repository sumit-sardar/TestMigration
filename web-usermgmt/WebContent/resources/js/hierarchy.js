
var gridjsondata;
var idarray =[];
var gridloaded = false;

var orgTreeHierarchy;
var SelectedOrgNodeId ;
//var assignedOrgNodeIds ="108784";
var assignedOrgNodeIds="";

var roleOptions=[];
var timeZoneOptions=[];
var stateOptions=[];

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
         	"themes" : {
				"theme" : "apple",
				"dots" : false,
				"icons" : true
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
		//$('#innerID').jstree('open_node', $('#'+SelectedOrgNodeId).parent());   
     });  


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
    
    $("#innerID").bind("change_state.jstree", function (e, d) { 
    	var currentlySelectedNode ="";
    	assignedOrgNodeIds = "";
        if ((d.args[0].tagName == "A" || d.args[0].tagName == "INS")){
		$("#innerID").find(".jstree-checked").each(function(i, element){
				
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
			});
			
			if(currentlySelectedNode.length > 0 ) {
					$("#notSelectedOrgNodes").css("visibility","hidden");
					$("#selectedOrgNodesName").text(currentlySelectedNode);	
				} else {
					$("#notSelectedOrgNodes").css("visibility","visible");
					$("#selectedOrgNodesName").text("");	
				}
        	}
        });
}


function getText(element){
	var elementText  = element.childNodes[1].lastChild.data;
return elementText;
}

function  searchUser(){
	SelectedOrgNodeId= $("#orgNodeHierarchy a.jstree-clicked").parent().attr("id");
	$("#treeSelectedOrgNodeId").val(SelectedOrgNodeId);
	setElementValueAndSubmitWithAnchor('actionElement', 'actionElement', 'userSearchResult');
	
}

function populateTreeSelect() {
			$("#notSelectedOrgNodes").css("visibility","visible");
			$("#selectedOrgNodesName").text("");	
			createMultiNodeSelectedTree (orgTreeHierarchy);	

}



 
		
			
function populateGrid() {

		document.getElementById('changePW').style.display = "block";	
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
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "desc",
			height: 370,  
			editurl: 'userOrgNodeHierarchyGrid.do',
			caption:"User List",
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
			 
}

function changePwdForUser(){

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
	
	function AddUserDetail(){
		
	/*$("#preButton").css("visibility","hidden");	
	$("#nextButton").css("visibility","hidden");*/	
	document.getElementById('displayMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";	
	reset();
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						
						UIBlock();
					},
		url:		'getOptionList.do?isLasLinkCustomer='+$("#isLasLinkCustomer").val(), 
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						//alert('in');
						$.unblockUI();
						//UIBlock();
						//overlayblockUI(); 
						roleOptions = data.roleOptions;
						timeZoneOptions=data.timeZoneOptions;
						stateOptions=data.stateOptions;
						fillDropDown("roleOptions", roleOptions);
						fillDropDown("timeZoneOptions", timeZoneOptions);
						fillDropDown("stateOptions", stateOptions);
						
						//customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
						$("#addEditUserDetail").dialog({  
								title:"Add User",  
							 	resizable:false,
							 	autoOpen: true,
							 	width: '800px',
							 	modal: true,
							 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
							 	});	
						$('#addEditUserDetail').bind('keydown', function(event) {
			 				  var code = (event.keyCode ? event.keyCode : event.which);
  							  if(code == 27){
			  				  onCancel();
			  				  return false;
			 				 }
			 				
							});
						 	 
							setPopupPosition();	
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
		
	});
	
							 	
						 	
	}
	
	
	function setPopupPosition(){
				var toppos = ($(window).height() - 610) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#addEditUserDetail").parent().css("top",toppos);
				$("#addEditUserDetail").parent().css("left",leftpos);		 	 
				$("#User_Information").css("height",'300px');
				$("#User_Information").css("overflow",'auto');
				$("#Contact_Information").css("height",'300px');
				$("#Contact_Information").css("overflow",'auto');
				
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
	
	
	
	
	function userDetailSubmit(){
	var validflag = VerifyUserDetail(assignedOrgNodeIds);
	//var validflag = true;
	if(validflag){
		isEmailProvided();
	}else {
		document.getElementById('displayMessage').style.display = "block";
	}
	}
	
	
	function saveUserDetail(){
				$.ajax(
						{
								async:		true,
								beforeSend:	function(){
											
												
												UIBlock();
												//alert('before send....');
											},
								url:		'saveAddEditUser.do',
								type:		'POST',
								data:		$("#addEditUserDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds ,
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
												$.unblockUI();  
												var errorFlag = data.errorFlag;
												var successFlag = data.successFlag;
												if(successFlag) {
													closePopUp('addEditUserDetail');
													setMessageMain(data.title, data.content, data.type, "");
													document.getElementById('displayMessageMain').style.display = "block";	
													assignedOrgNodeIds = "";
        										}
        										else{
        											setMessage(data.title, data.content, data.type, "");
        											document.getElementById('displayMessage').style.display = "block";	
        											
        										}
																								
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
	
	
	 function reset() {
		$("#userFirstName").val("");	
		$("#userLastName").val("");	
		$("#userMiddleName").val("");		
		$("#userEmail").val("");
		$("#userExternalId").val("");		
		$("#addressLine1").val("");		
		$("#addressLine2").val("");		
		$("#city").val("");
		$("#state").val("");
		$("#zipCode1").val("");
		$("#zipCode2").val("");
		$("#primaryPhone1").val("");
		$("#primaryPhone2").val("");
		$("#primaryPhone3").val("");
		$("#primaryPhone4").val("");
		$("#secondaryPhone1").val("");
		$("#secondaryPhone2").val("");
		$("#secondaryPhone3").val("");
		$("#secondaryPhone4").val("");
		$("#faxNumber1").val("");	
		$("#faxNumber2").val("");	
		$("#faxNumber3").val("");
		
		var allSelects = $("#addEditUserDetail select");
		for(var count = 0; count < allSelects.length ; count++) {
			$(allSelects[count]).find("option:eq(0)").attr("selected","true");
		}	
	 }
	
	
	function onCancel() {
		
		isValueChanged = false;
		if($("#userFirstName").val()!= "" 
			|| $("#userLastName").val()!= ""
			|| $("#userMiddleName").val()!= ""
			|| $("#userEmail").val()!= ""
			|| $("#userExternalId").val()!= ""
			|| $("#addressLine1").val() != ""
			|| $("#addressLine2").val() != ""
			|| $("#city").val()!= ""
			|| $("#stateOptions").val()!= "-1"
			|| $("#zipCode1").val()!= ""
			|| $("#zipCode2").val()!= ""
			|| $("#primaryPhone1").val() != ""
			|| $("#primaryPhone2").val() != ""
			|| $("#primaryPhone3").val()!= ""
			|| $("#primaryPhone4").val()!= ""
			|| $("#secondaryPhone1").val()!= ""
			|| $("#secondaryPhone2").val()!= ""
			|| $("#secondaryPhone3").val() != ""
			|| $("#secondaryPhone4").val() != ""
			|| $("#faxNumber1").val() != "" 
			|| $("#faxNumber2").val()!= ""
			|| $("#faxNumber3").val()!= "") {
			isValueChanged = true;	
			}
		
		if(!isValueChanged) {
			var allSelects = $("#addEditUserDetail select");
			for(var count = 0; count < allSelects.length ; count++) {
				var selectedInd = $(allSelects[count]).attr("selectedIndex");
				if(selectedInd != 0 && ($(allSelects[count]).attr("disabled") == null  || $(allSelects[count]).attr("disabled") == false)) {
					isValueChanged = true;
					break;
				}
			}
		}
		
		if(!isValueChanged){
			if(assignedOrgNodeIds != ""){
				isValueChanged = true;
			}
		}
		
		if(isValueChanged) {
		$("#confirmationPopup").dialog({  
			title:"Confirmation Alert",  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
			 $("#confirmationPopup").css('height',100);
			  var toppos = ($(window).height() - 290) /2 + 'px';
			 var leftpos = ($(window).width() - 410) /2 + 'px';
			 $("#confirmationPopup").parent().css("top",toppos);
			 $("#confirmationPopup").parent().css("left",leftpos);	
			  
		} else {
			closePopUp('addEditUserDetail');
		}
	}
	
	
	function closePopUp(dailogId){
		if(dailogId == 'addEditUserDetail') {
			$('#accordion').accordion('activate', 0 );
			$("#User_Information").scrollTop(0);
			$("#Contact_Information").scrollTop(0);
			$('#Contact_Information').hide();
			populateTreeSelect();
			
		}
		$("#"+dailogId).dialog("close");
		
		if(dailogId == 'confirmationPopup') {
			$('#accordion').accordion('activate', 0 );
			$("#User_Information").scrollTop(0);
			$("#Contact_Information").scrollTop(0);
			$('#Contact_Information').hide();
			$("#userFirstName").trigger("focus");
		} 
	}
	
	function closeConfirmationPopup() {
		closePopUp('confirmationPopup');
		closePopUp('addEditUserDetail');
	}
	
	function closeEmailWarningPopup() {
		closePopUp('EmailWarning');
		saveUserDetail();
		
	}
	
	function isEmailProvided(){
		
		if($("#userEmail").val()== "") {
			$("#EmailWarning").dialog({  
			title:"Email Alert",  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
			 $("#EmailWarning").css('height',150);
			 var toppos = ($(window).height() - 290) /2 + 'px';
			 var leftpos = ($(window).width() - 410) /2 + 'px';
			 $("#EmailWarning").parent().css("top",toppos);
			 $("#EmailWarning").parent().css("left",leftpos);	
		
		}
		else
			saveUserDetail();
	}
	
function fillDropDown( elementId, optionList) {
	var ddl = document.getElementById(elementId);
	var optionHtml = "" ;
	for(var i = 0; i < optionList.length; i++ ) {
	     //alert( optionList[i]);
	     //alert( optionList[i].split(":"));
	     var val = optionList[i].split("|");
	     
		optionHtml += "<option  value='"+ val[0]+"'>"+ val[1]+"</option>";	
	}
	$(ddl).html(optionHtml);
}






			
		



