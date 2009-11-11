package com.ctb.lexington.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.Stringx;

/**
 * <p>Title: ScanStudentVO</p>
 * <p>Description: holds an individual student's scanned test responses</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Nate Cohen
 * @version 1.0
 */

public class ScanStudentVO implements Serializable, Comparable{

    public  static final String VO_LABEL       = "com.ctb.lexington.data.ScanStudentVO";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    //  statuses
    public static final String STUDENT_STATUS_PENDING_VALIDATION  = "PV";
    public static final String STUDENT_STATUS_PENDING_MATCHING    = "PM";
    public static final String STUDENT_STATUS_ERROR_NO_RESPONSES  = "ER";
    public static final String STUDENT_STATUS_ERROR_MATCHING      = "EM";
    public static final String STUDENT_STATUS_HIDDEN              = "HN";
    public static final String STUDENT_STATUS_SUCCESS             = "SS";

    public ScanStudentVO() {
    }
    private Integer scanStudentId;
    private Integer scanHeaderId;
    private Integer scanStudentIndex;
    private Integer studentId;
    private Date studentIdModDate;
    private Integer studentIdModUserId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String gender;
    private String studentNumber;
    private String itemResponses;
    private String scanStudentStatus;
    private String birthMonth;
    private String birthYear;
    private String birthDay;
    private Integer numResponsesNotPersisted;
    private Integer rosterId;
    private Integer rosterIdModUserId;
    private Date rosterIdModDate;
    private String grade;
    
    // extra detail not in SCAN_STUDENT table
    // eg not populated by ScanStudentLocalHome finders or TOAssembler
    // this info is populated by retrieve
    private String fileName;
    private String testAccessCode;
    private String uploadUserName;
    private Date uploadDate;
    
