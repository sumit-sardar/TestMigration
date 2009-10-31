/**
 * dynamic_table.js
 */


var chosenRow = null;

/**
*
*/
function initCreation()
{
    var table = document.getElementById('frameworkTable');
    var row = table.rows[2];
    selectRow(row);
    enableEditText(row);   
    setButtonState();                  
}

/**
*
*/
function hilightRow(row) 
{
    if( row != chosenRow ) {
        row.className = 'dynamicHilight'; 
    }
    return true;
}


/**
*
*/
function unhilightRow(row) 
{
    if( row == chosenRow ) {
        row.className = 'dynamicChosen'; 
    } 
    else {
        row.className = 'dynamic'; 
    }
    return true;
}


/**
*
*/
function selectRow(row) 
{
    if ( row == null ) {
        if ( chosenRow != null ) {
            chosenRow.className = 'dynamic';
        }
        chosenRow = null;
    } 
    else
    if ( row != chosenRow ) {
        if ( chosenRow != null ) {
            chosenRow.className = 'dynamic';
        }
        chosenRow = row;
        chosenRow.className = 'dynamicChosen';
        
        resetInternalStates();
    }      
    
    setButtonState();
    
    return true;
}


/**
*
*/
function renameRow()
{
    enableEditText(chosenRow);                     
}


/**
*
*/
function deleteRow()
{
    var ret = confirm("Click 'OK' to delete this Layer.");    
    if (ret == false) 
        return true;

    var table = document.getElementById('frameworkTable');
    var rowLength = table.rows.length;
    
    for (var i=2 ; i<rowLength ; i++) {
        var row = table.rows[i];
        if (row.id == chosenRow.id) {        
            table.deleteRow(i);
            resetInternalAttributes(); 
            break;
        }
    }

    chosenRow = null;
    setButtonState();
}


/**
*
*/
function addRow(where)
{
    chosenRow.className = 'dynamic';
    
    resetInternalStates();
            
    var index = getCurrentIndex(chosenRow);

    chosenRow = addRowToTable(index, where);

    resetInternalAttributes(); 
  
    setButtonState();
          
    return true;
}


/**
*
*/
function addRowToTable(index, where)
{
    var newIndex;
    var iteration;

    var table = document.getElementById('frameworkTable');
        
    if (where == 'after') {
        // insert after
        newIndex = parseInt(index) + 2; // plus button row and header row
        
        iteration = newIndex - 1;
    }
    else {
        // insert before
        newIndex = parseInt(index) + 1; 
        iteration = newIndex - 3;
    }

    var row = table.insertRow(newIndex);
    // first cell
    var firstCell = row.insertCell(0);
    setCellStyle(firstCell, 'center');
    var textNode = document.createTextNode(iteration);
    firstCell.appendChild(textNode);

    // second cell
    var secondCell = row.insertCell(1);
    setCellStyle(secondCell, 'left');
    
    var staticIteration = 'static_text_' + iteration;
    var staticDiv = createDiv(staticIteration, getDisplayHide());    
    secondCell.appendChild(staticDiv);  

    var inputIteration = 'input_text_' + iteration;
    var inputDiv = createDiv(inputIteration, getDisplayShow());
    secondCell.appendChild(inputDiv);  
    
    var textInputControl = createTextInputControl(iteration);
    inputDiv.appendChild(textInputControl);  
    textInputControl.focus();

    var idIteration = 'src_row_' + iteration;
    row.id = idIteration;
    row.className = 'dynamicChosen';
    row.setAttribute('permtext', 'default');    
    row.onmouseover = function() { return hilightRow(this) };    
    row.onmouseout = function() { return unhilightRow(this) };
    row.onmousedown = function() { return selectRow(this) };
    row.ondblclick = function() { return enableEditText(this) };
    
    return row;
}


/**
*
*/
function findDiv(id) 
{
    var elements=document.getElementsByTagName('div');
    for (var i=0 ; i<elements.length ; i++) { 
        var element = elements[i];
        if (element.id == id) {
            return element;
        }
    }    
    return null;
}

