/*
 * OEConstants.java
 *
 * Created on August 22, 2002, 10:27 AM
 */

package com.ctb.lexington.util;

/**
 *
 * @author  vsaxena
 * @version 
 */
public class OEConstants {

    public static final int CONTACT_TYPE_ID_1 = 10;    
    
    public static final String PHASE_ID_LABEL        = "PhaseID";
    public static final String ORG_NODE_ID_LABEL     = "OrgNodeID";
    public static final String CONTACT_TYPE_ID_LABEL = "ContactTypeID";
    public static final String CONTACT_LABEL         = "Contact";
    public static final String OENODE_LABEL          = "OENode";
    public static final String EDIT_CONTACT_ROWS_UPDATED_LABEL = "EditContactRowsUpdated";
    public static final String USER_NAME_LABEL       = "UserName";
    public static final String UPDATED_ENROLLMENT_COUNTHT_LABEL = "SubjectsHelperHT";
    public static final String EDIT_ENROLLMENT_ROWS_UPDATED_LABEL = "EditEnrollmentRowsUpdated";
        
    public static final String   COUNTRY_LABEL       = "country";
    public static final String   DIST_LABEL          = "dist";
    public static final String   DIST_CODE_LABEL     = "distCode";
    public static final String   EMAIL_LABEL         = "email";
    public static final String   FAX_LABEL           = "fax";           
    public static final String   PHONE_LABEL         = "phone";
    public static final String   SCHOOL_LABEL        = "school";
    public static final String   SCHOOL_CODE_LABEL   = "REQ_PARAM_SCHOOL_CODE_NAME";
    public static final String   STATE_LABEL         = "state";                          
    public static final String   STREET_LABEL        = "street";
    public static final String   TEST_ADMIN_LABEL    = "testAdmin";
    public static final String   TEST_COOD_LABEL     = "testCood";
    public static final String   ZIP_LABEL           = "zip";
    //>>
    public static final String USER_ID_LABEL = "UserID";
    public static final String PHASE_NAME_LABEL = "REQ_PARAM_TEST_AD_NAME";
    public static final String PARENT_ORG_NODE_ID_LABEL = "ParentOrgID";                                           
    public static final String PARENT_ORG_NAME_LABEL = "REQ_PARAM_PARENT_NAME";                                           
    public static final String PARENT_ORG_CODE_LABEL = "REQ_PARAM_PARENT_CODE_NAME";                                           
    public static final String PARENT_ORG_CAT_NAME_LABEL = "REQ_PARAM_PARENT_CAT_NAME";                                                       
    public static final String SCHOOL_NAME_LABEL = "REQ_PARAM_SCHOOL_NAME";     
    public static final String ADDED_SCHOOL_NODE_ID_LABEL = "AddedSchoolNodeID";
    //<<
    //org node category names, these are what will be displayed
    public static final String ORG_CATEGORY_STATE           = "State";
    public static final String ORG_CATEGORY_COUNTY          = "County";
    public static final String ORG_CATEGORY_CORPORATION     = "Corporation";
    public static final String ORG_CATEGORY_DIOCESE         = "Diocese";
    public static final String ORG_CATEGORY_DISTRICT        = "District";    
    public static final String ORG_CATEGORY_SCHOOL          = "School"; 

    //for Select School to add achool, edit "district" contact points
    public static final String THIS_NODE                    = "ThisNode";
    public static final String PARENT_NODE                  = "ParentNode";
    public static final String EDIT_DIST_CONTACT_URI        = "/editdistrictcontact/editcontactcmd";
    public static final String ADD_SCHOOL_URI               = "/addschoolnode/stepzerocmd";
    public static final String SCHOOL_DETAIL_URI            = "/editschoolenrollments/editenrollmentscmd";
    public static final String SELECT_SCHOOL_URI            = "/selectschool/selectschoolcmd";                                                            
   
    public static final String EDIT_CONTACT_CLIENT_LABEL     = "ECClient";
    public static final String SELECT_SCHOOL_PAGE     = "SelectSchool";
    public static final String SCHOOL_DETAIL_PAGE     = "SchoolDetail";
    public static final String PHASES_HT = "PHT";
    public static final String SCHOOL_DETAIL_CLIENT_LABEL = "SDClient";
    public static final String EDIT_SCHOOL_CONTACT_CONFIRM_PAGE = "SCConfirm";
    public static final String SELECT_SCHOOL_CLIENT_LABEL = "SSClient";
    public static final String EDIT_DIST_CONTACT_CONFIRM_PAGE = "DCConfirm";
    public static final String ADD_SCHOOL_CONFIRM_PAGE = "ADSConfirm";
    public static final String CONTEXTUAL_NAV = "CNAV";
    
    
    public static final String OE_TREE_NAME = "oeTestTreeTag";
    
    public static final int LOG_LEVEL_1 = 1;
    public static final int LOG_LEVEL_2 = 3;
    public static final int LOG_LEVEL_3 = 7;
    
    //Below constants pertain to OE R2
    public static final String OPEN_SCHOOL_STATUS_LABEL = "0";
    public static final String CLOSE_SCHOOL_STATUS_LABEL = "1";
    public static final String IS_SCHOOL_CLOSED_LABEL = "isSchoolClosed";
    public static final String NEW_SCHOOL_LABEL = "newSchoolLabel";
    
    public static final String[] BANNED_CHARACTERS = {";","<",">","\"",")","(","%","&","+"};
    
    public static final String UNITED_STATES_COUNTRY_CODE = "US";
    public static final String CANADA_COUNTRY_CODE = "CA";
    public static final String UNITED_STATES_COUNTRY_LABEL = "United States of America";
    public static final String CANADA_COUNTRY_LABEL = "Canada";
    
    public static final String REPORT_FILE_NAME_LABEL = "ReportFileName";
    public static final String IS_TOP_LEVEL_USER_LABEL = "IsTopLevelUser";
    
    /** Creates new OEConstants */
    public OEConstants() {
    }
}
