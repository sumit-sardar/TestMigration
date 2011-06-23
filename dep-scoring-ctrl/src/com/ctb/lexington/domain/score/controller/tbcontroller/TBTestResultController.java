package com.ctb.lexington.domain.score.controller.tbcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.ctb.lexington.db.constants.ATSDatabaseConstants;
import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.OrgNodeData;
import com.ctb.lexington.db.data.ReportingLevels;
import com.ctb.lexington.db.data.ScoreMoveData;
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import com.ctb.lexington.db.data.StudentData;
import com.ctb.lexington.db.data.StudentDemographicData;
import com.ctb.lexington.db.data.StudentItemResponseData;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.StudentTestDetails;
import com.ctb.lexington.db.data.UserData;
import com.ctb.lexington.db.data.ValidatedScoreRecord;
import com.ctb.lexington.db.irsdata.IrsDemographicData;
import com.ctb.lexington.db.mapper.StsTestResultFactRecordMapper;
import com.ctb.lexington.db.mapper.StsTotalStudentScoreRecordMapper;
import com.ctb.lexington.domain.score.controller.TestResultController;
import com.ctb.lexington.domain.score.controller.AdminController;
import com.ctb.lexington.domain.score.controller.CurriculumController;
import com.ctb.lexington.domain.score.controller.OrgNodeController;
import com.ctb.lexington.domain.score.controller.StudentController;
import com.ctb.lexington.domain.teststructure.ValidationStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.Timer;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class TBTestResultController implements TestResultController {
    private Connection conn;
    private ScoreMoveData data;
    private ReportingLevels reportingLevels;

    public TBTestResultController(Connection conn, ScoreMoveData data, ReportingLevels reportingLevels) {
        this.conn = conn;
        this.data = data;
        this.reportingLevels = reportingLevels;
    }

    public void run(ValidationStatus rosterValidationStatus) throws IOException, DataException, CTBSystemException, SQLException {
        // gather collected data
    	IrsDemographicData demographicData = getIrsDemographics(data.getDemographicData());
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
        StudentPredictedScoresData predictedData = data.getStudentPredictedScoresData();
        StudentItemResponseData studentItemResponseData = data.getStudentItemResponseData();
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
        context.setGradeId(new Long(studentData.getGrade().equals("AD")?1:2));
        context.setAssessmentId(adminData.getAssessmentId());
        context.setAssessmentType(adminData.getAssessmentType());
        context.setProgramId(adminData.getProgramId());
        context.setDemographicData(demographicData);
        
        new CurriculumController(conn, curriculumData, adminData, context).run();
        System.out.println("***** SCORING: Persisted dimension data.");
        
        // persist scores
        new StudentPredictedScoresController(conn, predictedData, curriculumData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted predicted fact data.");
        new StudentCompositeScoresController(conn, totalStudentScoreData, predictedData, curriculumData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted composite fact data.");
        new StudentContentAreaScoresController(conn, studentSubtestScoresData, factData, curriculumData, testData, adminData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted content area fact data.");
        new StudentObjectiveScoresController(conn, studentScoreSummaryData, curriculumData, testData, adminData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted objective fact data.");
        new StudentItemScoresController(conn, studentItemScoreData, studentItemResponseData, curriculumData, testData, adminData, context).run();
        System.out.println("***** SCORING: TestResultController: Persisted item fact data.");
    
        new StudentResultStatusController(conn, context).run();
        System.out.println("***** SCORING: Marked prior results non-current as necessary.");
    }
    
    public IrsDemographicData getIrsDemographics(StudentDemographicData data){
    		final IrsDemographicData details = new IrsDemographicData();
    		Map rd = data.getResearchData();
        
    		if(rd.containsKey("ELL")) {
    		ArrayList<String> lell = (ArrayList<String>)rd.get("ELL");
	    		String ELL = lell.toString();
	    		details.setAttr1Id(new Long(("Yes".equals(ELL) || ("Y".equals(ELL)))?1:
	                                    ("True".equals(ELL) || ("T".equals(ELL)))?1:2));
    		}
    	 
    		if(rd.containsKey("Ethnicity")) {
    		ArrayList<String> lethnicity = (ArrayList<String>)rd.get("Ethnicity");
	    		String ethnicity = lethnicity.toString();
	    		details.setAttr2Id(new Long(("Asian or Pacific Islander".equals(ethnicity))?1:
	                                    ("Asian/Pacific Islander".equals(ethnicity))?1:
	                                    ("American Indian or Alaska Native".equals(ethnicity))?2:
	                                    ("African American or Black".equals(ethnicity))?3:
	                                    ("Hispanic or Latino".equals(ethnicity))?4:
	                                    ("Caucasian".equals(ethnicity))?5:
	                                    ("Multi-ethnic".equals(ethnicity))?6:7));
    		}
        
    		if(rd.containsKey("Free Lunch")) {
    		ArrayList<String> lunch = (ArrayList<String>)rd.get("Free Lunch");
	    		String freelunch = lunch.toString();
	    		details.setAttr3Id(new Long(("Yes".equals(freelunch) || ("Y".equals(freelunch)))?1:
	                                    ("True".equals(freelunch) || ("T".equals(freelunch)))?1:2));
    		}
    		details.setAttr4Id(new Long(("Male".equals(data.getGender()))?1:
    								("Female".equals(data.getGender()))?2:
                                    ("M".equals(data.getGender()))?1:
                                    ("F".equals(data.getGender()))?2:3));

    	
    		if(rd.containsKey("IEP")) {
    		ArrayList<String> iepl = (ArrayList<String>)rd.get("IEP");
	    			String iep = iepl.toString();
	    		details.setAttr5Id(new Long(("Yes".equals(iep) || ("Y".equals(iep)))?1:
	                                    ("True".equals(iep) || ("T".equals(iep)))?1:2));
    		}
                                    
    		if(rd.containsKey("Labor Force Status")) {
    		ArrayList<String> labour = (ArrayList<String>)rd.get("Labor Force Status");
    			String lfstat = labour.toString();
	    		details.setAttr6Id(new Long(("Employed".equals(lfstat))?1:
	                                    ("Unemployed".equals(lfstat))?2:
	                                    ("Not in Labor Force".equals(lfstat))?3:4));
    		}
    	
    		if(rd.containsKey("LEP")) {
    		ArrayList<String> llep = (ArrayList<String>)rd.get("LEP");
    			String lep = llep.toString();
    			details.setAttr7Id(new Long(("Yes".equals(lep) || ("Y".equals(lep)))?1:
                                    	("True".equals(lep) || ("T".equals(lep)))?1:2));
    		}
        
    		if(rd.containsKey("Migrant")) {
    		ArrayList<String> mig = (ArrayList<String>)rd.get("Migrant");
    			String migrant = mig.toString();
    			details.setAttr8Id(new Long(("Yes".equals(migrant) || "Y".equals(migrant))?1:
                                    	("True".equals(migrant) || "T".equals(migrant))?1:2));
    		}
        
    		details.setAttr9Id(new Long(("Y".equals(data.getScreenMagnifier()))?1:
            					("T".equals(data.getScreenMagnifier()))?1:2));
    		details.setAttr11Id(new Long(("Y".equals(data.getScreenReader()))?1:
								("T".equals(data.getScreenReader()))?1:2));
    		details.setAttr12Id(new Long(("Y".equals(data.getCalculator()))?1:
								("T".equals(data.getCalculator()))?1:2));
    		details.setAttr13Id(new Long(("Y".equals(data.getTestPause()))?1:
								("T".equals(data.getTestPause()))?1:2));
    		details.setAttr14Id(new Long(("Y".equals(data.getUntimedTest()))?1:
								("T".equals(data.getUntimedTest()))?1:2));
    		details.setAttr15Id(new Long((data.getQuestionBGColor() != null || data.getQuestionFontColor() != null)?1:2));
    		details.setAttr16Id(new Long((data.getQuestionFontSize() != null)?1:2));
    	
    		if(rd.containsKey("Section 504")) {
    		ArrayList<String> section = (ArrayList<String>)rd.get("Section 504");
    			String sec504 = section.toString();
    			details.setAttr10Id(new Long(("Yes".equals(sec504) || "Y".equals(sec504))?1:
                                    	("True".equals(sec504) || "T".equals(sec504))?1:2));
    		}
         
    	 return details;
    }
}