    public String getGrade(){
    	return grade;
    }
    public void setGrade(String grade_){
    	this.grade = grade_;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName_) {
        this.fileName = fileName_;
    }
    public String getTestAccessCode() {
        return testAccessCode;
    }
    public void setTestAccessCode(String testAccessCode_) {
        this.testAccessCode = testAccessCode_;
    }
    public String getUploadUserName() {
        return uploadUserName;
    }
    public void setUploadUserName(String uploadUserName_) {
        this.uploadUserName = uploadUserName_;
    }
    public Date getUploadDate() {
        return uploadDate;
    }
    public void setUploadDate(Date uploadDate_) {
        this.uploadDate = uploadDate_;
    }
    public Integer getScanStudentId() {
        return scanStudentId;
    }
    public void setScanStudentId(Integer scanStudentId) {
        this.scanStudentId = scanStudentId;
    }
    public void setScanHeaderId(Integer scanHeaderId) {
        this.scanHeaderId = scanHeaderId;
    }
    public Integer getScanHeaderId() {
        return scanHeaderId;
    }
    public void setScanStudentIndex(Integer scanStudentIndex) {
        this.scanStudentIndex = scanStudentIndex;
    }
    public Integer getScanStudentIndex() {
        return scanStudentIndex;
    }
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
    public Integer getStudentId() {
        return studentId;
    }
    public void setStudentIdModDate(java.util.Date studentIdModDate) {
        this.studentIdModDate = studentIdModDate;
    }
    public java.util.Date getStudentIdModDate() {
        return studentIdModDate;
    }
    public void setStudentIdModUserId(Integer studentIdModUserId) {
        this.studentIdModUserId = studentIdModUserId;
    }
    public Integer getStudentIdModUserId() {
        return studentIdModUserId;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getGender() {
        return gender;
    }
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
    public String getBirthDay() {
        return birthDay;
    }
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    public String getStudentNumber() {
        return studentNumber;
    }
    public void setItemResponses(String itemResponses) {
        this.itemResponses = itemResponses;
    }
    public String getItemResponses() {
        return itemResponses;
    }
    public void setScanStudentStatus(String scanStudentStatus) {
        this.scanStudentStatus = scanStudentStatus;
    }
    public String getScanStudentStatus() {
        return scanStudentStatus;
    }
    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }
    public String getBirthMonth() {
        return birthMonth;
    }
    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }
    public String getBirthYear() {
        return birthYear;
    }
    public void setNumResponsesNotPersisted(Integer numResponsesNotPersisted) {
        this.numResponsesNotPersisted = numResponsesNotPersisted;
    }
    public Integer getNumResponsesNotPersisted() {
        return numResponsesNotPersisted;
    }
    public void setRosterId(Integer rosterId) {
        this.rosterId = rosterId;
    }
    public Integer getRosterId() {
        return rosterId;
    }
    public void setRosterIdModUserId(Integer rosterIdModUserId) {
        this.rosterIdModUserId = rosterIdModUserId;
    }
    public Integer getRosterIdModUserId() {
        return rosterIdModUserId;
    }
    public void setRosterIdModDate(java.util.Date rosterIdModDate) {
        this.rosterIdModDate = rosterIdModDate;
    }
    public java.util.Date getRosterIdModDate() {
        return rosterIdModDate;
    }
    
    
	public boolean equals(Object object){
		boolean isEqual = false;
		try{
			ScanStudentVO ss = (ScanStudentVO) object;
			if(((ss.birthDay == null && this.birthDay == null) ||
				(ss.birthDay.equals(this.birthDay))) &&
			   ((ss.birthMonth == null && this.birthMonth == null) ||
				(ss.birthMonth.equals(this.birthMonth))) &&
			   ((ss.birthYear == null && this.birthYear == null) ||
				(ss.birthYear.equals(this.birthYear))) &&
			   ((ss.firstName == null && this.firstName == null) ||
				(ss.firstName.equals(this.firstName))) &&
			   ((ss.gender == null && this.gender == null) ||
				(ss.gender.equals(this.gender))) &&
			   ((ss.itemResponses == null && this.itemResponses == null) ||
				(ss.itemResponses.equals(this.itemResponses))) &&
			   ((ss.lastName == null && this.lastName == null) ||
				(ss.lastName.equals(this.lastName))) &&
			   ((ss.middleName == null && this.middleName == null) ||
				(ss.middleName.equals(this.middleName))) &&
			   ((ss.numResponsesNotPersisted == null && this.numResponsesNotPersisted == null) ||
				(ss.numResponsesNotPersisted.equals(this.numResponsesNotPersisted))) &&
			   ((ss.rosterId == null && this.rosterId == null) ||
				(ss.rosterId.equals(this.rosterId))) &&
			   ((ss.rosterIdModDate == null && this.rosterIdModDate == null) ||
				(ss.rosterIdModDate.equals(this.rosterIdModDate))) &&
			   ((ss.rosterIdModUserId == null && this.rosterIdModUserId == null) ||
				(ss.rosterIdModUserId.equals(this.rosterIdModUserId))) &&
			   ((ss.scanHeaderId == null && this.scanHeaderId == null) ||
				(ss.scanHeaderId.equals(this.scanHeaderId))) &&
			   ((ss.scanStudentId == null && this.scanStudentId == null) ||
				(ss.scanStudentId.equals(this.scanStudentId))) &&
			   ((ss.scanStudentIndex == null && this.scanStudentIndex == null) ||
				(ss.scanStudentIndex.equals(this.scanStudentIndex))) &&
			   ((ss.scanStudentStatus == null && this.scanStudentStatus == null) ||
				(ss.scanStudentStatus.equals(this.scanStudentStatus))) &&
			   ((ss.studentId == null && this.studentId == null) ||
				(ss.studentId.equals(this.studentId))) &&
			   ((ss.studentIdModDate == null && this.studentIdModDate == null) ||
				(ss.studentIdModDate.equals(this.studentIdModDate))) &&
			   ((ss.studentIdModUserId == null && this.studentIdModUserId == null) ||
				(ss.studentIdModUserId.equals(this.studentIdModUserId))) &&
			   ((ss.studentNumber == null && this.studentNumber == null) ||
				(ss.studentNumber.equals(this.studentNumber)))) {
				isEqual = true;
			 }
		}
		catch(Exception e){
		}
		return isEqual;
	}
	
	public int compareTo(Object obj_) {
		ScanStudentVO toObject = (ScanStudentVO)obj_;
		String fromName = this.getLastName() + " " + this.getFirstName();
		String toName = toObject.getLastName() + " " + toObject.getFirstName();
		return(fromName.compareTo(toName));
	}
	
	public String birthday() throws ParseException {
		if (dateIsBlank()) return Stringx.empty();
		if (multipleMarks()) return CTBConstants.MULTIPLE_MARKS_ON_DATE; 
		if (!dateIsValid() || dateContainsBlank() ) return dateStringAsIs();

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		dateFormat.setLenient(false);
		Date bdayFormat = dateFormat.parse(getBirthMonth()+"/"+getBirthDay()+"/"+getBirthYear());
		return dateFormat.format(bdayFormat);
	}
	
	public String getDisplayBirthday() {
		if (dateIsBlank()) return Stringx.empty();
		if (multipleMarks()) return CTBConstants.MULTIPLE_MARKS_ON_DATE; 
		if (dateContainsBlank() || !dateIsValid()) return dateStringAsIs();

		String birthMonth = getBirthMonth() == null ? "" : pad(getBirthMonth());
		String birthDay = getBirthDay() == null ? "" : pad(getBirthDay());
		String birthYear = getBirthYear() == null ? "" : getBirthYear();
		String birthDate = birthMonth + "-" + birthDay + "-" +birthYear ;
		return birthDate;
	}
	

	private boolean dateIsValid() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		formatter.setLenient(false);
		try {
			formatter.parse(dateStringAsIs());
		}
		catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	private String dateStringAsIs() {
		//return zero(getBirthMonth()) + "/" + zero(getBirthDay()) + "/" + zero(getBirthYear());
		return zero(getBirthMonth()) + "-" + zero(getBirthDay()) + "-" + zero(getBirthYear());
	}
	
	private boolean multipleMarks() {
		return (hasStar(getBirthMonth())
				|| hasStar(getBirthDay())
				|| hasStar(getBirthYear()));
	}
	
	private boolean hasStar(String string) {
		if (Stringx.isEmpty(string)) return false;
		return string.indexOf("*") != -1;
	}
	private String zero(String component) {
		if (Stringx.hasContent(component)) 
		    return pad(component);
		else
		    return "  ";
	}
	
	private String pad(String component) {
		if (component.length() == 1) return "0"+component;
		return component;
	}
	
	private boolean dateIsBlank() {
		return Stringx.isEmpty(getBirthYear()) 
			&& Stringx.isEmpty(getBirthMonth()) 
 			&& Stringx.isEmpty(getBirthDay());
	}
	
	private boolean dateContainsBlank(){
		String day = getBirthDay() == null ? "" : getBirthDay();
		String month = getBirthMonth() == null ? "" : getBirthMonth();
		String year = getBirthYear() == null ? "" : getBirthYear();
		return (day.indexOf(" ") != -1 || month.indexOf(" ") != -1 || year.indexOf(" ") != -1);
	}
	
	private boolean dateIsPartiallyBlank() {
		boolean result = false;
		result = !dateIsBlank()
				&&( Stringx.isEmpty(getBirthYear()) 
						|| Stringx.isEmpty(getBirthMonth()) 
						|| Stringx.isEmpty(getBirthDay()));
		if (result) return result;
		return Integer.parseInt(getBirthMonth()) == 0 || Integer.parseInt(getBirthDay()) == 0;
	}
}