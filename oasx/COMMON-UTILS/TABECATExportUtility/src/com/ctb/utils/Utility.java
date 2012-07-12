package com.ctb.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utility {
	private static final DateFormat birthDateFormat = new SimpleDateFormat(
	"yyyy-MM-dd");

	public static String calculateAge(Date birthDate) {
		Integer totalMonths = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(birthDate);
		String currentDate = birthDateFormat.format(new Date());
		System.out.println("Age:sb :   " + sb.toString());
		int dd1 = Integer.parseInt(sb.toString().substring(8, 10));
		int mm1 = Integer.parseInt(sb.toString().substring(5, 7));
		int yy1 = Integer.parseInt(sb.toString().substring(0, 4));

		int dd2 = Integer.parseInt(currentDate.substring(8, 10));
		int mm2 = Integer.parseInt(currentDate.substring(5, 7));
		int yy2 = Integer.parseInt(currentDate.substring(0, 4));
		int day = 0;
		int year = 0;
		int mm3 = 0;
		int k = 0;
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
		if (dd2 != dd1) {
			day = dd2 - dd1;
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
	public static String truncateScores(String value, int length)
	{
	  if (value != null && value.length() > length){
	    value = value.substring(0, length);
	    value.replace(".", "");
	  }
	  else {
			value = "";
			value = String.format("%"+length+"s", value);
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
		try{
			if(val != null && !val.startsWith("N/A")){
				Integer result = Float.valueOf(val).intValue();
				val = result.toString();
			} else {
				val = "";
			}
		}catch(NumberFormatException ne){
			if (val.length() > 0){
				if(val.length() > len){
					val = val.substring(0, len);
				}
			}else {
				val = null;
			}
		}
		/*if(val != null && val.trim().length() !=0){
			val = String.format("%"+len+"s", val).replace(" ", " ").concat("#");
			return val;
		}else{
			val = "";
			val = String.format("%"+len+"s", val).concat("#");
			return val;
		} */
		val=val.concat("#");
		return val;
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
		if (val != null && val.trim().length() != 0) {
			DecimalFormat df = new DecimalFormat("##0.00");
			try {
				val = df.format(Double.parseDouble(val.trim()));
			} catch (NumberFormatException e) {
				val = "0.00";
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
	//For TimeZone Conversion
	public static String getTimeZone(String val, String timeZon,
			boolean dateType) {

		if(val == null)
			return "";
		
		String newDate = null;
		if (timeZon == null){
			timeZon = "GMT";
		}
		try {
			String str_date = val + " " + "GMT";
			DateFormat formatter;
			Date date;
			formatter = new SimpleDateFormat("MMddyyyy HH:mm:ss zzz");
			date = (Date) formatter.parse(str_date);
			TimeZone tz = TimeZone.getTimeZone(timeZon);
			formatter.setTimeZone(tz);
			System.out.println(formatter.format(date));
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
	

}
