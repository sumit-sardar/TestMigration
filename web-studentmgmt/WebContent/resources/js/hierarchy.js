
var gridjsondata;
var idarray =[];
var gridloaded = false;

var gradeOptions = []; 
var genderOptions = []; 
var dayOptions = []; 
var yearOptions = []; 
var monthOptions = []; 
var testPurposeOptions = [];
var orgTreeHierarchy;
var assignedOrgNodeIds = "";
var customerDemographicValue;
var isValueChanged = false;
var leafNodeCategoryId;

			
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

function blockUI(){	
	$("body").append('<div id="blockDiv" style="position:fixed;top:0;left:0;width:100%;height:100%;z-index:9999"><img src="/StudentManagementWeb/resources/images/loading.gif" style="left:50%;top:40%;position:absolute;"/></div>');
	$("#blockDiv").css("cursor","wait");		
}
			
function unblockUI(){
	$("#blockDiv").css("cursor","normal");
	$("#blockDiv").remove();
}

function overlayblockUI(){	
	$("body").append('<div id="blDiv" style="position: absolute;top:0;left:0;width:100%;height:100%;z-index:10"></div>');
	$("#blDiv").css("cursor","wait");		
						
}

function overlayunblockUI(){
	$("#blDiv").css("cursor","normal");
	$("#blDiv").remove(); 
}

function confirmationblockUI(){	
	$("body").append('<div id="conDiv" style="opacity: 0.6; background-color: #000000;position: absolute;top:0;left:0;width:100%;height:100%;z-index:10"></div>');
	$("#conDiv").css("cursor","wait");		
						
}

