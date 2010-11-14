package utils; 

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.Message;
import dto.StudentContactInformation;


public class WebUtils 
{ 
	public static String buildURI(HttpServletRequest req, String flow, String controller)
	{
		StringBuffer uri = new StringBuffer(req.getContextPath());
		uri.append("/" + flow);
		uri.append("/" + controller);
		return uri.toString();
	}

	public static void writeResponseContent(HttpServletResponse res, String content)
	{
		res.setContentType("text/xml");
		res.setHeader("Cache-Control", "no-cache");

		try {
			res.getWriter().write(content);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean requestHasInvalidParameters(String str)
	{

		String[] invalidChars = { "<script>", "javascript:"};

		for( int i=0; i<invalidChars.length; i++ )
		{
			if ( str.indexOf(invalidChars[i]) != -1 )
			{
				// Invalid character found in this parameter, set to invalid
				return true;                
			}
		}

		return false;
	}



	public static boolean invalidCharacter(char ch)
	{
		boolean invalid = false;
		if (ch == '!' ||
				ch == '@' ||
				ch == '#' ||
				ch == '$' ||
				ch == '%' ||
				ch == '^' ||
				ch == '&' ||
				ch == ')' ||
				ch == '(' ||
				ch == '+') {
			invalid = true; 
		}
		return invalid;
	}

	public static boolean validNameCharacter(char ch)
	{
		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		boolean validChar = ((ch == '/') || 
				(ch == '\'') || 
				(ch == '-') || 
				(ch == '\\') || 
				(ch == '.') || 
				(ch == '(') || 
				(ch == ')') || 
				(ch == '&') || 
				(ch == '+') || 
				(ch == ',') || 
				(ch == ' '));

		return (zero_nine || A_Z || a_z || validChar);
	}

	public static boolean validTextCharacter(char ch)
	{
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean validChar = (ch == ' ');

		return (zero_nine || A_Z || a_z || validChar);
	}

	public static boolean validString(String str)
	{
		char[] characters = str.toCharArray();
		for (int i=0 ; i<characters.length ; i++) {
			char character = characters[i];
			if (invalidCharacter(character))
				return false;
		}

		return !requestHasInvalidParameters(str);
	}

	public static boolean validNameString(String str)
	{
		char[] characters = str.toCharArray();
		for (int i=0 ; i<characters.length ; i++) {
			char character = characters[i];
			if (! validNameCharacter(character))
				return false;
		}
		return !requestHasInvalidParameters(str);
	}

	public static boolean validTextString(String str)
	{
		char[] characters = str.toCharArray();
		for (int i=0 ; i<characters.length ; i++) {
			char character = characters[i];
			if (! validTextCharacter(character))
				return false;
		}
		return !requestHasInvalidParameters(str);
	}

	public static String verifyCreateStudentName(String firstName, String lastName, String middleName)
	{
		String invalidCharFields = "";
		int invalidCharFieldCount = 0;

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
	//Changes for CA-ABE
	public static String verifyABECreateStudentInstructorName(String firstName, String lastName, String middleName, String instructorFirstName, String instructorLastName)
	{
		String invalidCharFields = "";
		int invalidCharFieldCount = 0;

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
		if (! validNameString(instructorFirstName) ) {
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString("Instructor First Name", invalidCharFieldCount, invalidCharFields);       
		}
		if (! validNameString(instructorLastName) ) {
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString("Instructor Last Name", invalidCharFieldCount, invalidCharFields);       
		}

		return invalidCharFields;
	}

	public static String verifyABEValidStudentContact(StudentContactInformation studentContact){
		String addressLine1 = studentContact.getAddressLine1();
		String addressLine2 = studentContact.getAddressLine2();
		String city = studentContact.getCity();
		String email =studentContact.getEmail();
		
		
		
		String invalidCharFields = "";
		int invalidCharFieldCount = 0;
		if (! validAddressString(addressLine1) ) {
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString("First Name", invalidCharFieldCount, invalidCharFields);       
		}
		if (! validAddressString(addressLine2) ) {
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString("Last Name", invalidCharFieldCount, invalidCharFields);       
		}
		if (! validNameString(city) ) {
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString("city", invalidCharFieldCount, invalidCharFields);       
		}
		
		if (! validEmail(email)) {
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString("email", invalidCharFieldCount, invalidCharFields);       
		}
		
		if(invalidPrimaryPhoneLength(studentContact)){
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString("Primary Phone", invalidCharFieldCount, invalidCharFields);       
		
		}
		if(invalidSecondaryPhoneLength(studentContact)){
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString("Secondary Phone", invalidCharFieldCount, invalidCharFields);       
		}
		if( invalidZipLength(studentContact)){
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString("Zip Code", invalidCharFieldCount, invalidCharFields);       
		}
			
		return invalidCharFields;

	}

	public static String verifyCreateStudentNumber(String studentNumber, String studentSecondNumber, String studentIdLabelName, String studentId2LabelName)
	{
		String invalidCharFields = "";
		int invalidCharFieldCount = 0;

		if (studentNumber != null) {
			if (! validTextString(studentNumber) ) {
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(studentIdLabelName, invalidCharFieldCount, invalidCharFields);       //Changed for GA2011CR001
			}
		}
		if (studentSecondNumber != null) {
			if (! validTextString(studentSecondNumber) ) {
				invalidCharFieldCount += 1;            
				invalidCharFields = buildErrorString(studentId2LabelName, invalidCharFieldCount, invalidCharFields);      //Changed for GA2011CR001 
			}
		}

		return invalidCharFields;
	}

	public static String verifyFindStudentInfo(String firstName, String lastName, String middleName,
			String studentNumber, String loginId, String studentIdLabelName)
	{
		String invalidCharFields = "";
		int invalidCharFieldCount = 0;

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

		if (! validString(studentNumber) ) {
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString(studentIdLabelName, invalidCharFieldCount, invalidCharFields);      //Changed for GA2011CR001 
		}

		if (! validString(loginId) ) {
			invalidCharFieldCount += 1;            
			invalidCharFields = buildErrorString("Login ID", invalidCharFieldCount, invalidCharFields);       
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

	//Changes for CA-ABE
	public static boolean validEmail(String email)
	{        
		email = email.trim();
		boolean matchFound = true;
		if (email != null && email.length()>0) {
			//Pattern p = Pattern.compile(".+@.+\\.[a-z]+"); 
			Pattern p = Pattern.compile("[a-z|A-Z|0-9|_|\\-|.]+@[a-z|A-Z|0-9|_|\\-|.]+\\.[a-z|A-Z|0-9|_|\\-|.]+");                  
			//Match the given string with the pattern
			Matcher m = p.matcher(email);
			//check whether match is found 
			matchFound = m.matches();
		}        
		return matchFound;        
	}
	//Changes for CA-ABE
	public static boolean validAddressString(String str)
	{
		str = str.trim();
		char[] characters = str.toCharArray();
		for (int i=0 ; i<characters.length ; i++) {
			char character = characters[i];
			if (! validAddressCharacter(character) )
				return false;
		}
		return !requestHasInvalidParameters(str);
	}
	//Changes for CA-ABE
	public static boolean validAddressCharacter(char ch)
	{
		boolean valid = validNameCharacter(ch);
		if (ch == '#')
			valid = true; 

		return valid;
	}
	//Changes for CA-ABE
	public static boolean validNumber(String str)
	{
		str = str.trim();
		char[] characters = str.toCharArray();

		for (int i=0 ; i<characters.length ; i++) {
			char character = characters[i];
			if (!((character >= 48) && (character <= 57))) {
				return false;
			}
		} 
		return true;
	}
	
	//Changes for CA-ABE
    public static boolean invalidPrimaryPhoneLength(StudentContactInformation studentContact) 
    {         
       if (studentContact.getPrimaryPhone1().trim().length() > 0 ||
    		   studentContact.getPrimaryPhone2().trim().length() > 0 ||
    		   studentContact.getPrimaryPhone3().trim().length() > 0) {
                       
           if (studentContact.getPrimaryPhone1().trim().length() < 3 ||
        		   studentContact.getPrimaryPhone2().trim().length() < 3 ||
        		   studentContact.getPrimaryPhone3().trim().length() < 4) { 
               return true;
           }
       }

       if (studentContact.getPrimaryPhone1().trim().length() == 0 && 
    		   studentContact.getPrimaryPhone2().trim().length() == 0 && 
    		   studentContact.getPrimaryPhone3().trim().length() == 0 && 
    		   studentContact.getPrimaryPhone4().trim().length() > 0) { 
           return true;
       }
       
       return false;
    }
    
    public static boolean invalidSecondaryPhoneLength(StudentContactInformation studentContact) 
    {         
       if (studentContact.getSecondaryPhone1().trim().length() > 0 ||
    		   studentContact.getSecondaryPhone2().trim().length() > 0 ||
    		   studentContact.getSecondaryPhone3().trim().length() > 0) {
                           
               if (studentContact.getSecondaryPhone1().trim().length() < 3 ||
            		   studentContact.getSecondaryPhone2().trim().length() < 3 ||
            		   studentContact.getSecondaryPhone3().trim().length() < 4) { 
               return true;
           }
       }

       if (studentContact.getSecondaryPhone1().trim().length() == 0 && 
    		   studentContact.getSecondaryPhone2().trim().length() == 0 && 
    		   studentContact.getSecondaryPhone3().trim().length() == 0 && 
    		   studentContact.getSecondaryPhone4().trim().length() > 0) { 
           return true;
       }
       
       return false;
    }
    
    public static boolean invalidZipLength(StudentContactInformation studentContact) 
    {     
        if (studentContact.getZipCode1().trim().length() > 0 
            || studentContact.getZipCode2().trim().length() > 0) {
                
            if(studentContact.getZipCode1().trim().length() < 5 ) {
                                  
                return true;
            }
            
        }
        return false;
    }
    
  //Changes for CA-ABE
	public static String validProviderUse(String str)
	{
		str = str.trim();
		char[] characters = str.toCharArray();
		String invalidCharFields = "";
		int invalidCharFieldCount = 0;
		for (int i=0 ; i<characters.length ; i++) {
			char character = characters[i];
			if (! validAlphaNumericCharacter(character) )
				invalidCharFieldCount += 1;            
			   
		}
		if (invalidCharFieldCount > 0) {
			invalidCharFields = buildErrorString(Message.FIELD_PROVIDER_USE, invalidCharFieldCount, invalidCharFields);
		}
		
		return invalidCharFields;
	}
	
    public static boolean validAlphaNumericCharacter(char ch)
	{
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		

		return (zero_nine || A_Z || a_z );
	}
	

} 
