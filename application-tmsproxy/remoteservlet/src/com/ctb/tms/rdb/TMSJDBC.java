package com.ctb.tms.rdb; 

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ctb.tms.bean.login.AccommodationsData;
import com.ctb.tms.bean.login.AuthenticationData;
import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.ManifestData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.bean.login.TestProduct;


public class TMSJDBC
{ 
    public static final String THROTTLE_MODE_SQL = "select throttle_mode as throttleMode, throttle_threshold as throttleThreshold, check_frequency as checkFrequency from system_throttle";

    public static final String IN_PROGRESS_COUNT_SQL = "select count(*) as counter from test_roster where test_completion_status = 'IP'";
   
    public static final String ROSTER_COMPLETION_STATUS_SQL = "update  test_roster set  test_completion_status = {status},  restart_number = {restartNumber},  start_date_time = nvl(start_date_time,{timestamp}),  last_login_date_time = {timestamp},  capture_method = {captureMethod},  updated_date_time = {timestamp},  last_mseq = {mseq},  correlation_id = null where  test_roster_id = {testRosterId}";
    
    public static final String AUTHENTICATE_STUDENT_SQL = "select  ros.test_roster_id as testRosterId,  stu.student_id as studentId,  stu.last_name as studentLastName,  stu.first_name as studentFirstName,  stu.middle_name as studentMiddleName,  ros.test_completion_status as rosterTestCompletionStatus,  adm.login_start_date as windowStartDate,  adm.login_end_date as windowEndDate,  adm.daily_login_start_time as dailyStartTime,  adm.daily_login_end_time as dailyEndTime,  adm.test_admin_status as testAdminStatus,  adm.time_zone AS timeZone,  ros.capture_method as captureMethod,  ros.restart_number as restartNumber,  ros.test_admin_id as testAdminId, \t  ros.random_distractor_seed as randomDistractorSeedNumber, \t  ros.tts_speed_status as ttsSpeedStatus from  student stu,  test_roster ros,  test_admin adm where  adm.test_admin_id = ros.test_admin_id  and ros.student_id = stu.student_id  and stu.activation_status = 'AC'  and ros.activation_status = 'AC'  and adm.activation_status = 'AC'  and upper(stu.user_name) = upper(?)  and upper(ros.password) = upper(?)";
    
    public static final String ACCOMMODATIONS_SQL = "select  accom.student_id as studentId,  accom.screen_magnifier as screenMagnifier,  accom.screen_reader as screenReader,  accom.calculator as calculator,  accom.test_pause as testPause,  accom.untimed_test as untimedTest,  accom.question_background_color as questionBackgroundColor,  accom.question_font_color as questionFontColor,  accom.question_font_size as questionFontSize,  accom.answer_background_color as answerBackgroundColor,  accom.answer_font_color as answerFontColor,  accom.answer_font_size as answerFontSize,  accom.highlighter as highlighter  from  test_roster ros,  student_accommodation accom  where  accom.student_id = ros.student_id  and ros.test_roster_id = {testRosterId}";
    
