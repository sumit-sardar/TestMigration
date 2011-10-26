var INVALID_NUMBER_CHARS = "Please re-enter your string with these characters: A-Z, a-z, 0-9, space";
var INVALID_NAME_CHARS = "Please re-enter your string with these characters: A-Z, a-z, 0-9, /, \\, -, ', (, ), &, +, comma, period, space";
var REQUIRED_TEXT = "Please enter/select this value to continue.";
var REQUIRED_TEXT_MULTIPLE = "Please enter/select these values to continue.";
var INVALID_ALPHANUMBER_CHARS = "Please enter ID of alpha-numeric characters, with no spaces. Acceptable characters are a-z, A-Z, 0-9.";
var INVALID_NUMBER_FORMAT = "Please enter ID of numeric characters, with no spaces. Acceptable characters are 0-9.";
var INVALID_STUDENT_MINLENGTH_FORMAT = "Please enter ID of minimum length:";;
var INVALID_DATE = "Please re-select valid month, day, and year.";
var invalid_char_message = "One or more fields contain invalid formats or invalid values:";
var invalid_birthdate = "Invalid Date of Birth:";

var grade; var gender; var day; var year; var month; 
var firstName;var middleName;var lastName;var studentExternalId;var studentExternalId2;
var studentNumber;var selectedOrgNodes ;var studentSecondNumber;var studentIdLabelName;
var studentId2LabelName;var studentIdConfigurable;var studentId2Configurable;
var isStudentIdNumeric; var isStudentId2Numeric; var studentIdMinLength; var studentId2MinLength;

