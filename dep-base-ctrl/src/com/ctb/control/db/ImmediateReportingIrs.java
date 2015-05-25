package com.ctb.control.db;

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testAdmin.ItemResponseAndScore;
import com.ctb.bean.testAdmin.StudentReportIrsScore;
import java.sql.Clob;
import com.ctb.bean.testAdmin.LiteracyProExportData;

/** 
 * Defines a new database control. 
 * 
 * The @jc:connection tag indicates which WebLogic data source will be used by 
 * this database control. Please change this to suit your needs. You can see a 
 * list of available data sources by going to the WebLogic console in a browser 
 * (typically http://localhost:7001/console) and clicking Services, JDBC, 
 * Data Sources. 
 *
 * @author TCS
 * 
 * @jc:connection data-source-jndi-name="irsDataSource" 
 */ 

@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "irsDataSource")
public interface ImmediateReportingIrs extends JdbcControl{
	
	static final long serialVersionUID = 1L;
	
	@JdbcControl.SQL(statement = "select proficency_level from laslink_composite_fact where studentid = {studentId} and sessionid = {testAdminId}")
            String getProficiencyLevel(Integer studentId, Integer testAdminId) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT CAD.NAME AS CONTENTAREANAME, LCAF.POINTS_OBTAINED AS RAWSCORE, LCAF.SCALE_SCORE AS SCALESCORE, LCAF.PROFICENCY_LEVEL AS PROFICIENCYLEVEL FROM CONTENT_AREA_DIM CAD, LASLINK_CONTENT_AREA_FACT LCAF WHERE LCAF.STUDENTID = {studentId} AND LCAF.SESSIONID = {testAdminId} AND CAD.CONTENT_AREAID = LCAF.CONTENT_AREAID")
	StudentReportIrsScore [] getScoreDataForReport(Integer studentId, Integer testAdminId) throws SQLException;

	@JdbcControl.SQL(statement = "SELECT 'Overall' AS CONTENTAREANAME, LCF.POINTS_OBTAINED AS RAWSCORE, LCF.SCALE_SCORE AS SCALESCORE, LCF.PROFICENCY_LEVEL AS PROFICIENCYLEVEL FROM LASLINK_COMPOSITE_FACT LCF WHERE LCF.STUDENTID = {studentId} AND LCF.SESSIONID = {testAdminId}")
	StudentReportIrsScore getScoreDataForReportComposite(Integer studentId, Integer testAdminId) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT DISTINCT DERIEVED.TESTROSTERID, DERIEVED.CONTENTAREANAME, DERIEVED.RAWSCORE, DERIEVED.SCALESCORE, DERIEVED.PROFICIENCYLEVEL FROM (SELECT ROS.TEST_ROSTER_ID     AS TESTROSTERID, CAD.NAME              AS CONTENTAREANAME, LCAF.POINTS_OBTAINED  AS RAWSCORE, LCAF.SCALE_SCORE      AS SCALESCORE, LCAF.PROFICENCY_LEVEL AS PROFICIENCYLEVEL FROM CONTENT_AREA_DIM          CAD, LASLINK_CONTENT_AREA_FACT LCAF, LASLINK_COMPOSITE_FACT    LCF, TEST_ROSTER@OAS           ROS WHERE LCAF.STUDENTID = ROS.STUDENT_ID AND LCAF.SESSIONID = ROS.TEST_ADMIN_ID AND CAD.CONTENT_AREAID = LCAF.CONTENT_AREAID AND {sql: testrosterIds} UNION ALL SELECT ROS.TEST_ROSTER_ID AS TESTROSTERID, 'Overall' AS CONTENTAREANAME, LCF.POINTS_OBTAINED AS RAWSCORE, LCF.SCALE_SCORE AS SCALESCORE, LCF.PROFICENCY_LEVEL AS PROFICIENCYLEVEL FROM LASLINK_COMPOSITE_FACT LCF, TEST_ROSTER@OAS ROS WHERE LCF.STUDENTID = ROS.STUDENT_ID AND LCF.SESSIONID = ROS.TEST_ADMIN_ID AND {sql: testrosterIds}) DERIEVED ORDER BY DERIEVED.TESTROSTERID",arrayMaxLength = 0, fetchSize = 100)
	StudentReportIrsScore [] getScoreDataForReportByGroup(String testrosterIds) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT SOD.NAME  AS contentAreaName, LSOF.POINTS_OBTAINED  AS PtsObtained, LSOF.POINTS_POSSIBLE  AS PtsPossible, LSOF.PERCENT_OBTAINED AS PerCorrect FROM SEC_OBJ_DIM SOD, LASLINK_SEC_OBJ_FACT LSOF WHERE LSOF.STUDENTID = {studentId} AND LSOF.SESSIONID = {testAdminId} AND SOD.SEC_OBJID = LSOF.SEC_OBJID")
	StudentReportIrsScore [] getScoreDataForAcademicReport(Integer studentId, Integer testAdminId) throws SQLException;
	
