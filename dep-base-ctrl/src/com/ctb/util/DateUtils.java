package com.ctb.util; 

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils 
{ 
    static final long serialVersionUID = 1L;
    
    private final static String DATE_FORMAT="MM/dd/yy";
    private final static String TIME_FORMAT="hh:mm a";
	/**
	 * Converts a date in the input timezone to a date in the output timezone.
	 * @param date
	 * @param inputTimezone
	 * @param outputTimezone
	 * @return Date
	 */
    public static Date getAdjustedDate(Date date, String inputTimezone, String outputTimezone, Date offsetDate) {
        
        TimeZone inputZone = TimeZone.getTimeZone(inputTimezone);
        TimeZone outputZone = TimeZone.getTimeZone(outputTimezone);
        
        int inputOffset = inputZone.getOffset(offsetDate.getTime());
        int outputOffset = outputZone.getOffset(offsetDate.getTime());
        
        long adjustedTime = (date.getTime() - inputOffset) + outputOffset;
        
        Date result = new Date(adjustedTime);
        
        OASLogger.getLogger("TestAdmin").debug("DateUtils: getAdjustedDate: converted date: " + date.getTime() + " to: " + result.getTime());
        return result;
    }
    
    public static long daysToMillis(int days) {
        return ((long)days) * 24 * 60 * 60 * 1000;
    }
    
    public static long hoursToMillis(int hours) {
        return ((long)hours) * 60 * 60 * 1000;
    }
    
    public static int timeInSeconds(Date date) {
        return (date.getHours() * 60 * 60) +
                (date.getMinutes() * 60) +
                  date.getSeconds(); 
    }
    
    public static boolean dateBefore(Date date1, Date date2) {
        boolean result =    (date2.getYear() >  date1.getYear()) ||
                            (date2.getYear() == date1.getYear() && date2.getMonth() >  date1.getMonth()) ||
                            (date2.getYear() == date1.getYear() && date2.getMonth() == date1.getMonth() && date2.getDate() > date1.getDate());
        OASLogger.getLogger("TestAdmin").debug("DateUtils: dateBefore: " + date1.toString() + " before " + date2.toString() + "? " + result);
        return result;
    }
    
    public static boolean dateAfter(Date date1, Date date2) {
        boolean result =    (date2.getYear() <  date1.getYear()) ||
                            (date2.getYear() == date1.getYear() && date2.getMonth() <  date1.getMonth()) ||
                            (date2.getYear() == date1.getYear() && date2.getMonth() == date1.getMonth() && date2.getDate() < date1.getDate());
        OASLogger.getLogger("TestAdmin").debug("DateUtils: dateAfter: " + date1.toString() + " after " + date2.toString() + "? " + result);
        return result;
    }
    
    public static boolean dateEquals(Date date1, Date date2) {
        boolean result = date2.getYear() == date1.getYear() &&
                date2.getMonth() == date1.getMonth() &&
                date2.getDate() == date1.getDate();
        OASLogger.getLogger("TestAdmin").debug("DateUtils: dateEquals: " + date1.toString() + " equals " + date2.toString() + "? " + result);
        return result;
    }
    
    public static boolean timeBefore(Date date1, Date date2) {
        boolean result = timeInSeconds(date2) > timeInSeconds(date1);
        OASLogger.getLogger("TestAdmin").debug("DateUtils: timeBefore: " + date1.toString() + " before " + date2.toString() + "? " + result);
        return result;
    }
    
    public static boolean timeAfter(Date date1, Date date2) {
        boolean result = timeInSeconds(date2) < timeInSeconds(date1);;
        OASLogger.getLogger("TestAdmin").debug("DateUtils: timeAfter: " + date1.toString() + " after " + date2.toString() + "? " + result);
        return result;
    }
    
    public static boolean timeEquals(Date date1, Date date2) {
        boolean result = timeInSeconds(date2) == timeInSeconds(date1);;
        OASLogger.getLogger("TestAdmin").debug("DateUtils: timeEquals: " + date1.toString() + " equals " + date2.toString() + "? " + result);
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
    
    public static String formatDateToDateString(Date date, String dateFormat){
        String result = null;
        if (date == null)
            return result;

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(dateFormat);
        try{
            result = sdf.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    public static String getAdjustedTodayString(String timeZone, String dateFormat) {
	Date now = new Date(System.currentTimeMillis());
	//System.out.println("Today: " + now.toString() + "TimeZone.getDefault().getID()" + TimeZone.getDefault().getDisplayName());
	Date today = getAdjustedDate(now, TimeZone.getDefault().getID(), timeZone, now);
	String todayString = formatDateToDateString(today, dateFormat);
	//System.out.println("Adjusted Today: " + todayString);
	return todayString;
    }
    
    public static String getAdjustedDateString(String dateStr, String userTimeZone, String dateFormat) throws ParseException{
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	Date date = format.parse(dateStr);
	//System.out.println("//Date: " + date);
	Date today = getAdjustedDate(date, TimeZone.getDefault().getID(), userTimeZone, date);
	String str = formatDateToDateString(today, dateFormat);
	//System.out.println("//Adjusted Date: " + str);
	return str;
    }
    
    public static String getAdjustedDateString(String dateStr, String dateStrFormat, String userTimeZone, String dateFormat) throws ParseException{
	DateFormat format = new SimpleDateFormat(dateStrFormat, Locale.ENGLISH);
	Date date = format.parse(dateStr);
	//System.out.println("//Date: " + date);
	Date today = getAdjustedDate(date, TimeZone.getDefault().getID(), userTimeZone, date);
	String str = formatDateToDateString(today, dateFormat);
	//System.out.println("//Adjusted Date: " + str);
	return str;
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

} 
