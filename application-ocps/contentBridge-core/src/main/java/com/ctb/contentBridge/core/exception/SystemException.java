/**
 * 
 */
package com.ctb.contentBridge.core.exception;

/**
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class SystemException extends RuntimeException {

	/**
	 * Auto generated serial version UID
	 */
	private static final long serialVersionUID = 9021067510715395358L;

	public SystemException(String message) {
		super(message);
	}

	public SystemException() {
		super();
	}

	/**
	 * This is the constructor of the class which calls the super constructor of
	 * the Exception class and executes the expected behavior of the exception
	 * based on the throwable type.
	 * 
	 * @param throwable
	 *            - This can accept any Exception
	 */
	public SystemException(Throwable throwable) {
		super(throwable);
	}
}
