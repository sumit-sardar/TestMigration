package com.ctb.utils;

/**
 * This class contains constant values used in the utility.
 * 
 * @author TCS
 * 
 */
public class Constants {
	
	//File Headers of SF license data file::
	public static final String  CUSTOMER_ID 				= "OAS Implementation: Customer Org ID";
	public static final String  OAS_IMPLEMENTATION_ID 		= "OAS Implementation: OAS Name";
	public static final String  IMPL_RECORD_TYPE			= "OAS Implementation: OAS Implementation Record Type";
	public static final String  CUSTOMER_ACCOUNT_NAME 		= "OAS Implementation: Customer Account Name";
	public static final String  ACCOUNT_STATE 				= "OAS Implementation: Account State";
	public static final String  ORG_NODE_ID					= "ORG_ID";
	public static final String  ORG_NODE_NAME 				= "License Management Action: Organization_Name";
	public static final String  CONTACT_PHONE 				= "OAS Implementation: Contact Phone";
	public static final String  CONTACT						= "OAS Implementation: Contact";
	public static final String  CONTACT_EMAIL				= "OAS Implementation: Email";
	public static final String  CATEGORY_NAME		 		= "Category/Framework Name";
	public static final String  CATEGORY_LEVEL				= "Category/Framework Level (1-7)";
	public static final String  LICENSE_MODEL				= "OAS Implementation: License Model Type";
	public static final String  LICENSE_COUNT 				= "License Distribution Amount";
	public static final String  ORDER_QUANTITY				= "OAS Implementation: Order Quantity";
	public static final String  LICENSE_DISTRIBUTED_TO		= "License Distributed To";
	public static final String  CREATED_DATE				= "License Management Action: Created Date";
	public static final String  INTERVAL_NAME				= "OAS Implementation: Interval Name";
	
	//File extensions for input files::
	public static final String FILE_TYPE_XLS					= ".xls";
	public static final String FILE_TYPE_XLSX					= ".xlsx";
		
	//Header Count
	public static final Integer HEADER_COUNT					= new Integer(18);
	
	//Data Field Size
	public static final int  OAS_IMPLEMENTATION_ID_SIZE 		= 10;
	public static final int  IMPL_RECORD_TYPE_SIZE				= 64;
	public static final int  CUSTOMER_ACCOUNT_NAME_SIZE 		= 64;
	public static final int  ACCOUNT_STATE_SIZE 				= 2;
	public static final int  ORG_NODE_NAME_SIZE 				= 64;
	public static final int  CONTACT_PHONE_SIZE 				= 32;
	public static final int  CONTACT_SIZE						= 64;
	public static final int  CONTACT_EMAIL_SIZE					= 64;
	public static final int  CATEGORY_NAME_SIZE		 			= 64;
	public static final int  LICENSE_MODEL_SIZE					= 10;
	public static final int  LICENSE_DISTRIBUTED_TO_SIZE		= 10;
	public static final int  INTERVAL_NAME_SIZE					= 32;
}
