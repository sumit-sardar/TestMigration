package com.ctb.lexington.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TimeUtils {
	public static String getCurrentTime() {
		return "["+(new SimpleDateFormat("MM-dd-yyyy HH:mm:ss:SSS")).format(Calendar.getInstance().getTime())+"] :";
	}
	
}