    public static final String MANIFEST_SQL = "select scoOrder,  scoParentId,  adminForceLogout,  showStudentFeedback,  id,  title,  testTitle,  scoDurationMinutes,  0 as totalTime,  scoUnitType,  scoEntry,  completionStatus,  asmtHash,  asmtEncryptionKey,  itemEncryptionKey,  adsid,  randomDistractorStatus  from (select siss.item_Set_order as scoOrder,  isp.parent_item_Set_id as scoParentId,  ta.force_logout as adminForceLogout,  ta.show_student_feedback as showStudentFeedback,  iset.item_set_id as id,  iset.item_set_name as title,  test.item_set_name as testTitle,  iset.time_limit / 60 as scoDurationMinutes,  'SUBTEST' as scoUnitType,  'ab-initio' as scoEntry,  siss.completion_status as completionStatus,  iset.asmt_hash as asmtHash,  iset.asmt_encryption_key as asmtEncryptionKey,  iset.item_encryption_key as itemEncryptionKey,  iset.ads_ob_asmt_id as adsid,  ta.test_admin_id testid,  ta.random_distractor_status as randomDistractorStatus  from item_set_item  isi,  item_Set  iset,  item_set  test,  student_item_set_status siss,  test_roster  tr,  test_admin  ta,  item_set_parent  isp,  test_admin_item_set  tais  where isi.item_set_id = iset.item_set_id  and iset.item_set_id = siss.item_set_id  and iset.item_set_type = 'TD'  and siss.test_roster_id = tr.test_roster_id  and ta.test_admin_id = tr.test_admin_id  and isp.item_Set_id = iset.item_set_id  and tr.test_roster_id = ?  and tais.item_set_id = isp.parent_item_set_id  and test.item_set_id = ta.item_set_id  and upper(tais.access_code) = upper(?)  and tais.test_admin_id = ta.test_admin_id  group by siss.item_Set_order,  isp.parent_item_set_id,  ta.force_logout,  ta.show_student_feedback,  iset.item_Set_id,  iset.item_set_name,  test.item_set_name,  iset.time_limit,  isi.item_sort_order,  siss.completion_status,  iset.asmt_hash,  iset.asmt_encryption_key,  iset.item_encryption_key,  iset.ads_ob_asmt_id,  iset.item_set_level,  ta.test_admin_id,  ta.random_distractor_status)  group by scoOrder,  scoParentId,  adminForceLogout,  showStudentFeedback,  id,  title,  testTitle,  scoDurationMinutes,  scoUnitType,  scoEntry,  completionStatus,  asmtHash,  asmtEncryptionKey,  itemEncryptionKey,  adsid,  randomDistractorStatus  order by scoOrder";
    
    public static final String SUBTEST_ELAPSED_TIME_SQL = "select  nvl(sum(max(resp.response_elapsed_time)), 0) as totalTime from  item_response resp where  resp.test_roster_id = ?  and resp.item_set_id = ? group by  resp.test_roster_id,  resp.item_set_id,  resp.item_id";
    
    public static final String IS_ULTIMATE_ACCESS_CODE_SQL = "select  decode(count(siss.item_set_id), 0, 'T', 'F') from \t  student_item_set_status siss,  test_Admin_item_set tais,  item_set_parent isp where  tais.test_Admin_id = ?  and siss.test_roster_id = ?  and upper(tais.access_code) != upper(?) \t  and isp.parent_item_set_id = tais.item_set_id \t  and siss.item_set_id = isp.item_set_id \t  and siss.completion_status != 'CO'";
    
    public static final String RESTART_RESPONSES_SQL = "select  ir.item_id as itemId,  ir.response_seq_num as responseSeqNum,  ir.student_marked as studentMarked,  i.item_type as itemType,  isi.item_sort_order as itemSortOrder,  ir.response as response,  icr.constructed_response as constructedResponse,  ir.response_elapsed_time as responseElapsedTime,  decode(ir.response, i.correct_answer, 1, 0) as score,  i.ads_item_id as eid from  item_response ir,  item i,  item_response_cr icr,  item_set_item isi where  ir.test_roster_id = ?  and ir.item_set_id = ?  and ir.item_id = i.item_id  and ir.item_set_id = isi.item_set_id  and ir.item_id = isi.item_id  and ir.response_seq_num =  (select  max(ir1.response_seq_num)  from  item_response ir1  where  ir1.item_set_id = ir.item_set_id  and ir1.item_id = ir.item_id  and ir1.test_roster_id = ir.test_roster_id) \t  AND ir.test_roster_id = icr.test_roster_id (+) \t  AND ir.item_set_id = icr.item_set_id (+) \t  AND ir.item_id = icr.item_id (+) order by  isi.item_sort_order";
    
    public static final String TABE_LOCATOR_MANIFEST_SQL = "select  siss.item_Set_order as scoOrder, \t  iset.item_set_id as id, \t  iset.item_set_name as title, \t  siss.completion_status as completionStatus, \t  iset.asmt_hash as asmtHash, \t  iset.asmt_encryption_key as asmtEncryptionKey, \t  iset.item_encryption_key as itemEncryptionKey, \t  iset.ads_ob_asmt_id as adsid  from student_item_set_status siss, item_set iset where siss.item_set_id = iset.item_set_id and siss.test_roster_id = {testRosterId} and iset.item_set_level = 'L'";

