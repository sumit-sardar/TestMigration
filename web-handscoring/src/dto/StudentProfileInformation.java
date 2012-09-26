package dto; 

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import utils.DateUtils;
import utils.OptionList;
import utils.StringUtils;

import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.OrganizationNode;
import com.ctb.util.web.sanitizer.SanitizedFormField;

/**
 * 
 */
public class StudentProfileInformation extends SanitizedFormField
{ 
    static final long serialVersionUID = 1L;
    
    private Integer studentId;
    private String userName;
    private String firstName;
    private String middleName;
    private String middleInitial;
    private String lastName;
    private String gender;
    private String grade;
    private String studentNumber;
    private String studentSecondNumber;
    private Date birthdate;
    private String displayName;    
    private OrganizationNode[] organizationNodes;
    private String  orgNodeNamesStr;
    //START- (LLO82) StudentManagement Changes For LasLink product
    private String testPurpose;
    //END- (LLO82) StudentManagement Changes For LasLink product

    private Integer createBy;
    private OptionList optionList;
    private List stuDemographic;
    private StudentAccommodationsDetail stuAccommodation;
    private String birthdateString;
    private String month;
    private String day;
    private String year;
    
    
    private String screenMagnifier;
    private String screenReader;
    private String calculator; 
    private String testPause;
    private String untimedTest;
    private String questionBackgroundColor;
    private String questionFontColor;
    private String questionFontSize;
    private String answerBackgroundColor;
    private String answerFontColor;
    private String answerFontSize;
    private String colorFontAccommodation;
    private String colorFont;//bulk accommodation new field
    private String highlighter;
    private String maskingRuler;//Added for masking
    private String auditoryCalming;//Added for auditory calming
    private String musicFile;//Added for auditory calming
    private String magnifyingGlass;//Added for magnifier
    private String extendedTime; // Added for student pacing
    private String maskingTool;
    
    private String hasAccommodations = "No";
    
    private String orgNodeName;
    private Integer orgNodeId;
    private String orgIdList;
    private String orgNameList;
    
    //Added for out of school
    private String outOfSchool;
    
    //Added for new UI hand scoring
    private String testCatalogName;
    private String testAdminId;
    private Integer rosterId;
    private String testSessionName;
    private String studentName;
    private String accessCode;
    private Integer itemSetIdTC;
    private Integer itemCountCRAI;
    private String scoringStatus;

    /**
	 * @return the hasAccommodations
	 */
	public String getHasAccommodations() {
		return hasAccommodations;
	}


	/**
	 * @param hasAccommodations the hasAccommodations to set
	 */
	public void setHasAccommodations(String hasAccommodations) {
		this.hasAccommodations = hasAccommodations;
	}


	/**
	 * @return the screenMagnifier
	 */
	public String getScreenMagnifier() {
		return screenMagnifier;
	}


	/**
	 * @param screenMagnifier the screenMagnifier to set
	 */
	public void setScreenMagnifier(String screenMagnifier) {
		this.screenMagnifier = screenMagnifier;
	}


	/**
	 * @return the scoringStatus
	 */
	public String getScoringStatus() {
		return scoringStatus;
	}


	/**
	 * @param scoringStatus the scoringStatus to set
	 */
	public void setScoringStatus(String scoringStatus) {
		this.scoringStatus = scoringStatus;
	}


	/**
	 * @return the screenReader
	 */
	public String getScreenReader() {
		return screenReader;
	}


	/**
	 * @param screenReader the screenReader to set
	 */
	public void setScreenReader(String screenReader) {
		this.screenReader = screenReader;
	}


	/**
	 * @return the calculator
	 */
	public String getCalculator() {
		return calculator;
	}


	/**
	 * @param calculator the calculator to set
	 */
	public void setCalculator(String calculator) {
		this.calculator = calculator;
	}


	/**
	 * @return the testPause
	 */
	public String getTestPause() {
		return testPause;
	}


	/**
	 * @param testPause the testPause to set
	 */
	public void setTestPause(String testPause) {
		this.testPause = testPause;
	}


