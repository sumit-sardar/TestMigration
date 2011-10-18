
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
var customerDemographicValue = "";
var isValueChanged = false;
var leafNodeCategoryId;
var SelectedOrgNodeId;
var SelectedOrgNode;
var SelectedOrgNodes = [];
var optionList = [];
var studentList;
var isAddStudent = true;
var stuDemographic ;
var stuAccommodation;
var loginId = "";
var profileEditable = "true";
var isPopUp = false;
var dbStudentDetails;
var organizationNodes = [];
						


$(document).bind('keydown', function(event) {
		
	      var code = (event.keyCode ? event.keyCode : event.which);
	      if(code == 27){
	      		if(isPopUp){				
	      			onCancel();
	      		}
	            return false;
	      }
	  });


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
	    	//SelectedOrgNode = $(this).parent();
	    	//SelectedOrgNodes = SelectedOrgNode.parentsUntil(".jstree","li");
  			SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#treeOrgNodeId").val(SelectedOrgNodeId);
 		     UIBlock();
 		  	if(!gridloaded) {
 		  		gridloaded = true;
 		  		populateTreeSelect();
 		  		var hideAccommodation = $("#supportAccommodations").val();
 		  		if(customerDemographicValue == "")
 		  			customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
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

	
	
 	$("#innerID").jstree({
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

   		
	  	if($("#innerID ul").length>0){
		 	//jQuery.jstree._reference("#innerID").destroy();
		 	//openTreeNodes();
	 	}
    	hideCheckBox();
    	
 		$("#innerID").delegate("li","click", function(e) {
 			$(this).find("li").each(function(i, element) {
    			var orgcategorylevel = $(element).attr("categoryid");
    			if(orgcategorylevel != leafNodeCategoryId) {
	    		  $("a ins.jstree-checkbox", this).first().hide();
	    		  }
	  		});
	  		
	  	if($(this).attr("categoryid") == leafNodeCategoryId){
		 	var currentlySelectedNode ="";
			assignedOrgNodeIds = "";
			//var optionHtml = "";
			$("#innerID").find(".jstree-checked").each(function(i, element){
				
				var orgcategorylevel = $(element).attr("categoryid");
				if(orgcategorylevel == leafNodeCategoryId) {
					if(currentlySelectedNode=="") {
						//currentlySelectedNode = $(element).text();
						currentlySelectedNode += "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ $(element).text()+"</a>";	
					} else {
						//currentlySelectedNode = currentlySelectedNode + " , " + $(element).text(); 
						currentlySelectedNode = currentlySelectedNode + " , " + "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ $(element).text()+"</a>";
					}
	
		    		if(assignedOrgNodeIds=="") {
						assignedOrgNodeIds = $(element).attr("id");
					} else {
						assignedOrgNodeIds = $(element).attr("id") +"," + assignedOrgNodeIds; 
					}
	    		}
			});
				if(currentlySelectedNode.length > 0 ) {
					$("#notSelectedOrgNodes").css("display","none");
					$("#selectedOrgNodesName").html(currentlySelectedNode);
					//$("#selectedOrgNodesName").text(currentlySelectedNode);	
				} else {
					$("#notSelectedOrgNodes").css("display","inline");
					$("#selectedOrgNodesName").text("");	
				}
		
			}
		});
		
}
		function populateTreeSelect() {
			$("#notSelectedOrgNodes").css("display","inline");
			$("#selectedOrgNodesName").text("");	
			createMultiNodeSelectedTree (orgTreeHierarchy);
			
		}




	
	  function gridReload(){ 
           jQuery("#list2").jqGrid('setGridParam',{datatype:'json'});
           jQuery("#list2").jqGrid('setGridParam', {url:'getStudentForSelectedOrgNodeGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(),page:1}).trigger("reloadGrid");
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
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",
		   	records: function(obj) { 
		   	studentList = JSON.stringify(obj.studentProfileInformation);
		   	idarray = obj.studentIdArray.split(",");
		   	return obj.studentProfileInformation.length; } },
		   	 
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
			ondblClickRow: function(rowid) {viewEditStudentPopup();},
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
			//jQuery("#list2").jqGrid('navGrid','#pager2',{});  
			jQuery("#list2").navGrid('#pager2', {
				addfunc: function() {
		    		AddStudentDetail();
		    	},
		    	editfunc: function() {
		    		 viewEditStudentPopup();
		    	}
			});
			
			
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
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",
		   	records: function(obj) { 
		   	studentList = JSON.stringify(obj.studentProfileInformation);
		   	idarray = obj.studentIdArray.split(",");
		   	return obj.studentProfileInformation.length; } },
		   	 
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
			ondblClickRow: function(rowid) {viewEditStudentPopup();},
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
			//jQuery("#list2").jqGrid('navGrid','#pager2',{});  
			jQuery("#list2").navGrid('#pager2', {
				addfunc: function() {
		    		AddStudentDetail();
		    	},
		    	editfunc: function() {
		    		 viewEditStudentPopup();
		    	}
			});
	}
	


function AddStudentDetail(){
isAddStudent = true;
isPopUp = true;
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
						//customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
						
						$("#addEditStudentDetail").dialog({  
													title:"Add Student",  
												 	resizable:false,
												 	autoOpen: true,
												 	width: '800px',
												 	modal: true,
												 	closeOnEscape: false,
												 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		 											});	
						$('#addEditStudentDetail').bind('keydown', function(event) {
							
 							var code = (event.keyCode ? event.keyCode : event.which);
 							if(code == 27){
		  				  	onCancel();
		  				 	return false;
		 				 }
		 				
						});
						setPopupPosition(isAddStudent);	
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
		
	});

	} else {
		$('#Student_Information :checkbox').attr('disabled', false); 
		$('#Student_Information :radio').attr('disabled', false); 
		$('#Student_Information select').attr('disabled', false);
		$('#Student_Information :input').attr('disabled', false);
		
		$("#studentFirstName").val(""); 
		$("#studentMiddleName").val(""); 
		$("#studentLastName").val(""); 
		$("#studentExternalId").val(""); 
		$("#studentExternalId2").val(""); 
						
		reset();
		if($("#isLasLinkCustomer").val() =="true")
			fillDropDown("testPurposeOptions", testPurposeOptions);
		$("#addEditStudentDetail").dialog({  
			title:"Add Student",  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '800px',
		 	modal: true,
		 	closeOnEscape: false,
		 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		 	});	
		 	$('#addEditStudentDetail').bind('keydown', function(event) {
		 						  //alert("key up 2");
 							var code = (event.keyCode ? event.keyCode : event.which);
 							if(code == 27){
		  				  	onCancel();
		  				 	return false;
		 				 }
		 				
						});
		setPopupPosition(isAddStudent);	
	}	
	
}

	function setPopupPosition(isAddStudent){
				var toppos = ($(window).height() - 610) /2 + 'px';
				var leftpos = ($(window).width() - 760) /2 + 'px';
				$("#addEditStudentDetail").parent().css("top",toppos);
				$("#addEditStudentDetail").parent().css("left",leftpos);		 	 
				$("#Student_Information").css("height",'300px');
				$("#Student_Information").css("overflow",'auto');
				$("#Student_Additional_Information").css("height",'300px');
				$("#Student_Additional_Information").css("overflow",'auto');
				$("#Student_Accommodation_Information").css("height",'300px');
				$("#Student_Accommodation_Information").css("overflow",'auto');
				if(isAddStudent) {
					$("#preButton").css("visibility","hidden");	
					$("#nextButton").css("visibility","hidden");
					$("#viewPreButton").css("visibility","hidden");	
					$("#viewNextButton").css("visibility","hidden");
				} else {
					$("#preButton").css("visibility","visible");	
					$("#nextButton").css("visibility","visible");
					$("#viewPreButton").css("visibility","visible");	
					$("#viewNextButton").css("visibility","visible");
					
				}
	}
	
    function reset() {
    	assignedOrgNodeIds = "";
    	
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
		enableAudioFiles();
		
   }


			

