package com.ctb.control.db;

import java.sql.SQLException;

import org.apache.beehive.controls.system.jdbc.JdbcControl;
import org.apache.beehive.controls.api.bean.ControlExtension;
import com.ctb.bean.testDelivery.loadTest.LoadTestConfig;
import com.ctb.bean.testDelivery.loadTest.LoadTestRoster;



@ControlExtension
@JdbcControl.ConnectionDataSource(jndiName = "oasDataSource")
public interface LoadTestDB extends JdbcControl {
	/*
	Sample database control javabean and method.

	static public class Customer { 
	    private int id; 
	    private String name; 
	
	    public int getId() { return id; }
	    public void setId(int i) { id = i; }
	
	    public String getName() { return name; }
	    public void setName(String n) { name = n; }
	}

	@JdbcControl.SQL(statement="SELECT ID, NAME FROM CUSTOMERS WHERE ID = {id}") 
	Customer findCustomer(int id) throws SQLException;
	 */
	static final long serialVersionUID = 1L;
	
	 // Changes for defect 65267
	 //START
	@JdbcControl.SQL(statement = "select  NVL(run_load,'X') as runLoad,  NVL(max_load,-1) as maxLoad,  to_char(NVL(run_date,to_date('01-JAN-1900')),'YYYY-MM-DD HH24:MI:SS') as runDate, nvl(ramp_up_time,0) as rampUpTime, nvl(filter_sites,'N') as filterSites,NVL(allow_test_simulation,'X') as allowTestSimulation from load_test_config")
	LoadTestConfig getLoadTestConfig() throws SQLException;	
	//END
	@JdbcControl.SQL(statement = "select to_char(tr.test_roster_id) AS testRosterId, tr.password as password, ta.access_code AS accessCode, s.user_name AS loginId FROM test_roster tr, test_admin ta, student s, load_test_rosters ltr WHERE ltr.test_roster_id = tr.test_roster_id AND ltr.used_flag != 'Y' AND tr.test_admin_id = ta.test_admin_id AND tr.student_id = s.student_id AND ROWNUM < 30")
	LoadTestRoster [] getLoadTestRoster() throws SQLException;

	@JdbcControl.SQL(statement = "SELECT TO_CHAR(TR.TEST_ROSTER_ID) AS TESTROSTERID, TR.PASSWORD AS PASSWORD, TA.ACCESS_CODE AS ACCESSCODE, S.USER_NAME AS LOGINID FROM TEST_ROSTER TR, TEST_ADMIN TA, STUDENT S, LOAD_TEST_ROSTERS LTR, LOAD_TEST_STATISTICS LTS WHERE LTR.TEST_ROSTER_ID = TR.TEST_ROSTER_ID AND LTR.USED_FLAG = 'Y' AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID AND TR.STUDENT_ID = S.STUDENT_ID AND LTS.SYSTEM_ID = {systemId} AND LTS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID AND TR.TEST_COMPLETION_STATUS = 'SC' AND ROWNUM = 1")
	LoadTestRoster getAssignedLoadTestRoster(String systemId) throws SQLException;
	
	@JdbcControl.SQL(statement = "update load_test_rosters set used_flag = 'Y' where test_roster_id = {testRosterId}")
	int setUsedFlag(Integer testRosterId) throws SQLException;

	@JdbcControl.SQL(statement = "insert into load_test_statistics(system_id, test_roster_id, start_time) values({systemId},{testRosterId}, sysdate )")
	int createStatisticsRecord(String systemId, Integer testRosterId) throws SQLException;
	
	@JdbcControl.SQL(statement = "update load_test_statistics set end_time = sysdate, max_response_time = {maxResponseTime}, min_response_time = {minResponseTime}, avg_response_time = {avgResponseTime}, success_count = {successCount}, failure_count = {failureCount} where  system_id = {systemId} and test_roster_id = {testRosterId}")
	int updateStatistics(String systemId, Integer testRosterId, Integer maxResponseTime, Integer minResponseTime, Integer avgResponseTime, Integer successCount, Integer failureCount ) throws SQLException;

	@JdbcControl.SQL(statement = "select to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS') from dual")
	String getServerTime() throws SQLException;
	
	@JdbcControl.SQL(statement = "select count(1) from test_roster where test_completion_status = 'IP'")
	int getInprogressRosters() throws SQLException;
	
	@JdbcControl.SQL(statement = "INSERT INTO site_info(SITE_ID,SYSTEM_ID,OS_NAME,OS_VERSION,SYSTEM_MODEL,PHYSICAL_MEMORY,VIRTUAL_MEMORY,PROCESSORS,NETWORK_CARDS, CREATED_DATE, UPDATED_DATE) VALUES({siteId},{systemId},{osName},{osVersion},{systemModel},{physicalMemory},{virtualMemory},{processors},{networkCards}, sysdate, sysdate)")
	int insertSystemInfo(String siteId, String systemId, String osName, String osVersion, String systemModel, String physicalMemory, String virtualMemory, String processors, String networkCards) throws SQLException;

	@JdbcControl.SQL(statement = "UPDATE site_info SET OS_NAME = {osName} ,OS_VERSION = {osVersion}, SYSTEM_MODEL = {systemModel}, PHYSICAL_MEMORY= {physicalMemory}, VIRTUAL_MEMORY = {virtualMemory}, PROCESSORS = {processors} , NETWORK_CARDS = {networkCards}, UPDATED_DATE = sysdate WHERE system_id = {systemId}")
	int updateSystemInfo(String systemId, String osName, String osVersion, String systemModel, String physicalMemory, String virtualMemory, String processors, String networkCards) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT COUNT(1) FROM ALLOWED_SITES WHERE LOWER(CORP_ID) = LOWER({corpId})")
	int allowedSite(String corpId) throws SQLException;
	
	@JdbcControl.SQL(statement = "select iset.item_set_id  from item_Set iset where iset.ads_ob_asmt_id = {adsSubtestId}")
	int getTDItemSetId(int adsSubtestId) throws SQLException;
	
	@JdbcControl.SQL(statement = "select isa.ancestor_item_set_id from item_set_ancestor isa where isa.ancestor_item_set_type = 'TC' and isa.item_set_id = {TDSubtestId}")
	int getTCItemSetId(int TDSubtestId) throws SQLException;
	
	@JdbcControl.SQL(statement = " select  tc.block_download_flag from test_catalog tc where tc.item_set_id = {TCSubtestId}")
	String getBlockFlag(int TCSubtestId) throws SQLException;

}