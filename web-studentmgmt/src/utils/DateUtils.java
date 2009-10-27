package utils; 

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
    
    
    public final static String TIME_FORMAT =" hh:mm a";
    public final static String DATE_FORMAT = "MM/dd/yyyy";
    public final static String DATETIME_FORMAT = "MM/dd/yyyy hh:mm a";
    public final static String DATE_FORMAT_CHAR = "MMM/dd/yyyy";
    public final static String DATE_FORMAT_DISPLAY = "MMM dd, yyyy";
    
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
        TimeZone defaultTimeZone = TimeZone.getDefault();
        sdf.applyPattern(TIME_FORMAT);
        sdf.setTimeZone(defaultTimeZone);
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


    public static String formatDateToDateString(Date date, String pattern){
        String result = null;
        if (date == null)
            return result;

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(pattern);
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
        catch (Exception e) {
            e.printStackTrace();
            return DATE_INVALID;
        }
        
        return DATE_VALID;
    } 

    public static int validateDateValues(String year, String month, String day)
    {
        int i = 0;
        int y = Integer.parseInt(year) - 1900;
        int m = Integer.parseInt(monthStringToNumber(month)) - 1;
        int d = Integer.parseInt(day);
        
        Date date = null;
                
        try {
            date = new Date(y, m, d);
        }
        catch (Exception e) {
            e.printStackTrace();
            return DATE_INVALID;
        }
        
        int y2 = date.getYear();
        if (y2 != y)
            return DATE_INVALID;

        int m2 = date.getMonth();
        if (m2 != m)
            return DATE_INVALID;

        int d2 = date.getDate();
        if (d2 != d)
            return DATE_INVALID;
            
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
    
    /**
     * getMonthOptions
     */
    public static String [] getMonthOptions()
    {
        List options = new ArrayList();
        options.add("");
        
        options.add("Jan");
        options.add("Feb");
        options.add("Mar");
        options.add("Apr");
        options.add("May");
        options.add("Jun");
        options.add("Jul");
        options.add("Aug");
        options.add("Sep");
        options.add("Oct");
        options.add("Nov");
        options.add("Dec");
                
        return (String [])options.toArray(new String[0]);        
    }

    /**
     * getDayOptions
     */
    public static String [] getDayOptions()
    {
        List options = new ArrayList();
        options.add("");
        
        for (int i=1 ; i<=31 ; i++) {
            String day = String.valueOf(i);
            if (i < 10)
                day = "0" + day;
            options.add(day);
        }
                
        return (String [])options.toArray(new String[0]);        
    }

    /**
     * getYearOptions
     */
    public static String [] getYearOptions()
    {
        List options = new ArrayList();
        options.add("");
        
        Date systemDate = new Date();
        int systemYear = systemDate.getYear() + 1900;
        
        int startYear = systemYear - 101;
        int endYear = systemYear - 1;
        
        for (int i=endYear ; i>=startYear ; i--) {
            options.add(String.valueOf(i));
        }
                
        return (String [])options.toArray(new String[0]);        
    }

    public static boolean allSelected(String month, String day, String year)
    {
        if (month.equals("") || day.equals("") || year.equals(""))
            return false;
        else
            return true;    
    }
        
    public static boolean verifyDate(String month, String day, String year)
    {
        return true;    // for now
    }
    
    public static String monthStringToNumber(String month)
    {
        if (month.equals("Jan")) return "01";
        if (month.equals("Feb")) return "02";
        if (month.equals("Mar")) return "03";
        if (month.equals("Apr")) return "04";
        if (month.equals("May")) return "05";
        if (month.equals("Jun")) return "06";
        if (month.equals("Jul")) return "07";
        if (month.equals("Aug")) return "08";
        if (month.equals("Sep")) return "09";
        if (month.equals("Oct")) return "10";
        if (month.equals("Nov")) return "11";
        if (month.equals("Dec")) return "12";
        return "";        
    }
    
    public static Date createDate(String year, String month, String day)
    {
        int y = Integer.parseInt(year) - 1900;
        int m = Integer.parseInt(monthStringToNumber(month)) - 1;
        int d = Integer.parseInt(day);
        
        Date date = new Date(y, m, d);
        return date;
    }
} 
