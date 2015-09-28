package com.ctb.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class EmetricUtil {
	private static final DateFormat birthDateFormat = new SimpleDateFormat(
	"yyyy-MM-dd");

	public static String calculateChronologicalAge(Date birthDate) {
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(birthDate);
		Calendar endCalendar = new GregorianCalendar();
		Date today = new Date();

		endCalendar.setTime(today);

		int diffYear = endCalendar.get(Calendar.YEAR)
				- startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH)
				- startCalendar.get(Calendar.MONTH);

		return String.valueOf(diffMonth);
	}
	
	public static String calculateAge(Date birthDate) {
		Integer totalMonths = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(birthDate);
		String currentDate = birthDateFormat.format(new Date());
		//System.out.println("Age:sb :   " + sb.toString());
		int dd1 = Integer.parseInt(sb.toString().substring(8, 10));
		int mm1 = Integer.parseInt(sb.toString().substring(5, 7));
		int yy1 = Integer.parseInt(sb.toString().substring(0, 4));

		int dd2 = Integer.parseInt(currentDate.substring(8, 10));
		int mm2 = Integer.parseInt(currentDate.substring(5, 7));
		int yy2 = Integer.parseInt(currentDate.substring(0, 4));
		int year = 0;
		int mm3 = 0;
		if (mm1 > mm2) {
			year = yy2 - yy1 - 1;
			mm3 = 12 + mm2 - mm1;
		} else if (mm1 < mm2) {
			year = yy2 - yy1;
			mm3 = mm2 - mm1;
		} else if (mm1 == mm2 && dd2 > dd1) {
			year = yy2 - yy1;
		} else if (mm1 == mm2 && dd2 < dd1) {
			year = yy2 - yy1 - 1;
		}
		totalMonths = (year * 12) + mm3;

		return totalMonths.toString();
	}

	public static String convertPhoneNumber(String phoneNumber) {

		String[] phoneSplit = null;
		if (phoneNumber != null) {
			phoneNumber = phoneNumber.replaceAll("\\W", "");
			if (phoneNumber.length() > 10) {
				phoneSplit = phoneNumber.split("x");
			} else {
				phoneNumber = String.format("%03d-%03d-%04d Ext:", new Integer(
						phoneNumber.substring(0, 3)), new Integer(phoneNumber
								.substring(3, 6)), new Integer(phoneNumber.substring(6,
										phoneNumber.length())));
				return phoneNumber;

			}
			if (phoneSplit.length > 0) {
				phoneNumber = String.format("%03d-%03d-%04d Ext:%04d",
						new Integer(phoneSplit[0].substring(0, 3)),
						new Integer(phoneSplit[0].substring(3, 6)),
						new Integer(phoneSplit[0].substring(6, phoneSplit[0]
								.length())), new Integer(phoneSplit[1]));
				return phoneNumber;
			} else {
				phoneNumber = String.format("%03d-%03d-%04d Ext:", new Integer(
						phoneNumber.substring(0, 3)), new Integer(phoneNumber
						.substring(3, 6)), new Integer(phoneNumber.substring(6,
						phoneNumber.length())));
				return phoneNumber;
			}
		} else {
			phoneNumber = "";
		}

		return phoneNumber;

	}

	public static String rightJustify(String text, int length) {
		if (text.length() >= length) {
			return text;
		}
		int blankCount = length - text.length();
		StringBuffer buf = new StringBuffer(length);
		for (int i = 0; i < blankCount; i++) {
			buf.append(' ');
		}
		buf.append(text);
		return buf.toString();
	}

	public static String leftJustify(String text, int length) {
		if (text.length() >= length) {
			return text;
		}
		int blankCount = length - text.length();
		StringBuffer buf = new StringBuffer(length);
		buf.append(text);
		for (int i = 0; i < blankCount; i++) {
			buf.append(' ');
		}
		return buf.toString();
	}

	public static String padRight(String s, int n) {
		if (s != null) {

			return String.format("%1$-" + n + "s", s);
		} else {

			return "";
		}
	}
	public static String truncate(String value, int length)
	{
	  if (value != null && value.length() > length){
	    value = value.substring(0, length);
	  }
	  else {
		  return value;
	  }
	  return value;
	}
	
	public static String formatGrade(String grade){
		
		if (grade.startsWith("K")) {
			
			return "KG";
		
		}else{
			grade = String.format("%2s", grade).replace(' ',
			'0');
		}
		return grade;
	}
	
	public static String getFormatedString(String val, int len){
		if(val != null && val.trim().length() !=0){
			val = String.format("%"+len+"s", val).replace(" ", "0");
			return val;
		}else{
			val = "";
			val = String.format("%"+len+"s", val);
			return val;
		}

		
	}
	public static String getFormatedStringScaleScore(String val, int len){
		if(val != null && val.trim().length() !=0){
			val = String.format("%"+len+"s", val).replace(" ", "0");
			return val;
		}else{
			val = "";
			val = String.format("%"+len+"s", val);
			return val;
		}

		
	}
	
	public static String getFormatedNumberCorrectString(String val, int len){
		if(val != null && val.trim().length()!=0){
			val = String.format("%"+len+"s", val).replace(" ", "0");
			return val;
		}else{
			val = "";
			val = String.format("%"+len+"s", val);
			return val;
		}
	}
	
	public static String getFormatedStringProficiency(String val, int len){
		if(val != null){
			val = String.format("%"+len+"s", val);
			return val;
		}else {
			val = "";
			val = String.format("%"+len+"s", val);
			return val;
		}

	}
	
	public static String getNumberFormatedString(String val) {
		if (val != null && (val.equalsIgnoreCase("XXXXX") || val.equalsIgnoreCase("N/A"))){
			//do nothing
		}else  if (val != null && val.trim().length() != 0) {
			DecimalFormat df = new DecimalFormat("##0.0");
			try {
				val = df.format(Double.parseDouble(val.trim()));
			} catch (NumberFormatException e) {
				val = "0.0";
			}
		} else {
			val = "";
		}

		val = String.format("%5s", val);
		return val;

	}
	
	public static String getFormatedStringwithBlankValue(String val, int len){
		if(val == null){
			val = "";
		}
		val = String.format("%"+len+"s", val);
		return val;

	}
	
	/**
	 * Time zone conversion 
	 * @param Val
	 * @param Timezon
	 * @param dateType
	 * @return
	 */
	public static String getTimeZone(String Val, String Timezon,
			boolean dateType) {

		String newDate = null;
		try {
			String str_date = Val + " " + "GMT";
			DateFormat formatter;
			Date date;
			formatter = new SimpleDateFormat("MMddyyyy HH:mm:ss zzz");
			date = (Date) formatter.parse(str_date);
			TimeZone tz = TimeZone.getTimeZone(Timezon);
			formatter.setTimeZone(tz);
			//System.out.println(formatter.format(date));
			Date tempDate = (Date) formatter.parse(formatter.format(date));
			if (dateType) {
				formatter = new SimpleDateFormat("MMddyy HH:mm:ss");
				formatter.setTimeZone(tz);
			} else {
				formatter = new SimpleDateFormat("MMddyyyy HH:mm:ss");
				formatter.setTimeZone(tz);
			}
			newDate = formatter.format(tempDate);

		} catch (ParseException e) {
			System.out.println("Exception :" + e);

		}

		return newDate;
	}
	
	
	/**
	 * validate the date String ( Date pattern should MM/dd/yyyy)
	 * @param dateStr
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	public static boolean validateDateString(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("MM/dd/yyyy");
		try {
			Date temp = sdf.parse(dateStr);
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}

		StringTokenizer tokenizer = new StringTokenizer(dateStr, "/");
		int i = 0;
		int month = 0;
		int day = 0;
		int year = 0;

		try {
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				int value = new Integer(token).intValue();
				if (i == 0) {
					if (value > 12 || value <= 0) {

						return false;

					}
					month = value;
				} else if (i == 1) {
					if (value > 31 || value <= 0) {

						return false;

					}
					day = value;
				} else if (i == 2) {

					year = value;

				}
				i++;
			}
			year = 2000 + year;
			if (month == 4 || month == 6 || month == 9 || month == 11) {

				if (day > 30) {

					return false;

				}
			} else if (month == 2) {
				if (isLeapYear(year) && day > 29) {

					return false;
				} else if (!isLeapYear(year) && day > 28) {

					return false;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * validate the leap year
	 * @param year
	 * @return boolean
	 */
	public static boolean isLeapYear(int year) {
		if (year % 100 == 0) {

			if (year % 400 == 0) {

				return true;

			} else {

				return false;

			}
		}
		if (year % 4 == 0) {

			return true;

		} else {

			return false;

		}
	}

	/**
	 * validate customer Id is numeric or not.
	 * @param customerIdStr
	 * @return boolean TURE if customerIdStr is numeric or else FALSE
	 */
	@SuppressWarnings("unused")
	public static boolean checkIntegerValue(String customerIdStr) {
		try{
			Integer customerId = Integer.parseInt(customerIdStr);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	/**
	 * validate local export file path (i.e. valid and exists)
	 * @param localPath
	 * @return boolean TRUE if local file path is correct or else false
	 */
	public static boolean validLocalPath(String localPath) {
		try {
			if (!(new File(localPath)).exists()) {
				File f = new File(localPath);
				f.mkdirs();
				return f.canWrite();
			}else{
				File f = new File(localPath);
				return f.canWrite();
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Time unit formatter
	 * 
	 * @param millis
	 * @return
	 */
	public static String timeTaken(long millis) {
		long p = millis % 1000;
		long s = (millis / 1000) % 60;
		long m = ((millis / 1000) / 60) % 60;
		long h = ((millis / 1000) / (60 * 60)) % 24;
		return (h == 0) ? ((m == 0) ? String.format("%02d.%03d Sec", s, p)
				: String.format("%02d Minutes %02d.%03d Sec", m, s, p))
				: String.format("%d Hours %02d Minutes %02d.%03d Sec", h, m, s,
						p);
	}
}
