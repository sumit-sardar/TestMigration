var grade; 
var gender; 
var day; 
var year; 
var month; 
var firstName;
var middleName;
var lastName;
var selectedOrgNodes ;
var studentNumber;


function VerifyStudentDetail(assignedOrgNodeIds){
                       // alert($("#isMandatoryBirthDate").val());
						
						var requiredFields = "";
			            var requiredFieldCount = 0;
						var isMandatoryBirthdate = $("#isMandatoryBirthDate").val();
						//var isMandatoryStudentId = $("#isMandatoryStudentId").val();
						firstName = $("#studentRegFirstName").val();
						middleName = $("#studentRegMiddleName").val();
						lastName = $("#studentRegLastName").val();
						month = $("#monthOptions").val();
						day = $("#dayOptions").val();
						year = $("#yearOptions").val();
						gender = $("#genderOptions").val();
						grade = $("#gradeOptions").val();
						studentNumber = $("#studentRegExtPin1").val();
						selectedOrgNodes = assignedOrgNodeIds;
						/*studentNumber = $("#studentExternalId").val();
						studentSecondNumber = $("#studentExternalId2").val();
						selectedOrgNodes = assignedOrgNodeIds;
                        studentIdLabelName = $("#studentIdLabelName").val();
                        studentId2LabelName = $("#studentId2LabelName").val();
						studentIdConfigurable = $("#isStudentIdConfigurable").val();
						studentId2Configurable= $("#isStudentId2Configurable").val();
                        isStudentIdNumeric = $("#isStudentIdNumeric").val();
                        isStudentId2Numeric = $("#isStudentId2Numeric").val();
                        studentIdMinLength = $("#studentIdMinLength").val();
                        studentId2MinLength = $("#studentId2MinLength").val();*/
                        firstName = trim(firstName);
			            middleName = trim(middleName);
			            lastName =  trim(lastName);
			          	studentNumber = trim(studentNumber);
			           // studentSecondNumber = trim(studentSecondNumber); 
			            
			if ( firstName.length == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString($("#jqgFirstNameID").val(), requiredFieldCount, requiredFields);       
			}

		   if ( lastName.length == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString($("#jqgLastNameID").val(), requiredFieldCount, requiredFields);       
			}
			if(isMandatoryBirthdate == "true") {
				if (!allSelected(month, day, year)) {
					requiredFieldCount += 1;            
					requiredFields = buildErrorString($("#dateOfBirthID").val(), requiredFieldCount, requiredFields);       
				}
			}
           if ( grade == "Select a grade") {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString($("#jqgGradeID").val(), requiredFieldCount, requiredFields);       
			}

			if ( gender == "Select a gender" ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString($("#jqgGenderID").val(), requiredFieldCount, requiredFields);       
			}
					
		/*	if(isMandatoryStudentId == "true"){
				var externalStudentNumber = trim(studentNumber);
				if ( externalStudentNumber.length==0) {
					requiredFieldCount += 1;     
					requiredFields = buildErrorString(studentIdLabelName, requiredFieldCount, requiredFields);   
				}
			} */

			if ( selectedOrgNodes.length == "" ) {
				requiredFieldCount += 1;      
				requiredFields = buildErrorString($("#orgAssingID").val(), requiredFieldCount, requiredFields);       
			}        
			
			
			if (requiredFieldCount > 0) {
				if (requiredFieldCount == 1) {
					//requiredFields += ("\n" + REQUIRED_TEXT);
					setMessage($("#missRequiredFieldID").val(), requiredFields, "errorMessage", $("#reqTextID").val());
				}
				else {
					//requiredFields += ("\n" + REQUIRED_TEXT_MULTIPLE);
					var missMsg = $("#missRequiredFieldID").val()+"s";
					setMessage(missMsg, requiredFields, "errorMessage", $("#mReqTextID").val());
				}
				return false;
			}
			
			var invalidCharFields = verifyCreateStudentName(firstName, lastName, middleName);                
			if (invalidCharFields.length > 0) {
				//invalidCharFields += ("<br/>" + INVALID_NAME_CHARS);
				setMessage($("#invalidCharID").val(), invalidCharFields, "errorMessage",$("#invalidNameID").val());
				return false;
			}
			
			invalidCharFields = verifyAlphaNumericStudentNumber(studentNumber);                
			if (invalidCharFields.length > 0) {
				//invalidCharFields += ("<br/>" + INVALID_ALPHANUMBER_CHARS);
				setMessage($("#invalidCharID").val(), invalidCharFields, "errorMessage",$("#alphaNumericCharsID").val());
				return false;
			}
					
			if(isMandatoryBirthdate == "true" && !allSelected(month, day, year)) {
				if (!noneSelected(month, day, year)) {
					//invalidCharFields += INVALID_DATE;
					setMessage($("#invalidBirthDayID").val(), invalidCharFields,"errorMessage",$("#invalidDateID").val());
					return false;
					      
				}
			}
						
			if (allSelected(month, day, year)) {
				var isDateValid = validateDateValues(year, month, day);
				if (isDateValid == 0) {
					//invalidCharFields += INVALID_DATE;
					setMessage($("#invalidBirthDayID").val(), invalidCharFields, "errorMessage", $("#invalidDateID").val());
					return false;
				}
			}
			return true;							
  }
  
  function verifyAlphaNumericStudentNumber(studentNumber){
		var invalidCharFields = "";
		var invalidCharFieldCount = 0;
			if (studentNumber != "") {
				if (! validAlphaNumericString(studentNumber) ) {
					invalidCharFieldCount += 1;            
					invalidCharFields = buildErrorString(studentNumber, invalidCharFieldCount, invalidCharFields);      
				}
			}
		return invalidCharFields;
	}
	
	function validAlphaNumericString(str){
        var characters = [];
        characters = toCharArray(str);
        for (var i=0 ; i<characters.length ; i++) {
            var character = characters[i];
            if (! validAlphaNumericCharacter(character))
                return false;
        }
        return !requestHasInvalidParameters(str);
    }
    
    function validAlphaNumericCharacter(str){
        var ch = toascii(str);
        var zero_nine = ((ch >= 48) && (ch <= 57));
        var A_Z = ((ch >= 65) && (ch <= 90));
        var a_z = ((ch >= 97) && (ch <= 122));
       
        return (zero_nine || A_Z || a_z );
    }
  
  function buildErrorString(field, count, str){
        var result = str;
        if (count == 1) {
            result += field;
        }else {
            result += (", " + field);            
        }        
        return result;
    }
    
    function setMessage(title, content, type, message){
			$("#title").text(title);
			$("#content").text(content);
			$("#message").text(message);
			//$('#messageType').attr ('src', '/StudentWeb/resources/images/messaging/icon_error.gif');
			
	}
	
	function verifyCreateStudentName(firstName, lastName, middleName) {
        var invalidCharFields = "";
        var invalidCharFieldCount = 0;

        if (! validNameString(firstName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString($("#jqgFirstNameID").val(), invalidCharFieldCount, invalidCharFields);       
        }
        
        if (! validNameString(lastName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString($("#jqgLastNameID").val(), invalidCharFieldCount, invalidCharFields);       
        }

        if (! validNameString(middleName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString($("#middleNameID").val(), invalidCharFieldCount, invalidCharFields);       
        }
    
        return invalidCharFields;
    }	
    
    function validNameString(str){
        var characters = [];
        characters = toCharArray(str);
        for (var i=0 ; i<characters.length ; i++) {
            var character = characters[i];
            if (! validNameCharacter(character))
                return false;
        }
        return !requestHasInvalidParameters(str);
    }
    
    function toCharArray(str){
       var charArr=new Array();
       for(var i=0;i<str.length;i++){
            charArr[i]= str.charAt(i);
            }
            return charArr;
            
      }
       
     function validNameCharacter(str){
        var ch = toascii(str);
        var A_Z = ((ch >= 65) && (ch <= 90));
        var a_z = ((ch >= 97) && (ch <= 122));
        var zero_nine = ((ch >= 48) && (ch <= 57));
        var validChar = ((str == '/') || 
                         (str == '\'') || 
                         (str == '-') || 
                         (str == '\\') || 
                         (str == '.') || 
                         (str == '(') || 
                         (str == ')') || 
                         (str == '&') || 
                         (str == '+') || 
                         (str == ',') || 
                         (str == ' '));
        
        return (zero_nine || A_Z || a_z || validChar);
    }
    
    function requestHasInvalidParameters(str){
       
        var invalidChars = [];
        invalidChars [0] = "<script>";
		invalidChars [1] = "javascript:";
        
            for( var i=0; i<invalidChars.length; i++ )
            {
                if ( str.indexOf(invalidChars[i]) != -1 )
                {
                    return true;                
                }
            }
          
        return false;
    }
    
    function noneSelected(month, day, year){
        if (month == "" && day == "" && year == "")
            return true;
        else
            return false;    
    } 	
    
    function allSelected(month, day, year){
    	if (month == null || day == null || year == null)
    		return false;
    	else if (month == "" || day == "" || year == "")
            return false;
        else
            return true;    
    }
    
    function validateDateValues(y, m,d){
        var field = d; 
        field += ("-" + monthStringToNumber(m)); 
        field += ("-" + y);
		var checkstr = "0123456789";
		var DateField = field;
		var Datevalue = "";
		var DateTemp = "";
		var day;
		var month;
		var year;
		var leap = 0;
		var i;
	    DateValue = DateField;
	   /* Delete all chars except 0..9 */
	   for (i = 0; i < DateValue.length; i++) {
		  if (checkstr.indexOf(DateValue.substr(i,1)) >= 0) {
		     DateTemp = DateTemp + DateValue.substr(i,1);
		  }
	   }
	   DateValue = DateTemp;
	   /* Always change date to 8 digits - string*/
	   /* if year is entered as 2-digit / always assume 20xx */
	   if (DateValue.length == 6) {
	      DateValue = DateValue.substr(0,4) + '20' + DateValue.substr(4,2); }
	   if (DateValue.length != 8) {
	      return 0;}
	   /* year is wrong if year = 0000 */
	   year = DateValue.substr(4,4);
	   if (year == 0) {
	      return 0;
	   }
	   /* Validation of month*/
	   month = DateValue.substr(2,2);
	   if ((month < 1) || (month > 12)) {
	      return 0;
	   }
	   /* Validation of day*/
	   day = DateValue.substr(0,2);
	   if (day < 1) {
	     return 0;
	   }
	   /* Validation leap-year / february / day */
	   if ((year % 4 == 0) || (year % 100 == 0) || (year % 400 == 0)) {
	      leap = 1;
	   }
	   if ((month == 2) && (leap == 1) && (day > 29)) {
	      return 0;
	   }
	   if ((month == 2) && (leap != 1) && (day > 28)) {
	      return 0;
	   }
	   /* Validation of other months */
	   if ((day > 31) && ((month == "01") || (month == "03") || (month == "05") || (month == "07") || (month == "08") || (month == "10") || (month == "12"))) {
	      return 0;
	   }
	   if ((day > 30) && ((month == "04") || (month == "06") || (month == "09") || (month == "11"))) {
	      return 0;
	   }
	    else {
	     
	    return 1;
	   }
	}
	
	function monthStringToNumber(month){
        if (month == "Jan") return "01";
        if (month == "Feb") return "02";
        if (month == "Mar") return "03";
        if (month == "Apr") return "04";
        if (month == "May") return "05";
        if (month == "Jun") return "06";
        if (month == "Jul") return "07";
        if (month == "Aug") return "08";
        if (month == "Sep") return "09";
        if (month == "Oct") return "10";
        if (month == "Nov") return "11";
        if (month == "Dec") return "12";
        return "";        
    }
    
    function toascii (c){
	c = c . charAt (0);
	var i;
	for (i = 0; i < 256; ++ i)
	{
		var h = i . toString (16);
		if (h . length == 1)
			h = "0" + h;
		h = "%" + h;
		h = unescape (h);

		if (h == c)
			break;
	}
	return i;
	}
	
	function setMessageMain(title, content, type, message){
			$("#contentMain").text(content);
	}
