	var hasAutolocator = false;
	var locatorSubtest = {};
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
	
	var LASLINKS_SPEAKING = "Speaking";
	var LASLINKS_LISTENING = "Listening";
	var LASLINKS_READING = "Reading";
	var LASLINKS_WRINTING = "Writing";
	
	
	
	function getCopy(array) {
	    var tmpArray = new Array();
	    for (var ii = 0; ii < array.length; ii ++ ) {
	        tmpArray[ii] = array[ii];
	    }
	    
	    return tmpArray;
	}
	function validateAndUpdateSubtest() {
	    var isValidated = true;
	    var tmpSelectedSubtests = new Array();
	    prepareSelectedSubtests(tmpSelectedSubtests);
	    
	    if(isTabeProduct){
	    	var validateLevels = false; 
	    	if(locatorSubtest!=null && locatorSubtest!= undefined && locatorSubtest.id!=undefined && !hasAutolocator) {
	    		validateLevels = true;
	    	}
	    	isValidated = validateTABESubtest(tmpSelectedSubtests, validateLevels, false);
	    } else if(isTabeAdaptiveProduct ) {
	    	isValidated = validationTABE_ADAPTIVE(tmpSelectedSubtests, false);
	    }
	    
	    if(isLasLinksProduct){
			isValidated = validateLASLINKSSubtest(tmpSelectedSubtests);
			
	    }
	    
	    if (isValidated) {
	        subtestGridLoaded = false;
	        selectedSubtests = tmpSelectedSubtests;
	        createSubtestGrid();
	        updateAllSubtests(allSubtests, selectedSubtests);
	        populateAllSubtestMap(allSubtests) 
	        updateOldAccessCodes(selectedSubtests);
	        closePopUp('modifyTestPopup');
	    }
	    
	    
	}
	
	function updateOldAccessCodes(subtests) {
		 if(isTestBreak) {
		    for(var i=0; i<subtests.length; i++){	
				if(selectedAccessCodeMap.get(subtests[i].id)!=null){
					  ($("#"+subtests[i].id).children() [0]).value = selectedAccessCodeMap.get(subtests[i].id);
				} else {
					var newAccessCode = getSelectUniqueAccessCode();
					if(newAccessCode!=undefined) {
						selectedAccessCodeMap.put(subtests[i].id, newAccessCode);
						($("#"+subtests[i].id).children() [0]).value = newAccessCode;
					}
						
				}
			}
			if(locatorSubtest!=null && locatorSubtest!= undefined && locatorSubtest.id!=undefined && hasAutolocator) {
				 ($("#"+locatorSubtest.id).children() [0]).value = selectedAccessCodeMap.get(locatorSubtest.id);
			}
	    } else {
	    	$("#aCode").val(selectedAccessCodeMap.get("AccessCode"));
	    }
	}
	
	function getSelectUniqueAccessCode() {
		for(var i=0; i<ProductData.accessCodeList.length; i++) {
			var found = false;
			var keys = selectedAccessCodeMap.getKeys();
			for(var j=0; j<keys.length; j++) {
				if(String(selectedAccessCodeMap.get(keys[j])).toLowerCase() ==  ProductData.accessCodeList[i].toLowerCase() ){
					found = true;
					break;
				}
			}
			if( !found )
				return  ProductData.accessCodeList[i];
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
	
	function getCurrentIndex(allSubtestsSrc ,id) {
	    for (var ii = 0; ii < allSubtestsSrc.length; ii ++ ) {
	        if (allSubtestsSrc[ii].id == id) {
	            return ii;
	        }
	        
	    }
	    return - 1;
	}
	
	
	function validationTABE_ADAPTIVE (subtests , isForStudentMsm) { 
		var isValid = true;
		if (subtests == undefined || subtests == null || subtests.length == 0) {
	        isValid = false;
	        if(isForStudentMsm){
	         	setMsmErrorMessage($("#subtestValidationFailedMsg").val(), $("#noSubtestMsg").val());
	        } else {
	        	 setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#noSubtestMsg").val());
	        }
	       
	    } else if ( !isForStudentMsm ) {
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
	        if (currentMessage != "") {
	            setSubtestWarningMessage(currentMessage);
	        } else {
	            hideSubtestWarningMessage(currentMessage);
	        }
	    }
	
	 return isValid;
	}
	
	function validateLASLINKSSubtest(subtests) {
		var isValid = true;
		if (subtests == undefined || subtests == null || subtests.length == 0) {
	        isValid = false;
	        setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#noSubtestMsg").val());
	    	  //Message = NO_SUBTEST_MSG;
	    
        }
        
        /**
        else if (! comprehensionDependency(subtests)) {
        	isValid = false;
            setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#comprehensionDependencyMsg").val());
        
        }else if (! oralDependency(subtests)) {
        	isValid = false;
            setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#oralDependencyMsg").val());
        }
        **/
     return isValid;
	}
	
	function validateTABESubtest(subtests, validateLevels , isForStudentMsm) {
	    var isValid = true;
	   
	    if (subtests == undefined || subtests == null || subtests.length == 0) {
	        isValid = false;
	        if(isForStudentMsm) {
	        	setMsmErrorMessage($("#subtestValidationFailedMsg").val(), $("#noSubtestMsg").val());
	        } else {
	         	setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#noSubtestMsg").val());
	        }
	       
	    } else if ( ! languageDependency(subtests)) {
	        isValid = false;
	         if(isForStudentMsm) {
	        	setMsmErrorMessage($("#subtestValidationFailedMsg").val(), $("#languageDependencyMsg").val());
	        } else {
	         	 setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#languageDependencyMsg").val());
	        }
       
	    } else if(validateLevels && ! languageLevel(subtests)){
	     	isValid = false;
	     	if(isForStudentMsm) {
	        	setMsmErrorMessage($("#subtestValidationFailedMsg").val(), $("#languageLevelMsg").val());
	        } else {
	         	setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#languageLevelMsg").val());
	        }
	    } else if ( ! readingDependency(subtests)) {
	        isValid = false;
	       if(isForStudentMsm) {
	        	 setMsmErrorMessage($("#subtestValidationFailedMsg").val(), $("#readingDependencyMsg").val());
	        } else {
	         	 setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#readingDependencyMsg").val());
	        }
	    } else if(validateLevels && ! readingLevel(subtests)){
	    	isValid = false;
	    	if(isForStudentMsm) {
	        	setMsmErrorMessage($("#subtestValidationFailedMsg").val(), $("#readingLevelMsg").val());
	        } else {
	         	setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#readingLevelMsg").val());
	        }
	    } else if(validateLevels && ! mathLevel(subtests)){
	    	isValid = false;
	    	if(isForStudentMsm) {
	        	setMsmErrorMessage($("#subtestValidationFailedMsg").val(), $("#mathLevelMsg").val());
	        } else {
	         	setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#mathLevelMsg").val());
	        }
	    }
	    // test for level
	    else if (!isForStudentMsm) {
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
	        if (currentMessage != "") {
	            setSubtestWarningMessage(currentMessage);
	        } else {
	            hideSubtestWarningMessage(currentMessage);
	        }
	    }
	    return isValid;
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
	/** Laslinks comprehensionDependency Validation (R and L together) **/
	function comprehensionDependency(subtests) {
        var isValid = true;
        if (presented(LASLINKS_READING, subtests)) {
            if (! presented(LASLINKS_LISTENING, subtests)) {
                isValid = false;
            }
        }
        if (presented(LASLINKS_LISTENING, subtests)) {
            if (! presented(LASLINKS_READING, subtests)) {
                isValid = false;
            }
        }
        return isValid;
    }
	/** Laslinks oralDependency Validation (L and S together) **/
    function oralDependency(subtests)
    {
        var isValid = true;
   		if (presented(LASLINKS_LISTENING, subtests)) {
            if (! presented(LASLINKS_SPEAKING, subtests)) {
                isValid = false;
            }
        }
        if (presented(LASLINKS_SPEAKING, subtests)) {
            if (! presented(LASLINKS_LISTENING, subtests)) {
                isValid = false;
            }
        }return isValid;
    }
	function prepareSelectedSubtests(tmpSelectedSubtests) {
	     var setLevels = false;
	     if(locatorSubtest!=null && locatorSubtest!= undefined && locatorSubtest.id!=undefined && !hasAutolocator) {
	    	setLevels = true;
	    }
	    var selectetedTestRowCount = $("#selectedSubtestsTable input").length;
	    for (var ii = 1; ii <= selectetedTestRowCount; ii ++ ) {
	        var val = $("#index_" + ii).val();
	        if (val > 0) {
	            tmpSelectedSubtests[val - 1] = allSubtestMap.get(ii - 1);
	            if(setLevels) {
	            	var level = $("#level_"+ii).val();
	            	tmpSelectedSubtests[val - 1].level = level;
	            } else {
	            	
	            }
	        }
	        
	    }
	}
	
	function populateAllSubtestMap(valueArray) {
	    allSubtestMap = new Map();
	    for (var i = 0; i < valueArray.length; i ++ ) {
	        allSubtestMap.put(i, valueArray[i]);
	        
	    }
	    
	}
	
	function populateSelectedSubtestsMap(valueArray) {
	    selectedSubtestsMap = new Map();
	    var order = 1;
	    for (var i = 0; i < valueArray.length; i ++ ) {
	        var index = getIndexOfMap(allSubtestMap, valueArray[i]);
	        if (index != null && index != undefined) {
	            selectedSubtestsMap.put(order ++, index);
	        }
	    }
	    
	}
	
	function getIndexOfMap(allSubtestMap, val) {
	    var keys = allSubtestMap.getKeys();
	    var index = null;
	    for (var i = 0; i < keys.length; i ++ ) {
	        if (String(allSubtestMap.get(keys[i]).id) == String(val.id)) {
	            index = keys[i];
	            break;
	        }
	    }
	    return index;
	}
	
	function openModifyTestPopup(element) {
		if(isButtonDisabled(element))
			return true;
		isForStudentSubtestModification = false;//used for subtest modification false for testadmin	
		$("#selectedSubtestsTableMsm").html("");
        $("#availableSubtestsTableMsm").html("");
        $("#selectedSubtestsTable").html("");
        $("#availableSubtestsTable").html("");
		hideSubtestValidationMessage();
	    UIBlock();
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
	    var toppos = (($(window).height() - 290) / 2) + 50 + 'px';
	    var leftpos = (($(window).width() - 968) / 2) + 'px';
	    $("#modifyTestPopup").parent().css("top", toppos);
	    $("#modifyTestPopup").parent().css("left", leftpos);
	    
	    updateEditSubtestTable();
	    selectedAccessCodeMap = new Map();
	    if(isTestBreak) {
		    for(var i=0;i<subtestLength;i++){	
		   			var itemSetIdTd = document.getElementById("itemSetIdTD"+i).value;
			      	var selectedAccessCode = document.getElementById("aCodeB"+i).value;
			      	selectedAccessCodeMap.put(itemSetIdTd, selectedAccessCode);
			}
			if(locatorSubtest!=null && locatorSubtest!= undefined && locatorSubtest.id!=undefined && hasAutolocator) {
				var itemSetIdTd = document.getElementById("itemSetIdTD").value;
			    var selectedAccessCode = document.getElementById("aCodeB_l").value;
				selectedAccessCodeMap.put(itemSetIdTd, selectedAccessCode);
			}
	    	
	    } else {
				var selectedAccessCode = document.getElementById("aCode").value;
				selectedAccessCodeMap.put('AccessCode', selectedAccessCode);
	    
	    }
	    
	    $.unblockUI();
	}
	
	
	function updateEditSubtestTable() {
	    $("#addRow").attr("disabled", true);
	    $("#addAllRows").attr("disabled", true);
	    $("#removeRow").attr("disabled", true);
	    $("#removeAllRows").attr("disabled", true);
	    $("#moveUp").attr("disabled", true);
	    $("#moveDown").attr("disabled", true);
	    
	    if ($("#addRow").hasClass("ui-widget-header")) {
	        $("#addRow").removeClass("ui-widget-header");
	    }
	    if ($("#addAllRows").hasClass("ui-widget-header")) {
	        $("#addAllRows").removeClass("ui-widget-header");
	    }
	    if ($("#removeRow").hasClass("ui-widget-header")) {
	        $("#removeRow").removeClass("ui-widget-header");
	    }
	    if ($("#removeAllRows").hasClass("ui-widget-header")) {
	        $("#removeAllRows").removeClass("ui-widget-header");
	    }
	    
	    if ($("#moveUp").hasClass("ui-widget-header")) {
	        $("#moveUp").removeClass("ui-widget-header");
	    }
	    if ($("#moveDown").hasClass("ui-widget-header")) {
	        $("#moveDown").removeClass("ui-widget-header");
	    }
	    
	    if(locatorSubtest!=null && locatorSubtest != undefined && locatorSubtest.id!=undefined && !hasAutolocator) { 
	         $("#modifyTestLevel").show();
	   } else {
	          $("#modifyTestLevel").hide();
	   }
	   if(isTabeProduct){ 
	   		$("#modifySubtestMsg").html($("#tabeModifySubtestMsg").val());
	   } else {
	   		$("#modifySubtestMsg").html($("#tabeAdaptiveModifySubtestMsg").val());
	   }
	   if(isLasLinksProduct){
	   		$("#modifySubtestMsg").html($("#laslinksModifySubtestMsg").val());
	   }
	   
	    displaySourceTable(allSubtests, selectedSubtests,'availableSubtestsTable');
	    var isProductHasLocator = ( locatorSubtest != null && locatorSubtest != undefined && locatorSubtest.id != undefined) ? true : false ;
	    var haslocator= (locatorSubtest!=null && locatorSubtest != undefined && locatorSubtest.id!=undefined && hasAutolocator);
	    displayDestinationTable(allSubtests, selectedSubtests, ProductData.levelOptions, false, isProductHasLocator, haslocator);
	    
	    if (selectedSubtests != undefined && selectedSubtests.length > 0) {
	        $("#removeAllRows").attr("disabled", false);
	        $("#removeAllRows").addClass("ui-widget-header");
	    }
	    if (selectedSubtests != undefined && allSubtests != undefined && allSubtests.length > selectedSubtests.length) {
	        $("#addAllRows").attr("disabled", false);
	        $("#addAllRows").addClass("ui-widget-header");
	    }
	    var noOfRows = 0;
	    if (allSubtests != undefined) {
	        noOfRows = allSubtests.length;
	    }
	    $("#numberOfRows").val(noOfRows);
	    
	    
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
	
	function adjustHeightTables(){
		var sourceTableHeight = $("table.dynamicHeader","#rightTableContainer").outerHeight() + $("table.dynamic","#rightTableContainer").outerHeight();
		if(sourceTableHeight <200){
			sourceTableHeight = 200;
		}
		$("#rightTableContainer").height(sourceTableHeight);
		$("#leftTableContainer").height(sourceTableHeight);
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
	
	
	function isExists(srcList, val) {
	    var found = false;
	    for (var i = 0; i < srcList.length; i ++ ) {
	        var arrayVal = srcList[i];
	        if (arrayVal.id == val.id) {
	            found = true;
	            break;
	        }
	    }
	    return found;
	    
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
	    	removeAllRows = document.getElementById("removeAllRows");
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
	    	removeAllRows = document.getElementById("removeAllRows");
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
	
	
	function setSubtestValidationMessage(title, content) {
	    $("#subTitle").text(title);
	    $("#subContent").html(content);
	    $('#displaySubtestValidationMsg').show();
	}
	
	function hideSubtestValidationMessage() {
	    $('#displaySubtestValidationMsg').hide();
	}
	
	
	function setSubtestWarningMessage(content) {
	    $("#contentEditSubtest").html(content);
	    $('#displayEditSubtestInfo').show();
	}
	
	function hideSubtestWarningMessage() {
	    $('#displayEditSubtestInfo').hide();
	}
	
	function hideLocatorMessage() {
	    clearTimeout(htimer);
	    $('#statusLocatorLegend').hide();
	    //document.getElementById("statusLocatorLegend").style.display = "none";
	}
	
	function showLocatorMessage(event) {
	    var isIE = document.all ? true: false;
	    var tempX = 0;
	    var tempY = 0;
	    var legendDiv = null;
	    var padding = 15;
	    
	    if (isIE) {
	        tempX = event.clientX + document.documentElement.scrollLeft;
	        tempY = event.clientY + document.documentElement.scrollTop;
	    } else {
	        tempX = event.pageX;
	        tempY = event.pageY;
	    }
	    legendDiv = document.getElementById("statusLocatorLegend");
	    legendDiv.style.left = (tempX - $(legendDiv).width() / 3) + "px";
	    legendDiv.style.top = (tempY - $(legendDiv).height() - padding) + "px";
	    legendDiv.style.display = "block";
	    htimer = setTimeout("hideLocatorMessage()", 10000);
	}
	
	function getNumberSelectedSubtests() {
		if ((selectedSubtests != null) && (selectedSubtests != undefined)) 
			return selectedSubtests.length;
		else
			return 0;
	}