    public static final String MANIFEST_BY_ROSTER_SQL = "select \t  scoOrder, \t  scoParentId, \t  adminForceLogout, \t  showStudentFeedback, \t  id, \t  title, \t  testTitle, \t  scoDurationMinutes, \t  0 as totalTime, \t  scoUnitType, \t  scoEntry, \t  completionStatus, \t  asmtHash, \t  asmtEncryptionKey, \t  itemEncryptionKey, \t  adsid,  accessCode\t from (  select \t  siss.item_Set_order as scoOrder, \t  isp.parent_item_Set_id as scoParentId, \t  ta.force_logout as adminForceLogout, \t  ta.show_student_feedback as showStudentFeedback, \t  iset.item_set_id as id, \t  iset.item_set_name as title, \t  test.item_set_name as testTitle, \t  iset.time_limit / 60 as scoDurationMinutes, \t  'SUBTEST' as scoUnitType, \t  'ab-initio' as scoEntry, \t  siss.completion_status as completionStatus, \t  iset.asmt_hash as asmtHash, \t  iset.asmt_encryption_key as asmtEncryptionKey, \t  iset.item_encryption_key as itemEncryptionKey, \t  iset.ads_ob_asmt_id as adsid,  tais.access_code as accessCode  from  item_set_item isi,  item_Set iset,  item_set test,  student_item_set_status siss,  test_roster tr,  test_admin ta,  item_set_parent isp,  test_admin_item_set tais  where  isi.item_set_id = iset.item_set_id  and iset.item_set_id = siss.item_set_id  and iset.item_set_type = 'TD'  and siss.test_roster_id = tr.test_roster_id  and ta.test_admin_id = tr.test_admin_id  and isp.item_Set_id = iset.item_set_id  and tr.test_roster_id = {testRosterId}  and tais.item_set_id = isp.parent_item_set_id  and test.item_set_id = ta.item_set_id  and tais.test_admin_id = ta.test_admin_id  group by  siss.item_Set_order,  isp.parent_item_set_id,  ta.force_logout,  ta.show_student_feedback,  iset.item_Set_id,  iset.item_set_name,  test.item_set_name,  iset.time_limit,  isi.item_sort_order,  siss.completion_status,  iset.asmt_hash,  iset.asmt_encryption_key,  iset.item_encryption_key,  iset.ads_ob_asmt_id,  tais.access_code) group by scoOrder, \t  scoParentId, \t  adminForceLogout, \t  showStudentFeedback, \t  id, \t  title, \t  testTitle, \t  scoDurationMinutes, \t  scoUnitType, \t  scoEntry, \t  completionStatus, \t  asmtHash, \t  asmtEncryptionKey, \t  itemEncryptionKey, \t  adsid,  accessCode \torder by \t  scoOrder";
    
    public static final String PRODUCT_LOGO_SQL = "select resource_uri  from PRODUCT_RESOURCE  where resource_type_code = 'TDCLOGO'  and product_id = ?";
    
    public static final String TUTORIAL_RESOURCE_SQL = "select pr.resource_uri from product pp, product cp, product_resource pr where pp.product_id = cp.parent_product_id and pp.product_id = pr.product_id and pr.resource_type_code = 'TUTORIAL' and cp.product_id in ( select product_id from test_admin where test_admin_id in  (select test_admin_id from test_roster where test_roster_id = ?))";
    
    public static final String TUTORIAL_TAKEN_SQL = "select count(*) as counter from test_roster tr, test_admin ta, student_tutorial_status sts where tr.test_admin_id = ta.test_admin_id and ta.product_id = sts.product_id and tr.student_id = sts.student_id and sts.completion_status = 'CO' and tr.test_roster_id = ?";
    
    public static final String SCRATCHPAD_CONTENT_SQL = "select scratchpad_content from student_item_set_status where test_roster_id = ? and item_set_id = ?";
    
    public static final String UPDATE_TEST_ROSTER_WITH_RD_SEED_SQL = "update  test_roster set  random_distractor_seed = {rndNumber} where  test_roster_id = {testRosterId}";

    public static final String SPEECH_CONTROLLER_SQL = "select cconfig.default_value as speechControllerFlag  from test_roster  ros,  customer  cus,  customer_configuration cconfig,  student_accommodation  accom  where accom.screen_reader = 'T'  and accom.student_id = ros.student_id  and cconfig.customer_configuration_name = 'Allow_Speech_Controller'  and cconfig.customer_id = cus.customer_id  and cus.customer_id = ros.customer_id  and ros.test_roster_id = {testRosterId}";

