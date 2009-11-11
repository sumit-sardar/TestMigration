package com.ctb.lexington.data;

/*
 * StudentVO.java
 *
 * Still Needs to be refactored into StudentDetailVO
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

//java
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ctb.lexington.domain.teststructure.CompletionStatus;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.RosterStatus;
import com.ctb.lexington.util.RosterTestCompletionStatus;

/**
 * @author <a href="mailto:Krys_Gawetski@ctb.com">Krys Gawetski</a>
 * @version
 */
public class StudentVO extends Object implements java.io.Serializable, java.lang.Cloneable
{
    public  static final String VO_LABEL       = "com.ctb.lexington.data.StudentVO";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    private Integer studentId  = null;
    private String  firstName  = null;
    private String  lastName   = null;
    private String  middleName = null;

    private String  userName      = null;
    private String  password      = null;
    private String  preferredName = null;
    private String  prefix        = null;

    private String  suffix    = null;
    private Calendar birthdate = Calendar.getInstance();
    private String  gender    = null;
    private String  ethnicity = null;

    private String  email    = null;
    private String  grade    = "AD";
    private String  extElmId = null;
    private String  extPin1  = null;

    private String  extPin2             = null;
    private String  extPin3             = null;
    private String  activeSession       = null;
    private String  potentialDuplicated = null;

    private Integer createdBy       = null;
    private Date    createdDateTime = null;
    private Integer updatedBy       = null;
    private Date    updatedDateTime = null;

    private String activationStatus = null;
    private String extSchoolId      = null;
    private String birthDateMonth   = null;
    private String birthDateDay     = null;
    private String birthDateYear    = null;

    //--------------------------------------------------------------------------
    // Derived/BUSINESS fields - not a data property on respective entity bean
    //
    // NOTE: these below should be moved to StudentDetailVO!
    //
    //--------------------------------------------------------------------------
    private Integer testRosterId         = null;//testRoster specific... ejb beware!
    private String  testCompletionStatus = null;//testRoster specific... ejb beware!
    private String  validationStatus     = null;//testRoster specific... ejb beware!
    private String  captureMethod        = null;//testRoster specific->need RosterId

    private StudentContactVO studentContact = null;
    private List 			 nodeList 	 	= null;
    private List 			 groupList 		= null;
    private Integer 		 orgNodeId 		= null;
    
    private boolean srOnly = false;
    

    /**
     * <code>True</code> if the current user can associate/disassociate this student from TA Session roster.<br>
     * <code>False</code> otherwise; e.g. for Proctor user, who cannot modifiy students outside his/her<br>
     * <code>OrgNode</code> ownership (i.e. above own OrgNode).
     */
    private boolean modifiableByUser = false; //is not modifiable by a user.
    private boolean isAllowRemoved = true;


    /** Creates new StudentVO */
    public StudentVO() {}

    public Object clone(){
        try
        {
            return super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            return null;
        }
    }

    /**
     * Simple setters/getters...
     */
    public Integer getStudentId()                     { return this.studentId; }
    public void    setStudentId( Integer studentId_ ) { this.studentId = studentId_; }

    public String getFirstName()
    {
        if ( this.firstName == null ) return "";
        else return this.firstName ;
    }
    public void   setFirstName( String firstName_ )
    {
        if (firstName_ != null && !firstName_.equals(""))
        {
            firstName_ = firstName_.trim();
            if (firstName_.length() >= 2)
            {
                String firstLetter = firstName_.substring(0,1).toUpperCase();
                String otherLetters = firstName_.substring(1);
                this.firstName = new StringBuffer().append(firstLetter).append(otherLetters).toString();
            }
            else
            {
                this.firstName = firstName_;
            }
        }
    }

    public String getLastName()
    {
        if ( this.lastName == null ) return "";
        else return this.lastName ;
    }
    public void   setLastName( String lastName_ )
    {
        if (lastName_ != null && !lastName_.equals(""))
        {
            lastName_ = lastName_.trim();
            if (lastName_.length() >= 2)
            {
                String firstLetter = lastName_.substring(0,1).toUpperCase();
                String otherLetters = lastName_.substring(1);
                this.lastName = new StringBuffer().append(firstLetter).append(otherLetters).toString();
            }
            else
            {
                this.lastName = lastName_;
            }
        }
    }