/**
*
*/
function copyValueFromTextToInput() 
{
    var table = document.getElementById('frameworkTable');
    var rowLength = table.rows.length;  

    for (var i=2 ; i<rowLength ; i++) {
        var row = table.rows[i];

        var secondCell = row.cells[1];

        var staticEl = null;
        var inputEl = null;
         
        for (var j=0 ; j<secondCell.childNodes.length ; j++) {
            var el = secondCell.childNodes[j];
            var tagName = el.tagName.toUpperCase();
            if (tagName == 'DIV') {
                if (el.id.indexOf('static_text_') != -1) {
                    staticEl = el;
                }
                if (el.id.indexOf('input_text_') != -1) {
                    inputEl = el.childNodes[0];                    
                }
            }                        
        }

        inputEl.value = staticEl.innerHTML;
    }
}    


/**
*
*/
function copyValueFromInputToText() 
{
    var table = document.getElementById('frameworkTable');
    var rowLength = table.rows.length;  

    for (var i=2 ; i<rowLength ; i++) {
        var row = table.rows[i];

        var secondCell = row.cells[1];

        var staticEl = null;
        var inputEl = null;
         
        for (var j=0 ; j<secondCell.childNodes.length ; j++) {
            var el = secondCell.childNodes[j];
            var tagName = el.tagName.toUpperCase();
            if (tagName == 'DIV') {
                if (el.id.indexOf('static_text_') != -1) {
                    staticEl = el;
                }
                if (el.id.indexOf('input_text_') != -1) {
                    inputEl = el.childNodes[0];                    
                }
            }                        
        }

        staticEl.innerHTML = sanitized(inputEl.value);                
    }
}    


/**
*
*/
function resetInternalAttributes() 
{
    var table = document.getElementById('frameworkTable');
    var rowLength = table.rows.length;  
    var iteration = 1;

    for (var i=2 ; i<rowLength ; i++) {
        var row = table.rows[i];

        var idIteration = 'src_row_' + iteration;
        row.id = idIteration;
        
        var firstCell = row.cells[0];
        firstCell.innerHTML = iteration;

        var secondCell = row.cells[1];

        var staticIteration = 'static_text_' + iteration;
        var inputIteration = 'input_text_' + iteration;
        var txtIteration = 'txt_index_' + iteration;

        for (var j=0 ; j<secondCell.childNodes.length ; j++) {
            var el = secondCell.childNodes[j];
            var tagName = el.tagName.toUpperCase();
            if (tagName == 'DIV') {

                if (el.id.indexOf('static_text_') != -1) {
                    el.id = staticIteration;
                }

                if (el.id.indexOf('input_text_') != -1) {
                    el.id = inputIteration;                    
                    var txtControl = el.childNodes[0];
                    txtControl.id = txtIteration;                    
                    txtControl.name = txtIteration;                    
                }
            }            
        }
        
        iteration = iteration + 1;
    }
}

/**
*
*/
function resetInternalStates() 
{
    copyValueFromInputToText();
    
    resetControls();
}

/**
*
*/
function resetControls(rowLength) 
{
    var table = document.getElementById('frameworkTable');
    var rowLength = table.rows.length - 2;  

    for (var i=1 ; i<=rowLength ; i++) {
        var inputId = "input_text_" + i;
        var inputEl = findDiv(inputId);
        inputEl.style.display = getDisplayHide();

        var staticId = "static_text_" + i;
        var staticEl = findDiv(staticId);
        staticEl.style.display = getDisplayShow();            
    }  
}

/**
*
*/
function enableEditText(row) 
{
    var id;
    var el;
    var index = getCurrentIndex(row);

    id = "static_text_" + index;
    el = document.getElementById(id);
    el.style.display = getDisplayHide();

    id = "input_text_" + index;
    el = document.getElementById(id);
    el.style.display = getDisplayShow();
            
    id = "txt_index_" + index;
    el = document.getElementById(id);        
    el.focus();    
    el.select();  
}    


