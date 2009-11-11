package com.ctb.lexington.util;

/*
 * DateUtils.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * <P>
 * This utility class will be used to encapsulate typical <code>Date</code>
 * manipulation such as <code>String</code> formatting.
 * </P>
 *
 * @author Giuseppe Gennaro
 */
public final class DateUtils
{
  /**
   * Used by {@link #getGMTString(Date)} method to create GMT date strings in
   * the format of "<code>yyyy-MM-dd HH:mm:ss z</code>".
   */
  public static final String GMT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss z";
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String YEAR_FORMAT = "yyyy";
  public static final String MONTH_FORMAT = "MM";
  public static final String DAY_FORMAT = "dd";

  public static final String DISPLAY_DATE_FORMAT = "MMM dd, yyyy hh:mm:ss a";
  public static final String DISPLAY_DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss a";

  /**
   * Used by {@link #formatTimeString(Date)} method to create <code>time string</code> in
   * the INTERNAL format of "<code>HHmmss</code>" used by interlayer communication.
   */
  public static final String TIME_FORMAT = "HHmmss";

  /**
   * Static variable to be the default timezone (GMT) for this application.
   */
  public static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");
  
  public static final String START_OF_DAY = "00:00:00";

  /**
   * Formats the specified <code>Date</code> into a <code>String</code> with
   * the format specified by the {@link #GMT_DATE_FORMAT} member variable.
   *
   * @param date  The <code>Date</code> to format.
   * @return      A <code>String</code> in the format of
   *              "<code>yyyy-MM-dd HH:mm:ss z</code>"
   */
  public static String getGMTString(Date date)
  { SimpleDateFormat sdf;

    sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance();
    sdf.setTimeZone(GMT_TIME_ZONE);
    sdf.applyPattern(GMT_DATE_FORMAT);

    return sdf.format(date);
  }

  public static Date getDateFromDisplayDateTimeFormat(String date_){
  	Date result = null;
  	SimpleDateFormat sdf = new SimpleDateFormat();
  	sdf.applyPattern(DISPLAY_DATE_TIME_FORMAT);
  	try{
  		result = sdf.parse(date_);
  	}
  	catch (Exception e){}
  	return result;
  }
  /**
   * Formats the specified <code>Date</code> into a <code>String</code> with
   * the format specified by the {@link #GMT_DATE_FORMAT} member variable.
   *
   * @param date  The <code>Date</code> to format.
   * @param timeZoneCode  The time zone to use.
   * @return      A <code>String</code> in the format of
   *              "<code>yyyy-MM-dd HH:mm:ss z</code>"
   */
  public static String getTZString(Date date, String timeZoneCode)
  { SimpleDateFormat sdf;

  sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance();
  sdf.setTimeZone(TimeZone.getTimeZone(timeZoneCode));
  sdf.applyPattern(DISPLAY_DATE_FORMAT);

  return sdf.format(date);
  }

  /**
   * Formats the specified <code>Date</code> into a <code>String</code> with
   * the format specified by the {@link #TIME_FORMAT} member variable.
   *
   * @param date_  The <code>Date</code> to format.
   * @return       A <code>String</code> in the format specified in TIME_FORMAT
   */
  public static String formatTimeString(Date date_)
  {
    SimpleDateFormat sdf;

    sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance();
    sdf.applyPattern(TIME_FORMAT);

    return sdf.format(date_);
  }

