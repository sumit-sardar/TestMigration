package main.java.util;



import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class AWSResourceUtil extends ResourceBundle{
	private static ResourceBundle awsRB;
	
	static {
		awsRB = ResourceBundle.getBundle("AWSConfig");
	}
	public static String getAWSDetail(String key){
		try {
			return awsRB.getString(key);
		} catch (MissingResourceException mre) {
			System.out.println("Please provide valid \""+ key +"\" in AWSConfig.properties file.");
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
