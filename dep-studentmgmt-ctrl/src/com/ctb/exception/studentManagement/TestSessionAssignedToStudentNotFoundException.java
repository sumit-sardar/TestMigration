/**
 * 
 */
package com.ctb.exception.studentManagement;

import com.ctb.exception.CTBBusinessException;

/**
 * @author TCS
 *
 */
public class TestSessionAssignedToStudentNotFoundException extends
		CTBBusinessException {
	
	static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
	public TestSessionAssignedToStudentNotFoundException(String message) {
	    super(message);
	}
}
