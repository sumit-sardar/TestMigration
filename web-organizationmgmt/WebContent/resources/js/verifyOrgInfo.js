
/*var REQUIRED_TEXT = "Please enter/select this value to continue.";
var REQUIRED_TEXT_MULTIPLE = "Please enter/select these values to continue.";
var INVALID__MDRNUMBER_FORMAT  = "Please re-enter your information with a unique 8-digit combination of 0-9.";
var INVALID_DUP_FORMAT_TITLE = "One or more fields contain invalid formats, duplicate or invalid values:";
var INVALID_NAME_CHARS_ORG  = "Please re-enter your information with these characters: A-Z, a-z, 0-9, /, \\, -, ', (, ), &, +, comma, period, space";
var INVALID_FORMAT_TITLE = "One or more fields contain invalid formats or invalid values:";*/ 

var orgName="";
var mDRNumber="";
var orgType="";
var parentOrgName="";
var orgCode="";
var isLasLinkCustomer = false;

	function VerifyOrgDetail(assignedOrgNodeIds, isLasLinkCustomer1) {
	
	    isLasLinkCustomer = isLasLinkCustomer1;
        
        // check for required fields
        var requiredFields = "";
        var requiredFieldCount = 0;
        
        orgName = $.trim($("#orgName").val());
        if ( orgName == "" || orgName.length == 0 ) {
            
            requiredFieldCount += 1;          
            requiredFields = buildErrorString($("#orgNameID").val(), requiredFieldCount, requiredFields);       
        
        }
        if(isLasLinkCustomer == 'true'){
	        mDRNumber = $.trim($("#mdrNumber").val());
	        if ( mDRNumber == "" || mDRNumber.length == 0 ) {
	                requiredFieldCount += 1;            
	                requiredFields = buildErrorString($("#mdrID").val(), requiredFieldCount, requiredFields);       
	        }
        }

        orgType = $.trim($("#layerOptions").val());
        if ( orgType == "" || orgType.length == 0 || orgType == 'Select a layer') {
            requiredFieldCount += 1;            
            requiredFields = buildErrorString($("#layerID").val(), requiredFieldCount, requiredFields);       
        }
                
        if ( assignedOrgNodeIds == "" || assignedOrgNodeIds.length == 0) {
            requiredFieldCount += 1;            
            requiredFields = buildErrorString($("#pOrgID").val(), requiredFieldCount, requiredFields);       
        
        }
        
        orgCode = $.trim($("#orgCode").val());
                
        if (requiredFieldCount > 0) {
            if (requiredFieldCount == 1) {
               
                setMessage($("#missRequiredID").val(), requiredFields, "errorMessage", $("#requiredID").val());
            
            }
            else {
                
                setMessage($("#mMissRequiredID").val(), requiredFields, "errorMessage", $("#mRequiredID").val());
            
            }
            return false;
        }
        
        
  	  	var invalidCharFields = verifyOrgInfo();   
  	  	var invalidCharFieldCount = 0;                     
 
 	    if (invalidCharFields.length > 0) {
	          setMessage($("#invalidFormatID").val(), invalidCharFields, "errorMessage", $("#invalidNameCharsID").val());
					return false;
		}
        

  	  	
  	  	 
	  	if(isLasLinkCustomer == 'true'){
  			        if ((mDRNumber.length > 0) && mDRNumber.length < 8) {
  			            
  			            invalidCharFieldCount += 1;            
  			            invalidCharFields = buildErrorString($("#mdrID").val(), invalidCharFieldCount, invalidCharFields);       
  			        
  			        }
	  		      
	  			
	  		        if ( invalidCharFieldCount > 0) {
	  		            
	  		            invalidString = $("#mdrNumberID").val();
	  		            setMessage($("#invalidFormatID").val(), $("#mdrID").val(), "errorMessage", invalidString);
	  		             return false; 
	  		        }	
  		      
  		}
  		
  		var mdrField = "";
  		var invalidMDRCount = 0;
  		var invalidString = "";
  		
  		if(isLasLinkCustomer == 'true') {
  		
  		$.ajax({
		async:		false,
		beforeSend:	function(){
					UIBlock();
					},
		url:		'uniqueMDRNumber.do?mdrNumber='+mDRNumber,
		type:		'POST',
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){	
						$.unblockUI();
					if(data.length > 0 && data == 'F')
							invalidMDRCount += 1;
						
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					},
		complete :  function(){
					}
		});
	  		if(invalidMDRCount > 0) {
	  		
	  			 setMessage($("#dupFormatID").val(), $("#mdrID").val(), "errorMessage", $("#mdrNumberID").val());
	  			return false;
	  		}
  		}
  		
  		
  		
  		return true;
        
    }





	function verifyOrgInfo()
    {
        var invalidCharFields = "";
        var invalidCharFieldCount = 0;

        if ( !validNameString(orgName) ) {
        
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString($("#orgNameID").val(), invalidCharFieldCount, invalidCharFields);       
        
        }
        
        if ( !validNameString(orgCode) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString($("#orgCodeID").val(), invalidCharFieldCount, invalidCharFields);       
        
        }
        if(isLasLinkCustomer == 'true'){
	        if ( !validNameString(mDRNumber) ) {
	            
	            invalidCharFieldCount += 1;            
	            invalidCharFields = buildErrorString($("#mdrID").val(), invalidCharFieldCount, invalidCharFields);       
	        
	        }
        }
            
        return invalidCharFields;
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







function setMessage(title, content, type, message){
			$("#title").text(title);
			$("#content").html(content);
			$("#message").text(message);
	
		}

function setMessageMain(title, content, type, message){
			$("#titleMain").text(title);
			$("#contentMain").text(content);
			$("#messageMain").text(message);
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