package com.ctb.schedular;

import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * @author TCS Kolkata Offshore 
 * @version 05/09/2012
 */
public class Configuration implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Configuration conf = null;
	private int rescoreRetryInterval = 5;
	private int rescoreRetryCount    = 4;
	private int rescoreRosterLoadFactor  = 100;
	private String cronExpression  = "0 0/5 * * * ?";
	
	private Configuration(){
		load();
	}
	
	private void load() {
		try{
			ResourceBundle rb = ResourceBundle.getBundle("security");
			try {
				String  rescore_retry_interval= rb.getString("rescore.retry.interval");
				rescoreRetryInterval = Integer.parseInt(rescore_retry_interval.trim());
			} catch(Exception e){
				System.err.println("Invalid configuration rescore.retry.interval. Default value ["+rescoreRetryInterval+"] is used.");
			}

			try{
				String  rescore_retry_count= rb.getString("rescore.retry.count");
				rescoreRetryCount = Integer.parseInt(rescore_retry_count.trim());
			} catch(Exception e){
				System.err.println("Invalid configuration rescore.retry.count. Default value ["+rescoreRetryCount+"] is used.");
			}
	
			try {
				String  rescore_roster_loadfactor= rb.getString("rescore.roster.load.factor");
				rescoreRosterLoadFactor = Integer.parseInt(rescore_roster_loadfactor.trim());
			} catch(Exception e){
				System.err.println("Invalid configuration rescore.roster.load.factor. Default value ["+rescoreRosterLoadFactor+"] is used.");
			}
			
			try {
				cronExpression= rb.getString("rescore.cronExpression");
			} catch(Exception e){
				System.err.println("Invalid configuration rescore.cronExpression. Default value ["+cronExpression+"] is used.");
			}
			
		} catch ( Exception e ){
			e.printStackTrace();
		}
		 
		
	}

	public static Configuration getConfiguration(){
		if(conf == null){
			conf = new Configuration();
			System.out.println("Schedular For Rescore Configuration: "+conf);
		}
		
		return conf;
	}

	/**
	 * @return the rescoreRetryInterval
	 */
	public int getRescoreRetryInterval() {
		return rescoreRetryInterval;
	}

	/**
	 * @return the rescoreRetryCount
	 */
	public int getRescoreRetryCount() {
		return rescoreRetryCount;
	}

	/**
	 * @return the rescoreRosterLoadFactor
	 */
	public int getRescoreRosterLoadFactor() {
		return rescoreRosterLoadFactor;
	}
	
	/**
	 * @return the cronExpression
	 */
	public String getCronExpression() {
		return cronExpression;
	}

	@Override
	public String toString() {
		return "RetryInterval["+rescoreRetryInterval+"] RetryCount["+ rescoreRetryCount+"] RosterLoadFactor[" +rescoreRosterLoadFactor+"] CronExpression["+cronExpression+"].";
	}

}