	/**
	 * @return the untimedTest
	 */
	public String getUntimedTest() {
		return untimedTest;
	}


	/**
	 * @param untimedTest the untimedTest to set
	 */
	public void setUntimedTest(String untimedTest) {
		this.untimedTest = untimedTest;
	}


	/**
	 * @return the questionBackgroundColor
	 */
	public String getQuestionBackgroundColor() {
		return questionBackgroundColor;
	}


	/**
	 * @param questionBackgroundColor the questionBackgroundColor to set
	 */
	public void setQuestionBackgroundColor(String questionBackgroundColor) {
		this.questionBackgroundColor = questionBackgroundColor;
	}


	/**
	 * @return the questionFontColor
	 */
	public String getQuestionFontColor() {
		return questionFontColor;
	}


	/**
	 * @param questionFontColor the questionFontColor to set
	 */
	public void setQuestionFontColor(String questionFontColor) {
		this.questionFontColor = questionFontColor;
	}


	/**
	 * @return the questionFontSize
	 */
	public String getQuestionFontSize() {
		return questionFontSize;
	}


	/**
	 * @param questionFontSize the questionFontSize to set
	 */
	public void setQuestionFontSize(String questionFontSize) {
		this.questionFontSize = questionFontSize;
	}


	/**
	 * @return the answerBackgroundColor
	 */
	public String getAnswerBackgroundColor() {
		return answerBackgroundColor;
	}


	/**
	 * @param answerBackgroundColor the answerBackgroundColor to set
	 */
	public void setAnswerBackgroundColor(String answerBackgroundColor) {
		this.answerBackgroundColor = answerBackgroundColor;
	}


	/**
	 * @return the answerFontColor
	 */
	public String getAnswerFontColor() {
		return answerFontColor;
	}


	/**
	 * @param answerFontColor the answerFontColor to set
	 */
	public void setAnswerFontColor(String answerFontColor) {
		this.answerFontColor = answerFontColor;
	}


	/**
	 * @return the answerFontSize
	 */
	public String getAnswerFontSize() {
		return answerFontSize;
	}


	/**
	 * @param answerFontSize the answerFontSize to set
	 */
	public void setAnswerFontSize(String answerFontSize) {
		this.answerFontSize = answerFontSize;
	}


	/**
	 * @return the colorFontAccommodation
	 */
	public String getColorFontAccommodation() {
		return colorFontAccommodation;
	}


	/**
	 * @param colorFontAccommodation the colorFontAccommodation to set
	 */
	public void setColorFontAccommodation(String colorFontAccommodation) {
		this.colorFontAccommodation = colorFontAccommodation;
	}


	/**
	 * @return the colorFont
	 */
	public String getColorFont() {
		return colorFont;
	}


	/**
	 * @param colorFont the colorFont to set
	 */
	public void setColorFont(String colorFont) {
		this.colorFont = colorFont;
	}


	/**
	 * @return the highlighter
	 */
	public String getHighlighter() {
		return highlighter;
	}


	/**
	 * @param highlighter the highlighter to set
	 */
	public void setHighlighter(String highlighter) {
		this.highlighter = highlighter;
	}


	/**
	 * @return the maskingRuler
	 */
	public String getMaskingRuler() {
		return maskingRuler;
	}


	/**
	 * @param maskingRuler the maskingRuler to set
	 */
	public void setMaskingRuler(String maskingRuler) {
		this.maskingRuler = maskingRuler;
	}


	/**
	 * @return the auditoryCalming
	 */
	public String getAuditoryCalming() {
		return auditoryCalming;
	}


	/**
	 * @param auditoryCalming the auditoryCalming to set
	 */
	public void setAuditoryCalming(String auditoryCalming) {
		this.auditoryCalming = auditoryCalming;
	}


	/**
	 * @return the musicFile
	 */
	public String getMusicFile() {
		return musicFile;
	}


