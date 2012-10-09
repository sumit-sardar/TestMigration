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


function AddStudentDetail(){
	isAddStudent = true;
	isAddStudPopup = true;
	profileEditable = "true";//to see fields enabled if a new student is added after editing a imported student.
	resetDisabledFields();
	document.getElementById('displayMessage').style.display = "none";
	document.getElementById('displayMessageMain').style.display = "none";		
	if(!(studentGradeOptions.length > 0 
		&& studentGenderOptions.length > 0
			&& dayOptions.length > 0
				&& monthOptions.length > 0 
					&& yearOptions.length > 0)){
					
 		var postDataObject = {};
 		//postDataObject.isLasLinkCustomer = $("#isLasLinkCustomer").val();
	
	$.ajax({
		async:		true,
		beforeSend:	function(){
						
						UIBlock();
					},
		url:		'getStudentOptionList.do', 
		type:		'POST',
		data:		postDataObject,
		dataType:	'json',
		success:	function(data, textStatus, XMLHttpRequest){
						$.unblockUI();
						//UIBlock();
						//overlayblockUI(); 
						studentGradeOptions = data.gradeOptions;
						studentGenderOptions = data.genderOptions;
						dayOptions = data.dayOptions; 
						monthOptions = data.monthOptions;
						yearOptions = data.yearOptions; 
						//testPurposeOptions = data.testPurposeOptions;
						fillDropDown("gradeOptions", studentGradeOptions);
						fillDropDown("genderOptions", studentGenderOptions);
						fillDropDown("dayOptions", dayOptions);
						fillDropDown("monthOptions", monthOptions);
						fillDropDown("yearOptions", yearOptions);
						/*if($("#isLasLinkCustomer").val() =="true")
							fillDropDown("testPurposeOptions", testPurposeOptions);*/
						//customerDemographicValue = $("#addEditStudentDetail *").serializeArray(); 
						
						$("#studentAddEditDetail").dialog({  
													title:$("#addStuID").val(),  
												 	resizable:false,
												 	autoOpen: true,
												 	width: '800px',
												 	modal: true,
												 	closeOnEscape: false,
												 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		 											});	
						$('#studentAddEditDetail').bind('keydown', function(event) {
							
 							var code = (event.keyCode ? event.keyCode : event.which);
 							if(code == 27){
		  				  	onCancel();
		  				 	return false;
		 				 }
		 				
						});
						setPopupPosition(isAddStudent);	
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/SessionWeb/logout.do";
						
					}
		
	});

	} else {
		$('#Student_Info :checkbox').attr('disabled', false); 
		$('#Student_Info :radio').attr('disabled', false); 
		$('#Student_Info select').attr('disabled', false);
		$('#Student_Info :input').attr('disabled', false);
		
		$("#studentRegFirstName").val(""); 
		$("#studentRegMiddleName").val(""); 
		$("#studentRegLastName").val("");
						
		reset();
		/*if($("#isLasLinkCustomer").val() =="true")
			fillDropDown("testPurposeOptions", testPurposeOptions);*/
		$("#studentAddEditDetail").dialog({  
			title:$("#addStuID").val(),  
		 	resizable:false,
		 	autoOpen: true,
		 	width: '800px',
		 	modal: true,
		 	closeOnEscape: false,
		 	open: function(event, ui) {$(".ui-dialog-titlebar-close").hide(); }
		 	});	
		 	$('#studentAddEditDetail').bind('keydown', function(event) {
		 						  //alert("key up 2");
 							var code = (event.keyCode ? event.keyCode : event.which);
 							if(code == 27){
		  				  	onCancel();
		  				 	return false;
		 				 }
		 				
						});
		setPopupPosition(isAddStudent);	
	}	
}

function resetDisabledFields(){
		$('#Student_Info :checkbox').attr('disabled', false); 
		$('#Student_Info :radio').attr('disabled', false); 
		$('#Student_Info select').attr('disabled', false);
		$('#Student_Info :input').attr('disabled', false);
		$('#Student_Additional_Info :checkbox').attr('disabled', false); 
		$('#Student_Additional_Info :radio').attr('disabled', false); 
		$('#Student_Additional_Info select').attr('disabled', false);
		$('#Student_Additional_Info :input').attr('disabled', false); 
	}
	
