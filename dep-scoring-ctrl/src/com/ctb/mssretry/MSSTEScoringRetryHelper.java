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
	
	/*
	 * Comment Out TASC FT TE Items Scoring for Story OAS-3944
	 */
	/*private static final ResourceBundle rb = ResourceBundle.getBundle("config");
	private static final String FTTE_RETRY_CRONEXPRESSION = rb.getString("ftte.retry.cronexpression");*/
	private static final String FTTE_RETRY_CRONEXPRESSION = "0 0/5 * * * ?"; //Added to Comment Out the config file property, TASC FT TE Items Scoring for Story OAS-3944
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
