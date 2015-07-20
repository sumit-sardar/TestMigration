package com.ctb.lexington.domain.score.controller.tscontroller;

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
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StudentData;
import com.ctb.lexington.db.data.StudentDemographicData;
import com.ctb.lexington.db.data.StudentItemResponseData;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.UserData;
import com.ctb.lexington.db.irsdata.IrsDemographicData;
import com.ctb.lexington.domain.score.controller.AdminController;
import com.ctb.lexington.domain.score.controller.CurriculumController;
import com.ctb.lexington.domain.score.controller.OrgNodeController;
import com.ctb.lexington.domain.score.controller.StudentController;
import com.ctb.lexington.domain.score.controller.TestResultController;
import com.ctb.lexington.domain.teststructure.ValidationStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;
import com.ctb.prism.web.handler.PrismWebServiceHandler;

public class TSTestResultController implements TestResultController {
    private Connection conn;
    private ScoreMoveData data;
    private ReportingLevels reportingLevels;

    public TSTestResultController(Connection conn, ScoreMoveData data, ReportingLevels reportingLevels) {
        this.conn = conn;
        this.data = data;
        this.reportingLevels = reportingLevels;
    }

    public void run(ValidationStatus rosterValidationStatus) throws IOException, DataException, CTBSystemException, SQLException {
        
    	// gather collected data
        OrgNodeData orgNodeData = data.getOrgNodeData();
        StudentData studentData = data.getStudentData();
        AdminData adminData = data.getAdminData();
        CurriculumData curriculumData = data.getCurriculumData();
        StudentTestData testData = data.getTestData();
        StudentSubtestScoresData studentSubtestScoresData = data.getStudentSubtestScoresData();
        StudentScoreSummaryData studentScoreSummaryData = data.getStudentScoreSummaryData();
        StudentItemScoreData studentItemScoreData = data.getStudentItemScoreData();
        StsTestResultFactData factData = data.getStsTestResultFactData();
        UserData userData = data.getUserData();
        StsTotalStudentScoreData totalStudentScoreData = data.getStsTotalStudentScoreData();
        StudentItemResponseData studentItemResponseData = data.getStudentItemResponseData();
        StudentItemScoreData studentfieldTestTEItemScoreData = data.getStudentfieldTestTEItemScoreData();
        boolean isRetryFieldTestTE = data.isRetryFieldTestTE();
        
        // persist context
        new OrgNodeController(conn, orgNodeData, adminData).run();
        new StudentController(conn, studentData).run();
        new AdminController(conn, adminData, curriculumData, new Long(studentData.getGrade().equals("AD")?31:32)).run();
        
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
	        new StudentCompositeScoresController(conn, totalStudentScoreData, curriculumData, context).run();
	        System.out.println("***** SCORING: TestResultController: Persisted composite fact data.");
	        new StudentContentAreaScoresController(conn, studentSubtestScoresData, factData, curriculumData, testData, adminData, context).run();
	        System.out.println("***** SCORING: TestResultController: Persisted content area fact data.");
	        new StudentObjectiveScoresController(conn, factData, studentScoreSummaryData, curriculumData, testData, adminData, context).run();
	        System.out.println("***** SCORING: TestResultController: Persisted objective fact data.");
	        new StudentItemScoresController(conn, studentItemScoreData, studentfieldTestTEItemScoreData, studentItemResponseData, curriculumData, testData, adminData, context, isRetryFieldTestTE).run();
	        System.out.println("***** SCORING: TestResultController: Persisted item fact data.");
	    
	        new StudentResultStatusController(conn, context).run();
	        System.out.println("***** SCORING: Marked prior results non-current as necessary.");
	        
	        //Prism web service is called for scoring
	        try {
	        	PrismWebServiceHandler.scoring(data.getTestRosterId(), studentData.getOasStudentId().intValue(), adminData.getSessionId(), 0, 0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
    
    public IrsDemographicData getIrsDemographics(StudentDemographicData data){
    	final IrsDemographicData details = new IrsDemographicData();
    	return details;
    }
}