function fillDropDown( elementId, optionList) {
	var ddl = document.getElementById(elementId);
	var optionHtml = "";
	for(var i = 0; i < optionList.length; i++) {
		optionHtml += "<option  value='"+optionList[i]+"'>"+optionList[i]+"</option>";	
	}
	$(ddl).html(optionHtml);
	
}

function fillselectedOrgNode( elementId, orgList) {
	var ddl = document.getElementById(elementId);
	var optionHtml = "";
	assignedOrgNodeIds = "";
	for(var i = 0; i < orgList.length; i++) {
		if(assignedOrgNodeIds == "") {
			assignedOrgNodeIds = orgList[i].orgNodeId;
		} else {
			assignedOrgNodeIds = assignedOrgNodeIds + " , " + orgList[i].orgNodeId; 
		}
		optionHtml += "<a style='color: blue;text-decoration:underline'  href=javascript:openTreeNodes('"+orgList[i].orgNodeId+"');>"+orgList[i].orgNodeName+"</a>";	
	}
	$(ddl).html(optionHtml);
	
	if(orgList.length > 0 ) {
		$("#notSelectedOrgNodes").css("display","none");
	} else {
		$("#notSelectedOrgNodes").css("display","inline");
		$("#selectedOrgNodesName").text("");	
	}
}

	function closePopUp(dailogId){
		if(dailogId == 'addEditStudentDetail') {
			$('#accordion').accordion('activate', 0 );
			$("#Student_Information").scrollTop(0);
			$("#Student_Additional_Information").scrollTop(0);
			$("#Student_Accommodation_Information").scrollTop(0);
			$('#Student_Additional_Information').hide();
			$('#Student_Accommodation_Information').hide();
			populateTreeSelect();
			isPopUp = false;
		}
		
		if(dailogId == 'viewStudentDetail') {
			$('#viewaccordion').accordion('activate', 0 );
			$("#view_Student_Information").scrollTop(0);
			$("#view_Student_Additional_Information").scrollTop(0);
			$("#view_Student_Accommodation_Information").scrollTop(0);
			$('#view_Student_Additional_Information').hide();
			$('#view_Student_Accommodation_Information').hide();
			populateTreeSelect();
			isPopUp = false;
		}
			
		if(dailogId == 'confirmationPopup') {
			$('#accordion').accordion('activate', 0 );
			$("#Student_Information").scrollTop(0);
			$("#Student_Additional_Information").scrollTop(0);
			$("#Student_Accommodation_Information").scrollTop(0);
			$('#Student_Additional_Information').hide();
			$('#Student_Accommodation_Information').hide();
			$('#studentFirstName').trigger("focus");		
		}
		$("#"+dailogId).dialog("close");
		 
	}
	
	function closeConfirmationPopup() {
		closePopUp('confirmationPopup');
		closePopUp('addEditStudentDetail');
	}
	
	function onCancel() {
		
			isValueChanged = false;
	if(isAddStudent){
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
			 var radiofields = $("#addEditStudentDetail :radio"); 
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
		   var checkBoxfields = $("#addEditStudentDetail :checkbox"); 
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
			 var toppos = ($(window).height() - 290) /2 + 'px';
			 var leftpos = ($(window).width() - 410) /2 + 'px';
			 $("#confirmationPopup").parent().css("top",toppos);
			 $("#confirmationPopup").parent().css("left",leftpos);		 	 
				
			  
		} else {
			closePopUp('addEditStudentDetail');
		}
	
	} else {
		
		var newStudentValue = $("#addEditStudentDetail *").serializeArray(); 
		isValueChanged = false;	
		 if(!isValueChanged) {
		      for(var key = 0; key <dbStudentDetails.length ; key++) {
		       if(newStudentValue[key].value != dbStudentDetails[key].value){
		      		isValueChanged = true;
		      	}
		      	 
		      }
	      }
	       if(!isValueChanged) {
		       OrgNodeIds = ""
		        for ( var i= 0; i<organizationNodes.length;i++) {
		         if(OrgNodeIds == "") {
		      		OrgNodeIds = organizationNodes[i].orgNodeId; 
		      		} else {
		      		OrgNodeIds = OrgNodeIds + " , " + organizationNodes[i].orgNodeId; 
		      		}
		      	}
		      	if(OrgNodeIds != assignedOrgNodeIds) {
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
				 var toppos = ($(window).height() - 290) /2 + 'px';
				 var leftpos = ($(window).width() - 410) /2 + 'px';
				 $("#confirmationPopup").parent().css("top",toppos);
				 $("#confirmationPopup").parent().css("left",leftpos);		 	 
					
				  
			} else {
				closePopUp('addEditStudentDetail');
			}
			
			
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
	
	var param;
	var createBy = "";
	if(isAddStudent){
		param = $("#addEditStudentDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds+ "&studentIdLabelName=" +
		 $("#studentIdLabelName").val()+ "&studentIdConfigurable=" + $("#isStudentIdConfigurable").val() + 
		 "&isAddStudent=" + isAddStudent +"&createBy="+createBy ;
	}else{
		var selectedStudentId = $("#list2").jqGrid('getGridParam', 'selrow');
		createBy = getDataFromJson(selectedStudentId);
		param = $("#addEditStudentDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds+ "&studentIdLabelName=" +
			 $("#studentIdLabelName").val()+ "&studentIdConfigurable=" + $("#isStudentIdConfigurable").val()+
			  "&selectedStudentId=" + selectedStudentId + "&isAddStudent=" + isAddStudent + "&createBy="+createBy + "&loginId=" + loginId ;
	}
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
								data:		 param,
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
	
	
	
	
	function openTreeNodes(orgNodeId) {
		var parentOrgNodeId = $("#" + orgNodeId).parent();
		
		var ancestorNodes = parentOrgNodeId.parentsUntil(".jstree","li");
		//open tree nodes from root to the clicked node	
		if(ancestorNodes.length > 0) {
			for(var count = ancestorNodes.length - 1; count >= 0; --count) {
	  		 		var tmpNode = ancestorNodes[count].id;
					$('#innerID').jstree('open_node', "#"+tmpNode); 
				
	  		 }
	  		 //$('#innerID').jstree('check_node', "#"+orgNodeId); 
  		 }
  		  $('#innerID').jstree('check_node', "#"+orgNodeId); 
  		 hideCheckBox();
	}
	
	function hideCheckBox(){
		$("#innerID li").not(".jstree-le").each(function() {
    			var orgcategorylevel = $(this).attr("categoryid");
    			if(orgcategorylevel != leafNodeCategoryId) {
	    		  $("a ins.jstree-checkbox", this).first().hide();
	    		  }
	  	});
	
	}
	
	function disablenextprev(selectedPosition,maxlength) {
                    selectedPosition == 0 ? $("#pData").addClass("ui-state-disabled") : $("#pData").removeClass("ui-state-disabled");
                    selectedPosition == maxlength? $("#nData").addClass("ui-state-disabled") : $("#nData").removeClass("ui-state-disabled");
                    selectedPosition == 0 ? $("#viewpData").addClass("ui-state-disabled") : $("#viewpData").removeClass("ui-state-disabled");
                    selectedPosition == maxlength? $("#viewnData").addClass("ui-state-disabled") : $("#viewnData").removeClass("ui-state-disabled");
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

	function editStudentDetail(SelectedStudentId){
	isPopUp = true;
	var rowid;
	isAddStudent = false;
	document.getElementById('displayMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";	
	if(SelectedStudentId == undefined){
		rowid = $("#list2").jqGrid('getGridParam', 'selrow');
	} else {
		rowid = SelectedStudentId;
	}
	
    var createBy =  getDataFromJson(rowid);
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'getStudentDataForSelectedStudent.do?&studentID='+rowid +'&createBy='+createBy , 
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI();
						gradeOptions = data.optionList.gradeOptions;
						genderOptions = data.optionList.genderOptions;
						dayOptions = data.optionList.dayOptions; 
						monthOptions = data.optionList.monthOptions;
						yearOptions = data.optionList.yearOptions; 
						testPurposeOptions = data.optionList.testPurposeOptions;
						loginId = data.userName;
						fillDropDown("gradeOptions", gradeOptions);
						fillDropDown("genderOptions", genderOptions);
						fillDropDown("dayOptions", dayOptions);
						fillDropDown("monthOptions", monthOptions);
						fillDropDown("yearOptions", yearOptions);
						if($("#isLasLinkCustomer").val() =="true")
							fillDropDown("testPurposeOptions", testPurposeOptions);
							
						$("#studentFirstName").val(data.firstName);
						$("#studentMiddleName").val(data.middleName);
						$("#studentLastName").val(data.lastName);
						$("#monthOptions").val(data.month);
						$("#dayOptions").val(data.day);
						$("#yearOptions").val(data.year);
						$("#genderOptions").val(data.gender);
						$("#gradeOptions").val(data.grade);
						$("#studentExternalId").val(data.studentNumber);
						$("#studentExternalId2").val(data.studentSecondNumber);
						if($("#isLasLinkCustomer").val() == "true")
							$("#testPurposeOptions").val(data.testPurpose);
							
						profileEditable = data.profileEditable;
						if(profileEditable == "false") {
							$('#Student_Information :checkbox').attr('disabled', true); 
							$('#Student_Information :radio').attr('disabled', true); 
							$('#Student_Information select').attr('disabled', true);
							$('#Student_Information :input').attr('disabled', true);
							 
						}
							
						stuDemographic = data.stuDemographic;
						stuAccommodation = data.stuAccommodation;
						organizationNodes = data.organizationNodes;
						fillselectedOrgNode("selectedOrgNodesName", organizationNodes);
						setEditStudentDetail();
						
						$.unblockUI();  
						$("#addEditStudentDetail").dialog({  
													title:"Edit Student",  
												 	resizable:false,
												 	autoOpen: true,
												 	width: '800px',
												 	modal: true,
												 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		 	
												 	});	
												 	 
							 setPopupPosition(isAddStudent);
						dbStudentDetails = 	$("#addEditStudentDetail *").serializeArray();  
						
							 		
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
		
	});
	
}

	function viewEditStudentPopup(){
		var editStudentPermission = $("#showEditButton").val();
		//var editStudentPermission = "false";
		if(editStudentPermission == "true"){
			editStudentDetail();	
		} else {
			viewStuDetail();		
		}
	}
	
	
	
	function viewStuDetail(SelectedStudentId){
	var rowid;
	isPopUp = true;
	isAddStudent = false;
	document.getElementById('displayMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";	
	if(SelectedStudentId == undefined){
		rowid = $("#list2").jqGrid('getGridParam', 'selrow');
	} else {
		rowid = SelectedStudentId;
	}
	var createBy =  getDataFromJson(rowid);
    
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'getViewStudentData.do?&studentID='+rowid +'&createBy='+createBy , 
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI();
						$("#studentFirstNameView").text(data.firstName);
						$("#studentMiddleNameView").text(data.middleName);
						$("#studentLastNameView").text(data.lastName);
						$("#studentUserNameView").text(data.userName);
						$("#birthdateStringView").text(data.birthdateString);
						$("#studentgradeView").text(data.grade);
						$("#studentgenderView").text(data.gender);
						$("#studentNumberView").text(data.studentNumber);
						$("#studenttestPurposeView").text(data.testPurpose);
						$("#orgNodeNameView").text(data.orgNodeNamesStr);
						stuDemographic = data.stuDemographic;
						stuAccommodation = data.stuAccommodation;
						setViewStudentDetail();
						//disableFields('viewStudentDetail', true);
						$('#viewStudentDetail :checkbox').attr('disabled', true); 
						$('#viewStudentDetail :radio').attr('disabled', true); 
						$('#viewStudentDetail select').attr('disabled', true); 
						$.unblockUI();  
						//customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
						$("#viewStudentDetail").dialog({  
													title:"View Student",  
												 	resizable:false,
												 	autoOpen: true,
												 	width: '800px',
												 	modal: true,
												 	closeOnEscape: false,
												 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		 	
												 	});	
												 	 
							 setPopupPosition(isAddStudent);
							 
							
							 		
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
		
	});
	
}
		
		/*disableFields(dailogId, flag){
			$("#"+dailogId + ' :checkbox').attr('disabled', flag); 
			$("#"+dailogId + ' :radio').attr('disabled', flag); 
			$("#"+dailogId + ' :select').attr('disabled', flag); 
		}*/
	
				function nDataClick(popupname) {
				var SelectedStudentId = $("#list2").jqGrid('getGridParam', 'selrow');
                  	var str = idarray;
					var nextStudentId ;
					var indexOfId = str.indexOf(SelectedStudentId);
					if(indexOfId != str.length-1) {
						nextStudentId = str[indexOfId + 1];
						highlightnextprev(SelectedStudentId, nextStudentId);
		       			if(popupname == 'Edit')
		       				editStudentDetail(nextStudentId);
		       			else
		       				viewStuDetail(nextStudentId);
		       			disablenextprev(indexOfId+1,str.length-1);
					}
               }
               
               function pDataClick(popupname) {
               var SelectedStudentId = $("#list2").jqGrid('getGridParam', 'selrow');
                  	var str = idarray;
					var preStudentId ;
					var indexOfId = str.indexOf(SelectedStudentId);
					if(indexOfId != 0) {
						preStudentId = str[indexOfId - 1];
						highlightnextprev(SelectedStudentId, preStudentId);
		       			if(popupname == 'Edit')
		       				editStudentDetail(preStudentId);
		       			else
		       				viewStuDetail(preStudentId);
		       			disablenextprev(indexOfId-1,str.length-1);
					}
               }
	
	
	
		function getDataFromJson(id){
			var str = studentList;
			var createBy = "";
			var indexOfId = str.indexOf(id);
			var indexOfCreatedBy = -1;
			var indexOfComma = -1;
			if(indexOfId > 0){
				str = str.substring(parseInt(indexOfId), str.length);
				//createBy
				indexOfCreatedBy = str.indexOf("createBy");
				indexOfComma = str.indexOf(',', parseInt(indexOfCreatedBy));
				indexOfCreatedBy += 10;
				createBy = str.substring(parseInt(indexOfCreatedBy), parseInt(indexOfComma));
				createBy = createBy.trim();
			}else{
				
			}
			return createBy;
	}
	
	
	
	function setViewStudentDetail() {
    	assignedOrgNodeIds = "";
    	var SelectedStudentId = $("#list2").jqGrid('getGridParam', 'selrow');
        var str = idarray;
		var indexOfId = str.indexOf(SelectedStudentId);
		disablenextprev(indexOfId,str.length-1);
		
      for(var key in stuAccommodation) {
		     $("#view_Student_Accommodation_Information :checkbox[name='" + key+ "']").attr('checked', stuAccommodation[key]);
		     $("#view_Student_Accommodation_Information select[name='" + key+ "']").attr("selected",stuAccommodation[key]);
		}
		
		for(var count=0; count< stuDemographic.length; count++) {
			 if(stuDemographic[count]['studentDemographicValues'].length == 1){
		     	var dynKey = stuDemographic[count]['labelName'] + "_" + stuDemographic[count]['studentDemographicValues'][0]['valueName'] ;
		     	if(trim(stuDemographic[count]['studentDemographicValues'][0]['selectedFlag']) == 'true'){
			     	$("#view_Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', true);
			     }else {
			     	$("#view_Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', false);
			     }
		     }else {
		     	for(var innerCount = 0 ; innerCount < stuDemographic[count]['studentDemographicValues'].length; innerCount++){
		     		if(trim(stuDemographic[count]['studentDemographicValues'][innerCount]['selectedFlag']) == 'true'){
		     			$("#view_Student_Additional_Information select[name='" + stuDemographic[count]['labelName']+ "']").val(stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']);			     			
		     			$("#view_Student_Additional_Information :radio[value='" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']+ "']").attr('checked',true);
		     			break;
		     		} else {
		     			$("#view_Student_Additional_Information :radio[value='None']").attr('checked',true);
		     			$("#view_Student_Additional_Information select[name='" + stuDemographic[count]['labelName']+ "']").find("option:eq(0)").attr("selected","true");
						
		     			
		     		}
		     	}
		     	for(var innerCount = 0 ; innerCount < stuDemographic[count]['studentDemographicValues'].length; innerCount++){
		     		if(trim(stuDemographic[count]['studentDemographicValues'][innerCount]['selectedFlag']) == 'true'){
		     			$("#view_Student_Additional_Information :checkbox[value='" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']+ "']").attr('checked',true);
		     			break;
		     		} else {
		     			$("#view_Student_Additional_Information :checkbox[value='" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']+ "']").attr('checked',false);
		     			
		     		}
		     	}
		     }
		}
		var radiofields = $("#view_Student_Additional_Information :radio"); 
		var flag = false;
       	for (var i=0; i<radiofields.length; i++) {
			if (radiofields[i].checked == true  && radiofields[i].value != "None") { 
				flag = true;
			}
		}
		if(!flag) {
		for (var i=0; i<radiofields.length; i++) {
			if(radiofields[i].value == "None")
				radiofields[i].checked = true;
		}
		}
   }
   
   
   function setEditStudentDetail() {
   		var SelectedStudentId = $("#list2").jqGrid('getGridParam', 'selrow');
        var str = idarray;
		var indexOfId = str.indexOf(SelectedStudentId);
		disablenextprev(indexOfId,str.length-1);
					
    	for(var key in stuAccommodation) {
		     $("#Student_Accommodation_Information :checkbox[name='" + key+ "']").attr('checked', stuAccommodation[key]);
		     $("#Student_Accommodation_Information select[name='" + key+ "']").attr("selected",stuAccommodation[key]);
		}
		
		for(var count=0; count< stuDemographic.length; count++) {
			 if(stuDemographic[count]['studentDemographicValues'].length == 1){
		     	var dynKey = stuDemographic[count]['labelName'] + "_" + stuDemographic[count]['studentDemographicValues'][0]['valueName'] ;
		     	if(trim(stuDemographic[count]['studentDemographicValues'][0]['selectedFlag']) == 'true'){
			     	$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', true);
			     }else {
			     	$("#Student_Additional_Information :checkbox[name='" + dynKey+ "']").attr('checked', false);
			     }
		     }else {
		     	for(var innerCount = 0 ; innerCount < stuDemographic[count]['studentDemographicValues'].length; innerCount++){
		     		if(trim(stuDemographic[count]['studentDemographicValues'][innerCount]['selectedFlag']) == 'true'){
		     			$("#Student_Additional_Information select[name='" + stuDemographic[count]['labelName']+ "']").val(stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']);			     			
		     			$("#Student_Additional_Information :radio[value='" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']+ "']").attr('checked',true);
		     			break;
		     		} else {
		     			$("#Student_Additional_Information :radio[value='None']").attr('checked',true);
		     			$("#Student_Additional_Information select[name='" + stuDemographic[count]['labelName']+ "']").find("option:eq(0)").attr("selected","true");
						
		     			
		     		}
		     	}
		     	for(var innerCount = 0 ; innerCount < stuDemographic[count]['studentDemographicValues'].length; innerCount++){
		     		if(trim(stuDemographic[count]['studentDemographicValues'][innerCount]['selectedFlag']) == 'true'){
		     			$("#Student_Additional_Information :checkbox[value='" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']+ "']").attr('checked',true);
		     			break;
		     		} else {
		     			$("#Student_Additional_Information :checkbox[value='" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName']+ "']").attr('checked',false);
		     			
		     		}
		     	}
		     }
		}
		
		
		
   }



			
		



