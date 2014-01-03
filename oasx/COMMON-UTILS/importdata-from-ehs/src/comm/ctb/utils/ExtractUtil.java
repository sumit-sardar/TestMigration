package comm.ctb.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class provides properties file handling utility functions.
 * 
 * @author TCS
 * 
 */
	public class ExtractUtil extends ResourceBundle {

		private static ResourceBundle rb;
		
		/*static {
			rb = ResourceBundle.getBundle("config");
		}*/
		
		public static void loadPropetiesFile(String fileName){
			rb = ResourceBundle.getBundle(fileName);
		}
		
		public static void loadExternalPropetiesFile(String baseName, String externalPropertiesFilePAth){
			File file = new File(externalPropertiesFilePAth);
			ClassLoader loader = null;
			try{
				//URL[] urls = {file.toURI().toURL()};// not compatible with jdk 1.3
				URL[] urls = {file.toURL()};
				loader = new URLClassLoader(urls);
			}catch (MalformedURLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			rb = ResourceBundle.getBundle(baseName, Locale.getDefault(), loader);
		}
		
		public static String getDetail(String key){
			return rb.getString(key);			
		}
				
		

		public Enumeration getKeys() {
			// TODO Auto-generated method stub
			return rb.getKeys();
		}



		protected Object handleGetObject(String key) {
			// TODO Auto-generated method stub
			return null;
		}			

	}

