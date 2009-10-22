package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * Data bean used to indicate the editability/copyability
 * of a SchedulingStudent or SessionStudent object. The editable
 * and copyable flags contain "T" or "F" to indicate whether the
 * student is copyable from or editable within the current session,
 * the code field contains one of the values in EditCopyStatus.StatusCode
 * to indicate the reason for an editable flag value of "F", and in the case
 * that the code is PREVIOUSLY_SCHEDULED, the priorSession object contains
 * details about the test session of a test (which disallows multiple administrations
 * to the same student) in which the student was previously scheduled.
 * 
 * @author Nate_Cohen
 */
public class EditCopyStatus extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private String editable;
    private String copyable;
    private String code;
    private TestSession priorSession;
    
    public static class StatusCode {
        public static final String PREVIOUSLY_SCHEDULED = "Ses";
        public static final String SESSION_IN_PROGRESS = "Inp";
        public static final String SESSION_COMPLETED = "Cmp";
        public static final String OUTSIDE_VISIBLE_ORG = "Org";
    }
    
    public EditCopyStatus () {
        super();
        this.editable = "T";
        this.copyable = "T";
        this.code = "";
    }
    
    /**
	 * @return Returns the priorSession.
	 */
	public TestSession getPriorSession() {
		return priorSession;
	}
	/**
	 * @param priorSession The priorSession to set.
	 */
	public void setPriorSession(TestSession priorSession) {
		this.priorSession = priorSession;
	}
    /**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
    /**
	 * @return Returns the editable.
	 */
	public String getEditable() {
		return editable;
	}
	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(String editable) {
		this.editable = editable;
	}
    /**
	 * @return Returns the copyable.
	 */
	public String getCopyable() {
		return copyable;
	}
	/**
	 * @param copyable The copyable to set.
	 */
	public void setCopyable(String copyable) {
		this.copyable = copyable;
	}
} 
