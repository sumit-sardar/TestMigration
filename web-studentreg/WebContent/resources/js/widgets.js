/**
 * widgets.js
 */
function doHourglass()
{
    document.body.style.cursor = 'wait';
}
 
 
function showElementById( elementId ) {
    document.getElementById(elementId).style.display = "block";
}

function hideElementById( elementId ) {
    document.getElementById(elementId).style.display = "none";
}

function showSection( sectionId, sectionVisible ) {
    var showId = sectionId + "_show";
    var hideId = sectionId + "_hide";
    
    showElementById(showId);
    hideElementById(hideId);    
    setElementValue(sectionVisible, true);    
}


function hideSection( sectionId, sectionVisible ) {
    var showId = sectionId + "_show";
    var hideId = sectionId + "_hide";
    
    showElementById(hideId);
    hideElementById(showId);    
    setElementValue(sectionVisible, false);    
}


function setElementValue(elementId, value) {
    setElementValue( elementId, value, false );
}

function setElementValue(elementId, value, submitForm) {
    var element = document.getElementById(elementId);

    if( element ) {
        element.value = value;

        var actionElement = document.getElementById('actionElement');
        if( actionElement ) {
            actionElement.value = elementId;
        }

        if( submitForm ) {
            document.body.style.cursor = 'wait';
            element.form.submit();
        }

    } else {
        alert("Could not set value for form element '" + elementId + "'!");
    }
}

function setElementValueAndSubmit(elementId, value) {
    setElementValue( elementId, value, true );
}

function setElementValueAndSubmitWithAnchor(elementId, value, anchorName) {
    var element = document.getElementById(elementId);

    if( element ) {
        element.value = value;

        var actionElement = document.getElementById('actionElement');
        if( actionElement ) {
            actionElement.value = elementId;
        }

        if ((anchorName != null) && (anchorName != "null")) {            
            var index = element.form.action.indexOf("#");
            if (index == -1) {                
                element.form.action += ("#" + anchorName); 
            }
        }
        
        document.body.style.cursor = 'wait';
        element.form.submit();

    } else {
        alert("Could not set value for form element '" + elementId + "'!");
    }
}
 


function enableElementById(elementId) {
    var element = document.getElementById(elementId);
    
    if( element != null) {
        element.removeAttribute("disabled");      
    }
}    


function constrainNumericKeyEvent( e, element, id, anchorName ) {
    var keyId = (window.event) ? event.keyCode : e.which;
    var keyValue = String.fromCharCode( keyId );
    var regExp = /\d/;
    var results = false;
    
    if ( keyId == 13 ) {
        var actionElement = document.getElementById('actionElement');
        if( actionElement ) {
            if ( id != null )
                actionElement.value = "EnterKeyInvoked_" + id;
            else
                actionElement.value = "EnterKeyInvoked";
            
            if ((anchorName != null) && (anchorName != "null")) {            
                var index = actionElement.form.action.indexOf("#");
                if (index == -1) {                
                    actionElement.form.action += ("#" + anchorName);
                }
            }
            
            actionElement.form.submit();
        }
        return results;
    }
     
    if( keyId == null || keyId == 0 || keyId == 8 || keyId == 9 || keyId == 13 || keyId == 27 ) {
        // allow control characters
        if( element.value.length == 0 )
            element.value = 1;
        results = true;
    } else if( regExp.test(keyValue) ) {
        results = true;    
    }
    
    return results;
}

function buttonGoInvoked( id, anchorName ) {
    var actionElement = document.getElementById('actionElement');
    if( actionElement ) {
        if (id != null)
            actionElement.value = "ButtonGoInvoked_" + id;
        else
            actionElement.value = "ButtonGoInvoked";
            
        if ((anchorName != null) && (anchorName != "null")) {            
            var index = actionElement.form.action.indexOf("#");
            if (index == -1) {                
                actionElement.form.action += ("#" + anchorName);
            }
        }            
    }
    return true;
}
 
function addAnchor( action, anchorName ) {
    var index = action.indexOf("#");
    if (index == -1) {                
        action += ("#" + anchorName);
    }
    return action;
}

function constrainEnterKeyEvent( e ) {
    var keyId = (window.event) ? event.keyCode : e.which;
    var keyValue = String.fromCharCode( keyId );
    
    if( keyId == 13 )
        return false;
    else
        return true;
}





/** General Aux Windows *******************************************************/

