
	var subTestDetails = {};
	var modifyManifestStudentId = -1;
	var modifyManifestStudentOrgId = -1;
	var oldModifyManifestStudentId = -1;
	var studentManifestdetails =[];
	var isForStudentSubtestModification = false;
	var allSubtestsMsm = new Array();
	var selectedSubtestsMsm = new Array();
	var levelOptions = new Array();
	var allSubtestMapMsm = new Map();
	var isSelectedTestTabePr = "false";
	var isSelectedTabeAdaptivePr = "false";
	 

	function initializeModifyTestPopup(rowId,listId, isTabeProduct, isTabeAdaptiveProduct, productType){

		isSelectedTestTabePr      = isTabeProduct;
		isSelectedTabeAdaptivePr = isTabeAdaptiveProduct;
		if((isTabeProduct == "true" || isTabeAdaptiveProduct =="true" ) && (productType!='tabeLocatorProductType') && ($("#modifyStdManifestButton").length > 0)){
			setAnchorButtonState('modifyStdManifestButton', false);
		} else if ($("#modifyStdManifestButton").length > 0) {
			setAnchorButtonState('modifyStdManifestButton', true);
		}
	}
	
	
	function openModifyStdManifestPopup(element) {
	    isForStudentSubtestModification = true; //used for subtest modification true for student	
	    resetMsmOldValues();
		if (isButtonDisabled(element)) {
			return true;
		}
		
		$('#displayMessage').hide();	
		document.getElementById('modifyStdManifestButton').style.display = "block";	
		populateModifyStdManifestPopup();
		 if(isSelectedTestTabePr == "true" ){ 
	   		$("#mmsModifySubtestMsg").html($("#tabeModifySubtestMsg").val())
	   } else {
	   		$("#mmsModifySubtestMsg").html($("#tabeAdaptiveModifySubtestMsg").val())
	   }
		
	}
	
	function populateModifyStdManifestPopup(){
		
		var statusWizard = $("#modifyStudentManifestAccordion").accordion({ header: "h3", active: 0, event:false });
		$("h3", statusWizard).each(function(index) {
				$(this).bind("click", function(e) {
				    if($(this).parent().attr("id") == 'mStdMStudentDetailId'){
				        if( modifyManifestStudentId == -1 ){
				           setMsmErrorMessage( $("#msmSelectStudentValidationTitle").val() , "");
				        	return;
				        }
				        hideMessage();
				        if(oldModifyManifestStudentId != -1 && oldModifyManifestStudentId == modifyManifestStudentId){// if already populated no need to populate again
				        	statusWizard.accordion("activate", index);
				        } else {
				        	getAndPopulateManifestDetail(statusWizard, index);
				        }
				    } else {
				    	statusWizard.accordion("activate", index);
				    }
					
			  });
			});
		$("#modifyStudentManifestPopup").dialog( {
	        title: "Modify Student's Test",
	        resizable: false,
	        autoOpen: true,
	        width: '900px',
	        modal: true,
	        open: function(event, ui) {
	            $(".ui-dialog-titlebar-close").hide();
	        }
	    });
	    setPopupPositionModifyStudent();
		populateSessionStudentList();
	}
	
	function populateSessionStudentList(){
		UIBlock();
	   $('#mmStdList').GridUnload();
	    var studentIdTitle = $("#studentIdLabelName").val();
	    var param = {};
	  	param.testAdminId = selectedTestAdminId;
	  	var RURL = 	'getScheduledStudentsWithTest.do';
		$("#mmStdList").jqGrid({
		 	url: RURL, 
		 	mtype:   'POST',
		 	datatype: "json",
		 	postData:	param,
            colNames:[ $("#testStuLN").val(),$("#testStuFN").val(), $("#testStuMI").val(),  $("#testStuAccom").val(), leafNodeCategoryName ,  'studentId',  'Editable', '' ],
		   	colModel:[
		   		{name:'lastName',index:'lastName', width:135, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'firstName',index:'firstName', width:135, editable: true, align:"left",sorttype:'text',sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'middleName',index:'middleName', width:135, editable: true, align:"left",sorttype:'text',cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'hasAccommodations',index:'hasAccommodations', width:165, editable: true, align:"left", sortable:true, title:false,
		   			cellattr: function (rowId, tv, rawObject, cm, rdata) { 
		   				var returnStr = '';
						if(rawObject.hasAccommodations == 'Yes'){
							returnStr = 'style="white-space: normal;cursor:pointer;" onmouseover="showAccoToolTip('+rawObject.studentId+',event);" onmouseout="hideAccoToolTipPopUp();"' ;
						}else {
							returnStr = 'style="white-space: normal;cursor:pointer;"' ;
						}	
						return returnStr;				
		   		}},
		   		{name:'orgNodeName',index:'orgNodeName',editable: true, width:150, align:"left", sortable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'studentId',index:'studentId',editable: false, hidden:true,width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;' } },
		   		{name:'statusEditable',index:'statusEditable',editable: false,hidden:true, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;width=0px!important' } },
		   		{name:'orgNodeId',index:'orgNodeId',editable: false,hidden:true, width:0, align:"left", sortable:false,search: false, cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;cursor:pointer;width=0px!important' } }
		   	],
		   	jsonReader: { repeatitems : false, root:"savedStudentsDetails", id:"studentId",
		   		records: function(obj) {	
			   		subTestDetails = obj.testSession; 
			   		prepareallSubtestsMsm(); 
			   		levelOptions = obj.levelOptions; 
			   		var noOfRows = 0;
				    if (subTestDetails.subtests != undefined) { 
				        noOfRows = subTestDetails.subtests.length;
				    }
				    $("#numberOfRowsMsm").val(noOfRows);

			   	} 
		   	
		   	},
		   	loadui: "disable",
			rowNum:10,
			loadonce: true, 
			multiselect: false,
			pager: '#mmStdlistPager', 
			sortname: 'lastName', 
			viewrecords: true, 
			sortorder: "asc",
			height: 162,  
			caption:$("#stuListGrid").val(),
			onPaging: function() {
				$("#studentAddDeleteInfo").hide();
				var reqestedPage = parseInt($('#mmStdList').getGridParam("page"));
				var maxPageSize = parseInt($('#sp_1_mmStdlistPager').text());
				var minPageSize = 1;
				if(reqestedPage > maxPageSize){
					$('#mmStdList').setGridParam({"page": maxPageSize});
				}
				if(reqestedPage <= minPageSize){
					$('#mmStdList').setGridParam({"page": minPageSize});
				}
				resetOnSelectStudentValues();
				modifyManifestStudentId = -1;
        		oldModifyManifestStudentId = -1;
        		modifyManifestStudentOrgId = -1;
        		
				studentManifestdetails = [];
			},
			onSortCol:function(){
				resetOnSelectStudentValues();
				modifyManifestStudentId = -1;
       			oldModifyManifestStudentId = -1;
       			modifyManifestStudentOrgId = -1;
       			studentManifestdetails = [];
			},
			gridComplete: function() {
				var allRowsInGridPresent = $('#mmStdList').jqGrid('getDataIDs');
				for(var k = 0; k < allRowsInGridPresent.length; k++) {
					var selectedRowData = $("#mmStdList").getRowData(allRowsInGridPresent[k]);
					if(selectedRowData.statusEditable != "T") {
						$("#"+selectedRowData.studentId, "#mmStdList").addClass('ui-state-disabled');
					}
				}
				
			},
			onSelectRow: function (rowid, status) {
				modifyManifestStudentId = rowid;
				var rowData = $("#mmStdList").getRowData(rowid);
				modifyManifestStudentOrgId = rowData.orgNodeId ;
				if(modifyManifestStudentId != oldModifyManifestStudentId){
					resetOnSelectStudentValues();
					oldModifyManifestStudentId = -1;
				}
				
				
			},
			loadComplete: function () {
			    //setEmptyListMessage('studentGrid');
				/*var showAccommodations = $("#supportAccommodations").val();
				if(showAccommodations  == 'false') {
					$("#mmStdList").jqGrid("hideCol","hasAccommodations"); 
				} else {
					$("#mmStdList").jqGrid("showCol","hasAccommodations"); 
				}*/
				//$("#mmStdList").jqGrid("hideCol","studentId");
				var width = jQuery("#modifyStudentManifestPopup").width();
		    	width = width - 85; // Fudge factor to prevent horizontal scrollbars
				jQuery("#mmStdList").setGridWidth(width,true);
				
				if ($('#mmStdList').getGridParam('records') === 0) {
            	 	$('#sp_1_mmStdlistPager').text("1");
            	 	$('#next_mmStdlistPager').addClass('ui-state-disabled');
            	 	$('#last_mmStdlistPager').addClass('ui-state-disabled');
            	}
            	 var allRowsInGridPresent = $('#mmStdList').jqGrid('getDataIDs');
            	if(allRowsInGridPresent.length==0){
				  $('#mmStdList').append("<tr><th>&nbsp;</th></tr><tr><th>&nbsp;</th></tr>");
				  $('#mmStdList').append("<tr><td style='width: 100%;padding-left: 30%;' colspan='6'><table><tbody><tr width='100%'><th style='padding-right: 12px; text-align: right;' rowspan='2'><img height='23' src='/SessionWeb/resources/images/messaging/icon_info.gif'></th><th colspan='6'>"+$("#msmNoStudentTitle").val()+"</th></tr><tr width='100%'></tr></tbody></table></td></tr>");
				} 
				
				$.unblockUI(); 
			},
			loadError: function(XMLHttpRequest, textStatus, errorThrown){
				$.unblockUI();  
				window.location.href="/SessionWeb/logout.do";
						
			}
	 });
	  jQuery("#mmStdList").jqGrid('navGrid','#mmStdlistPager',{edit:false,add:false,del:false,search:false,refresh:false});
		
	}
	
	
	function getAndPopulateManifestDetail(statusWizard, index) {
	
		var postDataObject = {};
 		postDataObject.studentId = modifyManifestStudentId;
 		postDataObject.testAdminId  = selectedTestAdminId;
 		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'getScheduleStudentsManifestDetails.do',
			type:		'POST',
			dataType:	'json',
			data:		postDataObject,
			success:	function(data, textStatus, XMLHttpRequest){
							
							studentManifestdetails = data;
							prepareSelectedSubtestsMsm();
							PopulateManifestDetail();
							statusWizard.accordion("activate", index);
							enableButton('msmSaveButton');
							oldModifyManifestStudentId = modifyManifestStudentId;
							
						},
			error  :    function(XMLHttpRequest, textStatus, errorThrown){
							$.unblockUI();
							window.location.href="/SessionWeb/logout.do";
						},
			complete :  function(){
							$.unblockUI(); 
						}
		});
		//setPopupPositionViewStatus();
	}
	
	function PopulateManifestDetail() {
		populateLocatorTable();
		populateSubtestTable();
	}
	
	function populateLocatorTable(statusWizard, index) {
		if(subTestDetails.locatorSubtest == undefined){
			$("#mStdMlocatorDiv").hide();
		} else {
		    var isLocatorPresent = "";
		    if(studentManifestdetails.studentManifests.length>0){
		    	isLocatorPresent = (subTestDetails.locatorSubtest.id == studentManifestdetails.studentManifests[0].id) ? "checked='checked'" : "";
		    }
		    
			var html = "";
			html += '<table cellspacing="1" cellpadding="0" width="100%" class="ts"> ';
			html += '<tbody> ';
			html += '<tr class="subtestHeader"> ';
			html += '<th height="23" align="center" width="24"> <strong>Select</strong> </th> ';
			html += '<th height="23" align="left" width="419" style="padding-left: 5px;"> <strong>Subtest Name </strong></th> ';
			html += '</tr> ';
			html += '<tr> ';
			html += '<td height="23" bgcolor="#ffffff" width="24" style="padding-left: 10px;"> ';
			html += '<input type="checkBox" onclick="javascript:updateMsmLocatorValue(); " value="true" '+isLocatorPresent+ ' name="msmHasAutolocator" id="msmHasAutolocator"> ';
			html += '</td>';
			html += '<td height="23" bgcolor="#ffffff" width="419" style="padding-left: 5px;"> ';
			html += '<div align="left" id="sName">';
			html += subTestDetails.locatorSubtest.subtestName;
			html += '</div>';
			html += '</td>	</tr> </tbody> </table>';
		    $("#mStdMlocatorContentDiv").html(html);
			$("#mStdMlocatorDiv").show();
		}
	}
	function populateSubtestTable(){
		$("#addRowMsm").attr("disabled", true);
	    $("#addAllRowsMsm").attr("disabled", true);
	    $("#removeRowMsm").attr("disabled", true);
	    $("#removeAllRowsMsm").attr("disabled", true);
	    $("#moveUpMsm").attr("disabled", true);
	    $("#moveDownMsm").attr("disabled", true);
	    
	    if ($("#addRowMsm").hasClass("ui-widget-header")) {
	        $("#addRowMsm").removeClass("ui-widget-header");
	    }
	    if ($("#addAllRowsMsm").hasClass("ui-widget-header")) {
	        $("#addAllRowsMsm").removeClass("ui-widget-header");
	    }
	    if ($("#removeRowMsm").hasClass("ui-widget-header")) {
	        $("#removeRow").removeClass("ui-widget-header");
	    }
	    if ($("#removeAllRowsMsm").hasClass("ui-widget-header")) {
	        $("#removeAllRowsMsm").removeClass("ui-widget-header");
	    }
	    
	    if ($("#moveUpMsm").hasClass("ui-widget-header")) {
	        $("#moveUpMsm").removeClass("ui-widget-header");
	    }
	    if ($("#moveDownMsm").hasClass("ui-widget-header")) {
	        $("#moveDownMsm").removeClass("ui-widget-header");
	    }
	    var isLocatorPresent = false;
	    if(studentManifestdetails.studentManifests.length>0){
	    	isLocatorPresent = (subTestDetails.locatorSubtest != undefined && subTestDetails.locatorSubtest.id == studentManifestdetails.studentManifests[0].id) ? true : false;
	    }
	    var isProductHasLocator = (subTestDetails.locatorSubtest != null && subTestDetails.locatorSubtest != undefined) ? true : false;
	    if(isProductHasLocator && !isLocatorPresent) { 
	         $("#modifyTestLevelMsm").show();
	   } else {
	          $("#modifyTestLevelMsm").hide();
	   }
	     updateAllSubtests(allSubtestsMsm, selectedSubtestsMsm);
	     displaySourceTable(allSubtestsMsm, selectedSubtestsMsm,'availableSubtestsTableMsm');
	     displayDestinationTable(allSubtestsMsm, selectedSubtestsMsm, levelOptions, true ,isProductHasLocator, isLocatorPresent);

	     if (selectedSubtestsMsm != undefined && selectedSubtestsMsm.length > 0) {
	        $("#removeAllRowsMsm").attr("disabled", false);
	        $("#removeAllRowsMsm").addClass("ui-widget-header");
	    }
	    if (selectedSubtestsMsm != undefined && allSubtestsMsm != undefined && allSubtestsMsm.length > selectedSubtestsMsm.length) {
	        $("#addAllRowsMsm").attr("disabled", false);
	        $("#addAllRowsMsm").addClass("ui-widget-header");
	    }
	
	}
	 
	
	function setPopupPositionModifyStudent(){
		$("#mStdMStudentListContent").css("height",'380px');
		$("#mStdMStudentDetailContent").css("height",'380px');
		var toppos = ($(window).height() - 650) /2 + 'px';
		var leftpos = ($(window).width() - 1024) /2 + 'px';
		$("#modifyStudentManifestPopup").parent().css("top",toppos);
		$("#modifyStudentManifestPopup").parent().css("left",leftpos);	
	}
	
		
	function enableButton(elementid) {
	    var element = $("#"+elementid);
		element.removeClass('ui-state-disabled');
		element.addClass('ui-widget-header');
		element.attr('disabled', false);
	
	}
	
	function disableButton(elementid) {
		var element = $("#"+elementid);
		element.removeClass('ui-widget-header');
		element.addClass('ui-state-disabled');
		element.attr('disabled', 'disabled');
	
	}
	
	function prepareallSubtestsMsm() {
		allSubtestsMsm = new Array();
		allSubtestMapMsm = new Map();
		for (var ii = 0, jj = subTestDetails.subtests.length; ii < jj; ii ++ ) {
			allSubtestsMsm[ii] = subTestDetails.subtests[ii];
			allSubtestMapMsm.put(ii+1, subTestDetails.subtests[ii]);
		}
		if(subTestDetails.locatorSubtest != undefined ){
			allSubtestMapMsm.put(0,subTestDetails.locatorSubtest);
		}
		
	}
	
	function prepareSelectedSubtestsMsm() {
		selectedSubtestsMsm = new Array();
		for (var ii = 0, counter =0, jj = studentManifestdetails.studentManifests.length; ii < jj; ii ++ ) {
		    if(ii == 0 && subTestDetails.locatorSubtest != undefined && studentManifestdetails.studentManifests[0].id == subTestDetails.locatorSubtest.id ){
		    	continue;
		    }
			selectedSubtestsMsm[counter] = studentManifestdetails.studentManifests[ii];
			counter ++;
		}
	}
	
	
	function updateMsmLocatorValue(){
		var msmlocator = document.getElementById("msmHasAutolocator");
		//var tds = $('#selectedSubtestsTableMsm #subtestLevel');
		var removeSubtestLevel = false;
		if(msmlocator.checked){
			removeSubtestLevel = true;
			$("#modifyTestLevelMsm").hide();
		} else {
			removeSubtestLevel = false;
			$("#modifyTestLevelMsm").show();
		}
		/*for( var ii=0, jj=tds.length; ii<jj; ii++ ){
			if(removeSubtestLevel) {
				$(tds[ii]).hide();
			} else {
				$(tds[ii]).show();
			}
		}*/
		/*$('#selectedSubtestsTableMsm #subtestLevel').each(function (i) { 
				if(removeSubtestLevel) {
					$(this).hide();
				} else {
					$(this).show();
				}      
			}); */
		
			for( var ii=0, jj=allSubtestsMsm.length; ii<jj; ii++ ){
				if(removeSubtestLevel) {
					$("#subtestLevel_"+ii).hide();
				} else {
					$("#subtestLevel_"+ii).show();
				}
			}
		
	
	}
	

	function closemodifyStudentManifestPopup(){
	 resetMsmOldValues();
	 closePopUp('modifyStudentManifestPopup');
      isSelectedTestTabePr = "false";
	  isSelectedTabeAdaptivePr = "false";
	}

	function resetMsmOldValues(){
	   $("#mStdMStudentDetailHeader").unbind("click");
		subTestDetails = {};
		studentManifestdetails = [];
		$("#mStdMlocatorContentDiv").html("");
  		$("#modifyStudentManifestAccordion").accordion("destroy");
        $("#selectedSubtestsTable").html("");
        $("#availableSubtestsTable").html("");
        allSubtestMapMsm = new Map();
        modifyManifestStudentId = -1;
        oldModifyManifestStudentId = -1;
        modifyManifestStudentOrgId = -1;
        studentManifestdetails = [];
		resetOnSelectStudentValues();
		hideMessage();
	}
	function resetOnSelectStudentValues(){
		$("#selectedSubtestsTableMsm").html("");
        $("#availableSubtestsTableMsm").html("");
        disableButton('msmSaveButton');
        hideMessage();
	}
	
	function hideMessage(){
        $("#displayMsmMsg").hide();
		$("#displayMsmMsgErrorIcon").hide();
		$("#displayMsmMsgInfoIcon").hide();
		$("#msmSubContent").html("");
		$("#msmSubTitle").html("");
	}
	
	function setMsmErrorMessage( messageHeader, messageContent){
        $("#displayMsmMsg").show();
		$("#displayMsmMsgErrorIcon").show();
		$("#displayMsmMsgInfoIcon").hide();
		$("#msmSubTitle").html(messageHeader);
		$("#msmSubContent").html(messageContent);
	}
	
	function setMsmInfoMessage( messageHeader, messageContent){
        $("#displayMsmMsg").show();
		$("#displayMsmMsgErrorIcon").hide();
		$("#displayMsmMsgInfoIcon").show();
		$("#msmSubTitle").html(messageHeader);
		$("#msmSubContent").html(messageContent);
	}
	
	
	function validateAndUpdateStudentSubtest(){

		hideMessage();
		var isValidated = true;
		var validateLevels = true;
	    var tmpSelectedSubtestsMsm = new Array();
	    if(subTestDetails.locatorSubtest != undefined){
		  	validateLevels = !(document.getElementById("msmHasAutolocator").checked);
		 }
	    prepareMsmSelectedSubtests(tmpSelectedSubtestsMsm, validateLevels);
	    if(isSelectedTestTabePr=="true"){
	    	isValidated = validateTABESubtest(tmpSelectedSubtestsMsm, validateLevels, true);
	    } else {
	    	isValidated = validationTABE_ADAPTIVE (tmpSelectedSubtestsMsm, true);
	    }
	    if(isValidated) {
			updateManifestForRoster(tmpSelectedSubtestsMsm);
	    }
	    
	}
	
	
	function updateManifestForRoster(selectedSubtestsMsm){
		
		   var param = "";
		   var msmParam ="";
		   var locElement = document.getElementById("msmHasAutolocator");
		   var msmHasLocator = false;
		   if( subTestDetails.locatorSubtest != undefined  && locElement !=null && locElement.checked){
		  	 msmHasLocator = true;
		   } 
		   msmParam += "&hasAutolocator="+msmHasLocator;
		   if(msmHasLocator){
		   		msmParam += "&itemSetIdTD_l=" + subTestDetails.locatorSubtest.id;
		   		msmParam += "&subtestName_l=" +subTestDetails.locatorSubtest.subtestName;
		   }
		   
		   for(var ii=0; ii<selectedSubtestsMsm.length; ii++){
		   		msmParam += "&itemSetIdTD=" + selectedSubtestsMsm[ii].id;
		   		if(selectedSubtestsMsm[ii].level != undefined){
		   			msmParam += "&itemSetForm=" + selectedSubtestsMsm[ii].level;
		   		}
		   		msmParam += "&subtestName=" +selectedSubtestsMsm[ii].subtestName;
		   }
		   
		param += "testAdminId="+ selectedTestAdminId;
		param += "&studentId=" + modifyManifestStudentId;
		param += "&studentOrgNodeId=" + modifyManifestStudentOrgId;
		param += msmParam;
		$.ajax({
			async:		true,
			beforeSend:	function(){
							UIBlock();
						},
			url:		'updateManifestForRoster.do',
			type:		'POST',
			data:		 param ,
			dataType:	'json',
			success:	function(data, textStatus, XMLHttpRequest){
						   
						   var successMessage;
						   var key;
						   var messageHeader;
						   var messageArray;
						   var length = 0;
						   
						  if(data.isSuccess){
						  	successMessage   = data.successMessage;
							key 		     = data.successInfo.key;
							messageHeader 	 = data.successInfo.messageHeader;
							messageArray     = data.successInfo.message;
						  } else if(data.IsSystemError) {
						  		messageHeader 	 = data.validationFailedInfo.messageHeader;
						        messageArray     = data.validationFailedInfo.message;
						  }
						  
						  if(messageArray!=undefined){
							length= messageArray.length;
						  }
						   
						  if(data.isSuccess){
						  	if(length==0) {
						  		setMsmInfoMessage(messageHeader,"");
							} else if (length==1) {
								setSessionSaveMessage(messageHeader, messageArray[0]);
							}  
						  } else if (data.IsSystemError) {
						  		if(length==0){
						         	setMsmErrorMessage(messageHeader, "");
						         }else {
						         	setMsmErrorMessage(messageHeader, messageArray[0]);
						         }					         
					  	}
					  	$.unblockUI();
					  
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
	
	function prepareMsmSelectedSubtests(tmpSelectedSubtests , setLevels){
		 
		var selectetedTestRowCount = $("#selectedSubtestsTableMsm input").length;
	    for (var ii = 1; ii <= selectetedTestRowCount; ii ++ ) {
	        var val = $("#index_" + ii).val();
	        if (val > 0) {
	            tmpSelectedSubtests[val - 1] = allSubtestMapMsm.get(ii );
	            if(setLevels) {
	            	var level = $("#level_"+ii).val();
	            	tmpSelectedSubtests[val - 1].level = level;
	            } 
	        }
	        
	    }
	
	}
	
	