package com.ctb.schedular;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author TCS Kolkata Offshore 
 * @version 05/09/2012
 */
public class ScheduleRosterDao {
	
	private static String GET_SCHEDULE_ROSTER = " SELECT SROSTER.TEST_ROSTER_ID FROM SCHEDULED_SCORABLE_ROSTER SROSTER, TEST_ROSTER ROSTER WHERE (UPPER(NVL(SROSTER.STATE, 'FAILED')) <> UPPER('SCHEDULED') OR   (SYSDATE - SROSTER.UPDATED_DATE_TIME) > 1)  AND ROSTER.TEST_ROSTER_ID = SROSTER.TEST_ROSTER_ID AND SROSTER.RETRY_COUNT <> ? AND (SYSDATE - SROSTER.UPDATED_DATE_TIME) * 1440 > ?  ORDER BY SROSTER.UPDATED_DATE_TIME FOR UPDATE ";
	private static String UPDATE_SCHEDULE_ROSTER_COUNT = " UPDATE SCHEDULED_SCORABLE_ROSTER   SET STATE  = 'SCHEDULED',  UPDATED_DATE_TIME = SYSDATE,  RETRY_COUNT  = (RETRY_COUNT + 1) WHERE TEST_ROSTER_ID IN ( <<ROSTER_LIST>> ) ";
	private static String ADD_UPDATE_SCHEDULE_ROSTER = " MERGE INTO SCHEDULED_SCORABLE_ROSTER SSR USING (SELECT <<ROSTER_ID>> TEST_ROSTER_ID FROM DUAL) DEST ON (SSR.TEST_ROSTER_ID = DEST.TEST_ROSTER_ID) WHEN MATCHED THEN UPDATE SET SSR.updated_date_time = Sysdate, SSR.state = 'FAILED' WHEN NOT MATCHED THEN INSERT(test_roster_id,state) VALUES(DEST.test_roster_id,'FAILED') ";
	private static String DELETE_SCHEDULE_ROSTER = "DELETE FROM SCHEDULED_SCORABLE_ROSTER WHERE TEST_ROSTER_ID = ?";
	
	
	
	public List<Integer> getSchedulableRosterList(Configuration conf) {
		List<Integer> list = new LinkedList<Integer>();
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		int loadFact = conf.getRescoreRosterLoadFactor();
		
		try {
			con = SqlUtil.openOASDBcon(true);
			pst = con.prepareStatement(GET_SCHEDULE_ROSTER);
			pst.setInt(1, conf.getRescoreRetryCount());
			pst.setInt(2, conf.getRescoreRetryInterval());
			//pst.setInt(3, conf.getRescoreRosterLoadFactor());
			rs = pst.executeQuery();
			
			while (rs.next() && list.size()<loadFact) {
				list.add(rs.getInt(1));
			}
			
			if(list.size()>0){
				ClosableHelper.close(pst,rs );
				for (String rosterList : getFormettedTestRoster(list)) {
					pst = con.prepareStatement(UPDATE_SCHEDULE_ROSTER_COUNT.replaceAll("<<ROSTER_LIST>>", rosterList));
					pst.executeUpdate();
					ClosableHelper.close(pst);
				}
				
			}
			
			
			con.commit();	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ClosableHelper.close(con, pst, rs);
		}
		return list;
	}
	
	
	public void addUpdateRosterForSchedule(int testRosterId){
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SqlUtil.openOASDBcon(true);
			pst = con.prepareStatement(ADD_UPDATE_SCHEDULE_ROSTER.replaceAll("<<ROSTER_ID>>", String.valueOf(testRosterId)));
			pst.executeUpdate();
			con.commit();		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ClosableHelper.close(con, pst, rs);
		}	
		
	}
	
	public void deleteScheduleRoster(int testRosterId){
		PreparedStatement pst = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SqlUtil.openOASDBcon(true);
			pst = con.prepareStatement(DELETE_SCHEDULE_ROSTER);
			pst.setInt(1, testRosterId);
			pst.executeUpdate();
			con.commit();		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ClosableHelper.close(con, pst, rs);
		}	
		
	}
	
	
	
	
	private List<String> getFormettedTestRoster(List<Integer> testroster) {
		int count = 0;
		StringBuilder rosterin = new StringBuilder();
		List<String> vallist = new LinkedList<String>();
		boolean isBlank = true;
	
		for (Integer roster : testroster) {
			if(!isBlank){
				rosterin.append(", ");	
			} else {
				isBlank = false;
			}
			rosterin.append(roster.toString());
			++count;
			if (count > 500) {
				vallist.add(rosterin.toString());
				rosterin = new StringBuilder();
				isBlank  = true;
				count= 0;
				
				
			} 
		}
		if(!isBlank && vallist.toString().trim().length()>0 ) {
			vallist.add(rosterin.toString());
		}
		return vallist;
	}

}
