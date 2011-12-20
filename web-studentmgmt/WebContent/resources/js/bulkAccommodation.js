var bulkStudentgridLoaded = false;	
var gradeOptions = ":Any;JV:JV;AD:AD"; 
var AccommOption = ":Any;T:Yes;F:No";
var allRowSelected = false;
var studentArrId = [];
var totalRowSelectedOnPage = 0;
var customerDemographicList ;

var selectedStudentObjArr = {};
var BULK_ADD_TITLE      = "Edit Accommodations";
var BULK_ACCOM_NOTSELECTED = "No accommodation was selected. Accommodations for the selected set of students was not updated.";	
	
function populateBulkAccommTree() {
	
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
						customerDemographicList = data.customerDemographicList; 
						leafNodeCategoryId = data.leafNodeCategoryId;
						var gOptionsArr = data.gradeOptions;
						
						var gOptions = ":Any;";
						for(var i=0; i<gOptionsArr.length; i++){
						
							gOptions = gOptions+gOptionsArr[i] +":"+gOptionsArr[i]+";";
						}
						 if(gOptions != ""){
						 gradeOptions = gOptions.substring(0,gOptions.length-1);
						 }
						
						orgTreeHierarchy = data;
						jsonData = orgTreeHierarchy.data;
						getRootNodeDetails();
						createSingleNodeSelectedTreeInBulk (orgTreeHierarchy);
						$("#searchheader").css("visibility","visible");	
						$("#studentBulkOrgNode").css("visibility","visible");	
												
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



function createSingleNodeSelectedTreeInBulk(jsondata) {
	   $("#studentBulkOrgNode").jstree({
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
	    
	    $("#studentBulkOrgNode").delegate("a","click", function(e) {
	    	//SelectedOrgNode = $(this).parent();
	    	//SelectedOrgNodes = SelectedOrgNode.parentsUntil(".jstree","li");
	    	//clear message before moving to pther node
	    	//clearMessage();
	    	$('#gs_grade').val('Any');
	    	$('#gs_calculator').val('Any');
	    	$('#gs_hasColorFontAccommodations').val('Any');
	    	$('#gs_testPause').val('Any');
	    	$('#gs_screenReader').val('Any');
	    	$('#gs_untimedTest').val('Any');
	    	$('#gs_highLighter').val('Any');
	    	
	    	resetBulk();
	    	setAnchorButtonState('assignAccommButton', true);
			SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#selectedBulkTreeOrgNodeId").val(SelectedOrgNodeId);
 		     UIBlock();
 		  	if(!bulkStudentgridLoaded) {
 		  		bulkStudentgridLoaded = true;
 		  			populateBulkStudentGrid();
 		  		}
			else
				gridReloadForBulkStudent();
				
				 if(bulkStudentgridLoaded) {
				 	document.getElementById('ShowButtons').style.display = "block";
				 	var showAccommodations = $("#supportAccommodations").val();
					if(showAccommodations  == 'false') {
						$("#studentAccommGrid").jqGrid("hideCol",["calculator","hasColorFontAccommodations","testPause","screenReader","untimedTest"]); 
					} else {
						$("#studentAccommGrid").jqGrid("showCol",["calculator","hasColorFontAccommodations","testPause","screenReader","untimedTest"]); 
					}
					$("#studentAccommGrid").jqGrid("hideCol",["orgNodeId","orgNodeName","hasAccommodations"]);
					
				}
		});
		
	registerDelegate("studentBulkOrgNode");		
	   
}

function imageFormat( cellvalue, options, rowObject ){
	if(cellvalue == 'T')
		return "Yes";
	else
		return "No";
		
} 

function selectFormat( cellvalue, options, rowObject ){
		var orgArray = 	orgForDupStu[rowObject.studentId];
		var optList = "<select id='dupStu"+rowObject.studentId +"'>" ;
		for(var key in orgArray){
		if(key != undefined)
           optList= optList + "<option value='"+key+"'>"+$.trim(orgArray[key])+"</option>" 
		}    
		optList = optList + "</select> " ;
		return optList;
}



function populateBulkStudentGrid() {
 		UIBlock();
 		bulkStudentgridLoaded = true;
 		var studentIdTitle = $("#studentIdLabelName").val();
 		var calculator= '<img src="/StudentWeb/resources/images/calc.PNG" title="Calculator"/>';
 		var colorFont= '<img src="/StudentWeb/resources/images/colorfont.PNG" title="Color/Font"/>';
 		var testPause= '<img src="/StudentWeb/resources/images/pause.PNG" title="Pause"/>';
 		var screenReader= '<img src="/StudentWeb/resources/images/screenreader.PNG" title="Reader"/>';
 		var untimedTest= '<img src="/StudentWeb/resources/images/untimed.PNG" title="Untimed"/>';
 		//reset();
       $("#studentAccommGrid").jqGrid({         
          url: 'getStudentForSelectedNode.do?q=2&stuForOrgNodeId='+$("#selectedBulkTreeOrgNodeId").val(), 
		  type:   'POST',
		  datatype: "json",          
          colNames:[ 'Last Name','First Name', 'M.I', studentIdTitle, 'Organization','orgName','Accommodation', 'Grade', calculator, colorFont, testPause, screenReader, untimedTest, 'high'],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:132, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:133, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:60, editable: true, align:"left",sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'userName',index:'userName', width:170, editable: true, align:"left", sortable:true, search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeId',index:'orgNodeId',editable: false, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeName',index:'orgNodeName',editable: false, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations',editable: false, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade', width:70, editable: true, align:"left", sortable:true, search: true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: gradeOptions }},
		   		{name:'calculator',index:'calculator', width:60, editable: true, align:"center", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'hasColorFontAccommodations',index:'hasColorFontAccommodations',editable: true, width:60, align:"center", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'testPause',index:'testPause',editable: true, width:60, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'screenReader',index:'screenReader',editable: true, width:60, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'untimedTest',index:'untimedTest',editable: true, width:60, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'highLighter',index:'highLighter',editable: true, width:60, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} }
		   		
		   	],
		   		jsonReader: { repeatitems : false, root:"studentNode", id:"studentId",
		   	records: function(obj) { 
		   	 	studentArrId = obj.studentIdArray.split(",");
		   	 } },
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:true,
			pager: '#studentAccommpager', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,  
			caption:"Student List",
			//toolbar: [true,"top"],
			onPaging: function() {
				var reqestedPage = parseInt($('#studentAccommGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_studentAccommGrid').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#studentAccommGrid').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#studentAccommGrid').setGridParam({"page": minPageSize});
				}
			},
			gridComplete: function() { 
			totalRowSelectedOnPage = 0;
			var allRowsInGrid = $('#studentAccommGrid').jqGrid('getDataIDs');
			 	$('.cbox').attr('checked', false); 
			 	for(var i=0; i<allRowsInGrid.length; i++) {
			 		if(selectedStudentObjArr[allRowsInGrid[i]] != undefined) {
					 	$("#"+selectedStudentObjArr[allRowsInGrid[i]]+" td input").attr('checked', true).trigger('click').attr('checked', true);
					 	$("#"+selectedStudentObjArr[allRowsInGrid[i]]).addClass("ui-state-highlight").attr({
			               "aria-selected": true,
			               tabindex: "0"
			           });
			        } else {
		           		$("#"+selectedStudentObjArr[allRowsInGrid[i]]+" td input").attr('checked', false).trigger('click').attr('checked', false);
		           }
				}
				
				 if(totalRowSelectedOnPage == allRowsInGrid.length ) {
				 	$('#cb_studentAccommGrid').attr('checked', true);
				 } else {
				 	$('#cb_studentAccommGrid').attr('checked', false);
				 }
			},
			onSelectAll: function (rowIds, status) {
				if(!status) {
					//allRowSelected = false;
					UIBlock();
					for(var i=0; i<studentArrId.length; i++){
						delete selectedStudentObjArr[studentArrId[i]];
					}
					totalRowSelectedOnPage = 0;
					setAnchorButtonState('assignAccommButton', true);
					$.unblockUI();  
				} else {
					//allRowSelected = true;
					UIBlock();
					for(var i=0; i<studentArrId.length; i++){
						selectedStudentObjArr[parseInt(studentArrId[i])]= parseInt(studentArrId[i]);
					}
					setAnchorButtonState('assignAccommButton', false);
					$.unblockUI();
					var allRowsInGrid = $('#studentAccommGrid').jqGrid('getDataIDs');  
					totalRowSelectedOnPage = allRowsInGrid.length;
				}
				
			},
			onSelectRow: function (rowid, status) {
				var selectedRowId = rowid;
				var isRowSelected = $("#studentAccommGrid").jqGrid('getGridParam', 'selrow');
				if(isRowSelected != null) {
					setAnchorButtonState('assignAccommButton', false);
				} else {
					setAnchorButtonState('assignAccommButton', true);
				}
				if(status) {
					totalRowSelectedOnPage = totalRowSelectedOnPage+1;
					selectedStudentObjArr[selectedRowId]= selectedRowId;
				} else {
					totalRowSelectedOnPage = totalRowSelectedOnPage-1;
					 delete selectedStudentObjArr[selectedRowId]; 
				}
				var allRowsInGrid = $('#studentAccommGrid').jqGrid('getDataIDs');
				if(totalRowSelectedOnPage == allRowsInGrid.length ) {
				 	$('#cb_studentAccommGrid').attr('checked', true);
				 } else {
				 	$('#cb_studentAccommGrid').attr('checked', false);
				 }
			},
			loadComplete: function () {
				if ($('#studentAccommGrid').getGridParam('records') === 0) {
					isPAGridEmpty = true;
            		$('#sp_1_studentAccommpager').text("1");
            		$('#next_studentAccommpager').addClass('ui-state-disabled');
            	 	$('#last_studentAccommpager').addClass('ui-state-disabled');
            	} else {
            		isPAGridEmpty = false;
            	}
            	//setEmptyListMessage('PA');
            	$.unblockUI();  
				$("#studentAccommGrid").setGridParam({datatype:'local'});
				var tdList = ("#studentAccommpager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#studentAccommGrid").jqGrid('navGrid','#selectStudentPager',{edit:false,add:false,del:false,search:false,refresh:false});
	 jQuery("#studentAccommGrid").jqGrid('filterToolbar');
	/* $("#t_selectStudent").append("<select id='groupSelection'>     <option>Show All</option>   <option>Student with accommodation</option>     <option>Student without accommodation</option>     </select> ");
	$("select","#t_selectStudent").change(function( val){
		if($('#groupSelection').val() == 'Student with accommodation') {
			$('#gs_calculator').val('T');
			$('#gs_hasColorFontAccommodations').val('T');
			$('#gs_testPause').val('T');
			$('#gs_screenReader').val('T');
			$('#gs_untimedTest').val('T');
		} else if ($('#groupSelection').val() == 'Student without accommodation'){
			$('#gs_calculator').val('F');
			$('#gs_hasColorFontAccommodations').val('F');
			$('#gs_testPause').val('F');
			$('#gs_screenReader').val('F');
			$('#gs_untimedTest').val('F');
		} else if ($('#groupSelection').val() == 'Show All'){
			$('#gs_calculator').val('');
			$('#gs_hasColorFontAccommodations').val('');
			$('#gs_testPause').val('');
			$('#gs_screenReader').val('');
			$('#gs_untimedTest').val('');
		}
		
	});*/
	 
	 
	 
}


 function gridReloadForBulkStudent(){ 
       jQuery("#studentAccommGrid").jqGrid('setGridParam',{datatype:'json'});     
 	   var sortArrow = jQuery("#studentAccommGrid");
       jQuery("#studentAccommGrid").jqGrid('setGridParam', {url:'getStudentForSelectedNode.do?q=2&stuForOrgNodeId='+$("#selectedBulkTreeOrgNodeId").val(),page:1}).trigger("reloadGrid");
       
       jQuery("#studentAccommGrid").sortGrid('lastName',true);
     	//For MQC Defect - 67122
       var arrowElements = sortArrow[0].grid.headers[0].el.lastChild.lastChild;
       $(arrowElements.childNodes[0]).removeClass('ui-state-disabled');
       $(arrowElements.childNodes[1]).addClass('ui-state-disabled');
      

  }
      
  function resetBulk (){
  	
  	document.getElementById('displayPopupMessage').style.display = "none";	
	document.getElementById('displayBulkMessageMain').style.display = "none";	
	document.getElementById('errorImg').style.display = "none";	
  }
  
  function openAssignAccommPopup(element){
 	if (isButtonDisabled(element))
		return true;
		
	 $("#AssignAccommPopup").dialog({  
		title:"Assign Accommodation",  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '550px',
	 	modal: true,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 //$("#AssignAccommPopup").css('height',120);
		 var toppos = ($(window).height() - 475) /2 + 'px';
		 var leftpos = ($(window).width() - 550) /2 + 'px';
		 $("#AssignAccommPopup").parent().css("top",toppos);
		 $("#AssignAccommPopup").parent().css("left",leftpos);
		 resetBulk();	
  }
  
  
  function saveBulkStudentData(){
 
	var param;
	var showStudentInGrid = false;
	var studentIds = "";
	
	
	for(var key in selectedStudentObjArr){
		studentIds =  selectedStudentObjArr[key] + "," + studentIds;
	}
	studentIds = studentIds.substring(0,studentIds.length-1)
	param = $("#AssignAccommPopup *").serialize();
	var dataToBeAdded  = getDataToBeAdded(param);
	if(dataToBeAdded['isAccommodationSelected']) {
	$.ajax(
		{
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'saveBulkStudentData.do?studentIds='+studentIds,
				type:		'POST',
				data:		 param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){	
								  
								var errorFlag = data.errorFlag;
								var successFlag = data.successFlag;
								if(successFlag) {
									resetRadioAccommodation();
									closePopUp('AssignAccommPopup');
									setBulkMessageMain(data.title, data.content, data.type, "");
									document.getElementById('displayBulkMessageMain').style.display = "block";	
									
						            var allRowsInGrid = $('#studentAccommGrid').jqGrid('getDataIDs');
								 	for(var i=0; i<allRowsInGrid.length; i++) {
								 	 	 jQuery("#studentAccommGrid").setRowData(selectedStudentObjArr[allRowsInGrid[i]], dataToBeAdded, "first");
									}
									
									$.unblockUI();			
									}
									else{
										if(data.type == 'alertMessage') {
											setPopupMessage();
										} else {
											setBulkMessageMain(data.title, data.content, data.type, "");
											document.getElementById('displayBulkMessageMain').style.display = "block";	
											document.getElementById('errorImg').style.display = "block";	
											resetRadioAccommodation();
											closePopUp('AssignAccommPopup');
										}
										
										
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
	} else {
		setPopupMessage();
		document.getElementById('displayPopupMessage').style.display = "block";	
		
	}
	
  }
  
  function setBulkMessageMain(title, content, type, message){
			$("#titleBulkMain").text(title);
			$("#contentBulkMain").text(content);
			$("#messageBulkMain").text(message);
		}
		function setPopupMessage(){
			$("#titleBulkPopup").text(BULK_ADD_TITLE);
			$("#contentBulkPopup").text(BULK_ACCOM_NOTSELECTED);
			$("#messageBulkPopup").text("");
		}
      
	
	function getDataToBeAdded(param) {
		var dataToBeAdded = {};
		var isAccommodationSelected = false;
		var screen_reader = param.indexOf("screen_reader");
        if(screen_reader != -1) {
      	 screen_reader = screen_reader + "screen_reader".length+1;
      	 dataToBeAdded['screenReader'] = param.substring(screen_reader, screen_reader+1);
      	 isAccommodationSelected = true;
       	}
       	var calculator = param.indexOf("calculator");
        if(calculator != -1) {
      	 calculator = calculator + "calculator".length+1;
      	 dataToBeAdded['calculator'] = param.substring(calculator, calculator+1);
      	 isAccommodationSelected = true;
       	}
       	var test_pause = param.indexOf("test_pause");
        if(test_pause != -1) {
      	 test_pause = test_pause + "test_pause".length+1;
      	 dataToBeAdded['testPause'] = param.substring(test_pause, test_pause+1);
      	 isAccommodationSelected = true;
       	}
       	var untimed_test = param.indexOf("untimed_test");
        if(untimed_test != -1) {
      	 untimed_test = untimed_test + "untimed_test".length+1;
      	 dataToBeAdded['untimedTest'] = param.substring(untimed_test, untimed_test+1);
      	 isAccommodationSelected = true;
       	}
       	var highlighter = param.indexOf("highlighter");
        if(highlighter != -1) {
      	 highlighter = highlighter + "highlighter".length+1;
      	 dataToBeAdded['highLighter'] = param.substring(highlighter, highlighter+1);
      	 isAccommodationSelected = true;
       	}
       	var colorFont = param.indexOf("colorFont");
        if(colorFont != -1) {
      	 colorFont = colorFont + "colorFont".length+1;
      	 dataToBeAdded['hasColorFontAccommodations'] = param.substring(colorFont, colorFont+1);
      	 isAccommodationSelected = true;
       	}
       	dataToBeAdded['isAccommodationSelected'] = isAccommodationSelected;
       	return dataToBeAdded;
	
	
	}