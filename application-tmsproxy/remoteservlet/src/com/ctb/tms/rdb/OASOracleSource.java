package com.ctb.tms.rdb; 

import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import noNamespace.BaseType;
import noNamespace.EntryType;
import noNamespace.StereotypeType;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ast;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Ov;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData.Tsd.Ist.Rv;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco.ScoUnitType;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.TestingSessionData.LmsStudentAccommodations;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.TestingSessionData.LmsStudentAccommodations.StereotypeStyle;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Tutorial;

import com.ctb.tms.bean.login.AccommodationsData;
import com.ctb.tms.bean.login.AuthenticationData;
import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.ManifestData;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.ScratchpadData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.bean.login.TestProduct;
import com.ctb.tms.exception.testDelivery.AuthenticationFailureException;
import com.ctb.tms.exception.testDelivery.KeyEnteredResponsesException;
import com.ctb.tms.exception.testDelivery.OutsideTestWindowException;
import com.ctb.tms.exception.testDelivery.TestSessionCompletedException;
import com.ctb.tms.exception.testDelivery.TestSessionInProgressException;
import com.ctb.tms.exception.testDelivery.TestSessionNotScheduledException;
import com.ctb.tms.util.Constants;

public class OASOracleSource implements OASRDBSource
{ 
	private static volatile boolean haveDataSource = true;
	private static String OASDatabaseURL = "jdbc:oracle:thin:@nj09mhe0393-vip.mhe.mhc:1521:oasr5t1";
	private static String OASDatabaseUser = "oas";
	private static String OASDatabaseUserPassword = "oaspr5r";
	private static String OASDatabaseJDBCDriver = "oracle.jdbc.driver.OracleDriver";
	
