var bulkStudentgridLoaded = false;	
var gradeOptions = ":Any;JV:JV;AD:AD"; 
var AccommOption = ":Any;T:Yes;F:No";
var allRowSelected = false;
var studentArrId = [];
var studentObjArr = [];
var totalRowSelectedOnPage = 0;
var customerDemographicList ;
var onNodechange = false;
var listStr;
var submittedSuccesfully = "";
var previousDataForpaging = {};


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
						listStr = createDemoList();
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
	    	onNodechange = true;
	    	submittedSuccesfully = "";
	    	clearFilterDropDown();
	    	clearList();
	    	resetBulk();
	    	setAnchorButtonState('assignAccommButton', true);
			SelectedOrgNodeId = $(this).parent().attr("id");
 		    $("#selectedBulkTreeOrgNodeId").val(SelectedOrgNodeId);
 		     UIBlock();
 		  	if(!bulkStudentgridLoaded) {
 		  		bulkStudentgridLoaded = true;
 		  			populateBulkStudentGrid();
 		  			loadMenu();
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
 		var highlighter= '<img src="/StudentWeb/resources/images/highlighter.JPG" title="Highlighter" />';
 		
 		//reset();
       $("#studentAccommGrid").jqGrid({         
          url: 'getStudentForSelectedNode.do?q=2&stuForOrgNodeId='+$("#selectedBulkTreeOrgNodeId").val(), 
		  type:   'POST',
		  datatype: "json",          
          colNames:[ 'Last Name','First Name', 'M.I', 'Grade', calculator, colorFont, testPause, screenReader, untimedTest, highlighter],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:152, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:153, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:80, editable: true, align:"left",sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade', width:90, editable: true, align:"left", sortable:true, search: true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: gradeOptions }},
		   		{name:'calculator',index:'calculator', width:75, editable: true, align:"center", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'hasColorFontAccommodations',index:'hasColorFontAccommodations',editable: true, width:75, align:"center", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'testPause',index:'testPause',editable: true, width:75, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'screenReader',index:'screenReader',editable: true, width:75, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'untimedTest',index:'untimedTest',editable: true, width:75, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'highLighter',index:'highLighter',editable: true, width:75, align:"center", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} }
		   		
		   	],
		   		jsonReader: { repeatitems : false, root:"studentNode", id:"studentId",
		   	records: function(obj) { 
		   	 	studentArrId = obj.studentIdArray.split(",");
		   	 	studentObjArr = obj.studentNode;
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
			toolbar: [true,"top"],
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
			var gridreloadRowCount = totalRowSelectedOnPage;
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
		           if(submittedSuccesfully != ""){
		           	 jQuery("#studentAccommGrid").setRowData(previousDataForpaging[allRowsInGrid[i]], submittedSuccesfully, "first");
		           	 resetBulk();
		           }
		        }
				totalRowSelectedOnPage = gridreloadRowCount;
				if(onNodechange){
					totalRowSelectedOnPage = 0;
					for(var i=0; i<studentArrId.length; i++) {
						if (selectedStudentObjArr[studentArrId[i]] != undefined)
							totalRowSelectedOnPage = totalRowSelectedOnPage + 1;
					}	
					onNodechange = false;
				} 
				
				 if(totalRowSelectedOnPage == studentArrId.length ) {
				 	$('#cb_studentAccommGrid').attr('checked', true);
				 } else {
				 	$('#cb_studentAccommGrid').attr('checked', false);
				 }
			},
			onSelectAll: function (rowIds, status) {
					var grade = $('#gs_grade').val();
			    	var calculator = $('#gs_calculator').val();
			    	var hasColorFontAccommodations = $('#gs_hasColorFontAccommodations').val();
			    	var testPause = $('#gs_testPause').val();
			    	var screenReader = $('#gs_screenReader').val();
			    	var untimedTest = $('#gs_untimedTest').val();
			    	var highLighter = $('#gs_highLighter').val();
					
					if(!status) {
						UIBlock();
						if(defaultFilterApplied()) {
							for(var i=0; i<studentArrId.length; i++){
								delete selectedStudentObjArr[studentArrId[i]];
							}
						} else {
							for(var i=0; i<studentArrId.length; i++){
								var skip = skipStudent(grade, calculator, hasColorFontAccommodations, testPause, screenReader, untimedTest, highLighter, studentArrId[i]);
						 	 	if(!skip) {
							 	 	delete selectedStudentObjArr[studentArrId[i]];
							 	 }
								  
							}
				
						}	
						totalRowSelectedOnPage = 0;
						setAnchorButtonState('assignAccommButton', true);
						$.unblockUI();  
					} else {
						UIBlock();
						if(defaultFilterApplied()) {
							for(var i=0; i<studentArrId.length; i++){
								selectedStudentObjArr[parseInt(studentArrId[i])]= parseInt(studentArrId[i]);
							}
							totalRowSelectedOnPage = studentArrId.length;
						} else {
							totalRowSelectedOnPage = 0;
							for(var i=0; i<studentArrId.length; i++){
								var skip = skipStudent(grade, calculator, hasColorFontAccommodations, testPause, screenReader, untimedTest, highLighter, studentArrId[i]);
							 	  if(!skip) {
								  	selectedStudentObjArr[parseInt(studentArrId[i])]= parseInt(studentArrId[i]);
								  	totalRowSelectedOnPage = totalRowSelectedOnPage + 1;
								  } else {
								  	delete selectedStudentObjArr[studentArrId[i]];
								  }
								  
							}
							
						}	
						setAnchorButtonState('assignAccommButton', false);
						$.unblockUI();
						var allRowsInGrid = $('#studentAccommGrid').jqGrid('getDataIDs');  
						
					}
				 if(submittedSuccesfully != ""){
		           	 resetBulk();
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
				
				if(totalRowSelectedOnPage == studentArrId.length ) {
				 	$('#cb_studentAccommGrid').attr('checked', true);
				 } else {
				 	$('#cb_studentAccommGrid').attr('checked', false);
				 }
				 
				  if(submittedSuccesfully != ""){
		           	 resetBulk();
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
	 $("thead:first tr.ui-jqgrid-labels", $("#studentAccommGrid")[0].grid.hDiv).height('26px');
	 jQuery("#studentAccommGrid").jqGrid('navGrid','#selectStudentPager',{edit:false,add:false,del:false,search:false,refresh:false});
	 jQuery("#studentAccommGrid").jqGrid('filterToolbar');
	 $("#t_studentAccommGrid").append(listStr).height(30);
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
  		var demo1 = $('#selectedBox1').text();
  		 if(demo1.indexOf('Please select')!= -1){
			demo1 = "";
		 } else {
		 	var d1a = demo1.split(">>");
		 	demo1 = $.trim(d1a[0])+"_"+$.trim(d1a[1]);
		 }
		 var demo2 = $('#selectedBox2').text();
  		 if(demo2.indexOf('Please select')!= -1){
			demo2 = "";
		 } else {
		 	var d1a = demo2.split(">>");
		 	demo2 = $.trim(d1a[0])+"_"+$.trim(d1a[1]);
		 }
		 var demo3 = $('#selectedBox3').text();
  		 if(demo3.indexOf('Please select')!= -1){
			demo3 = "";
		 } else {
		 	var d1a = demo3.split(">>");
		 	demo3 = $.trim(d1a[0])+"_"+$.trim(d1a[1]);
		 }
       jQuery("#studentAccommGrid").jqGrid('setGridParam',{datatype:'json'});     
 	   var sortArrow = jQuery("#studentAccommGrid");
       jQuery("#studentAccommGrid").jqGrid('setGridParam', {url:'getStudentForSelectedNode.do?q=2&stuForOrgNodeId='+$("#selectedBulkTreeOrgNodeId").val()+'&demoFilter1='+demo1+'&demoFilter2='+demo2+'&demoFilter3='+demo3,page:1}).trigger("reloadGrid");
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
		title:"Select Accommodation",  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '560px',
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
									closePopUp('AssignAccommPopup');
									setBulkMessageMain(data.title, data.content, data.type, "");
									var allRowsInGrid = $('#studentAccommGrid').jqGrid('getDataIDs');
						            for(var i=0; i<allRowsInGrid.length; i++) {
								 	 	 jQuery("#studentAccommGrid").setRowData(selectedStudentObjArr[allRowsInGrid[i]], dataToBeAdded, "first");
								 	 	 $("#"+selectedStudentObjArr[allRowsInGrid[i]]+" td input").attr('checked', false).trigger('click').attr('checked', false);
								 	 	 submittedSuccesfully = dataToBeAdded;
									}
									previousDataForpaging = selectedStudentObjArr;
									selectedStudentObjArr = {};
									document.getElementById('displayBulkMessageMain').style.display = "block";	
									
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
			//$("#titleBulkMain").text(title);
			$("#contentBulkMain").text(content);
			$("#messageBulkMain").text(message);
		}
		function setPopupMessage(){
			//$("#titleBulkPopup").text(BULK_ADD_TITLE);
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
	var globalCustomerDemographic1='';
	var globalCustomerDemographic2='';
	var globalCustomerDemographic3='';
	
	function createDemoList(){
		var liMenuStr = "<div><ul class='sf-menu' id='menu'>";
		 for (var i=0; i<3; i++) {
		 	liMenuStr = liMenuStr + "<li class='selected'><a href='#' class='menuText' id='selectedBox"+parseInt(i+1)+"'>Please select</a><ul><li><a href='#' id='ps_"+parseInt(i+1)+"'>Please select</a>";
		 	for(var j=0; j<customerDemographicList.length; j++) {
		 		liMenuStr= liMenuStr + "<li><a href='#' id='"+customerDemographicList[j].id+"_"+parseInt(i+1)+"'>"+customerDemographicList[j].labelName+"</a>";
		 		var cDemoValues = customerDemographicList[j].customerDemographicValues;
		 		if (cDemoValues.length > 0){
		 			liMenuStr= liMenuStr + "<ul>";
		 			for(var k=0; k< cDemoValues.length; k++){
		 				liMenuStr = liMenuStr + "<li><a href='#' id='"+cDemoValues[k].valueName+"'>"+cDemoValues[k].valueName+"</a></li>";
		 			}
		 			liMenuStr= liMenuStr + "</ul>";
		 		}
		 		liMenuStr= liMenuStr + "</li>";
		 	}
		 	liMenuStr= liMenuStr + "</ul></li>";
		 	
		 	
		 }
		 liMenuStr = liMenuStr + "</ul></div>";
		 liMenuStr = liMenuStr + "<div><input type='button' id='apply' onclick='javascript:applyList(); return false;'  class = 'ui-widget-header' style='border:1px solid;float:right;width:40px;margin:5px;height:20px;font-weight:bold;font-family:arial;cursor:pointer !important;' value='Apply'/>"+
		 			"	<input type='button' id='clear' onclick='javascript:clearList(); return false;' class = 'ui-widget-header' value='Clear' style='border:1px solid;float:right;width:40px;margin:5px;height:20px;font-weight:bold;font-family:arial;margin-right:0px;cursor:pointer !important;'/></div>";
		 return liMenuStr;
	}
	
	
	function loadMenu()
{	
	$('#menu').superfish({
		delay:10,
		speed:'fast'
	}).delegate("a","click",function(e) {
		
		var selected = $(this);
		var selectedFullId = "";	
		var selectedParent = selected.parent().parent().prev();
		 if(!selectedParent.is(".menuText")) {
			var pTxt = selected.parent().parent().prev().text();
			pTxt = pTxt.substring(0,pTxt.length);
			var parentTxt =  ((pTxt.length >= 15) ? pTxt.substring(0,15)+"... >> " : pTxt);

			var tooltip = txt = selected.text();	
			if(! selected.is(".menuText")) {
				var txt = ((txt.length >= 15) ? txt.substring(0,15)+"..." : txt);		
				selected.parents("li.selected").children("a").attr("title",tooltip).html(parentTxt +" "+ txt+"<span class='sf-sub-indicator'></span>");
				selected.parents("ul:not(.sf-menu)").hide();
			}
			selectedFullId = selected.parents("li:eq(1)").children("a").attr("id");			
			
		} else {
			var parentTxt = "";
			var tooltip = txt = selected.text();			
			if(! selected.is(".menuText")) {
				var txt = ((txt.length >= 15) ? txt.substring(0,15)+"... >> All " : txt.substring(0,txt.length) +" All");		
				if(txt.indexOf('Please select')!= -1){
					txt = 'Please select';
				}
				
				selected.parents("li.selected").children("a").attr("title",tooltip).html(parentTxt + txt+"<span class='sf-sub-indicator'></span>");
				selected.parents("ul:not(.sf-menu)").hide();
				selectedFullId = selected.attr("id");
				
			}
			
		}
		if(selectedFullId != undefined) {
			var selectedId = selectedFullId.split('_');
				
			if(selectedFullId.indexOf('ps_') != -1){
				if(selectedId[1] == '1'){
					$("[id^='" + globalCustomerDemographic1+"_']").show();
				}
				if(selectedId[1] == '2'){
					$("[id^='" + globalCustomerDemographic2+"_']").show();
				}
				if(selectedId[1] == '3'){
					$("[id^='" + globalCustomerDemographic3+"_']").show();
				}
			}else {	
				if(selectedId[1] == '1'){
					globalCustomerDemographic1 = selectedId[0];
					document.getElementById(selectedId[0]+"_2").style.display = 'none';
					document.getElementById(selectedId[0]+"_3").style.display = 'none';
				}
				if(selectedId[1] == '2'){
				globalCustomerDemographic2 = selectedId[0];
					document.getElementById(selectedId[0]+"_1").style.display = 'none';
					document.getElementById(selectedId[0]+"_3").style.display = 'none';
				}
				if(selectedId[1] == '3'){
					globalCustomerDemographic3 = selectedId[0];
					document.getElementById(selectedId[0]+"_1").style.display = 'none';
					document.getElementById(selectedId[0]+"_2").style.display = 'none';
				}
			}
		}			
	});	
	
	
}
	function clearList(){
		 $("#t_studentAccommGrid").html(listStr).height(30);
		 loadMenu();
	}
	
	function applyList(){
		UIBlock();
		gridReloadForBulkStudent();
		clearFilterDropDown();
		//$.unblockUI();   
	}
	
	 function defaultFilterApplied(){
	 	if ($('#gs_grade').val() != "" && 
	    	$('#gs_calculator').val() != "" && 
	    	$('#gs_hasColorFontAccommodations').val() != "" && 
	    	$('#gs_testPause').val() != "" && 
	    	$('#gs_screenReader').val() != "" && 
	    	$('#gs_untimedTest').val() != "" && 
	    	$('#gs_highLighter').val() != "") 
	    		return true;
	    	else
	    		return false;
	 }
	 
	 function closeUnsaveConfirmationPopup (){
	 	closePopUp('unSaveConfirmationPopup');
	 	closePopUp('AssignAccommPopup');
	 }
	 
	 function cancelAssignAccom(){
	 	param = $("#AssignAccommPopup *").serialize();
		var dataToBeAdded  = getDataToBeAdded(param);
		if(dataToBeAdded['isAccommodationSelected']){
			 $("#unSaveConfirmationPopup").dialog({  
			title:"Confirmation Alert",  
			resizable:false,
		 	autoOpen: true,
		 	width: '400px',
		 	modal: true,
		 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
			});	
		    $("#unSaveConfirmationPopup").css('height',120);
			var toppos = ($(window).height() - 290) /2 + 'px';
			var leftpos = ($(window).width() - 410) /2 + 'px';
			$("#unSaveConfirmationPopup").parent().css("top",toppos);
			$("#unSaveConfirmationPopup").parent().css("left",leftpos);	
		} else {
			closePopUp('AssignAccommPopup');
		}
	 }
	 
	  function clearFilterDropDown (){
	  		$("option:contains('Any')",'#gs_grade' ).attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_calculator').attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_hasColorFontAccommodations').attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_testPause').attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_screenReader').attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_untimedTest').attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_highLighter').attr("selected", "selected");
	    	$('#gs_grade').trigger('change');
	    	$('#gs_calculator').trigger('change');
	    	$('#gs_hasColorFontAccommodations').trigger('change');
	    	$('#gs_testPause').trigger('change');
	    	$('#gs_screenReader').trigger('change');
	    	$('#gs_untimedTest').trigger('change');
	    	$('#gs_highLighter').trigger('change');
	  }
	  
	  
	   function skipStudent(grade, calculator, hasColorFontAccommodations, testPause, screenReader, untimedTest, highLighter, studentId){
	  	var skip = false;
		 var objIndex = jQuery.inArray(studentId, studentArrId); 
		 var obj = studentObjArr[objIndex];
		 if(skip == false && grade != ""){
			 if(obj['grade'] == grade)
			 	skip = false;
			 else 
			 	skip = true;
		 } else  if(skip == false && calculator != ""){
			 if(obj['calculator'] == calculator)
			 	skip = false;
			 else 
			 	skip = true;
		 } else if(skip == false && hasColorFontAccommodations != ""){
			 if(obj['hasColorFontAccommodations'] == hasColorFontAccommodations)
			 	skip = false;
			 else 
			 	skip = true;
		 }
		 else if(skip == false && testPause != ""){
			 if(obj['testPause'] == testPause)
			 	skip = false;
			 else 
			 	skip = true;
		 }
		 else if(skip == false && screenReader != ""){
			 if(obj['screenReader'] == screenReader)
			 	skip = false;
			 else 
			 	skip = true;
		 }
		 else if(skip == false && untimedTest != ""){
			 if(obj['untimedTest'] == untimedTest)
			 	skip = false;
			 else 
			 	skip = true;
		 }
		 else if(skip == false && highLighter != ""){
			 if(obj['highLighter'] == highLighter)
			 	skip = false;
			 else 
			 	skip = true;
		 }
		
		  return skip;
	  }
	
	