package com.ctb.lexington.db.utils;

import java.sql.Date;
import java.util.Calendar;

public class DataHelper {

	public static int getQuarterMonth(String str) {
		int ret =0;
		// huuuuuuuuuuuuuugggggggggeeeeeeeeeeee
		// if( else statement, directly from the stored proc
		// REDTAG!!!refactor
		if(str.equals("F1997C")|| str.equals("F1998C")||str.equals("F1998R")||str.equals("F1999C")||str.equals("F1999R")||str.equals("F2000C")||str.equals("F2000R")||str.equals("F2001C")||str.equals("F2001R")){
			ret= 3;
		}
		else if( str.equals("S1999R")||str.equals("S2000R")||str.equals("S2001R")||str.equals("S2002R")){
			ret= 1;
		}
		return ret;
	}

	public static String getSchoolYear(Date date) {
		//copied directly from the stored proc!!
		// refactor!
		String schoolYear;

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

		if(month>=8 && month<=12){
			//We're in the first half of the year.  Add one to the year in the second hald
			schoolYear = year+"-"+(year + 1);
		}else{
			schoolYear = (year-1)+"-"+year;
		}
		return schoolYear;
	}

	public static int getCalendarYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
	}

	public static int getDifferenceInMonths(Date date) {
		Calendar currDate = Calendar.getInstance();

		Calendar newCal = Calendar.getInstance();
		newCal.setTimeInMillis(date.getTime());

		int diffYears = getDifferenceInYears(date) - 1;
		int months = diffYears *12;
		months += Math.abs(currDate.get(Calendar.MONTH) + (12 - newCal.get(Calendar.MONTH)));
		return months;
	}

	public static int getDifferenceInYears(Date date) {
		Calendar currDate = Calendar.getInstance();

		Calendar newCal = Calendar.getInstance();
		newCal.setTimeInMillis(date.getTime());

		return currDate.get(Calendar.YEAR) - newCal.get(Calendar.YEAR);
	}

	public static boolean matchObjects(Object obj1, Object obj2) {
		if(obj1 == null && obj2 == null) return true;
        //if either of them is null but both are not null (look at above cond),
        // then they do not match
        if(obj1 == null || obj2 == null ) return false;
        //if both are not null, check if they are equal
        return obj1.equals(obj2);
	}
}