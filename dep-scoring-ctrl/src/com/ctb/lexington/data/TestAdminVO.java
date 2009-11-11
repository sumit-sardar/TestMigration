package com.ctb.lexington.data;

/*
 * TestAdminVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */
/* java import */
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @author jtsang
 * @created January 20, 2002
 * @version
 * $Id$
 */
public class TestAdminVO implements Serializable
{
    //Constants
    public static final String VO_LABEL = "com.ctb.lexington.bean.TestAdminVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    public static final String STATUS_PAST    = "PA"; //from db
    public static final String STATUS_CURRENT = "CU"; //from db, and if not STATUS_ACTIVE
    public static final String STATUS_FUTURE  = "FU"; //from db
    public static final String STATUS_ACTIVE  = "AC"; //CURRENT that has logged in Studs

    public static final String TYPE_TUTORIAL_SESSION = "TU";
    public static final String TYPE_TEST_SESSION     = "SE";

    //--------------------------------------------------------------------------
    // DATA fields i.e. in TestAdminEJB
    //--------------------------------------------------------------------------

    private String testAdminId;
    private String testAdminName;
    
    private Integer normsGroup;
    private String ageCategory;
    
    private String itemSetId;
    private String itemSetDisplayName; //derived indirectly

    private String createdBy;
    private String createdByName; //derived indirectly

    private String creatorOrgNodeId;
    private String creatorOrgNodeName;

    private String location;

    private Calendar loginStartDate;
    private Calendar loginEndDate;

    private String loginBeginTime;
    private String loginFinishTime;

    private String timezoneDescription; //derived indirectly

    private String accessCode;

    private boolean enforceTimeLimit = true;
    private boolean enforceTutorial  = true;
    private boolean enforceBreak     = false;

    private String lexingtonVersion;

    private String sessionNumber;
    private String testAdminStatus;
    private String testAdminType;
    private String productId;
    private Date createdDateTime;
    private Integer customerId;
    private Date dailyLoginEndTime;
    private Date dailyLoginStartTime;
    private Integer tutorialId;
    private Integer updatedBy;
    private Date updatedDateTime;


    //--------------------------------------------------------------------------
    // Derived/BUSINESS fields - not a data property on respective entity bean
    //
    // NOTE: these below should be moved to testAdminDetailVO!
    //
    //--------------------------------------------------------------------------
    private boolean haveStudentsCompletedTest = false;
    private boolean haveStudentsLoggedIn = false;


	public String getAgeCategory() {
		return ageCategory;
	}
	public void setAgeCategory(String ageCategory) {
		this.ageCategory = ageCategory;
	}
	public Integer getNormsGroup() {
		return normsGroup;
	}
	public void setNormsGroup(Integer normsGroup) {
		this.normsGroup = normsGroup;
	}
    /**
     * Ctor - Creates new TestAdminVO
     */
    public TestAdminVO() { }


    //--------------------------------------------------------------------------
    // DATA fields simple Set/Get methods.
    //--------------------------------------------------------------------------

    /**
     * @return The testAdministrationId value
     */
    public String getTestAdminId()
    {
        return this.testAdminId;
    }

    /**
     * Sets the testAdministrationId attribute of the TestAdminVO
     * object
     *
     * @param id_ The new testAdministrationId value
     */
    public void setTestAdminId(String id_)
    {
        this.testAdminId = id_;
    }

    /**
     * Gets the testAdministrationName attribute of the TestAdminVO
     * object
     *
     * @return The testAdministrationName value
     */
    public String getTestAdminName()
    {
        return this.testAdminName;
    }

    /**
     * Sets the testAdministrationName attribute of the TestAdminVO
     * object
     *
     * @param testAdministrationName_ The new testAdministrationName value
     */
    public void setTestAdminName(String testAdministrationName_)
    {
        this.testAdminName = testAdministrationName_;
    }

    /**
     * Gets the productId attribute of the TestAdminVO object
     *
     * @return The productId value
     */
    public String getProductId()
    {
        return this.productId;
    }

