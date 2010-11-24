package com.ctb.control.testDelivery;


import com.bea.control.*;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import java.io.Serializable;
import java.sql.SQLException;
import com.ctb.control.db.LoadTestDB;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.xmlbeans.XmlObject;
import noNamespace.TmssvcRequestDocument;
import noNamespace.TmssvcRequestDocument.TmssvcRequest;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.RunLoadRequest;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.UploadStatisticsRequest;
import noNamespace.TmssvcRequestDocument.TmssvcRequest.UploadSystemInfoRequest;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.RunLoadResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.UploadStatisticsResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.UploadSystemInfoResponse;
import com.ctb.bean.testDelivery.loadTest.LoadTestConfig;
import com.ctb.bean.testDelivery.loadTest.LoadTestRoster;
import com.ctb.util.testDelivery.Constants;
import com.ctb.util.DateUtils;
import com.ctb.util.OASLogger;
import java.util.Date;
import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@ControlImplementation()
public class LoadTestImpl implements LoadTest, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Control
	private LoadTestDB loadTestDB;
	
	
	public TmssvcResponseDocument getLoadTestConfig(TmssvcRequestDocument document){
		TmssvcRequest runLoadRequest = document.getTmssvcRequest();
        TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance();
		RunLoadResponse runLoadResponse = response.addNewTmssvcResponse().addNewRunLoadResponse();
		response.addNewTmssvcResponse().setMethod("run_load_response");
		runLoadResponse.setSystemId(runLoadRequest.getRunLoadRequest().getSystemId());
		
		String systemId = runLoadRequest.getRunLoadRequest().getSystemId();
		String loadTestRosterId = "";
		
		//changes for filtering load test by sites
		boolean allowedSite = true;
		String siteId = systemId.substring(0, systemId.indexOf(":"));
        String corpId = "";
        if(!siteId.equals("")){
        	corpId = siteId.substring(0, systemId.indexOf("-"));
        }       
        
		try{
        	LoadTestConfig loadTestConfig = loadTestDB.getLoadTestConfig();
        	if (loadTestConfig != null){
        		if (loadTestConfig.getRunLoad().equals("Y")){
            		
        			//changes for filtering load test by sites
        			if (loadTestConfig.getFilterSites().equals("Y")){
            			if (!corpId.equals("")){
            				int siteCount = loadTestDB.allowedSite(corpId);
            				
            				if (siteCount == 0 ){
            					allowedSite = false;
            				}
            			}else{
            				allowedSite = false;
            			}
            		}
        			
        			if (allowedSite){
        				int inProgressRosters = loadTestDB.getInprogressRosters();
            			
            			if (inProgressRosters < loadTestConfig.getMaxLoad()){
            				runLoadResponse.setStatus(Constants.LoadTestConfig.RUN_LOAD);
                			
                			try{
                				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    			
                    			Date requestTime = df.parse(runLoadRequest.getRunLoadRequest().getSystemTime());
                    			
                    			//get client time from request
                    			Calendar clientTime = Calendar.getInstance();
                    			clientTime.setTime(requestTime);
                    			
                    			//get server time from database
                    			Calendar serverTime = Calendar.getInstance();
                    			serverTime.setTime(df.parse(loadTestDB.getServerTime()));
                    			
                    			//time diff in hours
                    			Long longTimeDiff = (clientTime.getTimeInMillis() - serverTime.getTimeInMillis())/(60 * 1000);
                    			Integer timeDiff = Integer.valueOf(longTimeDiff.intValue());
                    			
                    			Calendar runDate = Calendar.getInstance();
                    			runDate.setTime(df.parse(loadTestConfig.getRunDate()));
                    			
                    			//adjust run date as per time difference between server and client
                    			runDate.add(Calendar.MINUTE, timeDiff);
                    			                			
                    			//change to randomly distribute the load test schedule over 60 mins              			                			
                    			Integer rampUpTime = loadTestConfig.getRampUpTime();
                    			if (rampUpTime != 0){
                    				Random r = new Random();
                    				Integer randSecs = r.nextInt(rampUpTime);
                        			runDate.add(Calendar.SECOND, randSecs);
                    			}                    			                    		              			
                    			
                    			runLoadResponse.setRunDate(df.format(runDate.getTime()));
                			}catch(Exception e){
                				OASLogger.getLogger("TestDelivery").debug(loadTestConfig.toString());
                			}        			        			        			        			            		
                    		
                			LoadTestRoster loadTestRoster = null;
                			LoadTestRoster [] newLoadTestRosters = null;
                			boolean existingRoster = true;
                			//check if this system already has a roster scheduled for future run
                			loadTestRoster = loadTestDB.getAssignedLoadTestRoster(systemId);
                			
                			if (loadTestRoster == null){
                				//change to fix duplicate roster assignment
                				newLoadTestRosters = loadTestDB.getLoadTestRoster();
                				if(newLoadTestRosters != null){
                					Random n = new Random();
                					Integer randIndex = n.nextInt(newLoadTestRosters.length);
                					loadTestRoster = newLoadTestRosters[randIndex];
                				}
                				existingRoster = false;
                			}
                				
                			
                    		if (loadTestRoster != null){
                    			runLoadResponse.setRosterId(loadTestRoster.getTestRosterId());
                        		runLoadResponse.setLoginId(loadTestRoster.getLoginId());
                        		runLoadResponse.setAccessCode(loadTestRoster.getAccessCode());
                        		runLoadResponse.setPassword(loadTestRoster.getPassword());
                        		
                        		int updateCount = loadTestDB.setUsedFlag(Integer.valueOf(loadTestRoster.getTestRosterId()));
                        		if (updateCount <= 0){
                        			OASLogger.getLogger("TestDelivery").debug(loadTestRoster.toString());
                        		}                		
                        		if (!existingRoster){
                        			int insertCount = loadTestDB.createStatisticsRecord(runLoadRequest.getRunLoadRequest().getSystemId(), Integer.valueOf(loadTestRoster.getTestRosterId()));
                                	if (insertCount <= 0){
                                		OASLogger.getLogger("TestDelivery").debug(loadTestRoster.toString());
                                	}
                        		} 
                			}else{
                				runLoadResponse.setStatus(Constants.LoadTestConfig.NO_RUN);
                    			System.out.println("##### loadTestRoster SQL exception ### ");
                			}
                    		     		            		            		                    	
            			}else{
            				runLoadResponse.setStatus(Constants.LoadTestConfig.NO_RUN); 
                    		System.out.println("##### loadTestConfig ### exceeds max load");
            			}
        			}else{
        				runLoadResponse.setStatus(Constants.LoadTestConfig.NO_RUN); 
                		System.out.println("##### site not allowed to participate ###");
        			}       			        			
        		}		
        	}else{
        		runLoadResponse.setStatus(Constants.LoadTestConfig.NO_RUN); 
        		System.out.println("##### loadTestConfig ### No Data");
        	}        	
        } catch(SQLException sqle){
        	System.out.println("##### SQL Exception");
        	System.out.println(sqle.getErrorCode() +"  "  + sqle.getMessage());
        	runLoadResponse.setStatus(Constants.LoadTestConfig.NO_RUN);        	
        }				
		return response;
	}
	
	public TmssvcResponseDocument setLoadTestStatistics(TmssvcRequestDocument document){
		TmssvcRequest uploadStatisticsRequest = document.getTmssvcRequest();
        TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance();
		UploadStatisticsResponse uploadStatisticsResponse = response.addNewTmssvcResponse().addNewUploadStatisticsResponse();
		response.addNewTmssvcResponse().setMethod("upload_statistics_response");
		
		
		Integer maxResponseTime = Integer.valueOf(uploadStatisticsRequest.getUploadStatisticsRequest().getMaxResponseTime());
		
		Integer avgResponseTime = Integer.valueOf(uploadStatisticsRequest.getUploadStatisticsRequest().getAvgResponseTime());
		
		Integer minResponseTime = Integer.valueOf(uploadStatisticsRequest.getUploadStatisticsRequest().getMinResponseTime());
		
		Integer failureCount = Integer.valueOf(uploadStatisticsRequest.getUploadStatisticsRequest().getFailureCount());
		
		Integer successCount = Integer.valueOf(uploadStatisticsRequest.getUploadStatisticsRequest().getSuccessCount());
		
		Integer testRosterId =  Integer.valueOf(uploadStatisticsRequest.getUploadStatisticsRequest().getRosterId());
		
		String systemId = uploadStatisticsRequest.getUploadStatisticsRequest().getSystemId();
		
		uploadStatisticsResponse.setSystemId(systemId);
		try{
			
			int updateCount = loadTestDB.updateStatistics(systemId, testRosterId, maxResponseTime, minResponseTime, avgResponseTime, successCount, failureCount);
        	
			if (updateCount > 0){
				
				uploadStatisticsResponse.setStatus("OK");
			}else{
				
				uploadStatisticsResponse.setStatus("ERROR");
			}
        	
		}catch(SQLException sqle){
			System.out.println("##### SQL Exception");
        	System.out.println(sqle.getErrorCode() +  "  "  + sqle.getMessage());
        	uploadStatisticsResponse.setStatus("ERROR");
		}
		
		
		return response;
	}
	
	public TmssvcResponseDocument uploadSystemInfo(TmssvcRequestDocument document){
		TmssvcRequest uploadSystemInfoRequest = document.getTmssvcRequest();
        TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance();
		UploadSystemInfoResponse uploadSystemInfoResponse = response.addNewTmssvcResponse().addNewUploadSystemInfoResponse();
		response.addNewTmssvcResponse().setMethod("upload_systemInfo_response");
		
		String systemId = uploadSystemInfoRequest.getUploadSystemInfoRequest().getSystemId();
		String osName = uploadSystemInfoRequest.getUploadSystemInfoRequest().getOsName();
		String osVersion = uploadSystemInfoRequest.getUploadSystemInfoRequest().getOsVersion();
		String systemModel = uploadSystemInfoRequest.getUploadSystemInfoRequest().getSystemModel();
		String physicalMemory = uploadSystemInfoRequest.getUploadSystemInfoRequest().getPhysicalMemory();
		String virtualMemory = uploadSystemInfoRequest.getUploadSystemInfoRequest().getVirtualMemory();
		String processors = uploadSystemInfoRequest.getUploadSystemInfoRequest().getProcessors();
		String networkCards = uploadSystemInfoRequest.getUploadSystemInfoRequest().getNetworkCards();
		String siteId = systemId.substring(0, systemId.indexOf(":"));
		try{
			int updateCount = loadTestDB.updateSystemInfo(systemId, osName, osVersion, systemModel, physicalMemory, virtualMemory, processors, networkCards);
			if (updateCount > 0){
				uploadSystemInfoResponse.setStatus("OK");
			}else{//insert new record
				int insertCount = loadTestDB.insertSystemInfo(siteId, systemId, osName, osVersion, systemModel, physicalMemory, virtualMemory, processors, networkCards);
				if (insertCount > 0){
					uploadSystemInfoResponse.setStatus("OK");
				}else{
					uploadSystemInfoResponse.setStatus("ERROR");
				}
			}
				
		}catch(SQLException sqle){
			System.out.println("##### SQL Exception");
        	System.out.println(sqle.getErrorCode() +  "  "  + sqle.getMessage());
        	uploadSystemInfoResponse.setStatus("ERROR");
		}
		
		return response;
	}
}