	/**
	 * @param musicFile the musicFile to set
	 */
	public void setMusicFile(String musicFile) {
		this.musicFile = musicFile;
	}


	/**
	 * @return the magnifyingGlass
	 */
	public String getMagnifyingGlass() {
		return magnifyingGlass;
	}


	/**
	 * @param magnifyingGlass the magnifyingGlass to set
	 */
	public void setMagnifyingGlass(String magnifyingGlass) {
		this.magnifyingGlass = magnifyingGlass;
	}


	/**
	 * @return the extendedTime
	 */
	public String getExtendedTime() {
		return extendedTime;
	}


	/**
	 * @param extendedTime the extendedTime to set
	 */
	public void setExtendedTime(String extendedTime) {
		this.extendedTime = extendedTime;
	}


	/**
	 * @return the maskingTool
	 */
	public String getMaskingTool() {
		return maskingTool;
	}


	/**
	 * @param maskingTool the maskingTool to set
	 */
	public void setMaskingTool(String maskingTool) {
		this.maskingTool = maskingTool;
	}


	public StudentProfileInformation() {
        this.studentId = new Integer(0);
        this.userName = "";
        this.firstName = "";
        this.middleName = "";
        this.middleInitial = "";
        this.lastName = "";
        this.displayName = "";
        this.gender = "";
        this.grade = "";
        this.studentNumber = "";
        this.studentSecondNumber = "";
        this.birthdate = null;        
        this.organizationNodes = null;
        
        this.birthdateString = "";
        this.month = "";
        this.day = "";
        this.year = "";

        this.createBy = null;
        //START- (LLO82) StudentManagement Changes For LasLink product
        this.testPurpose = "";
        //END- (LLO82) StudentManagement Changes For LasLink product
        
        
    }
        
        
    public StudentProfileInformation(ManageStudent student) {
        this.studentId = student.getId();
        this.userName = student.getLoginId();
        this.firstName = student.getFirstName();
        this.middleName = student.getMiddleName();
        if(student.getMiddleName() != null && !student.getMiddleName().equals(""))
        	this.middleInitial = student.getMiddleName().substring(0,1);
        this.lastName = student.getLastName();
        this.displayName = student.getStudentName();
        this.createBy = student.getCreatedBy();
        
        this.gender = student.getGender();
        if (this.gender == null) this.gender = "Unknown";
        else
        if (this.gender.equals("M")) this.gender = "Male";
        else
        if (this.gender.equals("F")) this.gender = "Female";
        else
        if (this.gender.equals("U")) this.gender = "Unknown";
        
        this.grade = student.getGrade();
        if (this.grade == null) this.grade = "";
        //GACR005  For Defect # 65786
        this.studentNumber = student.getStudentIdNumber()!= null ? student.getStudentIdNumber().trim() : "";
        this.studentSecondNumber = student.getStudentIdNumber2() != null ? student.getStudentIdNumber2().trim() : "";
        //GACR005   For Defect # 65786
        this.birthdate = student.getBirthDate(); 
        if (this.birthdate != null) {
            this.birthdateString = DateUtils.formatDateToDateString(this.birthdate, DateUtils.DATE_FORMAT_CHAR);     
            StringTokenizer tokenizer = new StringTokenizer(this.birthdateString, "/");
            this.month = tokenizer.nextToken();
            this.day = tokenizer.nextToken();
            this.year = tokenizer.nextToken();
            this.birthdateString = DateUtils.formatDateToDateString(this.birthdate, DateUtils.DATE_FORMAT_DISPLAY);     
        }
        
        this.organizationNodes = student.getOrganizationNodes();
        this.orgNodeNamesStr = getOrgNodeNamesString();
        //START- (LLO82) StudentManagement Changes For LasLink product
        if(student.getTestPurpose() != null) {
        	this.testPurpose = student.getTestPurpose();
        	if (this.testPurpose.equals("1")) this.testPurpose = "Initial Placement";
            else
            if (this.testPurpose.equals("2")) this.testPurpose = "Annual Assessment";
        }
       //END- (LLO82) StudentManagement Changes For LasLink product
        this.screenMagnifier = student.getScreenMagnifier();
        this.screenReader = student.getScreenReader();
        this.calculator= student.getCalculator();
        this.testPause = student.getTestPause();
        this.untimedTest = student.getUntimedTest();
        this.questionBackgroundColor = student.getQuestionBackgroundColor();
        this.questionFontColor = student.getQuestionFontColor();
        this.questionFontSize = student.getQuestionFontSize();
        this.answerBackgroundColor = student.getAnswerBackgroundColor();
        this.answerFontColor = student.getAnswerFontColor();
        this.answerFontSize = student.getAnswerFontSize();
        this.highlighter = student.getHighlighter();
        this.maskingRuler = student.getMaskingRuler();
        this.auditoryCalming = student.getAuditoryCalming();
        this.magnifyingGlass = student.getMagnifyingGlass();
        this.extendedTime = student.getExtendedTime();
        this.maskingTool = student.getMaskingTool();
        this.hasAccommodations = studentHasAccommodation();
        this.orgNodeId = student.getOrgNodeId();
        this.orgNodeName = student.getOrgNodeName();
        this.orgIdList = student.getOrgIdList();
        this.orgNameList = student.getOrgNameList();
        this.outOfSchool = student.getOutOfSchool();
        if(orgNameList!=null && orgNameList.length()>0){
        	this.orgNodeNamesStr = orgNameList.replace('|', ',');
        }
        this.testCatalogName = student.getTestCatalogName();
        this.testAdminId = student.getTestAdminId();
        this.rosterId = student.getRosterId();
        this.testSessionName = student.getTestSessionName();
        this.studentName = student.getStudentName();
        this.accessCode = student.getAccessCode();
        this.itemSetIdTC = student.getItemSetIdTC();
        this.itemCountCRAI = student.getItemCountCRAI();
        this.scoringStatus = student.getScoringStatus();
    }
    