    /**
     * Sets the productId attribute of the TestAdminVO object
     *
     * @param productId_ The new productId value
     */
    public void setProductId(String productId_)
    {
        this.productId = productId_;
    }

    /**
     * Gets the itemSetId attribute of the TestAdminVO object
     *
     * @return The itemSetId value
     */
    public String getItemSetId()
    {
        return this.itemSetId;
    }

    /**
     * Sets the itemSetId attribute of the TestAdminVO object
     *
     * @param itemSetID_ The new itemSetId value
     */
    public void setItemSetId(String itemSetID_)
    {
        this.itemSetId = itemSetID_;
    }

    /**
     * Gets the itemSetDisplayName attribute of the TestAdminVO
     * object
     *
     * @return The itemSetDisplayName value
     */
    public String getItemSetDisplayName()
    {
        return this.itemSetDisplayName;
    }

    /**
     * Sets the itemSetDisplayName attribute of the TestAdminVO
     * object
     *
     * @param n The new itemSetDisplayName value
     */
    public void setItemSetDisplayName(String n)
    {
        this.itemSetDisplayName = n;
    }

    /**
     * Gets the lexingtonVersion attribute of the TestAdminVO object
     *
     * @return The lexingtonVersion value
     */
    public String getLexingtonVersion()
    {
        return this.lexingtonVersion;
    }

    /**
     * Sets the lexingtonVersion attribute of the TestAdminVO object
     *
     * @param lexingtonVersion_ The new lexingtonVersion value
     */
    public void setLexingtonVersion(String lexingtonVersion_)
    {
        this.lexingtonVersion = lexingtonVersion_;
    }

    /**
     * Gets the sessionNumber attribute of the TestAdminVO object
     *
     * @return The sessionNumber value
     */
    public String getSessionNumber()
    {
        return this.sessionNumber;
    }

    /**
     * Sets the sessionNumber attribute of the TestAdminVO object
     *
     * @param sessionNumber_ The new sessionNumber value
     */
    public void setSessionNumber(String sessionNumber_)
    {
        this.sessionNumber = sessionNumber_;
    }

    /**
     * Gets the TestAdminStatus attribute of the TestAdminVO object
     *
     * @return The TestAdminStatus value
     */
    public String getTestAdminStatus()
    {
        return this.testAdminStatus;
    }

    /**
     * Sets the TestAdminStatus attribute of the TestAdminVO object
     *
     * @param testAdminStatus_ The new TestAdminStatus value
     */
    public void setTestAdminStatus(String testAdminStatus_)
    {
        this.testAdminStatus = testAdminStatus_;
    }

    /**
     * Gets the TestAdminType attribute of the TestAdminVO object
     *
     * @return The TestAdminType value
     */
    public String getTestAdminType()
    {
        return this.testAdminType;
    }

    /**
     * Sets the TestAdminType attribute of the TestAdminVO object
     *
     * @param testAdminType_ The new TestAdminType value
     */
    public void setTestAdminType(String testAdminType_)
    {
        this.testAdminType = testAdminType_;
    }

    /**
     * Gets the createdBy attribute of the TestAdminVO object
     *
     * @return The createdBy value
     */
    public String getCreatedBy()
    {
        return this.createdBy;
    }

    /**
     * Sets the createdBy attribute of the TestAdminVO object
     *
     * @param s The new createdBy value
     */
    public void setCreatedBy(String s)
    {
        this.createdBy = s;
    }

    /**
     * Gets the createdByName attribute of the TestAdminVO object
     *
     * @return The createdByName value
     */
    public String getCreatedByName()
    {
        return this.createdByName;
    }

    /**
     * Sets the createdByName attribute of the TestAdminVO object
     *
     * @param s The new createdByName value
     */
    public void setCreatedByName(String s)
    {
        this.createdByName = s;
    }

