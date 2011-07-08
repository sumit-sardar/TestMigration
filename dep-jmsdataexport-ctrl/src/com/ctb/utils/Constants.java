package com.ctb.utils;

/**
 * This interface contains all the constants used in this POC
 * all the values need to be modified as the environment
 */
    public interface Constants 
{ 
    public static final String dbServerName = "01hw157458";
    public static final String dbPortNumber = "1521";
    public static final String SID = "martini5";
    public static final String dbUserID = "martini_store553";
    public static final String dbPassword = "martini553";
    public static final String PROVIDER_URL = "t3://localhost:7001";
    //public static final String PROVIDER_URL = "t3://192.168.14.74:7001";//payal system
    public static final String CTBConnFactory = "sadhujndi";
    public static final String CTBQueue = "JMSQueue";
    public static final String AcuityConnFactory = "AcuityConnFacJNDI";
    public static final String AcuityQueue = "AcuityQueueJNDI";
    public static final String OASConnFactory = "factoas";
    //public static final String OASConnFactory = "oasscoringfact";//payal system
    public static final String OASQueue = "queueoas";
    //public static final String OASQueue = "oasscoringqueue";//payal system
}