    public StudentProfileInformation(ManageStudent student,
			Map<Integer, Map> accomodationMap) {
    	Map innerMap = new HashMap();
    	this.studentId = student.getId();
        this.userName = student.getLoginId();
        this.firstName = student.getFirstName();
        this.middleName = student.getMiddleName();
        if(student.getMiddleName() != null && !student.getMiddleName().equals(""))
        	this.middleInitial = student.getMiddleName().substring(0,1);
        this.lastName = student.getLastName();
        this.displayName = student.getStudentName();
        this.createBy = student.getCreatedBy();
        
        this.gender = student.getGender();
        if (this.gender == null) this.gender = "Unknown";
        else
        if (this.gender.equals("M")) this.gender = "Male";
        else
        if (this.gender.equals("F")) this.gender = "Female";
        else
        if (this.gender.equals("U")) this.gender = "Unknown";
        
        this.grade = student.getGrade();
        if (this.grade == null) this.grade = "";
        //GACR005  For Defect # 65786
        this.studentNumber = student.getStudentIdNumber()!= null ? student.getStudentIdNumber().trim() : "";
        this.studentSecondNumber = student.getStudentIdNumber2() != null ? student.getStudentIdNumber2().trim() : "";
        //GACR005   For Defect # 65786
        this.birthdate = student.getBirthDate(); 
        if (this.birthdate != null) {
            this.birthdateString = DateUtils.formatDateToDateString(this.birthdate, DateUtils.DATE_FORMAT_CHAR);     
            StringTokenizer tokenizer = new StringTokenizer(this.birthdateString, "/");
            this.month = tokenizer.nextToken();
            this.day = tokenizer.nextToken();
            this.year = tokenizer.nextToken();
            this.birthdateString = DateUtils.formatDateToDateString(this.birthdate, DateUtils.DATE_FORMAT_DISPLAY);     
        }
        
        this.organizationNodes = student.getOrganizationNodes();
        this.orgNodeNamesStr = getOrgNodeNamesString();
        //START- (LLO82) StudentManagement Changes For LasLink product
        if(student.getTestPurpose() != null) {
        	this.testPurpose = student.getTestPurpose();
        	if (this.testPurpose.equals("1")) this.testPurpose = "Initial Placement";
            else
            if (this.testPurpose.equals("2")) this.testPurpose = "Annual Assessment";
        }
       //END- (LLO82) StudentManagement Changes For LasLink product
        this.screenMagnifier = student.getScreenMagnifier();
        this.screenReader = student.getScreenReader();
        this.calculator= student.getCalculator();
        this.testPause = student.getTestPause();
        this.untimedTest = student.getUntimedTest();
        this.questionBackgroundColor = student.getQuestionBackgroundColor();
        this.questionFontColor = student.getQuestionFontColor();
        this.questionFontSize = student.getQuestionFontSize();
        this.answerBackgroundColor = student.getAnswerBackgroundColor();
        this.answerFontColor = student.getAnswerFontColor();
        this.answerFontSize = student.getAnswerFontSize();
        this.highlighter = student.getHighlighter();
        this.maskingRuler = student.getMaskingRuler();
        //this.auditoryCalming = student.getAuditoryCalming();
        this.musicFile = student.getMusicFile();
        if(StringUtils.isNullEmpty(this.musicFile)){
        	this.auditoryCalming = "F";
        }else {
        	this.auditoryCalming = "T";
        }
        this.magnifyingGlass = student.getMagnifyingGlass();
        this.extendedTime = student.getExtendedTime();
        this.maskingTool = student.getMaskingTool();
        this.hasAccommodations = studentHasAccommodation();
        this.orgNodeId = student.getOrgNodeId();
        this.orgNodeName = student.getOrgNodeName();
        this.orgIdList = student.getOrgIdList();
        this.orgNameList = student.getOrgNameList();
        this.outOfSchool = student.getOutOfSchool();
        innerMap.put("screenMagnifier", this.screenMagnifier);
        innerMap.put("screenReader", this.screenReader);
        innerMap.put("calculator", this.calculator);
        innerMap.put("testPause", this.testPause);
        innerMap.put("untimedTest", this.untimedTest);
        innerMap.put("maskingRuler", this.maskingRuler);
        innerMap.put("magnifyingGlass", this.magnifyingGlass);
        innerMap.put("maskingTool", this.maskingTool);
        innerMap.put("extendedTime", this.extendedTime);
        innerMap.put("highlighter", this.highlighter);
        innerMap.put("auditoryCalming", this.auditoryCalming);
	    if(StringUtils.isNullEmpty(this.questionBackgroundColor) 
	    			&& StringUtils.isNullEmpty(this.questionFontColor)
	    			&& StringUtils.isNullEmpty(this.questionFontSize)
	    			&& StringUtils.isNullEmpty(this.answerBackgroundColor)
	    			&& StringUtils.isNullEmpty(this.answerFontColor)
	    			&& StringUtils.isNullEmpty(this.answerFontSize)){
    	  innerMap.put("colorFont","F");
        }else {
    	  innerMap.put("colorFont","T");
        }
       
        
        accomodationMap.put(studentId, innerMap);
        
        if(orgNameList!=null && orgNameList.length()>0){
        	this.orgNodeNamesStr = orgNameList.replace('|', ',');
        }
	}