/**
*
*/
function getCurrentIndex(row) 
{
  /* var srcId = row.id;
     var index = srcId.substr(srcId.length-1, srcId.length);
  */
    var srcId = row.id;
    var index;
    
    if(srcId.length == 9) {
        index = srcId.substr(srcId.length-1, srcId.length);
    } else {
        index = srcId.substr(srcId.length-2, srcId.length);
    }
     return index;
}


/**
*
*/
function getRowLength()
{
    var table = document.getElementById('frameworkTable');
    var rowLength = table.rows.length;  
    return (rowLength - 2);     // minus button row and header row
}


/**
*
*/
function setCellStyle(cell, align) 
{ 
    if (isBrowerTypeIE()) {
        cell.className = 'dynamic';
    }
    else {
        cell.style.borderColor = "#cc9";
        cell.style.borderStyle = "solid";
        cell.style.borderWidth = "1px";
        cell.style.fontSize = "12px";
    }    
    cell.setAttribute("align", align); 
}

/**
*
*/
function createTextInputControl(iteration)
{
    var el;
    
    if (isBrowerTypeIE()) {
        el = document.createElement('<input type="text" maxLength="32" >');
    }
    else {
        el = document.createElement('input');
        el.type = 'text';
        el.maxLength = 32;
    }
    var id = 'txt_index_' + iteration;
    el.id = id;
    el.name = id;
    el.alt = '0';    
    el.style.width = '450px';
    


    if (isBrowerTypeIE()) {
        el.onblur = function() { return resetInternalStates() };
        el.onkeypress = function() { return handleEnter(event) };
    }
    else {
        el.addEventListener("onblur", resetInternalStates, true); 
        el.addEventListener("keypress", handleEnterNonIE, false); 
    }
    
    return el;
}

/**
*
*/
function createDiv(id, displayStyle)
{
    var el;
    
    el = document.createElement('div');
    el.id = id;
    el.style.display = displayStyle;

    return el;
}

function showElementById( elementId ) {
    var element = document.getElementById(elementId);
    if (element != null) {
        element.style.display = getDisplayShow();
    }
}

function hideElementById( elementId ) {
    var element = document.getElementById(elementId);
    if (element != null) {
        element.style.display = getDisplayHide();
    }
}


/**
*
*/
function getDisplayHide()
{
    return "none";
}    


/**
*
*/
function getDisplayShow()
{
    if (isBrowerTypeIE())
        return "block";
    else
        return "table-row";
}


/**
*
*/
function setButtonState()
{
    var renameButton = document.getElementById('renameId');
    var deleteButton = document.getElementById('deleteId');
    var insertBeforeButton = document.getElementById('insertBeforeId');
    var insertAfterButton = document.getElementById('insertAfterId');
    
    if (chosenRow == null) {
        renameButton.disabled = "true";   
        deleteButton.disabled = "true";   
        insertBeforeButton.disabled = "true";   
        insertAfterButton.disabled = "true";   
    }
    else {
        renameButton.removeAttribute("disabled");      
        deleteButton.removeAttribute("disabled");     
        insertBeforeButton.removeAttribute("disabled");      
        insertAfterButton.removeAttribute("disabled");     
        
        var permtext = chosenRow.getAttribute("permtext");
        
        if (permtext.indexOf('notDeletable') != -1) {
            deleteButton.disabled = "true";   
        }
        if (permtext.indexOf('notBeforeInsertable') != -1) {
            insertBeforeButton.disabled = "true";   
        }
        if (permtext.indexOf('notAfterInsertable') != -1) {
            insertAfterButton.disabled = "true";   
        }
    }
    
    var table = document.getElementById('frameworkTable');
    var rowLength = table.rows.length;
    
    if (rowLength <= 3) {
        deleteButton.disabled = "true";   
    }
}


