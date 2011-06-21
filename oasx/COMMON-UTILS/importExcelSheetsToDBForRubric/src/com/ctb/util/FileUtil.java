package com.ctb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileUtil {
	
	 public static Properties ReadProperty(String fileName){
		    
		 Properties prop = new Properties();
         System.out.println("Environment file path ["+fileName+"]");
		 try {
			 File f = new File(fileName);
		        if(f.exists()){
		          FileInputStream in = new FileInputStream(f);
		          prop.load(in);
		        }
		    } catch (IOException e) {
		    	System.err.println("Exception occurred while reading environment file.");
		    	e.printStackTrace();
		    }
		    return prop;  			
		  }
	 

}
