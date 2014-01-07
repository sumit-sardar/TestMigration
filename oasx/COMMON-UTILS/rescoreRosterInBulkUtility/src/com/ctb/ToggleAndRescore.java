package com.ctb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ctb.utils.Configuration;
import com.ctb.utils.QueueSend;
import com.ctb.utils.ResourceUtils;
import com.ctb.utils.SqlQuery;
import com.ctb.utils.SqlUtil;

public class ToggleAndRescore {
	
	public static void main(String [] args ){
		ToggleAndRescore tar = new ToggleAndRescore();
		ResourceUtils.loadExternalProperties(args[0], args[1]);
		List<String> myRosterList = null;
		if(!tar.validateConfig()){
			System.err.println("Some mandatory field are missing. Please provide the valid input.");
			System.exit(0);
		}
		myRosterList = tar.createRosterList(Configuration.getCsvFilePath());
		if(null != myRosterList && myRosterList.size()>0){
			tar.invokeScoring(myRosterList);
			System.out.println("Re-score process has been completed. \n\nWait for few minutes to reflect the score in IRS database.");
		}else{
			System.err.println("Roster list is not present in the csv file.");
			System.exit(0);
		}
		
	}
	
	private void invokeScoring(List<String> rosterList){
		
		for (String rosterId : rosterList) {
			if(null != rosterId && !"".equals(rosterId)){
				Integer testRosterId = new Integer(rosterId);
				InitialContext ic;
				try {
					ic = QueueSend.getInitialContext(Configuration
							.getJndiFactory(), Configuration.getJmsURL(), Configuration
							.getJmsPrincipal(), Configuration.getJmsCredentials());
					QueueSend qs = new QueueSend();
				    qs.init(ic, Configuration.getJmsFactory(), Configuration.getJmsQueue());
				    QueueSend.readAndSend(qs,testRosterId);
				    qs.close();
				    ic.close();
				} catch (Exception e) {
					System.err.println("JMS Message has not been Sent for Roster : "+ rosterId);
					e.printStackTrace();
				}
			}
		}
		
	}

//	private void toggleValidateRoster(List<String> rosterList) throws SQLException {
//		Connection oascon = null;
//		oascon = SqlUtil.openOASDBconnectionForResearch();
//		Map<String, String> rosterStatusMap = new HashMap<String, String>(); 
//		int inClauselimit = 999;
//		int loopCounters = rosterList.size() / inClauselimit;
//		if((rosterList.size() % inClauselimit) > 0){
//			loopCounters = loopCounters + 1;
//		}
//		for(int counter=0; counter<loopCounters; counter++){
//			Integer[] newRosterIds = null;
//			String searchByRosterIds = "";
//			if((counter+1)!=loopCounters){
//				newRosterIds = new Integer [inClauselimit];
//				System.arraycopy(rosterList.toArray(new String[rosterList.size()]), (counter*inClauselimit) , newRosterIds, 0, inClauselimit);
//			} else {
//				int count = rosterList.size() % inClauselimit;
//				newRosterIds = new Integer [count];
//				System.arraycopy(rosterList.toArray(new String[rosterList.size()]), ((loopCounters-1)*inClauselimit) , newRosterIds, 0, count);
//			}
//			try {
//				getRosterValidationStatus(oascon, newRosterIds, rosterStatusMap);
//			} catch (SQLException e) {
//				
//				e.printStackTrace();
//			}
//		}
//		
//	}
//
//	private void getRosterValidationStatus(Connection oascon,
//			Integer[] newRosterIds, Map<String, String> rosterStatusMap) throws SQLException {
//		String rosterIds = "";
//		PreparedStatement ps = null ;
//		ResultSet rs = null;
//		rosterIds = SqlQuery.generateSQLCriteria("ROS.TEST_ROSTER_ID IN", newRosterIds);
//		try {
//			ps = oascon.prepareStatement(SqlQuery.GET_ROSTER_VALIDATION_STATUS);
//			ps.setString(1, rosterIds);
//			rs = ps.executeQuery(); 
//			while(rs.next()){
//				if(!rosterStatusMap.containsKey(rs.getString(1)))
//					rosterStatusMap.put(rs.getString(1), rs.getString(2));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally{
//			rs.close();
//			ps.close();
//		}
//	}

	private List<String> createRosterList(String path) {
		List<String> myRosterList = new ArrayList<String>();
		File file = new File(path);                
		try {
			if(file.getName().endsWith(".csv")){
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				int count = 1;
				while(null != (line = br.readLine())){
					System.out.println("Line "+ count +" : "+line);
					if(line.contains(",")){
						String [] rosterArr = line.split(",");
						myRosterList = Arrays.asList(rosterArr);
					}else if(line.contains("|")){
						String [] rosterArr = line.split("|");
						myRosterList = Arrays.asList(rosterArr);
					}else{
						String [] rosterArr = line.split(",");
						myRosterList = Arrays.asList(rosterArr);
					}
				}count++;
			}else{
				System.err.println("File is not in csv format.");
				System.exit(0);
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return myRosterList;
	}

	private boolean validateConfig(){
		return !"".equals(Configuration.getCsvFilePath())&&
		!"".equals(Configuration.getJmsCredentials()) &&
		!"".equals(Configuration.getJmsFactory()) &&
		!"".equals(Configuration.getJmsPrincipal()) && 
		!"".equals(Configuration.getJmsQueue()) &&
		!"".equals(Configuration.getJmsURL()) && 
		!"".equals(Configuration.getJndiFactory());
	}

}