var termsWindow  = null;
var coppaWindow  = null;
var policyWindow = null;
var helpWindow = null;

function showHelpWindow(location_)
{
    if( helpWindow != null ){
        helpWindow.close();
    }
    helpWindow = window.open(location_,"help",'toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=560, height=430');
    helpWindow.focus();
    return false;
}

function showTermsOfUseWindow( url )
{
    if( !termsWindow || termsWindow.closed ) {
        termsWindow = window.open( url , 'TermsOfUse', 'toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=560, height=430');
    } else {
        termsWindow.location = url;
    }
    termsWindow.focus();
    return false;
}

function showCOPPAWindow( url )
{
    if( !coppaWindow || coppaWindow.closed ) {
        coppaWindow = window.open( url , 'COPPA', 'toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=560, height=430');
    } else {
        coppaWindow.location = url;
    }
    coppaWindow.focus();
    return false;
}

function showPrivacyPolicyWindow( url )
{
    if( !policyWindow || policyWindow.closed ) {
        policyWindow = window.open( url , 'PrivacyPolicy', 'toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=560, height=430');
    } else {
        policyWindow.location = url;
    }
    policyWindow.focus();
    return false;
}




/** ToolTip *******************************************************************/

var toolTip = null;

function showToolTip( event, text ) {
    
    if( toolTip == null )
        toolTip = new ToolTip();
        
    toolTip.show( event, text );    
}


function hideToolTip( event ) {
    
    if( toolTip == null )
        toolTip = new ToolTip();
        
    toolTip.hide( event );
}



function ToolTip() {
    // add the object to the DOM
    document.body.innerHTML += "<div id='toolTip'>tooltip</div>";
    
    // member variables
    this.element = document.getElementById("toolTip");
    this.offsetX = 16;
    this.offsetY = 16;
    this.timer = null;
    this.timerDelay = 700;
        
    // methods
    this.show = ToolTipShow;
    this.hide = ToolTipHide;
}

function ToolTipShow( event, text ) {
    // IE has window.event, others don't.
    var mouseX = (window.event) ? window.event.clientX + document.body.scrollLeft : event.pageX;
    var mouseY = (window.event) ? window.event.clientY + document.body.scrollTop : event.pageY;
    
    if( text == null ) {
        var html = "";
        html += "x = " + mouseX + "<br/>";
        html += "y = " + mouseY + "<br/>";
        html += "element width = " + this.element.offsetWidth + "<br/>";
        html += "element height = " + this.element.offsetHeight + "<br/>";
        html += "document width = " + document.body.clientWidth+ "<br/>";
        html += "document height = " + document.body.clientHeight+ "<br/>";
        this.element.innerHTML = html;
    } else {
        this.element.innerHTML = text;
    }
    
    this.element.style.top  = mouseY + this.offsetY;

    if( mouseX + this.offsetX + this.element.offsetWidth > document.body.clientWidth ) {
        this.element.style.left = document.body.clientWidth - (2*this.offsetX + this.element.offsetWidth);
    } else {
        this.element.style.left = mouseX + this.offsetX;
    }
    
    
    
    this.timer = setTimeout( "toolTip.element.style.visibility = 'visible';", this.timerDelay );
}

function ToolTipHide( event ) {
    clearTimeout( this.timer );
    this.element.style.visibility = "hidden";
}



/** Calendar ******************************************************************/

var calendar = null;

function showCalendar(formElementChosen, formElementNoEarlier) {

    if( calendar == null )
        calendar = new Calendar("testSession");
    
    calendar.init(formElementChosen, formElementNoEarlier);
    calendar.show();
}

function showStudentCalendar(formElementChosen) {

    if( calendar == null )
        calendar = new Calendar("student");
    
    calendar.init(formElementChosen, null);
    calendar.show();
}

function showUserCalendar(formElementChosen) {

    if( calendar == null )
        calendar = new Calendar("user");
    
    calendar.init(formElementChosen, null);
    calendar.show();
}

