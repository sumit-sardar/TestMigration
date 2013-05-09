package com.ctb.util.licensing; 

/*
 * CTBConstants.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 */

import java.math.BigDecimal;

/**
 * <code>CTBConstants</code> defines constants used throughout the application.
 *
 * @author  Tata Consultency Services
 * 
 */

public class CTBConstants 
{ 
    static public final String SESSION_PREFERENCES = "sessionPrefs";
    static public final String TRUE_INITIAL = "T";
    static public final String FALSE_INITIAL = "F";   
	
    //mail configuration
    public static final String EMAIL_FROM = "iknow_account_management@ctb.com";
    public static final String EMAIL_FROM_ALIAS_LASLINKS = "LAS Links – OAS Account Management";
	public static final String EMAIL_CONTENT_PLACEHOLDER_ORDERNO = "<#orderNo#>";
    public static final String EMAIL_CONTENT_PLACEHOLDER_LICENSEQTY = "<#licenseQuantity#>";
    public static final String EMAIL_CONTENT_PLACEHOLDER_PURCHASEDATE = "<#purchaseDate#>";
    public static final String EMAIL_CONTENT_PLACEHOLDER_EXPIRYDATE = "<#expiryDate#>";
} 
