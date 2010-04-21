package utils;


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

		// check for required fields
		String requiredFields = "";
		int requiredFieldCount = 0;

		if(form.getSelectedTab().equals("moduleStudentTestSession")) {

			String studentLoginId = form.getStudentProfile().getStudentLoginId().trim();
			if ( studentLoginId.length() == 0 ) {

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

				requiredFields += ("<br/>" + Message.REQUIRED_TEXT);
				form.setMessage(Message.MISSING_REQUIRED_FIELD, requiredFields, Message.ERROR);
			}
			else {

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

		String ticketId = null;
		String serviceRequestor = null;
		String requestDescription = null;

		if(form.getSelectedTab().equals("moduleStudentTestSession") && form.getCurrentAction().equals("applySearch")) { 
			studentLoginId = form.getStudentProfile()!= null && form.getStudentProfile().getStudentLoginId() != null ?  form.getStudentProfile().getStudentLoginId().trim() : null;
			testAccessCode =  form.getTestAccessCode() != null? form.getTestAccessCode().trim() : null;

			if (studentLoginId != null && ! WebUtils.validNameString(studentLoginId)) {
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(Message.STUDENT_LOGIN_ID, invalidCharFieldCount, invalidCharFields);       
			}

			if (testAccessCode != null && ! WebUtils.validNameString(testAccessCode)) {
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(Message.STUDENT_TEST_ACCESS_CODE, invalidCharFieldCount, invalidCharFields);       
			}

		}

		if(form.getSelectedTab().equals("moduleTestSession") && form.getCurrentAction().equals("applySearch")) {

			testAccessCode = form.getTestAccessCode()!=null ? form.getTestAccessCode().trim() : null;
			if (testAccessCode != null && ! WebUtils.validNameString(testAccessCode)) {
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(Message.STUDENT_TEST_ACCESS_CODE, invalidCharFieldCount, invalidCharFields);       
			}
		}

		/*if(form.getCurrentAction().equals("reOpenSubtest") || form.getCurrentAction().equals("reOpenSubtestForStudents")) {

			ticketId = form.getTicketId() != null ? form.getTicketId().trim() : null;
			serviceRequestor = form.getServiceRequestor() != null ? form.getServiceRequestor().trim() : null;
			requestDescription = form.getRequestDescription() != null ? form.getRequestDescription().trim() : null;

			if (ticketId != null  && ! WebUtils.validNameString(ticketId) ) {
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(Message.TEST_TICKET_ID, invalidCharFieldCount, invalidCharFields);       
			}

			if (serviceRequestor != null && ! WebUtils.validNameString(serviceRequestor) ) {
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(Message.TEST_SERVICE_REQUESTOR, invalidCharFieldCount, invalidCharFields);       
			}

			if (requestDescription != null && ! WebUtils.validNameString(requestDescription) ) {
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(Message.TEST_REQUEST_DESCRIPTION, invalidCharFieldCount, invalidCharFields);       
			}
		}*/

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