function Calendar(calendarType) {
    // member variables
    this.type = calendarType;
    this.title = "Calendar";
    this.date = new Date();
    this.noEarlierDate = new Date();
    this.window = null;
    this.formElement = null;
    this.monthNames = new Array('January', 'February', 'March', 'April', 'May', 'June',
                                'July', 'August', 'September', 'October', 'November', 'December');
    this.monthDays  = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    
    // methods
    this.init = CalendarInit;
    
    if (this.type == "testSession") {
        this.parseDate = TestSessionCalendarParseDate;
        this.choseDate = TestSessionCalendarChoseDate;
    }
    else {
        this.parseDate = CalendarParseDate;
        this.choseDate = CalendarChoseDate;
    }
    
    this.show = CalendarShow;
    this.focus = CalendarFocus;
    this.draw = CalendarDraw;
    this.endOfTheMonth = CalendarEndOfTheMonth;
    
}



function CalendarInit(formElementChosen, formElementNoEarlier) {
    this.formElement = formElementChosen;
    this.date = new Date(); // default today.
    this.noEarlierDate = new Date(); // default today.

    if( formElementChosen != null && formElementChosen.value != null && formElementChosen.value.length > 0 && !isNaN(Date.parse(formElementChosen.value)) ) {
        this.date = this.parseDate(formElementChosen.value);
    }

    if( formElementNoEarlier != null && formElementNoEarlier.value != null && formElementNoEarlier.value.length > 0 && !isNaN(Date.parse(formElementNoEarlier.value)) ) {
        this.noEarlierDate = this.parseDate(formElementNoEarlier.value);
    }
    
}

function TestSessionCalendarParseDate(strDate) {
    var re = /(\d\d)\/(\d\d)\/(\d\d)/;
    var tokens = re.exec( strDate );
    var parsedDate = new Date();

    if( tokens != null ) {
        parsedDate.setYear( parseInt(tokens[3],10) + 2000);
        parsedDate.setDate( tokens[2] );
        parsedDate.setMonth( tokens[1] - 1 );
        
    }
    return parsedDate;
}

function CalendarParseDate(strDate) {
    var re = /(\d\d)\/(\d\d)\/(\d\d\d\d)/;
    var tokens = re.exec( strDate );
    var parsedDate = new Date();
    
    if( tokens != null ) {
        parsedDate.setYear( parseInt(tokens[3],10) );
        parsedDate.setDate( tokens[2] );
        parsedDate.setMonth( tokens[1] - 1 );
        
    }
    return parsedDate;
}

function CalendarShow() {
    var width  = 300;
    var height = 300;
    var x = (window.screen.width - width)/ 2 ; 
    var y = (window.screen.height - height)/ 2;
    var windowOptions = "";
    
    windowOptions += "menubar=0,toolbar=0,status=1,resizable=0,scrollbars=0,width=300,height=300";
    windowOptions += ",left=" + x;
    windowOptions += ",top="  + y;

    if( this.window == null || this.window.closed ) {
        this.window = window.open("about:blank", "calendar", windowOptions);
    }
    this.draw();
    this.focus();
}

function CalendarFocus() {
    if( this.window != null ) {
        this.window.focus();
    } else {
        alert("CalendarFocus(): Calendar window is not defined!");
    }
}

function TestSessionCalendarChoseDate(month, day, year) {
    if( this.window != null ) {
        var output;
        if( month < 10 )
            output = "0" + month + "/";
        else
            output = month + "/";

        if( day < 10 )
            output += "0" + day + "/";
        else
            output += day + "/";

        if( year > 100 )
            year = year % 100;
        if( year < 10 )
            output += "0" + year;
        else
            output += year;
        
        if( this.formElement )
            this.formElement.value = output;
        this.window.close();
    }
}

function CalendarChoseDate(month, day, year) {
    if( this.window != null ) {
        var output;
        if( month < 10 )
            output = "0" + month + "/";
        else
            output = month + "/";

        if( day < 10 )
            output += "0" + day + "/";
        else
            output += day + "/";

        output += year;
        
        if( this.formElement )
            this.formElement.value = output;
        this.window.close();
    }
}

function CalendarEndOfTheMonth(monthIndex, year) {
    var daysInMonth = this.monthDays[monthIndex];
    
    if( monthIndex == 1 ) {
        var rollover = new Date(year, 1, 29);
        if( rollover.getDate() == 29 )
            daysInMonth++;
    }
    
    return daysInMonth;
}