	private static final String ROSTER_COMPLETION_STATUS_SQL = "update  test_roster set  test_completion_status = {status},  restart_number = {restartNumber},  start_date_time = nvl(start_date_time,{timestamp}),  last_login_date_time = {timestamp},  capture_method = {captureMethod},  updated_date_time = {timestamp},  last_mseq = {mseq},  correlation_id = null where  test_roster_id = {testRosterId}";
	private static final String AUTHENTICATE_STUDENT_SQL = "select  ros.test_roster_id as testRosterId,  stu.student_id as studentId,  stu.last_name as studentLastName,  stu.first_name as studentFirstName,  stu.middle_name as studentMiddleName,  ros.test_completion_status as rosterTestCompletionStatus,  adm.login_start_date as windowStartDate,  adm.login_end_date as windowEndDate,  adm.daily_login_start_time as dailyStartTime,  adm.daily_login_end_time as dailyEndTime,  adm.test_admin_status as testAdminStatus,  adm.time_zone AS timeZone,  ros.capture_method as captureMethod,  ros.restart_number as restartNumber,  ros.test_admin_id as testAdminId, \t  ros.random_distractor_seed as randomDistractorSeedNumber, \t  ros.tts_speed_status as ttsSpeedStatus from  student stu,  test_roster ros,  test_admin adm where  adm.test_admin_id = ros.test_admin_id  and ros.student_id = stu.student_id  and stu.activation_status = 'AC'  and ros.activation_status = 'AC'  and adm.activation_status = 'AC'  and upper(stu.user_name) = upper(?)  and upper(ros.password) = upper(?)";    
	private static final String ACCOMMODATIONS_SQL = "select  accom.student_id as studentId,  accom.screen_magnifier as screenMagnifier,  accom.screen_reader as screenReader,  accom.calculator as calculator,  accom.test_pause as testPause,  accom.untimed_test as untimedTest,  accom.question_background_color as questionBackgroundColor,  accom.question_font_color as questionFontColor,  accom.question_font_size as questionFontSize,  accom.answer_background_color as answerBackgroundColor,  accom.answer_font_color as answerFontColor,  accom.answer_font_size as answerFontSize,  accom.highlighter as highlighter  from  test_roster ros,  student_accommodation accom  where  accom.student_id = ros.student_id  and ros.test_roster_id = ?";    
	private static final String MANIFEST_SQL = "select scoOrder,  scoParentId,  adminForceLogout,  showStudentFeedback,  id,  title,  testTitle,  scoDurationMinutes,  0 as totalTime,  scoUnitType,  scoEntry,  completionStatus,  asmtHash,  asmtEncryptionKey,  itemEncryptionKey,  adsid,  randomDistractorStatus  from (select siss.item_Set_order as scoOrder,  isp.parent_item_Set_id as scoParentId,  ta.force_logout as adminForceLogout,  ta.show_student_feedback as showStudentFeedback,  iset.item_set_id as id,  iset.item_set_name as title,  test.item_set_name as testTitle,  iset.time_limit / 60 as scoDurationMinutes,  'SUBTEST' as scoUnitType,  'ab-initio' as scoEntry,  siss.completion_status as completionStatus,  iset.asmt_hash as asmtHash,  iset.asmt_encryption_key as asmtEncryptionKey,  iset.item_encryption_key as itemEncryptionKey,  iset.ads_ob_asmt_id as adsid,  ta.test_admin_id testid,  ta.random_distractor_status as randomDistractorStatus  from item_set_item  isi,  item_Set  iset,  item_set  test,  student_item_set_status siss,  test_roster  tr,  test_admin  ta,  item_set_parent  isp,  test_admin_item_set  tais  where isi.item_set_id = iset.item_set_id  and iset.item_set_id = siss.item_set_id  and iset.item_set_type = 'TD'  and siss.test_roster_id = tr.test_roster_id  and ta.test_admin_id = tr.test_admin_id  and isp.item_Set_id = iset.item_set_id  and tr.test_roster_id = ?  and tais.item_set_id = isp.parent_item_set_id  and test.item_set_id = ta.item_set_id  and upper(tais.access_code) = upper(?)  and tais.test_admin_id = ta.test_admin_id  group by siss.item_Set_order,  isp.parent_item_set_id,  ta.force_logout,  ta.show_student_feedback,  iset.item_Set_id,  iset.item_set_name,  test.item_set_name,  iset.time_limit,  isi.item_sort_order,  siss.completion_status,  iset.asmt_hash,  iset.asmt_encryption_key,  iset.item_encryption_key,  iset.ads_ob_asmt_id,  iset.item_set_level,  ta.test_admin_id,  ta.random_distractor_status)  group by scoOrder,  scoParentId,  adminForceLogout,  showStudentFeedback,  id,  title,  testTitle,  scoDurationMinutes,  scoUnitType,  scoEntry,  completionStatus,  asmtHash,  asmtEncryptionKey,  itemEncryptionKey,  adsid,  randomDistractorStatus  order by scoOrder";
	private static final String SUBTEST_ELAPSED_TIME_SQL = "select  nvl(sum(max(resp.response_elapsed_time)), 0) as totalTime from  item_response resp where  resp.test_roster_id = ?  and resp.item_set_id = ? group by  resp.test_roster_id,  resp.item_set_id,  resp.item_id";
	private static final String IS_ULTIMATE_ACCESS_CODE_SQL = "select  decode(count(siss.item_set_id), 0, 'T', 'F') from \t  student_item_set_status siss,  test_Admin_item_set tais,  item_set_parent isp where  tais.test_Admin_id = ?  and siss.test_roster_id = ?  and upper(tais.access_code) != upper(?) \t  and isp.parent_item_set_id = tais.item_set_id \t  and siss.item_set_id = isp.item_set_id \t  and siss.completion_status != 'CO'";
	private static final String RESTART_RESPONSES_SQL = "select  ir.item_id as itemId,  ir.response_seq_num as responseSeqNum,  ir.student_marked as studentMarked,  i.item_type as itemType,  isi.item_sort_order as itemSortOrder,  ir.response as response,  icr.constructed_response as constructedResponse,  ir.response_elapsed_time as responseElapsedTime,  decode(ir.response, i.correct_answer, 1, 0) as score,  i.ads_item_id as eid from  item_response ir,  item i,  item_response_cr icr,  item_set_item isi where  ir.test_roster_id = ?  and ir.item_set_id = ?  and ir.item_id = i.item_id  and ir.item_set_id = isi.item_set_id  and ir.item_id = isi.item_id  and ir.response_seq_num =  (select  max(ir1.response_seq_num)  from  item_response ir1  where  ir1.item_set_id = ir.item_set_id  and ir1.item_id = ir.item_id  and ir1.test_roster_id = ir.test_roster_id) \t  AND ir.test_roster_id = icr.test_roster_id (+) \t  AND ir.item_set_id = icr.item_set_id (+) \t  AND ir.item_id = icr.item_id (+) order by  isi.item_sort_order";
	private static final String TABE_LOCATOR_MANIFEST_SQL = "select  siss.item_Set_order as scoOrder, \t  iset.item_set_id as id, \t  iset.item_set_name as title, \t  siss.completion_status as completionStatus, \t  iset.asmt_hash as asmtHash, \t  iset.asmt_encryption_key as asmtEncryptionKey, \t  iset.item_encryption_key as itemEncryptionKey, \t  iset.ads_ob_asmt_id as adsid  from student_item_set_status siss, item_set iset where siss.item_set_id = iset.item_set_id and siss.test_roster_id = {testRosterId} and iset.item_set_level = 'L'";
	private static final String MANIFEST_BY_ROSTER_SQL = "select \t  scoOrder, \t  scoParentId, \t  adminForceLogout, \t  showStudentFeedback, \t  id, \t  title, \t  testTitle, \t  scoDurationMinutes, \t  0 as totalTime, \t  scoUnitType, \t  scoEntry, \t  completionStatus, \t  asmtHash, \t  asmtEncryptionKey, \t  itemEncryptionKey, \t  adsid,  accessCode\t from (  select \t  siss.item_Set_order as scoOrder, \t  isp.parent_item_Set_id as scoParentId, \t  ta.force_logout as adminForceLogout, \t  ta.show_student_feedback as showStudentFeedback, \t  iset.item_set_id as id, \t  iset.item_set_name as title, \t  test.item_set_name as testTitle, \t  iset.time_limit / 60 as scoDurationMinutes, \t  'SUBTEST' as scoUnitType, \t  'ab-initio' as scoEntry, \t  siss.completion_status as completionStatus, \t  iset.asmt_hash as asmtHash, \t  iset.asmt_encryption_key as asmtEncryptionKey, \t  iset.item_encryption_key as itemEncryptionKey, \t  iset.ads_ob_asmt_id as adsid,  tais.access_code as accessCode  from  item_set_item isi,  item_Set iset,  item_set test,  student_item_set_status siss,  test_roster tr,  test_admin ta,  item_set_parent isp,  test_admin_item_set tais  where  isi.item_set_id = iset.item_set_id  and iset.item_set_id = siss.item_set_id  and iset.item_set_type = 'TD'  and siss.test_roster_id = tr.test_roster_id  and ta.test_admin_id = tr.test_admin_id  and isp.item_Set_id = iset.item_set_id  and tr.test_roster_id = {testRosterId}  and tais.item_set_id = isp.parent_item_set_id  and test.item_set_id = ta.item_set_id  and tais.test_admin_id = ta.test_admin_id  group by  siss.item_Set_order,  isp.parent_item_set_id,  ta.force_logout,  ta.show_student_feedback,  iset.item_Set_id,  iset.item_set_name,  test.item_set_name,  iset.time_limit,  isi.item_sort_order,  siss.completion_status,  iset.asmt_hash,  iset.asmt_encryption_key,  iset.item_encryption_key,  iset.ads_ob_asmt_id,  tais.access_code) group by scoOrder, \t  scoParentId, \t  adminForceLogout, \t  showStudentFeedback, \t  id, \t  title, \t  testTitle, \t  scoDurationMinutes, \t  scoUnitType, \t  scoEntry, \t  completionStatus, \t  asmtHash, \t  asmtEncryptionKey, \t  itemEncryptionKey, \t  adsid,  accessCode \torder by \t  scoOrder";   
	private static final String PRODUCT_LOGO_SQL = "select resource_uri  from PRODUCT_RESOURCE  where resource_type_code = 'TDCLOGO'  and product_id = ?";    
	private static final String TUTORIAL_RESOURCE_SQL = "select pr.resource_uri from product pp, product cp, product_resource pr where pp.product_id = cp.parent_product_id and pp.product_id = pr.product_id and pr.resource_type_code = 'TUTORIAL' and cp.product_id in ( select product_id from test_admin where test_admin_id in  (select test_admin_id from test_roster where test_roster_id = ?))";    
	private static final String TUTORIAL_TAKEN_SQL = "select count(*) as counter from test_roster tr, test_admin ta, student_tutorial_status sts where tr.test_admin_id = ta.test_admin_id and ta.product_id = sts.product_id and tr.student_id = sts.student_id and sts.completion_status = 'CO' and tr.test_roster_id = ?";    
	private static final String SCRATCHPAD_CONTENT_SQL = "select scratchpad_content, item_set_id from student_item_set_status where test_roster_id = ?";
	private static final String UPDATE_TEST_ROSTER_WITH_RD_SEED_SQL = "update  test_roster set  random_distractor_seed = {rndNumber} where  test_roster_id = {testRosterId}";
	private static final String SPEECH_CONTROLLER_SQL = "select cconfig.default_value as speechControllerFlag  from test_roster  ros,  customer  cus,  customer_configuration cconfig,  student_accommodation  accom  where accom.screen_reader = 'T'  and accom.student_id = ros.student_id  and cconfig.customer_configuration_name = 'Allow_Speech_Controller'  and cconfig.customer_id = cus.customer_id  and cus.customer_id = ros.customer_id  and ros.test_roster_id = {testRosterId}";
	private static final String TEST_PRODUCT_FOR_ADMIN_SQL = "select distinct  prod.product_id as productId,  prod.product_name as productName,  prod.version as version,  prod.product_description as productDescription,  prod.created_by as createdBy,  prod.created_date_time as createdDateTime,  prod.updated_by as updatedBy,  prod.updated_date_time as updatedDateTime,  prod.activation_status as activationStatus,  prod.product_type as productType,  prod.scoring_item_set_level as scoringItemSetLevel,  prod.preview_item_set_level as previewItemSetLevel,  prod.parent_product_id as parentProductId,  prod.ext_product_id as extProductId,  prod.content_area_level as contentAreaLevel,  prod.internal_display_name as internalDisplayName,  prod.sec_scoring_item_set_level as secScoringItemSetLevel,  prod.ibs_show_cms_id as ibsShowCmsId,  prod.printable as printable,  prod.scannable as scannable,  prod.keyenterable as keyenterable,  prod.branding_type_code as brandingTypeCode,  prod.acknowledgments_url as acknowledgmentsURL,  prod.show_student_feedback as showStudentFeedback,  prod.static_manifest as staticManifest,  prod.session_manifest as sessionManifest,  prod.subtests_selectable as subtestsSelectable,  prod.subtests_orderable as subtestsOrderable,  prod.subtests_levels_vary as subtestsLevelsVary,  cec.support_phone_number as supportPhoneNumber,  prod.off_grade_testing_disabled as offGradeTestingDisabled from  product prod, test_admin adm, customer_email_config cec where  prod.product_id = adm.product_id  and cec.customer_id (+) = adm.customer_id  and adm.test_admin_id = ?";
	private static final String ACTIVE_ROSTERS_SQL = "select distinct stu.user_name as username, tr.password as password, tais.access_code as accesscode from test_roster tr, test_admin ta, test_admin_item_set tais, student stu where tr.test_completion_status in ('SC', 'IN', 'IS', 'IP') and sysdate > (TA.LOGIN_START_DATE - 3) and sysdate < (TA.LOGIN_END_DATE + 3) and ((tr.updated_date_time > sysdate - 30 and ta.updated_date_time > sysdate - 30) OR (stu.user_name > 'pt-student1450000' and stu.user_name < 'pt-student1500000') OR (tr.updated_date_time is null and ta.updated_date_time > sysdate - 30)) and ta.test_admin_id = tr.test_admin_id and tais.test_admin_id = ta.test_admin_id and stu.student_id = tr.student_id and rownum < 1000";
	