function VerifyStudentDetail(assignedOrgNodeIds){
                       // alert($("#isMandatoryBirthDate").val());
						
						var requiredFields = "";
			            var requiredFieldCount = 0;
						var isMandatoryBirthdate = $("#isMandatoryBirthDate").val();
						var isMandatoryStudentId = $("#isMandatoryStudentId").val();
						firstName = $("#studentFirstName").val();
						middleName = $("#studentMiddleName").val();
						lastName = $("#studentLastName").val();
						month = $("#monthOptions").val();
						day = $("#dayOptions").val();
						year = $("#yearOptions").val();
						gender = $("#genderOptions").val();
						grade = $("#gradeOptions").val();
						studentNumber = $("#studentExternalId").val();
						studentSecondNumber = $("#studentExternalId2").val();
						selectedOrgNodes = assignedOrgNodeIds;
                        studentIdLabelName = $("#studentIdLabelName").val();
                        studentId2LabelName = $("#studentId2LabelName").val();
						studentIdConfigurable = $("#isStudentIdConfigurable").val();
						studentId2Configurable= $("#isStudentId2Configurable").val();
                        isStudentIdNumeric = $("#isStudentIdNumeric").val();
                        isStudentId2Numeric = $("#isStudentId2Numeric").val();
                        studentIdMinLength = $("#studentIdMinLength").val();
                        studentId2MinLength = $("#studentId2MinLength").val();
                        firstName = trim(firstName);
			            middleName = trim(middleName);
			            lastName =  trim(lastName);
			            studentNumber = trim(studentNumber);
			            studentSecondNumber = trim(studentSecondNumber);
			            
			if ( firstName.length == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("First Name", requiredFieldCount, requiredFields);       
			}

		   if ( lastName.length == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("Last Name", requiredFieldCount, requiredFields);       
			}
			if(isMandatoryBirthdate == "true") {
				if (!allSelected(month, day, year)) {
					requiredFieldCount += 1;            
					requiredFields = buildErrorString("Date of Birth", requiredFieldCount, requiredFields);       
				}
			}
           if ( grade == "Select a grade") {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("Grade", requiredFieldCount, requiredFields);       
			}

			if ( gender == "Select a gender" ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("Gender", requiredFieldCount, requiredFields);       
			}
					
			if(isMandatoryStudentId == "true"){
				var externalStudentNumber = trim(studentNumber);
				if ( externalStudentNumber.length==0) {
					requiredFieldCount += 1;     
					requiredFields = buildErrorString(studentIdLabelName, requiredFieldCount, requiredFields);   
				}
			}

			if ( selectedOrgNodes.length == "" ) {
				requiredFieldCount += 1;      
				requiredFields = buildErrorString("Organization Assignment", requiredFieldCount, requiredFields);       
			}        
			
			
			if (requiredFieldCount > 0) {
				if (requiredFieldCount == 1) {
					//requiredFields += ("\n" + REQUIRED_TEXT);
					setMessage("Missing required field", requiredFields, "errorMessage", REQUIRED_TEXT);
				}
				else {
					//requiredFields += ("\n" + REQUIRED_TEXT_MULTIPLE);
					setMessage("Missing required fields", requiredFields, "errorMessage", REQUIRED_TEXT_MULTIPLE);
				}
				return false;
			}
			
			var invalidCharFields = verifyCreateStudentName(firstName, lastName, middleName);                
			if (invalidCharFields.length > 0) {
				//invalidCharFields += ("<br/>" + INVALID_NAME_CHARS);
				setMessage(invalid_char_message, invalidCharFields, "errorMessage",INVALID_NAME_CHARS);
				return false;
			}
			invalidCharFields = verifyCreateStudentNumber(studentNumber, studentSecondNumber, studentIdLabelName, studentId2LabelName,studentIdConfigurable, studentId2Configurable  );                
			if (invalidCharFields.length > 0) {
				//invalidCharFields += ("<br/>" + INVALID_NUMBER_CHARS);
				setMessage(invalid_char_message, invalidCharFields, "errorMessage",INVALID_NUMBER_CHARS);
				return false;
			}
		
			invalidCharFields = verifyAlphaNumericStudentNumber(studentNumber, studentSecondNumber, studentIdLabelName, studentId2LabelName, isStudentIdNumeric, isStudentId2Numeric,studentIdConfigurable, studentId2Configurable  );                
			if (invalidCharFields.length > 0) {
				//invalidCharFields += ("<br/>" + INVALID_ALPHANUMBER_CHARS);
				setMessage(invalid_char_message, invalidCharFields, "errorMessage",INVALID_ALPHANUMBER_CHARS);
				return false;
			}
			
			invalidCharFields = verifyConfigurableStudentNumber(studentNumber, studentSecondNumber, studentIdLabelName,studentId2LabelName, isStudentIdNumeric, isStudentId2Numeric);                
			if (invalidCharFields.length > 0) {
				//invalidCharFields += ("<br/>" + INVALID_NUMBER_FORMAT);
				setMessage(invalid_char_message, invalidCharFields, "errorMessage",INVALID_NUMBER_FORMAT);
					return false;
			}
			
			invalidCharFields = verifyMinLengthStudentNumber(studentNumber, studentSecondNumber, studentIdLabelName, studentId2LabelName, studentIdMinLength, studentId2MinLength );                
			if (invalidCharFields.length > 0) {
				var str = [];
				str = invalidCharFields.split(",");
				var invalidFields = INVALID_STUDENT_MINLENGTH_FORMAT;  
				for(var temp=0; temp< str.length; temp++){
					if(trim(str[temp])== studentIdLabelName)
						invalidFields += (" " + studentIdLabelName +" - " + studentIdMinLength + " characters");  
					if(trim(str[temp])==studentId2LabelName)
						invalidFields += (" " + studentId2LabelName +" - " + studentId2MinLength+ " characters");  
				}
		
				setMessage(invalid_char_message, invalidCharFields, "errorMessage" , invalidFields);
					return false;
				}
						
			if(isMandatoryBirthdate == "true" && !allSelected(month, day, year)) {
				if (!noneSelected(month, day, year)) {
					//invalidCharFields += INVALID_DATE;
					setMessage(invalid_birthdate, invalidCharFields,"errorMessage",INVALID_DATE);
					return false;
					      
				}
			}
						
			if (allSelected(month, day, year)) {
				var isDateValid = validateDateValues(year, month, day);
				if (isDateValid == 0) {
					//invalidCharFields += INVALID_DATE;
					setMessage(invalid_birthdate, invalidCharFields, "errorMessage", INVALID_DATE);
					return false;
				}
			}
			return true;			
													
  }
    
 
    
	
    function trim(str, chars) {
		return ltrim(rtrim(str, chars), chars);
	}
 
	function ltrim(str, chars) {
		chars = chars || "\\s";
		return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
	}
 
	function rtrim(str, chars) {
		chars = chars || "\\s";
		return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
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
			//$('#messageType').attr ('src', '/StudentManagementWeb/resources/images/messaging/icon_error.gif');
			
		}	
		
		function setMessageMain(title, content, type, message){
			$("#titleMain").text(title);
			$("#contentMain").text(content);
			$("#messageMain").text(message);
		}
		
	 function allSelected(month, day, year){
    	if (month == null || day == null || year == null)
    		return false;
    	else if (month == "" || day == "" || year == "")
            return false;
        else
            return true;    
    }
	function verifyCreateStudentName(firstName, lastName, middleName) {
        var invalidCharFields = "";
        var invalidCharFieldCount = 0;

        if (! validNameString(firstName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("First Name", invalidCharFieldCount, invalidCharFields);       
        }
        
        if (! validNameString(lastName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Last Name", invalidCharFieldCount, invalidCharFields);       
        }

        if (! validNameString(middleName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Middle Name", invalidCharFieldCount, invalidCharFields);       
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
        function verifyCreateStudentNumber(studentNumber, studentSecondNumber, studentIdLabelName, studentId2LabelName, studentIdConfigurable, studentId2Configurable ){
		var invalidCharFields = "";
		var invalidCharFieldCount = 0;
		if(!(studentIdConfigurable == "true")) {
			if (studentNumber != "") {
				if (! validTextString(studentNumber) ) {
					invalidCharFieldCount += 1;            
					invalidCharFields = buildErrorString(studentIdLabelName, invalidCharFieldCount, invalidCharFields);       //Changed for GA2011CR001
				}
			}
		}
		
		if(!(studentId2Configurable == "true")) {
			if (studentSecondNumber != "") {
				if (! validTextString(studentSecondNumber) ) {
					invalidCharFieldCount += 1;            
					invalidCharFields = buildErrorString(studentId2LabelName, invalidCharFieldCount, invalidCharFields);      //Changed for GA2011CR001 
				}
			}
		}
		
		
		return invalidCharFields;
	}
	
	function validTextString(str){
        var characters = [];
         characters = toCharArray(str);
        for (var i=0 ; i<characters.length ; i++) {
            var character = characters[i];
            if (! validTextCharacter(character))
                return false;
        }
        return !requestHasInvalidParameters(str);
    }
    
    function validTextCharacter(str){
        var ch = toascii(str);
        var zero_nine = ((ch >= 48) && (ch <= 57));
        var A_Z = ((ch >= 65) && (ch <= 90));
        var a_z = ((ch >= 97) && (ch <= 122));
        var validChar = (ch == ' ');
        
        return (zero_nine || A_Z || a_z || validChar);
    } 
    
function verifyAlphaNumericStudentNumber(studentNumber, studentSecondNumber, studentIdLabelName, studentId2LabelName,isStudentIdNumeric,isStudentId2Numeric,studentIdConfigurable, studentId2Configurable ){
		var invalidCharFields = "";
		var invalidCharFieldCount = 0;
		if(studentIdConfigurable == "true" && isStudentIdNumeric == "AN") {
			if (studentNumber != "") {
				if (! validAlphaNumericString(studentNumber) ) {
					invalidCharFieldCount += 1;            
					invalidCharFields = buildErrorString(studentIdLabelName, invalidCharFieldCount, invalidCharFields);      
				}
			}
		}
		
		if(studentId2Configurable == "true" && isStudentId2Numeric == "AN") {
			if (studentSecondNumber != "") {
				if (! validAlphaNumericString(studentSecondNumber) ) {
					invalidCharFieldCount += 1;            
					invalidCharFields = buildErrorString(studentId2LabelName, invalidCharFieldCount, invalidCharFields);    
				}
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
   
    function verifyConfigurableStudentNumber(studentNumber,studentSecondNumber,studentIdLabelName,studentId2LabelName, isStudentIdNumeric, isStudentId2Numeric){
		var invalidCharFields = "";
		var invalidCharFieldCount = 0;
		if(isStudentIdNumeric == "NU") {
			if (studentNumber != "") {
				if (! validNumber(studentNumber) ) {
					invalidCharFieldCount += 1;            
					invalidCharFields = buildErrorString(studentIdLabelName, invalidCharFieldCount, invalidCharFields);      
				}
			}
		}
		
		if(isStudentId2Numeric == "NU") {
			if (studentSecondNumber != "") {
				if (! validNumber(studentSecondNumber) ) {
					invalidCharFieldCount += 1;            
					invalidCharFields = buildErrorString(studentId2LabelName, invalidCharFieldCount, invalidCharFields);     
				}
			}
		}

		return invalidCharFields;
	}
	
	function validNumber(str){
		str = trim(str);
		var characters = [];
		characters = toCharArray(str);

		for (var i=0 ; i<str.length ; i++) {
		var charCode = str.charCodeAt(i);
			if (!((charCode >= 48) && (charCode <= 57))) {
				return false;
			}
		} 
		return true;
	}
	
	function verifyMinLengthStudentNumber(studentNumber,studentSecondNumber,studentIdLabelName, studentId2LabelName,studentIdMinLength,studentId2MinLength){
		var invalidCharFields = "";
		var invalidCharFieldCount = 0;
		
		if (studentNumber != "") {
			if ((studentNumber.length > 0 ) && studentNumber.length < parseInt(studentIdMinLength)) {     
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(studentIdLabelName, invalidCharFieldCount, invalidCharFields);       
			}
		}
		if (studentSecondNumber != "") {
			if ((studentSecondNumber.length > 0 ) && studentSecondNumber.length < parseInt(studentId2MinLength)) {     
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(studentId2LabelName, invalidCharFieldCount, invalidCharFields);      
			}
		}

		return invalidCharFields;
	}
	
	function noneSelected(month, day, year){
        if (month == "" && day == "" && year == "")
            return true;
        else
            return false;    
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