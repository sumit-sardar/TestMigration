package com.ctb.util.testDelivery; 

import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Status.StatusCode;

public class Constants 
{ 
    public static class StudentLoginResponseStatus 
    {
        public static StatusCode.Enum OK_STATUS = StatusCode.X_200;
        public static StatusCode.Enum OUT_OF_DATE_APPLICATION_STATUS = StatusCode.X_461;
        public static StatusCode.Enum AUTHENTICATION_FAILURE_STATUS = StatusCode.X_471;
        public static StatusCode.Enum TEST_SESSION_IN_PROGRESS_STATUS = StatusCode.X_472;
        public static StatusCode.Enum TEST_SESSION_NOT_SCHEDULED_OR_INTERRUPTED_STATUS = StatusCode.X_473;
        public static StatusCode.Enum OUTSIDE_TEST_WINDOW_STATUS = StatusCode.X_474;
        public static StatusCode.Enum TEST_SESSION_COMPLETED_STATUS = StatusCode.X_475;
        public static StatusCode.Enum KEY_ENTERED_RESPONSES_STATUS = StatusCode.X_476;
        public static StatusCode.Enum LOCATOR_SUBTEST_NOT_COMPLETED_STATUS = StatusCode.X_477;
        public static StatusCode.Enum INTERNAL_SERVER_ERROR_STATUS = StatusCode.X_500;
        public static StatusCode.Enum SERVER_ERROR_STATUS = StatusCode.X_501;
    }
    
    public static class StudentTestCompletionStatus 
    { 
        public static String IN_PROGRESS_STATUS = "IP";
        public static String SCHEDULED_STATUS = "SC";
        public static String STUDENT_STOP_STATUS = "IS";
        public static String SYSTEM_STOP_STATUS = "IN";
        public static String COMPLETED_STATUS = "CO";
        public static String ONLINE_COMPLETED_STATUS = "OC";
        public static String STUDENT_PAUSE_STATUS = "SP";
        public static String INCOMPLETE_STATUS = "IC";
    } 
    
    public static class TestAdminStatus 
    { 
        public static String CURRENT = "CU";
        public static String FUTURE = "FU";
        public static String PAST = "PA";
    } 
    
    public static class RosterCaptureMethod
    {
        public static String CAPTURE_METHOD_ONLINE = "ON";
    }
    
    public static class RenderSpecs
    {
        public static int STANDARD_FONT_SIZE = 12;
    }
    
    public static class LifecycleEvent
    {
        public static String START_ITEM_SET = "lms_initialize";
        public static String FINISH_ITEM_SET = "lms_finish";
    }
    public static class SpeechController
    {
        public static String DEFAULT_TTS_SPEED_VALUE  = "M";
        
    }
    
    public static class LoadTestConfig{
    	
    	public static String RUN_LOAD = "R";
    	public static String NO_RUN = "N";
    }
} 
