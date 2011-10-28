
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
var isPopUp = false;

var userList;//added on 24.10.2011
var isAddUser = true;////added on 24.10.2011
var dbUserDetails;
var requetForUser="";
var organizationNodes = [];
var isValueChanged = false;

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
	$.blockUI({ message: '<img src="/UserManagementWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
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
			// $("#changePWDBtn").attr('disabled', true);
			setAnchorButtonState('changePWButton', true);
			 	 
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

function  searchUser(){
	SelectedOrgNodeId= $("#orgNodeHierarchy a.jstree-clicked").parent().attr("id");
	$("#treeSelectedOrgNodeId").val(SelectedOrgNodeId);
	setElementValueAndSubmitWithAnchor('actionElement', 'actionElement', 'userSearchResult');
	
}

function populateTreeSelect() {
			$("#notSelectedOrgNodes").css("display","inline");
			$("#selectedOrgNodesName").text("");	
			$("#innerID").undelegate();
			createMultiNodeSelectedTree (orgTreeHierarchy);	
}



 
		
			
function populateGrid() {

		document.getElementById('changePW').style.display = "block";
		// $("#changePWDBtn").attr('disabled', true); 
		setAnchorButtonState('changePWButton', true);
			
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
		   	jsonReader: { repeatitems : false, root:"userProfileInformation", id:"userId",records: function(obj) { userList = JSON.stringify(obj.userProfileInformation);return obj.userProfileInformation.length; } },
		   	 
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			width: 975, 
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
			onSelectRow: function () {
				//alert($("#roleNameID").val());
				if ($("#roleNameID").val() == 'Administrator') {
				//alert('Administrator....');
					// $("#changePWDBtn").removeAttr('disabled');
					setAnchorButtonState('changePWButton', false);
				}
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
				 
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
	 });
			jQuery("#list2").jqGrid('navGrid','#pager2',{
				addfunc: function() {
					requetForUser = "";
		    		AddUserDetail();
		    	},
		    	editfunc: function() {
		    		 requetForUser = "";
		    		 EditUserDetail();
		    	}
			});  
			 
}

function resetPassword () {
	
	resetPwdMsg();
	hidePwdMsgDiv();
	resetPwdTxtBox();

}

function resetPwdMsg () {

	$("#titlePWD").text("");
	$("#contentPWD").html("");
	$("#messagePWD").text("");

}

function hidePwdMsgDiv () {

	document.getElementById('displayMessageChangePassword').style.display = "none";
}

function resetPwdTxtBox () {

	$("#newPassword").val("");
	$("#confirmPassword").val("");

}

function getUserName () {

	var selId = $("#list2").jqGrid('getGridParam', 'selrow');
	
	var fName = getColValueJson (selId,'firstName');
	var lName = getColValueJson (selId,'lastName');
	
	return fName+' '+lName;
}

function changePwdForUser(element){

	if (isButtonDisabled(element))
		return true;
	
	resetPassword();
	
	document.getElementById('changePW').style.display = "block";
	
			$("#changeUserPassword").dialog({  
					title:"Change Password: "+getUserName(),  
				 	resizable:false,
				 	autoOpen: true,
				 	width: '800px',
				 	modal: true,
					closeOnEscape: false,
				 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
				 	});	
			$('#changeUserPassword').bind('keydown', function(event) {
 				  var code = (event.keyCode ? event.keyCode : event.which);
				  if(code == 27){
	  				  onChangePasswordCancel();
	  				  return false;
 				  }
				});
			 	 
			setPopupPosition();	
}

function gridReload(){ 
           jQuery("#list2").jqGrid('setGridParam',{datatype:'json'});
           var sortArrow = jQuery("#list2");
           jQuery("#list2").jqGrid('setGridParam', {url:'userOrgNodeHierarchyGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(),page:1}).trigger("reloadGrid");
      	   jQuery("#list2").sortGrid('lastName',true);  
      	   //For MQC Defect - 67122
           var arrowElements = sortArrow[0].grid.headers[0].el.lastChild.lastChild;
           $(arrowElements.childNodes[0]).removeClass('ui-state-disabled');
           $(arrowElements.childNodes[1]).addClass('ui-state-disabled'); 
      }


	
function userDetailEdit(){
	isPopUp	= true;
	
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
								closeOnEscape: false,
							 	width: '600px'
							 	});	
	$("#preButton").css("visibility","visible");	
	$("#nextButton").css("visibility","visible");	
	$('#accordion ul:eq(0)').show();
	
	}

	 /*Added on 24.10.2011
	  * for editUserDetails functionality
	  */
	function EditUserDetail(selectedUserId){
	isPopUp	= true;
	isAddUser = false;//added on 25.10.2011
	var rowid;
	document.getElementById('displayMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";
	//var selectedUserId = $("#list2").jqGrid('getGridParam', 'selrow');
	if(selectedUserId == undefined ){
		rowid = $("#list2").jqGrid('getGridParam', 'selrow');
	} else {
		rowid = selectedUserId;
	}
	var userName =  getColValueJson(rowid,'userName');
	
		$.ajax({
		async:		true,
		beforeSend:	function(){
						
						UIBlock();
					},
		url:		'getUserDetailsForEdit.do?isLasLinkCustomer='+$("#isLasLinkCustomer").val()+'&selectedUserName='+userName, 
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						//alert('got json data ::'+data);
						$.unblockUI();
						//UIBlock();
						//overlayblockUI(); 
						organizationNodes = data.organizationNodes;
						roleOptions = data.optionList.roleOptions;
						//alert(roleOptions);
						timeZoneOptions=data.optionList.timeZoneOptions;
						stateOptions=data.optionList.stateOptions;
						//alert('check1');
						fillDropDown("roleOptions", roleOptions);
						fillDropDown("timeZoneOptions", timeZoneOptions);
						fillDropDown("stateOptions", stateOptions);
						//alert(data.userName);
						$("#userFirstName").val(data.firstName);
						//alert(data.firstName);
						$("#userMiddleName").val(data.middleName);
						$("#userLastName").val(data.lastName);
						$("#timeZoneOptions").val(data.timeZone);
						//alert(data.timeZone);
						//$("#roleOptions").val(data.role);
						$("#roleOptions").val(data.roleId);
						//alert(data.roleId);
						//alert(data.role);
						//$("").val(data.);
						$("#addressLine1").val(data.userContact.addressLine1);
						$("#addressLine2").val(data.userContact.addressLine2);
						$("#city").val(data.userContact.city);
						$("#stateOptions").val(data.userContact.state);
						$("#zipCode1").val(data.userContact.zipCode1);
						$("#zipCode2").val(data.userContact.zipCode2);
						$("#primaryPhone1").val(data.userContact.primaryPhone1);
						$("#primaryPhone2").val(data.userContact.primaryPhone2);
						$("#primaryPhone3").val(data.userContact.primaryPhone3);
						$("#primaryPhone4").val(data.userContact.primaryPhone4);
						$("#secondaryPhone1").val(data.userContact.secondaryPhone1);
						$("#secondaryPhone2").val(data.userContact.secondaryPhone2);
						$("#secondaryPhone3").val(data.userContact.secondaryPhone3);
						$("#secondaryPhone4").val(data.userContact.secondaryPhone4);
						$("#faxNumber1").val(data.userContact.faxNumber1);
						$("#faxNumber2").val(data.userContact.faxNumber2);
						$("#faxNumber3").val(data.userContact.faxNumber3);
						
						fillselectedOrgNode("selectedOrgNodesName", data.organizationNodes);
						//alert('check3');
						//customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
		
		
			// for enable next and prev			
			var pageDataIds = $("#list2").jqGrid('getDataIDs'); 
			var str = pageDataIds;
			//var indexOfId = str.indexOf(selectedUserId);
			var indexOfId = -1;
			if(!Array.indexOf) {
				indexOfId = findIndexFromArray (str , rowid);
			} else {
				indexOfId = str.indexOf(rowid);
			}
			disablenextprev (indexOfId,(str.length) - 1);
			// for enable next and prev	
			
						$("#addEditUserDetail").dialog({  
								title:"Edit User",  
							 	resizable:false,
							 	autoOpen: true,
							 	width: '800px',
							 	modal: true,
								closeOnEscape: false,
							 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
							 	});	
						$('#addEditUserDetail').bind('keydown', function(event) {
			 				  var code = (event.keyCode ? event.keyCode : event.which);
  							  if(code == 27){
			  				  onCancel();
			  				  return false;
			 				 }
			 				
							});
						 	 
							setPopupPosition(isAddUser);
							checkOpenNode(data.organizationNodes);
							dbUserDetails = $("#addEditUserDetail *").serializeArray(); 	
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
		
	});
	
	}//end of EditUserDetail()
	
	/*function getUserNameJson(id){
	var str = userList;
	//alert(str);
	var userName = "";
	var indexOfId = str.indexOf(id);
	//alert(indexOfId);
	var indexOfCreatedBy = -1;
	var indexOfComma = -1;
	if(indexOfId > 0){
		str = str.substring(parseInt(indexOfId), str.length);
		//userName
		indexOfCreatedBy = str.indexOf("userName");
		indexOfComma = str.indexOf(',', parseInt(indexOfCreatedBy));
		indexOfCreatedBy += 10;
		userName = str.substring(parseInt(indexOfCreatedBy), parseInt(indexOfComma));
		userName = userName.substring(1,userName.length-1);
		userName = trim(userName);
	}else{
				
		}
	return userName;
	}*/
	
function fillselectedOrgNode( elementId, orgList) {
	var ddl = document.getElementById(elementId);
	optionHtml = "";
	assignedOrgNodeIds = "";
	var customerId = $("#customerID").val();
	for(var i = 0; i < orgList.length; i++) {
	
		if(assignedOrgNodeIds == "") {
			assignedOrgNodeIds = orgList[i].orgNodeId + "|" + customerId;
		} else {
			assignedOrgNodeIds = assignedOrgNodeIds + "," + orgList[i].orgNodeId + "|" + customerId; 
		}
		if(optionHtml == "") {
			optionHtml += "<a style='color: blue;text-decoration:underline'  href=javascript:openTreeNodes('"+orgList[i].orgNodeId+"');>"+trim(orgList[i].orgNodeName)+"</a>";	
		} else {
			optionHtml = optionHtml +" , " + "<a style='color: blue;text-decoration:underline'  href=javascript:openTreeNodes('"+orgList[i].orgNodeId+"');>"+ trim(orgList[i].orgNodeName)+"</a>";	
		}
		
	}
	$(ddl).html(optionHtml);
	
	if(orgList.length > 0 ) {
		$("#notSelectedOrgNodes").css("display","none");
	} else {
		$("#notSelectedOrgNodes").css("display","inline");
		$("#selectedOrgNodesName").text("");	
	}
}
	function checkOpenNode(organizationNodes){
	for(var i=0; i<organizationNodes.length; i++){
		var assignedOrg = organizationNodes[i].orgNodeId;
		var isIdExist = $('#'+assignedOrg,'#innerID').length;
			if(isIdExist > 0){
				$('#innerID').jstree('check_node', "#"+assignedOrg); 
			}
		}
	}
	//upto this on 24.10.2011
	function AddUserDetail(){
	isPopUp	= true;
	isAddUser = true;//added on 25.10.2011
	/*$("#preButton").css("visibility","hidden");	
	$("#nextButton").css("visibility","hidden");*/	
	document.getElementById('displayMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";	
	
	if(!(roleOptions.length > 0 
		&& timeZoneOptions.length > 0
			&& stateOptions.length > 0)){
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
								closeOnEscape: false,
							 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
							 	});	
						$('#addEditUserDetail').bind('keydown', function(event) {
			 				  var code = (event.keyCode ? event.keyCode : event.which);
  							  if(code == 27){
			  				  onCancel();
			  				  return false;
			 				 }
			 				
							});
						 	 
							setPopupPosition(isAddUser);	
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
		
	});
	
	} else {
		reset();
		$("#addEditUserDetail").dialog({  
			title:"Add User",  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '800px',
		 	modal: true,
		 	closeOnEscape: false,
		 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		 	});	
		 	$('#addEditUserDetail').bind('keydown', function(event) {
		 						  //alert("key up 2");
 							var code = (event.keyCode ? event.keyCode : event.which);
 							if(code == 27){
		  				  	onCancel();
		  				 	return false;
		 				 }
		 				
						});
		setPopupPosition(isAddUser);	
	}	
							 	
						 	
	}
	
	
	function setPopupPosition(isAddUser){
				var toppos = ($(window).height() - 610) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#addEditUserDetail").parent().css("top",toppos);
				$("#addEditUserDetail").parent().css("left",leftpos);		 	 
				$("#User_Information").css("height",'300px');
				$("#User_Information").css("overflow",'auto');
				$("#Contact_Information").css("height",'300px');
				$("#Contact_Information").css("overflow",'auto');
				if(isAddUser) {
					$("#preButton").css("visibility","hidden");	
					$("#nextButton").css("visibility","hidden");
					//$("#viewPreButton").css("visibility","hidden");	
					//$("#viewNextButton").css("visibility","hidden");
				} else {
					$("#preButton").css("visibility","visible");	
					$("#nextButton").css("visibility","visible");
					//$("#viewPreButton").css("visibility","visible");	
					//$("#viewNextButton").css("visibility","visible");
					
				}
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
	var param;//added on 25.10.2011
	var rowid = $("#list2").jqGrid('getGridParam', 'selrow');
	if(isAddUser){
		param = $("#addEditUserDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds;
	}
	else{
		var userName =  getColValueJson(rowid,'userName');
		var addressId = getColValueJson(rowid,'addressId');
		param = $("#addEditUserDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds+"&selectedUserName="+userName+"&userId="+rowid+"&addressId"+addressId;
	}

				$.ajax(
						{
								async:		true,
								beforeSend:	function(){
											
												
												UIBlock();
												//alert('before send....');
											},
								url:		'saveAddEditUser.do',
								type:		'POST',
								data:		param,
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
		
		assignedOrgNodeIds = "";
	 }
	
	
	function onCancel() {
		
		isValueChanged = false;
		if(isAddUser){
			if($("#userFirstName").val()!= "" 
			|| $("#userLastName").val()!= ""
			|| $("#userMiddleName").val()!= ""
			|| $("#userEmail").val()!= ""
			|| $("#userExternalId").val()!= ""
			|| $("#addressLine1").val() != ""
			|| $("#addressLine2").val() != ""
			|| $("#city").val()!= ""
			|| $("#stateOptions").val()!= ""
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
				openConfirmationPopup();	 
			} else {
				closePopUp('addEditUserDetail');
			}
		}
		else {
		  	isValueChanged = isEditUserDataChanged();
	      	if(isValueChanged) {
				openConfirmationPopup();	
			} else {
				closePopUp('addEditUserDetail');
			}
		}
		
	}
	
	
	function closePopUp(dailogId){
		if(dailogId == 'addEditUserDetail') {
			$('#accordion').accordion('activate', 0 );
			$("#User_Information").scrollTop(0);
			$("#Contact_Information").scrollTop(0);
			$('#Contact_Information').hide();
			populateTreeSelect();
			isPopUp = false;
			
		}
		$("#"+dailogId).dialog("close");
		
		if(dailogId == 'confirmationPopup') {
			$('#accordion').accordion('activate', 0 );
			$("#User_Information").scrollTop(0);
			$("#Contact_Information").scrollTop(0);
			$('#Contact_Information').hide();
			$("#userFirstName").trigger("focus");
		} 
		if(dailogId == 'confirmationPopupNavigation') {
		//	$('#accordion').accordion('activate', 0 );
		//	$("#Student_Information").scrollTop(0);
		//	$("#Student_Additional_Information").scrollTop(0);
		//	$("#Student_Accommodation_Information").scrollTop(0);
		//	$('#Student_Additional_Information').hide();
		//	$('#Student_Accommodation_Information').hide();
		//	$('#studentFirstName').trigger("focus");
			requetForUser = "";				
		}
	}
	
	/*function closeConfirmationPopup() {
		closePopUp('confirmationPopup');
		closePopUp('addEditUserDetail');
	}*/
	
	function closeConfirmationPopup() {
		if(isAddUser){
			closePopUp('confirmationPopup');
			closePopUp('confirmationPopupNavigation');
			closePopUp('addEditUserDetail');
		} else {
			
			var navFlow = requetForUser;
			closePopUp('confirmationPopup');
			closePopUp('confirmationPopupNavigation');
			if(navFlow == "Next"){
				 fetchNextData('Edit');
			} else if(navFlow == "Previous"){
				fetchPreviousData('Edit');
			} else{
				closePopUp('addEditUserDetail');
			}
			
		}
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
	//alert(optionList);
	var optionHtml = "" ;
	for(var i = 0; i < optionList.length; i++ ) {
	     //alert( optionList[i]);
	     //alert( optionList[i].split(":"));
	     var val = optionList[i].split("|");
	     
		optionHtml += "<option  value='"+ val[0]+"'>"+ val[1]+"</option>";	
	}
	//alert(optionHtml);
	$(ddl).html(optionHtml);
}

function saveChangePassword() {
	var userId = $("#list2").jqGrid('getGridParam', 'selrow');
	var userName = getColValueJson(userId,'userName');
	
				$.ajax(
						{
								async:		true,
								beforeSend:	function(){
												UIBlock();
											},
								url:		'saveUserPassword.do',
								type:		'POST',
								data:		$("#changeUserPassword *").serialize()+ "&userName="+userName ,
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
												$.unblockUI();  
												var errorFlag = data.errorFlag;
												var successFlag = data.successFlag;
												if(successFlag) {
													closeChangePasswordPopUp('changeUserPassword');
													setMessageMain(data.title, data.content, data.message, "");
													document.getElementById('displayMessageMain').style.display = "block";	
													
        										}
        										else{
        											setMessageChangePWD(data.title, data.content, data.type, "");
        											document.getElementById('displayMessageChangePassword').style.display = "block";	
        											
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

function onChangePasswordCancel() {
	closeChangePasswordPopUp('changeUserPassword');
}

function closeChangePasswordPopUp(dailogId){
		
		$("#"+dailogId).dialog("close");
		//$("#changePWDBtn").attr('disabled', true);
		
		if(dailogId == 'confirmationPopup') {
			$('#accordion').accordion('activate', 0 );
			$("#new_user_password").scrollTop(0);
			$('#new_user_password').hide();
			//$("#userFirstName").trigger("focus");
		} 
	}

function getColValueJson(id,colName){
	var str = userList;
	var colVal = "";
	var indexOfId = str.indexOf(id);
	
	var indexOfCreatedBy = -1;
	var indexOfComma = -1;
	if(indexOfId > 0){
		str = str.substring(parseInt(indexOfId), str.length);
		//userName
		indexOfCreatedBy = str.indexOf(colName);
		indexOfComma = str.indexOf(',', parseInt(indexOfCreatedBy));
		indexOfCreatedBy += colName.length + 2;
		colVal = str.substring(parseInt(indexOfCreatedBy), parseInt(indexOfComma));
		colVal = colVal.substring(1,colVal.length-1);
		colVal = trim(colVal);
	}else{
				
	}
	return colVal;
}

function nDataClick(popupname) {
	requetForUser = "Next";
	var isValueChanged = false;
		if(popupname == 'Edit') {
			isValueChanged = isEditUserDataChanged();
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
           	requetForUser = "Previous";
           		var isValueChanged = false;
	if(popupname == 'Edit') {
		isValueChanged = isEditUserDataChanged();
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
			var selectedUserId = $("#list2").jqGrid('getGridParam', 'selrow');
			var pageRows = $("#list2").jqGrid('getGridParam','rowNum');
			var curPage = parseInt($('#list2').jqGrid('getGridParam','page')); 
	        var str = idarray;
			var nextUserId ;
			var pageDataIds = $("#list2").jqGrid('getDataIDs'); 
			//alert("Selected:["+selectedUserId+"]fetchNextData:"+pageDataIds);
			str = pageDataIds;
			//var indexOfId = str.indexOf(selectedUserId);
			var indexOfId = -1;
			if(!Array.indexOf) {
				indexOfId = findIndexFromArray (str , selectedUserId);
			} else {
				indexOfId = str.indexOf(selectedUserId);
			}
			//alert("indexOfId :"+indexOfId+"::"+str.length+"::"+pageRows+"::"+curPage);
	
			if(indexOfId != str.length-1) {
				if(indexOfId%pageRows != pageRows-1){	//if(indexOfId != (pageRows*curPage)-1){
					nextUserId = str[indexOfId + 1];
					highlightnextprev(selectedUserId, nextUserId);
		      			if(popupname == 'Edit')
		      				EditUserDetail(nextUserId);
		      			//else
		      				//viewUserDetail(nextUserId);
		      			disablenextprev(indexOfId+1,str.length-1);
		      			populateTreeSelect();
		      			$("#list2").setSelection(nextUserId, true); 
	      		}
			}
			requetForUser = "";
		}
		
function fetchPreviousData(popupname){
	var selectedUserId = $("#list2").jqGrid('getGridParam', 'selrow');
	var pageRows = $("#list2").jqGrid('getGridParam','rowNum');
	var curPage = parseInt($('#list2').jqGrid('getGridParam','page'));
          var str = idarray;
	var preUserId ;
	var pageDataIds = $("#list2").jqGrid('getDataIDs'); 
	//alert("Selected:["+selectedUserId+"fetchPreviousData:"+pageDataIds);
	str = pageDataIds;
	var indexOfId = -1;
	if(!Array.indexOf) {
		indexOfId = findIndexFromArray (str , selectedUserId);
	} else {
		indexOfId = str.indexOf(selectedUserId);;
	}

	//alert("indexOfId :"+indexOfId+"::"+str.length+"::"+pageRows+"::"+curPage);

	if(indexOfId%pageRows != 0){	//if(indexOfId != (pageRows*(curPage-1))){
			preUserId = str[indexOfId - 1];
			highlightnextprev(selectedUserId, preUserId);
       		if(popupname == 'Edit')
       			EditUserDetail(preUserId);
       		//else
       			//viewUserDetail(preUserId);
       		disablenextprev(indexOfId-1,str.length-1);
       		populateTreeSelect();
       		$("#list2").setSelection(preUserId, true); 
    }	
	requetForUser = "";
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


function isEditUserDataChanged(){
		var newUserValue = $("#addEditUserDetail *").serializeArray(); 
		isValueChanged = false;	
		
		if(dbUserDetails.length != newUserValue.length) {
			isValueChanged = true;
		}
		 if(!isValueChanged) {
		      for(var key = 0; key <dbUserDetails.length ; key++) {
		       if(newUserValue[key].name != dbUserDetails[key].name) {
		       	isValueChanged = true;
		       	break;
		       }
		       if(trim(newUserValue[key].value) != trim(dbUserDetails[key].value)){
		      		isValueChanged = true;
		      		break;
		      	}
		      	 
		      }
	      }
	      var assignedOrgNodeIdsList = [];
	          if(String(assignedOrgNodeIds).indexOf(",") > 0) {
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
		     	} else {
		     		if(assignedOrgNodeIds != ""){
		     		 	if(organizationNodes.length > 1) {
			     		   	if(organizationNodes.length != 1) {
					      		isValueChanged = true;
					      	} 
					    } else {
					      		if(String(organizationNodes[0].orgNodeId) != assignedOrgNodeIds) {
					      			isValueChanged = true;
					      		}
					      	}
				      	
				     } else {
				     		isValueChanged = true;
				     }
		     	
		     	}
	     
	       
      	
      	return  isValueChanged;
}

function isExist(val, customerValCheckbox){
		for(var i=0; i < customerValCheckbox.length; i++){
			if(val == customerValCheckbox[i]){
				return true;
			}
		}
		return false;
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
		   $("#"+prevSelectedRow).removeClass("ui-state-highlight").attr({
               "aria-selected": "false",
               tabindex: "-1"
           });
           $("#"+nextSelectedRow).addClass("ui-state-highlight").attr({
               "aria-selected": true,
               tabindex: "0"
           });
}
			
function disablenextprev(selectedPosition,maxlength) {
               if(selectedPosition == 0 || ((selectedPosition%20)==0)) {
                	$("#pData").addClass("ui-state-disabled") ;
                    $("#viewpData").addClass("ui-state-disabled");
              
               } else {
						$("#pData").removeClass("ui-state-disabled");
                        $("#viewpData").removeClass("ui-state-disabled");
               }
               
                if ((selectedPosition == maxlength) || (((selectedPosition+1)%20) ==0)) {
               		$("#nData").addClass("ui-state-disabled");
                    $("#viewnData").addClass("ui-state-disabled");
               
               } else {
               		$("#nData").removeClass("ui-state-disabled");
                    $("#viewnData").removeClass("ui-state-disabled");
               }
}




