
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
var isTreeExpandIconClicked = false;
var isAction = true;
var isViewMod = false;
var titleViewEdit = $("#userEditTitleID").val();
var isGridRefreshRequired = false;

var currentNodeId ;
var currentCategoryLevel;
var currentTreeArray;
var currentIndex;
var dataObj2=[];
var dataObj3=[];
var dataObj4;
var dataObj5;
var dataObj6;
var dataObj7;
var dataObj8;
var rootMap = {};
var jsonData;
var map = new Map();
var rootNode = [];
var checkedListObject = {};
var type;
var asyncOver = 0;
var leafParentOrgNodeId = "";
var optionHtml="";


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
						jsonData = orgTreeHierarchy.data;
						getRootNodeDetails();
						createSingleNodeSelectedTree (orgTreeHierarchy);
						$("#searchheader").css("visibility","visible");	
						$("#orgNodeHierarchy").css("visibility","visible");							
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();
						window.location.href="/SessionWeb/logout.do";
						
					},
		complete :  function(){
						 $.unblockUI(); 
					}
	});

}

function UIBlock(){
	$.blockUI({ message: '<img src="/UserWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}
			

function createSingleNodeSelectedTree(jsondata) {
	   $("#orgNodeHierarchy").jstree({
	        "json_data" : {	             
	            "data" : rootNode,
				"progressive_render" : true,
				"progressive_unload" : true
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
	    	clearMessage();
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
	   registerDelegate("orgNodeHierarchy");
}
	
	
	function clearMessage(){
		document.getElementById('displayMessage').style.display = "none";	
		document.getElementById('displayMessageMain').style.display = "none";	
	  			
	}
	
	
function registerDelegate(tree){

	$("#"+tree).delegate("li ins","click", function(e) {
		type = tree;
		var x = this.parentNode;		
		var categoryId  = x.getAttribute("tcl");
		if(categoryId != null || categoryId != undefined){
			currentCategoryLevel = categoryId ; 
		}
		else{
			currentCategoryLevel ="1";
		}
		currentNodeId = x.id;			
		var classState = $(x).hasClass("jstree-open");			
		var rootCategoryLevel = rootMap[currentNodeId];
	
		if (classState == false){
			if (currentCategoryLevel == 1) {	
				dataObj2 = [];	
				var indexOfRoot = getIndexOfRoot(currentNodeId);
				populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot);
			}
	
			var cacheData = map.get(currentNodeId);
			if (cacheData != null){
				currentTreeArray = cacheData;			
			}
			if (cacheData == null){
				switch(currentCategoryLevel){
					
					//Not caching at initial level because the whole data will be put in cache which may increase the cache size
					//considerably
					
					case "2": 	dataObj3 =getObject(jsonData,currentNodeId,currentCategoryLevel,x.parentNode.parentNode.id);
								currentIndex = dataObj3.index;
								currentTreeArray = dataObj3.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//alert("case2");
							break;
							
					case "3": 	dataObj4 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj4,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								//alert("case3");
							break;
					case "4": 	dataObj5 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj5,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							//	alert("case4");
							break;
					case "5": 	dataObj6 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj6,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "6": 	dataObj7 = map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj7,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "7": 	dataObj8 =map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj8,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
								
							break;	
					case "8": 	dataObj9 =map.get(x.parentNode.parentNode.id);
								currentTreeArray =getObject(dataObj9,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;						
					
				}
			}

		}
			
	});

}
	
	
	
function createMultiNodeSelectedTree(jsondata) {
var styleClass;
  $("#innerID").jstree({
        "json_data" : {	             
            "data" : rootNode,
			"progressive_render" : true,
			"progressive_unload" : true
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
    })
    
    registerDelegate("innerID");
   
    	$("#innerID").delegate("li a","click",
    		 function(e) {
    				styleClass = $(this.parentNode).attr('class');
    				var orgcategorylevel = $(this.parentNode).attr("cid");
					var elementId = $(this.parentNode).attr('id');
					var currentlySelectedNode ="";
					isexist = false;
					currentId = $(this.parentNode).attr("id");
    				var element = this.parentNode;
					if(styleClass.indexOf("unchecked") > 0){
						$(this.parentNode).removeClass("jstree-unchecked").addClass("jstree-checked");
						checkedListObject[element.id] = "unchecked" ;
					}else {
						$(this.parentNode).removeClass("jstree-checked").addClass("jstree-unchecked");
						checkedListObject[element.id] = "checked" ;
					}
					
					var isChecked = $(element).hasClass("jstree-checked");
    			
					updateOrganization(element,isChecked);
			   	 }
			  );
    
   		 $("#innerID").bind("change_state.jstree",
   		 	 function (e, d) {
   		 	 if(isAction){ 

			    var elementId = d.rslt[0].getAttribute("id");
			    var currentlySelectedNode="";
				var isChecked = $(d.rslt[0]).hasClass("jstree-checked");
				if (isChecked){
					checkedListObject[elementId] = "checked" ;
				}else{					
					checkedListObject[elementId] = "unchecked" ;
				}
				updateOrganization(d.rslt[0],isChecked);
				}
        		}
        	);
}


	function updateOrganization(element,isChecked){
	
  		var currentlySelectedNode ="";
			isexist = false;
			currentId = $(element).attr("id");
			if(!isAddUser){
				if(assignedOrgNodeIds != "") {
						if(String(assignedOrgNodeIds).indexOf(",") > 0) {
					  			var orgList = assignedOrgNodeIds.split(",");
					  			for(var key=0; key<orgList.length; key++){
					  				if(trim(orgList[key]) == currentId){
					  					isexist = true; 
					  					break; 
					  				}
					  			}
					  		} else {
					  			if(assignedOrgNodeIds == currentId){
					  				isexist = true;  
					  			}
					  		}
					}
				if(isChecked) {
					if(!isexist) {
						currentlySelectedNode = optionHtml;
						if(currentlySelectedNode=="") {
							currentlySelectedNode += "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ getText(element)+"</a>";	
						} else {
							currentlySelectedNode = currentlySelectedNode + " , " + "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ getText(element)+"</a>";
						//console.log("if not exist"+ trim($(element).text()));
						}
						if(assignedOrgNodeIds=="") {
							assignedOrgNodeIds = $(element).attr("id");
						} else {
							assignedOrgNodeIds = $(element).attr("id") +"," + assignedOrgNodeIds; 
						}
						optionHtml = currentlySelectedNode;
					} else{
						currentlySelectedNode = optionHtml;
					}
					
				} else {
				if(isexist){
					currentlySelectedNode ="";
						//var newcurrentlySelectedNode = "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(this).attr("id")+"');>"+ trim($(this).text())+"</a>";
						 var nodeId = trim($(element).attr("id"));	
						var opList = optionHtml.split(",");
						for(var key=0; key<opList.length; key++){
							var opListVal = $.trim(opList[key]);
							 var resLen = opListVal.split(nodeId);
						//	if($.trim(newcurrentlySelectedNode) != opListVal){
						 	if(resLen.length < 2) {
								if(currentlySelectedNode=="") {
								currentlySelectedNode = opListVal;	
								} else {
								currentlySelectedNode = opListVal + " , " + currentlySelectedNode;
							}
							}
							
						}
						optionHtml = currentlySelectedNode;
					var newassignedOrgNodeIds ="";
						if(assignedOrgNodeIds != ""){
							if(String(assignedOrgNodeIds).indexOf(",") > 0) {
							var orgList = assignedOrgNodeIds.split(",");
								for(var key=0; key<orgList.length; key++){
								var orgListVal = $.trim(orgList[key]);
								 var esLen = orgListVal.split(nodeId);
									//if(orgListVal != currentId){
									if(esLen.length < 2) {
										if(newassignedOrgNodeIds=="") {
											newassignedOrgNodeIds = orgListVal;
										} else {
											newassignedOrgNodeIds = orgListVal +"," + newassignedOrgNodeIds; 
										}
									}
									
								}
							}
							 if(newassignedOrgNodeIds == "") {
								assignedOrgNodeIds = "";
							} else {
								assignedOrgNodeIds = newassignedOrgNodeIds;
							}
						}
					} else {
						currentlySelectedNode = optionHtml;
					}
				}
			}
			if(isAddUser){
				var currentlySelectedNode ="";
				assignedOrgNodeIds = "";
				$("#innerID").find(".jstree-checked").each(function(i, element){
					
			
							if(currentlySelectedNode=="") {
								currentlySelectedNode += "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ getText(element)+"</a>";	
							} else {
								currentlySelectedNode = currentlySelectedNode + " , " + "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ getText(element)+"</a>";
							}
			
				    		if(assignedOrgNodeIds=="") {
								assignedOrgNodeIds = $(element).attr("id");
							} else {
								assignedOrgNodeIds = $(element).attr("id") +"," + assignedOrgNodeIds; 
							}
			    		
					});
			}
			
				if(currentlySelectedNode.length > 0 ) {
					$("#notSelectedOrgNodes").css("display","none");
					$("#selectedOrgNodesName").html(currentlySelectedNode);	
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
			$("#innerID").unbind();
			createMultiNodeSelectedTree (orgTreeHierarchy);	
}



 
		
			
function populateGrid() {

		document.getElementById('changePW').style.visibility = "visible";
		// $("#changePWDBtn").attr('disabled', true); 
		setAnchorButtonState('changePWButton', true);
		resetSearchCrit();
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
 		
         $("#list2").jqGrid({         
         url:'userOrgNodeHierarchyGrid.do', 
		 mtype:   'POST',
		 datatype: "json",         
          postData: postDataObject,      
          colNames:[$("#jqgLastNameID").val(),$("#jqgFirstNameID").val(),$("#jqgLoginID").val(), $("#jqgRoleID").val(), $("#jqgEmailID").val(),$("#jqgOrgID").val()],
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
			width: $("#jqGrid-content-section").width(), 
			editurl: 'userOrgNodeHierarchyGrid.do',
			ondblClickRow: function(rowid) {EditUserDetail();},
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
				setAnchorButtonState('changePWButton', true);
				
			},
			onSortCol:function(){
				setAnchorButtonState('changePWButton', true);
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
						window.location.href="/SessionWeb/logout.do";
						
					}
	 });
			jQuery("#list2").jqGrid('navGrid','#pager2',
			{
				search: false,add:false,edit:false,del:false 
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-search", onClickButton:function(){
			    	$("#searchUserByKeyword").dialog({  
						title:$("#searchUserID").val(),  
					 	resizable:false,
					 	autoOpen: true,
					 	width: '300px',
					 	modal: true,
						closeOnEscape: false,
					 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
					 	});
			    }, position: "one-before-last", title:"Search User", cursor: "pointer"
			}).jqGrid('navSeparatorAdd',"#pager2",{position: "first"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-trash", onClickButton:function(){
			    	var rowid = $("#list2").jqGrid('getGridParam', 'selrow');
			    	if(rowid == ''|| rowid == null){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 200,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		 requetForUser = "";
		    		 	 deleteUserPopup();
			    	}
			    	
			    }, position: "first", title:"Delete User", cursor: "pointer",id:"del_list2"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-pencil", onClickButton:function(){
			    	var rowid = $("#list2").jqGrid('getGridParam', 'selrow');
			    	if(rowid == ''|| rowid == null){
			    		$("#nodataSelectedPopUp").dialog({  
							title:"Warning",  
						 	resizable:false,
						 	autoOpen: true,
						 	width: 200,
						 	height: 80,
						 	modal: true,
						 	open: function(event, ui) {$(".ui-dialog-titlebar-close").show();}
						 	});
			    	}else {
			    		requetForUser = "";
		    		 	EditUserDetail();
			    	}
			    	 
			    }, position: "first", title:"View/Edit User", cursor: "pointer",id:"edit_list2"
			}).jqGrid('navButtonAdd',"#pager2",{
			    caption:"", buttonicon:"ui-icon-plus", onClickButton:function(){
			    	 requetForUser = "";
		    		 AddUserDetail();
			    }, position: "first", title:"Add User", cursor: "pointer",id:"add_list2"
			});  
			jQuery(".ui-icon-refresh").bind("click",function(){
				$("#searchUserByKeywordInput").val('');
			});
		setupButtonPerUserPermission();	 
}

function resetSearchCrit(){
	$("#searchUserByKeywordInput").val('');
	var grid = $("#list2"); 
	grid.jqGrid('setGridParam',{search:false});	
    var postData = grid.jqGrid('getGridParam','postData');
    $.extend(postData,{filters:""});
}

function searchUserByKeyword(){
	 var searchFiler = $.trim($("#searchUserByKeywordInput").val()), f;
	 var grid = $("#list2"); 
	 
	 if (searchFiler.length === 0) {
		 grid[0].p.search = false;
	 }else {
	 	 f = {groupOp:"OR",rules:[]};
		 f.rules.push({field:"lastName",op:"cn",data:searchFiler});
		 f.rules.push({field:"firstName",op:"cn",data:searchFiler});
		 f.rules.push({field:"loginId",op:"cn",data:searchFiler});
		 f.rules.push({field:"role",op:"cn",data:searchFiler});
		 f.rules.push({field:"email",op:"cn",data:searchFiler});
		 f.rules.push({field:"orgNodeNamesStr",op:"cn",data:searchFiler}); 
		 grid[0].p.search = true;
		 grid[0].p.ignoreCase = true;
		 $.extend(grid[0].p.postData,{filters:JSON.stringify(f)});
	 }
	 grid.trigger("reloadGrid",[{page:1,current:true}]); 
	 closePopUp('searchUserByKeyword');
}

function resetSearch(){
	var grid = $("#list2"); 
	$("#searchUserByKeywordInput").val('');
	 grid[0].p.search = false;
	 grid.trigger("reloadGrid",[{page:1,current:true}]); 
	 closePopUp('searchUserByKeyword');
}

function trapEnterKey(e){
	var key;
   if(window.event)
        key = window.event.keyCode;     //IE
   else
        key = e.which;     //firefox
        
   if(key == 13){
   		searchUserByKeyword();
   }
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
	
	document.getElementById('changePW').style.visibility = "visible";
	
			$("#changeUserPassword").dialog({  
					title:$("#chgPwdID").val()+getUserName(),  
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
  		resetSearchCrit();
  		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.treeOrgNodeId = $("#treeOrgNodeId").val();
        jQuery("#list2").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});
        var sortArrow = jQuery("#list2");
        jQuery("#list2").jqGrid('setGridParam', {url:'userOrgNodeHierarchyGrid.do',postData:postDataObject,page:1}).trigger("reloadGrid");
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
								title:$("#editRecordID").val(),  
							 	resizable:false,
							 	autoOpen: true,
								closeOnEscape: false,
							 	width: '600px'
							 	});	
	$("#preButton").css("visibility","visible");	
	$("#nextButton").css("visibility","visible");	
	$('#userAccordion ul:eq(0)').show();
	
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
	//$("#displayMessage").empty();
	//var selectedUserId = $("#list2").jqGrid('getGridParam', 'selrow');
	if(selectedUserId == undefined ){
		rowid = $("#list2").jqGrid('getGridParam', 'selrow');
	} else {
		rowid = selectedUserId;
	}
	//var userName =  getColValueJson(rowid,'userName');
	var userName = $('#list2').jqGrid('getCell',rowid,'loginId');
	var postDataObject = {};
 	postDataObject.isLasLinkCustomer = $("#isLasLinkCustomer").val();
 	postDataObject.selectedUserName = userName;
 	
		$.ajax({
		async:		true,
		beforeSend:	function(){
						
						UIBlock();
					},
		url:		'getUserDetailsForEdit.do', 
		type:		'POST',
		dataType:	'json',
		data:		postDataObject,
		success:	function(data, textStatus, XMLHttpRequest){	
		
						//alert('data.viewMode::'+data.viewMode);
						$.unblockUI();
						if (data.viewMode) {
							viewEnable();
							viewUser (data);
							isViewMod = true;
							titleViewEdit = $("#userViewTitleID").val();
						} else {
							isViewMod = false;
							var primaryPhn3 = trim(data.userContact.primaryPhone3);
							var primaryPhoneExtn = trim(data.userContact.primaryPhone4);
							var secondaryPhn3 = trim(data.userContact.secondaryPhone3);
							var secondaryPhoneExtn = trim(data.userContact.secondaryPhone4);
							
							titleViewEdit = $("#userEditTitleID").val();
							editEnable();
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
							$("#userEmail").val(data.email);
							$("#timeZoneOptions").val(data.timeZone);
							//alert(data.timeZone);
							//$("#roleOptions").val(data.role);
							$("#roleOptions").val(data.roleId);
							$("#userExternalId").val(data.extPin1);
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
							$("#primaryPhone3").val(primaryPhn3);
							$("#primaryPhone4").val(primaryPhoneExtn);
							$("#secondaryPhone1").val(data.userContact.secondaryPhone1);
							$("#secondaryPhone2").val(data.userContact.secondaryPhone2);
							$("#secondaryPhone3").val(secondaryPhn3);
							$("#secondaryPhone4").val(secondaryPhoneExtn);
							$("#faxNumber1").val(data.userContact.faxNumber1);
							$("#faxNumber2").val(data.userContact.faxNumber2);
							$("#faxNumber3").val(data.userContact.faxNumber3);
							
							fillselectedOrgNode("selectedOrgNodesName", data.organizationNodes);
							prepareCheckedList();
							
						}
		
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
								title:titleViewEdit,  
							 	resizable:false,
							 	autoOpen: true,
							 	width: '800px',
							 	modal: true,
								closeOnEscape: false,
							 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide();}
							 	});	
						/*$('#addEditUserDetail').bind('keydown', function(event) {
			 				  var code = (event.keyCode ? event.keyCode : event.which);
  							  if(code == 27){
			  				  onCancel();
			  				  return false;
			 				 }
			 				
							});*/
						 	
						 	if (data.viewMode) {
								$("#viewEditDisplayId").css("display","none");
							} else {
								$("#viewEditDisplayId").css("display","block");
								$("#userAccordion h3").css("display","block");
							} 
							setPopupPosition(isAddUser);
							checkOpenNode(data.organizationNodes);
							dbUserDetails = $("#addEditUserDetail *").serializeArray(); 	
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
		
	});
	
	}
	
	function prepareCheckedList(){	
		var orgList = assignedOrgNodeIds;
		orgList = String(orgList).split(",");	
		for(var i = 0; i < orgList.length; i++) {	
			checkedListObject[orgList[i]] = "checked";	
		}
	}
	
	function viewEnable () {
		$("#userAccordion").accordion("destroy");
		$("#userAccordion").accordion({ header: "h3", autoHeight : true });
		//$("#userAccordion h3").css("display","block");
		//$("").trigger("focus");
		//$("").focusin();
		$("#user_information_acco").hide();
		$("#user_information_view_acco").show();
		//$("#user_information_view_acco h3").focusin();
		$("#userAccordion").accordion( "activate" , 1 );	
		$("#contact_information_acco").hide();
		$("#contact_information_view_acco").show();
		//$("#contact_information_view_acco h3").focusin();	
		$("#saveBtn").css("display","none");
		$("#cancelBtn").css({'padding-left': '300px'});
					
	}
	
	function editEnable () {
		$("#userAccordion").accordion("destroy");
		$("#userAccordion").accordion({ header: "h3", autoHeight : true });
		$("#user_information_acco").show();
		//$("#user_information_acco h3").focusin();
		$("#userAccordion").accordion( "activate" , 0 );
		$("#user_information_view_acco").hide();
		$("#contact_information_acco").show();
		//$("#contact_information_acco h3").focusin();
		$("#contact_information_view_acco").hide();
		$("#saveBtn").css("display","block");
		$("#cancelBtn").css({'padding-right': '0px'});
		$("#cancelBtn").css({'padding-left': '0px'});
		
	}
	
	function viewUser (data) {
	
		$("#userFirstNameView").text(data.firstName);
		$("#userMiddleNameView").text(data.middleName);
		$("#userLastNameView").text(data.lastName);
		$("#userEmailView").text(data.email);
		$("#timeZoneOptionsView").text(data.timeZoneDesc);
		$("#roleOptionsView").text(data.role);
		$("#userExternalIdView").text(data.extPin1);
		$("#selectedOrgNodesNameView").text(data.organizationNodes);
		$("#addressLine1View").text(data.userContact.addressLine1);
		$("#addressLine2View").text(data.userContact.addressLine2);
		$("#cityView").text(data.userContact.city);
		$("#stateOptionsView").text(data.userContact.stateDesc);
		var zipCode1 = data.userContact.zipCode1;
		var zipCode2 = data.userContact.zipCode2;
		$("#zipCodeView").text(formatZipCode (zipCode1, zipCode2));
		var primaryPhone1 = data.userContact.primaryPhone1;
		var primaryPhone2 = data.userContact.primaryPhone2;
		var primaryPhone3 = data.userContact.primaryPhone3;
		var primaryPhone4 = data.userContact.primaryPhone4;
		$("#primaryPhoneView").text(formatPhoneNumber (primaryPhone1, primaryPhone2, primaryPhone3, primaryPhone4));
		var secondaryPhone1 = data.userContact.secondaryPhone1;
		var secondaryPhone2 = data.userContact.secondaryPhone2;
		var secondaryPhone3 = data.userContact.secondaryPhone3;
		var secondaryPhone4 = data.userContact.secondaryPhone4;
		$("#secondaryPhoneView").text(formatPhoneNumber (secondaryPhone1, secondaryPhone2, secondaryPhone3, secondaryPhone4))
		var faxNumber1 = data.userContact.faxNumber1;
		var faxNumber2 = data.userContact.faxNumber2;
		var faxNumber3 = data.userContact.faxNumber3;
		$("#faxNumberView").text(formatFaxNumber(faxNumber1,faxNumber2,faxNumber3));
		
		$("#selectedOrgNodesNameView").html(data.orgNodeNamesStr);
	
	}
	
	/**
		This method is responsible to format the ZipCode.
	*/
	
	function formatZipCode (zipCode1, zipCode2) {
	
		var zipCode = "";
		if (zipCode1 != "") {
			
			zipCode = zipCode1;
			
			if (zipCode2 != "") {
			
				zipCode = zipCode+"-"+zipCode2;
			}
						
		}
		return zipCode;
	}
	
	/**
	
		This method is responsible to format the PhoneNumber.
	*/
	
	
	function formatPhoneNumber (phone1, phone2, phone3, phone4) {
	
		var phoneNumber = "";
		
		if (phone1 != "") {
		
			phoneNumber = "("+phone1+")"+phone2+"-"+phone3;
			
			if (phone4 != "") {
			
				phoneNumber = phoneNumber+$("#extID").val()+phone4;
			}		
		}
	
		return phoneNumber;
	}
	
	/**
	
		This method is responsible to format the FaxNumber
	
	*/
	
	function formatFaxNumber (fax1, fax2, fax3) {
	
		var faxNumber = "";
		
		if (fax1 != "") {
		
			faxNumber = "("+fax1+")"+fax2+"-"+fax3;
		} 
	
		return faxNumber;
	}
	
	
	//end of EditUserDetail()
	
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
	for(var i = 0; i < orgList.length; i++) {
	
		if(assignedOrgNodeIds == "") {
			assignedOrgNodeIds = orgList[i].orgNodeId ;
		} else {
			assignedOrgNodeIds = assignedOrgNodeIds + "," + orgList[i].orgNodeId ; 
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
	editEnable();
	$("#viewEditDisplayId").show();
	/*$("#preButton").css("visibility","hidden");	
	$("#nextButton").css("visibility","hidden");*/	
	document.getElementById('displayMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";
	
	if(!(roleOptions.length > 0 
		&& timeZoneOptions.length > 0
			&& stateOptions.length > 0)){
			
	var postDataObject = {};
 	postDataObject.isLasLinkCustomer = $("#isLasLinkCustomer").val();		
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						
						UIBlock();
					},
		url:		'getOptionList.do', 
		type:		'POST',
		dataType:	'json',
		data:		postDataObject,
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
						/*$('#addEditUserDetail').bind('keydown', function(event) {
			 				  var code = (event.keyCode ? event.keyCode : event.which);
  							  if(code == 27){
			  				  onCancel();
			  				  return false;
			 				 }
			 				
							});*/
						 	 
							setPopupPosition(isAddUser);	
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
		
	});
	
	} else {
		reset();
		$("#addEditUserDetail").dialog({  
			title:$("#addUserID").val(),  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '800px',
		 	modal: true,
		 	closeOnEscape: false,
		 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		 	});	
		 	/*$('#addEditUserDetail').bind('keydown', function(event) {
		 						  //alert("key up 2");
 							var code = (event.keyCode ? event.keyCode : event.which);
 							if(code == 27){
		  				  	onCancel();
		  				 	return false;
		 				 }
		 				
						});*/
		setPopupPosition(isAddUser);	
	}	
							 	
						 	
	}
	
	
	function setPopupPosition(isAddUser){
				var toppos = ($(window).height() - 610) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#addEditUserDetail").parent().css("top",toppos);
				$("#addEditUserDetail").parent().css("left",leftpos);
				$("#addEditUserDetail").css("overflow",'hidden');		 	 
				$("#User_Information").css("height",'300px');
				$("#User_Information").css("overflow",'auto');
				$("#Contact_Information").css("height",'300px');
				$("#Contact_Information").css("overflow",'auto');
				$("#User_Information_view").css("height",'300px');
				$("#User_Information_view").css("overflow",'auto');
				$("#Contact_Information_view").css("height",'300px');
				$("#Contact_Information_view").css("overflow",'auto');
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
	var orgName = $("#selectedOrgNodesName").text();
	var treeOrgNodeId = $("#treeOrgNodeId").val();
	
	if(String(assignedOrgNodeIds).indexOf(",") > 0) {
		 var assignedOrgIdList = assignedOrgNodeIds.split(",");
		 for( var i=0; i<assignedOrgIdList.length; i++) {
		 	var keyVal = $.trim(assignedOrgIdList[i]);
		 	if( keyVal == treeOrgNodeId){
		 		isGridRefreshRequired = true;
		 		break;
		 	}
		 }
	}
	else {
		if( assignedOrgNodeIds == treeOrgNodeId){
		 		isGridRefreshRequired = true;
		}
	}
	
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
													//setMessageMain(data.title, data.content, data.type, "");
													$("#contentMain").text(data.content);
													$('#errorIcon').hide();
													$('#infoIcon').show();
													document.getElementById('displayMessageMain').style.display = "block";
													assignedOrgNodeIds = "";
													
													// Added new codes for Refreshing the Grid for Adding/Updating users : Start
													var dataToBeAdded = {lastName:data.userProfile.lastName,
																		 firstName:data.userProfile.firstName,
																		 loginId:data.userProfile.userName,
																		 role:data.userProfile.role,
																		 email:data.userProfile.email,
																		 orgNodeNamesStr:orgName};
													
													var sortOrd = jQuery("#list2").getGridParam("sortorder");
													var sortCol = jQuery("#list2").getGridParam("sortname");	
													
													if(isAddUser) {
														if(isGridRefreshRequired){
															jQuery("#list2").addRowData(data.userProfile.userId, dataToBeAdded, "first");
															isGridRefreshRequired = false;
														}
													}
													else {
														if (isGridRefreshRequired){
															jQuery("#list2").setRowData(data.userProfile.userId, dataToBeAdded, "first");
															isGridRefreshRequired = false;
														}
														else {
															jQuery("#list2").delRowData(data.userProfile.userId);
														}
													}
											
													jQuery("#list2").sortGrid(sortCol,true);
													
													// Added new codes for Refreshing the Grid for Adding/Updating users : End
        										}
        										else{
        											setMessage(data.title, data.content, data.type, "");    
        											$("#contentMain").text(data.content);
													$('#errorIcon').show();
													$('#infoIcon').hide(); 
        											document.getElementById('displayMessage').style.display = "block";	
        											
        										}
																								
											},
								error  :    function(XMLHttpRequest, textStatus, errorThrown){
													$.unblockUI();  
												window.location.href="/SessionWeb/logout.do";
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
		
		$("#notSelectedOrgNodes").css("display","inline");
					$("#selectedOrgNodesName").text("");
		
	 }
	
	
	function onCancel() {
		
		isValueChanged = false;
		//$("#displayMessage").text("");
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
			$('#userAccordion').accordion('activate', 0);			
			$("#User_Information").scrollTop(0);
			$("#Contact_Information").scrollTop(0);
			$('#Contact_Information').hide();
		//	populateTreeSelect();
			$('#innerID').jstree('close_all', -1);
			$("#notSelectedOrgNodes").css("display","inline");
			$("#selectedOrgNodesName").text("");
			isPopUp = false;
			checkedListObject = {};
		}
		
		if(dailogId != "EmailWarning" && dailogId != "confirmationPopup") {
			$('#innerID').jstree('uncheck_all');
			checkedListObject = {};
		}

		$("#"+dailogId).dialog("close");
		
		if(dailogId == 'confirmationPopup') {
		//if second accordion is open, the focus should stay on second accordion 
			//$('#userAccordion').accordion('activate', 0 );
			//$("#User_Information").scrollTop(0);
			//$("#Contact_Information").scrollTop(0);
			//$('#Contact_Information').hide();
			//$("#userFirstName").trigger("focus");
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
			title:$("#emailAlertID").val(),  
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
	//var userName = getColValueJson(userId,'userName');
	var userName = $('#list2').jqGrid('getCell',userId,'loginId');
	
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
												window.location.href="/SessionWeb/logout.do";
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
			//$("#displayMessage").text("");
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
	//$("#displayMessage").text("");
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
				title:$("#confirmID").val(),  
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
			title:$("#confirmID").val(),  
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

			if(isViewMod){
				return  false;
			}

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

function openTreeNodes(orgNodeId) {
	var isopened = false;
	asyncOver = 0;
	leafParentOrgNodeId = "";
	var par = null;
	$('#innerID').jstree('close_all');
	 if(isTreeExpandIconClicked )
	    return;
	
		var isIdExist = $('#innerID', '#'+assignedOrgNodeIds).length;
			if(isIdExist > 0){
				$('#innerID').jstree('check_node', "#"+assignedOrgNodeIds);
				isopened = true; 
			} else {
				leafParentOrgNodeId = "";
				for(var i=0; i< organizationNodes.length; i++){
						if(orgNodeId == organizationNodes[i].orgNodeId){
							var leafOrgNodePath = organizationNodes[i].leafNodePath;
							 leafParentOrgNodeId = leafOrgNodePath.split(",");
							break;
						}
					}
					type = "innerID";
					if(leafParentOrgNodeId.length > 0) {
						for(var count = 0; count < leafParentOrgNodeId.length - 1; count++) {
				  		 	var tmpNode = leafParentOrgNodeId[count];

				  		 	currentCategoryLevel = String(count+1);
				  		 	currentNodeId = tmpNode;
				  		 	if(count != 0) { 
				  		 		par = leafParentOrgNodeId[count-1];		 						  		 			
				  		 	} else {
				  		 		par = leafParentOrgNodeId[count];
				  		 	}
				  		 	prepareData(false,currentCategoryLevel,currentNodeId,par);
				  		 }	 			
				  		 	var tmpNode = leafParentOrgNodeId[0];	
				  		 	currentCategoryLevel = "1";
				  		 	currentNodeId = tmpNode;
				  		 	currentTreeArray = map.get(currentNodeId);
				  		 	$('#innerID').jstree('open_node', "#"+currentNodeId); 
							
				  		 	$('#innerID').jstree('check_node', "#"+orgNodeId);
				  		 	$("#"+orgNodeId).focus();
				  		 	isopened = true; 
			  		 }
		 }

		
	}
	
	function populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot){	
	//TODO : Updation in root node is a problem need to work on that but this method is also necessary for populating the immediate //tree because if we use the cache all the 2nd level objects will be stored in cache which makes the cache very heavy
		var jsonObject = jsonData;
		jsonObject = jsonObject[indexOfRoot];
		if (dataObj2.length == 0){
			for (var i = 0, j = jsonObject.children.length; i < j; i++ ){
					dataObj2.push({data: jsonObject.children[i].data,attr:{id: jsonObject.children[i].attr.id,cid:jsonObject.children[i].attr.cid, tcl:jsonObject.children[i].attr.tcl,chlen:jsonObject.children[i].hasOwnProperty("children")},children : [{}]});	
			}
		}
	}
	
	/******Jstree Methods*****/
	//method triggered from library
	  function customLoad(){
	  //	console.log("Custom Load called");
	    pushInsideElement(currentNodeId,currentCategoryLevel,dataObj2,currentTreeArray);
	  }
  
    function pushInsideElement(currentNodeId,currentCategoryLevel,dataObj2,currentTreeArray){
		var objArray;
		switch(currentCategoryLevel){
		case "1": objArray = dataObj2;				  
				break;
		case "2": objArray = currentTreeArray.children;
				break;
				
		case "3": objArray = currentTreeArray.children;
				break;
		case "4": objArray = currentTreeArray.children;
				break;
		case "5": objArray = currentTreeArray.children;
				break;
		case "6": objArray = currentTreeArray.children;
				break;
		case "7": objArray = currentTreeArray.children;
				break;
		case "8": objArray = currentTreeArray.children;
				break;
		}
		
		updateTree(objArray);	
	  }
	  function updateTree(objArray){
	  	//timer.setStartTime();
	  	var currentElement;
		if (isPopUp){
			currentElement = $('li[id='+ currentNodeId+ ']');
			if(currentElement.length < 2) currentElement = currentElement[0];
			else currentElement = currentElement[1];
				
		}else {		
			currentElement = document.getElementById(currentNodeId);
		}
	
		currentElement.className = "jstree-open jstree-"+ getCheckedStatus(currentNodeId);
		var fragment = document.createDocumentFragment();
		var ulElement = document.createElement('ul');
		
		if(type.indexOf("innerID") < 0){
		stream(objArray,ulElement,fragment,streamPush, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
			 //currentElement.childNodes[1].firstChild.style.display = "none";
		 });	
		 }
		 else{
		 stream(objArray,ulElement,fragment,streamInnerPush, null, function(){
			currentElement.appendChild(fragment);
			$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
			asyncOver++;
			openNextLevel(asyncOver);
			// currentElement.childNodes[1].firstChild.style.display = "none";
		 });	
		 }
	  }

	  //Seperated outer tree push and inner tree push because each method is a performance critical operation conditions will increase the 
	  // tree population.	  
	  function streamPush(objArray,ulElement,fragment){
	  	var liElement = document.createElement('li');
		liElement.setAttribute("id", objArray.attr.id);
		liElement.setAttribute("cid" , objArray.attr.cid);
		liElement.setAttribute("tcl" , objArray.attr.tcl);
		var condition = objArray.attr.chlen;
		
		switch(condition){
		case false: liElement.className = "jstree-leaf jstree-unchecked";
					break;
		case true:  liElement.className = "jstree-closed jstree-unchecked";
					break;
		case undefined: var con = objArray.hasOwnProperty("children");
							switch(con){
								case false: liElement.className = "jstree-leaf jstree-unchecked";
											break;
								case true:  liElement.className = "jstree-closed jstree-unchecked";
											break;						
							}
						  break;
					}
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		ulElement.appendChild(liElement);
		fragment.appendChild(ulElement);
	  }
	  
	  function streamInnerPush(objArray,ulElement,fragment){
	  	var liElement = document.createElement('li');
		liElement.setAttribute("id", objArray.attr.id);
		liElement.setAttribute("cid" , objArray.attr.cid);
		liElement.setAttribute("tcl" , objArray.attr.tcl);
		var condition = objArray.attr.chlen;
		
		switch(condition){
		case false: liElement.className = "jstree-leaf jstree-"+ getCheckedStatus(objArray.attr.id);
					break;
		case true:  liElement.className = "jstree-closed jstree-"+ getCheckedStatus(objArray.attr.id);
					break;
		case undefined: var con = objArray.hasOwnProperty("children");
							switch(con){
								case false: liElement.className = "jstree-leaf jstree-"+ getCheckedStatus(objArray.attr.id);
											break;
								case true:  liElement.className = "jstree-closed jstree-"+ getCheckedStatus(objArray.attr.id);
											break;						
							}
						  break;
		
		}	
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: inline-block;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		ulElement.appendChild(liElement);
		fragment.appendChild(ulElement);
	  }
	
	
	
	/************Tree Utils*****************/
	function getObject(jsonObject,currentNodeId,currentCategoryLevel, parentNodeId){
			var obj = new Object();
			var indexRoot = getIndexOfRoot(parentNodeId);
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
	
	
	function getRootNodeDetails(){
		var noOfRoots = jsonData.length;
			for (var i = 0,j = noOfRoots; i < j; i++ ){
				rootMap[jsonData[i].attr.id] = jsonData[i].attr.cid;
				rootNode.push({data: jsonData[i].data,attr:{id: jsonData[i].attr.id,cid:jsonData[i].attr.cid,tcl:jsonData[i].attr.tcl},children : [{}]});
			}
	}


	function getIndexOfRoot(currentNodeId){
		var numberOfRoots = jsonData.length;
	
		for (var i = 0,j = numberOfRoots; i < j; i++ ){
			if (jsonData[i].attr.id == currentNodeId){				
				return i;
			}
		}	
	}
	
	function stream(array,element,fragment,process,context,callback){
	 var treeData = array.concat();   
	
		setTimeout(function(){

			var start = +new Date();

			do {
				 process.call(context, treeData.shift(),element,fragment);
			} while (treeData.length > 0 && (+new Date() - start < 50));

			if (treeData.length > 0){
				setTimeout(arguments.callee, 25);
			} else {
				callback(array);
			}
		}, 25);    
	
	}	
	
	function getCheckedStatus(id){
    if (checkedListObject[id] == "checked"){
		return "checked";
	}
  		return "unchecked";
  }
  
  
  function prepareData(classState,currentCategoryLevel,currentNodeId,element){
  
		if (classState == false || !classState || classState == 'false'){

			var cacheData = map.get(currentNodeId);
			if (cacheData != null){
				currentTreeArray = cacheData;			
			}
					
			if (cacheData == null){
		if (currentCategoryLevel == "1") {	
			dataObj2 = [];	
			var indexOfRoot = getIndexOfRoot(currentNodeId);
			populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot);
		}
				switch(String(currentCategoryLevel)){
					
					case "2": 	dataObj3 =getObject(jsonData,currentNodeId,currentCategoryLevel,element);
								currentIndex = dataObj3.index;
								currentTreeArray = dataObj3.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;							
					case "3": 	dataObj4 = map.get(element);
								currentTreeArray =getObject(dataObj4,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "4": 	dataObj5 = map.get(element);
								currentTreeArray =getObject(dataObj5,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "5": 	dataObj6 = map.get(element);
								currentTreeArray =getObject(dataObj6,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "6": 	dataObj7 = map.get(element);
								currentTreeArray =getObject(dataObj7,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;
					case "7": 	dataObj8 =map.get(element);
								currentTreeArray =getObject(dataObj8,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);								
							break;	
					case "8": 	dataObj9 =map.get(element);
								currentTreeArray =getObject(dataObj9,currentNodeId,currentCategoryLevel);
								currentIndex = currentTreeArray.index;
								currentTreeArray = currentTreeArray.jsonData;
								map.put(currentNodeId,currentTreeArray);
							break;						
					
				}
			}

		}
  
  }
  
  
  function openNextLevel(asyncOver){
  		if(leafParentOrgNodeId.length - 1 > asyncOver) {
	  		var tmpNode = leafParentOrgNodeId[asyncOver];	
			currentCategoryLevel = String(asyncOver + 1);
			currentNodeId = tmpNode;
			currentTreeArray = map.get(currentNodeId);
			$('#innerID').jstree('open_node', "#"+currentNodeId);
		}
  }
  
  
  
	/******CRUD Operations****/
		function addTree(currentNodeId){
		//TODO :  
		}

		function deleteTree(currentNodeId){
		//TODO :  
		}

		function moveTree(currentNodeId){
		deleteTree(currentNodeId);
		addTree(currentNodeId);
		}

		function hideCheckBox(){
		//TODO: Use the pushInside data.attr.children == null add leaf class.

		}

		function openTree(){
		//TODO: need to work on;
		} 
	
	
	function setupButtonPerUserPermission() {
		var deleteUserEnable = $("#deleteUserEnable").val();
		if (deleteUserEnable == 'false') {	
			var element = document.getElementById('del_list2');
			element.style.display = 'none';
		}
	}
	
	function deleteUserPopup(){
		//console.log("deleteUserPopUp");
		$('#displayMessageMain').hide();
				
		$("#deleteUserPopup").dialog({  
			title:$("#delUserTitleID").val(),  
			resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});
				
	    $("#deleteUserPopup").css('height',130);
		var toppos = ($(window).height() - 130) /2 + 'px';
		var leftpos = ($(window).width() - 400) /2 + 'px';
		$("#deleteUserPopup").parent().css("top",toppos);
		$("#deleteUserPopup").parent().css("left",leftpos);
		
	}
	
	function deleteSelectedUser(){
		closePopUp('deleteUserPopup');
		var selectedUserId = $("#list2").jqGrid('getGridParam', 'selrow');
		var selectedUserName = $('#list2').jqGrid('getCell',selectedUserId,'loginId');
		var postDataObject = {};
 		postDataObject.selectedUserId = selectedUserId;
 		postDataObject.selectedUserName = selectedUserName;

		$.ajax(
			{
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'deleteUser.do',
				type:		'POST',
				dataType:	'json',
				data:		postDataObject,
				success:	function(data, textStatus, XMLHttpRequest){
								var successFlag = data.successFlag;
								if(successFlag){
									$("#contentMain").text(data.message);
									$('#errorIcon').hide();
									$('#infoIcon').show();
									$("#displayMessageMain").show();
									jQuery("#list2").delRowData(selectedUserId);
								}
								else{
									$("#contentMain").text(data.message);
									$('#errorIcon').show();
									$('#infoIcon').hide();
									$("#displayMessageMain").show();
								}
															
							    $.unblockUI(); 																														 						
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								$.unblockUI();
								window.location.href="/SessionWeb/logout.do";
							}
			}
		);
	}
	