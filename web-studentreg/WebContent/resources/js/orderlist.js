/**
 * orderlist.js
 */


/** Order List *******************************************************/

	var chosenRow = null;
	
	function hilightRow(row) 
    {
		if( row != chosenRow ) {
            row.className = 'dynamicHilight'; 
		}
        return true;
	}
	
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

	function selectRow(row) 
    {
		if( row == null ) {
            if( chosenRow != null ) {
                chosenRow.className = 'dynamic';
            }
			chosenRow = null;
		} 
        else {
			if( chosenRow != null ) {
				chosenRow.className = 'dynamic';
			}
			chosenRow = row;
			chosenRow.className = 'dynamicChosen';
            
            var id = row.id;
            if (id.indexOf("src_row_") == 0) {
                enableSourceTableButtons();
                disableDestinationTableButtons();
            }
            else {
                enableDestinationTableButtons();
                disableSourceTableButtons();
            }
		}        
        return true;
	}
	
	function dragRow(event, row) 
    {
		var targetElement = event.srcElement;
        
		if( row == null ) {
            if( chosenRow != null ) {
                chosenRow.className = 'dynamic';
            }
			chosenRow = null;
		} 
        else {
			if( chosenRow != null ) {
				chosenRow.className = 'dynamic';
			}
			chosenRow = row;
			chosenRow.className = 'dynamicChosen';
            
            if (targetElement.tagName == 'TD') {
                chosenRow.parentNode.style.cursor = 'move';
            }
		}        
        return true;
	}
	
	function cancelDrag() 
    {
        if (chosenRow != null) {
            chosenRow.parentNode.style.cursor = 'default';
        }
        return true;
	}

	function moveRow(row, delta) 
    {
		if ( row == null || delta == 0 ) 
			return true;

		var table = row.parentNode;		
		var targetIndex = row.rowIndex + delta;
       	
		if ( delta > 0 ) 
			targetIndex = targetIndex + 1;
		
		if ( targetIndex < 0 )
			targetIndex = 0;
                
		if ( targetIndex >= table.rows.length ) 
			table.appendChild(row);
		else 
			table.insertBefore(row, table.rows[targetIndex]);
    	
        var id = row.id;
        if (id.indexOf("des_row_") == 0) {        
            setVisibleFlag(row); 
        }
        
        selectRow(row);
                
        return true;
	}
	
	function delegateEvent(event, listener) 
    {
		var targetElement;

		
		if( !event ) {
			event = window.event;
		}
		if( event.target ) {
			targetElement = event.target;
		} else if( event.srcElement ) {
			targetElement = event.srcElement;
		}
		if( targetElement.nodeType == 3 ) {
			targetElement = targetElement.parentNode;
		}

		switch(event.type)
		{
			case 'mouseup':
				if( chosenRow != null ) {
                    if (targetElement.parentNode.rowIndex != null) {
                        if (targetElement.tagName == 'TD') {                    
                            var delta = targetElement.parentNode.rowIndex - chosenRow.rowIndex;
                            moveRow(chosenRow, delta);
                            chosenRow.parentNode.style.cursor = 'default';
                        }
                        else return false;
                    }
                    else return false;
				}
				unhilightRow(targetElement.parentNode);
				break;
			case 'mouseout':
				if( targetElement.tagName == 'div' ) {
				}
				break;
			default:
				break;
		}
        
        return true;
	}
  

    function enableDestinationTableButtons() 
    {
        var removeRow = document.getElementById("removeRow");
        removeRow.removeAttribute("disabled");      

        var removeAllRows = document.getElementById("removeAllRows");
        removeAllRows.removeAttribute("disabled");      

        var moveUp = document.getElementById("moveUp");
        moveUp.removeAttribute("disabled");  
            
        var moveDown = document.getElementById("moveDown");
        moveDown.removeAttribute("disabled");   
        
        var visibleRows = getVisibleRows("des_row_");
        
        if (visibleRows <= 1) {
            moveUp.setAttribute("disabled", "true");   
            moveDown.setAttribute("disabled", "true");   
        } 
        
        if (chosenRow != null) {        
            if (isFirstVisibleRow(chosenRow)) { 
                moveUp.setAttribute("disabled", "true");  
            }
            if (isLastVisibleRow(chosenRow)) { 
                moveDown.setAttribute("disabled", "true");   
            }
        }
    }    

    function disableDestinationTableButtons() 
    {
        var removeRow = document.getElementById("removeRow");
        removeRow.setAttribute("disabled", "true");   
        
        var removeAllRows = document.getElementById("removeAllRows");
        removeAllRows.setAttribute("disabled", "true");   
        
        var visibleRows = getVisibleRows("des_row_");
        if (visibleRows > 0) {
            removeAllRows.removeAttribute("disabled");      
        }
        
        var moveUp = document.getElementById("moveUp");
        moveUp.setAttribute("disabled", "true");   

        var moveDown = document.getElementById("moveDown");        
        moveDown.setAttribute("disabled", "true");   
    }    

    function enableSourceTableButtons() 
    {
        var addRow = document.getElementById("addRow");
        addRow.removeAttribute("disabled");      

        var addAllRows = document.getElementById("addAllRows");
        addAllRows.removeAttribute("disabled");      
    }    

    function disableSourceTableButtons() 
    {
        var addRow = document.getElementById("addRow");
        addRow.setAttribute("disabled", "true");   

        var addAllRows = document.getElementById("addAllRows");
        addAllRows.setAttribute("disabled", "true");      
             
        var visibleRows = getVisibleRows("src_row_");
        if (visibleRows > 0) {
            addAllRows.removeAttribute("disabled");      
        }        
    }    

    function unSelectItem() 
    {
		if ( chosenRow != null ) {
			chosenRow.className = 'dynamic';
		}
		chosenRow = null;
    
    	disableDestinationTableButtons();

    	disableSourceTableButtons(); 
	}
    
    function addSelectedRow(row) 
    {
		if( row == null )
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
		}
        else {
			table.appendChild(destRow);
		}
        
        selectRow(destRow); 

        setVisibleFlag(destRow);
                
        return true; 
    }    

    function addAllSelectedRows() 
    {
        var numberOfRows = document.getElementById("numberOfRows").value;      
        
        for (var i=1 ; i<=numberOfRows ; i++) {
            var srcId = "src_row_" + i;
            var srcRow = document.getElementById(srcId); 
            if ( isRowVisible(srcRow) ) {
                addSelectedRow(srcRow);
            }
        }

		unSelectItem();
    
        return true;   
    }    

    function removeSelectedRow(row) 
    {
		if( row == null )
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
        
        selectRow(srcRow); 

        setVisibleFlag(destRow);
        
        return true; 
    }    

    function removeAllSelectedRows() 
    {
        var numberOfRows = document.getElementById("numberOfRows").value;      
        
        for (var i=1 ; i<=numberOfRows ; i++) {
            var desId = "des_row_" + i;
            var desRow = document.getElementById(desId);      
            if ( isRowVisible(desRow) ) {
                removeSelectedRow(desRow);
            }
        }

		unSelectItem();
    
        return true;   
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

    function getVisibleRows(rowPrefix) 
    {
        var numberOfRows = document.getElementById("numberOfRows").value;      
        var visibleRows = 0;
        
        for (var i=1 ; i<=numberOfRows ; i++) {
            var id = rowPrefix + i;
            var row = document.getElementById(id); 
            if ( isRowVisible(row) ) {
                visibleRows = visibleRows + 1;
            }
        }    
        return visibleRows;   
    }    

    function isFirstVisibleRow(desRow) 
    {
		var table = desRow.parentNode;		
		if (table.rows.length > 0) {
            var row = table.rows[0];
			if (row.id == desRow.id) 
				return true;
		}
		return false;
    }

    function isLastVisibleRow(desRow) 
    {    
		var table = desRow.parentNode;		
		for (var i=table.rows.length-1 ; i>=0 ; i--) {
            var row = table.rows[i];
			if (isRowVisible(row)) {           
				if (row.id == desRow.id) 
					return true;
				else 
					return false;
			}
		}
		return false;
    }

    function isRowVisible(row)
    {
        if (row.style.display != "none") 
            return true;
        else
            return false;
    }    

    function isRowHidden(row)
    {
        if (row.style.display == "none") 
            return true;
        else
            return false;
    }    

    function getDisplayHide()
    {
        return "none";
    }    
    
    function getDisplayShow()
    {
        if (isBrowerTypeIE())
            return "block";
        else
            return "table-row";
    }

    function setVisibleFlag(destRow) 
    {
 		var table = destRow.parentNode;
   
		for(var i=0; i < table.rows.length; i++) {
            var row = table.rows[i];
			var column = row.cells[0];
            var value = 0;
            if (isRowVisible(row)) {
                value = i + 1;
            }
			column.innerHTML = column.innerHTML.replace(/value=.*? /, 'value="' + value + '"');
		}
    }    
 
    function getFirstInvisibleRowIndex(table) 
    {
		var index = table.rows.length-1; 
		for (var i=0 ; i<table.rows.length ; i++) {
            var row = table.rows[i];
			if (isRowHidden(row)) {           
				index = i;
				break;
			}
		}
		return index;
    }    