	public StudentProfileInformation createClone() {
        StudentProfileInformation copied = new StudentProfileInformation();
        
        copied.setStudentId(this.studentId);
        copied.setUserName(this.userName);
        copied.setFirstName(this.firstName);
        copied.setMiddleName(this.middleName);
        copied.setLastName(this.lastName);
        copied.setGender(this.gender);
        copied.setGrade(this.grade);
        copied.setStudentNumber(this.studentNumber);
        copied.setStudentSecondNumber(this.studentSecondNumber);
        copied.setBirthdate(this.birthdate);
        copied.setOrganizationNodes(this.organizationNodes);
        //START- (LLO82) StudentManagement Changes For LasLink product
        if(testPurpose != null) {
        	copied.setTestPurpose(this.testPurpose);
        }
        //END- (LLO82) StudentManagement Changes For LasLink product
        return copied;       
    }
  
    public ManageStudent makeCopy(Integer studentId, List selectedOrgNodes) {
        ManageStudent copied = new ManageStudent();
        
        copied.setId(studentId);
        copied.setLoginId(this.userName);
        copied.setFirstName( upperCaseFirstLetter(this.firstName) );
        copied.setMiddleName( upperCaseFirstLetter(this.middleName) );
        copied.setLastName( upperCaseFirstLetter(this.lastName) );
        copied.setGrade(this.grade);
        copied.setStudentIdNumber(this.studentNumber.trim());
        copied.setStudentIdNumber2(this.studentSecondNumber.trim());
        //GACRCT2010CR007 - changed for creating date when supplied.
        Date date = null;
        
        if (DateUtils.allSelected(month, day, year)) 
        	 date = DateUtils.createDate(this.year, this.month, this.day);
        
        copied.setBirthDate(date);
        
        if (this.gender.equals("Male")) copied.setGender("M");
        else if (this.gender.equals("Female")) copied.setGender("F");
        else copied.setGender("U");

        /*OrganizationNode[] orgNodes = new OrganizationNode[ selectedOrgNodes.size() ];        
        for (int i=0 ; i<selectedOrgNodes.size() ; i++) {
            PathNode node = (PathNode)selectedOrgNodes.get(i);
            OrganizationNode orgNode = new OrganizationNode();
            orgNode.setOrgNodeId(node.getId());
            orgNodes[i] = orgNode;
        }*/
        OrganizationNode[] orgNodes = new OrganizationNode[ selectedOrgNodes.size() ];        
        for (int i=0 ; i<selectedOrgNodes.size() ; i++) {
            OrganizationNode orgNode = new OrganizationNode();
            orgNode.setOrgNodeId((Integer)selectedOrgNodes.get(i));
            orgNodes[i] = orgNode;
        }
        copied.setOrganizationNodes(orgNodes);  
        //START- (LLO82) StudentManagement Changes For LasLink product
        if(testPurpose != null) {
        	if (this.testPurpose.equals("Initial Placement")) copied.setTestPurpose("1");
        	else if (this.testPurpose.equals("Annual Assessment")) copied.setTestPurpose("2");
        }
        //END- (LLO82) StudentManagement Changes For LasLink product
        return copied;       
    }
    
