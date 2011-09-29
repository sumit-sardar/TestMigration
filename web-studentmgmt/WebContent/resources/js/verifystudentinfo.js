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
var studentNumber;var selectedOrgNodes = [];var studentSecondNumber;var studentIdLabelName;
var studentId2LabelName;var studentIdConfigurable;var studentId2Configurable;
var isStudentIdNumeric; var isStudentId2Numeric; var studentIdMinLength; var studentId2MinLength;

function VerifyStudentDetail(){
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
						/*selectedOrgNodes = *;*/
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

           if ( grade == "Select a grade") {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("Grade", requiredFieldCount, requiredFields);       
			}

			if ( gender == "Select a gender" ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("Gender", requiredFieldCount, requiredFields);       
			}
			if(isMandatoryBirthdate) {
				if (!allSelected(month, day, year)) {
					requiredFieldCount += 1;            
					requiredFields = buildErrorString("Date of Birth", requiredFieldCount, requiredFields);       
				}
			}
			
			if(isMandatoryStudentId){
				var externalStudentNumber = trim(studentNumber);
				if ( externalStudentNumber.length==0) {
					requiredFieldCount += 1;     
					requiredFields = buildErrorString(studentIdLabelName, requiredFieldCount, requiredFields);   
				}
			}

			if ( selectedOrgNodes.size == 0 ) {
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
			
			/*var invalidCharFields = verifyCreateStudentName(firstName, lastName, middleName);                
			if (invalidCharFields.length > 0) {
				invalidCharFields += ("<br/>" + INVALID_NAME_CHARS);
				setMessage(getMessage("invalid_char_message"), invalidCharFields, "errorMessage");
				return false;
			}
			invalidCharFields = verifyCreateStudentNumber(studentNumber, studentSecondNumber, studentIdLabelName, studentId2LabelName,studentIdConfigurable, studentId2Configurable  );                
			if (invalidCharFields.length > 0) {
				invalidCharFields += ("<br/>" + INVALID_NUMBER_CHARS);
				setMessage(invalid_char_message, invalidCharFields, "errorMessage");
				return false;
			}
		
			invalidCharFields = verifyAlphaNumericStudentNumber(studentNumber, studentSecondNumber, studentIdLabelName, studentId2LabelName, isStudentIdNumeric, isStudentId2Numeric,studentIdConfigurable, this.studentId2Configurable  );                
			if (invalidCharFields.length > 0) {
				invalidCharFields += ("<br/>" + INVALID_ALPHANUMBER_CHARS);
				setMessage(invalid_char_message, invalidCharFields, "errorMessage");
				return false;
			}
			
			invalidCharFields = verifyConfigurableStudentNumber(studentNumber, studentSecondNumber, studentIdLabelName,studentId2LabelName, isStudentIdNumeric, isStudentId2Numeric);                
			if (invalidCharFields.length > 0) {
				invalidCharFields += ("<br/>" + INVALID_NUMBER_FORMAT);
				setMessage(invalid_char_message, invalidCharFields, "errorMessage");
					return false;
			}
			
			invalidCharFields = verifyMinLengthStudentNumber(studentNumber, studentSecondNumber, studentIdLabelName, studentId2LabelName, studentIdMinLength, studentId2MinLength );                
			if (invalidCharFields.length > 0) {
				var str = [];
				str = invalidCharFields.split(",");
				invalidCharFields += ("<br/>" + INVALID_STUDENT_MINLENGTH_FORMAT);  
				for(var temp :  str){
					if(trim(temp)== studentIdLabelName)
						invalidCharFields += ("<br/>" + studentIdLabelName +" - " + studentIdMinLength + " characters");  
					if(trim(temp)==studentId2LabelName)
						invalidCharFields += ("<br/>" + studentId2LabelName +" - " + studentId2MinLength+ " characters");  
				}
		
				setMessage(invalid_char_message, invalidCharFields, "errorMessage");
					return false;
				}
						
			if(isMandatoryBirthdate && !allSelected(month, day, year)) {
				if (!noneSelected(month, day, year)) {
					invalidCharFields += INVALID_DATE;
					setMessage(invalid_birthdate, invalidCharFields,"errorMessage");
					return false;
					      
				}
			}
						
			if (allSelected(month, day, year)) {
				var isDateValid = validateDateValues(year, month, day);
				if (isDateValid != 0) {
					invalidCharFields += INVALID_DATE;
					setMessage(invalid_birthdate, invalidCharFields, "errorMessage");
					return false;
				}
			}*/
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
		}	
		
	 function allSelected(month, day, year){
    	if (month == null || day == null || year == null)
    		return false;
    	else if (month == "" || day == "" || year == "")
            return false;
        else
            return true;    
    }
	
     
    

