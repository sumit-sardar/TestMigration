var dfirstName;var dmiddleName;var dlastName;var duserEmail;var duserRole;
var dtimeZone;var daddressLine1;var daddressLine2;
var dcity;var dstate;var dzipCode1;var dzipCode2;var dprimaryPhone1;
var dfaxNumber;var dprimaryPhone2; var dprimaryPhone3;var dprimaryPhone4;var dsecondaryPhone1;
var dsecondaryPhone2;var dsecondaryPhone3;var dsecondaryPhone4;var dfaxNumber1;var dfaxNumber2;var dfaxNumber3;
  
function VerifyLoginUserDetail(orgNodeIds){	  
	 
	var requiredFields = "";
    var requiredFieldCount = 0; 

	dfirstName = $("#profileFirstName").val();
	dmiddleName = $("#profileMiddleName").val();
	dlastName = $("#profileLastName").val();
	dtimeZone = $("#profileTimeZoneOptions").val();
	duserEmail = $("#profileEmail").val();
	dfirstName = trim(dfirstName);
	dmiddleName = trim(dmiddleName);
	dlastName =  trim(dlastName); 
	daddressLine1 = $("#profileAddressLine1").val();
	daddressLine2 = $("#profileAddressLine2").val();
	dcity = $("#profileCity").val();	
	dstate = $("#profileStateOptions").val();
	dzipCode1 = $("#profileZipCode1").val();
	dzipCode2 = $("#profileZipCode2").val();
	dprimaryPhone1 = $("#profilePrimaryPhone1").val();
	dprimaryPhone2 = $("#profilePrimaryPhone2").val();
	dprimaryPhone3 = $("#profilePrimaryPhone3").val();
	dprimaryPhone4 = $("#profilePrimaryPhone4").val();
	dsecondaryPhone1 = $("#profileSecondaryPhone1").val();
	dsecondaryPhone2 = $("#profileSecondaryPhone2").val();
	dsecondaryPhone3 = $("#profileSecondaryPhone3").val();
	dsecondaryPhone4 = $("#profileSecondaryPhone4").val();
	dfaxNumber1 = $("#profileFaxNumber1").val();	
	dfaxNumber2 = $("#profileFaxNumber2").val();	
	dfaxNumber3 = $("#profileFaxNumber3").val();
	dhintAns = $("#profileHintAns").val();  
	  
	if ( dfirstName.length == 0 ) {
		requiredFieldCount += 1;            
		requiredFields = buildErrorString($("#mpFirstNameID").val(), requiredFieldCount, requiredFields);       
	}
	if ( dlastName.length == 0 ) {
		requiredFieldCount += 1;            
		requiredFields = buildErrorString($("#mpLastNameID").val(), requiredFieldCount, requiredFields);       
	}
	
	if ( dtimeZone == "-1" ) {
		requiredFieldCount += 1;            
		requiredFields = buildErrorString($("#mptZoneID").val(), requiredFieldCount, requiredFields);       
	}
	if($("#profileOldPassword").val().length > 0 || $("#profileNewPassword").val().length > 0 || $("#profileConfirmPassword").val().length > 0 ){
		if( dhintAns.length == 0 ){
			requiredFieldCount += 1;            
			requiredFields = buildErrorString($("#mpHintAns").val(), requiredFieldCount, requiredFields); 
		}
	}
	
	for(var i = 0; i <dbProfileDetails.length ; i++) {
		if(dbProfileDetails[i].name == "profileHintAns"){
			if(trim($("#profileHintAns").val()) != trim(dbProfileDetails[i].value)){
				if($("#profileOldPassword").val().length == 0  && $("#profileNewPassword").val().length == 0 && $("#profileConfirmPassword").val().length == 0){
					requiredFieldCount += 1;            
					requiredFields = buildErrorString($("#mpOldPwd").val(), requiredFieldCount, requiredFields);
					break;
				}
			}	
		}
		
		if(dbProfileDetails[i].name == "profileHintQues"){
			if(trim($("#profileHintQues").val()) != trim(dbProfileDetails[i].value)){
				if($("#profileOldPassword").val().length == 0  && $("#profileNewPassword").val().length == 0 && $("#profileConfirmPassword").val().length == 0){
					requiredFieldCount += 1;            
					requiredFields = buildErrorString($("#mpOldPwd").val(), requiredFieldCount, requiredFields);
					break;
				}
			}	
		}
			 
	}					
	if (requiredFieldCount > 0) {
		if (requiredFieldCount == 1) {
			setMessageProfile($("#mpRequiredID").val(), requiredFields, "errorMessage", $("#mpSRequiredID").val());
		}
		else {
			setMessageProfile($("#mpRequiredIDs").val(), requiredFields, "errorMessage", $("#mpRequiredID").val());
		}
		return false;
	}
			
	var invalidCharFields = verifyInvalidChar(dfirstName, dmiddleName,dlastName);
	var invalidString = "";                        
    if (invalidCharFields.length > 0) {
          setMessageProfile($("#mpInCharID").val(), invalidCharFields, "errorMessage",$("#inNameCharID").val());
				return false;
	}																									   
	if(duserEmail != "") {
		var validmail = true;
		
		if (validmail == false) {
			if(invalidString != ""){
		   		invalidString += ("<br/>");
		   	}
		   setMessageProfile($("#mpInCharID").val(), invalidCharFields, "errorMessage",$("#mpInEmailID").val());
					return false;
		}   
	}   
	if (invalidString!= "") {    
		setMessageProfile($("#mpInFormatID").val(), invalidString,"errorMessage");
		return false;
	} 
	 
	 
	var val = verifyInvalidContact();
	if (val){
		return false;
	}
	  
	    return true;	
			
}