	@JdbcControl.SQL(statement = "call wipeout_scoring_after_reset({studentIds},{testAdminId},{contentAreaId}, {contentAreaName})")
	void wipeOutStudentsScoring(Clob studentIds, Integer testAdminId, Long contentAreaId, String contentAreaName) throws SQLException;
	
	@JdbcControl.SQL(statement = "call wipeout_scoring_after_reset_ts({studentIds},{testAdminId},{contentAreaId}, {contentAreaName})")
	void wipeOutStudentsScoringForTASC(Clob studentIds, Long testAdminId, Long contentAreaId, String contentAreaName) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT IDIM.OAS_ITEMID AS itemId, TIF.POINTS_OBTAINED AS rawScore, TIF.POINTS_POSSIBLE AS possibleScore FROM {sql: scoreTableName} TIF, ITEM_DIM IDIM WHERE IDIM.ITEMID = TIF.ITEMID AND TIF.SESSIONID = {testAdminId} AND TIF.STUDENTID = {studentId} AND TIF.POINTS_OBTAINED IS NOT NULL",arrayMaxLength = 0, fetchSize=10)
	ItemResponseAndScore[] getObtainedScoreForItem(String scoreTableName, Integer studentId, Integer testAdminId) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT REPORTING_LEVEL AS subtest, SESSIONID AS sessionID, STUDENTID AS oasStudentId, REPORTING_LEVEL_SS AS scaledScore, REPORTING_LEVEL_GE AS gLE, REPORT_LEVEL_ID AS reportingLevelId FROM (SELECT CAD.NAME AS REPORTING_LEVEL, TBCONFACT.SESSIONID AS SESSIONID, TBCONFACT.STUDENTID AS STUDENTID, TBCONFACT.SCALE_SCORE AS REPORTING_LEVEL_SS, TBCONFACT.GRADE_EQUIVALENT AS REPORTING_LEVEL_GE, TBCONFACT.SESSIONID || TBCONFACT.STUDENTID || TBCONFACT.CONTENT_AREAID AS REPORT_LEVEL_ID FROM TABE_CONTENT_AREA_FACT TBCONFACT, CONTENT_AREA_DIM CAD WHERE {sql: contentFact} AND CAD.CONTENT_AREAID = TBCONFACT.CONTENT_AREAID AND CAD.SUBJECTID <> 999999 UNION ALL SELECT COMD.NAME AS REPORTING_LEVEL, TBCOMP.SESSIONID AS SESSIONID, TBCOMP.STUDENTID AS STUDENTID, TBCOMP.SCALE_SCORE AS REPORTING_LEVEL_SS, TBCOMP.GRADE_EQUIVALENT AS REPORTING_LEVEL_GE, TBCOMP.SESSIONID || TBCOMP.STUDENTID || TBCOMP.COMPOSITEID AS REPORT_LEVEL_ID FROM TABE_COMPOSITE_FACT TBCOMP, COMPOSITE_DIM COMD, ASSESSMENT_DIM TC WHERE  {sql: compositeFact} AND COMD.COMPOSITEID = TBCOMP.COMPOSITEID AND TC.ASSESSMENTID = TBCOMP.ASSESSMENTID AND TC.TYPE <> 'TL')", arrayMaxLength = 0, fetchSize = 100)
	LiteracyProExportData[] getBulkReportCSVData(String contentFact, String compositeFact) throws SQLException;
}
