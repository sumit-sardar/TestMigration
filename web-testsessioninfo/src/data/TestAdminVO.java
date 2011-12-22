package data; 

import java.util.Date;
import java.util.List;

import com.ctb.util.web.sanitizer.SanitizedFormField;

public class TestAdminVO extends SanitizedFormField
{ 
    static final long serialVersionUID = 1L;

    private String sessionName = null;
    private String testName = null;
    private String location = null;
    private String accessCode = null;
    private String level = null;
    private Date startDate = null;
    private Date endDate = null;
    private Date startTime = null;
    private Date endTime = null;
    private String timeZone = null;
    private String timeLimit = null;
    private String enforceBreaks = null;
    private String tutorial = null;
    private List subtests = null;
    private Integer productId = null;
    private Integer itemSetId = null;
    //Added for Random Distractor
    private String isRandomize = null; 
    private String displayRandomize = null;
    
    
    // Added for Random Distractor
    public String getDisplayRandomize () {
        
        return this.isRandomize != null && !this.isRandomize.trim().equals("") ? 
            this.isRandomize.equals("Y")? "Yes":"No":null;
             
    }
   
    
    public TestAdminVO() {}
    
    public TestAdminVO(String testName, 
                       String location, 
                       String accessCode) {
        this.testName = testName;
        this.location = location;
        this.accessCode = accessCode;
    }

    public TestAdminVO(String sessionName,
                       String testName, 
                       String location, 
                       String accessCode,
                       String level,
                       Date startDate,
                       Date endDate,
                       Date startTime,
                       Date endTime,
                       String timeZone,
                       String timeLimit,
                       String enforceBreaks,
                       //added for random distracter
                       String isRandomize,
                       String tutorial,
                       List subtests,
                       Integer productId) {
        this.sessionName = sessionName;
        this.testName = testName;
        this.location = location;
        this.accessCode = accessCode;
        this.level = level;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeZone = timeZone;
        this.timeLimit = timeLimit;
        this.enforceBreaks = enforceBreaks;
        this.isRandomize = isRandomize;
        this.tutorial = tutorial;
        this.subtests = subtests;
        this.productId = productId;
    }

    public TestAdminVO(TestAdminVO src) {
        this.sessionName = src.getSessionName();
        this.testName = src.getTestName();
        this.location = src.getLocation();
        this.accessCode = src.getAccessCode();
        this.level = src.getLevel();
        this.startDate = src.getStartDate();
        this.endDate = src.getEndDate();
        this.startTime = src.getStartTime();
        this.endTime = src.getEndTime();
        this.timeZone = src.getTimeZone();
        this.timeLimit = src.getTimeLimit();
        this.enforceBreaks = src.getEnforceBreaks();
        this.isRandomize = src.getIsRandomize();
        this.tutorial = src.getTutorial();
        this.subtests = src.getSubtests();
        this.productId = src.getProductId();
        this.itemSetId = src.getItemSetId();
    }

    public String getSessionName() {
        if (this.sessionName != null)
            this.sessionName = this.sessionName.trim();
        return this.sessionName;
    }
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
    public String getTestName() {
        if (this.testName != null)
            this.testName = this.testName.trim();
        return this.testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public String getLocation() {
        if (this.location != null)
            this.location = this.location.trim();
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }   
    public String getAccessCode() {
        if (this.accessCode != null)
            this.accessCode = this.accessCode.trim();
        return this.accessCode;
    }
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }   
    public String getLevel() {
        return this.level;
    }
    public void setLevel(String level) {
        this.level = level;
    }   
    public Date getStartDate() {
        return this.startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }   
    public Date getEndDate() {
        return this.endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }   
    public Date getStartTime() {
        return this.startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }   
    public Date getEndTime() {
        return this.endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }   
    public String getTimeZone() {
        return this.timeZone;
    }
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }   
    public String getTimeLimit() {
        return this.timeLimit;
    }
    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }   
    public String getEnforceBreaks() {
        return this.enforceBreaks;
    }
    public void setEnforceBreaks(String enforceBreaks) {
        this.enforceBreaks = enforceBreaks;
    }   
    public String getTutorial() {
        return this.tutorial;
    }
    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }   
    public List getSubtests() {
        return this.subtests;
    }
    public void setSubtests(List subtests) {
        this.subtests = subtests;
    }   
    public Integer getProductId() {
        return this.productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }   

    public Integer getItemSetId() {
        return this.itemSetId;
    }
    public void setItemSetId(Integer itemSetId) {
        this.itemSetId = itemSetId;
    }   
    
    /**
	 * @return Returns the IsRandomize.
	 */
    
    public String getIsRandomize() {
		 return this.isRandomize;
	}
    
    /**
	 * @param IsRandomize The IsRandomize to set.
	 */
	public void setIsRandomize(String isRandomize) {
           this.isRandomize = isRandomize;
    }
    
    


} 
