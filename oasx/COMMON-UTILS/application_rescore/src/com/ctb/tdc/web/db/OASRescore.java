package com.ctb.tdc.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import com.Main;
import com.Main.ScoringMessage;
import com.ctb.tdc.web.to.AuthenticationData;
import com.ctb.tdc.web.to.ItemResponseData;
import com.ctb.tdc.web.to.ItemSet;
import com.ctb.tdc.web.to.StudentCredentials;
import com.ctb.tdc.web.to.TestData;
import com.ctb.tdc.web.utils.CATEngineProxy;
import com.ctb.tdc.web.utils.Constants;

public class OASRescore {
	public static String cArea=null;
	ArrayList<TestData> testData = new ArrayList<TestData>();
	private static final String AUTHENTICATE_STUDENT_SQL = "select  ros.test_roster_id as testRosterId,  stu.student_id as studentId,  stu.last_name as studentLastName,  stu.first_name as studentFirstName,  stu.middle_name as studentMiddleName,  ros.test_completion_status as rosterTestCompletionStatus,  adm.login_start_date as windowStartDate,  adm.login_end_date as windowEndDate,  adm.daily_login_start_time as dailyStartTime,  adm.daily_login_end_time as dailyEndTime,  adm.test_admin_status as testAdminStatus,  adm.time_zone AS timeZone,  ros.capture_method as captureMethod,  ros.restart_number as restartNumber,  ros.test_admin_id as testAdminId, \t  ros.random_distractor_seed as randomDistractorSeedNumber, \t  ros.tts_speed_status as ttsSpeedStatus from  student stu,  test_roster ros,  test_admin adm where  adm.test_admin_id = ros.test_admin_id  and ros.student_id = stu.student_id  and stu.activation_status = 'AC'  and ros.activation_status = 'AC'  and adm.activation_status = 'AC'  and upper(stu.user_name) = upper(?)  and upper(ros.password) = upper(?)";    
	private static final String ITEMSET_SQL = "SELECT SISS.COMPLETION_STATUS AS COMPLETIONSTATUS,ISET.ITEM_SET_ID AS ID FROM ITEM_SET ISET, STUDENT_ITEM_SET_STATUS SISS, TEST_ROSTER TR, TEST_ADMIN TA WHERE tr.test_roster_id=? AND tr.test_admin_id = ta.test_admin_id AND iset.item_set_id = siss.item_set_id AND siss.test_roster_id = tr.test_roster_id AND ISET.ITEM_SET_TYPE = 'TD' AND siss.completion_status = 'CO' AND siss.objective_score IS NOT NULL";
	private static final String GET_ROSTER_DATA = "SELECT "
		+"stu.user_name username,tr.PASSWORD password,ta.access_code accesscode "
		+"FROM test_roster tr, test_admin ta, student stu "
		+"WHERE " 
		+"tr.test_admin_id = ta.test_admin_id "
		+"AND tr.test_roster_id = ? "
		+"AND stu.student_id = tr.student_id";
	private static final String RESTART_RESPONSES_SQL = "select  ir.item_id as itemId, its.subject contentArea, ir.response_seq_num as responseSeqNum,  ir.student_marked as studentMarked,  i.item_type as itemType,  isi.item_sort_order as itemSortOrder,  ir.response as response, TO_CLOB(decode (i.answer_area,'AudioItem', decode(length(icr.constructed_response),'','',ir.test_roster_id || '_' || ir.item_id), DBMS_LOB.SUBSTR(icr.constructed_response, 4000, 1))) as constructedResponse, ir.response_elapsed_time as responseElapsedTime,  decode(ir.response, i.correct_answer, 1, 0) as score,  i.ads_item_id as eid, decode(i.answer_area,'AudioItem','T','F') as audioItem from  item_response ir,  item i,  item_response_cr icr,  item_set_item isi  ,item_set its where  ir.test_roster_id = ?  and ir.item_set_id = ?  and ir.item_id = i.item_id and its.item_set_id = ir.item_set_id and ir.item_set_id = isi.item_set_id  and ir.item_id = isi.item_id AND its.item_set_type = 'TD' and its.sample = 'F' and ir.response_seq_num =  (select  max(ir1.response_seq_num)  from  item_response ir1  where  ir1.item_set_id = ir.item_set_id  and ir1.item_id = ir.item_id  and ir1.test_roster_id = ir.test_roster_id) \t  AND ir.test_roster_id = icr.test_roster_id (+) \t  AND ir.item_set_id = icr.item_set_id (+) \t  AND ir.item_id = icr.item_id (+) order by  ir.response_seq_num asc";
	private static final String UPDATE_SCORE="UPDATE student_item_set_status siss SET siss.objective_score = ? ,siss.ability_score = ? ,siss.sem_score = ? WHERE siss.item_set_id=? AND siss.test_roster_id=?";
	private static final String UPDATE_RESCORESTATUS="UPDATE test_roster_temp tr SET tr.rescored = 'T' WHERE tr.test_roster_id=?";
	private static final String CHECK_RESCORESTATUS="SELECT tr.rescored rescored FROM test_roster_temp tr WHERE tr.test_roster_id=?";
	
	
	public ArrayList<TestData> getRoster(int rosterId, Connection conn){
		PreparedStatement stmt;
		String key = null;
		try{
			stmt = conn.prepareStatement(GET_ROSTER_DATA);
			stmt.setInt(1, rosterId);
			ResultSet rs1 = stmt.executeQuery();
			while (rs1.next()) {
				key = rs1.getString("username") + ":" + rs1.getString("password") + ":" + rs1.getString("accesscode");
			}
			rs1.close();
			return getRosterData(conn,key);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<TestData> getRosterData(Connection conn, String key)  throws Exception {
		String username = key.substring(0, key.indexOf(":"));
    	key = key.substring(key.indexOf(":") + 1, key.length());
    	String password = key.substring(0, key.indexOf(":"));
    	key = key.substring(key.indexOf(":") + 1, key.length());
    	String accessCode = key;

    	StudentCredentials creds = new StudentCredentials();
    	creds.setUsername(username);
    	creds.setPassword(password);
    	creds.setAccesscode(accessCode);
    	
    	return getRosterData(conn, creds);
	}
	
	private ArrayList<TestData> getRosterData(Connection conn, StudentCredentials creds)  throws Exception {
    	String username = creds.getUsername();
    	String password = creds.getPassword();
    	String accessCode = creds.getAccesscode();

    	// might be more than one roster for these creds, due to random passwords
    	AuthenticationData [] authDataArray = authenticateStudentByCreds(conn, username, password);
    	return generateRosterData(conn, authDataArray);
    }
	
	private ArrayList<TestData> generateRosterData (Connection conn, AuthenticationData [] authDataArray) throws Exception {
		ArrayList itemSetData = new ArrayList();
    	AuthenticationData authData = null;
        int testRosterId = -1;
        for(int a=0;authDataArray != null && a<authDataArray.length;a++) {
            authData = authDataArray[a];
            testRosterId = authData.getTestRosterId();
            itemSetData = getItemSetData(conn, String.valueOf(testRosterId));
        }
        if(!checkRescoredStatus(conn,testRosterId)){
        	Main.enqueueRoster(new ScoringMessage(System.currentTimeMillis(), new Integer(testRosterId).toString()));
	        if(authData != null) {    
		        for(int i=0; i<itemSetData.size() ;i++) {
		        	ItemSet iSet = (ItemSet)itemSetData.get(i);
		            if(Constants.StudentTestCompletionStatus.COMPLETED_STATUS.equals(iSet.getStudentTestCompletionStatus())) {
	                	ItemResponseData[] itemResponseData = getRestartItemResponses(conn, testRosterId, iSet.getId());
	                    RosterData.generateRestartData(itemResponseData);
		            }
			        if(RosterData.restartItemCount>0){
				        CATEngineProxy.initCAT(cArea);
			        	CATEngineProxy.restartCAT(RosterData.restartItemCount,RosterData.restartItemsArr,RosterData.restartItemsRawScore);
			        	
			        	Double abilityScore = CATEngineProxy.getAbilityScore();
	        			Double sem = CATEngineProxy.getSEM();
	        			String objScore = CATEngineProxy.getObjScore();	
	        			
	        			TestData tData = new TestData();
	        			tData.setTestRosterId(testRosterId);
	        			tData.setItemSetId(iSet.getId());
	        			tData.setAbilityScore(abilityScore);
	        			tData.setSem(sem);
	        			tData.setObjScore(objScore);
	        			
	        			testData.add(tData);
			        }
		        }
	        } 
        }
        return testData;
    }
	
	public void updateScore(Connection con, ArrayList<TestData> allTestData){
		PreparedStatement stmt1 = null;
		
		try{
			stmt1 = con.prepareStatement(UPDATE_SCORE);
			if(allTestData.size() > 0){
				Iterator itr = allTestData.iterator();
				while(itr.hasNext()){
					TestData tData = new TestData();
					tData = (TestData) itr.next();
					stmt1.setString(1, tData.getObjScore());
					stmt1.setFloat(2, tData.getAbilityScore().floatValue());
					stmt1.setFloat(3, tData.getSem().floatValue());
					stmt1.setInt(4, tData.getItemSetId());
					stmt1.setInt(5, tData.getTestRosterId());
					stmt1.addBatch();				
				}
				stmt1.executeBatch();
				con.commit();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				System.err.println("Exception occured in update score"+e);
			}
		}
	}
	
	public void updateRescoredStatus(Connection con, ArrayList<TestData> allTestData){
		PreparedStatement stmt1 = null;
		try{
			stmt1 = con.prepareStatement(UPDATE_RESCORESTATUS);
			if(allTestData.size() > 0){
				Iterator itr = allTestData.iterator();
				while(itr.hasNext()){
					TestData tData = new TestData();
					tData = (TestData) itr.next();
					stmt1.setInt(1, tData.getTestRosterId());
					stmt1.addBatch();				
				}
				stmt1.executeBatch();
				con.commit();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				System.err.println("Exception occured in update score"+e);
			}
		}
	}
	
	private boolean checkRescoredStatus(Connection con, int testRosterId){
		PreparedStatement stmt1 = null;
		boolean isRescored=false; 
		
		try{
			stmt1 = con.prepareStatement(CHECK_RESCORESTATUS);
			stmt1.setInt(1, testRosterId);
			ResultSet rs = stmt1.executeQuery();
			while(rs.next()){
				if(rs.getString("rescored").equalsIgnoreCase("T")){
					isRescored = true;
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				System.err.println("Exception occured in update score"+e);
			}
		}
		return isRescored;
	}
	
	private ArrayList getItemSetData(Connection con, String testRosterId){
		PreparedStatement stmt1 = null;
		ArrayList<ItemSet> dataSet = new ArrayList();
		try {
			stmt1 = con.prepareStatement(ITEMSET_SQL);
			stmt1.setString(1, testRosterId);
			ResultSet rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				ItemSet itemSet = new ItemSet();
				itemSet.setId(rs1.getInt("ID"));
				itemSet.setStudentTestCompletionStatus(rs1.getString("COMPLETIONSTATUS"));
				dataSet.add(itemSet);
			}
			return dataSet;
		} catch (Exception e) {
			e.printStackTrace();
			//manifests = null;
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		return dataSet;
	}
	
	
	private static AuthenticationData [] authenticateStudentByCreds(Connection con, String username, String password) {
    	AuthenticationData[] data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(AUTHENTICATE_STUDENT_SQL);
			stmt1.setString(1, username);
			stmt1.setString(2, password);
			ResultSet rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				data = new AuthenticationData[1];
				AuthenticationData auth = new AuthenticationData();
				data[0] = auth;
				auth.setCaptureMethod(rs1.getString("captureMethod"));
				auth.setDailyEndTime(rs1.getTimestamp("dailyEndTime"));
				auth.setDailyStartTime(rs1.getTimestamp("dailyStartTime"));
				auth.setRandomDistractorSeedNumber(rs1.getInt("randomDistractorSeedNumber"));
				auth.setRestartNumber(rs1.getInt("restartNumber"));
				auth.setRosterTestCompletionStatus(rs1.getString("rosterTestCompletionStatus"));
				auth.setStudentFirstName(rs1.getString("studentFirstName"));
				auth.setStudentId(rs1.getInt("studentId"));
				auth.setStudentLastName(rs1.getString("studentLastName"));
				auth.setStudentMiddleName(rs1.getString("studentMiddleName"));
				auth.setTestAdminId(rs1.getInt("testAdminId"));
				auth.setTestAdminStatus(rs1.getString("testAdminStatus"));
				auth.setTestRosterId(rs1.getInt("testRosterId"));
				auth.setTimeZone(rs1.getString("timeZone"));
				auth.setTtsSpeedStatus(rs1.getString("ttsSpeedStatus"));
				auth.setWindowEndDate(rs1.getTimestamp("windowEndDate"));
				auth.setWindowStartDate(rs1.getTimestamp("windowStartDate"));	
			}
			rs1.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		return data;
	}
	
	private static ItemResponseData[] getRestartItemResponses(Connection con, int testRosterId, int itemSetId) {
		String contentArea = null;
		ArrayList <ItemResponseData> data = new ArrayList<ItemResponseData>();
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(RESTART_RESPONSES_SQL);
			stmt1.setInt(1, testRosterId);
			stmt1.setInt(2, itemSetId);
			ResultSet rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				ItemResponseData response = new ItemResponseData();
				//response.setConstructedResponse(rs1.getString("constructedResponse"));
				response.setEid(rs1.getInt("eid"));
				response.setItemId(rs1.getString("itemId"));
				response.setItemSortOrder(rs1.getInt("itemSortOrder"));
				response.setItemType(rs1.getString("itemType"));
				response.setResponse(rs1.getString("response"));
				response.setResponseElapsedTime(rs1.getInt("responseElapsedTime"));
				response.setResponseSeqNum(rs1.getString("responseSeqNum"));
				response.setScore(rs1.getInt("score"));
				response.setStudentMarked(rs1.getString("studentMarked"));
				response.setConstructedResponse(rs1.getString("constructedResponse"));
				response.setTestRosterId(testRosterId);
				response.setAudioItem("T".equals(rs1.getString("audioItem"))?true:false);
				//response.setResponseType(("CR".equals(response.getItemType())?BaseType.STRING:BaseType.IDENTIFIER).toString());
				response.setContentArea(rs1.getString("contentArea"));
				contentArea = rs1.getString("contentArea");
				data.add(response);
			}
			if(contentArea!=null){
				if(contentArea.equalsIgnoreCase("Mathematics Computation")){
					cArea="MC";
				}else if(contentArea.equalsIgnoreCase("Language")){
					cArea="LA";
				}else if(contentArea.equalsIgnoreCase("Reading")){
					cArea="RD";
				}else if(contentArea.equalsIgnoreCase("Applied Mathematics")){
					cArea="AM";
				}
			}
			rs1.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		return data.toArray(new ItemResponseData[0]);
	}
}
