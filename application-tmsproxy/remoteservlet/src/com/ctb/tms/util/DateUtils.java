package com.ctb.tms.util; 

import java.util.Date;
import java.util.TimeZone;

public class DateUtils 
{ 
    static final long serialVersionUID = 1L;
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
        return result;
    }
    
    public static boolean dateAfter(Date date1, Date date2) {
        boolean result =    (date2.getYear() <  date1.getYear()) ||
                            (date2.getYear() == date1.getYear() && date2.getMonth() <  date1.getMonth()) ||
                            (date2.getYear() == date1.getYear() && date2.getMonth() == date1.getMonth() && date2.getDate() < date1.getDate());
        return result;
    }
    
    public static boolean dateEquals(Date date1, Date date2) {
        boolean result = date2.getYear() == date1.getYear() &&
                date2.getMonth() == date1.getMonth() &&
                date2.getDate() == date1.getDate();
        return result;
    }
    
    public static boolean timeBefore(Date date1, Date date2) {
        boolean result = timeInSeconds(date2) > timeInSeconds(date1);
        return result;
    }
    
    public static boolean timeAfter(Date date1, Date date2) {
        boolean result = timeInSeconds(date2) < timeInSeconds(date1);;
        return result;
    }
    
    public static boolean timeEquals(Date date1, Date date2) {
        boolean result = timeInSeconds(date2) == timeInSeconds(date1);;
        return result;
    }
} 
