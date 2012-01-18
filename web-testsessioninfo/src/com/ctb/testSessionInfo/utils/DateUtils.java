package com.ctb.testSessionInfo.utils; 

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import org.apache.struts.action.ActionError;

public class DateUtils 
{ 
    public final static int DATE_VALID = 0;
    public final static int DATE_INVALID = 1;
    public final static int DATE_INVALID_MONTH = 2;
    public final static int DATE_INVALID_DAY = 3;
    
    
    private final static String TIME_FORMAT="hh:mm a";
    private final static String DATE_FORMAT="MM/dd/yy";
    private final static String DATETIME_FORMAT="MM/dd/yy hh:mm a";
    
    private static Hashtable timeZoneHashtable;
    
    static {
        timeZoneHashtable = new Hashtable();
        timeZoneHashtable.put("(GMT-10:00) Hawaii", "Pacific/Honolulu");
        timeZoneHashtable.put("(GMT-10:00) Aleutian Islands", "America/Adak");
        timeZoneHashtable.put("(GMT-09:00) Alaska", "America/Anchorage");
        timeZoneHashtable.put("(GMT-08:00) Pacific Time (US and Canada); Tijuana", "America/Los_Angeles");
        timeZoneHashtable.put("(GMT-07:00) Mountain Time (US and Canada)", "America/Denver");
        timeZoneHashtable.put("(GMT-07:00) Arizona", "America/Phoenix");
        timeZoneHashtable.put("(GMT-06:00) Central Time (US and Canada)", "America/Chicago");
        timeZoneHashtable.put("(GMT-05:00) Indiana (East)", "America/Indianapolis");
        timeZoneHashtable.put("(GMT-05:00) Eastern Time (US and Canada)", "America/New_York");
        timeZoneHashtable.put("(GMT) Greenwich Mean Time", "GMT");
    }
    
    public static Date getDateFromTimeString(String date){
        Date result = null;
        if (date == null)
            return result;
            
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(TIME_FORMAT);
        try{
            result = sdf.parse(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }    

    public static Date getDateFromDateString(String date){
        Date result = null;
        if (date == null)
            return result;

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(DATE_FORMAT);
        try{
            result = sdf.parse(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }    

    public static Date getDateFromDateTimeString(String date){
        Date result = null;
        if (date == null)
            return result;

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(DATETIME_FORMAT);
        try{
            result = sdf.parse(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }    


    public static String formatDateToDateString(Date date){
        String result = null;
        if (date == null)
            return result;

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(DATE_FORMAT);
        try{
            result = sdf.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }    

    public static String formatDateToTimeString(Date date){
        String result = null;
        if (date == null)
            return result;

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(TIME_FORMAT);
        try{
            result = sdf.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }    


    public static int validateDateString(String dateStr)
    {
        if (dateStr == null)
            return DATE_INVALID;
        
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(DATE_FORMAT);
        try{
            Date temp = sdf.parse(dateStr);
        }
        catch (Exception e){
            e.printStackTrace();
            return DATE_INVALID;
        }
        StringTokenizer tokenizer = new StringTokenizer(dateStr, "/");
        int i = 0;
        int month = 0;
        int day = 0;
        int year = 0;
        
        try {        
            while (tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken();
                int value = new Integer(token).intValue();
                if (i==0) {
                    if (value > 12 || value <= 0)
                        return DATE_INVALID_MONTH;
                    month=value;
                }
                else if (i==1) {
                    if (value > 31 || value <= 0)
                        return DATE_INVALID_DAY;
                    day = value;
                }
                else if (i==2) {
                    year = value;
                }
                i++;
            }
            year = 2000+year;
            if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day > 30)
                    return DATE_INVALID_DAY;
            }
            else if (month ==2) {
                if (isLeapYear(year) && day > 29)
                    return DATE_INVALID_DAY;
                else if (!isLeapYear(year) && day > 28)
                    return DATE_INVALID_DAY;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return DATE_INVALID;
        }
        
        return DATE_VALID;
    } 

	public static boolean isLeapYear(int year) {
		if (year%100==0) {
			if (year%400 == 0)
				return true;
			else
				return false;
		} 
		if (year%4==0)
			return true;
		else
			return false;
	}
    
    public static boolean isBeforeToday(Date date, String timezone) 
    {
        if (date == null)
            return false;
        Date today = new Date(System.currentTimeMillis());
        today = com.ctb.util.DateUtils.getAdjustedDate(today, TimeZone.getDefault().getID(), timezone, today);
        return com.ctb.util.DateUtils.dateBefore(date, today);    
    }   

    public static boolean isBeforeNow(Date date, String timezone) 
    {
        if (date == null)
            return false;
        Date today = new Date(System.currentTimeMillis());
        today = com.ctb.util.DateUtils.getAdjustedDate(today, TimeZone.getDefault().getID(), timezone, today);
        if (date.compareTo(today) < 0)
            return true;
        else
            return false;    
    }   


    public static List getTimeZoneList() 
    {
        List result = new ArrayList(); 
        result.add("(GMT-10:00) Hawaii");
        result.add("(GMT-10:00) Aleutian Islands");
        result.add("(GMT-09:00) Alaska");
        result.add("(GMT-08:00) Pacific Time (US and Canada); Tijuana");
        result.add("(GMT-07:00) Mountain Time (US and Canada)");
        result.add("(GMT-07:00) Arizona");
        result.add("(GMT-06:00) Central Time (US and Canada)");
        result.add("(GMT-05:00) Indiana (East)");
        result.add("(GMT-05:00) Eastern Time (US and Canada)");

        return result;
    }
    
    public static String getDBTimeZone(String UITimeZone) {
        String result;
        result = (String) timeZoneHashtable.get(UITimeZone);
        return result;
    }

    public static String getUITimeZone(String DBTimeZone) {
        String result=null;
        
        Enumeration enu = timeZoneHashtable.keys();
        boolean found = false;
        while (enu.hasMoreElements() && !found) {
            String key = (String) enu.nextElement();    
            String value = (String) timeZoneHashtable.get(key);
            if (value.equals(DBTimeZone)) {
                found = true;
                result = key;
            }
        }
        return result;
    }
    
    public static List getTimeList() {
        
        List result = new ArrayList(); 
        for (int i = 0; i < 96; i++ ) {
            StringBuffer buf = new StringBuffer();
            String hourStr = ""+((i/4%12==0)?12:(i/4%12));
            if (hourStr.length()==1)
                hourStr = "0"+hourStr;
            buf.append(hourStr).append(":");
            
            if (i%4 == 0) 
                buf.append("00");
            else if (i%4 == 1) 
                buf.append("15");                
            else if (i%4 == 2) 
                buf.append("30");                
            else if (i%4 == 3) 
                buf.append("45");                
            buf.append((i<48)?" AM":" PM");    
            
            result.add(buf.toString());        
        }
        return result;
    }

	public static boolean isToday(Date date, String timezone) {

        if (date == null)
            return false;
        Date today = new Date(System.currentTimeMillis());
        today = com.ctb.util.DateUtils.getAdjustedDate(today, TimeZone.getDefault().getID(), timezone, today);
        return com.ctb.util.DateUtils.dateEquals(date, today);  
		
	}

	public static boolean isAfterToday(Date date,	String timezone) {
		 if (date == null)
	            return false;
	        Date today = new Date(System.currentTimeMillis());
	        today = com.ctb.util.DateUtils.getAdjustedDate(today, TimeZone.getDefault().getID(), timezone, today);
	        return com.ctb.util.DateUtils.dateAfter(date, today); 
	}     
    
} 
