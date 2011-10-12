
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
var SelectedOrgNodeId;
var SelectedOrgNode;
var SelectedOrgNodes = [];




function UIBlock(){
	$.blockUI({ message: '<img src="/StudentManagementWeb/resources/images/loading.gif" />',css: {border: '0px',backgroundColor: '#aaaaaa', opacity:  0.5, width:'45px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 
}


			
function populateTree() {
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
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


function overlayblockUI(){	
	$("body").append('<div id="blDiv" style="opacity: 0.6; background-color: #d0e5f5;position: absolute;top:0;left:0;width:100%;height:100%;z-index:10"></div>');
	$("#blDiv").css("cursor","wait");		
						
}

function overlayunblockUI(){
	$("#blDiv").css("cursor","normal");
	$("#blDiv").remove(); 
}



function createSingleNodeSelectedTree(jsondata) {
	   $("#orgNodeHierarchy").jstree({
	        "json_data" : {	             
	            "data" : jsondata.data,
				"progressive_render" : true,
				"progressive_unload" : true
	        },
            "themes" : {
			    "theme" : "apple",
			    "dots" : false,
			    "icons" : true
			},       
	        "ui" : {  
	           "select_limit" : 1
         	}, 
				"plugins" : [ "themes", "json_data", "ui"]  
				
	    });
	    
	    $("#orgNodeHierarchy").delegate("a","click", function(e) {
	    	SelectedOrgNode = $(this).parent();
	    	SelectedOrgNodes = SelectedOrgNode.parentsUntil(".jstree","li");
  			SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#treeOrgNodeId").val(SelectedOrgNodeId);
 		     UIBlock();
 		  	if(!gridloaded) {
 		  		gridloaded = true;
 		  		//populateTreeSelect();
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
 			//openTreeNodes();
 	
	/*		$("#innerID li").not(".jstree-le").each(function() {
    			var orgcategorylevel = $(this).attr("categoryid");
    			if(orgcategorylevel != leafNodeCategoryId) {
	    		  $("a ins.jstree-checkbox", this).first().hide();
	    		}

	  	}); */
	  }).jstree({
	        "json_data" : {	             
	            "data" : jsondata.data,
				"progressive_render" : true,
				"progressive_unload" : true
				
	        },  
	            "themes" : {
			    "theme" : "apple",
			    "dots" : false,
			    "icons" : true
			},       
	 			"plugins" : [ "themes", "json_data", "ui", "checkbox"]
	   });	

   		$("#innerID li").not(".jstree-le").each(function() {
    			var orgcategorylevel = $(this).attr("categoryid");
    			if(orgcategorylevel != leafNodeCategoryId) {
	    		  $("a ins.jstree-checkbox", this).first().hide();
	    		  }
	  	});
	  	
	  	if($("#innerID ul").length>0){
		 	//jQuery.jstree._reference("#innerID").destroy();
		 	openTreeNodes();
	 	
			$("#innerID li").not(".jstree-le").each(function() {
	    			var orgcategorylevel = $(this).attr("categoryid");
	    			if(orgcategorylevel != leafNodeCategoryId) {
		    		  $("a ins.jstree-checkbox", this).first().hide();
		    		}
		    });		
		}
    	
 		$("#innerID").delegate("li","click", function(e) {
	 		$("#innerID li").not(".jstree-le").each(function() {
    			var orgcategorylevel = $(this).attr("categoryid");
    			if(orgcategorylevel != leafNodeCategoryId) {
	    		  $("a ins.jstree-checkbox", this).first().hide();
	    		  }
	  		});
	  		
	  	if($(this).attr("categoryid") == leafNodeCategoryId){
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
          colNames:['Last Name','First Name', 'Middle Initial', 'Grade','Organization', 'Gender', 'Accommodation', 'Login ID', studentIdTitle],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:100, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:100, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:100, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:70, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr', width:110, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'gender',index:'gender', width:80, editable: true, align:"left",sorttype:'text',sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations', width:125, editable: true, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userName',index:'userName',editable: true, width:150, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentNumber',index:'studentNumber',editable: true, width:100, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",records: function(obj) { return obj.studentProfileInformation.length; } },
		   	 
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,  
			editurl: 'getStudentForSelectedOrgNodeGrid.do',
			caption:"Student List",
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
	
	
	function populateGridWithoutAccommodation() {
	var studentIdTitle = $("#studentIdLabelName").val();
         $("#list2").jqGrid({         
          url:'getStudentForSelectedOrgNodeGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(), 
		 type:   'POST',
		 datatype: "json",         
          colNames:['Last Name','First Name', 'Middle Initial', 'Grade','Organization', 'Gender', 'Login ID', studentIdTitle],
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
		   	 
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,  
			editurl: 'getStudentForSelectedOrgNodeGrid.do',
			caption:"Student List",
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
						
						UIBlock();
						populateTreeSelect();
					},
		url:		'getOptionList.do?isLasLinkCustomer='+$("#isLasLinkCustomer").val(), 
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						//alert('in');
						$.unblockUI();
						//UIBlock();
						//overlayblockUI(); 
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
						$("#addEditStudentDetail").dialog({  
													title:"Add Student",  
												 	resizable:false,
												 	autoOpen: true,
												 	width: '800px',
												 	modal: true,
												 	open: function(event, ui) { }
		 											});	
						setPopupPosition();	
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
		
	});

	} else {
		reset();
		if($("#isLasLinkCustomer").val() =="true")
			fillDropDown("testPurposeOptions", testPurposeOptions);
		//UIBlock(); 
		//overlayblockUI(); 
		
		$("#addEditStudentDetail").dialog({  
			title:"Add Student",  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '800px',
		 	modal: true,
		 	open: function(event, ui) {  }
		 	});	
		setPopupPosition();	
	}	
	
}

	function setPopupPosition(){
				var toppos = ($(window).height() - 610) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#addEditStudentDetail").parent().css("top",toppos);
				$("#addEditStudentDetail").parent().css("left",leftpos);		 	 
				$("#Student_Information").css("height",'300px');
				$("#Student_Information").css("overflow",'auto');
				$("#Student_Additional_Information").css("height",'300px');
				$("#Student_Additional_Information").css("overflow",'auto');
				$("#Student_Additional_Information").scrollTop(0);
				$("#Student_Accommodation_Information").css("height",'300px');
				$("#Student_Accommodation_Information").css("overflow",'auto');
				$("#Student_Accommodation_Information").scrollTop(0);
	}
	
    function reset() {
    	assignedOrgNodeIds = "";
    	populateTreeSelect();
    	
       jQuery.each(customerDemographicValue, function(i, field){         
      		$("#"+field.name).val(field.value);
      		
       });  
       var customerValCheckbox = [];
	   var counter=0;
       var checkBoxfields = $(":checkbox"); 
	       for(var i=0; i < checkBoxfields.length; i++){
		       for(var j=0; j<customerDemographicValue.length; j++) {
			       	if($(checkBoxfields).eq(i).attr('name') == customerDemographicValue[j].name) {
			       			customerValCheckbox[counter] =  customerDemographicValue[j].name;
							counter++;
					}
		       	}
			} 
			if(customerValCheckbox.length > 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
			       for(var j=0; j<customerValCheckbox.length; j++) {
				       	if(!isExist( $(checkBoxfields).eq(i).attr('name') ,customerValCheckbox)) { //
				       			$(checkBoxfields).eq(i).attr('checked', false); 
				    	} else {
				    			$(checkBoxfields).eq(i).attr('checked', true); 
				    	}
			       	}
				} 
			}
			if(customerValCheckbox.length == 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
					$(checkBoxfields).eq(i).attr('checked', false); 
				}
			}
		var radiofields = $(":radio"); 
       for (var i=0; i<radiofields.length; i++) {
			if (radiofields[i].value== "None") { 
				radiofields[i].checked = true;
			}
		}
		//change for defect #67004
		var allSelects = $("#addEditStudentDetail select");
		for(var count = 0; count < allSelects.length ; count++) {
			$(allSelects[count]).find("option:eq(0)").attr("selected","true");
		}
		
		enableColorSettingsLink("false");
		
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
		if(dailogId == 'addEditStudentDetail') {
			$('#accordion').accordion('activate', 0 );
			$('#Student_Additional_Information').hide();
			$('#Student_Accommodation_Information').hide();
			//populateTreeSelect();
		}
		$("#"+dailogId).dialog("close");
		
		 
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
				if (radiofields[i].value != "None" && radiofields[i].checked == true && radiofields[i].checked != ""  && radiofields[i].disabled == false && radiofields[i].getAttribute("disabled") == null) { 
					isValueChanged = true;
					break;
				}
			}
		}
		if(!isValueChanged) {
			var allSelects = $("#addEditStudentDetail select");
			for(var count = 0; count < allSelects.length ; count++) {
				var selectedInd = $(allSelects[count]).attr("selectedIndex");
				if(selectedInd != 0 && ($(allSelects[count]).attr("disabled") == null  || $(allSelects[count]).attr("disabled") == false)) {
					isValueChanged = true;
					break;
				}
			}
		}
		if(!isValueChanged) {
		var customerValCheckbox = [];
			var counter=0;
		   var checkBoxfields = $(":checkbox"); 
	       for(var i=0; i < checkBoxfields.length; i++){
		       for(var j=0; j<customerDemographicValue.length; j++) {
			       	if($(checkBoxfields).eq(i).attr('name') == customerDemographicValue[j].name) {
			       			if($(checkBoxfields).eq(i).attr('checked')== false)
			       			{
			       				isValueChanged = true;
			       				break;
			       			}
			       			customerValCheckbox[counter] =  customerDemographicValue[j].name;
							counter++;
					}
		       	}
			} 
			if(!isValueChanged && customerValCheckbox.length > 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
			       for(var j=0; j<customerValCheckbox.length; j++) {
				       	if(!isExist( $(checkBoxfields).eq(i).attr('name') ,customerValCheckbox)) { //
				       			if($(checkBoxfields).eq(i).attr('checked')== true)
				       			{
				       				isValueChanged = true;
				       				break;
				       			}
				       			
						}
			       	}
				} 
			}
			if(!isValueChanged && customerValCheckbox.length == 0) {
				 for(var i=0; i < checkBoxfields.length; i++){
					if($(checkBoxfields).eq(i).attr('checked')== true) {
						isValueChanged = true;
						break;
					}
				}
			}
			
		}
		if(!isValueChanged){
			if(assignedOrgNodeIds != ""){
				isValueChanged = true;
			}
		}
		
		if(isValueChanged) {
		//UIBlock();
		$("#confirmationPopup").dialog({  
			title:"Confirmation Alert",  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
			 $("#confirmationPopup").css('height',100);
			  
		} else {
			closePopUp('addEditStudentDetail');
		}
	}
	
	function isExist(val, customerValCheckbox){
		for(var i=0; i < customerValCheckbox.length; i++){
			if(val == customerValCheckbox[i]){
				return true;
			}
		}
		return false;
	}
	
	
	function studentDetailSubmit(){
	 var validflag = VerifyStudentDetail(assignedOrgNodeIds);
	if(validflag) {
					$.ajax(
						{
								async:		true,
								beforeSend:	function(){
											
												
												UIBlock();
												//alert('before send....');
											},
								url:		'saveAddEditStudent.do',
								type:		'POST',
								data:		$("#addEditStudentDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds+ "&studentIdLabelName=" + $("#studentIdLabelName").val()+ "&studentIdConfigurable=" + $("#isStudentIdConfigurable").val(),
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
												$.unblockUI();  
												var errorFlag = data.errorFlag;
												var successFlag = data.successFlag;
												if(successFlag) {
													closePopUp('addEditStudentDetail');
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
	
	} else {
			document.getElementById('displayMessage').style.display = "block";
	}
	}
	
	function openTreeNodes() {
		//open tree nodes from root to the clicked node		
		for(var count = SelectedOrgNodes.length - 1; count >= 0; --count) {
  		 		var tmpNode = SelectedOrgNodes[count].id;
				$('#innerID').jstree('open_node', "#"+tmpNode); 
			
  		 }
	}
	

	
	
	
	
	
	
	
	






			
		