function CalendarDraw(monthIndex, year) {
    if( this.window != null ) {
        var viewedDate = new Date();
        
        if( monthIndex == null || year == null ) {
            viewedDate.setYear(this.date.getFullYear());
            viewedDate.setDate(1);
            viewedDate.setMonth(this.date.getMonth());
        } else {
            viewedDate.setYear(year);
            viewedDate.setDate(1);
            viewedDate.setMonth(monthIndex);
        }
        
        var today = new Date();
        
        var nextMonth = new Date();
        nextMonth.setYear( viewedDate.getFullYear() );
        nextMonth.setDate( 1 );
        nextMonth.setMonth( viewedDate.getMonth() + 1);

        var prevMonth = new Date();
        prevMonth.setYear( viewedDate.getFullYear() );
        prevMonth.setDate( 1 );
        prevMonth.setMonth( viewedDate.getMonth() - 1);
        
        var cells = new Array();
        var cellStyle;
        var dayNum = 0;
        for(var week=0; week < 6; week++ ) {
            cells[week] = new Array();
            for(var day=0; day < 7; day++ ) {
                
                if( (week == 0 && day == viewedDate.getDay()) || dayNum > 0 ) {
                    dayNum++;
                }

                if( viewedDate.getFullYear() == this.date.getFullYear() && viewedDate.getMonth() == this.date.getMonth() && dayNum == this.date.getDate() ) {
                    cellStyle = "chosen";
                } else if( day == 0 || day == 6 ) {
                    cellStyle = "weekend";
                } else {
                    cellStyle = "default";
                }

                if( dayNum > 0 && dayNum <= this.endOfTheMonth(viewedDate.getMonth(), viewedDate.getFullYear()) ) {
                    cells[week][day] = "<td class='" + cellStyle + "'>";
                    
                    var cellDate = new Date();
                    cellDate.setYear( viewedDate.getFullYear() );
                    cellDate.setMonth( viewedDate.getMonth() );
                    cellDate.setDate( dayNum );
                    
                    
                    if ( (this.type == "student") || (this.type == "user")
                         ( cellDate >= this.noEarlierDate && cellDate >= today ) ) 
                        cells[week][day] += "<a href='#' onclick='choseDate(" + (viewedDate.getMonth()+1)  + ", " + dayNum + ", " + viewedDate.getFullYear() + "); return false;'>" + dayNum + "</a>";
                    else 
                        cells[week][day] += dayNum;
                        
                    cells[week][day] += "</td>";
                    
                } 
                else {
                    cells[week][day] = "<td class='" + cellStyle + "'>&nbsp;</td>";
                }
            }
        }
        
        this.window.document.open();
        this.window.document.writeln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        this.window.document.writeln("<html>");
        this.window.document.writeln("<head>");
        this.window.document.writeln("  <title>" + this.title + "</title>");
        this.window.document.writeln("  <style type='text/css'>");
        this.window.document.writeln("    body {");
        this.window.document.writeln("      background-color: #fff; ");
        this.window.document.writeln("      color: #000; ");
        this.window.document.writeln("      font-family: arial, helvetica, sans serif; ");
        this.window.document.writeln("      font-weight: normal; ");
        this.window.document.writeln("      font-size: 12px; ");
        this.window.document.writeln("      font-style: normal; ");
        this.window.document.writeln("      text-align: center; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .calendar table {");
        this.window.document.writeln("      border-collapse: collapse;");
        this.window.document.writeln("      border-color: #996; ");
        this.window.document.writeln("      border-style: solid; ");
        this.window.document.writeln("      border-width: 1px; ");
        this.window.document.writeln("      width: 100%; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .calendar tr {");
        this.window.document.writeln("       ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .calendar th {");
        this.window.document.writeln("      background-color: #ffc; ");
        this.window.document.writeln("      border-color: #996; ");
        this.window.document.writeln("      border-style: solid; ");
        this.window.document.writeln("      border-width: 1px; ");
        this.window.document.writeln("      font-family: arial, helvetica, sans serif; ");
        this.window.document.writeln("      font-weight: normal; ");
        this.window.document.writeln("      font-size: 11px; ");
        this.window.document.writeln("      font-style: normal; ");
        this.window.document.writeln("      padding: 5px; ");
        this.window.document.writeln("      width: 14%; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .calendar td {");
        this.window.document.writeln("      border-color: #996; ");
        this.window.document.writeln("      border-style: solid; ");
        this.window.document.writeln("      border-width: 1px; ");
        this.window.document.writeln("      color: #999; ");
        this.window.document.writeln("      font-family: arial, helvetica, sans serif; ");
        this.window.document.writeln("      font-weight: normal; ");
        this.window.document.writeln("      font-size: 11px; ");
        this.window.document.writeln("      font-style: normal; ");
        this.window.document.writeln("      padding: 5px; ");
        this.window.document.writeln("      text-align: right;");
        this.window.document.writeln("      width: 14%; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .calendar td.default {");
        this.window.document.writeln("      background-color: #fff; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .calendar td.weekend {");
        this.window.document.writeln("      background-color: #eee; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .calendar td.chosen {");
        this.window.document.writeln("      background-color: #fcc; ");
        this.window.document.writeln("      border-color: #663; ");
        this.window.document.writeln("      border-style: solid; ");
        this.window.document.writeln("      border-width: 1px; ");
        this.window.document.writeln("      color: #966; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .calendar td a, .control td a:hover {");
        this.window.document.writeln("      color: #00f; ");
        this.window.document.writeln("      font-family: arial, helvetica, sans serif; ");
        this.window.document.writeln("      font-weight: normal; ");
        this.window.document.writeln("      font-size: 11px; ");
        this.window.document.writeln("      font-style: normal; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .control table {");
        this.window.document.writeln("      border-collapse: collapse;");
        this.window.document.writeln("      border-style: none; ");
        this.window.document.writeln("      border-width: 0px; ");
        this.window.document.writeln("      width: 100%; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .control tr {");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .control td.leftControl {");
        this.window.document.writeln("      text-align: left; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .control td.rightControl {");
        this.window.document.writeln("      text-align: right; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("    .control td a, .control td a:hover {");
        this.window.document.writeln("      color: #00f; ");
        this.window.document.writeln("      font-family: arial, helvetica, sans serif; ");
        this.window.document.writeln("      font-weight: normal; ");
        this.window.document.writeln("      font-size: 11px; ");
        this.window.document.writeln("      font-style: normal; ");
        this.window.document.writeln("    }");
        this.window.document.writeln("  </style>");
        this.window.document.writeln("<script type='text/javascript'>");
        this.window.document.writeln("function viewNextMonth() {");
        this.window.document.writeln("  if( window.opener && window.opener.calendar ) { ");
        this.window.document.writeln("    window.opener.calendar.draw(" + nextMonth.getMonth() + "," + nextMonth.getFullYear() + ");");
        this.window.document.writeln("  } else { ");
        this.window.document.writeln("    self.close();");
        this.window.document.writeln("  }");
        this.window.document.writeln("}");
        this.window.document.writeln("function viewPrevMonth() {");
        this.window.document.writeln("  if( window.opener && window.opener.calendar ) { ");
        this.window.document.writeln("    window.opener.calendar.draw(" + prevMonth.getMonth() + "," + prevMonth.getFullYear() + ");");
        this.window.document.writeln("  } else { ");
        this.window.document.writeln("    self.close();");
        this.window.document.writeln("  }");
        this.window.document.writeln("}");
        this.window.document.writeln("function choseDate(month, day, year) {");
        this.window.document.writeln("  if( window.opener && window.opener.calendar ) { ");
        this.window.document.writeln("    window.opener.calendar.choseDate(month, day, year);");
        this.window.document.writeln("  } else { ");
        this.window.document.writeln("    self.close();");
        this.window.document.writeln("  }");
        this.window.document.writeln("}");
        this.window.document.writeln("function watchKeys( e ) { ");
        this.window.document.writeln("  e = (window.event) ? window.event : e; ");
        this.window.document.writeln("  if( e.keyCode == 8 || e.keyCode == 116 || e.ctrlKey || e.altKey ) {");
        this.window.document.writeln("     cancelKeyEvent(e); ");
        this.window.document.writeln("  } ");
        this.window.document.writeln("} ");
        this.window.document.writeln("function cancelKeyEvent( e ) { ");
        this.window.document.writeln("  if( e.preventDefault ) { ");
        this.window.document.writeln("    e.preventDefault(); ");
        this.window.document.writeln("    return false; ");
        this.window.document.writeln("  } else { ");
        this.window.document.writeln("    e.keyCode = 0; ");
        this.window.document.writeln("    e.returnValue = false; ");
        this.window.document.writeln("  } ");
        this.window.document.writeln("} ");
        this.window.document.writeln("</script>");
        this.window.document.writeln("</head>");
        this.window.document.writeln("<body onkeydown='watchKeys(event);' onkeypress='watchKeys(event);' onblur='self.focus();'>");
        this.window.document.writeln("  " + this.monthNames[viewedDate.getMonth()]);
        this.window.document.writeln("  " + viewedDate.getFullYear());
        this.window.document.writeln("<div class='calendar'>");
        this.window.document.writeln("  <table>");
        this.window.document.writeln("  <tr>");
        this.window.document.writeln("    <th>Sun</th>");
        this.window.document.writeln("    <th>Mon</th>");
        this.window.document.writeln("    <th>Tue</th>");
        this.window.document.writeln("    <th>Wed</th>");
        this.window.document.writeln("    <th>Thu</th>");
        this.window.document.writeln("    <th>Fri</th>");
        this.window.document.writeln("    <th>Sat</th>");
        this.window.document.writeln("  </tr>");
        for(var week=0; week < cells.length; week++ ) {
            this.window.document.writeln("  <tr>");
            for(var day=0; day < cells[week].length; day++ ) {
                this.window.document.writeln(cells[week][day]);
            }
            this.window.document.writeln("  </tr>");
        }
        this.window.document.writeln("  </table>");
        this.window.document.writeln("</div>");
        this.window.document.writeln("<div class='control'>");
        this.window.document.writeln("  <table>");
        this.window.document.writeln("  <tr>");
        this.window.document.writeln("    <td class='leftControl'>");
        this.window.document.writeln("      <a href='#' onclick='viewPrevMonth(); return false;'>&lt;&lt; previous month</a>");
        this.window.document.writeln("    </td>");
        this.window.document.writeln("    <td class='rightControl'>");
        this.window.document.writeln("      <a href='#' onclick='viewNextMonth(); return false;'>next month &gt;&gt;</a>");
        this.window.document.writeln("    </td>");
        this.window.document.writeln("  </tr>");
        this.window.document.writeln("  </table>");
        this.window.document.writeln("</div>");
        this.window.document.writeln("</body>");
        this.window.document.writeln("</html>");
        this.window.document.close();

    } else {
        alert("CalendarDraw(): Calendar window is not defined!");
    }
}
    
function moveSelectedOption(elementId, moveDirection) 
{
    var selbox = document.getElementById(elementId);    
    if (selbox == null)
        return false;
        
    var selectedIndex = selbox.options.selectedIndex;
    if (selectedIndex < 0)
        return false;

    var length = selbox.options.length;
    var values = new Array();
    var ids = new Array();

    for (var i=0 ; i<length ; i++) {
        values[i] = selbox.options[i].value;
        ids[i] = selbox.options[i].id;
    }

    if (moveDirection == "moveUp") {         
        if (selectedIndex == 0)
            return false;
    
        selbox.options.length = 0;
        var index = 0;
        for (var i=0 ; i<selectedIndex-1 ; i++) {
            selbox.options[index] = new Option(ids[i], values[i]);  
            selbox.options[index].id = ids[i];      
            index++;
        }
    
        selbox.options[index] = new Option(ids[selectedIndex], values[selectedIndex]);  
        selbox.options[index].id = ids[selectedIndex];      
        index++;
    
        selbox.options[index] = new Option(ids[selectedIndex-1], values[selectedIndex-1]);  
        selbox.options[index].id = ids[selectedIndex-1];      
        index++;
    
        for (var i=selectedIndex+1 ; i<length ; i++) {
            selbox.options[index] = new Option(ids[i], values[i]);  
            selbox.options[index].id = ids[i];      
            index++;
        }
    
        selbox.options.selectedIndex = selectedIndex - 1;
    }
    else {
        if (selectedIndex == length-1)
            return false;
    
        selbox.options.length = 0;
        var index = 0;
        for (var i=0 ; i<selectedIndex ; i++) {
            selbox.options[index] = new Option(ids[i], values[i]);  
            selbox.options[index].id = ids[i];      
            index++;
        }
        
        selbox.options[index] = new Option(ids[selectedIndex+1], values[selectedIndex+1]);  
        selbox.options[index].id = ids[selectedIndex+1];      
        index++;

        selbox.options[index] = new Option(ids[selectedIndex], values[selectedIndex]);  
        selbox.options[index].id = ids[selectedIndex];      
        index++;

        for (var i=selectedIndex+2 ; i<length ; i++) {
            selbox.options[index] = new Option(ids[i], values[i]);  
            selbox.options[index].id = ids[i];      
            index++;
        }
    
        selbox.options.selectedIndex = selectedIndex + 1;
    }
        
    return false;
}    