function verifyInvalidChar(firstName, middleName,lastName)
{
    var invalidCharFields = "";
    var invalidCharFieldCount = 0;

    if (!validNameString(firstName) ) {
        invalidCharFieldCount += 1;            
        invalidCharFields = buildErrorString($("#mpFirstNameID").val(), invalidCharFieldCount, invalidCharFields);       
    }
    
    if (!validNameString(middleName) ) {
        invalidCharFieldCount += 1;            
        invalidCharFields = buildErrorString($("#mpMidNameID").val(), invalidCharFieldCount, invalidCharFields);       
    }
    
    if (!validNameString(lastName) ) {
        invalidCharFieldCount += 1;            
        invalidCharFields = buildErrorString($("#mpLastNameID").val(), invalidCharFieldCount, invalidCharFields);       
    }
        
    return invalidCharFields;
}
    
function verifyInvalidContact()
{
    var invalidCharFields = verifyAddress(daddressLine1,daddressLine2);
    var invalidString = "";                        
    if (invalidCharFields.length > 0) {
        setMessageProfile($("#mpInCharID").val(), invalidCharFields, "errorMessage",$("#mpInAddressID").val());
        return true;

    }	
    
    // this section is for the validation of City
    invalidCharFields = verifyCity(dcity);
    if (invalidCharFields.length > 0) {
        if(invalidString!= ""){
           invalidString += ("<br/>");
        }
        setMessageProfile($("#mpInCharID").val(), invalidCharFields, "errorMessage",$("#inNameCharID").val());
         return true;

    }																									   
     
    // this section is for the validation of Zip  
    var invalidNumFields = "";
    if (verifyZip(dzipCode1,dzipCode2)) {
        invalidNumFields += $("#mpZipID").val();
    }
    
    // this section is for the validation of Primary Phone 
    if (isInvalidPrimaryPhone(dprimaryPhone1,dprimaryPhone2,dprimaryPhone3,dprimaryPhone4)){
        if(invalidNumFields!="" && invalidNumFields.length>0) {
           invalidNumFields += ", ";
        }
        invalidNumFields += $("#mpPPhoneID").val();
    }
    
    if (isInvalidSecondaryPhone(dsecondaryPhone1,dsecondaryPhone2,dsecondaryPhone3,dsecondaryPhone4)){
        if(invalidNumFields!="" && invalidNumFields.length>0){
           invalidNumFields += ", ";
        }
        invalidNumFields += $("#mpSPhoneID").val();
    }
    
    if (isInvalidFaxNumber(dfaxNumber1,dfaxNumber2,dfaxNumber3)){
        if(invalidNumFields!="" && invalidNumFields.length>0){
           invalidNumFields += ", ";
        }
        invalidNumFields += $("#mpFaxID").val();
    }
           
    if (invalidNumFields != "" && invalidNumFields.length>0 ) {
        if(invalidString!="" && invalidString.length>0){
           invalidString += ("<br/>");
        }
        //invalidString += invalidNumFields + ("<br/>" + INVALID_NUMBER_FORMAT);
        setMessageProfile($("#mpInCharID").val(), invalidNumFields, "errorMessage",$("#mpInNemericFormatID").val());
		return true;
    }
                     
    if (invalidString!="" && invalidString.length > 0 ) {    
        setMessageProfile($("#mpInFormatID").val(), invalidString, "errorMessage");
        return true;
    } 
    
    return false;   
}
    
function verifyAddress(addressLine1,addressLine2)
{
    var invalidCharFields = "";
    var invalidCharFieldCount = 0;

    if (!validAddressString(addressLine1) ) {
        invalidCharFieldCount += 1;            
        invalidCharFields = buildErrorString($("#mpAddrID1").val(), invalidCharFieldCount, invalidCharFields);       
    }
    
    if (!validAddressString(addressLine2) ) {
        invalidCharFieldCount += 1;            
        invalidCharFields = buildErrorString($("#mpAddrID2").val(), invalidCharFieldCount, invalidCharFields);       
    }
            
    return invalidCharFields;
}

function verifyZip(zipCode1,zipCode2) 
{
    var invalidZiplen = verifyZipLength(zipCode1,zipCode2);
    if (invalidZiplen) {
        return true;
    }
    
    var invalidZipFor  = verifyZipFormat(zipCode1,zipCode2);
    if (invalidZipFor) {
        return true;
    }
    
   
    return false;
} 
 
function verifyZipLength(zipCode1,zipCode2) 
{     
   if (trim(zipCode1).length > 0 
        || trim(zipCode2).length > 0) {
            
        if(trim(zipCode1).length < 5 ) {             
            return true;
        }
        
    }
    return false;
}

function verifyZipFormat(zipCode1,zipCode2) 
{     
    if ( (! validNumber(zipCode1)) ||
         (! validNumber(zipCode2)) ) {                    
        return true;
    }
    return false;
}

function verifyCity(str)
{
    var invalidCharFields = "";
    var invalidCharFieldCount = 0;

    if (!validNameString(str) ) {
        invalidCharFieldCount += 1;            
        invalidCharFields = buildErrorString($("#mpCityID").val(), invalidCharFieldCount, invalidCharFields);       
    }
    
      
    return invalidCharFields;
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

	
function setMessageProfile(title, content, type, message){
	$("#titleProf").text(title);
	$("#contentProf").html(content);
	$("#messageProf").text(message);

}	
	
	