function confirmationunblockUI(){
	$("#conDiv").css("cursor","normal");
	$("#conDiv").remove(); 
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
 		  		populateTreeSelect();
 		  		var hideAccommodation = $("#supportAccommodations").val();
 		  		if(hideAccommodation == "false")
 		  			populateGridWithoutAccommodation();
 		  		else
					populateGrid();
				
				}
			else
				gridReload();
		});
	   
}


	
function createMultiNodeSelectedTree(jsondata) {

	$("#innerID").bind("loaded.jstree", function (event, data) {  

     //   $('#innerID').jstree("check_node", "#118638"); 
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
	         	 
			"core" : {
					"animation" : 200
				},
					        
	 		"plugins" : [ "themes", "json_data", "checkbox", "ui"]
	   })
	   	   
	   .bind("change_state", function (e, data) { 
			// `data.rslt.obj` is the jquery extended node that was clicked
			//alert("change_state.................");
		})
		    
    	$("#innerID li").not(".jstree-leaf").each(function() {
    			var orgcategorylevel = $(this).attr("categoryid");
    			if(orgcategorylevel != leafNodeCategoryId) {
	    		  $("a ins.jstree-checkbox", this).first().hide();
	    		  }
	  	});
    	
 		$("#innerID").delegate("li","click", function(e) {
	 		$("#innerID li").not(".jstree-leaf").each(function() {
    			var orgcategorylevel = $(this).attr("categoryid");
    			if(orgcategorylevel != leafNodeCategoryId) {
	    		  $("a ins.jstree-checkbox", this).first().hide();
	    		  }
	  		});
	  		
		 	var currentlySelectedNode ="";
			assignedOrgNodeIds = "";
			$("#innerID").find(".jstree-checked").each(function(i, element){
				
				var orgcategorylevel = $(element).attr("categoryid");
				if(orgcategorylevel == leafNodeCategoryId) {
					if(currentlySelectedNode=="") {
						currentlySelectedNode = $(element).text();
					} else {
						currentlySelectedNode = currentlySelectedNode + " , " + $(element).text(); 
					}
	
		    		if(assignedOrgNodeIds=="") {
						assignedOrgNodeIds = $(element).attr("id");
					} else {
						assignedOrgNodeIds = $(element).attr("id") +"," + assignedOrgNodeIds; 
					}
	    		}
				
			});
				if(currentlySelectedNode.length > 0 ) {
					$("#notSelectedOrgNodes").css("visibility","hidden");
					$("#selectedOrgNodesName").text(currentlySelectedNode);	
				} else {
					$("#notSelectedOrgNodes").css("visibility","visible");
					$("#selectedOrgNodesName").text("");	
				}
		
		
		});
   
}
		function populateTreeSelect() {
			$("#notSelectedOrgNodes").css("visibility","visible");
			$("#selectedOrgNodesName").text("");	
			createMultiNodeSelectedTree (orgTreeHierarchy);
			
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
	var studentIdTitle = $("#studentIdLabelName").val();
         $("#list2").jqGrid({         
          url:'getStudentForSelectedOrgNodeGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(), 
		 type:   'POST',
	     datatype: "json",         
          colNames:['Last Name','First Name', 'Middle Name', 'Grade','Organization', 'Gender', 'Accommodation', 'Login ID', studentIdTitle],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:100, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:100, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:100, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:70, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr', width:110, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'gender',index:'gender', width:80, editable: true, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations', width:150, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userName',index:'userName',editable: true, width:150, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentNumber',index:'studentNumber',editable: true, width:100, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",records: function(obj) { return obj.studentProfileInformation.length; } },
		   	 
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			//height: "500px",  
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
	
	
	function populateGridWithoutAccommodation() {
	var studentIdTitle = $("#studentIdLabelName").val();
         $("#list2").jqGrid({         
          url:'getStudentForSelectedOrgNodeGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(), 
		 type:   'POST',
	     datatype: "json",         
          colNames:['Last Name','First Name', 'Middle Name', 'Grade','Organization', 'Gender', 'Login ID', studentIdTitle],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:100, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:100, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:100, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:70, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr', width:150, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'gender',index:'gender', width:80, editable: true, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userName',index:'userName',editable: true, width:200, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentNumber',index:'studentNumber',editable: true, width:120, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: space:nowrap;' } }
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",records: function(obj) { return obj.studentProfileInformation.length; } },
		   	 
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			//height: "500px",  
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
	


function AddStudentDetail(){

document.getElementById('displayMessage').style.display = "none";	
document.getElementById('displayMessageMain').style.display = "none";	
	if(!(gradeOptions.length > 0 
		&& genderOptions.length > 0
			&& dayOptions.length > 0
				&& monthOptions.length > 0 
					&& yearOptions.length > 0)){
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						
						$.blockUI({ message: null }); 
					},
		url:		'getOptionList.do?isLasLinkCustomer='+$("#isLasLinkCustomer").val(), 
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						//alert('in');
						$.unblockUI();
						overlayblockUI(); 
						gradeOptions = data.gradeOptions;
						genderOptions = data.genderOptions;
						dayOptions = data.dayOptions; 
						monthOptions = data.monthOptions;
						yearOptions = data.yearOptions; 
						testPurposeOptions = data.testPurposeOptions;
						fillDropDown("gradeOptions", gradeOptions);
						fillDropDown("genderOptions", genderOptions);
						fillDropDown("dayOptions", dayOptions);
						fillDropDown("monthOptions", monthOptions);
						fillDropDown("yearOptions", yearOptions);
						if($("#isLasLinkCustomer").val() =="true")
							fillDropDown("testPurposeOptions", testPurposeOptions);
						customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
						// $('#accordion').accordion('activate','Student_Information_div');
						$("#addEditStudentDetail").dialog({  
													title:"Add Record",  
												 	resizable:false,
												 	autoOpen: true,
												 	width: '800px',
												 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		 	
												 	});	
							 $("#Student_Information").css("height",'auto');
							 $("#Student_Additional_Information").css("height",'auto');
							 		
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					},
		complete :  function(){
						 $.unblockUI();  
					}
	});

	} else {
		reset();
		if($("#isLasLinkCustomer").val() =="true")
			fillDropDown("testPurposeOptions", testPurposeOptions);
		overlayblockUI(); 
		$("#addEditStudentDetail").dialog({  
			title:"Add Record",  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '800px',
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
		 	$("#Student_Information").css("height",'auto');
			$("#Student_Additional_Information").css("height",'auto');
	}	
	
}
    function reset() {
       jQuery.each(customerDemographicValue, function(i, field){         
      		$("#"+field.name).val(field.value);
       });  
       var checkBoxfields = $(":checkbox"); 
       for(var i=0; i < checkBoxfields.length; i++){
			$(checkBoxfields).eq(i).attr('checked', false); 
		} 
		var radiofields = $(":radio"); 
       for (var i=0; i<radiofields.length; i++) {
			if (radiofields[i].value== "None") { 
				radiofields[i].checked = true;
			}
		}
   }


			

function fillDropDown( elementId, optionList) {
	var ddl = document.getElementById(elementId);
	//var theOption = new Option;
	var optionHtml = "";
	for(var i = 0; i < optionList.length; i++) {
		optionHtml += "<option  value='"+optionList[i]+"'>"+optionList[i]+"</option>";	
	}
	$(ddl).html(optionHtml);
	
}



	function closePopUp(dailogId){
		$('#accordion').accordion('activate', 0 );
		$("#"+dailogId).dialog("close");
		overlayunblockUI();
		populateTreeSelect();
	}
	
	function closeConfirmationPopup() {
		closePopUp('confirmationPopup');
		closePopUp('addEditStudentDetail');
	}
	
	function onCancel() {
		
			isValueChanged = false;
		if($("#studentFirstName").val()!= "" 
			|| $("#studentMiddleName").val()!= ""
			|| $("#studentLastName").val()!= ""
			|| $("#studentExternalId").val()!= ""
			|| $("#studentExternalId2").val()!= ""
			|| $("#genderOptions").val() != "Select a gender"
			|| $("#gradeOptions").val() != "Select a grade" ) {
			isValueChanged = true;	
			}
		if(!isValueChanged) {	
			var radiofields = $(":radio"); 
	       	for (var i=0; i<radiofields.length; i++) {
				if (radiofields[i].value != "None" && radiofields[i].checked == true) { 
					isValueChanged = true;
					break;
				}
			}
		}
		/*if(!isValueChanged) {
			var selectfields = $(":select");
			for (var i=0; i<selectfields.length; i++) {
				if (selectfields[i].value != "Select a grade" && selectfields[i].value != "Select a gender" && selectfields[i].value != "Please Select") { 
					isValueChanged = true;
					break;
				}
			}
		}*/
		if(!isValueChanged) {
		   var checkBoxfields = $(":checkbox"); 
	       for(var i=0; i < checkBoxfields.length; i++){
	       		if($(checkBoxfields).eq(i).attr('checked')== true) {
					isValueChanged = true;
					break;
					}
			} 
		}
		
		if(isValueChanged) {
		overlayblockUI();
		$("#confirmationPopup").dialog({  
			title:"Confirmation Alert",  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
			 $("#confirmationPopup").css('height',100);
		} else {
			closePopUp('addEditStudentDetail');
		}
	}
	
	function studentDetailSubmit(){
	 var validflag = VerifyStudentDetail(assignedOrgNodeIds);
	if(validflag) {
					$.ajax(
						{
								async:		true,
								beforeSend:	function(){
											
												
												blockUI();
												//alert('before send....');
											},
								url:		'saveAddEditStudent.do',
								type:		'POST',
								data:		$("#addEditStudentDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds+ "&studentIdLabelName=" + $("#studentIdLabelName").val()+ "&studentIdConfigurable=" + $("#isStudentIdConfigurable").val(),
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
												unblockUI();
												var errorFlag = data.errorFlag;
												var successFlag = data.successFlag;
												if(successFlag) {
													closePopUp('addEditStudentDetail');
													setMessageMain(data.title, data.content, data.type, "");
													document.getElementById('displayMessageMain').style.display = "block";	
													
        										}
        										else{
        											setMessage(data.title, data.content, data.type, "");
        											document.getElementById('displayMessage').style.display = "block";	
        											
        										}
																								
											},
								error  :    function(XMLHttpRequest, textStatus, errorThrown){
												window.location.href="/TestSessionInfoWeb/logout.do";
											},
								complete :  function(){
												unblockUI();
											}
						}
					);
	
	} else {
			document.getElementById('displayMessage').style.display = "block";
	}
	}
	
	
	
	
	
	
	
	
	
	






			
		