	/**
	 * @return Returns the studentId.
	 */
	public Integer getStudentId() {
		return this.studentId != null ? this.studentId : new Integer(0);
	}
	/**
	 * @param studentId The studentId to set.
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return this.displayName != null ? this.displayName : "";
	}
	/**
	 * @param displayName 
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return Returns the studentNumber.
	 */
	public String getStudentNumber() {
		return this.studentNumber != null ? this.studentNumber : "";
	}
	/**
	 * @param studentNumber The extElmId to set.
	 */
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	/**
	 * @return Returns the studentSecondNumber.
	 */
	public String getStudentSecondNumber() {
		return this.studentSecondNumber != null ? this.studentSecondNumber : "";
	}
	/**
	 * @param studentSecondNumber The extElmId to set.
	 */
	public void setStudentSecondNumber(String studentSecondNumber) {
		this.studentSecondNumber = studentSecondNumber;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return this.firstName != null ? this.firstName : "";
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
        this.firstName = firstName;
	}
	/**
	 * @return Returns the gender.
	 */
	public String getGender() {
        if (this.gender != null)        
            return this.gender;
        else
            return "";
	}
	/**
	 * @param gender The gender to set.
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return Returns the grade.
	 */
	public String getGrade() {
        if (this.grade != null)
            return this.grade;
        else
            return "";
	}
	/**
	 * @param grade The grade to set.
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return this.lastName != null ? this.lastName : "";
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
        this.lastName = lastName;
	}
	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return this.middleName != null ? this.middleName : "";
	}
	/**
	 * @param middleName The middleName to set.
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return this.userName != null ? this.userName : "";
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return Returns the birthdate.
	 */
	public Date getBirthdate() {
		return this.birthdate;
	}
	/**
	 * @param birthdate The birthdate to set.
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	/**
	 * @return Returns the birthdateString.
	 */
	public String getBirthdateString() {
		return this.birthdateString != null ? this.birthdateString : "";
	}
	/**
	 * @param birthdateString The birthdateString to set.
	 */
	public void setBirthdateString(String birthdateString) {
		this.birthdateString = birthdateString;
	}   
	/**
	 * @return Returns the createBy.
	 */
	public Integer getCreateBy() {
		return this.createBy != null ? this.createBy : new Integer(0);
	}
	/**
	 * @param createBy The createBy to set.
	 */
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
     