function studentDetailSubmit(){
	
	var param;
	var createBy = "";
	var assignedOrg = $('#selectedOrgNode').text();
	var showStudentInGrid = false;
	//updateAccomodationMap = {};
		
	if(profileEditable === "false") {
		resetDisabledFields();
	}
	
	if(isAddStudent){
		param = $("#studentAddEditDetail *").serialize()+ "&assignedOrgNodeIds="+assignedOrgNodeIds+ "&isAddStudent=" + isAddStudent +
		"&createBy="+createBy ;
	}
	
		var validflag = VerifyStudentDetail(assignedOrgNodeIds);
	 
	if(validflag) {
					$.ajax(
						{
								async:		true,
								beforeSend:	function(){
											
												
												UIBlock();
												//alert('before send....');
											},
								url:		'saveAddEditStudent.do',
								type:		'POST',
								data:		 param,
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
												var orgs;  
												var errorFlag = data.errorFlag;
												var successFlag = data.successFlag;
												if(successFlag) {
														modifyStudentId = data.studentId;
														formAccomationMap(data.studentId);
													$('#errorIcon').hide();
													$('#infoIcon').show();
													setMessageMain(data.title, data.content, data.type, "");
													document.getElementById('displayMessageMain').style.display = "block";
													if(String(assignedOrgNodeIds).indexOf(",") > 0) { 
														orgs = assignedOrgNodeIds.split(",");
													}
													else {
														orgs = [assignedOrgNodeIds];
													}
													if(orgs.length > 0 || orgs != null || orgs != "undefined") {
													 if(isExist(SelectedOrgNodeId,orgs)){
															assignedOrg = $("#" +SelectedOrgNodeId).text();
															showStudentInGrid = true;
														} else {
														if ($("#" +SelectedOrgNodeId).attr("cid") < leafNodeCategoryId) {
														var tempAssignedOrg = "";
																for(var i=0; i<orgs.length; i++){
																	if(leafNodePathMap[orgs[i]]){
																		var ancestorNodes = leafNodePathMap[orgs[i]].split(",");
																		for(var count = 0; count < ancestorNodes.length-1; count++) {
																  		 		var tmpNode = ancestorNodes[count];
																				if($.trim(tmpNode) == $.trim(SelectedOrgNodeId)){
																				 if(tempAssignedOrg == ""){
																				 	tempAssignedOrg =  tempAssignedOrg + leafNodeTextMap[orgs[i]] ;
																				 } else {
																				 	tempAssignedOrg =  tempAssignedOrg + "," + leafNodeTextMap[orgs[i]] ;
																				 }
																					showStudentInGrid = true;
																					break;
																				}
																		 }	
																	}
																}
															assignedOrg	= tempAssignedOrg;
															} else {
																showStudentInGrid = false;
															}
														}
													}													
													accomodationMap[modifyStudentId] = updateAccomodationMap[modifyStudentId];
																										
													if(showStudentInGrid) {
														
														var orgNodeNamesList ="";
														var orgNodeIDNameList ="";
														var elm = $("#selectedOrgNode a");
														for(var kk=elm.length ;kk>0; kk--){
															if(kk!=elm.length){
																orgNodeNamesList += "|";
															}
															orgNodeNamesList +=$(elm[kk-1]).text();
														}
														orgNodeIDNameList = prepareOrgNodeIdNameStr(assignedOrgNodeIds, orgNodeNamesList);
														
														var dataToBeAdded = {studentName:initCap($("#studentRegLastName").val())+','+ initCap($("#studentRegFirstName").val()),
																			grade:$("#gradeOptions").val(),
																			orgNodeNamesStr:$.trim(assignedOrg),
																			gender:$("#genderOptions").val(),
																			userName:data.studentLoginId,
																			studentNumber:$("#studentRegExtPin1").val(),
																			orgNodeIdList:assignedOrgNodeIds.replace(/,/g,"|"),
																			orgNodeNamesList:orgNodeNamesList,
																			orgIdNameList:orgNodeIDNameList
																			};
														
														var sortOrd = jQuery("#studentRegistrationGrid").getGridParam("sortorder");
														var sortCol = jQuery("#studentRegistrationGrid").getGridParam("sortname");	
														
														if(!isAddStudent) {
															jQuery("#studentRegistrationGrid").setRowData(data.studentId, dataToBeAdded, "first");
														}
														else {
															jQuery("#studentRegistrationGrid").addRowData(data.studentId, dataToBeAdded, "first");
														}
															jQuery("#studentRegistrationGrid").sortGrid(sortCol,true);
														
													} 
													assignedOrgNodeIds = "";
													closePopUp('studentAddEditDetail');
													$.unblockUI();			
        										}
        										else{
        											setMessage(data.title, data.content, data.type, "");
        											$('#errorIcon').show();
													$('#infoIcon').hide();
        											document.getElementById('displayMessage').style.display = "block";
        											if(profileEditable === "false") {
														disableAllNonEdFlds();
													} else {
														resetDisabledFields();
													}
        											$.unblockUI();
        										}
																								
											},
								error  :    function(XMLHttpRequest, textStatus, errorThrown){
													$.unblockUI();  
												window.location.href="/SessionWeb/logout.do";
											},
								complete :  function(){
												$.unblockUI();  
											}
						}
					);
	
	} else {
			document.getElementById('displayMessage').style.display = "block";
			if(profileEditable === "false") {
				disableAllNonEdFlds();
			} else {
				resetDisabledFields();
			}
		}
	}	
	
	function initCap(str) {
	 /* First letter as uppercase, rest lower */
	 var returnStr = $.trim(str);
	 if(str != null && str != ""){
	 	returnStr = returnStr.substring(0,1).toUpperCase() + returnStr.substring(1,returnStr.length);
	 }
	 return returnStr;
	} 

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
	
	var updateAccomodationMap = {};
	
	function formAccomationMap(studentId){
		updateAccomodationMap[studentId] = {};
		$(":checkbox","#Student_Accommodation_Info").each(
			function(){
				var key ="";
				if($(this).attr('name') != null && $(this).attr('name') != ""){
					key = $(this).attr('name').substring(0,1).toLowerCase() + $(this).attr('name').substring(1);
				}				
				if($(this).attr('checked')){
					updateAccomodationMap[studentId][key] = 'T';
				}else {
					updateAccomodationMap[studentId][key] = 'F';
				}
			}
		);
		
	}
	
	function disableAllNonEdFlds() {
		
		$('#Student_Info :checkbox').attr('disabled', true); 
		$('#Student_Info :radio').attr('disabled', true); 
		$('#Student_Info select').attr('disabled', true);
		$('#Student_Info :input').attr('disabled', true);
		var noRadioData = true;
		for(var count=0; count< stuDemographic.length; count++) {
		
		if(stuDemographic[count]['studentDemographicValues'].length == 1){
		     	var dynKey = stuDemographic[count]['labelName'] + "_" + stuDemographic[count]['studentDemographicValues'][0]['valueName'] ;
			     if(stuDemographic[count].importEditable == 'F' && profileEditable === "false") {
			     	$("#Student_Additional_Info :checkbox[name='" + dynKey+ "']").attr('disabled', true);
			     } else {
			     	$("#Student_Additional_Info :checkbox[name='" + dynKey+ "']").attr('disabled', false);
			     }
		     }else {
				var valueCardinality = stuDemographic[count]['valueCardinality'];
				if(valueCardinality == 'SINGLE'){
						if(profileEditable === "false" && stuDemographic[count].importEditable == 'F') {
			     			$("#"+stuDemographic[count]['labelName']).attr('disabled', true);
			     			var selectArray = $("#Student_Additional_Info select");
			     			for(var k = 0; k < selectArray.length; k++) {
			     				if($(selectArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(selectArray).eq(k).attr("disabled", true);
			     				} 
			     			}
			     			var radioArray = $("#Student_Additional_Info :radio");
			     			for(var k = 0; k < radioArray.length; k++) {
			     				if($(radioArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(radioArray).eq(k).attr("disabled", true);
			     				} 
			     			}
			     		} else {
			     			$("#"+stuDemographic[count]['labelName']).attr('disabled', false);
			     			var selectArray = $("#Student_Additional_Info select");
			     			for(var k = 0; k < selectArray.length; k++) {
			     				if($(selectArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(selectArray).eq(k).attr("disabled", false);
			     				} 
			     			}
			     			var radioArray = $("#Student_Additional_Info :radio");
			     			for(var k = 0; k < radioArray.length; k++) {
			     				if($(radioArray).eq(k).attr('id') == stuDemographic[count]['labelName']) {
			     					$(radioArray).eq(k).attr("disabled", false);
			     				} 
			     			}
			     		}
				}
				if(valueCardinality == 'MULTIPLE'){
					for(var innerCount = 0 ; innerCount < stuDemographic[count]['studentDemographicValues'].length; innerCount++){
			     		var dynKey = stuDemographic[count]['labelName'] + "_" + stuDemographic[count]['studentDemographicValues'][innerCount]['valueName'] ;
					     if(stuDemographic[count].importEditable == 'F' && profileEditable === "false"){
			     		$("#Student_Additional_Info :checkbox[name='" + dynKey+ "']").attr('disabled', true);
					     }else {
					     $("#Student_Additional_Info :checkbox[name='" + dynKey+ "']").attr('disabled', false);
					     }
			     	}
			    }
		   }
		}
	}
	
	function createMultiNodeSelectedTreeForStudent(jsondata) {
	
 	$("#orgInnerID").jstree({
	        "json_data" : {	             
	            "data" : rootNode,
				"progressive_render" : true,
				"progressive_unload" : true
				
	        },
	         "checkbox" : {
        "two_state" : true
        },  
	            "themes" : {
			    "theme" : "apple",
			    "dots" : false,
			    "icons" : false
			},       
	 			"plugins" : [ "themes", "json_data", "ui", "checkbox"]
	   });	   
	   	$("#orgInnerID").bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel != leafNodeCategoryId) {
						$("#orgInnerID ul li").eq(i).find('a').find('.jstree-checkbox:first').hide();
		    		} else {
		    			$("#orgInnerID ul li").eq(i).find('.jstree-icon').hide();
		    		}
		    		if(profileEditable === "false"  && $("#studentClassReassignable").val() === "true") {
		    			$("#orgInnerID ul li").eq(i).find('a').find('.jstree-checkbox:first').hide();
		    		}
				}
			}
		);
			
			registerDelegate("orgInnerID");
		
		$("#orgInnerID").delegate("li a","click", 
			function(e) {
				if(profileEditable === "false"  && $("#studentClassReassignable").val() === "true") {
					return true;
				}
				styleClass = $(this.parentNode).attr('class');
				var orgcategorylevel = $(this.parentNode).attr("cid");
				var elementId = $(this.parentNode).attr('id');
				var currentlySelectedNode ="";
				isexist = false;
				currentId = $(this.parentNode).attr("id");
				var element = this.parentNode;
    			if(orgcategorylevel == leafNodeCategoryId) {
    				if ($(element).hasClass("jstree-checked")){
						$(element).removeClass("jstree-checked").addClass("jstree-unchecked");
						checkedListObject[element.id] = "unchecked" ;
					}else{
						$(element).removeClass("jstree-unchecked").addClass("jstree-checked");
						checkedListObject[element.id] = "checked" ;
						}
				var isChecked = $(element).hasClass("jstree-checked");
				if(isChecked){
					var completePath = $("#orgInnerID").jstree("get_path",$("#"+elementId),true);
					if(completePath){
						leafNodePathMap[elementId] = completePath.toString();
						leafNodeTextMap[elementId] = trim($("#"+elementId).text());
					}
				}else {					
					delete leafNodePathMap[elementId];
					delete leafNodeTextMap[elementId];			
				}
				
    			updateOrganization(this.parentNode,isChecked);
    			}
			}
			);
			
			
			$("#orgInnerID").bind("change_state.jstree",
		  		function (e, d) { 
			  		if(isAction){
			    	var orgcategorylevel = d.rslt[0].getAttribute("cid");
			    	var elementId = d.rslt[0].getAttribute("id");
			    	var currentlySelectedNode="";
					var isChecked = $(d.rslt[0]).hasClass("jstree-checked");
					//console.log("isChecked" + isChecked);
					if (isChecked){
					checkedListObject[elementId] = "checked" ;
					}else{					
					checkedListObject[elementId] = "unchecked" ;
					}
					if(orgcategorylevel == leafNodeCategoryId) {
						if(isChecked){
							var completePath = $("#orgInnerID").jstree("get_path",$("#"+elementId),true);
							if(completePath){
								leafNodePathMap[elementId] = completePath.toString();
								leafNodeTextMap[elementId] = trim($("#"+elementId).text());
							}
						}else {					
							delete leafNodePathMap[elementId];
							delete leafNodeTextMap[elementId];
						}
						updateOrganization(d.rslt[0],isChecked);
					}
    			}
			}
			);
	}
	
	function updateOrganization(element, isChecked){
	  	if($(element).attr("cid") == leafNodeCategoryId){
	  		var currentlySelectedNode ="";
			isexist = false;
			currentId = $(element).attr("id");
			if(isAddStudent){
				var currentlySelectedNode ="";
				assignedOrgNodeIds = "";
				$("#orgInnerID").find(".jstree-checked").each(function(i, element){
					
					var orgcategorylevel = $(element).attr("cid");
						if(orgcategorylevel == leafNodeCategoryId) {
							if(currentlySelectedNode=="") {
								currentlySelectedNode += "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ trim($(element).text())+"</a>";	
							} else {
								currentlySelectedNode = currentlySelectedNode + " , " + "<a style='color: blue;text-decoration:underline' href=javascript:openTreeNodes('"+$(element).attr("id")+"');>"+ trim($(element).text())+"</a>";
							}
			
				    		if(assignedOrgNodeIds=="") {
								assignedOrgNodeIds = $(element).attr("id");
							} else {
								assignedOrgNodeIds = $(element).attr("id") +"," + assignedOrgNodeIds; 
							}
			    		}
					});
				}
			
				if(currentlySelectedNode.length > 0 ) {
					$("#notSelectedOrgNode").css("display","none");
					$("#selectedOrgNode").html(currentlySelectedNode);
					//$("#selectedOrgNodesName").text(currentlySelectedNode);	
				} else {
					$("#notSelectedOrgNode").css("display","inline");
					$("#selectedOrgNode").text("");	
				}
		
			}
		}

		function prepareOrgNodeIdNameStr(orgNodeIdStr, orgNodeNameStr){
			var orgNodeIdArray;
			var orgNodeNameArray;
			var orgNodeIdNameStr = "";
			if(orgNodeIdStr != "" && orgNodeIdStr != undefined)
				orgNodeIdArray = orgNodeIdStr.split(",");

			if(orgNodeNameStr != "" && orgNodeNameStr != undefined)
				orgNodeNameArray = orgNodeNameStr.split("|");
			
			if(orgNodeIdArray != null && orgNodeNameArray != null && (orgNodeIdArray.length == orgNodeNameArray.length)){
				for(i=0; i<orgNodeIdArray.length; i++){
					orgNodeIdNameStr += orgNodeIdArray[i]+'$'+orgNodeNameArray[i];
					if(i != (orgNodeIdArray.length - 1))
						orgNodeIdNameStr += "|";					
				}			
			}				
			
			return orgNodeIdNameStr;
		}