package utils;

import java.util.List;

import manageCustomerService.CustomerServiceManagementController.CustomerServiceManagementForm;
import dto.Message;

public class CustomerServiceFormUtils {


	public static boolean verifyFormInformation(CustomerServiceManagementForm form)	    
	{                    

		if ( isRequiredfieldMissing(form) ) {

			return false;

		}

		if (isInvalidFormInfo(form)){

			return false;

		}           									   

		return true;
	}

	public static boolean isRequiredfieldMissing(CustomerServiceManagementForm form )
	{
		System.out.println("isRequiredfieldMissing1.."+form.getStudentProfile().getStudentLoginId());
		System.out.println("isRequiredfieldMissing2.."+form.getSelectedTab());

		// check for required fields
		String requiredFields = "";
		int requiredFieldCount = 0;

		if(form.getSelectedTab().equals("moduleStudentTestSession")) {

			String studentLoginId = form.getStudentProfile().getStudentLoginId().trim();
			if ( studentLoginId.length() == 0 ) {
				System.out.println("isRequiredfieldMissing3..");
				requiredFieldCount += 1;    
				form.setSelectedStudentLoginId("");       
				requiredFields = Message.buildErrorString(Message.STUDENT_LOGIN_ID, requiredFieldCount, requiredFields);       

			}
		}
		else if(form.getSelectedTab().equals("moduleTestSession")) { 

			String testAccessCode = form.getTestAccessCode();
			if ( "".equals(testAccessCode) || testAccessCode.length() == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = Message.buildErrorString(Message.STUDENT_TEST_ACCESS_CODE, requiredFieldCount, requiredFields);       
			}
		}
		if (requiredFieldCount > 0) {
			if (requiredFieldCount == 1) {

				System.out.println("isRequiredfieldMissing4..");
				requiredFields += ("<br/>" + Message.REQUIRED_TEXT);
				form.setMessage(Message.MISSING_REQUIRED_FIELD, requiredFields, Message.ERROR);

			}
			else {
				System.out.println("isRequiredfieldMissing5..");
				requiredFields += ("<br/>" + Message.REQUIRED_TEXT_MULTIPLE);
				form.setMessage(Message.MISSING_REQUIRED_FIELDS, requiredFields, Message.ERROR);

			}
			return true;
		}
		return false;
	}

	public static boolean isInvalidFormInfo(CustomerServiceManagementForm form)
	{

		String invalidCharFields = verifyTestSessionInfo(form);
		String invalidString = "";                        
		if ( invalidCharFields.length() > 0 ) {

			invalidString = invalidCharFields + ("<br/>" + Message.INVALID_NAME_CHARS_ORG);

		}																									   

		if ( invalidString != null && invalidString.length() > 0 ) {    

			form.setMessage(Message.INVALID_FORMAT_TITLE, invalidString, Message.ERROR);
			return true;

		} 
		return false;   
	}

	public static String verifyTestSessionInfo(CustomerServiceManagementForm form) {

		String invalidCharFields = "";
		int invalidCharFieldCount = 0;
		String studentLoginId = null;
		String testAccessCode = null;

		
		if(form.getSelectedTab().equals("moduleStudentTestSession")) { 
			studentLoginId = form.getStudentProfile()!=null && form.getStudentProfile().getStudentLoginId() != null ?  form.getStudentProfile().getStudentLoginId().trim() : null;
			testAccessCode =  form.getTestAccessCode() != null? form.getTestAccessCode().trim() : null;

			if (! WebUtils.validString(studentLoginId) && studentLoginId != null ) {
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(Message.STUDENT_LOGIN_ID, invalidCharFieldCount, invalidCharFields);       
			}


			if (! WebUtils.validString(testAccessCode) && testAccessCode != null ) {
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(Message.STUDENT_TEST_ACCESS_CODE, invalidCharFieldCount, invalidCharFields);       
			}
		}
	

	if(form.getSelectedTab().equals("moduleTestSession")) { 
		testAccessCode = form.getTestAccessCode()!=null ? form.getTestAccessCode().trim() : null;
		if (! WebUtils.validString(testAccessCode) && testAccessCode != null ) {
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString(Message.STUDENT_TEST_ACCESS_CODE, invalidCharFieldCount, invalidCharFields);       
		}
	}


	return invalidCharFields;
}

public static String buildErrorString(String field, int count, String str)
{
	String result = str;
	if (count == 1) {
		result += field;
	}
	else {
		result += (", " + field);            
	}        
	return result;
}

}
