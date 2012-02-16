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
	    isValidated = validateTABESubtest(tmpSelectedSubtests);
	    if (isValidated) {
	        subtestGridLoaded = false;
	        selectedSubtests = tmpSelectedSubtests;
	        createSubtestGrid();
	        updateAllSubtests(selectedSubtests);
	        closePopUp('modifyTestPopup');
	    }
	    
	    
	}
	
	function updateAllSubtests(subtests) {
	    for (var ii = 0; ii < subtests.length; ii ++ ) {
	        var indx = getCurrentIndex(subtests[ii].id);
	        var tmp = allSubtests[indx];
	        allSubtests[indx] = allSubtests[ii];
	        allSubtests[ii] = tmp;
	    }
	    
	}
	
	function getCurrentIndex(id) {
	    for (var ii = 0; ii < allSubtests.length; ii ++ ) {
	        if (allSubtests[ii].id == id) {
	            return ii;
	        }
	        
	    }
	    return - 1;
	}
	
	function validateTABESubtest(subtests) {
	    var isValid = true;
	    if (subtests == undefined || subtests == null || subtests.length == 0) {
	        isValid = false;
	        setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#noSubtestMsg").val());
	    } else if ( ! languageDependency(subtests)) {
	        isValid = false;
	        setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#languageDependencyMsg").val());
	    } else if ( ! readingDependency(subtests)) {
	        isValid = false;
	        setSubtestValidationMessage($("#subtestValidationFailedMsg").val(), $("#readingDependencyMsg").val());
	    }
	    // test for level
	    else {
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
	
	function scoreCalculatable(subtests) {
	    if (presented(READING, subtests) && presented(LANGUAGE, subtests) && presented(MATH_COMPUTATION, subtests) && presented(APPLIED_MATH, subtests)) {
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
	function presented(subtestName, subtests) {
	    for (var i = 0; i < subtests.length; i ++ ) {
	        var subtest = subtests[i];
	        if (subtestName == subtest.subtestName)
	            return true;
	    }
	    return false;
	}
	function prepareSelectedSubtests(tmpSelectedSubtests) {
	    var selectetedTestRowCount = $("#selectedSubtestsTable input").length;
	    for (var ii = 1; ii <= selectetedTestRowCount; ii ++ ) {
	        var val = $("#index_" + ii).val();
	        if (val > 0) {
	            tmpSelectedSubtests[val - 1] = allSubtestMap.get(ii - 1);
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
	
	
	
	
	
	function openModifyTestPopup() {
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
	    
	    displaySourceTable();
	    displayDestinationTable();
	    
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
	
	function displaySourceTable() {
	    var destHtmlText = "";
	    for (var ii = 0, jj = allSubtests.length; ii < jj; ii ++ ) {
	        var found = false;
	        var displayFlag = "none";
	        var value = String(ii + 1);
	        if ( ! isExists(selectedSubtests, allSubtests[ii])) {
	            displayFlag = "table-row";
	        } else {
	            value = "0";
	        }
	        var index = ii + 1;
	        destHtmlText += displaySourceRowStart(index, displayFlag);;
	        destHtmlText += displaySourceCell(allSubtests[ii].subtestName);
	    }
	    
	    $("#availableSubtestsTable").html(destHtmlText);  
	    setTimeout("adjustHeightTables()",100);
	}
	
	function adjustHeightTables(){
		var sourceTableHeight = $("table.dynamicHeader","#rightTableContainer").outerHeight() + $("table.dynamic","#rightTableContainer").outerHeight();
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
	
	function displayDestinationTable() {
	    var destHtmlText = "";
	    if (allSubtests == null) {
	        allSubtests = new Array();
	    }
	    
	    for (var ii = 0, jj = allSubtests.length; ii < jj; ii ++ ) {
	        var found = false;
	        var displayFlag = "none";
	        var value = String(ii + 1);
	        if (isExists(selectedSubtests, allSubtests[ii])) {
	            displayFlag = "table-row";
	        } else {
	            value = "0";
	        }
	        var index = ii + 1;
	        destHtmlText += displayDestinationRowStart(index, displayFlag);
	        destHtmlText += displayDestinationNameCell(allSubtests[ii].subtestName, index, value);
	    }
	    $("#selectedSubtestsTable").html(destHtmlText);
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
	    var targetIndex = row.rowIndex + delta;
	    
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
	    var removeRow = document.getElementById("removeRow");
	    removeRow.removeAttribute("disabled");
	    $(removeRow).addClass("ui-widget-header");
	    
	    var removeAllRows = document.getElementById("removeAllRows");
	    removeAllRows.removeAttribute("disabled");
	    $(removeAllRows).addClass("ui-widget-header");
	    
	    
	    
	    var moveUp = document.getElementById("moveUp");
	    moveUp.removeAttribute("disabled");
	    $(moveUp).addClass("ui-widget-header");
	    
	    var moveDown = document.getElementById("moveDown");
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
	    var removeRow = document.getElementById("removeRow");
	    removeRow.setAttribute("disabled", "true");
	    if ($(removeRow).hasClass("ui-widget-header")) {
	        $(removeRow).removeClass("ui-widget-header");
	    }
	    var removeAllRows = document.getElementById("removeAllRows");
	    removeAllRows.setAttribute("disabled", "true");
	    
	    if ($(removeAllRows).hasClass("ui-widget-header")) {
	        $(removeAllRows).removeClass("ui-widget-header");
	    }
	    
	    var visibleRows = getVisibleRows("des_row_");
	    if (visibleRows > 0) {
	        removeAllRows.removeAttribute("disabled");
	        $(removeAllRows).addClass("ui-widget-header");
	        
	    }
	    
	    var moveUp = document.getElementById("moveUp");
	    moveUp.setAttribute("disabled", "true");
	    if ($(moveUp).hasClass("ui-widget-header")) {
	        $(moveUp).removeClass("ui-widget-header");
	    }
	    
	    var moveDown = document.getElementById("moveDown");
	    moveDown.setAttribute("disabled", "true");
	    if ($(moveDown).hasClass("ui-widget-header")) {
	        $(moveDown).removeClass("ui-widget-header");
	    }
	}
	
	function enableSubtestSourceTableButtons() {
	    var addRow = document.getElementById("addRow");
	    addRow.removeAttribute("disabled");
	    $(addRow).addClass("ui-widget-header");
	    
	    var addAllRows = document.getElementById("addAllRows");
	    addAllRows.removeAttribute("disabled");
	    $(addAllRows).addClass("ui-widget-header");
	}
	
	function disableSubtestSourceTableButtons() {
	    var addRow = document.getElementById("addRow");
	    addRow.setAttribute("disabled", "true");
	    if ($(addRow).hasClass("ui-widget-header")) {
	        $(addRow).removeClass("ui-widget-header");
	    }
	    
	    var addAllRows = document.getElementById("addAllRows");
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
	    
	    var numberOfRows = document.getElementById("numberOfRows").value;
	    
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
	    var numberOfRows = document.getElementById("numberOfRows").value;
	    
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
	    var numberOfRows = document.getElementById("numberOfRows").value;
	    
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
	    var numberOfRows = document.getElementById("numberOfRows").value;
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