	/**
	 * @return Returns the month.
	 */
	public String getMonth() {
		return this.month != null ? this.month : "";
	}
	/**
	 * @param month The month to set.
	 */
	public void setMonth(String month) {
		this.month = month;
	}    
	/**
	 * @return Returns the day.
	 */
	public String getDay() {
		return this.day != null ? this.day : "";
	}
	/**
	 * @param day The day to set.
	 */
	public void setDay(String day) {
		this.day = day;
	}    
	/**
	 * @return Returns the year.
	 */
	public String getYear() {
		return this.year != null ? this.year : "";
	}
	/**
	 * @param year The year to set.
	 */
	public void setYear(String year) {
		this.year = year;
	}    
	/**
	 * @return Returns the organizationNodes.
	 */
	public OrganizationNode[] getOrganizationNodes() {
		return this.organizationNodes;
	}
	/**
	 * @param organizationNodes The organizationNodes to set.
	 */
	public void setOrganizationNodes(OrganizationNode[] organizationNodes) {
		this.organizationNodes = organizationNodes;
	}
	/**
	 * @return Returns the getOrgNodeNamesString.
	 */
	public String getOrgNodeNamesString() {
        String str = "";
        if(this.organizationNodes != null && this.organizationNodes.length > 0) {
	        for (int i=0 ; i<this.organizationNodes.length ; i++) {
	            OrganizationNode node = this.organizationNodes[i];
	            str = str + node.getOrgNodeName();
	            if (i < (this.organizationNodes.length - 1))
	                str = str + ", ";
	        }
        }
        return str;
	}  
	
	public String studentHasAccommodation(){
		 String hasAccommodations = "No";
	        if( "T".equals(this.screenMagnifier) ||
	            "T".equals(this.screenReader) ||
	            "T".equals(this.calculator) ||
	            "T".equals(this.testPause) ||
	            "T".equals(this.untimedTest) ||
	            "T".equals(this.highlighter) ||
	            "T".equals(this.extendedTime) ||
	            (this.maskingRuler != null && !this.maskingRuler.equals("") && !this.maskingRuler.equals("F"))||
	            (this.extendedTime != null && !this.extendedTime.equals("") && !this.extendedTime.equals("F")) || 
	            (this.auditoryCalming != null && !this.auditoryCalming.equals("") && !this.auditoryCalming.equals("F")) || 
	            (this.magnifyingGlass != null && !this.magnifyingGlass.equals("") && !this.magnifyingGlass.equals("F")) || 
	            (this.maskingTool != null && !this.maskingTool.equals("") && !this.maskingTool.equals("F")) || 
	            this.questionBackgroundColor != null ||
	            this.questionFontColor != null ||
	            this.questionFontSize != null ||
	            this.answerBackgroundColor != null ||
	            this.answerFontColor != null ||
	            this.answerFontSize != null) {
	        	hasAccommodations = "Yes";
	       
	   }
	   return hasAccommodations;
	}
	/**
	 * upperCaseFirstLetter
	 */
	public String upperCaseFirstLetter(String str) {
        if (str != null && !str.equals("")) {
            str = str.trim();
            if (str.length() <= 1) {
                str = str.toUpperCase();
            }
            else {
                String firstLetter = str.substring(0,1).toUpperCase();
                String otherLetters = str.substring(1);
                str = new StringBuffer().append(firstLetter).append(otherLetters).toString();
            }
        }
        return str;
	}