    /**
     * Gets the creatorOrgNodeId attribute of the TestAdminVO object
     *
     * @return The creatorOrgNodeId value
     */
    public String getCreatorOrgNodeId()
    {
        return this.creatorOrgNodeId;
    }

    /**
     * Sets the creatorOrgNodeId attribute of the TestAdminVO object
     *
     * @param id_ The new creatorOrgNodeId value
     */
    public void setCreatorOrgNodeId(String id_)
    {
        this.creatorOrgNodeId = id_;
    }

    /**
     * Gets the creatorOrgNodeName attribute of the TestAdminVO
     * object
     *
     * @return The creatorOrgNodeName value
     */
    public String getCreatorOrgNodeName()
    {
        return this.creatorOrgNodeName;
    }

    /**
     * Sets the creatorOrgNodeName attribute of the TestAdminVO
     * object
     *
     * @param name_ The new creatorOrgNodeName value
     */
    public void setCreatorOrgNodeName(String name_)
    {
        this.creatorOrgNodeName = name_;
    }

    /**
     * Gets the location attribute of the TestAdminVO object
     *
     * @return The location value
     */
    public String getLocation()
    {
        return this.location;
    }

    /**
     * Sets the location attribute of the TestAdminVO object
     *
     * @param location_ The new location value
     */
    public void setLocation(String location_)
    {
        this.location = location_;
    }

    /**
     * Gets the loginStartDate attribute of the TestAdminVO object
     *
     * @return The loginStartDate value
     */
    public Calendar getLoginStartDate()
    {
        return this.loginStartDate;
    }

    /**
     * Sets the loginStartDate attribute of the TestAdminVO object
     *
     * @param loginStartDate_ The new loginStartDate value
     */
    public void setLoginStartDate(Calendar loginStartDate_)
    {
        this.loginStartDate = loginStartDate_;
    }

    /**
     * @return String for loginBeginTime value
     */
    public String getLoginBeginTime()
    {
        return this.loginBeginTime;
    }

    /**
     * @param loginBeginTime_ The new loginBeginTime value
     */
    public void setLoginBeginTime(String loginBeginTime_)
    {
        this.loginBeginTime = loginBeginTime_;
    }

    /**
     * Gets the loginEndDate attribute of the TestAdminVO object
     *
     * @return The loginEndDate value
     */
    public Calendar getLoginEndDate()
    {
        return this.loginEndDate;
    }

    /**
     * Sets the loginEndDate attribute of the TestAdminVO object
     *
     * @param loginEndDate_ The new loginEndDate value
     */
    public void setLoginEndDate(Calendar loginEndDate_)
    {
        this.loginEndDate = loginEndDate_;
    }

    /**
     * @return String of loginFinishTime value
     */
    public String getLoginFinishTime()
    {
        return this.loginFinishTime;
    }

    /**
     * @param loginFinishTime_ The new loginFinishTime value
     */
    public void setLoginFinishTime(String loginFinishTime_)
    {
        this.loginFinishTime = loginFinishTime_;
    }

    /**
     * @return String value for timezoneDescription
     */
    public String getTimeZone()
    {
        return this.timezoneDescription;
    }

    /**
     * Sets the timezoneDescription attribute of the TestAdminVO
     * object
     *
     * @param timezoneDescription_ The new timezoneDescription value
     */
    public void setTimeZone(String timezoneDescription_)
    {
        this.timezoneDescription = timezoneDescription_;
    }

    /**
     * Gets the accessCode attribute of the TestAdminVO object
     *
     * @return The accessCode value
     */
    public String getAccessCode()
    {
        return this.accessCode;
    }

    /**
     * Sets the accessCode attribute of the TestAdminVO object
     *
     * @param accessCode_ The new accessCode value
     */
    public void setAccessCode(String accessCode_)
    {
        this.accessCode = accessCode_;
    }

    /**
     * Gets the enforceTimeLimit attribute of the TestAdminVO object
     *
     * @return The enforceTimeLimit value
     */
    public boolean getEnforceTimeLimit()
    {
        return this.enforceTimeLimit;
    }

