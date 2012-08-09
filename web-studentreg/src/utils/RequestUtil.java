package utils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil implements RequestParam{

	public static final int RETURN_TYPE_INT = 0;
	public static final int RETURN_TYPE_STRING = 0;
	


	public static String getValueFromRequest(HttpServletRequest request,
			String name, boolean isRequiredDefault, String defaultValue) {
		String val = request.getParameter(name);
		if (val == null && isRequiredDefault) {
			val = defaultValue;
		} 
		
		return val;

	}

	public static String[] getValuesFromRequest(HttpServletRequest request,
			String name,  boolean isRequiredDefault, String[] defaultValue) {
		String[] val = request.getParameterValues(name);
		if(val == null && isRequiredDefault){
			val = defaultValue;
		}
     return val;
	}
	
	public static Object getTypedValueFromRequest(HttpServletRequest request,
			int returnTypeInt, String name) {
		String val = request.getParameter(name);
		Object retVal = null;
		if (returnTypeInt == RETURN_TYPE_INT) {
			try {
				retVal = Integer.valueOf(val);
			} catch (NumberFormatException ne) {

			}
		} else if (returnTypeInt == RETURN_TYPE_STRING) {
			retVal = val;
		}
		return retVal;
	}

}