    public static final String TEST_PRODUCT_FOR_ADMIN_SQL = "select distinct  prod.product_id as productId,  prod.product_name as productName,  prod.version as version,  prod.product_description as productDescription,  prod.created_by as createdBy,  prod.created_date_time as createdDateTime,  prod.updated_by as updatedBy,  prod.updated_date_time as updatedDateTime,  prod.activation_status as activationStatus,  prod.product_type as productType,  prod.scoring_item_set_level as scoringItemSetLevel,  prod.preview_item_set_level as previewItemSetLevel,  prod.parent_product_id as parentProductId,  prod.ext_product_id as extProductId,  prod.content_area_level as contentAreaLevel,  prod.internal_display_name as internalDisplayName,  prod.sec_scoring_item_set_level as secScoringItemSetLevel,  prod.ibs_show_cms_id as ibsShowCmsId,  prod.printable as printable,  prod.scannable as scannable,  prod.keyenterable as keyenterable,  prod.branding_type_code as brandingTypeCode,  prod.acknowledgments_url as acknowledgmentsURL,  prod.show_student_feedback as showStudentFeedback,  prod.static_manifest as staticManifest,  prod.session_manifest as sessionManifest,  prod.subtests_selectable as subtestsSelectable,  prod.subtests_orderable as subtestsOrderable,  prod.subtests_levels_vary as subtestsLevelsVary,  cec.support_phone_number as supportPhoneNumber,  prod.off_grade_testing_disabled as offGradeTestingDisabled from  product prod, test_admin adm, customer_email_config cec where  prod.product_id = adm.product_id  and cec.customer_id (+) = adm.customer_id  and adm.test_admin_id = ?";
    
    public static final String ACTIVE_ROSTERS_SQL = "select distinct stu.user_name as username, tr.password as password, tais.access_code from test_roster tr, test_admin ta, test_admin_item_set tais, student stu where tr.test_completion_status in ('SC', 'IN', 'IS', 'IP') and sysdate > (TA.LOGIN_START_DATE - 3) and sysdate < (TA.LOGIN_END_DATE + 3) and tr.updated_date_time > sysdate - 180 and ta.updated_date_time > sysdate - 180 and ta.test_admin_id = tr.test_admin_id and tais.test_admin_id = ta.test_admin_id and stu.student_id = tr.student_id";
    
