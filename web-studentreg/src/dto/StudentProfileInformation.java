package dto; 

import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.OrganizationNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import utils.DateUtils;

/**
 * 
 */
public class StudentProfileInformation implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;
    
    private Integer studentId;
    private String userName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String grade;
    private String studentNumber;
    private String studentSecondNumber;
    private Date birthdate;
    private String displayName;    
    private OrganizationNode[] organizationNodes;

    private Integer createBy;

    private String birthdateString;
    private String month;
    private String day;
    private String year;

    private String selectable;
    
    public StudentProfileInformation() {
        this.studentId = new Integer(0);
        this.userName = "";
        this.firstName = "";
        this.middleName = "";
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
        this.selectable = "true";
    }
        
        
    public StudentProfileInformation(ManageStudent student) {
        this.studentId = student.getId();
        this.userName = student.getLoginId();
        this.firstName = student.getFirstName();
        this.middleName = student.getMiddleName();
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
        
        this.studentNumber = student.getStudentIdNumber();
        this.studentSecondNumber = student.getStudentIdNumber2();
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
        this.selectable = "true";
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
        copied.setSelectable(this.selectable);
        
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
        copied.setStudentIdNumber(this.studentNumber);
        copied.setStudentIdNumber2(this.studentSecondNumber);    
        
        Date date = DateUtils.createDate(this.year, this.month, this.day);
        copied.setBirthDate(date);
        
        if (this.gender.equals("Male")) copied.setGender("M");
        else 
        if (this.gender.equals("Female")) copied.setGender("F");
        else 
        if (this.gender.equals("Unknown")) copied.setGender("U");
        else
        copied.setGender(null);
        
        OrganizationNode[] orgNodes = new OrganizationNode[ selectedOrgNodes.size() ];        
        for (int i=0 ; i<selectedOrgNodes.size() ; i++) {
            PathNode node = (PathNode)selectedOrgNodes.get(i);
            OrganizationNode orgNode = new OrganizationNode();
            orgNode.setOrgNodeId(node.getId());
            orgNodes[i] = orgNode;
        }
        copied.setOrganizationNodes(orgNodes);    
            
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
        for (int i=0 ; i<this.organizationNodes.length ; i++) {
            OrganizationNode node = this.organizationNodes[i];
            str = str + node.getOrgNodeName();
            if (i < (this.organizationNodes.length - 1))
                str = str + ", ";
        }
        return str;
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
	 */
	public void setSelectable(String selectable) {
		this.selectable = selectable;
	}    
	/**
	 */
	public String getSelectable() {
		return this.selectable;
	}
} 