    public String getMiddleName()
    {
        if ( this.middleName == null ) return "";
        else return this.middleName;
    }
    public void   setMiddleName( String s )
    {
        if ( s != null )
        {
            this.middleName = s.trim();
        }
    }

    public String getUserName()
    {
        if ( this.userName == null )
            return "";
        else return this.userName;
    }
    public void   setUserName  (String s)
    {
        if ( s != null )
        {
            this.userName = s.trim();
        }
    }

    public String getPassword()         { return this.password; }
    public void   setPassword(String s) { this.password = s; }

    public String getPreferredName()
    {
        if ( this.preferredName == null ) return "";
        else return this.preferredName ;
    }
    public void   setPreferredName(String s)
    {
        if ( s != null )
        {
            this.preferredName = s.trim();
        }
    }

    public String getPrefix()
    {
        if ( this.prefix == null ) return "";
        else return this.prefix ;
    }
    public void   setPrefix(String s){ this.prefix = s; }

    public String getSuffix()
    {
        if ( this.suffix == null ) return "";
        else return this.suffix ;
    }
    public void   setSuffix(String s)
    {
        if ( s != null )
        {
            this.suffix = s.trim();
        }
    }

    public boolean getIsAllowRemoved() { return this.isAllowRemoved; }
    public void setIsAllowRemoved(boolean isAllowRemoved_) { this.isAllowRemoved = isAllowRemoved_; }

    /**
     * Get a date object from the bean instance.  This method will be
     * used to populate the database with a Date object.  Individual
     * date parts (i.e. month, day, year) are stored in separate
     * getter/setter so that we can preselect individual date elements
     * in the JSP pages.
     *
     * @return Date The value of the property.
     */

    public Calendar getBirthdate()
    {
        // Since the Date/Calendar objects are zero based for months, the
        // individual date getter setters used in the drop down boxes range from
        // 0 - 11.  To correct for this, the indiv month field must be incremented
        // by one before sending back to the EJB/Database layer.
        if (this.birthdate != null) {
            this.birthdate.set( Integer.parseInt(this.getBirthDateYear()),
                                Integer.parseInt(this.getBirthDateMonth()),
                                Integer.parseInt(this.getBirthDateDay()));
        }
        return this.birthdate;
    }

    /**
     * Used by the EJB components to set the individual String date elements
     * from a Calendar object.
     *
     * @param dateOfBirth_ The value to set the property to.
     */
    public void setBirthdate(Calendar dateOfBirth_)
    {
        this.birthdate = dateOfBirth_;
        if (dateOfBirth_ != null) {
            this.setBirthDateMonth(Integer.toString(dateOfBirth_.get(Calendar.MONTH)));
            this.setBirthDateDay(Integer.toString(dateOfBirth_.get(Calendar.DAY_OF_MONTH)));
            this.setBirthDateYear(Integer.toString(dateOfBirth_.get(Calendar.YEAR)));
        }
    }

    public String getBirthDateMonth() { return this.birthDateMonth; }
    public void   setBirthDateMonth(String birthDateMonth_){
        if (birthDateMonth_ != null) this.birthDateMonth = birthDateMonth_.trim();
    }

    public String getBirthDateDay(){ return this.birthDateDay; }
    public void   setBirthDateDay(String birthDateDay_){
        if (birthDateDay_ != null) this.birthDateDay = birthDateDay_.trim();
    }

    public String getBirthDateYear(){ return this.birthDateYear; }
    public void   setBirthDateYear(String birthDateYear_) {
        if (birthDateYear_ != null) this.birthDateYear = birthDateYear_.trim();
    }

    public void   setGender(String s){ this.gender = s; }
    public String getGender()
    {
        if ( this.gender == null ) return "";
        else return this.gender ;
    }

    public void   setEthnicity(String s){ this.ethnicity = s; }
    public String getEthnicity()
    {
        if ( this.ethnicity == null ) return "";
        else return this.ethnicity ;
    }

    public void   setEmail(String s){ this.email = s; }
    public String getEmail()
    {
        if ( this.email == null ) return "";
        else return this.email ;
    }

    public void   setGrade(String s){ this.grade = s; }

    public String getGrade()
    {
        if ( grade !=null && this.grade.equals("AD") )
            return "Adult";
        else
            return this.grade ;
    }

    public String getGradeUnmodified(){
        return grade;
    }

    public void   setExtElmId(String s){ this.extElmId = s; }
    public String getExtElmId()
    {
        if ( this.extElmId == null ) return "";
        else return this.extElmId ;
    }

