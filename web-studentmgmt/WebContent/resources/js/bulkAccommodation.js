var bulkStudentgridLoaded = false;	
var gradeOptions = ":Any;JV:JV;AD:AD"; 
var AccommOption = ":Any;T:Yes;F:No";
var allRowSelected = false;
var studentArrId = [];
var studentIdIndexer = {};
var studentObjArr = [];
var totalRowSelectedOnPage = 0;
var customerDemographicList ;
var onNodechange = false;
var onNodeChangeEmptyMsg = true;
var listStr;
var submittedSuccesfully = "";
var previousDataForpaging = {};
var applyChange = false;
var filterLoadFlag = false;
var filterTimer = null;
var selectedStudentObjArr = {};
var isPopUp = false;

$(document).bind('keydown', function(event) {
	var code = (event.keyCode ? event.keyCode : event.which);
    if(code == 27){
   		if(isPopUp){
	   		if(document.getElementById("bulkMoveButton") != null) {
	   			closeBulkMovePopup();
	   		} else {
				cancelAssignAccom();
			}
   		}
        return false;
    }
});
	
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
	    
	    $("#studentBulkOrgNode").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#studentBulkOrgNode ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
	    
	    $("#studentBulkOrgNode").delegate("a","click", function(e) {
	    	submittedSuccesfully = "";
	    	clearFilterDropDown();
	    	clearList();
	    	resetBulk();
	    	$('#cb_studentAccommGrid').attr('checked', false);
	    	determineStudentSel(selectedStudentObjArr, 'assignAccommButton');
			SelectedOrgNodeId = $(this).parent().attr("id");
			var topNodeSelected = $(this).parent().attr("cid");
			if(topNodeSelected == leafNodeCategoryId || topNodeSelected == (leafNodeCategoryId -1)) {
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
				 	document.getElementById('viewStatus').style.visibility = "visible";
				 	var showAccommodations = $("#supportAccommodations").val();
					if(showAccommodations  == 'false') {
						$("#studentAccommGrid").jqGrid("hideCol",["calculator","hasColorFontAccommodations","testPause","screenReader","untimedTest"]); 
					} else {
						$("#studentAccommGrid").jqGrid("showCol",["calculator","hasColorFontAccommodations","testPause","screenReader","untimedTest"]); 
					}
					
				}
			}
			totalRowSelectedOnPage = 0;
	    	onNodechange = true;
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

function isEmpty(obj){
	var empty = true;
	for( var i in obj ) {
		empty = false;
		break;
	}
	return empty;
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
 		var highlighter= '<img src="/StudentWeb/resources/images/highlighter.PNG" title="Highlighter" />';
 		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.stuForOrgNodeId = $("#selectedBulkTreeOrgNodeId").val();
 		//reset();
       $("#studentAccommGrid").jqGrid({         
          url: 'getStudentForSelectedNode.do', 
		  mtype:   'POST',
		  datatype: "json",
		  postData: postDataObject,
          colNames:[ $("#jqgLastNameID").val(),$("#jqgFirstNameID").val(), $("#jqgMiddleIniID").val(), $("#jqgGradeID").val(), calculator, colorFont, testPause, screenReader, untimedTest, highlighter],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:152, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:153, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:30, editable: true, align:"left",sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade', width:90, editable: true, align:"left", sortable:true, search: true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, stype: 'select', searchoptions:{ sopt:['eq'], value: gradeOptions }},
		   		{name:'calculator',index:'calculator', width:75, editable: true, align:"center", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'hasColorFontAccommodations',index:'hasColorFontAccommodations',editable: true, width:115, align:"center", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'testPause',index:'testPause',editable: true, width:75, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'screenReader',index:'screenReader',editable: true, width:75, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'untimedTest',index:'untimedTest',editable: true, width:75, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'highLighter',index:'highLighter',editable: true, width:75, align:"center", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} }
		   		
		   	],
		   		jsonReader: { repeatitems : false, root:"studentNode", id:"studentId",
		   	records: function(obj) { 
		   	 	studentArrId = obj.studentIdArray.split(",");
		   	 	studentObjArr = obj.studentNode;
		   	 	studentIdIndexer = obj.studentIdIndexer;
		   	 } },
		   	loadui: "disable",
			rowNum:20,
			loadonce:true, 
			multiselect:true,
			pager: '#studentAccommpager', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 392,  
			width: $("#jqGrid-content-section").width(), 
			caption:$("#accomStuGrid").val(),
			toolbar: [true,"top"],
			onPaging: function() {
				var reqestedPage = parseInt($('#studentAccommGrid').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_studentAccommpager').text());
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
		           if(!isEmpty(previousDataForpaging)){
		           	 jQuery("#studentAccommGrid").setRowData(previousDataForpaging[allRowsInGrid[i]], submittedSuccesfully, "first");
		           	 if ($('#AssignAccommPopup').parents('.ui-dialog:visible').length == 0) {		           	 	
		           	 	resetBulk();
		           	 }
		           	 
		           }
		        }
				totalRowSelectedOnPage = gridreloadRowCount;
				if(onNodechange || applyChange){
					totalRowSelectedOnPage = 0;
					for(var i=0; i<studentArrId.length; i++) {
						if (selectedStudentObjArr[studentArrId[i]] != undefined)
							totalRowSelectedOnPage = totalRowSelectedOnPage + 1;
					}
					 if(onNodechange){
						 onNodeChangeEmptyMsg = true;	
					 }
					 applyChange = false;
					//onNodechange = false;
				} 
				determineStudentSel(selectedStudentObjArr, 'assignAccommButton');
				 if(totalRowSelectedOnPage == studentArrId.length ) {
				 	$('#cb_studentAccommGrid').attr('checked', true);
				 } else {
				 	$('#cb_studentAccommGrid').attr('checked', false);
				 }
				if(allRowsInGrid.length <= 0){
					//setAnchorButtonState('assignAccommButton', true);
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
					var allData = $("#studentAccommGrid").jqGrid('getGridParam','data');
					
					var args = {
						"grade" : grade,
						"calculator" : calculator,
						"hasColorFontAccommodations" : hasColorFontAccommodations,
						"testPause" : testPause,
						"screenReader" : screenReader,
						"untimedTest" : untimedTest,
						"highLighter" : highLighter,						
						"allData" : allData						 					
					};
					
					if(!status) {
						UIBlock();
						if(defaultFilterApplied()) {
							for(var i=0; i<studentArrId.length; i++){
								if(studentArrId[i] == "") 
									continue;
								delete selectedStudentObjArr[studentArrId[i]];
							}
						$.unblockUI();  
						} else {
							var worker1 = new CustomWorker(studentArrId,args,ba_runnable,
							function(){
								runnableOnComplete();
								$.unblockUI(); 
							});
							worker1.start();
						}	
						totalRowSelectedOnPage = 0;
					} else {
						UIBlock();
						if(defaultFilterApplied()) {
							for(var i=0; i<studentArrId.length; i++){
								if(studentArrId[i] == "") 
									continue;
								selectedStudentObjArr[parseInt(studentArrId[i])]= parseInt(studentArrId[i]);
							}
							totalRowSelectedOnPage = studentArrId.length;
							stopSelectAll(studentArrId);
							$.unblockUI();
						} else {
							totalRowSelectedOnPage = 0;
							var worker2 = new CustomWorker(studentArrId,args,ba_runnable1,
							function(){
								stopSelectAll(studentArrId);
								runnableOnComplete();
								$.unblockUI(); 
							});
							worker2.start();							
						}						
						//setAnchorButtonState('assignAccommButton', false);
					}
					var allRowsInGrid = $('#studentAccommGrid').jqGrid('getDataIDs');
					determineStudentSel(selectedStudentObjArr, 'assignAccommButton');

					if(submittedSuccesfully != ""){
			           	resetBulk();
			        }

			},
			onSelectRow: function (rowid, status) {
				var selectedRowId = rowid;
				var isRowSelected = $("#studentAccommGrid").jqGrid('getGridParam', 'selrow');
				if(status) {
					totalRowSelectedOnPage = totalRowSelectedOnPage+1;
					selectedStudentObjArr[selectedRowId]= parseInt(selectedRowId);
				} else {
					totalRowSelectedOnPage = totalRowSelectedOnPage-1;
					 delete selectedStudentObjArr[selectedRowId]; 
				}
				
				if(totalRowSelectedOnPage == studentArrId.length ) {
				 	$('#cb_studentAccommGrid').attr('checked', true);
				 } else {
				 	$('#cb_studentAccommGrid').attr('checked', false);
				 }
				 determineStudentSel(selectedStudentObjArr, 'assignAccommButton');
				  if(submittedSuccesfully != ""){
		           	 resetBulk();
		         }
			},
			loadComplete: function () {
				var isSAGridEmpty = false;
				if ($('#studentAccommGrid').getGridParam('records') === 0) {
					isSAGridEmpty = true;
            		$('#sp_1_studentAccommpager').text("1");
            		$('#next_studentAccommpager').addClass('ui-state-disabled');
            	 	$('#last_studentAccommpager').addClass('ui-state-disabled');
            	} else {
            		isSAGridEmpty = false;
            	}
            	
            	setEmptyListMessage('SelectAccommodation', isSAGridEmpty);
            	if(onNodechange){
            		onNodeChangeEmptyMsg = false;
            		onNodechange = false;
            	}
            	$.unblockUI();  
				$("#studentAccommGrid").setGridParam({datatype:'local'});
				var tdList = ("#studentAccommpager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				filterLoadFlag = true;
				if ($('#AssignAccommPopup').parents('.ui-dialog:visible').length && dataSubmited) {
					dataSubmited = false;
					var gridDOM = this;
					setTimeout(function () {
			            gridDOM.triggerToolbar();
			            editAccommodationClose();
			        }, 200);
					
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
function editAccommodationClose(){
	resetRadioAccommodation();
	$('#AssignAccommPopup').dialog('close');
	$(".blockUI").hide();
}
	
	 function setEmptyListMessage(requestedTab, isSAGridEmpty){
		 if(requestedTab == 'SelectAccommodation') {
		 if(isSAGridEmpty) {
			  if(onNodeChangeEmptyMsg) {
			 	$('#studentAccommGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 	$('#studentAccommGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/StudentWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsg").val()+"</td></tr></tbody></table></td></tr>");
			 } else {
			 	$('#studentAccommGrid').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
			 	$('#studentAccommGrid').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='8'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/StudentWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitle").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#filterNoStuSelected").val()+"</td></tr></tbody></table></td></tr>");
			 }
		 }
		 } 
	 }
	

 function gridReloadForBulkStudent(){ 
  		var demo1 = $('#selectedBox1').text();
  		 if(demo1.indexOf('Please select')!= -1){
			demo1 = "";
		 } else {
		 	
		 	var d1a = globalDemo1.split(">>");
		 	demo1 = $.trim(d1a[0])+"_"+$.trim(globalSubDemo1);
		 	//demo1 = $.trim(d1a[0])+"_"+$.trim(d1a[1]);
		 }
		 var demo2 = $('#selectedBox2').text();
  		 if(demo2.indexOf('Please select')!= -1){
			demo2 = "";
		 } else {
		 	var d1a = globalDemo2.split(">>");
		 	demo2 = $.trim(d1a[0])+"_"+$.trim(globalSubDemo2);
		 	//demo2 = $.trim(d1a[0])+"_"+$.trim(d1a[1]);
		 }
		 var demo3 = $('#selectedBox3').text();
  		 if(demo3.indexOf('Please select')!= -1){
			demo3 = "";
		 } else {
		 	var d1a = globalDemo3.split(">>");
		 	demo3 = $.trim(d1a[0])+"_"+$.trim(globalSubDemo3);
		 	//	demo3 = $.trim(d1a[0])+"_"+$.trim(d1a[1]);
		 }
		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.stuForOrgNodeId = $("#selectedBulkTreeOrgNodeId").val();
 		postDataObject.demoFilter1 = demo1;
 		postDataObject.demoFilter2 = demo2;
 		postDataObject.demoFilter3 = demo3;
 		//reset();
       jQuery("#studentAccommGrid").jqGrid('setGridParam',{datatype:'json',mtype:'POST'});     
 	  // var sortArrow = jQuery("#studentAccommGrid");
 	   
       jQuery("#studentAccommGrid").jqGrid('setGridParam', {url:'getStudentForSelectedNode.do',postData: postDataObject,page:1}).trigger("reloadGrid");
       jQuery("#studentAccommGrid").sortGrid('lastName',true,'asc');
     	//For MQC Defect - 67122
      // var arrowElements = sortArrow[0].grid.headers[0].el.lastChild.lastChild;
      // $(arrowElements.childNodes[0]).removeClass('ui-state-disabled');
       //$(arrowElements.childNodes[1]).addClass('ui-state-disabled');
      

  }
 
      
  function resetBulk (){
  	
  	document.getElementById('displayPopupMessage').style.display = "none";	
	document.getElementById('displayMessageMain').style.display = "none";	
	//document.getElementById('errorImg').style.display = "none";	
  }
  
  function openAssignAccommPopup(element){
 	if (isButtonDisabled(element))
		return true;
		
	isPopUp = true;
	 $("#AssignAccommPopup").dialog({  
		title:$("#selectAccomPopup").val(),  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '560px',
	 	modal: true,
	 	closeOnEscape: false,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 //$("#AssignAccommPopup").css('height',120);
		 var toppos = ($(window).height() - 475) /2 + 'px';
		 var leftpos = ($(window).width() - 550) /2 + 'px';
		 $("#AssignAccommPopup").parent().css("top",toppos);
		 $("#AssignAccommPopup").parent().css("left",leftpos);
		 resetBulk();	
  }
  
  var dataSubmited = false;
  
  function saveBulkStudentData(){
 
	var param;
	var showStudentInGrid = false;
	var studentIds = "";
	
	studentIds = JSON.stringify(selectedStudentObjArr);
	param = $("#AssignAccommPopup *").serialize();
	var dataToBeAdded  = getDataToBeAdded(param);
	param = param + "&studentIds="+studentIds;
	if(dataToBeAdded['isAccommodationSelected']) {
	$.ajax(
		{
				async:		false,//asynchronous calls do not sent post data in MAC safari
				beforeSend:	function(){
								$(".blockUI").show();
							},
				url:		'saveBulkStudentData.do',
				type:		'POST',
				data:		 param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){	
								  
								var errorFlag = data.errorFlag;
								var successFlag = data.successFlag;								
								
								if(successFlag) {
									$('#errorIcon').hide();
									$('#infoIcon').show();							
									var selectedStudentCount = data.additionalInfoMap.selectedStudentCount;
									dataSubmited = true;
									if(parseInt(selectedStudentCount) > 1000){	
										 submittedSuccesfully = dataToBeAdded;		
										 previousDataForpaging = selectedStudentObjArr;											 
										 filterLoadFlag = false;							
										 gridReloadForBulkStudent();
									}else {
									   var allRowsInGrid = $('#studentAccommGrid').jqGrid('getDataIDs');
							           for(var i=0; i<allRowsInGrid.length; i++) {
									 	 	jQuery("#studentAccommGrid").setRowData(selectedStudentObjArr[allRowsInGrid[i]], dataToBeAdded, "first");
									 	 	 $("#"+selectedStudentObjArr[allRowsInGrid[i]]+" td input").attr('checked', false).trigger('click').attr('checked', false);
									 	 	 submittedSuccesfully = dataToBeAdded;
										}
										
										previousDataForpaging = selectedStudentObjArr;									
										var alldata = $("#studentAccommGrid").jqGrid('getGridParam','data');
										if(alldata){
											for(var key in selectedStudentObjArr){
												var index = studentIdIndexer[key];
												if(alldata[index] != null && alldata[index] != undefined) {
													for(var prop in dataToBeAdded){
														alldata[index][prop] = dataToBeAdded[prop];
													}
												}
											}
											
										}									
										jQuery("#studentAccommGrid").jqGrid('setGridParam', { data: alldata,datatype:'local',page: 1 }).trigger("reloadGrid");
									}
									setBulkMessageMain(data.title, data.content, data.type, "");
									
									previousDataForpaging = selectedStudentObjArr;									
									
									document.getElementById('displayMessageMain').style.display = "block";
									selectedStudentObjArr = {};
									//call here again to disable button now because selectedStudentObjArr might be having other org's data  
									determineStudentSel(selectedStudentObjArr, "assignAccommButton"); 
									totalRowSelectedOnPage = 0;
								
									//$.unblockUI();			
									}
									else{
										if(data.type == 'alertMessage') {
											setPopupMessage();
										} else {
											setBulkMessageMain(data.title, data.content, data.type, "");
											$('#errorIcon').show();
											$('#infoIcon').hide();							
											document.getElementById('displayMessageMain').style.display = "block";	
											//document.getElementById('errorImg').style.display = "block";	
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
			$("#contentMain").text(content);
			//$("#messageBulkMain").text(message);
		}
		function setPopupMessage(){
			//$("#titleBulkPopup").text(BULK_ADD_TITLE);
			$("#contentBulkPopup").text($("#stuAssNoAccoID").val());
			//$("#messageBulkPopup").text("");
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
	var globalDemo1='';
	var globalDemo2='';
	var globalDemo3='';
	var globalSubDemo1='';
	var globalSubDemo2='';
	var globalSubDemo3='';
	
	function createDemoList(){
		var liMenuStr = "<div><ul class='sf-menu' id='menu'>";
		 for (var i=0; i<3; i++) {
		 	liMenuStr = liMenuStr + "<li class='selected roundedBorder' id='sf-menu-sub"+i+"'><a href='#' class='menuText' id='selectedBox"+parseInt(i+1)+"'>Please select</a><ul>";
		 	if(customerDemographicList.length > demomenuShowCount){
		 		if(i==0){
	 				showScrollMenu1 = true;
	 			}else if(i == 1){
	 				showScrollMenu2 = true;
	 			}else {
	 				showScrollMenu3 = true;
	 			}
		 	}
		 	
		 	if(i == 0 && showScrollMenu1){
		 		liMenuStr= liMenuStr + "<li class='scollSection'><a class='scrollUp' style='display:none;' href='#' onclick='scrollmenu(0,&#34;M&#34;,&#34;U&#34;,event);return false;'>&#x25B2;</a></li>";
		 	}
		 	
		 	if(i == 1 && showScrollMenu2){
		 		liMenuStr= liMenuStr + "<li class='scollSection'><a class='scrollUp' style='display:none;' href='#' onclick='scrollmenu(1,&#34;M&#34;,&#34;U&#34;,event);return false;'>&#x25B2;</a></li>";
		 	}
		 	
		 	if(i == 2 && showScrollMenu3){
		 		liMenuStr= liMenuStr + "<li class='scollSection'><a class='scrollUp' style='display:none;' href='#' onclick='scrollmenu(2,&#34;M&#34;,&#34;U&#34;,event);return false;'>&#x25B2;</a></li>";
		 	}
		 	
		 	liMenuStr = liMenuStr + "<li><a href='#' id='ps_"+parseInt(i+1)+"'>Please select</a>";
		 	for(var j=0; j<customerDemographicList.length; j++) {
		 		
		 		if(j>=demomenuShowCount){
		 			liMenuStr= liMenuStr + "<li class='hidemenu'><a href='#' id='"+customerDemographicList[j].id+"_"+parseInt(i+1)+"'>"+customerDemographicList[j].labelName+"</a>";
		 		}else {
		 			liMenuStr= liMenuStr + "<li class='showMenu'><a href='#' id='"+customerDemographicList[j].id+"_"+parseInt(i+1)+"'>"+customerDemographicList[j].labelName+"</a>";
		 		}
		 		var cDemoValues = customerDemographicList[j].customerDemographicValues;
		 		
		 		if (cDemoValues.length > 0){
		 			if(cDemoValues.length > demoSubmenuShowCount) {
		 				showSubMenuScroll = true;
		 			}else {
		 				showSubMenuScroll = false;
		 			}
		 			
		 			var innerUlId = i + "" + j;
		 			liMenuStr= liMenuStr + "<ul class='sf-menu-sub-level2' id='sf-menu-sub-level" +i + "" + j+"'>";
		 			if(showSubMenuScroll){
		 				liMenuStr= liMenuStr + "<li class='scollSection'><a class='scrollUp' style='display:none;' href='#' onclick='scrollmenu(&#34;"+innerUlId+"&#34;,&#34;S&#34;,&#34;U&#34;,event);return false;'>&#x25B2;</a></li>";
		 			}
		 			
		 			for(var k=0; k< cDemoValues.length; k++){
		 				if(k>=demoSubmenuShowCount){
		 					liMenuStr = liMenuStr + "<li class='hidemenu'><a href='#' id='"+cDemoValues[k].valueName+"'>"+cDemoValues[k].valueName+"</a></li>";
		 				}else {
		 					liMenuStr = liMenuStr + "<li class='showMenu'><a href='#' id='"+cDemoValues[k].valueName+"'>"+cDemoValues[k].valueName+"</a></li>";
		 				}
		 				
		 			}
		 			if(showSubMenuScroll){
		 				liMenuStr= liMenuStr + "<li class='scollSection'><a class='scrollDown'  href='#' onclick='scrollmenu(&#34;"+innerUlId+"&#34;,&#34;S&#34;,&#34;D&#34;,event);return false;'>&#x25BC;</a></li>";
		 			}
		 			
		 			liMenuStr= liMenuStr + "</ul>";
		 		}
		 		liMenuStr= liMenuStr + "</li>";
		 	}
		 	if(i == 0 && showScrollMenu1){
		 		liMenuStr= liMenuStr + "<li class='scollSection'><a class='scrollDown' href='#' onclick='scrollmenu(0,&#34;M&#34;,&#34;D&#34;,event);return false;'>&#x25BC;</a></li>";
		 	}
		 	
		 	if(i == 1 && showScrollMenu2){
		 		liMenuStr= liMenuStr + "<li class='scollSection'><a class='scrollDown' href='#' onclick='scrollmenu(1,&#34;M&#34;,&#34;D&#34;,event);return false;'>&#x25BC;</a></li>";
		 	}
		 	
		 	if(i == 2 && showScrollMenu3){
		 		liMenuStr= liMenuStr + "<li class='scollSection'><a class='scrollDown' href='#' onclick='scrollmenu(2,&#34;M&#34;,&#34;D&#34;,event);return false;'>&#x25BC;</a></li>";
		 	}
		 	
		 	liMenuStr= liMenuStr + "</ul></li>";
		 	
		 	
		 }
		 liMenuStr = liMenuStr + "</ul></div>";
		 liMenuStr = liMenuStr + "<div><input type='button' id='apply' onclick='javascript:applyList(); return false;'  class = 'ui-widget-header rounded' style='float:right;width:40px;margin:5px;height:20px;font-weight:bold;font-family:arial;cursor:pointer !important;' value='Apply'/>"+
		 			"	<input type='button' id='clear' onclick='javascript:clearList(); return false;' class = 'ui-widget-header rounded' value='Clear' style='float:right;width:40px;margin:5px;height:20px;font-weight:bold;font-family:arial;margin-right:0px;cursor:pointer !important;'/></div>";
		 return liMenuStr;
	}
	
	
	function scrollmenu(menuNo,menyType,action,e){
		// menuNo : 1st, 2nd or 3rd menu
		// menyType : menu(M) or submenu(S)
		//action: up(U) or down(D)
		disableHover = true;
		if(menyType == 'M'){
			var menuID = 'sf-menu-sub' + menuNo;
			var submenuUL = $("#" +menuID).children("ul");
		}else {
			var menuID = 'sf-menu-sub-level' + menuNo;
			var submenuUL = $("#" +menuID);
		}
		
		var scrollCount;
		
		if(action == 'D'){
			$(submenuUL).children("li.scollSection").children(".scrollUp").show();
				var count = $(submenuUL).children(".showMenu:first").prevAll(".hidemenu").length + $(submenuUL).children(".showMenu").length;
				if(menyType == 'M'){
					var downDiff = $(submenuUL).children(':not(.scollSection)').length - count - 1;
				}else {
					var downDiff = $(submenuUL).children(':not(.scollSection)').length - count;
				}
				
				if(downDiff > 0){
					if(downDiff<demomenuShowCount){
						scrollCount = downDiff;
					}else {
						scrollCount = demomenuShowCount;
					}
					for(var i =0;i<scrollCount;i++){
						$(submenuUL).children('.showMenu:first').removeClass("showMenu").addClass("hidemenu");
						$(submenuUL).children('.showMenu:last').next(".hidemenu").removeClass("hidemenu").addClass("showMenu");
					}
					if($(submenuUL).children(".showMenu:last").next("li.hidemenu").length == 0){
						$(submenuUL).children("li.scollSection").children(".scrollDown").hide();
					}
				}
		}else {			
			$(submenuUL).children("li.scollSection").children(".scrollDown").show();
			var count = $(submenuUL).children(".showMenu").length + $(submenuUL).children(".showMenu:last").nextAll(".hidemenu").length;
			if(menyType == 'M'){
				var upDiff = $(submenuUL).children(':not(.scollSection)').length - count - 1;
			}else {
				var upDiff = $(submenuUL).children(':not(.scollSection)').length - count;
			}
			
			if(upDiff < demomenuShowCount){
				scrollCount = upDiff;
			}else {
				scrollCount = demomenuShowCount;
			}
			for(var i =0;i<scrollCount;i++){
				$(submenuUL).children('.showMenu:last').removeClass("showMenu").addClass("hidemenu");
				$(submenuUL).children('.showMenu:first').prev(".hidemenu").removeClass("hidemenu").addClass("showMenu");
			}
			if($(submenuUL).children(".showMenu:first").prev("li.hidemenu").length == 0){
				$(submenuUL).children("li.scollSection").children(".scrollUp").hide();
			}
		}
		
		if(e.preventDefault){
			e.preventDefault();
		}else {
			e.returnValue = false;
			
		}		
		e.cancelBubble = true;
		
		window.setTimeout(function(){disableHover = false;},100);
		return false;
	}
	
	
	
	var demomenuShowCount = 12;
	var demoSubmenuShowCount = 8;
	var showScrollMenu1 = false;
	var showScrollMenu2 = false;
	var showScrollMenu3 = false;
	var showSubMenuScroll = false;
	var disableHover = false;
	
	var menuDiffTobeCheckedAgainst = (demoSubmenuShowCount)*32;
	function loadMenu()
	{	
	$('#menu').superfish({
		delay:10,
		speed:'fast',
		onBeforeShow:  function(){ 
			if($(this).hasClass('sf-menu-sub-level2') && $(this).parents("#sf-menu-sub2").length > 0){
				$(this,"#sf-menu-sub2").css('left',parseInt(-1 * $(this).parent().outerWidth()) + 'px');
			}
			if($(this).hasClass('sf-menu-sub-level2')){
				var subMenuOffSet = $(this).parent().offset().top;
				var menuOffset=$('ul.sf-menu').offset().top;
				var diff = subMenuOffSet - menuOffset;
				if(diff > menuDiffTobeCheckedAgainst){
					var subMenuHght = $(this).height() - $(this).parent().outerHeight();
					$(this).css('top',parseInt(-1 * subMenuHght)+'px');
				}
			}
			$(this).children("li.scollSection").children(".scrollUp").hide();
			$(this).children("li.scollSection").children(".scrollDown").show();
			
			if($(this).hasClass('sf-menu-sub-level2')){
				var childrenLength = $(this).children(':not(.scollSection)').length;
				if(childrenLength>(demoSubmenuShowCount+1)){
					$(this).children(':not(.scollSection)').slice(1,demoSubmenuShowCount+1).addClass('showMenu').removeClass('hidemenu');
					$(this).children(':not(.scollSection)').slice(demoSubmenuShowCount+1,childrenLength).addClass('hidemenu').removeClass('showMenu');
				}
			}else {
				var childrenLength = $(this).children(':not(.scollSection)').length;
				if(childrenLength>(demomenuShowCount+1)){
					$(this).children(':not(.scollSection)').slice(1,demomenuShowCount+1).addClass('showMenu').removeClass('hidemenu');
					$(this).children(':not(.scollSection)').slice(demomenuShowCount+1,childrenLength).addClass('hidemenu').removeClass('showMenu');
				}
			}
		}	
	}).delegate("li[class!='scollSection'] a","click",function(e) {
		
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
			if(selectedFullId != undefined) {
				var selectedId = selectedFullId.split('_');
				if(selectedId[1] == '1'){
					globalDemo1= pTxt;
					globalSubDemo1= tooltip;
				}
				if(selectedId[1] == '2'){
					globalDemo2= pTxt;
					globalSubDemo2= tooltip;
				}
				if(selectedId[1] == '3'){
					globalDemo3= pTxt;
					globalSubDemo3= tooltip;
				}
			}	
			
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
			if(selectedFullId != undefined) {
				var selectedId = selectedFullId.split('_');
				if(selectedId[1] == '1'){
					globalDemo1= tooltip;
					globalSubDemo1= "All";
				}
				if(selectedId[1] == '2'){
					globalDemo2= tooltip;
					globalSubDemo2= "All";
				}
				if(selectedId[1] == '3'){
					globalDemo3= tooltip;
					globalSubDemo3= "All";
				}
			}	
			
		}
		if(selectedFullId != undefined) {
			var selectedId = selectedFullId.split('_');
				
			if(selectedFullId.indexOf('ps_') != -1){
				if(selectedId[1] == '1'){
					$("[id^='" + globalCustomerDemographic1+"_']").parent().show();
				}
				if(selectedId[1] == '2'){
					$("[id^='" + globalCustomerDemographic2+"_']").parent().show();
				}
				if(selectedId[1] == '3'){
					$("[id^='" + globalCustomerDemographic3+"_']").parent().show();
				}
			}else {	
				if(selectedId[1] == '1'){					
					$("[id^='" + globalCustomerDemographic1+"_']").parent().show();
					globalCustomerDemographic1 = selectedId[0];
					$('#'+selectedId[0]+"_2").parent().hide();
					$('#'+selectedId[0]+"_3").parent().hide();
				}
				if(selectedId[1] == '2'){
					$("[id^='" + globalCustomerDemographic2+"_']").parent().show();
					globalCustomerDemographic2 = selectedId[0];
					$('#'+selectedId[0]+"_1").parent().hide();
					$('#'+selectedId[0]+"_3").parent().hide();
				}
				if(selectedId[1] == '3'){
					$("[id^='" + globalCustomerDemographic3+"_']").parent().show();
					globalCustomerDemographic3 = selectedId[0];
					$('#'+selectedId[0]+"_1").parent().hide();
					$('#'+selectedId[0]+"_2").parent().hide();
				}
			}
		}			
	});	
	Nifty("li.roundedBorder","normal top same-height");
	
	
}
	function clearList(){
		 $("#t_studentAccommGrid").html(listStr).height(30);
		 loadMenu();
	}
	
	function applyList(){
		UIBlock();
		filterLoadFlag = false;
		applyChange = true;
		gridReloadForBulkStudent();
		filterTimer = setTimeout("applyFilter()", 200);
		//clearFilterDropDown();
		//$.unblockUI();   
	}
	function applyFilter() {
		if(filterLoadFlag) {
			$('#gs_grade').trigger('change');
			$('#gs_calculator').trigger('change');
			$('#gs_hasColorFontAccommodations').trigger('change');
			$('#gs_testPause').trigger('change');
			$('#gs_screenReader').trigger('change');
			$('#gs_untimedTest').trigger('change');
			$('#gs_highLighter').trigger('change');
		} else {
			filterTimer = setTimeout("applyFilter()", 200);
		}
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
	 	var param = $("#AssignAccommPopup *").serialize();
	 	var dataToBeAdded  = getDataToBeAdded(param);
		if(dataToBeAdded['isAccommodationSelected']){
			 $("#unSaveConfirmationPopup").dialog({  
			title:$("#confirmAlert").val(),  
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
	 //Fix for 68188
	  function clearFilterDropDown (){
	  		$("option:contains('Any')",'#gs_grade' ).attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_calculator').attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_hasColorFontAccommodations').attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_testPause').attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_screenReader').attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_untimedTest').attr("selected", "selected");
	    	$("option:contains('Any')",'#gs_highLighter').attr("selected", "selected");
	  }
	  
	  
	   function skipStudent(grade, calculator, hasColorFontAccommodations, testPause, 
	   						screenReader, untimedTest, highLighter, studentId, allData){
	  	 var skip = false;
	  	 var objIndex = jQuery.inArray(studentId, studentArrId); 
		 var obj = allData[objIndex];
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
	  
	  
	function resetAccommpopup()   {
		resetBulk();
		resetRadioAccommodation();
	}
	
	//Implementation of the two runnables or the process
	function ba_runnable(studentArr,args){
		if(studentArr !=""){
		var skip = skipStudent(args.grade, args.calculator, args.hasColorFontAccommodations, args.testPause,
											 args.screenReader, args.untimedTest, args.highLighter, studentArr, args.allData);
		if(!skip) {
			delete selectedStudentObjArr[studentArr];
			}		
		}								
	}
	
	
	function ba_runnable1(studentArr,args){
		if(studentArr !="") {
		var skip = skipStudent(args.grade, args.calculator, args.hasColorFontAccommodations, args.testPause,
											 args.screenReader, args.untimedTest, args.highLighter, studentArr, args.allData);
		 	if(!skip) {
			  	selectedStudentObjArr[parseInt(studentArr)]= parseInt(studentArr);
			  	totalRowSelectedOnPage = totalRowSelectedOnPage + 1;
		  	} else {
		  		delete selectedStudentObjArr[studentArr];
		  	}	
		}							
	}
	
	function runnableOnComplete(){	
		determineStudentSel(selectedStudentObjArr, 'assignAccommButton');
		if(submittedSuccesfully != ""){
		   	resetBulk();
		  }
	}
	function stopSelectAll(){		
						//The below condition is used to stop select all in grid if the grid is empty
			if(studentArrId == undefined || studentArrId.length <=0) {
				var noStudents = $('.ui-state-highlight');
					if(noStudents.length > 0) {
						for(var k = 0; k < noStudents.length; k++) {
							$(noStudents[k]).removeClass('ui-state-highlight');
							}
						}
					}
			if(studentArrId.length == 1 && studentArrId[0] == "") {
				var noStudents = $('.ui-state-highlight');
					if(noStudents.length > 0) {
						for(var k = 0; k < noStudents.length; k++) {
							$(noStudents[k]).removeClass('ui-state-highlight');
						}
					}
				}
			var present1 = false;
			for(var key in selectedStudentObjArr) {
				present1 = true;
				break;
			}
			if(!present1) {
				var noStudents = $('.ui-state-highlight');
				if(noStudents.length > 0) {
					for(var k = 0; k < noStudents.length; k++) {
						$(noStudents[k]).removeClass('ui-state-highlight');
					}
				}
			}	
	}