    public static AuthenticationData [] authenticateStudent(Connection con, String username, String password) {
    	AuthenticationData[] data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(AUTHENTICATE_STUDENT_SQL);
			stmt1.setString(1, username);
			stmt1.setString(2, password);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				data = new AuthenticationData[1];
				AuthenticationData auth = new AuthenticationData();
				data[0] = auth;
				auth.setCaptureMethod(rs1.getString("capture_method"));
				auth.setDailyEndTime(rs1.getDate("daily_end_time"));
				auth.setDailyStartTime(rs1.getDate("daily_start_time"));
				auth.setRandomDistractorSeedNumber(rs1.getInt("random_distractor_seed_number"));
				auth.setRestartNumber(rs1.getInt("restart_number"));
				auth.setRosterTestCompletionStatus(rs1.getString("test_completion_status"));
				auth.setStudentFirstName(rs1.getString("student_first_name"));
				auth.setStudentId(rs1.getInt("student_id"));
				auth.setStudentLastName(rs1.getString("student_last_name"));
				auth.setStudentMiddleName(rs1.getString("student_middle_name"));
				auth.setTestAdminId(rs1.getInt("test_admin_id"));
				auth.setTestAdminStatus(rs1.getString("test_admin_status"));
				auth.setTestRosterId(rs1.getInt("test_roster_id"));
				auth.setTimeZone(rs1.getString("time_zone"));
				auth.setTtsSpeedStatus(rs1.getString("tts_speed_status"));
				auth.setWindowEndDate(rs1.getDate("window_end_date"));
				auth.setWindowStartDate(rs1.getDate("windows_start_date"));	
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
    
    public static ManifestData [] getManifest(Connection con, int testRosterId, String testAccessCode) {
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
				manifest.setAccessCode(rs1.getString("access_code"));
				manifest.setAdminForceLogout(rs1.getString("admin_force_logout"));
				manifest.setAdsid(rs1.getString("adsid"));
				manifest.setAsmtEncryptionKey(rs1.getString("asmt_encryption_key"));
				manifest.setAsmtHash(rs1.getString("asmt_hash"));
				manifest.setCompletionStatus(rs1.getString("completion_status"));
				manifest.setId(rs1.getInt("id"));
				manifest.setItemEncryptionKey(rs1.getString("item_encryption_key"));
				manifest.setRandomDistractorStatus(rs1.getString("random_distractor_status"));
				manifest.setScoDurationMinutes(rs1.getInt("sco_duration_minutes"));
				manifest.setScoEntry(rs1.getString("sco_entry"));
				manifest.setScoOrder(rs1.getInt("sco_order"));
				manifest.setScoParentId(rs1.getInt("sco_parent_id"));
				manifest.setScoUnitQuestionOffset(rs1.getInt("sco_unit_question_offset"));
				manifest.setScoUnitType(rs1.getString("sco_unit_type"));
				//manifest.setScratchpadContent(rs1.getClob("scratchpad_content"));
				//manifest.setScratchpadContentStr(rs1.getString("scratchpad_content"));
				manifest.setShowStudentFeedback(rs1.getString("show_student_feedback"));
				manifest.setTestTitle(rs1.getString("test_title"));
				manifest.setTitle(rs1.getString("title"));
				manifest.setTotalTime(rs1.getInt("total_time"));
				dataList.add(manifest);
			}
			rs1.close();
			data = (ManifestData[]) dataList.toArray();
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
    
    public static Clob getScratchpadContent(Connection con, int testRosterId, int itemSetId) {
    	Clob clob = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(SCRATCHPAD_CONTENT_SQL);
			stmt1.setInt(1, testRosterId);
			stmt1.setInt(2, itemSetId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				clob = rs1.getClob("scratchpad_content");
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
		return clob;
	}
    
    public static AccommodationsData getAccommodations(Connection con, int testRosterId) {
    	AccommodationsData data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(ACCOMMODATIONS_SQL);
			stmt1.setInt(1, testRosterId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				data = new AccommodationsData();
				data.setAnswerBackgroundColor(rs1.getString("answer_background_color"));
				data.setAnswerFontColor(rs1.getString("answer_font_color"));
				data.setAnswerFontSize(rs1.getInt("answer_font_size"));
				data.setCalculator(rs1.getString("calculator"));
				data.setHighlighter(rs1.getString("highlighter"));
				data.setQuestionBackgroundColor(rs1.getString("question_background_color"));
				data.setQuestionFontColor(rs1.getString("question_font_color"));
				data.setQuestionFontSize(rs1.getInt("question_font_size"));
				data.setScreenMagnifier(rs1.getString("screen_magnifier"));
				data.setScreenReader(rs1.getString("screen_reader"));
				data.setStudentId(rs1.getInt("student_id"));
				data.setTestPause(rs1.getString("test_pause"));
				data.setUntimedTest(rs1.getString("untimed_test"));
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
    
    public static int getTotalElapsedTimeForSubtest(Connection con, int testRosterId, int itemSetId) {
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
    
    public static ItemResponseData [] getRestartItemResponses(Connection con, int testRosterId, int itemSetId) {
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
				response.setConstructedResponse(rs1.getClob("constructed_response"));
				response.setEid(rs1.getInt("eid"));
				response.setItemId(rs1.getString("item_id"));
				response.setItemSortOrder(rs1.getInt("item_sort_order"));
				response.setItemType(rs1.getString("item_type"));
				response.setResponse(rs1.getString("response"));
				response.setResponseElapsedTime(rs1.getInt("response_elapsed_time"));
				response.setResponseSeqNum(rs1.getInt("response_seq_num"));
				response.setScore(rs1.getInt("score"));
				response.setStudentMarked(rs1.getString("student_marked"));
				dataList.add(response);
			}
			rs1.close();
			data = (ItemResponseData[]) dataList.toArray();
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
    
    public static String getTutorialResource(Connection con, int testRosterId) {
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
    
    public static boolean wasTutorialTaken(Connection con, int testRosterId) {
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
    
    public static TestProduct getProductForTestAdmin(Connection con, int testAdminId) {
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
    
    public static String getProductLogo(Connection con, int productId) {
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
    
    public static String isUltimateAccessCode(Connection con, int testAdminId, int testRosterId, String accessCode) {
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
    
    public static StudentCredentials [] getActiveRosters(Connection con) {
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
			}
			rs1.close();
			data = (StudentCredentials[]) dataList.toArray();
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
}