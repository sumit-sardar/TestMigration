package dto;

import com.ctb.bean.testAdmin.Student;
import com.ctb.util.web.sanitizer.SanitizedFormField;

public class StudentProfileInformation extends SanitizedFormField
{ 
	static final long serialVersionUID = 1L;

	private Integer studentId;
	private String studentLoginId="";
	private String studentFirstName;
	private String studentLastName;
	private String studentMiddleName;
	private String testAccessCode="";
	private String studentPreferredName;
	private String studentExternalId;

	private Organization associatedOrganization = new Organization();

	//default constructor
	public StudentProfileInformation() {

	}

	public StudentProfileInformation(Student student) {
		
		this.studentId = student.getStudentId();
		this.studentLoginId = student.getUserName();
		this.studentFirstName = student.getFirstName();
		this.studentMiddleName = student.getMiddleName();
		this.studentLastName = student.getLastName();
		this.studentPreferredName = student.getPreferredName();
		this.studentExternalId = student.getExtPin1();

	}



	/**
	 * createClone
	 */
	public StudentProfileInformation createClone() {
		StudentProfileInformation copied = new StudentProfileInformation();

		copied.setStudentId(this.studentId);
		copied.setStudentLoginId(this.studentLoginId);
		copied.setStudentFirstName(this.studentFirstName);
		copied.setStudentLastName(this.studentLastName);
		copied.setStudentMiddleName(this.studentMiddleName);
		copied.setTestAccessCode(this.testAccessCode);
		return copied;       
	}
	/**
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the studentLoginId
	 */
	public String getStudentLoginId() {
		return studentLoginId;
	}
	/**
	 * @param studentLoginId the studentLoginId to set
	 */
	public void setStudentLoginId(String studentLoginId) {
		this.studentLoginId = studentLoginId;
	}
	/**
	 * @return the studentFirstName
	 */
	public String getStudentFirstName() {
		return studentFirstName;
	}
	/**
	 * @param studentFirstName the studentFirstName to set
	 */
	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}
	/**
	 * @return the studentLastName
	 */
	public String getStudentLastName() {
		return studentLastName;
	}
	/**
	 * @param studentLastName the studentLastName to set
	 */
	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}
	/**
	 * @return the associatedOrganization
	 */
	public Organization getAssociatedOrganization() {
		return associatedOrganization;
	}
	/**
	 * @param associatedOrganization the associatedOrganization to set
	 */
	public void setAssociatedOrganization(Organization associatedOrganization) {
		this.associatedOrganization = associatedOrganization;
	}
	/**
	 * @return the studentMiddleName
	 */
	public String getStudentMiddleName() {
		return studentMiddleName;
	}
	/**
	 * @param studentMiddleName the studentMiddleName to set
	 */
	public void setStudentMiddleName(String studentMiddleName) {
		this.studentMiddleName = studentMiddleName;
	}
	public String getTestAccessCode() {
		return testAccessCode;
	}
	public void setTestAccessCode(String testAccessCode) {
		this.testAccessCode = testAccessCode;
	}  
    /**
	 * @return the studentPreferredName
	 */
	public String getStudentPreferredName() {
		return studentPreferredName;
	}

	/**
	 * @param studentPreferredName the studentPreferredName to set
	 */
	public void setStudentPreferredName(String studentPreferredName) {
		this.studentPreferredName = studentPreferredName;
	}

	/**
	 * @return the studentExternalId
	 */
	public String getStudentExternalId() {
		return studentExternalId;
	}

	/**
	 * @param studentExternalId the studentExternalId to set
	 */
	public void setStudentExternalId(String studentExternalId) {
		this.studentExternalId = studentExternalId;
	}


}


