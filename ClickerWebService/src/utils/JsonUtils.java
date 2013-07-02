package utils; 

import java.security.MessageDigest;

//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;



public class JsonUtils {
	/*
	public static String getJsonString (Object _object, String alias, Class<?> _class) throws Exception{
		
		XStream jsondriver = new XStream(new JettisonMappedXmlDriver());
		jsondriver.alias(alias, _class);
		jsondriver.alias("entry", String.class);
		return jsondriver.toXML(_object);
		
	}
*/
	public static String encodePassword(String password) {
        MessageDigest md;
        StringBuffer retval = new StringBuffer("");
        byte[] hash = new byte[] {};
        try {
              md = MessageDigest.getInstance("MD5");
              md.update(password.getBytes());
              hash = md.digest();
        } catch (Exception e) {
              e.printStackTrace();
        }
        for (int i = 0; i < hash.length; ++i) {
              if (((int) hash[i] & 0xff) < 0x10) {
                    retval.append("0");
              }
              retval.append(Long.toString((int) hash[i] & 0xff, 16));

        }

        return retval.toString();
	}

	public static Integer newInteger(String valueStr) {
    	Integer valueInt = new Integer(0);
    	try {
        	valueInt = new Integer(valueStr);
    	}
      	catch (Exception e) {
        	valueInt = new Integer(0);
      		
      	}
		return valueInt;
    }
	
	public static String safeGuardString(String value) 
	{
		if (value == null)
			value = "";
		return value.trim();
	}
	
	public static String safeGuardInteger(String value) 
	{
		if (value == null)
			value = "0";
		return value.trim();
	}
	
}