	{
		try {
			ResourceBundle rb = ResourceBundle.getBundle("env");
			OASDatabaseJDBCDriver = rb.getString("oas.db.driver");
			OASDatabaseURL = rb.getString("oas.db.url");
			OASDatabaseUser = rb.getString("oas.db.user");
			OASDatabaseUserPassword = rb.getString("oas.db.password");
			haveDataSource = true;
		} catch (Exception e) {
			System.out.println("***** No OAS DB connection info specified in env.properties, using static defaults");
			//e.printStackTrace();
		}
	}
	
	public StudentCredentials [] getActiveRosters(Connection con) {
    	StudentCredentials[] data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(ACTIVE_ROSTERS_SQL);
			ResultSet rs1 = stmt1.executeQuery();
			ArrayList<StudentCredentials> dataList = new ArrayList<StudentCredentials>();
			while (rs1.next()) {
				StudentCredentials creds = new StudentCredentials();
				creds.setUsername(rs1.getString("username"));
				creds.setPassword(rs1.getString("password"));
				creds.setAccesscode(rs1.getString("accesscode"));
				dataList.add(creds);
			}
			rs1.close();
			data = (StudentCredentials[]) dataList.toArray(new StudentCredentials[0]);
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
	
    public RosterData getRosterData(Connection conn, StudentCredentials creds)  throws Exception {
    	String username = creds.getUsername();
    	String password = creds.getPassword();
    	String testAccessCode = creds.getAccesscode();
    	TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance();
        LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
        loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.OK_STATUS);
        response.getTmssvcResponse().setMethod("login_response");

    	// might be more than one roster for these creds, due to random passwords
    	AuthenticationData [] authDataArray = authenticateStudent(conn, username, password);
    	//System.out.print("1");
        AuthenticationData authData = null;
        boolean authenticated = false;
        int testRosterId = -1;
        String lsid = null;
        ManifestData [] manifestData = new ManifestData [0];
        for(int a=0;authDataArray != null && a<authDataArray.length && !authenticated;a++) {
            authData = authDataArray[a];
            testRosterId = authData.getTestRosterId();
            lsid = String.valueOf(testRosterId) + ":" + testAccessCode;
            loginResponse.setLsid(lsid);
            manifestData = getManifest(conn, testRosterId, testAccessCode);
           // System.out.print("2");
            if(manifestData.length > 0) {
                authenticated = true;
                ScratchpadData [] scratchData = getScratchpadContent(conn, testRosterId);
                //System.out.print("3");
                HashMap<Integer, Clob> scratchMap = new HashMap<Integer, Clob>(scratchData.length);
                for (int i = 0; i < scratchData.length; i++) {
                	scratchMap.put(new Integer(scratchData[i].getItemSetId()), scratchData[i].getScratchpadData());
                }
                for (int i = 0; i < manifestData.length; i++) {
                	manifestData[i].setScratchpadContent(scratchMap.get(new Integer(manifestData[i].getId())));
                }
            }
        }
        if(authData != null) {    
	        if(authData.getRosterTestCompletionStatus().equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS) ||
	        		authData.getRosterTestCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS)) {
	                loginResponse.setRestartFlag(true);
	            } else {
	            	loginResponse.setRestartFlag(false);
	            }
	        	loginResponse.setRestartNumber(new BigInteger(String.valueOf(authData.getRestartNumber())));
	        
