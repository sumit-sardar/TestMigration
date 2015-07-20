package com.ctb.lexington.domain.score.controller.trcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.TimeZone;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.OrgNodeData;
import com.ctb.lexington.db.data.ReportingLevels;
import com.ctb.lexington.db.data.ScoreMoveData;
import com.ctb.lexington.db.data.StudentData;
import com.ctb.lexington.db.data.StudentDemographicData;
import com.ctb.lexington.db.data.StudentItemResponseData;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.irsdata.IrsDemographicData;
import com.ctb.lexington.domain.score.controller.AdminController;
import com.ctb.lexington.domain.score.controller.CurriculumController;
import com.ctb.lexington.domain.score.controller.OrgNodeController;
import com.ctb.lexington.domain.score.controller.StudentController;
import com.ctb.lexington.domain.score.controller.TestResultController;
import com.ctb.lexington.domain.teststructure.ValidationStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;

@SuppressWarnings({"unused", "unchecked"})
public class TRTestResultController implements TestResultController {
    private Connection conn;
    private ScoreMoveData data;
    private ReportingLevels reportingLevels;

    public TRTestResultController(Connection conn, ScoreMoveData data, ReportingLevels reportingLevels) {
        this.conn = conn;
        this.data = data;
        this.reportingLevels = reportingLevels;
    }

    public void run(ValidationStatus rosterValidationStatus) throws IOException, DataException, CTBSystemException, SQLException {
        // gather collected data
    	//IrsDemographicData demographicData = getIrsDemographics(data.getDemographicData());
        OrgNodeData orgNodeData = data.getOrgNodeData();
        StudentData studentData = data.getStudentData();
        AdminData adminData = data.getAdminData();
        CurriculumData curriculumData = data.getCurriculumData();
        StudentTestData testData = data.getTestData();
        StudentItemScoreData studentItemScoreData = data.getStudentItemScoreData();
        StudentItemResponseData studentItemResponseData = data.getStudentItemResponseData();
        StudentItemScoreData studentfieldTestTEItemScoreData = data.getStudentfieldTestTEItemScoreData();
        boolean isRetryFieldTestTE = data.isRetryFieldTestTE();
        
        /*StsTestResultFactData factData = data.getStsTestResultFactData();
        UserData userData = data.getUserData();
        StsTotalStudentScoreData totalStudentScoreData = data.getStsTotalStudentScoreData();
        StudentSubtestScoresData studentSubtestScoresData = data.getStudentSubtestScoresData();
        StudentScoreSummaryData studentScoreSummaryData = data.getStudentScoreSummaryData();
        */
                
        // persist context
        new OrgNodeController(conn, orgNodeData, adminData).run();
        new StudentController(conn, studentData).run();
        new AdminController(conn, adminData, curriculumData, new Long(studentData.getGrade().equals("AD")?1:2)).run();
        
        // gather context dim ids for fact records
        ContextData context = new ContextData();
        
        Timestamp completionTime = testData.getMaxCompletionTime(adminData.getTimeZone());
        if(completionTime == null) {
            int tzOffset = TimeZone.getTimeZone(adminData.getTimeZone()).getOffset(System.currentTimeMillis());
            completionTime = new Timestamp(System.currentTimeMillis() + tzOffset);
        }
                
        context.setTestStartTimestamp(studentData.getStartDateTime());
        context.setTestCompletionTimestamp(completionTime);
        context.setOrgNodeId(orgNodeData.getOrgNodeId());
        context.setStudentId(studentData.getOasStudentId());
        context.setSessionId(adminData.getSessionId());
        context.setCurrentResultId(new Long(rosterValidationStatus.isValid()?1:2));
        context.setGradeId(new Long(studentData.getGrade().equals("AD")?31:32));
        context.setAssessmentId(adminData.getAssessmentId());
        context.setAssessmentType(adminData.getAssessmentType());
        context.setProgramId(adminData.getProgramId());
        
        if(isRetryFieldTestTE) {
        	new CurriculumController(conn, curriculumData, adminData, context).runFTTE();
        	System.out.println("***** SCORING: Persisted dimension data for field test TE item.");
        	
        	new StudentItemScoresController(conn, studentItemScoreData, studentfieldTestTEItemScoreData, studentItemResponseData, curriculumData, testData, adminData, context, isRetryFieldTestTE).run();
            System.out.println("***** SCORING: TestResultController: Persisted item fact data only for field test TE item.");
        } else {
	        new CurriculumController(conn, curriculumData, adminData, context).run();
	        System.out.println("***** SCORING: Persisted dimension data.");
	        
	        // persist scores
	        /*  new StudentContentAreaScoresController(conn, studentSubtestScoresData, factData, curriculumData, testData, adminData, context).run();
	        System.out.println("***** SCORING: TestResultController: Persisted content area fact data.");
	        
	        new StudentObjectiveScoresController(conn, studentScoreSummaryData, curriculumData, testData, adminData, context).run(); 
	        System.out.println("***** SCORING: TestResultController: Persisted objective fact data."); */
	        
	      	new StudentItemScoresController(conn, studentItemScoreData, studentfieldTestTEItemScoreData, studentItemResponseData, curriculumData, testData, adminData, context, isRetryFieldTestTE).run();
	        System.out.println("***** SCORING: TestResultController: Persisted item fact data.");
	    
	        new StudentResultStatusController(conn, context).run();
	        System.out.println("***** SCORING: Marked prior results non-current as necessary.");
        }
    }
    
    public IrsDemographicData getIrsDemographics(StudentDemographicData data){
    	IrsDemographicData details = new IrsDemographicData();
    	return details;
    }
	
}