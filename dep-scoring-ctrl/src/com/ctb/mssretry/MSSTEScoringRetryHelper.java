/**
 * 
 */
package com.ctb.mssretry;

import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author TCS
 *
 */
public class MSSTEScoringRetryHelper {
	private static final ResourceBundle rb = ResourceBundle.getBundle("config");
	private static final String FTTE_RETRY_CRONEXPRESSION = rb.getString("ftte.retry.cronexpression");
	/**
	 * Retry the in progress WS call  
	 */
	public void retryMSSTEScoringProgress(){
		try {
			MSSRetryDBUtility.retryMSSTEScoringProgress();
		} catch (SQLException e) {
			//Do Nothing
		}
	}
	
	public String getMSSRetryCronExpression(){
		return FTTE_RETRY_CRONEXPRESSION;
	}
}