    public void   setExtPin1(String s){ this.extPin1 = s; }
    public String getExtPin1()
    {
        if ( this.extPin1 == null ) return "";
        else return this.extPin1 ;
    }

    public void   setExtPin2(String s){ this.extPin2 = s; }
    public String getExtPin2()
    {
        if ( this.extPin2 == null ) return "";
        else return this.extPin2 ;
    }

    public void   setExtPin3(String s){ this.extPin3 = s; }
    public String getExtPin3()
    {
        if ( this.extPin3 == null ) return "";
        else return this.extPin3 ;
    }

    public String getActiveSession()        { return this.activeSession ; }
    public void   setActiveSession(String s){ this.activeSession = s; }

    public String getPotentialDuplicated()        { return this.potentialDuplicated ; }
    public void   setPotentialDuplicated(String s){ this.potentialDuplicated = s; }

    public Integer getCreatedBy()         { return this.createdBy ; }
    public void    setCreatedBy(Integer i){ this.createdBy = i; }

    public Date getCreatedDateTime()      { return this.createdDateTime ; }
    public void setCreatedDateTime(Date d){ this.createdDateTime = d; }

    public Integer getUpdatedBy()         { return this.updatedBy ; }
    public void    setUpdatedBy(Integer i){ this.updatedBy = i; }

    public Date getUpdatedDateTime()      { return this.updatedDateTime ; }
    public void setUpdatedDateTime(Date d){ this.updatedDateTime = d; }

    public void   setActivationStatus(String s){ this.activationStatus = s; }
    public String getActivationStatus()
    {
        return this.activationStatus ;
    }

    public void   setExtSchoolId(String s){ this.extSchoolId = s; }
    public String getExtSchoolId()
    {
        if ( this.extSchoolId == null ) return "";
        else return this.extSchoolId ;
    }

    public Integer getOrgNodeId() 			  { return this.orgNodeId; }
    public void    setOrgNodeId(Integer arg1) { this.orgNodeId = arg1; }

    //--------------------------------------------------------------------------
    // Derived/BUSINESS methods
    //
    // NOTE: these below should be moved to testAdminDetailVO!
    //
    //--------------------------------------------------------------------------

    public StudentContactVO getStudentContact(){ return this.studentContact; }
    public void             setStudentContact(StudentContactVO studentContact_){
        this.studentContact = studentContact_;
    }

    public List getNodeList(){ return this.nodeList; }
    public void setNodeList(List nodeList_) {
        //System.out.println("setNodeList called with null?" + (nodeList_ == null));
        this.nodeList = nodeList_;
    }

    /**
     * Returns the nodeNamesList.
     * @return List
     */
    public List getNodeNamesList()
    {
        List nodeNameList = new ArrayList();
        if ( this.nodeList != null )
        {
            List nodeNames = new ArrayList();
            Iterator nodesIt = this.nodeList.iterator();
            while ( nodesIt.hasNext() )
            {
                NodeVO node = (NodeVO) nodesIt.next();
                nodeNames.add(node.getOrgNodeName());
            }
            nodeNameList = nodeNames;
        }
        return nodeNameList;
    }


    public List getGroupList(){ return this.groupList; }
    public void setGroupList(List groupList_){
        this.groupList = groupList_;
    }


    /**
     * Sets the nodeNamesList.
     */
/*    public void setNodeNamesList(List nodeNamesList) {
        this.nodeNamesList = nodeNamesList;
    }
*/
    public boolean getModifiableByUser()         { return this.modifiableByUser ; }
    public void    setModifiableByUser(boolean b){ this.modifiableByUser = b; }


    /**
     * @return <code>testRosterId<code> for the TestAdmin associated for this Student.
     */
    public Integer getTestRosterId()                        { return this.testRosterId; }
    public void    setTestRosterId( Integer testRosterId_ ) { this.testRosterId = testRosterId_; }

    /**
     * @return Values enumerated in TestRosterLocal.testCompletionStatus!
     * @see TestRosterLocal and testCompletionStatus inner class.
     */
    public String getTestCompletionStatus()        { return this.testCompletionStatus ; }
    public void   setTestCompletionStatus(String s){
        if(s != null)
            this.testCompletionStatus = s.trim();
    }

