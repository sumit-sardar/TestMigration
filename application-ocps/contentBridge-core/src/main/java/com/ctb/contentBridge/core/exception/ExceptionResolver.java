/**
 * 
 */
package com.ctb.contentBridge.core.exception;

/**
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 * 
 */
public class ExceptionResolver {
	public static SystemException resolve(Exception ex) {
		if (ex instanceof BusinessException)
			return (BusinessException) ex;
		else if (ex instanceof SystemException)
			return (SystemException) ex;
		else
			return new SystemException(ex);
	}
}
