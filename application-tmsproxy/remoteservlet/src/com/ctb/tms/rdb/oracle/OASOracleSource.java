package com.ctb.tms.rdb.oracle; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import noNamespace.EntryType;
import noNamespace.StereotypeType;
import noNamespace.TmssvcResponseDocument;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco.ScoUnitType;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.TestingSessionData.LmsStudentAccommodations;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.TestingSessionData.LmsStudentAccommodations.StereotypeStyle;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Tutorial;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

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
import com.ctb.tms.rdb.OASRDBSource;
import com.ctb.tms.util.Constants;
import com.ctb.tms.web.listener.TestDeliveryContextListener;

public class OASOracleSource implements OASRDBSource
{ 
	private static volatile boolean haveDataSource = true;
	/* private static String OASDatabaseURL = "jdbc:oracle:thin:@nj09mhe0393-vip.mhe.mhc:1521:oasr5t1";
	private static String OASDatabaseUser = "oas";
	private static String OASDatabaseUserPassword = "oaspr5r";
	private static String OASDatabaseJDBCDriver = "oracle.jdbc.driver.OracleDriver"; */
	
	static Logger logger = Logger.getLogger(OASOracleSource.class);
	
	private static final String AUTHENTICATE_STUDENT_SQL = "select  ros.test_roster_id as testRosterId,  stu.student_id as studentId,  stu.last_name as studentLastName,  stu.first_name as studentFirstName,  stu.middle_name as studentMiddleName,  ros.test_completion_status as rosterTestCompletionStatus,  adm.login_start_date as windowStartDate,  adm.login_end_date as windowEndDate,  adm.daily_login_start_time as dailyStartTime,  adm.daily_login_end_time as dailyEndTime,  adm.test_admin_status as testAdminStatus,  adm.time_zone AS timeZone,  ros.capture_method as captureMethod,  ros.restart_number as restartNumber,  ros.test_admin_id as testAdminId, \t  ros.random_distractor_seed as randomDistractorSeedNumber, \t  ros.tts_speed_status as ttsSpeedStatus from  student stu,  test_roster ros,  test_admin adm where  adm.test_admin_id = ros.test_admin_id  and ros.student_id = stu.student_id  and stu.activation_status = 'AC'  and ros.activation_status = 'AC'  and adm.activation_status = 'AC'  and upper(stu.user_name) = upper(?)  and upper(ros.password) = upper(?)";    
	private static final String REAUTHENTICATE_STUDENT_SQL = "select distinct upper(tais.access_code) as accessCode, ros.test_roster_id as testRosterId,  stu.student_id as studentId,  stu.last_name as studentLastName,  stu.first_name as studentFirstName,  stu.middle_name as studentMiddleName,  ros.test_completion_status as rosterTestCompletionStatus,  adm.login_start_date as windowStartDate,  adm.login_end_date as windowEndDate,  adm.daily_login_start_time as dailyStartTime,  adm.daily_login_end_time as dailyEndTime,  adm.test_admin_status as testAdminStatus,  adm.time_zone AS timeZone,  ros.capture_method as captureMethod,  ros.restart_number as restartNumber,  ros.test_admin_id as testAdminId, ros.random_distractor_seed as randomDistractorSeedNumber,  ros.tts_speed_status as ttsSpeedStatus, ros.start_date_time as startTime, ros.completion_date_time as endTime, ros.correlation_id as correlationId, ros.last_mseq as lastMseq from  student stu,  test_roster ros,  test_admin adm, test_admin_item_set tais, student_item_set_status siss, item_set_parent isa where  adm.test_admin_id = ros.test_admin_id  and ros.student_id = stu.student_id  and stu.activation_status = 'AC'  and ros.activation_status = 'AC'  and adm.activation_status = 'AC'  and tais.test_admin_id = adm.test_admin_id and tais.item_set_id = isa.parent_item_set_id and isa.item_set_id = siss.item_set_id and siss.test_roster_id = ros.test_roster_id and ros.test_roster_id = ? and upper(tais.access_code) = upper(?)";    
	private static final String ACCOMMODATIONS_SQL = "select  accom.student_id as studentId,  accom.screen_magnifier as screenMagnifier,  accom.screen_reader as screenReader,  accom.calculator as calculator,  accom.test_pause as testPause,  accom.untimed_test as untimedTest,  accom.question_background_color as questionBackgroundColor,  accom.question_font_color as questionFontColor,  accom.question_font_size as questionFontSize,  accom.answer_background_color as answerBackgroundColor,  accom.answer_font_color as answerFontColor,  accom.answer_font_size as answerFontSize,  accom.highlighter as highlighter, accom.masking_ruler as maskingRuler,accom.magnifying_glass as magnifyingGlass, decode(accom.music_file_id, NULL, 'F', 'T')as auditoryCalming, accom.music_file_id as musicFileId, accom.extended_time as extendedTime, accom.masking_tool as maskingTool from  test_roster ros,  student_accommodation accom  where  accom.student_id = ros.student_id  and ros.test_roster_id = ?";    
	private static final String MANIFEST_SQL = "select scoOrder,  scoParentId,  adminForceLogout,  showStudentFeedback,  id,  title,  testTitle,  scoDurationMinutes,  0 as totalTime,  scoUnitType,  scoEntry,  completionStatus,  asmtHash,  asmtEncryptionKey,  itemEncryptionKey,  adsid,  randomDistractorStatus, forwardOnly, scorable,product,subtestForm, subtestLevel, adaptive, accessCode, raw_score, max_score, unscored, ability_score, sem_score, objective_score, start_date_time, completion_date_time, contentURI, recommended_level, blockDownload from (select siss.item_Set_order as scoOrder,  isp.parent_item_Set_id as scoParentId,  ta.force_logout as adminForceLogout,  ta.show_student_feedback as showStudentFeedback,  iset.item_set_id as id,  iset.item_set_name as title,  test.item_set_name as testTitle,  decode(nvl(tr.extended_time,0),0,iset.time_limit / 60,(iset.time_limit * tr.extended_time) / 60) as scoDurationMinutes,  'SUBTEST' as scoUnitType,  'ab-initio' as scoEntry,  siss.completion_status as completionStatus,  iset.asmt_hash as asmtHash,  iset.asmt_encryption_key as asmtEncryptionKey,  iset.item_encryption_key as itemEncryptionKey,  iset.ads_ob_asmt_id as adsid,  ta.test_admin_id testid,  ta.random_distractor_status as randomDistractorStatus, iset.forward_only as forwardOnly, pr.scorable as scorable, pr.product_type as product, iset.item_set_form as subtestForm, iset.item_set_level as subtestLevel, iset.adaptive as adaptive, upper(tais.access_code) as accessCode, siss.raw_score, siss.max_score, siss.unscored, siss.ability_score, siss.sem_score, siss.objective_score, siss.start_date_time, siss.completion_date_time, iset.content_repository_uri as contentURI, siss.recommended_level, tc.block_download_flag as blockDownload from item_set_item  isi,  item_Set  iset,  item_set  test,  student_item_set_status siss,  test_roster  tr,  test_admin  ta,  test_catalog tc, item_set_parent  isp,  test_admin_item_set  tais, product pr where tc.test_catalog_id = ta.test_catalog_id and isi.item_set_id = iset.item_set_id  and iset.item_set_id = siss.item_set_id  and iset.item_set_type = 'TD'  and siss.test_roster_id = tr.test_roster_id  and ta.test_admin_id = tr.test_admin_id  and isp.item_Set_id = iset.item_set_id  and tr.test_roster_id = ?  and tais.item_set_id = isp.parent_item_set_id  and test.item_set_id = ta.item_set_id and tais.test_admin_id = ta.test_admin_id and pr.product_id = ta.product_id group by siss.item_Set_order,  isp.parent_item_set_id,  ta.force_logout,  ta.show_student_feedback,  iset.item_Set_id,  iset.item_set_name,  test.item_set_name,  iset.time_limit,  isi.item_sort_order,  siss.completion_status,  iset.asmt_hash,  iset.asmt_encryption_key,  iset.item_encryption_key,  iset.ads_ob_asmt_id,  iset.item_set_level,  ta.test_admin_id,  ta.random_distractor_status, tr.extended_time, iset.forward_only, scorable,pr.product_type, iset.item_set_form, iset.item_set_level, iset.adaptive, tais.access_code, siss.raw_score, siss.max_score, siss.unscored, siss.ability_score, siss.sem_score, siss.objective_score, siss.start_date_time, siss.completion_date_time, iset.content_repository_uri, siss.recommended_level, tc.block_download_flag)  group by scoOrder,  scoParentId,  adminForceLogout,  showStudentFeedback,  id,  title,  testTitle,  scoDurationMinutes,  scoUnitType,  scoEntry,  completionStatus,  asmtHash,  asmtEncryptionKey,  itemEncryptionKey,  adsid,  randomDistractorStatus, forwardOnly, scorable,product, subtestForm, subtestLevel, adaptive, accessCode, raw_score, max_score, unscored, ability_score, sem_score, objective_score, start_date_time, completion_date_time, contentURI, recommended_level, blockDownload order by scoOrder";
	private static final String SUBTEST_ELAPSED_TIME_SQL = "select  nvl(sum(max(resp.response_elapsed_time)), 0) as totalTime from  item_response resp where  resp.test_roster_id = ?  and resp.item_set_id = ? group by  resp.test_roster_id,  resp.item_set_id,  resp.item_id";
	private static final String IS_ULTIMATE_ACCESS_CODE_SQL = "select  decode(count(siss.item_set_id), 0, 'T', 'F') from \t  student_item_set_status siss,  test_Admin_item_set tais,  item_set_parent isp where  tais.test_Admin_id = ?  and siss.test_roster_id = ?  and upper(tais.access_code) != upper(?) \t  and isp.parent_item_set_id = tais.item_set_id \t  and siss.item_set_id = isp.item_set_id \t  and siss.completion_status != 'CO'";
	private static final String RESTART_RESPONSES_SQL = "select  ir.item_id as itemId,  ir.response_seq_num as responseSeqNum,  ir.student_marked as studentMarked,  i.item_type as itemType,  isi.item_sort_order as itemSortOrder,  ir.response as response, TO_CLOB(decode (i.answer_area,'AudioItem', decode(length(icr.constructed_response),'','',ir.test_roster_id || '_' || ir.item_id), DBMS_LOB.SUBSTR(icr.constructed_response, 0, 3999))) as constructedResponse, ir.response_elapsed_time as responseElapsedTime,  decode(ir.response, i.correct_answer, 1, 0) as score,  i.ads_item_id as eid from  item_response ir,  item i,  item_response_cr icr,  item_set_item isi where  ir.test_roster_id = ?  and ir.item_set_id = ?  and ir.item_id = i.item_id  and ir.item_set_id = isi.item_set_id  and ir.item_id = isi.item_id  and ir.response_seq_num =  (select  max(ir1.response_seq_num)  from  item_response ir1  where  ir1.item_set_id = ir.item_set_id  and ir1.item_id = ir.item_id  and ir1.test_roster_id = ir.test_roster_id) \t  AND ir.test_roster_id = icr.test_roster_id (+) \t  AND ir.item_set_id = icr.item_set_id (+) \t  AND ir.item_id = icr.item_id (+) order by  ir.response_seq_num asc"; //isi.item_sort_order";
	//private static final String TABE_LOCATOR_MANIFEST_SQL = "select  siss.item_Set_order as scoOrder, \t  iset.item_set_id as id, \t  iset.item_set_name as title, \t  siss.completion_status as completionStatus, \t  iset.asmt_hash as asmtHash, \t  iset.asmt_encryption_key as asmtEncryptionKey, \t  iset.item_encryption_key as itemEncryptionKey, \t  iset.ads_ob_asmt_id as adsid  from student_item_set_status siss, item_set iset where siss.item_set_id = iset.item_set_id and siss.test_roster_id = {testRosterId} and iset.item_set_level = 'L'";
	//private static final String MANIFEST_BY_ROSTER_SQL = "select \t  scoOrder, \t  scoParentId, \t  adminForceLogout, \t  showStudentFeedback, \t  id, \t  title, \t  testTitle, \t  scoDurationMinutes, \t  0 as totalTime, \t  scoUnitType, \t  scoEntry, \t  completionStatus, \t  asmtHash, \t  asmtEncryptionKey, \t  itemEncryptionKey, \t  adsid,  accessCode\t from (  select \t  siss.item_Set_order as scoOrder, \t  isp.parent_item_Set_id as scoParentId, \t  ta.force_logout as adminForceLogout, \t  ta.show_student_feedback as showStudentFeedback, \t  iset.item_set_id as id, \t  iset.item_set_name as title, \t  test.item_set_name as testTitle, \t  iset.time_limit / 60 as scoDurationMinutes, \t  'SUBTEST' as scoUnitType, \t  'ab-initio' as scoEntry, \t  siss.completion_status as completionStatus, \t  iset.asmt_hash as asmtHash, \t  iset.asmt_encryption_key as asmtEncryptionKey, \t  iset.item_encryption_key as itemEncryptionKey, \t  iset.ads_ob_asmt_id as adsid,  tais.access_code as accessCode  from  item_set_item isi,  item_Set iset,  item_set test,  student_item_set_status siss,  test_roster tr,  test_admin ta,  item_set_parent isp,  test_admin_item_set tais  where  isi.item_set_id = iset.item_set_id  and iset.item_set_id = siss.item_set_id  and iset.item_set_type = 'TD'  and siss.test_roster_id = tr.test_roster_id  and ta.test_admin_id = tr.test_admin_id  and isp.item_Set_id = iset.item_set_id  and tr.test_roster_id = {testRosterId}  and tais.item_set_id = isp.parent_item_set_id  and test.item_set_id = ta.item_set_id  and tais.test_admin_id = ta.test_admin_id  group by  siss.item_Set_order,  isp.parent_item_set_id,  ta.force_logout,  ta.show_student_feedback,  iset.item_Set_id,  iset.item_set_name,  test.item_set_name,  iset.time_limit,  isi.item_sort_order,  siss.completion_status,  iset.asmt_hash,  iset.asmt_encryption_key,  iset.item_encryption_key,  iset.ads_ob_asmt_id,  tais.access_code) group by scoOrder, \t  scoParentId, \t  adminForceLogout, \t  showStudentFeedback, \t  id, \t  title, \t  testTitle, \t  scoDurationMinutes, \t  scoUnitType, \t  scoEntry, \t  completionStatus, \t  asmtHash, \t  asmtEncryptionKey, \t  itemEncryptionKey, \t  adsid,  accessCode \torder by \t  scoOrder";   
	//private static final String PRODUCT_LOGO_SQL = "select resource_uri  from PRODUCT_RESOURCE  where resource_type_code = 'TDCLOGO'  and product_id = ?";    
	private static final String TUTORIAL_RESOURCE_SQL = "select pr.resource_uri from product pp, product cp, product_resource pr where pp.product_id = cp.parent_product_id and pp.product_id = pr.product_id and pr.resource_type_code = 'TUTORIAL' and cp.product_id in ( select product_id from test_admin where test_admin_id in  (select test_admin_id from test_roster where test_roster_id = ?))";    
	private static final String TUTORIAL_TAKEN_SQL = "select count(*) as counter from test_roster tr, test_admin ta, student_tutorial_status sts where tr.test_admin_id = ta.test_admin_id and tr.student_id = sts.student_id and sts.completion_status = 'CO' and tr.test_roster_id = ?";    
	private static final String SCRATCHPAD_CONTENT_SQL = "select scratchpad_content, item_set_id from student_item_set_status where test_roster_id = ?";
	//private static final String UPDATE_TEST_ROSTER_WITH_RD_SEED_SQL = "update  test_roster set  random_distractor_seed = {rndNumber} where  test_roster_id = {testRosterId}";
	//private static final String SPEECH_CONTROLLER_SQL = "select cconfig.default_value as speechControllerFlag  from test_roster  ros,  customer  cus,  customer_configuration cconfig,  student_accommodation  accom  where accom.screen_reader = 'T'  and accom.student_id = ros.student_id  and cconfig.customer_configuration_name = 'Allow_Speech_Controller'  and cconfig.customer_id = cus.customer_id  and cus.customer_id = ros.customer_id  and ros.test_roster_id = {testRosterId}";
	private static final String TEST_PRODUCT_FOR_ADMIN_SQL = "select distinct  productId, productName,  version,  productDescription,  createdBy, createdDateTime,   updatedBy,  updatedDateTime,  activationStatus,  productType,  scoringItemSetLevel,  previewItemSetLevel,  parentProductId,  extProductId,  contentAreaLevel,  internalDisplayName,  secScoringItemSetLevel,  ibsShowCmsId,  printable,  scannable,  keyenterable,  brandingTypeCode,   acknowledgmentsURL,  showStudentFeedback,  staticManifest,  sessionManifest,  subtestsSelectable,  subtestsOrderable,  subtestsLevelsVary,  supportPhoneNumber,  offGradeTestingDisabled, max(resource_uri) as resource_uri from (select distinct  prod.product_id as productId,  prod.product_name as productName,  prod.version as version,  prod.product_description as productDescription,  prod.created_by as createdBy,  prod.created_date_time as createdDateTime,  prod.updated_by as updatedBy,  prod.updated_date_time as updatedDateTime,  prod.activation_status as activationStatus,  prod.product_type as productType,  prod.scoring_item_set_level as scoringItemSetLevel,  prod.preview_item_set_level as previewItemSetLevel,  prod.parent_product_id as parentProductId,  prod.ext_product_id as extProductId,  prod.content_area_level as contentAreaLevel,  prod.internal_display_name as internalDisplayName,  prod.sec_scoring_item_set_level as secScoringItemSetLevel,  prod.ibs_show_cms_id as ibsShowCmsId,  prod.printable as printable,  prod.scannable as scannable,  prod.keyenterable as keyenterable,  prod.branding_type_code as brandingTypeCode,  prod.acknowledgments_url as acknowledgmentsURL,  prod.show_student_feedback as showStudentFeedback,  prod.static_manifest as staticManifest,  prod.session_manifest as sessionManifest,  prod.subtests_selectable as subtestsSelectable,  prod.subtests_orderable as subtestsOrderable,  prod.subtests_levels_vary as subtestsLevelsVary,  cec.support_phone_number as supportPhoneNumber,  prod.off_grade_testing_disabled as offGradeTestingDisabled, null as resource_uri from  test_admin adm, customer_email_config cec, product prod where  prod.product_id = adm.product_id  and cec.customer_id (+) = adm.customer_id  and adm.test_admin_id = ? union select distinct  prod.product_id as productId,  prod.product_name as productName,  prod.version as version,  prod.product_description as productDescription,  prod.created_by as createdBy,  prod.created_date_time as createdDateTime,  prod.updated_by as updatedBy,  prod.updated_date_time as updatedDateTime,  prod.activation_status as activationStatus,  prod.product_type as productType,  prod.scoring_item_set_level as scoringItemSetLevel,  prod.preview_item_set_level as previewItemSetLevel,  prod.parent_product_id as parentProductId,  prod.ext_product_id as extProductId,  prod.content_area_level as contentAreaLevel,  prod.internal_display_name as internalDisplayName,  prod.sec_scoring_item_set_level as secScoringItemSetLevel,  prod.ibs_show_cms_id as ibsShowCmsId,  prod.printable as printable,  prod.scannable as scannable,  prod.keyenterable as keyenterable,  prod.branding_type_code as brandingTypeCode,  prod.acknowledgments_url as acknowledgmentsURL,  prod.show_student_feedback as showStudentFeedback,  prod.static_manifest as staticManifest,  prod.session_manifest as sessionManifest,  prod.subtests_selectable as subtestsSelectable,  prod.subtests_orderable as subtestsOrderable,  prod.subtests_levels_vary as subtestsLevelsVary,  cec.support_phone_number as supportPhoneNumber,  prod.off_grade_testing_disabled as offGradeTestingDisabled, pr.resource_uri as resource_uri from  test_admin adm, customer_email_config cec, product prod, product_resource pr where  prod.product_id = adm.product_id  and cec.customer_id (+) = adm.customer_id  and pr.product_id = prod.product_id and pr.resource_type_code = 'TDCLOGO' and adm.test_admin_id = ?) group by productId, productName,  version,  productDescription,  createdBy, createdDateTime,   updatedBy,  updatedDateTime,  activationStatus,  productType,  scoringItemSetLevel,  previewItemSetLevel,  parentProductId,  extProductId,  contentAreaLevel,  internalDisplayName,  secScoringItemSetLevel,  ibsShowCmsId,  printable,  scannable,  keyenterable,  brandingTypeCode,   acknowledgmentsURL,  showStudentFeedback,  staticManifest,  sessionManifest,  subtestsSelectable,  subtestsOrderable,  subtestsLevelsVary,  supportPhoneNumber,  offGradeTestingDisabled";
	private static final String ACTIVE_ROSTERS_SQL = "" +
	"                select distinct * from ( " +
	"                select " + 
	"                    stu.user_name as username, " +
	"                    tr.password as password, " +
	"                    tais.access_code as accessCode, " +
	"                    'F' as tmsUpdate, " +
	"                   tr.test_roster_id as testRosterId " + 
	"                from " +
	"                    test_roster tr, " +
	"                    student stu, " +
	"                    test_admin_item_set tais, " +
	"                    item_set_parent isp, " +
	"                    test_admin ta, " +
	"                    (select test_roster_id, item_set_id from student_item_set_status where (NVL(tms_update, 'F') = 'F')) tsu " +
	"                where " +
	"                    tr.test_roster_id = tsu.test_roster_id " +
	"                    and ta.test_Admin_id = tr.test_Admin_id " + 
	"                    and sysdate > (TA.LOGIN_START_DATE - 1) " +  
	"                    and sysdate < (TA.LOGIN_END_DATE + 1) " + 
	"                    and tr.activation_status = 'AC' " +  
	"                    and ta.activation_status = 'AC' " + 
	"                    and tr.test_completion_status in ('SC', 'IN', 'IS', 'IP', 'IC', 'NT') " +
	"                    and stu.student_id = tr.student_id " +
	"                    and tais.test_Admin_id = ta.test_admin_id " +
	"                    and tsu.item_set_id = isp.item_set_id " +
	"                    and tais.item_set_id = isp.parent_item_set_id " +
	"                union " +  
	"                select " + 
	"                    stu.user_name as username, " +
	"                    tr.password as password, " +
	"                    tais.access_code as accessCode, " +
	"                    'F' as tmsUpdate, " +
	"                    tr.test_roster_id as testRosterId " + 
	"                from " +
	"                    test_roster tr, " +
	"                    student stu, " +
	"                    test_admin_item_set tais, " +
	"                    item_set_parent isp, " +
	"                    test_admin ta, " +
	"                    student_item_Set_status siss, " +
	"                    (select test_roster_id from test_roster where (NVL(tms_update, 'F') = 'F')) tsu " +
	"                where " +
	"                    tr.test_roster_id = tsu.test_roster_id " +
	"                    and ta.test_Admin_id = tr.test_Admin_id " + 
	"                    and sysdate > (TA.LOGIN_START_DATE - 1) " +  
	"                    and sysdate < (TA.LOGIN_END_DATE + 1) " + 
	"                    and tr.activation_status = 'AC' " +  
	"                    and ta.activation_status = 'AC' " + 
	"                    and tr.test_completion_status in ('SC', 'IN', 'IS', 'IP', 'IC', 'NT') " +
	"                    and stu.student_id = tr.student_id " +
	"                    and tais.test_Admin_id = ta.test_admin_id " +
	"                    and siss.item_set_id = isp.item_set_id " +
	"                    and tais.item_set_id = isp.parent_item_set_id " +
	"                    and siss.test_roster_id = tr.test_roster_id) where rownum <= " + TestDeliveryContextListener.batchSize;
	