    /**
     * Sets the enforceTimeLimit attribute of the TestAdminVO object
     *
     * @param enforceTimeLimit_ The new enforceTimeLimit value
     */
    public void setEnforceTimeLimit(boolean enforceTimeLimit_)
    {
        this.enforceTimeLimit = enforceTimeLimit_;
    }

    /**
     * Sets the enforceTimeLimit attribute of the TestAdminVO object
     *
     * @param enforceTimeLimit_ The new enforceTimeLimit value
     */
    public void setEnforceTimeLimit(Boolean enforceTimeLimit_)
    {
        if (enforceTimeLimit_ == null)
        {
            this.enforceTimeLimit = false;
        }
        else
        {
            this.enforceTimeLimit = enforceTimeLimit_.booleanValue();
        }
    }

    /**
     * Gets the enforceTutorial attribute of the TestAdminVO object
     *
     * @return The enforceTutorial value
     */
    public boolean getEnforceTutorial()
    {
        return this.enforceTutorial;
    }

    /**
     * Sets the enforceTutorial attribute of the TestAdminVO object
     *
     * @param b The new enforceTutorial value
     */
    public void setEnforceTutorial(boolean b)
    {
        this.enforceTutorial = b;
    }

    /**
     * Gets the enforceBreak attribute of the TestAdminVO object
     *
     * @return The enforceBreak value
     */
    public boolean getEnforceBreak()
    {
        return this.enforceBreak;
    }

    /**
     * Sets the enforceBreak attribute of the TestAdminVO object
     *
     * @param b The new enforceBreak value
     */
    public void setEnforceBreak(boolean b)
    {
        this.enforceBreak = b;
    }

    /**
     * Gets the createdDateTime attribute of the TestAdminVO object
     *
     * @return The createdDateTime value
     */
    public Date getCreatedDateTime()
    {
        return this.createdDateTime;
    }

    /**
     * Sets the createdDateTime attribute of the TestAdminVO object
     *
     * @param date_ The new createdDateTime value
     */
    public void setCreatedDateTime(Date date_)
    {
        this.createdDateTime = date_;
    }

    /**
     * Gets the customerId attribute of the TestAdminVO object
     *
     * @return The customerId value
     */
    public Integer getCustomerId()
    {
        return this.customerId;
    }

    /**
     * Sets the customerId attribute of the TestAdminVO object
     *
     * @param customerId_ The new customerId value
     */
    public void setCustomerId(Integer customerId_)
    {
        this.customerId = customerId_;
    }

    /**
     * Gets the dailyLoginEndTime attribute of the TestAdminVO object
     *
     * @return The dailyLoginEndTime value
     */
    public Date getDailyLoginEndTime()
    {
        return this.dailyLoginEndTime;
    }

    /**
     * Sets the dailyLoginEndTime attribute of the TestAdminVO object
     *
     * @param date_ The new dailyLoginEndTime value
     */
    public void setDailyLoginEndTime(Date date_)
    {
        this.dailyLoginEndTime = date_;
    }

    /**
     * Gets the dailyLoginStartTime attribute of the TestAdminVO
     * object
     *
     * @return The dailyLoginStartTime value
     */
    public Date getDailyLoginStartTime()
    {
        return this.dailyLoginStartTime;
    }

    /**
     * Sets the dailyLoginStartTime attribute of the TestAdminVO
     * object
     *
     * @param date_ The new dailyLoginStartTime value
     */
    public void setDailyLoginStartTime(Date date_)
    {
        this.dailyLoginStartTime = date_;
    }

    /**
     * Gets the tutorialId attribute of the TestAdminVO object
     *
     * @return The tutorialId value
     */
    public Integer getTutorialId()
    {
        return this.tutorialId;
    }

    /**
     * Sets the tutorialId attribute of the TestAdminVO object
     *
     * @param id_ The new tutorialId value
     */
    public void setTutorialId(Integer id_)
    {
        this.tutorialId = id_;
    }

