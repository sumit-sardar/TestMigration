package com.ctb.util.testAdmin; 

import com.ctb.bean.testAdmin.ActiveSession;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.util.DateUtils;
import java.util.Date;

public class TestAdminStatusComputer 
{ 
    public static void adjustSessionTimesToGMT(ActiveSession session) {
        TestSession testSession = new TestSession();
        testSession.setLoginStartDate(session.getLoginStartDate());
        testSession.setLoginEndDate(session.getLoginEndDate());
        testSession.setDailyLoginStartTime(session.getDailyLoginStartTime());
        testSession.setDailyLoginEndTime(session.getDailyLoginEndTime());
        testSession.setTimeZone(session.getTimeZone());
        adjustSessionTimesToGMT(testSession);
        session.setLoginStartDate(testSession.getLoginStartDate());
        session.setLoginEndDate(testSession.getLoginEndDate());
        session.setDailyLoginStartTime(testSession.getDailyLoginStartTime());
        session.setDailyLoginEndTime(testSession.getDailyLoginEndTime());
    }
    
    public static void adjustSessionTimesToGMT(TestSession session) {
        Date originalStartTime = session.getDailyLoginStartTime();
        Date startOffSetDate = concatinateDateTime(session.getLoginStartDate(), originalStartTime);
        startOffSetDate = DateUtils.getAdjustedDate(startOffSetDate, session.getTimeZone(), "GMT", startOffSetDate);
        Date adjustedStartTime = DateUtils.getAdjustedDate(session.getDailyLoginStartTime(), session.getTimeZone(), "GMT", startOffSetDate);
        
        Date originalEndTime = session.getDailyLoginEndTime();
        Date endOffSetDate = concatinateDateTime(session.getLoginEndDate(), originalEndTime);
        endOffSetDate = DateUtils.getAdjustedDate(endOffSetDate, session.getTimeZone(), "GMT", endOffSetDate);
        Date adjustedEndTime = DateUtils.getAdjustedDate(session.getDailyLoginEndTime(), session.getTimeZone(), "GMT", endOffSetDate);
        
        Date originalStartDate = session.getLoginStartDate();
        Date adjustedStartDate = DateUtils.getAdjustedDate(session.getLoginStartDate(), session.getTimeZone(), "GMT", startOffSetDate);
        
        Date originalEndDate = session.getLoginEndDate();
        Date adjustedEndDate = DateUtils.getAdjustedDate(session.getLoginEndDate(), session.getTimeZone(), "GMT", endOffSetDate);
        
        if(DateUtils.dateAfter(adjustedStartTime, originalStartTime))
            adjustedStartDate.setTime(adjustedStartDate.getTime() + DateUtils.daysToMillis(1));
        else if(DateUtils.dateBefore(adjustedStartTime, originalStartTime))
            adjustedStartDate.setTime(adjustedStartDate.getTime() - DateUtils.daysToMillis(1));
            
        if(DateUtils.dateAfter(adjustedEndTime, originalEndTime))
            adjustedEndDate.setTime(adjustedEndDate.getTime() + DateUtils.daysToMillis(1));
        else if(DateUtils.dateBefore(adjustedEndTime, originalEndTime))
            adjustedEndDate.setTime(adjustedEndDate.getTime() - DateUtils.daysToMillis(1));
        
        session.setDailyLoginStartTime(adjustedStartTime);
        session.setDailyLoginEndTime(adjustedEndTime);
        session.setLoginStartDate(adjustedStartDate);
        session.setLoginEndDate(adjustedEndDate);
    }
    
    public static void adjustSessionTimesToLocalTimeZone(ActiveSession session) {
        TestSession testSession = new TestSession();
        testSession.setLoginStartDate(session.getLoginStartDate());
        testSession.setLoginEndDate(session.getLoginEndDate());
        testSession.setDailyLoginStartTime(session.getDailyLoginStartTime());
        testSession.setDailyLoginEndTime(session.getDailyLoginEndTime());
        testSession.setTimeZone(session.getTimeZone());
        adjustSessionTimesToLocalTimeZone(testSession);
        session.setLoginStartDate(testSession.getLoginStartDate());
        session.setLoginEndDate(testSession.getLoginEndDate());
        session.setDailyLoginStartTime(testSession.getDailyLoginStartTime());
        session.setDailyLoginEndTime(testSession.getDailyLoginEndTime());
    }
    
    public static void adjustSessionTimesToLocalTimeZone(TestSession testSession) {
        Date originalStartTime = testSession.getDailyLoginStartTime();
        Date startOffSetDate = concatinateDateTime(testSession.getLoginStartDate(), originalStartTime);
        Date adjustedStartTime = DateUtils.getAdjustedDate(testSession.getDailyLoginStartTime(), "GMT", testSession.getTimeZone(), startOffSetDate);
        
        Date originalEndTime = testSession.getDailyLoginEndTime();
        Date endOffSetDate = concatinateDateTime(testSession.getLoginEndDate(), originalEndTime);
        Date adjustedEndTime = DateUtils.getAdjustedDate(testSession.getDailyLoginEndTime(), "GMT", testSession.getTimeZone(), endOffSetDate);
        
        Date originalStartDate = testSession.getLoginStartDate();
        Date adjustedStartDate = DateUtils.getAdjustedDate(testSession.getLoginStartDate(), "GMT", testSession.getTimeZone(), startOffSetDate);
        
        Date originalEndDate = testSession.getLoginEndDate();
        Date adjustedEndDate = DateUtils.getAdjustedDate(testSession.getLoginEndDate(), "GMT", testSession.getTimeZone(), endOffSetDate);
        
        if(DateUtils.dateAfter(adjustedStartTime, originalStartTime))
            adjustedStartDate.setTime(adjustedStartDate.getTime() + DateUtils.daysToMillis(1));
        else if(DateUtils.dateBefore(adjustedStartTime, originalStartTime))
            adjustedStartDate.setTime(adjustedStartDate.getTime() - DateUtils.daysToMillis(1));

        if(DateUtils.dateAfter(adjustedEndTime, originalEndTime))
            adjustedEndDate.setTime(adjustedEndDate.getTime() + DateUtils.daysToMillis(1));
        else if(DateUtils.dateBefore(adjustedEndTime, originalEndTime))
            adjustedEndDate.setTime(adjustedEndDate.getTime() - DateUtils.daysToMillis(1));

        testSession.setDailyLoginStartTime(adjustedStartTime);
        testSession.setDailyLoginEndTime(adjustedEndTime);
        testSession.setLoginStartDate(adjustedStartDate);
        testSession.setLoginEndDate(adjustedEndDate);
    }
    
    public static void setTestSessionStatus(TestSession session) {
        Date now = new Date();
        String status = "FU";
        if(DateUtils.dateBefore(session.getLoginStartDate(), now))
            status = "CU";
        if(DateUtils.dateEquals(session.getLoginStartDate(), now))
            if(DateUtils.timeBefore(session.getDailyLoginStartTime(), now))
                status = "CU";
        if(DateUtils.dateBefore(session.getLoginEndDate(), now))
            status = "PA";
        if(DateUtils.dateEquals(session.getLoginEndDate(), now))
            if(DateUtils.timeBefore(session.getDailyLoginEndTime(), now))
                status = "PA";
                
        session.setTestAdminStatus(status);
    }
    
    private static Date concatinateDateTime(Date date, Date time) {
        Date result = (Date) date.clone();
        result.setHours(time.getHours());
        result.setMinutes(time.getMinutes());
        result.setSeconds(time.getSeconds());
        return result;
    }
} 
