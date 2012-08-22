	//var hasAutolocator = false;
	//var locatorSubtest = {};
	var availableSubtest = new Array();
	var allSubtests = new Array();
	var selectedSubtests = new Array();
	var allSubtestMap = new Map();
	var selectedSubtestsMap = new Map();
	var chosenRow = null;
	var dragState = null;
	var READING = "TABE Reading";
	var MATH_COMPUTATION = "TABE Mathematics Computation";
	var APPLIED_MATH = "TABE Applied Mathematics";
	var LANGUAGE = "TABE Language";
	var VOCABULARY = "TABE Vocabulary";
	var LANGUAGE_MECHANICS = "TABE Language Mechanics";
	var SPELLING = "TABE Spelling";
	var selectedAccessCodeMap = new Map();
	
	var TABE_ADAPTIVE_READING = "TABE Adaptive Reading";
	var TABE_ADAPTIVE_MATH_COMPUTATION = "TABE Adaptive Mathematics Computation";
	var TABE_ADAPTIVE_APPLIED_MATH = "TABE Adaptive Applied Mathematics";
	var TABE_ADAPTIVE_LANGUAGE = "TABE Adaptive Language";
	var selectedStudentOrgNodeName;
	var selectedStudentOrgNodeid;
	
	var selectedSubtestsMsm = new Array();
	var subTestDetails = {};
	var hasDefaultAutoLocator= false;
	var studentManifests = new Array();
	var allSubtestsMsm = new Array();
	var allSubtestMapMsm = new Map();
	var levelOptions = new Array();
	var recomendedLevelMap = {};
	var locatorSessionInfo ="";
	var subtestWarningMsg="";
	
	var displayAccessCodeOnTicket = false;
	
	function updateModifyTestSize(){
		$("#modifyTestPopup").parent().height($("#sessionStudRegId").parent().height());
		$("#modifyTestPopup").parent().width($("#sessionStudRegId").parent().width());
		$("#editSubtedtinnerDiv").height($("#sessionStudRegId").parent().height()-100);
	}
	

	function openModifyTestPopup(element){
	 if(element !=null && isButtonDisabled(element))
		return true;
		
		closePopUp('recommendedDialogID');// closinig popop
		$('#msmTestSessionName1').text(selectedTestAdminName);
		$('#msmStudentName').text(selectedStudentNameFromSessionPopup);
		$('#msmOrgName').html(getOrgDropDownList(selectedStudentOrgNodeName, selectedStudentOrgNodeid));
		if(selectedStudentOrgNodeid.split("|").length<2){
			$('#stdOrg').attr('disabled',true);
		} else {
			$('#stdOrg').removeAttr('disabled');
		}
		
		
		var param = "&testAdminId="+selectedTestAdminId;
		 param += "&itemSetIdTc="+selectedItemSetIdTC;
		 param += "&studentId="+selectedStudentId;
		   
    	UIBlock();
    	$.ajax({
			async:		true,
			beforeSend :	function(){
							},
			url:		'getStudentManifestDetail.do',
			type:		'POST',
			data:		param,
			dataType:	'json',
			success:	function(data, textStatus, XMLHttpRequest){
			                subTestDetails = data.testSession;
			                studentManifests = data.studentDefaultManifest;
			                levelOptions = data.levelOptions; 
			                recomendedLevelMap = data.recomendedLevel;
			                locatorSessionInfo = data.locatorSessionInfo;
			                $("#numberOfRowsMsm").val(subTestDetails.subtests.length);
							openModifyManifestPopup();	
							 $.unblockUI();		
						},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
				             $.unblockUI();
								window.location.href="/SessionWeb/logout.do";
						}
				
				}
			);
		

	}
	
	function openModifyManifestPopup() {
		
		isForStudentSubtestModification = true;//used for subtest modification false for testadmin	
		$("#selectedSubtestsTableMsm").html("");
        $("#availableSubtestsTableMsm").html("");
       // $("#selectedSubtestsTable").html("");
        //$("#availableSubtestsTable").html("");
        if(!subTestDetails.hasMultipleSubtests){
       	 $("#mmsModifySubtestMsg").html("");
         $("#mmsModifySubtestMsg").html("");
        }else if(isTabeProduct){ 
	   		$("#mmsModifySubtestMsg").html($("#tabeModifySubtestMsg").val())
	   } else if (isTabeAdaptiveProduct){
	   		$("#mmsModifySubtestMsg").html($("#tabeAdaptiveModifySubtestMsg").val())
	   } else {
	  	 $("#mmsModifySubtestMsg").html("");
	   
	   }
	   $("#msmLocatorInfo").hide();
	   $("#msmLocatorInfo1").hide();
		hideSubtestValidationMessage();
	   
	    $("#modifyTestPopup").dialog( {
	        title: "Modify Test",
	        resizable: false,
	        autoOpen: true,
	        width: '900px',
	        modal: true,
	        open: function(event, ui) {
	            $(".ui-dialog-titlebar-close").hide();
	        }
	    });
	    //$("#modifyTestPopup").css('height',120);
	    updateModifyTestSize();
	    setPopupPosition("modifyTestPopup");
	  
	   PopulateManifestDetail();
	  	   
	}
	
	
	
	function PopulateManifestDetail() {
	     prepareSelectedSubtestsMsm();
	     prepareallSubtestsMsm(); 
	     updateAllSubtests(allSubtestsMsm, selectedSubtestsMsm);
	     populateAllSubtestMapMsm(allSubtestsMsm);
		 populateLocatorTable();
		 if(subTestDetails.hasMultipleSubtests){
		    var isLocatorPresentForStd = false;
		    if(subTestDetails.locatorSubtest !=undefined ){
		     isLocatorPresentForStd = (subTestDetails.locatorSubtest.id == studentManifests[0].id) ? true : false;
		    }
		     
		  	populateSubtestTable();
		  	populateRecomendedLevel(recomendedLevelMap , isLocatorPresentForStd);
		  	$("#subtestTableDiv").show();
		  	
		  	$("#subtestHeading").show();
		  	$("#SubtestInfo").show();
		  	if(isTabeProduct){
		  		$("#SubtestInfoForTabe").show();
		  		$("#SubtestInfoForTabeAdaptive").hide();
		  	} else if (isTabeAdaptiveProduct){
		  		$("#SubtestInfoForTabe").hide();
		  		$("#SubtestInfoForTabeAdaptive").show();
		  	} else {
		  		$("#SubtestInfoForTabe").hide();
		  		$("#SubtestInfoForTabeAdaptive").hide();
		  	}
		  	
		  	$("#msmHasAutolocator").removeAttr('disabled');
		    $("#mStdMlocatorSubtestGridToolTip").show();
		 } else {
		 	$("#subtestHeading").hide();
		  	$("#SubtestInfo").hide();
		 	$("#subtestTableDiv").hide();
		 	$("#msmHasAutolocator").attr("disabled", true);
		 	$("#mStdMlocatorSubtestGridToolTip").hide();
		 }
	}
	
	function prepareSelectedSubtestsMsm() {
		selectedSubtestsMsm = new Array();
		for (var ii = 0, counter =0, jj = studentManifests.length; ii < jj; ii ++ ) {
		    if(ii == 0 && subTestDetails.locatorSubtest != undefined && studentManifests[0].id == subTestDetails.locatorSubtest.id ){
		    	continue;
		    }
			selectedSubtestsMsm[counter] = studentManifests[ii];
			counter ++;
		}
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
	function updateAllSubtests(allSubtestsSrc , subtests) {
	    for (var ii = 0; ii < subtests.length; ii ++ ) {
	        var indx = getCurrentIndex(allSubtestsSrc , subtests[ii].id);
	        var tmp = allSubtestsSrc[indx];
	        allSubtestsSrc[indx] = allSubtestsSrc[ii];
	        allSubtestsSrc[ii] = tmp;
	    }
	    
	}
	
	
	function populateAllSubtestMapMsm(valueArray) {
	    allSubtestMapMsm = new Map();
	    for (var i = 0; i < valueArray.length; i ++ ) {
	        allSubtestMapMsm.put(i+1, valueArray[i]);
	    }
	    if(subTestDetails.locatorSubtest != undefined ){
			allSubtestMapMsm.put(0,subTestDetails.locatorSubtest);
		}
	    
	}
	
	function getCurrentIndex(allSubtestsSrc ,id) {
	    for (var ii = 0; ii < allSubtestsSrc.length; ii ++ ) {
	        if (allSubtestsSrc[ii].id == id) {
	            return ii;
	        }
	        
	    }
	    return - 1;
	}
	
	
	
	function populateLocatorTable(statusWizard, index) {
		if(subTestDetails.locatorSubtest == undefined){
			$("#mStdMlocatorDiv").hide();
		} else {
		    var isLocatorPresent = "";
		    if(studentManifests.length>0 && subTestDetails.locatorSubtest !=undefined){
		    	isLocatorPresent = (subTestDetails.locatorSubtest.id == studentManifests[0].id) ? "checked='checked'" : "";
		    }
		    
			var html = "";
			html += '<table cellspacing="1" cellpadding="0" width="100%" class="ts" style="border: 1px solid rgb(166, 201, 226);"> ';
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
	    if(studentManifests.length>0){
	    	isLocatorPresent = (subTestDetails.locatorSubtest != undefined && subTestDetails.locatorSubtest.id == studentManifests[0].id) ? true : false;
	    }
	    var isProductHasLocator = (subTestDetails.locatorSubtest != null && subTestDetails.locatorSubtest != undefined) ? true : false;
	    if(isProductHasLocator && !isLocatorPresent) { 
	         $("#modifyTestLevelMsm").show();
	   } else {
	          $("#modifyTestLevelMsm").hide();
	   }
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
	
	function displaySourceTable(allSubtestsSrc, selectedSubtestsSrc, srcDivId) {
	    var destHtmlText = "";
	    for (var ii = 0, jj = allSubtestsSrc.length; ii < jj; ii ++ ) {
	        var found = false;
	        var displayFlag = "none";
	        var value = String(ii + 1);
	        if ( ! isExists(selectedSubtestsSrc, allSubtestsSrc[ii])) {
	            displayFlag = "table-row";
	        } else {
	            value = "0";
	        }
	        var index = ii + 1;
	        destHtmlText += displaySourceRowStart(index, displayFlag);;
	        destHtmlText += displaySourceCell(allSubtestsSrc[ii].subtestName);
	    }
	    
	     //$("#availableSubtestsTable").html(destHtmlText);  
	     $("#"+srcDivId).html(destHtmlText); 
	    setTimeout("adjustHeightTables()",100);
	}
	
	
	function displaySourceRowStart(index, displayFlag) {
	    var id = "src_row_" + String(index);
	    var display = "display: " + displayFlag;
	    return "<tr id='" + id + "' class='dynamic' style='" + display + "' onmouseover='return hilightSubTestRow(this);' onmouseout='return unhilightSubTestRow(this);'  onmousedown='return selectSubtestRow(this);'>";
	    
	}
	
	function displaySourceCell(name) {
	    return "<td class='dynamic' >" + name + "</td>";
	}
	
	
	function displayDestinationTable(allSubtestsSrc, selectedSubtestsSrc, levelOptionsSrc, isFromModifyStdManifest, isProductHasLocator, isLocatorPresent ) {
	    var destHtmlText = "";
	    if (allSubtestsSrc == null) {
	        allSubtestsSrc = new Array();
	    }
	    
	    for (var ii = 0, jj = allSubtestsSrc.length; ii < jj; ii ++ ) {
	        var found = false;
	        var displayFlag = "none";
	        var value = String(ii + 1);
	        var level ="";
	        if(allSubtestsSrc[ii].level!=undefined && allSubtestsSrc[ii].level!=null){
	        	level = allSubtestsSrc[ii].level;
	        }
	        if (isExists(selectedSubtestsSrc, allSubtestsSrc[ii])) {
	            displayFlag = "table-row";
	            level = getItemLevel(selectedSubtestsSrc, allSubtestsSrc[ii]);
	        } else {
	            value = "0";
	        }
	        
	        var index = ii + 1;
	        destHtmlText += displayDestinationRowStart(index, displayFlag);
	        destHtmlText += displayDestinationNameCell(allSubtestsSrc[ii].subtestName, index, value);
	        if(!isFromModifyStdManifest){
	        	if(isProductHasLocator && !isLocatorPresent ) {
	        	 	destHtmlText += displayDestinationLevelDropdown(index, level, levelOptionsSrc, true, ii);
	         		$("#modifyTestLevel").show();
	        	} else {
	          		$("#modifyTestLevel").hide();
	        	}
	        } else {
	            if(isProductHasLocator){
	        		destHtmlText += displayDestinationLevelDropdown(index, level, levelOptionsSrc, !isLocatorPresent, ii);
	        	}
	        
	        }

	    }
	    if(!isFromModifyStdManifest){
	    	$("#selectedSubtestsTable").html(destHtmlText);
	    } else {
	   		 $("#selectedSubtestsTableMsm").html(destHtmlText);
	    }
	}
	
	
	function displayDestinationRowStart(index, displayFlag) {
	    var id = "des_row_" + index;
	    var display = "display: " + displayFlag;
	    return "<tr id='" + id + "' class='dynamic' style='" + display + "' onmouseover='return hilightSubTestRow(this);' onmouseout='return unhilightSubTestRow(this);'  onmousedown='return selectSubtestRow(this);'>";
	    
	}
	
	function displayDestinationNameCell(name, index, value) {
	    var strIndex = "index_" + String(index);
	    var retVal = "";
	    retVal += "<td class='dynamic'>";
	    retVal += "<input type='hidden' value='" + value + "' name='" + strIndex + "' id='" + strIndex + "' />";
	    retVal += name;
	    retVal += "</td>";
	    return retVal;
	    
	}
	
	function displayDestinationLevelDropdown( index, level , levelOptions, displayLevel, lindex)   {
        var strIndex = String(index);
        var strLevel = "level_" + strIndex;
		var retVal = "";
		var disabled = (displayLevel==true) ? "" : " style = 'display:none' "
        retVal+= '<td class="dynamic" id = "subtestLevel_'+lindex+'" align="center" ' + disabled + ' >';
        retVal+='<select name="' + strLevel + '" id= "' + strLevel + '" size="1" style="font-family: Arial, Verdana, Sans Serif; font-size: 11px;">';
        
        for (var i=0 ; i<levelOptions.length ; i++) {
            var option = levelOptions[i];         
            if (option==level)           
                retVal+='<option value="' + option + '" selected>' + " &nbsp;&nbsp;"+option + " &nbsp;&nbsp;&nbsp;"+'</option>';
            else
                 retVal+='<option value="' + option + '" >' + "&nbsp;&nbsp;"+ option + " &nbsp;&nbsp;&nbsp;"+ '</option>';
        }
        retVal+='"</select>';
        retVal+='</td>';
		 return  retVal;
    }
    
    
    function selectSubtestRow(row) {
	    if (row == null) {
	        if (chosenRow != null) {
	            chosenRow.className = 'dynamic';
	        }
	        chosenRow = null;
	    } else {
	        if (chosenRow != null) {
	            chosenRow.className = 'dynamic';
	        }
	        chosenRow = row;
	        chosenRow.className = 'dynamicChosen';
	        var id = row.id;
	        if (id.indexOf("src_row_") == 0) {
	            enableSubtestSourceTableButtons();
	            disableSubtestDestinationTableButtons();
	        } else {
	            enableSubtestDestinationTableButtons();
	            disableSubtestSourceTableButtons();
	        }
	    }
	    return true;
	}
	
	function hilightSubTestRow(row) {
	    if (row != chosenRow) {
	        row.className = 'dynamicHilight';
	    }
	    
	    return true;
	}
	
	
	function unhilightSubTestRow(row) {
	    if (row == chosenRow) {
	        row.className = 'dynamicChosen';
	    } else {
	        row.className = 'dynamic';
	    }
	    return true;
	}
	
	
	
	function moveSubtestRow(row, delta) {
	    if (row == null || delta == 0)
	        return true;
	    
	    var table = row.parentNode;
	    var targetIndex = parseInt(row.rowIndex - 1) + delta;
	    
	    if (delta > 0)
	        targetIndex = targetIndex + 1;
	    
	    if (targetIndex < 0)
	        targetIndex = 0;
	    
	    if (targetIndex >= table.rows.length)
	        table.appendChild(row);
	    else table.insertBefore(row, table.rows[targetIndex]);
	    
	    var id = row.id;
	    if (id.indexOf("des_row_") == 0) {
	        setVisibleFlag(row);
	    }
	    
	    // selectRow(row);
	    selectSubtestRow(row);
	    return true;
	}
	
	
	function enableSubtestDestinationTableButtons() {
	    var removeRow = undefined;
	    var removeAllRows = undefined;
	    var moveUp = undefined;
	    var moveDown = undefined;
	    if(isForStudentSubtestModification){
	    	removeRow = document.getElementById("removeRowMsm");
	    	removeAllRows = document.getElementById("removeAllRowsMsm");
	    	moveUp = document.getElementById("moveUpMsm");
	    	moveDown = document.getElementById("moveDownMsm");
	    } else {
	    	removeRow = document.getElementById("removeRow");
	    	removeAllRows = document.getElementById("removeRow");
	    	moveUp = document.getElementById("moveUp");
	    	moveDown = document.getElementById("moveDown");
	    }
	    
	    

	    removeRow.removeAttribute("disabled");
	    $(removeRow).addClass("ui-widget-header");
	    
	    removeAllRows.removeAttribute("disabled");
	    $(removeAllRows).addClass("ui-widget-header");
	    
	    moveUp.removeAttribute("disabled");
	    $(moveUp).addClass("ui-widget-header");
	    
	    moveDown.removeAttribute("disabled");
	    $(moveDown).addClass("ui-widget-header");
	    
	    
	    var visibleRows = getVisibleRows("des_row_");
	    
	    if (visibleRows <= 1) {
	        moveUp.setAttribute("disabled", "true");
	        moveDown.setAttribute("disabled", "true");
	        
	        if ($(moveUp).hasClass("ui-widget-header")) {
	            $(moveUp).removeClass("ui-widget-header");
	        }
	        
	        if ($(moveDown).hasClass("ui-widget-header")) {
	            $(moveDown).removeClass("ui-widget-header");
	        }
	        
	    }
	    
	    if (chosenRow != null) {
	        if (isFirstVisibleRow(chosenRow)) {
	            moveUp.setAttribute("disabled", "true");
	            if ($(moveUp).hasClass("ui-widget-header")) {
	                $(moveUp).removeClass("ui-widget-header");
	            }
	        }
	        if (isLastVisibleRow(chosenRow)) {
	            moveDown.setAttribute("disabled", "true");
	            if ($(moveDown).hasClass("ui-widget-header")) {
	                $(moveDown).removeClass("ui-widget-header");
	            }
	        }
	    }
	}
	
	function disableSubtestDestinationTableButtons() {
	   
	    
	    var removeRow = undefined;
	    var removeAllRows = undefined;
	    var moveUp = undefined;
	    var moveDown = undefined;
	    
	    if(isForStudentSubtestModification){
	    	removeRow = document.getElementById("removeRowMsm");
	    	removeAllRows = document.getElementById("removeAllRowsMsm");
	    	moveUp = document.getElementById("moveUpMsm");
	    	moveDown = document.getElementById("moveDownMsm");
	    } else {
	    	removeRow = document.getElementById("removeRow");
	    	removeAllRows = document.getElementById("removeRow");
	    	moveUp = document.getElementById("moveUp");
	    	moveDown = document.getElementById("moveDown");
	    }
	    
	    
	    removeRow.setAttribute("disabled", "true");
	    if ($(removeRow).hasClass("ui-widget-header")) {
	        $(removeRow).removeClass("ui-widget-header");
	    }

	    removeAllRows.setAttribute("disabled", "true");
	    
	    if ($(removeAllRows).hasClass("ui-widget-header")) {
	        $(removeAllRows).removeClass("ui-widget-header");
	    }
	    
	    var visibleRows = getVisibleRows("des_row_");
	    if (visibleRows > 0) {
	        removeAllRows.removeAttribute("disabled");
	        $(removeAllRows).addClass("ui-widget-header");
	        
	    }
	    

	    moveUp.setAttribute("disabled", "true");
	    if ($(moveUp).hasClass("ui-widget-header")) {
	        $(moveUp).removeClass("ui-widget-header");
	    }
	    

	    moveDown.setAttribute("disabled", "true");
	    if ($(moveDown).hasClass("ui-widget-header")) {
	        $(moveDown).removeClass("ui-widget-header");
	    }
	}
	
	function enableSubtestSourceTableButtons() {
		var addRow = undefined;
		var addAllRows = undefined;

	    if(isForStudentSubtestModification){
	    	addRow = document.getElementById("addRowMsm");
	    	addAllRows = document.getElementById("addAllRowsMsm");
	    } else {
	    	addRow = document.getElementById("addRow");
	    	addAllRows = document.getElementById("addAllRows");
	    }

	    addRow.removeAttribute("disabled");
	    $(addRow).addClass("ui-widget-header");
	    
	    addAllRows.removeAttribute("disabled");
	    $(addAllRows).addClass("ui-widget-header");
	}
	
	function disableSubtestSourceTableButtons() {
		var addRow = undefined;
		var addAllRows = undefined;

	    if(isForStudentSubtestModification){
	    	addRow = document.getElementById("addRowMsm");
	    	addAllRows = document.getElementById("addAllRowsMsm");
	    } else {
	    	addRow = document.getElementById("addRow");
	    	addAllRows = document.getElementById("addAllRows");
	    }
	
	    addRow.setAttribute("disabled", "true");
	    if ($(addRow).hasClass("ui-widget-header")) {
	        $(addRow).removeClass("ui-widget-header");
	    }
	    

	    addAllRows.setAttribute("disabled", "true");
	    
	    if ($(addAllRows).hasClass("ui-widget-header")) {
	        $(addAllRows).removeClass("ui-widget-header");
	    }
	    
	    var visibleRows = getVisibleRows("src_row_");
	    if (visibleRows > 0) {
	        addAllRows.removeAttribute("disabled");
	        $(addAllRows).addClass("ui-widget-header");
	    }
	}
	
	
	
	function addSubtestSelectedRow(row) {
	    if (row == null)
	        return false;
	    
	    var numberOfRows = 0;
	    if(isForStudentSubtestModification){
	    	numberOfRows =document.getElementById("numberOfRowsMsm").value;
	    }else {
	    	numberOfRows =document.getElementById("numberOfRows").value;
	    }
	    
	    // get Ids
	    var srcId = row.id;
	    var desId = "des" + srcId.substr(3, srcId.length);
	    
	    // remove row from source table
	    var srcRow = document.getElementById(srcId);
	    srcRow.style.display = getDisplayHide();
	    srcRow.className = 'dynamic';
	    
	    // add row into destination table
	    var destRow = document.getElementById(desId);
	    destRow.style.display = getDisplayShow();
	    
	    var table = destRow.parentNode;
	    var index = getFirstInvisibleRowIndex(table);
	    
	    if (index < numberOfRows) {
	        table.insertBefore(destRow, table.rows[index]);
	    } else {
	        table.appendChild(destRow);
	    }
	    
	    selectSubtestRow(destRow);
	    
	    setVisibleFlag(destRow);
	    
	    return true;
	}
	
	function addSubtestAllSelectedRows() {
	    var numberOfRows = 0;
	    if(isForStudentSubtestModification){
	    	numberOfRows =document.getElementById("numberOfRowsMsm").value;
	    }else {
	    	numberOfRows =document.getElementById("numberOfRows").value;
	    }
	    
	    for (var i = 1; i <= numberOfRows; i ++ ) {
	        var srcId = "src_row_" + i;
	        var srcRow = document.getElementById(srcId);
	        if (isRowVisible(srcRow)) {
	            addSubtestSelectedRow(srcRow);
	        }
	    }
	    
	    unSelectItem();
	    
	    return true;
	}
	
	function removeSubtestSelectedRow(row) {
	    if (row == null)
	        return false;
	    
	    // get Ids
	    var desId = row.id;
	    var srcId = "src" + desId.substr(3, desId.length);
	    
	    // remove row from destination table
	    var destRow = document.getElementById(desId);
	    destRow.style.display = getDisplayHide();
	    destRow.className = 'dynamic';
	    
	    // move to the end of the list after hide this row 
	    var table = destRow.parentNode;
	    table.appendChild(destRow);
	    
	    // add row into source table
	    var srcRow = document.getElementById(srcId);
	    srcRow.style.display = getDisplayShow();
	    
	    selectSubtestRow(srcRow);
	    
	    setVisibleFlag(destRow);
	    
	    return true;
	}
	
	function removeSubtestAllSelectedRows() {
	    var numberOfRows = 0;
	    if(isForStudentSubtestModification){
	    	numberOfRows =document.getElementById("numberOfRowsMsm").value;
	    }else {
	    	numberOfRows =document.getElementById("numberOfRows").value;
	    }
	    
	    for (var i = 1; i <= numberOfRows; i ++ ) {
	        var desId = "des_row_" + i;
	        var desRow = document.getElementById(desId);
	        if (isRowVisible(desRow)) {
	            removeSubtestSelectedRow(desRow);
	        }
	    }
	    
	    unSelectItem();
	    
	    return true;
	}
	
	function getVisibleRows(rowPrefix) {
	    var numberOfRows = 0;
	    if(isForStudentSubtestModification){
	    	numberOfRows =document.getElementById("numberOfRowsMsm").value;
	    }else {
	    	numberOfRows =document.getElementById("numberOfRows").value;
	    }
	    var visibleRows = 0;
	    
	    for (var i = 1; i <= numberOfRows; i ++ ) {
	        var id = rowPrefix + i;
	        var row = document.getElementById(id);
	        if (isRowVisible(row)) {
	            visibleRows = visibleRows + 1;
	        }
	    }
	    return visibleRows;
	}
	
	
	function setVisibleFlag(destRow) {
	    var table = destRow.parentNode;
	    
	    for (var i = 0; i < table.rows.length; i ++ ) {
	        var row = table.rows[i];
	        var column = row.cells[0];
	        var value = 0;
	        if (isRowVisible(row)) {
	            value = i + 1;
	        }
	        column.innerHTML = column.innerHTML.replace(/value=.*? /, 'value="' + value + '"');
	    }
	}
	
	function isRowVisible(row) {
	    if (row.style.display != "none")
	        return true;
	    else return false;
	}
	
	
	function getFirstInvisibleRowIndex(table) {
	    var index = table.rows.length - 1;
	    for (var i = 0; i < table.rows.length; i ++ ) {
	        var row = table.rows[i];
	        if (isRowHidden(row)) {
	            index = i;
	            break;
	        }
	    }
	    return index;
	}
	
	function getDisplayShow() {
	    if (isBrowerTypeIE())
	        return "block";
	    else return "table-row";
	}
	
	function getDisplayHide() {
	    return "none";
	}
	
	function isRowHidden(row) {
	    if (row.style.display == "none")
	        return true;
	    else return false;
	}
	
	function isRowVisible(row) {
	    if (row.style.display != "none")
	        return true;
	    else return false;
	}
	
	
	function isLastVisibleRow(desRow) {
	    var table = desRow.parentNode;
	    for (var i = table.rows.length - 1; i >= 0; i -- ) {
	        var row = table.rows[i];
	        if (isRowVisible(row)) {
	            if (row.id == desRow.id)
	                return true;
	            else return false;
	        }
	    }
	    return false;
	}
	
	
	function isFirstVisibleRow(desRow) {
	    var table = desRow.parentNode;
	    if (table.rows.length > 0) {
	        var row = table.rows[0];
	        if (row.id == desRow.id)
	            return true;
	    }
	    return false;
	}
	
	function unSelectItem() {
	    if (chosenRow != null) {
	        chosenRow.className = 'dynamic';
	    }
	    chosenRow = null;
	    
	    disableSubtestDestinationTableButtons();
	    
	    disableSubtestSourceTableButtons();
	}
	
	function getItemLevel( srcList, item)  {
        var level = "";
        for (var i=0 ; i<srcList.length ; i++) {        
            if (String(srcList[i].id)== String(item.id)) {
                level = srcList[i].level;
                if(level== undefined){
                	level = "";
                }
            }
        }
        return level;
    }
	
	
	
	function updateMsmLocatorValue(){
		var msmlocator = document.getElementById("msmHasAutolocator");
		//var tds = $('#selectedSubtestsTableMsm #subtestLevel');
		var removeSubtestLevel = false;
		if(msmlocator.checked){
			removeSubtestLevel = true;
			$("#modifyTestLevelMsm").hide();
			$("#msmLocatorInfo").hide();
			$("#msmLocatorInfo1").hide();
			$("#msmLocatorDetail").text("");
			
			
		} else {
			removeSubtestLevel = false;
			$("#modifyTestLevelMsm").show();
			if( locatorSessionInfo != undefined && locatorSessionInfo != ""){
				$("#msmLocatorDetail").text(locatorSessionInfo);
				$("#msmLocatorInfo").show();
				$("#msmLocatorInfo1").show();
				
			}
			
		}
		for( var ii=0, jj=allSubtestsMsm.length; ii<jj; ii++ ){
			if(removeSubtestLevel) {
				$("#subtestLevel_"+ii).hide();
			} else {
				$("#subtestLevel_"+ii).show();
			}
		}
	}
	
	function isExists(srcList, val) {
	    var found = false;
	    for (var i = 0; i < srcList.length; i ++ ) {
	        var arrayVal = srcList[i];
	        if (val != undefined && arrayVal.id == val.id) {
	            found = true;
	            break;
	        }
	    }
	    return found;
	    
	}
	
	function adjustHeightTables(){
		var sourceTableHeight = $("table.dynamicHeader","#rightTableContainer").outerHeight() + $("table.dynamic","#rightTableContainer").outerHeight();
		if(sourceTableHeight <200){
			sourceTableHeight = 200;
		}
		$("#rightTableContainer").height(sourceTableHeight);
		$("#leftTableContainer").height(sourceTableHeight);
	}
	
	function getOrgDropDownList(selectedStudentOrgNodeName, selectedStudentOrgNodeid) {
	    var ret = "";
		var orgNameArray = selectedStudentOrgNodeName.split("|");
		var orgIdArray   = selectedStudentOrgNodeid.split("|");
        	ret = '<select id="stdOrg" > ';
			for(var i=0;i<orgNameArray.length && i<orgIdArray.length ; i++){	
				ret += '<option value="'+ orgIdArray[i] +'" >'+orgNameArray[i]+" "+'</option> ';
    		}
    		ret += '</select>';
       	return ret;
	}
	
	function isBrowerTypeIE()
    {
        var agt = navigator.userAgent.toLowerCase();
        return (agt.indexOf("msie") != -1);
    }
    
    function isBrowerTypeFirefox()
    {
        var agt = navigator.userAgent.toLowerCase();
        return (agt.indexOf("firefox") != -1);
    }
    
    function isBrowerTypeMac()
    {
        var agt = navigator.userAgent.toLowerCase();
        return (agt.indexOf("mac") != -1);
    }
	
	
	function hideSubtestValidationMessage() {
	    $('#displaySubtestValidationMsg').hide();
	}
	
	
	function populateRecomendedLevel(recomendedLevelMap, isLocatorPresentForStd){
		if(subTestDetails.locatorSubtest == undefined || recomendedLevelMap.length==0){
			return;
		}
		
		if(isLocatorPresentForStd ){
		    for (var ii=0; ii<allSubtestsMsm.length; ii++ ) {
		         var recLevel= recomendedLevelMap[allSubtestsMsm[ii].id];
		    	if(recLevel != undefined){
		    	 	$("#level_"+(ii+1)).val(recLevel);
		    	}
		    }
		
		} else {
			for (var ii=0; ii<allSubtestsMsm.length; ii++ ) {
		         var recLevel= recomendedLevelMap[allSubtestsMsm[ii].id];
		          if (recLevel != undefined && !isExists(selectedSubtestsMsm, allSubtestsMsm[ii])) {
					$("#level_"+(ii+1)).val(recLevel);
		          }
		    }
			if( locatorSessionInfo != undefined && locatorSessionInfo!=""){
				$("#msmLocatorDetail").text(locatorSessionInfo);
				$("#msmLocatorInfo").show();
				$("#msmLocatorInfo1").show();
			}else {
				$("#msmLocatorInfo").hide();
				$("#msmLocatorInfo1").hide();
			}
		}
	}
	
	
	function validateAndScheduleStudent() {
	     var tmpSelectedSubtestsMsm = new Array();
	     subtestWarningMsg = "";
	     var validSubtest = validateSubtest(tmpSelectedSubtestsMsm);
	     if(validSubtest){
	        //closePopUp('modifyTestPopup');
	    	//closePopUp('sessionStudRegId');
	    	scheduleStudentAndOpenConfirmMationPopup(tmpSelectedSubtestsMsm);
	     }

	}
	
	function validateSubtest(tmpSelectedSubtestsMsm){
	
		hideMessage();
		var isValidated = true;
		var validateLevels = true;
	    //var tmpSelectedSubtestsMsm = new Array();
	    if(subTestDetails.locatorSubtest != undefined){
		  	validateLevels = !(document.getElementById("msmHasAutolocator").checked);
		 }
		 if(subTestDetails.subtests.length > 0){
		  	prepareMsmSelectedSubtests(tmpSelectedSubtestsMsm, validateLevels);
		  	if(isTabeProduct){
	    		isValidated = validateTABESubtest(tmpSelectedSubtestsMsm, validateLevels, true);
	    	} else {
	    		isValidated = validationTABE_ADAPTIVE (tmpSelectedSubtestsMsm, true);
	    	}
		 }
		
	return isValidated;
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
	
	
	function validateTABESubtest(subtests, validateLevels , isForStudentMsm) {
	    var isValid = true;
	   
	    if (subtests == undefined || subtests == null || subtests.length == 0) {
	        isValid = false;
         	setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#noSubtestMsg").val());
	    } else if ( ! languageDependency(subtests)) {
	        isValid = false;
         	 setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#languageDependencyMsg").val());
	    } else if(validateLevels && ! languageLevel(subtests)){
	     	isValid = false;
         	setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#languageLevelMsg").val());
	    } else if ( ! readingDependency(subtests)) {
	        isValid = false;
         	 setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#readingDependencyMsg").val());
	    } else if(validateLevels && ! readingLevel(subtests)){
	    	isValid = false;
         	setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#readingLevelMsg").val());
	    } else if(validateLevels && ! mathLevel(subtests)){
	    	isValid = false;
         	setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#mathLevelMsg").val());
	    }
	    
	    var currentMessage = "";
	        if ( ! mathSubtests(subtests)) {
	            currentMessage = $("#mathSubtestsMsg").val();
	        }
	        if ( ! scoreCalculatable(subtests)) {
	            if (currentMessage == "")
	                currentMessage = $("#scoreCalulatableMsg").val();
	            // just warning, not error
	            else {
	                currentMessage += "<br/>";
	                currentMessage += $("#scoreCalulatableMsg").val();
	                // just warning, not error
	            }
	        }
	     subtestWarningMsg = currentMessage;
	    
	    return isValid;
	}
	
	
	function validationTABE_ADAPTIVE (subtests , isForStudentMsm) { 
		var isValid = true;
		if (subtests == undefined || subtests == null || subtests.length == 0) {
	        isValid = false;
        	setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#noSubtestMsg").val());
	    } 
	    
	    var currentMessage = "";
	   	if ( ! mathSubtests_TABE_ADAPTIVE(subtests)) {
	    	currentMessage = $("#mathSubtestsMsg").val();
	    }
	    if ( ! scoreCalculatable_TABE_ADAPTIVE(subtests)) {
	    	if (currentMessage == "")
	        	currentMessage = $("#scoreCalulatableMsg").val();  // just warning, not error
	        else {
	            currentMessage += "<br/>";
	           	currentMessage += $("#scoreCalulatableMsg").val();  // just warning, not error
	       }
	    }
	    subtestWarningMsg = currentMessage;	
	 	return isValid;
	}
	
	function languageDependency(subtests) {
	    var isValid = true;
	    if (presented(LANGUAGE_MECHANICS, subtests) || presented(SPELLING, subtests)) {
	        if ( ! presented(LANGUAGE, subtests)) {
	            isValid = false;
	        }
	    }
	    
	    return isValid;
	}
	
	
	function readingDependency(subtests) {
	    var isValid = true;
	    if (presented(VOCABULARY, subtests)) {
	        if ( ! presented(READING, subtests)) {
	            isValid = false;
	        }
	    }
	    return isValid;
	}
	
	
	function mathSubtests(subtests) {
	    var isValid = false;
	    if (presented(MATH_COMPUTATION, subtests) && presented(APPLIED_MATH, subtests)) {
	        isValid = true;
	    }
	    
	    
	    return isValid;
	}
	
	function mathSubtests_TABE_ADAPTIVE(subtests) {
	    var isValid = false;
	    if (presented(TABE_ADAPTIVE_MATH_COMPUTATION, subtests) && presented(TABE_ADAPTIVE_APPLIED_MATH, subtests)) {
	        isValid = true;
	    }
	    return isValid;
	}

	function presented(subtestName, subtests) {
	    for (var i = 0; i < subtests.length; i ++ ) {
	        var subtest = subtests[i];
	        if (subtestName == subtest.subtestName)
	            return true;
	    }
	    return false;
	}
	
	
	function mathLevel( subtests)    {
        if (presented(MATH_COMPUTATION, subtests) && presented(APPLIED_MATH, subtests)) {
            if (! samelevel(MATH_COMPUTATION, APPLIED_MATH, subtests)) {
                return false;
            }
        }
        return true;
    }
    
    
	function languageLevel( subtests)    {
        if (presented(LANGUAGE, subtests) && presented(LANGUAGE_MECHANICS, subtests)) {
            if (! samelevel(LANGUAGE, LANGUAGE_MECHANICS, subtests)) {
                return false;
            }
        }
        if (presented(LANGUAGE, subtests) && presented(SPELLING, subtests)) {
            if (! samelevel(LANGUAGE, SPELLING, subtests)) {
                return false;
            }
        }
        return true;
    }
    
    
	function readingLevel( subtests)   {
        if (presented(READING, subtests) && presented(VOCABULARY, subtests)) {
            if (! samelevel(READING, VOCABULARY, subtests)) {
                return false;
            }
        }
        return true;
    }
    
    function scoreCalculatable(subtests) {
		if (presented(READING, subtests) && presented(LANGUAGE, subtests) && presented(MATH_COMPUTATION, subtests) && presented(APPLIED_MATH, subtests)) {
	        return true;
	    }
	    return false;
	}
    
    function scoreCalculatable_TABE_ADAPTIVE(subtests) {
	    if (presented(TABE_ADAPTIVE_READING, subtests) && presented(TABE_ADAPTIVE_LANGUAGE, subtests) && presented(TABE_ADAPTIVE_MATH_COMPUTATION, subtests) && presented(TABE_ADAPTIVE_APPLIED_MATH, subtests)) {
	        return true;
	    }
	    return false;
	}
	
	function samelevel( subtestName1,  subtestName2,  subtests)
    {
        var level1 = getlevel(subtestName1, subtests);
        var level2 = getlevel(subtestName2, subtests);
        
        if (level1 == null || level1 == undefined || level1 == "") level1 = "E";
        if (level2 == null || level2 == undefined || level2 == "" ) level2 = "E";
        
        if (level1==level2)
            return true;
        
        return false;
    }
    
    function getlevel( subtestName,  subtests)
    {
        for (var i=0 ; i<subtests.length ; i++) {
            var subtest = subtests[i];
            if (subtestName == subtest.subtestName) 
                return subtest.level;
        }
        return null;
    }
    
    function scheduleStudentAndOpenConfirmMationPopup(finalSelectedSubtestsMsm){
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
			 	msmParam += "&subtestName_l=" +formatString(subTestDetails.locatorSubtest.subtestName);
			 }
			for(var ii=0; ii<finalSelectedSubtestsMsm.length; ii++){
				msmParam += "&itemSetIdTD=" + finalSelectedSubtestsMsm[ii].id;
			   	if(finalSelectedSubtestsMsm[ii].level != undefined){
			   		msmParam += "&itemSetForm=" + finalSelectedSubtestsMsm[ii].level;
			   	}
			   	msmParam += "&subtestName=" +formatString(finalSelectedSubtestsMsm[ii].subtestName);
			}  
	        param += "testAdminId="+ selectedTestAdminId;
			param += "&studentId=" + selectedStudentId;
			param += "&studentOrgNodeId=" + $("#stdOrg").val();
			param += msmParam;
			
			
			$.ajax({
				async:		true,
				beforeSend:	function(){
								UIBlock();
							},
				url:		'registerStudent.do',
				type:		'POST',
				data:		 param ,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){
							 if(data.status.isSuccess){
							    closePopUp('modifyTestPopup');
		    					closePopUp('sessionStudRegId');
		    					$("#SubtestDetails").html("");
							 	displayStudConfirmation();
							 	populateStudConfirmation(data);
							 } else {
							  	setSubtestValidationMessage($("#studentRegistrationFailed").val(),"");
							 
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
	    
	   function populateStudConfirmation(data){
	   
	    if(subtestWarningMsg!=undefined && subtestWarningMsg != null && subtestWarningMsg.length>0) {
			$("#subtestWarnMsg").show();
			$("#subtestWarnMsgcontent").html(subtestWarningMsg);
	    } else {
	    	$("#subtestWarnMsg").hide();
	    	$("#subtestWarnMsgcontent").html("");
	    }
	   
	     $("#dstudentName").text(data.studentName);
	    
	     if(data.enforceBreak=="No"){
	      	$("#enforceBreakNo").text(data.testAccessCode);
	      	$("#enforceBreakNo").show();
	      	$("#enforceBreakYes").hide();
	     } else {
	     $("#enforceBreakNo").text("");
	     	$("#enforceBreakNo").hide();
	     	$("#enforceBreakYes").show();
	     }
	     $("#dloginName").text(data.loginName);
	     $("#dpassword").text(data.password);
	     $("#dtestName").text(data.testName);
	     $("#dcreatorOrgNodeName").text(data.creatorOrgNodeName);
	     $("#dstartDate").text(data.startDate);
	     $("#dstartTime").text(data.startTime);
	     if(data.isLocatorTest){
	     	$("#dDisplayAutolocator").hide();
	     } else {
	     	$("#dDisplayAutolocator").show();
	     	 
	     	 $("#dautoLocatorDisplay").text(data.autoLocatorDisplay);
	     }
	     $("#dtestAdminName").text(data.testAdminName);
	     $("#dsessionNumber").text(data.sessionNumber);
	     $("#dendDate").text(data.endDate);
	     $("#dendTime").text(data.endTime);
	     if(data.isLocatorTest){
	     	$("#dShowHideAllowBreak").hide();
	     } else {
	     	$("#dShowHideAllowBreak").show();
	     	 $("#denforceBreak").text(data.enforceBreak);
	     }
	     $("#dorgNodeId").val(data.studentOrgId);
	     if(data.showAccessCode){
	     	$("#showAccessCode").show();
	     } else {
	     	$("#showAccessCode").hide();
	     }		
     
	     if(data.autoLocator){
	     	$("#locatorDiv").show();
	     	$("#loc_subtest_name").text(data.locatorSubtest.itemSetName);
	     	$("#loc_subtest_name").show();
	     	if(data.enforceBreak=="Yes"){
	     		$("#loc_accesscode_value").text(data.locatorSubtest.accessCode);
	     		$("#loc_accesscode_header").show();
	     		$("#loc_accesscode_value").show();
	     	} else {
	     		$("#loc_accesscode_header").hide();
	     		$("#loc_accesscode_value").hide();
	     		$("#loc_accesscode_value").text("");
	     	}
	     	
	     } else {
	     	$("#locatorDiv").hide();
	     }
     
	     if(data.isLocatorTest || data.selectedSubtests.length<1){
	     	$("#SubtestDetails").html("");
	     	$("#SubtestDetails").hide();
	     } else {
	        var subtestTable = getSubtestTableData(data);
	        $("#SubtestDetails").html(subtestTable);
	        $("#SubtestDetails").show();
	     }
     
    
    }
    
    
    
    function  getSubtestTableData(data){
      var retString = "";
      var autolocator =  data.autoLocator;
       	retString +='<table align="center" width="95%">	<tr>	<td>	<table class="sortable">';
      	retString +='<tr class="sortable"> <th class="ui-widget-header alignCenter" height="25">';
      	retString +=$("#subtestDisplayorder").val();
      	retString +='</th> '; 
      	retString +=' <th class="ui-widget-header alignLeft" height="25">&nbsp;&nbsp;';
      	retString +=  $("#subtestDisplayName").val();
      	retString +='</th> ';  
      	if(data.enforceBreak=="Yes"){
      		retString +=' <th class="ui-widget-header alignLeft" height="25">&nbsp;&nbsp;';
      		retString +=  $("#subtestDisplayAccessCode").val();
      		retString +='</th> '; 
      	}
      	           
		if(!autolocator){
			retString +=' <th class="ui-widget-header alignLeft" height="25">&nbsp;&nbsp;';
	      	retString +=  $("#subtestDisplayLevel").val();
	      	retString +='</th> '; 
		}			        
		retString += '</tr>';			        
      
      
      for (var ii = 0,jj = data.selectedSubtests.length; ii < jj; ii ++ ) {
      	retString += '<tr class="sortable"><td class="sortable alignCenter">';
      	retString += (ii+1);
       	retString += '</td>';
       	retString +='<td class="sortable">';
       	retString +=data.selectedSubtests[ii].itemSetName;
       	retString +='</td>';
       	if(data.enforceBreak=="Yes"){
       		retString +='<td class="sortable">';
       		retString +=data.selectedSubtests[ii].testAccessCode;
       		retString +='</td>';
       	}
       	if(!autolocator){
       		retString +='<td class="sortable">';
       		retString +=data.selectedSubtests[ii].itemSetForm;
       		retString +='</td>';
       	}
       	
       	retString += '</tr>';
      }
	
	  retString +='</table>   </td>	</tr> </table>';
    
      return retString;
    
    }
    
    
    
    function formatString( inputString){
		var outputString = inputString.replace(/&/g,'')
		return outputString;
	}
    
	function setSubtestValidationMessage(title, content) {
	    $("#subTitle").text(title);
	    $("#subContent").html(content);
	    $('#displaySubtestValidationMsg').show();
	}
	
	function hideMessage(){
         $("#subTitle").text("");
	    $("#subContent").html("");
	    $('#displaySubtestValidationMsg').hide();
	}
	
	function openTestTicketIndividual( anchor ) {
		var testAdminId = selectedTestAdminId;
		var orgNodeId= $("#dorgNodeId").val();
		var studentId =selectedStudentId;
		var url = "/SessionWeb/testTicketOperation/individualTestTicket.do";
		return openTestTicket( "individual", anchor, url, testAdminId, orgNodeId, studentId  );
	}
	function openTestTicket( ticketType, anchor, url, testAdminId, orgNodeId, studentId ) {
	    anchor.href  = url;
	    anchor.href += "?testAdminId=" + testAdminId;
	    anchor.href += "&orgNodeId=" + orgNodeId;
	    anchor.href += "&studentId=" + studentId;
	    anchor.href += "&ticketType=" + ticketType; 
	    anchor.href += "&displayAccess=" + displayAccessCodeOnTicket;   
	    return true;
	}
	
	function accessCode() {
	
	var checkAccess = document.getElementsByName("individualAccess");
	displayAccessCodeOnTicket = checkAccess[0].checked;
 }