    /**
     * Gets the updatedBy attribute of the TestAdminVO object
     *
     * @return The updatedBy value
     */
    public Integer getUpdatedBy()
    {
        return this.updatedBy;
    }

    /**
     * Sets the updatedBy attribute of the TestAdminVO object
     *
     * @param id_ The new updatedBy value
     */
    public void setUpdatedBy(Integer id_)
    {
        this.updatedBy = id_;
    }

    /**
     * Gets the updatedDateTime attribute of the TestAdminVO object
     *
     * @return The updatedDateTime value
     */
    public Date getUpdatedDateTime()
    {
        return this.updatedDateTime;
    }

    /**
     * Sets the updatedDateTime attribute of the TestAdminVO object
     *
     * @param date_ The new updatedDateTime value
     */
    public void setUpdatedDateTime(Date date_)
    {
        this.updatedDateTime = date_;
    }


    //--------------------------------------------------------------------------
    // Derived/BUSINESS methods
    //
    // NOTE: these below should be moved to testAdminDetailVO!
    //
    //--------------------------------------------------------------------------

    /**
     * Gets the haveStudentsCompletedTest attribute of the
     * TestAdminVO object
     *
     * @return The haveStudentsCompletedTest value
     */
    public boolean getHaveStudentsCompletedTest()
    {
        return this.haveStudentsCompletedTest;
    }

    /**
     * Sets the haveStudentsCompletedTest attribute of the
     * TestAdminVO object
     *
     * @param haveStudentsCompletedTest_ The new haveStudentsCompletedTest value
     */
    public void setHaveStudentsCompletedTest(boolean haveStudentsCompletedTest_)
    {
        this.haveStudentsCompletedTest = haveStudentsCompletedTest_;
    }

    public void setHaveStudentsLoggedIn(boolean haveStudentsLoggedIn) {
        this.haveStudentsLoggedIn = haveStudentsLoggedIn;
    }
    public boolean getHaveStudentsLoggedIn() {
        return haveStudentsLoggedIn;
    }


    //-------------------------------------------------------------------helpers

