/**
 * 
 */
package com.ctb.prismws;

import java.util.ResourceBundle;

/**
 * @author TCS
 *
 */
public class PrismWebServiceHelper {
	
	/**
	 * Retry the in progress WS call  
	 */
	public void retryWSProgress(){
		com.ctb.prism.web.dbutility.PrismWebServiceDBUtility.retryWSProgress();
	}
	
	public String getPrismWSCronExpression(){
		ResourceBundle resourceBundler = com.ctb.prism.web.constant.PrismWebServiceConstant.resourceBundler;
		try{
			return resourceBundler.getString("wscronexpression");
		}catch(Exception e){
			//Do Nothing
		}
		return "0 0/5 * * * ?";
	}
}