    /**
     * @return Values enumerated in TestRosterLocal.testCompletionStatus!
     * @see TestRosterLocal and testCompletionStatus inner class.
     */
    public String getCaptureMethod() { 
    	return this.captureMethod ; 
    }
    public void   setCaptureMethod(String s){
        if(s != null)
            this.captureMethod = s.trim();
    }

    /**
     * Returns the validationStatus.
     * @return String
     */
    public String getValidationStatus() {
        return this.validationStatus;
    }
    /**
     * Returns the validationStatus.
     */
    public void setValidationStatus( String validationStatus ) {
        this.validationStatus = validationStatus;
    }
    
    public boolean isSrOnly() {
    	return this.srOnly;
    }
    
    public void setSrOnly(boolean srOnly) {
    	this.srOnly = srOnly;
    }

    //-------------------------------------------------------------------Helpers

    /**
     * Gives the name for the testCompletionStatus value/code.
     * @return String <code>Name</code> of testCompletionStatus.
     */
    public String getTestCompletionStatusName() {
        return ( CompletionStatus.getByCode(this.getTestCompletionStatus()).getDescription());
    }

    /**
     * Checks if the Bean property is indicating that student IS logged in to take a test.
     * @return <code>true</code> in such cases; <code>false</code> otherwise.
     */
    public boolean isStudentLoggedIn()
    {
        return( RosterTestCompletionStatus.
                 isStudentLoggedIn( this.getTestCompletionStatus() ));
    }

    /**
     * Checks if the Entity Bean (Local) property is indicating that:<br>
     * 1. student HAS logged in to take a test<br>
     * NOTE: COmpleted might be without student loggin/taking a test.
     * @return <code>true</code> in such cases; <code>false</code> otherwise.
     */
    public boolean hasStudentEverLoggedIn() {
        return( RosterStatus.hasStudentEverLoggedIn( this.getCaptureMethod() ));
    }

    /**
     * Checks if the Bean property is indicating that student HAS logged in to take a test<br>
     *        or Completed (e.g. via Key ENtry).
     * @return <code>true</code> in such cases; <code>false</code> otherwise.
     */
    public boolean hasLoggedInOrCompleted()
    {
        return( RosterTestCompletionStatus.
                hasLoggedInOrCompleted( this.getTestCompletionStatus() ));
    }

    /**
     * Checks if the Bean property is indicating that student HAS logged in to take a test.
     * @return <code>true</code> in such cases; <code>false</code> otherwise.
     */
    public boolean haveSomeResponses()
    {
        if(this.getCaptureMethod() != null && !this.getCaptureMethod().trim().equals("")) {
            return true;
        } else {
            return ( RosterTestCompletionStatus.
                 hasSomeResponses( this.getTestCompletionStatus() ));
        }
    }


    /**
     * Helper method.
     * Checks if the student has <b>never taken</b> the test.<br>
     * @return <code>true</code> if <b>testCompletionStatus</b> indicates student "never Taken Test" category; <code>false</code> otherwise.
     */
    public boolean haveNeverTakenTest() {
        return ( RosterTestCompletionStatus.
                 haveNeverTakenTest( this.getTestCompletionStatus() ));
    }

    /**
     * Checks if the student has <b>taken</b>, <b>finished</b>, and
     * got <b><u>scored</u></b>the test.<br>
     * (i.e. All <b>responses</b> are available, and <b>scoring</b> is at least in progress (if not completed).
     */
    public boolean haveCompletedTest() {
        return( RosterTestCompletionStatus.
                haveCompletedTest( this.getTestCompletionStatus() ));
    }

    /**
    * Helper method for rosterValidationStatus.
    * @return TRUE if getValidationStatus is (equal to) VALID!
    */
    public boolean getIsValid() {
        return (this.getValidationStatus().equals( CTBConstants.VALID ) || this.getValidationStatus().equals( CTBConstants.PARTIAL ));
    }

    public String getFormattedBirthdate(){
        if(birthDateDay == null || birthDateMonth == null || birthDateYear == null)
            return "";
        return new SimpleDateFormat("MM-dd-yyyy").format(new Date(birthdate.getTimeInMillis()));

    }

    public boolean equals(Object obj) {
        if (obj instanceof StudentVO) {
            StudentVO o2 = (StudentVO) obj;
            if (o2.getFirstName().equals(this.firstName) &&
                o2.getLastName().equals(this.lastName) &&
                o2.getStudentId().equals(this.studentId)) {
                return true;
            }
        }
        return false;
    }

}