    /**
     * Helper for <code>TestAdminType</code> property.
     *
     * @return <code>True</code> for Tutorial Session, <code>False</code>
     *      otherwise.
     */
    public boolean isTutorial()
    {
        if (this.testAdminType.equals(TestAdminVO.TYPE_TUTORIAL_SESSION))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * Interrogates the TestAdminStatus property of the TestAdminVO object
     * @return <code>true</code> if TestAdminStatus is of the "active" testSession category.
     */
    public boolean isTestAdminStatusActive()
    {
        return( isTestAdminStatusActive(this.getTestAdminStatus() ));
    }

    /**
     * NOTE:
     * 1.  currently STATUS-ACTIVE is a subset of CURRENT,
     * 2.  this is NOT anythign to do with ACTIVATION indicator.
     *
     * @param testAdminStatus_ String code value of the <code>testAdminStatus</code> to be intorrogated.
     * @return <code>true</code> if TestAdminStatus is of the "active" testSession category<br>
     *         -some students have logged in and the TestSession is Current from the<br>
     *          loginTimeWindow perspective.
     */
    public static boolean isTestAdminStatusActive( String testAdminStatus_ )
    {
        return ( testAdminStatus_.equals( TestAdminVO.STATUS_ACTIVE ));
    }


    /**
     * Interrogates the TestAdminStatus attribute of the TestAdminVO object
     * @return <code>true</code> if TestAdminStatus is of the "current" testSession category.
     */
    public boolean isTestAdminStatusCurrent()
    {
        return( isTestAdminStatusCurrent(this.getTestAdminStatus() ));
    }

    /**
     * @param testAdminStatus_ String code value of the <code>testAdminStatus</code> to be intorrogated.
     * @return <code>true</code> if TestAdminStatus is of the "current" testSession category<br>
     *         -current from the loginTimeWindow perspective.
     *         -AND there are NO students have logged in<br>; i.e CURRENT but not ACTIVE
     */
    public static boolean isTestAdminStatusCurrent( String testAdminStatus_ )
    {
        return ( testAdminStatus_.equals( TestAdminVO.STATUS_CURRENT ));
    }


    /**
     * Interrogates the TestAdminStatus attribute of the TestAdminVO object
     * @return <code>true</code> if TestAdminStatus is of the "future" testSession category.
     */
    public boolean isTestAdminStatusFuture()
    {
        return( isTestAdminStatusFuture(this.getTestAdminStatus() ));
    }

    /**
     * @param testAdminStatus_ String code value of the <code>testAdminStatus</code> to be intorrogated.
     * @return <code>true</code> if TestAdminStatus is of the "future" testSession category<br>
     *         i.e. based on the loginTimeWindow.
     */
    public static boolean isTestAdminStatusFuture( String testAdminStatus_ )
    {
        return ( testAdminStatus_.equals( TestAdminVO.STATUS_FUTURE ));
    }


    /**
     * Interrogates the TestAdminStatus attribute of the TestAdminVO object
     * @return <code>true</code> if TestAdminStatus is of the "past" testSession category.
     */
    public boolean isTestAdminStatusPast()
    {
        return( isTestAdminStatusPast(this.getTestAdminStatus() ));
    }

    /**
     * @param testAdminStatus_ String code value of the <code>testAdminStatus</code> to be intorrogated.
     * @return <code>true</code> if TestAdminStatus is of the "past" testSession category<br>
     *         i.e. based on the loginTimeWindow.
     */
    public static boolean isTestAdminStatusPast( String testAdminStatus_ )
    {
        if( testAdminStatus_.equals( TestAdminVO.STATUS_PAST ))
            return( true );
        else
            return( false );
    }
    
    public boolean equals(Object obj_){
    	boolean result = false;
    	try{
    		TestAdminVO other = (TestAdminVO)obj_;
    		String otherTestAdminId = other.getTestAdminId();
    		result = this.testAdminId.equals(otherTestAdminId);
    	}
    	catch (Exception e){
    	}
    	return result;
    }
}
/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:40  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:47:40  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.7  2005/05/03 21:26:11  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.5.12.1.2.4  2004/10/02 17:05:48  vraravam
 * Removed unused imports.
 *
 * Revision 1.5.12.1.2.3  2004/09/28 00:02:56  arathore
 * age category
 *
 * Revision 1.5.12.1.2.2  2004/08/25 00:42:00  arathore
 * norms
 *
 * Revision 1.5.12.1.2.1  2004/08/17 22:01:41  binkley
 * Globally organize imports (removed 1,000+ warnings).
 *
 * Revision 1.5.12.1  2004/05/27 19:34:41  jbecker
 * improve get test admins for user performance - defect CQDTS00025231
 *
 * Revision 1.5  2003/04/13 21:38:08  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.4  2003/04/07 22:49:16  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.3.2.5  2003/04/08 23:43:41  kgawetsk
 * Added more static helpers for the isTestAdminStatusXXX( String).
 *
 * Revision 1.3.2.4  2003/04/08 23:20:41  kgawetsk
 * Created static helpers for isTestAdminStatusActive( String ).
 *
 * Revision 1.3.2.3  2003/04/08 22:21:15  kgawetsk
 * Added isTestAdminStatusXXX() helper methods.
 *
 * Revision 1.3.2.2  2003/04/07 21:40:50  ncohen
 * testAdministrationPager and viewAllTestSessionsPager must properly set
 * new 'haveStudentsLoggedIn' attribute on TestAdminVO per value on TestRoster
 * entity - JSPs with test session pagers should show 'my reports' link for admins
 * with corresponding partial completion status. CQ defect CQDTS00015653
 *
 * Revision 1.3.2.1  2003/03/25 03:26:33  kgawetsk
 * Updated comments.
 *
 */
