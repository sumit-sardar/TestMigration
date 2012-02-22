var selectStudentgridLoaded = false;
var allRowSelected = false;
var selectedSession = [];
var unCheckedSession = [];
var categoriesStr = ":All;JV:JV;AD:AD"; 
var AccommOption = ":Any;T:Yes;F:No";
var addedOrg={status:"true"};

var AddStudentLocaldata ={};
var isOnBack = false;
var stuForSelectedOrg;
var preSelectedOrg;
var delStuIdObjArray = [];
var orgForDupStu = [];
var studentWithaccommodation = 0;
var allStudentIds = [];
var allFilteredStudentRow = [];
var studentGradesCustomerConfig = [];
var allSelectOrg = [];
var allSelectOrgTmp = [];// stores org node whose all node checked 
var countAllSelect = 0;
var studentMap = new Map();
var visitedNodeCounter = new Map(); /// has to be flush select student is clicked
var visitedNodeTraverseCounter = new Map();// after selecting a node this will track paging/sorting/filtering
var studentTempMap = new Map();
var deleteStudentCounter = 0;
/// FOR FILTER
var isNodeChanged = false;
var defaultGrade 		= "";
var defaultCalculator 	= "";
var defaultHasColorFont = "";
var defaultTestPause 	= "";
var defaultScreenReader = "";
var defaultUntimedTest 	= "";

var prvGrade 		= "";
var prvCalculator 	= "";
var prvHasColorFont = "";
var prvTestPause 	= "";
var prvScreenReader = "";
var prvUntimedTest 	= "";
var isPagingEvent  = false;
var isSortingEvent = false;
var filterAppliedOrgMap = new Map();
var studentEditStatusMap = new Map(); //changes for ie issue

/// FOR FILTER

function showSelectStudent(){
	$("#studentAddDeleteInfo").hide();
	selectedSubtestId = selectedTestId;
	$("#Student_Tab").css('display', 'none');
	$("#Select_Student_Tab").css('display', 'block');
	$("#selectStudentPager").css('display', 'none');
	if(orgTreeHierarchy == "" || orgTreeHierarchy ==undefined) {
		populateStuTree();
	} else {
		loadInnerStuOrgTree();
	}
		allSelectOrgTmp = allSelectOrg.slice(0);
		visitedNodeCounter = new Map();
		visitedNodeTraverseCounter = new Map();
	
}
// when back button invoked
function hideSelectStudent(){
	isOnBack = true;
	if(AddStudentLocaldata == undefined || AddStudentLocaldata.length == 0)
		studentWithaccommodation = 0;
	cloneStudentMapToTemp();
	hideSelectStudentPopup();
	allSelectOrg = allSelectOrgTmp.slice(0);
	$("#stuOrgNodeHierarchy").undelegate();
}

function hideSelectStudentPopup() {
	$('#totalStudent').text(AddStudentLocaldata.length);
	if($("#supportAccommodations").val() != 'false')
	 	 $('#stuWithAcc').text(studentWithaccommodation);
	$("#Student_Tab").css('display', 'block');
	$("#Select_Student_Tab").css('display', 'none');
	$("#selectStudentPager").css('display', 'none');
}

function loadInnerStuOrgTree() {
	//if(!isOnBack) {
		createinnSingleNodeSelectedTree (orgTreeHierarchy);
	//} else{
	//	$("#stuOrgNodeHierarchy").jstree("close_all",-1);
		$("#selectStudent").GridUnload();
		selectStudentgridLoaded = false;
	//}
	$("#innerSearchheader").css("visibility","visible");	
	$("#stuOrgNodeHierarchy").css("visibility","visible");	

}

function populateStuTree() {
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						UIBlock();
					},
		url:		'userOrgNodeHierarchyList.do',
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
							orgTreeHierarchy = data;
							jsonData = orgTreeHierarchy.data;
							getRootNodeDetails();
						createinnSingleNodeSelectedTree (orgTreeHierarchy);
						$("#innerSearchheader").css("visibility","visible");	
						$("#stuOrgNodeHierarchy").css("visibility","visible");	
						$.unblockUI(); 						
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						//$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
		
	});

}

