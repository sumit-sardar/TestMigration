package com.ctb.util.testAdmin; 

import java.util.Date;

import com.ctb.bean.testAdmin.ActiveSession;
import com.ctb.bean.testAdmin.ManageTestSession;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.util.DateUtils;

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
        
        Date originalEndTime = session.getDailyLoginEndTime();
        Date endOffSetDate = concatinateDateTime(session.getLoginEndDate(), originalEndTime);

        Date adjustedStartDate = DateUtils.getAdjustedDate(startOffSetDate, session.getTimeZone(), "GMT", startOffSetDate);
        Date adjustedEndDate = DateUtils.getAdjustedDate(endOffSetDate, session.getTimeZone(), "GMT", endOffSetDate);
        
       
        session.setDailyLoginStartTime(adjustedStartDate);
        session.setDailyLoginEndTime(adjustedEndDate);
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
        
        Date originalEndTime = testSession.getDailyLoginEndTime();
        Date endOffSetDate = concatinateDateTime(testSession.getLoginEndDate(), originalEndTime);
       
        Date adjustedStartDate = DateUtils.getAdjustedDate(startOffSetDate, "GMT", testSession.getTimeZone(), startOffSetDate);
        
        Date adjustedEndDate = DateUtils.getAdjustedDate(endOffSetDate, "GMT", testSession.getTimeZone(), endOffSetDate);
        
        testSession.setDailyLoginStartTime(adjustedStartDate);
        testSession.setDailyLoginEndTime(adjustedEndDate);
        testSession.setLoginStartDate(adjustedStartDate);
        testSession.setLoginEndDate(adjustedEndDate);
    }
    
    //Change for defect #66657
    public static void adjustSessionTimesToLocalTimeZoneForExport(ManageTestSession testSession) {
    	Date originalStartTime = testSession.getDailyLoginStartTime();
        Date startOffSetDate = concatinateDateTime(testSession.getStartDate(), originalStartTime);
        //Date adjustedStartTime = DateUtils.getAdjustedDate(testSession.getDailyLoginStartTime(), "GMT", testSession.getTimeZone(), startOffSetDate);
        
        Date originalEndTime = testSession.getDailyLoginEndTime();
        Date endOffSetDate = concatinateDateTime(testSession.getEndDate(), originalEndTime);
        //Date adjustedEndTime = DateUtils.getAdjustedDate(testSession.getDailyLoginEndTime(), "GMT", testSession.getTimeZone(), endOffSetDate);
        
        //Date originalStartDate = testSession.getStartDate();
        Date adjustedStartDate = DateUtils.getAdjustedDate(startOffSetDate, "GMT", testSession.getTimeZone(), startOffSetDate);
        
        //Date originalEndDate = testSession.getEndDate();
        Date adjustedEndDate = DateUtils.getAdjustedDate(endOffSetDate, "GMT", testSession.getTimeZone(), endOffSetDate);
        
        /*if(DateUtils.dateAfter(adjustedStartTime, originalStartTime))
            adjustedStartDate.setTime(adjustedStartDate.getTime() + DateUtils.daysToMillis(1));
        else if(DateUtils.dateBefore(adjustedStartTime, originalStartTime))
            adjustedStartDate.setTime(adjustedStartDate.getTime() - DateUtils.daysToMillis(1));

        if(DateUtils.dateAfter(adjustedEndTime, originalEndTime))
            adjustedEndDate.setTime(adjustedEndDate.getTime() + DateUtils.daysToMillis(1));
        else if(DateUtils.dateBefore(adjustedEndTime, originalEndTime))
            adjustedEndDate.setTime(adjustedEndDate.getTime() - DateUtils.daysToMillis(1));*/

        testSession.setDailyLoginStartTime(adjustedStartDate);
        testSession.setDailyLoginEndTime(adjustedEndDate);
        testSession.setStartDate(adjustedStartDate);
        testSession.setEndDate(adjustedEndDate);
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
