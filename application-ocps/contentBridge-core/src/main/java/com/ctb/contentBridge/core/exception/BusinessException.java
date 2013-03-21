/**
 * 
 */
package com.ctb.contentBridge.core.exception;

/**
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class BusinessException extends SystemException {

	private static final long serialVersionUID = -2622089944219283877L;

	private String code;

	private String[] params;

	private String customExceptionMessage;

	public BusinessException(String code, String[] params) {
		super();
		this.code = code;
		this.params = params;
	}

	public BusinessException(String message) {
		super(message);
		this.customExceptionMessage = message;
	}

	public String getCode() {
		return this.code;
	}

	public String[] getParams() {
		return params;
	}

	public String getCustomExceptionMessage() {
		return this.customExceptionMessage;
	}

	public String getMessage() {
		return this.code;
	}
}