	/**
	 * @return the testPurpose
	 */
	public String getTestPurpose() {
		return testPurpose;
	}


	/**
	 * @param testPurpose the testPurpose to set
	 */
	public void setTestPurpose(String testPurpose) {
		this.testPurpose = testPurpose;
	}


	/**
	 * @return the orgNodeNamesStr
	 */
	public String getOrgNodeNamesStr() {
		return orgNodeNamesStr;
	}


	/**
	 * @param orgNodeNamesStr the orgNodeNamesStr to set
	 */
	public void setOrgNodeNamesStr(String orgNodeNamesStr) {
		this.orgNodeNamesStr = orgNodeNamesStr;
	}

	/**
	 * @return the optionList
	 */
	public OptionList getOptionList() {
		return optionList;
	}

	/**
	 * @param optionList the optionList to set
	 */
	public void setOptionList(OptionList optionList) {
		this.optionList = optionList;
	}


	/**
	 * @return the stuDemographic
	 */
	public List getStuDemographic() {
		return stuDemographic;
	}


	/**
	 * @param stuDemographic the stuDemographic to set
	 */
	public void setStuDemographic(List stuDemographic) {
		this.stuDemographic = stuDemographic;
	}


	/**
	 * @return the stuAccommodation
	 */
	public StudentAccommodationsDetail getStuAccommodation() {
		return stuAccommodation;
	}


	/**
	 * @param stuAccommodation the stuAccommodation to set
	 */
	public void setStuAccommodation(StudentAccommodationsDetail stuAccommodation) {
		this.stuAccommodation = stuAccommodation;
	}


	public String getOrgNodeName() {
		return orgNodeName;
	}


	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}


	public Integer getOrgNodeId() {
		return orgNodeId;
	}


	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}


	
	/**
	 * @return the orgIdList
	 */
	public String getOrgIdList() {
		return orgIdList;
	}


	
	/**
	 * @param orgIdList the orgIdList to set
	 */
	public void setOrgIdList(String orgIdList) {
		this.orgIdList = orgIdList;
	}


	
	/**
	 * @return the orgNameList
	 */
	public String getOrgNameList() {
		return orgNameList;
	}


	
	/**
	 * @param orgNameList the orgNameList to set
	 */
	public void setOrgNameList(String orgNameList) {
		this.orgNameList = orgNameList;
	}


	public String getOutOfSchool() {
		return outOfSchool;
	}


	public void setOutOfSchool(String outOfSchool) {
		this.outOfSchool = outOfSchool;
	}


	public String getMiddleInitial() {
		return middleInitial;
	}


	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}


	public String getTestCatalogName() {
		return testCatalogName;
	}


	public void setTestCatalogName(String testCatalogName) {
		this.testCatalogName = testCatalogName;
	}


	public String getTestAdminId() {
		return testAdminId;
	}


	public void setTestAdminId(String testAdminId) {
		this.testAdminId = testAdminId;
	}


	public Integer getRosterId() {
		return rosterId;
	}


	public void setRosterId(Integer rosterId) {
		this.rosterId = rosterId;
	}


	public String getTestSessionName() {
		return testSessionName;
	}


	public void setTestSessionName(String testSessionName) {
		this.testSessionName = testSessionName;
	}


	public String getStudentName() {
		return studentName;
	}


	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}


	public String getAccessCode() {
		return accessCode;
	}


	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}


	public Integer getItemSetIdTC() {
		return itemSetIdTC;
	}


	public void setItemSetIdTC(Integer itemSetIdTC) {
		this.itemSetIdTC = itemSetIdTC;
	}


	public Integer getItemCountCRAI() {
		return itemCountCRAI;
	}


	public void setItemCountCRAI(Integer itemCountCRAI) {
		this.itemCountCRAI = itemCountCRAI;
	}
} 