/**
*
*/
function collectData()
{
    resetInternalStates();
    
    hideAllDiv();    
    var table = document.getElementById('frameworkTable');   
    var rowLength = table.rows.length;     
    var iteration = 1;   
    var dataList = "";
    var names = new Array();
    var invalid = false;
    var invalidLength = false;

    for (var i=2 ; i<rowLength ; i++) {
        var row = table.rows[i];
        var secondCell = row.cells[1];
        for (var j=0 ; j<secondCell.childNodes.length ; j++) {
            var el = secondCell.childNodes[j];
            var tagName = el.tagName.toUpperCase();
           
            if (tagName == 'DIV') {           	
                if (el.id.indexOf('input_text_') != -1) {
                    var txtControl = el.childNodes[0];
                    var value = trim( txtControl.value );
                    
                    if (! validNameString(value)) {
                        invalid = true;
                    }
                    if(value.length > 32) {
                        invalidLength = true;
                    }
                    
                    var info = value + "_" + txtControl.alt + "_" + iteration;
                    dataList = dataList + info + "#";
                    names.push(value);
                    
                    iteration = iteration + 1;
                }
            }            
        }
    }
    
    hideElementById('errorDupNameDiv');
    if ( isContainDuplicatedName(names) ) {
        showElementById('errorDupNameDiv');
        hideElementById('messageDiv');
        return false;
    }
    
    hideElementById('errorDiv');
    if (invalid) {
        showElementById('errorDiv');
        hideElementById('messageDiv');
        return false;
    }
    
    hideElementById('errorLengthDiv');
    if (invalidLength) {
        showElementById('errorLengthDiv');
        hideElementById('messageDiv');
        return false;    
    }
    
    var actionElement = document.getElementById("actionElement");
    actionElement.value = dataList;
    
    return true;
}

/**
*
*/
function hideAllDiv()
{
    hideElementById('errorDiv');
    hideElementById('errorLengthDiv');
}


/**
*
*/
function isContainDuplicatedName(names)
{
    for (var i=0 ; i<(names.length-1) ; i++) {
        var value_i = trim(names[i]).toLowerCase();
        for (var j=(i + 1) ; j<names.length ; j++) {        
            var value_j = trim(names[j]).toLowerCase();
            if ((value_i.length > 0) && (value_j.length > 0) && (value_i == value_j)) {
                return true;
            }
        }       
    }
    return false;
}


/**
*
*/
function validNameCharacter(ch)
{
    var A_Z = ((ch >= 'A') && (ch <= 'Z'));
    var a_z = ((ch >= 'a') && (ch <= 'z'));
    var zero_nine = ((ch >= '0') && (ch <= '9'));
    var validChar = ((ch == '/') || 
                     (ch == '\'') || 
                     (ch == '-') || 
                     (ch == '\\') || 
                     (ch == '.') || 
                     (ch == '(') || 
                     (ch == ')') || 
                     (ch == '&') || 
                     (ch == '+') || 
                     (ch == ',') || 
                     (ch == ' '));
    
    return (zero_nine || A_Z || a_z || validChar);
}


/**
*
*/
function validNameString(s)
{
	var len = s.length;
    
    if (len == 0)
        return false;
        
	for (var i = 0; i < len; i++) {
		var ch = s.charAt(i);
        if (! validNameCharacter(ch))
            return false;
    }
    return true;    
}


/**
*
*/
function handleEnter( e ) 
{
    var keyId = (window.event) ? window.event.keyCode : e.which;
    if (keyId == 13) {
        resetInternalStates();
        return false;
    }
    return true;
}

/**
*
*/
function handleEnterNonIE( event ) 
{
    if (!event) var event = window.event;

    if (event.which == 13) {
        resetInternalStates();        
        if (event.preventDefault) {
            event.preventDefault();
        }        
        return false;
    }

    return true;
}


/**
*
*/
function LTrim( value ) 
{
    var re = /\s*((\S+\s*)*)/;
    return value.replace(re, "$1");
}
  	

/**
*
*/
function RTrim( value ) 
{
    var re = /((\s*\S+)*)\s*/;
    return value.replace(re, "$1");
}
    

/**
*
*/
function trim( value ) 
{
    return LTrim(RTrim(value));
}

/**
*
*/
function sanitized(value) 
{
    value = value.replace('&', '&amp;');   
    value = value.replace('<', '&lt;');   
    value = value.replace('>', '&gt;');   
    
    return value;       
}