	            TestProduct testProduct = getProductForTestAdmin(conn, authData.getTestAdminId());
	            //System.out.print("4");
	            //AuthenticateStudent authenticator = authenticatorFactory.create();
	
	            if ("TB".equals(testProduct.getProductType())) {
	                for(int i = 0;i<manifestData.length;i++) {
	                    	if(!testAccessCode.equalsIgnoreCase(manifestData[i].getAccessCode()) &&
	                    			manifestData[i].getTitle().indexOf("locator") >= 0 &&
	                    			!manifestData[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS)) {
	                    		response = TmssvcResponseDocument.Factory.newInstance();
	            	            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
	            	            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.LOCATOR_SUBTEST_NOT_COMPLETED_STATUS);
	                    	}
	                }
	            }
	            
	            String logoURI = getProductLogo(conn,testProduct.getProductId());
	            //System.out.print("5");
	            if (logoURI == null || "".equals(logoURI))
	                logoURI = "/resources/logo.swf";
	            loginResponse.addNewBranding().setTdclogo(logoURI);
	
	            if (authData.getRandomDistractorSeedNumber() != null) {
	
	
				 loginResponse.setRandomDistractorSeedNumber(
						 new BigInteger(String.valueOf( authData.
								 getRandomDistractorSeedNumber())));
	
	
			 }  else {
	
				 if (manifestData[0].getRandomDistractorStatus() != null && 
						 manifestData[0].getRandomDistractorStatus().equals("Y")) {
	
					 Integer ranodmSeedNumber = generateRandomNumber();
	
					 loginResponse.setRandomDistractorSeedNumber(
							 new BigInteger( String.valueOf(ranodmSeedNumber.intValue())));
				 }
	
			 }
	        copyAuthenticationDataToResponse(loginResponse, authData);
	        AccommodationsData accomData = getAccommodations(conn, testRosterId);
	        //System.out.print("6");
	        
	        if(accomData != null) {
	            copyAccomodationsDataToResponse(loginResponse, accomData);
	        }
	
	        boolean gotRestart = false;
	        for(int i=0; i<manifestData.length ;i++) {
	            if(Constants.StudentTestCompletionStatus.SCHEDULED_STATUS.equals(manifestData[i].getCompletionStatus()) ||
	               Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS.equals(manifestData[i].getCompletionStatus()) ||
	               Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS.equals(manifestData[i].getCompletionStatus()) ||
	               Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS.equals(manifestData[i].getCompletionStatus()) ||
	               Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS.equals(manifestData[i].getCompletionStatus())) {
	                if(!gotRestart && loginResponse.getRestartFlag() && 
	                		(manifestData[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS) || 
	                		 manifestData[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS))) {
	                	ConsolidatedRestartData restartData = loginResponse.addNewConsolidatedRestartData();
	                	manifestData[i].setTotalTime(getTotalElapsedTimeForSubtest(conn, testRosterId, manifestData[i].getId()));
	                    //System.out.print("7");
	                    int remSec = (manifestData[i].getScoDurationMinutes() * 60) - manifestData[i].getTotalTime();
	                    ItemResponseData [] itemResponseData = getRestartItemResponses(conn, testRosterId, manifestData[i].getId());
	                    //System.out.print("8");
	                    //START Change For deferred defect 63502
	                    copyRestartDataToResponse(lsid, testRosterId, manifestData[i].getId(), loginResponse, itemResponseData, remSec, 
	                    		Integer.parseInt(manifestData[i].getAdsid()), manifestData[i].getScratchpadContentStr(), restartData);
	                    		//END
	                    gotRestart = true;
	                }
	            }
	        }
	        copyManifestDataToResponse(conn, loginResponse, manifestData, testRosterId, authData.getTestAdminId(), testAccessCode);
	
	        String tutorialResource = getTutorialResource(conn, testRosterId);
	        //System.out.print("9");
	        boolean wasTutorialTaken = wasTutorialTaken(conn, testRosterId);
	        //System.out.print("10");
	        if (tutorialResource!= null && !tutorialResource.trim().equals("")) {
	            Tutorial tutorial =loginResponse.addNewTutorial();
	            tutorial.setTutorialUrl(tutorialResource);
	            tutorial.setDeliverTutorial(new BigInteger(wasTutorialTaken ? "0":"1"));
	        }
	        RosterData result = new RosterData();
	        result.setDocument(response);
	        result.setAuthData(authData);
	        com.ctb.tms.bean.login.Manifest manifest = new com.ctb.tms.bean.login.Manifest();
	        manifest.setManifest(manifestData);
	        result.setManifest(manifest);
	        System.out.print("\n");
	        return result;
        } else {
        	return null;
        }
    }
    
    //START Change for Deferred defect 63502
    private static void copyRestartDataToResponse(String lsid, 
                                    int testRosterId, 
                                    int subtestItemSetId, 
                                    LoginResponse loginResponse, 
                                    ItemResponseData [] itemResponseData, 
                                    int remSec, 
                                    int adsAssessmentId, 
                                    String scratchpadContent,
                                    ConsolidatedRestartData restartData ) throws SQLException
    {
    //END
        //ConsolidatedRestartData restartData = loginResponse.addNewConsolidatedRestartData();
        Tsd tsd = restartData.addNewTsd();        
        tsd.setScid(String.valueOf(subtestItemSetId));
        tsd.setLsid(lsid);
        if (scratchpadContent == null) scratchpadContent = "";
     //   tsd.addSp("<![CDATA[" + scratchpadContent + "]]>");
        tsd.addSp(scratchpadContent);
        Ast ast = tsd.addNewAst();
        ast.setRemSec(Float.parseFloat(String.valueOf(remSec)));
        int maxRSN = 0;
        for(int i=0;i<itemResponseData.length;i++) {
            ItemResponseData data = itemResponseData[i];
            tsd.addNewIst();
            Ist ist = tsd.getIstArray(i);
            ist.setIid(data.getItemId());
//            ist.setEid(String.valueOf(data.getResponseSeqNum()));
            ist.setEid(""+data.getEid());
            ist.setCst(Ist.Cst.UNKNOWN);
            ist.setMrk("T".equals(data.getStudentMarked())?"1":"0");
//            ist.setAwd("1");
            ist.setDur(data.getResponseElapsedTime());
            Rv rv = ist.addNewRv();
            if ("SR".equals(data.getItemType())) {
                rv.setT(BaseType.IDENTIFIER);
                rv.setV(data.getResponse());
            }
            else { 
                rv.setT(BaseType.STRING);
                String crResponse = "";
                Clob crResponseClob = data.getConstructedResponse();
                if (crResponseClob != null) {
                    int length = (int) crResponseClob.length();
                    crResponse = crResponseClob.getSubString(1, length);
                }
                rv.setV(crResponse);
            }
            rv.setN("RESPONSE");
            
            
            Ov ov = ist.addNewOv();
            ov.setN("SCORE");
            ov.setT(BaseType.INTEGER);
            
            if ("SR".equals(data.getItemType()))
                ov.setV(""+data.getScore());
            else
                ov.setV("");

            if(data.getResponseSeqNum() > maxRSN) {
                ast.setCurEid(""+itemResponseData[i].getEid());
                maxRSN = data.getResponseSeqNum();
            }
        }
    }
    
    private static void copyAuthenticationDataToResponse(LoginResponse response, AuthenticationData authData) throws AuthenticationFailureException, KeyEnteredResponsesException, OutsideTestWindowException, TestSessionCompletedException, TestSessionInProgressException, TestSessionNotScheduledException {
        response.addNewTestingSessionData();
        response.getTestingSessionData().addNewCmiCore();
        response.getTestingSessionData().getCmiCore().setStudentId(String.valueOf(authData.getStudentId()));
        response.getTestingSessionData().getCmiCore().setStudentLastName(authData.getStudentLastName());
        response.getTestingSessionData().getCmiCore().setStudentFirstName(authData.getStudentFirstName());
        response.getTestingSessionData().getCmiCore().setStudentMiddleName(authData.getStudentMiddleName());
    }
    
    private static void copyAccomodationsDataToResponse(LoginResponse response, AccommodationsData accomData) {
        if(accomData != null) {
            response.getTestingSessionData().addNewLmsStudentAccommodations();
            LmsStudentAccommodations accommodations = response.getTestingSessionData().getLmsStudentAccommodations();
            accommodations.setCalculator( "T".equals(accomData.getCalculator()) ?  true : false );
            accommodations.setMagnifier( "T".equals(accomData.getScreenMagnifier()) ?  true : false );
            accommodations.setScreenReader( "T".equals(accomData.getScreenReader()) ?  true : false );
            accommodations.setUntimed( "T".equals(accomData.getUntimedTest()) ?  true : false );
            accommodations.setRestBreak( "T".equals(accomData.getTestPause()) ?  true : false );
            //set the boolean value in accommodations.setHighlighter depends upon wheather the highlighter is true or false.
            accommodations.setHighlighter("T".equals(accomData.getHighlighter()) ?  true : false );
            accommodations.addNewStereotypeStyle();
            StereotypeStyle directionsStereotype = accommodations.getStereotypeStyleArray(0);
            directionsStereotype.setStereotype(StereotypeType.DIRECTIONS);
            if(accomData.getQuestionBackgroundColor() != null) 
                directionsStereotype.setBgcolor(accomData.getQuestionBackgroundColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontColor() != null)
                directionsStereotype.setFontColor(accomData.getQuestionFontColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontSize() == 0)
                directionsStereotype.setFontMagnification(new Float(1.0).floatValue());
            else
                directionsStereotype.setFontMagnification(accomData.getQuestionFontSize());
            accommodations.addNewStereotypeStyle();
            StereotypeStyle stimulusStereotype = accommodations.getStereotypeStyleArray(1);
            stimulusStereotype.setStereotype(StereotypeType.STIMULUS);
            if(accomData.getQuestionBackgroundColor() != null)
                stimulusStereotype.setBgcolor(accomData.getQuestionBackgroundColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontColor() != null)
                stimulusStereotype.setFontColor(accomData.getQuestionFontColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontSize() == 0)
                stimulusStereotype.setFontMagnification(new Float(1.0).floatValue());
            else
                stimulusStereotype.setFontMagnification(accomData.getQuestionFontSize());
            accommodations.addNewStereotypeStyle();
            StereotypeStyle stemStereotype = accommodations.getStereotypeStyleArray(2);
            stemStereotype.setStereotype(StereotypeType.STEM);
            if(accomData.getQuestionBackgroundColor() != null)
                stemStereotype.setBgcolor(accomData.getQuestionBackgroundColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontColor() != null)
                stemStereotype.setFontColor(accomData.getQuestionFontColor().replaceAll("#","0x"));
            if(accomData.getQuestionFontSize() == 0)
                stemStereotype.setFontMagnification(new Float(1.0).floatValue());
            else
                stemStereotype.setFontMagnification(accomData.getQuestionFontSize());
            accommodations.addNewStereotypeStyle();
            StereotypeStyle answerStereotype = accommodations.getStereotypeStyleArray(3);
            answerStereotype.setStereotype(StereotypeType.ANSWER_AREA);
            if(accomData.getAnswerBackgroundColor() != null)
                answerStereotype.setBgcolor(accomData.getAnswerBackgroundColor().replaceAll("#","0x"));
            if(accomData.getAnswerFontColor() != null)
                answerStereotype.setFontColor(accomData.getAnswerFontColor().replaceAll("#","0x"));
            if(accomData.getAnswerFontSize() == 0)
                answerStereotype.setFontMagnification(new Float(1.0).floatValue());
            else
                answerStereotype.setFontMagnification(accomData.getAnswerFontSize());
        }
    }
    
    private static void copyManifestDataToResponse(Connection conn, LoginResponse response, ManifestData [] manifestData, int testRosterId, int testAdminId, String accessCode) throws SQLException {
        response.addNewManifest();
        Manifest manifest = response.getManifest();
        String isUltimateAccessCode = isUltimateAccessCode(conn, testRosterId, testAdminId, accessCode);
        
        if(response.getRestartFlag()) {
	        ArrayList a = new ArrayList();
	        for(int i=0;i<manifestData.length;i++) {
	        	ManifestData data = manifestData[i];
	        	if(Constants.StudentTestCompletionStatus.COMPLETED_STATUS.equals(data.getCompletionStatus())){
	        		continue;
	        	}else{
	        		a.add(data);
	        	}
	        }
	        
	        manifestData = new ManifestData[a.size()];
	        a.toArray(manifestData);
	       // manifestData = (ManifestData [])a.toArray();
        }
        
        for(int i=0;i<manifestData.length;i++) {
        	ManifestData data = manifestData[i];
        /*	if(response.getRestartFlag() && "T".equals(isUltimateAccessCode) 
        				&& Constants.StudentTestCompletionStatus.COMPLETED_STATUS.equals(data.getCompletionStatus())){
	            System.out.println("***** In If");
	            System.out.println("RestartFlag: "+response.getRestartFlag()+", isUltimateAccessCode: "
	            			+isUltimateAccessCode+", CompletionStatus: "+data.getCompletionStatus());
        		continue;
        	}else{*/
        		//System.out.println("***** In Else");
        		//System.out.println("RestartFlag: "+response.getRestartFlag()+", isUltimateAccessCode: " +isUltimateAccessCode+", CompletionStatus: "+data.getCompletionStatus());
        		manifest.setTitle(data.getTestTitle());
	            manifest.addNewSco();
	            Sco sco = manifest.getScoArray(i);
	            if(data.getAdminForceLogout().equals("T") &&
	                ((i >= manifestData.length - 1) || (data.getScoParentId() != manifestData[i+1].getScoParentId()))) {
	                sco.setForceLogout(true);
	            } else {
	                sco.setForceLogout(false);
	            }
	            if(data.getTotalTime() > 0) {
	                sco.setCmiCoreEntry(EntryType.RESUME);
	            } else {
	                sco.setCmiCoreEntry(EntryType.AB_INITIO);
	            }
	            sco.setId(String.valueOf(data.getId()));
	            sco.setScoDurationMinutes(new BigInteger(String.valueOf(data.getScoDurationMinutes())));
	            // scoUnitQuestionNumberOffset will be used to control multi-part subtest numbering
	            sco.setScoUnitQuestionNumberOffset(String.valueOf(0));
	            sco.setScoUnitType(ScoUnitType.SUBTEST);
	            sco.setTitle(data.getTitle());
	            sco.setAsmtHash(data.getAsmtHash());
	            sco.setAsmtEncryptionKey(data.getAsmtEncryptionKey());
	            sco.setItemEncryptionKey(data.getItemEncryptionKey());
	            sco.setAdsid(data.getAdsid());
	            int hours = (int) Math.floor(data.getTotalTime() / 3600);
	            int minutes = (int) Math.floor((data.getTotalTime() - (hours * 3600)) / 60);
	            int seconds = data.getTotalTime() - (hours * 3600) - (minutes * 60);
	            sco.setCmiCoreTotalTime(hours + ":" + minutes + ":" + seconds);
        	//}
        }
        //AuthenticateStudent authenticator = authenticatorFactory.create();
        if("T".equals(isUltimateAccessCode)) {
            if(manifestData.length > 0 && "T".equals(manifestData[0].getShowStudentFeedback())) {
                manifest.addNewFeedback();
                manifest.getFeedback().setId("STUDENT_FEEDBACK");
            }
        }
        manifest.addNewTerminator();
        manifest.getTerminator().setId("SEE_YOU_LATER");  
    }
	
    /**
     * Changes For Random Distractor
     */
	private static Integer generateRandomNumber () {

		final String NUM_ARRAY   = "1234567890";
		String alphaNumArray = NUM_ARRAY;

		int index = 0;

		Random rnd = new Random();

		boolean validRandom = false;
		String seed = "";
		while(!validRandom) {

			for(int i = 0; i < 3; i++) {

				index = rnd.nextInt();

				if (index < 0) {
					index = index * -1;
				}

				// make sure the index is a value within the length of our array
				if(index != 0) {
					index = index % alphaNumArray.length();
				}

				seed = seed.concat(String.valueOf(alphaNumArray.charAt(index)));
			}

			if (isNumOdd(seed)) {

				validRandom = true;
				if(verifyContainsCharFrom(NUM_ARRAY,seed)) {
					validRandom = true;
				}
			} else {

				seed = "";

			}

		}
		return Integer.valueOf(seed);

	}
    
     /**
	 *  Verify the characters of random distractor seed  
	 */
     
	private static boolean verifyContainsCharFrom(String charArray,String seed) {
		boolean verified = false;
		int j = 0;

		while(!verified && (j < seed.length())) {
			if(charArray.indexOf(String.valueOf(seed.charAt(j))) != -1) {
				verified = true;
			}
			j++;
		}
		return verified;
	}
    
    /**
     * Checking for odd Number
     */
    private static boolean isNumOdd(String seed) {

		return Integer.valueOf(String.valueOf(seed.charAt(seed.length() - 1))).
				intValue() % 2 == 0 ? false:true;
	}
    
	public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection newConn = null;
		try {    
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource)envContext.lookup("jdbc/OASDataSource");
			newConn = ds.getConnection(); 
			haveDataSource = true;
			//System.out.println("*****  Using OASDataSource for DB connection");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			haveDataSource = false;
		}

		if(!haveDataSource) {
			// no OASDataSource available, falling back on local properties
			Properties props = new Properties();
			props.put("user", OASDatabaseUser);
			props.put("password", OASDatabaseUserPassword);
			Driver driver = (Driver) Class.forName(OASDatabaseJDBCDriver).newInstance();
			newConn = driver.connect(OASDatabaseURL, props);
			//System.out.println("*****  Using local properties for OAS DB connection");
		}

		return newConn;
	}
	        
	private static AuthenticationData [] authenticateStudent(Connection con, String username, String password) {
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
    
	private static ManifestData [] getManifest(Connection con, int testRosterId, String testAccessCode) {
    	ManifestData[] data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(MANIFEST_SQL);
			stmt1.setInt(1, testRosterId);
			stmt1.setString(2, testAccessCode);
			ResultSet rs1 = stmt1.executeQuery();
			ArrayList<ManifestData> dataList = new ArrayList<ManifestData>();
			while (rs1.next()) {
				ManifestData manifest = new ManifestData();
				manifest.setAccessCode(testAccessCode);
				manifest.setAdminForceLogout(rs1.getString("adminForceLogout"));
				manifest.setAdsid(rs1.getString("adsid"));
				manifest.setAsmtEncryptionKey(rs1.getString("asmtEncryptionKey"));
				manifest.setAsmtHash(rs1.getString("asmtHash"));
				manifest.setCompletionStatus(rs1.getString("completionStatus"));
				manifest.setId(rs1.getInt("id"));
				manifest.setItemEncryptionKey(rs1.getString("itemEncryptionKey"));
				manifest.setRandomDistractorStatus(rs1.getString("randomDistractorStatus"));
				manifest.setScoDurationMinutes(rs1.getInt("scoDurationMinutes"));
				manifest.setScoEntry(rs1.getString("scoEntry"));
				manifest.setScoOrder(rs1.getInt("scoOrder"));
				manifest.setScoParentId(rs1.getInt("scoParentId"));
				//manifest.setScoUnitQuestionOffset(rs1.getInt("scoUnitQuestionOffset"));
				manifest.setScoUnitType(rs1.getString("scoUnitType"));
				//manifest.setScratchpadContent(rs1.getClob("scratchpad_content"));
				//manifest.setScratchpadContentStr(rs1.getString("scratchpad_content"));
				manifest.setShowStudentFeedback(rs1.getString("showStudentFeedback"));
				manifest.setTestTitle(rs1.getString("testTitle"));
				manifest.setTitle(rs1.getString("title"));
				manifest.setTotalTime(rs1.getInt("totalTime"));
				dataList.add(manifest);
			}
			rs1.close();
			data = (ManifestData[]) dataList.toArray(new ManifestData[0]);
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
    
	private static ScratchpadData[] getScratchpadContent(Connection con, int testRosterId) {
    	ScratchpadData[] data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(SCRATCHPAD_CONTENT_SQL);
			stmt1.setInt(1, testRosterId);
			ResultSet rs1 = stmt1.executeQuery();
			ArrayList<ScratchpadData> list = new ArrayList<ScratchpadData>();
			while (rs1.next()) {
				ScratchpadData scratch = new ScratchpadData();
				scratch.setScratchpadData(rs1.getClob("scratchpad_content"));
				scratch.setItemSetId(rs1.getInt("item_set_id"));
				list.add(scratch);
			}
			rs1.close();
			data = list.toArray(new ScratchpadData[0]);
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
    
	private static AccommodationsData getAccommodations(Connection con, int testRosterId) {
    	AccommodationsData data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(ACCOMMODATIONS_SQL);
			stmt1.setInt(1, testRosterId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				data = new AccommodationsData();
				data.setAnswerBackgroundColor(rs1.getString("answerBackgroundColor"));
				data.setAnswerFontColor(rs1.getString("answerFontColor"));
				data.setAnswerFontSize(rs1.getFloat("answerFontSize"));
				data.setCalculator(rs1.getString("calculator"));
				data.setHighlighter(rs1.getString("highlighter"));
				data.setQuestionBackgroundColor(rs1.getString("questionBackgroundColor"));
				data.setQuestionFontColor(rs1.getString("questionFontColor"));
				data.setQuestionFontSize(rs1.getFloat("questionFontSize"));
				data.setScreenMagnifier(rs1.getString("screenMagnifier"));
				data.setScreenReader(rs1.getString("screenReader"));
				data.setStudentId(rs1.getInt("studentId"));
				data.setTestPause(rs1.getString("testPause"));
				data.setUntimedTest(rs1.getString("untimedTest"));
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
    
	private static int getTotalElapsedTimeForSubtest(Connection con, int testRosterId, int itemSetId) {
    	int result = 0;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(SUBTEST_ELAPSED_TIME_SQL);
			stmt1.setInt(1, testRosterId);
			stmt1.setInt(2, itemSetId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				result = rs1.getInt("totalTime");
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
		return result;
	}
    
	private static ItemResponseData [] getRestartItemResponses(Connection con, int testRosterId, int itemSetId) {
    	ItemResponseData[] data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(RESTART_RESPONSES_SQL);
			stmt1.setInt(1, testRosterId);
			stmt1.setInt(2, itemSetId);
			ResultSet rs1 = stmt1.executeQuery();
			ArrayList<ItemResponseData> dataList = new ArrayList<ItemResponseData>();
			while (rs1.next()) {
				ItemResponseData response = new ItemResponseData();
				response.setConstructedResponse(rs1.getClob("constructedResponse"));
				response.setEid(rs1.getInt("eid"));
				response.setItemId(rs1.getString("itemId"));
				response.setItemSortOrder(rs1.getInt("itemSortOrder"));
				response.setItemType(rs1.getString("itemType"));
				response.setResponse(rs1.getString("response"));
				response.setResponseElapsedTime(rs1.getInt("responseElapsedTime"));
				response.setResponseSeqNum(rs1.getInt("responseSeqNum"));
				response.setScore(rs1.getInt("score"));
				response.setStudentMarked(rs1.getString("studentMarked"));
				dataList.add(response);
			}
			rs1.close();
			data = (ItemResponseData[]) dataList.toArray(new ItemResponseData[0]);
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
    
	private static String getTutorialResource(Connection con, int testRosterId) {
    	String result = "";
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(TUTORIAL_RESOURCE_SQL);
			stmt1.setInt(1, testRosterId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				result = rs1.getString("resource_uri");
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
		return result;
	}
    
	private static boolean wasTutorialTaken(Connection con, int testRosterId) {
    	boolean result = false;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(TUTORIAL_TAKEN_SQL);
			stmt1.setInt(1, testRosterId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				result = rs1.getInt("counter") > 0;
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
		return result;
	}
    
	private static TestProduct getProductForTestAdmin(Connection con, int testAdminId) {
    	TestProduct data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(TEST_PRODUCT_FOR_ADMIN_SQL);
			stmt1.setInt(1, testAdminId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				data = new TestProduct();
				data.setProductId(rs1.getInt("productId"));
				data.setProductName(rs1.getString("productName"));
				data.setVersion(rs1.getString("version"));
				data.setProductDescription(rs1.getString("productDescription"));
				data.setCreatedBy(rs1.getInt("createdBy"));
				data.setCreatedDateTime(rs1.getDate("createdDateTime"));
				data.setUpdatedBy(rs1.getInt("updatedBy"));
				data.setUpdatedDateTime(rs1.getDate("updatedDateTime"));
				data.setActivationStatus(rs1.getString("activationStatus"));
				data.setProductType(rs1.getString("productType"));
				data.setScoringItemSetLevel(rs1.getInt("scoringItemSetLevel"));
				data.setPreviewItemSetLevel(rs1.getInt("previewItemSetLevel"));
				data.setParentProductId(rs1.getInt("parentProductId"));
				data.setExtProductId(rs1.getString("extProductId"));
				data.setContentAreaLevel(rs1.getInt("contentAreaLevel"));
				data.setInternalDisplayName(rs1.getString("internalDisplayName"));
				data.setSecScoringItemSetLevel(rs1.getInt("secScoringItemSetLevel"));
				data.setIbsShowCmsId(rs1.getString("ibsShowCmsId"));
				data.setPrintable(rs1.getString("printable"));
				data.setScannable(rs1.getString("scannable"));
				data.setKeyenterable(rs1.getString("keyenterable"));
				data.setBrandingTypeCode(rs1.getString("brandingTypeCode"));
				data.setAcknowledgmentsURL(rs1.getString("acknowledgmentsURL"));
				data.setShowStudentFeedback(rs1.getString("showStudentFeedback"));
				data.setStaticManifest(rs1.getString("staticManifest"));
				data.setSessionManifest(rs1.getString("sessionManifest"));
				data.setSubtestsSelectable(rs1.getString("subtestsSelectable"));
				data.setSubtestsOrderable(rs1.getString("subtestsOrderable"));
				data.setSubtestsLevelsVary(rs1.getString("subtestsLevelsVary"));
				data.setSupportPhoneNumber(rs1.getString("supportPhoneNumber"));
				data.setOffGradeTestingDisabled(rs1.getString("offGradeTestingDisabled"));
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
    
	private static String getProductLogo(Connection con, int productId) {
    	String result = "";
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(PRODUCT_LOGO_SQL);
			stmt1.setInt(1, productId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				result = rs1.getString("resource_uri");
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
		return result;
	}
    
	private static String isUltimateAccessCode(Connection con, int testAdminId, int testRosterId, String accessCode) {
    	String result = "";
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(IS_ULTIMATE_ACCESS_CODE_SQL);
			stmt1.setInt(1, testAdminId);
			stmt1.setInt(2, testRosterId);
			stmt1.setString(3, accessCode);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				result = rs1.getString(1);
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
		return result;
	}

	public void shutdown() {
		// do nothing
	}
	
} 
