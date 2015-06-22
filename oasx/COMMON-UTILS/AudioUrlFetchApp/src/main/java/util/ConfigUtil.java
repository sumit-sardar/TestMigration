package main.java.util;



import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ConfigUtil extends ResourceBundle{
	private static ResourceBundle rb;
	
	static {
		rb = ResourceBundle.getBundle("config_DB");
	}
	public static String getDetail(String key){
		try {
			return rb.getString(key);
		} catch (MissingResourceException mre) {
			System.out.println("Please provide valid \""+ key +"\" in config.properties file.");
			return null;
		}
	}
	
	@Override
	public Enumeration<String> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object handleGetObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