	public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return OracleSetup.getOASConnection();
	}
	
	public StudentCredentials[] getActiveRosters(Connection con) {
    	PreparedStatement stmt1 = null;
    	int recordCount = 0;
    	try {
    		stmt1 = con.prepareStatement(ACTIVE_ROSTERS_SQL);
			ResultSet rs1 = stmt1.executeQuery();
			ArrayList<StudentCredentials> dataList = new ArrayList<StudentCredentials>();
			while (rs1.next()) {
				recordCount++;
				StudentCredentials creds = new StudentCredentials();
				creds.setUsername(rs1.getString("username"));
				creds.setPassword(rs1.getString("password"));
				creds.setAccesscode(rs1.getString("accesscode"));
				creds.setTmsUpdate(rs1.getString("tmsUpdate"));
				creds.setTestRosterId(rs1.getString("testRosterId"));
				dataList.add(creds);
			}
			rs1.close();
			return (StudentCredentials[]) dataList.toArray(new StudentCredentials[0]);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}
	
	public RosterData getRosterData(Connection conn, String key)  throws Exception {
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
	
    private RosterData getRosterData(Connection conn, StudentCredentials creds)  throws Exception {
    	String username = creds.getUsername();
    	String password = creds.getPassword();
    	String accessCode = creds.getAccesscode();

    	// might be more than one roster for these creds, due to random passwords
    	AuthenticationData [] authDataArray = authenticateStudentByCreds(conn, username, password);
    	return generateRosterData(conn, authDataArray, accessCode);
    }
    
    private RosterData generateRosterData (Connection conn, AuthenticationData [] authDataArray, String accessCode) throws SQLException, AuthenticationFailureException, KeyEnteredResponsesException, OutsideTestWindowException, TestSessionCompletedException, TestSessionInProgressException, TestSessionNotScheduledException, IOException, ClassNotFoundException {
    	XmlOptions xmlOptions = new XmlOptions(); 
    	xmlOptions.setCharacterEncoding("UTF-8");
        xmlOptions.setUnsynchronized();
    	TmssvcResponseDocument response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
        LoginResponse loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
        loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.OK_STATUS);
        response.getTmssvcResponse().setMethod("login_response");
        
    	AuthenticationData authData = null;
        boolean authenticated = false;
        int testRosterId = -1;
        String lsid = null;
        ManifestData [] manifestData = null;
        for(int a=0;authDataArray != null && a<authDataArray.length && !authenticated;a++) {
            authData = authDataArray[a];
            testRosterId = authData.getTestRosterId();
            lsid = String.valueOf(testRosterId) + ":" + accessCode;
            loginResponse.setLsid(lsid);
            manifestData = getManifest(conn, String.valueOf(testRosterId), accessCode);
            if(manifestData != null && manifestData.length > 0) {
                authenticated = true;
                ScratchpadData [] scratchData = getScratchpadContent(conn, testRosterId);
                HashMap<Integer, Clob> scratchMap = new HashMap<Integer, Clob>(scratchData.length);
                for (int i = 0; i < scratchData.length; i++) {
                	scratchMap.put(new Integer(scratchData[i].getItemSetId()), scratchData[i].getScratchpadData());
                }
                for (int i = 0; i < manifestData.length; i++) {
                	Clob clob = scratchMap.get(new Integer(manifestData[i].getId()));
                	if(clob != null && clob.length() > 0) {
	                	final char[] buffer = new char[0x1000];
	                	Reader in = clob.getCharacterStream();
	                	StringBuilder out = new StringBuilder(); 
	                	int read; 
	                	do {   
	                		read = in.read(buffer, 0, buffer.length);   
	                		if (read>0) {     
	                			out.append(buffer, 0, read);   
	                		} 
	                	} while (read>=0); 
	                	manifestData[i].setScratchpadContent(out.toString());
                	}
                }
            } else {
            	return null;
            }
        }
        if(authData != null) {    
	        if(authData.getRosterTestCompletionStatus().equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS) ||
	           authData.getRosterTestCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS) ||
	           authData.getRosterTestCompletionStatus().equals(Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS)) {
	                loginResponse.setRestartFlag(true);
	            } else {
	            	loginResponse.setRestartFlag(false);
	            }
	        	logger.debug("Restart flag from DB " + loginResponse.getRestartFlag());
	        	loginResponse.setRestartNumber(new BigInteger(String.valueOf(authData.getRestartNumber())));
	        	logger.debug("Restart number from DB " + loginResponse.getRestartNumber());
	        	
	            TestProduct testProduct = getProductForTestAdmin(conn, authData.getTestAdminId());
	            //AuthenticateStudent authenticator = authenticatorFactory.create();
	
	            if ("TB".equals(testProduct.getProductType())) {
	                for(int i = 0;i<manifestData.length;i++) {
	                    	if(!accessCode.equalsIgnoreCase(manifestData[i].getAccessCode()) &&
	                    			manifestData[i].getTitle().indexOf("locator") >= 0 &&
	                    			!manifestData[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.COMPLETED_STATUS)) {
	                    		response = TmssvcResponseDocument.Factory.newInstance(xmlOptions);
	            	            loginResponse = response.addNewTmssvcResponse().addNewLoginResponse();
	            	            loginResponse.addNewStatus().setStatusCode(Constants.StudentLoginResponseStatus.LOCATOR_SUBTEST_NOT_COMPLETED_STATUS);
	                    	}
	                }
	            }
	            // TODO: get product logo URI as part of product data
	            String logoURI = testProduct.getLogoURI();
	            if (logoURI == null || "".equals(logoURI))
	                logoURI = "/resources/logo.swf";
	            loginResponse.addNewBranding().setTdclogo(logoURI);
	
	        /* if (authData.getRandomDistractorSeedNumber() != null) {
	
	
				 loginResponse.setRandomDistractorSeedNumber(
						 new BigInteger(String.valueOf( authData.
								 getRandomDistractorSeedNumber())));
	
	
			 }  else {
	
				 if ("Y".equals(manifestData[0].getRandomDistractorStatus())) {
	
					 Integer ranodmSeedNumber = generateRandomNumber();
	
					 loginResponse.setRandomDistractorSeedNumber(new BigInteger( String.valueOf(ranodmSeedNumber.intValue())));
				 }
	
			 } */
	        copyAuthenticationDataToResponse(loginResponse, authData);
	        AccommodationsData accomData = getAccommodations(conn, testRosterId);
	        
	        if(accomData != null) {
	            copyAccomodationsDataToResponse(loginResponse, accomData);
	        }
	
	        boolean gotRestart = false;
	        for(int i=0; i<manifestData.length ;i++) {
	        	logger.debug("Checking for restart data for subtest: " + manifestData[i].getId());
	            if(Constants.StudentTestCompletionStatus.SCHEDULED_STATUS.equals(manifestData[i].getCompletionStatus()) ||
	               Constants.StudentTestCompletionStatus.STUDENT_PAUSE_STATUS.equals(manifestData[i].getCompletionStatus()) ||
	               Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS.equals(manifestData[i].getCompletionStatus()) ||
	               Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS.equals(manifestData[i].getCompletionStatus()) ||
	               Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS.equals(manifestData[i].getCompletionStatus())) {
	            	logger.debug("Subtest " + manifestData[i].getId() + " has appropriate status for restart");
	            	if(!gotRestart && loginResponse.getRestartFlag() && 
	                		(manifestData[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.SYSTEM_STOP_STATUS) || 
	                		 manifestData[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.STUDENT_STOP_STATUS) ||
	                		 manifestData[i].getCompletionStatus().equals(Constants.StudentTestCompletionStatus.IN_PROGRESS_STATUS))) {
	                	ConsolidatedRestartData restartData = loginResponse.addNewConsolidatedRestartData();
	                	ItemResponseData [] itemResponseData = getRestartItemResponses(conn, testRosterId, manifestData[i].getId());
	                    RosterData.generateRestartData(loginResponse, manifestData[i], itemResponseData, restartData);
	                    gotRestart = true;
	                }
	            }
	        }
	        copyManifestDataToResponse(conn, loginResponse, manifestData, testRosterId, authData.getTestAdminId(), accessCode);
	
	        String tutorialResource = getTutorialResource(conn, testRosterId);
	        if (tutorialResource!= null && !tutorialResource.trim().equals("")) {
	        	boolean wasTutorialTaken = wasTutorialTaken(conn, testRosterId);
	            Tutorial tutorial =loginResponse.addNewTutorial();
	            tutorial.setTutorialUrl(tutorialResource);
	            tutorial.setDeliverTutorial(new BigInteger(wasTutorialTaken ? "0":"1"));
	        }
	        RosterData result = new RosterData();
	        result.setDocument(response);
	        result.setAuthData(authData);
	        //com.ctb.tms.bean.login.Manifest manifest = new com.ctb.tms.bean.login.Manifest();
	        //manifest.setManifest(manifestData);
	        //manifest.setTestRosterId(String.valueOf(testRosterId));
	        //result.setManifest(manifest);
	        //logger.debug(response.xmlText());
	        return result;
        } else {
        	return null;
        }
    }
    
    //START Change for Deferred defect 63502
    
    
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
            accommodations.setMaskingRuler("T".equals(accomData.getMaskingRuler()) ? true : false );
            //Changes for magnifying glass user story
            accommodations.setMagnifyingGlass("T".equals(accomData.getMagnifyingGlass()) ? true : false );
            accommodations.setAuditoryCalming("T".equals(accomData.getAuditoryCalming()) ? true : false );
            if(accomData.getMusicFileId() != null){
            	accommodations.setMusicFileId(BigInteger.valueOf(accomData.getMusicFileId()));
            }
            accommodations.setExtendedTime("T".equals(accomData.getExtendedTime()) ? true : false );
            //Change for Masking tool
            accommodations.setMaskingTool("T".equals(accomData.getMaskingTool()) ? true : false );

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
        // TODO: calculate 'ultimate access code' without DB call
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
        	manifest.setBlockContentDownload("T".equals(manifestData[i].getBlockDownload())?"true":"false");
        	/*	if(response.getRestartFlag() && "T".equals(isUltimateAccessCode) 
        				&& Constants.StudentTestCompletionStatus.COMPLETED_STATUS.equals(data.getCompletionStatus())){
        		continue;
        	}else{*/
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
	            sco.setFwdOnly("T".equals(data.getForwardOnly()) ? true : false);
	            sco.setAdaptive("T".equals(data.getAdaptive()) ? true : false );	            
	            sco.setContentURI(data.getContentURI());
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
	
	private static AuthenticationData authenticateStudentByRoster(Connection con, String testRosterId, String accessCode) {
    	AuthenticationData auth = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(REAUTHENTICATE_STUDENT_SQL);
			stmt1.setString(1, testRosterId);
			stmt1.setString(2, accessCode);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				auth = new AuthenticationData();
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
				auth.setLastAccessCode(rs1.getString("accessCode"));
				// addl fields for manifest update
				auth.setCorrelationId(rs1.getInt("correlationId"));
				int mseq = rs1.getInt("lastMseq");
				if(mseq == 0) mseq = -1;
				auth.setLastMseq(mseq);
				Timestamp start = rs1.getTimestamp("startTime");
    			if(start != null) {
    				auth.setStartTime(start);
    			}
    			Timestamp end = rs1.getTimestamp("endTime");
    			if(end != null) {
    				auth.setEndTime(end);
    			}
			} else {
				logger.debug("No auth data found for roster " + testRosterId + ", access code " + accessCode);
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
		return auth;
	}
	
	private ManifestData[] getManifest(Connection con, String testRosterId, String accessCode) throws IOException, ClassNotFoundException {
		logger.debug("Looking for manifest for roster " + testRosterId + ", accessCode " + accessCode);
		com.ctb.tms.bean.login.Manifest[] manifests = getManifest(con, testRosterId);
		if(manifests != null) {
			for(int i=0;i<manifests.length;i++) {
				com.ctb.tms.bean.login.Manifest manifest = manifests[i];
				logger.debug("     Comparing access code: " + manifest.getAccessCode());
				if(accessCode.equalsIgnoreCase(manifest.getAccessCode())) {
					logger.debug("     Found matching manifest! ");
					return manifest.getManifest();
				}
			}
		}
		return null;
	}
	
	public com.ctb.tms.bean.login.Manifest[] getManifest(Connection con, String testRosterId) {
    	com.ctb.tms.bean.login.Manifest[] manifests = null;
    	PreparedStatement stmt1 = null;
    	HashMap<String,ArrayList> manifestMap = new HashMap<String,ArrayList>(10);
    	try {
			stmt1 = con.prepareStatement(MANIFEST_SQL);
			stmt1.setString(1, testRosterId);
			ResultSet rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				String accessCode = rs1.getString("accessCode");
				ArrayList<ManifestData> dataList = manifestMap.get(accessCode);
				if(dataList == null) {
					dataList = new ArrayList<ManifestData>();
				}
				ManifestData manifest = new ManifestData();
				manifest.setAccessCode(accessCode);
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
				manifest.setForwardOnly(rs1.getString("forwardOnly"));
				manifest.setAdaptive(rs1.getString("adaptive"));
				manifest.setScorable(rs1.getString("scorable"));
				manifest.setProduct(rs1.getString("product"));
				manifest.setForm(rs1.getString("subtestForm"));
				manifest.setLevel(rs1.getString("subtestLevel"));
				manifest.setRawScore(rs1.getInt("raw_score"));
    			manifest.setMaxScore(rs1.getInt("max_score"));
    			manifest.setUnscored(rs1.getInt("unscored"));
    			manifest.setAbilityScore(rs1.getInt("ability_score"));
    			manifest.setSemScore(rs1.getInt("sem_score"));
    			manifest.setObjectiveScore(rs1.getString("objective_score"));
    			Timestamp start = rs1.getTimestamp("start_date_time");
    			if(start != null) {
    				manifest.setStartTime(start.getTime());
    			}
    			Timestamp end = rs1.getTimestamp("completion_date_time");
    			if(end != null) {
    				manifest.setEndTime(end.getTime());
    			}
				manifest.setContentURI(rs1.getString("contentURI"));
				manifest.setRecommendedLevel(rs1.getString("recommended_level"));
				manifest.setBlockDownload(rs1.getString("blockDownload"));
				dataList.add(manifest);
				manifestMap.put(accessCode, dataList);
			}
			rs1.close();
			manifests = new com.ctb.tms.bean.login.Manifest[manifestMap.keySet().size()];
			Iterator<String> it = manifestMap.keySet().iterator();
			int i=0;
			while(it.hasNext()) {
				String accessCode = it.next();
				ArrayList<ManifestData> manifestList = manifestMap.get(accessCode);
				ManifestData[] data = (ManifestData[]) manifestList.toArray(new ManifestData[0]);
				com.ctb.tms.bean.login.Manifest manifest = new com.ctb.tms.bean.login.Manifest();
				manifest.setManifest(data);
				manifest.setTestRosterId(testRosterId);
				manifest.setAccessCode(accessCode);
				// fill in roster data for updated manifests
				AuthenticationData auth = authenticateStudentByRoster(con, testRosterId, accessCode);
				if(auth != null) {
					manifest.setRosterLastMseq(auth.getLastMseq());
					manifest.setRosterRestartNumber(auth.getRestartNumber());
					manifest.setRosterCorrelationId(auth.getCorrelationId());
					manifest.setRandomDistractorSeed(auth.getRandomDistractorSeedNumber());
					manifest.setRosterCompletionStatus(auth.getRosterTestCompletionStatus());
					manifest.setRosterStartTime(auth.getStartTime().getTime());
					manifest.setRosterEndTime(auth.getEndTime().getTime());
					manifests[i] = manifest;
				} else {
					return null;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		return manifests;
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
				data.setMaskingRuler(rs1.getString("maskingRuler"));
				data.setMagnifyingGlass(rs1.getString("magnifyingGlass")); 
				data.setAuditoryCalming(rs1.getString("auditoryCalming"));
				data.setMusicFileId(rs1.getInt("musicFileId"));
				data.setExtendedTime(rs1.getString("extendedTime"));
				data.setMaskingTool(rs1.getString("maskingTool"));
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
				response.setConstructedResponse(rs1.getString("constructedResponse"));
				response.setEid(rs1.getInt("eid"));
				response.setItemId(rs1.getString("itemId"));
				response.setItemSortOrder(rs1.getInt("itemSortOrder"));
				response.setItemType(rs1.getString("itemType"));
				response.setResponse(rs1.getString("response"));
				response.setResponseElapsedTime(rs1.getInt("responseElapsedTime"));
				response.setResponseSeqNum(rs1.getString("responseSeqNum"));
				response.setScore(rs1.getInt("score"));
				response.setStudentMarked(rs1.getString("studentMarked"));
				response.setConstructedResponse(clobToString(rs1.getClob("constructedResponse")));
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
		logger.debug("Found " + data.length + " item responses in db for subtest " + itemSetId);
		return data;
	}
	
	private static String clobToString(Clob clb) throws IOException, SQLException {
		if (clb == null) return  "";
	            
	    StringBuffer str = new StringBuffer();
	    String strng;
	    
	    BufferedReader bufferRead = new BufferedReader(clb.getCharacterStream());
	   
	    while ((strng=bufferRead.readLine())!=null) str.append(strng);
	    
	    return str.toString();     
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
			stmt1.setInt(2, testAdminId);
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
				data.setLogoURI(rs1.getString("resource_uri"));
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
