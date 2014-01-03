package comm.ctb.utils;

/**
 * This class contains constant values used in the utility.
 * 
 * @author TCS
 * 
 */
public class Constants {
	//Elements::
	public static final String ELEM_STU_TEST_DET 		= "Student_Test_Details";
	public static final String ELEM_ITEM_DP_DET 		= "Item_DataPoint_Details";
	public static final String ELEM_READ_DET			= "Read_Details";
	
	//Attributes::
	public static final String ATTR_STU_TEST_ID    		= "Student_Test_ID";
	public static final String ATTR_STU_L_NAME  		= "Last_Name";
	public static final String ATTR_STU_F_NAME 			= "First_Name";
	public static final String ATTR_STU_M_I	    		= "Middle_Initial";
	public static final String ATTR_STU_DOB	    		= "Date_Of_Birth";
	public static final String ATTR_STU_DOC_ID    		= "Document_ID";
	public static final String ATTR_STU_GRADE    		= "Grade";
	public static final String ATTR_LEVEL	    		= "Level";
	public static final String ATTR_COMM_CODE    		= "Commodity_Code";
	public static final String ATTR_FORM	    		= "Form";
	public static final String ATTR_VENDOR_STU_ID  		= "Vendor_Student_Id";
	public static final String ATTR_ITEM_ID   	 		= "Item_ID";
	public static final String ATTR_DATAPOINT    		= "Data_Point";
	public static final String ATTR_ITEM_NO	    		= "Item_No";
	public static final String ATTR_ALERT_CODE    		= "Alert_Code";
	public static final String ATTR_READ_NO	    		= "Read_Number";
	public static final String ATTR_SCORE_VAL    		= "Score_Value";
	public static final String ATTR_READER_ID    		= "Reader_ID";
	public static final String ATTR_DATE_TIME   		= "Date_Time";
	public static final String ATTR_ELAPSED_TIME		= "Elapsed_Time";
	
	//Regex::
	public static final String regexNumeric = "[0-8]{1}"; 
	public static final String regexAlpha = "[A-E]{1}";
	public static final String regexDecimal = "^([0-9]+)(\\.[0-9]+)$";
}
