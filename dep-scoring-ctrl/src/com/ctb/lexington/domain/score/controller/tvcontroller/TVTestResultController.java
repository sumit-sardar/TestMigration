package com.ctb.lexington.domain.score.controller.tvcontroller;

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
import com.ctb.lexington.db.data.StudentItemResponseData;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.StudentTestDetails;
import com.ctb.lexington.db.data.UserData;
import com.ctb.lexington.db.data.StudentDemographicData;
import com.ctb.lexington.db.data.ValidatedScoreRecord;
import com.ctb.lexington.db.data.WsReportingDataTV;
import com.ctb.lexington.db.irsdata.IrsDemographicData;
import com.ctb.lexington.db.mapper.StsTestResultFactRecordMapper;
import com.ctb.lexington.db.mapper.StsTotalStudentScoreRecordMapper;
import com.ctb.lexington.domain.score.controller.AdminController;
import com.ctb.lexington.domain.score.controller.CurriculumController;
import com.ctb.lexington.domain.score.controller.OrgNodeController;
import com.ctb.lexington.domain.score.controller.StudentController;
import com.ctb.lexington.domain.score.controller.TestResultController;
import com.ctb.lexington.domain.teststructure.ValidationStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.Timer;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class TVTestResultController implements TestResultController {
    private Connection conn;
    private ScoreMoveData data;
    private ReportingLevels reportingLevels;

    public TVTestResultController(Connection conn, ScoreMoveData data, ReportingLevels reportingLevels) {
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
        context.setGradeId(new Long(
                           studentData.getGrade().equals("3")?3:
                           studentData.getGrade().equals("4")?4:
                           studentData.getGrade().equals("5")?5:
                           studentData.getGrade().equals("6")?6:
                           studentData.getGrade().equals("7")?7:
                           studentData.getGrade().equals("8")?8:
                           studentData.getGrade().equals("9")?9:
                           studentData.getGrade().equals("10")?10:
                           studentData.getGrade().equals("11")?11:
                           studentData.getGrade().equals("12")?12:13));
        context.setAssessmentId(adminData.getAssessmentId());
        context.setAssessmentType(adminData.getAssessmentType());
        context.setProgramId(adminData.getProgramId());
        context.setDemographicData(demographicData);
        
        new CurriculumController(conn, curriculumData, adminData, context).run();
        System.out.println("***** SCORING: Persisted dimension data.");
        
        // persist scores
     //   new StudentPredictedScoresController(conn, predictedData, curriculumData, context).run();
        
        WsReportingDataTV wsTvReportData = new WsReportingDataTV();
        wsTvReportData.setStudentid(studentData.getOasStudentId());
        
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
        
        new TVWsAcuityDataController(curriculumData, context, factData, wsTvReportData, studentScoreSummaryData);
        System.out.println("Stop");
        
    }
    
    public IrsDemographicData getIrsDemographics(StudentDemographicData data){
    		final IrsDemographicData details = new IrsDemographicData();
    		Map rd = data.getResearchData();
        
    		details.setAttr1Id(new Long(4));
    		if(rd.containsKey("ELL")) {
    			ArrayList<String> lell = (ArrayList<String>)rd.get("ELL");
	    		String ELL = lell.get(0);
	    		details.setAttr1Id(new Long(("Yes".equals(ELL) || ("Y".equals(ELL)))?3:
                                    ("True".equals(ELL) || ("T".equals(ELL)))?3:4));
    		}
    	 
    		details.setAttr2Id(new Long(16));
    		if(rd.containsKey("Ethnicity")) {
    			ArrayList<String> lethnicity = (ArrayList<String>)rd.get("Ethnicity");
	    		String ethnicity = lethnicity.get(0);
	    		details.setAttr2Id(new Long(("African American or Black, Not Hispanic".equals(ethnicity))?8:
                              ("American Indian or Alaska Native".equals(ethnicity))?9:
                              ("Asian".equals(ethnicity))?10:
                              ("Pacific Islander".equals(ethnicity))?11:
                              ("Hispanic or Latino".equals(ethnicity))?12:
                              ("White, Not Hispanic".equals(ethnicity))?13:
                              ("Multi-ethnic".equals(ethnicity))?14:
                              ("Other".equals(ethnicity))?15:16));
    		}
    		
    		details.setAttr3Id(new Long(4));
    		if(rd.containsKey("Free Lunch")) {
    			ArrayList<String> lunch = (ArrayList<String>)rd.get("Free Lunch");
	    		String freelunch = lunch.get(0);
	    		details.setAttr3Id(new Long(("Yes".equals(freelunch) || ("Y".equals(freelunch)))?3:
                                    ("True".equals(freelunch) || ("T".equals(freelunch)))?3:4));
    		}
    		details.setAttr4Id(new Long(("Male".equals(data.getGender()))?5:
    								("Female".equals(data.getGender()))?4:
                                    ("M".equals(data.getGender()))?5:
                                    ("F".equals(data.getGender()))?4:6));
    	
    		details.setAttr5Id(new Long(4));
    		if(rd.containsKey("IEP")) {
    			ArrayList<String> iepl = (ArrayList<String>)rd.get("IEP");
	    			String iep = iepl.get(0);
	    			details.setAttr5Id(new Long(("Yes".equals(iep) || ("Y".equals(iep)))?3:
                                    ("True".equals(iep) || ("T".equals(iep)))?3:4));
    		}
                                    
    		details.setAttr6Id(new Long(9));
    		
    		details.setAttr7Id(new Long(4));
    		if(rd.containsKey("LEP")) {
    			ArrayList<String> llep = (ArrayList<String>)rd.get("LEP");
    			String lep = llep.get(0);
    			details.setAttr7Id(new Long(("Yes".equals(lep) || ("Y".equals(lep)))?3:
                                    ("True".equals(lep) || ("T".equals(lep)))?3:4));
    		}
    		
    		details.setAttr8Id(new Long(4));
    		if(rd.containsKey("Migrant")) {        
    			ArrayList<String> mig = (ArrayList<String>)rd.get("Migrant");
    			String migrant = mig.get(0);
    			details.setAttr8Id(new Long(("Yes".equals(migrant) || "Y".equals(migrant))?3:
                                    ("True".equals(migrant) || "T".equals(migrant))?3:4));
    		}
        
    		details.setAttr9Id(new Long(("Y".equals(data.getScreenMagnifier()))?3:
            					("T".equals(data.getScreenMagnifier()))?3:4));
    		details.setAttr11Id(new Long(("Y".equals(data.getScreenReader()))?3:
								("T".equals(data.getScreenReader()))?3:4));
    		details.setAttr12Id(new Long(("Y".equals(data.getCalculator()))?3:
								("T".equals(data.getCalculator()))?3:4));
    		details.setAttr13Id(new Long(("Y".equals(data.getTestPause()))?3:
								("T".equals(data.getTestPause()))?3:4));
    		details.setAttr14Id(new Long(("Y".equals(data.getUntimedTest()))?3:
								("T".equals(data.getUntimedTest()))?3:4));
    		details.setAttr15Id(new Long((data.getQuestionBGColor() != null || data.getQuestionFontColor() != null)?3:4));
    		details.setAttr16Id(new Long((data.getQuestionFontSize() != null)?3:4));
    	
    		details.setAttr10Id(new Long(4));
    		if(rd.containsKey("Section 504")) { 
    			ArrayList<String> section = (ArrayList<String>)rd.get("Section 504");
    			String sec504 = section.get(0);
    			details.setAttr10Id(new Long(("Yes".equals(sec504) || "Y".equals(sec504))?3:
                                    ("True".equals(sec504) || "T".equals(sec504))?3:4));
    		}
         
    	 return details;
    }

}