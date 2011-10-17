var INVALID_NUMBER_CHARS = "Please re-enter your string with these characters: A-Z, a-z, 0-9, space";
var INVALID_NAME_CHARS = "Please re-enter your information with only these characters: A-Z, a-z, 0-9, /, \\, -, ', (, ), &, +, comma, period, space";
var REQUIRED_TEXT = "Please enter/select this value to continue.";
var REQUIRED_TEXT_MULTIPLE = "Please enter/select these values to continue.";
var INVALID_ALPHANUMBER_CHARS = "Please enter Email in proper format.";
var INVALID_NUMBER_FORMAT = "Please enter phone and fax with numeric characters, with no spaces. Acceptable characters are 0-9.";
var invalid_char_message = "One or more fields contain invalid formats or invalid values:";
var INVALID_EMAIL  = "Please enter a valid email address";
var INVALID_FORMAT_TITLE = "One or more fields contain invalid formats or invalid values:"; 
var INVALID_ADDRESS_CHARS  = "Please re-enter your information with only these characters: A-Z, a-z, 0-9, #, /, \\, -, ', (, ), &, +, comma, period, space";
var INVALID_CITY_CHARS  = "Please re-enter your information with only these characters: A-Z, a-z, 0-9, /, \\, -, ', (, ), &, +, comma, period, space";
var INVALID_EMAIL  = "Please enter a valid email address";
      
    

var firstName;var middleName;var lastName;var userEmail;var userRole;
var timeZone;var selectedOrgNodes ;var addressLine1;var addressLine2;
var city;var state;var zipCode1;var zipCode2;var primaryPhone1;
var faxNumber;var primaryPhone2; var primaryPhone3;var primaryPhone4;var secondaryPhone1;
var secondaryPhone2;var secondaryPhone3;var secondaryPhone4;var faxNumber1;var faxNumber2;var faxNumber3;
	  
