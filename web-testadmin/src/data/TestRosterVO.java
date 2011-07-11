package data; 

import com.ctb.bean.testAdmin.RosterElement;

public class TestRosterVO implements java.io.Serializable 
{
    static final long serialVersionUID = 1L;

    private Integer testRosterId = null;
    private Integer studentId = null;
    private String firstName = null;
    private String middleName = null;
    private String lastName = null;
    private String studentNumber = null;
    private String loginName = null;
    private String password = null;
    private String validationStatus = null;
    private String testStatus = null;
    private String[] accommodations = null;
    private String form = null;
    private int seq = 0;
    private boolean hasPause = false;
    private String hasAccommodations = null;
    private String screenReader = null;
    private String calculator = null;
    private String hasColorFontAccommodations = null;
    private String testPause = null;
    private String untimedTest = null;
    private String highLighter = null; /* 51931 Deferred Defect For HighLighter*/
    private String extendedTimeAccom = null; // added for student pacing
    
    public TestRosterVO()
    {
    }
    public TestRosterVO(RosterElement rElt)
    {
        this.testRosterId = rElt.getTestRosterId();
        this.studentId = rElt.getStudentId();
        this.firstName = rElt.getFirstName();
        this.middleName = rElt.getMiddleName();
        this.lastName = rElt.getLastName();
        this.studentNumber = rElt.getExtPin1();
        this.loginName = rElt.getUserName();
        this.password = rElt.getPassword();
        this.validationStatus = rElt.getValidationStatus();
        this.testStatus = rElt.getTestCompletionStatus();
        this.form = rElt.getFormAssignment();
    }  
    public String getForm(){
        return this.form;
    }
    public void setForm(String form){
        this.form = form;
    }
    public String getHasAccommodations(){
        return this.hasAccommodations;
    }
    public void setHasAccommodations(String hasAccommodations){
        this.hasAccommodations = hasAccommodations;
    }
    public String getScreenReader(){
        return this.screenReader;
    }
    public void setScreenReader(String screenReader){
        this.screenReader = screenReader;
    }
    public String getCalculator(){
        return this.calculator;
    }
    public void setCalculator(String calculator){
        this.calculator = calculator;
    }
    public String getHasColorFontAccommodations(){
        return this.hasColorFontAccommodations;
    }
    public void setHasColorFontAccommodations(String hasColorFontAccommodations){
        this.hasColorFontAccommodations = hasColorFontAccommodations;
    }
    public String getTestPause(){
        return this.testPause;
    }
    public void setTestPause(String testPause){
        this.testPause = testPause;
    }
    public String getUntimedTest(){
        return this.untimedTest;
    }
    public void setUntimedTest(String untimedTest){
        this.untimedTest = untimedTest;
    }
    public boolean getHasPause(){
        return this.hasPause;
    }
    public void setHasPause(boolean hasPause){
        this.hasPause = hasPause;
    }
    public int getSeq(){
        return this.seq;
    }  
    public void setSeq (int seq){
        this.seq = seq;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public Integer getTestRosterId() {
        return testRosterId;
    }
    public void setTestRosterId(Integer id) {
        this.testRosterId = id;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getStudentId() {
        return studentId;
    }
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
    public String getStudentNumber() {
        return studentNumber;
    }
    public void setStudentId(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    public String getTestStatus() {
        return this.testStatus;
    }
    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }
    public String getValidationStatus() {
        return this.validationStatus;
    }
    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }
    public String[] getAccommodations() {
        return this.accommodations;
    }
    public void setAccommodations(String[] accommodations) {
        this.accommodations = accommodations;
    }
    
    /* 51931 Deferred Defect For HighLighter*/
    public void setHighLighter(String highLighter){
        this.highLighter = highLighter;
    }
    public String getHighLighter(){
        return this.highLighter;
    }
    
    // Start- added for student pacing
	/**
	 * @return the extendedTimeAccom
	 */
	public String getExtendedTimeAccom() {
		return extendedTimeAccom;
	}
	/**
	 * @param extendedTimeAccom the extendedTimeAccom to set
	 */
	public void setExtendedTimeAccom(String extendedTimeAccom) {
		this.extendedTimeAccom = extendedTimeAccom;
	}
	// end- added for student pacing
} 
