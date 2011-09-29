
var gridjsondata;
var idarray =[];
var gridloaded = false;

var gradeOptions = []; 
var genderOptions = []; 
var dayOptions = []; 
var yearOptions = []; 
var monthOptions = []; 
var orgTreeHierarchy;
var assignedOrgNodeIds ;
var customerDemographicValue;
var isValueChanged = false;

			
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
						orgTreeHierarchy = data;
						createSingleNodeSelectedTree (data);
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
        
			"plugins" : [ "themes", "json_data", "checkbox"]
		
			
    });
 		 $("#innerID").delegate("a","click", function(e) {
		 	var checked_ids = [];
			var currentlySelectedNode ="";
			assignedOrgNodeIds = "";
			checked_ids = $("#innerID .jstree-checked a");
			for(var i= 0; i<checked_ids.length; i++){
				//alert(checked_ids[i].attr("id"));
				currentlySelectedNode =  currentlySelectedNode + " , " + checked_ids[i].text ;
				//assignedOrgNodeIds = checked_ids[i].attr("id") +"," + assignedOrgNodeIds;
				$("#selectedOrgNodesName").text(currentlySelectedNode);
				if(currentlySelectedNode.length > 0 )
					$("#notSelectedOrgNodes").css("visibility","hidden");	
				else
					$("#notSelectedOrgNodes").css("visibility","visible");	
			}
		});
   
}
		function populateTreeSelect() {
		
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

         $("#list2").jqGrid({         
          url:'getStudentForSelectedOrgNodeGrid.do?q=2&treeOrgNodeId='+$("#treeOrgNodeId").val(), 
		 type:   'POST',
	     datatype: "json",         
          colNames:['Student Name','Organization', 'Login ID','Grade', 'Student ID'],
		   	colModel:[
		   	
		   		{name:'displayName',index:'displayName', width:230, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeNamesStr',index:'orgNodeNamesStr', width:150, editable: true, align:"left", sortable:false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userName',index:'userName',editable: true, width:200, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade',editable: true, width:70, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentNumber',index:'studentNumber',editable: true, width:150, align:"left", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	
		   	],
		   	jsonReader: { repeatitems : false, root:"studentProfileInformation", id:"studentId",records: function(obj) { return obj.studentProfileInformation.length; } },
		   	 
			rowNum:10,
			loadonce:true, 
			multiselect:false,
			pager: '#pager2', 
			sortname: 'displayName', 
			viewrecords: true, 
			sortorder: "asc",
			height: "100%",  
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
		url:		'getOptionList.do',
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
						fillDropDown("gradeOptions", gradeOptions);
						fillDropDown("genderOptions", genderOptions);
						fillDropDown("dayOptions", dayOptions);
						fillDropDown("monthOptions", monthOptions);
						fillDropDown("yearOptions", yearOptions);
						customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
						$("#addEditStudentDetail").dialog({  
													title:"Add Record",  
												 	resizable:false,
												 	autoOpen: true,
												 	width: '800px',
												 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		 	
												 	});	
						$('#accordion ul:eq(0)').show(); 						
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
		overlayblockUI(); 
		$("#addEditStudentDetail").dialog({  
			title:"Add Record",  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '800px',
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
		 	
		 	
		$('#accordion ul:eq(0)').show(); 		
	}	
	//$('#addEditStudentDetail .ui-dialog-titlebar-close').hide();
	
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
		$("#"+dailogId).dialog("close");
		overlayunblockUI();
	}
	
	function closeConfirmationPopup() {
		closePopUp('confirmationPopup');
		closePopUp('addEditStudentDetail');
	}
	
	function onCancel() {
		isValueChanged = false;
		var radiofields = $(":radio"); 
       	for (var i=0; i<radiofields.length; i++) {
			if (radiofields[i].value != "None" && radiofields[i].checked == true) { 
				isValueChanged = true;
				break;
			}
		}
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
	 var validflag = VerifyStudentDetail();
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
								data:		$("#addEditStudentDetail *").serialize(),
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
												//alert("data : " + data);
												unblockUI();
												var errorFlag = data.errorFlag;
												var successFlag = data.successFlag;
												if(successFlag) {
													document.getElementById('displayMessage').innerHTML = "Student Added Successfully";
        											document.getElementById('displayMessage').style.display = "block";	
        										}
        										else{
        											document.getElementById('displayMessage').innerHTML = "Student Not Added ";
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
	
	
	
	
	
	
	






			
		