function VerifyUserDetail(assignedOrgNodeIds){	  
	 
	 var requiredFields = "";
     var requiredFieldCount = 0; 
     firstName = $("#userFirstName").val();
	 middleName = $("#userMiddleName").val();
	 lastName = $("#userLastName").val();
	 userRole = $("#roleOptions").val();
	 timeZone = $("#timeZoneOptions").val();
	 userEmail = $("#userEmail").val();
	 selectedOrgNodes = trim(assignedOrgNodeIds);
	 firstName = trim(firstName);
	 middleName = trim(middleName);
	 lastName =  trim(lastName); 
	 addressLine1 = $("#addressLine1").val();
	 addressLine2 = $("#addressLine2").val();
	 city = $("#city").val();	
	 state = $("#stateOptions").val();
	 zipCode1 = $("#zipCode1").val();
	 zipCode2 = $("#zipCode2").val();
	 primaryPhone1 = $("#primaryPhone1").val();
	 primaryPhone2 = $("#primaryPhone2").val();
	 primaryPhone3 = $("#primaryPhone3").val();
	 primaryPhone4 = $("#primaryPhone4").val();
	 secondaryPhone1 = $("#secondaryPhone1").val();
	 secondaryPhone2 = $("#secondaryPhone2").val();
	 secondaryPhone3 = $("#secondaryPhone3").val();
	 secondaryPhone4 = $("#secondaryPhone4").val();
	 faxNumber1 = $("#faxNumber1").val();	
	 faxNumber2 = $("#faxNumber2").val();	
	 faxNumber3 = $("#faxNumber3").val();
	  
	  
	if ( firstName.length == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("First Name", requiredFieldCount, requiredFields);       
			}
   if ( lastName.length == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("Last Name", requiredFieldCount, requiredFields);       
			}

   if ( timeZone == "-1" ) {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("TimeZone", requiredFieldCount, requiredFields);       
			}  

	if ( userRole == "-1") {
				requiredFieldCount += 1;            
				requiredFields = buildErrorString("Role", requiredFieldCount, requiredFields);       
			}

   if ( selectedOrgNodes == "" ) {
			requiredFieldCount += 1;            
			requiredFields = buildErrorString("Organization Assignment", requiredFieldCount, requiredFields);       
		} 
			
   //if ( selectedOrgNodes.length == "" ) {
	//			requiredFieldCount += 1;      
	//			requiredFields = buildErrorString("Organization Assignment", requiredFieldCount, requiredFields);       
	//		}        
			
			
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
			
 var invalidCharFields = verifyUserInfo(firstName, middleName,lastName);
 var invalidString = "";                        
    if (invalidCharFields.length > 0) {
          setMessage(invalid_char_message, invalidCharFields, "errorMessage",INVALID_NAME_CHARS);
				return false;
	}																									   
if(userEmail != "") {
 var validmail = validEmail(userEmail);  

  if (validmail == false) {
     if(invalidString != ""){
    invalidString += ("<br/>");
    }
    //invalidString += "Email" + ("<br/>" + INVALID_EMAIL);
    setMessage(invalid_char_message, invalidCharFields, "errorMessage",INVALID_EMAIL);
				return false;
 }   
}   
  if (invalidString!= "") {    
     setMessage(INVALID_FORMAT_TITLE, invalidString,"errorMessage");
     return false;
 } 
 
 
 var val = isInvalidUserContact();
  if (val){
            return false;
   }
   
     return true;	
			
}
	  
  function isInvalidUserContact()
    {
        var invalidCharFields = verifyUserAddressInfo(addressLine1,addressLine2);
        var invalidString = "";                        
        if (invalidCharFields.length > 0) {
            //invalidString = invalidCharFields + ("<br/>" + INVALID_ADDRESS_CHARS);
            setMessage(invalid_char_message, invalidCharFields, "errorMessage",INVALID_ADDRESS_CHARS);
				
        }	
        
        // this section is for the validation of City
        invalidCharFields = verifyUserCityInfo(city);
        if (invalidCharFields.length > 0) {
            if(invalidString!= ""){
               invalidString += ("<br/>");
            }
            //invalidString += invalidCharFields + ("<br/>" + INVALID_CITY_CHARS);
            setMessage(invalid_char_message, invalidCharFields, "errorMessage",INVALID_CITY_CHARS);
				
        }																									   
         
        // this section is for the validation of Zip  
        var invalidNumFields = "";
        if (isInvalidZip(zipCode1,zipCode2)) {
            invalidNumFields += "Zip";
        }
        
        // this section is for the validation of Primary Phone 
        if (isInvalidPrimaryPhone(primaryPhone1,primaryPhone2,primaryPhone3,primaryPhone4)){
            if(invalidNumFields!="" && invalidNumFields.length>0) {
               invalidNumFields += ", ";
            }
            invalidNumFields += "Primary Phone";
        }
        
        if (isInvalidSecondaryPhone(secondaryPhone1,secondaryPhone2,secondaryPhone3,secondaryPhone4)){
            if(invalidNumFields!="" && invalidNumFields.length>0){
               invalidNumFields += ", ";
            }
            invalidNumFields += "Secondary Phone";
        }
        
        if (isInvalidFaxNumber(faxNumber1,faxNumber2,faxNumber3)){
            if(invalidNumFields!="" && invalidNumFields.length>0){
               invalidNumFields += ", ";
            }
            invalidNumFields += "Fax Number";
        }
               
        if (invalidNumFields != "" && invalidNumFields.length>0 ) {
            if(invalidString!="" && invalidString.length>0){
               invalidString += ("<br/>");
            }
            //invalidString += invalidNumFields + ("<br/>" + INVALID_NUMBER_FORMAT);
            setMessage(invalid_char_message, invalidNumFields, "errorMessage",INVALID_NUMBER_FORMAT);
				return false;
        }
                         
        if (invalidString!="" && invalidString.length > 0 ) {    
            setMessage(INVALID_FORMAT_TITLE, invalidString, "errorMessage");
            return false;
        } 
        
        return true;   
    }
    
   function verifyUserAddressInfo(addressLine1,addressLine2)
    {
        var invalidCharFields = "";
        var invalidCharFieldCount = 0;

        if (!validAddressString(addressLine1) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Address Line1", invalidCharFieldCount, invalidCharFields);       
        }
        
        if (!validAddressString(addressLine2) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Address Line2", invalidCharFieldCount, invalidCharFields);       
        }
                
        return invalidCharFields;
    }
    
    function validAddressString(str)
    {
        str = trim(str);
        var characters = [];
        characters = toCharArray(str);
        for (var i=0 ; i<characters.length ; i++) {
            var character = characters[i];
            if (! validAddressCharacter(character) )
                return false;
        }
        return !requestHasInvalidParameters(str);
    }
    
    function validAddressCharacter(ch)
    {
        var valid = validNameCharacter(ch);
    	if (ch == '#')
           valid = true; 
    
        return valid;
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
    
    
	 function verifyUserInfo(firstName, middleName,lastName)
    {
        var invalidCharFields = "";
        var invalidCharFieldCount = 0;

        if (!validNameString(firstName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("First Name", invalidCharFieldCount, invalidCharFields);       
        }
        
        if (!validNameString(middleName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Middle Name", invalidCharFieldCount, invalidCharFields);       
        }
        
        if (!validNameString(lastName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Last Name", invalidCharFieldCount, invalidCharFields);       
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
		
		function setMessageMain(title, content, type, message){
			$("#titleMain").text(title);
			$("#contentMain").text(content);
			$("#messageMain").text(message);
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
  function verifyUserCityInfo(str)
    {
        var invalidCharFields = "";
        var invalidCharFieldCount = 0;

        if (!validNameString(str) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("City", invalidCharFieldCount, invalidCharFields);       
        }
        
          
        return invalidCharFields;
    }     
     
    
  function isInvalidZip(zipCode1,zipCode2) 
    {
        var invalidZiplen = invalidZipLength(zipCode1,zipCode2);
        if (invalidZiplen) {
            return true;
        }
        
        var invalidZipFor  = invalidZipFormat(zipCode1,zipCode2);
        if (invalidZipFor) {
            return true;
        }
        
       
        return false;
    } 
   
  function invalidZipLength(zipCode1,zipCode2) 
    {     
       if (trim(zipCode1).length > 0 
            || trim(zipCode2).length > 0) {
                
            if(trim(zipCode1).length < 5 ) {
            //    || (userProfile.getUserContact().getZipCode2().trim().length() != 0 
            //    && userProfile.getUserContact().getZipCode2().trim().length() < 5 )) {
                        
                return true;
            }
            
        }
        return false;
    }
    
   function invalidZipFormat(zipCode1,zipCode2) 
    {     
        if ( (! validNumber(zipCode1)) ||
             (! validNumber(zipCode2)) ) {                    
            return true;
        }
        return false;
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
	 
	function isInvalidPrimaryPhone(primaryPhone1,primaryPhone2,primaryPhone3,primaryPhone4) 
    {
        var invalidPhonelength = invalidPrimaryPhoneLength(primaryPhone1,primaryPhone2,primaryPhone3,primaryPhone4);
        if (invalidPhonelength) {
            return true;
        }
        
        var invalidPhoneFormat  = invalidPrimaryPhoneFormat(primaryPhone1,primaryPhone2,primaryPhone3,primaryPhone4);
        if (invalidPhoneFormat) {
            return true;
        }
        
        return false;
    }
     
     function invalidPrimaryPhoneLength(primaryPhone1,primaryPhone2,primaryPhone3,primaryPhone4) 
     {         
        if (trim(primaryPhone1).length > 0 ||
            trim(primaryPhone2).length > 0 ||
            trim(primaryPhone3).length > 0) {
                        
            if (trim(primaryPhone1).length < 3 ||
                trim(primaryPhone2).length < 3 ||
                trim(primaryPhone3).length < 4) { 
                return true;
            }
        }

        if (trim(primaryPhone1).length == 0 && 
            trim(primaryPhone2).length == 0 && 
            trim(primaryPhone3).length == 0 && 
            trim(primaryPhone4).length > 0) { 
            return true;
        }
        
        return false;
     } 
	
	function invalidPrimaryPhoneFormat(primaryPhone1,primaryPhone2,primaryPhone3,primaryPhone4)
    {
        if ((! validNumber(primaryPhone1)) ||
            (! validNumber(primaryPhone2)) ||
            (! validNumber(primaryPhone3)) ||
            (! validNumber(primaryPhone4))) {
            return true;       
        }
                
        return false;
    }
    
    function isInvalidSecondaryPhone(secondaryPhone1,secondaryPhone2,secondaryPhone3,secondaryPhone4) 
    {
        var invalidPhonelength = invalidSecondaryPhoneLength(secondaryPhone1,secondaryPhone2,secondaryPhone3,secondaryPhone4);
        if (invalidPhonelength) {
            return true;
        }
        
        var invalidPhoneFormat  = invalidSecondaryPhoneFormat(secondaryPhone1,secondaryPhone2,secondaryPhone3,secondaryPhone4);
        if (invalidPhoneFormat) {
            return true;
        }
        
        return false;
    }
    
     function invalidSecondaryPhoneLength(secondaryPhone1,secondaryPhone2,secondaryPhone3,secondaryPhone4) 
     {         
        if (trim(secondaryPhone1).length > 0 ||
            trim(secondaryPhone2).length > 0 ||
            trim(secondaryPhone3).length > 0) {
                            
                if (trim(secondaryPhone1).length < 3 ||
                    trim(secondaryPhone2).length < 3 ||
                    trim(secondaryPhone3).length < 4) { 
                return true;
            }
        }

        if (trim(secondaryPhone1).length == 0 && 
            trim(secondaryPhone2).length == 0 && 
            trim(secondaryPhone3).length == 0 && 
            trim(secondaryPhone4).length > 0) { 
            return true;
        }
        
        return false;
     }
     
    function invalidSecondaryPhoneFormat(secondaryPhone1,secondaryPhone2,secondaryPhone3,secondaryPhone4)
    {
        if ((! validNumber(secondaryPhone1)) ||
            (! validNumber(secondaryPhone2)) ||
            (! validNumber(secondaryPhone3)) ||
            (! validNumber(secondaryPhone4))) {
            return true;       
        }
                
        return false;
    }
    
    function isInvalidFaxNumber(faxNumber1,faxNumber2,faxNumber3) 
    {
        var invalidPhonelength = invalidFaxNumberLength(faxNumber1,faxNumber2,faxNumber3);
        if (invalidPhonelength) {
            return true;
        }
        
        var invalidPhoneFormat  = invalidFaxNumberFormat(faxNumber1,faxNumber2,faxNumber3);
        if (invalidPhoneFormat) {
            return true;
        }
        
        return false;
    }
    
    function invalidFaxNumberLength(faxNumber1,faxNumber2,faxNumber3) 
     {         
        if (trim(faxNumber1).length > 0 ||
            trim(faxNumber2).length > 0 ||
            trim(faxNumber3).length > 0) {
                    
            if (trim(faxNumber1).length < 3 ||
                trim(faxNumber2).length < 3 ||
                trim(faxNumber3).length < 4 ) {                           
                return true;
            } 
        }
        
        return false;
     }
    
    function invalidFaxNumberFormat(faxNumber1,faxNumber2,faxNumber3)
    {
        if ((! validNumber(faxNumber1)) ||
            (! validNumber(faxNumber2)) ||
            (! validNumber(faxNumber3))) {
            return true;       
        }
                
        return false;
    }
    
   
   function validEmail(emailStr) 
    {
 	 if ((emailStr.indexOf('@') < 0) || ((emailStr.charAt(emailStr.length-4) != '.') && (emailStr.charAt(emailStr.length-3) != '.')))
      { 
       return false;
      }
     return true;

}

