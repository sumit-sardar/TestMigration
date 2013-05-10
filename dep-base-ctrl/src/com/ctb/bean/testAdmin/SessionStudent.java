package com.ctb.bean.testAdmin; 

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * Data bean representing the contents of the OAS.STUDENT table
 * with additional fields for current grade and accommodations
 * information, as well as an org node name and form assignment
 * associated with a test session scheduling activity on this student.
 * A tested flag is also included, to indicate whether the student
 * roster has left the SC (scheduled) status, and thus cannot be
 * removed from the test session. Also, an EditCopyStatus object 
 * is provided in the status field, and used to indicate whether
 * the student can be scheduled for the selected test or copied into
 * a new session. If the editCopyStatus.editable flag is "F", 
 * the editCopyStatus.code field indicates the reason the student 
 * is non-editable. If the student is non-editable because the session
 * is of a test which the customer has indicated does not permit retakes,
 * the status.priorSession field contains information on the test session
 * in which the student was previously scheduled.
 * 
 * @author Nate_Cohen, John_Wang
 */
public class SessionStudent extends SchedulingStudent
{ 
    static final long serialVersionUID = 1L;
    private String itemSetForm;
    private String tested;
    private String testCompletionStatus;
    private String scoringStatus;
    private StudentManifest [] studentManifests;
    private String[] valueMap;
    private List valueHashMap;
    private boolean isNewStudent = false;
    private Map<Integer, Integer> savedlocatorTDMap;
    
    public SessionStudent() {
        super();
    }
  
    public SessionStudent (SchedulingStudent student) {
        setStudentId(student.getStudentId());
        setUserName(student.getUserName());
        setPassword(student.getPassword());
        setFirstName(student.getFirstName());
        setMiddleName(student.getMiddleName());
        setLastName(student.getLastName());
        setPreferredName(student.getPreferredName());
        setPrefix(student.getPrefix());
        setSuffix(student.getSuffix());
        setBirthdate(student.getBirthdate());
        setGender(student.getGender());
        setEthnicity(student.getEthnicity());
        setEmail(student.getEmail());
        setGrade(student.getGrade());
        setOutOfSchool(student.getOutOfSchool());	//Added for out of school changes
        setExtElmId(student.getExtElmId());
        setExtPin1(student.getExtPin1());
        setExtPin2(student.getExtPin2());
        setExtPin3(student.getExtPin3());
        setExtSchoolId(student.getExtSchoolId());
        setActiveSession(student.getActiveSession());
        setPotentialDuplicatedStudent(student.getPotentialDuplicatedStudent());
        setCreatedBy(student.getCreatedBy());
        setCreatedDateTime(student.getCreatedDateTime());
        setUpdatedBy(student.getUpdatedBy());
        setUpdatedDateTime(student.getUpdatedDateTime());
        setActivationStatus(student.getActivationStatus());
        setDataImportHistoryId(student.getDataImportHistoryId());
        setStudentGrade(student.getStudentGrade());
        setScreenMagnifier(student.getScreenMagnifier());
        setScreenReader(student.getScreenReader());
        setCalculator(student.getCalculator());
        setTestPause(student.getTestPause());
        setUntimedTest(student.getUntimedTest());
        setQuestionBackgroundColor(student.getQuestionBackgroundColor());
        setQuestionFontColor(student.getQuestionFontColor());
        setQuestionFontSize(student.getQuestionFontSize());
        setAnswerBackgroundColor(student.getAnswerBackgroundColor());
        setAnswerFontColor(student.getAnswerFontColor());
        setAnswerFontSize(student.getAnswerFontSize());
        setOrgNodeId(student.getOrgNodeId());
        setOrgNodeName(student.getOrgNodeName());
        setHasColorFontAccommodations(student.getHasColorFontAccommodations());
        setHasAccommodations(student.getHasAccommodations());
        setOrgNodeCategoryName(student.getOrgNodeCategoryName());
        setStatus(student.getStatus());
        setHighLighter(student.getHighLighter()); /* 51931 Deferred Defect For HighLighter*/
        setExtendedTimeAccom(student.getExtendedTimeAccom()); // Added for Student Pacing
    }
    
    /**
	 * @return Returns the itemSetForm.
	 */
	public String getItemSetForm() {
		return itemSetForm;
	}
	/**
	 * @param itemSetForm The itemSetForm to set.
	 */
	public void setItemSetForm(String itemSetForm) {
		this.itemSetForm = itemSetForm;
	}
    /**
	 * @return Returns the tested.
	 */
	public String getTested() {
		return tested;
	}
	/**
	 * @param tested The tested to set.
	 */
	public void setTested(String tested) {
		this.tested = tested;
	}
    /**
     * @return Returns the testCompletionStatus.
     */
    public String getTestCompletionStatus() {
        return testCompletionStatus;
    }
    /**
     * @param testCompletionStatus The testCompletionStatus to set.
     */
    public void setTestCompletionStatus(String testCompletionStatus) {
        this.testCompletionStatus = testCompletionStatus;
    }
    /**
     * @return Returns the scoringStatus.
     */
    public String getScoringStatus() {
        return scoringStatus;
    }
    /**
     * @param scoringStatus The scoringStatus to set.
     */
    public void setScoringStatus(String scoringStatus) {
        this.scoringStatus = scoringStatus;
    }

	/**
	 * @return the studentManifests
	 */
	public StudentManifest[] getStudentManifests() {
		return studentManifests;
	}

	/**
	 * @param studentManifests the studentManifests to set
	 */
	public void setStudentManifests(StudentManifest[] studentManifests) {
		this.studentManifests = studentManifests;
	}
	/**
	 * @return the valueMap
	 */
	public String[] getValueMap() {
		return valueMap;
	}
	/**
	 * @param valueMap the valueMap to set
	 */
	public void setValueMap(String[] valueMap) {
		this.valueMap = valueMap;
		if (valueHashMap != null) {
			valueHashMap.add( this.valueMap );
		} else {
			valueHashMap = new ArrayList();
			valueHashMap.add( this.valueMap );
		}
		
	}
	/**
	 * @return the valueHashMap
	 */
	public List getValueHashMap() {
		return valueHashMap;
	}
	
	/**
	 * @param valueHashMap the valueHashMap to set
	 */
	public void setValueHashMap(List valueHashMap) {
		this.valueHashMap = valueHashMap;
	}

	public boolean isNewStudent() {
		return isNewStudent;
	}

	public void setNewStudent(boolean isNewStudent) {
		this.isNewStudent = isNewStudent;
	}

	public Map<Integer, Integer> getSavedlocatorTDMap() {
		return savedlocatorTDMap;
	}

	public void setSavedlocatorTDMap(Map<Integer, Integer> savedlocatorTDMap) {
		this.savedlocatorTDMap = savedlocatorTDMap;
	}
	
	
} 