function createinnSingleNodeSelectedTree(jsondata) {
	   $("#stuOrgNodeHierarchy").jstree({
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
	     registerDelegate("stuOrgNodeHierarchy");
	     
		    $("#stuOrgNodeHierarchy").delegate("a","click", function(e) {
		    	preSelectedOrg = stuForSelectedOrg;
		    	stuForSelectedOrg = $(this).parent().attr("id");
	 		    $("#stuForOrgNodeId").val(stuForSelectedOrg);
		    	var topNodeSelected = $(this).parent().attr("cid");
		    	if(topNodeSelected == leafNodeCategoryId) {
	    		if(!selectStudentgridLoaded) {
		    		populateSelectStudentGrid();
		    	} else  
		    		gridReloadSelectStu();
		    	}
		    	 if(selectStudentgridLoaded) {
			    	var width = jQuery("#scheduleSession").width();
				   	width = width - 72; // Fudge factor to prevent horizontal scrollbars
				   
					var showAccommodations = $("#supportAccommodations").val();
					if(showAccommodations  == 'false') {
						$("#selectStudent").jqGrid("hideCol",["calculator","hasColorFontAccommodations","testPause","screenReader","untimedTest"]); 
					} else {
						$("#selectStudent").jqGrid("showCol",["calculator","hasColorFontAccommodations","testPause","screenReader","untimedTest"]); 
					}
					$("#selectStudent").jqGrid("hideCol",["orgNodeId","orgNodeName","hasAccommodations"]);
					jQuery("#selectStudent").setGridWidth(width,true);
					$("#selectStudentPager").css('display', 'block');
				}
				
		   });
		   
		   $("#stuOrgNodeHierarchy").bind("loaded.jstree", 
		 	function (event, data) {
		 		for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#stuOrgNodeHierarchy ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
	  
	  	  
}

function imageFormat( cellvalue, options, rowObject ){
	if(cellvalue == 'T')
		return "Yes";
	else
		return "No";
		
} 

function selectFormat( cellvalue, options, rowObject ){
		var orgArrayList = 	orgForDupStu[rowObject.studentId];
		var optList = "<select id='dupStu"+rowObject.studentId +"' style = 'width: 190px;'>" ;
		var optionList = "";
		for(var key in orgArrayList){
			if(key != undefined) {
				if($.trim(orgArrayList[key]).length > 25)
					optionList = "<option title = \""+$.trim(orgArrayList[key])+"\" value='"+key+"'>"+$.trim(orgArrayList[key])+"</option>" + optionList;
				else
					optionList = "<option value='"+key+"'>"+$.trim(orgArrayList[key])+"</option>" + optionList;
			}
		}    
		optList = optList + optionList + "</select> " ;
		return optList;
}

function dupDropChanged(studentIdDrop) {
	var textValue = $("#dupStu"+studentIdDrop+" option:selected").text();
	if(textValue.length > 25) {
		$("#dupStu"+studentIdDrop).attr('title',textValue);
	} else {
		$("#dupStu"+studentIdDrop).removeAttr('title');
	}
}

function populateSelectStudentGrid() {
 		UIBlock();
 		if(blockOffGradeTesting != null && blockOffGradeTesting)		
 			populateGradeLevelFilter();
 		selectStudentgridLoaded = true;
 		var studentIdTitle = $("#studentIdLabelName").val();
 		var calculator= '<img src="/SessionWeb/resources/images/calc.PNG" title="Calculator"/>';
 		var colorFont= '<img src="/SessionWeb/resources/images/colorfont.PNG" title="Color/Font"/>';
 		var testPause= '<img src="/SessionWeb/resources/images/pause.PNG" title="Pause"/>';
 		var screenReader= '<img src="/SessionWeb/resources/images/screenreader.PNG" title="Reader"/>';
 		var untimedTest= '<img src="/SessionWeb/resources/images/untimed.PNG" title="Untimed"/>';
 		var status = 'Status'+""+'<img id=statusLegend src=/SessionWeb/resources/images/questionmark.jpg onmouseover=showStatusLegend(event); onmouseout=hideStatusLegend(); />'  
 		reset();
 		var postDataObject = {};
 		postDataObject.q = 2;
 		postDataObject.stuForOrgNodeId = $("#stuForOrgNodeId").val();
 		postDataObject.selectedTestId = $("#selectedTestId").val();
 		postDataObject.blockOffGradeTesting = blockOffGradeTesting;
 		postDataObject.selectedLevel = selectedLevel;
 		
 		if(state == "EDIT"){
	       	postDataObject.testAdminId = selectedTestAdminId;
	    }
       $("#selectStudent").jqGrid({         
          url: 'getStudentForList.do', 
		  mtype:   'POST',
		  datatype: "json",
		  postData: postDataObject, 
          colNames:[ $("#testStuLN").val(),$("#testStuFN").val(), $("#testStuMI").val(), studentIdTitle, 'Organization','orgName','Accommodation', $("#testDetGrade").val(), status, calculator, colorFont, testPause, screenReader, untimedTest, "StatusCopyable", "ItemSetForm","ExtendedTimeAccom","StatusEditable","StudentId", "ToolTip", "outOfSchool"],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:90, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:90, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:35, editable: true, align:"left",sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'extPin1',index:'extPin1', width:110, editable: true, align:"left", sortable:true, search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeId',index:'orgNodeId',editable: false, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeName',index:'orgNodeName',editable: false, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations',editable: false, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade', width:50, editable: true, align:"center", sortable:true, search: true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal"' }, stype: 'select', searchoptions:{ sopt:['eq'], value: categoriesStr }},
		   		{name:'status.code',index:'code',editable: true, width:50, align:"left", sortable:false, search: false, formatter:addToolTipFmatter, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'calculator',index:'calculator', width:38, editable: true, align:"center", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'hasColorFontAccommodations',index:'hasColorFontAccommodations',editable: true, width:38, align:"center", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'testPause',index:'testPause',editable: true, width:38, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'screenReader',index:'screenReader',editable: true, width:38, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'untimedTest',index:'untimedTest',editable: true, width:38, align:"center", sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }, formatter:imageFormat, stype:'select', editoptions:{value:AccommOption} },
		   		{name:'statusCopyable',index:'statusCopyable',hidden:true,editable: true, width:38, editable: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'itemSetForm',index:'itemSetForm',hidden:true,editable: true, width:38, editable: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'extendedTimeAccom',index:'extendedTimeAccom',hidden:true,editable: true, width:38, editable: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'statusEditable',index:'editable1',hidden:true,editable: true, width:38, editable: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'studentId',index:'studentId',hidden:true,editable: true, width:38, editable: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'extPin2',index:'extPin2',hidden:true,editable: true, width:38, editable: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'outOfSchool',index:'outOfSchool',hidden:true,editable: true, width:38, editable: true, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } }
		   	],
		   		jsonReader: { repeatitems : false, root:"studentNode", id:"studentId",
		   	records: function(obj) {
		   	$('#gs_calculator').val("");
		   	$('#gs_hasColorFontAccommodations').val("");
		   	$('#gs_testPause').val("");
		   	$('#gs_screenReader').val("");
		   	$('#gs_untimedTest').val("");

		   	accomodationMap = obj.accomodationMap;
		   	 isNodeChanged = true;
		   	 if(visitedNodeCounter.get(stuForSelectedOrg) != null){
			   	  var vcounter = visitedNodeCounter.get(stuForSelectedOrg);
			   	  vcounter = parseInt(vcounter)+parseInt(1);
			   	  visitedNodeCounter.put(stuForSelectedOrg,vcounter);
		   	 } else {
		   	 		visitedNodeCounter.put(stuForSelectedOrg,0);
		   	 }
		   	 	allStudentIds 			= [];
		   	 	allFilteredStudentRow	= [];
		   	 	if(obj.studentNode != null && obj.studentNode.length>0) {
		   	 		allStudentIds = allStudentIds.concat(obj.studentNode);
		   	 		allFilteredStudentRow = allFilteredStudentRow.concat(obj.studentNode);
		   	 	}
		   	 	studentEditStatusMap = new Map(); //changes for ie issue
		   	 	for(var ii=0; ii<obj.studentNode.length;ii++){
		   	 	   var data1= obj.studentNode[ii];
		   	 	   studentEditStatusMap.put(data1.studentId,data1.statusEditable );
		   	 	}

		   	 if(blockOffGradeTesting == null || blockOffGradeTesting == undefined || blockOffGradeTesting == "" || !blockOffGradeTesting) {
		   	 	studentGradesCustomerConfig = obj.gradeList;
 			 	populateGradeLevelFilter();
 			 }
		   	 } },
		   	loadui: "disable",
			rowNum:10,
			loadonce:true, 
			multiselect:true,
			pager: '#selectStudentPager', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 162,  
			caption:$("#stuListGrid").val(),
			//toolbar: [true,"top"],
			onPaging: function() {
				var reqestedPage = parseInt($('#selectStudent').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_selectStudentPager').text());
				var minPageSize = 1;
				isPagingEvent  = true;
				if(reqestedPage > maxPageSize){
					$('#selectStudent').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#selectStudent').setGridParam({"page": minPageSize});
				}
				
				if(allRowSelected) {
					var sessions  = jQuery('#selectStudent').jqGrid('getGridParam','selarrrow');	
					if(selectedSession.length > 0) {
					var existlen = selectedSession.length;
					for(var i=0; i<sessions.length; i++) {
						if(!include(selectedSession, sessions[i])) {
							selectedSession[existlen] = sessions[i] ;
							existlen++;
						}
					}
					 var allRowsInGrid = $('#selectStudent').jqGrid('getDataIDs');
					
					} else {
						selectedSession = sessions;
					} 
				}
			},
			onSortCol : function(index, columnIndex, sortOrder) { 
				isSortingEvent = true;
			}, 
			gridComplete: function() {
				var allRowsInGridPresent = $('#selectStudent').jqGrid('getDataIDs');
				if(state=="EDIT"){
					for(var k = 0; k < allRowsInGridPresent.length; k++) {
						var selectedRowData = $("#selectStudent").getRowData(allRowsInGridPresent[k]);
						if(editDataMrkStds.get(selectedRowData.studentId) == selectedRowData.studentId){
							$("#"+selectedRowData.studentId+" td input","#selectStudent").attr("disabled", true);
				 			$("#"+selectedRowData.studentId, "#selectStudent").addClass('ui-state-disabled');
						}				
					}				
				} 
				
				//Added for out of school students
				for(var kk = 0; kk < allRowsInGridPresent.length; kk++) {
					var selectedRowData = $("#selectStudent").getRowData(allRowsInGridPresent[kk]);
					if(selectedRowData.outOfSchool == "Yes"){
						$("#"+selectedRowData.studentId+" td input","#selectStudent").attr("disabled", true);
				 		$("#"+selectedRowData.studentId, "#selectStudent").addClass('ui-state-disabled');
					}				
				}
			
			// For repopulating filtered data
			if(isNodeChanged) {
			 	setDefaultGridFilter();
			}
			 if(visitedNodeTraverseCounter.get(stuForSelectedOrg) != null){
				 var vcounter = visitedNodeTraverseCounter.get(stuForSelectedOrg);
				 vcounter = parseInt(vcounter)+parseInt(1);
				 visitedNodeTraverseCounter.put(stuForSelectedOrg,vcounter);
			 } else {
			   	visitedNodeTraverseCounter.put(stuForSelectedOrg,0);
			  }
    		if(!isDefaultFilterUpdated()) {
				allFilteredStudentRow = allStudentIds;
			} else {
				updateFilteredStudentData();
			}
	   	  	
			var allRowChecked = false;
			if(allSelectOrg != null && allSelectOrg.length > 0) {
				for(var i = 0; i < allSelectOrg.length; i++) {
				    if(allSelectOrg[i] == null || allSelectOrg[i] == undefined) {
				    	continue;
				    }
					if(allSelectOrg[i] != null && (allSelectOrg[i] == stuForSelectedOrg ))
						allRowChecked = true;
					 else if(!isFilterUpdated() && isDefaultFilterUpdated() && ( allSelectOrg[i] == stuForSelectedOrg+"_f"))
					 	allRowChecked = true;
				}
			}
			if(allRowChecked ) { 
				 $("#cb_selectStudent").attr("checked", true);
				 $("#cb_selectStudent").trigger('click');
				 $("#cb_selectStudent").attr("checked", true);
				 allRowSelected = true;
				 updateUI(); // for disabling restricted student
			 } else if(visitedNodeTraverseCounter.get(stuForSelectedOrg) == 0) {
			 
			 	if(AddStudentLocaldata != null && AddStudentLocaldata.length > 0 ) {
						$('.cbox').attr('checked', false); 
						for(var i = 0; i < AddStudentLocaldata.length; i++) {
							var stuObj = AddStudentLocaldata[i];
							if(stuObj != null && stuObj != undefined) {
								var orgArray = 	String(stuObj.orgNodeId);
								if(orgArray == stuForSelectedOrg) {
									$("#"+stuObj.studentId+" td input").attr("checked", true);
									$("#"+stuObj.studentId).trigger('click');
									$("#"+stuObj.studentId+" td input").attr("checked", true); 
								} else {
									$("#"+stuObj.studentId+" td input").attr("checked", false);
									//$("#"+stuObj.studentId).trigger('click');
									//$("#"+stuObj.studentId+" td input").attr("checked", false);
								}
							} 					
						}
					}
			 
			 
			 } else { // if next time read from already loaded data
			    
			    $('.cbox').attr('checked', false); 
					var allRowsInGridHere = $('#selectStudent').jqGrid('getDataIDs');
			 		for(var i = 0; i < allRowsInGridHere.length; i++) {
						var stdData = studentTempMap.get(allRowsInGridHere[i]);
						if(stdData != null && stdData != undefined) {
							var orgArray = 	String(stdData.orgNodeId);
							if(orgArray.indexOf(String(stuForSelectedOrg)) >= 0 && String(allRowsInGridHere[i]) == String(stdData.studentId)) {
								$("#"+allRowsInGridHere[i]+" td input").attr("checked", true);
								$("#"+allRowsInGridHere[i]).trigger('click');
								$("#"+allRowsInGridHere[i]+" td input").attr("checked", true);
							}
						}
					}
			 
			 
			 
			    /// update if any scenario exists

			 }
			 isNodeChanged = false;
			 isSortingEvent = false;
			 isPagingEvent  = false;
			},
			onSelectAll: function (rowIds, status) {
			    UIBlock();
				var isFilteredApplied = isDefaultFilterUpdated();
				if(status) {
					allRowSelected = true;
					for(var i = 0; i < allFilteredStudentRow.length; i++) {						
						if(studentTempMap.length() == 0) {
							if (!addToStudentTempMap(allFilteredStudentRow[i].studentId,allFilteredStudentRow[i])){
							 	$("#"+allFilteredStudentRow[i].studentId+" td input").attr("checked", false);
							}
						} else {
							if (studentTempMap.get(allFilteredStudentRow[i].studentId) == null || studentTempMap.get(allFilteredStudentRow[i].studentId) == undefined) {
								if(!addToStudentTempMap(allFilteredStudentRow[i].studentId,allFilteredStudentRow[i])){
									$("#"+allFilteredStudentRow[i].studentId+" td input").attr("checked", false);
								}
							} else {
								// Added to handle duplicate students
								var studentData = studentTempMap.get(allFilteredStudentRow[i].studentId);
								var orgList = String(studentData.orgNodeId);
								var orgListName = studentData.orgNodeName;
								var orgListAll = String(allFilteredStudentRow[i].orgNodeId);
								var orgListAllName = String(allFilteredStudentRow[i].orgNodeName);
								if(orgList.indexOf(orgListAll) == -1) {
								    if(addToStudentTempMap(studentData.studentId, studentData)) {
									    orgList = orgList + "," + orgListAll;
										orgListName = orgListName + "," + orgListAllName;
										studentData.orgNodeId = orgList;
										studentData.orgNodeName = orgListName;
								    
								    } else {
								    	$("#"+studentData.studentId+" td input").attr("checked", false);
								    }
								}
							}
						}
					}	
					// Added to handle multiple organization select All	
					var present = false;
					if(allSelectOrg != null && allSelectOrg != undefined ) {
						for(var i = 0; i < allSelectOrg.length; i++) {
							if(allSelectOrg[i] != null && (allSelectOrg[i] == stuForSelectedOrg || allSelectOrg[i] == stuForSelectedOrg+"_f")){
								if(isFilteredApplied){
									 allSelectOrg[i] = stuForSelectedOrg+"_f";
								} else {
									allSelectOrg[i] = stuForSelectedOrg;
								}
								present = true;
							}
								
						}
						if(!present) {
							if(isFilteredApplied) {
								allSelectOrg[countAllSelect] = stuForSelectedOrg+"_f";
							} else {
								allSelectOrg[countAllSelect] = stuForSelectedOrg;
							}
							countAllSelect++;
						}
					} else {
							if(isFilteredApplied) {
								allSelectOrg[countAllSelect] = stuForSelectedOrg+"_f";
							} else {
								allSelectOrg[countAllSelect] = stuForSelectedOrg;
							}
							countAllSelect++;
					}			
				} else {
					allRowSelected = false;	
					for(var i = 0; i < allFilteredStudentRow.length; i++) {
						var studIdVal = studentTempMap.get(allFilteredStudentRow[i].studentId);
						if(studIdVal != null && studIdVal != undefined) {
							deleteStudentFromTemp(stuForSelectedOrg, studIdVal, allFilteredStudentRow[i].studentId, studentTempMap);
							
						}
					}
					for(var i = 0; i < countAllSelect; i++) {
						if(allSelectOrg[i] != null && (allSelectOrg[i] == stuForSelectedOrg || allSelectOrg[i] == stuForSelectedOrg+"_f")) {
							allSelectOrg.splice(i,1);
							countAllSelect--;
						}
					}	
													
				}
				$.unblockUI(); 
			},
			onSelectRow: function (rowid, status) {
				var selectedRowId = rowid;
				var selectedRowData = $("#selectStudent").getRowData(selectedRowId);
				
				if(status) {
					if(studentTempMap.length() == 0) {
						addToStudentTempMap(selectedRowId,selectedRowData);
					} else {					
						var studentDataVal = studentTempMap.get(selectedRowId);
						if(studentDataVal == null || studentDataVal == undefined) {
							if(!addToStudentTempMap(selectedRowId,selectedRowData)){
							  $("#"+selectedRowId+" td input").attr("checked", false);
							}
						} else {
							// Added to handle duplicate students
							var orgList = String(studentDataVal.orgNodeId);
							var orgListName = studentDataVal.orgNodeName;
							var orgListAll = String(selectedRowData.orgNodeId);
							var orgListAllName = String(selectedRowData.orgNodeName);
							if(orgList.indexOf(orgListAll) == -1) {
							     if(addToStudentTempMap(studentDataVal.studentId, studentDataVal)) {
								     orgList = orgList + "," + orgListAll;
									 orgListName = orgListName + "," + orgListAllName;
									 studentDataVal.orgNodeId = orgList;
									 studentDataVal.orgNodeName = orgListName;
							     
							     } else {
							     	$("#"+studentDataVal.studentId+" td input").attr("checked", false);
							     }
							}
						}
					}
				} else {
					var studentIdVal = studentTempMap.get(selectedRowId);
					if(studentIdVal != null && studentIdVal != undefined) {
						deleteStudentFromTemp(stuForSelectedOrg, studentIdVal, selectedRowId, studentTempMap);
					}
					for(var i = 0; i < allSelectOrg.length; i++) {
						if(allSelectOrg[i] != null && allSelectOrg[i] == stuForSelectedOrg || allSelectOrg[i] == stuForSelectedOrg+"_f")
							allSelectOrg.splice(i,1);
					}
					$("#cb_selectStudent").attr("checked", false);
				}
			},
			loadComplete: function () {
				if ($('#selectStudent').getGridParam('records') === 0) {
					isPAGridEmpty = true;
            		$('#sp_1_selectStudentPager').text("1");
            		$('#next_selectStudentPager').addClass('ui-state-disabled');
            	 	$('#last_selectStudentPager').addClass('ui-state-disabled');
            	 	$('#selectStudent').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
		 			$('#selectStudent').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='6'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/SessionWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#noStudentTitleGrd").val()+"</th></tr><tr width='100%'><td colspan='6'>"+$("#noStudentMsgGrd").val()+"</td></tr></tbody></table></td></tr>");
            	} else {
            		var allRowsInGridHere = $('#selectStudent').jqGrid('getDataIDs');
			 		for(var ii = 0; ii < allRowsInGridHere.length; ii++) {
			 		    //var selectedRowData = $("#selectStudent").getRowData(String(allRowsInGridHere[ii]));
			 		    //var statEditable = selectedRowData ["status.editable"];
                        var statEditable = studentEditStatusMap.get(allRowsInGridHere[ii]); // changes for if issue
			 		     if(statEditable == "F"){ 
					 		var trs = $('#gview_selectStudent tr');
					 		var node;
					 		for(count=0; count<trs.length; count++) {
					 		   if(count<2)
					 		     continue;
					 			node = trs.eq(count);
					 			if(node.attr("id") == allRowsInGridHere[ii]){
					 			  node.addClass('ui-state-disabled');
					 			  node.addClass('removeOpacity');
					 			  //node[0].setAttribute("disabled","disabled");//disabling this way works in IE also
					 			  //node[0].setAttribute("title", node.attr("id"));//tooltip for ses
					 			   if(node[0].children.length>0 && node[0].children[0].children.length>0 && node[0].children[0].children[0].getAttribute("type")== "checkbox"){
					 			   	 node[0].children[0].children[0].setAttribute("disabled","disabled");
					 			   
					 			   }
					 			}
					 		}
					 		//  $('#selectStudent').setCell(allRowsInGridHere[ii], 5, '', {color:'blue'},{ title: selectedRowData.extPin2});
			 		     }

			 		}
            		isPAGridEmpty = false;
            	}
            	
            	$.unblockUI();  
				//$("#selectStudent").setGridParam({datatype:'local'});
				var tdList = ("#selectStudentPager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#selectStudent").jqGrid('navGrid','#selectStudentPager',{edit:false,add:false,del:false,search:false,refresh:false});
	 jQuery("#selectStudent").jqGrid('filterToolbar');
	 $("thead:first tr.ui-jqgrid-labels", $("#selectStudent")[0].grid.hDiv).height('28px'); // Changed for increasing height of header of grid
	 
	 //Changes to block off grade testing
		if(blockOffGradeTesting) {
			if(selectedLevel.length > 2)
				$("#gs_grade").attr('disabled', false);
			else
				$("#gs_grade").attr('disabled', true);
		}
	 	$("#gs_grade").attr("style", "width: 50px");//to avoid changing width in IE
	 
}

function include(arr,obj) {
     //return (arr.indexOf(obj) != -1);
    var indx =  jQuery.inArray(obj, arr); 
     if(indx != -1)
      return true;
     else
      return false;
     
}
	
function updateDupStudent(){
	var dupData = $("#dupStudentlist").jqGrid('getGridParam','data');
	for(var key in dupData){
		var objstr = dupData[key];
		var orgId = $("#dupStu"+objstr.studentId).val(); 
		var OrgName = orgForDupStu[objstr.studentId][orgId];
		var studentDataTe = studentMap.get(objstr.studentId);
		var studentTempData = studentTempMap.get(objstr.studentId);
		
		
		
		
		if(studentDataTe == null || studentDataTe == undefined) {
			studentMap.put(objstr.studentId,studentTempData);
			studentDataTe = studentMap.get(objstr.studentId);
		}
		splitArr = String(studentDataTe.orgNodeId).split(",");
		for(var i = 0; i < splitArr.length; i++) {
			for(var k = 0; k < countAllSelect; k++) {
				if(allSelectOrg[k] != null && (allSelectOrg[k] == splitArr[i] || allSelectOrg[k] == splitArr[i]+"_f")) {
					if(String(orgId)!=String(splitArr[i])){
						allSelectOrg.splice(k,1);
						countAllSelect--;
					}
					
				}
			}
		}
		
		studentDataTe.orgNodeId = orgId;
		studentDataTe.orgNodeName = $.trim(OrgName);
		studentMap.put(objstr.studentId,studentDataTe);
	}
	 var previous = AddStudentLocaldata.length; 
	if(previous == undefined)
	 	previous = 0;
	 updateAddStudentLocaldata();
	 var newValue = AddStudentLocaldata.length;
	 if(newValue == undefined)
	 	newValue = 0; 
	 var finalValue = newValue - previous;
	 if(finalValue < 0) {
	 	finalValue = 0 - finalValue;
	 	if(newValue == 0)
	 		message = $("#stuDelAllMsg").val();
	 	else
	 		message = finalValue + "  " + $("#stuDelMsg").val();
	 } else if (finalValue > 0) {
	 	message = finalValue + "  " + $("#stuAddedMsg").val();
	 } else {
	 	$("#studentAddDeleteInfo").hide();
	 }
	 $("#studentAddDeleteInfo").show();
	 var message = finalValue + "  " + $("#stuAddedMsg").val();
	 $("#addDeleteStud").text(message);
	 hideSelectStudentPopup();
	 gridReloadStu(false);
	 $("#duplicateStudent").dialog("close");
}
	
function returnSelectedStudent() {
	var duplicateStuArray=[];
	delStuIdObjArray = [];
	selectAllForDelete = 0;
	orgForDupStu = [];
	var dupStuPresent = false;
	var duplicateStuArraydata ={};
	studentWithaccommodation = 0;
	studentMap = new Map();




	var keys = studentTempMap.getKeys();
	for(var ll =0 ; ll<keys.length; ll++ ) {

		var stdId = keys[ll];
		var objstr = studentTempMap.get(stdId);
		if(objstr != null && objstr != undefined) {
			var hasAccom = objstr.hasAccommodations;
			if(hasAccom == 'Yes') {
	 	 		studentWithaccommodation = studentWithaccommodation + 1;
	 	 	}
	 	 	var orgArray = String(objstr.orgNodeId).split(",");
	 	 	var mm= 0;
	 	 	var tempOrgArray = [];
	 	 	// extracare to avoid junc data
	 	 	if(orgArray.length>1){
		 	 	 for(var nn = 0; nn<orgArray.length ; ++nn){
		 	 	 	if(!(orgArray[nn]== undefined ||orgArray[nn]== null, orgArray[nn].length==0 ) ) {
		 	 	 		tempOrgArray[mm] = orgArray[nn];
		 	 	 		mm = mm+1;
		 	 	 	}
		 	 	 
		 	 	 }
		 	 	orgArray =  tempOrgArray;
	 	 	}
	 	 	objstr.orgNodeId=orgArray.valueOf();
	 	 	var orgNameArray = String(objstr.orgNodeName).split(",");
	 	 	mm= 0;
	 	 	tempOrgArray = [];
	 	 	if(orgNameArray.length>1){
		 	 	 for(var nn = 0; nn<orgNameArray.length ; ++nn){
		 	 	 	if(!(orgNameArray[nn]== undefined ||orgNameArray[nn]== null, orgNameArray[nn].length==0 ) ) {
		 	 	 		tempOrgArray[mm] = orgNameArray[nn];
		 	 	 		mm = mm+1;
		 	 	 	}
		 	 	 
		 	 	 }
		 	 	orgNameArray =  tempOrgArray;
	 	 	}
	 	 	objstr.orgNodeName=orgNameArray.valueOf();
	 	 	// extracare to avoid junc data
	 	 	
	 	 	if(orgArray.length > 1) {
				dupStuPresent = true;
				objstr.studentId = stdId;
				duplicateStuArray.push(objstr);
				orgNameArray = String(objstr.orgNodeName).split(",");
				var orgIdNameMap = {};
				for(var i=0;i<orgArray.length; i++) {
					orgIdNameMap[orgArray[i]] = orgNameArray[i];
				}
				orgForDupStu[objstr.studentId] = orgIdNameMap;
			} else {	
		 		studentMap.put(stdId,objstr);
	 		}
		}
	}
 if(dupStuPresent) {
 var message = "";
 duplicateStuArraydata = duplicateStuArray;
 	$('#dupStudentlist').GridUnload();	
 	openDuplicateStudentPopup(duplicateStuArraydata, orgForDupStu);
 } else {
 	$("#studentAddDeleteInfo").show();
	 var previous = AddStudentLocaldata.length; 
	if(previous == undefined)
	 	previous = 0;
	 updateAddStudentLocaldata();
	 var newValue = AddStudentLocaldata.length;
	 if(newValue == undefined)
	 	newValue = 0; 
	 var finalValue = newValue - previous;
	 if(finalValue < 0) {
	 	finalValue = 0 - finalValue;
	 	if(newValue == 0)
	 		message = $("#stuDelAllMsg").val();
	 	else
	 		message = finalValue + "  " + $("#stuDelMsg").val();
	 } else if (finalValue > 0) {
	 	message = finalValue + "  " + $("#stuAddedMsg").val();
	 } else {
	 	$("#studentAddDeleteInfo").hide();
	 }
	 $("#addDeleteStud").text(message);
	 hideSelectStudentPopup();
	 gridReloadStu(false);
	 $("#duplicateStudent").dialog("close");
	 $("#stuOrgNodeHierarchy").undelegate();
 }
 
}

function updateAddStudentLocaldata() {
	AddStudentLocaldata = [];
	var keys = studentTempMap.getKeys();

	for(var i =0 ; i<keys.length; i++ ) {
		if(studentTempMap.get(keys[i]) != null && studentTempMap.get(keys[i]) != undefined) {
		    AddStudentLocaldata[i] = studentTempMap.get(keys[i]);
			AddStudentLocaldata[i].studentId = keys[i];
		}
	
	
	}
}

function openDuplicateStudentPopup(duplicateStuArray, orgForDupStu){
	populateDuplicateStudentGrid(duplicateStuArray, orgForDupStu);
	$("#duplicateStudent").dialog({  
		title:$("#dupStuResolve").val(),  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '1024px',
	 	modal: true,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});	
		 var toppos = ($(window).height() - 650) /2 + 'px';
		 var leftpos = ($(window).width() - 1024) /2 + 'px';
		 $("#scheduleSession").parent().css("top",toppos);
		 $("#scheduleSession").parent().css("left",leftpos);	
		 
	}	
	

	
function populateDuplicateStudentGrid(duplicateStuArray, orgForDupStu) {
 		//UIBlock();
 		
 		var studentIdTitle = $("#studentIdLabelName").val();
 		reset();
       $("#dupStudentlist").jqGrid({
      	  data: duplicateStuArray,         
          datatype: 'local',          
          colNames:[ $("#testStuLN").val(),$("#testStuFN").val(), $("#testStuMI").val(), studentIdTitle, leafNodeCategoryName],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:200, editable: true, align:"left",sorttype:'text',search: false, sortable:false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:150, editable: true, align:"left",sorttype:'text',search: false, sortable:false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:150, editable: true, align:"left",sorttype:'text',search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'extPin1',index:'extPin1', width:250, editable: true, align:"left", sortable:false, search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'orgNodeName',index:'orgNodeName',editable: false, width:200, align:"left", sortable:false, search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' },formatter:selectFormat }
		   	],
		   		jsonReader: { repeatitems : false, root:"rows", id:"studentId",
		   	records: function(obj) { 
		   	 } },
		   	loadui: "disable",
			rowNum:10000,
			loadonce:true, 
			multiselect:false,
			sortname: 'lastName',
			pager: '#dupStudentPager', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,  
			loadComplete: function () {
				if ($('#dupStudentlist').getGridParam('records') === 0) {
					
            		$('#sp_1_dupStudentPager').text("1");
            		$('#next_dupStudentPager').addClass('ui-state-disabled');
            	 	$('#last_dupStudentPager').addClass('ui-state-disabled');
            	} else {
            		
            	}
            	$.unblockUI();  
				$("#dupStudentlist").setGridParam({datatype:'local'});
				var tdList = ("#dupStudentPager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#dupStudentlist").jqGrid('navGrid','#dupStudentPager',{edit:false,add:false,del:false,search:false,refresh:false});
	 
	 
}


function populateGradeLevelFilter() {

	var init = 0;
	var final = 0;	
	var splitArray = [];
	if(blockOffGradeTesting) {
		categoriesStr = "";
		if(selectedLevel.indexOf("-") >= 0 || selectedLevel.indexOf("\/") >= 0)
			categoriesStr = ":"+selectedLevel+";";
		dropListToDisplay = [{"id":selectedLevel,"name":selectedLevel}];
	}
	else {
		dropListToDisplay = [];
		for (var i = 0; i < studentGradesCustomerConfig.length; i++)
			dropListToDisplay[i] = {"id":studentGradesCustomerConfig[i],"name":studentGradesCustomerConfig[i]};
		categoriesStr = ":All;";
	}
	for (var i = 0; i < dropListToDisplay.length; i++) {
		if(dropListToDisplay[i].id == "Show all")
			continue;
		else {
			if(dropListToDisplay[i].id.indexOf("-") < 0) {				
				if(dropListToDisplay[i].id.indexOf("\/") >= 0) { // For handling terranova product
					splitAttay = String(dropListToDisplay[i].id).split("/");
					categoriesStr = categoriesStr + splitAttay[0] + ":" + splitAttay[0] + ";";
					categoriesStr = categoriesStr + splitAttay[1] + ":" + splitAttay[1] + ";";
				} else {
					categoriesStr = categoriesStr + dropListToDisplay[i].id + ":" + dropListToDisplay[i].name + ";";
				}
			} else { // For handling laslink products
				splitArray = String(dropListToDisplay[i].id).split("-");
				init = parseInt(splitArray[0]);
				final = parseInt(splitArray[1]);
				for(var j = init; j <= final; j++) {
					categoriesStr = categoriesStr + j + ":" + j + ";";
				}
			}
		}
	}
	categoriesStr = categoriesStr.substr(0,categoriesStr.length - 1);
	if(blockOffGradeTesting == null || blockOffGradeTesting == undefined || blockOffGradeTesting == "" || !blockOffGradeTesting) {
		var optionList = [];
		optionList[0] = {"id":'',"name":'All'};		
		var gradeArray = String(categoriesStr).split(";");
		for(var i = 1; i < gradeArray.length; i++) {
			var splitGrade = String(gradeArray[i]).split(":");
			optionList[i] = {"id":splitGrade[0],"name":splitGrade[1]};
		}
		fillDropDown('gs_grade',optionList);
	}
}



function getStudentListArray(studentArray) {
	  var resultStdArray = [];
	  if (studentArray==undefined) {
	  	return resultStdArray;
	  }
	  var l = 0;
	  for (var i=0; i<studentArray.length; i++) {
	    if(studentArray[i]!= undefined && studentArray[i] != null) {
	    	var val = new SessionStudent(studentArray[i].studentId, studentArray[i].orgNodeId, studentArray[i].extendedTimeAccom, studentArray[i]["statusCopyable"],  studentArray[i].itemSetForm );
	        resultStdArray[l]= val.toString();
	        ++l;
	    }
	  		
	  	
	  }
	  return resultStdArray;
	}
	function validateIdenticalStudent (studentArray) {
    	var accessCodes=new Array();
    	var validStatus = true;
		var resultStdArray = [];
		var duplicateStudentArray = [];
		var k = 0;
		if (studentArray==undefined) {
		  	return true;
		 }
		  
		for (i=0; i<studentArray.length; i++) {
		  var val = studentArray[i].studentId;
		  resultStdArray[i]= val;
		}
    
		var sorted_arr = resultStdArray.sort(); 
		for (var i = 0; i < resultStdArray.length - 1; i++) {
			if (sorted_arr[i + 1] == sorted_arr[i]) {
				duplicateStudentArray[k]=sorted_arr[i]; 
		    	validStatus = false; 
		    	k++;
		 	} 
		} 
    	return validStatus;
    }

    function SessionStudent(studentId, orgNodeId, extendedTimeAccom, statusCopyable, itemSetForm) {
	   this.studentId = studentId;
	   this.orgNodeId = orgNodeId;
	   this.extendedTimeAccom = "";
	   if(extendedTimeAccom != undefined)
	   		this.extendedTimeAccom = extendedTimeAccom;
	   
	   this.statusCopyable = "";
	   if(statusCopyable != undefined)
	   		this.statusCopyable = statusCopyable;

	   this.itemSetForm ="";
	   if(itemSetForm != undefined)
	   		this.itemSetForm = itemSetForm;
	}
	
	SessionStudent.prototype.toString = function () {
  		return ( ""+"studentId="+this.studentId +":orgNodeId=" +this.orgNodeId + ":extendedTimeAccom="+this.extendedTimeAccom + ":statusCopyable=" +this.statusCopyable +":itemSetForm=" +this.itemSetForm+":");
	};
	
	function cloneStudentMapToTemp() {
		studentTempMap = new Map();
		var keys = studentMap.getKeys();
		for(var i =0 ; i<keys.length; i++ ) {
			studentTempMap.put(keys[i], studentMap.get(keys[i]));
		}

	}
	
	function deleteStudentFromTemp(  stuForSelectedOrg,  studentIdVal, selectedRowId, studentTempMap) {
		var tmpOrgIdList    =  String(studentIdVal.orgNodeId).split(",");
		var tmpOrgNameList  =  String(studentIdVal.orgNodeName).split(",");
		var finalTmpOrgIdList ="";
		var finalTmpOrgNameList ="";
		if(studentIdVal.statusEditable =="F"){
			return;
		} else if(tmpOrgIdList.length == 1) {
		   if(stuForSelectedOrg == tmpOrgIdList[0])
			 studentTempMap.remove(selectedRowId);
		} else {
			for(var kk = 0; kk<tmpOrgIdList.length ; kk++  ){
				if( tmpOrgIdList[kk] != stuForSelectedOrg) {
					if(finalTmpOrgIdList.length ==0){
							finalTmpOrgIdList =tmpOrgIdList[kk];
							finalTmpOrgNameList =  tmpOrgNameList[kk];
					}else {
							finalTmpOrgIdList   = tmpOrgIdList[kk]+","+finalTmpOrgIdList;
							finalTmpOrgNameList = tmpOrgNameList[kk]+","+finalTmpOrgNameList;
					}					         	
				}
			}
			studentIdVal.orgNodeId   = finalTmpOrgIdList;
			studentIdVal.orgNodeName = finalTmpOrgNameList;
			studentTempMap.put(selectedRowId,studentIdVal);
		}
	
	
	}
	

	function isFilterUpdated(){
		var result = false;
		 if(prvGrade != $("#gs_grade").val()){
		 	result = true;
		 } else if (prvCalculator != $("#gs_calculator").val()) {
		 	result = true;
		 } else if (prvHasColorFont != $("#gs_hasColorFontAccommodations").val()) {
		 	result = true;
		 }else if (prvTestPause != $("#gs_testPause").val()) {
		 	result = true;
		 }else if (prvScreenReader != $("#gs_screenReader").val()) {
		 	result = true;
		 }else if (prvUntimedTest 	= $("#gs_untimedTest").val()) {
		 	result = true;
		 }
		 
		 prvGrade 		= $("#gs_grade").val();
		 prvCalculator 	= $("#gs_calculator").val();
		 prvHasColorFont = $("#gs_hasColorFontAccommodations").val();
		 prvTestPause 	= $("#gs_testPause").val();
		 prvScreenReader = $("#gs_screenReader").val();
		 prvUntimedTest  = $("#gs_untimedTest").val();

	     return result;
	
	}
	
	function isDefaultFilterUpdated(){
		var result = false;
		 if(defaultGrade != $("#gs_grade").val()){
		 	result = true;
		 } else if (defaultCalculator != $("#gs_calculator").val()) {
		 	result = true;
		 } else if (defaultHasColorFont != $("#gs_hasColorFontAccommodations").val()) {
		 	result = true;
		 }else if (defaultTestPause != $("#gs_testPause").val()) {
		 	result = true;
		 }else if (defaultScreenReader != $("#gs_screenReader").val()) {
		 	result = true;
		 }else if (defaultUntimedTest 	= $("#gs_untimedTest").val()) {
		 	result = true;
		 }
	     return result;
	}
	
	
	
	function updateFilteredStudentData(){
		allFilteredStudentRow = [];
		var fgrade 			= $("#gs_grade").val();
		var fcalculator 	= $("#gs_calculator").val();
		var fColorFont		= $("#gs_hasColorFontAccommodations").val();
		var ftestPause  	= $("#gs_testPause").val();
		var fscreenReader	= $("#gs_screenReader").val();
		var funtimedTest	= $("#gs_untimedTest").val();
		var all 			= "";
		var isFilteredSuccess = false;
		var indx = 0;
		if(fgrade==defaultGrade && fcalculator==defaultCalculator && fColorFont ==defaultHasColorFont && ftestPause == defaultTestPause && fscreenReader == defaultScreenReader  && funtimedTest==defaultUntimedTest ){
			allFilteredStudentRow = allStudentIds;
			return;
		}
		
		for(var i = 0; i < allStudentIds.length; i++) {	
		    isFilteredSuccess = true;
		    var student = allStudentIds[i];
			if(fgrade!=defaultGrade && fgrade != student.grade){
				isFilteredSuccess =false;
			} 
			if (isFilteredSuccess && fcalculator!=defaultCalculator && fcalculator != student.calculator) {
				isFilteredSuccess =false;
			} 
			if (isFilteredSuccess && fColorFont!=defaultHasColorFont && fColorFont != student.hasColorFontAccommodations) {
				isFilteredSuccess =false;
			} 
			if (isFilteredSuccess && ftestPause!=defaultTestPause && ftestPause != student.testPause){
				isFilteredSuccess =false;
			} 
			if (isFilteredSuccess && fscreenReader!=defaultScreenReader && fscreenReader != student.screenReader){
				isFilteredSuccess =false;
			}
			if (isFilteredSuccess && funtimedTest!=defaultUntimedTest && funtimedTest != student.untimedTest){
				isFilteredSuccess =false;
			}
			if(isFilteredSuccess){
				allFilteredStudentRow[indx++] = student;
			}

		}
	
	}
	
	function setDefaultGridFilter(){
			  defaultGrade 			= $("#gs_grade").val();
		   	  defaultCalculator 	= $("#gs_calculator").val();
		   	  defaultHasColorFont 	= $("#gs_hasColorFontAccommodations").val();
		   	  defaultTestPause 		= $("#gs_testPause").val();
		   	  defaultScreenReader 	= $("#gs_screenReader").val();
		   	  defaultUntimedTest 	= $("#gs_untimedTest").val();
		   	 prvGrade 			= defaultGrade;
			 prvCalculator 		= defaultCalculator;
			 prvHasColorFont 	= defaultHasColorFont;
			 prvTestPause 		= defaultTestPause;
			 prvScreenReader 	= defaultScreenReader;
			 prvUntimedTest 	= defaultUntimedTest;
	
	}
	

	function showStatusLegend(event) {
		var isIE = document.all?true:false;
		var tempX = 0;
		var tempY = 0;
		var legendDiv = null;
		var padding = 15;
		
		if (isIE) { 
			tempX = event.clientX + document.documentElement.scrollLeft;
			tempY = event.clientY + document.documentElement.scrollTop;
		}
		else { 
			tempX = event.pageX;
			tempY = event.pageY;
		}  
		legendDiv = document.getElementById("statusLegend");
		legendDiv.style.left = (tempX - $(legendDiv).width() / 3)+"px" ;
		legendDiv.style.top = (tempY - $(legendDiv).height() - padding)+"px"; 
		legendDiv.style.display = "block";
		htimer = setTimeout("hideStatusLegend()", 5000);
	}
	
	function hideStatusLegend() {
		clearTimeout(htimer);
		document.getElementById("statusLegend").style.display = "none";
	}
	function addToStudentTempMap(studentId,studentRow){
	  if(studentRow["statusEditable"] != undefined && studentRow["statusEditable"] =="T") {
	  
	  	if(studentRow.outOfSchool == undefined || studentRow.outOfSchool == 'No') { // Added for out of school students
	  		studentTempMap.put(studentId, studentRow);
	  		return true;
	  	} else {
	  		return false;
	  	}
	  } else if (studentRow.status != undefined && studentRow.statusEditable != undefined && studentRow.statusEditable =="T"){
	  	if(studentRow.outOfSchool == undefined || studentRow.outOfSchool == 'No') { // Added for out of school students
	  		studentTempMap.put(studentId, studentRow);
	  		return true;
	  	} else {
	  		return false;
	  	}
	  
	  }else {
	   	return false;
	  }
	
	}
	
	
	function updateUI(){
		var allRowsInGridHere = $('#selectStudent').jqGrid('getDataIDs');
		for(var ii = 0; ii < allRowsInGridHere.length; ii++) {
		    //var selectedRowData = $("#selectStudent").getRowData(allRowsInGridHere[ii]);
		    //var statEditable = selectedRowData["status.editable"];
		     var statEditable = studentEditStatusMap.get(allRowsInGridHere[ii]);
		    if(statEditable == "F"){ 
		 		var trs = $('#gview_selectStudent tr');
		 		var node;
		 		for(count=0; count<trs.length; count++) {
		 		    if(count<2) // to skip first two header row
		 		     	continue;
		 			node = trs.eq(count);
		 			if(node.attr("id") == allRowsInGridHere[ii]){
			 			  node.removeClass('ui-state-highlight');
					}
				}
						 		 
			}
		 }
	}
	
	function addToolTipFmatter(cellvalue, options, rowObject){
	  var val = cellvalue;
        if(cellvalue=="Ses") {
	       var tooltip = rowObject.extPin2;
	       if(tooltip == undefined  || tooltip == null){
	       	tooltip = "";
	       }
		    tooltip = tooltip.replace(new RegExp("<br/>", 'g'),"&#013;" ); 
		 	//val = "<p style='color:blue;' title='"+tooltip+"' >"+ val +"</p>";
		 	val = "<a href='#' style=' color:blue;' title=' "+tooltip+" ' >"+ val +"</a>";

		 }
	return val;
  }
 	
	function openRestrictedStudentPopUp(restrictedStudents, totalStuLen) {
		populateRestrivtedGrig(restrictedStudents);
		if(state != "EDIT") {
			$("#addOperation").show();
			$("#editOperation").hide();
		} else {
			$("#editOperation").show();
			$("#addOperation").hide();
		}
		$("#restrictedStudent").dialog({  
		title:$("#restrictedTitle").val(),  
	 	resizable:false,
	 	autoOpen: true,
	 	width: '920px',
	 	modal: true,
	 	open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
		});
		var toppos = ($(window).height() - 650) /2 + 'px';
		 var leftpos = ($(window).width() - 920) /2 + 'px';
		 $("#scheduleSession").parent().css("top",toppos);
		 $("#scheduleSession").parent().css("left",leftpos);
		 $("#restrictedCount").text(restrictedStudents.length);
		$("#totalCountStu").text(totalStuLen);
	}
	
	function populateRestrivtedGrig(restrictedStudents) {
		$("#restrictedStudentlist").jqGrid({
      	  data: restrictedStudents,         
          datatype: 'local',          
          colNames:[ $("#testStuLN").val(),$("#testStuFN").val(), $("#testStuMI").val(), $("#testDetGrade").val(), $("#resSessionName").val(), $("#resStartDate").val(), $("#resEndDate").val()],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:150, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'firstName',index:'firstName', width:150, editable: true, align:"left",sorttype:'text',search: false, sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'middleName',index:'middleName', width:80, editable: true, align:"left",sorttype:'text',search: false, sortable:true,cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'grade',index:'grade', width:70, editable: true, align:"left", sortable:true, search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' } },
		   		{name:'status.priorSession.testAdminName',index:'orgNodeName',editable: false, width:248, align:"left", sortable:false, search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }},
		   		{name:'status.priorSession.loginStartDateString',index:'StartDate',editable: false, width:80, align:"left", sortable:false, search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }},
		   		{name:'status.priorSession.loginEndDateString',index:'EndDate',editable: false, width:80, align:"left", sortable:false, search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;' }}
		   	],
		   		jsonReader: { repeatitems : false, root:"rows",
		   	records: function(obj) { 
		   	 } },
		   	loadui: "disable",
		   	caption: $("#resGridTitle").val(),
			rowNum:20,
			loadonce:true, 
			multiselect:false,
			sortname: 'lastName',
			pager: '#restrictedStudentpager', 
			viewrecords: true, 
			sortorder: "asc",
			height: 370,
			onPaging: function() {
				var reqestedPage = parseInt($('#restrictedStudentlist').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_restrictedStudentpager').text());
				var minPageSize = 1;
				isPagingEvent  = true;
				if(reqestedPage > maxPageSize){
					$('#restrictedStudentlist').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#restrictedStudentlist').setGridParam({"page": minPageSize});
				}
			},
			loadComplete: function () {
				if ($('#dupStudentlist').getGridParam('records') === 0) {
					
            		$('#sp_1_restrictedStudentpager').text("1");
            		$('#next_restrictedStudentpager').addClass('ui-state-disabled');
            	 	$('#last_restrictedStudentpager').addClass('ui-state-disabled');
            	} else {
            		
            	}
            	$.unblockUI();  
				var tdList = ("#restrictedStudentpager_left table.ui-pg-table  td");
				for(var i=0; i < tdList.length; i++){
					$(tdList).eq(i).attr("tabIndex", i+1);
				}
				
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	 jQuery("#restrictedStudentlist").jqGrid('navGrid','#restrictedStudentpager',{edit:false,add:false,del:false,search:false,refresh:false});
	 
		
	}