	public static Date getDate(String month_, String day_, String year_, String timeZone_){
		Date result = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone_));
			result = sdf.parse( year_ + "-" + month_ + "-" + day_ + " " + START_OF_DAY);
		}
		catch(Exception e){}
		return result;
	}
	
	public static boolean isValidDate(String month_, String day_, String year_){
		boolean result = false;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			Date dt = sdf.parse(year_ + "-" + month_ + "-" + day_);
			String parsedDate = sdf.format(dt);
			String year = parsedDate.substring(0, 4);
			String month = parsedDate.substring(5, 7);
			String day = parsedDate.substring(8, 10);
			if(month.equals(month_) && day.equals(day_) && year.equals(year_))
				result = true;
		}
		catch(Exception e){}
		return result;
	}
	
	public static String getCurrentYear(String timeZone_){
		SimpleDateFormat sdf = new SimpleDateFormat(YEAR_FORMAT);
		if(timeZone_ != null)
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone_));
		return sdf.format(new Date());
	}

	public static Date getStartOfDay(String sourceTimeZone_, String destinationTimeZone_, Date day_){
		Date result = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			sdf.setTimeZone(TimeZone.getTimeZone(sourceTimeZone_));
			String startOfDay = sdf.format(day_) + " " + START_OF_DAY;
			sdf.applyPattern(DATE_TIME_FORMAT);
			sdf.setTimeZone(TimeZone.getTimeZone(destinationTimeZone_));
			result = sdf.parse(startOfDay);
		}
		catch(Exception e){}
		return result;
	}
	
	public static Date getStartOfNextDay(String sourceTimeZone_, String destinationTimeZone_, Date day_){
		Date result = null;
		try{
			GregorianCalendar day = new GregorianCalendar();
			day.setTime(day_);
			day.add(Calendar.DATE, 1);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			sdf.setTimeZone(TimeZone.getTimeZone(sourceTimeZone_));
			String startOfDay = sdf.format(day.getTime()) + " " + START_OF_DAY;
			sdf.applyPattern(DATE_TIME_FORMAT);
			sdf.setTimeZone(TimeZone.getTimeZone(destinationTimeZone_));
			result = sdf.parse(startOfDay);
		}
		catch (ParseException e){}
		return result;
	}
	
	public static String getStartOfDayInGMT(String timeZone_, Date day_){
		return getGMTDateTime(getStartOfDay(timeZone_, timeZone_, day_));
	}
	
	public static String getStartOfNextDayInGMT(String timeZone_, Date day_){
		return getGMTDateTime(getStartOfNextDay(timeZone_, timeZone_, day_));
	}
	
	public static String getStartOfDayInGMT(String month_, String day_, String year_, String timeZone_){
		return getStartOfDayInGMT(timeZone_, getDate(month_, day_, year_, timeZone_));
	}

	public static String getStartOfNextDayInGMT(String month_, String day_, String year_, String timeZone_){
		return getStartOfNextDayInGMT(timeZone_, getDate(month_, day_, year_, timeZone_));
	}

	public static Date addTime(Date date, int days_, int months_){
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		if(days_ != 0){
			calendar.add(Calendar.DATE, days_);
		}
		if(months_ != 0){
			calendar.add(Calendar.MONTH, months_);
		}
		return calendar.getTime();
	}
	
	public static String getGMTDateTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		sdf.setTimeZone(GMT_TIME_ZONE);
		String result = sdf.format(date);
		return result;
	}
	
	public static boolean toDateBeforeFromDate(String fromMonth_, String fromDay_, String fromYear_, 
			                                   String toMonth_, String toDay_, String toYear_){
		Date toDate = getDate(toMonth_, toDay_, toYear_, GMT_TIME_ZONE.getID());
		Date fromDate = getDate(fromMonth_, fromDay_, fromYear_, GMT_TIME_ZONE.getID());
		return toDate.before(fromDate);
	}

	public static int compareDatesSimple(GregorianCalendar date1, GregorianCalendar date2) 
	{
		// uses only month, day, year fields for comparison;
		// date1 before date2	returns -1
		// date1 equals date2	returns 0
		// date1 after  date2	returns 1
		int month1 = date1.get(Calendar.MONTH);
		int day1   = date1.get(Calendar.DAY_OF_MONTH);
		int year1  = date1.get(Calendar.YEAR);
		int month2 = date2.get(Calendar.MONTH);
		int day2   = date2.get(Calendar.DAY_OF_MONTH);
		int year2  = date2.get(Calendar.YEAR);
		int comp = 0;
		if (year1 < year2)
			comp = -1;
		else if (year1 > year2)
			comp = 1;
		else
		{
			// same year
			if (month1 < month2)
				comp = -1;
			else if (month1 > month2)
				comp = 1;
			else
			{
				// same month
				if (day1 < day2)
					comp = -1;
				else if (day1 > day2)
					comp = 1;
			}
		}
		return comp;
	}
}